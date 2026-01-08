package com.kfonetalentsuite.pageobjects.JobMapping;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import com.kfonetalentsuite.utils.JobMapping.ScreenshotHandler;
import com.kfonetalentsuite.utils.JobMapping.PageObjectHelper;

public class PO29_SelectAndPublishLoadedProfiles_JAM extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO29_SelectAndPublishLoadedProfiles_JAM.class);

	// Locators
	private static final By ALL_CHECKBOXES = By.xpath("//tbody//tr//td[1][contains(@class,'whitespace')]//input");

	public PO29_SelectAndPublishLoadedProfiles_JAM() {
		super();
	}

	public void verify_profiles_loaded_after_clicking_header_checkbox_are_not_selected_in_job_mapping_screen() {
		int loadedProfilesBeforeHeaderCheckboxClick = PO04_JobMappingPageComponents.loadedProfilesBeforeHeaderCheckboxClick.get();
		int selectedProfilesAfterHeaderCheckboxClick = PO04_JobMappingPageComponents.selectedProfilesAfterHeaderCheckboxClick.get();
		int disabledProfilesInLoadedProfiles = PO04_JobMappingPageComponents.disabledProfilesCountInLoadedProfiles.get();
		int totalProfilesCurrentlyLoaded = 0;
		int newlyLoadedProfilesAfterScrolling = 0;
		int unselectedProfilesCount = 0;
		int disabledInNewlyLoadedProfiles = 0;

		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(Locators.Spinners.DATA_LOADER));
			PageObjectHelper.waitForPageReady(driver, 2);

			// Step 1: Count TOTAL profiles currently in DOM (includes newly loaded after scrolling)
			totalProfilesCurrentlyLoaded = findElements(ALL_CHECKBOXES).size();

			LOGGER.info("Total Profiles Currently loaded on screen: " + totalProfilesCurrentlyLoaded);

			// Step 2: Calculate newly loaded profiles
			newlyLoadedProfilesAfterScrolling = totalProfilesCurrentlyLoaded - loadedProfilesBeforeHeaderCheckboxClick;

			if (newlyLoadedProfilesAfterScrolling <= 0) {
				LOGGER.info("No newly loaded profiles to verify (Before: " + loadedProfilesBeforeHeaderCheckboxClick + 
						", Selected: " + selectedProfilesAfterHeaderCheckboxClick + 
						", Disabled: " + disabledProfilesInLoadedProfiles + 
						", Total Now: " + totalProfilesCurrentlyLoaded + ")");
				return;
			}

			LOGGER.info("Profile Summary - Before: " + loadedProfilesBeforeHeaderCheckboxClick + 
					", Selected: " + selectedProfilesAfterHeaderCheckboxClick + 
					", Disabled: " + disabledProfilesInLoadedProfiles + 
					", Total Now: " + totalProfilesCurrentlyLoaded + 
					", Newly Loaded: " + newlyLoadedProfilesAfterScrolling);
			LOGGER.info("Verifying " + newlyLoadedProfilesAfterScrolling + " newly loaded profiles are NOT selected...");

			// Step 3: Get all checkboxes and verify newly loaded ones are NOT selected
			// Get all checkbox elements directly (not by row position, as some rows don't have checkboxes)
			List<WebElement> allCheckboxes = findElements(ALL_CHECKBOXES);

			// Verify checkboxes from index loadedProfilesBeforeHeaderCheckboxClick onwards (newly loaded profiles)
			for (int i = loadedProfilesBeforeHeaderCheckboxClick; i < allCheckboxes.size(); i++) {
				try {
					WebElement checkbox = allCheckboxes.get(i);

					// Scroll to element to ensure it's visible
					js.executeScript("arguments[0].scrollIntoView(true);", checkbox);

					// Check if checkbox is disabled
					boolean isDisabled = !checkbox.isEnabled();

					// Count disabled profiles in newly loaded profiles
					if (isDisabled) {
						disabledInNewlyLoadedProfiles++;
						LOGGER.debug("Newly loaded profile at index " + (i + 1) + " is disabled");
						continue;
					}

					// Check if profile is selected by examining the parent <tr> class
					// Selected: tr class = "bg-blue-50 h-24"
					// Unselected: tr class = "bg-white h-24"
					WebElement parentTr = checkbox.findElement(By.xpath("./ancestor::tr"));
					String trClass = parentTr.getAttribute("class");
					boolean isSelected = trClass != null && trClass.contains("bg-blue-50");

					// Verify that newly loaded profile is NOT selected
					if (!isSelected) {
						unselectedProfilesCount++;
					} else {
						LOGGER.warn(" Newly loaded profile at index " + (i + 1) + " is SELECTED (tr class: " + trClass
								+ ", should be unselected!)");
					}

				} catch (Exception e) {
					LOGGER.debug("Could not verify profile at index " + (i + 1) + ": " + e.getMessage());
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
				LOGGER.info("Validation PASSED: " + unselectedProfilesCount
						+ " newly loaded profiles are NOT selected (as expected)");
			} else {
				int missingUnselected = expectedUnselected - unselectedProfilesCount;
				LOGGER.info("Validation FAILED: " + missingUnselected
						+ " newly loaded profiles are unexpectedly selected");
				Assert.fail("Validation FAILED: " + missingUnselected
						+ " newly loaded profiles are selected (should be unselected)");
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot(
					"verify_profiles_loaded_after_clicking_header_checkbox_are_not_selected_in_job_mapping_screen", e);
			PageObjectHelper.handleError(LOGGER,
					"verify_profiles_loaded_after_clicking_header_checkbox_are_not_selected_in_job_mapping_screen",
					"Error verifying newly loaded profiles are not selected", e);
			Assert.fail("Error verifying newly loaded profiles are not selected: " + e.getMessage());
		}
	}
}
