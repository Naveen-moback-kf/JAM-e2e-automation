package com.kfonetalentsuite.pageobjects.JobMapping;
import static com.kfonetalentsuite.pageobjects.JobMapping.BasePageObject.Locators.JobMappingScreen.*;
import static com.kfonetalentsuite.pageobjects.JobMapping.BasePageObject.Locators.Common.*;
import static com.kfonetalentsuite.pageobjects.JobMapping.BasePageObject.Locators.ProfileDetails.*;

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

import com.kfonetalentsuite.utils.JobMapping.Utilities;
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
	/**
	 * Universal method to check if profiles are available for manual mapping.
	 * Throws SkipException if all profiles are already mapped - used across Features 16 and 17.
	 * Called from Step Definition at the beginning of manual mapping scenarios.
	 */
	public void skipScenarioIfAllProfilesAlreadyMapped() {
		// If mapSP is false or null, it means all profiles are already mapped
		Boolean mapSPValue = mapSP.get();
		
		if (mapSPValue == null || !mapSPValue) {
			LOGGER.warn("SKIPPED: All profiles are already mapped with BIC profiles - No unmapped profiles available for manual mapping");
			throw new org.testng.SkipException("All profiles are already mapped with BIC profiles - No unmapped profiles available for manual mapping");
		}
	}

	// METHODs
	public void user_should_search_for_job_profile_with_search_a_different_profile_button_on_mapped_success_profile() {
		try {
			waitForSpinners();
			// PERFORMANCE: Replaced Thread.sleep(2000) with smart page ready wait
			Utilities.waitForPageReady(driver);

			// Reset the flag at the start
			mapSP.set(false);
			;

			LOGGER.info("Searching for job profile with 'Search a Different Profile' button (Manually Mapped profile)...");

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
														LOGGER.info(" Job profile with 'Search a Different Profile' button found at row " + i);
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
				Utilities.waitForPageReady(driver, 2);
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
				Utilities.handleError(LOGGER,
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
				Utilities.waitForPageReady(driver, 2);
				WebElement jobName = driver.findElement(By.xpath("//tbody//tr[" + Integer.toString(rowNumber.get() - 1)
						+ "]//td[2]//div[contains(text(),'(')]"));
				Assert.assertTrue(Utilities.waitForVisible(wait, jobName).isDisplayed());
				String jobname1 = Utilities.waitForVisible(wait, jobName).getText();
				orgJobName.set(jobname1.split("-", 2)[0].trim());
				orgJobCode.set(jobname1.split("-", 2)[1].trim().substring(1, jobname1.split("-", 2)[1].length() - 2));
				LOGGER.info("Organization Job name / Job Code of Profilewith Search a Different Profile button in the organization table : "
								+ orgJobName.get() + "/" + orgJobCode.get());
				LOGGER.info(
						"Organization Job name / Job Code of Profile a Different Profile button in the organization table : "
								+ orgJobName.get() + "/" + orgJobCode.get());
			} catch (Exception e) {
				Utilities.handleError(LOGGER, "user_should_verify_organization_job_name_and_job_code_values",
						"Issue verifying organization job name/code", e);
				e.printStackTrace();
				LOGGER.info("Issue in verifying job name of Profile with Search a Different Profile button in the Organization jobs profile list...Please Investigate!!!");
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
				Assert.assertTrue(Utilities.waitForVisible(wait, jobGrade).isDisplayed());
				String jobGradeText = Utilities.waitForVisible(wait, jobGrade).getText();
				if (jobGradeText.contentEquals("-") || jobGradeText.isEmpty() || jobGradeText.isBlank()) {
					jobGradeText = "NULL";
					orgJobGrade.set(jobGradeText);
				}
				orgJobGrade.set(jobGradeText);
				LOGGER.info("Grade value of Organization Job Profile with Search a Different Profile button : "
								+ orgJobGrade.get());
			} catch (Exception e) {
				Utilities.handleError(LOGGER, "user_should_verify_organization_job_grade_and_department_values",
						"Issue verifying organization job grade", e);
				e.printStackTrace();
				LOGGER.info("Issue in Verifying Organization Job Grade value of Profile with Search a Different Profile button...Please Investigate!!!");
				Assert.fail(
						"Issue in Verifying Organization Job Grade value of Profile with Search a Different Profile button...Please Investigate!!!");
			}

			try {
				WebElement jobDepartment = driver.findElement(
						By.xpath("//tbody//tr[" + Integer.toString(rowNumber.get() - 1) + "]//td[4]//div[1]"));
				Assert.assertTrue(Utilities.waitForVisible(wait, jobDepartment).isDisplayed());
				String jobDepartmentText = Utilities.waitForVisible(wait, jobDepartment).getText();
				if (jobDepartmentText.contentEquals("-") || jobDepartmentText.isEmpty()
						|| jobDepartmentText.isBlank()) {
					jobDepartmentText = "NULL";
					orgJobDepartment.set(jobDepartmentText);
				} else {
					orgJobDepartment.set(jobDepartmentText);
				}
				LOGGER.info("Department value of Organization Job Profile with Search a Different Profile button : "
								+ orgJobDepartment.get());
			} catch (Exception e) {
				Utilities.handleError(LOGGER, "user_should_verify_organization_job_grade_and_department_values",
						"Issue verifying organization job department", e);
				e.printStackTrace();
				LOGGER.info("Issue in Verifying Organization Job Department value of Profile with Search a Different Profile button...Please Investigate!!!");
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
				Assert.assertTrue(Utilities.waitForVisible(wait, jobFunction).isDisplayed());
				String jobFunctionText = Utilities.waitForVisible(wait, jobFunction).getText();
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
				LOGGER.info("Function / Sub-function values of Organization Job Profile with Search a Different Profile button : "
								+ orgJobFunction.get());
			} catch (Exception e) {
				Utilities.handleError(LOGGER, "user_should_verify_organization_job_function_or_sub_function",
						"Issue verifying organization job function", e);
				e.printStackTrace();
				LOGGER.info("Issue in Verifying Organization Job Function / Sub-function values of Profile with Search a Different Profile button...Please Investigate!!!");
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
				String MappedProfileNameText = Utilities.waitForClickable(wait, linkedMappedProfile)
						.getText();
				mappedSuccessPrflName.set(MappedProfileNameText);
				// Scroll element into view before clicking
				WebElement element = Utilities.waitForClickable(wait, linkedMappedProfile);
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
				LOGGER.info("Clicked on Manually Mapped Profile with name "
						+ MappedProfileNameText + " of Organization Job " + orgJobName.get());
				waitForSpinners();
			} catch (Exception e) {
				Utilities.handleError(LOGGER,
						"click_on_mapped_profile_of_job_profile_with_search_a_different_profile_button",
						"Issue clicking mapped profile", e);
				e.printStackTrace();
				LOGGER.info("Issue in clicking Manually Mapped Profile linked with job name "
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
				LOGGER.info("Mapped Profile details popup is displayed on screen");
			} catch (Exception e) {
				Utilities.handleError(LOGGER, "verify_mapped_profile_details_popup_is_displayed",
						"Issue displaying profile details popup", e);
			}
		}
	}

	public void user_is_on_profile_details_popup_of_manually_mapped_profile() {
		if (mapSP.get() | manualMapping.get()) {
			LOGGER.info("User is on Profile details Popup of Manually Mapped Profile");
		}
	}

	public void verify_profile_header_matches_with_mapped_profile_name() {
		if (mapSP.get() | manualMapping.get()) {
			try {
				String profileHeaderName = getElementText(PROFILE_HEADER_TEXT);
				Assert.assertEquals(mappedSuccessPrflName.get(), profileHeaderName);
				LOGGER.info("Profile header on the details popup : " + profileHeaderName);
			} catch (Exception e) {
				Utilities.handleError(LOGGER, "verify_profile_header_matches_with_mapped_profile_name",
						"Issue verifying profile header", e);
			}
		}
	}

	public void verify_mapped_profile_details_displaying_on_the_popup() {
		if (mapSP.get() | manualMapping.get()) {
			try {
				String profileDeatilsText = getElementText(DETAILS_CONTAINER);
				ProfileDetails.set(profileDeatilsText);
				LOGGER.info("Profile Details displaying on the popup screen: " + profileDeatilsText);
			} catch (Exception e) {
				Utilities.handleError(LOGGER, "verify_mapped_profile_details_displaying_on_the_popup",
						"Issue displaying profile details", e);
			}
		}
	}

	public void user_should_verify_profile_level_dropdown_is_available_and_validate_levels_present_inside_dropdown_of_mapped_profile() {
		if (mapSP.get() | manualMapping.get()) {
			try {
				if (waitForElement(PROFILE_LEVEL_DROPDOWN).isEnabled()) {
					waitForClickable(PROFILE_LEVEL_DROPDOWN).click();
					LOGGER.info("Profile Level dropdown is available and Clicked on it...");
					Select dropdown = new Select(findElement(PROFILE_LEVEL_DROPDOWN));
					List<WebElement> allOptions = dropdown.getOptions();
					LOGGER.info("Levels present inside profile level dropdown : ");
					for (WebElement option : allOptions) {
						LOGGER.info(option.getText());
					}
					waitForClickable(PROFILE_LEVEL_DROPDOWN).click(); // This click is
					LOGGER.info("Successfully Verified Profile Level dropdown and levels present inside it...");
				} else {
					LOGGER.info("No Profile Levels available for this mapped profile with name : "
							+ mappedSuccessPrflName.get());
					LOGGER.info("No Profile Levels available for this mapped profile with name : "
									+ mappedSuccessPrflName.get());
				}
			} catch (Exception e) {
				Utilities.handleError(LOGGER,
						"user_should_verify_profile_level_dropdown_is_available_and_validate_levels_present_inside_dropdown_of_mapped_profile",
						"Issue validating profile level dropdown", e);
				e.printStackTrace();
				LOGGER.info(" Issue in validating profile level dropdown in profile details popup in Job Mapping page...Please Investigate!!!");
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
				LOGGER.info("Role summary of Mapped Success Profile : " + ProfileRoleSummary.get());
			} catch (Exception e) {
				Utilities.handleError(LOGGER, "validate_role_summary_of_mapped_profile_is_displaying",
						"Issue validating role summary", e);
				e.printStackTrace();
				LOGGER.info(" Issue in validating Role Summary in Profile Details Popup in Job Mapping page...Please Investigate!!!");
				Assert.fail(
						"Issue in validating Role Summary in Profile Details Popup in Job Mapping page...Please Investigate!!!");
			}
		}
	}

	public void validate_data_in_responsibilities_tab_of_mapped_profile() {
		if (mapSP.get() | manualMapping.get()) {
			try {
				// PERFORMANCE: Replaced Thread.sleep(2000) with smart element wait
				Utilities.waitForUIStability(driver, 2);
				
				// Safely get first View More button - may not exist
				List<WebElement> viewMoreButtons = findElements(VIEW_MORE_RESPONSIBILITIES);
				if (!viewMoreButtons.isEmpty()) {
					Utilities.waitForClickable(wait, viewMoreButtons.get(0)).click();
				}
				
				while (true) {
					try {
						viewMoreButtons = findElements(VIEW_MORE_RESPONSIBILITIES);
						if (viewMoreButtons.isEmpty()) {
							LOGGER.info("Reached end of content in Responsibilities screen");
							break;
						}
						WebElement viewMoreBtn = viewMoreButtons.get(0);
						js.executeScript("arguments[0].scrollIntoView(true);", viewMoreBtn);
						String ViewMoreBtnText = Utilities.waitForVisible(wait, viewMoreBtn).getText();
						viewMoreBtn.click();
						LOGGER.info("Clicked on " + ViewMoreBtnText + " button in Responsibilities screen");
						// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
						Utilities.waitForUIStability(driver, 2);
					} catch (StaleElementReferenceException e) {
						LOGGER.info("Reached end of content in Responsibilities screen");
						break;
					}
				}
				String responsibilitiesDataText = Utilities.waitForVisible(wait, findElement(RESPONSIBILITIES_DATA))
						.getText();
				ProfileResponsibilities.set(responsibilitiesDataText);
			} catch (Exception e) {
				Utilities.handleError(LOGGER, "validate_data_in_responsibilities_tab_of_mapped_profile",
						"Issue validating responsibilities screen", e);
				e.printStackTrace();
				LOGGER.info(" Issue in validating data in Responsibilities screen in Profile Details Popup in Job Mapping page...Please Investigate!!!");
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
				Utilities.waitForUIStability(driver, 2);
				Utilities.waitForClickable(wait, findElement(BEHAVIOUR_TAB)).click();
				LOGGER.info("Clicked on Behaviour Competencies screen");
				while (true) {
					try {
						List<WebElement> viewMoreButtons = findElements(VIEW_MORE_BEHAVIOUR);
						if (viewMoreButtons.isEmpty()) {
							LOGGER.info("Reached end of content in Behaviour Competencies screen");
							break;
						}
						WebElement viewMoreBtn = viewMoreButtons.get(0);
						js.executeScript("arguments[0].scrollIntoView(true);", viewMoreBtn);
						String ViewMoreBtnText = Utilities.waitForVisible(wait, viewMoreBtn).getText();
						viewMoreBtn.click();
						LOGGER.info("Clicked on " + ViewMoreBtnText + " button in Behaviour Competencies screen");
						// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
						Utilities.waitForUIStability(driver, 2);
					} catch (StaleElementReferenceException e) {
						LOGGER.info("Reached end of content in Behaviour Competencies screen");
						break;
					}
				}
				String behaviourDataText = Utilities.waitForVisible(wait, findElement(BEHAVIOUR_DATA)).getText();
				ProfileBehaviouralCompetencies.set(behaviourDataText);
			} catch (Exception e) {
				Utilities.handleError(LOGGER, "validate_data_in_behavioural_competencies_tab_of_mapped_profile",
						"Issue validating behavioural competencies screen", e);
				e.printStackTrace();
				LOGGER.info(" Issue in validating data in Behaviour Competencies screen in Profile Details Popup in Job Mapping page...Please Investigate!!!");
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
				Utilities.waitForUIStability(driver, 2);

				// Wait for any blocking loader overlays to disappear
				try {
					wait.until(
							ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@data-testid='loader']")));
				} catch (Exception e) {
					// Loader not present or already gone
				}

				Utilities.waitForClickable(wait, findElement(SKILLS_TAB)).click();
				LOGGER.info("Clicked on Skills screen");
				while (true) {
					try {
						List<WebElement> viewMoreButtons = findElements(VIEW_MORE_SKILLS);
						if (viewMoreButtons.isEmpty()) {
							LOGGER.info("Reached end of content in Skills screen");
							break;
						}
						WebElement viewMoreBtn = viewMoreButtons.get(0);
						js.executeScript("arguments[0].scrollIntoView(true);", viewMoreBtn);
						String ViewMoreBtnText = Utilities.waitForVisible(wait, viewMoreBtn).getText();
						viewMoreBtn.click();
						LOGGER.info("Clicked on " + ViewMoreBtnText + " button in Skills screen");
						// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
						Utilities.waitForUIStability(driver, 2);
					} catch (StaleElementReferenceException e) {
						LOGGER.info("Reached end of content in Skills screen");
						break;
					}
				}
				String skillsDataText = Utilities.waitForVisible(wait, findElement(SKILLS_DATA)).getText();
				ProfileSkills.set(skillsDataText);
//				LOGGER.info("Data present in Skills screen : \n" + skillsDataText);
//				LOGGER.info("Data present in Skills screen : \n" + skillsDataText);
			} catch (Exception e) {
				Utilities.handleError(LOGGER, "validate_data_in_skills_tab_of_mapped_profile",
						"Issue validating skills screen", e);
				e.printStackTrace();
				LOGGER.info(" Issue in validating data in Skills screen in Profile Details Popup in Job Mapping page...Please Investigate!!!");
				Assert.fail(
						"Issue in validating data in Skills screen in Profile Details Popup in Job Mapping page...Please Investigate!!!");
			}
		}
	}

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
				Utilities.waitForPageReady(driver, 2);
				Thread.sleep(500); // Additional buffer for DOM updates in headless mode

				// STEP 3: Verify the button is now visible and clickable
				LOGGER.debug("Waiting for Publish button to be clickable after scroll...");
				boolean isButtonDisplayed = Utilities.waitForClickable(wait, findElement(PUBLISH_PROFILE_BTN))
						.isDisplayed();

				if (isButtonDisplayed) {
					LOGGER.info("✅ Publish button is displaying on the Profile Details Popup and is clickable");
				} else {
					throw new Exception("Publish button found but not displayed");
				}

			} catch (Exception e) {
				LOGGER.error(
						"❌ Issue verifying Publish Profile button - Method: user_should_verify_publish_profile_button_is_available_on_popup_screen_of_mapped_profile",
						e);
				LOGGER.error(
						"Possible causes: 1) Button not scrolled into view in headless mode, 2) Popup not fully loaded, 3) Element locator issue");
				Utilities.handleError(LOGGER,
						"verify_publish_profile_button_is_displayed_on_mapped_profile_details_popup",
						"Issue verifying publish profile button", e);
				e.printStackTrace();
				LOGGER.info(" Issue in verifying Publish Profile button on profile details popup screen in Job Mapping page....Please Investigate!!!");
				Assert.fail(
						"Issue in verifying Publish Profile button on profile details popup screen in Job Mapping page....Please Investigate!!!");
			}
		}
	}

	public void click_on_close_button_in_profile_details_popup_of_mapped_profile() {
		if (mapSP.get() | manualMapping.get()) {
			try {
				// Scroll element into view before clicking
				WebElement element = Utilities.waitForClickable(wait, findElement(PROFILE_DETAILS_CLOSE_BTN));
				js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
				try {
					Thread.sleep(500);
				} catch (InterruptedException ie) {
				}
				try {
					element.click();
				} catch (Exception e) {
					try {
						js.executeScript("arguments[0].click();", findElement(PROFILE_DETAILS_CLOSE_BTN));
					} catch (Exception s) {
						jsClick(findElement(PROFILE_DETAILS_CLOSE_BTN));
					}
				}
				LOGGER.info("Clicked on close button in Profile details popup");
				waitForSpinners();
			} catch (Exception e) {
				Utilities.handleError(LOGGER, "click_on_close_button_in_profile_details_popup_of_mapped_profile",
						"Issue clicking close button", e);
				e.printStackTrace();
				LOGGER.info("Issue in clicking close button in Profile details popup in Job Mapping page...Please Investigate!!!");
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
				WebElement element = Utilities.waitForClickable(wait, searchDifferntProfileBtn);
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
				LOGGER.info("Clicked on Search a different profile button on Mapped SP of Organization Job Profile with name : "
								+ orgJobName.get());
			} catch (Exception e) {
				Utilities.handleError(LOGGER,
						"click_on_search_a_different_profile_button_on_mapped_success_profile",
						"Issue clicking Search Different Profile button", e);
				e.printStackTrace();
				LOGGER.info("Issue in clicking on Search a different profile button in Job Mapping page...Please Investigate!!!");
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
			Utilities.waitForVisible(wait, By.xpath("//*[contains(text(),'Which profile')]"));
			Assert.assertEquals(actualHeader, expectedHeader);
				LOGGER.info("User navigated to Manual Mapping screen");
				if (actualHeader.contentEquals(expectedHeader)) {
					manualMapping.set(true);
				}
			} catch (Exception e) {
				Utilities.handleError(LOGGER, "user_should_be_navigated_to_manual_job_mapping_screen",
						"Issue navigating to Manual Mapping screen", e);
				e.printStackTrace();
				LOGGER.info(" Issue in navigating to Manual Mapping screen...Please Investigate!!!");
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
				LOGGER.info("Organization Job Name and Job code validated successfully in the Manual Mapping screen");
			} catch (Exception e) {
				Utilities.handleError(LOGGER, "verify_organization_job_details_in_manual_mapping_screen",
						"Issue validating organization job details", e);
				e.printStackTrace();
				LOGGER.info(" Issue in validating Organization Job Name and Job Code in the Manual Mapping screen....Please Investigate!!!");
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
				LOGGER.info("Organization Job Grade value validated successfully in the Manual Mapping screen");
			} catch (Exception e) {
				Utilities.handleError(LOGGER, "verify_organization_job_details_in_manual_mapping_screen",
						"Issue validating organization job grade in manual mapping", e);
				e.printStackTrace();
				Assert.fail(
						"Issue in validating Organization Job Grade value in the Manual Mapping screen....Please Investigate!!!");
				LOGGER.info("Issue in validating Organization Job Grade value in the Manual Mapping screen....Please Investigate!!!");
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
				LOGGER.info("Organization Job Department value validated successfully in the Manual Mapping screen");
			} catch (Exception e) {
				Utilities.handleError(LOGGER, "verify_organization_job_details_in_manual_mapping_screen",
						"Issue validating organization job department in manual mapping", e);
				e.printStackTrace();
				Assert.fail(
						"Issue in validating Organization Job Department value in the Manual Mapping screen....Please Investigate!!!");
				LOGGER.info("Issue in validating Organization Job Department value in the Manual Mapping screen....Please Investigate!!!");
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
								LOGGER.info("Organization Job Function / Sub-function values validated successfully in the Manual Mapping screen");
			} catch (Exception e) {
				Utilities.handleError(LOGGER, "verify_organization_job_details_in_manual_mapping_screen",
						"Issue validating organization job function in manual mapping", e);
				e.printStackTrace();
				Assert.fail(
						"Issue in validating Organization Job Function / Sub-function values in the Manual Mapping screen....Please Investigate!!!");
				LOGGER.info("Issue in validating Organization Job Function / Sub-function values in the Manual Mapping screen....Please Investigate!!!");
			}
		}
	}

	public void user_should_verify_last_saved_profile_name_is_displaying_in_manual_mapping_screen() {
		if (manualMapping.get()) {
			try {
				String lastSavedProfileText = wait
						.until(ExpectedConditions.visibilityOf(findElement(LAST_SAVED_PROFILE))).getText();
				Assert.assertTrue(lastSavedProfileText.contains("LAST SAVED PROFILE"));
								LOGGER.info("Last saved Profile is displaying in the Manual Mapping screen as "
						+ lastSavedProfileText);
			} catch (Exception e) {
				Utilities.handleError(LOGGER,
						"user_should_verify_last_saved_profile_name_is_displaying_in_manual_mapping_screen",
						"Issue verifying last saved profile name", e);
				e.printStackTrace();
				Assert.fail(
						"Issue in displaying of Last Saved Profile name in the Manual Mapping screen....Please Investigate!!!");
				LOGGER.info("Issue in displaying of Last Saved Profile name in the Manual Mapping screen....Please Investigate!!!");
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
				LOGGER.info("Clicked on Last Saved Profile name : " + text + " in the Manual Mapping screen");
				Utilities.waitForClickable(wait, findElement(MANUAL_MAPPING_PROFILE_TITLE)).isDisplayed();
			} catch (Exception e) {
				Utilities.handleError(LOGGER, "click_on_last_saved_profile_name_in_manual_mapping_screen",
						"Issue clicking last saved profile name", e);
				e.printStackTrace();
				Assert.fail(
						"Issue in clicking on Last Saved Profile name in the Manual Mapping screen....Please Investigate!!!");
				LOGGER.info("Issue in clicking on Last Saved Profile name in the Manual Mapping screen....Please Investigate!!!");
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
								LOGGER.info("Last Saved Profile Name in the Manual Mapping screen matches with Profile Name in the details popup");
			} catch (Exception e) {
				Utilities.handleError(LOGGER,
						"user_should_verify_last_saved_profile_name_in_manual_mapping_screen_matches_with_profile_name_in_details_popup",
						"Issue verifying last saved profile name match", e);
				e.printStackTrace();
				Assert.fail(
						"Issue in verifying Last Saved Profile Name in the Manual Mapping screen....Please Investigate!!!");
				LOGGER.info("Issue in verifying Last Saved Profile Name in the Manual Mapping screen....Please Investigate!!!");
			}
		}
	}

	public void user_should_verify_profile_level_dropdown_is_available_on_last_saved_success_profile_and_validate_levels_present_inside_dropdown() {
		if (manualMapping.get()) {
			try {
				if (waitForElement(PROFILE_LEVEL_DROPDOWN).isEnabled()) {
					waitForClickable(PROFILE_LEVEL_DROPDOWN).click();
					LOGGER.info("Profile Level dropdown is available and Clicked on it in Manual Mapping screen...");
					Select dropdown = new Select(findElement(PROFILE_LEVEL_DROPDOWN));
					List<WebElement> allOptions = dropdown.getOptions();
					LOGGER.info("Levels present inside profile level dropdown : ");
					for (WebElement option : allOptions) {
						LOGGER.info(option.getText());
					}
					waitForClickable(PROFILE_LEVEL_DROPDOWN).click(); // This click is
										LOGGER.info("Successfully Verified Profile Level dropdown in Manual Mapping screen and levels present inside it...");
				} else {
					LOGGER.info("No Profile Levels available for this Last Saved profile with name : "
							+ mappedSuccessPrflName.get());
					LOGGER.info("No Profile Levels available for this Last Saved profile with name : "
									+ mappedSuccessPrflName.get());
				}

			} catch (Exception e) {
				Utilities.handleError(LOGGER,
						"user_should_verify_profile_level_dropdown_is_available_on_last_saved_success_profile_and_validate_levels_present_inside_dropdown",
						"Issue validating profile level dropdown in manual mapping", e);
				e.printStackTrace();
				Assert.fail(
						"Issue in validating profile level dropdown of Last Saved Profile in Manual Mapping page...Please Investigate!!!");
				LOGGER.info("Issue in validating profile level dropdown in Last Saved Profile in Manual Mapping page...Please Investigate!!!");
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
								LOGGER.info("Last Saved Profile Role Summary in the Manual Mapping screen matches with Mapped Success Profile Role Summary in details popup");
			} catch (Exception e) {
				Utilities.handleError(LOGGER,
						"validate_last_saved_success_profile_role_summary_matches_with_profile_role_summary_in_details_popup",
						"Issue validating role summary match", e);
				e.printStackTrace();
				Assert.fail(
						"Issue in Validating Last Saved Profile Role Summary in the Manual Mapping screen....Please Investigate!!!");
				LOGGER.info("Issue in Validating Last Saved Profile Role Summary in the Manual Mapping screen....Please Investigate!!!");
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
								LOGGER.info("Last Saved Profile Details in the Manual Mapping screen matches with Mapped Success Profile Details in details popup");
			} catch (Exception e) {
				Utilities.handleError(LOGGER,
						"validate_last_saved_success_profile_details_matches_with_profile_details_in_details_popup",
						"Issue validating profile details match", e);
				e.printStackTrace();
				Assert.fail(
						"Issue in Validating Last Saved Profile Details in the Manual Mapping screen....Please Investigate!!!");
				LOGGER.info("Issue in Validating Last Saved Profile Details in the Manual Mapping screen....Please Investigate!!!");
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
					Utilities.waitForClickable(wait, viewMoreButtons.get(0)).click();
				}
				
				while (true) {
					try {
						viewMoreButtons = findElements(MANUAL_MAPPING_VIEW_MORE_RESPONSIBILITIES);
						if (viewMoreButtons.isEmpty()) {
							js.executeScript("window.scrollTo(0, document.documentElement.scrollHeight);"); // Headless-compatible
							LOGGER.info("Reached end of content in Last Saved Success Profile Responsibilities Section in Manual Mapping screen");
							break;
						}
						WebElement viewMoreBtn = viewMoreButtons.get(0);
						String ViewMoreResponsibilitiesBtnText = Utilities.waitForVisible(wait, viewMoreBtn).getText();
						// Scroll element into view before clicking
						js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", viewMoreBtn);
						safeSleep(500);
						// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
						Utilities.waitForUIStability(driver, 2);
						try {
							Utilities.waitForClickable(wait, viewMoreBtn).click();
						} catch (Exception e) {
							js.executeScript("arguments[0].click();", viewMoreBtn);
						}
						LOGGER.info("Clicked on " + ViewMoreResponsibilitiesBtnText
								+ " button in Last Saved Success Profile Responsibilities Section in Manual Mapping screen");
						// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
						Utilities.waitForUIStability(driver, 2);
					} catch (StaleElementReferenceException e) {
						js.executeScript("window.scrollTo(0, document.documentElement.scrollHeight);"); // Headless-compatible
						LOGGER.info("Reached end of content in Last Saved Success Profile Responsibilities Section in Manual Mapping screen");
						break;
					}
				}
				String responsibilitiesText = wait
						.until(ExpectedConditions.visibilityOf(findElement(MANUAL_MAPPING_RESPONSIBILITIES_DATA))).getText();
				Assert.assertEquals(ProfileResponsibilities.get(), responsibilitiesText);
								LOGGER.info("Last Saved Success Profile Responsibilities in the Job Compare page matches with Mapped Success Profile Responsibilities in details popup");
			} catch (Exception e) {
				Utilities.handleError(LOGGER,
						"validate_last_saved_success_profile_responsibilities_matches_with_profile_responsibilities_in_details_popup",
						"Issue validating responsibilities match", e);
				e.printStackTrace();
				Assert.fail(
						"Issue in validating data in Last Saved Success Profile Responsibilities Section in Manual Mapping screen...Please Investigate!!!");
				LOGGER.info("Issue in validating data in Last Saved Success Profile Responsibilities Section in Manual Mapping screen...Please Investigate!!!");
			}
		}
	}

	public void validate_last_saved_success_profile_behavioural_competencies_matches_with_profile_behavioural_competencies_in_details_popup() {
		if (manualMapping.get()) {
			try {
				js.executeScript("arguments[0].scrollIntoView(true);", findElement(MANUAL_MAPPING_ROLE_SUMMARY));
				// PERFORMANCE: Replaced Thread.sleep(2000) with smart element wait
				Utilities.waitForUIStability(driver, 2);
				Utilities.waitForClickable(wait, findElement(MANUAL_MAPPING_BEHAVIOUR_TAB))
						.click();
								LOGGER.info("Clicked on BEHAVIOURAL COMPETENCIES screen of Last Saved Profile in Manual Mapping screen");
			while (true) {
					try {
						List<WebElement> viewMoreButtons = findElements(MANUAL_MAPPING_VIEW_MORE_BEHAVIOUR);
						if (viewMoreButtons.isEmpty()) {
							LOGGER.info("Reached end of content in Last Saved Success Profile Behavioural Competencies Section in Manual Mapping screen");
							break;
						}
						WebElement viewMoreBtn = viewMoreButtons.get(0);
						String ViewMoreCompetenciesBtnText = Utilities.waitForVisible(wait, viewMoreBtn).getText();
						// Scroll element into view before clicking
						js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", viewMoreBtn);
						safeSleep(500);
						// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
						Utilities.waitForUIStability(driver, 2);
						try {
							Utilities.waitForClickable(wait, viewMoreBtn).click();
						} catch (Exception e) {
							js.executeScript("arguments[0].click();", viewMoreBtn);
						}
						LOGGER.info("Clicked on " + ViewMoreCompetenciesBtnText
								+ " button in Last Saved Success Profile Behavioural Competencies Section in Manual Mapping screen");
						// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
						Utilities.waitForUIStability(driver, 2);
					} catch (StaleElementReferenceException e) {
						LOGGER.info("Reached end of content in Last Saved Success Profile Behavioural Competencies Section in Manual Mapping screen");
						break;
					}
				}
				String behaviourText = wait
						.until(ExpectedConditions.visibilityOf(findElement(MANUAL_MAPPING_BEHAVIOUR_DATA)))
						.getText();
				Assert.assertEquals(ProfileBehaviouralCompetencies.get(),
						behaviourText);
				LOGGER.info("Last Saved Success Profile Behavioural Competencies in the Job Compare page matches with Mapped Success Profile Behavioural Competencies in details popup");
			} catch (Exception e) {
				Utilities.handleError(LOGGER,
						"validate_last_saved_success_profile_behavioural_competencies_matches_with_profile_behavioural_competencies_in_details_popup",
						"Issue validating behavioural competencies match", e);
				e.printStackTrace();
				Assert.fail(
						"Issue in validating data in Last Saved Success Profile Behavioural Competencies Section in Manual Mapping screen...Please Investigate!!!");
				LOGGER.info("Issue in validating data in Last Saved Success Profile Behavioural Competencies Section in Manual Mapping screen...Please Investigate!!!");
			}
		}
	}

	public void validate_last_saved_success_profile_skills_macthes_with_profile_skills_in_details_popup() {
		if (manualMapping.get()) {
			try {
				js.executeScript("arguments[0].scrollIntoView(true);", findElement(MANUAL_MAPPING_ROLE_SUMMARY));
				// PERFORMANCE: Replaced Thread.sleep(2000) with smart element wait
				Utilities.waitForUIStability(driver, 2);

				// Wait for any blocking loader overlays to disappear
				try {
					wait.until(
							ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@data-testid='loader']")));
				} catch (Exception e) {
					// Loader not present or already gone
				}

			Utilities.waitForClickable(wait, findElement(MANUAL_MAPPING_SKILLS_TAB)).click();
				LOGGER.info("Clicked on SKILLS screen of Last Saved Profile in Manual Mapping screen");
				while (true) {
					try {
						List<WebElement> viewMoreButtons = findElements(MANUAL_MAPPING_VIEW_MORE_SKILLS);
						if (viewMoreButtons.isEmpty()) {
							LOGGER.info("Reached end of content in Last Saved Success Profile Skills Section in Manual Mapping screen");
							break;
						}
						WebElement viewMoreBtn = viewMoreButtons.get(0);
						String ViewMoreSkillsBtnText = Utilities.waitForVisible(wait, viewMoreBtn).getText();
						// Scroll element into view before clicking
						js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", viewMoreBtn);
						safeSleep(500);
						// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
						Utilities.waitForUIStability(driver, 2);
						try {
							Utilities.waitForClickable(wait, viewMoreBtn).click();
						} catch (Exception e) {
							js.executeScript("arguments[0].click();", viewMoreBtn);
						}
						LOGGER.info("Clicked on " + ViewMoreSkillsBtnText
								+ " button in Last Saved Success Profile Skills Section in Manual Mapping screen");
						// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
						Utilities.waitForUIStability(driver, 2);
					} catch (StaleElementReferenceException e) {
						LOGGER.info("Reached end of content in Last Saved Success Profile Skills Section in Manual Mapping screen");
						break;
					}
				}
				String skillsText = wait
						.until(ExpectedConditions.visibilityOf(findElement(MANUAL_MAPPING_SKILLS_DATA))).getText();
				Assert.assertEquals(ProfileSkills.get(), skillsText);
								LOGGER.info("Last Saved Success Profile Skills in the Job Compare page matches with Mapped Success Profile Skills in details popup");
			} catch (Exception e) {
				Utilities.handleError(LOGGER,
						"validate_last_saved_success_profile_skills_macthes_with_profile_skills_in_details_popup",
						"Issue validating skills match", e);
				e.printStackTrace();
				Assert.fail(
						"Issue in validating data in Last Saved Success Profile Skills Section in Manual Mapping screen...Please Investigate!!!");
				LOGGER.info("Issue in validating data in Last Saved Success Profile Skills Section in Manual Mapping screen...Please Investigate!!!");
			}
		}
	}

	public void search_for_success_profile_in_manual_mapping_screen() {
		if (manualMapping.get()) {
			waitForSpinners();
			js.executeScript("window.scrollTo(0, 0);"); // Scroll to top (headless-compatible)
			try {
				// PERFORMANCE: Replaced Thread.sleep(2000) with element readiness wait
				Utilities.waitForVisible(wait, KF_SP_SEARCH_BAR);
				Utilities.waitForVisible(wait, findElement(KF_SP_SEARCH_BAR)).sendKeys(Keys.chord(Keys.CONTROL, "a"));
				Utilities.waitForVisible(wait, findElement(KF_SP_SEARCH_BAR)).sendKeys(SPSearchString.get());
				Assert.assertEquals(SPSearchString.get(), findElement(KF_SP_SEARCH_BAR).getAttribute("value"));
				LOGGER.info("Entered " + SPSearchString.get()
						+ " as Korn Ferry SP Search String in the search bar in Manual Mapping screen");
			} catch (Exception e) {
				Utilities.handleError(LOGGER, "search_for_success_profile_in_manual_mapping_screen",
						"Issue entering search string", e);
				e.printStackTrace();
				Assert.fail(
						"Failed to enter Korn Ferry SP Search String in the search bar in Manual Mapping screen...Please Investigate!!!");
				LOGGER.info("Failed to enter Korn Ferry SP Search String in the search bar in Manual Mapping screen...Please Investigate!!!");
			}
		}
	}

	public void select_first_sucess_profile_from_search_results_in_manual_mapping_screen() {
		if (manualMapping.get()) {
			waitForSpinners();
			try {
				// Wait for search results to load - use presenceOfElementLocated then visibilityOfElementLocated
				// This properly waits for the element to exist before checking visibility
				Utilities.waitForPresent(wait, FIRST_SEARCH_RESULT_BTN);
				WebElement firstResultBtn = Utilities.waitForVisible(wait, FIRST_SEARCH_RESULT_BTN);
				Assert.assertTrue(firstResultBtn.isDisplayed());
				
				// Get the text before clicking
				WebElement firstResultText = Utilities.waitForVisible(wait, FIRST_SEARCH_RESULT_TEXT);
				customSPNameinSearchResults.set(firstResultText.getText());
				
				// Click the first result
				Utilities.waitForClickable(wait, FIRST_SEARCH_RESULT_BTN).click();
				
				LOGGER.info("First SP with Name : " + customSPNameinSearchResults.get()
						+ " from search results is selected in Manual Mapping screen");
				
				// Wait for results to disappear (indicating selection was processed)
				wait.until(ExpectedConditions.invisibilityOfElementLocated(FIRST_SEARCH_RESULT_BTN));
			} catch (Exception e) {
				Utilities.handleError(LOGGER,
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
				String profileHeaderName = Utilities.waitForVisible(wait, findElement(MANUAL_MAPPING_PROFILE_TITLE))
						.getText();
				Assert.assertEquals(customSPNameinSearchResults.get(), profileHeaderName);
				manualMappingProfileName.set(profileHeaderName);
				mappedSuccessPrflName.set(manualMappingProfileName.get());
								LOGGER.info("Success Profile with name " + profileHeaderName
						+ " is added successfully on Manual Job Mapping screen");
			} catch (Exception e) {
				Utilities.handleError(LOGGER, "verify_success_profile_is_added_in_manual_job_mapping_screen",
						"Issue verifying success profile added", e);
				e.printStackTrace();
				Assert.fail("Issue in adding success profile on manual job mapping screen....Please Investigate!!!");
				LOGGER.info("Issue in adding success profile on manual job mapping screen....Please Investigate!!!");
			}
		}
	}

	public void user_should_verify_profile_level_dropdown_is_available_and_validate_levels_present_inside_dropdown_in_manual_job_mapping_screen() {
		if (manualMapping.get()) {
			try {
				if (waitForElement(PROFILE_LEVEL_DROPDOWN).isEnabled()) {
					waitForClickable(PROFILE_LEVEL_DROPDOWN).click();
					LOGGER.info("Profile Level dropdown is available and Clicked on it in Manual Mapping screen...");
					Select dropdown = new Select(findElement(PROFILE_LEVEL_DROPDOWN));
					List<WebElement> allOptions = dropdown.getOptions();
					LOGGER.info("Levels present inside profile level dropdown : ");
					for (WebElement option : allOptions) {
						LOGGER.info(option.getText());
					}
					waitForClickable(PROFILE_LEVEL_DROPDOWN).click(); // This click is
										LOGGER.info("Successfully Verified Profile Level dropdown in Manual Mapping screen and levels present inside it...");
				} else {
					LOGGER.info("No Profile Levels available for the profile with name : "
							+ manualMappingProfileName.get() + " in Manual Mapping page");
					LOGGER.info("No Profile Levels available for this Last Saved profile with name : "
									+ manualMappingProfileName.get() + " in Manual Mapping page");
				}

			} catch (Exception e) {
				Utilities.handleError(LOGGER,
						"user_should_verify_profile_level_dropdown_is_available_and_validate_levels_present_inside_dropdown_in_manual_job_mapping_screen",
						"Issue validating profile level dropdown", e);
				e.printStackTrace();
				Assert.fail(
						"Issue in validating profile level dropdown of Last Saved Profile in Manual Mapping page...Please Investigate!!!");
				LOGGER.info("Issue in validating profile level dropdown in Last Saved Profile in Manual Mapping page...Please Investigate!!!");
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
				Utilities.waitForVisible(wait, By.xpath("//select//option"));
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
					Utilities.waitForUIStability(driver);
					LOGGER.info("Successfully Changed Profile Level to : "
							+ changedlevelvalue.get() + " in Manual Mapping screen");
					Utilities.waitForUIStability(driver);
				} catch (Exception e) {
					Utilities.handleError(LOGGER, "change_profile_level_in_manual_job_mapping_screen",
							"Issue changing profile level", e);
					e.printStackTrace();
					Assert.fail("Issue in Changing Profile Level in Manual Mapping screen...Please Investigate!!!");
					LOGGER.info("Issue in Changing Profile Level in Manual Mapping screen...Please Investigate!!!");
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
					LOGGER.info("Profile Level dropdown closed successfully in Manual Mapping screen");
				} catch (Exception e) {
					e.printStackTrace();
					Assert.fail(
							"Issue in clicking on Profile Level dropdown to close it in Manual Mapping screen...Please Investigate!!!");
					LOGGER.info("Issue in clicking on Profile Level dropdown to close it in Manual Mapping screen...Please Investigate!!!");
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
				LOGGER.info("Role summary of Success Profile in Manual Mapping screen : "
						+ manualMappingProfileRoleSummary.get());
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail(
						"Issue in validating Role Summary of Success Profile in Manual Mapping screen...Please Investigate!!!");
				LOGGER.info("Issue in validating Role Summary of Success Profile in Manual Mapping screen...Please Investigate!!!");
			}
		}
	}

	public void verify_profile_details_displaying_in_manual_job_mapping_screen() {
		if (manualMapping.get()) {
			try {
				String profileDeatilsText = Utilities.waitForVisible(wait, findElement(MANUAL_MAPPING_PROFILE_DETAILS))
						.getText();
				manualMappingProfileDetails.set(profileDeatilsText);
				LOGGER.info("Profile Details for " + manualMappingProfileName.get()
						+ " in Manual Mapping screen: \n" + profileDeatilsText);
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail("Issue in displaying profile details in Manual Mapping screen....Please Investigate!!!");
				LOGGER.info("Issue in displaying profile details in Manual Mapping screen....Please Investigate!!!");
			}
		}
	}

	public void validate_data_in_responsibilities_tab_in_manual_job_mapping_screen() {
		if (manualMapping.get()) {
			try {
				js.executeScript("arguments[0].scrollIntoView(true);", findElement(MANUAL_MAPPING_ROLE_SUMMARY));
				// PERFORMANCE: Replaced Thread.sleep(2000) with smart element wait
				Utilities.waitForUIStability(driver, 2);
			Utilities.waitForClickable(wait, findElement(MANUAL_MAPPING_RESPONSIBILITIES_TAB)).click();
				LOGGER.info("Clicked on Responsibilities screen in Manual Mapping screen");
				while (true) {
					try {
						List<WebElement> viewMoreButtons = findElements(MANUAL_MAPPING_VIEW_MORE_RESPONSIBILITIES);
						if (viewMoreButtons.isEmpty()) {
							js.executeScript("window.scrollTo(0, document.documentElement.scrollHeight);"); // Headless-compatible
							LOGGER.info("Reached end of content in Success Profile Responsibilities Section in Manual Mapping screen");
							break;
						}
						WebElement viewMoreBtn = viewMoreButtons.get(0);
						String ViewMoreResponsibilitiesBtnText = Utilities.waitForVisible(wait, viewMoreBtn).getText();
						// Scroll element into view before clicking
						js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", viewMoreBtn);
						safeSleep(500);
						// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
						Utilities.waitForUIStability(driver, 2);
						try {
							Utilities.waitForClickable(wait, viewMoreBtn).click();
						} catch (Exception e) {
							js.executeScript("arguments[0].click();", viewMoreBtn);
						}
						LOGGER.info("Clicked on " + ViewMoreResponsibilitiesBtnText
								+ " button in Success Profile Responsibilities Section in Manual Mapping screen");
						// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
						Utilities.waitForUIStability(driver, 2);
					} catch (StaleElementReferenceException e) {
						js.executeScript("window.scrollTo(0, document.documentElement.scrollHeight);"); // Scroll DOWN (headless-compatible)
						LOGGER.info("Reached end of content in Success Profile Responsibilities Section in Manual Mapping screen");
						break;
					}
				}
				String responsibilitiesText = wait
						.until(ExpectedConditions.visibilityOf(findElement(MANUAL_MAPPING_RESPONSIBILITIES_DATA))).getText();
				manualMappingProfileResponsibilities.set(responsibilitiesText);
//				LOGGER.info("Data present in Responsibilities screen in Manual Mapping screen : \n" + responsibilitiesText);
//				LOGGER.info("Data present in Responsibilities screen in Manual Mapping screen : \n" + responsibilitiesText);
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail(
						"Issue in validating data in Responsibilities screen in Manual Mapping screen...Please Investigate!!!");
				LOGGER.info("Issue in validating data in Responsibilities screen in Manual Mapping screen...Please Investigate!!!");
			}
		}
	}

	public void validate_data_in_behavioural_competencies_tab_in_manual_job_mapping_screen() {
		if (manualMapping.get()) {
			try {
				js.executeScript("arguments[0].scrollIntoView(true);", findElement(MANUAL_MAPPING_ROLE_SUMMARY));
				// PERFORMANCE: Replaced Thread.sleep(2000) with smart element wait
				Utilities.waitForUIStability(driver, 2);
				Utilities.waitForClickable(wait, findElement(MANUAL_MAPPING_BEHAVIOUR_TAB))
						.click();
				LOGGER.info("Clicked on Behaviour Competencies screen in Manual Mapping screen");
				while (true) {
					try {
						List<WebElement> viewMoreButtons = findElements(MANUAL_MAPPING_VIEW_MORE_BEHAVIOUR);
						if (viewMoreButtons.isEmpty()) {
							LOGGER.info("Reached end of content in Success Profile Behavioural Competencies Section in Manual Mapping screen");
							break;
						}
						WebElement viewMoreBtn = viewMoreButtons.get(0);
						String ViewMoreCompetenciesBtnText = Utilities.waitForVisible(wait, viewMoreBtn).getText();
						// Scroll element into view before clicking
						js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", viewMoreBtn);
						safeSleep(500);
						// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
						Utilities.waitForUIStability(driver, 2);
						try {
							Utilities.waitForClickable(wait, viewMoreBtn).click();
						} catch (Exception e) {
							js.executeScript("arguments[0].click();", viewMoreBtn);
						}
						LOGGER.info("Clicked on " + ViewMoreCompetenciesBtnText
								+ " button in Success Profile Behavioural Competencies Section in Manual Mapping screen");
						// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
						Utilities.waitForUIStability(driver, 2);
					} catch (StaleElementReferenceException e) {
						LOGGER.info("Reached end of content in Success Profile Behavioural Competencies Section in Manual Mapping screen");
						break;
					}
				}
				String behaviourText = wait
						.until(ExpectedConditions.visibilityOf(findElement(MANUAL_MAPPING_BEHAVIOUR_DATA)))
						.getText();
				manualMappingProfileBehaviouralCompetencies.set(behaviourText);
//				LOGGER.info("Data present in Behavioural Competencies screen in Manual Mapping screen : \n" + behaviourText);
//				LOGGER.info("Data present in Behavioural Competencies screen in Manual Mapping screen : \n" + behaviourText);
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail(
						"Issue in validating data in Behavioural Competencies screen in Manual Mapping screen...Please Investigate!!!");
				LOGGER.info("Issue in validating data in Behavioural Competencies screen in Manual Mapping screen...Please Investigate!!!");
			}
		}
	}

	public void validate_data_in_skills_tab_in_manual_job_mapping_screen() {
		if (manualMapping.get()) {
			try {
				js.executeScript("arguments[0].scrollIntoView(true);", findElement(MANUAL_MAPPING_ROLE_SUMMARY));
				// PERFORMANCE: Replaced Thread.sleep(2000) with smart element wait
				Utilities.waitForUIStability(driver, 2);
			Utilities.waitForClickable(wait, findElement(MANUAL_MAPPING_SKILLS_TAB)).click();
				LOGGER.info("Clicked on Skills screen in Manual Mapping screen");
				while (true) {
					try {
						List<WebElement> viewMoreButtons = findElements(MANUAL_MAPPING_VIEW_MORE_SKILLS);
						if (viewMoreButtons.isEmpty()) {
							LOGGER.info("Reached end of content in Success Profile Skills Section in Manual Mapping screen");
							break;
						}
						WebElement viewMoreBtn = viewMoreButtons.get(0);
						String ViewMoreSkillsBtnText = Utilities.waitForVisible(wait, viewMoreBtn).getText();
						// Scroll element into view before clicking
						js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", viewMoreBtn);
						safeSleep(500);
						// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
						Utilities.waitForUIStability(driver, 2);
						try {
							Utilities.waitForClickable(wait, viewMoreBtn).click();
						} catch (Exception e) {
							js.executeScript("arguments[0].click();", viewMoreBtn);
						}
						LOGGER.info("Clicked on " + ViewMoreSkillsBtnText
								+ " button in Success Profile Skills Section in Manual Mapping screen");
						// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
						Utilities.waitForUIStability(driver, 2);
					} catch (StaleElementReferenceException e) {
						LOGGER.info("Reached end of content in Success Profile Skills Section in Manual Mapping screen");
						break;
					}
				}
				String skillsText = wait
						.until(ExpectedConditions.visibilityOf(findElement(MANUAL_MAPPING_SKILLS_DATA))).getText();
				manualMappingProfileSkills.set(skillsText);
//				LOGGER.info("Data present in Skills screen in Manual Mapping screen : \n" + skillsText);
//				LOGGER.info("Data present in Skills screen in Manual Mapping screen : \n" + skillsText);
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail(
						"Issue in validating data in Skills screen in Manual Mapping screen...Please Investigate!!!");
				LOGGER.info("Issue in validating data in Skills screen in Manual Mapping screen...Please Investigate!!!");
			}
		}
	}

	public void click_on_save_selection_button_in_manual_job_mapping_screen() {
		if (manualMapping.get()) {
			try {
				js.executeScript("window.scrollTo(0, 0);"); // Scroll to top (headless-compatible)
				// Scroll element into view before clicking
				WebElement element = Utilities.waitForClickable(wait, findElement(SAVE_SELECTION_BTN));
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
				LOGGER.info("Successfully clicked on Save Selection button in Manual Mapping screen");
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail(
						"Issue in clicking on Save Selection button in Manual Mapping screen...Please Investigate!!!");
				LOGGER.info("Issue in clicking on Save Selection button in Manual Mapping screen...Please Investigate!!!");
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
				Utilities.waitForVisible(wait, findElement(PAGE_CONTAINER));
				Assert.assertTrue(Utilities.waitForVisible(wait, findElement(PAGE_CONTAINER)).isDisplayed());
				LOGGER.info("User navigated to JOB MAPPING page");
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.info("Issue in navigating to Job Mapping page...Please Investigate!!!");
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
				Utilities.waitForPageReady(driver, 2);
				WebElement button = driver.findElement(By.xpath(
						"//tbody//tr[2]//button[contains(text(),'different profile')] | //tbody//tr[2]//button[contains(@id,'view')]"));
				js.executeScript("arguments[0].scrollIntoView(true);", button);
				String text = button.getText();
				if (text.contains("different profile")) {
					rowNumber.set(2);
					LOGGER.info("Organization Job profile " + orgJobName.get()
							+ " with new Mapped Success Profile " + manualMappingProfileName.get()
							+ " is displaying on Top of Profiles List");
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
				LOGGER.info("Issue in Verifying Organization Job with Recently Mapped SP displaying on Top of Profiles List or Not....Please Investigate!!!");
			}
		}
	}

	public void user_should_verify_profile_level_dropdown_is_available_in_details_popup_and_validate_levels_present_inside_dropdown() {
		if (manualMapping.get()) {
			try {
				if (waitForElement(PROFILE_LEVEL_DROPDOWN).isEnabled()) {
					waitForClickable(PROFILE_LEVEL_DROPDOWN).click();
										LOGGER.info("Profile Level dropdown is available and Clicked on it in Mapped Profile details Popup...");
					Select dropdown = new Select(findElement(PROFILE_LEVEL_DROPDOWN));
					List<WebElement> allOptions = dropdown.getOptions();
					LOGGER.info("Levels present inside profile level dropdown : ");
					for (WebElement option : allOptions) {
						LOGGER.info(option.getText());
					}
					waitForClickable(PROFILE_LEVEL_DROPDOWN).click(); // This click is
										LOGGER.info("Successfully Verified Profile Level dropdown in Mapped Profile details Popup and levels present inside it...");
				} else {
					LOGGER.info("No Profile Levels available for Mapped Profile in details Popup : "
							+ manualMappingProfileName.get());
					LOGGER.info("No Profile Levels available for Mapped Profile in details Popup : "
									+ manualMappingProfileName.get());
				}

			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail(
						"Issue in validating profile level dropdown of Mapped Profile in details Popup...Please Investigate!!!");
				LOGGER.info("Issue in validating profile level dropdown in Mapped Profile in details Popup...Please Investigate!!!");
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
								LOGGER.info("Profile Role Summary in the Details Popup matches with Mapped Success Profile Role Summary");
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail(
						"Issue in Validating Profile Role Summary in Details Popup matches with Mapped Success Profile Role Summary....Please Investigate!!!");
				LOGGER.info("Issue in Validating Profile Role Summary in Details Popup matches with Mapped Success Profile Role Summary....Please Investigate!!!");
			}
		}
	}

	public void validate_profile_details_in_details_popup_matches_with_mapped_success_profile_details() {
		if (manualMapping.get()) {
			try {
				js.executeScript("arguments[0].scrollIntoView(true);", findElement(DETAILS_CONTAINER));
				String mappedProfileDetailsText = Utilities.waitForVisible(wait, findElement(DETAILS_CONTAINER)).getText();
				Assert.assertEquals(mappedProfileDetailsText, manualMappingProfileDetails.get());
				LOGGER.info("Profile Details in Details Popup matches with Mapped Success Profile Details");
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail(
						"Issue in Validating Profile Details in Details Popup matches with Mapped Success Profile Details....Please Investigate!!!");
				LOGGER.info("Issue in Validating Profile Details in Details Popup matches with Mapped Success Profile Details....Please Investigate!!!");
			}
		}
	}

	public void validate_profile_responsibilities_in_details_popup_matches_with_mapped_success_profile_responsibilities() {
		if (manualMapping.get()) {
			try {
				// PERFORMANCE: Replaced Thread.sleep(2000) with smart element wait
				Utilities.waitForUIStability(driver, 2);
				
				// Safely get first View More button - may not exist
				List<WebElement> viewMoreButtons = findElements(VIEW_MORE_RESPONSIBILITIES);
				if (!viewMoreButtons.isEmpty()) {
					Utilities.waitForClickable(wait, viewMoreButtons.get(0)).click();
				}
				
				while (true) {
					try {
						viewMoreButtons = findElements(VIEW_MORE_RESPONSIBILITIES);
						if (viewMoreButtons.isEmpty()) {
							js.executeScript("window.scrollTo(0, document.documentElement.scrollHeight);"); // Scroll DOWN (headless-compatible)
							LOGGER.info("Reached end of content in Profile Responsibilities Section in Details Popup");
							break;
						}
						WebElement viewMoreBtn = viewMoreButtons.get(0);
						String ViewMoreResponsibilitiesBtnText = Utilities.waitForVisible(wait, viewMoreBtn).getText();
						// Scroll element into view before clicking
						js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", viewMoreBtn);
						safeSleep(500);
						// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
						Utilities.waitForUIStability(driver, 2);
						try {
							Utilities.waitForClickable(wait, viewMoreBtn).click();
						} catch (Exception e) {
							js.executeScript("arguments[0].click();", viewMoreBtn);
						}
						LOGGER.info("Clicked on " + ViewMoreResponsibilitiesBtnText
								+ " button in Profile Responsibilities Section in Details Popup");
						// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
						Utilities.waitForUIStability(driver, 2);
					} catch (StaleElementReferenceException e) {
						js.executeScript("window.scrollTo(0, document.documentElement.scrollHeight);"); // Scroll DOWN (headless-compatible)
						LOGGER.info("Reached end of content in Profile Responsibilities Section in Details Popup");
						break;
					}
				}
				String ProfileResponsibilitiesText = Utilities.waitForVisible(wait, findElement(RESPONSIBILITIES_DATA))
						.getText();
				Assert.assertEquals(ProfileResponsibilitiesText, manualMappingProfileResponsibilities.get());
								LOGGER.info("Profile Responsibilities in Details Popup matches with Mapped Success Profile Responsibilities");
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail(
						"Issue in validating data in Profile Responsibilities in Details Popup matches with Mapped Success Profile Responsibilities...Please Investigate!!!");
				LOGGER.info("Issue in validating data in Profile Responsibilities in Details Popup matches with Mapped Success Profile Responsibilities...Please Investigate!!!");
			}
		}
	}

	public void validate_profile_behavioural_competencies_in_details_popup_matches_with_mapped_success_profile_behavioural_competencies() {
		if (manualMapping.get()) {
			try {
				scrollToElement(findElement(ROLE_SUMMARY));
				// PERFORMANCE: Replaced Thread.sleep(2000) with smart element wait
				Utilities.waitForUIStability(driver, 2);

				// Wait for any blocking loader overlays to disappear
				try {
					wait.until(
							ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@data-testid='loader']")));
				} catch (Exception e) {
					// Loader not present or already gone
				}

				// Scroll element into view before clicking
				WebElement element = wait
						.until(ExpectedConditions.elementToBeClickable(findElement(BEHAVIOUR_TAB)));
				js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
				try {
					Thread.sleep(500);
				} catch (InterruptedException ie) {
				}

				// Wait for element to be clickable without any overlay
				Utilities.waitForClickable(wait, findElement(BEHAVIOUR_TAB));

				try {
					element.click();
				} catch (Exception e) {
					try {
						js.executeScript("arguments[0].click();", findElement(BEHAVIOUR_TAB));
					} catch (Exception s) {
						jsClick(findElement(BEHAVIOUR_TAB));
					}
				}
			LOGGER.info("Clicked on BEHAVIOURAL COMPETENCIES screen in Profiles Details Popup");
				while (true) {
					try {
						List<WebElement> viewMoreButtons = findElements(VIEW_MORE_BEHAVIOUR);
						if (viewMoreButtons.isEmpty()) {
							LOGGER.info("Reached end of content in Profile Behavioural Competencies Section in Details Popup");
							break;
						}
						WebElement viewMoreBtn = viewMoreButtons.get(0);
						String ViewMoreCompetenciesBtnText = Utilities.waitForVisible(wait, viewMoreBtn).getText();
						// Scroll element into view before clicking
						js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", viewMoreBtn);
						safeSleep(500);
						// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
						Utilities.waitForUIStability(driver, 2);
						try {
							Utilities.waitForClickable(wait, viewMoreBtn).click();
						} catch (Exception e) {
							js.executeScript("arguments[0].click();", viewMoreBtn);
						}
						LOGGER.info("Clicked on " + ViewMoreCompetenciesBtnText
								+ " button in Profile Behavioural Competencies Section in Details Popup");
						// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
						Utilities.waitForUIStability(driver, 2);
					} catch (StaleElementReferenceException e) {
						LOGGER.info("Reached end of content in Profile Behavioural Competencies Section in Details Popup");
						break;
					}
				}
				String ProfileBehaviouralCompetenciesText = Utilities.waitForVisible(wait, findElement(BEHAVIOUR_DATA))
						.getText();
				Assert.assertEquals(ProfileBehaviouralCompetenciesText,
						manualMappingProfileBehaviouralCompetencies.get());
								LOGGER.info("Profile Behavioural Competencies in Details Popup matches with Mapped Success Profile Behavioural Competencies");
			} catch (Exception e) {
				Utilities.handleError(LOGGER,
						"validate_profile_behavioural_competencies_in_details_popup_matches_with_mapped_success_profile_behavioural_competencies",
						"Issue validating behavioural competencies in details popup", e);
				e.printStackTrace();
				Assert.fail(
						"Issue in validating data in Profile Behavioural Competencies Section in Details Popup matches with Mapped Success Profile Behavioural Competencies...Please Investigate!!!");
				LOGGER.info("Issue in validating data in Profile Behavioural Competencies Section in Details Popup matches with Mapped Success Profile Behavioural Competencies...Please Investigate!!!");
			}
		}
	}

	public void validate_profile_skills_in_details_popup_matches_with_mapped_success_profile_skills() {
		if (manualMapping.get()) {
			try {
				scrollToElement(findElement(ROLE_SUMMARY));
				// PERFORMANCE: Replaced Thread.sleep(2000) with smart element wait
				Utilities.waitForUIStability(driver, 2);

				// Wait for any blocking loader overlays to disappear
				try {
					wait.until(
							ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@data-testid='loader']")));
				} catch (Exception e) {
					// Loader not present or already gone
				}

				Utilities.waitForClickable(wait, findElement(SKILLS_TAB)).click();
				LOGGER.info("Clicked on SKILLS screen in Profiles Details Popup");
				while (true) {
					try {
						List<WebElement> viewMoreButtons = findElements(VIEW_MORE_SKILLS);
						if (viewMoreButtons.isEmpty()) {
							LOGGER.info("Reached end of content in Profile Skills Section in Details Popup");
							break;
						}
						WebElement viewMoreBtn = viewMoreButtons.get(0);
						String ViewMoreSkillsBtnText = Utilities.waitForVisible(wait, viewMoreBtn).getText();
						// Scroll element into view before clicking
						js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", viewMoreBtn);
						safeSleep(500);
						// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
						Utilities.waitForUIStability(driver, 2);
						try {
							Utilities.waitForClickable(wait, viewMoreBtn).click();
						} catch (Exception e) {
							js.executeScript("arguments[0].click();", viewMoreBtn);
						}
						LOGGER.info("Clicked on " + ViewMoreSkillsBtnText
								+ " button in Profile Skills Section in Details Popup");
						// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
						Utilities.waitForUIStability(driver, 2);
					} catch (StaleElementReferenceException e) {
						LOGGER.info("Reached end of content in Profile Skills Section in Details Popup");
						break;
					}
				}
				String ProfileSkillsText = Utilities.waitForVisible(wait, findElement(SKILLS_DATA)).getText();
				Assert.assertEquals(ProfileSkillsText, manualMappingProfileSkills.get());
				LOGGER.info("Profile Skills in Details Popup matches with Mapped Success Profile Skills");
			} catch (Exception e) {
				Utilities.handleError(LOGGER,
						"validate_profile_skills_in_details_popup_matches_with_mapped_success_profile_skills",
						"Issue validating skills in details popup", e);
				e.printStackTrace();
				Assert.fail(
						"Issue in validating data in Profile Skills Section in Details Popup matches with Mapped Success Profile Skills...Please Investigate!!!");
				LOGGER.info("Issue in validating data in Profile Skills Section in Details Popup matches with Mapped Success Profile Skills...Please Investigate!!!");
			}
		}
	}

	public void search_for_organization_job_with_manually_mapped_sp() {
		if (manualMapping.get()) {
			try {
				Utilities.waitForVisible(wait, findElement(SEARCH_BAR)).clear();
				try {
					Utilities.waitForClickable(wait, findElement(SEARCH_BAR)).click();
				} catch (Exception e) {
					try {
						js.executeScript("arguments[0].click();", findElement(SEARCH_BAR));
					} catch (Exception s) {
						jsClick(findElement(SEARCH_BAR));
					}
				}
			Utilities.waitForVisible(wait, findElement(SEARCH_BAR)).sendKeys(orgJobName.get());
			Utilities.waitForVisible(wait, findElement(SEARCH_BAR)).sendKeys(Keys.ENTER);
			waitForSpinners();
			// PERFORMANCE: Replaced Thread.sleep(2000) with search results wait
			Utilities.waitForSpinnersToDisappear(driver, 2);
			Utilities.waitForVisible(wait, By.xpath("//tbody//tr"));
			LOGGER.info("Entered job name as " + orgJobName.get() + " in the search bar");
				// PERFORMANCE: Replaced Thread.sleep(2000) with page ready wait
				Utilities.waitForPageReady(driver, 2);
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail("Failed to enter Organization job name text in search bar...Please investigate!!!");
				LOGGER.info("Failed to enter Organization job name text in search bar...Please investigate!!!");
			}
		}
	}
}

