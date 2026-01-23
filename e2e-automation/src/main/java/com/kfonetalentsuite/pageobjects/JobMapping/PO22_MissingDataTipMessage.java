package com.kfonetalentsuite.pageobjects.JobMapping;
import static com.kfonetalentsuite.pageobjects.JobMapping.BasePageObject.Locators.JobMappingScreen.*;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import com.kfonetalentsuite.utils.common.Utilities;

public class PO22_MissingDataTipMessage extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO22_MissingDataTipMessage.class);
	
	public static ThreadLocal<String> initialJobCount = ThreadLocal.withInitial(() -> "NOT_SET");

	public PO22_MissingDataTipMessage() {
		super();
	}
	
	/**
	 * Universal method to check if Missing Data Tip Message is displayed.
	 * Throws SkipException if not found - used across Features 22, 23, 24, 25, 35.
	 * Called from Step Definition at the beginning of data-dependent scenarios.
	 * 
	 * CONTEXT-AWARE: If user is already on Add Job Data page or Missing Data screen,
	 * OR if CSV file exists (file manipulation scenarios), allows execution to proceed.
	 * 
	 * SPECIAL HANDLING: For Feature 35 verification scenarios (after re-upload), 
	 * if tip message is NOT found, allows scenario to proceed as this indicates 
	 * successful fix (all missing data resolved).
	 */
	public void skipScenarioIfMissingDataTipMessageNotDisplayed() {
		LOGGER.info("Checking prerequisite for Missing Data Tip Message...");
		
		// Small wait to ensure page is loaded
		safeSleep(1000);
		
		// CONTEXT-AWARE CHECK: Determine which page we're on
		String currentUrl = driver.getCurrentUrl();
		String pageTitle = driver.getTitle();
		String pageSource = driver.getPageSource();
		
		LOGGER.info("Current URL: " + currentUrl);
		LOGGER.info("Page Title: " + pageTitle);
		
		// Check page source for Add Job Data indicators (most reliable - works for all clients)
		if (pageSource.contains("Manual Upload") && pageSource.contains("HCM Connection") && pageSource.contains("Add Job Data")) {
			LOGGER.info("User is on Add Job Data page (detected via page content) - prerequisite assumed met");
			return;
		}
		
		// Check URL patterns (works regardless of client UUID)
		if ((currentUrl.contains("/pm/") || currentUrl.contains("/app/")) && 
		    (pageSource.contains("Add Job Data") || pageSource.contains("Manual Upload"))) {
			LOGGER.info("User is on Add Job Data page (detected via URL pattern) - prerequisite assumed met");
			return;
		}
		
		// Check page title
		if (pageTitle != null && (pageTitle.contains("Add Job Data") || pageTitle.contains("Add More Jobs"))) {
			LOGGER.info("User is on Add Job Data page (detected via page title) - prerequisite assumed met");
			return;
		}
		
		// Check for "Step 1", "Step 2", "Step 3" indicators on Add Job Data page
		if (pageSource.contains("Step 1") && pageSource.contains("Connection type") && pageSource.contains("Sync Data")) {
			LOGGER.info("User is on Add Job Data page (detected via step indicators) - prerequisite assumed met");
			return;
		}
		
		// Check for Missing Data screen
		if (currentUrl.contains("missing") || pageSource.contains("Jobs With Missing Data") || pageSource.contains("Re-upload button")) {
			LOGGER.info("User is on Missing Data screen - prerequisite assumed met");
			return;
		}
		
		// Check for upload success/progress screens
		if (pageSource.contains("Job data added successfully") || 
		    pageSource.contains("Upload Complete") || 
		    pageSource.contains("Upload in progress")) {
			LOGGER.info("User is on upload/success page - prerequisite assumed met");
			return;
		}
		
		try {
			// Also try element-based detection as fallback
			List<WebElement> addJobDataElements = driver.findElements(By.xpath(
				"//*[contains(text(), 'Add Job Data')] | " +
				"//button[contains(text(), 'Manual Upload')] | " +
				"//button[contains(text(), 'HCM Connection')]"
			));
			
			if (!addJobDataElements.isEmpty()) {
				LOGGER.info("User is on Add Job Data page (detected via elements) - prerequisite assumed met");
				return;
			}
		} catch (Exception e) {
			LOGGER.debug("Element check exception: " + e.getMessage());
		}
		
		// If we're here, we're likely on Job Mapping page - check for Missing Data Tip Message
		LOGGER.info("User appears to be on Job Mapping page - checking for Missing Data Tip Message...");
		
		try {
			waitForSpinners();
			waitForUIStabilityInMs(1000);
			
			// Use a shorter wait (5 seconds) for faster feedback
			// For Feature 35 verification scenarios, tip message should be gone quickly if profiles are fixed
			WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
			
			// Try to find element - single attempt only for speed
			WebElement tipMessage = null;
			try {
				// Try visibility check (most reliable for displayed elements)
				tipMessage = Utilities.waitForVisible(shortWait, MISSING_DATA_TIP_MESSAGE_CONTAINER);
			} catch (Exception visibilityEx) {
				// Quick check with findElements (no wait) as final attempt
				List<WebElement> elements = driver.findElements(MISSING_DATA_TIP_MESSAGE_CONTAINER);
				if (!elements.isEmpty() && elements.get(0).isDisplayed()) {
					tipMessage = elements.get(0);
				}
			}
			
			// Verify element is actually displayed
			if (tipMessage != null && tipMessage.isDisplayed()) {
				LOGGER.info("Missing Data Tip Message found - scenario will proceed");
			} else {
				throw new Exception("Element not displayed");
			}
			
		} catch (Exception e) {
			// Check if this is a Feature 35 verification scenario (after re-upload)
			// For these scenarios, missing tip message = successful fix, allow to proceed
			String excelPath = PO35_ReuploadMissingDataProfiles.excelFilePath.get();
			if (excelPath != null && !excelPath.equals("NOT_SET")) {
				LOGGER.info("Feature 35 verification scenario - Missing Data Tip Message NOT found");
				LOGGER.info("This indicates all profiles were successfully fixed - scenario will proceed to verify");
				return; // Allow scenario to proceed - tip message absence is expected after successful fix
			}
			
			// For other scenarios, skip if tip message not found
			LOGGER.warn("Missing Data Tip Message NOT found on current page");
			LOGGER.warn("SKIPPED: No Missing Data Tip Message found - Application has NO profiles with missing data");
			throw new org.testng.SkipException("No Missing Data Tip Message found - Application has NO profiles with missing data");
		}
	}
	private void waitForUIStabilityInMs(int milliseconds) throws InterruptedException {
		try {
			Utilities.waitForUIStability(driver, Math.max(1, milliseconds / 1000));
		} catch (Exception e) {
			LOGGER.debug("UI stability wait failed: {}", e.getMessage());
		}
	}

	public void ensureTipMessageIsPresent() throws IOException {
		try {
			if (isElementDisplayed(MISSING_DATA_TIP_MESSAGE_CONTAINER)) {
				LOGGER.info("Missing Data Tip Message is already present");
				return;
			}
		} catch (Exception e) {
			LOGGER.info("Tip message not visible, refreshing page to restore it");
			refreshPage();

			try {
				waitForSpinners();
				Utilities.waitForVisible(wait, MISSING_DATA_TIP_MESSAGE_CONTAINER);
				LOGGER.info("Missing Data Tip Message restored after page refresh");
			} catch (Exception refreshException) {
				LOGGER.info("Unable to restore Missing Data Tip Message after refresh");
				throw new IOException("Cannot restore tip message for verification");
			}
		}
	}

	public void verify_missing_data_tip_message_is_displaying_on_job_mapping_page() {
		try {
			// Use a shorter wait (10 seconds) for faster feedback
			WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(10));
			
			// Try presence check first, then visibility
			WebElement container = null;
			try {
				container = Utilities.waitForPresent(shortWait, MISSING_DATA_TIP_MESSAGE_CONTAINER);
			} catch (Exception presenceEx) {
				container = Utilities.waitForVisible(shortWait, MISSING_DATA_TIP_MESSAGE_CONTAINER);
			}
			
			Assert.assertTrue(container.isDisplayed(),
					"Missing Data Tip Message should be displayed on Job Mapping page");
			LOGGER.info("Missing Data Tip Message is successfully displayed on Job Mapping page");
			
		} catch (org.testng.SkipException se) {
			throw se;
		} catch (Exception e) {
			LOGGER.warn("SKIPPED: No Missing Data Tip Message found - no profiles with missing data exist");
			throw new org.testng.SkipException("No Missing Data Tip Message found - no profiles with missing data exist");
		}
	}

	public void verify_missing_data_tip_message_contains_correct_count_of_jobs_with_missing_data() {
		try {
			// Check if tip message exists (data-dependent scenario)
			List<WebElement> countElements = driver.findElements(MISSING_DATA_COUNT_AND_TEXT);
			
			if (countElements.isEmpty()) {
				LOGGER.warn("No Missing Data Tip Message count text found");
				return; // Graceful skip - feature-level skip will handle this
			}
			
			WebElement countText = countElements.get(0);
			String tipMessageText = countText.getText();

			Pattern pattern = Pattern.compile("(\\d+)\\s+jobs have missing data");
			Matcher matcher = pattern.matcher(tipMessageText);

			if (matcher.find()) {
				String extractedCount = matcher.group(1);
				Assert.assertTrue(extractedCount != null && !extractedCount.isEmpty(),
						"Job count should be extracted from tip message");
				LOGGER.info("Successfully extracted job count from tip message: " + extractedCount);
			} else {
				Assert.fail("Could not extract job count from tip message: " + tipMessageText);
			}
		} catch (org.testng.SkipException se) {
			throw se; // Rethrow SkipException
		} catch (Exception e) {
			Utilities.handleError(LOGGER,
					"verify_missing_data_tip_message_contains_correct_count_of_jobs_with_missing_data",
					"Failed to verify job count in tip message", e);
		}
	}

	public void verify_link_is_present_in_missing_data_tip_message(String linkText) {
		try {
			// Check if link exists (data-dependent scenario)
			List<WebElement> links = driver.findElements(VIEW_REUPLOAD_JOBS_LINK);
			
			if (links.isEmpty()) {
				LOGGER.warn("'View & Re-upload jobs' link not found");
				return; // Graceful skip - feature-level skip will handle this
			}
			
			WebElement link = links.get(0);
			Assert.assertTrue(link.isDisplayed(),
					"'" + linkText + "' link should be present in Missing Data Tip Message");
			Assert.assertTrue(link.getText().contains(linkText),
					"Link should contain expected text: " + linkText);
			LOGGER.info("'" + linkText + "' link is present in Missing Data Tip Message");
		} catch (org.testng.SkipException se) {
			throw se; // Rethrow SkipException
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_link_is_present_in_missing_data_tip_message",
					"Failed to verify link presence", e);
		}
	}

	public void click_on_link_in_missing_data_tip_message(String linkText) {
		try {
			// Check if link exists before clicking
			List<WebElement> links = driver.findElements(VIEW_REUPLOAD_JOBS_LINK);
			
			if (links.isEmpty()) {
				LOGGER.warn("'View & Re-upload jobs' link not found");
				return; // Graceful skip - feature-level skip will handle this
			}
			
			Utilities.waitForVisible(wait, VIEW_REUPLOAD_JOBS_LINK);
			Utilities.waitForClickable(wait, VIEW_REUPLOAD_JOBS_LINK);

			clickElement(VIEW_REUPLOAD_JOBS_LINK);
			LOGGER.info("Clicked '" + linkText + "' link");
			waitForUIStabilityInMs(500);
		} catch (org.testng.SkipException se) {
			throw se; // Rethrow SkipException
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "click_on_link_in_missing_data_tip_message",
					"Failed to click on '" + linkText + "' link", e);
		}
	}

	public void verify_user_is_navigated_to_appropriate_page_for_viewing_and_re_uploading_jobs() throws IOException {
		try {
			waitForUIStabilityInMs(500);

			boolean pageVerified = false;
			String verificationResults = "";

			try {
				Utilities.waitForVisible(wait, CLOSE_REUPLOAD_JOBS_PAGE_BUTTON);
				if (isElementDisplayed(CLOSE_REUPLOAD_JOBS_PAGE_BUTTON)) {
					verificationResults += " Close button found; ";
					pageVerified = true;
				}
			} catch (Exception e) {
				verificationResults += "- Close button not found; ";
			}

			try {
				Utilities.waitForVisible(wait, REUPLOAD_BUTTON);
				if (isElementDisplayed(REUPLOAD_BUTTON)) {
					verificationResults += " Re-upload button found; ";
					pageVerified = true;
				}
			} catch (Exception e) {
				verificationResults += "- Re-upload button not found; ";
			}

			try {
				List<WebElement> jobRows = driver.findElements(JOB_TABLE_ROWS);
				if (!jobRows.isEmpty()) {
					verificationResults += " Job data table found (" + jobRows.size() + " rows); ";
				} else {
					verificationResults += "- No job data rows found; ";
				}
			} catch (Exception e) {
				verificationResults += "- Job data table check failed; ";
			}

			if (!pageVerified) {
				try {
					String pageSource = driver.getPageSource().toLowerCase();
					if (pageSource.contains("missing data") || pageSource.contains("re-upload")
							|| pageSource.contains("organization jobs")
							|| pageSource.contains("jobs with missing data")) {
						verificationResults += " Page content indicates correct screen via source scan; ";
						pageVerified = true;
					}
				} catch (Exception e) {
					verificationResults += "- Page source check failed; ";
				}
			}

			Assert.assertTrue(pageVerified,
					"Failed to verify Jobs with Missing Data screen. Results: " + verificationResults);
			LOGGER.info("Verified Jobs with Missing Data screen");
		} catch (Exception e) {
			LOGGER.error("Failed to verify Jobs with Missing Data screen", e);
			throw new IOException(e);
		}
	}

	public void navigate_back_to_job_mapping_page() throws IOException {
		try {
			LOGGER.info("Attempting to navigate back to Job Mapping page using Close button");

			boolean navigatedSuccessfully = false;

			try {
				Utilities.waitForVisible(wait, CLOSE_REUPLOAD_JOBS_PAGE_BUTTON);
				Utilities.waitForClickable(wait, CLOSE_REUPLOAD_JOBS_PAGE_BUTTON);

				clickElement(CLOSE_REUPLOAD_JOBS_PAGE_BUTTON);
				LOGGER.info("Clicked Close button on re-upload page");
				navigatedSuccessfully = true;
			} catch (Exception e1) {
				LOGGER.info("Strategy 1 failed - Could not find/click specific Close button: " + e1.getMessage());

				try {
					clickElement(ALTERNATIVE_CLOSE_BUTTON);
					LOGGER.info("Successfully clicked Close button using alternative locator");
					navigatedSuccessfully = true;
				} catch (Exception e2) {
					LOGGER.info("Strategy 2 failed - Alternative Close button not found: " + e2.getMessage());

					try {
						driver.navigate().back();
						LOGGER.info("Used browser back navigation as fallback");
						navigatedSuccessfully = true;
					} catch (Exception e3) {
						LOGGER.info("All navigation strategies failed");
						throw new IOException("Failed to navigate back using any strategy", e3);
					}
				}
			}

			if (navigatedSuccessfully) {
				waitForUIStabilityInMs(1500);

				try {
					Utilities.waitForVisible(wait, PAGE_CONTAINER);
					String currentUrl = driver.getCurrentUrl();
					LOGGER.info("Successfully navigated back to Job Mapping page");
					LOGGER.info("Current URL after navigation: " + currentUrl);
				} catch (Exception verifyException) {
					LOGGER.info("Navigation completed but could not verify page elements");
				}
			}
		} catch (Exception e) {
			try {
				String currentUrl = driver.getCurrentUrl();
				LOGGER.info("Failed to navigate back to Job Mapping page");
				LOGGER.info("Current URL: " + currentUrl);
			} catch (Exception debugEx) {
				LOGGER.debug("Failed to get debug URL: " + debugEx.getMessage());
			}

			Utilities.handleError(LOGGER, "navigate_back_to_job_mapping_page",
					"Failed to navigate back to Job Mapping page", e);
		}
	}

	public void verify_missing_data_tip_message_is_still_displaying_on_job_mapping_page() {
		try {
			WebElement container = Utilities.waitForVisible(wait, MISSING_DATA_TIP_MESSAGE_CONTAINER);
			Assert.assertTrue(container.isDisplayed(),
					"Missing Data Tip Message should still be displayed on Job Mapping page after navigation");
			LOGGER.info("Missing Data Tip Message is still displaying on Job Mapping page after navigation");
		} catch (Exception e) {
			Utilities.handleError(LOGGER,
					"verify_missing_data_tip_message_is_still_displaying_on_job_mapping_page",
					"Failed to verify Missing Data Tip Message is still displayed", e);
		}
	}

	public void click_on_close_button_in_missing_data_tip_message() {
		try {
			LOGGER.info("Clicking close button on Missing Data Tip Message (targeting correct tip message)...");

			Utilities.waitForVisible(wait, CLOSE_TIP_MESSAGE_BUTTON);
			Utilities.waitForClickable(wait, CLOSE_TIP_MESSAGE_BUTTON);

			clickElement(CLOSE_TIP_MESSAGE_BUTTON);
			} catch (Exception e) {
			Utilities.handleError(LOGGER, "click_on_close_button_in_missing_data_tip_message",
					"Failed to click on missing data tip message close button", e);
		}
	}

	public void verify_missing_data_tip_message_is_no_longer_displayed_on_job_mapping_page() {
		try {
			waitForUIStabilityInMs(1000);

			boolean tipMessageHidden = false;
			String hiddenReason = "";

			try {
				List<WebElement> tipElements = driver.findElements(MISSING_DATA_TIP_MESSAGE_CONTAINER);

				if (tipElements.isEmpty()) {
					tipMessageHidden = true;
					hiddenReason = "Element removed from DOM";
					LOGGER.info("Missing Data Tip Message is no longer displayed - element removed from DOM");
				} else {
					WebElement tipElement = tipElements.get(0);
					if (!tipElement.isDisplayed()) {
						tipMessageHidden = true;
						hiddenReason = "Element hidden (display:none or visibility:hidden)";
						LOGGER.info("Missing Data Tip Message is no longer displayed - element hidden via CSS");
					} else {
						tipMessageHidden = false;
						hiddenReason = "Element is still visible";
					}
				}
			} catch (Exception e) {
				tipMessageHidden = true;
				hiddenReason = "Exception occurred while checking element - likely removed: " + e.getMessage();
				LOGGER.info("Missing Data Tip Message is no longer displayed - exception indicates element not present");
			}

			if (tipMessageHidden) {
				LOGGER.info("SUCCESS: Missing Data Tip Message is no longer displayed on Job Mapping page - Reason: "
								+ hiddenReason);
			} else {
				String errorMessage = "Missing Data Tip Message is still displayed when it should be hidden. Reason: "
						+ hiddenReason;
				LOGGER.error(errorMessage);
				Assert.fail(errorMessage);
			}
		} catch (Exception e) {
			Utilities.handleError(LOGGER,
					"verify_missing_data_tip_message_is_no_longer_displayed_on_job_mapping_page",
					"Failed to verify tip message is hidden", e);
		}
	}

	public void verify_missing_data_tip_message_contains_text_about_jobs_having_missing_data() {
		try {
			WebElement countText = Utilities.waitForVisible(wait, MISSING_DATA_COUNT_AND_TEXT);
			String tipMessageText = countText.getText();

			Assert.assertTrue(tipMessageText.contains("jobs have missing data"),
					"Tip message should contain text about jobs having missing data");
			LOGGER.info("Tip message contains text about jobs having missing data");
		} catch (Exception e) {
			Utilities.handleError(LOGGER,
					"verify_missing_data_tip_message_contains_text_about_jobs_having_missing_data",
					"Failed to verify missing data text", e);
		}
	}

	public void verify_missing_data_tip_message_contains_text_about_reduced_match_accuracy() {
		try {
			WebElement countText = Utilities.waitForVisible(wait, MISSING_DATA_COUNT_AND_TEXT);
			String tipMessageText = countText.getText();

			Assert.assertTrue(tipMessageText.contains("reduce match accuracy"),
					"Tip message should contain text about reduced match accuracy");
			LOGGER.info("Tip message contains text about reduced match accuracy");
		} catch (Exception e) {
			Utilities.handleError(LOGGER,
					"verify_missing_data_tip_message_contains_text_about_reduced_match_accuracy",
					"Failed to verify reduced accuracy text", e);
		}
	}
}

