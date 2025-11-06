package com.JobMapping.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

/**
 * Smart wait utilities to replace Thread.sleep calls with intelligent waits
 * Improves test performance and reliability by waiting for actual conditions
 * 
 * @author QA Automation Team
 * @version 2.1.0
 */
public class SmartWaits {
    private static final int DEFAULT_TIMEOUT = 30;
    private static final int SHORT_TIMEOUT = 10;
    
    /**
     * Wait for element to be clickable (replaces most Thread.sleep scenarios)
     * @param driver - WebDriver instance
     * @param element - WebElement to wait for
     */
    public static void waitForElementClickable(WebDriver driver, WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }
    
    /**
     * Wait for element to be clickable with custom timeout
     * @param driver - WebDriver instance
     * @param element - WebElement to wait for
     * @param timeoutSeconds - Timeout in seconds
     */
    public static void waitForElementClickable(WebDriver driver, WebElement element, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }
    
    /**
     * Wait for element to be visible
     * @param driver - WebDriver instance
     * @param element - WebElement to wait for
     */
    public static void waitForElementVisible(WebDriver driver, WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        wait.until(ExpectedConditions.visibilityOf(element));
    }
    
    /**
     * Wait for element to be invisible (useful for loading spinners)
     * @param driver - WebDriver instance
     * @param element - WebElement to wait for invisibility
     */
    public static void waitForElementInvisible(WebDriver driver, WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        wait.until(ExpectedConditions.invisibilityOf(element));
    }
    
    /**
     * Wait for page to be fully loaded (replaces Thread.sleep after navigation)
     * @param driver - WebDriver instance
     */
    public static void waitForPageLoad(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        wait.until(webDriver -> 
            ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
    }
    
    /**
     * Wait for jQuery/AJAX to complete (useful after form submissions)
     * @param driver - WebDriver instance
     */
    public static void waitForAjaxComplete(WebDriver driver) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
            wait.until(webDriver -> 
                ((JavascriptExecutor) webDriver)
                    .executeScript("return typeof jQuery != 'undefined' && jQuery.active == 0"));
        } catch (Exception e) {
            // If jQuery is not available, fall back to a short wait
            EnhancedLogger.debug("jQuery not available, using fallback wait");
            shortWait(driver);
        }
    }
    
    /**
     * Wait for specific text to appear in element
     * @param driver - WebDriver instance
     * @param element - WebElement to check for text
     * @param text - Expected text
     */
    public static void waitForTextInElement(WebDriver driver, WebElement element, String text) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        wait.until(ExpectedConditions.textToBePresentInElement(element, text));
    }
    
    /**
     * Wait for element to be present in DOM (by locator)
     * @param driver - WebDriver instance
     * @param locator - By locator to find element
     */
    public static void waitForElementPresent(WebDriver driver, By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }
    
    /**
     * Short intelligent wait (replaces Thread.sleep(1000-3000))
     * Waits for page readiness first, then minimal additional time
     * @param driver - WebDriver instance
     */
    public static void shortWait(WebDriver driver) {
        try {
            // First check if page is ready
            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_TIMEOUT));
            wait.until(webDriver -> js.executeScript("return document.readyState").equals("complete"));
            
            // Then minimal additional wait for any pending operations
            Thread.sleep(500); // Only 500ms instead of multiple seconds
        } catch (Exception e) {
            EnhancedLogger.debug("Short wait interrupted: " + e.getMessage());
        }
    }
    
    /**
     * Medium wait for complex operations (replaces Thread.sleep(5000-10000))
     * @param driver - WebDriver instance
     */
    public static void mediumWait(WebDriver driver) {
        waitForPageLoad(driver);
        waitForAjaxComplete(driver);
    }
    
    /**
     * Wait for loading spinner to disappear (common pattern in the framework)
     * @param driver - WebDriver instance
     * @param spinnerElement - The loading spinner element
     */
    public static void waitForSpinnerToDisappear(WebDriver driver, WebElement spinnerElement) {
        try {
            // First check if spinner is present
            if (spinnerElement.isDisplayed()) {
                EnhancedLogger.debug("Waiting for loading spinner to disappear...");
                waitForElementInvisible(driver, spinnerElement);
                EnhancedLogger.debug("Loading spinner disappeared");
            }
        } catch (Exception e) {
            // Spinner might not be present or already disappeared
            EnhancedLogger.debug("Spinner not found or already disappeared: " + e.getMessage());
        }
    }
    
    /**
     * Retry operation with intelligent waits (replaces try-catch with Thread.sleep)
     * @param driver - WebDriver instance
     * @param operation - Runnable operation to retry
     * @param maxRetries - Maximum number of retries
     * @param condition - Expected condition to wait for between retries
     */
    public static void retryOperation(WebDriver driver, Runnable operation, int maxRetries, 
                                    java.util.function.Supplier<Boolean> condition) {
        for (int i = 0; i < maxRetries; i++) {
            try {
                operation.run();
                return; // Success
            } catch (Exception e) {
                if (i == maxRetries - 1) {
                    throw new RuntimeException("Operation failed after " + maxRetries + " retries", e);
                }
                
                // Wait for condition or short wait
                try {
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_TIMEOUT));
                    wait.until(webDriver -> condition.get());
                } catch (Exception waitException) {
                    shortWait(driver);
                }
            }
        }
    }
}
