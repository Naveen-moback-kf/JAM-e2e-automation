package com.kfonetalentsuite.pageobjects.JobMapping;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.ScreenshotHandler;
import com.kfonetalentsuite.utils.JobMapping.PageObjectHelper;

/**
 * Page Object for validating Sorting functionality in HCM Sync Profiles (PM) screen.
 * Handles single-level and multi-level sorting validation by various columns.
 * 
 * Enhanced to extend BasePageObject for consistency and code reuse.
 */
public class PO34_SortingFunctionalityInHCMScreen_PM extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO34_SortingFunctionalityInHCMScreen_PM.class);

	public PO34_SortingFunctionalityInHCMScreen_PM() {
		super();
	}

	// ==================== LOCATORS ====================
	// PAGE_LOAD_SPINNER is available via Locators.Spinners.PAGE_LOAD_SPINNER

	// Table Headers (used for sorting)
	private static final By TABLE_HEADER_NAME = By.xpath("//thead//tr//div[@kf-sort-header='name']//div");
	private static final By TABLE_HEADER_STATUS = By.xpath("//thead//tr//div//div[text()=' Status ']");
	private static final By TABLE_HEADER_LEVEL = By.xpath("//thead//tr//div//div[text()=' Level ']");
	private static final By TABLE_HEADER_FUNCTION = By.xpath("//thead//tr//div//div[text()=' Function ']");
	private static final By TABLE_HEADER_EXPORT_STATUS = By.xpath("//thead//tr//div//div[text()=' Export status ']");

	// Data elements
	private static final By PROFILE_NAME_ELEMENTS = By.xpath("//tbody//tr//td//div//span[1]//a");
	private static final By LEVEL_ELEMENTS = By.xpath("//tbody//tr//td[4]//div//span[1]");
	private static final By JOB_STATUS_ELEMENTS = By.xpath("//tbody//tr//td[2]//div//span[1]");
	private static final By FUNCTION_ELEMENTS = By.xpath("//tbody//tr//td[5]//div//span[1]");
	private static final By EXPORT_STATUS_ELEMENTS = By.xpath("//tbody//tr//td[8]//div//span[1]");

	/**
	 * Sorts profiles by Name in ascending order (single click).
	 */
	public void sort_profiles_by_name_in_ascending_order_in_hcm_sync_profiles_screen() {
		try {
			clickElement(TABLE_HEADER_NAME);
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver);
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PageObjectHelper.log(LOGGER, "Clicked on Name header to Sort Profiles in ascending order");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "sort_by_name_ascending", "Issue sorting by name ascending", e);
			ScreenshotHandler.captureFailureScreenshot("sort_profiles_by_name_ascending", e);
			Assert.fail("Issue in sorting by Name in ascending order...Please Investigate!!!");
		}
	}

	/**
	 * Verifies profiles are sorted by Name in ascending order.
	 */
	public void user_should_verify_first_hundred_job_profiles_are_correctly_sorted_by_name_in_ascending_order() {
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver);

			List<WebElement> allElements = findElements(PROFILE_NAME_ELEMENTS);
			PageObjectHelper.log(LOGGER, "Profiles After sorting by Name in Ascending Order:");

			ArrayList<String> profileNames = new ArrayList<>();
			int specialCharCount = 0;
			int nonAsciiCount = 0;
			int limit = Math.min(allElements.size(), 100);

			for (int i = 0; i < limit; i++) {
				String text = allElements.get(i).getText();
				profileNames.add(text);

				if (!text.isEmpty() && !Character.isLetterOrDigit(text.charAt(0))) {
					specialCharCount++;
				} else if (!text.isEmpty() && text.charAt(0) > 127) {
					nonAsciiCount++;
				}
				PageObjectHelper.log(LOGGER, "Profile Name : " + text);
			}

			if (specialCharCount > 0) {
				PageObjectHelper.log(LOGGER, specialCharCount + " profile(s) start with special characters");
			}
			if (nonAsciiCount > 0) {
				PageObjectHelper.log(LOGGER, nonAsciiCount + " profile(s) contain non-ASCII characters");
			}

			// Validate ascending order
			int sortViolations = validateAscendingOrder(profileNames);

			if (sortViolations > 0) {
				String errorMsg = "SORTING FAILED: Found " + sortViolations + " violation(s). NOT in Ascending Order!";
				PageObjectHelper.log(LOGGER, errorMsg);
				Assert.fail(errorMsg);
			} else {
				PageObjectHelper.log(LOGGER, "Sorting validation PASSED - Data is correctly sorted in Ascending Order");
			}

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_sorted_by_name_ascending", 
					"Issue verifying sorted profiles", e);
			ScreenshotHandler.captureFailureScreenshot("verify_profiles_sorted_by_name_ascending", e);
			Assert.fail("Issue in Verifying Profiles sorted by Name in Ascending Order...Please Investigate!!!");
		}
	}

	/**
	 * Sorts profiles by Level in descending order (clicks header twice).
	 */
	public void sort_profiles_by_level_in_descending_order_in_hcm_sync_profiles_screen() {
		try {
			WebElement levelHeader = findElement(TABLE_HEADER_LEVEL);
			js.executeScript("arguments[0].scrollIntoView({block: 'center'});", levelHeader);
			safeSleep(500);

			// First click - ascending
			clickElement(TABLE_HEADER_LEVEL);
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			safeSleep(3000);
			PerformanceUtils.waitForPageReady(driver);
			safeSleep(1500);

			// Second click - descending
			js.executeScript("arguments[0].scrollIntoView({block: 'center'});", levelHeader);
			safeSleep(500);
			clickElement(TABLE_HEADER_LEVEL);
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			safeSleep(3000);
			PerformanceUtils.waitForPageReady(driver);

			PageObjectHelper.log(LOGGER, "Clicked twice on Level header to Sort in Descending order");

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "sort_by_level_descending", "Issue sorting by level descending", e);
			ScreenshotHandler.captureFailureScreenshot("sort_profiles_by_level_descending", e);
			Assert.fail("Issue in sorting by Level in Descending order...Please Investigate!!!");
		}
	}

	/**
	 * Verifies profiles are sorted by Level in descending order.
	 */
	public void user_should_verify_first_hundred_job_profiles_are_correctly_sorted_by_level_in_descending_order() {
		try {
			waitForElement(Locators.Spinners.PAGE_LOAD_SPINNER, 2);
			PerformanceUtils.waitForPageReady(driver);

			List<WebElement> profileNameElements = findElements(PROFILE_NAME_ELEMENTS);
			List<WebElement> levelElements = findElements(LEVEL_ELEMENTS);

			PageObjectHelper.log(LOGGER, "Profiles After sorting by Level in Descending Order:");

			ArrayList<String> levels = new ArrayList<>();
			int emptyCount = 0;
			int limit = Math.min(Math.min(profileNameElements.size(), levelElements.size()), 100);

			for (int i = 0; i < limit; i++) {
				String profileName = profileNameElements.get(i).getText();
				String levelText = levelElements.get(i).getText();

				if (levelText == null || levelText.trim().isEmpty() || levelText.equals("-")) {
					emptyCount++;
					PageObjectHelper.log(LOGGER, "Profile: " + profileName + " - Level: [EMPTY]");
				} else {
					levels.add(levelText);
					PageObjectHelper.log(LOGGER, "Profile: " + profileName + " - Level: " + levelText);
				}
			}

			if (emptyCount > 0) {
				PageObjectHelper.log(LOGGER, emptyCount + " profile(s) have empty Level values");
			}

			// Validate descending order
			int sortViolations = validateDescendingOrder(levels);

			if (sortViolations > 0) {
				String errorMsg = "SORTING FAILED: Found " + sortViolations + " violation(s). NOT in Descending Order!";
				PageObjectHelper.log(LOGGER, errorMsg);
				Assert.fail(errorMsg);
			} else if (levels.size() > 1) {
				PageObjectHelper.log(LOGGER, "Sorting validation PASSED - Data is correctly sorted in Descending Order");
			} else {
				PageObjectHelper.log(LOGGER, "Validation skipped - insufficient data for sorting validation");
			}

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_sorted_by_level_descending", 
					"Issue verifying sorted profiles", e);
			ScreenshotHandler.captureFailureScreenshot("verify_profiles_sorted_by_level_descending", e);
			Assert.fail("Issue in Verifying Profiles sorted by Level in Descending Order...Please Investigate!!!");
		}
	}

	/**
	 * Sorts profiles by Job Status in ascending order (single click).
	 */
	public void sort_profiles_by_job_status_in_ascending_order_in_hcm_sync_profiles_screen() {
		try {
			clickElement(TABLE_HEADER_STATUS);
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver);
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PageObjectHelper.log(LOGGER, "Clicked on Job Status header to Sort in ascending order");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "sort_by_job_status_ascending", 
					"Issue sorting by job status ascending", e);
			ScreenshotHandler.captureFailureScreenshot("sort_profiles_by_job_status_ascending", e);
			Assert.fail("Issue in sorting by Job Status in ascending order...Please Investigate!!!");
		}
	}

	/**
	 * Sorts profiles by Export Status in descending order (clicks header twice).
	 */
	public void sort_profiles_by_export_status_in_descending_order_in_hcm_sync_profiles_screen() {
		try {
			WebElement exportStatusHeader = findElement(TABLE_HEADER_EXPORT_STATUS);
			js.executeScript("arguments[0].scrollIntoView({block: 'center'});", exportStatusHeader);
			safeSleep(500);

			// First click - ascending
			clickElement(TABLE_HEADER_EXPORT_STATUS);
			waitForElement(Locators.Spinners.PAGE_LOAD_SPINNER, 2);
			safeSleep(3000);
			PerformanceUtils.waitForPageReady(driver);
			safeSleep(1500);

			// Second click - descending
			js.executeScript("arguments[0].scrollIntoView({block: 'center'});", exportStatusHeader);
			safeSleep(500);
			clickElement(TABLE_HEADER_EXPORT_STATUS);
			waitForElement(Locators.Spinners.PAGE_LOAD_SPINNER, 2);
			safeSleep(3000);
			PerformanceUtils.waitForPageReady(driver);

			PageObjectHelper.log(LOGGER, "Clicked twice on Export Status header to Sort in Descending order");

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "sort_by_export_status_descending", 
					"Issue sorting by export status descending", e);
			ScreenshotHandler.captureFailureScreenshot("sort_profiles_by_export_status_descending", e);
			Assert.fail("Issue in sorting by Export Status in Descending order...Please Investigate!!!");
		}
	}

	/**
	 * Sorts profiles by Function in ascending order (single click).
	 */
	public void sort_profiles_by_function_in_ascending_order_in_hcm_sync_profiles_screen() {
		try {
			WebElement functionHeader = findElement(TABLE_HEADER_FUNCTION);
			js.executeScript("arguments[0].scrollIntoView({block: 'center'});", functionHeader);
			safeSleep(500);

			clickElement(TABLE_HEADER_FUNCTION);
			waitForElement(Locators.Spinners.PAGE_LOAD_SPINNER, 2);
			PerformanceUtils.waitForPageReady(driver);
			PageObjectHelper.log(LOGGER, "Clicked on Function header to Sort in ascending order");

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "sort_by_function_ascending", 
					"Issue sorting by function ascending", e);
			ScreenshotHandler.captureFailureScreenshot("sort_profiles_by_function_ascending", e);
			Assert.fail("Issue in sorting by Function in ascending order...Please Investigate!!!");
		}
	}

	/**
	 * Sorts profiles by Function in descending order (clicks header twice).
	 */
	public void sort_profiles_by_function_in_descending_order_in_hcm_sync_profiles_screen() {
		try {
			// First click - ascending
			clickElement(TABLE_HEADER_FUNCTION);
			waitForElement(Locators.Spinners.PAGE_LOAD_SPINNER, 2);
			safeSleep(2000);
			PerformanceUtils.waitForPageReady(driver);
			safeSleep(1000);

			// Second click - descending
			clickElement(TABLE_HEADER_FUNCTION);
			waitForElement(Locators.Spinners.PAGE_LOAD_SPINNER, 2);
			safeSleep(2000);
			PerformanceUtils.waitForPageReady(driver);

			PageObjectHelper.log(LOGGER, "Clicked twice on Function header to Sort in Descending order");

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "sort_by_function_descending", 
					"Issue sorting by function descending", e);
			ScreenshotHandler.captureFailureScreenshot("sort_profiles_by_function_descending", e);
			Assert.fail("Issue in sorting by Function in Descending order...Please Investigate!!!");
		}
	}

	/**
	 * Verifies profiles are sorted by Job Status (ascending) and Function (descending).
	 */
	public void user_should_verify_first_hundred_job_profiles_are_correctly_sorted_by_job_status_in_ascending_and_function_in_descending_order() {
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver);

			List<WebElement> profileNameElements = findElements(PROFILE_NAME_ELEMENTS);
			List<WebElement> jobStatusElements = findElements(JOB_STATUS_ELEMENTS);
			List<WebElement> functionElements = findElements(FUNCTION_ELEMENTS);

			PageObjectHelper.log(LOGGER, "Profiles sorted by Job Status (Asc) and Function (Desc):");

			int limit = Math.min(Math.min(profileNameElements.size(), jobStatusElements.size()), 100);
			limit = Math.min(limit, functionElements.size());

			for (int i = 0; i < limit; i++) {
				String profileName = profileNameElements.get(i).getText();
				String jobStatusText = getValueOrEmpty(jobStatusElements.get(i).getText());
				String functionText = getValueOrEmpty(functionElements.get(i).getText());

				PageObjectHelper.log(LOGGER, "Profile: " + profileName + " - Job Status: " + 
						jobStatusText + " - Function: " + functionText);
			}

			// Validate multi-level sorting
			int sortViolations = validateMultiLevelSort(jobStatusElements, functionElements, limit, true, false);

			if (sortViolations > 0) {
				String errorMsg = "SORTING FAILED: Found " + sortViolations + " violation(s)!";
				PageObjectHelper.log(LOGGER, errorMsg);
				Assert.fail(errorMsg);
			} else {
				PageObjectHelper.log(LOGGER, "Multi-level sorting validation PASSED");
			}

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_sorted_by_job_status_and_function", 
					"Issue verifying sorted profiles", e);
			ScreenshotHandler.captureFailureScreenshot("verify_profiles_sorted_by_job_status_and_function", e);
			Assert.fail("Issue in Verifying Profiles sorted by Job Status and Function...Please Investigate!!!");
		}
	}

	/**
	 * Verifies three-level sorting: Job Status (asc), Export Status (desc), Function (asc).
	 */
	public void user_should_verify_first_hundred_job_profiles_are_correctly_sorted_by_job_status_ascending_export_status_descending_and_function_ascending() {
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver);

			List<WebElement> profileNameElements = findElements(PROFILE_NAME_ELEMENTS);
			List<WebElement> jobStatusElements = findElements(JOB_STATUS_ELEMENTS);
			List<WebElement> exportStatusElements = findElements(EXPORT_STATUS_ELEMENTS);
			List<WebElement> functionElements = findElements(FUNCTION_ELEMENTS);

			PageObjectHelper.log(LOGGER, "Profiles sorted by Job Status (Asc), Export Status (Desc), Function (Asc):");

			int limit = Math.min(Math.min(profileNameElements.size(), jobStatusElements.size()), 100);
			limit = Math.min(limit, Math.min(exportStatusElements.size(), functionElements.size()));

			for (int i = 0; i < limit; i++) {
				String profileName = profileNameElements.get(i).getText();
				String jobStatus = getValueOrEmpty(jobStatusElements.get(i).getText());
				String exportStatus = getValueOrEmpty(exportStatusElements.get(i).getText());
				String function = getValueOrEmpty(functionElements.get(i).getText());

				PageObjectHelper.log(LOGGER, "Profile: " + profileName + " | Job Status: " + jobStatus +
						" | Export Status: " + exportStatus + " | Function: " + function);
			}

			// Validate three-level sorting
			int sortViolations = validateThreeLevelSort(jobStatusElements, exportStatusElements, 
					functionElements, limit);

			if (sortViolations > 0) {
				String errorMsg = "SORTING FAILED: Found " + sortViolations + " violation(s)!";
				PageObjectHelper.log(LOGGER, errorMsg);
				Assert.fail(errorMsg);
			} else {
				PageObjectHelper.log(LOGGER, "Three-level sorting validation PASSED");
			}

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_3level_sorting", 
					"Issue verifying 3-level sorted profiles", e);
			ScreenshotHandler.captureFailureScreenshot("verify_profiles_3level_sorting", e);
			Assert.fail("Issue in Verifying Profiles After 3-level sorting...Please Investigate!!!");
		}
	}

	// ==================== HELPER METHODS (File-specific multi-level sort validation) ====================

	/**
	 * Validates multi-level sorting with primary and secondary columns.
	 * Uses inherited normalizeForSorting() and shouldSkipInSortValidation() from BasePageObject.
	 */
	private int validateMultiLevelSort(List<WebElement> primaryElements, List<WebElement> secondaryElements,
			int limit, boolean primaryAscending, boolean secondaryAscending) {
		int violations = 0;

		for (int i = 0; i < limit - 1; i++) {
			String currentPrimary = primaryElements.get(i).getText();
			String nextPrimary = primaryElements.get(i + 1).getText();
			String currentSecondary = secondaryElements.get(i).getText();
			String nextSecondary = secondaryElements.get(i + 1).getText();

			if (shouldSkipInSortValidation(currentPrimary) || shouldSkipInSortValidation(nextPrimary)) {
				continue;
			}

			int primaryComparison = currentPrimary.compareToIgnoreCase(nextPrimary);
			
			if ((primaryAscending && primaryComparison > 0) || (!primaryAscending && primaryComparison < 0)) {
				violations++;
				LOGGER.error("PRIMARY SORT VIOLATION at Row {} -> Row {}", (i + 1), (i + 2));
			}
			else if (primaryComparison == 0 && !shouldSkipInSortValidation(currentSecondary) && 
					!shouldSkipInSortValidation(nextSecondary)) {
				
				String currentNormalized = normalizeForSorting(currentSecondary);
				String nextNormalized = normalizeForSorting(nextSecondary);
				int secondaryComparison = currentNormalized.compareToIgnoreCase(nextNormalized);
				
				if ((secondaryAscending && secondaryComparison > 0) || 
						(!secondaryAscending && secondaryComparison < 0)) {
					violations++;
					LOGGER.error("SECONDARY SORT VIOLATION at Row {} -> Row {}", (i + 1), (i + 2));
				}
			}
		}

		return violations;
	}

	/**
	 * Validates three-level sorting: L1 (asc), L2 (desc), L3 (asc).
	 * Uses inherited methods from BasePageObject.
	 */
	private int validateThreeLevelSort(List<WebElement> level1Elements, List<WebElement> level2Elements,
			List<WebElement> level3Elements, int limit) {
		int violations = 0;

		for (int i = 0; i < limit - 1; i++) {
			String currentL1 = level1Elements.get(i).getText();
			String nextL1 = level1Elements.get(i + 1).getText();
			String currentL2 = level2Elements.get(i).getText();
			String nextL2 = level2Elements.get(i + 1).getText();
			String currentL3 = level3Elements.get(i).getText();
			String nextL3 = level3Elements.get(i + 1).getText();

			if (shouldSkipInSortValidation(currentL1) || shouldSkipInSortValidation(nextL1)) {
				continue;
			}

			int l1Comparison = currentL1.compareToIgnoreCase(nextL1);
			
			// Level 1: Ascending
			if (l1Comparison > 0) {
				violations++;
				LOGGER.error("L1 SORT VIOLATION at Row {} -> Row {}", (i + 1), (i + 2));
			}
			// Level 2: Descending when L1 same
			else if (l1Comparison == 0 && !shouldSkipInSortValidation(currentL2) && 
					!shouldSkipInSortValidation(nextL2)) {
				
				String l2Current = normalizeForSorting(currentL2);
				String l2Next = normalizeForSorting(nextL2);
				int l2Comparison = l2Current.compareToIgnoreCase(l2Next);
				
				if (l2Comparison < 0) {
					violations++;
					LOGGER.error("L2 SORT VIOLATION at Row {} -> Row {}", (i + 1), (i + 2));
				}
				// Level 3: Ascending when L1 and L2 same
				else if (l2Comparison == 0 && !shouldSkipInSortValidation(currentL3) && 
						!shouldSkipInSortValidation(nextL3)) {
					
					if (normalizeForSorting(currentL3).compareToIgnoreCase(normalizeForSorting(nextL3)) > 0) {
						violations++;
						LOGGER.error("L3 SORT VIOLATION at Row {} -> Row {}", (i + 1), (i + 2));
					}
				}
			}
		}

		return violations;
	}
}
