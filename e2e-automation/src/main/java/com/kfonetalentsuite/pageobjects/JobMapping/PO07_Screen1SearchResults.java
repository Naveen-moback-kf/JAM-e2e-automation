package com.kfonetalentsuite.pageobjects.JobMapping;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.ScreenshotHandler;
import com.kfonetalentsuite.utils.JobMapping.PageObjectHelper;

public class PO07_Screen1SearchResults extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO07_Screen1SearchResults.class);

	public static String[] jobNamesTextInSearchResults;
	private static boolean hasSearchResults = true;

	private static final By JOB_NAMES_IN_RESULTS = By.xpath("//tbody//tr//td[2]//div[contains(text(),'(')]");

	public PO07_Screen1SearchResults() {
		super();
	}

	public static void setHasSearchResults(boolean hasResults) {
		hasSearchResults = hasResults;
	}

	public void user_should_scroll_down_to_view_last_search_result() throws InterruptedException {
		if (!hasSearchResults) {
			PageObjectHelper.log(LOGGER, "SKIPPING scroll step - No search results found (0 results)");
			return;
		}

		try {
			PageObjectHelper.log(LOGGER, "Scrolling down to last search result");

			int maxScrollAttempts = 50;
			int scrollCount = 0;
			int previousShowing = -1;
			int stableCount = 0;

			while (scrollCount < maxScrollAttempts) {
				String resultsCountText = wait.until(ExpectedConditions.visibilityOfElementLocated(Locators.JAMScreen.SHOWING_RESULTS_COUNT)).getText();
				String[] parts = resultsCountText.split(" ");
				int currentShowing = Integer.parseInt(parts[1]);
				int totalResults = Integer.parseInt(parts[3]);

				LOGGER.debug("Scroll attempt {}: Showing {} of {}", scrollCount + 1, currentShowing, totalResults);

				if (currentShowing == totalResults) {
					PageObjectHelper.log(LOGGER, "Scrolled to last search result - " + resultsCountText);
					break;
				}

				if (currentShowing == previousShowing) {
					stableCount++;
					if (stableCount >= 5) {
						LOGGER.warn("Count unchanged after {} attempts. Current: {} of {}", stableCount, currentShowing, totalResults);
						break;
					}
				} else {
					stableCount = 0;
				}

				scrollToBottom();
				safeSleep(3000);
				PerformanceUtils.waitForSpinnersToDisappear(driver, 5);
				PerformanceUtils.waitForPageReady(driver, 2);
				safeSleep(1000);

				previousShowing = currentShowing;
				scrollCount++;
			}

			if (scrollCount >= maxScrollAttempts) {
				LOGGER.warn("Reached maximum scroll attempts ({})", maxScrollAttempts);
			}

			scrollToTop();
		} catch (Exception e) {
			ScreenshotHandler.handleTestFailure("scroll_page_view_last_search_result", e, "Issue scrolling to last search result");
		}
	}

	public void user_should_validate_all_search_results_contains_substring_used_for_searching() {
		if (!hasSearchResults) {
			PageObjectHelper.log(LOGGER, "SKIPPING validation step - No search results found (0 results)");
			return;
		}

		try {
			List<WebElement> allElements = driver.findElements(JOB_NAMES_IN_RESULTS);
			String searchSubstring = PO04_JobMappingPageComponents.jobnamesubstring.get();
			int matchCount = 0;
			int nonMatchCount = 0;

			for (WebElement element : allElements) {
				String text = element.getText();
				if (text.toLowerCase().contains(searchSubstring.toLowerCase())) {
					matchCount++;
					LOGGER.debug("Job Profile '{}' contains substring '{}'", text, searchSubstring);
				} else {
					nonMatchCount++;
					LOGGER.info("Job Profile '{}' DOES NOT contain substring '{}'", text, searchSubstring);
				}
			}

			PageObjectHelper.log(LOGGER, "Search validation complete: " + matchCount + " matches, " + nonMatchCount + " non-matches for substring '" + searchSubstring + "'");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_validate_all_search_results_contains_substring_used_for_searching",
					"Issue validating search results containing substring: " + PO04_JobMappingPageComponents.jobnamesubstring.get(), e);
		}
	}
}
