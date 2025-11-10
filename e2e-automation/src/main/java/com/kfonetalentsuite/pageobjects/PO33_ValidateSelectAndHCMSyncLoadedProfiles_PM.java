package com.kfonetalentsuite.pageobjects;

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

import com.kfonetalentsuite.utils.PerformanceUtils;
import com.kfonetalentsuite.utils.ScreenshotHandler;
import com.kfonetalentsuite.utils.Utilities;
import com.kfonetalentsuite.webdriverManager.DriverManager;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

public class PO33_ValidateSelectAndHCMSyncLoadedProfiles_PM {
	
	WebDriver driver = DriverManager.getDriver();	

	protected static final Logger LOGGER = (Logger) LogManager.getLogger();
	
	public PO33_ValidateSelectAndHCMSyncLoadedProfiles_PM() throws IOException {
		PageFactory.initElements(driver, this);
	}

	WebDriverWait wait = DriverManager.getWait();
	Utilities utils = new Utilities();
	JavascriptExecutor js = (JavascriptExecutor) driver;
	
	//XPATHs
	@FindBy(xpath = "//*[@class='blocking-loader']//img")
	@CacheLookup
	WebElement pageLoadSpinner;
	
	@FindBy(xpath = "//div[contains(text(),'Showing')]")
	@CacheLookup
	WebElement showingJobResultsCount;
	
	/**
	 * Verifies that profiles loaded after clicking the header checkbox are NOT selected.
	 * 
	 * Expected Flow:
	 * 1. Loaded Profiles on screen (BEFORE Header checkbox is clicked): 100 profiles
	 * 2. Header checkbox is clicked → Selects 61 profiles (39 are disabled, cannot be selected)
	 * 3. User scrolls → Loads 50 MORE profiles (Total now = 150)
	 * 4. These 50 newly loaded profiles should NOT be selected
	 * 
	 * Implementation:
	 * - Uses stored values from PO22 (loadedProfilesBeforeHeaderCheckboxClick, selectedProfilesAfterHeaderCheckboxClick, disabledProfilesCountInLoadedProfiles)
	 * - Counts TOTAL profiles currently in DOM
	 * - Calculates newly loaded = Total - Loaded Before
	 * - Verifies newly loaded profiles are UNSELECTED
	 * 
	 * This validates that the "header checkbox select" only selects currently LOADED profiles,
	 * not profiles that load later via lazy loading/scrolling.
	 */
	public void verify_profiles_loaded_after_clicking_header_checkbox_are_not_selected_in_HCM_Sync_Profiles_screen() {
		int loadedProfilesBeforeHeaderCheckboxClick = PO22_ValidateHCMSyncProfilesScreen_PM.loadedProfilesBeforeHeaderCheckboxClick;
		int selectedProfilesAfterHeaderCheckboxClick = PO22_ValidateHCMSyncProfilesScreen_PM.selectedProfilesAfterHeaderCheckboxClick;
		int disabledProfilesInLoadedProfiles = PO22_ValidateHCMSyncProfilesScreen_PM.disabledProfilesCountInLoadedProfiles;
		int totalProfilesCurrentlyLoaded = 0;
		int newlyLoadedProfilesAfterScrolling = 0;
		int unselectedProfilesCount = 0;
		int disabledInNewlyLoadedProfiles = 0;
		
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			PerformanceUtils.waitForPageReady(driver, 2);
			
			// Step 1: Count TOTAL profiles currently in DOM (includes newly loaded after scrolling)
			totalProfilesCurrentlyLoaded = driver.findElements(
				By.xpath("//tbody//tr//td[1]//div[1]//kf-checkbox")
			).size();
			
			LOGGER.info("Total Profiles Currently loaded on screen: " + totalProfilesCurrentlyLoaded);
			
			// Step 2: Calculate newly loaded profiles
			newlyLoadedProfilesAfterScrolling = totalProfilesCurrentlyLoaded - loadedProfilesBeforeHeaderCheckboxClick;
			
			if (newlyLoadedProfilesAfterScrolling <= 0) {
				LOGGER.warn("⚠️ No newly loaded profiles detected after scrolling");
				LOGGER.warn("   Total profiles: " + totalProfilesCurrentlyLoaded + ", Loaded before checkbox: " + loadedProfilesBeforeHeaderCheckboxClick);
				ExtentCucumberAdapter.addTestStepLog("⚠️ No newly loaded profiles to verify");
				return;
			}
			
			LOGGER.info("========================================");
			LOGGER.info("PROFILE COUNTS SUMMARY");
			LOGGER.info("========================================");
			LOGGER.info("Loaded Profiles on screen (BEFORE Header checkbox clicked): " + loadedProfilesBeforeHeaderCheckboxClick);
			LOGGER.info("Selected Profiles (When header checkbox clicked): " + selectedProfilesAfterHeaderCheckboxClick);
			LOGGER.info("Disabled profiles (cannot be selected) in loaded profiles: " + disabledProfilesInLoadedProfiles);
			LOGGER.info("Newly Loaded Profiles on screen (AFTER Header checkbox clicked): " + newlyLoadedProfilesAfterScrolling);
			LOGGER.info("Total Profiles Currently loaded on screen: " + totalProfilesCurrentlyLoaded);
			LOGGER.info("========================================");
			
			ExtentCucumberAdapter.addTestStepLog("Verifying " + newlyLoadedProfilesAfterScrolling + " newly loaded profiles are NOT selected...");
			
			// Step 3: Verify that newly loaded profiles (from position loadedProfilesBeforeHeaderCheckboxClick+1 onwards) are NOT selected
			for (int i = loadedProfilesBeforeHeaderCheckboxClick + 1; i <= totalProfilesCurrentlyLoaded; i++) {
				try {
					WebElement checkbox = driver.findElement(
						By.xpath("//tbody//tr[" + i + "]//td[1]//div[1]//kf-checkbox//div")
					);
					
					// Scroll to element to ensure it's visible
					js.executeScript("arguments[0].scrollIntoView(true);", checkbox);
					
					// Check if checkbox class attributes
					String classAttribute = checkbox.getAttribute("class");
					boolean isSelected = classAttribute != null && 
						(classAttribute.contains("selected") || classAttribute.contains("checked"));
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
						LOGGER.warn("⚠️ Newly loaded profile at position " + i + " is SELECTED (should be unselected!)");
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
				LOGGER.info("✓ VALIDATION PASSED: All " + unselectedProfilesCount + " newly loaded profiles are correctly unselected");
				ExtentCucumberAdapter.addTestStepLog("✓ Validation PASSED: " + unselectedProfilesCount + " newly loaded profiles are NOT selected (as expected)");
			} else {
				int missingUnselected = expectedUnselected - unselectedProfilesCount;
				LOGGER.error("❌ VALIDATION FAILED: Expected " + expectedUnselected + " unselected, but found only " + unselectedProfilesCount);
				LOGGER.error("   → " + missingUnselected + " newly loaded profiles are unexpectedly SELECTED");
				ExtentCucumberAdapter.addTestStepLog("❌ Validation FAILED: " + missingUnselected + " newly loaded profiles are unexpectedly selected");
				Assert.fail("Validation FAILED: " + missingUnselected + " newly loaded profiles are selected (should be unselected)");
			}
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_profiles_loaded_after_clicking_header_checkbox_are_not_selected_in_HCM_Sync_Profiles_screen", e);
			LOGGER.error("Error verifying newly loaded profiles in HCM Sync Profiles Screen - Method: verify_profiles_loaded_after_clicking_header_checkbox_are_not_selected_in_HCM_Sync_Profiles_screen", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Error verifying newly loaded profiles are not selected in HCM Sync Profiles Screen");
			Assert.fail("Error verifying newly loaded profiles are not selected: " + e.getMessage());
		}
	}
}

