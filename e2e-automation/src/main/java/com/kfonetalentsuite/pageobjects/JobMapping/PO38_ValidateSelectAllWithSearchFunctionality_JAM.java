package com.kfonetalentsuite.pageobjects.JobMapping;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.ScreenshotHandler;
import com.kfonetalentsuite.utils.JobMapping.PageObjectHelper;

/**
 * Page Object for validating Select All functionality with Search in Job Mapping (JAM) screen.
 * Verifies that "Select All" only selects the searched results, not all profiles.
 * 
 * Enhanced to extend BasePageObject for consistency and code reuse.
 */
public class PO38_ValidateSelectAllWithSearchFunctionality_JAM extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO38_ValidateSelectAllWithSearchFunctionality_JAM.class);

	public PO38_ValidateSelectAllWithSearchFunctionality_JAM() {
		super();
	}

	// ==================== LOCATORS ====================
	private static final By SHOWING_JOB_RESULTS_COUNT = By.xpath("//div[contains(@id,'results-toggle')]//*[contains(text(),'Showing')]");
	private static final By SEARCH_BAR = By.xpath("//input[@id='search-job-title-input-search-input']");
	private static final By ALL_PROFILE_ROWS = By.xpath("//div[@id='org-job-container']//tbody//tr[.//td[1]//input[@type='checkbox']]");
	private static final By SELECTED_PROFILE_ROWS = By.xpath("//div[@id='org-job-container']//tbody//tr[.//td[1]//input[@type='checkbox' and @checked]]");

	// ==================== THREAD-SAFE STATE ====================
	public static ThreadLocal<Integer> searchResultsCount = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<String> alternativeSearchSubstring = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<Integer> totalSecondSearchResults = ThreadLocal.withInitial(() -> 0);

	/**
	 * Validates that "Select All" only selects the searched results, not all profiles.
	 * Scrolls to load all profiles and validates selection count.
	 */
	public void verify_only_searched_profiles_are_selected_after_clearing_search_bar_in_JAM_screen() {
		int totalProfilesVisible = 0;
		int previousTotalProfilesVisible = 0;
		int actualSelectedCount = 0;
		int scrollAttempts = 0;
		int maxScrollAttempts = 50;
		int stableCountAttempts = 0;
		int requiredStableAttempts = 3;
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

			// Parse total profile count from "Showing X of Y results" to adjust max scrolls
			try {
				js.executeScript("window.scrollTo(0, 0);");
				safeSleep(500);
				
				PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
				PerformanceUtils.waitForPageReady(driver, 3);
				
				WebElement resultsElement = wait.until(ExpectedConditions.visibilityOfElementLocated(SHOWING_JOB_RESULTS_COUNT));
				String resultsCountText = resultsElement.getText().trim();
				
				if (resultsCountText.contains("of")) {
					String[] parts = resultsCountText.split("\\s+");
					for (int i = 0; i < parts.length; i++) {
						if (parts[i].equals("of") && i + 1 < parts.length) {
							String countStr = parts[i + 1].replaceAll("[^0-9]", "");
							if (!countStr.isEmpty()) {
								expectedTotalProfiles = Integer.parseInt(countStr);
								int estimatedScrollsNeeded = (expectedTotalProfiles / 10) + 10;
								maxScrollAttempts = Math.max(50, estimatedScrollsNeeded);
								LOGGER.debug("Expected total profiles: {}, adjusted max scrolls to: {}", 
										expectedTotalProfiles, maxScrollAttempts);
							}
							break;
						}
					}
				}
			} catch (Exception e) {
				LOGGER.warn("Could not parse total profile count: {}", e.getMessage());
			}

			// Scroll to load all search results
			LOGGER.debug("Loading search results by scrolling...");

			while (scrollAttempts < maxScrollAttempts) {
				scrollToBottom();
				scrollAttempts++;
				safeSleep(3000);

				PerformanceUtils.waitForSpinnersToDisappear(driver, 5);
				PerformanceUtils.waitForPageReady(driver, 2);
				safeSleep(1000);

				totalProfilesVisible = findElements(ALL_PROFILE_ROWS).size();

				if (totalProfilesVisible == previousTotalProfilesVisible) {
					stableCountAttempts++;

					if (stableCountAttempts >= requiredStableAttempts) {
						LOGGER.debug("No new profiles loaded after {} consecutive attempts. Final total: {}",
								requiredStableAttempts, totalProfilesVisible);
						break;
					}

					if (stableCountAttempts == 2) {
						js.executeScript(
								"window.scrollTo(0, Math.max(document.body.scrollHeight, document.documentElement.scrollHeight));");
						safeSleep(2000);
						PerformanceUtils.waitForSpinnersToDisappear(driver, 5);
					}
				} else {
					stableCountAttempts = 0;
				}

				previousTotalProfilesVisible = totalProfilesVisible;
			}

			// Count selected profiles using JavaScript for performance
			int currentSelectedCount = getSelectedProfileCount(js);
			actualSelectedCount = currentSelectedCount;
			maxScrollLimitReached = scrollAttempts >= maxScrollAttempts;

			if (maxScrollLimitReached) {
				LOGGER.warn("Reached max scroll attempts ({})", maxScrollAttempts);
			}

			// After loading all profiles, recount if needed
			if (actualSelectedCount == 0) {
				actualSelectedCount = getSelectedProfileCount(js);
			}
			int notSelectedProfiles = totalProfilesVisible - actualSelectedCount;

			// Calculate missing/extra selections
			int missingSelections = Math.max(0, searchResultsCount.get() - actualSelectedCount);
			int extraSelections = Math.max(0, actualSelectedCount - searchResultsCount.get());

			// Structured summary logging
			LOGGER.info("========================================");
			LOGGER.info("VALIDATION SUMMARY (After Clearing Search)");
			LOGGER.info("========================================");
			if (maxScrollLimitReached && expectedTotalProfiles > 0 && totalProfilesVisible < expectedTotalProfiles) {
				LOGGER.warn("PARTIAL VALIDATION (Max scroll limit reached)");
				LOGGER.info("Expected Total Profiles: {}", expectedTotalProfiles);
				LOGGER.info("Profiles Actually Loaded: {} ({:.1f}%)", totalProfilesVisible,
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

			// Validate selection counts
			validateSelectionCounts(maxScrollLimitReached, expectedTotalProfiles, totalProfilesVisible,
					actualSelectedCount, missingSelections, extraSelections);

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot(
					"verify_only_searched_profiles_are_selected_after_clearing_search_bar_in_JAM_screen", e);
			PageObjectHelper.handleError(LOGGER, "verify_only_searched_profiles_are_selected", "Error verifying selection", e);
			throw new AssertionError("Error verifying only searched profiles are selected", e);
		}
	}

	/**
	 * Captures the baseline of selected profiles after Select All in search results.
	 */
	public void user_is_in_job_mapping_page_with_selected_search_results() {
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

			JavascriptExecutor js = (JavascriptExecutor) driver;
			int selectedCount = getSelectedProfileCount(js);
			int totalVisibleProfiles = getTotalProfileCount(js);
			int disabledProfiles = countDisabledCheckboxes("tbody tr");
			int unselectedProfiles = totalVisibleProfiles - selectedCount - disabledProfiles;
			
			searchResultsCount.set(selectedCount);

			LOGGER.info("=== BASELINE COUNTS ===");
			LOGGER.info("Total Profiles: {}", totalVisibleProfiles);
			LOGGER.info("Selected: {}", selectedCount);
			LOGGER.info("Unselected (enabled but not selected): {}", unselectedProfiles);
			LOGGER.info("Disabled (cannot be selected): {}", disabledProfiles);

		} catch (Exception e) {
			LOGGER.warn("Error capturing selected profiles count: {}", e.getMessage());
			searchResultsCount.set(0);
		}
	}

	/**
	 * Enters a different job name substring in search bar for alternative validation.
	 */
	public void enter_different_job_name_substring_in_search_bar_for_alternative_validation() {
		boolean foundResults = false;
		String firstSearchSubstring = PO04_VerifyJobMappingPageComponents.jobnamesubstring.get();

		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

			PageObjectHelper.log(LOGGER, "Alternative validation: Searching different substring...");

			for (String substring : PO04_VerifyJobMappingPageComponents.SEARCH_SUBSTRING_OPTIONS) {
				if (substring.equalsIgnoreCase(firstSearchSubstring)) {
					continue;
				}

				try {
					// Clear and enter new search
					WebElement searchBarElement = wait.until(ExpectedConditions.elementToBeClickable(SEARCH_BAR));
					searchBarElement.click();
					searchBarElement.sendKeys(Keys.CONTROL + "a");
					searchBarElement.sendKeys(Keys.DELETE);

					PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
					PerformanceUtils.waitForPageReady(driver, 1);

					searchBarElement = wait.until(ExpectedConditions.visibilityOfElementLocated(SEARCH_BAR));
					searchBarElement.clear();
					searchBarElement.sendKeys(substring);
					searchBarElement.sendKeys(Keys.ENTER);
					
					PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
					PerformanceUtils.waitForPageReady(driver, 5);
					safeSleep(1000);

					String resultsCountText = getElementText(SHOWING_JOB_RESULTS_COUNT);

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
			PageObjectHelper.handleError(LOGGER, "enter_different_job_name_substring", "Error applying different search", e);
			ScreenshotHandler.captureFailureScreenshot("enter_different_job_name_substring", e);
			Assert.fail("Failed to enter different job name substring");
		}
	}

	/**
	 * Scrolls down to load all second search results.
	 */
	public void scroll_down_to_load_all_second_search_results() throws InterruptedException {
		String secondSearchSubstring = alternativeSearchSubstring.get();
		JavascriptExecutor js = (JavascriptExecutor) driver;

		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

			String resultsCountText = getElementText(SHOWING_JOB_RESULTS_COUNT);

			if (resultsCountText.matches(".*Showing\\s+0\\s+of.*")) {
				PageObjectHelper.log(LOGGER, "Second search returned 0 results - skipping");
				totalSecondSearchResults.set(0);
				return;
			}

			int expectedTotal = parseProfileCountFromText(resultsCountText);
			PageObjectHelper.log(LOGGER, "Second search '" + secondSearchSubstring + "': " + expectedTotal + " profiles total");

			int previousProfileCount = findElements(ALL_PROFILE_ROWS).size();
			int currentProfileCount = previousProfileCount;
			int scrollAttempts = 0;
			int maxScrollAttempts = 50;
			int noChangeCount = 0;

			while (currentProfileCount < expectedTotal && scrollAttempts < maxScrollAttempts && noChangeCount < 3) {
				scrollAttempts++;
				scrollToBottom();
				safeSleep(3000);
				PerformanceUtils.waitForSpinnersToDisappear(driver, 5);
				PerformanceUtils.waitForPageReady(driver, 2);
				safeSleep(1000);
				
				currentProfileCount = findElements(ALL_PROFILE_ROWS).size();
				
				if (currentProfileCount == previousProfileCount) {
					noChangeCount++;
				} else {
					noChangeCount = 0;
					previousProfileCount = currentProfileCount;
				}
			}

			totalSecondSearchResults.set(currentProfileCount);
			PageObjectHelper.log(LOGGER, "Loaded " + currentProfileCount + " of " + expectedTotal + " second search profiles");

			js.executeScript("window.scrollTo(0, 0);");
			safeSleep(300);

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("scroll_down_to_load_all_second_search_results", e);
			PageObjectHelper.handleError(LOGGER, "scroll_down_to_load_all_second_search_results", "Error loading second search results", e);
			Assert.fail("Error loading second search results: " + e.getMessage());
		}
	}

	/**
	 * Verifies that all loaded profiles from the second search are NOT selected.
	 */
	public void verify_all_loaded_profiles_in_second_search_are_not_selected() {
		String firstSearchSubstring = PO04_VerifyJobMappingPageComponents.jobnamesubstring.get();
		String secondSearchSubstring = alternativeSearchSubstring.get();
		JavascriptExecutor js = (JavascriptExecutor) driver;
		
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 5);
			
			List<WebElement> allRows = findElements(ALL_PROFILE_ROWS);
			int totalLoaded = allRows.size();
			int selectedCount = getSelectedProfileCount(js);
			
			LOGGER.debug("Second search validation: {} total loaded, {} selected", totalLoaded, selectedCount);
			
			if (selectedCount > 0) {
				int invalidCount = checkNewProfilesForInvalidSelections(0, totalLoaded, firstSearchSubstring, secondSearchSubstring);
				
				if (invalidCount > 0) {
					String errorMsg = "FAIL: Found " + invalidCount + " invalid selections in second search results. " +
							"These profiles match '" + secondSearchSubstring + "' but are selected (should NOT be selected).";
					PageObjectHelper.log(LOGGER, errorMsg);
					Assert.fail(errorMsg);
					return;
				}
			}
			
			PageObjectHelper.log(LOGGER, "Validation PASSED: " + totalLoaded + " profiles checked, " + 
					selectedCount + " selected (all valid from first search)");
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_all_loaded_profiles_in_second_search_are_not_selected", e);
			PageObjectHelper.handleError(LOGGER, "verify_all_loaded_profiles_in_second_search", "Error verifying second search profiles", e);
			Assert.fail("Error verifying second search profiles: " + e.getMessage());
		}
	}

	// ==================== HELPER METHODS ====================
	// Note: Common helpers inherited from BasePageObject:
	// - parseProfileCountFromText(), countSelectedCheckboxes(), countTotalCheckboxes()

	private int getSelectedProfileCount(JavascriptExecutor js) {
		int count = countSelectedCheckboxes("#org-job-container tbody tr");
		return count > 0 ? count : findElements(SELECTED_PROFILE_ROWS).size();
	}

	private int getTotalProfileCount(JavascriptExecutor js) {
		int count = countTotalCheckboxes("#org-job-container tbody tr");
		return count > 0 ? count : findElements(ALL_PROFILE_ROWS).size();
	}

	private void applyFallbackSearch(String firstSearchSubstring) {
		String selectedSubstring = "";
		for (String substring : PO04_VerifyJobMappingPageComponents.SEARCH_SUBSTRING_OPTIONS) {
			if (!substring.equalsIgnoreCase(firstSearchSubstring)) {
				selectedSubstring = substring;
				alternativeSearchSubstring.set(substring);
				break;
			}
		}
		
		PageObjectHelper.log(LOGGER, "Using fallback: '" + selectedSubstring + "' (no results found)");
		
		try {
			WebElement searchBarElement = wait.until(ExpectedConditions.elementToBeClickable(SEARCH_BAR));
			searchBarElement.click();
			searchBarElement.sendKeys(Keys.CONTROL + "a");
			searchBarElement.sendKeys(Keys.DELETE);
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			
			searchBarElement = wait.until(ExpectedConditions.visibilityOfElementLocated(SEARCH_BAR));
			searchBarElement.sendKeys(selectedSubstring);
			searchBarElement.sendKeys(Keys.ENTER);
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 5);
			safeSleep(1000);
		} catch (Exception applyEx) {
			LOGGER.error("Failed to apply fallback search: {}", applyEx.getMessage());
		}
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

					String jobName = null;
					try {
						WebElement jobNameElement = selectedRow.findElement(By.xpath(".//td[2]//div"));
						jobName = jobNameElement.getText().toLowerCase().trim();
					} catch (Exception e) {
						try {
							WebElement jobNameElement = selectedRow.findElement(By.xpath(".//td[position()=2]//div"));
							jobName = jobNameElement.getText().toLowerCase().trim();
						} catch (Exception e2) {
							continue;
						}
					}

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
}
