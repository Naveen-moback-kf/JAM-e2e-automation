package com.kfonetalentsuite.pageobjects.JobMapping;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.PageObjectHelper;

public class PO23_VerifyProfileswithNoJobCode_PM extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO23_VerifyProfileswithNoJobCode_PM.class);

	public static ThreadLocal<Integer> rowNumber = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<String> SPJobName = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<Boolean> noJobCode = ThreadLocal.withInitial(() -> false);

	private static final By TABLE_HEADER_1 = By.xpath("//thead//tr//div[@kf-sort-header='name']//div");
	private static final By TABLE_HEADER_2 = By.xpath("//thead//tr//div//div[text()=' Status ']");
	private static final By TABLE_HEADER_3 = By.xpath("//thead//tr//div//div[text()=' kf grade ']");
	private static final By TABLE_HEADER_4 = By.xpath("//thead//tr//div//div[text()=' Level ']");
	private static final By TABLE_HEADER_5 = By.xpath("//thead//tr//div//div[text()=' Function ']");
	private static final By TABLE_HEADER_6 = By.xpath("//thead//tr//div//div[text()=' Created By ']");
	private static final By TABLE_HEADER_7 = By.xpath("//thead//tr//div//div[text()=' Last Modified ']");
	private static final By TABLE_HEADER_8 = By.xpath("//thead//tr//div//div[text()=' Export status ']");
	private static final By SHOWING_JOB_RESULTS_COUNT = By.xpath("//div[contains(text(),'Showing')]");
	private static final By NO_JOB_CODE_TOOLTIP = By.xpath("//div[@class='p-tooltip-text']");
	private static final By DOWNLOAD_BTN = By.xpath("//button[contains(@class,'border-button')]");

	public PO23_VerifyProfileswithNoJobCode_PM() {
		super();
	}

	public void user_should_search_for_success_profile_with_no_job_code_assigned() {
		try {
			waitForSpinners();
			PerformanceUtils.waitForPageReady(driver, 2);
			String resultsCountText = getElementText(SHOWING_JOB_RESULTS_COUNT);
			String[] resultsCountText_split = resultsCountText.split(" ");
			int totalProfiles = Integer.parseInt(resultsCountText_split[3]);

			PageObjectHelper.log(LOGGER, "Searching through " + totalProfiles + " profiles for Success Profile with No Job Code...");

			for (int i = 1; i <= totalProfiles; i++) {
				try {
					rowNumber.set(i);
					WebElement SP_Checkbox = driver.findElement(By.xpath("//tbody//tr[" + rowNumber.get() + "]//td[1]//*//..//div//kf-checkbox//div"));
					scrollToElement(SP_Checkbox);
					String text = SP_Checkbox.getAttribute("class");
					if (text.contains("disable")) {
						PageObjectHelper.log(LOGGER, "Success profile with No Job Code assigned is found at row " + rowNumber.get());
						noJobCode.set(true);
						break;
					}
				} catch (Exception e) {
					waitForSpinners();
					PerformanceUtils.waitForPageReady(driver, 3);
					rowNumber.set(i);
					WebElement SP_Checkbox = driver.findElement(By.xpath("//tbody//tr[" + rowNumber.get() + "]//td[1]//*//..//div//kf-checkbox//div"));
					scrollToElement(SP_Checkbox);
					String text = SP_Checkbox.getAttribute("class");
					if (text.contains("disable")) {
						PageObjectHelper.log(LOGGER, "Success profile with No Job Code assigned is found at row " + rowNumber.get() + " (after retry)");
						noJobCode.set(true);
						break;
					}
				}
			}

			if (!noJobCode.get()) {
				PageObjectHelper.log(LOGGER, "No Success profile with No Job Code was found in the current results");
			}

		} catch (Exception s) {
			PageObjectHelper.handleError(LOGGER, "user_should_search_for_success_profile_with_no_job_code_assigned", "Issue in searching for a Success profile with No Job Code assigned in HCM Sync Profiles screen in PM", s);
		}
	}

	public void user_should_verify_tooltip_is_displaying_on_checkbox_of_success_profile_with_no_job_code() {
		if (noJobCode.get()) {
			try {
				if (rowNumber.get() == 1) {
					scrollToElement(driver.findElement(DOWNLOAD_BTN));
				} else if (rowNumber.get() < 5) {
					WebElement SP_JobName = driver.findElement(By.xpath("//tbody//tr[1]//td[1]//*"));
					scrollToElement(SP_JobName);
				} else if (rowNumber.get() > 5) {
					WebElement SP_JobName = driver.findElement(By.xpath("//tbody//tr[" + (rowNumber.get() - 5) + "]//td[1]//*"));
					scrollToElement(SP_JobName);
				}

				WebElement SP_Checkbox = driver.findElement(By.xpath("//tbody//tr[" + rowNumber.get() + "]//td[1]//*//..//div//kf-checkbox//div"));

				try {
					Actions action = new Actions(driver);
					action.moveToElement(SP_Checkbox).build().perform();
				} catch (Exception e) {
					js.executeScript("var evt = new MouseEvent('mouseover', {bubbles: true, cancelable: true, view: window}); arguments[0].dispatchEvent(evt);", SP_Checkbox);
				}

				Assert.assertTrue(waitForElement(NO_JOB_CODE_TOOLTIP).isDisplayed());
				String tipMessage = getElementText(NO_JOB_CODE_TOOLTIP);

				PageObjectHelper.log(LOGGER, "Tooltip verified on Checkbox of SP with No Job Code at row " + rowNumber.get());
				PageObjectHelper.log(LOGGER, "Tooltip Message: " + tipMessage);

			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER, "user_should_verify_tooltip_is_displaying_on_checkbox_of_success_profile_with_no_job_code", "Issue in verifying Tooltip on checkbox of Success profile with No Job Code assigned in HCM Sync Profiles screen in PM", e);
			}
		} else {
			PageObjectHelper.log(LOGGER, "Skipping tooltip verification - No profile with No Job Code was found");
		}
	}

	public void verify_details_of_the_success_profile_with_no_job_code_assigned_in_hcm_sync_profiles_tab() {
		if (noJobCode.get()) {
			try {
				if (rowNumber.get() > 2) {
					WebElement SP_JobName = driver.findElement(By.xpath("//tbody//tr[" + (rowNumber.get() - 2) + "]//td[1]//*"));
					scrollToElement(SP_JobName);
				} else if (rowNumber.get() == 1) {
					scrollToElement(driver.findElement(DOWNLOAD_BTN));
				}

				PerformanceUtils.waitForPageReady(driver, 1);

				WebElement SP_JobName = driver.findElement(By.xpath("//tbody//tr[" + rowNumber.get() + "]//td[1]//*"));
				scrollToElement(SP_JobName);
				SPJobName.set(SP_JobName.getText());

				WebElement SP_Status = driver.findElement(By.xpath("//tbody//tr[" + rowNumber.get() + "]//td[2]//*"));
				WebElement SP_KFGrade = driver.findElement(By.xpath("//tbody//tr[" + rowNumber.get() + "]//td[3]//*"));
				WebElement SP_Level = driver.findElement(By.xpath("//tbody//tr[" + rowNumber.get() + "]//td[4]//*"));
				WebElement SP_Function = driver.findElement(By.xpath("//tbody//tr[" + rowNumber.get() + "]//td[5]//*"));
				WebElement SP_CreatedBy = driver.findElement(By.xpath("//tbody//tr[" + rowNumber.get() + "]//td[6]//*"));
				WebElement SP_LastModified = driver.findElement(By.xpath("//tbody//tr[" + rowNumber.get() + "]//td[7]//*"));
				WebElement SP_ExportStatus = driver.findElement(By.xpath("//tbody//tr[" + rowNumber.get() + "]//td[8]//*"));

				PageObjectHelper.log(LOGGER, "Below are the details of the Success Profile with No Job Code assigned in HCM Sync Profiles screen in PM (Row " + rowNumber.get() + "): \n "
						+ getElementText(TABLE_HEADER_1).replaceAll("\\s+[^\\w\\s]+$", "") + " : " + SP_JobName.getText() + "   "
						+ getElementText(TABLE_HEADER_2).replaceAll("\\s+[^\\w\\s]+$", "") + " : " + SP_Status.getText() + "   "
						+ getElementText(TABLE_HEADER_3).replaceAll("\\s+[^\\w\\s]+$", "") + " : " + SP_KFGrade.getText() + "   "
						+ getElementText(TABLE_HEADER_4).replaceAll("\\s+[^\\w\\s]+$", "") + " : " + SP_Level.getText() + "   "
						+ getElementText(TABLE_HEADER_5).replaceAll("\\s+[^\\w\\s]+$", "") + " : " + SP_Function.getText() + "   "
						+ getElementText(TABLE_HEADER_6).replaceAll("\\s+[^\\w\\s]+$", "") + " : " + SP_CreatedBy.getText() + "   "
						+ getElementText(TABLE_HEADER_7).replaceAll("\\s+[^\\w\\s]+$", "") + " : " + SP_LastModified.getText() + "   "
						+ getElementText(TABLE_HEADER_8).replaceAll("\\s+[^\\w\\s]+$", "") + " : " + SP_ExportStatus.getText());

			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER, "verify_details_of_the_success_profile_with_no_job_code_assigned_in_hcm_sync_profiles_tab", "Issue in Verifying details of the Success Profile with No Job Code assigned in HCM Sync Profiles screen in PM", e);
			}
		} else {
			PageObjectHelper.log(LOGGER, "Currently, Job Code has been assigned to all Success Profiles in HCM Sync Profiles screen in PM");
		}
	}
}
