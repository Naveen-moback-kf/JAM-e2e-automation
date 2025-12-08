package com.kfonetalentsuite.testNGAnalyzer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * RetryAnalyzer - Automatically retries failed tests
 * 
 * This class implements TestNG's IRetryAnalyzer to automatically retry
 * failed tests up to a configurable number of times.
 * 
 * Thread-safe implementation for parallel test execution.
 * 
 * @author Automation Team
 */
public class RetryAnalyzer implements IRetryAnalyzer {

	private static final Logger LOGGER = LogManager.getLogger(RetryAnalyzer.class);

	// Thread-safe counter for parallel execution
	private static ThreadLocal<Integer> retryCount = ThreadLocal.withInitial(() -> 0);

	// Maximum number of retry attempts (can be made configurable via config.properties)
	private static final int MAX_RETRY_COUNT = 2;

	/**
	 * Determines whether a failed test should be retried.
	 * 
	 * @param result The test result of the failed test
	 * @return true if the test should be retried, false otherwise
	 */
	@Override
	public boolean retry(ITestResult result) {
		int currentRetryCount = retryCount.get();

		if (currentRetryCount < MAX_RETRY_COUNT) {
			currentRetryCount++;
			retryCount.set(currentRetryCount);

			String testName = result.getMethod().getMethodName();
			String className = result.getTestClass().getName();
			Throwable throwable = result.getThrowable();
			String errorMessage = (throwable != null) ? throwable.getMessage() : "Unknown error";

			LOGGER.warn("╔═══════════════════════════════════════════════════════════════╗");
			LOGGER.warn("║                    TEST RETRY TRIGGERED                       ║");
			LOGGER.warn("╠═══════════════════════════════════════════════════════════════╣");
			LOGGER.warn("║ Test Class  : {}", className);
			LOGGER.warn("║ Test Method : {}", testName);
			LOGGER.warn("║ Retry       : {} of {}", currentRetryCount, MAX_RETRY_COUNT);
			LOGGER.warn("║ Error       : {}", truncateMessage(errorMessage, 50));
			LOGGER.warn("╚═══════════════════════════════════════════════════════════════╝");

			return true;
		}

		// Reset counter for next test
		retryCount.remove();

		LOGGER.error("Test '{}' FAILED after {} retry attempts", 
				result.getMethod().getMethodName(), MAX_RETRY_COUNT);

		return false;
	}

	/**
	 * Truncates a message if it exceeds the specified length.
	 */
	private String truncateMessage(String message, int maxLength) {
		if (message == null) {
			return "null";
		}
		// Remove newlines for cleaner log output
		message = message.replace("\n", " ").replace("\r", "");
		if (message.length() > maxLength) {
			return message.substring(0, maxLength) + "...";
		}
		return message;
	}

	/**
	 * Get the maximum retry count configured.
	 * 
	 * @return Maximum number of retries
	 */
	public static int getMaxRetryCount() {
		return MAX_RETRY_COUNT;
	}
}
