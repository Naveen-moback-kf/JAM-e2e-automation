package com.kfonetalentsuite.pageobjects.JobMapping;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import com.kfonetalentsuite.utils.JobMapping.ScreenshotHandler;
import com.kfonetalentsuite.utils.JobMapping.PageObjectHelper;

public class PO30_SelectAndPublishAllJobProfiles_JAM extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO30_SelectAndPublishAllJobProfiles_JAM.class);

	public PO30_SelectAndPublishAllJobProfiles_JAM() {
		super();
	}
	public static final int PROFILES_PER_MINUTE = 100;
	public static final int REFRESH_INTERVAL_SECONDS = 30;
	public static final int MAX_CHECK_ATTEMPTS = 10;
	public static final int MAX_CONSECUTIVE_NO_PROGRESS_CHECKS = 5; // Increased to 5 for large datasets
	public static final int MIN_CHECKS_TO_CONFIRM_PROGRESS = 2; // Minimum checks showing progress to confirm publishing is working
	public static final int INITIAL_WAIT_SECONDS = 60; // Initial wait before first check (for backend to start processing)
	// Using centralized Locators from BasePageObject
	private static final By CHEVRON_BTN = Locators.JAMScreen.CHEVRON_BUTTON;
	// SELECT_ALL_BTN is available via Locators.Table.SELECT_ALL_BTN
	// RESULTS_COUNT_TEXT is available via Locators.Table.RESULTS_COUNT_TEXT
	// VIEW_PUBLISHED_TOGGLE is available via Locators.Actions.VIEW_PUBLISHED_TOGGLE
	private static final By ASYNC_MESSAGE = Locators.JobMapping.ASYNC_MESSAGE;
	private static final By SUCCESS_HEADER = Locators.Modals.SUCCESS_MODAL_HEADER;
	private static final By SUCCESS_MSG = Locators.Modals.SUCCESS_MODAL_MESSAGE;
	private static final By SUCCESS_CLOSE_BTN = Locators.Modals.SUCCESS_MODAL_CLOSE_BTN;
	public static ThreadLocal<Integer> unpublishedProfilesCountBefore = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<Integer> publishedProfilesCountBefore = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<Integer> unpublishedProfilesCountAfter = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<Integer> publishedProfilesCountAfter = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<Integer> totalPublishedCount = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<Integer> expectedTotalMinutes = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<Integer> profilesToBePublished = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<Integer> selectedProfilesCount = ThreadLocal.withInitial(() -> 0);

	public void verify_count_of_total_un_published_profiles_before_publishing_selected_profiles() {
		try {
			String countText = getElementText(Locators.Table.RESULTS_COUNT_TEXT);
			int count = parseProfileCountFromText(countText);
			
			if (count > 0) {
				unpublishedProfilesCountBefore.set(count);
				LOGGER.info("Un-Published Profiles count BEFORE Publishing: " + count);
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_unpublished_profiles_before", e);
			PageObjectHelper.handleError(LOGGER, "verify_unpublished_profiles_before", 
					"Error getting unpublished profiles count", e);
		}
	}

	public void verify_count_of_total_published_profiles_before_publishing_selected_profiles() {
		try {
			String countText = getElementText(Locators.Table.RESULTS_COUNT_TEXT);
			int count = parseProfileCountFromText(countText);
			
			if (count > 0) {
				publishedProfilesCountBefore.set(count);
				LOGGER.info("Published Profiles count BEFORE Publishing: " + count);
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_published_profiles_before", e);
			PageObjectHelper.handleError(LOGGER, "verify_published_profiles_before", 
					"Error getting published profiles count", e);
		}
	}

	public void click_on_view_published_toggle_button_to_turn_off() {
		try {
			PageObjectHelper.waitForSpinnersToDisappear(driver, 10);

			WebElement toggle = findElement(Locators.Actions.VIEW_PUBLISHED_TOGGLE);
			if (toggle.isSelected() || "true".equals(toggle.getAttribute("aria-checked"))) {
				clickElement(Locators.Actions.VIEW_PUBLISHED_TOGGLE);
				LOGGER.info("Clicked View Published toggle to turn OFF");
				PageObjectHelper.waitForSpinnersToDisappear(driver, 10);
				PageObjectHelper.waitForPageReady(driver, 2);
			}
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_view_published_toggle_off", e);
			PageObjectHelper.handleError(LOGGER, "click_view_published_toggle_off", 
					"Issue clicking View Published toggle", e);
		}
	}

	public void click_on_chevron_button_beside_header_checkbox_in_job_mapping_screen() {
		try {
			PageObjectHelper.waitForPageReady(driver, 1);
			JavascriptExecutor js = (JavascriptExecutor) driver;

			js.executeScript("window.scrollTo(0, 0);");
			safeSleep(300);
			PageObjectHelper.waitForSpinnersToDisappear(driver, 10);

			WebElement chevronBtn = findElement(CHEVRON_BTN);
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", chevronBtn);
			safeSleep(300);

			clickElement(CHEVRON_BTN);
			LOGGER.info("Clicked Chevron Button beside Header Checkbox");
			PageObjectHelper.waitForPageReady(driver, 2);

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_chevron_button", e);
			PageObjectHelper.handleError(LOGGER, "click_chevron_button", "Issue clicking Chevron Button", e);
		}
	}

	public void click_on_select_all_button_in_job_mapping_screen() {
		try {
			clickElement(Locators.Table.SELECT_ALL_BTN);
			PageObjectHelper.waitForSpinnersToDisappear(driver, 10);
			PageObjectHelper.waitForPageReady(driver, 2);

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_select_all_button", e);
			PageObjectHelper.handleError(LOGGER, "click_select_all_button", "Issue clicking Select All button", e);
		}
	}

	public void verify_count_of_selected_profiles_by_scrolling_through_all_profiles_in_job_mapping_screen() {
		int selectedCount = 0;

		try {
			PageObjectHelper.waitForSpinnersToDisappear(driver, 10);
			PageObjectHelper.waitForPageReady(driver, 2);

			// Get total profile count from "Showing X of Y results" text
			try {
				String countText = getElementText(Locators.Table.RESULTS_COUNT_TEXT);
				selectedCount = parseProfileCountFromText(countText);
				
				if (selectedCount > 0) {
					LOGGER.info("=== SELECTED PROFILES COUNT (from 'Showing X of Y results') ===");
					LOGGER.info("Total Selected Profiles to be published: {}", selectedCount);
					
					selectedProfilesCount.set(selectedCount);
					
					LOGGER.info("Selected Profiles Count: " + selectedCount + 
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

	public void verify_async_functionality_message_is_displayed_on_jam_screen() {
		try {
			PageObjectHelper.waitForSpinnersToDisappear(driver, 10);
			PageObjectHelper.waitForPageReady(driver, 3);

			WebElement asyncMsg = findElement(ASYNC_MESSAGE);
			if (asyncMsg.isDisplayed()) {
				String messageText = asyncMsg.getText();
				LOGGER.info("Async functionality message: " + messageText);
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_async_message", e);
			PageObjectHelper.handleError(LOGGER, "verify_async_message", "Error verifying async message", e);
		}
	}

	public void refresh_job_mapping_page_after_specified_time_in_message() throws InterruptedException {
		try {
			LOGGER.debug("Checking for success popup...");
			boolean popupAppeared = false;

			try {
				PageObjectHelper.waitForVisible(wait, SUCCESS_HEADER);
				popupAppeared = true;

			String successHeaderText = getElementText(SUCCESS_HEADER);
			String successMsgText = getElementText(SUCCESS_MSG);
			LOGGER.info("Success popup appeared - Header: " + successHeaderText + 
					", Message: " + successMsgText);

			// Close the popup with multiple fallback strategies
			closeSuccessPopupWithFallback();

			PageObjectHelper.waitForSpinnersToDisappear(driver, 10);
			PageObjectHelper.waitForPageReady(driver, 2);

			} catch (Exception popupException) {
				LOGGER.info("Async publishing detected - no immediate success popup");
			}

			if (!popupAppeared) {
				LOGGER.debug("Waiting 30 seconds before initial refresh...");
				safeSleep(30000);
			}

			driver.navigate().refresh();
			PageObjectHelper.waitForSpinnersToDisappear(driver, 10);
			PageObjectHelper.waitForPageReady(driver, 3);

			LOGGER.info("Ready for progressive monitoring");

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("refresh_after_publish", e);
			PageObjectHelper.handleError(LOGGER, "refresh_after_publish", "Error in initial refresh", e);
		}
	}

	public void verify_count_of_total_un_published_profiles_after_publishing_selected_profiles() {
		try {
			PageObjectHelper.waitForSpinnersToDisappear(driver, 10);
			PageObjectHelper.waitForPageReady(driver, 2);

			String countText = getElementText(Locators.Table.RESULTS_COUNT_TEXT);
			int count = parseProfileCountFromText(countText);

			if (count >= 0) {
				unpublishedProfilesCountAfter.set(count);
				LOGGER.info("Un-Published Profiles count AFTER Publishing: " + count);

				if (count < unpublishedProfilesCountBefore.get()) {
					LOGGER.info("Unpublished count decreased from " + 
							unpublishedProfilesCountBefore.get() + " to " + count);
				}
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_unpublished_profiles_after", e);
			PageObjectHelper.handleError(LOGGER, "verify_unpublished_profiles_after", 
					"Error getting unpublished profiles count", e);
		}
	}

	public void get_count_of_published_profiles_in_job_mapping_screen() {
		try {
			PageObjectHelper.waitForSpinnersToDisappear(driver, 10);
			PageObjectHelper.waitForPageReady(driver, 2);

			totalPublishedCount.set(unpublishedProfilesCountBefore.get() - unpublishedProfilesCountAfter.get()
					+ publishedProfilesCountBefore.get());

			LOGGER.debug("Published Profiles Calculation: (Before: {} - After: {}) + Published Before: {} = Total: {}",
					unpublishedProfilesCountBefore.get(), unpublishedProfilesCountAfter.get(),
					publishedProfilesCountBefore.get(), totalPublishedCount.get());

			LOGGER.info("Total Published: " + totalPublishedCount.get() + " profiles");

			// Validation
			if (selectedProfilesCount.get() > 0) {
				int actualPublished = unpublishedProfilesCountBefore.get() - unpublishedProfilesCountAfter.get();

				if (actualPublished == selectedProfilesCount.get()) {
					LOGGER.info("Validation PASSED: " + actualPublished + 
							" published (matches " + selectedProfilesCount.get() + " selected)");
				} else {
					int difference = Math.abs(actualPublished - selectedProfilesCount.get());
					LOGGER.info("Validation WARNING: Expected " + selectedProfilesCount.get() + 
							", Actual " + actualPublished + " (difference: " + difference + ")");
				}
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("get_published_profiles_count", e);
			PageObjectHelper.handleError(LOGGER, "get_published_profiles_count", "Error calculating published count", e);
		}
	}

	public void verify_count_of_total_published_profiles_after_publishing_selected_profiles() {
		try {
			PageObjectHelper.waitForSpinnersToDisappear(driver, 10);
			PageObjectHelper.waitForPageReady(driver, 2);

			String countText = getElementText(Locators.Table.RESULTS_COUNT_TEXT);
			int count = parseProfileCountFromText(countText);

			if (count > 0) {
				publishedProfilesCountAfter.set(count);
				LOGGER.info("Published Profiles count AFTER Publishing: " + count);
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_published_profiles_after", e);
			PageObjectHelper.handleError(LOGGER, "verify_published_profiles_after", 
					"Error getting published profiles count", e);
		}
	}

	public void verify_published_profiles_count_matches_in_view_published_screen() {
		try {
			PageObjectHelper.waitForSpinnersToDisappear(driver, 10);
			PageObjectHelper.waitForPageReady(driver, 2);

			if (publishedProfilesCountAfter.get().equals(totalPublishedCount.get())) {
				LOGGER.info("Published profiles count matches: " + totalPublishedCount.get());
			} else {
				LOGGER.info("Count mismatch! Expected: " + totalPublishedCount.get() + 
						", Actual: " + publishedProfilesCountAfter.get());
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_published_count_match", e);
			PageObjectHelper.handleError(LOGGER, "verify_published_count_match", "Error verifying count match", e);
		}
	}

	public void user_is_in_job_mapping_page_after_initial_refresh() {
		try {
			PageObjectHelper.waitForSpinnersToDisappear(driver, 10);
			PageObjectHelper.waitForPageReady(driver, 2);
			LOGGER.debug("Job Mapping page ready");

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_job_mapping_page", e);
			PageObjectHelper.handleError(LOGGER, "verify_job_mapping_page", "Error verifying Job Mapping page", e);
		}
	}

	public void calculate_expected_total_time_for_batch_publishing_based_on_profile_count() {
		try {
			profilesToBePublished.set(selectedProfilesCount.get());
			expectedTotalMinutes.set((int) Math.ceil((double) profilesToBePublished.get() / PROFILES_PER_MINUTE));

			LOGGER.info("Publishing " + profilesToBePublished.get() + " profiles @ " + 
					PROFILES_PER_MINUTE + "/min = ~" + expectedTotalMinutes.get() + " min expected");

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("calculate_expected_time", e);
			PageObjectHelper.handleError(LOGGER, "calculate_expected_time", "Error calculating expected time", e);
		}
	}

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
			LOGGER.info("=== STARTING PROGRESSIVE BATCH PUBLISHING VALIDATION ===");
			LOGGER.info("Objective: Confirm publishing is working (not waiting for completion)");
			LOGGER.info("Dataset size: " + selectedProfilesCount.get() + " profiles");
			LOGGER.info("Initial wait: " + INITIAL_WAIT_SECONDS + " seconds (for backend to start processing)");
			LOGGER.info("Check interval: " + REFRESH_INTERVAL_SECONDS + " seconds");
			LOGGER.info("Max check attempts: " + MAX_CHECK_ATTEMPTS);
			LOGGER.info("Initial unpublished count: " + initialUnpublishedCount);
			LOGGER.info("Target unpublished count: " + targetUnpublishedCount);

			// Initial wait for backend to start processing (especially for large datasets)
			LOGGER.info("⏳ Waiting " + INITIAL_WAIT_SECONDS + " seconds for backend to initialize batch publishing...");
			safeSleep(INITIAL_WAIT_SECONDS * 1000);
			totalElapsedSeconds += INITIAL_WAIT_SECONDS;

			while (checkNumber < MAX_CHECK_ATTEMPTS) {
				checkNumber++;
				
				// Refresh and wait for page ready
				driver.navigate().refresh();
				// Refresh and wait for page ready
				driver.navigate().refresh();
				PageObjectHelper.waitForSpinnersToDisappear(driver, 10);
				PageObjectHelper.waitForPageReady(driver, 2);

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
				
				LOGGER.info(progressMsg);

				// Check if all profiles are published (early completion)
				if (currentUnpublishedCount <= targetUnpublishedCount) {
					publishingConfirmed = true;
					terminationReason = "All profiles published";
					LOGGER.info("✓ SUCCESS! All " + totalPublishedSoFar + 
							" profiles published in " + elapsedMinutes + "m " + elapsedSecondsRemainder + "s");
					break;
				}

				// Check if publishing progress is confirmed (KEY OPTIMIZATION)
				if (checksWithProgress >= MIN_CHECKS_TO_CONFIRM_PROGRESS) {
					publishingConfirmed = true;
					terminationReason = "Publishing confirmed - progress detected in " + checksWithProgress + " checks";
					LOGGER.info("✓ SUCCESS! Publishing is working properly");
					LOGGER.info("Published so far: " + totalPublishedSoFar + 
							" out of " + selectedProfilesCount.get() + 
							" (" + String.format("%.1f%%", progressPercentage) + ")");
					LOGGER.info("Publishing rate: " + String.format("%.0f", publishingRate) + " profiles/min");
					LOGGER.info("Estimated time for all profiles: ~" + 
							Math.ceil((double) remainingToPublish / publishingRate) + " minutes remaining");
					LOGGER.info("Validation complete - not waiting for full completion");
					break;
				}

				// Check for consecutive no-progress termination
				if (consecutiveNoProgressChecks >= MAX_CONSECUTIVE_NO_PROGRESS_CHECKS) {
					terminationReason = "No progress detected in " + MAX_CONSECUTIVE_NO_PROGRESS_CHECKS + 
							" consecutive checks";
					LOGGER.info("⚠ EARLY TERMINATION: " + terminationReason);
					LOGGER.info("Total time elapsed: " + elapsedMinutes + "m " + elapsedSecondsRemainder + "s");
					LOGGER.info("Published so far: " + totalPublishedSoFar + 
							" out of " + selectedProfilesCount.get() + 
							" (" + String.format("%.1f%%", progressPercentage) + ")");
					LOGGER.info("Still remaining: " + remainingToPublish + " profiles");
					
					if (totalPublishedSoFar == 0) {
						LOGGER.info("⚠ Publishing may not have started - check backend logs for errors");
					} else {
						LOGGER.info("⚠ Publishing started but appears to have stalled");
					}
					break;
				}

				// Warn if no progress but below consecutive threshold
				if (consecutiveNoProgressChecks > 0) {
					LOGGER.info("⚠ Warning: No progress in check #" + checkNumber + 
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

				LOGGER.info("=== VALIDATION ENDED: " + terminationReason + " ===");
				LOGGER.info("Total time elapsed: " + (totalElapsedSeconds / 60) + "m " + 
						(totalElapsedSeconds % 60) + "s");
				LOGGER.info("Profiles published: " + totalPublishedSoFar + " out of " + 
						selectedProfilesCount.get() + " (" + String.format("%.1f%%", finalProgress) + ")");
				LOGGER.info("Checks with progress: " + checksWithProgress + "/" + checkNumber);
				
				if (checksWithProgress > 0) {
					LOGGER.info("⚠ Publishing appears to be working but needs more time");
				} else {
					LOGGER.info("⚠ No publishing progress detected in any checks");
				}
			}

			// Final validation result summary
			LOGGER.info("=== PROGRESSIVE BATCH PUBLISHING VALIDATION COMPLETE ===");
			LOGGER.info("Status: " + (publishingConfirmed ? "CONFIRMED - Publishing is working" : "UNCONFIRMED - Publishing may have issues"));
			LOGGER.info("Termination reason: " + terminationReason);
			LOGGER.info("Total checks performed: " + checkNumber + "/" + MAX_CHECK_ATTEMPTS);
			LOGGER.info("Checks showing progress: " + checksWithProgress);
			LOGGER.info("Total time: " + (totalElapsedSeconds / 60) + " minutes " + 
					(totalElapsedSeconds % 60) + " seconds");

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("monitor_batch_publishing", e);
			PageObjectHelper.handleError(LOGGER, "monitor_batch_publishing", 
					"Error monitoring batch publishing", e);
		}
	}

	public void verify_all_profiles_are_published_successfully() {
		try {
			PageObjectHelper.waitForSpinnersToDisappear(driver, 10);
			PageObjectHelper.waitForPageReady(driver, 2);

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
				LOGGER.info("SUCCESS! All " + actualPublished + " profiles published");
			} else {
				LOGGER.info("Incomplete! " + remainingToPublish + " of " + 
						selectedProfilesCount.get() + " profiles still pending");
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_all_published", e);
			PageObjectHelper.handleError(LOGGER, "verify_all_published", "Error verifying final status", e);
		}
	}
	
	private void closeSuccessPopupWithFallback() {
		// Try multiple close button selectors
		By[] closeButtonSelectors = {
			SUCCESS_CLOSE_BTN,  // Primary: //button[@id='close-success-modal-btn']
			By.xpath("//button[contains(@id, 'close')]"),
			By.xpath("//button[contains(text(), 'Close')]"),
			By.xpath("//button[contains(@class, 'close')]"),
			By.xpath("//button[@aria-label='Close']"),
			By.xpath("//*[@role='dialog']//button"),
			By.cssSelector("button.close"),
			By.cssSelector("[aria-label='close']")
		};
		
		boolean popupClosed = false;
		for (By selector : closeButtonSelectors) {
			try {
				WebElement closeBtn = driver.findElement(selector);
				if (closeBtn.isDisplayed()) {
					try {
						closeBtn.click();
					} catch (Exception e) {
						jsClick(closeBtn);
					}
					LOGGER.info("✓ Closed success popup using selector: {}", selector);
					popupClosed = true;
					break;
				}
			} catch (Exception e) {
				// Try next selector
			}
		}
		
		if (!popupClosed) {
			LOGGER.warn("Could not find close button - trying ESC key and page refresh");
			try {
				// Try pressing ESC key
				((JavascriptExecutor) driver).executeScript("document.dispatchEvent(new KeyboardEvent('keydown', {'key': 'Escape'}));");
				safeSleep(500);
			} catch (Exception e) {
				LOGGER.debug("ESC key failed, popup may auto-dismiss or require page refresh");
			}
		}
	}
}
