package com.kfonetalentsuite.pageobjects.JobMapping;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.PageObjectHelper;

public class PO14_ValidateProfileLevelFunctionality extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO14_ValidateProfileLevelFunctionality.class);

	public static ThreadLocal<String> changedlevelvalue = ThreadLocal.withInitial(() -> "NOT_SET");

	private static final By PROFILE_LEVEL_DROPDOWN = By.xpath("//select[contains(@id,'profileLevel') or @id='profile-level']");
	private static final By PROFILE_HEADER = By.xpath("//h2[@id='summary-modal']//p");
	private static final By PROFILE_1_TITLE = By.xpath("//div[@class='shadow']//div[contains(@id,'card-title')]");

	public PO14_ValidateProfileLevelFunctionality() throws IOException {
		super();
	}

	public void change_profile_level() {
		WebElement dropdown = waitForElement(PROFILE_LEVEL_DROPDOWN);
		if (dropdown.isEnabled()) {
			scrollToTop();
			try {
				clickElement(dropdown);
				PerformanceUtils.waitForUIStability(driver, 2);
				Select select = new Select(dropdown);
				List<WebElement> allOptions = select.getOptions();
				for (WebElement option : allOptions) {
					changedlevelvalue.set(option.getText());
				}
				select.selectByVisibleText(changedlevelvalue.get());
				waitForSpinners();
				PerformanceUtils.waitForPageReady(driver, 4);
				PageObjectHelper.log(LOGGER, "Successfully changed Profile Level to: " + changedlevelvalue.get());
			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER, "change_profile_level", "Issue changing Profile Level", e);
			}

			try {
				clickElement(dropdown);
				PageObjectHelper.log(LOGGER, "Profile Level dropdown closed successfully");
			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER, "change_profile_level", "Issue clicking Profile Level dropdown to close it", e);
			}
		}
	}

	public void change_profile_level_in_job_comparison_page() {
		WebElement dropdown = waitForElement(PROFILE_LEVEL_DROPDOWN);
		if (dropdown.isEnabled()) {
			scrollToTop();
			try {
				clickElement(dropdown);
				PerformanceUtils.waitForUIStability(driver, 2);
				Select select = new Select(dropdown);
				List<WebElement> allOptions = select.getOptions();
				for (WebElement option : allOptions) {
					changedlevelvalue.set(option.getText());
				}
				select.selectByVisibleText(changedlevelvalue.get());
				waitForSpinners();
				PerformanceUtils.waitForPageReady(driver, 4);
				PageObjectHelper.log(LOGGER, "Successfully changed Profile Level to: " + changedlevelvalue.get());
			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER, "change_profile_level_in_job_comparison_page", "Issue changing Profile Level in Job Comparison Page", e);
			}
		}
	}

	public void user_should_verify_profile_header_matches_with_changed_profile_level_in_job_profile_details_popup() {
		try {
			waitForSpinners();
			PerformanceUtils.waitForPageReady(driver, 2);
			String profileHeaderName = getElementText(PROFILE_HEADER);
			Assert.assertEquals(profileHeaderName, changedlevelvalue.get());
			PageObjectHelper.log(LOGGER, "Profile header on details popup: " + profileHeaderName + " matches with changed profile level: " + changedlevelvalue.get());
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_profile_header_matches_with_changed_profile_level_in_job_profile_details_popup", "Issue verifying profile details popup header matches with changed profile level", e);
		}
	}

	public void user_should_verify_recommended_profile_name_matches_with_changed_profile_level() {
		try {
			waitForSpinners();
			PerformanceUtils.waitForPageReady(driver, 2);
			String profileTitle = getElementText(PROFILE_1_TITLE);
			Assert.assertEquals(profileTitle, changedlevelvalue.get());
			PageObjectHelper.log(LOGGER, "Recommended Profile Name in Job Compare page matches with Changed Profile Level: " + changedlevelvalue.get());
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_recommended_profile_name_matches_with_changed_profile_level", "Issue verifying Recommended Profile Name matches with changed profile level", e);
		}
	}

	public void user_is_in_job_comparison_page_after_changing_profile_level() {
		PerformanceUtils.waitForPageReady(driver, 1);
		PageObjectHelper.log(LOGGER, "User is in Job Comparison Page after changing profile level");
	}
}
