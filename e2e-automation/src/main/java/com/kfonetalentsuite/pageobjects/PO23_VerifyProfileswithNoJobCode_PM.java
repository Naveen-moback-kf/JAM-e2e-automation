package com.kfonetalentsuite.pageobjects;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;

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

import com.kfonetalentsuite.utils.PerformanceUtils;
import com.kfonetalentsuite.utils.ScreenshotHandler;
import com.kfonetalentsuite.utils.Utilities;
import com.kfonetalentsuite.webdriverManager.DriverManager;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

public class PO23_VerifyProfileswithNoJobCode_PM {
	WebDriver driver = DriverManager.getDriver();	

	protected static final Logger LOGGER = (Logger) LogManager.getLogger();
	PO23_VerifyProfileswithNoJobCode_PM verifyProfileswithNoJobCode_PM;
	LocalDate currentdate = LocalDate.now();
	Month currentMonth = currentdate.getMonth();
	public static int rowNumber;
	public static String SPJobName;
	public static boolean noJobCode=false;

	public PO23_VerifyProfileswithNoJobCode_PM() throws IOException {
		// super();
		PageFactory.initElements(driver, this);
		// TODO Auto-generated constructor stub
	}

	WebDriverWait wait = DriverManager.getWait();
	Utilities utils = new Utilities();
	JavascriptExecutor js = (JavascriptExecutor) driver;
	
	//XAPTHs
	@FindBy(xpath = "//*[@class='blocking-loader']//img")
	@CacheLookup
	WebElement pageLoadSpinner;
	
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
	
	@FindBy(xpath = "//div[contains(text(),'Showing')]")
	@CacheLookup
	public WebElement showingJobResultsCount;
	
	@FindBy(xpath = "//div[@class='p-tooltip-text']")
	@CacheLookup
	public WebElement noJobCodeToolTip;
	
	@FindBy(xpath = "//button[contains(@class,'border-button')]")
	@CacheLookup
	WebElement downloadBtn;
	
	//METHODs
public void user_should_search_for_success_profile_with_no_job_code_assigned() {
	try {
		wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
		PerformanceUtils.waitForPageReady(driver, 2);
		String resultsCountText = wait.until(ExpectedConditions.visibilityOf(showingJobResultsCount)).getText();
		String[] resultsCountText_split = resultsCountText.split(" ");
		for(int i = 1; i<=Integer.parseInt(resultsCountText_split[3]); i++) {
			try {
				rowNumber=i;
				WebElement	SP_Checkbox = driver.findElement(By.xpath("//tbody//tr[" + Integer.toString(rowNumber) + "]//td[1]//*//..//div//kf-checkbox//div"));
				js.executeScript("arguments[0].scrollIntoView(true);", SP_Checkbox);
				String text = SP_Checkbox.getAttribute("class");
				if(text.contains("disable")) {
					LOGGER.info("Success profile with No Job Code assigned is found....");
					ExtentCucumberAdapter.addTestStepLog("Success profile with No Job Code assigned is found....");
					noJobCode = true;
					break;
				}
			} catch(Exception e) {
				wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
				PerformanceUtils.waitForPageReady(driver, 3);
				rowNumber=i;
				WebElement	SP_Checkbox = driver.findElement(By.xpath("//tbody//tr[" + Integer.toString(rowNumber) + "]//td[1]//*//..//div//kf-checkbox//div"));
				js.executeScript("arguments[0].scrollIntoView(true);", SP_Checkbox);
				String text = SP_Checkbox.getAttribute("class");
				if(text.contains("disable")) {
					LOGGER.info("Success profile with No Job Code assigned is found....");
					ExtentCucumberAdapter.addTestStepLog("Success profile with No Job Code assigned is found....");
					noJobCode = true;
					break;
				}
			}
		}
	} catch(Exception s) {
		LOGGER.error("❌ Issue searching for success profile with no job code - Method: user_should_search_for_success_profile_with_no_job_code_assigned", s);
		ScreenshotHandler.captureFailureScreenshot("search_profile_no_job_code", s);
		s.printStackTrace();
		ExtentCucumberAdapter.addTestStepLog("Issue in searching for a Success profile with No Job Code assigned in HCM Sync Profiles screen in PM....Please Investigate!!!!");
		Assert.fail("Issue in searching for a Success profile with No Job Code assigned in HCM Sync Profiles screen in PM....Please Investigate!!!!");
	}
	}
	
	public void user_should_verify_tooltip_is_displaying_on_checkbox_of_success_profile_with_no_job_code() {
		if(noJobCode) {
			try {
				if (rowNumber == 1) {
					js.executeScript("arguments[0].scrollIntoView(true);", downloadBtn);
				} else if(rowNumber<5) {
					WebElement	SP_JobName = driver.findElement(By.xpath("//tbody//tr[" + Integer.toString(1) + "]//td[1]//*"));
					js.executeScript("arguments[0].scrollIntoView(true);", SP_JobName);
				} else if(rowNumber>5) {
					WebElement	SP_JobName = driver.findElement(By.xpath("//tbody//tr[" + Integer.toString(rowNumber-5) + "]//td[1]//*"));
					js.executeScript("arguments[0].scrollIntoView(true);", SP_JobName);
				}
				Actions action = new Actions(driver);
				WebElement	SP_Checkbox = driver.findElement(By.xpath("//tbody//tr[" + Integer.toString(rowNumber) + "]//td[1]//*//..//div//kf-checkbox//div"));
				action.moveToElement(SP_Checkbox).build().perform();
				Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(noJobCodeToolTip)).isDisplayed());
				String TipMessage = noJobCodeToolTip.getText();
				LOGGER.info(TipMessage);
			LOGGER.info("Tooltip on Checkbox of SP with No Job Code is verified Successfully");
			ExtentCucumberAdapter.addTestStepLog(TipMessage);
		} catch (Exception e) {
			LOGGER.error("❌ Issue verifying tooltip on checkbox - Method: user_should_verify_tooltip_is_displaying_on_checkbox_of_success_profile_with_no_job_code", e);
			ScreenshotHandler.captureFailureScreenshot("verify_tooltip_no_job_code", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying Tooltip on checkbox of Success profile with No Job Code assigned in HCM Sync Profiles screen in PM....Please Investigate!!!!");
			Assert.fail("Issue in verifying Tooltip on checkbox of Success profile with No Job Code assigned in HCM Sync Profiles screen in PM....Please Investigate!!!!");
		}
			
		}
	}
	
	public void verify_details_of_the_success_profile_with_no_job_code_assigned_in_hcm_sync_profiles_tab() {
		if(noJobCode) {
			try {
				if(rowNumber>2) {
					WebElement	SP_JobName = driver.findElement(By.xpath("//tbody//tr[" + Integer.toString(rowNumber-2) + "]//td[1]//*"));
					js.executeScript("arguments[0].scrollIntoView(true);", SP_JobName);
				} else if (rowNumber == 1) {
					js.executeScript("arguments[0].scrollIntoView(true);", downloadBtn);
				}
				WebElement	SP_JobName = driver.findElement(By.xpath("//tbody//tr[" + Integer.toString(rowNumber) + "]//td[1]//*"));
				js.executeScript("arguments[0].scrollIntoView(true);", SP_JobName);
				SPJobName = SP_JobName.getText();
				WebElement	SP_Status = driver.findElement(By.xpath("//tbody//tr[" + Integer.toString(rowNumber) + "]//td[2]//*"));
				WebElement	SP_KFGrade = driver.findElement(By.xpath("//tbody//tr[" + Integer.toString(rowNumber) + "]//td[3]//*"));
				WebElement	SP_Level = driver.findElement(By.xpath("//tbody//tr[" + Integer.toString(rowNumber) + "]//td[4]//*"));
				WebElement	SP_Function = driver.findElement(By.xpath("//tbody//tr[" + Integer.toString(rowNumber) + "]//td[5]//*"));
				WebElement	SP_CreatedBy = driver.findElement(By.xpath("//tbody//tr[" + Integer.toString(rowNumber) + "]//td[6]//*"));
				WebElement	SP_LastModified = driver.findElement(By.xpath("//tbody//tr[" + Integer.toString(rowNumber) + "]//td[7]//*"));
				WebElement	SP_ExportStatus = driver.findElement(By.xpath("//tbody//tr[" + Integer.toString(rowNumber) + "]//td[8]//*"));
				LOGGER.info("Below are the details of the Success Profile with No Job Code assigned in HCM Sync Profiles screen in PM : \n "
						+ tableHeader1.getText().split(" ▼")[0] + " : " + SP_JobName.getText() + "   "
						+ tableHeader2.getText().split(" ▼")[0] + " : " + SP_Status.getText() + "   "
						+ tableHeader3.getText().split(" ▼")[0] + " : " + SP_KFGrade.getText() + "   "
						+ tableHeader4.getText().split(" ▼")[0] + " : " + SP_Level.getText() + "   "
						+ tableHeader5.getText().split(" ▼")[0] + " : " + SP_Function.getText() + "  "
						+ tableHeader6.getText().split(" ▼")[0] + " : " + SP_CreatedBy.getText() + "   "
						+ tableHeader7.getText().split(" ▼")[0] + " : " + SP_LastModified.getText() + "  "
						+ tableHeader8.getText().split(" ▼")[0] + " : " + SP_ExportStatus.getText() + "   ");
				ExtentCucumberAdapter.addTestStepLog("Below are the details of the Success Profile with No Job Code assigned in HCM Sync Profiles screen in PM : \n "
						+ tableHeader1.getText().split(" ▼")[0] + " : " + SP_JobName.getText() + "   "
						+ tableHeader2.getText().split(" ▼")[0] + " : " + SP_Status.getText() + "   "
						+ tableHeader3.getText().split(" ▼")[0] + " : " + SP_KFGrade.getText() + "   "
						+ tableHeader4.getText().split(" ▼")[0] + " : " + SP_Level.getText() + "   "
						+ tableHeader5.getText().split(" ▼")[0] + " : " + SP_Function.getText() + "  "
						+ tableHeader6.getText().split(" ▼")[0] + " : " + SP_CreatedBy.getText() + "   "
					+ tableHeader7.getText().split(" ▼")[0] + " : " + SP_LastModified.getText() + "  "
					+ tableHeader8.getText().split(" ▼")[0] + " : " + SP_ExportStatus.getText() + "   ");
		} catch (Exception e) {
			LOGGER.error("❌ Issue verifying profile details - Method: verify_details_of_the_success_profile_with_no_job_code_assigned_in_hcm_sync_profiles_tab", e);
			ScreenshotHandler.captureFailureScreenshot("verify_profile_details_no_job_code", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying details of the Success Profile with No Job Code assigned in HCM Sync Profiles screen in PM....Please Investigate!!!!");
			Assert.fail("Issue in Verifying details of the Success Profile with No Job Code assigned in HCM Sync Profiles screen in PM....Please Investigate!!!!");
		}
		} else {
			LOGGER.info("Currently, Job Code has been assigned to all Success Profiles in HCM Sync Profiles screen in PM");
			ExtentCucumberAdapter.addTestStepLog("Currently, Job Code has been assigned to all Success Profiles in HCM Sync Profiles screen in PM");
		}
	}
}
