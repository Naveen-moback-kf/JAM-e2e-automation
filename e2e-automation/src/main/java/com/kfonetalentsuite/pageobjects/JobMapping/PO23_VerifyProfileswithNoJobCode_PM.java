package com.kfonetalentsuite.pageobjects.JobMapping;

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

import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.Utilities;
import com.kfonetalentsuite.utils.PageObjectHelper;
import com.kfonetalentsuite.webdriverManager.DriverManager;

public class PO23_VerifyProfileswithNoJobCode_PM {
	WebDriver driver = DriverManager.getDriver();

	protected static final Logger LOGGER = (Logger) LogManager.getLogger();
	PO23_VerifyProfileswithNoJobCode_PM verifyProfileswithNoJobCode_PM;
	LocalDate currentdate = LocalDate.now();
	Month currentMonth = currentdate.getMonth();
	// THREAD-SAFE: Each thread gets its own isolated state for parallel execution
	public static ThreadLocal<Integer> rowNumber = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<String> SPJobName = ThreadLocal.withInitial(() -> null);
	public static ThreadLocal<Boolean> noJobCode = ThreadLocal.withInitial(() -> false);

	public PO23_VerifyProfileswithNoJobCode_PM() throws IOException {
		PageFactory.initElements(driver, this);
	}

	WebDriverWait wait = DriverManager.getWait();
	Utilities utils = new Utilities();
	JavascriptExecutor js = (JavascriptExecutor) driver;

	// XAPTHs
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

	// METHODs
	public void user_should_search_for_success_profile_with_no_job_code_assigned() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			PerformanceUtils.waitForPageReady(driver, 2);
			String resultsCountText = wait.until(ExpectedConditions.visibilityOf(showingJobResultsCount)).getText();
			String[] resultsCountText_split = resultsCountText.split(" ");
			int totalProfiles = Integer.parseInt(resultsCountText_split[3]);

			PageObjectHelper.log(LOGGER,
					"Searching through " + totalProfiles + " profiles for Success Profile with No Job Code...");

			for (int i = 1; i <= totalProfiles; i++) {
				try {
					rowNumber.set(i);
					WebElement SP_Checkbox = driver.findElement(By.xpath("//tbody//tr["
							+ Integer.toString(rowNumber.get()) + "]//td[1]//*//..//div//kf-checkbox//div"));
					js.executeScript("arguments[0].scrollIntoView(true);", SP_Checkbox);
					String text = SP_Checkbox.getAttribute("class");
					if (text.contains("disable")) {
						PageObjectHelper.log(LOGGER,
								"Success profile with No Job Code assigned is found at row " + rowNumber.get());
						noJobCode.set(true);
						break;
					}
				} catch (Exception e) {
					// Retry logic with explicit waits
					wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
					PerformanceUtils.waitForPageReady(driver, 3);
					rowNumber.set(i);
					WebElement SP_Checkbox = driver.findElement(By.xpath("//tbody//tr["
							+ Integer.toString(rowNumber.get()) + "]//td[1]//*//..//div//kf-checkbox//div"));
					js.executeScript("arguments[0].scrollIntoView(true);", SP_Checkbox);
					String text = SP_Checkbox.getAttribute("class");
					if (text.contains("disable")) {
						PageObjectHelper.log(LOGGER, "Success profile with No Job Code assigned is found at row "
								+ rowNumber.get() + " (after retry)");
						noJobCode.set(true);
						break;
					}
				}
			}

			if (!noJobCode.get()) {
				PageObjectHelper.log(LOGGER, "No Success profile with No Job Code was found in the current results");
			}

		} catch (Exception s) {
			PageObjectHelper.handleError(LOGGER, "user_should_search_for_success_profile_with_no_job_code_assigned",
					"Issue in searching for a Success profile with No Job Code assigned in HCM Sync Profiles screen in PM",
					s);
		}
	}

	public void user_should_verify_tooltip_is_displaying_on_checkbox_of_success_profile_with_no_job_code() {
		if (noJobCode.get()) {
			try {
				// Scroll to ensure element is visible
				if (rowNumber.get() == 1) {
					js.executeScript("arguments[0].scrollIntoView(true);", downloadBtn);
				} else if (rowNumber.get() < 5) {
					WebElement SP_JobName = driver
							.findElement(By.xpath("//tbody//tr[" + Integer.toString(1) + "]//td[1]//*"));
					js.executeScript("arguments[0].scrollIntoView(true);", SP_JobName);
				} else if (rowNumber.get() > 5) {
					WebElement SP_JobName = driver.findElement(
							By.xpath("//tbody//tr[" + Integer.toString(rowNumber.get() - 5) + "]//td[1]//*"));
					js.executeScript("arguments[0].scrollIntoView(true);", SP_JobName);
				}

				// Perform hover action
				WebElement SP_Checkbox = driver.findElement(By.xpath(
						"//tbody//tr[" + Integer.toString(rowNumber.get()) + "]//td[1]//*//..//div//kf-checkbox//div"));

				try {
					// Try Actions first
					Actions action = new Actions(driver);
					action.moveToElement(SP_Checkbox).build().perform();
				} catch (Exception e) {
					// Fallback to JavaScript hover
					js.executeScript(
							"var evt = new MouseEvent('mouseover', {bubbles: true, cancelable: true, view: window}); arguments[0].dispatchEvent(evt);",
							SP_Checkbox);
				}

				// Verify tooltip is displayed
				Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(noJobCodeToolTip)).isDisplayed());
				String tipMessage = noJobCodeToolTip.getText();

				PageObjectHelper.log(LOGGER,
						"Tooltip verified on Checkbox of SP with No Job Code at row " + rowNumber.get());
				PageObjectHelper.log(LOGGER, "Tooltip Message: " + tipMessage);

			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER,
						"user_should_verify_tooltip_is_displaying_on_checkbox_of_success_profile_with_no_job_code",
						"Issue in verifying Tooltip on checkbox of Success profile with No Job Code assigned in HCM Sync Profiles screen in PM",
						e);
			}
		} else {
			PageObjectHelper.log(LOGGER, "Skipping tooltip verification - No profile with No Job Code was found");
		}
	}

	public void verify_details_of_the_success_profile_with_no_job_code_assigned_in_hcm_sync_profiles_tab() {
		if (noJobCode.get()) {
			try {
				// Scroll to ensure element is visible (headless-compatible)
				if (rowNumber.get() > 2) {
					WebElement SP_JobName = driver.findElement(
							By.xpath("//tbody//tr[" + Integer.toString(rowNumber.get() - 2) + "]//td[1]//*"));
					js.executeScript("arguments[0].scrollIntoView(true);", SP_JobName);
				} else if (rowNumber.get() == 1) {
					js.executeScript("arguments[0].scrollIntoView(true);", downloadBtn);
				}

				// Wait for page stability after scroll
				PerformanceUtils.waitForPageReady(driver, 1);

				// Extract all profile details
				WebElement SP_JobName = driver
						.findElement(By.xpath("//tbody//tr[" + Integer.toString(rowNumber.get()) + "]//td[1]//*"));
				js.executeScript("arguments[0].scrollIntoView(true);", SP_JobName);
				SPJobName.set(SP_JobName.getText());

				WebElement SP_Status = driver
						.findElement(By.xpath("//tbody//tr[" + Integer.toString(rowNumber.get()) + "]//td[2]//*"));
				WebElement SP_KFGrade = driver
						.findElement(By.xpath("//tbody//tr[" + Integer.toString(rowNumber.get()) + "]//td[3]//*"));
				WebElement SP_Level = driver
						.findElement(By.xpath("//tbody//tr[" + Integer.toString(rowNumber.get()) + "]//td[4]//*"));
				WebElement SP_Function = driver
						.findElement(By.xpath("//tbody//tr[" + Integer.toString(rowNumber.get()) + "]//td[5]//*"));
				WebElement SP_CreatedBy = driver
						.findElement(By.xpath("//tbody//tr[" + Integer.toString(rowNumber.get()) + "]//td[6]//*"));
				WebElement SP_LastModified = driver
						.findElement(By.xpath("//tbody//tr[" + Integer.toString(rowNumber.get()) + "]//td[7]//*"));
				WebElement SP_ExportStatus = driver
						.findElement(By.xpath("//tbody//tr[" + Integer.toString(rowNumber.get()) + "]//td[8]//*"));

				PageObjectHelper.log(LOGGER,
						"Below are the details of the Success Profile with No Job Code assigned in HCM Sync Profiles screen in PM (Row "
								+ rowNumber.get() + "): \n " + tableHeader1.getText().replaceAll("\\s+[^\\w\\s]+$", "")
								+ " : " + SP_JobName.getText() + "   "
								+ tableHeader2.getText().replaceAll("\\s+[^\\w\\s]+$", "") + " : " + SP_Status.getText()
								+ "   " + tableHeader3.getText().replaceAll("\\s+[^\\w\\s]+$", "") + " : "
								+ SP_KFGrade.getText() + "   "
								+ tableHeader4.getText().replaceAll("\\s+[^\\w\\s]+$", "") + " : " + SP_Level.getText()
								+ "   " + tableHeader5.getText().replaceAll("\\s+[^\\w\\s]+$", "") + " : "
								+ SP_Function.getText() + "   "
								+ tableHeader6.getText().replaceAll("\\s+[^\\w\\s]+$", "") + " : "
								+ SP_CreatedBy.getText() + "   "
								+ tableHeader7.getText().replaceAll("\\s+[^\\w\\s]+$", "") + " : "
								+ SP_LastModified.getText() + "   "
								+ tableHeader8.getText().replaceAll("\\s+[^\\w\\s]+$", "") + " : "
								+ SP_ExportStatus.getText());

			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER,
						"verify_details_of_the_success_profile_with_no_job_code_assigned_in_hcm_sync_profiles_tab",
						"Issue in Verifying details of the Success Profile with No Job Code assigned in HCM Sync Profiles screen in PM",
						e);
			}
		} else {
			PageObjectHelper.log(LOGGER,
					"Currently, Job Code has been assigned to all Success Profiles in HCM Sync Profiles screen in PM");
		}
	}
}
