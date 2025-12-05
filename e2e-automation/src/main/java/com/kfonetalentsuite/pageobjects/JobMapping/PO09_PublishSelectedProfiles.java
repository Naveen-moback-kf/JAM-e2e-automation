package com.kfonetalentsuite.pageobjects.JobMapping;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
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

	public void user_should_verify_published_first_job_profile_is_displayed_in_row1_in_view_published_screen() throws Exception {
		try {
			// PARALLEL EXECUTION FIX: Verify expected job name matches (search should filter correctly)
			String expectedJobName = PO04_VerifyJobMappingPageComponents.orgJobNameInRow1.get();
			if (expectedJobName == null || expectedJobName.isEmpty()) {
				throw new Exception("Expected job name is null or empty");
			}
			
			// Wait for search results to filter - retry until first row matches expected job
			int maxRetries = 5;
			int retryCount = 0;
			boolean found = false;
			
			while (retryCount < maxRetries && !found) {
				PerformanceUtils.waitForPageReady(driver, 2);
				waitForSpinners();
				safeSleep(500);
				
				try {
					// Re-fetch element to avoid stale reference
					WebElement jobNameElement = waitForElement(JOB_NAME_ROW_1, 5);
					String job1NameText = jobNameElement.getText();
					String actualJobName = job1NameText.split("-", 2)[0].trim();
					
					if (expectedJobName.equals(actualJobName)) {
						found = true;
						PageObjectHelper.log(LOGGER, "Found expected job: " + actualJobName);
					} else {
						retryCount++;
						if (retryCount < maxRetries) {
							LOGGER.warn("Search not filtered yet. Expected: '{}', Found: '{}' (retry {}/{})", 
								expectedJobName, actualJobName, retryCount, maxRetries);
							safeSleep(1000 * retryCount); // Exponential backoff
						}
					}
				} catch (org.openqa.selenium.StaleElementReferenceException e) {
					retryCount++;
					LOGGER.warn("Stale element during search verification (retry {}/{})", retryCount, maxRetries);
					if (retryCount < maxRetries) {
						safeSleep(1000);
					}
				} catch (TimeoutException e) {
					retryCount++;
					LOGGER.warn("Element not found yet during search verification (retry {}/{})", retryCount, maxRetries);
					if (retryCount < maxRetries) {
						safeSleep(1000 * retryCount);
					}
				}
			}
			
			if (!found) {
				// Final attempt to get actual value for better error message
				String actualJobName = "Unknown";
				try {
					WebElement jobNameElement = waitForElement(JOB_NAME_ROW_1, 5);
					String job1NameText = jobNameElement.getText();
					actualJobName = job1NameText.split("-", 2)[0].trim();
				} catch (Exception e) {
					LOGGER.error("Could not get actual job name for error message: {}", e.getMessage());
				}
				Assert.fail(String.format("Expected job '%s' but found '%s' after %d retries. Search may not have filtered correctly.", 
					expectedJobName, actualJobName, maxRetries));
			}
			
			Assert.assertTrue(waitForElement(JOB_1_PUBLISHED_BTN).isDisplayed());
			PageObjectHelper.log(LOGGER, "Published Job (Org: " + expectedJobName + ") is displayed in Row1");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_published_first_job_profile_is_displayed_in_row1_in_view_published_screen", "Issue verifying published first job profile in Row1", e);
			throw e;
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

	public void user_should_verify_published_second_job_profile_is_displayed_in_row1_in_view_published_screen() throws Exception {
		try {
			// PARALLEL EXECUTION FIX: Verify expected job name matches (search should filter correctly)
			String expectedJobName = PO04_VerifyJobMappingPageComponents.orgJobNameInRow2.get();
			if (expectedJobName == null || expectedJobName.isEmpty()) {
				throw new Exception("Expected job name is null or empty");
			}
			
			// Wait for search results to filter - retry until first row matches expected job
			int maxRetries = 5;
			int retryCount = 0;
			boolean found = false;
			
			while (retryCount < maxRetries && !found) {
				PerformanceUtils.waitForPageReady(driver, 2);
				waitForSpinners();
				safeSleep(500);
				
				try {
					// Re-fetch element to avoid stale reference
					WebElement jobNameElement = waitForElement(JOB_NAME_ROW_1, 5);
					String job1NameText = jobNameElement.getText();
					String actualJobName = job1NameText.split("-", 2)[0].trim();
					
					if (expectedJobName.equals(actualJobName)) {
						found = true;
						PageObjectHelper.log(LOGGER, "Found expected job: " + actualJobName);
					} else {
						retryCount++;
						if (retryCount < maxRetries) {
							LOGGER.warn("Search not filtered yet. Expected: '{}', Found: '{}' (retry {}/{})", 
								expectedJobName, actualJobName, retryCount, maxRetries);
							safeSleep(1000 * retryCount); // Exponential backoff
						}
					}
				} catch (org.openqa.selenium.StaleElementReferenceException e) {
					retryCount++;
					LOGGER.warn("Stale element during search verification (retry {}/{})", retryCount, maxRetries);
					if (retryCount < maxRetries) {
						safeSleep(1000);
					}
				} catch (TimeoutException e) {
					retryCount++;
					LOGGER.warn("Element not found yet during search verification (retry {}/{})", retryCount, maxRetries);
					if (retryCount < maxRetries) {
						safeSleep(1000 * retryCount);
					}
				}
			}
			
			if (!found) {
				// Final attempt to get actual value for better error message
				String actualJobName = "Unknown";
				try {
					WebElement jobNameElement = waitForElement(JOB_NAME_ROW_1, 5);
					String job1NameText = jobNameElement.getText();
					actualJobName = job1NameText.split("-", 2)[0].trim();
				} catch (Exception e) {
					LOGGER.error("Could not get actual job name for error message: {}", e.getMessage());
				}
				Assert.fail(String.format("Expected job '%s' but found '%s' after %d retries. Search may not have filtered correctly.", 
					expectedJobName, actualJobName, maxRetries));
			}
			
			Assert.assertTrue(waitForElement(JOB_1_PUBLISHED_BTN).isDisplayed());
			PageObjectHelper.log(LOGGER, "Published Job (Org: " + expectedJobName + ") is displayed in Row1");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_published_second_job_profile_is_displayed_in_row1_in_view_published_screen", "Issue verifying published second job profile in Row1", e);
			throw e;
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

	public void user_should_verify_published_second_job_profile_is_displayed_in_row1_in_hcm_sync_profiles_tab_in_pm() throws Exception {
		try {
			// PARALLEL EXECUTION FIX: Verify expected job name matches (search should filter correctly)
			String expectedJobName = PO04_VerifyJobMappingPageComponents.orgJobNameInRow2.get();
			if (expectedJobName == null || expectedJobName.isEmpty()) {
				throw new Exception("Expected job name is null or empty");
			}
			
			// Wait for search results to filter - retry until first row matches expected job
			int maxRetries = 5;
			int retryCount = 0;
			boolean found = false;
			
			while (retryCount < maxRetries && !found) {
				PerformanceUtils.waitForPageReady(driver, 2);
				waitForSpinners();
				safeSleep(500);
				
				try {
					// Re-fetch element to avoid stale reference
					WebElement jobNameElement = waitForElement(HCM_JOB_ROW_1, 5);
					String job2NameText = jobNameElement.getText();
					String actualJobName = job2NameText.split("-", 2)[0].trim();
					
					if (expectedJobName.equals(actualJobName)) {
						found = true;
						PageObjectHelper.log(LOGGER, "Found expected job in HCM Sync: " + actualJobName);
					} else {
						retryCount++;
						if (retryCount < maxRetries) {
							LOGGER.warn("Search not filtered yet in HCM. Expected: '{}', Found: '{}' (retry {}/{})", 
								expectedJobName, actualJobName, retryCount, maxRetries);
							safeSleep(1000 * retryCount); // Exponential backoff
						}
					}
				} catch (org.openqa.selenium.StaleElementReferenceException e) {
					retryCount++;
					LOGGER.warn("Stale element during HCM verification (retry {}/{})", retryCount, maxRetries);
					if (retryCount < maxRetries) {
						safeSleep(1000);
					}
				} catch (TimeoutException e) {
					retryCount++;
					if (retryCount < maxRetries) {
						LOGGER.warn("Job element not found yet in HCM (retry {}/{})", retryCount, maxRetries);
						safeSleep(1000 * retryCount);
					}
				}
			}
			
			if (!found) {
				// Final attempt to get actual value for better error message
				String actualJobName = "Unknown";
				try {
					WebElement jobNameElement = waitForElement(HCM_JOB_ROW_1, 5);
					String job2NameText = jobNameElement.getText();
					actualJobName = job2NameText.split("-", 2)[0].trim();
				} catch (Exception e) {
					LOGGER.error("Could not get actual job name for error message: {}", e.getMessage());
				}
				Assert.fail(String.format("Expected job '%s' but found '%s' after %d retries in HCM Sync Profiles. Search may not have filtered correctly.", 
					expectedJobName, actualJobName, maxRetries));
			}
			
			PageObjectHelper.log(LOGGER, "Published Second Job (Org: " + expectedJobName + ") is displayed in HCM Sync Profiles Row1");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_published_second_job_profile_is_displayed_in_row1_in_hcm_sync_profiles_tab_in_pm", "Issue verifying published second job profile in HCM Sync Profiles", e);
			throw e;
		}
	}

	// formatDateForDisplay() removed - now using inherited method from BasePageObject
}
