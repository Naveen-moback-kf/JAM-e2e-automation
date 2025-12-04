package com.kfonetalentsuite.pageobjects.JobMapping;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.PageObjectHelper;

public class PO24_ValidatePublishCenter_PM extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO24_ValidatePublishCenter_PM.class);

	// formatDateForDisplay() is inherited from BasePageObject
	public static int ProfilesCountInRow1;
	public static ArrayList<String> profilesCountInDefaultOrder = new ArrayList<String>();

	public PO24_ValidatePublishCenter_PM() {
		super();
	}

	private static final By PUBLISH_CENTER_BTN = By.xpath("//button[contains(@class,'publish-center')]");
	private static final By JPH_SCREEN_TITLE = By.xpath("//*[contains(text(),'Job Profile History')]");
	private static final By JPH_PROFILES_COUNT_ROW1 = By.xpath("//*/kf-page-content/div[2]/div[2]/div[1]/div[1]/span");
	private static final By JPH_ACCESSED_BY_ROW1 = By.xpath("//*/kf-page-content/div[2]/div[2]/div[1]/div[2]/span");
	private static final By JPH_ACCESSED_DATE_ROW1 = By.xpath("//*/kf-page-content/div[2]/div[2]/div[1]/div[3]/span");
	private static final By JPH_ACTION_TAKEN_ROW1 = By.xpath("//*/kf-page-content/div[2]/div[2]/div[1]/div[4]/span");
	private static final By JPH_STATUS_ROW1 = By.xpath("//*/kf-page-content/div[2]/div[2]/div[1]/div[5]/span");
	private static final By JPH_HEADER1 = By.xpath("//*/div/kf-page-content/div[2]/div[1]/div[1]");
	private static final By JPH_HEADER2 = By.xpath("//*/div/kf-page-content/div[2]/div[1]/div[2]");
	private static final By JPH_HEADER3 = By.xpath("//*/div/kf-page-content/div[2]/div[1]/div[3]");
	private static final By JPH_HEADER4 = By.xpath("//*/div/kf-page-content/div[2]/div[1]/div[4]");
	private static final By JPH_HEADER5 = By.xpath("//*/div/kf-page-content/div[2]/div[1]/div[5]");
	private static final By PROFILES_DOWNLOADED_TITLE = By.xpath("//*[contains(text(),'Profiles Downloaded')]");
	private static final By PROFILES_DOWNLOADED_HEADER = By.xpath("//*[contains(@class,'header-details')]");
	private static final By CLOSE_BTN = By.xpath("//*[contains(text(),'Close')]//..");
	private static final By PD_HEADER1 = By.xpath("//*/div[2]/div/div[2]/div[1]/div[1]");
	private static final By PD_HEADER2 = By.xpath("//*/div[2]/div/div[2]/div[1]/div[2]");
	private static final By PD_HEADER3 = By.xpath("//*/div[2]/div/div[2]/div[1]/div[3]");
	private static final By PD_HEADER4 = By.xpath("//*/div[2]/div/div[2]/div[1]/div[4]");
	private static final By PROFILES_EXPORTED_TITLE = By.xpath("//*[contains(text(),'Profiles Exported')]");
	private static final By SHOWING_JOB_RESULTS = By.xpath("//*[contains(text(),'Showing')]");

	public void user_is_in_hcm_sync_profiles_screen_after_syncing_profiles() {
		PageObjectHelper.log(LOGGER, "User is in HCM Sync Profiles screen after syncing profiles");
	}

	public void click_on_publish_center_button() {
		try {
			scrollToTop();
			waitForSpinners();
			WebElement publishCenterBtn = waitForClickable(PUBLISH_CENTER_BTN);
			Assert.assertTrue(publishCenterBtn.isEnabled());
			tryClickWithStrategies(publishCenterBtn);
			PageObjectHelper.log(LOGGER,
					"Clicked on Publish Center button in My Organization's Job Profiles screen in PM");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_publish_center_button",
					"Issue in clicking on Publish Center button in My Organization's Job Profiles screen in PM", e);
		}
	}

	public void verify_user_navigated_to_job_profile_history_screen_succcessfully() {
		try {
			waitForSpinners();
			PerformanceUtils.waitForPageReady(driver, 2);
			WebElement screenTitle = waitForElement(JPH_SCREEN_TITLE);
			Assert.assertTrue(screenTitle.isDisplayed());
			PageObjectHelper.log(LOGGER, "User navigated to " + screenTitle.getText() + " screen successfully....");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_user_navigated_to_job_profile_history_screen_succcessfully",
					"Issue in navigating to Job Profile History screen on click of Publish Center button", e);
		}
	}

	public void verify_recently_downloaded_job_profiles_history_is_in_top_row() {
		try {
			String profilesCountTextinRow1 = getElementText(JPH_PROFILES_COUNT_ROW1);
			Assert.assertEquals(Integer.toString(PO22_ValidateHCMSyncProfilesScreen_PM.profilesCount.get()),
					profilesCountTextinRow1);
			PageObjectHelper.log(LOGGER,
					"Recently downloaded Job Profiles History is displaying in Top Row in Publish Center as expected....");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_recently_downloaded_job_profiles_history_is_in_top_row",
					"Issue in Verifying Recently downloaded Job Profiles History is displaying in Top Row in Publish Center",
					e);
		}
	}

	public void verify_details_of_the_recently_downloaded_job_profiles_in_job_profile_history_screen() {
		try {
			String todayDate = formatDateForDisplay();
			Assert.assertEquals(Integer.toString(PO22_ValidateHCMSyncProfilesScreen_PM.profilesCount.get()),
					getElementText(JPH_PROFILES_COUNT_ROW1));
			PageObjectHelper.log(LOGGER,
					"No.of Profiles count in Publish Center matches with No.of Profiles selected for Download in HCM Sync Profiles screen in PM");
			PO22_ValidateHCMSyncProfilesScreen_PM.profilesCount.set(0);
			ProfilesCountInRow1 = Integer.parseInt(getElementText(JPH_PROFILES_COUNT_ROW1));
			Assert.assertEquals(getElementText(JPH_ACCESSED_DATE_ROW1), todayDate);
			PageObjectHelper.log(LOGGER,
					"Accessed Date of the Recently downloaded Job Profile Matches with today's date as expected");
			Assert.assertEquals(getElementText(JPH_ACTION_TAKEN_ROW1), "Downloaded");
			PageObjectHelper.log(LOGGER, "Action taken tag displaying as Downloaded for the Recently downloaded Job Profile as expected");
			PageObjectHelper.log(LOGGER,
					"Below are the details of the Recently Downloaded Job Profile in Job Profile History screen : \n "
							+ getElementText(JPH_HEADER1).replaceAll("\\s+[^\\w\\s]+$", "") + " : "
							+ getElementText(JPH_PROFILES_COUNT_ROW1) + "   "
							+ getElementText(JPH_HEADER2).replaceAll("\\s+[^\\w\\s]+$", "") + " : "
							+ getElementText(JPH_ACCESSED_BY_ROW1) + "   "
							+ getElementText(JPH_HEADER3).replaceAll("\\s+[^\\w\\s]+$", "") + " : "
							+ getElementText(JPH_ACCESSED_DATE_ROW1) + "   "
							+ getElementText(JPH_HEADER4).replaceAll("\\s+[^\\w\\s]+$", "") + " : "
							+ getElementText(JPH_ACTION_TAKEN_ROW1) + "   "
							+ getElementText(JPH_HEADER5).replaceAll("\\s+[^\\w\\s]+$", "") + " : "
							+ getElementText(JPH_STATUS_ROW1));
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"verify_details_of_the_recently_downloaded_job_profiles_in_job_profile_history_screen",
					"Issue in Verifying details of the Recently downloaded Job Profiles in Job Profile History screen in Publish Center",
					e);
		}
	}

	public void click_on_profiles_count_of_recently_downloaded_job_profiles_in_job_profile_history_screen() {
		try {
			clickElement(JPH_PROFILES_COUNT_ROW1);
			PageObjectHelper.log(LOGGER,
					"Clicked on Profiles Count of Recently downloaded job profiles in Job Profile History screen");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"click_on_profiles_count_of_recently_downloaded_job_profiles_in_job_profile_history_screen",
					"Issue in clicking on Profiles Count of Recently downloaded job profiles in Job Profile History screen",
					e);
		}
	}

	public void user_should_be_navigated_to_profiles_downloaded_screen() {
		try {
			waitForSpinners();
			PerformanceUtils.waitForPageReady(driver, 2);
			WebElement title = waitForElement(PROFILES_DOWNLOADED_TITLE);
			Assert.assertTrue(title.isDisplayed());
			PageObjectHelper.log(LOGGER,
					"User navigated to " + title.getText() + " screen successfully....");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_be_navigated_to_profiles_downloaded_screen",
					"Issue in navigating to Profiles Downloaded screen on click of Profiles count of Recently Downloaded Profile",
					e);
		}
	}

	public void verify_details_in_profiles_downloaded_screen() {
		try {
			waitForSpinners();
			PerformanceUtils.waitForPageReady(driver, 2);
			PageObjectHelper.log(LOGGER,
					"Below are the Header Details of the Recently Downloaded Job Profiles in Profiles Downloaded screen : \n "
							+ getElementText(PROFILES_DOWNLOADED_HEADER));
			PageObjectHelper.log(LOGGER,
					"Below are the Profiles Details of the Recently Downloaded Job Profiles in Profiles Downloaded screen :");
			for (int i = 1; i <= ProfilesCountInRow1; i++) {
				WebElement profilename = driver.findElement(By.xpath("//*/div/div[2]/div[2]/div[" + i + "]/div[1]/span"));
				WebElement jobCode = driver.findElement(By.xpath("//*/div/div[2]/div[2]/div[" + i + "]/div[2]/span"));
				WebElement modifiedBy = driver.findElement(By.xpath("//*/div/div[2]/div[2]/div[" + i + "]/div[3]/span"));
				WebElement lastModified = driver.findElement(By.xpath("//*/div/div[2]/div[2]/div[" + i + "]/div[4]/span"));
				PageObjectHelper.log(LOGGER,
						getElementText(PD_HEADER1) + " : " + profilename.getText() + "   " + getElementText(PD_HEADER2) + " : "
								+ jobCode.getText() + "   " + getElementText(PD_HEADER3) + " : " + modifiedBy.getText() + "   "
								+ getElementText(PD_HEADER4) + " : " + lastModified.getText());
			}
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_details_in_profiles_downloaded_screen",
					"Issue in Verifying details of the Recently downloaded Job Profiles in Profiles Downloaded screen in Publish Center",
					e);
		}
	}

	public void close_profiles_downloaded_screen() {
		try {
			WebElement closeBtn = waitForElement(CLOSE_BTN);
			Assert.assertTrue(closeBtn.isDisplayed());
			tryClickWithStrategies(closeBtn);
			PageObjectHelper.log(LOGGER, "Profiles Downloaded screen closed succesfully....");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "close_profiles_downloaded_screen",
					"Issue in clicking on Close button in Profiles Downloaded screen", e);
		}
	}

	public void select_job_profiles_in_hcm_sync_profiles_tab() {
		try {
			for (int i = 25; i >= 6; i--) {
				WebElement SP_Checkbox = driver.findElement(
						By.xpath("//tbody//tr[" + i + "]//td[1]//*//..//div//kf-checkbox//div"));
				String text = SP_Checkbox.getAttribute("class");
				if (text.contains("disable")) {
					continue;
				} else {
					WebElement profilecheckbox = driver.findElement(By.xpath("//tbody//tr[" + i + "]//div[1]//kf-checkbox"));
					scrollToElement(profilecheckbox);
					tryClickWithStrategies(profilecheckbox);
					PO22_ValidateHCMSyncProfilesScreen_PM.profilesCount
							.set(PO22_ValidateHCMSyncProfilesScreen_PM.profilesCount.get() + 1);
					PageObjectHelper.log(LOGGER,
							"Selected " + PO22_ValidateHCMSyncProfilesScreen_PM.profilesCount.get()
									+ " Job Profiles in HCM Sync Profiles screen in PM");
				}
			}
		} catch (NoSuchElementException e) {
			PageObjectHelper.handleError(LOGGER, "select_job_profiles_in_hcm_sync_profiles_tab",
					"Issue in selecting Job Profiles in My Organization Job Profiles screen in PM", e);
		}
	}

	public void verify_recently_exported_job_profiles_history_is_in_top_row() {
		try {
			String profilesCountTextinRow1 = getElementText(JPH_PROFILES_COUNT_ROW1);
			int expectedCount = PO22_ValidateHCMSyncProfilesScreen_PM.profilesCount.get();
			int actualExportedCount = Integer.parseInt(profilesCountTextinRow1.replaceAll("[^0-9]", ""));
			
			LOGGER.info("Verifying export count - Selected: {} | Actually Exported (UI): {}", 
					expectedCount, actualExportedCount);
			
			if (actualExportedCount != expectedCount) {
				int difference = expectedCount - actualExportedCount;
				LOGGER.warn("MISMATCH DETECTED: {} profiles selected but only {} exported (difference: {})", 
						expectedCount, actualExportedCount, difference);
				LOGGER.warn("This may indicate: profiles already synced, sync failures, or disabled profiles");
			}
			
			Assert.assertEquals(actualExportedCount, expectedCount,
					"Export count mismatch - Selected: " + expectedCount + ", Exported: " + actualExportedCount);
			PageObjectHelper.log(LOGGER,
					"Recently Exported Job Profiles History is displaying in Top Row in Publish Center as expected....");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_recently_exported_job_profiles_history_is_in_top_row",
					"Issue in Verifying Recently Exported Job Profiles History is displaying in Top Row in Publish Center",
					e);
		}
	}

	public void verify_details_of_the_recently_exported_job_profiles_in_job_profile_history_screen() {
		try {
			String todayDate = formatDateForDisplay();
			String profilesCountText = getElementText(JPH_PROFILES_COUNT_ROW1);

			if (PO22_ValidateHCMSyncProfilesScreen_PM.isProfilesCountComplete.get()) {
				Assert.assertEquals(Integer.toString(PO22_ValidateHCMSyncProfilesScreen_PM.profilesCount.get()),
						profilesCountText);
				PageObjectHelper.log(LOGGER,
						"No.of Profiles count in Publish Center matches with No.of Profiles selected for Export to HCM in My Organization's Job Profiles screen in PM");
				PageObjectHelper.log(LOGGER, "Expected: " + PO22_ValidateHCMSyncProfilesScreen_PM.profilesCount.get()
						+ ", Actual: " + profilesCountText);
			} else {
				PageObjectHelper.log(LOGGER, "SKIPPED: Profile count assertion (incomplete count due to max scroll limit)");
				PageObjectHelper.log(LOGGER, "Counted: " + PO22_ValidateHCMSyncProfilesScreen_PM.profilesCount.get() 
						+ " | Actual exported: " + profilesCountText + " (discrepancy expected)");
			}

			PO22_ValidateHCMSyncProfilesScreen_PM.profilesCount.set(0);
			PO22_ValidateHCMSyncProfilesScreen_PM.isProfilesCountComplete.set(true);

			ProfilesCountInRow1 = Integer.parseInt(profilesCountText);
			Assert.assertEquals(getElementText(JPH_ACCESSED_DATE_ROW1), todayDate);
			PageObjectHelper.log(LOGGER,
					"Accessed Date of the Recently Exported Job Profile Matches with today's date as expected");
			Assert.assertEquals(getElementText(JPH_ACTION_TAKEN_ROW1), "Exported to HCM");
			PageObjectHelper.log(LOGGER,
					"Action taken tag displaying as Exported to HCM for the Recently downloaded Job Profile as expected");
			PageObjectHelper.log(LOGGER,
					"Below are the details of the Recently Exported Job Profiles in Job Profile History screen : \n "
							+ getElementText(JPH_HEADER1).replaceAll("\\s+[^\\w\\s]+$", "") + " : "
							+ profilesCountText + "   "
							+ getElementText(JPH_HEADER2).replaceAll("\\s+[^\\w\\s]+$", "") + " : "
							+ getElementText(JPH_ACCESSED_BY_ROW1) + "   "
							+ getElementText(JPH_HEADER3).replaceAll("\\s+[^\\w\\s]+$", "") + " : "
							+ getElementText(JPH_ACCESSED_DATE_ROW1) + "   "
							+ getElementText(JPH_HEADER4).replaceAll("\\s+[^\\w\\s]+$", "") + " : "
							+ getElementText(JPH_ACTION_TAKEN_ROW1) + "   "
							+ getElementText(JPH_HEADER5).replaceAll("\\s+[^\\w\\s]+$", "") + " : "
							+ getElementText(JPH_STATUS_ROW1));
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"verify_details_of_the_recently_exported_job_profiles_in_job_profile_history_screen",
					"Issue in Verifying details of the Recently Exported Job Profiles in Job Profile History screen in Publish Center",
					e);
		}
	}

	public void click_on_profiles_count_of_recently_exported_job_profiles_in_job_profile_history_screen() {
		try {
			clickElement(JPH_PROFILES_COUNT_ROW1);
			PageObjectHelper.log(LOGGER,
					"Clicked on Profiles Count of Recently Exported job profiles in Job Profile History screen");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"click_on_profiles_count_of_recently_exported_job_profiles_in_job_profile_history_screen",
					"Issue in clicking on Profiles Count of Recently Exported job profiles in Job Profile History screen",
					e);
		}
	}

	public void user_should_be_navigated_to_profiles_exported_screen() {
		try {
			waitForSpinners();
			PerformanceUtils.waitForPageReady(driver, 2);
			WebElement title = waitForElement(PROFILES_EXPORTED_TITLE);
			Assert.assertTrue(title.isDisplayed());
			PageObjectHelper.log(LOGGER,
					"User navigated to " + title.getText() + " screen successfully....");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_be_navigated_to_profiles_exported_screen",
					"Issue in navigating to Profiles Exported screen on click of Profiles count of Recently Downloaded Profile",
					e);
		}
	}

	public void verify_details_in_profiles_exported_screen() {
		try {
			PageObjectHelper.log(LOGGER,
					"Below are the Header Details of the Recently Exported Job Profiles in Profiles Exported screen : \n "
							+ getElementText(PROFILES_DOWNLOADED_HEADER));
			scrollToBottom();
			PageObjectHelper.log(LOGGER,
					"Below are the Profiles Details of the Recently Exported Job Profiles in Profiles Exported screen :");
			for (int i = 1; i <= ProfilesCountInRow1; i++) {
				WebElement profilename = driver.findElement(By.xpath("//*/div/div[2]/div[2]/div[" + i + "]/div[1]/span"));
				WebElement jobCode = driver.findElement(By.xpath("//*/div/div[2]/div[2]/div[" + i + "]/div[2]/span"));
				WebElement modifiedBy = driver.findElement(By.xpath("//*/div/div[2]/div[2]/div[" + i + "]/div[3]/span"));
				WebElement lastModified = driver.findElement(By.xpath("//*/div/div[2]/div[2]/div[" + i + "]/div[4]/span"));
				PageObjectHelper.log(LOGGER,
						getElementText(PD_HEADER1) + " : " + profilename.getText() + "   " + getElementText(PD_HEADER2) + " : "
								+ jobCode.getText() + "   " + getElementText(PD_HEADER3) + " : " + modifiedBy.getText() + "   "
								+ getElementText(PD_HEADER4) + " : " + lastModified.getText());
			}
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_details_in_profiles_exported_screen",
					"Issue in Verifying details of the Recently Exported Job Profiles in Profiles Exported screen in Publish Center",
					e);
		}
	}

	public void close_profiles_exported_screen() {
		try {
			WebElement closeBtn = waitForElement(CLOSE_BTN);
			Assert.assertTrue(closeBtn.isDisplayed());
			tryClickWithStrategies(closeBtn);
			PageObjectHelper.log(LOGGER, "Profiles Exported screen closed succesfully....");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "close_profiles_exported_screen",
					"Issue in clicking on Close button in Profiles Exported screen", e);
		}
	}

	public void user_is_in_job_profile_history_screen() {
		PageObjectHelper.log(LOGGER, "User is in Job Profile History screen in Publish Center....");
	}

	public void user_should_scroll_page_down_two_times_to_view_first_thirty_job_profiles_in_job_profile_history_screen() {
		try {
			WebElement resultsCount = waitForElement(SHOWING_JOB_RESULTS);
			Assert.assertTrue(resultsCount.isDisplayed());

			scrollToBottom();
			waitForSpinners();
			PerformanceUtils.waitForPageReady(driver, 2);

			scrollToBottom();
			waitForSpinners();
			PerformanceUtils.waitForPageReady(driver, 2);

			String resultsCountText_updated = getElementText(SHOWING_JOB_RESULTS);
			PageObjectHelper.log(LOGGER, "Scrolled down till third page and now " + resultsCountText_updated
					+ " of Job Profiles as expected");

			scrollToTop();
			waitForSpinners();
			PerformanceUtils.waitForPageReady(driver, 1);
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"scroll_page_down_two_times_to_view_first_thirty_job_profiles_in_job_profile_history_screen",
					"Issue in scrolling page down two times to view first thirty job profiles in Job Profile History screen",
					e);
		}
	}

	public void user_should_verify_first_thirty_job_profiles_in_default_order_before_applying_sorting_in_job_profile_history_screen() {
		try {
			waitForSpinners();
			PerformanceUtils.waitForPageReady(driver, 2);
			
			int rowsToValidate = getAvailableJPHRowCount();
			PageObjectHelper.log(LOGGER,
					"Below are the details of the First " + rowsToValidate + " Job Profiles in Default Order before applying sorting in Job Profile History screen : ");
			
			for (int i = 1; i <= rowsToValidate; i++) {
				List<WebElement> rowCheck = driver.findElements(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + i + "]/div[1]/span"));
				if (rowCheck.isEmpty()) break;
				
				WebElement jphProfilesCount = rowCheck.get(0);
				WebElement jphAccessedBy = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + i + "]/div[2]/span"));
				WebElement jphAccessedDate = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + i + "]/div[3]/span"));
				WebElement jphActionTaken = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + i + "]/div[4]/span"));
				WebElement jphStatus = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + i + "]/div[5]/span"));
				PageObjectHelper.log(LOGGER, getElementText(JPH_HEADER1).replaceAll("\\s+[^\\w\\s]+$", "") + " : "
						+ jphProfilesCount.getText() + "   " + getElementText(JPH_HEADER2).replaceAll("\\s+[^\\w\\s]+$", "")
						+ " : " + jphAccessedBy.getText() + "   "
						+ getElementText(JPH_HEADER3).replaceAll("\\s+[^\\w\\s]+$", "") + " : " + jphAccessedDate.getText()
						+ "   " + getElementText(JPH_HEADER4).replaceAll("\\s+[^\\w\\s]+$", "") + " : "
						+ jphActionTaken.getText() + "   " + getElementText(JPH_HEADER5).replaceAll("\\s+[^\\w\\s]+$", "")
						+ " : " + jphStatus.getText());
				profilesCountInDefaultOrder.add(jphProfilesCount.getText());
			}
			PageObjectHelper.log(LOGGER,
					"Default Order of Job Profiles before applying sorting in Job Profile History screen is verified successfully");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"user_should_verify_first_thirty_job_profiles_in_default_order_before_applying_sorting_in_job_profile_history_screen",
					"Issue in Verifying default Order of first thirty Job Profiles before applying sorting in Job Profile History screen",
					e);
		}
	}

	public void sort_job_profiles_by_no_of_profiles_in_ascending_order_in_job_profile_history_screen() {
		try {
			clickElement(JPH_HEADER1);
			waitForSpinners();
			PerformanceUtils.waitForPageReady(driver, 2);
			PageObjectHelper.log(LOGGER,
					"Clicked on NO. OF PROFILES header to Sort Job Profiles by No. of Profiles in ascending order in Job Profile History screen");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"sort_job_profiles_by_no_of_profiles_in_ascending_order_in_job_profile_history_screen",
					"Issue in clicking on NO. OF PROFILES header to Sort Job Profiles by No. of Profiles in ascending order in Job Profile History screen",
					e);
		}
	}

	public void user_should_verify_first_thirty_job_profiles_sorted_by_no_of_profiles_in_ascending_order_in_job_profile_history_screen() {
		try {
			waitForSpinners();
			PerformanceUtils.waitForPageReady(driver, 2);
			
			int rowsToValidate = getAvailableJPHRowCount();
			PageObjectHelper.log(LOGGER,
					"Below are the details of the First " + rowsToValidate + " Job Profiles in Job Profile History screen after sorting by NO. OF PROIFLES in Ascending Order: ");

			ArrayList<Integer> profileCounts = new ArrayList<Integer>();

			for (int i = 1; i <= rowsToValidate; i++) {
				List<WebElement> rowCheck = driver.findElements(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + i + "]/div[1]/span"));
				if (rowCheck.isEmpty()) break;
				
				WebElement jphProfilesCount = rowCheck.get(0);
				WebElement jphAccessedBy = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + i + "]/div[2]/span"));
				WebElement jphAccessedDate = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + i + "]/div[3]/span"));
				WebElement jphActionTaken = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + i + "]/div[4]/span | //*/kf-page-content/div[2]/div[2]/div[" + i + "]/div[4]"));
				WebElement jphStatus = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + i + "]/div[5]/span"));

				int profileCount = Integer.parseInt(jphProfilesCount.getText().trim());
				profileCounts.add(profileCount);

				LOGGER.info(getElementText(JPH_HEADER1).replaceAll("\\s+[^\\w\\s]+$", "") + " : " + jphProfilesCount.getText()
						+ "   " + getElementText(JPH_HEADER2).replaceAll("\\s+[^\\w\\s]+$", "") + " : "
						+ jphAccessedBy.getText() + "   " + getElementText(JPH_HEADER3).replaceAll("\\s+[^\\w\\s]+$", "")
						+ " : " + jphAccessedDate.getText() + "   "
						+ getElementText(JPH_HEADER4).replaceAll("\\s+[^\\w\\s]+$", "") + " : " + jphActionTaken.getText()
						+ "   " + getElementText(JPH_HEADER5).replaceAll("\\s+[^\\w\\s]+$", "") + " : " + jphStatus.getText());
			}

			int sortViolations = 0;
			for (int i = 0; i < profileCounts.size() - 1; i++) {
				int current = profileCounts.get(i);
				int next = profileCounts.get(i + 1);
				if (current > next) {
					sortViolations++;
					PageObjectHelper.log(LOGGER, "SORT VIOLATION at Row " + (i + 1) + " -> Row " + (i + 2) + ": "
							+ current + " > " + next + " (NOT in Ascending Order!)");
				}
			}

			if (sortViolations > 0) {
				String errorMsg = "SORTING FAILED: Found " + sortViolations
						+ " violation(s). Data is NOT sorted in Ascending Order!";
				PageObjectHelper.log(LOGGER, errorMsg);
				Assert.fail(errorMsg + " Please check the sorting implementation!");
			} else {
				PageObjectHelper.log(LOGGER, "SORT VALIDATION PASSED: All " + profileCounts.size()
						+ " Job Profiles are correctly sorted by NO. OF PROFILES in Ascending Order");
			}
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"user_should_verify_first_thirty_job_profiles_sorted_by_no_of_profiles_in_ascending_order_in_job_profile_history_screen",
					"Issue in Verifying first thirty Job Profiles in Job Profile History screen after sorting by NO. OF PROIFLES in Ascending Order",
					e);
		}
	}

	public void user_should_refresh_job_profile_history_screen_and_verify_job_profiles_are_in_default_order() {
		try {
			refreshPage();
			waitForSpinners();
			PerformanceUtils.waitForPageReady(driver, 3);
			PageObjectHelper.log(LOGGER, "Refreshed Job Profile History screen....");
			waitForElement(JPH_PROFILES_COUNT_ROW1);
			for (int i = 1; i <= 10; i++) {
				WebElement jphProfilesCount = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + i + "]/div[1]/span"));
				String text = jphProfilesCount.getText();
				if (text.contentEquals(profilesCountInDefaultOrder.get(i - 1))) {
					continue;
				} else {
					throw new Exception("No. Of Profiles Count : " + text + " in Row " + i
							+ " DOEST NOT Match with No. Of Profiles count : " + profilesCountInDefaultOrder.get(i - 1)
							+ " after Refreshing Job Profile History screen");
				}
			}
			PageObjectHelper.log(LOGGER,
					"Job Profiles are in Default Order as expected After Refreshing the Job Profile History screen....");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"user_should_refresh_job_profile_history_screen_and_verify_job_profiles_are_in_default_order",
					"Issue in Verifying Default order of Job Profiles after Refreshing Job Profile History screen", e);
		}
	}

	public void sort_job_profiles_by_no_of_profiles_in_descending_order_in_job_profile_history_screen() {
		try {
			LOGGER.info("First click on NO. OF PROFILES header to sort ascending...");
			clickElement(JPH_HEADER1);
			waitForSpinners();
			safeSleep(2000);
			PerformanceUtils.waitForPageReady(driver, 2);
			safeSleep(1000);

			LOGGER.info("First sort completed. Now clicking second time for descending order...");
			clickElement(JPH_HEADER1);
			waitForSpinners();
			safeSleep(2000);
			PerformanceUtils.waitForPageReady(driver, 2);

			PageObjectHelper.log(LOGGER,
					"Clicked two times on NO. OF PROFILES header to Sort Job Profiles by No. of Profiles in descending order in Job Profile History screen");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"sort_job_profiles_by_no_of_profiles_in_descending_order_in_job_profile_history_screen",
					"Issue in clicking on NO. OF PROFILES header to Sort Job Profiles by No. of Profiles in descending order in Job Profile History screen",
					e);
		}
	}

	// formatDateForDisplay() removed - now using inherited method from BasePageObject

	public void user_should_verify_first_thirty_job_profiles_sorted_by_no_of_profiles_in_descending_order_in_job_profile_history_screen() {
		try {
			waitForSpinners();
			PerformanceUtils.waitForPageReady(driver, 2);
			waitForElement(JPH_PROFILES_COUNT_ROW1);
			
			int rowsToValidate = getAvailableJPHRowCount();
			LOGGER.info(
					"Below are the details of the First " + rowsToValidate + " Job Profiles in Job Profile History screen after sorting by NO. OF PROIFLES in Descending Order: ");

			ArrayList<Integer> profileCounts = new ArrayList<Integer>();

			for (int i = 1; i <= rowsToValidate; i++) {
				List<WebElement> rowCheck = driver.findElements(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + i + "]/div[1]/span"));
				if (rowCheck.isEmpty()) break;
				
				WebElement jphProfilesCount = rowCheck.get(0);
				WebElement jphAccessedBy = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + i + "]/div[2]/span"));
				WebElement jphAccessedDate = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + i + "]/div[3]/span"));
				WebElement jphActionTaken = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + i + "]/div[4]/span | //*/kf-page-content/div[2]/div[2]/div[" + i + "]/div[4]"));
				WebElement jphStatus = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + i + "]/div[5]/span"));

				int profileCount = Integer.parseInt(jphProfilesCount.getText().trim());
				profileCounts.add(profileCount);

				LOGGER.info(getElementText(JPH_HEADER1).replaceAll("\\s+[^\\w\\s]+$", "") + " : " + jphProfilesCount.getText()
						+ "   " + getElementText(JPH_HEADER2).replaceAll("\\s+[^\\w\\s]+$", "") + " : "
						+ jphAccessedBy.getText() + "   " + getElementText(JPH_HEADER3).replaceAll("\\s+[^\\w\\s]+$", "")
						+ " : " + jphAccessedDate.getText() + "   "
						+ getElementText(JPH_HEADER4).replaceAll("\\s+[^\\w\\s]+$", "") + " : " + jphActionTaken.getText()
						+ "   " + getElementText(JPH_HEADER5).replaceAll("\\s+[^\\w\\s]+$", "") + " : " + jphStatus.getText());
			}

			int sortViolations = 0;
			for (int i = 0; i < profileCounts.size() - 1; i++) {
				int current = profileCounts.get(i);
				int next = profileCounts.get(i + 1);
				if (current < next) {
					sortViolations++;
					PageObjectHelper.log(LOGGER, "SORT VIOLATION at Row " + (i + 1) + " -> Row " + (i + 2) + ": "
							+ current + " < " + next + " (NOT in Descending Order!)");
				}
			}

			if (sortViolations > 0) {
				String errorMsg = "SORTING FAILED: Found " + sortViolations
						+ " violation(s). Data is NOT sorted in Descending Order!";
				PageObjectHelper.log(LOGGER, errorMsg);
				Assert.fail(errorMsg + " Please check the sorting implementation!");
			} else {
				PageObjectHelper.log(LOGGER, "SORT VALIDATION PASSED: All " + profileCounts.size()
						+ " Job Profiles are correctly sorted by NO. OF PROFILES in Descending Order");
			}
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"user_should_verify_first_thirty_job_profiles_sorted_by_no_of_profiles_in_descending_order_in_job_profile_history_screen",
					"Issue in Verifying first thirty Job Profiles in Job Profile History screen after sorting by NO. OF PROIFLES in Descending Order",
					e);
		}
	}

	public void sort_job_profiles_by_accessed_date_in_ascending_order_in_job_profile_history_screen() {
		try {
			clickElement(JPH_HEADER3);
			waitForSpinners();
			PerformanceUtils.waitForPageReady(driver, 2);
			PageObjectHelper.log(LOGGER,
					"Clicked on ACCESSED DATE header to Sort Job Profiles by Accessed Date in ascending order in Job Profile History screen");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"sort_job_profiles_by_accessed_date_in_ascending_order_in_job_profile_history_screen",
					"Issue in clicking on ACCESSED DATE header to Sort Job Profiles by Accessed Date in ascending order in Job Profile History screen",
					e);
		}
	}

	public void user_should_verify_first_thirty_job_profiles_sorted_by_accessed_date_in_ascending_order_in_job_profile_history_screen() {
		try {
			waitForSpinners();
			PerformanceUtils.waitForPageReady(driver, 2);
			
			int rowsToValidate = getAvailableJPHRowCount();
			LOGGER.info(
					"Below are the details of the First " + rowsToValidate + " Job Profiles in Job Profile History screen after sorting by ACCESSED DATE in Ascending Order: ");

			ArrayList<LocalDate> accessedDates = new ArrayList<LocalDate>();
			java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy",
					java.util.Locale.ENGLISH);

			for (int i = 1; i <= rowsToValidate; i++) {
				// Check if row exists before accessing
				List<WebElement> rowCheck = driver.findElements(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + i + "]/div[1]/span"));
				if (rowCheck.isEmpty()) {
					LOGGER.debug("Row {} not found, stopping iteration", i);
					break;
				}
				
				WebElement jphProfilesCount = rowCheck.get(0);
				WebElement jphAccessedBy = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + i + "]/div[2]/span"));
				WebElement jphAccessedDate = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + i + "]/div[3]/span"));
				WebElement jphActionTaken = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + i + "]/div[4]/span | //*/kf-page-content/div[2]/div[2]/div[" + i + "]/div[4]"));
				WebElement jphStatus = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + i + "]/div[5]/span"));

				String dateText = jphAccessedDate.getText().trim();
				try {
					LocalDate date = LocalDate.parse(dateText, formatter);
					accessedDates.add(date);
				} catch (Exception dateParseEx) {
					LOGGER.warn("Unable to parse date: " + dateText + " at Row " + i
							+ ". Skipping date validation for this row.");
				}

				PageObjectHelper.log(LOGGER, getElementText(JPH_HEADER1).replaceAll("\\s+[^\\w\\s]+$", "") + " : "
						+ jphProfilesCount.getText() + "   " + getElementText(JPH_HEADER2).replaceAll("\\s+[^\\w\\s]+$", "")
						+ " : " + jphAccessedBy.getText() + "   "
						+ getElementText(JPH_HEADER3).replaceAll("\\s+[^\\w\\s]+$", "") + " : " + jphAccessedDate.getText()
						+ "   " + getElementText(JPH_HEADER4).replaceAll("\\s+[^\\w\\s]+$", "") + " : "
						+ jphActionTaken.getText() + "   " + getElementText(JPH_HEADER5).replaceAll("\\s+[^\\w\\s]+$", "")
						+ " : " + jphStatus.getText());
			}

			int sortViolations = 0;
			for (int i = 0; i < accessedDates.size() - 1; i++) {
				LocalDate current = accessedDates.get(i);
				LocalDate next = accessedDates.get(i + 1);
				if (current.isAfter(next)) {
					sortViolations++;
					PageObjectHelper.log(LOGGER, "SORT VIOLATION at Row " + (i + 1) + " -> Row " + (i + 2) + ": "
							+ current + " is AFTER " + next + " (NOT in Ascending Order!)");
				}
			}

			if (sortViolations > 0) {
				String errorMsg = "SORTING FAILED: Found " + sortViolations
						+ " violation(s). Data is NOT sorted by ACCESSED DATE in Ascending Order!";
				PageObjectHelper.log(LOGGER, errorMsg);
				Assert.fail(errorMsg + " Please check the sorting implementation!");
			} else {
				PageObjectHelper.log(LOGGER, "SORT VALIDATION PASSED: All " + accessedDates.size()
						+ " Job Profiles are correctly sorted by ACCESSED DATE in Ascending Order");
			}
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"user_should_verify_first_thirty_job_profiles_sorted_by_accessed_date_in_ascending_order_in_job_profile_history_screen",
					"Issue in Verifying first thirty Job Profiles in Job Profile History screen after sorting by ACCESSED DATE in Ascending Order",
					e);
		}
	}

	public void sort_job_profiles_by_accessed_date_in_descending_order_in_job_profile_history_screen() {
		try {
			PageObjectHelper.log(LOGGER, "First click on ACCESSED DATE header to sort ascending...");
			clickElement(JPH_HEADER3);
			waitForSpinners();
			safeSleep(2000);
			PerformanceUtils.waitForPageReady(driver, 2);
			safeSleep(1000);

			LOGGER.info("First sort completed. Now clicking second time for descending order...");
			clickElement(JPH_HEADER3);
			waitForSpinners();
			safeSleep(2000);
			PerformanceUtils.waitForPageReady(driver, 2);

			PageObjectHelper.log(LOGGER,
					"Clicked two times on ACCESSED DATE header to Sort Job Profiles by Accessed Date in descending order in Job Profile History screen");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"sort_job_profiles_by_accessed_date_in_descending_order_in_job_profile_history_screen",
					"Issue in clicking on ACCESSED DATE header to Sort Job Profiles by Accessed Date in descending order in Job Profile History screen",
					e);
		}
	}

	public void user_should_verify_first_thirty_job_profiles_sorted_by_accessed_date_in_descending_order_in_job_profile_history_screen() {
		try {
			waitForSpinners();
			PerformanceUtils.waitForPageReady(driver, 2);
			waitForElement(JPH_PROFILES_COUNT_ROW1);
			
			int rowsToValidate = getAvailableJPHRowCount();
			LOGGER.info(
					"Below are the details of the First " + rowsToValidate + " Job Profiles in Job Profile History screen after sorting by ACCESSED DATE in Descending Order: ");

			ArrayList<LocalDate> accessedDates = new ArrayList<LocalDate>();
			java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy",
					java.util.Locale.ENGLISH);

			for (int i = 1; i <= rowsToValidate; i++) {
				List<WebElement> rowCheck = driver.findElements(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + i + "]/div[1]/span"));
				if (rowCheck.isEmpty()) break;
				
				WebElement jphProfilesCount = rowCheck.get(0);
				WebElement jphAccessedBy = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + i + "]/div[2]/span"));
				WebElement jphAccessedDate = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + i + "]/div[3]/span"));
				WebElement jphActionTaken = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + i + "]/div[4]/span | //*/kf-page-content/div[2]/div[2]/div[" + i + "]/div[4]"));
				WebElement jphStatus = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + i + "]/div[5]/span"));

				String dateText = jphAccessedDate.getText().trim();
				try {
					LocalDate date = LocalDate.parse(dateText, formatter);
					accessedDates.add(date);
				} catch (Exception dateParseEx) {
					LOGGER.warn("Unable to parse date: " + dateText + " at Row " + i
							+ ". Skipping date validation for this row.");
				}

				PageObjectHelper.log(LOGGER, getElementText(JPH_HEADER1).replaceAll("\\s+[^\\w\\s]+$", "") + " : "
						+ jphProfilesCount.getText() + "   " + getElementText(JPH_HEADER2).replaceAll("\\s+[^\\w\\s]+$", "")
						+ " : " + jphAccessedBy.getText() + "   "
						+ getElementText(JPH_HEADER3).replaceAll("\\s+[^\\w\\s]+$", "") + " : " + jphAccessedDate.getText()
						+ "   " + getElementText(JPH_HEADER4).replaceAll("\\s+[^\\w\\s]+$", "") + " : "
						+ jphActionTaken.getText() + "   " + getElementText(JPH_HEADER5).replaceAll("\\s+[^\\w\\s]+$", "")
						+ " : " + jphStatus.getText());
			}

			int sortViolations = 0;
			for (int i = 0; i < accessedDates.size() - 1; i++) {
				LocalDate current = accessedDates.get(i);
				LocalDate next = accessedDates.get(i + 1);
				if (current.isBefore(next)) {
					sortViolations++;
					PageObjectHelper.log(LOGGER, "SORT VIOLATION at Row " + (i + 1) + " -> Row " + (i + 2) + ": "
							+ current + " is BEFORE " + next + " (NOT in Descending Order!)");
				}
			}

			if (sortViolations > 0) {
				String errorMsg = "SORTING FAILED: Found " + sortViolations
						+ " violation(s). Data is NOT sorted by ACCESSED DATE in Descending Order!";
				PageObjectHelper.log(LOGGER, errorMsg);
				Assert.fail(errorMsg + " Please check the sorting implementation!");
			} else {
				PageObjectHelper.log(LOGGER, "SORT VALIDATION PASSED: All " + accessedDates.size()
						+ " Job Profiles are correctly sorted by ACCESSED DATE in Descending Order");
			}
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"user_should_verify_first_thirty_job_profiles_sorted_by_accessed_date_in_descending_order_in_job_profile_history_screen",
					"Issue in Verifying first thirty Job Profiles in Job Profile History screen after sorting by ACCESSED DATE in Descending Order",
					e);
		}
	}

	// ==================== HELPER METHODS ====================

	/**
	 * Gets the count of available JPH rows on screen (max 30).
	 * Dynamically counts actual rows instead of assuming 30 are loaded.
	 */
	private int getAvailableJPHRowCount() {
		int actualRowCount = driver.findElements(By.xpath("//*/kf-page-content/div[2]/div[2]/div[div[1]/span]")).size();
		int rowsToValidate = Math.min(30, actualRowCount);
		LOGGER.debug("Found {} rows loaded on screen, will validate {} rows", actualRowCount, rowsToValidate);
		return rowsToValidate;
	}

}
