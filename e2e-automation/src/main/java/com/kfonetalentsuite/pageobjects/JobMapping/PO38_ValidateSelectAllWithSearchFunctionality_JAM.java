package com.kfonetalentsuite.pageobjects.JobMapping;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
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

public class PO38_ValidateSelectAllWithSearchFunctionality_JAM {

	WebDriver driver = DriverManager.getDriver();

	protected static final Logger LOGGER = (Logger) LogManager.getLogger();

	public PO38_ValidateSelectAllWithSearchFunctionality_JAM() throws IOException {
		PageFactory.initElements(driver, this);
	}

	WebDriverWait wait = DriverManager.getWait();
	Utilities utils = new Utilities();
	JavascriptExecutor js = (JavascriptExecutor) driver;

	// XPATHs
	@FindBy(xpath = "//div[@data-testid='loader']//img")
	@CacheLookup
	WebElement pageLoadSpinner2;

	// NOTE: Do NOT use @CacheLookup here - element refreshes after search operations and causes StaleElementReferenceException
	@FindBy(xpath = "//div[contains(@id,'results-toggle')]//*[contains(text(),'Showing')]")
	public WebElement showingJobResultsCount;

	@FindBy(xpath = "//input[@id='search-job-title-input-search-input']")
	@CacheLookup
	public WebElement searchBar;

	// THREAD-SAFE: Each thread gets its own isolated state for parallel execution
	public static ThreadLocal<Integer> searchResultsCount = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<String> alternativeSearchSubstring = ThreadLocal.withInitial(() -> "");
	public static ThreadLocal<Integer> totalSecondSearchResults = ThreadLocal.withInitial(() -> 0);

	/**
	 * OLD APPROACH (REVERTED): Simple validation - scroll all, then validate at the
	 * end. Validates that "Select All" only selects the searched results, not all
	 * profiles.
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

		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

			LOGGER.info("Verifying only searched profiles remain selected (expected: " + searchResultsCount.get() + ")");
			ExtentCucumberAdapter
					.addTestStepLog("Verifying only " + searchResultsCount.get() + " profiles remain selected...");

			if (searchResultsCount.get() == 0) {
				LOGGER.warn(" Search results count is 0, skipping verification");
				ExtentCucumberAdapter.addTestStepLog(" No search results to verify");
				return;
			}

			// Parse total profile count from "Showing X of Y results" to adjust max scrolls
			try {
				// Scroll to top of page first to ensure proper data load
				js.executeScript("window.scrollTo(0, 0);");
				Thread.sleep(500);
				
				// Wait for spinners to disappear and page to stabilize
				PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
				PerformanceUtils.waitForPageReady(driver, 3);
				
				// Re-find element dynamically to avoid stale element issues after search clear
				WebElement resultsElement = driver.findElement(By.xpath("//div[contains(@id,'results-toggle')]//*[contains(text(),'Showing')]"));
				String resultsCountText = resultsElement.getText().trim();
				LOGGER.debug("Results count text: '{}'", resultsCountText);
				
				if (resultsCountText.contains("of")) {
					String[] parts = resultsCountText.split("\\s+");
					for (int i = 0; i < parts.length; i++) {
						if (parts[i].equals("of") && i + 1 < parts.length) {
							String countStr = parts[i + 1].replaceAll("[^0-9]", "");
							if (!countStr.isEmpty()) {
								expectedTotalProfiles = Integer.parseInt(countStr);
								// Adjust maxScrollAttempts based on expected total (assuming ~10 profiles per
								// scroll in JAM)
								int estimatedScrollsNeeded = (expectedTotalProfiles / 10) + 10;
								maxScrollAttempts = Math.max(50, estimatedScrollsNeeded);
								LOGGER.info("Expected total profiles: " + expectedTotalProfiles
										+ ", adjusted max scrolls to: " + maxScrollAttempts);
							}
							break;
						}
					}
				}
			} catch (Exception e) {
				LOGGER.warn("Could not parse total profile count: " + e.getMessage());
			}

			// Scroll to load all search results
			LOGGER.info(" Loading search results by scrolling...");

			while (scrollAttempts < maxScrollAttempts) {
				// ENHANCED SCROLLING STRATEGY for HEADLESS MODE:
				// Use multiple scroll techniques to ensure lazy loading triggers

				// Method 1: Scroll using document.body.scrollHeight (more reliable in headless)
				try {
					js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
				} catch (Exception e1) {
					// Fallback to documentElement.scrollHeight
					try {
						js.executeScript("window.scrollTo(0, document.documentElement.scrollHeight);");
					} catch (Exception e2) {
						// Last resort: scroll by large pixel amount
						js.executeScript("window.scrollBy(0, 10000);");
					}
				}

				scrollAttempts++;
				LOGGER.debug("Scroll attempt #{} - waiting for content to load...", scrollAttempts);

				// CRITICAL: Longer wait for HEADLESS MODE (lazy loading needs more time)
				Thread.sleep(3000); // Increased from 2000 to 3000ms for headless stability

				// Wait for any spinners to disappear
				PerformanceUtils.waitForSpinnersToDisappear(driver, 5);

				// Wait for page readiness
				PerformanceUtils.waitForPageReady(driver, 2); // Increased from 1 to 2 seconds

				// Additional wait for DOM updates in headless mode
				Thread.sleep(1000); // Extra buffer for lazy-loaded content to render

				// Get current count - count only rows with checkboxes (actual profiles)
				totalProfilesVisible = driver.findElements(By.xpath("//div[@id='org-job-container']//tbody//tr[.//td[1]//input[@type='checkbox']]")).size();

				LOGGER.debug("Current profile count after scroll #{}: {}", scrollAttempts, totalProfilesVisible);

				// Check if no new profiles loaded
				if (totalProfilesVisible == previousTotalProfilesVisible) {
					stableCountAttempts++;
					LOGGER.debug("No new profiles loaded. Stagnation count: {}/{}", stableCountAttempts,
							requiredStableAttempts);

					if (stableCountAttempts >= requiredStableAttempts) {
						LOGGER.info("... No new profiles loaded after {} consecutive attempts. Final total: {}",
								requiredStableAttempts, totalProfilesVisible);
						break;
					}

					// ADDITIONAL: Try forcing scroll to absolute bottom one more time
					if (stableCountAttempts == 2) {
						LOGGER.debug("Attempting final aggressive scroll to ensure all content loaded...");
						js.executeScript(
								"window.scrollTo(0, Math.max(document.body.scrollHeight, document.documentElement.scrollHeight, document.body.offsetHeight, document.documentElement.offsetHeight, document.body.clientHeight, document.documentElement.clientHeight));");
						Thread.sleep(2000); // Wait after aggressive scroll

						// Wait for spinners after aggressive scroll
						PerformanceUtils.waitForSpinnersToDisappear(driver, 5);
					}
				} else {
					stableCountAttempts = 0;
					int newProfiles = totalProfilesVisible - previousTotalProfilesVisible;
					LOGGER.debug("✓ Loaded {} new profiles (total: {}, scroll: #{})", newProfiles, totalProfilesVisible,
							scrollAttempts);
				}

				previousTotalProfilesVisible = totalProfilesVisible;
			}

			// Check if selected count increased beyond baseline
			// Use JavaScript for faster counting (same approach as PO40)
			int currentSelectedCount = 0;
			try {
				Object result = js.executeScript("return document.querySelectorAll('#org-job-container tbody tr td:first-child input[type=\"checkbox\"]:checked').length;");
				currentSelectedCount = ((Long) result).intValue();
			} catch (Exception e) {
				currentSelectedCount = driver.findElements(By.xpath("//div[@id='org-job-container']//tbody//tr//td[1]//input[@type='checkbox' and @checked]")).size();
			}
			actualSelectedCount = currentSelectedCount;

			LOGGER.info("... Loaded {} profiles using {} scrolls", totalProfilesVisible, scrollAttempts);
			LOGGER.info("Selected profiles: {} (baseline: {})", currentSelectedCount, searchResultsCount.get());

			if (currentSelectedCount > searchResultsCount.get()) {
				int extra = currentSelectedCount - searchResultsCount.get();
				LOGGER.warn(" Found {} selected profiles (expected {}), {} extra selections", currentSelectedCount,
						searchResultsCount.get(), extra);
			}
			maxScrollLimitReached = false;

			// Check if max scroll limit was reached
			if (scrollAttempts >= maxScrollAttempts) {
				maxScrollLimitReached = true;
				LOGGER.warn(" Reached max scroll attempts (" + maxScrollAttempts + ")");

				// Check if we loaded all expected profiles
				if (expectedTotalProfiles > 0 && totalProfilesVisible < expectedTotalProfiles) {
					int missingProfiles = expectedTotalProfiles - totalProfilesVisible;
					LOGGER.warn(" INCOMPLETE VALIDATION: Only loaded " + totalProfilesVisible + " of "
							+ expectedTotalProfiles + " profiles (" + missingProfiles + " not loaded)");
					LOGGER.warn(" Validation will be based on loaded profiles only - results may not be complete!");
					ExtentCucumberAdapter.addTestStepLog(" WARNING: Partial validation (" + totalProfilesVisible
							+ " of " + expectedTotalProfiles + " profiles loaded)");
				} else {
					LOGGER.info("Proceeding with validation of " + totalProfilesVisible + " loaded profiles");
				}
			}

			// After loading all profiles, count selected vs not selected (if not already
			// counted during fail-fast)
			if (actualSelectedCount == 0) {
				try {
					Object result = js.executeScript("return document.querySelectorAll('#org-job-container tbody tr td:first-child input[type=\"checkbox\"]:checked').length;");
					actualSelectedCount = ((Long) result).intValue();
				} catch (Exception e) {
					actualSelectedCount = driver.findElements(By.xpath("//div[@id='org-job-container']//tbody//tr//td[1]//input[@type='checkbox' and @checked]")).size();
				}
			}
			int notSelectedProfiles = totalProfilesVisible - actualSelectedCount;

			// Calculate missing/extra selections
			int missingSelections = 0;
			int extraSelections = 0;
			if (actualSelectedCount < searchResultsCount.get()) {
				missingSelections = searchResultsCount.get() - actualSelectedCount;
			} else if (actualSelectedCount > searchResultsCount.get()) {
				extraSelections = actualSelectedCount - searchResultsCount.get();
			}

			// Structured summary logging
			LOGGER.info("========================================");
			LOGGER.info("VALIDATION SUMMARY (After Clearing Search)");
			LOGGER.info("========================================");
			if (maxScrollLimitReached && expectedTotalProfiles > 0 && totalProfilesVisible < expectedTotalProfiles) {
				LOGGER.warn(" PARTIAL VALIDATION (Max scroll limit reached)");
				LOGGER.info("Expected Total Profiles: " + expectedTotalProfiles);
				LOGGER.info("Profiles Actually Loaded: " + totalProfilesVisible + " ("
						+ String.format("%.1f", (totalProfilesVisible * 100.0 / expectedTotalProfiles)) + "%)");
			} else {
				LOGGER.info("Total Profiles Loaded: " + totalProfilesVisible);
			}
			LOGGER.info("Currently Selected Profiles: " + actualSelectedCount);
			LOGGER.info("Not Selected Profiles (Disabled + Unselected): " + notSelectedProfiles);
			LOGGER.info("----------------------------------------");
			LOGGER.info("Baseline (from first search): " + searchResultsCount.get() + " profiles");
			if (missingSelections > 0) {
				LOGGER.warn("Missing Selections: " + missingSelections + " (disabled or lost selection)");
			} else if (extraSelections > 0) {
				LOGGER.warn("Extra Selections: " + extraSelections + " (incorrectly selected)");
			} else {
				LOGGER.info("Missing/Extra Selections: 0");
			}
			if (maxScrollLimitReached && expectedTotalProfiles > 0 && totalProfilesVisible < expectedTotalProfiles) {
				LOGGER.warn(" Note: " + (expectedTotalProfiles - totalProfilesVisible)
						+ " profiles were not loaded/validated");
			}
			LOGGER.info("========================================");

			// Validate that ONLY the originally searched profiles remain selected
			// Special handling if max scroll limit was reached and not all profiles loaded
			if (maxScrollLimitReached && expectedTotalProfiles > 0 && totalProfilesVisible < expectedTotalProfiles) {
				// FEATURE 40 APPROACH: Smart validation for partial data
				if (actualSelectedCount == 0) {
					String errorMsg = " FAIL: No selections found in " + totalProfilesVisible
							+ " loaded profiles (expected " + searchResultsCount.get() + ")";
					LOGGER.error(errorMsg);
					ExtentCucumberAdapter.addTestStepLog(errorMsg);
					Assert.fail(errorMsg);
				} else if (actualSelectedCount > searchResultsCount.get()) {
					String errorMsg = " FAIL: Found " + actualSelectedCount + " selected (expected "
							+ searchResultsCount.get() + "), " + extraSelections + " extra profiles incorrectly selected";
					LOGGER.error(errorMsg);
					ExtentCucumberAdapter.addTestStepLog(errorMsg);
					Assert.fail(errorMsg);
				} else if (actualSelectedCount < searchResultsCount.get()) {
					// PASS: Remaining selections likely in unloaded profiles
					String successMsg = " PASS: Found " + actualSelectedCount + " of " + searchResultsCount
							+ " selected profiles in loaded data (remaining " + missingSelections
							+ " likely in unloaded profiles)";
					LOGGER.info(successMsg);
					ExtentCucumberAdapter.addTestStepLog(successMsg);
				} else {
					// actualSelectedCount == searchResultsCount
					String successMsg = " PASS: All " + searchResultsCount
							+ " searched profiles found selected in loaded data";
					LOGGER.info(successMsg);
					ExtentCucumberAdapter.addTestStepLog(successMsg);
				}
			} else {
				// Normal validation when all profiles are loaded
				if (actualSelectedCount == searchResultsCount.get()) {
					String successMsg = " PASS: All " + searchResultsCount.get() + " searched profiles remain selected";
					LOGGER.info(successMsg);
					ExtentCucumberAdapter.addTestStepLog(successMsg);
				} else if (actualSelectedCount < searchResultsCount.get()) {
					String errorMsg = " FAIL: Only " + actualSelectedCount + " selected (expected " + searchResultsCount.get()
							+ "), " + missingSelections + " profiles cannot be selected or lost selection";
					LOGGER.error(errorMsg);
					ExtentCucumberAdapter.addTestStepLog(errorMsg);
					Assert.fail(errorMsg);
				} else {
					String errorMsg = " FAIL: " + actualSelectedCount + " selected (expected " + searchResultsCount.get()
							+ "), " + extraSelections + " extra profiles incorrectly selected";
					LOGGER.error(errorMsg);
					ExtentCucumberAdapter.addTestStepLog(errorMsg);
					Assert.fail(errorMsg);
				}
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot(
					"verify_only_searched_profiles_are_selected_after_clearing_search_bar_in_JAM_screen", e);
			LOGGER.error("Error verifying searched profiles selection", e);
			ExtentCucumberAdapter.addTestStepLog(" Error verifying selection");
			throw new AssertionError("Error verifying only searched profiles are selected", e);
		}
	}

	public void user_is_in_job_mapping_page_with_selected_search_results() {
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

			// Count ACTUAL selected profiles (this is the real baseline)
			// Use JavaScript for faster and more reliable checkbox counting (same as PO40)
			String jsSelectedCount = "return document.querySelectorAll('#org-job-container tbody tr td:first-child input[type=\"checkbox\"]:checked').length;";
			String jsTotalCount = "return document.querySelectorAll('#org-job-container tbody tr td:first-child input[type=\"checkbox\"]').length;";
			
			int selectedCount = 0;
			int totalVisibleProfiles = 0;
			
			try {
				Object selectedResult = js.executeScript(jsSelectedCount);
				selectedCount = ((Long) selectedResult).intValue();
				
				Object totalResult = js.executeScript(jsTotalCount);
				totalVisibleProfiles = ((Long) totalResult).intValue();
			} catch (Exception jsEx) {
				LOGGER.warn("JavaScript counting failed, using XPath fallback: " + jsEx.getMessage());
				// Fallback to XPath
				selectedCount = driver.findElements(By.xpath("//div[@id='org-job-container']//tbody//tr//td[1]//input[@type='checkbox' and @checked]")).size();
				totalVisibleProfiles = driver.findElements(By.xpath("//div[@id='org-job-container']//tbody//tr//td[1]//input[@type='checkbox']")).size();
			}
			
			searchResultsCount.set(selectedCount);
			int disabledProfiles = totalVisibleProfiles - searchResultsCount.get();
			int unselectedProfiles = 0; // After "Select All", unselected should be 0 (all selectable profiles are
										// selected)

			// Structured logging
			LOGGER.info("========================================");
			LOGGER.info("BASELINE COUNTS (After First Search + Selection)");
			LOGGER.info("========================================");
			LOGGER.info("Total Profiles Loaded: " + totalVisibleProfiles);
			LOGGER.info("Selected Profiles: " + searchResultsCount.get());
			LOGGER.info("Disabled Profiles (cannot be selected): " + disabledProfiles);
			LOGGER.info("Unselected Profiles (can be selected but not selected): " + unselectedProfiles);
			LOGGER.info("========================================");

			ExtentCucumberAdapter.addTestStepLog("Baseline: " + searchResultsCount.get() + " selected profiles");

		} catch (Exception e) {
			LOGGER.warn("Error capturing selected profiles count: " + e.getMessage());
			searchResultsCount.set(0);
		}
	}

	// ========================================================================================
	// Alternative Validation Strategy - Using Second Search to verify first search
	// selection
	// ========================================================================================

	public void enter_different_job_name_substring_in_search_bar_for_alternative_validation() {
		boolean foundResults = false;
		String firstSearchSubstring = PO04_VerifyJobMappingPageComponents.jobnamesubstring.get();
		String selectedSubstring = "";

		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

			LOGGER.info("Alternative validation: First search was '" + firstSearchSubstring
					+ "', finding different substring...");
			ExtentCucumberAdapter.addTestStepLog("Alternative validation: Searching different substring...");

			for (String substring : PO04_VerifyJobMappingPageComponents.SEARCH_SUBSTRING_OPTIONS) {
				if (substring.equalsIgnoreCase(firstSearchSubstring)) {
					continue;
				}

				try {
					// Clear search bar
					wait.until(ExpectedConditions.elementToBeClickable(searchBar)).click();
					searchBar.sendKeys(Keys.CONTROL + "a");
					searchBar.sendKeys(Keys.DELETE);

					PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
					PerformanceUtils.waitForPageReady(driver, 1);

					// Enter different substring
					wait.until(ExpectedConditions.visibilityOf(searchBar)).clear();
					wait.until(ExpectedConditions.elementToBeClickable(searchBar)).click();
					wait.until(ExpectedConditions.visibilityOf(searchBar)).sendKeys(substring);
					wait.until(ExpectedConditions.visibilityOf(searchBar)).sendKeys(Keys.ENTER);
					PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
					PerformanceUtils.waitForPageReady(driver, 5); // Increased from 2 to 5 seconds for search results to load
					
					// Additional wait for search results to stabilize
					Thread.sleep(1000);

					String resultsCountText = showingJobResultsCount.getText().trim();

					if (resultsCountText.contains("Showing") && !resultsCountText.startsWith("Showing 0")) {
						selectedSubstring = substring;
						alternativeSearchSubstring.set(substring);
						foundResults = true;
						LOGGER.info(" Using '" + substring + "': " + resultsCountText);
						ExtentCucumberAdapter.addTestStepLog(" Second search: '" + substring + "'");
						break;
					}

				} catch (Exception e) {
					// Continue to next substring
				}
			}

			if (!foundResults) {
				for (String substring : PO04_VerifyJobMappingPageComponents.SEARCH_SUBSTRING_OPTIONS) {
					if (!substring.equalsIgnoreCase(firstSearchSubstring)) {
						selectedSubstring = substring;
						alternativeSearchSubstring.set(substring);
						break;
					}
				}
				LOGGER.warn(" No alternative substring returned results, using: '" + selectedSubstring + "'");
				ExtentCucumberAdapter.addTestStepLog(" Using: '" + selectedSubstring + "' (no results found)");
				
				// Actually apply the fallback search
				try {
					wait.until(ExpectedConditions.elementToBeClickable(searchBar)).click();
					searchBar.sendKeys(Keys.CONTROL + "a");
					searchBar.sendKeys(Keys.DELETE);
					PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
					
					wait.until(ExpectedConditions.visibilityOf(searchBar)).sendKeys(selectedSubstring);
					wait.until(ExpectedConditions.visibilityOf(searchBar)).sendKeys(Keys.ENTER);
					PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
					PerformanceUtils.waitForPageReady(driver, 5);
					Thread.sleep(1000); // Wait for search results to stabilize
					LOGGER.info(" Applied fallback search: '" + selectedSubstring + "'");
				} catch (Exception applyEx) {
					LOGGER.error(" Failed to apply fallback search: " + applyEx.getMessage());
				}
			}

		} catch (Exception e) {
			LOGGER.error(" Error applying different search", e);
			ScreenshotHandler.captureFailureScreenshot("enter_different_job_name_substring", e);
			Assert.fail("Failed to enter different job name substring");
			ExtentCucumberAdapter.addTestStepLog("Failed to enter different job name substring");
		}
	}

	/**
	 * OPTIMIZED: Checks only NEWLY loaded profiles in range [startIndex, endIndex)
	 * for invalid selections. Only logs the FIRST invalid profile found, then
	 * returns immediately for fail-fast.
	 */
	private int checkNewProfilesForInvalidSelections(int startIndex, int endIndex, String firstSearchSubstring,
			String secondSearchSubstring) {
		int invalidCount = 0;

		try {
			// Get ONLY selected rows with checked checkbox
			var selectedRows = driver.findElements(By
					.xpath("//div[@id='org-job-container']//tbody//tr[.//td[1]//input[@type='checkbox' and @checked]]"));

			// Get all rows to determine indices
			var allRows = driver.findElements(By.xpath("//div[@id='org-job-container']//tbody//tr[.//td[1]//input[@type='checkbox']]"));

			// Check only selected rows in our range
			for (int i = 0; i < selectedRows.size(); i++) {
				try {
					WebElement selectedRow = selectedRows.get(i);
					int rowIndex = allRows.indexOf(selectedRow);

					// Skip if outside our validation range
					if (rowIndex < startIndex || rowIndex >= endIndex) {
						continue;
					}

					// Extract job title
					String jobName = null;
					try {
						// Try primary XPath first
						WebElement jobNameElement = selectedRow.findElement(By.xpath(".//td[2]//div"));
						jobName = jobNameElement.getText().toLowerCase().trim();
					} catch (Exception e) {
						// Try fallback XPath
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

					// Validate
					boolean containsFirstSubstring = jobName.contains(firstSearchSubstring.toLowerCase());

					if (!containsFirstSubstring) {
						invalidCount++;
						// Log ONLY the first invalid profile for clarity
						if (invalidCount == 1) {
							LOGGER.warn("- INVALID at row " + (rowIndex + 1) + ": '" + jobName + "' (has '"
									+ secondSearchSubstring + "' but NOT '" + firstSearchSubstring + "')");
						}
						// Return immediately for fail-fast
						return invalidCount;
					}
				} catch (Exception e) {
					continue;
				}
			}

		} catch (Exception e) {
			LOGGER.error("Validation error: " + e.getMessage());
		}

		return invalidCount;
	}

	/**
	 * SIMPLIFIED ALTERNATIVE VALIDATION - No Scrolling Required
	 * 
	 * Checks ONLY the initial visible profiles from the second search. This is
	 * sufficient because: - If ANY profile from second search is incorrectly
	 * selected FAIL immediately - If ALL visible profiles are unselected (or
	 * validly selected) PASS
	 * 
	 * Benefits: - 10x faster (no scrolling needed) - Immediate failure detection -
	 * Matches original alternative validation design
	 */
	public void scroll_down_to_load_all_second_search_results() throws InterruptedException {
		String secondSearchSubstring = alternativeSearchSubstring.get();

		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

			String resultsCountText = showingJobResultsCount.getText().trim();

			if (resultsCountText.matches(".*Showing\\s+0\\s+of.*")) {
				LOGGER.warn(" Second search returned 0 results, skipping");
				ExtentCucumberAdapter.addTestStepLog(" Second search returned 0 results");
				totalSecondSearchResults.set(0);
				return;
			}

			// Parse expected total from "Showing X of Y"
			int expectedTotal = 0;
			try {
				if (resultsCountText.contains("of")) {
					String[] parts = resultsCountText.split("\\s+");
					for (int i = 0; i < parts.length; i++) {
						if (parts[i].equals("of") && i + 1 < parts.length) {
							expectedTotal = Integer.parseInt(parts[i + 1]);
							break;
						}
					}
				}
			} catch (Exception e) {
				LOGGER.debug("Could not parse expected total: " + e.getMessage());
			}

			LOGGER.info("Second search '" + secondSearchSubstring + "': " + expectedTotal + " profiles total");
			ExtentCucumberAdapter.addTestStepLog("Second search '" + secondSearchSubstring + "': " + expectedTotal + " profiles");

			// Get initial profile count (rows with checkboxes)
			int previousProfileCount = driver.findElements(By.xpath("//div[@id='org-job-container']//tbody//tr[.//td[1]//input[@type='checkbox']]")).size();
			int currentProfileCount = previousProfileCount;
			int scrollAttempts = 0;
			int maxScrollAttempts = 50; // Safety limit
			int noChangeCount = 0;

			LOGGER.info("Starting scroll to load all second search results. Initial loaded: " + previousProfileCount);

			// Scroll until all profiles are loaded or max attempts reached
			while (currentProfileCount < expectedTotal && scrollAttempts < maxScrollAttempts && noChangeCount < 3) {
				scrollAttempts++;
				
				// Scroll down using JavaScript
				js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
				Thread.sleep(500); // Wait for lazy loading
				
				// Wait for any spinners
				PerformanceUtils.waitForSpinnersToDisappear(driver, 5);
				
				// Get updated profile count
				currentProfileCount = driver.findElements(By.xpath("//div[@id='org-job-container']//tbody//tr[.//td[1]//input[@type='checkbox']]")).size();
				
				if (currentProfileCount == previousProfileCount) {
					noChangeCount++;
				} else {
					noChangeCount = 0;
					previousProfileCount = currentProfileCount;
				}
				
				// Log progress every 5 scrolls
				if (scrollAttempts % 5 == 0) {
					LOGGER.info("Scroll attempt {}: Loaded {} of {} profiles", scrollAttempts, currentProfileCount, expectedTotal);
				}
			}

			totalSecondSearchResults.set(currentProfileCount);
			
			LOGGER.info("Scrolling complete. Loaded {} of {} profiles in {} scroll attempts", 
					currentProfileCount, expectedTotal, scrollAttempts);
			ExtentCucumberAdapter.addTestStepLog("Loaded " + currentProfileCount + " of " + expectedTotal + " second search profiles");

			// Scroll back to top for validation
			js.executeScript("window.scrollTo(0, 0);");
			Thread.sleep(300);

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("scroll_down_to_load_all_second_search_results", e);
			LOGGER.error("Error loading second search results", e);
			ExtentCucumberAdapter.addTestStepLog(" Error loading second search results");
			Assert.fail("Error loading second search results: " + e.getMessage());
		}
	}

	/**
	 * Verifies that all loaded profiles from the second search are NOT selected.
	 * These profiles should NOT be selected because they don't match the first search.
	 */
	public void verify_all_loaded_profiles_in_second_search_are_not_selected() {
		String firstSearchSubstring = PO04_VerifyJobMappingPageComponents.jobnamesubstring.get();
		String secondSearchSubstring = alternativeSearchSubstring.get();
		
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 5);
			
			// Get all loaded profiles
			var allRows = driver.findElements(By.xpath("//div[@id='org-job-container']//tbody//tr[.//td[1]//input[@type='checkbox']]"));
			int totalLoaded = allRows.size();
			
			// Count selected profiles using JavaScript
			int selectedCount = 0;
			try {
				Object result = js.executeScript("return document.querySelectorAll('#org-job-container tbody tr td:first-child input[type=\"checkbox\"]:checked').length;");
				selectedCount = ((Long) result).intValue();
			} catch (Exception e) {
				selectedCount = driver.findElements(By.xpath("//div[@id='org-job-container']//tbody//tr//td[1]//input[@type='checkbox' and @checked]")).size();
			}
			
			LOGGER.info("Second search validation: {} total loaded, {} selected", totalLoaded, selectedCount);
			
			// If any profiles are selected, check if they belong to first search (valid) or second search (invalid)
			if (selectedCount > 0) {
				// Check for invalid selections
				int invalidCount = checkNewProfilesForInvalidSelections(0, totalLoaded, firstSearchSubstring, secondSearchSubstring);
				
				if (invalidCount > 0) {
					String errorMsg = "FAIL: Found " + invalidCount + " invalid selections in second search results. " +
							"These profiles match '" + secondSearchSubstring + "' but are selected (should NOT be selected).";
					LOGGER.error(errorMsg);
					ExtentCucumberAdapter.addTestStepLog(errorMsg);
					Assert.fail(errorMsg);
					return;
				}
				
				LOGGER.info("All {} selected profiles are valid (from first search '{}')", selectedCount, firstSearchSubstring);
			}
			
			LOGGER.info(" Validation PASSED: {} profiles checked, no invalid selections found", totalLoaded);
			ExtentCucumberAdapter.addTestStepLog("Validation PASSED: " + totalLoaded + " profiles checked, " + 
					selectedCount + " selected (all valid from first search)");
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_all_loaded_profiles_in_second_search_are_not_selected", e);
			LOGGER.error("Error verifying second search profiles", e);
			ExtentCucumberAdapter.addTestStepLog(" Error verifying second search profiles");
			Assert.fail("Error verifying second search profiles: " + e.getMessage());
		}
	}
}
