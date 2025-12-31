package com.kfonetalentsuite.testNGAnalyzer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

	private static final Logger LOGGER = LogManager.getLogger(RetryAnalyzer.class);

	// Instance variable for retry count
	// TestNG creates a new RetryAnalyzer instance for each @Test method
	private int retryCount = 0;

	// Maximum number of retry attempts (can be made configurable via config.properties)
	private static final int MAX_RETRY_COUNT = 2;

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

	private String getSimpleClassName(String fullClassName) {
		if (fullClassName == null) return "Unknown";
		int lastDot = fullClassName.lastIndexOf('.');
		return lastDot > 0 ? fullClassName.substring(lastDot + 1) : fullClassName;
	}

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

	public static int getMaxRetryCount() {
		return MAX_RETRY_COUNT;
	}
}
