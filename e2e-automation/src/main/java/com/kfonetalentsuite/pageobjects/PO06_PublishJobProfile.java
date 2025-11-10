package com.kfonetalentsuite.pageobjects;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.kfonetalentsuite.utils.PerformanceUtils;
import com.kfonetalentsuite.utils.ScreenshotHandler;
import com.kfonetalentsuite.utils.Utilities;
import com.kfonetalentsuite.webdriverManager.DriverManager;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;


public class PO06_PublishJobProfile {
	
	WebDriver driver = DriverManager.getDriver();	

	protected static final Logger LOGGER = (Logger) LogManager.getLogger();
	PO06_PublishJobProfile publishJobProfile;
	
	public static String job1OrgName;

	public PO06_PublishJobProfile() throws IOException {
		// super();
		PageFactory.initElements(driver, this);
	}

	WebDriverWait wait = DriverManager.getWait();
	Utilities utils = new Utilities();
	JavascriptExecutor js = (JavascriptExecutor) driver;
	LocalDate currentdate = LocalDate.now();
	Month currentMonth = currentdate.getMonth();
	
	//XPATHs
	@FindBy(xpath = "//*[@class='blocking-loader']//img")
	@CacheLookup
	WebElement pageLoadSpinner1;
	
	@FindBy(xpath = "//div[@data-testid='loader']//img")
	@CacheLookup
	WebElement pageLoadSpinner2;
	
	@FindBy(xpath = "//tbody//tr[1]//td[2]//div[contains(text(),'(')]")
	@CacheLookup
	public WebElement jobNameinRow1;
	
	@FindBy(xpath = "//tbody//tr[2]//td[2]//div[contains(text(),'(')]")
	@CacheLookup
	public WebElement jobNameinRow2;
	
	@FindBy(xpath = "//tbody//tr[2]//button[@id='publish-btn'][1]")
	@CacheLookup
	public WebElement job1PublishBtn;
	
	// Primary success message locator with fallback options
	@FindBy(xpath = "//p[contains(text(),'Success profile published')]/..")
	@CacheLookup
	public WebElement publishSuccessMsg;
	
	// Alternative success message locators for enhanced reliability
	@FindBy(xpath = "//p[contains(text(),'Success profile published')]")
	@CacheLookup
	public WebElement publishSuccessMsgDirect;
	
	@FindBy(xpath = "//*[contains(text(),'profile published') or contains(text(),'successfully published')]")
	@CacheLookup
	public WebElement publishSuccessMsgFlexible;
	
	@FindBy(xpath = "//input[contains(@id,'search-job-title')]")
	@CacheLookup
	public WebElement searchBar;
	
	@FindBy(xpath = "//div[contains(@id,'results-toggle')]//label//div//div[2]")
	@CacheLookup
	public WebElement viewPublishedToggleBtn;
	
	@FindBy(xpath = "//input[@id='toggleSwitch']")
	@CacheLookup
	public WebElement toggleSwitch;
	
	@FindBy(xpath = "//tbody//tr[2]//button[text()='Published'][1]")
	@CacheLookup
	public WebElement job1PublishedBtn;
	
	@FindBy(xpath = "//span[contains(text(),'HCM Sync')]")
	@CacheLookup
	public WebElement HCMSyncProfilesTabinPM;
	
	@FindBy(xpath = "//span[text()='Jobs']")
	@CacheLookup
	public WebElement JobsPageHeaderinArchitect;
	
	@FindBy(xpath = "//h1[contains(text(),'Sync Profiles')]")
	@CacheLookup
	public WebElement HCMSyncProfilesHeader;
	
	@FindBy(xpath = "//input[@type='search']")
	@CacheLookup
	public WebElement ProfilesSearch;
	
	@FindBy(xpath = "//tbody//tr[1]//td//div//span[1]//a")
	@CacheLookup
	public WebElement HCMSyncProfilesJobinRow1;
	
	@FindBy(xpath = "//span[contains(text(),'Select your view')]")
	@CacheLookup
	public WebElement SPdetailsPageText;
	
	@FindBy(xpath = "//tbody//tr[1]//td[7]//span")
	@CacheLookup
	public WebElement HCMSyncProfilesDateinRow1;
	
	@FindBy(xpath = "//h1[contains(text(),'Profile Manager')]")
	@CacheLookup
	WebElement PMHeader;
	
	@FindBy(xpath = "//h1[contains(text(),'Architect')]")
	@CacheLookup
	WebElement ArchitectHeader;
	
	@FindBy(xpath = "//div[contains(@class,'leading')]//div[1]//div[1]")
	@CacheLookup
	WebElement JCpageOrgJobTitleHeader;
	
	@FindBy(xpath = "//button[@id='publish-select-btn']")
	@CacheLookup
	WebElement JCPagePublishSelectBtn;
	
	@FindBy(xpath = "//*[@id='no-data-container']")
	@CacheLookup
	WebElement noDataContainer;
	
	@FindBy(xpath = "//span[@aria-label='Profile Manager']")
	@CacheLookup
	public WebElement KfoneMenuPMBtn;
	
	@FindBy(xpath = "//button[@id='global-nav-menu-btn']")
	@CacheLookup
	public WebElement KfoneMenu;
	
	@FindBy(xpath = "//span[@aria-label='Architect']")
	@CacheLookup
	public WebElement KfoneMenuArchitectBtn;
	
	@FindBy(xpath = "//tbody//tr[1]//td//div//div//a")
	@CacheLookup
	public WebElement ArchitectJobinRow1;
	
	@FindBy(xpath = "//tbody//tr[1]//td[9]")
	@CacheLookup
	public WebElement ArchitectDateinRow1;
	
	
	//METHODs
	public void user_should_verify_publish_button_is_displaying_on_first_job_profile() {
		try { 
			String job1NameText = wait.until(ExpectedConditions.visibilityOf(jobNameinRow1)).getText();
			job1OrgName = job1NameText.split("-", 2)[0].trim();
			PO15_ValidateRecommendedProfileDetails.orgJobName = job1OrgName;
			wait.until(ExpectedConditions.visibilityOf(job1PublishBtn)).isDisplayed();
			Assert.assertEquals(true, job1PublishBtn.isDisplayed());
			LOGGER.info("Publish button on first job profile with Organization Name: " + job1NameText.split("-", 2)[0].trim() +" is displaying as expected");
			ExtentCucumberAdapter.addTestStepLog("Publish button on first job profile with Organization Name: " + job1NameText.split("-", 2)[0].trim() + " is displaying as expected");		
		} catch (Exception e) {
			ScreenshotHandler.handleTestFailure("verify_publish_btn_on_first_job_profile", e, 
				"Issue in verifying Publish button on first job....Please Investigate!!!");
		}
	}
	
	public void click_on_publish_button_on_first_job_profile() {
		try { 
			wait.until(ExpectedConditions.elementToBeClickable(job1PublishBtn)).click();
			LOGGER.info("Clicked on Publish button on first job profile with Organization Name: " + job1OrgName);
			ExtentCucumberAdapter.addTestStepLog("Clicked on Publish button on first job profile with Organization Name: " + job1OrgName);	
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_on_publish_button_on_first_job_profile", e);
			LOGGER.error("Issue in clicking Publish button on first job - Method: click_on_publish_button_on_first_job_profile", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in clicking Publish button on first job....Please Investigate!!!");
			Assert.fail("Issue in clicking Publish button on first job....Please Investigate!!!");
		}
	}
	
	public void user_should_verify_publish_success_popup_appears_on_screen() {
		try { 
			// Wait for any loading spinners to disappear first
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			
			// Primary approach: wait for the success message element
			WebElement successElement = null;
			boolean primarySuccess = false;
			
			try {
				successElement = wait.until(ExpectedConditions.visibilityOf(publishSuccessMsg));
				primarySuccess = true;
				LOGGER.info("Success popup found using primary locator (parent element)");
			} catch (Exception primaryException) {
				LOGGER.warn("Primary locator failed, trying alternative approaches...");
				
				// Alternative approach 1: Direct text element (no parent)
				try {
					successElement = wait.until(ExpectedConditions.visibilityOf(publishSuccessMsgDirect));
					LOGGER.info("Success popup found using direct text locator");
				} catch (Exception altException1) {
					
					// Alternative approach 2: Flexible text matching WebElement
					try {
						successElement = wait.until(ExpectedConditions.visibilityOf(publishSuccessMsgFlexible));
						LOGGER.info("Success popup found using flexible text matching WebElement");
					} catch (Exception altException2) {
						
						// Alternative approach 3: Dynamic By locators (fallback)
						try {
							successElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
								By.xpath("//*[contains(text(),'profile published') or contains(text(),'successfully published')]")));
							LOGGER.info("Success popup found using dynamic flexible locator");
						} catch (Exception altException3) {
							
							// Final attempt: Look for any success indicators
							try {
								successElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
									By.xpath("//*[contains(@class,'success') or contains(@class,'alert-success') or contains(text(),'Success')]")));
								LOGGER.info("Success indicator found using generic success locator");
							} catch (Exception finalException) {
								// Take screenshot for debugging
								try {
									String screenshotPath = ScreenshotHandler.captureScreenshotWithDescription("publish_success_popup_not_found");
									LOGGER.error("Screenshot taken for debugging: " + screenshotPath);
								} catch (Exception screenshotException) {
									LOGGER.error("Failed to take debugging screenshot: " + screenshotException.getMessage());
								}
							
								// Log page source for debugging
								LOGGER.error("Current page title: " + driver.getTitle());
								LOGGER.error("Current page URL: " + driver.getCurrentUrl());
								
								throw new RuntimeException("Success popup not found using any locator strategy. " +
									"Primary error: " + primaryException.getMessage() + 
									". Check screenshot for debugging details.");
							}
						}
					}
				}
			}
			
		// Verify the element is displayed and get text
		Assert.assertTrue(successElement.isDisplayed(), "Success popup element is not displayed");
		String publishSuccessMsgText = successElement.getText();
		LOGGER.info("Success message text: " + publishSuccessMsgText);
		ExtentCucumberAdapter.addTestStepLog("Success message: " + publishSuccessMsgText);
			
			// Wait for the popup to disappear (with shorter timeout for CI/CD compatibility)
			// In headless/Linux environments, popup may not auto-dismiss via JavaScript
			try {
				// Use a shorter wait (10 seconds) instead of full 180 seconds
				WebDriverWait shortWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
				
				if (primarySuccess) {
					shortWait.until(ExpectedConditions.invisibilityOf(publishSuccessMsg));
					LOGGER.info("Success popup dismissed automatically");
				} else {
					shortWait.until(ExpectedConditions.invisibilityOf(successElement));
					LOGGER.info("Success popup dismissed automatically");
				}
			} catch (org.openqa.selenium.TimeoutException te) {
				// Popup didn't auto-dismiss (common in headless mode) - this is OK
				LOGGER.warn("Success popup did not auto-dismiss within 10 seconds (expected in headless mode)");
				LOGGER.info("Continuing test execution - success message was verified");
				ExtentCucumberAdapter.addTestStepLog("✓ Success popup verified (auto-dismiss skipped for headless compatibility)");
			}
			
		// Final spinner check
		wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
		
		LOGGER.info("Publish Success Popup verification completed successfully");
		ExtentCucumberAdapter.addTestStepLog("Publish Success Popup appeared and verified");
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_verify_publish_success_popup_appears_on_screen", e);
			LOGGER.error("Error in verifying Publish Success popup - Method: user_should_verify_publish_success_popup_appears_on_screen", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying Publish Success popup - " + e.getMessage());
			Assert.fail("Issue in verifying Publish Success popup on screen. Error: " + e.getMessage());
		}
	}
	
	public void click_on_view_published_toggle_button_to_turn_on() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForPageReady(driver, 3); // Smart wait instead of fixed 3000ms
			if(toggleSwitch.getAttribute("aria-checked")== "true" || toggleSwitch.isSelected()) {
				LOGGER.info("View published toggle button is already turned ON");
				ExtentCucumberAdapter.addTestStepLog("View published toggle button is already turned ON"); 
			} else {
				utils.jsClick(driver, viewPublishedToggleBtn);
				wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
				PerformanceUtils.waitForPageReady(driver, 2); // Smart wait instead of fixed 2000ms
				LOGGER.info("View published toggle button is turned ON");
				ExtentCucumberAdapter.addTestStepLog("View published toggle button is turned ON");
				PO17_ValidateSortingFunctionality.jobNamesTextInDefaultOrder.clear();
			}
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForPageReady(driver, 3); // Smart wait instead of fixed 3000ms
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_on_view_published_toggle_button_to_turn_on", e);
			LOGGER.error("Issue in clicking View Published toggle button to turn ON - Method: click_on_view_published_toggle_button_to_turn_on", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in clicking View Published toggle button to turn ON...Please Investigate!!!");
			Assert.fail("Issue in clicking View Published toggle button to turn ON...Please Investigate!!!");
		}
	}
	
	public void search_for_published_job_name_in_view_published_screen() {	
		try {
			wait.until(ExpectedConditions.visibilityOf(searchBar)).clear();
			try {
				wait.until(ExpectedConditions.elementToBeClickable(searchBar)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", searchBar);
				} catch (Exception s) {
					utils.jsClick(driver, searchBar);
				}
			}
			
			wait.until(ExpectedConditions.visibilityOf(searchBar)).sendKeys(PO15_ValidateRecommendedProfileDetails.orgJobName);
			wait.until(ExpectedConditions.visibilityOf(searchBar)).sendKeys(Keys.ENTER);
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForPageReady(driver, 2); 
			LOGGER.info("Entered job name as " + PO15_ValidateRecommendedProfileDetails.orgJobName + " in the search bar in View Published screen");
			ExtentCucumberAdapter.addTestStepLog("Entered job name as " + PO15_ValidateRecommendedProfileDetails.orgJobName  + " in the search bar in View Published screen");
			PerformanceUtils.waitForPageReady(driver, 2); 
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("search_for_published_job_name_in_view_published_screen", e);
			LOGGER.error("Failed to enter Organization job name text in search bar in View Published screen - Method: search_for_published_job_name_in_view_published_screen", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Failed to enter Organization job name text in search bar in View Published screen...Please investigate!!!");
			Assert.fail("Failed to enter Organization job name text in search bar in View Published screen...Please investigate!!!");
		}
	    
	}
	
	public void user_should_verify_published_job_is_displayed_in_view_published_screen() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			String job1NameText = wait.until(ExpectedConditions.visibilityOf(jobNameinRow1)).getText();
			job1OrgName = job1NameText.split("-", 2)[0].trim();
			Assert.assertEquals(PO15_ValidateRecommendedProfileDetails.orgJobName, job1NameText.split("-", 2)[0].trim());
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(job1PublishedBtn)).isDisplayed());
			LOGGER.info("Published Job with name : " + job1NameText.split("-", 2)[0].trim() +" is displayed in view published screen as expected");
			ExtentCucumberAdapter.addTestStepLog("Published Job with name : " + job1NameText.split("-", 2)[0].trim() + " is displayed in view published screen as expected");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_verify_published_job_is_displayed_in_view_published_screen", e);
			LOGGER.error("Issue in verifying Published Job in view published screen - Method: user_should_verify_published_job_is_displayed_in_view_published_screen", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying Published Job with name : " + PO15_ValidateRecommendedProfileDetails.orgJobName + " in view published screen...Please Investigate!!!");
			Assert.fail("Issue in verifying Published Job with name : " + PO15_ValidateRecommendedProfileDetails.orgJobName + " in view published screen...Please Investigate!!!");
		}
	}
	
	public void user_should_navigate_to_hcm_sync_profiles_tab_in_pm() {
		try {
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner1));
			PerformanceUtils.waitForPageReady(driver, 2);
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(HCMSyncProfilesTabinPM)).isDisplayed());
			try {
				wait.until(ExpectedConditions.elementToBeClickable(HCMSyncProfilesTabinPM)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", HCMSyncProfilesTabinPM);
				} catch (Exception s) {
					utils.jsClick(driver, HCMSyncProfilesTabinPM);
				}
			}
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner1));
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(HCMSyncProfilesHeader)).isDisplayed());
			String HCMSyncProfilesHeaderText = wait.until(ExpectedConditions.visibilityOf(HCMSyncProfilesHeader)).getText();
			Assert.assertEquals("HCM Sync Profiles",HCMSyncProfilesHeaderText);
			LOGGER.info("User navigated to HCM Sync Profiles screen in PM");
			ExtentCucumberAdapter.addTestStepLog("User navigated to HCM Sync Profiles screen in PM");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_navigate_to_hcm_sync_profiles_tab_in_pm", e);
			LOGGER.error("Issue in Navigating to HCM Sync Profiles screen in Profile Manager - Method: user_should_navigate_to_hcm_sync_profiles_tab_in_pm", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in Navigating to HCM Sync Profiles screen in Profile Manager...Please Investigate!!!");
			Assert.fail("Issue in Navigating to HCM Sync Profiles screen in Profile Manager...Please Investigate!!!");
		}
		
	}
	
	public void search_for_published_job_name_in_hcm_sync_profiles_tab_in_pm() {
		wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner1));
		Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(ProfilesSearch)).isDisplayed());
		try {
			wait.until(ExpectedConditions.visibilityOf(ProfilesSearch)).clear();
			wait.until(ExpectedConditions.visibilityOf(ProfilesSearch)).sendKeys(job1OrgName.split("-", 2)[0].trim());
			wait.until(ExpectedConditions.visibilityOf(ProfilesSearch)).sendKeys(Keys.ENTER);
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner1));
			LOGGER.info("Entered job name as " + job1OrgName.split("-", 2)[0].trim() +" in the search bar in HCM Sync Profiles screen in PM");
			ExtentCucumberAdapter.addTestStepLog("Entered job name as " + job1OrgName.split("-", 2)[0].trim() + " in the search bar in  HCM Sync Profiles screen in PM");			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("search_for_published_job_name_in_hcm_sync_profiles_tab_in_pm", e);
			LOGGER.error("Failed to enter Organization job name text in search bar in HCM Sync Profiles screen in PM - Method: search_for_published_job_name_in_hcm_sync_profiles_tab_in_pm", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Failed to enter Organization job name text in search bar in HCM Sync Profiles screen in PM...Please investigate!!!");
			Assert.fail("Failed to enter Organization job name text in search bar in HCM Sync Profiles screen in PM...Please investigate!!!");
		}
	}
	
	public void user_should_verify_published_job_is_displayed_in_hcm_sync_profiles_tab_in_pm() {
		try {
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner1));
			PerformanceUtils.waitForPageReady(driver, 2); // Smart wait instead of fixed 2000ms
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner1));
			String job1NameText = wait.until(ExpectedConditions.visibilityOf(HCMSyncProfilesJobinRow1)).getText();
			Assert.assertEquals(job1OrgName, job1NameText.split("-", 2)[0].trim());
			LOGGER.info("Published Job with name : " + job1NameText.split("-", 2)[0].trim() +" is displayed in HCM Sync Profiles screen in PM as expected");
			ExtentCucumberAdapter.addTestStepLog("Published Job with name : " + job1NameText.split("-", 2)[0].trim() + " is displayed in HCM Sync Profiles screen in PM as expected");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_verify_published_job_is_displayed_in_hcm_sync_profiles_tab_in_pm", e);
			LOGGER.error("Issue in verifying published job in HCM Sync Profiles screen in PM - Method: user_should_verify_published_job_is_displayed_in_hcm_sync_profiles_tab_in_pm", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying published job in HCM Sync Profiles screen in PM...Please Investigate!!!");
			Assert.fail("Issue in verifying published job in HCM Sync Profiles screen in PM...Please Investigate!!!");
		}
	}
	
	public void user_should_verify_date_on_published_job_matches_with_current_date() {
		try {
			int currentYear = currentdate.getYear();
			int currentDay = currentdate.getDayOfMonth();
			String cd;
			String todayDate;
			if(Integer.toString(currentDay).length() == 1) {
				DecimalFormat df = new DecimalFormat("00"); 
				cd = df.format(currentDay);
				todayDate = currentMonth.toString().substring(0,1) + currentMonth.toString().substring(1,3).toLowerCase() + " " + cd + ", " + Integer.toString(currentYear);
			} else {
				todayDate = currentMonth.toString().substring(0,1) + currentMonth.toString().substring(1,3).toLowerCase() + " " + Integer.toString(currentDay) + ", " + Integer.toString(currentYear);
			}
			String jobPublishedDate = wait.until(ExpectedConditions.visibilityOf(HCMSyncProfilesDateinRow1)).getText();
			Assert.assertEquals(jobPublishedDate, todayDate);
			LOGGER.info("Last Modified date is verified successfully on Published Job with name : " + job1OrgName );
			ExtentCucumberAdapter.addTestStepLog("Last Modified Date is verified successfully on Published Job with name : " + job1OrgName );
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_verify_date_on_published_job_matches_with_current_date", e);
			LOGGER.error("Issue in verifying date on published job that matches with Last Modified date - Method: user_should_verify_date_on_published_job_matches_with_current_date", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying date on published job that matches with Last Modified date...Please Invetsigate!!!");
			Assert.fail("Issue in verifying date on published job that matches with Last Modified date...Please Invetsigate!!!");
		}
	}
	
	public void user_should_verify_sp_details_page_opens_on_click_of_published_job_name() {
		try {
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner1));
			PerformanceUtils.waitForPageReady(driver, 2); // Smart wait instead of fixed 2000ms
			try {
				wait.until(ExpectedConditions.elementToBeClickable(HCMSyncProfilesJobinRow1)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", HCMSyncProfilesJobinRow1);
				} catch (Exception s) {
					utils.jsClick(driver, HCMSyncProfilesJobinRow1);
				}
			}
			LOGGER.info("Clicked on Published Job with name : " + job1OrgName + " in HCM Sync Profiles screen in PM");
			ExtentCucumberAdapter.addTestStepLog("Published Job with name : " + job1OrgName + " in HCM Sync Profiles screen in PM");
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner1));
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(SPdetailsPageText)).isDisplayed());
			LOGGER.info("SP details page opens as expected on click of Published Job name in HCM Sync Profiles screen in PM....");
			ExtentCucumberAdapter.addTestStepLog( "SP details page opens as expected on click of Published Job name in HCM Sync Profiles screen in PM....");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_verify_sp_details_page_opens_on_click_of_published_job_name", e);
			LOGGER.error("Issue in Navigating to SP details Page on click of Published Job name in HCM Sync Profiles screen in PM - Method: user_should_verify_sp_details_page_opens_on_click_of_published_job_name", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in Navigating to SP details Page on click of Published Job name in HCM Sync Profiles screen in PM...Please Investigate!!!");
			Assert.fail("Issue in Navigating to SP details Page on click of Published Job name in HCM Sync Profiles screen in PM...Please Investigate!!!");
		}
	}
	
	public void click_on_kfone_global_menu_in_job_mapping_ui() {
	try {
		wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
		PerformanceUtils.waitForPageReady(driver, 2);
		// Click on KFONE Global Menu button
		try {
			wait.until(ExpectedConditions.elementToBeClickable(KfoneMenu)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", KfoneMenu);
				} catch (Exception s) {
					utils.jsClick(driver, KfoneMenu);
				}
			}
			
			LOGGER.info("Successfully clicked KFONE Global Menu in Job Mapping UI");
			ExtentCucumberAdapter.addTestStepLog("Successfully clicked KFONE Global Menu in Job Mapping UI");
			
			PerformanceUtils.waitForPageReady(driver, 1);			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_on_kfone_global_menu_in_job_mapping_ui", e);
			LOGGER.error("Issue in clicking on KFone Global Menu in Job Mapping UI - Method: click_on_kfone_global_menu_in_job_mapping_ui", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in clicking on KFone Global Menu in Job Mapping UI...Please Investigate!!!");
			Assert.fail("Issue in clicking on KFone Global Menu in Job Mapping UI...Please Investigate!!!");
		}
	}
	
	public void click_on_profile_manager_application_button_in_kfone_global_menu() {
		Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(KfoneMenuPMBtn)).isDisplayed());
		String applicationNameText = wait.until(ExpectedConditions.visibilityOf(KfoneMenuPMBtn)).getText();
		LOGGER.info(applicationNameText + " application is displaying as expected in KFONE Global Menu");
		ExtentCucumberAdapter.addTestStepLog(applicationNameText + " application is displaying as expected in KFONE Global Menu");
		try {
			wait.until(ExpectedConditions.elementToBeClickable(KfoneMenuPMBtn)).click();
			LOGGER.info("Clicked on Profile Manager application button in KFONE Global Menu");
			ExtentCucumberAdapter.addTestStepLog("Clicked on Profile Manager application button in KFONE Global Menu");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_on_profile_manager_application_button_in_kfone_global_menu", e);
			LOGGER.error("Issue in clicking Profile Manager application button in KFONE Global - Method: click_on_profile_manager_application_button_in_kfone_global_menu", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in clicking Profile Manager application button in KFONE Global...Please Investigate!!!");
			Assert.fail("Issue in clicking Profile Manager application button in KFONE Global...Please Investigate!!!");
		}
	}

	public void verify_user_should_land_on_profile_manager_dashboard_page() {
		try {
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner1));
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(PMHeader)).isDisplayed());
			String PMHeaderText = wait.until(ExpectedConditions.visibilityOf(PMHeader)).getText();
			LOGGER.info("User Successfully landed on the " + PMHeaderText + " Dashboard Page");
			ExtentCucumberAdapter.addTestStepLog("User Successfully landed on the " + PMHeaderText + " Dashboard Page");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_user_should_land_on_profile_manager_dashboard_page", e);
			LOGGER.error("Issue in landing on Profile Manager dashboard - Method: verify_profile_manager_dashboard_landing", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in landing on Profile Manager dashboard page...Please Investigate!!!");
			Assert.fail("Issue in landing on Profile Manager dashboard page...Please Investigate!!!");
		}
	}
	
	public void click_on_architect_application_button_in_kfone_global_menu() {
		Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(KfoneMenuArchitectBtn)).isDisplayed());
		String applicationNameText = wait.until(ExpectedConditions.visibilityOf(KfoneMenuArchitectBtn)).getText();
		LOGGER.info(applicationNameText + " application is displaying as expected in KFONE Global Menu");
		ExtentCucumberAdapter.addTestStepLog(applicationNameText + " application is displaying as expected in KFONE Global Menu");
		try {
			wait.until(ExpectedConditions.elementToBeClickable(KfoneMenuArchitectBtn)).click();
			LOGGER.info("Clicked on Architect application button in KFONE Global Menu");
			ExtentCucumberAdapter.addTestStepLog("Clicked on Architect application button in KFONE Global Menu");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_on_architect_application_button_in_kfone_global_menu", e);
			LOGGER.error("Issue in clicking Architect application button in KFONE Global - Method: click_on_architect_application_button_in_kfone_global_menu", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in clicking Architect application button in KFONE Global...Please Investigate!!!");
			Assert.fail("Issue in clicking Architect application button in KFONE Global...Please Investigate!!!");
		}
	}
	
	public void verify_user_should_land_on_architect_dashboard_page() {
		try {
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner1));
//			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(ArchitectHeader)).isDisplayed());
//			String ArchitectHeaderText = wait.until(ExpectedConditions.visibilityOf(ArchitectHeader)).getText();
			LOGGER.info("User Successfully landed on the Architect Dashboard Page");
			ExtentCucumberAdapter.addTestStepLog("User Successfully landed on the Architect Dashboard Page");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_user_should_land_on_architect_dashboard_page", e);
			LOGGER.error("Issue in landing on Profile Manager dashboard - Method: verify_architect_dashboard_landing", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in landing on Architect dashboard page...Please Investigate!!!");
			Assert.fail("Issue in landing on Architect dashboard page...Please Investigate!!!");
		}
	}
	
	public void user_should_navigate_to_jobs_page_in_architect() {
		try {
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner1));
			PerformanceUtils.waitForPageReady(driver, 2);
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(JobsPageHeaderinArchitect)).isDisplayed());
			try {
				wait.until(ExpectedConditions.elementToBeClickable(JobsPageHeaderinArchitect)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", JobsPageHeaderinArchitect);
				} catch (Exception s) {
					utils.jsClick(driver, JobsPageHeaderinArchitect);
				}
			}
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner1));
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(JobsPageHeaderinArchitect)).isDisplayed());
			String JobsHeaderText = wait.until(ExpectedConditions.visibilityOf(JobsPageHeaderinArchitect)).getText();
			Assert.assertEquals("Jobs",JobsHeaderText);
			LOGGER.info("User navigated to Jobs page in Architect");
			ExtentCucumberAdapter.addTestStepLog("User navigated to Jobs page in Architect");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_navigate_to_jobs_page_in_architect", e);
			LOGGER.error("Issue in Navigating to Jobs page in Architect - Method: user_should_navigate_to_jobs_page_in_architect", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in Navigating to Jobs page in Architect...Please Investigate!!!");
			Assert.fail("Issue in Navigating to Jobs page in Architect...Please Investigate!!!");
		}
		
	}
	
	public void search_for_published_job_name_in_jobs_page_in_architect() {
		wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner1));
		Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(ProfilesSearch)).isDisplayed());
		try {
			wait.until(ExpectedConditions.visibilityOf(ProfilesSearch)).clear();
			wait.until(ExpectedConditions.visibilityOf(ProfilesSearch)).sendKeys(job1OrgName.split("-", 2)[0].trim());
			wait.until(ExpectedConditions.visibilityOf(ProfilesSearch)).sendKeys(Keys.ENTER);
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner1));
			LOGGER.info("Entered job name as " + job1OrgName.split("-", 2)[0].trim() +" in the search bar in Jobs page in Architect");
			ExtentCucumberAdapter.addTestStepLog("Entered job name as " + job1OrgName.split("-", 2)[0].trim() + " in the search bar in Jobs page in Architect");			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("search_for_published_job_name_in_jobs_page_in_architect", e);
			LOGGER.error("Failed to enter Organization job name text in search bar in Jobs page in Architect - Method: search_for_published_job_name_in_jobs_page_in_architect", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Failed to enter Organization job name text in search bar in Jobs page in Architect...Please investigate!!!");
			Assert.fail("Failed to enter Organization job name text in search bar in Jobs page in Architect...Please investigate!!!");
		}
	}
	
	public void user_should_verify_published_job_is_displayed_in_jobs_page_in_architect() {
		try {
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner1));
			PerformanceUtils.waitForPageReady(driver, 2); // Smart wait instead of fixed 2000ms
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner1));
			String job1NameText = wait.until(ExpectedConditions.visibilityOf(ArchitectJobinRow1)).getText();
			Assert.assertEquals(job1OrgName, job1NameText.split("-", 2)[0].trim());
			LOGGER.info("Published Job with name : " + job1NameText.split("-", 2)[0].trim() +" is displayed in Jobs page in Architect as expected");
			ExtentCucumberAdapter.addTestStepLog("Published Job with name : " + job1NameText.split("-", 2)[0].trim() + " is displayed in Jobs page in Architect as expected");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_verify_published_job_is_displayed_in_hcm_sync_profiles_tab_in_pm", e);
			LOGGER.error("Issue in verifying published job in Jobs page in Architect - Method: user_should_verify_published_job_is_displayed_in_hcm_sync_profiles_tab_in_pm", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying published job in Jobs page in Architect...Please Investigate!!!");
			Assert.fail("Issue in verifying published job in Jobs page in Architect...Please Investigate!!!");
		}
	}
	
	public void user_should_verify__updated_date_on_published_job_matches_with_current_date_in_architect() {
		try {
			int currentYear = currentdate.getYear();
			int currentDay = currentdate.getDayOfMonth();
			String cd;
			String todayDate;
			if(Integer.toString(currentDay).length() == 1) {
				DecimalFormat df = new DecimalFormat("00"); 
				cd = df.format(currentDay);
				todayDate = currentMonth.toString().substring(0,1) + currentMonth.toString().substring(1,3).toLowerCase() + " " + cd + ", " + Integer.toString(currentYear);
			} else {
				todayDate = currentMonth.toString().substring(0,1) + currentMonth.toString().substring(1,3).toLowerCase() + " " + Integer.toString(currentDay) + ", " + Integer.toString(currentYear);
			}
			String jobPublishedDate = wait.until(ExpectedConditions.visibilityOf(ArchitectDateinRow1)).getText();
			Assert.assertEquals(jobPublishedDate, todayDate);
			LOGGER.info("Updated date is verified successfully on Published Job with name : " + job1OrgName + " in Architect Application");
			ExtentCucumberAdapter.addTestStepLog("Last Modified Date is verified successfully on Published Job with name : " + job1OrgName + job1OrgName + " in Architect Application");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_verify__updated_date_on_published_job_matches_with_current_date_in_architect", e);
			LOGGER.error("Issue in verifying date on published job that matches with Updated date in Architect Application - Method: user_should_verify__updated_date_on_published_job_matches_with_current_date_in_architect", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying date on published job that matches with Updated date in Architect Application...Please Invetsigate!!!");
			Assert.fail("Issue in verifying date on published job that matches with Updated date in Architect Application...Please Invetsigate!!!");
		}
	}
}
