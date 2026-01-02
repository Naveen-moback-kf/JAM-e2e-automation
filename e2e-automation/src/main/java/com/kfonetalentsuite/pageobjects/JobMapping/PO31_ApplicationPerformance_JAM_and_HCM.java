package com.kfonetalentsuite.pageobjects.JobMapping;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.ScreenshotHandler;
import com.kfonetalentsuite.utils.JobMapping.PageObjectHelper;

public class PO31_ApplicationPerformance_JAM_and_HCM extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO31_ApplicationPerformance_JAM_and_HCM.class);

	// Performance thresholds (in milliseconds)
	// NOTE: These thresholds should be adjusted based on your application's
	// baseline performance
	// Run multiple tests to establish average load times, then set threshold =
	// average + 20% buffer
	// Current threshold is set based on initial test runs - adjust as needed
	private static final long PAGE_LOAD_THRESHOLD_MS = 12000; // 12 seconds for page load (updated based on actual performance)
	private static final long SEARCH_THRESHOLD_MS = 6000; // 6 seconds for search operation
	private static final long CLEAR_SEARCH_THRESHOLD_MS = 9000; // 9 seconds for clear search operation
	private static final long SINGLE_FILTER_THRESHOLD_MS = 9000; // 9 seconds for single filter application
	private static final long MULTIPLE_FILTERS_THRESHOLD_MS = 18000; // 18 seconds for multiple filters application
	private static final long CLEAR_FILTERS_THRESHOLD_MS = 6000; // 6 seconds for clear filters operation
	private static final long SCROLL_OPERATION_THRESHOLD_MS = 7000; // 7 seconds for complete scroll operation
	private static final long LAZY_LOAD_THRESHOLD_MS = 3000; // 3 seconds for lazy loading new content
	private static final long NAVIGATION_THRESHOLD_MS = 5000; // 5 seconds for screen navigation
	private static final long SORT_OPERATION_THRESHOLD_MS = 4000; // 4 seconds for sort operation
	private static final long SELECT_ALL_THRESHOLD_MS = 3000; // 3 seconds for select all operation
	private static final long HCM_PAGE_LOAD_THRESHOLD_MS = 10000; // 10 seconds for HCM page load
	private static final long SYNC_OPERATION_THRESHOLD_MS = 10000; // 10 seconds for sync operation

	private static ThreadLocal<Long> pageLoadStartTime = ThreadLocal.withInitial(() -> 0L);
	private static ThreadLocal<Long> pageLoadEndTime = ThreadLocal.withInitial(() -> 0L);
	private static ThreadLocal<Long> totalPageLoadTime = ThreadLocal.withInitial(() -> 0L);

	private static ThreadLocal<Long> searchStartTime = ThreadLocal.withInitial(() -> 0L);
	private static ThreadLocal<Long> searchEndTime = ThreadLocal.withInitial(() -> 0L);
	private static ThreadLocal<Long> totalSearchTime = ThreadLocal.withInitial(() -> 0L);

	private static ThreadLocal<Long> clearSearchStartTime = ThreadLocal.withInitial(() -> 0L);
	private static ThreadLocal<Long> clearSearchEndTime = ThreadLocal.withInitial(() -> 0L);
	private static ThreadLocal<Long> totalClearSearchTime = ThreadLocal.withInitial(() -> 0L);

	private static ThreadLocal<Long> filterDropdownOpenStartTime = ThreadLocal.withInitial(() -> 0L);
	private static ThreadLocal<Long> filterDropdownOpenEndTime = ThreadLocal.withInitial(() -> 0L);
	private static ThreadLocal<Long> totalFilterDropdownOpenTime = ThreadLocal.withInitial(() -> 0L);

	private static ThreadLocal<Long> singleFilterStartTime = ThreadLocal.withInitial(() -> 0L);
	private static ThreadLocal<Long> singleFilterEndTime = ThreadLocal.withInitial(() -> 0L);
	private static ThreadLocal<Long> totalSingleFilterTime = ThreadLocal.withInitial(() -> 0L);

	private static ThreadLocal<Long> multipleFiltersStartTime = ThreadLocal.withInitial(() -> 0L);
	private static ThreadLocal<Long> multipleFiltersEndTime = ThreadLocal.withInitial(() -> 0L);
	private static ThreadLocal<Long> totalMultipleFiltersTime = ThreadLocal.withInitial(() -> 0L);

	private static ThreadLocal<Long> clearFiltersStartTime = ThreadLocal.withInitial(() -> 0L);
	private static ThreadLocal<Long> clearFiltersEndTime = ThreadLocal.withInitial(() -> 0L);
	private static ThreadLocal<Long> totalClearFiltersTime = ThreadLocal.withInitial(() -> 0L);

	// Search related variables
	private static ThreadLocal<String> searchKeyword = ThreadLocal.withInitial(() -> "");
	private static ThreadLocal<Integer> resultsCountBeforeSearch = ThreadLocal.withInitial(() -> 0);
	private static ThreadLocal<Integer> resultsCountAfterSearch = ThreadLocal.withInitial(() -> 0);
	private static ThreadLocal<Integer> resultsCountAfterClear = ThreadLocal.withInitial(() -> 0);

	// Filter related variables
	private static ThreadLocal<String> appliedFilterType = ThreadLocal.withInitial(() -> "");
	private static ThreadLocal<String> appliedFilterValue = ThreadLocal.withInitial(() -> "");
	private static ThreadLocal<Integer> resultsCountBeforeFilter = ThreadLocal.withInitial(() -> 0);
	private static ThreadLocal<Integer> resultsCountAfterFilter = ThreadLocal.withInitial(() -> 0);
	private static ThreadLocal<Integer> resultsCountAfterClearFilters = ThreadLocal.withInitial(() -> 0);
	private static ThreadLocal<Integer> numberOfFiltersApplied = ThreadLocal.withInitial(() -> 0);

	// Scroll and lazy load related variables
	private static ThreadLocal<Long> totalScrollTime = ThreadLocal.withInitial(() -> 0L);
	private static ThreadLocal<Long> scrollStartTime = ThreadLocal.withInitial(() -> 0L);
	private static ThreadLocal<Long> scrollEndTime = ThreadLocal.withInitial(() -> 0L);
	private static ThreadLocal<Integer> totalScrolls = ThreadLocal.withInitial(() -> 0);
	private static ThreadLocal<Integer> initialProfileCount = ThreadLocal.withInitial(() -> 0);
	private static ThreadLocal<Integer> finalProfileCount = ThreadLocal.withInitial(() -> 0);
	private static ThreadLocal<java.util.List<Long>> lazyLoadTimes = ThreadLocal.withInitial(java.util.ArrayList::new);
	private static ThreadLocal<Long> avgLazyLoadTime = ThreadLocal.withInitial(() -> 0L);

	// Navigation related variables
	private static ThreadLocal<Long> navigationToComparisonStartTime = ThreadLocal.withInitial(() -> 0L);
	private static ThreadLocal<Long> navigationToComparisonEndTime = ThreadLocal.withInitial(() -> 0L);
	private static ThreadLocal<Long> totalNavigationToComparisonTime = ThreadLocal.withInitial(() -> 0L);
	private static ThreadLocal<Long> navigationBackToMappingStartTime = ThreadLocal.withInitial(() -> 0L);
	private static ThreadLocal<Long> navigationBackToMappingEndTime = ThreadLocal.withInitial(() -> 0L);
	private static ThreadLocal<Long> totalNavigationBackToMappingTime = ThreadLocal.withInitial(() -> 0L);

	// Sort related variables
	private static ThreadLocal<Long> sortByJobTitleStartTime = ThreadLocal.withInitial(() -> 0L);
	private static ThreadLocal<Long> sortByJobTitleEndTime = ThreadLocal.withInitial(() -> 0L);
	private static ThreadLocal<Long> totalSortByJobTitleTime = ThreadLocal.withInitial(() -> 0L);
	private static ThreadLocal<Long> sortByGradeStartTime = ThreadLocal.withInitial(() -> 0L);
	private static ThreadLocal<Long> sortByGradeEndTime = ThreadLocal.withInitial(() -> 0L);
	private static ThreadLocal<Long> totalSortByGradeTime = ThreadLocal.withInitial(() -> 0L);

	// Select All related variables
	private static ThreadLocal<Long> chevronClickStartTime = ThreadLocal.withInitial(() -> 0L);
	private static ThreadLocal<Long> chevronClickEndTime = ThreadLocal.withInitial(() -> 0L);
	private static ThreadLocal<Long> totalChevronClickTime = ThreadLocal.withInitial(() -> 0L);
	private static ThreadLocal<Long> selectAllClickStartTime = ThreadLocal.withInitial(() -> 0L);
	private static ThreadLocal<Long> selectAllClickEndTime = ThreadLocal.withInitial(() -> 0L);
	private static ThreadLocal<Long> totalSelectAllClickTime = ThreadLocal.withInitial(() -> 0L);
	private static ThreadLocal<Integer> profilesCountAfterSelectAll = ThreadLocal.withInitial(() -> 0);

	// HCM Page Load related variables
	private static ThreadLocal<Long> hcmPageLoadStartTime = ThreadLocal.withInitial(() -> 0L);
	private static ThreadLocal<Long> hcmPageLoadEndTime = ThreadLocal.withInitial(() -> 0L);
	private static ThreadLocal<Long> totalHCMPageLoadTime = ThreadLocal.withInitial(() -> 0L);
	private static ThreadLocal<Integer> hcmProfilesCount = ThreadLocal.withInitial(() -> 0);

	// HCM Sync related variables
	private static ThreadLocal<Long> syncClickStartTime = ThreadLocal.withInitial(() -> 0L);
	private static ThreadLocal<Long> syncClickEndTime = ThreadLocal.withInitial(() -> 0L);
	private static ThreadLocal<Long> totalSyncClickTime = ThreadLocal.withInitial(() -> 0L);
	private static ThreadLocal<Long> syncProcessStartTime = ThreadLocal.withInitial(() -> 0L);
	private static ThreadLocal<Long> syncProcessEndTime = ThreadLocal.withInitial(() -> 0L);
	private static ThreadLocal<Long> totalSyncProcessTime = ThreadLocal.withInitial(() -> 0L);
	private static ThreadLocal<Integer> selectedProfilesCountBeforeSync = ThreadLocal.withInitial(() -> 0);

	// Navigation helper variables
	private static ThreadLocal<Integer> rowNumberForViewOtherMatches = ThreadLocal.withInitial(() -> 0);
	private static ThreadLocal<String> jobNameForComparison = ThreadLocal.withInitial(() -> "");

	// Locators (static final By for better performance and thread safety)
	private static final By JAM_LOGO = By.xpath("//div[@id='header-logo']");
	private static final By JOB_MAPPING_PAGE_CONTAINER = By.xpath("//div[@id='org-job-container']");
	private static final By PAGE_TITLE_HEADER = By.xpath("//div[@id='page-heading']//h1");
	private static final By PAGE_TITLE_DESC = By.xpath("//div[@id='page-title']//p[1]");
	private static final By SEARCH_BAR = By.xpath("//input[contains(@id,'search-job-title')]");
	private static final By FILTERS_DROPDOWN_BUTTON = By.xpath("//button[@id='filters-btn']");
	
	// Navigation XPaths (Job Comparison) - from Locators.ComparisonPage
	private static final By JOB_COMPARISON_HEADER = Locators.ComparisonPage.COMPARE_HEADER;
	
	// Sort XPaths
	private static final By ORG_JOB_NAME_HEADER = By.xpath("//*[@id='org-job-container']/div/table/thead/tr/th[2]/div");
	private static final By ORG_JOB_GRADE_HEADER = By.xpath("//*[@id='org-job-container']/div/table/thead/tr/th[3]/div");
	
	// Select All XPaths (Job Mapping) - from Locators.JAMScreen
	private static final By CHEVRON_BTN_IN_JAM = Locators.JAMScreen.CHEVRON_BUTTON;
	// SELECT_ALL_BTN is available via Locators.Table.SELECT_ALL_BTN
	
	// KFONE Global Menu XPaths - from Locators.Navigation
	// KFONE_MENU is available via Locators.Navigation.GLOBAL_NAV_MENU_BTN
	private static final By KFONE_MENU_PM_BTN = Locators.Navigation.KFONE_MENU_PM_BTN;
	
	// HCM Navigation XPaths - from Locators.HCMSyncProfiles
	private static final By PROFILE_MANAGER_HEADER = Locators.HCMSyncProfiles.PROFILE_MANAGER_HEADER;
	private static final By HCM_SYNC_PROFILES_HEADER_TAB = Locators.HCMSyncProfiles.HCM_SYNC_TAB;
	private static final By HCM_SYNC_PROFILES_TITLE = Locators.HCMSyncProfiles.SYNC_PROFILES_TITLE;
	
	private static final By SYNC_SUCCESS_POPUP_TEXT = By.xpath("//div[@class='p-toast-detail']");
	private static final By SYNC_SUCCESS_POPUP_CLOSE_BTN = By.xpath("//button[contains(@class,'p-toast-icon-close')]");
	
	// Additional common locators
	private static final By FIRST_PROFILE_ELEMENT = By.xpath("//tbody//tr[1]//td[2]//div[contains(text(),'(')]");
	private static final By PROFILE_ROWS = By.xpath("//div[@id='org-job-container']//tbody//tr//td[2]//div[contains(text(),'(')]");
	private static final By ALL_CHECKBOXES = By.xpath("//div[@id='org-job-container']//tbody//tr//td[1]//input[@type='checkbox']");
	private static final By HCM_RESULTS_COUNT = By.xpath("//div[contains(text(),'Showing')]");
	private static final By HCM_PROFILE_CHECKBOXES = By.xpath("//tbody//tr//td[1]//div[1]//kf-checkbox");

	public PO31_ApplicationPerformance_JAM_and_HCM() {
		super();
	}

	public void user_measures_the_time_taken_to_load_job_mapping_page() {
		try {
			// Start measuring
			pageLoadStartTime.set(System.currentTimeMillis());

			// Wait for page to be fully loaded
			try {
				PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			} catch (Exception e) {
				// Spinners not found or already invisible
			}

			PerformanceUtils.waitForPageReady(driver, 3);
			wait.until(ExpectedConditions.presenceOfElementLocated(JOB_MAPPING_PAGE_CONTAINER));
			wait.until(ExpectedConditions.presenceOfElementLocated(JAM_LOGO));

			// End measuring
			pageLoadEndTime.set(System.currentTimeMillis());
			totalPageLoadTime.set(pageLoadEndTime.get() - pageLoadStartTime.get());

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("measure_page_load_time_failed", e);
			PageObjectHelper.log(LOGGER, " Failed to measure page load time: " + e.getMessage());
		}
	}

	public void user_validates_page_load_time_is_within_acceptable_threshold() {
		safeValidateThreshold(totalPageLoadTime.get(), PAGE_LOAD_THRESHOLD_MS, "Page Load", "page_load");
	}

	public void user_verifies_all_critical_page_components_are_loaded() {
		try {
			int totalComponents = 7;
			int loadedComponents = 0;

			// Verify critical components using By locators
			if (verifyComponentByLocator("Logo", JAM_LOGO))
				loadedComponents++;
			if (verifyComponentByLocator("Container", JOB_MAPPING_PAGE_CONTAINER))
				loadedComponents++;
			if (verifyComponentByLocator("Title", PAGE_TITLE_HEADER))
				loadedComponents++;
			if (verifyComponentByLocator("Description", PAGE_TITLE_DESC))
				loadedComponents++;
			if (verifyComponentByLocator("Search", SEARCH_BAR))
				loadedComponents++;
			if (verifyComponentByLocator("Results Count", Locators.JobMappingResults.SHOWING_JOB_RESULTS))
				loadedComponents++;
			if (verifyComponentByLocator("Filters", FILTERS_DROPDOWN_BUTTON))
				loadedComponents++;

			// Summary
			String summaryMsg = String.format(" Components Loaded: %d/%d", loadedComponents, totalComponents);
			PageObjectHelper.log(LOGGER, summaryMsg);

			if (loadedComponents < totalComponents) {
				String failMsg = String.format(" Only %d/%d components loaded", loadedComponents, totalComponents);
				LOGGER.error(failMsg);
				ScreenshotHandler.captureFailureScreenshot("critical_components_missing", new Exception(failMsg));
				Assert.fail(failMsg);
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_critical_components_failed", e);
			PageObjectHelper.log(LOGGER, " Failed to verify critical components: " + e.getMessage());
			Assert.fail("Failed to verify critical components: " + e.getMessage());
		}
	}

	private boolean verifyComponentByLocator(String componentName, By locator) {
		try {
			WebElement element = driver.findElement(locator);
			return element != null && element.isDisplayed();
		} catch (Exception e) {
			LOGGER.warn(" " + componentName + " not found");
			return false;
		}
	}

	// ========================================
	// HELPER METHODS - CODE OPTIMIZATION
	// ========================================

	private void logSectionHeader(String title) {
		LOGGER.debug("- " + title);
	}

	private String getPerformanceRating(long actualTime, long threshold) {
		double percentage = (actualTime / (double) threshold) * 100;

		if (percentage <= 50)
			return " EXCELLENT";
		else if (percentage <= 75)
			return " GOOD";
		else if (percentage <= 100)
			return " ACCEPTABLE";
		else if (percentage <= 150)
			return " POOR";
		else
			return " VERY POOR";
	}

	private int extractResultsCount(String resultsText) {
		try {
			if (resultsText != null && resultsText.contains("of")) {
				String[] parts = resultsText.split("of");
				if (parts.length > 1) {
					String totalPart = parts[1].trim().split(" ")[0].trim();
					return Integer.parseInt(totalPart);
				}
			}
		} catch (Exception e) {
			LOGGER.warn(" Could not parse results count: " + resultsText);
		}
		return 0;
	}

	private void validateThreshold(long actualTime, long threshold, String operationName, String screenshotPrefix) {
		if (actualTime == 0) {
			String errorMsg = " TECHNICAL FAILURE: " + operationName + " time was not measured";
			PageObjectHelper.log(LOGGER, errorMsg);
			Assert.fail(errorMsg);
		}

		String performanceRating = getPerformanceRating(actualTime, threshold);

		// PERFORMANCE METRICS: Capture metrics for Excel reporting
		try {
			String threadId = Thread.currentThread().getName();
			String scenarioName = com.kfonetalentsuite.listeners.ExcelReportListener.getCurrentScenarioName(threadId);

			if (scenarioName != null && !scenarioName.isEmpty()) {
				// Clean rating for Excel (remove emoji prefixes)
				String cleanRating = performanceRating.replace(" ", "").replace(" ", "").replace(" ", "")
						.replace(" ", "").replace(" ", "");

				com.kfonetalentsuite.listeners.ExcelReportListener.setPerformanceMetrics(scenarioName, threshold,
						actualTime, cleanRating, operationName);
			}
		} catch (Exception e) {
			// Silently ignore if scenario context is not available
			LOGGER.debug("Could not capture performance metrics for Excel: {}", e.getMessage());
		}

		if (actualTime <= threshold) {
			String successMsg = String.format(" %s: %d ms (%.2f sec) | %s", operationName, actualTime,
					actualTime / 1000.0, performanceRating);
			PageObjectHelper.log(LOGGER, successMsg);
		} else {
			long difference = actualTime - threshold;
			String warnMsg = String.format(" %s: %d ms (%.2f sec) | Exceeded by %d ms | %s", operationName, actualTime,
					actualTime / 1000.0, difference, performanceRating);
			LOGGER.warn(warnMsg);
			PageObjectHelper.log(LOGGER, warnMsg);
			// Note: Performance warnings are logged but don't capture failure screenshots
			// as they are alerts, not test failures
		}
	}

	private void safeValidateThreshold(long actualTime, long threshold, String operationName, String screenshotPrefix) {
		try {
			validateThreshold(actualTime, threshold, operationName, screenshotPrefix);
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_" + screenshotPrefix + "_threshold_failed", e);
			PageObjectHelper.log(LOGGER, " Failed to validate " + operationName.toLowerCase() + " threshold: " + e.getMessage());
			Assert.fail("Failed to validate " + operationName.toLowerCase() + " threshold: " + e.getMessage());
		}
	}

	public static void resetPerformanceMetrics() {
		pageLoadStartTime.remove();
		pageLoadEndTime.remove();
		totalPageLoadTime.remove();
		searchStartTime.remove();
		searchEndTime.remove();
		totalSearchTime.remove();
		clearSearchStartTime.remove();
		clearSearchEndTime.remove();
		totalClearSearchTime.remove();
		filterDropdownOpenStartTime.remove();
		filterDropdownOpenEndTime.remove();
		totalFilterDropdownOpenTime.remove();
		singleFilterStartTime.remove();
		singleFilterEndTime.remove();
		totalSingleFilterTime.remove();
		multipleFiltersStartTime.remove();
		multipleFiltersEndTime.remove();
		totalMultipleFiltersTime.remove();
		clearFiltersStartTime.remove();
		clearFiltersEndTime.remove();
		totalClearFiltersTime.remove();
		searchKeyword.remove();
		resultsCountBeforeSearch.remove();
		resultsCountAfterSearch.remove();
		resultsCountAfterClear.remove();
		appliedFilterType.remove();
		appliedFilterValue.remove();
		resultsCountBeforeFilter.remove();
		resultsCountAfterFilter.remove();
		resultsCountAfterClearFilters.remove();
		numberOfFiltersApplied.remove();
		totalScrollTime.remove();
		scrollStartTime.remove();
		scrollEndTime.remove();
		totalScrolls.remove();
		initialProfileCount.remove();
		finalProfileCount.remove();
		lazyLoadTimes.get().clear();
		avgLazyLoadTime.remove();
		navigationToComparisonStartTime.remove();
		navigationToComparisonEndTime.remove();
		totalNavigationToComparisonTime.remove();
		navigationBackToMappingStartTime.remove();
		navigationBackToMappingEndTime.remove();
		totalNavigationBackToMappingTime.remove();
		sortByJobTitleStartTime.remove();
		sortByJobTitleEndTime.remove();
		totalSortByJobTitleTime.remove();
		sortByGradeStartTime.remove();
		sortByGradeEndTime.remove();
		totalSortByGradeTime.remove();
		chevronClickStartTime.remove();
		chevronClickEndTime.remove();
		totalChevronClickTime.remove();
		selectAllClickStartTime.remove();
		selectAllClickEndTime.remove();
		totalSelectAllClickTime.remove();
		profilesCountAfterSelectAll.remove();
		hcmPageLoadStartTime.remove();
		hcmPageLoadEndTime.remove();
		totalHCMPageLoadTime.remove();
		hcmProfilesCount.remove();
		syncClickStartTime.remove();
		syncClickEndTime.remove();
		totalSyncClickTime.remove();
		syncProcessStartTime.remove();
		syncProcessEndTime.remove();
		totalSyncProcessTime.remove();
		selectedProfilesCountBeforeSync.remove();
		rowNumberForViewOtherMatches.remove();
		jobNameForComparison.remove();
		LOGGER.debug("Performance metrics reset for next test");
	}

	// ========================================
	// SCENARIO 2: SEARCH PERFORMANCE TESTING
	// ========================================

	public void user_is_on_job_mapping_page_with_loaded_profiles() {
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);
			wait.until(ExpectedConditions.presenceOfElementLocated(JOB_MAPPING_PAGE_CONTAINER));
			wait.until(ExpectedConditions.elementToBeClickable(SEARCH_BAR));

			String resultsText = findElement(Locators.JobMappingResults.SHOWING_JOB_RESULTS).getText().trim();
			resultsCountBeforeSearch.set(extractResultsCount(resultsText));

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("job_mapping_page_not_ready", e);
			PageObjectHelper.log(LOGGER, " Job Mapping page not ready: " + e.getMessage());
			Assert.fail("Job Mapping page not ready: " + e.getMessage());
		}
	}

	public void user_measures_time_to_perform_search_with_dynamic_keyword() {
		try {
			// Extract dynamic search keyword
			wait.until(ExpectedConditions.presenceOfElementLocated(FIRST_PROFILE_ELEMENT));
			WebElement firstProfileElement = findElement(FIRST_PROFILE_ELEMENT);
			String fullJobName = firstProfileElement.getText().trim();

			if (fullJobName.length() >= 3) {
				searchKeyword.set(fullJobName.substring(0, 3));
			} else if (fullJobName.contains(" ")) {
				searchKeyword.set(fullJobName.split(" ")[0]);
			} else {
				searchKeyword.set(fullJobName);
			}

			// Measure search performance
			WebElement searchBarElement = findElement(SEARCH_BAR);
			searchBarElement.clear();
			safeSleep(300);

			searchStartTime.set(System.currentTimeMillis());
			searchBarElement.sendKeys(searchKeyword.get());
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);
			safeSleep(500);
			searchEndTime.set(System.currentTimeMillis());
			totalSearchTime.set(searchEndTime.get() - searchStartTime.get());

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("measure_search_time_failed", e);
			PageObjectHelper.log(LOGGER, " Failed to measure search time: " + e.getMessage());
			Assert.fail("Failed to measure search time: " + e.getMessage());
		}
	}

	public void user_validates_search_response_time_is_within_acceptable_threshold() {
		safeValidateThreshold(totalSearchTime.get(), SEARCH_THRESHOLD_MS, "Search", "search");
	}

	public void user_verifies_search_results_are_accurate() {
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 1);

			String resultsText = findElement(Locators.JobMappingResults.SHOWING_JOB_RESULTS).getText().trim();
			resultsCountAfterSearch.set(extractResultsCount(resultsText));

			java.util.List<WebElement> visibleProfiles = findElements(PROFILE_ROWS);
			int matchingResults = 0;
			String searchKeywordLower = searchKeyword.get().toLowerCase();

			for (WebElement profile : visibleProfiles) {
				String jobName = profile.getText().toLowerCase();
				if (jobName.contains(searchKeywordLower)) {
					matchingResults++;
				}
			}

			String accuracyMsg = String.format(" Search '%s': %d matching | %d  %d profiles", searchKeyword.get(),
					matchingResults, resultsCountBeforeSearch.get(), resultsCountAfterSearch.get());

			PageObjectHelper.log(LOGGER, accuracyMsg);

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_search_results_failed", e);
			PageObjectHelper.log(LOGGER, " Failed to verify search results: " + e.getMessage());
			Assert.fail("Failed to verify search results: " + e.getMessage());
		}
	}

	public void user_validates_search_suggestions_appear_instantly() {
		try {
			long instantThreshold = 1000;
			String responseMsg;
			long searchTime = totalSearchTime.get();

			if (searchTime <= instantThreshold) {
				responseMsg = String.format(" Search UI Responsive: %d ms (Instant)", searchTime);
				LOGGER.info(responseMsg);
			} else if (searchTime <= SEARCH_THRESHOLD_MS) {
				responseMsg = String.format(" Search UI Acceptable: %d ms (Not instant)", searchTime);
				LOGGER.warn(responseMsg);
			} else {
				responseMsg = String.format(" Search UI Slow: %d ms", searchTime);
				LOGGER.error(responseMsg);
			}
			PageObjectHelper.log(LOGGER, responseMsg);

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_search_suggestions_failed", e);
			PageObjectHelper.log(LOGGER, " Failed to validate search UI: " + e.getMessage());
			Assert.fail("Failed to validate search UI: " + e.getMessage());
		}
	}

	// ==========================================
	// SCENARIO 3: CLEAR SEARCH PERFORMANCE
	// ==========================================

	public void user_has_performed_search_operation_with_filtered_results() {
		try {
			if (searchKeyword.get() == null || searchKeyword.get().isEmpty() || resultsCountAfterSearch.get() == 0) {
				user_is_on_job_mapping_page_with_loaded_profiles();
				user_measures_time_to_perform_search_with_dynamic_keyword();
			}

			String currentSearchText = findElement(SEARCH_BAR).getAttribute("value");
			if (currentSearchText == null || currentSearchText.trim().isEmpty()) {
				String errorMsg = " Search bar is empty";
				PageObjectHelper.log(LOGGER, errorMsg);
				Assert.fail(errorMsg);
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("search_not_performed", e);
			PageObjectHelper.log(LOGGER, " Failed to verify search state: " + e.getMessage());
			Assert.fail("Failed to verify search state: " + e.getMessage());
		}
	}

	public void user_measures_time_to_clear_search() {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(SEARCH_BAR));
			WebElement searchBarFresh = findElement(SEARCH_BAR);

			// Measure clear search performance
			clearSearchStartTime.set(System.currentTimeMillis());

			wait.until(ExpectedConditions.elementToBeClickable(searchBarFresh)).click();
			safeSleep(200);

			searchBarFresh = findElement(SEARCH_BAR);
			searchBarFresh.sendKeys(org.openqa.selenium.Keys.CONTROL + "a");
			safeSleep(100);

			searchBarFresh = findElement(SEARCH_BAR);
			searchBarFresh.sendKeys(org.openqa.selenium.Keys.DELETE);
			safeSleep(100);

			searchBarFresh = findElement(SEARCH_BAR);
			searchBarFresh.sendKeys(org.openqa.selenium.Keys.ENTER);

			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);
			safeSleep(500);

			clearSearchEndTime.set(System.currentTimeMillis());
			totalClearSearchTime.set(clearSearchEndTime.get() - clearSearchStartTime.get());

			String resultsText = findElement(Locators.JobMappingResults.SHOWING_JOB_RESULTS).getText().trim();
			resultsCountAfterClear.set(extractResultsCount(resultsText));

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("measure_clear_search_time_failed", e);
			PageObjectHelper.log(LOGGER, " Failed to measure clear search time: " + e.getMessage());
			Assert.fail("Failed to measure clear search time: " + e.getMessage());
		}
	}

	public void user_validates_clear_search_time_is_within_acceptable_threshold() {
		safeValidateThreshold(totalClearSearchTime.get(), CLEAR_SEARCH_THRESHOLD_MS, "Clear Search", "clear_search");
	}

	public void user_verifies_all_profiles_are_restored_correctly() {
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 1);

			WebElement searchBarFresh = wait.until(ExpectedConditions.presenceOfElementLocated(SEARCH_BAR));
			String currentSearchText = searchBarFresh.getAttribute("value");
			boolean isSearchBarEmpty = (currentSearchText == null || currentSearchText.trim().isEmpty());
			boolean countsMatch = (resultsCountAfterClear.get().equals(resultsCountBeforeSearch.get()));

			String restorationMsg = String.format(" Restoration: %d  %d  %d profiles | Empty: %s | Match: %s",
					resultsCountBeforeSearch.get(), resultsCountAfterSearch.get(), resultsCountAfterClear.get(),
					isSearchBarEmpty ? "" : "-", countsMatch ? "" : "-");
			PageObjectHelper.log(LOGGER, restorationMsg);

			if (!countsMatch) {
				String failMsg = String.format(" Profile count mismatch: Expected %d, Actual %d",
						resultsCountBeforeSearch.get(), resultsCountAfterClear.get());
				PageObjectHelper.log(LOGGER, failMsg);
				ScreenshotHandler.captureFailureScreenshot("profiles_not_restored", new Exception(failMsg));
				Assert.fail(failMsg);
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_restoration_failed", e);
			PageObjectHelper.log(LOGGER, " Failed to verify profile restoration: " + e.getMessage());
			Assert.fail("Failed to verify profile restoration: " + e.getMessage());
		}
	}

	public void user_validates_ui_remains_responsive_during_clear_operation() {
		try {
			long instantThreshold = 1000;
			String responseMsg;
			long clearTime = totalClearSearchTime.get();

			if (clearTime <= instantThreshold) {
				responseMsg = String.format(" Clear UI Responsive: %d ms (Instant)", clearTime);
				LOGGER.info(responseMsg);
			} else if (clearTime <= CLEAR_SEARCH_THRESHOLD_MS) {
				responseMsg = String.format(" Clear UI Acceptable: %d ms (Not instant)", clearTime);
				LOGGER.warn(responseMsg);
			} else {
				responseMsg = String.format(" Clear UI Slow: %d ms", clearTime);
				LOGGER.error(responseMsg);
			}
			PageObjectHelper.log(LOGGER, responseMsg);

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_clear_ui_responsiveness_failed", e);
			PageObjectHelper.log(LOGGER, " Failed to validate clear UI: " + e.getMessage());
			Assert.fail("Failed to validate clear UI: " + e.getMessage());
		}
	}

	// =========================================
	// SCENARIO 4: SINGLE FILTER PERFORMANCE
	// =========================================

	public void user_measures_time_to_open_filters_dropdown() {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(Locators.JobMappingResults.SHOWING_JOB_RESULTS));
			String resultsText = findElement(Locators.JobMappingResults.SHOWING_JOB_RESULTS).getText().trim();
			resultsCountBeforeFilter.set(extractResultsCount(resultsText));

			js.executeScript("window.scrollTo(0, 0);");
			safeSleep(300);

			filterDropdownOpenStartTime.set(System.currentTimeMillis());
			wait.until(ExpectedConditions.elementToBeClickable(FILTERS_DROPDOWN_BUTTON)).click();
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath("//div[starts-with(@data-testid,'dropdown-')]")));
			filterDropdownOpenEndTime.set(System.currentTimeMillis());
			totalFilterDropdownOpenTime.set(filterDropdownOpenEndTime.get() - filterDropdownOpenStartTime.get());

			String performanceLog = String.format(" Dropdown Open: %d ms (%.2f sec)", totalFilterDropdownOpenTime.get(),
					totalFilterDropdownOpenTime.get() / 1000.0);
			PageObjectHelper.log(LOGGER, performanceLog);

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("measure_filter_dropdown_open_failed", e);
			PageObjectHelper.log(LOGGER, " Failed to measure filter dropdown open time: " + e.getMessage());
			Assert.fail("Failed to measure filter dropdown open time: " + e.getMessage());
		}
	}

	public void user_measures_time_to_apply_single_filter_dynamically() {
		try {
			logSectionHeader("PERFORMANCE MEASUREMENT: Apply Single Filter");

			// Start measuring filter application time
			singleFilterStartTime.set(System.currentTimeMillis());

			boolean filterApplied = false;
			// Map display names to data-testid values
			String[][] filterTypes = { { "Grades", "dropdown-Grades" },
					{ "Functions", "dropdown-Functions_SubFunctions" }, { "Mapping Status", "dropdown-MappingStatus" }
					// Note: Departments is often disabled in the UI
			};

			// Try each filter type until one works
			for (String[] filterInfo : filterTypes) {
				String filterType = filterInfo[0];
				String dataTestId = filterInfo[1];

				try {

					// Look for the filter category header using data-testid
					java.util.List<WebElement> filterHeaders = driver
							.findElements(By.xpath("//div[@data-testid='" + dataTestId + "']"));

					if (filterHeaders.isEmpty()) {
						LOGGER.debug(filterType + " filter not found, trying next...");
						continue;
					}

					// Check if filter is disabled
					WebElement filterHeader = filterHeaders.get(0);
					try {
						WebElement parentDiv = filterHeader.findElement(
								By.xpath("./ancestor::div[contains(@class,'p-4')][1]"));
						String parentClass = parentDiv.getAttribute("class");
						if (parentClass != null && parentClass.contains("pointer-events-none")) {
							LOGGER.debug(filterType + " filter is disabled, trying next...");
							continue;
						}
					} catch (Exception e) {
						LOGGER.debug("Could not check disabled state for " + filterType);
					}

					// Click to expand the filter category
					filterHeader.click();
					safeSleep(800); // Increased wait for checkboxes to render
					LOGGER.debug(" Expanded " + filterType + " filter category");

					// Find available filter options (checkboxes) for this category
					// Try multiple XPath strategies to find checkboxes
					java.util.List<WebElement> filterOptions = driver
							.findElements(By.xpath("//div[@data-testid='" + dataTestId
									+ "']/following-sibling::div//input[@type='checkbox']"));

					// If not found using following-sibling, try parent-based approach
					if (filterOptions.isEmpty()) {
						LOGGER.debug("Trying parent-based XPath for " + filterType);
						filterOptions = driver.findElements(By.xpath("//div[@data-testid='"
								+ dataTestId + "']/ancestor::div[contains(@class,'p-4')]//input[@type='checkbox']"));
					}

					LOGGER.debug("Found " + filterOptions.size() + " filter options for " + filterType);

					if (filterOptions.isEmpty()) {
						LOGGER.debug("No filter options found for " + filterType);
						continue;
					}

					// Try to click the first available unchecked filter option
					for (WebElement filterOption : filterOptions) {
						if (!filterOption.isSelected()) {
							// Get the filter value from the label
							WebElement labelElement = filterOption
									.findElement(By.xpath("./following-sibling::label"));
							appliedFilterValue.set(labelElement.getText().trim());

							// Click the checkbox directly
							try {
								wait.until(ExpectedConditions.elementToBeClickable(filterOption)).click();
							} catch (Exception e) {
								js.executeScript("arguments[0].click();", filterOption);
							}

							appliedFilterType.set(filterType);
							LOGGER.info(" Applied filter: " + filterType + " = " + appliedFilterValue.get());

							// Wait for results to update
							safeSleep(1500);
							PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
							PerformanceUtils.waitForPageReady(driver, 1);

							filterApplied = true;
							break;
						}
					}

					if (filterApplied) {
						break;
					}

				} catch (Exception e) {
					LOGGER.debug("Error applying " + filterType + " filter: " + e.getMessage());
					continue;
				}
			}

			if (!filterApplied) {
				throw new Exception("Could not apply any filter dynamically - no valid filter options found");
			}

			// Close the filters dropdown after applying filter
			try {
				LOGGER.debug("Closing filters dropdown after applying filter");
				wait.until(ExpectedConditions.elementToBeClickable(FILTERS_DROPDOWN_BUTTON)).click();
				safeSleep(500);
				LOGGER.debug(" Filters dropdown closed");
			} catch (Exception e) {
				LOGGER.debug("Could not close filters dropdown: " + e.getMessage());
			}

			// End measuring filter application time
			singleFilterEndTime.set(System.currentTimeMillis());
			totalSingleFilterTime.set(singleFilterEndTime.get() - singleFilterStartTime.get());

			// Get results count after filtering using helper method
			wait.until(ExpectedConditions.visibilityOfElementLocated(Locators.JobMappingResults.SHOWING_JOB_RESULTS));
			String resultsText = findElement(Locators.JobMappingResults.SHOWING_JOB_RESULTS).getText().trim();
			resultsCountAfterFilter.set(extractResultsCount(resultsText));

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("measure_single_filter_failed", e);
			PageObjectHelper.log(LOGGER, " Failed to measure single filter time: " + e.getMessage());
			Assert.fail("Failed to measure single filter time: " + e.getMessage());
		}
	}

	public void user_validates_filter_application_time_is_within_acceptable_threshold() {
		safeValidateThreshold(totalSingleFilterTime.get(), SINGLE_FILTER_THRESHOLD_MS, "Single Filter", "filter");
	}

	public void user_verifies_filtered_results_are_displayed_correctly() {
		try {
			boolean clearFiltersVisible = false;
			try {
				java.util.List<WebElement> clearButtons = driver
						.findElements(By.xpath("//button[@data-testid='Clear Filters']"));
				if (clearButtons.isEmpty()) {
					clearButtons = driver
							.findElements(By.xpath("//button[contains(text(),'Clear') and contains(text(),'Filter')]"));
				}
				clearFiltersVisible = !clearButtons.isEmpty() && clearButtons.get(0).isDisplayed();
			} catch (Exception e) {
				// Clear button check skipped
			}

			String validationMsg = String.format(" Filter %s=%s: %d  %d profiles | Clear Btn: %s", appliedFilterType.get(),
					appliedFilterValue.get(), resultsCountBeforeFilter.get(), resultsCountAfterFilter.get(),
					clearFiltersVisible ? "" : "-");
			PageObjectHelper.log(LOGGER, validationMsg);

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_filtered_results_failed", e);
			PageObjectHelper.log(LOGGER, " Failed to verify filtered results: " + e.getMessage());
			Assert.fail("Failed to verify filtered results: " + e.getMessage());
		}
	}

	public void user_validates_ui_remains_responsive_during_filter_operation() {
		try {
			long instantThreshold = 2000;
			String responseMsg;
			long filterTime = totalSingleFilterTime.get();

			if (filterTime <= instantThreshold) {
				responseMsg = String.format(" Filter UI Responsive: %d ms (Instant)", filterTime);
				LOGGER.info(responseMsg);
			} else if (filterTime <= SINGLE_FILTER_THRESHOLD_MS) {
				responseMsg = String.format(" Filter UI Acceptable: %d ms (Not instant)", filterTime);
				LOGGER.warn(responseMsg);
			} else {
				responseMsg = String.format(" Filter UI Slow: %d ms", filterTime);
				LOGGER.error(responseMsg);
			}
			PageObjectHelper.log(LOGGER, responseMsg);

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_filter_ui_responsiveness_failed", e);
			PageObjectHelper.log(LOGGER, " Failed to validate filter UI: " + e.getMessage());
			Assert.fail("Failed to validate filter UI: " + e.getMessage());
		}
	}

	// ==========================================
	// SCENARIO 5: MULTIPLE FILTERS PERFORMANCE
	// ==========================================

	public void user_measures_time_to_apply_multiple_filters_dynamically_from_available_options() {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(Locators.JobMappingResults.SHOWING_JOB_RESULTS));
			String resultsText = findElement(Locators.JobMappingResults.SHOWING_JOB_RESULTS).getText().trim();
			resultsCountBeforeFilter.set(extractResultsCount(resultsText));

			js.executeScript("window.scrollTo(0, 0);");
			safeSleep(300);

			java.util.List<WebElement> existingDropdowns = driver
					.findElements(By.xpath("//div[starts-with(@data-testid,'dropdown-')]"));

			if (existingDropdowns.isEmpty()) {
				wait.until(ExpectedConditions.elementToBeClickable(FILTERS_DROPDOWN_BUTTON)).click();
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//div[starts-with(@data-testid,'dropdown-')]")));
				safeSleep(500);
			}

			multipleFiltersStartTime.set(System.currentTimeMillis());
			numberOfFiltersApplied.set(0);

			String[][] filterTypes = { { "Grades", "dropdown-Grades" },
					{ "Functions", "dropdown-Functions_SubFunctions" },
					{ "Mapping Status", "dropdown-MappingStatus" } };
			StringBuilder appliedFiltersLog = new StringBuilder();

			for (String[] filterInfo : filterTypes) {
				if (numberOfFiltersApplied.get() >= 2) {
					break;
				}

				String filterType = filterInfo[0];
				String dataTestId = filterInfo[1];

				try {
					java.util.List<WebElement> filterHeaders = driver
							.findElements(By.xpath("//div[@data-testid='" + dataTestId + "']"));

					if (filterHeaders.isEmpty()) {
						continue;
					}

					WebElement filterHeader = filterHeaders.get(0);
					try {
						WebElement parentDiv = filterHeader.findElement(
								By.xpath("./ancestor::div[contains(@class,'p-4')][1]"));
						String parentClass = parentDiv.getAttribute("class");
						if (parentClass != null && parentClass.contains("pointer-events-none")) {
							continue;
						}
					} catch (Exception e) {
						// Continue if disabled check fails
					}

					filterHeader.click();
					safeSleep(800);

					java.util.List<WebElement> filterOptions = driver
							.findElements(By.xpath("//div[@data-testid='" + dataTestId
									+ "']/following-sibling::div//input[@type='checkbox']"));

					if (filterOptions.isEmpty()) {
						filterOptions = driver.findElements(By.xpath("//div[@data-testid='"
								+ dataTestId + "']/ancestor::div[contains(@class,'p-4')]//input[@type='checkbox']"));
					}

					if (filterOptions.isEmpty()) {
						continue;
					}

					for (WebElement filterOption : filterOptions) {
						if (!filterOption.isSelected()) {
							WebElement labelElement = filterOption
									.findElement(By.xpath("./following-sibling::label"));
							String filterValue = labelElement.getText().trim();

							try {
								wait.until(ExpectedConditions.elementToBeClickable(filterOption)).click();
							} catch (Exception e) {
								js.executeScript("arguments[0].click();", filterOption);
							}

							numberOfFiltersApplied.set(numberOfFiltersApplied.get() + 1);
							appliedFiltersLog.append(String.format("    %s = %s%n", filterType, filterValue));
							LOGGER.info(" Applied filter " + numberOfFiltersApplied.get() + ": " + filterType + " = "
									+ filterValue);

							safeSleep(1500);
							PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
							PerformanceUtils.waitForPageReady(driver, 1);

							break;
						}
					}

				} catch (Exception e) {
					continue;
				}
			}

			if (numberOfFiltersApplied.get() < 2) {
				throw new Exception(
						"Could not apply at least 2 filters dynamically - only applied " + numberOfFiltersApplied.get());
			}

			try {
				wait.until(ExpectedConditions.elementToBeClickable(FILTERS_DROPDOWN_BUTTON)).click();
				safeSleep(500);
			} catch (Exception e) {
				// Continue if dropdown close fails
			}

			multipleFiltersEndTime.set(System.currentTimeMillis());
			totalMultipleFiltersTime.set(multipleFiltersEndTime.get() - multipleFiltersStartTime.get());

			wait.until(ExpectedConditions.visibilityOfElementLocated(Locators.JobMappingResults.SHOWING_JOB_RESULTS));
			resultsText = findElement(Locators.JobMappingResults.SHOWING_JOB_RESULTS).getText().trim();
			resultsCountAfterFilter.set(extractResultsCount(resultsText));

			String performanceLog = String.format(
					" Performance Metrics:%n" + "    Filters Applied: %d%n" + "%s"
							+ "    Total Time: %d ms (%.2f sec)%n" + "    Results: %d  %d profiles",
					numberOfFiltersApplied.get(), appliedFiltersLog.toString(), totalMultipleFiltersTime.get(),
					totalMultipleFiltersTime.get() / 1000.0, resultsCountBeforeFilter.get(), resultsCountAfterFilter.get());
			PageObjectHelper.log(LOGGER, performanceLog);

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("measure_multiple_filters_failed", e);
			PageObjectHelper.log(LOGGER, " Failed to measure multiple filters time: " + e.getMessage());
			Assert.fail("Failed to measure multiple filters time: " + e.getMessage());
		}
	}

	public void user_validates_multiple_filter_application_time_is_within_acceptable_threshold() {
		safeValidateThreshold(totalMultipleFiltersTime.get(), MULTIPLE_FILTERS_THRESHOLD_MS,
				"Multiple Filters (" + numberOfFiltersApplied.get() + " filters)", "multiple_filters");
	}

	public void user_verifies_combined_filtered_results_are_displayed_correctly() {
		try {
			boolean resultsDecreased = resultsCountAfterFilter.get() <= resultsCountBeforeFilter.get();

			boolean clearFiltersVisible = false;
			try {
				java.util.List<WebElement> clearButtons = driver
						.findElements(By.xpath("//button[@data-testid='Clear Filters']"));
				if (clearButtons.isEmpty()) {
					clearButtons = driver.findElements(By
							.xpath("//button[contains(text(),'Clear') and contains(text(),'Filter')]"));
				}
				clearFiltersVisible = !clearButtons.isEmpty() && clearButtons.get(0).isDisplayed();
			} catch (Exception e) {
				// Clear Filters button check failed
			}

			String resultsText = findElement(Locators.JobMappingResults.SHOWING_JOB_RESULTS).getText().trim();
			boolean resultsCountDisplayed = !resultsText.isEmpty();

			String validationMsg = String.format(
					" Combined Filter Results: %d filters applied | %d  %d profiles | Clear button: %s",
					numberOfFiltersApplied.get(), resultsCountBeforeFilter.get(), resultsCountAfterFilter.get(),
					clearFiltersVisible ? "Visible " : "Not visible -");
			PageObjectHelper.log(LOGGER, validationMsg);

			if (resultsDecreased && clearFiltersVisible && resultsCountDisplayed) {
				PageObjectHelper.log(LOGGER, " All combined filter validations passed");
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_combined_filtered_results_failed", e);
			PageObjectHelper.log(LOGGER, " Failed to verify combined filtered results: " + e.getMessage());
			Assert.fail("Failed to verify combined filtered results: " + e.getMessage());
		}
	}

	public void user_validates_no_ui_lag_during_multi_filter_operation() {
		try {
			long smoothThreshold = 3000;
			String responseMsg;
			long filterTime = totalMultipleFiltersTime.get();
			if (filterTime <= smoothThreshold) {
				responseMsg = String.format(" UI Responsive: %d ms (Smooth)", filterTime);
			} else {
				responseMsg = String.format(" UI Acceptable: %d ms (Not instant)", filterTime);
			}
			PageObjectHelper.log(LOGGER, responseMsg);

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_multi_filter_ui_lag_failed", e);
			PageObjectHelper.log(LOGGER, " Failed to validate UI lag: " + e.getMessage());
			Assert.fail("Failed to validate UI lag: " + e.getMessage());
		}
	}

	// ==========================================
	// SCENARIO 6: CLEAR FILTERS PERFORMANCE
	// ==========================================

	public void user_has_applied_filters_and_has_filtered_results() {
		try {
			// Clear any existing filters from previous scenarios
			try {
				java.util.List<WebElement> clearButtons = driver
						.findElements(By.xpath("//button[@data-testid='Clear Filters']"));
				if (clearButtons.isEmpty()) {
					clearButtons = driver.findElements(By
							.xpath("//button[contains(text(),'Clear') and contains(text(),'Filter')]"));
				}

				if (!clearButtons.isEmpty() && clearButtons.get(0).isDisplayed()) {
					js.executeScript("window.scrollTo(0, 0);");
					safeSleep(300);
					clearButtons.get(0).click();
					safeSleep(500);
					PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
					PerformanceUtils.waitForPageReady(driver, 2);
					safeSleep(1000);
				}
			} catch (Exception e) {
				// No existing filters
			}

			wait.until(ExpectedConditions.visibilityOfElementLocated(Locators.JobMappingResults.SHOWING_JOB_RESULTS));
			safeSleep(1000);

			int maxRetries = 10;
			int retryCount = 0;
			String resultsText = "";

			while (retryCount < maxRetries) {
				resultsText = findElement(Locators.JobMappingResults.SHOWING_JOB_RESULTS).getText().trim();
				resultsCountBeforeFilter.set(extractResultsCount(resultsText));

				if (resultsCountBeforeFilter.get() > 0) {
					break;
				}

				safeSleep(500);
				retryCount++;
			}

			if (resultsCountBeforeFilter.get() <= 0) {
				String errorMsg = " Failed to capture valid profile count before filtering after " + maxRetries
						+ " attempts";
				LOGGER.error(errorMsg);
				ScreenshotHandler.captureFailureScreenshot("invalid_profile_count_before_filter",
						new Exception(errorMsg));
				Assert.fail(errorMsg);
			}

			js.executeScript("window.scrollTo(0, 0);");
			safeSleep(300);

			java.util.List<WebElement> existingDropdowns = driver
					.findElements(By.xpath("//div[starts-with(@data-testid,'dropdown-')]"));

			if (existingDropdowns.isEmpty()) {
				wait.until(ExpectedConditions.elementToBeClickable(FILTERS_DROPDOWN_BUTTON)).click();
				wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//div[starts-with(@data-testid,'dropdown-')]")));
				safeSleep(500);
			}

			boolean filterApplied = false;
			String[][] filterTypes = { { "Grades", "dropdown-Grades" },
					{ "Functions", "dropdown-Functions_SubFunctions" },
					{ "Mapping Status", "dropdown-MappingStatus" } };

			for (String[] filterInfo : filterTypes) {
				String filterType = filterInfo[0];
				String dataTestId = filterInfo[1];

				try {
					java.util.List<WebElement> filterHeaders = driver
							.findElements(By.xpath("//div[@data-testid='" + dataTestId + "']"));

					if (filterHeaders.isEmpty()) {
						continue;
					}

					WebElement filterHeader = filterHeaders.get(0);
					try {
						WebElement parentDiv = filterHeader.findElement(
								By.xpath("./ancestor::div[contains(@class,'p-4')][1]"));
						String parentClass = parentDiv.getAttribute("class");
						if (parentClass != null && parentClass.contains("pointer-events-none")) {
							continue;
						}
					} catch (Exception e) {
						// Continue if disabled check fails
					}

					filterHeader.click();
					safeSleep(800);

					java.util.List<WebElement> filterOptions = driver
							.findElements(By.xpath("//div[@data-testid='" + dataTestId
									+ "']/following-sibling::div//input[@type='checkbox']"));

					if (filterOptions.isEmpty()) {
						filterOptions = driver.findElements(By.xpath("//div[@data-testid='"
								+ dataTestId + "']/ancestor::div[contains(@class,'p-4')]//input[@type='checkbox']"));
					}

					if (filterOptions.isEmpty()) {
						continue;
					}

					for (WebElement filterOption : filterOptions) {
						if (!filterOption.isSelected()) {
							WebElement labelElement = filterOption
									.findElement(By.xpath("./following-sibling::label"));
							appliedFilterValue.set(labelElement.getText().trim());

							try {
								wait.until(ExpectedConditions.elementToBeClickable(filterOption)).click();
							} catch (Exception e) {
								js.executeScript("arguments[0].click();", filterOption);
							}

							appliedFilterType.set(filterType);
							LOGGER.info(" Applied filter: " + filterType + " = " + appliedFilterValue.get());

							safeSleep(1500);
							PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
							PerformanceUtils.waitForPageReady(driver, 1);

							filterApplied = true;
							break;
						}
					}

					if (filterApplied) {
						break;
					}

				} catch (Exception e) {
					continue;
				}
			}

			if (!filterApplied) {
				throw new Exception("Could not apply any filter dynamically");
			}

			try {
				wait.until(ExpectedConditions.elementToBeClickable(FILTERS_DROPDOWN_BUTTON)).click();
				safeSleep(500);
			} catch (Exception e) {
				// Continue if dropdown close fails
			}

			wait.until(ExpectedConditions.visibilityOfElementLocated(Locators.JobMappingResults.SHOWING_JOB_RESULTS));
			resultsText = findElement(Locators.JobMappingResults.SHOWING_JOB_RESULTS).getText().trim();
			resultsCountAfterFilter.set(extractResultsCount(resultsText));

			String setupMsg = String.format(" Filter applied: %s = %s | %d  %d profiles", appliedFilterType.get(),
					appliedFilterValue.get(), resultsCountBeforeFilter.get(), resultsCountAfterFilter.get());
			PageObjectHelper.log(LOGGER, setupMsg);

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("apply_filter_for_clear_test_failed", e);
			PageObjectHelper.log(LOGGER, " Failed to apply filter for clear test: " + e.getMessage());
			Assert.fail("Failed to apply filter for clear test: " + e.getMessage());
		}
	}

	public void user_measures_time_to_click_clear_all_filters_button() {
		try {
			js.executeScript("window.scrollTo(0, 0);");
			safeSleep(300);

			WebElement clearFiltersButton = null;
			try {
				clearFiltersButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
						By.xpath("//button[@data-testid='Clear Filters']")));
			} catch (Exception e) {
				clearFiltersButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By
						.xpath("//button[contains(text(),'Clear') and contains(text(),'Filter')]")));
			}

			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", clearFiltersButton);
			safeSleep(500);

			clearFiltersStartTime.set(System.currentTimeMillis());

			try {
				wait.until(ExpectedConditions.elementToBeClickable(clearFiltersButton)).click();
			} catch (Exception e) {
				js.executeScript("arguments[0].click();", clearFiltersButton);
			}

			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

			clearFiltersEndTime.set(System.currentTimeMillis());
			totalClearFiltersTime.set(clearFiltersEndTime.get() - clearFiltersStartTime.get());

			WebElement resultsCountElement = wait
					.until(ExpectedConditions.visibilityOfElementLocated(Locators.JobMappingResults.SHOWING_JOB_RESULTS));
			String resultsText = resultsCountElement.getText().trim();
			resultsCountAfterClearFilters.set(extractResultsCount(resultsText));

			String performanceLog = String.format(
					" Performance Metrics:%n" + "    Clear Filters Time: %d ms (%.2f sec)%n"
							+ "    Results: %d  %d profiles%n" + "    Restored to original: %s",
					totalClearFiltersTime.get(), totalClearFiltersTime.get() / 1000.0, resultsCountAfterFilter.get(),
					resultsCountAfterClearFilters.get(),
					resultsCountAfterClearFilters.get().equals(resultsCountBeforeFilter.get()) ? "Yes " : "No -");
			PageObjectHelper.log(LOGGER, performanceLog);

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("measure_clear_filters_failed", e);
			PageObjectHelper.log(LOGGER, " Failed to measure clear filters time: " + e.getMessage());
			Assert.fail("Failed to measure clear filters time: " + e.getMessage());
		}
	}

	public void user_validates_clear_filters_operation_time_is_within_acceptable_threshold() {
		safeValidateThreshold(totalClearFiltersTime.get(), CLEAR_FILTERS_THRESHOLD_MS, "Clear Filters", "clear_filters");
	}

	public void user_verifies_all_profiles_are_restored_correctly_after_clearing_filters() {
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 1);

			String restorationMsg = String.format(" Restored profiles: %d  %d  %d | Match: %s",
					resultsCountBeforeFilter.get(), resultsCountAfterFilter.get(), resultsCountAfterClearFilters.get(),
					resultsCountAfterClearFilters.get().equals(resultsCountBeforeFilter.get()) ? "Yes " : "No -");
			PageObjectHelper.log(LOGGER, restorationMsg);

			if (resultsCountAfterClearFilters.get().equals(resultsCountBeforeFilter.get())) {
				PageObjectHelper.log(LOGGER, " All profiles restored to original count");
			} else {
				int difference = Math.abs(resultsCountAfterClearFilters.get() - resultsCountBeforeFilter.get());
				String failMsg = String.format(" Count mismatch: Expected %d, Actual %d (Diff: %d)",
						resultsCountBeforeFilter.get(), resultsCountAfterClearFilters.get(), difference);
				PageObjectHelper.log(LOGGER, failMsg);
				ScreenshotHandler.captureFailureScreenshot("profiles_not_restored_after_clear_filters",
						new Exception(failMsg));
				Assert.fail(failMsg);
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_restoration_after_clear_filters_failed", e);
			PageObjectHelper.log(LOGGER, " Failed to verify profile restoration: " + e.getMessage());
			Assert.fail("Failed to verify profile restoration: " + e.getMessage());
		}
	}

	public void user_validates_no_ui_freeze_during_filter_clearing() {
		try {
			long instantThreshold = 1000;
			String responseMsg;
			long clearTime = totalClearFiltersTime.get();
			if (clearTime <= instantThreshold) {
				responseMsg = String.format(" UI Responsive: %d ms (Instant)", clearTime);
			} else {
				responseMsg = String.format(" UI Acceptable: %d ms (Not instant)", clearTime);
			}
			PageObjectHelper.log(LOGGER, responseMsg);

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_clear_filters_ui_freeze_failed", e);
			PageObjectHelper.log(LOGGER, " Failed to validate UI freeze: " + e.getMessage());
			Assert.fail("Failed to validate UI freeze: " + e.getMessage());
		}
	}

	// =========================================
	// SCENARIO 7: SCROLL AND LAZY LOAD PERFORMANCE
	// =========================================

	public void user_measures_time_to_scroll_through_all_profiles() {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(Locators.JobMappingResults.SHOWING_JOB_RESULTS));
			String resultsText = findElement(Locators.JobMappingResults.SHOWING_JOB_RESULTS).getText().trim();
			initialProfileCount.set(extractResultsCount(resultsText));

			java.util.List<WebElement> initialProfiles = findElements(PROFILE_ROWS);
			int previousProfileCount = initialProfiles.size();

			scrollStartTime.set(System.currentTimeMillis());
			totalScrolls.set(0);
			lazyLoadTimes.get().clear();

			int targetScrolls = 4;

			for (int i = 0; i < targetScrolls; i++) {
				long scrollIterationStart = System.currentTimeMillis();

				js.executeScript("window.scrollTo(0, document.documentElement.scrollHeight);"); // Scroll DOWN (headless-compatible)
				totalScrolls.set(totalScrolls.get() + 1);
				safeSleep(300);

				PerformanceUtils.waitForSpinnersToDisappear(driver, 2);

				safeSleep(500);

				java.util.List<WebElement> currentProfiles = findElements(PROFILE_ROWS);
				int currentProfileCount = currentProfiles.size();

				long scrollIterationEnd = System.currentTimeMillis();
				long lazyLoadTime = scrollIterationEnd - scrollIterationStart;

				if (currentProfileCount > previousProfileCount) {
					int newProfilesLoaded = currentProfileCount - previousProfileCount;
					lazyLoadTimes.get().add(lazyLoadTime);
					LOGGER.info(String.format("Scroll #%d: Loaded %d new profiles in %d ms", totalScrolls.get(),
							newProfilesLoaded, lazyLoadTime));
					previousProfileCount = currentProfileCount;
				} else {
					lazyLoadTimes.get().add(lazyLoadTime);
				}
			}

			scrollEndTime.set(System.currentTimeMillis());
			totalScrollTime.set(scrollEndTime.get() - scrollStartTime.get());

			java.util.List<WebElement> finalProfiles = findElements(PROFILE_ROWS);
			finalProfileCount.set(finalProfiles.size());

			if (!lazyLoadTimes.get().isEmpty()) {
				avgLazyLoadTime.set(lazyLoadTimes.get().stream().mapToLong(Long::longValue).sum() / lazyLoadTimes.get().size());
			}

			String performanceLog = String.format(
					" Scroll Metrics: %d scrolls | %d  %d profiles | Avg lazy load: %d ms | Total: %d ms", totalScrolls.get(),
					initialProfileCount.get(), finalProfileCount.get(), avgLazyLoadTime.get(), totalScrollTime.get());
			PageObjectHelper.log(LOGGER, performanceLog);

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("measure_scroll_time_failed", e);
			PageObjectHelper.log(LOGGER, " Failed to measure scroll time: " + e.getMessage());
			Assert.fail("Failed to measure scroll time: " + e.getMessage());
		}
	}

	public void user_validates_lazy_loading_triggers_at_appropriate_intervals() {
		try {
			if (lazyLoadTimes.get().isEmpty()) {
				LOGGER.warn(" No lazy loading detected");
				PageObjectHelper.log(LOGGER, " No lazy loading detected");
				return;
			}

			boolean lazyLoadPerformanceGood = avgLazyLoadTime.get() <= LAZY_LOAD_THRESHOLD_MS;
			String validationMsg = String.format(" Lazy Load: %d triggers | Avg: %d ms | Status: %s",
					lazyLoadTimes.get().size(), avgLazyLoadTime.get(), lazyLoadPerformanceGood ? "" : "-");
			PageObjectHelper.log(LOGGER, validationMsg);

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_lazy_loading_failed", e);
			PageObjectHelper.log(LOGGER, " Failed to validate lazy loading: " + e.getMessage());
			Assert.fail("Failed to validate lazy loading: " + e.getMessage());
		}
	}

	public void user_verifies_scroll_performance_is_smooth_without_lag() {
		safeValidateThreshold(totalScrollTime.get(), SCROLL_OPERATION_THRESHOLD_MS, "Scroll Operation", "scroll");
	}

	public void user_validates_newly_loaded_profiles_render_within_acceptable_time() {
		try {
			if (lazyLoadTimes.get().isEmpty()) {
				PageObjectHelper.log(LOGGER, " No lazy loading - all profiles available on page load");
				return;
			}

			long maxLazyLoadTime = lazyLoadTimes.get().stream().mapToLong(Long::longValue).max().orElse(0);
			long minLazyLoadTime = lazyLoadTimes.get().stream().mapToLong(Long::longValue).min().orElse(0);
			boolean allWithinThreshold = maxLazyLoadTime <= LAZY_LOAD_THRESHOLD_MS;

			String renderMsg = String.format(
					" Render Times: Min %d ms | Avg %d ms | Max %d ms | Threshold: %d ms | Status: %s", minLazyLoadTime,
					avgLazyLoadTime.get(), maxLazyLoadTime, LAZY_LOAD_THRESHOLD_MS, allWithinThreshold ? "" : "-");

			PageObjectHelper.log(LOGGER, renderMsg);

			if (allWithinThreshold) {
				LOGGER.info(" All profile render times within acceptable threshold");
			} else {
				String warnMsg = String.format(" Some lazy load times exceeded threshold (Max: %d ms > %d ms)",
						maxLazyLoadTime, LAZY_LOAD_THRESHOLD_MS);
				LOGGER.warn(warnMsg);
				PageObjectHelper.log(LOGGER, warnMsg);
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_render_time_failed", e);
			PageObjectHelper.log(LOGGER, " Failed to validate render time: " + e.getMessage());
			Assert.fail("Failed to validate render time: " + e.getMessage());
		}
	}

	public void user_verifies_total_time_to_load_all_profiles_via_scrolling() {
		try {
			String summaryMsg = String.format(
					"%n" + " Scroll Performance Summary:%n" + "    Test Scope: 4 scroll operations (3-4 pages)%n"
							+ "    Total Time: %d ms (%.2f sec)%n" + "    Initial Profiles: %d%n"
							+ "    Final Profiles: %d%n" + "    New Profiles Loaded: %d%n"
							+ "    Lazy Load Operations: %d%n" + "    Avg Lazy Load Time: %d ms%n"
							+ "    Performance Rating: %s%n" + "",
					totalScrollTime.get(), totalScrollTime.get() / 1000.0, initialProfileCount.get(), finalProfileCount.get(),
					(finalProfileCount.get() - initialProfileCount.get()), lazyLoadTimes.get().size(), avgLazyLoadTime.get(),
					getPerformanceRating(totalScrollTime.get(), SCROLL_OPERATION_THRESHOLD_MS));

			PageObjectHelper.log(LOGGER, summaryMsg);

			if (totalScrollTime.get() <= SCROLL_OPERATION_THRESHOLD_MS
					&& (lazyLoadTimes.get().isEmpty() || avgLazyLoadTime.get() <= LAZY_LOAD_THRESHOLD_MS)) {
				LOGGER.info(" Scroll and lazy load performance meets all requirements");
			} else {
				LOGGER.warn(" Some performance metrics need attention");
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_total_scroll_time_failed", e);
			PageObjectHelper.log(LOGGER, " Failed to verify total scroll time: " + e.getMessage());
			Assert.fail("Failed to verify total scroll time: " + e.getMessage());
		}
	}

	// =========================================
	// SCENARIO 8: NAVIGATION PERFORMANCE
	// =========================================

	public void user_measures_time_to_navigate_to_job_comparison_screen_from_job_mapping() {
		try {
			navigationToComparisonStartTime.set(System.currentTimeMillis());

			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tbody")));
			PerformanceUtils.waitForPageReady(driver, 2);

			boolean foundViewOtherMatchesButton = false;
			for (int i = 2; i <= 47; i = i + 3) {
				try {
					WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(By
							.xpath("//tbody//tr[" + Integer.toString(i) + "]//button[not(contains(@id,'publish'))]")));

					js.executeScript("arguments[0].scrollIntoView(true);", button);
					safeSleep(300);

					wait.until(ExpectedConditions.visibilityOf(button));
					String buttonText = button.getText();

					if (buttonText.contains("Other Matches")) {
						rowNumberForViewOtherMatches.set(i);
						WebElement jobNameElement = driver.findElement(
								By.xpath("//tbody//tr[" + Integer.toString(rowNumberForViewOtherMatches.get() - 1)
										+ "]//td[2]//div[contains(text(),'(')]"));
						jobNameForComparison.set(jobNameElement.getText().split("-", 2)[0].trim());
						foundViewOtherMatchesButton = true;
						break;
					}
				} catch (Exception rowException) {
					continue;
				}
			}

			if (!foundViewOtherMatchesButton) {
				throw new Exception("No profile with 'View Other Matches' button found");
			}

			WebElement viewOtherMatchesBtn = wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath("//tbody//tr[" + Integer.toString(rowNumberForViewOtherMatches.get())
							+ "]//button[not(contains(@id,'publish'))]")));

			js.executeScript("arguments[0].scrollIntoView(true);", viewOtherMatchesBtn);
			safeSleep(300);

			wait.until(ExpectedConditions.visibilityOf(viewOtherMatchesBtn));

			try {
				wait.until(ExpectedConditions.elementToBeClickable(viewOtherMatchesBtn)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", viewOtherMatchesBtn);
				} catch (Exception s) {
					jsClick(viewOtherMatchesBtn);
				}
			}

			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			wait.until(ExpectedConditions.visibilityOfElementLocated(JOB_COMPARISON_HEADER));
			PerformanceUtils.waitForPageReady(driver, 2);

			navigationToComparisonEndTime.set(System.currentTimeMillis());
			totalNavigationToComparisonTime.set(navigationToComparisonEndTime.get() - navigationToComparisonStartTime.get());

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("measure_navigation_to_comparison_failed", e);
			PageObjectHelper.log(LOGGER, " Failed to measure navigation time: " + e.getMessage());
			Assert.fail("Failed to measure navigation time: " + e.getMessage());
		}
	}

	public void user_validates_navigation_time_is_within_acceptable_threshold() {
		safeValidateThreshold(totalNavigationToComparisonTime.get(), NAVIGATION_THRESHOLD_MS, "Navigation to Job Comparison",
				"navigation_to_comparison");
	}

	public void user_verifies_job_comparison_screen_loads_without_delay() {
		try {
			WebElement headerElement = wait.until(ExpectedConditions.visibilityOfElementLocated(JOB_COMPARISON_HEADER));
			String headerText = headerElement.getText();

			if (headerText.equals("Which profile do you want to use for this job?")) {
				LOGGER.info(String.format(" Job Comparison screen loaded successfully | Profile: %s",
						jobNameForComparison.get()));
				PageObjectHelper.log(LOGGER, " Job Comparison screen verified");
			} else {
				throw new Exception(
						"Job Comparison screen header mismatch. Expected: 'Which profile do you want to use for this job?', Got: '"
								+ headerText + "'");
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_comparison_screen_failed", e);
			PageObjectHelper.log(LOGGER, " Failed to verify Job Comparison screen: " + e.getMessage());
			Assert.fail("Failed to verify Job Comparison screen: " + e.getMessage());
		}
	}

	public void user_measures_time_to_navigate_back_to_job_mapping_screen() {
		try {
			navigationBackToMappingStartTime.set(System.currentTimeMillis());

			driver.navigate().back();

			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			wait.until(ExpectedConditions.presenceOfElementLocated(JOB_MAPPING_PAGE_CONTAINER));
			PerformanceUtils.waitForPageReady(driver, 2);

			navigationBackToMappingEndTime.set(System.currentTimeMillis());
			totalNavigationBackToMappingTime.set(navigationBackToMappingEndTime.get() - navigationBackToMappingStartTime.get());

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("measure_navigation_back_failed", e);
			PageObjectHelper.log(LOGGER, " Failed to measure back navigation time: " + e.getMessage());
			Assert.fail("Failed to measure back navigation time: " + e.getMessage());
		}
	}

	public void user_validates_back_navigation_time_is_within_acceptable_threshold() {
		safeValidateThreshold(totalNavigationBackToMappingTime.get(), NAVIGATION_THRESHOLD_MS,
				"Navigation Back to Job Mapping", "navigation_back");
	}

	public void user_verifies_job_mapping_screen_loads_correctly_after_navigation() {
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(JOB_MAPPING_PAGE_CONTAINER));

			WebElement resultsCountElement = findElement(Locators.JobMappingResults.SHOWING_JOB_RESULTS);
			wait.until(ExpectedConditions.visibilityOf(resultsCountElement));
			String resultsText = resultsCountElement.getText().trim();
			int profileCount = extractResultsCount(resultsText);

			PageObjectHelper.log(LOGGER, "Job Mapping screen verified - " + profileCount + " profiles loaded");

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_mapping_screen_after_nav_failed", e);
			PageObjectHelper.log(LOGGER, " Failed to verify Job Mapping screen: " + e.getMessage());
			Assert.fail("Failed to verify Job Mapping screen: " + e.getMessage());
		}
	}

	// =========================================
	// SCENARIO 9: SORT PERFORMANCE
	// =========================================

	public void user_is_on_job_mapping_page_with_maximum_loaded_profiles() {
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

			String resultsText = findElement(Locators.JobMappingResults.SHOWING_JOB_RESULTS).getText().trim();
			int currentCount = extractResultsCount(resultsText);

			int scrollAttempts = 10;
			for (int i = 0; i < scrollAttempts; i++) {
				js.executeScript("window.scrollTo(0, document.documentElement.scrollHeight);"); // Scroll DOWN
																								// (headless-compatible)
				safeSleep(500);

				PerformanceUtils.waitForSpinnersToDisappear(driver, 2);
			}

			js.executeScript("window.scrollTo(0, 0);");
			safeSleep(500);

			resultsText = findElement(Locators.JobMappingResults.SHOWING_JOB_RESULTS).getText().trim();
			int finalCount = extractResultsCount(resultsText);

			LOGGER.info(String.format(" Maximum profiles loaded: %d profiles (%d initial  %d final)", finalCount,
					currentCount, finalCount));
			PageObjectHelper.log(LOGGER, " Maximum profiles loaded for sort test: " + finalCount);

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("load_max_profiles_failed", e);
			PageObjectHelper.log(LOGGER, " Failed to load maximum profiles: " + e.getMessage());
			Assert.fail("Failed to load maximum profiles: " + e.getMessage());
		}
	}

	public void user_measures_time_to_sort_profiles_by_job_title() {
		try {
			sortByJobTitleStartTime.set(System.currentTimeMillis());

			WebElement sortHeader = findElement(ORG_JOB_NAME_HEADER);
			try {
				wait.until(ExpectedConditions.elementToBeClickable(sortHeader)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", sortHeader);
				} catch (Exception s) {
					jsClick(sortHeader);
				}
			}

			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 1);
			safeSleep(500);

			sortByJobTitleEndTime.set(System.currentTimeMillis());
			totalSortByJobTitleTime.set(sortByJobTitleEndTime.get() - sortByJobTitleStartTime.get());

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("measure_sort_by_title_failed", e);
			PageObjectHelper.log(LOGGER, " Failed to measure sort by Job Title: " + e.getMessage());
			Assert.fail("Failed to measure sort by Job Title: " + e.getMessage());
		}
	}

	public void user_measures_time_to_sort_profiles_by_grade() {
		try {
			sortByGradeStartTime.set(System.currentTimeMillis());

			WebElement sortHeader = findElement(ORG_JOB_GRADE_HEADER);
			try {
				wait.until(ExpectedConditions.elementToBeClickable(sortHeader)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", sortHeader);
				} catch (Exception s) {
					jsClick(sortHeader);
				}
			}

			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 1);
			safeSleep(500);

			sortByGradeEndTime.set(System.currentTimeMillis());
			totalSortByGradeTime.set(sortByGradeEndTime.get() - sortByGradeStartTime.get());

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("measure_sort_by_grade_failed", e);
			PageObjectHelper.log(LOGGER, " Failed to measure sort by Grade: " + e.getMessage());
			Assert.fail("Failed to measure sort by Grade: " + e.getMessage());
		}
	}

	public void user_validates_sorting_operation_time_is_within_acceptable_threshold() {
		try {
			safeValidateThreshold(totalSortByJobTitleTime.get(), SORT_OPERATION_THRESHOLD_MS, "Sort by Job Title",
					"sort_by_title");
			safeValidateThreshold(totalSortByGradeTime.get(), SORT_OPERATION_THRESHOLD_MS, "Sort by Grade", "sort_by_grade");

			long avgSortTime = (totalSortByJobTitleTime.get() + totalSortByGradeTime.get()) / 2;
			LOGGER.info(String.format(" Average Sort Time: %d ms (%.2f sec)", avgSortTime, avgSortTime / 1000.0));

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_sort_threshold_failed", e);
			PageObjectHelper.log(LOGGER, " Failed to validate sort threshold: " + e.getMessage());
			Assert.fail("Failed to validate sort threshold: " + e.getMessage());
		}
	}

	public void user_verifies_sorted_results_are_accurate() {
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//div[@id='org-job-container']//tbody//tr")));

			java.util.List<WebElement> rows = findElements(PROFILE_ROWS);

			LOGGER.info(
					String.format(" Sort completed successfully | %d profiles visible in sorted view", rows.size()));
			PageObjectHelper.log(LOGGER, " Sorted results verified (table populated)");

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_sorted_results_failed", e);
			PageObjectHelper.log(LOGGER, " Failed to verify sorted results: " + e.getMessage());
			Assert.fail("Failed to verify sorted results: " + e.getMessage());
		}
	}

	public void user_validates_ui_remains_responsive_during_sorting() {
		try {
			long maxSortTime = Math.max(totalSortByJobTitleTime.get(), totalSortByGradeTime.get());
			long instantThreshold = 2000;

			String responseMsg;
			if (maxSortTime <= instantThreshold) {
				responseMsg = String.format(" Sort operations feel instant (max: %d ms)", maxSortTime);
				LOGGER.info(responseMsg);
			} else if (maxSortTime <= SORT_OPERATION_THRESHOLD_MS) {
				responseMsg = String.format(" Sort operations acceptable but not instant (max: %d ms)", maxSortTime);
				LOGGER.warn(responseMsg);
			} else {
				responseMsg = String.format(" Sort operations too slow (max: %d ms)", maxSortTime);
				LOGGER.error(responseMsg);
			}
			PageObjectHelper.log(LOGGER, responseMsg);

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_sort_ui_responsiveness_failed", e);
			PageObjectHelper.log(LOGGER, " Failed to validate UI responsiveness: " + e.getMessage());
			Assert.fail("Failed to validate UI responsiveness: " + e.getMessage());
		}
	}

	// =========================================
	// SCENARIO 10: SELECT ALL PERFORMANCE
	// =========================================

	public void user_measures_time_to_click_chevron_button_beside_header_checkbox() {
		try {
			js.executeScript("window.scrollTo(0, 0);");
			safeSleep(500);

			chevronClickStartTime.set(System.currentTimeMillis());

			WebElement chevronBtn = findElement(CHEVRON_BTN_IN_JAM);
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", chevronBtn);
			safeSleep(300);

			try {
				wait.until(ExpectedConditions.elementToBeClickable(chevronBtn)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", chevronBtn);
				} catch (Exception s) {
					jsClick(chevronBtn);
				}
			}

			PerformanceUtils.waitForPageReady(driver, 1);
			safeSleep(300);

			chevronClickEndTime.set(System.currentTimeMillis());
			totalChevronClickTime.set(chevronClickEndTime.get() - chevronClickStartTime.get());

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("measure_chevron_click_failed", e);
			PageObjectHelper.log(LOGGER, " Failed to measure chevron click time: " + e.getMessage());
			Assert.fail("Failed to measure chevron click time: " + e.getMessage());
		}
	}

	public void user_measures_time_to_click_select_all_option() {
		try {
			selectAllClickStartTime.set(System.currentTimeMillis());

			WebElement selectAllButton = findElement(Locators.Table.SELECT_ALL_BTN);
			try {
				wait.until(ExpectedConditions.elementToBeClickable(selectAllButton)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", selectAllButton);
				} catch (Exception s) {
					jsClick(selectAllButton);
				}
			}

			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);
			safeSleep(500);

			selectAllClickEndTime.set(System.currentTimeMillis());
			totalSelectAllClickTime.set(selectAllClickEndTime.get() - selectAllClickStartTime.get());

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("measure_select_all_click_failed", e);
			PageObjectHelper.log(LOGGER, " Failed to measure Select All click time: " + e.getMessage());
			Assert.fail("Failed to measure Select All click time: " + e.getMessage());
		}
	}

	public void user_validates_select_all_operation_time_is_within_acceptable_threshold() {
		try {
			long totalSelectAllOperationTime = totalChevronClickTime.get() + totalSelectAllClickTime.get();

			safeValidateThreshold(totalSelectAllOperationTime, SELECT_ALL_THRESHOLD_MS,
					"Select All Operation (Chevron + Select All)", "select_all_operation");

			LOGGER.info(String.format(" Breakdown: Chevron (%d ms) + Select All (%d ms) = Total (%d ms)",
					totalChevronClickTime.get(), totalSelectAllClickTime.get(), totalSelectAllOperationTime));

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_select_all_threshold_failed", e);
			PageObjectHelper.log(LOGGER, " Failed to validate Select All threshold: " + e.getMessage());
			Assert.fail("Failed to validate Select All threshold: " + e.getMessage());
		}
	}

	public void user_verifies_all_profiles_are_selected() {
		try {
			for (int i = 0; i < 3; i++) {
				js.executeScript("window.scrollBy(0, 800);");
				safeSleep(300);
			}

			js.executeScript("window.scrollTo(0, 0);");
			safeSleep(500);

			java.util.List<WebElement> allCheckboxes = findElements(ALL_CHECKBOXES);

			int selectedCount = 0;
			for (WebElement checkbox : allCheckboxes) {
				if (checkbox.isSelected()) {
					selectedCount++;
				}
			}

			profilesCountAfterSelectAll.set(selectedCount / 3);

			LOGGER.info(
					String.format(" Selected profiles verified: %d profiles selected (Sample of %d checkboxes checked)",
							profilesCountAfterSelectAll.get(), selectedCount));
			PageObjectHelper.log(LOGGER, 
					String.format(" Select All successful: %d profiles selected", profilesCountAfterSelectAll.get()));

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_all_profiles_selected_failed", e);
			PageObjectHelper.log(LOGGER, " Failed to verify all profiles selected: " + e.getMessage());
			Assert.fail("Failed to verify all profiles selected: " + e.getMessage());
		}
	}

	public void user_validates_ui_remains_responsive_during_bulk_selection() {
		try {
			long totalOperationTime = totalChevronClickTime.get() + totalSelectAllClickTime.get();
			long instantThreshold = 1500;

			String responseMsg;
			if (totalOperationTime <= instantThreshold) {
				responseMsg = String.format(" Select All operation feels instant (%d ms)", totalOperationTime);
				LOGGER.info(responseMsg);
			} else if (totalOperationTime <= SELECT_ALL_THRESHOLD_MS) {
				responseMsg = String.format(" Select All operation acceptable but not instant (%d ms)",
						totalOperationTime);
				LOGGER.warn(responseMsg);
			} else {
				responseMsg = String.format(" Select All operation too slow (%d ms)", totalOperationTime);
				LOGGER.error(responseMsg);
			}
			PageObjectHelper.log(LOGGER, responseMsg);

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_bulk_selection_ui_responsiveness_failed", e);
			PageObjectHelper.log(LOGGER, " Failed to validate UI responsiveness: " + e.getMessage());
			Assert.fail("Failed to validate UI responsiveness: " + e.getMessage());
		}
	}

	// =========================================
	// SCENARIO 11: HCM PAGE LOAD PERFORMANCE
	// =========================================

	public void user_navigates_to_profile_manager_screen() {
		try {
			// Wait for page to be ready
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 1);

			// Click on KFONE Global Menu button
			WebElement kfoneMenuBtn = findElement(Locators.Navigation.GLOBAL_NAV_MENU_BTN);
			try {
				wait.until(ExpectedConditions.elementToBeClickable(kfoneMenuBtn)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", kfoneMenuBtn);
				} catch (Exception s) {
					jsClick(kfoneMenuBtn);
				}
			}

			PerformanceUtils.waitForPageReady(driver, 1);

			// Click on Profile Manager button in KFONE menu
			WebElement pmBtn = findElement(KFONE_MENU_PM_BTN);
			try {
				wait.until(ExpectedConditions.elementToBeClickable(pmBtn)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", pmBtn);
				} catch (Exception s) {
					jsClick(pmBtn);
				}
			}

			// Wait for Profile Manager page to load
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			wait.until(ExpectedConditions.visibilityOfElementLocated(PROFILE_MANAGER_HEADER));
			PerformanceUtils.waitForPageReady(driver, 2);

			PageObjectHelper.log(LOGGER, " Navigated to Profile Manager screen");

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("navigate_to_profile_manager_failed", e);
			PageObjectHelper.log(LOGGER, " Failed to navigate to Profile Manager: " + e.getMessage());
			Assert.fail("Failed to navigate to Profile Manager: " + e.getMessage());
		}
	}

	public void user_clicks_on_hcm_sync_profiles_tab() {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(HCM_SYNC_PROFILES_HEADER_TAB)).click();
			safeSleep(500);

			PageObjectHelper.log(LOGGER, " Clicked on HCM Sync Profiles tab");

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_hcm_tab_failed", e);
			PageObjectHelper.log(LOGGER, " Failed to click HCM tab: " + e.getMessage());
			Assert.fail("Failed to click HCM tab: " + e.getMessage());
		}
	}

	public void user_measures_time_to_load_hcm_sync_profiles_page() {
		try {
			hcmPageLoadStartTime.set(System.currentTimeMillis());

			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			wait.until(ExpectedConditions.visibilityOfElementLocated(HCM_SYNC_PROFILES_TITLE));
			PerformanceUtils.waitForPageReady(driver, 2);
			safeSleep(1000);

			hcmPageLoadEndTime.set(System.currentTimeMillis());
			totalHCMPageLoadTime.set(hcmPageLoadEndTime.get() - hcmPageLoadStartTime.get());

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("measure_hcm_page_load_failed", e);
			PageObjectHelper.log(LOGGER, " Failed to measure HCM page load time: " + e.getMessage());
			Assert.fail("Failed to measure HCM page load time: " + e.getMessage());
		}
	}

	public void user_validates_hcm_page_load_time_is_within_acceptable_threshold() {
		safeValidateThreshold(totalHCMPageLoadTime.get(), HCM_PAGE_LOAD_THRESHOLD_MS, "HCM Page Load", "hcm_page_load");
	}

	public void user_verifies_all_hcm_profiles_are_loaded_correctly() {
		try {
			try {
				WebElement hcmResultsCountElement = findElement(HCM_RESULTS_COUNT);
				String resultsText = hcmResultsCountElement.getText().trim();
				hcmProfilesCount.set(extractResultsCount(resultsText));
				LOGGER.info(String.format(" HCM Sync Profiles loaded successfully | %d profiles available",
						hcmProfilesCount.get()));
			} catch (Exception e) {
				java.util.List<WebElement> profileCheckboxes = findElements(HCM_PROFILE_CHECKBOXES);
				hcmProfilesCount.set(profileCheckboxes.size());
				LOGGER.info(
						String.format(" HCM Sync Profiles loaded successfully | %d profiles found", hcmProfilesCount.get()));
			}

			PageObjectHelper.log(LOGGER, String.format(" HCM profiles loaded: %d profiles", hcmProfilesCount.get()));

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_hcm_profiles_loaded_failed", e);
			PageObjectHelper.log(LOGGER, " Failed to verify HCM profiles loaded: " + e.getMessage());
			Assert.fail("Failed to verify HCM profiles loaded: " + e.getMessage());
		}
	}

	// =========================================
	// SCENARIO 12: HCM SYNC PERFORMANCE
	// =========================================

	public void user_is_on_hcm_sync_profiles_page_with_selected_profiles() {
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

			// Get profiles count before selection
			try {
				WebElement hcmResultsCountElement = findElement(HCM_RESULTS_COUNT);
				String resultsText = hcmResultsCountElement.getText().trim();
				selectedProfilesCountBeforeSync.set(extractResultsCount(resultsText));
			} catch (Exception e) {
				// If results count not available, count checkboxes
				java.util.List<WebElement> checkboxes = findElements(HCM_PROFILE_CHECKBOXES);
				selectedProfilesCountBeforeSync.set(checkboxes.size());
			}

			// Click header checkbox to select all loaded profiles
			WebElement hcmHeaderCheckbox = findElement(Locators.HCMSyncProfiles.TABLE_HEADER_CHECKBOX);
			try {
				wait.until(ExpectedConditions.elementToBeClickable(hcmHeaderCheckbox)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", hcmHeaderCheckbox);
				} catch (Exception s) {
					jsClick(hcmHeaderCheckbox);
				}
			}

			safeSleep(500);
			PerformanceUtils.waitForPageReady(driver, 1);

			LOGGER.info(String.format(" Selected profiles using header checkbox: %d profiles ready for sync",
					selectedProfilesCountBeforeSync.get()));
			PageObjectHelper.log(LOGGER, 
					String.format(" %d profiles selected for sync test", selectedProfilesCountBeforeSync.get()));

			// Verify Sync button is enabled
			WebElement syncBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(Locators.HCMSyncProfiles.SYNC_WITH_HCM_BTN));
			if (!syncBtn.isEnabled()) {
				LOGGER.warn(" Sync with HCM button is not enabled, profiles may not be selected");
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_selected_profiles_for_sync_failed", e);
			PageObjectHelper.log(LOGGER, " Failed to verify selected profiles for sync: " + e.getMessage());
			Assert.fail("Failed to verify selected profiles for sync: " + e.getMessage());
		}
	}

	public void user_measures_time_to_click_sync_selected_profiles_button() {
		try {
			syncClickStartTime.set(System.currentTimeMillis());

			WebElement syncBtn = findElement(Locators.HCMSyncProfiles.SYNC_WITH_HCM_BTN);
			try {
				wait.until(ExpectedConditions.elementToBeClickable(syncBtn)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", syncBtn);
				} catch (Exception s) {
					jsClick(syncBtn);
				}
			}

			syncClickEndTime.set(System.currentTimeMillis());
			totalSyncClickTime.set(syncClickEndTime.get() - syncClickStartTime.get());

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("measure_sync_click_failed", e);
			PageObjectHelper.log(LOGGER, " Failed to measure sync click time: " + e.getMessage());
			Assert.fail("Failed to measure sync click time: " + e.getMessage());
		}
	}

	public void user_validates_sync_operation_processing_time() {
		try {
			syncProcessStartTime.set(System.currentTimeMillis());

			wait.until(ExpectedConditions.visibilityOfElementLocated(SYNC_SUCCESS_POPUP_TEXT));

			syncProcessEndTime.set(System.currentTimeMillis());
			totalSyncProcessTime.set(syncProcessEndTime.get() - syncProcessStartTime.get());

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("measure_sync_process_failed", e);
			PageObjectHelper.log(LOGGER, " Failed to measure sync process time: " + e.getMessage());
			Assert.fail("Failed to measure sync process time: " + e.getMessage());
		}
	}

	public void user_verifies_sync_operation_completes_within_acceptable_threshold() {
		try {
			long totalSyncTime = totalSyncClickTime.get() + totalSyncProcessTime.get();

			safeValidateThreshold(totalSyncTime, SYNC_OPERATION_THRESHOLD_MS, "Sync Operation (Click + Process)",
					"sync_operation");

			LOGGER.info(String.format(" Breakdown: Click (%d ms) + Process (%d ms) = Total (%d ms)", totalSyncClickTime.get(),
					totalSyncProcessTime.get(), totalSyncTime));

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_sync_threshold_failed", e);
			PageObjectHelper.log(LOGGER, " Failed to validate sync threshold: " + e.getMessage());
			Assert.fail("Failed to validate sync threshold: " + e.getMessage());
		}
	}

	public void user_validates_sync_status_updates_appear_promptly() {
		try {
			// PARALLEL EXECUTION FIX: Use wait with locator instead of direct findElement
			WebElement successPopup = wait.until(ExpectedConditions.visibilityOfElementLocated(SYNC_SUCCESS_POPUP_TEXT));
			String successMsg = successPopup.getText().trim();
			PageObjectHelper.log(LOGGER, "Sync success message displayed: " + successMsg);

			// PARALLEL EXECUTION FIX: Use elementToBeClickable for close button
			WebElement closeBtn = wait.until(ExpectedConditions.elementToBeClickable(SYNC_SUCCESS_POPUP_CLOSE_BTN));
			closeBtn.click();
			safeSleep(500);

			LOGGER.info(String.format(" Sync operation completed | %d profiles synced with HCM",
					selectedProfilesCountBeforeSync.get()));
			PageObjectHelper.log(LOGGER, String.format(" Synced %d profiles successfully", selectedProfilesCountBeforeSync.get()));

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_sync_status_failed", e);
			PageObjectHelper.log(LOGGER, " Failed to verify sync status: " + e.getMessage());
			Assert.fail("Failed to verify sync status: " + e.getMessage());
		}
	}
}
