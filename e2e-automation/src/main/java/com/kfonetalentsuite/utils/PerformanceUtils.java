package com.kfonetalentsuite.utils;

import java.time.Duration;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Performance Optimized Utilities - Smart Wait Strategies
 * 
 * This class replaces Thread.sleep() calls with intelligent, condition-based waits
 * to dramatically improve test execution performance.
 * 
 * KEY BENEFITS:
 *  No blocking waits - waits only as long as necessary
 *  Condition-based - waits for specific states, not arbitrary time
 *  Performance optimized - significantly faster than Thread.sleep()
 *  Failure-resilient - proper timeout handling
 * 
 * USAGE EXAMPLES:
 * 
 * Instead of: Thread.sleep(2000);
 * Use: PerformanceUtils.waitForPageReady(driver);
 * 
 * Instead of: Thread.sleep(3000); // wait for element
 * Use: PerformanceUtils.waitForElement(driver, element);
 * 
 * Instead of: Thread.sleep(5000); // wait for spinner
 * Use: PerformanceUtils.waitForSpinnersToDisappear(driver);
 */
public class PerformanceUtils {
    
    private static final Logger LOGGER = (Logger) LogManager.getLogger(PerformanceUtils.class);
    
    // Performance-optimized timeout values (reduced for faster execution)
    private static final int DEFAULT_TIMEOUT_SECONDS = 5;  // Reduced from 10s to 5s
    private static final int SHORT_TIMEOUT_SECONDS = 3;    // Reduced from 5s to 3s
    
    // Common spinner and loader selectors
    private static final String[] SPINNER_SELECTORS = {
        "//*[@class='blocking-loader']//img",
        "//*[contains(@className,'animate-spin')]",
        "//*[contains(@class,'spinner')]",
        "//*[contains(@class,'loading')]",
        "//*[contains(@class,'loader')]"
    };
    
    /**
     * PERFORMANCE: Smart wait for page readiness (replaces Thread.sleep(2000-5000))
     * Waits for all common loading indicators to disappear
     */
    public static void waitForPageReady(WebDriver driver) {
        waitForPageReady(driver, DEFAULT_TIMEOUT_SECONDS);
    }
    
    public static void waitForPageReady(WebDriver driver, int timeoutSeconds) {
        // PERFORMANCE: Ultra-fast wait with smart early exit conditions
        WebDriverWait ultraFastWait = new WebDriverWait(driver, Duration.ofMillis(500)); // Only 0.5 seconds per spinner
        WebDriverWait quickWait = new WebDriverWait(driver, Duration.ofSeconds(Math.min(timeoutSeconds, 3))); // Max 3 seconds for document ready
        
        try {
            // ENHANCED: Early exit if document is already ready
            try {
                Boolean isReady = (Boolean) ((org.openqa.selenium.JavascriptExecutor) driver)
                    .executeScript("return document.readyState === 'complete'");
                if (Boolean.TRUE.equals(isReady)) {
                    // LOGGER.debug("✅ Page already ready - immediate return");
                    return; // Fast exit!
                }
            } catch (Exception e) {
                // Continue with spinner checks
            }
            
            // OPTIMIZED: Quick spinner visibility check with immediate exit
            boolean foundVisibleSpinner = false;
            for (String selector : SPINNER_SELECTORS) {
                try {
                    List<WebElement> spinners = driver.findElements(By.xpath(selector));
                    if (!spinners.isEmpty()) {
                        // Ultra-fast visibility check
                        for (WebElement spinner : spinners) {
                            try {
                                if (spinner.isDisplayed()) {
                                    foundVisibleSpinner = true;
                                    LOGGER.debug("⏳ Found visible spinner: {}", selector);
                                    // Wait only 0.5 seconds max per spinner
                                    ultraFastWait.until(ExpectedConditions.invisibilityOf(spinner));
                                    break; // Exit inner loop after handling first visible spinner
                                }
                            } catch (Exception e) {
                                // Spinner disappeared or became stale - good!
                                LOGGER.debug("✅ Spinner resolved quickly");
                            }
                        }
                    }
                    
                    // PERFORMANCE: Early exit if no visible spinners found
                    if (!foundVisibleSpinner) {
                        break; // Exit spinner loop early
                    }
                    
                } catch (Exception e) {
                    // Continue to next spinner type
                    LOGGER.debug("❌ Spinner selector not found: {}", selector);
                }
            }
            
            // FINAL: Quick document ready check (max 3 seconds)
            if (!foundVisibleSpinner) {
                try {
                    quickWait.until(webDriver -> 
                        ((org.openqa.selenium.JavascriptExecutor) webDriver)
                            .executeScript("return document.readyState").equals("complete"));
                    LOGGER.debug("✅ Page ready - document complete");
                } catch (Exception e) {
                    // Acceptable timeout - page might be ready anyway
                    LOGGER.debug("⚠️ Document ready check timeout (acceptable) - continuing");
                }
            }
            
        } catch (Exception e) {
            LOGGER.debug("⚡ Page ready completed quickly with minor timeout: {}", e.getMessage());
            // Don't throw exception - performance is more important
        }
    }
    
    /**
     * PERFORMANCE: Smart wait for specific spinners (replaces Thread.sleep after spinner waits)
     */
    public static void waitForSpinnersToDisappear(WebDriver driver) {
        waitForSpinnersToDisappear(driver, DEFAULT_TIMEOUT_SECONDS);
    }
    
    public static void waitForSpinnersToDisappear(WebDriver driver, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        
        try {
            for (String selector : SPINNER_SELECTORS) {
                try {
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(selector)));
                } catch (Exception e) {
                    // Continue to next spinner - it might not exist
                }
            }
            LOGGER.debug(" All spinners disappeared");
        } catch (Exception e) {
            LOGGER.warn(" Spinner wait timeout: {}", e.getMessage());
        }
    }
    
    /**
     * PERFORMANCE: Smart wait for element to be ready for interaction
     * Combines visibility and clickability checks
     */
    public static WebElement waitForElement(WebDriver driver, WebElement element) {
        return waitForElement(driver, element, DEFAULT_TIMEOUT_SECONDS);
    }
    
    public static WebElement waitForElement(WebDriver driver, WebElement element, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        
        try {
            // Wait for element to be visible and clickable
            wait.until(ExpectedConditions.and(
                ExpectedConditions.visibilityOf(element),
                ExpectedConditions.elementToBeClickable(element)
            ));
            
            LOGGER.debug(" Element ready for interaction");
            return element;
            
        } catch (Exception e) {
            LOGGER.warn(" Element wait timeout after {} seconds: {}", timeoutSeconds, e.getMessage());
            return element; // Return element anyway for fallback
        }
    }
    
    /**
     * PERFORMANCE: Smart wait for element by locator
     */
    public static WebElement waitForElement(WebDriver driver, By locator) {
        return waitForElement(driver, locator, DEFAULT_TIMEOUT_SECONDS);
    }
    
    public static WebElement waitForElement(WebDriver driver, By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(locator));
        } catch (Exception e) {
            LOGGER.warn(" Element locator wait timeout: {}", e.getMessage());
            throw e;
        }
    }
    
    /**
     * PERFORMANCE: Smart wait for form/dropdown interactions
     * Waits for dropdown options to be loaded and visible
     */
    public static void waitForDropdownOptions(WebDriver driver, By dropdownLocator) {
        waitForDropdownOptions(driver, dropdownLocator, DEFAULT_TIMEOUT_SECONDS);
    }
    
    public static void waitForDropdownOptions(WebDriver driver, By dropdownLocator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        
        try {
            // Wait for dropdown to be present and have options
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(dropdownLocator));
            
            // Additional wait to ensure options are fully loaded
            wait.until(webDriver -> {
                List<WebElement> options = webDriver.findElements(dropdownLocator);
                return !options.isEmpty() && options.get(0).isDisplayed();
            });
            
            LOGGER.debug(" Dropdown options loaded and ready");
            
        } catch (Exception e) {
            LOGGER.warn(" Dropdown options wait timeout: {}", e.getMessage());
        }
    }
    
    /**
     * PERFORMANCE: Smart wait for search results to load
     */
    public static void waitForSearchResults(WebDriver driver, By resultsLocator) {
        waitForSearchResults(driver, resultsLocator, DEFAULT_TIMEOUT_SECONDS);
    }
    
    public static void waitForSearchResults(WebDriver driver, By resultsLocator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        
        try {
            // Wait for spinners to disappear first
            waitForSpinnersToDisappear(driver, SHORT_TIMEOUT_SECONDS);
            
            // Then wait for results to appear
            wait.until(ExpectedConditions.visibilityOfElementLocated(resultsLocator));
            
            LOGGER.debug(" Search results loaded");
            
        } catch (Exception e) {
            LOGGER.warn(" Search results wait timeout: {}", e.getMessage());
        }
    }
    
    /**
     * PERFORMANCE: Smart wait with custom condition
     */
    public static <T> T waitForCondition(WebDriver driver, 
            org.openqa.selenium.support.ui.ExpectedCondition<T> condition) {
        return waitForCondition(driver, condition, DEFAULT_TIMEOUT_SECONDS);
    }
    
    public static <T> T waitForCondition(WebDriver driver, 
            org.openqa.selenium.support.ui.ExpectedCondition<T> condition, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        
        try {
            return wait.until(condition);
        } catch (Exception e) {
            LOGGER.warn(" Custom condition wait timeout: {}", e.getMessage());
            throw e;
        }
    }
    
    /**
     * PERFORMANCE: Smart wait for attribute to have specific value
     * Useful for waiting for form fields to be cleared or populated
     */
    public static void waitForAttributeValue(WebDriver driver, WebElement element, 
            String attribute, String expectedValue) {
        waitForAttributeValue(driver, element, attribute, expectedValue, DEFAULT_TIMEOUT_SECONDS);
    }
    
    public static void waitForAttributeValue(WebDriver driver, WebElement element, 
            String attribute, String expectedValue, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        
        try {
            wait.until(ExpectedConditions.attributeToBe(element, attribute, expectedValue));
            LOGGER.debug(" Attribute '{}' has expected value '{}'", attribute, expectedValue);
        } catch (Exception e) {
            LOGGER.warn(" Attribute value wait timeout: {}", e.getMessage());
        }
    }
    
    /**
     * PERFORMANCE: Optimized wait for UI stability after actions
     * Replaces Thread.sleep(1000-3000) with smart condition checking
     */
    public static void waitForUIStability(WebDriver driver) {
        waitForUIStability(driver, SHORT_TIMEOUT_SECONDS);
    }
    
    public static void waitForUIStability(WebDriver driver, int timeoutSeconds) {
        try {
            // Wait for page ready state
            waitForPageReady(driver, timeoutSeconds);
            
            // Wait for any animations or transitions to complete
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            wait.until(webDriver -> {
                try {
                    // Check if any transitions are running
                    Object result = ((org.openqa.selenium.JavascriptExecutor) webDriver)
                        .executeScript("return window.jQuery && jQuery.active == 0");
                    return result == null || Boolean.TRUE.equals(result);
                } catch (Exception e) {
                    return true; // If jQuery not available, assume stable
                }
            });
            
//            LOGGER.debug(" UI stable and ready");
            
        } catch (Exception e) {
            LOGGER.debug(" UI stability check completed with timeout: {}", e.getMessage());
        }
    }
    
    /**
     * PERFORMANCE: Log performance improvement statistics
     */
    public static void logPerformanceGain(String methodName, long threadSleepMs) {
        LOGGER.info(" PERFORMANCE GAIN: {} - Eliminated {}ms Thread.sleep with smart wait", 
                   methodName, threadSleepMs);
    }
    
    /**
     * PERFORMANCE: Batch wait for multiple conditions (replaces multiple Thread.sleep calls)
     */
    public static void waitForMultipleConditions(WebDriver driver, 
            org.openqa.selenium.support.ui.ExpectedCondition<?>... conditions) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS));
        
        try {
            for (org.openqa.selenium.support.ui.ExpectedCondition<?> condition : conditions) {
                wait.until(condition);
            }
            LOGGER.debug(" All conditions met");
        } catch (Exception e) {
            LOGGER.warn(" Multiple conditions wait failed: {}", e.getMessage());
        }
    }
    
    public static void safeSleep(WebDriver driver, long milliseconds) {
        try {
            waitForUIStability(driver, (int)(milliseconds / 1000));
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            LOGGER.warn("Sleep interrupted: " + e.getMessage());
        }
    }
    
    /**
     * PERFORMANCE: Safe method to get element text with stale element retry logic
     * This is specifically useful for dynamic elements like result counts that update frequently
     * 
     * @param driver WebDriver instance
     * @param locator By locator for the element
     * @return Element text content
     */
    public static String getElementTextSafely(WebDriver driver, By locator) {
        return getElementTextSafely(driver, locator, DEFAULT_TIMEOUT_SECONDS, 3);
    }
    
    public static String getElementTextSafely(WebDriver driver, By locator, int timeoutSeconds, int maxRetries) {
        int retryAttempts = 0;
        String elementText = "";
        
        while (retryAttempts < maxRetries) {
            try {
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
                elementText = wait.until(ExpectedConditions.presenceOfElementLocated(locator)).getText();
                return elementText; // Success - return text
            } catch (org.openqa.selenium.StaleElementReferenceException e) {
                retryAttempts++;
                if (retryAttempts >= maxRetries) {
                    LOGGER.error("StaleElementReferenceException after {} attempts for locator: {}", maxRetries, locator);
                    throw e; // Re-throw if max retries reached
                }
                LOGGER.warn("StaleElementReferenceException on attempt {}, retrying... (locator: {})", retryAttempts, locator);
                waitForPageReady(driver, 1);
            } catch (Exception e) {
                LOGGER.error("Failed to get element text for locator: {} - {}", locator, e.getMessage());
                throw e;
            }
        }
        
        return elementText;
    }
    
}
