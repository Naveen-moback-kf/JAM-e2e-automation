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
import com.kfonetalentsuite.webdriverManager.DriverManager;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

public class PO45_ClearProfileSelectionwithNoneButton_JAM {
	
	WebDriver driver = DriverManager.getDriver();	

	protected static final Logger LOGGER = (Logger) LogManager.getLogger();
	
	public PO45_ClearProfileSelectionwithNoneButton_JAM() throws IOException {
		PageFactory.initElements(driver, this);
	}

	WebDriverWait wait = DriverManager.getWait();
	Utilities utils = new Utilities();
	JavascriptExecutor js = (JavascriptExecutor) driver;
	
	//XPATHs
	@FindBy(xpath = "//div[@data-testid='loader']//img")
	@CacheLookup
	WebElement pageLoadSpinner;
	
	@FindBy(xpath = "//*[contains(text(),'None')]")
	@CacheLookup
	WebElement noneBtn;
	
	/**
	 * Clicks on None button to unselect all profiles in Job Mapping screen
	 * This button is available in the dropdown that appears when clicking the chevron beside header checkbox
	 * The dropdown appears immediately after chevron click without page load spinner
	 */
	public void click_on_none_button_in_job_mapping_screen() {
		try {
			// Wait directly for None button to be clickable (dropdown appears immediately after chevron click)
			// No need to wait for spinner as dropdown shows without page reload
			
			try {
				wait.until(ExpectedConditions.elementToBeClickable(noneBtn)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", noneBtn);
				} catch (Exception s) {
					utils.jsClick(driver, noneBtn);
				}
			}
			
			// Wait for unselection to complete
			Thread.sleep(1000);
			PerformanceUtils.waitForPageReady(driver, 2);
			
			LOGGER.info("Successfully clicked on None button to unselect all profiles");
			ExtentCucumberAdapter.addTestStepLog("Clicked on None button in Job Mapping screen");
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_on_none_button_in_job_mapping_screen", e);
			LOGGER.error("Error clicking on None button - Method: click_on_none_button_in_job_mapping_screen", e);
			ExtentCucumberAdapter.addTestStepLog(" Error clicking on None button in Job Mapping screen");
			Assert.fail("Error clicking on None button in Job Mapping screen: " + e.getMessage());
		}
	}
	
	/**
	 * Verifies that all currently loaded profiles are unselected in Job Mapping screen
	 * This method checks only the profiles already loaded on the page (no scrolling)
	 */
	public void verify_all_loaded_profiles_are_unselected_in_job_mapping_screen() {
		int totalProfilesChecked = 0;
		int selectedProfilesFound = 0;
		int disabledProfilesCount = 0;
		
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			PerformanceUtils.waitForPageReady(driver, 2);
			
			LOGGER.info("========================================");
			LOGGER.info("VERIFYING ALL LOADED PROFILES ARE UNSELECTED");
			LOGGER.info("========================================");
			
			// Get all currently loaded checkboxes (no scrolling)
			var allCheckboxes = driver.findElements(
				By.xpath("//tbody//tr//td[1][contains(@class,'whitespace')]//input")
			);
			
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
				LOGGER.info(" VALIDATION PASSED: All profiles are correctly unselected");
				ExtentCucumberAdapter.addTestStepLog(" Validation PASSED: All " + totalProfilesChecked + " profiles are unselected");
			} else {
				LOGGER.error(" VALIDATION FAILED: Found " + selectedProfilesFound + " selected profiles (expected 0)");
				ExtentCucumberAdapter.addTestStepLog(" Validation FAILED: " + selectedProfilesFound + " profiles are still selected");
				Assert.fail("Validation FAILED: " + selectedProfilesFound + " profiles are still selected");
			}
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_all_loaded_profiles_are_unselected_in_job_mapping_screen", e);
			LOGGER.error("Error verifying all profiles are unselected - Method: verify_all_loaded_profiles_are_unselected_in_job_mapping_screen", e);
			ExtentCucumberAdapter.addTestStepLog(" Error verifying all loaded profiles are unselected");
			Assert.fail("Error verifying all loaded profiles are unselected: " + e.getMessage());
		}
	}
}

