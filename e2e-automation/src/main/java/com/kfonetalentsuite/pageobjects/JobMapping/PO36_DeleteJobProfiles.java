package com.kfonetalentsuite.pageobjects.JobMapping;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import com.kfonetalentsuite.utils.JobMapping.PageObjectHelper;

public class PO36_DeleteJobProfiles extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO36_DeleteJobProfiles.class);
	// LOCATORS
	
	// Delete Button in Job Mapping screen
	private static final By DELETE_BUTTON = By.id("delete-selected-mappings-btn");
	
	// Delete Confirmation Popup
	private static final By DELETE_POPUP_TITLE = By.xpath("//h2[@id='delete-modal-title']");
	private static final By DELETE_POPUP_MESSAGE = By.xpath("//div[@role='document']//p[contains(text(),'Are you sure you want to delete')]");
	private static final By DELETE_POPUP_CANCEL_BTN = By.xpath("//div[@role='document']//button[text()='Cancel']");
	private static final By DELETE_POPUP_CONFIRM_BTN = By.xpath("//div[@role='document']//button[text()='Delete']");
	
	// Delete Success Message
	private static final By DELETE_SUCCESS_TITLE = By.xpath("//p[text()='Job Profiles Deleted Successfully']");
	private static final By DELETE_SUCCESS_MSG = By.xpath("//p[contains(text(),'Successfully deleted')]");

	public PO36_DeleteJobProfiles() {
		super();
	}

	public void user_should_verify_delete_button_is_disabled() {
		try {
			PageObjectHelper.waitForPageReady(driver, 2);
			waitForSpinners();
			
			WebElement deleteBtn = waitForElement(DELETE_BUTTON, 10);
			Assert.assertTrue(deleteBtn.isDisplayed(), "Delete button should be displayed");
			
			// Check if button has disabled attribute
			String disabledAttr = deleteBtn.getAttribute("disabled");
			boolean isDisabled = disabledAttr != null || deleteBtn.getAttribute("class").contains("cursor-not-allowed");
			
			Assert.assertTrue(isDisabled, "Delete button should be disabled when no profiles are selected");
			LOGGER.info("✅ Verified Delete button is DISABLED");
			
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_delete_button_is_disabled", 
					"Issue verifying Delete button is disabled", e);
		}
	}

	public void user_should_verify_delete_button_is_enabled() {
		try {
			PageObjectHelper.waitForPageReady(driver, 2);
			waitForSpinners();
			
			WebElement deleteBtn = waitForElement(DELETE_BUTTON, 10);
			Assert.assertTrue(deleteBtn.isDisplayed(), "Delete button should be displayed");
			
			// Check if button is NOT disabled
			String disabledAttr = deleteBtn.getAttribute("disabled");
			boolean isEnabled = disabledAttr == null && !deleteBtn.getAttribute("class").contains("cursor-not-allowed");
			
			Assert.assertTrue(isEnabled, "Delete button should be enabled when profiles are selected");
			LOGGER.info("✅ Verified Delete button is ENABLED");
			
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_delete_button_is_enabled", 
					"Issue verifying Delete button is enabled", e);
		}
	}

	public void click_on_delete_button_in_job_mapping() {
		try {
			PageObjectHelper.waitForPageReady(driver, 2);
			waitForSpinners();
			
			WebElement deleteBtn = PageObjectHelper.waitForClickable(wait, DELETE_BUTTON);
			clickElement(deleteBtn);
			
			LOGGER.info("Clicked on Delete button in Job Mapping screen");
			safeSleep(500); // Wait for popup to appear
			
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_delete_button_in_job_mapping", 
					"Issue clicking Delete button in Job Mapping", e);
		}
	}

	public void verify_delete_confirmation_popup_is_displayed() {
		try {
			PageObjectHelper.waitForPageReady(driver, 2);
			
			// Verify popup title
			WebElement popupTitle = waitForElement(DELETE_POPUP_TITLE, 10);
			Assert.assertTrue(popupTitle.isDisplayed(), "Delete Confirmation popup title should be displayed");
			Assert.assertEquals(popupTitle.getText(), "Delete Selected Jobs", "Popup title should match");
			
			// Verify popup message
			WebElement popupMessage = waitForElement(DELETE_POPUP_MESSAGE, 5);
			Assert.assertTrue(popupMessage.isDisplayed(), "Delete Confirmation popup message should be displayed");
			
			// Verify Cancel and Delete buttons are present
			Assert.assertTrue(waitForElement(DELETE_POPUP_CANCEL_BTN, 5).isDisplayed(), "Cancel button should be displayed");
			Assert.assertTrue(waitForElement(DELETE_POPUP_CONFIRM_BTN, 5).isDisplayed(), "Delete button should be displayed");
			
			LOGGER.info("✅ Delete Confirmation popup is displayed with title: 'Delete Selected Jobs'");
			
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_delete_confirmation_popup_is_displayed", 
					"Issue verifying Delete Confirmation popup", e);
		}
	}

	public void click_on_cancel_button_on_delete_confirmation_popup() {
		try {
			PageObjectHelper.waitForPageReady(driver, 2);
			
			WebElement cancelBtn = PageObjectHelper.waitForClickable(wait, DELETE_POPUP_CANCEL_BTN);
			clickElement(cancelBtn);
			
			LOGGER.info("Clicked on Cancel button on Delete Confirmation popup");
			safeSleep(500); // Wait for popup to close
			
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_cancel_button_on_delete_confirmation_popup", 
					"Issue clicking Cancel button on Delete Confirmation popup", e);
		}
	}

	public void click_on_delete_button_on_delete_confirmation_popup() {
		try {
			PageObjectHelper.waitForPageReady(driver, 2);
			
			WebElement deleteBtn = PageObjectHelper.waitForClickable(wait, DELETE_POPUP_CONFIRM_BTN);
			clickElement(deleteBtn);
			
			LOGGER.info("Clicked on Delete button on Delete Confirmation popup");
			waitForSpinners();
			
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_delete_button_on_delete_confirmation_popup", 
					"Issue clicking Delete button on Delete Confirmation popup", e);
		}
	}

	public void user_should_verify_delete_success_popup_appears_on_screen() {
		try {
			PageObjectHelper.waitForPageReady(driver, 2);
			
			// Verify success title
			WebElement successTitle = waitForElement(DELETE_SUCCESS_TITLE, 15);
			Assert.assertTrue(successTitle.isDisplayed(), "Delete success title should be displayed");
			Assert.assertEquals(successTitle.getText(), "Job Profiles Deleted", "Success title should match");
			
			// Verify success message
			WebElement successMsg = waitForElement(DELETE_SUCCESS_MSG, 5);
			Assert.assertTrue(successMsg.isDisplayed(), "Delete success message should be displayed");
			
			String msgText = successMsg.getText();
			LOGGER.info("✅ Delete success popup appeared: 'Job Profiles Deleted Successfully' - " + msgText);
			
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_delete_success_popup_appears_on_screen", 
					"Issue verifying Delete success popup", e);
		}
	}

}
