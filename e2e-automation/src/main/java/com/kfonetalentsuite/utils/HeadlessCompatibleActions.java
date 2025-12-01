package com.kfonetalentsuite.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

public class HeadlessCompatibleActions {

	private static final Logger LOGGER = LoggerFactory.getLogger(HeadlessCompatibleActions.class);
	private final WebDriver driver;
	private final JavascriptExecutor js;
	private final WebDriverWait wait;

	public HeadlessCompatibleActions(WebDriver driver) {
		this.driver = driver;
		this.js = (JavascriptExecutor) driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
	}

	public void scrollToBottom() throws InterruptedException {
		try {
			// Use documentElement instead of body (more reliable in headless)
			js.executeScript("window.scrollTo(0, document.documentElement.scrollHeight);");
			Thread.sleep(500);

			// Trigger scroll event for lazy loading
			js.executeScript("window.dispatchEvent(new Event('scroll'));");
			Thread.sleep(300);

			LOGGER.debug("Scrolled to bottom (headless-compatible)");
		} catch (Exception e) {
			LOGGER.warn("Scroll to bottom failed: {}", e.getMessage());
			// Fallback
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
			Thread.sleep(500);
		}
	}

	public void scrollToElement(WebElement element) throws InterruptedException {
		try {
			// Method 1: scrollIntoView with center alignment (best for headless)
			js.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center', inline: 'center'});",
					element);
			Thread.sleep(300);

			// Method 2: Ensure element is not behind fixed headers
			js.executeScript("window.scrollBy(0, -150);"); // Scroll up 150px to avoid headers
			Thread.sleep(200);

			LOGGER.debug("Scrolled to element (headless-compatible)");
		} catch (Exception e) {
			LOGGER.warn("Scroll to element failed: {}", e.getMessage());
		}
	}

	public boolean clickElement(WebElement element) {
		try {
			// Scroll element into view first
			scrollToElement(element);

			// Strategy 1: Regular Selenium click
			try {
				wait.until(ExpectedConditions.elementToBeClickable(element)).click();
				LOGGER.debug("Clicked element using regular click");
				return true;
			} catch (Exception e1) {
				LOGGER.debug("Regular click failed: {}", e1.getMessage());

				// Strategy 2: JavaScript click (most reliable in headless)
				try {
					js.executeScript("arguments[0].click();", element);
					LOGGER.debug("Clicked element using JS click");
					return true;
				} catch (Exception e2) {
					LOGGER.debug("JS click failed: {}", e2.getMessage());

					// Strategy 3: Force click by removing overlays
					try {
						js.executeScript("var element = arguments[0];" + "var event = new MouseEvent('click', {"
								+ "    'view': window," + "    'bubbles': true," + "    'cancelable': true" + "});"
								+ "element.dispatchEvent(event);", element);
						LOGGER.debug("Clicked element using MouseEvent dispatch");
						return true;
					} catch (Exception e3) {
						LOGGER.error("All click strategies failed");
						return false;
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Click element failed: {}", e.getMessage());
			return false;
		}
	}

	public boolean isElementInViewport(WebElement element) {
		try {
			Boolean inViewport = (Boolean) js.executeScript(
					"var elem = arguments[0];" + "var rect = elem.getBoundingClientRect();" + "return ("
							+ "    rect.top >= 0 &&" + "    rect.left >= 0 &&"
							+ "    rect.bottom <= (window.innerHeight || document.documentElement.clientHeight) &&"
							+ "    rect.right <= (window.innerWidth || document.documentElement.clientWidth)" + ");",
					element);
			return inViewport != null && inViewport;
		} catch (Exception e) {
			LOGGER.warn("Could not check if element is in viewport: {}", e.getMessage());
			return false;
		}
	}

	public boolean waitAndClick(WebElement element, int timeoutSeconds) {
		try {
			WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
			customWait.until(ExpectedConditions.elementToBeClickable(element));
			return clickElement(element);
		} catch (Exception e) {
			LOGGER.error("Wait and click failed: {}", e.getMessage());
			return false;
		}
	}


	public void scrollWithinContainer(WebElement container, int scrollAmount) throws InterruptedException {
		try {
			js.executeScript("arguments[0].scrollTop = arguments[0].scrollTop + arguments[1];", container,
					scrollAmount);
			Thread.sleep(200);
			LOGGER.debug("Scrolled within container by {} pixels", scrollAmount);
		} catch (Exception e) {
			LOGGER.warn("Scroll within container failed: {}", e.getMessage());
		}
	}


	public void scrollContainerToTop(WebElement container) throws InterruptedException {
		try {
			js.executeScript("arguments[0].scrollTop = 0;", container);
			Thread.sleep(200);
			LOGGER.debug("Scrolled container to top");
		} catch (Exception e) {
			LOGGER.warn("Scroll container to top failed: {}", e.getMessage());
		}
	}


	public void scrollContainerToBottom(WebElement container) throws InterruptedException {
		try {
			js.executeScript("arguments[0].scrollTop = arguments[0].scrollHeight;", container);
			Thread.sleep(200);
			LOGGER.debug("Scrolled container to bottom");
		} catch (Exception e) {
			LOGGER.warn("Scroll container to bottom failed: {}", e.getMessage());
		}
	}


	public long getScrollHeight() {
		try {
			return (Long) js.executeScript("return document.documentElement.scrollHeight;");
		} catch (Exception e) {
			LOGGER.warn("Could not get scroll height: {}", e.getMessage());
			return 0;
		}
	}


	public void scrollToLastElement(List<WebElement> elements) throws InterruptedException {
		if (elements != null && !elements.isEmpty()) {
			WebElement lastElement = elements.get(elements.size() - 1);
			scrollToElement(lastElement);
		}
	}

	public boolean sendKeysWithRetry(WebElement element, String text, boolean clearFirst) {
		try {
			wait.until(ExpectedConditions.visibilityOf(element));

			if (clearFirst) {
				try {
					element.clear();
				} catch (Exception e) {
					// If clear fails, try JS clear
					js.executeScript("arguments[0].value = '';", element);
				}
			}

			element.sendKeys(text);
			LOGGER.debug("Sent keys to element: {}", text);
			return true;
		} catch (Exception e) {
			LOGGER.error("Send keys failed: {}", e.getMessage());
			// Try JS fallback
			try {
				if (clearFirst) {
					js.executeScript("arguments[0].value = '';", element);
				}
				js.executeScript("arguments[0].value = arguments[1];", element, text);
				// Trigger input event
				js.executeScript("arguments[0].dispatchEvent(new Event('input', { bubbles: true }));", element);
				LOGGER.debug("Sent keys using JS fallback");
				return true;
			} catch (Exception ex) {
				LOGGER.error("JS send keys also failed: {}", ex.getMessage());
				return false;
			}
		}
	}

	public void waitForPageLoad(int timeoutSeconds) {
		try {
			WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));

			// Wait for document ready state
			customWait.until(driver1 -> {
				String readyState = js.executeScript("return document.readyState").toString();
				return "complete".equals(readyState);
			});

			// Wait for jQuery if present
			try {
				customWait.until(driver1 -> {
					Boolean jQueryDefined = (Boolean) js.executeScript("return typeof jQuery != 'undefined'");
					if (jQueryDefined) {
						return (Boolean) js.executeScript("return jQuery.active == 0");
					}
					return true;
				});
			} catch (Exception e) {
				// jQuery not present, that's fine
			}

			LOGGER.debug("Page fully loaded");
		} catch (Exception e) {
			LOGGER.warn("Wait for page load timed out: {}", e.getMessage());
		}
	}
}
