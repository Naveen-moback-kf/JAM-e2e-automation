package com.kfonetalentsuite.pageobjects.JobMapping;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.StaleElementReferenceException;
import org.testng.Assert;

import com.kfonetalentsuite.utils.JobMapping.Utilities;
import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.PageObjectHelper;
import com.kfonetalentsuite.webdriverManager.DriverManager;

public class PO25_ValidateExportStatusFunctionality_PM {
	WebDriver driver = DriverManager.getDriver();

	protected static final Logger LOGGER = (Logger) LogManager.getLogger();
	PO25_ValidateExportStatusFunctionality_PM validateExportStatusFunctionality_PM;
	// THREAD-SAFE: Each thread gets its own isolated state for parallel execution
	public static ThreadLocal<Integer> rowNumber = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<String> SPJobName = ThreadLocal.withInitial(() -> null);

	public PO25_ValidateExportStatusFunctionality_PM() throws IOException {
		PageFactory.initElements(driver, this);
	}

	WebDriverWait wait = DriverManager.getWait();
	Utilities utils = new Utilities();
	JavascriptExecutor js = (JavascriptExecutor) driver;

	// XAPTHs
	@FindBy(xpath = "//*[@class='blocking-loader']//img")
	@CacheLookup
	WebElement pageLoadSpinner;

	@FindBy(xpath = "//thead//tr//div[@kf-sort-header='name']//div")
	@CacheLookup
	WebElement tableHeader1;

	@FindBy(xpath = "//thead//tr//div//div[text()=' Status ']")
	@CacheLookup
	WebElement tableHeader2;

	@FindBy(xpath = "//thead//tr//div//div[text()=' kf grade ']")
	@CacheLookup
	WebElement tableHeader3;

	@FindBy(xpath = "//thead//tr//div//div[text()=' Level ']")
	@CacheLookup
	WebElement tableHeader4;

	@FindBy(xpath = "//thead//tr//div//div[text()=' Function ']")
	@CacheLookup
	WebElement tableHeader5;

	@FindBy(xpath = "//thead//tr//div//div[text()=' Created By ']")
	@CacheLookup
	WebElement tableHeader6;

	@FindBy(xpath = "//thead//tr//div//div[text()=' Last Modified ']")
	@CacheLookup
	WebElement tableHeader7;

	@FindBy(xpath = "//thead//tr//div//div[text()=' Export status ']")
	@CacheLookup
	WebElement tableHeader8;

	@FindBy(xpath = "//span[contains(text(),'Select your view')]")
	@CacheLookup
	public WebElement SPdetailsPageText;

	@FindBy(xpath = "//kf-icon[contains(@class,'dots-three')]")
	@CacheLookup
	public WebElement threeDotsinSPdetailsPage;

	@FindBy(xpath = "//*[contains(text(),'Edit Success')]")
	@CacheLookup
	public WebElement editSPbuttoninSPdetailsPage;

	@FindBy(xpath = "//*[contains(@class,'editDetails')]")
	@CacheLookup
	public WebElement editDetailsBtn;

	@FindBy(xpath = "//label[contains(text(),'Function')]//..//..//button")
	@CacheLookup
	public WebElement FunctionDropdownofSP;

	// REMOVED: Hardcoded function/subfunction values - now dynamically selected
	// from available options
	// Old elements: FunctionValue ("Academic"), SubFunctionValue ("Academic Medical
	// Affairs")
	// New approach: Select first available option from dropdown (see
	// modify_function_and_sub_function_values_of_the_success_profile method)

	@FindBy(xpath = "//label[contains(text(),'Subfunction')]//..//..//button")
	@CacheLookup
	public WebElement SubFunctionDropdownofSP;

	@FindBy(xpath = "//*[contains(text(),'Done')]")
	@CacheLookup
	public WebElement DoneBtnofSP;

	@FindBy(xpath = "//button[text()='Save']")
	@CacheLookup
	public WebElement SaveBtnofSP;

	@FindBy(xpath = "//button[contains(@class,'border-button')]")
	@CacheLookup
	WebElement downloadBtn;

	@FindBy(xpath = "//div[contains(text(),'Showing')]")
	@CacheLookup
	public WebElement showingJobResultsCount;

	// HELPER METHODs

	/**
	 * Enhanced click method with multiple fallback strategies Logs only the element
	 * clicked, not the technical strategy used
	 * 
	 * @param element     The WebElement to click
	 * @param elementName Name of the element for logging
	 */
	private void performEnhancedClick(WebElement element, String elementName) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(element)).click();
			PageObjectHelper.log(LOGGER, "Clicked on " + elementName);
		} catch (ElementNotInteractableException | TimeoutException e1) {
			try {
				js.executeScript("arguments[0].click();", element);
				PageObjectHelper.log(LOGGER, "Clicked on " + elementName);
			} catch (Exception e2) {
				try {
					utils.jsClick(driver, element);
					PageObjectHelper.log(LOGGER, "Clicked on " + elementName);
				} catch (Exception e3) {
					LOGGER.error("Click failed for: " + elementName);
					throw e3;
				}
			}
		}
	}

	/**
	 * PERFORMANCE OPTIMIZED: Wait for page to be ready and spinner to disappear
	 * 
	 * @param additionalWaitMs Additional wait time in milliseconds (now optimized
	 *                         with max limits)
	 */
	private void waitForPageReady(int additionalWaitMs) {
		try {
			// PERFORMANCE: Use ultra-fast wait instead of full DriverManager timeout
			WebDriverWait fastWait = new WebDriverWait(driver, Duration.ofSeconds(2)); // Max 2 seconds instead of 10+

			// ENHANCED: Quick early exit if page is already ready
			try {
				Boolean jsReady = (Boolean) js.executeScript("return document.readyState === 'complete'");
				if (Boolean.TRUE.equals(jsReady)) {
//					LOGGER.debug("... Page already ready - early exit from custom waitForPageReady");
					return; // Fast exit!
				}
			} catch (Exception e) {
				// Continue with spinner check
			}

			// OPTIMIZED: Quick spinner check with timeout protection
			try {
				List<WebElement> spinners = driver.findElements(By.xpath("//*[@class='blocking-loader']//img"));
				if (!spinners.isEmpty()) {
					// Check if spinner is actually visible before waiting
					boolean hasVisibleSpinner = spinners.stream().anyMatch(spinner -> {
						try {
							return spinner.isDisplayed();
						} catch (Exception e) {
							return false;
						}
					});

					if (hasVisibleSpinner) {
						LOGGER.debug("Waiting for spinner to disappear (max 2 seconds)");
						fastWait.until(ExpectedConditions.invisibilityOfAllElements(spinners));
					}
				}
			} catch (TimeoutException e) {
				// ACCEPTABLE: Spinner timeout after 2 seconds - continue anyway
				LOGGER.debug("Spinner timeout after 2s (acceptable) - page likely ready");
			} catch (Exception e) {
				// Spinner check failed - continue anyway
				LOGGER.debug("Spinner check failed: {} - continuing", e.getMessage());
			}

			// OPTIONAL: Minimal additional wait if specified (capped at 1 second max)
			if (additionalWaitMs > 0) {
				int cappedWait = Math.min(additionalWaitMs, 1000); // Cap at 1 second
				try {
					Thread.sleep(cappedWait);
					LOGGER.debug("Additional wait: {}ms (requested: {}ms)", cappedWait, additionalWaitMs);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					LOGGER.debug("Additional wait interrupted");
				}
			}

		} catch (Exception e) {
			// Handle any unexpected exceptions during page ready check
			LOGGER.debug("Wait for page ready completed with minor issue: {}", e.getMessage());
		}
	}

	/**
	 * PERFORMANCE OPTIMIZED: Overloaded method with minimal default wait (now
	 * capped at 1s max)
	 */
	private void waitForPageReady() {
		waitForPageReady(1000); // Reduced from 2000ms to 1000ms
	}

	/**
	 * Find element by row number and column with retry logic
	 * 
	 * @param rowNum Row number (1-based)
	 * @param colNum Column number (1-based)
	 * @return WebElement found
	 */
	private WebElement findTableElement(int rowNum, int colNum) {
		String xpath = String.format("//tbody//tr[%d]//td[%d]//*", rowNum, colNum);
		try {
			return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
		} catch (Exception e) {
			// Retry with additional wait and capture screenshot if still fails
			waitForPageReady(3000);
			try {
				return driver.findElement(By.xpath(xpath));
			} catch (Exception retryException) {
				// Capture screenshot for table element not found after retry
				LOGGER.error("Table element find retry failed - Method: findTableElement_retry", retryException);
				retryException.printStackTrace();
				throw retryException;
			}
		}
	}

	/**
	 * Scroll element into view smoothly
	 * 
	 * @param element Element to scroll to
	 */
	private void scrollToElement(WebElement element) {
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
		try {
			Thread.sleep(500); // Allow scroll animation to complete
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * DEPRECATED: Use PageObjectHelper.log() directly instead Log and add to extent
	 * report
	 * 
	 * @param message Message to log
	 */
	private void logAndReport(String message) {
		PageObjectHelper.log(LOGGER, message);
	}

	private boolean waitForPageNavigation(int timeoutSeconds) {
		try {
			WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));

			try {
				shortWait.until(
						ExpectedConditions.or(ExpectedConditions.visibilityOf(SPdetailsPageText), ExpectedConditions
								.presenceOfElementLocated(By.xpath("//span[contains(text(),'Select your view')]"))));

				String currentUrl = driver.getCurrentUrl();
				if (currentUrl.contains("/search")) {
					return false;
				}

				PerformanceUtils.waitForSpinnersToDisappear(driver, 3);
				return true;

			} catch (TimeoutException e) {
				String currentUrl = driver.getCurrentUrl();

				if (currentUrl.contains("/search")) {
					return false;
				}

				String urlPath = "";
				try {
					java.net.URL url = new java.net.URL(currentUrl);
					urlPath = url.getPath() + (url.getRef() != null ? "#" + url.getRef() : "");
				} catch (Exception urlEx) {
					urlPath = currentUrl;
				}

				boolean isDetailsPage = urlPath.contains("/sp/") || urlPath.contains("/success-profile/")
						|| urlPath.contains("/details/");

				if (isDetailsPage) {
					PerformanceUtils.waitForSpinnersToDisappear(driver, 3);
					return true;
				}
				return false;
			}

		} catch (Exception e) {
			return false;
		}
	}

	public void user_should_search_for_a_profile_with_export_status_as_not_exported() {
		try {
			waitForPageReady();

			for (int i = 1; i <= 1000; i++) {
				rowNumber.set(i);

				try {
					// Find export status element with simple retry logic
					WebElement exportStatus = findExportStatusElement(i);
					scrollToElement(exportStatus);

					String statusText = exportStatus.getText();
					if (statusText.contains("Not Exported")) {
						logAndReport("Job profile with export Status as Not Exported is found");

						// Check if profile can be exported (checkbox enabled)
						if (isProfileExportable(i)) {
							break; // Found valid profile
						} else {
							logAndReport(
									"But Success profile with No Job Code assigned cannot be Exported...So Searching for another Profile....");
						}
					}
				} catch (NoSuchElementException e) {
					LOGGER.debug("Row {} not accessible, continuing search", i);
					continue; // Skip this row and continue
				} catch (StaleElementReferenceException e) {
					LOGGER.warn("Stale element encountered at row {}, retrying once", i);
					// Capture screenshot for stale element in search
					LOGGER.error("Profile search stale element at row {} - Method: profile_search_stale_element", i, e);
					e.printStackTrace();
					waitForPageReady(1000);
					i--; // Retry the same row
					continue;
				}
			}

		} catch (TimeoutException e) {
			PageObjectHelper.handleError(LOGGER, "user_should_search_for_a_profile_with_export_status_as_not_exported",
					"Profile search in table", e);
		} catch (NoSuchElementException e) {
			PageObjectHelper.handleError(LOGGER, "user_should_search_for_a_profile_with_export_status_as_not_exported",
					"Table elements", e);
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_search_for_a_profile_with_export_status_as_not_exported",
					"Profile search operation", e);
		}
	}

	/**
	 * Helper method to find export status element with enhanced retry logic
	 */
	private WebElement findExportStatusElement(int rowNum) {
		String xpath = String.format("//tbody//tr[%d]//td[8]//span", rowNum);
		try {
			return driver.findElement(By.xpath(xpath));
		} catch (Exception e) {
			waitForPageReady(3000);
			return driver.findElement(By.xpath(xpath));
		}
	}

	/**
	 * Helper method to check if profile is exportable (checkbox enabled)
	 */
	private boolean isProfileExportable(int rowNum) {
		try {
			String checkboxXpath = String.format("//tbody//tr[%d]//td[1]//*//..//div//kf-checkbox//div", rowNum);
			WebElement checkbox = driver.findElement(By.xpath(checkboxXpath));
			scrollToElement(checkbox);
			String attributeValue = checkbox.getAttribute("class");
			return !attributeValue.contains("disable");
		} catch (Exception e) {
			return false;
		}
	}

	public void verify_details_of_the_not_exported_success_profile_in_hcm_sync_profiles_tab() {
		try {
			// Get all profile details using helper method
			WebElement jobNameElement = findTableElement(rowNumber.get(), 1);
			scrollToElement(jobNameElement);
			SPJobName.set(jobNameElement.getText());

			// Build profile details string
			StringBuilder profileDetails = buildProfileDetailsString();

			// Log the details
			String message = "Below are the details of the Not Exported Success Profile in HCM Sync Profiles screen in PM : \n"
					+ profileDetails.toString();
			logAndReport(message);

		} catch (TimeoutException e) {
			PageObjectHelper.handleError(LOGGER,
					"verify_details_of_the_not_exported_success_profile_in_hcm_sync_profiles_tab",
					"Profile details verification", e);
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"verify_details_of_the_not_exported_success_profile_in_hcm_sync_profiles_tab",
					"Profile details operation", e);
		}
	}

	/**
	 * Helper method to build profile details string
	 */
	private StringBuilder buildProfileDetailsString() {
		StringBuilder details = new StringBuilder();

		// Array of table headers and corresponding column numbers
		WebElement[] headers = { tableHeader1, tableHeader2, tableHeader3, tableHeader4, tableHeader5, tableHeader6,
				tableHeader7, tableHeader8 };

		for (int col = 1; col <= 8; col++) {
			WebElement dataElement = findTableElement(rowNumber.get(), col);
			String headerText = headers[col - 1].getText().replaceAll("\\s+[^\\w\\s]+$", "");
			details.append(headerText).append(" : ").append(dataElement.getText()).append("   ");
		}

		return details;
	}

	public void verify_success_profile_checkbox_is_enabled_and_able_to_perform_export_operation() {
		try {
			String checkboxXpath = String.format("//tbody//tr[%d]//td[1]//*//..//div//kf-checkbox", rowNumber.get());
			WebElement checkbox = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(checkboxXpath)));
			scrollToElement(checkbox);

			// Verify checkbox is displayed and enabled
			if (!checkbox.isDisplayed()) {
				throw new AssertionError("Checkbox is not displayed for profile: " + SPJobName.get());
			}

			if (!checkbox.isEnabled()) {
				throw new AssertionError(
						"Checkbox is disabled for profile: " + SPJobName.get() + ". This profile cannot be exported.");
			}

			// Verify clickability
			Assert.assertTrue(wait.until(ExpectedConditions.elementToBeClickable(checkbox)).isEnabled());

			logAndReport("Checkbox of the SP with Name " + SPJobName.get()
					+ " is Enabled and able to Perform Export Operation");

		} catch (TimeoutException e) {
			PageObjectHelper.handleError(LOGGER,
					"verify_success_profile_checkbox_is_enabled_and_able_to_perform_export_operation",
					"Checkbox element for profile: " + SPJobName, e);
		} catch (AssertionError e) {
			String errorMsg = "Checkbox verification for profile: " + SPJobName;
			LOGGER.error(errorMsg
					+ " - Method: verify_success_profile_checkbox_is_enabled_and_able_to_perform_export_operation", e);
			throw new RuntimeException(errorMsg, e);
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"verify_success_profile_checkbox_is_enabled_and_able_to_perform_export_operation",
					"Checkbox operation for profile: " + SPJobName, e);
		}
	}

	public void click_on_checkbox_of_success_profile_with_export_status_as_not_exported() {
		try {
			String checkboxXpath = String.format("//tbody//tr[%d]//td[1]//*//..//div//kf-checkbox", rowNumber.get());
			WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(checkboxXpath)));

			// Simple validation before clicking
			if (!checkbox.isEnabled()) {
				LOGGER.warn(" Checkbox is disabled for profile: {}", SPJobName.get());
				throw new IllegalStateException("Checkbox is disabled for profile: " + SPJobName.get());
			}

			performEnhancedClick(checkbox, "checkbox of Success profile with name: " + SPJobName.get());
			PO22_ValidateHCMSyncProfilesScreen_PM.profilesCount.set(1);

		} catch (TimeoutException e) {
			PageObjectHelper.handleWithContext(
					"click_on_checkbox_of_success_profile_with_export_status_as_not_exported", e,
					"Checkbox for profile: " + SPJobName);
		} catch (ElementNotInteractableException e) {
			PageObjectHelper.handleWithContext(
					"click_on_checkbox_of_success_profile_with_export_status_as_not_exported", e,
					"Checkbox interaction for profile: " + SPJobName);
		} catch (StaleElementReferenceException e) {
			PageObjectHelper.handleWithContext(
					"click_on_checkbox_of_success_profile_with_export_status_as_not_exported", e,
					"Stale checkbox element for profile: " + SPJobName);
		} catch (Exception e) {
			PageObjectHelper.handleWithContext(
					"click_on_checkbox_of_success_profile_with_export_status_as_not_exported", e,
					"Checkbox operation for profile: " + SPJobName);
		}
	}

	public void refresh_hcm_sync_profiles_tab() {
		try {
			driver.navigate().refresh();
			waitForPageReady();
			logAndReport("HCM Sync Profiles screen page is Refreshed....");
		} catch (Exception e) {
			PageObjectHelper.handleWithContext("refresh_hcm_sync_profiles_tab", e, "Page refresh operation");
		}
	}

	public void user_should_verify_export_status_of_sp_updated_as_exported() {
		try {
			waitForPageReady();

			// Scroll through rows to ensure table is loaded
			scrollThroughTableRows(rowNumber.get() + 2);

			// Verify job name and export status
			WebElement jobNameElement = findTableElement(rowNumber.get(), 1);
			scrollToElement(jobNameElement);

			String actualJobName = jobNameElement.getText();
			if (!actualJobName.equals(SPJobName.get())) {
				LOGGER.warn("Job name mismatch at row {}. Expected: '{}', Actual: '{}'", rowNumber.get(),
						SPJobName.get(), actualJobName);
			}
			SPJobName.set(actualJobName);

			WebElement exportStatusElement = findTableElement(rowNumber.get(), 8);
			String actualStatus = exportStatusElement.getText();

			if (!"Exported".equals(actualStatus)) {
				throw new AssertionError(String.format(
						"Export status verification failed for profile '%s' at row %d. Expected: 'Exported', Actual: '%s'",
						SPJobName.get(), rowNumber.get(), actualStatus));
			}

			logAndReport("Export Status of SP with name " + SPJobName.get() + " updated as " + actualStatus
					+ " as expected");

		} catch (TimeoutException e) {
			PageObjectHelper.handleWithContext("user_should_verify_export_status_of_sp_updated_as_exported", e,
					"Table elements for profile: " + SPJobName.get());
		} catch (NoSuchElementException e) {
			PageObjectHelper.handleWithContext("user_should_verify_export_status_of_sp_updated_as_exported", e,
					"Table row " + rowNumber.get());
		} catch (AssertionError e) {
			PageObjectHelper.handleWithContext("user_should_verify_export_status_of_sp_updated_as_exported", e,
					"Export status assertion for profile: " + SPJobName.get());
		} catch (Exception e) {
			PageObjectHelper.handleWithContext("user_should_verify_export_status_of_sp_updated_as_exported", e,
					"Export status verification for profile: " + SPJobName.get());
		}
	}

	/**
	 * Scroll through table rows to ensure they are loaded
	 */
	private void scrollThroughTableRows(int maxRows) {
		int lastSuccessfulRow = 0;
		int scrollAttempts = 0;
		int maxScrollAttempts = 10;

		for (int j = 1; j <= maxRows; j++) {
			try {
				String xpath = String.format("//tbody//tr[%d]//td[1]//*", j);
				WebElement rowElement = driver.findElement(By.xpath(xpath));
				scrollToElement(rowElement);
				lastSuccessfulRow = j;
				scrollAttempts = 0;

			} catch (NoSuchElementException e) {
				if (lastSuccessfulRow > 0) {
					try {
						String lastRowXpath = String.format("//tbody//tr[%d]//td[1]//*", lastSuccessfulRow);
						WebElement lastRow = driver.findElement(By.xpath(lastRowXpath));

						js.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'end'});", lastRow);
						PerformanceUtils.waitForUIStability(driver, 1);
						js.executeScript("window.scrollBy(0, 500);");
						PerformanceUtils.waitForUIStability(driver, 1);

						try {
							String xpath = String.format("//tbody//tr[%d]//td[1]//*", j);
							WebElement rowElement = driver.findElement(By.xpath(xpath));
							scrollToElement(rowElement);
							lastSuccessfulRow = j;
							scrollAttempts = 0;
						} catch (NoSuchElementException retryEx) {
							scrollAttempts++;
							if (scrollAttempts >= maxScrollAttempts) {
								break;
							}
							j--;
						}
					} catch (Exception scrollEx) {
						scrollAttempts++;
						if (scrollAttempts >= maxScrollAttempts) {
							break;
						}
						j--;
					}
				} else {
					break;
				}
			} catch (Exception e) {
				LOGGER.debug("Error accessing row {}: {}", j, e.getMessage());
			}
		}
	}

	public void user_should_click_on_recently_exported_success_profile_job_name() {
		try {
			// Wait for page to be ready
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

			// Enhanced scrolling logic with better element targeting
			if (rowNumber.get() == 1) {
				js.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});",
						showingJobResultsCount);
			} else if (rowNumber.get() < 5) {
				WebElement SP_JobName = wait
						.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tbody//tr[1]//td[1]//*")));
				js.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});", SP_JobName);
			} else if (rowNumber.get() > 5) {
				WebElement SP_JobName = wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//tbody//tr[" + Integer.toString(rowNumber.get() - 5) + "]//td[1]//*")));
				js.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});", SP_JobName);
			}

			PerformanceUtils.waitForUIStability(driver, 1);

			// Enhanced element location with multiple fallback strategies
			WebElement E_SP_JobName = null;
			String primaryXpath = "//tbody//tr[" + Integer.toString(rowNumber.get()) + "]//td[1]//*";
			String fallbackXpath = "//tbody//tr[" + Integer.toString(rowNumber.get()) + "]//td[1]//a";
			String fallbackXpath2 = "//tbody//tr[" + Integer.toString(rowNumber.get()) + "]//td[1]//span";

			try {
				E_SP_JobName = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(primaryXpath)));
			} catch (Exception e1) {
				try {
					E_SP_JobName = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(fallbackXpath)));
				} catch (Exception e2) {
					E_SP_JobName = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(fallbackXpath2)));
				}
			}

			// Verify element text matches expected
			Assert.assertEquals(E_SP_JobName.getText(), SPJobName.get());

			// Scroll element into view and ensure it's ready
			js.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});", E_SP_JobName);
			PerformanceUtils.waitForSpinnersToDisappear(driver, 5);

			// Wait for element to be clickable with shorter timeout
			WebDriverWait clickWait = new WebDriverWait(driver, Duration.ofSeconds(10));
			clickWait.until(ExpectedConditions.elementToBeClickable(E_SP_JobName));

			// Try multiple click strategies
			boolean navigationSuccessful = false;

			// Strategy 1: Double-click with Actions
			if (!navigationSuccessful) {
				try {
					org.openqa.selenium.interactions.Actions actions = new org.openqa.selenium.interactions.Actions(
							driver);
					actions.moveToElement(E_SP_JobName).pause(java.time.Duration.ofMillis(200)).doubleClick()
							.pause(java.time.Duration.ofMillis(300)).build().perform();

					if (waitForPageNavigation(5)) {
						navigationSuccessful = true;
					}
				} catch (Exception e) {
					LOGGER.debug("Strategy 1 failed: {}", e.getMessage());
				}
			}

			// Strategy 2: Double-click on direct anchor element
			if (!navigationSuccessful) {
				try {
					WebElement directLink = E_SP_JobName;
					String tagName = E_SP_JobName.getTagName().toLowerCase();

					if (!tagName.equals("a")) {
						try {
							directLink = E_SP_JobName.findElement(By.xpath(".//a"));
						} catch (NoSuchElementException nse) {
							// Use original element
						}
					}

					org.openqa.selenium.interactions.Actions actions = new org.openqa.selenium.interactions.Actions(
							driver);
					actions.moveToElement(directLink).pause(java.time.Duration.ofMillis(200)).doubleClick()
							.pause(java.time.Duration.ofMillis(300)).build().perform();

					if (waitForPageNavigation(5)) {
						navigationSuccessful = true;
					}
				} catch (Exception e) {
					LOGGER.debug("Strategy 2 failed: {}", e.getMessage());
				}
			}

			// Strategy 3: JavaScript double-click event
			if (!navigationSuccessful) {
				try {
					js.executeScript("var evt = new MouseEvent('dblclick', {" + "  bubbles: true,"
							+ "  cancelable: true," + "  view: window" + "});" + "arguments[0].dispatchEvent(evt);",
							E_SP_JobName);

					if (waitForPageNavigation(5)) {
						navigationSuccessful = true;
					}
				} catch (Exception e) {
					LOGGER.debug("Strategy 3 failed: {}", e.getMessage());
				}
			}

			// Strategy 4: JavaScript sequential clicks
			if (!navigationSuccessful) {
				try {
					js.executeScript(
							"function simulateDoubleClick(element) {" + "  var clickEvent = new MouseEvent('click', {"
									+ "    bubbles: true, cancelable: true, view: window, detail: 1" + "  });"
									+ "  element.dispatchEvent(clickEvent);" + "  setTimeout(function() {"
									+ "    var secondClick = new MouseEvent('click', {"
									+ "      bubbles: true, cancelable: true, view: window, detail: 2" + "    });"
									+ "    element.dispatchEvent(secondClick);" + "  }, 50);" + "}"
									+ "simulateDoubleClick(arguments[0]);",
							E_SP_JobName);

					Thread.sleep(300);

					if (waitForPageNavigation(5)) {
						navigationSuccessful = true;
					}
				} catch (Exception e) {
					LOGGER.debug("Strategy 4 failed: {}", e.getMessage());
				}
			}

			// Strategy 5: Standard single click
			if (!navigationSuccessful) {
				try {
					E_SP_JobName.click();
					if (waitForPageNavigation(5)) {
						navigationSuccessful = true;
					}
				} catch (Exception e) {
					LOGGER.debug("Strategy 5 failed: {}", e.getMessage());
				}
			}

			// Strategy 6: JavaScript single click
			if (!navigationSuccessful) {
				try {
					js.executeScript("arguments[0].click();", E_SP_JobName);
					if (waitForPageNavigation(5)) {
						navigationSuccessful = true;
					}
				} catch (Exception e) {
					LOGGER.debug("Strategy 6 failed: {}", e.getMessage());
				}
			}

			// Final validation
			if (!navigationSuccessful) {
				LOGGER.error("Navigation failed for profile: {} (row {})", SPJobName.get(), rowNumber.get());
				throw new Exception("Failed to navigate to Success Profile details page for: " + SPJobName.get());
			}

			PageObjectHelper.log(LOGGER, "Clicked on Recently Exported Success Profile with name: " + SPJobName.get());
			PerformanceUtils.waitForSpinnersToDisappear(driver, 5);

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			PageObjectHelper.handleWithContext("user_should_click_on_recently_exported_success_profile_job_name", e,
					"Click operation interrupted");
		} catch (Exception e) {
			PageObjectHelper.handleWithContext("user_should_click_on_recently_exported_success_profile_job_name", e,
					"Click operation failed");
		}
	}

	public void user_should_be_navigated_to_sp_details_page() {
		try {
			waitForPageReady();

			try {
				Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(SPdetailsPageText)).isDisplayed());
			} catch (TimeoutException e) {
				String currentUrl = driver.getCurrentUrl();

				String urlPath = "";
				try {
					java.net.URL url = new java.net.URL(currentUrl);
					urlPath = url.getPath() + (url.getRef() != null ? "#" + url.getRef() : "");
				} catch (Exception urlEx) {
					urlPath = currentUrl;
				}

				boolean isDetailsPage = urlPath.contains("/sp/") || urlPath.contains("/success-profile/")
						|| urlPath.contains("details") || (urlPath.contains("profile") && !urlPath.contains("/search"));

				if (!isDetailsPage || urlPath.contains("/search")) {
					throw new AssertionError("Navigation failed - not on SP details page. URL: " + currentUrl);
				}
			}

			logAndReport(
					"SP details page opens as expected on click of Recently Exported Success Profile Job name in HCM Sync Profiles screen in PM....");

		} catch (Exception e) {
			PageObjectHelper.handleWithContext("user_should_be_navigated_to_sp_details_page", e,
					"SP details page verification");
		}
	}

	public void click_on_three_dots_in_sp_details_page() {
		try {
			waitForPageReady();
			performEnhancedClick(threeDotsinSPdetailsPage, "Three Dots in SP Details Page");
			waitForPageReady(0); // Wait for spinner only
		} catch (Exception e) {
			PageObjectHelper.handleWithContext("click_on_three_dots_in_sp_details_page", e, "Three dots menu");
		}
	}

	public void click_on_edit_success_profile_option() {
		try {
			waitForPageReady();
			performEnhancedClick(editSPbuttoninSPdetailsPage, "Edit Success Profile option in SP Details Page");
			waitForPageReady(0);
		} catch (Exception e) {
			PageObjectHelper.handleWithContext("click_on_edit_success_profile_option", e,
					"Edit Success Profile option");
		}
	}

	public void click_on_edit_button_of_details_section() {
		try {
			waitForPageReady();
			performEnhancedClick(editDetailsBtn, "Edit button of Success Profile Details section");
			waitForPageReady(0);
		} catch (Exception e) {
			PageObjectHelper.handleWithContext("click_on_edit_button_of_details_section", e,
					"Edit button of Details section");
		}
	}

	public void modify_function_and_sub_function_values_of_the_success_profile() {
		try {
			waitForPageReady();

			// DYNAMIC FIX: Select first available Function option from dropdown
			performEnhancedClick(FunctionDropdownofSP, "Function dropdown in Success Profile Details section");

			// Wait for dropdown options to load and select first available option
			PerformanceUtils.waitForUIStability(driver, 1);
			List<WebElement> functionOptions = wait.until(
					ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//kf-dropdown-item//div//span")));

			if (functionOptions.isEmpty()) {
				throw new AssertionError("No Function options available in dropdown");
			}

			// Get first visible and enabled option
			WebElement selectedFunctionOption = null;
			String functionValue = null;
			for (WebElement option : functionOptions) {
				if (option.isDisplayed() && option.isEnabled()) {
					functionValue = option.getText().trim();
					selectedFunctionOption = option;
					break;
				}
			}

			if (selectedFunctionOption == null || functionValue == null || functionValue.isEmpty()) {
				throw new AssertionError("No valid Function option found to select");
			}

			performEnhancedClick(selectedFunctionOption, "Function value: " + functionValue);
			// Only log the business action, not the click (already logged by
			// performEnhancedClick)
			logAndReport("Function value Modified to '" + functionValue + "' in Success Profile Details section");

			PerformanceUtils.waitForUIStability(driver, 2);

			// DYNAMIC FIX: Select first available Sub-Function option from dropdown
			performEnhancedClick(SubFunctionDropdownofSP, "Subfunction dropdown in Success Profile Details section");

			// Wait for dropdown options to load and select first available option
			PerformanceUtils.waitForUIStability(driver, 1);
			List<WebElement> subFunctionOptions = wait.until(
					ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//kf-dropdown-item//div//span")));

			if (subFunctionOptions.isEmpty()) {
				throw new AssertionError("No Sub-Function options available in dropdown");
			}

			// Get first visible and enabled option
			WebElement selectedSubFunctionOption = null;
			String subFunctionValue = null;
			for (WebElement option : subFunctionOptions) {
				if (option.isDisplayed() && option.isEnabled()) {
					subFunctionValue = option.getText().trim();
					selectedSubFunctionOption = option;
					break;
				}
			}

			if (selectedSubFunctionOption == null || subFunctionValue == null || subFunctionValue.isEmpty()) {
				throw new AssertionError("No valid Sub-Function option found to select");
			}

			performEnhancedClick(selectedSubFunctionOption, "Sub-Function value: " + subFunctionValue);
			// Only log the business action, not the click (already logged by
			// performEnhancedClick)
			logAndReport(
					"Sub-Function value Modified to '" + subFunctionValue + "' in Success Profile Details section");

		} catch (TimeoutException e) {
			PageObjectHelper.handleWithContext("modify_function_and_sub_function_values_of_the_success_profile", e,
					"Function/SubFunction dropdown operations");
		} catch (Exception e) {
			PageObjectHelper.handleWithContext("modify_function_and_sub_function_values_of_the_success_profile", e,
					"Function/SubFunction modification");
		}
	}

	public void click_on_done_button() {
		try {
			PerformanceUtils.waitForPageReady(driver, 2);
			performEnhancedClick(DoneBtnofSP, "Done button of Success Profile Details section");
			waitForPageReady();
		} catch (Exception e) {
			PageObjectHelper.handleWithContext("click_on_done_button", e, "Done button operation");
		}
	}

	public void click_on_save_button() {
		try {
			waitForPageReady();
			performEnhancedClick(SaveBtnofSP, "Save button of Success Profile");
			waitForPageReady();
		} catch (Exception e) {
			PageObjectHelper.handleWithContext("click_on_save_button", e, "Save button operation");
		}
	}

	public void user_should_be_navigated_to_sp_details_page_after_saving_sp_details() {
		try {
			waitForPageReady();
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(SPdetailsPageText)).isDisplayed());
			logAndReport("Navigated to SP details page as expected after Saving SP details....");
		} catch (TimeoutException e) {
			PageObjectHelper.handleWithContext("user_should_be_navigated_to_sp_details_page_after_saving_sp_details",
					e, "SP details page navigation after save");
		} catch (AssertionError e) {
			PageObjectHelper.handleWithContext("user_should_be_navigated_to_sp_details_page_after_saving_sp_details",
					e, "SP details page assertion after save");
		} catch (Exception e) {
			PageObjectHelper.handleWithContext("user_should_be_navigated_to_sp_details_page_after_saving_sp_details",
					e, "SP details page verification after save");
		}
	}

	public void user_should_verify_recently_modified_success_profile_is_displaying_on_top_of_the_job_proifles_list() {
		try {
			waitForPageReady();
			WebElement topJobName = findTableElement(1, 1);
			Assert.assertEquals(topJobName.getText(), SPJobName.get());
			logAndReport("Recently Modified SP with name " + topJobName.getText()
					+ " is displaying on the Top of the Profiles List as Expected in HCM Sync Profiles screen");
		} catch (TimeoutException e) {
			PageObjectHelper.handleWithContext(
					"user_should_verify_recently_modified_success_profile_is_displaying_on_top_of_the_job_proifles_list",
					e, "Top profile verification");
		} catch (AssertionError e) {
			PageObjectHelper.handleWithContext(
					"user_should_verify_recently_modified_success_profile_is_displaying_on_top_of_the_job_proifles_list",
					e, "Profile position assertion");
		} catch (Exception e) {
			PageObjectHelper.handleWithContext(
					"user_should_verify_recently_modified_success_profile_is_displaying_on_top_of_the_job_proifles_list",
					e, "Profile list verification");
		}
	}

	public void user_should_verify_recently_exported_and_modified_success_profile_export_status_updated_as_exported_modfied() {
		try {
			waitForPageReady();
			WebElement topJobName = findTableElement(1, 1);
			Assert.assertEquals(topJobName.getText(), SPJobName.get());
			SPJobName.set(topJobName.getText());

			WebElement exportStatus = findTableElement(1, 8);
			Assert.assertEquals(exportStatus.getText(), "Exported - Modified");
			logAndReport("Export Status of recently Exported and Modfied SP with name " + SPJobName.get()
					+ " updated as " + exportStatus.getText() + " as expected...");
		} catch (TimeoutException e) {
			PageObjectHelper.handleWithContext(
					"user_should_verify_recently_exported_and_modified_success_profile_export_status_updated_as_exported_modfied",
					e, "Export status verification after modification");
		} catch (AssertionError e) {
			PageObjectHelper.handleWithContext(
					"user_should_verify_recently_exported_and_modified_success_profile_export_status_updated_as_exported_modfied",
					e, "Export status assertion for modified profile");
		} catch (Exception e) {
			PageObjectHelper.handleWithContext(
					"user_should_verify_recently_exported_and_modified_success_profile_export_status_updated_as_exported_modfied",
					e, "Modified profile export status verification");
		}
	}

}
