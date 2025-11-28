package com.kfonetalentsuite.pageobjects.JobMapping;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.Utilities;
import com.kfonetalentsuite.utils.PageObjectHelper;
import com.kfonetalentsuite.webdriverManager.DriverManager;

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

	// XPATHS
	@FindBy(xpath = "//div[@data-testid='loader']//img")
	WebElement pageLoadSpinner2;

	@FindBy(xpath = "//select[contains(@id,'profileLevel') or @id='profile-level']")
	public WebElement profileLevelDropdown;

	@FindBy(xpath = "//h2[@id='summary-modal']//p")
	public WebElement profileHeader;

	@FindBy(xpath = "//div[@class='shadow']//div[contains(@id,'card-title')]")
	WebElement JCpageProfile1Title;

	// METHODs
	public void change_profile_level() {
		if (wait.until(ExpectedConditions.visibilityOf(profileLevelDropdown)).isEnabled()) {
			js.executeScript("window.scrollTo(document.body.scrollHeight, 0)");
			try {
				wait.until(ExpectedConditions.elementToBeClickable(profileLevelDropdown)).click();
				PerformanceUtils.waitForUIStability(driver, 2);
				Select dropdown = new Select(profileLevelDropdown);
				List<WebElement> allOptions = dropdown.getOptions();
				for (WebElement option : allOptions) {
					String lastlevelvalue = option.getText();
					changedlevelvalue.set(lastlevelvalue);
				}
				dropdown.selectByVisibleText(changedlevelvalue.get());
				PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
				PerformanceUtils.waitForPageReady(driver, 4);
				PageObjectHelper.log(LOGGER, "Successfully changed Profile Level to: " + changedlevelvalue.get());
				PerformanceUtils.waitForPageReady(driver, 4);
			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER, "change_profile_level", "Issue changing Profile Level", e);
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
				PageObjectHelper.log(LOGGER, "Profile Level dropdown closed successfully");
			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER, "change_profile_level",
						"Issue clicking Profile Level dropdown to close it", e);
			}
		}
	}

	public void change_profile_level_in_job_comparison_page() {
		if (wait.until(ExpectedConditions.visibilityOf(profileLevelDropdown)).isEnabled()) {
			js.executeScript("window.scrollTo(document.body.scrollHeight, 0)");
			try {
				wait.until(ExpectedConditions.elementToBeClickable(profileLevelDropdown)).click();
				PerformanceUtils.waitForUIStability(driver, 2);
				Select dropdown = new Select(profileLevelDropdown);
				List<WebElement> allOptions = dropdown.getOptions();
				for (WebElement option : allOptions) {
					String lastlevelvalue = option.getText();
					changedlevelvalue.set(lastlevelvalue);
				}
				dropdown.selectByVisibleText(changedlevelvalue.get());
				PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
				PerformanceUtils.waitForPageReady(driver, 4);
				PageObjectHelper.log(LOGGER, "Successfully changed Profile Level to: " + changedlevelvalue.get());
				PerformanceUtils.waitForPageReady(driver, 4);
			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER, "change_profile_level_in_job_comparison_page",
						"Issue changing Profile Level in Job Comparison Page", e);
			}
		}
	}

	public void user_should_verify_profile_header_matches_with_changed_profile_level_in_job_profile_details_popup() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForPageReady(driver, 2);
			String profileHeaderName = wait.until(ExpectedConditions.visibilityOf(profileHeader)).getText();
			Assert.assertEquals(profileHeaderName, changedlevelvalue.get());
			PageObjectHelper.log(LOGGER, "Profile header on details popup: " + profileHeaderName
					+ " matches with changed profile level: " + changedlevelvalue.get());
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"user_should_verify_profile_header_matches_with_changed_profile_level_in_job_profile_details_popup",
					"Issue verifying profile details popup header matches with changed profile level", e);
		}
	}

	public void user_should_verify_recommended_profile_name_matches_with_changed_profile_level() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForPageReady(driver, 2);
			String JCpageProfile1TitleText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1Title)).getText();
			Assert.assertEquals(JCpageProfile1TitleText, changedlevelvalue.get());
			PageObjectHelper.log(LOGGER,
					"Recommended Profile Name in Job Compare page matches with Changed Profile Level: "
							+ changedlevelvalue.get());
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"user_should_verify_recommended_profile_name_matches_with_changed_profile_level",
					"Issue verifying Recommended Profile Name matches with changed profile level", e);
		}
	}

	public void user_is_in_job_comparison_page_after_changing_profile_level() {
		PerformanceUtils.waitForPageReady(driver, 1);
		PageObjectHelper.log(LOGGER, "User is in Job Comparison Page after changing profile level");
	}
}
