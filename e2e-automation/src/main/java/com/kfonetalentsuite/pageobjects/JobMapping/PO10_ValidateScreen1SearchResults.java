package com.kfonetalentsuite.pageobjects.JobMapping;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.ScreenshotHandler;
import com.kfonetalentsuite.utils.JobMapping.Utilities;
import com.kfonetalentsuite.utils.PageObjectHelper;
import com.kfonetalentsuite.webdriverManager.DriverManager;

public class PO10_ValidateScreen1SearchResults {
	WebDriver driver = DriverManager.getDriver();

	protected static final Logger LOGGER = (Logger) LogManager.getLogger();
	PO10_ValidateScreen1SearchResults validateScreen1SearchResults;

	public static String[] jobNamesTextInSearchResults;

	// Flag to track if search results exist - used to skip validation when 0
	// results
	private static boolean hasSearchResults = true;

	public PO10_ValidateScreen1SearchResults() throws IOException {
		PageFactory.initElements(driver, this);
	}

	WebDriverWait wait = DriverManager.getWait();
	Utilities utils = new Utilities();
	JavascriptExecutor js = (JavascriptExecutor) driver;

	// XPATHS
	@FindBy(xpath = "//div[@data-testid='loader']//img")
	WebElement pageLoadSpinner2;

	@FindBy(xpath = "//div[contains(@id,'results-toggle')]//*[contains(text(),'Showing')]")
	public WebElement showingJobResultsCount;

	// Methods

	/**
	 * Set the search results flag (called from count verification method)
	 * 
	 * @param hasResults - true if search returned results, false if 0 results
	 */
	public static void setHasSearchResults(boolean hasResults) {
		hasSearchResults = hasResults;
	}

	public void user_should_scroll_down_to_view_last_search_result() throws InterruptedException {
		// SKIP if no search results found
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
				// Read the current count at the START of each iteration
				String resultsCountText = wait.until(ExpectedConditions.visibilityOf(showingJobResultsCount)).getText();
				String[] resultsCountText_split = resultsCountText.split(" ");

				// Parse: "Showing X of Y results" -> resultsCountText_split[1] = X,
				// resultsCountText_split[3] = Y
				int currentShowing = Integer.parseInt(resultsCountText_split[1]);
				int totalResults = Integer.parseInt(resultsCountText_split[3]);

				LOGGER.debug("Scroll attempt {}: Showing {} of {}", scrollCount + 1, currentShowing, totalResults);

				// Check if we've loaded all results
				if (currentShowing == totalResults) {
					String resultsCountText_updated = wait
							.until(ExpectedConditions.visibilityOf(showingJobResultsCount)).getText();
					PageObjectHelper.log(LOGGER, "Scrolled to last search result - " + resultsCountText_updated);
					break;
				}

				// Check if count hasn't changed (page might be stuck)
				if (currentShowing == previousShowing) {
					stableCount++;
					if (stableCount >= 5) {
						LOGGER.warn("Count unchanged after {} attempts. Current: {} of {}", stableCount, currentShowing,
								totalResults);
						break;
					}
				} else {
					stableCount = 0;
				}

				// Scroll down and wait for content to load
				js.executeScript("window.scrollTo(0, document.documentElement.scrollHeight);");

				// Wait for page to load new content
				Thread.sleep(500);

				// Wait for spinner to disappear
				wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));

				// Wait for page to stabilize
				Thread.sleep(1500);
				PerformanceUtils.waitForUIStability(driver, 1);

				previousShowing = currentShowing;
				scrollCount++;
			}

			if (scrollCount >= maxScrollAttempts) {
				LOGGER.warn("Reached maximum scroll attempts ({})", maxScrollAttempts);
			}

			js.executeScript("window.scrollTo(0, 0);"); // Scroll back to top
		} catch (Exception e) {
			ScreenshotHandler.handleTestFailure("scroll_page_view_last_search_result", e,
					"Issue scrolling to last search result");
		}
	}

	public void user_should_validate_all_search_results_contains_substring_used_for_searching() {
		// SKIP if no search results found
		if (!hasSearchResults) {
			PageObjectHelper.log(LOGGER, "SKIPPING validation step - No search results found (0 results)");
			return;
		}

		try {
			List<WebElement> allElements = driver
					.findElements(By.xpath("//tbody//tr//td[2]//div[contains(text(),'(')]"));
			String searchSubstring = PO04_VerifyJobMappingPageComponents.jobnamesubstring.get();
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

			PageObjectHelper.log(LOGGER, "Search validation complete: " + matchCount + " matches, " + nonMatchCount
					+ " non-matches for substring '" + searchSubstring + "'");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"user_should_validate_all_search_results_contains_substring_used_for_searching",
					"Issue validating search results containing substring: "
							+ PO04_VerifyJobMappingPageComponents.jobnamesubstring.get(),
					e);
		}
	}
}
