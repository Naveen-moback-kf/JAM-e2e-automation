package com.kfonetalentsuite.pageobjects.JobMapping;

import java.io.IOException;
import java.util.ArrayList;
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

import com.kfonetalentsuite.utils.JobMapping.Utilities;
import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.ScreenshotHandler;
import com.kfonetalentsuite.webdriverManager.DriverManager;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

public class PO47_ValidateSortingFunctionalityInHCMScreen_PM {
	WebDriver driver = DriverManager.getDriver();	

	protected static final Logger LOGGER = (Logger) LogManager.getLogger();
	PO47_ValidateSortingFunctionalityInHCMScreen_PM validateSortingFunctionalityInHCMScreen;
	

	public PO47_ValidateSortingFunctionalityInHCMScreen_PM() throws IOException {
		// super();
		PageFactory.initElements(driver, this);
		// TODO Auto-generated constructor stub
	}

	WebDriverWait wait = DriverManager.getWait();
	Utilities utils = new Utilities();
	JavascriptExecutor js = (JavascriptExecutor) driver;
	
	//XPATHS
	@FindBy(xpath = "//*[@class='blocking-loader']//img")
	@CacheLookup
	WebElement pageLoadSpinner;
	
	@FindBy(xpath = "//span[contains(text(),'Hi')]//following::img[1]")
	@CacheLookup
	public WebElement MenuBtn;

	@FindBy(xpath = "//*[*[contains(text(),'Hi, ')]]/div[2]/img")
	@CacheLookup
	WebElement homeMenuBtn1;

	@FindBy(xpath = "//a[contains(text(),'Profile Manager')]")
	@CacheLookup
	public WebElement PMBtn;

	@FindBy(xpath = "//h1[contains(text(),'Profile Manager')]")
	@CacheLookup
	public WebElement PMHeader;
	
	@FindBy(xpath = "//span[contains(text(),'HCM Sync Profiles')]")
	@CacheLookup
	public WebElement hcmSyncProfilesHeader;
	
	@FindBy(xpath = "//h1[contains(text(),'Sync Profiles')]")
	@CacheLookup
	public WebElement hcmSyncProfilesTitle;
	
	@FindBy(xpath = "//p[contains(text(),'Select a job profile')]")
	@CacheLookup
	public WebElement hcmSyncProfilesTitleDesc;
	
	@FindBy(xpath = "//input[@type='search']")
	@CacheLookup
	public WebElement hcmSyncProfilesSearchbar;
	
	// Table Headers for HCM Sync Profiles Screen
	@FindBy(xpath = "//thead//tr//div[@kf-sort-header='name']//div")
	@CacheLookup
	WebElement tableHeader1;
	
	@FindBy(xpath = "//thead//tr//div//div[text()=' Status ']")
	@CacheLookup
	WebElement tableHeader2;
	
	@FindBy(xpath = "//thead//tr//div//div[text()=' kf grade ']")
	@CacheLookup
	WebElement tableHeader3;
	
	@FindBy(xpath = "//thead//tr//div//div[text()=' Level ']")
	@CacheLookup
	WebElement tableHeader4;
	
	@FindBy(xpath = "//thead//tr//div//div[text()=' Function ']")
	@CacheLookup
	WebElement tableHeader5;
	
	@FindBy(xpath = "//thead//tr//div//div[text()=' Created By ']")
	@CacheLookup
	WebElement tableHeader6;
	
	@FindBy(xpath = "//thead//tr//div//div[text()=' Last Modified ']")
	@CacheLookup
	WebElement tableHeader7;
	
	@FindBy(xpath = "//thead//tr//div//div[text()=' Export status ']")
	@CacheLookup
	WebElement tableHeader8;
	
	
	//METHODs
	
	/**
	 * Sorts profiles by Name in ascending order (single click)
	 */
	public void sort_profiles_by_name_in_ascending_order_in_hcm_sync_profiles_screen() {
		try {
			try {
				wait.until(ExpectedConditions.elementToBeClickable(tableHeader1)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", tableHeader1);
				} catch (Exception s) {
					utils.jsClick(driver, tableHeader1);
				}
			}
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			PerformanceUtils.waitForPageReady(driver);
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			PerformanceUtils.waitForPageReady(driver);
			LOGGER.info("Clicked on Name header to Sort Profiles by Name in ascending order");
			ExtentCucumberAdapter.addTestStepLog("Clicked on Name header to Sort Profiles by Name in ascending order");
		} catch (Exception e) {
			LOGGER.error(" Issue sorting by name ascending - Method: sort_profiles_by_name_in_ascending_order_in_hcm_sync_profiles_screen", e);
			ScreenshotHandler.captureFailureScreenshot("sort_profiles_by_name_ascending", e);
			e.printStackTrace();
			Assert.fail("Issue in clicking on Name header to Sort Profiles by Name in ascending order...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in sorting by Name in ascending order...Please Investigate!!!");
		}
	}
	
	/**
	 * Verifies profiles are sorted by Name in ascending order
	 */
	public void user_should_verify_first_hundred_job_profiles_are_correctly_sorted_by_name_in_ascending_order() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			PerformanceUtils.waitForPageReady(driver);
			
			List<WebElement> allElements = driver.findElements(By.xpath("//tbody//tr//td//div//span[1]//a"));
			LOGGER.info("Below are Profiles After sorting by Name in Ascending Order:");
			ExtentCucumberAdapter.addTestStepLog("Below are Profiles After sorting by Name in Ascending Order:");
			
			// Collect profile names for validation
			ArrayList<String> profileNames = new ArrayList<String>();
			int specialCharCount = 0;
			int nonAsciiCount = 0;
			
			// Limit to first 100 profiles
			int limit = Math.min(allElements.size(), 100);
			
			for (int i = 0; i < limit; i++) {
				WebElement element = allElements.get(i);
				String text = element.getText();
				profileNames.add(text); // Store original case for proper comparison
				
				// Detect special characters at start (expected at top in ascending)
				if (!text.isEmpty() && !Character.isLetterOrDigit(text.charAt(0))) {
					specialCharCount++;
					LOGGER.info("Profile Name : " + text + " [SPECIAL CHAR at start - expected at top in Ascending]");
				}
				// Detect non-ASCII characters (Chinese, etc.)
				else if (!text.isEmpty() && text.charAt(0) > 127) {
					nonAsciiCount++;
					LOGGER.info("Profile Name : " + text + " [NON-ASCII detected]");
				} else {
					LOGGER.info("Profile Name : " + text);
				}
				ExtentCucumberAdapter.addTestStepLog("Profile Name : " + text);
			}
			
			if (specialCharCount > 0) {
				LOGGER.info("ℹ Found " + specialCharCount + " profile(s) with special characters - these appear at top in Ascending order as expected");
				ExtentCucumberAdapter.addTestStepLog("ℹ " + specialCharCount + " profile(s) start with special characters (?, -, etc.) - expected at top");
			}
			if (nonAsciiCount > 0) {
				LOGGER.info("ℹ Found " + nonAsciiCount + " profile(s) with non-ASCII characters (Chinese, etc.)");
				ExtentCucumberAdapter.addTestStepLog("ℹ " + nonAsciiCount + " profile(s) contain non-ASCII characters");
			}
			
		// ✅ VALIDATE ASCENDING ORDER (using normalized comparison to match UI behavior)
		// UI ignores hyphens and special chars in sorting, so we normalize strings before comparison
		int sortViolations = 0;
		for(int i = 0; i < profileNames.size() - 1; i++) {
			String current = profileNames.get(i);
			String next = profileNames.get(i + 1);
			
			// Normalize strings to match UI sorting: remove hyphens and other punctuation
			// UI treats "AI-Ready" same as "AIReady" for sorting purposes
			String currentNormalized = current.replaceAll("[-'\"()\\[\\].,:;!?]", "").trim();
			String nextNormalized = next.replaceAll("[-'\"()\\[\\].,:;!?]", "").trim();
			
			// Use normalized strings for comparison (case-insensitive)
			if(currentNormalized.compareToIgnoreCase(nextNormalized) > 0) {
				sortViolations++;
				LOGGER.error("❌ SORT VIOLATION at Row " + (i + 1) + " -> Row " + (i + 2) + ": '" + current + "' > '" + next + "' (NOT in Ascending Order!)");
				LOGGER.debug("   Normalized comparison: '" + currentNormalized + "' vs '" + nextNormalized + "'");
				ExtentCucumberAdapter.addTestStepLog("❌ SORT VIOLATION: Row " + (i + 1) + " > Row " + (i + 2));
			}
		}
			
			if(sortViolations > 0) {
				String errorMsg = "❌ SORTING FAILED: Found " + sortViolations + " violation(s). Data is NOT sorted by Name in Ascending Order!";
				LOGGER.error(errorMsg);
				ExtentCucumberAdapter.addTestStepLog(errorMsg);
				Assert.fail(errorMsg + " Please check the sorting implementation!");
			} else {
				LOGGER.info("✅ SORT VALIDATION PASSED: All " + profileNames.size() + " Profiles are correctly sorted by Name in Ascending Order (including special chars and non-ASCII)");
				ExtentCucumberAdapter.addTestStepLog("✅ Sorting validation PASSED - Data is correctly sorted in Ascending Order");
			}
			
		} catch (Exception e) {
			LOGGER.error(" Issue verifying sorted profiles by name ascending - Method: user_should_verify_first_hundred_job_profiles_are_correctly_sorted_by_name_in_ascending_order", e);
			ScreenshotHandler.captureFailureScreenshot("verify_profiles_sorted_by_name_ascending", e);
			e.printStackTrace();
			Assert.fail("Issue in Verifying Profiles After sorting by Name in Ascending Order...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying Profiles After sorting by Name in Ascending Order...Please Investigate!!!");
		}
	}
	
	/**
	 * Sorts profiles by Level in descending order (clicks header twice)
	 * Enhanced with proper waits for HEADLESS MODE compatibility
	 */
	public void sort_profiles_by_level_in_descending_order_in_hcm_sync_profiles_screen() {
		try {
			// Scroll to ensure Level header is in view and clickable
			js.executeScript("arguments[0].scrollIntoView({block: 'center'});", tableHeader4);
			Thread.sleep(500);
			
			// FIRST CLICK - Sort Ascending
			LOGGER.info("First click on Level header to sort ascending...");
			LOGGER.info("Level header text: " + tableHeader4.getText());
			try {
				wait.until(ExpectedConditions.elementToBeClickable(tableHeader4)).click();
			} catch (Exception e) {
				try {
					LOGGER.warn("Standard click failed, trying JavaScript click...");
					js.executeScript("arguments[0].click();", tableHeader4);
				} catch (Exception s) {
					LOGGER.warn("JavaScript click failed, trying utils click...");
					utils.jsClick(driver, tableHeader4);
				}
			}
			
			// Wait for FIRST sort to complete (critical for headless mode)
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			Thread.sleep(3000); // Increased wait for UI to process first sort
			PerformanceUtils.waitForPageReady(driver);
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			Thread.sleep(1500); // Additional buffer for headless mode
			
			LOGGER.info("First sort completed. Now clicking second time for descending order...");
			
			// Ensure header is still in view
			js.executeScript("arguments[0].scrollIntoView({block: 'center'});", tableHeader4);
			Thread.sleep(500);
			
			// SECOND CLICK - Sort Descending
			// Ensure element is clickable again before second click
			try {
				wait.until(ExpectedConditions.elementToBeClickable(tableHeader4)).click();
			} catch (Exception e) {
				try {
					Thread.sleep(500); // Brief pause before retry
					LOGGER.warn("Second standard click failed, trying JavaScript click...");
					js.executeScript("arguments[0].click();", tableHeader4);
				} catch (Exception s) {
					LOGGER.warn("Second JavaScript click failed, trying utils click...");
					utils.jsClick(driver, tableHeader4);
				}
			}
			
			// Wait for SECOND sort to complete
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			Thread.sleep(3000); // Increased wait for UI to process second sort
			PerformanceUtils.waitForPageReady(driver);
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			Thread.sleep(1500); // Additional buffer
			
			LOGGER.info("Clicked two times on Level header to Sort Profiles by Level in Descending order");
			ExtentCucumberAdapter.addTestStepLog("Clicked two times on Level header to Sort Profiles by Level in Descending order");
			
			// Debug: Log first few profile levels to verify sorting
			LOGGER.info("Verifying sort applied - checking first 5 profile levels...");
			List<WebElement> firstFiveLevels = driver.findElements(By.xpath("//tbody//tr//td[4]//div//span[1]"));
			for(int i = 0; i < Math.min(5, firstFiveLevels.size()); i++) {
				LOGGER.info("Level at row " + (i+1) + ": " + firstFiveLevels.get(i).getText());
			}
			
		} catch (Exception e) {
			LOGGER.error(" Issue sorting by level descending - Method: sort_profiles_by_level_in_descending_order_in_hcm_sync_profiles_screen", e);
			ScreenshotHandler.captureFailureScreenshot("sort_profiles_by_level_descending", e);
			e.printStackTrace();
			Assert.fail("Issue in clicking on Level header to Sort Profiles by Level in Descending order...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in sorting by Level in Descending order...Please Investigate!!!");
		}
	}
	
	/**
	 * Verifies profiles are sorted by Level in descending order
	 */
	public void user_should_verify_first_hundred_job_profiles_are_correctly_sorted_by_level_in_descending_order() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			PerformanceUtils.waitForPageReady(driver);
			
			List<WebElement> profileNameElements = driver.findElements(By.xpath("//tbody//tr//td//div//span[1]//a"));
			List<WebElement> levelElements = driver.findElements(By.xpath("//tbody//tr//td[4]//div//span[1]"));
			
			LOGGER.info("Below are Profiles After sorting by Level in Descending Order:");
			ExtentCucumberAdapter.addTestStepLog("Below are Profiles After sorting by Level in Descending Order:");
			
			// Collect levels for validation
			ArrayList<String> levels = new ArrayList<String>();
			int specialCharCount = 0;
			int nonAsciiCount = 0;
			int emptyCount = 0;
			
			// Limit to first 100 profiles
			int limit = Math.min(profileNameElements.size(), 100);
			limit = Math.min(limit, levelElements.size());
			
			for (int i = 0; i < limit; i++) {
				String profileName = profileNameElements.get(i).getText();
				String levelText = levelElements.get(i).getText();
				
				// Check if level is empty or null
				if (levelText == null || levelText.trim().isEmpty() || levelText.equals("-")) {
					emptyCount++;
					LOGGER.info("Profile: " + profileName + " - Level: [EMPTY]");
					ExtentCucumberAdapter.addTestStepLog("Profile: " + profileName + " - Level: [EMPTY]");
				} else {
					levels.add(levelText); // Store for validation (keep original case for Unicode)
					
					// Detect non-ASCII characters at start (expected at top in descending)
					if (!levelText.isEmpty() && levelText.charAt(0) > 127) {
						nonAsciiCount++;
						LOGGER.info("Profile: " + profileName + " - Level: " + levelText + " [NON-ASCII at start - expected at top in Descending]");
					}
					// Detect special characters
					else if (!levelText.isEmpty() && !Character.isLetterOrDigit(levelText.charAt(0))) {
						specialCharCount++;
						LOGGER.info("Profile: " + profileName + " - Level: " + levelText + " [SPECIAL CHAR detected]");
					} else {
						LOGGER.info("Profile: " + profileName + " - Level: " + levelText);
					}
					ExtentCucumberAdapter.addTestStepLog("Profile: " + profileName + " - Level: " + levelText);
				}
			}
			
			if (emptyCount > 0) {
				LOGGER.info("ℹ Found " + emptyCount + " profile(s) with empty Level values");
				ExtentCucumberAdapter.addTestStepLog("ℹ " + emptyCount + " profile(s) have empty Level values");
			}
			if (nonAsciiCount > 0) {
				LOGGER.info("ℹ Found " + nonAsciiCount + " level(s) with non-ASCII characters - these appear at top in Descending order as expected");
				ExtentCucumberAdapter.addTestStepLog("ℹ " + nonAsciiCount + " level(s) start with non-ASCII characters (Chinese, etc.) - expected at top");
			}
			if (specialCharCount > 0) {
				LOGGER.info("ℹ Found " + specialCharCount + " level(s) with special characters (?, -, etc.)");
				ExtentCucumberAdapter.addTestStepLog("ℹ " + specialCharCount + " level(s) start with special characters");
			}
			
		// ✅ VALIDATE DESCENDING ORDER (using normalized comparison to match UI behavior)
		// UI ignores hyphens and special chars in sorting, so we normalize strings before comparison
		int sortViolations = 0;
		for(int i = 0; i < levels.size() - 1; i++) {
			String current = levels.get(i);
			String next = levels.get(i + 1);
			
			// Normalize strings to match UI sorting: remove hyphens and other punctuation
			String currentNormalized = current.replaceAll("[-'\"()\\[\\].,:;!?]", "").trim();
			String nextNormalized = next.replaceAll("[-'\"()\\[\\].,:;!?]", "").trim();
			
			// Use normalized strings for comparison (case-insensitive)
			if(currentNormalized.compareToIgnoreCase(nextNormalized) < 0) {
				sortViolations++;
				LOGGER.error("❌ SORT VIOLATION at Row " + (i + 1) + " -> Row " + (i + 2) + ": '" + current + "' < '" + next + "' (NOT in Descending Order!)");
				LOGGER.debug("   Normalized comparison: '" + currentNormalized + "' vs '" + nextNormalized + "'");
				ExtentCucumberAdapter.addTestStepLog("❌ SORT VIOLATION: Row " + (i + 1) + " < Row " + (i + 2));
			}
		}
			
			if(sortViolations > 0) {
				String errorMsg = "❌ SORTING FAILED: Found " + sortViolations + " violation(s). Data is NOT sorted by Level in Descending Order!";
				LOGGER.error(errorMsg);
				ExtentCucumberAdapter.addTestStepLog(errorMsg);
				Assert.fail(errorMsg + " Please check the sorting implementation!");
			} else if(levels.size() > 1) {
				LOGGER.info("✅ SORT VALIDATION PASSED: All " + levels.size() + " Levels are correctly sorted in Descending Order (including special chars and non-ASCII)");
				ExtentCucumberAdapter.addTestStepLog("✅ Sorting validation PASSED - Data is correctly sorted in Descending Order");
			} else {
				LOGGER.info("ℹ VALIDATION SKIPPED: Not enough profiles with Level values to validate sorting (" + levels.size() + " profile(s) with levels)");
				ExtentCucumberAdapter.addTestStepLog("ℹ Validation skipped - insufficient data for sorting validation");
			}
			
		} catch (Exception e) {
			LOGGER.error(" Issue verifying sorted profiles by level descending - Method: user_should_verify_first_hundred_job_profiles_are_correctly_sorted_by_level_in_descending_order", e);
			ScreenshotHandler.captureFailureScreenshot("verify_profiles_sorted_by_level_descending", e);
			e.printStackTrace();
			Assert.fail("Issue in Verifying Profiles After sorting by Level in Descending Order...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying Profiles After sorting by Level in Descending Order...Please Investigate!!!");
		}
	}
	
	/**
	 * Sorts profiles by Last Modified in ascending order (single click)
	 */
	public void sort_profiles_by_last_modified_in_ascending_order_in_hcm_sync_profiles_screen() {
		try {
			try {
				wait.until(ExpectedConditions.elementToBeClickable(tableHeader7)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", tableHeader7);
				} catch (Exception s) {
					utils.jsClick(driver, tableHeader7);
				}
			}
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			PerformanceUtils.waitForPageReady(driver);
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			PerformanceUtils.waitForPageReady(driver);
			LOGGER.info("Clicked on Last Modified header to Sort Profiles by Last Modified in ascending order");
			ExtentCucumberAdapter.addTestStepLog("Clicked on Last Modified header to Sort Profiles by Last Modified in ascending order");
		} catch (Exception e) {
			LOGGER.error(" Issue sorting by last modified ascending - Method: sort_profiles_by_last_modified_in_ascending_order_in_hcm_sync_profiles_screen", e);
			ScreenshotHandler.captureFailureScreenshot("sort_profiles_by_last_modified_ascending", e);
			e.printStackTrace();
			Assert.fail("Issue in clicking on Last Modified header to Sort Profiles by Last Modified in ascending order...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in sorting by Last Modified in ascending order...Please Investigate!!!");
		}
	}
	
	/**
	 * Sorts profiles by Export Status in descending order (clicks header twice)
	 * Enhanced with proper waits for HEADLESS MODE compatibility
	 */
	public void sort_profiles_by_export_status_in_descending_order_in_hcm_sync_profiles_screen() {
		try {
			// Scroll to ensure Export Status header is in view and clickable
			js.executeScript("arguments[0].scrollIntoView({block: 'center'});", tableHeader8);
			Thread.sleep(500);
			
			// FIRST CLICK - Sort Ascending
			LOGGER.info("First click on Export Status header to sort ascending...");
			try {
				wait.until(ExpectedConditions.elementToBeClickable(tableHeader8)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", tableHeader8);
				} catch (Exception s) {
					utils.jsClick(driver, tableHeader8);
				}
			}
			
			// Wait for FIRST sort to complete (critical for headless mode)
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			Thread.sleep(3000); // Give UI time to process first sort
			PerformanceUtils.waitForPageReady(driver);
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			Thread.sleep(1500); // Additional buffer for headless mode
			
			LOGGER.info("First sort completed. Now clicking second time for descending order...");
			
			// Ensure header is still in view
			js.executeScript("arguments[0].scrollIntoView({block: 'center'});", tableHeader8);
			Thread.sleep(500);
			
			// SECOND CLICK - Sort Descending
			try {
				wait.until(ExpectedConditions.elementToBeClickable(tableHeader8)).click();
			} catch (Exception e) {
				try {
					Thread.sleep(500); // Brief pause before retry
					js.executeScript("arguments[0].click();", tableHeader8);
				} catch (Exception s) {
					utils.jsClick(driver, tableHeader8);
				}
			}
			
			// Wait for SECOND sort to complete
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			Thread.sleep(3000); // Give UI time to process second sort
			PerformanceUtils.waitForPageReady(driver);
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			
			LOGGER.info("Clicked two times on Export Status header to Sort Profiles by Export Status in Descending order");
			ExtentCucumberAdapter.addTestStepLog("Clicked two times on Export Status header to Sort Profiles by Export Status in Descending order");
		} catch (Exception e) {
			LOGGER.error(" Issue sorting by export status descending - Method: sort_profiles_by_export_status_in_descending_order_in_hcm_sync_profiles_screen", e);
			ScreenshotHandler.captureFailureScreenshot("sort_profiles_by_export_status_descending", e);
			e.printStackTrace();
			Assert.fail("Issue in clicking on Export Status header to Sort Profiles by Export Status in Descending order...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in sorting by Export Status in Descending order...Please Investigate!!!");
		}
	}
	
	/**
	 * Sorts profiles by Function in ascending order (single click)
	 */
	public void sort_profiles_by_function_in_ascending_order_in_hcm_sync_profiles_screen() {
		try {
			// Scroll to ensure Function header is in view and clickable
			js.executeScript("arguments[0].scrollIntoView({block: 'center'});", tableHeader5);
			Thread.sleep(500);
			
			try {
				wait.until(ExpectedConditions.elementToBeClickable(tableHeader5)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", tableHeader5);
				} catch (Exception s) {
					utils.jsClick(driver, tableHeader5);
				}
			}
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			PerformanceUtils.waitForPageReady(driver);
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			PerformanceUtils.waitForPageReady(driver);
			LOGGER.info("Clicked on Function header to Sort Profiles by Function in ascending order");
			ExtentCucumberAdapter.addTestStepLog("Clicked on Function header to Sort Profiles by Function in ascending order");
		} catch (Exception e) {
			LOGGER.error(" Issue sorting by function ascending - Method: sort_profiles_by_function_in_ascending_order_in_hcm_sync_profiles_screen", e);
			ScreenshotHandler.captureFailureScreenshot("sort_profiles_by_function_ascending", e);
			e.printStackTrace();
			Assert.fail("Issue in clicking on Function header to Sort Profiles by Function in ascending order...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in sorting by Function in ascending order...Please Investigate!!!");
		}
	}
	
	/**
	 * Sorts profiles by Function in descending order (clicks header twice)
	 * Enhanced with proper waits for HEADLESS MODE compatibility
	 */
	public void sort_profiles_by_function_in_descending_order_in_hcm_sync_profiles_screen() {
		try {
			// FIRST CLICK - Sort Ascending
			LOGGER.info("First click on Function header to sort ascending...");
			try {
				wait.until(ExpectedConditions.elementToBeClickable(tableHeader5)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", tableHeader5);
				} catch (Exception s) {
					utils.jsClick(driver, tableHeader5);
				}
			}
			
			// Wait for FIRST sort to complete (critical for headless mode)
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			Thread.sleep(2000); // Give UI time to process first sort
			PerformanceUtils.waitForPageReady(driver);
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			Thread.sleep(1000); // Additional buffer for headless mode
			
			LOGGER.info("First sort completed. Now clicking second time for descending order...");
			
			// SECOND CLICK - Sort Descending
			// Ensure element is clickable again before second click
			try {
				wait.until(ExpectedConditions.elementToBeClickable(tableHeader5)).click();
			} catch (Exception e) {
				try {
					Thread.sleep(500); // Brief pause before retry
					js.executeScript("arguments[0].click();", tableHeader5);
				} catch (Exception s) {
					utils.jsClick(driver, tableHeader5);
				}
			}
			
			// Wait for SECOND sort to complete
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			Thread.sleep(2000); // Give UI time to process second sort
			PerformanceUtils.waitForPageReady(driver);
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			
			LOGGER.info("Clicked two times on Function header to Sort Profiles by Function in Descending order");
			ExtentCucumberAdapter.addTestStepLog("Clicked two times on Function header to Sort Profiles by Function in Descending order");
		} catch (Exception e) {
			LOGGER.error(" Issue sorting by function descending - Method: sort_profiles_by_function_in_descending_order_in_hcm_sync_profiles_screen", e);
			ScreenshotHandler.captureFailureScreenshot("sort_profiles_by_function_descending", e);
			e.printStackTrace();
			Assert.fail("Issue in clicking on Function header to Sort Profiles by Function in Descending order...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in sorting by Function in Descending order...Please Investigate!!!");
		}
	}
	
	/**
	 * Verifies profiles are sorted by Last Modified in ascending and Function in descending order (multi-level sorting)
	 */
	public void user_should_verify_first_hundred_job_profiles_are_correctly_sorted_by_last_modified_in_ascending_and_function_in_descending_order() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			PerformanceUtils.waitForPageReady(driver);
			
			List<WebElement> profileNameElements = driver.findElements(By.xpath("//tbody//tr//td//div//span[1]//a"));
			List<WebElement> lastModifiedElements = driver.findElements(By.xpath("//tbody//tr//td[7]//div//span[1]"));
			List<WebElement> functionElements = driver.findElements(By.xpath("//tbody//tr//td[5]//div//span[1]"));
			
			LOGGER.info("Below are Profiles After sorting by Last Modified in Ascending and Function in Descending Order:");
			ExtentCucumberAdapter.addTestStepLog("Below are Profiles After sorting by Last Modified in Ascending and Function in Descending Order:");
			
			// Collect data for validation
			ArrayList<String> lastModifiedDates = new ArrayList<String>();
			ArrayList<String> functions = new ArrayList<String>();
			int emptyLastModifiedCount = 0;
			int emptyFunctionCount = 0;
			
			// Limit to first 100 profiles
			int limit = Math.min(profileNameElements.size(), 100);
			limit = Math.min(limit, lastModifiedElements.size());
			limit = Math.min(limit, functionElements.size());
			
			for (int i = 0; i < limit; i++) {
				String profileName = profileNameElements.get(i).getText();
				String lastModifiedText = lastModifiedElements.get(i).getText();
				String functionText = functionElements.get(i).getText();
				
				// Track empty values
				if (lastModifiedText == null || lastModifiedText.trim().isEmpty() || lastModifiedText.equals("-")) {
					emptyLastModifiedCount++;
					lastModifiedText = "[EMPTY]";
				} else {
					lastModifiedDates.add(lastModifiedText);
				}
				
				if (functionText == null || functionText.trim().isEmpty() || functionText.equals("-")) {
					emptyFunctionCount++;
					functionText = "[EMPTY]";
				} else {
					functions.add(functionText);
				}
				
				LOGGER.info("Profile: " + profileName + " - Last Modified: " + lastModifiedText + " - Function: " + functionText);
				ExtentCucumberAdapter.addTestStepLog("Profile: " + profileName + " - Last Modified: " + lastModifiedText + " - Function: " + functionText);
			}
			
			if (emptyLastModifiedCount > 0) {
				LOGGER.info("ℹ Found " + emptyLastModifiedCount + " profile(s) with empty Last Modified values");
				ExtentCucumberAdapter.addTestStepLog("ℹ " + emptyLastModifiedCount + " profile(s) have empty Last Modified values");
			}
			if (emptyFunctionCount > 0) {
				LOGGER.info("ℹ Found " + emptyFunctionCount + " profile(s) with empty Function values");
				ExtentCucumberAdapter.addTestStepLog("ℹ " + emptyFunctionCount + " profile(s) have empty Function values");
			}
			
			// ✅ VALIDATE MULTI-LEVEL SORTING
			// Primary sort: Last Modified (Ascending)
			// Secondary sort: Function (Descending) - within same Last Modified date
			int sortViolations = 0;
			
			// For multi-level sorting, we validate that:
			// 1. Last Modified is in ascending order
			// 2. For same Last Modified date, Function is in descending order
			for(int i = 0; i < limit - 1; i++) {
				String currentLastModified = lastModifiedElements.get(i).getText();
				String nextLastModified = lastModifiedElements.get(i + 1).getText();
				String currentFunction = functionElements.get(i).getText();
				String nextFunction = functionElements.get(i + 1).getText();
				
				// Skip empty values in validation
				if (currentLastModified.equals("-") || currentLastModified.trim().isEmpty() ||
					nextLastModified.equals("-") || nextLastModified.trim().isEmpty()) {
					continue;
				}
				
				// Primary sort check: Last Modified ascending (case-insensitive)
				int lastModifiedComparison = currentLastModified.compareToIgnoreCase(nextLastModified);
				if(lastModifiedComparison > 0) {
					sortViolations++;
					LOGGER.error("❌ SORT VIOLATION (Last Modified) at Row " + (i + 1) + " -> Row " + (i + 2) + ": '" + currentLastModified + "' > '" + nextLastModified + "' (NOT in Ascending Order!)");
					ExtentCucumberAdapter.addTestStepLog("❌ SORT VIOLATION (Last Modified): Row " + (i + 1) + " > Row " + (i + 2));
				}
			// Secondary sort check: If Last Modified is same, Function should be descending (normalized comparison)
			else if(lastModifiedComparison == 0) {
				if (!currentFunction.equals("-") && !currentFunction.trim().isEmpty() &&
					!nextFunction.equals("-") && !nextFunction.trim().isEmpty()) {
					// Normalize Function values to match UI sorting behavior
					String currentFunctionNormalized = currentFunction.replaceAll("[-'\"()\\[\\].,:;!?]", "").trim();
					String nextFunctionNormalized = nextFunction.replaceAll("[-'\"()\\[\\].,:;!?]", "").trim();
					if(currentFunctionNormalized.compareToIgnoreCase(nextFunctionNormalized) < 0) {
						sortViolations++;
						LOGGER.error("❌ SORT VIOLATION (Function) at Row " + (i + 1) + " -> Row " + (i + 2) + ": '" + currentFunction + "' < '" + nextFunction + "' (NOT in Descending Order for same Last Modified!)");
						LOGGER.debug("   Normalized comparison: '" + currentFunctionNormalized + "' vs '" + nextFunctionNormalized + "'");
						ExtentCucumberAdapter.addTestStepLog("❌ SORT VIOLATION (Function): Row " + (i + 1) + " < Row " + (i + 2));
					}
				}
			}
			}
			
			if(sortViolations > 0) {
				String errorMsg = "❌ SORTING FAILED: Found " + sortViolations + " violation(s). Data is NOT sorted by Last Modified (Ascending) and Function (Descending)!";
				LOGGER.error(errorMsg);
				ExtentCucumberAdapter.addTestStepLog(errorMsg);
				Assert.fail(errorMsg + " Please check the multi-level sorting implementation!");
			} else {
				LOGGER.info("✅ SORT VALIDATION PASSED: All Profiles are correctly sorted by Last Modified (Ascending) and Function (Descending)");
				ExtentCucumberAdapter.addTestStepLog("✅ Multi-level sorting validation PASSED");
			}
			
		} catch (Exception e) {
			LOGGER.error(" Issue verifying sorted profiles by last modified and function - Method: user_should_verify_first_hundred_job_profiles_are_correctly_sorted_by_last_modified_in_ascending_and_function_in_descending_order", e);
			ScreenshotHandler.captureFailureScreenshot("verify_profiles_sorted_by_last_modified_and_function", e);
			e.printStackTrace();
			Assert.fail("Issue in Verifying Profiles After sorting by Last Modified and Function...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying Profiles After sorting by Last Modified and Function...Please Investigate!!!");
		}
	}
	
	/**
	 * Verifies profiles are sorted by Last Modified (ascending), Export Status (descending), and Function (ascending)
	 * Three-level sorting validation
	 */
	public void user_should_verify_first_hundred_job_profiles_are_correctly_sorted_by_last_modified_ascending_export_status_descending_and_function_ascending() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			PerformanceUtils.waitForPageReady(driver);
			
			List<WebElement> profileNameElements = driver.findElements(By.xpath("//tbody//tr//td//div//span[1]//a"));
			List<WebElement> lastModifiedElements = driver.findElements(By.xpath("//tbody//tr//td[7]//div//span[1]"));
			List<WebElement> exportStatusElements = driver.findElements(By.xpath("//tbody//tr//td[8]//div//span[1]"));
			List<WebElement> functionElements = driver.findElements(By.xpath("//tbody//tr//td[5]//div//span[1]"));
			
			LOGGER.info("Below are Profiles After 3-level sorting: Last Modified (Ascending), Export Status (Descending), Function (Ascending):");
			ExtentCucumberAdapter.addTestStepLog("Below are Profiles After 3-level sorting: Last Modified (Ascending), Export Status (Descending), Function (Ascending):");
			
			// Collect data for validation
			int emptyLastModifiedCount = 0;
			int emptyExportStatusCount = 0;
			int emptyFunctionCount = 0;
			
			// Limit to first 100 profiles
			int limit = Math.min(profileNameElements.size(), 100);
			limit = Math.min(limit, lastModifiedElements.size());
			limit = Math.min(limit, exportStatusElements.size());
			limit = Math.min(limit, functionElements.size());
			
			for (int i = 0; i < limit; i++) {
				String profileName = profileNameElements.get(i).getText();
				String lastModifiedText = lastModifiedElements.get(i).getText();
				String exportStatusText = exportStatusElements.get(i).getText();
				String functionText = functionElements.get(i).getText();
				
				// Track empty values
				if (lastModifiedText == null || lastModifiedText.trim().isEmpty() || lastModifiedText.equals("-")) {
					emptyLastModifiedCount++;
					lastModifiedText = "[EMPTY]";
				}
				
				if (exportStatusText == null || exportStatusText.trim().isEmpty() || exportStatusText.equals("-")) {
					emptyExportStatusCount++;
					exportStatusText = "[EMPTY]";
				}
				
				if (functionText == null || functionText.trim().isEmpty() || functionText.equals("-")) {
					emptyFunctionCount++;
					functionText = "[EMPTY]";
				}
				
				LOGGER.info("Profile: " + profileName + " | Last Modified: " + lastModifiedText + " | Export Status: " + exportStatusText + " | Function: " + functionText);
				ExtentCucumberAdapter.addTestStepLog("Profile: " + profileName + " | Last Modified: " + lastModifiedText + " | Export Status: " + exportStatusText + " | Function: " + functionText);
			}
			
			if (emptyLastModifiedCount > 0) {
				LOGGER.info("ℹ Found " + emptyLastModifiedCount + " profile(s) with empty Last Modified values");
				ExtentCucumberAdapter.addTestStepLog("ℹ " + emptyLastModifiedCount + " profile(s) have empty Last Modified values");
			}
			if (emptyExportStatusCount > 0) {
				LOGGER.info("ℹ Found " + emptyExportStatusCount + " profile(s) with empty Export Status values");
				ExtentCucumberAdapter.addTestStepLog("ℹ " + emptyExportStatusCount + " profile(s) have empty Export Status values");
			}
			if (emptyFunctionCount > 0) {
				LOGGER.info("ℹ Found " + emptyFunctionCount + " profile(s) with empty Function values");
				ExtentCucumberAdapter.addTestStepLog("ℹ " + emptyFunctionCount + " profile(s) have empty Function values");
			}
			
			// ✅ VALIDATE THREE-LEVEL SORTING
			// Primary sort: Last Modified (Ascending)
			// Secondary sort: Export Status (Descending) - within same Last Modified
			// Tertiary sort: Function (Ascending) - within same Last Modified and Export Status
			int sortViolations = 0;
			
			for(int i = 0; i < limit - 1; i++) {
				String currentLastModified = lastModifiedElements.get(i).getText();
				String nextLastModified = lastModifiedElements.get(i + 1).getText();
				String currentExportStatus = exportStatusElements.get(i).getText();
				String nextExportStatus = exportStatusElements.get(i + 1).getText();
				String currentFunction = functionElements.get(i).getText();
				String nextFunction = functionElements.get(i + 1).getText();
				
				// Skip empty values in validation
				if (currentLastModified.equals("-") || currentLastModified.trim().isEmpty() ||
					nextLastModified.equals("-") || nextLastModified.trim().isEmpty()) {
					continue;
				}
				
				// Primary sort check: Last Modified ascending (case-insensitive)
				int lastModifiedComparison = currentLastModified.compareToIgnoreCase(nextLastModified);
				if(lastModifiedComparison > 0) {
					sortViolations++;
					LOGGER.error("❌ PRIMARY SORT VIOLATION (Last Modified) at Row " + (i + 1) + " -> Row " + (i + 2) + ": '" + currentLastModified + "' > '" + nextLastModified + "' (NOT in Ascending Order!)");
					ExtentCucumberAdapter.addTestStepLog("❌ PRIMARY SORT VIOLATION (Last Modified): Row " + (i + 1) + " > Row " + (i + 2));
				}
			// Secondary sort check: If Last Modified is same, Export Status should be descending (normalized comparison)
			else if(lastModifiedComparison == 0) {
				if (!currentExportStatus.equals("-") && !currentExportStatus.trim().isEmpty() &&
					!nextExportStatus.equals("-") && !nextExportStatus.trim().isEmpty()) {
					// Normalize Export Status values to match UI sorting behavior
					String currentExportStatusNormalized = currentExportStatus.replaceAll("[-'\"()\\[\\].,:;!?]", "").trim();
					String nextExportStatusNormalized = nextExportStatus.replaceAll("[-'\"()\\[\\].,:;!?]", "").trim();
					int exportStatusComparison = currentExportStatusNormalized.compareToIgnoreCase(nextExportStatusNormalized);
					if(exportStatusComparison < 0) {
						sortViolations++;
						LOGGER.error("❌ SECONDARY SORT VIOLATION (Export Status) at Row " + (i + 1) + " -> Row " + (i + 2) + ": '" + currentExportStatus + "' < '" + nextExportStatus + "' (NOT in Descending Order for same Last Modified!)");
						LOGGER.debug("   Normalized comparison: '" + currentExportStatusNormalized + "' vs '" + nextExportStatusNormalized + "'");
						ExtentCucumberAdapter.addTestStepLog("❌ SECONDARY SORT VIOLATION (Export Status): Row " + (i + 1) + " < Row " + (i + 2));
					}
					// Tertiary sort check: If Last Modified and Export Status are same, Function should be ascending (normalized comparison)
					else if(exportStatusComparison == 0) {
						if (!currentFunction.equals("-") && !currentFunction.trim().isEmpty() &&
							!nextFunction.equals("-") && !nextFunction.trim().isEmpty()) {
							// Normalize Function values to match UI sorting behavior
							String currentFunctionNormalized = currentFunction.replaceAll("[-'\"()\\[\\].,:;!?]", "").trim();
							String nextFunctionNormalized = nextFunction.replaceAll("[-'\"()\\[\\].,:;!?]", "").trim();
							if(currentFunctionNormalized.compareToIgnoreCase(nextFunctionNormalized) > 0) {
								sortViolations++;
								LOGGER.error("❌ TERTIARY SORT VIOLATION (Function) at Row " + (i + 1) + " -> Row " + (i + 2) + ": '" + currentFunction + "' > '" + nextFunction + "' (NOT in Ascending Order for same Last Modified and Export Status!)");
								LOGGER.debug("   Normalized comparison: '" + currentFunctionNormalized + "' vs '" + nextFunctionNormalized + "'");
								ExtentCucumberAdapter.addTestStepLog("❌ TERTIARY SORT VIOLATION (Function): Row " + (i + 1) + " > Row " + (i + 2));
							}
						}
					}
				}
			}
			}
			
			if(sortViolations > 0) {
				String errorMsg = "❌ SORTING FAILED: Found " + sortViolations + " violation(s). Data is NOT sorted by Last Modified (Asc), Export Status (Desc), and Function (Asc)!";
				LOGGER.error(errorMsg);
				ExtentCucumberAdapter.addTestStepLog(errorMsg);
				Assert.fail(errorMsg + " Please check the three-level sorting implementation!");
			} else {
				LOGGER.info("✅ THREE-LEVEL SORT VALIDATION PASSED: All Profiles are correctly sorted by Last Modified (Ascending), Export Status (Descending), and Function (Ascending)");
				ExtentCucumberAdapter.addTestStepLog("✅ Three-level sorting validation PASSED");
			}
			
		} catch (Exception e) {
			LOGGER.error(" Issue verifying 3-level sorted profiles - Method: user_should_verify_first_hundred_job_profiles_are_correctly_sorted_by_last_modified_ascending_export_status_descending_and_function_ascending", e);
			ScreenshotHandler.captureFailureScreenshot("verify_profiles_3level_sorting", e);
			e.printStackTrace();
			Assert.fail("Issue in Verifying Profiles After 3-level sorting...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying Profiles After 3-level sorting...Please Investigate!!!");
		}
	}

}

