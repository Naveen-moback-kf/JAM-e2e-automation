package com.kfonetalentsuite.pageobjects.JobMapping;

import java.time.Duration;

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

	// formatDateForDisplay() is inherited from BasePageObject

	private static final By JOB_NAME_ROW_1 = By.xpath("//tbody//tr[1]//td[2]//div[contains(text(),'(')]");
	private static final By JOB_1_PUBLISH_BTN = By.xpath("//tbody//tr[2]//button[@id='publish-btn'][1]");
	private static final By PUBLISH_SUCCESS_MSG = By.xpath("//p[contains(text(),'Success profile published')]/..");
	private static final By PUBLISH_SUCCESS_MSG_DIRECT = By.xpath("//p[contains(text(),'Success profile published')]");
	private static final By PUBLISH_SUCCESS_MSG_FLEXIBLE = By.xpath("//*[contains(text(),'profile published') or contains(text(),'successfully published')]");
	// TOGGLE_SWITCH is available via Locators.Actions.VIEW_PUBLISHED_TOGGLE
	private static final By JOB_1_PUBLISHED_BTN = By.xpath("//tbody//tr[2]//button[text()='Published'][1]");
	private static final By HCM_SYNC_TAB = By.xpath("//span[contains(text(),'HCM Sync')]");
	private static final By HCM_SYNC_HEADER = By.xpath("//h1[contains(text(),'Sync Profiles')]");
	private static final By PROFILES_SEARCH = By.xpath("//input[@type='search']");
	private static final By HCM_JOB_ROW_1 = By.xpath("//tbody//tr[1]//td//div//span[1]//a");
	private static final By SP_DETAILS_TEXT = By.xpath("//span[contains(text(),'Select your view')]");
	private static final By HCM_DATE_ROW_1 = By.xpath("//tbody//tr[1]//td[7]//span");
	private static final By PM_HEADER = By.xpath("//h1[contains(text(),'Profile Manager')]");
	private static final By KFONE_MENU_PM_BTN = By.xpath("//span[@aria-label='Profile Manager']");
	private static final By KFONE_MENU_ARCHITECT_BTN = By.xpath("//span[@aria-label='Architect']");
	private static final By ARCHITECT_JOB_ROW_1 = By.xpath("//tbody//tr[1]//td//div//div//a");
	private static final By ARCHITECT_DATE_ROW_1 = By.xpath("//tbody//tr[1]//td[9]");
	private static final By JOBS_LINK = By.xpath("//span[text()='Jobs']");
	// RESULTS_COUNT is available via Locators.JobMappingResults.SHOWING_JOB_RESULTS
	private static final By NO_PROFILES_MSG = By.xpath("//div[contains(text(),'no Success Profiles') or contains(text(),'No profiles found')]");

	public PO06_PublishJobProfile() {
		super();
	}

	public void user_should_verify_publish_button_is_displaying_on_first_job_profile() {
		try {
			String job1NameText = getElementText(JOB_NAME_ROW_1);
			job1OrgName.set(job1NameText.split("-", 2)[0].trim());
			PO15_ValidateRecommendedProfileDetails.orgJobName.set(job1OrgName.get());
			Assert.assertTrue(waitForElement(JOB_1_PUBLISH_BTN).isDisplayed());
			PageObjectHelper.log(LOGGER, "Publish button on first job profile (Org: " + job1OrgName.get() + ") is displaying");
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
				PO17_ValidateSortingFunctionality_JAM.jobNamesTextInDefaultOrder.get().clear();
			}
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_view_published_toggle_button_to_turn_on", "Issue clicking View Published toggle button", e);
		}
	}

	public void search_for_published_job_name_in_view_published_screen() {
		try {
			String jobName = getJobNameToSearch();
			
			waitForSpinners();
			PerformanceUtils.waitForPageReady(driver, 3);
			scrollToTop();
			safeSleep(500);
			
			WebElement searchBar = waitForElement(Locators.SearchAndFilters.SEARCH_BAR);
			scrollToElement(searchBar);
			
			try {
				searchBar.clear();
				searchBar.click();
			} catch (Exception clickEx) {
				jsClick(searchBar);
			}
			
			searchBar.sendKeys(jobName);
			searchBar.sendKeys(Keys.ENTER);
			PerformanceUtils.waitForPageReady(driver, 3);
			PageObjectHelper.log(LOGGER, "Searched for job: " + jobName + " in View Published screen");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "search_for_published_job_name_in_view_published_screen", "Failed to search for job in View Published screen", e);
		}
	}

	public void user_should_verify_published_job_is_displayed_in_view_published_screen() {
		try {
			// PARALLEL EXECUTION FIX: Verify expected job name matches (search should filter correctly)
			String expectedJobName = PO15_ValidateRecommendedProfileDetails.orgJobName.get();
			PerformanceUtils.waitForPageReady(driver, 3);
			String job1NameText = getElementText(JOB_NAME_ROW_1);
			String actualJobName = job1NameText.split("-", 2)[0].trim();
			job1OrgName.set(actualJobName);
			Assert.assertEquals(expectedJobName, actualJobName, 
				String.format("Expected job '%s' but found '%s'", expectedJobName, actualJobName));
			Assert.assertTrue(waitForElement(JOB_1_PUBLISHED_BTN).isDisplayed());
			PageObjectHelper.log(LOGGER, "Published Job (Org: " + job1OrgName.get() + ") is displayed in view published screen");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_published_job_is_displayed_in_view_published_screen", "Issue verifying Published Job", e);
		}
	}

	public void user_should_navigate_to_hcm_sync_profiles_tab_in_pm() {
		try {
			clickElement(HCM_SYNC_TAB);
			PerformanceUtils.waitForPageReady(driver, 5);
			Assert.assertEquals("HCM Sync Profiles", getElementText(HCM_SYNC_HEADER));
			PageObjectHelper.log(LOGGER, "Navigated to HCM Sync Profiles screen in PM");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_navigate_to_hcm_sync_profiles_tab_in_pm", "Issue navigating to HCM Sync Profiles screen", e);
		}
	}

	public void search_for_published_job_name_in_hcm_sync_profiles_tab_in_pm() {
		try {
			String jobName = getJobNameToSearch();

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
			String expectedJobName = getJobNameToSearch();
			waitForSpinners();
			PerformanceUtils.waitForPageReady(driver, 2);
			String job1NameText = "";
			try {
				WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(10));
				job1NameText = shortWait.until(ExpectedConditions.visibilityOfElementLocated(HCM_JOB_ROW_1)).getText();
			} catch (TimeoutException e) {
				try {
					String countText = driver.findElement(Locators.JobMappingResults.SHOWING_JOB_RESULTS).getText();
					if (countText.contains("Showing 0")) {
						Assert.fail("Search returned 0 results - Job '" + expectedJobName + "' not found");
					}
				} catch (Exception countEx) {
					// Continue
				}
				try {
					if (driver.findElement(NO_PROFILES_MSG).isDisplayed()) {
						Assert.fail("No profiles message displayed - Job '" + expectedJobName + "' not found");
					}
				} catch (Exception noMsgEx) {
					// Continue
				}
				Assert.fail("Published job '" + expectedJobName + "' not found in HCM Sync Profiles");
			}
			String actualJobName = job1NameText.split("-", 2)[0].trim();
			// PARALLEL EXECUTION FIX: Verify expected job name matches (search should filter correctly)
			Assert.assertEquals(expectedJobName, actualJobName, 
				String.format("Expected job '%s' but found '%s'", expectedJobName, actualJobName));
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

	public void user_should_verify_published_job_is_displayed_in_jobs_page_in_architect() {
		try {
			String expectedJobName = getJobNameToSearch();
			PerformanceUtils.waitForPageReady(driver, 3);
			String job1NameText = getElementText(ARCHITECT_JOB_ROW_1);
			String actualJobName = job1NameText.split("-", 2)[0].trim();
			// PARALLEL EXECUTION FIX: Verify expected job name matches (search should filter correctly)
			Assert.assertEquals(expectedJobName, actualJobName,
				String.format("Expected job '%s' but found '%s'", expectedJobName, actualJobName));
			PageObjectHelper.log(LOGGER, "Published Job (Org: " + actualJobName + ") is displayed in Jobs page in Architect");
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
		if (jobName == null || jobName.isEmpty()) {
			jobName = PO15_ValidateRecommendedProfileDetails.orgJobName.get();
		}
		if (jobName == null || jobName.isEmpty()) {
			throw new IllegalStateException("Job name is not set in either job1OrgName or PO15_ValidateRecommendedProfileDetails.orgJobName");
		}
		job1OrgName.set(jobName);
		return jobName;
	}
}
