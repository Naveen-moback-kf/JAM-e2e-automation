package com.kfonetalentsuite.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IExecutionListener;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.ITestContext;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.HashMap;


import com.kfonetalentsuite.utils.JobMapping.DailyExcelTracker;
import com.kfonetalentsuite.utils.JobMapping.ProgressBarUtil;
import com.kfonetalentsuite.utils.common.CommonVariable;
import com.kfonetalentsuite.utils.JobMapping.KeepAwakeUtil;
import com.kfonetalentsuite.utils.JobMapping.ScreenshotHandler;
import java.io.StringWriter;
import java.io.PrintWriter;

public class ExcelReportListener implements IExecutionListener, ISuiteListener, ITestListener, IInvokedMethodListener {
    
    private static final Logger LOGGER = LogManager.getLogger(ExcelReportListener.class);
    
    // Configuration flag to enable/disable Excel generation (unified with DailyExcelTracker)
    private static boolean isExcelReportingEnabled() {
        return CommonVariable.EXCEL_REPORTING_ENABLED == null || 
               !CommonVariable.EXCEL_REPORTING_ENABLED.equalsIgnoreCase("false");
    }
    
    /**
     * Configure Extent Reporting based on extent.reporting.enabled flag
     * This runs at the very start of test execution (before any tests run)
     */
    private static void configureExtentReporting() {
        try {
            boolean extentEnabled = CommonVariable.EXTENT_REPORTING_ENABLED == null || 
                                   !CommonVariable.EXTENT_REPORTING_ENABLED.trim().equalsIgnoreCase("false");
            
            LOGGER.debug("Extent Reporting Configuration: {}", extentEnabled);
            
            // Set system property for Extent to respect
            System.setProperty("extent.reporter.spark.start", String.valueOf(extentEnabled));
            
            if (extentEnabled) {
                LOGGER.info("✅ Extent HTML reports ENABLED");
            } else {
                LOGGER.info("⚠️ Extent HTML reports DISABLED");
            }
            
        } catch (Exception e) {
            LOGGER.error("Failed to configure Extent reporting: {}", e.getMessage(), e);
            LOGGER.warn("Extent reporting will use default configuration (enabled)");
        }
    }
    
    // Execution statistics
    private static int totalTestMethods = 0;
    private static int passedTestMethods = 0;
    private static int failedTestMethods = 0;
    private static int skippedTestMethods = 0;
    
    // Thread-safe storage for current scenario information
    private static final Map<String, String> currentScenarios = new ConcurrentHashMap<>();
    
    // Store cross-browser runner information
    private static final Map<String, String> crossBrowserRunnerNames = new ConcurrentHashMap<>();
    
    // ENHANCED: Store actual exception details for Excel reporting
    private static final Map<String, ExceptionDetails> testExceptionDetails = new ConcurrentHashMap<>();
    
    // ENHANCED: Store skip reasons by scenario name for easier lookup during Excel generation
    private static final Map<String, String> skipReasonsByScenario = new ConcurrentHashMap<>();
    
    // ENHANCED: Store global skip reasons by test runner class for features with multiple scenarios
    private static final Map<String, String> globalSkipReasonsByRunner = new ConcurrentHashMap<>();
    
    // PERFORMANCE METRICS: Store performance test metrics for Feature 41 (Performance Testing)
    private static final Map<String, PerformanceMetrics> scenarioPerformanceMetrics = new ConcurrentHashMap<>();
    
    // Track completed test classes for incremental updates
    private static final Map<String, Integer> completedMethodsPerClass = new ConcurrentHashMap<>();
    private static final Map<String, Integer> totalMethodsPerClass = new ConcurrentHashMap<>();
    
    // ENHANCED: Progress tracking for test suite execution
    private static int totalTestContexts = 0;
    private static int completedTestContexts = 0;
    
    /**
     * Store current scenario name for a thread (called from ScenarioHooks)
     */
    public static void setCurrentScenario(String threadId, String scenarioName) {
        currentScenarios.put(threadId, scenarioName);
    }
    
    /**
     * Get current scenario name for a thread (called from page objects for performance tracking)
     */
    public static String getCurrentScenarioName(String threadId) {
        return currentScenarios.get(threadId);
    }
    
    /**
     * Remove scenario name when scenario completes (called from ScenarioHooks)
     */
    public static void clearCurrentScenario(String threadId) {
        currentScenarios.remove(threadId);
    }
    
    /**
     * Set cross-browser runner information for Excel reporting
     */
    private void setCrossBrowserRunnerInfo(ITestResult result) {
        try {
            String testClassName = result.getTestClass().getName();
            
            // Check if this is a cross-browser test
            if (testClassName.contains("CrossBrowser") || testClassName.contains("crossbrowser")) {
                // Extract browser name from parameters
                Object[] parameters = result.getParameters();
                if (parameters != null && parameters.length > 0) {
                    String browserName = String.valueOf(parameters[0]);
                    if (browserName != null && (browserName.equals("chrome") || browserName.equals("firefox") || browserName.equals("edge"))) {
                        // Store the runner class name with browser info
                        String threadId = Thread.currentThread().getName();
                        crossBrowserRunnerNames.put(threadId, testClassName);
                        
                        // Also store for DailyExcelTracker
                        System.setProperty("current.runner.class." + threadId, testClassName);
                        System.setProperty("current.browser.name." + threadId, browserName);
                        
                        LOGGER.debug("Cross-browser runner info set: {} for browser: {} on thread: {}", testClassName, browserName, threadId);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.debug("Failed to set cross-browser runner info: {}", e.getMessage());
        }
    }
    
    /**
     * Get cross-browser runner name for current thread
     */
    public static String getCrossBrowserRunnerName(String threadId) {
        return crossBrowserRunnerNames.get(threadId);
    }
    
    /**
     * Get current scenario name for logging
     */
    private String getCurrentScenarioName() {
        String threadId = Thread.currentThread().getName();
        String scenarioName = currentScenarios.get(threadId);
        return scenarioName != null ? scenarioName : "Test Scenario";
    }
    
    @Override
    public void onExecutionStart() {
        // EXTENT REPORTING CONFIGURATION: Configure Extent before anything else
        configureExtentReporting();
        
        // SYSTEM POWER MANAGEMENT: Initialize Keep System Awake at suite level (ONCE for entire execution)
        KeepAwakeUtil.initialize();
        
        if (!isExcelReportingEnabled()) {
            return;
        }
        
        // Reset counters and add logging for debugging
        totalTestMethods = 0;
        passedTestMethods = 0;
        failedTestMethods = 0;
        skippedTestMethods = 0;
        
        LOGGER.debug("Excel reporting listener initialized");
        
        // Reset class tracking for incremental updates
        completedMethodsPerClass.clear();
        totalMethodsPerClass.clear();
        
        // ENHANCED: Reset progress tracking
        totalTestContexts = 0;
        completedTestContexts = 0;
        
        // PERFORMANCE OPTIMIZATION: Reset screenshot counters for clean tracking
        ScreenshotHandler.resetCounters();
        
        LOGGER.debug("Screenshot performance mode: {}", 
                   ScreenshotHandler.isPerformanceModeEnabled() ? "ENABLED" : "DISABLED");
    }
    
    // ISuiteListener implementation - for suite-level tracking
    @Override
    public void onStart(org.testng.ISuite suite) {
        if (!isExcelReportingEnabled()) {
            return;
        }
        
        // ENHANCED: Count total test contexts (runners) and initialize progress bar
        try {
            Map<String, org.testng.xml.XmlTest> allTests = suite.getXmlSuite().getTests().stream()
                    .collect(java.util.stream.Collectors.toMap(
                            org.testng.xml.XmlTest::getName, 
                            test -> test));
            
            totalTestContexts = allTests.size();
            
            if (totalTestContexts > 0) {
                LOGGER.info(" Test Suite Started: '{}' with {} runners", 
                           suite.getName(), totalTestContexts);
                ProgressBarUtil.initializeProgress(totalTestContexts);
            }
        } catch (Exception e) {
            LOGGER.warn("Could not initialize progress tracking: {}", e.getMessage());
            // Fallback - disable progress bar for this execution
            totalTestContexts = 0;
        }
    }
    
    @Override
    public void onFinish(org.testng.ISuite suite) {
        if (!isExcelReportingEnabled()) {
            return;
        }
        
        // Suite completed - final report handled in onExecutionFinish()
    }
    
    // ITestListener implementation - for individual test (runner) tracking  
    @Override
    public void onStart(ITestContext context) {
        if (!isExcelReportingEnabled()) {
            return;
        }
        
        // Individual test started
    }
    
    @Override
    public void onFinish(ITestContext context) {
        if (!isExcelReportingEnabled()) {
            return;
        }
        
        // ENHANCED: Update progress bar when runner completes
        if (totalTestContexts > 0) {
            completedTestContexts++;
            String testName = context.getName();
            ProgressBarUtil.updateProgress(testName);
            
            LOGGER.debug("Runner completed: '{}' ({}/{})", 
                        testName, completedTestContexts, totalTestContexts);
        }
        
        // Individual test completed - Excel update deferred to execution completion
    }

    @Override
    public void onExecutionFinish() {
        LOGGER.info("Execution finished - Total: {}, Passed: {}, Failed: {}, Skipped: {}", 
                   totalTestMethods, passedTestMethods, failedTestMethods, skippedTestMethods);
        
        if (!isExcelReportingEnabled()) {
            LOGGER.debug("Excel reporting is DISABLED - Skipping Excel generation");
            
            // SYSTEM POWER MANAGEMENT: Shutdown Keep System Awake at suite level (ONCE after entire execution)
            KeepAwakeUtil.shutdown();
            return;
        }
        
        // Generate final Excel report with execution history
        LOGGER.debug("Excel reporting is ENABLED - Proceeding with report generation");
        
        // PERFORMANCE OPTIMIZATION: Log screenshot performance statistics
        LOGGER.debug("Screenshot performance stats: {}", 
                   ScreenshotHandler.getPerformanceStats());
        
        try {
            DailyExcelTracker.generateDailyReport(false); // Full update with execution history
            LOGGER.info("Excel report generated successfully");
            
            // ENHANCED: Clean up captured exception details after Excel report is generated
            clearExceptionDetails();
            LOGGER.debug("Cleared captured exception details after Excel report generation");
            
            // Clean up cross-browser runner information
            crossBrowserRunnerNames.clear();
            
            // ENHANCED: Reset progress tracking after execution completes
            ProgressBarUtil.resetProgress();
            
        } catch (Exception e) {
            LOGGER.error("Excel report generation failed", e);
        } finally {
            // SYSTEM POWER MANAGEMENT: Shutdown Keep System Awake at suite level (ONCE after entire execution)
            KeepAwakeUtil.shutdown();
        }
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        passedTestMethods++;
        totalTestMethods++;
        // No logging needed for individual test success
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        failedTestMethods++;
        totalTestMethods++;
        
        // ENHANCED: Capture actual exception details for Excel reporting
        if (isExcelReportingEnabled()) {
            String scenarioInfo = extractScenarioInfo(result);
            LOGGER.warn("Test failed: {}", scenarioInfo);
            
            // ENHANCED: Capture comprehensive failure details with actual exception message
            captureFailureExceptionDetails(result, scenarioInfo);
        }
    }
    
    /**
     * ENHANCED: Capture comprehensive failure exception details
     */
    private void captureFailureExceptionDetails(ITestResult result, String scenarioInfo) {
        try {
            Throwable throwable = result.getThrowable();
            String runnerClassName = result.getTestClass().getName();
            
            LOGGER.debug("FAIL CAPTURE - Scenario: '{}', Throwable: {}", scenarioInfo, throwable != null ? throwable.getClass().getSimpleName() : "null");
            
            if (throwable != null) {
                // Create comprehensive exception details object for failure
                ExceptionDetails details = new ExceptionDetails();
                details.scenarioName = scenarioInfo;
                
                // ENHANCED: Get the most meaningful exception message
                String actualExceptionMessage = extractMostMeaningfulExceptionMessage(throwable);
                details.exceptionMessage = actualExceptionMessage;
                details.exceptionType = throwable.getClass().getSimpleName();
                details.stackTrace = getStackTraceAsString(throwable);
                details.timestamp = System.currentTimeMillis();
                details.testStatus = "FAILED";
                
                // Create a unique key for this failure
                String testKey = runnerClassName + "." + result.getMethod().getMethodName();
                testExceptionDetails.put(testKey, details);
                
                LOGGER.debug("Captured failure exception for '{}': '{}'", scenarioInfo, actualExceptionMessage);
                
                // ENHANCED: Also store failure reason by runner for global lookup
                if (actualExceptionMessage != null && actualExceptionMessage.length() > 10) {
                    String runnerKey = extractRunnerKey(runnerClassName);
                    // Store with a different key to avoid conflicts with skip reasons
                    globalSkipReasonsByRunner.put(runnerKey + "_failure", actualExceptionMessage);
                    LOGGER.debug("Stored global failure reason for runner '{}': '{}'", runnerKey, actualExceptionMessage);
                }
                
            } else {
                // Even if no throwable, create a basic failure details entry
                ExceptionDetails details = new ExceptionDetails();
                details.scenarioName = scenarioInfo;
                details.exceptionMessage = "Test failed - no exception details available";
                details.exceptionType = "UnknownFailure";
                details.stackTrace = "";
                details.timestamp = System.currentTimeMillis();
                details.testStatus = "FAILED";
                
                String testKey = runnerClassName + "." + result.getMethod().getMethodName();
                testExceptionDetails.put(testKey, details);
                
                LOGGER.debug("FAIL CAPTURE - No throwable, captured basic failure for '{}'. Total captured: {}", scenarioInfo, testExceptionDetails.size());
            }
            
        } catch (Exception e) {
            LOGGER.warn("Failed to capture failure exception details", e);
        }
    }
    
    /**
     * Extract the most meaningful exception message from a throwable chain
     */
    private String extractMostMeaningfulExceptionMessage(Throwable throwable) {
        if (throwable == null) return "Unknown failure";
        
        // Start with the original exception message
        String originalMessage = throwable.getMessage();
        String mostMeaningful = originalMessage;
        
        // Walk through the exception chain to find the most meaningful message
        Throwable current = throwable;
        while (current != null) {
            String currentMessage = current.getMessage();
            
            if (currentMessage != null && !currentMessage.trim().isEmpty()) {
                // Prioritize messages that contain specific error information
                if (isMoreMeaningfulMessage(currentMessage, mostMeaningful)) {
                    mostMeaningful = currentMessage;
                }
            }
            
            current = current.getCause();
        }
        
        // If we still don't have a meaningful message, use the exception type
        if (mostMeaningful == null || mostMeaningful.trim().isEmpty()) {
            mostMeaningful = throwable.getClass().getSimpleName() + " occurred during test execution";
        }
        
        return mostMeaningful;
    }
    
    /**
     * Determine if a message is more meaningful than the current best message
     */
    private boolean isMoreMeaningfulMessage(String newMessage, String currentBest) {
        if (newMessage == null) return false;
        if (currentBest == null) return true;
        
        String newLower = newMessage.toLowerCase();
        String currentLower = currentBest.toLowerCase();
        
        // Prioritize messages with specific error indicators
        String[] meaningfulKeywords = {
            "assertion", "expected", "actual", "timeout", "not found", "invalid", 
            "failed to", "unable to", "cannot", "error", "exception", "missing",
            "required", "forbidden", "unauthorized", "not available", "not supported"
        };
        
        int newScore = 0;
        int currentScore = 0;
        
        for (String keyword : meaningfulKeywords) {
            if (newLower.contains(keyword)) newScore++;
            if (currentLower.contains(keyword)) currentScore++;
        }
        
        // Longer messages with specific keywords are generally more meaningful
        if (newScore > currentScore) return true;
        if (newScore == currentScore && newMessage.length() > currentBest.length()) return true;
        
        return false;
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        skippedTestMethods++;
        totalTestMethods++;
        
        // ENHANCED: Capture actual skip exception details for Excel reporting
        if (isExcelReportingEnabled()) {
            String scenarioInfo = extractScenarioInfo(result);
            LOGGER.debug("Test skipped: {}", scenarioInfo);
            
            // Capture and store the actual skip exception details (including role-based skip reasons)
            captureSkipExceptionDetails(result, scenarioInfo);
        }
    }
    
    @Override
    public void onTestStart(ITestResult result) {
        // Set runner class name for cross-browser tests
        if (isExcelReportingEnabled()) {
            setCrossBrowserRunnerInfo(result);
        }
        // No logging needed for individual test starts - reduces noise
    }
    
    // IInvokedMethodListener implementation - for tracking individual method completion
    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        if (!isExcelReportingEnabled() || !method.isTestMethod()) {
            return;
        }
        
        String className = testResult.getTestClass().getName();
        // Count total methods per class
        totalMethodsPerClass.merge(className, 1, Integer::sum);
    }
    
    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        if (!isExcelReportingEnabled() || !method.isTestMethod()) {
            return;
        }
        
        String className = testResult.getTestClass().getName();
        
        // Count completed methods per class
        int completedMethods = completedMethodsPerClass.merge(className, 1, Integer::sum);
        int totalMethods = totalMethodsPerClass.getOrDefault(className, 0);
        
        // Check if this class (runner) has completed all its methods
        if (completedMethods >= totalMethods) {
            // Reset tracking for this class - runner completed
            completedMethodsPerClass.remove(className);
            totalMethodsPerClass.remove(className);
        }
    }
    
    /**
     * Extract meaningful scenario information from test result - optimized
     */
    private String extractScenarioInfo(ITestResult result) {
        // Strategy 1: Extract from TestNG parameters
        Object[] parameters = result.getParameters();
        if (parameters != null && parameters.length > 0) {
            for (Object param : parameters) {
                if (param != null) {
                    // Try direct PickleWrapper access via reflection
                    try {
                        if (param.getClass().getName().contains("PickleWrapper")) {
                            Object pickle = param.getClass().getMethod("getPickle").invoke(param);
                            if (pickle != null) {
                                String scenarioName = (String) pickle.getClass().getMethod("getName").invoke(pickle);
                                if (scenarioName != null && scenarioName.length() > 5) {
                                    return scenarioName;
                                }
                            }
                        }
                    } catch (Exception e) {
                        // Silent fallback
                    }
                    
                    // Enhanced parameter extraction from string representation
                    String paramString = param.toString();
                    if (paramString.contains("PickleWrapper") || paramString.contains("Scenario:")) {
                        String extractedName = extractScenarioNameFromParam(paramString);
                        if (extractedName != null && extractedName.length() > 5) {
                            return extractedName;
                        }
                    }
                }
            }
        }
        
        // Strategy 2: Try current scenario name from thread context
        String scenarioName = getCurrentScenarioName();
        if (!scenarioName.equals("Test Scenario")) {
            return scenarioName;
        }
        
        // Strategy 3: Fallback - Use method name with class context
        String methodName = result.getMethod().getMethodName();
        String className = result.getTestClass().getName();
        
        if ("runScenario".equals(methodName) && className.contains("Runner")) {
            String runnerName = className.substring(className.lastIndexOf('.') + 1)
                                        .replace("Runner", "")
                                        .replaceAll("([A-Z])", " $1")
                                        .trim();
            String fallbackName = "Scenario from " + runnerName;
            LOGGER.debug("Using generic scenario name (extraction failed): '{}'", fallbackName);
            return fallbackName;
        }
        
        return methodName;
    }
    
    /**
     * Extract scenario name from parameter string - optimized
     */
    private String extractScenarioNameFromParam(String paramString) {
        try {
            // Strategy 1: Look for PickleWrapper with scenario name
            if (paramString.contains("PickleWrapper") && paramString.contains("Scenario:")) {
                int scenarioStart = paramString.indexOf("Scenario:") + 9;
                int scenarioEnd = paramString.indexOf(",", scenarioStart);
                if (scenarioEnd == -1) scenarioEnd = paramString.indexOf("]", scenarioStart);
                if (scenarioEnd == -1) scenarioEnd = paramString.length();
                
                String extracted = paramString.substring(scenarioStart, scenarioEnd).trim();
                if (!extracted.isEmpty() && !extracted.equals("runScenario")) {
                    return extracted;
                }
            }
            
            // Strategy 2: Look for quoted scenario names
            if (paramString.contains("\"")) {
                int start = paramString.indexOf("\"");
                int end = paramString.lastIndexOf("\"");
                if (start >= 0 && end > start) {
                    String extracted = paramString.substring(start + 1, end);
                    if (extracted.length() > 5 && !extracted.equals("runScenario")) {
                        return extracted;
                    }
                }
            }
            
            // Strategy 3: Look for scenario names after "Scenario:" pattern
            if (paramString.contains("Scenario:")) {
                int start = paramString.indexOf("Scenario:") + 9;
                int end = paramString.indexOf("\n", start);
                if (end == -1) end = paramString.indexOf(",", start);
                if (end == -1) end = paramString.indexOf("]", start);
                if (end == -1) end = paramString.length();
                String extracted = paramString.substring(start, end).trim();
                if (!extracted.isEmpty() && extracted.length() > 3) {
                    return extracted;
                }
            }
            
        } catch (Exception e) {
            // Silent error handling - no logging needed
        }
        return null;
    }
    
    /**
     * Get current execution statistics
     */
    public static ExecutionStats getExecutionStats() {
        ExecutionStats stats = new ExecutionStats();
        stats.totalTests = totalTestMethods;
        stats.passed = passedTestMethods;
        stats.failed = failedTestMethods;
        stats.skipped = skippedTestMethods;
        return stats;
    }
    
    /**
     * Check if Excel reporting is enabled (public method for external access)
     */
    public static boolean isExcelReportingEnabledPublic() {
        return isExcelReportingEnabled();
    }
    
    /**
     * Manually trigger Excel report generation (for testing or manual execution)
     * ENHANCED: Added debug info for troubleshooting data collection issues
     */
    public static void generateManualReport() {
        try {
            LOGGER.info("Manual Excel generation triggered - Total: {}, Passed: {}, Failed: {}, Skipped: {}", 
                       totalTestMethods, passedTestMethods, failedTestMethods, skippedTestMethods);
            
            DailyExcelTracker.generateDailyReport();
            LOGGER.info("Manual Excel report generated successfully");
        } catch (Exception e) {
            LOGGER.error("Error generating manual Excel report", e);
        }
    }
    
    /**
     * Force Excel generation with current execution data (for immediate testing)
     */
    public static void forceGenerateReport() {
        LOGGER.info("Force generating Excel report with current data");
        generateManualReport();
    }
    
    /**
     * ENHANCED: Capture actual skip exception details from test skip (including role-based skip reasons)
     */
    private void captureSkipExceptionDetails(ITestResult result, String scenarioInfo) {
        try {
            Throwable throwable = result.getThrowable();
            LOGGER.debug("SKIP CAPTURE - Scenario: '{}', Throwable: {}", scenarioInfo, throwable != null ? throwable.getClass().getSimpleName() : "null");
            
            String skipReason = null;
            String runnerClassName = result.getTestClass().getName();
            
            if (throwable != null) {
                // Create exception details object for skip
                ExceptionDetails details = new ExceptionDetails();
                details.scenarioName = scenarioInfo;
                details.exceptionMessage = throwable.getMessage() != null ? throwable.getMessage() : "Test was skipped";
                details.exceptionType = throwable.getClass().getSimpleName();
                details.stackTrace = getStackTraceAsString(throwable);
                details.timestamp = System.currentTimeMillis();
                details.testStatus = "SKIPPED";
                
                skipReason = details.exceptionMessage;
                
                // Create a unique key for this skip
                String testKey = runnerClassName + "." + result.getMethod().getMethodName();
                testExceptionDetails.put(testKey, details);
                
                LOGGER.debug("Captured skip exception for '{}': '{}'", scenarioInfo, details.exceptionMessage);
            } else {
                // Even if no throwable, create a basic skip details entry
                ExceptionDetails details = new ExceptionDetails();
                details.scenarioName = scenarioInfo;
                details.exceptionMessage = "Test was skipped - no specific reason provided";
                details.exceptionType = "Skip";
                details.stackTrace = "";
                details.timestamp = System.currentTimeMillis();
                details.testStatus = "SKIPPED";
                
                skipReason = details.exceptionMessage;
                
                String testKey = runnerClassName + "." + result.getMethod().getMethodName();
                testExceptionDetails.put(testKey, details);
                
                LOGGER.debug("SKIP CAPTURE - No throwable, captured basic skip for '{}'. Total captured: {}", scenarioInfo, testExceptionDetails.size());
            }
            
            // ENHANCED: Universal skip reason storage for ALL features
            if (skipReason != null && scenarioInfo != null) {
                // Store by original scenario name (lowercase for case-insensitive lookup)
                String normalizedScenarioName = scenarioInfo.toLowerCase().trim();
                skipReasonsByScenario.put(normalizedScenarioName, skipReason);
                
                // ENHANCED: Also store with "scenario:" prefix removed for better matching
                String withoutPrefix = normalizedScenarioName.replaceAll("^scenario:\\s*", "");
                if (!withoutPrefix.equals(normalizedScenarioName)) {
                    skipReasonsByScenario.put(withoutPrefix, skipReason);
                }
                
                // ENHANCED: Store scenario-specific skip reasons (no global override)
                // Each scenario should have its own specific skip reason
                if (!skipReason.equals("Test was skipped - no specific reason provided") &&
                    !skipReason.equals("Test was skipped")) {
                    
                    // Store scenario-specific skip reason with detailed key
                    String detailedKey = extractRunnerKey(runnerClassName) + "_" + scenarioInfo.toLowerCase().replace(" ", "_");
                    globalSkipReasonsByRunner.put(detailedKey, skipReason);
                    LOGGER.debug("Stored scenario-specific skip reason for '{}': '{}'", detailedKey, skipReason);
                    
                    // Also store by runner key for backward compatibility, but only if it's Forward Flow
                    String runnerKey = extractRunnerKey(runnerClassName);
                    if (scenarioInfo.toLowerCase().contains("forward")) {
                        globalSkipReasonsByRunner.put(runnerKey, skipReason);
                        LOGGER.debug("Stored Forward Flow as global for runner '{}': '{}'", runnerKey, skipReason);
                    }
                }
                
                LOGGER.debug("SKIP CAPTURE - Stored skip reason by scenario name: '{}' -> '{}'", scenarioInfo, skipReason);
            }
            
        } catch (Exception e) {
            LOGGER.warn("Failed to capture skip exception details for test skip", e);
        }
    }
    
    /**
     * Extract a clean runner key from full class name for global skip reason storage
     */
    private String extractRunnerKey(String fullClassName) {
        if (fullClassName == null) return "unknown";
        
        // Extract just the runner class name (e.g., "Runner16_ValidatePCRestrictedTipMessage" from full path)
        String className = fullClassName.substring(fullClassName.lastIndexOf('.') + 1);
        
        // Clean up the runner name for key usage
        return className.toLowerCase().replace("runner", "").replace("_", "");
    }
    
    /**
     * Convert exception stack trace to string
     */
    private String getStackTraceAsString(Throwable throwable) {
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            throwable.printStackTrace(pw);
            String stackTrace = sw.toString();
            
            // Limit stack trace length for Excel cells (Excel has cell character limits)
            if (stackTrace.length() > 2000) {
                stackTrace = stackTrace.substring(0, 1900) + "\n... [Stack trace truncated for Excel display]";
            }
            
            return stackTrace;
        } catch (Exception e) {
            return "Stack trace capture failed: " + e.getMessage();
        }
    }
    
    /**
     * Get captured exception details for a test (used by Excel reporting)
     */
    public static ExceptionDetails getExceptionDetails(String testKey) {
        return testExceptionDetails.get(testKey);
    }
    
    /**
     * Get all captured exception details (used by DailyExcelTracker for skip message lookup)
     */
    public static Map<String, ExceptionDetails> getAllExceptionDetails() {
        return new HashMap<>(testExceptionDetails); // Return a copy for thread safety
    }
    
    /**
     * Get skip reason by scenario name (used by DailyExcelTracker for direct scenario lookup)
     * ENHANCED: Universal approach for ALL features, not just Feature 16
     */
    public static String getSkipReasonByScenarioName(String scenarioName) {
        if (scenarioName == null) return null;
        
        // Try exact match first
        String directMatch = skipReasonsByScenario.get(scenarioName.toLowerCase().trim());
        if (directMatch != null) {
            return directMatch;
        }
        
        // ENHANCED: Check for scenario-specific skip reasons first, then global
        LOGGER.debug("SKIP LOOKUP - Checking scenario-specific and global skip reasons. Available keys: {}", globalSkipReasonsByRunner.keySet());
        
        // First, try to find scenario-specific skip reason
        String scenarioSpecificKey = null;
        for (Map.Entry<String, String> entry : globalSkipReasonsByRunner.entrySet()) {
            String key = entry.getKey();
            if (key.contains("_") && key.contains(scenarioName.toLowerCase().replace(" ", "_"))) {
                scenarioSpecificKey = key;
                LOGGER.debug("SKIP LOOKUP - Found scenario-specific key '{}' for scenario '{}'", key, scenarioName);
                break;
            }
        }
        
        if (scenarioSpecificKey != null) {
            String scenarioSpecificReason = globalSkipReasonsByRunner.get(scenarioSpecificKey);
            LOGGER.debug("SKIP LOOKUP - Using scenario-specific skip reason: '{}'", scenarioSpecificReason);
            return scenarioSpecificReason;
        }
        
        // Fallback to global skip reason by runner
        for (Map.Entry<String, String> entry : globalSkipReasonsByRunner.entrySet()) {
            String runnerKey = entry.getKey();
            String globalSkipReason = entry.getValue();
            
            LOGGER.debug("SKIP LOOKUP - Testing runner '{}' against scenario '{}'", runnerKey, scenarioName);
            
            // Check if this scenario could belong to this runner
            if (couldScenarioBelongToRunner(scenarioName, runnerKey)) {
                LOGGER.debug("SKIP LOOKUP - Found global skip for runner '{}' and scenario '{}': '{}'", 
                           runnerKey, scenarioName, globalSkipReason);
                return globalSkipReason;
            } else {
                LOGGER.debug("SKIP LOOKUP - No match between runner '{}' and scenario '{}'", runnerKey, scenarioName);
            }
        }
        
        // Try fuzzy match with stored scenario names
        String searchKey = scenarioName.toLowerCase().trim();
        for (Map.Entry<String, String> entry : skipReasonsByScenario.entrySet()) {
            String storedScenario = entry.getKey();
            if (storedScenario.contains(searchKey) || searchKey.contains(storedScenario)) {
                return entry.getValue();
            }
        }
        
        return null;
    }
    
    /**
     * Check if a scenario could belong to a specific runner (universal matching logic)
     */
    private static boolean couldScenarioBelongToRunner(String scenarioName, String runnerKey) {
        if (scenarioName == null || runnerKey == null) return false;
        
        String lowerScenario = scenarioName.toLowerCase();
        String lowerRunner = runnerKey.toLowerCase();
        
        // ENHANCED: Feature16-specific matching
        // Runner16_ValidatePCRestrictedTipMessage should match ALL scenarios in that feature
        // These scenarios include: Navigate to User Admin, Teams, Profile Collections, Job Mapping, etc.
        if (lowerRunner.contains("16validatepcrestrictedtipmessage") || 
            lowerRunner.contains("validatepcrestrictedtipmessage")) {
            // For Feature16, match scenarios related to User Admin, Teams, Profile Collections, Job Mapping
            if (lowerScenario.contains("user admin") || 
                lowerScenario.contains("team") || 
                lowerScenario.contains("profile collection") || 
                lowerScenario.contains("job mapping") ||
                lowerScenario.contains("navigate") ||
                lowerScenario.contains("create") ||
                lowerScenario.contains("delete") ||
                lowerScenario.contains("cleanup") ||
                lowerScenario.contains("restricted") ||
                lowerScenario.contains("tip message")) {
                return true;
            }
        }
        
        // Direct keyword matching - works for any feature
        if (lowerRunner.contains("validate") && lowerScenario.contains("validate")) {
            return true;
        }
        if (lowerRunner.contains("profile") && lowerScenario.contains("profile")) {
            return true;
        }
        if (lowerRunner.contains("user") && lowerScenario.contains("user")) {
            return true;
        }
        if (lowerRunner.contains("admin") && lowerScenario.contains("admin")) {
            return true;
        }
        if (lowerRunner.contains("team") && lowerScenario.contains("team")) {
            return true;
        }
        if (lowerRunner.contains("collection") && lowerScenario.contains("collection")) {
            return true;
        }
        if (lowerRunner.contains("delete") && lowerScenario.contains("delete")) {
            return true;
        }
        if (lowerRunner.contains("create") && lowerScenario.contains("create")) {
            return true;
        }
        if (lowerRunner.contains("navigate") && lowerScenario.contains("navigate")) {
            return true;
        }
        if (lowerRunner.contains("mapping") && lowerScenario.contains("mapping")) {
            return true;
        }
        if (lowerRunner.contains("job") && lowerScenario.contains("job")) {
            return true;
        }
        if (lowerRunner.contains("login") && lowerScenario.contains("login")) {
            return true;
        }
        if (lowerRunner.contains("dashboard") && lowerScenario.contains("dashboard")) {
            return true;
        }
        
        // Feature number matching (e.g., "16" matches Feature 16 scenarios)
        if (lowerRunner.matches(".*\\d+.*")) {
            String runnerNumber = lowerRunner.replaceAll("[^0-9]", "");
            if (!runnerNumber.isEmpty()) {
                // For Feature 16, be more lenient - any Feature 16 scenario should match
                if (runnerNumber.equals("16")) {
                    return true; // Accept all scenarios for Feature 16
                }
                if (lowerScenario.contains(runnerNumber)) {
                    return true;
                }
            }
        }
        
        // Generic scenario patterns that could belong to any feature
        if (lowerScenario.contains("verify") || lowerScenario.contains("check") || 
            lowerScenario.contains("test") || lowerScenario.contains("validate")) {
            return true;
        }
        
        return false;
    }
    
    public static void clearExceptionDetails() {
        testExceptionDetails.clear();
        skipReasonsByScenario.clear(); // Also clear skip reasons
        globalSkipReasonsByRunner.clear(); // Also clear global skip reasons
        clearPerformanceMetrics(); // Also clear performance metrics
    }
    
    /**
     * PERFORMANCE METRICS: Store performance test metrics for Feature 41
     */
    public static class PerformanceMetrics {
        public String scenarioName;
        public long thresholdTimeMs;
        public long actualTimeMs;
        public String performanceRating; // EXCELLENT, GOOD, ACCEPTABLE, POOR, VERY POOR
        public String operationName; // e.g., "Page Load", "Search Operation"
        public long timestamp;
        
        public PerformanceMetrics(String scenarioName, long thresholdTimeMs, long actualTimeMs, String performanceRating, String operationName) {
            this.scenarioName = scenarioName;
            this.thresholdTimeMs = thresholdTimeMs;
            this.actualTimeMs = actualTimeMs;
            this.performanceRating = performanceRating;
            this.operationName = operationName;
            this.timestamp = System.currentTimeMillis();
        }
        
        public String getFormattedMetricsForExcel() {
            return String.format("Threshold: %d ms | Actual: %d ms | Performance Rating: %s",
                thresholdTimeMs, actualTimeMs, performanceRating);
        }
    }
    
    /**
     * PERFORMANCE METRICS: Store performance metrics for a scenario
     * Called from PO41_ValidateApplicationPerformance_JAM_and_HCM
     */
    public static void setPerformanceMetrics(String scenarioName, long thresholdTimeMs, long actualTimeMs, 
                                             String performanceRating, String operationName) {
        if (scenarioName != null && !scenarioName.trim().isEmpty()) {
            PerformanceMetrics metrics = new PerformanceMetrics(scenarioName, thresholdTimeMs, actualTimeMs, 
                                                                performanceRating, operationName);
            scenarioPerformanceMetrics.put(scenarioName, metrics);
            LOGGER.debug("Performance metrics stored for scenario: {} | Threshold: {} ms | Actual: {} ms | Rating: {}",
                        scenarioName, thresholdTimeMs, actualTimeMs, performanceRating);
        }
    }
    
    /**
     * PERFORMANCE METRICS: Get performance metrics for a scenario
     */
    public static PerformanceMetrics getPerformanceMetrics(String scenarioName) {
        return scenarioPerformanceMetrics.get(scenarioName);
    }
    
    /**
     * PERFORMANCE METRICS: Clear all performance metrics (called after Excel generation)
     */
    public static void clearPerformanceMetrics() {
        scenarioPerformanceMetrics.clear();
        LOGGER.debug("Cleared all performance metrics");
    }
    
    public static class ExceptionDetails {
        public String scenarioName;
        public String exceptionMessage;
        public String exceptionType;
        public String stackTrace;
        public long timestamp;
        public String testStatus; // ADDED: Track whether this is FAILED or SKIPPED
       
        public String getFormattedExceptionForExcel() {
            StringBuilder sb = new StringBuilder();
            
            // For skip exceptions, prioritize the actual skip message
            if ("SKIPPED".equals(testStatus)) {
                if (exceptionMessage != null && !exceptionMessage.trim().isEmpty() && 
                    !exceptionMessage.equals("Test was skipped")) {
                    // Use the actual skip reason (e.g., role-based skip message)
                    return exceptionMessage;
                } else {
                    return "Test was skipped - no specific reason provided";
                }
            }
            
            // For failed tests, use the detailed exception format
            sb.append("Exception: ").append(exceptionType).append("\n");
            sb.append("Message: ").append(exceptionMessage != null ? exceptionMessage : "No message provided").append("\n");
            
            // Add key stack trace lines (not full stack trace for readability)
            if (stackTrace != null) {
                String[] stackLines = stackTrace.split("\n");
                sb.append("Key Stack Trace:\n");
                int lineCount = 0;
                for (String line : stackLines) {
                    if (lineCount >= 5) break; // Limit to 5 key lines for Excel readability
                    if (line.trim().startsWith("at ") && (line.contains("com.kfonetalentsuite") || line.contains("org.testng") || lineCount < 2)) {
                        sb.append("  ").append(line.trim()).append("\n");
                        lineCount++;
                    }
                }
            }
            
            return sb.toString().trim();
        }
    }
    
    public static class ExecutionStats {
        public int totalTests;
        public int passed;
        public int failed;
        public int skipped;
        
        public double getSuccessRate() {
            return totalTests > 0 ? (double) passed / totalTests * 100 : 0;
        }
        
        @Override
        public String toString() {
            return String.format("ExecutionStats{total=%d, passed=%d, failed=%d, skipped=%d, successRate=%.1f%%}", 
                               totalTests, passed, failed, skipped, getSuccessRate());
        }
    }
}
