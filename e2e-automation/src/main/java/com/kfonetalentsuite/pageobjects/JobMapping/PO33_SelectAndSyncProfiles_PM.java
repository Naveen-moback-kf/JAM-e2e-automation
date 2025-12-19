package com.kfonetalentsuite.pageobjects.JobMapping;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.ScreenshotHandler;
import com.kfonetalentsuite.utils.JobMapping.PageObjectHelper;

/**
 * Consolidated Page Object for Select and Sync Profiles in HCM Sync Profiles (PM) screen.
 * 
 * Supports two selection methods:
 * 1. Header Checkbox - Selects only LOADED profiles (lazy loading aware)
 * 2. Select All (Chevron + Select All) - Selects ALL profiles including those not yet loaded
 * 
 * Merged from: PO33_ValidateSelectAndHCMSyncLoadedProfiles_PM and PO34_ValidateSelectAndSyncAllProfiles_PM
 */
public class PO33_SelectAndSyncProfiles_PM extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO33_SelectAndSyncProfiles_PM.class);

	// Static variable for storing count of profiles selected to export
	public static int CountOfProfilesSelectedToExport;

	// Locators - Uses centralized locators from BasePageObject.Locators where available
	private static final By ALL_CHECKBOXES = By.xpath("//tbody//tr//td[1]//div[1]//kf-checkbox");
	private static final By ALL_ROWS = By.xpath("//tbody//tr");

	public PO33_SelectAndSyncProfiles_PM() {
		super();
	}

	// =====================================================
	// FLOW 1: HEADER CHECKBOX SELECTION (Loaded Profiles)
	// =====================================================

	/**
	 * Verifies that profiles loaded after clicking the header checkbox are NOT selected.
	 * 
	 * Expected Flow:
	 * 1. Loaded Profiles on screen (BEFORE Header checkbox is clicked): 100 profiles
	 * 2. Header checkbox is clicked - Selects 61 profiles (39 are disabled, cannot be selected)
	 * 3. User scrolls - Loads 50 MORE profiles (Total now = 150)
	 * 4. These 50 newly loaded profiles should NOT be selected
	 * 
	 * This validates that the "header checkbox select" only selects currently
	 * LOADED profiles, not profiles that load later via lazy loading/scrolling.
	 */
	public void verify_profiles_loaded_after_clicking_header_checkbox_are_not_selected_in_HCM_Sync_Profiles_screen() {
		int loadedProfilesBeforeHeaderCheckboxClick = PO22_ValidateHCMSyncProfilesScreen_PM.loadedProfilesBeforeHeaderCheckboxClick.get();
		int selectedProfilesAfterHeaderCheckboxClick = PO22_ValidateHCMSyncProfilesScreen_PM.selectedProfilesAfterHeaderCheckboxClick.get();
		int disabledProfilesInLoadedProfiles = PO22_ValidateHCMSyncProfilesScreen_PM.disabledProfilesCountInLoadedProfiles.get();
		int totalProfilesCurrentlyLoaded = 0;
		int newlyLoadedProfilesAfterScrolling = 0;
		int unselectedProfilesCount = 0;
		int disabledInNewlyLoadedProfiles = 0;

		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(Locators.Spinners.PAGE_LOAD_SPINNER));
			PerformanceUtils.waitForPageReady(driver, 2);

			// Step 1: Count TOTAL profiles currently in DOM (includes newly loaded after scrolling)
			totalProfilesCurrentlyLoaded = findElements(ALL_CHECKBOXES).size();

			LOGGER.info("Total Profiles Currently loaded on screen: " + totalProfilesCurrentlyLoaded);

			// Step 2: Calculate newly loaded profiles
			newlyLoadedProfilesAfterScrolling = totalProfilesCurrentlyLoaded - loadedProfilesBeforeHeaderCheckboxClick;

			if (newlyLoadedProfilesAfterScrolling <= 0) {
				PageObjectHelper.log(LOGGER, "No newly loaded profiles to verify (Before: " + loadedProfilesBeforeHeaderCheckboxClick + 
						", Selected: " + selectedProfilesAfterHeaderCheckboxClick + 
						", Disabled: " + disabledProfilesInLoadedProfiles + 
						", Total Now: " + totalProfilesCurrentlyLoaded + ")");
				return;
			}

			PageObjectHelper.log(LOGGER, "Profile Summary - Before: " + loadedProfilesBeforeHeaderCheckboxClick + 
					", Selected: " + selectedProfilesAfterHeaderCheckboxClick + 
					", Disabled: " + disabledProfilesInLoadedProfiles + 
					", Total Now: " + totalProfilesCurrentlyLoaded + 
					", Newly Loaded: " + newlyLoadedProfilesAfterScrolling);
			PageObjectHelper.log(LOGGER, "Verifying " + newlyLoadedProfilesAfterScrolling + " newly loaded profiles are NOT selected...");

			// Step 3: Verify that newly loaded profiles are NOT selected
			for (int i = loadedProfilesBeforeHeaderCheckboxClick + 1; i <= totalProfilesCurrentlyLoaded; i++) {
				try {
					WebElement checkbox = driver.findElement(By.xpath("//tbody//tr[" + i + "]//td[1]//div[1]//kf-checkbox//div"));

					// Scroll to element to ensure it's visible
					js.executeScript("arguments[0].scrollIntoView(true);", checkbox);

					// Check checkbox class attributes
					String classAttribute = checkbox.getAttribute("class");
					boolean isSelected = classAttribute != null
							&& (classAttribute.contains("selected") || classAttribute.contains("checked"));
					boolean isDisabled = classAttribute != null && classAttribute.contains("disable");

					// Count disabled profiles in newly loaded profiles
					if (isDisabled) {
						disabledInNewlyLoadedProfiles++;
						LOGGER.debug("Newly loaded profile at position " + i + " is disabled (no job code)");
						continue;
					}

					// Verify that newly loaded profile is NOT selected
					if (!isSelected) {
						unselectedProfilesCount++;
					} else {
						LOGGER.warn(" Newly loaded profile at position " + i + " is SELECTED (should be unselected!)");
					}

				} catch (Exception e) {
					LOGGER.debug("Could not verify profile at position " + i + ": " + e.getMessage());
					continue;
				}
			}

			// Step 4: Validate results
			LOGGER.info("========================================");
			LOGGER.info("VALIDATION RESULTS");
			LOGGER.info("========================================");
			LOGGER.info("Newly loaded profiles (after scrolling): " + newlyLoadedProfilesAfterScrolling);
			LOGGER.info("Disabled profiles in newly loaded: " + disabledInNewlyLoadedProfiles);
			LOGGER.info("Unselected Profiles (From newly loaded profiles): " + unselectedProfilesCount);
			LOGGER.info("Expected unselected: " + (newlyLoadedProfilesAfterScrolling - disabledInNewlyLoadedProfiles));
			LOGGER.info("========================================");

			// Validation logic: All newly loaded profiles (excluding disabled) should be unselected
			int expectedUnselected = newlyLoadedProfilesAfterScrolling - disabledInNewlyLoadedProfiles;

			if (unselectedProfilesCount >= expectedUnselected) {
				PageObjectHelper.log(LOGGER, "✅ Validation PASSED: " + unselectedProfilesCount
						+ " newly loaded profiles are NOT selected (as expected)");
			} else {
				int missingUnselected = expectedUnselected - unselectedProfilesCount;
				PageObjectHelper.log(LOGGER, "❌ Validation FAILED: " + missingUnselected
						+ " newly loaded profiles are unexpectedly selected");
				Assert.fail("Validation FAILED: " + missingUnselected
						+ " newly loaded profiles are selected (should be unselected)");
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot(
					"verify_profiles_loaded_after_clicking_header_checkbox_are_not_selected_in_HCM_Sync_Profiles_screen", e);
			PageObjectHelper.handleError(LOGGER, 
					"verify_profiles_loaded_after_clicking_header_checkbox_are_not_selected_in_HCM_Sync_Profiles_screen",
					"Error verifying newly loaded profiles are not selected in HCM Sync Profiles Screen", e);
			Assert.fail("Error verifying newly loaded profiles are not selected: " + e.getMessage());
		}
	}

	// =====================================================
	// FLOW 2: SELECT ALL (Chevron + Select All Button)
	// =====================================================

	/**
	 * Clicks on Chevron Button beside Header Checkbox in HCM Sync Profiles screen.
	 */
	public void click_on_chevron_button_beside_header_checkbox_in_hcm_sync_profiles_screen() {
		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(Locators.Spinners.PAGE_LOAD_SPINNER));
			
			try {
				wait.until(ExpectedConditions.elementToBeClickable(Locators.PMScreen.CHEVRON_BUTTON)).click();
			} catch (Exception e) {
				try {
					jsClick(findElement(Locators.PMScreen.CHEVRON_BUTTON));
				} catch (Exception s) {
					clickElement(Locators.PMScreen.CHEVRON_BUTTON);
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
				WebElement countElement = findElement(Locators.HCMSyncProfiles.SHOWING_RESULTS_COUNT);
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

			// Step 2: Scroll to load all profiles
			PageObjectHelper.log(LOGGER, "Loading all profiles by scrolling...");

			// DYNAMIC maxScrollAttempts calculation based on total profiles
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
			LOGGER.info("Counting selected/disabled checkboxes...");
			
			try {
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
			PageObjectHelper.log(LOGGER, "✅ Selected (to be synced): " + selectedCount);
			PageObjectHelper.log(LOGGER, "⬜ Unselected (enabled but not selected): " + unselectedProfiles);
			PageObjectHelper.log(LOGGER, "🚫 Disabled (cannot be selected): " + disabledCount);

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

