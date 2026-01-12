package com.kfonetalentsuite.pageobjects.JobMapping;
import static com.kfonetalentsuite.pageobjects.JobMapping.BasePageObject.JobMappingPage.*;
import static com.kfonetalentsuite.pageobjects.JobMapping.BasePageObject.AddJobDataPage.*;
import static com.kfonetalentsuite.pageobjects.JobMapping.BasePageObject.Locators.SharedLocators.*;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import com.kfonetalentsuite.utils.common.ScreenshotHandler;
import com.kfonetalentsuite.utils.JobMapping.Utilities;

public class PO04_JobMappingPageComponents extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO04_JobMappingPageComponents.class);

	static String expectedPageTitle = "Korn Ferry Digital";
	static String expectedTitleHeader = "Review and Publish Your Mapped Job Profiles";

	public static String[] SEARCH_SUBSTRING_OPTIONS = { "An", "Ma", "Ac", "Sa", "En", "Ad", "As", "Co", "Te", "Di" };

	public static ThreadLocal<String> jobnamesubstring = ThreadLocal.withInitial(() -> "Ac");
	public static ThreadLocal<String> orgJobNameInRow1 = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<String> orgJobCodeInRow1 = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<String> orgJobGradeInRow1 = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<String> orgJobFunctionInRow1 = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<String> orgJobDepartmentInRow1 = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<String> intialResultsCount = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<String> updatedResultsCount = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<String> initialFilteredResultsCount = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<String> orgJobNameInRow2 = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<String> orgJobCodeInRow2 = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<String> matchedSuccessPrflName = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<Integer> loadedProfilesBeforeHeaderCheckboxClick = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<Integer> selectedProfilesAfterHeaderCheckboxClick = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<Integer> disabledProfilesCountInLoadedProfiles = ThreadLocal.withInitial(() -> 0);
	// LOCATORS - Using centralized Locators from BasePageObject where available
	
	// Page header - from Locators.JobMapping
	// Job profile rows - from Locators.JobMappingResults
	// Filters - from Locators.SearchAndFilters
	// Add More Jobs
	// Actions - from Locators.Actions and Locators.Table
	// View Other Matches & Comparison - from Locators.ComparisonPage
	// Success Modal - from Locators.Modals
	// JAM Logo and navigation
	public PO04_JobMappingPageComponents() {
		super();
	}

	public void user_should_verify_job_mapping_logo_is_displayed_on_screen() {
		try {
			Assert.assertTrue(waitForElement(JAM_LOGO).isDisplayed());
			LOGGER.info("Job Mapping logo is displayed on screen");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_should_verify_job_mapping_logo_is_displayed_on_screen", "Issue in displaying Job Mapping Logo", e);
		}
	}

	public void navigate_to_job_mapping_page_from_kfone_global_menu_in_pm() {
		try {
			String currentUrl = driver.getCurrentUrl();
			if (!currentUrl.contains("profilemanager")) {
				throw new RuntimeException("Navigation failed: Not on Profile Manager page. Current URL: " + currentUrl);
			}

			Utilities.waitForPageReady(driver, 5);

			int maxAttempts = 2;
			boolean menuClicked = false;

			for (int attempt = 1; attempt <= maxAttempts && !menuClicked; attempt++) {
				try {
					WebElement menuButton = waitForClickable(GLOBAL_NAV_MENU_BTN);
					scrollToElement(menuButton);

					if (!tryClickWithStrategies(menuButton)) {
						jsClick(menuButton);
					}
					menuClicked = true;
					Utilities.waitForUIStability(driver, 1);
				} catch (TimeoutException e) {
					if (attempt < maxAttempts) {
						driver.findElement(By.tagName("body")).sendKeys(Keys.ESCAPE);
						Thread.sleep(500);
					} else {
						throw e;
					}
				}
			}

			LOGGER.info("Clicked KFONE Global Menu");
			waitForSpinners();
			Utilities.waitForPageReady(driver, 3);

			try {
				WebElement menuContainer = driver.findElement(By.xpath("//div[contains(@class,'menu') or contains(@class,'drawer') or @role='dialog' or @role='menu']"));
				js.executeScript("arguments[0].scrollTop = arguments[0].scrollHeight;", menuContainer);
				Thread.sleep(500);
			} catch (Exception e) {
				LOGGER.warn("Could not scroll menu container");
			}

			WebElement jamButton = Utilities.waitForClickable(wait, BROWSE_RESOURCES_JAM);
			scrollToElement(jamButton);
			clickElement(jamButton);

			Utilities.waitForPageReady(driver, 10);

			String finalUrl = driver.getCurrentUrl();
			if (!finalUrl.contains("aiauto")) {
				throw new RuntimeException("Navigation failed. Expected job-mapping URL but got: " + finalUrl);
			}
			waitForBackgroundDataLoad();
			LOGGER.info("Successfully Navigated to Job Mapping screen");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "navigate_to_job_mapping_page_from_kfone_global_menu_in_pm",
					"Failed to navigate to Job Mapping page from KFONE Global Menu in Profile Manager", e);
		}
	}

	public void user_should_be_landed_on_job_mapping_page() {
		try {
			Utilities.waitForPageReady(driver, 5);
			Assert.assertTrue(waitForElement(PAGE_CONTAINER).isDisplayed());
			LOGGER.info("User landed on the JOB MAPPING page");
			waitForBackgroundDataLoad();
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_should_be_landed_on_job_mapping_page", "Issue in landing Job Mapping page", e);
		}
	}

	public void user_is_in_job_mapping_page() throws InterruptedException {
		
		Utilities.waitForPageReady(driver, 5);
		LOGGER.info("User is in JOB MAPPING page");
	}
	
	public void verify_title_header_is_correctly_displaying() throws InterruptedException {
		try {
			String actualTitleHeader = wait.until(ExpectedConditions.refreshed(ExpectedConditions.presenceOfElementLocated(PAGE_TITLE_HEADER))).getText();
			Utilities.waitForPageReady(driver, 3);
			Assert.assertEquals(actualTitleHeader, expectedTitleHeader);
			LOGGER.info("Title header '" + actualTitleHeader + "' is displaying");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_title_header_is_correctly_displaying", "Issue in verifying title header", e);
		}
	}

	public void verify_title_description_below_the_title_header() {
		try {
			String titleDesc = getElementText(PAGE_TITLE_DESC);
			LOGGER.info("Description below the title header: " + titleDesc);
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_title_description_below_the_title_header", "Issue in verifying title description", e);
		}
	}

	public void verify_organization_jobs_search_bar_text_box_is_clickable() {
		try {
			WebElement searchBar = waitForElement(SEARCH_BAR);
			scrollToElement(searchBar);
			clickElement(searchBar);
			LOGGER.info("Organization Jobs Search bar text box is clickable");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_organization_jobs_search_bar_text_box_is_clickable", "Failed to click search bar", e);
		}
	}

	public void verify_organization_jobs_search_bar_placeholder_text() {
		try {
			String placeholderText = driver.findElement(SEARCH_BAR).getText();
			LOGGER.info("Placeholder text inside Organization Jobs search bar is " + placeholderText);
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_organization_jobs_search_bar_placeholder_text", "Issue in verifying search bar placeholder", e);
		}
	}

	public void enter_job_name_substring_in_search_bar() {
		boolean foundResults = false;
		String selectedSubstring = SEARCH_SUBSTRING_OPTIONS[0];
		// Use specific JAM search bar locator for reliability
		By jamSearchBar = By.xpath("//input[@id='search-job-title-input-search-input']");
		waitForBackgroundDataLoad();
		try {
			LOGGER.info("Searching with dynamic substring (with fallback retry until results found)...");

			// Wait for any modal/loading overlay to disappear before searching
			try {
				new WebDriverWait(driver, Duration.ofSeconds(15))
					.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.modal-parent")));
				LOGGER.info("Modal overlay cleared, proceeding with search");
			} catch (Exception e) {
				LOGGER.debug("Modal wait completed or not present: {}", e.getMessage());
			}
			
			for (String substring : SEARCH_SUBSTRING_OPTIONS) {
				try {
					// Wait for page to stabilize before each search attempt
					Utilities.waitForPageReady(driver, 2);
					
					// Re-find search bar fresh each iteration to avoid stale element
					WebElement searchBar = new WebDriverWait(driver, Duration.ofSeconds(10))
						.until(ExpectedConditions.elementToBeClickable(jamSearchBar));
					
					// Scroll to search bar to ensure visibility
					((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", searchBar);
					safeSleep(300);
					
					// Use JS click to bypass any overlay issues
					jsClick(searchBar);
					safeSleep(200);
					
					// Use React-compatible method to clear and set value
					// React tracks input values internally, need to use native setter
					js.executeScript(
						"var input = arguments[0];" +
						"var nativeInputValueSetter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;" +
						"nativeInputValueSetter.call(input, '');" +
						"input.dispatchEvent(new Event('input', { bubbles: true }));",
						searchBar
					);
					safeSleep(300);
					
					// Re-find element fresh to avoid stale element
					WebElement freshSearchBar = new WebDriverWait(driver, Duration.ofSeconds(5))
						.until(ExpectedConditions.elementToBeClickable(jamSearchBar));
					
					// Use React-compatible method to set value
					js.executeScript(
						"var input = arguments[0];" +
						"var nativeInputValueSetter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;" +
						"nativeInputValueSetter.call(input, arguments[1]);" +
						"input.dispatchEvent(new Event('input', { bubbles: true }));",
						freshSearchBar, substring
					);
					
					// Wait for client-side filtering to complete (no Enter key - preserves View Published filter)
					safeSleep(2000);  // Allow debounce and filtering to complete
					Utilities.waitForSpinnersToDisappear(driver, 15);
					Utilities.waitForPageReady(driver, 3);
					safeSleep(1000); // Allow results to fully render

					String resultsCountText = "";
					int retries = 0;
					while (retries < 15) {
						safeSleep(500);
						try {
							resultsCountText = driver.findElement(JobMappingPage.SHOWING_RESULTS_COUNT).getText().trim();
							LOGGER.debug("Search '{}' attempt {}: {}", substring, retries + 1, resultsCountText);
							if (resultsCountText.contains("Showing") && !resultsCountText.startsWith("Showing 0")) {
								break;
							}
						} catch (Exception e) {
							// continue
						}
						retries++;
					}

					if (resultsCountText.contains("Showing") && !resultsCountText.startsWith("Showing 0")) {
						selectedSubstring = substring;
						jobnamesubstring.set(substring);
						foundResults = true;
						LOGGER.info("Search successful with substring '" + substring + "' - " + resultsCountText);
						break;
					} else {
						LOGGER.debug("No results for '{}', trying next substring...", substring);
					}
				} catch (Exception e) {
					LOGGER.warn("Error searching with '{}': {}", substring, e.getMessage());
				}
			}

			if (!foundResults) {
				// Set the substring anyway for downstream methods
				jobnamesubstring.set(selectedSubstring);
				LOGGER.info("No search substring returned results. Using: '" + selectedSubstring + "'");
				// Final wait to ensure page is stable
				Utilities.waitForSpinnersToDisappear(driver, 5);
				Utilities.waitForPageReady(driver, 2);
			}
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "enter_job_name_substring_in_search_bar", "Failed to enter job name substring", e);
		}
	}

	public void user_should_verify_organization_job_name_and_job_code_values_of_first_profile() {
		try {
			Utilities.waitForPageReady(driver, 2);
			String jobname1 = getElementText(JOB_NAME_PROFILE_1);
			orgJobNameInRow1.set(jobname1.split("-", 2)[0].trim());
			orgJobCodeInRow1.set(jobname1.split("-", 2)[1].trim().substring(1, jobname1.split("-", 2)[1].length() - 2));
			LOGGER.info("Job name of the first profile: " + orgJobNameInRow1.get());
			LOGGER.info("Job code of the first profile: " + orgJobCodeInRow1.get());
		} catch (Exception e) {
			Assert.fail("Issue in verifying job name matching profile in Row1");
		}
	}

	public void user_should_verify_organization_job_grade_and_department_values_of_first_profile() {
		try {
			String jobGrade = getElementText(JOB_GRADE_PROFILE_1);
			if (jobGrade.contentEquals("-") || jobGrade.isEmpty()) jobGrade = "NULL";
			orgJobGradeInRow1.set(jobGrade);
			LOGGER.info("Grade value of Organization Job first profile: " + orgJobGradeInRow1.get());
		} catch (Exception e) {
			Assert.fail("Issue in Verifying Organization Job Grade value");
		}

		try {
			String jobDepartment = getElementText(JOB_DEPARTMENT_PROFILE_1);
			if (jobDepartment.contentEquals("-") || jobDepartment.isEmpty()) jobDepartment = "NULL";
			orgJobDepartmentInRow1.set(jobDepartment);
			LOGGER.info("Department value of Organization Job first profile: " + orgJobDepartmentInRow1.get());
		} catch (Exception e) {
			Assert.fail("Issue in Verifying Organization Job Department value");
		}
	}

	public void user_should_verify_organization_job_function_or_sub_function_of_first_profile() {
		try {
			String jobFunction = getElementText(JOB_FUNCTION_PROFILE_1);
			if (jobFunction.contentEquals("- | -") || jobFunction.contentEquals("-") || jobFunction.isEmpty()) {
				jobFunction = "NULL | NULL";
			} else if (jobFunction.endsWith("-") || jobFunction.endsWith("| -") || !jobFunction.contains("|")) {
				jobFunction = jobFunction + " | NULL";
			}
			orgJobFunctionInRow1.set(jobFunction);
			LOGGER.info("Function/Sub-function values of first profile: " + orgJobFunctionInRow1.get());
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_should_verify_organization_job_function_or_sub_function_of_first_profile", "Issue verifying function", e);
		}
	}

	public void click_on_matched_profile_of_job_in_first_row() {
		try {
			waitForSpinners();
			WebElement matchedProfile = waitForClickable(JOB_1_MATCHED_PROFILE);
			matchedSuccessPrflName.set(matchedProfile.getText());
			scrollToElement(matchedProfile);
			clickElement(matchedProfile);
			LOGGER.info("Clicked on Matched Profile '" + matchedSuccessPrflName.get() + "'");
			waitForSpinners();
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "click_on_matched_profile_of_job_in_first_row", "Issue clicking matched profile", e);
		}
	}

	public void verify_profile_details_popup_is_displayed() {
		try {
			waitForSpinners();
			Assert.assertTrue(waitForElement(PROFILE_DETAILS_POPUP_HEADER).isDisplayed());
			LOGGER.info("Profile details popup is displayed on screen");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_profile_details_popup_is_displayed", "Issue displaying popup", e);
		}
	}

	public void click_on_close_button_in_profile_details_popup() {
		try {
			clickElement(PROFILE_DETAILS_CLOSE_BTN);
			LOGGER.info("Clicked on close button in Profile details popup");
			waitForSpinners();
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "click_on_close_button_in_profile_details_popup", "Issue closing popup", e);
		}
	}

	public void click_on_filters_dropdown_button() {
		try {
			scrollToTop();
			clickElement(FILTERS_BTN);
			waitForSpinners();
			Utilities.waitForPageReady(driver, 2);
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "click_on_filters_dropdown_button", "Issue clicking filters button", e);
		}
	}

	public void verify_options_available_inside_filters_dropdown() {
		Assert.assertTrue(waitForElement(JobMappingPage.FILTER_OPTIONS).isDisplayed());
		try {
			Assert.assertEquals(getElementText(FILTER_OPTION_1), "Grades");
			Assert.assertEquals(getElementText(FILTER_OPTION_2), "Departments");
			Assert.assertEquals(getElementText(FILTER_OPTION_3), "Functions / Subfunctions");
			Assert.assertEquals(getElementText(FILTER_OPTION_4), "Mapping Status");
			LOGGER.info("Options inside Filter dropdown verified");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_options_available_inside_filters_dropdown", "Issue verifying filter options", e);
		}

		try {
			clickElement(FILTER_OPTION_3);
			Utilities.waitForPageReady(driver, 5);
			waitForElement(SEARCH_BAR_FILTER_OPTION_3);
			clickElement(SEARCH_BAR_FILTER_OPTION_3);
			LOGGER.info("Search bar inside Functions / Subfunctions filter option is available and clickable");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_options_available_inside_filters_dropdown", "Issue verifying filter search bar", e);
		}
	}

	public void close_the_filters_dropdown() {
		try {
			clickElement(FILTERS_BTN);
			Thread.sleep(500);
			LOGGER.info("Closed Filters dropdown");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "close_filters_dropdown", "Failed to close Filters dropdown", e);
		}
	}

	public void user_should_see_add_more_jobs_button_is_displaying() {
		try {
			String btnText = getElementText(ADD_MORE_JOBS_BTN);
			LOGGER.info(btnText + " button is displaying");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_should_see_add_more_jobs_button_is_displaying", "Issue displaying add more jobs button", e);
		}
	}

	public void verify_add_more_jobs_button_is_clickable() throws InterruptedException {
		try {
			driver.manage().deleteAllCookies();
			Utilities.waitForPageReady(driver, 5);

			WebElement button = waitForClickable(ADD_MORE_JOBS_BTN);
			scrollToElement(button);
			clickElement(button);

			Utilities.waitForPageReady(driver, 10);
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_add_more_jobs_button_is_clickable", "Issue clicking Add more jobs button", e);
		}
	}

	public void verify_user_landed_on_add_more_jobs_page() {
		try {
			String headerText = getElementText(ADD_MORE_JOBS_PAGE_HEADER);
			Assert.assertEquals(headerText, "Add Job Data");
			LOGGER.info("Add More Jobs landing page is Verified");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_user_landed_on_add_more_jobs_page", "Issue verifying Add more jobs page", e);
		}
	}

	public void close_add_more_jobs_page() {
		try {
			clickElement(ADD_MORE_JOBS_CLOSE_BTN);
			LOGGER.info("Clicked on Add more jobs Close button(X)");
			waitForSpinners();
			
			// AUTOMATION FIX: Handle "Keep working?" popup (method now in BasePageObject)
			dismissKeepWorkingPopupIfPresent();
			
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "close_add_more_jobs_page", "Issue closing Add more jobs page", e);
		}
	}

	public void user_should_verify_publish_selected_profiles_button_is_disabled() {
		try {
			Assert.assertTrue(!waitForElement(PUBLISH_SELECTED_BTN).isEnabled());
			LOGGER.info("Publish Selected Profiles button is disabled");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_should_verify_publish_selected_profiles_button_is_disabled", "Issue verifying button disabled", e);
		}
	}

	public void click_on_header_checkbox_to_select_loaded_job_profiles_in_job_mapping_screen() {
		try {
			Utilities.waitForPageReady(driver, 1);

			String resultsCountText = "";
			int retryAttempts = 0;

			while (retryAttempts < 3) {
				try {
					resultsCountText = Utilities.waitForPresent(wait, JobMappingPage.SHOWING_RESULTS_COUNT).getText();
					break;
				} catch (Exception e) {
					retryAttempts++;
					if (retryAttempts >= 3) throw e;
					Utilities.waitForPageReady(driver, 2);
					Thread.sleep(1000);
				}
			}

			if (resultsCountText.contains("Showing") && resultsCountText.contains("of")) {
				String[] parts = resultsCountText.split("\\s+");
				loadedProfilesBeforeHeaderCheckboxClick.set(Integer.parseInt(parts[1]));
			}

			clickElement(HEADER_CHECKBOX);

			var allCheckboxes = driver.findElements(ROW_CHECKBOXES);
			int checkboxesToCheck = Math.min(allCheckboxes.size(), loadedProfilesBeforeHeaderCheckboxClick.get());
			int profilesCount = loadedProfilesBeforeHeaderCheckboxClick.get();
			disabledProfilesCountInLoadedProfiles.set(0);

			for (int i = 0; i < checkboxesToCheck; i++) {
				try {
					if (!allCheckboxes.get(i).isEnabled()) {
						disabledProfilesCountInLoadedProfiles.set(disabledProfilesCountInLoadedProfiles.get() + 1);
						profilesCount--;
					}
				} catch (Exception e) {
					// Continue
				}
			}

			selectedProfilesAfterHeaderCheckboxClick.set(profilesCount);
			LOGGER.info("Clicked on header checkbox and selected " + selectedProfilesAfterHeaderCheckboxClick.get() + " job profiles");
		} catch (Exception e) {
			Assert.fail("Issue clicking header checkbox to select job profiles");
		}
	}

	public void user_should_verify_publish_selected_profiles_button_is_enabled() {
		try {
			Utilities.waitForPageReady(driver, 2);
			Thread.sleep(500);
			Assert.assertTrue(driver.findElement(PUBLISH_SELECTED_BTN).isEnabled(), "Publish button should be enabled");
			LOGGER.info("Publish Selected Profiles button is enabled");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_should_verify_publish_selected_profiles_button_is_enabled", "Issue verifying button enabled", e);
		}
	}

	public void user_should_uncheck_header_checkbox_to_deselect_selected_job_profiles() {
		try {
			clickElement(HEADER_CHECKBOX);
			LOGGER.info("Clicked on header checkbox and Deselected all job profiles");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_should_uncheck_header_checkbox_to_deselect_selected_job_profiles", "Issue deselecting profiles", e);
		}
	}

	public void click_on_checkbox_of_first_job_profile() {
		try {
			WebElement jobNameElement = waitForElement(JOB_NAME_PROFILE_1, 10);
			String job1NameText = jobNameElement.getText();
			
			if (job1NameText == null || job1NameText.trim().isEmpty()) {
				throw new Exception("Job name element text is null or empty");
			}
			
			// Extract job name (before the dash) and job code (within parentheses)
			String[] parts = job1NameText.split("-", 2);
			String extractedJobName = parts[0].trim();
			String extractedJobCode = "NOT_SET";
			
			// Extract job code from the second part (e.g., "(ABC123)" -> "ABC123")
			if (parts.length > 1) {
				String codePart = parts[1].trim();
				if (codePart.startsWith("(") && codePart.contains(")")) {
					extractedJobCode = codePart.substring(1, codePart.indexOf(")")).trim();
				}
			}
			
			if (extractedJobName.isEmpty() || extractedJobName.equals("NOT_SET")) {
				throw new Exception("Failed to extract valid job name from: " + job1NameText);
			}
			
			// Store job name and code in ThreadLocal variables
			orgJobNameInRow1.set(extractedJobName);
			orgJobCodeInRow1.set(extractedJobCode);
			clickElement(PROFILE_1_CHECKBOX);
			LOGGER.info("Clicked on checkbox of First job profile: " + orgJobNameInRow1.get());
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "click_on_checkbox_of_first_job_profile", "Issue clicking first profile checkbox", e);
		}
	}

	public void click_on_checkbox_of_second_job_profile() {
		try {
			WebElement jobNameElement = waitForElement(JOB_NAME_PROFILE_2, 10);
			String job1NameText = jobNameElement.getText();
			
			if (job1NameText == null || job1NameText.trim().isEmpty()) {
				throw new Exception("Job name element text is null or empty");
			}
			
			// Extract job name (before the dash) and job code (within parentheses)
			String[] parts = job1NameText.split("-", 2);
			String extractedJobName = parts[0].trim();
			String extractedJobCode = "NOT_SET";
			
			// Extract job code from the second part (e.g., "(ABC123)" -> "ABC123")
			if (parts.length > 1) {
				String codePart = parts[1].trim();
				if (codePart.startsWith("(") && codePart.contains(")")) {
					extractedJobCode = codePart.substring(1, codePart.indexOf(")")).trim();
				}
			}
			
			if (extractedJobName.isEmpty() || extractedJobName.equals("NOT_SET")) {
				throw new Exception("Failed to extract valid job name from: " + job1NameText);
			}
			
			// Store job name and code in ThreadLocal variables
			orgJobNameInRow2.set(extractedJobName);
			orgJobCodeInRow2.set(extractedJobCode);
			clickElement(PROFILE_2_CHECKBOX);
			LOGGER.info("Clicked on checkbox of Second job profile: " + orgJobNameInRow2.get());
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "click_on_checkbox_of_second_job_profile", "Issue clicking second profile checkbox", e);
		}
	}

	public void click_on_publish_selected_profiles_button() {
		try {
			clickElement(PUBLISH_SELECTED_BTN);
			Utilities.waitForSpinnersToDisappear(driver, 30);
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "click_on_publish_selected_profiles_button", "Issue clicking Publish Selected button", e);
		}
	}

	public void verify_job_profiles_count_is_displaying_on_the_page() {
		int retryAttempts = 0;
		try {
			Utilities.waitForPageReady(driver, 1);

			String resultsCountText = "";
			while (retryAttempts < 5) {
				try {
					resultsCountText = Utilities.waitForPresent(wait, JobMappingPage.SHOWING_RESULTS_COUNT).getText();
					break;
				} catch (Exception e) {
					retryAttempts++;
					if (retryAttempts >= 5) throw e;
					Utilities.waitForPageReady(driver, 2);
					Thread.sleep(1000);
				}
			}

			intialResultsCount.set(resultsCountText);
			initialFilteredResultsCount.set(null);
			LOGGER.info("Initially " + resultsCountText + " of job profiles");
		} catch (Exception e) {
			Assert.fail("Failed to verify job profiles count after " + retryAttempts + " attempts");
		}
	}

	public void scroll_page_to_view_more_job_profiles() throws InterruptedException {
		try {
			waitForSpinners();
			scrollToBottom();
			safeSleep(3000);
			Utilities.waitForSpinnersToDisappear(driver, 5);
			Utilities.waitForPageReady(driver, 2);
			safeSleep(1000);
			LOGGER.info("Scrolled page down to view more job profiles");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "scroll_page_to_view_more_job_profiles", "Issue scrolling page", e);
		}
	}

	public void user_should_verify_count_of_job_profiles_is_correctly_showing_on_top_of_job_profiles_listing_table() throws InterruptedException {
		try {
			scrollToTop();
			waitForSpinners();
			Utilities.waitForPageReady(driver, 5);

			String resultsCountText2 = "";
			int retryAttempts = 0;

			while (retryAttempts < 10) {
				try {
					String currentText = driver.findElement(JobMappingPage.SHOWING_RESULTS_COUNT).getText().trim();
					if (!currentText.isEmpty() && !currentText.equals(intialResultsCount.get())) {
						resultsCountText2 = currentText;
						break;
					}
					Thread.sleep(retryAttempts == 0 ? 500 : 200);
					retryAttempts++;
				} catch (Exception e) {
					retryAttempts++;
					Thread.sleep(200);
				}
			}

			if (resultsCountText2.isEmpty()) {
				resultsCountText2 = Utilities.waitForPresent(wait, JobMappingPage.SHOWING_RESULTS_COUNT).getText();
			}

			updatedResultsCount.set(resultsCountText2);

			if (initialFilteredResultsCount.get() == null && !resultsCountText2.equals(intialResultsCount.get())) {
				initialFilteredResultsCount.set(resultsCountText2);
			}

			if (resultsCountText2.contains("Showing 0 of")) {
				PO07_Screen1SearchResults.setHasSearchResults(false);
				LOGGER.info("Profile Results: " + resultsCountText2 + " - No results found");
				return;
			}

			PO07_Screen1SearchResults.setHasSearchResults(true);
			LOGGER.info("Profile Results count updated: " + resultsCountText2);
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_should_verify_count_of_job_profiles_is_correctly_showing_on_top_of_job_profiles_listing_table", "Issue verifying results count", e);
		}
	}

	public void user_should_verify_view_published_toggle_button_is_displaying() {
		Assert.assertTrue(waitForElement(VIEW_PUBLISHED_TOGGLE).isDisplayed());
		LOGGER.info("View published toggle button is displaying on screen");
	}

	public void click_on_toggle_button_to_turn_on() throws InterruptedException {
		try {
			clickElement(VIEW_PUBLISHED_TOGGLE);
			LOGGER.info("View published toggle button is turned ON");
			waitForSpinners();
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "click_on_toggle_button_to_turn_on", "Issue clicking toggle button ON", e);
		}
	}

	public void user_should_verify_published_jobs_are_displaying_in_the_listing_table() {
		try {
			waitForElement(PUBLISHED_BTN);
			LOGGER.info("Published jobs are displaying in the profile table");

			Assert.assertFalse(driver.findElement(PUBLISHED_BTN).isEnabled(), "Published button should be disabled");
			LOGGER.info("Published button is disabled");
		} catch (TimeoutException e) {
			try {
				waitForElement(NO_DATA_AVAILABLE);
				LOGGER.info("No Jobs were published yet");
			} catch (Exception ex) {
				Utilities.handleError(LOGGER, "user_should_verify_published_jobs_are_displaying_in_the_listing_table", "Issue verifying published jobs", ex);
			}
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_should_verify_published_jobs_are_displaying_in_the_listing_table", "Issue verifying published jobs", e);
		}
	}

	public void click_on_toggle_button_to_turn_off() throws InterruptedException {
		try {
			clickElement(VIEW_PUBLISHED_TOGGLE);
			LOGGER.info("View published toggle button is turned OFF");
			Thread.sleep(2000);
			Utilities.waitForPageReady(driver, 10);
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "click_on_toggle_button_to_turn_off", "Issue clicking toggle button OFF", e);
		}
	}

	public void user_should_verify_unpublished_jobs_are_displaying_in_the_listing_table() {
		try {
			WebElement publishButton = waitForElement(PUBLISH_BTN);
			Assert.assertTrue(publishButton.isDisplayed() && publishButton.isEnabled());
			LOGGER.info("Unpublished jobs with enabled Publish button are displaying");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_should_verify_unpublished_jobs_are_displaying_in_the_listing_table", "Issue verifying unpublished jobs", e);
		}
	}

	public void user_should_verify_organization_jobs_table_title_and_headers() {
		try {
			Assert.assertEquals(getElementText(TABLE_1_TITLE), "Organization jobs");
			LOGGER.info("Organization jobs table title verified");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_should_verify_organization_jobs_table_title_and_headers", "Issue verifying table title", e);
		}
		try {
			Assert.assertEquals(getElementText(TABLE_1_HEADER_1), "NAME / JOB CODE");
			Assert.assertEquals(getElementText(TABLE_1_HEADER_2), "GRADE");
			Assert.assertEquals(getElementText(TABLE_1_HEADER_3), "DEPARTMENT");
			LOGGER.info("Organization jobs table headers verified");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_should_verify_organization_jobs_table_title_and_headers", "Issue verifying table headers", e);
		}
	}

	public void user_should_verify_matched_success_profiles_table_title_and_headers() {
		try {
			Assert.assertEquals(getElementText(TABLE_2_TITLE), "Matched success profiles");
			LOGGER.info("Matched success profiles table title verified");
		} catch (Exception e) {
			Assert.fail("Issue verifying Matched success profiles table title");
		}
		try {
			Assert.assertEquals(getElementText(TABLE_2_HEADER_1), "MATCHED PROFILE");
			Assert.assertEquals(getElementText(TABLE_2_HEADER_2), "GRADE");
			Assert.assertEquals(getElementText(TABLE_2_HEADER_3), "TOP RESPONSIBILITIES");
			Assert.assertEquals(getElementText(TABLE_2_HEADER_4), "LEVEL / SUBLEVEL");
			Assert.assertEquals(getElementText(TABLE_2_HEADER_5), "FUNCTION / SUBFUNCTION");
			LOGGER.info("Matched success profiles table headers verified");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_should_verify_matched_success_profiles_table_title_and_headers", "Issue verifying table headers", e);
		}
	}

	public void user_should_verify_view_other_matches_button_on_matched_success_profile_is_displaying() {
		try {
			String viewOtherMatchesText = getElementText(JOB_1_VIEW_OTHER_MATCHES);
			LOGGER.info("Button with text " + viewOtherMatchesText + " is displaying on the matched success profile");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_should_verify_view_other_matches_button_on_matched_success_profile_is_displaying", "Issue displaying button", e);
		}
	}

	public void click_on_view_other_matches_button() {
		try {
			By viewOtherMatchesLocator = By.xpath("//tbody//tr[" + PO12_RecommendedProfileDetails.rowNumber.get() + "]//button[not(contains(@id,'publish'))]");
			clickElement(viewOtherMatchesLocator);
			LOGGER.info("Clicked on View Other Matches button on job: " + PO12_RecommendedProfileDetails.orgJobName.get());
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "click_on_view_other_matches_button", "Issue clicking View Other Matches button", e);
		}
	}

	public void verify_user_landed_on_job_compare_page() throws InterruptedException {
		try {
			waitForSpinners();
			Assert.assertEquals(getElementText(COMPARE_SELECT_HEADER), "Which profile do you want to use for this job?");
			LOGGER.info("User landed on the Job Compare page");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_user_landed_on_job_compare_page", "Issue landing Job Compare page", e);
		}
		driver.navigate().back();
		Utilities.waitForPageReady(driver, 3);
		LOGGER.info("Navigated back to Job Mapping page");
		waitForBackgroundDataLoad();
	}

	public void user_should_verify_publish_button_on_matched_success_profile_is_displaying_and_clickable() {
		try {
			waitForSpinners();
			Utilities.waitForPageReady(driver, 3);
			clickElement(JOB_1_PUBLISH_BTN);
			waitForSpinners();
			LOGGER.info("Publish button on matched success profile is displaying and clicked");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_should_verify_publish_button_on_matched_success_profile_is_displaying_and_clickable", "Issue clicking Publish button", e);
		}
	}

	public void user_should_get_success_profile_published_popup() {
		try {
			waitForSpinners();
			Utilities.waitForPageReady(driver, 3);
			
			WebElement headerElement = null;
			String successHeaderText = "";

			// Try multiple locators with SHORT timeouts (10 sec each, not 60+)
			LOGGER.info("Waiting for success popup...");
			
			// Strategy 1: Primary locator
			try {
				headerElement = Utilities.waitForVisible(wait, PUBLISHED_SUCCESS_HEADER);
				successHeaderText = headerElement.getText().trim();
				LOGGER.debug("Found popup with primary locator");
			} catch (Exception e1) {
				LOGGER.debug("Primary locator failed (10s), trying fallback...");
				
				// Strategy 2: BasePageObject locator
				try {
					headerElement = Utilities.waitForVisible(wait, SUCCESS_MODAL_HEADER);
					successHeaderText = headerElement.getText().trim();
					LOGGER.debug("Found popup with Locators.Modals locator");
				} catch (Exception e2) {
					LOGGER.debug("Fallback 1 failed (10s), trying flexible locator...");
					
					// Strategy 3: Flexible text-based locator
					try {
						By flexibleLocator = By.xpath("//*[contains(text(),'Success') and contains(text(),'Published')]");
						headerElement = Utilities.waitForVisible(wait, flexibleLocator);
						successHeaderText = headerElement.getText().trim();
						LOGGER.debug("Found popup with flexible locator");
					} catch (Exception e3) {
						// All strategies failed - capture screenshot and fail
						LOGGER.error("All popup locators failed after 30 seconds total");
						ScreenshotHandler.captureScreenshotWithDescription("success_popup_not_found");
						Assert.fail("Success popup not found after publishing (30s timeout). Check if popup appears or locators need update.");
					}
				}
			}

			// Verify we found the right popup
			Assert.assertTrue(successHeaderText.toLowerCase().contains("success") && 
					successHeaderText.toLowerCase().contains("published"),
					"Expected success popup header to contain 'Success' and 'Published'. Actual: '" + successHeaderText + "'");

			// Get message text (optional)
			String successMsgText = "";
			try {
				successMsgText = Utilities.waitForVisible(wait, PUBLISHED_SUCCESS_MSG).getText();
			} catch (Exception msgEx) {
				LOGGER.debug("Could not get success message text");
			}

			LOGGER.info("Success popup displayed - Header: '" + successHeaderText + "', Message: '" + successMsgText + "'");

		} catch (AssertionError ae) {
			throw ae;
		} catch (Exception e) {
			ScreenshotHandler.captureScreenshotWithDescription("success_popup_error");
			Utilities.handleError(LOGGER, "user_should_get_success_profile_published_popup", "Issue displaying success popup", e);
			Assert.fail("Failed to verify success popup: " + e.getMessage());
		}
	}

	public void close_the_success_profile_published_popup() {
		try {
			clickElement(PUBLISHED_SUCCESS_CLOSE_BTN);
			Utilities.waitForPageReady(driver, 2);
			LOGGER.info("Successfully closed Success Profile Published popup");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "close_the_success_profile_published_popup", "Issue closing success popup", e);
		}
	}

	public void clear_search_bar_in_job_mapping_page() {
		try {
			// Wait for any modal/loading overlay to disappear before interacting
		try {
			new WebDriverWait(driver, Duration.ofSeconds(15))
				.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.modal-parent")));
		} catch (Exception ignored) {
			// Modal may not be present, continue
		}
		
		WebElement searchBar = driver.findElement(SEARCH_BAR);
		scrollToElement(searchBar);
		
		// Use clearSearchBar helper (handles all edge cases)
		clearSearchBar(SEARCH_BAR);
		Utilities.waitForPageReady(driver, 2);
		
		LOGGER.info("Search bar successfully cleared in Job Mapping page");
	} catch (Exception e) {
			Utilities.handleError(LOGGER, "clear_search_bar_in_job_mapping_page", "Issue clearing search bar", e);
		}
	}
}

