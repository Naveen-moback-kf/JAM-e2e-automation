package com.kfonetalentsuite.pageobjects.JobMapping;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.kfonetalentsuite.utils.HeadlessCompatibleActions;
import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.Utilities;
import com.kfonetalentsuite.utils.PageObjectHelper;
import com.kfonetalentsuite.webdriverManager.DriverManager;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

public class PO24_ValidatePublishCenter_PM {
	WebDriver driver = DriverManager.getDriver();

	protected static final Logger LOGGER = (Logger) LogManager.getLogger();
	PO24_ValidatePublishCenter_PM validatePublishCenter_PM;
	LocalDate currentdate = LocalDate.now();
	Month currentMonth = currentdate.getMonth();
	public static int ProfilesCountInRow1;

	public static ArrayList<String> profilesCountInDefaultOrder = new ArrayList<String>();

	public PO24_ValidatePublishCenter_PM() throws IOException {
		PageFactory.initElements(driver, this);
		this.headlessActions = new HeadlessCompatibleActions(driver);
	}

	WebDriverWait wait = DriverManager.getWait();
	Utilities utils = new Utilities();
	JavascriptExecutor js = (JavascriptExecutor) driver;
	HeadlessCompatibleActions headlessActions; // Utility for headless-compatible actions

	// XAPTHs
	@FindBy(xpath = "//*[@class='blocking-loader']//img")
	@CacheLookup
	WebElement pageLoadSpinner;

	@FindBy(xpath = "//button[contains(@class,'publish-center')]")
	@CacheLookup
	WebElement publishCenterBtn;

	@FindBy(xpath = "//*[contains(text(),'Job Profile History')]")
	WebElement jphScreenTitle;

	@FindBy(xpath = "//*/kf-page-content/div[2]/div[2]/div[1]/div[1]/span")
	@CacheLookup
	WebElement jphProfilesCountinRow1;

	@FindBy(xpath = "//*/kf-page-content/div[2]/div[2]/div[1]/div[2]/span")
	@CacheLookup
	WebElement jphAccessedByinRow1;

	@FindBy(xpath = "//*/kf-page-content/div[2]/div[2]/div[1]/div[3]/span")
	@CacheLookup
	WebElement jphAccessedDateinRow1;

	@FindBy(xpath = "//*/kf-page-content/div[2]/div[2]/div[1]/div[4]/span")
	@CacheLookup
	WebElement jphActionTakeninRow1;

	@FindBy(xpath = "//*/kf-page-content/div[2]/div[2]/div[1]/div[5]/span")
	@CacheLookup
	WebElement jphStatusinRow1;

	@FindBy(xpath = "//*/div/kf-page-content/div[2]/div[1]/div[1]")
	@CacheLookup
	WebElement jphHeader1;

	@FindBy(xpath = "//*/div/kf-page-content/div[2]/div[1]/div[2]")
	@CacheLookup
	WebElement jphHeader2;

	@FindBy(xpath = "//*/div/kf-page-content/div[2]/div[1]/div[3]")
	@CacheLookup
	WebElement jphHeader3;

	@FindBy(xpath = "//*/div/kf-page-content/div[2]/div[1]/div[4]")
	@CacheLookup
	WebElement jphHeader4;

	@FindBy(xpath = "//*/div/kf-page-content/div[2]/div[1]/div[5]")
	@CacheLookup
	WebElement jphHeader5;

	@FindBy(xpath = "//*[contains(text(),'Profiles Downloaded')]")
	@CacheLookup
	WebElement profilesDownloadedScreenTitle;

	@FindBy(xpath = "//*[contains(@class,'header-details')]")
	@CacheLookup
	WebElement profilesDownloadedScreenHeaderDetails;

	@FindBy(xpath = "//*[contains(text(),'Close')]//..")
	@CacheLookup
	WebElement closeBtn;

	@FindBy(xpath = "//*/div[2]/div/div[2]/div[1]/div[1]")
	@CacheLookup
	WebElement pdHeader1;

	@FindBy(xpath = "//*/div[2]/div/div[2]/div[1]/div[2]")
	@CacheLookup
	WebElement pdHeader2;

	@FindBy(xpath = "//*/div[2]/div/div[2]/div[1]/div[3]")
	@CacheLookup
	WebElement pdHeader3;

	@FindBy(xpath = "//*/div[2]/div/div[2]/div[1]/div[4]")
	@CacheLookup
	WebElement pdHeader4;

	@FindBy(xpath = "//*[contains(text(),'Profiles Exported')]")
	@CacheLookup
	WebElement profilesExportedScreenTitle;

	@FindBy(xpath = "//*[contains(text(),'Showing')]")
	@CacheLookup
	public WebElement showingJobResultsCount;

	// METHODs
	public void user_is_in_hcm_sync_profiles_screen_after_syncing_profiles() {
		PageObjectHelper.log(LOGGER, "User is in HCM Sync Profiles screen after syncing profiles");
	}

	public void click_on_publish_center_button() {
		try {
			js.executeScript("window.scrollTo(0, 0);"); // Scroll to top (headless-compatible)
			// PERFORMANCE FIX: Use optimized spinner wait with shorter timeout
			PerformanceUtils.waitForSpinnersToDisappear(driver, 5);
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(publishCenterBtn)).isEnabled());
			try {
				wait.until(ExpectedConditions.elementToBeClickable(publishCenterBtn)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", publishCenterBtn);
				} catch (Exception s) {
					utils.jsClick(driver, publishCenterBtn);
				}
			}
			PageObjectHelper.log(LOGGER,
					"Clicked on Publish Center button in My Organization's Job Profiles screen in PM");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_publish_center_button",
					"Issue in clicking on Publish Center button in My Organization's Job Profiles screen in PM", e);
		}
	}

	public void verify_user_navigated_to_job_profile_history_screen_succcessfully() {
		try {
			// PERFORMANCE FIX: Use shorter wait for spinners after modal close
			try {
				WebDriverWait shortWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(3));
				shortWait.until(ExpectedConditions
						.invisibilityOfElementLocated(By.xpath("//*[@class='blocking-loader']//img")));
			} catch (TimeoutException e) {
				// Spinner may not appear after closing modal - this is fine
				LOGGER.debug("Spinner check timed out - continuing");
			}

			PerformanceUtils.waitForPageReady(driver, 2);

			// Use visibilityOfElementLocated instead of cached element
			WebElement screenTitle = wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath("//*[contains(text(),'Job Profile History')]")));
			Assert.assertTrue(screenTitle.isDisplayed());
			PageObjectHelper.log(LOGGER, "User navigated to " + screenTitle.getText() + " screen successfully....");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_user_navigated_to_job_profile_history_screen_succcessfully",
					"Issue in navigating to Job Profile History screen on click of Publish Center button", e);
		}
	}

	public void verify_recently_downloaded_job_profiles_history_is_in_top_row() {
		try {
			String profilesCountTextinRow1 = wait.until(ExpectedConditions.visibilityOf(jphProfilesCountinRow1))
					.getText();
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
			Assert.assertEquals(Integer.toString(PO22_ValidateHCMSyncProfilesScreen_PM.profilesCount.get()),
					jphProfilesCountinRow1.getText());
			PageObjectHelper.log(LOGGER,
					"No.of Profiles count in Publish Center matches with No.of Profiles selected for Download in HCM Sync Profiles screen in PM");
			PO22_ValidateHCMSyncProfilesScreen_PM.profilesCount.set(0);
			ProfilesCountInRow1 = Integer.parseInt(jphProfilesCountinRow1.getText());
			Assert.assertEquals(jphAccessedDateinRow1.getText(), todayDate);
			PageObjectHelper.log(LOGGER,
					"Accessed Date of the Recently downloaded Job Profile Matches with today's date as expected");
			Assert.assertEquals(jphActionTakeninRow1.getText(), "Downloaded");
			PageObjectHelper.log(LOGGER, "Action taken tag displaying as " + "Downloaded"
					+ " for the Recently downloaded Job Profile as expected");
			PageObjectHelper.log(LOGGER,
					"Below are the details of the Recently Downloaded Job Profile in Job Profile History screen : \n "
							+ jphHeader1.getText().replaceAll("\\s+[^\\w\\s]+$", "") + " : "
							+ jphProfilesCountinRow1.getText() + "   "
							+ jphHeader2.getText().replaceAll("\\s+[^\\w\\s]+$", "") + " : "
							+ jphAccessedByinRow1.getText() + "   "
							+ jphHeader3.getText().replaceAll("\\s+[^\\w\\s]+$", "") + " : "
							+ jphAccessedDateinRow1.getText() + "   "
							+ jphHeader4.getText().replaceAll("\\s+[^\\w\\s]+$", "") + " : "
							+ jphActionTakeninRow1.getText() + "   "
							+ jphHeader5.getText().replaceAll("\\s+[^\\w\\s]+$", "") + " : "
							+ jphStatusinRow1.getText());

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"verify_details_of_the_recently_downloaded_job_profiles_in_job_profile_history_screen",
					"Issue in Verifying details of the Recently downloaded Job Profiles in Job Profile History screen in Publish Center",
					e);
		}
	}

	public void click_on_profiles_count_of_recently_downloaded_job_profiles_in_job_profile_history_screen() {
		try {
			try {
				wait.until(ExpectedConditions.elementToBeClickable(jphProfilesCountinRow1)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", jphProfilesCountinRow1);
				} catch (Exception s) {
					utils.jsClick(driver, jphProfilesCountinRow1);
				}
			}
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
			// PERFORMANCE FIX: Use optimized spinner wait
			PerformanceUtils.waitForSpinnersToDisappear(driver, 5);
			PerformanceUtils.waitForPageReady(driver, 2);
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(profilesDownloadedScreenTitle)).isDisplayed());
			PageObjectHelper.log(LOGGER,
					"User navigated to " + profilesDownloadedScreenTitle.getText() + " screen successfully....");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_be_navigated_to_profiles_downloaded_screen",
					"Issue in navigating to Profiles Downloaded screen on click of Profiles count of Recently Downloaded Profile",
					e);
		}
	}

	public void verify_details_in_profiles_downloaded_screen() {
		try {
			// PERFORMANCE FIX: Use optimized spinner wait
			PerformanceUtils.waitForSpinnersToDisappear(driver, 5);
			PerformanceUtils.waitForPageReady(driver, 2);
			PageObjectHelper.log(LOGGER,
					"Below are the " + "Header Details" + " of the Recently Downloaded Job Profiles in "
							+ "Profiles Downloaded" + " screen : \n "
							+ profilesDownloadedScreenHeaderDetails.getText());
			PageObjectHelper.log(LOGGER,
					"Below are the Profiles Details of the Recently Downloaded Job Profiles in Profiles Downloaded screen :");
			for (int i = 1; i <= ProfilesCountInRow1; i++) {
				WebElement profilename = driver
						.findElement(By.xpath("//*/div/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[1]/span"));
				WebElement jobCode = driver
						.findElement(By.xpath("//*/div/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[2]/span"));
				WebElement modifiedBy = driver
						.findElement(By.xpath("//*/div/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[3]/span"));
				WebElement lastModified = driver
						.findElement(By.xpath("//*/div/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[4]/span"));
				PageObjectHelper.log(LOGGER,
						pdHeader1.getText() + " : " + profilename.getText() + "   " + pdHeader2.getText() + " : "
								+ jobCode.getText() + "   " + pdHeader3.getText() + " : " + modifiedBy.getText() + "   "
								+ pdHeader4.getText() + " : " + lastModified.getText());
			}
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_details_in_profiles_downloaded_screen",
					"Issue in Verifying details of the Recently downloaded Job Profiles in Profiles Downloaded screen in Publish Center",
					e);
		}
	}

	public void close_profiles_downloaded_screen() {
		try {
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(closeBtn)).isDisplayed());
			try {
				wait.until(ExpectedConditions.elementToBeClickable(closeBtn)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", closeBtn);
				} catch (Exception s) {
					utils.jsClick(driver, closeBtn);
				}
			}
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
						By.xpath("//tbody//tr[" + Integer.toString(i) + "]//td[1]//*//..//div//kf-checkbox//div"));
				String text = SP_Checkbox.getAttribute("class");
				if (text.contains("disable")) {
					continue;
				} else {
					WebElement profilecheckbox = driver
							.findElement(By.xpath("//tbody//tr[" + Integer.toString(i) + "]//div[1]//kf-checkbox"));
					try {
						js.executeScript("arguments[0].scrollIntoView();", profilecheckbox);
						wait.until(ExpectedConditions.elementToBeClickable(profilecheckbox)).click();
						PO22_ValidateHCMSyncProfilesScreen_PM.profilesCount
								.set(PO22_ValidateHCMSyncProfilesScreen_PM.profilesCount.get() + 1);
					} catch (Exception e) {
						try {
							js.executeScript("arguments[0].click();", profilecheckbox);
						} catch (Exception s) {
							utils.jsClick(driver, profilecheckbox);
						}
						PO22_ValidateHCMSyncProfilesScreen_PM.profilesCount
								.set(PO22_ValidateHCMSyncProfilesScreen_PM.profilesCount.get() + 1);
					}
//					ValidateHCMSyncProfiles_PM.profilesCount = ValidateHCMSyncProfiles_PM.profilesCount + 1;
					PageObjectHelper.log(LOGGER,
							"Selected " + Integer.toString(PO22_ValidateHCMSyncProfilesScreen_PM.profilesCount.get())
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
			String profilesCountTextinRow1 = wait.until(ExpectedConditions.visibilityOf(jphProfilesCountinRow1))
					.getText();
			Assert.assertEquals(Integer.toString(PO22_ValidateHCMSyncProfilesScreen_PM.profilesCount.get()),
					profilesCountTextinRow1);
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

			// CONDITIONAL ASSERTION: Skip if profile count is incomplete due to max scroll
			// limit
			if (PO22_ValidateHCMSyncProfilesScreen_PM.isProfilesCountComplete.get()) {
				// Count is COMPLETE - Perform assertion
				Assert.assertEquals(Integer.toString(PO22_ValidateHCMSyncProfilesScreen_PM.profilesCount.get()),
						jphProfilesCountinRow1.getText());
				PageObjectHelper.log(LOGGER,
						"No.of Profiles count in Publish Center matches with No.of Profiles selected for Export to HCM in My Organization's Job Profiles screen in PM");
				PageObjectHelper.log(LOGGER, "Expected: " + PO22_ValidateHCMSyncProfilesScreen_PM.profilesCount.get()
						+ ", Actual: " + jphProfilesCountinRow1.getText());
			} else {
				// Count is INCOMPLETE - Skip assertion with clear logging
				LOGGER.warn(
						"SKIPPING PROFILE COUNT ASSERTION: Profile count is INCOMPLETE due to max scroll limit reached");
				LOGGER.warn("Counted profiles from scrolling: "
						+ PO22_ValidateHCMSyncProfilesScreen_PM.profilesCount.get());
				LOGGER.warn("Actual exported profiles in Publish Center: " + jphProfilesCountinRow1.getText());
				LOGGER.warn("This is NOT a test failure - it's a limitation of scrolling with large datasets");
				ExtentCucumberAdapter
						.addTestStepLog("SKIPPED: Profile count assertion (incomplete count due to max scroll limit)");
				ExtentCucumberAdapter.addTestStepLog(
						"Counted: " + PO22_ValidateHCMSyncProfilesScreen_PM.profilesCount.get() + " | Actual exported: "
								+ jphProfilesCountinRow1.getText() + " (discrepancy expected)");
			}

			// Reset profile count and flag for future test runs
			PO22_ValidateHCMSyncProfilesScreen_PM.profilesCount.set(0);
			PO22_ValidateHCMSyncProfilesScreen_PM.isProfilesCountComplete.set(true); // Reset flag to default (true) for
																						// next test

			ProfilesCountInRow1 = Integer.parseInt(jphProfilesCountinRow1.getText());
			Assert.assertEquals(jphAccessedDateinRow1.getText(), todayDate);
			PageObjectHelper.log(LOGGER,
					"Accessed Date of the Recently Exported Job Profile Matches with today's date as expected");
			Assert.assertEquals(jphActionTakeninRow1.getText(), "Exported to HCM");
			PageObjectHelper.log(LOGGER,
					"Action taken tag displaying as Exported to HCM for the Recently downloaded Job Profile as expected");
			PageObjectHelper.log(LOGGER,
					"Below are the details of the Recently Exported Job Profiles in Job Profile History screen : \n "
							+ jphHeader1.getText().replaceAll("\\s+[^\\w\\s]+$", "") + " : "
							+ jphProfilesCountinRow1.getText() + "   "
							+ jphHeader2.getText().replaceAll("\\s+[^\\w\\s]+$", "") + " : "
							+ jphAccessedByinRow1.getText() + "   "
							+ jphHeader3.getText().replaceAll("\\s+[^\\w\\s]+$", "") + " : "
							+ jphAccessedDateinRow1.getText() + "   "
							+ jphHeader4.getText().replaceAll("\\s+[^\\w\\s]+$", "") + " : "
							+ jphActionTakeninRow1.getText() + "   "
							+ jphHeader5.getText().replaceAll("\\s+[^\\w\\s]+$", "") + " : "
							+ jphStatusinRow1.getText());
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"verify_details_of_the_recently_exported_job_profiles_in_job_profile_history_screen",
					"Issue in Verifying details of the Recently Exported Job Profiles in Job Profile History screen in Publish Center",
					e);
		}
	}

	public void click_on_profiles_count_of_recently_exported_job_profiles_in_job_profile_history_screen() {
		try {
			try {
				wait.until(ExpectedConditions.elementToBeClickable(jphProfilesCountinRow1)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", jphProfilesCountinRow1);
				} catch (Exception s) {
					utils.jsClick(driver, jphProfilesCountinRow1);
				}
			}
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
			// PERFORMANCE FIX: Use optimized spinner wait
			PerformanceUtils.waitForSpinnersToDisappear(driver, 5);
			PerformanceUtils.waitForPageReady(driver, 2);
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(profilesExportedScreenTitle)).isDisplayed());
			PageObjectHelper.log(LOGGER,
					"User navigated to " + profilesExportedScreenTitle.getText() + " screen successfully....");
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
							+ profilesDownloadedScreenHeaderDetails.getText());
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
			PageObjectHelper.log(LOGGER,
					"Below are the Profiles Details of the Recently Exported Job Profiles in Profiles Exported screen :");
			for (int i = 1; i <= ProfilesCountInRow1; i++) {
				WebElement profilename = driver
						.findElement(By.xpath("//*/div/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[1]/span"));
				WebElement jobCode = driver
						.findElement(By.xpath("//*/div/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[2]/span"));
				WebElement modifiedBy = driver
						.findElement(By.xpath("//*/div/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[3]/span"));
				WebElement lastModified = driver
						.findElement(By.xpath("//*/div/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[4]/span"));
				PageObjectHelper.log(LOGGER,
						pdHeader1.getText() + " : " + profilename.getText() + "   " + pdHeader2.getText() + " : "
								+ jobCode.getText() + "   " + pdHeader3.getText() + " : " + modifiedBy.getText() + "   "
								+ pdHeader4.getText() + " : " + lastModified.getText());
			}
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_details_in_profiles_exported_screen",
					"Issue in Verifying details of the Recently Exported Job Profiles in Profiles Exported screen in Publish Center",
					e);
		}
	}

	public void close_profiles_exported_screen() {
		try {
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(closeBtn)).isDisplayed());
			try {
				wait.until(ExpectedConditions.elementToBeClickable(closeBtn)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", closeBtn);
				} catch (Exception s) {
					utils.jsClick(driver, closeBtn);
				}
			}
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
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(showingJobResultsCount)).isDisplayed());

			// PERFORMANCE FIX: Use optimized spinner waits with shorter timeouts
			// First scroll to bottom (headless-compatible)
			headlessActions.scrollToBottom();
			PerformanceUtils.waitForSpinnersToDisappear(driver, 5);
			PerformanceUtils.waitForPageReady(driver, 2);

			// Second scroll to bottom (headless-compatible)
			headlessActions.scrollToBottom();
			PerformanceUtils.waitForSpinnersToDisappear(driver, 5);
			PerformanceUtils.waitForPageReady(driver, 2);

			String resultsCountText_updated = wait.until(ExpectedConditions.visibilityOf(showingJobResultsCount))
					.getText();
			PageObjectHelper.log(LOGGER, "Scrolled down till third page and now " + resultsCountText_updated
					+ " of Job Profiles as expected");

			// Scroll to top (headless-compatible)
			js.executeScript("window.scrollTo(0, 0);");
			PerformanceUtils.waitForSpinnersToDisappear(driver, 3);
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
			// PERFORMANCE FIX: Use optimized spinner wait with shorter timeout
			PerformanceUtils.waitForSpinnersToDisappear(driver, 5);
			PerformanceUtils.waitForPageReady(driver, 2);
			PageObjectHelper.log(LOGGER,
					"Below are the details of the First 30 Job Profiles in Default Order before applying sorting in Job Profile History screen : ");
			for (int i = 1; i <= 30; i++) {
				WebElement jphProfilesCount = driver.findElement(
						By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[1]/span"));
				WebElement jphAccessedBy = driver.findElement(
						By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[2]/span"));
				WebElement jphAccessedDate = driver.findElement(
						By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[3]/span"));
				WebElement jphActionTaken = driver.findElement(
						By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[4]/span"));
				WebElement jphStatus = driver.findElement(
						By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[5]/span"));
				PageObjectHelper.log(LOGGER, jphHeader1.getText().replaceAll("\\s+[^\\w\\s]+$", "") + " : "
						+ jphProfilesCount.getText() + "   " + jphHeader2.getText().replaceAll("\\s+[^\\w\\s]+$", "")
						+ " : " + jphAccessedBy.getText() + "   "
						+ jphHeader3.getText().replaceAll("\\s+[^\\w\\s]+$", "") + " : " + jphAccessedDate.getText()
						+ "   " + jphHeader4.getText().replaceAll("\\s+[^\\w\\s]+$", "") + " : "
						+ jphActionTaken.getText() + "   " + jphHeader5.getText().replaceAll("\\s+[^\\w\\s]+$", "")
						+ " : " + jphStatus.getText());
				profilesCountInDefaultOrder.add(jphProfilesCount.getText());
			}
			PageObjectHelper.log(LOGGER,
					"Default Order of first thirty Job Profiles before applying sorting in Job Profile History screen is verified successfully");

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"user_should_verify_first_thirty_job_profiles_in_default_order_before_applying_sorting_in_job_profile_history_screen",
					"Issue in Verifying default Order of first thirty Job Profiles before applying sorting in Job Profile History screen",
					e);
		}
	}

	public void sort_job_profiles_by_no_of_profiles_in_ascending_order_in_job_profile_history_screen() {
		try {
			try {
				wait.until(ExpectedConditions.visibilityOf(jphHeader1)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", jphHeader1);
				} catch (Exception s) {
					utils.jsClick(driver, jphHeader1);
				}
			}
			// PERFORMANCE FIX: Use optimized spinner wait
			PerformanceUtils.waitForSpinnersToDisappear(driver, 5);
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
			// PERFORMANCE FIX: Use optimized spinner wait
			PerformanceUtils.waitForSpinnersToDisappear(driver, 5);
			PerformanceUtils.waitForPageReady(driver, 2);
			PageObjectHelper.log(LOGGER,
					"Below are the details of the First 30 Job Profiles in Job Profile History screen after sorting by NO. OF PROIFLES in Ascending Order: ");

			// Collect profile counts for validation
			ArrayList<Integer> profileCounts = new ArrayList<Integer>();

			for (int i = 1; i <= 30; i++) {
				WebElement jphProfilesCount = driver.findElement(
						By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[1]/span"));
				WebElement jphAccessedBy = driver.findElement(
						By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[2]/span"));
				WebElement jphAccessedDate = driver.findElement(
						By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[3]/span"));
				WebElement jphActionTaken = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div["
						+ Integer.toString(i) + "]/div[4]/span | //*/kf-page-content/div[2]/div[2]/div["
						+ Integer.toString(i) + "]/div[4]"));
				WebElement jphStatus = driver.findElement(
						By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[5]/span"));

				// Store profile count as integer for validation
				int profileCount = Integer.parseInt(jphProfilesCount.getText().trim());
				profileCounts.add(profileCount);

				LOGGER.info(jphHeader1.getText().replaceAll("\\s+[^\\w\\s]+$", "") + " : " + jphProfilesCount.getText()
						+ "   " + jphHeader2.getText().replaceAll("\\s+[^\\w\\s]+$", "") + " : "
						+ jphAccessedBy.getText() + "   " + jphHeader3.getText().replaceAll("\\s+[^\\w\\s]+$", "")
						+ " : " + jphAccessedDate.getText() + "   "
						+ jphHeader4.getText().replaceAll("\\s+[^\\w\\s]+$", "") + " : " + jphActionTaken.getText()
						+ "   " + jphHeader5.getText().replaceAll("\\s+[^\\w\\s]+$", "") + " : " + jphStatus.getText());
			}

			// VALIDATE ASCENDING ORDER
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
			driver.navigate().refresh();
			// PERFORMANCE FIX: Use optimized spinner waits
			PerformanceUtils.waitForSpinnersToDisappear(driver, 5);
			PerformanceUtils.waitForPageReady(driver, 3);
			PageObjectHelper.log(LOGGER, "Refreshed Job Profile History screen....");
			wait.until(ExpectedConditions.visibilityOf(jphProfilesCountinRow1));
			for (int i = 1; i <= 10; i++) {
				WebElement jphProfilesCount = driver.findElement(
						By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[1]/span"));
				String text = jphProfilesCount.getText();
				if (text.contentEquals(profilesCountInDefaultOrder.get(i - 1))) {
					continue;
				} else {
					throw new Exception("No. Of Profiles Count : " + text + " in Row " + Integer.toString(i)
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

	/**
	 * Sorts job profiles by NO. OF PROFILES in descending order (clicks header
	 * twice) Enhanced with proper waits for HEADLESS MODE compatibility
	 */
	public void sort_job_profiles_by_no_of_profiles_in_descending_order_in_job_profile_history_screen() {
		try {
			// FIRST CLICK - Sort Ascending
			LOGGER.info("First click on NO. OF PROFILES header to sort ascending...");
			try {
				wait.until(ExpectedConditions.elementToBeClickable(jphHeader1)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", jphHeader1);
				} catch (Exception s) {
					utils.jsClick(driver, jphHeader1);
				}
			}

			// PERFORMANCE FIX: Wait for FIRST sort to complete
			PerformanceUtils.waitForSpinnersToDisappear(driver, 5);
			Thread.sleep(2000); // Give UI time to process first sort
			PerformanceUtils.waitForPageReady(driver, 2);
			Thread.sleep(1000); // Additional buffer for headless mode

			LOGGER.info("First sort completed. Now clicking second time for descending order...");

			// SECOND CLICK - Sort Descending
			// Ensure element is clickable again before second click
			try {
				wait.until(ExpectedConditions.elementToBeClickable(jphHeader1)).click();
			} catch (Exception e) {
				try {
					Thread.sleep(500); // Brief pause before retry
					js.executeScript("arguments[0].click();", jphHeader1);
				} catch (Exception s) {
					utils.jsClick(driver, jphHeader1);
				}
			}

			// PERFORMANCE FIX: Wait for SECOND sort to complete
			PerformanceUtils.waitForSpinnersToDisappear(driver, 5);
			Thread.sleep(2000); // Give UI time to process second sort
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

	public void user_should_verify_first_thirty_job_profiles_sorted_by_no_of_profiles_in_descending_order_in_job_profile_history_screen() {
		try {
			// PERFORMANCE FIX: Use optimized spinner wait
			PerformanceUtils.waitForSpinnersToDisappear(driver, 5);
			PerformanceUtils.waitForPageReady(driver, 2);
			wait.until(ExpectedConditions.visibilityOf(jphProfilesCountinRow1));
			LOGGER.info(
					"Below are the details of the First 30 Job Profiles in Job Profile History screen after sorting by NO. OF PROIFLES in Descending Order: ");

			// Collect profile counts for validation
			ArrayList<Integer> profileCounts = new ArrayList<Integer>();

			for (int i = 1; i <= 30; i++) {
				WebElement jphProfilesCount = driver.findElement(
						By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[1]/span"));
				WebElement jphAccessedBy = driver.findElement(
						By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[2]/span"));
				WebElement jphAccessedDate = driver.findElement(
						By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[3]/span"));
				WebElement jphActionTaken = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div["
						+ Integer.toString(i) + "]/div[4]/span | //*/kf-page-content/div[2]/div[2]/div["
						+ Integer.toString(i) + "]/div[4]"));
				WebElement jphStatus = driver.findElement(
						By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[5]/span"));

				// Store profile count as integer for validation
				int profileCount = Integer.parseInt(jphProfilesCount.getText().trim());
				profileCounts.add(profileCount);

				LOGGER.info(jphHeader1.getText().replaceAll("\\s+[^\\w\\s]+$", "") + " : " + jphProfilesCount.getText()
						+ "   " + jphHeader2.getText().replaceAll("\\s+[^\\w\\s]+$", "") + " : "
						+ jphAccessedBy.getText() + "   " + jphHeader3.getText().replaceAll("\\s+[^\\w\\s]+$", "")
						+ " : " + jphAccessedDate.getText() + "   "
						+ jphHeader4.getText().replaceAll("\\s+[^\\w\\s]+$", "") + " : " + jphActionTaken.getText()
						+ "   " + jphHeader5.getText().replaceAll("\\s+[^\\w\\s]+$", "") + " : " + jphStatus.getText());
			}

			// VALIDATE DESCENDING ORDER
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
			try {
				wait.until(ExpectedConditions.visibilityOf(jphHeader3)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", jphHeader3);
				} catch (Exception s) {
					utils.jsClick(driver, jphHeader3);
				}
			}
			// PERFORMANCE FIX: Use optimized spinner wait
			PerformanceUtils.waitForSpinnersToDisappear(driver, 5);
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
			// PERFORMANCE FIX: Use optimized spinner wait
			PerformanceUtils.waitForSpinnersToDisappear(driver, 5);
			PerformanceUtils.waitForPageReady(driver, 2);
			LOGGER.info(
					"Below are the details of the First 30 Job Profiles in Job Profile History screen after sorting by ACCESSED DATE in Ascending Order: ");

			// Collect accessed dates for validation
			ArrayList<LocalDate> accessedDates = new ArrayList<LocalDate>();
			java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy",
					java.util.Locale.ENGLISH);

			for (int i = 1; i <= 30; i++) {
				WebElement jphProfilesCount = driver.findElement(
						By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[1]/span"));
				WebElement jphAccessedBy = driver.findElement(
						By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[2]/span"));
				WebElement jphAccessedDate = driver.findElement(
						By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[3]/span"));
				WebElement jphActionTaken = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div["
						+ Integer.toString(i) + "]/div[4]/span | //*/kf-page-content/div[2]/div[2]/div["
						+ Integer.toString(i) + "]/div[4]"));
				WebElement jphStatus = driver.findElement(
						By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[5]/span"));

				// Store accessed date for validation
				String dateText = jphAccessedDate.getText().trim();
				try {
					LocalDate date = LocalDate.parse(dateText, formatter);
					accessedDates.add(date);
				} catch (Exception dateParseEx) {
					LOGGER.warn("Unable to parse date: " + dateText + " at Row " + i
							+ ". Skipping date validation for this row.");
				}

				PageObjectHelper.log(LOGGER, jphHeader1.getText().replaceAll("\\s+[^\\w\\s]+$", "") + " : "
						+ jphProfilesCount.getText() + "   " + jphHeader2.getText().replaceAll("\\s+[^\\w\\s]+$", "")
						+ " : " + jphAccessedBy.getText() + "   "
						+ jphHeader3.getText().replaceAll("\\s+[^\\w\\s]+$", "") + " : " + jphAccessedDate.getText()
						+ "   " + jphHeader4.getText().replaceAll("\\s+[^\\w\\s]+$", "") + " : "
						+ jphActionTaken.getText() + "   " + jphHeader5.getText().replaceAll("\\s+[^\\w\\s]+$", "")
						+ " : " + jphStatus.getText());
			}

			// VALIDATE ASCENDING ORDER (oldest to newest)
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
			// FIRST CLICK - Sort Ascending
			PageObjectHelper.log(LOGGER, "First click on ACCESSED DATE header to sort ascending...");
			try {
				wait.until(ExpectedConditions.elementToBeClickable(jphHeader3)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", jphHeader3);
				} catch (Exception s) {
					utils.jsClick(driver, jphHeader3);
				}
			}

			// PERFORMANCE FIX: Wait for FIRST sort to complete
			PerformanceUtils.waitForSpinnersToDisappear(driver, 5);
			Thread.sleep(2000); // Give UI time to process first sort
			PerformanceUtils.waitForPageReady(driver, 2);
			Thread.sleep(1000); // Additional buffer for headless mode

			LOGGER.info("First sort completed. Now clicking second time for descending order...");

			// SECOND CLICK - Sort Descending
			// Ensure element is clickable again before second click
			try {
				wait.until(ExpectedConditions.elementToBeClickable(jphHeader3)).click();
			} catch (Exception e) {
				try {
					Thread.sleep(500); // Brief pause before retry
					js.executeScript("arguments[0].click();", jphHeader3);
				} catch (Exception s) {
					utils.jsClick(driver, jphHeader3);
				}
			}

			// PERFORMANCE FIX: Wait for SECOND sort to complete
			PerformanceUtils.waitForSpinnersToDisappear(driver, 5);
			Thread.sleep(2000); // Give UI time to process second sort
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
			// PERFORMANCE FIX: Use optimized spinner wait
			PerformanceUtils.waitForSpinnersToDisappear(driver, 5);
			PerformanceUtils.waitForPageReady(driver, 2);
			wait.until(ExpectedConditions.visibilityOf(jphProfilesCountinRow1));
			LOGGER.info(
					"Below are the details of the First 30 Job Profiles in Job Profile History screen after sorting by ACCESSED DATE in Descending Order: ");

			// Collect accessed dates for validation
			ArrayList<LocalDate> accessedDates = new ArrayList<LocalDate>();
			java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy",
					java.util.Locale.ENGLISH);

			for (int i = 1; i <= 30; i++) {
				WebElement jphProfilesCount = driver.findElement(
						By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[1]/span"));
				WebElement jphAccessedBy = driver.findElement(
						By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[2]/span"));
				WebElement jphAccessedDate = driver.findElement(
						By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[3]/span"));
				WebElement jphActionTaken = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div["
						+ Integer.toString(i) + "]/div[4]/span | //*/kf-page-content/div[2]/div[2]/div["
						+ Integer.toString(i) + "]/div[4]"));
				WebElement jphStatus = driver.findElement(
						By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[5]/span"));

				// Store accessed date for validation
				String dateText = jphAccessedDate.getText().trim();
				try {
					LocalDate date = LocalDate.parse(dateText, formatter);
					accessedDates.add(date);
				} catch (Exception dateParseEx) {
					LOGGER.warn("Unable to parse date: " + dateText + " at Row " + i
							+ ". Skipping date validation for this row.");
				}

				PageObjectHelper.log(LOGGER, jphHeader1.getText().replaceAll("\\s+[^\\w\\s]+$", "") + " : "
						+ jphProfilesCount.getText() + "   " + jphHeader2.getText().replaceAll("\\s+[^\\w\\s]+$", "")
						+ " : " + jphAccessedBy.getText() + "   "
						+ jphHeader3.getText().replaceAll("\\s+[^\\w\\s]+$", "") + " : " + jphAccessedDate.getText()
						+ "   " + jphHeader4.getText().replaceAll("\\s+[^\\w\\s]+$", "") + " : "
						+ jphActionTaken.getText() + "   " + jphHeader5.getText().replaceAll("\\s+[^\\w\\s]+$", "")
						+ " : " + jphStatus.getText());
			}

			// VALIDATE DESCENDING ORDER (newest to oldest)
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

}
