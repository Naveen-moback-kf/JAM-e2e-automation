package com.kfonetalentsuite.pageobjects.JobMapping;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.openqa.selenium.By;
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
import com.kfonetalentsuite.utils.PageObjectHelper;
import com.kfonetalentsuite.webdriverManager.DriverManager;

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
		PageObjectHelper.log(LOGGER, "User is on Profile Details Popup");
	}
	
	public void verify_profile_header_matches_with_matched_profile_name() {
		try {
			String profileHeaderName = wait.until(ExpectedConditions.visibilityOf(profileHeader)).getText();
			Assert.assertEquals(PO15_ValidateRecommendedProfileDetails.matchedSuccessPrflName.get(), profileHeaderName);
			PageObjectHelper.log(LOGGER, "Profile header on the details popup: " + profileHeaderName);
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_profile_header_matches_with_matched_profile_name", e);
			PageObjectHelper.handleError(LOGGER, "verify_profile_header_matches_with_matched_profile_name",
					"Failed to verify profile details popup header", e);
		}
	}
	
	public void verify_profile_details_displaying_on_the_popup() {
		try {
			String profileDetailsText = wait.until(ExpectedConditions.visibilityOf(profileDetails)).getText();
			ProfileDetails = profileDetailsText;
			PageObjectHelper.log(LOGGER, "Profile Details displaying on the popup screen:\n" + profileDetailsText);
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_profile_details_displaying_on_the_popup", e);
			PageObjectHelper.handleError(LOGGER, "verify_profile_details_displaying_on_the_popup",
					"Failed to display profile details on the popup screen", e);
		}
	}

	
	public void user_should_verify_profile_level_dropdown_is_available_and_validate_levels_present_inside_dropdown() {
		try {
			if(wait.until(ExpectedConditions.visibilityOf(profileLevelDropdown)).isEnabled()) {
				wait.until(ExpectedConditions.elementToBeClickable(profileLevelDropdown)).click();
				Select dropdown = new Select(profileLevelDropdown);
				List<WebElement> allOptions = dropdown.getOptions();
				StringBuilder levels = new StringBuilder();
				for(WebElement option : allOptions){
					levels.append(option.getText()).append(", ");
				}
				wait.until(ExpectedConditions.elementToBeClickable(profileLevelDropdown)).click(); // Close dropdown
				PageObjectHelper.log(LOGGER, "Profile Level dropdown verified. Levels: " + levels.toString().replaceAll(", $", ""));
			} else {
				PageObjectHelper.log(LOGGER, "No Profile Levels available for profile: " + PO15_ValidateRecommendedProfileDetails.matchedSuccessPrflName);
			}
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_verify_profile_level_dropdown_is_available_and_validate_levels_present_inside_dropdown", e);
			PageObjectHelper.handleError(LOGGER, "user_should_verify_profile_level_dropdown_is_available_and_validate_levels_present_inside_dropdown",
					"Failed to validate profile level dropdown", e);
		}	
	}

	
	public void validate_role_summary_is_displaying() {
		try {
			String roleSummaryText = wait.until(ExpectedConditions.visibilityOf(roleSummary)).getText();
			ProfileRoleSummary = roleSummaryText.split(": ", 2)[1].trim();
			PageObjectHelper.log(LOGGER, "Role summary of Matched Success Profile: " + ProfileRoleSummary);
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_role_summary_is_displaying", e);
			PageObjectHelper.handleError(LOGGER, "validate_role_summary_is_displaying",
					"Failed to validate Role Summary in Profile Details Popup", e);
		}
	}

	
public void validate_data_in_responsibilities_tab() {
	try {
		PerformanceUtils.waitForPageReady(driver, 2);
		wait.until(ExpectedConditions.elementToBeClickable(viewMoreButtonInResponsibilitiesTab.get(0))).click();
		
		while(true) {
			try {
				if(viewMoreButtonInResponsibilitiesTab.isEmpty()) {
					break;
				}
				js.executeScript("arguments[0].scrollIntoView(true);", viewMoreButtonInResponsibilitiesTab.get(0));
				viewMoreButtonInResponsibilitiesTab.get(0).click();
				js.executeScript("arguments[0].scrollIntoView(true);", viewMoreButtonInResponsibilitiesTab.get(0));
				PerformanceUtils.waitForUIStability(driver, 2);
			} catch(StaleElementReferenceException e) {
				break;
			}	
		}
		
		String responsibilitiesDataText = wait.until(ExpectedConditions.visibilityOf(responisbilitiesData)).getText();
		ProfileResponsibilities = responsibilitiesDataText;
		PageObjectHelper.log(LOGGER, "Responsibilities data validated successfully");
	} catch (Exception e) {
		ScreenshotHandler.captureFailureScreenshot("validate_data_in_responsibilities_tab", e);
		PageObjectHelper.handleError(LOGGER, "validate_data_in_responsibilities_tab",
				"Failed to validate data in Responsibilities screen", e);
	}
}
	
	
public void validate_data_in_behavioural_competencies_tab() {
	try {
		js.executeScript("arguments[0].scrollIntoView(true);", roleSummary);
		PerformanceUtils.waitForPageReady(driver, 3);
		wait.until(ExpectedConditions.elementToBeClickable(behaviourCompetenciesTabButton)).click();
		
		while(true) {
			try {
				if(viewMoreButtonInBehaviourCompetenciesTab.isEmpty()) {
					break;
				}
				js.executeScript("arguments[0].scrollIntoView(true);", viewMoreButtonInBehaviourCompetenciesTab.get(0));
				viewMoreButtonInBehaviourCompetenciesTab.get(0).click();
				js.executeScript("arguments[0].scrollIntoView(true);", viewMoreButtonInBehaviourCompetenciesTab.get(0));
				PerformanceUtils.waitForUIStability(driver, 2);
			} catch(StaleElementReferenceException e) {
				break;
			}	
		}
		
		String behaviourDataText = wait.until(ExpectedConditions.visibilityOf(behaviourData)).getText();
		ProfileBehaviouralCompetencies = behaviourDataText;
		PageObjectHelper.log(LOGGER, "Behavioural Competencies data validated successfully");
	} catch (Exception e) {
		ScreenshotHandler.captureFailureScreenshot("validate_data_in_behavioural_competencies_tab", e);
		PageObjectHelper.handleError(LOGGER, "validate_data_in_behavioural_competencies_tab",
				"Failed to validate data in Behaviour Competencies screen", e);
	}
}

	
public void validate_data_in_skills_tab() {
	try {
		js.executeScript("arguments[0].scrollIntoView(true);", roleSummary);
		PerformanceUtils.waitForPageReady(driver, 2);
		wait.until(ExpectedConditions.elementToBeClickable(skillsTabButton)).click();
		
		while(true) {
			try {
				if(viewMoreButtonInSkillsTab.isEmpty()) {
					break;
				}
				js.executeScript("arguments[0].scrollIntoView(true);", viewMoreButtonInSkillsTab.get(0));
				viewMoreButtonInSkillsTab.get(0).click();
				js.executeScript("arguments[0].scrollIntoView(true);", viewMoreButtonInSkillsTab.get(0));
				PerformanceUtils.waitForUIStability(driver, 2);
			} catch(StaleElementReferenceException e) {
				break;
			}	
		}
		
		String skillsDataText = wait.until(ExpectedConditions.visibilityOf(skillsData)).getText();
		ProfileSkills = skillsDataText;
		PageObjectHelper.log(LOGGER, "Skills data validated successfully");
	} catch (Exception e) {
		ScreenshotHandler.captureFailureScreenshot("validate_data_in_skills_tab", e);
		PageObjectHelper.handleError(LOGGER, "validate_data_in_skills_tab",
				"Failed to validate data in Skills screen", e);
	}
}

	
	/**
	 * Verifies Publish Profile button is available on popup screen
	 * ENHANCED FOR HEADLESS MODE: Scrolls popup to bottom to ensure button is visible
	 */
	public void user_should_verify_publish_profile_button_is_available_on_popup_screen() {
		try {
			// Try multiple scrolling strategies to ensure button comes into view
			try {
				js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'end', inline: 'nearest'});", publishProfileButton);
				Thread.sleep(1000);
			} catch (Exception scrollEx1) {
				try {
					WebElement popupContainer = driver.findElement(By.xpath("//div[contains(@class, 'modal-body') or contains(@class, 'popup-content') or contains(@class, 'dialog-content')]"));
					js.executeScript("arguments[0].scrollTop = arguments[0].scrollHeight;", popupContainer);
					Thread.sleep(1000);
				} catch (Exception scrollEx2) {
					js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
					Thread.sleep(500);
				}
			}
			
			PerformanceUtils.waitForPageReady(driver, 2);
			Thread.sleep(500);
			
			boolean isButtonDisplayed = wait.until(ExpectedConditions.elementToBeClickable(publishProfileButton)).isDisplayed();
			if (!isButtonDisplayed) {
				throw new Exception("Publish button found but not displayed");
			}
			
			PageObjectHelper.log(LOGGER, "✅ Publish button is displaying on the Profile Details Popup and is clickable");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_verify_publish_profile_button_is_available_on_popup_screen", e);
			PageObjectHelper.handleError(LOGGER, "user_should_verify_publish_profile_button_is_available_on_popup_screen",
					"Failed to verify Publish Profile button on profile details popup", e);
		}
	}


}
