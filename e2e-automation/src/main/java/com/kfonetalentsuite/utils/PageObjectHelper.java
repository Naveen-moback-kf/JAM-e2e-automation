package com.kfonetalentsuite.utils;

import com.kfonetalentsuite.utils.JobMapping.ScreenshotHandler;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.ElementNotInteractableException;
import org.testng.Assert;

import java.util.function.Supplier;


public class PageObjectHelper {

	public static void log(Logger logger, String message) {
		logger.info(message);
	}
	
	public static void handleError(Logger logger, String methodName, String issueDescription, Exception e) {
		String errorMsg = issueDescription + " - Method: " + methodName;
		logger.error(errorMsg, e);
		ScreenshotHandler.captureFailureScreenshot(methodName, e);
		throw new RuntimeException(errorMsg, e);
	}

	public static void handleWithContext(String methodName, Throwable e, String elementContext) {
		if (e instanceof StaleElementReferenceException || e instanceof ElementNotInteractableException) {
			
		}
		String errorMsg = String.format(" Method: %s | Element: %s | Error: %s", 
				formatMethodName(methodName), elementContext, e.getMessage());

		String screenshotPath = ScreenshotHandler.captureFailureScreenshot(methodName, e);
		if (screenshotPath != null) {
			errorMsg += " | Screenshot: " + screenshotPath;
		}
		Assert.fail(errorMsg);
	}


	public static void handleWithContext(String methodName, Throwable e) {
		handleWithContext(methodName, e, "Unknown element");
	}

	private static String formatMethodName(String methodName) {
		return methodName.replaceAll("_", " ").toLowerCase();
	}

	public static <T> T retryOnStaleElement(Logger logger, Supplier<T> supplier) {
		int maxRetries = 3;
		for (int attempt = 1; attempt <= maxRetries; attempt++) {
			try {
				return supplier.get();
			} catch (org.openqa.selenium.StaleElementReferenceException e) {
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

	public static void retryOnStaleElement(Logger logger, Runnable operation) {
		retryOnStaleElement(logger, () -> {
			operation.run();
			return null;
		});
	}
}
