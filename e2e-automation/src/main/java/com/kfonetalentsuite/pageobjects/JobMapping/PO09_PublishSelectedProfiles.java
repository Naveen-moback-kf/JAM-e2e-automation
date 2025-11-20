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
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.Utilities;
import com.kfonetalentsuite.utils.PageObjectHelper;
import com.kfonetalentsuite.webdriverManager.DriverManager;

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

	// XPATHS
	@FindBy(xpath = "//*[@class='blocking-loader']//img")
	WebElement pageLoadSpinner1;

	@FindBy(xpath = "//div[@data-testid='loader']//img")
	WebElement pageLoadSpinner2;

	@FindBy(xpath = "//input[contains(@id,'search-job-title')]")
	public WebElement searchBar;

	@FindBy(xpath = "//tbody//tr[1]//td[2]//div[contains(text(),'(')]")
	public WebElement jobNameinRow1;

	@FindBy(xpath = "//tbody//tr[4]//td[2]//div[contains(text(),'(')]")
	public WebElement jobNameinRow2;

	@FindBy(xpath = "//tbody//tr[2]//button[text()='Published'][1]")
	public WebElement job1PublishedBtn;

	@FindBy(xpath = "//tbody//tr[5]//button[text()='Published'][1]")
	public WebElement job2PublishedBtn;

	@FindBy(xpath = "//input[@type='search']")
	public WebElement HCMJobProfilesSearch;

	@FindBy(xpath = "//tbody//tr[1]//td//div//span[1]//a")
	public WebElement HCMJobProfilesJobinRow1;

	@FindBy(xpath = "//tbody//tr[1]//td[7]//span")
	public WebElement HCMJobProfilesDateinRow1;

	@FindBy(xpath = "//tbody//tr[2]//td//div//span[1]//a")
	public WebElement HCMJobProfilesJobinRow2;

	@FindBy(xpath = "//tbody//tr[2]//td[7]//span")
	public WebElement HCMJobProfilesDateinRow2;

	// Methods

	public void search_for_published_job_name1() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForPageReady(driver, 2);
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			wait.until(ExpectedConditions.visibilityOf(searchBar)).click();
			wait.until(ExpectedConditions.visibilityOf(searchBar)).clear();
			wait.until(ExpectedConditions.visibilityOf(searchBar))
					.sendKeys(PO04_VerifyJobMappingPageComponents.orgJobNameInRow1.get());
			wait.until(ExpectedConditions.visibilityOf(searchBar)).sendKeys(Keys.ENTER);
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForPageReady(driver, 2);
			PageObjectHelper.log(LOGGER, "Searched for job: "
					+ PO04_VerifyJobMappingPageComponents.orgJobNameInRow1.get() + " in View Published screen");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "search_for_published_job_name1",
					"Failed to search for first job in View Published screen", e);
		}
	}

	public void user_should_verify_published_first_job_profile_is_displayed_in_row1_in_view_published_screen() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForPageReady(driver, 2);
			String job1NameText = wait.until(ExpectedConditions.visibilityOf(jobNameinRow1)).getText();
			Assert.assertEquals(PO04_VerifyJobMappingPageComponents.orgJobNameInRow1.get(),
					job1NameText.split("-", 2)[0].trim());
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(job1PublishedBtn)).isDisplayed());
			PageObjectHelper.log(LOGGER,
					"Published Job (Org: " + job1NameText.split("-", 2)[0].trim() + ") is displayed in Row1");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"user_should_verify_published_first_job_profile_is_displayed_in_row1_in_view_published_screen",
					"Issue verifying published first job profile in Row1", e);
		}
	}

	public void search_for_published_job_name2() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForPageReady(driver, 2);
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			wait.until(ExpectedConditions.visibilityOf(searchBar)).click();
			wait.until(ExpectedConditions.visibilityOf(searchBar)).clear();
			wait.until(ExpectedConditions.visibilityOf(searchBar))
					.sendKeys(PO04_VerifyJobMappingPageComponents.orgJobNameInRow2.get());
			wait.until(ExpectedConditions.visibilityOf(searchBar)).sendKeys(Keys.ENTER);
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForPageReady(driver, 2);
			PageObjectHelper.log(LOGGER, "Searched for job: "
					+ PO04_VerifyJobMappingPageComponents.orgJobNameInRow2.get() + " in View Published screen");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "search_for_published_job_name2",
					"Failed to search for second job in View Published screen", e);
		}
	}

	public void user_should_verify_published_second_job_profile_is_displayed_in_row1_in_view_published_screen() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForPageReady(driver, 2);
			String job1NameText = wait.until(ExpectedConditions.visibilityOf(jobNameinRow1)).getText();
			Assert.assertEquals(PO04_VerifyJobMappingPageComponents.orgJobNameInRow2.get(),
					job1NameText.split("-", 2)[0].trim());
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(job1PublishedBtn)).isDisplayed());
			PageObjectHelper.log(LOGGER,
					"Published Job (Org: " + job1NameText.split("-", 2)[0].trim() + ") is displayed in Row1");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"user_should_verify_published_second_job_profile_is_displayed_in_row1_in_view_published_screen",
					"Issue verifying published second job profile in Row1", e);
		}
	}

	public void user_should_verify_date_on_published_first_job_matches_with_current_date() {
		try {
			PerformanceUtils.waitForPageReady(driver, 2);
			int currentYear = currentdate.getYear();
			int currentDay = currentdate.getDayOfMonth();
			String cd;
			String todayDate;
			if (Integer.toString(currentDay).length() == 1) {
				DecimalFormat df = new DecimalFormat("00");
				cd = df.format(currentDay);
				todayDate = currentMonth.toString().substring(0, 1)
						+ currentMonth.toString().substring(1, 3).toLowerCase() + " " + cd + ", "
						+ Integer.toString(currentYear);
			} else {
				todayDate = currentMonth.toString().substring(0, 1)
						+ currentMonth.toString().substring(1, 3).toLowerCase() + " " + Integer.toString(currentDay)
						+ ", " + Integer.toString(currentYear);
			}
			String jobPublishedDate = wait.until(ExpectedConditions.visibilityOf(HCMJobProfilesDateinRow1)).getText();
			Assert.assertEquals(jobPublishedDate, todayDate);
			PageObjectHelper.log(LOGGER, "Current date verified on Published First Job: "
					+ PO04_VerifyJobMappingPageComponents.orgJobNameInRow1.get());
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"user_should_verify_date_on_published_first_job_matches_with_current_date",
					"Issue verifying date on published first job", e);
		}
	}

	// REMOVED: Unused method - corresponding step is commented out in feature file

	public void user_should_verify_date_on_published_second_job_matches_with_current_date() {
		try {
			int currentYear = currentdate.getYear();
			int currentDay = currentdate.getDayOfMonth();
			String cd;
			String todayDate;
			if (Integer.toString(currentDay).length() == 1) {
				DecimalFormat df = new DecimalFormat("00");
				cd = df.format(currentDay);
				todayDate = currentMonth.toString().substring(0, 1)
						+ currentMonth.toString().substring(1, 3).toLowerCase() + " " + cd + ", "
						+ Integer.toString(currentYear);
			} else {
				todayDate = currentMonth.toString().substring(0, 1)
						+ currentMonth.toString().substring(1, 3).toLowerCase() + " " + Integer.toString(currentDay)
						+ ", " + Integer.toString(currentYear);
			}
			String jobPublishedDate = wait.until(ExpectedConditions.visibilityOf(HCMJobProfilesDateinRow2)).getText();
			Assert.assertEquals(jobPublishedDate, todayDate);
			PageObjectHelper.log(LOGGER, "Current date verified on Published Second Job: "
					+ PO04_VerifyJobMappingPageComponents.orgJobNameInRow2.get());
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"user_should_verify_date_on_published_second_job_matches_with_current_date",
					"Issue verifying date on published second job", e);
		}
	}

	public void search_for_published_job_name2_in_hcm_sync_profiles_tab_in_pm() {
		Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(HCMJobProfilesSearch)).isDisplayed());
		try {
			wait.until(ExpectedConditions.elementToBeClickable(HCMJobProfilesSearch)).click();
			wait.until(ExpectedConditions.visibilityOf(HCMJobProfilesSearch)).clear();
			wait.until(ExpectedConditions.visibilityOf(HCMJobProfilesSearch))
					.sendKeys(PO04_VerifyJobMappingPageComponents.orgJobNameInRow2.get());
			wait.until(ExpectedConditions.visibilityOf(HCMJobProfilesSearch)).sendKeys(Keys.ENTER);
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner1));
			PerformanceUtils.waitForPageReady(driver, 2);
			PageObjectHelper.log(LOGGER, "Searched for job: "
					+ PO04_VerifyJobMappingPageComponents.orgJobNameInRow2.get() + " in HCM Sync Profiles");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "search_for_published_job_name2_in_hcm_sync_profiles_tab_in_pm",
					"Failed to search for job in HCM Sync Profiles", e);
		}
	}

	public void user_should_verify_published_second_job_profile_is_displayed_in_row1_in_hcm_sync_profiles_tab_in_pm() {
		try {
			PerformanceUtils.waitForPageReady(driver, 2);
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner1));
			String job2NameText = wait.until(ExpectedConditions.visibilityOf(HCMJobProfilesJobinRow1)).getText();
			Assert.assertEquals(PO04_VerifyJobMappingPageComponents.orgJobNameInRow2.get(),
					job2NameText.split("-", 2)[0].trim());
			PageObjectHelper.log(LOGGER, "Published Second Job (Org: " + job2NameText.split("-", 2)[0].trim()
					+ ") is displayed in HCM Sync Profiles Row1");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"user_should_verify_published_second_job_profile_is_displayed_in_row1_in_hcm_sync_profiles_tab_in_pm",
					"Issue verifying published second job profile in HCM Sync Profiles", e);
		}
	}

}
