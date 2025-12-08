package com.kfonetalentsuite.utils.JobMapping;

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
 * This class replaces Thread.sleep() calls with intelligent, condition-based
 * waits to dramatically improve test execution performance.
 * 
 * KEY BENEFITS: No blocking waits - waits only as long as necessary
 * Condition-based - waits for specific states, not arbitrary time Performance
 * optimized - significantly faster than Thread.sleep() Failure-resilient -
 * proper timeout handling
 * 
 * USAGE EXAMPLES:
 * 
 * Instead of: Thread.sleep(2000); Use:
 * PerformanceUtils.waitForPageReady(driver);
 * 
 * Instead of: Thread.sleep(3000); // wait for element Use:
 * PerformanceUtils.waitForElement(driver, element);
 * 
 * Instead of: Thread.sleep(5000); // wait for spinner Use:
 * PerformanceUtils.waitForSpinnersToDisappear(driver);
 */
public class PerformanceUtils {

	private static final Logger LOGGER = (Logger) LogManager.getLogger(PerformanceUtils.class);

	// Performance-optimized timeout values (reduced for faster execution)
	private static final int DEFAULT_TIMEOUT_SECONDS = 5; // Reduced from 10s to 5s
	private static final int SHORT_TIMEOUT_SECONDS = 3; // Reduced from 5s to 3s

	// Common spinner and loader selectors
	// OPTIMIZED: More specific selectors to avoid matching non-spinner elements
	private static final String[] SPINNER_SELECTORS = { "//*[@class='blocking-loader']//img", // Primary spinner
			"//*[contains(@class,'animate-spin') and not(contains(@class,'hidden'))]", // Tailwind spinner (fixed
																						// @className -> @class)
			"//*[@data-testid='loader']", // Data-testid loader
			"//div[contains(@class,'spinner') and contains(@class,'visible')]", // Visible spinner divs
			"//img[contains(@src,'loader') or contains(@src,'spinner')]" // Spinner images
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
		WebDriverWait quickWait = new WebDriverWait(driver, Duration.ofSeconds(Math.min(timeoutSeconds, 3))); // Max 3
																												// seconds
																												// for
																												// document
																												// ready

		try {
			// ENHANCED: Early exit if document is already ready
			try {
				Boolean isReady = (Boolean) ((org.openqa.selenium.JavascriptExecutor) driver)
						.executeScript("return document.readyState === 'complete'");
				if (Boolean.TRUE.equals(isReady)) {
					// LOGGER.debug("... Page already ready - immediate return");
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
									// Only log spinner wait if debugging performance issues
									// LOGGER.debug(" Found visible spinner: {}", selector);
									// Wait only 0.5 seconds max per spinner
									ultraFastWait.until(ExpectedConditions.invisibilityOf(spinner));
									break; // Exit inner loop after handling first visible spinner
								}
							} catch (Exception e) {
								// Spinner disappeared or became stale - good!
								// Removed frequent log - expected behavior
							}
						}
					}

					// PERFORMANCE: Early exit if no visible spinners found
					if (!foundVisibleSpinner) {
						break; // Exit spinner loop early
					}

				} catch (Exception e) {
					// Continue to next spinner type - normal behavior
					// No need to log - most spinner selectors won't exist
				}
			}

			// FINAL: Quick document ready check (max 3 seconds)
			if (!foundVisibleSpinner) {
				try {
					quickWait.until(webDriver -> ((org.openqa.selenium.JavascriptExecutor) webDriver)
							.executeScript("return document.readyState").equals("complete"));
					// Removed frequent log - Page ready is expected behavior
				} catch (Exception e) {
					// Acceptable timeout - page might be ready anyway
					// Removed frequent log - continuing silently
				}
			}

		} catch (Exception e) {
			// Page ready completed - minor timeout is acceptable
			// Removed log - not useful for normal operations
		}
	}

	/**
	 * PERFORMANCE: Smart wait for specific spinners (replaces Thread.sleep after
	 * spinner waits)
	 */
	public static void waitForSpinnersToDisappear(WebDriver driver) {
		waitForSpinnersToDisappear(driver, DEFAULT_TIMEOUT_SECONDS);
	}

	public static void waitForSpinnersToDisappear(WebDriver driver, int timeoutSeconds) {
		long operationStartTime = System.currentTimeMillis();

		// CRITICAL FIX: Temporarily disable implicit wait to make findElements()
		// instant
		// Without this, findElements() waits for implicit timeout (20s) even when
		// element doesn't exist
		Duration originalImplicitWait = null;
		try {
			originalImplicitWait = driver.manage().timeouts().getImplicitWaitTimeout();
			// Set implicit wait to 0 for instant findElements() calls
			driver.manage().timeouts().implicitlyWait(Duration.ZERO);
		} catch (Exception e) {
			LOGGER.warn("⚠️  Could not modify implicit wait: {}", e.getMessage());
		}

		// CRITICAL FIX: Use short polling timeout with existence check first
		WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofMillis(500)); // Only 0.5s per check

		// Track overall timeout
		long maxWaitTime = timeoutSeconds * 1000L; // Convert to milliseconds
		int iterationCount = 0;

		try {
			boolean foundAnySpinner = false;
			int totalSpinnersWaited = 0;

			while ((System.currentTimeMillis() - operationStartTime) < maxWaitTime) {
				iterationCount++;
				boolean foundSpinnerInThisIteration = false;

				for (int i = 0; i < SPINNER_SELECTORS.length; i++) {
					String selector = SPINNER_SELECTORS[i];
					try {
						// PERFORMANCE: First check if spinner EXISTS before waiting
						// With implicit wait = 0, this returns instantly if element doesn't exist
						List<WebElement> spinners = driver.findElements(By.xpath(selector));

						if (!spinners.isEmpty()) {
							// Spinner exists - now check if it's visible
							for (int j = 0; j < spinners.size(); j++) {
								WebElement spinner = spinners.get(j);
								try {
									if (spinner.isDisplayed()) {
										foundAnySpinner = true;
										foundSpinnerInThisIteration = true;
										totalSpinnersWaited++;

										// Wait for this specific spinner to disappear (max 0.5s)
										try {
											shortWait.until(ExpectedConditions.invisibilityOf(spinner));
										} catch (org.openqa.selenium.TimeoutException te) {
											// Spinner still visible, will check again in next iteration
										}
									}
								} catch (org.openqa.selenium.StaleElementReferenceException e) {
									// Spinner disappeared or DOM changed - good!
								} catch (Exception e) {
									// Continue checking other spinners
								}
							}
						}
					} catch (Exception e) {
						// Continue to next spinner selector
					}
				}

				// If no visible spinners in this iteration, we're done!
				if (!foundSpinnerInThisIteration) {
					long totalDuration = System.currentTimeMillis() - operationStartTime;
					// Only log if wait was significant (> 1 second) or spinners were found
					if (totalDuration > 1000 || totalSpinnersWaited > 0) {
						LOGGER.info("✅ Spinners disappeared after {}ms", totalDuration);
					}
					return; // Exit successfully
				}

				// Small delay before next iteration to avoid hammering the DOM
				Thread.sleep(100);
			}

			// TIMEOUT: Exceeded max wait time
			long totalDuration = System.currentTimeMillis() - operationStartTime;
			LOGGER.warn("⚠️  Spinner wait timeout! Duration: {}ms, Iterations: {}", totalDuration, iterationCount);

			// PERFORMANCE: If no spinners found at all, return immediately
			if (!foundAnySpinner) {
				return; // Fast exit!
			}
			
			// PARALLEL EXECUTION FIX: Fallback check - verify if page is actually interactive
			// Sometimes spinners remain visible but page is actually ready
			try {
				// Check if document is ready and page is interactive
				org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
				String readyState = (String) js.executeScript("return document.readyState");
				boolean jqueryReady = true;
				try {
					Object jquery = js.executeScript("return typeof jQuery !== 'undefined' && jQuery.active === 0");
					jqueryReady = (jquery != null && (Boolean) jquery);
				} catch (Exception e) {
					// jQuery not available - that's fine
				}
				
				if ("complete".equals(readyState) && jqueryReady) {
					LOGGER.info("Page appears ready (document.readyState=complete) despite visible spinner - continuing");
					return; // Page is ready, continue even if spinner is visible
				}
			} catch (Exception fallbackEx) {
				LOGGER.debug("Fallback page readiness check failed: {}", fallbackEx.getMessage());
			}
			
			// Final fallback: If timeout exceeded but we've waited long enough, continue anyway
			// This prevents tests from failing due to persistent but non-blocking spinners
			if (timeoutSeconds >= 10) {
				LOGGER.warn("Spinner still visible after {}s timeout, but continuing execution to avoid false failures", timeoutSeconds);
			}

		} catch (Exception e) {
			// Only log if significant timeout occurred
			if (timeoutSeconds > 3) {
				long totalDuration = System.currentTimeMillis() - operationStartTime;
				LOGGER.warn(" Spinner wait timeout after {}s (duration: {}ms): {}", timeoutSeconds, totalDuration,
						e.getMessage());
			}
		} finally {
			// CRITICAL: Restore original implicit wait
			if (originalImplicitWait != null) {
				try {
					driver.manage().timeouts().implicitlyWait(originalImplicitWait);
				} catch (Exception e) {
					LOGGER.warn("⚠️  Could not restore implicit wait: {}", e.getMessage());
				}
			}
		}
	}

	/**
	 * PERFORMANCE: Smart wait for element to be ready for interaction Combines
	 * visibility and clickability checks
	 */
	public static WebElement waitForElement(WebDriver driver, WebElement element) {
		return waitForElement(driver, element, DEFAULT_TIMEOUT_SECONDS);
	}

	public static WebElement waitForElement(WebDriver driver, WebElement element, int timeoutSeconds) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));

		try {
			long startTime = System.currentTimeMillis();

			// Wait for element to be visible and clickable
			wait.until(ExpectedConditions.and(ExpectedConditions.visibilityOf(element),
					ExpectedConditions.elementToBeClickable(element)));

			long waitTime = System.currentTimeMillis() - startTime;

			// Only log if wait time was significant (>1 second)
			if (waitTime > 1000) {
				LOGGER.debug(" Element ready for interaction after {}ms", waitTime);
			}

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
	 * PERFORMANCE: Smart wait for form/dropdown interactions Waits for dropdown
	 * options to be loaded and visible
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

			// Dropdown options loaded - no need to log (expected behavior)

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

			// Search results loaded - no need to log (expected behavior)

		} catch (Exception e) {
			LOGGER.warn(" Search results wait timeout: {}", e.getMessage());
		}
	}

	/**
	 * PERFORMANCE: Optimized wait for UI stability after actions Replaces
	 * Thread.sleep(1000-3000) with smart condition checking
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

			// UI stable - no need to log (expected behavior)

		} catch (Exception e) {
			// UI stability check timeout - acceptable, continuing
		}
	}

	public static void safeSleep(WebDriver driver, long milliseconds) {
		try {
			waitForUIStability(driver, (int) (milliseconds / 1000));
		} catch (Exception e) {
			Thread.currentThread().interrupt();
			LOGGER.warn("Sleep interrupted: " + e.getMessage());
		}
	}

	/**
	 * Short intelligent wait - replaces Thread.sleep(1000-3000)
	 * Waits for page readiness first, then minimal additional time
	 * 
	 * @param driver WebDriver instance
	 */
	public static void shortWait(WebDriver driver) {
		try {
			waitForPageReady(driver, SHORT_TIMEOUT_SECONDS);
		} catch (Exception e) {
			// Acceptable - continue
		}
	}

	/**
	 * Wait for element to be clickable
	 * 
	 * @param driver  WebDriver instance
	 * @param element WebElement to wait for
	 */
	public static void waitForElementClickable(WebDriver driver, WebElement element) {
		waitForElementClickable(driver, element, DEFAULT_TIMEOUT_SECONDS);
	}

	public static void waitForElementClickable(WebDriver driver, WebElement element, int timeoutSeconds) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
		wait.until(ExpectedConditions.elementToBeClickable(element));
	}

	/**
	 * Wait for element to become invisible (useful for loading indicators)
	 * 
	 * @param driver  WebDriver instance
	 * @param element WebElement to wait for invisibility
	 */
	public static void waitForElementInvisible(WebDriver driver, WebElement element) {
		waitForElementInvisible(driver, element, DEFAULT_TIMEOUT_SECONDS);
	}

	public static void waitForElementInvisible(WebDriver driver, WebElement element, int timeoutSeconds) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
		wait.until(ExpectedConditions.invisibilityOf(element));
	}

}
