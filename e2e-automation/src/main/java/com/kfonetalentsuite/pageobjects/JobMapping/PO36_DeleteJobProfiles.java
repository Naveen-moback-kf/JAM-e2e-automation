package com.kfonetalentsuite.pageobjects.JobMapping;
import static com.kfonetalentsuite.pageobjects.JobMapping.BasePageObject.Locators.JobMappingScreen.*;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import com.kfonetalentsuite.utils.common.Utilities;

public class PO36_DeleteJobProfiles extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO36_DeleteJobProfiles.class);
	// LOCATORS
	
	// Delete Button in Job Mapping screen
	// Delete Confirmation Popup
	// Delete Success Message
	public PO36_DeleteJobProfiles() {
		super();
	}

	public void user_should_verify_delete_button_is_disabled() {
		try {
			Utilities.waitForPageReady(driver, 2);
			waitForSpinners();
			
			WebElement deleteBtn = Utilities.waitForVisible(new WebDriverWait(driver, Duration.ofSeconds(5)), DELETE_BUTTON);
			Assert.assertTrue(deleteBtn.isDisplayed(), "Delete button should be displayed");
			
			// Check if button has disabled attribute
			String disabledAttr = deleteBtn.getAttribute("disabled");
			boolean isDisabled = disabledAttr != null || deleteBtn.getAttribute("class").contains("cursor-not-allowed");
			
			Assert.assertTrue(isDisabled, "Delete button should be disabled when no profiles are selected");
			LOGGER.info("✅ Verified Delete button is DISABLED");
			
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_should_verify_delete_button_is_disabled", 
					"Issue verifying Delete button is disabled", e);
		}
	}

	public void user_should_verify_delete_button_is_enabled() {
		try {
			Utilities.waitForPageReady(driver, 2);
			waitForSpinners();
			
			WebElement deleteBtn = Utilities.waitForVisible(new WebDriverWait(driver, Duration.ofSeconds(5)), DELETE_BUTTON);
			Assert.assertTrue(deleteBtn.isDisplayed(), "Delete button should be displayed");
			
			// Check if button is NOT disabled
			String disabledAttr = deleteBtn.getAttribute("disabled");
			boolean isEnabled = disabledAttr == null && !deleteBtn.getAttribute("class").contains("cursor-not-allowed");
			
			Assert.assertTrue(isEnabled, "Delete button should be enabled when profiles are selected");
			LOGGER.info("✅ Verified Delete button is ENABLED");
			
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_should_verify_delete_button_is_enabled", 
					"Issue verifying Delete button is enabled", e);
		}
	}

	public void click_on_delete_button_in_job_mapping() {
		try {
			Utilities.waitForPageReady(driver, 2);
			waitForSpinners();
			
			WebElement deleteBtn = Utilities.waitForClickable(wait, DELETE_BUTTON);
			clickElement(deleteBtn);
			
			LOGGER.info("Clicked on Delete button in Job Mapping screen");
			safeSleep(500); // Wait for popup to appear
			
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "click_on_delete_button_in_job_mapping", 
					"Issue clicking Delete button in Job Mapping", e);
		}
	}

	public void verify_delete_confirmation_popup_is_displayed() {
		try {
			Utilities.waitForPageReady(driver, 2);
			
			// Verify popup title
			WebElement popupTitle = Utilities.waitForVisible(new WebDriverWait(driver, Duration.ofSeconds(5)), DELETE_POPUP_TITLE);
			Assert.assertTrue(popupTitle.isDisplayed(), "Delete Confirmation popup title should be displayed");
			Assert.assertEquals(popupTitle.getText(), "Delete Selected Jobs", "Popup title should match");
			
			// Verify popup message
			WebElement popupMessage = Utilities.waitForVisible(new WebDriverWait(driver, Duration.ofSeconds(5)), DELETE_POPUP_MESSAGE);
			Assert.assertTrue(popupMessage.isDisplayed(), "Delete Confirmation popup message should be displayed");
			
			// Verify Cancel and Delete buttons are present
			Assert.assertTrue(Utilities.waitForVisible(new WebDriverWait(driver, Duration.ofSeconds(5)), DELETE_POPUP_CANCEL_BTN).isDisplayed(), "Cancel button should be displayed");
			Assert.assertTrue(Utilities.waitForVisible(new WebDriverWait(driver, Duration.ofSeconds(5)), DELETE_POPUP_CONFIRM_BTN).isDisplayed(), "Delete button should be displayed");
			
			LOGGER.info("✅ Delete Confirmation popup is displayed with title: 'Delete Selected Jobs'");
			
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_delete_confirmation_popup_is_displayed", 
					"Issue verifying Delete Confirmation popup", e);
		}
	}

	public void click_on_cancel_button_on_delete_confirmation_popup() {
		try {
			Utilities.waitForPageReady(driver, 2);
			
			WebElement cancelBtn = Utilities.waitForClickable(wait, DELETE_POPUP_CANCEL_BTN);
			clickElement(cancelBtn);
			
			LOGGER.info("Clicked on Cancel button on Delete Confirmation popup");
			safeSleep(500); // Wait for popup to close
			
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "click_on_cancel_button_on_delete_confirmation_popup", 
					"Issue clicking Cancel button on Delete Confirmation popup", e);
		}
	}

	public void click_on_delete_button_on_delete_confirmation_popup() {
		try {
			Utilities.waitForPageReady(driver, 2);
			
			WebElement deleteBtn = Utilities.waitForClickable(wait, DELETE_POPUP_CONFIRM_BTN);
			clickElement(deleteBtn);
			
			LOGGER.info("Clicked on Delete button on Delete Confirmation popup");
			waitForSpinners();
			
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "click_on_delete_button_on_delete_confirmation_popup", 
					"Issue clicking Delete button on Delete Confirmation popup", e);
		}
	}

	public void user_should_verify_delete_success_popup_appears_on_screen() {
		try {
			Utilities.waitForPageReady(driver, 2);
			
			// Verify success title
			WebElement successTitle = Utilities.waitForVisible(new WebDriverWait(driver, Duration.ofSeconds(5)), DELETE_SUCCESS_TITLE);
			Assert.assertTrue(successTitle.isDisplayed(), "Delete success title should be displayed");
			Assert.assertEquals(successTitle.getText(), "Job Profiles Deleted Successfully", "Success title should match");
			
			// Verify success message
			WebElement successMsg = Utilities.waitForVisible(new WebDriverWait(driver, Duration.ofSeconds(5)), DELETE_SUCCESS_MSG);
			Assert.assertTrue(successMsg.isDisplayed(), "Delete success message should be displayed");
			
			String msgText = successMsg.getText();
			LOGGER.info("✅ Delete success popup appeared: 'Job Profiles Deleted Successfully' - " + msgText);
			
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_should_verify_delete_success_popup_appears_on_screen", 
					"Issue verifying Delete success popup", e);
		}
	}

}

