package com.kfonetalentsuite.pageobjects.JobMapping;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import com.kfonetalentsuite.utils.JobMapping.PageObjectHelper;

public class PO09_FilterPersistence extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO09_FilterPersistence.class);

	private static final By CLEAR_FILTERS_BTN = By.xpath("//button[@data-testid='Clear Filters']");
	private static final By VIEW_PUBLISHED_TOGGLE = By.xpath("//div[contains(@id,'results-toggle')]//label//div//div[2]");
	private static final By ORG_JOB_GRADE_SORT_ICON = By.xpath("//*[@id='org-job-container']/div/table/thead/tr/th[3]/div//span//*[@class='text-blue-600']");
	private static final By MATCHED_SP_GRADE_SORT_ICON = By.xpath("//*[@id='kf-job-container']/div/table/thead/tr/th[2]/div//span//*[@class='text-blue-600']");

	public PO09_FilterPersistence() {
		super();
	}

	public void refresh_job_mapping_page() {
		try {
			refreshPage();
			PageObjectHelper.waitForPageReady(driver, 2);
			// Wait for background API (~100K records) to complete after refresh
			waitForBackgroundDataLoad();
			scrollToTop();
			safeSleep(800);
			LOGGER.info("Refreshed Job Mapping page");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "refresh_job_mapping_page", "Issue refreshing Job Mapping page", e);
		}
	}

	public void verify_applied_filters_persist_on_job_mapping_ui() {
		try {
			scrollToTop();
			Thread.sleep(300);
			waitForSpinners();

			String actualResultsCount = getElementText(Locators.JAMScreen.SHOWING_RESULTS_COUNT);
			waitForElement(CLEAR_FILTERS_BTN);

			if (actualResultsCount.equals(PO04_JobMappingPageComponents.intialResultsCount.get())) {
				throw new AssertionError("Filters NOT persisted - count reverted to unfiltered state: " + actualResultsCount);
			}

			LOGGER.info("Applied Filters Persisted correctly - Results: {}", actualResultsCount);
		} catch (AssertionError e) {
			String actualCount = "";
			try {
				actualCount = driver.findElement(Locators.JAMScreen.SHOWING_RESULTS_COUNT).getText();
			} catch (Exception ex) {
				actualCount = "Unable to read";
			}

			String errorMsg = String.format("Filter Persistence Check Failed:%n  Actual displayed count: %s%n  Initial unfiltered count: %s%n",
					actualCount, PO04_JobMappingPageComponents.intialResultsCount.get());
			PageObjectHelper.handleError(LOGGER, "verify_applied_filters_persist_on_job_mapping_ui", "Applied Filters Not Persisted", new RuntimeException(errorMsg, e));
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_applied_filters_persist_on_job_mapping_ui", "Applied Filters Not Persisted on Job Mapping page", e);
		}
	}

	public void click_on_browser_back_button() {
		try {
			driver.navigate().back();
			PageObjectHelper.waitForPageReady(driver, 2);
			LOGGER.info("Browser back button clicked");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_browser_back_button", "Issue clicking Browser back button", e);
		}
	}

	public void verify_view_published_toggle_button_is_persisted() {
		try {
			PageObjectHelper.waitForPageReady(driver, 2);
			waitForElement(VIEW_PUBLISHED_TOGGLE).isEnabled();
			LOGGER.info("View Published toggle button persisted on Job Mapping page");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_view_published_toggle_button_is_persisted", "View Published toggle button not persisted", e);
		}
	}

	public void verify_applied_sorting_persist_on_job_mapping_ui() {
		try {
			PageObjectHelper.waitForPageReady(driver, 2);
			waitForElement(ORG_JOB_GRADE_SORT_ICON).isDisplayed();
			LOGGER.info("Sorting persisted on Job Mapping page");
		} catch (Exception e) {
			try {
				waitForSpinners();
				PageObjectHelper.waitForPageReady(driver, 2);
				waitForElement(MATCHED_SP_GRADE_SORT_ICON).isDisplayed();
				LOGGER.info("Sorting persisted on Job Mapping page");
			} catch (Exception s) {
				PageObjectHelper.handleError(LOGGER, "verify_applied_sorting_persist_on_job_mapping_ui", "Issue validating Sorting Persistence", s);
			}
		}
	}

	public void user_is_in_job_mapping_page_with_grades_filters_applied() {
		try {
			waitForSpinners();
		} catch (Exception e) {
			// Spinner might not be present
		}
		PageObjectHelper.waitForPageReady(driver, 2);

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		try {
			WebElement resultsElement = PageObjectHelper.waitForVisible(wait, Locators.JAMScreen.SHOWING_RESULTS_COUNT);
			String currentCount = resultsElement.getText();

			if (currentCount != null && !currentCount.isEmpty() && currentCount.contains("Showing")) {
				PO04_JobMappingPageComponents.initialFilteredResultsCount.set(currentCount);
			}
		} catch (Exception e) {
			// Will retry in verify method
		}
		LOGGER.info("User is in Job Mapping page with Grades Filters applied");
	}

	public void user_is_in_job_mapping_page_with_mapping_status_filters_applied() {
		try {
			waitForSpinners();
		} catch (Exception e) {
			// Continue
		}
		PageObjectHelper.waitForPageReady(driver, 2);

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		try {
			String currentCount = getElementText(Locators.JAMScreen.SHOWING_RESULTS_COUNT);
			if (currentCount != null && !currentCount.isEmpty() && currentCount.contains("Showing")) {
				PO04_JobMappingPageComponents.initialFilteredResultsCount.set(currentCount);
			}
		} catch (Exception e) {
			// Will retry
		}

		LOGGER.info("User is in Job Mapping page with Mapping Status Filters applied");
	}

	public void user_is_in_view_published_screen_with_grades_filters_applied() {
		try {
			waitForSpinners();
		} catch (Exception e) {
			// Continue
		}
		PageObjectHelper.waitForPageReady(driver, 2);

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		// Capture the current filtered count if not already set
		try {
			String currentCount = getElementText(Locators.JAMScreen.SHOWING_RESULTS_COUNT);
			if (currentCount != null && !currentCount.isEmpty() && currentCount.contains("Showing")) {
				if (PO04_JobMappingPageComponents.initialFilteredResultsCount.get() == null) {
					PO04_JobMappingPageComponents.initialFilteredResultsCount.set(currentCount);
				}
			}
		} catch (Exception e) {
			LOGGER.debug("Could not capture results count: {}", e.getMessage());
		}

		LOGGER.info("User is in View Published screen with Grades filters applied - Using initial count: {}",
				PO04_JobMappingPageComponents.initialFilteredResultsCount.get());
	}

	public void user_is_in_view_published_screen_with_multiple_filters_applied() {
		try {
			waitForSpinners();
		} catch (Exception e) {
			// Continue
		}
		PageObjectHelper.waitForPageReady(driver, 2);

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		// Capture the current filtered count if not already set
		try {
			String currentCount = getElementText(Locators.JAMScreen.SHOWING_RESULTS_COUNT);
			if (currentCount != null && !currentCount.isEmpty() && currentCount.contains("Showing")) {
				if (PO04_JobMappingPageComponents.initialFilteredResultsCount.get() == null) {
					PO04_JobMappingPageComponents.initialFilteredResultsCount.set(currentCount);
				}
			}
		} catch (Exception e) {
			LOGGER.debug("Could not capture results count: {}", e.getMessage());
		}

		LOGGER.info("User is in View Published screen with multiple filters applied - Using initial count: {}",
				PO04_JobMappingPageComponents.initialFilteredResultsCount.get());
	}

	public void user_is_in_job_mapping_page_with_multi_level_sorting_applied() {
		PageObjectHelper.waitForPageReady(driver, 1);
		LOGGER.info("User is in Job Mapping page with multi-level sorting applied");
	}

	public void user_is_in_job_mapping_page_with_sorting_and_filters_applied() {
		PageObjectHelper.waitForPageReady(driver, 1);
		LOGGER.info("User is in Job Mapping page with sorting and filters applied");
	}

	public void user_is_in_view_published_screen_with_sorting_applied() {
		PageObjectHelper.waitForPageReady(driver, 1);
		LOGGER.info("User is in View Published screen with sorting applied");
	}
}
