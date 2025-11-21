package com.kfonetalentsuite.pageobjects.JobMapping;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.openqa.selenium.By;
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

public class PO15_ValidateRecommendedProfileDetails {
	WebDriver driver = DriverManager.getDriver();	

	protected static final Logger LOGGER = (Logger) LogManager.getLogger();
	PO15_ValidateRecommendedProfileDetails validateRecommendedProfileDetails;
	

	public PO15_ValidateRecommendedProfileDetails() throws IOException {
		// super();
		PageFactory.initElements(driver, this);
	}

	WebDriverWait wait = DriverManager.getWait();
	Utilities utils = new Utilities();
	JavascriptExecutor js = (JavascriptExecutor) driver;
	
	// THREAD-SAFE: Each thread gets its own isolated state for parallel execution
	public static ThreadLocal<Integer> rowNumber = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<String> orgJobName = ThreadLocal.withInitial(() -> null);
	public static ThreadLocal<String> orgJobCode = ThreadLocal.withInitial(() -> null);
	public static ThreadLocal<String> orgJobGrade = ThreadLocal.withInitial(() -> null);
	public static ThreadLocal<String> orgJobFunction = ThreadLocal.withInitial(() -> null);
	public static ThreadLocal<String> orgJobDepartment = ThreadLocal.withInitial(() -> null);
	public static ThreadLocal<String> matchedSuccessPrflName = ThreadLocal.withInitial(() -> null);
	
	//XPATHs
	@FindBy(xpath = "//div[@data-testid='loader']//img")
	WebElement pageLoadSpinner2;
	
	@FindBy(xpath = "//h1[@id='compare-desc']")
	public WebElement CompareandSelectheader;
	
	@FindBy(xpath = "//div[contains(@class,'leading')]//div[1]//div[1]")
	WebElement JCpageOrgJobTitleHeader;
	
	@FindBy(xpath = "//div[contains(@class,'leading')]//div[contains(text(),'Grade')]//span")
	WebElement JCpageOrgJobGradeValue;
	
	@FindBy(xpath = "//div[contains(@class,'leading')]//div[contains(text(),'Department')]//span")
	WebElement JCpageOrgJobDepartmentValue;
	
	@FindBy(xpath = "//div[contains(@class,'leading')]//div[contains(text(),'Function')]//span")
	WebElement JCpageOrgJobFunctionValue;
	
	@FindBy(xpath = "//div[@class='shadow']//div[contains(@id,'card-title')]")
	WebElement JCpageProfile1Title;
	
	@FindBy(xpath = "//div[@class='shadow']//div[contains(@id,'card-header')][1]//span")
	WebElement JCpageProfile1SelectBtn;
	
	@FindBy(xpath = "//div[@class='shadow']//div[contains(@id,'recommended-title')]")
	WebElement JCpageProfile1RecommendedTag;
	
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
	
	
	//METHODS
	public void search_for_job_profile_with_view_other_matches_button() {
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tbody")));
			PerformanceUtils.waitForPageReady(driver, 2);
			
			for(int i = 2; i<=47; i=i+3) {
				try {
					WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//tbody//tr[" + Integer.toString(i)+"]//button[not(contains(@id,'publish'))]")));
					
					js.executeScript("arguments[0].scrollIntoView(true);", button);
					PerformanceUtils.waitForUIStability(driver, 1);
					
					wait.until(ExpectedConditions.visibilityOf(button));
					String text = button.getText();
					
					if(text.contains("Other Matches")) {
						rowNumber.set(i);
						WebElement	jobName = driver.findElement(By.xpath("//tbody//tr["+Integer.toString(rowNumber.get()-1)+"]//td[2]//div[contains(text(),'(')]"));
						Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(jobName)).isDisplayed());
					String jobname1 = wait.until(ExpectedConditions.visibilityOf(jobName)).getText(); 
					orgJobName.set(jobname1.split("-", 2)[0].trim());
				PageObjectHelper.log(LOGGER, "View Other Matches button found for Job Profile: " + orgJobName.get());
					break;
					} else {
						rowNumber.set(i);
					}
				} catch (Exception rowException) {
					LOGGER.debug("Row " + i + " doesn't have the expected button, continuing search...");
					continue;
				}
			}
	} catch(Exception e) {
		PageObjectHelper.handleError(LOGGER, "search_for_job_profile_with_view_other_matches_button",
			"Issue searching Job Profile with View Other Matches button", e);
	}
	}
	
	public void user_should_verify_organization_job_name_and_job_code_values_of_job_profile_with_view_other_matches_button() {
		try {
			// OPTIMIZED: Single comprehensive wait
			PerformanceUtils.waitForPageReady(driver, 2);
			WebElement	jobName = driver.findElement(By.xpath("//tbody//tr["+Integer.toString(rowNumber.get()-1)+"]//td[2]//div[contains(text(),'(')]"));
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(jobName)).isDisplayed());
			String jobname1 = wait.until(ExpectedConditions.visibilityOf(jobName)).getText(); 
		orgJobName.set(jobname1.split("-", 2)[0].trim());
		orgJobCode.set(jobname1.split("-", 2)[1].trim().substring(1, jobname1.split("-", 2)[1].length()-2));
	PageObjectHelper.log(LOGGER, "Job name: " + orgJobName.get());
	PageObjectHelper.log(LOGGER, "Job code: " + orgJobCode.get());
} catch (Exception e) {
	PageObjectHelper.handleError(LOGGER, "user_should_verify_organization_job_name_and_job_code_values_of_job_profile_with_view_other_matches_button",
		"Issue verifying job name of Profile with View Other Matches button", e);
}
	}
	
	public void user_should_verify_organization_job_grade_and_department_values_of_job_profile_with_view_other_matches_button() {
		try {
			WebElement	jobGrade = driver.findElement(By.xpath("//tbody//tr["+Integer.toString(rowNumber.get()-1)+"]//td[3]//div[1]"));
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(jobGrade)).isDisplayed());
			String jobGradeText = wait.until(ExpectedConditions.visibilityOf(jobGrade)).getText(); 
			if(jobGradeText.contentEquals("-") || jobGradeText.isEmpty() || jobGradeText.isBlank()) {
				jobGradeText = "NULL";
				orgJobGrade.set(jobGradeText);
		}
	orgJobGrade.set(jobGradeText);
	PageObjectHelper.log(LOGGER, "Grade value: " + orgJobGrade.get());
} catch (Exception e) {
	PageObjectHelper.handleError(LOGGER, "user_should_verify_organization_job_grade_value",
		"Issue verifying Organization Job Grade value", e);
}
		
		try {
			WebElement	jobDepartment = driver.findElement(By.xpath("//tbody//tr["+Integer.toString(rowNumber.get()-1)+"]//td[4]//div[1]"));
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(jobDepartment)).isDisplayed());
			String jobDepartmentText = wait.until(ExpectedConditions.visibilityOf(jobDepartment)).getText(); 
			if(jobDepartmentText.contentEquals("-") || jobDepartmentText.isEmpty() || jobDepartmentText.isBlank()) {
				jobDepartmentText = "NULL";
				orgJobDepartment.set(jobDepartmentText);
		}
	orgJobDepartment.set(jobDepartmentText);
	PageObjectHelper.log(LOGGER, "Department value: " + orgJobDepartment.get());
} catch (Exception e) {
	PageObjectHelper.handleError(LOGGER, "user_should_verify_organization_job_department_value",
		"Issue verifying Organization Job Department value", e);
}
	}
	
	public void user_should_verify_organization_job_function_or_sub_function_of_job_profile_with_view_other_matches_button() {
		try {
			WebElement	jobFunction = driver.findElement(By.xpath("//tbody//tr["+Integer.toString(rowNumber.get())+"]//div//span[2]"));
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(jobFunction)).isDisplayed());
			String jobFunctionText = wait.until(ExpectedConditions.visibilityOf(jobFunction)).getText(); 
			if(jobFunctionText.contentEquals("-") || jobFunctionText.isEmpty() || jobFunctionText.isBlank()) {
				jobFunctionText = "NULL | NULL";
				orgJobFunction.set(jobFunctionText);
			} else if (jobFunctionText.endsWith("-") || jobFunctionText.endsWith("| -") || jobFunctionText.endsWith("|") || (!(jobFunctionText.contains("|")) && (jobFunctionText.length() > 1))) {
				jobFunctionText = jobFunctionText + " | NULL";
				orgJobFunction.set(jobFunctionText);
			}
		
	orgJobFunction.set(jobFunctionText);
	PageObjectHelper.log(LOGGER, "Function / Sub-function values: " + orgJobFunction.get());
} catch (Exception e) {
	PageObjectHelper.handleError(LOGGER, "user_should_verify_organization_job_function_or_sub_function_of_job_profile_with_view_other_matches_button",
		"Issue verifying Organization Job Function / Sub-function values", e);
}
	}
	
	/**
	 * Clicks on matched profile of job profile with view other matches button
	 * ENHANCED FOR HEADLESS MODE: Scrolls element into view before clicking
	 */
	public void click_on_matched_profile_of_job_profile_with_view_other_matches_button() {
		try {
			// PARALLEL EXECUTION FIX: Wait for page to be ready first
			PerformanceUtils.waitForPageReady(driver, 5);
			
			// Validate rowNumber is set
			int currentRow = rowNumber.get();
			if (currentRow == 0) {
				throw new Exception("rowNumber is not set. Please ensure 'Search for Job Profile with View Other Matches button' step executed successfully.");
			}
			
			int matchedProfileRow = currentRow - 1;
			LOGGER.debug("Attempting to click matched profile in row {} (button found in row {})", matchedProfileRow, currentRow);
			
			// Build XPath for matched profile
			String matchedProfileXPath = "//div[@id='kf-job-container']//div//table//tbody//tr[" + matchedProfileRow + "]//td[1]//div";
			
			// Wait for matched profile element to be present
			WebElement linkedMatchedProfile = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(matchedProfileXPath)));
			
			// Wait for element to be clickable and get text
			WebElement clickableElement = wait.until(ExpectedConditions.elementToBeClickable(linkedMatchedProfile));
			String MatchedProfileNameText = clickableElement.getText();
			
			if (MatchedProfileNameText == null || MatchedProfileNameText.trim().isEmpty()) {
				throw new Exception("Matched Profile name is empty at row " + matchedProfileRow);
			}
			
			matchedSuccessPrflName.set(MatchedProfileNameText);
			LOGGER.debug("Found matched profile: '{}' at row {}", MatchedProfileNameText, matchedProfileRow);
			
			// PARALLEL EXECUTION FIX: Use 'auto' scroll for instant positioning
			js.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});", linkedMatchedProfile);
			
			// PARALLEL EXECUTION FIX: Wait for element to be stable
			PerformanceUtils.waitForElement(driver, linkedMatchedProfile, 3);
			
			// Attempt to click with fallback strategies
			boolean clickSuccessful = false;
			try {
				clickableElement.click();
				clickSuccessful = true;
				LOGGER.debug("Successfully clicked using WebElement.click()");
			} catch (Exception e) {
				LOGGER.debug("WebElement.click() failed, trying JavaScript click: {}", e.getMessage());
				try {
					js.executeScript("arguments[0].click();", linkedMatchedProfile);
					clickSuccessful = true;
					LOGGER.debug("Successfully clicked using JavaScript");
				} catch (Exception s) {
					LOGGER.debug("JavaScript click failed, trying utils.jsClick(): {}", s.getMessage());
					utils.jsClick(driver, linkedMatchedProfile);
					clickSuccessful = true;
					LOGGER.debug("Successfully clicked using utils.jsClick()");
				}
			}
			
			if (clickSuccessful) {
				PageObjectHelper.log(LOGGER, "Clicked on Matched Profile: " + MatchedProfileNameText + " of Organization Job: " + orgJobName.get());
				// PARALLEL EXECUTION FIX: Use consolidated wait
				PerformanceUtils.waitForPageReady(driver, 10);
			} else {
				throw new Exception("All click strategies failed for Matched Profile: " + MatchedProfileNameText);
			}
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_matched_profile_of_job_profile_with_view_other_matches_button",
				"Issue clicking Matched Profile linked with job name " + orgJobName.get() + " (rowNumber: " + rowNumber.get() + ")", e);
		}
	}
	public void verify_user_navigated_to_job_comparison_page() {
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
	String compareAndSelectHeaderText = wait.until(ExpectedConditions.visibilityOf(CompareandSelectheader)).getText();
	Assert.assertEquals(compareAndSelectHeaderText, "Which profile do you want to use for this job?");
	PageObjectHelper.log(LOGGER, "User navigated to Job Compare page successfully");
} catch (Exception e) {
	PageObjectHelper.handleError(LOGGER, "verify_user_navigated_to_job_comparison_page",
		"Issue navigating to Job Compare page", e);
}
	}
	
public void user_is_in_job_comparison_page() {
	PageObjectHelper.log(LOGGER, "User is in Job Comparison Page");
}
	
	public void validate_organization_job_name_and_code_in_job_comparison_page() {
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
	String JCpageOrgJobTitleHeaderText = wait.until(ExpectedConditions.visibilityOf(JCpageOrgJobTitleHeader)).getText();
	Assert.assertTrue(JCpageOrgJobTitleHeaderText.contains(PO15_ValidateRecommendedProfileDetails.orgJobName.get()));
	Assert.assertTrue(JCpageOrgJobTitleHeaderText.contains(PO15_ValidateRecommendedProfileDetails.orgJobCode.get()));
	PageObjectHelper.log(LOGGER, "Organization Job Name and Job code validated successfully");
} catch (Exception e) {
	PageObjectHelper.handleError(LOGGER, "validate_organization_job_name_and_code_in_job_comparison_page",
		"Issue validating Organization Job Name and Job Code", e);
}
	}
	
	public void user_should_validate_organization_job_grade_department_and_function_or_subfunction_in_job_comparison_page() {
		try {
			String JCpageOrgJobGradeValueText = wait.until(ExpectedConditions.visibilityOf(JCpageOrgJobGradeValue)).getText();
			if(JCpageOrgJobGradeValueText.contentEquals("-") || JCpageOrgJobGradeValueText.isEmpty() || JCpageOrgJobGradeValueText.isBlank()) {
				JCpageOrgJobGradeValueText = "NULL";
				Assert.assertTrue(JCpageOrgJobGradeValueText.contains(PO15_ValidateRecommendedProfileDetails.orgJobGrade.get()));
			} else {
		Assert.assertTrue(JCpageOrgJobGradeValueText.contains(PO15_ValidateRecommendedProfileDetails.orgJobGrade.get()));
	}
	PageObjectHelper.log(LOGGER, "Organization Job Grade value validated successfully");
} catch (Exception e) {
	PageObjectHelper.handleError(LOGGER, "user_should_validate_organization_job_grade",
		"Issue validating Organization Job Grade value", e);
}
		
		try {
			String JCpageOrgJobDepartmentValueText = wait.until(ExpectedConditions.visibilityOf(JCpageOrgJobDepartmentValue)).getText();
			if(JCpageOrgJobDepartmentValueText.contentEquals("-") || JCpageOrgJobDepartmentValueText.isEmpty() || JCpageOrgJobDepartmentValueText.isBlank()) {
				JCpageOrgJobDepartmentValueText = "NULL";
				Assert.assertTrue(JCpageOrgJobDepartmentValueText.contains(PO15_ValidateRecommendedProfileDetails.orgJobDepartment.get()));
			} else {
		Assert.assertTrue(JCpageOrgJobDepartmentValueText.contains(PO15_ValidateRecommendedProfileDetails.orgJobDepartment.get()));
	}
	PageObjectHelper.log(LOGGER, "Organization Job Department value validated successfully");
} catch (Exception e) {
	PageObjectHelper.handleError(LOGGER, "user_should_validate_organization_job_department",
		"Issue validating Organization Job Department value", e);
}
		
		try {
			String JCpageOrgJobFunctionValueText = wait.until(ExpectedConditions.visibilityOf(JCpageOrgJobFunctionValue)).getText();
			if(JCpageOrgJobFunctionValueText.contentEquals("-") || JCpageOrgJobFunctionValueText.isEmpty() || JCpageOrgJobFunctionValueText.isBlank()) {
				JCpageOrgJobFunctionValueText = "NULL | NULL";
				Assert.assertTrue(JCpageOrgJobFunctionValueText.contains(PO15_ValidateRecommendedProfileDetails.orgJobFunction.get()));
			} else if (JCpageOrgJobFunctionValueText.endsWith("-") || JCpageOrgJobFunctionValueText.endsWith("| -") || JCpageOrgJobFunctionValueText.endsWith("|") || (!(JCpageOrgJobFunctionValueText.contains("|")) && (JCpageOrgJobFunctionValueText.length() > 1))) {
				JCpageOrgJobFunctionValueText = JCpageOrgJobFunctionValueText + " | NULL";
			} else {
		Assert.assertTrue(JCpageOrgJobFunctionValueText.contains(PO15_ValidateRecommendedProfileDetails.orgJobFunction.get()));
	}
	PageObjectHelper.log(LOGGER, "Organization Job Function / Sub-function values validated successfully");
} catch (Exception e) {
	PageObjectHelper.handleError(LOGGER, "user_should_validate_organization_job_function_or_subfunction",
		"Issue validating Organization Job Function / Sub-function values", e);
}
	}
	
	public void user_should_verify_recommended_profile_name_matches_with_matched_success_profile_name() {
		try {
	String JCpageProfile1TitleText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1Title)).getText();
	Assert.assertTrue(JCpageProfile1TitleText.contains(PO15_ValidateRecommendedProfileDetails.matchedSuccessPrflName.get()));
	PageObjectHelper.log(LOGGER, "Recommended Profile Name matches with Matched Success Profile Name");
} catch (Exception e) {
	PageObjectHelper.handleError(LOGGER, "user_should_verify_recommended_profile_name_matches_with_matched_success_profile_name",
		"Issue verifying Recommended Profile Name", e);
}
	}
	
	public void user_should_verify_recommended_tag_and_select_button_is_displaying_on_the_profile() {
		try {
	Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(JCpageProfile1RecommendedTag)).isDisplayed());
	PageObjectHelper.log(LOGGER, "Recommended tag is displaying on Recommended Profile");
	Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(JCpageProfile1SelectBtn)).isDisplayed());
	Assert.assertEquals("true",wait.until(ExpectedConditions.visibilityOf(JCpageProfile1SelectBtn)).getAttribute("aria-checked").toString());
	PageObjectHelper.log(LOGGER, "Select button is displaying and in Selected status by default");
} catch (Exception e) {
	PageObjectHelper.handleError(LOGGER, "user_should_verify_recommended_tag_and_select_button_is_displaying_on_the_profile",
		"Issue verifying Recommended tag and Select button", e);
}
	}
	
	public void validate_recommended_profile_grade_matches_with_matched_success_profile_grade() {
		try {
	String JCpageProfile1GradeText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1Grade)).getText();
	Assert.assertTrue(PO05_ValidateJobProfileDetailsPopup.ProfileDetails.contains("Grade:"+JCpageProfile1GradeText));
	PageObjectHelper.log(LOGGER, "Recommended Profile Grade matches with Matched Success Profile Grade");
} catch (Exception e) {
	PageObjectHelper.handleError(LOGGER, "validate_recommended_profile_grade_matches_with_matched_success_profile_grade",
		"Issue validating Recommended Profile Grade", e);
}
	}
	
	public void validate_recommended_profile_level_sublevels_matches_with_matched_success_profile_level_sublevels() {
		try {
	String JCpageProfile1LevelText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1Level)).getText();
	Assert.assertTrue(PO05_ValidateJobProfileDetailsPopup.ProfileDetails.contains("Level / Sublevel:"+JCpageProfile1LevelText));
	PageObjectHelper.log(LOGGER, "Recommended Profile Level / Sublevels matches with Matched Success Profile");
} catch (Exception e) {
	PageObjectHelper.handleError(LOGGER, "validate_recommended_profile_level_sublevels_matches_with_matched_success_profile_level_sublevels",
		"Issue validating Recommended Profile Level / Sublevels", e);
}
	}
	
	public void validate_recommended_profile_function_subfunction_matches_with_matched_success_profile_function_subfunction() {
		try {
	js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1Function);
	String JCpageProfile1FunctionText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1Function)).getText();
	Assert.assertTrue(PO05_ValidateJobProfileDetailsPopup.ProfileDetails.contains("Function / Sub-function:"+JCpageProfile1FunctionText));
	PageObjectHelper.log(LOGGER, "Recommended Profile Function / Sub-function matches with Matched Success Profile");
} catch (Exception e) {
	PageObjectHelper.handleError(LOGGER, "validate_recommended_profile_function_subfunction_matches_with_matched_success_profile_function_subfunction",
		"Issue validating Recommended Profile Function / Sub-function", e);
}
	}
	
	public void validate_recommended_profile_seniority_level_matches_with_matched_success_profile_seniority_level() {
		try {
	js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1SeniorityLevel);
	String JCpageProfile1SeniorityLevelText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1SeniorityLevel)).getText();
	Assert.assertTrue(PO05_ValidateJobProfileDetailsPopup.ProfileDetails.contains("Seniority level:"+JCpageProfile1SeniorityLevelText));
	PageObjectHelper.log(LOGGER, "Recommended Profile Seniority level matches with Matched Success Profile");
} catch (Exception e) {
	PageObjectHelper.handleError(LOGGER, "validate_recommended_profile_seniority_level_matches_with_matched_success_profile_seniority_level",
		"Issue validating Recommended Profile Seniority level", e);
}
	}
	
	public void validate_recommended_profile_managerial_experience_matches_with_matched_success_profile_managerial_experience() {
		try {
	js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1ManagerialExperience);
	String JCpageProfile1ManagerialExperienceText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1ManagerialExperience)).getText();
	Assert.assertTrue(PO05_ValidateJobProfileDetailsPopup.ProfileDetails.contains("Managerial experience:"+JCpageProfile1ManagerialExperienceText));
	PageObjectHelper.log(LOGGER, "Recommended Profile Managerial experience matches with Matched Success Profile");
} catch (Exception e) {
	PageObjectHelper.handleError(LOGGER, "validate_recommended_profile_managerial_experience_matches_with_matched_success_profile_managerial_experience",
		"Issue validating Recommended Profile Managerial experience", e);
}
	}
	
public void validate_recommended_profile_education_matches_with_matched_success_profile_education() {
	try {
		js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1Education);
		String JCpageProfile1EducationText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1Education)).getText();
		Assert.assertTrue(PO05_ValidateJobProfileDetailsPopup.ProfileDetails.contains("Education:"+JCpageProfile1EducationText));
		PageObjectHelper.log(LOGGER, "Recommended Profile Education matches with Matched Success Profile");
	} catch (Exception e) {
		PageObjectHelper.handleError(LOGGER, "validate_recommended_profile_education_matches_with_matched_success_profile_education",
			"Issue validating Recommended Profile Education", e);
	}
}
	
public void validate_recommended_profile_general_experience_matches_with_matched_success_profile_general_experience() {
	try {
		js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1GeneralExperience);
		String JCpageProfile1GeneralExperienceText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1GeneralExperience)).getText();
		Assert.assertTrue(PO05_ValidateJobProfileDetailsPopup.ProfileDetails.contains("General experience:"+JCpageProfile1GeneralExperienceText));
		PageObjectHelper.log(LOGGER, "Recommended Profile General experience matches with Matched Success Profile");
	} catch (Exception e) {
		PageObjectHelper.handleError(LOGGER, "validate_recommended_profile_general_experience_matches_with_matched_success_profile_general_experience",
			"Issue validating Recommended Profile General experience", e);
	}
}
	
	public void validate_recommended_profile_role_summary_matches_with_matched_success_profile_role_summary() {
		try {
	js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1RoleSummary);
	String JCpageProfile1RoleSummaryText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1RoleSummary)).getText();
	Assert.assertEquals(PO05_ValidateJobProfileDetailsPopup.ProfileRoleSummary, JCpageProfile1RoleSummaryText);
	PageObjectHelper.log(LOGGER, "Recommended Profile Role Summary matches with Matched Success Profile");
} catch (Exception e) {
	PageObjectHelper.handleError(LOGGER, "validate_recommended_profile_role_summary_matches_with_matched_success_profile_role_summary",
		"Issue validating Recommended Profile Role Summary", e);
}
	}
	
	public void validate_recommended_profile_responsibilities_matches_with_matched_success_profile_responsibilities() {
		try {
	js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1Responsibilities);
	PerformanceUtils.waitForUIStability(driver, 2);
	wait.until(ExpectedConditions.elementToBeClickable(JCpageProfile1ViewMoreResponsibilitiesBtn.get(0))).click();
	while(true) {
		try {
			if(JCpageProfile1ViewMoreResponsibilitiesBtn.isEmpty()) {
				js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
				break;
			}
			js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1ViewMoreResponsibilitiesBtn.get(0));
			PerformanceUtils.waitForUIStability(driver, 2);
			try {
				wait.until(ExpectedConditions.elementToBeClickable(JCpageProfile1ViewMoreResponsibilitiesBtn.get(0))).click();
			} catch(Exception e) {
				js.executeScript("arguments[0].click();", JCpageProfile1ViewMoreResponsibilitiesBtn.get(0));
			}
			PerformanceUtils.waitForUIStability(driver, 2);
			js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1ViewMoreResponsibilitiesBtn.get(0));
		} catch(StaleElementReferenceException e) {
			js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
			break;
		}	
	}
	String JCpageProfile1ResponsibilitiesText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1Responsibilities)).getText();
	Assert.assertEquals(PO05_ValidateJobProfileDetailsPopup.ProfileResponsibilities, JCpageProfile1ResponsibilitiesText);
	PageObjectHelper.log(LOGGER, "Recommended Profile Responsibilities matches with Matched Success Profile");
} catch (Exception e) {
	PageObjectHelper.handleError(LOGGER, "validate_recommended_profile_responsibilities_matches_with_matched_success_profile_responsibilities",
		"Issue validating Recommended Profile Responsibilities", e);
}
	}
	
	public void validate_recommended_profile_behavioural_competencies_matches_with_matched_success_profile_behavioural_competencies() {
		try {
	js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1BehaviouralCompetencies);
	PerformanceUtils.waitForUIStability(driver, 2);
	wait.until(ExpectedConditions.elementToBeClickable(JCpageProfile1ViewMoreCompetenciesBtn.get(0))).click();
	while(true) {
		try {
			if(JCpageProfile1ViewMoreCompetenciesBtn.isEmpty()) {
				break;
			}
			js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1ViewMoreCompetenciesBtn.get(0));
			PerformanceUtils.waitForUIStability(driver, 2);
			try {
				wait.until(ExpectedConditions.elementToBeClickable(JCpageProfile1ViewMoreCompetenciesBtn.get(0))).click();
			} catch(Exception e) {
				js.executeScript("arguments[0].click();", JCpageProfile1ViewMoreCompetenciesBtn.get(0));
			}
			PerformanceUtils.waitForUIStability(driver, 2);
			js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1ViewMoreCompetenciesBtn.get(0));
		} catch(StaleElementReferenceException e) {
			break;
		}	
	}
	String JCpageProfile1BehaviouralCompetenciesText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1BehaviouralCompetencies)).getText();
	Assert.assertEquals(PO05_ValidateJobProfileDetailsPopup.ProfileBehaviouralCompetencies, JCpageProfile1BehaviouralCompetenciesText);
	PageObjectHelper.log(LOGGER, "Recommended Profile Behavioural Competencies matches with Matched Success Profile");
} catch (Exception e) {
	PageObjectHelper.handleError(LOGGER, "validate_recommended_profile_behavioural_competencies_matches_with_matched_success_profile_behavioural_competencies",
		"Issue validating Recommended Profile Behavioural Competencies", e);
}
	}
 public void validate_recommended_profile_skills_matches_with_matched_success_profile_skills() {
	 try {
			js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1Skills);
			PerformanceUtils.waitForUIStability(driver, 2);
			wait.until(ExpectedConditions.elementToBeClickable(JCpageProfile1ViewMoreSkillsBtn.get(0))).click();
			while(true) {
				try {
					if(JCpageProfile1ViewMoreSkillsBtn.isEmpty()) {
						break;
					} 
					js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1ViewMoreSkillsBtn.get(0));
					PerformanceUtils.waitForUIStability(driver, 2);
					js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1ViewMoreSkillsBtn.get(0));
					try {
						wait.until(ExpectedConditions.elementToBeClickable(JCpageProfile1ViewMoreSkillsBtn.get(0))).click();
					} catch(Exception e) {
						js.executeScript("arguments[0].click();", JCpageProfile1ViewMoreCompetenciesBtn.get(0));
					}
					PerformanceUtils.waitForUIStability(driver, 2);
					js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1ViewMoreSkillsBtn.get(0));
				} catch(StaleElementReferenceException e) {
					break;
				}	
			}
			String JCpageProfile1SkillsText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1Skills)).getText();
			Assert.assertEquals(PO05_ValidateJobProfileDetailsPopup.ProfileSkills, JCpageProfile1SkillsText);
			PageObjectHelper.log(LOGGER, "Recommended Profile Skills matches with Matched Success Profile");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "validate_recommended_profile_skills_matches_with_matched_success_profile_skills",
				"Issue validating Recommended Profile Skills", e);
		}
 }
}

