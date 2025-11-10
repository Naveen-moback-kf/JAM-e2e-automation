package com.kfonetalentsuite.pageobjects;

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

import com.kfonetalentsuite.utils.Utilities;
import com.kfonetalentsuite.utils.PerformanceUtils;
import com.kfonetalentsuite.utils.ScreenshotHandler;
import com.kfonetalentsuite.webdriverManager.DriverManager;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

public class PO17_ValidateSortingFunctionality {
	WebDriver driver = DriverManager.getDriver();	

	protected static final Logger LOGGER = (Logger) LogManager.getLogger();
	PO17_ValidateSortingFunctionality validateSortingFunctionality;
	
	public static ArrayList<String> jobNamesTextInDefaultOrder = new ArrayList<String>();
//	public static ArrayList<WebElement> MatchedSPNameElements = new ArrayList<WebElement>();
	

	public PO17_ValidateSortingFunctionality() throws IOException {
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
			js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			// PERFORMANCE: Replaced 3x Thread.sleep(3000) = 9 seconds with smart page ready wait!
			PerformanceUtils.waitForPageReady(driver);
			js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
			PerformanceUtils.waitForElement(driver, showingJobResultsCount);
			String resultsCountText_updated = wait.until(ExpectedConditions.visibilityOf(showingJobResultsCount)).getText();
			LOGGER.info("Scrolled down till third page and now "+ resultsCountText_updated + " of Job Profiles as expected");
			ExtentCucumberAdapter.addTestStepLog("Scrolled down till third page and now "+ resultsCountText_updated + " of Job Profiles as expected");	
			js.executeScript("window.scrollTo(document.body.scrollHeight, 0)");
		wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
		// PERFORMANCE: Replaced Thread.sleep(2000) with smart page ready wait
		PerformanceUtils.waitForPageReady(driver);	
	} catch (Exception e) {
		LOGGER.error("❌ Issue in scrolling page - Method: scroll_page_down_two_times", e);
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
		LOGGER.error("❌ Issue verifying default order - Method: verify_first_thirty_job_profiles_in_default_order", e);
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
		LOGGER.error("❌ Issue sorting by org job name ascending - Method: sort_job_profiles_by_organiztion_job_name_in_ascending_order", e);
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
			for (WebElement element: allElements) {
				String text = element.getText();
			LOGGER.info("Organization Job Profile with Job Name / Code : " + text);
			ExtentCucumberAdapter.addTestStepLog("Organization Job Profile with Name Job Name / Code : " + text);
		}
	} catch (Exception e) {
		LOGGER.error("❌ Issue verifying sorted profiles ascending - Method: user_should_verify_first_thirty_job_profiles_sorted_by_organiztion_job_name_in_ascending_order", e);
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
		LOGGER.error("❌ Issue verifying default order after refresh - Method: user_should_refresh_job_mapping_and_verify_job_profiles_are_in_default_order", e);
		ScreenshotHandler.captureFailureScreenshot("verify_default_order_after_refresh", e);
		e.printStackTrace();
		Assert.fail("Issue in Verifying Default order of Job Profiles after Refreshing Job Mapping page...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in Verifying Default order of Job Profiles after Refreshing Job Mapping page...Please Investigate!!!");
	}
	}
	
	/**
	 * Sorts job profiles by job name in descending order (clicks header twice)
	 * Page already loaded, directly clicks to sort
	 */
	public void sort_job_profiles_by_organiztion_job_name_in_descending_order() {
		try {
			// Page already loaded, directly click to sort
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
			// PERFORMANCE: Replaced Thread.sleep(3000) with smart page ready wait
			PerformanceUtils.waitForPageReady(driver);
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			try {
				wait.until(ExpectedConditions.visibilityOf(orgJobNameHeader)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", orgJobNameHeader);
				} catch (Exception s) {
					utils.jsClick(driver, orgJobNameHeader);
				}
			}
//			wait.until(ExpectedConditions.visibilityOf(orgJobNameHeader)).click();
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			// PERFORMANCE: Replaced Thread.sleep(2000) with smart page ready wait
		PerformanceUtils.waitForPageReady(driver);
		LOGGER.info("Clicked two times on Organization job name / code header to Sort Job Profiles by Name in Descending order");
		ExtentCucumberAdapter.addTestStepLog("Clicked two times on Organization job name / code header to Sort Job Profiles by Name in Descending order");
	} catch (Exception e) {
		LOGGER.error("❌ Issue sorting by org job name descending - Method: sort_job_profiles_by_organiztion_job_name_in_descending_order", e);
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
			for (WebElement element: allElements) {
				String text = element.getText();
			LOGGER.info("Organization Job Profile with Job Name / Code : " + text);
			ExtentCucumberAdapter.addTestStepLog("Organization Job Profile with Name Job Name / Code : " + text);
		}
	} catch (Exception e) {
		LOGGER.error("❌ Issue verifying sorted profiles descending - Method: user_should_verify_first_thirty_job_profiles_sorted_by_organiztion_job_name_in_descending_order", e);
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
		LOGGER.error("❌ Issue sorting by SP grade ascending - Method: sort_job_profiles_by_matched_success_profile_grade_in_ascending_order", e);
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
			List<WebElement> MatchedSPGrades = driver.findElements(By.xpath("//*[@id='kf-job-container']/div/table/tbody/tr/td[2]/div"));
			LOGGER.info("Below are Job Profiles After sorting by Matched Success Profile Grade in Ascending Order:");
			ExtentCucumberAdapter.addTestStepLog("Below are Job Profiles After sorting by Matched Success Profile Grade in Ascending Order:");
			
			// Use the smaller size to avoid IndexOutOfBoundsException when unmapped jobs exist
			int iterationLimit = Math.min(allElements.size(), MatchedSPGrades.size());
			int unmappedCount = 0;
			
			for (int i = 0; i < iterationLimit; i++) {
				WebElement element = allElements.get(i);
				String text = element.getText();
				WebElement GradeElement = MatchedSPGrades.get(i);
				String gradeText = GradeElement.getText();
				
				// Check if this is an unmapped job (no SP grade)
				if (gradeText == null || gradeText.trim().isEmpty() || gradeText.equals("-")) {
					unmappedCount++;
					LOGGER.info("Organization Job Profile with Job Name / Code : " + text + " - UNMAPPED (No SP Grade)");
					ExtentCucumberAdapter.addTestStepLog("Organization Job Profile: " + text + " - UNMAPPED (No SP Grade)");
				} else {
					LOGGER.info("Organization Job Profile with Job Name / Code : " + text + " with Matched SP Grade : " + gradeText);
					ExtentCucumberAdapter.addTestStepLog("Organization Job Profile: " + text + " with Matched SP Grade : " + gradeText);
				}
			}
			
			if (unmappedCount > 0) {
				LOGGER.info("Found {} unmapped job(s) without SP grade details", unmappedCount);
				ExtentCucumberAdapter.addTestStepLog("Found " + unmappedCount + " unmapped job(s) without SP grade details");
			}
		} catch (Exception e) {
			LOGGER.error("❌ Issue verifying sorted by SP grade ascending - Method: user_should_verify_first_thirty_job_profiles_sorted_by_matched_success_profile_grade_in_ascending_order", e);
			ScreenshotHandler.captureFailureScreenshot("verify_sorted_by_sp_grade_ascending", e);
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying Job Profiles After sorting by Matched Success Profile Grade in Ascending Order...Please Investigate!!!");
		}
	}
	
	/**
	 * Sorts job profiles by matched SP grade in descending order (clicks header twice)
	 * Page already loaded, directly clicks to sort
	 */
	public void sort_job_profiles_by_matched_success_profile_grade_in_descending_order() {
		try {
			// Page already loaded, directly click to sort
			wait.until(ExpectedConditions.visibilityOf(matchedSPGradeHeader)).click();
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			// PERFORMANCE: Replaced Thread.sleep(3000) with smart page ready wait
			PerformanceUtils.waitForPageReady(driver);
			try {
				wait.until(ExpectedConditions.visibilityOf(matchedSPGradeHeader)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", matchedSPGradeHeader);
				} catch (Exception s) {
					utils.jsClick(driver, matchedSPGradeHeader);
				}
			}
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			// PERFORMANCE: Replaced Thread.sleep(2000) with smart page ready wait
		PerformanceUtils.waitForPageReady(driver);
		LOGGER.info("Clicked two times on Matched Success Profile Grade header to Sort Job Profiles by Matched SP Grade in descending order");
		ExtentCucumberAdapter.addTestStepLog("Clicked two times on Matched Success Profile Grade header to Sort Job Profiles by Matched SP Grade in descending order");
	} catch (Exception e) {
		LOGGER.error("❌ Issue sorting by SP grade descending - Method: sort_job_profiles_by_matched_success_profile_grade_in_descending_order", e);
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
			List<WebElement> MatchedSPGrades = driver.findElements(By.xpath("//*[@id='kf-job-container']/div/table/tbody/tr/td[2]/div"));
			LOGGER.info("Below are Job Profiles After sorting by Matched Success Profile Grade in Descending Order:");
			ExtentCucumberAdapter.addTestStepLog("Below are Job Profiles After sorting by Matched Success Profile Grade in Descending Order:");
			
			// Use the smaller size to avoid IndexOutOfBoundsException when unmapped jobs exist
			int iterationLimit = Math.min(allElements.size(), MatchedSPGrades.size());
			int unmappedCount = 0;
			
			for (int i = 0; i < iterationLimit; i++) {
				WebElement element = allElements.get(i);
				String text = element.getText();
				WebElement GradeElement = MatchedSPGrades.get(i);
				String gradeText = GradeElement.getText();
				
				// Check if this is an unmapped job (no SP grade)
				if (gradeText == null || gradeText.trim().isEmpty() || gradeText.equals("-")) {
					unmappedCount++;
					LOGGER.info("Organization Job Profile with Job Name / Code : " + text + " - UNMAPPED (No SP Grade)");
					ExtentCucumberAdapter.addTestStepLog("Organization Job Profile: " + text + " - UNMAPPED (No SP Grade)");
				} else {
					LOGGER.info("Organization Job Profile with Job Name / Code : " + text + " with Matched SP Grade : " + gradeText);
					ExtentCucumberAdapter.addTestStepLog("Organization Job Profile: " + text + " with Matched SP Grade : " + gradeText);
				}
			}
			
			if (unmappedCount > 0) {
				LOGGER.info("Found {} unmapped job(s) without SP grade details", unmappedCount);
				ExtentCucumberAdapter.addTestStepLog("Found " + unmappedCount + " unmapped job(s) without SP grade details");
			}
		} catch (Exception e) {
			LOGGER.error("❌ Issue verifying sorted by SP grade descending - Method: user_should_verify_first_thirty_job_profiles_sorted_by_matched_success_profile_grade_in_descending_order", e);
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
		LOGGER.error("❌ Issue sorting by SP name ascending - Method: sort_job_profiles_by_matched_success_profile_name_in_ascending_order", e);
		ScreenshotHandler.captureFailureScreenshot("sort_by_sp_name_ascending", e);
		e.printStackTrace();
		Assert.fail("Issue in clicking on Matched Success Profile Name header to Sort Job Profiles by Matched SP Name in ascending order...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in clicking on Matched Success Profile Name header to Sort Job Profiles by Matched SP Name in ascending order...Please Investigate!!!");
	}
	}
	
	public void user_should_verify_first_thirty_job_profiles_sorted_by_matched_success_profile_name_in_ascending_order() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			// PERFORMANCE: Replaced Thread.sleep(3000) with smart page ready wait
			PerformanceUtils.waitForPageReady(driver);
			List<WebElement> allElements = driver.findElements(By.xpath("//tbody//tr//td[2]//div[contains(text(),'(')]"));
			
			// Dynamically find all SP Name elements that actually exist (avoiding unmapped jobs)
			List<WebElement> MatchedSPNameElements = driver.findElements(By.xpath("//*[@id='kf-job-container']/div/table/tbody/tr/td[1]/div"));
			
			LOGGER.info("Below are Job Profiles After sorting by Matched Success Profile Name in Ascending Order:");
			ExtentCucumberAdapter.addTestStepLog("Below are Job Profiles After sorting by Matched Success Profile Name in Ascending Order:");
			
			// Use the smaller size to avoid IndexOutOfBoundsException when unmapped jobs exist
			int iterationLimit = Math.min(allElements.size(), MatchedSPNameElements.size());
			int unmappedCount = 0;
			
			for (int i = 0; i < iterationLimit; i++) {
				WebElement element = allElements.get(i);
				String text = element.getText();
				WebElement MatchedSPNameElement = MatchedSPNameElements.get(i);
				String NameText = MatchedSPNameElement.getText();
				
				// Check if this is an unmapped job (no SP name)
				if (NameText == null || NameText.trim().isEmpty() || NameText.equals("-")) {
					unmappedCount++;
					LOGGER.info("Organization Job Profile with Job Name / Code : " + text + " - UNMAPPED (No SP Name)");
					ExtentCucumberAdapter.addTestStepLog("Organization Job Profile: " + text + " - UNMAPPED (No SP Name)");
				} else {
					LOGGER.info("Organization Job Profile with Job Name / Code : " + text + " with Matched SP Name : " + NameText);
					ExtentCucumberAdapter.addTestStepLog("Organization Job Profile: " + text + " with Matched SP Name : " + NameText);
				}
			}
			
			if (unmappedCount > 0) {
				LOGGER.info("Found {} unmapped job(s) without SP name details", unmappedCount);
				ExtentCucumberAdapter.addTestStepLog("Found " + unmappedCount + " unmapped job(s) without SP name details");
			}
		} catch (Exception e) {
			LOGGER.error("❌ Issue verifying sorted by SP name ascending - Method: user_should_verify_first_thirty_job_profiles_sorted_by_matched_success_profile_name_in_ascending_order", e);
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
		LOGGER.error("❌ Issue sorting by org grade ascending - Method: sort_job_profiles_by_organization_grade_in_ascending_order", e);
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
			
			for (int i = 0; i < iterationLimit; i++) {
				WebElement element = allElements.get(i);
				String text = element.getText();
				WebElement OrgGradeElement = OrgGradeElements.get(i);
				String GradeText = OrgGradeElement.getText();
				LOGGER.info("Organization Job Profile with Job Name / Code : " + text + " with Organization Job Grade : " + GradeText);
				ExtentCucumberAdapter.addTestStepLog("Organization Job Profile with Job Name / Code : " + text + " with Organization Job Grade : " + GradeText);
			}
		} catch (Exception e) {
			LOGGER.error("❌ Issue verifying sorted by org grade ascending - Method: user_should_verify_first_thirty_job_profiles_sorted_by_organization_grade_and_organization_job_name_in_ascending_order", e);
			ScreenshotHandler.captureFailureScreenshot("verify_sorted_by_org_grade_ascending", e);
			e.printStackTrace();
			Assert.fail("Issue in Verifying Job Profiles After sorting by Organization Grade and Organization Job Name in Ascending Order...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying Job Profiles After sorting by Organization Grade and Organization Job Name in Ascending Order...Please Investigate!!!");
		}
	}
	
	public void sort_job_profiles_by_organization_grade_in_descending_order() {
		try {
			wait.until(ExpectedConditions.visibilityOf(orgJobGradeHeader)).click();
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			// PERFORMANCE: Replaced Thread.sleep(3000) with smart page ready wait
			PerformanceUtils.waitForPageReady(driver);
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			// PERFORMANCE: Replaced Thread.sleep(2000) with smart page ready wait
			PerformanceUtils.waitForPageReady(driver);
			wait.until(ExpectedConditions.visibilityOf(orgJobGradeHeader)).click();
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			// PERFORMANCE: Replaced Thread.sleep(2000) with smart page ready wait
		PerformanceUtils.waitForPageReady(driver);
		LOGGER.info("Clicked two times on Organization Grade header to Sort Job Profiles by Grade in Descending order");
		ExtentCucumberAdapter.addTestStepLog("Clicked two times on Organization Grade to Sort Job Profiles by Grade in Descending order");
	} catch (Exception e) {
		LOGGER.error("❌ Issue sorting by org grade descending - Method: sort_job_profiles_by_organization_grade_in_descending_order", e);
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
			
			for (int i = 0; i < iterationLimit; i++) {
				WebElement element = allElements.get(i);
				String text = element.getText();
				WebElement OrgGradeElement = OrgGradeElements.get(i);
				String GradeText = OrgGradeElement.getText();
				LOGGER.info("Organization Job Profile with Job Name / Code : " + text + " with Organization Job Grade : " + GradeText);
				ExtentCucumberAdapter.addTestStepLog("Organization Job Profile with Job Name / Code : " + text + " with Organization Job Grade : " + GradeText);
			}
		} catch (Exception e) {
			LOGGER.error("❌ Issue verifying sorted by org grade descending - Method: user_should_verify_first_thirty_job_profiles_sorted_by_organization_grade_in_descending_order_and_organization_job_name_in_ascending_order", e);
			ScreenshotHandler.captureFailureScreenshot("verify_sorted_by_org_grade_descending", e);
			e.printStackTrace();
			Assert.fail("Issue in Verifying Job Profiles After sorting by Organization Grade in Descending Order and Organization Job Name in Ascending Order...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying Job Profiles After sorting by Organization Grade in Descending Order and Organization Job Name in Ascending Order...Please Investigate!!!");
		}
	}

}
