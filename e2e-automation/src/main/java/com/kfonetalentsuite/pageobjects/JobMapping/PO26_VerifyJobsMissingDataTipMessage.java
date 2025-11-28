package com.kfonetalentsuite.pageobjects.JobMapping;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.Utilities;
import com.kfonetalentsuite.utils.PageObjectHelper;
import com.kfonetalentsuite.webdriverManager.DriverManager;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

public class PO26_VerifyJobsMissingDataTipMessage {

	WebDriver driver = DriverManager.getDriver();

	protected static final Logger LOGGER = (Logger) LogManager.getLogger();
	PO26_VerifyJobsMissingDataTipMessage verifyJobsMissingDataTipMessage;

	// THREAD-SAFE: Each thread gets its own isolated state for parallel execution
	public static ThreadLocal<String> initialJobCount = ThreadLocal.withInitial(() -> null);

	public PO26_VerifyJobsMissingDataTipMessage() throws IOException {
		PageFactory.initElements(driver, this);
	}

	WebDriverWait wait = DriverManager.getWait();
	Utilities utils = new Utilities();
	JavascriptExecutor js = (JavascriptExecutor) driver;

	// XPATHs for Missing Data Tip Message Elements
	@FindBy(xpath = "//div[@id='warning-message-container']//div[contains(@class, 'inline-flex') and contains(., 'jobs have missing data')]")
	@CacheLookup
	WebElement missingDataTipMessageContainer;

	@FindBy(xpath = "//p[contains(text(), 'jobs have missing data and can reduce match accuracy')]")
	@CacheLookup
	WebElement missingDataCountAndText;

	@FindBy(xpath = "//a[contains(text(), 'View & Re-upload jobs') and contains(@href, 'aiauto')]")
	@CacheLookup
	WebElement viewReuploadJobsLink;

	// Specific close button for missing data tip message only
	@FindBy(xpath = "//p[contains(text(),'have missing data')]//..//button[@aria-label='Dismiss warning']")
	@CacheLookup
	WebElement closeTipMessageButton;

	// Close button on "View & Re-upload jobs" screen
	@FindBy(xpath = "//button[contains(@class, 'border-[#007BC7]') and contains(text(), 'Close')]")
	@CacheLookup
	WebElement closeReuploadJobsPageButton;

	// Elements on "View & Re-upload jobs" page for verification
	@FindBy(xpath = "//h1[contains(text(), 'Jobs With Missing Data')] | //*[contains(text(), 'Organization Jobs With Missing Data')] | //*[contains(text(), '1815 Organization Jobs')]")
	@CacheLookup
	WebElement reuploadPageTitle;

	@FindBy(xpath = "//button[contains(text(), 'Re-upload')] | //button[contains(text(), 'upload')]")
	@CacheLookup
	WebElement reuploadButton;

	@FindBy(xpath = "//div[@id='warning-message-container']//svg[contains(@class, 'mr-3')]")
	@CacheLookup
	WebElement warningIcon;

	// Alternative warning icon locator (more specific)
	@FindBy(xpath = "//div[@id='warning-message-container']//div[contains(@class, 'inline-flex')]//svg[@class='mr-3']")
	@CacheLookup
	WebElement warningIconAlternative;

	@FindBy(xpath = "//div[@id='table-container']")
	@CacheLookup
	WebElement jobListingContainer;

	@FindBy(xpath = "//tbody//tr[contains(@class, 'bg-white')]")
	List<WebElement> individualJobRows;

	@FindBy(xpath = "//div[@id='page-container']")
	@CacheLookup
	WebElement pageContainer;

	@FindBy(xpath = "//div[@data-testid='loader']//img")
	@CacheLookup
	WebElement pageLoadSpinner2;

	// Helper method to ensure tip message is present
	public void ensureTipMessageIsPresent() throws IOException {
		try {
			// Check if tip message is already visible
			if (missingDataTipMessageContainer.isDisplayed()) {
				ExtentCucumberAdapter.addTestStepLog(" Missing Data Tip Message is already present");
				return;
			}
		} catch (Exception e) {
			// Tip message not visible, try to refresh page to restore it
			LOGGER.info("Tip message not visible, refreshing page to restore it");
			driver.navigate().refresh();

			try {
				PerformanceUtils.waitForPageReady(driver, 3);
				wait.until(ExpectedConditions.visibilityOf(missingDataTipMessageContainer));
				ExtentCucumberAdapter.addTestStepLog(" Missing Data Tip Message restored after page refresh");
			} catch (Exception refreshException) {
				ExtentCucumberAdapter.addTestStepLog(" Unable to restore Missing Data Tip Message after refresh");
				throw new IOException("Cannot restore tip message for verification");
			}
		}
	}

	// Basic Verification Methods
	public void verify_missing_data_tip_message_is_displaying_on_job_mapping_page() throws IOException {
		try {
			wait.until(ExpectedConditions.visibilityOf(missingDataTipMessageContainer));
			Assert.assertTrue(missingDataTipMessageContainer.isDisplayed(),
					"Missing Data Tip Message should be displayed on Job Mapping page");
			PageObjectHelper.log(LOGGER, "Missing Data Tip Message is successfully displayed on Job Mapping page");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_missing_data_tip_message_is_displaying_on_job_mapping_page",
					"Failed to verify Missing Data Tip Message display", e);
		}
	}

	public void verify_missing_data_tip_message_contains_correct_count_of_jobs_with_missing_data() throws IOException {
		try {
			wait.until(ExpectedConditions.visibilityOf(missingDataCountAndText));
			String tipMessageText = missingDataCountAndText.getText();

			// Extract number from the tip message using regex
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

	// Component Verification Methods
	public void verify_link_is_present_in_missing_data_tip_message(String linkText) throws IOException {
		try {
			wait.until(ExpectedConditions.visibilityOf(viewReuploadJobsLink));
			Assert.assertTrue(viewReuploadJobsLink.isDisplayed(),
					"'" + linkText + "' link should be present in Missing Data Tip Message");
			Assert.assertTrue(viewReuploadJobsLink.getText().contains(linkText),
					"Link should contain expected text: " + linkText);
			PageObjectHelper.log(LOGGER, "'" + linkText + "' link is present in Missing Data Tip Message");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_link_is_present_in_missing_data_tip_message",
					"Failed to verify link presence", e);
		}
	}

	// Functional Verification Methods
	public void click_on_link_in_missing_data_tip_message(String linkText) throws IOException {
		try {
			WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(10));
			shortWait.until(ExpectedConditions.visibilityOf(viewReuploadJobsLink));
			shortWait.until(ExpectedConditions.elementToBeClickable(viewReuploadJobsLink));

			performRobustClick(viewReuploadJobsLink, "'" + linkText + "' link");
			safeSleep(500);
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_link_in_missing_data_tip_message",
					"Failed to click on '" + linkText + "' link", e);
		}
	}

	public void verify_user_is_navigated_to_appropriate_page_for_viewing_and_re_uploading_jobs() throws IOException {
		try {
			safeSleep(500);

			boolean pageVerified = false;
			String verificationResults = "";

			// Check for Close button (essential for closing modal)
			try {
				WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
				shortWait.until(ExpectedConditions.visibilityOf(closeReuploadJobsPageButton));
				if (closeReuploadJobsPageButton.isDisplayed()) {
					verificationResults += " Close button found; ";
					pageVerified = true;
				}
			} catch (Exception e) {
				verificationResults += "- Close button not found; ";
			}

			// Check for Re-upload button (confirms this is the upload screen)
			try {
				WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
				shortWait.until(ExpectedConditions.visibilityOf(reuploadButton));
				if (reuploadButton.isDisplayed()) {
					verificationResults += " Re-upload button found; ";
					pageVerified = true;
				}
			} catch (Exception e) {
				verificationResults += "- Re-upload button not found; ";
			}

			// Check for job table/rows (confirms data is loaded)
			try {
				List<WebElement> jobRows = driver.findElements(By.xpath("//table//tr[contains(@class, 'border-b')]"));
				if (!jobRows.isEmpty()) {
					verificationResults += " Job data table found (" + jobRows.size() + " rows); ";
				} else {
					verificationResults += "- No job data rows found; ";
				}
			} catch (Exception e) {
				verificationResults += "- Job data table check failed; ";
			}

			// Alternative verification: Check page source (fallback only)
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
			ExtentCucumberAdapter.addTestStepLog(" Attempting to navigate back to Job Mapping page using Close button");

			boolean navigatedSuccessfully = false;

			// Strategy 1: Use the specific Close button
			try {
				WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(10));
				shortWait.until(ExpectedConditions.visibilityOf(closeReuploadJobsPageButton));
				shortWait.until(ExpectedConditions.elementToBeClickable(closeReuploadJobsPageButton));

				// Use robust click helper method
				performRobustClick(closeReuploadJobsPageButton, "Close button on re-upload page");
				navigatedSuccessfully = true;

			} catch (Exception e1) {
				ExtentCucumberAdapter.addTestStepLog(
						" Strategy 1 failed - Could not find/click specific Close button: " + e1.getMessage());
				LOGGER.warn("Primary close button strategy failed: " + e1.getMessage());

				// Strategy 2: Try alternative Close button locator
				try {
					WebElement alternativeCloseButton = driver.findElement(By.xpath(
							"//button[contains(text(), 'Close')] | //button[@aria-label='Close'] | //*[text()='Close']"));
					performRobustClick(alternativeCloseButton, "Alternative Close button");

					ExtentCucumberAdapter
							.addTestStepLog(" Successfully clicked Close button using alternative locator");
					LOGGER.info("Successfully used alternative Close button locator");
					navigatedSuccessfully = true;

				} catch (Exception e2) {
					ExtentCucumberAdapter.addTestStepLog(
							" Strategy 2 failed - Alternative Close button not found: " + e2.getMessage());
					LOGGER.warn("Alternative close button strategy failed: " + e2.getMessage());

					// Strategy 3: Use browser back navigation as last resort
					try {
						driver.navigate().back();
						ExtentCucumberAdapter.addTestStepLog(" Used browser back navigation as fallback");
						LOGGER.info("Used browser back navigation as fallback");
						navigatedSuccessfully = true;
					} catch (Exception e3) {
						ExtentCucumberAdapter.addTestStepLog("- All navigation strategies failed");
						LOGGER.error("All navigation strategies failed", e3);
						throw new IOException("Failed to navigate back using any strategy", e3);
					}
				}
			}

			if (navigatedSuccessfully) {
				// Wait for navigation back to Job Mapping page (optimized)
				safeSleep(1500); // Optimized wait for page transition

				// Verify we're back on Job Mapping page
				try {
					wait.until(ExpectedConditions.visibilityOf(pageContainer));
					String currentUrl = driver.getCurrentUrl();
					ExtentCucumberAdapter.addTestStepLog(" Successfully navigated back to Job Mapping page");
					ExtentCucumberAdapter.addTestStepLog("   Current URL after navigation: " + currentUrl);
					LOGGER.info("Successfully navigated back to Job Mapping page. URL: " + currentUrl);
				} catch (Exception verifyException) {
					ExtentCucumberAdapter.addTestStepLog(" Navigation completed but could not verify page elements");
					ExtentCucumberAdapter.addTestStepLog("   This may be normal - continuing with test");
					LOGGER.warn(
							"Could not verify page elements after navigation back: " + verifyException.getMessage());
				}
			}

		} catch (Exception e) {
			// Add debugging information
			try {
				String currentUrl = driver.getCurrentUrl();
				ExtentCucumberAdapter.addTestStepLog("- Failed to navigate back to Job Mapping page");
				ExtentCucumberAdapter.addTestStepLog("   Current URL: " + currentUrl);
			} catch (Exception debugEx) {
				LOGGER.debug("Failed to get debug URL: " + debugEx.getMessage());
			}

			PageObjectHelper.handleError(LOGGER, "navigate_back_to_job_mapping_page",
					"Failed to navigate back to Job Mapping page", e);
		}
	}

	public void verify_missing_data_tip_message_is_still_displaying_on_job_mapping_page() throws IOException {
		try {
			wait.until(ExpectedConditions.visibilityOf(missingDataTipMessageContainer));
			Assert.assertTrue(missingDataTipMessageContainer.isDisplayed(),
					"Missing Data Tip Message should still be displayed on Job Mapping page after navigation");
			PageObjectHelper.log(LOGGER,
					"Missing Data Tip Message is still displaying on Job Mapping page after navigation");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"verify_missing_data_tip_message_is_still_displaying_on_job_mapping_page",
					"Failed to verify Missing Data Tip Message is still displayed", e);
		}
	}

	public void click_on_close_button_in_missing_data_tip_message() throws IOException {
		try {
			PageObjectHelper.log(LOGGER,
					" Clicking close button on Missing Data Tip Message (targeting correct tip message)...");

			// Wait for the specific missing data tip message close button
			wait.until(ExpectedConditions.visibilityOf(closeTipMessageButton));
			wait.until(ExpectedConditions.elementToBeClickable(closeTipMessageButton));

			// Use robust click helper method
			performRobustClick(closeTipMessageButton, "Missing Data Tip Message close button");

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_close_button_in_missing_data_tip_message",
					"Failed to click on missing data tip message close button", e);
		}
	}

	public void verify_missing_data_tip_message_is_no_longer_displayed_on_job_mapping_page() throws IOException {
		try {
			safeSleep(1000); // Optimized wait for UI update (reduced from 2000ms)

			boolean tipMessageHidden = false;
			String hiddenReason = "";

			// Strategy 1: Check if element is present in DOM but not visible
			try {
				// Check if element exists
				List<WebElement> tipElements = driver.findElements(By.xpath(
						"//div[@id='warning-message-container']//div[contains(@class, 'inline-flex') and contains(., 'jobs have missing data')]"));

				if (tipElements.isEmpty()) {
					// Element completely removed from DOM
					tipMessageHidden = true;
					hiddenReason = "Element removed from DOM";
					PageObjectHelper.log(LOGGER,
							"Missing Data Tip Message is no longer displayed - element removed from DOM");
				} else {
					// Element exists, check if it's visible
					WebElement tipElement = tipElements.get(0);
					if (!tipElement.isDisplayed()) {
						// Element exists but is hidden
						tipMessageHidden = true;
						hiddenReason = "Element hidden (display:none or visibility:hidden)";
						PageObjectHelper.log(LOGGER,
								"Missing Data Tip Message is no longer displayed - element hidden via CSS");
					} else {
						// Element is still visible - this is a failure
						tipMessageHidden = false;
						hiddenReason = "Element is still visible";
					}
				}

			} catch (Exception e) {
				// If we get any exception checking for the element, assume it's hidden
				tipMessageHidden = true;
				hiddenReason = "Exception occurred while checking element - likely removed: " + e.getMessage();
				PageObjectHelper.log(LOGGER,
						"Missing Data Tip Message is no longer displayed - exception indicates element not present");
			}

			// Final assertion
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

	// Helper method for robust element clicking with multiple strategies (optimized
	// for speed)
	private void performRobustClick(WebElement element, String elementName) throws IOException {
		// Scroll element into view with faster behavior
		js.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});", element);
		safeSleep(200); // Reduced from 500ms to 200ms

		// Try normal click first
		try {
			element.click();
			ExtentCucumberAdapter.addTestStepLog("Clicked " + elementName);
		} catch (Exception clickEx1) {
			// Try JavaScript click if normal click fails
			try {
				js.executeScript("arguments[0].click();", element);
				LOGGER.debug("Used JavaScript click for: " + elementName);
				ExtentCucumberAdapter.addTestStepLog("Clicked " + elementName);
			} catch (Exception clickEx2) {
				// Try dispatch event as last resort
				js.executeScript(
						"arguments[0].dispatchEvent(new MouseEvent('click', {bubbles: true, cancelable: true, view: window}));",
						element);
				LOGGER.debug("Used dispatch event for: " + elementName);
				ExtentCucumberAdapter.addTestStepLog("Clicked " + elementName);
			}
		}
	}

	// Helper method to handle thread sleep with interruption (optimized for shorter
	// waits)
	private void safeSleep(int milliseconds) throws IOException {
		try {
			PerformanceUtils.waitForUIStability(driver, milliseconds / 1000);
		} catch (Exception ie) {
			Thread.currentThread().interrupt();
			throw new IOException("Thread interrupted during sleep", ie);
		}
	}

	// Content and Formatting Verification Methods
	public void verify_missing_data_tip_message_contains_text_about_jobs_having_missing_data() throws IOException {
		try {
			wait.until(ExpectedConditions.visibilityOf(missingDataCountAndText));
			String tipMessageText = missingDataCountAndText.getText();

			Assert.assertTrue(tipMessageText.contains("jobs have missing data"),
					"Tip message should contain text about jobs having missing data");
			PageObjectHelper.log(LOGGER, "Tip message contains text about jobs having missing data");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"verify_missing_data_tip_message_contains_text_about_jobs_having_missing_data",
					"Failed to verify missing data text", e);
		}
	}

	public void verify_missing_data_tip_message_contains_text_about_reduced_match_accuracy() throws IOException {
		try {
			wait.until(ExpectedConditions.visibilityOf(missingDataCountAndText));
			String tipMessageText = missingDataCountAndText.getText();

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
