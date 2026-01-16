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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Map;
import java.util.HashMap;

import com.kfonetalentsuite.utils.JobMapping.DailyExcelTracker;
import com.kfonetalentsuite.utils.common.CommonVariableManager;
import com.kfonetalentsuite.utils.JobMapping.KeepAwakeUtil;
import com.kfonetalentsuite.utils.common.ScreenshotHandler;
import java.io.StringWriter;
import java.io.PrintWriter;

public class ExcelReportListener implements IExecutionListener, ISuiteListener, ITestListener, IInvokedMethodListener {

	private static final Logger LOGGER = LogManager.getLogger(ExcelReportListener.class);

	// Configuration flag to enable/disable Excel generation (unified with
	// DailyExcelTracker)
	private static boolean isExcelReportingEnabled() {
		return CommonVariableManager.EXCEL_REPORTING_ENABLED == null
				|| !CommonVariableManager.EXCEL_REPORTING_ENABLED.equalsIgnoreCase("false");
	}

	// RETRY FIX: Flag to detect retry runs and avoid counter reset
	private static volatile boolean isRetryRun = false;
	
	// RETRY FIX: Track if we're within a retry execution to skip Excel generation during retry
	private static volatile boolean skipExcelGenerationDuringRetry = false;
	
	// RETRY FIX: Track scenarios that passed in retry (to update Excel report)
	private static final Map<String, String> scenariosPassedInRetry = new ConcurrentHashMap<>();

	// Execution statistics - Thread-safe counters for parallel execution
	private static final AtomicInteger totalTestMethods = new AtomicInteger(0);
	private static final AtomicInteger passedTestMethods = new AtomicInteger(0);
	private static final AtomicInteger failedTestMethods = new AtomicInteger(0);
	private static final AtomicInteger skippedTestMethods = new AtomicInteger(0);

	// Thread-safe storage for current scenario information
	private static final Map<String, String> currentScenarios = new ConcurrentHashMap<>();

	// Store cross-browser runner information
	private static final Map<String, String> crossBrowserRunnerNames = new ConcurrentHashMap<>();

	// ENHANCED: Store actual exception details for Excel reporting
	private static final Map<String, ExceptionDetails> testExceptionDetails = new ConcurrentHashMap<>();

	// ENHANCED: Store skip reasons by scenario name for easier lookup during Excel
	// generation
	private static final Map<String, String> skipReasonsByScenario = new ConcurrentHashMap<>();

	// ENHANCED: Store global skip reasons by test runner class for features with
	// multiple scenarios
	private static final Map<String, String> globalSkipReasonsByRunner = new ConcurrentHashMap<>();

	// PERFORMANCE METRICS: Store performance test metrics for Feature 41
	// (Performance Testing)
	private static final Map<String, PerformanceMetrics> scenarioPerformanceMetrics = new ConcurrentHashMap<>();

	// Track completed test classes for incremental updates
	private static final Map<String, Integer> completedMethodsPerClass = new ConcurrentHashMap<>();
	private static final Map<String, Integer> totalMethodsPerClass = new ConcurrentHashMap<>();

	// ENHANCED: Progress tracking for test suite execution - Thread-safe for
	// parallel execution
	private static final AtomicInteger totalTestContexts = new AtomicInteger(0);
	private static final AtomicInteger completedTestContexts = new AtomicInteger(0);

	// ENHANCED: Store current suite name for Excel reporting (Suite vs Individual
	// Runner execution)
	private static volatile String currentSuiteName = null;

	// ========================================
	// PROGRESS BAR FUNCTIONALITY (Merged from ProgressBarUtil)
	// ========================================

	// Progress tracking
	private static int totalRunners = 0;
	private static int completedRunners = 0;

	// Configuration
	private static final boolean PROGRESS_BAR_ENABLED = Boolean
			.parseBoolean(System.getProperty("progress.bar.enabled", "true"));

	// ANSI Color codes for console output
	private static final String ANSI_RESET = "\u001B[0m";
	private static final String ANSI_GREEN = "\u001B[32m";
	private static final String ANSI_BRIGHT_GREEN = "\u001B[92m";
	private static final String ANSI_YELLOW = "\u001B[33m";
	private static final String ANSI_CYAN = "\u001B[36m";
	private static final String ANSI_DARK_GREY = "\u001B[90m";

	// Progress bar characters - solid blocks for clean appearance
	private static final String PROGRESS_FILLED = "-";
	private static final String PROGRESS_EMPTY = "-";
	private static final String PROGRESS_ARROW = "-";

	public static void setCurrentScenario(String threadId, String scenarioName) {
		currentScenarios.put(threadId, scenarioName);
	}

	public static String getCurrentSuiteName() {
		return currentSuiteName;
	}

	public static String getCurrentScenarioName(String threadId) {
		return currentScenarios.get(threadId);
	}

	public static void clearCurrentScenario(String threadId) {
		currentScenarios.remove(threadId);
	}

	private void setCrossBrowserRunnerInfo(ITestResult result) {
		try {
			String testClassName = result.getTestClass().getName();

			// Check if this is a cross-browser test
			if (testClassName.contains("CrossBrowser") || testClassName.contains("crossbrowser")) {
				// Extract browser name from parameters
				Object[] parameters = result.getParameters();
				if (parameters != null && parameters.length > 0) {
					String browserName = String.valueOf(parameters[0]);
					if (browserName != null && (browserName.equals("chrome") || browserName.equals("firefox")
							|| browserName.equals("edge"))) {
						// Store the runner class name with browser info
						String threadId = Thread.currentThread().getName();
						crossBrowserRunnerNames.put(threadId, testClassName);

						// Also store for DailyExcelTracker
						System.setProperty("current.runner.class." + threadId, testClassName);
						System.setProperty("current.browser.name." + threadId, browserName);
					}
				}
			}
		} catch (Exception e) {
			// Silent - not critical for test execution
		}
	}

	public static String getCrossBrowserRunnerName(String threadId) {
		return crossBrowserRunnerNames.get(threadId);
	}

	private String getCurrentScenarioName() {
		String threadId = Thread.currentThread().getName();
		String scenarioName = currentScenarios.get(threadId);
		return scenarioName != null ? scenarioName : "Test Scenario";
	}

	@Override
	public void onExecutionStart() {
		// SYSTEM POWER MANAGEMENT: Initialize Keep System Awake at suite level (ONCE
		// for entire execution)
		KeepAwakeUtil.initialize();

		if (!isExcelReportingEnabled()) {
			return;
		}

		// RETRY FIX: Don't reset counters during retry runs - accumulate results instead
		if (isRetryRun) {
			LOGGER.debug("Retry run detected - preserving existing counters and exception details");
			// Don't reset counters during retry - we want to accumulate results
			return;
		}

		// Reset counters and add logging for debugging (only on first/original run)
		totalTestMethods.set(0);
		passedTestMethods.set(0);
		failedTestMethods.set(0);
		skippedTestMethods.set(0);

		// Reset class tracking for incremental updates
		completedMethodsPerClass.clear();
		totalMethodsPerClass.clear();

		// ENHANCED: Reset progress tracking
		totalTestContexts.set(0);
		completedTestContexts.set(0);

		// PERFORMANCE OPTIMIZATION: Reset screenshot counters for clean tracking
		ScreenshotHandler.resetCounters();
	}

	// ISuiteListener implementation - for suite-level tracking
	@Override
	public void onStart(org.testng.ISuite suite) {
		if (!isExcelReportingEnabled()) {
			return;
		}

		// Store suite name for Excel reporting
		currentSuiteName = suite.getName();
		LOGGER.debug("Suite: '{}'", currentSuiteName);

		// Count total test contexts (runners) and initialize progress bar
		try {
			Map<String, org.testng.xml.XmlTest> allTests = suite.getXmlSuite().getTests().stream()
					.collect(java.util.stream.Collectors.toMap(org.testng.xml.XmlTest::getName, test -> test));

			totalTestContexts.set(allTests.size());

			if (totalTestContexts.get() > 0) {
				LOGGER.debug("Suite '{}' - {} runners", suite.getName(), totalTestContexts.get());
				initializeProgress(totalTestContexts.get());
			}
		} catch (Exception e) {
			LOGGER.debug("Progress tracking init failed");
			totalTestContexts.set(0);
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

		// CRITICAL: Cache user/client info immediately after runner completes
		// This ensures the cache is populated for suite-level reporting
		DailyExcelTracker.cacheUserAndClientInfo();

		// ENHANCED: Update progress bar when runner completes
		if (totalTestContexts.get() > 0) {
			completedTestContexts.incrementAndGet();
			String testName = context.getName();
			updateProgress(testName);
		}

		// Individual test completed - Excel update deferred to execution completion
	}

	@Override
	public void onExecutionFinish() {
		LOGGER.debug("Execution finished - Total: {}, Passed: {}, Failed: {}, Skipped: {}", totalTestMethods.get(),
				passedTestMethods.get(), failedTestMethods.get(), skippedTestMethods.get());

		if (!isExcelReportingEnabled()) {
			KeepAwakeUtil.shutdown();
			return;
		}

		// Skip Excel generation during retry - will be generated after retry completes
		if (skipExcelGenerationDuringRetry) {
			LOGGER.debug("Deferring Excel generation until retry completes");
			return;
		}

		try {
			// Log scenarios that passed in retry (condensed)
			Map<String, String> retryPassed = getScenariosPassedInRetry();
			if (!retryPassed.isEmpty()) {
				LOGGER.info("✅ {} scenario(s) passed in retry", retryPassed.size());
			}
			
			DailyExcelTracker.generateDailyReport(false);
			LOGGER.debug("Excel report generated");

			clearExceptionDetails();
			clearScenariosPassedInRetry();
			crossBrowserRunnerNames.clear();
			resetProgress();
			System.gc();

		} catch (Exception e) {
			LOGGER.error("Excel report generation failed", e);
		} finally {
			if (!isRetryRun) {
				KeepAwakeUtil.shutdown();
			}
		}
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		passedTestMethods.incrementAndGet();
		totalTestMethods.incrementAndGet();
		
		// Track scenarios that passed in retry run
		if (isRetryRun) {
			String scenarioName = extractScenarioInfo(result);
			if (scenarioName != null && !scenarioName.isEmpty() && !scenarioName.equals("Test Scenario")) {
				scenariosPassedInRetry.put(scenarioName.toLowerCase().trim(), "PASSED");
				LOGGER.debug("Retry PASSED: '{}'", scenarioName);
			}
		}
	}

	@Override
	public void onTestFailure(ITestResult result) {
		failedTestMethods.incrementAndGet();
		totalTestMethods.incrementAndGet();

		// ENHANCED: Capture actual exception details for Excel reporting
		if (isExcelReportingEnabled()) {
			String scenarioInfo = extractScenarioInfo(result);
			LOGGER.debug("Test failed: {}", scenarioInfo);

			// ENHANCED: Capture comprehensive failure details with actual exception message
			captureFailureExceptionDetails(result, scenarioInfo);

			// Clear scenario name from thread after capturing exception (prevent memory
			// leaks)
			String threadId = Thread.currentThread().getName();
			clearCurrentScenario(threadId);
		}
	}

	private void captureFailureExceptionDetails(ITestResult result, String scenarioInfo) {
		try {
			Throwable throwable = result.getThrowable();
			String runnerClassName = result.getTestClass().getName();

			if (throwable != null) {
				ExceptionDetails details = new ExceptionDetails();
				details.scenarioName = scenarioInfo;
				details.exceptionMessage = extractMostMeaningfulExceptionMessage(throwable);
				details.exceptionType = throwable.getClass().getSimpleName();
				details.stackTrace = getStackTraceAsString(throwable);
				details.timestamp = System.currentTimeMillis();
				details.testStatus = "FAILED";

				String testKey = runnerClassName + "." + result.getMethod().getMethodName() + "." + scenarioInfo;
				testExceptionDetails.put(testKey, details);

				if (details.exceptionMessage != null && details.exceptionMessage.length() > 10) {
					String runnerKey = extractRunnerKey(runnerClassName);
					globalSkipReasonsByRunner.put(runnerKey + "_failure", details.exceptionMessage);
				}

			} else {
				ExceptionDetails details = new ExceptionDetails();
				details.scenarioName = scenarioInfo;
				details.exceptionMessage = "Test failed - no exception details available";
				details.exceptionType = "UnknownFailure";
				details.stackTrace = "";
				details.timestamp = System.currentTimeMillis();
				details.testStatus = "FAILED";

				String testKey = runnerClassName + "." + result.getMethod().getMethodName() + "." + scenarioInfo;
				testExceptionDetails.put(testKey, details);
			}

		} catch (Exception e) {
			LOGGER.debug("Could not capture failure details");
		}
	}

	private String extractMostMeaningfulExceptionMessage(Throwable throwable) {
		if (throwable == null)
			return "Unknown failure";

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

	private boolean isMoreMeaningfulMessage(String newMessage, String currentBest) {
		if (newMessage == null)
			return false;
		if (currentBest == null)
			return true;

		String newLower = newMessage.toLowerCase();
		String currentLower = currentBest.toLowerCase();

		// Prioritize messages with specific error indicators
		String[] meaningfulKeywords = { "assertion", "expected", "actual", "timeout", "not found", "invalid",
				"failed to", "unable to", "cannot", "error", "exception", "missing", "required", "forbidden",
				"unauthorized", "not available", "not supported" };

		int newScore = 0;
		int currentScore = 0;

		for (String keyword : meaningfulKeywords) {
			if (newLower.contains(keyword))
				newScore++;
			if (currentLower.contains(keyword))
				currentScore++;
		}

		// Longer messages with specific keywords are generally more meaningful
		if (newScore > currentScore)
			return true;
		if (newScore == currentScore && newMessage.length() > currentBest.length())
			return true;

		return false;
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		skippedTestMethods.incrementAndGet();
		totalTestMethods.incrementAndGet();

		if (isExcelReportingEnabled()) {
			String scenarioInfo = extractScenarioInfo(result);
			captureSkipExceptionDetails(result, scenarioInfo);

			String threadId = Thread.currentThread().getName();
			clearCurrentScenario(threadId);
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

	// IInvokedMethodListener implementation - for tracking individual method
	// completion
	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
		if (!isExcelReportingEnabled() || !method.isTestMethod()) {
			return;
		}

		String className = testResult.getTestClass().getName();
		// Count total methods per class
		totalMethodsPerClass.merge(className, 1, (oldValue, value) -> oldValue + value);
	}

	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
		if (!isExcelReportingEnabled() || !method.isTestMethod()) {
			return;
		}

		String className = testResult.getTestClass().getName();

		// Count completed methods per class
		int completedMethods = completedMethodsPerClass.merge(className, 1, (oldValue, value) -> oldValue + value);
		int totalMethods = totalMethodsPerClass.getOrDefault(className, 0);

		// Check if this class (runner) has completed all its methods
		if (completedMethods >= totalMethods) {
			// Reset tracking for this class - runner completed
			completedMethodsPerClass.remove(className);
			totalMethodsPerClass.remove(className);
		}
	}

	private String extractScenarioInfo(ITestResult result) {
		// Strategy 0: PRIORITY - Try current scenario name from thread context FIRST
		// (most reliable for Cucumber scenarios)
		String scenarioName = getCurrentScenarioName();
		if (scenarioName != null && !scenarioName.equals("Test Scenario") && scenarioName.length() > 5) {
			return scenarioName;
		}

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
								String extractedName = (String) pickle.getClass().getMethod("getName").invoke(pickle);
								if (extractedName != null && extractedName.length() > 5) {
									return extractedName;
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

		// Strategy 2: Fallback - Use method name with class context
		String methodName = result.getMethod().getMethodName();
		String className = result.getTestClass().getName();

		if ("runScenario".equals(methodName) && className.contains("Runner")) {
			String runnerName = className.substring(className.lastIndexOf('.') + 1).replace("Runner", "")
					.replaceAll("([A-Z])", " $1").trim();
			return "Scenario from " + runnerName;
		}

		return methodName;
	}

	private String extractScenarioNameFromParam(String paramString) {
		try {
			// Strategy 1: Look for PickleWrapper with scenario name
			if (paramString.contains("PickleWrapper") && paramString.contains("Scenario:")) {
				int scenarioStart = paramString.indexOf("Scenario:") + 9;
				int scenarioEnd = paramString.indexOf(",", scenarioStart);
				if (scenarioEnd == -1)
					scenarioEnd = paramString.indexOf("]", scenarioStart);
				if (scenarioEnd == -1)
					scenarioEnd = paramString.length();

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
				if (end == -1)
					end = paramString.indexOf(",", start);
				if (end == -1)
					end = paramString.indexOf("]", start);
				if (end == -1)
					end = paramString.length();
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

	public static ExecutionStats getExecutionStats() {
		ExecutionStats stats = new ExecutionStats();
		stats.totalTests = totalTestMethods.get();
		stats.passed = passedTestMethods.get();
		stats.failed = failedTestMethods.get();
		stats.skipped = skippedTestMethods.get();
		return stats;
	}

	public static boolean isExcelReportingEnabledPublic() {
		return isExcelReportingEnabled();
	}

	public static void generateManualReport() {
		try {
			LOGGER.debug("Manual Excel generation - Total: {}, Passed: {}, Failed: {}, Skipped: {}",
					totalTestMethods.get(), passedTestMethods.get(), failedTestMethods.get(), skippedTestMethods.get());
			DailyExcelTracker.generateDailyReport();
			LOGGER.debug("Manual Excel report generated");
		} catch (Exception e) {
			LOGGER.error("Error generating manual Excel report", e);
		}
	}

	public static void forceGenerateReport() {
		LOGGER.debug("Force generating Excel report");
		generateManualReport();
	}

	private void captureSkipExceptionDetails(ITestResult result, String scenarioInfo) {
		try {
			Throwable throwable = result.getThrowable();
			String skipReason = null;
			String runnerClassName = result.getTestClass().getName();

			if (throwable != null) {
				ExceptionDetails details = new ExceptionDetails();
				details.scenarioName = scenarioInfo;
				details.exceptionMessage = throwable.getMessage() != null ? throwable.getMessage() : "Test was skipped";
				details.exceptionType = throwable.getClass().getSimpleName();
				details.stackTrace = getStackTraceAsString(throwable);
				details.timestamp = System.currentTimeMillis();
				details.testStatus = "SKIPPED";

				skipReason = details.exceptionMessage;

				String testKey = runnerClassName + "." + result.getMethod().getMethodName() + "." + scenarioInfo;
				testExceptionDetails.put(testKey, details);
			} else {
				ExceptionDetails details = new ExceptionDetails();
				details.scenarioName = scenarioInfo;
				details.exceptionMessage = "Test was skipped - no specific reason provided";
				details.exceptionType = "Skip";
				details.stackTrace = "";
				details.timestamp = System.currentTimeMillis();
				details.testStatus = "SKIPPED";

				skipReason = details.exceptionMessage;

				String testKey = runnerClassName + "." + result.getMethod().getMethodName() + "." + scenarioInfo;
				testExceptionDetails.put(testKey, details);
			}

			// Store skip reasons for Excel reporting
			if (skipReason != null && scenarioInfo != null) {
				String normalizedScenarioName = scenarioInfo.toLowerCase().trim();
				skipReasonsByScenario.put(normalizedScenarioName, skipReason);

				String withoutPrefix = normalizedScenarioName.replaceAll("^scenario:\\s*", "");
				if (!withoutPrefix.equals(normalizedScenarioName)) {
					skipReasonsByScenario.put(withoutPrefix, skipReason);
				}

				if (!skipReason.equals("Test was skipped - no specific reason provided")
						&& !skipReason.equals("Test was skipped")) {

					String detailedKey = extractRunnerKey(runnerClassName) + "_"
							+ scenarioInfo.toLowerCase().replace(" ", "_");
					globalSkipReasonsByRunner.put(detailedKey, skipReason);

					String runnerKey = extractRunnerKey(runnerClassName);
					if (scenarioInfo.toLowerCase().contains("forward")) {
						globalSkipReasonsByRunner.put(runnerKey, skipReason);
					}
				}
			}

		} catch (Exception e) {
			LOGGER.debug("Could not capture skip details");
		}
	}

	private String extractRunnerKey(String fullClassName) {
		if (fullClassName == null)
			return "unknown";

		// Extract just the runner class name (e.g.,
		// "Runner16_ValidatePCRestrictedTipMessage" from full path)
		String className = fullClassName.substring(fullClassName.lastIndexOf('.') + 1);

		// Clean up the runner name for key usage
		return className.toLowerCase().replace("runner", "").replace("_", "");
	}

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

	public static ExceptionDetails getExceptionDetails(String testKey) {
		return testExceptionDetails.get(testKey);
	}

	public static Map<String, ExceptionDetails> getAllExceptionDetails() {
		return new HashMap<>(testExceptionDetails); // Return a copy for thread safety
	}

	public static String getSkipReasonByScenarioName(String scenarioName) {
		if (scenarioName == null)
			return null;

		// Try exact match first
		String directMatch = skipReasonsByScenario.get(scenarioName.toLowerCase().trim());
		if (directMatch != null) {
			return directMatch;
		}

		// Try scenario-specific skip reason
		String scenarioSpecificKey = null;
		for (Map.Entry<String, String> entry : globalSkipReasonsByRunner.entrySet()) {
			String key = entry.getKey();
			if (key.contains("_") && key.contains(scenarioName.toLowerCase().replace(" ", "_"))) {
				scenarioSpecificKey = key;
				break;
			}
		}

		if (scenarioSpecificKey != null) {
			return globalSkipReasonsByRunner.get(scenarioSpecificKey);
		}

		// Fallback to global skip reason by runner
		for (Map.Entry<String, String> entry : globalSkipReasonsByRunner.entrySet()) {
			String runnerKey = entry.getKey();
			String globalSkipReason = entry.getValue();

			if (couldScenarioBelongToRunner(scenarioName, runnerKey)) {
				return globalSkipReason;
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

	private static boolean couldScenarioBelongToRunner(String scenarioName, String runnerKey) {
		if (scenarioName == null || runnerKey == null)
			return false;

		String lowerScenario = scenarioName.toLowerCase();
		String lowerRunner = runnerKey.toLowerCase();

		// ENHANCED: Feature16-specific matching
		// Runner16_ValidatePCRestrictedTipMessage should match ALL scenarios in that
		// feature
		// These scenarios include: Navigate to User Admin, Teams, Profile Collections,
		// Job Mapping, etc.
		if (lowerRunner.contains("16validatepcrestrictedtipmessage")
				|| lowerRunner.contains("validatepcrestrictedtipmessage")) {
			// For Feature16, match scenarios related to User Admin, Teams, Profile
			// Collections, Job Mapping
			if (lowerScenario.contains("user admin") || lowerScenario.contains("team")
					|| lowerScenario.contains("profile collection") || lowerScenario.contains("job mapping")
					|| lowerScenario.contains("navigate") || lowerScenario.contains("create")
					|| lowerScenario.contains("delete") || lowerScenario.contains("cleanup")
					|| lowerScenario.contains("restricted") || lowerScenario.contains("tip message")) {
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
		if (lowerScenario.contains("verify") || lowerScenario.contains("check") || lowerScenario.contains("test")
				|| lowerScenario.contains("validate")) {
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

	public static void markRetryRunStart() {
		isRetryRun = true;
		skipExcelGenerationDuringRetry = true;
		LOGGER.debug("Retry run started");
	}

	public static void markRetryRunComplete() {
		isRetryRun = false;
		skipExcelGenerationDuringRetry = false;
		LOGGER.debug("Retry run complete");
	}

	public static boolean isInRetryRun() {
		return isRetryRun;
	}

	public static boolean didScenarioPassInRetry(String scenarioName) {
		if (scenarioName == null) return false;
		return scenariosPassedInRetry.containsKey(scenarioName.toLowerCase().trim());
	}

	public static Map<String, String> getScenariosPassedInRetry() {
		return new HashMap<>(scenariosPassedInRetry);
	}

	public static void clearScenariosPassedInRetry() {
		int count = scenariosPassedInRetry.size();
		scenariosPassedInRetry.clear();
		if (count > 0) {
			LOGGER.debug("Cleared {} retry-passed scenarios", count);
		}
	}

	public static class PerformanceMetrics {
		public String scenarioName;
		public long thresholdTimeMs;
		public long actualTimeMs;
		public String performanceRating; // EXCELLENT, GOOD, ACCEPTABLE, POOR, VERY POOR
		public String operationName; // e.g., "Page Load", "Search Operation"
		public long timestamp;

		public PerformanceMetrics(String scenarioName, long thresholdTimeMs, long actualTimeMs,
				String performanceRating, String operationName) {
			this.scenarioName = scenarioName;
			this.thresholdTimeMs = thresholdTimeMs;
			this.actualTimeMs = actualTimeMs;
			this.performanceRating = performanceRating;
			this.operationName = operationName;
			this.timestamp = System.currentTimeMillis();
		}

		public String getFormattedMetricsForExcel() {
			return String.format("Threshold: %d ms | Actual: %d ms | Performance Rating: %s", thresholdTimeMs,
					actualTimeMs, performanceRating);
		}
	}

	public static void setPerformanceMetrics(String scenarioName, long thresholdTimeMs, long actualTimeMs,
			String performanceRating, String operationName) {
		if (scenarioName != null && !scenarioName.trim().isEmpty()) {
			PerformanceMetrics metrics = new PerformanceMetrics(scenarioName, thresholdTimeMs, actualTimeMs,
					performanceRating, operationName);
			scenarioPerformanceMetrics.put(scenarioName, metrics);
		}
	}

	public static PerformanceMetrics getPerformanceMetrics(String scenarioName) {
		return scenarioPerformanceMetrics.get(scenarioName);
	}

	public static void clearPerformanceMetrics() {
		scenarioPerformanceMetrics.clear();
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
				if (exceptionMessage != null && !exceptionMessage.trim().isEmpty()
						&& !exceptionMessage.equals("Test was skipped")) {
					// Use the actual skip reason (e.g., role-based skip message)
					return exceptionMessage;
				} else {
					return "Test was skipped - no specific reason provided";
				}
			}

			// For failed tests, use the detailed exception format
			sb.append("Exception: ").append(exceptionType).append("\n");
			sb.append("Message: ").append(exceptionMessage != null ? exceptionMessage : "No message provided")
					.append("\n");

			// Add key stack trace lines (not full stack trace for readability)
			if (stackTrace != null) {
				String[] stackLines = stackTrace.split("\n");
				sb.append("Key Stack Trace:\n");
				int lineCount = 0;
				for (String line : stackLines) {
					if (lineCount >= 5)
						break; // Limit to 5 key lines for Excel readability
					if (line.trim().startsWith("at ") && (line.contains("com.kfonetalentsuite")
							|| line.contains("org.testng") || lineCount < 2)) {
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

	// ========================================
	// PROGRESS BAR METHODS (Merged from ProgressBarUtil)
	// ========================================

	private static synchronized void initializeProgress(int totalCount) {
		// Only show progress bar for test suites (more than 1 runner)
		if (!PROGRESS_BAR_ENABLED || totalCount <= 1) {
			return;
		}

		totalRunners = totalCount;
		completedRunners = 0;

		displayProgressBar("Initializing...", false);
	}

	private static synchronized void updateProgress(String runnerName) {
		if (!PROGRESS_BAR_ENABLED || totalRunners <= 0) {
			return;
		}

		completedRunners++;

		String cleanRunnerName = cleanRunnerName(runnerName);

		displayProgressBar("... Completed: " + cleanRunnerName, true);

		if (completedRunners >= totalRunners) {
			logWithProgress(" All runners completed successfully! ({}/{})", completedRunners, totalRunners);
		}
	}

	private static void displayProgressBar(String statusMessage, boolean showCompleted) {
		if (totalRunners <= 0)
			return;

		double progressPercent = (double) completedRunners / totalRunners;
		int barWidth = 40;
		int filledWidth = (int) (progressPercent * barWidth);

		StringBuilder progressBar = new StringBuilder();

		if (supportsColors()) {
			progressBar.append("[");
			for (int i = 0; i < barWidth; i++) {
				if (i < filledWidth) {
					progressBar.append(ANSI_BRIGHT_GREEN).append(PROGRESS_FILLED).append(ANSI_RESET);
				} else if (i == filledWidth && showCompleted) {
					progressBar.append(ANSI_GREEN).append(PROGRESS_ARROW).append(ANSI_RESET);
				} else {
					progressBar.append(ANSI_DARK_GREY).append(PROGRESS_EMPTY).append(ANSI_RESET);
				}
			}
			progressBar.append("]");

			progressBar.append(ANSI_CYAN).append(String.format(" %3.0f%% ", progressPercent * 100)).append(ANSI_YELLOW)
					.append(String.format("(%d/%d runners)", completedRunners, totalRunners)).append(ANSI_RESET);
		} else {
			progressBar.append("[");
			for (int i = 0; i < barWidth; i++) {
				if (i < filledWidth) {
					progressBar.append("=");
				} else if (i == filledWidth && showCompleted) {
					progressBar.append(">");
				} else {
					progressBar.append("-");
				}
			}
			progressBar.append("]");
			progressBar.append(
					String.format(" %3.0f%% (%d/%d runners)", progressPercent * 100, completedRunners, totalRunners));
		}

		if (statusMessage != null && !statusMessage.isEmpty()) {
			LOGGER.info(" PROGRESS: {} {}", statusMessage, progressBar.toString());
		} else {
			LOGGER.info(" PROGRESS: {}", progressBar.toString());
		}
	}

	private static String cleanRunnerName(String runnerName) {
		if (runnerName == null)
			return "Unknown Runner";

		String cleaned = runnerName;
		if (cleaned.contains(".")) {
			cleaned = cleaned.substring(cleaned.lastIndexOf('.') + 1);
		}
		if (cleaned.endsWith("Runner")) {
			cleaned = cleaned.substring(0, cleaned.length() - 6);
		}

		cleaned = cleaned.replaceAll("([A-Z])", " $1").trim();

		if (cleaned.length() > 35) {
			cleaned = cleaned.substring(0, 32) + "...";
		}

		return cleaned;
	}

	private static boolean supportsColors() {
		boolean forceColors = Boolean.parseBoolean(System.getProperty("progress.colors.force", "false"));
		if (forceColors) {
			return true;
		}

		boolean colorsDisabled = Boolean.parseBoolean(System.getProperty("progress.colors.disabled", "false"));
		if (colorsDisabled) {
			return false;
		}

		String logAppenders = System.getProperty("log4j.configuration.appenders", "");
		boolean isConsoleLogging = logAppenders.contains("console") || logAppenders.isEmpty();

		String term = System.getenv("TERM");
		String os = System.getProperty("os.name", "").toLowerCase();
		boolean isWindows = os.contains("windows");

		boolean isTerminal = term != null && !term.equals("dumb");
		if (isWindows) {
			String wtSession = System.getenv("WT_SESSION");
			String conEmuANSI = System.getenv("ConEmuANSI");
			isTerminal = isTerminal || wtSession != null || conEmuANSI != null || forceColors;
		}

		boolean colorsSupported = isConsoleLogging && isTerminal;

		return colorsSupported;
	}

	private static void logWithProgress(String message, Object... args) {
		if (PROGRESS_BAR_ENABLED) {
			LOGGER.info(" " + message, args);
		}
	}

	private static synchronized void resetProgress() {
		totalRunners = 0;
		completedRunners = 0;
	}
}
