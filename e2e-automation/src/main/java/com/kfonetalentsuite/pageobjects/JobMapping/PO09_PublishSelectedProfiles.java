package com.kfonetalentsuite.pageobjects.JobMapping;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
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

public class PO09_PublishSelectedProfiles {
	WebDriver driver = DriverManager.getDriver();	

	protected static final Logger LOGGER = (Logger) LogManager.getLogger();
	PO09_PublishSelectedProfiles publishSelectedProfiles;
	

	public PO09_PublishSelectedProfiles() throws IOException {
		// super();
		PageFactory.initElements(driver, this);
	}

	WebDriverWait wait = DriverManager.getWait();
	Utilities utils = new Utilities();
	JavascriptExecutor js = (JavascriptExecutor) driver;
	LocalDate currentdate = LocalDate.now();
	Month currentMonth = currentdate.getMonth();
	
	//XPATHS
	@FindBy(xpath = "//*[@class='blocking-loader']//img")
	@CacheLookup
	WebElement pageLoadSpinner1;
	
	@FindBy(xpath = "//div[@data-testid='loader']//img")
	@CacheLookup
	WebElement pageLoadSpinner2;
	
	@FindBy(xpath = "//input[contains(@id,'search-job-title')]")
	@CacheLookup
	public WebElement searchBar;
	
	@FindBy(xpath = "//tbody//tr[1]//td[2]//div[contains(text(),'(')]")
	@CacheLookup
	public WebElement jobNameinRow1;
	
	@FindBy(xpath = "//tbody//tr[4]//td[2]//div[contains(text(),'(')]")
	@CacheLookup
	public WebElement jobNameinRow2;
	
	@FindBy(xpath = "//tbody//tr[2]//button[text()='Published'][1]")
	@CacheLookup
	public WebElement job1PublishedBtn;
	
	@FindBy(xpath = "//tbody//tr[5]//button[text()='Published'][1]")
	@CacheLookup
	public WebElement job2PublishedBtn;
	
	@FindBy(xpath = "//input[@type='search']")
	@CacheLookup
	public WebElement HCMJobProfilesSearch;
	
	@FindBy(xpath = "//tbody//tr[1]//td//div//span[1]//a")
	@CacheLookup
	public WebElement HCMJobProfilesJobinRow1;
	
	@FindBy(xpath = "//tbody//tr[1]//td[7]//span")
	@CacheLookup
	public WebElement HCMJobProfilesDateinRow1;
	
	@FindBy(xpath = "//tbody//tr[2]//td//div//span[1]//a")
	@CacheLookup
	public WebElement HCMJobProfilesJobinRow2;
	
	@FindBy(xpath = "//tbody//tr[2]//td[7]//span")
	@CacheLookup
	public WebElement HCMJobProfilesDateinRow2;
	
	//Methods
	
	public void search_for_published_job_name1() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForPageReady(driver, 2);
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
		wait.until(ExpectedConditions.visibilityOf(searchBar)).click();
		wait.until(ExpectedConditions.visibilityOf(searchBar)).clear();
		wait.until(ExpectedConditions.visibilityOf(searchBar)).sendKeys(PO04_VerifyJobMappingPageComponents.orgJobNameInRow1.get());
		wait.until(ExpectedConditions.visibilityOf(searchBar)).sendKeys(Keys.ENTER);
		wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
		PerformanceUtils.waitForPageReady(driver, 2);
		LOGGER.info("Entered job name as " + PO04_VerifyJobMappingPageComponents.orgJobNameInRow1.get() + " in the search bar in View Published screen");
		ExtentCucumberAdapter.addTestStepLog("Entered job name as " + PO04_VerifyJobMappingPageComponents.orgJobNameInRow1.get() + " in the search bar in View Published screen");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("search_for_published_job_name1", e);
			LOGGER.error("Failed to enter Organization job name in search - Method: search_published_job_name1", e);
			e.printStackTrace();
				ExtentCucumberAdapter.addTestStepLog("Failed to enter Organization job name text in search bar in View Published screen...Please investigate!!!");
			Assert.fail("Failed to enter Organization job name text in search bar in View Published screen...Please investigate!!!");
		}
	}
	
	public void user_should_verify_published_first_job_profile_is_displayed_in_row1_in_view_published_screen() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
		PerformanceUtils.waitForPageReady(driver, 2);
		String job1NameText = wait.until(ExpectedConditions.visibilityOf(jobNameinRow1)).getText();
		Assert.assertEquals(PO04_VerifyJobMappingPageComponents.orgJobNameInRow1.get(), job1NameText.split("-", 2)[0].trim());
		Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(job1PublishedBtn)).isDisplayed());
		LOGGER.info("Published Job with name : " + job1NameText.split("-", 2)[0].trim() + "is displayed in view published screen as expected in Row1");
			ExtentCucumberAdapter.addTestStepLog("Published Job with name : " + job1NameText.split("-", 2)[0].trim() + "is displayed in view published screen as expected in Row1");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_verify_published_first_job_profile_is_displayed_in_row1_in_view_published_screen", e);
			LOGGER.error("Issue in verifying published first job profile Row1 search - Method: user_should_verify_published_first_job_profile_is_displayed_in_row1_in_view_published_screen", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying Published First job profile in Row1 in using Search in view published screen...Please Investigate!!!");
			Assert.fail("Issue in Verifying Published First job profile in Row1 in using Search in view published screen...Please Investigate!!!");
		}
		}
	
public void search_for_published_job_name2() {
	try {
		wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
		PerformanceUtils.waitForPageReady(driver, 2);
		wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
	wait.until(ExpectedConditions.visibilityOf(searchBar)).click();
	wait.until(ExpectedConditions.visibilityOf(searchBar)).clear();
	wait.until(ExpectedConditions.visibilityOf(searchBar)).sendKeys(PO04_VerifyJobMappingPageComponents.orgJobNameInRow2.get());
	wait.until(ExpectedConditions.visibilityOf(searchBar)).sendKeys(Keys.ENTER);
	wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
	PerformanceUtils.waitForPageReady(driver, 2);
	LOGGER.info("Entered job name as " + PO04_VerifyJobMappingPageComponents.orgJobNameInRow2.get() + " in the search bar in View Published screen");
	ExtentCucumberAdapter.addTestStepLog("Entered job name as " + PO04_VerifyJobMappingPageComponents.orgJobNameInRow2.get() + " in the search bar in View Published screen");
	} catch (Exception e) {
		ScreenshotHandler.captureFailureScreenshot("search_for_published_job_name2", e);
		LOGGER.error("Failed to enter Organization job name in search - Method: search_published_job_name2", e);
		e.printStackTrace();
		ExtentCucumberAdapter.addTestStepLog("Failed to enter Organization job name text in search bar in View Published screen...Please investigate!!!");
		Assert.fail("Failed to enter Organization job name text in search bar in View Published screen...Please investigate!!!");
	}
	}
	
public void user_should_verify_published_second_job_profile_is_displayed_in_row1_in_view_published_screen() {
	try {
		wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
	PerformanceUtils.waitForPageReady(driver, 2);
	String job1NameText = wait.until(ExpectedConditions.visibilityOf(jobNameinRow1)).getText();
	Assert.assertEquals(PO04_VerifyJobMappingPageComponents.orgJobNameInRow2.get(), job1NameText.split("-", 2)[0].trim());
	Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(job1PublishedBtn)).isDisplayed());
	LOGGER.info("Published Job with name : " + job1NameText.split("-", 2)[0].trim() + "is displayed in view published screen as expected in Row1");
		ExtentCucumberAdapter.addTestStepLog("Published Job with name : " + job1NameText.split("-", 2)[0].trim() + "is displayed in view published screen as expected in Row1");
	} catch (Exception e) {
		ScreenshotHandler.captureFailureScreenshot("user_should_verify_published_second_job_profile_is_displayed_in_row1_in_view_published_screen", e);
		LOGGER.error("Issue in verifying published second job profile Row1 search - Method: user_should_verify_published_second_job_profile_is_displayed_in_row1_in_view_published_screen", e);
		e.printStackTrace();
		ExtentCucumberAdapter.addTestStepLog("Issue in Verifying Published Second job profile in Row1 in using Search in view published screen...Please Investigate!!!");
		Assert.fail("Issue in Verifying Published Second job profile in Row1 in using Search in view published screen...Please Investigate!!!");
	}
	}
	
public void user_should_verify_date_on_published_first_job_matches_with_current_date() {
	try {
		PerformanceUtils.waitForPageReady(driver, 2);
		int currentYear = currentdate.getYear();
			int currentDay = currentdate.getDayOfMonth();
			String cd;
			String todayDate;
			if(Integer.toString(currentDay).length() == 1) {
				DecimalFormat df = new DecimalFormat("00"); 
				cd = df.format(currentDay);
				todayDate = currentMonth.toString().substring(0,1) + currentMonth.toString().substring(1,3).toLowerCase() + " " + cd + ", " + Integer.toString(currentYear);
			} else {
				todayDate = currentMonth.toString().substring(0,1) + currentMonth.toString().substring(1,3).toLowerCase() + " " + Integer.toString(currentDay) + ", " + Integer.toString(currentYear);
			}
		String jobPublishedDate = wait.until(ExpectedConditions.visibilityOf(HCMJobProfilesDateinRow1)).getText();
	Assert.assertEquals(jobPublishedDate, todayDate);
	LOGGER.info("Current date is verified successfully on Published First Job with name : " + PO04_VerifyJobMappingPageComponents.orgJobNameInRow1.get() );
	ExtentCucumberAdapter.addTestStepLog("Current date is verified successfully on Published First Job with name : " + PO04_VerifyJobMappingPageComponents.orgJobNameInRow1.get() );
	} catch (Exception e) {
		ScreenshotHandler.captureFailureScreenshot("user_should_verify_date_on_published_first_job_matches_with_current_date", e);
		LOGGER.error("Issue in verifying published date matches current date - Method: verify_date_published_first_job_current_date", e);
		e.printStackTrace();
		ExtentCucumberAdapter.addTestStepLog("Issue in verifying date on published first job that matches with current date...Please Investigate!!!");
		Assert.fail("Issue in verifying date on published first job that matches with current date...Please Investigate!!!");
	}
	}
	
	
	// REMOVED: Unused method - corresponding step is commented out in feature file
	
	public void user_should_verify_date_on_published_second_job_matches_with_current_date() {
		try {
			int currentYear = currentdate.getYear();
			int currentDay = currentdate.getDayOfMonth();
			String cd;
			String todayDate;
			if(Integer.toString(currentDay).length() == 1) {
				DecimalFormat df = new DecimalFormat("00"); 
				cd = df.format(currentDay);
				todayDate = currentMonth.toString().substring(0,1) + currentMonth.toString().substring(1,3).toLowerCase() + " " + cd + ", " + Integer.toString(currentYear);
			} else {
				todayDate = currentMonth.toString().substring(0,1) + currentMonth.toString().substring(1,3).toLowerCase() + " " + Integer.toString(currentDay) + ", " + Integer.toString(currentYear);
			}
		String jobPublishedDate = wait.until(ExpectedConditions.visibilityOf(HCMJobProfilesDateinRow2)).getText();
	Assert.assertEquals(jobPublishedDate, todayDate);
	LOGGER.info("Current date is verified successfully on Published Second Job with name : " + PO04_VerifyJobMappingPageComponents.orgJobNameInRow2.get() );
	ExtentCucumberAdapter.addTestStepLog("Current date is verified successfully on Published Second Job with name : " + PO04_VerifyJobMappingPageComponents.orgJobNameInRow2.get() );
	} catch (Exception e) {
		ScreenshotHandler.captureFailureScreenshot("user_should_verify_date_on_published_second_job_matches_with_current_date", e);
		LOGGER.error("Issue in verifying published second job date matches current date - Method: verify_date_published_second_job_current_date", e);
		e.printStackTrace();
		ExtentCucumberAdapter.addTestStepLog("Issue in verifying date on published second job that matches with current date...Please Investigate!!!");
		Assert.fail("Issue in verifying date on published second job that matches with current date...Please Investigate!!!");
	}
	}
	
	public void search_for_published_job_name2_in_hcm_sync_profiles_tab_in_pm() {
		Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(HCMJobProfilesSearch)).isDisplayed());
		try {
		wait.until(ExpectedConditions.elementToBeClickable(HCMJobProfilesSearch)).click();
		wait.until(ExpectedConditions.visibilityOf(HCMJobProfilesSearch)).clear();
		wait.until(ExpectedConditions.visibilityOf(HCMJobProfilesSearch)).sendKeys(PO04_VerifyJobMappingPageComponents.orgJobNameInRow2.get());
	wait.until(ExpectedConditions.visibilityOf(HCMJobProfilesSearch)).sendKeys(Keys.ENTER);
	wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner1));
	PerformanceUtils.waitForPageReady(driver, 2);
	LOGGER.info("Entered job name2 as " + PO04_VerifyJobMappingPageComponents.orgJobNameInRow2.get() + " in the search bar in HCM Sync Profiles screen in PM");
	ExtentCucumberAdapter.addTestStepLog("Entered job name2 as " + PO04_VerifyJobMappingPageComponents.orgJobNameInRow2.get() + " in the search bar in  HCM Sync Profiles screen in PM");
		} catch (Exception e) {
		ScreenshotHandler.captureFailureScreenshot("search_for_published_job_name2_in_hcm_sync_profiles_tab_in_pm", e);
		LOGGER.error("Failed to enter Organization job name in HCM Sync search - Method: search_published_job_name2_hcm_sync", e);
		e.printStackTrace();
		ExtentCucumberAdapter.addTestStepLog("Failed to enter Organization job name text in search bar in HCM Sync Profiles screen in PM...Please investigate!!!");
		Assert.fail("Failed to enter Organization job name text in search bar in HCM Sync Profiles screen in PM...Please investigate!!!");
	}
	}
	
public void user_should_verify_published_second_job_profile_is_displayed_in_row1_in_hcm_sync_profiles_tab_in_pm() {
	try {
		PerformanceUtils.waitForPageReady(driver, 2);
	wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner1));
		String job2NameText = wait.until(ExpectedConditions.visibilityOf(HCMJobProfilesJobinRow1)).getText();
		Assert.assertEquals(PO04_VerifyJobMappingPageComponents.orgJobNameInRow2.get(), job2NameText.split("-", 2)[0].trim());
	LOGGER.info("Published Second Job with name : " + job2NameText.split("-", 2)[0].trim() + "is displayed in HCM Sync Profiles screen in PM as expected in Row1");
		ExtentCucumberAdapter.addTestStepLog("Published Second Job with name : " + job2NameText.split("-", 2)[0].trim() + "is displayed in HCM Sync Profiles screen in PM as expected in Row1");
		} catch (Exception e) {
		ScreenshotHandler.captureFailureScreenshot("user_should_verify_published_second_job_profile_is_displayed_in_row1_in_hcm_sync_profiles_tab_in_pm", e);
		LOGGER.error("Issue in verifying published second job profile Row1 HCM search - Method: verify_published_second_job_profile_row1_hcm_search", e);
		e.printStackTrace();
		ExtentCucumberAdapter.addTestStepLog("Issue in verifying published second job profile in Row1 in HCM Sync Profiles screen in PM...Please Investigate!!!");
		Assert.fail("Issue in verifying published second job profile in Row1 in HCM Sync Profiles screen in PM...Please Investigate!!!");
	}
	}

}
