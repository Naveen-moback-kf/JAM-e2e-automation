package com.kfonetalentsuite.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

/**
 * Utility class providing headless-compatible actions for Selenium WebDriver.
 * All methods work reliably in both windowed and headless modes.
 * 
 * @author KF One Talent Suite Automation Team
 */
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

	/**
	 * Scrolls to the bottom of the page in a headless-compatible way. Uses
	 * document.documentElement.scrollHeight and triggers scroll events.
	 * 
	 * @throws InterruptedException if thread sleep is interrupted
	 */
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

	/**
	 * Scrolls to a specific element and brings it into view. Works reliably in
	 * headless mode by using multiple strategies.
	 * 
	 * @param element The element to scroll to
	 * @throws InterruptedException if thread sleep is interrupted
	 */
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

	/**
	 * Clicks an element using headless-compatible strategies. Tries multiple
	 * methods: regular click, JS click, and action click. Scrolls element into view
	 * before clicking.
	 * 
	 * @param element The element to click
	 * @return true if click succeeded, false otherwise
	 */
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

	/**
	 * Checks if an element is visible in the viewport. More reliable than
	 * isDisplayed() in headless mode.
	 * 
	 * @param element The element to check
	 * @return true if element is in viewport, false otherwise
	 */
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

	/**
	 * Waits for an element to be clickable and then clicks it. Scrolls element into
	 * view if needed.
	 * 
	 * @param element        The element to click
	 * @param timeoutSeconds Timeout in seconds
	 * @return true if click succeeded, false otherwise
	 */
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

	/**
	 * Scrolls within a specific container element (for scrollable divs). Useful for
	 * dropdowns, modals, etc.
	 * 
	 * @param container    The container element with overflow
	 * @param scrollAmount Amount to scroll in pixels (negative for up, positive for
	 *                     down)
	 * @throws InterruptedException if thread sleep is interrupted
	 */
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

	/**
	 * Scrolls a container to the top.
	 * 
	 * @param container The container element
	 * @throws InterruptedException if thread sleep is interrupted
	 */
	public void scrollContainerToTop(WebElement container) throws InterruptedException {
		try {
			js.executeScript("arguments[0].scrollTop = 0;", container);
			Thread.sleep(200);
			LOGGER.debug("Scrolled container to top");
		} catch (Exception e) {
			LOGGER.warn("Scroll container to top failed: {}", e.getMessage());
		}
	}

	/**
	 * Scrolls a container to the bottom.
	 * 
	 * @param container The container element
	 * @throws InterruptedException if thread sleep is interrupted
	 */
	public void scrollContainerToBottom(WebElement container) throws InterruptedException {
		try {
			js.executeScript("arguments[0].scrollTop = arguments[0].scrollHeight;", container);
			Thread.sleep(200);
			LOGGER.debug("Scrolled container to bottom");
		} catch (Exception e) {
			LOGGER.warn("Scroll container to bottom failed: {}", e.getMessage());
		}
	}

	/**
	 * Gets the current scroll height of the page. Uses documentElement for headless
	 * compatibility.
	 * 
	 * @return The scroll height in pixels
	 */
	public long getScrollHeight() {
		try {
			return (Long) js.executeScript("return document.documentElement.scrollHeight;");
		} catch (Exception e) {
			LOGGER.warn("Could not get scroll height: {}", e.getMessage());
			return 0;
		}
	}

	/**
	 * Scrolls to the last visible element in a list. Useful for triggering lazy
	 * loading.
	 * 
	 * @param elements List of elements
	 * @throws InterruptedException if thread sleep is interrupted
	 */
	public void scrollToLastElement(List<WebElement> elements) throws InterruptedException {
		if (elements != null && !elements.isEmpty()) {
			WebElement lastElement = elements.get(elements.size() - 1);
			scrollToElement(lastElement);
		}
	}

	/**
	 * Sends keys to an element with retry logic. Clears the field first if
	 * specified.
	 * 
	 * @param element    The element to send keys to
	 * @param text       The text to send
	 * @param clearFirst Whether to clear the field first
	 * @return true if successful, false otherwise
	 */
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

	/**
	 * Waits for the page to be fully loaded. Checks document.readyState and jQuery
	 * if available.
	 * 
	 * @param timeoutSeconds Timeout in seconds
	 */
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
