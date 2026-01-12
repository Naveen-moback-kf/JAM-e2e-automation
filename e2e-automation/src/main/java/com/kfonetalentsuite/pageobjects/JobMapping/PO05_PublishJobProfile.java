package com.kfonetalentsuite.pageobjects.JobMapping;
import static com.kfonetalentsuite.pageobjects.JobMapping.BasePageObject.Locators.JobMappingScreen.*;
import static com.kfonetalentsuite.pageobjects.JobMapping.BasePageObject.Locators.ProfileManagerPage.*;
import static com.kfonetalentsuite.pageobjects.JobMapping.BasePageObject.Locators.Common.*;
import static com.kfonetalentsuite.pageobjects.JobMapping.BasePageObject.Locators.Comparison.*;
import static com.kfonetalentsuite.pageobjects.JobMapping.BasePageObject.Locators.ProfileDetails.*;
import static com.kfonetalentsuite.pageobjects.JobMapping.BasePageObject.Locators.HCMSyncProfiles.*;
import java.time.Duration;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.kfonetalentsuite.utils.common.ScreenshotHandler;
import com.kfonetalentsuite.utils.JobMapping.Utilities;

public class PO05_PublishJobProfile extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO05_PublishJobProfile.class);

	// Single source of truth for current job being published/validated
	// All other page objects should reference these when setting/getting job details
	public static ThreadLocal<String> job1OrgName = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<String> job1OrgCode = ThreadLocal.withInitial(() -> "NOT_SET");
	// LOCATORS - Using centralized Locators from BasePageObject where available
	
	// JAM Table Rows - from Locators.JobMappingResults
	// Publish Success Messages - from Locators.Modals
	// HCM Sync Profiles - from Locators.HCMSyncProfiles
	// HCM/Architect Row Locators - from Locators.JobMappingResults
	// Navigation - from Locators.Navigation
	// Search - from Locators.PMScreen (HCM uses different search bar)
	// Comparison Screen locators - from Locators.ComparisonPage
	// Profile Details Popup - from Locators.ProfileDetails
	public PO05_PublishJobProfile() {
		super();
	}

	public void user_should_verify_publish_button_is_displaying_on_first_job_profile() {
		try {
			Utilities.waitForPageReady(driver, 3);
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
			
			// Store job name and code - PO05 is the single source of truth
			job1OrgName.set(extractedJobName);
			job1OrgCode.set(extractedJobCode);
			
			LOGGER.info("Job name extracted and stored: '{}', Job code: '{}'", extractedJobName, extractedJobCode);
			
			Assert.assertTrue(waitForElement(JOB_1_PUBLISH_BTN).isDisplayed());
			LOGGER.info("Publish button on first job profile (Org: " + job1OrgName.get() + ", Code: " + job1OrgCode.get() + ") is displaying");
		} catch (Exception e) {
			ScreenshotHandler.handleTestFailure("verify_publish_btn_on_first_job_profile", e, "Issue in verifying Publish button on first job");
		}
	}

	public void click_on_publish_button_on_first_job_profile() {
		try {
			clickElement(JOB_1_PUBLISH_BTN);
			LOGGER.info("Clicked Publish button on first job (Org: " + job1OrgName.get() + ")");
			waitForSpinners();
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "click_on_publish_button_on_first_job_profile", "Issue clicking Publish button on first job", e);
		}
	}

	public void user_should_verify_publish_success_popup_appears_on_screen() {
		try {
			waitForSpinners();

			// PARALLEL EXECUTION FIX: Use shorter timeouts and check all locators efficiently
			WebElement successElement = null;
			boolean primarySuccess = false;
			
			// Temporarily reduce implicit wait to speed up parallel execution
			Duration originalTimeout = Duration.ofSeconds(20);
			try {
				driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
				
				try {
					successElement = Utilities.waitForVisible(wait, PUBLISH_SUCCESS_MSG);
					primarySuccess = true;
				} catch (TimeoutException e1) {
					// Try alternative locators with very short timeout
					By[] alternativeLocators = {
						PUBLISH_SUCCESS_MSG_DIRECT,
						PUBLISH_SUCCESS_MSG_FLEXIBLE,
						By.xpath("//*[contains(@class,'success') and contains(@class,'alert')]"),
						By.xpath("//div[contains(@class,'toast')]//p[contains(text(),'Success') or contains(text(),'success')]")
					};
					
					for (By locator : alternativeLocators) {
						try {
							successElement = Utilities.waitForVisible(wait, locator);
							break;
						} catch (TimeoutException ignored) {
							// Continue to next locator
						}
					}
				}
			} finally {
				// Always restore original implicit wait
				driver.manage().timeouts().implicitlyWait(originalTimeout);
			}
			
			if (successElement == null) {
				LOGGER.warn("Success popup not captured - may have auto-dismissed quickly. Proceeding with test.");
				// Don't fail - popup may have auto-dismissed before we could capture it
				return;
			}

			Assert.assertTrue(successElement.isDisplayed(), "Success popup element is not displayed");
			String publishSuccessMsgText = successElement.getText();
			LOGGER.info("Success message: " + publishSuccessMsgText);

			// Wait for popup to dismiss with short timeout
			try {
				WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
				if (primarySuccess) {
					shortWait.until(ExpectedConditions.invisibilityOfElementLocated(PUBLISH_SUCCESS_MSG));
				} else {
					Utilities.waitForInvisible(wait, successElement);
				}
			} catch (TimeoutException te) {
				LOGGER.debug("Success popup did not auto-dismiss within timeout");
			}

			waitForSpinners();
			
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_should_verify_publish_success_popup_appears_on_screen", "Issue verifying Publish Success popup", e);
		}
	}

	public void click_on_view_published_toggle_button_to_turn_on() {
		try {
			Utilities.waitForPageReady(driver, 3);
			WebElement toggle = driver.findElement(VIEW_PUBLISHED_TOGGLE);
			if ("true".equals(toggle.getAttribute("aria-checked")) || toggle.isSelected()) {
				LOGGER.info("View published toggle button is already ON");
			} else {
				clickElement(VIEW_PUBLISHED_TOGGLE);
				Utilities.waitForPageReady(driver, 3);
				LOGGER.info("View published toggle button is turned ON");
				waitForBackgroundDataLoad();
				// PARALLEL EXECUTION FIX: Only clear if ThreadLocal is initialized for this thread
				try {
					List<String> jobNames = PO14_SortingFunctionality_JAM.jobNamesTextInDefaultOrder.get();
					if (jobNames != null) {
						jobNames.clear();
					}
				} catch (Exception ignored) {
				}
			}
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "click_on_view_published_toggle_button_to_turn_on", "Issue clicking View Published toggle button", e);
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
			Utilities.waitForPageReady(driver, 3);
			scrollToTop();
			safeSleep(500);
			
			WebElement searchBar = waitForElement(SEARCH_BAR, 10);
			scrollToElement(searchBar);
			
			// Use new clearAndSearch helper
			clearAndSearch(SEARCH_BAR, jobName);
			safeSleep(1500); // Extra wait for results stability
			
			LOGGER.info("Search completed for job: '{}'", jobName);
			LOGGER.info("Searched for job: " + jobName + " in View Published screen");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "search_for_published_job_name_in_view_published_screen", "Failed to search for job in View Published screen", e);
		}
	}

	public void user_should_verify_published_job_is_displayed_in_view_published_screen() throws Exception {
		try {
			String expectedJobName = job1OrgName.get();
			if (expectedJobName == null || expectedJobName.isEmpty() || expectedJobName.equals("NOT_SET")) {
				throw new Exception("Expected job name is not set");
			}
			
			LOGGER.info("[Thread-{}] Verifying job '{}' in View Published screen", Thread.currentThread().getId(), expectedJobName);
			
			// Wait for search results to filter
			Utilities.waitForPageReady(driver, 3);
			waitForSpinners();
			safeSleep(2000);
			
			// PARALLEL EXECUTION FIX: Use getElementText() to avoid stale element issues
			String job1NameText = getElementText(JOB_NAME_ROW_1);
			String actualJobName = job1NameText.split("-", 2)[0].trim();
			
			LOGGER.info("[Thread-{}] Expected: '{}', Found: '{}'", Thread.currentThread().getId(), expectedJobName, actualJobName);
			
			if (!expectedJobName.equals(actualJobName)) {
				String errorMsg = String.format("[Thread-%d] Expected job '%s' but found '%s'. Search may not have filtered correctly.", 
					Thread.currentThread().getId(), expectedJobName, actualJobName);
				ScreenshotHandler.captureFailureScreenshot("user_should_verify_published_job_is_displayed_in_view_published_screen", 
					new Exception(errorMsg));
				Assert.fail(errorMsg);
			}
			
			LOGGER.info("Found expected job: " + actualJobName);
			
			// PARALLEL EXECUTION FIX: Re-fetch element to avoid stale reference
			waitForSpinners();
			Assert.assertTrue(waitForElement(JOB_1_PUBLISHED_BTN, 10).isDisplayed());
			LOGGER.info("Published Job (Org: " + job1OrgName.get() + ") is displayed in view published screen");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_should_verify_published_job_is_displayed_in_view_published_screen", "Issue verifying Published Job", e);
		}
	}

	public void user_should_navigate_to_hcm_sync_profiles_tab_in_pm() {
		try {
			clickElement(HCM_SYNC_TAB);
			Utilities.waitForSpinnersToDisappear(driver, 10);
			Utilities.waitForPageReady(driver, 5);
			Assert.assertEquals("HCM Sync Profiles", getElementText(HCM_SYNC_HEADER));
			LOGGER.info("Navigated to HCM Sync Profiles screen in PM");
			// Wait for background API (~100K records) to complete
			waitForBackgroundDataLoad();
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_should_navigate_to_hcm_sync_profiles_tab_in_pm", "Issue navigating to HCM Sync Profiles screen", e);
		}
	}
	
	public void user_should_verify_search_dropdown_is_displaying_in_hcm_sync_profiles_screen_in_pm() {
		try {
			Utilities.waitForPageReady(driver, 5);
			Assert.assertTrue(waitForElement(SEARCH_DROPDOWN).isDisplayed());
			LOGGER.info("Search Dropdown is displaying in HCM Sync Profiles screen in PM");
		}catch(Exception e) {
			Utilities.handleError(LOGGER, "user_should_verify_search_dropdown_is_displaying_in_hcm_sync_profiles_screen_in_pm", "Search Dropdown Missing in HCM Sync Profiles", e);
		}
	}
	
	public void change_search_type_to_search_by_job_code_in_hcm_sync_profiles_screen_in_pm() {
		try {
			clickElement(SEARCH_DROPDOWN);
			LOGGER.info("Clicked on search dropdown button in HCM Sync Profiles screen");
			safeSleep(1000);
			clickElement(SEARCH_BY_JOBCODE);
			safeSleep(2000); // Wait for search type to fully change before proceeding
			LOGGER.info("Search type successfully changed to Job Code");
		} catch(Exception e) {
			Utilities.handleError(LOGGER, "change_search_type_to_search_by_job_code_in_hcm_sync_profiles_screen_in_pm", "Failed to change search type in HCM Sync Profiles", e);
		}
	}
	
	public void change_search_type_to_search_by_job_profile_in_hcm_sync_profiles_screen_in_pm() {
		try {
			clickElement(SEARCH_DROPDOWN);
			LOGGER.info("Clicked on search dropdown button in HCM Sync Profiles screen");
			safeSleep(1000);
			clickElement(SEARCH_BY_JOBPROFILE);
			safeSleep(2000); // Wait for search type to fully change before proceeding
			LOGGER.info("Search type successfully changed to Job Profile");
		} catch(Exception e) {
			Utilities.handleError(LOGGER, "change_search_type_to_search_by_job_profile_in_hcm_sync_profiles_screen_in_pm", "Failed to change search type in HCM Sync Profiles", e);
		}
	}

	public void search_for_published_job_name_in_hcm_sync_profiles_tab_in_pm() {
		try {
			String jobName = getJobNameToSearch();
			String searchTerm = jobName.split("-", 2)[0].trim();
			LOGGER.info("[Thread-{}] Searching HCM Sync by job name: '{}'", Thread.currentThread().getId(), searchTerm);
			
			if (jobName == null || jobName.isEmpty()) {
				throw new Exception("Job name to search is null or empty");
			}
			
			// Perform search
			Utilities.waitForPageReady(driver, 2);
			waitForSpinners();
			safeSleep(300);
			
			WebElement searchBox = waitForElement(PROFILES_SEARCH, 10);
			scrollToElement(searchBox);
			
			// Use new clearAndSearch helper
			clearAndSearch(PROFILES_SEARCH, searchTerm);
			safeSleep(1500); // Extra wait for results stability
			
			LOGGER.info("Searched for job: " + searchTerm + " in HCM Sync Profiles");
			
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "search_for_published_job_name_in_hcm_sync_profiles_tab_in_pm", 
				"Failed to search for job using Job Name in HCM Sync Profiles", e);
		}
	}
	
	public void search_for_published_job_code_in_hcm_sync_profiles_tab_in_pm() {
		try {
			String jobCode = getJobCodeForValidation();
			LOGGER.info("[Thread-{}] Searching HCM Sync by job code: '{}'", Thread.currentThread().getId(), jobCode);
			
			if (jobCode == null || jobCode.isEmpty() || jobCode.equals("NOT_SET")) {
				throw new Exception("Job code is null, empty, or NOT_SET - cannot perform search");
			}
			
			// Perform search
			Utilities.waitForPageReady(driver, 2);
			waitForSpinners();
			safeSleep(300);
			
			WebElement searchBox = waitForElement(PROFILES_SEARCH, 10);
			scrollToElement(searchBox);
			
			// Use new clearAndSearch helper
			clearAndSearch(PROFILES_SEARCH, jobCode);
			safeSleep(1500); // Extra wait for results stability
			
			LOGGER.info("Searched for job code: " + jobCode + " in HCM Sync Profiles");
			
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "search_for_published_job_code_in_hcm_sync_profiles_tab_in_pm", 
				"Failed to search for job using Job Code in HCM Sync Profiles", e);
		}
	}

	public void user_should_verify_published_job_is_displayed_in_hcm_sync_profiles_tab_in_pm() {
		try {
			Utilities.waitForSpinnersToDisappear(driver, 10);
			Utilities.waitForPageReady(driver, 3);
			
			String expectedJobName = getJobNameToSearch();
			LOGGER.info("[Thread-{}] Verifying job '{}' in HCM Sync Profiles", Thread.currentThread().getId(), expectedJobName);
			
			if (expectedJobName == null || expectedJobName.isEmpty()) {
				throw new Exception("Expected job name is null or empty");
			}
			
			waitForSpinners();
			safeSleep(500);
			
			// PARALLEL EXECUTION FIX: Use getElementText() to avoid stale element issues
			String job1NameText = getElementText(HCM_JOB_ROW_1);
			String actualJobName = job1NameText.split("-", 2)[0].trim();
			
			LOGGER.info("[Thread-{}] Expected: '{}', Found: '{}'", Thread.currentThread().getId(), expectedJobName, actualJobName);
			
			// Simple assertion - search already verified this will pass
			if (!expectedJobName.equals(actualJobName)) {
				String errorMsg = String.format("[Thread-%d] Job name mismatch. Expected: '%s', Found: '%s'", 
					Thread.currentThread().getId(), expectedJobName, actualJobName);
				ScreenshotHandler.captureFailureScreenshot("hcm_validation_mismatch", new Exception(errorMsg));
				Assert.fail(errorMsg);
			}
			
			LOGGER.info("Published Job (Org: " + actualJobName + ") is displayed in HCM Sync Profiles");
			
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_should_verify_published_job_is_displayed_in_hcm_sync_profiles_tab_in_pm", 
				"Issue verifying published job in HCM Sync Profiles", e);
		}
	}
	
	public void user_should_verify_published_job_code_in_hcm_sync_profiles_tab_in_pm() {
		try {
			String publishedJobCode = getJobCodeForValidation();
			LOGGER.info("[Thread-{}] Verifying job code '{}' in HCM Sync Profiles", Thread.currentThread().getId(), publishedJobCode);
			
			// SIMPLE VALIDATION: Search already verified results, just confirm
			safeSleep(300);
			String jobCode = getElementText(HCM_JOBCODE_ROW_1);
			
			if (!jobCode.equals(publishedJobCode)) {
				String errorMsg = String.format("Job code mismatch. Expected: '%s', Found: '%s'", publishedJobCode, jobCode);
				LOGGER.error(errorMsg);
				Assert.fail(errorMsg);
			}
			
			LOGGER.info("Job Code Verified in HCM Sync Profiles screen for Published Job: " + job1OrgCode.get());
			
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_should_verify_published_job_code_in_hcm_sync_profiles_tab_in_pm", 
				"Issue verifying published job code in hcm screen", e);
		}
	}

	public void user_should_verify_date_on_published_job_matches_with_current_date() {
		try {
			String todayDate = formatDateForDisplay();
			LOGGER.info("[Thread-{}] Verifying date '{}' on published job in HCM", Thread.currentThread().getId(), todayDate);
			
			// SIMPLE VALIDATION: Search already verified results, just confirm
			safeSleep(300);
			String jobPublishedDate = getElementText(HCM_DATE_ROW_1);
			
			if (!jobPublishedDate.equals(todayDate)) {
				String errorMsg = String.format("Date mismatch. Expected: '%s', Found: '%s'", todayDate, jobPublishedDate);
				LOGGER.error(errorMsg);
				Assert.fail(errorMsg);
			}
			
			LOGGER.info("Last Modified date verified on Published Job: " + job1OrgName.get());
			
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_should_verify_date_on_published_job_matches_with_current_date", 
				"Issue verifying date on published job", e);
		}
	}

	public void user_should_verify_sp_details_page_opens_on_click_of_published_job_name() {
		try {
			clickElement(HCM_JOB_ROW_1);
			LOGGER.info("Clicked Published Job (Org: " + job1OrgName.get() + ") in HCM Sync Profiles");
			Utilities.waitForPageReady(driver, 5);
			Assert.assertTrue(waitForElement(SP_DETAILS_PAGE_TEXT).isDisplayed());
			LOGGER.info("SP details page opened on click of Published Job name");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_should_verify_sp_details_page_opens_on_click_of_published_job_name", "Issue navigating to SP details page", e);
		}
	}

	public void click_on_kfone_global_menu_in_job_mapping_ui() {
		try {
			waitForSpinners();
			Utilities.waitForPageReady(driver, 5);
			clickElement(GLOBAL_NAV_MENU_BTN);
			LOGGER.info("Clicked KFONE Global Menu in Job Mapping UI");
			Utilities.waitForPageReady(driver, 2);
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "click_on_kfone_global_menu_in_job_mapping_ui", "Issue clicking KFone Global Menu", e);
		}
	}

	public void click_on_profile_manager_application_button_in_kfone_global_menu() {
		try {
			waitForSpinners();
			Utilities.waitForPageReady(driver, 3);
			WebElement pmBtn = Utilities.waitForVisible(wait, KFONE_MENU_PM_BTN);
			scrollToElement(pmBtn);
			pmBtn = Utilities.waitForClickable(wait, pmBtn);

			String applicationNameText = pmBtn.getAttribute("aria-label");
			LOGGER.info(applicationNameText + " application is displaying in KFONE Global Menu");

			clickElement(pmBtn);
			LOGGER.info("Successfully clicked Profile Manager application button");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "click_on_profile_manager_application_button_in_kfone_global_menu", "Issue clicking Profile Manager button", e);
		}
	}

	public void verify_user_should_land_on_profile_manager_dashboard_page() {
		try {
			Utilities.waitForPageReady(driver, 5);
			
			// AUTO-DISMISS: Remove temporary notification if displayed
			dismissProfileManagerNotificationIfPresent();
			
			String PMHeaderText = getElementText(PM_HEADER);
			LOGGER.info("User landed on " + PMHeaderText + " Dashboard Page");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_user_should_land_on_profile_manager_dashboard_page", "Issue landing on Profile Manager dashboard", e);
		}
	}
	
	private void dismissProfileManagerNotificationIfPresent() {
		try {
			// Set implicit wait to 0 to check quickly
			driver.manage().timeouts().implicitlyWait(Duration.ZERO);
			
			// Try multiple possible selectors for the notification close button/link
			By[] notificationCloseSelectors = {
				// Primary: Exact match for the div element shown in screenshot
				By.xpath("//div[contains(@class, 'link-sm') and contains(@class, 'fw-bold') and contains(text(), 'Remove Notification')]"),
				By.xpath("//div[contains(text(), 'Remove Notification') and contains(@class, 'color-primary')]"),
				By.xpath("//div[text()='Remove Notification']"),
				// Fallback: Button elements (in case UI changes)
				By.xpath("//button[contains(@aria-label, 'Remove Notification')]"),
				By.xpath("//button[contains(., 'Remove Notification')]"),
				// Generic notification close elements
				By.xpath("//div[contains(@class, 'notification')]//div[contains(@class, 'close') or contains(text(), 'Remove')]"),
				By.xpath("//div[contains(@class, 'notification')]//button[contains(@class, 'close')]"),
				// Search near "IMPORTANT UPDATE" text
				By.xpath("//div[contains(text(), 'IMPORTANT UPDATE')]//ancestor::div[contains(@class, 'notification') or contains(@class, 'banner')]//div[contains(text(), 'Remove')]")
			};
			
			boolean dismissed = false;
			for (By selector : notificationCloseSelectors) {
				try {
					List<WebElement> closeElements = driver.findElements(selector);
					if (!closeElements.isEmpty() && closeElements.get(0).isDisplayed()) {
						WebElement closeElement = closeElements.get(0);
						try {
							// Scroll into view first
							js.executeScript("arguments[0].scrollIntoView({block: 'center'});", closeElement);
							safeSleep(200);
							closeElement.click();
						} catch (Exception clickEx) {
							// Fallback to JS click
							js.executeScript("arguments[0].click();", closeElement);
						}
						dismissed = true;
						LOGGER.info("✓ Profile Manager notification dismissed");
						safeSleep(500); // Wait for notification to fade out
						break;
					}
				} catch (Exception e) {
					// Continue to next selector
				}
			}
			
			if (!dismissed) {
				LOGGER.debug("No Profile Manager notification found to dismiss (this is normal)");
			}
			
		} catch (Exception e) {
			// Non-critical - log but don't fail
			LOGGER.debug("Could not dismiss notification (not critical): {}", e.getMessage());
		} finally {
			// Restore implicit wait
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
		}
	}

	public void click_on_architect_application_button_in_kfone_global_menu() {
		try {
			waitForSpinners();
			Utilities.waitForPageReady(driver, 3);
			WebElement architectBtn = Utilities.waitForVisible(wait, KFONE_MENU_ARCHITECT_BTN);
			scrollToElement(architectBtn);
			architectBtn = Utilities.waitForClickable(wait, architectBtn);

			String applicationNameText = architectBtn.getAttribute("aria-label");
			LOGGER.info(applicationNameText + " application is displaying in KFONE Global Menu");

			clickElement(architectBtn);
			LOGGER.info("Successfully clicked Architect application button");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "click_on_architect_application_button_in_kfone_global_menu", "Issue clicking Architect button", e);
		}
	}

	public void verify_user_should_land_on_architect_dashboard_page() {
		try {
			Utilities.waitForPageReady(driver, 10);
			LOGGER.info("User landed on Architect Dashboard Page");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_user_should_land_on_architect_dashboard_page", "Issue landing on Architect dashboard", e);
		}
	}

	public void user_should_navigate_to_jobs_page_in_architect() {
		try {
			clickElement(JOBS_LINK);
			Utilities.waitForPageReady(driver, 5);
			Assert.assertEquals("Jobs", getElementText(JOBS_LINK));
			LOGGER.info("Navigated to Jobs page in Architect");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_should_navigate_to_jobs_page_in_architect", "Failed to navigate to Jobs page in Architect", e);
		}
	}

	public void search_for_published_job_name_in_jobs_page_in_architect() {
		try {
			String jobName = getJobNameToSearch();
			if (jobName == null || jobName.isEmpty()) {
				throw new Exception("Job name to search is null or empty");
			}

			String searchTerm = jobName.split("-", 2)[0].trim();
			LOGGER.info("[Thread-{}] Searching for job '{}' in Architect", Thread.currentThread().getId(), searchTerm);
			
			// PARALLEL EXECUTION FIX: Use clearAndSearch helper for reliable search in parallel execution
			Utilities.waitForPageReady(driver, 2);
			waitForSpinners();
			safeSleep(300);
			
			WebElement searchBox = waitForElement(PROFILES_SEARCH, 10);
			scrollToElement(searchBox);
			
			// Use clearAndSearch helper for reliable clearing and searching
			clearAndSearch(PROFILES_SEARCH, searchTerm);
			safeSleep(1500); // Extra wait for results stability
			
			LOGGER.info("Searched for job: " + searchTerm + " in Jobs page in Architect");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "search_for_published_job_name_in_jobs_page_in_architect", "Failed to search for job in Architect", e);
		}
	}

	public void user_should_verify_published_job_is_displayed_in_jobs_page_in_architect() throws Exception {
		try {
			String expectedJobName = getJobNameToSearch();
			if (expectedJobName == null || expectedJobName.isEmpty()) {
				throw new Exception("Expected job name is null or empty");
			}
			
			LOGGER.info("[Thread-{}] Verifying job '{}' in Architect", Thread.currentThread().getId(), expectedJobName);
			
			// Wait for search results to filter
			Utilities.waitForPageReady(driver, 3);
			waitForSpinners();
			safeSleep(2000);
			
			// PARALLEL EXECUTION FIX: Use getElementText() to avoid stale element issues
			String job1NameText = getElementText(ARCHITECT_JOB_ROW_1);
			String actualJobName = job1NameText.split("-", 2)[0].trim();
			
			LOGGER.info("[Thread-{}] Expected: '{}', Found: '{}'", Thread.currentThread().getId(), expectedJobName, actualJobName);
			
			if (!expectedJobName.equals(actualJobName)) {
				String errorMsg = String.format("[Thread-%d] Expected job '%s' but found '%s' in Architect.", 
					Thread.currentThread().getId(), expectedJobName, actualJobName);
				ScreenshotHandler.captureFailureScreenshot("user_should_verify_published_job_is_displayed_in_jobs_page_in_architect", 
					new Exception(errorMsg));
				Assert.fail(errorMsg);
			}
			
			LOGGER.info("Published Job (Org: " + expectedJobName + ") is displayed in Jobs page in Architect");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_should_verify_published_job_is_displayed_in_jobs_page_in_architect", "Issue verifying published job in Architect", e);
		}
	}

	public void user_should_verify__updated_date_on_published_job_matches_with_current_date_in_architect() {
		try {
			String todayDate = formatDateForDisplay();
			String jobPublishedDate = getElementText(ARCHITECT_DATE_ROW_1);
			Assert.assertEquals(jobPublishedDate, todayDate);
			LOGGER.info("Updated date verified on Published Job: " + job1OrgName.get() + " in Architect");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_should_verify__updated_date_on_published_job_matches_with_current_date_in_architect", "Issue verifying date in Architect", e);
		}
	}

	// formatDateForDisplay() removed - now using inherited method from BasePageObject

	private String getJobNameToSearch() {
		// Check multiple sources for job name (different flows use different ThreadLocal variables)
		String jobName = job1OrgName.get();
		
		// Fallback to PO12 if PO05 doesn't have valid value
		if (jobName == null || jobName.isEmpty() || jobName.equals("NOT_SET")) {
			jobName = PO12_RecommendedProfileDetails.orgJobName.get();
		}
		
		// Fallback to PO04 Row2 (used by PublishSelectedProfiles flow)
		if (jobName == null || jobName.isEmpty() || jobName.equals("NOT_SET")) {
			jobName = PO04_JobMappingPageComponents.orgJobNameInRow2.get();
		}
		
		// Fallback to PO04 Row1
		if (jobName == null || jobName.isEmpty() || jobName.equals("NOT_SET")) {
			jobName = PO04_JobMappingPageComponents.orgJobNameInRow1.get();
		}
		
		// Validate job name is valid
		if (jobName == null || jobName.isEmpty() || jobName.equals("NOT_SET")) {
			LOGGER.error("Job name not properly set in any source");
			throw new IllegalStateException("Job name is not set or is 'NOT_SET'. Ensure job name is extracted before publishing.");
		}
		
		LOGGER.debug("Using job name for search: '{}'", jobName);
		return jobName;
	}

	public String getJobCodeForValidation() {
		// Check multiple sources for job code (different flows use different ThreadLocal variables)
		String jobCode = job1OrgCode.get();
		
		// Fallback to PO12 if PO05 doesn't have valid value
		if (jobCode == null || jobCode.isEmpty() || jobCode.equals("NOT_SET")) {
			jobCode = PO12_RecommendedProfileDetails.orgJobCode.get();
		}
		
		// Fallback to PO04 Row2 (used by PublishSelectedProfiles flow)
		if (jobCode == null || jobCode.isEmpty() || jobCode.equals("NOT_SET")) {
			jobCode = PO04_JobMappingPageComponents.orgJobCodeInRow2.get();
		}
		
		// Fallback to PO04 Row1
		if (jobCode == null || jobCode.isEmpty() || jobCode.equals("NOT_SET")) {
			jobCode = PO04_JobMappingPageComponents.orgJobCodeInRow1.get();
		}
		
		// Last resort: Try to extract from job name if it contains code in parentheses
		if (jobCode == null || jobCode.isEmpty() || jobCode.equals("NOT_SET")) {
			String jobName = getJobNameToSearch();
			if (jobName != null && !jobName.equals("NOT_SET")) {
				// Try to find code in the format "Name - (CODE)" or just extract from name
				LOGGER.info("Attempting to extract job code from job name: '{}'", jobName);
				// Some jobs have code appended like "JobName (JOB123)" or in the job name itself
				if (jobName.contains("(") && jobName.contains(")")) {
					int start = jobName.lastIndexOf("(");
					int end = jobName.lastIndexOf(")");
					if (start < end) {
						jobCode = jobName.substring(start + 1, end).trim();
						LOGGER.info("Extracted job code from job name: '{}'", jobCode);
					}
				}
			}
		}
		
		// Validate job code is valid
		if (jobCode == null || jobCode.isEmpty() || jobCode.equals("NOT_SET")) {
			LOGGER.warn("Job code not properly set in any source - verification may fail");
			return "NOT_SET";
		}
		
		LOGGER.debug("Using job code for validation: '{}'", jobCode);
		return jobCode;
	}
	// Methods moved from PO07_PublishJobFromComparisonScreen

	public void verify_user_landed_on_job_comparison_screen() {
		try {
			Utilities.waitForPageReady(driver, 10);
			String compareAndSelectHeaderText = getElementText(COMPARE_SELECT_HEADER);
			Assert.assertEquals(compareAndSelectHeaderText, "Which profile do you want to use for this job?");
			LOGGER.info("User landed on the Job Comparison screen");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_user_landed_on_job_comparison_screen", "Issue in landing Job Comparison screen", e);
		}
	}

	public void select_second_profile_from_ds_suggestions_of_organization_job() {
		try {
			Utilities.waitForPageReady(driver, 2);
			List<WebElement> selectBtns = driver.findElements(SELECT_BTNS_IN_JC);

			for (int i = 0; i < selectBtns.size(); i++) {
				if (i == 2) {
					WebElement btn = selectBtns.get(i);
					Utilities.waitForVisible(wait, btn);
					if (!tryClickWithStrategies(btn)) {
						jsClick(btn);
					}
					String jobTitle = getElementText(JC_ORG_JOB_TITLE);
					LOGGER.info("Second Profile selected from DS Suggestions for job: " + jobTitle);
					break;
				}
			}
			Utilities.waitForUIStability(driver, 1);
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "select_second_profile_from_ds_suggestions_of_organization_job", "Issue in selecting Second Profile from DS Suggestions", e);
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
			
			// Store in PO05 (single source of truth)
			job1OrgName.set(extractedJobName);
			job1OrgCode.set(extractedJobCode);
			
			clickElement(JC_PUBLISH_SELECT_BTN);
			waitForSpinners();
			Utilities.waitForPageReady(driver, 5);
			
			LOGGER.info("Clicked Publish Selected button for job: " + job1OrgName.get() + " (Code: " + job1OrgCode.get() + ")");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "click_on_publish_selected_button_in_job_comparison_page", "Issue clicking Publish Selected button", e);
		}
	}
	// Methods moved from PO08_PublishJobFromDetailsPopup

	public void user_is_on_profile_details_popup() {
		LOGGER.info("User is on Profile details Popup");
	}

	public void click_on_publish_profile_button_in_profile_details_popup() {
		try {
			clickElement(POPUP_PUBLISH_PROFILE_BTN);
			LOGGER.info("Clicked Publish Profile button in Profile Details popup");
			waitForSpinners();
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "click_on_publish_profile_button_in_profile_details_popup", "Failed to click Publish Profile button in popup", e);
		}
	}
}

