package com.kfonetalentsuite.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Helper class for headless-compatible click operations.
 * Ensures elements are scrolled into view before clicking.
 * 
 * @author KF One Talent Suite Automation Team
 */
public class HeadlessClickHelper {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(HeadlessClickHelper.class);
    private final WebDriver driver;
    private final JavascriptExecutor js;
    private final WebDriverWait wait;
    
    public HeadlessClickHelper(WebDriver driver) {
        this.driver = driver;
        this.js = (JavascriptExecutor) driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }
    
    /**
     * Clicks an element with headless-compatible scrolling and multiple fallback methods.
     * 
     * @param element The WebElement to click
     * @throws Exception if all click methods fail
     */
    public void clickElement(WebElement element) throws Exception {
        clickElement(element, "Element");
    }
    
    /**
     * Clicks an element with headless-compatible scrolling and multiple fallback methods.
     * 
     * @param element The WebElement to click
     * @param elementName Name for logging purposes
     * @throws Exception if all click methods fail
     */
    public void clickElement(WebElement element, String elementName) throws Exception {
        try {
            // Step 1: Scroll element into view (CRITICAL for headless mode)
            scrollIntoView(element);
            Thread.sleep(300); // Brief wait for scroll to complete
            
            // Step 2: Wait for element to be clickable
            wait.until(ExpectedConditions.elementToBeClickable(element));
            
            // Step 3: Try multiple click methods
            boolean clicked = false;
            Exception lastException = null;
            
            // Method 1: Standard Selenium click
            if (!clicked) {
                try {
                    element.click();
                    clicked = true;
                    LOGGER.debug("Clicked {} using standard Selenium click", elementName);
                } catch (Exception e) {
                    lastException = e;
                    LOGGER.debug("Standard click failed for {}: {}", elementName, e.getClass().getSimpleName());
                }
            }
            
            // Method 2: JavaScript click (more reliable in headless)
            if (!clicked) {
                try {
                    js.executeScript("arguments[0].click();", element);
                    clicked = true;
                    LOGGER.debug("Clicked {} using JavaScript click", elementName);
                } catch (Exception e) {
                    lastException = e;
                    LOGGER.debug("JavaScript click failed for {}: {}", elementName, e.getClass().getSimpleName());
                }
            }
            
            // Method 3: Force click with scroll retry
            if (!clicked) {
                try {
                    // Scroll to center of viewport
                    js.executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'center'});", element);
                    Thread.sleep(200);
                    js.executeScript("arguments[0].click();", element);
                    clicked = true;
                    LOGGER.debug("Clicked {} using forced JavaScript click with center scroll", elementName);
                } catch (Exception e) {
                    lastException = e;
                    LOGGER.error("All click methods failed for {}", elementName);
                }
            }
            
            if (!clicked) {
                throw new Exception("Failed to click " + elementName + " after trying all methods. Last error: " + 
                                  (lastException != null ? lastException.getMessage() : "Unknown"));
            }
            
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            throw new Exception("Click operation interrupted for " + elementName, ie);
        }
    }
    
    /**
     * Scrolls an element into view using headless-compatible method.
     * 
     * @param element The WebElement to scroll into view
     */
    public void scrollIntoView(WebElement element) {
        try {
            // Use JavaScript scrollIntoView with 'block: center' for best visibility
            js.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center', inline: 'center'});", element);
            LOGGER.debug("Scrolled element into view (headless-compatible)");
        } catch (Exception e) {
            LOGGER.warn("Failed to scroll element into view: {}", e.getMessage());
            // Fallback: try simple scrollIntoView
            try {
                js.executeScript("arguments[0].scrollIntoView(true);", element);
            } catch (Exception fallbackEx) {
                LOGGER.error("Fallback scroll also failed: {}", fallbackEx.getMessage());
            }
        }
    }
    
    /**
     * Waits for element to be clickable, scrolls into view, then clicks.
     * Combines wait + scroll + click in one method.
     * 
     * @param element The WebElement to click
     * @param timeoutSeconds Maximum time to wait for element to be clickable
     * @throws Exception if element is not clickable or click fails
     */
    public void waitScrollAndClick(WebElement element, int timeoutSeconds) throws Exception {
        waitScrollAndClick(element, "Element", timeoutSeconds);
    }
    
    /**
     * Waits for element to be clickable, scrolls into view, then clicks.
     * Combines wait + scroll + click in one method.
     * 
     * @param element The WebElement to click
     * @param elementName Name for logging purposes
     * @param timeoutSeconds Maximum time to wait for element to be clickable
     * @throws Exception if element is not clickable or click fails
     */
    public void waitScrollAndClick(WebElement element, String elementName, int timeoutSeconds) throws Exception {
        try {
            // Wait for element to be present and visible
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            customWait.until(ExpectedConditions.visibilityOf(element));
            
            // Scroll into view
            scrollIntoView(element);
            Thread.sleep(300);
            
            // Wait for clickable
            customWait.until(ExpectedConditions.elementToBeClickable(element));
            
            // Click
            clickElement(element, elementName);
            
        } catch (Exception e) {
            LOGGER.error("waitScrollAndClick failed for {}: {}", elementName, e.getMessage());
            throw new Exception("Failed to wait, scroll, and click " + elementName, e);
        }
    }
    
    /**
     * Checks if an element is in the viewport (visible area).
     * Useful for debugging scroll issues.
     * 
     * @param element The WebElement to check
     * @return true if element is in viewport, false otherwise
     */
    public boolean isElementInViewport(WebElement element) {
        try {
            Boolean inViewport = (Boolean) js.executeScript(
                "var elem = arguments[0];" +
                "var rect = elem.getBoundingClientRect();" +
                "return (" +
                "  rect.top >= 0 &&" +
                "  rect.left >= 0 &&" +
                "  rect.bottom <= (window.innerHeight || document.documentElement.clientHeight) &&" +
                "  rect.right <= (window.innerWidth || document.documentElement.clientWidth)" +
                ");",
                element
            );
            return inViewport != null && inViewport;
        } catch (Exception e) {
            LOGGER.warn("Failed to check if element is in viewport: {}", e.getMessage());
            return false;
        }
    }
}

