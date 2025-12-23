package com.kfonetalentsuite.pageobjects.JobMapping;

import java.time.Duration;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.PageObjectHelper;

public class PO12_RecommendedProfileDetails extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO12_RecommendedProfileDetails.class);

	// ThreadLocal variables for thread-safe test execution
	public static ThreadLocal<Integer> rowNumber = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<String> orgJobName = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<String> orgJobCode = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<String> orgJobGrade = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<String> orgJobFunction = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<String> orgJobDepartment = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<String> matchedSuccessPrflName = ThreadLocal.withInitial(() -> "NOT_SET");

	// ============================================================
	// Static variables moved from PO05_ValidateJobProfileDetailsPopup
	// ============================================================
	public static String ProfileDetails;
	public static String ProfileRoleSummary;
	public static String ProfileResponsibilities;
	public static String ProfileBehaviouralCompetencies;
	public static String ProfileSkills;

	// ============================================================
	// Locators moved from PO05_ValidateJobProfileDetailsPopup (Profile Details Popup)
	// ============================================================
	private static final By PROFILE_HEADER = By.xpath("//h2[@id='summary-modal']//p");
	private static final By PROFILE_DETAILS = By.xpath("//div[@id='details-container']//div[contains(@class,'mt-2')]");
	private static final By PROFILE_LEVEL_DROPDOWN = By.xpath("//select[contains(@id,'profileLevel') or @id='profile-level']");
	private static final By ROLE_SUMMARY = By.xpath("//div[@id='role-summary-container']//p");
	private static final By POPUP_VIEW_MORE_RESPONSIBILITIES = By.xpath("//div[@id='responsibilities-panel']//button[contains(text(),'View')]");
	private static final By POPUP_RESPONSIBILITIES_DATA = By.xpath("//div[@id='responsibilities-panel']//div[@id='item-container']");
	private static final By BEHAVIOUR_TAB_BTN = By.xpath("//button[contains(text(),'BEHAVIOUR')]");
	private static final By POPUP_VIEW_MORE_BEHAVIOUR = By.xpath("//div[@id='behavioural-panel']//button[contains(text(),'View')]");
	private static final By POPUP_BEHAVIOUR_DATA = By.xpath("//div[@id='behavioural-panel']//div[@id='item-container']");
	private static final By SKILLS_TAB_BTN = By.xpath("//button[text()='SKILLS']");
	private static final By POPUP_VIEW_MORE_SKILLS = By.xpath("//div[@id='skills-panel']//button[contains(text(),'View')]");
	private static final By POPUP_SKILLS_DATA = By.xpath("//div[@id='skills-panel']//div[@id='item-container']");
	private static final By PUBLISH_PROFILE_BTN = By.xpath("//button[@id='publish-job-profile']");
	private static final By POPUP_CONTAINER = By.xpath("//div[contains(@class, 'modal-body') or contains(@class, 'popup-content') or contains(@class, 'dialog-content')]");

	// Job Comparison Page Locators - from Locators.ComparisonPage
	private static final By COMPARE_AND_SELECT_HEADER = Locators.ComparisonPage.COMPARE_HEADER;
	private static final By ORG_JOB_TITLE_HEADER = By.xpath("//div[contains(@class,'leading')]//div[1]//div[1]");
	private static final By ORG_JOB_GRADE_VALUE = By.xpath("//div[contains(@class,'leading')]//div[contains(text(),'Grade')]//span");
	private static final By ORG_JOB_DEPARTMENT_VALUE = By.xpath("//div[contains(@class,'leading')]//div[contains(text(),'Department')]//span");
	private static final By ORG_JOB_FUNCTION_VALUE = By.xpath("//div[contains(@class,'leading')]//div[contains(text(),'Function')]//span");
	private static final By PROFILE_1_TITLE = By.xpath("//div[@class='shadow']//div[contains(@id,'card-title')]");
	private static final By PROFILE_1_SELECT_BTN = By.xpath("//div[@class='shadow']//div[contains(@id,'card-header')][1]//span");
	private static final By PROFILE_1_RECOMMENDED_TAG = By.xpath("//div[@class='shadow']//div[contains(@id,'recommended-title')]");
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

	public PO12_RecommendedProfileDetails() {
		super();
	}

	public void search_for_job_profile_with_view_other_matches_button() {
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tbody")));
			PerformanceUtils.waitForPageReady(driver, 2);

			for (int i = 2; i <= 47; i = i + 3) {
				try {
					WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(
							By.xpath("//tbody//tr[" + i + "]//button[not(contains(@id,'publish'))]")));

					scrollToElement(button);
					PerformanceUtils.waitForUIStability(driver, 1);

					wait.until(ExpectedConditions.visibilityOf(button));
					String text = button.getText();

					if (text.contains("Other Matches")) {
						rowNumber.set(i);
						WebElement jobName = driver.findElement(By.xpath("//tbody//tr[" + (rowNumber.get() - 1) + "]//td[2]//div[contains(text(),'(')]"));
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
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "search_for_job_profile_with_view_other_matches_button", "Issue searching Job Profile with View Other Matches button", e);
		}
	}

	public void user_should_verify_organization_job_name_and_job_code_values_of_job_profile_with_view_other_matches_button() {
		try {
			PerformanceUtils.waitForPageReady(driver, 2);
			WebElement jobName = driver.findElement(By.xpath("//tbody//tr[" + (rowNumber.get() - 1) + "]//td[2]//div[contains(text(),'(')]"));
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(jobName)).isDisplayed());
			String jobname1 = wait.until(ExpectedConditions.visibilityOf(jobName)).getText();
			
			String extractedJobName = jobname1.split("-", 2)[0].trim();
			String extractedJobCode = jobname1.split("-", 2)[1].trim().substring(1, jobname1.split("-", 2)[1].length() - 2);
			
			// Set values in PO12 ThreadLocal variables
			orgJobName.set(extractedJobName);
			orgJobCode.set(extractedJobCode);
			
			// Also sync to PO05 ThreadLocal variables to ensure consistency across all verification methods
			PO05_PublishJobProfile.job1OrgName.set(extractedJobName);
			PO05_PublishJobProfile.job1OrgCode.set(extractedJobCode);
			
			PageObjectHelper.log(LOGGER, "Job name: " + orgJobName.get());
			PageObjectHelper.log(LOGGER, "Job code: " + orgJobCode.get());
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_organization_job_name_and_job_code_values_of_job_profile_with_view_other_matches_button", "Issue verifying job name of Profile with View Other Matches button", e);
		}
	}

	public void user_should_verify_organization_job_grade_and_department_values_of_job_profile_with_view_other_matches_button() {
		try {
			WebElement jobGrade = driver.findElement(By.xpath("//tbody//tr[" + (rowNumber.get() - 1) + "]//td[3]//div[1]"));
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(jobGrade)).isDisplayed());
			String jobGradeText = wait.until(ExpectedConditions.visibilityOf(jobGrade)).getText();
			if (jobGradeText.contentEquals("-") || jobGradeText.isEmpty() || jobGradeText.isBlank()) {
				jobGradeText = "NULL";
			}
			orgJobGrade.set(jobGradeText);
			PageObjectHelper.log(LOGGER, "Grade value: " + orgJobGrade.get());
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_organization_job_grade_value", "Issue verifying Organization Job Grade value", e);
		}

		try {
			WebElement jobDepartment = driver.findElement(By.xpath("//tbody//tr[" + (rowNumber.get() - 1) + "]//td[4]//div[1]"));
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(jobDepartment)).isDisplayed());
			String jobDepartmentText = wait.until(ExpectedConditions.visibilityOf(jobDepartment)).getText();
			if (jobDepartmentText.contentEquals("-") || jobDepartmentText.isEmpty() || jobDepartmentText.isBlank()) {
				jobDepartmentText = "NULL";
			}
			orgJobDepartment.set(jobDepartmentText);
			PageObjectHelper.log(LOGGER, "Department value: " + orgJobDepartment.get());
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_organization_job_department_value", "Issue verifying Organization Job Department value", e);
		}
	}

	public void user_should_verify_organization_job_function_or_sub_function_of_job_profile_with_view_other_matches_button() {
		try {
			WebElement jobFunction = driver.findElement(By.xpath("//tbody//tr[" + rowNumber.get() + "]//div//span[2]"));
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(jobFunction)).isDisplayed());
			String jobFunctionText = wait.until(ExpectedConditions.visibilityOf(jobFunction)).getText();
			if (jobFunctionText.contentEquals("-") || jobFunctionText.isEmpty() || jobFunctionText.isBlank()) {
				jobFunctionText = "NULL | NULL";
			} else if (jobFunctionText.endsWith("-") || jobFunctionText.endsWith("| -") || jobFunctionText.endsWith("|")
					|| (!(jobFunctionText.contains("|")) && (jobFunctionText.length() > 1))) {
				jobFunctionText = jobFunctionText + " | NULL";
			}
			orgJobFunction.set(jobFunctionText);
			PageObjectHelper.log(LOGGER, "Function / Sub-function values: " + orgJobFunction.get());
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_organization_job_function_or_sub_function_of_job_profile_with_view_other_matches_button", "Issue verifying Organization Job Function / Sub-function values", e);
		}
	}

	public void click_on_matched_profile_of_job_profile_with_view_other_matches_button() {
		try {
			PerformanceUtils.waitForPageReady(driver, 5);
			waitForSpinners();

			int currentRow = rowNumber.get();
			if (currentRow == 0) {
				throw new Exception("rowNumber is not set. Please ensure 'Search for Job Profile with View Other Matches button' step executed successfully.");
			}

			int matchedProfileRow = currentRow - 1;
			String matchedProfileXPath = "//div[@id='kf-job-container']//div//table//tbody//tr[" + matchedProfileRow + "]//td[1]//div";

			WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement linkedMatchedProfile = shortWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(matchedProfileXPath)));

			WebElement clickableElement = shortWait.until(ExpectedConditions.elementToBeClickable(linkedMatchedProfile));
			String matchedProfileNameText = clickableElement.getText();

			if (matchedProfileNameText == null || matchedProfileNameText.trim().isEmpty()) {
				throw new Exception("Matched Profile name is empty at row " + matchedProfileRow);
			}

			matchedSuccessPrflName.set(matchedProfileNameText);
			scrollToElement(linkedMatchedProfile);
			Thread.sleep(300);

			Duration savedImplicitWait = null;
			try {
				savedImplicitWait = driver.manage().timeouts().getImplicitWaitTimeout();
				driver.manage().timeouts().implicitlyWait(Duration.ZERO);
			} catch (Exception e) {
				LOGGER.warn("Could not disable implicit wait: {}", e.getMessage());
			}

			try {
				try {
					List<WebElement> spinners = driver.findElements(Locators.Spinners.PAGE_LOAD_SPINNER);
					if (!spinners.isEmpty() && spinners.get(0).isDisplayed()) {
						WebDriverWait spinnerWait = new WebDriverWait(driver, Duration.ofSeconds(5));
						spinnerWait.until(ExpectedConditions.invisibilityOfAllElements(spinners));
					}
				} catch (Exception e) {
				}

				try {
					List<WebElement> loaders = driver.findElements(By.xpath("//div[@data-testid='loader' and contains(@class, 'fixed')]"));
					if (!loaders.isEmpty() && loaders.get(0).isDisplayed()) {
						WebDriverWait loaderWait = new WebDriverWait(driver, Duration.ofSeconds(5));
						loaderWait.until(ExpectedConditions.invisibilityOfAllElements(loaders));
					}
				} catch (Exception e) {
				}
			} finally {
				if (savedImplicitWait != null) {
					try {
						driver.manage().timeouts().implicitlyWait(savedImplicitWait);
					} catch (Exception e) {
						LOGGER.warn("Could not restore implicit wait: {}", e.getMessage());
					}
				}
			}

			clickableElement = shortWait.until(ExpectedConditions.elementToBeClickable(linkedMatchedProfile));

			boolean clickSuccessful = tryClickWithStrategies(clickableElement);

			if (clickSuccessful) {
				PageObjectHelper.log(LOGGER, "Clicked on Matched Profile: " + matchedProfileNameText + " of Organization Job: " + orgJobName.get());
				PerformanceUtils.waitForPageReady(driver, 10);
			} else {
				throw new Exception("All click strategies failed for Matched Profile: " + matchedProfileNameText);
			}

		} catch (Exception e) {
			LOGGER.error("Matched Profile Click FAILED: {}", e.getMessage());
			PageObjectHelper.handleError(LOGGER, "click_on_matched_profile_of_job_profile_with_view_other_matches_button",
					"Issue clicking Matched Profile linked with job name " + orgJobName.get() + " (rowNumber: " + rowNumber.get() + ")", e);
		}
	}

	public void verify_user_navigated_to_job_comparison_page() {
		try {
			waitForSpinners();
			String compareAndSelectHeaderText = getElementText(COMPARE_AND_SELECT_HEADER);
			Assert.assertEquals(compareAndSelectHeaderText, "Which profile do you want to use for this job?");
			PageObjectHelper.log(LOGGER, "User navigated to Job Compare page successfully");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_user_navigated_to_job_comparison_page", "Issue navigating to Job Compare page", e);
		}
	}

	public void user_is_in_job_comparison_page() {
		PageObjectHelper.log(LOGGER, "User is in Job Comparison Page");
	}

	public void validate_organization_job_name_and_code_in_job_comparison_page() {
		try {
			waitForSpinners();
			String orgJobTitleHeaderText = getElementText(ORG_JOB_TITLE_HEADER);
			Assert.assertTrue(orgJobTitleHeaderText.contains(PO12_RecommendedProfileDetails.orgJobName.get()));
			Assert.assertTrue(orgJobTitleHeaderText.contains(PO12_RecommendedProfileDetails.orgJobCode.get()));
			PageObjectHelper.log(LOGGER, "Organization Job Name and Job code validated successfully");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "validate_organization_job_name_and_code_in_job_comparison_page", "Issue validating Organization Job Name and Job Code", e);
		}
	}

	public void user_should_validate_organization_job_grade_department_and_function_or_subfunction_in_job_comparison_page() {
		try {
			String gradeValueText = getElementText(ORG_JOB_GRADE_VALUE);
			if (gradeValueText.contentEquals("-") || gradeValueText.isEmpty() || gradeValueText.isBlank()) {
				gradeValueText = "NULL";
			}
			Assert.assertTrue(gradeValueText.contains(PO12_RecommendedProfileDetails.orgJobGrade.get()));
			PageObjectHelper.log(LOGGER, "Organization Job Grade value validated successfully");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_validate_organization_job_grade", "Issue validating Organization Job Grade value", e);
		}

		try {
			String departmentValueText = getElementText(ORG_JOB_DEPARTMENT_VALUE);
			if (departmentValueText.contentEquals("-") || departmentValueText.isEmpty() || departmentValueText.isBlank()) {
				departmentValueText = "NULL";
			}
			Assert.assertTrue(departmentValueText.contains(PO12_RecommendedProfileDetails.orgJobDepartment.get()));
			PageObjectHelper.log(LOGGER, "Organization Job Department value validated successfully");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_validate_organization_job_department", "Issue validating Organization Job Department value", e);
		}

		try {
			String functionValueText = getElementText(ORG_JOB_FUNCTION_VALUE);
			if (functionValueText.contentEquals("-") || functionValueText.isEmpty() || functionValueText.isBlank()) {
				functionValueText = "NULL | NULL";
			} else if (functionValueText.endsWith("-") || functionValueText.endsWith("| -") || functionValueText.endsWith("|")
					|| (!(functionValueText.contains("|")) && (functionValueText.length() > 1))) {
				functionValueText = functionValueText + " | NULL";
			}
			Assert.assertTrue(functionValueText.contains(PO12_RecommendedProfileDetails.orgJobFunction.get()));
			PageObjectHelper.log(LOGGER, "Organization Job Function / Sub-function values validated successfully");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_validate_organization_job_function_or_subfunction", "Issue validating Organization Job Function / Sub-function values", e);
		}
	}

	public void user_should_verify_recommended_profile_name_matches_with_matched_success_profile_name() {
		try {
			String profileTitleText = getElementText(PROFILE_1_TITLE);
			Assert.assertTrue(profileTitleText.contains(PO12_RecommendedProfileDetails.matchedSuccessPrflName.get()));
			PageObjectHelper.log(LOGGER, "Recommended Profile Name matches with Matched Success Profile Name");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_recommended_profile_name_matches_with_matched_success_profile_name", "Issue verifying Recommended Profile Name", e);
		}
	}

	public void user_should_verify_recommended_tag_and_select_button_is_displaying_on_the_profile() {
		try {
			Assert.assertTrue(waitForElement(PROFILE_1_RECOMMENDED_TAG).isDisplayed());
			PageObjectHelper.log(LOGGER, "Recommended tag is displaying on Recommended Profile");
			WebElement selectBtn = waitForElement(PROFILE_1_SELECT_BTN);
			Assert.assertTrue(selectBtn.isDisplayed());
			Assert.assertEquals("true", selectBtn.getAttribute("aria-checked").toString());
			PageObjectHelper.log(LOGGER, "Select button is displaying and in Selected status by default");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_recommended_tag_and_select_button_is_displaying_on_the_profile", "Issue verifying Recommended tag and Select button", e);
		}
	}

	public void validate_recommended_profile_grade_matches_with_matched_success_profile_grade() {
		try {
			String gradeText = getElementText(PROFILE_1_GRADE);
			Assert.assertTrue(ProfileDetails.contains("Grade:" + gradeText));
			PageObjectHelper.log(LOGGER, "Recommended Profile Grade matches with Matched Success Profile Grade");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "validate_recommended_profile_grade_matches_with_matched_success_profile_grade", "Issue validating Recommended Profile Grade", e);
		}
	}

	public void validate_recommended_profile_level_sublevels_matches_with_matched_success_profile_level_sublevels() {
		try {
			String levelText = getElementText(PROFILE_1_LEVEL);
			Assert.assertTrue(ProfileDetails.contains("Level / Sublevel:" + levelText));
			PageObjectHelper.log(LOGGER, "Recommended Profile Level / Sublevels matches with Matched Success Profile");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "validate_recommended_profile_level_sublevels_matches_with_matched_success_profile_level_sublevels", "Issue validating Recommended Profile Level / Sublevels", e);
		}
	}

	public void validate_recommended_profile_function_subfunction_matches_with_matched_success_profile_function_subfunction() {
		try {
			scrollToElement(driver.findElement(PROFILE_1_FUNCTION));
			String functionText = getElementText(PROFILE_1_FUNCTION);
			Assert.assertTrue(ProfileDetails.contains("Function / Sub-function:" + functionText));
			PageObjectHelper.log(LOGGER, "Recommended Profile Function / Sub-function matches with Matched Success Profile");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "validate_recommended_profile_function_subfunction_matches_with_matched_success_profile_function_subfunction", "Issue validating Recommended Profile Function / Sub-function", e);
		}
	}

	public void validate_recommended_profile_seniority_level_matches_with_matched_success_profile_seniority_level() {
		try {
			scrollToElement(driver.findElement(PROFILE_1_SENIORITY));
			String seniorityText = getElementText(PROFILE_1_SENIORITY);
			Assert.assertTrue(ProfileDetails.contains("Seniority level:" + seniorityText));
			PageObjectHelper.log(LOGGER, "Recommended Profile Seniority level matches with Matched Success Profile");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "validate_recommended_profile_seniority_level_matches_with_matched_success_profile_seniority_level", "Issue validating Recommended Profile Seniority level", e);
		}
	}

	public void validate_recommended_profile_managerial_experience_matches_with_matched_success_profile_managerial_experience() {
		try {
			scrollToElement(driver.findElement(PROFILE_1_MANAGERIAL));
			String managerialText = getElementText(PROFILE_1_MANAGERIAL);
			Assert.assertTrue(ProfileDetails.contains("Managerial experience:" + managerialText));
			PageObjectHelper.log(LOGGER, "Recommended Profile Managerial experience matches with Matched Success Profile");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "validate_recommended_profile_managerial_experience_matches_with_matched_success_profile_managerial_experience", "Issue validating Recommended Profile Managerial experience", e);
		}
	}

	public void validate_recommended_profile_education_matches_with_matched_success_profile_education() {
		try {
			scrollToElement(driver.findElement(PROFILE_1_EDUCATION));
			String educationText = getElementText(PROFILE_1_EDUCATION);
			Assert.assertTrue(ProfileDetails.contains("Education:" + educationText));
			PageObjectHelper.log(LOGGER, "Recommended Profile Education matches with Matched Success Profile");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "validate_recommended_profile_education_matches_with_matched_success_profile_education", "Issue validating Recommended Profile Education", e);
		}
	}

	public void validate_recommended_profile_general_experience_matches_with_matched_success_profile_general_experience() {
		try {
			scrollToElement(driver.findElement(PROFILE_1_GENERAL_EXP));
			String generalExpText = getElementText(PROFILE_1_GENERAL_EXP);
			Assert.assertTrue(ProfileDetails.contains("General experience:" + generalExpText));
			PageObjectHelper.log(LOGGER, "Recommended Profile General experience matches with Matched Success Profile");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "validate_recommended_profile_general_experience_matches_with_matched_success_profile_general_experience", "Issue validating Recommended Profile General experience", e);
		}
	}

	public void validate_recommended_profile_role_summary_matches_with_matched_success_profile_role_summary() {
		try {
			scrollToElement(driver.findElement(PROFILE_1_ROLE_SUMMARY));
			String roleSummaryText = getElementText(PROFILE_1_ROLE_SUMMARY);
			Assert.assertEquals(ProfileRoleSummary, roleSummaryText);
			PageObjectHelper.log(LOGGER, "Recommended Profile Role Summary matches with Matched Success Profile");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "validate_recommended_profile_role_summary_matches_with_matched_success_profile_role_summary", "Issue validating Recommended Profile Role Summary", e);
		}
	}

	public void validate_recommended_profile_responsibilities_matches_with_matched_success_profile_responsibilities() {
		try {
			WebElement responsibilities = driver.findElement(PROFILE_1_RESPONSIBILITIES);
			scrollToElement(responsibilities);
			Thread.sleep(200);
			expandAllViewMoreButtons(VIEW_MORE_RESPONSIBILITIES);
			String responsibilitiesText = getElementText(PROFILE_1_RESPONSIBILITIES);
			Assert.assertEquals(ProfileResponsibilities, responsibilitiesText);
			PageObjectHelper.log(LOGGER, "Recommended Profile Responsibilities matches with Matched Success Profile");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "validate_recommended_profile_responsibilities_matches_with_matched_success_profile_responsibilities", "Issue validating Recommended Profile Responsibilities", e);
		}
	}

	public void validate_recommended_profile_behavioural_competencies_matches_with_matched_success_profile_behavioural_competencies() {
		try {
			WebElement competencies = driver.findElement(PROFILE_1_COMPETENCIES);
			scrollToElement(competencies);
			Thread.sleep(200);
			expandAllViewMoreButtons(VIEW_MORE_COMPETENCIES);
			String competenciesText = getElementText(PROFILE_1_COMPETENCIES);
			Assert.assertEquals(ProfileBehaviouralCompetencies, competenciesText);
			PageObjectHelper.log(LOGGER, "Recommended Profile Behavioural Competencies matches with Matched Success Profile");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "validate_recommended_profile_behavioural_competencies_matches_with_matched_success_profile_behavioural_competencies", "Issue validating Recommended Profile Behavioural Competencies", e);
		}
	}

	public void validate_recommended_profile_skills_matches_with_matched_success_profile_skills() {
		try {
			WebElement skills = driver.findElement(PROFILE_1_SKILLS);
			scrollToElement(skills);
			Thread.sleep(200);

			List<WebElement> viewMoreBtns = driver.findElements(VIEW_MORE_SKILLS);
			if (!viewMoreBtns.isEmpty()) {
				expandAllViewMoreButtons(VIEW_MORE_SKILLS);
			}

			String skillsText = getElementText(PROFILE_1_SKILLS);
			Assert.assertEquals(ProfileSkills, skillsText);
			PageObjectHelper.log(LOGGER, "Recommended Profile Skills matches with Matched Success Profile");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "validate_recommended_profile_skills_matches_with_matched_success_profile_skills", "Issue validating Recommended Profile Skills", e);
		}
	}

	// ============================================================
	// Methods moved from PO05_ValidateJobProfileDetailsPopup
	// ============================================================

	public void user_is_on_profile_details_popup() {
		PageObjectHelper.log(LOGGER, "User is on Profile Details Popup");
	}

	public void verify_profile_header_matches_with_matched_profile_name() {
		try {
			String profileHeaderName = getElementText(PROFILE_HEADER);
			Assert.assertEquals(matchedSuccessPrflName.get(), profileHeaderName);
			PageObjectHelper.log(LOGGER, "Profile header on the details popup: " + profileHeaderName);
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_profile_header_matches_with_matched_profile_name", "Failed to verify profile details popup header", e);
		}
	}

	public void verify_profile_details_displaying_on_the_popup() {
		try {
			String profileDetailsText = getElementText(PROFILE_DETAILS);
			ProfileDetails = profileDetailsText;
			PageObjectHelper.log(LOGGER, "Profile Details displaying on the popup screen:\n" + profileDetailsText);
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_profile_details_displaying_on_the_popup", "Failed to display profile details on the popup screen", e);
		}
	}

	public void user_should_verify_profile_level_dropdown_is_available_and_validate_levels_present_inside_dropdown() {
		try {
			WebElement dropdown = waitForElement(PROFILE_LEVEL_DROPDOWN);
			if (dropdown.isEnabled()) {
				dropdown.click();
				Select select = new Select(dropdown);
				List<WebElement> allOptions = select.getOptions();
				StringBuilder levels = new StringBuilder();
				for (WebElement option : allOptions) {
					levels.append(option.getText()).append(", ");
				}
				dropdown.click();
				PageObjectHelper.log(LOGGER, "Profile Level dropdown verified. Levels: " + levels.toString().replaceAll(", $", ""));
			} else {
				PageObjectHelper.log(LOGGER, "No Profile Levels available for profile: " + matchedSuccessPrflName.get());
			}
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_profile_level_dropdown_is_available_and_validate_levels_present_inside_dropdown", "Failed to validate profile level dropdown", e);
		}
	}

	public void validate_role_summary_is_displaying() {
		try {
			String roleSummaryText = getElementText(ROLE_SUMMARY);
			ProfileRoleSummary = roleSummaryText.split(": ", 2)[1].trim();
			PageObjectHelper.log(LOGGER, "Role summary of Matched Success Profile: " + ProfileRoleSummary);
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "validate_role_summary_is_displaying", "Failed to validate Role Summary in Profile Details Popup", e);
		}
	}

	public void validate_data_in_responsibilities_tab() {
		try {
			PerformanceUtils.waitForPageReady(driver, 2);
			expandAllViewMoreButtons(POPUP_VIEW_MORE_RESPONSIBILITIES);

			String responsibilitiesDataText = getElementText(POPUP_RESPONSIBILITIES_DATA);
			ProfileResponsibilities = responsibilitiesDataText;
			PageObjectHelper.log(LOGGER, "Responsibilities data validated successfully");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "validate_data_in_responsibilities_tab", "Failed to validate data in Responsibilities screen", e);
		}
	}

	public void validate_data_in_behavioural_competencies_tab() {
		try {
			scrollToElement(driver.findElement(ROLE_SUMMARY));
			PerformanceUtils.waitForPageReady(driver, 3);
			clickElement(BEHAVIOUR_TAB_BTN);
			expandAllViewMoreButtons(POPUP_VIEW_MORE_BEHAVIOUR);

			String behaviourDataText = getElementText(POPUP_BEHAVIOUR_DATA);
			ProfileBehaviouralCompetencies = behaviourDataText;
			PageObjectHelper.log(LOGGER, "Behavioural Competencies data validated successfully");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "validate_data_in_behavioural_competencies_tab", "Failed to validate data in Behaviour Competencies screen", e);
		}
	}

	public void validate_data_in_skills_tab() {
		try {
			scrollToElement(driver.findElement(ROLE_SUMMARY));
			PerformanceUtils.waitForPageReady(driver, 2);
			clickElement(SKILLS_TAB_BTN);
			expandAllViewMoreButtons(POPUP_VIEW_MORE_SKILLS);

			String skillsDataText = getElementText(POPUP_SKILLS_DATA);
			ProfileSkills = skillsDataText;
			PageObjectHelper.log(LOGGER, "Skills data validated successfully");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "validate_data_in_skills_tab", "Failed to validate data in Skills screen", e);
		}
	}

	public void user_should_verify_publish_profile_button_is_available_on_popup_screen() {
		try {
			try {
				WebElement publishBtn = driver.findElement(PUBLISH_PROFILE_BTN);
				js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'end', inline: 'nearest'});", publishBtn);
				Thread.sleep(1000);
			} catch (Exception scrollEx1) {
				try {
					WebElement popupContainer = driver.findElement(POPUP_CONTAINER);
					js.executeScript("arguments[0].scrollTop = arguments[0].scrollHeight;", popupContainer);
					Thread.sleep(1000);
				} catch (Exception scrollEx2) {
					scrollToBottom();
					Thread.sleep(500);
				}
			}

			PerformanceUtils.waitForPageReady(driver, 2);
			Thread.sleep(500);

			boolean isButtonDisplayed = wait.until(ExpectedConditions.elementToBeClickable(PUBLISH_PROFILE_BTN)).isDisplayed();
			if (!isButtonDisplayed) {
				throw new Exception("Publish button found but not displayed");
			}

			PageObjectHelper.log(LOGGER, "Publish button is displaying on the Profile Details Popup and is clickable");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_publish_profile_button_is_available_on_popup_screen", "Failed to verify Publish Profile button", e);
		}
	}
}
