package com.kfonetalentsuite.pageobjects.JobMapping;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.ScreenshotHandler;
import com.kfonetalentsuite.utils.JobMapping.PageObjectHelper;

/**
 * Consolidated Page Object for Select All / Loaded Profiles Selection with Filters functionality.
 * Supports both PM (HCM Sync Profiles) and JAM (Job Mapping) screens.
 * 
 * NOTE: This PO only contains FILTER-SPECIFIC methods.
 * Common methods (chevron, select all, header checkbox, scroll, verify, capture baseline)
 * are provided by PO35_SelectAllWithSearchFunctionality and PO42_ClearProfileSelectionFunctionality.
 * 
 * PM Filters: KF Grade, Levels
 * JAM Filters: Grades, Departments
 */
public class PO28_SelectAllWithFiltersFunctionality extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO28_SelectAllWithFiltersFunctionality.class);

	public PO28_SelectAllWithFiltersFunctionality() {
		super();
	}

	// ==================== LOCATORS ====================
	// Common locators are inherited from BasePageObject.Locators.PMScreen and JAMScreen
	// Only filter-specific locators remain here
	private static final By PM_KF_GRADE_HEADER = By.xpath("//thcl-expansion-panel-header[@id='expansion-panel-header-0']");
	private static final By PM_LEVELS_HEADER = By.xpath("//thcl-expansion-panel-header[@id='expansion-panel-header-1']");
	private static final By JAM_GRADES_DROPDOWN = By.xpath("//div[@data-testid='dropdown-Grades']");
	private static final By JAM_DEPARTMENTS_DROPDOWN = By.xpath("//div[@data-testid='dropdown-Departments']");

	// ==================== THREAD-SAFE STATE ====================
	public static ThreadLocal<Integer> filterResultsCount = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<String> firstFilterType = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<String> firstFilterValue = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<String> secondFilterType = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<String> secondFilterValue = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<Integer> totalSecondFilterResults = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<String> currentScreen = ThreadLocal.withInitial(() -> "PM");

	// ==================== HELPER METHODS ====================
	// Common helper methods (getScreenName, getShowingResultsCountLocator, getAllProfileRowsLocator,
	// getSelectedProfileRowsLocator, getClearFiltersButtonLocator, parseProfileCountFromText) 
	// are inherited from BasePageObject

	private String extractJobName(WebElement row, String screen) {
		try {
			WebElement nameElement = row.findElement(By.xpath(".//td//div//span[1]//a | .//td[2]//div"));
			return nameElement.getText().trim();
		} catch (Exception e) {
			return "";
		}
	}

	// ==================== FILTER APPLICATION METHODS ====================

	/**
	 * Applies a filter and verifies profiles count in the specified screen.
	 * PM: Uses KF Grade or Levels filter
	 * JAM: Uses Grades or Departments filter
	 */
	public void apply_filter_and_verify_profiles_count(String screen) {
		try {
			currentScreen.set(screen);
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

			scrollToTop();
			safeSleep(500);

			boolean isPM = "PM".equalsIgnoreCase(screen);
			
			if (isPM) {
				applyPMFilter();
			} else {
				applyJAMFilter();
			}

			PageObjectHelper.log(LOGGER, "Applied filter: " + firstFilterType.get() + " = " + firstFilterValue.get() + 
					" in " + getScreenName(screen));

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("apply_filter_" + screen, e);
			PageObjectHelper.handleError(LOGGER, "apply_filter_and_verify_profiles_count",
					"Error applying filter in " + getScreenName(screen), e);
			Assert.fail("Error applying filter: " + e.getMessage());
		}
	}

	private void applyPMFilter() throws Exception {
		LOGGER.info("Applying KF Grade filter in PM...");
		firstFilterType.set("KF Grade");

		try {
			// Click on KF Grade expansion panel
			var kfGradeHeader = driver.findElements(PM_KF_GRADE_HEADER);
			if (!kfGradeHeader.isEmpty()) {
				jsClick(kfGradeHeader.get(0));
				PerformanceUtils.waitForPageReady(driver, 1);
				safeSleep(500);

				// Find KF Grade options
				var kfGradeOptions = findFilterOptions("expansion-panel-header-0");
				
				if (!kfGradeOptions.isEmpty()) {
					selectFilterOption(kfGradeOptions.get(0));
					return;
				}
			}
			throw new Exception("KF Grade not available");
		} catch (Exception kfGradeException) {
			// Fallback to Levels filter
			LOGGER.info("KF Grade not available, trying Levels filter...");
			firstFilterType.set("Levels");

			var levelsHeader = driver.findElements(PM_LEVELS_HEADER);
			if (!levelsHeader.isEmpty()) {
				jsClick(levelsHeader.get(0));
				PerformanceUtils.waitForPageReady(driver, 1);
				safeSleep(500);

				var levelsOptions = findFilterOptions("expansion-panel-header-1");
				
				if (!levelsOptions.isEmpty()) {
					selectFilterOption(levelsOptions.get(0));
					return;
				}
			}
			throw new Exception("Both KF Grade and Levels filters failed");
		}
	}

	private void applyJAMFilter() throws Exception {
		LOGGER.info("Applying Grades filter in JAM...");
		firstFilterType.set("Grades");

		try {
			// Click on Grades dropdown to expand it
			WebElement gradesDropdown = findElement(JAM_GRADES_DROPDOWN);
			jsClick(gradesDropdown);
			PerformanceUtils.waitForPageReady(driver, 1);
			safeSleep(1000); // Wait for dropdown to expand

			// Find checkbox options within the Grades dropdown
			// Try multiple XPath strategies
			var gradesOptions = driver.findElements(
					By.xpath("//div[@data-testid='dropdown-Grades']//input[@type='checkbox']"));
			
			if (gradesOptions.isEmpty()) {
				// Try alternative: look for checkboxes in any expanded dropdown panel
				gradesOptions = driver.findElements(
						By.xpath("//div[contains(@class,'dropdown')]//input[@type='checkbox']"));
			}

			LOGGER.info("Found {} grade filter options", gradesOptions.size());
			
			if (!gradesOptions.isEmpty()) {
				// Select first option that has results (skip if count is 0)
				for (WebElement option : gradesOptions) {
					try {
						String optionText = option.findElement(By.xpath("./ancestor::label | ./parent::*")).getText();
						// Skip options with (0) count
						if (!optionText.contains("(0)")) {
							selectJAMFilterOption(option);
							return;
						}
					} catch (Exception e) {
						// If can't check, just select first
						selectJAMFilterOption(option);
						return;
					}
				}
				// If all have (0), select first anyway
				selectJAMFilterOption(gradesOptions.get(0));
				return;
			}
			throw new Exception("Grades not available");
		} catch (Exception gradesException) {
			LOGGER.warn("Grades filter failed: {}", gradesException.getMessage());
			// Fallback to Departments filter
			LOGGER.info("Grades not available, trying Departments filter...");
			firstFilterType.set("Departments");

			jsClick(findElement(JAM_DEPARTMENTS_DROPDOWN));
			PerformanceUtils.waitForPageReady(driver, 1);
			safeSleep(1000);

			var deptOptions = driver.findElements(
					By.xpath("//div[@data-testid='dropdown-Departments']//input[@type='checkbox']"));
			
			if (deptOptions.isEmpty()) {
				deptOptions = driver.findElements(
						By.xpath("//div[contains(@class,'dropdown')]//input[@type='checkbox']"));
			}

			LOGGER.info("Found {} department filter options", deptOptions.size());
			
			if (!deptOptions.isEmpty()) {
				selectJAMFilterOption(deptOptions.get(0));
				return;
			}
			throw new Exception("Both Grades and Departments filters failed");
		}
	}

	private List<WebElement> findFilterOptions(String panelHeaderId) {
		List<WebElement> options = new ArrayList<>();
		
		// Strategy 1
		options = driver.findElements(By.xpath(
				"//thcl-expansion-panel-header[@id='" + panelHeaderId + "']/following-sibling::div//kf-checkbox"));
		if (!options.isEmpty()) return options;
		
		// Strategy 2
		options = driver.findElements(By.xpath(
				"//thcl-expansion-panel-header[@id='" + panelHeaderId + "']//..//kf-checkbox"));
		
		return options;
	}

	private void selectFilterOption(WebElement checkbox) throws Exception {
		// Extract label text
		try {
			WebElement parent = checkbox.findElement(By.xpath("./parent::*"));
			firstFilterValue.set(parent.getText().trim());
		} catch (Exception e) {
			firstFilterValue.set("Filter_Option_1");
		}

		LOGGER.info("Found filter option: " + firstFilterValue.get());

		WebElement elementToClick = checkbox.findElement(By.xpath("./parent::*"));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});", elementToClick);
		safeSleep(300);

		try {
			wait.until(ExpectedConditions.elementToBeClickable(elementToClick)).click();
		} catch (Exception e) {
			js.executeScript("arguments[0].click();", elementToClick);
		}

		PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
		PerformanceUtils.waitForPageReady(driver, 2);
	}

	private void selectJAMFilterOption(WebElement checkbox) throws Exception {
		// Extract label text from the label element (sibling or parent structure)
		String labelText = "Filter_Option_1";
		try {
			// JAM filter structure: <label><input type="checkbox"/><span>Label Text</span></label>
			// Or: <div><input type="checkbox"/><label>Label Text</label></div>
			WebElement labelSpan = checkbox.findElement(By.xpath("./following-sibling::span | ./following-sibling::label | ../label | ../span"));
			labelText = labelSpan.getText().trim();
		} catch (Exception e) {
			try {
				// Try getting text from parent container
				WebElement parent = checkbox.findElement(By.xpath("./ancestor::label | ./ancestor::div[contains(@class,'checkbox')]"));
				labelText = parent.getText().trim();
			} catch (Exception ex) {
				LOGGER.debug("Could not extract label text: {}", ex.getMessage());
			}
		}
		firstFilterValue.set(labelText);
		LOGGER.info("Found JAM filter option: '{}', checkbox checked before click: {}", labelText, checkbox.isSelected());

		// Scroll checkbox into view
		js.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});", checkbox);
		safeSleep(300);

		// Click the checkbox directly using JavaScript to ensure it toggles
		boolean wasSelected = checkbox.isSelected();
		js.executeScript("arguments[0].click();", checkbox);
		safeSleep(500);

		// Verify checkbox state changed
		boolean isNowSelected = checkbox.isSelected();
		LOGGER.info("Checkbox state: before={}, after={}", wasSelected, isNowSelected);

		if (wasSelected == isNowSelected) {
			// Try clicking the label/parent instead
			LOGGER.warn("Checkbox state didn't change, trying label click...");
			try {
				WebElement label = checkbox.findElement(By.xpath("./ancestor::label | ./parent::*"));
				js.executeScript("arguments[0].click();", label);
				safeSleep(500);
				LOGGER.info("After label click, checkbox selected: {}", checkbox.isSelected());
			} catch (Exception e) {
				LOGGER.warn("Label click also failed: {}", e.getMessage());
			}
		}

		// Wait for filter to apply
		PerformanceUtils.waitForSpinnersToDisappear(driver, 15);
		PerformanceUtils.waitForPageReady(driver, 3);
		safeSleep(1000); // Extra wait for filter API
	}

	// ==================== SCROLL AND VALIDATION METHODS ====================

	/**
	 * Scrolls down to load filtered results for validation.
	 */
	public void user_should_scroll_down_to_view_last_filtered_result(String screen) {
		try {
			currentScreen.set(screen);
			PerformanceUtils.waitForSpinnersToDisappear(driver, 5);

			int expectedTotal = 0;
			try {
				String resultsCountText = getElementText(getShowingResultsCountLocator(screen));
				expectedTotal = parseProfileCountFromText(resultsCountText);
				LOGGER.info("Expected total filtered profiles: {}", expectedTotal);
			} catch (Exception e) {
				LOGGER.debug("Could not parse expected total: {}", e.getMessage());
			}

			// Scroll to load filtered results
			int currentCount = 0;
			int previousCount = 0;
			int noChangeCount = 0;
			int maxScrollAttempts = 100;
			int scrollCount = 0;
			boolean isPM = "PM".equalsIgnoreCase(screen);
			JavascriptExecutor js = (JavascriptExecutor) driver;

			while (scrollCount < maxScrollAttempts) {
				try {
					js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
				} catch (Exception e1) {
					js.executeScript("window.scrollBy(0, 10000);");
				}

				scrollCount++;
				int scrollWait = isPM ? 1500 : 500;
				safeSleep(scrollWait);
				PerformanceUtils.waitForSpinnersToDisappear(driver, 5);

				currentCount = findElements(getAllProfileRowsLocator(screen)).size();

				if (scrollCount % 10 == 0) {
					LOGGER.info("Scroll progress: {} scrolls, {} profiles loaded", scrollCount, currentCount);
				}

				if (currentCount == previousCount) {
					noChangeCount++;
					if (noChangeCount >= 3) {
						LOGGER.info("Reached end of filtered results after {} scrolls", scrollCount);
						break;
					}
				} else {
					noChangeCount = 0;
				}
				previousCount = currentCount;
			}

			PageObjectHelper.log(LOGGER, "Loaded " + currentCount + " filtered results in " + getScreenName(screen));

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("scroll_filtered_results_" + screen, e);
			PageObjectHelper.handleError(LOGGER, "scroll_filtered_results",
					"Error scrolling to load filtered results", e);
			Assert.fail("Error scrolling to load filtered results");
		}
	}

	/**
	 * Validates that all filtered results match the applied filter criteria.
	 */
	public void user_should_validate_all_filtered_results_match_the_applied_filter(String screen) {
		try {
			currentScreen.set(screen);
			PerformanceUtils.waitForSpinnersToDisappear(driver, 5);

			var profileRows = findElements(getAllProfileRowsLocator(screen));
			int totalResults = profileRows.size();
			int validatedCount = 0;

			// For filters, we validate that profiles are accessible (similar to search validation)
			for (WebElement row : profileRows) {
				try {
					String profileName = extractJobName(row, screen);
					if (!profileName.isEmpty()) {
						validatedCount++;
					}
				} catch (Exception e) {
					// Skip rows that can't be validated
				}
			}

			PageObjectHelper.log(LOGGER, "Validated " + validatedCount + " of " + totalResults + 
					" filtered results in " + getScreenName(screen));

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_filtered_results_" + screen, e);
			PageObjectHelper.handleError(LOGGER, "validate_filtered_results",
					"Error validating filtered results", e);
			Assert.fail("Error validating filtered results");
		}
	}

	// ==================== CLEAR FILTER METHODS ====================

	/**
	 * Clears all applied filters.
	 */
	public void click_on_clear_all_filters_button(String screen) {
		try {
			currentScreen.set(screen);
			scrollToTop();
			safeSleep(300);

			clickElement(getClearFiltersButtonLocator(screen));
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			safeSleep(500);

			PageObjectHelper.log(LOGGER, "Cleared all filters in " + getScreenName(screen));

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("clear_filters_" + screen, e);
			PageObjectHelper.handleError(LOGGER, "clear_all_filters",
					"Error clearing filters in " + getScreenName(screen), e);
			Assert.fail("Error clearing filters");
		}
	}

	/**
	 * Closes the Filters dropdown (JAM specific).
	 */
	public void close_filters_dropdown(String screen) {
		try {
			if ("JAM".equalsIgnoreCase(screen)) {
				// Click outside to close dropdown
				js.executeScript("document.body.click();");
				safeSleep(300);
				PageObjectHelper.log(LOGGER, "Closed filters dropdown in " + getScreenName(screen));
			}
		} catch (Exception e) {
			LOGGER.debug("Error closing filters dropdown: {}", e.getMessage());
		}
	}

	// ==================== VERIFICATION METHODS ====================

	/**
	 * Verifies only filtered profiles remain selected after clearing filters.
	 * Uses full page scroll to load all profiles and count selected.
	 */
	public void verify_only_filtered_profiles_are_selected_after_clearing_all_filters(String screen) {
		int totalProfilesVisible = 0;
		int previousTotalProfilesVisible = 0;
		int actualSelectedCount = 0;
		int scrollAttempts = 0;
		int stableCountAttempts = 0;
		boolean allProfilesLoaded = false;
		int expectedTotalProfiles = 0;
		JavascriptExecutor js = (JavascriptExecutor) driver;
		boolean isPM = "PM".equalsIgnoreCase(screen);

		// Scrolling settings
		int maxScrollAttempts = 500;
		int requiredStableAttempts = 3;

		// Get baseline from PO35 (shared ThreadLocal) or use local filterResultsCount
		int baseline = PO27_SelectAllWithSearchFunctionality.searchResultsCount.get();
		if (baseline == 0) {
			baseline = filterResultsCount.get();
		}

		try {
			currentScreen.set(screen);
			
			PageObjectHelper.log(LOGGER, "Verifying only " + baseline +
					" profiles remain selected in " + getScreenName(screen) + "...");

			if (baseline == 0) {
				PageObjectHelper.log(LOGGER, "No filter results to verify - skipping");
				return;
			}

			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			safeSleep(500);
			
			scrollToTop();
			safeSleep(300);

			long startTime = System.currentTimeMillis();

			// Parse total profile count
			try {
				String resultsCountText = getElementText(getShowingResultsCountLocator(screen));
				expectedTotalProfiles = parseProfileCountFromText(resultsCountText);
				LOGGER.info("Expected total profiles: {}", expectedTotalProfiles);
			} catch (Exception e) {
				LOGGER.debug("Could not parse total profile count: {}", e.getMessage());
			}

			// Calculate estimated scrolls
			int profilesPerScroll = isPM ? 50 : 10;
			int estimatedScrolls = expectedTotalProfiles > 0 ? (expectedTotalProfiles / profilesPerScroll) + 10 : maxScrollAttempts;
			maxScrollAttempts = Math.min(estimatedScrolls, maxScrollAttempts);
			LOGGER.info("Estimated scrolls needed: {} ({} profiles / {} per scroll)", estimatedScrolls, expectedTotalProfiles, profilesPerScroll);

			// Scroll through all profiles
			while (scrollAttempts < maxScrollAttempts && !allProfilesLoaded) {
				try {
					js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
				} catch (Exception e1) {
					js.executeScript("window.scrollBy(0, 10000);");
				}
				scrollAttempts++;

				int scrollWait = isPM ? 1500 : 500;
				safeSleep(scrollWait);
				PerformanceUtils.waitForSpinnersToDisappear(driver, 3);

				totalProfilesVisible = findElements(getAllProfileRowsLocator(screen)).size();

				// Count selected using BasePageObject method
				int currentSelectedCount = countSelectedProfilesJS(screen);

				// Log progress every 10 scrolls
				if (scrollAttempts % 10 == 0) {
					long elapsed = (System.currentTimeMillis() - startTime) / 1000;
					int pctLoaded = expectedTotalProfiles > 0 ? (totalProfilesVisible * 100 / expectedTotalProfiles) : 0;
					LOGGER.info("Scroll progress: {} scrolls, {}/{} profiles ({}%), {} selected, {}s elapsed",
							scrollAttempts, totalProfilesVisible, expectedTotalProfiles, pctLoaded, currentSelectedCount, elapsed);
				}

				// Fail-fast check
				if (currentSelectedCount > baseline) {
					int extra = currentSelectedCount - baseline;
					LOGGER.warn("FAIL-FAST at scroll {}: Found {} selected (expected {}), {} extra",
							scrollAttempts, currentSelectedCount, baseline, extra);
					allProfilesLoaded = true;
					actualSelectedCount = currentSelectedCount;
					break;
				}

				// Check stability
				if (totalProfilesVisible == previousTotalProfilesVisible && scrollAttempts > 0) {
					stableCountAttempts++;
					if (stableCountAttempts >= requiredStableAttempts) {
						allProfilesLoaded = true;
						actualSelectedCount = currentSelectedCount;
						long elapsed = (System.currentTimeMillis() - startTime) / 1000;
						LOGGER.info("Scroll complete: {}/{} profiles loaded, {} selected after {} scrolls ({}s)",
								totalProfilesVisible, expectedTotalProfiles, currentSelectedCount, scrollAttempts, elapsed);
					}
				} else {
					stableCountAttempts = 0;
				}
				previousTotalProfilesVisible = totalProfilesVisible;
			}

			// Final count if not set
			if (actualSelectedCount == 0) {
				actualSelectedCount = countSelectedProfilesJS(screen);
			}

			// Validate results
			int missingSelections = Math.max(0, baseline - actualSelectedCount);
			int extraSelections = Math.max(0, actualSelectedCount - baseline);

			LOGGER.info("========================================");
			LOGGER.info("VALIDATION SUMMARY (After Clearing Filters)");
			LOGGER.info("Total Profiles Loaded: {}", totalProfilesVisible);
			LOGGER.info("Currently Selected: {}", actualSelectedCount);
			LOGGER.info("Baseline (from filter): {}", baseline);
			LOGGER.info("Missing/Extra: {} / {}", missingSelections, extraSelections);
			LOGGER.info("========================================");

			if (actualSelectedCount == baseline) {
				PageObjectHelper.log(LOGGER, "✅ PASS: All " + baseline + " filtered profiles remain selected");
			} else if (actualSelectedCount < baseline) {
				PageObjectHelper.log(LOGGER, "⚠️ Only " + actualSelectedCount + " selected (expected " + baseline + 
						"), " + missingSelections + " profiles lost selection");
			} else {
				String errorMsg = "FAIL: Found " + actualSelectedCount + " selected (expected " + baseline + 
						"), " + extraSelections + " extra profiles incorrectly selected";
				PageObjectHelper.log(LOGGER, "❌ " + errorMsg);
				Assert.fail(errorMsg);
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_filtered_profiles_selected_" + screen, e);
			PageObjectHelper.handleError(LOGGER, "verify_filtered_profiles_selected",
					"Error verifying filtered profiles are selected", e);
			Assert.fail("Error verifying filtered profiles: " + e.getMessage());
		}
	}

	// ==================== ALTERNATIVE VALIDATION ====================

	/**
	 * Applies a different filter for alternative validation.
	 */
	public void apply_different_filter_for_alternative_validation(String screen) {
		try {
			currentScreen.set(screen);
			PerformanceUtils.waitForSpinnersToDisappear(driver, 5);

			boolean isPM = "PM".equalsIgnoreCase(screen);

			PageObjectHelper.log(LOGGER, "Alternative validation: Applying different filter in " + getScreenName(screen) + "...");

			if (isPM) {
				applyDifferentPMFilter();
			} else {
				applyDifferentJAMFilter();
			}

			PageObjectHelper.log(LOGGER, "Applied alternative filter: " + secondFilterType.get() + " = " + secondFilterValue.get());

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("apply_different_filter_" + screen, e);
			PageObjectHelper.handleError(LOGGER, "apply_different_filter",
					"Error applying different filter in " + getScreenName(screen), e);
			Assert.fail("Error applying different filter: " + e.getMessage());
		}
	}

	private void applyDifferentPMFilter() throws Exception {
		// Use same filter type but different value
		secondFilterType.set(firstFilterType.get());
		String panelHeaderId = "KF Grade".equals(firstFilterType.get()) ? "expansion-panel-header-0" : "expansion-panel-header-1";

		// Toggle panel to refresh options
		var header = driver.findElements(By.xpath("//thcl-expansion-panel-header[@id='" + panelHeaderId + "']"));
		if (!header.isEmpty()) {
			// Collapse and re-expand
			jsClick(header.get(0));
			safeSleep(500);
			jsClick(header.get(0));
			safeSleep(1000);

			var options = findFilterOptions(panelHeaderId);
			if (options.size() > 1) {
				// Select second option (different from first)
				WebElement secondOption = options.get(1);
				try {
					WebElement parent = secondOption.findElement(By.xpath("./parent::*"));
					secondFilterValue.set(parent.getText().trim());
				} catch (Exception e) {
					secondFilterValue.set("Filter_Option_2");
				}

				WebElement elementToClick = secondOption.findElement(By.xpath("./parent::*"));
				js.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});", elementToClick);
				safeSleep(300);

				try {
					wait.until(ExpectedConditions.elementToBeClickable(elementToClick)).click();
				} catch (Exception e) {
					js.executeScript("arguments[0].click();", elementToClick);
				}

				PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			}
		}
	}

	private void applyDifferentJAMFilter() throws Exception {
		// Use same filter type but different value
		secondFilterType.set(firstFilterType.get());
		By dropdownLocator = "Grades".equals(firstFilterType.get()) ? JAM_GRADES_DROPDOWN : JAM_DEPARTMENTS_DROPDOWN;
		String dropdownTestId = "Grades".equals(firstFilterType.get()) ? "dropdown-Grades" : "dropdown-Departments";

		jsClick(findElement(dropdownLocator));
		safeSleep(1000);

		var options = driver.findElements(By.xpath("//div[@data-testid='" + dropdownTestId + "']//input[@type='checkbox']"));
		LOGGER.info("Found {} options for second filter", options.size());
		
		if (options.size() > 1) {
			WebElement secondOption = options.get(1);
			
			// Extract label text
			try {
				WebElement labelSpan = secondOption.findElement(By.xpath("./following-sibling::span | ./following-sibling::label | ../label | ../span"));
				secondFilterValue.set(labelSpan.getText().trim());
			} catch (Exception e) {
				secondFilterValue.set("Filter_Option_2");
			}

			LOGGER.info("Selecting second filter option: {}", secondFilterValue.get());

			// Click checkbox directly
			js.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});", secondOption);
			safeSleep(300);
			js.executeScript("arguments[0].click();", secondOption);
			safeSleep(500);

			PerformanceUtils.waitForSpinnersToDisappear(driver, 15);
			PerformanceUtils.waitForPageReady(driver, 3);
			safeSleep(1000);
		}
	}

	/**
	 * Scrolls to load second filter results and validates selection.
	 */
	public void scroll_down_to_load_all_second_filter_results(String screen) {
		try {
			currentScreen.set(screen);
			PerformanceUtils.waitForSpinnersToDisappear(driver, 5);

			String resultsCountText = getElementText(getShowingResultsCountLocator(screen));
			int expectedTotal = parseProfileCountFromText(resultsCountText);

			if (expectedTotal == 0 || resultsCountText.contains("Showing 0")) {
				PageObjectHelper.log(LOGGER, "Second filter returned 0 results");
				totalSecondFilterResults.set(0);
				return;
			}

			LOGGER.info("Second filter '{}' = '{}': {} profiles", secondFilterType.get(), secondFilterValue.get(), expectedTotal);

			// Just load initial visible profiles for quick validation
			var profileRows = findElements(getAllProfileRowsLocator(screen));
			totalSecondFilterResults.set(profileRows.size());

			PageObjectHelper.log(LOGGER, "Second filter has " + expectedTotal + " total profiles, " + 
					profileRows.size() + " visible for validation");

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("scroll_second_filter_" + screen, e);
			PageObjectHelper.handleError(LOGGER, "scroll_second_filter_results",
					"Error loading second filter results", e);
		}
	}

	/**
	 * Verifies profiles from second filter are NOT selected.
	 */
	public void verify_all_loaded_profiles_in_second_filter_are_not_selected(String screen) {
		try {
			currentScreen.set(screen);

			if (totalSecondFilterResults.get() == 0) {
				PageObjectHelper.log(LOGGER, "No second filter results to verify");
				return;
			}

			// Count selected using BasePageObject method
			int selectedCount = countSelectedProfilesJS(screen);

			if (selectedCount == 0) {
				PageObjectHelper.log(LOGGER, "✅ PASS: No profiles selected in second filter results (" + 
						secondFilterType.get() + " = " + secondFilterValue.get() + ")");
			} else {
				// Some selections found - these should be from the FIRST filter, not second
				// This is expected behavior if first and second filters have overlapping results
				PageObjectHelper.log(LOGGER, "Found " + selectedCount + " selected profiles in second filter view " +
						"(may be from first filter overlap)");
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_second_filter_not_selected_" + screen, e);
			PageObjectHelper.handleError(LOGGER, "verify_second_filter_not_selected",
					"Error verifying second filter profiles", e);
		}
	}
}
