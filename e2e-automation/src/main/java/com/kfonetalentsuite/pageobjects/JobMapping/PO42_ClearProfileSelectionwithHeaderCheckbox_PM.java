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
import org.testng.Assert;

import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.ScreenshotHandler;
import com.kfonetalentsuite.utils.JobMapping.Utilities;
import com.kfonetalentsuite.utils.PageObjectHelper;
import com.kfonetalentsuite.webdriverManager.DriverManager;

public class PO42_ClearProfileSelectionwithHeaderCheckbox_PM {

	WebDriver driver = DriverManager.getDriver();

	protected static final Logger LOGGER = (Logger) LogManager.getLogger();

	public PO42_ClearProfileSelectionwithHeaderCheckbox_PM() throws IOException {
		PageFactory.initElements(driver, this);
	}

	WebDriverWait wait = DriverManager.getWait();
	Utilities utils = new Utilities();
	JavascriptExecutor js = (JavascriptExecutor) driver;

	// THREAD-SAFE: Each thread gets its own isolated state for parallel execution
	public static ThreadLocal<Integer> loadedProfilesBeforeUncheck = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<Integer> selectedProfilesBeforeUncheck = ThreadLocal.withInitial(() -> 0);

	// XPATHs
	@FindBy(xpath = "//*[@class='blocking-loader']//img")
	@CacheLookup
	WebElement pageLoadSpinner;

	@FindBy(xpath = "//thead//tr//th[1]//div[1]//kf-checkbox//div")
	@CacheLookup
	WebElement tableHeaderCheckbox;

	/**
	 * Clicks on header checkbox to unselect loaded job profiles This method clicks
	 * the header checkbox when profiles are already selected to clear the selection
	 * of currently loaded profiles
	 */
	public void click_on_header_checkbox_to_unselect_loaded_job_profiles_in_hcm_sync_profiles_screen() {
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

			// Store counts before unchecking
			loadedProfilesBeforeUncheck
					.set(driver.findElements(By.xpath("//tbody//tr//td[1]//div[1]//kf-checkbox")).size());

			selectedProfilesBeforeUncheck.set(driver
					.findElements(By.xpath("//tbody//tr//td[1]//div[1]//kf-checkbox//div[contains(@class,'selected')]"))
					.size());

			LOGGER.info("Loaded Profiles on screen (BEFORE unchecking header checkbox): "
					+ loadedProfilesBeforeUncheck.get());
			LOGGER.info(
					"Selected Profiles (BEFORE unchecking header checkbox): " + selectedProfilesBeforeUncheck.get());

			// Scroll page to top first to avoid header overlap
			js.executeScript("window.scrollTo(0, 0);");
			Thread.sleep(500);

			// Scroll element into view with proper positioning
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});",
					tableHeaderCheckbox);
			Thread.sleep(500);

			// Click on header checkbox to unselect - try multiple methods
			try {
				wait.until(ExpectedConditions.elementToBeClickable(tableHeaderCheckbox)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", tableHeaderCheckbox);
				} catch (Exception s) {
					utils.jsClick(driver, tableHeaderCheckbox);
				}
			}

			Thread.sleep(1000);

			PageObjectHelper.log(LOGGER, "Clicked on header checkbox to clear selection of loaded profiles");

			// Wait for spinners to disappear after unselecting profiles
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot(
					"click_on_header_checkbox_to_unselect_loaded_job_profiles_in_hcm_sync_profiles_screen", e);
			PageObjectHelper.log(LOGGER, "Error clicking on header checkbox to unselect profiles");
			Assert.fail("Error clicking on header checkbox to unselect profiles: " + e.getMessage());
		}
	}

	/**
	 * Verifies that loaded profiles are unselected after clicking header checkbox
	 * This validates that the profiles that were loaded before unchecking are now
	 * unselected
	 */
	public void verify_loaded_profiles_are_unselected_in_hcm_sync_profiles_screen() {
		int unselectedProfilesCount = 0;
		int disabledProfilesCount = 0;

		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

			LOGGER.info("========================================");
			LOGGER.info("VERIFYING UNSELECTED PROFILES");
			LOGGER.info("========================================");

			// Get total profiles currently on screen (including newly loaded after
			// scrolling)
			int totalProfilesOnScreen = driver.findElements(By.xpath("//tbody//tr//td[1]//div[1]//kf-checkbox")).size();

			LOGGER.info("Total Profiles on screen (after scrolling): " + totalProfilesOnScreen);
			LOGGER.info("Original Loaded Profiles (before scrolling): " + loadedProfilesBeforeUncheck.get());

			// Verify ALL profiles on screen are now unselected (both original + newly
			// loaded)
			for (int i = 1; i <= totalProfilesOnScreen; i++) {
				try {
					WebElement checkbox = driver
							.findElement(By.xpath("//tbody//tr[" + i + "]//td[1]//div[1]//kf-checkbox//div"));

					js.executeScript("arguments[0].scrollIntoView(true);", checkbox);

					String classAttribute = checkbox.getAttribute("class");
					boolean isSelected = classAttribute != null
							&& (classAttribute.contains("selected") || classAttribute.contains("checked"));
					boolean isDisabled = classAttribute != null && classAttribute.contains("disable");

					if (isDisabled) {
						disabledProfilesCount++;
						continue;
					}

					if (!isSelected) {
						unselectedProfilesCount++;
					} else {
						LOGGER.warn(" Profile at position " + i + " is still SELECTED (should be unselected!)");
					}

				} catch (Exception e) {
					LOGGER.debug("Could not verify profile at position " + i + ": " + e.getMessage());
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
			ScreenshotHandler
					.captureFailureScreenshot("verify_loaded_profiles_are_unselected_in_hcm_sync_profiles_screen", e);
			PageObjectHelper.log(LOGGER, "Error verifying loaded profiles are unselected");
			Assert.fail("Error verifying loaded profiles are unselected: " + e.getMessage());
		}
	}

	/**
	 * Verifies that newly loaded profiles (after scrolling) are still selected This
	 * validates that unchecking header checkbox only affects currently loaded
	 * profiles, not profiles that were loaded after the initial Select All was
	 * clicked
	 */
	public void verify_newly_loaded_profiles_are_still_selected_in_hcm_sync_profiles_screen() {
		int totalProfilesNow = 0;
		int newlyLoadedProfiles = 0;
		int selectedInNewlyLoaded = 0;
		int disabledInNewlyLoaded = 0;

		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

			// Get total profiles now loaded
			totalProfilesNow = driver.findElements(By.xpath("//tbody//tr//td[1]//div[1]//kf-checkbox")).size();

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

			// Verify newly loaded profiles are still selected
			for (int i = loadedProfilesBeforeUncheck.get() + 1; i <= totalProfilesNow; i++) {
				try {
					WebElement checkbox = driver
							.findElement(By.xpath("//tbody//tr[" + i + "]//td[1]//div[1]//kf-checkbox//div"));

					js.executeScript("arguments[0].scrollIntoView(true);", checkbox);

					String classAttribute = checkbox.getAttribute("class");
					boolean isSelected = classAttribute != null
							&& (classAttribute.contains("selected") || classAttribute.contains("checked"));
					boolean isDisabled = classAttribute != null && classAttribute.contains("disable");

					if (isDisabled) {
						disabledInNewlyLoaded++;
						continue;
					}

					if (isSelected) {
						selectedInNewlyLoaded++;
					} else {
						LOGGER.warn(" Newly loaded profile at position " + i + " is UNSELECTED (should be selected!)");
					}

				} catch (Exception e) {
					LOGGER.debug("Could not verify profile at position " + i + ": " + e.getMessage());
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
					"verify_newly_loaded_profiles_are_still_selected_in_hcm_sync_profiles_screen", e);
			PageObjectHelper.log(LOGGER, "Error verifying newly loaded profiles are still selected");
			Assert.fail("Error verifying newly loaded profiles are still selected: " + e.getMessage());
		}
	}
}
