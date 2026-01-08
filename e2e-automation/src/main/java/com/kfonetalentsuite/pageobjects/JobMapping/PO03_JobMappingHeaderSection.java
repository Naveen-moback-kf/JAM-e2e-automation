package com.kfonetalentsuite.pageobjects.JobMapping;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import com.kfonetalentsuite.utils.JobMapping.Utilities;

public class PO03_JobMappingHeaderSection extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO03_JobMappingHeaderSection.class);

	private static final By KFONE_CLIENTS_PAGE_TITLE = By.xpath("//h2[text()='Clients']");

	public PO03_JobMappingHeaderSection() {
		super();
	}

	public void verify_kf_talent_suite_logo_is_displaying() {
		try {
			Assert.assertTrue(waitForElement(Locators.Navigation.KF_TALENT_SUITE_LOGO).isDisplayed());
			LOGGER.info("KORN FERRY TALENT SUITE Logo is displaying as Expected on Job Mapping UI Header");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_kf_talent_suite_logo_is_displaying", "Issue in displaying KF Talent Suite logo", e);
		}
	}

	public void click_on_kf_talent_suite_logo() {
		try {
			clickElement(Locators.Navigation.KF_TALENT_SUITE_LOGO);
			LOGGER.info("Clicked on KORN FERRY TALENT SUITE Logo in Job Mapping UI Header");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "click_on_kf_talent_suite_logo", "Issue in clicking on KF Talent Suite Logo", e);
		}
	}

	public void navigate_to_job_mapping_page_from_kfone_global_menu() {
		int maxRetries = 2;
		boolean navigationSuccess = false;

		for (int attempt = 1; attempt <= maxRetries && !navigationSuccess; attempt++) {
			try {
				if (attempt > 1) {
					LOGGER.warn("Retry attempt {}/{} - Refreshing browser", attempt, maxRetries);
					refreshPage();
					Utilities.waitForUIStability(driver, 3);
					Thread.sleep(5000);
				}

				waitForSpinners();
				Utilities.waitForPageReady(driver, 2);

				try {
					WebElement menuButton = waitForClickable(Locators.Navigation.GLOBAL_NAV_MENU_BTN);
					if (!tryClickWithStrategies(menuButton)) {
						jsClick(menuButton);
					}
					Utilities.waitForUIStability(driver, 1);
				} catch (Exception e) {
					LOGGER.warn("Failed to click KFONE Global Menu on attempt {}/{}", attempt, maxRetries);
					if (attempt < maxRetries) continue;
					throw new RuntimeException("KFONE Global Menu button not clickable after " + maxRetries + " attempts");
				}

				Utilities.waitForPageReady(driver, 1);

				try {
					WebElement jamButton = waitForClickable(Locators.Navigation.JOB_MAPPING_BTN);
					jamButton.click();
					navigationSuccess = true;
				} catch (Exception e) {
					try {
						jsClick(driver.findElement(Locators.Navigation.JOB_MAPPING_BTN));
						navigationSuccess = true;
					} catch (Exception s) {
						if (attempt < maxRetries) continue;
						throw new RuntimeException("Job Mapping button not found after " + maxRetries + " attempts");
					}
				}

				waitForSpinners();
				Utilities.waitForPageReady(driver, 2);
				// Wait for background API (~100K records) to complete
				waitForBackgroundDataLoad();
				LOGGER.info("Successfully Navigated to Job Mapping screen");

			} catch (Exception e) {
				if (attempt < maxRetries) {
					LOGGER.warn("Navigation attempt {}/{} failed: {}", attempt, maxRetries, e.getMessage());
				} else {
					Utilities.handleError(LOGGER, "navigate_to_job_mapping_page_from_kfone_global_menu",
							"Failed to navigate to Job Mapping page after " + maxRetries + " attempts", e);
					throw new RuntimeException("Failed to navigate to Job Mapping page", e);
				}
			}
		}
	}

	public void user_should_verify_client_name_is_correctly_displaying() {
		try {
			WebElement clientNameElement = waitForElement(Locators.Navigation.CLIENT_NAME);
			Assert.assertTrue(clientNameElement.isDisplayed());
			String clientNameText = clientNameElement.getText();
			Assert.assertEquals(PO01_KFoneLogin.clientName.get(), clientNameText);
			LOGGER.info("Client name correctly displaying on the Job Mapping UI Header: " + clientNameText);
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_should_verify_client_name_is_correctly_displaying", "Issue in displaying correct client name", e);
		}
	}

	public void click_on_client_name_in_job_mapping_ui_header() {
		try {
			clickElement(Locators.Navigation.CLIENT_NAME);
			LOGGER.info("Clicked on client name in Job Mapping UI Header");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "click_on_client_name_in_job_mapping_ui_header", "Issue in clicking on client name", e);
		}
	}

	public void verify_user_navigated_to_kfone_clients_page() {
		try {
			waitForSpinners();
			Utilities.waitForPageReady(driver, 5);
			waitForElement(Locators.KFONE.LANDING_PAGE_TITLE);
			String text = getElementText(KFONE_CLIENTS_PAGE_TITLE);
			Assert.assertEquals("Clients", text);
			LOGGER.info("User navigated to KFONE Clients Page");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_user_navigated_to_kfone_clients_page", "Issue in navigating to KFONE clients page", e);
		}
	}

	public void verify_user_profile_logo_is_displaying_and_clickable() {
		try {
			Assert.assertTrue(waitForElement(Locators.UserProfile.PROFILE_AVATAR).isDisplayed());
			clickElement(Locators.UserProfile.PROFILE_BTN);
			LOGGER.info("User Profile logo is displaying in Job Mapping Header and clicked on it");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_user_profile_logo_is_displaying_and_clickable", "Issue in displaying/clicking User Profile logo", e);
		}
	}

	public void verify_user_profile_menu_is_opened() {
		try {
			Utilities.waitForPageReady(driver, 2);
			Assert.assertTrue(waitForElement(Locators.UserProfile.PROFILE_USER_NAME).isDisplayed());
			LOGGER.info("User profile menu is opened on click of user profile logo");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_user_profile_menu_is_opened", "Issue in opening User Profile Menu", e);
		}
	}

	public void verify_user_name_is_displayed_in_profile_menu() {
		try {
			String profileUserNameText = getElementText(Locators.UserProfile.PROFILE_USER_NAME);
			LOGGER.info("User Name: " + profileUserNameText + " is displayed in Profile Menu");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_user_name_is_displayed_in_profile_menu", "Issue in displaying User name", e);
		}
	}

	public void verify_user_email_is_displayed_in_profile_menu() {
		try {
			String profileUserEmailText = getElementText(Locators.UserProfile.PROFILE_EMAIL);
			Assert.assertTrue(PO01_KFoneLogin.username.get().equalsIgnoreCase(profileUserEmailText),
					"Expected email: " + PO01_KFoneLogin.username.get() + " but found: " + profileUserEmailText);
			LOGGER.info("User Email: " + profileUserEmailText + " is displayed in Profile Menu");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_user_email_is_displayed_in_profile_menu", "Issue in displaying User email", e);
		}
	}

	public void click_on_signout_button_in_user_profile_menu() {
		try {
			Assert.assertTrue(waitForElement(Locators.UserProfile.SIGN_OUT_BTN).isDisplayed());
			clickElement(Locators.UserProfile.SIGN_OUT_BTN);
			LOGGER.info("Clicked on logout button in User Profile Menu");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "click_on_signout_button_in_user_profile_menu", "Issue in clicking logout button", e);
		}
	}

	public void user_should_be_signed_out_from_the_application() {
		try {
			Utilities.waitForPageReady(driver, 3);
			Assert.assertTrue(waitForElement(Locators.KFONE.LOGIN_PAGE_TEXT).isDisplayed());
			LOGGER.info("User signed out successfully and navigated back to Korn Ferry Talent Suite Sign In page");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_should_be_signed_out_from_the_application", "Issue in signing out from Application", e);
		}
	}
}


