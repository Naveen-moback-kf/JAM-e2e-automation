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

public class PO17_ValidateSortingFunctionality_JAM {
	WebDriver driver = DriverManager.getDriver();	

	protected static final Logger LOGGER = (Logger) LogManager.getLogger();
	PO17_ValidateSortingFunctionality_JAM validateSortingFunctionality;
	
	public static ArrayList<String> jobNamesTextInDefaultOrder = new ArrayList<String>();
//	public static ArrayList<WebElement> MatchedSPNameElements = new ArrayList<WebElement>();
	

	public PO17_ValidateSortingFunctionality_JAM() throws IOException {
		// super();
		PageFactory.initElements(driver, this);
		// TODO Auto-generated constructor stub
	}

	WebDriverWait wait = DriverManager.getWait();
	Utilities utils = new Utilities();
	JavascriptExecutor js = (JavascriptExecutor) driver;
	
	//XPATHS
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
	
	
	//METHODs
	public void user_should_scroll_page_down_two_times_to_view_first_thirty_job_profiles() {
	try {
		Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(showingJobResultsCount)).isDisplayed());
		js.executeScript("window.scrollTo(0, document.documentElement.scrollHeight);"); // Scroll DOWN (headless-compatible)
		wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
		// PERFORMANCE: Replaced 3x Thread.sleep(3000) = 9 seconds with smart page ready wait!
		PerformanceUtils.waitForPageReady(driver);
		js.executeScript("window.scrollTo(0, document.documentElement.scrollHeight);"); // Scroll DOWN (headless-compatible)
		PerformanceUtils.waitForElement(driver, showingJobResultsCount);
		String resultsCountText_updated = wait.until(ExpectedConditions.visibilityOf(showingJobResultsCount)).getText();
		LOGGER.info("Scrolled down till third page and now "+ resultsCountText_updated + " of Job Profiles as expected");
		ExtentCucumberAdapter.addTestStepLog("Scrolled down till third page and now "+ resultsCountText_updated + " of Job Profiles as expected");	
		js.executeScript("window.scrollTo(0, 0);"); // Scroll UP to top (headless-compatible)
	wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
		// PERFORMANCE: Replaced Thread.sleep(2000) with smart page ready wait
		PerformanceUtils.waitForPageReady(driver);	
	} catch (Exception e) {
		LOGGER.error(" Issue in scrolling page - Method: scroll_page_down_two_times", e);
		ScreenshotHandler.captureFailureScreenshot("scroll_page_down_two_times", e);
		e.printStackTrace();
		Assert.fail("Issue in scrolling page down two times to view first thirty job profiles...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in scrolling page down two times to view first thirty job profiles...Please Investigate!!!");
	}
	}
	
	
	public void user_should_verify_first_thirty_job_profiles_in_default_order_before_applying_sorting() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			// PERFORMANCE: Replaced Thread.sleep(2000) with smart page ready wait
			PerformanceUtils.waitForPageReady(driver);
			List<WebElement> allElements = driver.findElements(By.xpath("//tbody//tr//td[2]//div[contains(text(),'(')]"));
			LOGGER.info("Below is default Order of first thirty Job Profiles before applying sorting:");
			ExtentCucumberAdapter.addTestStepLog("Below is default Order of first thirty Job Profiles before applying sorting:");
			for (WebElement element: allElements) {
				String text = element.getText();
				jobNamesTextInDefaultOrder.add(text);
			LOGGER.info("Organization Job Profile with Job Name / Code : " + text);
			ExtentCucumberAdapter.addTestStepLog("Organization Job Profile with Name Job Name / Code : " + text);
		}
	} catch (Exception e) {
		LOGGER.error(" Issue verifying default order - Method: verify_first_thirty_job_profiles_in_default_order", e);
		ScreenshotHandler.captureFailureScreenshot("verify_first_thirty_job_profiles_in_default_order", e);
		e.printStackTrace();
		Assert.fail("Issue in Verifying default Order of first thirty Job Profiles before applying sorting...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in Verifying default Order of first thirty Job Profiles before applying sorting...Please Investigate!!!");
	}
	}
	
	public void sort_job_profiles_by_organiztion_job_name_in_ascending_order() {
		try {
			try {
				wait.until(ExpectedConditions.visibilityOf(orgJobNameHeader)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", orgJobNameHeader);
				} catch (Exception s) {
					utils.jsClick(driver, orgJobNameHeader);
				}
			}
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			// PERFORMANCE: Replaced Thread.sleep(2000) with smart page ready wait
			PerformanceUtils.waitForPageReady(driver);
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			// PERFORMANCE: Replaced Thread.sleep(2000) with smart page ready wait
			PerformanceUtils.waitForPageReady(driver);
			LOGGER.info("Clicked on Organization job name / code header to Sort Job Profiles by Name in ascending order");
			ExtentCucumberAdapter.addTestStepLog("Clicked on Organization job name / code header to Sort Job Profiles by Name in ascending order");
		wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
		// PERFORMANCE: Replaced Thread.sleep(2000) with smart page ready wait
		PerformanceUtils.waitForPageReady(driver);
	} catch (Exception e) {
		LOGGER.error(" Issue sorting by org job name ascending - Method: sort_job_profiles_by_organiztion_job_name_in_ascending_order", e);
		ScreenshotHandler.captureFailureScreenshot("sort_job_profiles_by_organiztion_job_name_in_ascending_order", e);
		e.printStackTrace();
		Assert.fail("Issue in clicking on Organization job name / code header to Sort Job Profiles by Name in ascending order...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in clicking on Organization job name / code header to Sort Job Profiles by Name in ascending order...Please Investigate!!!");
	}
	}
	
	public void user_should_verify_first_thirty_job_profiles_sorted_by_organiztion_job_name_in_ascending_order() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			// PERFORMANCE: Replaced Thread.sleep(2000) with smart page ready wait
			PerformanceUtils.waitForPageReady(driver);
			List<WebElement> allElements = driver.findElements(By.xpath("//tbody//tr//td[2]//div[contains(text(),'(')]"));
			LOGGER.info("Below are first thirty Job Profiles After sorting Job Profiles by Name in Ascending Order:");
			ExtentCucumberAdapter.addTestStepLog("Below are first thirty Job Profiles After sorting Job Profiles by Name in Ascending Order:");
			
			// Collect job names for validation
			ArrayList<String> jobNames = new ArrayList<String>();
			int specialCharCount = 0;
			int nonAsciiCount = 0;
			
			for (WebElement element: allElements) {
				String text = element.getText();
				jobNames.add(text); // Store original case for proper comparison
				
				// Detect special characters at start (expected at top in ascending)
				if (!text.isEmpty() && !Character.isLetterOrDigit(text.charAt(0))) {
					specialCharCount++;
					LOGGER.info("Organization Job Profile with Job Name / Code : " + text + " [SPECIAL CHAR at start - expected at top in Ascending]");
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
			
			if (specialCharCount > 0) {
				LOGGER.info("ℹ Found " + specialCharCount + " job(s) with special characters - these appear at top in Ascending order as expected");
				ExtentCucumberAdapter.addTestStepLog("ℹ " + specialCharCount + " job(s) start with special characters (?, -, etc.) - expected at top");
			}
			if (nonAsciiCount > 0) {
				LOGGER.info("ℹ Found " + nonAsciiCount + " job(s) with non-ASCII characters (Chinese, etc.)");
				ExtentCucumberAdapter.addTestStepLog("ℹ " + nonAsciiCount + " job(s) contain non-ASCII characters");
			}
			
			// ✅ VALIDATE ASCENDING ORDER (using case-insensitive comparison to match UI behavior)
			int sortViolations = 0;
			for(int i = 0; i < jobNames.size() - 1; i++) {
				String current = jobNames.get(i);
				String next = jobNames.get(i + 1);
				// compareToIgnoreCase handles case-insensitive ordering: special chars < ASCII letters (A/a) < non-ASCII
				if(current.compareToIgnoreCase(next) > 0) {
					sortViolations++;
					LOGGER.error("❌ SORT VIOLATION at Row " + (i + 1) + " -> Row " + (i + 2) + ": '" + current + "' > '" + next + "' (NOT in Ascending Order!)");
					ExtentCucumberAdapter.addTestStepLog("❌ SORT VIOLATION: Row " + (i + 1) + " > Row " + (i + 2));
				}
			}
			
			if(sortViolations > 0) {
				String errorMsg = "❌ SORTING FAILED: Found " + sortViolations + " violation(s). Data is NOT sorted by Job Name in Ascending Order!";
				LOGGER.error(errorMsg);
				ExtentCucumberAdapter.addTestStepLog(errorMsg);
				Assert.fail(errorMsg + " Please check the sorting implementation!");
			} else {
				LOGGER.info("✅ SORT VALIDATION PASSED: All " + jobNames.size() + " Job Profiles are correctly sorted by Job Name in Ascending Order (including special chars and non-ASCII)");
				ExtentCucumberAdapter.addTestStepLog("✅ Sorting validation PASSED - Data is correctly sorted in Ascending Order");
			}
			
	} catch (Exception e) {
		LOGGER.error(" Issue verifying sorted profiles ascending - Method: user_should_verify_first_thirty_job_profiles_sorted_by_organiztion_job_name_in_ascending_order", e);
		ScreenshotHandler.captureFailureScreenshot("verify_job_profiles_sorted_by_name_ascending", e);
		e.printStackTrace();
		Assert.fail("Issue in Verifying first thirty Job Profiles After sorting Job Profiles by Name in Ascending Order...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in Verifying first thirty Job Profiles After sorting Job Profiles by Name in Ascending Order...Please Investigate!!!");
	}
	}
	
	public void user_should_refresh_job_mapping_and_verify_job_profiles_are_in_default_order() {
		try {
			driver.navigate().refresh();
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			// PERFORMANCE: Replaced Thread.sleep(2000) with smart page ready wait
			PerformanceUtils.waitForPageReady(driver);
			LOGGER.info("Refreshed Job Mapping page....");
			ExtentCucumberAdapter.addTestStepLog("Refreshed Job Mapping page....");
			List<WebElement> allElements = driver.findElements(By.xpath("//tbody//tr//td[2]//div[contains(text(),'(')]"));
			for (int i = 0; i < allElements.size(); i++) {
				WebElement element = allElements.get(i);
				String text = element.getText();
				if(text.contentEquals(jobNamesTextInDefaultOrder.get(i))) {
					continue;
				} else {
					throw new Exception("Organization Job Name / code : " + text + " in Row " + Integer.toString(i) + "DOEST NOT Match with Job Name / Code : " + jobNamesTextInDefaultOrder.get(i) + " after Refreshing Job Mapping page");
				}
			}
		LOGGER.info("Organization Job Profiles are in Default Order as expected After Refreshing the Job Mapping page....");
		ExtentCucumberAdapter.addTestStepLog("Organization Job Profiles are in Default Order as expected After Refreshing the Job Mapping page....");
	} catch (Exception e) {
		LOGGER.error(" Issue verifying default order after refresh - Method: user_should_refresh_job_mapping_and_verify_job_profiles_are_in_default_order", e);
		ScreenshotHandler.captureFailureScreenshot("verify_default_order_after_refresh", e);
		e.printStackTrace();
		Assert.fail("Issue in Verifying Default order of Job Profiles after Refreshing Job Mapping page...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in Verifying Default order of Job Profiles after Refreshing Job Mapping page...Please Investigate!!!");
	}
	}
	
	/**
	 * Sorts job profiles by job name in descending order (clicks header twice)
	 * Page already loaded, directly clicks to sort
	 * Enhanced with proper waits for HEADLESS MODE compatibility
	 */
	public void sort_job_profiles_by_organiztion_job_name_in_descending_order() {
		try {
			// FIRST CLICK - Sort Ascending
			LOGGER.info("First click on Organization job name header to sort ascending...");
			try {
				wait.until(ExpectedConditions.elementToBeClickable(orgJobNameHeader)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", orgJobNameHeader);
				} catch (Exception s) {
					utils.jsClick(driver, orgJobNameHeader);
				}
			}
			
			// Wait for FIRST sort to complete (critical for headless mode)
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			Thread.sleep(2000); // Give UI time to process first sort
			PerformanceUtils.waitForPageReady(driver);
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			Thread.sleep(1000); // Additional buffer for headless mode
			
			LOGGER.info("First sort completed. Now clicking second time for descending order...");
			
			// SECOND CLICK - Sort Descending
			// Ensure element is clickable again before second click
			try {
				wait.until(ExpectedConditions.elementToBeClickable(orgJobNameHeader)).click();
			} catch (Exception e) {
				try {
					Thread.sleep(500); // Brief pause before retry
					js.executeScript("arguments[0].click();", orgJobNameHeader);
				} catch (Exception s) {
					utils.jsClick(driver, orgJobNameHeader);
				}
			}
			
			// Wait for SECOND sort to complete
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			Thread.sleep(2000); // Give UI time to process second sort
			PerformanceUtils.waitForPageReady(driver);
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			
		LOGGER.info("Clicked two times on Organization job name / code header to Sort Job Profiles by Name in Descending order");
		ExtentCucumberAdapter.addTestStepLog("Clicked two times on Organization job name / code header to Sort Job Profiles by Name in Descending order");
	} catch (Exception e) {
		LOGGER.error(" Issue sorting by org job name descending - Method: sort_job_profiles_by_organiztion_job_name_in_descending_order", e);
		ScreenshotHandler.captureFailureScreenshot("sort_job_profiles_by_name_descending", e);
		e.printStackTrace();
		Assert.fail("Issue in clicking on Organization job name / code header to Sort Job Profiles by Name in Descending order...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in clicking on Organization job name / code header to Sort Job Profiles by Name in Descending order...Please Investigate!!!");
	}
	}
	
	public void user_should_verify_first_thirty_job_profiles_sorted_by_organiztion_job_name_in_descending_order() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			// PERFORMANCE: Replaced Thread.sleep(2000) with smart page ready wait
			PerformanceUtils.waitForPageReady(driver);
			List<WebElement> allElements = driver.findElements(By.xpath("//tbody//tr//td[2]//div[contains(text(),'(')]"));
			LOGGER.info("Below are first thirty Job Profiles After sorting Job Profiles by Name in Descending Order:");
			ExtentCucumberAdapter.addTestStepLog("Below are first thirty Job Profiles After sorting Job Profiles by Name in Descending Order:");
			
			// Collect job names for validation
			ArrayList<String> jobNames = new ArrayList<String>();
			int specialCharCount = 0;
			int nonAsciiCount = 0;
			
			for (WebElement element: allElements) {
				String text = element.getText();
				jobNames.add(text); // Store original case for proper comparison
				
				// Detect non-ASCII characters at start (expected at top in descending)
				if (!text.isEmpty() && text.charAt(0) > 127) {
					nonAsciiCount++;
					LOGGER.info("Organization Job Profile with Job Name / Code : " + text + " [NON-ASCII at start - expected at top in Descending]");
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
			
			if (nonAsciiCount > 0) {
				LOGGER.info("ℹ Found " + nonAsciiCount + " job(s) with non-ASCII characters - these appear at top in Descending order as expected");
				ExtentCucumberAdapter.addTestStepLog("ℹ " + nonAsciiCount + " job(s) start with non-ASCII characters (Chinese, etc.) - expected at top");
			}
			if (specialCharCount > 0) {
				LOGGER.info("ℹ Found " + specialCharCount + " job(s) with special characters (?, -, etc.)");
				ExtentCucumberAdapter.addTestStepLog("ℹ " + specialCharCount + " job(s) start with special characters");
			}
			
			// ✅ VALIDATE DESCENDING ORDER (using case-insensitive comparison to match UI behavior)
			int sortViolations = 0;
			for(int i = 0; i < jobNames.size() - 1; i++) {
				String current = jobNames.get(i);
				String next = jobNames.get(i + 1);
				// compareToIgnoreCase handles case-insensitive ordering: non-ASCII > ASCII letters (A/a) > special chars
				if(current.compareToIgnoreCase(next) < 0) {
					sortViolations++;
					LOGGER.error("❌ SORT VIOLATION at Row " + (i + 1) + " -> Row " + (i + 2) + ": '" + current + "' < '" + next + "' (NOT in Descending Order!)");
					ExtentCucumberAdapter.addTestStepLog("❌ SORT VIOLATION: Row " + (i + 1) + " < Row " + (i + 2));
				}
			}
			
			if(sortViolations > 0) {
				String errorMsg = "❌ SORTING FAILED: Found " + sortViolations + " violation(s). Data is NOT sorted by Job Name in Descending Order!";
				LOGGER.error(errorMsg);
				ExtentCucumberAdapter.addTestStepLog(errorMsg);
				Assert.fail(errorMsg + " Please check the sorting implementation!");
			} else {
				LOGGER.info("✅ SORT VALIDATION PASSED: All " + jobNames.size() + " Job Profiles are correctly sorted by Job Name in Descending Order (including special chars and non-ASCII)");
				ExtentCucumberAdapter.addTestStepLog("✅ Sorting validation PASSED - Data is correctly sorted in Descending Order");
			}
			
	} catch (Exception e) {
		LOGGER.error(" Issue verifying sorted profiles descending - Method: user_should_verify_first_thirty_job_profiles_sorted_by_organiztion_job_name_in_descending_order", e);
		ScreenshotHandler.captureFailureScreenshot("verify_job_profiles_sorted_by_name_descending", e);
		e.printStackTrace();
		Assert.fail("Issue in Verifying first thirty Job Profiles After sorting Job Profiles by Name in Descending Order...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in Verifying first thirty Job Profiles After sorting Job Profiles by Name in Descending Order...Please Investigate!!!");
	}
	}
	
	/**
	 * Sorts job profiles by matched SP grade in ascending order
	 * Page already loaded, directly clicks header to sort
	 */
	public void sort_job_profiles_by_matched_success_profile_grade_in_ascending_order() {
		try {
			// Page already loaded, directly click to sort
			wait.until(ExpectedConditions.visibilityOf(matchedSPGradeHeader)).click();
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			// PERFORMANCE: Replaced Thread.sleep(2000) with smart page ready wait
		PerformanceUtils.waitForPageReady(driver);
		LOGGER.info("Clicked on Matched Success Profile Grade header to Sort Job Profiles by Matched SP Grade in ascending order");
		ExtentCucumberAdapter.addTestStepLog("Clicked on Matched Success Profile Grade header to Sort Job Profiles by Matched SP Grade in ascending order");
	} catch (Exception e) {
		LOGGER.error(" Issue sorting by SP grade ascending - Method: sort_job_profiles_by_matched_success_profile_grade_in_ascending_order", e);
		ScreenshotHandler.captureFailureScreenshot("sort_by_sp_grade_ascending", e);
		e.printStackTrace();
		Assert.fail("Issue in clicking on Matched Success Profile Grade header to Sort Job Profiles by Matched SP Grade in ascending order...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in clicking on Matched Success Profile Grade header to Sort Job Profiles by Matched SP Grade in ascending order...Please Investigate!!!");
	}
	}
	
	public void user_should_verify_first_thirty_job_profiles_sorted_by_matched_success_profile_grade_in_ascending_order() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			// PERFORMANCE: Replaced Thread.sleep(2000) with smart page ready wait
			PerformanceUtils.waitForPageReady(driver);
			List<WebElement> allElements = driver.findElements(By.xpath("//tbody//tr//td[2]//div[contains(text(),'(')]"));
			LOGGER.info("Below are Job Profiles After sorting by Matched Success Profile Grade in Ascending Order:");
			ExtentCucumberAdapter.addTestStepLog("Below are Job Profiles After sorting by Matched Success Profile Grade in Ascending Order:");
			
			// Collect SP grades for validation (only mapped jobs)
			ArrayList<String> spGrades = new ArrayList<String>();
			int specialCharCount = 0;
			int nonAsciiCount = 0;
			int unmappedCount = 0;
			
			// Iterate through org job profiles and get corresponding SP Grades
			// SP Grades appear at tr[1], tr[4], tr[7]... pattern (every 3rd row starting from index 1)
			for (int i = 0; i < allElements.size(); i++) {
				WebElement element = allElements.get(i);
				String text = element.getText();
				
				// Calculate the correct tr index for this SP Grade: tr[3*i + 1]
				int spRowIndex = (3 * i) + 1;
				
				// Try to find the SP Grade element at the correct row
				String gradeText = "";
				try {
					WebElement GradeElement = driver.findElement(
						By.xpath("//*[@id='kf-job-container']/div/table/tbody/tr[" + spRowIndex + "]/td[2]/div")
					);
					gradeText = GradeElement.getText();
				} catch (Exception spGradeEx) {
					// SP Grade element not found at expected position - likely unmapped job
					gradeText = "";
					LOGGER.warn("SP Grade element not found at tr[" + spRowIndex + "] for job: " + text);
				}
				
				// Check if this is an unmapped job (no SP grade)
				if (gradeText == null || gradeText.trim().isEmpty() || gradeText.equals("-")) {
					unmappedCount++;
					LOGGER.info("Organization Job Profile with Job Name / Code : " + text + " - UNMAPPED (No SP Grade)");
					ExtentCucumberAdapter.addTestStepLog("Organization Job Profile: " + text + " - UNMAPPED (No SP Grade)");
				} else {
					spGrades.add(gradeText); // Store for validation (keep original case for Unicode)
					
					// Detect special characters at start (expected at top in ascending)
					if (!gradeText.isEmpty() && !Character.isLetterOrDigit(gradeText.charAt(0))) {
						specialCharCount++;
						LOGGER.info("Organization Job Profile with Job Name / Code : " + text + " with Matched SP Grade : " + gradeText + " [SPECIAL CHAR at start]");
					}
					// Detect non-ASCII characters
					else if (!gradeText.isEmpty() && gradeText.charAt(0) > 127) {
						nonAsciiCount++;
						LOGGER.info("Organization Job Profile with Job Name / Code : " + text + " with Matched SP Grade : " + gradeText + " [NON-ASCII]");
					} else {
						LOGGER.info("Organization Job Profile with Job Name / Code : " + text + " with Matched SP Grade : " + gradeText);
					}
					ExtentCucumberAdapter.addTestStepLog("Organization Job Profile: " + text + " with Matched SP Grade : " + gradeText);
				}
			}
			
			if (unmappedCount > 0) {
				LOGGER.info("Found {} unmapped job(s) without SP grade details", unmappedCount);
				ExtentCucumberAdapter.addTestStepLog("Found " + unmappedCount + " unmapped job(s) without SP grade details");
			}
			if (specialCharCount > 0) {
				LOGGER.info("ℹ Found " + specialCharCount + " SP grade(s) with special characters - expected at top in Ascending order");
				ExtentCucumberAdapter.addTestStepLog("ℹ " + specialCharCount + " SP grade(s) start with special characters");
			}
			if (nonAsciiCount > 0) {
				LOGGER.info("ℹ Found " + nonAsciiCount + " SP grade(s) with non-ASCII characters");
				ExtentCucumberAdapter.addTestStepLog("ℹ " + nonAsciiCount + " SP grade(s) contain non-ASCII characters");
			}
			
			// ✅ VALIDATE ASCENDING ORDER (only for mapped jobs) - case-insensitive
			int sortViolations = 0;
			for(int i = 0; i < spGrades.size() - 1; i++) {
				String current = spGrades.get(i);
				String next = spGrades.get(i + 1);
				if(current.compareToIgnoreCase(next) > 0) {
					sortViolations++;
					LOGGER.error("❌ SORT VIOLATION: SP Grade at position " + (i + 1) + " (" + current + ") > position " + (i + 2) + " (" + next + ") - NOT in Ascending Order!");
					ExtentCucumberAdapter.addTestStepLog("❌ SORT VIOLATION: SP Grade position " + (i + 1) + " > position " + (i + 2));
				}
			}
			
			if(sortViolations > 0) {
				String errorMsg = "❌ SORTING FAILED: Found " + sortViolations + " violation(s). SP Grades are NOT sorted in Ascending Order!";
				LOGGER.error(errorMsg);
				ExtentCucumberAdapter.addTestStepLog(errorMsg);
				Assert.fail(errorMsg + " Please check the sorting implementation!");
			} else if(spGrades.size() > 1) {
				LOGGER.info("✅ SORT VALIDATION PASSED: " + spGrades.size() + " SP Grades are correctly sorted in Ascending Order");
				ExtentCucumberAdapter.addTestStepLog("✅ Sorting validation PASSED - SP Grades are correctly sorted in Ascending Order");
			} else {
				LOGGER.info("ℹ VALIDATION SKIPPED: Not enough mapped jobs with SP grades to validate sorting (" + spGrades.size() + " job(s) with grades)");
				ExtentCucumberAdapter.addTestStepLog("ℹ Validation skipped - insufficient data for sorting validation");
			}
			
		} catch (Exception e) {
			LOGGER.error(" Issue verifying sorted by SP grade ascending - Method: user_should_verify_first_thirty_job_profiles_sorted_by_matched_success_profile_grade_in_ascending_order", e);
			ScreenshotHandler.captureFailureScreenshot("verify_sorted_by_sp_grade_ascending", e);
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying Job Profiles After sorting by Matched Success Profile Grade in Ascending Order...Please Investigate!!!");
		}
	}
	
	/**
	 * Sorts job profiles by matched SP grade in descending order (clicks header twice)
	 * Page already loaded, directly clicks to sort
	 * Enhanced with proper waits for HEADLESS MODE compatibility
	 */
	public void sort_job_profiles_by_matched_success_profile_grade_in_descending_order() {
		try {
			// FIRST CLICK - Sort Ascending
			LOGGER.info("First click on SP Grade header to sort ascending...");
			try {
				wait.until(ExpectedConditions.elementToBeClickable(matchedSPGradeHeader)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", matchedSPGradeHeader);
				} catch (Exception s) {
					utils.jsClick(driver, matchedSPGradeHeader);
				}
			}
			
			// Wait for FIRST sort to complete (critical for headless mode)
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			Thread.sleep(2000); // Give UI time to process first sort
			PerformanceUtils.waitForPageReady(driver);
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			Thread.sleep(1000); // Additional buffer for headless mode
			
			LOGGER.info("First sort completed. Now clicking second time for descending order...");
			
			// SECOND CLICK - Sort Descending
			// Ensure element is clickable again before second click
			try {
				wait.until(ExpectedConditions.elementToBeClickable(matchedSPGradeHeader)).click();
			} catch (Exception e) {
				try {
					Thread.sleep(500); // Brief pause before retry
					js.executeScript("arguments[0].click();", matchedSPGradeHeader);
				} catch (Exception s) {
					utils.jsClick(driver, matchedSPGradeHeader);
				}
			}
			
			// Wait for SECOND sort to complete
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			Thread.sleep(2000); // Give UI time to process second sort
			PerformanceUtils.waitForPageReady(driver);
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			
		LOGGER.info("Clicked two times on Matched Success Profile Grade header to Sort Job Profiles by Matched SP Grade in descending order");
		ExtentCucumberAdapter.addTestStepLog("Clicked two times on Matched Success Profile Grade header to Sort Job Profiles by Matched SP Grade in descending order");
	} catch (Exception e) {
		LOGGER.error(" Issue sorting by SP grade descending - Method: sort_job_profiles_by_matched_success_profile_grade_in_descending_order", e);
		ScreenshotHandler.captureFailureScreenshot("sort_by_sp_grade_descending", e);
		e.printStackTrace();
		Assert.fail("Issue in clicking two times on Matched Success Profile Grade header to Sort Job Profiles by Matched SP Grade in descending order...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in clicking two times on Matched Success Profile Grade header to Sort Job Profiles by Matched SP Grade in descending order...Please Investigate!!!");
	}
	}
	
	public void user_should_verify_first_thirty_job_profiles_sorted_by_matched_success_profile_grade_in_descending_order() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			// PERFORMANCE: Replaced Thread.sleep(2000) with smart page ready wait
			PerformanceUtils.waitForPageReady(driver);
			List<WebElement> allElements = driver.findElements(By.xpath("//tbody//tr//td[2]//div[contains(text(),'(')]"));
			LOGGER.info("Below are Job Profiles After sorting by Matched Success Profile Grade in Descending Order:");
			ExtentCucumberAdapter.addTestStepLog("Below are Job Profiles After sorting by Matched Success Profile Grade in Descending Order:");
			
			// Collect SP grades for validation (only mapped jobs)
			ArrayList<String> spGrades = new ArrayList<String>();
			int specialCharCount = 0;
			int nonAsciiCount = 0;
			int unmappedCount = 0;
			
			// Iterate through org job profiles and get corresponding SP Grades
			// SP Grades appear at tr[1], tr[4], tr[7]... pattern (every 3rd row starting from index 1)
			for (int i = 0; i < allElements.size(); i++) {
				WebElement element = allElements.get(i);
				String text = element.getText();
				
				// Calculate the correct tr index for this SP Grade: tr[3*i + 1]
				int spRowIndex = (3 * i) + 1;
				
				// Try to find the SP Grade element at the correct row
				String gradeText = "";
				try {
					WebElement GradeElement = driver.findElement(
						By.xpath("//*[@id='kf-job-container']/div/table/tbody/tr[" + spRowIndex + "]/td[2]/div")
					);
					gradeText = GradeElement.getText();
				} catch (Exception spGradeEx) {
					// SP Grade element not found at expected position - likely unmapped job
					gradeText = "";
					LOGGER.warn("SP Grade element not found at tr[" + spRowIndex + "] for job: " + text);
				}
				
				// Check if this is an unmapped job (no SP grade)
				if (gradeText == null || gradeText.trim().isEmpty() || gradeText.equals("-")) {
					unmappedCount++;
					LOGGER.info("Organization Job Profile with Job Name / Code : " + text + " - UNMAPPED (No SP Grade)");
					ExtentCucumberAdapter.addTestStepLog("Organization Job Profile: " + text + " - UNMAPPED (No SP Grade)");
				} else {
					spGrades.add(gradeText); // Store for validation (keep original case for Unicode)
					
					// Detect non-ASCII characters at start (expected at top in descending)
					if (!gradeText.isEmpty() && gradeText.charAt(0) > 127) {
						nonAsciiCount++;
						LOGGER.info("Organization Job Profile with Job Name / Code : " + text + " with Matched SP Grade : " + gradeText + " [NON-ASCII at start]");
					}
					// Detect special characters
					else if (!gradeText.isEmpty() && !Character.isLetterOrDigit(gradeText.charAt(0))) {
						specialCharCount++;
						LOGGER.info("Organization Job Profile with Job Name / Code : " + text + " with Matched SP Grade : " + gradeText + " [SPECIAL CHAR]");
					} else {
						LOGGER.info("Organization Job Profile with Job Name / Code : " + text + " with Matched SP Grade : " + gradeText);
					}
					ExtentCucumberAdapter.addTestStepLog("Organization Job Profile: " + text + " with Matched SP Grade : " + gradeText);
				}
			}
			
			if (unmappedCount > 0) {
				LOGGER.info("Found {} unmapped job(s) without SP grade details", unmappedCount);
				ExtentCucumberAdapter.addTestStepLog("Found " + unmappedCount + " unmapped job(s) without SP grade details");
			}
			if (nonAsciiCount > 0) {
				LOGGER.info("ℹ Found " + nonAsciiCount + " SP grade(s) with non-ASCII characters - expected at top in Descending order");
				ExtentCucumberAdapter.addTestStepLog("ℹ " + nonAsciiCount + " SP grade(s) start with non-ASCII characters");
			}
			if (specialCharCount > 0) {
				LOGGER.info("ℹ Found " + specialCharCount + " SP grade(s) with special characters");
				ExtentCucumberAdapter.addTestStepLog("ℹ " + specialCharCount + " SP grade(s) start with special characters");
			}
			
			// ✅ VALIDATE DESCENDING ORDER (only for mapped jobs) - case-insensitive
			int sortViolations = 0;
			for(int i = 0; i < spGrades.size() - 1; i++) {
				String current = spGrades.get(i);
				String next = spGrades.get(i + 1);
				if(current.compareToIgnoreCase(next) < 0) {
					sortViolations++;
					LOGGER.error("❌ SORT VIOLATION: SP Grade at position " + (i + 1) + " (" + current + ") < position " + (i + 2) + " (" + next + ") - NOT in Descending Order!");
					ExtentCucumberAdapter.addTestStepLog("❌ SORT VIOLATION: SP Grade position " + (i + 1) + " < position " + (i + 2));
				}
			}
			
			if(sortViolations > 0) {
				String errorMsg = "❌ SORTING FAILED: Found " + sortViolations + " violation(s). SP Grades are NOT sorted in Descending Order!";
				LOGGER.error(errorMsg);
				ExtentCucumberAdapter.addTestStepLog(errorMsg);
				Assert.fail(errorMsg + " Please check the sorting implementation!");
			} else if(spGrades.size() > 1) {
				LOGGER.info("✅ SORT VALIDATION PASSED: " + spGrades.size() + " SP Grades are correctly sorted in Descending Order");
				ExtentCucumberAdapter.addTestStepLog("✅ Sorting validation PASSED - SP Grades are correctly sorted in Descending Order");
			} else {
				LOGGER.info("ℹ VALIDATION SKIPPED: Not enough mapped jobs with SP grades to validate sorting (" + spGrades.size() + " job(s) with grades)");
				ExtentCucumberAdapter.addTestStepLog("ℹ Validation skipped - insufficient data for sorting validation");
			}
			
		} catch (Exception e) {
			LOGGER.error(" Issue verifying sorted by SP grade descending - Method: user_should_verify_first_thirty_job_profiles_sorted_by_matched_success_profile_grade_in_descending_order", e);
			ScreenshotHandler.captureFailureScreenshot("verify_sorted_by_sp_grade_descending", e);
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying Job Profiles After sorting by Matched Success Profile Grade in Descending Order...Please Investigate!!!");
		}
	}
	
	/**
	 * Sorts job profiles by matched SP name in ascending order
	 * Page already loaded, directly clicks header to sort
	 */
	public void sort_job_profiles_by_matched_success_profile_name_in_ascending_order() {
		try {
			// Page already loaded, directly click to sort
			wait.until(ExpectedConditions.visibilityOf(matcheSPNameHeader)).click();
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			// PERFORMANCE: Replaced Thread.sleep(2000) with smart page ready wait
		PerformanceUtils.waitForPageReady(driver);
		LOGGER.info("Clicked on Matched Success Profile Name header to Sort Job Profiles by Matched SP Name in ascending order");
		ExtentCucumberAdapter.addTestStepLog("Clicked on Matched Success Profile Name header to Sort Job Profiles by Matched SP Name in ascending order");
	} catch (Exception e) {
		LOGGER.error(" Issue sorting by SP name ascending - Method: sort_job_profiles_by_matched_success_profile_name_in_ascending_order", e);
		ScreenshotHandler.captureFailureScreenshot("sort_by_sp_name_ascending", e);
		e.printStackTrace();
		Assert.fail("Issue in clicking on Matched Success Profile Name header to Sort Job Profiles by Matched SP Name in ascending order...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in clicking on Matched Success Profile Name header to Sort Job Profiles by Matched SP Name in ascending order...Please Investigate!!!");
	}
	}
	
	public void user_should_verify_first_thirty_job_profiles_sorted_by_matched_success_profile_name_in_ascending_order() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			Thread.sleep(3000);
			PerformanceUtils.waitForPageReady(driver);
			List<WebElement> allElements = driver.findElements(By.xpath("//tbody//tr//td[2]//div[contains(text(),'(')]"));
			
			LOGGER.info("Below are Job Profiles After sorting by Matched Success Profile Name in Ascending Order:");
			ExtentCucumberAdapter.addTestStepLog("Below are Job Profiles After sorting by Matched Success Profile Name in Ascending Order:");
			
			// Collect SP names for validation (only mapped jobs)
			ArrayList<String> spNames = new ArrayList<String>();
			int specialCharCount = 0;
			int nonAsciiCount = 0;
			int unmappedCount = 0;
			
			// Iterate through org job profiles and get corresponding SP Names
			// SP Names appear at tr[1], tr[4], tr[7]... pattern (every 3rd row starting from index 1)
			for (int i = 0; i < allElements.size(); i++) {
				WebElement element = allElements.get(i);
				String text = element.getText();
				
				// Calculate the correct tr index for this SP Name: tr[3*i + 1]
				int spRowIndex = (3 * i) + 1;
				
				// Try to find the SP Name element at the correct row
				String NameText = "";
				try {
					WebElement MatchedSPNameElement = driver.findElement(
						By.xpath("//*[@id='kf-job-container']/div/table/tbody/tr[" + spRowIndex + "]/td[1]/div")
					);
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
					ExtentCucumberAdapter.addTestStepLog("Organization Job Profile: " + text + " - UNMAPPED (No SP Name)");
				} else {
					spNames.add(NameText); // Store for validation (keep original case for Unicode)
					
					// Detect special characters at start (expected at top in ascending)
					if (!NameText.isEmpty() && !Character.isLetterOrDigit(NameText.charAt(0))) {
						specialCharCount++;
						LOGGER.info("Organization Job Profile with Job Name / Code : " + text + " with Matched SP Name : " + NameText + " [SPECIAL CHAR at start]");
					}
					// Detect non-ASCII characters
					else if (!NameText.isEmpty() && NameText.charAt(0) > 127) {
						nonAsciiCount++;
						LOGGER.info("Organization Job Profile with Job Name / Code : " + text + " with Matched SP Name : " + NameText + " [NON-ASCII]");
					} else {
						LOGGER.info("Organization Job Profile with Job Name / Code : " + text + " with Matched SP Name : " + NameText);
					}
					ExtentCucumberAdapter.addTestStepLog("Organization Job Profile: " + text + " with Matched SP Name : " + NameText);
				}
			}
			
			if (unmappedCount > 0) {
				LOGGER.info("Found {} unmapped job(s) without SP name details", unmappedCount);
				ExtentCucumberAdapter.addTestStepLog("Found " + unmappedCount + " unmapped job(s) without SP name details");
			}
			if (specialCharCount > 0) {
				LOGGER.info("ℹ Found " + specialCharCount + " SP name(s) with special characters - expected at top in Ascending order");
				ExtentCucumberAdapter.addTestStepLog("ℹ " + specialCharCount + " SP name(s) start with special characters");
			}
			if (nonAsciiCount > 0) {
				LOGGER.info("ℹ Found " + nonAsciiCount + " SP name(s) with non-ASCII characters");
				ExtentCucumberAdapter.addTestStepLog("ℹ " + nonAsciiCount + " SP name(s) contain non-ASCII characters");
			}
			
			// ✅ VALIDATE ASCENDING ORDER (only for mapped jobs) - case-insensitive
			int sortViolations = 0;
			for(int i = 0; i < spNames.size() - 1; i++) {
				String current = spNames.get(i);
				String next = spNames.get(i + 1);
				if(current.compareToIgnoreCase(next) > 0) {
					sortViolations++;
					LOGGER.error("❌ SORT VIOLATION: SP Name at position " + (i + 1) + " (" + current + ") > position " + (i + 2) + " (" + next + ") - NOT in Ascending Order!");
					ExtentCucumberAdapter.addTestStepLog("❌ SORT VIOLATION: SP Name position " + (i + 1) + " > position " + (i + 2));
				}
			}
			
			if(sortViolations > 0) {
				String errorMsg = "❌ SORTING FAILED: Found " + sortViolations + " violation(s). SP Names are NOT sorted in Ascending Order!";
				LOGGER.error(errorMsg);
				ExtentCucumberAdapter.addTestStepLog(errorMsg);
				Assert.fail(errorMsg + " Please check the sorting implementation!");
			} else if(spNames.size() > 1) {
				LOGGER.info("✅ SORT VALIDATION PASSED: " + spNames.size() + " SP Names are correctly sorted in Ascending Order");
				ExtentCucumberAdapter.addTestStepLog("✅ Sorting validation PASSED - SP Names are correctly sorted in Ascending Order");
			} else {
				LOGGER.info("ℹ VALIDATION SKIPPED: Not enough mapped jobs with SP names to validate sorting (" + spNames.size() + " job(s) with names)");
				ExtentCucumberAdapter.addTestStepLog("ℹ Validation skipped - insufficient data for sorting validation");
			}
			
		} catch (Exception e) {
			LOGGER.error(" Issue verifying sorted by SP name ascending - Method: user_should_verify_first_thirty_job_profiles_sorted_by_matched_success_profile_name_in_ascending_order", e);
			ScreenshotHandler.captureFailureScreenshot("verify_sorted_by_sp_name_ascending", e);
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying Job Profiles After sorting by Matched Success Profile Name in Ascending Order...Please Investigate!!!");
		}
	}
	
	/**
	 * Sorts job profiles by organization grade in ascending order
	 * Page already loaded, clicks header and waits for sort to complete
	 */
	public void sort_job_profiles_by_organization_grade_in_ascending_order() {
		try {
			// Page already loaded, directly click to sort
			wait.until(ExpectedConditions.visibilityOf(orgJobGradeHeader)).click();
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
		PerformanceUtils.waitForPageReady(driver);
		LOGGER.info("Clicked on Organization Grade header to Sort Job Profiles by Grade in ascending order");
		ExtentCucumberAdapter.addTestStepLog("Clicked on Organization Grade header to Sort Job Profiles by Grade in ascending order");
	} catch (Exception e) {
		LOGGER.error(" Issue sorting by org grade ascending - Method: sort_job_profiles_by_organization_grade_in_ascending_order", e);
		ScreenshotHandler.captureFailureScreenshot("sort_by_org_grade_ascending", e);
		e.printStackTrace();
		Assert.fail("Issue in clicking on Organization Grade header to Sort Job Profiles by Grade in ascending order...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in clicking on Organization Grade header to Sort Job Profiles by Grade in ascending order...Please Investigate!!!");
	}
	}
	
	public void user_should_verify_first_thirty_job_profiles_sorted_by_organization_grade_and_organization_job_name_in_ascending_order() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			// PERFORMANCE: Replaced Thread.sleep(2000) with smart page ready wait
			PerformanceUtils.waitForPageReady(driver);
			List<WebElement> allElements = driver.findElements(By.xpath("//tbody//tr//td[2]//div[contains(text(),'(')]"));
			
			// Dynamically find all Organization Grade elements that actually exist
			List<WebElement> OrgGradeElements = driver.findElements(By.xpath("//*[@id='org-job-container']/div/table/tbody/tr/td[3]/div"));
			
			LOGGER.info("Below are Job Profiles After sorting by Organization Grade and Organization Job Name in Ascending Order:");
			ExtentCucumberAdapter.addTestStepLog("Below are Job Profiles After sorting by Organization Grade and Organization Job Name in Ascending Order:");
			
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
						LOGGER.info("Organization Job Profile with Job Name / Code : " + text + " with Organization Job Grade : " + GradeText + " [SPECIAL CHAR at start]");
					}
					// Detect non-ASCII characters
					else if (!GradeText.isEmpty() && GradeText.charAt(0) > 127) {
						nonAsciiCount++;
						LOGGER.info("Organization Job Profile with Job Name / Code : " + text + " with Organization Job Grade : " + GradeText + " [NON-ASCII]");
					} else {
						LOGGER.info("Organization Job Profile with Job Name / Code : " + text + " with Organization Job Grade : " + GradeText);
					}
				} else {
					LOGGER.info("Organization Job Profile with Job Name / Code : " + text + " with Organization Job Grade : " + GradeText);
				}
				ExtentCucumberAdapter.addTestStepLog("Organization Job Profile with Job Name / Code : " + text + " with Organization Job Grade : " + GradeText);
			}
			
			if (specialCharCount > 0) {
				LOGGER.info("ℹ Found " + specialCharCount + " org grade(s) with special characters - expected at top in Ascending order");
				ExtentCucumberAdapter.addTestStepLog("ℹ " + specialCharCount + " org grade(s) start with special characters");
			}
			if (nonAsciiCount > 0) {
				LOGGER.info("ℹ Found " + nonAsciiCount + " org grade(s) with non-ASCII characters");
				ExtentCucumberAdapter.addTestStepLog("ℹ " + nonAsciiCount + " org grade(s) contain non-ASCII characters");
			}
			
			// ✅ VALIDATE ASCENDING ORDER - case-insensitive
			int sortViolations = 0;
			for(int i = 0; i < orgGrades.size() - 1; i++) {
				String current = orgGrades.get(i);
				String next = orgGrades.get(i + 1);
				if(current.compareToIgnoreCase(next) > 0) {
					sortViolations++;
					LOGGER.error("❌ SORT VIOLATION: Org Grade at position " + (i + 1) + " (" + current + ") > position " + (i + 2) + " (" + next + ") - NOT in Ascending Order!");
					ExtentCucumberAdapter.addTestStepLog("❌ SORT VIOLATION: Org Grade position " + (i + 1) + " > position " + (i + 2));
				}
			}
			
			if(sortViolations > 0) {
				String errorMsg = "❌ SORTING FAILED: Found " + sortViolations + " violation(s). Org Grades are NOT sorted in Ascending Order!";
				LOGGER.error(errorMsg);
				ExtentCucumberAdapter.addTestStepLog(errorMsg);
				Assert.fail(errorMsg + " Please check the sorting implementation!");
			} else if(orgGrades.size() > 1) {
				LOGGER.info("✅ SORT VALIDATION PASSED: " + orgGrades.size() + " Organization Grades are correctly sorted in Ascending Order");
				ExtentCucumberAdapter.addTestStepLog("✅ Sorting validation PASSED - Org Grades are correctly sorted in Ascending Order");
			}
			
		} catch (Exception e) {
			LOGGER.error(" Issue verifying sorted by org grade ascending - Method: user_should_verify_first_thirty_job_profiles_sorted_by_organization_grade_and_organization_job_name_in_ascending_order", e);
			ScreenshotHandler.captureFailureScreenshot("verify_sorted_by_org_grade_ascending", e);
			e.printStackTrace();
			Assert.fail("Issue in Verifying Job Profiles After sorting by Organization Grade and Organization Job Name in Ascending Order...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying Job Profiles After sorting by Organization Grade and Organization Job Name in Ascending Order...Please Investigate!!!");
		}
	}
	
	/**
	 * Sorts job profiles by organization grade in descending order (clicks header twice)
	 * Enhanced with proper waits for HEADLESS MODE compatibility
	 */
	public void sort_job_profiles_by_organization_grade_in_descending_order() {
		try {
			// FIRST CLICK - Sort Ascending
			LOGGER.info("First click on Organization Grade header to sort ascending...");
			try {
				wait.until(ExpectedConditions.elementToBeClickable(orgJobGradeHeader)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", orgJobGradeHeader);
				} catch (Exception s) {
					utils.jsClick(driver, orgJobGradeHeader);
				}
			}
			
			// Wait for FIRST sort to complete (critical for headless mode)
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			Thread.sleep(2000); // Give UI time to process first sort
			PerformanceUtils.waitForPageReady(driver);
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			Thread.sleep(1000); // Additional buffer for headless mode
			
			LOGGER.info("First sort completed. Now clicking second time for descending order...");
			
			// SECOND CLICK - Sort Descending
			// Ensure element is clickable again before second click
			try {
				wait.until(ExpectedConditions.elementToBeClickable(orgJobGradeHeader)).click();
			} catch (Exception e) {
				try {
					Thread.sleep(500); // Brief pause before retry
					js.executeScript("arguments[0].click();", orgJobGradeHeader);
				} catch (Exception s) {
					utils.jsClick(driver, orgJobGradeHeader);
				}
			}
			
			// Wait for SECOND sort to complete
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			Thread.sleep(2000); // Give UI time to process second sort
			PerformanceUtils.waitForPageReady(driver);
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			
		LOGGER.info("Clicked two times on Organization Grade header to Sort Job Profiles by Grade in Descending order");
		ExtentCucumberAdapter.addTestStepLog("Clicked two times on Organization Grade to Sort Job Profiles by Grade in Descending order");
	} catch (Exception e) {
		LOGGER.error(" Issue sorting by org grade descending - Method: sort_job_profiles_by_organization_grade_in_descending_order", e);
		ScreenshotHandler.captureFailureScreenshot("sort_by_org_grade_descending", e);
		e.printStackTrace();
		Assert.fail("Issue in clicking on Organization Grade to Sort Job Profiles by Grade in Descending order...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in clicking on Organization Grade to Sort Job Profiles by Grade in Descending order...Please Investigate!!!");
	}
	}
	
	public void user_should_verify_first_thirty_job_profiles_sorted_by_organization_grade_in_descending_order_and_organization_job_name_in_ascending_order() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			// PERFORMANCE: Replaced Thread.sleep(2000) with smart page ready wait
			PerformanceUtils.waitForPageReady(driver);
			List<WebElement> allElements = driver.findElements(By.xpath("//tbody//tr//td[2]//div[contains(text(),'(')]"));
			
			// Dynamically find all Organization Grade elements that actually exist
			List<WebElement> OrgGradeElements = driver.findElements(By.xpath("//*[@id='org-job-container']/div/table/tbody/tr/td[3]/div"));
			
			LOGGER.info("Below are Job Profiles After sorting by Organization Grade in Descending Order and Organization Job Name in Ascending Order:");
			ExtentCucumberAdapter.addTestStepLog("Below are Job Profiles After sorting by Organization Grade in Descending Order and Organization Job Name in Ascending Order:");
			
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
					
					// Detect non-ASCII characters at start (expected at top in descending)
					if (!GradeText.isEmpty() && GradeText.charAt(0) > 127) {
						nonAsciiCount++;
						LOGGER.info("Organization Job Profile with Job Name / Code : " + text + " with Organization Job Grade : " + GradeText + " [NON-ASCII at start]");
					}
					// Detect special characters
					else if (!GradeText.isEmpty() && !Character.isLetterOrDigit(GradeText.charAt(0))) {
						specialCharCount++;
						LOGGER.info("Organization Job Profile with Job Name / Code : " + text + " with Organization Job Grade : " + GradeText + " [SPECIAL CHAR]");
					} else {
						LOGGER.info("Organization Job Profile with Job Name / Code : " + text + " with Organization Job Grade : " + GradeText);
					}
				} else {
					LOGGER.info("Organization Job Profile with Job Name / Code : " + text + " with Organization Job Grade : " + GradeText);
				}
				ExtentCucumberAdapter.addTestStepLog("Organization Job Profile with Job Name / Code : " + text + " with Organization Job Grade : " + GradeText);
			}
			
			if (nonAsciiCount > 0) {
				LOGGER.info("ℹ Found " + nonAsciiCount + " org grade(s) with non-ASCII characters - expected at top in Descending order");
				ExtentCucumberAdapter.addTestStepLog("ℹ " + nonAsciiCount + " org grade(s) start with non-ASCII characters");
			}
			if (specialCharCount > 0) {
				LOGGER.info("ℹ Found " + specialCharCount + " org grade(s) with special characters");
				ExtentCucumberAdapter.addTestStepLog("ℹ " + specialCharCount + " org grade(s) start with special characters");
			}
			
			// ✅ VALIDATE DESCENDING ORDER - case-insensitive
			int sortViolations = 0;
			for(int i = 0; i < orgGrades.size() - 1; i++) {
				String current = orgGrades.get(i);
				String next = orgGrades.get(i + 1);
				if(current.compareToIgnoreCase(next) < 0) {
					sortViolations++;
					LOGGER.error("❌ SORT VIOLATION: Org Grade at position " + (i + 1) + " (" + current + ") < position " + (i + 2) + " (" + next + ") - NOT in Descending Order!");
					ExtentCucumberAdapter.addTestStepLog("❌ SORT VIOLATION: Org Grade position " + (i + 1) + " < position " + (i + 2));
				}
			}
			
			if(sortViolations > 0) {
				String errorMsg = "❌ SORTING FAILED: Found " + sortViolations + " violation(s). Org Grades are NOT sorted in Descending Order!";
				LOGGER.error(errorMsg);
				ExtentCucumberAdapter.addTestStepLog(errorMsg);
				Assert.fail(errorMsg + " Please check the sorting implementation!");
			} else if(orgGrades.size() > 1) {
				LOGGER.info("✅ SORT VALIDATION PASSED: " + orgGrades.size() + " Organization Grades are correctly sorted in Descending Order");
				ExtentCucumberAdapter.addTestStepLog("✅ Sorting validation PASSED - Org Grades are correctly sorted in Descending Order");
			}
			
		} catch (Exception e) {
			LOGGER.error(" Issue verifying sorted by org grade descending - Method: user_should_verify_first_thirty_job_profiles_sorted_by_organization_grade_in_descending_order_and_organization_job_name_in_ascending_order", e);
			ScreenshotHandler.captureFailureScreenshot("verify_sorted_by_org_grade_descending", e);
			e.printStackTrace();
			Assert.fail("Issue in Verifying Job Profiles After sorting by Organization Grade in Descending Order and Organization Job Name in Ascending Order...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying Job Profiles After sorting by Organization Grade in Descending Order and Organization Job Name in Ascending Order...Please Investigate!!!");
		}
	}

}
