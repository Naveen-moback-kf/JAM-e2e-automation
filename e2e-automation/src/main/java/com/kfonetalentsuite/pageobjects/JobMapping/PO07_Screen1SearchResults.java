package com.kfonetalentsuite.pageobjects.JobMapping;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
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
			LOGGER.info("SKIPPING scroll step - No search results found (0 results)");
			return;
		}

		try {
			LOGGER.info("=== Starting Scroll Operation to Load All Search Results ===");

			int maxScrollAttempts = 50;
			int scrollCount = 0;
			int previousShowing = -1;
			int stableCount = 0;
			
			// Get initial count
			String initialCountText = PageObjectHelper.waitForVisible(wait, Locators.JAMScreen.SHOWING_RESULTS_COUNT).getText();
			LOGGER.info("Initial results count: {}", initialCountText);

			while (scrollCount < maxScrollAttempts) {
				String resultsCountText = PageObjectHelper.waitForVisible(wait, Locators.JAMScreen.SHOWING_RESULTS_COUNT).getText();
				String[] parts = resultsCountText.split(" ");
				int currentShowing = Integer.parseInt(parts[1]);
				int totalResults = Integer.parseInt(parts[3]);
				
				int remainingToLoad = totalResults - currentShowing;
				double progressPercent = (currentShowing * 100.0) / totalResults;

				LOGGER.info("Scroll #{}: Loaded {} of {} results ({}/{}% complete, {} remaining)", 
					scrollCount + 1, currentShowing, totalResults, 
					String.format("%.1f", progressPercent), remainingToLoad);

				if (currentShowing == totalResults) {
					LOGGER.info("✓ SUCCESS: All {} results loaded after {} scroll attempts", totalResults, scrollCount + 1);
					LOGGER.info("Final count: {}", resultsCountText);
					break;
				}

				if (currentShowing == previousShowing) {
					stableCount++;
					LOGGER.debug("Progress stalled - count unchanged ({}/5 stable checks)", stableCount);
					if (stableCount >= 5) {
						LOGGER.warn("⚠ Scroll stalled after {} attempts. Loaded {} of {} results ({} still pending)", 
							stableCount, currentShowing, totalResults, remainingToLoad);
						LOGGER.warn("This may indicate slow loading or all visible results have been loaded");
						break;
					}
				} else {
					if (stableCount > 0) {
						LOGGER.debug("Progress resumed - loaded {} new results", currentShowing - previousShowing);
					}
					stableCount = 0;
				}

				LOGGER.debug("Scrolling to bottom...");
				scrollToBottom();
				safeSleep(3000);
				
				LOGGER.debug("Waiting for spinners and page ready...");
				PageObjectHelper.waitForSpinnersToDisappear(driver, 5);
				PageObjectHelper.waitForPageReady(driver, 2);
				safeSleep(1000);

				previousShowing = currentShowing;
				scrollCount++;
			}

			if (scrollCount >= maxScrollAttempts) {
				LOGGER.warn("⚠ Reached maximum scroll attempts ({}) - stopping scroll operation", maxScrollAttempts);
				String finalCount = PageObjectHelper.waitForVisible(wait, Locators.JAMScreen.SHOWING_RESULTS_COUNT).getText();
				LOGGER.warn("Final loaded count: {}", finalCount);
			}

			LOGGER.info("Scrolling back to top...");
			scrollToTop();
			LOGGER.info("=== Scroll Operation Complete ===");
		} catch (Exception e) {
			LOGGER.error("❌ Error during scroll operation: {}", e.getMessage());
			ScreenshotHandler.handleTestFailure("scroll_page_view_last_search_result", e, "Issue scrolling to last search result");
		}
	}

	public void user_should_validate_all_search_results_contains_substring_used_for_searching() {
		if (!hasSearchResults) {
			LOGGER.info("SKIPPING validation step - No search results found (0 results)");
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

			LOGGER.info("Search validation complete: " + matchCount + " matches, " + nonMatchCount + " non-matches for substring '" + searchSubstring + "'");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_validate_all_search_results_contains_substring_used_for_searching",
					"Issue validating search results containing substring: " + PO04_JobMappingPageComponents.jobnamesubstring.get(), e);
		}
	}
}
