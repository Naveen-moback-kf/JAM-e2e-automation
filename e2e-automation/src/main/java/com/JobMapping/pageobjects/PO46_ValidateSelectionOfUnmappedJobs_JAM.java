package com.JobMapping.pageobjects;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;

import com.JobMapping.utils.PerformanceUtils;
import com.JobMapping.utils.ScreenshotHandler;
import com.JobMapping.utils.Utilities;
import com.JobMapping.webdriverManager.DriverManager;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

import java.util.List;

public class PO46_ValidateSelectionOfUnmappedJobs_JAM {
	
	WebDriver driver = DriverManager.getDriver();	

	protected static final Logger LOGGER = (Logger) LogManager.getLogger();
	
	// Flag to track if scenario should be skipped (when Unmapped option is not available)
	public static boolean skipScenario = false;
	
	public PO46_ValidateSelectionOfUnmappedJobs_JAM() throws IOException {
		PageFactory.initElements(driver, this);
	}

	WebDriverWait wait = DriverManager.getWait();
	Utilities utils = new Utilities();
	JavascriptExecutor js = (JavascriptExecutor) driver;
	
	//XPATHs
	@FindBy(xpath = "//div[@data-testid='loader']//img")
	@CacheLookup
	WebElement pageLoadSpinner;
	
	@FindBy(xpath = "//thead//input[@type='checkbox']")
	@CacheLookup
	WebElement headerCheckbox;
	
	/**
	 * Selects Unmapped jobs option from Mapping Status Filters dropdown
	 * This method specifically looks for "Unmapped" option in the mapping status filter
	 * If Unmapped option is not found, the scenario will be skipped
	 */
	public void select_unmapped_jobs_option_in_mapping_status_filters_dropdown() {
		// Reset skip flag at the start of scenario
		skipScenario = false;
		
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			PerformanceUtils.waitForPageReady(driver, 2);
			
			List<WebElement> mappingStatusCheckboxes = driver.findElements(
				By.xpath("//div[@data-testid='dropdown-MappingStatus']//..//input[@type='checkbox']")
			);
			List<WebElement> mappingStatusValues = driver.findElements(
				By.xpath("//div[@data-testid='dropdown-MappingStatus']//..//input[@type='checkbox']//..//label")
			);
			
			boolean unmappedFound = false;
			
			// Look for "Unmapped" option
			for (int i = 0; i < mappingStatusValues.size(); i++) {
				String statusText = mappingStatusValues.get(i).getText();
				
				if (statusText.contains("Unmapped")) {
					LOGGER.info("Found Unmapped option: {}", statusText);
					ExtentCucumberAdapter.addTestStepLog("Found Unmapped option: " + statusText);
					
					js.executeScript("arguments[0].scrollIntoView();", mappingStatusValues.get(i));
					
					try {
						wait.until(ExpectedConditions.elementToBeClickable(mappingStatusValues.get(i))).click();
					} catch (Exception e) {
						try {
							js.executeScript("arguments[0].click();", mappingStatusValues.get(i));
						} catch (Exception s) {
							utils.jsClick(driver, mappingStatusValues.get(i));
						}
					}
					
					wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
					PerformanceUtils.waitForPageReady(driver, 3);
					
					Assert.assertTrue(mappingStatusCheckboxes.get(i).isSelected(), 
						"Unmapped option should be selected");
					
					LOGGER.info("Successfully selected Unmapped jobs option from Mapping Status Filters");
					ExtentCucumberAdapter.addTestStepLog("Selected Unmapped jobs option from Mapping Status Filters");
					
					unmappedFound = true;
					break;
				}
			}
			
			if (!unmappedFound) {
				skipScenario = true;
				String skipMessage = "Unmapped option not found in Mapping Status Filters dropdown - Skipping Feature 46 validation";
				LOGGER.warn(skipMessage);
				ExtentCucumberAdapter.addTestStepLog("⚠️ " + skipMessage);
				throw new SkipException(skipMessage);
			}
			
		} catch (SkipException e) {
			// Re-throw SkipException to properly skip the scenario
			throw e;
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("select_unmapped_jobs_option_in_mapping_status_filters_dropdown", e);
			LOGGER.error("Error selecting Unmapped jobs option - Method: select_unmapped_jobs_option_in_mapping_status_filters_dropdown", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Error selecting Unmapped jobs option");
			Assert.fail("Error selecting Unmapped jobs option: " + e.getMessage());
		}
	}
	
	/**
	 * Verifies that Header Checkbox is disabled in Job Mapping screen
	 * When Unmapped filter is applied, users cannot select unmapped jobs, 
	 * so the header checkbox should be disabled
	 */
	public void verify_header_checkbox_is_disabled_in_job_mapping_screen() {
		// Skip if Unmapped option was not found
		if (skipScenario) {
			throw new SkipException("Skipping validation - Unmapped Jobs option not available in Mapping Status Filters in Job Mapping");
		}
		
		try {
			PerformanceUtils.waitForPageReady(driver, 2);
			
			// Scroll to header checkbox
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", headerCheckbox);
			Thread.sleep(500);
			
			boolean isEnabled = headerCheckbox.isEnabled();
			
			Assert.assertFalse(isEnabled, "Header Checkbox should be disabled when Unmapped filter is applied");
			
			LOGGER.info("✓ Header Checkbox is disabled as expected (Unmapped jobs cannot be selected)");
			ExtentCucumberAdapter.addTestStepLog("✓ Header Checkbox is disabled as expected");
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_header_checkbox_is_disabled_in_job_mapping_screen", e);
			LOGGER.error("Error verifying header checkbox is disabled - Method: verify_header_checkbox_is_disabled_in_job_mapping_screen", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Error verifying header checkbox is disabled");
			Assert.fail("Error verifying header checkbox is disabled: " + e.getMessage());
		}
	}
	
	/**
	 * Verifies that Chevron button is disabled in Job Mapping screen
	 * When Unmapped filter is applied, the chevron button (Select All/None dropdown) should be disabled
	 * Disabled state is indicated by classes: cursor-not-allowed and opacity-30
	 * XPath for disabled chevron in JAM: //*[contains(@class,'cursor-not-allowed opacity-30')]
	 */
	/**
	 * Verifies chevron button is disabled for unmapped jobs
	 * No spinner wait needed as filter is already applied and results are visible
	 */
	public void verify_chevron_button_is_disabled_in_job_mapping_screen() {
		// Skip if Unmapped option was not found
		if (skipScenario) {
			throw new SkipException("Skipping validation - Unmapped option not available in Mapping Status Filters");
		}
		
		try {
			// Filter results are already loaded, no need to wait for spinner
			PerformanceUtils.waitForPageReady(driver, 1);
			
			// Scroll to top first to ensure header area is visible
			js.executeScript("window.scrollTo(0, 0);");
			Thread.sleep(300);
			
			// Look for disabled chevron button using the exact XPath for JAM screen disabled state
			List<WebElement> disabledChevrons = driver.findElements(
				By.xpath("//th[@scope='col']//div[@class='relative inline-block']//*[contains(@class,'cursor-not-allowed opacity-30')]")
			);
			
			if (disabledChevrons.isEmpty()) {
				LOGGER.error("Disabled chevron button not found in Job Mapping screen");
				LOGGER.error("Expected XPath: //th[@scope='col']//div[@class='relative inline-block']//*[contains(@class,'cursor-not-allowed opacity-30')]");
				
				// Try to find if any chevron exists (enabled or disabled)
				List<WebElement> anyChevrons = driver.findElements(
					By.xpath("//th[@scope='col']//div[@class='relative inline-block']//div//*")
				);
				
				if (!anyChevrons.isEmpty()) {
					WebElement chevron = anyChevrons.get(0);
					String actualClasses = chevron.getAttribute("class");
					LOGGER.error("Found chevron with classes: {}", actualClasses);
					LOGGER.error("This indicates the chevron button is NOT disabled (it should be disabled for Unmapped jobs)");
					Assert.fail("Chevron button is NOT disabled. Expected 'cursor-not-allowed opacity-30' classes but found: " + actualClasses);
				} else {
					LOGGER.error("No chevron button found at all in header area");
					Assert.fail("Chevron button not found in Job Mapping screen header. Cannot verify disabled state.");
				}
			}
			
			WebElement chevronButton = disabledChevrons.get(0);
			
			// Get and verify classes
			String classAttribute = chevronButton.getAttribute("class");
			
			boolean hasDisabledCursor = classAttribute != null && classAttribute.contains("cursor-not-allowed");
			boolean hasOpacity = classAttribute != null && classAttribute.contains("opacity-30");
			
			Assert.assertTrue(hasDisabledCursor, 
				"Chevron button should have 'cursor-not-allowed' class. Found classes: " + classAttribute);
			Assert.assertTrue(hasOpacity, 
				"Chevron button should have 'opacity-30' class. Found classes: " + classAttribute);
			
			LOGGER.info("✓ Chevron button is disabled as expected (has 'cursor-not-allowed' and 'opacity-30' classes)");
			ExtentCucumberAdapter.addTestStepLog("✓ Chevron button is disabled as expected (Unmapped jobs cannot be selected)");
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_chevron_button_is_disabled_in_job_mapping_screen", e);
			LOGGER.error("Error verifying chevron button is disabled - Method: verify_chevron_button_is_disabled_in_job_mapping_screen", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Error verifying chevron button is disabled");
			Assert.fail("Error verifying chevron button is disabled: " + e.getMessage());
		}
	}
	
	/**
	 * Verifies that all individual unmapped job checkboxes are disabled
	 * Also verifies tooltip on the first checkbox (tooltip appears on hover as a separate div element with data-testid='tooltip')
	 * This method scrolls through all loaded profiles to verify each checkbox's disabled state
	 */
	public void verify_checkbox_of_all_unmapped_jobs_is_disabled_with_tooltip() {
		// Skip if Unmapped option was not found
		if (skipScenario) {
			throw new SkipException("Skipping validation - Unmapped option not available in Mapping Status Filters");
		}
		
		int totalCheckboxes = 0;
		int disabledCheckboxes = 0;
		int checkboxesWithTooltip = 0;
		String expectedTooltipText = "No Success Profile have been mapped to this Job.";
		
		try {
			// Filter results are already loaded, no need to wait for spinner
			PerformanceUtils.waitForPageReady(driver, 1);
			
			LOGGER.info("========================================");
			LOGGER.info("VERIFYING UNMAPPED JOB CHECKBOXES");
			LOGGER.info("========================================");
			
			// Get all checkboxes
			var allCheckboxes = driver.findElements(
				By.xpath("//tbody//tr//td[1][contains(@class,'whitespace')]//input")
			);
			
			totalCheckboxes = allCheckboxes.size();
			LOGGER.info("Total checkboxes found: {}", totalCheckboxes);
			
			// Sample check: Verify only first checkbox for tooltip (one is enough for validation)
			int samplesToCheck = Math.min(1, totalCheckboxes);
			
			// Get tooltip containers that contain checkboxes (not the expanded row tooltip containers)
			var allTooltipContainers = driver.findElements(
				By.xpath("//tbody//tr//td[1][contains(@class,'whitespace')]//div[@data-testid='tooltip-container']")
			);
			
			LOGGER.info("Total tooltip containers (with checkboxes) found: {}", allTooltipContainers.size());
			
		// Verify each checkbox is disabled
		for (int i = 0; i < allCheckboxes.size(); i++) {
			try {
				WebElement checkbox = allCheckboxes.get(i);
				
				// Check if disabled (no need to scroll for simple property check)
				boolean isDisabled = !checkbox.isEnabled();
				
				if (isDisabled) {
					disabledCheckboxes++;
				} else {
					LOGGER.warn("⚠️ Checkbox at position {} is NOT disabled", (i + 1));
				}
				
				// Check for tooltip on first checkbox only
				if (i < samplesToCheck && i < allTooltipContainers.size()) {
					try {
						WebElement tooltipContainer = allTooltipContainers.get(i);
						
						// Scroll to tooltip container once for first checkbox
						js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", tooltipContainer);
						Thread.sleep(200); // Reduced from 300ms
						
						// Hover over the tooltip container to trigger tooltip
						Actions actions = new Actions(driver);
						actions.moveToElement(tooltipContainer).perform();
						Thread.sleep(500); // Reduced from 800ms - tooltip appears quickly
							
							// Look for tooltip element
							List<WebElement> tooltips = driver.findElements(
								By.xpath("//div[@data-testid='tooltip']")
							);
							
							if (!tooltips.isEmpty()) {
								String tooltipText = tooltips.get(0).getText();
								LOGGER.debug("Tooltip found for checkbox {}: {}", (i + 1), tooltipText);
								
								if (tooltipText != null && tooltipText.contains(expectedTooltipText)) {
									checkboxesWithTooltip++;
									LOGGER.debug("Checkbox {} has correct tooltip: {}", (i + 1), tooltipText);
								}
							} else {
								LOGGER.debug("No tooltip element found for checkbox {}", (i + 1));
							}
							
						} catch (Exception tooltipException) {
							LOGGER.debug("Could not verify tooltip for checkbox at position {}: {}", 
								(i + 1), tooltipException.getMessage());
						}
					}
					
				} catch (Exception e) {
					LOGGER.debug("Could not verify checkbox at position {}: {}", (i + 1), e.getMessage());
					continue;
				}
			}
			
			LOGGER.info("========================================");
			LOGGER.info("VALIDATION RESULTS");
			LOGGER.info("========================================");
			LOGGER.info("Total Checkboxes: {}", totalCheckboxes);
			LOGGER.info("Disabled Checkboxes: {}", disabledCheckboxes);
			LOGGER.info("Checkbox with Tooltip verified: {}", checkboxesWithTooltip > 0 ? "Yes" : "No");
			LOGGER.info("========================================");
			
			// Validate results
			Assert.assertEquals(disabledCheckboxes, totalCheckboxes, 
				"All checkboxes should be disabled for Unmapped jobs");
			
			// Tooltip check - verify first checkbox has tooltip
			if (samplesToCheck > 0) {
				Assert.assertTrue(checkboxesWithTooltip > 0, 
					"First checkbox should have tooltip explaining why it's disabled. Expected text: " + expectedTooltipText);
			}
			
			LOGGER.info("✓ VALIDATION PASSED: All {} unmapped job checkboxes are disabled", disabledCheckboxes);
			LOGGER.info("✓ First checkbox has correct tooltip with expected text");
			ExtentCucumberAdapter.addTestStepLog("✓ Validation PASSED: All " + disabledCheckboxes + " unmapped job checkboxes are disabled");
			ExtentCucumberAdapter.addTestStepLog("✓ First checkbox has correct tooltip: '" + expectedTooltipText + "'");
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_checkbox_of_all_unmapped_jobs_is_disabled_with_tooltip", e);
			LOGGER.error("Error verifying unmapped job checkboxes - Method: verify_checkbox_of_all_unmapped_jobs_is_disabled_with_tooltip", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Error verifying unmapped job checkboxes");
			Assert.fail("Error verifying unmapped job checkboxes: " + e.getMessage());
		}
	}
}

