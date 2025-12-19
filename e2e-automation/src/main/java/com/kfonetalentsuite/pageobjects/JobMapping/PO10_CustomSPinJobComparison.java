package com.kfonetalentsuite.pageobjects.JobMapping;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import com.kfonetalentsuite.utils.JobMapping.PageObjectHelper;

public class PO10_CustomSPinJobComparison extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO10_CustomSPinJobComparison.class);

	public static ThreadLocal<String> customSPSearchString = ThreadLocal.withInitial(() -> "Engineer");
	public static ThreadLocal<String> customSPNameinSearchResults = ThreadLocal.withInitial(() -> "NOT_SET");

	private static final By SEARCH_BAR_JC = By.xpath("//input[contains(@id,'jobcompare-search')]");
	private static final By SEARCH_BAR_CANCEL_BTN = By.xpath("//*[contains(@id,'cancel-icon')]//..");
	private static final By FIRST_SEARCH_RESULT_BTN = By.xpath("//ul//li[1]//button");
	private static final By THIRD_SEARCH_RESULT_BTN = By.xpath("//ul//li[3]//button");
	private static final By FIRST_SEARCH_RESULT_TEXT = By.xpath("//ul//li[1]//button//div");
	private static final By THIRD_SEARCH_RESULT_TEXT = By.xpath("//ul//li[3]//button//div");
	private static final By CUSTOM_SP_CLOSE_BTN = By.xpath("//div[@class='shadow']//div[contains(@id,'card-header')]//button");
	private static final By PROFILE_1_TITLE = By.xpath("//div[@class='shadow']//div[contains(@id,'card-title')]");
	private static final By PROFILE_1_SELECT_BTN = By.xpath("//div[@class='shadow']//div[contains(@id,'card-header')][1]//span");
	private static final By PROFILE_1_GRADE = By.xpath("//div[@class='shadow']//div[contains(@id,'grade')]");
	private static final By PROFILE_1_LEVEL = By.xpath("//div[@class='shadow']//div[contains(@id,'level-sublevels')]");
	private static final By PROFILE_1_FUNCTION = By.xpath("//div[@class='shadow']//div[contains(@id,'function-subfunction')]");
	private static final By PROFILE_1_SENIORITY = By.xpath("//div[@class='shadow']//div[contains(@id,'seniority-level')]");
	private static final By PROFILE_1_MANAGERIAL = By.xpath("//div[@class='shadow']//div[contains(@id,'managerial-experience')]");
	private static final By PROFILE_1_EDUCATION = By.xpath("//div[@class='shadow']//div[contains(@id,'education')]");
	private static final By PROFILE_1_GENERAL_EXP = By.xpath("//div[@class='shadow']//div[contains(@id,'general-experience')]");
	private static final By PROFILE_1_ROLE_SUMMARY = By.xpath("//div[@class='shadow']//div[contains(@id,'role-summary')]");
	private static final By PROFILE_1_RESPONSIBILITIES = By.xpath("//div[@class='shadow']//div[contains(@id,'responsibilities')]");
	private static final By VIEW_MORE_RESPONSIBILITIES = By.xpath("//div[contains(@id,'responsibilities')]//button[@data-testid='view-more-responsibilities']");
	private static final By PROFILE_1_COMPETENCIES = By.xpath("//div[@class='shadow']//div[contains(@id,'behavioural-competencies')]");
	private static final By VIEW_MORE_COMPETENCIES = By.xpath("//div[contains(@id,'behavioural-competencies')]//button[@data-testid='view-more-competencies']");
	private static final By PROFILE_1_SKILLS = By.xpath("//div[@class='shadow']//div[contains(@id,'skills')]");
	private static final By VIEW_MORE_SKILLS = By.xpath("//div[contains(@id,'skills')]//button[@data-testid='view-more-skills']");

	public PO10_CustomSPinJobComparison() {
		super();
	}

	public void click_on_search_bar_in_job_comparison_page() {
		waitForSpinners();
		try {
			clickElement(SEARCH_BAR_JC);
			PageObjectHelper.log(LOGGER, "Clicked on Search bar in Job Comparison page");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_search_bar_in_job_comparison_page", "Issue clicking Search bar in Job Comparison page", e);
		}
	}

	public void verify_search_bar_placeholder_text_in_job_comparison_page() {
		try {
			String placeholderText = waitForElement(SEARCH_BAR_JC).getAttribute("placeholder");
			Assert.assertEquals(placeholderText, "Search Korn Ferry Success Profiles...");
			PageObjectHelper.log(LOGGER, "Search bar Placeholder text verified successfully in Job Comparison page");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_search_bar_placeholder_text_in_job_comparison_page", "Issue verifying Search bar Placeholder text", e);
		}
	}

	public void user_should_enter_custom_sp_search_string_in_the_search_bar() {
		waitForSpinners();
		try {
			waitForElement(SEARCH_BAR_JC).sendKeys(customSPSearchString.get());
			PageObjectHelper.log(LOGGER, "Entered " + customSPSearchString.get() + " as Custom SP Search String in search bar");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_enter_custom_sp_search_string_in_the_search_bar", "Failed to enter Custom SP Search String", e);
		}
	}

	public void select_first_custom_sp_from_search_results() {
		waitForSpinners();
		try {
			Assert.assertTrue(waitForElement(FIRST_SEARCH_RESULT_BTN).isDisplayed());
			customSPNameinSearchResults.set(getElementText(FIRST_SEARCH_RESULT_TEXT));
			clickElement(FIRST_SEARCH_RESULT_BTN);
			PageObjectHelper.log(LOGGER, "First Custom SP with Name: " + customSPNameinSearchResults.get() + " selected from search results");
			wait.until(ExpectedConditions.invisibilityOfElementLocated(FIRST_SEARCH_RESULT_BTN));
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "select_first_custom_sp_from_search_results", "Issue selecting First Custom SP from Search Results", e);
		}
	}

	public void verify_custom_sp_added_to_profiles_list_in_job_comparison_page() {
		waitForSpinners();
		try {
			Assert.assertTrue(waitForElement(CUSTOM_SP_CLOSE_BTN).isDisplayed());
			String profileTitle = getElementText(PROFILE_1_TITLE);
			PageObjectHelper.log(LOGGER, "Custom SP with Name: " + profileTitle + " added to Profiles List");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_custom_sp_added_to_profiles_list_in_job_comparison_page", "Issue adding Custom SP to Profiles List", e);
		}
	}

	public void user_should_verify_custom_sp_name_and_close_button_are_displaying_in_search_bar_after_adding_custom_sp() {
		try {
			String customSPNameTextinSearchBar = waitForElement(SEARCH_BAR_JC).getAttribute("value");
			Assert.assertEquals(customSPNameTextinSearchBar, customSPNameinSearchResults.get());
			PageObjectHelper.log(LOGGER, "Custom SP Name is displaying in search bar");
			Assert.assertTrue(waitForClickable(SEARCH_BAR_CANCEL_BTN).isDisplayed());
			PageObjectHelper.log(LOGGER, "Close or Cancel Button is displaying in search bar");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_custom_sp_name_and_close_button_are_displaying_in_search_bar_after_adding_custom_sp", "Issue verifying Custom SP Name and Close button in Search Bar", e);
		}
	}

	public void user_added_custom_sp_to_profiles_list_in_job_comparison_page() {
		PageObjectHelper.log(LOGGER, "User added Custom SP to Profiles List");
	}

	public void validate_custom_sp_profile_name_matches_with_selected_custom_sp_name_from_search_results() {
		try {
			String profileTitle = getElementText(PROFILE_1_TITLE);
			Assert.assertEquals(profileTitle, customSPNameinSearchResults.get());
			PageObjectHelper.log(LOGGER, "Custom SP Profile Name: " + profileTitle + " matches Selected Custom SP Name: " + customSPNameinSearchResults.get());
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "validate_custom_sp_profile_name_matches_with_selected_custom_sp_name_from_search_results", "Issue validating Custom SP Profile Name with Selected Custom SP Name", e);
		}
	}

	public void user_should_verify_close_button_and_select_button_are_displaying_on_the_profile() {
		try {
			Assert.assertTrue(waitForClickable(CUSTOM_SP_CLOSE_BTN).isDisplayed());
			PageObjectHelper.log(LOGGER, "Close Button is displaying on Custom SP Profile");
			WebElement selectBtn = waitForElement(PROFILE_1_SELECT_BTN);
			Assert.assertTrue(selectBtn.isDisplayed());
			Assert.assertFalse(selectBtn.isSelected());
			PageObjectHelper.log(LOGGER, "Select button is displaying and is in De-Select status");
			jsClick(selectBtn);
			Assert.assertTrue(selectBtn.isEnabled());
			PageObjectHelper.log(LOGGER, "Custom SP Profile selected successfully");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_close_button_and_select_button_are_displaying_on_the_profile", "Issue verifying Close and Select buttons", e);
		}
	}

	public void verify_custom_sp_profile_grade() {
		try {
			String gradeText = getElementText(PROFILE_1_GRADE);
			PageObjectHelper.log(LOGGER, "Custom SP Profile Grade: " + gradeText);
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_custom_sp_profile_grade", "Issue verifying Custom SP Profile Grade", e);
		}
	}

	public void verify_custom_sp_profile_level_sublevels() {
		try {
			String levelText = getElementText(PROFILE_1_LEVEL);
			PageObjectHelper.log(LOGGER, "Custom SP Profile Level / Sub-levels: " + levelText);
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_custom_sp_profile_level_sublevels", "Issue verifying Custom SP Profile Level / Sub-levels", e);
		}
	}

	public void verify_custom_sp_profile_function_subfunction() {
		try {
			scrollToElement(driver.findElement(PROFILE_1_FUNCTION));
			String functionText = getElementText(PROFILE_1_FUNCTION);
			PageObjectHelper.log(LOGGER, "Custom SP Profile Function / Sub-function: " + functionText);
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_custom_sp_profile_function_subfunction", "Issue verifying Custom SP Profile Function / Sub-function", e);
		}
	}

	public void verify_custom_sp_profile_seniority_level() {
		try {
			scrollToElement(driver.findElement(PROFILE_1_SENIORITY));
			String seniorityText = getElementText(PROFILE_1_SENIORITY);
			PageObjectHelper.log(LOGGER, "Custom SP Profile Seniority level: " + seniorityText);
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_custom_sp_profile_seniority_level", "Issue verifying Custom SP Profile Seniority level", e);
		}
	}

	public void verify_custom_sp_profile_managerial_experience() {
		try {
			scrollToElement(driver.findElement(PROFILE_1_MANAGERIAL));
			String managerialText = getElementText(PROFILE_1_MANAGERIAL);
			PageObjectHelper.log(LOGGER, "Custom SP Profile Managerial experience: " + managerialText);
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_custom_sp_profile_managerial_experience", "Issue verifying Custom SP Profile Managerial experience", e);
		}
	}

	public void verify_custom_sp_profile_education() {
		try {
			scrollToElement(driver.findElement(PROFILE_1_EDUCATION));
			String educationText = getElementText(PROFILE_1_EDUCATION);
			PageObjectHelper.log(LOGGER, "Custom SP Profile Education: " + educationText);
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_custom_sp_profile_education", "Issue verifying Custom SP Profile Education", e);
		}
	}

	public void verify_custom_sp_profile_general_experience() {
		try {
			scrollToElement(driver.findElement(PROFILE_1_GENERAL_EXP));
			String generalExpText = getElementText(PROFILE_1_GENERAL_EXP);
			PageObjectHelper.log(LOGGER, "Custom SP Profile General experience: " + generalExpText);
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_custom_sp_profile_general_experience", "Issue verifying Custom SP Profile General experience", e);
		}
	}

	public void verify_custom_sp_profile_role_summary() {
		try {
			scrollToElement(driver.findElement(PROFILE_1_ROLE_SUMMARY));
			String roleSummaryText = getElementText(PROFILE_1_ROLE_SUMMARY);
			PageObjectHelper.log(LOGGER, "Custom SP Profile Role Summary: " + roleSummaryText);
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_custom_sp_profile_role_summary", "Issue verifying Custom SP Profile Role Summary", e);
		}
	}

	public void verify_custom_sp_profile_responsibilities() {
		try {
			WebElement responsibilities = driver.findElement(PROFILE_1_RESPONSIBILITIES);
			scrollToElement(responsibilities);
			Thread.sleep(200);
			expandAllViewMoreButtons(VIEW_MORE_RESPONSIBILITIES);
			String responsibilitiesText = getElementText(PROFILE_1_RESPONSIBILITIES);
			PageObjectHelper.log(LOGGER, "Custom SP Profile Responsibilities verified (" + responsibilitiesText.length() + " chars)");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_custom_sp_profile_responsibilities", "Issue verifying Custom SP Profile Responsibilities", e);
		}
	}

	public void verify_custom_sp_profile_behavioural_competencies() {
		try {
			WebElement competencies = driver.findElement(PROFILE_1_COMPETENCIES);
			scrollToElement(competencies);
			Thread.sleep(200);
			expandAllViewMoreButtons(VIEW_MORE_COMPETENCIES);
			String competenciesText = getElementText(PROFILE_1_COMPETENCIES);
			PageObjectHelper.log(LOGGER, "Custom SP Profile Behavioural Competencies verified (" + competenciesText.length() + " chars)");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_custom_sp_profile_behavioural_competencies", "Issue verifying Custom SP Profile Behavioural Competencies", e);
		}
	}

	public void verify_custom_sp_profile_skills() {
		try {
			WebElement skills = driver.findElement(PROFILE_1_SKILLS);
			scrollToElement(skills);
			Thread.sleep(200);

			List<WebElement> viewMoreBtns = driver.findElements(VIEW_MORE_SKILLS);
			if (!viewMoreBtns.isEmpty()) {
				expandAllViewMoreButtons(VIEW_MORE_SKILLS);
			}

			String skillsText = getElementText(PROFILE_1_SKILLS);
			PageObjectHelper.log(LOGGER, "Custom SP Profile Skills verified (" + skillsText.length() + " chars)");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_custom_sp_profile_skills", "Issue verifying Custom SP Profile Skills", e);
		}
	}

	public void clear_text_in_search_bar_with_clear_button_in_the_search_bar() {
		try {
			scrollToTop();
			Assert.assertTrue(waitForElement(SEARCH_BAR_CANCEL_BTN).isDisplayed());
			jsClick(driver.findElement(SEARCH_BAR_CANCEL_BTN));
			PageObjectHelper.log(LOGGER, "Clicked on clear button and cleared text in Search bar");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "clear_text_in_search_bar_with_clear_button_in_the_search_bar", "Issue clearing text with clear button in Search bar", e);
		}
	}

	public void user_should_verify_added_custom_sp_is_not_cleared_from_profiles_list_in_job_comparison_page() {
		waitForSpinners();
		try {
			Assert.assertTrue(waitForElement(CUSTOM_SP_CLOSE_BTN).isDisplayed());
			String profileTitle = getElementText(PROFILE_1_TITLE);
			PageObjectHelper.log(LOGGER, "After clearing text, Custom SP: " + profileTitle + " is retained in Profiles List");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_added_custom_sp_is_not_cleared_from_profiles_list_in_job_comparison_page", "Issue verifying Custom SP retained in Profiles List after clearing text", e);
		}
	}

	public void select_third_custom_sp_from_search_results() {
		waitForSpinners();
		try {
			Assert.assertTrue(waitForElement(THIRD_SEARCH_RESULT_BTN).isDisplayed());
			customSPNameinSearchResults.set(getElementText(THIRD_SEARCH_RESULT_TEXT));
			clickElement(THIRD_SEARCH_RESULT_BTN);
			PageObjectHelper.log(LOGGER, "Third Custom SP with Name: " + customSPNameinSearchResults.get() + " selected");
			wait.until(ExpectedConditions.invisibilityOfElementLocated(THIRD_SEARCH_RESULT_BTN));
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "select_third_custom_sp_from_search_results", "Issue selecting Third Custom SP from Search Results", e);
		}
	}

	public void verify_new_custom_sp_replaces_existing_custom_sp_in_profiles_list_in_job_comparison_page() {
		waitForSpinners();
		try {
			scrollToElement(driver.findElement(CUSTOM_SP_CLOSE_BTN));
			Assert.assertTrue(waitForElement(CUSTOM_SP_CLOSE_BTN).isDisplayed());
			String profileTitle = getElementText(PROFILE_1_TITLE);
			Assert.assertEquals(profileTitle, customSPNameinSearchResults.get());
			PageObjectHelper.log(LOGGER, "New Custom SP: " + profileTitle + " successfully replaced existing Custom SP");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_new_custom_sp_replaces_existing_custom_sp_in_profiles_list_in_job_comparison_page", "Issue verifying Custom SP replacement", e);
		}
	}

	public void clear_custom_sp_and_text_in_search_bar_with_close_button_on_the_profile() {
		waitForSpinners();
		try {
			scrollToElement(driver.findElement(CUSTOM_SP_CLOSE_BTN));
			jsClick(driver.findElement(CUSTOM_SP_CLOSE_BTN));
			PageObjectHelper.log(LOGGER, "Clicked on close button on Custom SP Profile");
			wait.until(ExpectedConditions.attributeToBe(SEARCH_BAR_JC, "value", ""));
			Assert.assertEquals(driver.findElement(SEARCH_BAR_JC).getAttribute("value"), "");
			PageObjectHelper.log(LOGGER, "Custom SP and text in Search bar cleared successfully");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "clear_custom_sp_and_text_in_search_bar_with_close_button_on_the_profile", "Issue clearing Custom SP and text in Search bar", e);
		}
	}
}
