package com.JobMapping.pageobjects;

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

import com.JobMapping.utils.PerformanceUtils;
import com.JobMapping.utils.ScreenshotHandler;
import com.JobMapping.utils.Utilities;
import com.JobMapping.webdriverManager.DriverManager;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

public class PO42_ClearProfileSelectionwithHeaderCheckbox_PM {
	
	WebDriver driver = DriverManager.getDriver();	

	protected static final Logger LOGGER = (Logger) LogManager.getLogger();
	
	public PO42_ClearProfileSelectionwithHeaderCheckbox_PM() throws IOException {
		PageFactory.initElements(driver, this);
	}

	WebDriverWait wait = DriverManager.getWait();
	Utilities utils = new Utilities();
	JavascriptExecutor js = (JavascriptExecutor) driver;
	
	// Static variables to store counts for validation
	public static int loadedProfilesBeforeUncheck = 0;
	public static int selectedProfilesBeforeUncheck = 0;
	
	//XPATHs
	@FindBy(xpath = "//*[@class='blocking-loader']//img")
	@CacheLookup
	WebElement pageLoadSpinner;
	
	@FindBy(xpath = "//thead//tr//th[1]//div[1]//kf-checkbox//div")
	@CacheLookup
	WebElement tableHeaderCheckbox;
	
	/**
	 * Clicks on header checkbox to unselect loaded job profiles
	 * This method clicks the header checkbox when profiles are already selected
	 * to clear the selection of currently loaded profiles
	 */
	public void click_on_header_checkbox_to_unselect_loaded_job_profiles_in_hcm_sync_profiles_screen() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			PerformanceUtils.waitForPageReady(driver, 2);
			
			// Store counts before unchecking
			loadedProfilesBeforeUncheck = driver.findElements(
				By.xpath("//tbody//tr//td[1]//div[1]//kf-checkbox")
			).size();
			
			selectedProfilesBeforeUncheck = driver.findElements(
				By.xpath("//tbody//tr//td[1]//div[1]//kf-checkbox//div[contains(@class,'selected')]")
			).size();
			
			LOGGER.info("Loaded Profiles on screen (BEFORE unchecking header checkbox): " + loadedProfilesBeforeUncheck);
			LOGGER.info("Selected Profiles (BEFORE unchecking header checkbox): " + selectedProfilesBeforeUncheck);
			
			// Scroll page to top first to avoid header overlap
			js.executeScript("window.scrollTo(0, 0);");
			Thread.sleep(500);
			
			// Scroll element into view with proper positioning
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", tableHeaderCheckbox);
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
			
			LOGGER.info("Successfully clicked on header checkbox to unselect loaded profiles");
			ExtentCucumberAdapter.addTestStepLog("Clicked on header checkbox to clear selection of loaded profiles");
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_on_header_checkbox_to_unselect_loaded_job_profiles_in_hcm_sync_profiles_screen", e);
			LOGGER.error("Error clicking on header checkbox to unselect profiles - Method: click_on_header_checkbox_to_unselect_loaded_job_profiles_in_hcm_sync_profiles_screen", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Error clicking on header checkbox to unselect profiles");
			Assert.fail("Error clicking on header checkbox to unselect profiles: " + e.getMessage());
		}
	}
	
	/**
	 * Verifies that loaded profiles are unselected after clicking header checkbox
	 * This validates that the profiles that were loaded before unchecking are now unselected
	 */
	public void verify_loaded_profiles_are_unselected_in_hcm_sync_profiles_screen() {
		int unselectedProfilesCount = 0;
		int disabledProfilesCount = 0;
		
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			PerformanceUtils.waitForPageReady(driver, 2);
			
			LOGGER.info("========================================");
			LOGGER.info("VERIFYING UNSELECTED PROFILES");
			LOGGER.info("========================================");
			
			// Verify loaded profiles are now unselected
			for (int i = 1; i <= loadedProfilesBeforeUncheck; i++) {
				try {
					WebElement checkbox = driver.findElement(
						By.xpath("//tbody//tr[" + i + "]//td[1]//div[1]//kf-checkbox//div")
					);
					
					js.executeScript("arguments[0].scrollIntoView(true);", checkbox);
					
					String classAttribute = checkbox.getAttribute("class");
					boolean isSelected = classAttribute != null && 
						(classAttribute.contains("selected") || classAttribute.contains("checked"));
					boolean isDisabled = classAttribute != null && classAttribute.contains("disable");
					
					if (isDisabled) {
						disabledProfilesCount++;
						continue;
					}
					
					if (!isSelected) {
						unselectedProfilesCount++;
					} else {
						LOGGER.warn("⚠️ Profile at position " + i + " is still SELECTED (should be unselected!)");
					}
					
				} catch (Exception e) {
					LOGGER.debug("Could not verify profile at position " + i + ": " + e.getMessage());
					continue;
				}
			}
			
			int expectedUnselected = loadedProfilesBeforeUncheck - disabledProfilesCount;
			
			LOGGER.info("========================================");
			LOGGER.info("VALIDATION RESULTS");
			LOGGER.info("========================================");
			LOGGER.info("Loaded Profiles: " + loadedProfilesBeforeUncheck);
			LOGGER.info("Disabled Profiles: " + disabledProfilesCount);
			LOGGER.info("Unselected Profiles: " + unselectedProfilesCount);
			LOGGER.info("Expected Unselected: " + expectedUnselected);
			LOGGER.info("========================================");
			
			if (unselectedProfilesCount >= expectedUnselected) {
				LOGGER.info("✓ VALIDATION PASSED: All loaded profiles are correctly unselected");
				ExtentCucumberAdapter.addTestStepLog("✓ Validation PASSED: " + unselectedProfilesCount + " loaded profiles are unselected");
			} else {
				LOGGER.error("❌ VALIDATION FAILED: Expected " + expectedUnselected + " unselected, but found only " + unselectedProfilesCount);
				ExtentCucumberAdapter.addTestStepLog("❌ Validation FAILED: Not all loaded profiles are unselected");
				Assert.fail("Validation FAILED: Not all loaded profiles are unselected");
			}
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_loaded_profiles_are_unselected_in_hcm_sync_profiles_screen", e);
			LOGGER.error("Error verifying loaded profiles are unselected - Method: verify_loaded_profiles_are_unselected_in_hcm_sync_profiles_screen", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Error verifying loaded profiles are unselected");
			Assert.fail("Error verifying loaded profiles are unselected: " + e.getMessage());
		}
	}
	
	/**
	 * Verifies that newly loaded profiles (after scrolling) are still selected
	 * This validates that unchecking header checkbox only affects currently loaded profiles,
	 * not profiles that were loaded after the initial Select All was clicked
	 */
	public void verify_newly_loaded_profiles_are_still_selected_in_hcm_sync_profiles_screen() {
		int totalProfilesNow = 0;
		int newlyLoadedProfiles = 0;
		int selectedInNewlyLoaded = 0;
		int disabledInNewlyLoaded = 0;
		
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			PerformanceUtils.waitForPageReady(driver, 2);
			
			// Get total profiles now loaded
			totalProfilesNow = driver.findElements(
				By.xpath("//tbody//tr//td[1]//div[1]//kf-checkbox")
			).size();
			
			newlyLoadedProfiles = totalProfilesNow - loadedProfilesBeforeUncheck;
			
			if (newlyLoadedProfiles <= 0) {
				LOGGER.warn("⚠️ No newly loaded profiles detected after scrolling");
				ExtentCucumberAdapter.addTestStepLog("⚠️ No newly loaded profiles to verify");
				return;
			}
			
			LOGGER.info("========================================");
			LOGGER.info("VERIFYING NEWLY LOADED PROFILES");
			LOGGER.info("========================================");
			LOGGER.info("Total Profiles Now: " + totalProfilesNow);
			LOGGER.info("Newly Loaded Profiles: " + newlyLoadedProfiles);
			
			// Verify newly loaded profiles are still selected
			for (int i = loadedProfilesBeforeUncheck + 1; i <= totalProfilesNow; i++) {
				try {
					WebElement checkbox = driver.findElement(
						By.xpath("//tbody//tr[" + i + "]//td[1]//div[1]//kf-checkbox//div")
					);
					
					js.executeScript("arguments[0].scrollIntoView(true);", checkbox);
					
					String classAttribute = checkbox.getAttribute("class");
					boolean isSelected = classAttribute != null && 
						(classAttribute.contains("selected") || classAttribute.contains("checked"));
					boolean isDisabled = classAttribute != null && classAttribute.contains("disable");
					
					if (isDisabled) {
						disabledInNewlyLoaded++;
						continue;
					}
					
					if (isSelected) {
						selectedInNewlyLoaded++;
					} else {
						LOGGER.warn("⚠️ Newly loaded profile at position " + i + " is UNSELECTED (should be selected!)");
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
				LOGGER.info("✓ VALIDATION PASSED: All newly loaded profiles are still selected");
				ExtentCucumberAdapter.addTestStepLog("✓ Validation PASSED: " + selectedInNewlyLoaded + " newly loaded profiles are still selected");
			} else {
				LOGGER.error("❌ VALIDATION FAILED: Expected " + expectedSelected + " selected, but found only " + selectedInNewlyLoaded);
				ExtentCucumberAdapter.addTestStepLog("❌ Validation FAILED: Not all newly loaded profiles are selected");
				Assert.fail("Validation FAILED: Not all newly loaded profiles are selected");
			}
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_newly_loaded_profiles_are_still_selected_in_hcm_sync_profiles_screen", e);
			LOGGER.error("Error verifying newly loaded profiles are still selected - Method: verify_newly_loaded_profiles_are_still_selected_in_hcm_sync_profiles_screen", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Error verifying newly loaded profiles are still selected");
			Assert.fail("Error verifying newly loaded profiles are still selected: " + e.getMessage());
		}
	}
}

