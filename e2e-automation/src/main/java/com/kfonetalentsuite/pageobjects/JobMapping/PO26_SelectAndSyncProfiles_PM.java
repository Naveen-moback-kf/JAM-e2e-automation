package com.kfonetalentsuite.pageobjects.JobMapping;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import com.kfonetalentsuite.utils.JobMapping.ScreenshotHandler;
import com.kfonetalentsuite.utils.JobMapping.PageObjectHelper;

public class PO26_SelectAndSyncProfiles_PM extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO26_SelectAndSyncProfiles_PM.class);

	// Static variable for storing count of profiles selected to export
	public static ThreadLocal<Integer> CountOfProfilesSelectedToExport = ThreadLocal.withInitial(() -> 0);

	// Locators - Uses centralized locators from BasePageObject.Locators where available
	private static final By ALL_CHECKBOXES = By.xpath("//tbody//tr//td[1]//div[1]//kf-checkbox");
	private static final By ALL_ROWS = By.xpath("//tbody//tr");

	public PO26_SelectAndSyncProfiles_PM() {
		super();
	}
	// FLOW 1: HEADER CHECKBOX SELECTION (Loaded Profiles)

	public void verify_profiles_loaded_after_clicking_header_checkbox_are_not_selected_in_HCM_Sync_Profiles_screen() {
		int loadedProfilesBeforeHeaderCheckboxClick = PO18_HCMSyncProfilesTab_PM.loadedProfilesBeforeHeaderCheckboxClick.get();
		int selectedProfilesAfterHeaderCheckboxClick = PO18_HCMSyncProfilesTab_PM.selectedProfilesAfterHeaderCheckboxClick.get();
		int disabledProfilesInLoadedProfiles = PO18_HCMSyncProfilesTab_PM.disabledProfilesCountInLoadedProfiles.get();
		int totalProfilesCurrentlyLoaded = 0;
		int newlyLoadedProfilesAfterScrolling = 0;
		int unselectedProfilesCount = 0;
		int disabledInNewlyLoadedProfiles = 0;

		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(Locators.Spinners.PAGE_LOAD_SPINNER));
			PageObjectHelper.waitForPageReady(driver, 2);

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
	// FLOW 2: SELECT ALL (Chevron + Select All Button)

	public void click_on_chevron_button_beside_header_checkbox_in_hcm_sync_profiles_screen() {
		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(Locators.Spinners.PAGE_LOAD_SPINNER));
			
			try {
				PageObjectHelper.waitForClickable(wait, Locators.PMScreen.CHEVRON_BUTTON).click();
			} catch (Exception e) {
				try {
					jsClick(findElement(Locators.PMScreen.CHEVRON_BUTTON));
				} catch (Exception s) {
					clickElement(Locators.PMScreen.CHEVRON_BUTTON);
				}
			}
			
			PageObjectHelper.log(LOGGER, "Clicked on Chevron Button beside Header Checkbox in HCM Sync Profiles Screen");
			PageObjectHelper.waitForPageReady(driver, 2);
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot(
					"click_on_chevron_button_beside_header_checkbox_in_hcm_sync_profiles_screen", e);
			PageObjectHelper.handleError(LOGGER,
					"click_on_chevron_button_beside_header_checkbox_in_hcm_sync_profiles_screen",
					"Issue in clicking Chevron Button beside Header Checkbox in HCM Sync Profiles Screen", e);
		}
	}

	public void click_on_select_all_button_in_hcm_sync_profiles_screen() {
		try {
			// Wait directly for Select All button to be clickable (dropdown appears immediately after chevron click)
			try {
				PageObjectHelper.waitForClickable(wait, Locators.Table.SELECT_ALL_BTN).click();
			} catch (Exception e) {
				try {
					jsClick(findElement(Locators.Table.SELECT_ALL_BTN));
				} catch (Exception s) {
					clickElement(Locators.Table.SELECT_ALL_BTN);
				}
			}
			
			PageObjectHelper.log(LOGGER, "Clicked on Select All button in HCM Sync Profiles Screen");
			PageObjectHelper.waitForPageReady(driver, 2);
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_on_select_all_button_in_hcm_sync_profiles_screen", e);
			PageObjectHelper.handleError(LOGGER, "click_on_select_all_button_in_hcm_sync_profiles_screen",
					"Issue in clicking Select All button in HCM Sync Profiles Screen", e);
		}
	}

	public void verify_count_of_selected_profiles_by_scrolling_through_all_profiles() {
		int selectedCount = 0;
		int totalProfiles = 0;
		int disabledCount = 0;

		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(Locators.Spinners.PAGE_LOAD_SPINNER));
			PageObjectHelper.waitForPageReady(driver, 2);

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
				PageObjectHelper.waitForSpinnersToDisappear(driver, 5);
				PageObjectHelper.waitForPageReady(driver, 2);
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
						PageObjectHelper.waitForSpinnersToDisappear(driver, 5);
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
			// FIX: Count only ENABLED selected checkboxes (exclude disabled with check icons)
			LOGGER.info("Counting selected/disabled checkboxes...");
			
			try {
				// Count disabled checkboxes first
				String jsCountDisabled = "return document.querySelectorAll('tbody tr td:first-child kf-checkbox div.disable').length;";
				Object disabledResult = js.executeScript(jsCountDisabled);
				disabledCount = ((Long) disabledResult).intValue();
				
				// FIX: Count ONLY selected checkboxes that are NOT disabled
				// Some disabled checkboxes may still show a check icon but should not be counted as "selected for export"
				String jsCountEnabledSelected = 
					"return Array.from(document.querySelectorAll('tbody tr td:first-child kf-checkbox')).filter(cb => " +
					"  cb.querySelector('kf-icon[icon=\"checkbox-check\"]') && " +
					"  !cb.querySelector('div.disable')" +
					").length;";
				
				Object selectedResult = js.executeScript(jsCountEnabledSelected);
				selectedCount = ((Long) selectedResult).intValue();
				
				// Also count disabled checkboxes that show a check icon (for diagnostic purposes)
				String jsCountDisabledWithCheck = 
					"return Array.from(document.querySelectorAll('tbody tr td:first-child kf-checkbox')).filter(cb => " +
					"  cb.querySelector('kf-icon[icon=\"checkbox-check\"]') && " +
					"  cb.querySelector('div.disable')" +
					").length;";
				Object disabledWithCheckResult = js.executeScript(jsCountDisabledWithCheck);
				int disabledWithCheckCount = ((Long) disabledWithCheckResult).intValue();
				
				int unselectedCount = currentRowCount - selectedCount - disabledCount;
				LOGGER.info("Counted - Total: {}, Selected (enabled): {}, Disabled: {}, Unselected: {}", 
						currentRowCount, selectedCount, disabledCount, unselectedCount);
				
				if (disabledWithCheckCount > 0) {
					LOGGER.info("Note: {} disabled profiles show check icon (not counted as exportable)", disabledWithCheckCount);
				}

			} catch (Exception countException) {
				LOGGER.warn("JavaScript counting failed, using XPath fallback: {}", countException.getMessage());
				// Fallback to XPath - count selected checkboxes that are NOT disabled
				disabledCount = driver.findElements(By.xpath(
						"//tbody//tr//td[1]//kf-checkbox//div[contains(@class, 'disable')]")).size();
				// Count all checked, then subtract disabled-with-check
				int allChecked = driver.findElements(By.xpath(
						"//tbody//tr//td[1]//kf-checkbox//kf-icon[@icon='checkbox-check']")).size();
				int disabledWithCheck = driver.findElements(By.xpath(
						"//tbody//tr//td[1]//kf-checkbox[.//div[contains(@class, 'disable')]]//kf-icon[@icon='checkbox-check']")).size();
				selectedCount = allChecked - disabledWithCheck;
				LOGGER.info("Fallback Counted - Selected (enabled): {}, Disabled: {}, Disabled with check: {}", 
						selectedCount, disabledCount, disabledWithCheck);
			}

			// Calculate unselected count for final summary
			totalProfiles = currentRowCount;
			int unselectedProfiles = totalProfiles - selectedCount - disabledCount;
			
			PageObjectHelper.log(LOGGER, "=== PROFILE COUNT SUMMARY ===");
			PageObjectHelper.log(LOGGER, "Total Profiles: " + totalProfiles);
			PageObjectHelper.log(LOGGER, "✅ Selected (to be synced): " + selectedCount);
			PageObjectHelper.log(LOGGER, "⬜ Unselected (enabled but not selected): " + unselectedProfiles);
			PageObjectHelper.log(LOGGER, "🚫 Disabled (cannot be selected): " + disabledCount);

			CountOfProfilesSelectedToExport.set(selectedCount);
			PO18_HCMSyncProfilesTab_PM.profilesCount.set(CountOfProfilesSelectedToExport.get());
			PO18_HCMSyncProfilesTab_PM.isProfilesCountComplete.set(!reachedMaxScrollLimit);

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

