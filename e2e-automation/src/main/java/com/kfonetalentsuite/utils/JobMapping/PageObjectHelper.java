package com.kfonetalentsuite.utils.JobMapping;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.ElementNotInteractableException;
import org.testng.Assert;

import java.util.function.Supplier;

/**
 * Helper utilities for Page Object classes.
 * Provides logging, error handling, and retry mechanisms.
 */
public class PageObjectHelper {

	/**
	 * Log an info message using the provided logger.
	 */
	public static void log(Logger logger, String message) {
		logger.info(message);
	}
	
	/**
	 * Handle errors with screenshot capture and exception throwing.
	 */
	public static void handleError(Logger logger, String methodName, String issueDescription, Exception e) {
		String errorMsg = issueDescription + " - Method: " + methodName;
		logger.error(errorMsg, e);
		ScreenshotHandler.captureFailureScreenshot(methodName, e);
		throw new RuntimeException(errorMsg, e);
	}

	/**
	 * Handle errors with context information and screenshot.
	 */
	public static void handleWithContext(String methodName, Throwable e, String elementContext) {
		if (e instanceof StaleElementReferenceException || e instanceof ElementNotInteractableException) {
			// These are common transient errors, handled below
		}
		String errorMsg = String.format("Method: %s | Element: %s | Error: %s", 
				formatMethodName(methodName), elementContext, e.getMessage());

		String screenshotPath = ScreenshotHandler.captureFailureScreenshot(methodName, e);
		if (screenshotPath != null) {
			errorMsg += " | Screenshot: " + screenshotPath;
		}
		Assert.fail(errorMsg);
	}

	/**
	 * Handle errors with context (unknown element).
	 */
	public static void handleWithContext(String methodName, Throwable e) {
		handleWithContext(methodName, e, "Unknown element");
	}

	private static String formatMethodName(String methodName) {
		return methodName.replaceAll("_", " ").toLowerCase();
	}

	/**
	 * Retry an operation on stale element exceptions.
	 */
	public static <T> T retryOnStaleElement(Logger logger, Supplier<T> supplier) {
		int maxRetries = 3;
		for (int attempt = 1; attempt <= maxRetries; attempt++) {
			try {
				return supplier.get();
			} catch (StaleElementReferenceException e) {
				if (attempt == maxRetries) {
					logger.error("Element remained stale after {} attempts", maxRetries);
					throw new RuntimeException("Failed after " + maxRetries + " retry attempts", e);
				}
				logger.warn("Stale element on attempt {}/{} - retrying...", attempt, maxRetries);
				try {
					Thread.sleep(500);
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
					throw new RuntimeException("Interrupted during retry", ie);
				}
			}
		}
		throw new RuntimeException("Unexpected error in retry logic");
	}

	/**
	 * Retry a void operation on stale element exceptions.
	 */
	public static void retryOnStaleElement(Logger logger, Runnable operation) {
		retryOnStaleElement(logger, () -> {
			operation.run();
			return null;
		});
	}
}

