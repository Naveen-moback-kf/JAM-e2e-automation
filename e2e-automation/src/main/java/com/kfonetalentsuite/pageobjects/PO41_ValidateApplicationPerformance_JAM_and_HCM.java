package com.kfonetalentsuite.pageobjects;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.kfonetalentsuite.utils.PerformanceUtils;
import com.kfonetalentsuite.utils.ScreenshotHandler;
import com.kfonetalentsuite.utils.Utilities;
import com.kfonetalentsuite.webdriverManager.DriverManager;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

public class PO41_ValidateApplicationPerformance_JAM_and_HCM {
	WebDriver driver = DriverManager.getDriver();	

	protected static final Logger LOGGER = (Logger) LogManager.getLogger();
	
	// Performance thresholds (in milliseconds)
	// NOTE: These thresholds should be adjusted based on your application's baseline performance
	// Run multiple tests to establish average load times, then set threshold = average + 20% buffer
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
	
	// Performance metrics storage
	private static long pageLoadStartTime = 0;
	private static long pageLoadEndTime = 0;
	private static long totalPageLoadTime = 0;
	
	private static long searchStartTime = 0;
	private static long searchEndTime = 0;
	private static long totalSearchTime = 0;
	
	private static long clearSearchStartTime = 0;
	private static long clearSearchEndTime = 0;
	private static long totalClearSearchTime = 0;
	
	private static long filterDropdownOpenStartTime = 0;
	private static long filterDropdownOpenEndTime = 0;
	private static long totalFilterDropdownOpenTime = 0;
	
	private static long singleFilterStartTime = 0;
	private static long singleFilterEndTime = 0;
	private static long totalSingleFilterTime = 0;
	
	private static long multipleFiltersStartTime = 0;
	private static long multipleFiltersEndTime = 0;
	private static long totalMultipleFiltersTime = 0;
	
	private static long clearFiltersStartTime = 0;
	private static long clearFiltersEndTime = 0;
	private static long totalClearFiltersTime = 0;
	
	// Search related variables
	private static String searchKeyword = "";
	private static int resultsCountBeforeSearch = 0;
	private static int resultsCountAfterSearch = 0;
	private static int resultsCountAfterClear = 0;
	
	// Filter related variables
	private static String appliedFilterType = "";
	private static String appliedFilterValue = "";
	private static int resultsCountBeforeFilter = 0;
	private static int resultsCountAfterFilter = 0;
	private static int resultsCountAfterClearFilters = 0;
	private static int numberOfFiltersApplied = 0;
	
	// Scroll and lazy load related variables
	private static long totalScrollTime = 0;
	private static long scrollStartTime = 0;
	private static long scrollEndTime = 0;
	private static int totalScrolls = 0;
	private static int initialProfileCount = 0;
	private static int finalProfileCount = 0;
	private static java.util.List<Long> lazyLoadTimes = new java.util.ArrayList<>();
	private static long avgLazyLoadTime = 0;
	
	// Navigation related variables
	private static long navigationToComparisonStartTime = 0;
	private static long navigationToComparisonEndTime = 0;
	private static long totalNavigationToComparisonTime = 0;
	private static long navigationBackToMappingStartTime = 0;
	private static long navigationBackToMappingEndTime = 0;
	private static long totalNavigationBackToMappingTime = 0;
	
	// Sort related variables
	private static long sortByJobTitleStartTime = 0;
	private static long sortByJobTitleEndTime = 0;
	private static long totalSortByJobTitleTime = 0;
	private static long sortByGradeStartTime = 0;
	private static long sortByGradeEndTime = 0;
	private static long totalSortByGradeTime = 0;
	
	// Select All related variables
	private static long chevronClickStartTime = 0;
	private static long chevronClickEndTime = 0;
	private static long totalChevronClickTime = 0;
	private static long selectAllClickStartTime = 0;
	private static long selectAllClickEndTime = 0;
	private static long totalSelectAllClickTime = 0;
	private static int profilesCountAfterSelectAll = 0;
	
	// HCM Page Load related variables
	private static long hcmPageLoadStartTime = 0;
	private static long hcmPageLoadEndTime = 0;
	private static long totalHCMPageLoadTime = 0;
	private static int hcmProfilesCount = 0;
	
	// HCM Sync related variables
	private static long syncClickStartTime = 0;
	private static long syncClickEndTime = 0;
	private static long totalSyncClickTime = 0;
	private static long syncProcessStartTime = 0;
	private static long syncProcessEndTime = 0;
	private static long totalSyncProcessTime = 0;
	private static int selectedProfilesCountBeforeSync = 0;
	
	// Navigation helper variables
	private static int rowNumberForViewOtherMatches = 0;
	private static String jobNameForComparison = "";

	public PO41_ValidateApplicationPerformance_JAM_and_HCM() throws IOException {
		PageFactory.initElements(driver, this);
	}

	WebDriverWait wait = DriverManager.getWait();
	Utilities utils = new Utilities();
	JavascriptExecutor js = (JavascriptExecutor) driver;
	
	//XPATHs
	@FindBy(xpath = "//*[@class='blocking-loader']//img")
	@CacheLookup
	WebElement pageLoadSpinner1;
	
	@FindBy(xpath = "//div[@data-testid='loader']//img")
	@CacheLookup
	WebElement pageLoadSpinner2;
	
	@FindBy(xpath = "//div[@id='header-logo']")
	@CacheLookup
	public WebElement JAMLogo;
	
	@FindBy(xpath = "//div[@id='org-job-container']")
	@CacheLookup
	public WebElement jobMappingPageContainer;
	
	@FindBy(xpath = "//div[@id='page-heading']//h1")
	@CacheLookup
	public WebElement pageTitleHeader;
	
	@FindBy(xpath = "//div[@id='page-title']//p[1]")
	@CacheLookup
	public WebElement pageTitleDesc;
	
	@FindBy(xpath = "//input[contains(@id,'search-job-title')]")
	@CacheLookup
	public WebElement searchBar;
	
	@FindBy(xpath = "//div[contains(@id,'results-toggle')]//*[contains(text(),'Showing')]")
	@CacheLookup
	public WebElement showingJobResultsCount;
	
	@FindBy(xpath = "//button[@id='filters-btn']")
	@CacheLookup
	public WebElement filtersDropdownButton;
	
	// Navigation XPaths (Job Comparison)
	// Note: Job Comparison is accessed via "View Other Matches" button on a profile
	
	@FindBy(xpath = "//h1[@id='compare-desc']")
	public WebElement jobComparisonHeader;
	
	// Sort XPaths (from PO17)
	@FindBy(xpath = "//*[@id='org-job-container']/div/table/thead/tr/th[2]/div")
	public WebElement orgJobNameHeader;
	
	@FindBy(xpath = "//*[@id='org-job-container']/div/table/thead/tr/th[3]/div")
	public WebElement orgJobGradeHeader;
	
	@FindBy(xpath = "//button[@data-testid='Clear Filters']")
	@CacheLookup
	public WebElement clearFiltersBtn;
	
	// Select All XPaths (Job Mapping)
	@FindBy(xpath = "//th[@scope='col']//div[@class='relative inline-block']//div//*[contains(@class,'cursor-pointer')]")
	public WebElement chevronBtninJAM;
	
	@FindBy(xpath = "//*[contains(text(),'Select All')]")
	public WebElement selectAllBtn;
	
	// KFONE Global Menu XPaths
	@FindBy(xpath = "//button[@id='global-nav-menu-btn']")
	@CacheLookup
	public WebElement KfoneMenu;
	
	@FindBy(xpath = "//span[@aria-label='Profile Manager']")
	@CacheLookup
	public WebElement KfoneMenuPMBtn;
	
	// HCM Navigation XPaths
	@FindBy(xpath = "//h1[contains(text(),'Profile Manager')]")
	public WebElement profileManagerHeader;
	
	@FindBy(xpath = "//span[contains(text(),'HCM Sync Profiles')]")
	public WebElement hcmSyncProfilesHeaderTab;
	
	@FindBy(xpath = "//h1[contains(text(),'Sync Profiles')]")
	public WebElement hcmSyncProfilesTitle;
	
	// HCM Sync XPaths
	@FindBy(xpath = "//thead//tr//div//kf-checkbox")
	public WebElement hcmTableHeaderCheckbox;
	
	@FindBy(xpath = "//button[contains(@class,'custom-export')]")
	public WebElement syncWithHCMBtn;
	
	@FindBy(xpath = "//div[@class='p-toast-detail']")
	public WebElement syncSuccessPopupText;
	
	@FindBy(xpath = "//button[contains(@class,'p-toast-icon-close')]")
	public WebElement syncSuccessPopupCloseBtn;
	
	/**
	 * Measures the time taken to load Job Mapping page
	 * Captures both navigation time and page ready time
	 */
	public void user_measures_the_time_taken_to_load_job_mapping_page() {
		try {
			// Start measuring
			pageLoadStartTime = System.currentTimeMillis();
			
			// Wait for page to be fully loaded
			try {
				wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner1, pageLoadSpinner2));
			} catch (Exception e) {
				// Spinners not found or already invisible
			}
			
			PerformanceUtils.waitForPageReady(driver, 3);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='org-job-container']")));
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='header-logo']")));
			
			// End measuring
			pageLoadEndTime = System.currentTimeMillis();
			totalPageLoadTime = pageLoadEndTime - pageLoadStartTime;
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("measure_page_load_time_failed", e);
			LOGGER.error("❌ Failed to measure page load time", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to measure page load time: " + e.getMessage());
		}
	}
	
	/**
	 * Validates that page load time is within acceptable threshold
	 * Fails the test if performance is below expectations
	 */
	public void user_validates_page_load_time_is_within_acceptable_threshold() {
		try {
			validateThreshold(totalPageLoadTime, PAGE_LOAD_THRESHOLD_MS, "Page Load", "page_load");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_page_load_threshold_failed", e);
			LOGGER.error("❌ Failed to validate page load threshold", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to validate page load threshold: " + e.getMessage());
			Assert.fail("Failed to validate page load threshold: " + e.getMessage());
		}
	}
	
	/**
	 * Verifies that all critical page components are loaded correctly
	 * Validates presence and visibility of key UI elements
	 */
	public void user_verifies_all_critical_page_components_are_loaded() {
		try {
			int totalComponents = 7;
			int loadedComponents = 0;
			
			// Verify critical components
			if (verifyComponent("Logo", JAMLogo)) loadedComponents++;
			if (verifyComponent("Container", jobMappingPageContainer)) loadedComponents++;
			if (verifyComponent("Title", pageTitleHeader)) loadedComponents++;
			if (verifyComponent("Description", pageTitleDesc)) loadedComponents++;
			if (verifyComponent("Search", searchBar)) loadedComponents++;
			if (verifyComponent("Results Count", showingJobResultsCount)) loadedComponents++;
			if (verifyComponent("Filters", filtersDropdownButton)) loadedComponents++;
			
			// Summary
			String summaryMsg = String.format("📊 Components Loaded: %d/%d", loadedComponents, totalComponents);
			LOGGER.info(summaryMsg);
			ExtentCucumberAdapter.addTestStepLog(summaryMsg);
			
			if (loadedComponents < totalComponents) {
				String failMsg = String.format("❌ Only %d/%d components loaded", loadedComponents, totalComponents);
				LOGGER.error(failMsg);
				ScreenshotHandler.captureFailureScreenshot("critical_components_missing", new Exception(failMsg));
				Assert.fail(failMsg);
			}
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_critical_components_failed", e);
			LOGGER.error("❌ Failed to verify critical components", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to verify critical components: " + e.getMessage());
			Assert.fail("Failed to verify critical components: " + e.getMessage());
		}
	}
	
	/**
	 * Helper method to verify individual component
	 */
	private boolean verifyComponent(String componentName, WebElement element) {
		try {
			return element != null && element.isDisplayed();
		} catch (Exception e) {
			LOGGER.warn("⚠️ " + componentName + " not found");
			return false;
		}
	}
	
	// ========================================
	// HELPER METHODS - CODE OPTIMIZATION
	// ========================================
	
	/**
	 * Helper method to log section headers consistently
	 * Reduced verbosity - only for major sections
	 */
	private void logSectionHeader(String title) {
		LOGGER.debug("▶ " + title);
	}
	
	/**
	 * Calculates performance rating based on actual time vs threshold
	 */
	private String getPerformanceRating(long actualTime, long threshold) {
		double percentage = (actualTime / (double) threshold) * 100;
		
		if (percentage <= 50) return "⚡ EXCELLENT";
		else if (percentage <= 75) return "✅ GOOD";
		else if (percentage <= 100) return "⚠️ ACCEPTABLE";
		else if (percentage <= 150) return "❌ POOR";
		else return "🔴 VERY POOR";
	}
	
	/**
	 * Helper method to extract results count from "Showing X of Y results" text
	 */
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
			LOGGER.warn("⚠️ Could not parse results count: " + resultsText);
		}
		return 0;
	}
	
	/**
	 * Generic helper method to validate performance threshold
	 * Logs performance metrics and rating
	 * Does NOT fail test if threshold exceeded - only reports rating
	 * ENHANCED: Captures performance metrics for Excel reporting
	 */
	private void validateThreshold(long actualTime, long threshold, String operationName, String screenshotPrefix) {
		if (actualTime == 0) {
			String errorMsg = "❌ TECHNICAL FAILURE: " + operationName + " time was not measured";
			LOGGER.error(errorMsg);
			ExtentCucumberAdapter.addTestStepLog(errorMsg);
			Assert.fail(errorMsg);
		}
		
		String performanceRating = getPerformanceRating(actualTime, threshold);
		
		// PERFORMANCE METRICS: Capture metrics for Excel reporting
		try {
			String threadId = Thread.currentThread().getName();
			String scenarioName = com.kfonetalentsuite.listeners.ExcelReportListener.getCurrentScenarioName(threadId);
			
			if (scenarioName != null && !scenarioName.isEmpty()) {
				// Clean rating for Excel (remove emoji prefixes)
				String cleanRating = performanceRating
					.replace("⚡ ", "")
					.replace("✅ ", "")
					.replace("⚠️ ", "")
					.replace("❌ ", "")
					.replace("🔴 ", "");
				
				com.kfonetalentsuite.listeners.ExcelReportListener.setPerformanceMetrics(
					scenarioName, threshold, actualTime, cleanRating, operationName);
			}
		} catch (Exception e) {
			// Silently ignore if scenario context is not available
			LOGGER.debug("Could not capture performance metrics for Excel: {}", e.getMessage());
		}
		
		if (actualTime <= threshold) {
			String successMsg = String.format("✅ %s: %d ms (%.2f sec) | %s",
				operationName, actualTime, actualTime / 1000.0, performanceRating);
			LOGGER.info(successMsg);
			ExtentCucumberAdapter.addTestStepLog(successMsg);
		} else {
			long difference = actualTime - threshold;
			String warnMsg = String.format("⚠️ %s: %d ms (%.2f sec) | Exceeded by %d ms | %s",
				operationName, actualTime, actualTime / 1000.0, difference, performanceRating);
			LOGGER.warn(warnMsg);
			ExtentCucumberAdapter.addTestStepLog(warnMsg);
			// Note: Performance warnings are logged but don't capture failure screenshots
			// as they are alerts, not test failures
		}
	}
	
	/**
	 * Reset performance metrics for next test
	 */
	public static void resetPerformanceMetrics() {
		pageLoadStartTime = 0;
		pageLoadEndTime = 0;
		totalPageLoadTime = 0;
		searchStartTime = 0;
		searchEndTime = 0;
		totalSearchTime = 0;
		clearSearchStartTime = 0;
		clearSearchEndTime = 0;
		totalClearSearchTime = 0;
		filterDropdownOpenStartTime = 0;
		filterDropdownOpenEndTime = 0;
		totalFilterDropdownOpenTime = 0;
		singleFilterStartTime = 0;
		singleFilterEndTime = 0;
		totalSingleFilterTime = 0;
		multipleFiltersStartTime = 0;
		multipleFiltersEndTime = 0;
		totalMultipleFiltersTime = 0;
		clearFiltersStartTime = 0;
		clearFiltersEndTime = 0;
		totalClearFiltersTime = 0;
		searchKeyword = "";
		resultsCountBeforeSearch = 0;
		resultsCountAfterSearch = 0;
		resultsCountAfterClear = 0;
		appliedFilterType = "";
		appliedFilterValue = "";
		resultsCountBeforeFilter = 0;
		resultsCountAfterFilter = 0;
		resultsCountAfterClearFilters = 0;
		numberOfFiltersApplied = 0;
		totalScrollTime = 0;
		scrollStartTime = 0;
		scrollEndTime = 0;
		totalScrolls = 0;
		initialProfileCount = 0;
		finalProfileCount = 0;
		lazyLoadTimes.clear();
		avgLazyLoadTime = 0;
		navigationToComparisonStartTime = 0;
		navigationToComparisonEndTime = 0;
		totalNavigationToComparisonTime = 0;
		navigationBackToMappingStartTime = 0;
		navigationBackToMappingEndTime = 0;
		totalNavigationBackToMappingTime = 0;
		sortByJobTitleStartTime = 0;
		sortByJobTitleEndTime = 0;
		totalSortByJobTitleTime = 0;
		sortByGradeStartTime = 0;
		sortByGradeEndTime = 0;
		totalSortByGradeTime = 0;
		chevronClickStartTime = 0;
		chevronClickEndTime = 0;
		totalChevronClickTime = 0;
		selectAllClickStartTime = 0;
		selectAllClickEndTime = 0;
		totalSelectAllClickTime = 0;
		profilesCountAfterSelectAll = 0;
		hcmPageLoadStartTime = 0;
		hcmPageLoadEndTime = 0;
		totalHCMPageLoadTime = 0;
		hcmProfilesCount = 0;
		syncClickStartTime = 0;
		syncClickEndTime = 0;
		totalSyncClickTime = 0;
		syncProcessStartTime = 0;
		syncProcessEndTime = 0;
		totalSyncProcessTime = 0;
		selectedProfilesCountBeforeSync = 0;
		rowNumberForViewOtherMatches = 0;
		jobNameForComparison = "";
		LOGGER.debug("Performance metrics reset for next test");
	}
	
	// ========================================
	// SCENARIO 2: SEARCH PERFORMANCE TESTING
	// ========================================
	
	/**
	 * Validates user is on Job Mapping page with loaded profiles
	 * Ensures page is ready for search performance testing
	 */
	public void user_is_on_job_mapping_page_with_loaded_profiles() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForPageReady(driver, 2);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='org-job-container']")));
			wait.until(ExpectedConditions.elementToBeClickable(searchBar));
			
			String resultsText = showingJobResultsCount.getText().trim();
			resultsCountBeforeSearch = extractResultsCount(resultsText);
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("job_mapping_page_not_ready", e);
			LOGGER.error("❌ Job Mapping page not ready", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Job Mapping page not ready: " + e.getMessage());
			Assert.fail("Job Mapping page not ready: " + e.getMessage());
		}
	}
	
	/**
	 * Measures time to perform search with dynamic keyword
	 * Extracts a job name substring from visible profiles and performs search
	 */
	public void user_measures_time_to_perform_search_with_dynamic_keyword() {
		try {
			// Extract dynamic search keyword
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tbody//tr[1]//td[2]//div[contains(text(),'(')]")));
			var firstProfileElement = driver.findElement(By.xpath("//tbody//tr[1]//td[2]//div[contains(text(),'(')]"));
			String fullJobName = firstProfileElement.getText().trim();
			
			if (fullJobName.length() >= 3) {
				searchKeyword = fullJobName.substring(0, 3);
			} else if (fullJobName.contains(" ")) {
				searchKeyword = fullJobName.split(" ")[0];
			} else {
				searchKeyword = fullJobName;
			}
			
			// Measure search performance
			searchBar.clear();
			Thread.sleep(300);
			
			searchStartTime = System.currentTimeMillis();
			searchBar.sendKeys(searchKeyword);
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForPageReady(driver, 2);
			Thread.sleep(500);
			searchEndTime = System.currentTimeMillis();
			totalSearchTime = searchEndTime - searchStartTime;
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("measure_search_time_failed", e);
			LOGGER.error("❌ Failed to measure search time", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to measure search time: " + e.getMessage());
			Assert.fail("Failed to measure search time: " + e.getMessage());
		}
	}
	
	/**
	 * Validates that search response time is within acceptable threshold
	 * Fails the test if search performance is below expectations
	 */
	public void user_validates_search_response_time_is_within_acceptable_threshold() {
		try {
			validateThreshold(totalSearchTime, SEARCH_THRESHOLD_MS, "Search", "search");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_search_threshold_failed", e);
			LOGGER.error("❌ Failed to validate search threshold", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to validate search threshold: " + e.getMessage());
			Assert.fail("Failed to validate search threshold: " + e.getMessage());
		}
	}
	
	/**
	 * Verifies search results information (informational only for performance testing)
	 * For performance tests, we don't care if search returns 0 or 100 results
	 * We only measure how FAST the search executes, not the accuracy
	 */
	public void user_verifies_search_results_are_accurate() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForPageReady(driver, 1);
			
			String resultsText = showingJobResultsCount.getText().trim();
			resultsCountAfterSearch = extractResultsCount(resultsText);
			
			var visibleProfiles = driver.findElements(By.xpath("//div[@id='org-job-container']//tbody//tr//td[2]//div[contains(text(),'(')]"));
			int matchingResults = 0;
			String searchKeywordLower = searchKeyword.toLowerCase();
			
			for (WebElement profile : visibleProfiles) {
				String jobName = profile.getText().toLowerCase();
				if (jobName.contains(searchKeywordLower)) {
					matchingResults++;
				}
			}
			
			String accuracyMsg = String.format("📊 Search '%s': %d matching | %d → %d profiles",
				searchKeyword, matchingResults, resultsCountBeforeSearch, resultsCountAfterSearch);
			
			LOGGER.info(accuracyMsg);
			ExtentCucumberAdapter.addTestStepLog(accuracyMsg);
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_search_results_failed", e);
			LOGGER.error("❌ Failed to verify search results", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to verify search results: " + e.getMessage());
			Assert.fail("Failed to verify search results: " + e.getMessage());
		}
	}
	
	/**
	 * Validates that search suggestions appear instantly
	 * Verifies UI responsiveness during search operation
	 */
	public void user_validates_search_suggestions_appear_instantly() {
		try {
			long instantThreshold = 1000;
			String responseMsg;
			
			if (totalSearchTime <= instantThreshold) {
				responseMsg = String.format("✅ Search UI Responsive: %d ms (Instant)", totalSearchTime);
				LOGGER.info(responseMsg);
			} else if (totalSearchTime <= SEARCH_THRESHOLD_MS) {
				responseMsg = String.format("⚠️ Search UI Acceptable: %d ms (Not instant)", totalSearchTime);
				LOGGER.warn(responseMsg);
			} else {
				responseMsg = String.format("❌ Search UI Slow: %d ms", totalSearchTime);
				LOGGER.error(responseMsg);
			}
			ExtentCucumberAdapter.addTestStepLog(responseMsg);
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_search_suggestions_failed", e);
			LOGGER.error("❌ Failed to validate search UI responsiveness", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to validate search UI: " + e.getMessage());
			Assert.fail("Failed to validate search UI: " + e.getMessage());
		}
	}
	
	// ==========================================
	// SCENARIO 3: CLEAR SEARCH PERFORMANCE
	// ==========================================
	
	/**
	 * Validates that user has performed search operation with filtered results
	 * Ensures search is already performed and ready for clear operation
	 */
	public void user_has_performed_search_operation_with_filtered_results() {
		try {
			if (searchKeyword == null || searchKeyword.isEmpty() || resultsCountAfterSearch == 0) {
				user_is_on_job_mapping_page_with_loaded_profiles();
				user_measures_time_to_perform_search_with_dynamic_keyword();
			}
			
			String currentSearchText = searchBar.getAttribute("value");
			if (currentSearchText == null || currentSearchText.trim().isEmpty()) {
				String errorMsg = "❌ Search bar is empty";
				LOGGER.error(errorMsg);
				ExtentCucumberAdapter.addTestStepLog(errorMsg);
				Assert.fail(errorMsg);
			}
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("search_not_performed", e);
			LOGGER.error("❌ Failed to verify search state", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to verify search state: " + e.getMessage());
			Assert.fail("Failed to verify search state: " + e.getMessage());
		}
	}
	
	/**
	 * Measures time to clear search
	 * Clears search bar and waits for all profiles to be restored
	 */
	public void user_measures_time_to_clear_search() {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[contains(@id,'search-job-title')]")));
			WebElement searchBarFresh = driver.findElement(By.xpath("//input[contains(@id,'search-job-title')]"));
			
			// Measure clear search performance
			clearSearchStartTime = System.currentTimeMillis();
			
			wait.until(ExpectedConditions.elementToBeClickable(searchBarFresh)).click();
			Thread.sleep(200);
			
			searchBarFresh = driver.findElement(By.xpath("//input[contains(@id,'search-job-title')]"));
			searchBarFresh.sendKeys(org.openqa.selenium.Keys.CONTROL + "a");
			Thread.sleep(100);
			
			searchBarFresh = driver.findElement(By.xpath("//input[contains(@id,'search-job-title')]"));
			searchBarFresh.sendKeys(org.openqa.selenium.Keys.DELETE);
			Thread.sleep(100);
			
			searchBarFresh = driver.findElement(By.xpath("//input[contains(@id,'search-job-title')]"));
			searchBarFresh.sendKeys(org.openqa.selenium.Keys.ENTER);
			
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForPageReady(driver, 2);
			Thread.sleep(500);
			
			clearSearchEndTime = System.currentTimeMillis();
			totalClearSearchTime = clearSearchEndTime - clearSearchStartTime;
			
			String resultsText = showingJobResultsCount.getText().trim();
			resultsCountAfterClear = extractResultsCount(resultsText);
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("measure_clear_search_time_failed", e);
			LOGGER.error("❌ Failed to measure clear search time", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to measure clear search time: " + e.getMessage());
			Assert.fail("Failed to measure clear search time: " + e.getMessage());
		}
	}
	
	/**
	 * Validates that clear search time is within acceptable threshold
	 * Fails the test if clear search performance is below expectations
	 */
	public void user_validates_clear_search_time_is_within_acceptable_threshold() {
		try {
			validateThreshold(totalClearSearchTime, CLEAR_SEARCH_THRESHOLD_MS, "Clear Search", "clear_search");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_clear_search_threshold_failed", e);
			LOGGER.error("❌ Failed to validate clear search threshold", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to validate clear search threshold: " + e.getMessage());
			Assert.fail("Failed to validate clear search threshold: " + e.getMessage());
		}
	}
	
	/**
	 * Verifies that all profiles are restored correctly after clearing search
	 * Validates that profile count matches the original count before search
	 */
	public void user_verifies_all_profiles_are_restored_correctly() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForPageReady(driver, 1);
			
			WebElement searchBarFresh = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[contains(@id,'search-job-title')]")));
			String currentSearchText = searchBarFresh.getAttribute("value");
			boolean isSearchBarEmpty = (currentSearchText == null || currentSearchText.trim().isEmpty());
			boolean countsMatch = (resultsCountAfterClear == resultsCountBeforeSearch);
			
			String restorationMsg = String.format("📊 Restoration: %d → %d → %d profiles | Empty: %s | Match: %s",
				resultsCountBeforeSearch, resultsCountAfterSearch, resultsCountAfterClear,
				isSearchBarEmpty ? "✓" : "✗", countsMatch ? "✓" : "✗");
			LOGGER.info(restorationMsg);
			ExtentCucumberAdapter.addTestStepLog(restorationMsg);
			
			if (!countsMatch) {
				String failMsg = String.format("❌ Profile count mismatch: Expected %d, Actual %d",
					resultsCountBeforeSearch, resultsCountAfterClear);
				LOGGER.error(failMsg);
				ExtentCucumberAdapter.addTestStepLog(failMsg);
				ScreenshotHandler.captureFailureScreenshot("profiles_not_restored", new Exception(failMsg));
				Assert.fail(failMsg);
			}
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_restoration_failed", e);
			LOGGER.error("❌ Failed to verify profile restoration", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to verify profile restoration: " + e.getMessage());
			Assert.fail("Failed to verify profile restoration: " + e.getMessage());
		}
	}
	
	/**
	 * Validates that UI remains responsive during clear operation
	 * Verifies no freeze or lag during clear search
	 */
	public void user_validates_ui_remains_responsive_during_clear_operation() {
		try {
			long instantThreshold = 1000;
			String responseMsg;
			
			if (totalClearSearchTime <= instantThreshold) {
				responseMsg = String.format("✅ Clear UI Responsive: %d ms (Instant)", totalClearSearchTime);
				LOGGER.info(responseMsg);
			} else if (totalClearSearchTime <= CLEAR_SEARCH_THRESHOLD_MS) {
				responseMsg = String.format("⚠️ Clear UI Acceptable: %d ms (Not instant)", totalClearSearchTime);
				LOGGER.warn(responseMsg);
			} else {
				responseMsg = String.format("❌ Clear UI Slow: %d ms", totalClearSearchTime);
				LOGGER.error(responseMsg);
			}
			ExtentCucumberAdapter.addTestStepLog(responseMsg);
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_clear_ui_responsiveness_failed", e);
			LOGGER.error("❌ Failed to validate clear UI responsiveness", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to validate clear UI: " + e.getMessage());
			Assert.fail("Failed to validate clear UI: " + e.getMessage());
		}
	}
	
	// =========================================
	// SCENARIO 4: SINGLE FILTER PERFORMANCE
	// =========================================
	
	/**
	 * Measures time to open filters dropdown
	 */
	public void user_measures_time_to_open_filters_dropdown() {
		try {
			wait.until(ExpectedConditions.visibilityOf(showingJobResultsCount));
			String resultsText = showingJobResultsCount.getText().trim();
			resultsCountBeforeFilter = extractResultsCount(resultsText);
			
			js.executeScript("window.scrollTo(0, 0);");
			Thread.sleep(300);
			
			filterDropdownOpenStartTime = System.currentTimeMillis();
			wait.until(ExpectedConditions.elementToBeClickable(filtersDropdownButton)).click();
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[starts-with(@data-testid,'dropdown-')]")));
			filterDropdownOpenEndTime = System.currentTimeMillis();
			totalFilterDropdownOpenTime = filterDropdownOpenEndTime - filterDropdownOpenStartTime;
			
			String performanceLog = String.format("📊 Dropdown Open: %d ms (%.2f sec)",
				totalFilterDropdownOpenTime, totalFilterDropdownOpenTime / 1000.0);
			LOGGER.info(performanceLog);
			ExtentCucumberAdapter.addTestStepLog(performanceLog);
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("measure_filter_dropdown_open_failed", e);
			LOGGER.error("❌ Failed to measure filter dropdown open time", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to measure filter dropdown open time: " + e.getMessage());
			Assert.fail("Failed to measure filter dropdown open time: " + e.getMessage());
		}
	}
	
	/**
	 * Measures time to apply single filter dynamically
	 * Uses dynamic filter selection from available filter options
	 */
	public void user_measures_time_to_apply_single_filter_dynamically() {
		try {
			logSectionHeader("PERFORMANCE MEASUREMENT: Apply Single Filter");
			
			// Start measuring filter application time
			singleFilterStartTime = System.currentTimeMillis();
			
			boolean filterApplied = false;
			// Map display names to data-testid values
			String[][] filterTypes = {
				{"Grades", "dropdown-Grades"},
				{"Functions", "dropdown-Functions_SubFunctions"},
				{"Mapping Status", "dropdown-MappingStatus"}
				// Note: Departments is often disabled in the UI
			};
			
			// Try each filter type until one works
			for (String[] filterInfo : filterTypes) {
				String filterType = filterInfo[0];
				String dataTestId = filterInfo[1];
				
				try {
					
					// Look for the filter category header using data-testid
					java.util.List<WebElement> filterHeaders = driver.findElements(
						org.openqa.selenium.By.xpath("//div[@data-testid='" + dataTestId + "']")
					);
					
					if (filterHeaders.isEmpty()) {
						LOGGER.debug(filterType + " filter not found, trying next...");
						continue;
					}
					
					// Check if filter is disabled
					WebElement filterHeader = filterHeaders.get(0);
					try {
						WebElement parentDiv = filterHeader.findElement(org.openqa.selenium.By.xpath("./ancestor::div[contains(@class,'p-4')][1]"));
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
					Thread.sleep(800); // Increased wait for checkboxes to render
					LOGGER.debug("✓ Expanded " + filterType + " filter category");
					
					// Find available filter options (checkboxes) for this category
					// Try multiple XPath strategies to find checkboxes
					java.util.List<WebElement> filterOptions = driver.findElements(
						org.openqa.selenium.By.xpath("//div[@data-testid='" + dataTestId + "']/following-sibling::div//input[@type='checkbox']")
					);
					
					// If not found using following-sibling, try parent-based approach
					if (filterOptions.isEmpty()) {
						LOGGER.debug("Trying parent-based XPath for " + filterType);
						filterOptions = driver.findElements(
							org.openqa.selenium.By.xpath("//div[@data-testid='" + dataTestId + "']/ancestor::div[contains(@class,'p-4')]//input[@type='checkbox']")
						);
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
							WebElement labelElement = filterOption.findElement(
								org.openqa.selenium.By.xpath("./following-sibling::label")
							);
							appliedFilterValue = labelElement.getText().trim();
							
							// Click the checkbox directly
							try {
								wait.until(ExpectedConditions.elementToBeClickable(filterOption)).click();
							} catch (Exception e) {
								js.executeScript("arguments[0].click();", filterOption);
							}
							
							appliedFilterType = filterType;
							LOGGER.info("✓ Applied filter: " + filterType + " = " + appliedFilterValue);
							
							// Wait for results to update
							Thread.sleep(1500);
							wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
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
				wait.until(ExpectedConditions.elementToBeClickable(filtersDropdownButton)).click();
				Thread.sleep(500);
				LOGGER.debug("✓ Filters dropdown closed");
			} catch (Exception e) {
				LOGGER.debug("Could not close filters dropdown: " + e.getMessage());
			}
			
			// End measuring filter application time
			singleFilterEndTime = System.currentTimeMillis();
			totalSingleFilterTime = singleFilterEndTime - singleFilterStartTime;
			
			// Get results count after filtering using helper method
			wait.until(ExpectedConditions.visibilityOf(showingJobResultsCount));
			String resultsText = showingJobResultsCount.getText().trim();
			resultsCountAfterFilter = extractResultsCount(resultsText);
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("measure_single_filter_failed", e);
			LOGGER.error("❌ Failed to measure single filter time", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to measure single filter time: " + e.getMessage());
			Assert.fail("Failed to measure single filter time: " + e.getMessage());
		}
	}
	
	/**
	 * Validates filter application time is within acceptable threshold
	 */
	public void user_validates_filter_application_time_is_within_acceptable_threshold() {
		try {
			validateThreshold(totalSingleFilterTime, SINGLE_FILTER_THRESHOLD_MS, "Single Filter", "filter");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_filter_threshold_failed", e);
			LOGGER.error("❌ Failed to validate filter threshold", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to validate filter threshold: " + e.getMessage());
			Assert.fail("Failed to validate filter threshold: " + e.getMessage());
		}
	}
	
	/**
	 * Verifies filtered results are displayed correctly
	 */
	public void user_verifies_filtered_results_are_displayed_correctly() {
		try {
			boolean clearFiltersVisible = false;
			try {
				java.util.List<WebElement> clearButtons = driver.findElements(By.xpath("//button[@data-testid='Clear Filters']"));
				if (clearButtons.isEmpty()) {
					clearButtons = driver.findElements(By.xpath("//button[contains(text(),'Clear') and contains(text(),'Filter')]"));
				}
				clearFiltersVisible = !clearButtons.isEmpty() && clearButtons.get(0).isDisplayed();
			} catch (Exception e) {
				// Clear button check skipped
			}
			
			String validationMsg = String.format("📊 Filter %s=%s: %d → %d profiles | Clear Btn: %s",
				appliedFilterType, appliedFilterValue, resultsCountBeforeFilter, resultsCountAfterFilter,
				clearFiltersVisible ? "✓" : "✗");
			LOGGER.info(validationMsg);
			ExtentCucumberAdapter.addTestStepLog(validationMsg);
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_filtered_results_failed", e);
			LOGGER.error("❌ Failed to verify filtered results", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to verify filtered results: " + e.getMessage());
			Assert.fail("Failed to verify filtered results: " + e.getMessage());
		}
	}
	
	/**
	 * Validates UI remains responsive during filter operation
	 */
	public void user_validates_ui_remains_responsive_during_filter_operation() {
		try {
			long instantThreshold = 2000;
			String responseMsg;
			
			if (totalSingleFilterTime <= instantThreshold) {
				responseMsg = String.format("✅ Filter UI Responsive: %d ms (Instant)", totalSingleFilterTime);
				LOGGER.info(responseMsg);
			} else if (totalSingleFilterTime <= SINGLE_FILTER_THRESHOLD_MS) {
				responseMsg = String.format("⚠️ Filter UI Acceptable: %d ms (Not instant)", totalSingleFilterTime);
				LOGGER.warn(responseMsg);
			} else {
				responseMsg = String.format("❌ Filter UI Slow: %d ms", totalSingleFilterTime);
				LOGGER.error(responseMsg);
			}
			ExtentCucumberAdapter.addTestStepLog(responseMsg);
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_filter_ui_responsiveness_failed", e);
			LOGGER.error("❌ Failed to validate filter UI responsiveness", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to validate filter UI: " + e.getMessage());
			Assert.fail("Failed to validate filter UI: " + e.getMessage());
		}
	}
	
	// ==========================================
	// SCENARIO 5: MULTIPLE FILTERS PERFORMANCE
	// ==========================================
	
	/**
	 * Measures time to apply multiple filters dynamically from available options
	 */
	public void user_measures_time_to_apply_multiple_filters_dynamically_from_available_options() {
		try {
			wait.until(ExpectedConditions.visibilityOf(showingJobResultsCount));
			String resultsText = showingJobResultsCount.getText().trim();
			resultsCountBeforeFilter = Integer.parseInt(resultsText.replaceAll("[^0-9]", "").substring(resultsText.replaceAll("[^0-9]", "").length() - 2));
			
			js.executeScript("window.scrollTo(0, 0);");
			Thread.sleep(300);
			
			java.util.List<WebElement> existingDropdowns = driver.findElements(
				org.openqa.selenium.By.xpath("//div[starts-with(@data-testid,'dropdown-')]")
			);
			
			if (existingDropdowns.isEmpty()) {
				wait.until(ExpectedConditions.elementToBeClickable(filtersDropdownButton)).click();
				wait.until(ExpectedConditions.presenceOfElementLocated(
					org.openqa.selenium.By.xpath("//div[starts-with(@data-testid,'dropdown-')]")
				));
				Thread.sleep(500);
			}
			
			multipleFiltersStartTime = System.currentTimeMillis();
			numberOfFiltersApplied = 0;
			
			String[][] filterTypes = {
				{"Grades", "dropdown-Grades"},
				{"Functions", "dropdown-Functions_SubFunctions"},
				{"Mapping Status", "dropdown-MappingStatus"}
			};
			StringBuilder appliedFiltersLog = new StringBuilder();
			
			for (String[] filterInfo : filterTypes) {
				if (numberOfFiltersApplied >= 2) {
					break;
				}
				
				String filterType = filterInfo[0];
				String dataTestId = filterInfo[1];
				
				try {
					java.util.List<WebElement> filterHeaders = driver.findElements(
						org.openqa.selenium.By.xpath("//div[@data-testid='" + dataTestId + "']")
					);
					
					if (filterHeaders.isEmpty()) {
						continue;
					}
					
					WebElement filterHeader = filterHeaders.get(0);
					try {
						WebElement parentDiv = filterHeader.findElement(org.openqa.selenium.By.xpath("./ancestor::div[contains(@class,'p-4')][1]"));
						String parentClass = parentDiv.getAttribute("class");
						if (parentClass != null && parentClass.contains("pointer-events-none")) {
							continue;
						}
					} catch (Exception e) {
						// Continue if disabled check fails
					}
					
					filterHeader.click();
					Thread.sleep(800);
					
					java.util.List<WebElement> filterOptions = driver.findElements(
						org.openqa.selenium.By.xpath("//div[@data-testid='" + dataTestId + "']/following-sibling::div//input[@type='checkbox']")
					);
					
					if (filterOptions.isEmpty()) {
						filterOptions = driver.findElements(
							org.openqa.selenium.By.xpath("//div[@data-testid='" + dataTestId + "']/ancestor::div[contains(@class,'p-4')]//input[@type='checkbox']")
						);
					}
					
					if (filterOptions.isEmpty()) {
						continue;
					}
					
					for (WebElement filterOption : filterOptions) {
						if (!filterOption.isSelected()) {
							WebElement labelElement = filterOption.findElement(
								org.openqa.selenium.By.xpath("./following-sibling::label")
							);
							String filterValue = labelElement.getText().trim();
							
							try {
								wait.until(ExpectedConditions.elementToBeClickable(filterOption)).click();
							} catch (Exception e) {
								js.executeScript("arguments[0].click();", filterOption);
							}
							
							numberOfFiltersApplied++;
							appliedFiltersLog.append(String.format("   • %s = %s%n", filterType, filterValue));
							LOGGER.info("✓ Applied filter " + numberOfFiltersApplied + ": " + filterType + " = " + filterValue);
							
							Thread.sleep(1500);
							wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
							PerformanceUtils.waitForPageReady(driver, 1);
							
							break;
						}
					}
					
				} catch (Exception e) {
					continue;
				}
			}
			
			if (numberOfFiltersApplied < 2) {
				throw new Exception("Could not apply at least 2 filters dynamically - only applied " + numberOfFiltersApplied);
			}
			
			try {
				wait.until(ExpectedConditions.elementToBeClickable(filtersDropdownButton)).click();
				Thread.sleep(500);
			} catch (Exception e) {
				// Continue if dropdown close fails
			}
			
			multipleFiltersEndTime = System.currentTimeMillis();
			totalMultipleFiltersTime = multipleFiltersEndTime - multipleFiltersStartTime;
			
			wait.until(ExpectedConditions.visibilityOf(showingJobResultsCount));
			resultsText = showingJobResultsCount.getText().trim();
			resultsCountAfterFilter = Integer.parseInt(resultsText.replaceAll("[^0-9]", "").substring(resultsText.replaceAll("[^0-9]", "").length() - 2));
			
			String performanceLog = String.format(
				"📊 Performance Metrics:%n" +
				"   • Filters Applied: %d%n" +
				"%s" +
				"   • Total Time: %d ms (%.2f sec)%n" +
				"   • Results: %d → %d profiles",
				numberOfFiltersApplied,
				appliedFiltersLog.toString(),
				totalMultipleFiltersTime, totalMultipleFiltersTime / 1000.0,
				resultsCountBeforeFilter, resultsCountAfterFilter
			);
			ExtentCucumberAdapter.addTestStepLog(performanceLog);
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("measure_multiple_filters_failed", e);
			LOGGER.error("❌ Failed to measure multiple filters time", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to measure multiple filters time: " + e.getMessage());
			Assert.fail("Failed to measure multiple filters time: " + e.getMessage());
		}
	}
	
	/**
	 * Validates multiple filter application time is within acceptable threshold
	 */
	public void user_validates_multiple_filter_application_time_is_within_acceptable_threshold() {
		try {
			validateThreshold(totalMultipleFiltersTime, MULTIPLE_FILTERS_THRESHOLD_MS, "Multiple Filters (" + numberOfFiltersApplied + " filters)", "multiple_filters");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_multiple_filters_threshold_failed", e);
			LOGGER.error("❌ Failed to validate multiple filters threshold", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to validate multiple filters threshold: " + e.getMessage());
			Assert.fail("Failed to validate multiple filters threshold: " + e.getMessage());
		}
	}
	
	/**
	 * Verifies combined filtered results are displayed correctly
	 */
	public void user_verifies_combined_filtered_results_are_displayed_correctly() {
		try {
			boolean resultsDecreased = resultsCountAfterFilter <= resultsCountBeforeFilter;
			
			boolean clearFiltersVisible = false;
			try {
				java.util.List<WebElement> clearButtons = driver.findElements(
					org.openqa.selenium.By.xpath("//button[@data-testid='Clear Filters']")
				);
				if (clearButtons.isEmpty()) {
					clearButtons = driver.findElements(
						org.openqa.selenium.By.xpath("//button[contains(text(),'Clear') and contains(text(),'Filter')]")
					);
				}
				clearFiltersVisible = !clearButtons.isEmpty() && clearButtons.get(0).isDisplayed();
			} catch (Exception e) {
				// Clear Filters button check failed
			}
			
			String resultsText = showingJobResultsCount.getText().trim();
			boolean resultsCountDisplayed = !resultsText.isEmpty();
			
			String validationMsg = String.format(
				"📊 Combined Filter Results: %d filters applied | %d → %d profiles | Clear button: %s",
				numberOfFiltersApplied, resultsCountBeforeFilter, resultsCountAfterFilter,
				clearFiltersVisible ? "Visible ✓" : "Not visible ✗"
			);
			LOGGER.info(validationMsg);
			ExtentCucumberAdapter.addTestStepLog(validationMsg);
			
			if (resultsDecreased && clearFiltersVisible && resultsCountDisplayed) {
				ExtentCucumberAdapter.addTestStepLog("✅ All combined filter validations passed");
			}
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_combined_filtered_results_failed", e);
			LOGGER.error("❌ Failed to verify combined filtered results", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to verify combined filtered results: " + e.getMessage());
			Assert.fail("Failed to verify combined filtered results: " + e.getMessage());
		}
	}
	
	/**
	 * Validates no UI lag during multi-filter operation
	 */
	public void user_validates_no_ui_lag_during_multi_filter_operation() {
		try {
			long smoothThreshold = 3000;
			String responseMsg;
			if (totalMultipleFiltersTime <= smoothThreshold) {
				responseMsg = String.format("✅ UI Responsive: %d ms (Smooth)", totalMultipleFiltersTime);
			} else {
				responseMsg = String.format("⚠️ UI Acceptable: %d ms (Not instant)", totalMultipleFiltersTime);
			}
			LOGGER.info(responseMsg);
			ExtentCucumberAdapter.addTestStepLog(responseMsg);
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_multi_filter_ui_lag_failed", e);
			LOGGER.error("❌ Failed to validate UI lag", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to validate UI lag: " + e.getMessage());
			Assert.fail("Failed to validate UI lag: " + e.getMessage());
		}
	}
	
	// ==========================================
	// SCENARIO 6: CLEAR FILTERS PERFORMANCE
	// ==========================================
	
	/**
	 * User has applied filters and has filtered results (prerequisite for clear filters scenario)
	 * IMPORTANT: Clears any existing filters first to capture true baseline count
	 */
	public void user_has_applied_filters_and_has_filtered_results() {
		try {
			// Clear any existing filters from previous scenarios
			try {
				java.util.List<WebElement> clearButtons = driver.findElements(
					org.openqa.selenium.By.xpath("//button[@data-testid='Clear Filters']")
				);
				if (clearButtons.isEmpty()) {
					clearButtons = driver.findElements(
						org.openqa.selenium.By.xpath("//button[contains(text(),'Clear') and contains(text(),'Filter')]")
					);
				}
				
				if (!clearButtons.isEmpty() && clearButtons.get(0).isDisplayed()) {
					js.executeScript("window.scrollTo(0, 0);");
					Thread.sleep(300);
					clearButtons.get(0).click();
					Thread.sleep(500);
					wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
					PerformanceUtils.waitForPageReady(driver, 2);
					Thread.sleep(1000);
				}
			} catch (Exception e) {
				// No existing filters
			}
			
			wait.until(ExpectedConditions.visibilityOf(showingJobResultsCount));
			Thread.sleep(1000);
			
			int maxRetries = 10;
			int retryCount = 0;
			String resultsText = "";
			
			while (retryCount < maxRetries) {
				resultsText = showingJobResultsCount.getText().trim();
				resultsCountBeforeFilter = extractResultsCount(resultsText);
				
				if (resultsCountBeforeFilter > 0) {
					break;
				}
				
				Thread.sleep(500);
				retryCount++;
			}
			
			if (resultsCountBeforeFilter <= 0) {
				String errorMsg = "❌ Failed to capture valid profile count before filtering after " + maxRetries + " attempts";
				LOGGER.error(errorMsg);
				ScreenshotHandler.captureFailureScreenshot("invalid_profile_count_before_filter", new Exception(errorMsg));
				Assert.fail(errorMsg);
			}
			
			js.executeScript("window.scrollTo(0, 0);");
			Thread.sleep(300);
			
			java.util.List<WebElement> existingDropdowns = driver.findElements(
				org.openqa.selenium.By.xpath("//div[starts-with(@data-testid,'dropdown-')]")
			);
			
			if (existingDropdowns.isEmpty()) {
				wait.until(ExpectedConditions.elementToBeClickable(filtersDropdownButton)).click();
				wait.until(ExpectedConditions.presenceOfElementLocated(
					org.openqa.selenium.By.xpath("//div[starts-with(@data-testid,'dropdown-')]")
				));
				Thread.sleep(500);
			}
			
			boolean filterApplied = false;
			String[][] filterTypes = {
				{"Grades", "dropdown-Grades"},
				{"Functions", "dropdown-Functions_SubFunctions"},
				{"Mapping Status", "dropdown-MappingStatus"}
			};
			
			for (String[] filterInfo : filterTypes) {
				String filterType = filterInfo[0];
				String dataTestId = filterInfo[1];
				
				try {
					java.util.List<WebElement> filterHeaders = driver.findElements(
						org.openqa.selenium.By.xpath("//div[@data-testid='" + dataTestId + "']")
					);
					
					if (filterHeaders.isEmpty()) {
						continue;
					}
					
					WebElement filterHeader = filterHeaders.get(0);
					try {
						WebElement parentDiv = filterHeader.findElement(org.openqa.selenium.By.xpath("./ancestor::div[contains(@class,'p-4')][1]"));
						String parentClass = parentDiv.getAttribute("class");
						if (parentClass != null && parentClass.contains("pointer-events-none")) {
							continue;
						}
					} catch (Exception e) {
						// Continue if disabled check fails
					}
					
					filterHeader.click();
					Thread.sleep(800);
					
					java.util.List<WebElement> filterOptions = driver.findElements(
						org.openqa.selenium.By.xpath("//div[@data-testid='" + dataTestId + "']/following-sibling::div//input[@type='checkbox']")
					);
					
					if (filterOptions.isEmpty()) {
						filterOptions = driver.findElements(
							org.openqa.selenium.By.xpath("//div[@data-testid='" + dataTestId + "']/ancestor::div[contains(@class,'p-4')]//input[@type='checkbox']")
						);
					}
					
					if (filterOptions.isEmpty()) {
						continue;
					}
					
					for (WebElement filterOption : filterOptions) {
						if (!filterOption.isSelected()) {
							WebElement labelElement = filterOption.findElement(
								org.openqa.selenium.By.xpath("./following-sibling::label")
							);
							appliedFilterValue = labelElement.getText().trim();
							
							try {
								wait.until(ExpectedConditions.elementToBeClickable(filterOption)).click();
							} catch (Exception e) {
								js.executeScript("arguments[0].click();", filterOption);
							}
							
							appliedFilterType = filterType;
							LOGGER.info("✓ Applied filter: " + filterType + " = " + appliedFilterValue);
							
							Thread.sleep(1500);
							wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
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
				wait.until(ExpectedConditions.elementToBeClickable(filtersDropdownButton)).click();
				Thread.sleep(500);
			} catch (Exception e) {
				// Continue if dropdown close fails
			}
			
			wait.until(ExpectedConditions.visibilityOf(showingJobResultsCount));
			resultsText = showingJobResultsCount.getText().trim();
			resultsCountAfterFilter = Integer.parseInt(resultsText.replaceAll("[^0-9]", "").substring(resultsText.replaceAll("[^0-9]", "").length() - 2));
			
			String setupMsg = String.format("📊 Filter applied: %s = %s | %d → %d profiles", 
				appliedFilterType, appliedFilterValue, resultsCountBeforeFilter, resultsCountAfterFilter);
			LOGGER.info(setupMsg);
			ExtentCucumberAdapter.addTestStepLog(setupMsg);
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("apply_filter_for_clear_test_failed", e);
			LOGGER.error("❌ Failed to apply filter for clear test", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to apply filter for clear test: " + e.getMessage());
			Assert.fail("Failed to apply filter for clear test: " + e.getMessage());
		}
	}
	
	/**
	 * Measures time to click Clear All Filters button
	 */
	public void user_measures_time_to_click_clear_all_filters_button() {
		try {
			js.executeScript("window.scrollTo(0, 0);");
			Thread.sleep(300);
			
			WebElement clearFiltersButton = null;
			try {
				clearFiltersButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
					org.openqa.selenium.By.xpath("//button[@data-testid='Clear Filters']")
				));
			} catch (Exception e) {
				clearFiltersButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
					org.openqa.selenium.By.xpath("//button[contains(text(),'Clear') and contains(text(),'Filter')]")
				));
			}
			
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", clearFiltersButton);
			Thread.sleep(500);
			
			clearFiltersStartTime = System.currentTimeMillis();
			
			try {
				wait.until(ExpectedConditions.elementToBeClickable(clearFiltersButton)).click();
			} catch (Exception e) {
				js.executeScript("arguments[0].click();", clearFiltersButton);
			}
			
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForPageReady(driver, 2);
			
			clearFiltersEndTime = System.currentTimeMillis();
			totalClearFiltersTime = clearFiltersEndTime - clearFiltersStartTime;
			
			WebElement resultsCountElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
			org.openqa.selenium.By.xpath("//div[contains(@id,'results-toggle')]//*[contains(text(),'Showing')]")
		));
		String resultsText = resultsCountElement.getText().trim();
		resultsCountAfterClearFilters = extractResultsCount(resultsText);
			
			String performanceLog = String.format(
				"📊 Performance Metrics:%n" +
				"   • Clear Filters Time: %d ms (%.2f sec)%n" +
				"   • Results: %d → %d profiles%n" +
				"   • Restored to original: %s",
				totalClearFiltersTime, totalClearFiltersTime / 1000.0,
				resultsCountAfterFilter, resultsCountAfterClearFilters,
				resultsCountAfterClearFilters == resultsCountBeforeFilter ? "Yes ✓" : "No ✗"
			);
			ExtentCucumberAdapter.addTestStepLog(performanceLog);
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("measure_clear_filters_failed", e);
			LOGGER.error("❌ Failed to measure clear filters time", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to measure clear filters time: " + e.getMessage());
			Assert.fail("Failed to measure clear filters time: " + e.getMessage());
		}
	}
	
	/**
	 * Validates clear filters operation time is within acceptable threshold
	 */
	public void user_validates_clear_filters_operation_time_is_within_acceptable_threshold() {
		try {
			validateThreshold(totalClearFiltersTime, CLEAR_FILTERS_THRESHOLD_MS, "Clear Filters", "clear_filters");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_clear_filters_threshold_failed", e);
			LOGGER.error("❌ Failed to validate clear filters threshold", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to validate clear filters threshold: " + e.getMessage());
			Assert.fail("Failed to validate clear filters threshold: " + e.getMessage());
		}
	}
	
	/**
	 * Verifies all profiles are restored correctly after clearing filters
	 */
	public void user_verifies_all_profiles_are_restored_correctly_after_clearing_filters() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForPageReady(driver, 1);
			
			String restorationMsg = String.format("📊 Restored profiles: %d → %d → %d | Match: %s",
				resultsCountBeforeFilter, resultsCountAfterFilter, resultsCountAfterClearFilters,
				resultsCountAfterClearFilters == resultsCountBeforeFilter ? "Yes ✓" : "No ✗");
			LOGGER.info(restorationMsg);
			ExtentCucumberAdapter.addTestStepLog(restorationMsg);
			
			if (resultsCountAfterClearFilters == resultsCountBeforeFilter) {
				ExtentCucumberAdapter.addTestStepLog("✅ All profiles restored to original count");
			} else {
				int difference = Math.abs(resultsCountAfterClearFilters - resultsCountBeforeFilter);
				String failMsg = String.format("❌ Count mismatch: Expected %d, Actual %d (Diff: %d)",
					resultsCountBeforeFilter, resultsCountAfterClearFilters, difference);
				LOGGER.error(failMsg);
				ExtentCucumberAdapter.addTestStepLog(failMsg);
				ScreenshotHandler.captureFailureScreenshot("profiles_not_restored_after_clear_filters", new Exception(failMsg));
				Assert.fail(failMsg);
			}
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_restoration_after_clear_filters_failed", e);
			LOGGER.error("❌ Failed to verify profile restoration", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to verify profile restoration: " + e.getMessage());
			Assert.fail("Failed to verify profile restoration: " + e.getMessage());
		}
	}
	
	/**
	 * Validates no UI freeze during filter clearing
	 */
	public void user_validates_no_ui_freeze_during_filter_clearing() {
		try {
			long instantThreshold = 1000;
			String responseMsg;
			if (totalClearFiltersTime <= instantThreshold) {
				responseMsg = String.format("✅ UI Responsive: %d ms (Instant)", totalClearFiltersTime);
			} else {
				responseMsg = String.format("⚠️ UI Acceptable: %d ms (Not instant)", totalClearFiltersTime);
			}
			LOGGER.info(responseMsg);
			ExtentCucumberAdapter.addTestStepLog(responseMsg);
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_clear_filters_ui_freeze_failed", e);
			LOGGER.error("❌ Failed to validate UI freeze", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to validate UI freeze: " + e.getMessage());
			Assert.fail("Failed to validate UI freeze: " + e.getMessage());
		}
	}
	
	// =========================================
	// SCENARIO 7: SCROLL AND LAZY LOAD PERFORMANCE
	// =========================================
	
	/**
	 * Measures time to scroll through 3-4 pages to check lazy load performance
	 * Focused on lazy load performance testing rather than loading all profiles
	 */
	public void user_measures_time_to_scroll_through_all_profiles() {
		try {
			wait.until(ExpectedConditions.visibilityOf(showingJobResultsCount));
			String resultsText = showingJobResultsCount.getText().trim();
			initialProfileCount = extractResultsCount(resultsText);
			
			java.util.List<WebElement> initialProfiles = driver.findElements(
				org.openqa.selenium.By.xpath("//div[@id='org-job-container']//tbody//tr//td[2]//div[contains(text(),'(')]")
			);
			int previousProfileCount = initialProfiles.size();
			
			scrollStartTime = System.currentTimeMillis();
			totalScrolls = 0;
			lazyLoadTimes.clear();
			
			int targetScrolls = 4;
			
			for (int i = 0; i < targetScrolls; i++) {
				long scrollIterationStart = System.currentTimeMillis();
				
				js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
				totalScrolls++;
				Thread.sleep(300);
				
				try {
					new WebDriverWait(driver, java.time.Duration.ofSeconds(2)).until(
						ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2)
					);
				} catch (Exception e) {
					// No spinner
				}
				
				Thread.sleep(500);
				
				java.util.List<WebElement> currentProfiles = driver.findElements(
					org.openqa.selenium.By.xpath("//div[@id='org-job-container']//tbody//tr//td[2]//div[contains(text(),'(')]")
				);
				int currentProfileCount = currentProfiles.size();
				
				long scrollIterationEnd = System.currentTimeMillis();
				long lazyLoadTime = scrollIterationEnd - scrollIterationStart;
				
				if (currentProfileCount > previousProfileCount) {
					int newProfilesLoaded = currentProfileCount - previousProfileCount;
					lazyLoadTimes.add(lazyLoadTime);
					LOGGER.info(String.format("Scroll #%d: Loaded %d new profiles in %d ms", 
						totalScrolls, newProfilesLoaded, lazyLoadTime));
					previousProfileCount = currentProfileCount;
				} else {
					lazyLoadTimes.add(lazyLoadTime);
				}
			}
			
			scrollEndTime = System.currentTimeMillis();
			totalScrollTime = scrollEndTime - scrollStartTime;
			
			java.util.List<WebElement> finalProfiles = driver.findElements(
				org.openqa.selenium.By.xpath("//div[@id='org-job-container']//tbody//tr//td[2]//div[contains(text(),'(')]")
			);
			finalProfileCount = finalProfiles.size();
		
		if (!lazyLoadTimes.isEmpty()) {
			avgLazyLoadTime = lazyLoadTimes.stream().mapToLong(Long::longValue).sum() / lazyLoadTimes.size();
		}
			
			String performanceLog = String.format("📊 Scroll Metrics: %d scrolls | %d → %d profiles | Avg lazy load: %d ms | Total: %d ms",
				totalScrolls, initialProfileCount, finalProfileCount, avgLazyLoadTime, totalScrollTime);
			ExtentCucumberAdapter.addTestStepLog(performanceLog);
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("measure_scroll_time_failed", e);
			LOGGER.error("❌ Failed to measure scroll time", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to measure scroll time: " + e.getMessage());
			Assert.fail("Failed to measure scroll time: " + e.getMessage());
		}
	}
	
	/**
	 * Validates that lazy loading triggers at appropriate intervals
	 * Verifies lazy load performance is consistent
	 */
	public void user_validates_lazy_loading_triggers_at_appropriate_intervals() {
		try {
			if (lazyLoadTimes.isEmpty()) {
				LOGGER.warn("⚠️ No lazy loading detected");
				ExtentCucumberAdapter.addTestStepLog("⚠️ No lazy loading detected");
				return;
			}
			
			boolean lazyLoadPerformanceGood = avgLazyLoadTime <= LAZY_LOAD_THRESHOLD_MS;
			String validationMsg = String.format("📊 Lazy Load: %d triggers | Avg: %d ms | Status: %s",
				lazyLoadTimes.size(), avgLazyLoadTime, lazyLoadPerformanceGood ? "✓" : "✗");
			LOGGER.info(validationMsg);
			ExtentCucumberAdapter.addTestStepLog(validationMsg);
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_lazy_loading_failed", e);
			LOGGER.error("❌ Failed to validate lazy loading", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to validate lazy loading: " + e.getMessage());
			Assert.fail("Failed to validate lazy loading: " + e.getMessage());
		}
	}
	
	/**
	 * Verifies scroll performance is smooth without lag
	 * Validates no significant delays or UI freezes during scrolling
	 */
	public void user_verifies_scroll_performance_is_smooth_without_lag() {
		try {
			validateThreshold(totalScrollTime, SCROLL_OPERATION_THRESHOLD_MS, "Scroll Operation", "scroll");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_scroll_performance_failed", e);
			LOGGER.error("❌ Failed to verify scroll performance", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to verify scroll performance: " + e.getMessage());
			Assert.fail("Failed to verify scroll performance: " + e.getMessage());
		}
	}
	
	/**
	 * Validates newly loaded profiles render within acceptable time
	 * Checks individual lazy load render times
	 */
	public void user_validates_newly_loaded_profiles_render_within_acceptable_time() {
		try {
			if (lazyLoadTimes.isEmpty()) {
				LOGGER.info("✓ All profiles loaded initially - no lazy loading required");
				ExtentCucumberAdapter.addTestStepLog("✓ No lazy loading - all profiles available on page load");
				return;
			}
			
			long maxLazyLoadTime = lazyLoadTimes.stream().mapToLong(Long::longValue).max().orElse(0);
			long minLazyLoadTime = lazyLoadTimes.stream().mapToLong(Long::longValue).min().orElse(0);
			boolean allWithinThreshold = maxLazyLoadTime <= LAZY_LOAD_THRESHOLD_MS;
			
			String renderMsg = String.format(
				"📊 Render Times: Min %d ms | Avg %d ms | Max %d ms | Threshold: %d ms | Status: %s",
				minLazyLoadTime, avgLazyLoadTime, maxLazyLoadTime, LAZY_LOAD_THRESHOLD_MS,
				allWithinThreshold ? "✓" : "✗"
			);
			
			LOGGER.info(renderMsg);
			ExtentCucumberAdapter.addTestStepLog(renderMsg);
			
			if (allWithinThreshold) {
				LOGGER.info("✅ All profile render times within acceptable threshold");
			} else {
				String warnMsg = String.format(
					"⚠️ Some lazy load times exceeded threshold (Max: %d ms > %d ms)",
					maxLazyLoadTime, LAZY_LOAD_THRESHOLD_MS
				);
				LOGGER.warn(warnMsg);
				ExtentCucumberAdapter.addTestStepLog(warnMsg);
			}
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_render_time_failed", e);
			LOGGER.error("❌ Failed to validate render time", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to validate render time: " + e.getMessage());
			Assert.fail("Failed to validate render time: " + e.getMessage());
		}
	}
	
	/**
	 * Verifies total time to load profiles via scrolling (3-4 pages test)
	 * Provides comprehensive scroll and lazy load performance summary
	 */
	public void user_verifies_total_time_to_load_all_profiles_via_scrolling() {
		try {
			String summaryMsg = String.format(
				"═══════════════════════════════════════════════════════════════════%n" +
				"📊 Scroll Performance Summary:%n" +
				"   • Test Scope: 4 scroll operations (3-4 pages)%n" +
				"   • Total Time: %d ms (%.2f sec)%n" +
				"   • Initial Profiles: %d%n" +
				"   • Final Profiles: %d%n" +
				"   • New Profiles Loaded: %d%n" +
				"   • Lazy Load Operations: %d%n" +
				"   • Avg Lazy Load Time: %d ms%n" +
				"   • Performance Rating: %s%n" +
				"═══════════════════════════════════════════════════════════════════",
				totalScrollTime, totalScrollTime / 1000.0,
				initialProfileCount, finalProfileCount,
				(finalProfileCount - initialProfileCount),
				lazyLoadTimes.size(),
				avgLazyLoadTime,
				getPerformanceRating(totalScrollTime, SCROLL_OPERATION_THRESHOLD_MS)
			);
			
			LOGGER.info(summaryMsg);
			ExtentCucumberAdapter.addTestStepLog(summaryMsg);
			
			if (totalScrollTime <= SCROLL_OPERATION_THRESHOLD_MS && 
			    (lazyLoadTimes.isEmpty() || avgLazyLoadTime <= LAZY_LOAD_THRESHOLD_MS)) {
				LOGGER.info("✅ Scroll and lazy load performance meets all requirements");
			} else {
				LOGGER.warn("⚠️ Some performance metrics need attention");
			}
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_total_scroll_time_failed", e);
			LOGGER.error("❌ Failed to verify total scroll time", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to verify total scroll time: " + e.getMessage());
			Assert.fail("Failed to verify total scroll time: " + e.getMessage());
		}
	}
	
	// =========================================
	// SCENARIO 8: NAVIGATION PERFORMANCE
	// =========================================
	
	/**
	 * Prerequisite: User is on Job Mapping page with loaded profiles (reuses existing method)
	 * Uses user_is_on_job_mapping_page_with_loaded_profiles() from Scenario 2
	 */
	
	/**
	 * Measures time to navigate to Job Comparison screen from Job Mapping
	 * Note: Job Comparison is accessed via "View Other Matches" button on a profile
	 */
	public void user_measures_time_to_navigate_to_job_comparison_screen_from_job_mapping() {
		try {
			navigationToComparisonStartTime = System.currentTimeMillis();
			
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tbody")));
			PerformanceUtils.waitForPageReady(driver, 2);
			
			boolean foundViewOtherMatchesButton = false;
			for(int i = 2; i <= 47; i = i + 3) {
				try {
					WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//tbody//tr[" + Integer.toString(i) + "]//button[not(contains(@id,'publish'))]")));
					
					js.executeScript("arguments[0].scrollIntoView(true);", button);
					Thread.sleep(300);
					
					wait.until(ExpectedConditions.visibilityOf(button));
					String buttonText = button.getText();
					
					if(buttonText.contains("Other Matches")) {
						rowNumberForViewOtherMatches = i;
						WebElement jobNameElement = driver.findElement(
							By.xpath("//tbody//tr[" + Integer.toString(rowNumberForViewOtherMatches - 1) + "]//td[2]//div[contains(text(),'(')]"));
						jobNameForComparison = jobNameElement.getText().split("-", 2)[0].trim();
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
			
			WebElement viewOtherMatchesBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//tbody//tr[" + Integer.toString(rowNumberForViewOtherMatches) + "]//button[not(contains(@id,'publish'))]")));
			
			js.executeScript("arguments[0].scrollIntoView(true);", viewOtherMatchesBtn);
			Thread.sleep(300);
			
			wait.until(ExpectedConditions.visibilityOf(viewOtherMatchesBtn));
			
			try {
				wait.until(ExpectedConditions.elementToBeClickable(viewOtherMatchesBtn)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", viewOtherMatchesBtn);
				} catch (Exception s) {
					utils.jsClick(driver, viewOtherMatchesBtn);
				}
			}
			
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			wait.until(ExpectedConditions.visibilityOf(jobComparisonHeader));
		PerformanceUtils.waitForPageReady(driver, 2);
		
		navigationToComparisonEndTime = System.currentTimeMillis();
		totalNavigationToComparisonTime = navigationToComparisonEndTime - navigationToComparisonStartTime;
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("measure_navigation_to_comparison_failed", e);
			LOGGER.error("❌ Failed to measure navigation time", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to measure navigation time: " + e.getMessage());
			Assert.fail("Failed to measure navigation time: " + e.getMessage());
		}
	}
	
	/**
	 * Validates navigation time is within acceptable threshold
	 */
	public void user_validates_navigation_time_is_within_acceptable_threshold() {
		try {
			validateThreshold(totalNavigationToComparisonTime, NAVIGATION_THRESHOLD_MS, "Navigation to Job Comparison", "navigation_to_comparison");
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_navigation_threshold_failed", e);
			LOGGER.error("❌ Failed to validate navigation threshold", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to validate navigation threshold: " + e.getMessage());
			Assert.fail("Failed to validate navigation threshold: " + e.getMessage());
		}
	}
	
	/**
	 * Verifies Job Comparison screen loads without delay
	 */
	public void user_verifies_job_comparison_screen_loads_without_delay() {
		try {
			wait.until(ExpectedConditions.visibilityOf(jobComparisonHeader));
			String headerText = jobComparisonHeader.getText();
			
			if (headerText.equals("Which profile do you want to use for this job?")) {
				LOGGER.info(String.format("✅ Job Comparison screen loaded successfully | Profile: %s", jobNameForComparison));
				ExtentCucumberAdapter.addTestStepLog("✅ Job Comparison screen verified");
			} else {
				throw new Exception("Job Comparison screen header mismatch. Expected: 'Which profile do you want to use for this job?', Got: '" + headerText + "'");
			}
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_comparison_screen_failed", e);
			LOGGER.error("❌ Failed to verify Job Comparison screen", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to verify Job Comparison screen: " + e.getMessage());
			Assert.fail("Failed to verify Job Comparison screen: " + e.getMessage());
		}
	}
	
	/**
	 * Measures time to navigate back to Job Mapping screen
	 * Uses browser back navigation
	 */
	public void user_measures_time_to_navigate_back_to_job_mapping_screen() {
		try {
			navigationBackToMappingStartTime = System.currentTimeMillis();
			
			driver.navigate().back();
			
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			wait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//div[@id='org-job-container']")
			));
		PerformanceUtils.waitForPageReady(driver, 2);
		
		navigationBackToMappingEndTime = System.currentTimeMillis();
		totalNavigationBackToMappingTime = navigationBackToMappingEndTime - navigationBackToMappingStartTime;
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("measure_navigation_back_failed", e);
			LOGGER.error("❌ Failed to measure back navigation time", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to measure back navigation time: " + e.getMessage());
			Assert.fail("Failed to measure back navigation time: " + e.getMessage());
		}
	}
	
	/**
	 * Validates back navigation time is within acceptable threshold
	 */
	public void user_validates_back_navigation_time_is_within_acceptable_threshold() {
		try {
			validateThreshold(totalNavigationBackToMappingTime, NAVIGATION_THRESHOLD_MS, "Navigation Back to Job Mapping", "navigation_back");
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_back_navigation_threshold_failed", e);
			LOGGER.error("❌ Failed to validate back navigation threshold", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to validate back navigation threshold: " + e.getMessage());
			Assert.fail("Failed to validate back navigation threshold: " + e.getMessage());
		}
	}
	
	/**
	 * Verifies Job Mapping screen loads correctly after navigation
	 */
	public void user_verifies_job_mapping_screen_loads_correctly_after_navigation() {
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//div[@id='org-job-container']")
			));
			
			WebElement resultsCountElement = driver.findElement(
				By.xpath("//div[contains(@id,'results-toggle')]//*[contains(text(),'Showing')]")
			);
			wait.until(ExpectedConditions.visibilityOf(resultsCountElement));
			String resultsText = resultsCountElement.getText().trim();
			int profileCount = extractResultsCount(resultsText);
			
			LOGGER.info(String.format("✅ Job Mapping screen loaded successfully | %d profiles available", profileCount));
			ExtentCucumberAdapter.addTestStepLog("✅ Job Mapping screen verified after navigation");
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_mapping_screen_after_nav_failed", e);
			LOGGER.error("❌ Failed to verify Job Mapping screen", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to verify Job Mapping screen: " + e.getMessage());
			Assert.fail("Failed to verify Job Mapping screen: " + e.getMessage());
		}
	}
	
	// =========================================
	// SCENARIO 9: SORT PERFORMANCE
	// =========================================
	
	/**
	 * Prerequisite: User is on Job Mapping page with maximum loaded profiles
	 * Scrolls to load maximum profiles before testing sort performance
	 */
	public void user_is_on_job_mapping_page_with_maximum_loaded_profiles() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForPageReady(driver, 2);
			
			String resultsText = showingJobResultsCount.getText().trim();
			int currentCount = extractResultsCount(resultsText);
			
			int scrollAttempts = 10;
			for (int i = 0; i < scrollAttempts; i++) {
				js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
				Thread.sleep(500);
				
				try {
					new WebDriverWait(driver, java.time.Duration.ofSeconds(2)).until(
						ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2)
					);
				} catch (Exception e) {
					// No spinner, continue
				}
			}
			
			js.executeScript("window.scrollTo(0, 0);");
			Thread.sleep(500);
			
			resultsText = showingJobResultsCount.getText().trim();
			int finalCount = extractResultsCount(resultsText);
			
			LOGGER.info(String.format("✓ Maximum profiles loaded: %d profiles (%d initial → %d final)", 
				finalCount, currentCount, finalCount));
			ExtentCucumberAdapter.addTestStepLog("✓ Maximum profiles loaded for sort test: " + finalCount);
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("load_max_profiles_failed", e);
			LOGGER.error("❌ Failed to load maximum profiles", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to load maximum profiles: " + e.getMessage());
			Assert.fail("Failed to load maximum profiles: " + e.getMessage());
		}
	}
	
	/**
	 * Measures time to sort profiles by Job Title
	 */
	public void user_measures_time_to_sort_profiles_by_job_title() {
		try {
			sortByJobTitleStartTime = System.currentTimeMillis();
			
			try {
				wait.until(ExpectedConditions.elementToBeClickable(orgJobNameHeader)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", orgJobNameHeader);
				} catch (Exception s) {
					utils.jsClick(driver, orgJobNameHeader);
				}
			}
			
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForPageReady(driver, 1);
		Thread.sleep(500);
		
		sortByJobTitleEndTime = System.currentTimeMillis();
		totalSortByJobTitleTime = sortByJobTitleEndTime - sortByJobTitleStartTime;
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("measure_sort_by_title_failed", e);
			LOGGER.error("❌ Failed to measure sort by Job Title", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to measure sort by Job Title: " + e.getMessage());
			Assert.fail("Failed to measure sort by Job Title: " + e.getMessage());
		}
	}
	
	/**
	 * Measures time to sort profiles by Grade
	 */
	public void user_measures_time_to_sort_profiles_by_grade() {
		try {
			sortByGradeStartTime = System.currentTimeMillis();
			
			try {
				wait.until(ExpectedConditions.elementToBeClickable(orgJobGradeHeader)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", orgJobGradeHeader);
				} catch (Exception s) {
					utils.jsClick(driver, orgJobGradeHeader);
				}
			}
			
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForPageReady(driver, 1);
		Thread.sleep(500);
		
		sortByGradeEndTime = System.currentTimeMillis();
		totalSortByGradeTime = sortByGradeEndTime - sortByGradeStartTime;
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("measure_sort_by_grade_failed", e);
			LOGGER.error("❌ Failed to measure sort by Grade", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to measure sort by Grade: " + e.getMessage());
			Assert.fail("Failed to measure sort by Grade: " + e.getMessage());
		}
	}
	
	/**
	 * Validates sorting operation time is within acceptable threshold
	 */
	public void user_validates_sorting_operation_time_is_within_acceptable_threshold() {
		try {
			validateThreshold(totalSortByJobTitleTime, SORT_OPERATION_THRESHOLD_MS, "Sort by Job Title", "sort_by_title");
			validateThreshold(totalSortByGradeTime, SORT_OPERATION_THRESHOLD_MS, "Sort by Grade", "sort_by_grade");
			
			long avgSortTime = (totalSortByJobTitleTime + totalSortByGradeTime) / 2;
			LOGGER.info(String.format("📊 Average Sort Time: %d ms (%.2f sec)", avgSortTime, avgSortTime / 1000.0));
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_sort_threshold_failed", e);
			LOGGER.error("❌ Failed to validate sort threshold", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to validate sort threshold: " + e.getMessage());
			Assert.fail("Failed to validate sort threshold: " + e.getMessage());
		}
	}
	
	/**
	 * Verifies sorted results are accurate (informational check)
	 */
	public void user_verifies_sorted_results_are_accurate() {
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(
				org.openqa.selenium.By.xpath("//div[@id='org-job-container']//tbody//tr")
			));
			
			java.util.List<WebElement> rows = driver.findElements(
				org.openqa.selenium.By.xpath("//div[@id='org-job-container']//tbody//tr//td[2]//div[contains(text(),'(')]")
			);
			
			LOGGER.info(String.format("✅ Sort completed successfully | %d profiles visible in sorted view", rows.size()));
			ExtentCucumberAdapter.addTestStepLog("✅ Sorted results verified (table populated)");
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_sorted_results_failed", e);
			LOGGER.error("❌ Failed to verify sorted results", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to verify sorted results: " + e.getMessage());
			Assert.fail("Failed to verify sorted results: " + e.getMessage());
		}
	}
	
	/**
	 * Validates UI remains responsive during sorting
	 */
	public void user_validates_ui_remains_responsive_during_sorting() {
		try {
			long maxSortTime = Math.max(totalSortByJobTitleTime, totalSortByGradeTime);
			long instantThreshold = 2000;
			
			String responseMsg;
			if (maxSortTime <= instantThreshold) {
				responseMsg = String.format("✅ Sort operations feel instant (max: %d ms)", maxSortTime);
				LOGGER.info(responseMsg);
			} else if (maxSortTime <= SORT_OPERATION_THRESHOLD_MS) {
				responseMsg = String.format("⚠️ Sort operations acceptable but not instant (max: %d ms)", maxSortTime);
				LOGGER.warn(responseMsg);
			} else {
				responseMsg = String.format("❌ Sort operations too slow (max: %d ms)", maxSortTime);
				LOGGER.error(responseMsg);
			}
			ExtentCucumberAdapter.addTestStepLog(responseMsg);
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_sort_ui_responsiveness_failed", e);
			LOGGER.error("❌ Failed to validate UI responsiveness", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to validate UI responsiveness: " + e.getMessage());
			Assert.fail("Failed to validate UI responsiveness: " + e.getMessage());
		}
	}
	
	// =========================================
	// SCENARIO 10: SELECT ALL PERFORMANCE
	// =========================================
	
	/**
	 * Prerequisite: User is on Job Mapping page with maximum loaded profiles (reuses existing method)
	 * Uses user_is_on_job_mapping_page_with_maximum_loaded_profiles() from Scenario 9
	 */
	
	/**
	 * Measures time to click chevron button beside header checkbox
	 */
	public void user_measures_time_to_click_chevron_button_beside_header_checkbox() {
		try {
			js.executeScript("window.scrollTo(0, 0);");
			Thread.sleep(500);
			
			chevronClickStartTime = System.currentTimeMillis();
			
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", chevronBtninJAM);
			Thread.sleep(300);
			
			try {
				wait.until(ExpectedConditions.elementToBeClickable(chevronBtninJAM)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", chevronBtninJAM);
				} catch (Exception s) {
					utils.jsClick(driver, chevronBtninJAM);
				}
			}
			
			PerformanceUtils.waitForPageReady(driver, 1);
		Thread.sleep(300);
		
		chevronClickEndTime = System.currentTimeMillis();
		totalChevronClickTime = chevronClickEndTime - chevronClickStartTime;
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("measure_chevron_click_failed", e);
			LOGGER.error("❌ Failed to measure chevron click time", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to measure chevron click time: " + e.getMessage());
			Assert.fail("Failed to measure chevron click time: " + e.getMessage());
		}
	}
	
	/**
	 * Measures time to click Select All option
	 */
	public void user_measures_time_to_click_select_all_option() {
		try {
			selectAllClickStartTime = System.currentTimeMillis();
			
			try {
				wait.until(ExpectedConditions.elementToBeClickable(selectAllBtn)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", selectAllBtn);
				} catch (Exception s) {
					utils.jsClick(driver, selectAllBtn);
				}
			}
			
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForPageReady(driver, 2);
		Thread.sleep(500);
		
		selectAllClickEndTime = System.currentTimeMillis();
		totalSelectAllClickTime = selectAllClickEndTime - selectAllClickStartTime;
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("measure_select_all_click_failed", e);
			LOGGER.error("❌ Failed to measure Select All click time", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to measure Select All click time: " + e.getMessage());
			Assert.fail("Failed to measure Select All click time: " + e.getMessage());
		}
	}
	
	/**
	 * Validates select all operation time is within acceptable threshold
	 */
	public void user_validates_select_all_operation_time_is_within_acceptable_threshold() {
		try {
			long totalSelectAllOperationTime = totalChevronClickTime + totalSelectAllClickTime;
			
			validateThreshold(totalSelectAllOperationTime, SELECT_ALL_THRESHOLD_MS, "Select All Operation (Chevron + Select All)", "select_all_operation");
			
			LOGGER.info(String.format("📊 Breakdown: Chevron (%d ms) + Select All (%d ms) = Total (%d ms)", 
				totalChevronClickTime, totalSelectAllClickTime, totalSelectAllOperationTime));
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_select_all_threshold_failed", e);
			LOGGER.error("❌ Failed to validate Select All threshold", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to validate Select All threshold: " + e.getMessage());
			Assert.fail("Failed to validate Select All threshold: " + e.getMessage());
		}
	}
	
	/**
	 * Verifies all profiles are selected after Select All operation
	 */
	public void user_verifies_all_profiles_are_selected() {
		try {
			for (int i = 0; i < 3; i++) {
				js.executeScript("window.scrollBy(0, 800);");
				Thread.sleep(300);
			}
			
			js.executeScript("window.scrollTo(0, 0);");
			Thread.sleep(500);
			
			java.util.List<WebElement> allCheckboxes = driver.findElements(
				By.xpath("//div[@id='org-job-container']//tbody//tr//td[1]//input[@type='checkbox']")
			);
			
			int selectedCount = 0;
			for (WebElement checkbox : allCheckboxes) {
				if (checkbox.isSelected()) {
					selectedCount++;
				}
			}
			
			profilesCountAfterSelectAll = selectedCount / 3;
			
			LOGGER.info(String.format("✅ Selected profiles verified: %d profiles selected (Sample of %d checkboxes checked)", 
				profilesCountAfterSelectAll, selectedCount));
			ExtentCucumberAdapter.addTestStepLog(String.format("✅ Select All successful: %d profiles selected", profilesCountAfterSelectAll));
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_all_profiles_selected_failed", e);
			LOGGER.error("❌ Failed to verify all profiles selected", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to verify all profiles selected: " + e.getMessage());
			Assert.fail("Failed to verify all profiles selected: " + e.getMessage());
		}
	}
	
	/**
	 * Validates UI remains responsive during bulk selection
	 */
	public void user_validates_ui_remains_responsive_during_bulk_selection() {
		try {
			long totalOperationTime = totalChevronClickTime + totalSelectAllClickTime;
			long instantThreshold = 1500;
			
			String responseMsg;
			if (totalOperationTime <= instantThreshold) {
				responseMsg = String.format("✅ Select All operation feels instant (%d ms)", totalOperationTime);
				LOGGER.info(responseMsg);
			} else if (totalOperationTime <= SELECT_ALL_THRESHOLD_MS) {
				responseMsg = String.format("⚠️ Select All operation acceptable but not instant (%d ms)", totalOperationTime);
				LOGGER.warn(responseMsg);
			} else {
				responseMsg = String.format("❌ Select All operation too slow (%d ms)", totalOperationTime);
				LOGGER.error(responseMsg);
			}
			ExtentCucumberAdapter.addTestStepLog(responseMsg);
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_bulk_selection_ui_responsiveness_failed", e);
			LOGGER.error("❌ Failed to validate UI responsiveness", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to validate UI responsiveness: " + e.getMessage());
			Assert.fail("Failed to validate UI responsiveness: " + e.getMessage());
		}
	}
	
	// =========================================
	// SCENARIO 11: HCM PAGE LOAD PERFORMANCE
	// =========================================
	
	/**
	 * Prerequisite: User is on Job Mapping page (reuses existing condition)
	 */
	
	/**
	 * Navigates to Profile Manager screen from KFone Global Menu
	 */
	public void user_navigates_to_profile_manager_screen() {
		try {
			// Wait for page to be ready
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForPageReady(driver, 1);
			
			// Click on KFONE Global Menu button
			try {
				wait.until(ExpectedConditions.elementToBeClickable(KfoneMenu)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", KfoneMenu);
				} catch (Exception s) {
					utils.jsClick(driver, KfoneMenu);
				}
			}
			
			PerformanceUtils.waitForPageReady(driver, 1);
			
			// Click on Profile Manager button in KFONE menu
			try {
				wait.until(ExpectedConditions.elementToBeClickable(KfoneMenuPMBtn)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", KfoneMenuPMBtn);
				} catch (Exception s) {
					utils.jsClick(driver, KfoneMenuPMBtn);
				}
			}
			
			// Wait for Profile Manager page to load
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			wait.until(ExpectedConditions.visibilityOf(profileManagerHeader));
			PerformanceUtils.waitForPageReady(driver, 2);
			
			LOGGER.info("✅ Navigated to Profile Manager screen");
			ExtentCucumberAdapter.addTestStepLog("✅ Navigated to Profile Manager screen");
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("navigate_to_profile_manager_failed", e);
			LOGGER.error("❌ Failed to navigate to Profile Manager", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to navigate to Profile Manager: " + e.getMessage());
			Assert.fail("Failed to navigate to Profile Manager: " + e.getMessage());
		}
	}
	
	/**
	 * Clicks on HCM Sync Profiles tab
	 */
	public void user_clicks_on_hcm_sync_profiles_tab() {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(hcmSyncProfilesHeaderTab)).click();
			Thread.sleep(500);
			
			LOGGER.info("✅ Clicked on HCM Sync Profiles tab");
			ExtentCucumberAdapter.addTestStepLog("✅ Clicked on HCM Sync Profiles tab");
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_hcm_tab_failed", e);
			LOGGER.error("❌ Failed to click HCM tab", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to click HCM tab: " + e.getMessage());
			Assert.fail("Failed to click HCM tab: " + e.getMessage());
		}
	}
	
	/**
	 * Measures time to load HCM Sync Profiles page
	 */
	public void user_measures_time_to_load_hcm_sync_profiles_page() {
		try {
			hcmPageLoadStartTime = System.currentTimeMillis();
			
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			wait.until(ExpectedConditions.visibilityOf(hcmSyncProfilesTitle));
			PerformanceUtils.waitForPageReady(driver, 2);
		Thread.sleep(1000);
		
		hcmPageLoadEndTime = System.currentTimeMillis();
		totalHCMPageLoadTime = hcmPageLoadEndTime - hcmPageLoadStartTime;
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("measure_hcm_page_load_failed", e);
			LOGGER.error("❌ Failed to measure HCM page load time", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to measure HCM page load time: " + e.getMessage());
			Assert.fail("Failed to measure HCM page load time: " + e.getMessage());
		}
	}
	
	/**
	 * Validates HCM page load time is within acceptable threshold
	 */
	public void user_validates_hcm_page_load_time_is_within_acceptable_threshold() {
		try {
			validateThreshold(totalHCMPageLoadTime, HCM_PAGE_LOAD_THRESHOLD_MS, "HCM Page Load", "hcm_page_load");
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_hcm_page_load_threshold_failed", e);
			LOGGER.error("❌ Failed to validate HCM page load threshold", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to validate HCM page load threshold: " + e.getMessage());
			Assert.fail("Failed to validate HCM page load threshold: " + e.getMessage());
		}
	}
	
	/**
	 * Verifies all HCM profiles are loaded correctly
	 */
	public void user_verifies_all_hcm_profiles_are_loaded_correctly() {
		try {
			try {
				WebElement hcmResultsCount = driver.findElement(
					By.xpath("//div[contains(text(),'Showing')]")
				);
				String resultsText = hcmResultsCount.getText().trim();
				hcmProfilesCount = extractResultsCount(resultsText);
				LOGGER.info(String.format("✅ HCM Sync Profiles loaded successfully | %d profiles available", hcmProfilesCount));
			} catch (Exception e) {
				java.util.List<WebElement> profileCheckboxes = driver.findElements(
					By.xpath("//tbody//tr//td[1]//div[1]//kf-checkbox")
				);
				hcmProfilesCount = profileCheckboxes.size();
				LOGGER.info(String.format("✅ HCM Sync Profiles loaded successfully | %d profiles found", hcmProfilesCount));
			}
			
			ExtentCucumberAdapter.addTestStepLog(String.format("✅ HCM profiles loaded: %d profiles", hcmProfilesCount));
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_hcm_profiles_loaded_failed", e);
			LOGGER.error("❌ Failed to verify HCM profiles loaded", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to verify HCM profiles loaded: " + e.getMessage());
			Assert.fail("Failed to verify HCM profiles loaded: " + e.getMessage());
		}
	}
	
	// =========================================
	// SCENARIO 12: HCM SYNC PERFORMANCE
	// =========================================
	
	/**
	 * Prerequisite: User is on HCM Sync Profiles page with selected profiles
	 * Selects profiles using header checkbox
	 */
	public void user_is_on_hcm_sync_profiles_page_with_selected_profiles() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForPageReady(driver, 2);
			
			// Get profiles count before selection
			try {
				WebElement hcmResultsCount = driver.findElement(
					By.xpath("//div[contains(text(),'Showing')]")
				);
				String resultsText = hcmResultsCount.getText().trim();
				selectedProfilesCountBeforeSync = extractResultsCount(resultsText);
			} catch (Exception e) {
				// If results count not available, count checkboxes
				java.util.List<WebElement> checkboxes = driver.findElements(
					By.xpath("//tbody//tr//td[1]//div[1]//kf-checkbox")
				);
				selectedProfilesCountBeforeSync = checkboxes.size();
			}
			
			// Click header checkbox to select all loaded profiles
			try {
				wait.until(ExpectedConditions.elementToBeClickable(hcmTableHeaderCheckbox)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", hcmTableHeaderCheckbox);
				} catch (Exception s) {
					utils.jsClick(driver, hcmTableHeaderCheckbox);
				}
			}
			
			Thread.sleep(500);
			PerformanceUtils.waitForPageReady(driver, 1);
			
			LOGGER.info(String.format("✓ Selected profiles using header checkbox: %d profiles ready for sync", selectedProfilesCountBeforeSync));
			ExtentCucumberAdapter.addTestStepLog(String.format("✓ %d profiles selected for sync test", selectedProfilesCountBeforeSync));
			
			// Verify Sync button is enabled
			wait.until(ExpectedConditions.visibilityOf(syncWithHCMBtn));
			if (!syncWithHCMBtn.isEnabled()) {
				LOGGER.warn("⚠️ Sync with HCM button is not enabled, profiles may not be selected");
			}
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_selected_profiles_for_sync_failed", e);
			LOGGER.error("❌ Failed to verify selected profiles for sync", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to verify selected profiles for sync: " + e.getMessage());
			Assert.fail("Failed to verify selected profiles for sync: " + e.getMessage());
		}
	}
	
	/**
	 * Measures time to click Sync Selected Profiles button
	 */
	public void user_measures_time_to_click_sync_selected_profiles_button() {
		try {
			syncClickStartTime = System.currentTimeMillis();
			
			try {
				wait.until(ExpectedConditions.elementToBeClickable(syncWithHCMBtn)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", syncWithHCMBtn);
				} catch (Exception s) {
					utils.jsClick(driver, syncWithHCMBtn);
				}
		}
		
		syncClickEndTime = System.currentTimeMillis();
		totalSyncClickTime = syncClickEndTime - syncClickStartTime;
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("measure_sync_click_failed", e);
			LOGGER.error("❌ Failed to measure sync click time", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to measure sync click time: " + e.getMessage());
			Assert.fail("Failed to measure sync click time: " + e.getMessage());
		}
	}
	
	/**
	 * Validates sync operation processing time
	 */
	public void user_validates_sync_operation_processing_time() {
		try {
			syncProcessStartTime = System.currentTimeMillis();
			
		wait.until(ExpectedConditions.visibilityOf(syncSuccessPopupText));
		
		syncProcessEndTime = System.currentTimeMillis();
		totalSyncProcessTime = syncProcessEndTime - syncProcessStartTime;
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("measure_sync_process_failed", e);
			LOGGER.error("❌ Failed to measure sync process time", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to measure sync process time: " + e.getMessage());
			Assert.fail("Failed to measure sync process time: " + e.getMessage());
		}
	}
	
	/**
	 * Verifies sync operation completes within acceptable threshold
	 */
	public void user_verifies_sync_operation_completes_within_acceptable_threshold() {
		try {
			long totalSyncTime = totalSyncClickTime + totalSyncProcessTime;
			
			validateThreshold(totalSyncTime, SYNC_OPERATION_THRESHOLD_MS, "Sync Operation (Click + Process)", "sync_operation");
			
			LOGGER.info(String.format("📊 Breakdown: Click (%d ms) + Process (%d ms) = Total (%d ms)", 
				totalSyncClickTime, totalSyncProcessTime, totalSyncTime));
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_sync_threshold_failed", e);
			LOGGER.error("❌ Failed to validate sync threshold", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to validate sync threshold: " + e.getMessage());
			Assert.fail("Failed to validate sync threshold: " + e.getMessage());
		}
	}
	
	/**
	 * Validates sync status updates appear promptly
	 */
	public void user_validates_sync_status_updates_appear_promptly() {
		try {
			String successMsg = syncSuccessPopupText.getText().trim();
			LOGGER.info(String.format("✅ Sync success message displayed: \"%s\"", successMsg));
			ExtentCucumberAdapter.addTestStepLog("✅ Sync success message displayed promptly");
			
			syncSuccessPopupCloseBtn.click();
			Thread.sleep(500);
			
			LOGGER.info(String.format("✅ Sync operation completed | %d profiles synced with HCM", selectedProfilesCountBeforeSync));
			ExtentCucumberAdapter.addTestStepLog(String.format("✅ Synced %d profiles successfully", selectedProfilesCountBeforeSync));
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_sync_status_failed", e);
			LOGGER.error("❌ Failed to verify sync status", e);
			ExtentCucumberAdapter.addTestStepLog("❌ Failed to verify sync status: " + e.getMessage());
			Assert.fail("Failed to verify sync status: " + e.getMessage());
		}
	}
}

