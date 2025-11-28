package com.kfonetalentsuite.pageobjects.JobMapping;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.Utilities;
import com.kfonetalentsuite.utils.PageObjectHelper;
import com.kfonetalentsuite.webdriverManager.DriverManager;

public class PO12_ValidatePersistanceOfFilters {
	WebDriver driver = DriverManager.getDriver();

	protected static final Logger LOGGER = (Logger) LogManager.getLogger();
	PO12_ValidatePersistanceOfFilters validatePersistanceOfFilters;

	public PO12_ValidatePersistanceOfFilters() throws IOException {
		// super();
		PageFactory.initElements(driver, this);
	}

	WebDriverWait wait = DriverManager.getWait();
	Utilities utils = new Utilities();
	JavascriptExecutor js = (JavascriptExecutor) driver;

	// XPATHs
	@FindBy(xpath = "//div[@data-testid='loader']//img")
	WebElement pageLoadSpinner2;

	@FindBy(xpath = "//button[@data-testid='Clear Filters']")
	WebElement clearFiltersBtn;

	@FindBy(xpath = "//div[contains(@id,'results-toggle')]//*[contains(text(),'Showing')]")
	public WebElement showingJobResultsCount;

	@FindBy(xpath = "//div[contains(@id,'results-toggle')]//label//div//div[2]")
	public WebElement viewPublishedToggleBtn;

	@FindBy(xpath = "//*[@id='org-job-container']/div/table/thead/tr/th[3]/div//span//*[@class='text-blue-600']")
	public WebElement orgJobGradeSortIcon;

	@FindBy(xpath = "//*[@id='kf-job-container']/div/table/thead/tr/th[2]/div//span//*[@class='text-blue-600']")
	public WebElement matchedSPGradeSortIcon;

	// Methods
	public void refresh_job_mapping_page() {
		try {
			driver.navigate().refresh();
			// OPTIMIZED: Single comprehensive wait
			PerformanceUtils.waitForPageReady(driver, 2);

			// CRITICAL: Scroll to top immediately to prevent lazy loading in headless mode
			js.executeScript("window.scrollTo(0, 0);");

			// Additional wait for headless mode to ensure page stabilizes at initial state
			try {
				Thread.sleep(800);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}

			PageObjectHelper.log(LOGGER, "Refreshed Job Mapping page");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "refresh_job_mapping_page", "Issue refreshing Job Mapping page", e);
		}
	}

	public void verify_applied_filters_persist_on_job_mapping_ui() {
		try {
			js.executeScript("window.scrollTo(0, 0);");
			Thread.sleep(300);

			// Wait for page to be ready
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);

			// Capture the actual results count after navigation/refresh
			String actualResultsCount = wait.until(ExpectedConditions.visibilityOf(showingJobResultsCount)).getText();

			wait.until(ExpectedConditions.visibilityOf(clearFiltersBtn));

			String displayedCount = actualResultsCount;
			LOGGER.info("✓ Filters persisted - Displayed count after navigation: {}", displayedCount);

			if (displayedCount.equals(PO04_VerifyJobMappingPageComponents.intialResultsCount.get())) {
				throw new AssertionError(
						"Filters NOT persisted - count reverted to unfiltered state: " + displayedCount);
			}

			LOGGER.info("✓ Applied Filters Persisted correctly - Results: {}", displayedCount);

		} catch (AssertionError e) {
			// Assertion failed - log detailed error
			String actualCount = "";
			try {
				actualCount = showingJobResultsCount.getText();
			} catch (Exception ex) {
				actualCount = "Unable to read";
			}

			String errorMsg = String.format(
					"Filter Persistence Check Failed:%n" + "  Actual displayed count: %s%n"
							+ "  Initial unfiltered count: %s%n"
							+ "This indicates filters did not persist correctly after navigation.",
					actualCount, PO04_VerifyJobMappingPageComponents.intialResultsCount.get());
			PageObjectHelper.handleError(LOGGER, "verify_applied_filters_persist_on_job_mapping_ui",
					"Applied Filters Not Persisted on Job Mapping page", new RuntimeException(errorMsg, e));
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_applied_filters_persist_on_job_mapping_ui",
					"Applied Filters Not Persisted on Job Mapping page", e);
		}
	}

	public void click_on_browser_back_button() {
		try {
			driver.navigate().back();
			// OPTIMIZED: Single comprehensive wait
			PerformanceUtils.waitForPageReady(driver, 2);
			PageObjectHelper.log(LOGGER, "Browser back button clicked");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_browser_back_button", "Issue clicking Browser back button",
					e);
		}
	}

	public void verify_view_published_toggle_button_is_persisted() {
		try {
			// OPTIMIZED: Single comprehensive wait
			PerformanceUtils.waitForPageReady(driver, 2);
			wait.until(ExpectedConditions.visibilityOf(viewPublishedToggleBtn)).isEnabled();
			PageObjectHelper.log(LOGGER, "View Published toggle button persisted on Job Mapping page");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_view_published_toggle_button_is_persisted",
					"View Published toggle button not persisted on Job Mapping page", e);
		}
	}

	public void verify_applied_sorting_persist_on_job_mapping_ui() {
		try {
			// OPTIMIZED: Single comprehensive wait
			PerformanceUtils.waitForPageReady(driver, 2);
			wait.until(ExpectedConditions.visibilityOf(orgJobGradeSortIcon)).isDisplayed();
			PageObjectHelper.log(LOGGER, "Sorting persisted on Job Mapping page");
		} catch (Exception e) {
			try {
				PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
				PerformanceUtils.waitForPageReady(driver, 2);
				wait.until(ExpectedConditions.visibilityOf(matchedSPGradeSortIcon)).isDisplayed();
				PageObjectHelper.log(LOGGER, "Sorting persisted on Job Mapping page");
			} catch (Exception s) {
				PageObjectHelper.handleError(LOGGER, "verify_applied_sorting_persist_on_job_mapping_ui",
						"Issue validating Sorting Persistence in Job Mapping page", s);
			}
		}
	}

	public void user_is_in_job_mapping_page_with_grades_filters_applied() {
		// Wait for page to be fully ready (critical for headless mode)
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
		} catch (Exception e) {
			// Spinner might not be present, continue
		}
		PerformanceUtils.waitForPageReady(driver, 2);

		// Additional wait for headless mode
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		// Capture current filtered count as baseline (if not already set)
		try {
			WebElement resultsElement = wait.until(ExpectedConditions.visibilityOf(showingJobResultsCount));
			String currentCount = resultsElement.getText();

			if (currentCount != null && !currentCount.isEmpty() && currentCount.contains("Showing")) {
				PO04_VerifyJobMappingPageComponents.initialFilteredResultsCount.set(currentCount);
			}
		} catch (Exception e) {
			// Will retry in verify method if needed
		}
		LOGGER.info("User is in Job Mapping page with Grades Filters applied");
	}

	public void user_is_in_job_mapping_page_with_mapping_status_filters_applied() {
		// Wait for page to be fully ready (critical for headless mode)
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
		} catch (Exception e) {
			// Spinner might not be present, continue
		}
		PerformanceUtils.waitForPageReady(driver, 2);

		// Additional wait for headless mode
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		// Capture current filtered count as baseline (if not already set)
		try {
			WebElement resultsElement = wait.until(ExpectedConditions.visibilityOf(showingJobResultsCount));
			String currentCount = resultsElement.getText();

			if (currentCount != null && !currentCount.isEmpty() && currentCount.contains("Showing")) {
				PO04_VerifyJobMappingPageComponents.initialFilteredResultsCount.set(currentCount);
			}
		} catch (Exception e) {
			// Will retry in verify method if needed
		}

		LOGGER.info("User is in Job Mapping page with Mapping Status Filters applied");
	}

	public void user_is_in_view_published_screen_with_grades_filters_applied() {
		// Wait for page to be fully ready (critical for headless mode)
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
		} catch (Exception e) {
			// Spinner might not be present, continue
		}
		PerformanceUtils.waitForPageReady(driver, 2);

		// Additional wait for headless mode
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		LOGGER.info("User is in View Published screen with Grades filters applied - Using initial count: {}",
				PO04_VerifyJobMappingPageComponents.initialFilteredResultsCount.get());

	}

	public void user_is_in_view_published_screen_with_multiple_filters_applied() {
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
		} catch (Exception e) {

		}
		PerformanceUtils.waitForPageReady(driver, 2);

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		LOGGER.info("User is in View Published screen with multiple filters applied - Using initial count: {}",
				PO04_VerifyJobMappingPageComponents.initialFilteredResultsCount.get());
	}

	public void user_is_in_job_mapping_page_with_multi_level_sorting_applied() {
		PerformanceUtils.waitForPageReady(driver, 1);
		LOGGER.info("User is in Job Mapping page with multi-level sorting applied");
	}

	public void user_is_in_job_mapping_page_with_sorting_and_filters_applied() {
		PerformanceUtils.waitForPageReady(driver, 1);
		LOGGER.info("User is in Job Mapping page with sorting and filters applied");
	}

	public void user_is_in_view_published_screen_with_sorting_applied() {
		PerformanceUtils.waitForPageReady(driver, 1);
		LOGGER.info("User is in View Published screen with sorting applied");
	}

}
