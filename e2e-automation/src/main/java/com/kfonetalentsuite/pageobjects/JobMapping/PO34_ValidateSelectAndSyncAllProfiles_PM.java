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

	// XAPTHs
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
			ExtentCucumberAdapter
					.addTestStepLog("Clicked on Chevron Button beside Header Checbox in HCM Sync Profiles Screen");
			PerformanceUtils.waitForPageReady(driver, 2);
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot(
					"click_on_chevron_button_beside_header_checkbox_in_hcm_sync_profiles_screen", e);
			LOGGER.error(
					"Issue in clicking Chevron Button beside Header Checbox in HCM Sync Profiles Screen - Method: click_on_chevron_button_beside_header_checkbox_in_hcm_sync_profiles_screen",
					e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog(
					"Issue in clicking Chevron Button beside Header Checbox in HCM Sync Profiles Screen...Please Investigate!!!");
		}
	}

	/**
	 * Clicks on Select All button in HCM Sync Profiles screen The dropdown appears
	 * immediately after chevron click without page load spinner
	 */
	public void click_on_select_all_button_in_hcm_sync_profiles_screen() {
		try {
			// Wait directly for Select All button to be clickable (dropdown appears
			// immediately after chevron click)
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
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_on_select_all_button_in_hcm_sync_profiles_screen", e);
			LOGGER.error(
					"Issue in clicking Select All button in HCM Sync Profiles Screen - Method: click_on_select_all_button_in_hcm_sync_profiles_screen",
					e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog(
					"Issue in clicking Select All button in HCM Sync Profiles Screen...Please Investigate!!!");
		}
	}

	/**
	 * Scrolls through all profiles and counts the number of selected profiles based
	 * on checkbox state. This method handles lazy loading by scrolling to the
	 * bottom with increased wait times.
	 * 
	 * Process: 1. Get total profile count from "Showing X of Y" text 2. Scroll to
	 * bottom repeatedly with longer waits to load all profiles 3. Count checkboxes
	 * that are enabled (excluding disabled ones)
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
				WebElement countElement = driver
						.findElement(By.xpath("//div[contains(@class, 'body-text') and contains(text(), 'Showing')]"));
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
				ExtentCucumberAdapter.addTestStepLog(
						" Could not determine total profile count. Scrolling through all available profiles...");
			}

			// Step 2: Scroll to load all profiles (ENHANCED FOR HEADLESS MODE)
			LOGGER.info(" Starting to scroll and load all profiles...");
			ExtentCucumberAdapter.addTestStepLog("Loading all profiles by scrolling...");

			// DYNAMIC maxScrollAttempts calculation based on total profiles
			int maxScrollAttempts;
			if (totalProfiles > 0) {
				// Estimate scrolls needed: Assume ~50 rows load per scroll on average
				// Add 25% buffer to account for varying load sizes + add 5 for safety
				int estimatedScrolls = (int) Math.ceil(totalProfiles / 50.0);
				maxScrollAttempts = (int) (estimatedScrolls * 1.25) + 5;

				// Set reasonable bounds: minimum 15, maximum 100
				maxScrollAttempts = Math.max(15, Math.min(100, maxScrollAttempts));

				LOGGER.info("Dynamic maxScrollAttempts calculated: {} (based on {} total profiles)", maxScrollAttempts,
						totalProfiles);
				ExtentCucumberAdapter.addTestStepLog("Max scroll attempts set to: " + maxScrollAttempts
						+ " (dynamically calculated for " + totalProfiles + " profiles)");
			} else {
				// Fallback if total count unavailable
				maxScrollAttempts = 60;
				LOGGER.info("Using default maxScrollAttempts: {} (total profile count unavailable)", maxScrollAttempts);
				ExtentCucumberAdapter.addTestStepLog(
						"Max scroll attempts: " + maxScrollAttempts + " (default - profile count unknown)");
			}

			int currentRowCount = 0;
			int previousRowCount = 0;
			int noChangeCount = 0;
			int scrollCount = 0;

			// Track if we reached max scroll attempts (incomplete count scenario)
			boolean reachedMaxScrollLimit = false;

			while (scrollCount < maxScrollAttempts) {
				// ENHANCED SCROLLING STRATEGY for HEADLESS MODE:
				// Use multiple scroll techniques to ensure lazy loading triggers

				// Method 1: Scroll using document.body.scrollHeight (more reliable in headless)
				try {
					js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
				} catch (Exception e1) {
					// Fallback to documentElement.scrollHeight
					try {
						js.executeScript("window.scrollTo(0, document.documentElement.scrollHeight);");
					} catch (Exception e2) {
						// Last resort: scroll by large pixel amount
						js.executeScript("window.scrollBy(0, 10000);");
					}
				}

				scrollCount++;
				LOGGER.debug("Scroll attempt #{} - waiting for content to load...", scrollCount);

				// CRITICAL: Longer wait for HEADLESS MODE (lazy loading needs more time)
				Thread.sleep(3000); // Increased from 2000 to 3000ms for headless stability

				// Wait for any spinners to disappear (using PerformanceUtils for consistency)
				PerformanceUtils.waitForSpinnersToDisappear(driver, 5);

				// Wait for page readiness
				PerformanceUtils.waitForPageReady(driver, 2); // Increased from 1 to 2 seconds

				// Additional wait for DOM updates in headless mode
				Thread.sleep(1000); // Extra buffer for lazy-loaded content to render

				// Check current row count
				currentRowCount = driver.findElements(By.xpath("//tbody//tr")).size();

				LOGGER.debug("Current row count after scroll #{}: {}", scrollCount, currentRowCount);

				// Check if no new rows loaded
				if (currentRowCount == previousRowCount) {
					noChangeCount++;
					LOGGER.debug("No new rows loaded. Stagnation count: {}/3", noChangeCount);

					if (noChangeCount >= 3) {
						LOGGER.info("... Reached end of content after {} consecutive non-loading scrolls",
								noChangeCount);
						LOGGER.info("... Final row count: {}", currentRowCount);
						break;
					}

					// ADDITIONAL: Try forcing scroll to absolute bottom one more time
					if (noChangeCount == 2) {
						LOGGER.debug("Attempting final aggressive scroll to ensure all content loaded...");
						js.executeScript(
								"window.scrollTo(0, Math.max(document.body.scrollHeight, document.documentElement.scrollHeight, document.body.offsetHeight, document.documentElement.offsetHeight, document.body.clientHeight, document.documentElement.clientHeight));");
						Thread.sleep(2000); // Wait after aggressive scroll

						// Wait for spinners after aggressive scroll
						PerformanceUtils.waitForSpinnersToDisappear(driver, 5);
					}
				} else {
					noChangeCount = 0;
					int newRows = currentRowCount - previousRowCount;
					LOGGER.debug("✓ Loaded {} new rows (total: {}, scroll: #{})", newRows, currentRowCount,
							scrollCount);
				}

				previousRowCount = currentRowCount;
			}

			// Check if we exited loop due to maxScrollAttempts (not because of 3
			// consecutive no-change)
			if (scrollCount >= maxScrollAttempts && noChangeCount < 3) {
				reachedMaxScrollLimit = true;
				LOGGER.warn(
						"⚠️ REACHED MAX SCROLL LIMIT: Stopped at {} scrolls (limit: {}). Some profiles may not have been loaded!",
						scrollCount, maxScrollAttempts);
				LOGGER.warn("⚠️ Profile count may be INCOMPLETE. Current rows loaded: {} (Expected: ~{})",
						currentRowCount, totalProfiles);
				ExtentCucumberAdapter.addTestStepLog("⚠️ WARNING: Reached max scroll limit (" + maxScrollAttempts
						+ " scrolls). Profile count may be incomplete!");
			}

			LOGGER.info("... Scrolling complete. Total rows loaded: {}, Scrolls performed: {}", currentRowCount,
					scrollCount);
			ExtentCucumberAdapter.addTestStepLog(
					"... Total rows loaded: " + currentRowCount + " (using " + scrollCount + " scrolls)");

			// Step 3: Count selected checkboxes (enabled ones only, excluding disabled)
			LOGGER.info("Counting selected profiles...");

			// OPTIMIZED: Use JavaScript to count checkboxes in one call (much faster!)
			// This avoids making multiple XPath queries
			String jsScriptTotal = "return document.querySelectorAll('tbody tr td:first-child kf-checkbox div').length;";
			String jsScriptDisabled = "return document.querySelectorAll('tbody tr td:first-child kf-checkbox div.disable').length;";

			try {
				// Count using JavaScript (single call for each)
				Object totalResult = js.executeScript(jsScriptTotal);
				int totalCheckboxes = ((Long) totalResult).intValue();

				Object disabledResult = js.executeScript(jsScriptDisabled);
				disabledCount = ((Long) disabledResult).intValue();

				// Calculate enabled checkboxes (all - disabled = enabled/selected)
				selectedCount = totalCheckboxes - disabledCount;

				LOGGER.info(" Counted using JavaScript - Total: " + totalCheckboxes + ", Disabled: " + disabledCount
						+ ", Selected: " + selectedCount);

			} catch (Exception jsException) {
				// Fallback: Use XPath counting (still faster than looping)
				LOGGER.warn("JavaScript counting failed, using fallback method...");

				// Count disabled checkboxes directly via XPath
				disabledCount = driver
						.findElements(By
								.xpath("//tbody//tr//td[1]//*//..//div//kf-checkbox//div[contains(@class, 'disable')]"))
						.size();

				// Count all checkboxes
				int totalCheckboxes = driver.findElements(By.xpath("//tbody//tr//td[1]//*//..//div//kf-checkbox//div"))
						.size();

				// Calculate enabled checkboxes (all - disabled = enabled)
				selectedCount = totalCheckboxes - disabledCount;

				LOGGER.info(" Counted using fallback method - Total: " + totalCheckboxes + ", Disabled: "
						+ disabledCount + ", Selected: " + selectedCount);
			}

			LOGGER.info("Found " + (selectedCount + disabledCount) + " total checkbox elements");
			LOGGER.info("Disabled checkboxes: " + disabledCount);
			LOGGER.info("Enabled (selected) checkboxes: " + selectedCount);

			LOGGER.info(" Successfully counted selected profiles: " + selectedCount + " out of " + currentRowCount
					+ " profiles in HCM Sync Profiles screen");
			LOGGER.info(" Disabled profiles (skipped): " + disabledCount);
			ExtentCucumberAdapter.addTestStepLog(" Total rows loaded: " + currentRowCount);
			ExtentCucumberAdapter
					.addTestStepLog(" Selected profiles: " + selectedCount + " | Disabled: " + disabledCount);

			// Store the count and flag indicating if count is complete
			CountOfProfilesSelectedToExport = selectedCount;
			PO22_ValidateHCMSyncProfilesScreen_PM.profilesCount.set(CountOfProfilesSelectedToExport);
			PO22_ValidateHCMSyncProfilesScreen_PM.isProfilesCountComplete.set(!reachedMaxScrollLimit);

			// Log the count completeness status
			if (reachedMaxScrollLimit) {
				LOGGER.warn("⚠️ INCOMPLETE COUNT FLAG SET: isProfilesCountComplete = false");
				LOGGER.warn("⚠️ Counted {} profiles, but max scroll limit reached. Actual total may be higher.",
						selectedCount);
				ExtentCucumberAdapter.addTestStepLog("⚠️ Count Status: INCOMPLETE (max scroll limit reached)");
			} else {
				LOGGER.info("✅ COMPLETE COUNT FLAG SET: isProfilesCountComplete = true");
				LOGGER.info("✅ Successfully loaded and counted all {} profiles", selectedCount);
				ExtentCucumberAdapter.addTestStepLog("✅ Count Status: COMPLETE (all profiles loaded and counted)");
			}

		} catch (Exception e) {
			ScreenshotHandler
					.captureFailureScreenshot("verify_count_of_selected_profiles_by_scrolling_through_all_profiles", e);
			LOGGER.error(
					"Error getting selected profiles count in HCM Sync Profiles screen - Method: verify_count_of_selected_profiles_by_scrolling_through_all_profiles",
					e);
			ExtentCucumberAdapter.addTestStepLog(" Error getting selected profiles count");
		}
	}
}
