package com.kfonetalentsuite.pageobjects.JobMapping;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.kfonetalentsuite.utils.PageObjectHelper;
import com.kfonetalentsuite.webdriverManager.DriverManager;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

public class PO17_ValidateSortingFunctionality_JAM {
	WebDriver driver = DriverManager.getDriver();

	protected static final Logger LOGGER = (Logger) LogManager.getLogger();
	PO17_ValidateSortingFunctionality_JAM validateSortingFunctionality;

	public static ArrayList<String> jobNamesTextInDefaultOrder = new ArrayList<String>();

	public PO17_ValidateSortingFunctionality_JAM() throws IOException {
		PageFactory.initElements(driver, this);
	}

	WebDriverWait wait = DriverManager.getWait();
	Utilities utils = new Utilities();
	JavascriptExecutor js = (JavascriptExecutor) driver;

	// XPATHS
	@FindBy(xpath = "//div[@data-testid='loader']//img")
	@CacheLookup
	WebElement pageLoadSpinner2;

	@FindBy(xpath = "//div[contains(@id,'results-toggle')]//*[contains(text(),'Showing')]")
	@CacheLookup
	public WebElement showingJobResultsCount;

	@FindBy(xpath = "//*[@id='org-job-container']/div/table/thead/tr/th[2]/div")
	@CacheLookup
	public WebElement orgJobNameHeader;

	@FindBy(xpath = "//*[@id='kf-job-container']/div/table/thead/tr/th[2]")
	@CacheLookup
	public WebElement matchedSPGradeHeader;

	@FindBy(xpath = "//*[@id='kf-job-container']/div/table/thead/tr/th[1]/div")
	@CacheLookup
	public WebElement matcheSPNameHeader;

	@FindBy(xpath = "//*[@id='org-job-container']/div/table/thead/tr/th[3]/div")
	@CacheLookup
	public WebElement orgJobGradeHeader;

	// METHODs

	/**
	 * ENHANCED: Wait for specific loader to disappear after sorting action Waits
	 * for the data-testid='loader' spinner to disappear with proper timeout
	 */
	private void waitForLoaderToDisappear() {
		try {
			// Wait for loader to appear first (indicates sort started)
			try {
				wait.until(ExpectedConditions.visibilityOf(pageLoadSpinner2));
				LOGGER.debug("Loader appeared - sort operation started");
			} catch (Exception e) {
				// Loader might be too fast to catch - acceptable
				LOGGER.debug("Loader not caught appearing (too fast) - continuing");
			}

			// Wait for loader to disappear (sort operation completed)
			try {
				wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner2));
				LOGGER.debug("Loader disappeared - sort operation completed");
			} catch (Exception e) {
				LOGGER.warn("Loader invisibility timeout - continuing anyway");
			}

			// Additional short wait for DOM updates to complete
			Thread.sleep(500);

		} catch (Exception e) {
			LOGGER.warn("Loader wait failed: " + e.getMessage() + " - continuing");
		}
	}

	public void user_should_scroll_page_down_two_times_to_view_first_thirty_job_profiles() {
		try {
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(showingJobResultsCount)).isDisplayed());
			PerformanceUtils.waitForSpinnersToDisappear(driver);
			PerformanceUtils.waitForPageReady(driver, 3);
			Thread.sleep(2000);

			// First scroll
			js.executeScript("window.scrollTo(0, document.documentElement.scrollHeight);");
			PerformanceUtils.waitForSpinnersToDisappear(driver);
			PerformanceUtils.waitForPageReady(driver, 3);
			Thread.sleep(2000);

			// Second scroll
			js.executeScript("window.scrollTo(0, document.documentElement.scrollHeight);");
			PerformanceUtils.waitForSpinnersToDisappear(driver);
			PerformanceUtils.waitForPageReady(driver, 3);
			Thread.sleep(2000);

			// Third scroll
			js.executeScript("window.scrollTo(0, document.documentElement.scrollHeight);");
			PerformanceUtils.waitForSpinnersToDisappear(driver);
			PerformanceUtils.waitForPageReady(driver, 3);
			Thread.sleep(3000);
			PerformanceUtils.waitForElement(driver, showingJobResultsCount, 5);
			String resultsCountText_updated = wait.until(ExpectedConditions.visibilityOf(showingJobResultsCount))
					.getText();
			PageObjectHelper.log(LOGGER, "Scrolled down till third page and now " + resultsCountText_updated
					+ " of Job Profiles as expected");

			// Scroll back to top
			js.executeScript("window.scrollTo(0, 0);");
			PerformanceUtils.waitForSpinnersToDisappear(driver);
			PerformanceUtils.waitForPageReady(driver, 2);
			Thread.sleep(5000);
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"user_should_scroll_page_down_two_times_to_view_first_thirty_job_profiles",
					"Issue in scrolling page down two times to view first thirty job profiles", e);
		}
	}

	public void user_should_verify_first_thirty_job_profiles_in_default_order_before_applying_sorting() {
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver);
			PerformanceUtils.waitForPageReady(driver, 3);
			Thread.sleep(500); // Additional wait for DOM stability

			// FIXED: Extract text immediately to avoid stale element references
			List<WebElement> allElements = driver
					.findElements(By.xpath("//tbody//tr//td[2]//div[contains(text(),'(')]"));
			PageObjectHelper.log(LOGGER,
					"Below is default Order of first thirty Job Profiles before applying sorting:");

			for (WebElement element : allElements) {
				String text = element.getText();
				jobNamesTextInDefaultOrder.add(text);
				PageObjectHelper.log(LOGGER, "Organization Job Profile with Job Name / Code : " + text);
			}
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"user_should_verify_first_thirty_job_profiles_in_default_order_before_applying_sorting",
					"Issue in Verifying default Order of first thirty Job Profiles before applying sorting", e);
		}
	}

	public void sort_job_profiles_by_organiztion_job_name_in_ascending_order() {
		try {
			Thread.sleep(2000);
			// Click with fallback strategies
			try {
				wait.until(ExpectedConditions.visibilityOf(orgJobNameHeader)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", orgJobNameHeader);
				} catch (Exception s) {
					utils.jsClick(driver, orgJobNameHeader);
				}
			}

			// PERFORMANCE: Single comprehensive wait (removed triple stacking)
			PerformanceUtils.waitForPageReady(driver, 5);
			PageObjectHelper.log(LOGGER,
					"Clicked on Organization job name / code header to Sort Job Profiles by Name in ascending order");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "sort_job_profiles_by_organiztion_job_name_in_ascending_order",
					"Issue in clicking on Organization job name / code header to Sort Job Profiles by Name in ascending order",
					e);
		}
	}

	public void user_should_verify_first_thirty_job_profiles_sorted_by_organiztion_job_name_in_ascending_order() {
		try {
			// ENHANCED: Ensure DOM is fully stable after sorting
			PerformanceUtils.waitForSpinnersToDisappear(driver);
			PerformanceUtils.waitForPageReady(driver, 3);
			Thread.sleep(3000); // Additional wait for DOM stability

			List<WebElement> allElements = driver
					.findElements(By.xpath("//tbody//tr//td[2]//div[contains(text(),'(')]"));
			PageObjectHelper.log(LOGGER,
					"Below are first thirty Job Profiles After sorting Job Profiles by Name in Ascending Order:");

			// Get ALL Find Match buttons in ONE query, then map to job indices
			Set<Integer> unmappedJobIndices = new HashSet<>();
			try {
				List<WebElement> allButtons = driver
						.findElements(By.xpath("//*[@id='kf-job-container']//button[contains(text(),'Find Match')]"));

				for (WebElement button : allButtons) {
					try {
						// Get the row number of this button (0-based index)
						Long rowIndex = (Long) js.executeScript("var tr = arguments[0].closest('tr');"
								+ "var tbody = tr.parentElement;" + "return Array.from(tbody.children).indexOf(tr);",
								button);

						if (rowIndex != null && rowIndex >= 0) {
							// Right table (kf-job-container) has 2 rows per job
							// Each org job corresponds to 2 KF rows (0-1, 2-3, 4-5, etc.)
							int jobIndex = (int) (rowIndex / 2);
							unmappedJobIndices.add(jobIndex);
						}
					} catch (Exception e) {
						// Ignore if we can't determine the row
					}
				}
			} catch (Exception e) {
				LOGGER.warn("Error detecting unmapped jobs: " + e.getMessage());
			}

			LOGGER.info("Found " + unmappedJobIndices.size() + " unmapped job(s) with Find Match buttons");

			// Collect job names for validation (ONLY mapped jobs)
			ArrayList<String> jobNames = new ArrayList<String>();
			int specialCharCount = 0;
			int nonAsciiCount = 0;
			int unmappedCount = 0;

			for (int i = 0; i < allElements.size(); i++) {
				WebElement element = allElements.get(i);
				String text = element.getText();

				// INSTANT: O(1) lookup in HashSet
				boolean isUnmapped = unmappedJobIndices.contains(i);

				// Skip unmapped jobs from sort validation (they appear at top regardless of
				// sort)
				if (isUnmapped) {
					unmappedCount++;
					LOGGER.info("Organization Job Profile with Job Name / Code : " + text
							+ " - UNMAPPED (has Find Match button) - SKIPPED from sort validation");
					ExtentCucumberAdapter
							.addTestStepLog("Organization Job Profile: " + text + " - UNMAPPED - Appears at top");
					continue; // Skip this job from sort validation
				}

				// Add to validation list (only mapped jobs)
				jobNames.add(text); // Store original case for proper comparison

				// Detect special characters at start (expected at top in ascending)
				if (!text.isEmpty() && !Character.isLetterOrDigit(text.charAt(0))) {
					specialCharCount++;
					LOGGER.info("Organization Job Profile with Job Name / Code : " + text
							+ " [SPECIAL CHAR at start - expected at top in Ascending]");
				}
				// Detect non-ASCII characters (Chinese, etc.)
				else if (!text.isEmpty() && text.charAt(0) > 127) {
					nonAsciiCount++;
					LOGGER.info("Organization Job Profile with Job Name / Code : " + text + " [NON-ASCII detected]");
				} else {
					LOGGER.info("Organization Job Profile with Job Name / Code : " + text);
				}
				ExtentCucumberAdapter.addTestStepLog("Organization Job Profile with Name Job Name / Code : " + text);
			}

			if (unmappedCount > 0) {
				LOGGER.info("ℹ Found " + unmappedCount
						+ " unmapped job(s) - these appear at top and are EXCLUDED from sort validation");
				ExtentCucumberAdapter.addTestStepLog(
						"ℹ " + unmappedCount + " unmapped job(s) excluded from sort validation (appear at top)");
			}
			if (specialCharCount > 0) {
				LOGGER.info("ℹ Found " + specialCharCount
						+ " job(s) with special characters - these appear at top in Ascending order as expected");
				ExtentCucumberAdapter.addTestStepLog("ℹ " + specialCharCount
						+ " job(s) start with special characters (?, -, etc.) - expected at top");
			}
			if (nonAsciiCount > 0) {
				LOGGER.info("ℹ Found " + nonAsciiCount + " job(s) with non-ASCII characters (Chinese, etc.)");
				ExtentCucumberAdapter.addTestStepLog("ℹ " + nonAsciiCount + " job(s) contain non-ASCII characters");
			}

			// ✅ VALIDATE ASCENDING ORDER (using case-insensitive comparison to match UI
			// behavior)
			// NOTE: Only validates MAPPED jobs - unmapped jobs appear at top and are
			// excluded
			int sortViolations = 0;
			for (int i = 0; i < jobNames.size() - 1; i++) {
				String current = jobNames.get(i);
				String next = jobNames.get(i + 1);
				// compareToIgnoreCase handles case-insensitive ordering: special chars < ASCII
				// letters (A/a) < non-ASCII
				if (current.compareToIgnoreCase(next) > 0) {
					sortViolations++;
					LOGGER.error("❌ SORT VIOLATION at Row " + (i + 1) + " -> Row " + (i + 2) + ": '" + current + "' > '"
							+ next + "' (NOT in Ascending Order!)");
					ExtentCucumberAdapter.addTestStepLog("❌ SORT VIOLATION: Row " + (i + 1) + " > Row " + (i + 2));
				}
			}

			if (sortViolations > 0) {
				String errorMsg = "❌ SORTING FAILED: Found " + sortViolations
						+ " violation(s). Data is NOT sorted by Job Name in Ascending Order!";
				LOGGER.error(errorMsg);
				ExtentCucumberAdapter.addTestStepLog(errorMsg);
				Assert.fail(errorMsg + " Please check the sorting implementation!");
			} else {
				LOGGER.info("✅ SORT VALIDATION PASSED: All " + jobNames.size()
						+ " MAPPED Job Profiles are correctly sorted by Job Name in Ascending Order (including special chars and non-ASCII)");
				ExtentCucumberAdapter.addTestStepLog("✅ Sorting validation PASSED - " + jobNames.size()
						+ " mapped job(s) correctly sorted in Ascending Order");
			}

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"user_should_verify_first_thirty_job_profiles_sorted_by_organiztion_job_name_in_ascending_order",
					"Issue in Verifying first thirty Job Profiles After sorting Job Profiles by Name in Ascending Order",
					e);
		}
	}

	public void user_should_refresh_job_mapping_and_verify_job_profiles_are_in_default_order() {
		try {
			driver.navigate().refresh();
			PerformanceUtils.waitForSpinnersToDisappear(driver);
			PerformanceUtils.waitForPageReady(driver, 5);
			PageObjectHelper.log(LOGGER, "Refreshed Job Mapping page....");
			List<WebElement> allElements = driver
					.findElements(By.xpath("//tbody//tr//td[2]//div[contains(text(),'(')]"));
			for (int i = 0; i < allElements.size(); i++) {
				WebElement element = allElements.get(i);
				String text = element.getText();
				if (text.contentEquals(jobNamesTextInDefaultOrder.get(i))) {
					continue;
				} else {
					throw new Exception("Organization Job Name / code : " + text + " in Row " + Integer.toString(i)
							+ "DOEST NOT Match with Job Name / Code : " + jobNamesTextInDefaultOrder.get(i)
							+ " after Refreshing Job Mapping page");
				}
			}
			PageObjectHelper.log(LOGGER,
					"Organization Job Profiles are in Default Order as expected After Refreshing the Job Mapping page....");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"user_should_refresh_job_mapping_and_verify_job_profiles_are_in_default_order",
					"Issue in Verifying Default order of Job Profiles after Refreshing Job Mapping page", e);
		}
	}

	/**
	 * Sorts job profiles by job name in descending order (clicks header twice) Page
	 * already loaded, directly clicks to sort Enhanced with proper waits for
	 * HEADLESS MODE compatibility
	 */
	public void sort_job_profiles_by_organiztion_job_name_in_descending_order() {
		try {
			// FIRST CLICK - Sort Ascending
			PageObjectHelper.log(LOGGER, "First click on Organization job name header to sort ascending...");
			try {
				wait.until(ExpectedConditions.elementToBeClickable(orgJobNameHeader)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", orgJobNameHeader);
				} catch (Exception s) {
					utils.jsClick(driver, orgJobNameHeader);
				}
			}

			// ENHANCED: Wait specifically for loader to disappear after first click
			waitForLoaderToDisappear();
			PageObjectHelper.log(LOGGER, "First sort completed. Now clicking second time for descending order...");

			// SECOND CLICK - Sort Descending
			try {
				wait.until(ExpectedConditions.elementToBeClickable(orgJobNameHeader)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", orgJobNameHeader);
				} catch (Exception s) {
					utils.jsClick(driver, orgJobNameHeader);
				}
			}

			// ENHANCED: Wait specifically for loader to disappear after second click
			waitForLoaderToDisappear();
			PageObjectHelper.log(LOGGER,
					"Clicked two times on Organization job name / code header to Sort Job Profiles by Name in Descending order");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "sort_job_profiles_by_organiztion_job_name_in_descending_order",
					"Issue in clicking on Organization job name / code header to Sort Job Profiles by Name in Descending order",
					e);
		}
	}

	public void user_should_verify_first_thirty_job_profiles_sorted_by_organiztion_job_name_in_descending_order() {
		try {
			// ENHANCED: Ensure DOM is fully stable after sorting
			PerformanceUtils.waitForSpinnersToDisappear(driver);
			PerformanceUtils.waitForPageReady(driver, 3);
			Thread.sleep(3000); // Additional wait for DOM stability

			List<WebElement> allElements = driver
					.findElements(By.xpath("//tbody//tr//td[2]//div[contains(text(),'(')]"));
			PageObjectHelper.log(LOGGER,
					"Below are first thirty Job Profiles After sorting Job Profiles by Name in Descending Order:");

			// Get ALL Find Match buttons in ONE query, then map to job indices
			Set<Integer> unmappedJobIndices = new HashSet<>();
			try {
				List<WebElement> allButtons = driver
						.findElements(By.xpath("//*[@id='kf-job-container']//button[contains(text(),'Find Match')]"));

				for (WebElement button : allButtons) {
					try {
						// Get the row number of this button (0-based index)
						Long rowIndex = (Long) js.executeScript("var tr = arguments[0].closest('tr');"
								+ "var tbody = tr.parentElement;" + "return Array.from(tbody.children).indexOf(tr);",
								button);

						if (rowIndex != null && rowIndex >= 0) {
							// Right table (kf-job-container) has 2 rows per job
							// Each org job corresponds to 2 KF rows (0-1, 2-3, 4-5, etc.)
							int jobIndex = (int) (rowIndex / 2);
							unmappedJobIndices.add(jobIndex);
						}
					} catch (Exception e) {
						// Ignore if we can't determine the row
					}
				}
			} catch (Exception e) {
				LOGGER.warn("Error detecting unmapped jobs: " + e.getMessage());
			}

			LOGGER.info("Found " + unmappedJobIndices.size() + " unmapped job(s) with Find Match buttons");

			// Collect job names for validation (ONLY mapped jobs)
			ArrayList<String> jobNames = new ArrayList<String>();
			int specialCharCount = 0;
			int nonAsciiCount = 0;
			int unmappedCount = 0;

			for (int i = 0; i < allElements.size(); i++) {
				WebElement element = allElements.get(i);
				String text = element.getText();

				// INSTANT: O(1) lookup in HashSet
				boolean isUnmapped = unmappedJobIndices.contains(i);

				// Skip unmapped jobs from sort validation (they appear at top regardless of
				// sort)
				if (isUnmapped) {
					unmappedCount++;
					LOGGER.info("Organization Job Profile with Job Name / Code : " + text
							+ " - UNMAPPED (has Find Match button) - SKIPPED from sort validation");
					ExtentCucumberAdapter
							.addTestStepLog("Organization Job Profile: " + text + " - UNMAPPED - Appears at top");
					continue; // Skip this job from sort validation
				}

				// Add to validation list (only mapped jobs)
				jobNames.add(text); // Store original case for proper comparison

				// Detect non-ASCII characters at start (expected at top in descending)
				if (!text.isEmpty() && text.charAt(0) > 127) {
					nonAsciiCount++;
					LOGGER.info("Organization Job Profile with Job Name / Code : " + text
							+ " [NON-ASCII at start - expected at top in Descending]");
				}
				// Detect special characters
				else if (!text.isEmpty() && !Character.isLetterOrDigit(text.charAt(0))) {
					specialCharCount++;
					LOGGER.info("Organization Job Profile with Job Name / Code : " + text + " [SPECIAL CHAR detected]");
				} else {
					LOGGER.info("Organization Job Profile with Job Name / Code : " + text);
				}
				ExtentCucumberAdapter.addTestStepLog("Organization Job Profile with Name Job Name / Code : " + text);
			}

			if (unmappedCount > 0) {
				LOGGER.info("ℹ Found " + unmappedCount
						+ " unmapped job(s) - these appear at top and are EXCLUDED from sort validation");
				ExtentCucumberAdapter.addTestStepLog(
						"ℹ " + unmappedCount + " unmapped job(s) excluded from sort validation (appear at top)");
			}
			if (nonAsciiCount > 0) {
				LOGGER.info("ℹ Found " + nonAsciiCount
						+ " job(s) with non-ASCII characters - these appear at top in Descending order as expected");
				ExtentCucumberAdapter.addTestStepLog("ℹ " + nonAsciiCount
						+ " job(s) start with non-ASCII characters (Chinese, etc.) - expected at top");
			}
			if (specialCharCount > 0) {
				LOGGER.info("ℹ Found " + specialCharCount + " job(s) with special characters (?, -, etc.)");
				ExtentCucumberAdapter.addTestStepLog("ℹ " + specialCharCount + " job(s) start with special characters");
			}

			// ✅ VALIDATE DESCENDING ORDER (using case-insensitive comparison to match UI
			// behavior)
			// NOTE: Only validates MAPPED jobs - unmapped jobs appear at top and are
			// excluded
			int sortViolations = 0;
			for (int i = 0; i < jobNames.size() - 1; i++) {
				String current = jobNames.get(i);
				String next = jobNames.get(i + 1);
				// compareToIgnoreCase handles case-insensitive ordering: non-ASCII > ASCII
				// letters (A/a) > special chars
				if (current.compareToIgnoreCase(next) < 0) {
					sortViolations++;
					LOGGER.error("❌ SORT VIOLATION at Row " + (i + 1) + " -> Row " + (i + 2) + ": '" + current + "' < '"
							+ next + "' (NOT in Descending Order!)");
					ExtentCucumberAdapter.addTestStepLog("❌ SORT VIOLATION: Row " + (i + 1) + " < Row " + (i + 2));
				}
			}

			if (sortViolations > 0) {
				String errorMsg = "❌ SORTING FAILED: Found " + sortViolations
						+ " violation(s). Data is NOT sorted by Job Name in Descending Order!";
				LOGGER.error(errorMsg);
				ExtentCucumberAdapter.addTestStepLog(errorMsg);
				Assert.fail(errorMsg + " Please check the sorting implementation!");
			} else {
				LOGGER.info("✅ SORT VALIDATION PASSED: All " + jobNames.size()
						+ " MAPPED Job Profiles are correctly sorted by Job Name in Descending Order (including special chars and non-ASCII)");
				ExtentCucumberAdapter.addTestStepLog("✅ Sorting validation PASSED - " + jobNames.size()
						+ " mapped job(s) correctly sorted in Descending Order");
			}

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"user_should_verify_first_thirty_job_profiles_sorted_by_organiztion_job_name_in_descending_order",
					"Issue in Verifying first thirty Job Profiles After sorting Job Profiles by Name in Descending Order",
					e);
		}
	}

	/**
	 * Sorts job profiles by matched SP grade in ascending order Page already
	 * loaded, directly clicks header to sort ENHANCED: Added loader wait for
	 * stability
	 */
	public void sort_job_profiles_by_matched_success_profile_grade_in_ascending_order() {
		try {
			wait.until(ExpectedConditions.visibilityOf(matchedSPGradeHeader)).click();
			waitForLoaderToDisappear();
			PageObjectHelper.log(LOGGER,
					"Clicked on Matched Success Profile Grade header to Sort Job Profiles by Matched SP Grade in ascending order");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"sort_job_profiles_by_matched_success_profile_grade_in_ascending_order",
					"Issue in clicking on Matched Success Profile Grade header to Sort Job Profiles by Matched SP Grade in ascending order",
					e);
		}
	}

	public void user_should_verify_first_thirty_job_profiles_sorted_by_matched_success_profile_grade_in_ascending_order() {
		try {
			// ENHANCED: Ensure DOM is fully stable after sorting
			PerformanceUtils.waitForSpinnersToDisappear(driver);
			PerformanceUtils.waitForPageReady(driver, 3);
			Thread.sleep(3000); // Additional wait for DOM stability

			List<WebElement> allElements = driver
					.findElements(By.xpath("//tbody//tr//td[2]//div[contains(text(),'(')]"));
			PageObjectHelper.log(LOGGER,
					"Below are Job Profiles After sorting by Matched Success Profile Grade in Ascending Order:");

			// FAST: Pre-detect unmapped jobs using Find Match buttons
			Set<Integer> unmappedJobIndices = new HashSet<>();
			try {
				List<WebElement> allButtons = driver
						.findElements(By.xpath("//*[@id='kf-job-container']//button[contains(text(),'Find Match')]"));
				for (WebElement button : allButtons) {
					try {
						Long rowIndex = (Long) js.executeScript("var tr = arguments[0].closest('tr');"
								+ "var tbody = tr.parentElement;" + "return Array.from(tbody.children).indexOf(tr);",
								button);
						if (rowIndex != null && rowIndex >= 0) {
							int jobIndex = (int) (rowIndex / 2);
							unmappedJobIndices.add(jobIndex);
						}
					} catch (Exception e) {
						// Ignore if we can't determine the row
					}
				}
			} catch (Exception e) {
				LOGGER.warn("Error detecting unmapped jobs: " + e.getMessage());
			}
			LOGGER.info("Found " + unmappedJobIndices.size() + " unmapped job(s) with Find Match buttons");

			// ULTRA-FAST: Pre-fetch ALL SP Grades at once (single query!)
			List<WebElement> allGradeElements = driver
					.findElements(By.xpath("//*[@id='kf-job-container']/div/table/tbody/tr//td[2]/div"));
			LOGGER.info("Pre-fetched " + allGradeElements.size() + " SP Grade elements from KF table");

			// Collect SP grades for validation (only mapped jobs)
			ArrayList<String> spGrades = new ArrayList<String>();
			int specialCharCount = 0;
			int nonAsciiCount = 0;
			int unmappedCount = 0;
			int gradeIndex = 0; // Track current position in grade elements

			// Iterate through org job profiles and match with pre-fetched grades
			for (int i = 0; i < allElements.size(); i++) {
				WebElement element = allElements.get(i);
				String text = element.getText();

				// Check if this is an unmapped job
				if (unmappedJobIndices.contains(i)) {
					unmappedCount++;
					LOGGER.info("Organization Job Profile with Job Name / Code : " + text
							+ " - UNMAPPED (has Find Match button) - SKIPPED from sort validation");
					ExtentCucumberAdapter
							.addTestStepLog("Organization Job Profile: " + text + " - UNMAPPED (No SP Grade)");
					continue; // Skip to next job, don't increment gradeIndex
				}

				// For mapped jobs: Get grade from pre-fetched list
				String gradeText = "";
				if (gradeIndex < allGradeElements.size()) {
					try {
						gradeText = allGradeElements.get(gradeIndex).getText();
						gradeIndex++;
					} catch (Exception e) {
						gradeText = "";
					}
				}

				// Store grade for validation
				if (gradeText != null && !gradeText.trim().isEmpty() && !gradeText.equals("-")) {
					spGrades.add(gradeText);

					// Detect special characters
					if (!gradeText.isEmpty() && !Character.isLetterOrDigit(gradeText.charAt(0))) {
						specialCharCount++;
						LOGGER.info("Organization Job Profile with Job Name / Code : " + text
								+ " with Matched SP Grade : " + gradeText + " [SPECIAL CHAR at start]");
					}
					// Detect non-ASCII characters
					else if (!gradeText.isEmpty() && gradeText.charAt(0) > 127) {
						nonAsciiCount++;
						LOGGER.info("Organization Job Profile with Job Name / Code : " + text
								+ " with Matched SP Grade : " + gradeText + " [NON-ASCII]");
					} else {
						LOGGER.info("Organization Job Profile with Job Name / Code : " + text
								+ " with Matched SP Grade : " + gradeText);
					}
					ExtentCucumberAdapter.addTestStepLog(
							"Organization Job Profile: " + text + " with Matched SP Grade : " + gradeText);
				} else {
					unmappedCount++;
					LOGGER.info(
							"Organization Job Profile with Job Name / Code : " + text + " - UNMAPPED (No SP Grade)");
					ExtentCucumberAdapter
							.addTestStepLog("Organization Job Profile: " + text + " - UNMAPPED (No SP Grade)");
				}
			}

			if (unmappedCount > 0) {
				LOGGER.info("Found " + unmappedCount + " unmapped job(s) - excluded from SP Grade sort validation");
			}
			if (specialCharCount > 0) {
				LOGGER.info("ℹ Found " + specialCharCount
						+ " SP grade(s) with special characters - expected at top in Ascending order");
			}
			if (nonAsciiCount > 0) {
				LOGGER.info("ℹ Found " + nonAsciiCount + " SP grade(s) with non-ASCII characters");
			}

			// ✅ VALIDATE ASCENDING ORDER (only for mapped jobs with grades)
			// NOTE: Use NUMERIC comparison since grades are numbers (9, 12, 15, etc.)
			int sortViolations = 0;
			for (int i = 0; i < spGrades.size() - 1; i++) {
				String current = spGrades.get(i);
				String next = spGrades.get(i + 1);

				// Try numeric comparison first, fall back to string if not numeric
				try {
					int currentNum = Integer.parseInt(current);
					int nextNum = Integer.parseInt(next);
					if (currentNum > nextNum) {
						sortViolations++;
						LOGGER.error("❌ SORT VIOLATION: SP Grade at position " + (i + 1) + " (" + current
								+ ") > position " + (i + 2) + " (" + next + ") - NOT in Ascending Order!");
						ExtentCucumberAdapter.addTestStepLog(
								"❌ SORT VIOLATION: SP Grade position " + (i + 1) + " > position " + (i + 2));
					}
				} catch (NumberFormatException e) {
					// Fall back to string comparison for non-numeric grades
					if (current.compareToIgnoreCase(next) > 0) {
						sortViolations++;
						LOGGER.error("❌ SORT VIOLATION: SP Grade at position " + (i + 1) + " (" + current
								+ ") > position " + (i + 2) + " (" + next + ") - NOT in Ascending Order!");
						ExtentCucumberAdapter.addTestStepLog(
								"❌ SORT VIOLATION: SP Grade position " + (i + 1) + " > position " + (i + 2));
					}
				}
			}

			if (sortViolations > 0) {
				String errorMsg = "❌ SORTING FAILED: Found " + sortViolations
						+ " violation(s). SP Grades are NOT sorted in Ascending Order!";
				LOGGER.error(errorMsg);
				ExtentCucumberAdapter.addTestStepLog(errorMsg);
				Assert.fail(errorMsg + " Please check the sorting implementation!");
			} else if (spGrades.size() > 1) {
				LOGGER.info("✅ SORT VALIDATION PASSED: " + spGrades.size()
						+ " SP Grades are correctly sorted in Ascending Order");
				ExtentCucumberAdapter.addTestStepLog(
						"✅ Sorting validation PASSED - SP Grades are correctly sorted in Ascending Order");
			} else {
				LOGGER.info("ℹ VALIDATION SKIPPED: Not enough mapped jobs with SP grades to validate sorting ("
						+ spGrades.size() + " job(s) with grades)");
				ExtentCucumberAdapter.addTestStepLog("ℹ Validation skipped - insufficient data for sorting validation");
			}

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"user_should_verify_first_thirty_job_profiles_sorted_by_matched_success_profile_grade_in_ascending_order",
					"Issue in Verifying Job Profiles After sorting by Matched Success Profile Grade in Ascending Order",
					e);
		}
	}

	/**
	 * Sorts job profiles by matched SP grade in descending order (clicks header
	 * twice) Page already loaded, directly clicks to sort Enhanced with proper
	 * waits for HEADLESS MODE compatibility
	 */
	public void sort_job_profiles_by_matched_success_profile_grade_in_descending_order() {
		try {
			// FIRST CLICK - Sort Ascending
			PageObjectHelper.log(LOGGER, "First click on SP Grade header to sort ascending...");
			try {
				wait.until(ExpectedConditions.elementToBeClickable(matchedSPGradeHeader)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", matchedSPGradeHeader);
				} catch (Exception s) {
					utils.jsClick(driver, matchedSPGradeHeader);
				}
			}

			// ENHANCED: Wait specifically for loader to disappear after first click
			waitForLoaderToDisappear();
			PageObjectHelper.log(LOGGER, "First sort completed. Now clicking second time for descending order...");
			Thread.sleep(2000);

			// SECOND CLICK - Sort Descending
			try {
				wait.until(ExpectedConditions.elementToBeClickable(matchedSPGradeHeader)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", matchedSPGradeHeader);
				} catch (Exception s) {
					utils.jsClick(driver, matchedSPGradeHeader);
				}
			}

			// ENHANCED: Wait specifically for loader to disappear after second click
			waitForLoaderToDisappear();
			Thread.sleep(2000);
			PageObjectHelper.log(LOGGER,
					"Clicked two times on Matched Success Profile Grade header to Sort Job Profiles by Matched SP Grade in descending order");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"sort_job_profiles_by_matched_success_profile_grade_in_descending_order",
					"Issue in clicking two times on Matched Success Profile Grade header to Sort Job Profiles by Matched SP Grade in descending order",
					e);
		}
	}

	public void user_should_verify_first_thirty_job_profiles_sorted_by_matched_success_profile_grade_in_descending_order() {
		try {
			// ENHANCED: Ensure DOM is fully stable after sorting
			PerformanceUtils.waitForSpinnersToDisappear(driver);
			PerformanceUtils.waitForPageReady(driver, 3);
			Thread.sleep(3000); // Additional wait for DOM stability

			List<WebElement> allElements = driver
					.findElements(By.xpath("//tbody//tr//td[2]//div[contains(text(),'(')]"));
			PageObjectHelper.log(LOGGER,
					"Below are Job Profiles After sorting by Matched Success Profile Grade in Descending Order:");

			// FAST: Pre-detect unmapped jobs using Find Match buttons
			Set<Integer> unmappedJobIndices = new HashSet<>();
			try {
				List<WebElement> allButtons = driver
						.findElements(By.xpath("//*[@id='kf-job-container']//button[contains(text(),'Find Match')]"));
				for (WebElement button : allButtons) {
					try {
						Long rowIndex = (Long) js.executeScript("var tr = arguments[0].closest('tr');"
								+ "var tbody = tr.parentElement;" + "return Array.from(tbody.children).indexOf(tr);",
								button);
						if (rowIndex != null && rowIndex >= 0) {
							int jobIndex = (int) (rowIndex / 2);
							unmappedJobIndices.add(jobIndex);
						}
					} catch (Exception e) {
						// Ignore if we can't determine the row
					}
				}
			} catch (Exception e) {
				LOGGER.warn("Error detecting unmapped jobs: " + e.getMessage());
			}
			LOGGER.info("Found " + unmappedJobIndices.size() + " unmapped job(s) with Find Match buttons");

			// ULTRA-FAST: Pre-fetch ALL SP Grades at once (single query!)
			List<WebElement> allGradeElements = driver
					.findElements(By.xpath("//*[@id='kf-job-container']/div/table/tbody/tr//td[2]/div"));
			LOGGER.info("Pre-fetched " + allGradeElements.size() + " SP Grade elements from KF table");

			// Collect SP grades for validation (only mapped jobs)
			ArrayList<String> spGrades = new ArrayList<String>();
			int specialCharCount = 0;
			int nonAsciiCount = 0;
			int unmappedCount = 0;
			int gradeIndex = 0; // Track current position in grade elements

			// Iterate through org job profiles and match with pre-fetched grades
			for (int i = 0; i < allElements.size(); i++) {
				WebElement element = allElements.get(i);
				String text = element.getText();

				// Check if this is an unmapped job
				if (unmappedJobIndices.contains(i)) {
					unmappedCount++;
					LOGGER.info("Organization Job Profile with Job Name / Code : " + text
							+ " - UNMAPPED (has Find Match button) - SKIPPED from sort validation");
					ExtentCucumberAdapter
							.addTestStepLog("Organization Job Profile: " + text + " - UNMAPPED (No SP Grade)");
					continue; // Skip to next job, don't increment gradeIndex
				}

				// For mapped jobs: Get grade from pre-fetched list
				String gradeText = "";
				if (gradeIndex < allGradeElements.size()) {
					try {
						gradeText = allGradeElements.get(gradeIndex).getText();
						gradeIndex++;
					} catch (Exception e) {
						gradeText = "";
					}
				}

				// Store grade for validation
				if (gradeText != null && !gradeText.trim().isEmpty() && !gradeText.equals("-")) {
					spGrades.add(gradeText);

					// Detect special characters
					if (!gradeText.isEmpty() && !Character.isLetterOrDigit(gradeText.charAt(0))) {
						specialCharCount++;
						LOGGER.info("Organization Job Profile with Job Name / Code : " + text
								+ " with Matched SP Grade : " + gradeText + " [SPECIAL CHAR at start]");
					}
					// Detect non-ASCII characters
					else if (!gradeText.isEmpty() && gradeText.charAt(0) > 127) {
						nonAsciiCount++;
						LOGGER.info("Organization Job Profile with Job Name / Code : " + text
								+ " with Matched SP Grade : " + gradeText + " [NON-ASCII]");
					} else {
						LOGGER.info("Organization Job Profile with Job Name / Code : " + text
								+ " with Matched SP Grade : " + gradeText);
					}
					ExtentCucumberAdapter.addTestStepLog(
							"Organization Job Profile: " + text + " with Matched SP Grade : " + gradeText);
				} else {
					unmappedCount++;
					LOGGER.info(
							"Organization Job Profile with Job Name / Code : " + text + " - UNMAPPED (No SP Grade)");
					ExtentCucumberAdapter
							.addTestStepLog("Organization Job Profile: " + text + " - UNMAPPED (No SP Grade)");
				}
			}

			if (unmappedCount > 0) {
				LOGGER.info("Found " + unmappedCount + " unmapped job(s) - excluded from SP Grade sort validation");
			}
			if (nonAsciiCount > 0) {
				LOGGER.info("ℹ Found " + nonAsciiCount
						+ " SP grade(s) with non-ASCII characters - expected at top in Descending order");
				ExtentCucumberAdapter
						.addTestStepLog("ℹ " + nonAsciiCount + " SP grade(s) start with non-ASCII characters");
			}
			if (specialCharCount > 0) {
				LOGGER.info("ℹ Found " + specialCharCount + " SP grade(s) with special characters");
				ExtentCucumberAdapter
						.addTestStepLog("ℹ " + specialCharCount + " SP grade(s) start with special characters");
			}

			// ✅ VALIDATE DESCENDING ORDER (only for mapped jobs) - NUMERIC comparison
			int sortViolations = 0;
			for (int i = 0; i < spGrades.size() - 1; i++) {
				String current = spGrades.get(i);
				String next = spGrades.get(i + 1);

				// Try numeric comparison first, fall back to string if not numeric
				try {
					int currentNum = Integer.parseInt(current);
					int nextNum = Integer.parseInt(next);
					if (currentNum < nextNum) {
						sortViolations++;
						LOGGER.error("❌ SORT VIOLATION: SP Grade at position " + (i + 1) + " (" + current
								+ ") < position " + (i + 2) + " (" + next + ") - NOT in Descending Order!");
						ExtentCucumberAdapter.addTestStepLog(
								"❌ SORT VIOLATION: SP Grade position " + (i + 1) + " < position " + (i + 2));
					}
				} catch (NumberFormatException e) {
					// Fall back to string comparison for non-numeric grades
					if (current.compareToIgnoreCase(next) < 0) {
						sortViolations++;
						LOGGER.error("❌ SORT VIOLATION: SP Grade at position " + (i + 1) + " (" + current
								+ ") < position " + (i + 2) + " (" + next + ") - NOT in Descending Order!");
						ExtentCucumberAdapter.addTestStepLog(
								"❌ SORT VIOLATION: SP Grade position " + (i + 1) + " < position " + (i + 2));
					}
				}
			}

			if (sortViolations > 0) {
				String errorMsg = "❌ SORTING FAILED: Found " + sortViolations
						+ " violation(s). SP Grades are NOT sorted in Descending Order!";
				LOGGER.error(errorMsg);
				ExtentCucumberAdapter.addTestStepLog(errorMsg);
				Assert.fail(errorMsg + " Please check the sorting implementation!");
			} else if (spGrades.size() > 1) {
				LOGGER.info("✅ SORT VALIDATION PASSED: " + spGrades.size()
						+ " SP Grades are correctly sorted in Descending Order");
				ExtentCucumberAdapter.addTestStepLog(
						"✅ Sorting validation PASSED - SP Grades are correctly sorted in Descending Order");
			} else {
				LOGGER.info("ℹ VALIDATION SKIPPED: Not enough mapped jobs with SP grades to validate sorting ("
						+ spGrades.size() + " job(s) with grades)");
				ExtentCucumberAdapter.addTestStepLog("ℹ Validation skipped - insufficient data for sorting validation");
			}

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"user_should_verify_first_thirty_job_profiles_sorted_by_matched_success_profile_grade_in_descending_order",
					"Issue in Verifying Job Profiles After sorting by Matched Success Profile Grade in Descending Order",
					e);
		}
	}

	/**
	 * Sorts job profiles by matched SP name in ascending order Page already loaded,
	 * directly clicks header to sort
	 */
	public void sort_job_profiles_by_matched_success_profile_name_in_ascending_order() {
		try {
			wait.until(ExpectedConditions.visibilityOf(matcheSPNameHeader)).click();
			PerformanceUtils.waitForSpinnersToDisappear(driver);
			PerformanceUtils.waitForPageReady(driver, 3);
			PageObjectHelper.log(LOGGER,
					"Clicked on Matched Success Profile Name header to Sort Job Profiles by Matched SP Name in ascending order");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "sort_job_profiles_by_matched_success_profile_name_in_ascending_order",
					"Issue in clicking on Matched Success Profile Name header to Sort Job Profiles by Matched SP Name in ascending order",
					e);
		}
	}

	public void user_should_verify_first_thirty_job_profiles_sorted_by_matched_success_profile_name_in_ascending_order() {
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver);
			PerformanceUtils.waitForPageReady(driver, 5);
			Thread.sleep(3000);
			List<WebElement> allElements = driver
					.findElements(By.xpath("//tbody//tr//td[2]//div[contains(text(),'(')]"));
			PageObjectHelper.log(LOGGER,
					"Below are Job Profiles After sorting by Matched Success Profile Name in Ascending Order:");

			// Collect SP names for validation (only mapped jobs)
			ArrayList<String> spNames = new ArrayList<String>();
			int specialCharCount = 0;
			int nonAsciiCount = 0;
			int unmappedCount = 0;

			// Iterate through org job profiles and get corresponding SP Names
			// SP Names appear at tr[1], tr[4], tr[7]... pattern (every 3rd row starting
			// from index 1)
			for (int i = 0; i < allElements.size(); i++) {
				WebElement element = allElements.get(i);
				String text = element.getText();

				// Calculate the correct tr index for this SP Name: tr[3*i + 1]
				int spRowIndex = (3 * i) + 1;

				// Try to find the SP Name element at the correct row
				String NameText = "";
				try {
					WebElement MatchedSPNameElement = driver.findElement(
							By.xpath("//*[@id='kf-job-container']/div/table/tbody/tr[" + spRowIndex + "]/td[1]/div"));
					NameText = MatchedSPNameElement.getText();
				} catch (Exception spNameEx) {
					// SP Name element not found at expected position - likely unmapped job
					NameText = "";
					LOGGER.warn("SP Name element not found at tr[" + spRowIndex + "] for job: " + text);
				}

				// Check if this is an unmapped job (no SP name)
				if (NameText == null || NameText.trim().isEmpty() || NameText.equals("-")) {
					unmappedCount++;
					LOGGER.info("Organization Job Profile with Job Name / Code : " + text + " - UNMAPPED (No SP Name)");
					ExtentCucumberAdapter
							.addTestStepLog("Organization Job Profile: " + text + " - UNMAPPED (No SP Name)");
				} else {
					spNames.add(NameText); // Store for validation (keep original case for Unicode)

					// Detect special characters at start (expected at top in ascending)
					if (!NameText.isEmpty() && !Character.isLetterOrDigit(NameText.charAt(0))) {
						specialCharCount++;
						LOGGER.info("Organization Job Profile with Job Name / Code : " + text
								+ " with Matched SP Name : " + NameText + " [SPECIAL CHAR at start]");
					}
					// Detect non-ASCII characters
					else if (!NameText.isEmpty() && NameText.charAt(0) > 127) {
						nonAsciiCount++;
						LOGGER.info("Organization Job Profile with Job Name / Code : " + text
								+ " with Matched SP Name : " + NameText + " [NON-ASCII]");
					} else {
						LOGGER.info("Organization Job Profile with Job Name / Code : " + text
								+ " with Matched SP Name : " + NameText);
					}
					ExtentCucumberAdapter.addTestStepLog(
							"Organization Job Profile: " + text + " with Matched SP Name : " + NameText);
				}
			}

			if (unmappedCount > 0) {
				LOGGER.info("Found {} unmapped job(s) without SP name details", unmappedCount);
				ExtentCucumberAdapter
						.addTestStepLog("Found " + unmappedCount + " unmapped job(s) without SP name details");
			}
			if (specialCharCount > 0) {
				LOGGER.info("ℹ Found " + specialCharCount
						+ " SP name(s) with special characters - expected at top in Ascending order");
				ExtentCucumberAdapter
						.addTestStepLog("ℹ " + specialCharCount + " SP name(s) start with special characters");
			}
			if (nonAsciiCount > 0) {
				LOGGER.info("ℹ Found " + nonAsciiCount + " SP name(s) with non-ASCII characters");
				ExtentCucumberAdapter.addTestStepLog("ℹ " + nonAsciiCount + " SP name(s) contain non-ASCII characters");
			}

			// ✅ VALIDATE ASCENDING ORDER (only for mapped jobs) - case-insensitive
			int sortViolations = 0;
			for (int i = 0; i < spNames.size() - 1; i++) {
				String current = spNames.get(i);
				String next = spNames.get(i + 1);
				if (current.compareToIgnoreCase(next) > 0) {
					sortViolations++;
					LOGGER.error("❌ SORT VIOLATION: SP Name at position " + (i + 1) + " (" + current + ") > position "
							+ (i + 2) + " (" + next + ") - NOT in Ascending Order!");
					ExtentCucumberAdapter
							.addTestStepLog("❌ SORT VIOLATION: SP Name position " + (i + 1) + " > position " + (i + 2));
				}
			}

			if (sortViolations > 0) {
				String errorMsg = "❌ SORTING FAILED: Found " + sortViolations
						+ " violation(s). SP Names are NOT sorted in Ascending Order!";
				LOGGER.error(errorMsg);
				ExtentCucumberAdapter.addTestStepLog(errorMsg);
				Assert.fail(errorMsg + " Please check the sorting implementation!");
			} else if (spNames.size() > 1) {
				LOGGER.info("✅ SORT VALIDATION PASSED: " + spNames.size()
						+ " SP Names are correctly sorted in Ascending Order");
				ExtentCucumberAdapter.addTestStepLog(
						"✅ Sorting validation PASSED - SP Names are correctly sorted in Ascending Order");
			} else {
				LOGGER.info("ℹ VALIDATION SKIPPED: Not enough mapped jobs with SP names to validate sorting ("
						+ spNames.size() + " job(s) with names)");
				ExtentCucumberAdapter.addTestStepLog("ℹ Validation skipped - insufficient data for sorting validation");
			}

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"user_should_verify_first_thirty_job_profiles_sorted_by_matched_success_profile_name_in_ascending_order",
					"Issue in Verifying Job Profiles After sorting by Matched Success Profile Name in Ascending Order",
					e);
		}
	}

	/**
	 * Sorts job profiles by organization grade in ascending order Page already
	 * loaded, clicks header and waits for sort to complete
	 */
	public void sort_job_profiles_by_organization_grade_in_ascending_order() {
		try {
			wait.until(ExpectedConditions.visibilityOf(orgJobGradeHeader)).click();
			PerformanceUtils.waitForSpinnersToDisappear(driver);
			PerformanceUtils.waitForPageReady(driver, 3);
			PageObjectHelper.log(LOGGER,
					"Clicked on Organization Grade header to Sort Job Profiles by Grade in ascending order");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "sort_job_profiles_by_organization_grade_in_ascending_order",
					"Issue in clicking on Organization Grade header to Sort Job Profiles by Grade in ascending order",
					e);
		}
	}

	public void user_should_verify_first_thirty_job_profiles_sorted_by_organization_grade_and_organization_job_name_in_ascending_order() {
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver);
			PerformanceUtils.waitForPageReady(driver, 3);
			Thread.sleep(3000);

			List<WebElement> allElements = driver
					.findElements(By.xpath("//tbody//tr//td[2]//div[contains(text(),'(')]"));

			// Dynamically find all Organization Grade elements that actually exist
			List<WebElement> OrgGradeElements = driver
					.findElements(By.xpath("//*[@id='org-job-container']/div/table/tbody/tr/td[3]/div"));

			PageObjectHelper.log(LOGGER,
					"Below are Job Profiles After sorting by Organization Grade and Organization Job Name in Ascending Order:");

			// Use the smaller size to avoid IndexOutOfBoundsException
			int iterationLimit = Math.min(allElements.size(), OrgGradeElements.size());

			// Collect org grades for validation
			ArrayList<String> orgGrades = new ArrayList<String>();
			int specialCharCount = 0;
			int nonAsciiCount = 0;

			for (int i = 0; i < iterationLimit; i++) {
				WebElement element = allElements.get(i);
				String text = element.getText();
				WebElement OrgGradeElement = OrgGradeElements.get(i);
				String GradeText = OrgGradeElement.getText();

				// Store grade for validation (if not empty)
				if (GradeText != null && !GradeText.trim().isEmpty() && !GradeText.equals("-")) {
					orgGrades.add(GradeText); // Keep original case for Unicode

					// Detect special characters at start (expected at top in ascending)
					if (!GradeText.isEmpty() && !Character.isLetterOrDigit(GradeText.charAt(0))) {
						specialCharCount++;
						LOGGER.info("Organization Job Profile with Job Name / Code : " + text
								+ " with Organization Job Grade : " + GradeText + " [SPECIAL CHAR at start]");
					}
					// Detect non-ASCII characters
					else if (!GradeText.isEmpty() && GradeText.charAt(0) > 127) {
						nonAsciiCount++;
						LOGGER.info("Organization Job Profile with Job Name / Code : " + text
								+ " with Organization Job Grade : " + GradeText + " [NON-ASCII]");
					} else {
						LOGGER.info("Organization Job Profile with Job Name / Code : " + text
								+ " with Organization Job Grade : " + GradeText);
					}
				} else {
					LOGGER.info("Organization Job Profile with Job Name / Code : " + text
							+ " with Organization Job Grade : " + GradeText);
				}
				ExtentCucumberAdapter.addTestStepLog("Organization Job Profile with Job Name / Code : " + text
						+ " with Organization Job Grade : " + GradeText);
			}

			if (specialCharCount > 0) {
				LOGGER.info("ℹ Found " + specialCharCount
						+ " org grade(s) with special characters - expected at top in Ascending order");
				ExtentCucumberAdapter
						.addTestStepLog("ℹ " + specialCharCount + " org grade(s) start with special characters");
			}
			if (nonAsciiCount > 0) {
				LOGGER.info("ℹ Found " + nonAsciiCount + " org grade(s) with non-ASCII characters");
				ExtentCucumberAdapter
						.addTestStepLog("ℹ " + nonAsciiCount + " org grade(s) contain non-ASCII characters");
			}

			// ✅ VALIDATE MULTI-LEVEL SORTING: Grade (ASC) → Job Name (ASC within each grade
			// group)
			int sortViolations = 0;

			// Collect job data with both grade and name
			List<Map.Entry<String, String>> jobData = new ArrayList<>();
			for (int i = 0; i < iterationLimit; i++) {
				String jobName = allElements.get(i).getText();
				String grade = OrgGradeElements.get(i).getText();
				if (grade != null && !grade.trim().isEmpty() && !grade.equals("-")) {
					jobData.add(new AbstractMap.SimpleEntry<>(grade, jobName));
				}
			}

			// Validate primary sort (Grade) and secondary sort (Job Name within each grade)
			for (int i = 0; i < jobData.size() - 1; i++) {
				String currentGrade = jobData.get(i).getKey();
				String currentName = jobData.get(i).getValue();
				String nextGrade = jobData.get(i + 1).getKey();
				String nextName = jobData.get(i + 1).getValue();

				// Primary sort: Grade must be ascending
				int gradeComparison = currentGrade.compareToIgnoreCase(nextGrade);
				if (gradeComparison > 0) {
					sortViolations++;
					LOGGER.error("❌ SORT VIOLATION: Grade at position " + (i + 1) + " (" + currentGrade
							+ ") > position " + (i + 2) + " (" + nextGrade + ") - NOT in Ascending Order!");
					ExtentCucumberAdapter
							.addTestStepLog("❌ SORT VIOLATION: Grade position " + (i + 1) + " > position " + (i + 2));
				}
				// Secondary sort: If grades are equal, job names must be ascending
				else if (gradeComparison == 0 && currentName.compareToIgnoreCase(nextName) > 0) {
					sortViolations++;
					LOGGER.error("❌ SORT VIOLATION: Within Grade '" + currentGrade + "', Job Name at position "
							+ (i + 1) + " ('" + currentName + "') > position " + (i + 2) + " ('" + nextName
							+ "') - NOT in Ascending Order!");
					ExtentCucumberAdapter.addTestStepLog("❌ SORT VIOLATION: Job Name position " + (i + 1)
							+ " > position " + (i + 2) + " within same grade");
				}
			}

			if (sortViolations > 0) {
				String errorMsg = "❌ MULTI-LEVEL SORTING FAILED: Found " + sortViolations
						+ " violation(s). Data is NOT sorted by Grade→Job Name in Ascending Order!";
				LOGGER.error(errorMsg);
				ExtentCucumberAdapter.addTestStepLog(errorMsg);
				Assert.fail(errorMsg + " Please check the sorting implementation!");
			} else if (jobData.size() > 1) {
				LOGGER.info("✅ MULTI-LEVEL SORT VALIDATION PASSED: " + jobData.size()
						+ " jobs correctly sorted by Grade (ASC) → Job Name (ASC)");
				ExtentCucumberAdapter
						.addTestStepLog("✅ Multi-level sorting validation PASSED - Grade→Job Name correctly sorted");
			}

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"user_should_verify_first_thirty_job_profiles_sorted_by_organization_grade_and_organization_job_name_in_ascending_order",
					"Issue in Verifying Job Profiles After sorting by Organization Grade and Organization Job Name in Ascending Order",
					e);
		}
	}

	/**
	 * Sorts job profiles by organization grade in descending order (clicks header
	 * twice) Enhanced with proper waits for HEADLESS MODE compatibility
	 */
	public void sort_job_profiles_by_organization_grade_in_descending_order() {
		try {
			// FIRST CLICK - Sort Ascending
			PageObjectHelper.log(LOGGER, "First click on Organization Grade header to sort ascending...");
			try {
				wait.until(ExpectedConditions.elementToBeClickable(orgJobGradeHeader)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", orgJobGradeHeader);
				} catch (Exception s) {
					utils.jsClick(driver, orgJobGradeHeader);
				}
			}

			// ENHANCED: Wait specifically for loader to disappear after first click
			waitForLoaderToDisappear();
			PageObjectHelper.log(LOGGER, "First sort completed. Now clicking second time for descending order...");
			Thread.sleep(2000);
			// SECOND CLICK - Sort Descending
			try {
				wait.until(ExpectedConditions.elementToBeClickable(orgJobGradeHeader)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", orgJobGradeHeader);
				} catch (Exception s) {
					utils.jsClick(driver, orgJobGradeHeader);
				}
			}

			// ENHANCED: Wait specifically for loader to disappear after second click
			waitForLoaderToDisappear();
			Thread.sleep(2000);
			PageObjectHelper.log(LOGGER,
					"Clicked two times on Organization Grade header to Sort Job Profiles by Grade in Descending order");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "sort_job_profiles_by_organization_grade_in_descending_order",
					"Issue in clicking on Organization Grade to Sort Job Profiles by Grade in Descending order", e);
		}
	}

	public void user_should_verify_first_thirty_job_profiles_sorted_by_organization_grade_in_descending_order_and_organization_job_name_in_ascending_order() {
		try {
			// ENHANCED: Ensure DOM is fully stable after sorting
			PerformanceUtils.waitForSpinnersToDisappear(driver);
			PerformanceUtils.waitForPageReady(driver, 3);
			Thread.sleep(3000); // Additional wait for DOM stability

			// FIXED: Get element counts first to avoid stale elements
			int elementCount = driver.findElements(By.xpath("//tbody//tr//td[2]//div[contains(text(),'(')]")).size();
			int gradeElementCount = driver
					.findElements(By.xpath("//*[@id='org-job-container']/div/table/tbody/tr/td[3]/div")).size();

			PageObjectHelper.log(LOGGER,
					"Below are Job Profiles After sorting by Organization Grade in Descending Order and Organization Job Name in Ascending Order:");

			// Use the smaller size to avoid IndexOutOfBoundsException
			int iterationLimit = Math.min(elementCount, gradeElementCount);

			// Collect org grades for validation
			ArrayList<String> orgGrades = new ArrayList<String>();
			int specialCharCount = 0;
			int nonAsciiCount = 0;

			for (int i = 0; i < iterationLimit; i++) {
				// FIXED: Re-find elements on each iteration to avoid stale element
				WebElement element = driver.findElements(By.xpath("//tbody//tr//td[2]//div[contains(text(),'(')]"))
						.get(i);
				String text = element.getText();
				WebElement OrgGradeElement = driver
						.findElements(By.xpath("//*[@id='org-job-container']/div/table/tbody/tr/td[3]/div")).get(i);
				String GradeText = OrgGradeElement.getText();

				// Store grade for validation (if not empty)
				if (GradeText != null && !GradeText.trim().isEmpty() && !GradeText.equals("-")) {
					orgGrades.add(GradeText); // Keep original case for Unicode

					// Detect non-ASCII characters at start (expected at top in descending)
					if (!GradeText.isEmpty() && GradeText.charAt(0) > 127) {
						nonAsciiCount++;
						LOGGER.info("Organization Job Profile with Job Name / Code : " + text
								+ " with Organization Job Grade : " + GradeText + " [NON-ASCII at start]");
					}
					// Detect special characters
					else if (!GradeText.isEmpty() && !Character.isLetterOrDigit(GradeText.charAt(0))) {
						specialCharCount++;
						LOGGER.info("Organization Job Profile with Job Name / Code : " + text
								+ " with Organization Job Grade : " + GradeText + " [SPECIAL CHAR]");
					} else {
						LOGGER.info("Organization Job Profile with Job Name / Code : " + text
								+ " with Organization Job Grade : " + GradeText);
					}
				} else {
					LOGGER.info("Organization Job Profile with Job Name / Code : " + text
							+ " with Organization Job Grade : " + GradeText);
				}
				ExtentCucumberAdapter.addTestStepLog("Organization Job Profile with Job Name / Code : " + text
						+ " with Organization Job Grade : " + GradeText);
			}

			if (nonAsciiCount > 0) {
				LOGGER.info("ℹ Found " + nonAsciiCount
						+ " org grade(s) with non-ASCII characters - expected at top in Descending order");
				ExtentCucumberAdapter
						.addTestStepLog("ℹ " + nonAsciiCount + " org grade(s) start with non-ASCII characters");
			}
			if (specialCharCount > 0) {
				LOGGER.info("ℹ Found " + specialCharCount + " org grade(s) with special characters");
				ExtentCucumberAdapter
						.addTestStepLog("ℹ " + specialCharCount + " org grade(s) start with special characters");
			}

			// ✅ VALIDATE MULTI-LEVEL SORTING: Grade (DESC) → Job Name (ASC within each
			// grade group)
			int sortViolations = 0;

			// Collect job data with both grade and name
			List<Map.Entry<String, String>> jobData = new ArrayList<>();
			for (int i = 0; i < iterationLimit; i++) {
				WebElement element = driver.findElements(By.xpath("//tbody//tr//td[2]//div[contains(text(),'(')]"))
						.get(i);
				String jobName = element.getText();
				WebElement OrgGradeElement = driver
						.findElements(By.xpath("//*[@id='org-job-container']/div/table/tbody/tr/td[3]/div")).get(i);
				String grade = OrgGradeElement.getText();
				if (grade != null && !grade.trim().isEmpty() && !grade.equals("-")) {
					jobData.add(new AbstractMap.SimpleEntry<>(grade, jobName));
				}
			}

			// Validate primary sort (Grade DESC) and secondary sort (Job Name ASC within
			// each grade)
			for (int i = 0; i < jobData.size() - 1; i++) {
				String currentGrade = jobData.get(i).getKey();
				String currentName = jobData.get(i).getValue();
				String nextGrade = jobData.get(i + 1).getKey();
				String nextName = jobData.get(i + 1).getValue();

				// Primary sort: Grade must be descending
				int gradeComparison = currentGrade.compareToIgnoreCase(nextGrade);
				if (gradeComparison < 0) {
					sortViolations++;
					LOGGER.error("❌ SORT VIOLATION: Grade at position " + (i + 1) + " (" + currentGrade
							+ ") < position " + (i + 2) + " (" + nextGrade + ") - NOT in Descending Order!");
					ExtentCucumberAdapter
							.addTestStepLog("❌ SORT VIOLATION: Grade position " + (i + 1) + " < position " + (i + 2));
				}
				// Secondary sort: If grades are equal, job names must be ascending
				else if (gradeComparison == 0 && currentName.compareToIgnoreCase(nextName) > 0) {
					sortViolations++;
					LOGGER.error("❌ SORT VIOLATION: Within Grade '" + currentGrade + "', Job Name at position "
							+ (i + 1) + " ('" + currentName + "') > position " + (i + 2) + " ('" + nextName
							+ "') - NOT in Ascending Order!");
					ExtentCucumberAdapter.addTestStepLog("❌ SORT VIOLATION: Job Name position " + (i + 1)
							+ " > position " + (i + 2) + " within same grade");
				}
			}

			if (sortViolations > 0) {
				String errorMsg = "❌ MULTI-LEVEL SORTING FAILED: Found " + sortViolations
						+ " violation(s). Data is NOT sorted by Grade (DESC) → Job Name (ASC)!";
				LOGGER.error(errorMsg);
				ExtentCucumberAdapter.addTestStepLog(errorMsg);
				Assert.fail(errorMsg + " Please check the sorting implementation!");
			} else if (jobData.size() > 1) {
				LOGGER.info("✅ MULTI-LEVEL SORT VALIDATION PASSED: " + jobData.size()
						+ " jobs correctly sorted by Grade (DESC) → Job Name (ASC)");
				ExtentCucumberAdapter.addTestStepLog(
						"✅ Multi-level sorting validation PASSED - Grade (DESC) → Job Name (ASC) correctly sorted");
			}

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"user_should_verify_first_thirty_job_profiles_sorted_by_organization_grade_in_descending_order_and_organization_job_name_in_ascending_order",
					"Issue in Verifying Job Profiles After sorting by Organization Grade in Descending Order and Organization Job Name in Ascending Order",
					e);
		}
	}

}
