package com.kfonetalentsuite.utils.JobMapping;

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

import com.kfonetalentsuite.webdriverManager.DriverManager;
import com.kfonetalentsuite.utils.common.CommonVariable;

public class ScreenshotHandler {

	private static final Logger LOGGER = LogManager.getLogger(ScreenshotHandler.class);

	// Configuration
	private static final String SCREENSHOTS_BASE_DIR;
	private static final String FAILURE_SCREENSHOTS_DIR = "FailureScreenshots";
	private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	// Initialize screenshots directory
	static {
		String userDir = System.getProperty("user.dir");
		String projectRoot;

		// If running from e2e-automation folder, use it directly
		if (userDir.endsWith("e2e-automation")) {
			projectRoot = userDir;
		} else {
			// If running from root, append e2e-automation
			projectRoot = userDir + File.separator + "e2e-automation";
		}

		SCREENSHOTS_BASE_DIR = projectRoot + File.separator + "Screenshots";

		// Log the screenshots directory for debugging
		System.out.println("ScreenshotHandler initialized - Screenshots directory: " + SCREENSHOTS_BASE_DIR);
	}

	// PERFORMANCE OPTIMIZATION: Screenshot throttling and smart capture
	private static final long SCREENSHOT_THROTTLE_MS = 2000; // Minimum 2 seconds between screenshots
	private static final int MAX_SCREENSHOTS_PER_TEST = 3; // Maximum screenshots per test method
	private static final java.util.Map<String, Long> lastScreenshotTime = new java.util.concurrent.ConcurrentHashMap<>();
	private static final java.util.Map<String, Integer> screenshotCount = new java.util.concurrent.ConcurrentHashMap<>();
	private static final java.util.concurrent.atomic.AtomicBoolean performanceModeEnabled = new java.util.concurrent.atomic.AtomicBoolean(
			true); // Default: Performance mode ON

	// ========================================
	// CORE SCREENSHOT CAPTURE METHODS
	// ========================================

	public static String captureFailureScreenshot(String methodName, String errorMessage) {
		try {
			// PERFORMANCE OPTIMIZATION: Early exit conditions
			if (!shouldCaptureScreenshot(methodName, errorMessage)) {
				LOGGER.debug("Screenshot skipped for performance (method: {}, reason: {})", methodName,
						getSkipReason(methodName, errorMessage));
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
			LOGGER.error(" Failed to capture screenshot for method: {}", methodName, e);
			return null;
		}
	}

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
			LOGGER.info(" SCREENSHOT CAPTURED - Method: {} | Path: {}", methodName, screenshotPath);

			// ENHANCEMENT: Attach screenshot to Allure report (if enabled)
			if (isAllureReportingEnabled()) {
				try {
					AllureReportingManager.attachScreenshotToAllure(screenshotPath, 
						"Failure Screenshot: " + methodName);
					LOGGER.debug("Screenshot attached to Allure report");
				} catch (Exception e) {
					LOGGER.debug("Could not attach screenshot to Allure (non-critical): {}", e.getMessage());
					// Don't fail if Allure attachment fails
				}
			}

			return screenshotPath;

		} catch (Exception e) {
			LOGGER.error(" Sync screenshot capture failed for method: {}", methodName, e);
			return null;
		}
	}

	private static String captureScreenshotAsync(String methodName, String errorMessage, WebDriver driver) {
		try {
			String screenshotPath = createScreenshotPath(methodName);

			// Quick screenshot capture
			TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
			File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);

			// Process screenshot file operations in background thread
			java.util.concurrent.CompletableFuture<Void> screenshotFuture = java.util.concurrent.CompletableFuture.runAsync(() -> {
				try {
					File destFile = new File(screenshotPath);
					destFile.getParentFile().mkdirs();
					FileUtils.copyFile(sourceFile, destFile);

					LOGGER.debug(" ASYNC SCREENSHOT SAVED - Method: {} | Path: {}", methodName, screenshotPath);

				} catch (Exception e) {
					LOGGER.warn("Async screenshot processing failed for method: {}", methodName, e);
				}
			});

			LOGGER.info(" ASYNC SCREENSHOT QUEUED - Method: {}", methodName);
			
			// CRITICAL FIX: If Allure reporting is enabled, wait for screenshot to be written
			// This ensures the file exists when AllureReportingManager tries to attach it
			if (isAllureReportingEnabled()) {
				try {
					// Wait for screenshot to be written (with timeout to prevent hanging)
					screenshotFuture.get(5, java.util.concurrent.TimeUnit.SECONDS);
					LOGGER.debug("Screenshot file ready for Allure attachment: {}", screenshotPath);
				} catch (java.util.concurrent.TimeoutException e) {
					LOGGER.warn("Screenshot write timeout for Allure - continuing anyway");
				} catch (Exception e) {
					LOGGER.warn("Error waiting for screenshot: {}", e.getMessage());
				}
			}
			
			return screenshotPath;

		} catch (Exception e) {
			LOGGER.error(" Async screenshot capture failed for method: {}", methodName, e);
			return null;
		}
	}

	public static String captureFailureScreenshot(String methodName, Throwable throwable) {
		String errorMessage = throwable != null ? throwable.getMessage() : "Unknown error";
		return captureFailureScreenshot(methodName, errorMessage);
	}

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

			LOGGER.info(" Screenshot captured: {}", description);
			LOGGER.info("Saved to: {}", screenshotPath);

			// ENHANCEMENT: Attach screenshot to Allure report (if enabled)
			if (isAllureReportingEnabled()) {
				try {
					AllureReportingManager.attachScreenshotToAllure(screenshotPath, description);
					LOGGER.debug("Screenshot attached to Allure report");
				} catch (Exception e) {
					LOGGER.debug("Could not attach screenshot to Allure (non-critical): {}", e.getMessage());
					// Don't fail if Allure attachment fails
				}
			}

			return screenshotPath;

		} catch (Exception e) {
			LOGGER.error(" Failed to capture screenshot: {}", description, e);
			return null;
		}
	}

	// ========================================
	// HIGH-LEVEL FAILURE HANDLING METHODS
	// ========================================

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
				LOGGER.error(" TEST FAILED - {} | Screenshot saved: {}", methodName, screenshotPath);
			} else {
				LOGGER.error(" TEST FAILED - {} | Screenshot capture failed", methodName);
			}

			// Log error
			LOGGER.info(errorMsg);

			// Fail the test
			Assert.fail(errorMsg);

		} catch (Exception e) {
			LOGGER.error("Error in failure handling for method: {}", methodName, e);
			Assert.fail("Test failed and error handling also failed: " + methodName);
		}
	}

	public static void handleTestFailure(String methodName, Exception exception) {
		String defaultMessage = "Issue in " + formatMethodName(methodName) + "...Please Investigate!!!";
		handleTestFailure(methodName, exception, defaultMessage);
	}

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
				LOGGER.error(" TEST FAILED - {} | Screenshot saved: {}", methodName, screenshotPath);
			} else {
				LOGGER.error(" TEST FAILED - {} | Screenshot capture failed", methodName);
			}

			// Log error
			LOGGER.info(errorMsg);

			// Fail the test
			Assert.fail(errorMsg);

		} catch (Exception e) {
			LOGGER.error("Error in failure handling for method: {}", methodName, e);
			Assert.fail("Test failed and error handling also failed: " + methodName);
		}
	}

	public static void failWithScreenshot(String methodName, String errorMessage) {
		try {
			// Capture screenshot
			String screenshotPath = captureFailureScreenshot(methodName, errorMessage);

			// Prepare error message
			String errorMsg = errorMessage;
			if (screenshotPath != null) {
				errorMsg += " | Screenshot captured: " + screenshotPath;
				LOGGER.error(" TEST FAILED - {} | Screenshot saved: {}", methodName, screenshotPath);
			}

			// Log error
			LOGGER.info(errorMsg);

			// Fail the test
			Assert.fail(errorMsg);

		} catch (Exception e) {
			LOGGER.error("Error in failure handling for method: {}", methodName, e);
			Assert.fail("Test failed: " + errorMessage);
		}
	}

	public static String captureAndLog(String methodName, String logMessage) {
		try {
			String screenshotPath = captureFailureScreenshot(methodName, logMessage);

			if (screenshotPath != null) {
				LOGGER.warn(" ISSUE DETECTED - {} | Screenshot saved: {} | {}", methodName, screenshotPath, logMessage);
			} else {
				LOGGER.warn(" ISSUE DETECTED - {} | Screenshot capture failed | {}", methodName, logMessage);
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

	private static String createScreenshotPath(String methodName) {
		String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
		String date = LocalDateTime.now().format(DATE_FORMATTER);

		// Clean method name for file system
		String cleanMethodName = cleanMethodNameForFile(methodName);

		// Format: FAILURE_timestamp_methodName.png
		// This ensures ascending order when sorted alphabetically (oldest first)
		String fileName = String.format("FAILURE_%s_%s.png", timestamp, cleanMethodName);

		return String.format("%s%s%s%s%s%s%s", SCREENSHOTS_BASE_DIR, File.separator, FAILURE_SCREENSHOTS_DIR,
				File.separator, date, File.separator, fileName);
	}

	private static String createScreenshotPathWithDescription(String description) {
		String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
		String date = LocalDateTime.now().format(DATE_FORMATTER);

		String cleanDescription = cleanMethodNameForFile(description);
		String fileName = String.format("%s_%s.png", cleanDescription, timestamp);

		return String.format("%s%s%s%s%s%s%s", SCREENSHOTS_BASE_DIR, File.separator, "CustomScreenshots",
				File.separator, date, File.separator, fileName);
	}

	private static String cleanMethodNameForFile(String methodName) {
		if (methodName == null) {
			return "UnknownMethod";
		}

		return methodName.replaceAll("[^a-zA-Z0-9_]", "_") // Replace special chars with underscore
				.replaceAll("_{2,}", "_") // Replace multiple underscores with single
				.replaceAll("^_|_$", ""); // Remove leading/trailing underscores
	}

	private static String formatMethodName(String methodName) {
		if (methodName == null || methodName.trim().isEmpty()) {
			return "Unknown Method";
		}

		// Convert snake_case or camelCase to readable format
		String formatted = methodName.replaceAll("_", " ").replaceAll("([a-z])([A-Z])", "$1 $2").toLowerCase();

		// Capitalize first letter of each word
		String[] words = formatted.split(" ");
		StringBuilder result = new StringBuilder();
		for (String word : words) {
			if (word.length() > 0) {
				result.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
			}
		}
		return result.toString().trim();
	}

	// ========================================
	// CONFIGURATION AND MANAGEMENT METHODS
	// ========================================

	// ========================================
	// PERFORMANCE OPTIMIZATION METHODS
	// ========================================

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

	private static boolean isCriticalFailure(String errorMessage) {
		if (errorMessage == null)
			return false;

		String msg = errorMessage.toLowerCase();
		return msg.contains("assert") || msg.contains("fail") || msg.contains("error") || msg.contains("exception")
				|| msg.contains("not found") || msg.contains("invalid");
	}

	private static boolean isMinorIssue(String errorMessage) {
		if (errorMessage == null)
			return false;

		String msg = errorMessage.toLowerCase();
		return msg.contains("timeout") || msg.contains("wait") || msg.contains("loading") || msg.contains("slow")
				|| msg.contains("spinner") || msg.contains("element not immediately visible");
	}

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

	private static void updateScreenshotCounters(String methodName) {
		lastScreenshotTime.put(methodName, System.currentTimeMillis());
		screenshotCount.put(methodName, screenshotCount.getOrDefault(methodName, 0) + 1);
	}

	private static boolean isAsyncCaptureEnabled() {
		return Boolean.parseBoolean(System.getProperty("screenshot.async", "true"));
	}

	// ========================================
	// PERFORMANCE CONFIGURATION METHODS
	// ========================================

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

	public static boolean isPerformanceModeEnabled() {
		return performanceModeEnabled.get();
	}

	public static void resetCounters() {
		lastScreenshotTime.clear();
		screenshotCount.clear();
	}

	public static String getPerformanceStats() {
		int totalMethods = screenshotCount.size();
		int totalScreenshots = screenshotCount.values().stream().mapToInt(Integer::intValue).sum();

		return String.format("Screenshot Performance Stats - Methods: %d | Screenshots: %d | Avg per method: %.1f",
				totalMethods, totalScreenshots, totalMethods > 0 ? (double) totalScreenshots / totalMethods : 0.0);
	}

	public static boolean isScreenshotEnabled() {
		return Boolean.parseBoolean(System.getProperty("screenshot.enabled", "true"));
	}

	public static void setScreenshotEnabled(boolean enabled) {
		System.setProperty("screenshot.enabled", String.valueOf(enabled));
		LOGGER.info("Screenshot capture {}", enabled ? "ENABLED" : "DISABLED");
	}

	public static String getScreenshotsDirectory() {
		return SCREENSHOTS_BASE_DIR;
	}

	public static void cleanupOldScreenshots(int daysToKeep) {
		try {
			File screenshotsDir = new File(SCREENSHOTS_BASE_DIR);
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

	private static void cleanupDirectory(File directory, long cutoffTime) {
		File[] files = directory.listFiles();
		if (files == null)
			return;

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

	public static void standardCatchBlock(String methodName, Exception exception, String customMessage) {
		handleTestFailure(methodName, exception, customMessage);
	}

	public static boolean isScreenshotAvailable() {
		return isScreenshotEnabled();
	}

	public static String getExampleCatchBlock(String methodName) {
		return String.format("} catch(Exception e) {\n" + "    ScreenshotHandler.handleTestFailure(\"%s\", e);\n" + "}",
				methodName);
	}

	public static java.util.List<File> getFailureScreenshotsAscending(String date) {
		try {
			File dateDir = new File(
					SCREENSHOTS_BASE_DIR + File.separator + FAILURE_SCREENSHOTS_DIR + File.separator + date);

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

	public static java.util.List<File> getTodayFailureScreenshotsAscending() {
		String today = LocalDateTime.now().format(DATE_FORMATTER);
		return getFailureScreenshotsAscending(today);
	}

	private static boolean isAllureReportingEnabled() {
		return CommonVariable.ALLURE_REPORTING_ENABLED == null
				|| !CommonVariable.ALLURE_REPORTING_ENABLED.equalsIgnoreCase("false");
	}
}
