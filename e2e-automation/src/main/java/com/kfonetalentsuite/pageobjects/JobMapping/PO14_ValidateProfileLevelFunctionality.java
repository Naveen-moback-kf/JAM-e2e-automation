package com.kfonetalentsuite.pageobjects.JobMapping;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.ScreenshotHandler;
import com.kfonetalentsuite.utils.JobMapping.Utilities;
import com.kfonetalentsuite.webdriverManager.DriverManager;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

public class PO14_ValidateProfileLevelFunctionality {
	WebDriver driver = DriverManager.getDriver();	

	protected static final Logger LOGGER = (Logger) LogManager.getLogger();
	PO14_ValidateProfileLevelFunctionality validateProfileLevelFunctionality;
	
	// THREAD-SAFE: Each thread gets its own isolated state for parallel execution
	public static ThreadLocal<String> changedlevelvalue = ThreadLocal.withInitial(() -> null);
	

	public PO14_ValidateProfileLevelFunctionality() throws IOException {
		// super();
		PageFactory.initElements(driver, this);
	}

	WebDriverWait wait = DriverManager.getWait();
	Utilities utils = new Utilities();
	JavascriptExecutor js = (JavascriptExecutor) driver;
	
	//XPATHS
	@FindBy(xpath = "//div[@data-testid='loader']//img")
	@CacheLookup
	WebElement pageLoadSpinner2;
	
	@FindBy(xpath = "//select[contains(@id,'profileLevel') or @id='profile-level']")
	@CacheLookup
	public WebElement profileLevelDropdown;
	
	@FindBy(xpath = "//h2[@id='summary-modal']//p")
	@CacheLookup
	public WebElement profileHeader;
	
	@FindBy(xpath = "//div[@class='shadow']//div[contains(@id,'card-title')]")
	@CacheLookup
	WebElement JCpageProfile1Title;
	
	//METHODs
	public void change_profile_level() {
		if(wait.until(ExpectedConditions.visibilityOf(profileLevelDropdown)).isEnabled()) {
			js.executeScript("window.scrollTo(document.body.scrollHeight, 0)");
			try {
				wait.until(ExpectedConditions.elementToBeClickable(profileLevelDropdown)).click();
				PerformanceUtils.waitForUIStability(driver, 2);
				Select dropdown = new Select(profileLevelDropdown);
				List<WebElement> allOptions = dropdown.getOptions();
				for(WebElement option : allOptions){
					String lastlevelvalue = option.getText();
					changedlevelvalue.set(lastlevelvalue);
			        }
				dropdown.selectByVisibleText(changedlevelvalue.get());
				wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
				PerformanceUtils.waitForPageReady(driver, 4);
				LOGGER.info("Successfully Changed Profile Level to : " + changedlevelvalue.get());
				ExtentCucumberAdapter.addTestStepLog("Successfully Changed Profile Level to : " + changedlevelvalue.get());
				PerformanceUtils.waitForPageReady(driver, 4);
			} catch (Exception e) {
				ScreenshotHandler.captureFailureScreenshot("change_profile_level", e);
				LOGGER.error("Issue in Changing Profile Level - Method: change_profile_level", e);
				e.printStackTrace();
				ExtentCucumberAdapter.addTestStepLog("Issue in Changing Profile Level...Please Investigate!!!");
				Assert.fail("Issue in Changing Profile Level...Please Investigate!!!");
			}
			
			try {
				try {
					wait.until(ExpectedConditions.elementToBeClickable(profileLevelDropdown)).click();
				} catch (Exception e) {
					try {
						js.executeScript("arguments[0].click();", profileLevelDropdown);
					} catch (Exception s) {
						utils.jsClick(driver, profileLevelDropdown);
					}
				}
				LOGGER.info("Profile Level dropdown closed successfully....");
				ExtentCucumberAdapter.addTestStepLog("Profile Level dropdown closed successfully....");
				} catch (Exception e) {
					ScreenshotHandler.captureFailureScreenshot("change_profile_level_close_dropdown", e);
					LOGGER.error("Issue in clicking on Profile Level dropdown to close it - Method: change_profile_level", e);
					e.printStackTrace();
					ExtentCucumberAdapter.addTestStepLog("Issue in clicking on Profile Level dropdown to close it...Please Investigate!!!");
					Assert.fail("Issue in clicking on Profile Level dropdown to close it...Please Investigate!!!");
				}
		}
	}
	
	public void change_profile_level_in_job_comparison_page() {
		if(wait.until(ExpectedConditions.visibilityOf(profileLevelDropdown)).isEnabled()) {
			js.executeScript("window.scrollTo(document.body.scrollHeight, 0)");
			try {
				wait.until(ExpectedConditions.elementToBeClickable(profileLevelDropdown)).click();
				PerformanceUtils.waitForUIStability(driver, 2);
				Select dropdown = new Select(profileLevelDropdown);
				List<WebElement> allOptions = dropdown.getOptions();
				for(WebElement option : allOptions){
					String lastlevelvalue = option.getText();
					changedlevelvalue.set(lastlevelvalue);
			        }
				dropdown.selectByVisibleText(changedlevelvalue.get());
				wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
				PerformanceUtils.waitForPageReady(driver, 4);
				LOGGER.info("Successfully Changed Profile Level to : " + changedlevelvalue.get());
				ExtentCucumberAdapter.addTestStepLog("Successfully Changed Profile Level to : " + changedlevelvalue.get());
				PerformanceUtils.waitForPageReady(driver, 4);
			} catch (Exception e) {
				ScreenshotHandler.captureFailureScreenshot("change_profile_level_in_job_comparison_page", e);
				LOGGER.error("Issue in Changing Profile Level in Job Comparison Page - Method: change_profile_level_in_job_comparison_page", e);
				e.printStackTrace();
				ExtentCucumberAdapter.addTestStepLog("Issue in Changing Profile Level in Job Comparison Page...Please Investigate!!!");
				Assert.fail("Issue in Changing Profile Level in Job Comparison Page...Please Investigate!!!");
			}
		}
	}
	
	public void user_should_verify_profile_header_matches_with_changed_profile_level_in_job_profile_details_popup() {
		try {
		wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
		PerformanceUtils.waitForPageReady(driver, 2);
		String profileHeaderName = wait.until(ExpectedConditions.visibilityOf(profileHeader)).getText();
		Assert.assertEquals(profileHeaderName,changedlevelvalue.get());
		LOGGER.info("Profile header on the details popup : " + profileHeaderName + " matches with changed profile level : " + changedlevelvalue.get());
		ExtentCucumberAdapter.addTestStepLog("Profile header on the details popup : " + profileHeaderName + " matches with changed profile level : " + changedlevelvalue.get());
			} catch (Exception e) {
				ScreenshotHandler.captureFailureScreenshot("user_should_verify_profile_header_matches_with_changed_profile_level_in_job_profile_details_popup", e);
				LOGGER.error("Issue in verifying profile details popup header matches with changed profile level - Method: user_should_verify_profile_header_matches_with_changed_profile_level_in_job_profile_details_popup", e);
				e.printStackTrace();
				ExtentCucumberAdapter.addTestStepLog("Issue in verifying profile details popup header matches with changed profile level....Please Investigate!!!");
				Assert.fail("Issue in verifying profile details popup header matches with changed profile level....Please Investigate!!!");
			}
	}
	
	public void  user_should_verify_recommended_profile_name_matches_with_changed_profile_level() {
		try {
		wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
		PerformanceUtils.waitForPageReady(driver, 2);
		String JCpageProfile1TitleText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1Title)).getText();
		Assert.assertEquals(JCpageProfile1TitleText,changedlevelvalue.get());
		LOGGER.info("Recommended Profile Name in the Job Compare page matches with Changed Profile Level : " + changedlevelvalue.get());
		ExtentCucumberAdapter.addTestStepLog("Recommended Profile Name in the Job Compare page matches with Changed Profile Level : " + changedlevelvalue.get());
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_verify_recommended_profile_name_matches_with_changed_profile_level", e);
			LOGGER.error("Issue in verifying Recommended Profile Name in the Job Compare page matches with changed profile level - Method: user_should_verify_recommended_profile_name_matches_with_changed_profile_level", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying Recommended Profile Name in the Job Compare page matches with changed profile level....Please Investigate!!!");
			Assert.fail("Issue in verifying Recommended Profile Name in the Job Compare page matches with changed profile level....Please Investigate!!!");
		}
	}
	
	public void user_is_in_job_comparison_page_after_changing_profile_level() {
		PerformanceUtils.waitForPageReady(driver, 1);
		LOGGER.info("User is in Job Comparison Page after changing profile level");
		ExtentCucumberAdapter.addTestStepLog("User is in Job Comparison Page after changing profile level");
	}
}

