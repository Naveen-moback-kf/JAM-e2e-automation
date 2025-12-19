package com.kfonetalentsuite.pageobjects.JobMapping;

import java.time.Duration;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.PageObjectHelper;

public class PO18_HCMSyncProfilesTab_PM extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO18_HCMSyncProfilesTab_PM.class);

	// Dynamic search profile names with fallback options
	// The system will try each profile name in order until results are found
	public static String[] SEARCH_PROFILE_NAME_OPTIONS = { "Architect", "Manager", "Analyst", "Senior", "Engineer",
			"Administrator", "Assistant", "Coordinator", "Technician", "Director" };

	// THREAD-SAFE: Each thread gets its own isolated state for parallel execution
	public static ThreadLocal<String> jobProfileName = ThreadLocal.withInitial(() -> "Architect"); 
	public static ThreadLocal<String> intialResultsCount = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<String> updatedResultsCount = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<String> orgJobNameInRow1 = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<Integer> profilesCount = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<Boolean> isProfilesCountComplete = ThreadLocal.withInitial(() -> true); 
	public static ThreadLocal<String> jobname1 = ThreadLocal.withInitial(() -> null);
	public static ThreadLocal<String> jobname2 = ThreadLocal.withInitial(() -> null);
	public static ThreadLocal<String> jobname3 = ThreadLocal.withInitial(() -> null);

	// Variables for Feature 33 - Select Loaded Profiles validation
	// THREAD-SAFE: Each thread maintains its own counts
	public static ThreadLocal<Integer> loadedProfilesBeforeHeaderCheckboxClick = ThreadLocal.withInitial(() -> 0); 
	public static ThreadLocal<Integer> selectedProfilesAfterHeaderCheckboxClick = ThreadLocal.withInitial(() -> 0); 
	public static ThreadLocal<Integer> disabledProfilesCountInLoadedProfiles = ThreadLocal.withInitial(() -> 0); 

	public PO18_HCMSyncProfilesTab_PM() {
		super();
	}

	// =============================================
	// HELPER METHODS - Reusable click and wait patterns
	// =============================================

	/**
	 * Click element with fallback pattern: standard click → JS click → jsClick()
	 * Eliminates repeated nested try-catch blocks throughout the class
	 */
	private void clickWithFallback(By locator) {
		WebElement element = findElement(locator);
		try {
			wait.until(ExpectedConditions.elementToBeClickable(element)).click();
		} catch (Exception e) {
			try {
				js.executeScript("arguments[0].click();", element);
			} catch (Exception ex) {
				jsClick(element);
			}
		}
	}

	/**
	 * Wait for page stability - consolidated wait method
	 * Replaces: waitForPageReady() + waitForSpinners() + safeSleep() pattern
	 */
	private void waitForPageStability(int timeoutSeconds) {
		PerformanceUtils.waitForPageReady(driver, timeoutSeconds);
	}

	/**
	 * Scroll to element and wait for stability
	 */
	private void scrollToAndWait(WebElement element) {
		js.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});", element);
		safeSleep(300); // Brief pause for scroll animation
	}
	
	private static final By MENU_BTN = By.xpath("//span[contains(text(),'Hi')]//following::img[1]");
	private static final By HOME_MENU_BTN = By.xpath("//*[*[contains(text(),'Hi, ')]]/div[2]/img");
	private static final By PM_BTN = By.xpath("//a[contains(text(),'Profile Manager')]");
	private static final By PM_HEADER = Locators.HCMSyncProfiles.PROFILE_MANAGER_HEADER;
	private static final By HCM_SYNC_PROFILES_HEADER = Locators.HCMSyncProfiles.HCM_SYNC_TAB;
	private static final By HCM_SYNC_PROFILES_TITLE = Locators.HCMSyncProfiles.SYNC_PROFILES_TITLE;
	private static final By HCM_SYNC_PROFILES_TITLE_DESC = By.xpath("//p[contains(text(),'Select a job profile')]");
	private static final By HCM_SYNC_PROFILES_SEARCHBAR = Locators.PMScreen.SEARCH_BAR;
	private static final By HCM_SYNC_PROFILES_JOB_ROW1 = Locators.JobMappingResults.HCM_JOB_ROW_1;
	private static final By NO_SP_MSG = By.xpath("//div[contains(text(),'no Success Profiles')]");
	private static final By NO_RESULTS_MESSAGE = By.xpath("//*[contains(text(),'no results available') or contains(text(),'There are no results')]");
	private static final By HCM_SYNC_PROFILES_JOB_ROW2 = By.xpath("//tbody//tr[2]//td//div//span[1]//a");
	private static final By HCM_SYNC_PROFILES_JOB_ROW3 = By.xpath("//tbody//tr[3]//td//div//span[1]//a");
	private static final By SP_DETAILS_PAGE_TEXT = By.xpath("//span[contains(text(),'Select your view')]");
	private static final By SHOWING_JOB_RESULTS_COUNT = By.xpath("//div[contains(text(),'Showing')]");
	private static final By FILTERS_DROPDOWN_BTN = By.xpath("//span[text()='Filters']");
	private static final By FILTER_OPTIONS = By.xpath("//*[@class='accordion']");
	private static final By KF_GRADE_FILTERS_DROPDOWN = By.xpath("//thcl-expansion-panel-header//div[text()=' KF Grade ']");
	private static final By KF_GRADE_ALL_CHECKBOXES = By.xpath("//thcl-expansion-panel[contains(@class,'sp-search-filter-expansion-panel')][1]//kf-checkbox");
	private static final By KF_GRADE_ALL_LABELS = By.xpath("//thcl-expansion-panel[contains(@class,'sp-search-filter-expansion-panel')][1]//div[contains(@class,'body-text')]");
	private static final By CLOSE_APPLIED_FILTER = By.xpath("//div[contains(@class,'applied-filters')]//span//kf-icon");
	private static final By LEVELS_FILTERS_DROPDOWN = By.xpath("//thcl-expansion-panel-header//div[text()=' Levels ']");
	private static final By LEVELS_ALL_CHECKBOXES = By.xpath("//thcl-expansion-panel[contains(@class,'sp-search-filter-expansion-panel')][2]//kf-checkbox");
	private static final By LEVELS_ALL_LABELS = By.xpath("//thcl-expansion-panel[contains(@class,'sp-search-filter-expansion-panel')][2]//div[contains(@class,'body-text')]");
	private static final By FUNCTIONS_SUBFUNCTIONS_FILTERS_DROPDOWN = By.xpath("//thcl-expansion-panel-header//div[text()=' Functions / Subfunctions ']");
	private static final By FUNCTIONS_SUBFUNCTIONS_ALL_CHECKBOXES = By.xpath("//thcl-expansion-panel[contains(@class,'sp-search-filter-expansion-panel')][3]//thcl-expansion-panel//kf-checkbox");
	private static final By FUNCTIONS_SUBFUNCTIONS_ALL_LABELS = By.xpath("//thcl-expansion-panel[contains(@class,'sp-search-filter-expansion-panel')][3]//thcl-expansion-panel-header//span[contains(@class,'text-break')]");
	private static final By PROFILE_STATUS_FILTERS_DROPDOWN = By.xpath("//thcl-expansion-panel-header//div[text()=' Profile Status ']");
	private static final By PROFILE_STATUS_ALL_CHECKBOXES = By.xpath("//thcl-expansion-panel-header//div[text()=' Profile Status ']/ancestor::thcl-expansion-panel//kf-checkbox");
	private static final By PROFILE_STATUS_ALL_LABELS = By.xpath("//thcl-expansion-panel-header//div[text()=' Profile Status ']/ancestor::thcl-expansion-panel//span[contains(@class,'wrapped') or contains(@class,'body-text')]");
	private static final By CLEAR_ALL_FILTERS_BTN = By.xpath("//a[contains(text(),'Clear All')]");
	private static final By TABLE_HEADER1 = By.xpath("//thead//tr//div[@kf-sort-header='name']//div");
	private static final By TABLE_HEADER2 = By.xpath("//thead//tr//div//div[text()=' Status ']");
	private static final By TABLE_HEADER3 = By.xpath("//thead//tr//div//div[text()=' kf grade ']");
	private static final By TABLE_HEADER4 = By.xpath("//thead//tr//div//div[text()=' Level ']");
	private static final By TABLE_HEADER5 = By.xpath("//thead//tr//div//div[text()=' Function ']");
	private static final By TABLE_HEADER6 = By.xpath("//thead//tr//div//div[text()=' Created By ']");
	private static final By TABLE_HEADER7 = By.xpath("//thead//tr//div//div[text()=' Last Modified ']");
	private static final By TABLE_HEADER8 = By.xpath("//thead//tr//div//div[text()=' Export status ']");
	private static final By DOWNLOAD_BTN = By.xpath("//button[contains(@class,'border-button')]");
	private static final By TABLE_HEADER_CHECKBOX = By.xpath("//thead//tr//div//kf-checkbox");
	private static final By PROFILE1_CHECKBOX = By.xpath("//tbody//tr[1]//div[1]//kf-checkbox");
	private static final By PROFILE2_CHECKBOX = By.xpath("//tbody//tr[2]//div[1]//kf-checkbox");
	private static final By PROFILE3_CHECKBOX = By.xpath("//tbody//tr[3]//div[1]//kf-checkbox");
	private static final By SYNC_WITH_HCM_BTN = By.xpath("//button[contains(@class,'custom-export')] | //*[contains(text(),'Sync with HCM')]");
	private static final By SYNC_WITH_HCM_SUCCESS_POPUP_TEXT = By.xpath("//div[@class='p-toast-detail']");
	private static final By SYNC_WITH_HCM_SUCCESS_POPUP_CLOSE_BTN = By.xpath("//button[contains(@class,'p-toast-icon-close')]");
	private static final By SYNC_WITH_HCM_WARNING_MSG = By.xpath("//span[contains(@class,'message')]");
	private static final By SYNC_WITH_HCM_WARNING_MSG_CLOSE_BTN = By.xpath("//button[contains(@class,'close-btn')]");

	// METHODs
	public void user_is_on_architect_dashboard_page() {
		waitForSpinners();
		PageObjectHelper.log(LOGGER, "User is on Architect Dashboard Page");
	}

	public void user_is_on_profile_manager_page() {
		try {
			// PERFORMANCE: Visibility check already ensures spinners are gone
			Assert.assertTrue(waitForElement(PM_HEADER).isDisplayed());
			String PMHeaderText = waitForElement(PM_HEADER).getText();
			PageObjectHelper.log(LOGGER, "User is on " + PMHeaderText + " page as expected");
		} catch (AssertionError e) {
			PageObjectHelper.handleError(LOGGER, "user_is_on_profile_manager_page",
					"Assertion failed - User is NOT on Profile Manager page", new Exception(e));
			throw e; // Re-throw to fail the test
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_is_on_profile_manager_page",
					"Issue verifying user is on Profile Manager page", e);
			Assert.fail("Issue in verifying user is on Profile Manager page...Please Investigate!!!");
		}
	}

	public void click_on_menu_button() {
		// PERFORMANCE: Single comprehensive wait
		PerformanceUtils.waitForPageReady(driver, 3);
		try {
			jsClick(findElement(MENU_BTN));
		} catch (Exception e) {
			jsClick(findElement(HOME_MENU_BTN));
		}
		PageObjectHelper.log(LOGGER, "Able to click on Menu Button");

	}

	public void click_on_profile_manager_button() {

		try {
			jsClick(findElement(PM_BTN));
		} catch (Exception e) {

			waitForClickable(PM_BTN).click();
		}
		PageObjectHelper.log(LOGGER, "Able to click on Profile Manager Button");
	}

	public void user_should_be_landed_to_pm_dashboard() {
		waitForSpinners();
		PageObjectHelper.log(LOGGER, "User Successfully landed on the PROFILE MANAGER Dashboard Page");
	}

	public void click_on_hcm_sync_profiles_header_button() {
		try {
			clickWithFallback(HCM_SYNC_PROFILES_HEADER);
			PageObjectHelper.log(LOGGER, "Clicked on HCM Sync Profiles header in Profile Manager");
			waitForPageStability(2);
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_hcm_sync_profiles_header_button",
					"Issue clicking HCM Sync Profiles header", e);
		}
	}

	public void user_should_be_navigated_to_hcm_sync_profiles_screen() {
		try {
			wait.until(ExpectedConditions.visibilityOf(findElement(HCM_SYNC_PROFILES_TITLE))).isDisplayed();
			PageObjectHelper.log(LOGGER, "User navigated to HCM Sync Profiles screen in Profile Manager");
			// Wait for background API (~100K records) to complete
			waitForBackgroundDataLoad();
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_be_navigated_to_hcm_sync_profiles_screen",
					"Issue navigating to HCM Sync Profiles screen", e);
		}
	}

	public void verify_title_is_correctly_displaying_in_hcm_sync_profiles_tab() {
		try {
			String titleText = wait.until(ExpectedConditions.visibilityOf(findElement(HCM_SYNC_PROFILES_TITLE))).getText();
			Assert.assertEquals(titleText, "HCM Sync Profiles");
			PageObjectHelper.log(LOGGER, "Title is correctly displaying in HCM Sync Profiles screen");
		} catch (AssertionError e) {
			PageObjectHelper.handleError(LOGGER, "verify_title_is_correctly_displaying_in_hcm_sync_profiles_tab",
					"Title mismatch in HCM Sync Profiles screen", new Exception(e));
			throw e;
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_title_is_correctly_displaying_in_hcm_sync_profiles_tab",
					"Issue verifying title in HCM Sync Profiles screen", e);
		}
	}

	public void verify_description_below_the_title_is_correctly_displaying_in_hcm_sync_profiles_tab() {
		try {
			String titleDesc = wait.until(ExpectedConditions.visibilityOf(findElement(HCM_SYNC_PROFILES_TITLE_DESC))).getText();
			Assert.assertEquals(titleDesc, "Select a job profile to sync with HCM.");
			PageObjectHelper.log(LOGGER, "Title description is correctly displaying in HCM Sync Profiles screen");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_description_below_the_title_is_correctly_displaying_in_hcm_sync_profiles_tab",
					"Issue verifying title description", e);
		}
	}

	public void verify_search_bar_text_box_is_clickable_in_hcm_sync_profiles_tab() {
		try {
			waitForPageStability(15);
			scrollToAndWait(findElement(HCM_SYNC_PROFILES_SEARCHBAR));
			wait.until(ExpectedConditions.elementToBeClickable(findElement(HCM_SYNC_PROFILES_SEARCHBAR))).click();
			PageObjectHelper.log(LOGGER, "Search bar is displayed and clickable in HCM Sync Profiles screen");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_search_bar_text_box_is_clickable_in_hcm_sync_profiles_tab",
					"Issue verifying search bar clickability", e);
		}
	}

	public void verify_search_bar_placeholder_text_in_hcm_sync_profiles_tab() {
		try {
			String placeHolderText = wait.until(ExpectedConditions.visibilityOf(findElement(HCM_SYNC_PROFILES_SEARCHBAR)))
					.getAttribute("placeholder");
			Assert.assertEquals(placeHolderText, "Search job profiles within your organization...");
			PageObjectHelper.log(LOGGER, "Placeholder Text: '" + placeHolderText + "' is displaying correctly");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_search_bar_placeholder_text_in_hcm_sync_profiles_tab",
					"Issue verifying search bar placeholder text", e);
		}
	}

	/**
	 * Enters job profile name in search bar with dynamic fallback retry logic.
	 * 
	 * Strategy: 1. Try each profile name from SEARCH_PROFILE_NAME_OPTIONS array 2.
	 * Check if search returns results (not "Showing 0 of X") 3. If results found,
	 * use that profile name and stop 4. If no results, try next profile name 5. If
	 * all profile names fail, use the last one and proceed
	 * 
	 * This ensures we never get 0 results when searching (unless all options
	 * exhausted).
	 */
	public void enter_job_profile_name_in_search_bar_in_hcm_sync_profiles_tab() {
		boolean foundResults = false;
		String selectedProfileName = SEARCH_PROFILE_NAME_OPTIONS[0]; // Default to first option

		try {
			PageObjectHelper.log(LOGGER, "Searching with dynamic profile name (with fallback retry until results found)...");

			int attemptNumber = 0;
			for (String profileName : SEARCH_PROFILE_NAME_OPTIONS) {
				try {
					attemptNumber++;
					LOGGER.info("Search attempt {}: trying profile name '{}'", attemptNumber, profileName);

					// Clear and enter profile name
					wait.until(ExpectedConditions.elementToBeClickable(findElement(HCM_SYNC_PROFILES_SEARCHBAR))).clear();
					wait.until(ExpectedConditions.elementToBeClickable(findElement(HCM_SYNC_PROFILES_SEARCHBAR))).sendKeys(profileName);

					// CRITICAL FIX: Press Enter to trigger search
					findElement(HCM_SYNC_PROFILES_SEARCHBAR).sendKeys(Keys.ENTER);

					// CRITICAL: Wait for loader to APPEAR first (indicates search started)
					try {
						WebDriverWait shortWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(2));
						shortWait.until(ExpectedConditions.visibilityOfElementLocated(
								By.xpath("//*[@class='blocking-loader']//img | //div[@data-testid='loader']")));
					} catch (Exception e) {
						// Loader might have appeared and disappeared too quickly
					}

					// Wait for loader to DISAPPEAR (indicates search completed)
					try {
						WebDriverWait loaderWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(15));
						loaderWait.until(ExpectedConditions.invisibilityOfElementLocated(
								By.xpath("//*[@class='blocking-loader']//img | //div[@data-testid='loader']")));
					} catch (Exception e) {
						LOGGER.warn("Loader wait timeout - continuing");
					}

					// Additional stability wait for results to render
					try {
						Thread.sleep(1000);
					} catch (InterruptedException ie) {
					}

					// Check if results were found
					String resultsCountText = "";
					try {
						resultsCountText = findElement(SHOWING_JOB_RESULTS_COUNT).getText().trim();
						LOGGER.debug("Search results: {}", resultsCountText);
					} catch (Exception e) {
						LOGGER.debug("No profiles found with '{}' - trying next", profileName);
						continue; // Try next profile name
					}

					if (resultsCountText.contains("Showing") && !resultsCountText.startsWith("Showing 0")) {
						// Results found - now verify first result actually contains the search term
						try {
							WebDriverWait shortWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(3));
							String firstRowText = shortWait
									.until(ExpectedConditions.visibilityOf(findElement(HCM_SYNC_PROFILES_JOB_ROW1))).getText();
							String firstProfileName = firstRowText.split("-", 2)[0].trim();

							if (firstProfileName.toLowerCase().contains(profileName.toLowerCase())) {
								// Perfect match! First result contains the search term
								selectedProfileName = profileName;
								jobProfileName.set(profileName); // Update ThreadLocal variable for other methods to use
								foundResults = true;

																PageObjectHelper.log(LOGGER, "Search successful with profile name '"
										+ profileName + "' - " + resultsCountText);
								break; // Stop trying other profile names
							} else {
								LOGGER.debug("First result '{}' does not contain search term '{}' - trying next",
										firstProfileName, profileName);
							}
						} catch (Exception e) {
							LOGGER.debug("Could not verify first result, trying next search term");
						}
					} else {
						LOGGER.debug("No profiles found with '{}' (0 results) - trying next", profileName);
					}

				} catch (Exception e) {
					LOGGER.warn("Error searching with '{}': {} - trying next", profileName, e.getMessage());
					// Continue to next profile name
				}
			}

			if (!foundResults) {
				// All profile names exhausted, use the last one
				LOGGER.warn("All search profile names exhausted. Proceeding with '{}'", selectedProfileName);
				PageObjectHelper.log(LOGGER, "No profile name returned results. Using: '" + selectedProfileName + "'");
			}

			LOGGER.info("Final selected profile name: '{}'", jobProfileName.get());

		} catch (Exception e) {
			LOGGER.error("Issue entering job profile name in search bar", e);
			e.printStackTrace();
			Assert.fail(
					"Issue in entering Job Profile Name in Search bar in HCM Sync Profiles screen in Profile Manager...Please Investigate!!!");
			PageObjectHelper.log(LOGGER, 
					"Issue in entering Job Profile Name in Search bar in HCM Sync Profiles screen in Profile Manager...Please Investigate!!!");
		}
	}

	public void user_should_verify_profile_name_matching_profile_is_displaying_in_organization_jobs_profile_list() {
		try {
			// Validate that job profile name is available
			if (jobProfileName.get() == null || jobProfileName.get().isEmpty()) {
				throw new IllegalStateException(
						"Job profile name (jobProfileName) is not set. Ensure a profile was searched in a previous step.");
			}

			String searchedProfileName = jobProfileName.get();
			LOGGER.info("Verifying if profile name '{}' is displaying in HCM Sync Profiles list", searchedProfileName);

			// PERFORMANCE: Single comprehensive wait
			PerformanceUtils.waitForPageReady(driver, 5);

			// ENHANCED: First check if profile is found or "No SP" message is displayed
			boolean profileFound = false;
			String job1NameText = "";

			try {
				// Try to get the first row profile name with a shorter wait
				WebDriverWait shortWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(5));
				job1NameText = shortWait.until(ExpectedConditions.visibilityOf(findElement(HCM_SYNC_PROFILES_JOB_ROW1))).getText();
				profileFound = true;
				LOGGER.info("Found profile in first row: '{}'", job1NameText);
			} catch (Exception e) {
				LOGGER.debug("No profile found in first row, checking for 'No SP' message...");
				profileFound = false;
			}

			if (profileFound) {
				// Profile exists - verify it matches search criteria
				String profileNameFromList = job1NameText.split("-", 2)[0].trim();
				boolean matchesSearch = profileNameFromList.toLowerCase().contains(searchedProfileName.toLowerCase());

				if (matchesSearch) {
										PageObjectHelper.log(LOGGER, "Searched String present in Job Profile with name: "
							+ profileNameFromList + " is displaying in HCM Sync Profiles screen in PM as expected");
				} else {
					String errorMsg = String.format("Profile found but does not match search criteria. "
							+ "Searched for: '%s', Found profile: '%s', " + "Profile does not contain searched text.",
							searchedProfileName, profileNameFromList);
					LOGGER.error(errorMsg);
					Assert.fail(errorMsg);
				}
			} else {
				// No profile in first row - check if "No SP" message is displayed
				try {
					wait.until(ExpectedConditions.visibilityOf(findElement(NO_SP_MSG))).isDisplayed();
					PageObjectHelper.log(LOGGER, "No Success Profile Found with searched String: " + searchedProfileName);

					// Clear the search bar
					Assert.assertTrue(
							wait.until(ExpectedConditions.visibilityOf(findElement(HCM_SYNC_PROFILES_SEARCHBAR))).isDisplayed());
					Actions actions = new Actions(driver);

					actions.click(findElement(HCM_SYNC_PROFILES_SEARCHBAR)).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL)
							.sendKeys(Keys.BACK_SPACE).build().perform();
					PageObjectHelper.log(LOGGER, "Cleared Search bar in HCM Sync Profiles screen in PM");
				} catch (Exception d) {
					// Neither profile nor "No SP" message found
					String errorMsg = String.format(
							"Failed to verify profile name matching. " + "Searched for: '%s', "
									+ "Neither matching profile nor 'No Success Profile' message was found. "
									+ "Error type: %s, Error message: %s",
							searchedProfileName, d.getClass().getSimpleName(), d.getMessage());

					LOGGER.error(errorMsg, d);
					d.printStackTrace();
					Assert.fail(errorMsg);
					PageObjectHelper.log(LOGGER, "FAILURE: " + errorMsg);
				}
			}
		} catch (Exception outerException) {
			// Outer catch for any unexpected errors
			// Safely get profile name for error message
			String profileNameForError = "N/A";
			try {
				profileNameForError = (jobProfileName.get() != null) ? jobProfileName.get() : "N/A";
			} catch (Exception e) {
				// Ignore - use default "N/A"
			}

			String errorDetails = String.format(
					"Unexpected error while verifying profile name matching. "
							+ "Searched for: '%s', Error type: %s, Error message: %s",
					profileNameForError, outerException.getClass().getSimpleName(), outerException.getMessage());

			LOGGER.error(errorDetails, outerException);
			outerException.printStackTrace();
			Assert.fail(errorDetails);
			PageObjectHelper.log(LOGGER, "FAILURE: " + errorDetails);
		}
	}

	public void click_on_name_matching_profile_in_hcm_sync_profiles_tab() {
		try {
			String job1NameText = wait.until(ExpectedConditions.visibilityOf(findElement(HCM_SYNC_PROFILES_JOB_ROW1))).getText();
			wait.until(ExpectedConditions.elementToBeClickable(findElement(HCM_SYNC_PROFILES_JOB_ROW1))).click();
			LOGGER.info("Clicked on profile with name : " + job1NameText.split("-", 2)[0].trim()
					+ " in Row1 in HCM Sync Profiles screen in PM");
			PageObjectHelper.log(LOGGER, "Clicked on profile with name : "
					+ job1NameText.split("-", 2)[0].trim() + " in Row1 in HCM Sync Profiles screen in PM");
			waitForSpinners();
		} catch (Exception e) {
			LOGGER.error("Issue clicking on name matching profile", e);
			e.printStackTrace();
			Assert.fail(
					"Issue in Clicking on Searched name Matching profile in Row1 in HCM Sync Profiles screen in PM...Please Investigate!!!");
			PageObjectHelper.log(LOGGER, 
					"Issue in Clicking on Searched name Matching profile in Row1 in HCM Sync Profiles screen in PM...Please Investigate!!!");
		}
	}

	public void user_should_be_navigated_to_sp_details_page_on_click_of_matching_profile() {
		try {
			// PERFORMANCE: Visibility check already ensures spinners are gone
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(findElement(SP_DETAILS_PAGE_TEXT))).isDisplayed());
						PageObjectHelper.log(LOGGER, 
					"User navigated to SP details page as expected on click of Matching Profile Job name in HCM Sync Profiles screen in PM....");
		} catch (Exception e) {
			LOGGER.error("Issue navigating to SP details page", e);
			e.printStackTrace();
			Assert.fail(
					"Issue in Navigating to SP details Page on click of Matching Profile Job name in HCM Sync Profiles screen in PM...Please Investigate!!!");
			PageObjectHelper.log(LOGGER, 
					"Issue in Navigating to SP details Page on click of Matching Profile Job name in HCM Sync Profiles screen in PM...Please Investigate!!!");
		}
	}

	public void clear_search_bar_in_hcm_sync_profiles_tab() {
		try {
			// PARALLEL EXECUTION FIX: Extended wait for page readiness
			PerformanceUtils.waitForPageReady(driver, 3);

			// PARALLEL EXECUTION FIX: Wait for element to be present in DOM (avoid stale
			// element)
			WebDriverWait extendedWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(20));

			// STALE ELEMENT FIX: Use visibilityOfElementLocated instead of visibilityOf to
			// avoid stale element
			By searchBarLocator = By.xpath("//input[@type='search']");

			// Wait for element visibility using locator (not WebElement)
			WebElement searchBar = extendedWait.until(ExpectedConditions.visibilityOfElementLocated(searchBarLocator));

			Assert.assertTrue(searchBar.isDisplayed(), "Search bar not visible in HCM Sync Profiles screen");

			// HEADLESS FIX: Use 'auto' instead of 'smooth' for instant scroll in headless
			// mode
			js.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});", searchBar);

			// PARALLEL EXECUTION FIX: Additional wait for scroll and DOM stability
			try {
				Thread.sleep(1000); // Critical for parallel execution stability
			} catch (InterruptedException ie) {
				Thread.currentThread().interrupt();
			}

			// HEADLESS FIX: Longer wait for page to stabilize after scroll
			PerformanceUtils.waitForPageReady(driver, 2);

			// PARALLEL EXECUTION FIX: Re-fetch element to ensure it's fresh and clickable
			searchBar = extendedWait.until(ExpectedConditions.elementToBeClickable(searchBarLocator));

			// Try clicking with multiple fallback options
			try {
				searchBar.click();
			} catch (Exception clickException) {
				try {
					js.executeScript("arguments[0].click();", searchBar);
				} catch (Exception jsClickException) {
					jsClick(searchBar);
				}
			}

			// HEADLESS FIX: Use JavaScript to clear instead of keyboard combinations
			// This is more reliable across headless and windowed modes
			try {
				// First try WebDriver clear (fastest)
				searchBar.clear();
			} catch (Exception clearException) {
				// Fallback to JavaScript clear (most reliable for headless)
				js.executeScript("arguments[0].value = '';", searchBar);
				js.executeScript("arguments[0].dispatchEvent(new Event('input', { bubbles: true }));", searchBar);
				js.executeScript("arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", searchBar);
			}

			// CRITICAL HEADLESS FIX: Wait for spinners after clearing search - triggers
			// data reload
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);

			// PERFORMANCE: Single comprehensive wait after clearing
			PerformanceUtils.waitForPageReady(driver, 5);

			PageObjectHelper.log(LOGGER, "Cleared Search bar in HCM Sync Profiles screen in PM");
		} catch (Exception e) {
			LOGGER.error("Issue clearing search bar", e);
			e.printStackTrace();
			Assert.fail("Issue in clearing search bar in HCM Sync Profiles screen in PM");
			PageObjectHelper.log(LOGGER, "Issue in clearing search bar in HCM Sync Profiles screen in PM");
		}
	}

	public void verify_job_profiles_count_is_displaying_on_the_page_in_hcm_sync_profiles_tab() {
		try {
			waitForPageStability(5);
			WebElement resultsCountElement = waitForElement(SHOWING_JOB_RESULTS_COUNT, 10);
			String resultsCountText = resultsCountElement.getText().trim();

			if (resultsCountText.isEmpty()) {
				throw new Exception("Could not retrieve results count text");
			}

			intialResultsCount.set(resultsCountText);
			PageObjectHelper.log(LOGGER, "Initially " + resultsCountText + " on the page in HCM Sync Profiles screen");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_job_profiles_count_is_displaying_on_the_page_in_hcm_sync_profiles_tab",
					"Issue verifying job profiles count", e);
		}
	}

	public void scroll_page_to_view_more_job_profiles_in_hcm_sync_profiles_tab() {
		try {
			scrollToBottom();
			waitForPageStability(5);
			PageObjectHelper.log(LOGGER, "Scrolled page down to view more job profiles in HCM Sync Profiles screen");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "scroll_page_to_view_more_job_profiles_in_hcm_sync_profiles_tab",
					"Issue scrolling page down", e);
		}
	}

	public void user_should_verify_count_of_job_profiles_is_correctly_showing_on_top_of_job_profiles_listing_table_in_hcm_sync_profiles_tab() {
		try {
			scrollToElement(findElement(HCM_SYNC_PROFILES_TITLE));
			PerformanceUtils.waitForSpinnersToDisappear(driver, 5);
			PerformanceUtils.waitForPageReady(driver, 3);
			safeSleep(2000);

			String initialCount = intialResultsCount.get();
			LOGGER.info("Waiting for results count to change from initial: " + initialCount);

			// Wait for count to update using WebDriverWait
			WebDriverWait countWait = new WebDriverWait(driver, Duration.ofSeconds(10));
			String resultsCountText1 = countWait.until(driver -> {
				try {
					WebElement resultsElement = driver.findElement(SHOWING_JOB_RESULTS_COUNT);
					String currentText = resultsElement.getText().trim();
					if (!currentText.isEmpty() && !currentText.equals(initialCount)) {
						return currentText;
					}
					return null;
				} catch (Exception e) {
					return null;
				}
			});

			if (resultsCountText1 == null || resultsCountText1.equals(initialCount)) {
				throw new Exception("Results count did not update after scrolling. Initial: '" + initialCount + "'");
			}

			updatedResultsCount.set(resultsCountText1);
			Assert.assertNotEquals(initialCount, resultsCountText1,
					"Results count should have changed after scrolling");

			PageObjectHelper.log(LOGGER, "Success Profiles Results count updated to " + resultsCountText1
					+ " in HCM Sync Profiles screen in PM");

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_count_of_job_profiles_is_correctly_showing_on_top_of_job_profiles_listing_table_in_hcm_sync_profiles_tab",
					"Issue verifying profiles count after scrolling", e);
		}
	}

	public void user_is_in_hcm_sync_profiles_screen() {
		PageObjectHelper.log(LOGGER, "User is in HCM Sync Profiles screen in PM.....");
	}

	public void click_on_filters_dropdown_button_in_hcm_sync_profiles_tab() {
		try {
			js.executeScript("window.scrollTo(0, 0);");
			waitForPageStability(5);

			// Check if dropdown is already open
			boolean isDropdownOpen = false;
			try {
				isDropdownOpen = findElement(FILTER_OPTIONS).isDisplayed();
			} catch (Exception ex) {
				isDropdownOpen = false;
			}

			if (isDropdownOpen) {
				PageObjectHelper.log(LOGGER, "Filters dropdown is already open");
				return;
			}

			// Open dropdown using fallback click pattern
			clickWithFallback(FILTERS_DROPDOWN_BTN);
			PageObjectHelper.log(LOGGER, "Clicked on filters dropdown button in HCM Sync Profiles screen");

			// Wait for dropdown to be visible
			WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(10));
			shortWait.until(ExpectedConditions.visibilityOf(findElement(FILTER_OPTIONS)));
			LOGGER.debug("Filters dropdown opened successfully");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_filters_dropdown_button_in_hcm_sync_profiles_tab",
					"Issue clicking filters dropdown", e);
		}
	}

	public void verify_options_available_inside_filters_dropdown_in_hcm_sync_profiles_tab() {
		try {
			wait.until(ExpectedConditions.visibilityOf(findElement(FILTER_OPTIONS)));
			PerformanceUtils.waitForSpinnersToDisappear(driver, 5);
			safeSleep(500);

			// Get filter option texts
			String filterOption1Text = wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("//*[contains(@class,'sp-search-filter-expansion-panel')][1]//span//div"))).getText();
			String filterOption2Text = wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("//*[contains(@class,'sp-search-filter-expansion-panel')][2]//span//div"))).getText();
			String filterOption3Text = wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("//*[contains(@class,'sp-search-filter-expansion-panel')][3]//span//div"))).getText();

			Assert.assertEquals(filterOption1Text, "KF Grade", "Option 1 should be 'KF Grade'");
			Assert.assertEquals(filterOption2Text, "Levels", "Option 2 should be 'Levels'");
			Assert.assertEquals(filterOption3Text, "Functions / Subfunctions", "Option 3 should be 'Functions / Subfunctions'");

			PageObjectHelper.log(LOGGER, "Options inside Filters dropdown verified successfully in HCM Sync Profiles screen in PM");

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_options_available_inside_filters_dropdown_in_hcm_sync_profiles_tab",
					"Filter options verification failed", e);
		}

		// Verify Functions/Subfunctions search bar
		try {
			WebElement option3ToClick = wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("//*[contains(@class,'sp-search-filter-expansion-panel')][3]//span//div")));
			clickElement(option3ToClick);
			safeSleep(300);

			WebElement searchBar = wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("//*[contains(@class,'sp-search-filter-expansion-panel')][3]//input")));
			searchBar.click();

			PageObjectHelper.log(LOGGER, "Search bar inside Functions / Subfunctions filter option is available and clickable");

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_options_available_inside_filters_dropdown_in_hcm_sync_profiles_tab",
					"Search bar verification failed", e);
		}

		// Close the dropdown after verification
		try {
			WebElement filterOptionsElement = driver.findElement(FILTER_OPTIONS);
			if (filterOptionsElement.isDisplayed()) {
				jsClick(findElement(FILTERS_DROPDOWN_BTN));
				wait.until(ExpectedConditions.invisibilityOf(filterOptionsElement));
				PageObjectHelper.log(LOGGER, "Closed Filters dropdown after verification");
			}
		} catch (Exception e) {
			LOGGER.debug("Could not close filters dropdown: {}", e.getMessage());
		}
	}

	public void apply_kf_grade_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab() {
		try {
			try {
				PerformanceUtils.waitForElement(driver, findElement(KF_GRADE_FILTERS_DROPDOWN));
				PerformanceUtils.waitForElement(driver, driver.findElement(By.xpath("//div")));
				wait.until(ExpectedConditions.visibilityOf(findElement(KF_GRADE_FILTERS_DROPDOWN))).isDisplayed();
				try {
					wait.until(ExpectedConditions.elementToBeClickable(findElement(KF_GRADE_FILTERS_DROPDOWN))).click();
				} catch (Exception e) {
					try {
						js.executeScript("arguments[0].click();", findElement(KF_GRADE_FILTERS_DROPDOWN));
					} catch (Exception s) {
						jsClick(findElement(KF_GRADE_FILTERS_DROPDOWN));
					}
				}
				PageObjectHelper.log(LOGGER, "Clicked on KF Grade dropdown in Filters in HCM Sync Profiles screen in PM");
			} catch (Exception e) {
				LOGGER.error("Issue clicking KF Grade dropdown", e);
				e.printStackTrace();
				Assert.fail(
						"Issue in clicking KF Grade dropdown in Filters in HCM Sync Profiles screen in PM...Please Investigate!!!");
				PageObjectHelper.log(LOGGER, 
						"Issue in clicking KF Grade dropdown in Filters in HCM Sync Profiles screen in PM...Please Investigate!!!");
			}

			try {
				// DYNAMIC XPATH: Find all available KF Grade options using updated class-based
				// XPATH
				PerformanceUtils.waitForPageReady(driver, 2);

				if (findElements(KF_GRADE_ALL_CHECKBOXES).isEmpty() || findElements(KF_GRADE_ALL_LABELS).isEmpty()) {
					LOGGER.error(" No KF Grade options found after expanding dropdown");
					throw new Exception("No KF Grade filter options found");
				}

				// Get the first checkbox and its corresponding label
				WebElement firstCheckbox = findElements(KF_GRADE_ALL_CHECKBOXES).get(0);
				String kfGradeValue = findElements(KF_GRADE_ALL_LABELS).get(0).getText().trim();

				LOGGER.info("Found KF Grade option: " + kfGradeValue);

				// Scroll to element and click
				js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", firstCheckbox);
				Thread.sleep(300);

				try {
					wait.until(ExpectedConditions.elementToBeClickable(firstCheckbox)).click();
					LOGGER.info(" Clicked KF Grade option using standard click");
				} catch (Exception e) {
					LOGGER.warn("Standard click failed, trying JS click...");
					js.executeScript("arguments[0].click();", firstCheckbox);
					LOGGER.info(" Clicked KF Grade option using JS click");
				}

				Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(findElement(CLOSE_APPLIED_FILTER))).isDisplayed());
								PageObjectHelper.log(LOGGER, "Selected KF Grade Value : " + kfGradeValue
						+ " from Filters dropdown in HCM Sync Profiles screen in PM....");
			} catch (Exception e) {
				LOGGER.error("Issue selecting KF Grade option", e);
				e.printStackTrace();
				Assert.fail(
						"Issue in selecting a option from KF Grade dropdown in Filters in HCM Sync Profiles screen in PM...Please Investigate!!!");
				PageObjectHelper.log(LOGGER, 
						"Issue in selecting a option from KF Grade dropdown in Filters in HCM Sync Profiles screen in PM...Please Investigate!!!");
			}

			try {
				// PERFORMANCE: Removed Thread.sleep(2000) - scrolling doesn't need fixed delay
				js.executeScript("window.scrollTo(0, 0);"); // Scroll to top (headless-compatible)
				try {
					wait.until(ExpectedConditions.elementToBeClickable(findElement(HCM_SYNC_PROFILES_HEADER))).click();
				} catch (Exception e) {
					try {
						js.executeScript("arguments[0].click();", findElement(HCM_SYNC_PROFILES_HEADER));
					} catch (Exception s) {
						jsClick(findElement(HCM_SYNC_PROFILES_HEADER));
					}
				}
				Assert.assertTrue(wait.until(ExpectedConditions.invisibilityOf(findElement(FILTER_OPTIONS))));
				PageObjectHelper.log(LOGGER, "Filters dropdown closed successfully in HCM Sync Profiles screen in PM");
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail(
						"Issue in closing filters dropdown in HCM Sync Profiles screen in PM...Please Investigate!!!");
				PageObjectHelper.log(LOGGER, 
						"Issue in closing filters dropdown in HCM Sync Profiles screen in PM...Please Investigate!!!");
			}

			try {
				// PERFORMANCE: Single comprehensive wait after closing dropdown
				PerformanceUtils.waitForPageReady(driver, 5);

				// Check if there are no results after applying the filter
				boolean noResults = false;
				try {
					if (findElement(NO_RESULTS_MESSAGE).isDisplayed() || findElement(NO_SP_MSG).isDisplayed()) {
						noResults = true;
					}
				} catch (Exception e) {
					// Elements not found, meaning there are results
					noResults = false;
				}

				if (noResults) {
					LOGGER.warn(
							" NO RESULTS FOUND after applying KF Grade filter - This is acceptable, the selected filter value returned 0 results");
					PageObjectHelper.log(LOGGER, 
							" NO RESULTS - The applied KF Grade filter returned 0 results. This is an expected scenario.");
					try {
						String countText = findElement(SHOWING_JOB_RESULTS_COUNT).getText();
						PageObjectHelper.log(LOGGER, "Results count shows: " + countText);
					} catch (Exception ex) {
						LOGGER.info("No results count displayed - 0 profiles match the filter");
					}
				} else {
					// There are results, verify count changed
					PerformanceUtils.waitForElement(driver, findElement(SHOWING_JOB_RESULTS_COUNT));
					String resultsCountText2 = wait.until(ExpectedConditions.visibilityOf(findElement(SHOWING_JOB_RESULTS_COUNT)))
							.getText();
					Assert.assertNotEquals(updatedResultsCount.get(), resultsCountText2);
					if (!resultsCountText2.equals(updatedResultsCount.get())) {
												PageObjectHelper.log(LOGGER, "Success Profiles Results count updated and Now "
								+ resultsCountText2
								+ " as expected after applying KF Grade Filters in HCM Sync Profiles screen in PM");
					} else {
						PageObjectHelper.log(LOGGER, "Issue in updating success profiles results count, Still "
								+ resultsCountText2
								+ " after applying KF Grade Filters in HCM Sync Profiles screen in PM....Please Investiagte!!!");
						throw new Exception("Issue in updating success profiles results count, Still "
								+ resultsCountText2
								+ " after applying KF Grade Filters in HCM Sync Profiles screen in PM....Please Investiagte!!!");
					}
				}
			} catch (Exception e) {
				LOGGER.error("Issue verifying profiles count after KF Grade filter", e);
				e.printStackTrace();
				Assert.fail(
						"Issue in verifying success profiles results count after scrolling page down in HCM Sync Profiles screen in PM...Please Investigate!!!");
				PageObjectHelper.log(LOGGER, 
						"Issue in verifying success profiles results count after scrolling page down in in HCM Sync Profiles screen in PM...Please Investigate!!!");
			}
		} catch (Exception e) {
			LOGGER.error("Issue applying KF Grade filter", e);
			e.printStackTrace();
			Assert.fail(
					"Issue in verifying KF Grade Filter and Verifing Profiles count is correctly displaying in HCM Sync Profiles screen in PM...Please Investigate!!!");
			PageObjectHelper.log(LOGGER, 
					"Issue in verifying KF Grade Filter and Verifing Profiles count is correctly displaying in HCM Sync Profiles screen in PM...Please Investigate!!!");
		}
	}

	public void clear_kf_grade_filter_in_hcm_sync_profiles_tab() {
		try {
			WebElement closeFilterElement = wait.until(ExpectedConditions.visibilityOf(findElement(CLOSE_APPLIED_FILTER)));
			closeFilterElement.click();
			waitForSpinners();
			PageObjectHelper.log(LOGGER, "Cleared Applied KF Grade Filter in HCM Sync Profiles screen in PM");
		} catch (Exception e) {
			LOGGER.error("Issue clearing KF Grade filter", e);
			e.printStackTrace();
			Assert.fail(
					"Issue in Clearing Applied KF Grade Filter in HCM Sync Profiles screen in PM...Please Investigate!!!");
			PageObjectHelper.log(LOGGER, 
					"Issue in Clearing Applied KF Grade Filter in HCM Sync Profiles screen in PM...Please Investigate!!!");
		}
	}

	public void apply_levels_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab() {
		try {
			try {
				PerformanceUtils.waitForElement(driver, findElement(LEVELS_FILTERS_DROPDOWN));
				wait.until(ExpectedConditions.visibilityOf(findElement(LEVELS_FILTERS_DROPDOWN))).isDisplayed();
				jsClick(findElement(LEVELS_FILTERS_DROPDOWN));
				PageObjectHelper.log(LOGGER, "Clicked on Levels dropdown in Filters in HCM Sync Profiles screen in PM");
			} catch (Exception e) {
				LOGGER.error("Issue clicking Levels dropdown", e);
				e.printStackTrace();
				Assert.fail(
						"Issue in clicking Levels dropdown in Filters in HCM Sync Profiles screen in PM...Please Investigate!!!");
				PageObjectHelper.log(LOGGER, 
						"Issue in clicking Levels dropdown in Filters in HCM Sync Profiles screen in PM...Please Investigate!!!");
			}

			try {
				// DYNAMIC XPATH: Find all available Levels options using updated class-based
				// XPATH
				PerformanceUtils.waitForPageReady(driver, 2);

				if (findElements(LEVELS_ALL_CHECKBOXES).isEmpty() || findElements(LEVELS_ALL_LABELS).isEmpty()) {
					LOGGER.error(" No Levels options found after expanding dropdown");
					throw new Exception("No Levels filter options found");
				}

				// Get the first checkbox and its corresponding label
				WebElement firstCheckbox = findElements(LEVELS_ALL_CHECKBOXES).get(0);
				String levelsValue = findElements(LEVELS_ALL_LABELS).get(0).getText().trim();

				LOGGER.info("Found Levels option: " + levelsValue);

				// Scroll to element and click
				js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", firstCheckbox);
				Thread.sleep(300);

				try {
					wait.until(ExpectedConditions.elementToBeClickable(firstCheckbox)).click();
					LOGGER.info(" Clicked Levels option using standard click");
				} catch (Exception e) {
					LOGGER.warn("Standard click failed, trying JS click...");
					js.executeScript("arguments[0].click();", firstCheckbox);
					LOGGER.info(" Clicked Levels option using JS click");
				}

				Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(findElement(CLOSE_APPLIED_FILTER))).isDisplayed());
								PageObjectHelper.log(LOGGER, "Selected Levels Value : " + levelsValue
						+ " from Filters dropdown in HCM Sync Profiles screen in PM....");
			} catch (Exception e) {
				LOGGER.error(
						" Issue selecting Levels option - Method: apply_levels_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab",
						e);
				e.printStackTrace();
				Assert.fail(
						"Issue in selecting a option from Levels dropdown in Filters in HCM Sync Profiles screen in PM...Please Investigate!!!");
				PageObjectHelper.log(LOGGER, 
						"Issue in selecting a option from Levels dropdown in Filters in HCM Sync Profiles screen in PM...Please Investigate!!!");
			}

			try {
				// PERFORMANCE: Removed Thread.sleep(2000) - scrolling doesn't need fixed delay
				js.executeScript("window.scrollTo(0, 0);"); // Scroll to top (headless-compatible)
				try {
					wait.until(ExpectedConditions.visibilityOf(findElement(HCM_SYNC_PROFILES_HEADER))).click();
				} catch (Exception e) {
					try {
						js.executeScript("arguments[0].click();", findElement(HCM_SYNC_PROFILES_HEADER));
					} catch (Exception s) {
						jsClick(findElement(HCM_SYNC_PROFILES_HEADER));
					}
				}
				Assert.assertTrue(wait.until(ExpectedConditions.invisibilityOf(findElement(FILTER_OPTIONS))));
				PageObjectHelper.log(LOGGER, "Filters dropdown closed successfully in HCM Sync Profiles screen in PM");
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail(
						"Issue in closing filters dropdown in HCM Sync Profiles screen in PM...Please Investigate!!!");
				PageObjectHelper.log(LOGGER, 
						"Issue in closing filters dropdown in HCM Sync Profiles screen in PM...Please Investigate!!!");
			}

			try {
				// PERFORMANCE: Single comprehensive wait after closing dropdown
				PerformanceUtils.waitForPageReady(driver, 5);

				// Check if there are no results after applying the filter
				boolean noResults = false;
				try {
					if (findElement(NO_RESULTS_MESSAGE).isDisplayed() || findElement(NO_SP_MSG).isDisplayed()) {
						noResults = true;
					}
				} catch (Exception e) {
					// Elements not found, meaning there are results
					noResults = false;
				}

				if (noResults) {
					LOGGER.warn(
							" NO RESULTS FOUND after applying Levels filter - This is acceptable, the selected filter value returned 0 results");
					PageObjectHelper.log(LOGGER, 
							" NO RESULTS - The applied Levels filter returned 0 results. This is an expected scenario.");
					try {
						String countText = findElement(SHOWING_JOB_RESULTS_COUNT).getText();
						PageObjectHelper.log(LOGGER, "Results count shows: " + countText);
					} catch (Exception ex) {
						LOGGER.info("No results count displayed - 0 profiles match the filter");
					}
				} else {
					// There are results, verify count changed
					// PARALLEL EXECUTION FIX: Add spinner wait before checking count
					PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
					PerformanceUtils.waitForPageReady(driver, 5);

					// PARALLEL EXECUTION FIX: Use locator-based wait to avoid stale element
					String resultsCountText2 = wait
							.until(ExpectedConditions
									.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Showing')]")))
							.getText();

					Assert.assertNotEquals(intialResultsCount.get(), resultsCountText2,
							"Results count should change after applying Levels filter");

					if (!resultsCountText2.equals(intialResultsCount.get())) {
												PageObjectHelper.log(LOGGER, "Success Profiles Results count updated and Now "
								+ resultsCountText2
								+ " as expected after applying Levels Filters in HCM Sync Profiles screen in PM");
					} else {
						Assert.fail("Issue in updating success profiles results count, Still " + resultsCountText2
								+ " after applying Levels Filters in HCM Sync Profiles screen in PM....Please Investiagte!!!");
						PageObjectHelper.log(LOGGER, "Issue in updating success profiles results count, Still "
								+ resultsCountText2
								+ " after applying Levels Filters in HCM Sync Profiles screen in PM....Please Investiagte!!!");
						throw new Exception("Issue in updating success profiles results count, Still "
								+ resultsCountText2
								+ " after applying Levels Filters in HCM Sync Profiles screen in PM....Please Investiagte!!!");
					}
				}
			} catch (Exception e) {
				LOGGER.error(
						" Issue verifying profiles count after Levels filter - Method: apply_levels_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab",
						e);
				e.printStackTrace();
				Assert.fail(
						"Issue in verifying success profiles results count after scrolling page down in HCM Sync Profiles screen in PM...Please Investigate!!!");
				PageObjectHelper.log(LOGGER, 
						"Issue in verifying success profiles results count after scrolling page down in in HCM Sync Profiles screen in PM...Please Investigate!!!");
			}
		} catch (Exception e) {
			LOGGER.error(
					" Issue applying Levels filter - Method: apply_levels_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab",
					e);
			e.printStackTrace();
			Assert.fail(
					"Issue in verifying Levels Filter and Verifing Profiles count is correctly displaying in HCM Sync Profiles screen in PM...Please Investigate!!!");
			PageObjectHelper.log(LOGGER, 
					"Issue in verifying Levels Filter and Verifing Profiles count is correctly displaying in HCM Sync Profiles screen in PM...Please Investigate!!!");
		}
	}

	public void clear_levels_filter_in_hcm_sync_profiles_tab() {
		try {
			WebElement closeFilterElement = wait.until(ExpectedConditions.visibilityOf(findElement(CLOSE_APPLIED_FILTER)));
			closeFilterElement.click();
			waitForSpinners();
			PageObjectHelper.log(LOGGER, "Cleared Applied Levels Filter in HCM Sync Profiles screen in PM");
		} catch (Exception e) {
			LOGGER.error(" Issue clearing Levels filter - Method: clear_levels_filter_in_hcm_sync_profiles_tab", e);
			e.printStackTrace();
			Assert.fail(
					"Issue in Clearing Applied Levels Filter in HCM Sync Profiles screen in PM...Please Investigate!!!");
			PageObjectHelper.log(LOGGER, 
					"Issue in Clearing Applied Levels Filter in HCM Sync Profiles screen in PM...Please Investigate!!!");
		}
	}

	public void apply_functions_or_subfunctions_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab() {
		try {
			try {
				PerformanceUtils.waitForElement(driver, findElement(FUNCTIONS_SUBFUNCTIONS_FILTERS_DROPDOWN));
				wait.until(ExpectedConditions.visibilityOf(findElement(FUNCTIONS_SUBFUNCTIONS_FILTERS_DROPDOWN))).isDisplayed();
				jsClick(findElement(FUNCTIONS_SUBFUNCTIONS_FILTERS_DROPDOWN));
								PageObjectHelper.log(LOGGER, 
						"Clicked on Functions / Subfunctions dropdown in Filters in HCM Sync Profiles screen in PM...");
			} catch (Exception e) {
				LOGGER.error(
						" Issue clicking Functions/Subfunctions dropdown - Method: apply_functions_or_subfunctions_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab",
						e);
				e.printStackTrace();
				Assert.fail(
						"Issue in clicking Functions / Subfunctions dropdown in Filters in HCM Sync Profiles screen in PM...Please Investigate!!!");
				PageObjectHelper.log(LOGGER, 
						"Issue in clicking Functions / Subfunctions dropdown in Filters in HCM Sync Profiles screen in PM...Please Investigate!!!");
			}

			try {
				// DYNAMIC XPATH: Find all available Functions/Subfunctions options using
				// updated class-based XPATH
				PerformanceUtils.waitForPageReady(driver, 2);

				if (findElements(FUNCTIONS_SUBFUNCTIONS_ALL_CHECKBOXES).isEmpty() || findElements(FUNCTIONS_SUBFUNCTIONS_ALL_LABELS).isEmpty()) {
					LOGGER.error(" No Functions/Subfunctions options found after expanding dropdown");
					throw new Exception("No Functions/Subfunctions filter options found");
				}

				// PARALLEL EXECUTION FIX: Iterate through options to find a valid function/subfunction name
				// Skip invalid values (pure numbers, very short text, or placeholder values)
				List<WebElement> checkboxes = findElements(FUNCTIONS_SUBFUNCTIONS_ALL_CHECKBOXES);
				List<WebElement> labels = findElements(FUNCTIONS_SUBFUNCTIONS_ALL_LABELS);
				
				if (checkboxes.size() != labels.size()) {
					LOGGER.warn("Mismatch between checkbox count ({}) and label count ({}). Re-fetching elements...", 
							checkboxes.size(), labels.size());
					PerformanceUtils.waitForPageReady(driver, 1);
					checkboxes = findElements(FUNCTIONS_SUBFUNCTIONS_ALL_CHECKBOXES);
					labels = findElements(FUNCTIONS_SUBFUNCTIONS_ALL_LABELS);
				}
				
				WebElement selectedCheckbox = null;
				String functionsValue = null;
				
				// Helper method to validate if a value looks like a valid function/subfunction name
				for (int i = 0; i < Math.min(checkboxes.size(), labels.size()); i++) {
					try {
						// Re-fetch elements to avoid stale references
						if (i > 0) {
							checkboxes = findElements(FUNCTIONS_SUBFUNCTIONS_ALL_CHECKBOXES);
							labels = findElements(FUNCTIONS_SUBFUNCTIONS_ALL_LABELS);
						}
						
						String candidateValue = labels.get(i).getText().trim();
						
						// Validate: Skip if value is empty, too short, pure numbers, or looks like invalid data
						if (candidateValue.isEmpty() || candidateValue.length() < 2) {
							LOGGER.debug("Skipping option {}: too short or empty", candidateValue);
							continue;
						}
						
						// Skip if it's pure numbers (like "123141") or looks like invalid data
						if (candidateValue.matches("^\\d+$") || candidateValue.matches("^[a-z]{1,5}$")) {
							LOGGER.debug("Skipping option {}: appears to be invalid (pure numbers or too short)", candidateValue);
							continue;
						}
						
						// Found a valid-looking option
						selectedCheckbox = checkboxes.get(i);
						functionsValue = candidateValue;
						LOGGER.info("Selected valid Functions/Subfunctions option: {}", functionsValue);
						break;
					} catch (org.openqa.selenium.StaleElementReferenceException e) {
						LOGGER.debug("Stale element at index {}, re-fetching...", i);
						checkboxes = findElements(FUNCTIONS_SUBFUNCTIONS_ALL_CHECKBOXES);
						labels = findElements(FUNCTIONS_SUBFUNCTIONS_ALL_LABELS);
						if (i < checkboxes.size() && i < labels.size()) {
							String candidateValue = labels.get(i).getText().trim();
							if (!candidateValue.isEmpty() && candidateValue.length() >= 2 
									&& !candidateValue.matches("^\\d+$") && !candidateValue.matches("^[a-z]{1,5}$")) {
								selectedCheckbox = checkboxes.get(i);
								functionsValue = candidateValue;
								LOGGER.info("Selected valid Functions/Subfunctions option after stale element: {}", functionsValue);
								break;
							}
						}
					}
				}
				
				// If no valid option found, use the first one as fallback
				if (selectedCheckbox == null || functionsValue == null) {
					LOGGER.warn("No clearly valid option found, using first option as fallback");
					checkboxes = findElements(FUNCTIONS_SUBFUNCTIONS_ALL_CHECKBOXES);
					labels = findElements(FUNCTIONS_SUBFUNCTIONS_ALL_LABELS);
					if (!checkboxes.isEmpty() && !labels.isEmpty()) {
						selectedCheckbox = checkboxes.get(0);
						functionsValue = labels.get(0).getText().trim();
						LOGGER.info("Using first option as fallback: {}", functionsValue);
					} else {
						throw new Exception("Could not find any Functions/Subfunctions option to select");
					}
				}

				// Scroll to element and click
				js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", selectedCheckbox);
				Thread.sleep(300);

				try {
					wait.until(ExpectedConditions.elementToBeClickable(selectedCheckbox)).click();
					LOGGER.info(" Clicked Functions/Subfunctions option using standard click");
				} catch (Exception e) {
					LOGGER.warn("Standard click failed, trying JS click...");
					js.executeScript("arguments[0].click();", selectedCheckbox);
					LOGGER.info(" Clicked Functions/Subfunctions option using JS click");
				}

				Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(findElement(CLEAR_ALL_FILTERS_BTN))).isDisplayed());
								PageObjectHelper.log(LOGGER, "Selected Function Value : " + functionsValue
						+ " from Filters dropdown in HCM Sync Profiles screen in PM....");
			} catch (Exception e) {
				LOGGER.error(
						" Issue selecting Functions/Subfunctions option - Method: apply_functions_or_subfunctions_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab",
						e);
				e.printStackTrace();
				Assert.fail(
						"Issue in selecting a option from Functions / Subfunctions dropdown in Filters in HCM Sync Profiles screen in PM...Please Investigate!!!");
				PageObjectHelper.log(LOGGER, 
						"Issue in selecting a option from Functions / Subfunctions dropdown in Filters in HCM Sync Profiles screen in PM...Please Investigate!!!");
			}

			try {
				// PERFORMANCE: Removed Thread.sleep(2000) - scrolling doesn't need fixed delay
				js.executeScript("window.scrollTo(0, 0);"); // Scroll to top (headless-compatible)
				try {
					wait.until(ExpectedConditions.visibilityOf(findElement(HCM_SYNC_PROFILES_HEADER))).click();
				} catch (Exception e) {
					try {
						js.executeScript("arguments[0].click();", findElement(HCM_SYNC_PROFILES_HEADER));
					} catch (Exception s) {
						jsClick(findElement(HCM_SYNC_PROFILES_HEADER));
					}
				}
				Assert.assertTrue(wait.until(ExpectedConditions.invisibilityOf(findElement(FILTER_OPTIONS))));
				PageObjectHelper.log(LOGGER, "Filters dropdown closed successfully in HCM Sync Profiles screen in PM");
			} catch (Exception e) {
				LOGGER.error(
						" Issue closing filters dropdown - Method: apply_functions_or_subfunctions_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab",
						e);
				e.printStackTrace();
				Assert.fail(
						"Issue in closing filters dropdown in HCM Sync Profiles screen in PM...Please Investigate!!!");
				PageObjectHelper.log(LOGGER, 
						"Issue in closing filters dropdown in HCM Sync Profiles screen in PM...Please Investigate!!!");
			}

			try {
				// PERFORMANCE: Single comprehensive wait after closing dropdown
				PerformanceUtils.waitForPageReady(driver, 5);

				// Check if there are no results after applying the filter
				boolean noResults = false;
				try {
					if (findElement(NO_RESULTS_MESSAGE).isDisplayed() || findElement(NO_SP_MSG).isDisplayed()) {
						noResults = true;
					}
				} catch (Exception e) {
					// Elements not found, meaning there are results
					noResults = false;
				}

				if (noResults) {
					LOGGER.warn(
							" NO RESULTS FOUND after applying Functions/Subfunctions filter - This is acceptable, the selected filter value returned 0 results");
					PageObjectHelper.log(LOGGER, 
							" NO RESULTS - The applied Functions/Subfunctions filter returned 0 results. This is an expected scenario.");
					try {
						String countText = findElement(SHOWING_JOB_RESULTS_COUNT).getText();
						PageObjectHelper.log(LOGGER, "Results count shows: " + countText);
					} catch (Exception ex) {
						LOGGER.info("No results count displayed - 0 profiles match the filter");
					}
				} else {
					// There are results, verify count changed
					PerformanceUtils.waitForElement(driver, findElement(SHOWING_JOB_RESULTS_COUNT));
					String resultsCountText2 = wait.until(ExpectedConditions.visibilityOf(findElement(SHOWING_JOB_RESULTS_COUNT)))
							.getText();
					Assert.assertNotEquals(intialResultsCount.get(), resultsCountText2);
					if (!resultsCountText2.equals(intialResultsCount.get())) {
												PageObjectHelper.log(LOGGER, "Success Profiles Results count updated and Now "
								+ resultsCountText2
								+ " as expected after applying Functions / Subfunctions Filters in HCM Sync Profiles screen in PM");
					} else {
						Assert.fail("Issue in updating success profiles results count, Still " + resultsCountText2
								+ " after applying Functions / Subfunctions Filters in HCM Sync Profiles screen in PM....Please Investiagte!!!");
						PageObjectHelper.log(LOGGER, "Issue in updating success profiles results count, Still "
								+ resultsCountText2
								+ " after applying Functions / Subfunctions Filters in HCM Sync Profiles screen in PM....Please Investiagte!!!");
						throw new Exception("Issue in updating success profiles results count, Still "
								+ resultsCountText2
								+ " after applying Functions / Subfunctions Filters in HCM Sync Profiles screen in PM....Please Investiagte!!!");
					}
				}
			} catch (Exception e) {
				LOGGER.error(
						" Issue verifying profiles count after Functions filter - Method: apply_functions_or_subfunctions_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab",
						e);
				e.printStackTrace();
				Assert.fail(
						"Issue in verifying success profiles results count after scrolling page down in HCM Sync Profiles screen in PM...Please Investigate!!!");
				PageObjectHelper.log(LOGGER, 
						"Issue in verifying success profiles results count after scrolling page down in in HCM Sync Profiles screen in PM...Please Investigate!!!");
			}
		} catch (Exception e) {
			LOGGER.error(
					" Issue applying Functions/Subfunctions filter - Method: apply_functions_or_subfunctions_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab",
					e);
			e.printStackTrace();
			Assert.fail(
					"Issue in verifying Functions / Subfunctions Filter and Verifing Profiles count is correctly displaying in HCM Sync Profiles screen in PM...Please Investigate!!!");
			PageObjectHelper.log(LOGGER, 
					"Issue in verifying Functions / Subfunctions Filter and Verifing Profiles count is correctly displaying in HCM Sync Profiles screen in PM...Please Investigate!!!");
		}
	}

	public void click_on_clear_all_filters_button_in_hcm_sync_profiles_tab() {
		try {
			// Scroll to top of page to avoid header interception
			js.executeScript("window.scrollTo(0, 0);");
			// PERFORMANCE: No wait needed for instant scroll

			// Scroll element into view and click
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", findElement(CLEAR_ALL_FILTERS_BTN));
			// PERFORMANCE: Reduced wait for smooth scroll animation (300ms is enough)
			Thread.sleep(300);

			try {
				wait.until(ExpectedConditions.elementToBeClickable(findElement(CLEAR_ALL_FILTERS_BTN))).click();
				LOGGER.info("Clicked on Clear All Filters button using standard click");
			} catch (Exception e) {
				LOGGER.warn("Standard click failed, trying JS click...");
				js.executeScript("arguments[0].click();", findElement(CLEAR_ALL_FILTERS_BTN));
				LOGGER.info("Clicked on Clear All Filters button using JS click");
			}

			PageObjectHelper.log(LOGGER, "Clicked on Clear All Filters button in HCM Sync Profiles screen in PM");
			waitForSpinners();
		} catch (Exception e) {
			LOGGER.error(
					" Issue clicking Clear All Filters button - Method: click_on_clear_all_filters_button_in_hcm_sync_profiles_tab",
					e);
			e.printStackTrace();
			Assert.fail(
					"Issue in clicking on Clear All Filters button in HCM Sync Profiles screen in PM...Please Investigate!!!");
			PageObjectHelper.log(LOGGER, 
					"Issue in clicking on Clear All Filters button in HCM Sync Profiles screen in PM...Please Investigate!!!");
		}
	}

	public void apply_profile_status_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab() {
		try {
			try {
				PerformanceUtils.waitForElement(driver, findElement(PROFILE_STATUS_FILTERS_DROPDOWN));
				wait.until(ExpectedConditions.visibilityOf(findElement(PROFILE_STATUS_FILTERS_DROPDOWN))).isDisplayed();
				jsClick(findElement(PROFILE_STATUS_FILTERS_DROPDOWN));
				PageObjectHelper.log(LOGGER, 
						"Clicked on Profile Status dropdown in Filters in HCM Sync Profiles screen in PM...");
			} catch (Exception e) {
				LOGGER.error(
						" Issue clicking Profile Status dropdown - Method: apply_profile_status_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab",
						e);
				e.printStackTrace();
				Assert.fail(
						"Issue in clicking on Profile Status dropdown in Filters in HCM Sync Profiles screen in PM...Please Investigate!!!");
				PageObjectHelper.log(LOGGER, 
						"Issue in clicking on Profile Status dropdown in Filters in HCM Sync Profiles screen in PM...Please Investigate!!!");
			}

			try {
				// DYNAMIC XPATH: Find all available Profile Status options using updated
				// class-based XPATH
				PerformanceUtils.waitForPageReady(driver, 2);

				if (findElements(PROFILE_STATUS_ALL_CHECKBOXES).isEmpty() || findElements(PROFILE_STATUS_ALL_LABELS).isEmpty()) {
					LOGGER.error(" No Profile Status options found after expanding dropdown");
					throw new Exception("No Profile Status filter options found");
				}

				// Get the first checkbox and its corresponding label
				WebElement firstCheckbox = findElements(PROFILE_STATUS_ALL_CHECKBOXES).get(0);
				String profileStatusValue = findElements(PROFILE_STATUS_ALL_LABELS).get(0).getText().trim();

				LOGGER.info("Found Profile Status option: " + profileStatusValue);

				// Scroll to element and click
				js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", firstCheckbox);
				Thread.sleep(300);

				try {
					wait.until(ExpectedConditions.elementToBeClickable(firstCheckbox)).click();
					LOGGER.info(" Clicked Profile Status option using standard click");
				} catch (Exception e) {
					LOGGER.warn("Standard click failed, trying JS click...");
					js.executeScript("arguments[0].click();", firstCheckbox);
					LOGGER.info(" Clicked Profile Status option using JS click");
				}

				Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(findElement(CLOSE_APPLIED_FILTER))).isDisplayed());
								PageObjectHelper.log(LOGGER, "Selected Profile Status Value : " + profileStatusValue
						+ " from Filters dropdown in HCM Sync Profiles screen in PM....");
			} catch (Exception e) {
				LOGGER.error(
						" Issue selecting Profile Status option - Method: apply_profile_status_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab",
						e);
				e.printStackTrace();
				Assert.fail(
						"Issue in selecting a option from Profile Status dropdown in Filters in HCM Sync Profiles screen in PM...Please Investigate!!!");
				PageObjectHelper.log(LOGGER, 
						"Issue in selecting a option from Profile Status dropdown in Filters in HCM Sync Profiles screen in PM...Please Investigate!!!");
			}

			try {
				// PERFORMANCE: Removed Thread.sleep(2000) - scrolling doesn't need fixed delay
				js.executeScript("window.scrollTo(0, 0);"); // Scroll to top (headless-compatible)
				try {
					wait.until(ExpectedConditions.elementToBeClickable(findElement(HCM_SYNC_PROFILES_HEADER))).click();
				} catch (Exception e) {
					try {
						js.executeScript("arguments[0].click();", findElement(HCM_SYNC_PROFILES_HEADER));
					} catch (Exception s) {
						jsClick(findElement(HCM_SYNC_PROFILES_HEADER));
					}
				}
				Assert.assertTrue(wait.until(ExpectedConditions.invisibilityOf(findElement(FILTER_OPTIONS))));
				PageObjectHelper.log(LOGGER, "Filters dropdown closed successfully in HCM Sync Profiles screen in PM");
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail(
						"Issue in closing filters dropdown in HCM Sync Profiles screen in PM...Please Investigate!!!");
				PageObjectHelper.log(LOGGER, 
						"Issue in closing filters dropdown in HCM Sync Profiles screen in PM...Please Investigate!!!");
			}

			try {
				// PERFORMANCE: Single comprehensive wait after closing dropdown
				PerformanceUtils.waitForPageReady(driver, 5);

				// Check if there are no results after applying the filter
				boolean noResults = false;
				try {
					if (findElement(NO_RESULTS_MESSAGE).isDisplayed() || findElement(NO_SP_MSG).isDisplayed()) {
						noResults = true;
					}
				} catch (Exception e) {
					// Elements not found, meaning there are results
					noResults = false;
				}

				if (noResults) {
					LOGGER.warn(
							" NO RESULTS FOUND after applying Profile Status filter - This is acceptable, the selected filter value returned 0 results");
					PageObjectHelper.log(LOGGER, 
							" NO RESULTS - The applied Profile Status filter returned 0 results. This is an expected scenario.");
					try {
						String countText = findElement(SHOWING_JOB_RESULTS_COUNT).getText();
						PageObjectHelper.log(LOGGER, "Results count shows: " + countText);
					} catch (Exception ex) {
						LOGGER.info("No results count displayed - 0 profiles match the filter");
					}
				} else {
					// There are results, verify count changed
					PerformanceUtils.waitForElement(driver, findElement(SHOWING_JOB_RESULTS_COUNT));
					String resultsCountText2 = wait.until(ExpectedConditions.visibilityOf(findElement(SHOWING_JOB_RESULTS_COUNT)))
							.getText();
					Assert.assertNotEquals(updatedResultsCount.get(), resultsCountText2);
					if (!resultsCountText2.equals(updatedResultsCount.get())) {
												PageObjectHelper.log(LOGGER, "Success Profiles Results count updated and Now "
								+ resultsCountText2
								+ " as expected after applying Profile Status Filters in HCM Sync Profiles screen in PM");
					} else {
						PageObjectHelper.log(LOGGER, "Issue in updating success profiles results count, Still "
								+ resultsCountText2
								+ " after applying Profile Status Filters in HCM Sync Profiles screen in PM....Please Investiagte!!!");
						throw new Exception("Issue in updating success profiles results count, Still "
								+ resultsCountText2
								+ " after applying Profile Status Filters in HCM Sync Profiles screen in PM....Please Investiagte!!!");
					}
				}
			} catch (Exception e) {
				LOGGER.error(
						" Issue verifying profiles count after Profile Status filter - Method: apply_profile_status_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab",
						e);
				e.printStackTrace();
				Assert.fail(
						"Issue in verifying success profiles results count after scrolling page down in HCM Sync Profiles screen in PM...Please Investigate!!!");
				PageObjectHelper.log(LOGGER, 
						"Issue in verifying success profiles results count after scrolling page down in in HCM Sync Profiles screen in PM...Please Investigate!!!");
			}
		} catch (Exception e) {
			LOGGER.error(
					" Issue applying Profile Status filter - Method: apply_profile_status_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab",
					e);
			e.printStackTrace();
			Assert.fail(
					"Issue in verifying Profile Status Filter and Verifing Profiles count is correctly displaying in HCM Sync Profiles screen in PM...Please Investigate!!!");
			PageObjectHelper.log(LOGGER, 
					"Issue in verifying Profile Status Filter and Verifing Profiles count is correctly displaying in HCM Sync Profiles screen in PM...Please Investigate!!!");
		}
	}

	public void user_should_verify_organization_jobs_table_headers_are_correctly_displaying_in_hcm_sync_profiles_tab() {
		try {
			// Verify all table headers using a more compact approach
			verifyHeaderText(TABLE_HEADER1, "NAME");
			verifyHeaderText(TABLE_HEADER2, "STATUS");
			verifyHeaderText(TABLE_HEADER3, "KF GRADE");
			verifyHeaderText(TABLE_HEADER4, "LEVEL");
			verifyHeaderText(TABLE_HEADER5, "FUNCTION");
			verifyHeaderText(TABLE_HEADER6, "CREATED BY");
			verifyHeaderText(TABLE_HEADER7, "LAST MODIFIED");
			verifyHeaderText(TABLE_HEADER8, "EXPORT STATUS");
			PageObjectHelper.log(LOGGER, "Organization jobs table headers verified successfully");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_organization_jobs_table_headers_are_correctly_displaying_in_hcm_sync_profiles_tab",
					"Issue verifying table headers", e);
		}
	}

	/**
	 * Helper method to verify table header text
	 */
	private void verifyHeaderText(By headerLocator, String expectedText) {
		String actualText = wait.until(ExpectedConditions.visibilityOf(findElement(headerLocator))).getText();
		Assert.assertEquals(actualText, expectedText, "Header mismatch: expected '" + expectedText + "' but got '" + actualText + "'");
	}

	public void user_should_verify_download_button_is_disabled_in_hcm_sync_profiles_tab() {
		try {
			Assert.assertTrue(!wait.until(ExpectedConditions.visibilityOf(findElement(DOWNLOAD_BTN))).isEnabled(),
					"Download button should be disabled");
			PageObjectHelper.log(LOGGER, "Download button is disabled as expected");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_download_button_is_disabled_in_hcm_sync_profiles_tab",
					"Issue verifying download button is disabled", e);
		}
	}

	public void click_on_header_checkbox_to_select_loaded_job_profiles_in_hcm_sync_profiles_tab() {
		try {
			// Step 1: Store count of profiles loaded BEFORE clicking header checkbox
			String resultsCountText = wait.until(ExpectedConditions.visibilityOf(findElement(SHOWING_JOB_RESULTS_COUNT))).getText();
			String[] resultsCountText_split = resultsCountText.split(" ");
			loadedProfilesBeforeHeaderCheckboxClick.set(Integer.parseInt(resultsCountText_split[1]));
			LOGGER.info("Loaded profiles on screen (BEFORE header checkbox click): "
					+ loadedProfilesBeforeHeaderCheckboxClick.get());

			// Step 2: Click header checkbox - try multiple approaches for kf-checkbox
			WebElement headerCheckbox = waitForElement(TABLE_HEADER_CHECKBOX, 10);
			try {
				WebElement innerInput = headerCheckbox.findElement(By.xpath(".//input | .//span | .//*[contains(@class,'checkbox')]"));
				jsClick(innerInput);
			} catch (Exception e1) {
				try {
					jsClick(headerCheckbox);
				} catch (Exception e2) {
					new Actions(driver).moveToElement(headerCheckbox).click().perform();
				}
			}
			
			// Wait for selection to complete
			waitForSpinners();
			safeSleep(500);

			// Step 3: Count selected and disabled profiles (without scrolling)
			profilesCount.set(loadedProfilesBeforeHeaderCheckboxClick.get());
			disabledProfilesCountInLoadedProfiles.set(0);

			for (int i = 1; i <= loadedProfilesBeforeHeaderCheckboxClick.get(); i++) {
				try {
					WebElement SP_Checkbox = driver.findElement(
							By.xpath("//tbody//tr[" + Integer.toString(i) + "]//td[1]//*//..//div//kf-checkbox//div"));
					// REMOVED: Scroll operation -
					// js.executeScript("arguments[0].scrollIntoView(true);", SP_Checkbox);
					String text = SP_Checkbox.getAttribute("class");
					if (text.contains("disable")) {
						PageObjectHelper.log(LOGGER, "Success profile with No Job Code assigned is found....");
						disabledProfilesCountInLoadedProfiles.set(disabledProfilesCountInLoadedProfiles.get() + 1);
						profilesCount.set(profilesCount.get() - 1);
					}
				} catch (Exception e) {
					// PERFORMANCE: Single comprehensive wait
					PerformanceUtils.waitForPageReady(driver, 5);
					WebElement SP_Checkbox = driver.findElement(
							By.xpath("//tbody//tr[" + Integer.toString(i) + "]//td[1]//*//..//div//kf-checkbox//div"));
					// REMOVED: Scroll operation -
					// js.executeScript("arguments[0].scrollIntoView(true);", SP_Checkbox);
					String text = SP_Checkbox.getAttribute("class");
					if (text.contains("disable")) {
						PageObjectHelper.log(LOGGER, "Success profile with No Job Code assigned is found....");
						disabledProfilesCountInLoadedProfiles.set(disabledProfilesCountInLoadedProfiles.get() + 1);
						profilesCount.set(profilesCount.get() - 1);
					}
				}
			}

			// Step 4: Store selected profiles count
			selectedProfilesAfterHeaderCheckboxClick.set(profilesCount.get());

			PageObjectHelper.log(LOGGER, 
					"Clicked on header checkbox and selected " + selectedProfilesAfterHeaderCheckboxClick.get()
							+ " job profiles in HCM Sync Profiles screen in PM. Loaded: " 
							+ loadedProfilesBeforeHeaderCheckboxClick.get());
		} catch (Exception e) {
			LOGGER.error(
					" Issue clicking header checkbox to select loaded profiles - Method: click_on_header_checkbox_to_select_loaded_profiles_in_hcm_sync_profiles_tab",
					e);
			e.printStackTrace();
			Assert.fail(
					"Issue in clicking on header checkbox to select loaded job profiles in HCM Sync Profiles screen in PM...Please Investigate!!!");
			PageObjectHelper.log(LOGGER, 
					"Issue in clicking on header checkbox to select loaded job profiles in in HCM Sync Profiles screen in PM...Please Investigate!!!");
		}
	}

	public void user_should_verify_download_button_is_enabled_in_hcm_sync_profiles_tab() {
		try {
			js.executeScript("window.scrollTo(0, 0);");
			waitForPageStability(3);
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(findElement(DOWNLOAD_BTN))).isEnabled(),
					"Download button should be enabled");
			PageObjectHelper.log(LOGGER, "Download button is enabled as expected after selecting profiles");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_download_button_is_enabled_in_hcm_sync_profiles_tab",
					"Issue verifying download button is enabled", e);
		}
	}

	public void user_should_uncheck_header_checkbox_to_deselect_selected_job_profiles_in_hcm_sync_profiles_tab() {
		try {
			js.executeScript("window.scrollTo(0, 0);");
			waitForSpinners();
			safeSleep(500);
			
			int profilesBeforeDeselect = selectedProfilesAfterHeaderCheckboxClick.get();
			LOGGER.info("Selected profiles count (BEFORE unchecking header checkbox): " + profilesBeforeDeselect);

			// Click header checkbox to deselect all - try multiple approaches
			WebElement headerCheckbox = waitForElement(TABLE_HEADER_CHECKBOX, 10);
			
			// Try clicking the inner input element first (more reliable for kf-checkbox)
			try {
				WebElement innerInput = headerCheckbox.findElement(By.xpath(".//input | .//span | .//*[contains(@class,'checkbox')]"));
				jsClick(innerInput);
			} catch (Exception e1) {
				// Fallback to clicking the kf-checkbox itself
				try {
					jsClick(headerCheckbox);
				} catch (Exception e2) {
					// Final fallback using Actions
					new Actions(driver).moveToElement(headerCheckbox).click().perform();
				}
			}

			// Wait for deselection to complete
			waitForSpinners();
			PerformanceUtils.waitForPageReady(driver, 2);
			safeSleep(1000);

			// Verify deselection - check that no profile checkboxes are selected
			try {
				WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
				shortWait.until(driver -> {
					try {
						// Check if any checkbox is still selected (has check icon)
						List<WebElement> selectedCheckboxes = driver.findElements(
							By.xpath("//tbody//tr//kf-checkbox//kf-icon[@icon='checkbox-check']"));
						return selectedCheckboxes.isEmpty();
					} catch (Exception e) {
						return true;
					}
				});
				LOGGER.info("Verified all checkboxes are deselected");
			} catch (TimeoutException te) {
				LOGGER.warn("Some checkboxes may still be selected after header checkbox click");
			}

			profilesCount.set(0);

			PageObjectHelper.log(LOGGER, "Clicked on header checkbox and deselected all "
					+ profilesBeforeDeselect + " job profiles in HCM Sync Profiles screen in PM");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_uncheck_header_checkbox_to_deselect_selected_job_profiles_in_hcm_sync_profiles_tab",
					"Issue clicking header checkbox to deselect all profiles", e);
		}
	}

	public void click_on_first_profile_checkbox_in_hcm_sync_profiles_tab() {
		try {
			jobname1.set(wait.until(ExpectedConditions.visibilityOf(findElement(HCM_SYNC_PROFILES_JOB_ROW1))).getText());
			
			// Check if already selected - skip if yes
			if (isCheckboxSelected(1)) return;
			
			clickWithFallback(PROFILE1_CHECKBOX);
			safeSleep(200);
			
			// Only increment count if checkbox is actually selected after clicking
			if (isCheckboxSelected(1)) {
				profilesCount.set(profilesCount.get() + 1);
				PageObjectHelper.log(LOGGER, "Selected First profile: " + jobname1.get());
			}
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_first_profile_checkbox_in_hcm_sync_profiles_tab",
					"Issue clicking first profile checkbox", e);
		}
	}

	/**
	 * Helper method to check if a profile checkbox is selected
	 */
	private boolean isCheckboxSelected(int rowNumber) {
		try {
			driver.findElement(By.xpath("//tbody//tr[" + rowNumber + "]//td[1]//kf-checkbox//kf-icon[@icon='checkbox-check']"));
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	public void click_on_second_profile_checkbox_in_hcm_sync_profiles_tab() {
		try {
			scrollToAndWait(findElement(HCM_SYNC_PROFILES_JOB_ROW2));
			jobname2.set(wait.until(ExpectedConditions.visibilityOf(findElement(HCM_SYNC_PROFILES_JOB_ROW2))).getText());
			
			// Check if already selected - skip if yes
			if (isCheckboxSelected(2)) return;
			
			clickWithFallback(PROFILE2_CHECKBOX);
			safeSleep(200);
			
			// Only increment count if checkbox is actually selected after clicking
			if (isCheckboxSelected(2)) {
				profilesCount.set(profilesCount.get() + 1);
				PageObjectHelper.log(LOGGER, "Selected Second profile: " + jobname2.get());
			}
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_second_profile_checkbox_in_hcm_sync_profiles_tab",
					"Issue clicking second profile checkbox", e);
		}
	}

	public void click_on_third_profile_checkbox_in_hcm_sync_profiles_tab() {
		try {
			waitForPageStability(3);

			// Wait for third row to be present
			WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement row3 = shortWait.until(
					ExpectedConditions.presenceOfElementLocated(By.xpath("//tbody//tr[3]//td//div//span[1]//a")));

			scrollToAndWait(row3);
			jobname3.set(shortWait.until(ExpectedConditions.visibilityOf(row3)).getText());

			// Check if already selected - skip if yes
			if (isCheckboxSelected(3)) return;

			clickWithFallback(PROFILE3_CHECKBOX);
			safeSleep(200);
			
			// Only increment count if checkbox is actually selected after clicking
			if (isCheckboxSelected(3)) {
				profilesCount.set(profilesCount.get() + 1);
				PageObjectHelper.log(LOGGER, "Selected Third profile: " + jobname3.get());
			}
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_third_profile_checkbox_in_hcm_sync_profiles_tab",
					"Issue clicking third profile checkbox", e);
		}
	}

	public void user_should_verify_sync_with_hcm_button_is_disabled_in_hcm_sync_profiles_tab() {
		try {
			// PERFORMANCE: Single comprehensive wait before checking button state
			PerformanceUtils.waitForPageReady(driver, 5);

			// Wait for button state to update with retry logic
			WebDriverWait buttonWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
			boolean isDisabled = buttonWait.until(driver -> {
				try {
					WebElement syncButton = wait.until(ExpectedConditions.visibilityOf(findElement(SYNC_WITH_HCM_BTN)));

					// Check 1: Check for 'text-disabled' class (Angular Material disabled state)
					String classAttribute = syncButton.getAttribute("class");
					if (classAttribute != null && classAttribute.contains("text-disabled")) {
						LOGGER.debug("Button has 'text-disabled' class - Button is disabled");
						return true;
					}

					// Check 2: Check for 'disabled' attribute
					String disabledAttribute = syncButton.getAttribute("disabled");
					if (disabledAttribute != null) {
						LOGGER.debug("Button has 'disabled' attribute - Button is disabled");
						return true;
					}

					// Check 3: Check for 'aria-disabled' attribute
					String ariaDisabled = syncButton.getAttribute("aria-disabled");
					if ("true".equalsIgnoreCase(ariaDisabled)) {
						LOGGER.debug("Button has 'aria-disabled=true' - Button is disabled");
						return true;
					}

					// Check 4: Fallback to isEnabled() check
					if (!syncButton.isEnabled()) {
						LOGGER.debug("Button.isEnabled() returned false - Button is disabled");
						return true;
					}

					LOGGER.debug("Button not yet disabled, retrying...");
					return false;
				} catch (Exception e) {
					LOGGER.debug("Error checking button state, retrying...");
					return false;
				}
			});

			Assert.assertTrue(isDisabled, "Sync with HCM button should be disabled but appears to be enabled");
			PageObjectHelper.log(LOGGER, " Sync with HCM button is disabled as expected in HCM Sync Profiles screen in PM");
		} catch (Exception e) {
			LOGGER.error(
					" Issue verifying Sync with HCM button disabled - Method: user_should_verify_sync_with_hcm_button_is_disabled_in_hcm_sync_profiles_tab",
					e);
			e.printStackTrace();
			Assert.fail(
					"Issue in verifying Sync with HCM button is disabled in HCM Sync Profiles screen in PM...Please Investigate!!!");
			PageObjectHelper.log(LOGGER, 
					"Issue in verifying Sync with HCM button is disabled in HCM Sync Profiles screen in PM...Please Investigate!!!");
		}
	}

	public void user_should_verify_sync_with_hcm_button_is_enabled_in_hcm_sync_profiles_tab() {
		try {
			// PERFORMANCE: Single comprehensive wait before checking button state
			PerformanceUtils.waitForPageReady(driver, 5);

			// Wait for button state to update with retry logic
			WebDriverWait buttonWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
			boolean isEnabled = buttonWait.until(driver -> {
				try {
					WebElement syncButton = wait.until(ExpectedConditions.visibilityOf(findElement(SYNC_WITH_HCM_BTN)));

					// Check 1: Verify 'text-disabled' class is NOT present
					String classAttribute = syncButton.getAttribute("class");
					if (classAttribute != null && classAttribute.contains("text-disabled")) {
						LOGGER.debug("Button has 'text-disabled' class - Button still disabled, retrying...");
						return false;
					}

					// Check 2: Verify 'disabled' attribute is NOT present
					String disabledAttribute = syncButton.getAttribute("disabled");
					if (disabledAttribute != null) {
//						LOGGER.debug("Button has 'disabled' attribute - Button still disabled, retrying...");
						return false;
					}

					// Check 3: Verify 'aria-disabled' is not 'true'
					String ariaDisabled = syncButton.getAttribute("aria-disabled");
					if ("true".equalsIgnoreCase(ariaDisabled)) {
						LOGGER.debug("Button has 'aria-disabled=true' - Button still disabled, retrying...");
						return false;
					}

					// Check 4: Verify isEnabled() returns true
					if (!syncButton.isEnabled()) {
						LOGGER.debug("Button.isEnabled() returned false - Button still disabled, retrying...");
						return false;
					}

					LOGGER.debug("Button is enabled!");
					return true;
				} catch (Exception e) {
					LOGGER.debug("Error checking button state, retrying...");
					return false;
				}
			});

			Assert.assertTrue(isEnabled, "Sync with HCM button should be enabled but appears to be disabled");
			PageObjectHelper.log(LOGGER, " Sync with HCM button is enabled as expected in HCM Sync Profiles screen in PM");
		} catch (Exception e) {
			LOGGER.error(
					" Issue verifying Sync with HCM button enabled - Method: user_should_verify_sync_with_hcm_button_is_enabled_in_hcm_sync_profiles_tab",
					e);
			e.printStackTrace();
			Assert.fail(
					"Issue in verifying Sync with HCM button is enabled in HCM Sync Profiles screen in PM...Please Investigate!!!");
			PageObjectHelper.log(LOGGER, 
					"Issue in verifying Sync with HCM button is enabled in HCM Sync Profiles screen in PM...Please Investigate!!!");
		}
	}

	public void verify_checkboxes_of_first_second_and_third_profile_are_selected_in_hcm_sync_profiles_tab() {
		try {
			// PERFORMANCE: Single comprehensive wait
			PerformanceUtils.waitForPageReady(driver, 5);
			String jobname1 = wait.until(ExpectedConditions.visibilityOf(findElement(HCM_SYNC_PROFILES_JOB_ROW1))).getText();
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(findElement(PROFILE1_CHECKBOX))).isSelected());
						PageObjectHelper.log(LOGGER, "First job profile with name : " + jobname1
					+ " is Already Selected in HCM Sync Profiles screen in PM");

			String jobname2 = wait.until(ExpectedConditions.visibilityOf(findElement(HCM_SYNC_PROFILES_JOB_ROW2))).getText();
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(findElement(PROFILE2_CHECKBOX))).isSelected());
						PageObjectHelper.log(LOGGER, "Second job profile with name : " + jobname2
					+ " is Already Selected in HCM Sync Profiles screen in PM");

			String jobname3 = wait.until(ExpectedConditions.visibilityOf(findElement(HCM_SYNC_PROFILES_JOB_ROW3))).getText();
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(findElement(PROFILE3_CHECKBOX))).isSelected());
						PageObjectHelper.log(LOGGER, "Third job profile with name : " + jobname3
					+ " is Already Selected in HCM Sync Profiles screen in PM");
		} catch (Exception e) {
			LOGGER.error(
					" Issue verifying profile checkboxes selected - Method: verify_checkboxes_of_first_second_and_third_profile_are_selected_in_hcm_sync_profiles_tab",
					e);
			e.printStackTrace();
			Assert.fail(
					"Issue in Verifying whether checkboxes of First, Second and Third Profile are Selected or Not in HCM Sync Profiles screen in PM...Please Investigate!!!");
			PageObjectHelper.log(LOGGER, 
					"Issue in Verifying whether checkboxes of First, Second and Third Profile are Selected or Not in HCM Sync Profiles screen in PM...Please Investigate!!!");
		}
	}

	public void click_on_sync_with_hcm_button_in_hcm_sync_profiles_tab() {
		try {
			js.executeScript("window.scrollTo(0, 0);");
			waitForPageStability(3);
			clickWithFallback(SYNC_WITH_HCM_BTN);
			PageObjectHelper.log(LOGGER, "Clicked on Sync with HCM button");
			waitForSpinners();
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_sync_with_hcm_button_in_hcm_sync_profiles_tab",
					"Issue clicking Sync with HCM button", e);
		}
	}

	public void user_should_verify_sync_with_hcm_success_popup_appears_on_screen_in_hcm_sync_profiles_tab() {
		try {
			waitForSpinners();
			WebElement successPopup = wait.until(ExpectedConditions.visibilityOf(findElement(SYNC_WITH_HCM_SUCCESS_POPUP_TEXT)));
			String SyncwithHCMSuccessMsg = successPopup.getText();
			PageObjectHelper.log(LOGGER, "Sync with HCM Success Popup Message : " + SyncwithHCMSuccessMsg);

			// Check if message contains "failed" - if so, mark scenario as failed but
			// continue
			if (SyncwithHCMSuccessMsg != null && SyncwithHCMSuccessMsg.toLowerCase().contains("failed")) {
				PageObjectHelper.log(LOGGER, "Message: " + SyncwithHCMSuccessMsg);

				// Close the popup before failing
				try {
					wait.until(ExpectedConditions.visibilityOf(findElement(SYNC_WITH_HCM_SUCCESS_POPUP_CLOSE_BTN))).click();
					LOGGER.info("Failure popup closed");
				} catch (Exception closeEx) {
					LOGGER.warn("Could not close failure popup: " + closeEx.getMessage());
				}

				// Capture screenshot and fail the scenario
				Assert.fail(SyncwithHCMSuccessMsg);
			}

			// If message doesn't contain "failed", verify it's the expected success message
			Assert.assertEquals(SyncwithHCMSuccessMsg,
					"Your profiles are being exported. This may take a few minutes to complete");
			wait.until(ExpectedConditions.visibilityOf(findElement(SYNC_WITH_HCM_SUCCESS_POPUP_CLOSE_BTN))).click();
			PageObjectHelper.log(LOGGER, 
					"Sync with HCM Success Popup closed successfully in HCM Sync Profiles screen in PM....");
		} catch (AssertionError e) {
			LOGGER.error(
					"Assertion failed in verifying Sync with HCM success popup - Method: user_should_verify_sync_with_hcm_success_popup_appears_on_screen_in_hcm_sync_profiles_tab",
					e);
			e.printStackTrace();
			PageObjectHelper.log(LOGGER, 
					"Issue in Verifying appearance of Sync with HCM Success Popup in HCM Sync Profiles screen in PM...Please Investigate!!!");
			throw e; // Re-throw to mark scenario as failed but allow next scenarios to continue
		} catch (Exception e) {
			LOGGER.error(
					"Issue verifying Sync with HCM success popup - Method: user_should_verify_sync_with_hcm_success_popup_appears_on_screen_in_hcm_sync_profiles_tab",
					e);
			e.printStackTrace();
			PageObjectHelper.log(LOGGER, 
					"Issue in Verifying appearance of Sync with HCM Success Popup in HCM Sync Profiles screen in PM...Please Investigate!!!");
			Assert.fail(
					"Issue in Verifying appearance of Sync with HCM Success Popup in HCM Sync Profiles screen in PM...Please Investigate!!!");
		}

		try {
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(findElement(SYNC_WITH_HCM_WARNING_MSG))).isDisplayed());
			String warningMsgText = wait.until(ExpectedConditions.visibilityOf(findElement(SYNC_WITH_HCM_WARNING_MSG)))
					.getText();
			PageObjectHelper.log(LOGGER, "Sync with HCM Warning Message : " + warningMsgText);
			wait.until(ExpectedConditions.visibilityOf(findElement(SYNC_WITH_HCM_WARNING_MSG_CLOSE_BTN))).click();
			PageObjectHelper.log(LOGGER, 
					"Sync with HCM Warning Message closed successfully in HCM Sync Profiles screen in PM....");
		} catch (Exception e) {
			LOGGER.error(
					"Issue verifying Sync with HCM warning message - Method: user_should_verify_sync_with_hcm_success_popup_appears_on_screen_in_hcm_sync_profiles_tab",
					e);
			e.printStackTrace();
			Assert.fail(
					"Issue in Verifying appearance of Sync with HCM Warning Message in HCM Sync Profiles screen in PM...Please Investigate!!!");
			PageObjectHelper.log(LOGGER, 
					"Issue in Verifying appearance of Sync with HCM Warning Message in HCM Sync Profiles screen in PM...Please Investigate!!!");
		}
	}

	public void verify_checkboxes_of_first_second_and_third_profile_are_deselected_in_hcm_sync_profiles_tab() {
		try {
			PerformanceUtils.waitForPageReady(driver, 5);
			String jobname1 = wait.until(ExpectedConditions.visibilityOf(findElement(HCM_SYNC_PROFILES_JOB_ROW1))).getText();
			Assert.assertTrue(!(wait.until(ExpectedConditions.visibilityOf(findElement(PROFILE1_CHECKBOX))).isSelected()));
						PageObjectHelper.log(LOGGER, "First job profile with name : " + jobname1
					+ " is De-Selected as expected after Sync with HCM is successful in HCM Sync Profiles screen in PM");

			String jobname2 = wait.until(ExpectedConditions.visibilityOf(findElement(HCM_SYNC_PROFILES_JOB_ROW2))).getText();
			Assert.assertTrue(!(wait.until(ExpectedConditions.visibilityOf(findElement(PROFILE2_CHECKBOX))).isSelected()));
						PageObjectHelper.log(LOGGER, "Second job profile with name : " + jobname2
					+ " is De-Selected as expected after Sync with HCM is successful in HCM Sync Profiles screen in PM");

			String jobname3 = wait.until(ExpectedConditions.visibilityOf(findElement(HCM_SYNC_PROFILES_JOB_ROW3))).getText();
			Assert.assertTrue(!(wait.until(ExpectedConditions.visibilityOf(findElement(PROFILE3_CHECKBOX))).isSelected()));
						PageObjectHelper.log(LOGGER, "Third job profile with name : " + jobname3
					+ " is De-Selected as expected after Sync with HCM is successful in HCM Sync Profiles screen in PM");
		} catch (Exception e) {
			LOGGER.error(
					" Issue verifying profile checkboxes deselected - Method: verify_checkboxes_of_first_second_and_third_profile_are_deselected_in_hcm_sync_profiles_tab",
					e);
			e.printStackTrace();
			Assert.fail(
					"Issue in Verifying whether checkboxes of First, Second and Third Profile are De-Selected or Not in HCM Sync Profiles screen in PM...Please Investigate!!!");
			PageObjectHelper.log(LOGGER, 
					"Issue in Verifying whether checkboxes of First, Second and Third Profile are De-Selected or Not in HCM Sync Profiles screen in PM...Please Investigate!!!");
		}
	}

}
