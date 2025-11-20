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
import com.kfonetalentsuite.utils.PageObjectHelper;
import com.kfonetalentsuite.webdriverManager.DriverManager;

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
	public WebElement publishProfileBtnInPopup;
	
	@FindBy(xpath = "//span[@aria-label='Loading']")
	public WebElement pageLoadSpinner2;

	public void user_is_on_profile_details_popup() {
		PageObjectHelper.log(LOGGER, "User is on Profile details Popup");
	}

	public void click_on_publish_profile_button_in_profile_details_popup() {
		try {
			// Wait for button to be visible and clickable
			WebElement publishButton = wait.until(ExpectedConditions.elementToBeClickable(publishProfileBtnInPopup));
			
			// Try regular click first
			try {
				publishButton.click();
			} catch (Exception clickException) {
				// Fallback: Use JavaScript click if regular click fails
				LOGGER.warn("Regular click failed, using JavaScript click");
				js.executeScript("arguments[0].click();", publishButton);
			}
			
			PageObjectHelper.log(LOGGER, "Clicked Publish Profile button in Profile Details popup");
			
			// Wait for loading spinner to disappear
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_publish_profile_button_in_profile_details_popup",
				"Failed to click Publish Profile button in popup", e);
		}
	}
}
