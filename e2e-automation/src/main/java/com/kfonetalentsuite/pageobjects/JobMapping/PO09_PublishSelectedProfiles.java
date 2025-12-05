package com.kfonetalentsuite.pageobjects.JobMapping;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.PageObjectHelper;

public class PO09_PublishSelectedProfiles extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO09_PublishSelectedProfiles.class);

	// formatDateForDisplay() is inherited from BasePageObject

	private static final By JOB_NAME_ROW_1 = By.xpath("//tbody//tr[1]//td[2]//div[contains(text(),'(')]");
	private static final By JOB_1_PUBLISHED_BTN = By.xpath("//tbody//tr[2]//button[text()='Published'][1]");
	private static final By HCM_PROFILES_SEARCH = By.xpath("//input[@type='search']");
	private static final By HCM_JOB_ROW_1 = By.xpath("//tbody//tr[1]//td//div//span[1]//a");
	private static final By HCM_DATE_ROW_1 = By.xpath("//tbody//tr[1]//td[7]//span");
	private static final By HCM_DATE_ROW_2 = By.xpath("//tbody//tr[2]//td[7]//span");

	public PO09_PublishSelectedProfiles() {
		super();
	}

	public void search_for_published_job_name1() {
		try {
			PerformanceUtils.waitForPageReady(driver, 3);
			WebElement searchBar = waitForElement(Locators.SearchAndFilters.SEARCH_BAR);
			searchBar.click();
			searchBar.clear();
			searchBar.sendKeys(PO04_VerifyJobMappingPageComponents.orgJobNameInRow1.get());
			searchBar.sendKeys(Keys.ENTER);
			PerformanceUtils.waitForPageReady(driver, 2);
			PageObjectHelper.log(LOGGER, "Searched for job: " + PO04_VerifyJobMappingPageComponents.orgJobNameInRow1.get() + " in View Published screen");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "search_for_published_job_name1", "Failed to search for first job in View Published screen", e);
		}
	}

	public void user_should_verify_published_first_job_profile_is_displayed_in_row1_in_view_published_screen() {
		try {
			// PARALLEL EXECUTION FIX: Verify expected job name matches (search should filter correctly)
			String expectedJobName = PO04_VerifyJobMappingPageComponents.orgJobNameInRow1.get();
			PerformanceUtils.waitForPageReady(driver, 2);
			String job1NameText = getElementText(JOB_NAME_ROW_1);
			String actualJobName = job1NameText.split("-", 2)[0].trim();
			Assert.assertEquals(expectedJobName, actualJobName, 
				String.format("Expected job '%s' but found '%s'", expectedJobName, actualJobName));
			Assert.assertTrue(waitForElement(JOB_1_PUBLISHED_BTN).isDisplayed());
			PageObjectHelper.log(LOGGER, "Published Job (Org: " + actualJobName + ") is displayed in Row1");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_published_first_job_profile_is_displayed_in_row1_in_view_published_screen", "Issue verifying published first job profile in Row1", e);
		}
	}

	public void search_for_published_job_name2() {
		try {
			PerformanceUtils.waitForPageReady(driver, 3);
			WebElement searchBar = waitForElement(Locators.SearchAndFilters.SEARCH_BAR);
			searchBar.click();
			searchBar.clear();
			searchBar.sendKeys(PO04_VerifyJobMappingPageComponents.orgJobNameInRow2.get());
			searchBar.sendKeys(Keys.ENTER);
			PerformanceUtils.waitForPageReady(driver, 2);
			PageObjectHelper.log(LOGGER, "Searched for job: " + PO04_VerifyJobMappingPageComponents.orgJobNameInRow2.get() + " in View Published screen");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "search_for_published_job_name2", "Failed to search for second job in View Published screen", e);
		}
	}

	public void user_should_verify_published_second_job_profile_is_displayed_in_row1_in_view_published_screen() {
		try {
			// PARALLEL EXECUTION FIX: Verify expected job name matches (search should filter correctly)
			String expectedJobName = PO04_VerifyJobMappingPageComponents.orgJobNameInRow2.get();
			PerformanceUtils.waitForPageReady(driver, 2);
			String job1NameText = getElementText(JOB_NAME_ROW_1);
			String actualJobName = job1NameText.split("-", 2)[0].trim();
			Assert.assertEquals(expectedJobName, actualJobName, 
				String.format("Expected job '%s' but found '%s'", expectedJobName, actualJobName));
			Assert.assertTrue(waitForElement(JOB_1_PUBLISHED_BTN).isDisplayed());
			PageObjectHelper.log(LOGGER, "Published Job (Org: " + actualJobName + ") is displayed in Row1");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_published_second_job_profile_is_displayed_in_row1_in_view_published_screen", "Issue verifying published second job profile in Row1", e);
		}
	}

	public void user_should_verify_date_on_published_first_job_matches_with_current_date() {
		try {
			PerformanceUtils.waitForPageReady(driver, 2);
			String todayDate = formatDateForDisplay();
			String jobPublishedDate = getElementText(HCM_DATE_ROW_1);
			Assert.assertEquals(jobPublishedDate, todayDate);
			PageObjectHelper.log(LOGGER, "Current date verified on Published First Job: " + PO04_VerifyJobMappingPageComponents.orgJobNameInRow1.get());
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_date_on_published_first_job_matches_with_current_date", "Issue verifying date on published first job", e);
		}
	}

	public void user_should_verify_date_on_published_second_job_matches_with_current_date() {
		try {
			String todayDate = formatDateForDisplay();
			String jobPublishedDate = getElementText(HCM_DATE_ROW_2);
			Assert.assertEquals(jobPublishedDate, todayDate);
			PageObjectHelper.log(LOGGER, "Current date verified on Published Second Job: " + PO04_VerifyJobMappingPageComponents.orgJobNameInRow2.get());
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_date_on_published_second_job_matches_with_current_date", "Issue verifying date on published second job", e);
		}
	}

	public void search_for_published_job_name2_in_hcm_sync_profiles_tab_in_pm() {
		try {
			WebElement searchBox = waitForElement(HCM_PROFILES_SEARCH);
			searchBox.click();
			searchBox.clear();
			searchBox.sendKeys(PO04_VerifyJobMappingPageComponents.orgJobNameInRow2.get());
			searchBox.sendKeys(Keys.ENTER);
			waitForSpinners();
			PerformanceUtils.waitForPageReady(driver, 2);
			PageObjectHelper.log(LOGGER, "Searched for job: " + PO04_VerifyJobMappingPageComponents.orgJobNameInRow2.get() + " in HCM Sync Profiles");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "search_for_published_job_name2_in_hcm_sync_profiles_tab_in_pm", "Failed to search for job in HCM Sync Profiles", e);
		}
	}

	public void user_should_verify_published_second_job_profile_is_displayed_in_row1_in_hcm_sync_profiles_tab_in_pm() {
		try {
			// PARALLEL EXECUTION FIX: Verify expected job name matches (search should filter correctly)
			String expectedJobName = PO04_VerifyJobMappingPageComponents.orgJobNameInRow2.get();
			PerformanceUtils.waitForPageReady(driver, 2);
			waitForSpinners();
			String job2NameText = getElementText(HCM_JOB_ROW_1);
			String actualJobName = job2NameText.split("-", 2)[0].trim();
			Assert.assertEquals(expectedJobName, actualJobName, 
				String.format("Expected job '%s' but found '%s'", expectedJobName, actualJobName));
			PageObjectHelper.log(LOGGER, "Published Second Job (Org: " + actualJobName + ") is displayed in HCM Sync Profiles Row1");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_published_second_job_profile_is_displayed_in_row1_in_hcm_sync_profiles_tab_in_pm", "Issue verifying published second job profile in HCM Sync Profiles", e);
		}
	}

	// formatDateForDisplay() removed - now using inherited method from BasePageObject
}
