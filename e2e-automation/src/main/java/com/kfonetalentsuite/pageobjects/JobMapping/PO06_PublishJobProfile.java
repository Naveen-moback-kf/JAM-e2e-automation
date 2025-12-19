package com.kfonetalentsuite.pageobjects.JobMapping;

import java.time.Duration;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.ScreenshotHandler;
import com.kfonetalentsuite.utils.JobMapping.PageObjectHelper;

public class PO06_PublishJobProfile extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO06_PublishJobProfile.class);

	public static ThreadLocal<String> job1OrgName = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<String> job1OrgCode = ThreadLocal.withInitial(() -> "NOT_SET");

	// ============================================================
	// LOCATORS - Using centralized Locators from BasePageObject where available
	// ============================================================
	
	// JAM Table Rows - from Locators.JobMappingResults
	private static final By JOB_NAME_ROW_1 = Locators.JobMappingResults.JOB_NAME_ROW_1;
	private static final By JOB_1_PUBLISH_BTN = Locators.JobMappingResults.JOB_1_PUBLISH_BTN;
	private static final By JOB_1_PUBLISHED_BTN = Locators.JobMappingResults.JOB_1_PUBLISHED_BTN;
	
	// Publish Success Messages - from Locators.Modals
	private static final By PUBLISH_SUCCESS_MSG = Locators.Modals.PUBLISH_SUCCESS_MSG;
	private static final By PUBLISH_SUCCESS_MSG_DIRECT = Locators.Modals.PUBLISH_SUCCESS_MSG_DIRECT;
	private static final By PUBLISH_SUCCESS_MSG_FLEXIBLE = Locators.Modals.PUBLISH_SUCCESS_MSG_FLEXIBLE;
	
	// HCM Sync Profiles - from Locators.HCMSyncProfiles
	private static final By HCM_SYNC_TAB = Locators.HCMSyncProfiles.HCM_SYNC_TAB;
	private static final By HCM_SYNC_HEADER = Locators.HCMSyncProfiles.SYNC_PROFILES_TITLE;
	private static final By PM_HEADER = Locators.HCMSyncProfiles.PROFILE_MANAGER_HEADER;
	
	// HCM/Architect Row Locators - from Locators.JobMappingResults
	private static final By HCM_JOB_ROW_1 = Locators.JobMappingResults.HCM_JOB_ROW_1;
	private static final By HCM_DATE_ROW_1 = Locators.JobMappingResults.HCM_DATE_ROW_1;
	private static final By ARCHITECT_JOB_ROW_1 = Locators.JobMappingResults.ARCHITECT_JOB_ROW_1;
	private static final By ARCHITECT_DATE_ROW_1 = Locators.JobMappingResults.ARCHITECT_DATE_ROW_1;
	
	// Navigation - from Locators.Navigation
	private static final By KFONE_MENU_PM_BTN = Locators.Navigation.KFONE_MENU_PM_BTN;
	private static final By KFONE_MENU_ARCHITECT_BTN = Locators.Navigation.KFONE_MENU_ARCHITECT_BTN;
	
	// Search - from Locators.PMScreen (HCM uses different search bar)
	private static final By PROFILES_SEARCH = Locators.PMScreen.SEARCH_BAR;
	
	// Local locators (not commonly used elsewhere)
	private static final By SP_DETAILS_TEXT = By.xpath("//span[contains(text(),'Select your view')]");
	private static final By JOBS_LINK = By.xpath("//span[text()='Jobs']");
	
	// Comparison Screen locators - from Locators.ComparisonPage
	private static final By COMPARE_SELECT_HEADER = Locators.ComparisonPage.COMPARE_HEADER;
	private static final By JC_ORG_JOB_TITLE = By.xpath("//div[contains(@class, 'text-[24px] font-semibold')] | //h2[contains(@class, 'job-title')]");
	private static final By JC_PUBLISH_SELECT_BTN = By.xpath("//button[@id='publish-select-btn']");
	private static final By SELECT_BTNS_IN_JC = Locators.ComparisonPage.CARD_HEADER;
	
	// Profile Details Popup - from Locators.ProfileDetails
	private static final By POPUP_PUBLISH_PROFILE_BTN = Locators.ProfileDetails.PUBLISH_PROFILE_BTN;

	public PO06_PublishJobProfile() {
		super();
	}

	public void user_should_verify_publish_button_is_displaying_on_first_job_profile() {
		try {
			PerformanceUtils.waitForPageReady(driver, 3);
			waitForSpinners();
			safeSleep(1000);
			
			WebElement jobNameElement = waitForElement(JOB_NAME_ROW_1, 10);
			String job1NameText = jobNameElement.getText();
			
			if (job1NameText == null || job1NameText.trim().isEmpty()) {
				throw new Exception("Job name element text is null or empty");
			}
			
			// Extract job name (before the dash) and job code (within parentheses)
			String[] parts = job1NameText.split("-", 2);
			String extractedJobName = parts[0].trim();
			String extractedJobCode = "NOT_SET";
			
			// Extract job code from the second part (e.g., "(ABC123)" -> "ABC123")
			if (parts.length > 1) {
				String codePart = parts[1].trim();
				if (codePart.startsWith("(") && codePart.contains(")")) {
					extractedJobCode = codePart.substring(1, codePart.indexOf(")")).trim();
				}
			}
			
			if (extractedJobName.isEmpty() || extractedJobName.equals("NOT_SET")) {
				throw new Exception("Failed to extract valid job name from: " + job1NameText);
			}
			
			// Store job name and code in ThreadLocal variables
			job1OrgName.set(extractedJobName);
			job1OrgCode.set(extractedJobCode);
			PO15_ValidateRecommendedProfileDetails.orgJobName.set(extractedJobName);
			PO15_ValidateRecommendedProfileDetails.orgJobCode.set(extractedJobCode);
			
			LOGGER.info("Job name extracted and stored: '{}', Job code: '{}'", extractedJobName, extractedJobCode);
			
			Assert.assertTrue(waitForElement(JOB_1_PUBLISH_BTN).isDisplayed());
			PageObjectHelper.log(LOGGER, "Publish button on first job profile (Org: " + job1OrgName.get() + ", Code: " + job1OrgCode.get() + ") is displaying");
		} catch (Exception e) {
			ScreenshotHandler.handleTestFailure("verify_publish_btn_on_first_job_profile", e, "Issue in verifying Publish button on first job");
		}
	}

	public void click_on_publish_button_on_first_job_profile() {
		try {
			clickElement(JOB_1_PUBLISH_BTN);
			PageObjectHelper.log(LOGGER, "Clicked Publish button on first job (Org: " + job1OrgName.get() + ")");
			waitForSpinners();
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_publish_button_on_first_job_profile", "Issue clicking Publish button on first job", e);
		}
	}

	public void user_should_verify_publish_success_popup_appears_on_screen() {
		try {
			waitForSpinners();

			WebElement successElement = null;
			boolean primarySuccess = false;

			try {
				successElement = waitForElement(PUBLISH_SUCCESS_MSG);
				primarySuccess = true;
			} catch (Exception e1) {
				try {
					successElement = waitForElement(PUBLISH_SUCCESS_MSG_DIRECT);
				} catch (Exception e2) {
					try {
						successElement = waitForElement(PUBLISH_SUCCESS_MSG_FLEXIBLE);
					} catch (Exception e3) {
						try {
							successElement = waitForElement(By.xpath("//*[contains(@class,'success') or contains(@class,'alert-success') or contains(text(),'Success')]"));
						} catch (Exception e4) {
							ScreenshotHandler.captureScreenshotWithDescription("publish_success_popup_not_found");
							throw new RuntimeException("Success popup not found using any locator strategy");
						}
					}
				}
			}

			Assert.assertTrue(successElement.isDisplayed(), "Success popup element is not displayed");
			String publishSuccessMsgText = successElement.getText();
			PageObjectHelper.log(LOGGER, "Success message: " + publishSuccessMsgText);

			try {
				WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(10));
				if (primarySuccess) {
					shortWait.until(ExpectedConditions.invisibilityOfElementLocated(PUBLISH_SUCCESS_MSG));
				} else {
					shortWait.until(ExpectedConditions.invisibilityOf(successElement));
				}
			} catch (TimeoutException te) {
				LOGGER.warn("Success popup did not auto-dismiss");
			}

			waitForSpinners();
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_publish_success_popup_appears_on_screen", "Issue verifying Publish Success popup", e);
		}
	}

	public void click_on_view_published_toggle_button_to_turn_on() {
		try {
			PerformanceUtils.waitForPageReady(driver, 3);
			WebElement toggle = driver.findElement(Locators.Actions.VIEW_PUBLISHED_TOGGLE);
			if ("true".equals(toggle.getAttribute("aria-checked")) || toggle.isSelected()) {
				PageObjectHelper.log(LOGGER, "View published toggle button is already ON");
			} else {
				clickElement(Locators.Actions.VIEW_PUBLISHED_TOGGLE);
				PerformanceUtils.waitForPageReady(driver, 3);
				PageObjectHelper.log(LOGGER, "View published toggle button is turned ON");
				waitForBackgroundDataLoad();
				PO17_ValidateSortingFunctionality_JAM.jobNamesTextInDefaultOrder.get().clear();
			}
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_view_published_toggle_button_to_turn_on", "Issue clicking View Published toggle button", e);
		}
	}

	public void search_for_published_job_name_in_view_published_screen() {
		try {
			String jobName = getJobNameToSearch();
			if (jobName == null || jobName.isEmpty()) {
				throw new Exception("Job name to search is null or empty");
			}
			
			LOGGER.info("Searching for published job with name: '{}'", jobName);
			
			waitForSpinners();
			PerformanceUtils.waitForPageReady(driver, 3);
			scrollToTop();
			safeSleep(500);
			
			WebElement searchBar = waitForElement(Locators.SearchAndFilters.SEARCH_BAR, 10);
			scrollToElement(searchBar);
			
			// Clear any existing search
			searchBar.click();
			safeSleep(300);
			searchBar.clear();
			searchBar.sendKeys(Keys.CONTROL + "a");
			searchBar.sendKeys(Keys.DELETE);
			safeSleep(300);
			
			// Type search term and submit
			searchBar.sendKeys(jobName);
			safeSleep(500);
			searchBar.sendKeys(Keys.ENTER);
			
			// Wait for search to complete
			waitForSpinners();
			PerformanceUtils.waitForPageReady(driver, 5);
			safeSleep(1500);
			
			LOGGER.info("Search completed for job: '{}'", jobName);
			PageObjectHelper.log(LOGGER, "Searched for job: " + jobName + " in View Published screen");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "search_for_published_job_name_in_view_published_screen", "Failed to search for job in View Published screen", e);
		}
	}

	public void user_should_verify_published_job_is_displayed_in_view_published_screen() throws Exception {
		try {
			String expectedJobName = PO15_ValidateRecommendedProfileDetails.orgJobName.get();
			if (expectedJobName == null || expectedJobName.isEmpty()) {
				throw new Exception("Expected job name is null or empty");
			}
			
			// Wait for search results to filter
			PerformanceUtils.waitForPageReady(driver, 3);
			waitForSpinners();
			safeSleep(2000);
			
			WebElement jobNameElement = waitForElement(JOB_NAME_ROW_1, 10);
			String job1NameText = jobNameElement.getText();
			String actualJobName = job1NameText.split("-", 2)[0].trim();
			
			if (!expectedJobName.equals(actualJobName)) {
				String errorMsg = String.format("Expected job '%s' but found '%s'. Search may not have filtered correctly.", 
					expectedJobName, actualJobName);
				ScreenshotHandler.captureFailureScreenshot("user_should_verify_published_job_is_displayed_in_view_published_screen", 
					new Exception(errorMsg));
				Assert.fail(errorMsg);
			}
			
			job1OrgName.set(actualJobName);
			PageObjectHelper.log(LOGGER, "Found expected job: " + actualJobName);
			
			Assert.assertTrue(waitForElement(JOB_1_PUBLISHED_BTN).isDisplayed());
			PageObjectHelper.log(LOGGER, "Published Job (Org: " + job1OrgName.get() + ") is displayed in view published screen");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_published_job_is_displayed_in_view_published_screen", "Issue verifying Published Job", e);
		}
	}

	public void user_should_navigate_to_hcm_sync_profiles_tab_in_pm() {
		try {
			clickElement(HCM_SYNC_TAB);
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 5);
			Assert.assertEquals("HCM Sync Profiles", getElementText(HCM_SYNC_HEADER));
			PageObjectHelper.log(LOGGER, "Navigated to HCM Sync Profiles screen in PM");
			// Wait for background API (~100K records) to complete
			waitForBackgroundDataLoad();
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_navigate_to_hcm_sync_profiles_tab_in_pm", "Issue navigating to HCM Sync Profiles screen", e);
		}
	}

	public void search_for_published_job_name_in_hcm_sync_profiles_tab_in_pm() {
		try {
			String jobName = getJobNameToSearch();
			PerformanceUtils.waitForPageReady(driver, 5);
			WebElement searchBox = waitForElement(PROFILES_SEARCH);
			String searchTerm = jobName.split("-", 2)[0].trim();
			searchBox.clear();
			searchBox.sendKeys(searchTerm);
			searchBox.sendKeys(Keys.ENTER);
			PerformanceUtils.waitForPageReady(driver, 5);
			PageObjectHelper.log(LOGGER, "Searched for job: " + searchTerm + " in HCM Sync Profiles");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "search_for_published_job_name_in_hcm_sync_profiles_tab_in_pm", "Failed to search for job in HCM Sync Profiles", e);
		}
	}

	public void user_should_verify_published_job_is_displayed_in_hcm_sync_profiles_tab_in_pm() {
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 5);
			safeSleep(2000);
			
			String expectedJobName = getJobNameToSearch();
			if (expectedJobName == null || expectedJobName.isEmpty()) {
				throw new Exception("Expected job name is null or empty");
			}
			
			waitForSpinners();
			
			WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(15));
			WebElement jobElement = shortWait.until(ExpectedConditions.visibilityOfElementLocated(HCM_JOB_ROW_1));
			String job1NameText = jobElement.getText();
			String actualJobName = job1NameText.split("-", 2)[0].trim();
			
			if (!expectedJobName.equals(actualJobName)) {
				String errorMsg = String.format("Expected job '%s' but found '%s' in HCM Sync Profiles.", expectedJobName, actualJobName);
				ScreenshotHandler.captureFailureScreenshot("user_should_verify_published_job_is_displayed_in_hcm_sync_profiles_tab_in_pm", 
					new Exception(errorMsg));
				Assert.fail(errorMsg);
			}
			
			PageObjectHelper.log(LOGGER, "Published Job (Org: " + actualJobName + ") is displayed in HCM Sync Profiles");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_published_job_is_displayed_in_hcm_sync_profiles_tab_in_pm", "Issue verifying published job in HCM Sync Profiles", e);
		}
	}

	public void user_should_verify_date_on_published_job_matches_with_current_date() {
		try {
			String todayDate = formatDateForDisplay();
			String jobPublishedDate = getElementText(HCM_DATE_ROW_1);
			Assert.assertEquals(jobPublishedDate, todayDate);
			PageObjectHelper.log(LOGGER, "Last Modified date verified on Published Job: " + job1OrgName.get());
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_date_on_published_job_matches_with_current_date", "Issue verifying date on published job", e);
		}
	}

	public void user_should_verify_sp_details_page_opens_on_click_of_published_job_name() {
		try {
			clickElement(HCM_JOB_ROW_1);
			PageObjectHelper.log(LOGGER, "Clicked Published Job (Org: " + job1OrgName.get() + ") in HCM Sync Profiles");
			PerformanceUtils.waitForPageReady(driver, 5);
			Assert.assertTrue(waitForElement(SP_DETAILS_TEXT).isDisplayed());
			LOGGER.info("SP details page opened on click of Published Job name");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_sp_details_page_opens_on_click_of_published_job_name", "Issue navigating to SP details page", e);
		}
	}

	public void click_on_kfone_global_menu_in_job_mapping_ui() {
		try {
			waitForSpinners();
			PerformanceUtils.waitForPageReady(driver, 5);
			clickElement(Locators.Navigation.GLOBAL_NAV_MENU_BTN);
			PageObjectHelper.log(LOGGER, "Clicked KFONE Global Menu in Job Mapping UI");
			PerformanceUtils.waitForPageReady(driver, 2);
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_kfone_global_menu_in_job_mapping_ui", "Issue clicking KFone Global Menu", e);
		}
	}

	public void click_on_profile_manager_application_button_in_kfone_global_menu() {
		try {
			waitForSpinners();
			PerformanceUtils.waitForPageReady(driver, 3);

			WebDriverWait extendedWait = new WebDriverWait(driver, Duration.ofSeconds(30));
			WebElement pmBtn = extendedWait.until(ExpectedConditions.visibilityOfElementLocated(KFONE_MENU_PM_BTN));
			scrollToElement(pmBtn);
			pmBtn = extendedWait.until(ExpectedConditions.elementToBeClickable(pmBtn));

			String applicationNameText = pmBtn.getAttribute("aria-label");
			PageObjectHelper.log(LOGGER, applicationNameText + " application is displaying in KFONE Global Menu");

			clickElement(pmBtn);
			PageObjectHelper.log(LOGGER, "Successfully clicked Profile Manager application button");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_profile_manager_application_button_in_kfone_global_menu", "Issue clicking Profile Manager button", e);
		}
	}

	public void verify_user_should_land_on_profile_manager_dashboard_page() {
		try {
			PerformanceUtils.waitForPageReady(driver, 5);
			String PMHeaderText = getElementText(PM_HEADER);
			PageObjectHelper.log(LOGGER, "User landed on " + PMHeaderText + " Dashboard Page");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_user_should_land_on_profile_manager_dashboard_page", "Issue landing on Profile Manager dashboard", e);
		}
	}

	public void click_on_architect_application_button_in_kfone_global_menu() {
		try {
			waitForSpinners();
			PerformanceUtils.waitForPageReady(driver, 3);

			WebDriverWait extendedWait = new WebDriverWait(driver, Duration.ofSeconds(30));
			WebElement architectBtn = extendedWait.until(ExpectedConditions.visibilityOfElementLocated(KFONE_MENU_ARCHITECT_BTN));
			scrollToElement(architectBtn);
			architectBtn = extendedWait.until(ExpectedConditions.elementToBeClickable(architectBtn));

			String applicationNameText = architectBtn.getAttribute("aria-label");
			PageObjectHelper.log(LOGGER, applicationNameText + " application is displaying in KFONE Global Menu");

			clickElement(architectBtn);
			PageObjectHelper.log(LOGGER, "Successfully clicked Architect application button");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_architect_application_button_in_kfone_global_menu", "Issue clicking Architect button", e);
		}
	}

	public void verify_user_should_land_on_architect_dashboard_page() {
		try {
			PerformanceUtils.waitForPageReady(driver, 10);
			PageObjectHelper.log(LOGGER, "User landed on Architect Dashboard Page");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_user_should_land_on_architect_dashboard_page", "Issue landing on Architect dashboard", e);
		}
	}

	public void user_should_navigate_to_jobs_page_in_architect() {
		try {
			clickElement(JOBS_LINK);
			PerformanceUtils.waitForPageReady(driver, 5);
			Assert.assertEquals("Jobs", getElementText(JOBS_LINK));
			PageObjectHelper.log(LOGGER, "Navigated to Jobs page in Architect");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_navigate_to_jobs_page_in_architect", "Failed to navigate to Jobs page in Architect", e);
		}
	}

	public void search_for_published_job_name_in_jobs_page_in_architect() {
		try {
			String jobName = getJobNameToSearch();

			WebElement searchBox = waitForElement(PROFILES_SEARCH);
			String searchTerm = jobName.split("-", 2)[0].trim();
			searchBox.clear();
			searchBox.sendKeys(searchTerm);
			searchBox.sendKeys(Keys.ENTER);
			PerformanceUtils.waitForPageReady(driver, 5);
			PageObjectHelper.log(LOGGER, "Searched for job: " + searchTerm + " in Jobs page in Architect");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "search_for_published_job_name_in_jobs_page_in_architect", "Failed to search for job in Architect", e);
		}
	}

	public void user_should_verify_published_job_is_displayed_in_jobs_page_in_architect() throws Exception {
		try {
			String expectedJobName = getJobNameToSearch();
			if (expectedJobName == null || expectedJobName.isEmpty()) {
				throw new Exception("Expected job name is null or empty");
			}
			
			// Wait for search results to filter
			PerformanceUtils.waitForPageReady(driver, 3);
			waitForSpinners();
			safeSleep(2000);
			
			WebElement jobNameElement = waitForElement(ARCHITECT_JOB_ROW_1, 10);
			String job1NameText = jobNameElement.getText();
			String actualJobName = job1NameText.split("-", 2)[0].trim();
			
			if (!expectedJobName.equals(actualJobName)) {
				String errorMsg = String.format("Expected job '%s' but found '%s' in Architect.", expectedJobName, actualJobName);
				ScreenshotHandler.captureFailureScreenshot("user_should_verify_published_job_is_displayed_in_jobs_page_in_architect", 
					new Exception(errorMsg));
				Assert.fail(errorMsg);
			}
			
			PageObjectHelper.log(LOGGER, "Published Job (Org: " + expectedJobName + ") is displayed in Jobs page in Architect");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_published_job_is_displayed_in_jobs_page_in_architect", "Issue verifying published job in Architect", e);
		}
	}

	public void user_should_verify__updated_date_on_published_job_matches_with_current_date_in_architect() {
		try {
			String todayDate = formatDateForDisplay();
			String jobPublishedDate = getElementText(ARCHITECT_DATE_ROW_1);
			Assert.assertEquals(jobPublishedDate, todayDate);
			PageObjectHelper.log(LOGGER, "Updated date verified on Published Job: " + job1OrgName.get() + " in Architect");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify__updated_date_on_published_job_matches_with_current_date_in_architect", "Issue verifying date in Architect", e);
		}
	}

	// formatDateForDisplay() removed - now using inherited method from BasePageObject

	private String getJobNameToSearch() {
		String jobName = job1OrgName.get();
		
		// Check if job name is valid (not null, empty, or "NOT_SET")
		if (jobName == null || jobName.isEmpty() || jobName.equals("NOT_SET")) {
			jobName = PO15_ValidateRecommendedProfileDetails.orgJobName.get();
		}
		
		// Validate job name is valid
		if (jobName == null || jobName.isEmpty() || jobName.equals("NOT_SET")) {
			LOGGER.error("Job name not properly set. job1OrgName='{}', PO15.orgJobName='{}'", 
				job1OrgName.get(), PO15_ValidateRecommendedProfileDetails.orgJobName.get());
			throw new IllegalStateException("Job name is not set or is 'NOT_SET'. Ensure job name is extracted before publishing.");
		}
		
		// Ensure both ThreadLocal variables are synchronized
		if (!jobName.equals(job1OrgName.get())) {
			job1OrgName.set(jobName);
		}
		if (!jobName.equals(PO15_ValidateRecommendedProfileDetails.orgJobName.get())) {
			PO15_ValidateRecommendedProfileDetails.orgJobName.set(jobName);
		}
		
		LOGGER.debug("Using job name for search: '{}'", jobName);
		return jobName;
	}

	/**
	 * Gets the job code to use for validation across different screens.
	 * Checks both PO06 and PO15 ThreadLocal variables for valid job code.
	 * @return The job code string
	 */
	public String getJobCodeForValidation() {
		String jobCode = job1OrgCode.get();
		
		// Check if job code is valid (not null, empty, or "NOT_SET")
		if (jobCode == null || jobCode.isEmpty() || jobCode.equals("NOT_SET")) {
			jobCode = PO15_ValidateRecommendedProfileDetails.orgJobCode.get();
		}
		
		// Validate job code is valid
		if (jobCode == null || jobCode.isEmpty() || jobCode.equals("NOT_SET")) {
			LOGGER.warn("Job code not properly set. job1OrgCode='{}', PO15.orgJobCode='{}'", 
				job1OrgCode.get(), PO15_ValidateRecommendedProfileDetails.orgJobCode.get());
			return "NOT_SET";
		}
		
		// Ensure both ThreadLocal variables are synchronized
		if (!jobCode.equals(job1OrgCode.get())) {
			job1OrgCode.set(jobCode);
		}
		if (!jobCode.equals(PO15_ValidateRecommendedProfileDetails.orgJobCode.get())) {
			PO15_ValidateRecommendedProfileDetails.orgJobCode.set(jobCode);
		}
		
		LOGGER.debug("Using job code for validation: '{}'", jobCode);
		return jobCode;
	}

	// ============================================================
	// Methods moved from PO07_PublishJobFromComparisonScreen
	// ============================================================

	public void verify_user_landed_on_job_comparison_screen() {
		try {
			PerformanceUtils.waitForPageReady(driver, 10);
			String compareAndSelectHeaderText = getElementText(COMPARE_SELECT_HEADER);
			Assert.assertEquals(compareAndSelectHeaderText, "Which profile do you want to use for this job?");
			PageObjectHelper.log(LOGGER, "User landed on the Job Comparison screen successfully");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_user_landed_on_job_comparison_screen", "Issue in landing Job Comparison screen", e);
		}
	}

	public void select_second_profile_from_ds_suggestions_of_organization_job() {
		try {
			PerformanceUtils.waitForPageReady(driver, 2);
			List<WebElement> selectBtns = driver.findElements(SELECT_BTNS_IN_JC);

			for (int i = 0; i < selectBtns.size(); i++) {
				if (i == 2) {
					WebElement btn = selectBtns.get(i);
					wait.until(ExpectedConditions.visibilityOf(btn));
					if (!tryClickWithStrategies(btn)) {
						jsClick(btn);
					}
					String jobTitle = getElementText(JC_ORG_JOB_TITLE);
					PageObjectHelper.log(LOGGER, "Second Profile selected from DS Suggestions for job: " + jobTitle);
					break;
				}
			}
			PerformanceUtils.waitForUIStability(driver, 1);
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "select_second_profile_from_ds_suggestions_of_organization_job", "Issue in selecting Second Profile from DS Suggestions", e);
		}
	}

	public void click_on_publish_selected_button_in_job_comparison_page() {
		try {
			String jobTitle = getElementText(JC_ORG_JOB_TITLE);
			String[] parts = jobTitle.split("-", 2);
			String extractedJobName = parts[0].trim();
			String extractedJobCode = "NOT_SET";
			
			// Extract job code from the second part (e.g., "(ABC123)" -> "ABC123")
			if (parts.length > 1) {
				String codePart = parts[1].trim();
				if (codePart.startsWith("(") && codePart.contains(")")) {
					extractedJobCode = codePart.substring(1, codePart.indexOf(")")).trim();
				}
			}
			
			// Store job name and code in ThreadLocal variables
			job1OrgName.set(extractedJobName);
			job1OrgCode.set(extractedJobCode);
			PO15_ValidateRecommendedProfileDetails.orgJobName.set(extractedJobName);
			PO15_ValidateRecommendedProfileDetails.orgJobCode.set(extractedJobCode);
			
			clickElement(JC_PUBLISH_SELECT_BTN);
			waitForSpinners();
			PerformanceUtils.waitForPageReady(driver, 5);
			
			PageObjectHelper.log(LOGGER, "Clicked Publish Selected button for job: " + job1OrgName.get() + " (Code: " + job1OrgCode.get() + ")");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_publish_selected_button_in_job_comparison_page", "Issue clicking Publish Selected button", e);
		}
	}

	// ============================================================
	// Methods moved from PO08_PublishJobFromDetailsPopup
	// ============================================================

	public void user_is_on_profile_details_popup() {
		PageObjectHelper.log(LOGGER, "User is on Profile details Popup");
	}

	public void click_on_publish_profile_button_in_profile_details_popup() {
		try {
			clickElement(POPUP_PUBLISH_PROFILE_BTN);
			PageObjectHelper.log(LOGGER, "Clicked Publish Profile button in Profile Details popup");
			waitForSpinners();
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_publish_profile_button_in_profile_details_popup", "Failed to click Publish Profile button in popup", e);
		}
	}
}
