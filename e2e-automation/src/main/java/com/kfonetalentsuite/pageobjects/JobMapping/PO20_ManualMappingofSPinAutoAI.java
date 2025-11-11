package com.kfonetalentsuite.pageobjects.JobMapping;

import java.io.IOException;
import java.util.NoSuchElementException;
import com.kfonetalentsuite.utils.JobMapping.ScreenshotHandler;

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
import com.kfonetalentsuite.webdriverManager.DriverManager;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

public class PO20_ManualMappingofSPinAutoAI {
	WebDriver driver = DriverManager.getDriver();	

	protected static final Logger LOGGER = (Logger) LogManager.getLogger();
	PO20_ManualMappingofSPinAutoAI manualMappingofSPinAutoAI;
	
	public static int row;
	public static String orgJobName;
	public static String orgJobCode;

	public PO20_ManualMappingofSPinAutoAI() throws IOException {
		// super();
		PageFactory.initElements(driver, this);
		// TODO Auto-generated constructor stub
	}

	WebDriverWait wait = DriverManager.getWait();
	Utilities utils = new Utilities();
	JavascriptExecutor js = (JavascriptExecutor) driver;
	
	//XPATHs
	@FindBy(xpath = "//div[@data-testid='loader']//img")
	@CacheLookup
	WebElement pageLoadSpinner2;
	
	@FindBy(xpath = "//tbody//tr[1]//button[contains(text(),'Find')]")
	@CacheLookup
	WebElement findMatchBtn;
	
	@FindBy(xpath = "//tbody//tr[2]//button[contains(text(),'different profile')]")
	@CacheLookup
	WebElement searchDifferntSPBtn;
	
	//METHODs
public void verify_profile_with_no_bic_mapping_is_displaying_on_top_after_sorting() throws InterruptedException {
	try {
		wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
		PerformanceUtils.waitForPageReady(driver, 3);
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//tbody//tr[2]//button[not(contains(@id,'view'))] | //tbody//tr[1]//button[contains(text(),'Find')]")))).isDisplayed();
		WebElement	button = driver.findElement(By.xpath("//tbody//tr[2]//button[not(contains(@id,'view'))] | //tbody//tr[1]//button[contains(text(),'Find')]"));
		js.executeScript("arguments[0].scrollIntoView(true);", button);
		String text = button.getText();
		if(text.contains("Publish")) {
			PO21_MapDifferentSPtoProfileInAutoAI.mapSP = false;
			LOGGER.info("Currently, All Profiles in Job Mapping are Mapped with BIC Profiles");
			ExtentCucumberAdapter.addTestStepLog("Currently, All Profiles in Job Mapping are Mapped with BIC Profiles");
			driver.navigate().refresh();
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForPageReady(driver, 3);
		} else {
			PO21_MapDifferentSPtoProfileInAutoAI.mapSP = true;
			LOGGER.info("Job profile with No BIC Profile Mapping is found");
			ExtentCucumberAdapter.addTestStepLog("Job profile with No BIC Profile Mapping is found");	
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForPageReady(driver, 2);
			WebElement	jobName = driver.findElement(By.xpath("//tbody//tr[1]//td[2]//div[contains(text(),'(')]"));
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(jobName)).isDisplayed());
			String jobname1 = wait.until(ExpectedConditions.visibilityOf(jobName)).getText(); 
			PO21_MapDifferentSPtoProfileInAutoAI.orgJobName = jobname1.split("-", 2)[0].trim();
			PO21_MapDifferentSPtoProfileInAutoAI.orgJobCode = jobname1.split("-", 2)[1].trim().substring(1, jobname1.split("-", 2)[1].length()-2);
			ExtentCucumberAdapter.addTestStepLog("Organization Job name / Job Code of Profile with No BIC Profile Mapped : " + PO21_MapDifferentSPtoProfileInAutoAI.orgJobName +  "/"  + PO21_MapDifferentSPtoProfileInAutoAI.orgJobCode);
			LOGGER.info("Organization Job name / Job Code of Profile with with No BIC Profile Mapped : " + PO21_MapDifferentSPtoProfileInAutoAI.orgJobName + "/" + PO21_MapDifferentSPtoProfileInAutoAI.orgJobCode);
			WebElement	jobGrade = driver.findElement(By.xpath("//tbody//tr[1]//td[3]//div[1]"));
			PO21_MapDifferentSPtoProfileInAutoAI.orgJobGrade = jobGrade.getText();
			if(PO21_MapDifferentSPtoProfileInAutoAI.orgJobGrade.contentEquals("-") || PO21_MapDifferentSPtoProfileInAutoAI.orgJobGrade.isEmpty() || PO21_MapDifferentSPtoProfileInAutoAI.orgJobGrade.isBlank()) {
				PO21_MapDifferentSPtoProfileInAutoAI.orgJobGrade = "NULL";	
			}
			WebElement	jobDepartment = driver.findElement(By.xpath("//tbody//tr[1]//td[4]//div[1]"));
			PO21_MapDifferentSPtoProfileInAutoAI.orgJobDepartment = jobDepartment.getText();
			if(PO21_MapDifferentSPtoProfileInAutoAI.orgJobDepartment.contentEquals("-") || PO21_MapDifferentSPtoProfileInAutoAI.orgJobDepartment.isEmpty() || PO21_MapDifferentSPtoProfileInAutoAI.orgJobDepartment.isBlank()) {
				PO21_MapDifferentSPtoProfileInAutoAI.orgJobDepartment = "NULL";	
			}
			WebElement	jobFunction = driver.findElement(By.xpath("//tbody//tr[2]//div//span[2]"));
			PO21_MapDifferentSPtoProfileInAutoAI.orgJobFunction = jobFunction.getText();
			if(PO21_MapDifferentSPtoProfileInAutoAI.orgJobFunction.contentEquals("-") || PO21_MapDifferentSPtoProfileInAutoAI.orgJobFunction.isEmpty() || PO21_MapDifferentSPtoProfileInAutoAI.orgJobFunction.isBlank()) {
				PO21_MapDifferentSPtoProfileInAutoAI.orgJobFunction = "NULL";	
		}
	}
} catch(NoSuchElementException e) {
		LOGGER.error("No profile with No BIC mapping found - Method: verify_profile_with_no_bic_mapping_is_displaying_on_top_after_sorting", e);
		ScreenshotHandler.captureFailureScreenshot("verify_profile_with_no_bic_mapping", e);
		PO21_MapDifferentSPtoProfileInAutoAI.mapSP = false;
		LOGGER.info("Currently, All Profiles in Job Mapping are Mapped with BIC Profiles");
		ExtentCucumberAdapter.addTestStepLog("Currently, All Profiles in Job Mapping are Mapped with BIC Profiles");
	}
}
	
	public void click_on_find_match_button() {
		if(PO21_MapDifferentSPtoProfileInAutoAI.mapSP) {
			try {
				try {
					wait.until(ExpectedConditions.elementToBeClickable(findMatchBtn)).click();
				} catch (Exception e) {
					try {
						js.executeScript("arguments[0].click();", findMatchBtn);
					} catch (Exception s) {
						utils.jsClick(driver, findMatchBtn);
					}
				}
				ExtentCucumberAdapter.addTestStepLog("Clicked on Find Match button of Organization Job : "+ PO21_MapDifferentSPtoProfileInAutoAI.orgJobName);
			LOGGER.info("Clicked on Find Match button of Organization Job : "+ PO21_MapDifferentSPtoProfileInAutoAI.orgJobName);
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
		} catch (Exception e) {
			LOGGER.error("Issue clicking Find Match button - Method: click_on_find_match_button", e);
			ScreenshotHandler.handleTestFailure("click_find_match_button", e, 
				"Issue in clicking on Find Match button of Organization job name "+ PO21_MapDifferentSPtoProfileInAutoAI.orgJobName + "...Please Investigate!!!");
		}
		}
	}
	
	public void user_should_verify_search_a_different_profile_button_is_displaying_on_manually_mapped_success_profile() {
		if(PO21_MapDifferentSPtoProfileInAutoAI.manualMapping) {
		try {
			PerformanceUtils.waitForPageReady(driver, 2);
		wait.until(ExpectedConditions.elementToBeClickable(searchDifferntSPBtn)).isDisplayed();
		LOGGER.info("Search a Different Profile is Displaying on matched success profile which is on the Top of the Profiles List");
		ExtentCucumberAdapter.addTestStepLog("Search a Different Profile is Displaying on matched success profile which is on the Top of the Profiles List");
	} catch (Exception e) {
		LOGGER.error("Search Different Profile button not found - Method: user_should_verify_search_a_different_profile_button_is_displaying_on_manually_mapped_success_profile", e);
		ScreenshotHandler.handleTestFailure("verify_search_different_profile_button_displaying", e, 
			"Search a different profile is NOT displaying on Manually Mapped success profile which is on the Top of the Profiles List");
	}
	}
}
	
	public void click_on_manually_mapped_profile_name_of_job_profile_on_top_of_profiles_list() {
		if(PO21_MapDifferentSPtoProfileInAutoAI.manualMapping) { 
			try {
				wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
				WebElement	linkedMappedProfile = driver.findElement(By.xpath("//div[@id='kf-job-container']//div//table//tbody//tr[1]//td[1]//div"));
				String MappedProfileNameText = wait.until(ExpectedConditions.elementToBeClickable(linkedMappedProfile)).getText();
				PO21_MapDifferentSPtoProfileInAutoAI.mappedSuccessPrflName = MappedProfileNameText;
				try {
					wait.until(ExpectedConditions.elementToBeClickable(linkedMappedProfile)).click();
				} catch (Exception e) {
					try {
						js.executeScript("arguments[0].click();", linkedMappedProfile);
					} catch (Exception s) {
						utils.jsClick(driver, linkedMappedProfile);
					}
				}
				ExtentCucumberAdapter.addTestStepLog("Clicked on Manually Mapped Profile with name " + MappedProfileNameText + " of Organization Job " + PO21_MapDifferentSPtoProfileInAutoAI.orgJobName + " which is on the Top of the Profiles List");
			LOGGER.info("Clicked on Manually Mapped Profile with name " + MappedProfileNameText +" of Organization Job " + PO21_MapDifferentSPtoProfileInAutoAI.orgJobName + " which is on the Top of the Profiles List");
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
		} catch (Exception e) {
			LOGGER.error("Issue clicking manually mapped profile - Method: click_on_manually_mapped_profile_name_of_job_profile_on_top_of_profiles_list", e);
			ScreenshotHandler.handleTestFailure("click_manually_mapped_profile_name_top_list", e, 
				"Issue in clicking Manually Mapped Profile linked with job name "+ PO21_MapDifferentSPtoProfileInAutoAI.orgJobName + "...Please Investigate!!!");
		}
		}
	}
		
}
