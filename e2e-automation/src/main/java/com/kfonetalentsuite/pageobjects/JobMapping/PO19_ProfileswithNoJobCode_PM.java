package com.kfonetalentsuite.pageobjects.JobMapping;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import com.kfonetalentsuite.utils.JobMapping.PageObjectHelper;
import com.kfonetalentsuite.utils.JobMapping.ScreenshotHandler;

public class PO19_ProfileswithNoJobCode_PM extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO19_ProfileswithNoJobCode_PM.class);

	public static ThreadLocal<Integer> rowNumber = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<String> SPJobName = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<Boolean> noJobCode = ThreadLocal.withInitial(() -> false);

	private static final By NO_JOB_CODE_TOOLTIP = By.xpath("//div[@class='p-tooltip-text']");

	public PO19_ProfileswithNoJobCode_PM() {
		super();
	}

	public void user_should_search_for_success_profile_with_no_job_code_assigned() {
		try {
			waitForSpinners();
			PageObjectHelper.waitForPageReady(driver, 2);
			String resultsCountText = getElementText(Locators.HCMSyncProfiles.SHOWING_RESULTS_COUNT);
			String[] resultsCountText_split = resultsCountText.split(" ");
			int totalProfiles = Integer.parseInt(resultsCountText_split[3]);

			LOGGER.info("Searching through " + totalProfiles + " profiles for Success Profile with No Job Code...");

			for (int i = 1; i <= totalProfiles; i++) {
				try {
					rowNumber.set(i);
					WebElement SP_Checkbox = driver.findElement(By.xpath("//tbody//tr[" + rowNumber.get() + "]//td[1]//*//..//div//kf-checkbox//div"));
					scrollToElement(SP_Checkbox);
					String text = SP_Checkbox.getAttribute("class");
					if (text.contains("disable")) {
						LOGGER.info("Success profile with No Job Code assigned is found at row " + rowNumber.get());
						noJobCode.set(true);
						break;
					}
				} catch (Exception e) {
					waitForSpinners();
					PageObjectHelper.waitForPageReady(driver, 3);
					rowNumber.set(i);
					WebElement SP_Checkbox = driver.findElement(By.xpath("//tbody//tr[" + rowNumber.get() + "]//td[1]//*//..//div//kf-checkbox//div"));
					scrollToElement(SP_Checkbox);
					String text = SP_Checkbox.getAttribute("class");
					if (text.contains("disable")) {
						LOGGER.info("Success profile with No Job Code assigned is found at row " + rowNumber.get() + " (after retry)");
						noJobCode.set(true);
						break;
					}
				}
			}

			if (!noJobCode.get()) {
				LOGGER.info("No Success profile with No Job Code was found in the current results");
			}

		} catch (Exception s) {
			PageObjectHelper.handleError(LOGGER, "user_should_search_for_success_profile_with_no_job_code_assigned", "Issue in searching for a Success profile with No Job Code assigned in HCM Sync Profiles screen in PM", s);
		}
	}

	public void user_should_verify_tooltip_is_displaying_on_checkbox_of_success_profile_with_no_job_code() {
		if (noJobCode.get()) {
			try {
				// Scroll to position the element properly based on row number
				if (rowNumber.get() == 1) {
					// Try to scroll to download button, but don't fail if it's not there
					try {
						WebElement SyncBtn = waitForElement(Locators.HCMSyncProfiles.SYNC_WITH_HCM_BTN, 5);
						scrollToElement(SyncBtn);
					} catch (Exception e) {
						LOGGER.debug("Download button not found, scrolling to top instead");
						scrollToTop();
					}
				} else if (rowNumber.get() < 5) {
					WebElement SP_JobName = waitForElement(By.xpath("//tbody//tr[1]//td[1]//*"), 5);
					scrollToElement(SP_JobName);
				} else if (rowNumber.get() > 5) {
					WebElement SP_JobName = waitForElement(By.xpath("//tbody//tr[" + (rowNumber.get() - 5) + "]//td[1]//*"), 5);
					scrollToElement(SP_JobName);
				}

				safeSleep(500); // Allow scroll to complete
				PageObjectHelper.waitForPageReady(driver, 2);

				// Find the checkbox for the profile with no job code
				WebElement SP_Checkbox = waitForElement(
					By.xpath("//tbody//tr[" + rowNumber.get() + "]//td[1]//*//..//div//kf-checkbox//div"), 10);
				
				LOGGER.debug("Found checkbox at row {}, triggering hover for tooltip", rowNumber.get());

				// Hover over checkbox to trigger tooltip
				try {
					Actions action = new Actions(driver);
					action.moveToElement(SP_Checkbox).build().perform();
					safeSleep(800); // Wait for tooltip animation
				} catch (Exception e) {
					LOGGER.debug("Standard hover failed, using JavaScript hover");
					js.executeScript("var evt = new MouseEvent('mouseover', {bubbles: true, cancelable: true, view: window}); arguments[0].dispatchEvent(evt);", SP_Checkbox);
					safeSleep(800);
				}

				// Verify tooltip is displayed
				WebElement tooltip = waitForElement(NO_JOB_CODE_TOOLTIP, 5);
				Assert.assertTrue(tooltip.isDisplayed(), "Tooltip should be visible on hover");
				String tipMessage = getElementText(NO_JOB_CODE_TOOLTIP);

				LOGGER.info("Tooltip verified on Checkbox of SP with No Job Code at row " + rowNumber.get());
				LOGGER.info("Tooltip Message: " + tipMessage);

			} catch (Exception e) {
				ScreenshotHandler.captureFailureScreenshot("tooltip_verification_failed", e);
				PageObjectHelper.handleError(LOGGER, "user_should_verify_tooltip_is_displaying_on_checkbox_of_success_profile_with_no_job_code", 
					"Issue in verifying Tooltip on checkbox of Success profile with No Job Code assigned in HCM Sync Profiles screen in PM", e);
			}
		} else {
			LOGGER.info("Skipping tooltip verification - No profile with No Job Code was found");
		}
	}

	public void verify_details_of_the_success_profile_with_no_job_code_assigned_in_hcm_sync_profiles_tab() {
		if (noJobCode.get()) {
			try {
				if (rowNumber.get() > 2) {
					WebElement SP_JobName = driver.findElement(By.xpath("//tbody//tr[" + (rowNumber.get() - 2) + "]//td[1]//*"));
					scrollToElement(SP_JobName);
				} else if (rowNumber.get() == 1) {
					scrollToElement(driver.findElement(Locators.HCMSyncProfiles.SYNC_WITH_HCM_BTN));
				}

				PageObjectHelper.waitForPageReady(driver, 1);

				WebElement SP_JobName = driver.findElement(By.xpath("//tbody//tr[" + rowNumber.get() + "]//td[1]//*"));
				scrollToElement(SP_JobName);
				SPJobName.set(SP_JobName.getText());

				WebElement SP_Status = driver.findElement(By.xpath("//tbody//tr[" + rowNumber.get() + "]//td[2]//*"));
				WebElement SP_JobCode = driver.findElement(By.xpath("//tbody//tr[" + rowNumber.get() + "]//td[3]//*"));
				WebElement SP_KFGrade = driver.findElement(By.xpath("//tbody//tr[" + rowNumber.get() + "]//td[4]//*"));
				WebElement SP_Level = driver.findElement(By.xpath("//tbody//tr[" + rowNumber.get() + "]//td[5]//*"));
				WebElement SP_Function = driver.findElement(By.xpath("//tbody//tr[" + rowNumber.get() + "]//td[6]//*"));
				WebElement SP_CreatedBy = driver.findElement(By.xpath("//tbody//tr[" + rowNumber.get() + "]//td[7]//*"));
				WebElement SP_LastModified = driver.findElement(By.xpath("//tbody//tr[" + rowNumber.get() + "]//td[8]//*"));
				WebElement SP_ExportStatus = driver.findElement(By.xpath("//tbody//tr[" + rowNumber.get() + "]//td[9]//*"));

			LOGGER.info("Below are the details of the Success Profile with No Job Code assigned in HCM Sync Profiles screen in PM (Row " + rowNumber.get() + "): \n "
					+ getElementText(Locators.HCMSyncProfiles.TABLE_HEADER_NAME).replaceAll("\\s+[^\\w\\s]+$", "") + " : " + SP_JobName.getText() + "   "
					+ getElementText(Locators.HCMSyncProfiles.TABLE_HEADER_STATUS).replaceAll("\\s+[^\\w\\s]+$", "") + " : " + SP_Status.getText() + "   "
					+ getElementText(Locators.HCMSyncProfiles.TABLE_HEADER_KF_GRADE).replaceAll("\\s+[^\\w\\s]+$", "") + " : " + SP_JobCode.getText() + "   "
					+ getElementText(Locators.HCMSyncProfiles.TABLE_HEADER_JOB_CODE).replaceAll("\\s+[^\\w\\s]+$", "") + " : " + SP_KFGrade.getText() + "   "
					+ getElementText(Locators.HCMSyncProfiles.TABLE_HEADER_LEVEL).replaceAll("\\s+[^\\w\\s]+$", "") + " : " + SP_Level.getText() + "   "
					+ getElementText(Locators.HCMSyncProfiles.TABLE_HEADER_FUNCTION).replaceAll("\\s+[^\\w\\s]+$", "") + " : " + SP_Function.getText() + "   "
					+ getElementText(Locators.HCMSyncProfiles.TABLE_HEADER_CREATED_BY).replaceAll("\\s+[^\\w\\s]+$", "") + " : " + SP_CreatedBy.getText() + "   "
					+ getElementText(Locators.HCMSyncProfiles.TABLE_HEADER_LAST_MODIFIED).replaceAll("\\s+[^\\w\\s]+$", "") + " : " + SP_LastModified.getText() + "   "
					+ getElementText(Locators.HCMSyncProfiles.TABLE_HEADER_EXPORT_STATUS).replaceAll("\\s+[^\\w\\s]+$", "") + " : " + SP_ExportStatus.getText());

			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER, "verify_details_of_the_success_profile_with_no_job_code_assigned_in_hcm_sync_profiles_tab", "Issue in Verifying details of the Success Profile with No Job Code assigned in HCM Sync Profiles screen in PM", e);
			}
		} else {
			LOGGER.info("Currently, Job Code has been assigned to all Success Profiles in HCM Sync Profiles screen in PM");
		}
	}
}
