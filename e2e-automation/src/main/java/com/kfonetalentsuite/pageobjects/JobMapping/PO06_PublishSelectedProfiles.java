package com.kfonetalentsuite.pageobjects.JobMapping;
import static com.kfonetalentsuite.pageobjects.JobMapping.BasePageObject.Locators.JobMappingScreen.*;
import static com.kfonetalentsuite.pageobjects.JobMapping.BasePageObject.Locators.Common.*;
import static com.kfonetalentsuite.pageobjects.JobMapping.BasePageObject.Locators.HCMSyncProfiles.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import com.kfonetalentsuite.utils.JobMapping.Utilities;
import com.kfonetalentsuite.utils.common.ScreenshotHandler;

public class PO06_PublishSelectedProfiles extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO06_PublishSelectedProfiles.class);

	// formatDateForDisplay() is inherited from BasePageObject
	
	// Using centralized Locators from BasePageObject
	public PO06_PublishSelectedProfiles() {
		super();
	}

	public void search_for_published_job_name1() {
		try {
			String jobName = PO04_JobMappingPageComponents.orgJobNameInRow1.get();
			if (jobName == null || jobName.isEmpty()) {
				throw new Exception("Job name to search is null or empty");
			}
			
			LOGGER.info("Searching for job: {}", jobName);
			Utilities.waitForPageReady(driver, 3);
			waitForSpinners();
			safeSleep(500);
			
			WebElement searchBar = waitForElement(SEARCH_BAR, 10);
			scrollToElement(searchBar);
			
			// Use new clearAndSearch helper
			clearAndSearch(SEARCH_BAR, jobName);
			safeSleep(1500); // Extra wait for results stability
			
			LOGGER.info("Searched for job: " + jobName + " in View Published screen");
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("search_for_published_job_name1", e);
			Utilities.handleError(LOGGER, "search_for_published_job_name1", "Failed to search for first job in View Published screen", e);
		}
	}

	public void user_should_verify_published_first_job_profile_is_displayed_in_row1_in_view_published_screen() throws Exception {
		try {
			// PARALLEL EXECUTION FIX: Verify expected job name matches (search should filter correctly)
			String expectedJobName = PO04_JobMappingPageComponents.orgJobNameInRow1.get();
			if (expectedJobName == null || expectedJobName.isEmpty()) {
				throw new Exception("Expected job name is null or empty");
			}
			
			// Wait for search results to filter - retry until first row matches expected job
			int maxRetries = 5;
			int retryCount = 0;
			boolean found = false;
			
			while (retryCount < maxRetries && !found) {
				Utilities.waitForPageReady(driver, 2);
				waitForSpinners();
				safeSleep(500);
				
				try {
					// Re-fetch element to avoid stale reference
					WebElement jobNameElement = waitForElement(JOB_NAME_ROW_1, 5);
					String job1NameText = jobNameElement.getText();
					String actualJobName = job1NameText.split("-", 2)[0].trim();
					
					if (expectedJobName.equals(actualJobName)) {
						found = true;
						LOGGER.info("Found expected job: " + actualJobName);
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
				String errorMsg = String.format("Expected job '%s' but found '%s' after %d retries. Search may not have filtered correctly.", 
					expectedJobName, actualJobName, maxRetries);
				ScreenshotHandler.captureFailureScreenshot("user_should_verify_published_first_job_profile_is_displayed_in_row1_in_view_published_screen", 
					new Exception(errorMsg));
				Assert.fail(errorMsg);
			}
			
			try {
				Assert.assertTrue(waitForElement(JOB_1_PUBLISHED_BTN).isDisplayed());
			} catch (Exception e) {
				Utilities.handleError(LOGGER, "user_should_verify_published_first_job_profile_is_displayed_in_row1_in_view_published_screen", "Published button not displayed", e);
			}
			LOGGER.info("Published Job (Org: " + expectedJobName + ") is displayed in Row1");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_should_verify_published_first_job_profile_is_displayed_in_row1_in_view_published_screen", "Issue verifying published first job profile in Row1", e);
		}
	}

	public void search_for_published_job_name2() {
		try {
			String jobName = PO04_JobMappingPageComponents.orgJobNameInRow2.get();
			if (jobName == null || jobName.isEmpty()) {
				throw new Exception("Job name to search is null or empty");
			}
			
			LOGGER.info("Searching for job: {}", jobName);
			Utilities.waitForPageReady(driver, 3);
			waitForSpinners();
			safeSleep(500);
			
			WebElement searchBar = waitForElement(SEARCH_BAR, 10);
			scrollToElement(searchBar);
			
			// Use new clearAndSearch helper
			clearAndSearch(SEARCH_BAR, jobName);
			safeSleep(1500); // Extra wait for results stability
			
			LOGGER.info("Searched for job: " + jobName + " in View Published screen");
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("search_for_published_job_name2", e);
			Utilities.handleError(LOGGER, "search_for_published_job_name2", "Failed to search for second job in View Published screen", e);
		}
	}

	public void user_should_verify_published_second_job_profile_is_displayed_in_row1_in_view_published_screen() throws Exception {
		try {
			// PARALLEL EXECUTION FIX: Verify expected job name matches (search should filter correctly)
			String expectedJobName = PO04_JobMappingPageComponents.orgJobNameInRow2.get();
			if (expectedJobName == null || expectedJobName.isEmpty()) {
				throw new Exception("Expected job name is null or empty");
			}
			
			// Wait for search results to filter - retry until first row matches expected job
			int maxRetries = 5;
			int retryCount = 0;
			boolean found = false;
			
			while (retryCount < maxRetries && !found) {
				Utilities.waitForPageReady(driver, 2);
				waitForSpinners();
				safeSleep(500);
				
				try {
					// Re-fetch element to avoid stale reference
					WebElement jobNameElement = waitForElement(JOB_NAME_ROW_1, 5);
					String job1NameText = jobNameElement.getText();
					String actualJobName = job1NameText.split("-", 2)[0].trim();
					
					if (expectedJobName.equals(actualJobName)) {
						found = true;
						LOGGER.info("Found expected job: " + actualJobName);
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
				String errorMsg = String.format("Expected job '%s' but found '%s' after %d retries. Search may not have filtered correctly.", 
					expectedJobName, actualJobName, maxRetries);
				ScreenshotHandler.captureFailureScreenshot("user_should_verify_published_first_job_profile_is_displayed_in_row1_in_view_published_screen", 
					new Exception(errorMsg));
				Assert.fail(errorMsg);
			}
			
			try {
				Assert.assertTrue(waitForElement(JOB_1_PUBLISHED_BTN).isDisplayed());
			} catch (Exception e) {
				Utilities.handleError(LOGGER, "user_should_verify_published_second_job_profile_is_displayed_in_row1_in_view_published_screen", "Published button not displayed", e);
			}
			LOGGER.info("Published Job (Org: " + expectedJobName + ") is displayed in Row1");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_should_verify_published_second_job_profile_is_displayed_in_row1_in_view_published_screen", "Issue verifying published second job profile in Row1", e);
		}
	}

	public void user_should_verify_date_on_published_first_job_matches_with_current_date() {
		try {
			Utilities.waitForPageReady(driver, 2);
			String todayDate = formatDateForDisplay();
			String jobPublishedDate = getElementText(HCM_DATE_ROW_1);
			Assert.assertEquals(jobPublishedDate, todayDate);
			LOGGER.info("Current date verified on Published First Job: " + PO04_JobMappingPageComponents.orgJobNameInRow1.get());
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_should_verify_date_on_published_first_job_matches_with_current_date", "Issue verifying date on published first job", e);
		}
	}

	public void user_should_verify_date_on_published_second_job_matches_with_current_date() {
		try {
			String todayDate = formatDateForDisplay();
			String jobPublishedDate = getElementText(HCM_DATE_ROW_2);
			Assert.assertEquals(jobPublishedDate, todayDate);
			LOGGER.info("Current date verified on Published Second Job: " + PO04_JobMappingPageComponents.orgJobNameInRow2.get());
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_should_verify_date_on_published_second_job_matches_with_current_date", "Issue verifying date on published second job", e);
		}
	}

	public void search_for_published_job_name2_in_hcm_sync_profiles_tab_in_pm() {
		try {
			// OPTION B FIX: Set PO05 variables before validation so PO05 methods can access them
			syncPO05VariablesForJob2();
			
			String jobName = PO04_JobMappingPageComponents.orgJobNameInRow2.get();
			if (jobName == null || jobName.isEmpty()) {
				throw new Exception("Job name to search is null or empty");
			}
			
			Utilities.waitForPageReady(driver, 3);
			waitForSpinners();
			safeSleep(500);
			
			WebElement searchBar = waitForElement(PROFILES_SEARCH, 10);
			scrollToElement(searchBar);
			
			// Use new clearAndSearch helper (includes all waits and retry logic)
			clearAndSearch(PROFILES_SEARCH, jobName);
			
			// Additional wait for HCM sync profile filtering
			safeSleep(1000);
			waitForSpinners();
			Utilities.waitForPageReady(driver, 2);
			
			LOGGER.info("HCM Search completed for job: {}", jobName);
			LOGGER.info("Searched for job: " + jobName + " in HCM Sync Profiles");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "search_for_published_job_name2_in_hcm_sync_profiles_tab_in_pm", "Failed to search for job in HCM Sync Profiles", e);
		}
	}
	
	public void search_for_published_job_code2_in_hcm_sync_profiles_tab_in_pm() {
		try {
			// OPTION B FIX: Set PO05 variables before validation so PO05 methods can access them
			syncPO05VariablesForJob2();
			
			String jobCode = PO04_JobMappingPageComponents.orgJobCodeInRow2.get();
			if (jobCode == null || jobCode.isEmpty()) {
				throw new Exception("Job code to search is null or empty");
			}
			
			Utilities.waitForPageReady(driver, 3);
			waitForSpinners();
			safeSleep(500);
			
			WebElement searchBar = waitForElement(PROFILES_SEARCH, 10);
			scrollToElement(searchBar);
			
			// Use new clearAndSearch helper (includes all waits and retry logic)
			clearAndSearch(PROFILES_SEARCH, jobCode);
			
			// Additional wait for HCM sync profile filtering
			safeSleep(1000);
			waitForSpinners();
			Utilities.waitForPageReady(driver, 2);
			
			LOGGER.info("HCM Search completed for job code: {}", jobCode);
			LOGGER.info("Searched for job code: " + jobCode + " in HCM Sync Profiles");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "search_for_published_job_code2_in_hcm_sync_profiles_tab_in_pm", "Failed to search for job code in HCM Sync Profiles", e);
		}
	}

	// OPTION B FIX: Sync PO05 variables with PO04 Row2 data for Job2 validation
	private void syncPO05VariablesForJob2() {
		String job2Name = PO04_JobMappingPageComponents.orgJobNameInRow2.get();
		String job2Code = PO04_JobMappingPageComponents.orgJobCodeInRow2.get();
		
		if (job2Name != null && !job2Name.isEmpty()) {
			PO05_PublishJobProfile.job1OrgName.set(job2Name);
			LOGGER.debug("Synced PO05.job1OrgName with Job2 name: {}", job2Name);
		}
		
		if (job2Code != null && !job2Code.isEmpty()) {
			PO05_PublishJobProfile.job1OrgCode.set(job2Code);
			LOGGER.debug("Synced PO05.job1OrgCode with Job2 code: {}", job2Code);
		}
	}

	// OPTION B FIX: Sync PO05 variables with PO04 Row1 data for Job1 validation
	@SuppressWarnings("unused")
	private void syncPO05VariablesForJob1() {
		String job1Name = PO04_JobMappingPageComponents.orgJobNameInRow1.get();
		String job1Code = PO04_JobMappingPageComponents.orgJobCodeInRow1.get();
		
		if (job1Name != null && !job1Name.isEmpty()) {
			PO05_PublishJobProfile.job1OrgName.set(job1Name);
			LOGGER.debug("Synced PO05.job1OrgName with Job1 name: {}", job1Name);
		}
		
		if (job1Code != null && !job1Code.isEmpty()) {
			PO05_PublishJobProfile.job1OrgCode.set(job1Code);
			LOGGER.debug("Synced PO05.job1OrgCode with Job1 code: {}", job1Code);
		}
	}

	public void user_should_verify_published_second_job_profile_is_displayed_in_row1_in_hcm_sync_profiles_tab_in_pm() throws Exception {
		try {
			// PARALLEL EXECUTION FIX: Verify expected job name matches (search should filter correctly)
			String expectedJobName = PO04_JobMappingPageComponents.orgJobNameInRow2.get();
			if (expectedJobName == null || expectedJobName.isEmpty()) {
				throw new Exception("Expected job name is null or empty");
			}
			
			// Wait for search results to filter - retry until first row matches expected job
			int maxRetries = 5;
			int retryCount = 0;
			boolean found = false;
			
			while (retryCount < maxRetries && !found) {
				Utilities.waitForPageReady(driver, 2);
				waitForSpinners();
				safeSleep(500);
				
				try {
					// Re-fetch element to avoid stale reference
					WebElement jobNameElement = waitForElement(HCM_JOB_ROW_1, 5);
					String job2NameText = jobNameElement.getText();
					String actualJobName = job2NameText.split("-", 2)[0].trim();
					
					if (expectedJobName.equals(actualJobName)) {
						found = true;
						LOGGER.info("Found expected job in HCM Sync: " + actualJobName);
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
				String errorMsg = String.format("Expected job '%s' but found '%s' after %d retries in HCM Sync Profiles. Search may not have filtered correctly.", 
					expectedJobName, actualJobName, maxRetries);
				ScreenshotHandler.captureFailureScreenshot("user_should_verify_published_second_job_profile_is_displayed_in_row1_in_hcm_sync_profiles_tab_in_pm", 
					new Exception(errorMsg));
				Assert.fail(errorMsg);
			}
			
			LOGGER.info("Published Second Job (Org: " + expectedJobName + ") is displayed in HCM Sync Profiles Row1");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_should_verify_published_second_job_profile_is_displayed_in_row1_in_hcm_sync_profiles_tab_in_pm", "Issue verifying published second job profile in HCM Sync Profiles", e);
		}
	}

	// formatDateForDisplay() removed - now using inherited method from BasePageObject
}

