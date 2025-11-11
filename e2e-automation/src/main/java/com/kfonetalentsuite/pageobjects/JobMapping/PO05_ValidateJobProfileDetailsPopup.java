package com.kfonetalentsuite.pageobjects.JobMapping;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.ScreenshotHandler;
import com.kfonetalentsuite.utils.JobMapping.Utilities;
import com.kfonetalentsuite.webdriverManager.DriverManager;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

public class PO05_ValidateJobProfileDetailsPopup {
	WebDriver driver = DriverManager.getDriver();	

	protected static final Logger LOGGER = (Logger) LogManager.getLogger();
	PO05_ValidateJobProfileDetailsPopup validateJobProfileDetailsPopup;
	

	public PO05_ValidateJobProfileDetailsPopup() throws IOException {
		// super();
		PageFactory.initElements(driver, this);
	}

	WebDriverWait wait = DriverManager.getWait();
	Utilities utils = new Utilities();
	JavascriptExecutor js = (JavascriptExecutor) driver;
	
	public static String ProfileDetails;
	public static String ProfileRoleSummary;
	public static String ProfileResponsibilities;
	public static String ProfileBehaviouralCompetencies;
	public static String ProfileSkills;
	
	//XPATHs
	@FindBy(xpath = "//h2[@id='summary-modal']//p")
	@CacheLookup
	public WebElement profileHeader;
	
	@FindBy(xpath = "//div[@id='details-container']//div[contains(@class,'mt-2')]")
	@CacheLookup
	public WebElement profileDetails;
	
	@FindBy(xpath = "//select[contains(@id,'profileLevel') or @id='profile-level']")
	@CacheLookup
	public WebElement profileLevelDropdown;
	
	@FindBy(xpath = "//div[@id='role-summary-container']//p")
	@CacheLookup
	public WebElement roleSummary;
	
	@FindBy(xpath = "//div[@id='responsibilities-panel']//button[contains(text(),'View')]")
	@CacheLookup
	public List <WebElement> viewMoreButtonInResponsibilitiesTab;
	
	@FindBy(xpath = "//div[@id='responsibilities-panel']//div[@id='item-container']")
	@CacheLookup
	public WebElement responisbilitiesData;
	
	@FindBy(xpath = "//button[contains(text(),'BEHAVIOUR')]")
	@CacheLookup
	public WebElement behaviourCompetenciesTabButton;
	
	@FindBy(xpath = "//div[@id='behavioural-panel']//button[contains(text(),'View')]")
	@CacheLookup
	public List <WebElement> viewMoreButtonInBehaviourCompetenciesTab;
	
	@FindBy(xpath = "//div[@id='behavioural-panel']//div[@id='item-container']")
	@CacheLookup
	public WebElement behaviourData;
	
	@FindBy(xpath = "//button[text()='SKILLS']")
	@CacheLookup
	public WebElement skillsTabButton;
	
	@FindBy(xpath = "//div[@id='skills-panel']//button[contains(text(),'View')]")
	@CacheLookup
	public List <WebElement> viewMoreButtonInSkillsTab;
	
	@FindBy(xpath = "//div[@id='skills-panel']//div[@id='item-container']")
	@CacheLookup
	public WebElement skillsData;
	
	@FindBy(xpath = "//button[@id='publish-job-profile']")
	@CacheLookup
	public WebElement publishProfileButton;
	
	
	//METHODs
	
	public void user_is_on_profile_details_popup() {
		LOGGER.info("User is on Profile Details Popup....");
		ExtentCucumberAdapter.addTestStepLog("User is on Profile Details Popup....");
	}
	
	public void verify_profile_header_matches_with_matched_profile_name() {
		try {
		String profileHeaderName = wait.until(ExpectedConditions.visibilityOf(profileHeader)).getText();
		Assert.assertEquals(PO15_ValidateRecommendedProfileDetails.matchedSuccessPrflName,profileHeaderName);
		LOGGER.info("Profile header on the details popup : " + profileHeaderName);
		ExtentCucumberAdapter.addTestStepLog("Profile header on the details popup : " + profileHeaderName);
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_profile_header_matches_with_matched_profile_name", e);
			LOGGER.error("❌ Failed to verify profile details popup header - Method: verify_profile_header_matches_with_matched_profile_name", e);
			e.printStackTrace();
			Assert.fail("Issue in verifying profile details popup header....Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying profile details popup header....Please Investigate!!!");
		}
	}
	
	public void verify_profile_details_displaying_on_the_popup() {
		try {
		String profileDeatilsText = wait.until(ExpectedConditions.visibilityOf(profileDetails)).getText();
		ProfileDetails = profileDeatilsText;
		LOGGER.info("Below are the Profile Details displaying on the popup screen: ");
		LOGGER.info(profileDeatilsText);
		ExtentCucumberAdapter.addTestStepLog("Below are the Profile Details displaying on the popup screen: \n");
		ExtentCucumberAdapter.addTestStepLog(profileDeatilsText);
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_profile_details_displaying_on_the_popup", e);
			LOGGER.error("❌ Failed to display profile details on the popup screen - Method: verify_profile_details_displaying_on_the_popup", e);
			e.printStackTrace();
			Assert.fail("Issue in displaying profile details on the popup screen....Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in displaying profile details on the popup screen....Please Investigate!!!");
		}
	}

	
	public void user_should_verify_profile_level_dropdown_is_available_and_validate_levels_present_inside_dropdown() {
		try {
			if(wait.until(ExpectedConditions.visibilityOf(profileLevelDropdown)).isEnabled()) {
				wait.until(ExpectedConditions.elementToBeClickable(profileLevelDropdown)).click();
				LOGGER.info("Profile Level dropdown is available and Clicked on it...");
				ExtentCucumberAdapter.addTestStepLog("Profile Level dropdown is available and Clicked on it...");
				Select dropdown = new Select(profileLevelDropdown);
				List<WebElement> allOptions = dropdown.getOptions();
				LOGGER.info("Levels present inside profile level dropdown : ");
				ExtentCucumberAdapter.addTestStepLog("Levels present inside profile level dropdown : ");
				for(WebElement option : allOptions){
			        LOGGER.info(option.getText());
			        ExtentCucumberAdapter.addTestStepLog(option.getText());
			        }
				wait.until(ExpectedConditions.elementToBeClickable(profileLevelDropdown)).click(); // This click is to close dropdown options visibility 
				LOGGER.info("Successfully Verified Profile Level dropdown and levels present inside it...");
				ExtentCucumberAdapter.addTestStepLog("Successfully Verified Profile Level dropdown and levels present inside it...");
			} else {
				LOGGER.info("No Profile Levels available for this mapped profile with name : " + PO15_ValidateRecommendedProfileDetails.matchedSuccessPrflName); 
				ExtentCucumberAdapter.addTestStepLog("No Profile Levels available for this mapped profile with name : " + PO15_ValidateRecommendedProfileDetails.matchedSuccessPrflName);
		}
		
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_verify_profile_level_dropdown_is_available_and_validate_levels_present_inside_dropdown", e);
			LOGGER.error("❌ Failed to validate profile level dropdown - Method: user_should_verify_profile_level_dropdown_is_available_and_validate_levels_present_inside_dropdown", e);
			e.printStackTrace();
			Assert.fail("Issue in validating profile level dropdown in profile details popup in Job Mapping page...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in validating profile level dropdown in profile details popup in Job Mapping page...Please Investigate!!!");
		}	
	}

	
	public void validate_role_summary_is_displaying() {
		try {
		String roleSummaryText = wait.until(ExpectedConditions.visibilityOf(roleSummary)).getText();
		ProfileRoleSummary = roleSummaryText.split(": ", 2)[1].trim();
		LOGGER.info("Role summary of Matched Success Profile : " + ProfileRoleSummary);
		ExtentCucumberAdapter.addTestStepLog("Role summary of Matched Success Profile : " + ProfileRoleSummary); 
	} catch (Exception e) {
		ScreenshotHandler.captureFailureScreenshot("validate_role_summary_is_displaying", e);
		LOGGER.error("❌ Failed to validate Role Summary in Profile Details Popup - Method: validate_role_summary_is_displaying", e);
		e.printStackTrace();
		Assert.fail("Issue in validating Role Summary in Profile Details Popup in Job Mapping page...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in validating Role Summary in Profile Details Popup in Job Mapping page...Please Investigate!!!");
	}
	}

	
public void validate_data_in_responsibilities_tab() {
	try {
		// PERFORMANCE: Replaced Thread.sleep(2000) with smart page ready wait
		PerformanceUtils.waitForPageReady(driver, 2);
		wait.until(ExpectedConditions.elementToBeClickable(viewMoreButtonInResponsibilitiesTab.get(0))).click();
			while(true) {
				try {
					if(viewMoreButtonInResponsibilitiesTab.isEmpty()) {
						LOGGER.info("Reached end of content in Responsibilities screen");
						ExtentCucumberAdapter.addTestStepLog("Reached end of content in Responsibilities screen");
						break;
					}
				js.executeScript("arguments[0].scrollIntoView(true);", viewMoreButtonInResponsibilitiesTab.get(0));
				String ViewMoreBtnText = wait.until(ExpectedConditions.visibilityOf(viewMoreButtonInResponsibilitiesTab.get(0))).getText();
				viewMoreButtonInResponsibilitiesTab.get(0).click();
				js.executeScript("arguments[0].scrollIntoView(true);", viewMoreButtonInResponsibilitiesTab.get(0));
				LOGGER.info("Clicked on "+ ViewMoreBtnText +" button in Responsibilities screen");
				ExtentCucumberAdapter.addTestStepLog("Clicked on "+ ViewMoreBtnText +" button in Responsibilities screen");
				// PERFORMANCE: Replaced Thread.sleep(2000) with smart UI stability wait
				PerformanceUtils.waitForUIStability(driver, 2);
				} catch(StaleElementReferenceException e) {
					LOGGER.info("Reached end of content in Responsibilities screen");
					ExtentCucumberAdapter.addTestStepLog("Reached end of content in Responsibilities screen");
					break;
				}	
			}
		String responsibilitiesDataText = wait.until(ExpectedConditions.visibilityOf(responisbilitiesData)).getText();
		ProfileResponsibilities = responsibilitiesDataText;
//			LOGGER.info("Data present in Responsibilities screen : \n" + responsibilitiesDataText);
//			ExtentCucumberAdapter.addTestStepLog("Data present in Responsibilities screen : \n" + responsibilitiesDataText);
	} catch (Exception e) {
		ScreenshotHandler.captureFailureScreenshot("validate_data_in_responsibilities_tab", e);
		LOGGER.error("❌ Failed to validate data in Responsibilities screen - Method: validate_data_in_responsibilities_tab", e);
		e.printStackTrace();
		Assert.fail("Issue in validating data in Responsibilities screen in Profile Details Popup in Job Mapping page...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in validating data in Responsibilities screen in Profile Details Popup in Job Mapping page...Please Investigate!!!");
	}
}
	
	
public void validate_data_in_behavioural_competencies_tab() {
	try {
		js.executeScript("arguments[0].scrollIntoView(true);", roleSummary);
		// PERFORMANCE: Replaced Thread.sleep(3000) with smart page ready wait
		PerformanceUtils.waitForPageReady(driver, 3);
		wait.until(ExpectedConditions.elementToBeClickable(behaviourCompetenciesTabButton)).click();
			LOGGER.info("Clicked on Behaviour Competencies screen");
			ExtentCucumberAdapter.addTestStepLog("Clicked on Behaviour Competencies screen");
			while(true) {
				try {
					if(viewMoreButtonInBehaviourCompetenciesTab.isEmpty()) {
						LOGGER.info("Reached end of content in Behaviour Competencies screen");
						ExtentCucumberAdapter.addTestStepLog("Reached end of content in Behaviour Competencies screen");
						break;
					}
			js.executeScript("arguments[0].scrollIntoView(true);", viewMoreButtonInBehaviourCompetenciesTab.get(0));
			String ViewMoreBtnText = wait.until(ExpectedConditions.visibilityOf(viewMoreButtonInBehaviourCompetenciesTab.get(0))).getText();
			viewMoreButtonInBehaviourCompetenciesTab.get(0).click();
			js.executeScript("arguments[0].scrollIntoView(true);", viewMoreButtonInBehaviourCompetenciesTab.get(0));
			LOGGER.info("Clicked on "+ ViewMoreBtnText +" button in Behaviour Competencies screen");
			ExtentCucumberAdapter.addTestStepLog("Clicked on "+ ViewMoreBtnText +" button in Behaviour Competencies screen");
			// PERFORMANCE: Replaced Thread.sleep(2000) with smart UI stability wait
			PerformanceUtils.waitForUIStability(driver, 2);
				} catch(StaleElementReferenceException e) {
					LOGGER.info("Reached end of content in Behaviour Competencies screen");
					ExtentCucumberAdapter.addTestStepLog("Reached end of content in Behaviour Competencies screen");
					break;
				}	
			}
		String behaviourDataText = wait.until(ExpectedConditions.visibilityOf(behaviourData)).getText();
		ProfileBehaviouralCompetencies = behaviourDataText;
//			LOGGER.info("Data present in Behaviour Competencies screen : \n" + behaviourDataText);
//			ExtentCucumberAdapter.addTestStepLog("Data present in Behaviour Competencies screen : \n" + behaviourDataText);
	} catch (Exception e) {
		ScreenshotHandler.captureFailureScreenshot("validate_data_in_behavioural_competencies_tab", e);
		LOGGER.error("❌ Failed to validate data in Behaviour Competencies screen - Method: validate_data_in_behavioural_competencies_tab", e);
		e.printStackTrace();
		Assert.fail("Issue in validating data in Behaviour Competencies screen in Profile Details Popup in Job Mapping page...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in validating data in Behaviour Competencies screen in Profile Details Popup in Job Mapping page...Please Investigate!!!");
	}
}

	
public void validate_data_in_skills_tab() {
	try {
		js.executeScript("arguments[0].scrollIntoView(true);", roleSummary);
		// PERFORMANCE: Replaced Thread.sleep(2000) with smart page ready wait
		PerformanceUtils.waitForPageReady(driver, 2);
		wait.until(ExpectedConditions.elementToBeClickable(skillsTabButton)).click();
			LOGGER.info("Clicked on Skills screen");
			ExtentCucumberAdapter.addTestStepLog("Clicked on Skills screen");
			while(true) {
				try {
					if(viewMoreButtonInSkillsTab.isEmpty()) {
						LOGGER.info("Reached end of content in Skills screen");
						ExtentCucumberAdapter.addTestStepLog("Reached end of content in Skills screen");
						break;
					}
			js.executeScript("arguments[0].scrollIntoView(true);", viewMoreButtonInSkillsTab.get(0));
			String ViewMoreBtnText = wait.until(ExpectedConditions.visibilityOf(viewMoreButtonInSkillsTab.get(0))).getText();
			viewMoreButtonInSkillsTab.get(0).click();
			js.executeScript("arguments[0].scrollIntoView(true);", viewMoreButtonInSkillsTab.get(0));
			LOGGER.info("Clicked on "+ ViewMoreBtnText +" button in Skills screen");
			ExtentCucumberAdapter.addTestStepLog("Clicked on "+ ViewMoreBtnText +" button in Skills screen");
			// PERFORMANCE: Replaced Thread.sleep(2000) with smart UI stability wait
			PerformanceUtils.waitForUIStability(driver, 2);
				} catch(StaleElementReferenceException e) {
					LOGGER.info("Reached end of content in Skills screen");
					ExtentCucumberAdapter.addTestStepLog("Reached end of content in Skills screen");
					break;
				}	
			}
			String skillsDataText = wait.until(ExpectedConditions.visibilityOf(skillsData)).getText();
		ProfileSkills = skillsDataText;
//			LOGGER.info("Data present in Skills screen : \n" + skillsDataText);
//			ExtentCucumberAdapter.addTestStepLog("Data present in Skills screen : \n" + skillsDataText);
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_data_in_skills_tab", e);
		LOGGER.error("❌ Failed to validate data in Skills screen - Method: validate_data_in_skills_tab", e);
		e.printStackTrace();
		Assert.fail("Issue in validating data in Skills screen in Profile Details Popup in Job Mapping page...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in validating data in Skills screen in Profile Details Popup in Job Mapping page...Please Investigate!!!");
	}
	    
	}

	
	public void user_should_verify_publish_profile_button_is_available_on_popup_screen() {
		try {
		wait.until(ExpectedConditions.elementToBeClickable(publishProfileButton)).isDisplayed();
		LOGGER.info("Publish button is displaying on the Profile Details Popup and is clickable");
		ExtentCucumberAdapter.addTestStepLog("Publish button is displaying on the Profile Details Popup and is clickable");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_verify_publish_profile_button_is_available_on_popup_screen", e);
			LOGGER.error("❌ Failed to verify Publish Profile button on profile details popup - Method: user_should_verify_publish_profile_button_is_available_on_popup_screen", e);
			e.printStackTrace();
			Assert.fail("Issue in verifying Publish Profile button on profile details popup screen in Job Mapping page....Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying Publish Profile button on profile details popup screen in Job Mapping page....Please Investigate!!!");
		}
	    
	}


}
