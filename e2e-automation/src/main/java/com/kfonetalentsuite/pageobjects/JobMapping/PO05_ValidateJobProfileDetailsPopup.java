package com.kfonetalentsuite.pageobjects.JobMapping;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.PageObjectHelper;

public class PO05_ValidateJobProfileDetailsPopup extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO05_ValidateJobProfileDetailsPopup.class);

	public static String ProfileDetails;
	public static String ProfileRoleSummary;
	public static String ProfileResponsibilities;
	public static String ProfileBehaviouralCompetencies;
	public static String ProfileSkills;

	private static final By PROFILE_HEADER = By.xpath("//h2[@id='summary-modal']//p");
	private static final By PROFILE_DETAILS = By.xpath("//div[@id='details-container']//div[contains(@class,'mt-2')]");
	private static final By PROFILE_LEVEL_DROPDOWN = By.xpath("//select[contains(@id,'profileLevel') or @id='profile-level']");
	private static final By ROLE_SUMMARY = By.xpath("//div[@id='role-summary-container']//p");
	private static final By VIEW_MORE_RESPONSIBILITIES = By.xpath("//div[@id='responsibilities-panel']//button[contains(text(),'View')]");
	private static final By RESPONSIBILITIES_DATA = By.xpath("//div[@id='responsibilities-panel']//div[@id='item-container']");
	private static final By BEHAVIOUR_TAB_BTN = By.xpath("//button[contains(text(),'BEHAVIOUR')]");
	private static final By VIEW_MORE_BEHAVIOUR = By.xpath("//div[@id='behavioural-panel']//button[contains(text(),'View')]");
	private static final By BEHAVIOUR_DATA = By.xpath("//div[@id='behavioural-panel']//div[@id='item-container']");
	private static final By SKILLS_TAB_BTN = By.xpath("//button[text()='SKILLS']");
	private static final By VIEW_MORE_SKILLS = By.xpath("//div[@id='skills-panel']//button[contains(text(),'View')]");
	private static final By SKILLS_DATA = By.xpath("//div[@id='skills-panel']//div[@id='item-container']");
	private static final By PUBLISH_PROFILE_BTN = By.xpath("//button[@id='publish-job-profile']");
	private static final By POPUP_CONTAINER = By.xpath("//div[contains(@class, 'modal-body') or contains(@class, 'popup-content') or contains(@class, 'dialog-content')]");

	public PO05_ValidateJobProfileDetailsPopup() {
		super();
	}

	public void user_is_on_profile_details_popup() {
		PageObjectHelper.log(LOGGER, "User is on Profile Details Popup");
	}

	public void verify_profile_header_matches_with_matched_profile_name() {
		try {
			String profileHeaderName = getElementText(PROFILE_HEADER);
			Assert.assertEquals(PO15_ValidateRecommendedProfileDetails.matchedSuccessPrflName.get(), profileHeaderName);
			PageObjectHelper.log(LOGGER, "Profile header on the details popup: " + profileHeaderName);
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_profile_header_matches_with_matched_profile_name", "Failed to verify profile details popup header", e);
		}
	}

	public void verify_profile_details_displaying_on_the_popup() {
		try {
			String profileDetailsText = getElementText(PROFILE_DETAILS);
			ProfileDetails = profileDetailsText;
			PageObjectHelper.log(LOGGER, "Profile Details displaying on the popup screen:\n" + profileDetailsText);
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_profile_details_displaying_on_the_popup", "Failed to display profile details on the popup screen", e);
		}
	}

	public void user_should_verify_profile_level_dropdown_is_available_and_validate_levels_present_inside_dropdown() {
		try {
			WebElement dropdown = waitForElement(PROFILE_LEVEL_DROPDOWN);
			if (dropdown.isEnabled()) {
				dropdown.click();
				Select select = new Select(dropdown);
				List<WebElement> allOptions = select.getOptions();
				StringBuilder levels = new StringBuilder();
				for (WebElement option : allOptions) {
					levels.append(option.getText()).append(", ");
				}
				dropdown.click();
				PageObjectHelper.log(LOGGER, "Profile Level dropdown verified. Levels: " + levels.toString().replaceAll(", $", ""));
			} else {
				PageObjectHelper.log(LOGGER, "No Profile Levels available for profile: " + PO15_ValidateRecommendedProfileDetails.matchedSuccessPrflName.get());
			}
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_profile_level_dropdown_is_available_and_validate_levels_present_inside_dropdown", "Failed to validate profile level dropdown", e);
		}
	}

	public void validate_role_summary_is_displaying() {
		try {
			String roleSummaryText = getElementText(ROLE_SUMMARY);
			ProfileRoleSummary = roleSummaryText.split(": ", 2)[1].trim();
			PageObjectHelper.log(LOGGER, "Role summary of Matched Success Profile: " + ProfileRoleSummary);
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "validate_role_summary_is_displaying", "Failed to validate Role Summary in Profile Details Popup", e);
		}
	}

	public void validate_data_in_responsibilities_tab() {
		try {
			PerformanceUtils.waitForPageReady(driver, 2);
			List<WebElement> viewMoreButtons = driver.findElements(VIEW_MORE_RESPONSIBILITIES);
			if (!viewMoreButtons.isEmpty()) {
				viewMoreButtons.get(0).click();
			}

			expandAllViewMoreButtons(VIEW_MORE_RESPONSIBILITIES);

			String responsibilitiesDataText = getElementText(RESPONSIBILITIES_DATA);
			ProfileResponsibilities = responsibilitiesDataText;
			PageObjectHelper.log(LOGGER, "Responsibilities data validated successfully");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "validate_data_in_responsibilities_tab", "Failed to validate data in Responsibilities screen", e);
		}
	}

	public void validate_data_in_behavioural_competencies_tab() {
		try {
			scrollToElement(driver.findElement(ROLE_SUMMARY));
			PerformanceUtils.waitForPageReady(driver, 3);
			clickElement(BEHAVIOUR_TAB_BTN);

			expandAllViewMoreButtons(VIEW_MORE_BEHAVIOUR);

			String behaviourDataText = getElementText(BEHAVIOUR_DATA);
			ProfileBehaviouralCompetencies = behaviourDataText;
			PageObjectHelper.log(LOGGER, "Behavioural Competencies data validated successfully");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "validate_data_in_behavioural_competencies_tab", "Failed to validate data in Behaviour Competencies screen", e);
		}
	}

	public void validate_data_in_skills_tab() {
		try {
			scrollToElement(driver.findElement(ROLE_SUMMARY));
			PerformanceUtils.waitForPageReady(driver, 2);
			clickElement(SKILLS_TAB_BTN);

			expandAllViewMoreButtons(VIEW_MORE_SKILLS);

			String skillsDataText = getElementText(SKILLS_DATA);
			ProfileSkills = skillsDataText;
			PageObjectHelper.log(LOGGER, "Skills data validated successfully");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "validate_data_in_skills_tab", "Failed to validate data in Skills screen", e);
		}
	}

	public void user_should_verify_publish_profile_button_is_available_on_popup_screen() {
		try {
			try {
				WebElement publishBtn = driver.findElement(PUBLISH_PROFILE_BTN);
				js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'end', inline: 'nearest'});", publishBtn);
				Thread.sleep(1000);
			} catch (Exception scrollEx1) {
				try {
					WebElement popupContainer = driver.findElement(POPUP_CONTAINER);
					js.executeScript("arguments[0].scrollTop = arguments[0].scrollHeight;", popupContainer);
					Thread.sleep(1000);
				} catch (Exception scrollEx2) {
					scrollToBottom();
					Thread.sleep(500);
				}
			}

			PerformanceUtils.waitForPageReady(driver, 2);
			Thread.sleep(500);

			boolean isButtonDisplayed = wait.until(ExpectedConditions.elementToBeClickable(PUBLISH_PROFILE_BTN)).isDisplayed();
			if (!isButtonDisplayed) {
				throw new Exception("Publish button found but not displayed");
			}

			PageObjectHelper.log(LOGGER, "Publish button is displaying on the Profile Details Popup and is clickable");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_publish_profile_button_is_available_on_popup_screen", "Failed to verify Publish Profile button", e);
		}
	}
}
