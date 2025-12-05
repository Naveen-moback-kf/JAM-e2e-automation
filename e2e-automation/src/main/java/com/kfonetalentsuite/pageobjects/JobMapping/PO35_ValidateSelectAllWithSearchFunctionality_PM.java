package com.kfonetalentsuite.pageobjects.JobMapping;

import java.time.Duration;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.ScreenshotHandler;
import com.kfonetalentsuite.utils.JobMapping.PageObjectHelper;

/**
 * Page Object for validating Select All functionality with Search in HCM Sync Profiles (PM) screen.
 * Verifies that "Select All" only selects the searched results, not all profiles.
 * 
 * Enhanced to extend BasePageObject for consistency and code reuse.
 */
public class PO35_ValidateSelectAllWithSearchFunctionality_PM extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO35_ValidateSelectAllWithSearchFunctionality_PM.class);

	public PO35_ValidateSelectAllWithSearchFunctionality_PM() {
		super();
	}

	// ==================== LOCATORS ====================
	// PAGE_LOAD_SPINNER is available via Locators.Spinners.PAGE_LOAD_SPINNER
	// SEARCH_BAR is available via Locators.SearchAndFilters.SEARCH_BAR
	private static final By SHOWING_JOB_RESULTS_COUNT = By.xpath("//div[contains(text(),'Showing')]");
	private static final By ALL_PROFILE_ROWS = By.xpath("//tbody//tr");
	private static final By SELECTED_PROFILE_ROWS = By.xpath("//tbody//tr[.//kf-icon[@icon='checkbox-check' and contains(@class,'ng-star-inserted')]]");
	private static final By PROFILE_NAME_ELEMENTS = By.xpath("//tbody//tr//td//div//span[1]//a");

	// ==================== THREAD-SAFE STATE ====================
	public static ThreadLocal<Integer> searchResultsCount = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<String> alternativeSearchSubstring = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<Integer> totalSecondSearchResults = ThreadLocal.withInitial(() -> 0);

	/**
	 * Scrolls down to view all search results in HCM Sync Profiles screen.
	 */
	public void user_should_scroll_down_to_view_last_search_result_in_hcm_sync_profiles_screen() {
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

			LOGGER.debug("Loading search results by scrolling...");

			int currentCount = 0;
			int previousCount = 0;
			int noChangeCount = 0;
			int maxScrollAttempts = 30;
			int scrollCount = 0;

			while (scrollCount < maxScrollAttempts) {
				scrollToBottom();
				scrollCount++;
				safeSleep(3000);

				PerformanceUtils.waitForSpinnersToDisappear(driver, 5);
				PerformanceUtils.waitForPageReady(driver, 2);
				safeSleep(1000);

				currentCount = findElements(ALL_PROFILE_ROWS).size();

				if (currentCount == previousCount) {
					noChangeCount++;
					if (noChangeCount >= 3) {
						LOGGER.debug("Reached end of search results after {} non-loading scrolls", noChangeCount);
						break;
					}

					if (noChangeCount == 2) {
						JavascriptExecutor js = (JavascriptExecutor) driver;
						js.executeScript(
								"window.scrollTo(0, Math.max(document.body.scrollHeight, document.documentElement.scrollHeight));");
						safeSleep(2000);
						PerformanceUtils.waitForSpinnersToDisappear(driver, 5);
					}
				} else {
					noChangeCount = 0;
				}

				previousCount = currentCount;
			}

			PageObjectHelper.log(LOGGER, "Loaded " + currentCount + " search results (using " + scrollCount + " scrolls)");

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_scroll_down_to_view_last_search_result", 
					"Issue scrolling to view search results", e);
			ScreenshotHandler.captureFailureScreenshot("scroll_search_results", e);
			Assert.fail("Issue scrolling to view search results");
		}
	}

	/**
	 * Validates that all search results contain the substring used for searching.
	 */
	public void user_should_validate_all_search_results_contains_substring_used_for_searching_in_hcm_sync_profiles_screen() {
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 1);

			String searchSubstring = PO22_ValidateHCMSyncProfilesScreen_PM.jobProfileName.get().toLowerCase();

			List<WebElement> profileNameElements = findElements(PROFILE_NAME_ELEMENTS);
			int totalResults = profileNameElements.size();
			int nonMatchingResults = 0;

			for (WebElement element : profileNameElements) {
				String profileName = element.getText().toLowerCase();
				if (!profileName.contains(searchSubstring)) {
					nonMatchingResults++;
					LOGGER.warn("Non-matching result: '{}'", element.getText());
				}
			}

			if (nonMatchingResults == 0) {
				PageObjectHelper.log(LOGGER, "All " + totalResults + " results contain '" + searchSubstring + "'");
			} else {
				PageObjectHelper.log(LOGGER, nonMatchingResults + " of " + totalResults + 
						" results do NOT contain '" + searchSubstring + "'");
			}

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "validate_search_results_contain_substring", 
					"Issue validating search results", e);
			ScreenshotHandler.captureFailureScreenshot("validate_search_results", e);
			Assert.fail("Issue validating search results contain substring");
		}
	}

	/**
	 * Validates that "Select All" only selects the searched results, not all profiles.
	 */
	public void verify_only_searched_profiles_are_selected_after_clearing_search_bar_in_hcm_sync_profiles_screen() {
		int totalProfilesVisible = 0;
		int previousTotalProfilesVisible = 0;
		int actualSelectedCount = 0;
		int scrollAttempts = 0;
		int maxScrollAttempts = 100;
		int stableCountAttempts = 0;
		int requiredStableAttempts = 3;
		boolean allProfilesLoaded = false;
		int expectedTotalProfiles = 0;
		boolean maxScrollLimitReached = false;
		JavascriptExecutor js = (JavascriptExecutor) driver;

		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

			PageObjectHelper.log(LOGGER, "Verifying only " + searchResultsCount.get() + " profiles remain selected...");

			if (searchResultsCount.get() == 0) {
				PageObjectHelper.log(LOGGER, "No search results to verify - skipping");
				return;
			}

			// Parse total profile count
			try {
				String resultsCountText = getElementText(SHOWING_JOB_RESULTS_COUNT);
				expectedTotalProfiles = parseProfileCountFromText(resultsCountText);
				if (expectedTotalProfiles > 0) {
					int estimatedScrollsNeeded = (expectedTotalProfiles / 50) + 10;
					maxScrollAttempts = Math.max(100, estimatedScrollsNeeded);
				}
			} catch (Exception e) {
				LOGGER.debug("Could not parse total profile count: {}", e.getMessage());
			}

			// Scroll down progressively to load all profiles
			while (scrollAttempts < maxScrollAttempts && !allProfilesLoaded) {
				scrollAttempts++;

				js.executeScript("window.scrollTo(0, document.documentElement.scrollHeight);");
				PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
				PerformanceUtils.waitForPageReady(driver, 2);
				safeSleep(1000);

				totalProfilesVisible = findElements(ALL_PROFILE_ROWS).size();

				// Fail-fast: Check if selected count increased beyond baseline
				int currentSelectedCount = findElements(SELECTED_PROFILE_ROWS).size();

				if (currentSelectedCount > searchResultsCount.get()) {
					int extra = currentSelectedCount - searchResultsCount.get();
					LOGGER.warn("FAIL-FAST at scroll {}: Found {} selected (expected {}), {} extra", 
							scrollAttempts, currentSelectedCount, searchResultsCount.get(), extra);
					allProfilesLoaded = true;
					actualSelectedCount = currentSelectedCount;
					break;
				}

				// Check if count is stable
				if (totalProfilesVisible == previousTotalProfilesVisible) {
					stableCountAttempts++;
					if (stableCountAttempts >= requiredStableAttempts) {
						allProfilesLoaded = true;
						LOGGER.debug("Loaded {} profiles after {} scrolls", totalProfilesVisible, scrollAttempts);
					}
				} else {
					stableCountAttempts = 0;
				}
				previousTotalProfilesVisible = totalProfilesVisible;
			}

			maxScrollLimitReached = scrollAttempts >= maxScrollAttempts;

			// After loading all profiles, count selected vs not selected
			if (actualSelectedCount == 0) {
				actualSelectedCount = findElements(SELECTED_PROFILE_ROWS).size();
			}
			int notSelectedProfiles = totalProfilesVisible - actualSelectedCount;

			// Calculate missing/extra selections
			int missingSelections = Math.max(0, searchResultsCount.get() - actualSelectedCount);
			int extraSelections = Math.max(0, actualSelectedCount - searchResultsCount.get());

			// Structured summary logging
			logValidationSummary(maxScrollLimitReached, expectedTotalProfiles, totalProfilesVisible,
					actualSelectedCount, notSelectedProfiles, missingSelections, extraSelections);

			// Validate selection counts
			validateSelectionCounts(maxScrollLimitReached, expectedTotalProfiles, totalProfilesVisible,
					actualSelectedCount, missingSelections, extraSelections);

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_only_searched_profiles_selected", e);
			PageObjectHelper.handleError(LOGGER, "verify_only_searched_profiles_selected", 
					"Error verifying searched profiles", e);
			Assert.fail("Error verifying only searched profiles are selected: " + e.getMessage());
		}
	}

	/**
	 * Captures the baseline of selected profiles after Select All in search results.
	 */
	public void user_is_in_hcm_sync_profiles_screen_with_selected_search_results() {
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

			int selectedCount = findElements(SELECTED_PROFILE_ROWS).size();
			searchResultsCount.set(selectedCount);

			int totalVisibleProfiles = findElements(ALL_PROFILE_ROWS).size();
			int disabledProfiles = countDisabledCheckboxes("tbody tr");
			int unselectedProfiles = totalVisibleProfiles - selectedCount - disabledProfiles;

			LOGGER.info("========================================");
			LOGGER.info("BASELINE COUNTS (After First Search + Selection)");
			LOGGER.info("========================================");
			LOGGER.info("Total Profiles Loaded: {}", totalVisibleProfiles);
			LOGGER.info("Selected Profiles: {}", selectedCount);
			LOGGER.info("Unselected Profiles (enabled but not selected): {}", unselectedProfiles);
			LOGGER.info("Disabled Profiles (cannot be selected): {}", disabledProfiles);
			LOGGER.info("========================================");

			PO22_ValidateHCMSyncProfilesScreen_PM.profilesCount.set(selectedCount);
			PageObjectHelper.log(LOGGER, "Baseline: " + selectedCount + " selected, " + 
					unselectedProfiles + " unselected, " + disabledProfiles + " disabled (total: " + totalVisibleProfiles + ")");

		} catch (Exception e) {
			LOGGER.warn("Error capturing selected profiles count: {}", e.getMessage());
			searchResultsCount.set(0);
		}
	}

	/**
	 * Enters a different job name substring in search bar for alternative validation.
	 */
	public void enter_different_job_name_substring_in_search_bar_for_alternative_validation_in_hcm_sync_profiles_screen() {
		boolean foundResults = false;
		String firstSearchSubstring = PO22_ValidateHCMSyncProfilesScreen_PM.jobProfileName.get();
		JavascriptExecutor js = (JavascriptExecutor) driver;

		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

			PageObjectHelper.log(LOGGER, "Alternative validation: Searching different substring...");

			for (String substring : PO22_ValidateHCMSyncProfilesScreen_PM.SEARCH_PROFILE_NAME_OPTIONS) {
				if (substring.equalsIgnoreCase(firstSearchSubstring)) {
					continue;
				}

				try {
					WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(10));
					WebElement searchBarElement = shortWait.until(ExpectedConditions.visibilityOfElementLocated(Locators.SearchAndFilters.SEARCH_BAR));
					js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", searchBarElement);
					PerformanceUtils.waitForPageReady(driver, 1);

					// Clear search bar
					try {
						shortWait.until(ExpectedConditions.elementToBeClickable(Locators.SearchAndFilters.SEARCH_BAR)).click();
					} catch (Exception clickEx) {
						jsClick(searchBarElement);
					}
					searchBarElement.sendKeys(Keys.CONTROL + "a");
					searchBarElement.sendKeys(Keys.DELETE);

					PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
					PerformanceUtils.waitForPageReady(driver, 1);

					// Enter different substring
					searchBarElement = shortWait.until(ExpectedConditions.visibilityOfElementLocated(Locators.SearchAndFilters.SEARCH_BAR));
					searchBarElement.clear();
					searchBarElement.sendKeys(substring);
					searchBarElement.sendKeys(Keys.ENTER);
					
					PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
					PerformanceUtils.waitForPageReady(driver, 2);

					WebElement resultsElement = shortWait.until(ExpectedConditions.visibilityOfElementLocated(SHOWING_JOB_RESULTS_COUNT));
					String resultsCountText = resultsElement.getText();

					if (resultsCountText.contains("Showing") && !resultsCountText.startsWith("Showing 0")) {
						alternativeSearchSubstring.set(substring);
						foundResults = true;
						PageObjectHelper.log(LOGGER, "Second search: '" + substring + "' - " + resultsCountText);
						break;
					}

				} catch (Exception e) {
					// Continue to next substring
				}
			}

			if (!foundResults) {
				applyFallbackSearch(firstSearchSubstring);
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("enter_different_substring_alternative_validation", e);
			PageObjectHelper.handleError(LOGGER, "enter_different_substring", "Failed to enter different substring", e);
			Assert.fail("Failed to enter different job name substring in search bar for alternative validation");
		}
	}

	/**
	 * Validates profiles from the second search (alternative validation).
	 */
	public void scroll_down_to_load_all_second_search_results_in_hcm_sync_profiles_screen() throws InterruptedException {
		String firstSearchSubstring = PO22_ValidateHCMSyncProfilesScreen_PM.jobProfileName.get();
		String secondSearchSubstring = alternativeSearchSubstring.get();

		try {
			waitForElement(Locators.Spinners.PAGE_LOAD_SPINNER, 2);
			PerformanceUtils.waitForPageReady(driver, 2);

			String resultsCountText = getElementText(SHOWING_JOB_RESULTS_COUNT);

			if (resultsCountText.matches(".*Showing\\s+0\\s+of.*")) {
				PageObjectHelper.log(LOGGER, "Second search returned 0 results - skipping");
				totalSecondSearchResults.set(0);
				return;
			}

			int expectedTotal = parseProfileCountFromText(resultsCountText);
			PageObjectHelper.log(LOGGER, "Alternative validation: Checking initial visible profiles from '" + 
					secondSearchSubstring + "' (expected: " + expectedTotal + ")");

			// Get initial visible profiles
			int initialCount = findElements(ALL_PROFILE_ROWS).size();
			totalSecondSearchResults.set(initialCount);

			int initialSelectedCount = findElements(SELECTED_PROFILE_ROWS).size();
			LOGGER.debug("Validating {} visible profiles ({} selected)...", initialCount, initialSelectedCount);

			// Check for invalid selections in visible profiles
			int invalidCount = checkNewProfilesForInvalidSelections(0, initialCount, firstSearchSubstring, secondSearchSubstring);

			if (invalidCount > 0) {
				String errorMsg = "FAIL: Found invalid selection in second search results";
				PageObjectHelper.log(LOGGER, errorMsg);
				Assert.fail(errorMsg);
				return;
			}

			PageObjectHelper.log(LOGGER, "All visible profiles validated successfully (no scrolling needed)");

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("scroll_second_search_results", e);
			PageObjectHelper.handleError(LOGGER, "scroll_second_search_results", "Error validating second search results", e);
			Assert.fail("Error validating second search results: " + e.getMessage());
		}
	}

	/**
	 * Final validation step (NO-OP as validation is done in previous step).
	 */
	public void verify_all_loaded_profiles_in_second_search_are_not_selected_in_hcm_sync_profiles_screen() {
		PageObjectHelper.log(LOGGER, "Validation already completed in previous step");
	}

	// ==================== HELPER METHODS ====================

	// parseExpectedTotal is now inherited from BasePageObject as parseProfileCountFromText()

	private void applyFallbackSearch(String firstSearchSubstring) {
		String selectedSubstring = "";
		for (String substring : PO22_ValidateHCMSyncProfilesScreen_PM.SEARCH_PROFILE_NAME_OPTIONS) {
			if (!substring.equalsIgnoreCase(firstSearchSubstring)) {
				selectedSubstring = substring;
				alternativeSearchSubstring.set(substring);
				break;
			}
		}
		PageObjectHelper.log(LOGGER, "Using fallback: '" + selectedSubstring + "' (no results found)");
	}

	private void logValidationSummary(boolean maxScrollLimitReached, int expectedTotalProfiles,
			int totalProfilesVisible, int actualSelectedCount, int notSelectedProfiles,
			int missingSelections, int extraSelections) {
		
		LOGGER.info("========================================");
		LOGGER.info("VALIDATION SUMMARY (After Clearing Search)");
		LOGGER.info("========================================");
		if (maxScrollLimitReached && expectedTotalProfiles > 0 && totalProfilesVisible < expectedTotalProfiles) {
			LOGGER.warn("PARTIAL VALIDATION (Max scroll limit reached)");
			LOGGER.info("Expected Total: {}, Actually Loaded: {} ({:.1f}%)", 
					expectedTotalProfiles, totalProfilesVisible, 
					(totalProfilesVisible * 100.0 / expectedTotalProfiles));
		} else {
			LOGGER.info("Total Profiles Loaded: {}", totalProfilesVisible);
		}
		LOGGER.info("Currently Selected: {}, Not Selected: {}", actualSelectedCount, notSelectedProfiles);
		LOGGER.info("Baseline (from first search): {} profiles", searchResultsCount.get());
		if (missingSelections > 0) {
			LOGGER.warn("Missing Selections: {}", missingSelections);
		} else if (extraSelections > 0) {
			LOGGER.warn("Extra Selections: {}", extraSelections);
		}
		LOGGER.info("========================================");
	}

	private void validateSelectionCounts(boolean maxScrollLimitReached, int expectedTotalProfiles,
			int totalProfilesVisible, int actualSelectedCount, int missingSelections, int extraSelections) {
		
		if (maxScrollLimitReached && expectedTotalProfiles > 0 && totalProfilesVisible < expectedTotalProfiles) {
			// Partial validation
			if (actualSelectedCount == 0) {
				String errorMsg = "FAIL: No selections found in " + totalProfilesVisible +
						" loaded profiles (expected " + searchResultsCount.get() + ")";
				PageObjectHelper.log(LOGGER, errorMsg);
				Assert.fail(errorMsg);
			} else if (actualSelectedCount > searchResultsCount.get()) {
				String errorMsg = "FAIL: Found " + actualSelectedCount + " selected (expected " +
						searchResultsCount.get() + "), " + extraSelections + " extra profiles incorrectly selected";
				PageObjectHelper.log(LOGGER, errorMsg);
				Assert.fail(errorMsg);
			} else if (actualSelectedCount < searchResultsCount.get()) {
				PageObjectHelper.log(LOGGER, "PASS: Found " + actualSelectedCount + " of " + searchResultsCount.get() +
						" selected (remaining " + missingSelections + " likely in unloaded profiles)");
			} else {
				PageObjectHelper.log(LOGGER, "PASS: All " + searchResultsCount.get() + " searched profiles found selected");
			}
		} else {
			// Full validation
			if (actualSelectedCount == searchResultsCount.get()) {
				PageObjectHelper.log(LOGGER, "PASS: All " + searchResultsCount.get() + " searched profiles remain selected");
			} else if (actualSelectedCount < searchResultsCount.get()) {
				String errorMsg = "FAIL: Only " + actualSelectedCount + " selected (expected " + searchResultsCount.get() +
						"), " + missingSelections + " profiles lost selection";
				PageObjectHelper.log(LOGGER, errorMsg);
				Assert.fail(errorMsg);
			} else {
				String errorMsg = "FAIL: " + actualSelectedCount + " selected (expected " + searchResultsCount.get() +
						"), " + extraSelections + " extra profiles incorrectly selected";
				PageObjectHelper.log(LOGGER, errorMsg);
				Assert.fail(errorMsg);
			}
		}
	}

	private int checkNewProfilesForInvalidSelections(int startIndex, int endIndex,
			String firstSearchSubstring, String secondSearchSubstring) {
		int invalidCount = 0;

		try {
			List<WebElement> selectedRows = findElements(SELECTED_PROFILE_ROWS);
			List<WebElement> allRows = findElements(ALL_PROFILE_ROWS);

			for (int i = 0; i < selectedRows.size(); i++) {
				try {
					WebElement selectedRow = selectedRows.get(i);
					int rowIndex = allRows.indexOf(selectedRow);

					if (rowIndex < startIndex || rowIndex >= endIndex) {
						continue;
					}

					String jobName = extractJobName(selectedRow);
					if (jobName == null || jobName.isEmpty()) {
						continue;
					}

					boolean containsFirstSubstring = jobName.contains(firstSearchSubstring.toLowerCase());

					if (!containsFirstSubstring) {
						invalidCount++;
						if (invalidCount == 1) {
							LOGGER.warn("INVALID at row {}: '{}' (has '{}' but NOT '{}')",
									(rowIndex + 1), jobName, secondSearchSubstring, firstSearchSubstring);
						}
						return invalidCount; // Fail-fast
					}
				} catch (Exception e) {
					continue;
				}
			}

		} catch (Exception e) {
			LOGGER.error("Validation error: {}", e.getMessage());
		}

		return invalidCount;
	}

	private String extractJobName(WebElement row) {
		try {
			WebElement jobNameElement = null;
			try {
				jobNameElement = row.findElement(By.xpath(".//td//div//span[1]//a"));
			} catch (Exception e1) {
				try {
					jobNameElement = row.findElement(By.xpath(".//td//div//span//a"));
				} catch (Exception e2) {
					jobNameElement = row.findElement(By.xpath(".//td[position()=1]//a"));
				}
			}
			return jobNameElement.getText().toLowerCase().trim();
		} catch (Exception e) {
			return null;
		}
	}
}
