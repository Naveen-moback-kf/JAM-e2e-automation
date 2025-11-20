package com.kfonetalentsuite.utils;

import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;
import com.kfonetalentsuite.utils.JobMapping.ScreenshotHandler;
import org.apache.logging.log4j.core.Logger;

import java.util.function.Supplier;

/**
 * Utility class for common Page Object operations including logging, error handling,
 * and stale element retry logic. All page objects should use these methods instead
 * of duplicating code.
 */
public class PageObjectHelper {

    /**
     * Logs a message to both Log4j Logger and Extent Report
     * 
     * @param logger The Log4j Logger instance from the calling class
     * @param message The message to log
     */
    public static void log(Logger logger, String message) {
        logger.info(message);
        ExtentCucumberAdapter.addTestStepLog(message);
    }

    /**
     * Handles errors by capturing screenshot, logging to both Log4j Logger and Extent Report, 
     * then throws RuntimeException
     * 
     * @param logger The Log4j Logger instance from the calling class
     * @param methodName The name of the method where the error occurred
     * @param issueDescription A description of what went wrong
     * @param e The exception that was caught
     * @throws RuntimeException Always throws to fail the test
     */
    public static void handleError(Logger logger, String methodName, String issueDescription, Exception e) {
        String errorMsg = issueDescription + " - Method: " + methodName;
        logger.error(errorMsg, e);
        ExtentCucumberAdapter.addTestStepLog(errorMsg + " - " + e.getMessage());
        
        // Capture screenshot before throwing exception
        ScreenshotHandler.captureFailureScreenshot(methodName, e);
        
        throw new RuntimeException(errorMsg, e);
    }

    /**
     * Retries an operation up to 3 times if StaleElementReferenceException occurs.
     * This is useful when the DOM is refreshing and elements become stale.
     * 
     * @param logger The Log4j Logger instance from the calling class
     * @param supplier The operation to retry (e.g., () -> element.click())
     * @param <T> The return type of the operation
     * @return The result of the operation
     * @throws RuntimeException if all retry attempts fail
     */
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

    /**
     * Overloaded version of retryOnStaleElement for void operations (Runnable)
     * 
     * @param logger The Log4j Logger instance from the calling class
     * @param operation The operation to retry (e.g., () -> element.click())
     */
    public static void retryOnStaleElement(Logger logger, Runnable operation) {
        retryOnStaleElement(logger, () -> {
            operation.run();
            return null;
        });
    }
}

