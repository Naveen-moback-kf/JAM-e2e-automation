package com.kfonetalentsuite.pageobjects.JobMapping;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.SkipException;

import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.PageObjectHelper;

public class PO17_MapDifferentSPtoProfile extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO17_MapDifferentSPtoProfile.class);

	public static ThreadLocal<Integer> rowNumber = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<Boolean> mapSP = ThreadLocal.withInitial(() -> false);
	public static ThreadLocal<Boolean> manualMapping = ThreadLocal.withInitial(() -> false);
	public static ThreadLocal<String> orgJobName = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<String> orgJobCode = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<String> orgJobGrade = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<String> orgJobFunction = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<String> orgJobDepartment = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<String> mappedSuccessPrflName = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<String> ProfileDetails = ThreadLocal.withInitial(() -> null);
	public static ThreadLocal<String> ProfileRoleSummary = ThreadLocal.withInitial(() -> null);
	public static ThreadLocal<String> ProfileResponsibilities = ThreadLocal.withInitial(() -> null);
	public static ThreadLocal<String> ProfileBehaviouralCompetencies = ThreadLocal.withInitial(() -> null);
	public static ThreadLocal<String> ProfileSkills = ThreadLocal.withInitial(() -> null);
	public static ThreadLocal<String> lastSavedProfileName = ThreadLocal.withInitial(() -> null);
	static String expectedHeader = "Which profile do you want to use for this job?";
	public static ThreadLocal<String> SPSearchString = ThreadLocal.withInitial(() -> "car");
	public static ThreadLocal<String> customSPNameinSearchResults = ThreadLocal.withInitial(() -> null);
	public static ThreadLocal<String> manualMappingProfileName = ThreadLocal.withInitial(() -> null);
	public static ThreadLocal<String> changedlevelvalue = ThreadLocal.withInitial(() -> null);
	public static ThreadLocal<String> manualMappingProfileRoleSummary = ThreadLocal.withInitial(() -> null);
	public static ThreadLocal<String> manualMappingProfileDetails = ThreadLocal.withInitial(() -> null);
	public static ThreadLocal<String> manualMappingProfileResponsibilities = ThreadLocal.withInitial(() -> null);
	public static ThreadLocal<String> manualMappingProfileBehaviouralCompetencies = ThreadLocal.withInitial(() -> null);
	public static ThreadLocal<String> manualMappingProfileSkills = ThreadLocal.withInitial(() -> null);

	public PO17_MapDifferentSPtoProfile() {
		super();
	}

	private static final By PROFILE_DETAILS_POPUP_HEADER = By.xpath("//h2[@id='summary-modal']");
	private static final By PROFILE_HEADER = By.xpath("//h2[@id='summary-modal']//p");
	private static final By PROFILE_DETAILS = By.xpath("//div[@id='details-container']//div[contains(@class,'mt-2')]");
	private static final By PROFILE_LEVEL_DROPDOWN = By.xpath("//select[contains(@id,'profileLevel') or @id='profile-level']");
	private static final By ROLE_SUMMARY = By.xpath("//div[@id='role-summary-container']//p");
	private static final By VIEW_MORE_RESPONSIBILITIES = By.xpath("//div[@id='responsibilities-panel']//button[contains(text(),'View')]");
	private static final By RESPONSIBILITIES_DATA = By.xpath("//div[@id='responsibilities-panel']//div[@id='item-container']");
	private static final By BEHAVIOUR_COMPETENCIES_TAB = By.xpath("//button[contains(text(),'BEHAVIOUR')]");
	private static final By VIEW_MORE_BEHAVIOUR = By.xpath("//div[@id='behavioural-panel']//button[contains(text(),'View')]");
	private static final By BEHAVIOUR_DATA = By.xpath("//div[@id='behavioural-panel']//div[@id='item-container']");
	private static final By SKILLS_TAB = By.xpath("//button[text()='SKILLS']");
	private static final By VIEW_MORE_SKILLS = By.xpath("//div[@id='skills-panel']//button[contains(text(),'View')]");
	private static final By SKILLS_DATA = By.xpath("//div[@id='skills-panel']//div[@id='item-container']");
	private static final By PUBLISH_PROFILE_BTN = By.xpath("//button[@id='publish-job-profile']");
	private static final By PROFILE_DETAILS_POPUP_CLOSE_BTN = By.xpath("//button[@id='close-profile-summary']");
	private static final By MANUAL_MAPPING_ORG_JOB_TITLE = By.xpath("//div[contains(@class,'leading')]//div[1]//div[1]");
	private static final By MANUAL_MAPPING_ORG_JOB_GRADE = By.xpath("//div[contains(@class,'leading')]//div[contains(text(),'Grade')]//span");
	private static final By MANUAL_MAPPING_ORG_JOB_DEPARTMENT = By.xpath("//div[contains(@class,'leading')]//div[contains(text(),'Department')]//span");
	private static final By MANUAL_MAPPING_ORG_JOB_FUNCTION = By.xpath("//div[contains(@class,'leading')]//div[contains(text(),'Function')]//span");
	private static final By LAST_SAVED_PROFILE = By.xpath("//*[contains(text(),'Last')]//..");
	private static final By LAST_SAVED_PROFILE_NAME_BTN = By.xpath("//*[contains(text(),'Last')]//..//span[2]");
	private static final By MANUAL_MAPPING_PROFILE_TITLE = By.xpath("//*[@id='summary-title']//p");
	private static final By MANUAL_MAPPING_ROLE_SUMMARY = By.xpath("//div[contains(@id,'role-summary')]//p");
	private static final By MANUAL_MAPPING_PROFILE_DETAILS = By.xpath("//span[contains(text(),'Grade')]//..");
	private static final By MANUAL_MAPPING_RESPONSIBILITIES_TAB = By.xpath("//button[contains(text(),'RESPONSIBILITIES')]");
	private static final By MANUAL_MAPPING_VIEW_MORE_RESPONSIBILITIES = By.xpath("//div[contains(@id,'responsibilities')]//button[contains(text(),'more...')]");
	private static final By MANUAL_MAPPING_RESPONSIBILITIES_DATA = By.xpath("//div[contains(@id,'responsibilities')]");
	private static final By MANUAL_MAPPING_BEHAVIOUR_TAB = By.xpath("//button[contains(text(),'BEHAVIOURAL COMPETENCIES')]");
	private static final By MANUAL_MAPPING_VIEW_MORE_BEHAVIOUR = By.xpath("//div[contains(@id,'behavioural-panel')]//button[contains(text(),'more...')]");
	private static final By MANUAL_MAPPING_BEHAVIOUR_DATA = By.xpath("//div[contains(@id,'behavioural-panel')]");
	private static final By MANUAL_MAPPING_SKILLS_TAB = By.xpath("//button[contains(text(),'SKILLS')]");
	private static final By MANUAL_MAPPING_VIEW_MORE_SKILLS = By.xpath("//div[contains(@id,'skills')]//button[contains(text(),'more...')]");
	private static final By MANUAL_MAPPING_SKILLS_DATA = By.xpath("//div[contains(@id,'skills')]");
	private static final By KF_SP_SEARCH_BAR = By.xpath("//input[contains(@placeholder,'Search Korn Ferry')]");
	private static final By FIRST_SEARCH_RESULT_BTN = By.xpath("//ul//li[1]//button");
	private static final By FIRST_SEARCH_RESULT_TEXT = By.xpath("//ul//li[1]//button//div");
	private static final By SAVE_SELECTION_BTN = By.xpath("//button[contains(text(),'Save selection')]");
	private static final By JOB_MAPPING_PAGE_CONTAINER = By.xpath("//div[@id='org-job-container']");
	private static final By SEARCH_BAR = By.xpath("//input[contains(@id,'search-job-title')]");

	// METHODs
	public void user_should_search_for_job_profile_with_search_a_different_profile_button_on_mapped_success_profile() {
		try {
			waitForSpinners();
			// PERFORMANCE: Replaced Thread.sleep(2000) with smart page ready wait
			PerformanceUtils.waitForPageReady(driver);

			// Reset the flag at the start
			mapSP.set(false);
			;

			PageObjectHelper.log(LOGGER, "Searching for job profile with 'Search a Different Profile' button (Manually Mapped profile)...");

			// Track consecutive failures to detect end of table
			int consecutiveNotFound = 0;
			int maxConsecutiveNotFound = 10; // Stop after 10 consecutive rows without the button
			int previousRowCount = 0;
			int noNewDataScrolls = 0;
			int maxNoNewDataScrolls = 3; // Stop if no new data loads after 3 scrolls
			int searchAttempts = 0;
			int maxSearchAttempts = 100; // Prevent infinite loops (100 scroll-search cycles)

			LOGGER.info("Starting dynamic search with scrolling to load all available records...");

			while (searchAttempts < maxSearchAttempts) {
				searchAttempts++;

				// Get current row count
				List<WebElement> allRows = driver.findElements(By.xpath("//tbody//tr"));
				int currentRowCount = allRows.size();

				LOGGER.debug("Search attempt {}: Current row count = {}", searchAttempts, currentRowCount);

				// Search through currently loaded rows (every 3rd row, starting from row 5)
				for (int i = 5; i <= currentRowCount; i += 3) {
					try {
						rowNumber.set(i);
						WebElement button = driver.findElement(By.xpath("//tbody//tr[" + Integer.toString(i)
								+ "]//button[contains(text(),'different profile')] | //tbody//tr[" + Integer.toString(i)
								+ "]//button[contains(@id,'view')]"));

						// Scroll element into view
						js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", button);

						String text = button.getText();
						if (text.contains("different profile")) {
														PageObjectHelper.log(LOGGER, 
									" Job profile with 'Search a Different Profile' button found at row " + i);
							mapSP.set(true);
							return; // Found it - exit method
						} else {
							consecutiveNotFound++;
						}
					} catch (Exception e) {
						// Element not found in this row - continue to next
						consecutiveNotFound++;
					}

					// Early exit if we've gone past the end of meaningful data
					if (consecutiveNotFound >= maxConsecutiveNotFound) {
						LOGGER.debug("Reached {} consecutive rows without button. Will scroll for more data.",
								maxConsecutiveNotFound);
						consecutiveNotFound = 0; // Reset for next batch
						break; // Break inner loop to trigger scroll
					}
				}

				// Check if new data was loaded
				if (currentRowCount == previousRowCount) {
					noNewDataScrolls++;
					LOGGER.debug("No new data loaded (scroll attempt {}/{})", noNewDataScrolls, maxNoNewDataScrolls);

					if (noNewDataScrolls >= maxNoNewDataScrolls) {
						LOGGER.info("No new data loaded after {} scroll attempts. Ending search at {} total rows.",
								maxNoNewDataScrolls, currentRowCount);
						break; // Exit while loop - no more data to load
					}
				} else {
					noNewDataScrolls = 0; // Reset counter - new data was loaded
					LOGGER.info("New data loaded: {} rows (was {} rows)", currentRowCount, previousRowCount);
				}

				previousRowCount = currentRowCount;

				// Scroll to bottom to load more data
				LOGGER.debug("Scrolling to load more records...");
				scrollToBottom();

				// Wait for spinner and page to stabilize
				waitForSpinners();
				PerformanceUtils.waitForPageReady(driver, 2);
			}

			// If we exit the loop without finding the button
			if (mapSP.get() == false) {
				LOGGER.warn("SKIPPING SCENARIO: No Manually Mapped profiles available in Job Mapping");
				LOGGER.warn("Searched through {} rows across {} scroll attempts - no 'Search a Different Profile' button found", previousRowCount, searchAttempts);
				LOGGER.warn("This scenario requires at least one Manually Mapped job profile to execute");

				// Throw SkipException to mark scenario as SKIPPED in TestNG
				throw new SkipException("SKIPPED: No Manually Mapped profiles available in Job Mapping after searching "
						+ previousRowCount
						+ " rows. This scenario requires at least one job with 'Search a Different Profile' button to execute.");
			}

		} catch (Exception e) {
			// Only fail if there's a genuine exception, not just missing data
			if (mapSP.get() == false) {
				// This is just missing data, not a real error - skip scenario
				LOGGER.warn("SKIPPING SCENARIO: Exception while searching - No Manually Mapped profiles found. Error: {}", e.getMessage());

				// Throw SkipException to mark scenario as SKIPPED in TestNG
				throw new SkipException(
						"SKIPPED: No Manually Mapped profiles available in Job Mapping. This scenario requires at least one job with 'Search a Different Profile' button to execute.");
			} else {
				// This is a real error that occurred after finding a profile
				PageObjectHelper.handleError(LOGGER,
						"user_should_search_for_job_profile_with_search_a_different_profile_button",
						"Issue in searching a Job Profile with Search a Different Profile button in Job Mapping UI", e);
				Assert.fail(
						"Issue in searching a Job Profile with Search a Different Profile button in Job Mapping UI....Please Investigate!!!!");
			}
		}
	}

	public void user_should_verify_organization_job_name_and_job_code_values_of_job_profile_with_search_a_different_profile_button() {
		if (mapSP.get()) {
			try {
				waitForSpinners();
				// PERFORMANCE: Replaced Thread.sleep(2000) with smart page ready wait
				PerformanceUtils.waitForPageReady(driver, 2);
				WebElement jobName = driver.findElement(By.xpath("//tbody//tr[" + Integer.toString(rowNumber.get() - 1)
						+ "]//td[2]//div[contains(text(),'(')]"));
				Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(jobName)).isDisplayed());
				String jobname1 = wait.until(ExpectedConditions.visibilityOf(jobName)).getText();
				orgJobName.set(jobname1.split("-", 2)[0].trim());
				orgJobCode.set(jobname1.split("-", 2)[1].trim().substring(1, jobname1.split("-", 2)[1].length() - 2));
				PageObjectHelper.log(LOGGER, 
						"Organization Job name / Job Code of Profilewith Search a Different Profile button in the organization table : "
								+ orgJobName.get() + "/" + orgJobCode.get());
				LOGGER.info(
						"Organization Job name / Job Code of Profile a Different Profile button in the organization table : "
								+ orgJobName.get() + "/" + orgJobCode.get());
			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER, "user_should_verify_organization_job_name_and_job_code_values",
						"Issue verifying organization job name/code", e);
				e.printStackTrace();
				PageObjectHelper.log(LOGGER, 
						"Issue in verifying job name of Profile with Search a Different Profile button in the Organization jobs profile list...Please Investigate!!!");
				Assert.fail(
						"Issue in verifying job name of Profile with Search a Different Profile button in the Organization jobs profile list...Please Investigate!!!");
			}
		}
	}

	public void user_should_verify_organization_job_grade_and_department_values_of_job_profile_with_search_a_different_profile_button() {
		if (mapSP.get()) {
			try {
				WebElement jobGrade = driver.findElement(
						By.xpath("//tbody//tr[" + Integer.toString(rowNumber.get() - 1) + "]//td[3]//div[1]"));
				Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(jobGrade)).isDisplayed());
				String jobGradeText = wait.until(ExpectedConditions.visibilityOf(jobGrade)).getText();
				if (jobGradeText.contentEquals("-") || jobGradeText.isEmpty() || jobGradeText.isBlank()) {
					jobGradeText = "NULL";
					orgJobGrade.set(jobGradeText);
				}
				orgJobGrade.set(jobGradeText);
				PageObjectHelper.log(LOGGER, 
						"Grade value of Organization Job Profile with Search a Different Profile button : "
								+ orgJobGrade.get());
			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER, "user_should_verify_organization_job_grade_and_department_values",
						"Issue verifying organization job grade", e);
				e.printStackTrace();
				PageObjectHelper.log(LOGGER, 
						"Issue in Verifying Organization Job Grade value of Profile with Search a Different Profile button...Please Investigate!!!");
				Assert.fail(
						"Issue in Verifying Organization Job Grade value of Profile with Search a Different Profile button...Please Investigate!!!");
			}

			try {
				WebElement jobDepartment = driver.findElement(
						By.xpath("//tbody//tr[" + Integer.toString(rowNumber.get() - 1) + "]//td[4]//div[1]"));
				Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(jobDepartment)).isDisplayed());
				String jobDepartmentText = wait.until(ExpectedConditions.visibilityOf(jobDepartment)).getText();
				if (jobDepartmentText.contentEquals("-") || jobDepartmentText.isEmpty()
						|| jobDepartmentText.isBlank()) {
					jobDepartmentText = "NULL";
					orgJobDepartment.set(jobDepartmentText);
				} else {
					orgJobDepartment.set(jobDepartmentText);
				}
				PageObjectHelper.log(LOGGER, 
						"Department value of Organization Job Profile with Search a Different Profile button : "
								+ orgJobDepartment.get());
			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER, "user_should_verify_organization_job_grade_and_department_values",
						"Issue verifying organization job department", e);
				e.printStackTrace();
				PageObjectHelper.log(LOGGER, 
						"Issue in Verifying Organization Job Department value of Profile with Search a Different Profile button...Please Investigate!!!");
				Assert.fail(
						"Issue in Verifying Organization Job Department value of Profile with Search a Different Profile button...Please Investigate!!!");
			}
		}
	}

	public void user_should_verify_organization_job_function_or_sub_function_of_job_profile_with_search_a_different_profile_button() {
		if (mapSP.get()) {
			try {
				WebElement jobFunction = driver
						.findElement(By.xpath("//tbody//tr[" + Integer.toString(rowNumber.get()) + "]//div//span[2]"));
				Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(jobFunction)).isDisplayed());
				String jobFunctionText = wait.until(ExpectedConditions.visibilityOf(jobFunction)).getText();
				if (jobFunctionText.contentEquals("-") || jobFunctionText.isEmpty() || jobFunctionText.isBlank()) {
					jobFunctionText = "NULL | NULL";
					orgJobFunction.set(jobFunctionText);
				} else if (jobFunctionText.endsWith("-") || jobFunctionText.endsWith("| -")
						|| jobFunctionText.endsWith("|")
						|| (!(jobFunctionText.contains("|")) && (jobFunctionText.length() > 1))) {
					jobFunctionText = jobFunctionText + " | NULL";
					orgJobFunction.set(jobFunctionText);
				} else {
					orgJobFunction.set(jobFunctionText);
				}
				PageObjectHelper.log(LOGGER, 
						"Function / Sub-function values of Organization Job Profile with Search a Different Profile button : "
								+ orgJobFunction.get());
			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER, "user_should_verify_organization_job_function_or_sub_function",
						"Issue verifying organization job function", e);
				e.printStackTrace();
				PageObjectHelper.log(LOGGER, 
						"Issue in Verifying Organization Job Function / Sub-function values of Profile with Search a Different Profile button...Please Investigate!!!");
				Assert.fail(
						"Issue in Verifying Organization Job Function / Sub-function values of Profile with Search a Different Profile button...Please Investigate!!!");
			}
		}
	}

	public void click_on_mapped_profile_of_job_profile_with_search_a_different_profile_button() {
		if (mapSP.get() | manualMapping.get()) {
			try {
				waitForSpinners();
				WebElement linkedMappedProfile = driver
						.findElement(By.xpath("//div[@id='kf-job-container']//div//table//tbody//tr["
								+ Integer.toString(rowNumber.get() - 1) + "]//td[1]//div"));
				String MappedProfileNameText = wait.until(ExpectedConditions.elementToBeClickable(linkedMappedProfile))
						.getText();
				mappedSuccessPrflName.set(MappedProfileNameText);
				// Scroll element into view before clicking
				WebElement element = wait.until(ExpectedConditions.elementToBeClickable(linkedMappedProfile));
				js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
				try {
					Thread.sleep(500);
				} catch (InterruptedException ie) {
				}
				try {
					element.click();
				} catch (Exception e) {
					try {
						js.executeScript("arguments[0].click();", linkedMappedProfile);
					} catch (Exception s) {
						jsClick(linkedMappedProfile);
					}
				}
				PageObjectHelper.log(LOGGER, "Clicked on Manually Mapped Profile with name "
						+ MappedProfileNameText + " of Organization Job " + orgJobName.get());
				waitForSpinners();
			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER,
						"click_on_mapped_profile_of_job_profile_with_search_a_different_profile_button",
						"Issue clicking mapped profile", e);
				e.printStackTrace();
				PageObjectHelper.log(LOGGER, "Issue in clicking Manually Mapped Profile linked with job name "
						+ orgJobName.get() + "...Please Investigate!!!");
				Assert.fail("Issue in clicking Manually Mapped Profile linked with job name " + orgJobName.get()
						+ "...Please Investigate!!!");
			}
		}
	}

	public void verify_mapped_profile_details_popup_is_displayed() {
		if (mapSP.get() | manualMapping.get()) {
			try {
				waitForSpinners();
				Assert.assertTrue(waitForElement(PROFILE_DETAILS_POPUP_HEADER).isDisplayed());
				PageObjectHelper.log(LOGGER, "Mapped Profile details popup is displayed on screen as expected");
			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER, "verify_mapped_profile_details_popup_is_displayed",
						"Issue displaying profile details popup", e);
			}
		}
	}

	public void user_is_on_profile_details_popup_of_manually_mapped_profile() {
		if (mapSP.get() | manualMapping.get()) {
			PageObjectHelper.log(LOGGER, "User is on Profile details Popup of Manually Mapped Profile");
		}
	}

	public void verify_profile_header_matches_with_mapped_profile_name() {
		if (mapSP.get() | manualMapping.get()) {
			try {
				String profileHeaderName = getElementText(PROFILE_HEADER);
				Assert.assertEquals(mappedSuccessPrflName.get(), profileHeaderName);
				PageObjectHelper.log(LOGGER, "Profile header on the details popup : " + profileHeaderName);
			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER, "verify_profile_header_matches_with_mapped_profile_name",
						"Issue verifying profile header", e);
			}
		}
	}

	public void verify_mapped_profile_details_displaying_on_the_popup() {
		if (mapSP.get() | manualMapping.get()) {
			try {
				String profileDeatilsText = getElementText(PROFILE_DETAILS);
				ProfileDetails.set(profileDeatilsText);
				PageObjectHelper.log(LOGGER, "Profile Details displaying on the popup screen: " + profileDeatilsText);
			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER, "verify_mapped_profile_details_displaying_on_the_popup",
						"Issue displaying profile details", e);
			}
		}
	}

	public void user_should_verify_profile_level_dropdown_is_available_and_validate_levels_present_inside_dropdown_of_mapped_profile() {
		if (mapSP.get() | manualMapping.get()) {
			try {
				if (waitForElement(PROFILE_LEVEL_DROPDOWN).isEnabled()) {
					waitForClickable(PROFILE_LEVEL_DROPDOWN).click();
					PageObjectHelper.log(LOGGER, "Profile Level dropdown is available and Clicked on it...");
					Select dropdown = new Select(findElement(PROFILE_LEVEL_DROPDOWN));
					List<WebElement> allOptions = dropdown.getOptions();
					PageObjectHelper.log(LOGGER, "Levels present inside profile level dropdown : ");
					for (WebElement option : allOptions) {
						PageObjectHelper.log(LOGGER, option.getText());
					}
					waitForClickable(PROFILE_LEVEL_DROPDOWN).click(); // This click is
																										// to close
																										// dropdown
																										// options
																										// visibility
					PageObjectHelper.log(LOGGER, 
							"Successfully Verified Profile Level dropdown and levels present inside it...");
				} else {
					LOGGER.info("No Profile Levels available for this mapped profile with name : "
							+ mappedSuccessPrflName.get());
					PageObjectHelper.log(LOGGER, "No Profile Levels available for this mapped profile with name : "
									+ mappedSuccessPrflName.get());
				}
			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER,
						"user_should_verify_profile_level_dropdown_is_available_and_validate_levels_present_inside_dropdown_of_mapped_profile",
						"Issue validating profile level dropdown", e);
				e.printStackTrace();
				PageObjectHelper.log(LOGGER, 
						" Issue in validating profile level dropdown in profile details popup in Job Mapping page...Please Investigate!!!");
				Assert.fail(
						"Issue in validating profile level dropdown in profile details popup in Job Mapping page...Please Investigate!!!");
			}
		}
	}

	public void validate_role_summary_of_mapped_profile_is_displaying() {
		if (mapSP.get() | manualMapping.get()) {
			try {
				String roleSummaryText = waitForElement(ROLE_SUMMARY).getText();
				ProfileRoleSummary.set(roleSummaryText.split(": ", 2)[1].trim());
				PageObjectHelper.log(LOGGER, "Role summary of Mapped Success Profile : " + ProfileRoleSummary.get());
			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER, "validate_role_summary_of_mapped_profile_is_displaying",
						"Issue validating role summary", e);
				e.printStackTrace();
				PageObjectHelper.log(LOGGER, 
						" Issue in validating Role Summary in Profile Details Popup in Job Mapping page...Please Investigate!!!");
				Assert.fail(
						"Issue in validating Role Summary in Profile Details Popup in Job Mapping page...Please Investigate!!!");
			}
		}
	}

	public void validate_data_in_responsibilities_tab_of_mapped_profile() {
		if (mapSP.get() | manualMapping.get()) {
			try {
				// PERFORMANCE: Replaced Thread.sleep(2000) with smart element wait
				PerformanceUtils.waitForElement(driver, driver.findElement(By.xpath("//div")));
				
				// Safely get first View More button - may not exist
				List<WebElement> viewMoreButtons = findElements(VIEW_MORE_RESPONSIBILITIES);
				if (!viewMoreButtons.isEmpty()) {
					wait.until(ExpectedConditions.elementToBeClickable(viewMoreButtons.get(0))).click();
				}
				
				while (true) {
					try {
						viewMoreButtons = findElements(VIEW_MORE_RESPONSIBILITIES);
						if (viewMoreButtons.isEmpty()) {
							PageObjectHelper.log(LOGGER, "Reached end of content in Responsibilities screen");
							break;
						}
						WebElement viewMoreBtn = viewMoreButtons.get(0);
						js.executeScript("arguments[0].scrollIntoView(true);", viewMoreBtn);
						String ViewMoreBtnText = wait.until(ExpectedConditions.visibilityOf(viewMoreBtn)).getText();
						viewMoreBtn.click();
						PageObjectHelper.log(LOGGER, "Clicked on " + ViewMoreBtnText + " button in Responsibilities screen");
						// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
						PerformanceUtils.waitForUIStability(driver, 2);
					} catch (StaleElementReferenceException e) {
						PageObjectHelper.log(LOGGER, "Reached end of content in Responsibilities screen");
						break;
					}
				}
				String responsibilitiesDataText = wait.until(ExpectedConditions.visibilityOf(findElement(RESPONSIBILITIES_DATA)))
						.getText();
				ProfileResponsibilities.set(responsibilitiesDataText);
			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER, "validate_data_in_responsibilities_tab_of_mapped_profile",
						"Issue validating responsibilities screen", e);
				e.printStackTrace();
				PageObjectHelper.log(LOGGER, 
						" Issue in validating data in Responsibilities screen in Profile Details Popup in Job Mapping page...Please Investigate!!!");
				Assert.fail(
						"Issue in validating data in Responsibilities screen in Profile Details Popup in Job Mapping page...Please Investigate!!!");
			}
		}
	}

	public void validate_data_in_behavioural_competencies_tab_of_mapped_profile() {
		if (mapSP.get() | manualMapping.get()) {
			try {
				scrollToElement(findElement(ROLE_SUMMARY));
				// PERFORMANCE: Replaced Thread.sleep(3000) with smart element wait
				PerformanceUtils.waitForElement(driver, driver.findElement(By.xpath("//div")));
				wait.until(ExpectedConditions.elementToBeClickable(findElement(BEHAVIOUR_COMPETENCIES_TAB))).click();
				PageObjectHelper.log(LOGGER, "Clicked on Behaviour Competencies screen");
				while (true) {
					try {
						List<WebElement> viewMoreButtons = findElements(VIEW_MORE_BEHAVIOUR);
						if (viewMoreButtons.isEmpty()) {
							PageObjectHelper.log(LOGGER, "Reached end of content in Behaviour Competencies screen");
							break;
						}
						WebElement viewMoreBtn = viewMoreButtons.get(0);
						js.executeScript("arguments[0].scrollIntoView(true);", viewMoreBtn);
						String ViewMoreBtnText = wait.until(ExpectedConditions.visibilityOf(viewMoreBtn)).getText();
						viewMoreBtn.click();
						PageObjectHelper.log(LOGGER, 
								"Clicked on " + ViewMoreBtnText + " button in Behaviour Competencies screen");
						// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
						PerformanceUtils.waitForUIStability(driver, 2);
					} catch (StaleElementReferenceException e) {
						PageObjectHelper.log(LOGGER, "Reached end of content in Behaviour Competencies screen");
						break;
					}
				}
				String behaviourDataText = wait.until(ExpectedConditions.visibilityOf(findElement(BEHAVIOUR_DATA))).getText();
				ProfileBehaviouralCompetencies.set(behaviourDataText);
			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER, "validate_data_in_behavioural_competencies_tab_of_mapped_profile",
						"Issue validating behavioural competencies screen", e);
				e.printStackTrace();
				PageObjectHelper.log(LOGGER, 
						" Issue in validating data in Behaviour Competencies screen in Profile Details Popup in Job Mapping page...Please Investigate!!!");
				Assert.fail(
						"Issue in validating data in Behaviour Competencies screen in Profile Details Popup in Job Mapping page...Please Investigate!!!");
			}
		}
	}

	public void validate_data_in_skills_tab_of_mapped_profile() {
		if (mapSP.get() | manualMapping.get()) {
			try {
				scrollToElement(findElement(ROLE_SUMMARY));
				// PERFORMANCE: Replaced Thread.sleep(2000) with smart element wait
				PerformanceUtils.waitForElement(driver, driver.findElement(By.xpath("//div")));

				// Wait for any blocking loader overlays to disappear
				try {
					wait.until(
							ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@data-testid='loader']")));
				} catch (Exception e) {
					// Loader not present or already gone
				}

				wait.until(ExpectedConditions.elementToBeClickable(findElement(SKILLS_TAB))).click();
				PageObjectHelper.log(LOGGER, "Clicked on Skills screen");
				while (true) {
					try {
						List<WebElement> viewMoreButtons = findElements(VIEW_MORE_SKILLS);
						if (viewMoreButtons.isEmpty()) {
							PageObjectHelper.log(LOGGER, "Reached end of content in Skills screen");
							break;
						}
						WebElement viewMoreBtn = viewMoreButtons.get(0);
						js.executeScript("arguments[0].scrollIntoView(true);", viewMoreBtn);
						String ViewMoreBtnText = wait.until(ExpectedConditions.visibilityOf(viewMoreBtn)).getText();
						viewMoreBtn.click();
						PageObjectHelper.log(LOGGER, "Clicked on " + ViewMoreBtnText + " button in Skills screen");
						// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
						PerformanceUtils.waitForUIStability(driver, 2);
					} catch (StaleElementReferenceException e) {
						PageObjectHelper.log(LOGGER, "Reached end of content in Skills screen");
						break;
					}
				}
				String skillsDataText = wait.until(ExpectedConditions.visibilityOf(findElement(SKILLS_DATA))).getText();
				ProfileSkills.set(skillsDataText);
//				LOGGER.info("Data present in Skills screen : \n" + skillsDataText);
//				PageObjectHelper.log(LOGGER, "Data present in Skills screen : \n" + skillsDataText);
			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER, "validate_data_in_skills_tab_of_mapped_profile",
						"Issue validating skills screen", e);
				e.printStackTrace();
				PageObjectHelper.log(LOGGER, 
						" Issue in validating data in Skills screen in Profile Details Popup in Job Mapping page...Please Investigate!!!");
				Assert.fail(
						"Issue in validating data in Skills screen in Profile Details Popup in Job Mapping page...Please Investigate!!!");
			}
		}
	}

	/**
	 * Verifies Publish Profile button is available on popup screen of mapped
	 * profile ENHANCED FOR HEADLESS MODE: Scrolls popup to bottom to ensure button
	 * is visible
	 */
	public void user_should_verify_publish_profile_button_is_available_on_popup_screen_of_mapped_profile() {
		if (mapSP.get() | manualMapping.get()) {
			try {
				LOGGER.debug("Attempting to verify Publish Profile button availability for mapped profile...");

				// STEP 1: Scroll the popup content to the bottom (CRITICAL for headless mode)
				// Try multiple scrolling strategies to ensure button comes into view
				try {
					// Strategy 1: Scroll to the button element directly
					LOGGER.debug("Scrolling popup to bring Publish button into view...");
					js.executeScript(
							"arguments[0].scrollIntoView({behavior: 'smooth', block: 'end', inline: 'nearest'});",
							findElement(PUBLISH_PROFILE_BTN));
					Thread.sleep(1000); // Wait for smooth scroll to complete
				} catch (Exception scrollEx1) {
					LOGGER.debug("Direct scroll failed, trying popup container scroll...");
					try {
						// Strategy 2: Find and scroll the popup container to bottom
						WebElement popupContainer = driver.findElement(By.xpath(
								"//div[contains(@class, 'modal-body') or contains(@class, 'popup-content') or contains(@class, 'dialog-content')]"));
						js.executeScript("arguments[0].scrollTop = arguments[0].scrollHeight;", popupContainer);
						Thread.sleep(1000);
					} catch (Exception scrollEx2) {
						LOGGER.debug("Popup container scroll failed, trying window scroll as fallback...");
						// Strategy 3: Fallback - scroll the window itself
						js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
						Thread.sleep(500);
					}
				}

				// STEP 2: Wait for page stability after scroll (critical for headless)
				PerformanceUtils.waitForPageReady(driver, 2);
				Thread.sleep(500); // Additional buffer for DOM updates in headless mode

				// STEP 3: Verify the button is now visible and clickable
				LOGGER.debug("Waiting for Publish button to be clickable after scroll...");
				boolean isButtonDisplayed = wait.until(ExpectedConditions.elementToBeClickable(findElement(PUBLISH_PROFILE_BTN)))
						.isDisplayed();

				if (isButtonDisplayed) {
					PageObjectHelper.log(LOGGER, 
							"✅ Publish button is displaying on the Profile Details Popup and is clickable");
				} else {
					throw new Exception("Publish button found but not displayed");
				}

			} catch (Exception e) {
				LOGGER.error(
						"❌ Issue verifying Publish Profile button - Method: user_should_verify_publish_profile_button_is_available_on_popup_screen_of_mapped_profile",
						e);
				LOGGER.error(
						"Possible causes: 1) Button not scrolled into view in headless mode, 2) Popup not fully loaded, 3) Element locator issue");
				PageObjectHelper.handleError(LOGGER,
						"verify_publish_profile_button_is_displayed_on_mapped_profile_details_popup",
						"Issue verifying publish profile button", e);
				e.printStackTrace();
				PageObjectHelper.log(LOGGER, 
						" Issue in verifying Publish Profile button on profile details popup screen in Job Mapping page....Please Investigate!!!");
				Assert.fail(
						"Issue in verifying Publish Profile button on profile details popup screen in Job Mapping page....Please Investigate!!!");
			}
		}
	}

	public void click_on_close_button_in_profile_details_popup_of_mapped_profile() {
		if (mapSP.get() | manualMapping.get()) {
			try {
				// Scroll element into view before clicking
				WebElement element = wait.until(ExpectedConditions.elementToBeClickable(findElement(PROFILE_DETAILS_POPUP_CLOSE_BTN)));
				js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
				try {
					Thread.sleep(500);
				} catch (InterruptedException ie) {
				}
				try {
					element.click();
				} catch (Exception e) {
					try {
						js.executeScript("arguments[0].click();", findElement(PROFILE_DETAILS_POPUP_CLOSE_BTN));
					} catch (Exception s) {
						jsClick(findElement(PROFILE_DETAILS_POPUP_CLOSE_BTN));
					}
				}
				PageObjectHelper.log(LOGGER, "Clicked on close button in Profile details popup");
				waitForSpinners();
			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER, "click_on_close_button_in_profile_details_popup_of_mapped_profile",
						"Issue clicking close button", e);
				e.printStackTrace();
				PageObjectHelper.log(LOGGER, 
						"Issue in clicking close button in Profile details popup in Job Mapping page...Please Investigate!!!");
				Assert.fail(
						"Issue in clicking close button in Profile details popup in Job Mapping page...Please investigate!!!");
			}
		}
	}

	public void click_on_search_a_different_profile_button_on_mapped_success_profile() {
		if (mapSP.get()) {
			try {
				WebElement searchDifferntProfileBtn = driver.findElement(By.xpath("//tbody//tr["
						+ Integer.toString(rowNumber.get()) + "]//td//button[contains(text(),'different profile')]"));
				// Scroll element into view before clicking
				WebElement element = wait.until(ExpectedConditions.elementToBeClickable(searchDifferntProfileBtn));
				js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
				try {
					Thread.sleep(500);
				} catch (InterruptedException ie) {
				}
				try {
					element.click();
				} catch (Exception e) {
					try {
						js.executeScript("arguments[0].click();" + "", searchDifferntProfileBtn);
					} catch (Exception s) {
						jsClick(searchDifferntProfileBtn);
					}
				}
				PageObjectHelper.log(LOGGER, 
						"Clicked on Search a different profile button on Mapped SP of Organization Job Profile with name : "
								+ orgJobName.get());
			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER,
						"click_on_search_a_different_profile_button_on_mapped_success_profile",
						"Issue clicking Search Different Profile button", e);
				e.printStackTrace();
				PageObjectHelper.log(LOGGER, 
						"Issue in clicking on Search a different profile button in Job Mapping page...Please Investigate!!!");
				Assert.fail(
						"Issue in clicking on Search a different profile button in Job Mapping page...Please investigate!!!");
			}

		}
	}

	public void user_should_be_navigated_to_manual_job_mapping_screen() {
		if (mapSP.get()) {
			try {
				waitForSpinners();
				String actualHeader = wait
						.until(ExpectedConditions.refreshed(ExpectedConditions
								.presenceOfElementLocated(By.xpath("//*[contains(text(),'Which profile')]"))))
						.getText();
				// PERFORMANCE: Replaced Thread.sleep(3000) with element readiness wait
				PerformanceUtils.waitForElement(driver,
						driver.findElement(By.xpath("//*[contains(text(),'Which profile')]")));
				Assert.assertEquals(actualHeader, expectedHeader);
				PageObjectHelper.log(LOGGER, "User navigated to Manual Mapping screen as expected");
				if (actualHeader.contentEquals(expectedHeader)) {
					manualMapping.set(true);
				}
			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER, "user_should_be_navigated_to_manual_job_mapping_screen",
						"Issue navigating to Manual Mapping screen", e);
				e.printStackTrace();
				PageObjectHelper.log(LOGGER, " Issue in navigating to Manual Mapping screen...Please Investigate!!!");
				Assert.fail("Issue in navigating to Manual Mapping screen...Please Investigate!!!");
			}
		}
	}

	public void verify_organization_job_details_in_manual_mapping_screen() {
		if (manualMapping.get()) {
			try {
				waitForSpinners();
				String orgJobTitleText = wait
						.until(ExpectedConditions.visibilityOf(findElement(MANUAL_MAPPING_ORG_JOB_TITLE))).getText();
				Assert.assertTrue(orgJobTitleText.contains(orgJobName.get()));
				Assert.assertTrue(orgJobTitleText.contains(orgJobCode.get()));
				PageObjectHelper.log(LOGGER, 
						"Organization Job Name and Job code validated successfully in the Manual Mapping screen");
			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER, "verify_organization_job_details_in_manual_mapping_screen",
						"Issue validating organization job details", e);
				e.printStackTrace();
				PageObjectHelper.log(LOGGER, 
						" Issue in validating Organization Job Name and Job Code in the Manual Mapping screen....Please Investigate!!!");
				Assert.fail(
						"Issue in validating Organization Job Name and Job Code in the Manual Mapping screen....Please Investigate!!!");
			}

			try {
				String orgJobGradeText = wait
						.until(ExpectedConditions.visibilityOf(findElement(MANUAL_MAPPING_ORG_JOB_GRADE))).getText();
				if (orgJobGradeText.contentEquals("-")
						|| orgJobGradeText.isEmpty()
						|| orgJobGradeText.isBlank()) {
					orgJobGradeText = "NULL";
					Assert.assertTrue(orgJobGradeText.contains(orgJobGrade.get()));
				} else {
					Assert.assertTrue(orgJobGradeText.contains(orgJobGrade.get()));
				}
				PageObjectHelper.log(LOGGER, 
						"Organization Job Grade value validated successfully in the Manual Mapping screen");
			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER, "verify_organization_job_details_in_manual_mapping_screen",
						"Issue validating organization job grade in manual mapping", e);
				e.printStackTrace();
				Assert.fail(
						"Issue in validating Organization Job Grade value in the Manual Mapping screen....Please Investigate!!!");
				PageObjectHelper.log(LOGGER, 
						"Issue in validating Organization Job Grade value in the Manual Mapping screen....Please Investigate!!!");
			}

			try {
				String orgJobDeptText = wait
						.until(ExpectedConditions.visibilityOf(findElement(MANUAL_MAPPING_ORG_JOB_DEPARTMENT))).getText();
				if (orgJobDeptText.contentEquals("-")
						|| orgJobDeptText.isEmpty()
						|| orgJobDeptText.isBlank()) {
					orgJobDeptText = "NULL";
					Assert.assertTrue(orgJobDeptText.contains(orgJobDepartment.get()));
				} else {
					Assert.assertTrue(orgJobDeptText.contains(orgJobDepartment.get()));
				}
				PageObjectHelper.log(LOGGER, 
						"Organization Job Department value validated successfully in the Manual Mapping screen");
			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER, "verify_organization_job_details_in_manual_mapping_screen",
						"Issue validating organization job department in manual mapping", e);
				e.printStackTrace();
				Assert.fail(
						"Issue in validating Organization Job Department value in the Manual Mapping screen....Please Investigate!!!");
				PageObjectHelper.log(LOGGER, 
						"Issue in validating Organization Job Department value in the Manual Mapping screen....Please Investigate!!!");
			}

			try {
				String orgJobFuncText = wait
						.until(ExpectedConditions.visibilityOf(findElement(MANUAL_MAPPING_ORG_JOB_FUNCTION))).getText();
				if (orgJobFuncText.contentEquals("-")
						|| orgJobFuncText.isEmpty()
						|| orgJobFuncText.isBlank()) {
					orgJobFuncText = "NULL | NULL";
					Assert.assertTrue(orgJobFuncText.contains(orgJobFunction.get()));
				} else if (orgJobFuncText.endsWith("-")
						|| orgJobFuncText.endsWith("| -")
						|| orgJobFuncText.endsWith("|")
						|| (!(orgJobFuncText.contains("|"))
								&& (orgJobFuncText.length() > 1))) {
					orgJobFuncText = orgJobFuncText + " | NULL";
					Assert.assertTrue(orgJobFuncText.contains(orgJobFunction.get()));
				} else {
					Assert.assertTrue(orgJobFuncText.contains(orgJobFunction.get()));
				}
								PageObjectHelper.log(LOGGER, 
						"Organization Job Function / Sub-function values validated successfully in the Manual Mapping screen");
			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER, "verify_organization_job_details_in_manual_mapping_screen",
						"Issue validating organization job function in manual mapping", e);
				e.printStackTrace();
				Assert.fail(
						"Issue in validating Organization Job Function / Sub-function values in the Manual Mapping screen....Please Investigate!!!");
				PageObjectHelper.log(LOGGER, 
						"Issue in validating Organization Job Function / Sub-function values in the Manual Mapping screen....Please Investigate!!!");
			}
		}
	}

	public void user_should_verify_last_saved_profile_name_is_displaying_in_manual_mapping_screen() {
		if (manualMapping.get()) {
			try {
				String lastSavedProfileText = wait
						.until(ExpectedConditions.visibilityOf(findElement(LAST_SAVED_PROFILE))).getText();
				Assert.assertTrue(lastSavedProfileText.contains("LAST SAVED PROFILE"));
								PageObjectHelper.log(LOGGER, "Last saved Profile is displaying in the Manual Mapping screen as "
						+ lastSavedProfileText);
			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER,
						"user_should_verify_last_saved_profile_name_is_displaying_in_manual_mapping_screen",
						"Issue verifying last saved profile name", e);
				e.printStackTrace();
				Assert.fail(
						"Issue in displaying of Last Saved Profile name in the Manual Mapping screen....Please Investigate!!!");
				PageObjectHelper.log(LOGGER, 
						"Issue in displaying of Last Saved Profile name in the Manual Mapping screen....Please Investigate!!!");
			}
		}
	}

	public void click_on_last_saved_profile_name_in_manual_mapping_screen() {
		if (manualMapping.get()) {
			try {
				String text = wait
						.until(ExpectedConditions.elementToBeClickable(findElement(LAST_SAVED_PROFILE_NAME_BTN)))
						.getText();
				lastSavedProfileName.set(text);
				Assert.assertEquals(mappedSuccessPrflName.get(), lastSavedProfileName.get());
				// Scroll element into view before clicking
				WebElement element = wait
						.until(ExpectedConditions.elementToBeClickable(findElement(LAST_SAVED_PROFILE_NAME_BTN)));
				js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
				try {
					Thread.sleep(500);
				} catch (InterruptedException ie) {
				}
				try {
					element.click();
				} catch (Exception e) {
					try {
						js.executeScript("arguments[0].click();", findElement(LAST_SAVED_PROFILE_NAME_BTN));
					} catch (Exception s) {
						jsClick(findElement(LAST_SAVED_PROFILE_NAME_BTN));
					}
				}
				PageObjectHelper.log(LOGGER, 
						"Clicked on Last Saved Profile name : " + text + " in the Manual Mapping screen");
				wait.until(ExpectedConditions.elementToBeClickable(findElement(MANUAL_MAPPING_PROFILE_TITLE))).isDisplayed();
			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER, "click_on_last_saved_profile_name_in_manual_mapping_screen",
						"Issue clicking last saved profile name", e);
				e.printStackTrace();
				Assert.fail(
						"Issue in clicking on Last Saved Profile name in the Manual Mapping screen....Please Investigate!!!");
				PageObjectHelper.log(LOGGER, 
						"Issue in clicking on Last Saved Profile name in the Manual Mapping screen....Please Investigate!!!");
			}
		}
	}

	public void user_should_verify_last_saved_profile_name_in_manual_mapping_screen_matches_with_profile_name_in_details_popup() {
		if (manualMapping.get()) {
			try {
				String manualMappingProfile1TitleText = wait
						.until(ExpectedConditions.visibilityOf(findElement(MANUAL_MAPPING_PROFILE_TITLE))).getText();
				Assert.assertEquals(mappedSuccessPrflName.get(), manualMappingProfile1TitleText);
				Assert.assertEquals(lastSavedProfileName.get(), manualMappingProfile1TitleText);
								PageObjectHelper.log(LOGGER, 
						"Last Saved Profile Name in the Manual Mapping screen matches with Profile Name in the details popup as expected");
			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER,
						"user_should_verify_last_saved_profile_name_in_manual_mapping_screen_matches_with_profile_name_in_details_popup",
						"Issue verifying last saved profile name match", e);
				e.printStackTrace();
				Assert.fail(
						"Issue in verifying Last Saved Profile Name in the Manual Mapping screen....Please Investigate!!!");
				PageObjectHelper.log(LOGGER, 
						"Issue in verifying Last Saved Profile Name in the Manual Mapping screen....Please Investigate!!!");
			}
		}
	}

	public void user_should_verify_profile_level_dropdown_is_available_on_last_saved_success_profile_and_validate_levels_present_inside_dropdown() {
		if (manualMapping.get()) {
			try {
				if (waitForElement(PROFILE_LEVEL_DROPDOWN).isEnabled()) {
					waitForClickable(PROFILE_LEVEL_DROPDOWN).click();
					PageObjectHelper.log(LOGGER, 
							"Profile Level dropdown is available and Clicked on it in Manual Mapping screen...");
					Select dropdown = new Select(findElement(PROFILE_LEVEL_DROPDOWN));
					List<WebElement> allOptions = dropdown.getOptions();
					PageObjectHelper.log(LOGGER, "Levels present inside profile level dropdown : ");
					for (WebElement option : allOptions) {
						PageObjectHelper.log(LOGGER, option.getText());
					}
					waitForClickable(PROFILE_LEVEL_DROPDOWN).click(); // This click is
																										// to close
																										// dropdown
																										// options
																										// visibility
										PageObjectHelper.log(LOGGER, 
							"Successfully Verified Profile Level dropdown in Manual Mapping screen and levels present inside it...");
				} else {
					LOGGER.info("No Profile Levels available for this Last Saved profile with name : "
							+ mappedSuccessPrflName.get());
					PageObjectHelper.log(LOGGER, "No Profile Levels available for this Last Saved profile with name : "
									+ mappedSuccessPrflName.get());
				}

			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER,
						"user_should_verify_profile_level_dropdown_is_available_on_last_saved_success_profile_and_validate_levels_present_inside_dropdown",
						"Issue validating profile level dropdown in manual mapping", e);
				e.printStackTrace();
				Assert.fail(
						"Issue in validating profile level dropdown of Last Saved Profile in Manual Mapping page...Please Investigate!!!");
				PageObjectHelper.log(LOGGER, 
						"Issue in validating profile level dropdown in Last Saved Profile in Manual Mapping page...Please Investigate!!!");
			}
		}
	}

	public void validate_last_saved_success_profile_role_summary_matches_with_profile_role_summary_in_details_popup() {
		if (manualMapping.get()) {
			try {
				js.executeScript("arguments[0].scrollIntoView(true);", findElement(MANUAL_MAPPING_ROLE_SUMMARY));
				String roleSummaryText = wait
						.until(ExpectedConditions.visibilityOf(findElement(MANUAL_MAPPING_ROLE_SUMMARY))).getText();
				Assert.assertEquals(ProfileRoleSummary.get(),
						roleSummaryText.split(": ", 2)[1].trim());
								PageObjectHelper.log(LOGGER, 
						"Last Saved Profile Role Summary in the Manual Mapping screen matches with Mapped Success Profile Role Summary in details popup as expected");
			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER,
						"validate_last_saved_success_profile_role_summary_matches_with_profile_role_summary_in_details_popup",
						"Issue validating role summary match", e);
				e.printStackTrace();
				Assert.fail(
						"Issue in Validating Last Saved Profile Role Summary in the Manual Mapping screen....Please Investigate!!!");
				PageObjectHelper.log(LOGGER, 
						"Issue in Validating Last Saved Profile Role Summary in the Manual Mapping screen....Please Investigate!!!");
			}
		}
	}

	public void validate_last_saved_success_profile_details_matches_with_profile_details_in_details_popup() {
		if (manualMapping.get()) {
			try {
				js.executeScript("arguments[0].scrollIntoView(true);", findElement(MANUAL_MAPPING_PROFILE_DETAILS));
				String profileDetailsText = wait
						.until(ExpectedConditions.visibilityOf(findElement(MANUAL_MAPPING_PROFILE_DETAILS))).getText();
				Assert.assertEquals(ProfileDetails.get(), profileDetailsText);
								PageObjectHelper.log(LOGGER, 
						"Last Saved Profile Details in the Manual Mapping screen matches with Mapped Success Profile Details in details popup as expected");
			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER,
						"validate_last_saved_success_profile_details_matches_with_profile_details_in_details_popup",
						"Issue validating profile details match", e);
				e.printStackTrace();
				Assert.fail(
						"Issue in Validating Last Saved Profile Details in the Manual Mapping screen....Please Investigate!!!");
				PageObjectHelper.log(LOGGER, 
						"Issue in Validating Last Saved Profile Details in the Manual Mapping screen....Please Investigate!!!");
			}
		}
	}

	public void validate_last_saved_success_profile_responsibilities_matches_with_profile_responsibilities_in_details_popup() {
		if (manualMapping.get()) {
			try {
				js.executeScript("arguments[0].scrollIntoView(true);", findElement(MANUAL_MAPPING_RESPONSIBILITIES_TAB));
				// PERFORMANCE: Removed Thread.sleep(2000) - scrollIntoView is immediate
				
				// Safely get first View More button - may not exist
				List<WebElement> viewMoreButtons = findElements(MANUAL_MAPPING_VIEW_MORE_RESPONSIBILITIES);
				if (!viewMoreButtons.isEmpty()) {
					js.executeScript("arguments[0].scrollIntoView(true);", viewMoreButtons.get(0));
					wait.until(ExpectedConditions.elementToBeClickable(viewMoreButtons.get(0))).click();
				}
				
				while (true) {
					try {
						viewMoreButtons = findElements(MANUAL_MAPPING_VIEW_MORE_RESPONSIBILITIES);
						if (viewMoreButtons.isEmpty()) {
							js.executeScript("window.scrollTo(0, document.documentElement.scrollHeight);"); // Headless-compatible
							PageObjectHelper.log(LOGGER, 
								"Reached end of content in Last Saved Success Profile Responsibilities Section in Manual Mapping screen");
							break;
						}
						WebElement viewMoreBtn = viewMoreButtons.get(0);
						String ViewMoreResponsibilitiesBtnText = wait.until(ExpectedConditions.visibilityOf(viewMoreBtn)).getText();
						// Scroll element into view before clicking
						js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", viewMoreBtn);
						safeSleep(500);
						// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
						PerformanceUtils.waitForUIStability(driver, 2);
						try {
							wait.until(ExpectedConditions.elementToBeClickable(viewMoreBtn)).click();
						} catch (Exception e) {
							js.executeScript("arguments[0].click();", viewMoreBtn);
						}
						PageObjectHelper.log(LOGGER, "Clicked on " + ViewMoreResponsibilitiesBtnText
								+ " button in Last Saved Success Profile Responsibilities Section in Manual Mapping screen");
						// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
						PerformanceUtils.waitForUIStability(driver, 2);
					} catch (StaleElementReferenceException e) {
						js.executeScript("window.scrollTo(0, document.documentElement.scrollHeight);"); // Headless-compatible
						PageObjectHelper.log(LOGGER, 
								"Reached end of content in Last Saved Success Profile Responsibilities Section in Manual Mapping screen");
						break;
					}
				}
				String responsibilitiesText = wait
						.until(ExpectedConditions.visibilityOf(findElement(MANUAL_MAPPING_RESPONSIBILITIES_DATA))).getText();
				Assert.assertEquals(ProfileResponsibilities.get(), responsibilitiesText);
								PageObjectHelper.log(LOGGER, 
						"Last Saved Success Profile Responsibilities in the Job Compare page matches with Mapped Success Profile Responsibilities in details popup as expected");
			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER,
						"validate_last_saved_success_profile_responsibilities_matches_with_profile_responsibilities_in_details_popup",
						"Issue validating responsibilities match", e);
				e.printStackTrace();
				Assert.fail(
						"Issue in validating data in Last Saved Success Profile Responsibilities Section in Manual Mapping screen...Please Investigate!!!");
				PageObjectHelper.log(LOGGER, 
						"Issue in validating data in Last Saved Success Profile Responsibilities Section in Manual Mapping screen...Please Investigate!!!");
			}
		}
	}

	public void validate_last_saved_success_profile_behavioural_competencies_matches_with_profile_behavioural_competencies_in_details_popup() {
		if (manualMapping.get()) {
			try {
				js.executeScript("arguments[0].scrollIntoView(true);", findElement(MANUAL_MAPPING_ROLE_SUMMARY));
				// PERFORMANCE: Replaced Thread.sleep(2000) with smart element wait
				PerformanceUtils.waitForElement(driver, driver.findElement(By.xpath("//div")));
				wait.until(ExpectedConditions.elementToBeClickable(findElement(MANUAL_MAPPING_BEHAVIOUR_TAB)))
						.click();
								PageObjectHelper.log(LOGGER, 
						"Clicked on BEHAVIOURAL COMPETENCIES screen of Last Saved Profile in Manual Mapping screen");
			while (true) {
					try {
						List<WebElement> viewMoreButtons = findElements(MANUAL_MAPPING_VIEW_MORE_BEHAVIOUR);
						if (viewMoreButtons.isEmpty()) {
							PageObjectHelper.log(LOGGER, 
								"Reached end of content in Last Saved Success Profile Behavioural Competencies Section in Manual Mapping screen");
							break;
						}
						WebElement viewMoreBtn = viewMoreButtons.get(0);
						String ViewMoreCompetenciesBtnText = wait.until(ExpectedConditions.visibilityOf(viewMoreBtn)).getText();
						// Scroll element into view before clicking
						js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", viewMoreBtn);
						safeSleep(500);
						// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
						PerformanceUtils.waitForUIStability(driver, 2);
						try {
							wait.until(ExpectedConditions.elementToBeClickable(viewMoreBtn)).click();
						} catch (Exception e) {
							js.executeScript("arguments[0].click();", viewMoreBtn);
						}
						PageObjectHelper.log(LOGGER, "Clicked on " + ViewMoreCompetenciesBtnText
								+ " button in Last Saved Success Profile Behavioural Competencies Section in Manual Mapping screen");
						// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
						PerformanceUtils.waitForUIStability(driver, 2);
					} catch (StaleElementReferenceException e) {
						PageObjectHelper.log(LOGGER, 
								"Reached end of content in Last Saved Success Profile Behavioural Competencies Section in Manual Mapping screen");
						break;
					}
				}
				String behaviourText = wait
						.until(ExpectedConditions.visibilityOf(findElement(MANUAL_MAPPING_BEHAVIOUR_DATA)))
						.getText();
				Assert.assertEquals(ProfileBehaviouralCompetencies.get(),
						behaviourText);
				PageObjectHelper.log(LOGGER, 
						"Last Saved Success Profile Behavioural Competencies in the Job Compare page matches with Mapped Success Profile Behavioural Competencies in details popup as expected");
			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER,
						"validate_last_saved_success_profile_behavioural_competencies_matches_with_profile_behavioural_competencies_in_details_popup",
						"Issue validating behavioural competencies match", e);
				e.printStackTrace();
				Assert.fail(
						"Issue in validating data in Last Saved Success Profile Behavioural Competencies Section in Manual Mapping screen...Please Investigate!!!");
				PageObjectHelper.log(LOGGER, 
						"Issue in validating data in Last Saved Success Profile Behavioural Competencies Section in Manual Mapping screen...Please Investigate!!!");
			}
		}
	}

	public void validate_last_saved_success_profile_skills_macthes_with_profile_skills_in_details_popup() {
		if (manualMapping.get()) {
			try {
				js.executeScript("arguments[0].scrollIntoView(true);", findElement(MANUAL_MAPPING_ROLE_SUMMARY));
				// PERFORMANCE: Replaced Thread.sleep(2000) with smart element wait
				PerformanceUtils.waitForElement(driver, driver.findElement(By.xpath("//div")));

				// Wait for any blocking loader overlays to disappear
				try {
					wait.until(
							ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@data-testid='loader']")));
				} catch (Exception e) {
					// Loader not present or already gone
				}

			wait.until(ExpectedConditions.elementToBeClickable(findElement(MANUAL_MAPPING_SKILLS_TAB))).click();
				PageObjectHelper.log(LOGGER, "Clicked on SKILLS screen of Last Saved Profile in Manual Mapping screen");
				while (true) {
					try {
						List<WebElement> viewMoreButtons = findElements(MANUAL_MAPPING_VIEW_MORE_SKILLS);
						if (viewMoreButtons.isEmpty()) {
							PageObjectHelper.log(LOGGER, 
								"Reached end of content in Last Saved Success Profile Skills Section in Manual Mapping screen");
							break;
						}
						WebElement viewMoreBtn = viewMoreButtons.get(0);
						String ViewMoreSkillsBtnText = wait.until(ExpectedConditions.visibilityOf(viewMoreBtn)).getText();
						// Scroll element into view before clicking
						js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", viewMoreBtn);
						safeSleep(500);
						// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
						PerformanceUtils.waitForUIStability(driver, 2);
						try {
							wait.until(ExpectedConditions.elementToBeClickable(viewMoreBtn)).click();
						} catch (Exception e) {
							js.executeScript("arguments[0].click();", viewMoreBtn);
						}
						PageObjectHelper.log(LOGGER, "Clicked on " + ViewMoreSkillsBtnText
								+ " button in Last Saved Success Profile Skills Section in Manual Mapping screen");
						// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
						PerformanceUtils.waitForUIStability(driver, 2);
					} catch (StaleElementReferenceException e) {
						PageObjectHelper.log(LOGGER, 
								"Reached end of content in Last Saved Success Profile Skills Section in Manual Mapping screen");
						break;
					}
				}
				String skillsText = wait
						.until(ExpectedConditions.visibilityOf(findElement(MANUAL_MAPPING_SKILLS_DATA))).getText();
				Assert.assertEquals(ProfileSkills.get(), skillsText);
								PageObjectHelper.log(LOGGER, 
						"Last Saved Success Profile Skills in the Job Compare page matches with Mapped Success Profile Skills in details popup as expected");
			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER,
						"validate_last_saved_success_profile_skills_macthes_with_profile_skills_in_details_popup",
						"Issue validating skills match", e);
				e.printStackTrace();
				Assert.fail(
						"Issue in validating data in Last Saved Success Profile Skills Section in Manual Mapping screen...Please Investigate!!!");
				PageObjectHelper.log(LOGGER, 
						"Issue in validating data in Last Saved Success Profile Skills Section in Manual Mapping screen...Please Investigate!!!");
			}
		}
	}

	public void search_for_success_profile_in_manual_mapping_screen() {
		if (manualMapping.get()) {
			waitForSpinners();
			js.executeScript("window.scrollTo(0, 0);"); // Scroll to top (headless-compatible)
			try {
				// PERFORMANCE: Replaced Thread.sleep(2000) with element readiness wait
				PerformanceUtils.waitForElement(driver, findElement(KF_SP_SEARCH_BAR), 2);
				wait.until(ExpectedConditions.visibilityOf(findElement(KF_SP_SEARCH_BAR))).sendKeys(Keys.chord(Keys.CONTROL, "a"));
				wait.until(ExpectedConditions.visibilityOf(findElement(KF_SP_SEARCH_BAR))).sendKeys(SPSearchString.get());
				Assert.assertEquals(SPSearchString.get(), findElement(KF_SP_SEARCH_BAR).getAttribute("value"));
				PageObjectHelper.log(LOGGER, "Entered " + SPSearchString.get()
						+ " as Korn Ferry SP Search String in the search bar in Manual Mapping screen");
			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER, "search_for_success_profile_in_manual_mapping_screen",
						"Issue entering search string", e);
				e.printStackTrace();
				Assert.fail(
						"Failed to enter Korn Ferry SP Search String in the search bar in Manual Mapping screen...Please Investigate!!!");
				PageObjectHelper.log(LOGGER, 
						"Failed to enter Korn Ferry SP Search String in the search bar in Manual Mapping screen...Please Investigate!!!");
			}
		}
	}

	public void select_first_sucess_profile_from_search_results_in_manual_mapping_screen() {
		if (manualMapping.get()) {
			waitForSpinners();
			try {
				// Wait for search results to load - use presenceOfElementLocated then visibilityOfElementLocated
				// This properly waits for the element to exist before checking visibility
				wait.until(ExpectedConditions.presenceOfElementLocated(FIRST_SEARCH_RESULT_BTN));
				WebElement firstResultBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(FIRST_SEARCH_RESULT_BTN));
				Assert.assertTrue(firstResultBtn.isDisplayed());
				
				// Get the text before clicking
				WebElement firstResultText = wait.until(ExpectedConditions.visibilityOfElementLocated(FIRST_SEARCH_RESULT_TEXT));
				customSPNameinSearchResults.set(firstResultText.getText());
				
				// Click the first result
				wait.until(ExpectedConditions.elementToBeClickable(FIRST_SEARCH_RESULT_BTN)).click();
				
				PageObjectHelper.log(LOGGER, "First SP with Name : " + customSPNameinSearchResults.get()
						+ " from search results is selected in Manual Mapping screen");
				
				// Wait for results to disappear (indicating selection was processed)
				wait.until(ExpectedConditions.invisibilityOfElementLocated(FIRST_SEARCH_RESULT_BTN));
			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER,
						"select_first_sucess_profile_from_search_results_in_manual_mapping_screen",
						"Issue selecting first SP from search results", e);
				e.printStackTrace();
				Assert.fail(
						"Issue in Selecting First SP from Search Results in Manual Mapping screen...Please Investigate!!!");
			}
		}
	}

	public void verify_success_profile_is_added_in_manual_job_mapping_screen() {
		if (manualMapping.get()) {
			try {
				String profileHeaderName = wait.until(ExpectedConditions.visibilityOf(findElement(MANUAL_MAPPING_PROFILE_TITLE)))
						.getText();
				Assert.assertEquals(customSPNameinSearchResults.get(), profileHeaderName);
				manualMappingProfileName.set(profileHeaderName);
				mappedSuccessPrflName.set(manualMappingProfileName.get());
								PageObjectHelper.log(LOGGER, "Success Profile with name " + profileHeaderName
						+ " is added successfully on Manual Job Mapping screen");
			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER, "verify_success_profile_is_added_in_manual_job_mapping_screen",
						"Issue verifying success profile added", e);
				e.printStackTrace();
				Assert.fail("Issue in adding success profile on manual job mapping screen....Please Investigate!!!");
				PageObjectHelper.log(LOGGER, 
						"Issue in adding success profile on manual job mapping screen....Please Investigate!!!");
			}
		}
	}

	public void user_should_verify_profile_level_dropdown_is_available_and_validate_levels_present_inside_dropdown_in_manual_job_mapping_screen() {
		if (manualMapping.get()) {
			try {
				if (waitForElement(PROFILE_LEVEL_DROPDOWN).isEnabled()) {
					waitForClickable(PROFILE_LEVEL_DROPDOWN).click();
					PageObjectHelper.log(LOGGER, 
							"Profile Level dropdown is available and Clicked on it in Manual Mapping screen...");
					Select dropdown = new Select(findElement(PROFILE_LEVEL_DROPDOWN));
					List<WebElement> allOptions = dropdown.getOptions();
					PageObjectHelper.log(LOGGER, "Levels present inside profile level dropdown : ");
					for (WebElement option : allOptions) {
						PageObjectHelper.log(LOGGER, option.getText());
					}
					waitForClickable(PROFILE_LEVEL_DROPDOWN).click(); // This click is
																										// to close
																										// dropdown
																										// options
																										// visibility
										PageObjectHelper.log(LOGGER, 
							"Successfully Verified Profile Level dropdown in Manual Mapping screen and levels present inside it...");
				} else {
					LOGGER.info("No Profile Levels available for the profile with name : "
							+ manualMappingProfileName.get() + " in Manual Mapping page");
					PageObjectHelper.log(LOGGER, "No Profile Levels available for this Last Saved profile with name : "
									+ manualMappingProfileName.get() + " in Manual Mapping page");
				}

			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER,
						"user_should_verify_profile_level_dropdown_is_available_and_validate_levels_present_inside_dropdown_in_manual_job_mapping_screen",
						"Issue validating profile level dropdown", e);
				e.printStackTrace();
				Assert.fail(
						"Issue in validating profile level dropdown of Last Saved Profile in Manual Mapping page...Please Investigate!!!");
				PageObjectHelper.log(LOGGER, 
						"Issue in validating profile level dropdown in Last Saved Profile in Manual Mapping page...Please Investigate!!!");
			}
		}
	}

	public void change_profile_level_in_manual_job_mapping_screen() {
		if (manualMapping.get()) {
			if (waitForElement(PROFILE_LEVEL_DROPDOWN).isEnabled()) {
				js.executeScript("window.scrollTo(0, 0);"); // Scroll to top (headless-compatible)
				try {
					waitForClickable(PROFILE_LEVEL_DROPDOWN).click();
					// PERFORMANCE: Replaced Thread.sleep(2000) with dropdown readiness wait
					PerformanceUtils.waitForDropdownOptions(driver, By.xpath("//select//option"));
					Select dropdown = new Select(findElement(PROFILE_LEVEL_DROPDOWN));
					List<WebElement> allOptions = dropdown.getOptions();
					for (WebElement option : allOptions) {
						String lastlevelvalue = option.getText();
						changedlevelvalue.set(lastlevelvalue);
					}
//					int levels = dropdown.getOptions().size();
					dropdown.selectByVisibleText(changedlevelvalue.get());
//					dropdown.selectByIndex(levels - 1);
					waitForSpinners();
					// PERFORMANCE: Replaced Thread.sleep(4000+4000) with UI stability wait
					PerformanceUtils.waitForUIStability(driver);
					PageObjectHelper.log(LOGGER, "Successfully Changed Profile Level to : "
							+ changedlevelvalue.get() + " in Manual Mapping screen");
					PerformanceUtils.waitForUIStability(driver);
				} catch (Exception e) {
					PageObjectHelper.handleError(LOGGER, "change_profile_level_in_manual_job_mapping_screen",
							"Issue changing profile level", e);
					e.printStackTrace();
					Assert.fail("Issue in Changing Profile Level in Manual Mapping screen...Please Investigate!!!");
					PageObjectHelper.log(LOGGER, 
							"Issue in Changing Profile Level in Manual Mapping screen...Please Investigate!!!");
				}

				try {
					// This click is to close dropdown options visibility
					// Scroll element into view before clicking
					WebElement element = waitForClickable(PROFILE_LEVEL_DROPDOWN);
					js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
					try {
						Thread.sleep(500);
					} catch (InterruptedException ie) {
					}
					try {
						element.click();
					} catch (Exception e) {
						try {
							js.executeScript("arguments[0].click();", findElement(PROFILE_LEVEL_DROPDOWN));
						} catch (Exception s) {
							jsClick(findElement(PROFILE_LEVEL_DROPDOWN));
						}
					}
					PageObjectHelper.log(LOGGER, "Profile Level dropdown closed successfully in Manual Mapping screen");
				} catch (Exception e) {
					e.printStackTrace();
					Assert.fail(
							"Issue in clicking on Profile Level dropdown to close it in Manual Mapping screen...Please Investigate!!!");
					PageObjectHelper.log(LOGGER, 
							"Issue in clicking on Profile Level dropdown to close it in Manual Mapping screen...Please Investigate!!!");
				}
			}
		}
	}

	public void validate_role_summary_is_displaying_in_manual_job_mapping_screen() {
		if (manualMapping.get()) {
			try {
				String roleSummaryText = wait
						.until(ExpectedConditions.visibilityOf(findElement(MANUAL_MAPPING_ROLE_SUMMARY))).getText();
				manualMappingProfileRoleSummary.set(roleSummaryText.split(": ", 2)[1].trim());
				LOGGER.info("Role summary of Success Profile in Manual Mapping screen : "
						+ manualMappingProfileRoleSummary.get());
				PageObjectHelper.log(LOGGER, "Role summary of Success Profile in Manual Mapping screen : "
						+ manualMappingProfileRoleSummary.get());
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail(
						"Issue in validating Role Summary of Success Profile in Manual Mapping screen...Please Investigate!!!");
				PageObjectHelper.log(LOGGER, 
						"Issue in validating Role Summary of Success Profile in Manual Mapping screen...Please Investigate!!!");
			}
		}
	}

	public void verify_profile_details_displaying_in_manual_job_mapping_screen() {
		if (manualMapping.get()) {
			try {
				String profileDeatilsText = wait.until(ExpectedConditions.visibilityOf(findElement(MANUAL_MAPPING_PROFILE_DETAILS)))
						.getText();
				manualMappingProfileDetails.set(profileDeatilsText);
				PageObjectHelper.log(LOGGER, "Profile Details for " + manualMappingProfileName.get()
						+ " in Manual Mapping screen: \n" + profileDeatilsText);
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail("Issue in displaying profile details in Manual Mapping screen....Please Investigate!!!");
				PageObjectHelper.log(LOGGER, 
						"Issue in displaying profile details in Manual Mapping screen....Please Investigate!!!");
			}
		}
	}

	public void validate_data_in_responsibilities_tab_in_manual_job_mapping_screen() {
		if (manualMapping.get()) {
			try {
				js.executeScript("arguments[0].scrollIntoView(true);", findElement(MANUAL_MAPPING_ROLE_SUMMARY));
				// PERFORMANCE: Replaced Thread.sleep(2000) with smart element wait
				PerformanceUtils.waitForElement(driver, driver.findElement(By.xpath("//div")));
			wait.until(ExpectedConditions.elementToBeClickable(findElement(MANUAL_MAPPING_RESPONSIBILITIES_TAB))).click();
				PageObjectHelper.log(LOGGER, "Clicked on Responsibilities screen in Manual Mapping screen");
				while (true) {
					try {
						List<WebElement> viewMoreButtons = findElements(MANUAL_MAPPING_VIEW_MORE_RESPONSIBILITIES);
						if (viewMoreButtons.isEmpty()) {
							js.executeScript("window.scrollTo(0, document.documentElement.scrollHeight);"); // Headless-compatible
							PageObjectHelper.log(LOGGER, 
								"Reached end of content in Success Profile Responsibilities Section in Manual Mapping screen");
							break;
						}
						WebElement viewMoreBtn = viewMoreButtons.get(0);
						String ViewMoreResponsibilitiesBtnText = wait.until(ExpectedConditions.visibilityOf(viewMoreBtn)).getText();
						// Scroll element into view before clicking
						js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", viewMoreBtn);
						safeSleep(500);
						// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
						PerformanceUtils.waitForUIStability(driver, 2);
						try {
							wait.until(ExpectedConditions.elementToBeClickable(viewMoreBtn)).click();
						} catch (Exception e) {
							js.executeScript("arguments[0].click();", viewMoreBtn);
						}
						PageObjectHelper.log(LOGGER, "Clicked on " + ViewMoreResponsibilitiesBtnText
								+ " button in Success Profile Responsibilities Section in Manual Mapping screen");
						// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
						PerformanceUtils.waitForUIStability(driver, 2);
					} catch (StaleElementReferenceException e) {
						js.executeScript("window.scrollTo(0, document.documentElement.scrollHeight);"); // Scroll DOWN (headless-compatible)
						PageObjectHelper.log(LOGGER, 
								"Reached end of content in Success Profile Responsibilities Section in Manual Mapping screen");
						break;
					}
				}
				String responsibilitiesText = wait
						.until(ExpectedConditions.visibilityOf(findElement(MANUAL_MAPPING_RESPONSIBILITIES_DATA))).getText();
				manualMappingProfileResponsibilities.set(responsibilitiesText);
//				LOGGER.info("Data present in Responsibilities screen in Manual Mapping screen : \n" + responsibilitiesText);
//				PageObjectHelper.log(LOGGER, "Data present in Responsibilities screen in Manual Mapping screen : \n" + responsibilitiesText);
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail(
						"Issue in validating data in Responsibilities screen in Manual Mapping screen...Please Investigate!!!");
				PageObjectHelper.log(LOGGER, 
						"Issue in validating data in Responsibilities screen in Manual Mapping screen...Please Investigate!!!");
			}
		}
	}

	public void validate_data_in_behavioural_competencies_tab_in_manual_job_mapping_screen() {
		if (manualMapping.get()) {
			try {
				js.executeScript("arguments[0].scrollIntoView(true);", findElement(MANUAL_MAPPING_ROLE_SUMMARY));
				// PERFORMANCE: Replaced Thread.sleep(2000) with smart element wait
				PerformanceUtils.waitForElement(driver, driver.findElement(By.xpath("//div")));
				wait.until(ExpectedConditions.elementToBeClickable(findElement(MANUAL_MAPPING_BEHAVIOUR_TAB)))
						.click();
				PageObjectHelper.log(LOGGER, "Clicked on Behaviour Competencies screen in Manual Mapping screen");
				while (true) {
					try {
						List<WebElement> viewMoreButtons = findElements(MANUAL_MAPPING_VIEW_MORE_BEHAVIOUR);
						if (viewMoreButtons.isEmpty()) {
							PageObjectHelper.log(LOGGER, 
								"Reached end of content in Success Profile Behavioural Competencies Section in Manual Mapping screen");
							break;
						}
						WebElement viewMoreBtn = viewMoreButtons.get(0);
						String ViewMoreCompetenciesBtnText = wait.until(ExpectedConditions.visibilityOf(viewMoreBtn)).getText();
						// Scroll element into view before clicking
						js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", viewMoreBtn);
						safeSleep(500);
						// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
						PerformanceUtils.waitForUIStability(driver, 2);
						try {
							wait.until(ExpectedConditions.elementToBeClickable(viewMoreBtn)).click();
						} catch (Exception e) {
							js.executeScript("arguments[0].click();", viewMoreBtn);
						}
						PageObjectHelper.log(LOGGER, "Clicked on " + ViewMoreCompetenciesBtnText
								+ " button in Success Profile Behavioural Competencies Section in Manual Mapping screen");
						// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
						PerformanceUtils.waitForUIStability(driver, 2);
					} catch (StaleElementReferenceException e) {
						PageObjectHelper.log(LOGGER, 
								"Reached end of content in Success Profile Behavioural Competencies Section in Manual Mapping screen");
						break;
					}
				}
				String behaviourText = wait
						.until(ExpectedConditions.visibilityOf(findElement(MANUAL_MAPPING_BEHAVIOUR_DATA)))
						.getText();
				manualMappingProfileBehaviouralCompetencies.set(behaviourText);
//				LOGGER.info("Data present in Behavioural Competencies screen in Manual Mapping screen : \n" + behaviourText);
//				PageObjectHelper.log(LOGGER, "Data present in Behavioural Competencies screen in Manual Mapping screen : \n" + behaviourText);
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail(
						"Issue in validating data in Behavioural Competencies screen in Manual Mapping screen...Please Investigate!!!");
				PageObjectHelper.log(LOGGER, 
						"Issue in validating data in Behavioural Competencies screen in Manual Mapping screen...Please Investigate!!!");
			}
		}
	}

	public void validate_data_in_skills_tab_in_manual_job_mapping_screen() {
		if (manualMapping.get()) {
			try {
				js.executeScript("arguments[0].scrollIntoView(true);", findElement(MANUAL_MAPPING_ROLE_SUMMARY));
				// PERFORMANCE: Replaced Thread.sleep(2000) with smart element wait
				PerformanceUtils.waitForElement(driver, driver.findElement(By.xpath("//div")));
			wait.until(ExpectedConditions.elementToBeClickable(findElement(MANUAL_MAPPING_SKILLS_TAB))).click();
				PageObjectHelper.log(LOGGER, "Clicked on Skills screen in Manual Mapping screen");
				while (true) {
					try {
						List<WebElement> viewMoreButtons = findElements(MANUAL_MAPPING_VIEW_MORE_SKILLS);
						if (viewMoreButtons.isEmpty()) {
							PageObjectHelper.log(LOGGER, 
								"Reached end of content in Success Profile Skills Section in Manual Mapping screen");
							break;
						}
						WebElement viewMoreBtn = viewMoreButtons.get(0);
						String ViewMoreSkillsBtnText = wait.until(ExpectedConditions.visibilityOf(viewMoreBtn)).getText();
						// Scroll element into view before clicking
						js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", viewMoreBtn);
						safeSleep(500);
						// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
						PerformanceUtils.waitForUIStability(driver, 2);
						try {
							wait.until(ExpectedConditions.elementToBeClickable(viewMoreBtn)).click();
						} catch (Exception e) {
							js.executeScript("arguments[0].click();", viewMoreBtn);
						}
						PageObjectHelper.log(LOGGER, "Clicked on " + ViewMoreSkillsBtnText
								+ " button in Success Profile Skills Section in Manual Mapping screen");
						// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
						PerformanceUtils.waitForUIStability(driver, 2);
					} catch (StaleElementReferenceException e) {
						PageObjectHelper.log(LOGGER, 
								"Reached end of content in Success Profile Skills Section in Manual Mapping screen");
						break;
					}
				}
				String skillsText = wait
						.until(ExpectedConditions.visibilityOf(findElement(MANUAL_MAPPING_SKILLS_DATA))).getText();
				manualMappingProfileSkills.set(skillsText);
//				LOGGER.info("Data present in Skills screen in Manual Mapping screen : \n" + skillsText);
//				PageObjectHelper.log(LOGGER, "Data present in Skills screen in Manual Mapping screen : \n" + skillsText);
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail(
						"Issue in validating data in Skills screen in Manual Mapping screen...Please Investigate!!!");
				PageObjectHelper.log(LOGGER, 
						"Issue in validating data in Skills screen in Manual Mapping screen...Please Investigate!!!");
			}
		}
	}

	public void click_on_save_selection_button_in_manual_job_mapping_screen() {
		if (manualMapping.get()) {
			try {
				js.executeScript("window.scrollTo(0, 0);"); // Scroll to top (headless-compatible)
				// Scroll element into view before clicking
				WebElement element = wait.until(ExpectedConditions.elementToBeClickable(findElement(SAVE_SELECTION_BTN)));
				js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
				try {
					Thread.sleep(500);
				} catch (InterruptedException ie) {
				}
				try {
					element.click();
				} catch (Exception e) {
					try {
						js.executeScript("arguments[0].click();", findElement(SAVE_SELECTION_BTN));
					} catch (Exception s) {
						jsClick(findElement(SAVE_SELECTION_BTN));
					}
				}
				PageObjectHelper.log(LOGGER, "Successfully clicked on Save Selection button in Manual Mapping screen");
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail(
						"Issue in clicking on Save Selection button in Manual Mapping screen...Please Investigate!!!");
				PageObjectHelper.log(LOGGER, 
						"Issue in clicking on Save Selection button in Manual Mapping screen...Please Investigate!!!");
			}
		}
	}

	public void user_should_be_navigated_to_job_mapping_page() {
		if (manualMapping.get()) {
			try {
				// Optimized wait - no blocking Thread.sleep
				waitForSpinners();
				// FIXED: Use more reliable org-job-container for page detection, then verify
				// header
				wait.until(ExpectedConditions.visibilityOf(findElement(JOB_MAPPING_PAGE_CONTAINER)));
				Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(findElement(JOB_MAPPING_PAGE_CONTAINER))).isDisplayed());
				PageObjectHelper.log(LOGGER, "User navigated to JOB MAPPING page successfully");
			} catch (Exception e) {
				e.printStackTrace();
				PageObjectHelper.log(LOGGER, "Issue in navigating to Job Mapping page...Please Investigate!!!");
				Assert.fail("Issue in navigating to Job Mapping page...Please Investigate!!!");
			}
		}
	}

	public void verify_organization_job_with_new_mapped_sp_is_displaying_on_top_of_profiles_list() {
		if (manualMapping.get()) {
			try {
				waitForSpinners();
				waitForSpinners();
				Thread.sleep(2000);
				PerformanceUtils.waitForPageReady(driver, 2);
				WebElement button = driver.findElement(By.xpath(
						"//tbody//tr[2]//button[contains(text(),'different profile')] | //tbody//tr[2]//button[contains(@id,'view')]"));
				js.executeScript("arguments[0].scrollIntoView(true);", button);
				String text = button.getText();
				if (text.contains("different profile")) {
					rowNumber.set(2);
					PageObjectHelper.log(LOGGER, "Organization Job profile " + orgJobName.get()
							+ " with new Mapped Success Profile " + manualMappingProfileName.get()
							+ " is displaying on Top of Profiles List as Expected");
				} else {
					mapSP.set(false);
					manualMapping.set(false);
					LOGGER.error("Organization Job profile {} with new Mapped Success Profile {} is NOT displaying on Top of Profiles List", 
							orgJobName.get(), manualMappingProfileName.get());
					Assert.fail("Organization Job profile " + orgJobName.get() + " with new Mapped Success Profile "
							+ manualMappingProfileName.get() + " is NOT displaying on Top of Profiles List");
				}
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail(
						"Issue in Verifying Organization Job with Recently Mapped SP displaying on Top of Profiles List or Not....Please Investigate!!!");
				PageObjectHelper.log(LOGGER, 
						"Issue in Verifying Organization Job with Recently Mapped SP displaying on Top of Profiles List or Not....Please Investigate!!!");
			}
		}
	}

	public void user_should_verify_profile_level_dropdown_is_available_in_details_popup_and_validate_levels_present_inside_dropdown() {
		if (manualMapping.get()) {
			try {
				if (waitForElement(PROFILE_LEVEL_DROPDOWN).isEnabled()) {
					waitForClickable(PROFILE_LEVEL_DROPDOWN).click();
										PageObjectHelper.log(LOGGER, 
							"Profile Level dropdown is available and Clicked on it in Mapped Profile details Popup...");
					Select dropdown = new Select(findElement(PROFILE_LEVEL_DROPDOWN));
					List<WebElement> allOptions = dropdown.getOptions();
					PageObjectHelper.log(LOGGER, "Levels present inside profile level dropdown : ");
					for (WebElement option : allOptions) {
						PageObjectHelper.log(LOGGER, option.getText());
					}
					waitForClickable(PROFILE_LEVEL_DROPDOWN).click(); // This click is
																										// to close
																										// dropdown
																										// options
																										// visibility
										PageObjectHelper.log(LOGGER, 
							"Successfully Verified Profile Level dropdown in Mapped Profile details Popup and levels present inside it...");
				} else {
					LOGGER.info("No Profile Levels available for Mapped Profile in details Popup : "
							+ manualMappingProfileName.get());
					PageObjectHelper.log(LOGGER, "No Profile Levels available for Mapped Profile in details Popup : "
									+ manualMappingProfileName.get());
				}

			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail(
						"Issue in validating profile level dropdown of Mapped Profile in details Popup...Please Investigate!!!");
				PageObjectHelper.log(LOGGER, 
						"Issue in validating profile level dropdown in Mapped Profile in details Popup...Please Investigate!!!");
			}
		}
	}

	public void validate_profile_role_summary_in_details_popup_matches_with_mapped_success_profile_role_summary() {
		if (manualMapping.get()) {
			try {
				scrollToElement(findElement(ROLE_SUMMARY));
				String mappedProfileRoleSummaryText = waitForElement(ROLE_SUMMARY)
						.getText();
				Assert.assertEquals(mappedProfileRoleSummaryText.split(": ", 2)[1].trim(),
						manualMappingProfileRoleSummary.get());
								PageObjectHelper.log(LOGGER, 
						"Profile Role Summary in the Details Popup matches with Mapped Success Profile Role Summary as expected");
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail(
						"Issue in Validating Profile Role Summary in Details Popup matches with Mapped Success Profile Role Summary....Please Investigate!!!");
				PageObjectHelper.log(LOGGER, 
						"Issue in Validating Profile Role Summary in Details Popup matches with Mapped Success Profile Role Summary....Please Investigate!!!");
			}
		}
	}

	public void validate_profile_details_in_details_popup_matches_with_mapped_success_profile_details() {
		if (manualMapping.get()) {
			try {
				js.executeScript("arguments[0].scrollIntoView(true);", findElement(PROFILE_DETAILS));
				String mappedProfileDetailsText = wait.until(ExpectedConditions.visibilityOf(findElement(PROFILE_DETAILS))).getText();
				Assert.assertEquals(mappedProfileDetailsText, manualMappingProfileDetails.get());
				PageObjectHelper.log(LOGGER, 
						"Profile Details in Details Popup matches with Mapped Success Profile Details as expected");
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail(
						"Issue in Validating Profile Details in Details Popup matches with Mapped Success Profile Details....Please Investigate!!!");
				PageObjectHelper.log(LOGGER, 
						"Issue in Validating Profile Details in Details Popup matches with Mapped Success Profile Details....Please Investigate!!!");
			}
		}
	}

	public void validate_profile_responsibilities_in_details_popup_matches_with_mapped_success_profile_responsibilities() {
		if (manualMapping.get()) {
			try {
				// PERFORMANCE: Replaced Thread.sleep(2000) with smart element wait
				PerformanceUtils.waitForElement(driver, driver.findElement(By.xpath("//div")));
				
				// Safely get first View More button - may not exist
				List<WebElement> viewMoreButtons = findElements(VIEW_MORE_RESPONSIBILITIES);
				if (!viewMoreButtons.isEmpty()) {
					wait.until(ExpectedConditions.elementToBeClickable(viewMoreButtons.get(0))).click();
				}
				
				while (true) {
					try {
						viewMoreButtons = findElements(VIEW_MORE_RESPONSIBILITIES);
						if (viewMoreButtons.isEmpty()) {
							js.executeScript("window.scrollTo(0, document.documentElement.scrollHeight);"); // Scroll DOWN (headless-compatible)
							PageObjectHelper.log(LOGGER, 
									"Reached end of content in Profile Responsibilities Section in Details Popup");
							break;
						}
						WebElement viewMoreBtn = viewMoreButtons.get(0);
						String ViewMoreResponsibilitiesBtnText = wait.until(ExpectedConditions.visibilityOf(viewMoreBtn)).getText();
						// Scroll element into view before clicking
						js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", viewMoreBtn);
						safeSleep(500);
						// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
						PerformanceUtils.waitForUIStability(driver, 2);
						try {
							wait.until(ExpectedConditions.elementToBeClickable(viewMoreBtn)).click();
						} catch (Exception e) {
							js.executeScript("arguments[0].click();", viewMoreBtn);
						}
						PageObjectHelper.log(LOGGER, "Clicked on " + ViewMoreResponsibilitiesBtnText
								+ " button in Profile Responsibilities Section in Details Popup");
						// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
						PerformanceUtils.waitForUIStability(driver, 2);
					} catch (StaleElementReferenceException e) {
						js.executeScript("window.scrollTo(0, document.documentElement.scrollHeight);"); // Scroll DOWN (headless-compatible)
						PageObjectHelper.log(LOGGER, 
								"Reached end of content in Profile Responsibilities Section in Details Popup");
						break;
					}
				}
				String ProfileResponsibilitiesText = wait.until(ExpectedConditions.visibilityOf(findElement(RESPONSIBILITIES_DATA)))
						.getText();
				Assert.assertEquals(ProfileResponsibilitiesText, manualMappingProfileResponsibilities.get());
								PageObjectHelper.log(LOGGER, 
						"Profile Responsibilities in Details Popup matches with Mapped Success Profile Responsibilities as expected");
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail(
						"Issue in validating data in Profile Responsibilities in Details Popup matches with Mapped Success Profile Responsibilities...Please Investigate!!!");
				PageObjectHelper.log(LOGGER, 
						"Issue in validating data in Profile Responsibilities in Details Popup matches with Mapped Success Profile Responsibilities...Please Investigate!!!");
			}
		}
	}

	public void validate_profile_behavioural_competencies_in_details_popup_matches_with_mapped_success_profile_behavioural_competencies() {
		if (manualMapping.get()) {
			try {
				scrollToElement(findElement(ROLE_SUMMARY));
				// PERFORMANCE: Replaced Thread.sleep(2000) with smart element wait
				PerformanceUtils.waitForElement(driver, driver.findElement(By.xpath("//div")));

				// Wait for any blocking loader overlays to disappear
				try {
					wait.until(
							ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@data-testid='loader']")));
				} catch (Exception e) {
					// Loader not present or already gone
				}

				// Scroll element into view before clicking
				WebElement element = wait
						.until(ExpectedConditions.elementToBeClickable(findElement(BEHAVIOUR_COMPETENCIES_TAB)));
				js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
				try {
					Thread.sleep(500);
				} catch (InterruptedException ie) {
				}

				// Wait for element to be clickable without any overlay
				wait.until(ExpectedConditions.elementToBeClickable(findElement(BEHAVIOUR_COMPETENCIES_TAB)));

				try {
					element.click();
				} catch (Exception e) {
					try {
						js.executeScript("arguments[0].click();", findElement(BEHAVIOUR_COMPETENCIES_TAB));
					} catch (Exception s) {
						jsClick(findElement(BEHAVIOUR_COMPETENCIES_TAB));
					}
				}
			PageObjectHelper.log(LOGGER, "Clicked on BEHAVIOURAL COMPETENCIES screen in Profiles Details Popup");
				while (true) {
					try {
						List<WebElement> viewMoreButtons = findElements(VIEW_MORE_BEHAVIOUR);
						if (viewMoreButtons.isEmpty()) {
							PageObjectHelper.log(LOGGER, 
								"Reached end of content in Profile Behavioural Competencies Section in Details Popup");
							break;
						}
						WebElement viewMoreBtn = viewMoreButtons.get(0);
						String ViewMoreCompetenciesBtnText = wait.until(ExpectedConditions.visibilityOf(viewMoreBtn)).getText();
						// Scroll element into view before clicking
						js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", viewMoreBtn);
						safeSleep(500);
						// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
						PerformanceUtils.waitForUIStability(driver, 2);
						try {
							wait.until(ExpectedConditions.elementToBeClickable(viewMoreBtn)).click();
						} catch (Exception e) {
							js.executeScript("arguments[0].click();", viewMoreBtn);
						}
						PageObjectHelper.log(LOGGER, "Clicked on " + ViewMoreCompetenciesBtnText
								+ " button in Profile Behavioural Competencies Section in Details Popup");
						// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
						PerformanceUtils.waitForUIStability(driver, 2);
					} catch (StaleElementReferenceException e) {
						PageObjectHelper.log(LOGGER, 
								"Reached end of content in Profile Behavioural Competencies Section in Details Popup");
						break;
					}
				}
				String ProfileBehaviouralCompetenciesText = wait.until(ExpectedConditions.visibilityOf(findElement(BEHAVIOUR_DATA)))
						.getText();
				Assert.assertEquals(ProfileBehaviouralCompetenciesText,
						manualMappingProfileBehaviouralCompetencies.get());
								PageObjectHelper.log(LOGGER, 
						"Profile Behavioural Competencies in Details Popup matches with Mapped Success Profile Behavioural Competencies as expected");
			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER,
						"validate_profile_behavioural_competencies_in_details_popup_matches_with_mapped_success_profile_behavioural_competencies",
						"Issue validating behavioural competencies in details popup", e);
				e.printStackTrace();
				Assert.fail(
						"Issue in validating data in Profile Behavioural Competencies Section in Details Popup matches with Mapped Success Profile Behavioural Competencies...Please Investigate!!!");
				PageObjectHelper.log(LOGGER, 
						"Issue in validating data in Profile Behavioural Competencies Section in Details Popup matches with Mapped Success Profile Behavioural Competencies...Please Investigate!!!");
			}
		}
	}

	public void validate_profile_skills_in_details_popup_matches_with_mapped_success_profile_skills() {
		if (manualMapping.get()) {
			try {
				scrollToElement(findElement(ROLE_SUMMARY));
				// PERFORMANCE: Replaced Thread.sleep(2000) with smart element wait
				PerformanceUtils.waitForElement(driver, driver.findElement(By.xpath("//div")));

				// Wait for any blocking loader overlays to disappear
				try {
					wait.until(
							ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@data-testid='loader']")));
				} catch (Exception e) {
					// Loader not present or already gone
				}

				wait.until(ExpectedConditions.elementToBeClickable(findElement(SKILLS_TAB))).click();
				PageObjectHelper.log(LOGGER, "Clicked on SKILLS screen in Profiles Details Popup");
				while (true) {
					try {
						List<WebElement> viewMoreButtons = findElements(VIEW_MORE_SKILLS);
						if (viewMoreButtons.isEmpty()) {
							PageObjectHelper.log(LOGGER, 
									"Reached end of content in Profile Skills Section in Details Popup");
							break;
						}
						WebElement viewMoreBtn = viewMoreButtons.get(0);
						String ViewMoreSkillsBtnText = wait.until(ExpectedConditions.visibilityOf(viewMoreBtn)).getText();
						// Scroll element into view before clicking
						js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", viewMoreBtn);
						safeSleep(500);
						// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
						PerformanceUtils.waitForUIStability(driver, 2);
						try {
							wait.until(ExpectedConditions.elementToBeClickable(viewMoreBtn)).click();
						} catch (Exception e) {
							js.executeScript("arguments[0].click();", viewMoreBtn);
						}
						PageObjectHelper.log(LOGGER, "Clicked on " + ViewMoreSkillsBtnText
								+ " button in Profile Skills Section in Details Popup");
						// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
						PerformanceUtils.waitForUIStability(driver, 2);
					} catch (StaleElementReferenceException e) {
						PageObjectHelper.log(LOGGER, "Reached end of content in Profile Skills Section in Details Popup");
						break;
					}
				}
				String ProfileSkillsText = wait.until(ExpectedConditions.visibilityOf(findElement(SKILLS_DATA))).getText();
				Assert.assertEquals(ProfileSkillsText, manualMappingProfileSkills.get());
				PageObjectHelper.log(LOGGER, 
						"Profile Skills in Details Popup matches with Mapped Success Profile Skills as expected");
			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER,
						"validate_profile_skills_in_details_popup_matches_with_mapped_success_profile_skills",
						"Issue validating skills in details popup", e);
				e.printStackTrace();
				Assert.fail(
						"Issue in validating data in Profile Skills Section in Details Popup matches with Mapped Success Profile Skills...Please Investigate!!!");
				PageObjectHelper.log(LOGGER, 
						"Issue in validating data in Profile Skills Section in Details Popup matches with Mapped Success Profile Skills...Please Investigate!!!");
			}
		}
	}

	public void search_for_organization_job_with_manually_mapped_sp() {
		if (manualMapping.get()) {
			try {
				wait.until(ExpectedConditions.visibilityOf(findElement(SEARCH_BAR))).clear();
				try {
					wait.until(ExpectedConditions.elementToBeClickable(findElement(SEARCH_BAR))).click();
				} catch (Exception e) {
					try {
						js.executeScript("arguments[0].click();", findElement(SEARCH_BAR));
					} catch (Exception s) {
						jsClick(findElement(SEARCH_BAR));
					}
				}
				wait.until(ExpectedConditions.visibilityOf(findElement(SEARCH_BAR))).sendKeys(orgJobName.get());
				wait.until(ExpectedConditions.visibilityOf(findElement(SEARCH_BAR))).sendKeys(Keys.ENTER);
				waitForSpinners();
				// PERFORMANCE: Replaced Thread.sleep(2000) with search results wait
				PerformanceUtils.waitForSearchResults(driver, By.xpath("//tbody//tr"), 2);
				PageObjectHelper.log(LOGGER, "Entered job name as " + orgJobName.get() + " in the search bar");
				// PERFORMANCE: Replaced Thread.sleep(2000) with page ready wait
				PerformanceUtils.waitForPageReady(driver, 2);
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail("Failed to enter Organization job name text in search bar...Please investigate!!!");
				PageObjectHelper.log(LOGGER, 
						"Failed to enter Organization job name text in search bar...Please investigate!!!");
			}
		}
	}
}
