package com.kfonetalentsuite.utils.JobMapping;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.ElementNotInteractableException;
import org.testng.Assert;

/**
 * Simplified error handler - middle ground between basic and complex.
 * Provides better error context without overwhelming complexity.
 * 
 * @version 2.0 (Extent Reports removed)
 */
public class SimpleErrorHandler {

	private static final Logger LOGGER = LogManager.getLogger(SimpleErrorHandler.class);

	/**
	 * Enhanced error handling with simple retry for common issues
	 * 
	 * @param methodName     Name of the method
	 * @param e              The throwable (exception or error)
	 * @param elementContext What element/operation failed
	 */
	public static void handleWithContext(String methodName, Throwable e, String elementContext) {
		// Simple retry for common transient issues
		if (e instanceof StaleElementReferenceException || e instanceof ElementNotInteractableException) {
			LOGGER.warn(" Retryable error in {} for {}: {}", methodName, elementContext, e.getMessage());

			try {
				Thread.sleep(1000); // Simple 1-second wait
			} catch (InterruptedException ie) {
				Thread.currentThread().interrupt();
			}
		}

		// Enhanced error message with context
		String errorMsg = String.format(" Method: %s | Element: %s | Error: %s", formatMethodName(methodName),
				elementContext, e.getMessage());

		// Take screenshot
		String screenshotPath = ScreenshotHandler.captureFailureScreenshot(methodName, e);
		if (screenshotPath != null) {
			errorMsg += " | Screenshot: " + screenshotPath;
		}

		// Log with appropriate level
		if (e instanceof TimeoutException || e instanceof NoSuchElementException) {
			LOGGER.error(" ELEMENT ISSUE - {}", errorMsg);
		} else if (e instanceof AssertionError) {
			LOGGER.error(" ASSERTION FAILED - {}", errorMsg);
		} else {
			LOGGER.error(" ERROR - {}", errorMsg);
		}

		// Fail test
		Assert.fail(errorMsg);
	}

	/**
	 * Simple method name formatting
	 */
	private static String formatMethodName(String methodName) {
		return methodName.replaceAll("_", " ").toLowerCase();
	}

	/**
	 * Quick method for most common use case
	 */
	public static void handle(String methodName, Throwable e) {
		handleWithContext(methodName, e, "Unknown element");
	}
}
