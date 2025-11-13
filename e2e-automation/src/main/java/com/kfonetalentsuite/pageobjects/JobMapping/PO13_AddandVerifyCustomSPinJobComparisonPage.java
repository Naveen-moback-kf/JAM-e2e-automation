package com.kfonetalentsuite.pageobjects.JobMapping;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.ScreenshotHandler;
import com.kfonetalentsuite.utils.JobMapping.Utilities;
import com.kfonetalentsuite.webdriverManager.DriverManager;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

public class PO13_AddandVerifyCustomSPinJobComparisonPage {
	WebDriver driver = DriverManager.getDriver();	

	protected static final Logger LOGGER = (Logger) LogManager.getLogger();
	PO13_AddandVerifyCustomSPinJobComparisonPage addandVerifyCustomSPinJobComparisonPage;
	
	public static String customSPSearchString = "Engineer";
	public static String customSPNameinSearchResults;
	
	public PO13_AddandVerifyCustomSPinJobComparisonPage() throws IOException {
		// super();
		PageFactory.initElements(driver, this);
	}

	WebDriverWait wait = DriverManager.getWait();
	Utilities utils = new Utilities();
	JavascriptExecutor js = (JavascriptExecutor) driver;
	
	//XPATHs
	@FindBy(xpath = "//div[@data-testid='loader']//img")
	@CacheLookup
	WebElement pageLoadSpinner2;
	
	@FindBy(xpath = "//input[contains(@id,'jobcompare-search')]")
	@CacheLookup
	WebElement searchBarinJCPage;
	
	@FindBy(xpath = "//*[contains(@id,'cancel-icon')]//..")
	@CacheLookup
	WebElement searchBarCancelBtninJCPage;
	
	@FindBy(xpath = "//ul//li[1]//button")
	@CacheLookup
	WebElement firstSearchResultBtninJCPage;
	
	@FindBy(xpath = "//ul//li[3]//button")
	@CacheLookup
	WebElement thirdSearchResultBtninJCPage;
	
	@FindBy(xpath = "//ul//li[1]//button//div")
	@CacheLookup
	WebElement firstSearchResultTextinJCPage;
	
	@FindBy(xpath = "//ul//li[3]//button//div")
	@CacheLookup
	WebElement thirdSearchResultTextinJCPage;
	
	@FindBy(xpath = "//div[@class='shadow']//div[contains(@id,'card-header')]//button")
	@CacheLookup
	WebElement customSPCloseBtninJCPage;
	
	@FindBy(xpath = "//div[@class='shadow']//div[contains(@id,'card-title')]")
	@CacheLookup
	WebElement JCpageProfile1Title;
	
	@FindBy(xpath = "//div[@class='shadow']//div[contains(@id,'card-header')][1]//span")
	@CacheLookup
	WebElement JCpageProfile1SelectBtn;
	
	@FindBy(xpath = "//div[@class='shadow']//div[contains(@id,'grade')]")
	@CacheLookup
	WebElement JCpageProfile1Grade;
	
	@FindBy(xpath = "//div[@class='shadow']//div[contains(@id,'level-sublevels')]")
	@CacheLookup
	WebElement JCpageProfile1Level;
	
	@FindBy(xpath = "//div[@class='shadow']//div[contains(@id,'function-subfunction')]")
	@CacheLookup
	WebElement JCpageProfile1Function;
	
	@FindBy(xpath = "//div[@class='shadow']//div[contains(@id,'seniority-level')]")
	@CacheLookup
	WebElement JCpageProfile1SeniorityLevel;
	
	@FindBy(xpath = "//div[@class='shadow']//div[contains(@id,'managerial-experience')]")
	@CacheLookup
	WebElement JCpageProfile1ManagerialExperience;
	
	@FindBy(xpath = "//div[@class='shadow']//div[contains(@id,'education')]")
	@CacheLookup
	WebElement JCpageProfile1Education;
	
	@FindBy(xpath = "//div[@class='shadow']//div[contains(@id,'general-experience')]")
	@CacheLookup
	WebElement JCpageProfile1GeneralExperience;
	
	@FindBy(xpath = "//div[@class='shadow']//div[contains(@id,'role-summary')]")
	@CacheLookup
	WebElement JCpageProfile1RoleSummary;
	
	@FindBy(xpath = "//div[@class='shadow']//div[contains(@id,'responsibilities')]")
	@CacheLookup
	WebElement JCpageProfile1Responsibilities;
	
	@FindBy(xpath = "//div[contains(@id,'responsibilities')]//button[@data-testid='view-more-responsibilities']")
	@CacheLookup
	List <WebElement> JCpageProfile1ViewMoreResponsibilitiesBtn;
	
	
	@FindBy(xpath = "//div[@class='shadow']//div[contains(@id,'behavioural-competencies')]")
	@CacheLookup
	WebElement JCpageProfile1BehaviouralCompetencies;
	
	@FindBy(xpath = "//div[contains(@id,'behavioural-competencies')]//button[@data-testid='view-more-competencies']")
	@CacheLookup
	List <WebElement> JCpageProfile1ViewMoreCompetenciesBtn;
	
	
	@FindBy(xpath = "//div[@class='shadow']//div[contains(@id,'skills')]")
	@CacheLookup
	WebElement JCpageProfile1Skills;
	
	@FindBy(xpath = "//div[contains(@id,'skills')]//button[@data-testid='view-more-skills']")
	@CacheLookup
	List <WebElement> JCpageProfile1ViewMoreSkillsBtn;
	
	
	//METHODs
	public void click_on_search_bar_in_job_comparison_page() {
		wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
		try {
			wait.until(ExpectedConditions.elementToBeClickable(searchBarinJCPage));
			js.executeScript("arguments[0].click();", searchBarinJCPage);
		LOGGER.info("Clicked on Search bar in Job Comparison page....");
		ExtentCucumberAdapter.addTestStepLog("Clicked on Search bar in Job Comparison page....");
	} catch (Exception e) {
		ScreenshotHandler.captureFailureScreenshot("click_on_search_bar_in_job_comparison_page", e);
		LOGGER.error(" Issue in clicking Search bar in Job Comparison page - Method: click_on_search_bar_in_job_comparison_page", e);
		e.printStackTrace();
		ExtentCucumberAdapter.addTestStepLog(" Issue in clicking Search bar in Job Comparison page...Please Investigate!!!");
		Assert.fail("Issue in clicking Search bar in Job Comparison page...Please Investigate!!!");
	}
	}
	
	public void verify_search_bar_placeholder_text_in_job_comparison_page() {
		try {
			String placeholderText = wait.until(ExpectedConditions.visibilityOf(searchBarinJCPage)).getAttribute("placeholder");
		Assert.assertEquals(placeholderText,"Search Korn Ferry Success Profiles...");
		LOGGER.info("Search bar Placeholder text is Verified Successfully in Job Comparison page....");
		ExtentCucumberAdapter.addTestStepLog("Search bar Placeholder text is Verified Successfully in Job Comparison page....");
	} catch (Exception e) {
		ScreenshotHandler.captureFailureScreenshot("verify_search_bar_placeholder_text_in_job_comparison_page", e);
		LOGGER.error(" Issue in verifying Search bar Placeholder text - Method: verify_search_bar_placeholder_text_in_job_comparison_page", e);
		e.printStackTrace();
		ExtentCucumberAdapter.addTestStepLog(" Issue in Verifying Search bar Placeholder text in Job Comparison page...Please Investigate!!!");
		Assert.fail("Issue in Verifying Search bar Placeholder text in Job Comparison page...Please Investigate!!!");
	}
	}
	
	public void user_should_enter_custom_sp_search_string_in_the_search_bar() {
		wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
		try {
			wait.until(ExpectedConditions.visibilityOf(searchBarinJCPage)).sendKeys(customSPSearchString);
		LOGGER.info("Entered " + customSPSearchString + " as Custom SP Search String in the search bar in Job Comparison page");
		ExtentCucumberAdapter.addTestStepLog("Entered " + customSPSearchString + " as Custom SP Search String in the search bar in Job Comparison page");
	} catch (Exception e) {
		ScreenshotHandler.captureFailureScreenshot("user_should_enter_custom_sp_search_string_in_the_search_bar", e);
		LOGGER.error(" Failed to enter Custom SP Search String - Method: user_should_enter_custom_sp_search_string_in_the_search_bar", e);
		e.printStackTrace();
		ExtentCucumberAdapter.addTestStepLog(" Failed to enter Custom SP Search String in the search bar in Job Comparison page...Please Investigate!!!");
		Assert.fail("Failed to enter Custom SP Search String in the search bar in Job Comparison page...Please Investigate!!!");
	}
	}
	
	public void select_first_custom_sp_from_search_results() {
		wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
		try {
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(firstSearchResultBtninJCPage)).isDisplayed());
			wait.until(ExpectedConditions.elementToBeClickable(firstSearchResultBtninJCPage)).click();
		customSPNameinSearchResults = firstSearchResultTextinJCPage.getText();
		LOGGER.info("First Custom SP with Name : "+ customSPNameinSearchResults + " from search results is selected in Job Comparison page");
		ExtentCucumberAdapter.addTestStepLog("First Custom SP with Name : "+ customSPNameinSearchResults + " from search results is selected in Job Comparison page");
		wait.until(ExpectedConditions.invisibilityOf(firstSearchResultBtninJCPage));
	} catch (Exception e) {
		ScreenshotHandler.captureFailureScreenshot("select_first_custom_sp_from_search_results", e);
		LOGGER.error(" Issue in selecting First Custom SP from Search Results - Method: select_first_custom_sp_from_search_results", e);
		e.printStackTrace();
		ExtentCucumberAdapter.addTestStepLog(" Issue in Selecting First Custom SP from Search Results in Job Comparison page...Please Investigate!!!");
		Assert.fail("Issue in Selecting First Custom SP from Search Results in Job Comparison page...Please Investigate!!!");
	}	
	}
	
	public void verify_custom_sp_added_to_profiles_list_in_job_comparison_page() {
		wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
		try {
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(customSPCloseBtninJCPage)).isDisplayed());
			String JCpageProfile1TitleText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1Title)).getText();
			LOGGER.info("Custom SP with Name : "+ JCpageProfile1TitleText + " added to Profiles List in Job Comparison page");
			ExtentCucumberAdapter.addTestStepLog("Custom SP with Name : "+ JCpageProfile1TitleText + " added to Profiles List in Job Comparison page");
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_custom_sp_added_to_profiles_list_in_job_comparison_page", e);
			LOGGER.error(" Issue in adding Custom SP to Profiles List - Method: verify_custom_sp_added_to_profiles_list_in_job_comparison_page", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog(" Issue in adding Custom SP to Profiles List in Job Comparison page...Please Investigate!!!");
			Assert.fail("Issue in adding Custom SP to Profiles List in Job Comparison page...Please Investigate!!!");
		}	
	}
	
	public void user_should_verify_custom_sp_name_and_close_button_are_displaying_in_search_bar_after_adding_custom_sp() {
		try {
			String customSPNameTextinSearchBar = wait.until(ExpectedConditions.visibilityOf(searchBarinJCPage)).getAttribute("value");
			Assert.assertEquals(customSPNameTextinSearchBar, customSPNameinSearchResults);
			LOGGER.info("Custom SP Name is displaying in the search bar after adding Custom SP to Profiles List in Job Comparison page....");
			ExtentCucumberAdapter.addTestStepLog("Custom SP Name is displaying in the search bar after adding Custom SP to Profiles List in Job Comparison page....");
			Assert.assertTrue(wait.until(ExpectedConditions.elementToBeClickable(searchBarCancelBtninJCPage)).isDisplayed());
			LOGGER.info("Close or Cancel Button is displaying in search bar after adding Custom SP to Profiles List in Job Comparison page....");
			ExtentCucumberAdapter.addTestStepLog("Close or Cancel Button is displaying in search bar after adding Custom SP to Profiles List in Job Comparison page....");
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_verify_custom_sp_name_and_close_button_are_displaying_in_search_bar_after_adding_custom_sp", e);
			LOGGER.error(" Issue in verifying Custom SP Name and Close button in Search Bar - Method: user_should_verify_custom_sp_name_and_close_button_are_displaying_in_search_bar_after_adding_custom_sp", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog(" Issue in Verifying Custom SP Name and Close button in Search Bar after Adding Custom SP in Job Comparison page...Please Investigate!!!");
			Assert.fail("Issue in Verifying Custom SP Name and Close button in Search Bar after Adding Custom SP in Job Comparison page...Please Investigate!!!");
		}
	}
	
	public void user_added_custom_sp_to_profiles_list_in_job_comparison_page() {
		LOGGER.info("User added Custom SP to Profiles List in Job Comparison page...");
		ExtentCucumberAdapter.addTestStepLog("User added Custom SP to Profiles List in Job Comparison page...");
	}
	
	public void validate_custom_sp_profile_name_matches_with_selected_custom_sp_name_from_search_results() {
		try {
			String JCpageProfile1TitleText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1Title)).getText();
			Assert.assertEquals(JCpageProfile1TitleText,customSPNameinSearchResults);
			LOGGER.info("Custom SP Profile Name :  " + JCpageProfile1TitleText + " matches with Selected Custom SP Name : " + customSPNameinSearchResults + " from Search Results in Job Comparison page...");
			ExtentCucumberAdapter.addTestStepLog("Custom SP Profile Name :  " + JCpageProfile1TitleText + " matches with Selected Custom SP Name : " + customSPNameinSearchResults + " from Search Results in Job Comparison page...");
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_custom_sp_profile_name_matches_with_selected_custom_sp_name_from_search_results", e);
			LOGGER.error("Issue in Validating Custom SP Profile Name with Selected Custom SP Name - Method: validate_custom_sp_profile_name_matches_with_selected_custom_sp_name_from_search_results", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in Validating Custom SP Profile Name with Selected Custom SP Name from Search Results in Job Comparison page...Please Investigate!!!");
			Assert.fail("Issue in Validating Custom SP Profile Name with Selected Custom SP Name from Search Results in Job Comparison page...Please Investigate!!!");
		}
	}
	
	public void user_should_verify_close_button_and_select_button_are_displaying_on_the_profile() {
		try {
			Assert.assertTrue(wait.until(ExpectedConditions.elementToBeClickable(customSPCloseBtninJCPage)).isDisplayed());
			LOGGER.info("Close Button is displaying on the Custom SP Profile as expected");
			ExtentCucumberAdapter.addTestStepLog("Close Button is displaying on the Custom SP Profile as expected");
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(JCpageProfile1SelectBtn)).isDisplayed());
			Assert.assertTrue(!(wait.until(ExpectedConditions.visibilityOf(JCpageProfile1SelectBtn)).isSelected()));
			LOGGER.info("Select button is displaying on the Custom SP Profile and is in De-Select status as Expected");
			ExtentCucumberAdapter.addTestStepLog("Select button is displaying on the Custom SP Profile and is in De-Select status as Expected");
			utils.jsClick(driver, JCpageProfile1SelectBtn);
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(JCpageProfile1SelectBtn)).isEnabled());
			LOGGER.info("Selected Custom SP Profile and is in Selected status as Expected");
			ExtentCucumberAdapter.addTestStepLog("Selected Custom SP Profile and is in Selected status as Expected");
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_verify_close_button_and_select_button_are_displaying_on_the_profile", e);
			LOGGER.error(" Issue in verifying Close and Select buttons on Custom SP Profile - Method: user_should_verify_close_button_and_select_button_are_displaying_on_the_profile", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog(" Issue in Verifying Close button and Select button on Custom SP Profile in the Job Compare page....Please Investigate!!!");
			Assert.fail("Issue in Verifying Close button and Select button on Custom SP Profile in the Job Compare page....Please Investigate!!!");
		}	
	}
	
	public void verify_custom_sp_profile_grade() {
		try {
			String JCpageProfile1GradeText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1Grade)).getText();
			LOGGER.info("Custom SP Profile Grade : " + JCpageProfile1GradeText);
			ExtentCucumberAdapter.addTestStepLog("Custom SP Profile Grade : " + JCpageProfile1GradeText);
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_custom_sp_profile_grade", e);
			LOGGER.error(" Issue in verifying Custom SP Profile Grade - Method: verify_custom_sp_profile_grade", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog(" Issue in Verifying Custom SP Profile Grade in the Job Compare page....Please Investigate!!!");
			Assert.fail("Issue in Verifying Custom SP Profile Grade in the Job Compare page....Please Investigate!!!");
		}
	}
	
	public void verify_custom_sp_profile_level_sublevels() {
		try {
			String JCpageProfile1LevelText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1Level)).getText();
			LOGGER.info("Custom SP Profile Level / Sub-levels : " + JCpageProfile1LevelText);
			ExtentCucumberAdapter.addTestStepLog("Custom SP Profile Level / Sub-levels : " + JCpageProfile1LevelText);
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_custom_sp_profile_level_sublevels", e);
			LOGGER.error("Issue in Verifying Custom SP Profile Level / Sub-levels - Method: verify_custom_sp_profile_level_sublevels", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying Custom SP Profile Level / Sub-levels in the Job Compare page....Please Investigate!!!");
			Assert.fail("Issue in Verifying Custom SP Profile Level / Sub-levels in the Job Compare page....Please Investigate!!!");
		}
	}
	
	public void verify_custom_sp_profile_function_subfunction() {
		try {
			js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1Function);
			String JCpageProfile1FunctionText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1Function)).getText();
			LOGGER.info("Custom SP Profile Function / Sub-function : " + JCpageProfile1FunctionText);
			ExtentCucumberAdapter.addTestStepLog("Custom SP Profile Function / Sub-function : " + JCpageProfile1FunctionText);
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_custom_sp_profile_function_subfunction", e);
			LOGGER.error("Issue in Verifying Custom SP Profile Function / Sub-function - Method: verify_custom_sp_profile_function_subfunction", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying Custom SP Profile Function / Sub-function in the Job Compare page....Please Investigate!!!");
			Assert.fail("Issue in Verifying Custom SP Profile Function / Sub-function in the Job Compare page....Please Investigate!!!");
		}
	}
	
	public void verify_custom_sp_profile_seniority_level() {
		try {
			js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1SeniorityLevel);
			String JCpageProfile1SeniorityLevelText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1SeniorityLevel)).getText();
			LOGGER.info("Custom SP Profile Seniority level : " + JCpageProfile1SeniorityLevelText);
			ExtentCucumberAdapter.addTestStepLog("Custom SP Profile Seniority level : " + JCpageProfile1SeniorityLevelText);
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_custom_sp_profile_seniority_level", e);
			LOGGER.error("Issue in Verifying Custom SP Profile Seniority level - Method: verify_custom_sp_profile_seniority_level", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying Custom SP Profile Seniority level in the Job Compare page....Please Investigate!!!");
			Assert.fail("Issue in Verifying Custom SP Profile Seniority level in the Job Compare page....Please Investigate!!!");
		}
	}
	
	public void verify_custom_sp_profile_managerial_experience() {
		try {
			js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1ManagerialExperience);
			String JCpageProfile1ManagerialExperienceText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1ManagerialExperience)).getText();
			LOGGER.info("Custom SP Profile Managerial experience : " + JCpageProfile1ManagerialExperienceText);
			ExtentCucumberAdapter.addTestStepLog("Custom SP Profile Managerial experience : " + JCpageProfile1ManagerialExperienceText);
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_custom_sp_profile_managerial_experience", e);
			LOGGER.error("Issue in Verifying Custom SP Profile Managerial experience - Method: verify_custom_sp_profile_managerial_experience", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying Custom SP Profile Managerial experience in the Job Compare page....Please Investigate!!!");
			Assert.fail("Issue in Verifying Custom SP Profile Managerial experience in the Job Compare page....Please Investigate!!!");
		}
	}
	
	public void verify_custom_sp_profile_education() {
		try {
			js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1Education);
			String JCpageProfile1EducationText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1Education)).getText();
			LOGGER.info("Custom SP Profile Education : " + JCpageProfile1EducationText);
			ExtentCucumberAdapter.addTestStepLog("Custom SP Profile Education : " + JCpageProfile1EducationText);
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_custom_sp_profile_education", e);
			LOGGER.error("Issue in Verifying Custom SP Profile Education - Method: verify_custom_sp_profile_education", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying Custom SP Profile Education in the Job Compare page....Please Investigate!!!");
			Assert.fail("Issue in Verifying Custom SP Profile Education in the Job Compare page....Please Investigate!!!");
		}
	}
	
	public void verify_custom_sp_profile_general_experience() {
		try {
			js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1GeneralExperience);
			String JCpageProfile1GeneralExperienceText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1GeneralExperience)).getText();
			LOGGER.info("Custom SP Profile General experience : " + JCpageProfile1GeneralExperienceText);
			ExtentCucumberAdapter.addTestStepLog("Custom SP Profile General experience : " + JCpageProfile1GeneralExperienceText);
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_custom_sp_profile_general_experience", e);
			LOGGER.error("Issue in Verifying Custom SP Profile General experience - Method: verify_custom_sp_profile_general_experience", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying Custom SP Profile General experience in the Job Compare page....Please Investigate!!!");
			Assert.fail("Issue in Verifying Custom SP Profile General experience in the Job Compare page....Please Investigate!!!");
		}
	}
	
	public void verify_custom_sp_profile_role_summary() {
		try {
			js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1RoleSummary);
			String JCpageProfile1RoleSummaryText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1RoleSummary)).getText();
			LOGGER.info("Custom SP Profile Role Summary : " + JCpageProfile1RoleSummaryText);
			ExtentCucumberAdapter.addTestStepLog("Custom SP Profile Role Summary : " + JCpageProfile1RoleSummaryText);
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_custom_sp_profile_role_summary", e);
			LOGGER.error("Issue in Verifying Custom SP Profile Role Summary - Method: verify_custom_sp_profile_role_summary", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying Custom SP Profile Role Summary in the Job Compare page....Please Investigate!!!");
			Assert.fail("Issue in Verifying Custom SP Profile Role Summary in the Job Compare page....Please Investigate!!!");
		}
	}
	
	public void verify_custom_sp_profile_responsibilities() {
		try {
			js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1Responsibilities);
			wait.until(ExpectedConditions.elementToBeClickable(JCpageProfile1ViewMoreResponsibilitiesBtn.get(0))).click();
			while(true) {
				try {
					if(JCpageProfile1ViewMoreResponsibilitiesBtn.isEmpty()) {
						LOGGER.info("Reached end of content in Custom SP Profile Responsibilities Section in Job Comparison Page");
						ExtentCucumberAdapter.addTestStepLog("Reached end of content in Custom SP Profile Responsibilities Section in Job Comparison Page");
						break;
					} 
					String ViewMoreResponsibilitiesBtnText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1ViewMoreResponsibilitiesBtn.get(0))).getText();
					JCpageProfile1ViewMoreResponsibilitiesBtn.get(0).click();
					js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1ViewMoreResponsibilitiesBtn.get(0));
					LOGGER.info("Clicked on "+ ViewMoreResponsibilitiesBtnText +" button in Custom SP Profile Responsibilities Section in Job Comparison page");
					ExtentCucumberAdapter.addTestStepLog("Clicked on "+ ViewMoreResponsibilitiesBtnText +" button in Custom SP Profile Responsibilities Section in Job Comparison page");
					PerformanceUtils.waitForUIStability(driver, 2);
				} catch(StaleElementReferenceException e) {
					LOGGER.info("Reached end of content in Custom SP Profile Responsibilities Section in Job Comparison Page");
					ExtentCucumberAdapter.addTestStepLog("Reached end of content in Custom SP Profile Responsibilities Section in Job Comparison Page");
					break;
				}	
			}
			String JCpageProfile1ResponsibilitiesText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1Responsibilities)).getText();
			LOGGER.info("Custom SP Profile Responsibilities : \n" + JCpageProfile1ResponsibilitiesText);
			ExtentCucumberAdapter.addTestStepLog("Custom SP Profile Responsibilities : \n" + JCpageProfile1ResponsibilitiesText);
	} catch (Exception e) {
			LOGGER.error(" Issue in verifying Custom SP Profile Responsibilities - Method: verify_custom_sp_profile_responsibilities", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog(" Issue in Verifying Custom SP Profile Responsibilities in the Job Compare page....Please Investigate!!!");
			Assert.fail("Issue in Verifying Custom SP Profile Responsibilities in the Job Compare page....Please Investigate!!!");
		}
		
	}
	
	public void verify_custom_sp_profile_behavioural_competencies() {
		try {
			js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1BehaviouralCompetencies);
			wait.until(ExpectedConditions.elementToBeClickable(JCpageProfile1ViewMoreCompetenciesBtn.get(0))).click();
			while(true) {
				try {
					if(JCpageProfile1ViewMoreCompetenciesBtn.isEmpty()) {
						LOGGER.info("Reached end of content in Custom SP Profile Behavioural Competencies Section in Job Comparison Page");
						ExtentCucumberAdapter.addTestStepLog("Reached end of content in Custom SP Profile Behavioural Competencies Section in Job Comparison Page");
						break;
					} 
					String ViewMoreCompetenciesBtnText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1ViewMoreCompetenciesBtn.get(0))).getText();
					JCpageProfile1ViewMoreCompetenciesBtn.get(0).click();
					js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1ViewMoreCompetenciesBtn.get(0));
					LOGGER.info("Clicked on "+ ViewMoreCompetenciesBtnText +" button in Custom SP Profile Behavioural Competencies Section in Job Comparison page");
					ExtentCucumberAdapter.addTestStepLog("Clicked on "+ ViewMoreCompetenciesBtnText +" button in Custom SP Profile Behavioural Competencies Section in Job Comparison page");
					PerformanceUtils.waitForUIStability(driver, 2);
				} catch(StaleElementReferenceException e) {
					LOGGER.info("Reached end of content in Custom SP Profile Behavioural Competencies Section in Job Comparison Page");
					ExtentCucumberAdapter.addTestStepLog("Reached end of content in Custom SP Profile Behavioural Competencies Section in Job Comparison Page");
					break;
				}	
			}
			String JCpageProfile1BehaviouralCompetenciesText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1BehaviouralCompetencies)).getText();
			LOGGER.info("Custom SP Profile Behavioural Competencies : \n" + JCpageProfile1BehaviouralCompetenciesText);
			ExtentCucumberAdapter.addTestStepLog("Custom SP Profile Behavioural Competencies : \n" + JCpageProfile1BehaviouralCompetenciesText);
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_custom_sp_profile_behavioural_competencies", e);
			LOGGER.error("Issue in Verifying Custom SP Profile Behavioural Competencies - Method: verify_custom_sp_profile_behavioural_competencies", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying Custom SP Profile Behavioural Competencies in the Job Compare page....Please Investigate!!!");
			Assert.fail("Issue in Verifying Custom SP Profile Behavioural Competencies in the Job Compare page....Please Investigate!!!");
		}
	}
	
	public void verify_custom_sp_profile_skills() {
		try {
			js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1Skills);
			wait.until(ExpectedConditions.elementToBeClickable(JCpageProfile1ViewMoreSkillsBtn.get(0))).click();
			while(true) {
				try {
					if(JCpageProfile1ViewMoreSkillsBtn.isEmpty()) {
						LOGGER.info("Reached end of content in Custom SP Profile Skills Section in Job Comparison Page");
						ExtentCucumberAdapter.addTestStepLog("Reached end of content in Custom SP Profile Skills Section in Job Comparison Page");
						break;
					} 
					String ViewMoreSkillsBtnText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1ViewMoreSkillsBtn.get(0))).getText();
					JCpageProfile1ViewMoreSkillsBtn.get(0).click();
					js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1ViewMoreSkillsBtn.get(0));
					LOGGER.info("Clicked on "+ ViewMoreSkillsBtnText +" button in Custom SP Profile Skills Section in Job Comparison page");
					ExtentCucumberAdapter.addTestStepLog("Clicked on "+ ViewMoreSkillsBtnText +" button in Custom SP Profile Skills Section in Job Comparison page");
					PerformanceUtils.waitForUIStability(driver, 2);
				} catch(StaleElementReferenceException e) {
					LOGGER.info("Reached end of content in Custom SP Profile Skills Section in Job Comparison Page");
					ExtentCucumberAdapter.addTestStepLog("Reached end of content in Custom SP Profile Skills Section in Job Comparison Page");
					break;
				}	
			}
			String JCpageProfile1SkillsText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1Skills)).getText();
			LOGGER.info("Custom SP Profile Skills : \n" + JCpageProfile1SkillsText);
			ExtentCucumberAdapter.addTestStepLog("Custom SP Profile Skills : \n" + JCpageProfile1SkillsText);
	} catch (Exception e) {
			LOGGER.error(" Issue in verifying Custom SP Profile Skills - Method: verify_custom_sp_profile_skills", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog(" Issue in Verifying Custom SP Profile Skills in the Job Compare page....Please Investigate!!!");
			Assert.fail("Issue in Verifying Custom SP Profile Skills in the Job Compare page....Please Investigate!!!");
		}
	}
	
	public void clear_text_in_search_bar_with_clear_button_in_the_search_bar() {
		try {
			js.executeScript("window.scrollTo(document.body.scrollHeight, 0)");
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(searchBarCancelBtninJCPage)).isDisplayed());
			utils.jsClick(driver, searchBarCancelBtninJCPage);
			LOGGER.info("Clicked on clear button and cleared text in the Search bar in Job Comparison page...");
			ExtentCucumberAdapter.addTestStepLog("Clicked on clear button and cleared text in the Search bar in Job Comparison page...");
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("clear_text_in_search_bar_with_clear_button_in_the_search_bar", e);
			LOGGER.error("Issue in clearing text with clear button in the Search bar - Method: clear_text_in_search_bar_with_clear_button_in_the_search_bar", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in clearing text with clear button in the Search bar in Job Comparison page...Please Investigate!!!");
			Assert.fail("Issue in clearing text with clear button in the Search bar in Job Comparison page...Please Investigate!!!");
		}	
	}
	
	public void user_should_verify_added_custom_sp_is_not_cleared_from_profiles_list_in_job_comparison_page() {
		wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
		try {
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(customSPCloseBtninJCPage)).isDisplayed());
			String JCpageProfile1TitleText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1Title)).getText();
			LOGGER.info("After clearing text in Search bar, Custom SP with Name : "+ JCpageProfile1TitleText + " is not cleared from Profiles List in Job Comparison page as Expected");
			ExtentCucumberAdapter.addTestStepLog("After clearing text in Search bar, Custom SP with Name : "+ JCpageProfile1TitleText + " is not cleared from Profiles List in Job Comparison page as Expected");
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_verify_added_custom_sp_is_not_cleared_from_profiles_list_in_job_comparison_page", e);
			LOGGER.error("Issue in Verifying added Custom SP in Profiles List after clearing text - Method: user_should_verify_added_custom_sp_is_not_cleared_from_profiles_list_in_job_comparison_page", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying added Custom SP in Profiles List after clearing text in Search bar in Job Comparison page...Please Investigate!!!");
			Assert.fail("Issue in Verifying added Custom SP in Profiles List after clearing text in Search bar in Job Comparison page...Please Investigate!!!");
		}	
	}
	
	public void select_third_custom_sp_from_search_results() {
		wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
		try {
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(thirdSearchResultBtninJCPage)).isDisplayed());
			wait.until(ExpectedConditions.elementToBeClickable(thirdSearchResultBtninJCPage)).click();
			customSPNameinSearchResults = thirdSearchResultTextinJCPage.getText();
			LOGGER.info("Third Custom SP with Name : "+ customSPNameinSearchResults + " from search results is selected in Job Comparison page");
			ExtentCucumberAdapter.addTestStepLog("Third Custom SP with Name : "+ customSPNameinSearchResults + " from search results is selected in Job Comparison page");
			wait.until(ExpectedConditions.invisibilityOf(thirdSearchResultBtninJCPage));
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("select_third_custom_sp_from_search_results", e);
			LOGGER.error("Issue in Selecting Third Custom SP from Search Results - Method: select_third_custom_sp_from_search_results", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in Selecting Third Custom SP from Search Results in Job Comparison page...Please Investigate!!!");
			Assert.fail("Issue in Selecting Third Custom SP from Search Results in Job Comparison page...Please Investigate!!!");
		}	
	}
	
	public void verify_new_custom_sp_replaces_existing_custom_sp_in_profiles_list_in_job_comparison_page() {
		wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
		try {
			js.executeScript("arguments[0].scrollIntoView(true);", customSPCloseBtninJCPage);
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(customSPCloseBtninJCPage)).isDisplayed());
			String JCpageProfile1TitleText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1Title)).getText();
			Assert.assertEquals(JCpageProfile1TitleText, customSPNameinSearchResults);
			LOGGER.info("New Custom SP with Name : "+ JCpageProfile1TitleText + " successfully replaced with existing Custom SP in Profiles List in Job Comparison page");
			ExtentCucumberAdapter.addTestStepLog("New Custom SP with Name : "+ JCpageProfile1TitleText + " successfully replaced with existing Custom SP in Profiles List in Job Comparison page");
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_new_custom_sp_replaces_existing_custom_sp_in_profiles_list_in_job_comparison_page", e);
			LOGGER.error(" Issue in replacing New Custom SP with existing Custom SP - Method: verify_new_custom_sp_replaces_existing_custom_sp_in_profiles_list_in_job_comparison_page", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog(" Issue in replacing New Custom SP with existing Custom SP in Profiles List in Job Comparison page...Please Investigate!!!");
			Assert.fail("Issue in replacing New Custom SP with existing Custom SP in Profiles List in Job Comparison page...Please Investigate!!!");
		}	
	}
	
	public void clear_custom_sp_and_text_in_search_bar_with_close_button_on_the_profile() {
		wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
		try {
			js.executeScript("arguments[0].scrollIntoView(true);", customSPCloseBtninJCPage);
			utils.jsClick(driver, customSPCloseBtninJCPage);
			LOGGER.info("Clicked on close button on the Custom SP Profile....");
			ExtentCucumberAdapter.addTestStepLog("Clicked on close button on the Custom SP Profile....");
			wait.until(ExpectedConditions.attributeToBe(searchBarinJCPage, "value", ""));
			Assert.assertEquals(searchBarinJCPage.getAttribute("value"), "");
			LOGGER.info("Added Custom SP and text in Search bar are cleared successfully with close button on the profile...");
			ExtentCucumberAdapter.addTestStepLog("Added Custom SP and text in Search bar are cleared successfully with close button on the profile...");
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("clear_custom_sp_and_text_in_search_bar_with_close_button_on_the_profile", e);
			LOGGER.error(" Issue in clearing Custom SP and text in search bar - Method: clear_custom_sp_and_text_in_search_bar_with_close_button_on_the_profile", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog(" Issue in clearing Added Custom SP and Search bar text together with close button on the Profile in Job Comparison page...Please Investigate!!!");
			Assert.fail("Issue in clearing Added Custom SP and Search bar text together with close button on the Profile in Job Comparison page...Please Investigate!!!");
		}	
	}
}

