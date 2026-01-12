package com.kfonetalentsuite.pageobjects.JobMapping;

import static com.kfonetalentsuite.pageobjects.JobMapping.BasePageObject.Locators.Comparison.*;
import static com.kfonetalentsuite.pageobjects.JobMapping.BasePageObject.Locators.ProfileDetails.*;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import com.kfonetalentsuite.utils.JobMapping.Utilities;
public class PO11_ProfileLevelFunctionality extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO11_ProfileLevelFunctionality.class);

	public static ThreadLocal<String> changedlevelvalue = ThreadLocal.withInitial(() -> "NOT_SET");
	public PO11_ProfileLevelFunctionality() {
		super();
	}

	public void change_profile_level() {
		WebElement dropdown = waitForElement(PROFILE_LEVEL_DROPDOWN);
		if (dropdown.isEnabled()) {
			scrollToTop();
			try {
				clickElement(dropdown);
				Utilities.waitForUIStability(driver, 2);
				Select select = new Select(dropdown);
				List<WebElement> allOptions = select.getOptions();
				for (WebElement option : allOptions) {
					changedlevelvalue.set(option.getText());
				}
				select.selectByVisibleText(changedlevelvalue.get());
				waitForSpinners();
				Utilities.waitForPageReady(driver, 4);
				LOGGER.info("Successfully changed Profile Level to: " + changedlevelvalue.get());
			} catch (Exception e) {
				Utilities.handleError(LOGGER, "change_profile_level", "Issue changing Profile Level", e);
			}

			try {
				clickElement(dropdown);
				LOGGER.info("Profile Level dropdown closed");
			} catch (Exception e) {
				Utilities.handleError(LOGGER, "change_profile_level", "Issue clicking Profile Level dropdown to close it", e);
			}
		}
	}

	public void change_profile_level_in_job_comparison_page() {
		WebElement dropdown = waitForElement(PROFILE_LEVEL_DROPDOWN);
		if (dropdown.isEnabled()) {
			scrollToTop();
			try {
				clickElement(dropdown);
				Utilities.waitForUIStability(driver, 2);
				Select select = new Select(dropdown);
				List<WebElement> allOptions = select.getOptions();
				for (WebElement option : allOptions) {
					changedlevelvalue.set(option.getText());
				}
				select.selectByVisibleText(changedlevelvalue.get());
				waitForSpinners();
				Utilities.waitForPageReady(driver, 4);
				LOGGER.info("Successfully changed Profile Level to: " + changedlevelvalue.get());
			} catch (Exception e) {
				Utilities.handleError(LOGGER, "change_profile_level_in_job_comparison_page", "Issue changing Profile Level in Job Comparison Page", e);
			}
		}
	}

	public void user_should_verify_profile_header_matches_with_changed_profile_level_in_job_profile_details_popup() {
		try {
			waitForSpinners();
			Utilities.waitForPageReady(driver, 2);
			String profileHeaderName = getElementText(PROFILE_HEADER_TEXT);
			Assert.assertEquals(profileHeaderName, changedlevelvalue.get());
			LOGGER.info("Profile header on details popup: " + profileHeaderName + " matches with changed profile level: " + changedlevelvalue.get());
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_should_verify_profile_header_matches_with_changed_profile_level_in_job_profile_details_popup", "Issue verifying profile details popup header matches with changed profile level", e);
		}
	}

	public void user_should_verify_recommended_profile_name_matches_with_changed_profile_level() {
		try {
			waitForSpinners();
			Utilities.waitForPageReady(driver, 2);
			String profileTitle = getElementText(CARD_TITLE);
			Assert.assertEquals(profileTitle, changedlevelvalue.get());
			LOGGER.info("Recommended Profile Name in Job Compare page matches with Changed Profile Level: " + changedlevelvalue.get());
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_should_verify_recommended_profile_name_matches_with_changed_profile_level", "Issue verifying Recommended Profile Name matches with changed profile level", e);
		}
	}

	public void user_is_in_job_comparison_page_after_changing_profile_level() {
		Utilities.waitForPageReady(driver, 1);
		LOGGER.info("User is in Job Comparison Page after changing profile level");
	}
}

