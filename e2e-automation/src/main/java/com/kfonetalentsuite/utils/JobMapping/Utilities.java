package com.kfonetalentsuite.utils.JobMapping;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.util.List;
import java.util.function.Supplier;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.kfonetalentsuite.utils.common.CommonVariableManager;
import com.kfonetalentsuite.utils.common.ScreenshotHandler;
import com.kfonetalentsuite.webdriverManager.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

public class Utilities {
	private static final Logger LOGGER = LogManager.getLogger(Utilities.class);

	// =============================================
	// ERROR HANDLING & RETRY UTILITIES
	// =============================================

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

	// =============================================
	// WAIT UTILITIES
	// =============================================

	private static final int DEFAULT_TIMEOUT_SECONDS = 20;
	private static final int SHORT_TIMEOUT_SECONDS = 5;
	private static final String[] SPINNER_SELECTORS = {
			"//div[@data-testid='loader']//img",
			"//div[contains(@class, 'loader')]",
			"//div[contains(@class, 'spinner')]",
			"//div[contains(@class, 'loading')]"
	};

	// Basic wait methods
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

	// Page readiness methods
	public static void waitForPageReady(WebDriver driver) {
		waitForPageReady(driver, DEFAULT_TIMEOUT_SECONDS);
	}

	public static void waitForPageReady(WebDriver driver, int timeoutSeconds) {
		WebDriverWait ultraFastWait = new WebDriverWait(driver, Duration.ofMillis(500));
		WebDriverWait quickWait = new WebDriverWait(driver, Duration.ofSeconds(Math.min(timeoutSeconds, 3)));

		try {
			try {
				Boolean isReady = (Boolean) ((JavascriptExecutor) driver)
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
					quickWait.until(webDriver -> ((JavascriptExecutor) webDriver)
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
										} catch (TimeoutException te) {
											// Will check again
										}
									}
								} catch (StaleElementReferenceException e) {
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
					Object result = ((JavascriptExecutor) webDriver)
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

	// =============================================
	// ELEMENT FINDING UTILITIES
	// =============================================

	public static WebElement getFirstElementOrNull(WebDriver driver, By locator) {
		List<WebElement> elements = driver.findElements(locator);
		return elements.isEmpty() ? null : elements.get(0);
	}

	public static boolean hasElements(WebDriver driver, By locator) {
		return !driver.findElements(locator).isEmpty();
	}

	public static boolean isElementDisplayed(WebDriver driver, By locator) {
		try {
			return driver.findElement(locator).isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	public static String getElementText(WebDriverWait wait, By locator, Logger logger) {
		try {
			return waitForVisible(wait, locator).getText();
		} catch (Exception e) {
			if (logger != null) {
				logger.debug("Could not get text from element: {}", e.getMessage());
			}
			return "";
		}
	}

	// =============================================
	// CLICK UTILITIES
	// =============================================

	public static void clickElement(WebDriver driver, WebDriverWait wait, JavascriptExecutor js, WebElement element) {
		try {
			waitForClickable(wait, element).click();
		} catch (Exception e) {
			try {
				js.executeScript("arguments[0].click();", element);
			} catch (Exception ex) {
				// Click failed
			}
		}
	}

	public static void clickElement(WebDriver driver, WebDriverWait wait, JavascriptExecutor js, By locator) {
		clickElementSafely(driver, wait, js, locator, 10);
	}

	public static void clickElementSafely(WebDriver driver, WebDriverWait wait, JavascriptExecutor js, By locator, int timeoutSeconds) {
		waitForPageReady(driver, 2);
		
		int maxRetries = 3;
		
		for (int attempt = 1; attempt <= maxRetries; attempt++) {
			try {
				WebElement element = waitForClickable(wait, locator);
				element.click();
				waitForUIStability(driver, 1);
				return;
			} catch (StaleElementReferenceException e) {
				if (attempt == maxRetries) {
					try {
						WebElement element = driver.findElement(locator);
						js.executeScript("arguments[0].click();", element);
						waitForUIStability(driver, 1);
						return;
					} catch (Exception ex) {
						throw new RuntimeException("Failed to click element after all retries", ex);
					}
				}
				try {
					Thread.sleep(300);
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
					throw new RuntimeException("Interrupted during retry", ie);
				}
			} catch (Exception e) {
				if (attempt == maxRetries) {
					try {
						WebElement element = driver.findElement(locator);
						js.executeScript("arguments[0].click();", element);
						waitForUIStability(driver, 1);
						return;
					} catch (Exception ex) {
						throw new RuntimeException("Failed to click element", ex);
					}
				}
				try {
					Thread.sleep(300);
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
					throw new RuntimeException("Interrupted during retry", ie);
				}
			}
		}
	}

	public static void jsClick(JavascriptExecutor js, WebElement element) {
		js.executeScript("arguments[0].click();", element);
	}

	public static boolean tryClickWithStrategies(WebDriver driver, JavascriptExecutor js, WebElement element) {
		try {
			if (element.isDisplayed() && element.isEnabled()) {
				element.click();
				waitForUIStability(driver, 1);
				return true;
			}
		} catch (Exception e) {
		}

		try {
			js.executeScript("arguments[0].click();", element);
			waitForUIStability(driver, 1);
			return true;
		} catch (Exception e) {
		}

		try {
			new org.openqa.selenium.interactions.Actions(driver).moveToElement(element).click().perform();
			waitForUIStability(driver, 1);
			return true;
		} catch (Exception e) {
		}

		return false;
	}

	public static boolean tryClickWithStrategies(WebDriver driver, JavascriptExecutor js, By locator, String elementName, Logger logger) {
		try {
			WebElement element = driver.findElement(locator);
			return tryClickWithStrategies(driver, js, element);
		} catch (Exception e) {
			if (logger != null) {
				logger.warn("Failed to click {}: {}", elementName, e.getMessage());
			}
			return false;
		}
	}

	// =============================================
	// SCROLL UTILITIES
	// =============================================

	public static void scrollToElement(JavascriptExecutor js, WebElement element) {
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	public static void scrollToTop(JavascriptExecutor js) {
		js.executeScript("window.scrollTo(0, 0);");
	}

	public static void scrollToBottom(JavascriptExecutor js) {
		js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
	}

	// =============================================
	// TEXT PROCESSING UTILITIES
	// =============================================

	public static String extractCellText(WebElement cell) {
		if (cell == null) return "";
		try {
			String text = cell.getText();
			if (text == null || text.trim().isEmpty()) {
				text = cell.getAttribute("innerText");
			}
			return text != null ? text.trim() : "";
		} catch (Exception e) {
			return "";
		}
	}

	public static String normalizeFieldValue(String fieldValue) {
		if (fieldValue == null) return "";
		String normalized = fieldValue.trim();
		if (normalized.startsWith("-")) normalized = normalized.substring(1).trim();
		if (normalized.endsWith("-")) normalized = normalized.substring(0, normalized.length() - 1).trim();
		return normalized;
	}

	public static String getValueOrEmpty(String value) {
		return isMissingData(value) ? "[EMPTY]" : value;
	}

	public static String normalizeForSorting(String value) {
		if (value == null) return "";
		return value.replaceAll("[-'\"()\\[\\].,:;!?]", "").trim();
	}

	public static boolean isMissingData(String value) {
		if (value == null || value.trim().isEmpty()) return true;
		String normalized = value.trim().toLowerCase();
		return normalized.equals("n/a") || normalized.equals("-") || normalized.equals("na") 
				|| normalized.equals("null") || normalized.equals("none");
	}

	public static boolean isNonAscii(String value) {
		return value != null && !value.matches("\\A\\p{ASCII}*\\z");
	}

	// =============================================
	// DATE UTILITIES
	// =============================================

	public static String formatCurrentDate() {
		java.time.LocalDate today = java.time.LocalDate.now();
		java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("MM/dd/yyyy");
		return today.format(formatter);
	}

	public static String formatDateForDisplay() {
		java.time.LocalDate currentDate = java.time.LocalDate.now();
		int currentYear = currentDate.getYear();
		int currentDay = currentDate.getDayOfMonth();
		java.time.Month currentMonth = currentDate.getMonth();
		String dayStr = (currentDay < 10) ? new java.text.DecimalFormat("00").format(currentDay) : String.valueOf(currentDay);
		return currentMonth.toString().substring(0, 1) + currentMonth.toString().substring(1, 3).toLowerCase() + " " + dayStr + ", " + currentYear;
	}

	// =============================================
	// VALIDATION UTILITIES
	// =============================================

	public static int validateAscendingOrder(List<String> values) {
		int violations = 0;
		for (int i = 0; i < values.size() - 1; i++) {
			String currentNormalized = normalizeForSorting(values.get(i));
			String nextNormalized = normalizeForSorting(values.get(i + 1));

			if (isNonAscii(currentNormalized) && isNonAscii(nextNormalized)) {
				continue;
			}

			if (shouldSkipInSortValidation(currentNormalized) || shouldSkipInSortValidation(nextNormalized)) {
				continue;
			}

			if (currentNormalized.compareToIgnoreCase(nextNormalized) > 0) {
				violations++;
			}
		}
		return violations;
	}

	public static int validateDescendingOrder(List<String> values) {
		int violations = 0;
		for (int i = 0; i < values.size() - 1; i++) {
			String currentNormalized = normalizeForSorting(values.get(i));
			String nextNormalized = normalizeForSorting(values.get(i + 1));

			if (isNonAscii(currentNormalized) && isNonAscii(nextNormalized)) {
				continue;
			}

			if (shouldSkipInSortValidation(currentNormalized) || shouldSkipInSortValidation(nextNormalized)) {
				continue;
			}

			if (currentNormalized.compareToIgnoreCase(nextNormalized) < 0) {
				violations++;
			}
		}
		return violations;
	}

	public static boolean shouldSkipInSortValidation(String value) {
		if (value == null || value.trim().isEmpty()) return true;
		String lower = value.toLowerCase();
		return lower.equals("n/a") || lower.equals("na") || lower.equals("-") 
				|| lower.equals("null") || lower.equals("none") || lower.equals("[empty]");
	}

	// =============================================
	// PARSING UTILITIES
	// =============================================

	public static int parseProfileCountFromText(String countText) {
		try {
			if (countText != null && countText.contains("of")) {
				String afterOf = countText.substring(countText.indexOf("of") + 2).trim();
				String totalCountStr = afterOf.split("\\s+")[0].replaceAll("[^0-9]", "");
				if (!totalCountStr.isEmpty()) {
					return Integer.parseInt(totalCountStr);
				}
			}
		} catch (Exception e) {
			// Failed to parse
		}
		return 0;
	}

	public static int getRowIndex(WebDriver driver, WebElement rowElement) {
		try {
			String dataIndex = rowElement.getAttribute("data-row-index");
			if (dataIndex != null && !dataIndex.isEmpty()) {
				return Integer.parseInt(dataIndex);
			}
			List<WebElement> allRows = driver.findElements(By.xpath("//tbody//tr"));
			for (int i = 0; i < allRows.size(); i++) {
				if (allRows.get(i).equals(rowElement)) {
					return i + 1;
				}
			}
		} catch (Exception e) {
			// Failed to get row index
		}
		return -1;
	}

	// =============================================
	// PAGE OPERATIONS
	// =============================================

	public static void refreshPage(WebDriver driver) {
		driver.navigate().refresh();
		waitForPageReady(driver);
	}

	// =============================================
	// FILE UPLOAD UTILITIES
	// =============================================

	public static boolean uploadFile(WebDriver driver, String filePath, By browseButtonLocator) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		
		if (tryDirectFileInput(driver, filePath)) return true;
		if (tryClickAndFindInput(driver, js, filePath, browseButtonLocator)) return true;
		if (tryJavaScriptUpload(js, filePath)) return true;
		if (!isHeadlessMode(js) && tryRobotUpload(driver, filePath, browseButtonLocator)) return true;
		
		return false;
	}

	public static boolean tryDirectFileInput(WebDriver driver, String filePath) {
		String[] selectors = {
			"//input[@type='file']",
			"//input[contains(@id,'upload')]",
			"//input[contains(@class,'upload')]",
			"//input[contains(@name,'file')]",
			"//input[contains(@accept,'.csv')]"
		};

		for (String selector : selectors) {
			try {
				List<WebElement> inputs = driver.findElements(By.xpath(selector));
				for (WebElement input : inputs) {
				if (input.isEnabled()) {
					input.sendKeys(filePath);
					try { Thread.sleep(2000); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
					return true;
				}
				}
			} catch (Exception e) {
				continue;
			}
		}
		return false;
	}

	public static boolean tryClickAndFindInput(WebDriver driver, JavascriptExecutor js, String filePath, By browseBtn) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			wait.until(ExpectedConditions.elementToBeClickable(browseBtn)).click();
			try { Thread.sleep(1000); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }

			String[] selectors = {
				"//input[@type='file']",
				"//input[contains(@style,'opacity: 0') or contains(@style,'display: none')][@type='file']",
				"//input[contains(@id,'upload')]",
				"//input[contains(@class,'upload')]"
			};

			for (String selector : selectors) {
				try {
					WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
					List<WebElement> inputs = shortWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(selector)));

					for (WebElement input : inputs) {
						try {
					js.executeScript("arguments[0].style.display='block'; arguments[0].style.visibility='visible'; arguments[0].style.opacity='1';", input);
					input.sendKeys(filePath);
					try { Thread.sleep(2000); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
					return true;
						} catch (Exception e) {
							continue;
						}
					}
				} catch (Exception e) {
					continue;
				}
			}
		} catch (Exception e) {
		}
		return false;
	}

	public static boolean tryJavaScriptUpload(JavascriptExecutor js, String filePath) {
		try {
			String script = """
				var fileInput = document.createElement('input');
				fileInput.type = 'file';
				fileInput.style.display = 'none';
				document.body.appendChild(fileInput);
				return fileInput;
			""";

			WebElement input = (WebElement) js.executeScript(script);
			if (input != null) {
				input.sendKeys(filePath);
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	public static boolean tryRobotUpload(WebDriver driver, String filePath, By browseBtn) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			wait.until(ExpectedConditions.elementToBeClickable(browseBtn)).click();

			Robot rb = new Robot();
			rb.delay(2000);

			StringSelection ss = new StringSelection(filePath);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);

			rb.keyPress(KeyEvent.VK_CONTROL);
			rb.keyPress(KeyEvent.VK_V);
			rb.delay(1000);
			rb.keyRelease(KeyEvent.VK_V);
			rb.keyRelease(KeyEvent.VK_CONTROL);
			rb.delay(1000);

			rb.keyPress(KeyEvent.VK_ENTER);
			rb.keyRelease(KeyEvent.VK_ENTER);
			rb.delay(2000);

			return true;
		} catch (Exception e) {
		}
		return false;
	}

	public static boolean isHeadlessMode(JavascriptExecutor js) {
		try {
			return js.executeScript("return navigator.webdriver === true").toString().equals("true")
				|| System.getProperty("java.awt.headless", "false").equals("true")
				|| System.getProperty("webdriver.chrome.args", "").contains("--headless");
		} catch (Exception e) {
			return true;
		}
	}

	public static String getUserRoleFromSessionStorage() {
		try {
			WebDriver driver = DriverManager.getDriver();
			JavascriptExecutor js = (JavascriptExecutor) driver;

			LOGGER.info("Extracting user role from session storage...");

			// Execute JavaScript to get user role from session storage
			String script = """
						try {
							// Get the HayGroup.user.roles from session storage
							var userRolesData = sessionStorage.getItem('HayGroup.user.roles');

							if (!userRolesData) {
								return 'SESSION_STORAGE_KEY_NOT_FOUND';
							}

							// Parse the JSON data
							var rolesArray = JSON.parse(userRolesData);

							if (!Array.isArray(rolesArray) || rolesArray.length === 0) {
								return 'ROLES_ARRAY_EMPTY';
							}

							// Get the first role (index 0) and extract the name
							var firstRole = rolesArray[0];

							if (!firstRole || !firstRole.name) {
								return 'ROLE_NAME_NOT_FOUND';
							}

							return firstRole.name;

						} catch (error) {
							return 'ERROR: ' + error.message;
						}
					""";

			Object result = js.executeScript(script);
			String roleName = (result != null) ? result.toString() : null;

			if (roleName != null && !roleName.startsWith("ERROR") && !roleName.equals("SESSION_STORAGE_KEY_NOT_FOUND")
					&& !roleName.equals("ROLES_ARRAY_EMPTY") && !roleName.equals("ROLE_NAME_NOT_FOUND")) {

				// Store the role globally for use across all feature files
				CommonVariableManager.CURRENT_USER_ROLE.set(roleName);
				LOGGER.info("Current User Role : '{}'", roleName);
				
				// Also fetch and store userLevelJobMappingEnabled and userLevelPermission
				fetchAndStoreUserLevelSettings(js);
				
				return roleName;
			} else {
				LOGGER.warn("Failed to extract user role. Result: {}", roleName);
				CommonVariableManager.CURRENT_USER_ROLE.set(null);
				return null;
			}

		} catch (Exception e) {
			LOGGER.error("Exception while extracting user role from session storage: {}", e.getMessage());
			CommonVariableManager.CURRENT_USER_ROLE.set(null);
			return null;
		}
	}

	private static void fetchAndStoreUserLevelSettings(JavascriptExecutor js) {
		try {
			// Fetch userLevelJobMappingEnabled
			String jobMappingEnabledScript = """
						try {
							var value = sessionStorage.getItem('userLevelJobMappingEnabled');
							return value !== null ? value : 'NOT_FOUND';
						} catch (error) {
							return 'ERROR: ' + error.message;
						}
					""";

			Object jobMappingResult = js.executeScript(jobMappingEnabledScript);
			String jobMappingValue = (jobMappingResult != null) ? jobMappingResult.toString() : null;

			if (jobMappingValue != null && !jobMappingValue.equals("NOT_FOUND") && !jobMappingValue.startsWith("ERROR")) {
				Boolean isEnabled = "true".equalsIgnoreCase(jobMappingValue);
				CommonVariableManager.USER_LEVEL_JOB_MAPPING_ENABLED.set(isEnabled);
				LOGGER.info("User Level Job Mapping Enabled: '{}'", isEnabled);
			} else {
				CommonVariableManager.USER_LEVEL_JOB_MAPPING_ENABLED.set(null);
				LOGGER.debug("userLevelJobMappingEnabled not found in session storage");
			}

			// Fetch userLevelPermission
			String permissionScript = """
						try {
							var value = sessionStorage.getItem('userLevelPermission');
							return value !== null ? value : 'NOT_FOUND';
						} catch (error) {
							return 'ERROR: ' + error.message;
						}
					""";

			Object permissionResult = js.executeScript(permissionScript);
			String permissionValue = (permissionResult != null) ? permissionResult.toString() : null;

			if (permissionValue != null && !permissionValue.equals("NOT_FOUND") && !permissionValue.startsWith("ERROR")) {
				CommonVariableManager.USER_LEVEL_PERMISSION.set(permissionValue);
				LOGGER.info("User Level Permission: '{}'", permissionValue);
			} else {
				CommonVariableManager.USER_LEVEL_PERMISSION.set(null);
				LOGGER.debug("userLevelPermission not found in session storage");
			}

		} catch (Exception e) {
			LOGGER.warn("Failed to fetch user level settings from session storage: {}", e.getMessage());
			CommonVariableManager.USER_LEVEL_JOB_MAPPING_ENABLED.set(null);
			CommonVariableManager.USER_LEVEL_PERMISSION.set(null);
		}
	}

	public static Boolean isUserLevelJobMappingEnabled() {
		return CommonVariableManager.USER_LEVEL_JOB_MAPPING_ENABLED.get();
	}

	public static boolean hasJobMappingAccess() {
		Boolean enabled = CommonVariableManager.USER_LEVEL_JOB_MAPPING_ENABLED.get();
		return Boolean.TRUE.equals(enabled);
	}

	public static String getUserLevelPermission() {
		return CommonVariableManager.USER_LEVEL_PERMISSION.get();
	}

	public static boolean hasHCMSyncAccess() {
		String permission = CommonVariableManager.USER_LEVEL_PERMISSION.get();
		return "true".equalsIgnoreCase(permission);
	}

	public static void clearUserLevelSettings() {
		CommonVariableManager.USER_LEVEL_JOB_MAPPING_ENABLED.set(null);
		CommonVariableManager.USER_LEVEL_PERMISSION.set(null);
		LOGGER.info("Cleared user level settings");
	}

	public static String getCurrentUserRole() {
		return CommonVariableManager.CURRENT_USER_ROLE.get();
	}

	public static boolean hasRole(String expectedRole) {
		String currentRole = CommonVariableManager.CURRENT_USER_ROLE.get();
		if (currentRole == null || expectedRole == null) {
			return false;
		}
		return currentRole.equals(expectedRole);
	}

	public static boolean setUserSessionDetailsFromSessionStorage() {
		String role = getUserRoleFromSessionStorage();
		return role != null;
	}
	
	@Deprecated
	public static boolean setCurrentUserRoleFromSessionStorage() {
		return setUserSessionDetailsFromSessionStorage();
	}

	public static void clearCurrentUserRole() {
		CommonVariableManager.CURRENT_USER_ROLE.set(null);
		CommonVariableManager.USER_LEVEL_JOB_MAPPING_ENABLED.set(null);
		CommonVariableManager.USER_LEVEL_PERMISSION.set(null);
		LOGGER.info("Cleared stored user role and user level settings");
	}

	public static boolean verifyUserRole(String expectedRole) {
		try {
			// First try to get from global variable
			String actualRole = CommonVariableManager.CURRENT_USER_ROLE.get();

			// If not set globally, fetch from session storage and store it
			if (actualRole == null) {
				LOGGER.info("User role not stored globally, fetching from session storage...");
				actualRole = getUserRoleFromSessionStorage();
			}

			if (actualRole == null) {
				LOGGER.error(" Could not retrieve user role from session storage");
				return false;
			}

			LOGGER.info(" Current User Role: '{}'", actualRole);

			if (expectedRole != null && !expectedRole.trim().isEmpty()) {
				if (actualRole.equals(expectedRole)) {
					LOGGER.info("... User role verification PASSED: Expected '{}', Found '{}'", expectedRole,
							actualRole);
					return true;
				} else {
					LOGGER.error(" User role verification FAILED: Expected '{}', Found '{}'", expectedRole, actualRole);
					return false;
				}
			} else {
				LOGGER.info("... User role successfully retrieved: '{}'", actualRole);
				return true;
			}

		} catch (Exception e) {
			LOGGER.error("Exception during user role verification: {}", e.getMessage());
			return false;
		}
	}

}
