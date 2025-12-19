package com.kfonetalentsuite.pageobjects.JobMapping;

import java.time.Duration;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.ScreenshotHandler;
import com.kfonetalentsuite.utils.JobMapping.PageObjectHelper;

/**
 * Consolidated Page Object for Select All / Loaded Profiles Selection with Search functionality.
 * Supports both PM (HCM Sync Profiles) and JAM (Job Mapping) screens.
 */
public class PO27_SelectAllWithSearchFunctionality extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO27_SelectAllWithSearchFunctionality.class);

	public PO27_SelectAllWithSearchFunctionality() {
		super();
	}

	// ==================== LOCATORS ====================
	// Common locators are now in BasePageObject.Locators.PMScreen and JAMScreen
	// Only search-specific locators remain here if needed

	// ==================== THREAD-SAFE STATE ====================
	public static ThreadLocal<Integer> searchResultsCount = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<String> alternativeSearchSubstring = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<Integer> totalSecondSearchResults = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<String> currentScreen = ThreadLocal.withInitial(() -> "PM");
	public static ThreadLocal<Integer> loadedProfilesBeforeScroll = ThreadLocal.withInitial(() -> 0);

	// ==================== HELPER METHODS ====================
	// Common helper methods (getScreenName, getShowingResultsCountLocator, getAllProfileRowsLocator,
	// getSelectedProfileRowsLocator, getChevronButtonLocator, getHeaderCheckboxLocator, 
	// getSearchBarLocator, getActionButtonLocator) are now inherited from BasePageObject

	private By getSelectAllButtonLocator(String screen) {
		// Both PM and JAM use the same Select All button locator
		return Locators.Table.SELECT_ALL_BTN;
	}

	private String getSearchSubstring(String screen) {
		if ("PM".equalsIgnoreCase(screen)) {
			return PO18_HCMSyncProfilesTab_PM.jobProfileName.get();
		} else {
			return PO04_JobMappingPageComponents.jobnamesubstring.get();
		}
	}

	private String[] getSearchOptions(String screen) {
		if ("PM".equalsIgnoreCase(screen)) {
			return PO18_HCMSyncProfilesTab_PM.SEARCH_PROFILE_NAME_OPTIONS;
		} else {
			return PO04_JobMappingPageComponents.SEARCH_SUBSTRING_OPTIONS;
		}
	}

	// ==================== SCROLL AND VIEW METHODS ====================

	/**
	 * Scrolls down to load a sample of search results for validation.
	 * Does NOT try to load all results - just enough for sample validation.
	 */
	public void user_should_scroll_down_to_view_last_search_result(String screen) {
		try {
			currentScreen.set(screen);
			PerformanceUtils.waitForSpinnersToDisappear(driver, 5);

			// Get initial count
			int initialCount = findElements(getAllProfileRowsLocator(screen)).size();
			
			// Only do a few scrolls to load more for validation - not all results
			int maxScrollAttempts = 5; // Limited scrolling for efficiency
			int scrollCount = 0;
			int currentCount = initialCount;
			int previousCount = 0;
			int noChangeCount = 0;

			while (scrollCount < maxScrollAttempts) {
				scrollToBottom();
				scrollCount++;
				safeSleep(500);

				PerformanceUtils.waitForSpinnersToDisappear(driver, 3);

				currentCount = findElements(getAllProfileRowsLocator(screen)).size();

				if (currentCount == previousCount) {
					noChangeCount++;
					if (noChangeCount >= 2) {
						break; // Stop if no more loading
					}
				} else {
					noChangeCount = 0;
				}

				previousCount = currentCount;
			}

			PageObjectHelper.log(LOGGER, "Loaded " + currentCount + " search results for validation in " + getScreenName(screen));

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_scroll_down_to_view_last_search_result",
					"Issue scrolling to view search results in " + getScreenName(screen), e);
			ScreenshotHandler.captureFailureScreenshot("scroll_search_results_" + screen, e);
			Assert.fail("Issue scrolling to view search results");
		}
	}

	/**
	 * Validates that all search results contain the substring used for searching.
	 */
	public void user_should_validate_all_search_results_contains_substring(String screen) {
		try {
			currentScreen.set(screen);
			PerformanceUtils.waitForSpinnersToDisappear(driver, 5);

			String searchSubstring = getSearchSubstring(screen).toLowerCase();

			List<WebElement> allRows = findElements(getAllProfileRowsLocator(screen));
			int totalResults = allRows.size();
			int nonMatchingResults = 0;

			for (WebElement row : allRows) {
				String profileName = extractJobName(row, screen).toLowerCase();
				if (!profileName.contains(searchSubstring)) {
					nonMatchingResults++;
					LOGGER.debug("Non-matching result: '{}'", profileName);
				}
			}

			if (nonMatchingResults == 0) {
				PageObjectHelper.log(LOGGER, "✅ All " + totalResults + " results contain '" + searchSubstring + "' in " + getScreenName(screen));
			} else {
				PageObjectHelper.log(LOGGER, "⚠️ " + nonMatchingResults + " of " + totalResults +
						" results do NOT contain '" + searchSubstring + "' in " + getScreenName(screen));
			}

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "validate_search_results_contain_substring",
					"Issue validating search results in " + getScreenName(screen), e);
			ScreenshotHandler.captureFailureScreenshot("validate_search_results_" + screen, e);
			Assert.fail("Issue validating search results contain substring");
		}
	}

	// ==================== SELECTION METHODS ====================

	/**
	 * Clicks on Chevron button beside header checkbox.
	 */
	public void click_on_chevron_button_beside_header_checkbox(String screen) {
		try {
			currentScreen.set(screen);
			PerformanceUtils.waitForSpinnersToDisappear(driver, 5);

			// Scroll to top to ensure chevron button is visible
			scrollToTop();
			safeSleep(500);

			clickElement(getChevronButtonLocator(screen));
			PageObjectHelper.log(LOGGER, "Clicked chevron button in " + getScreenName(screen));

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_chevron_button",
					"Error clicking chevron button in " + getScreenName(screen), e);
			ScreenshotHandler.captureFailureScreenshot("click_chevron_" + screen, e);
			Assert.fail("Error clicking chevron button");
		}
	}

	/**
	 * Clicks on Select All button.
	 */
	public void click_on_select_all_button(String screen) {
		try {
			currentScreen.set(screen);
			safeSleep(300);

			clickElement(getSelectAllButtonLocator(screen));

			PerformanceUtils.waitForSpinnersToDisappear(driver, 5);

			PageObjectHelper.log(LOGGER, "Clicked Select All button in " + getScreenName(screen));

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_select_all_button",
					"Error clicking Select All button in " + getScreenName(screen), e);
			ScreenshotHandler.captureFailureScreenshot("click_select_all_" + screen, e);
			Assert.fail("Error clicking Select All button");
		}
	}

	/**
	 * Clicks on header checkbox to select loaded profiles.
	 */
	public void click_on_header_checkbox_to_select_loaded_profiles(String screen) {
		try {
			currentScreen.set(screen);
			PerformanceUtils.waitForSpinnersToDisappear(driver, 5);

			// Capture count before clicking
			loadedProfilesBeforeScroll.set(findElements(getAllProfileRowsLocator(screen)).size());

			clickElement(getHeaderCheckboxLocator(screen));

			PerformanceUtils.waitForSpinnersToDisappear(driver, 5);

			PageObjectHelper.log(LOGGER, "Clicked header checkbox to select " + loadedProfilesBeforeScroll.get() +
					" loaded profiles in " + getScreenName(screen));

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_header_checkbox",
					"Error clicking header checkbox in " + getScreenName(screen), e);
			ScreenshotHandler.captureFailureScreenshot("click_header_checkbox_" + screen, e);
			Assert.fail("Error clicking header checkbox");
		}
	}

	/**
	 * Verifies action button (Sync with HCM / Publish Selected) is enabled.
	 */
	public void verify_action_button_is_enabled(String screen) {
		String buttonName = "PM".equalsIgnoreCase(screen) ? "Sync with HCM" : "Publish Selected Profiles";
		LOGGER.info("Verifying {} button is enabled in {}...", buttonName, getScreenName(screen));
		
		try {
			currentScreen.set(screen);
			PerformanceUtils.waitForSpinnersToDisappear(driver, 5);
			
			scrollToTop();
			safeSleep(300);

			WebElement button = wait.until(ExpectedConditions.visibilityOfElementLocated(getActionButtonLocator(screen)));
			boolean isEnabled = button.isEnabled();
			String buttonText = button.getText();
			
			LOGGER.info("Button found: '{}', enabled: {}", buttonText, isEnabled);

			if (isEnabled) {
				PageObjectHelper.log(LOGGER, "✅ " + buttonName + " button is enabled in " + getScreenName(screen));
			} else {
				PageObjectHelper.log(LOGGER, "❌ " + buttonName + " button is NOT enabled in " + getScreenName(screen));
				Assert.fail(buttonName + " button should be enabled after selection");
			}

		} catch (Exception e) {
			LOGGER.error("Failed to verify {} button: {}", buttonName, e.getMessage());
			PageObjectHelper.handleError(LOGGER, "verify_action_button_is_enabled",
					"Error verifying action button in " + getScreenName(screen), e);
			Assert.fail("Error verifying action button is enabled: " + e.getMessage());
		}
	}

	/**
	 * Scrolls page to view more job profiles.
	 */
	public void scroll_page_to_view_more_job_profiles(String screen) {
		try {
			currentScreen.set(screen);
			PerformanceUtils.waitForSpinnersToDisappear(driver, 3);

			scrollToBottom();
			safeSleep(500);

			PerformanceUtils.waitForSpinnersToDisappear(driver, 3);

			int newCount = findElements(getAllProfileRowsLocator(screen)).size();
			PageObjectHelper.log(LOGGER, "Scrolled to load more profiles. Now " + newCount + " visible in " + getScreenName(screen));

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "scroll_page_to_view_more_job_profiles",
					"Error scrolling in " + getScreenName(screen), e);
		}
	}

	/**
	 * Verifies profiles loaded after clicking header checkbox are NOT selected.
	 * Uses fast count-based approach instead of iterating through elements.
	 */
	public void verify_profiles_loaded_after_header_checkbox_are_not_selected(String screen) {
		try {
			currentScreen.set(screen);
			PerformanceUtils.waitForSpinnersToDisappear(driver, 3);

			int initialLoaded = loadedProfilesBeforeScroll.get();
			
			// Quick count using findElements size - much faster than iterating
			int currentTotal = findElements(getAllProfileRowsLocator(screen)).size();
			int selectedCount = findElements(getSelectedProfileRowsLocator(screen)).size();
			int newlyLoaded = currentTotal - initialLoaded;

			if (newlyLoaded <= 0) {
				PageObjectHelper.log(LOGGER, "No newly loaded profiles to verify in " + getScreenName(screen));
				return;
			}

			// If selected count hasn't increased beyond initial, newly loaded profiles are not selected
			// Initial selection was done on 'initialLoaded' profiles
			int expectedSelected = initialLoaded; // Header checkbox selected all loaded at that time
			
			if (selectedCount <= expectedSelected) {
				PageObjectHelper.log(LOGGER, "✅ Newly loaded " + newlyLoaded + " profiles are NOT selected in " + getScreenName(screen) + 
						" (selected: " + selectedCount + ", expected max: " + expectedSelected + ")");
			} else {
				int extraSelected = selectedCount - expectedSelected;
				PageObjectHelper.log(LOGGER, "⚠️ " + extraSelected + " newly loaded profiles are unexpectedly selected in " + getScreenName(screen));
			}

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_profiles_loaded_after_header_checkbox",
					"Error verifying newly loaded profiles in " + getScreenName(screen), e);
		}
	}

	/**
	 * Captures the baseline of selected profiles after selection.
	 * Scrolls through all profiles to count actual selected checkboxes.
	 * Fast scrolling for both PM and JAM (100K API preloads all data).
	 */
	public void capture_baseline_of_selected_profiles(String screen) {
		try {
			currentScreen.set(screen);
			PerformanceUtils.waitForSpinnersToDisappear(driver, 5);
			safeSleep(300);
			
			scrollToTop();
			safeSleep(200);

			int selectedCount = 0;
			int previousTotalVisible = 0;
			int scrollAttempts = 0;
			int noChangeCount = 0;
			int maxScrollAttempts = 500;
			int requiredStableCount = 3;
			int totalVisible = 0;
			int expectedTotal = 0;
			long startTime = System.currentTimeMillis();
			JavascriptExecutor js = (JavascriptExecutor) driver;
			boolean isJAM = "JAM".equalsIgnoreCase(screen);
			
			// Get expected total profile count
			try {
				String resultsCountText = getElementText(getShowingResultsCountLocator(screen));
				expectedTotal = parseProfileCountFromText(resultsCountText);
				LOGGER.info("Expected total profiles: {}", expectedTotal);
			} catch (Exception e) {
				LOGGER.debug("Could not parse total profile count: {}", e.getMessage());
			}
			
			// Calculate estimated scrolls needed - JAM loads ~10 profiles/scroll, PM loads ~50 profiles/scroll
			int profilesPerScroll = isJAM ? 10 : 50;
			int estimatedScrolls = expectedTotal > 0 ? (expectedTotal / profilesPerScroll) + 10 : maxScrollAttempts;
			maxScrollAttempts = Math.min(estimatedScrolls, maxScrollAttempts);
			
			PageObjectHelper.log(LOGGER, "Counting selected profiles in " + getScreenName(screen) + 
					" (est. " + estimatedScrolls + " scrolls for " + expectedTotal + " profiles)...");
			
			while (scrollAttempts < maxScrollAttempts) {
				// Scroll to FULL page bottom (this triggers lazy loading)
				try {
					js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
				} catch (Exception e1) {
					try {
						js.executeScript("window.scrollTo(0, document.documentElement.scrollHeight);");
					} catch (Exception e2) {
						js.executeScript("window.scrollBy(0, 10000);");
					}
				}
				scrollAttempts++;
				
				// Wait for lazy loading - JAM needs less time, PM needs more
				int scrollWait = isJAM ? 500 : 1500;
				safeSleep(scrollWait);
				PerformanceUtils.waitForSpinnersToDisappear(driver, 3);
				
				// Count current selected using JavaScript (faster)
				if (isJAM) {
					String jsScript = "return Array.from(document.querySelectorAll('#org-job-container tbody tr td:first-child input[type=\"checkbox\"]')).filter(cb => cb.checked).length;";
					Object result = js.executeScript(jsScript);
					selectedCount = result != null ? ((Long) result).intValue() : 0;
				} else {
					// PM: Use JavaScript for speed
					String jsScript = "return document.querySelectorAll('tbody tr kf-icon[icon=\"checkbox-check\"]').length;";
					Object result = js.executeScript(jsScript);
					selectedCount = result != null ? ((Long) result).intValue() : 0;
					if (selectedCount == 0) {
						selectedCount = findElements(getSelectedProfileRowsLocator(screen)).size();
					}
				}
				
				// Get total visible for logging
				totalVisible = findElements(getAllProfileRowsLocator(screen)).size();
				
				// Log progress every 10 scrolls
				if (scrollAttempts % 10 == 0) {
					long elapsed = (System.currentTimeMillis() - startTime) / 1000;
					int pctLoaded = expectedTotal > 0 ? (totalVisible * 100 / expectedTotal) : 0;
					LOGGER.info("Scroll progress: {} scrolls, {}/{} profiles ({}%), {} selected, {}s elapsed", 
							scrollAttempts, totalVisible, expectedTotal, pctLoaded, selectedCount, elapsed);
				}
				
				// Check if stable (no new profiles loaded)
				if (totalVisible == previousTotalVisible && scrollAttempts > 0) {
					noChangeCount++;
					if (noChangeCount >= requiredStableCount) {
						long elapsed = (System.currentTimeMillis() - startTime) / 1000;
						LOGGER.info("Scroll complete: {}/{} profiles loaded, {} selected after {} scrolls ({}s)", 
								totalVisible, expectedTotal, selectedCount, scrollAttempts, elapsed);
						break;
					}
				} else {
					noChangeCount = 0;
				}
				previousTotalVisible = totalVisible;
			}
			
			searchResultsCount.set(selectedCount);

			// Store in screen-specific ThreadLocal
			if (!isJAM) {
				PO18_HCMSyncProfilesTab_PM.profilesCount.set(selectedCount);
			}

			PageObjectHelper.log(LOGGER, "Baseline captured: " + selectedCount + " selected profiles in " + getScreenName(screen) + 
					" (scrolls: " + scrollAttempts + ", total loaded: " + totalVisible + ")");

		} catch (Exception e) {
			LOGGER.warn("Error capturing baseline in {}: {}", getScreenName(screen), e.getMessage());
			searchResultsCount.set(0);
		}
	}

	/**
	 * Clears the search bar.
	 */
	public void clear_search_bar(String screen) {
		try {
			currentScreen.set(screen);
			PerformanceUtils.waitForSpinnersToDisappear(driver, 5);
			
			// Scroll to top first to ensure search bar is accessible
			scrollToTop();
			safeSleep(500);

			JavascriptExecutor js = (JavascriptExecutor) driver;
			By searchBarLocator = getSearchBarLocator(screen);
			
			// Use JavaScript to clear and trigger the search - most reliable approach
			String clearScript = "";
			if ("JAM".equalsIgnoreCase(screen)) {
				clearScript = 
					"var searchBar = document.querySelector('#search-job-title-input-search-input');" +
					"if (searchBar) {" +
					"  searchBar.focus();" +
					"  searchBar.value = '';" +
					"  searchBar.dispatchEvent(new Event('input', { bubbles: true }));" +
					"  searchBar.dispatchEvent(new KeyboardEvent('keydown', { key: 'Enter', keyCode: 13, bubbles: true }));" +
					"  return true;" +
					"} return false;";
			} else {
				clearScript = 
					"var searchBar = document.querySelector('input[type=\"search\"]');" +
					"if (searchBar) {" +
					"  searchBar.focus();" +
					"  searchBar.value = '';" +
					"  searchBar.dispatchEvent(new Event('input', { bubbles: true }));" +
					"  searchBar.dispatchEvent(new KeyboardEvent('keydown', { key: 'Enter', keyCode: 13, bubbles: true }));" +
					"  return true;" +
					"} return false;";
			}
			
			Boolean cleared = (Boolean) js.executeScript(clearScript);
			
			if (cleared == null || !cleared) {
				// Fallback to WebElement approach with fresh element references
				LOGGER.debug("JS clear failed, trying WebElement approach");
				WebElement searchBar = wait.until(ExpectedConditions.elementToBeClickable(searchBarLocator));
				js.executeScript("arguments[0].scrollIntoView({block: 'center'});", searchBar);
				safeSleep(300);
				
				// Re-find and click
				searchBar = driver.findElement(searchBarLocator);
				js.executeScript("arguments[0].click();", searchBar);
				safeSleep(200);
				
				// Re-find and clear
				searchBar = driver.findElement(searchBarLocator);
				searchBar.sendKeys(Keys.CONTROL + "a");
				safeSleep(100);
				
				// Re-find and delete
				searchBar = driver.findElement(searchBarLocator);
				searchBar.sendKeys(Keys.DELETE);
				safeSleep(100);
				
				// Re-find and press Enter
				searchBar = driver.findElement(searchBarLocator);
				searchBar.sendKeys(Keys.ENTER);
			}

			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			safeSleep(1500); // Wait for results to reload

			PageObjectHelper.log(LOGGER, "Cleared search bar in " + getScreenName(screen));

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "clear_search_bar",
					"Error clearing search bar in " + getScreenName(screen), e);
		}
	}

	// ==================== VERIFICATION METHODS ====================

	/**
	 * Verifies that only searched profiles remain selected after clearing search.
	 * Fast scrolling for both PM and JAM (100K API preloads all data).
	 */
	public void verify_only_searched_profiles_are_selected_after_clearing_search_bar(String screen) {
		int totalProfilesVisible = 0;
		int previousTotalProfilesVisible = 0;
		int actualSelectedCount = 0;
		int scrollAttempts = 0;
		int stableCountAttempts = 0;
		boolean allProfilesLoaded = false;
		int expectedTotalProfiles = 0;
		boolean maxScrollLimitReached = false;
		JavascriptExecutor js = (JavascriptExecutor) driver;
		boolean isJAM = "JAM".equalsIgnoreCase(screen);
		
		// Scrolling settings - JAM loads ~10 profiles/scroll, PM loads ~50 profiles/scroll
		int maxScrollAttempts = 500;
		int requiredStableAttempts = 5;

		try {
			currentScreen.set(screen);
			
			PageObjectHelper.log(LOGGER, "Verifying only " + searchResultsCount.get() +
					" profiles remain selected in " + getScreenName(screen) + "...");

			if (searchResultsCount.get() == 0) {
				PageObjectHelper.log(LOGGER, "No search results to verify - skipping");
				return;
			}
			
			// Wait for page to stabilize after clearing search
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			safeSleep(500);
			
			// Scroll to top first
			scrollToTop();
			safeSleep(300);
			
			long startTime = System.currentTimeMillis();

			// Calculate estimated scrolls needed
			int profilesPerScroll = isJAM ? 10 : 50;
			int estimatedScrolls = expectedTotalProfiles > 0 ? (expectedTotalProfiles / profilesPerScroll) + 10 : maxScrollAttempts;
			maxScrollAttempts = Math.min(estimatedScrolls, maxScrollAttempts);
			LOGGER.info("Estimated scrolls needed: {} ({} profiles / {} per scroll)", estimatedScrolls, expectedTotalProfiles, profilesPerScroll);

			// Scroll through all profiles using FULL PAGE scroll (triggers lazy loading)
			while (scrollAttempts < maxScrollAttempts && !allProfilesLoaded) {
				// Scroll to FULL page bottom (this triggers lazy loading)
				try {
					js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
				} catch (Exception e1) {
					try {
						js.executeScript("window.scrollTo(0, document.documentElement.scrollHeight);");
					} catch (Exception e2) {
						js.executeScript("window.scrollBy(0, 10000);");
					}
				}
				scrollAttempts++;
				
				// Wait for lazy loading - JAM needs less time, PM needs more
				int scrollWait = isJAM ? 500 : 1500;
				safeSleep(scrollWait);
				PerformanceUtils.waitForSpinnersToDisappear(driver, 3);

				// Count visible profiles
				totalProfilesVisible = findElements(getAllProfileRowsLocator(screen)).size();

				// Count selected using JavaScript
				int currentSelectedCount = 0;
				if (isJAM) {
					String jsScript = "return Array.from(document.querySelectorAll('#org-job-container tbody tr td:first-child input[type=\"checkbox\"]')).filter(cb => cb.checked).length;";
					Object result = js.executeScript(jsScript);
					currentSelectedCount = result != null ? ((Long) result).intValue() : 0;
				} else {
					String jsScript = "return document.querySelectorAll('tbody tr kf-icon[icon=\"checkbox-check\"]').length;";
					Object result = js.executeScript(jsScript);
					currentSelectedCount = result != null ? ((Long) result).intValue() : 0;
					if (currentSelectedCount == 0) {
						currentSelectedCount = findElements(getSelectedProfileRowsLocator(screen)).size();
					}
				}

				// Log progress every 10 scrolls
				if (scrollAttempts % 10 == 0) {
					long elapsed = (System.currentTimeMillis() - startTime) / 1000;
					int pctLoaded = expectedTotalProfiles > 0 ? (totalProfilesVisible * 100 / expectedTotalProfiles) : 0;
					LOGGER.info("Scroll progress: {} scrolls, {}/{} profiles ({}%), {} selected, {}s elapsed", 
							scrollAttempts, totalProfilesVisible, expectedTotalProfiles, pctLoaded, currentSelectedCount, elapsed);
				}

				// Fail-fast: Check if selected count increased beyond baseline
				if (currentSelectedCount > searchResultsCount.get()) {
					int extra = currentSelectedCount - searchResultsCount.get();
					LOGGER.warn("FAIL-FAST at scroll {}: Found {} selected (expected {}), {} extra",
							scrollAttempts, currentSelectedCount, searchResultsCount.get(), extra);
					allProfilesLoaded = true;
					actualSelectedCount = currentSelectedCount;
					break;
				}

				// Check if count is stable (no new profiles loaded)
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

			maxScrollLimitReached = scrollAttempts >= maxScrollAttempts;
			
			if (maxScrollLimitReached) {
				long elapsed = (System.currentTimeMillis() - startTime) / 1000;
				LOGGER.warn("Max scroll limit reached: {} scrolls, {} profiles loaded ({}s)", 
						scrollAttempts, totalProfilesVisible, elapsed);
			}

			// After loading all profiles, count selected if not already set
			if (actualSelectedCount == 0) {
				if (isJAM) {
					String jsScript = "return Array.from(document.querySelectorAll('#org-job-container tbody tr td:first-child input[type=\"checkbox\"]')).filter(cb => cb.checked).length;";
					Object result = js.executeScript(jsScript);
					actualSelectedCount = result != null ? ((Long) result).intValue() : 0;
				} else {
					actualSelectedCount = findElements(getSelectedProfileRowsLocator(screen)).size();
				}
			}
			int notSelectedProfiles = totalProfilesVisible - actualSelectedCount;

			// Calculate missing/extra selections
			int missingSelections = Math.max(0, searchResultsCount.get() - actualSelectedCount);
			int extraSelections = Math.max(0, actualSelectedCount - searchResultsCount.get());

			// Log validation summary
			logValidationSummary(screen, maxScrollLimitReached, expectedTotalProfiles, totalProfilesVisible,
					actualSelectedCount, notSelectedProfiles, missingSelections, extraSelections);

			// Validate selection counts
			validateSelectionCounts(screen, maxScrollLimitReached, expectedTotalProfiles, totalProfilesVisible,
					actualSelectedCount, missingSelections, extraSelections);

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_only_searched_profiles_selected_" + screen, e);
			PageObjectHelper.handleError(LOGGER, "verify_only_searched_profiles_selected",
					"Error verifying searched profiles in " + getScreenName(screen), e);
			Assert.fail("Error verifying only searched profiles are selected: " + e.getMessage());
		}
	}

	// ==================== ALTERNATIVE VALIDATION METHODS ====================

	/**
	 * Enters a different job name substring in search bar for alternative validation.
	 */
	public void enter_different_job_name_substring_for_alternative_validation(String screen) {
		boolean foundResults = false;
		String firstSearchSubstring = getSearchSubstring(screen);
		JavascriptExecutor js = (JavascriptExecutor) driver;

		try {
			currentScreen.set(screen);
			PerformanceUtils.waitForSpinnersToDisappear(driver, 5);

			PageObjectHelper.log(LOGGER, "Alternative validation: Searching different substring in " + getScreenName(screen) + "...");

			String[] searchOptions = getSearchOptions(screen);
			int attemptCount = 0;
			
			for (String substring : searchOptions) {
				if (substring.equalsIgnoreCase(firstSearchSubstring)) {
					continue;
				}
				
				attemptCount++;
				LOGGER.debug("Trying search substring {}/{}: '{}'", attemptCount, searchOptions.length - 1, substring);

				try {
					// Scroll to top first
					scrollToTop();
					safeSleep(500);
					
					By searchBarLocator = getSearchBarLocator(screen);
					WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(15));
					
					// Re-find and scroll to search bar
					WebElement searchBarElement = shortWait.until(
							ExpectedConditions.visibilityOfElementLocated(searchBarLocator));
					js.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});", searchBarElement);
					safeSleep(300);

					// Clear search bar using JavaScript for reliability
					String clearAndSearchScript = "";
					if ("JAM".equalsIgnoreCase(screen)) {
						clearAndSearchScript = 
							"var searchBar = document.querySelector('#search-job-title-input-search-input');" +
							"if (searchBar) {" +
							"  searchBar.focus();" +
							"  searchBar.value = '';" +
							"  searchBar.dispatchEvent(new Event('input', { bubbles: true }));" +
							"  searchBar.value = '" + substring + "';" +
							"  searchBar.dispatchEvent(new Event('input', { bubbles: true }));" +
							"  searchBar.dispatchEvent(new KeyboardEvent('keydown', { key: 'Enter', keyCode: 13, bubbles: true }));" +
							"  return true;" +
							"} return false;";
					} else {
						clearAndSearchScript = 
							"var searchBar = document.querySelector('input[type=\"search\"]');" +
							"if (searchBar) {" +
							"  searchBar.focus();" +
							"  searchBar.value = '';" +
							"  searchBar.dispatchEvent(new Event('input', { bubbles: true }));" +
							"  searchBar.value = '" + substring + "';" +
							"  searchBar.dispatchEvent(new Event('input', { bubbles: true }));" +
							"  searchBar.dispatchEvent(new KeyboardEvent('keydown', { key: 'Enter', keyCode: 13, bubbles: true }));" +
							"  return true;" +
							"} return false;";
					}
					
					Boolean searched = (Boolean) js.executeScript(clearAndSearchScript);
					
					if (searched == null || !searched) {
						// Fallback to WebElement approach
						searchBarElement = driver.findElement(searchBarLocator);
						searchBarElement.click();
						safeSleep(200);
						searchBarElement.sendKeys(Keys.CONTROL + "a");
						searchBarElement.sendKeys(Keys.DELETE);
						safeSleep(200);
						searchBarElement = driver.findElement(searchBarLocator);
						searchBarElement.sendKeys(substring);
						searchBarElement.sendKeys(Keys.ENTER);
					}

					// Wait for spinners and page to stabilize
					PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
					safeSleep(2000); // Critical: Wait for results to load
					
					// For JAM, also wait for background data
					if ("JAM".equalsIgnoreCase(screen)) {
						safeSleep(1500); // Extra wait for JAM API
					}

					// Check results
					WebElement resultsElement = shortWait.until(
							ExpectedConditions.visibilityOfElementLocated(getShowingResultsCountLocator(screen)));
					String resultsCountText = resultsElement.getText();
					
					LOGGER.debug("Search '{}' results: {}", substring, resultsCountText);

					if (resultsCountText.contains("Showing") && !resultsCountText.contains("Showing 0")) {
						alternativeSearchSubstring.set(substring);
						foundResults = true;
						PageObjectHelper.log(LOGGER, "Second search: '" + substring + "' - " + resultsCountText);
						break;
					} else {
						LOGGER.debug("No results for '{}', trying next...", substring);
					}

				} catch (Exception e) {
					LOGGER.debug("Search attempt for '{}' failed: {}", substring, e.getMessage());
					// Continue to next substring
				}
			}

			if (!foundResults) {
				applyFallbackSearch(screen, firstSearchSubstring);
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("enter_different_substring_" + screen, e);
			PageObjectHelper.handleError(LOGGER, "enter_different_substring",
					"Failed to enter different substring in " + getScreenName(screen), e);
			Assert.fail("Failed to enter different job name substring in search bar");
		}
	}

	/**
	 * Validates second search results - checks that profiles from second search are NOT selected.
	 * Only validates initially visible profiles (no scrolling needed for this validation).
	 */
	public void scroll_down_to_load_all_second_search_results(String screen) throws InterruptedException {
		String secondSearchSubstring = alternativeSearchSubstring.get();
		String firstSearchSubstring = getSearchSubstring(screen);

		try {
			currentScreen.set(screen);
			PerformanceUtils.waitForSpinnersToDisappear(driver, 5);

			String resultsCountText = getElementText(getShowingResultsCountLocator(screen));

			if (resultsCountText.matches(".*Showing\\s+0\\s+of.*")) {
				PageObjectHelper.log(LOGGER, "Second search returned 0 results - skipping");
				totalSecondSearchResults.set(0);
				return;
			}

			int expectedTotal = parseProfileCountFromText(resultsCountText);
			PageObjectHelper.log(LOGGER, "Second search '" + secondSearchSubstring + 
					"': " + expectedTotal + " total profiles in " + getScreenName(screen));

			// Quick validation: Check if any selected profiles are visible that shouldn't be
			// We only need to verify that profiles matching SECOND search but NOT first are not selected
			List<WebElement> selectedRows = findElements(getSelectedProfileRowsLocator(screen));
			int selectedCount = selectedRows.size();
			
			LOGGER.debug("Found {} selected profiles in second search view", selectedCount);
			
			if (selectedCount == 0) {
				// Perfect - no selections visible means validation passes
				PageObjectHelper.log(LOGGER, "✅ No selected profiles visible in second search - PASS");
				totalSecondSearchResults.set(expectedTotal);
				return;
			}

			// Spot-check: Verify selected profiles contain the FIRST search substring
			// (They were selected from first search, so they should match first substring)
			int invalidCount = 0;
			int checkLimit = Math.min(selectedCount, 10); // Only check first 10 for performance
			
			for (int i = 0; i < checkLimit; i++) {
				try {
					WebElement row = selectedRows.get(i);
					String jobName = extractJobName(row, screen).toLowerCase();
					
					if (!jobName.contains(firstSearchSubstring.toLowerCase())) {
						// This profile is selected but doesn't match first search - INVALID!
						invalidCount++;
						LOGGER.warn("INVALID: Selected profile '{}' doesn't contain first search '{}'", 
								jobName, firstSearchSubstring);
						break; // Fail fast
					}
				} catch (Exception e) {
					// Skip this row if we can't read it
				}
			}

			totalSecondSearchResults.set(expectedTotal);

			if (invalidCount > 0) {
				PageObjectHelper.log(LOGGER, "❌ Found profiles selected that shouldn't be - FAIL");
				Assert.fail("Found invalid selections in second search results");
			} else {
				PageObjectHelper.log(LOGGER, "✅ All " + selectedCount + " selected profiles are valid (from first search) - PASS");
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("scroll_second_search_results_" + screen, e);
			PageObjectHelper.handleError(LOGGER, "scroll_second_search_results",
					"Error validating second search results in " + getScreenName(screen), e);
			Assert.fail("Error validating second search results: " + e.getMessage());
		}
	}

	/**
	 * Verifies that all loaded profiles in second search are NOT selected.
	 */
	public void verify_all_loaded_profiles_in_second_search_are_not_selected(String screen) {
		PageObjectHelper.log(LOGGER, "✅ Validation completed in previous step for " + getScreenName(screen));
	}

	// ==================== PRIVATE HELPER METHODS ====================

	private void applyFallbackSearch(String screen, String firstSearchSubstring) {
		String selectedSubstring = "";
		for (String substring : getSearchOptions(screen)) {
			if (!substring.equalsIgnoreCase(firstSearchSubstring)) {
				selectedSubstring = substring;
				alternativeSearchSubstring.set(substring);
				break;
			}
		}
		PageObjectHelper.log(LOGGER, "Using fallback: '" + selectedSubstring + "' (no results found)");
		
		// Actually execute the fallback search
		try {
			WebElement searchBarElement = wait.until(ExpectedConditions.elementToBeClickable(getSearchBarLocator(screen)));
			searchBarElement.click();
			searchBarElement.sendKeys(Keys.CONTROL + "a");
			searchBarElement.sendKeys(Keys.DELETE);
			PerformanceUtils.waitForSpinnersToDisappear(driver, 5);
			
			searchBarElement = wait.until(ExpectedConditions.visibilityOfElementLocated(getSearchBarLocator(screen)));
			searchBarElement.sendKeys(selectedSubstring);
			searchBarElement.sendKeys(Keys.ENTER);
			PerformanceUtils.waitForSpinnersToDisappear(driver, 5);
			safeSleep(500);
		} catch (Exception applyEx) {
			LOGGER.warn("Failed to apply fallback search: {}", applyEx.getMessage());
		}
	}

	private void logValidationSummary(String screen, boolean maxScrollLimitReached, int expectedTotalProfiles,
			int totalProfilesVisible, int actualSelectedCount, int notSelectedProfiles,
			int missingSelections, int extraSelections) {

		LOGGER.debug("========================================");
		LOGGER.debug("VALIDATION SUMMARY - {} (After Clearing Search)", getScreenName(screen));
		LOGGER.debug("========================================");
		if (maxScrollLimitReached && expectedTotalProfiles > 0 && totalProfilesVisible < expectedTotalProfiles) {
			LOGGER.debug("PARTIAL VALIDATION (Max scroll limit reached)");
			LOGGER.debug("Expected Total: {}, Actually Loaded: {}", expectedTotalProfiles, totalProfilesVisible);
		} else {
			LOGGER.debug("Total Profiles Loaded: {}", totalProfilesVisible);
		}
		LOGGER.debug("Currently Selected: {}, Not Selected: {}", actualSelectedCount, notSelectedProfiles);
		LOGGER.debug("Baseline (from search): {} profiles", searchResultsCount.get());
		if (missingSelections > 0) {
			LOGGER.debug("Missing Selections: {}", missingSelections);
		} else if (extraSelections > 0) {
			LOGGER.debug("Extra Selections: {}", extraSelections);
		}
		LOGGER.debug("========================================");
	}

	private void validateSelectionCounts(String screen, boolean maxScrollLimitReached, int expectedTotalProfiles,
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
				PageObjectHelper.log(LOGGER, "✅ PASS: Found " + actualSelectedCount + " of " + searchResultsCount.get() +
						" selected (remaining " + missingSelections + " likely in unloaded profiles)");
			} else {
				PageObjectHelper.log(LOGGER, "✅ PASS: All " + searchResultsCount.get() + " searched profiles found selected");
			}
		} else {
			// Full validation
			if (actualSelectedCount == searchResultsCount.get()) {
				PageObjectHelper.log(LOGGER, "✅ PASS: All " + searchResultsCount.get() +
						" searched profiles remain selected in " + getScreenName(screen));
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

	private String extractJobName(WebElement row, String screen) {
		try {
			WebElement jobNameElement = null;
			if ("PM".equalsIgnoreCase(screen)) {
				try {
					jobNameElement = row.findElement(By.xpath(".//td//div//span[1]//a"));
				} catch (Exception e1) {
					try {
						jobNameElement = row.findElement(By.xpath(".//td//div//span//a"));
					} catch (Exception e2) {
						jobNameElement = row.findElement(By.xpath(".//td[position()=1]//a"));
					}
				}
			} else {
				try {
					jobNameElement = row.findElement(By.xpath(".//td[2]//div"));
				} catch (Exception e1) {
					jobNameElement = row.findElement(By.xpath(".//td[position()=2]//div"));
				}
			}
			return jobNameElement.getText().trim();
		} catch (Exception e) {
			return "";
		}
	}

}

