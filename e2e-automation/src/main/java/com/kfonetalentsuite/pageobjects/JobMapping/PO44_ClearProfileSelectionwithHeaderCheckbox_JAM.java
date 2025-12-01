package com.kfonetalentsuite.pageobjects.JobMapping;

import java.io.IOException;
import java.util.List;

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
import com.kfonetalentsuite.utils.JobMapping.ScreenshotHandler;
import com.kfonetalentsuite.utils.JobMapping.Utilities;
import com.kfonetalentsuite.utils.PageObjectHelper;
import com.kfonetalentsuite.webdriverManager.DriverManager;

public class PO44_ClearProfileSelectionwithHeaderCheckbox_JAM {

	WebDriver driver = DriverManager.getDriver();

	protected static final Logger LOGGER = (Logger) LogManager.getLogger();

	public PO44_ClearProfileSelectionwithHeaderCheckbox_JAM() throws IOException {
		PageFactory.initElements(driver, this);
	}

	WebDriverWait wait = DriverManager.getWait();
	Utilities utils = new Utilities();
	JavascriptExecutor js = (JavascriptExecutor) driver;

	// THREAD-SAFE: Each thread gets its own isolated state for parallel execution
	public static ThreadLocal<Integer> loadedProfilesBeforeUncheck = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<Integer> selectedProfilesBeforeUncheck = ThreadLocal.withInitial(() -> 0);

	// XPATHs
	@FindBy(xpath = "//div[@data-testid='loader']//img")
	@CacheLookup
	WebElement pageLoadSpinner;

	@FindBy(xpath = "//thead//input[@type='checkbox']")
	@CacheLookup
	WebElement headerCheckbox;

	/**
	 * Clicks on header checkbox to unselect loaded job profiles in Job Mapping
	 * screen This method clicks the header checkbox when profiles are already
	 * selected to clear the selection of currently loaded profiles
	 */
	public void click_on_header_checkbox_to_unselect_loaded_job_profiles_in_job_mapping_screen() {
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

			// Store counts before unchecking
			List<WebElement> allCheckboxes = driver
					.findElements(By.xpath("//tbody//tr//td[1][contains(@class,'whitespace')]//input"));
			loadedProfilesBeforeUncheck.set(allCheckboxes.size());

			// Count selected checkboxes using isSelected() instead of @checked attribute
			int selectedCount = 0;
			for (WebElement checkbox : allCheckboxes) {
				if (checkbox.isSelected()) {
					selectedCount++;
				}
			}
			selectedProfilesBeforeUncheck.set(selectedCount);

			LOGGER.info("Loaded Profiles on screen (BEFORE unchecking header checkbox): "
					+ loadedProfilesBeforeUncheck.get());
			LOGGER.info(
					"Selected Profiles (BEFORE unchecking header checkbox): " + selectedProfilesBeforeUncheck.get());

			// Click on header checkbox to unselect
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", headerCheckbox);
			Thread.sleep(500);

			try {
				wait.until(ExpectedConditions.elementToBeClickable(headerCheckbox)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", headerCheckbox);
				} catch (Exception s) {
					utils.jsClick(driver, headerCheckbox);
				}
			}

			Thread.sleep(1000);

			PageObjectHelper.log(LOGGER, "Clicked on header checkbox to clear selection of loaded profiles");

			// Wait for spinners to disappear after unselecting profiles
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot(
					"click_on_header_checkbox_to_unselect_loaded_job_profiles_in_job_mapping_screen", e);
			PageObjectHelper.log(LOGGER, "Error clicking on header checkbox to unselect profiles");
			Assert.fail("Error clicking on header checkbox to unselect profiles: " + e.getMessage());
		}
	}

	/**
	 * Verifies that loaded profiles are unselected after clicking header checkbox
	 * in Job Mapping screen This validates that the profiles that were loaded
	 * before unchecking are now unselected
	 */
	public void verify_loaded_profiles_are_unselected_in_job_mapping_screen() {
		int unselectedProfilesCount = 0;
		int disabledProfilesCount = 0;

		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

			LOGGER.info("========================================");
			LOGGER.info("VERIFYING UNSELECTED PROFILES");
			LOGGER.info("========================================");

			// Get all checkboxes currently on screen (including newly loaded after
			// scrolling)
			var allCheckboxes = driver
					.findElements(By.xpath("//tbody//tr//td[1][contains(@class,'whitespace')]//input"));

			int totalProfilesOnScreen = allCheckboxes.size();
			LOGGER.info("Total Profiles on screen (after scrolling): " + totalProfilesOnScreen);
			LOGGER.info("Original Loaded Profiles (before scrolling): " + loadedProfilesBeforeUncheck.get());

			// Verify ALL profiles on screen are now unselected (both original 20 + newly
			// loaded 20)
			for (int i = 0; i < totalProfilesOnScreen; i++) {
				try {
					WebElement checkbox = allCheckboxes.get(i);

					js.executeScript("arguments[0].scrollIntoView(true);", checkbox);

					boolean isSelected = checkbox.isSelected();
					boolean isDisabled = !checkbox.isEnabled();

					if (isDisabled) {
						disabledProfilesCount++;
						continue;
					}

					if (!isSelected) {
						unselectedProfilesCount++;
					} else {
						LOGGER.warn(" Profile at position " + (i + 1) + " is still SELECTED (should be unselected!)");
					}

				} catch (Exception e) {
					LOGGER.debug("Could not verify profile at position " + (i + 1) + ": " + e.getMessage());
					continue;
				}
			}

			int expectedUnselected = totalProfilesOnScreen - disabledProfilesCount;

			LOGGER.info("========================================");
			LOGGER.info("VALIDATION RESULTS");
			LOGGER.info("========================================");
			LOGGER.info("Total Profiles on Screen: " + totalProfilesOnScreen);
			LOGGER.info("Disabled Profiles: " + disabledProfilesCount);
			LOGGER.info("Unselected Profiles: " + unselectedProfilesCount);
			LOGGER.info("Expected Unselected: " + expectedUnselected);
			LOGGER.info("========================================");

			if (unselectedProfilesCount >= expectedUnselected) {
				PageObjectHelper.log(LOGGER, "Validation PASSED: " + unselectedProfilesCount + " loaded profiles are unselected");
			} else {
				PageObjectHelper.log(LOGGER, "Validation FAILED: Not all loaded profiles are unselected");
				Assert.fail("Validation FAILED: Not all loaded profiles are unselected");
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_loaded_profiles_are_unselected_in_job_mapping_screen",
					e);
			PageObjectHelper.log(LOGGER, "Error verifying loaded profiles are unselected");
			Assert.fail("Error verifying loaded profiles are unselected: " + e.getMessage());
		}
	}

	/**
	 * Verifies that newly loaded profiles (after scrolling) are still selected in
	 * Job Mapping screen This validates that unchecking header checkbox only
	 * affects currently loaded profiles, not profiles that were loaded after the
	 * initial Select All was clicked
	 */
	public void verify_newly_loaded_profiles_are_still_selected_in_job_mapping_screen() {
		int totalProfilesNow = 0;
		int newlyLoadedProfiles = 0;
		int selectedInNewlyLoaded = 0;
		int disabledInNewlyLoaded = 0;

		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

			// Get total profiles now loaded
			var allCheckboxes = driver
					.findElements(By.xpath("//tbody//tr//td[1][contains(@class,'whitespace')]//input"));

			totalProfilesNow = allCheckboxes.size();
			newlyLoadedProfiles = totalProfilesNow - loadedProfilesBeforeUncheck.get();

			if (newlyLoadedProfiles <= 0) {
				PageObjectHelper.log(LOGGER, "No newly loaded profiles to verify");
				return;
			}

			LOGGER.info("========================================");
			LOGGER.info("VERIFYING NEWLY LOADED PROFILES");
			LOGGER.info("========================================");
			LOGGER.info("Total Profiles Now: " + totalProfilesNow);
			LOGGER.info("Newly Loaded Profiles: " + newlyLoadedProfiles);

			// Verify newly loaded profiles are still selected (from index
			// loadedProfilesBeforeUncheck onwards)
			for (int i = loadedProfilesBeforeUncheck.get(); i < allCheckboxes.size(); i++) {
				try {
					WebElement checkbox = allCheckboxes.get(i);

					js.executeScript("arguments[0].scrollIntoView(true);", checkbox);

					boolean isSelected = checkbox.isSelected();
					boolean isDisabled = !checkbox.isEnabled();

					if (isDisabled) {
						disabledInNewlyLoaded++;
						continue;
					}

					if (isSelected) {
						selectedInNewlyLoaded++;
					} else {
						LOGGER.warn(" Newly loaded profile at position " + (i + 1)
								+ " is UNSELECTED (should be selected!)");
					}

				} catch (Exception e) {
					LOGGER.debug("Could not verify profile at position " + (i + 1) + ": " + e.getMessage());
					continue;
				}
			}

			int expectedSelected = newlyLoadedProfiles - disabledInNewlyLoaded;

			LOGGER.info("========================================");
			LOGGER.info("VALIDATION RESULTS");
			LOGGER.info("========================================");
			LOGGER.info("Newly Loaded Profiles: " + newlyLoadedProfiles);
			LOGGER.info("Disabled in Newly Loaded: " + disabledInNewlyLoaded);
			LOGGER.info("Selected in Newly Loaded: " + selectedInNewlyLoaded);
			LOGGER.info("Expected Selected: " + expectedSelected);
			LOGGER.info("========================================");

			if (selectedInNewlyLoaded >= expectedSelected) {
				PageObjectHelper.log(LOGGER, "Validation PASSED: " + selectedInNewlyLoaded + " newly loaded profiles are still selected");
			} else {
				PageObjectHelper.log(LOGGER, "Validation FAILED: Not all newly loaded profiles are selected");
				Assert.fail("Validation FAILED: Not all newly loaded profiles are selected");
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot(
					"verify_newly_loaded_profiles_are_still_selected_in_job_mapping_screen", e);
			PageObjectHelper.log(LOGGER, "Error verifying newly loaded profiles are still selected");
			Assert.fail("Error verifying newly loaded profiles are still selected: " + e.getMessage());
		}
	}
}
