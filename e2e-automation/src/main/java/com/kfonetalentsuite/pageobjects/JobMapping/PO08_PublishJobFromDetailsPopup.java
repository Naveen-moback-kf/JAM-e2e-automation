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
	
	// THREAD-SAFE: Each thread gets its own isolated state for parallel execution
	public static ThreadLocal<String> job1OrgName = ThreadLocal.withInitial(() -> null);

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
			// Enhanced: Log element status before attempting click
			LOGGER.info("Attempting to locate Publish Profile button in popup - xpath: //button[@id='publish-job-profile']");
			
			// Wait for button to be visible and clickable
			WebElement publishButton = wait.until(ExpectedConditions.elementToBeClickable(publishProfileBtnInPopup));
			LOGGER.info("Publish Profile button found and clickable");
			
			// Try regular click first
			try {
				publishButton.click();
				LOGGER.info("Clicked on Publish Profile button using regular click");
			} catch (Exception clickException) {
				// Fallback: Use JavaScript click if regular click fails
				LOGGER.warn("Regular click failed, attempting JavaScript click. Error: " + clickException.getMessage());
				js.executeScript("arguments[0].click();", publishButton);
				LOGGER.info("Clicked on Publish Profile button using JavaScript click");
			}
			
			ExtentCucumberAdapter.addTestStepLog("Clicked on Publish Profile button in Profile Details popup");
			
			// Wait for loading spinner to disappear
			LOGGER.info("Waiting for loading spinner to disappear...");
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			LOGGER.info("Loading spinner disappeared - operation completed successfully");
			
		} catch (Exception e) {
			// Enhanced error details
			String errorDetails = String.format(
				"Failed to click Publish Profile button in popup. " +
				"Element ID: 'publish-job-profile', " +
				"Button displayed: %s, " +
				"Button enabled: %s, " +
				"Error type: %s, " +
				"Error message: %s",
				publishProfileBtnInPopup.isDisplayed(),
				publishProfileBtnInPopup.isEnabled(),
				e.getClass().getSimpleName(),
				e.getMessage()
			);
			
			ScreenshotHandler.captureFailureScreenshot("click_on_publish_profile_button_in_profile_details_popup", e);
			LOGGER.error(errorDetails, e);
			e.printStackTrace();
			Assert.fail(errorDetails);
			ExtentCucumberAdapter.addTestStepLog("FAILURE: " + errorDetails);
		}
	}
}
