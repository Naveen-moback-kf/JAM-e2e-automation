package com.kfonetalentsuite.pageobjects.JobMapping;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.ScreenshotHandler;
import com.kfonetalentsuite.utils.JobMapping.PageObjectHelper;

/**
 * Page Object for validating Select All and Publish functionality in Job Mapping (JAM) screen.
 * Handles selecting all profiles, publishing them, and monitoring batch publishing progress.
 * 
 * Enhanced to extend BasePageObject for consistency and code reuse.
 */
public class PO30_SelectAndPublishAllJobProfiles_JAM extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO30_SelectAndPublishAllJobProfiles_JAM.class);

	public PO30_SelectAndPublishAllJobProfiles_JAM() {
		super();
	}

	// ==================== CONSTANTS ====================
	public static final int PROFILES_PER_MINUTE = 100;
	public static final int REFRESH_INTERVAL_SECONDS = 30;

	// ==================== LOCATORS ====================
	// Using centralized Locators from BasePageObject
	private static final By CHEVRON_BTN = Locators.JAMScreen.CHEVRON_BUTTON;
	// SELECT_ALL_BTN is available via Locators.Table.SELECT_ALL_BTN
	// RESULTS_COUNT_TEXT is available via Locators.Table.RESULTS_COUNT_TEXT
	// VIEW_PUBLISHED_TOGGLE is available via Locators.Actions.VIEW_PUBLISHED_TOGGLE
	private static final By ASYNC_MESSAGE = Locators.JobMapping.ASYNC_MESSAGE;
	private static final By SUCCESS_HEADER = Locators.Modals.SUCCESS_MODAL_HEADER;
	private static final By SUCCESS_MSG = Locators.Modals.SUCCESS_MODAL_MESSAGE;
	private static final By SUCCESS_CLOSE_BTN = Locators.Modals.SUCCESS_MODAL_CLOSE_BTN;
	private static final By ALL_CHECKBOXES = Locators.Table.ROW_CHECKBOXES;
	private static final By SELECTED_CHECKBOXES = By.xpath("//tbody//tr//td[1]//input[@type='checkbox' and @checked]");

	// ==================== THREAD-SAFE STATE ====================
	public static ThreadLocal<Integer> unpublishedProfilesCountBefore = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<Integer> publishedProfilesCountBefore = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<Integer> unpublishedProfilesCountAfter = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<Integer> publishedProfilesCountAfter = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<Integer> totalPublishedCount = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<Integer> expectedTotalMinutes = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<Integer> profilesToBePublished = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<Integer> selectedProfilesCount = ThreadLocal.withInitial(() -> 0);

	/**
	 * Verifies count of total un-published profiles before publishing.
	 */
	public void verify_count_of_total_un_published_profiles_before_publishing_selected_profiles() {
		try {
			String countText = getElementText(Locators.Table.RESULTS_COUNT_TEXT);
			int count = parseProfileCountFromText(countText);
			
			if (count > 0) {
				unpublishedProfilesCountBefore.set(count);
				PageObjectHelper.log(LOGGER, "Un-Published Profiles count BEFORE Publishing: " + count);
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_unpublished_profiles_before", e);
			PageObjectHelper.handleError(LOGGER, "verify_unpublished_profiles_before", 
					"Error getting unpublished profiles count", e);
		}
	}

	/**
	 * Verifies count of total published profiles before publishing.
	 */
	public void verify_count_of_total_published_profiles_before_publishing_selected_profiles() {
		try {
			String countText = getElementText(Locators.Table.RESULTS_COUNT_TEXT);
			int count = parseProfileCountFromText(countText);
			
			if (count > 0) {
				publishedProfilesCountBefore.set(count);
				PageObjectHelper.log(LOGGER, "Published Profiles count BEFORE Publishing: " + count);
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_published_profiles_before", e);
			PageObjectHelper.handleError(LOGGER, "verify_published_profiles_before", 
					"Error getting published profiles count", e);
		}
	}

	/**
	 * Clicks on View Published toggle button to turn OFF.
	 */
	public void click_on_view_published_toggle_button_to_turn_off() {
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);

			WebElement toggle = findElement(Locators.Actions.VIEW_PUBLISHED_TOGGLE);
			if (toggle.isSelected() || "true".equals(toggle.getAttribute("aria-checked"))) {
				clickElement(Locators.Actions.VIEW_PUBLISHED_TOGGLE);
				PageObjectHelper.log(LOGGER, "Clicked View Published toggle to turn OFF");
				PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
				PerformanceUtils.waitForPageReady(driver, 2);
			}
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_view_published_toggle_off", e);
			PageObjectHelper.handleError(LOGGER, "click_view_published_toggle_off", 
					"Issue clicking View Published toggle", e);
		}
	}

	/**
	 * Clicks chevron button beside header checkbox in Job Mapping screen.
	 */
	public void click_on_chevron_button_beside_header_checkbox_in_job_mapping_screen() {
		try {
			PerformanceUtils.waitForPageReady(driver, 1);
			JavascriptExecutor js = (JavascriptExecutor) driver;

			js.executeScript("window.scrollTo(0, 0);");
			safeSleep(300);
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);

			WebElement chevronBtn = findElement(CHEVRON_BTN);
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", chevronBtn);
			safeSleep(300);

			clickElement(CHEVRON_BTN);
			PageObjectHelper.log(LOGGER, "Clicked Chevron Button beside Header Checkbox");
			PerformanceUtils.waitForPageReady(driver, 2);

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_chevron_button", e);
			PageObjectHelper.handleError(LOGGER, "click_chevron_button", "Issue clicking Chevron Button", e);
		}
	}

	/**
	 * Clicks on Select All button in Job Mapping screen.
	 */
	public void click_on_select_all_button_in_job_mapping_screen() {
		try {
			clickElement(Locators.Table.SELECT_ALL_BTN);
			PageObjectHelper.log(LOGGER, "Clicked Select All button");

			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_select_all_button", e);
			PageObjectHelper.handleError(LOGGER, "click_select_all_button", "Issue clicking Select All button", e);
		}
	}

	/**
	 * Scrolls through all job profiles and counts selected profiles.
	 */
	public void verify_count_of_selected_profiles_by_scrolling_through_all_profiles_in_job_mapping_screen() {
		int selectedCount = 0;
		int totalProfiles = 0;
		JavascriptExecutor js = (JavascriptExecutor) driver;

		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

			// Get total profile count
			try {
				String countText = getElementText(Locators.Table.RESULTS_COUNT_TEXT);
				totalProfiles = parseProfileCountFromText(countText);
				if (totalProfiles > 0) {
					LOGGER.debug("Total job profiles to process: {}", totalProfiles);
				}
			} catch (Exception e) {
				LOGGER.warn("Could not extract total profile count: {}", e.getMessage());
			}

			// Scroll to load all profiles
			int currentRowCount = 0;
			int previousRowCount = 0;
			int noChangeCount = 0;
			int maxScrollAttempts = 50;
			int scrollAttempts = 0;

			// Dynamic maxScrollAttempts based on total profiles
			// JAM screens load ~10 profiles per scroll (unlike PM screens which load ~50)
			if (totalProfiles > 0) {
				int estimatedScrolls = (int) Math.ceil(totalProfiles / 10.0);
				maxScrollAttempts = estimatedScrolls + 20; // Add 20 scrolls buffer for JAM
				maxScrollAttempts = Math.max(30, Math.min(500, maxScrollAttempts));
			}
			LOGGER.info("maxScrollAttempts: {} (estimated {} scrolls for {} profiles @ 10/scroll + 20 buffer)", 
					maxScrollAttempts, (int) Math.ceil(totalProfiles / 10.0), totalProfiles);

			while (scrollAttempts < maxScrollAttempts) {
				scrollToBottom();
				scrollAttempts++;
				safeSleep(3000);

				PerformanceUtils.waitForSpinnersToDisappear(driver, 5);
				PerformanceUtils.waitForPageReady(driver, 2);
				safeSleep(1000);

				currentRowCount = findElements(ALL_CHECKBOXES).size();

				// Log progress every 10 scrolls or when new data loads
				if (scrollAttempts % 10 == 0 || currentRowCount != previousRowCount) {
					int percentComplete = totalProfiles > 0 ? (currentRowCount * 100 / totalProfiles) : 0;
					LOGGER.info("Scroll {}/{}: Loaded {} profiles ({}% complete)", 
							scrollAttempts, maxScrollAttempts, currentRowCount, percentComplete);
				}

				if (currentRowCount == previousRowCount) {
					noChangeCount++;
					if (noChangeCount >= 3) {
						LOGGER.info("Reached end of content after {} consecutive non-loading scrolls", noChangeCount);
						break;
					}

					if (noChangeCount == 2) {
						js.executeScript(
								"window.scrollTo(0, Math.max(document.body.scrollHeight, document.documentElement.scrollHeight));");
						safeSleep(2000);
						PerformanceUtils.waitForSpinnersToDisappear(driver, 5);
					}
				} else {
					noChangeCount = 0;
				}

				previousRowCount = currentRowCount;
			}

			boolean reachedScrollLimit = scrollAttempts >= maxScrollAttempts;

			// Count selected, disabled, and unselected profiles
			int loadedSelectedCount = getSelectedProfileCount(js);
			int loadedDisabledCount = getDisabledProfileCount(js);
			int loadedUnselectedCount = currentRowCount - loadedSelectedCount - loadedDisabledCount;

			// Log summary - all 3 categories
			LOGGER.info("=== PROFILE COUNT SUMMARY ===");
			LOGGER.info("Total Profiles: {}", currentRowCount);
			LOGGER.info(" Selected (to be published): {}", loadedSelectedCount);
			LOGGER.info(" Unselected (enabled but not selected): {}", loadedUnselectedCount);
			LOGGER.info(" Disabled (cannot be selected): {}", loadedDisabledCount);

			if (reachedScrollLimit && totalProfiles > 0) {
				int unloadedProfiles = totalProfiles - currentRowCount;
				LOGGER.warn("Note: {} profiles not loaded due to scroll limit", unloadedProfiles);
			}

			selectedCount = loadedSelectedCount;
			selectedProfilesCount.set(selectedCount);

			PageObjectHelper.log(LOGGER, "Loaded: " + currentRowCount + " | Selected: " + loadedSelectedCount +
					" | Disabled: " + loadedDisabledCount + " | Unselected: " + loadedUnselectedCount);

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_selected_profiles_by_scrolling", e);
			PageObjectHelper.handleError(LOGGER, "verify_selected_profiles_by_scrolling", 
					"Error getting selected job profiles count", e);
		}
	}

	/**
	 * Verifies async functionality message is displayed.
	 */
	public void verify_async_functionality_message_is_displayed_on_jam_screen() {
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 3);

			WebElement asyncMsg = findElement(ASYNC_MESSAGE);
			if (asyncMsg.isDisplayed()) {
				String messageText = asyncMsg.getText();
				PageObjectHelper.log(LOGGER, "Async functionality message: " + messageText);
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_async_message", e);
			PageObjectHelper.handleError(LOGGER, "verify_async_message", "Error verifying async message", e);
		}
	}

	/**
	 * Refreshes page after publishing and handles success popup if appears.
	 */
	public void refresh_job_mapping_page_after_specified_time_in_message() throws InterruptedException {
		try {
			LOGGER.debug("Checking for success popup...");
			boolean popupAppeared = false;

			try {
				WebDriverWait shortWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
				shortWait.until(ExpectedConditions.visibilityOfElementLocated(SUCCESS_HEADER));
				popupAppeared = true;

				String successHeaderText = getElementText(SUCCESS_HEADER);
				String successMsgText = getElementText(SUCCESS_MSG);
				PageObjectHelper.log(LOGGER, "Success popup appeared - Header: " + successHeaderText + 
						", Message: " + successMsgText);

				// Close the popup
				try {
					clickElement(SUCCESS_CLOSE_BTN);
				} catch (Exception e) {
					jsClick(findElement(SUCCESS_CLOSE_BTN));
				}

				PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
				PerformanceUtils.waitForPageReady(driver, 2);

			} catch (Exception popupException) {
				PageObjectHelper.log(LOGGER, "Async publishing detected - no immediate success popup");
			}

			if (!popupAppeared) {
				LOGGER.debug("Waiting 30 seconds before initial refresh...");
				safeSleep(30000);
			}

			driver.navigate().refresh();
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 3);

			PageObjectHelper.log(LOGGER, "Ready for progressive monitoring");

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("refresh_after_publish", e);
			PageObjectHelper.handleError(LOGGER, "refresh_after_publish", "Error in initial refresh", e);
		}
	}

	/**
	 * Verifies count of total un-published profiles after publishing.
	 */
	public void verify_count_of_total_un_published_profiles_after_publishing_selected_profiles() {
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

			String countText = getElementText(Locators.Table.RESULTS_COUNT_TEXT);
			int count = parseProfileCountFromText(countText);

			if (count >= 0) {
				unpublishedProfilesCountAfter.set(count);
				PageObjectHelper.log(LOGGER, "Un-Published Profiles count AFTER Publishing: " + count);

				if (count < unpublishedProfilesCountBefore.get()) {
					PageObjectHelper.log(LOGGER, "Unpublished count decreased from " + 
							unpublishedProfilesCountBefore.get() + " to " + count);
				}
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_unpublished_profiles_after", e);
			PageObjectHelper.handleError(LOGGER, "verify_unpublished_profiles_after", 
					"Error getting unpublished profiles count", e);
		}
	}

	/**
	 * Gets count of published profiles.
	 */
	public void get_count_of_published_profiles_in_job_mapping_screen() {
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

			totalPublishedCount.set(unpublishedProfilesCountBefore.get() - unpublishedProfilesCountAfter.get()
					+ publishedProfilesCountBefore.get());

			LOGGER.debug("Published Profiles Calculation: (Before: {} - After: {}) + Published Before: {} = Total: {}",
					unpublishedProfilesCountBefore.get(), unpublishedProfilesCountAfter.get(),
					publishedProfilesCountBefore.get(), totalPublishedCount.get());

			PageObjectHelper.log(LOGGER, "Total Published: " + totalPublishedCount.get() + " profiles");

			// Validation
			if (selectedProfilesCount.get() > 0) {
				int actualPublished = unpublishedProfilesCountBefore.get() - unpublishedProfilesCountAfter.get();

				if (actualPublished == selectedProfilesCount.get()) {
					PageObjectHelper.log(LOGGER, "Validation PASSED: " + actualPublished + 
							" published (matches " + selectedProfilesCount.get() + " selected)");
				} else {
					int difference = Math.abs(actualPublished - selectedProfilesCount.get());
					PageObjectHelper.log(LOGGER, "Validation WARNING: Expected " + selectedProfilesCount.get() + 
							", Actual " + actualPublished + " (difference: " + difference + ")");
				}
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("get_published_profiles_count", e);
			PageObjectHelper.handleError(LOGGER, "get_published_profiles_count", "Error calculating published count", e);
		}
	}

	/**
	 * Verifies count of total published profiles after publishing.
	 */
	public void verify_count_of_total_published_profiles_after_publishing_selected_profiles() {
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

			String countText = getElementText(Locators.Table.RESULTS_COUNT_TEXT);
			int count = parseProfileCountFromText(countText);

			if (count > 0) {
				publishedProfilesCountAfter.set(count);
				PageObjectHelper.log(LOGGER, "Published Profiles count AFTER Publishing: " + count);
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_published_profiles_after", e);
			PageObjectHelper.handleError(LOGGER, "verify_published_profiles_after", 
					"Error getting published profiles count", e);
		}
	}

	/**
	 * Verifies published profiles count matches in View Published screen.
	 */
	public void verify_published_profiles_count_matches_in_view_published_screen() {
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

			if (publishedProfilesCountAfter.get().equals(totalPublishedCount.get())) {
				PageObjectHelper.log(LOGGER, "Published profiles count matches: " + totalPublishedCount.get());
			} else {
				PageObjectHelper.log(LOGGER, "Count mismatch! Expected: " + totalPublishedCount.get() + 
						", Actual: " + publishedProfilesCountAfter.get());
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_published_count_match", e);
			PageObjectHelper.handleError(LOGGER, "verify_published_count_match", "Error verifying count match", e);
		}
	}

	/**
	 * Verify user is on Job Mapping page after initial refresh.
	 */
	public void user_is_in_job_mapping_page_after_initial_refresh() {
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);
			LOGGER.debug("Job Mapping page ready");

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_job_mapping_page", e);
			PageObjectHelper.handleError(LOGGER, "verify_job_mapping_page", "Error verifying Job Mapping page", e);
		}
	}

	/**
	 * Calculate expected total time for batch publishing.
	 */
	public void calculate_expected_total_time_for_batch_publishing_based_on_profile_count() {
		try {
			profilesToBePublished.set(selectedProfilesCount.get());
			expectedTotalMinutes.set((int) Math.ceil((double) profilesToBePublished.get() / PROFILES_PER_MINUTE));

			PageObjectHelper.log(LOGGER, "Publishing " + profilesToBePublished.get() + " profiles @ " + 
					PROFILES_PER_MINUTE + "/min = ~" + expectedTotalMinutes.get() + " min expected");

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("calculate_expected_time", e);
			PageObjectHelper.handleError(LOGGER, "calculate_expected_time", "Error calculating expected time", e);
		}
	}

	/**
	 * Monitor and validate progressive batch publishing until completion.
	 */
	public void monitor_and_validate_progressive_batch_publishing_until_completion() throws InterruptedException {
		int maxWaitMinutes = expectedTotalMinutes.get() + 5;
		int maxWaitSeconds = maxWaitMinutes * 60;
		int elapsedSeconds = 0;
		int checkNumber = 0;
		int initialUnpublishedCount = unpublishedProfilesCountBefore.get();
		int previousUnpublishedCount = unpublishedProfilesCountBefore.get();
		int currentUnpublishedCount = unpublishedProfilesCountBefore.get();
		int targetUnpublishedCount = unpublishedProfilesCountBefore.get() - selectedProfilesCount.get();

		try {
			PageObjectHelper.log(LOGGER, "Monitoring: Publish " + selectedProfilesCount.get() + 
					" profiles (check every " + REFRESH_INTERVAL_SECONDS + "s, max " + maxWaitMinutes + " min)");

			while (elapsedSeconds < maxWaitSeconds) {
				checkNumber++;
				elapsedSeconds += REFRESH_INTERVAL_SECONDS;
				int elapsedMinutes = elapsedSeconds / 60;

				safeSleep(REFRESH_INTERVAL_SECONDS * 1000);
				driver.navigate().refresh();
				PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
				PerformanceUtils.waitForPageReady(driver, 2);

				// Get current unpublished count
				try {
					WebElement freshResultsElement = driver.findElement(Locators.Table.RESULTS_COUNT_TEXT);
					String countText = freshResultsElement.getText().trim();
					currentUnpublishedCount = parseProfileCountFromText(countText);
				} catch (Exception e) {
					LOGGER.warn("Could not read count at check #{}: {}", checkNumber, e.getMessage());
				}

				// Calculate progress
				int profilesPublishedSinceLastCheck = previousUnpublishedCount - currentUnpublishedCount;
				int totalPublishedSoFar = initialUnpublishedCount - currentUnpublishedCount;
				double progressPercentage = selectedProfilesCount.get() > 0 ? 
						((double) totalPublishedSoFar / selectedProfilesCount.get()) * 100 : 0;
				double publishingRate = elapsedSeconds > 0 ? (totalPublishedSoFar * 60.0) / elapsedSeconds : 0;

				String progressMsg = String.format("Check #%d (%dm%ds): %d remaining | +%d | %.0f/min | %.1f%%",
						checkNumber, elapsedMinutes, (elapsedSeconds % 60), currentUnpublishedCount,
						profilesPublishedSinceLastCheck, publishingRate, progressPercentage);
				
				PageObjectHelper.log(LOGGER, progressMsg);

				// Check if complete
				if (currentUnpublishedCount <= targetUnpublishedCount) {
					PageObjectHelper.log(LOGGER, "SUCCESS! All " + totalPublishedSoFar + 
							" profiles published in " + elapsedMinutes + " min");
					break;
				}

				// Warn if no progress
				if (elapsedSeconds >= 120 && profilesPublishedSinceLastCheck == 0) {
					PageObjectHelper.log(LOGGER, "Warning: No progress in check #" + checkNumber);
				}

				previousUnpublishedCount = currentUnpublishedCount;
			}

			// Final check
			int remainingToPublish = currentUnpublishedCount - targetUnpublishedCount;
			if (remainingToPublish > 0) {
				PageObjectHelper.log(LOGGER, "Publishing timeout after " + checkNumber + 
						" checks. Still remaining: " + remainingToPublish + " profiles");
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("monitor_batch_publishing", e);
			PageObjectHelper.handleError(LOGGER, "monitor_batch_publishing", "Error monitoring batch publishing", e);
		}
	}

	/**
	 * Verify all selected profiles are published successfully.
	 */
	public void verify_all_profiles_are_published_successfully() {
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

			int finalUnpublishedCount = 0;

			try {
				WebElement freshResultsElement = driver.findElement(Locators.Table.RESULTS_COUNT_TEXT);
				String countText = freshResultsElement.getText().trim();
				finalUnpublishedCount = parseProfileCountFromText(countText);
			} catch (Exception e) {
				LOGGER.warn("Could not read final unpublished count: {}", e.getMessage());
			}

			int initialUnpublishedCount = unpublishedProfilesCountBefore.get();
			int actualPublished = initialUnpublishedCount - finalUnpublishedCount;
			int expectedTargetUnpublished = initialUnpublishedCount - selectedProfilesCount.get();
			int remainingToPublish = finalUnpublishedCount - expectedTargetUnpublished;

			if (finalUnpublishedCount <= expectedTargetUnpublished) {
				PageObjectHelper.log(LOGGER, "SUCCESS! All " + actualPublished + " profiles published");
			} else {
				PageObjectHelper.log(LOGGER, "Incomplete! " + remainingToPublish + " of " + 
						selectedProfilesCount.get() + " profiles still pending");
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_all_published", e);
			PageObjectHelper.handleError(LOGGER, "verify_all_published", "Error verifying final status", e);
		}
	}

	// ==================== HELPER METHODS ====================
	// Note: Common helpers (parseProfileCountFromText, countSelectedCheckboxes, countDisabledCheckboxes)
	// are inherited from BasePageObject

	private int getSelectedProfileCount(JavascriptExecutor js) {
		int count = countSelectedCheckboxes("tbody tr");
		if (count == 0) {
			// Fallback to XPath
			try {
				return findElements(SELECTED_CHECKBOXES).size();
			} catch (Exception ex) {
				return 0;
			}
		}
		return count;
	}

	private int getDisabledProfileCount(JavascriptExecutor js) {
		return countDisabledCheckboxes("tbody tr");
	}
}
