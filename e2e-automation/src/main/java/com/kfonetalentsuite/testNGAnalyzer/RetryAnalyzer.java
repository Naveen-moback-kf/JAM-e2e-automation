package com.kfonetalentsuite.testNGAnalyzer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * RetryAnalyzer - Automatically retries failed FEATURES (entire test)
 * 
 * This class implements TestNG's IRetryAnalyzer to automatically retry
 * failed tests up to a configurable number of times.
 * 
 * FEATURE-LEVEL RETRY BEHAVIOR:
 * - When ANY scenario in a feature fails, the ENTIRE FEATURE is retried
 * - All scenarios in the feature will be re-executed from the beginning
 * - Up to MAX_RETRY_COUNT retry attempts for the whole feature
 * 
 * This is achieved by running all scenarios in a single @Test method (runFeature)
 * in CustomizeTestNGCucumberRunner. When the test method fails, RetryAnalyzer
 * retries the entire method, which re-runs all scenarios.
 * 
 * @author Automation Team
 */
public class RetryAnalyzer implements IRetryAnalyzer {

	private static final Logger LOGGER = LogManager.getLogger(RetryAnalyzer.class);

	// Instance variable for retry count
	// TestNG creates a new RetryAnalyzer instance for each @Test method
	private int retryCount = 0;

	// Maximum number of retry attempts (can be made configurable via config.properties)
	private static final int MAX_RETRY_COUNT = 2;

	/**
	 * Determines whether a failed test (feature) should be retried.
	 * 
	 * @param result The test result of the failed test
	 * @return true if the test should be retried, false otherwise
	 */
	@Override
	public boolean retry(ITestResult result) {
		if (retryCount < MAX_RETRY_COUNT) {
			retryCount++;

			String runnerName = getSimpleClassName(result.getTestClass().getName());
			Throwable throwable = result.getThrowable();
			String errorMessage = (throwable != null) ? throwable.getMessage() : "Unknown error";
			String failedScenario = extractFailedScenarioFromError(errorMessage);

			LOGGER.warn("╔═══════════════════════════════════════════════════════════════╗");
			LOGGER.warn("║              🔄 FEATURE RETRY TRIGGERED                       ║");
			LOGGER.warn("╠═══════════════════════════════════════════════════════════════╣");
			LOGGER.warn("║ Feature/Runner : {}", runnerName);
			LOGGER.warn("║ Failed At      : {}", truncateMessage(failedScenario, 42));
			LOGGER.warn("║ Retry Attempt  : {} of {}", retryCount, MAX_RETRY_COUNT);
			LOGGER.warn("║ Error          : {}", truncateMessage(errorMessage, 42));
			LOGGER.warn("╠═══════════════════════════════════════════════════════════════╣");
			LOGGER.warn("║ ⚠️  ALL SCENARIOS in this feature will be RE-EXECUTED         ║");
			LOGGER.warn("╚═══════════════════════════════════════════════════════════════╝");

			return true;
		}

		String runnerName = getSimpleClassName(result.getTestClass().getName());
		LOGGER.error("╔═══════════════════════════════════════════════════════════════╗");
		LOGGER.error("║              ❌ FEATURE FAILED - NO MORE RETRIES              ║");
		LOGGER.error("╠═══════════════════════════════════════════════════════════════╣");
		LOGGER.error("║ Feature/Runner : {}", runnerName);
		LOGGER.error("║ After {} retry attempts - marking as FAILED", MAX_RETRY_COUNT);
		LOGGER.error("╚═══════════════════════════════════════════════════════════════╝");

		return false;
	}

	/**
	 * Try to extract the failed scenario name from the error message.
	 */
	private String extractFailedScenarioFromError(String errorMessage) {
		if (errorMessage == null) return "Unknown Scenario";
		
		// Try to find scenario name patterns in error message
		// Cucumber often includes "Scenario:" or step text in errors
		if (errorMessage.contains("Scenario:")) {
			int start = errorMessage.indexOf("Scenario:");
			int end = errorMessage.indexOf("\n", start);
			if (end == -1) end = Math.min(start + 60, errorMessage.length());
			return errorMessage.substring(start, end).trim();
		}
		
		return "See error details above";
	}

	/**
	 * Get simple class name from fully qualified name.
	 */
	private String getSimpleClassName(String fullClassName) {
		if (fullClassName == null) return "Unknown";
		int lastDot = fullClassName.lastIndexOf('.');
		return lastDot > 0 ? fullClassName.substring(lastDot + 1) : fullClassName;
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
