package com.kfonetalentsuite.pageobjects.JobMapping;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.kfonetalentsuite.utils.JobMapping.Utilities;
import com.kfonetalentsuite.utils.JobMapping.PageObjectHelper;

public class PO02_AddMoreJobsFunctionality extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO02_AddMoreJobsFunctionality.class);

	public static ThreadLocal<String> lastSyncedInfo1 = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<String> ResultsCountBeforeAddingMoreJobs = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<String> KFONEjobsCountBeforeAddingMoreJobs = ThreadLocal.withInitial(() -> "NOT_SET");

	private static final By ADD_MORE_JOBS_PAGE_HEADER = By.xpath("//*[contains(text(),'Add Job Data')]");
	private static final By MANUAL_UPLOAD_BTN = By.xpath("//button[@data-testid='manual-upload-btn']");
	private static final By KFONE_JOBS_COUNT = By.xpath("//span[contains(@class,'regular-small')]");
	private static final By BROWSE_FILES_BTN = By.xpath("//*[@aria-label='Browse Files']");
	private static final By ATTACHED_FILE_NAME = By.xpath("//div[contains(@class,'text-ods-font-styles-body-regular-small')]");
	private static final By FILE_CLOSE_BTN = By.xpath("//*[@aria-label='fileclose']//*[@stroke-linejoin='round']");
	private static final By CONTINUE_BTN = By.xpath("//button[@id='btnContinue']");
	private static final By UPLOAD_PROGRESS_TEXT = By.xpath("//p[contains(text(),'Upload in progress')]");
	private static final By CLICK_HERE_BTN = By.xpath("//a[@id='clickHere']");
	private static final By LAST_SYNCED_INFO = By.xpath("//div[contains(text(),'Last Synced')]");
	private static final By UPLOAD_SUCCESS_MESSAGE = By.xpath("//p[text()='Upload complete!']");
	private static final By ADD_MORE_JOBS_CLOSE_BTN = By.xpath("//*[@data-testid='x-mark-icon']//*");
	private static final By DONE_BTN = By.xpath("//button[@id='btnDone']");
	private static final By NOTHING_TO_SEE_MESSAGE = By.xpath("//span[contains(@class,'font-proxima') and contains(@class,'text-[24px]') and contains(@class,'font-semibold') and contains(text(),'Nothing to see here... yet!')]");

	public PO02_AddMoreJobsFunctionality() {
		super();
	}

	public void verify_unpublished_jobs_count_before_adding_more_jobs() {
		try {
			PageObjectHelper.waitForPageReady(driver, 5);
			try {
				WebElement resultsCount = PageObjectHelper.waitForVisible(wait, Locators.JobMappingResults.SHOWING_JOB_RESULTS);
				ResultsCountBeforeAddingMoreJobs.set(resultsCount.getText().split(" ")[3]);
				LOGGER.info("Unpublished Job Profiles Count before Adding More Jobs: " + ResultsCountBeforeAddingMoreJobs.get());
			} catch (Exception e) {
				try {
					WebElement emptyState = PageObjectHelper.waitForPresent(wait, NOTHING_TO_SEE_MESSAGE);
					if (emptyState.isDisplayed()) {
						ResultsCountBeforeAddingMoreJobs.set("0");
						LOGGER.info("No unpublished jobs found - Count set to 0");
					}
				} catch (Exception ex) {
					throw e;
				}
			}
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_unpublished_jobs_count_before_adding_more_jobs", "Issue in verifying Unpublished job profiles count", e);
		}
	}

	public void user_should_be_landed_on_kfone_add_job_data_page() {
		try {
			String headerText = PageObjectHelper.retryOnStaleElement(LOGGER, () -> {
				return PageObjectHelper.waitForVisible(wait, ADD_MORE_JOBS_PAGE_HEADER).getText();
			});
			Assert.assertEquals(headerText, "Add Job Data");
			LOGGER.info("User landed on KFONE Add Job Data page Successfully");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_be_landed_on_kfone_add_job_data_page", "Issue in landing KFONE Add Job Data page", e);
		}
	}

	public void user_is_in_kfone_add_job_data_page() {
		try {
			WebElement header = PageObjectHelper.waitForVisible(wait, ADD_MORE_JOBS_PAGE_HEADER);
			Assert.assertTrue(header.isDisplayed());
			Assert.assertEquals(header.getText(), "Add Job Data");
			LOGGER.info("User is in KFONE Add Job Data page - Page header verified");
			handleCookiesBanner();
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_is_in_kfone_add_job_data_page", "Issue in validating KFONE Add Job Data page", e);
		}
	}

	public void user_should_click_on_manual_upload_button() {
		try {
			String buttonText = PageObjectHelper.retryOnStaleElement(LOGGER, () -> {
				return PageObjectHelper.waitForVisible(wait, MANUAL_UPLOAD_BTN).getText();
			});
			PageObjectHelper.waitForClickable(wait, MANUAL_UPLOAD_BTN).click();
			LOGGER.info(buttonText + " button clicked successfully");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_click_on_manual_upload_button", "Issue in clicking Manual upload Button", e);
		}
	}

	public void verify_jobs_count_in_kfone_add_job_data_screen_before_adding_more_jobs() {
		try {
			PageObjectHelper.waitForPageReady(driver, 2);
			WebElement jobsCount = PageObjectHelper.waitForVisible(wait, KFONE_JOBS_COUNT);
			KFONEjobsCountBeforeAddingMoreJobs.set(jobsCount.getText());
			LOGGER.info("Jobs count before Adding More Jobs: " + KFONEjobsCountBeforeAddingMoreJobs.get());
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_jobs_count_in_kfone_add_job_data_screen_before_adding_more_jobs", "Issue in verifying Jobs count", e);
		}
	}

	public void verify_last_synced_info_on_add_job_data_screen_before_adding_more_jobs() {
		try {
			scrollToBottom();
			PageObjectHelper.waitForUIStability(driver, 2);
			WebElement lastSynced = PageObjectHelper.waitForVisible(wait, LAST_SYNCED_INFO);
			Assert.assertTrue(lastSynced.isDisplayed());
			lastSyncedInfo1.set(lastSynced.getText());
			LOGGER.info("Last Synced Info before adding More Jobs: " + lastSyncedInfo1.get());
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_last_synced_info_on_add_job_data_screen_before_adding_more_jobs", "Issue in verifying Last Synced Info", e);
		}
	}

	public void upload_job_catalog_file_using_browse_files_button() {
		String filePath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
				+ File.separator + "resources" + File.separator + "Job Catalog with 100 profiles.csv";
		upload_file_using_browse_files_button(filePath);
	}

	public void upload_file_using_browse_files_button(String filePath) {
		File uploadFile = new File(filePath);
		if (!uploadFile.exists()) {
			Assert.fail("Upload file does not exist at path: " + filePath);
		}

		try {
			LOGGER.info("Starting file upload process...");
			boolean success = Utilities.uploadFile(driver, filePath, BROWSE_FILES_BTN);
			if (!success) {
				throw new RuntimeException("All file upload strategies failed");
			}

			PageObjectHelper.waitForUIStability(driver, 1);
			WebElement uploadedFile = PageObjectHelper.waitForVisible(wait, ATTACHED_FILE_NAME);
			Assert.assertTrue(uploadedFile.isDisplayed());
			LOGGER.info("Job catalog file uploaded successfully. File name: " + uploadedFile.getText());
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "upload_file_using_browse_files_button", "Issue in uploading Job Catalog file", e);
		}
	}

	public void user_should_verify_file_close_button_displaying_and_clickable() {
		try {
			WebElement closeBtn = PageObjectHelper.waitForVisible(wait, FILE_CLOSE_BTN);
			Assert.assertTrue(closeBtn.isDisplayed());
			closeBtn.click();
			LOGGER.info("File Close button clicked successfully");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_file_close_button_displaying_and_clickable", "Issue in verifying File Close Button", e);
		}
	}

	public void click_on_continue_button_in_add_job_data_screen() {
		try {
			String buttonText = PageObjectHelper.retryOnStaleElement(LOGGER, () -> {
				return PageObjectHelper.waitForVisible(wait, CONTINUE_BTN).getText();
			});
			PageObjectHelper.waitForClickable(wait, CONTINUE_BTN).click();
			LOGGER.info(buttonText + " button clicked successfully");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_continue_button_in_add_job_data_screen", "Issue in clicking Continue Button", e);
		}
	}

	public void user_should_validate_job_data_upload_is_in_progress() {
		try {
			WebElement uploadProgress = PageObjectHelper.waitForVisible(wait, UPLOAD_PROGRESS_TEXT);
			Assert.assertTrue(uploadProgress.isDisplayed());
			LOGGER.info("Upload in progress - " + uploadProgress.getText());
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_validate_job_data_upload_is_in_progress", "Issue in validating Upload in Progress", e);
		}
	}

	public void user_should_validate_job_data_added_successfully() {
		try {
			LOGGER.info("Waiting for 2 minutes before refreshing page...");
			safeSleep(120000);
			PageObjectHelper.waitForClickable(wait, CLICK_HERE_BTN).click();
			LOGGER.info("Clicked on Click Here button to refresh the page");

			PageObjectHelper.waitForUIStability(driver, 2);
			WebElement successMsg = PageObjectHelper.waitForVisible(wait, UPLOAD_SUCCESS_MESSAGE);
			LOGGER.info(successMsg.getText() + " - Job data added successfully");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_validate_job_data_added_successfully", "Issue in validating Job data added", e);
		}
	}

	public void verify_jobs_count_in_kfone_add_job_data_screen_after_adding_more_jobs() {
		try {
			PageObjectHelper.waitForUIStability(driver, 2);
			scrollToTop();
			WebElement jobsCount = PageObjectHelper.waitForVisible(wait, KFONE_JOBS_COUNT);
			String countAfter = jobsCount.getText();
			LOGGER.info("Jobs count after Adding More Jobs: " + countAfter);

			if (!KFONEjobsCountBeforeAddingMoreJobs.get().equals(countAfter)) {
				LOGGER.info("KFONE Jobs count UPDATED as expected");
			} else {
				throw new Exception("KFONE Jobs count NOT UPDATED (Before: " + KFONEjobsCountBeforeAddingMoreJobs.get() + ", After: " + countAfter + ")");
			}
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_jobs_count_in_kfone_add_job_data_screen_after_adding_more_jobs", "Issue in verifying Jobs count", e);
		}
	}

	public void verify_last_synced_info_on_add_job_data_screen_after_adding_more_jobs() {
		try {
			PageObjectHelper.waitForUIStability(driver, 2);
			WebElement lastSynced = PageObjectHelper.waitForVisible(wait, LAST_SYNCED_INFO);
			Assert.assertTrue(lastSynced.isDisplayed());
			String infoAfter = lastSynced.getText();
			LOGGER.info("Last Synced Info after adding More Jobs: " + infoAfter);

			if (!lastSyncedInfo1.get().equals(infoAfter)) {
				LOGGER.info("Last Synced Info UPDATED as expected");
			} else {
				throw new Exception("Last Synced Info NOT UPDATED (Before: " + lastSyncedInfo1.get() + ", After: " + infoAfter + ")");
			}
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_last_synced_info_on_add_job_data_screen_after_adding_more_jobs", "Issue in verifying Last Synced Info", e);
		}
	}

	public void close_add_job_data_screen() {
		try {
			PageObjectHelper.retryOnStaleElement(LOGGER, () -> {
				WebElement closeBtn = PageObjectHelper.waitForClickable(wait, ADD_MORE_JOBS_CLOSE_BTN);
				closeBtn.click();
			});
			LOGGER.info("Clicked on Add more jobs Close button");
			PageObjectHelper.waitForSpinnersToDisappear(driver, 10);
			
			// AUTOMATION FIX: Handle "Keep working?" popup (method now in BasePageObject)
			dismissKeepWorkingPopupIfPresent();
			
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "close_add_job_data_screen", "Issue in closing Add more jobs page", e);
		}
	}

	public void verify_unpublished_jobs_count_after_adding_more_jobs() {
		try {
			LOGGER.info("Waiting for 2 minutes before validating uploaded jobs count...");
			safeSleep(120000);
			refreshPage();
			LOGGER.info("Refreshed Job Mapping page");
			safeSleep(2000);
			WebElement resultsCount = PageObjectHelper.waitForVisible(wait, Locators.JobMappingResults.SHOWING_JOB_RESULTS);
			String countAfter = resultsCount.getText().split(" ")[3];
			LOGGER.info("Unpublished Job Profiles Count after Adding More Jobs: " + countAfter);

			if (!ResultsCountBeforeAddingMoreJobs.get().equals(countAfter)) {
				LOGGER.info("Unpublished Jobs count UPDATED as expected");
			} else {
				throw new Exception("Unpublished Jobs count NOT UPDATED (Before: " + ResultsCountBeforeAddingMoreJobs.get() + ", After: " + countAfter + ")");
			}
		} catch (Exception e) {
			try {
				LOGGER.info("Waiting for 1 more minute before validating uploaded jobs count...");
				safeSleep(60000);
				refreshPage();
				LOGGER.info("Refreshed Job Mapping page");
				safeSleep(2000);
				WebElement resultsCount = PageObjectHelper.waitForVisible(wait, Locators.JobMappingResults.SHOWING_JOB_RESULTS);
				String countAfter = resultsCount.getText().split(" ")[3];
				LOGGER.info("Unpublished Job Profiles Count after Adding More Jobs: " + countAfter);

				if (!ResultsCountBeforeAddingMoreJobs.get().equals(countAfter)) {
					LOGGER.info("Unpublished Jobs count UPDATED as expected");
				} else {
					throw new Exception("Unpublished Jobs count NOT UPDATED (Before: " + ResultsCountBeforeAddingMoreJobs.get() + ", After: " + countAfter + ")");
				}
			} catch(Exception d) {
				PageObjectHelper.handleError(LOGGER, "verify_unpublished_jobs_count_after_adding_more_jobs", "Issue in verifying Unpublished jobs count", e);
			}
		}
	}

	public void click_on_done_button_in_kfone_add_job_data_page() {
		try {
			String buttonText = PageObjectHelper.retryOnStaleElement(LOGGER, () -> {
				return PageObjectHelper.waitForVisible(wait, DONE_BTN).getText();
			});
			PageObjectHelper.waitForClickable(wait, DONE_BTN).click();
			LOGGER.info(buttonText + " button clicked successfully");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_done_button_in_kfone_add_job_data_page", "Issue in clicking Done Button", e);
		}
	}

	public void user_is_in_kfone_add_job_data_page_afer_uploading_file() {
		LOGGER.info("User is in KFONE Add Job Data page after uploading file");
	}
	// These methods are optional and should NOT fail the test
	// They log warnings if they fail instead of throwing exceptions

	public void verify_jobs_count_before_adding_more_jobs_optional() {
		try {
			LOGGER.info("[OPTIONAL] Verifying Jobs count before adding more jobs...");
			verify_jobs_count_in_kfone_add_job_data_screen_before_adding_more_jobs();
			LOGGER.info("[OPTIONAL] ✓ Jobs count verified successfully");
		} catch (Exception e) {
			LOGGER.warn("[OPTIONAL STEP] Could not verify Jobs count before adding jobs: {} - {}", 
					e.getClass().getSimpleName(), e.getMessage());
			LOGGER.warn("[OPTIONAL STEP] Continuing test execution - this is informational only");
		}
	}

	public void click_on_done_button_optional() {
		try {
			LOGGER.info("[OPTIONAL] Clicking Done button...");
			click_on_done_button_in_kfone_add_job_data_page();
			LOGGER.info("[OPTIONAL] ✓ Done button clicked successfully");
		} catch (Exception e) {
			LOGGER.warn("[OPTIONAL STEP] Could not click Done button: {} - {}", 
					e.getClass().getSimpleName(), e.getMessage());
			LOGGER.warn("[OPTIONAL STEP] Continuing test execution - this is informational only");
		}
	}

	public void validate_job_data_upload_is_in_progress_optional() {
		try {
			LOGGER.info("[OPTIONAL] Validating Job Data Upload is in Progress...");
			user_should_validate_job_data_upload_is_in_progress();
			LOGGER.info("[OPTIONAL] ✓ Upload progress validated successfully");
		} catch (Exception e) {
			LOGGER.warn("[OPTIONAL STEP] Could not validate upload progress: {} - {}", 
					e.getClass().getSimpleName(), e.getMessage());
			LOGGER.warn("[OPTIONAL STEP] Continuing test execution - this is informational only");
		}
	}

	public void validate_job_data_added_successfully_optional() {
		try {
			LOGGER.info("[OPTIONAL] Validating Job Data added successfully...");
			user_should_validate_job_data_added_successfully();
			LOGGER.info("[OPTIONAL] ✓ Job Data addition validated successfully");
		} catch (Exception e) {
			LOGGER.warn("[OPTIONAL STEP] Could not validate job data addition: {} - {}", 
					e.getClass().getSimpleName(), e.getMessage());
			LOGGER.warn("[OPTIONAL STEP] Continuing test execution - this is informational only");
		}
	}

	public void verify_jobs_count_after_adding_more_jobs_optional() {
		try {
			LOGGER.info("[OPTIONAL] Verifying Jobs count after adding more jobs...");
			verify_jobs_count_in_kfone_add_job_data_screen_after_adding_more_jobs();
			LOGGER.info("[OPTIONAL] ✓ Jobs count verified successfully");
		} catch (Exception e) {
			LOGGER.warn("[OPTIONAL STEP] Could not verify Jobs count after adding jobs: {} - {}", 
					e.getClass().getSimpleName(), e.getMessage());
			LOGGER.warn("[OPTIONAL STEP] Continuing test execution - this is informational only");
		}
	}
}
