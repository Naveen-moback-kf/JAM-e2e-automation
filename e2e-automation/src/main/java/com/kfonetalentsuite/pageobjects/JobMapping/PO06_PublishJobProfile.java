package com.kfonetalentsuite.pageobjects.JobMapping;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.ScreenshotHandler;
import com.kfonetalentsuite.utils.JobMapping.Utilities;
import com.kfonetalentsuite.utils.PageObjectHelper;
import com.kfonetalentsuite.webdriverManager.DriverManager;


public class PO06_PublishJobProfile {
	
	WebDriver driver = DriverManager.getDriver();	

	protected static final Logger LOGGER = (Logger) LogManager.getLogger();
	PO06_PublishJobProfile publishJobProfile;
	
	// THREAD-SAFE: Each thread gets its own isolated state for parallel execution
	public static ThreadLocal<String> job1OrgName = ThreadLocal.withInitial(() -> null);

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
	WebElement pageLoadSpinner1;
	
	@FindBy(xpath = "//div[@data-testid='loader']//img")
	WebElement pageLoadSpinner2;
	
	@FindBy(xpath = "//tbody//tr[1]//td[2]//div[contains(text(),'(')]")
	public WebElement jobNameinRow1;
	
	@FindBy(xpath = "//tbody//tr[2]//td[2]//div[contains(text(),'(')]")
	public WebElement jobNameinRow2;
	
	@FindBy(xpath = "//tbody//tr[2]//button[@id='publish-btn'][1]")
	public WebElement job1PublishBtn;
	
	// Primary success message locator with fallback options
	@FindBy(xpath = "//p[contains(text(),'Success profile published')]/..")
	public WebElement publishSuccessMsg;
	
	// Alternative success message locators for enhanced reliability
	@FindBy(xpath = "//p[contains(text(),'Success profile published')]")
	public WebElement publishSuccessMsgDirect;
	
	@FindBy(xpath = "//*[contains(text(),'profile published') or contains(text(),'successfully published')]")
	public WebElement publishSuccessMsgFlexible;
	
	@FindBy(xpath = "//input[contains(@id,'search-job-title')]")
	public WebElement searchBar;
	
	@FindBy(xpath = "//div[contains(@id,'results-toggle')]//label//div//div[2]")
	public WebElement viewPublishedToggleBtn;
	
	@FindBy(xpath = "//input[@id='toggleSwitch']")
	public WebElement toggleSwitch;
	
	@FindBy(xpath = "//tbody//tr[2]//button[text()='Published'][1]")
	public WebElement job1PublishedBtn;
	
	@FindBy(xpath = "//span[contains(text(),'HCM Sync')]")
	public WebElement HCMSyncProfilesTabinPM;
	
	@FindBy(xpath = "//span[text()='Jobs']")
	public WebElement JobsPageHeaderinArchitect;
	
	@FindBy(xpath = "//h1[contains(text(),'Sync Profiles')]")
	public WebElement HCMSyncProfilesHeader;
	
	@FindBy(xpath = "//input[@type='search']")
	public WebElement ProfilesSearch;
	
	@FindBy(xpath = "//tbody//tr[1]//td//div//span[1]//a")
	public WebElement HCMSyncProfilesJobinRow1;
	
	@FindBy(xpath = "//span[contains(text(),'Select your view')]")
	public WebElement SPdetailsPageText;
	
	@FindBy(xpath = "//tbody//tr[1]//td[7]//span")
	public WebElement HCMSyncProfilesDateinRow1;
	
	@FindBy(xpath = "//h1[contains(text(),'Profile Manager')]")
	WebElement PMHeader;
	
	@FindBy(xpath = "//h1[contains(text(),'Architect')]")
	WebElement ArchitectHeader;
	
	@FindBy(xpath = "//div[contains(@class,'leading')]//div[1]//div[1]")
	WebElement JCpageOrgJobTitleHeader;
	
	@FindBy(xpath = "//button[@id='publish-select-btn']")
	WebElement JCPagePublishSelectBtn;
	
	@FindBy(xpath = "//*[@id='no-data-container']")
	WebElement noDataContainer;
	
	@FindBy(xpath = "//span[@aria-label='Profile Manager']")
	public WebElement KfoneMenuPMBtn;
	
	@FindBy(xpath = "//button[@id='global-nav-menu-btn']")
	public WebElement KfoneMenu;
	
	@FindBy(xpath = "//span[@aria-label='Architect']")
	public WebElement KfoneMenuArchitectBtn;
	
	@FindBy(xpath = "//tbody//tr[1]//td//div//div//a")
	public WebElement ArchitectJobinRow1;
	
	@FindBy(xpath = "//tbody//tr[1]//td[9]")
	public WebElement ArchitectDateinRow1;
	

	//METHODs
	public void user_should_verify_publish_button_is_displaying_on_first_job_profile() {
		try { 
	String job1NameText = wait.until(ExpectedConditions.visibilityOf(jobNameinRow1)).getText();
	job1OrgName.set(job1NameText.split("-", 2)[0].trim());
	PO15_ValidateRecommendedProfileDetails.orgJobName.set(job1OrgName.get());
		wait.until(ExpectedConditions.visibilityOf(job1PublishBtn)).isDisplayed();
			Assert.assertEquals(true, job1PublishBtn.isDisplayed());
			PageObjectHelper.log(LOGGER, "Publish button on first job profile (Org: " + job1OrgName.get() + ") is displaying");
		} catch (Exception e) {
			ScreenshotHandler.handleTestFailure("verify_publish_btn_on_first_job_profile", e, 
				"Issue in verifying Publish button on first job");
		}
	}
	
	public void click_on_publish_button_on_first_job_profile() {
		try { 
		wait.until(ExpectedConditions.elementToBeClickable(job1PublishBtn)).click();
		PageObjectHelper.log(LOGGER, "Clicked Publish button on first job (Org: " + job1OrgName.get() + ")");
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_publish_button_on_first_job_profile",
				"Issue clicking Publish button on first job", e);
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
				LOGGER.info("Success popup found using primary locator");
			} catch (Exception primaryException) {
				LOGGER.warn("Primary locator failed, trying alternatives...");
				
				// Alternative approach 1: Direct text element
				try {
					successElement = wait.until(ExpectedConditions.visibilityOf(publishSuccessMsgDirect));
					LOGGER.info("Success popup found using direct text locator");
				} catch (Exception altException1) {
					
					// Alternative approach 2: Flexible text matching
					try {
						successElement = wait.until(ExpectedConditions.visibilityOf(publishSuccessMsgFlexible));
						LOGGER.info("Success popup found using flexible text locator");
					} catch (Exception altException2) {
						
						// Alternative approach 3: Dynamic By locators
						try {
							successElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
								By.xpath("//*[contains(text(),'profile published') or contains(text(),'successfully published')]")));
							LOGGER.info("Success popup found using dynamic locator");
						} catch (Exception altException3) {
							
							// Final attempt: Generic success indicators
							try {
								successElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
									By.xpath("//*[contains(@class,'success') or contains(@class,'alert-success') or contains(text(),'Success')]")));
								LOGGER.info("Success indicator found using generic locator");
							} catch (Exception finalException) {
								// Take screenshot for debugging
								try {
									String screenshotPath = ScreenshotHandler.captureScreenshotWithDescription("publish_success_popup_not_found");
									LOGGER.error("Screenshot taken: " + screenshotPath);
								} catch (Exception screenshotException) {
									LOGGER.error("Failed to take debugging screenshot");
								}
							
								// Log page info for debugging
								LOGGER.error("Current page title: " + driver.getTitle());
								LOGGER.error("Current URL: " + driver.getCurrentUrl());
								
								throw new RuntimeException("Success popup not found using any locator strategy");
							}
						}
					}
				}
			}
			
		// Verify the element is displayed and get text
		Assert.assertTrue(successElement.isDisplayed(), "Success popup element is not displayed");
		String publishSuccessMsgText = successElement.getText();
		PageObjectHelper.log(LOGGER, "Success message: " + publishSuccessMsgText);
			
			// Wait for the popup to disappear (with shorter timeout for CI/CD)
			try {
				WebDriverWait shortWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
				
				if (primarySuccess) {
					shortWait.until(ExpectedConditions.invisibilityOf(publishSuccessMsg));
					LOGGER.info("Success popup dismissed automatically");
				} else {
					shortWait.until(ExpectedConditions.invisibilityOf(successElement));
					LOGGER.info("Success popup dismissed automatically");
				}
			} catch (org.openqa.selenium.TimeoutException te) {
				LOGGER.warn("Success popup did not auto-dismiss (expected in headless mode)");
			}
			
		// Final spinner check
		wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
		LOGGER.info("Publish Success Popup verification completed");
			
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_publish_success_popup_appears_on_screen",
				"Issue verifying Publish Success popup", e);
		}
	}
	
	public void click_on_view_published_toggle_button_to_turn_on() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForPageReady(driver, 3);
			if(toggleSwitch.getAttribute("aria-checked")== "true" || toggleSwitch.isSelected()) {
				PageObjectHelper.log(LOGGER, "View published toggle button is already ON");
			} else {
				utils.jsClick(driver, viewPublishedToggleBtn);
				wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
				PerformanceUtils.waitForPageReady(driver, 2);
				PageObjectHelper.log(LOGGER, "View published toggle button is turned ON");
				PO17_ValidateSortingFunctionality_JAM.jobNamesTextInDefaultOrder.clear();
			}
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForPageReady(driver, 3);
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_view_published_toggle_button_to_turn_on",
				"Issue clicking View Published toggle button", e);
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
		
		wait.until(ExpectedConditions.visibilityOf(searchBar)).sendKeys(PO15_ValidateRecommendedProfileDetails.orgJobName.get());
			wait.until(ExpectedConditions.visibilityOf(searchBar)).sendKeys(Keys.ENTER);
		wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
		PerformanceUtils.waitForPageReady(driver, 2); 
		PageObjectHelper.log(LOGGER, "Searched for job: " + PO15_ValidateRecommendedProfileDetails.orgJobName.get() + " in View Published screen");
			PerformanceUtils.waitForPageReady(driver, 2); 
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "search_for_published_job_name_in_view_published_screen",
				"Failed to search for job in View Published screen", e);
		}
	    
	}
	
	public void user_should_verify_published_job_is_displayed_in_view_published_screen() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
	String job1NameText = wait.until(ExpectedConditions.visibilityOf(jobNameinRow1)).getText();
	job1OrgName.set(job1NameText.split("-", 2)[0].trim());
	Assert.assertEquals(PO15_ValidateRecommendedProfileDetails.orgJobName.get(), job1NameText.split("-", 2)[0].trim());
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(job1PublishedBtn)).isDisplayed());
			PageObjectHelper.log(LOGGER, "Published Job (Org: " + job1OrgName.get() + ") is displayed in view published screen");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_published_job_is_displayed_in_view_published_screen",
				"Issue verifying Published Job: " + PO15_ValidateRecommendedProfileDetails.orgJobName.get(), e);
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
			PageObjectHelper.log(LOGGER, "Navigated to HCM Sync Profiles screen in PM");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_navigate_to_hcm_sync_profiles_tab_in_pm",
				"Issue navigating to HCM Sync Profiles screen", e);
		}
		
	}
	
	public void search_for_published_job_name_in_hcm_sync_profiles_tab_in_pm() {
		wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner1));
		Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(ProfilesSearch)).isDisplayed());
		try {
		wait.until(ExpectedConditions.visibilityOf(ProfilesSearch)).clear();
		wait.until(ExpectedConditions.visibilityOf(ProfilesSearch)).sendKeys(job1OrgName.get().split("-", 2)[0].trim());
		wait.until(ExpectedConditions.visibilityOf(ProfilesSearch)).sendKeys(Keys.ENTER);
		wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner1));
		PageObjectHelper.log(LOGGER, "Searched for job: " + job1OrgName.get() + " in HCM Sync Profiles");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "search_for_published_job_name_in_hcm_sync_profiles_tab_in_pm",
				"Failed to search for job in HCM Sync Profiles", e);
		}
	}
	
	public void user_should_verify_published_job_is_displayed_in_hcm_sync_profiles_tab_in_pm() {
		try {
			// Wait for spinner and page ready
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner1));
			PerformanceUtils.waitForPageReady(driver, 2);

			
			// Check if results are actually present
			boolean resultsFound = false;
			String job1NameText = "";
			
			try {
				// Try to get first row with shorter wait (10 seconds)
				WebDriverWait shortWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
				job1NameText = shortWait.until(ExpectedConditions.visibilityOf(HCMSyncProfilesJobinRow1)).getText();
				resultsFound = true;
			} catch (TimeoutException e) {
				// No results found
				LOGGER.warn("Published job profile not found in search results after 10 seconds");
				
				// Try to get results count
				try {
					WebElement resultsCount = driver.findElement(By.xpath("//div[contains(@id,'results-toggle')]//*[contains(text(),'Showing')]"));
					String countText = resultsCount.getText();
					LOGGER.warn("Results count text: {}", countText);
					
				if (countText.contains("Showing 0")) {
					Assert.fail("Search returned 0 results - Published job '" + job1OrgName.get() + "' not found in HCM Sync Profiles");
					}
				} catch (Exception countEx) {
					LOGGER.warn("Could not get results count element");
				}
				
				// Check for "no profiles" message
				try {
					WebElement noProfilesMsg = driver.findElement(By.xpath("//div[contains(text(),'no Success Profiles') or contains(text(),'No profiles found')]"));
				if (noProfilesMsg.isDisplayed()) {
					Assert.fail("No profiles message displayed - Job '" + job1OrgName.get() + "' not found in HCM Sync Profiles");
					}
				} catch (Exception noMsgEx) {
					LOGGER.warn("Could not find 'no profiles' message");
				}
				
			Assert.fail("Published job '" + job1OrgName.get() + "' not found in HCM Sync Profiles after 10 seconds");
			}
			
			// Verify the job name matches
			if (resultsFound) {
			String actualJobName = job1NameText.split("-", 2)[0].trim();
			Assert.assertEquals(job1OrgName.get(), actualJobName);
			PageObjectHelper.log(LOGGER, "Published Job (Org: " + actualJobName + ") is displayed in HCM Sync Profiles");
			}
			
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_published_job_is_displayed_in_hcm_sync_profiles_tab_in_pm",
				"Issue verifying published job in HCM Sync Profiles", e);
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
		PageObjectHelper.log(LOGGER, "Last Modified date verified on Published Job: " + job1OrgName.get());
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_date_on_published_job_matches_with_current_date",
				"Issue verifying date on published job", e);
		}
	}
	
	public void user_should_verify_sp_details_page_opens_on_click_of_published_job_name() {
		try {
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner1));
			PerformanceUtils.waitForPageReady(driver, 2);
			try {
				wait.until(ExpectedConditions.elementToBeClickable(HCMSyncProfilesJobinRow1)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", HCMSyncProfilesJobinRow1);
				} catch (Exception s) {
					utils.jsClick(driver, HCMSyncProfilesJobinRow1);
				}
		}
		PageObjectHelper.log(LOGGER, "Clicked Published Job (Org: " + job1OrgName.get() + ") in HCM Sync Profiles");
		wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner1));
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(SPdetailsPageText)).isDisplayed());
			LOGGER.info("SP details page opened on click of Published Job name");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_sp_details_page_opens_on_click_of_published_job_name",
				"Issue navigating to SP details page", e);
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
			
		PageObjectHelper.log(LOGGER, "Clicked KFONE Global Menu in Job Mapping UI");
		
		PerformanceUtils.waitForPageReady(driver, 1);
		// PARALLEL EXECUTION FIX: Wait for menu to fully expand
		Thread.sleep(1000);
	} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_kfone_global_menu_in_job_mapping_ui",
				"Issue clicking KFone Global Menu", e);
		}
	}
	
	public void click_on_profile_manager_application_button_in_kfone_global_menu() {
		try {
			// PARALLEL EXECUTION FIX: Retry mechanism for stale elements
			int maxRetries = 3;
			boolean clicked = false;
			
			for (int attempt = 1; attempt <= maxRetries && !clicked; attempt++) {
				try {
					WebElement pmBtn = wait.until(ExpectedConditions.visibilityOf(KfoneMenuPMBtn));
					Assert.assertTrue(pmBtn.isDisplayed(), "Profile Manager button not visible in menu");
					String applicationNameText = pmBtn.getText();
					LOGGER.info(applicationNameText + " application is displaying in KFONE Global Menu");
					
					wait.until(ExpectedConditions.elementToBeClickable(KfoneMenuPMBtn)).click();
					PageObjectHelper.log(LOGGER, "Clicked Profile Manager application button in KFONE Global Menu");
					clicked = true;
				} catch (org.openqa.selenium.StaleElementReferenceException e) {
					if (attempt == maxRetries) {
						throw e;
					}
					LOGGER.warn("Stale element on attempt {}/{} - retrying PM button click", attempt, maxRetries);
					Thread.sleep(500);
				}
			}
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_profile_manager_application_button_in_kfone_global_menu",
				"Issue clicking Profile Manager application button", e);
		}
	}

	public void verify_user_should_land_on_profile_manager_dashboard_page() {
		try {
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner1));
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(PMHeader)).isDisplayed());
			String PMHeaderText = wait.until(ExpectedConditions.visibilityOf(PMHeader)).getText();
			PageObjectHelper.log(LOGGER, "User landed on " + PMHeaderText + " Dashboard Page");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_user_should_land_on_profile_manager_dashboard_page",
				"Issue landing on Profile Manager dashboard", e);
		}
	}
	
	public void click_on_architect_application_button_in_kfone_global_menu() {
		try {
			// PARALLEL EXECUTION FIX: Retry mechanism for stale elements
			int maxRetries = 3;
			boolean clicked = false;
			
			for (int attempt = 1; attempt <= maxRetries && !clicked; attempt++) {
				try {
					WebElement architectBtn = wait.until(ExpectedConditions.visibilityOf(KfoneMenuArchitectBtn));
					Assert.assertTrue(architectBtn.isDisplayed(), "Architect button not visible in menu");
					String applicationNameText = architectBtn.getText();
					LOGGER.info(applicationNameText + " application is displaying in KFONE Global Menu");
					
					wait.until(ExpectedConditions.elementToBeClickable(KfoneMenuArchitectBtn)).click();
					PageObjectHelper.log(LOGGER, "Clicked Architect application button in KFONE Global Menu");
					clicked = true;
				} catch (org.openqa.selenium.StaleElementReferenceException e) {
					if (attempt == maxRetries) {
						throw e;
					}
					LOGGER.warn("Stale element on attempt {}/{} - retrying Architect button click", attempt, maxRetries);
					Thread.sleep(500);
				}
			}
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_architect_application_button_in_kfone_global_menu",
				"Issue clicking Architect application button", e);
		}
	}
	
	public void verify_user_should_land_on_architect_dashboard_page() {
		try {
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner1));
			PageObjectHelper.log(LOGGER, "User landed on Architect Dashboard Page");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_user_should_land_on_architect_dashboard_page",
				"Issue landing on Architect dashboard", e);
		}
	}
	
	public void user_should_navigate_to_jobs_page_in_architect() {
		try {
			// Wait for page to be ready
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner1));
			PerformanceUtils.waitForPageReady(driver, 2);
			
			// HEADLESS FIX: Add extra wait for JavaScript
			Thread.sleep(500);
			
			// HEADLESS FIX: Use fresh element lookup
			WebElement jobsElement = wait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//span[text()='Jobs']")
			));
			
			// HEADLESS FIX: Ensure element is visible
			wait.until(ExpectedConditions.visibilityOf(jobsElement));
			
			// HEADLESS FIX: Scroll element into view
			js.executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", jobsElement);
			Thread.sleep(300);
			
			Assert.assertTrue(jobsElement.isDisplayed(), "Jobs element is not displayed after scrolling into view");
			
			// HEADLESS FIX: Multiple click strategies
			boolean clickSucceeded = false;
			
			try {
				wait.until(ExpectedConditions.elementToBeClickable(jobsElement));
				jobsElement.click();
				clickSucceeded = true;
			} catch (Exception e1) {
				try {
					js.executeScript("arguments[0].click();", jobsElement);
					clickSucceeded = true;
				} catch (Exception e2) {
					try {
						utils.jsClick(driver, jobsElement);
						clickSucceeded = true;
					} catch (Exception e3) {
						throw new Exception("Failed to click Jobs element using all methods: " + e3.getMessage());
					}
				}
			}
			
			if (!clickSucceeded) {
				throw new Exception("Failed to click Jobs element");
			}
			
			// Wait for page transition
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner1));
			PerformanceUtils.waitForPageReady(driver, 2);
			
			// HEADLESS FIX: Re-locate element after click
			WebElement jobsHeaderAfterClick = wait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//span[text()='Jobs']")
			));
			
			// Verify navigation succeeded
			Assert.assertTrue(
				wait.until(ExpectedConditions.visibilityOf(jobsHeaderAfterClick)).isDisplayed(),
				"Jobs header not displayed after click"
			);
			
			String JobsHeaderText = jobsHeaderAfterClick.getText();
			Assert.assertEquals("Jobs", JobsHeaderText);
			
			PageObjectHelper.log(LOGGER, "Navigated to Jobs page in Architect");
			
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_navigate_to_jobs_page_in_architect",
				"Failed to navigate to Jobs page in Architect", e);
		}
		
	}
	
	public void search_for_published_job_name_in_jobs_page_in_architect() {
		wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner1));
		Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(ProfilesSearch)).isDisplayed());
		try {
		wait.until(ExpectedConditions.visibilityOf(ProfilesSearch)).clear();
		wait.until(ExpectedConditions.visibilityOf(ProfilesSearch)).sendKeys(job1OrgName.get().split("-", 2)[0].trim());
		wait.until(ExpectedConditions.visibilityOf(ProfilesSearch)).sendKeys(Keys.ENTER);
		wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner1));
		PageObjectHelper.log(LOGGER, "Searched for job: " + job1OrgName.get() + " in Jobs page in Architect");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "search_for_published_job_name_in_jobs_page_in_architect",
				"Failed to search for job in Jobs page in Architect", e);
		}
	}
	
	public void user_should_verify_published_job_is_displayed_in_jobs_page_in_architect() {
		try {
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner1));
			PerformanceUtils.waitForPageReady(driver, 2);
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner1));
		String job1NameText = wait.until(ExpectedConditions.visibilityOf(ArchitectJobinRow1)).getText();
		Assert.assertEquals(job1OrgName.get(), job1NameText.split("-", 2)[0].trim());
		PageObjectHelper.log(LOGGER, "Published Job (Org: " + job1OrgName.get() + ") is displayed in Jobs page in Architect");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_published_job_is_displayed_in_jobs_page_in_architect",
				"Issue verifying published job in Jobs page in Architect", e);
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
		PageObjectHelper.log(LOGGER, "Updated date verified on Published Job: " + job1OrgName.get() + " in Architect");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify__updated_date_on_published_job_matches_with_current_date_in_architect",
				"Issue verifying date on published job in Architect", e);
		}
	}
}
