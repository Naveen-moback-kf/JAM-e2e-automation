package com.kfonetalentsuite.testNGAnalyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

public class SuiteRetryListener implements ISuiteListener {

	private static final Logger LOGGER = LogManager.getLogger(SuiteRetryListener.class);

	// Maximum retry attempts for failed runners
	private static final int MAX_RETRY_ATTEMPTS = 2;

	// Track failed test classes across the suite (thread-safe)
	private static final Map<String, FailedTestInfo> failedTests = new ConcurrentHashMap<>();

	// Track retry attempts per test class
	private static final Map<String, Integer> retryAttempts = new ConcurrentHashMap<>();

	// Flag to indicate if we're in retry mode (to prevent infinite loops)
	private static volatile boolean isRetryRun = false;

	@Override
	public void onStart(ISuite suite) {
		// Only clear on first run, not on retry runs
		if (!isRetryRun) {
			failedTests.clear();
			retryAttempts.clear();
			LOGGER.info("╔═══════════════════════════════════════════════════════════════╗");
			LOGGER.info("║     SUITE RETRY LISTENER - Retry at End of Suite Enabled      ║");
			LOGGER.info("║     Max Retry Attempts: {}                                     ║", MAX_RETRY_ATTEMPTS);
			LOGGER.info("╚═══════════════════════════════════════════════════════════════╝");
		}
	}

	@Override
	public void onFinish(ISuite suite) {
		// Collect failed tests from this suite run
		collectFailedTests(suite);

		// If we're already in a retry run, just collect results and return
		// The retry loop in executeRetriesWithLoop() will handle subsequent retries
		if (isRetryRun) {
			LOGGER.debug("Retry run completed - results collected, returning to retry loop");
			return;
		}

		// Check if there are failed tests to retry
		List<FailedTestInfo> testsToRetry = getTestsToRetry();

		if (testsToRetry.isEmpty()) {
			LOGGER.info("╔═══════════════════════════════════════════════════════════════╗");
			LOGGER.info("║     ✅ ALL TESTS PASSED - No retries needed                   ║");
			LOGGER.info("╚═══════════════════════════════════════════════════════════════╝");
			return;
		}

		// Log failed tests
		LOGGER.warn("╔═══════════════════════════════════════════════════════════════╗");
		LOGGER.warn("║     🔄 SUITE COMPLETED - {} FAILED TEST(S) DETECTED           ║", testsToRetry.size());
		LOGGER.warn("╠═══════════════════════════════════════════════════════════════╣");
		for (FailedTestInfo failedTest : testsToRetry) {
			int attempts = retryAttempts.getOrDefault(failedTest.className, 0);
			LOGGER.warn("║  ❌ {} (Retry {}/{})", 
					truncate(failedTest.testName, 35), attempts + 1, MAX_RETRY_ATTEMPTS);
		}
		LOGGER.warn("╠═══════════════════════════════════════════════════════════════╣");
		LOGGER.warn("║     Starting retry execution at END of suite...               ║");
		LOGGER.warn("╚═══════════════════════════════════════════════════════════════╝");

		// Execute retries with loop to handle multiple retry attempts
		executeRetriesWithLoop(suite);
		
		// EXCEL FIX: After all retries complete, log final status
		if (failedTests.isEmpty()) {
			LOGGER.info("╔═══════════════════════════════════════════════════════════════╗");
			LOGGER.info("║     ✅ ALL RETRIES PASSED - Final Excel report will include   ║");
			LOGGER.info("║        updated results from retry runs                        ║");
			LOGGER.info("╚═══════════════════════════════════════════════════════════════╝");
		} else {
			LOGGER.warn("╔═══════════════════════════════════════════════════════════════╗");
			LOGGER.warn("║     ❌ {} TEST(S) STILL FAILED after all retry attempts       ║", failedTests.size());
			LOGGER.warn("║        Final Excel report will show failed status             ║");
			LOGGER.warn("╚═══════════════════════════════════════════════════════════════╝");
		}
	}

	private void collectFailedTests(ISuite suite) {
		Map<String, ISuiteResult> results = suite.getResults();

		for (ISuiteResult suiteResult : results.values()) {
			ITestContext context = suiteResult.getTestContext();
			
			int failedCount = context.getFailedTests().size();
			int passedCount = context.getPassedTests().size();

			// Get failed tests
			for (ITestResult result : context.getFailedTests().getAllResults()) {
				String className = result.getTestClass().getName();
				String testName = context.getName();
				String errorMessage = result.getThrowable() != null 
						? result.getThrowable().getMessage() 
						: "Unknown error";

				// Only add if not already tracked or if we need to retry
				if (!failedTests.containsKey(className)) {
					failedTests.put(className, new FailedTestInfo(className, testName, errorMessage));
					LOGGER.debug("Tracked failed test: {} - {}", testName, truncate(errorMessage, 50));
				}
			}

			// Only remove from failed list if ALL tests passed (no failures) in this context
			// This is important for Cucumber where each scenario is a separate test in the same runner
			if (failedCount == 0 && passedCount > 0) {
				for (ITestResult result : context.getPassedTests().getAllResults()) {
					String className = result.getTestClass().getName();
					if (failedTests.containsKey(className)) {
						failedTests.remove(className);
						LOGGER.info("✅ Test {} PASSED on retry (all scenarios passed) - removed from failed list", 
								result.getTestClass().getRealClass().getSimpleName());
					}
				}
			} else if (failedCount > 0) {
				LOGGER.debug("Context {} has {} failed and {} passed tests - keeping in retry list", 
						context.getName(), failedCount, passedCount);
			}
		}
	}

	private List<FailedTestInfo> getTestsToRetry() {
		List<FailedTestInfo> testsToRetry = new ArrayList<>();

		for (FailedTestInfo failedTest : failedTests.values()) {
			int attempts = retryAttempts.getOrDefault(failedTest.className, 0);
			if (attempts < MAX_RETRY_ATTEMPTS) {
				testsToRetry.add(failedTest);
			} else {
				LOGGER.error("❌ {} - FAILED after {} retry attempts (no more retries)", 
						failedTest.testName, MAX_RETRY_ATTEMPTS);
			}
		}

		return testsToRetry;
	}

	private void executeRetriesWithLoop(ISuite originalSuite) {
		// EXCEL FIX: Mark retry run start to prevent counter reset and defer Excel generation
		com.kfonetalentsuite.listeners.ExcelReportListener.markRetryRunStart();
		
		try {
			int retryRound = 0;
			
			// Loop until no more tests to retry or max attempts exhausted for all
			while (true) {
				retryRound++;
				List<FailedTestInfo> testsToRetry = getTestsToRetry();
				
				if (testsToRetry.isEmpty()) {
					LOGGER.info("╔═══════════════════════════════════════════════════════════════╗");
					LOGGER.info("║     ✅ All tests passed after {} retry round(s)               ║", retryRound - 1);
					LOGGER.info("╚═══════════════════════════════════════════════════════════════╝");
					break;
				}
				
				LOGGER.info("╔═══════════════════════════════════════════════════════════════╗");
				LOGGER.info("║     🔄 RETRY ROUND {} - {} test(s) to retry                   ║", retryRound, testsToRetry.size());
				LOGGER.info("╚═══════════════════════════════════════════════════════════════╝");
				
				// Execute single retry round
				executeRetries(originalSuite, testsToRetry);
				
				// Check if any tests still need retry
				List<FailedTestInfo> remainingTests = getTestsToRetry();
				if (remainingTests.isEmpty()) {
					LOGGER.info("All tests passed after retry round {}", retryRound);
					break;
				}
				
				// Safety check: if no progress is being made, exit
				if (remainingTests.size() == testsToRetry.size()) {
					boolean anyCanRetry = false;
					for (FailedTestInfo test : remainingTests) {
						if (retryAttempts.getOrDefault(test.className, 0) < MAX_RETRY_ATTEMPTS) {
							anyCanRetry = true;
							break;
						}
					}
					if (!anyCanRetry) {
						LOGGER.warn("Max retry attempts exhausted for all remaining {} tests", remainingTests.size());
						break;
					}
				}
			}
		} finally {
			// Reset retry flag
			isRetryRun = false;
			// EXCEL FIX: Mark retry complete to allow final Excel generation
			com.kfonetalentsuite.listeners.ExcelReportListener.markRetryRunComplete();
			LOGGER.info("Retry loop completed - Excel report will now be generated with all results");
		}
	}

	private void executeRetries(ISuite originalSuite, List<FailedTestInfo> testsToRetry) {
		if (testsToRetry.isEmpty()) {
			return;
		}

		// Set retry flag
		isRetryRun = true;

		// Create a new TestNG instance for retry
		TestNG retryTestNG = new TestNG();
		retryTestNG.setUseDefaultListeners(false);

		// Create XML suite for retry
		XmlSuite retrySuite = new XmlSuite();
		// EXCEL FIX: Keep original suite name for proper feature matching in Excel
		retrySuite.setName(originalSuite.getName());
		retrySuite.setParallel(originalSuite.getXmlSuite().getParallel());
		retrySuite.setThreadCount(originalSuite.getXmlSuite().getThreadCount());

		// Add listeners from original suite (except this one to avoid recursion)
		for (String listener : originalSuite.getXmlSuite().getListeners()) {
			if (!listener.contains("SuiteRetryListener")) {
				retrySuite.addListener(listener);
			}
		}
		// Add this listener back for tracking retry results
		retrySuite.addListener(SuiteRetryListener.class.getName());

		// Create XmlTest for each failed test
		for (FailedTestInfo failedTest : testsToRetry) {
			// Increment retry attempt counter
			int currentAttempts = retryAttempts.getOrDefault(failedTest.className, 0);
			retryAttempts.put(failedTest.className, currentAttempts + 1);

			// Find original XmlTest configuration
			XmlTest originalTest = findOriginalXmlTest(originalSuite, failedTest.className);
			
			if (originalTest != null) {
				XmlTest retryTest = new XmlTest(retrySuite);
				// EXCEL FIX: Keep original test name for proper feature matching
				retryTest.setName(failedTest.testName);
				retryTest.setPreserveOrder(originalTest.getPreserveOrder());

				// Copy classes
				List<XmlClass> classes = new ArrayList<>();
				for (XmlClass xmlClass : originalTest.getClasses()) {
					if (xmlClass.getName().equals(failedTest.className)) {
						classes.add(new XmlClass(failedTest.className));
						break;
					}
				}
				retryTest.setXmlClasses(classes);

				LOGGER.info("🔄 Queued for retry: {} (Attempt {}/{})", 
						failedTest.testName, currentAttempts + 1, MAX_RETRY_ATTEMPTS);
			}
		}

		// Execute retries if we have any tests
		if (!retrySuite.getTests().isEmpty()) {
			List<XmlSuite> suites = new ArrayList<>();
			suites.add(retrySuite);
			retryTestNG.setXmlSuites(suites);

			LOGGER.info("╔═══════════════════════════════════════════════════════════════╗");
			LOGGER.info("║     🔄 EXECUTING {} RETRY TEST(S)...                          ║", testsToRetry.size());
			LOGGER.info("╚═══════════════════════════════════════════════════════════════╝");

			try {
				retryTestNG.run();
			} catch (Exception e) {
				LOGGER.error("Error during retry execution: {}", e.getMessage());
			}
		}
	}

	private XmlTest findOriginalXmlTest(ISuite suite, String className) {
		for (XmlTest xmlTest : suite.getXmlSuite().getTests()) {
			for (XmlClass xmlClass : xmlTest.getClasses()) {
				if (xmlClass.getName().equals(className)) {
					return xmlTest;
				}
			}
		}
		return null;
	}

	private String truncate(String str, int maxLength) {
		if (str == null) return "null";
		str = str.replace("\n", " ").replace("\r", "");
		return str.length() > maxLength ? str.substring(0, maxLength) + "..." : str;
	}

	public static int getMaxRetryAttempts() {
		return MAX_RETRY_ATTEMPTS;
	}

	public static boolean hasFailedTests() {
		return !failedTests.isEmpty();
	}

	public static int getFailedTestCount() {
		return failedTests.size();
	}

	private static class FailedTestInfo {
		final String className;
		final String testName;
		@SuppressWarnings("unused") // Stored for potential future logging/reporting use
		final String errorMessage;

		FailedTestInfo(String className, String testName, String errorMessage) {
			this.className = className;
			this.testName = testName;
			this.errorMessage = errorMessage;
		}
	}
}

