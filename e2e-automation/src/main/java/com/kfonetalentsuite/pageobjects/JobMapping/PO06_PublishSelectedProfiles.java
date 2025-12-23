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
import com.kfonetalentsuite.utils.JobMapping.ScreenshotHandler;

public class PO06_PublishSelectedProfiles extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO06_PublishSelectedProfiles.class);

	// formatDateForDisplay() is inherited from BasePageObject
	
	// Using centralized Locators from BasePageObject
	private static final By JOB_NAME_ROW_1 = Locators.JobMappingResults.JOB_NAME_ROW_1;
	private static final By JOB_1_PUBLISHED_BTN = Locators.JobMappingResults.JOB_1_PUBLISHED_BTN;
	private static final By HCM_PROFILES_SEARCH = Locators.PMScreen.SEARCH_BAR;
	private static final By HCM_JOB_ROW_1 = Locators.JobMappingResults.HCM_JOB_ROW_1;
	private static final By HCM_DATE_ROW_1 = Locators.JobMappingResults.HCM_DATE_ROW_1;
	private static final By HCM_DATE_ROW_2 = By.xpath("//tbody//tr[2]//td[8]//span");

	public PO06_PublishSelectedProfiles() {
		super();
	}

	public void search_for_published_job_name1() {
		try {
			String jobName = PO04_JobMappingPageComponents.orgJobNameInRow1.get();
			if (jobName == null || jobName.isEmpty()) {
				throw new Exception("Job name to search is null or empty");
			}
			
			PerformanceUtils.waitForPageReady(driver, 3);
			waitForSpinners();
			safeSleep(500);
			
			// PARALLEL EXECUTION FIX: Clear search bar more thoroughly and wait for it to be ready
			WebElement searchBar = waitForElement(Locators.SearchAndFilters.SEARCH_BAR, 10);
			scrollToElement(searchBar);
			
			// Clear any existing search
			try {
				searchBar.click();
				safeSleep(300);
				searchBar.clear();
				searchBar.sendKeys(Keys.CONTROL + "a");
				searchBar.sendKeys(Keys.DELETE);
				safeSleep(300);
			} catch (Exception clearEx) {
				jsClick(searchBar);
				safeSleep(300);
				js.executeScript("arguments[0].value = '';", searchBar);
				safeSleep(300);
			}
			
			// Type search term
			searchBar.sendKeys(jobName);
			safeSleep(500);
			
			// Press Enter and wait for search to complete
			searchBar.sendKeys(Keys.ENTER);
			
			// PARALLEL EXECUTION FIX: Wait for search to actually filter results
			waitForSpinners();
			PerformanceUtils.waitForPageReady(driver, 5);
			
			// Verify search was actually applied by checking search bar value
			int verifyRetries = 5;
			boolean searchApplied = false;
			for (int v = 0; v < verifyRetries && !searchApplied; v++) {
				safeSleep(500);
				try {
					WebElement verifySearchBar = driver.findElement(Locators.SearchAndFilters.SEARCH_BAR);
					String searchValue = verifySearchBar.getAttribute("value");
					if (searchValue != null && searchValue.contains(jobName)) {
						searchApplied = true;
						LOGGER.info("Search confirmed applied: '{}' found in search bar", searchValue);
					} else {
						LOGGER.warn("Search not yet applied (attempt {}/{}). Expected: '{}', Found: '{}'", 
							v + 1, verifyRetries, jobName, searchValue);
					}
				} catch (Exception e) {
					LOGGER.warn("Could not verify search bar value (attempt {}/{}): {}", v + 1, verifyRetries, e.getMessage());
				}
			}
			
			// Additional wait for search filtering to complete
			safeSleep(1000);
			waitForSpinners();
			PerformanceUtils.waitForPageReady(driver, 2);
			
			LOGGER.info("Search completed for job: {}", jobName);
			PageObjectHelper.log(LOGGER, "Searched for job: " + jobName + " in View Published screen");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "search_for_published_job_name1", "Failed to search for first job in View Published screen", e);
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
				String errorMsg = String.format("Expected job '%s' but found '%s' after %d retries. Search may not have filtered correctly.", 
					expectedJobName, actualJobName, maxRetries);
				ScreenshotHandler.captureFailureScreenshot("user_should_verify_published_first_job_profile_is_displayed_in_row1_in_view_published_screen", 
					new Exception(errorMsg));
				Assert.fail(errorMsg);
			}
			
			try {
				Assert.assertTrue(waitForElement(JOB_1_PUBLISHED_BTN).isDisplayed());
			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER, "user_should_verify_published_first_job_profile_is_displayed_in_row1_in_view_published_screen", "Published button not displayed", e);
			}
			PageObjectHelper.log(LOGGER, "Published Job (Org: " + expectedJobName + ") is displayed in Row1");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_published_first_job_profile_is_displayed_in_row1_in_view_published_screen", "Issue verifying published first job profile in Row1", e);
		}
	}

	public void search_for_published_job_name2() {
		try {
			String jobName = PO04_JobMappingPageComponents.orgJobNameInRow2.get();
			if (jobName == null || jobName.isEmpty()) {
				throw new Exception("Job name to search is null or empty");
			}
			
			PerformanceUtils.waitForPageReady(driver, 3);
			waitForSpinners();
			safeSleep(500);
			
			// PARALLEL EXECUTION FIX: Clear search bar more thoroughly and wait for it to be ready
			WebElement searchBar = waitForElement(Locators.SearchAndFilters.SEARCH_BAR, 10);
			scrollToElement(searchBar);
			
			// Clear any existing search
			try {
				searchBar.click();
				safeSleep(300);
				searchBar.clear();
				searchBar.sendKeys(Keys.CONTROL + "a");
				searchBar.sendKeys(Keys.DELETE);
				safeSleep(300);
			} catch (Exception clearEx) {
				jsClick(searchBar);
				safeSleep(300);
				js.executeScript("arguments[0].value = '';", searchBar);
				safeSleep(300);
			}
			
			// Type search term
			searchBar.sendKeys(jobName);
			safeSleep(500);
			
			// Press Enter and wait for search to complete
			searchBar.sendKeys(Keys.ENTER);
			
			// PARALLEL EXECUTION FIX: Wait for search to actually filter results
			waitForSpinners();
			PerformanceUtils.waitForPageReady(driver, 5);
			
			// Verify search was actually applied by checking search bar value
			int verifyRetries = 5;
			boolean searchApplied = false;
			for (int v = 0; v < verifyRetries && !searchApplied; v++) {
				safeSleep(500);
				try {
					WebElement verifySearchBar = driver.findElement(Locators.SearchAndFilters.SEARCH_BAR);
					String searchValue = verifySearchBar.getAttribute("value");
					if (searchValue != null && searchValue.contains(jobName)) {
						searchApplied = true;
						LOGGER.info("Search confirmed applied: '{}' found in search bar", searchValue);
					} else {
						LOGGER.warn("Search not yet applied (attempt {}/{}). Expected: '{}', Found: '{}'", 
							v + 1, verifyRetries, jobName, searchValue);
					}
				} catch (Exception e) {
					LOGGER.warn("Could not verify search bar value (attempt {}/{}): {}", v + 1, verifyRetries, e.getMessage());
				}
			}
			
			// Additional wait for search filtering to complete
			safeSleep(1000);
			waitForSpinners();
			PerformanceUtils.waitForPageReady(driver, 2);
			
			LOGGER.info("Search completed for job: {}", jobName);
			PageObjectHelper.log(LOGGER, "Searched for job: " + jobName + " in View Published screen");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "search_for_published_job_name2", "Failed to search for second job in View Published screen", e);
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
				String errorMsg = String.format("Expected job '%s' but found '%s' after %d retries. Search may not have filtered correctly.", 
					expectedJobName, actualJobName, maxRetries);
				ScreenshotHandler.captureFailureScreenshot("user_should_verify_published_first_job_profile_is_displayed_in_row1_in_view_published_screen", 
					new Exception(errorMsg));
				Assert.fail(errorMsg);
			}
			
			try {
				Assert.assertTrue(waitForElement(JOB_1_PUBLISHED_BTN).isDisplayed());
			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER, "user_should_verify_published_second_job_profile_is_displayed_in_row1_in_view_published_screen", "Published button not displayed", e);
			}
			PageObjectHelper.log(LOGGER, "Published Job (Org: " + expectedJobName + ") is displayed in Row1");
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
			PageObjectHelper.log(LOGGER, "Current date verified on Published First Job: " + PO04_JobMappingPageComponents.orgJobNameInRow1.get());
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_date_on_published_first_job_matches_with_current_date", "Issue verifying date on published first job", e);
		}
	}

	public void user_should_verify_date_on_published_second_job_matches_with_current_date() {
		try {
			String todayDate = formatDateForDisplay();
			String jobPublishedDate = getElementText(HCM_DATE_ROW_2);
			Assert.assertEquals(jobPublishedDate, todayDate);
			PageObjectHelper.log(LOGGER, "Current date verified on Published Second Job: " + PO04_JobMappingPageComponents.orgJobNameInRow2.get());
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_date_on_published_second_job_matches_with_current_date", "Issue verifying date on published second job", e);
		}
	}

	public void search_for_published_job_name2_in_hcm_sync_profiles_tab_in_pm() {
		try {
			WebElement searchBox = waitForElement(HCM_PROFILES_SEARCH);
			searchBox.click();
			searchBox.clear();
			searchBox.sendKeys(PO04_JobMappingPageComponents.orgJobNameInRow2.get());
			searchBox.sendKeys(Keys.ENTER);
			waitForSpinners();
			PerformanceUtils.waitForPageReady(driver, 2);
			PageObjectHelper.log(LOGGER, "Searched for job: " + PO04_JobMappingPageComponents.orgJobNameInRow2.get() + " in HCM Sync Profiles");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "search_for_published_job_name2_in_hcm_sync_profiles_tab_in_pm", "Failed to search for job in HCM Sync Profiles", e);
		}
	}
	
	public void search_for_published_job_code2_in_hcm_sync_profiles_tab_in_pm() {
		try {
			WebElement searchBox = waitForElement(HCM_PROFILES_SEARCH);
			searchBox.click();
			searchBox.clear();
			searchBox.sendKeys(PO04_JobMappingPageComponents.orgJobCodeInRow2.get());
			searchBox.sendKeys(Keys.ENTER);
			waitForSpinners();
			PerformanceUtils.waitForPageReady(driver, 2);
			PageObjectHelper.log(LOGGER, "Searched for job code: " + PO04_JobMappingPageComponents.orgJobCodeInRow2.get() + " in HCM Sync Profiles");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "search_for_published_job_code2_in_hcm_sync_profiles_tab_in_pm", "Failed to search for job code in HCM Sync Profiles", e);
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
				String errorMsg = String.format("Expected job '%s' but found '%s' after %d retries in HCM Sync Profiles. Search may not have filtered correctly.", 
					expectedJobName, actualJobName, maxRetries);
				ScreenshotHandler.captureFailureScreenshot("user_should_verify_published_second_job_profile_is_displayed_in_row1_in_hcm_sync_profiles_tab_in_pm", 
					new Exception(errorMsg));
				Assert.fail(errorMsg);
			}
			
			PageObjectHelper.log(LOGGER, "Published Second Job (Org: " + expectedJobName + ") is displayed in HCM Sync Profiles Row1");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_published_second_job_profile_is_displayed_in_row1_in_hcm_sync_profiles_tab_in_pm", "Issue verifying published second job profile in HCM Sync Profiles", e);
		}
	}

	// formatDateForDisplay() removed - now using inherited method from BasePageObject
}
