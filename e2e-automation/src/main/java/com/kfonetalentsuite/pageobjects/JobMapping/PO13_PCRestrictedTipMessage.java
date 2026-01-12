package com.kfonetalentsuite.pageobjects.JobMapping;

import static com.kfonetalentsuite.pageobjects.JobMapping.BasePageObject.KFONE.*;
import static com.kfonetalentsuite.pageobjects.JobMapping.BasePageObject.ProfileManagerPage.*;
import static com.kfonetalentsuite.pageobjects.JobMapping.BasePageObject.Locators.SharedLocators.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import com.kfonetalentsuite.utils.JobMapping.Utilities;
public class PO13_PCRestrictedTipMessage extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO13_PCRestrictedTipMessage.class);

	// formatDateForDisplay() is inherited from BasePageObject

	public static ThreadLocal<String> teamName = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<String> pcName = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<String> spNameString = ThreadLocal.withInitial(() -> "Account Manager");
	// KFONE_MENU is available via GLOBAL_NAV_MENU_BTN
	public PO13_PCRestrictedTipMessage() {
		super();
	}

	public void navigate_to_system_configuration_page_from_kfone_global_menu() {
		try {
			int maxRetries = 2;

			for (int attempt = 1; attempt <= maxRetries; attempt++) {
				try {
					LOGGER.info("Navigation attempt {}/{}", attempt, maxRetries);

					waitForSpinners();
					Utilities.waitForPageReady(driver, 2);

					LOGGER.info("Clicking KFONE Global Menu...");
					clickElement(GLOBAL_NAV_MENU_BTN);
					LOGGER.info("Successfully clicked KFONE Global Menu");

					Utilities.waitForUIStability(driver, 1);

					LOGGER.info("Clicking User Access button in KFONE menu...");
					clickElement(USER_ACCESS_BTN);
					LOGGER.info("Successfully clicked User Access button in KFONE menu");

					waitForSpinners();
					Utilities.waitForPageReady(driver, 2);

					WebElement uamBtn = waitForElement(UAM_BUTTON);
					if (uamBtn.isDisplayed()) {
						LOGGER.info("User landed on the SYSTEM CONFIGURATION page");
						return;
					}

				} catch (Exception retryEx) {
					if (attempt < maxRetries) {
						LOGGER.warn("Navigation failed on attempt {}/{} - refreshing page...", attempt, maxRetries);
						refreshPage();
						waitForSpinners();
						Utilities.waitForPageReady(driver, 5);
						Utilities.waitForUIStability(driver, 3);
						Thread.sleep(5000);
					} else {
						LOGGER.error("Failed to navigate to System Configuration page after {} attempts", maxRetries);
						throw retryEx;
					}
				}
			}

			throw new RuntimeException("Failed to navigate to System Configuration page after " + maxRetries + " attempts");

		} catch (Exception e) {
			Utilities.handleError(LOGGER, "navigate_to_system_configuration_page_from_kfone_global_menu", "Error navigating to System Configuration page from KFONE Global Menu", e);
			throw new RuntimeException("Failed to navigate to System Configuration page from KFONE Global Menu", e);
		}
	}

	public void click_on_user_admin_module_button() {
		try {
			scrollToElement(driver.findElement(UAM_BUTTON));
			Utilities.waitForUIStability(driver, 1);
			clickElement(UAM_BUTTON);
			waitForSpinners();
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "click_on_user_admin_module_button", "Issue in clicking on User Admin Module button", e);
			Assert.fail("Issue in clicking on User Admin Module button...Please Investigate!!!");
		}
	}

	public void user_should_be_landed_on_clients_dashboard_page() {
		try {
			waitForSpinners();
			String text = getElementText(CLIENT_DASHBOARD);
			Assert.assertEquals(text, "DASHBOARD");
			String clientname = getElementText(CLIENT_NAME_TEXT);
			LOGGER.info("User successfully landed on the Dashboard page of the client : " + clientname);
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_should_be_landed_on_clients_dashboard_page", "Issue in landing on Clients Dashboard page", e);
			Assert.fail("Issue in landing on Clients Dashboard page....Please Investigate!!!");
		}
	}

	public void user_is_on_clients_dashboard_page() {
		String clientname = getElementText(CLIENT_NAME_TEXT);
		LOGGER.info("User is on the Dashboard page of the client : " + clientname);
	}

	public void click_on_teams_section() {
		try {
			scrollToElement(driver.findElement(TEAMS_SECTION));
			Utilities.waitForUIStability(driver, 1);
			clickElement(TEAMS_SECTION);
			LOGGER.info("Clicked on Teams section in User Admin Module....");
			waitForSpinners();
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "click_on_teams_section", "Issue in clicking on Teams section in User Admin Module", e);
			Assert.fail("Issue in clicking on Teams section in User Admin Module...Please Investigate!!!");
		}
	}

	public void verify_user_landed_on_teams_page() {
		try {
			waitForSpinners();
			String text = getElementText(TEAMS_PAGE_HEADER);
			Assert.assertEquals(text, "TEAMS");
			LOGGER.info("User landed on the " + text + " page");
			String text1 = getElementText(TEAMS_PAGE_DESC);
			LOGGER.info(text1);
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_user_landed_on_teams_page", "Issue in landing on Teams page", e);
			Assert.fail("Issue in landing on Teams page....Please Investigate!!!");
		}
	}

	public void click_on_create_teams_button() {
		try {
			scrollToElement(driver.findElement(CREATE_TEAMS_BTN));
			Utilities.waitForUIStability(driver, 1);
			clickElement(CREATE_TEAMS_BTN);
			LOGGER.info("Clicked on CREATE TEAMS button in Teams section....");
			waitForSpinners();
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "click_on_create_teams_button", "Issue in clicking on Create Teams button in Teams section", e);
			Assert.fail("Issue in clicking on Create Teams button in Teams section...Please Investigate!!!");
		}
	}

	public void user_should_be_navigated_to_first_step_of_creating_a_team() {
		try {
			waitForSpinners();
			String text = getElementText(CREATE_TEAMS_STEP1_HEADER);
			Assert.assertEquals(text, "Step 1 of 2: Name and describe your team and select a Profile Collection");
			LOGGER.info("User successfully navigated to first step of creating a team");
			LOGGER.info(text);
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_should_be_navigated_to_first_step_of_creating_a_team", "Issue in navigating to first step of creating a team", e);
			Assert.fail("Issue in navigating to first step of creating a team....Please Investigate!!!");
		}
	}

	public void user_should_enter_team_name_and_team_description() {
		try {
			String todayDate = formatDateForDisplay();
			teamName.set("Team_" + todayDate);
			waitForElement(PC_NAME_TEXTBOX).sendKeys(teamName.get());
			LOGGER.info("Entered Team Name : " + teamName.get());
			scrollToElement(driver.findElement(PC_DESC_TEXTBOX));
			waitForElement(PC_DESC_TEXTBOX).sendKeys("Creating a Team through Automation on " + todayDate);
			LOGGER.info("Entered Team Description : Creating a Team through Automation on " + todayDate);
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_should_enter_team_name_and_team_description", "Issue in entering Team name and Team Description in the first step of creating a team", e);
			Assert.fail("Issue in entering Team name and Team Description in the first step of creating a team....Please Investigate!!!");
		}
	}

	public void click_on_next_button_in_create_team_page() {
		try {
			scrollToElement(driver.findElement(NEXT_BTN));
			Utilities.waitForUIStability(driver, 1);
			clickElement(NEXT_BTN);
			LOGGER.info("Clicked on Next button in Create Teams page....");
			waitForSpinners();
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "click_on_next_button_in_create_team_page", "Issue in clicking on Next button in Create Teams page", e);
			Assert.fail("Issue in clicking on Next button in Create Teams page...Please Investigate!!!");
		}
	}

	public void user_should_be_navigated_to_second_step_of_creating_a_team() {
		try {
			waitForSpinners();
			String text = getElementText(CREATE_TEAMS_STEP2_HEADER);
			Assert.assertEquals(text, "Step 2 of 2: Select Team Members");
			LOGGER.info("User successfully navigated to Second step of creating a team");
			LOGGER.info(text);
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_should_be_navigated_to_second_step_of_creating_a_team", "Issue in navigating to Second step of creating a team", e);
			Assert.fail("Issue in navigating to Second step of creating a team....Please Investigate!!!");
		}
	}

	public void search_user_to_add_as_team_member() {
		try {
			waitForElement(CREATE_TEAMS_STEP2_SEARCHBAR).sendKeys(PO01_KFoneLogin.username.get());
			waitForSpinners();
			LOGGER.info("Entered User name : " + PO01_KFoneLogin.username.get() + " in the search bar to add as Team Member");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "search_user_to_add_as_team_member", "Issue in Searching a user to add as Team Member", e);
			Assert.fail("Issue in Searching a user to add as Team Member....Please Investigate!!!");
		}
	}

	public void select_searched_user_to_add_as_team_member() {
		try {
			waitForSpinners();
			scrollToBottom();
			waitForSpinners();
			Utilities.waitForPageReady(driver, 2);

			String xpath = "//td[contains(@class,'check-box')]//kf-checkbox";
			js.executeScript(
					"var element = document.evaluate(arguments[0], document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue; if (element) element.click();",
					xpath);
			Utilities.waitForUIStability(driver, 1);

			LOGGER.info("User " + PO01_KFoneLogin.username.get() + " is Selected to add as Team Member");
			waitForSpinners();
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "select_searched_user_to_add_as_team_member", "Issue in selecting user to add as Team Member", e);
			Assert.fail("Issue in selecting user to add as Team Member...Please Investigate!!!");
		}
	}

	public void click_on_team_members_header_and_verify_user_is_added_successfully_as_team_member() {
		try {
			scrollToElement(driver.findElement(TEAM_MEMBERS_HEADER));
			Utilities.waitForUIStability(driver, 1);
			clickElement(TEAM_MEMBERS_HEADER);
			waitForSpinners();
			LOGGER.info("Clicked on Team Members header in Create Teams page....");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "click_on_team_members_header_and_verify_user_is_added_successfully_as_team_member", "Issue in clicking on Team Members header", e);
			Assert.fail("Issue in clicking on Team Members header...Please Investigate!!!");
		}

		try {
			waitForSpinners();
			waitForElement(USER_COUNT);
			String text = getElementText(USER_COUNT);
			Assert.assertEquals(text, "Showing 1 of 1 Users");
			LOGGER.info("User " + PO01_KFoneLogin.username.get() + " successfully added as Team Member");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "click_on_team_members_header_and_verify_user_is_added_successfully_as_team_member", "Issue in Verifying User added as Team Member", e);
			Assert.fail("Issue in Verifying User added as Team Member....Please Investigate!!!");
		}
	}

	public void click_on_save_button_on_team_page_and_verify_success_popup_appears() {
		try {
			WebElement element = waitForClickable(SAVE_BTN);
			scrollToElement(element);
			Utilities.waitForUIStability(driver, 1);
			clickElement(element);
			LOGGER.info("Clicked on Save button in Create Teams page....");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "click_on_save_button_on_team_page_and_verify_success_popup_appears", "Issue in clicking on Save button in Create Teams page", e);
			Assert.fail("Issue in clicking on Save button in Create Teams page...Please Investigate!!!");
		}

		try {
			Utilities.waitForPageReady(driver, 5);

			boolean popupFound = false;
			String popupMessage = "";

			try {
				wait.until(ExpectedConditions.or(
						ExpectedConditions.visibilityOfElementLocated(CREATE_TEAMS_SUCCESS_POPUP),
						ExpectedConditions.visibilityOfElementLocated(CREATE_TEAMS_FAILURE_POPUP)));

				try {
					WebElement successPopup = driver.findElement(CREATE_TEAMS_SUCCESS_POPUP);
					if (successPopup.isDisplayed()) {
						popupMessage = successPopup.getText();
						LOGGER.info(popupMessage);
						popupFound = true;
					}
				} catch (Exception e) {
				}

				if (!popupFound) {
					try {
						WebElement failurePopup = driver.findElement(CREATE_TEAMS_FAILURE_POPUP);
						if (failurePopup.isDisplayed()) {
							popupMessage = failurePopup.getText();
							LOGGER.info(popupMessage);
							popupFound = true;
						}
					} catch (Exception e) {
					}
				}
			} catch (Exception e) {
				LOGGER.warn("Timeout waiting for team creation popup to appear");
			}

			if (!popupFound) {
				throw new RuntimeException("Neither success nor failure popup appeared after creating team");
			}

			waitForSpinners();
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "click_on_save_button_on_team_page_and_verify_success_popup_appears", "Issue in verifying appearing of popup after creating a team", e);
			Assert.fail("Issue in verifying appearing of popup after creating a team....Please Investigate!!!");
		}
	}

	public void search_for_team_name_in_teams_page_and_verify_team_is_created_successfully() {
		try {
			waitForElement(TEAMS_PAGE_SEARCHBAR).sendKeys(teamName.get());
		waitForSpinners();
		Thread.sleep(2000);
		Utilities.waitForVisible(wait, driver.findElement(TEAM_NAME_IN_TOP_ROW));
		String text = getElementText(TEAM_NAME_IN_TOP_ROW);
		Assert.assertEquals(text, teamName.get());
			LOGGER.info("Team with name : " + teamName.get() + " is successfully created");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "search_for_team_name_in_teams_page_and_verify_team_is_created_successfully", "Issue in Searching and Verifying a Team is created", e);
			Assert.fail("Issue in Searching and Verifying a Team is created successfully....Please Investigate!!!");
		}
	}

	public void click_on_profile_collections_section() {
		try {
			WebElement element = waitForClickable(PC_SECTION);
			scrollToElement(element);
			Utilities.waitForUIStability(driver, 1);
			clickElement(element);
			LOGGER.info("Clicked on Profile Collections section in User Admin Module....");
			waitForSpinners();
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "click_on_profile_collections_section", "Issue in clicking on Profile Collections section in User Admin Module", e);
			Assert.fail("Issue in clicking on Profile Collections section in User Admin Module...Please Investigate!!!");
		}
	}

	public void verify_user_navigated_to_manage_success_profiles_page() {
		try {
			waitForSpinners();
			String text = getElementText(PC_PAGE_HEADER);
			Assert.assertEquals(text, "MANAGE SUCCESS PROFILES");
			LOGGER.info("User landed on the " + text + " page");
			String text1 = getElementText(PC_PAGE_DESC);
			LOGGER.info(text1);
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_user_navigated_to_manage_success_profiles_page", "Issue in landing on Manage Success Profiles page", e);
			Assert.fail("Issue in landing on Manage Success Profiles page....Please Investigate!!!");
		}
	}

	public void click_on_create_profile_collection_button() {
		try {
			WebElement element = waitForClickable(CREATE_PC_BTN);
			scrollToElement(element);
			Utilities.waitForUIStability(driver, 1);
			clickElement(element);
			LOGGER.info("Clicked on CREATE PROFILE COLLECTION button in Teams section");
			waitForSpinners();
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "click_on_create_profile_collection_button", "Issue in clicking on CREATE PROFILE COLLECTION button in Teams section", e);
			Assert.fail("Issue in clicking on CREATE PROFILE COLLECTION button in Teams section...Please Investigate!!!");
		}
	}

	public void user_should_navigated_to_first_step_of_creating_a_profile_collection() {
		try {
			waitForSpinners();
			String text = getElementText(CREATE_PC_STEP1_HEADER);
			Assert.assertEquals(text, "Step 1 of 3: Name and describe your Profile Collection");
			LOGGER.info("User successfully navigated to first step of creating a team");
			LOGGER.info(text);
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_should_navigated_to_first_step_of_creating_a_profile_collection", "Issue in navigating to first step of creating a Profile Collection", e);
			Assert.fail("Issue in navigating to first step of creating a Profile Collection....Please Investigate!!!");
		}
	}

	public void user_should_enter_profile_collection_name_and_profile_collection_description() {
		try {
			String todayDate = formatDateForDisplay();
			pcName.set("PC_" + todayDate);
			WebElement nameElement = waitForClickable(PC_NAME_TEXTBOX);
			scrollToElement(nameElement);
			Utilities.waitForUIStability(driver, 1);
			clickElement(nameElement);
			waitForElement(PC_NAME_TEXTBOX).sendKeys(pcName.get());
			LOGGER.info("Entered Profile Collection Name : " + pcName.get());
			WebElement descElement = waitForClickable(PC_DESC_TEXTBOX);
			scrollToElement(descElement);
			Utilities.waitForUIStability(driver, 1);
			clickElement(descElement);
			waitForElement(PC_DESC_TEXTBOX).sendKeys("Creating a Profile Collection through Automation on " + todayDate);
			LOGGER.info("Entered Profile Collection Description : Creating a Team through Automation on " + todayDate);
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_should_enter_profile_collection_name_and_profile_collection_description", "Issue in entering Profile Collection name and Profile Collection Description in the first step of creating a Profile Collection", e);
			Assert.fail("Issue in entering Profile Collection name and Profile Collection Description in the first step of creating a Profile Collection....Please Investigate!!!");
		}
	}

	public void click_on_next_button_in_create_profile_collection_page() {
		try {
			WebElement element = waitForClickable(NEXT_BTN);
			scrollToElement(element);
			Utilities.waitForUIStability(driver, 1);
			clickElement(element);
			LOGGER.info("Clicked on Next button in Create Profile Collection page....");
			waitForSpinners();
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "click_on_next_button_in_create_profile_collection_page", "Issue in clicking on Next button in Create Profile Collection page", e);
			Assert.fail("Issue in clicking on Next button in Create Profile Collection page...Please Investigate!!!");
		}
	}

	public void user_should_be_navigated_to_second_step_of_creating_a_profile_collection() {
		try {
			waitForSpinners();
			String text = getElementText(CREATE_PC_STEP2_HEADER);
			Assert.assertEquals(text, "Step 2 of 3: Select Team(s) for your Profile Collection");
			LOGGER.info("User successfully navigated to Second step of creating a Profile Collection");
			LOGGER.info(text);
			waitForSpinners();
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_should_be_navigated_to_second_step_of_creating_a_profile_collection", "Issue in navigating to Second step of creating a Profile Collection", e);
			Assert.fail("Issue in navigating to Second step of creating a Profile Collection....Please Investigate!!!");
		}
	}

	public void click_on_all_teams_header() {
		try {
			waitForSpinners();
			Utilities.waitForPageReady(driver);
			WebElement element = waitForClickable(ALL_TEAMS_HEADER);
			scrollToElement(element);
			Utilities.waitForUIStability(driver, 1);
			clickElement(element);
			LOGGER.info("Clicked on All Teams button in Create Profile Collection page....");
			waitForSpinners();
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "click_on_all_teams_header", "Issue in clicking on All Teams header button in Create Profile Collection page", e);
			Assert.fail("Issue in clicking on All Teams header button in Create Profile Collection page...Please Investigate!!!");
		}
	}

	public void select_recently_created_team_name() {
		try {
			waitForSpinners();
			scrollToBottom();
			waitForSpinners();
			Utilities.waitForPageReady(driver, 2);

			String xpath = "//td[contains(@class,'check-box')]//kf-checkbox";
			js.executeScript(
					"var element = document.evaluate(arguments[0], document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue; if (element) element.click();",
					xpath);
			Utilities.waitForUIStability(driver, 1);

			LOGGER.info("Recently Created Team with name " + teamName.get() + " is Selected for Profile Collection");
			waitForSpinners();
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "select_recently_created_team_name", "Issue in selecting Recently Created Team for Profile Collection", e);
			Assert.fail("Issue in selecting Recently Created Team for Profile Collection...Please Investigate!!!");
		}
	}

	public void click_on_selected_teams_header() {
		try {
			Utilities.waitForPageReady(driver, 2);
			WebElement element = waitForClickable(SELECTED_TEAMS_HEADER);
			scrollToElement(element);
			Utilities.waitForUIStability(driver, 1);
			clickElement(element);
			LOGGER.info("Clicked on Selected Teams button in Create Profile Collection page");
			waitForSpinners();
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "click_on_selected_teams_header", "Issue in clicking on Selected Teams header button in Create Profile Collection page", e);
			Assert.fail("Issue in clicking on Selected Teams header button in Create Profile Collection page...Please Investigate!!!");
		}
	}

	public void user_should_verify_recently_created_team_name_is_available_in_selected_teams() {
		try {
			waitForSpinners();
			Utilities.waitForPageReady(driver, 2);
			waitForElement(TEAM_COUNT);
			String text = getElementText(TEAM_COUNT);
			Assert.assertEquals(text, "Page 1 of 1");
			LOGGER.info("Recently created team " + teamName.get() + " is available in Selected Teams");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_should_verify_recently_created_team_name_is_available_in_selected_teams", "Issue in Verifing Recently created team name is available in selected teams", e);
			Assert.fail("Issue in Verifing Recently created team name is available in selected teams....Please Investigate!!!");
		}
	}

	public void user_should_be_navigated_to_third_step_of_creating_a_profile_collection() {
		try {
			waitForSpinners();
			String text = getElementText(CREATE_PC_STEP3_HEADER);
			Assert.assertEquals(text, "Step 3 of 3: Select Success Profiles for your Profile Collection");
			LOGGER.info("User successfully navigated to Third step of creating a Profile Collection");
			LOGGER.info(text);
			waitForSpinners();
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_should_be_navigated_to_third_step_of_creating_a_profile_collection", "Issue in navigating to Third step of creating a Profile Collection", e);
			Assert.fail("Issue in navigating to Third step of creating a Profile Collection....Please Investigate!!!");
		}
	}

	public void click_on_add_additional_success_profiles_header() {
		try {
			waitForSpinners();
			Utilities.waitForPageReady(driver, 3);

			WebElement element = waitForClickable(ADD_ADDITIONAL_SP_HEADER);
			scrollToElement(element);
			Utilities.waitForUIStability(driver, 1);
			clickElement(element);
			LOGGER.info("Clicked on ADD ADDITIONAL SUCCESS PROFILES button in Create Profile Collection page....");
			waitForSpinners();
			Utilities.waitForPageReady(driver, 3);
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "click_on_add_additional_success_profiles_header", "Issue in clicking on ADD ADDITIONAL SUCCESS PROFILES header button in Create Profile Collection page", e);
			Assert.fail("Issue in clicking on ADD ADDITIONAL SUCCESS PROFILES header button in Create Profile Collection page...Please Investigate!!!");
		}
	}

	public void search_and_add_success_profiles_to_profile_collection() {
		try {
			waitForSpinners();
			Utilities.waitForPageReady(driver, 5);

			waitForElement(PC_STEP3_SEARCHBAR).sendKeys(spNameString.get());
			LOGGER.info("Entered search term: " + spNameString.get() + " in Success Profiles search bar");

			waitForSpinners();
			Utilities.waitForPageReady(driver, 5);
			waitForSpinners();

			int maxAttempts = 3;
			boolean profilesSelected = false;

			for (int attempt = 1; attempt <= maxAttempts && !profilesSelected; attempt++) {
				try {
					WebElement selectAllElement = waitForClickable(SELECT_ALL_CHECKBOX);
					scrollToElement(selectAllElement);
					Utilities.waitForUIStability(driver, 1);
					Thread.sleep(2000);
					clickElement(selectAllElement);
					LOGGER.info("Clicked Select All checkbox (attempt {}/{})", attempt, maxAttempts);

					Utilities.waitForUIStability(driver, 3);
					waitForSpinners();

					try {
						String successProfilesHeaderText = getElementText(SP_HEADER_IN_PC);
						if (successProfilesHeaderText.contains("(") && !successProfilesHeaderText.contains("(0)")) {
							String count = successProfilesHeaderText.substring(successProfilesHeaderText.indexOf("(") + 1, successProfilesHeaderText.indexOf(")"));
							int profileCount = Integer.parseInt(count.trim());
							if (profileCount > 0) {
								profilesSelected = true;
								LOGGER.info("Successfully selected {} profiles", profileCount);
							} else {
								LOGGER.warn("Attempt {}/{}: Profile count is still 0, retrying...", attempt, maxAttempts);
							}
						} else {
							LOGGER.warn("Attempt {}/{}: Success Profiles count is 0, retrying...", attempt, maxAttempts);
						}
					} catch (Exception e) {
						LOGGER.warn("Attempt {}/{}: Could not verify profile count, retrying...", attempt, maxAttempts);
					}

					if (!profilesSelected && attempt < maxAttempts) {
						Utilities.waitForUIStability(driver, 2);
					}

				} catch (Exception e) {
					LOGGER.warn("Attempt {}/{} failed: {}", attempt, maxAttempts, e.getMessage());
					if (attempt == maxAttempts) {
						throw e;
					}
				}
			}

			if (!profilesSelected) {
				throw new RuntimeException("Failed to select profiles after " + maxAttempts + " attempts. Success Profiles count remains 0.");
			}

			LOGGER.info("Success Profiles which contains " + spNameString.get() + " as Sub-string are selected to add to Profile Collection");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "search_and_add_success_profiles_to_profile_collection", "Issue in Searching and adding Success Profiles to Profile Collection", e);
			Assert.fail("Issue in Searching and adding Success Profiles to Profile Collection....Please Investigate!!!");
		}
	}

	public void click_on_success_profiles_header() {
		try {
			waitForSpinners();
			Utilities.waitForPageReady(driver, 3);

			WebElement element = waitForClickable(SP_HEADER_IN_PC);
			scrollToElement(element);
			Utilities.waitForUIStability(driver, 1);
			clickElement(element);
			LOGGER.info("Clicked on SUCCESS PROFILES header button after selecting Success Profiles to Profile Collection....");
			waitForSpinners();
			Utilities.waitForPageReady(driver, 3);
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "click_on_success_profiles_header", "Issue in clicking on SUCCESS PROFILES header button after selecting Success Profiles to Profile Collection", e);
			Assert.fail("Issue in clicking on SUCCESS PROFILES header button after selecting Success Profiles to Profile Collection...Please Investigate!!!");
		}
	}

	public void user_should_verify_added_profiles_are_available_in_success_profiles_header() {
		try {
			waitForSpinners();
			Utilities.waitForPageReady(driver, 3);
			waitForElement(PC_COUNT);
			String resultsCountText = getElementText(PC_COUNT);
			String[] resultsCountText_split = resultsCountText.split(" ");
			LOGGER.info(resultsCountText_split[3] + "Success Profiles which contains " + spNameString.get() + " as Sub-string are added to Profile Collection");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_should_verify_added_profiles_are_available_in_success_profiles_header", "Issue in Verifing added profiles availablity in success profiles for Profile Collection", e);
			Assert.fail("Issue in Verifing added profiles availablity in success profiles for Profile Collection....Please Investigate!!!");
		}
	}

	public void click_on_done_button_on_create_profile_collection_page_and_verify_success_popup_appears() {
		try {
			WebElement element = waitForClickable(ProfileManagerPage.DONE_BTN);
			scrollToElement(element);
			Utilities.waitForUIStability(driver, 1);
			clickElement(element);
			LOGGER.info("Clicked on Done button in Create Profile Collections page....");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "click_on_done_button_on_create_profile_collection_page_and_verify_success_popup_appears", "Issue in clicking on Done button in Create Profile Collections page", e);
			Assert.fail("Issue in clicking on Done button in Create Profile Collections page.......Please Investigate!!!");
		}

		try {
			Utilities.waitForPageReady(driver, 5);
			waitForElement(PC_SUCCESS_POPUP);
			String SuccesstText = getElementText(PC_SUCCESS_POPUP);
			LOGGER.info(SuccesstText);
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "click_on_done_button_on_create_profile_collection_page_and_verify_success_popup_appears", "Issue in verifying appearing of success popup after creating a Profile Collection", e);
			Assert.fail("Issue in verifying appearing of success popup after creating a Profile Collection....Please Investigate!!!");
		}
		LOGGER.info("Profile Collection with name " + pcName.get() + " is created");
	}

	public void search_and_delete_profile_collection() {
		try {
			waitForElement(PC_SEARCHBAR).sendKeys(pcName.get());
			Utilities.waitForUIStability(driver, 1);
			waitForSpinners();
			waitForSpinners();
			Utilities.waitForPageReady(driver, 3);

			waitForClickable(TOP_ROW_THREE_DOTS).click();
			Utilities.waitForUIStability(driver, 2);

			WebElement deleteButton = waitForClickable(By.xpath("//div[@class='item-top-row']//*[contains(text(),'Delete')]"));
			deleteButton.click();
			Utilities.waitForUIStability(driver, 2);

			WebElement confirmButton = waitForClickable(DELETE_CONFIRM_BTN);
			confirmButton.click();
			waitForSpinners();
			Utilities.waitForPageReady(driver, 3);

			String text = getElementText(DELETION_SUCCESS_POPUP);
			LOGGER.info(text);
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "search_and_delete_profile_collection", "Issue in Searching and Deleting Profile Collection", e);
			Assert.fail("Issue in Searching and Deleting Profile Collection....Please Investigate!!!");
		}
	}

	public void search_for_team_name_in_teams_page_and_delete_team() {
		try {
			waitForElement(TEAMS_PAGE_SEARCHBAR).sendKeys(teamName.get());
			waitForSpinners();
			Utilities.waitForPageReady(driver, 3);

			waitForClickable(TOP_ROW_THREE_DOTS).click();
			Utilities.waitForUIStability(driver, 2);

			WebElement deleteButton = waitForClickable(By.xpath("//div[@class='item-top-row']//*[contains(text(),'Delete')]"));
			deleteButton.click();
			Utilities.waitForUIStability(driver, 2);

			WebElement confirmButton = waitForClickable(DELETE_CONFIRM_BTN);
			confirmButton.click();
			waitForSpinners();
			Utilities.waitForPageReady(driver, 3);
			String text = getElementText(DELETION_SUCCESS_POPUP);
			LOGGER.info(text);
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "search_for_team_name_in_teams_page_and_delete_team", "Issue in Searching and Deleting Team", e);
			Assert.fail("Issue in Searching and Deleting Team....Please Investigate!!!");
		}
	}

	public void verify_pc_restricted_tip_message_is_displaying_on_job_mapping_page() {
		try {
			Assert.assertTrue(waitForElement(TIP_MSG2_TEXT).isDisplayed());
			String TipMessage2 = getElementText(TIP_MSG2_TEXT);
			LOGGER.info(TipMessage2);
			LOGGER.info(TipMessage2);
			clickElement(TIP_MSG2_CLOSE_BTN);
			LOGGER.info("PC Restricted Tip Message verified and closed successfully...");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_pc_restricted_tip_message_is_displaying_on_job_mapping_page", "Issue in verifying PC Restricted Tip Message on Job Mapping page", e);
			Assert.fail("Issue in verifying PC Restricted Tip Message on Job Mapping page...Please Investigate!!!");
		}
	}

	public void user_is_on_teams_page() {
		Utilities.waitForPageReady(driver, 1);
		LOGGER.info("User is on TEAMS page");
	}

	public void user_is_on_second_step_of_creating_a_team() {
		Utilities.waitForPageReady(driver, 1);
		LOGGER.info("User is on second step of creating a team");
	}

	public void user_is_on_team_creation_page_with_team_members_added() {
		Utilities.waitForPageReady(driver, 1);
		LOGGER.info("User is on Team creation page with team members added");
	}

	public void user_is_on_manage_success_profiles_page() {
		Utilities.waitForPageReady(driver, 1);
		LOGGER.info("User is on Manage Success Profiles page");
	}

	public void user_is_on_second_step_of_creating_a_profile_collection() {
		Utilities.waitForPageReady(driver, 1);
		LOGGER.info("User is on second step of creating a Profile Collection");
	}

	public void user_is_on_third_step_of_creating_a_profile_collection() {
		Utilities.waitForPageReady(driver, 1);
		LOGGER.info("User is on third step of creating a Profile Collection");
	}

	public void user_is_on_profile_collection_page_with_profiles_added() {
		Utilities.waitForPageReady(driver, 1);
		LOGGER.info("User is on Profile Collection creation page with profiles added");
	}

	// formatDateForDisplay() removed - now using inherited method from BasePageObject
}

