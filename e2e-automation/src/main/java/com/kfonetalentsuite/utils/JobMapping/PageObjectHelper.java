package com.kfonetalentsuite.utils.JobMapping;

import java.time.Duration;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.util.function.Supplier;

public class PageObjectHelper {

	public static void handleError(Logger logger, String methodName, String issueDescription, Exception e) {
		String errorMsg = issueDescription + " - Method: " + methodName;
		logger.error(errorMsg, e);
		ScreenshotHandler.captureFailureScreenshot(methodName, e);
		throw new RuntimeException(errorMsg, e);
	}

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

	public static void retryOnStaleElement(Logger logger, Runnable operation) {
		retryOnStaleElement(logger, () -> {
			operation.run();
			return null;
		});
	}

	public static WebElement waitForClickable(WebDriverWait wait, By locator) {
		return wait.until(ExpectedConditions.elementToBeClickable(locator));
	}

	public static WebElement waitForClickable(WebDriverWait wait, WebElement element) {
		return wait.until(ExpectedConditions.elementToBeClickable(element));
	}

	public static WebElement waitForVisible(WebDriverWait wait, By locator) {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	public static WebElement waitForVisible(WebDriverWait wait, WebElement element) {
		return wait.until(ExpectedConditions.visibilityOf(element));
	}

	public static WebElement waitForPresent(WebDriverWait wait, By locator) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
	}

	public static boolean waitForInvisible(WebDriverWait wait, By locator) {
		return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
	}

	public static boolean waitForInvisible(WebDriverWait wait, WebElement element) {
		return wait.until(ExpectedConditions.invisibilityOf(element));
	}

	public static WebElement waitAndClick(WebDriverWait wait, By locator) {
		WebElement element = waitForClickable(wait, locator);
		element.click();
		return element;
	}

	public static WebElement waitAndSendKeys(WebDriverWait wait, By locator, String text) {
		WebElement element = waitForClickable(wait, locator);
		element.clear();
		element.sendKeys(text);
		return element;
	}

	// ==================== METHODS MOVED FROM PerformanceUtils ====================
	// Consolidated all wait methods into PageObjectHelper for single source of truth

	private static final int DEFAULT_TIMEOUT_SECONDS = 20;
	private static final int SHORT_TIMEOUT_SECONDS = 5;
	private static final String[] SPINNER_SELECTORS = {
			"//div[@data-testid='loader']//img",
			"//div[contains(@class, 'loader')]",
			"//div[contains(@class, 'spinner')]",
			"//div[contains(@class, 'loading')]"
	};

	public static void waitForPageReady(WebDriver driver) {
		waitForPageReady(driver, DEFAULT_TIMEOUT_SECONDS);
	}

	public static void waitForPageReady(WebDriver driver, int timeoutSeconds) {
		WebDriverWait ultraFastWait = new WebDriverWait(driver, Duration.ofMillis(500));
		WebDriverWait quickWait = new WebDriverWait(driver, Duration.ofSeconds(Math.min(timeoutSeconds, 3)));

		try {
			try {
				Boolean isReady = (Boolean) ((org.openqa.selenium.JavascriptExecutor) driver)
						.executeScript("return document.readyState === 'complete'");
				if (Boolean.TRUE.equals(isReady)) {
					return;
				}
			} catch (Exception e) {
				// Continue with spinner checks
			}

			boolean foundVisibleSpinner = false;
			for (String selector : SPINNER_SELECTORS) {
				try {
					List<WebElement> spinners = driver.findElements(By.xpath(selector));
					if (!spinners.isEmpty()) {
						for (WebElement spinner : spinners) {
							try {
								if (spinner.isDisplayed()) {
									foundVisibleSpinner = true;
									ultraFastWait.until(ExpectedConditions.invisibilityOf(spinner));
									break;
								}
							} catch (Exception e) {
								// Spinner disappeared - good
							}
						}
					}

					if (!foundVisibleSpinner) {
						break;
					}

				} catch (Exception e) {
					// Continue to next spinner type
				}
			}

			if (!foundVisibleSpinner) {
				try {
					quickWait.until(webDriver -> ((org.openqa.selenium.JavascriptExecutor) webDriver)
							.executeScript("return document.readyState").equals("complete"));
				} catch (Exception e) {
					// Acceptable timeout
				}
			}

		} catch (Exception e) {
			// Page ready completed
		}
	}

	public static void waitForSpinnersToDisappear(WebDriver driver) {
		waitForSpinnersToDisappear(driver, DEFAULT_TIMEOUT_SECONDS);
	}

	public static void waitForSpinnersToDisappear(WebDriver driver, int timeoutSeconds) {
		long operationStartTime = System.currentTimeMillis();
		Duration originalImplicitWait = null;
		
		try {
			originalImplicitWait = driver.manage().timeouts().getImplicitWaitTimeout();
			driver.manage().timeouts().implicitlyWait(Duration.ZERO);
		} catch (Exception e) {
			// Implicit wait modification failed - continue anyway
		}

		WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofMillis(500));
		long maxWaitTime = timeoutSeconds * 1000L;

		try {
			boolean foundAnySpinner = false;

			while ((System.currentTimeMillis() - operationStartTime) < maxWaitTime) {
				boolean foundSpinnerInThisIteration = false;

				for (String selector : SPINNER_SELECTORS) {
					try {
						List<WebElement> spinners = driver.findElements(By.xpath(selector));
						if (!spinners.isEmpty()) {
							for (WebElement spinner : spinners) {
								try {
								if (spinner.isDisplayed()) {
									foundAnySpinner = true;
									foundSpinnerInThisIteration = true;
									try {
											shortWait.until(ExpectedConditions.invisibilityOf(spinner));
										} catch (org.openqa.selenium.TimeoutException te) {
											// Will check again
										}
									}
								} catch (org.openqa.selenium.StaleElementReferenceException e) {
									// Spinner disappeared - good
								} catch (Exception e) {
									// Continue
								}
							}
						}
					} catch (Exception e) {
						// Continue
					}
				}

				if (!foundSpinnerInThisIteration) {
					return;
				}

				Thread.sleep(100);
			}

			if (!foundAnySpinner) {
				return;
			}

		} catch (Exception e) {
			// Timeout acceptable
		} finally {
			if (originalImplicitWait != null) {
				try {
					driver.manage().timeouts().implicitlyWait(originalImplicitWait);
				} catch (Exception e) {
					// Continue
				}
			}
		}
	}

	public static void waitForUIStability(WebDriver driver) {
		waitForUIStability(driver, SHORT_TIMEOUT_SECONDS);
	}

	public static void waitForUIStability(WebDriver driver, int timeoutSeconds) {
		try {
			waitForPageReady(driver, timeoutSeconds);

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
			wait.until(webDriver -> {
				try {
					Object result = ((org.openqa.selenium.JavascriptExecutor) webDriver)
							.executeScript("return window.jQuery && jQuery.active == 0");
					return result == null || Boolean.TRUE.equals(result);
				} catch (Exception e) {
					return true;
				}
			});

		} catch (Exception e) {
			// UI stability check timeout - acceptable
		}
	}

}

