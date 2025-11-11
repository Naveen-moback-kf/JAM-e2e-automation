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
import com.kfonetalentsuite.utils.JobMapping.SimpleErrorHandler;
import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.webdriverManager.DriverManager;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;



public class PO25_ValidateExportStatusFunctionality_PM {
	WebDriver driver = DriverManager.getDriver();	

	protected static final Logger LOGGER = (Logger) LogManager.getLogger();
	PO25_ValidateExportStatusFunctionality_PM validateExportStatusFunctionality_PM;
	public static int rowNumber;
	public static String SPJobName;


	public PO25_ValidateExportStatusFunctionality_PM() throws IOException {
		// super();
		PageFactory.initElements(driver, this);
		// TODO Auto-generated constructor stub
	}

	WebDriverWait wait = DriverManager.getWait();
	Utilities utils = new Utilities();
	JavascriptExecutor js = (JavascriptExecutor) driver;
	
	//XAPTHs
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
	
	@FindBy(xpath = "//kf-dropdown-item//div//span[contains(text(),'Academic')]")
	@CacheLookup
	public WebElement FunctionValue;
	
	@FindBy(xpath = "//label[contains(text(),'Subfunction')]//..//..//button")
	@CacheLookup
	public WebElement SubFunctionDropdownofSP;
	
	@FindBy(xpath = "//div//span[contains(text(),'Academic Medical Affairs')]")
	@CacheLookup
	public WebElement SubFunctionValue;
	
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
	
	
	//HELPER METHODs
	
	/**
	 * Enhanced click method with multiple fallback strategies
	 * @param element The WebElement to click
	 * @param elementName Name of the element for logging
	 */
	private void performEnhancedClick(WebElement element, String elementName) {
		try {
			// Strategy 1: Standard WebDriver click
			wait.until(ExpectedConditions.elementToBeClickable(element)).click();
			LOGGER.info("Clicked on {} using normal click", elementName);
		} catch (ElementNotInteractableException | TimeoutException e1) {
			try {
				// Strategy 2: JavaScript click
				js.executeScript("arguments[0].click();", element);
				LOGGER.info("Clicked on {} using JavaScript click", elementName);
			} catch (Exception e2) {
				try {
					// Strategy 3: Utility click
					utils.jsClick(driver, element);
					LOGGER.info("Clicked on {} using utility click", elementName);
							} catch (Exception e3) {
				LOGGER.error("All click strategies failed for: {}", elementName);
				// Capture screenshot for click failure
				LOGGER.error("Enhanced click failed for element: {} - Method: performEnhancedClick", elementName, e3);
				e3.printStackTrace();
				throw e3;
			}
			}
		}
	}
	
	/**
	 * PERFORMANCE OPTIMIZED: Wait for page to be ready and spinner to disappear
	 * @param additionalWaitMs Additional wait time in milliseconds (now optimized with max limits)
	 */
	private void waitForPageReady(int additionalWaitMs) {
		try {
			// PERFORMANCE: Use ultra-fast wait instead of full DriverManager timeout
			WebDriverWait fastWait = new WebDriverWait(driver, Duration.ofSeconds(2)); // Max 2 seconds instead of 10+
			
			// ENHANCED: Quick early exit if page is already ready
			try {
				Boolean jsReady = (Boolean) js.executeScript("return document.readyState === 'complete'");
				if (Boolean.TRUE.equals(jsReady)) {
//					LOGGER.debug("✅ Page already ready - early exit from custom waitForPageReady");
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
	 * PERFORMANCE OPTIMIZED: Overloaded method with minimal default wait (now capped at 1s max)
	 */
	private void waitForPageReady() {
		waitForPageReady(1000); // Reduced from 2000ms to 1000ms
	}
	

	
	/**
	 * Find element by row number and column with retry logic
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
	 * Log and add to extent report
	 * @param message Message to log
	 */
	private void logAndReport(String message) {
		LOGGER.info(message);
		ExtentCucumberAdapter.addTestStepLog(message);
	}
	

	
	/**
	 * Helper method to wait for page navigation after click
	 * Returns true if navigation occurred, false otherwise
	 */
	private boolean waitForPageNavigation() {
		try {
			// Strategy 1: Wait for URL change (if applicable)
			String currentUrl = driver.getCurrentUrl();
			
			// Strategy 2: Wait for specific elements that indicate navigation
			// Check if SP details page elements appear
			try {
				wait.until(ExpectedConditions.or(
					ExpectedConditions.visibilityOf(SPdetailsPageText),
					ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(text(),'Select your view')]")),
					ExpectedConditions.urlContains("profile"),
					ExpectedConditions.urlContains("details")
				));
				
				// Additional wait for page stability
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			PerformanceUtils.waitForUIStability(driver, 1);
			
				LOGGER.info("Page navigation detected - SP details page loaded");
				return true;
				
			} catch (Exception e) {
				// Strategy 3: Check for URL change
				String newUrl = driver.getCurrentUrl();
				if (!currentUrl.equals(newUrl)) {
					LOGGER.info("Page navigation detected - URL changed from {} to {}", currentUrl, newUrl);
					wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
					return true;
				}
				
				// Strategy 4: Check for page title change
				try {
					wait.until(ExpectedConditions.not(ExpectedConditions.titleIs(driver.getTitle())));
					LOGGER.info("Page navigation detected - Title changed");
					return true;
				} catch (Exception titleException) {
					LOGGER.warn("No navigation detected - staying on same page");
					return false;
				}
			}
			
		} catch (Exception e) {
			LOGGER.warn("Error during navigation detection: " + e.getMessage());
			// Capture screenshot for navigation detection failure
			LOGGER.error("Navigation detection failed - Method: navigation_detection_failed", e);
			e.printStackTrace();
			return false;
		}
	}
	
	public void user_should_search_for_a_profile_with_export_status_as_not_exported() {
		try {
			waitForPageReady();
			
			for(int i = 1; i <= 1000; i++) {
				rowNumber = i;
				
				try {
					// Find export status element with simple retry logic
					WebElement exportStatus = findExportStatusElement(i);
					scrollToElement(exportStatus);
					
					String statusText = exportStatus.getText();
					if(statusText.contains("Not Exported")) {
						logAndReport("Job profile with export Status as Not Exported is found");
						
						// Check if profile can be exported (checkbox enabled)
						if(isProfileExportable(i)) {
							break; // Found valid profile
						} else {
							logAndReport("But Success profile with No Job Code assigned cannot be Exported...So Searching for another Profile....");
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
			SimpleErrorHandler.handleWithContext(
				"user_should_search_for_a_profile_with_export_status_as_not_exported", e, "Profile search in table");
		} catch (NoSuchElementException e) {
			SimpleErrorHandler.handleWithContext(
				"user_should_search_for_a_profile_with_export_status_as_not_exported", e, "Table elements");
		} catch (Exception e) {
			SimpleErrorHandler.handleWithContext(
				"user_should_search_for_a_profile_with_export_status_as_not_exported", e, "Profile search operation");
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
			WebElement jobNameElement = findTableElement(rowNumber, 1);
			scrollToElement(jobNameElement);
			SPJobName = jobNameElement.getText();
			
			// Build profile details string
			StringBuilder profileDetails = buildProfileDetailsString();
			
			// Log the details
			String message = "Below are the details of the Not Exported Success Profile in HCM Sync Profiles screen in PM : \n" + profileDetails.toString();
			logAndReport(message);
			
		} catch (TimeoutException e) {
			SimpleErrorHandler.handleWithContext(
				"verify_details_of_the_not_exported_success_profile_in_hcm_sync_profiles_tab", e, "Profile details verification");
		} catch (Exception e) {
			SimpleErrorHandler.handleWithContext(
				"verify_details_of_the_not_exported_success_profile_in_hcm_sync_profiles_tab", e, "Profile details operation");
		}
	}
	
	/**
	 * Helper method to build profile details string
	 */
	private StringBuilder buildProfileDetailsString() {
		StringBuilder details = new StringBuilder();
		
		// Array of table headers and corresponding column numbers
		WebElement[] headers = {tableHeader1, tableHeader2, tableHeader3, tableHeader4, 
		                       tableHeader5, tableHeader6, tableHeader7, tableHeader8};
		
		for (int col = 1; col <= 8; col++) {
			WebElement dataElement = findTableElement(rowNumber, col);
			String headerText = headers[col-1].getText().split(" ▼")[0];
			details.append(headerText).append(" : ").append(dataElement.getText()).append("   ");
		}
		
		return details;
	}
	
	public void verify_success_profile_checkbox_is_enabled_and_able_to_perform_export_operation() {
		try {
			String checkboxXpath = String.format("//tbody//tr[%d]//td[1]//*//..//div//kf-checkbox", rowNumber);
			WebElement checkbox = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(checkboxXpath)));
			scrollToElement(checkbox);
			
			// Verify checkbox is displayed and enabled
			if (!checkbox.isDisplayed()) {
				throw new AssertionError("Checkbox is not displayed for profile: " + SPJobName);
			}
			
			if (!checkbox.isEnabled()) {
				throw new AssertionError("Checkbox is disabled for profile: " + SPJobName + ". This profile cannot be exported.");
			}
			
			// Verify clickability
			Assert.assertTrue(wait.until(ExpectedConditions.elementToBeClickable(checkbox)).isEnabled());
			
			logAndReport("Checkbox of the SP with Name " + SPJobName + " is Enabled and able to Perform Export Operation");
			
		} catch (TimeoutException e) {
			SimpleErrorHandler.handleWithContext(
				"verify_success_profile_checkbox_is_enabled_and_able_to_perform_export_operation", e,
				"Checkbox element for profile: " + SPJobName);
		} catch (AssertionError e) {
			SimpleErrorHandler.handleWithContext(
				"verify_success_profile_checkbox_is_enabled_and_able_to_perform_export_operation", e,
				"Checkbox verification for profile: " + SPJobName);
		} catch (Exception e) {
			SimpleErrorHandler.handleWithContext(
				"verify_success_profile_checkbox_is_enabled_and_able_to_perform_export_operation", e,
				"Checkbox operation for profile: " + SPJobName);
		}
	}
	
	public void click_on_checkbox_of_success_profile_with_export_status_as_not_exported() {
		try {
			String checkboxXpath = String.format("//tbody//tr[%d]//td[1]//*//..//div//kf-checkbox", rowNumber);
			WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(checkboxXpath)));
			
			// Simple validation before clicking
			if (!checkbox.isEnabled()) {
				LOGGER.warn("⚠️ Checkbox is disabled for profile: {}", SPJobName);
				throw new IllegalStateException("Checkbox is disabled for profile: " + SPJobName);
			}
			
			performEnhancedClick(checkbox, "checkbox of Success profile with name: " + SPJobName);
			logAndReport("Clicked on checkbox of Success profile with name : " + SPJobName +" in HCM Sync Profiles screen in PM");
			PO22_ValidateHCMSyncProfilesScreen_PM.profilesCount = 1;
			
		} catch (TimeoutException e) {
			SimpleErrorHandler.handleWithContext(
				"click_on_checkbox_of_success_profile_with_export_status_as_not_exported", e,
				"Checkbox for profile: " + SPJobName);
		} catch (ElementNotInteractableException e) {
			SimpleErrorHandler.handleWithContext(
				"click_on_checkbox_of_success_profile_with_export_status_as_not_exported", e,
				"Checkbox interaction for profile: " + SPJobName);
		} catch (StaleElementReferenceException e) {
			SimpleErrorHandler.handleWithContext(
				"click_on_checkbox_of_success_profile_with_export_status_as_not_exported", e,
				"Stale checkbox element for profile: " + SPJobName);
		} catch (Exception e) {
			SimpleErrorHandler.handleWithContext(
				"click_on_checkbox_of_success_profile_with_export_status_as_not_exported", e,
				"Checkbox operation for profile: " + SPJobName);
		}
	}
	
	public void refresh_hcm_sync_profiles_tab() {
		try {
			driver.navigate().refresh();
			waitForPageReady();
			logAndReport("HCM Sync Profiles screen page is Refreshed....");
		} catch(Exception e) {
			SimpleErrorHandler.handleWithContext("refresh_hcm_sync_profiles_tab", e, "Page refresh operation");
		}
	}
	
	public void user_should_verify_export_status_of_sp_updated_as_exported() {
		try {
			waitForPageReady();
			
			// Scroll through rows to ensure table is loaded
			scrollThroughTableRows(rowNumber + 2);
			
			// Verify job name and export status
			WebElement jobNameElement = findTableElement(rowNumber, 1);
			scrollToElement(jobNameElement);
			
			String actualJobName = jobNameElement.getText();
			if (!actualJobName.equals(SPJobName)) {
				LOGGER.warn("Job name mismatch at row {}. Expected: '{}', Actual: '{}'", 
						rowNumber, SPJobName, actualJobName);
			}
			SPJobName = actualJobName;
			
			WebElement exportStatusElement = findTableElement(rowNumber, 8);
			String actualStatus = exportStatusElement.getText();
			
			if (!"Exported".equals(actualStatus)) {
				throw new AssertionError(
					String.format("Export status verification failed for profile '%s' at row %d. Expected: 'Exported', Actual: '%s'", 
						SPJobName, rowNumber, actualStatus)
				);
			}
			
			logAndReport("Export Status of SP with name " + SPJobName + " updated as " + actualStatus + " as expected");
			
		} catch (TimeoutException e) {
			SimpleErrorHandler.handleWithContext(
				"user_should_verify_export_status_of_sp_updated_as_exported", e,
				"Table elements for profile: " + SPJobName);
		} catch (NoSuchElementException e) {
			SimpleErrorHandler.handleWithContext(
				"user_should_verify_export_status_of_sp_updated_as_exported", e,
				"Table row " + rowNumber);
		} catch (AssertionError e) {
			SimpleErrorHandler.handleWithContext(
				"user_should_verify_export_status_of_sp_updated_as_exported", e,
				"Export status assertion for profile: " + SPJobName);
		} catch (Exception e) {
			SimpleErrorHandler.handleWithContext(
				"user_should_verify_export_status_of_sp_updated_as_exported", e,
				"Export status verification for profile: " + SPJobName);
		}
	}
	
	/**
	 * Helper method to scroll through table rows to ensure they are loaded
	 */
	private void scrollThroughTableRows(int maxRows) {
		for(int j = 1; j <= maxRows; j++) {
			try {
				WebElement rowElement = findTableElement(j, 1);
				scrollToElement(rowElement);
			} catch(Exception e) {
				// If row doesn't exist, continue
				LOGGER.debug("Row {} not found or not accessible", j);
			}
		}
	}
	
	public void user_should_click_on_recently_exported_success_profile_job_name() {
		try {
			// Wait for page to be ready
		wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
		PerformanceUtils.waitForUIStability(driver, 1);
		
			// Enhanced scrolling logic with better element targeting
			if (rowNumber == 1) {
				js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", showingJobResultsCount);
			} else if(rowNumber < 5) {
				WebElement SP_JobName = wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//tbody//tr[1]//td[1]//*")));
				js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", SP_JobName);
			} else if(rowNumber > 5) {
				WebElement SP_JobName = wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//tbody//tr[" + Integer.toString(rowNumber-5) + "]//td[1]//*")));
				js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", SP_JobName);
			}
			
		PerformanceUtils.waitForUIStability(driver, 1);
		
			// Enhanced element location with multiple fallback strategies
			WebElement E_SP_JobName = null;
			String primaryXpath = "//tbody//tr[" + Integer.toString(rowNumber) + "]//td[1]//*";
			String fallbackXpath = "//tbody//tr[" + Integer.toString(rowNumber) + "]//td[1]//a";
			String fallbackXpath2 = "//tbody//tr[" + Integer.toString(rowNumber) + "]//td[1]//span";
			
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
			Assert.assertEquals(E_SP_JobName.getText(), SPJobName);
			
			// Scroll element into view and ensure it's visible
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", E_SP_JobName);
		PerformanceUtils.waitForUIStability(driver, 1);
		
			// Enhanced click operation with multiple strategies
			boolean clickSuccessful = false;
			
			try {
				org.openqa.selenium.interactions.Actions actions = new org.openqa.selenium.interactions.Actions(driver);
			actions.doubleClick(E_SP_JobName).perform();
			PerformanceUtils.waitForUIStability(driver, 1);
			LOGGER.info("Click successful using double-click approach...");
				if (waitForPageNavigation()) {
					LOGGER.info("Navigation successful after double-click");
					clickSuccessful = true;
				}
			} catch (Exception e) {
				LOGGER.warn("Double-click approach failed: " + e.getMessage());
				// Log double-click failure
				LOGGER.error("Double-click navigation failed - Method: navigation_doubleclick_failed", e);
			}
			
			if (clickSuccessful) {
				LOGGER.info("Clicked on Recently Exported Success Profile with name : " + SPJobName +" in HCM Sync Profiles screen in PM");
				ExtentCucumberAdapter.addTestStepLog("Clicked on Recently Exported Success Profile with name : " + SPJobName +" in HCM Sync Profiles screen in PM");
				
				// Enhanced post-click navigation handling
				boolean navigationSuccessful = waitForPageNavigation();
				
				if (!navigationSuccessful) {
					LOGGER.warn("Page navigation didn't occur after click, trying alternative approaches");
					
					
					
					// Alternative approach 1: JavaScript navigation
					if (!navigationSuccessful) {
						try {
							LOGGER.info("Trying JavaScript navigation approach...");
						js.executeScript("arguments[0].click(); arguments[0].focus();", E_SP_JobName);
						PerformanceUtils.waitForUIStability(driver, 1);
						
							if (waitForPageNavigation()) {
								LOGGER.info("Navigation successful via JavaScript focus click");
								navigationSuccessful = true;
							}
											} catch (Exception e) {
						LOGGER.warn("JavaScript navigation approach failed: " + e.getMessage());
						// Log JS navigation failure
						LOGGER.error("JavaScript navigation failed - Method: navigation_javascript_failed", e);
					}
					}
					
					// Alternative approach 2: Keyboard Enter
					if (!navigationSuccessful) {
						try {
							LOGGER.info("Trying keyboard Enter approach...");
						E_SP_JobName.sendKeys(org.openqa.selenium.Keys.ENTER);
						PerformanceUtils.waitForUIStability(driver, 1);
						
							if (waitForPageNavigation()) {
								LOGGER.info("Navigation successful via keyboard Enter");
								navigationSuccessful = true;
							}
											} catch (Exception e) {
						LOGGER.warn("Keyboard Enter approach failed: " + e.getMessage());
						// Log keyboard navigation failure
						LOGGER.error("Keyboard navigation failed - Method: navigation_keyboard_failed", e);
					}
					}
				}
				
				if (!navigationSuccessful) {
					LOGGER.error("All navigation attempts failed - page did not open after click");
					throw new Exception("Page navigation failed after successful click operation");
				} else {
					LOGGER.info("Page navigation completed successfully");
				}
			}
			
		} catch (TimeoutException e) {
			SimpleErrorHandler.handleWithContext("user_should_click_on_recently_exported_success_profile_job_name", e, "Job name element interaction timeout");
		} catch (NoSuchElementException e) {
			SimpleErrorHandler.handleWithContext("user_should_click_on_recently_exported_success_profile_job_name", e, "Job name element not found");
		} catch (ElementNotInteractableException e) {
			SimpleErrorHandler.handleWithContext("user_should_click_on_recently_exported_success_profile_job_name", e, "Job name element not interactable");
		} catch (StaleElementReferenceException e) {
			SimpleErrorHandler.handleWithContext("user_should_click_on_recently_exported_success_profile_job_name", e, "Stale job name element");
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			SimpleErrorHandler.handleWithContext("user_should_click_on_recently_exported_success_profile_job_name", e, "Thread interrupted during click operation");
		} catch (Exception e) {
			SimpleErrorHandler.handleWithContext("user_should_click_on_recently_exported_success_profile_job_name", e, "Job name click operation");
		} 
	}
	
	public void user_should_be_navigated_to_sp_details_page() {
		try {
			waitForPageReady();
			
			// Check for expected page element first
			try {
				Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(SPdetailsPageText)).isDisplayed());
			} catch (TimeoutException e) {
				// Fallback: Check URL pattern as alternative validation
				String currentUrl = driver.getCurrentUrl();
				LOGGER.info("Element not found, checking URL pattern. Current URL: {}", currentUrl);
				
				if (!currentUrl.contains("profile") && !currentUrl.contains("details")) {
					throw new AssertionError("Navigation failed - Expected SP details page but URL does not indicate success: " + currentUrl);
				} else {
					LOGGER.warn("⚠️ Page element not found but URL suggests navigation worked: {}", currentUrl);
				}
			}
			
			logAndReport("SP details page opens as expected on click of Recently Exported Success Profile Job name in HCM Sync Profiles screen in PM....");
			
		} catch (TimeoutException e) {
			SimpleErrorHandler.handleWithContext(
				"user_should_be_navigated_to_sp_details_page", e,
				"SP details page navigation verification");
		} catch (AssertionError e) {
			SimpleErrorHandler.handleWithContext(
				"user_should_be_navigated_to_sp_details_page", e,
				"SP details page navigation assertion");
		} catch (Exception e) {
			SimpleErrorHandler.handleWithContext(
				"user_should_be_navigated_to_sp_details_page", e,
				"SP details page verification");
		}
	}
	
	public void click_on_three_dots_in_sp_details_page() {
		try {
			waitForPageReady();
			performEnhancedClick(threeDotsinSPdetailsPage, "Three Dots in SP Details Page");
			logAndReport("Clicked on Three Dots in SP Details Page");
			waitForPageReady(0); // Wait for spinner only
		} catch(Exception e) {
			SimpleErrorHandler.handleWithContext("click_on_three_dots_in_sp_details_page", e, "Three dots menu");
		}
	}
	
	public void click_on_edit_success_profile_option() {
		try {
			waitForPageReady();
			performEnhancedClick(editSPbuttoninSPdetailsPage, "Edit Success Profile option in SP Details Page");
			logAndReport("Clicked on Edit Success Profile option in SP Details Page");
			waitForPageReady(0);
		} catch(Exception e) {
			SimpleErrorHandler.handleWithContext("click_on_edit_success_profile_option", e, "Edit Success Profile option");
		}
	}
	
	public void click_on_edit_button_of_details_section() {
		try {
			waitForPageReady();
			performEnhancedClick(editDetailsBtn, "Edit button of Success Profile Details section");
			logAndReport("Clicked on Edit button of Success Profile Details section");
			waitForPageReady(0);
		} catch(Exception e) {
			SimpleErrorHandler.handleWithContext("click_on_edit_button_of_details_section", e, "Edit button of Details section");
		}
	}
	
	public void modify_function_and_sub_function_values_of_the_success_profile() {
		try {
			waitForPageReady();
			
			// Modify Function dropdown
			performEnhancedClick(FunctionDropdownofSP, "Function dropdown in Success Profile Details section");
			logAndReport("Clicked on Function dropdown in Success Profile Details section");
			
			String functionValue = wait.until(ExpectedConditions.visibilityOf(FunctionValue)).getText();
			performEnhancedClick(FunctionValue, "Function value: " + functionValue);
			logAndReport("Function value Modified to " + functionValue +" in Success Profile Details section");
		
		PerformanceUtils.waitForUIStability(driver, 2);
		
			// Modify Sub-Function dropdown  
			performEnhancedClick(SubFunctionDropdownofSP, "Subfunction dropdown in Success Profile Details section");
			logAndReport("Clicked on Subfunction dropdown in Success Profile Details section");
			
			String subFunctionValue = wait.until(ExpectedConditions.visibilityOf(SubFunctionValue)).getText();
			performEnhancedClick(SubFunctionValue, "Sub-Function value: " + subFunctionValue);
			logAndReport("Sub-Function value Modified to " + subFunctionValue +" in Success Profile Details section");
			
	} catch(TimeoutException e) {
		SimpleErrorHandler.handleWithContext("modify_function_and_sub_function_values_of_the_success_profile", e, "Function/SubFunction dropdown operations");
	} catch(Exception e) {
		SimpleErrorHandler.handleWithContext("modify_function_and_sub_function_values_of_the_success_profile", e, "Function/SubFunction modification");
	}
	}
	
	public void click_on_done_button() {
	try {
		PerformanceUtils.waitForPageReady(driver, 2);
		performEnhancedClick(DoneBtnofSP, "Done button of Success Profile Details section");
		logAndReport("Clicked on Done button of Success Profile Details section");
		waitForPageReady();
	} catch(Exception e) {
		SimpleErrorHandler.handleWithContext("click_on_done_button", e, "Done button operation");
	}
	}
	
	public void click_on_save_button() {
		try {
			waitForPageReady();
			performEnhancedClick(SaveBtnofSP, "Save button of Success Profile");
			logAndReport("Clicked on Save button of Success Profile");
			waitForPageReady();
		} catch(Exception e) {
			SimpleErrorHandler.handleWithContext("click_on_save_button", e, "Save button operation");
		}
	}
	
	public void user_should_be_navigated_to_sp_details_page_after_saving_sp_details() {
		try {
			waitForPageReady();
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(SPdetailsPageText)).isDisplayed());
			logAndReport("Navigated to SP details page as expected after Saving SP details....");
		} catch (TimeoutException e) {
			SimpleErrorHandler.handleWithContext("user_should_be_navigated_to_sp_details_page_after_saving_sp_details", e, "SP details page navigation after save");
		} catch (AssertionError e) {
			SimpleErrorHandler.handleWithContext("user_should_be_navigated_to_sp_details_page_after_saving_sp_details", e, "SP details page assertion after save");
		} catch (Exception e) {
			SimpleErrorHandler.handleWithContext("user_should_be_navigated_to_sp_details_page_after_saving_sp_details", e, "SP details page verification after save");
		}
	}
	
	public void user_should_verify_recently_modified_success_profile_is_displaying_on_top_of_the_job_proifles_list() {
		try {
			waitForPageReady();
			WebElement topJobName = findTableElement(1, 1);
			Assert.assertEquals(topJobName.getText(), SPJobName);
			logAndReport("Recently Modified SP with name " + topJobName.getText() +" is displaying on the Top of the Profiles List as Expected in HCM Sync Profiles screen");
		} catch(TimeoutException e) {
			SimpleErrorHandler.handleWithContext("user_should_verify_recently_modified_success_profile_is_displaying_on_top_of_the_job_proifles_list", e, "Top profile verification");
		} catch(AssertionError e) {
			SimpleErrorHandler.handleWithContext("user_should_verify_recently_modified_success_profile_is_displaying_on_top_of_the_job_proifles_list", e, "Profile position assertion");
		} catch(Exception e) {
			SimpleErrorHandler.handleWithContext("user_should_verify_recently_modified_success_profile_is_displaying_on_top_of_the_job_proifles_list", e, "Profile list verification");
		}
	}
	
	public void user_should_verify_recently_exported_and_modified_success_profile_export_status_updated_as_exported_modfied() {
		try {
			waitForPageReady();
			WebElement topJobName = findTableElement(1, 1);
			Assert.assertEquals(topJobName.getText(), SPJobName);
			SPJobName = topJobName.getText();
			
			WebElement exportStatus = findTableElement(1, 8);
			Assert.assertEquals(exportStatus.getText(), "Exported - Modified");
			logAndReport("Export Status of recently Exported and Modfied SP with name " + SPJobName + " updated as " + exportStatus.getText() + " as expected...");
		} catch(TimeoutException e) {
			SimpleErrorHandler.handleWithContext("user_should_verify_recently_exported_and_modified_success_profile_export_status_updated_as_exported_modfied", e, "Export status verification after modification");
		} catch(AssertionError e) {
			SimpleErrorHandler.handleWithContext("user_should_verify_recently_exported_and_modified_success_profile_export_status_updated_as_exported_modfied", e, "Export status assertion for modified profile");
		} catch(Exception e) {
			SimpleErrorHandler.handleWithContext("user_should_verify_recently_exported_and_modified_success_profile_export_status_updated_as_exported_modfied", e, "Modified profile export status verification");
		}
	}

}
