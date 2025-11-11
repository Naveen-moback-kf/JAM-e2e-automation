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

import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.ScreenshotHandler;
import com.kfonetalentsuite.utils.JobMapping.Utilities;
import com.kfonetalentsuite.webdriverManager.DriverManager;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

public class PO34_ValidateSelectAndSyncAllProfiles_PM {
	WebDriver driver = DriverManager.getDriver();	

	protected static final Logger LOGGER = (Logger) LogManager.getLogger();
	
	public PO34_ValidateSelectAndSyncAllProfiles_PM() throws IOException {
		PageFactory.initElements(driver, this);
	}

	WebDriverWait wait = DriverManager.getWait();
	Utilities utils = new Utilities();
	JavascriptExecutor js = (JavascriptExecutor) driver;
	
	public static int CountOfProfilesSelectedToExport;
	
	//XAPTHs
	@FindBy(xpath = "//*[@class='blocking-loader']//img")
	@CacheLookup
	WebElement pageLoadSpinner;	
	
	@FindBy(xpath = "//*[contains(@class,'kf-icon-arrow-down')]")
	@CacheLookup
	WebElement HCMchevronBtn;	
	
	@FindBy(xpath = "//*[contains(text(),'Select All')]")
	@CacheLookup
	WebElement HCMSelectAllBtn;
	
	
	
	public void click_on_chevron_button_beside_header_checkbox_in_hcm_sync_profiles_screen() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			try {
				wait.until(ExpectedConditions.elementToBeClickable(HCMchevronBtn)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", HCMchevronBtn);
				} catch (Exception s) {
					utils.jsClick(driver, HCMchevronBtn);
				}
			}
			LOGGER.info("Clicked on Chevron Button beside Header Checbox in HCM Sync Profiles Screen");
			ExtentCucumberAdapter.addTestStepLog("Clicked on Chevron Button beside Header Checbox in HCM Sync Profiles Screen");
			PerformanceUtils.waitForPageReady(driver, 2);
		} catch(Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_on_chevron_button_beside_header_checkbox_in_hcm_sync_profiles_screen", e);
			LOGGER.error("Issue in clicking Chevron Button beside Header Checbox in HCM Sync Profiles Screen - Method: click_on_chevron_button_beside_header_checkbox_in_hcm_sync_profiles_screen", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in clicking Chevron Button beside Header Checbox in HCM Sync Profiles Screen...Please Investigate!!!");
		}
	}
	
	/**
	 * Clicks on Select All button in HCM Sync Profiles screen
	 * The dropdown appears immediately after chevron click without page load spinner
	 */
	public void click_on_select_all_button_in_hcm_sync_profiles_screen() {
		try {
			// Wait directly for Select All button to be clickable (dropdown appears immediately after chevron click)
			// No need to wait for spinner as dropdown shows without page reload
			
			try {
				wait.until(ExpectedConditions.elementToBeClickable(HCMSelectAllBtn)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", HCMSelectAllBtn);
				} catch (Exception s) {
					utils.jsClick(driver, HCMSelectAllBtn);
				}
			}
			LOGGER.info("Clicked on Select All button in HCM Sync Profiles Screen");
			ExtentCucumberAdapter.addTestStepLog("Clicked on Select All button in HCM Sync Profiles Screen");
			PerformanceUtils.waitForPageReady(driver, 2);
		} catch(Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_on_select_all_button_in_hcm_sync_profiles_screen", e);
			LOGGER.error("Issue in clicking Select All button in HCM Sync Profiles Screen - Method: click_on_select_all_button_in_hcm_sync_profiles_screen", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in clicking Select All button in HCM Sync Profiles Screen...Please Investigate!!!");
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
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			PerformanceUtils.waitForPageReady(driver, 2);
			
			// Step 1: Get total profile count from "Showing X of Y Success Profiles" text
			try {
				WebElement countElement = driver.findElement(By.xpath("//div[contains(@class, 'body-text') and contains(text(), 'Showing')]"));
				String countText = countElement.getText().trim();
				
				// Parse text like "Showing 50 of 2842 Success Profiles"
				if (countText.contains("of")) {
					String afterOf = countText.substring(countText.indexOf("of") + 2).trim();
					String totalCountStr = afterOf.split("\\s+")[0].replaceAll("[^0-9]", "");
					
					if (!totalCountStr.isEmpty()) {
						totalProfiles = Integer.parseInt(totalCountStr);
						LOGGER.info("Total profiles to process: " + totalProfiles);
						ExtentCucumberAdapter.addTestStepLog("Total profiles to process: " + totalProfiles);
					}
				}
			} catch (Exception e) {
				LOGGER.warn("Could not extract total profile count from text: " + e.getMessage());
			}
			
			if (totalProfiles == 0) {
				LOGGER.warn("Could not determine total profile count. Will scroll until no more data loads.");
				ExtentCucumberAdapter.addTestStepLog("âš ï¸ Could not determine total profile count. Scrolling through all available profiles...");
			}
			
		// Step 2: Scroll to load all profiles
		LOGGER.info("ðŸ”„ Starting to scroll and load all profiles...");
		ExtentCucumberAdapter.addTestStepLog("Loading all profiles by scrolling...");
		
		int currentRowCount = 0;
		int previousRowCount = 0;
		int noChangeCount = 0;
		int maxScrollAttempts = 50;
		int scrollCount = 0;
		
		while (scrollCount < maxScrollAttempts) {
			// Scroll to bottom
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
			scrollCount++;
			
			// Wait for content to load
			Thread.sleep(2000);
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			PerformanceUtils.waitForPageReady(driver, 1);
			
			// Check current row count
			currentRowCount = driver.findElements(By.xpath("//tbody//tr")).size();
			
			// Check if no new rows loaded
			if (currentRowCount == previousRowCount) {
				noChangeCount++;
				LOGGER.debug("No new rows loaded. Count: {}/3", noChangeCount);
				if (noChangeCount >= 3) {
					LOGGER.info("âœ… Reached end of content. Total rows loaded: {}", currentRowCount);
					break;
				}
			} else {
				noChangeCount = 0;
				LOGGER.debug("Loaded rows: {} (scroll: {})", currentRowCount, scrollCount);
			}
			
			previousRowCount = currentRowCount;
		}
		
		LOGGER.info("âœ… Scrolling complete. Total rows loaded: {}, Scrolls performed: {}", 
			currentRowCount, scrollCount);
		ExtentCucumberAdapter.addTestStepLog("âœ… Total rows loaded: " + currentRowCount + " (using " + scrollCount + " scrolls)");
			
			// Step 3: Count selected checkboxes (enabled ones only, excluding disabled)
			LOGGER.info("Counting selected profiles...");
			
			// OPTIMIZED: Use JavaScript to count checkboxes in one call (much faster!)
			// This avoids making multiple XPath queries
			String jsScriptTotal = 
				"return document.querySelectorAll('tbody tr td:first-child kf-checkbox div').length;";
			String jsScriptDisabled = 
				"return document.querySelectorAll('tbody tr td:first-child kf-checkbox div.disable').length;";
			
			try {
				// Count using JavaScript (single call for each)
				Object totalResult = js.executeScript(jsScriptTotal);
				int totalCheckboxes = ((Long) totalResult).intValue();
				
				Object disabledResult = js.executeScript(jsScriptDisabled);
				disabledCount = ((Long) disabledResult).intValue();
				
				// Calculate enabled checkboxes (all - disabled = enabled/selected)
				selectedCount = totalCheckboxes - disabledCount;
				
				LOGGER.info("âœ“ Counted using JavaScript - Total: " + totalCheckboxes + ", Disabled: " + disabledCount + ", Selected: " + selectedCount);
				
			} catch (Exception jsException) {
				// Fallback: Use XPath counting (still faster than looping)
				LOGGER.warn("JavaScript counting failed, using XPath fallback...");
				
				// Count disabled checkboxes directly via XPath
				disabledCount = driver.findElements(
					By.xpath("//tbody//tr//td[1]//*//..//div//kf-checkbox//div[contains(@class, 'disable')]")
				).size();
				
				// Count all checkboxes
				int totalCheckboxes = driver.findElements(
					By.xpath("//tbody//tr//td[1]//*//..//div//kf-checkbox//div")
				).size();
				
				// Calculate enabled checkboxes (all - disabled = enabled)
				selectedCount = totalCheckboxes - disabledCount;
				
				LOGGER.info("âœ“ Counted using XPath - Total: " + totalCheckboxes + ", Disabled: " + disabledCount + ", Selected: " + selectedCount);
			}
			
			LOGGER.info("Found " + (selectedCount + disabledCount) + " total checkbox elements");
			LOGGER.info("Disabled checkboxes: " + disabledCount);
			LOGGER.info("Enabled (selected) checkboxes: " + selectedCount);
			
			LOGGER.info("âœ“ Successfully counted selected profiles: " + selectedCount + " out of " + currentRowCount + " profiles in HCM Sync Profiles screen");
			LOGGER.info("âœ“ Disabled profiles (skipped): " + disabledCount);
			ExtentCucumberAdapter.addTestStepLog("âœ“ Total rows loaded: " + currentRowCount);
			ExtentCucumberAdapter.addTestStepLog("âœ“ Selected profiles: " + selectedCount + " | Disabled: " + disabledCount);
			
			CountOfProfilesSelectedToExport = selectedCount;
			PO22_ValidateHCMSyncProfilesScreen_PM.profilesCount = CountOfProfilesSelectedToExport;
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_count_of_selected_profiles_by_scrolling_through_all_profiles", e);
			LOGGER.error("Error getting selected profiles count in HCM Sync Profiles screen - Method: verify_count_of_selected_profiles_by_scrolling_through_all_profiles", e);
			ExtentCucumberAdapter.addTestStepLog("âŒ Error getting selected profiles count");
		}
	}
}
