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
 * Page Object for clearing profile selection with None button in Job Mapping (JAM) screen.
 * Handles clicking None button and verifying all profiles are unselected.
 */
public class PO45_ClearProfileSelectionwithNoneButton_JAM extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO45_ClearProfileSelectionwithNoneButton_JAM.class);

	// Locators
	private static final By NONE_BTN = By.xpath("//*[contains(text(),'None')]");
	private static final By ALL_CHECKBOXES = By.xpath("//tbody//tr//td[1][contains(@class,'whitespace')]//input");

	public PO45_ClearProfileSelectionwithNoneButton_JAM() {
		super();
	}

	/**
	 * Clicks on None button to unselect all profiles in Job Mapping screen.
	 * This button is available in the dropdown that appears when clicking the chevron
	 * beside header checkbox. The dropdown appears immediately after chevron click
	 * without page load spinner.
	 */
	public void click_on_none_button_in_job_mapping_screen() {
		try {
			// Wait directly for None button to be clickable (dropdown appears immediately after chevron click)
			// No need to wait for spinner as dropdown shows without page reload
			try {
				wait.until(ExpectedConditions.elementToBeClickable(NONE_BTN)).click();
			} catch (Exception e) {
				try {
					jsClick(findElement(NONE_BTN));
				} catch (Exception s) {
					clickElement(NONE_BTN);
				}
			}

			// Wait for unselection to complete
			safeSleep(1000);
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

			PageObjectHelper.log(LOGGER, "Clicked on None button in Job Mapping screen");

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_on_none_button_in_job_mapping_screen", e);
			PageObjectHelper.handleError(LOGGER, "click_on_none_button_in_job_mapping_screen",
					"Error clicking on None button in Job Mapping screen", e);
			Assert.fail("Error clicking on None button in Job Mapping screen: " + e.getMessage());
		}
	}

	/**
	 * Verifies that all currently loaded profiles are unselected in Job Mapping screen.
	 * This method checks only the profiles already loaded on the page (no scrolling).
	 */
	public void verify_all_loaded_profiles_are_unselected_in_job_mapping_screen() {
		int totalProfilesChecked = 0;
		int selectedProfilesFound = 0;
		int disabledProfilesCount = 0;

		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

			LOGGER.info("========================================");
			LOGGER.info("VERIFYING ALL LOADED PROFILES ARE UNSELECTED");
			LOGGER.info("========================================");

			// Get all currently loaded checkboxes (no scrolling)
			var allCheckboxes = findElements(ALL_CHECKBOXES);

			int totalCheckboxes = allCheckboxes.size();
			LOGGER.info("Total checkboxes found on page: " + totalCheckboxes);

			// Check each checkbox's state
			for (int i = 0; i < allCheckboxes.size(); i++) {
				try {
					WebElement checkbox = allCheckboxes.get(i);

					boolean isSelected = checkbox.isSelected();
					boolean isDisabled = !checkbox.isEnabled();

					if (isDisabled) {
						disabledProfilesCount++;
						continue;
					}

					totalProfilesChecked++;

					if (isSelected) {
						selectedProfilesFound++;
						LOGGER.warn(" Profile at position " + (i + 1) + " is SELECTED (should be unselected!)");
					}

				} catch (Exception e) {
					LOGGER.debug("Could not verify profile at position " + (i + 1) + ": " + e.getMessage());
					continue;
				}
			}

			LOGGER.info("========================================");
			LOGGER.info("VALIDATION RESULTS");
			LOGGER.info("========================================");
			LOGGER.info("Total Profiles Checked: " + totalProfilesChecked);
			LOGGER.info("Disabled Profiles (skipped): " + disabledProfilesCount);
			LOGGER.info("Selected Profiles Found: " + selectedProfilesFound);
			LOGGER.info("========================================");

			if (selectedProfilesFound == 0) {
				PageObjectHelper.log(LOGGER, "Validation PASSED: All " + totalProfilesChecked + " profiles are unselected");
			} else {
				PageObjectHelper.log(LOGGER, "Validation FAILED: " + selectedProfilesFound + " profiles are still selected");
				Assert.fail("Validation FAILED: " + selectedProfilesFound + " profiles are still selected");
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_all_loaded_profiles_are_unselected_in_job_mapping_screen", e);
			PageObjectHelper.handleError(LOGGER, "verify_all_loaded_profiles_are_unselected_in_job_mapping_screen",
					"Error verifying all loaded profiles are unselected", e);
			Assert.fail("Error verifying all loaded profiles are unselected: " + e.getMessage());
		}
	}
}
