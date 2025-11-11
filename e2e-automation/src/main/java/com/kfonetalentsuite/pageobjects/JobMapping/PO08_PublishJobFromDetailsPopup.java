package com.kfonetalentsuite.pageobjects.JobMapping;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;

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

import com.kfonetalentsuite.utils.JobMapping.ScreenshotHandler;
import com.kfonetalentsuite.utils.JobMapping.Utilities;
import com.kfonetalentsuite.webdriverManager.DriverManager;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

public class PO08_PublishJobFromDetailsPopup {
	WebDriver driver = DriverManager.getDriver();	

	protected static final Logger LOGGER = (Logger) LogManager.getLogger();
	PO08_PublishJobFromDetailsPopup publishJobFromDetailsPopup;
	
	public static String job1OrgName;

	public PO08_PublishJobFromDetailsPopup() throws IOException {
		// super();
		PageFactory.initElements(driver, this);
	}

	WebDriverWait wait = DriverManager.getWait();
	Utilities utils = new Utilities();
	JavascriptExecutor js = (JavascriptExecutor) driver;
	LocalDate currentdate = LocalDate.now();
	Month currentMonth = currentdate.getMonth();
	
	// WebElements for Profile Details Popup functionality
	@FindBy(xpath = "//button[@id='publish-job-profile']")
	@CacheLookup
	public WebElement publishProfileBtnInPopup;
	
	@FindBy(xpath = "//span[@aria-label='Loading']")
	@CacheLookup
	public WebElement pageLoadSpinner2;

	public void user_is_on_profile_details_popup() {
		LOGGER.info("User is on Profile details Popup");
		ExtentCucumberAdapter.addTestStepLog("User is on Profile details Popup");    
	}

	public void click_on_publish_profile_button_in_profile_details_popup() {
		try {
			wait.until(ExpectedConditions.visibilityOf(publishProfileBtnInPopup)).click();
		LOGGER.info("Clicked on Publish Profile button in Profile Details popup");
		ExtentCucumberAdapter.addTestStepLog("Clicked on Publish Profile button in Profile Details popup");
		wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
		} catch (Exception e) {
		ScreenshotHandler.captureFailureScreenshot("click_on_publish_profile_button_in_profile_details_popup", e);
		LOGGER.error("Failed to click Publish Profile button in popup - Method: click_on_publish_profile_button_in_profile_details_popup", e);
		e.printStackTrace();
		Assert.fail("Issue in clicking Publish Profile button in profile details popup...Please investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in clicking Publish Profile button in profile details popup...Please investigate!!!");
	}
	}
}
