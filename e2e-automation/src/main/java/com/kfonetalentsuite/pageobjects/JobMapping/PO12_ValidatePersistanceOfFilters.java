package com.kfonetalentsuite.pageobjects.JobMapping;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
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

public class PO12_ValidatePersistanceOfFilters {
	WebDriver driver = DriverManager.getDriver();	

	protected static final Logger LOGGER = (Logger) LogManager.getLogger();
	PO12_ValidatePersistanceOfFilters validatePersistanceOfFilters;
	

	public PO12_ValidatePersistanceOfFilters() throws IOException {
		// super();
		PageFactory.initElements(driver, this);
	}
	
	WebDriverWait wait = DriverManager.getWait();
	Utilities utils = new Utilities();
	JavascriptExecutor js = (JavascriptExecutor) driver;
	
	//XPATHs
	@FindBy(xpath = "//div[@data-testid='loader']//img")
	@CacheLookup
	WebElement pageLoadSpinner2;
	
	@FindBy(xpath = "//button[@data-testid='Clear Filters']")
	@CacheLookup
	WebElement clearFiltersBtn;
	
	@FindBy(xpath = "//div[contains(@id,'results-toggle')]//*[contains(text(),'Showing')]")
	@CacheLookup
	public WebElement showingJobResultsCount;
	
	@FindBy(xpath = "//div[contains(@id,'results-toggle')]//label//div//div[2]")
	@CacheLookup
	public WebElement viewPublishedToggleBtn;
	
	@FindBy(xpath = "//*[@id='org-job-container']/div/table/thead/tr/th[3]/div//span//*[@class='text-blue-600']")
	@CacheLookup
	public WebElement orgJobGradeSortIcon;
	
	@FindBy(xpath = "//*[@id='kf-job-container']/div/table/thead/tr/th[2]/div//span//*[@class='text-blue-600']")
	@CacheLookup
	public WebElement matchedSPGradeSortIcon;
	
	//Methods
	public void refresh_job_mapping_page() {
		try {
			driver.navigate().refresh();
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForPageReady(driver, 2);
		LOGGER.info("Refreshed Job Mapping page...");
		ExtentCucumberAdapter.addTestStepLog("Refreshed Job Mapping page...");
	} catch (Exception e) {
		ScreenshotHandler.captureFailureScreenshot("refresh_job_mapping_page", e);
		LOGGER.error("Issue in Refreshing Job Mapping page - Method: refresh_job_mapping_page", e);
		e.printStackTrace();
		ExtentCucumberAdapter.addTestStepLog("Issue in Refreshing Job Mapping page...Please Investigate!!!");
		Assert.fail("Issue in Refreshing Job Mapping page...Please Investigate!!!");
	}
	}
	
	public void verify_applied_filters_persist_on_job_mapping_ui() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForPageReady(driver, 2);
			wait.until(ExpectedConditions.visibilityOf(clearFiltersBtn)).isDisplayed();
			String resultsCountText2 = wait.until(ExpectedConditions.visibilityOf(showingJobResultsCount)).getText();
		Assert.assertEquals(PO04_VerifyJobMappingPageComponents.updatedResultsCount,resultsCountText2);
		LOGGER.info("Applied Filters Persisted on Job Mapping page as expected");
		ExtentCucumberAdapter.addTestStepLog("Applied Filters Persisted on Job Mapping page as expected");
	} catch (Exception e) {
		ScreenshotHandler.captureFailureScreenshot("verify_applied_filters_persist_on_job_mapping_ui", e);
		LOGGER.error("Applied Filters Not Persisted on Job Mapping page - Method: verify_applied_filters_persist_on_job_mapping_ui", e);
		e.printStackTrace();
		ExtentCucumberAdapter.addTestStepLog("Applied Filters Not Persisted on Job Mapping page....Please Investigate!!!");
		Assert.fail("Applied Filters Not Persisted on Job Mapping page....Please Investigate!!!");
	}
	}
	
	public void click_on_browser_back_button() {
		try {
			driver.navigate().back();
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
		PerformanceUtils.waitForPageReady(driver, 2);
		LOGGER.info("Browser back button is clicked...");
		ExtentCucumberAdapter.addTestStepLog("Browser back button is clicked...");
	} catch (Exception e) {
		ScreenshotHandler.captureFailureScreenshot("click_on_browser_back_button", e);
		LOGGER.error("Issue in clicking Browser back button - Method: click_on_browser_back_button", e);
		e.printStackTrace();
		ExtentCucumberAdapter.addTestStepLog("Issue in clicking Browser back button....Please Investigate!!!");
		Assert.fail("Issue in clicking Browser back button....Please Investigate!!!");
	}
	}
	
	public void verify_view_published_toggle_button_is_persisted() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForPageReady(driver, 2);
			wait.until(ExpectedConditions.visibilityOf(viewPublishedToggleBtn)).isEnabled();
			LOGGER.info("View Published toggle button is Persisted on Job Mapping page as expected");
			ExtentCucumberAdapter.addTestStepLog("View Published toggle button is Persisted on Job Mapping page as expected");
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_view_published_toggle_button_is_persisted", e);
			LOGGER.error("View Published toggle button Not Persisted on Job Mapping page - Method: verify_view_published_toggle_button_is_persisted", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("View Published toggle button Not Persisted on Job Mapping page....Please Investigate!!!");
			Assert.fail("View Published toggle button Not Persisted on Job Mapping page....Please Investigate!!!");
		}
	}
	
	public void verify_applied_sorting_persist_on_job_mapping_ui() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForPageReady(driver, 2);
		wait.until(ExpectedConditions.visibilityOf(orgJobGradeSortIcon)).isDisplayed();
		LOGGER.info("Sorting Persisted on Job Mapping page as expected");
		ExtentCucumberAdapter.addTestStepLog("Sorting Persisted on Job Mapping page as expected");
	} catch (Exception e) {
		try {
				wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
				PerformanceUtils.waitForPageReady(driver, 2);
				wait.until(ExpectedConditions.visibilityOf(matchedSPGradeSortIcon)).isDisplayed();
				LOGGER.info("Sorting Persisted on Job Mapping page as expected");
				ExtentCucumberAdapter.addTestStepLog("Sorting Persisted on Job Mapping page as expected");
			} catch (Exception s) {
				ScreenshotHandler.captureFailureScreenshot("verify_applied_sorting_persist_on_job_mapping_ui", s);
				LOGGER.error("Issue in validating Sorting Persistance in Job Mapping page - Method: verify_applied_sorting_persist_on_job_mapping_ui", s);
				e.printStackTrace();
				s.printStackTrace();
				ExtentCucumberAdapter.addTestStepLog("Issue in validating Sorting Persistance in Job Mapping page....Please Investigate!!!");
				Assert.fail("Issue in validating Sorting Persistance in Job Mapping page....Please Investigate!!!");
			}
		}
	}
	
	public void user_is_in_job_mapping_page_with_grades_filters_applied() {
		PerformanceUtils.waitForPageReady(driver, 1);
		ExtentCucumberAdapter.addTestStepLog("User is in Job Mapping page with Grades Filters applied");
		LOGGER.info("User is in Job Mapping page with Grades Filters applied");
	}
	
	public void user_is_in_job_mapping_page_with_mapping_status_filters_applied() {
		PerformanceUtils.waitForPageReady(driver, 1);
		ExtentCucumberAdapter.addTestStepLog("User is in Job Mapping page with Mapping Status Filters applied");
		LOGGER.info("User is in Job Mapping page with Mapping Status Filters applied");
	}
	
	public void user_is_in_view_published_screen_with_grades_filters_applied() {
		PerformanceUtils.waitForPageReady(driver, 1);
		ExtentCucumberAdapter.addTestStepLog("User is in View Published screen with Grades filters applied");
		LOGGER.info("User is in View Published screen with Grades filters applied");
	}
	
	public void user_is_in_view_published_screen_with_multiple_filters_applied() {
		PerformanceUtils.waitForPageReady(driver, 1);
		ExtentCucumberAdapter.addTestStepLog("User is in View Published screen with multiple filters applied");
		LOGGER.info("User is in View Published screen with multiple filters applied");
	}
	
	public void user_is_in_job_mapping_page_with_multi_level_sorting_applied() {
		PerformanceUtils.waitForPageReady(driver, 1);
		ExtentCucumberAdapter.addTestStepLog("User is in Job Mapping page with multi-level sorting applied");
		LOGGER.info("User is in Job Mapping page with multi-level sorting applied");
	}
	
	public void user_is_in_job_mapping_page_with_sorting_and_filters_applied() {
		PerformanceUtils.waitForPageReady(driver, 1);
		ExtentCucumberAdapter.addTestStepLog("User is in Job Mapping page with sorting and filters applied");
		LOGGER.info("User is in Job Mapping page with sorting and filters applied");
	}
	
	public void user_is_in_view_published_screen_with_sorting_applied() {
		PerformanceUtils.waitForPageReady(driver, 1);
		ExtentCucumberAdapter.addTestStepLog("User is in View Published screen with sorting applied");
		LOGGER.info("User is in View Published screen with sorting applied");
	}

}
