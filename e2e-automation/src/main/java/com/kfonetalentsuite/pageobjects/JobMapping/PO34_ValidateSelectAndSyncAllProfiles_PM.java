package com.kfonetalentsuite.pageobjects.JobMapping;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.ScreenshotHandler;
import com.kfonetalentsuite.utils.JobMapping.PageObjectHelper;

/**
 * Page Object for validating Select All and Sync All profiles in HCM Sync Profiles (PM) screen.
 * Handles clicking chevron/Select All buttons and scrolling through all profiles to count selected.
 */
public class PO34_ValidateSelectAndSyncAllProfiles_PM extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO34_ValidateSelectAndSyncAllProfiles_PM.class);

	public static int CountOfProfilesSelectedToExport;

	// Locators - Uses centralized locators from BasePageObject.Locators where available
	// PAGE_LOAD_SPINNER is available via Locators.Spinners.PAGE_LOAD_SPINNER
	// HCM_SELECT_ALL_BTN is available via Locators.Actions.SELECT_ALL_BTN
	// Using centralized Locators from BasePageObject where applicable
	private static final By HCM_CHEVRON_BTN = Locators.PMScreen.CHEVRON_BUTTON;
	private static final By SHOWING_COUNT = Locators.HCMSyncProfiles.SHOWING_RESULTS_COUNT;
	private static final By ALL_ROWS = By.xpath("//tbody//tr");

	public PO34_ValidateSelectAndSyncAllProfiles_PM() {
		super();
	}

	/**
	 * Clicks on Chevron Button beside Header Checkbox in HCM Sync Profiles screen.
	 */
	public void click_on_chevron_button_beside_header_checkbox_in_hcm_sync_profiles_screen() {
		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(Locators.Spinners.PAGE_LOAD_SPINNER));
			
			try {
				wait.until(ExpectedConditions.elementToBeClickable(HCM_CHEVRON_BTN)).click();
			} catch (Exception e) {
				try {
					jsClick(findElement(HCM_CHEVRON_BTN));
				} catch (Exception s) {
					clickElement(HCM_CHEVRON_BTN);
				}
			}
			
			PageObjectHelper.log(LOGGER, "Clicked on Chevron Button beside Header Checkbox in HCM Sync Profiles Screen");
			PerformanceUtils.waitForPageReady(driver, 2);
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot(
					"click_on_chevron_button_beside_header_checkbox_in_hcm_sync_profiles_screen", e);
			PageObjectHelper.handleError(LOGGER,
					"click_on_chevron_button_beside_header_checkbox_in_hcm_sync_profiles_screen",
					"Issue in clicking Chevron Button beside Header Checkbox in HCM Sync Profiles Screen", e);
		}
	}

	/**
	 * Clicks on Select All button in HCM Sync Profiles screen.
	 * The dropdown appears immediately after chevron click without page load spinner.
	 */
	public void click_on_select_all_button_in_hcm_sync_profiles_screen() {
		try {
			// Wait directly for Select All button to be clickable (dropdown appears immediately after chevron click)
			// No need to wait for spinner as dropdown shows without page reload
			try {
				wait.until(ExpectedConditions.elementToBeClickable(Locators.Table.SELECT_ALL_BTN)).click();
			} catch (Exception e) {
				try {
					jsClick(findElement(Locators.Table.SELECT_ALL_BTN));
				} catch (Exception s) {
					clickElement(Locators.Table.SELECT_ALL_BTN);
				}
			}
			
			PageObjectHelper.log(LOGGER, "Clicked on Select All button in HCM Sync Profiles Screen");
			PerformanceUtils.waitForPageReady(driver, 2);
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_on_select_all_button_in_hcm_sync_profiles_screen", e);
			PageObjectHelper.handleError(LOGGER, "click_on_select_all_button_in_hcm_sync_profiles_screen",
					"Issue in clicking Select All button in HCM Sync Profiles Screen", e);
		}
	}

	/**
	 * Scrolls through all profiles and counts the number of selected profiles based on checkbox state.
	 * This method handles lazy loading by scrolling to the bottom with increased wait times.
	 * 
	 * Process:
	 * 1. Get total profile count from "Showing X of Y" text
	 * 2. Scroll to bottom repeatedly with longer waits to load all profiles
	 * 3. Count checkboxes that are enabled (excluding disabled ones)
	 * 
	 * Example: If 2842 total profiles exist and 42 are disabled, this returns 2800
	 */
	public void verify_count_of_selected_profiles_by_scrolling_through_all_profiles() {
		int selectedCount = 0;
		int totalProfiles = 0;
		int disabledCount = 0;

		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(Locators.Spinners.PAGE_LOAD_SPINNER));
			PerformanceUtils.waitForPageReady(driver, 2);

			// Step 1: Get total profile count from "Showing X of Y Success Profiles" text
			try {
				WebElement countElement = findElement(SHOWING_COUNT);
				String countText = countElement.getText().trim();

				// Parse text like "Showing 50 of 2842 Success Profiles"
				if (countText.contains("of")) {
					String afterOf = countText.substring(countText.indexOf("of") + 2).trim();
					String totalCountStr = afterOf.split("\\s+")[0].replaceAll("[^0-9]", "");

					if (!totalCountStr.isEmpty()) {
						totalProfiles = Integer.parseInt(totalCountStr);
						PageObjectHelper.log(LOGGER, "Total profiles to process: " + totalProfiles);
					}
				}
			} catch (Exception e) {
				LOGGER.warn("Could not extract total profile count from text: " + e.getMessage());
			}

			if (totalProfiles == 0) {
				LOGGER.warn("Could not determine total profile count. Will scroll until no more data loads.");
				PageObjectHelper.log(LOGGER, "Could not determine total profile count. Scrolling through all available profiles...");
			}

			// Step 2: Scroll to load all profiles (ENHANCED FOR HEADLESS MODE)
			PageObjectHelper.log(LOGGER, "Loading all profiles by scrolling...");

			// DYNAMIC maxScrollAttempts calculation based on total profiles
			// ~50 profiles load per scroll, add small buffer for safety
			int maxScrollAttempts;
			if (totalProfiles > 0) {
				int estimatedScrolls = (int) Math.ceil(totalProfiles / 50.0);
				maxScrollAttempts = estimatedScrolls + 10; // Add 10 scrolls buffer
				maxScrollAttempts = Math.max(15, Math.min(100, maxScrollAttempts));

				LOGGER.info("maxScrollAttempts: {} (estimated {} scrolls for {} profiles + 10 buffer)", 
						maxScrollAttempts, estimatedScrolls, totalProfiles);
			} else {
				maxScrollAttempts = 60;
				LOGGER.info("Using default maxScrollAttempts: {} (total profile count unavailable)", maxScrollAttempts);
			}

			int currentRowCount = 0;
			int previousRowCount = 0;
			int noChangeCount = 0;
			int scrollCount = 0;
			boolean reachedMaxScrollLimit = false;

			while (scrollCount < maxScrollAttempts) {
				try {
					js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
				} catch (Exception e1) {
					try {
						js.executeScript("window.scrollTo(0, document.documentElement.scrollHeight);");
					} catch (Exception e2) {
						js.executeScript("window.scrollBy(0, 10000);");
					}
				}

				scrollCount++;
				safeSleep(3000);
				PerformanceUtils.waitForSpinnersToDisappear(driver, 5);
				PerformanceUtils.waitForPageReady(driver, 2);
				safeSleep(1000);

				currentRowCount = findElements(ALL_ROWS).size();

				// Log progress every 10 scrolls or when new data loads
				if (scrollCount % 10 == 0 || currentRowCount != previousRowCount) {
					int percentComplete = totalProfiles > 0 ? (currentRowCount * 100 / totalProfiles) : 0;
					LOGGER.info("Scroll {}/{}: Loaded {} profiles ({}% complete)", 
							scrollCount, maxScrollAttempts, currentRowCount, percentComplete);
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

			if (scrollCount >= maxScrollAttempts && noChangeCount < 3) {
				reachedMaxScrollLimit = true;
				LOGGER.warn("REACHED MAX SCROLL LIMIT: Stopped at {} scrolls.", scrollCount);
			}

			LOGGER.info("Scrolling complete. Total rows loaded: {}, Scrolls performed: {}", currentRowCount, scrollCount);

			// Step 3: Count checkboxes by state using JavaScript (FAST)
			// In HCM/PM screens:
			// - Selected: kf-icon[@icon='checkbox-check'] is ONLY present in DOM when checked
			// - Disabled: kf-checkbox has div with 'disable' class
			
			LOGGER.info("Counting selected/disabled checkboxes...");
			
			try {
				// JavaScript counting is MUCH faster than XPath findElements for large tables
				String jsCountSelected = "return document.querySelectorAll('tbody tr td:first-child kf-checkbox kf-icon[icon=\"checkbox-check\"]').length;";
				String jsCountDisabled = "return document.querySelectorAll('tbody tr td:first-child kf-checkbox div.disable').length;";
				
				Object selectedResult = js.executeScript(jsCountSelected);
				selectedCount = ((Long) selectedResult).intValue();
				
				Object disabledResult = js.executeScript(jsCountDisabled);
				disabledCount = ((Long) disabledResult).intValue();
				
				int unselectedCount = currentRowCount - selectedCount - disabledCount;
				LOGGER.info("Counted - Total: {}, Selected: {}, Disabled: {}, Unselected: {}", 
						currentRowCount, selectedCount, disabledCount, unselectedCount);

			} catch (Exception countException) {
				LOGGER.warn("JavaScript counting failed, using XPath fallback: {}", countException.getMessage());
				// Fallback to XPath (slower but reliable)
				selectedCount = driver.findElements(By.xpath(
						"//tbody//tr//td[1]//kf-checkbox//kf-icon[@icon='checkbox-check']")).size();
				disabledCount = driver.findElements(By.xpath(
						"//tbody//tr//td[1]//kf-checkbox//div[contains(@class, 'disable')]")).size();
				LOGGER.info("Fallback Counted - Selected: {}, Disabled: {}", selectedCount, disabledCount);
			}

			// Calculate unselected count for final summary
			totalProfiles = currentRowCount;
			int unselectedProfiles = totalProfiles - selectedCount - disabledCount;
			
			PageObjectHelper.log(LOGGER, "=== PROFILE COUNT SUMMARY ===");
			PageObjectHelper.log(LOGGER, "Total Profiles: " + totalProfiles);
			PageObjectHelper.log(LOGGER, " Selected (to be synced): " + selectedCount);
			PageObjectHelper.log(LOGGER, " Unselected (enabled but not selected): " + unselectedProfiles);
			PageObjectHelper.log(LOGGER, " Disabled (cannot be selected): " + disabledCount);

			CountOfProfilesSelectedToExport = selectedCount;
			PO22_ValidateHCMSyncProfilesScreen_PM.profilesCount.set(CountOfProfilesSelectedToExport);
			PO22_ValidateHCMSyncProfilesScreen_PM.isProfilesCountComplete.set(!reachedMaxScrollLimit);

			if (reachedMaxScrollLimit) {
				PageObjectHelper.log(LOGGER, "Count Status: INCOMPLETE (max scroll limit reached)");
			} else {
				PageObjectHelper.log(LOGGER, "Count Status: COMPLETE (all profiles loaded and counted)");
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_count_of_selected_profiles_by_scrolling_through_all_profiles", e);
			PageObjectHelper.handleError(LOGGER, "verify_count_of_selected_profiles_by_scrolling_through_all_profiles",
					"Error getting selected profiles count in HCM Sync Profiles screen", e);
		}
	}
}
