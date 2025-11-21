package com.kfonetalentsuite.pageobjects.JobMapping;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.Utilities;
import com.kfonetalentsuite.utils.PageObjectHelper;
import com.kfonetalentsuite.webdriverManager.DriverManager;

public class PO13_AddandVerifyCustomSPinJobComparisonPage {
	WebDriver driver = DriverManager.getDriver();	

	protected static final Logger LOGGER = (Logger) LogManager.getLogger();
	PO13_AddandVerifyCustomSPinJobComparisonPage addandVerifyCustomSPinJobComparisonPage;
	
	// THREAD-SAFE: Each thread gets its own isolated state for parallel execution
	public static ThreadLocal<String> customSPSearchString = ThreadLocal.withInitial(() -> "Engineer");
	public static ThreadLocal<String> customSPNameinSearchResults = ThreadLocal.withInitial(() -> null);
	
	public PO13_AddandVerifyCustomSPinJobComparisonPage() throws IOException {
		// super();
		PageFactory.initElements(driver, this);
	}

	WebDriverWait wait = DriverManager.getWait();
	Utilities utils = new Utilities();
	JavascriptExecutor js = (JavascriptExecutor) driver;
	
	//XPATHs
	@FindBy(xpath = "//div[@data-testid='loader']//img")
	WebElement pageLoadSpinner2;
	
	@FindBy(xpath = "//input[contains(@id,'jobcompare-search')]")
	WebElement searchBarinJCPage;
	
	@FindBy(xpath = "//*[contains(@id,'cancel-icon')]//..")
	WebElement searchBarCancelBtninJCPage;
	
	@FindBy(xpath = "//ul//li[1]//button")
	WebElement firstSearchResultBtninJCPage;
	
	@FindBy(xpath = "//ul//li[3]//button")
	WebElement thirdSearchResultBtninJCPage;
	
	@FindBy(xpath = "//ul//li[1]//button//div")
	WebElement firstSearchResultTextinJCPage;
	
	@FindBy(xpath = "//ul//li[3]//button//div")
	WebElement thirdSearchResultTextinJCPage;
	
	@FindBy(xpath = "//div[@class='shadow']//div[contains(@id,'card-header')]//button")
	WebElement customSPCloseBtninJCPage;
	
	@FindBy(xpath = "//div[@class='shadow']//div[contains(@id,'card-title')]")
	WebElement JCpageProfile1Title;
	
	@FindBy(xpath = "//div[@class='shadow']//div[contains(@id,'card-header')][1]//span")
	WebElement JCpageProfile1SelectBtn;
	
	@FindBy(xpath = "//div[@class='shadow']//div[contains(@id,'grade')]")
	WebElement JCpageProfile1Grade;
	
	@FindBy(xpath = "//div[@class='shadow']//div[contains(@id,'level-sublevels')]")
	WebElement JCpageProfile1Level;
	
	@FindBy(xpath = "//div[@class='shadow']//div[contains(@id,'function-subfunction')]")
	WebElement JCpageProfile1Function;
	
	@FindBy(xpath = "//div[@class='shadow']//div[contains(@id,'seniority-level')]")
	WebElement JCpageProfile1SeniorityLevel;
	
	@FindBy(xpath = "//div[@class='shadow']//div[contains(@id,'managerial-experience')]")
	WebElement JCpageProfile1ManagerialExperience;
	
	@FindBy(xpath = "//div[@class='shadow']//div[contains(@id,'education')]")
	WebElement JCpageProfile1Education;
	
	@FindBy(xpath = "//div[@class='shadow']//div[contains(@id,'general-experience')]")
	WebElement JCpageProfile1GeneralExperience;
	
	@FindBy(xpath = "//div[@class='shadow']//div[contains(@id,'role-summary')]")
	WebElement JCpageProfile1RoleSummary;
	
	@FindBy(xpath = "//div[@class='shadow']//div[contains(@id,'responsibilities')]")
	WebElement JCpageProfile1Responsibilities;
	
	@FindBy(xpath = "//div[contains(@id,'responsibilities')]//button[@data-testid='view-more-responsibilities']")
	List <WebElement> JCpageProfile1ViewMoreResponsibilitiesBtn;
	
	
	@FindBy(xpath = "//div[@class='shadow']//div[contains(@id,'behavioural-competencies')]")
	WebElement JCpageProfile1BehaviouralCompetencies;
	
	@FindBy(xpath = "//div[contains(@id,'behavioural-competencies')]//button[@data-testid='view-more-competencies']")
	List <WebElement> JCpageProfile1ViewMoreCompetenciesBtn;
	
	
	@FindBy(xpath = "//div[@class='shadow']//div[contains(@id,'skills')]")
	WebElement JCpageProfile1Skills;
	
	@FindBy(xpath = "//div[contains(@id,'skills')]//button[@data-testid='view-more-skills']")
	List <WebElement> JCpageProfile1ViewMoreSkillsBtn;
	
	
	//METHODs
	public void click_on_search_bar_in_job_comparison_page() {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
		try {
	wait.until(ExpectedConditions.elementToBeClickable(searchBarinJCPage));
	js.executeScript("arguments[0].click();", searchBarinJCPage);
	PageObjectHelper.log(LOGGER, "Clicked on Search bar in Job Comparison page");
} catch (Exception e) {
	PageObjectHelper.handleError(LOGGER, "click_on_search_bar_in_job_comparison_page",
		"Issue clicking Search bar in Job Comparison page", e);
}
	}
	
	public void verify_search_bar_placeholder_text_in_job_comparison_page() {
		try {
	String placeholderText = wait.until(ExpectedConditions.visibilityOf(searchBarinJCPage)).getAttribute("placeholder");
	Assert.assertEquals(placeholderText,"Search Korn Ferry Success Profiles...");
	PageObjectHelper.log(LOGGER, "Search bar Placeholder text verified successfully in Job Comparison page");
} catch (Exception e) {
	PageObjectHelper.handleError(LOGGER, "verify_search_bar_placeholder_text_in_job_comparison_page",
		"Issue verifying Search bar Placeholder text", e);
}
	}
	
	public void user_should_enter_custom_sp_search_string_in_the_search_bar() {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
		try {
	wait.until(ExpectedConditions.visibilityOf(searchBarinJCPage)).sendKeys(customSPSearchString.get());
	PageObjectHelper.log(LOGGER, "Entered " + customSPSearchString.get() + " as Custom SP Search String in search bar");
} catch (Exception e) {
	PageObjectHelper.handleError(LOGGER, "user_should_enter_custom_sp_search_string_in_the_search_bar",
		"Failed to enter Custom SP Search String", e);
}
	}
	
	public void select_first_custom_sp_from_search_results() {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
		try {
		Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(firstSearchResultBtninJCPage)).isDisplayed());
		wait.until(ExpectedConditions.elementToBeClickable(firstSearchResultBtninJCPage)).click();
	customSPNameinSearchResults.set(firstSearchResultTextinJCPage.getText());
	PageObjectHelper.log(LOGGER, "First Custom SP with Name: " + customSPNameinSearchResults.get() + " selected from search results");
	wait.until(ExpectedConditions.invisibilityOf(firstSearchResultBtninJCPage));
} catch (Exception e) {
	PageObjectHelper.handleError(LOGGER, "select_first_custom_sp_from_search_results",
		"Issue selecting First Custom SP from Search Results", e);
}	
	}
	
	public void verify_custom_sp_added_to_profiles_list_in_job_comparison_page() {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
		try {
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(customSPCloseBtninJCPage)).isDisplayed());
	String JCpageProfile1TitleText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1Title)).getText();
	PageObjectHelper.log(LOGGER, "Custom SP with Name: " + JCpageProfile1TitleText + " added to Profiles List");
} catch (Exception e) {
	PageObjectHelper.handleError(LOGGER, "verify_custom_sp_added_to_profiles_list_in_job_comparison_page",
		"Issue adding Custom SP to Profiles List", e);
}
	}
	
	public void user_should_verify_custom_sp_name_and_close_button_are_displaying_in_search_bar_after_adding_custom_sp() {
		try {
	String customSPNameTextinSearchBar = wait.until(ExpectedConditions.visibilityOf(searchBarinJCPage)).getAttribute("value");
	Assert.assertEquals(customSPNameTextinSearchBar, customSPNameinSearchResults.get());
	PageObjectHelper.log(LOGGER, "Custom SP Name is displaying in search bar");
	Assert.assertTrue(wait.until(ExpectedConditions.elementToBeClickable(searchBarCancelBtninJCPage)).isDisplayed());
	PageObjectHelper.log(LOGGER, "Close or Cancel Button is displaying in search bar");
} catch (Exception e) {
	PageObjectHelper.handleError(LOGGER, "user_should_verify_custom_sp_name_and_close_button_are_displaying_in_search_bar_after_adding_custom_sp",
		"Issue verifying Custom SP Name and Close button in Search Bar", e);
}
	}
	
public void user_added_custom_sp_to_profiles_list_in_job_comparison_page() {
	PageObjectHelper.log(LOGGER, "User added Custom SP to Profiles List");
}
	
	public void validate_custom_sp_profile_name_matches_with_selected_custom_sp_name_from_search_results() {
		try {
	String JCpageProfile1TitleText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1Title)).getText();
	Assert.assertEquals(JCpageProfile1TitleText,customSPNameinSearchResults.get());
	PageObjectHelper.log(LOGGER, "Custom SP Profile Name: " + JCpageProfile1TitleText + " matches Selected Custom SP Name: " + customSPNameinSearchResults.get());
} catch (Exception e) {
	PageObjectHelper.handleError(LOGGER, "validate_custom_sp_profile_name_matches_with_selected_custom_sp_name_from_search_results",
		"Issue validating Custom SP Profile Name with Selected Custom SP Name", e);
}
	}
	
	public void user_should_verify_close_button_and_select_button_are_displaying_on_the_profile() {
		try {
	Assert.assertTrue(wait.until(ExpectedConditions.elementToBeClickable(customSPCloseBtninJCPage)).isDisplayed());
	PageObjectHelper.log(LOGGER, "Close Button is displaying on Custom SP Profile");
	Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(JCpageProfile1SelectBtn)).isDisplayed());
	Assert.assertTrue(!(wait.until(ExpectedConditions.visibilityOf(JCpageProfile1SelectBtn)).isSelected()));
	PageObjectHelper.log(LOGGER, "Select button is displaying and is in De-Select status");
	utils.jsClick(driver, JCpageProfile1SelectBtn);
	Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(JCpageProfile1SelectBtn)).isEnabled());
	PageObjectHelper.log(LOGGER, "Custom SP Profile selected successfully");
} catch (Exception e) {
	PageObjectHelper.handleError(LOGGER, "user_should_verify_close_button_and_select_button_are_displaying_on_the_profile",
		"Issue verifying Close and Select buttons", e);
}
	}
	
	public void verify_custom_sp_profile_grade() {
		try {
	String JCpageProfile1GradeText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1Grade)).getText();
	PageObjectHelper.log(LOGGER, "Custom SP Profile Grade: " + JCpageProfile1GradeText);
} catch (Exception e) {
	PageObjectHelper.handleError(LOGGER, "verify_custom_sp_profile_grade",
		"Issue verifying Custom SP Profile Grade", e);
}
	}
	
	public void verify_custom_sp_profile_level_sublevels() {
		try {
	String JCpageProfile1LevelText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1Level)).getText();
	PageObjectHelper.log(LOGGER, "Custom SP Profile Level / Sub-levels: " + JCpageProfile1LevelText);
} catch (Exception e) {
	PageObjectHelper.handleError(LOGGER, "verify_custom_sp_profile_level_sublevels",
		"Issue verifying Custom SP Profile Level / Sub-levels", e);
}
	}
	
	public void verify_custom_sp_profile_function_subfunction() {
		try {
	js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1Function);
	String JCpageProfile1FunctionText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1Function)).getText();
	PageObjectHelper.log(LOGGER, "Custom SP Profile Function / Sub-function: " + JCpageProfile1FunctionText);
} catch (Exception e) {
	PageObjectHelper.handleError(LOGGER, "verify_custom_sp_profile_function_subfunction",
		"Issue verifying Custom SP Profile Function / Sub-function", e);
}
	}
	
	public void verify_custom_sp_profile_seniority_level() {
		try {
	js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1SeniorityLevel);
	String JCpageProfile1SeniorityLevelText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1SeniorityLevel)).getText();
	PageObjectHelper.log(LOGGER, "Custom SP Profile Seniority level: " + JCpageProfile1SeniorityLevelText);
} catch (Exception e) {
	PageObjectHelper.handleError(LOGGER, "verify_custom_sp_profile_seniority_level",
		"Issue verifying Custom SP Profile Seniority level", e);
}
	}
	
	public void verify_custom_sp_profile_managerial_experience() {
		try {
	js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1ManagerialExperience);
	String JCpageProfile1ManagerialExperienceText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1ManagerialExperience)).getText();
	PageObjectHelper.log(LOGGER, "Custom SP Profile Managerial experience: " + JCpageProfile1ManagerialExperienceText);
} catch (Exception e) {
	PageObjectHelper.handleError(LOGGER, "verify_custom_sp_profile_managerial_experience",
		"Issue verifying Custom SP Profile Managerial experience", e);
}
	}
	
public void verify_custom_sp_profile_education() {
	try {
		js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1Education);
		String JCpageProfile1EducationText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1Education)).getText();
		PageObjectHelper.log(LOGGER, "Custom SP Profile Education: " + JCpageProfile1EducationText);
	} catch (Exception e) {
		PageObjectHelper.handleError(LOGGER, "verify_custom_sp_profile_education",
			"Issue verifying Custom SP Profile Education", e);
	}
}
	
	public void verify_custom_sp_profile_general_experience() {
		try {
	js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1GeneralExperience);
	String JCpageProfile1GeneralExperienceText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1GeneralExperience)).getText();
	PageObjectHelper.log(LOGGER, "Custom SP Profile General experience: " + JCpageProfile1GeneralExperienceText);
} catch (Exception e) {
	PageObjectHelper.handleError(LOGGER, "verify_custom_sp_profile_general_experience",
		"Issue verifying Custom SP Profile General experience", e);
}
	}
	
	public void verify_custom_sp_profile_role_summary() {
		try {
	js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1RoleSummary);
	String JCpageProfile1RoleSummaryText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1RoleSummary)).getText();
	PageObjectHelper.log(LOGGER, "Custom SP Profile Role Summary: " + JCpageProfile1RoleSummaryText);
} catch (Exception e) {
	PageObjectHelper.handleError(LOGGER, "verify_custom_sp_profile_role_summary",
		"Issue verifying Custom SP Profile Role Summary", e);
}
	}
	
	public void verify_custom_sp_profile_responsibilities() {
		try {
	js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1Responsibilities);
	wait.until(ExpectedConditions.elementToBeClickable(JCpageProfile1ViewMoreResponsibilitiesBtn.get(0))).click();
	while(true) {
		try {
			if(JCpageProfile1ViewMoreResponsibilitiesBtn.isEmpty()) {
				break;
			} 
			JCpageProfile1ViewMoreResponsibilitiesBtn.get(0).click();
			js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1ViewMoreResponsibilitiesBtn.get(0));
			PerformanceUtils.waitForUIStability(driver, 2);
		} catch(StaleElementReferenceException e) {
			break;
		}	
	}
	String JCpageProfile1ResponsibilitiesText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1Responsibilities)).getText();
	PageObjectHelper.log(LOGGER, "Custom SP Profile Responsibilities verified (" + JCpageProfile1ResponsibilitiesText.length() + " chars)");
} catch (Exception e) {
	PageObjectHelper.handleError(LOGGER, "verify_custom_sp_profile_responsibilities",
		"Issue verifying Custom SP Profile Responsibilities", e);
}
		
	}
	
	public void verify_custom_sp_profile_behavioural_competencies() {
		try {
	js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1BehaviouralCompetencies);
	wait.until(ExpectedConditions.elementToBeClickable(JCpageProfile1ViewMoreCompetenciesBtn.get(0))).click();
	while(true) {
		try {
			if(JCpageProfile1ViewMoreCompetenciesBtn.isEmpty()) {
				break;
			} 
			JCpageProfile1ViewMoreCompetenciesBtn.get(0).click();
			js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1ViewMoreCompetenciesBtn.get(0));
			PerformanceUtils.waitForUIStability(driver, 2);
		} catch(StaleElementReferenceException e) {
			break;
		}	
	}
	String JCpageProfile1BehaviouralCompetenciesText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1BehaviouralCompetencies)).getText();
	PageObjectHelper.log(LOGGER, "Custom SP Profile Behavioural Competencies verified (" + JCpageProfile1BehaviouralCompetenciesText.length() + " chars)");
} catch (Exception e) {
	PageObjectHelper.handleError(LOGGER, "verify_custom_sp_profile_behavioural_competencies",
		"Issue verifying Custom SP Profile Behavioural Competencies", e);
}
	}
	
	public void verify_custom_sp_profile_skills() {
		try {
	js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1Skills);
	wait.until(ExpectedConditions.elementToBeClickable(JCpageProfile1ViewMoreSkillsBtn.get(0))).click();
	while(true) {
		try {
			if(JCpageProfile1ViewMoreSkillsBtn.isEmpty()) {
				break;
			} 
			JCpageProfile1ViewMoreSkillsBtn.get(0).click();
			js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1ViewMoreSkillsBtn.get(0));
			PerformanceUtils.waitForUIStability(driver, 2);
		} catch(StaleElementReferenceException e) {
			break;
		}	
	}
	String JCpageProfile1SkillsText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1Skills)).getText();
	PageObjectHelper.log(LOGGER, "Custom SP Profile Skills verified (" + JCpageProfile1SkillsText.length() + " chars)");
} catch (Exception e) {
	PageObjectHelper.handleError(LOGGER, "verify_custom_sp_profile_skills",
		"Issue verifying Custom SP Profile Skills", e);
}
	}
	
	public void clear_text_in_search_bar_with_clear_button_in_the_search_bar() {
		try {
	js.executeScript("window.scrollTo(document.body.scrollHeight, 0)");
	Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(searchBarCancelBtninJCPage)).isDisplayed());
	utils.jsClick(driver, searchBarCancelBtninJCPage);
	PageObjectHelper.log(LOGGER, "Clicked on clear button and cleared text in Search bar");
} catch (Exception e) {
	PageObjectHelper.handleError(LOGGER, "clear_text_in_search_bar_with_clear_button_in_the_search_bar",
		"Issue clearing text with clear button in Search bar", e);
}
	}
	
	public void user_should_verify_added_custom_sp_is_not_cleared_from_profiles_list_in_job_comparison_page() {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
		try {
	Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(customSPCloseBtninJCPage)).isDisplayed());
	String JCpageProfile1TitleText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1Title)).getText();
	PageObjectHelper.log(LOGGER, "After clearing text, Custom SP: " + JCpageProfile1TitleText + " is retained in Profiles List");
} catch (Exception e) {
	PageObjectHelper.handleError(LOGGER, "user_should_verify_added_custom_sp_is_not_cleared_from_profiles_list_in_job_comparison_page",
		"Issue verifying Custom SP retained in Profiles List after clearing text", e);
}
	}
	
	public void select_third_custom_sp_from_search_results() {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
		try {
	Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(thirdSearchResultBtninJCPage)).isDisplayed());
	wait.until(ExpectedConditions.elementToBeClickable(thirdSearchResultBtninJCPage)).click();
	customSPNameinSearchResults.set(thirdSearchResultTextinJCPage.getText());
	PageObjectHelper.log(LOGGER, "Third Custom SP with Name: " + customSPNameinSearchResults.get() + " selected");
	wait.until(ExpectedConditions.invisibilityOf(thirdSearchResultBtninJCPage));
} catch (Exception e) {
	PageObjectHelper.handleError(LOGGER, "select_third_custom_sp_from_search_results",
		"Issue selecting Third Custom SP from Search Results", e);
}
	}
	
	public void verify_new_custom_sp_replaces_existing_custom_sp_in_profiles_list_in_job_comparison_page() {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
		try {
	js.executeScript("arguments[0].scrollIntoView(true);", customSPCloseBtninJCPage);
	Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(customSPCloseBtninJCPage)).isDisplayed());
	String JCpageProfile1TitleText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1Title)).getText();
	Assert.assertEquals(JCpageProfile1TitleText, customSPNameinSearchResults.get());
	PageObjectHelper.log(LOGGER, "New Custom SP: " + JCpageProfile1TitleText + " successfully replaced existing Custom SP");
} catch (Exception e) {
	PageObjectHelper.handleError(LOGGER, "verify_new_custom_sp_replaces_existing_custom_sp_in_profiles_list_in_job_comparison_page",
		"Issue verifying Custom SP replacement", e);
}
	}
	
	public void clear_custom_sp_and_text_in_search_bar_with_close_button_on_the_profile() {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
		try {
	js.executeScript("arguments[0].scrollIntoView(true);", customSPCloseBtninJCPage);
	utils.jsClick(driver, customSPCloseBtninJCPage);
	PageObjectHelper.log(LOGGER, "Clicked on close button on Custom SP Profile");
	wait.until(ExpectedConditions.attributeToBe(searchBarinJCPage, "value", ""));
	Assert.assertEquals(searchBarinJCPage.getAttribute("value"), "");
	PageObjectHelper.log(LOGGER, "Custom SP and text in Search bar cleared successfully");
} catch (Exception e) {
	PageObjectHelper.handleError(LOGGER, "clear_custom_sp_and_text_in_search_bar_with_close_button_on_the_profile",
		"Issue clearing Custom SP and text in Search bar", e);
}
	}
}

