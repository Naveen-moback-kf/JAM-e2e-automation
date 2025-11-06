package com.JobMapping.utils;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.JobMapping.webdriverManager.DriverManager;

/**
 * Consolidated Screenshot Handler - Unified screenshot management
 * 
 * FUNCTIONALITY:
 * - Captures screenshots on test failures with enhanced context
 * - Provides both low-level and high-level interfaces
 * - Standardizes error handling and logging across test methods
 * - Integrates with ExtentReports for visual test reporting
 * - Organizes screenshots in structured directories
 * 
 * CONSOLIDATED FEATURES:
 * - Screenshot capture with descriptive names and timestamps
 * - Easy-to-use helper methods for test failure scenarios
 * - Automatic ExtentReports integration
 * - Configurable screenshot directory structure
 * - Backward compatibility with existing code
 * 
 * USAGE EXAMPLES:
 * 
 * 1. Basic screenshot capture:
 *    ScreenshotHandler.captureFailureScreenshot("methodName", exception);
 * 
 * 2. Test failure with screenshot:
 *    ScreenshotHandler.handleTestFailure("methodName", exception, "Custom message");
 * 
 * 3. Quick failure with screenshot:
 *    ScreenshotHandler.failWithScreenshot("methodName", "Error occurred");
 * 
 * 4. Non-failing screenshot with log:
 *    ScreenshotHandler.captureAndLog("methodName", "Issue detected");
 */
public class ScreenshotHandler {
    
    private static final Logger LOGGER = LogManager.getLogger(ScreenshotHandler.class);
    
    // Configuration
    private static final String SCREENSHOTS_DIR = "Screenshots";
    private static final String FAILURE_SCREENSHOTS_DIR = "FailureScreenshots";
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    // PERFORMANCE OPTIMIZATION: Screenshot throttling and smart capture
    private static final long SCREENSHOT_THROTTLE_MS = 2000; // Minimum 2 seconds between screenshots
    private static final int MAX_SCREENSHOTS_PER_TEST = 3;   // Maximum screenshots per test method
    private static final java.util.Map<String, Long> lastScreenshotTime = new java.util.concurrent.ConcurrentHashMap<>();
    private static final java.util.Map<String, Integer> screenshotCount = new java.util.concurrent.ConcurrentHashMap<>();
    private static final java.util.concurrent.atomic.AtomicBoolean performanceModeEnabled = 
        new java.util.concurrent.atomic.AtomicBoolean(true); // Default: Performance mode ON
    
    // ========================================
    // CORE SCREENSHOT CAPTURE METHODS
    // ========================================
    
    /**
     * Capture screenshot on test failure with method context
     * 
     * @param methodName The name of the test method that failed
     * @param errorMessage The error message or exception details
     * @return String path to the saved screenshot file
     */
    public static String captureFailureScreenshot(String methodName, String errorMessage) {
        try {
            // PERFORMANCE OPTIMIZATION: Early exit conditions
            if (!shouldCaptureScreenshot(methodName, errorMessage)) {
                LOGGER.debug("Screenshot skipped for performance (method: {}, reason: {})", 
                           methodName, getSkipReason(methodName, errorMessage));
                return null;
            }
            
            WebDriver driver = DriverManager.getDriver();
            
            if (driver == null) {
                LOGGER.warn("WebDriver is null - cannot capture screenshot");
                return null;
            }
            
            if (!isScreenshotEnabled()) {
                LOGGER.debug("Screenshot capture is disabled");
                return null;
            }
            
            // Update throttling counters
            updateScreenshotCounters(methodName);
            
            // PERFORMANCE OPTIMIZATION: Async screenshot capture
            if (isAsyncCaptureEnabled()) {
                return captureScreenshotAsync(methodName, errorMessage, driver);
            } else {
                return captureScreenshotSync(methodName, errorMessage, driver);
            }
            
        } catch (Exception e) {
            LOGGER.error("❌ Failed to capture screenshot for method: {}", methodName, e);
            return null;
        }
    }
    
    /**
     * PERFORMANCE OPTIMIZATION: Synchronous screenshot capture (original behavior)
     */
    private static String captureScreenshotSync(String methodName, String errorMessage, WebDriver driver) {
        try {
            // Create screenshot directory structure
            String screenshotPath = createScreenshotPath(methodName);
            
            // Capture screenshot
            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
            File destFile = new File(screenshotPath);
            
            // Ensure parent directories exist
            destFile.getParentFile().mkdirs();
            
            // Copy screenshot to destination
            FileUtils.copyFile(sourceFile, destFile);
            
            // Log screenshot capture
            LOGGER.info("📸 SCREENSHOT CAPTURED - Method: {} | Path: {}", methodName, screenshotPath);
            
            // Add screenshot to ExtentReports
            addScreenshotToExtentReport(screenshotPath, methodName, errorMessage);
            
            return screenshotPath;
            
        } catch (Exception e) {
            LOGGER.error("❌ Sync screenshot capture failed for method: {}", methodName, e);
            return null;
        }
    }
    
    /**
     * PERFORMANCE OPTIMIZATION: Asynchronous screenshot capture (non-blocking)
     */
    private static String captureScreenshotAsync(String methodName, String errorMessage, WebDriver driver) {
        try {
            String screenshotPath = createScreenshotPath(methodName);
            
            // Quick screenshot capture
            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
            
            // IMPORTANT: Add to ExtentReports SYNCHRONOUSLY (before async processing)
            // ExtentReports uses ThreadLocal which doesn't work in async threads
            addScreenshotToExtentReport(screenshotPath, methodName, errorMessage);
            
            // Process screenshot file operations in background thread (non-blocking)
            java.util.concurrent.CompletableFuture.runAsync(() -> {
                try {
                    File destFile = new File(screenshotPath);
                    destFile.getParentFile().mkdirs();
                    FileUtils.copyFile(sourceFile, destFile);
                    
                    LOGGER.debug("📸 ASYNC SCREENSHOT SAVED - Method: {} | Path: {}", methodName, screenshotPath);
                    
                } catch (Exception e) {
                    LOGGER.warn("Async screenshot processing failed for method: {}", methodName, e);
                }
            });
            
            LOGGER.info("📸 ASYNC SCREENSHOT QUEUED - Method: {}", methodName);
            return screenshotPath;
            
        } catch (Exception e) {
            LOGGER.error("❌ Async screenshot capture failed for method: {}", methodName, e);
            return null;
        }
    }
    
    /**
     * Wrapper method for easy integration with existing catch blocks
     * 
     * @param methodName The test method name
     * @param throwable The caught throwable (exception or error)
     * @return Screenshot path or null if failed
     */
    public static String captureFailureScreenshot(String methodName, Throwable throwable) {
        String errorMessage = throwable != null ? throwable.getMessage() : "Unknown error";
        return captureFailureScreenshot(methodName, errorMessage);
    }
    
    /**
     * Capture screenshot with custom description
     * 
     * @param description Custom description for the screenshot
     * @return String path to the saved screenshot file
     */
    public static String captureScreenshotWithDescription(String description) {
        try {
            WebDriver driver = DriverManager.getDriver();
            
            if (driver == null) {
                LOGGER.warn("WebDriver is null - cannot capture screenshot");
                return null;
            }
            
            String screenshotPath = createScreenshotPathWithDescription(description);
            
            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
            File destFile = new File(screenshotPath);
            
            destFile.getParentFile().mkdirs();
            FileUtils.copyFile(sourceFile, destFile);
            
            LOGGER.info("📸 Screenshot captured: {}", description);
            LOGGER.info("Saved to: {}", screenshotPath);
            
            return screenshotPath;
            
        } catch (Exception e) {
            LOGGER.error("❌ Failed to capture screenshot: {}", description, e);
            return null;
        }
    }
    
    // ========================================
    // HIGH-LEVEL FAILURE HANDLING METHODS
    // ========================================
    
    /**
     * Handle test failure with screenshot capture and standardized logging
     * 
     * @param methodName The name of the test method
     * @param exception The caught exception
     * @param customMessage Custom error message (optional)
     */
    public static void handleTestFailure(String methodName, Exception exception, String customMessage) {
        try {
            // Capture screenshot on failure
            String screenshotPath = captureFailureScreenshot(methodName, exception);
            
            // Print stack trace
            exception.printStackTrace();
            
            // Prepare error message
            String baseMessage = customMessage != null ? customMessage : "Test method failed: " + methodName;
            String errorMsg = baseMessage;
            
            if (screenshotPath != null) {
                errorMsg += " | Screenshot captured: " + screenshotPath;
                LOGGER.error("❌ TEST FAILED - {} | Screenshot saved: {}", methodName, screenshotPath);
            } else {
                LOGGER.error("❌ TEST FAILED - {} | Screenshot capture failed", methodName);
            }
            
            // Add to Extent Reports (if enabled)
            TestLogger.log(errorMsg);
            
            // Fail the test
            Assert.fail(errorMsg);
            
        } catch (Exception e) {
            LOGGER.error("Error in failure handling for method: {}", methodName, e);
            Assert.fail("Test failed and error handling also failed: " + methodName);
        }
    }
    
    /**
     * Handle test failure with screenshot (using exception message)
     * 
     * @param methodName The name of the test method
     * @param exception The caught exception
     */
    public static void handleTestFailure(String methodName, Exception exception) {
        String defaultMessage = "Issue in " + formatMethodName(methodName) + "...Please Investigate!!!";
        handleTestFailure(methodName, exception, defaultMessage);
    }
    
    /**
     * Handle test failure with Throwable support (includes Error and Exception)
     * 
     * @param methodName The name of the test method
     * @param throwable The caught throwable
     * @param customMessage Custom error message (optional)
     */
    public static void handleTestFailure(String methodName, Throwable throwable, String customMessage) {
        try {
            // Capture screenshot on failure
            String screenshotPath = captureFailureScreenshot(methodName, throwable);
            
            // Print stack trace
            throwable.printStackTrace();
            
            // Prepare error message
            String baseMessage = customMessage != null ? customMessage : "Test method failed: " + methodName;
            String errorMsg = baseMessage;
            
            if (screenshotPath != null) {
                errorMsg += " | Screenshot captured: " + screenshotPath;
                LOGGER.error("❌ TEST FAILED - {} | Screenshot saved: {}", methodName, screenshotPath);
            } else {
                LOGGER.error("❌ TEST FAILED - {} | Screenshot capture failed", methodName);
            }
            
            // Add to Extent Reports (if enabled)
            TestLogger.log(errorMsg);
            
            // Fail the test
            Assert.fail(errorMsg);
            
        } catch (Exception e) {
            LOGGER.error("Error in failure handling for method: {}", methodName, e);
            Assert.fail("Test failed and error handling also failed: " + methodName);
        }
    }
    
    /**
     * Fail test with screenshot and custom message (no exception)
     * 
     * @param methodName The name of the test method
     * @param errorMessage The error message
     */
    public static void failWithScreenshot(String methodName, String errorMessage) {
        try {
            // Capture screenshot
            String screenshotPath = captureFailureScreenshot(methodName, errorMessage);
            
            // Prepare error message
            String errorMsg = errorMessage;
            if (screenshotPath != null) {
                errorMsg += " | Screenshot captured: " + screenshotPath;
                LOGGER.error("❌ TEST FAILED - {} | Screenshot saved: {}", methodName, screenshotPath);
            }
            
            // Add to Extent Reports (if enabled)
            TestLogger.log(errorMsg);
            
            // Fail the test
            Assert.fail(errorMsg);
            
        } catch (Exception e) {
            LOGGER.error("Error in failure handling for method: {}", methodName, e);
            Assert.fail("Test failed: " + errorMessage);
        }
    }
    
    /**
     * Capture screenshot and log without failing the test
     * 
     * @param methodName The name of the test method
     * @param logMessage The log message
     * @return Screenshot path or null if failed
     */
    public static String captureAndLog(String methodName, String logMessage) {
        try {
            String screenshotPath = captureFailureScreenshot(methodName, logMessage);
            
            if (screenshotPath != null) {
                LOGGER.warn("⚠️ ISSUE DETECTED - {} | Screenshot saved: {}", methodName, screenshotPath);
                TestLogger.logWarning(logMessage + " | Screenshot: " + screenshotPath);
            } else {
                LOGGER.warn("⚠️ ISSUE DETECTED - {} | Screenshot capture failed", methodName);
                TestLogger.logWarning(logMessage);
            }
            
            return screenshotPath;
            
        } catch (Exception e) {
            LOGGER.error("Error capturing screenshot for method: {}", methodName, e);
            return null;
        }
    }
    
    // ========================================
    // UTILITY AND HELPER METHODS
    // ========================================
    
    /**
     * Create structured screenshot file path
     * Filename format ensures ascending chronological order when sorted alphabetically
     */
    private static String createScreenshotPath(String methodName) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
        String date = LocalDateTime.now().format(DATE_FORMATTER);
        
        // Clean method name for file system
        String cleanMethodName = cleanMethodNameForFile(methodName);
        
        // Format: FAILURE_timestamp_methodName.png
        // This ensures ascending order when sorted alphabetically (oldest first)
        String fileName = String.format("FAILURE_%s_%s.png", timestamp, cleanMethodName);
        
        return String.format("%s%s%s%s%s%s%s", 
            SCREENSHOTS_DIR, File.separator,
            FAILURE_SCREENSHOTS_DIR, File.separator,
            date, File.separator,
            fileName);
    }
    
    /**
     * Create screenshot path with custom description
     */
    private static String createScreenshotPathWithDescription(String description) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
        String date = LocalDateTime.now().format(DATE_FORMATTER);
        
        String cleanDescription = cleanMethodNameForFile(description);
        String fileName = String.format("%s_%s.png", cleanDescription, timestamp);
        
        return String.format("%s%s%s%s%s%s%s", 
            SCREENSHOTS_DIR, File.separator,
            "CustomScreenshots", File.separator,
            date, File.separator,
            fileName);
    }
    
    /**
     * Clean method name for file system compatibility
     */
    private static String cleanMethodNameForFile(String methodName) {
        if (methodName == null) {
            return "UnknownMethod";
        }
        
        return methodName
            .replaceAll("[^a-zA-Z0-9_]", "_")  // Replace special chars with underscore
            .replaceAll("_{2,}", "_")          // Replace multiple underscores with single
            .replaceAll("^_|_$", "");          // Remove leading/trailing underscores
    }
    
    /**
     * Format method name for user-friendly display
     */
    private static String formatMethodName(String methodName) {
        if (methodName == null || methodName.trim().isEmpty()) {
            return "Unknown Method";
        }
        
        // Convert snake_case or camelCase to readable format
        String formatted = methodName
            .replaceAll("_", " ")
            .replaceAll("([a-z])([A-Z])", "$1 $2")
            .toLowerCase();
        
        // Capitalize first letter of each word
        String[] words = formatted.split(" ");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (word.length() > 0) {
                result.append(Character.toUpperCase(word.charAt(0)))
                      .append(word.substring(1))
                      .append(" ");
            }
        }
        return result.toString().trim();
    }
    
    /**
     * Add screenshot to ExtentReports for visual reporting
     */
    private static void addScreenshotToExtentReport(String screenshotPath, String methodName, String errorMessage) {
        try {
            // Add to Extent Reports (if enabled)
            String logMessage = String.format("🔴 FAILURE SCREENSHOT - Method: %s", methodName);
            if (errorMessage != null && !errorMessage.trim().isEmpty()) {
                logMessage += String.format(" | Error: %s", errorMessage);
            }
            
            TestLogger.addScreenshot(screenshotPath, logMessage);
            
            LOGGER.debug("Screenshot added to Extent Reports (if enabled): {}", screenshotPath);
            
        } catch (Exception e) {
            LOGGER.warn("Could not add screenshot to Extent Reports", e);
        }
    }
    
    // ========================================
    // CONFIGURATION AND MANAGEMENT METHODS
    // ========================================
    
    // ========================================
    // PERFORMANCE OPTIMIZATION METHODS
    // ========================================
    
    /**
     * PERFORMANCE: Determine if screenshot should be captured based on performance rules
     */
    private static boolean shouldCaptureScreenshot(String methodName, String errorMessage) {
        // Always capture if performance mode is disabled
        if (!performanceModeEnabled.get()) {
            return true;
        }
        
        // Check if this is a critical failure that always needs screenshot
        if (isCriticalFailure(errorMessage)) {
            return true;
        }
        
        // Check throttling - minimum time between screenshots
        Long lastTime = lastScreenshotTime.get(methodName);
        long currentTime = System.currentTimeMillis();
        
        if (lastTime != null && (currentTime - lastTime) < SCREENSHOT_THROTTLE_MS) {
            return false; // Too soon, skip screenshot
        }
        
        // Check max screenshots per method limit
        Integer count = screenshotCount.getOrDefault(methodName, 0);
        if (count >= MAX_SCREENSHOTS_PER_TEST) {
            return false; // Too many screenshots for this method
        }
        
        // Check if this is a minor/timeout issue that can be skipped
        if (isMinorIssue(errorMessage)) {
            return false; // Skip screenshots for minor issues
        }
        
        return true; // Capture screenshot
    }
    
    /**
     * PERFORMANCE: Check if this is a critical failure that always needs screenshot
     */
    private static boolean isCriticalFailure(String errorMessage) {
        if (errorMessage == null) return false;
        
        String msg = errorMessage.toLowerCase();
        return msg.contains("assert") || 
               msg.contains("fail") || 
               msg.contains("error") || 
               msg.contains("exception") ||
               msg.contains("not found") ||
               msg.contains("invalid");
    }
    
    /**
     * PERFORMANCE: Check if this is a minor issue that can skip screenshot
     */
    private static boolean isMinorIssue(String errorMessage) {
        if (errorMessage == null) return false;
        
        String msg = errorMessage.toLowerCase();
        return msg.contains("timeout") || 
               msg.contains("wait") || 
               msg.contains("loading") ||
               msg.contains("slow") ||
               msg.contains("spinner") ||
               msg.contains("element not immediately visible");
    }
    
    /**
     * PERFORMANCE: Get reason why screenshot was skipped
     */
    private static String getSkipReason(String methodName, String errorMessage) {
        if (!performanceModeEnabled.get()) {
            return "Performance mode disabled";
        }
        
        Long lastTime = lastScreenshotTime.get(methodName);
        if (lastTime != null && (System.currentTimeMillis() - lastTime) < SCREENSHOT_THROTTLE_MS) {
            return "Throttled (too frequent)";
        }
        
        Integer count = screenshotCount.getOrDefault(methodName, 0);
        if (count >= MAX_SCREENSHOTS_PER_TEST) {
            return "Max screenshots reached (" + MAX_SCREENSHOTS_PER_TEST + ")";
        }
        
        if (isMinorIssue(errorMessage)) {
            return "Minor issue (timeout/loading)";
        }
        
        return "Unknown reason";
    }
    
    /**
     * PERFORMANCE: Update screenshot throttling counters
     */
    private static void updateScreenshotCounters(String methodName) {
        lastScreenshotTime.put(methodName, System.currentTimeMillis());
        screenshotCount.put(methodName, screenshotCount.getOrDefault(methodName, 0) + 1);
    }
    
    /**
     * PERFORMANCE: Check if async capture is enabled
     */
    private static boolean isAsyncCaptureEnabled() {
        return Boolean.parseBoolean(System.getProperty("screenshot.async", "true"));
    }
    
    // ========================================
    // PERFORMANCE CONFIGURATION METHODS
    // ========================================
    
    /**
     * Enable or disable performance mode for screenshots
     */
    public static void setPerformanceModeEnabled(boolean enabled) {
        performanceModeEnabled.set(enabled);
        LOGGER.info("Screenshot performance mode {}", enabled ? "ENABLED" : "DISABLED");
        
        if (enabled) {
            LOGGER.info("Performance optimizations active:");
            LOGGER.info("- Screenshot throttling: {} ms", SCREENSHOT_THROTTLE_MS);
            LOGGER.info("- Max screenshots per test: {}", MAX_SCREENSHOTS_PER_TEST);
            LOGGER.info("- Async capture: {}", isAsyncCaptureEnabled());
        }
    }
    
    /**
     * Check if performance mode is enabled
     */
    public static boolean isPerformanceModeEnabled() {
        return performanceModeEnabled.get();
    }
    
    /**
     * Reset screenshot counters for new test run
     */
    public static void resetCounters() {
        lastScreenshotTime.clear();
        screenshotCount.clear();
        LOGGER.debug("Screenshot performance counters reset");
    }
    
    /**
     * Get current performance statistics
     */
    public static String getPerformanceStats() {
        int totalMethods = screenshotCount.size();
        int totalScreenshots = screenshotCount.values().stream().mapToInt(Integer::intValue).sum();
        
        return String.format("Screenshot Performance Stats - Methods: %d | Screenshots: %d | Avg per method: %.1f", 
                           totalMethods, totalScreenshots, 
                           totalMethods > 0 ? (double) totalScreenshots / totalMethods : 0.0);
    }
    
    /**
     * Check if screenshot capture is enabled
     */
    public static boolean isScreenshotEnabled() {
        return Boolean.parseBoolean(System.getProperty("screenshot.enabled", "true"));
    }
    
    /**
     * Enable or disable screenshot capture
     */
    public static void setScreenshotEnabled(boolean enabled) {
        System.setProperty("screenshot.enabled", String.valueOf(enabled));
        LOGGER.info("Screenshot capture {}", enabled ? "ENABLED" : "DISABLED");
    }
    
    /**
     * Get screenshots directory path
     */
    public static String getScreenshotsDirectory() {
        return SCREENSHOTS_DIR;
    }
    
    /**
     * Clean up old screenshots (older than specified days)
     */
    public static void cleanupOldScreenshots(int daysToKeep) {
        try {
            File screenshotsDir = new File(SCREENSHOTS_DIR);
            if (!screenshotsDir.exists()) {
                return;
            }
            
            long cutoffTime = System.currentTimeMillis() - (daysToKeep * 24 * 60 * 60 * 1000L);
            
            cleanupDirectory(screenshotsDir, cutoffTime);
            
            LOGGER.info("Screenshot cleanup completed - removed files older than {} days", daysToKeep);
            
        } catch (Exception e) {
            LOGGER.error("Error during screenshot cleanup", e);
        }
    }
    
    /**
     * Recursively clean up directory
     */
    private static void cleanupDirectory(File directory, long cutoffTime) {
        File[] files = directory.listFiles();
        if (files == null) return;
        
        for (File file : files) {
            if (file.isDirectory()) {
                cleanupDirectory(file, cutoffTime);
                // Remove empty directories
                if (file.list() != null && file.list().length == 0) {
                    file.delete();
                }
            } else if (file.lastModified() < cutoffTime) {
                file.delete();
                LOGGER.debug("Deleted old screenshot: {}", file.getName());
            }
        }
    }
    
    // ========================================
    // BACKWARD COMPATIBILITY METHODS
    // ========================================
    
    /**
     * Create a standardized catch block for test methods
     * This is a template method that can be copied into test methods
     * 
     * Usage:
     * } catch(Exception e) {
     *     ScreenshotHandler.standardCatchBlock("methodName", e, "Custom error message");
     * }
     */
    public static void standardCatchBlock(String methodName, Exception exception, String customMessage) {
        handleTestFailure(methodName, exception, customMessage);
    }
    
    /**
     * Check if screenshot functionality is available
     */
    public static boolean isScreenshotAvailable() {
        return isScreenshotEnabled();
    }
    
    /**
     * Get example catch block code for easy copy-paste
     */
    public static String getExampleCatchBlock(String methodName) {
        return String.format(
            "} catch(Exception e) {\n" +
            "    ScreenshotHandler.handleTestFailure(\"%s\", e);\n" +
            "}",
            methodName
        );
    }
    
    /**
     * Get all failure screenshots for a specific date in ascending chronological order
     * 
     * @param date Date in format yyyy-MM-dd (e.g., "2025-10-27")
     * @return List of screenshot files sorted in ascending order (oldest first)
     */
    public static java.util.List<File> getFailureScreenshotsAscending(String date) {
        try {
            File dateDir = new File(SCREENSHOTS_DIR + File.separator + 
                                   FAILURE_SCREENSHOTS_DIR + File.separator + date);
            
            if (!dateDir.exists() || !dateDir.isDirectory()) {
                return new java.util.ArrayList<>();
            }
            
            File[] files = dateDir.listFiles((dir, name) -> name.endsWith(".png"));
            if (files == null || files.length == 0) {
                return new java.util.ArrayList<>();
            }
            
            // Sort in ascending order (oldest first) - filename starts with timestamp
            java.util.Arrays.sort(files, java.util.Comparator.comparing(File::getName));
            
            return java.util.Arrays.asList(files);
            
        } catch (Exception e) {
            LOGGER.error("Error getting failure screenshots for date: {}", date, e);
            return new java.util.ArrayList<>();
        }
    }
    
    /**
     * Get all failure screenshots for today in ascending chronological order
     * 
     * @return List of screenshot files sorted in ascending order (oldest first)
     */
    public static java.util.List<File> getTodayFailureScreenshotsAscending() {
        String today = LocalDateTime.now().format(DATE_FORMATTER);
        return getFailureScreenshotsAscending(today);
    }
}
