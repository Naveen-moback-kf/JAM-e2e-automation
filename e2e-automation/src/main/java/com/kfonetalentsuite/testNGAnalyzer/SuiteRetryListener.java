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

/**
 * SuiteRetryListener - Retries FAILED runners/features at the END of suite execution
 * 
 * This listener implements the "Retry at End of Suite" pattern:
 * 1. All runners execute normally (no immediate retry)
 * 2. Failed runners are tracked during execution
 * 3. After ALL runners complete, failed ones are retried
 * 4. Supports configurable max retry attempts
 * 
 * Benefits over immediate retry:
 * - Transient issues (network, timing) often resolve by end of suite
 * - Fresh browser session for each retry
 * - Clean reporting: see all failures first, then retries
 * - Better for CI/CD pipelines
 * 
 * @author Automation Team
 */
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

	/**
	 * Called before suite starts - initialize tracking
	 */
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

	/**
	 * Called after suite completes - trigger retries for failed tests
	 */
	@Override
	public void onFinish(ISuite suite) {
		// Collect failed tests from this suite run
		collectFailedTests(suite);

		// If we're already in a retry run, don't trigger another retry
		if (isRetryRun) {
			LOGGER.debug("Retry run completed - not triggering another retry");
			isRetryRun = false;
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

		// Execute retries
		executeRetries(suite, testsToRetry);
	}

	/**
	 * Collect failed tests from the suite results
	 */
	private void collectFailedTests(ISuite suite) {
		Map<String, ISuiteResult> results = suite.getResults();

		for (ISuiteResult suiteResult : results.values()) {
			ITestContext context = suiteResult.getTestContext();

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

			// Remove from failed if it passed (in case of retry success)
			for (ITestResult result : context.getPassedTests().getAllResults()) {
				String className = result.getTestClass().getName();
				if (failedTests.containsKey(className)) {
					failedTests.remove(className);
					LOGGER.info("✅ Test {} PASSED on retry - removed from failed list", 
							result.getTestClass().getRealClass().getSimpleName());
				}
			}
		}
	}

	/**
	 * Get list of tests that should be retried (haven't exceeded max attempts)
	 */
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

	/**
	 * Execute retry runs for failed tests
	 */
	private void executeRetries(ISuite originalSuite, List<FailedTestInfo> testsToRetry) {
		if (testsToRetry.isEmpty()) {
			return;
		}

		// Set retry flag to prevent infinite loops
		isRetryRun = true;

		// Create a new TestNG instance for retry
		TestNG retryTestNG = new TestNG();
		retryTestNG.setUseDefaultListeners(false);

		// Create XML suite for retry
		XmlSuite retrySuite = new XmlSuite();
		retrySuite.setName(originalSuite.getName() + " - RETRY");
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
				retryTest.setName(failedTest.testName + " - RETRY #" + (currentAttempts + 1));
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

	/**
	 * Find the original XmlTest configuration for a test class
	 */
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

	/**
	 * Truncate string for logging
	 */
	private String truncate(String str, int maxLength) {
		if (str == null) return "null";
		str = str.replace("\n", " ").replace("\r", "");
		return str.length() > maxLength ? str.substring(0, maxLength) + "..." : str;
	}

	/**
	 * Get max retry attempts (for external reference)
	 */
	public static int getMaxRetryAttempts() {
		return MAX_RETRY_ATTEMPTS;
	}

	/**
	 * Check if there are any permanently failed tests
	 */
	public static boolean hasFailedTests() {
		return !failedTests.isEmpty();
	}

	/**
	 * Get count of failed tests
	 */
	public static int getFailedTestCount() {
		return failedTests.size();
	}

	/**
	 * Data class to hold failed test information
	 */
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

