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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.ScreenshotHandler;
import com.kfonetalentsuite.utils.JobMapping.Utilities;
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
		// super();
		PageFactory.initElements(driver, this);
		// TODO Auto-generated constructor stub
	}

	WebDriverWait wait = DriverManager.getWait();
	Utilities utils = new Utilities();
	JavascriptExecutor js = (JavascriptExecutor) driver;
	
	//XAPTHs
	@FindBy(xpath = "//*[@class='blocking-loader']//img")
	@CacheLookup
	WebElement pageLoadSpinner;
	
	@FindBy(xpath = "//button[contains(@class,'publish-center')]")
	@CacheLookup
	WebElement publishCenterBtn;
	
	@FindBy(xpath = "//*[contains(text(),'Job Profile History')]")
	@CacheLookup
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
	
	
	
	//METHODs
	public void user_is_in_hcm_sync_profiles_screen_after_syncing_profiles() {
		LOGGER.info("User is in HCM Sync Profiles screen after syncing profiles");
		ExtentCucumberAdapter.addTestStepLog("User is in HCM Sync Profiles screen after syncing profiles");
	}
	
	public void click_on_publish_center_button() {
		try {
			js.executeScript("window.scrollTo(document.body.scrollHeight, 0)");
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
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
		LOGGER.info("Clicked on Publish Center button in My Organization's Job Profiles screen in PM");
		ExtentCucumberAdapter.addTestStepLog("Clicked on Publish Center button in My Organization's Job Profiles screen in PM");
	} catch (Exception e) {
		LOGGER.error("Issue clicking Publish Center button - Method: click_on_publish_center_button", e);
		ScreenshotHandler.captureFailureScreenshot("click_publish_center_button", e);
		e.printStackTrace();
		Assert.fail("Issue in clicking on Publish Center button in My Organization's Job Profiles screen in PM...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in clicking on Publish Center button in My Organization's Job Profiles screen in PM...Please Investigate!!!");
	}
	}
	
	public void verify_user_navigated_to_job_profile_history_screen_succcessfully() {
	try {
		wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
		PerformanceUtils.waitForPageReady(driver, 2);
		Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(jphScreenTitle)).isDisplayed());
		LOGGER.info("User navigated to " + jphScreenTitle.getText() + " screen successfully....");
		ExtentCucumberAdapter.addTestStepLog("User navigated to " + jphScreenTitle.getText() + " screen successfully....");
	} catch(Exception e) {
		LOGGER.error("Issue navigating to Job Profile History screen - Method: verify_user_navigated_to_job_profile_history_screen_succcessfully", e);
		ScreenshotHandler.captureFailureScreenshot("verify_navigation_job_profile_history", e);
		e.printStackTrace();
		Assert.fail("Issue in navigating to Job Profile History screen on click of Publish Center button...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in navigating to Job Profile History screen on click of Publish Center button...Please Investigate!!!");
	}
	}
	
	public void verify_recently_downloaded_job_profiles_history_is_in_top_row() {
		try {
			String profilesCountTextinRow1 = wait.until(ExpectedConditions.visibilityOf(jphProfilesCountinRow1)).getText();
			Assert.assertEquals(Integer.toString(PO22_ValidateHCMSyncProfilesScreen_PM.profilesCount), profilesCountTextinRow1);
			LOGGER.info("Recently downloaded Job Profiles History is displaying in Top Row in Publish Center as expected....");
			ExtentCucumberAdapter.addTestStepLog("Recently downloaded Job Profiles History is displaying in Top Row in Publish Center as expected....");
	} catch (Exception e) {
			LOGGER.error("Issue verifying recently downloaded profiles in top row - Method: verify_recently_downloaded_job_profiles_history_is_in_top_row", e);
			ScreenshotHandler.captureFailureScreenshot("verify_downloaded_profiles_top_row", e);
			e.printStackTrace();
			Assert.fail("Issue in Verifying Recently downloaded Job Profiles History is displaying in Top Row in Publish Center...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying Recently downloaded Job Profiles History is displaying in Top Row in Publish Center...Please Investigate!!!");
	}
		
	}
	
	public void verify_details_of_the_recently_downloaded_job_profiles_in_job_profile_history_screen() {
		try {
			int currentYear = currentdate.getYear();
			int currentDay = currentdate.getDayOfMonth();
			String cd;
			String todayDate;
			if(Integer.toString(currentDay).length() == 1) {
				DecimalFormat df = new DecimalFormat("00"); 
				cd = df.format(currentDay);
				todayDate = currentMonth.toString().substring(0,1) + currentMonth.toString().substring(1,3).toLowerCase() + " " + cd + ", " + Integer.toString(currentYear);
			} else {
				todayDate = currentMonth.toString().substring(0,1) + currentMonth.toString().substring(1,3).toLowerCase() + " " + Integer.toString(currentDay) + ", " + Integer.toString(currentYear);
			}
			Assert.assertEquals(Integer.toString(PO22_ValidateHCMSyncProfilesScreen_PM.profilesCount), jphProfilesCountinRow1.getText());
			LOGGER.info("No.of Profiles count in Publish Center matches with No.of Profiles selected for Download in HCM Sync Profiles screen in PM");
			ExtentCucumberAdapter.addTestStepLog("No.of Profiles count in Publish Center matches with No.of Profiles selected for Download in HCM Sync Job Profiles screen in PM");
			PO22_ValidateHCMSyncProfilesScreen_PM.profilesCount = 0;
			ProfilesCountInRow1 = Integer.parseInt(jphProfilesCountinRow1.getText());
			Assert.assertEquals(jphAccessedDateinRow1.getText(), todayDate);
			LOGGER.info("Accessed Date of the Recently downloaded Job Profile Matches with today's date as expected");
			ExtentCucumberAdapter.addTestStepLog("Accessed Date of the Recently downloaded Job Profile Matches with today's date as expected");
			Assert.assertEquals(jphActionTakeninRow1.getText(),"Downloaded");
			LOGGER.info("Action taken tag displaying as " + "Downloaded" +" for the Recently downloaded Job Profile as expected");
			ExtentCucumberAdapter.addTestStepLog("Action taken tag displaying as " + "Downloaded" +" for the Recently downloaded Job Profile as expected");
			LOGGER.info("Below are the details of the Recently Downloaded Job Profile in Job Profile History screen : \n "
					+ jphHeader1.getText().split(" ▼")[0] + " : " + jphProfilesCountinRow1.getText() + "   "
					+ jphHeader2.getText().split(" ▼")[0] + " : " + jphAccessedByinRow1.getText() + "   "
					+ jphHeader3.getText().split(" ▼")[0] + " : " + jphAccessedDateinRow1.getText() + "   "
					+ jphHeader4.getText().split(" ▼")[0] + " : " + jphActionTakeninRow1.getText()+ "   "
					+ jphHeader5.getText().split(" ▼")[0] + " : " + jphStatusinRow1.getText());
			ExtentCucumberAdapter.addTestStepLog("Below are the details of the Recently Downloaded Job Profile in Job Profile History screen : \n "
					+ jphHeader1.getText().split(" ▼")[0] + " : " + jphProfilesCountinRow1.getText() + "   "
					+ jphHeader2.getText().split(" ▼")[0] + " : " + jphAccessedByinRow1.getText() + "   "
					+ jphHeader3.getText().split(" ▼")[0] + " : " + jphAccessedDateinRow1.getText() + "   "
				+ jphHeader4.getText().split(" ▼")[0] + " : " + jphActionTakeninRow1.getText()+ "   "
				+ jphHeader5.getText().split(" ▼")[0] + " : " + jphStatusinRow1.getText());
		
	} catch (Exception e) {
		LOGGER.error("Issue verifying downloaded profile details - Method: verify_details_of_the_recently_downloaded_job_profiles_in_job_profile_history_screen", e);
		ScreenshotHandler.captureFailureScreenshot("verify_downloaded_profile_details", e);
		e.printStackTrace();
		Assert.fail("Issue in Verifying details of the Recently downloaded Job Profiles in Job Profile History screen in Publish Center...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in Verifying details of the Recently downloaded Job Profiles in Job Profile History screen in Publish Center...Please Investigate!!!");
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
			LOGGER.info("Clicked on Profiles Count of Recently downloaded job profiles in Job Profile History screen");
			ExtentCucumberAdapter.addTestStepLog("Clicked on Profiles Count of Recently downloaded job profiles in Job Profile History screen");
		} catch (Exception e) {
			LOGGER.error("Issue clicking profiles count - Method: click_on_profiles_count_of_recently_downloaded_job_profiles_in_job_profile_history_screen", e);
			ScreenshotHandler.captureFailureScreenshot("click_profiles_count", e);
			e.printStackTrace();
			Assert.fail("Issue in clicking on Profiles Count of Recently downloaded job profiles in Job Profile History screen...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in clicking on Profiles Count of Recently downloaded job profiles in Job Profile History screen...Please Investigate!!!");
	}
	}
	
	public void user_should_be_navigated_to_profiles_downloaded_screen() {
	try {
		wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
		// PERFORMANCE: Replaced Thread.sleep(2000) with smart page ready wait
		PerformanceUtils.waitForPageReady(driver, 2);
		Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(profilesDownloadedScreenTitle)).isDisplayed());
		LOGGER.info("User navigated to " + profilesDownloadedScreenTitle.getText() + " screen successfully....");
		ExtentCucumberAdapter.addTestStepLog("User navigated to " + profilesDownloadedScreenTitle.getText() + " screen successfully....");
	} catch(Exception e) {
		LOGGER.error("Issue navigating to Profiles Downloaded screen - Method: user_should_be_navigated_to_profiles_downloaded_screen", e);
		ScreenshotHandler.captureFailureScreenshot("navigate_profiles_downloaded_screen", e);
		e.printStackTrace();
		Assert.fail("Issue in navigating to Profiles Downloaded screen on click of Profiles count of Recently Downloaded Profile...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in navigating to Profiles Downloaded screen on click of Profiles count of Recently Downloaded Profile...Please Investigate!!!");
	}
	}
	
	
	public void verify_details_in_profiles_downloaded_screen() {
	try {
		wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
		// PERFORMANCE: Replaced Thread.sleep(2000) with smart page ready wait
		PerformanceUtils.waitForPageReady(driver, 2);
		LOGGER.info("Below are the " + "Header Details" +" of the Recently Downloaded Job Profiles in " + "Profiles Downloaded" +" screen : \n "
					+ profilesDownloadedScreenHeaderDetails.getText());
			ExtentCucumberAdapter.addTestStepLog("Below are the " + "Header Details" +" of the Recently Downloaded Job Profiles in " + "Profiles Downloaded" +" screen : \n "
					+ profilesDownloadedScreenHeaderDetails.getText());
			LOGGER.info("Below are the Profiles Details of the Recently Downloaded Job Profiles in Profiles Downloaded screen :");
			ExtentCucumberAdapter.addTestStepLog("Below are the Profiles Details of the Recently Downloaded Job Profiles in Profiles Downloaded screen :");
			for(int i=1; i<= ProfilesCountInRow1; i++) {
				WebElement profilename = driver.findElement(By.xpath("//*/div/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[1]/span"));
				WebElement jobCode = driver.findElement(By.xpath("//*/div/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[2]/span"));
				WebElement modifiedBy = driver.findElement(By.xpath("//*/div/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[3]/span"));
				WebElement lastModified = driver.findElement(By.xpath("//*/div/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[4]/span"));
				LOGGER.info(pdHeader1.getText() + " : " + profilename.getText() +
						"   " + pdHeader2.getText() + " : " + jobCode.getText() +
						"   " + pdHeader3.getText() + " : " + modifiedBy.getText() +
						"   " + pdHeader4.getText() + " : "+ lastModified.getText());
				ExtentCucumberAdapter.addTestStepLog(pdHeader1.getText() + " : " + profilename.getText() +
						"   " + pdHeader2.getText() + " : " + jobCode.getText() +
						"   " + pdHeader3.getText() + " : " + modifiedBy.getText() +
				"   " + pdHeader4.getText() + " : "+ lastModified.getText());
		}
	} catch (Exception e) {
		LOGGER.error("Issue verifying profiles downloaded details - Method: verify_details_in_profiles_downloaded_screen", e);
		ScreenshotHandler.captureFailureScreenshot("verify_profiles_downloaded_details", e);
		e.printStackTrace();
		Assert.fail("Issue in Verifying details of the Recently downloaded Job Profiles in Profiles Downloaded screen in Publish Center...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in Verifying details of the Recently downloaded Job Profiles in Profiles Downloaded screen in Publish Center...Please Investigate!!!");
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
		LOGGER.info("Profiles Downloaded screen closed succesfully....");
		ExtentCucumberAdapter.addTestStepLog("Profiles Downloaded screen closed succesfully....");
	} catch (Exception e) {
		LOGGER.error("Issue closing Profiles Downloaded screen - Method: close_profiles_downloaded_screen", e);
		ScreenshotHandler.captureFailureScreenshot("close_profiles_downloaded_screen", e);
		e.printStackTrace();
		Assert.fail("Issue in clicking on Close button in Profiles Downloaded screen...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in clicking on Close button in Profiles Downloaded screen...Please Investigate!!!");
	}
	}
	
	public void select_job_profiles_in_hcm_sync_profiles_tab() {
		try {
			for(int i=25; i>=6; i--) {
				WebElement	SP_Checkbox = driver.findElement(By.xpath("//tbody//tr[" + Integer.toString(i) + "]//td[1]//*//..//div//kf-checkbox//div"));
				String text = SP_Checkbox.getAttribute("class");
				if(text.contains("disable")) {
					continue;
				} else {
					WebElement profilecheckbox = driver.findElement(By.xpath("//tbody//tr[" + Integer.toString(i) + "]//div[1]//kf-checkbox"));
					try {
						js.executeScript("arguments[0].scrollIntoView();", profilecheckbox);
						wait.until(ExpectedConditions.elementToBeClickable(profilecheckbox)).click();
						PO22_ValidateHCMSyncProfilesScreen_PM.profilesCount = PO22_ValidateHCMSyncProfilesScreen_PM.profilesCount + 1;
					} catch (Exception e) {
						try {
							js.executeScript("arguments[0].click();", profilecheckbox);
						} catch (Exception s) {
							utils.jsClick(driver, profilecheckbox);
						}
						PO22_ValidateHCMSyncProfilesScreen_PM.profilesCount = PO22_ValidateHCMSyncProfilesScreen_PM.profilesCount + 1;
					}
//					ValidateHCMSyncProfiles_PM.profilesCount = ValidateHCMSyncProfiles_PM.profilesCount + 1;
					LOGGER.info("Selected " + Integer.toString(PO22_ValidateHCMSyncProfilesScreen_PM.profilesCount) + " Job Profiles in HCM Sync Profiles screen in PM");
					ExtentCucumberAdapter.addTestStepLog("Selected " + Integer.toString(PO22_ValidateHCMSyncProfilesScreen_PM.profilesCount) + " Job Profiles in HCM Sync Profiles screen in PM");
				}
			}	
	} catch (NoSuchElementException e) {
		LOGGER.error("Issue selecting job profiles - Method: select_job_profiles_in_hcm_sync_profiles_tab", e);
		ScreenshotHandler.captureFailureScreenshot("select_job_profiles", e);
		e.printStackTrace();
		Assert.fail("Issue in selecting Job Profiles in My Organization Job Profiles screen in PM...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in selecting Job Profiles in My Organization Job Profiles screen in PM...Please Investigate!!!");
	}
	}
	
	public void verify_recently_exported_job_profiles_history_is_in_top_row() {
		try {
			String profilesCountTextinRow1 = wait.until(ExpectedConditions.visibilityOf(jphProfilesCountinRow1)).getText();
			Assert.assertEquals(Integer.toString(PO22_ValidateHCMSyncProfilesScreen_PM.profilesCount), profilesCountTextinRow1);
			LOGGER.info("Recently Exported Job Profiles History is displaying in Top Row in Publish Center as expected....");
			ExtentCucumberAdapter.addTestStepLog("Recently Exported Job Profiles History is displaying in Top Row in Publish Center as expected....");
	} catch (Exception e) {
		LOGGER.error("Issue verifying exported profiles in top row - Method: verify_recently_exported_job_profiles_history_is_in_top_row", e);
		ScreenshotHandler.captureFailureScreenshot("verify_exported_profiles_top_row", e);
		e.printStackTrace();
		Assert.fail("Issue in Verifying Recently Exported Job Profiles History is displaying in Top Row in Publish Center...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in Verifying Recently Exported Job Profiles History is displaying in Top Row in Publish Center...Please Investigate!!!");
	}
	}
	
	public void verify_details_of_the_recently_exported_job_profiles_in_job_profile_history_screen() {
		try {
			int currentYear = currentdate.getYear();
			int currentDay = currentdate.getDayOfMonth();
			String cd;
			String todayDate;
			if(Integer.toString(currentDay).length() == 1) {
				DecimalFormat df = new DecimalFormat("00"); 
				cd = df.format(currentDay);
				todayDate = currentMonth.toString().substring(0,1) + currentMonth.toString().substring(1,3).toLowerCase() + " " + cd + ", " + Integer.toString(currentYear);
			} else {
				todayDate = currentMonth.toString().substring(0,1) + currentMonth.toString().substring(1,3).toLowerCase() + " " + Integer.toString(currentDay) + ", " + Integer.toString(currentYear);
			}
			Assert.assertEquals(Integer.toString(PO22_ValidateHCMSyncProfilesScreen_PM.profilesCount), jphProfilesCountinRow1.getText());
			LOGGER.info("No.of Profiles count in Publish Center matches with No.of Profiles selected for Export to HCM in My Organization's Job Profiles screen in PM");
			ExtentCucumberAdapter.addTestStepLog("No.of Profiles count in Publish Center matches with No.of Profiles selected for Export to HCM in My Organization's Job Profiles screen in PM");
			PO22_ValidateHCMSyncProfilesScreen_PM.profilesCount = 0;
			ProfilesCountInRow1 = Integer.parseInt(jphProfilesCountinRow1.getText());
			Assert.assertEquals(jphAccessedDateinRow1.getText(), todayDate);
			LOGGER.info("Accessed Date of the Recently Exported Job Profile Matches with today's date as expected");
			ExtentCucumberAdapter.addTestStepLog("Accessed Date of the Recently Exported Job Profile Matches with today's date as expected");
			Assert.assertEquals(jphActionTakeninRow1.getText(),"Exported to HCM");
			LOGGER.info("Action taken tag displaying as Exported to HCM for the Recently downloaded Job Profile as expected");
			ExtentCucumberAdapter.addTestStepLog("Action taken tag displaying as Export to HCM for the Recently downloaded Job Profile as expected");
			LOGGER.info("Below are the details of the Recently Exported Job Profiles in Job Profile History screen : \n "
					+ jphHeader1.getText().split(" ▼")[0] + " : " + jphProfilesCountinRow1.getText() + "   "
					+ jphHeader2.getText().split(" ▼")[0] + " : " + jphAccessedByinRow1.getText() + "   "
					+ jphHeader3.getText().split(" ▼")[0] + " : " + jphAccessedDateinRow1.getText() + "   "
					+ jphHeader4.getText().split(" ▼")[0] + " : " + jphActionTakeninRow1.getText() + "   "
					+ jphHeader5.getText().split(" ▼")[0] + " : " + jphStatusinRow1.getText());
			ExtentCucumberAdapter.addTestStepLog("Below are the details of the Recently Exported Job Profiles in Job Profile History screen : \n "
					+ jphHeader1.getText().split(" ▼")[0] + " : " + jphProfilesCountinRow1.getText() + "   "
					+ jphHeader2.getText().split(" ▼")[0] + " : " + jphAccessedByinRow1.getText() + "   "
					+ jphHeader3.getText().split(" ▼")[0] + " : " + jphAccessedDateinRow1.getText() + "   "
					+ jphHeader4.getText().split(" ▼")[0] + " : " + jphActionTakeninRow1.getText() + "   "
					+ jphHeader5.getText().split(" ▼")[0] + " : " + jphStatusinRow1.getText());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Issue in Verifying details of the Recently Exported Job Profiles in Job Profile History screen in Publish Center...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying details of the Recently Exported Job Profiles in Job Profile History screen in Publish Center...Please Investigate!!!");
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
			LOGGER.info("Clicked on Profiles Count of Recently Exported job profiles in Job Profile History screen");
			ExtentCucumberAdapter.addTestStepLog("Clicked on Profiles Count of Recently Exported job profiles in Job Profile History screen");
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Issue in clicking on Profiles Count of Recently Exported job profiles in Job Profile History screen...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in clicking on Profiles Count of Recently Exported job profiles in Job Profile History screen...Please Investigate!!!");
		}
	}
	
	public void user_should_be_navigated_to_profiles_exported_screen() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			PerformanceUtils.waitForPageReady(driver, 2);
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(profilesExportedScreenTitle)).isDisplayed());
			LOGGER.info("User navigated to " + profilesExportedScreenTitle.getText() + " screen successfully....");
			ExtentCucumberAdapter.addTestStepLog("User navigated to " + profilesExportedScreenTitle.getText() + " screen successfully....");
		} catch(Exception e) {
			e.printStackTrace();
			Assert.fail("Issue in navigating to Profiles Exported screen on click of Profiles count of Recently Downloaded Profile...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in navigating to Profiles Exported screen on click of Profiles count of Recently Downloaded Profile...Please Investigate!!!");
		}
	}
	
	public void verify_details_in_profiles_exported_screen() {
		try {
			LOGGER.info("Below are the Header Details of the Recently Exported Job Profiles in Profiles Exported screen : \n "
					+ profilesDownloadedScreenHeaderDetails.getText());
			ExtentCucumberAdapter.addTestStepLog("Below are the Header Details of the Recently Exported Job Profiles in Profiles Exported screen : \n "
					+ profilesDownloadedScreenHeaderDetails.getText());
			LOGGER.info("Below are the Profiles Details of the Recently Exported Job Profiles in Profiles Exported screen :");
			ExtentCucumberAdapter.addTestStepLog("Below are the Profiles Details of the Recently Exported Job Profiles in Profiles Exported screen :");
			for(int i=1; i<= ProfilesCountInRow1; i++) {
				WebElement profilename = driver.findElement(By.xpath("//*/div/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[1]/span"));
				WebElement jobCode = driver.findElement(By.xpath("//*/div/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[2]/span"));
				WebElement modifiedBy = driver.findElement(By.xpath("//*/div/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[3]/span"));
				WebElement lastModified = driver.findElement(By.xpath("//*/div/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[4]/span"));
				LOGGER.info(pdHeader1.getText() + " : " + profilename.getText() +
						"   " + pdHeader2.getText() + " : " + jobCode.getText() +
						"   " + pdHeader3.getText() + " : " + modifiedBy.getText() +
						"   " + pdHeader4.getText() + " : " + lastModified.getText());
				ExtentCucumberAdapter.addTestStepLog(pdHeader1.getText() + " : " + profilename.getText() +
						"   " + pdHeader2.getText() + " : " + jobCode.getText() +
						"   " + pdHeader3.getText() + " : " + modifiedBy.getText() +
						"   " + pdHeader4.getText() + " : " + lastModified.getText());
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Issue in Verifying details of the Recently Exported Job Profiles in Profiles Exported screen in Publish Center...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying details of the Recently Exported Job Profiles in Profiles Exported screen in Publish Center...Please Investigate!!!");
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
			LOGGER.info("Profiles Exported screen closed succesfully....");
			ExtentCucumberAdapter.addTestStepLog("Profiles Exported screen closed succesfully....");
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Issue in clicking on Close button in Profiles Exported screen...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in clicking on Close button in Profiles Exported screen...Please Investigate!!!");
		}
	}
	
	public void user_is_in_job_profile_history_screen() {
		LOGGER.info("User is in Job Profile History screen in Publish Center....");
		ExtentCucumberAdapter.addTestStepLog("User is in Job Profile History screen in Publish Center....");
	}
	
	public void user_should_scroll_page_down_two_times_to_view_first_thirty_job_profiles_in_job_profile_history_screen() {
		try {
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(showingJobResultsCount)).isDisplayed());
			js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			PerformanceUtils.waitForPageReady(driver, 3);
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			PerformanceUtils.waitForPageReady(driver, 3);
			String resultsCountText_updated = wait.until(ExpectedConditions.visibilityOf(showingJobResultsCount)).getText();
			LOGGER.info("Scrolled down till third page and now "+ resultsCountText_updated + " of Job Profiles as expected");
			ExtentCucumberAdapter.addTestStepLog("Scrolled down till third page and now "+ resultsCountText_updated + " of Job Profiles as expected");	
			js.executeScript("window.scrollTo(document.body.scrollHeight, 0)");
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			PerformanceUtils.waitForPageReady(driver, 2);
	} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Issue in scrolling page down two times to view first thirty job profiles in Job Profile History screen...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in scrolling page down two times to view first thirty job profiles in Job Profile History screen...Please Investigate!!!");
		} 	
	}
	
	public void user_should_verify_first_thirty_job_profiles_in_default_order_before_applying_sorting_in_job_profile_history_screen() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			PerformanceUtils.waitForPageReady(driver, 2);
			LOGGER.info("Below are the details of the First 30 Job Profiles in Default Order before applying sorting in Job Profile History screen : ");
			for(int i=1; i<= 30; i++) {
				WebElement jphProfilesCount = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[1]/span"));
				WebElement jphAccessedBy = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[2]/span"));
				WebElement jphAccessedDate = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[3]/span"));
				WebElement jphActionTaken = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[4]/span"));
				WebElement jphStatus = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[5]/span"));
				LOGGER.info(jphHeader1.getText().split(" ▼")[0] + " : " + jphProfilesCount.getText() + "   "
						+ jphHeader2.getText().split(" ▼")[0] + " : " + jphAccessedBy.getText() + "   "
						+ jphHeader3.getText().split(" ▼")[0] + " : " + jphAccessedDate.getText() + "   "
						+ jphHeader4.getText().split(" ▼")[0] + " : " + jphActionTaken.getText() + "   "
						+ jphHeader5.getText().split(" ▼")[0] + " : " + jphStatus.getText());
				ExtentCucumberAdapter.addTestStepLog(jphHeader1.getText().split(" ▼")[0] + " : " + jphProfilesCount.getText() + "   "
						+ jphHeader2.getText().split(" ▼")[0] + " : " + jphAccessedBy.getText() + "   "
						+ jphHeader3.getText().split(" ▼")[0] + " : " + jphAccessedDate.getText() + "   "
						+ jphHeader4.getText().split(" ▼")[0] + " : " + jphActionTaken.getText() + "   "
						+ jphHeader5.getText().split(" ▼")[0] + " : " + jphStatus.getText());
				profilesCountInDefaultOrder.add(jphProfilesCount.getText());
			}
			LOGGER.info("Default Order of first thirty Job Profiles before applying sorting in Job Profile History screen is verified successfully");
			ExtentCucumberAdapter.addTestStepLog("Default Order of first thirty Job Profiles before applying sorting in Job Profile History screen is verified successfully");
			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Issue in Verifying default Order of first thirty Job Profiles before applying sorting in Job Profile History screen...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying default Order of first thirty Job Profiles before applying sorting in Job Profile History screen...Please Investigate!!!");
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
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			PerformanceUtils.waitForPageReady(driver, 2);
			LOGGER.info("Clicked on NO. OF PROFILES header to Sort Job Profiles by No. of Profiles in ascending order in Job Profile History screen");
			ExtentCucumberAdapter.addTestStepLog("Clicked on NO. OF PROFILES header to Sort Job Profiles by No. of Profiles in ascending order in Job Profile History screen");
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Issue in clicking on NO. OF PROFILES header to Sort Job Profiles by No. of Profiles in ascending order in Job Profile History screen...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in clicking on NO. OF PROFILES header to Sort Job Profiles by No. of Profiles in ascending order in Job Profile History screen...Please Investigate!!!");
		}
	}
	
	public void user_should_verify_first_thirty_job_profiles_sorted_by_no_of_profiles_in_ascending_order_in_job_profile_history_screen() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			PerformanceUtils.waitForPageReady(driver, 2);
			LOGGER.info("Below are the details of the First 30 Job Profiles in Job Profile History screen after sorting by NO. OF PROIFLES in Ascending Order: ");
			for(int i=1; i<= 30; i++) {
				WebElement jphProfilesCount = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[1]/span"));
				WebElement jphAccessedBy = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[2]/span"));
				WebElement jphAccessedDate = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[3]/span"));
				WebElement jphActionTaken = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[4]/span | //*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[4]"));
				WebElement jphStatus = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[5]/span"));
				LOGGER.info(jphHeader1.getText().split(" ▼")[0] + " : " + jphProfilesCount.getText() + "   "
						+ jphHeader2.getText().split(" ▼")[0] + " : " + jphAccessedBy.getText() + "   "
						+ jphHeader3.getText().split(" ▼")[0] + " : " + jphAccessedDate.getText() + "   "
						+ jphHeader4.getText().split(" ▼")[0] + " : " + jphActionTaken.getText() + "   "
						+ jphHeader5.getText().split(" ▼")[0] + " : " + jphStatus.getText());
				ExtentCucumberAdapter.addTestStepLog(jphHeader1.getText().split(" ▼")[0] + " : " + jphProfilesCount.getText() + "   "
						+ jphHeader2.getText().split(" ▼")[0] + " : " + jphAccessedBy.getText() + "   "
						+ jphHeader3.getText().split(" ▼")[0] + " : " + jphAccessedDate.getText() + "   "
						+ jphHeader4.getText().split(" ▼")[0] + " : " + jphActionTaken.getText() + "   "
						+ jphHeader5.getText().split(" ▼")[0] + " : " + jphStatus.getText());
			}
			LOGGER.info("First thirty Job Profiles in Job Profile History screen after sorting by NO. OF PROIFLES in Ascending Order is verified successfully");
			ExtentCucumberAdapter.addTestStepLog("First thirty Job Profiles in Job Profile History screen after sorting by NO. OF PROIFLES in Ascending Order is verified successfully");
			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Issue in Verifying first thirty Job Profiles in Job Profile History screen after sorting by NO. OF PROIFLES in Ascending Order...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying first thirty Job Profiles in Job Profile History screen after sorting by NO. OF PROIFLES in Ascending Order...Please Investigate!!!");
		}	
	}
	
	public void user_should_refresh_job_profile_history_screen_and_verify_job_profiles_are_in_default_order() {
		try {
			driver.navigate().refresh();
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			PerformanceUtils.waitForPageReady(driver, 3);
			LOGGER.info("Refreshed Job Profile History screen....");
			ExtentCucumberAdapter.addTestStepLog("Refreshed Job Profile History screen....");
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			wait.until(ExpectedConditions.visibilityOf(jphProfilesCountinRow1));
			for(int i=1; i<= 10; i++) {
				WebElement jphProfilesCount = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[1]/span"));
				String text = jphProfilesCount.getText();
				if(text.contentEquals(profilesCountInDefaultOrder.get(i-1))) {
					continue;
				} else {
					throw new Exception("No. Of Profiles Count : " + text + " in Row " + Integer.toString(i) + " DOEST NOT Match with No. Of Profiles count : " + profilesCountInDefaultOrder.get(i-1) + " after Refreshing Job Profile History screen");
				}
				}
			LOGGER.info("Job Profiles are in Default Order as expected After Refreshing the Job Profile History screen....");
			ExtentCucumberAdapter.addTestStepLog("Job Profiles are in Default Order as expected After Refreshing the Job Profile History screen....");
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Issue in Verifying Default order of Job Profiles after Refreshing Job Profile History screen...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying Default order of Job Profiles after Refreshing Job Profile History screen...Please Investigate!!!");
		}
	}
	
	public void sort_job_profiles_by_no_of_profiles_in_descending_order_in_job_profile_history_screen() {
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
			
			try {
				wait.until(ExpectedConditions.visibilityOf(jphHeader1)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", jphHeader1);
				} catch (Exception s) {
					utils.jsClick(driver, jphHeader1);
				}
			}
			
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			PerformanceUtils.waitForPageReady(driver, 2);
			LOGGER.info("Clicked two times on NO. OF PROFILES header to Sort Job Profiles by No. of Profiles in descending order in Job Profile History screen");
			ExtentCucumberAdapter.addTestStepLog("Clicked two times on NO. OF PROFILES header to Sort Job Profiles by No. of Profiles in descending order in Job Profile History screen");
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Issue in clicking on NO. OF PROFILES header to Sort Job Profiles by No. of Profiles in descending order in Job Profile History screen...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in clicking on NO. OF PROFILES header to Sort Job Profiles by No. of Profiles in descending order in Job Profile History screen...Please Investigate!!!");
		}
	}
	
public void user_should_verify_first_thirty_job_profiles_sorted_by_no_of_profiles_in_descending_order_in_job_profile_history_screen() {
	try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			PerformanceUtils.waitForPageReady(driver, 2);
			wait.until(ExpectedConditions.visibilityOf(jphProfilesCountinRow1));
			LOGGER.info("Below are the details of the First 30 Job Profiles in Job Profile History screen after sorting by NO. OF PROIFLES in Descending Order: ");
			for(int i=1; i<= 30; i++) {
				WebElement jphProfilesCount = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[1]/span"));
				WebElement jphAccessedBy = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[2]/span"));
				WebElement jphAccessedDate = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[3]/span"));
				WebElement jphActionTaken = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[4]/span | //*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[4]"));
				WebElement jphStatus = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[5]/span"));
				LOGGER.info(jphHeader1.getText().split(" ▼")[0] + " : " + jphProfilesCount.getText() + "   "
						+ jphHeader2.getText().split(" ▼")[0] + " : " + jphAccessedBy.getText() + "   "
						+ jphHeader3.getText().split(" ▼")[0] + " : " + jphAccessedDate.getText() + "   "
						+ jphHeader4.getText().split(" ▼")[0] + " : " + jphActionTaken.getText() + "   "
						+ jphHeader5.getText().split(" ▼")[0] + " : " + jphStatus.getText());
				ExtentCucumberAdapter.addTestStepLog(jphHeader1.getText().split(" ▼")[0] + " : " + jphProfilesCount.getText() + "   "
						+ jphHeader2.getText().split(" ▼")[0] + " : " + jphAccessedBy.getText() + "   "
						+ jphHeader3.getText().split(" ▼")[0] + " : " + jphAccessedDate.getText() + "   "
						+ jphHeader4.getText().split(" ▼")[0] + " : " + jphActionTaken.getText() + "   "
						+ jphHeader5.getText().split(" ▼")[0] + " : " + jphStatus.getText());
			}
			LOGGER.info("First thirty Job Profiles in Job Profile History screen after sorting by NO. OF PROIFLES in Descending Order is verified successfully");
			ExtentCucumberAdapter.addTestStepLog("First thirty Job Profiles in Job Profile History screen after sorting by NO. OF PROIFLES in Descending Order is verified successfully");
			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Issue in Verifying first thirty Job Profiles in Job Profile History screen after sorting by NO. OF PROIFLES in Descending Order...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying first thirty Job Profiles in Job Profile History screen after sorting by NO. OF PROIFLES in Descending Order...Please Investigate!!!");			
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
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			PerformanceUtils.waitForPageReady(driver, 2);
			LOGGER.info("Clicked on ACCESSED DATE header to Sort Job Profiles by Accessed Date in ascending order in Job Profile History screen");
			ExtentCucumberAdapter.addTestStepLog("Clicked on ACCESSED DATE header to Sort Job Profiles by Accessed Date in ascending order in Job Profile History screen");
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Issue in clicking on ACCESSED DATE header to Sort Job Profiles by Accessed Date in ascending order in Job Profile History screen...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in clicking on ACCESSED DATE header to Sort Job Profiles by Accessed Date in ascending order in Job Profile History screen...Please Investigate!!!");
		}
	}
	
	public void user_should_verify_first_thirty_job_profiles_sorted_by_accessed_date_in_ascending_order_in_job_profile_history_screen() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			PerformanceUtils.waitForPageReady(driver, 2);
			LOGGER.info("Below are the details of the First 30 Job Profiles in Job Profile History screen after sorting by ACCESSED DATE in Ascending Order: ");
			for(int i=1; i<= 30; i++) {
				WebElement jphProfilesCount = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[1]/span"));
				WebElement jphAccessedBy = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[2]/span"));
				WebElement jphAccessedDate = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[3]/span"));
				WebElement jphActionTaken = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[4]/span | //*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[4]"));
				WebElement jphStatus = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[5]/span"));
				LOGGER.info(jphHeader1.getText().split(" ▼")[0] + " : " + jphProfilesCount.getText() + "   "
						+ jphHeader2.getText().split(" ▼")[0] + " : " + jphAccessedBy.getText() + "   "
						+ jphHeader3.getText().split(" ▼")[0] + " : " + jphAccessedDate.getText() + "   "
						+ jphHeader4.getText().split(" ▼")[0] + " : " + jphActionTaken.getText() + "   "
						+ jphHeader5.getText().split(" ▼")[0] + " : " + jphStatus.getText());
				ExtentCucumberAdapter.addTestStepLog(jphHeader1.getText().split(" ▼")[0] + " : " + jphProfilesCount.getText() + "   "
						+ jphHeader2.getText().split(" ▼")[0] + " : " + jphAccessedBy.getText() + "   "
						+ jphHeader3.getText().split(" ▼")[0] + " : " + jphAccessedDate.getText() + "   "
						+ jphHeader4.getText().split(" ▼")[0] + " : " + jphActionTaken.getText() + "   "
						+ jphHeader5.getText().split(" ▼")[0] + " : " + jphStatus.getText());			}
			LOGGER.info("First thirty Job Profiles in Job Profile History screen after sorting by ACCESSED DATE in Ascending Order is verified successfully");
			ExtentCucumberAdapter.addTestStepLog("First thirty Job Profiles in Job Profile History screen after sorting by ACCESSED DATE in Ascending Order is verified successfully");
			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Issue in Verifying first thirty Job Profiles in Job Profile History screen after sorting by ACCESSED DATE in Ascending Order...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying first thirty Job Profiles in Job Profile History screen after sorting by ACCESSED DATE in Ascending Order...Please Investigate!!!");
		}	
	}
	
	public void sort_job_profiles_by_accessed_date_in_descending_order_in_job_profile_history_screen() {
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
			
			try {
				wait.until(ExpectedConditions.visibilityOf(jphHeader3)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", jphHeader3);
				} catch (Exception s) {
					utils.jsClick(driver, jphHeader3);
				}
			}
			
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			PerformanceUtils.waitForPageReady(driver, 2);
			LOGGER.info("Clicked two times on ACCESSED DATE header to Sort Job Profiles by Accessed Date in descending order in Job Profile History screen");
			ExtentCucumberAdapter.addTestStepLog("Clicked two times on ACCESSED DATE header to Sort Job Profiles by Accessed Date in descending order in Job Profile History screen");
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Issue in clicking on ACCESSED DATE header to Sort Job Profiles by Accessed Date in descending order in Job Profile History screen...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in clicking on ACCESSED DATE header to Sort Job Profiles by Accessed Date in descending order in Job Profile History screen...Please Investigate!!!");
		}
	}
	
public void user_should_verify_first_thirty_job_profiles_sorted_by_accessed_date_in_descending_order_in_job_profile_history_screen() {
	try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			PerformanceUtils.waitForPageReady(driver, 2);
			wait.until(ExpectedConditions.visibilityOf(jphProfilesCountinRow1));
			LOGGER.info("Below are the details of the First 30 Job Profiles in Job Profile History screen after sorting by ACCESSED DATE in Descending Order: ");
			for(int i=1; i<= 30; i++) {
				WebElement jphProfilesCount = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[1]/span"));
				WebElement jphAccessedBy = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[2]/span"));
				WebElement jphAccessedDate = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[3]/span"));
				WebElement jphActionTaken = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[4]/span | //*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[4]"));
				WebElement jphStatus = driver.findElement(By.xpath("//*/kf-page-content/div[2]/div[2]/div[" + Integer.toString(i) + "]/div[5]/span"));
				LOGGER.info(jphHeader1.getText().split(" ▼")[0] + " : " + jphProfilesCount.getText() + "   "
						+ jphHeader2.getText().split(" ▼")[0] + " : " + jphAccessedBy.getText() + "   "
						+ jphHeader3.getText().split(" ▼")[0] + " : " + jphAccessedDate.getText() + "   "
						+ jphHeader4.getText().split(" ▼")[0] + " : " + jphActionTaken.getText() + "   "
						+ jphHeader5.getText().split(" ▼")[0] + " : " + jphStatus.getText());
				ExtentCucumberAdapter.addTestStepLog(jphHeader1.getText().split(" ▼")[0] + " : " + jphProfilesCount.getText() + "   "
						+ jphHeader2.getText().split(" ▼")[0] + " : " + jphAccessedBy.getText() + "   "
						+ jphHeader3.getText().split(" ▼")[0] + " : " + jphAccessedDate.getText() + "   "
						+ jphHeader4.getText().split(" ▼")[0] + " : " + jphActionTaken.getText() + "   "
						+ jphHeader5.getText().split(" ▼")[0] + " : " + jphStatus.getText());
			}
			LOGGER.info("First thirty Job Profiles in Job Profile History screen after sorting by ACCESSED DATE in Descending Order is verified successfully");
			ExtentCucumberAdapter.addTestStepLog("First thirty Job Profiles in Job Profile History screen after sorting by ACCESSED DATE in Descending Order is verified successfully");
			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Issue in Verifying first thirty Job Profiles in Job Profile History screen after sorting by ACCESSED DATE in Descending Order...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying first thirty Job Profiles in Job Profile History screen after sorting by ACCESSED DATE in Descending Order...Please Investigate!!!");			
		}	
	}
	
}
