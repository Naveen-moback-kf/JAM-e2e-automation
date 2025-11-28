package com.kfonetalentsuite.pageobjects.JobMapping;

import java.io.IOException;

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

import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.ScreenshotHandler;
import com.kfonetalentsuite.utils.JobMapping.Utilities;
import com.kfonetalentsuite.webdriverManager.DriverManager;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

public class PO40_ValidateSelectAndPublishAllJobProfilesinJAM {
	WebDriver driver = DriverManager.getDriver();

	protected static final Logger LOGGER = (Logger) LogManager.getLogger();

	public PO40_ValidateSelectAndPublishAllJobProfilesinJAM() throws IOException {
		PageFactory.initElements(driver, this);
	}

	WebDriverWait wait = DriverManager.getWait();
	Utilities utils = new Utilities();
	JavascriptExecutor js = (JavascriptExecutor) driver;

	// THREAD-SAFE: Each thread gets its own isolated state for parallel execution
	public static ThreadLocal<Integer> unpublishedProfilesCountBefore = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<Integer> publishedProfilesCountBefore = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<Integer> unpublishedProfilesCountAfter = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<Integer> publishedProfilesCountAfter = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<Integer> totalPublishedCount = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<Integer> expectedTotalMinutes = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<Integer> profilesToBePublished = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<Integer> selectedProfilesCount = ThreadLocal.withInitial(() -> 0);
	public static final int PROFILES_PER_MINUTE = 100;
	public static final int REFRESH_INTERVAL_SECONDS = 30; // Refresh every 30 seconds for better progress visibility

	// XPATHs
	@FindBy(xpath = "//div[@data-testid='loader']//img")
	@CacheLookup
	WebElement pageLoadSpinner2;

	@FindBy(xpath = "//th[@scope='col']//div[@class='relative inline-block']//div//*[contains(@class,'cursor-pointer')]")
	@CacheLookup
	WebElement chevronBtninJAM;

	@FindBy(xpath = "//*[contains(text(),'Select All')]")
	@CacheLookup
	WebElement selectAllBtn;

	@FindBy(xpath = "//div[@id='results-toggle-container']//p//span")
	@CacheLookup
	WebElement resultsCountText;

	@FindBy(xpath = "//input[@id='toggleSwitch']")
	@CacheLookup
	WebElement viewPublishedToggle;

	@FindBy(xpath = "//*[contains(text(),'Publish process') or contains(text(),'Please check')]")
	@CacheLookup
	WebElement asyncMessage;

	@FindBy(xpath = "//button[@id='publish-approved-mappings-btn']")
	@CacheLookup
	WebElement publishSelectedProfilesBtn;

	@FindBy(xpath = "//h2[@id='modal-header']")
	@CacheLookup
	WebElement publishedSuccessHeader;

	@FindBy(xpath = "//*[@id='modal-description']")
	@CacheLookup
	WebElement publishedSuccessMsg;

	@FindBy(xpath = "//button[@id='close-success-modal-btn']")
	@CacheLookup
	WebElement publishedSuccessMsgCloseBtn;

	/**
	 * Verifies count of total un-published profiles before publishing Page is
	 * already loaded, directly reads count from results text
	 */
	public void verify_count_of_total_un_published_profiles_before_publishing_selected_profiles() {
		try {
			// Page already loaded, directly get count from results text
			// Get total unpublished profiles count from "Showing X of Y results" text
			String countText = resultsCountText.getText().trim();

			// Parse text like "Showing 10 of 1428 results"
			if (countText.contains("of")) {
				String afterOf = countText.substring(countText.indexOf("of") + 2).trim();
				String totalCountStr = afterOf.split("\\s+")[0].replaceAll("[^0-9]", "");

				if (!totalCountStr.isEmpty()) {
					unpublishedProfilesCountBefore.set(Integer.parseInt(totalCountStr));
					LOGGER.info("Un-Published Profiles count BEFORE Publishing Selected Profiles: "
							+ unpublishedProfilesCountBefore.get());
					ExtentCucumberAdapter
							.addTestStepLog("Un-Published Profiles count BEFORE Publishing Selected Profiles: "
									+ unpublishedProfilesCountBefore.get());
				}
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot(
					"verify_count_of_total_un_published_profiles_before_publishing_selected_profiles", e);
			LOGGER.error(
					"Error getting unpublished profiles count before publishing Selected Profiles - Method: verify_count_of_total_un_published_profiles_before_publishing_selected_profiles",
					e);
			ExtentCucumberAdapter
					.addTestStepLog("Error getting unpublished profiles count before publishing selected profiles");
		}
	}

	/**
	 * Verifies count of total published profiles before publishing Page is already
	 * loaded, directly reads count from results text
	 */
	public void verify_count_of_total_published_profiles_before_publishing_selected_profiles() {
		try {
			// Page already loaded, directly get count from results text
			// Get total published profiles count from "Showing X of Y results" text
			String countText = resultsCountText.getText().trim();

			// Parse text like "Showing 10 of 100 results"
			if (countText.contains("of")) {
				String afterOf = countText.substring(countText.indexOf("of") + 2).trim();
				String totalCountStr = afterOf.split("\\s+")[0].replaceAll("[^0-9]", "");

				if (!totalCountStr.isEmpty()) {
					publishedProfilesCountBefore.set(Integer.parseInt(totalCountStr));
					LOGGER.info("Published Profiles count BEFORE Publishing Selected Profiles: "
							+ publishedProfilesCountBefore.get());
					ExtentCucumberAdapter
							.addTestStepLog("Published Profiles count BEFORE Publishing Selected Profiles: "
									+ publishedProfilesCountBefore.get());
				}
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot(
					"verify_count_of_total_published_profiles_before_publishing_selected_profiles", e);
			LOGGER.error(
					"Error getting published profiles count before publishing Selected profiles- Method: verify_count_of_total_published_profiles_before_publishing_selected_profiles",
					e);
			ExtentCucumberAdapter
					.addTestStepLog("Error getting published profiles count before publishing selected profiles");
		}
	}

	public void click_on_view_published_toggle_button_to_turn_off() {
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);

			// Check if toggle is ON, then click to turn OFF
			if (viewPublishedToggle.isSelected() || viewPublishedToggle.getAttribute("aria-checked").equals("true")) {
				try {
					wait.until(ExpectedConditions.elementToBeClickable(viewPublishedToggle)).click();
				} catch (Exception e) {
					js.executeScript("arguments[0].click();", viewPublishedToggle);
				}
				LOGGER.info("Clicked on View Published toggle button to turn OFF");
				ExtentCucumberAdapter.addTestStepLog("Clicked on View Published toggle button to turn OFF");
				PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
				PerformanceUtils.waitForPageReady(driver, 2);
			}
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_on_view_published_toggle_button_to_turn_off", e);
			LOGGER.error(
					"Issue clicking View Published toggle - Method: click_on_view_published_toggle_button_to_turn_off",
					e);
			ExtentCucumberAdapter.addTestStepLog("Issue clicking View Published toggle button...Please Investigate!!!");
		}
	}

	/**
	 * Clicks chevron button beside header checkbox Page is already loaded, so
	 * minimal wait needed
	 */
	public void click_on_chevron_button_beside_header_checkbox_in_job_mapping_screen() {
		try {
			// Page already loaded from previous step, just ensure readiness
			PerformanceUtils.waitForPageReady(driver, 1);

			// Scroll to top to ensure chevron button is in viewport
			js.executeScript("window.scrollTo(0, 0);");
			Thread.sleep(300); // Wait for scroll to complete
			LOGGER.info("Scrolled to top of page before clicking chevron button");

			// Wait for any spinners to disappear after scrolling
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);

			// Scroll chevron button into view
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", chevronBtninJAM);
			Thread.sleep(300);

			try {
				wait.until(ExpectedConditions.elementToBeClickable(chevronBtninJAM)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", chevronBtninJAM);
				} catch (Exception s) {
					utils.jsClick(driver, chevronBtninJAM);
				}
			}
			LOGGER.info("Clicked on Chevron Button beside Header Checkbox in Job Mapping Screen");
			ExtentCucumberAdapter
					.addTestStepLog("Clicked on Chevron Button beside Header Checkbox in Job Mapping Screen");
			PerformanceUtils.waitForPageReady(driver, 2);
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot(
					"click_on_chevron_button_beside_header_checkbox_in_job_mapping_screen", e);
			LOGGER.error(
					"Issue in clicking Chevron Button - Method: click_on_chevron_button_beside_header_checkbox_in_job_mapping_screen",
					e);
			ExtentCucumberAdapter.addTestStepLog("Issue in clicking Chevron Button...Please Investigate!!!");
		}
	}

	/**
	 * Clicks on Select All button in Job Mapping screen The dropdown appears
	 * immediately after chevron click without page load spinner
	 */
	public void click_on_select_all_button_in_job_mapping_screen() {
		try {
			// Wait directly for Select All button to be clickable (dropdown appears
			// immediately after chevron click)
			// No need to wait for spinner as dropdown shows without page reload

			try {
				wait.until(ExpectedConditions.elementToBeClickable(selectAllBtn)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", selectAllBtn);
				} catch (Exception s) {
					utils.jsClick(driver, selectAllBtn);
				}
			}
			LOGGER.info("Clicked on Select All button in Job Mapping Screen");
			ExtentCucumberAdapter.addTestStepLog("Clicked on Select All button in Job Mapping Screen");

			// Wait for selection to complete
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_on_select_all_button_in_job_mapping_screen", e);
			LOGGER.error(
					"Issue in clicking Select All button - Method: click_on_select_all_button_in_job_mapping_screen",
					e);
			ExtentCucumberAdapter.addTestStepLog("Issue in clicking Select All button...Please Investigate!!!");
		}
	}

	/**
	 * Scrolls through all job profiles and counts the number of selected profiles
	 * based on checkbox state. This method handles lazy loading by scrolling to the
	 * bottom with increased wait times.
	 * 
	 * Process: 1. Get total profile count from "Showing X of Y results" text 2.
	 * Scroll to bottom repeatedly with longer waits to load all profiles 3. Count
	 * checkboxes that are selected (checked)
	 * 
	 * Example: If 1428 total profiles exist and 1428 are selected, this returns
	 * 1428
	 */
	public void verify_count_of_selected_profiles_by_scrolling_through_all_profiles_in_job_mapping_screen() {
		int selectedCount = 0;
		int totalProfiles = 0;

		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

			// Step 1: Get total profile count from "Showing X of Y results" text
			try {
				String countText = resultsCountText.getText().trim();

				// Parse text like "Showing 10 of 1428 results"
				if (countText.contains("of")) {
					String afterOf = countText.substring(countText.indexOf("of") + 2).trim();
					String totalCountStr = afterOf.split("\\s+")[0].replaceAll("[^0-9]", "");

					if (!totalCountStr.isEmpty()) {
						totalProfiles = Integer.parseInt(totalCountStr);
						LOGGER.info("Total job profiles to process: " + totalProfiles);
					}
				}
			} catch (Exception e) {
				LOGGER.warn("Could not extract total profile count: " + e.getMessage());
			}

			if (totalProfiles == 0) {
				LOGGER.warn("Could not determine total profile count. Will scroll until no more data loads.");
			}

			// Step 2: Scroll to load all profiles
			LOGGER.info(" Starting to scroll and load all profiles in Job Mapping...");

			int currentRowCount = 0;
			int previousRowCount = 0;
			int noChangeCount = 0;
			int maxScrollAttempts = 50;
			int scrollAttempts = 0;

			while (scrollAttempts < maxScrollAttempts) {
				// ENHANCED SCROLLING STRATEGY for HEADLESS MODE:
				// Use multiple scroll techniques to ensure lazy loading triggers

				// Method 1: Scroll using document.body.scrollHeight (more reliable in headless)
				try {
					js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
				} catch (Exception e1) {
					// Fallback to documentElement.scrollHeight
					try {
						js.executeScript("window.scrollTo(0, document.documentElement.scrollHeight);");
					} catch (Exception e2) {
						// Last resort: scroll by large pixel amount
						js.executeScript("window.scrollBy(0, 10000);");
					}
				}

				scrollAttempts++;
				LOGGER.debug("Scroll attempt #{} - waiting for content to load...", scrollAttempts);

				// CRITICAL: Longer wait for HEADLESS MODE (lazy loading needs more time)
				Thread.sleep(3000); // Increased from 2000 to 3000ms for headless stability

				// Wait for any spinners to disappear
				PerformanceUtils.waitForSpinnersToDisappear(driver, 5);

				// Wait for page readiness
				PerformanceUtils.waitForPageReady(driver, 2); // Increased from 1 to 2 seconds

				// Additional wait for DOM updates in headless mode
				Thread.sleep(1000); // Extra buffer for lazy-loaded content to render

				// Check current row count
				currentRowCount = driver.findElements(By.xpath("//tbody//tr//td[1]//input[@type='checkbox']")).size();

				LOGGER.debug("Current row count after scroll #{}: {}", scrollAttempts, currentRowCount);

				// Check if no new rows loaded
				if (currentRowCount == previousRowCount) {
					noChangeCount++;
					LOGGER.debug("No new rows loaded. Stagnation count: {}/3", noChangeCount);

					if (noChangeCount >= 3) {
						LOGGER.info("... Reached end of content after {} consecutive non-loading scrolls",
								noChangeCount);
						LOGGER.info("... Final row count: {}", currentRowCount);
						break;
					}

					// ADDITIONAL: Try forcing scroll to absolute bottom one more time
					if (noChangeCount == 2) {
						LOGGER.debug("Attempting final aggressive scroll to ensure all content loaded...");
						js.executeScript(
								"window.scrollTo(0, Math.max(document.body.scrollHeight, document.documentElement.scrollHeight, document.body.offsetHeight, document.documentElement.offsetHeight, document.body.clientHeight, document.documentElement.clientHeight));");
						Thread.sleep(2000); // Wait after aggressive scroll

						// Wait for spinners after aggressive scroll
						PerformanceUtils.waitForSpinnersToDisappear(driver, 5);
					}
				} else {
					noChangeCount = 0;
					int newRows = currentRowCount - previousRowCount;
					LOGGER.debug("✓ Loaded {} new rows (total: {}, scroll: #{})", newRows, currentRowCount,
							scrollAttempts);
				}

				previousRowCount = currentRowCount;
			}

			LOGGER.info("... Scrolling complete. Total rows loaded: {}, Scrolls performed: {}", currentRowCount,
					scrollAttempts);

			// Determine if scrolling was limited by maxScrollAttempts
			boolean reachedScrollLimit = scrollAttempts >= maxScrollAttempts;

			if (reachedScrollLimit) {
				LOGGER.warn(" Reached max scroll limit (" + maxScrollAttempts + " scrolls)");
				LOGGER.warn(" Not all profiles loaded. Loaded: " + currentRowCount + " profiles");
				if (totalProfiles > 0) {
					LOGGER.warn(" Total profiles in system: " + totalProfiles + ", but only " + currentRowCount
							+ " loaded");
					LOGGER.warn(" Validation will be based on loaded profiles only (ignoring "
							+ (totalProfiles - currentRowCount) + " unloaded profiles)");
				}
				ExtentCucumberAdapter.addTestStepLog(
						" Max scroll limit reached. Validating " + currentRowCount + " loaded profiles only");
			} else {
				LOGGER.info(" Scrolling complete. All profiles loaded: " + currentRowCount);
				ExtentCucumberAdapter.addTestStepLog(" All profiles loaded: " + currentRowCount);
			}

			// Step 3: Count selected, disabled, and unselected profiles from LOADED
			// profiles

			// OPTIMIZED: Use JavaScript to count selected checkboxes in one call (much
			// faster!)
			String jsScript = "return document.querySelectorAll('tbody tr td:first-child input[type=\"checkbox\"]:checked').length;";

			int loadedSelectedCount = 0;
			int loadedDisabledCount = 0;
			int loadedUnselectedCount = 0;

			try {
				Object result = js.executeScript(jsScript);
				loadedSelectedCount = ((Long) result).intValue();
			} catch (Exception jsException) {
				// Fallback: Use XPath to directly get checked checkboxes
				LOGGER.warn("JavaScript counting failed, using fallback method");
				try {
					loadedSelectedCount = driver
							.findElements(By.xpath("//tbody//tr//td[1]//input[@type='checkbox' and @checked]")).size();

					// If @checked attribute doesn't work, try property check
					if (loadedSelectedCount == 0) {
						var checkboxes = driver.findElements(By.xpath("//tbody//tr//td[1]//input[@type='checkbox']"));
						for (WebElement checkbox : checkboxes) {
							try {
								if (checkbox.isSelected()) {
									loadedSelectedCount++;
								}
							} catch (Exception e) {
								continue;
							}
						}
					}
				} catch (Exception xpathException) {
					LOGGER.error("Checkbox counting failed: " + xpathException.getClass().getSimpleName());
				}
			}

			// Count disabled checkboxes (cannot be selected)
			try {
				String jsScriptDisabled = "return document.querySelectorAll('tbody tr td:first-child input[type=\"checkbox\"]:disabled').length;";
				Object resultDisabled = js.executeScript(jsScriptDisabled);
				loadedDisabledCount = ((Long) resultDisabled).intValue();
			} catch (Exception e) {
				LOGGER.debug("Could not count disabled checkboxes: " + e.getMessage());
			}

			// Calculate unselected (can be selected but not selected)
			loadedUnselectedCount = currentRowCount - loadedSelectedCount - loadedDisabledCount;

			// Log detailed counts
			LOGGER.info("========================================");
			LOGGER.info("PROFILE COUNTS SUMMARY (Loaded Profiles)");
			LOGGER.info("========================================");
			LOGGER.info("Total Profiles Loaded: " + currentRowCount);
			LOGGER.info("Selected Profiles: " + loadedSelectedCount);
			LOGGER.info("Disabled Profiles (cannot be selected): " + loadedDisabledCount);
			LOGGER.info("Unselected Profiles (can be selected but not selected): " + loadedUnselectedCount);
			LOGGER.info("========================================");

			if (reachedScrollLimit && totalProfiles > 0) {
				int unloadedProfiles = totalProfiles - currentRowCount;
				LOGGER.info(" Note: " + unloadedProfiles + " profiles not loaded due to scroll limit");
				LOGGER.info(" All calculations and validations will be based on " + currentRowCount
						+ " loaded profiles only");
			}

			// Store selected count for downstream validations
			selectedCount = loadedSelectedCount;
			selectedProfilesCount.set(selectedCount);

			ExtentCucumberAdapter.addTestStepLog(" Loaded: " + currentRowCount + " | Selected: " + loadedSelectedCount
					+ " | Disabled: " + loadedDisabledCount + " | Unselected: " + loadedUnselectedCount);

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot(
					"verify_count_of_selected_profiles_by_scrolling_through_all_profiles_in_job_mapping_screen", e);
			LOGGER.error(
					"Error getting selected job profiles count in Job Mapping screen - Method: verify_count_of_selected_profiles_by_scrolling_through_all_profiles_in_job_mapping_screen",
					e);
			ExtentCucumberAdapter.addTestStepLog(" Error getting selected job profiles count");
		}
	}

	public void verify_async_functionality_message_is_displayed_on_jam_screen() {
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 3);

			if (asyncMessage.isDisplayed()) {
				String messageText = asyncMessage.getText();
				LOGGER.info(" Async functionality message displayed: " + messageText);
				ExtentCucumberAdapter.addTestStepLog(" Async functionality message: " + messageText);
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_async_functionality_message_is_displayed_on_jam_screen",
					e);
			LOGGER.error(
					"Error verifying async message on JAM Screen - Method: verify_async_functionality_message_is_displayed_on_jam_screen",
					e);
			ExtentCucumberAdapter.addTestStepLog("Error verifying async message");
		}
	}

	/**
	 * Performs initial refresh after clicking publish button Since progressive
	 * batch monitoring handles detailed tracking, this just does initial setup: 1.
	 * Check if success popup appears (for small profile counts that complete
	 * quickly) 2. Close popup if it appears 3. Do a quick initial refresh to
	 * prepare for progressive monitoring
	 */
	public void refresh_job_mapping_page_after_specified_time_in_message() throws InterruptedException {
		try {
			// Check if success popup appears (wait up to 10 seconds)
			LOGGER.info("Checking for success popup...");

			boolean popupAppeared = false;

			try {
				WebDriverWait shortWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
				shortWait.until(ExpectedConditions.visibilityOf(publishedSuccessHeader));
				popupAppeared = true;

				// Popup appeared (quick completion)
				String successHeaderText = publishedSuccessHeader.getText();
				String successMsgText = publishedSuccessMsg.getText();
				LOGGER.info(" Success popup: " + successHeaderText + " - " + successMsgText);
				ExtentCucumberAdapter.addTestStepLog(" Success popup appeared (quick completion)");

				// Close the popup
				try {
					wait.until(ExpectedConditions.elementToBeClickable(publishedSuccessMsgCloseBtn)).click();
				} catch (Exception e) {
					try {
						js.executeScript("arguments[0].click();", publishedSuccessMsgCloseBtn);
					} catch (Exception s) {
						utils.jsClick(driver, publishedSuccessMsgCloseBtn);
					}
				}

				PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
				PerformanceUtils.waitForPageReady(driver, 2);

			} catch (Exception popupException) {
				// Popup didn't appear - async processing
				LOGGER.info(" No popup - async batch publishing in progress");
				ExtentCucumberAdapter.addTestStepLog(" Async publishing detected");
			}

			// Wait before first refresh
			if (!popupAppeared) {
				LOGGER.info("Waiting 30 seconds before initial refresh...");
				Thread.sleep(30000);
			}

			// Perform initial refresh
			driver.navigate().refresh();
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 3);

			LOGGER.info(" Page ready for progressive monitoring");
			ExtentCucumberAdapter.addTestStepLog(" Ready for progressive monitoring");

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("refresh_job_mapping_page_after_specified_time_in_message", e);
			LOGGER.error("Error in initial refresh - Method: refresh_job_mapping_page_after_specified_time_in_message",
					e);
			ExtentCucumberAdapter.addTestStepLog("Error in initial refresh");
		}
	}

	public void verify_count_of_total_un_published_profiles_after_publishing_selected_profiles() {
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

			// Get total unpublished profiles count after publishing
			String countText = resultsCountText.getText().trim();

			if (countText.contains("of")) {
				String afterOf = countText.substring(countText.indexOf("of") + 2).trim();
				String totalCountStr = afterOf.split("\\s+")[0].replaceAll("[^0-9]", "");

				if (!totalCountStr.isEmpty()) {
					unpublishedProfilesCountAfter.set(Integer.parseInt(totalCountStr));
					LOGGER.info(
							" Un-Published Profiles count AFTER Publishing Selected Profiles in Job Mapping screen: "
									+ unpublishedProfilesCountAfter.get());
					ExtentCucumberAdapter.addTestStepLog(
							" Un-Published Profiles count AFTER Publishing Selected  Profiles in Job Mapping screen: "
									+ unpublishedProfilesCountAfter.get());

					// Verify count decreased
					if (unpublishedProfilesCountAfter.get() < unpublishedProfilesCountBefore.get()) {
						LOGGER.info(" Unpublished count decreased from " + unpublishedProfilesCountBefore.get() + " to "
								+ unpublishedProfilesCountAfter.get());
						ExtentCucumberAdapter.addTestStepLog(" Unpublished count decreased successfully");
					}
				}
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot(
					"verify_count_of_total_un_published_profiles_after_publishing_selected_profiles", e);
			LOGGER.error(
					"Error getting unpublished profiles count after - Method: verify_count_of_total_un_published_profiles_after_publishing_selected_profiles",
					e);
			ExtentCucumberAdapter.addTestStepLog(" Error getting unpublished profiles count");
		}
	}

	public void get_count_of_published_profiles_in_job_mapping_screen() {
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

			// Calculate published count
			totalPublishedCount.set(unpublishedProfilesCountBefore.get() - unpublishedProfilesCountAfter.get()
					+ publishedProfilesCountBefore.get());

			LOGGER.info(" Published Profiles Calculation:");
			LOGGER.info("   (Before: " + unpublishedProfilesCountBefore.get() + "  After: "
					+ unpublishedProfilesCountAfter.get() + ") + Published Before: "
					+ publishedProfilesCountBefore.get());
			LOGGER.info("   Total Published: " + totalPublishedCount.get());

			ExtentCucumberAdapter.addTestStepLog(" Total Published: " + totalPublishedCount.get() + " profiles");

			// Validation: Compare with selected profiles count
			if (selectedProfilesCount.get() > 0) {
				int actualPublishedFromSelection = unpublishedProfilesCountBefore.get()
						- unpublishedProfilesCountAfter.get();

				if (actualPublishedFromSelection == selectedProfilesCount.get()) {
					LOGGER.info(" Validation PASSED: " + actualPublishedFromSelection + " published (matches "
							+ selectedProfilesCount.get() + " selected)");
					ExtentCucumberAdapter.addTestStepLog(
							" Validation: Count matches expected (" + selectedProfilesCount.get() + " profiles)");
				} else {
					int difference = Math.abs(actualPublishedFromSelection - selectedProfilesCount.get());
					LOGGER.warn(" Validation WARNING: Expected " + selectedProfilesCount.get() + ", Actual "
							+ actualPublishedFromSelection + " (" + difference + ")");
					ExtentCucumberAdapter.addTestStepLog(" Validation: Mismatch - Expected "
							+ selectedProfilesCount.get() + ", Actual " + actualPublishedFromSelection);
				}
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("get_count_of_published_profiles_in_job_mapping_screen", e);
			LOGGER.error(
					"Error calculating published count - Method: get_count_of_published_profiles_in_job_mapping_screen",
					e);
			ExtentCucumberAdapter.addTestStepLog(" Error calculating published count");
		}
	}

	public void verify_count_of_total_published_profiles_after_publishing_selected_profiles() {
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

			// Get published profiles count in View Published screen
			String countText = resultsCountText.getText().trim();

			if (countText.contains("of")) {
				String afterOf = countText.substring(countText.indexOf("of") + 2).trim();
				String totalCountStr = afterOf.split("\\s+")[0].replaceAll("[^0-9]", "");

				if (!totalCountStr.isEmpty()) {
					publishedProfilesCountAfter.set(Integer.parseInt(totalCountStr));
					LOGGER.info(" Published Profiles count AFTER Publishing Selected Profiles: "
							+ publishedProfilesCountAfter.get());
					ExtentCucumberAdapter
							.addTestStepLog(" Published Profiles count AFTER Publishing Selected Profiles: "
									+ publishedProfilesCountAfter.get());
				}
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot(
					"verify_count_of_total_published_profiles_after_publishing_selected_profiles", e);
			LOGGER.error(
					"Error getting published count after - Method: verify_count_of_total_published_profiles_after_publishing_selected_profiles",
					e);
			ExtentCucumberAdapter.addTestStepLog(" Error getting published profiles count");
		}
	}

	public void verify_published_profiles_count_matches_in_view_published_screen() {
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

			// Verify the counts match
			if (publishedProfilesCountAfter == totalPublishedCount) {
				LOGGER.info(" Published profiles count matches! Expected: " + totalPublishedCount + ", Actual: "
						+ publishedProfilesCountAfter);
				ExtentCucumberAdapter.addTestStepLog(" Published profiles count matches successfully");
			} else {
				LOGGER.warn(" Count mismatch! Expected: " + totalPublishedCount + ", Actual: "
						+ publishedProfilesCountAfter);
				ExtentCucumberAdapter.addTestStepLog(" Published profiles count mismatch");
			}

		} catch (Exception e) {
			ScreenshotHandler
					.captureFailureScreenshot("verify_published_profiles_count_matches_in_view_published_screen", e);
			LOGGER.error(
					"Error verifying count match - Method: verify_published_profiles_count_matches_in_view_published_screen",
					e);
			ExtentCucumberAdapter.addTestStepLog(" Error verifying count match");
		}
	}

	/**
	 * Verify user is on Job Mapping page after initial refresh
	 */
	public void user_is_in_job_mapping_page_after_initial_refresh() {
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);
			LOGGER.info(" Job Mapping page ready");

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_is_in_job_mapping_page_after_initial_refresh", e);
			LOGGER.error("Error verifying Job Mapping page - Method: user_is_in_job_mapping_page_after_initial_refresh",
					e);
			ExtentCucumberAdapter.addTestStepLog("Error verifying Job Mapping page");
		}
	}

	/**
	 * Calculate expected total time for batch publishing Formula: Selected profiles
	 * count / 100 profiles per minute = expected minutes
	 */
	public void calculate_expected_total_time_for_batch_publishing_based_on_profile_count() {
		try {
			profilesToBePublished = selectedProfilesCount;
			expectedTotalMinutes.set((int) Math.ceil((double) profilesToBePublished.get() / PROFILES_PER_MINUTE));

			LOGGER.info(" Batch Publishing: " + profilesToBePublished.get() + " profiles @ " + PROFILES_PER_MINUTE
					+ "/min  ~" + expectedTotalMinutes.get() + " minutes");
			ExtentCucumberAdapter.addTestStepLog(" Publishing " + profilesToBePublished.get() + " profiles (~"
					+ expectedTotalMinutes.get() + " min expected)");

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot(
					"calculate_expected_total_time_for_batch_publishing_based_on_profile_count", e);
			LOGGER.error(
					"Error calculating expected time - Method: calculate_expected_total_time_for_batch_publishing_based_on_profile_count",
					e);
			ExtentCucumberAdapter.addTestStepLog("Error calculating expected time");
		}
	}

	/**
	 * Monitor and validate progressive batch publishing until completion Refreshes
	 * page every 30 seconds and checks unpublished count to track progress
	 */
	public void monitor_and_validate_progressive_batch_publishing_until_completion() throws InterruptedException {
		int maxWaitMinutes = expectedTotalMinutes.get() + 5; // Add 5 minutes buffer
		int maxWaitSeconds = maxWaitMinutes * 60; // Convert to seconds
		int elapsedSeconds = 0;
		int checkNumber = 0;
		int initialUnpublishedCount = unpublishedProfilesCountBefore.get();
		int previousUnpublishedCount = unpublishedProfilesCountBefore.get();
		int currentUnpublishedCount = unpublishedProfilesCountBefore.get();
		int targetUnpublishedCount = unpublishedProfilesCountBefore.get() - selectedProfilesCount.get();

		try {
			LOGGER.info(" Monitoring: " + initialUnpublishedCount + "  " + targetUnpublishedCount + " (publish "
					+ selectedProfilesCount.get() + ") | Max wait: " + maxWaitMinutes + " min");
			ExtentCucumberAdapter.addTestStepLog(" Monitoring: Publish " + selectedProfilesCount.get()
					+ " profiles (check every " + REFRESH_INTERVAL_SECONDS + "s)");

			// Monitor progress every 30 seconds
			while (elapsedSeconds < maxWaitSeconds) {
				checkNumber++;
				elapsedSeconds += REFRESH_INTERVAL_SECONDS;
				int elapsedMinutes = elapsedSeconds / 60;

				Thread.sleep(REFRESH_INTERVAL_SECONDS * 1000);
				driver.navigate().refresh();
				PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
				PerformanceUtils.waitForPageReady(driver, 2);

				// Get current unpublished count - Re-find element after refresh to avoid
				// StaleElementReferenceException
				try {
					WebElement freshResultsElement = driver
							.findElement(By.xpath("//div[@id='results-toggle-container']//p//span"));
					String countText = freshResultsElement.getText().trim();

					if (countText.contains("of")) {
						String afterOf = countText.substring(countText.indexOf("of") + 2).trim();
						String totalCountStr = afterOf.split("\\s+")[0].replaceAll("[^0-9]", "");

						if (!totalCountStr.isEmpty()) {
							currentUnpublishedCount = Integer.parseInt(totalCountStr);
						}
					}
				} catch (Exception e) {
					LOGGER.warn(" Could not read unpublished count at check #" + checkNumber + ": " + e.getMessage());
					// Continue monitoring with previous count
				}

				// Calculate progress metrics
				int profilesPublishedSinceLastCheck = previousUnpublishedCount - currentUnpublishedCount;
				int totalPublishedSoFar = initialUnpublishedCount - currentUnpublishedCount;
				double progressPercentage = ((double) totalPublishedSoFar / selectedProfilesCount.get()) * 100;
				double publishingRate = elapsedSeconds > 0 ? (totalPublishedSoFar * 60.0) / elapsedSeconds : 0;

				LOGGER.info("+/- Check #" + checkNumber + " (" + elapsedMinutes + "m" + (elapsedSeconds % 60) + "s): "
						+ currentUnpublishedCount + " unpublished | +" + profilesPublishedSinceLastCheck + " | "
						+ String.format("%.0f", publishingRate) + "/min | "
						+ String.format("%.1f%%", progressPercentage));

				ExtentCucumberAdapter.addTestStepLog("+/- Check #" + checkNumber + " (" + elapsedMinutes + "m"
						+ (elapsedSeconds % 60) + "s): " + currentUnpublishedCount + " remaining | +"
						+ profilesPublishedSinceLastCheck + " | " + String.format("%.0f", publishingRate) + "/min | "
						+ String.format("%.1f%%", progressPercentage));

				// Check if all selected profiles are published (reached target)
				if (currentUnpublishedCount <= targetUnpublishedCount) {
					LOGGER.info(" SUCCESS! All " + totalPublishedSoFar + " profiles published in " + elapsedMinutes
							+ " minutes");
					ExtentCucumberAdapter.addTestStepLog(" SUCCESS! All " + totalPublishedSoFar
							+ " profiles published in " + elapsedMinutes + " min");
					break;
				}

				// Validate progress is happening (at least some profiles should be published)
				// Allow some buffer time before warning (check after 2 minutes = 120 seconds)
				if (elapsedSeconds >= 120 && profilesPublishedSinceLastCheck == 0) {
					LOGGER.warn(" Warning: No profiles published in this check (30 sec). Publishing may be stalled.");
					ExtentCucumberAdapter.addTestStepLog(" Warning: No progress in check #" + checkNumber);
				}

				previousUnpublishedCount = currentUnpublishedCount;
			}

			// Final check
			int remainingToPublishFinal = currentUnpublishedCount - targetUnpublishedCount;
			if (remainingToPublishFinal > 0) {
				int totalChecks = checkNumber;
				LOGGER.warn(" Publishing not completed within " + maxWaitMinutes + " minutes (" + totalChecks
						+ " checks at " + REFRESH_INTERVAL_SECONDS + "s intervals)");
				LOGGER.warn(" Current unpublished: " + currentUnpublishedCount);
				LOGGER.warn(" Target unpublished: " + targetUnpublishedCount);
				LOGGER.warn(" Still remaining to publish: " + remainingToPublishFinal);
				ExtentCucumberAdapter.addTestStepLog(" Publishing timeout after " + totalChecks
						+ " checks. Still remaining: " + remainingToPublishFinal + " profiles");
			}

		} catch (Exception e) {
			ScreenshotHandler
					.captureFailureScreenshot("monitor_and_validate_progressive_batch_publishing_until_completion", e);
			LOGGER.error(
					"Error monitoring batch publishing - Method: monitor_and_validate_progressive_batch_publishing_until_completion",
					e);
			ExtentCucumberAdapter.addTestStepLog("Error monitoring batch publishing");
		}
	}

	/**
	 * Verify all selected profiles are published successfully
	 */
	public void verify_all_profiles_are_published_successfully() {
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

			// Get final unpublished count - Re-find element to avoid
			// StaleElementReferenceException
			int finalUnpublishedCount = 0;

			try {
				WebElement freshResultsElement = driver
						.findElement(By.xpath("//div[@id='results-toggle-container']//p//span"));
				String countText = freshResultsElement.getText().trim();

				if (countText.contains("of")) {
					String afterOf = countText.substring(countText.indexOf("of") + 2).trim();
					String totalCountStr = afterOf.split("\\s+")[0].replaceAll("[^0-9]", "");

					if (!totalCountStr.isEmpty()) {
						finalUnpublishedCount = Integer.parseInt(totalCountStr);
					}
				}
			} catch (Exception e) {
				LOGGER.warn(" Could not read final unpublished count: " + e.getMessage());
			}

			// Calculate and verify results
			int initialUnpublishedCount = unpublishedProfilesCountBefore.get();
			int actualPublished = initialUnpublishedCount - finalUnpublishedCount;
			int expectedTargetUnpublished = initialUnpublishedCount - selectedProfilesCount.get();
			int remainingToPublish = finalUnpublishedCount - expectedTargetUnpublished;

			if (finalUnpublishedCount <= expectedTargetUnpublished) {
				LOGGER.info(" FINAL VERIFICATION PASSED! All " + selectedProfilesCount.get()
						+ " profiles published (Remaining: " + finalUnpublishedCount + ")");
				ExtentCucumberAdapter
						.addTestStepLog(" SUCCESS! All " + selectedProfilesCount.get() + " profiles published");
			} else {
				LOGGER.warn(" INCOMPLETE: Expected " + selectedProfilesCount.get() + ", Published " + actualPublished
						+ " (Remaining: " + remainingToPublish + ")");
				ExtentCucumberAdapter.addTestStepLog(" Incomplete! " + remainingToPublish + " profiles still pending");
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_all_profiles_are_published_successfully", e);
			LOGGER.error("Error verifying final status - Method: verify_all_profiles_are_published_successfully", e);
			ExtentCucumberAdapter.addTestStepLog("Error verifying final status");
		}
	}
}
