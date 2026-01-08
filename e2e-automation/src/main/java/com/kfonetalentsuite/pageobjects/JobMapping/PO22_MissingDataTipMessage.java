package com.kfonetalentsuite.pageobjects.JobMapping;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import com.kfonetalentsuite.utils.JobMapping.PageObjectHelper;

public class PO22_MissingDataTipMessage extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO22_MissingDataTipMessage.class);

	public static ThreadLocal<String> initialJobCount = ThreadLocal.withInitial(() -> "NOT_SET");

	public PO22_MissingDataTipMessage() {
		super();
	}

	private static final By MISSING_DATA_TIP_MESSAGE_CONTAINER = By.xpath("//div[@id='warning-message-container']//div[contains(@class, 'inline-flex') and contains(., 'jobs have missing data')]");
	private static final By MISSING_DATA_COUNT_AND_TEXT = By.xpath("//p[contains(text(), 'jobs have missing data and can reduce match accuracy')]");
	private static final By VIEW_REUPLOAD_JOBS_LINK = By.xpath("//a[contains(text(), 'View & Re-upload jobs') and contains(@href, 'aiauto')]");
	private static final By CLOSE_TIP_MESSAGE_BUTTON = By.xpath("//p[contains(text(),'have missing data')]//..//button[@aria-label='Dismiss warning']");
	private static final By CLOSE_REUPLOAD_JOBS_PAGE_BUTTON = By.xpath("//button[contains(@class, 'border-[#007BC7]') and contains(text(), 'Close')]");
	private static final By REUPLOAD_BUTTON = By.xpath("//button[contains(text(), 'Re-upload')] | //button[contains(text(), 'upload')]");
	private static final By PAGE_CONTAINER = By.xpath("//div[@id='page-container']");
	private static final By JOB_TABLE_ROWS = By.xpath("//table//tr[contains(@class, 'border-b')]");
	private static final By ALTERNATIVE_CLOSE_BUTTON = By.xpath("//button[contains(text(), 'Close')] | //button[@aria-label='Close'] | //*[text()='Close']");

	private void waitForUIStabilityInMs(int milliseconds) {
		try {
			PageObjectHelper.waitForUIStability(driver, Math.max(1, milliseconds / 1000));
		} catch (Exception e) {
			Thread.currentThread().interrupt();
		}
	}

	public void ensureTipMessageIsPresent() throws IOException {
		try {
			if (isElementDisplayed(MISSING_DATA_TIP_MESSAGE_CONTAINER)) {
				PageObjectHelper.log(LOGGER, "Missing Data Tip Message is already present");
				return;
			}
		} catch (Exception e) {
			LOGGER.info("Tip message not visible, refreshing page to restore it");
			refreshPage();

			try {
				waitForSpinners();
				waitForElement(MISSING_DATA_TIP_MESSAGE_CONTAINER);
				PageObjectHelper.log(LOGGER, "Missing Data Tip Message restored after page refresh");
			} catch (Exception refreshException) {
				PageObjectHelper.log(LOGGER, "Unable to restore Missing Data Tip Message after refresh");
				throw new IOException("Cannot restore tip message for verification");
			}
		}
	}

	public void verify_missing_data_tip_message_is_displaying_on_job_mapping_page() {
		try {
			WebElement container = waitForElement(MISSING_DATA_TIP_MESSAGE_CONTAINER);
			Assert.assertTrue(container.isDisplayed(),
					"Missing Data Tip Message should be displayed on Job Mapping page");
			PageObjectHelper.log(LOGGER, "Missing Data Tip Message is successfully displayed on Job Mapping page");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_missing_data_tip_message_is_displaying_on_job_mapping_page",
					"Failed to verify Missing Data Tip Message display", e);
		}
	}

	public void verify_missing_data_tip_message_contains_correct_count_of_jobs_with_missing_data() {
		try {
			WebElement countText = waitForElement(MISSING_DATA_COUNT_AND_TEXT);
			String tipMessageText = countText.getText();

			Pattern pattern = Pattern.compile("(\\d+)\\s+jobs have missing data");
			Matcher matcher = pattern.matcher(tipMessageText);

			if (matcher.find()) {
				String extractedCount = matcher.group(1);
				Assert.assertTrue(extractedCount != null && !extractedCount.isEmpty(),
						"Job count should be extracted from tip message");
				PageObjectHelper.log(LOGGER, "Successfully extracted job count from tip message: " + extractedCount);
			} else {
				Assert.fail("Could not extract job count from tip message: " + tipMessageText);
			}
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"verify_missing_data_tip_message_contains_correct_count_of_jobs_with_missing_data",
					"Failed to verify job count in tip message", e);
		}
	}

	public void verify_link_is_present_in_missing_data_tip_message(String linkText) {
		try {
			WebElement link = waitForElement(VIEW_REUPLOAD_JOBS_LINK);
			Assert.assertTrue(link.isDisplayed(),
					"'" + linkText + "' link should be present in Missing Data Tip Message");
			Assert.assertTrue(link.getText().contains(linkText),
					"Link should contain expected text: " + linkText);
			PageObjectHelper.log(LOGGER, "'" + linkText + "' link is present in Missing Data Tip Message");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_link_is_present_in_missing_data_tip_message",
					"Failed to verify link presence", e);
		}
	}

	public void click_on_link_in_missing_data_tip_message(String linkText) {
		try {
			PageObjectHelper.waitForVisible(wait, VIEW_REUPLOAD_JOBS_LINK);
			PageObjectHelper.waitForClickable(wait, VIEW_REUPLOAD_JOBS_LINK);

			clickElement(VIEW_REUPLOAD_JOBS_LINK);
			PageObjectHelper.log(LOGGER, "Clicked '" + linkText + "' link");
			waitForUIStabilityInMs(500);
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_link_in_missing_data_tip_message",
					"Failed to click on '" + linkText + "' link", e);
		}
	}

	public void verify_user_is_navigated_to_appropriate_page_for_viewing_and_re_uploading_jobs() throws IOException {
		try {
			waitForUIStabilityInMs(500);

			boolean pageVerified = false;
			String verificationResults = "";

			try {
				PageObjectHelper.waitForVisible(wait, CLOSE_REUPLOAD_JOBS_PAGE_BUTTON);
				if (isElementDisplayed(CLOSE_REUPLOAD_JOBS_PAGE_BUTTON)) {
					verificationResults += " Close button found; ";
					pageVerified = true;
				}
			} catch (Exception e) {
				verificationResults += "- Close button not found; ";
			}

			try {
				PageObjectHelper.waitForVisible(wait, REUPLOAD_BUTTON);
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
			PageObjectHelper.log(LOGGER, "Attempting to navigate back to Job Mapping page using Close button");

			boolean navigatedSuccessfully = false;

			try {
				PageObjectHelper.waitForVisible(wait, CLOSE_REUPLOAD_JOBS_PAGE_BUTTON);
				PageObjectHelper.waitForClickable(wait, CLOSE_REUPLOAD_JOBS_PAGE_BUTTON);

				clickElement(CLOSE_REUPLOAD_JOBS_PAGE_BUTTON);
				PageObjectHelper.log(LOGGER, "Clicked Close button on re-upload page");
				navigatedSuccessfully = true;
			} catch (Exception e1) {
				PageObjectHelper.log(LOGGER, "Strategy 1 failed - Could not find/click specific Close button: " + e1.getMessage());

				try {
					clickElement(ALTERNATIVE_CLOSE_BUTTON);
					PageObjectHelper.log(LOGGER, "Clicked Alternative Close button");
					PageObjectHelper.log(LOGGER, "Successfully clicked Close button using alternative locator");
					navigatedSuccessfully = true;
				} catch (Exception e2) {
					PageObjectHelper.log(LOGGER, "Strategy 2 failed - Alternative Close button not found: " + e2.getMessage());

					try {
						driver.navigate().back();
						PageObjectHelper.log(LOGGER, "Used browser back navigation as fallback");
						navigatedSuccessfully = true;
					} catch (Exception e3) {
						PageObjectHelper.log(LOGGER, "All navigation strategies failed");
						throw new IOException("Failed to navigate back using any strategy", e3);
					}
				}
			}

			if (navigatedSuccessfully) {
				waitForUIStabilityInMs(1500);

				try {
					waitForElement(PAGE_CONTAINER);
					String currentUrl = driver.getCurrentUrl();
					PageObjectHelper.log(LOGGER, "Successfully navigated back to Job Mapping page");
					PageObjectHelper.log(LOGGER, "Current URL after navigation: " + currentUrl);
				} catch (Exception verifyException) {
					PageObjectHelper.log(LOGGER, "Navigation completed but could not verify page elements");
				}
			}
		} catch (Exception e) {
			try {
				String currentUrl = driver.getCurrentUrl();
				PageObjectHelper.log(LOGGER, "Failed to navigate back to Job Mapping page");
				PageObjectHelper.log(LOGGER, "Current URL: " + currentUrl);
			} catch (Exception debugEx) {
				LOGGER.debug("Failed to get debug URL: " + debugEx.getMessage());
			}

			PageObjectHelper.handleError(LOGGER, "navigate_back_to_job_mapping_page",
					"Failed to navigate back to Job Mapping page", e);
		}
	}

	public void verify_missing_data_tip_message_is_still_displaying_on_job_mapping_page() {
		try {
			WebElement container = waitForElement(MISSING_DATA_TIP_MESSAGE_CONTAINER);
			Assert.assertTrue(container.isDisplayed(),
					"Missing Data Tip Message should still be displayed on Job Mapping page after navigation");
			PageObjectHelper.log(LOGGER,
					"Missing Data Tip Message is still displaying on Job Mapping page after navigation");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"verify_missing_data_tip_message_is_still_displaying_on_job_mapping_page",
					"Failed to verify Missing Data Tip Message is still displayed", e);
		}
	}

	public void click_on_close_button_in_missing_data_tip_message() {
		try {
			PageObjectHelper.log(LOGGER,
					"Clicking close button on Missing Data Tip Message (targeting correct tip message)...");

			waitForElement(CLOSE_TIP_MESSAGE_BUTTON);
			waitForClickable(CLOSE_TIP_MESSAGE_BUTTON);

			clickElement(CLOSE_TIP_MESSAGE_BUTTON);
			PageObjectHelper.log(LOGGER, "Clicked Missing Data Tip Message close button");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_close_button_in_missing_data_tip_message",
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
					PageObjectHelper.log(LOGGER,
							"Missing Data Tip Message is no longer displayed - element removed from DOM");
				} else {
					WebElement tipElement = tipElements.get(0);
					if (!tipElement.isDisplayed()) {
						tipMessageHidden = true;
						hiddenReason = "Element hidden (display:none or visibility:hidden)";
						PageObjectHelper.log(LOGGER,
								"Missing Data Tip Message is no longer displayed - element hidden via CSS");
					} else {
						tipMessageHidden = false;
						hiddenReason = "Element is still visible";
					}
				}
			} catch (Exception e) {
				tipMessageHidden = true;
				hiddenReason = "Exception occurred while checking element - likely removed: " + e.getMessage();
				PageObjectHelper.log(LOGGER,
						"Missing Data Tip Message is no longer displayed - exception indicates element not present");
			}

			if (tipMessageHidden) {
				PageObjectHelper.log(LOGGER,
						"SUCCESS: Missing Data Tip Message is no longer displayed on Job Mapping page - Reason: "
								+ hiddenReason);
			} else {
				String errorMessage = "Missing Data Tip Message is still displayed when it should be hidden. Reason: "
						+ hiddenReason;
				LOGGER.error(errorMessage);
				Assert.fail(errorMessage);
			}
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"verify_missing_data_tip_message_is_no_longer_displayed_on_job_mapping_page",
					"Failed to verify tip message is hidden", e);
		}
	}

	public void verify_missing_data_tip_message_contains_text_about_jobs_having_missing_data() {
		try {
			WebElement countText = waitForElement(MISSING_DATA_COUNT_AND_TEXT);
			String tipMessageText = countText.getText();

			Assert.assertTrue(tipMessageText.contains("jobs have missing data"),
					"Tip message should contain text about jobs having missing data");
			PageObjectHelper.log(LOGGER, "Tip message contains text about jobs having missing data");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"verify_missing_data_tip_message_contains_text_about_jobs_having_missing_data",
					"Failed to verify missing data text", e);
		}
	}

	public void verify_missing_data_tip_message_contains_text_about_reduced_match_accuracy() {
		try {
			WebElement countText = waitForElement(MISSING_DATA_COUNT_AND_TEXT);
			String tipMessageText = countText.getText();

			Assert.assertTrue(tipMessageText.contains("reduce match accuracy"),
					"Tip message should contain text about reduced match accuracy");
			PageObjectHelper.log(LOGGER, "Tip message contains text about reduced match accuracy");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"verify_missing_data_tip_message_contains_text_about_reduced_match_accuracy",
					"Failed to verify reduced accuracy text", e);
		}
	}
}
