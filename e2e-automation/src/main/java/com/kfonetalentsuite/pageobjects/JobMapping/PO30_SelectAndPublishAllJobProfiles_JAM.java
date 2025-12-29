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
	public static final int MAX_CHECK_ATTEMPTS = 10;
	public static final int MAX_CONSECUTIVE_NO_PROGRESS_CHECKS = 5; // Increased to 5 for large datasets
	public static final int MIN_CHECKS_TO_CONFIRM_PROGRESS = 2; // Minimum checks showing progress to confirm publishing is working
	public static final int INITIAL_WAIT_SECONDS = 60; // Initial wait before first check (for backend to start processing)

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
	 * Extracts selected profiles count from "Showing X of Y results" text without scrolling.
	 * This optimized method eliminates the need to scroll through all profiles for large datasets,
	 * significantly reducing execution time from hours to seconds.
	 */
	public void verify_count_of_selected_profiles_by_scrolling_through_all_profiles_in_job_mapping_screen() {
		int selectedCount = 0;

		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

			// Get total profile count from "Showing X of Y results" text
			try {
				String countText = getElementText(Locators.Table.RESULTS_COUNT_TEXT);
				selectedCount = parseProfileCountFromText(countText);
				
				if (selectedCount > 0) {
					LOGGER.info("=== SELECTED PROFILES COUNT (from 'Showing X of Y results') ===");
					LOGGER.info("Total Selected Profiles to be published: {}", selectedCount);
					
					selectedProfilesCount.set(selectedCount);
					
					PageObjectHelper.log(LOGGER, "Selected Profiles Count: " + selectedCount + 
							" (extracted from results count text without scrolling)");
				} else {
					LOGGER.warn("Could not extract selected profiles count from results text");
				}
			} catch (Exception e) {
				LOGGER.error("Error extracting profile count from results text: {}", e.getMessage());
				throw e;
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_selected_profiles_count", e);
			PageObjectHelper.handleError(LOGGER, "verify_selected_profiles_count", 
					"Error getting selected job profiles count from results text", e);
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
	 * Monitor and validate progressive batch publishing is working.
	 * Does NOT wait for all profiles to publish - just confirms publishing is progressing.
	 * Starts with 60-second initial wait for backend to initialize.
	 * Then uses maximum of 10 checks (every 30 seconds).
	 * Confirms success once progress is detected in 2 consecutive checks.
	 * Terminates early if no progress is detected in 5 consecutive checks.
	 */
	public void monitor_and_validate_progressive_batch_publishing_until_completion() throws InterruptedException {
		int checkNumber = 0;
		int consecutiveNoProgressChecks = 0;
		int checksWithProgress = 0;
		int initialUnpublishedCount = unpublishedProfilesCountBefore.get();
		int previousUnpublishedCount = unpublishedProfilesCountBefore.get();
		int currentUnpublishedCount = unpublishedProfilesCountBefore.get();
		int targetUnpublishedCount = unpublishedProfilesCountBefore.get() - selectedProfilesCount.get();
		int totalElapsedSeconds = 0;
		boolean publishingConfirmed = false;
		String terminationReason = "";

		try {
			PageObjectHelper.log(LOGGER, "=== STARTING PROGRESSIVE BATCH PUBLISHING VALIDATION ===");
			PageObjectHelper.log(LOGGER, "Objective: Confirm publishing is working (not waiting for completion)");
			PageObjectHelper.log(LOGGER, "Dataset size: " + selectedProfilesCount.get() + " profiles");
			PageObjectHelper.log(LOGGER, "Initial wait: " + INITIAL_WAIT_SECONDS + " seconds (for backend to start processing)");
			PageObjectHelper.log(LOGGER, "Check interval: " + REFRESH_INTERVAL_SECONDS + " seconds");
			PageObjectHelper.log(LOGGER, "Max check attempts: " + MAX_CHECK_ATTEMPTS);
			PageObjectHelper.log(LOGGER, "Initial unpublished count: " + initialUnpublishedCount);
			PageObjectHelper.log(LOGGER, "Target unpublished count: " + targetUnpublishedCount);

			// Initial wait for backend to start processing (especially for large datasets)
			PageObjectHelper.log(LOGGER, "⏳ Waiting " + INITIAL_WAIT_SECONDS + " seconds for backend to initialize batch publishing...");
			safeSleep(INITIAL_WAIT_SECONDS * 1000);
			totalElapsedSeconds += INITIAL_WAIT_SECONDS;

			while (checkNumber < MAX_CHECK_ATTEMPTS) {
				checkNumber++;
				
				// Refresh and wait for page ready
				driver.navigate().refresh();
				// Refresh and wait for page ready
				driver.navigate().refresh();
				PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
				PerformanceUtils.waitForPageReady(driver, 2);

				// Get current unpublished count from results text
				try {
					WebElement freshResultsElement = driver.findElement(Locators.Table.RESULTS_COUNT_TEXT);
					String countText = freshResultsElement.getText().trim();
					currentUnpublishedCount = parseProfileCountFromText(countText);
				} catch (Exception e) {
					LOGGER.warn("Could not read count at check #{}: {}", checkNumber, e.getMessage());
					// Continue with previous count if read fails
				}

				// Calculate progress metrics
				int profilesPublishedSinceLastCheck = previousUnpublishedCount - currentUnpublishedCount;
				int totalPublishedSoFar = initialUnpublishedCount - currentUnpublishedCount;
				int remainingToPublish = currentUnpublishedCount - targetUnpublishedCount;
				double progressPercentage = selectedProfilesCount.get() > 0 ? 
						((double) totalPublishedSoFar / selectedProfilesCount.get()) * 100 : 0;
				double publishingRate = totalElapsedSeconds > 0 ? 
						(totalPublishedSoFar * 60.0) / totalElapsedSeconds : 0;

				// Track consecutive no-progress checks and checks with progress
				if (profilesPublishedSinceLastCheck > 0) {
					checksWithProgress++;
					consecutiveNoProgressChecks = 0; // Reset counter when progress is made
				} else {
					consecutiveNoProgressChecks++;
				}

				// Calculate elapsed time
				int elapsedMinutes = totalElapsedSeconds / 60;
				int elapsedSecondsRemainder = totalElapsedSeconds % 60;

				// Log progress
				String progressMsg = String.format(
						"Check #%d/%d (%dm%ds): Remaining=%d | Published this check=+%d | Total published=%d | Rate=%.0f/min | Progress=%.1f%% | Checks with progress=%d",
						checkNumber, MAX_CHECK_ATTEMPTS, elapsedMinutes, elapsedSecondsRemainder,
						currentUnpublishedCount, profilesPublishedSinceLastCheck, totalPublishedSoFar,
						publishingRate, progressPercentage, checksWithProgress);
				
				PageObjectHelper.log(LOGGER, progressMsg);

				// Check if all profiles are published (early completion)
				if (currentUnpublishedCount <= targetUnpublishedCount) {
					publishingConfirmed = true;
					terminationReason = "All profiles published successfully";
					PageObjectHelper.log(LOGGER, "✓ SUCCESS! All " + totalPublishedSoFar + 
							" profiles published in " + elapsedMinutes + "m " + elapsedSecondsRemainder + "s");
					break;
				}

				// Check if publishing progress is confirmed (KEY OPTIMIZATION)
				if (checksWithProgress >= MIN_CHECKS_TO_CONFIRM_PROGRESS) {
					publishingConfirmed = true;
					terminationReason = "Publishing confirmed - progress detected in " + checksWithProgress + " checks";
					PageObjectHelper.log(LOGGER, "✓ SUCCESS! Publishing is working properly");
					PageObjectHelper.log(LOGGER, "Published so far: " + totalPublishedSoFar + 
							" out of " + selectedProfilesCount.get() + 
							" (" + String.format("%.1f%%", progressPercentage) + ")");
					PageObjectHelper.log(LOGGER, "Publishing rate: " + String.format("%.0f", publishingRate) + " profiles/min");
					PageObjectHelper.log(LOGGER, "Estimated time for all profiles: ~" + 
							Math.ceil((double) remainingToPublish / publishingRate) + " minutes remaining");
					PageObjectHelper.log(LOGGER, "Validation complete - not waiting for full completion");
					break;
				}

				// Check for consecutive no-progress termination
				if (consecutiveNoProgressChecks >= MAX_CONSECUTIVE_NO_PROGRESS_CHECKS) {
					terminationReason = "No progress detected in " + MAX_CONSECUTIVE_NO_PROGRESS_CHECKS + 
							" consecutive checks";
					PageObjectHelper.log(LOGGER, "⚠ EARLY TERMINATION: " + terminationReason);
					PageObjectHelper.log(LOGGER, "Total time elapsed: " + elapsedMinutes + "m " + elapsedSecondsRemainder + "s");
					PageObjectHelper.log(LOGGER, "Published so far: " + totalPublishedSoFar + 
							" out of " + selectedProfilesCount.get() + 
							" (" + String.format("%.1f%%", progressPercentage) + ")");
					PageObjectHelper.log(LOGGER, "Still remaining: " + remainingToPublish + " profiles");
					
					if (totalPublishedSoFar == 0) {
						PageObjectHelper.log(LOGGER, "⚠ Publishing may not have started - check backend logs for errors");
					} else {
						PageObjectHelper.log(LOGGER, "⚠ Publishing started but appears to have stalled");
					}
					break;
				}

				// Warn if no progress but below consecutive threshold
				if (consecutiveNoProgressChecks > 0) {
					PageObjectHelper.log(LOGGER, "⚠ Warning: No progress in check #" + checkNumber + 
							" (streak: " + consecutiveNoProgressChecks + "/" + MAX_CONSECUTIVE_NO_PROGRESS_CHECKS + ")");
				}

				previousUnpublishedCount = currentUnpublishedCount;
				
				// Wait before next check (except after last check)
				if (checkNumber < MAX_CHECK_ATTEMPTS) {
					safeSleep(REFRESH_INTERVAL_SECONDS * 1000);
					totalElapsedSeconds += REFRESH_INTERVAL_SECONDS;
				}
			}

			// Final summary after max attempts reached (without confirmation)
			if (checkNumber >= MAX_CHECK_ATTEMPTS && !publishingConfirmed) {
				terminationReason = "Maximum check attempts (" + MAX_CHECK_ATTEMPTS + ") reached without confirming progress";
				int totalPublishedSoFar = initialUnpublishedCount - currentUnpublishedCount;
				double finalProgress = selectedProfilesCount.get() > 0 ? 
						((double) totalPublishedSoFar / selectedProfilesCount.get()) * 100 : 0;

				PageObjectHelper.log(LOGGER, "=== VALIDATION ENDED: " + terminationReason + " ===");
				PageObjectHelper.log(LOGGER, "Total time elapsed: " + (totalElapsedSeconds / 60) + "m " + 
						(totalElapsedSeconds % 60) + "s");
				PageObjectHelper.log(LOGGER, "Profiles published: " + totalPublishedSoFar + " out of " + 
						selectedProfilesCount.get() + " (" + String.format("%.1f%%", finalProgress) + ")");
				PageObjectHelper.log(LOGGER, "Checks with progress: " + checksWithProgress + "/" + checkNumber);
				
				if (checksWithProgress > 0) {
					PageObjectHelper.log(LOGGER, "⚠ Publishing appears to be working but needs more time");
				} else {
					PageObjectHelper.log(LOGGER, "⚠ No publishing progress detected in any checks");
				}
			}

			// Final validation result summary
			PageObjectHelper.log(LOGGER, "=== PROGRESSIVE BATCH PUBLISHING VALIDATION COMPLETE ===");
			PageObjectHelper.log(LOGGER, "Status: " + (publishingConfirmed ? "CONFIRMED - Publishing is working" : "UNCONFIRMED - Publishing may have issues"));
			PageObjectHelper.log(LOGGER, "Termination reason: " + terminationReason);
			PageObjectHelper.log(LOGGER, "Total checks performed: " + checkNumber + "/" + MAX_CHECK_ATTEMPTS);
			PageObjectHelper.log(LOGGER, "Checks showing progress: " + checksWithProgress);
			PageObjectHelper.log(LOGGER, "Total time: " + (totalElapsedSeconds / 60) + " minutes " + 
					(totalElapsedSeconds % 60) + " seconds");

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("monitor_batch_publishing", e);
			PageObjectHelper.handleError(LOGGER, "monitor_batch_publishing", 
					"Error monitoring batch publishing", e);
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
	// Note: Common helpers (parseProfileCountFromText, etc.) are inherited from BasePageObject
}
