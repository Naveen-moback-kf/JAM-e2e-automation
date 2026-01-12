package com.kfonetalentsuite.pageobjects.JobMapping;
import static com.kfonetalentsuite.pageobjects.JobMapping.BasePageObject.Locators.SharedLocators.*;

import java.time.Duration;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import com.kfonetalentsuite.utils.common.ScreenshotHandler;
import com.kfonetalentsuite.utils.JobMapping.Utilities;

public class PO27_SelectAllWithSearchFunctionality extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO27_SelectAllWithSearchFunctionality.class);

	public PO27_SelectAllWithSearchFunctionality() {
		super();
	}
	// Common locators are now in BasePageObject.Locators.PMScreen and JAMScreen
	// Only search-specific locators remain here if needed
	public static ThreadLocal<Integer> searchResultsCount = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<String> alternativeSearchSubstring = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<Integer> totalSecondSearchResults = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<String> currentScreen = ThreadLocal.withInitial(() -> "PM");
	public static ThreadLocal<Integer> loadedProfilesBeforeScroll = ThreadLocal.withInitial(() -> 0);
	// Common helper methods (getScreenName, getShowingResultsCountLocator, getAllProfileRowsLocator,
	// getSelectedProfileRowsLocator, getChevronButtonLocator, getHeaderCheckboxLocator, 
	// getSearchBarLocator, getActionButtonLocator) are now inherited from BasePageObject

	private By getSelectAllButtonLocator(String screen) {
		// Both PM and JAM use the same Select All button locator
		return SELECT_ALL_BTN;
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

	public void user_should_scroll_down_to_view_last_search_result(String screen) {
		try {
			currentScreen.set(screen);
			Utilities.waitForSpinnersToDisappear(driver, 5);

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

				Utilities.waitForSpinnersToDisappear(driver, 3);

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

			LOGGER.info("Loaded " + currentCount + " search results for validation in " + getScreenName(screen));

		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_should_scroll_down_to_view_last_search_result",
					"Issue scrolling to view search results in " + getScreenName(screen), e);
			ScreenshotHandler.captureFailureScreenshot("scroll_search_results_" + screen, e);
			Assert.fail("Issue scrolling to view search results");
		}
	}

	public void user_should_validate_all_search_results_contains_substring(String screen) {
		try {
			currentScreen.set(screen);
			Utilities.waitForSpinnersToDisappear(driver, 5);

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
				LOGGER.info("✅ All " + totalResults + " results contain '" + searchSubstring + "' in " + getScreenName(screen));
			} else {
				LOGGER.info("⚠️ " + nonMatchingResults + " of " + totalResults +
						" results do NOT contain '" + searchSubstring + "' in " + getScreenName(screen));
			}

		} catch (Exception e) {
			Utilities.handleError(LOGGER, "validate_search_results_contain_substring",
					"Issue validating search results in " + getScreenName(screen), e);
			ScreenshotHandler.captureFailureScreenshot("validate_search_results_" + screen, e);
			Assert.fail("Issue validating search results contain substring");
		}
	}

	public void click_on_chevron_button_beside_header_checkbox(String screen) {
		try {
			currentScreen.set(screen);
			Utilities.waitForSpinnersToDisappear(driver, 5);

			// Scroll to top to ensure chevron button is visible
			scrollToTop();
			safeSleep(500);

			clickElement(getChevronButtonLocator(screen));
			LOGGER.info("Clicked chevron button in " + getScreenName(screen));

		} catch (Exception e) {
			Utilities.handleError(LOGGER, "click_on_chevron_button",
					"Error clicking chevron button in " + getScreenName(screen), e);
			ScreenshotHandler.captureFailureScreenshot("click_chevron_" + screen, e);
			Assert.fail("Error clicking chevron button");
		}
	}

	public void click_on_select_all_button(String screen) {
		try {
			currentScreen.set(screen);
			safeSleep(300);

			clickElement(getSelectAllButtonLocator(screen));

			Utilities.waitForSpinnersToDisappear(driver, 5);

			LOGGER.info("Clicked Select All button in " + getScreenName(screen));

		} catch (Exception e) {
			Utilities.handleError(LOGGER, "click_on_select_all_button",
					"Error clicking Select All button in " + getScreenName(screen), e);
			ScreenshotHandler.captureFailureScreenshot("click_select_all_" + screen, e);
			Assert.fail("Error clicking Select All button");
		}
	}

	public void click_on_header_checkbox_to_select_loaded_profiles(String screen) {
		try {
			currentScreen.set(screen);
			Utilities.waitForSpinnersToDisappear(driver, 5);

			// Capture count before clicking
			loadedProfilesBeforeScroll.set(findElements(getAllProfileRowsLocator(screen)).size());

			clickElement(getHeaderCheckboxLocator(screen));

			Utilities.waitForSpinnersToDisappear(driver, 5);

			LOGGER.info("Clicked header checkbox to select " + loadedProfilesBeforeScroll.get() +
					" loaded profiles in " + getScreenName(screen));

		} catch (Exception e) {
			Utilities.handleError(LOGGER, "click_on_header_checkbox",
					"Error clicking header checkbox in " + getScreenName(screen), e);
			ScreenshotHandler.captureFailureScreenshot("click_header_checkbox_" + screen, e);
			Assert.fail("Error clicking header checkbox");
		}
	}

	public void verify_action_button_is_enabled(String screen) {
		String buttonName = "PM".equalsIgnoreCase(screen) ? "Sync with HCM" : "Publish Selected Profiles";
		LOGGER.info("Verifying {} button is enabled in {}...", buttonName, getScreenName(screen));
		
		try {
			currentScreen.set(screen);
			Utilities.waitForSpinnersToDisappear(driver, 5);
			
			scrollToTop();
			safeSleep(300);

			WebElement button = Utilities.waitForVisible(wait, getActionButtonLocator(screen));
			boolean isEnabled = button.isEnabled();
			String buttonText = button.getText();
			
			LOGGER.info("Button found: '{}', enabled: {}", buttonText, isEnabled);

			if (isEnabled) {
				LOGGER.info("✅ " + buttonName + " button is enabled in " + getScreenName(screen));
			} else {
				LOGGER.info("❌ " + buttonName + " button is NOT enabled in " + getScreenName(screen));
				Assert.fail(buttonName + " button should be enabled after selection");
			}

		} catch (Exception e) {
			LOGGER.error("Failed to verify {} button: {}", buttonName, e.getMessage());
			Utilities.handleError(LOGGER, "verify_action_button_is_enabled",
					"Error verifying action button in " + getScreenName(screen), e);
			Assert.fail("Error verifying action button is enabled: " + e.getMessage());
		}
	}

	public void scroll_page_to_view_more_job_profiles(String screen) {
		try {
			currentScreen.set(screen);
			Utilities.waitForSpinnersToDisappear(driver, 3);

			scrollToBottom();
			safeSleep(500);

			Utilities.waitForSpinnersToDisappear(driver, 3);

			int newCount = findElements(getAllProfileRowsLocator(screen)).size();
			LOGGER.info("Scrolled to load more profiles. Now " + newCount + " visible in " + getScreenName(screen));

		} catch (Exception e) {
			Utilities.handleError(LOGGER, "scroll_page_to_view_more_job_profiles",
					"Error scrolling in " + getScreenName(screen), e);
		}
	}

	public void verify_profiles_loaded_after_header_checkbox_are_not_selected(String screen) {
		try {
			currentScreen.set(screen);
			Utilities.waitForSpinnersToDisappear(driver, 3);

			int initialLoaded = loadedProfilesBeforeScroll.get();
			
			// Quick count using findElements size - much faster than iterating
			int currentTotal = findElements(getAllProfileRowsLocator(screen)).size();
			int selectedCount = findElements(getSelectedProfileRowsLocator(screen)).size();
			int newlyLoaded = currentTotal - initialLoaded;

			if (newlyLoaded <= 0) {
				LOGGER.info("No newly loaded profiles to verify in " + getScreenName(screen));
				return;
			}

			// If selected count hasn't increased beyond initial, newly loaded profiles are not selected
			// Initial selection was done on 'initialLoaded' profiles
			int expectedSelected = initialLoaded; // Header checkbox selected all loaded at that time
			
			if (selectedCount <= expectedSelected) {
				LOGGER.info("✅ Newly loaded " + newlyLoaded + " profiles are NOT selected in " + getScreenName(screen) + 
						" (selected: " + selectedCount + ", expected max: " + expectedSelected + ")");
			} else {
				int extraSelected = selectedCount - expectedSelected;
				LOGGER.info("⚠️ " + extraSelected + " newly loaded profiles are unexpectedly selected in " + getScreenName(screen));
			}

		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_profiles_loaded_after_header_checkbox",
					"Error verifying newly loaded profiles in " + getScreenName(screen), e);
		}
	}

	public void capture_baseline_of_selected_profiles(String screen) {
		try {
			currentScreen.set(screen);
			Utilities.waitForSpinnersToDisappear(driver, 5);
			safeSleep(300);
			
			scrollToTop();
			safeSleep(200);

			int selectedCount = 0;
			int previousTotalVisible = 0;
			int scrollAttempts = 0;
			int noChangeCount = 0;
			int maxScrollAttempts = 200; // Reduced from 500 for better performance
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
			
			LOGGER.info("Counting selected profiles in " + getScreenName(screen) + 
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
				Utilities.waitForSpinnersToDisappear(driver, 3);
				
				// Count current selected using BasePageObject helper method
				selectedCount = countSelectedProfilesJS(screen);
				
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

			LOGGER.info("Baseline captured: " + selectedCount + " selected profiles in " + getScreenName(screen) + 
					" (scrolls: " + scrollAttempts + ", total loaded: " + totalVisible + ")");

		} catch (Exception e) {
			LOGGER.warn("Error capturing baseline in {}: {}", getScreenName(screen), e.getMessage());
			searchResultsCount.set(0);
		}
	}

	public void clear_search_bar(String screen) {
		try {
			currentScreen.set(screen);
			Utilities.waitForSpinnersToDisappear(driver, 5);
			
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
			// Fallback to clearAndSearch helper (search with empty string clears results)
			LOGGER.debug("JS clear failed, using clearAndSearch helper");
			clearAndSearch(searchBarLocator, "");
		}

		Utilities.waitForSpinnersToDisappear(driver, 10);
		safeSleep(1500); // Wait for results to reload

		LOGGER.info("Cleared search bar in " + getScreenName(screen));

		} catch (Exception e) {
			Utilities.handleError(LOGGER, "clear_search_bar",
					"Error clearing search bar in " + getScreenName(screen), e);
		}
	}

	public void verify_only_searched_profiles_are_selected_after_clearing_search_bar(String screen) {
		int totalProfilesVisible = 0;
		int previousTotalProfilesVisible = 0;
		int actualSelectedCount = 0;
		int scrollAttempts = 0;
		int stableCountAttempts = 0;
		int earlySuccessAttempts = 0;
		boolean allProfilesLoaded = false;
		int expectedTotalProfiles = 0;
		boolean maxScrollLimitReached = false;
		boolean earlySuccessExit = false;
		JavascriptExecutor js = (JavascriptExecutor) driver;
		boolean isJAM = "JAM".equalsIgnoreCase(screen);
		int baseline = searchResultsCount.get();
		
		// Scrolling settings - JAM loads ~10 profiles/scroll, PM loads ~50 profiles/scroll
		// Reduced max from 500 to 200 for better performance
		int maxScrollAttempts = 200;
		int requiredStableAttempts = 5;
		int requiredEarlySuccessAttempts = 3; // Consecutive scrolls with all expected selections found

		try {
			currentScreen.set(screen);
			
			LOGGER.info("Verifying only " + baseline +
					" profiles remain selected in " + getScreenName(screen) + "...");

			if (baseline == 0) {
				LOGGER.info("No search results to verify - skipping");
				return;
			}
			
			// Wait for page to stabilize after clearing search
			Utilities.waitForSpinnersToDisappear(driver, 10);
			safeSleep(500);
			
			// Scroll to top first
			scrollToTop();
			safeSleep(300);
			
			long startTime = System.currentTimeMillis();

			// Get expected total profile count from "Showing X of Y" text
			try {
				String resultsCountText = getElementText(getShowingResultsCountLocator(screen));
				expectedTotalProfiles = parseProfileCountFromText(resultsCountText);
				LOGGER.info("Expected total profiles from 'Showing' text: {}", expectedTotalProfiles);
			} catch (Exception e) {
				LOGGER.debug("Could not parse total profile count: {}", e.getMessage());
			}

			// Calculate estimated scrolls needed (capped at maxScrollAttempts)
			int profilesPerScroll = isJAM ? 10 : 50;
			int estimatedScrolls = expectedTotalProfiles > 0 ? (expectedTotalProfiles / profilesPerScroll) + 10 : maxScrollAttempts;
			maxScrollAttempts = Math.min(estimatedScrolls, maxScrollAttempts);
			
			// Minimum profiles to load before early success can trigger (at least 3x baseline or 50% of total)
			int minProfilesForEarlySuccess = Math.max(baseline * 3, expectedTotalProfiles / 2);
			
			LOGGER.info("Estimated scrolls needed: {} ({} profiles / {} per scroll), early success after {} profiles", 
					estimatedScrolls, expectedTotalProfiles, profilesPerScroll, minProfilesForEarlySuccess);

			// Scroll through profiles using FULL PAGE scroll (triggers lazy loading)
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
				Utilities.waitForSpinnersToDisappear(driver, 3);

				// Count visible profiles
				totalProfilesVisible = findElements(getAllProfileRowsLocator(screen)).size();

				// Count selected using BasePageObject helper method
				int currentSelectedCount = countSelectedProfilesJS(screen);

				// Log progress every 10 scrolls
				if (scrollAttempts % 10 == 0) {
					long elapsed = (System.currentTimeMillis() - startTime) / 1000;
					int pctLoaded = expectedTotalProfiles > 0 ? (totalProfilesVisible * 100 / expectedTotalProfiles) : 0;
					LOGGER.info("Scroll progress: {} scrolls, {}/{} profiles ({}%), {} selected, {}s elapsed", 
							scrollAttempts, totalProfilesVisible, expectedTotalProfiles, pctLoaded, currentSelectedCount, elapsed);
				}

				// FAIL-FAST: Check if selected count increased beyond baseline
				if (currentSelectedCount > baseline) {
					int extra = currentSelectedCount - baseline;
					LOGGER.warn("FAIL-FAST at scroll {}: Found {} selected (expected {}), {} extra",
							scrollAttempts, currentSelectedCount, baseline, extra);
					allProfilesLoaded = true;
					actualSelectedCount = currentSelectedCount;
					break;
				}

				// EARLY SUCCESS: All expected selections found + loaded enough profiles
				if (currentSelectedCount == baseline && totalProfilesVisible >= minProfilesForEarlySuccess) {
					earlySuccessAttempts++;
					if (earlySuccessAttempts >= requiredEarlySuccessAttempts) {
						long elapsed = (System.currentTimeMillis() - startTime) / 1000;
						int pctLoaded = expectedTotalProfiles > 0 ? (totalProfilesVisible * 100 / expectedTotalProfiles) : 0;
						LOGGER.info("EARLY SUCCESS at scroll {}: Found all {} expected selections, {}% profiles loaded ({}s)", 
								scrollAttempts, baseline, pctLoaded, elapsed);
						allProfilesLoaded = true;
						earlySuccessExit = true;
						actualSelectedCount = currentSelectedCount;
						break;
					}
				} else {
					earlySuccessAttempts = 0; // Reset if count changes
				}

				// Check if count is stable (no new profiles loaded - reached end)
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

			maxScrollLimitReached = scrollAttempts >= maxScrollAttempts && !earlySuccessExit;
			
			if (maxScrollLimitReached) {
				long elapsed = (System.currentTimeMillis() - startTime) / 1000;
				LOGGER.warn("Max scroll limit reached: {} scrolls, {} profiles loaded ({}s)", 
						scrollAttempts, totalProfilesVisible, elapsed);
			}

			// After loading all profiles, count selected if not already set
			if (actualSelectedCount == 0) {
				actualSelectedCount = countSelectedProfilesJS(screen);
			}
			int notSelectedProfiles = totalProfilesVisible - actualSelectedCount;

			// Calculate missing/extra selections
			int missingSelections = Math.max(0, baseline - actualSelectedCount);
			int extraSelections = Math.max(0, actualSelectedCount - baseline);

			// Log validation summary
			logValidationSummary(screen, maxScrollLimitReached, earlySuccessExit, expectedTotalProfiles, totalProfilesVisible,
					actualSelectedCount, notSelectedProfiles, missingSelections, extraSelections, baseline);

			// Validate selection counts
			validateSelectionCounts(screen, maxScrollLimitReached, earlySuccessExit, expectedTotalProfiles, totalProfilesVisible,
					actualSelectedCount, missingSelections, extraSelections, baseline);

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_only_searched_profiles_selected_" + screen, e);
			Utilities.handleError(LOGGER, "verify_only_searched_profiles_selected",
					"Error verifying searched profiles in " + getScreenName(screen), e);
			Assert.fail("Error verifying only searched profiles are selected: " + e.getMessage());
		}
	}

	public void enter_different_job_name_substring_for_alternative_validation(String screen) {
		boolean foundResults = false;
		String firstSearchSubstring = getSearchSubstring(screen);
		JavascriptExecutor js = (JavascriptExecutor) driver;

		try {
			currentScreen.set(screen);
			Utilities.waitForSpinnersToDisappear(driver, 5);

			LOGGER.info("Alternative validation: Searching different substring in " + getScreenName(screen) + "...");

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
						// Fallback to clearAndSearch helper
						clearAndSearch(searchBarLocator, substring);
					}

					// Wait for spinners and page to stabilize
					Utilities.waitForSpinnersToDisappear(driver, 10);
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
						LOGGER.info("Second search: '" + substring + "' - " + resultsCountText);
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
			Utilities.handleError(LOGGER, "enter_different_substring",
					"Failed to enter different substring in " + getScreenName(screen), e);
			Assert.fail("Failed to enter different job name substring in search bar");
		}
	}

	public void scroll_down_to_load_all_second_search_results(String screen) throws InterruptedException {
		String secondSearchSubstring = alternativeSearchSubstring.get();
		String firstSearchSubstring = getSearchSubstring(screen);

		try {
			currentScreen.set(screen);
			Utilities.waitForSpinnersToDisappear(driver, 5);

			String resultsCountText = getElementText(getShowingResultsCountLocator(screen));

			if (resultsCountText.matches(".*Showing\\s+0\\s+of.*")) {
				LOGGER.info("Second search returned 0 results - skipping");
				totalSecondSearchResults.set(0);
				return;
			}

			int expectedTotal = parseProfileCountFromText(resultsCountText);
			LOGGER.info("Second search '" + secondSearchSubstring + 
					"': " + expectedTotal + " total profiles in " + getScreenName(screen));

			// Quick validation: Check if any selected profiles are visible that shouldn't be
			// We only need to verify that profiles matching SECOND search but NOT first are not selected
			List<WebElement> selectedRows = findElements(getSelectedProfileRowsLocator(screen));
			int selectedCount = selectedRows.size();
			
			LOGGER.debug("Found {} selected profiles in second search view", selectedCount);
			
			if (selectedCount == 0) {
				// Perfect - no selections visible means validation passes
				LOGGER.info("✅ No selected profiles visible in second search - PASS");
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
				LOGGER.info("❌ Found profiles selected that shouldn't be - FAIL");
				Assert.fail("Found invalid selections in second search results");
			} else {
				LOGGER.info("✅ All " + selectedCount + " selected profiles are valid (from first search) - PASS");
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("scroll_second_search_results_" + screen, e);
			Utilities.handleError(LOGGER, "scroll_second_search_results",
					"Error validating second search results in " + getScreenName(screen), e);
			Assert.fail("Error validating second search results: " + e.getMessage());
		}
	}

	public void verify_all_loaded_profiles_in_second_search_are_not_selected(String screen) {
		LOGGER.info("✅ Validation completed in previous step for " + getScreenName(screen));
	}

	private void applyFallbackSearch(String screen, String firstSearchSubstring) {
		String selectedSubstring = "";
		for (String substring : getSearchOptions(screen)) {
			if (!substring.equalsIgnoreCase(firstSearchSubstring)) {
				selectedSubstring = substring;
				alternativeSearchSubstring.set(substring);
				break;
			}
		}
		LOGGER.info("Using fallback: '" + selectedSubstring + "' (no results found)");
		
		// Actually execute the fallback search
		try {
			// Use clearAndSearch helper for cleaner implementation
			clearAndSearch(getSearchBarLocator(screen), selectedSubstring);
			safeSleep(500);
		} catch (Exception applyEx) {
			LOGGER.warn("Failed to apply fallback search: {}", applyEx.getMessage());
		}
	}

	private void logValidationSummary(String screen, boolean maxScrollLimitReached, boolean earlySuccessExit,
			int expectedTotalProfiles, int totalProfilesVisible, int actualSelectedCount, int notSelectedProfiles,
			int missingSelections, int extraSelections, int baseline) {

		LOGGER.debug("========================================");
		LOGGER.debug("VALIDATION SUMMARY - {} (After Clearing Search)", getScreenName(screen));
		LOGGER.debug("========================================");
		if (earlySuccessExit) {
			LOGGER.debug("EARLY SUCCESS EXIT - All expected selections found");
			int pctLoaded = expectedTotalProfiles > 0 ? (totalProfilesVisible * 100 / expectedTotalProfiles) : 0;
			LOGGER.debug("Loaded: {}/{} profiles ({}%)", totalProfilesVisible, expectedTotalProfiles, pctLoaded);
		} else if (maxScrollLimitReached && expectedTotalProfiles > 0 && totalProfilesVisible < expectedTotalProfiles) {
			LOGGER.debug("PARTIAL VALIDATION (Max scroll limit reached)");
			LOGGER.debug("Expected Total: {}, Actually Loaded: {}", expectedTotalProfiles, totalProfilesVisible);
		} else {
			LOGGER.debug("FULL VALIDATION - All profiles loaded");
			LOGGER.debug("Total Profiles Loaded: {}", totalProfilesVisible);
		}
		LOGGER.debug("Currently Selected: {}, Not Selected: {}", actualSelectedCount, notSelectedProfiles);
		LOGGER.debug("Baseline (from search): {} profiles", baseline);
		if (missingSelections > 0) {
			LOGGER.debug("Missing Selections: {}", missingSelections);
		} else if (extraSelections > 0) {
			LOGGER.debug("Extra Selections: {}", extraSelections);
		}
		LOGGER.debug("========================================");
	}

	private void validateSelectionCounts(String screen, boolean maxScrollLimitReached, boolean earlySuccessExit,
			int expectedTotalProfiles, int totalProfilesVisible, int actualSelectedCount,
			int missingSelections, int extraSelections, int baseline) {

		// Early success exit - found all expected with no extras in loaded profiles
		if (earlySuccessExit) {
			int pctLoaded = expectedTotalProfiles > 0 ? (totalProfilesVisible * 100 / expectedTotalProfiles) : 0;
			LOGGER.info("✅ PASS (Early Success): All " + baseline +
					" searched profiles remain selected (" + pctLoaded + "% of profiles checked)");
			return;
		}

		if (maxScrollLimitReached && expectedTotalProfiles > 0 && totalProfilesVisible < expectedTotalProfiles) {
			// Partial validation
			if (actualSelectedCount == 0) {
				String errorMsg = "FAIL: No selections found in " + totalProfilesVisible +
						" loaded profiles (expected " + baseline + ")";
				LOGGER.info(errorMsg);
				Assert.fail(errorMsg);
			} else if (actualSelectedCount > baseline) {
				String errorMsg = "FAIL: Found " + actualSelectedCount + " selected (expected " +
						baseline + "), " + extraSelections + " extra profiles incorrectly selected";
				LOGGER.info(errorMsg);
				Assert.fail(errorMsg);
			} else if (actualSelectedCount < baseline) {
				LOGGER.info("✅ PASS: Found " + actualSelectedCount + " of " + baseline +
						" selected (remaining " + missingSelections + " likely in unloaded profiles)");
			} else {
				LOGGER.info("✅ PASS: All " + baseline + " searched profiles found selected");
			}
		} else {
			// Full validation
			if (actualSelectedCount == baseline) {
				LOGGER.info("✅ PASS: All " + baseline +
						" searched profiles remain selected in " + getScreenName(screen));
			} else if (actualSelectedCount < baseline) {
				String errorMsg = "FAIL: Only " + actualSelectedCount + " selected (expected " + baseline +
						"), " + missingSelections + " profiles lost selection";
				LOGGER.info(errorMsg);
				Assert.fail(errorMsg);
			} else {
				String errorMsg = "FAIL: " + actualSelectedCount + " selected (expected " + baseline +
						"), " + extraSelections + " extra profiles incorrectly selected";
				LOGGER.info(errorMsg);
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

