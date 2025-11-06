package com.JobMapping.pageobjects;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.openqa.selenium.By;
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

import com.JobMapping.utils.PerformanceUtils;
import com.JobMapping.utils.ScreenshotHandler;
import com.JobMapping.utils.Utilities;
import com.JobMapping.webdriverManager.DriverManager;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

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
	
	public static int rowNumber=0; 
	public static String orgJobName;
	public static String orgJobCode;
	public static String orgJobGrade;
	public static String orgJobFunction;
	public static String orgJobDepartment;
	public static String matchedSuccessPrflName;
	
	//XPATHs
	@FindBy(xpath = "//div[@data-testid='loader']//img")
	@CacheLookup
	WebElement pageLoadSpinner2;
	
	@FindBy(xpath = "//h1[@id='compare-desc']")
	@CacheLookup
	public WebElement CompareandSelectheader;
	
	@FindBy(xpath = "//div[contains(@class,'leading')]//div[1]//div[1]")
	@CacheLookup
	WebElement JCpageOrgJobTitleHeader;
	
	@FindBy(xpath = "//div[contains(@class,'leading')]//div[contains(text(),'Grade')]//span")
	@CacheLookup
	WebElement JCpageOrgJobGradeValue;
	
	@FindBy(xpath = "//div[contains(@class,'leading')]//div[contains(text(),'Department')]//span")
	@CacheLookup
	WebElement JCpageOrgJobDepartmentValue;
	
	@FindBy(xpath = "//div[contains(@class,'leading')]//div[contains(text(),'Function')]//span")
	@CacheLookup
	WebElement JCpageOrgJobFunctionValue;
	
	@FindBy(xpath = "//div[@class='shadow']//div[contains(@id,'card-title')]")
	@CacheLookup
	WebElement JCpageProfile1Title;
	
	@FindBy(xpath = "//div[@class='shadow']//div[contains(@id,'card-header')][1]//span")
	@CacheLookup
	WebElement JCpageProfile1SelectBtn;
	
	@FindBy(xpath = "//div[@class='shadow']//div[contains(@id,'recommended-title')]")
	@CacheLookup
	WebElement JCpageProfile1RecommendedTag;
	
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
						rowNumber = i;
						WebElement	jobName = driver.findElement(By.xpath("//tbody//tr["+Integer.toString(rowNumber-1)+"]//td[2]//div[contains(text(),'(')]"));
						Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(jobName)).isDisplayed());
						String jobname1 = wait.until(ExpectedConditions.visibilityOf(jobName)).getText(); 
						orgJobName = jobname1.split("-", 2)[0].trim();
						LOGGER.info("View Other Matches button is found for Job Profile with Name : " + orgJobName);
						ExtentCucumberAdapter.addTestStepLog("View Other Matches button is found for Job Profile with Name : " + orgJobName);;
						break;
					} else {
						rowNumber = i;
					}
				} catch (Exception rowException) {
					LOGGER.debug("Row " + i + " doesn't have the expected button, continuing search...");
					continue;
				}
			}
		} catch(Exception e) {
			ScreenshotHandler.captureFailureScreenshot("search_for_job_profile_with_view_other_matches_button", e);
			LOGGER.error("Issue in searching a Job Profile with View Other Matches button - Method: search_for_job_profile_with_view_other_matches_button", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in searching a Job Profile with View Other Matches button in Job Mapping UI....Please Investigate!!!!");
			Assert.fail("Issue in searching a Job Profile with View Other Matches button in Job Mapping UI....Please Investigate!!!!");
		}
	}
	
	public void user_should_verify_organization_job_name_and_job_code_values_of_job_profile_with_view_other_matches_button() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForPageReady(driver, 2);
			WebElement	jobName = driver.findElement(By.xpath("//tbody//tr["+Integer.toString(rowNumber-1)+"]//td[2]//div[contains(text(),'(')]"));
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(jobName)).isDisplayed());
			String jobname1 = wait.until(ExpectedConditions.visibilityOf(jobName)).getText(); 
			orgJobName = jobname1.split("-", 2)[0].trim();
			orgJobCode = jobname1.split("-", 2)[1].trim().substring(1, jobname1.split("-", 2)[1].length()-2);
			ExtentCucumberAdapter.addTestStepLog("Job name of Profile with View Other Matches button in the organization table : " + orgJobName);
			LOGGER.info("Job name of Profile with View Other Matches button in the organization table : " + orgJobName);
		ExtentCucumberAdapter.addTestStepLog("Job code of Profile with View Other Matches button in the organization table : " + orgJobCode);
		LOGGER.info("Job code of Profile with View Other Matches button in the organization table : " + orgJobCode);
	} catch (Exception e) {
		ScreenshotHandler.captureFailureScreenshot("user_should_verify_organization_job_name_and_job_code_values_of_job_profile_with_view_other_matches_button", e);
		LOGGER.error("Issue in verifying job name of Profile with View Other Matches button - Method: user_should_verify_organization_job_name_and_job_code_values_of_job_profile_with_view_other_matches_button", e);
		e.printStackTrace();
		ExtentCucumberAdapter.addTestStepLog("Issue in verifying job name of Profile with View Other Matches button in the Organization jobs profile list...Please Investigate!!!");
		Assert.fail("Issue in verifying job name of Profile with View Other Matches button in the Organization jobs profile list...Please Investigate!!!");
	}
	}
	
	public void user_should_verify_organization_job_grade_and_department_values_of_job_profile_with_view_other_matches_button() {
		try {
			WebElement	jobGrade = driver.findElement(By.xpath("//tbody//tr["+Integer.toString(rowNumber-1)+"]//td[3]//div[1]"));
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(jobGrade)).isDisplayed());
			String jobGradeText = wait.until(ExpectedConditions.visibilityOf(jobGrade)).getText(); 
			if(jobGradeText.contentEquals("-") || jobGradeText.isEmpty() || jobGradeText.isBlank()) {
				jobGradeText = "NULL";
				orgJobGrade = jobGradeText;
			}
			orgJobGrade = jobGradeText;
			ExtentCucumberAdapter.addTestStepLog("Grade value of Organization Job of Profile with View Other Matches button : " + orgJobGrade);
			LOGGER.info("Grade value of Organization Job of Profile with View Other Matches button : " + orgJobGrade);
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_verify_organization_job_grade_value", e);
			LOGGER.error("Issue in Verifying Organization Job Grade value - Method: user_should_verify_organization_job_grade_and_department_values_of_job_profile_with_view_other_matches_button", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying Organization Job Grade value of Profile with View Other Matches button...Please Investigate!!!");
			Assert.fail("Issue in Verifying Organization Job Grade value of Profile with View Other Matches button...Please Investigate!!!");
		}
		
		try {
			WebElement	jobDepartment = driver.findElement(By.xpath("//tbody//tr["+Integer.toString(rowNumber-1)+"]//td[4]//div[1]"));
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(jobDepartment)).isDisplayed());
			String jobDepartmentText = wait.until(ExpectedConditions.visibilityOf(jobDepartment)).getText(); 
			if(jobDepartmentText.contentEquals("-") || jobDepartmentText.isEmpty() || jobDepartmentText.isBlank()) {
				jobDepartmentText = "NULL";
				orgJobDepartment = jobDepartmentText;
			}
			orgJobDepartment = jobDepartmentText;
			ExtentCucumberAdapter.addTestStepLog("Department value of Organization Job of Profile with View Other Matches button : " + orgJobDepartment);
			LOGGER.info("Department value of Organization Job of Profile with View Other Matches button : " + orgJobDepartment);
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_verify_organization_job_department_value", e);
			LOGGER.error("Issue in Verifying Organization Job Department value - Method: user_should_verify_organization_job_grade_and_department_values_of_job_profile_with_view_other_matches_button", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying Organization Job Department value of Profile with View Other Matches button...Please Investigate!!!");
			Assert.fail("Issue in Verifying Organization Job Department value of Profile with View Other Matches button...Please Investigate!!!");
		}	
	}
	
	public void user_should_verify_organization_job_function_or_sub_function_of_job_profile_with_view_other_matches_button() {
		try {
			WebElement	jobFunction = driver.findElement(By.xpath("//tbody//tr["+Integer.toString(rowNumber)+"]//div//span[2]"));
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(jobFunction)).isDisplayed());
			String jobFunctionText = wait.until(ExpectedConditions.visibilityOf(jobFunction)).getText(); 
			if(jobFunctionText.contentEquals("-") || jobFunctionText.isEmpty() || jobFunctionText.isBlank()) {
				jobFunctionText = "NULL | NULL";
				orgJobFunction = jobFunctionText;
			} else if (jobFunctionText.endsWith("-") || jobFunctionText.endsWith("| -") || jobFunctionText.endsWith("|") || (!(jobFunctionText.contains("|")) && (jobFunctionText.length() > 1))) {
				jobFunctionText = jobFunctionText + " | NULL";
				orgJobFunction = jobFunctionText;
			}
			
			orgJobFunction = jobFunctionText;
			ExtentCucumberAdapter.addTestStepLog("Function / Sub-function values of Organization Job of Profile with View Other Matches button : " + orgJobFunction);
			LOGGER.info("Function / Sub-function values of Organization Job of Profile with View Other Matches button : " + orgJobFunction);
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_verify_organization_job_function_or_sub_function_of_job_profile_with_view_other_matches_button", e);
			LOGGER.error("Issue in Verifying Organization Job Function / Sub-function values - Method: user_should_verify_organization_job_function_or_sub_function_of_job_profile_with_view_other_matches_button", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying Organization Job Function / Sub-function values of Profile with View Other Matches button...Please Investigate!!!");
			Assert.fail("Issue in Verifying Organization Job Function / Sub-function values of Profile with View Other Matches button...Please Investigate!!!");
		}
	}
	
	public void click_on_matched_profile_of_job_profile_with_view_other_matches_button() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			WebElement	linkedMatchedProfile = driver.findElement(By.xpath("//div[@id='kf-job-container']//div//table//tbody//tr["+Integer.toString(rowNumber-1)+"]//td[1]//div"));
			String MatchedProfileNameText = wait.until(ExpectedConditions.elementToBeClickable(linkedMatchedProfile)).getText();
			matchedSuccessPrflName = MatchedProfileNameText;
			try {
				wait.until(ExpectedConditions.elementToBeClickable(linkedMatchedProfile)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", linkedMatchedProfile);
				} catch (Exception s) {
					utils.jsClick(driver, linkedMatchedProfile);
				}
			}
			ExtentCucumberAdapter.addTestStepLog("Clicked on Matched Profile with name "+ MatchedProfileNameText +" of Organization Job "+ orgJobName);
			LOGGER.info("Clicked on Matched Profile with name "+ MatchedProfileNameText +" of Organization Job "+ orgJobName);
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_on_matched_profile_of_job_profile_with_view_other_matches_button", e);
			LOGGER.error("Issue in clicking Matched Profile linked with job name "+ orgJobName + " - Method: click_on_matched_profile_of_job_profile_with_view_other_matches_button", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in clicking Matched Profile linked with job name "+ orgJobName + "...Please Investigate!!!");
			Assert.fail("Issue in clicking Matched Profile linked with job name "+ orgJobName + "...Please Investigate!!!");
		}
	}
	public void verify_user_navigated_to_job_comparison_page() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			String compareAndSelectHeaderText = wait.until(ExpectedConditions.visibilityOf(CompareandSelectheader)).getText();
			Assert.assertEquals(compareAndSelectHeaderText, "Which profile do you want to use for this job?");
			LOGGER.info("User navigated to Job Compare page successfully");
			ExtentCucumberAdapter.addTestStepLog("User navigated to Job Compare page successfully");
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_user_navigated_to_job_comparison_page", e);
			LOGGER.error("Issue in navigating to Job Compare page - Method: verify_user_navigated_to_job_comparison_page", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in navigating to Job Compare page....Please Investigate!!!");
			Assert.fail("Issue in navigating to Job Compare page....Please Investigate!!!");
		}
	}
	
	public void user_is_in_job_comparison_page() {
		LOGGER.info("User is in Job Comparison Page...");
		ExtentCucumberAdapter.addTestStepLog("User is in Job Comparison Page...");
	}
	
	public void validate_organization_job_name_and_code_in_job_comparison_page() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			String JCpageOrgJobTitleHeaderText = wait.until(ExpectedConditions.visibilityOf(JCpageOrgJobTitleHeader)).getText();
			Assert.assertTrue(JCpageOrgJobTitleHeaderText.contains(PO15_ValidateRecommendedProfileDetails.orgJobName));
			Assert.assertTrue(JCpageOrgJobTitleHeaderText.contains(PO15_ValidateRecommendedProfileDetails.orgJobCode));
			LOGGER.info("Organization Job Name and Job code validated successfully in the Job Compare page");
			ExtentCucumberAdapter.addTestStepLog("Organization Job Name and Job code validated successfully in the Job Compare page");
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_organization_job_name_and_code_in_job_comparison_page", e);
			LOGGER.error("Issue in validating Organization Job Name and Job Code - Method: validate_organization_job_name_and_code_in_job_comparison_page", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in validating Organization Job Name and Job Code in the Job Compare page....Please Investigate!!!");
			Assert.fail("Issue in validating Organization Job Name and Job Code in the Job Compare page....Please Investigate!!!");
		}	
	}
	
	public void user_should_validate_organization_job_grade_department_and_function_or_subfunction_in_job_comparison_page() {
		try {
			String JCpageOrgJobGradeValueText = wait.until(ExpectedConditions.visibilityOf(JCpageOrgJobGradeValue)).getText();
			if(JCpageOrgJobGradeValueText.contentEquals("-") || JCpageOrgJobGradeValueText.isEmpty() || JCpageOrgJobGradeValueText.isBlank()) {
				JCpageOrgJobGradeValueText = "NULL";
				Assert.assertTrue(JCpageOrgJobGradeValueText.contains(PO15_ValidateRecommendedProfileDetails.orgJobGrade));
			} else {
				Assert.assertTrue(JCpageOrgJobGradeValueText.contains(PO15_ValidateRecommendedProfileDetails.orgJobGrade));
			}
			LOGGER.info("Organization Job Grade value validated successfully in the Job Compare page");
			ExtentCucumberAdapter.addTestStepLog("Organization Job Grade value validated successfully in the Job Compare page");
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_validate_organization_job_grade", e);
			LOGGER.error("Issue in validating Organization Job Grade value - Method: user_should_validate_organization_job_grade_department_and_function_or_subfunction_in_job_comparison_page", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in validating Organization Job Grade value in the Job Compare page....Please Investigate!!!");
			Assert.fail("Issue in validating Organization Job Grade value in the Job Compare page....Please Investigate!!!");
		}	
		
		try {
			String JCpageOrgJobDepartmentValueText = wait.until(ExpectedConditions.visibilityOf(JCpageOrgJobDepartmentValue)).getText();
			if(JCpageOrgJobDepartmentValueText.contentEquals("-") || JCpageOrgJobDepartmentValueText.isEmpty() || JCpageOrgJobDepartmentValueText.isBlank()) {
				JCpageOrgJobDepartmentValueText = "NULL";
				Assert.assertTrue(JCpageOrgJobDepartmentValueText.contains(PO15_ValidateRecommendedProfileDetails.orgJobDepartment));
			} else {
				Assert.assertTrue(JCpageOrgJobDepartmentValueText.contains(PO15_ValidateRecommendedProfileDetails.orgJobDepartment));
			}
			LOGGER.info("Organization Job Department value validated successfully in the Job Compare page");
			ExtentCucumberAdapter.addTestStepLog("Organization Job Department value validated successfully in the Job Compare page");
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_validate_organization_job_department", e);
			LOGGER.error("Issue in validating Organization Job Department value - Method: user_should_validate_organization_job_grade_department_and_function_or_subfunction_in_job_comparison_page", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in validating Organization Job Department value in the Job Compare page....Please Investigate!!!");
			Assert.fail("Issue in validating Organization Job Department value in the Job Compare page....Please Investigate!!!");
		}	
		
		try {
			String JCpageOrgJobFunctionValueText = wait.until(ExpectedConditions.visibilityOf(JCpageOrgJobFunctionValue)).getText();
			if(JCpageOrgJobFunctionValueText.contentEquals("-") || JCpageOrgJobFunctionValueText.isEmpty() || JCpageOrgJobFunctionValueText.isBlank()) {
				JCpageOrgJobFunctionValueText = "NULL | NULL";
				Assert.assertTrue(JCpageOrgJobFunctionValueText.contains(PO15_ValidateRecommendedProfileDetails.orgJobFunction));
			} else if (JCpageOrgJobFunctionValueText.endsWith("-") || JCpageOrgJobFunctionValueText.endsWith("| -") || JCpageOrgJobFunctionValueText.endsWith("|") || (!(JCpageOrgJobFunctionValueText.contains("|")) && (JCpageOrgJobFunctionValueText.length() > 1))) {
				JCpageOrgJobFunctionValueText = JCpageOrgJobFunctionValueText + " | NULL";
			} else {
				Assert.assertTrue(JCpageOrgJobFunctionValueText.contains(PO15_ValidateRecommendedProfileDetails.orgJobFunction));
			}
			LOGGER.info("Organization Job Function / Sub-function values validated successfully in the Job Compare page");
			ExtentCucumberAdapter.addTestStepLog("Organization Job Function / Sub-function values validated successfully in the Job Compare page");
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_validate_organization_job_function_or_subfunction", e);
			LOGGER.error("Issue in validating Organization Job Function / Sub-function values - Method: user_should_validate_organization_job_grade_department_and_function_or_subfunction_in_job_comparison_page", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in validating Organization Job Function / Sub-function values in the Job Compare page....Please Investigate!!!");
			Assert.fail("Issue in validating Organization Job Function / Sub-function values in the Job Compare page....Please Investigate!!!");
		}	
	}
	
	public void user_should_verify_recommended_profile_name_matches_with_matched_success_profile_name() {
		try {
			String JCpageProfile1TitleText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1Title)).getText();
			Assert.assertTrue(JCpageProfile1TitleText.contains(PO15_ValidateRecommendedProfileDetails.matchedSuccessPrflName));
			LOGGER.info("Recommended Profile Name in the Job Compare page matches with Matched Success Profile Name");
			ExtentCucumberAdapter.addTestStepLog("Recommended Profile Name in the Job Compare page matches with Matched Success Profile Name");
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_verify_recommended_profile_name_matches_with_matched_success_profile_name", e);
			LOGGER.error("Issue in verifying Recommended Profile Name - Method: user_should_verify_recommended_profile_name_matches_with_matched_success_profile_name", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying Recommended Profile Name in the Job Compare page....Please Investigate!!!");
			Assert.fail("Issue in verifying Recommended Profile Name in the Job Compare page....Please Investigate!!!");
		}	
	}
	
	public void user_should_verify_recommended_tag_and_select_button_is_displaying_on_the_profile() {
		try {
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(JCpageProfile1RecommendedTag)).isDisplayed());
			LOGGER.info("Recommended tag is displaying on the Recommended Profile as expected");
			ExtentCucumberAdapter.addTestStepLog("Recommended tag is displaying on the Recommended Profile as expected");
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(JCpageProfile1SelectBtn)).isDisplayed());
			Assert.assertEquals("true",wait.until(ExpectedConditions.visibilityOf(JCpageProfile1SelectBtn)).getAttribute("aria-checked").toString());
			LOGGER.info("Select button is displaying on the Recommended Profile and is in Selected status as expected by default");
			ExtentCucumberAdapter.addTestStepLog("Select button is displaying on the Recommended Profile and is in Selected status as expected by default");
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_verify_recommended_tag_and_select_button_is_displaying_on_the_profile", e);
			LOGGER.error("Issue in Verifying Recommended tag and Select button - Method: user_should_verify_recommended_tag_and_select_button_is_displaying_on_the_profile", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying Recommended tag and Select button on Recommended Profile in the Job Compare page....Please Investigate!!!");
			Assert.fail("Issue in Verifying Recommended tag and Select button on Recommended Profile in the Job Compare page....Please Investigate!!!");
		}	
	}
	
	public void validate_recommended_profile_grade_matches_with_matched_success_profile_grade() {
		try {
			String JCpageProfile1GradeText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1Grade)).getText();
			Assert.assertTrue(PO05_ValidateJobProfileDetailsPopup.ProfileDetails.contains("Grade:"+JCpageProfile1GradeText));
			LOGGER.info("Recommended Profile Grade in the Job Compare page matches with Matched Success Profile Grade as expected");
			ExtentCucumberAdapter.addTestStepLog("Recommended Profile Grade in the Job Compare page matches with Matched Success Profile Grade as expected");
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_recommended_profile_grade_matches_with_matched_success_profile_grade", e);
			LOGGER.error("Issue in Validating Recommended Profile Grade - Method: validate_recommended_profile_grade_matches_with_matched_success_profile_grade", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in Validating Recommended Profile Grade in the Job Compare page....Please Investigate!!!");
			Assert.fail("Issue in Validating Recommended Profile Grade in the Job Compare page....Please Investigate!!!");
		}
	}
	
	public void validate_recommended_profile_level_sublevels_matches_with_matched_success_profile_level_sublevels() {
		try {
			String JCpageProfile1LevelText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1Level)).getText();
			Assert.assertTrue(PO05_ValidateJobProfileDetailsPopup.ProfileDetails.contains("Level / Sublevel:"+JCpageProfile1LevelText));
			LOGGER.info("Recommended Profile Level / Sublevels in the Job Compare page matches with Matched Success Profile Level / Sublevel as expected");
			ExtentCucumberAdapter.addTestStepLog("Recommended Profile Level / Sublevels in the Job Compare page matches with Matched Success Profile Level / Sublevels as expected");
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_recommended_profile_level_sublevels_matches_with_matched_success_profile_level_sublevels", e);
			LOGGER.error("Issue in Validating Recommended Profile Level / Sublevels - Method: validate_recommended_profile_level_sublevels_matches_with_matched_success_profile_level_sublevels", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in Validating Recommended Profile Level / Sublevels in the Job Compare page....Please Investigate!!!");
			Assert.fail("Issue in Validating Recommended Profile Level / Sublevels in the Job Compare page....Please Investigate!!!");
		}
	}
	
	public void validate_recommended_profile_function_subfunction_matches_with_matched_success_profile_function_subfunction() {
		try {
			js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1Function);
			String JCpageProfile1FunctionText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1Function)).getText();
			Assert.assertTrue(PO05_ValidateJobProfileDetailsPopup.ProfileDetails.contains("Function / Sub-function:"+JCpageProfile1FunctionText));
			LOGGER.info("Recommended Profile Function / Sub-function in the Job Compare page matches with Matched Success Profile Function / Sub-function as expected");
			ExtentCucumberAdapter.addTestStepLog("Recommended Profile Function / Sub-function in the Job Compare page matches with Matched Success Profile Function / Sub-function as expected");
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_recommended_profile_function_subfunction_matches_with_matched_success_profile_function_subfunction", e);
			LOGGER.error("Issue in Validating Recommended Profile Function / Sub-function - Method: validate_recommended_profile_function_subfunction_matches_with_matched_success_profile_function_subfunction", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in Validating Recommended Profile Function / Sub-function in the Job Compare page....Please Investigate!!!");
			Assert.fail("Issue in Validating Recommended Profile Function / Sub-function in the Job Compare page....Please Investigate!!!");
		}
	}
	
	public void validate_recommended_profile_seniority_level_matches_with_matched_success_profile_seniority_level() {
		try {
			js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1SeniorityLevel);
			String JCpageProfile1SeniorityLevelText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1SeniorityLevel)).getText();
			Assert.assertTrue(PO05_ValidateJobProfileDetailsPopup.ProfileDetails.contains("Seniority level:"+JCpageProfile1SeniorityLevelText));
			LOGGER.info("Recommended Profile Seniority level in the Job Compare page matches with Matched Success Profile Seniority level as expected");
			ExtentCucumberAdapter.addTestStepLog("Recommended Profile Seniority level in the Job Compare page matches with Matched Success Profile Seniority level as expected");
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_recommended_profile_seniority_level_matches_with_matched_success_profile_seniority_level", e);
			LOGGER.error("Issue in Validating Recommended Profile Seniority level - Method: validate_recommended_profile_seniority_level_matches_with_matched_success_profile_seniority_level", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in Validating Recommended Profile Seniority level in the Job Compare page....Please Investigate!!!");
			Assert.fail("Issue in Validating Recommended Profile Seniority level in the Job Compare page....Please Investigate!!!");
		}
	}
	
	public void validate_recommended_profile_managerial_experience_matches_with_matched_success_profile_managerial_experience() {
		try {
			js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1ManagerialExperience);
			String JCpageProfile1ManagerialExperienceText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1ManagerialExperience)).getText();
			Assert.assertTrue(PO05_ValidateJobProfileDetailsPopup.ProfileDetails.contains("Managerial experience:"+JCpageProfile1ManagerialExperienceText));
			LOGGER.info("Recommended Profile Managerial experience in the Job Compare page matches with Matched Success Profile Managerial experience as expected");
			ExtentCucumberAdapter.addTestStepLog("Recommended Profile Managerial experience in the Job Compare page matches with Matched Success Profile Managerial experience as expected");
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_recommended_profile_managerial_experience_matches_with_matched_success_profile_managerial_experience", e);
			LOGGER.error("Issue in Validating Recommended Profile Managerial experience - Method: validate_recommended_profile_managerial_experience_matches_with_matched_success_profile_managerial_experience", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in Validating Recommended Profile Managerial experience in the Job Compare page....Please Investigate!!!");
			Assert.fail("Issue in Validating Recommended Profile Managerial experience in the Job Compare page....Please Investigate!!!");
		}
	}
	
	public void validate_recommended_profile_education_matches_with_matched_success_profile_education() {
		try {
			js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1Education);
			String JCpageProfile1EducationText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1Education)).getText();
			Assert.assertTrue(PO05_ValidateJobProfileDetailsPopup.ProfileDetails.contains("Education:"+JCpageProfile1EducationText));
			LOGGER.info("Recommended Profile Education in the Job Compare page matches with Matched Success Profile Education as expected");
			ExtentCucumberAdapter.addTestStepLog("Recommended Profile Education in the Job Compare page matches with Matched Success Profile Education as expected");
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_recommended_profile_education_matches_with_matched_success_profile_education", e);
			LOGGER.error("Issue in Validating Recommended Profile Education - Method: validate_recommended_profile_education_matches_with_matched_success_profile_education", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in Validating Recommended Profile Education in the Job Compare page....Please Investigate!!!");
			Assert.fail("Issue in Validating Recommended Profile Education in the Job Compare page....Please Investigate!!!");
		}
	}
	
	public void validate_recommended_profile_general_experience_matches_with_matched_success_profile_general_experience() {
		try {
			js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1GeneralExperience);
			String JCpageProfile1GeneralExperienceText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1GeneralExperience)).getText();
			Assert.assertTrue(PO05_ValidateJobProfileDetailsPopup.ProfileDetails.contains("General experience:"+JCpageProfile1GeneralExperienceText));
			LOGGER.info("Recommended Profile General experience in the Job Compare page matches with Matched Success Profile General experience as expected");
			ExtentCucumberAdapter.addTestStepLog("Recommended Profile General experience in the Job Compare page matches with Matched Success Profile General experience as expected");
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_recommended_profile_general_experience_matches_with_matched_success_profile_general_experience", e);
			LOGGER.error("Issue in Validating Recommended Profile General experience - Method: validate_recommended_profile_general_experience_matches_with_matched_success_profile_general_experience", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in Validating Recommended Profile General experience in the Job Compare page....Please Investigate!!!");
			Assert.fail("Issue in Validating Recommended Profile General experience in the Job Compare page....Please Investigate!!!");
		}
	}
	
	public void validate_recommended_profile_role_summary_matches_with_matched_success_profile_role_summary() {
		try {
			js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1RoleSummary);
			String JCpageProfile1RoleSummaryText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1RoleSummary)).getText();
			Assert.assertEquals(PO05_ValidateJobProfileDetailsPopup.ProfileRoleSummary, JCpageProfile1RoleSummaryText);
			LOGGER.info("Recommended Profile Role Summary in the Job Compare page matches with Matched Success Profile Role Summary as expected");
			ExtentCucumberAdapter.addTestStepLog("Recommended Profile Role Summary in the Job Compare page matches with Matched Success Profile Role Summary as expected");
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_recommended_profile_role_summary_matches_with_matched_success_profile_role_summary", e);
			LOGGER.error("Issue in Validating Recommended Profile Role Summary - Method: validate_recommended_profile_role_summary_matches_with_matched_success_profile_role_summary", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in Validating Recommended Profile Role Summary in the Job Compare page....Please Investigate!!!");
			Assert.fail("Issue in Validating Recommended Profile Role Summary in the Job Compare page....Please Investigate!!!");
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
						LOGGER.info("Reached end of content in Recommended Profile Responsibilities Section in Job Comparison Page");
						ExtentCucumberAdapter.addTestStepLog("Reached end of content in Recommended Profile Responsibilities Section in Job Comparison Page");
						break;
					}
					String ViewMoreResponsibilitiesBtnText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1ViewMoreResponsibilitiesBtn.get(0))).getText();
					js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1ViewMoreResponsibilitiesBtn.get(0));
					PerformanceUtils.waitForUIStability(driver, 2);
					try {
						wait.until(ExpectedConditions.elementToBeClickable(JCpageProfile1ViewMoreResponsibilitiesBtn.get(0))).click();
					} catch(Exception e) {
						js.executeScript("arguments[0].click();", JCpageProfile1ViewMoreResponsibilitiesBtn.get(0));
					}
					LOGGER.info("Clicked on "+ ViewMoreResponsibilitiesBtnText +" button in Recommended Profile Responsibilities Section in Job Comparison page");
					ExtentCucumberAdapter.addTestStepLog("Clicked on "+ ViewMoreResponsibilitiesBtnText +" button in Recommended Profile Responsibilities Section in Job Comparison page");
					PerformanceUtils.waitForUIStability(driver, 2);
					js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1ViewMoreResponsibilitiesBtn.get(0));
				} catch(StaleElementReferenceException e) {
					js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
					LOGGER.info("Reached end of content in Recommended Profile Responsibilities Section in Job Comparison Page");
					ExtentCucumberAdapter.addTestStepLog("Reached end of content in Recommended Profile Responsibilities Section in Job Comparison Page");
					break;
				}	
			}
			String JCpageProfile1ResponsibilitiesText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1Responsibilities)).getText();
			Assert.assertEquals(PO05_ValidateJobProfileDetailsPopup.ProfileResponsibilities, JCpageProfile1ResponsibilitiesText);
			LOGGER.info("Recommended Profile Responsibilities in the Job Compare page matches with Matched Success Profile Responsibilities as expected");
			ExtentCucumberAdapter.addTestStepLog("Recommended Profile Responsibilities in the Job Compare page matches with Matched Success Profile Responsibilities as expected");
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_recommended_profile_responsibilities_matches_with_matched_success_profile_responsibilities", e);
			LOGGER.error("Issue in validating data in Recommended Profile Responsibilities Section - Method: validate_recommended_profile_responsibilities_matches_with_matched_success_profile_responsibilities", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in validating data in Recommended Profile Responsibilities Section in Job Comparison page...Please Investigate!!!");
			Assert.fail("Issue in validating data in Recommended Profile Responsibilities Section in Job Comparison page...Please Investigate!!!");
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
						LOGGER.info("Reached end of content in Recommended Profile Behavioural Competencies Section in Job Comparison Page");
						ExtentCucumberAdapter.addTestStepLog("Reached end of content in Recommended Profile Behavioural Competencies Section in Job Comparison Page");
						break;
					}
					String ViewMoreCompetenciesBtnText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1ViewMoreCompetenciesBtn.get(0))).getText();
					js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1ViewMoreCompetenciesBtn.get(0));
					PerformanceUtils.waitForUIStability(driver, 2);
					try {
						wait.until(ExpectedConditions.elementToBeClickable(JCpageProfile1ViewMoreCompetenciesBtn.get(0))).click();
					} catch(Exception e) {
						js.executeScript("arguments[0].click();", JCpageProfile1ViewMoreCompetenciesBtn.get(0));
					}
					LOGGER.info("Clicked on "+ ViewMoreCompetenciesBtnText +" button in Recommended Profile Behavioural Competencies Section in Job Comparison page");
					ExtentCucumberAdapter.addTestStepLog("Clicked on "+ ViewMoreCompetenciesBtnText +" button in Recommended Profile Behavioural Competencies Section in Job Comparison page");
					PerformanceUtils.waitForUIStability(driver, 2);
					js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1ViewMoreCompetenciesBtn.get(0));
				} catch(StaleElementReferenceException e) {
					LOGGER.info("Reached end of content in Recommended Profile Behavioural Competencies Section in Job Comparison Page");
					ExtentCucumberAdapter.addTestStepLog("Reached end of content in Recommended Profile Behavioural Competencies Section in Job Comparison Page");
					break;
				}	
			}
			String JCpageProfile1BehaviouralCompetenciesText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1BehaviouralCompetencies)).getText();
			Assert.assertEquals(PO05_ValidateJobProfileDetailsPopup.ProfileBehaviouralCompetencies, JCpageProfile1BehaviouralCompetenciesText);
			LOGGER.info("Recommended Profile Behavioural Competencies in the Job Compare page matches with Matched Success Profile Behavioural Competencies as expected");
			ExtentCucumberAdapter.addTestStepLog("Recommended Profile Behavioural Competencies in the Job Compare page matches with Matched Success Profile Behavioural Competencies as expected");		
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_recommended_profile_behavioural_competencies_matches_with_matched_success_profile_behavioural_competencies", e);
			LOGGER.error("Issue in validating data in Recommended Profile Behavioural Competencies Section - Method: validate_recommended_profile_behavioural_competencies_matches_with_matched_success_profile_behavioural_competencies", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in validating data in Recommended Profile Behavioural Competencies Section in Job Comparison page...Please Investigate!!!");
			Assert.fail("Issue in validating data in Recommended Profile Behavioural Competencies Section in Job Comparison page...Please Investigate!!!");
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
							LOGGER.info("Reached end of content in Recommended Profile Skills Section in Job Comparison Page");
							ExtentCucumberAdapter.addTestStepLog("Reached end of content in Recommended Profile Skills Section in Job Comparison Page");
							break;
						} 
						String ViewMoreSkillsBtnText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1ViewMoreSkillsBtn.get(0))).getText();
						js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1ViewMoreSkillsBtn.get(0));
						PerformanceUtils.waitForUIStability(driver, 2);
						js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1ViewMoreSkillsBtn.get(0));
						try {
							wait.until(ExpectedConditions.elementToBeClickable(JCpageProfile1ViewMoreSkillsBtn.get(0))).click();
						} catch(Exception e) {
							js.executeScript("arguments[0].click();", JCpageProfile1ViewMoreCompetenciesBtn.get(0));
						}
						LOGGER.info("Clicked on "+ ViewMoreSkillsBtnText +" button in Recommended Profile Skills Section in Job Comparison page");
						ExtentCucumberAdapter.addTestStepLog("Clicked on "+ ViewMoreSkillsBtnText +" button in Recommended Profile Skills Section in Job Comparison page");
						PerformanceUtils.waitForUIStability(driver, 2);
						js.executeScript("arguments[0].scrollIntoView(true);", JCpageProfile1ViewMoreSkillsBtn.get(0));
					} catch(StaleElementReferenceException e) {
						LOGGER.info("Reached end of content in Recommended Profile Skills Section in Job Comparison Page");
						ExtentCucumberAdapter.addTestStepLog("Reached end of content in Recommended Profile Skills Section in Job Comparison Page");
						break;
					}	
				}
				String JCpageProfile1SkillsText = wait.until(ExpectedConditions.visibilityOf(JCpageProfile1Skills)).getText();
				Assert.assertEquals(PO05_ValidateJobProfileDetailsPopup.ProfileSkills, JCpageProfile1SkillsText);
				LOGGER.info("Recommended Profile Skills in the Job Compare page matches with Matched Success Profile Skills as expected");
				ExtentCucumberAdapter.addTestStepLog("Recommended Profile Skills in the Job Compare page matches with Matched Success Profile Skills as expected");
			} catch (Exception e) {
				ScreenshotHandler.captureFailureScreenshot("validate_recommended_profile_skills_matches_with_matched_success_profile_skills", e);
				LOGGER.error("Issue in validating data in Recommended Profile Skills Section - Method: validate_recommended_profile_skills_matches_with_matched_success_profile_skills", e);
				e.printStackTrace();
				ExtentCucumberAdapter.addTestStepLog("Issue in validating data in Recommended Profile Skills Section in Job Comparison page...Please Investigate!!!");
				Assert.fail("Issue in validating data in Recommended Profile Skills Section in Job Comparison page...Please Investigate!!!");
			}
	 }
}
