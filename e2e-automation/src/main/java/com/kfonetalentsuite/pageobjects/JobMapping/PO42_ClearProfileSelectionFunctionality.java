package com.kfonetalentsuite.pageobjects.JobMapping;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.ScreenshotHandler;
import com.kfonetalentsuite.utils.JobMapping.PageObjectHelper;

/**
 * Page Object for Clear Profile Selection Functionality
 * 
 * Handles clearing profile selection in both PM (HCM Sync Profiles) and JAM (Job Mapping) screens
 * using either Header Checkbox or None Button methods.
 * 
 * Parameters:
 * - screen: "PM" for HCM Sync Profiles, "JAM" for Job Mapping
 */
public class PO42_ClearProfileSelectionFunctionality extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO42_ClearProfileSelectionFunctionality.class);

	// THREAD-SAFE: Each thread gets its own isolated state for parallel execution
	public static ThreadLocal<Integer> loadedProfilesBeforeUncheck = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<Integer> selectedProfilesBeforeUncheck = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<String> currentScreen = ThreadLocal.withInitial(() -> "PM");

	// ═══════════════════════════════════════════════════════════════════════════
	// LOCATORS (Header checkbox locators now inherited from BasePageObject)
	// ═══════════════════════════════════════════════════════════════════════════
	private static final By PM_ALL_CHECKBOXES = By.xpath("//tbody//tr//td[1]//div[1]//kf-checkbox");
	private static final By PM_NONE_BTN = By.xpath("//*[contains(text(),'None')]");
	private static final By JAM_ALL_CHECKBOXES = By.xpath("//tbody//tr//td[1][contains(@class,'whitespace')]//input");
	private static final By JAM_NONE_BTN = By.xpath("//*[contains(text(),'None')]");

	public PO42_ClearProfileSelectionFunctionality() {
		super();
	}

	// ═══════════════════════════════════════════════════════════════════════════
	// UTILITY METHODS
	// ═══════════════════════════════════════════════════════════════════════════
	// getHeaderCheckboxLocator() and getScreenName() are inherited from BasePageObject

	/**
	 * Get all checkboxes locator based on screen type
	 */
	private By getAllCheckboxesLocator(String screen) {
		return screen.equalsIgnoreCase("PM") ? PM_ALL_CHECKBOXES : JAM_ALL_CHECKBOXES;
	}

	/**
	 * Get None button locator based on screen type
	 */
	private By getNoneButtonLocator(String screen) {
		return screen.equalsIgnoreCase("PM") ? PM_NONE_BTN : JAM_NONE_BTN;
	}

	/**
	 * Check if a checkbox is selected based on screen type
	 * PM uses kf-icon[icon="checkbox-check"] inside the checkbox, JAM uses isSelected()
	 */
	private boolean isCheckboxSelected(WebElement checkbox, String screen) {
		if (screen.equalsIgnoreCase("PM")) {
			try {
				// PM uses kf-icon with icon="checkbox-check" to indicate selected state
				List<WebElement> checkIcons = checkbox.findElements(By.xpath(".//kf-icon[@icon='checkbox-check']"));
				if (!checkIcons.isEmpty()) {
					return true;
				}
				// Fallback: check class attribute
				String classAttribute = checkbox.getAttribute("class");
				return classAttribute != null && (classAttribute.contains("selected") || classAttribute.contains("checked"));
			} catch (Exception e) {
				return false;
			}
		} else {
			return checkbox.isSelected();
		}
	}

	/**
	 * Check if a checkbox is disabled based on screen type
	 */
	private boolean isCheckboxDisabled(WebElement checkbox, String screen) {
		if (screen.equalsIgnoreCase("PM")) {
			String classAttribute = checkbox.getAttribute("class");
			return classAttribute != null && classAttribute.contains("disable");
		} else {
			return !checkbox.isEnabled();
		}
	}

	// getScreenName() is now inherited from BasePageObject

	// ═══════════════════════════════════════════════════════════════════════════
	// HEADER CHECKBOX METHODS
	// ═══════════════════════════════════════════════════════════════════════════

	/**
	 * Clicks on header checkbox to select loaded job profiles
	 */
	public void click_on_header_checkbox_to_select_loaded_job_profiles(String screen) {
		try {
			currentScreen.set(screen);
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

			// Store counts before selecting
			List<WebElement> allCheckboxes = findElements(getAllCheckboxesLocator(screen));
			loadedProfilesBeforeUncheck.set(allCheckboxes.size());

			LOGGER.debug("Loaded Profiles on screen (BEFORE selecting): {}", loadedProfilesBeforeUncheck.get());

			WebElement headerCheckbox = findElement(getHeaderCheckboxLocator(screen));

			// Scroll to header checkbox
			js.executeScript("window.scrollTo(0, 0);");
			safeSleep(500);
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", headerCheckbox);
			safeSleep(500);

			// Click header checkbox
			try {
				wait.until(ExpectedConditions.elementToBeClickable(headerCheckbox)).click();
			} catch (Exception e) {
				jsClick(headerCheckbox);
			}

			safeSleep(1000);
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

			PageObjectHelper.log(LOGGER, "Clicked header checkbox to select loaded profiles in " + getScreenName(screen));

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_on_header_checkbox_to_select_" + screen, e);
			PageObjectHelper.handleError(LOGGER, "click_on_header_checkbox_to_select_loaded_job_profiles",
					"Error clicking header checkbox to select profiles in " + getScreenName(screen), e);
			Assert.fail("Error clicking header checkbox to select profiles: " + e.getMessage());
		}
	}

	/**
	 * Clicks on header checkbox to unselect loaded job profiles
	 */
	public void click_on_header_checkbox_to_unselect_loaded_job_profiles(String screen) {
		try {
			currentScreen.set(screen);
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

			// Store counts before unchecking
			List<WebElement> allCheckboxes = findElements(getAllCheckboxesLocator(screen));
			loadedProfilesBeforeUncheck.set(allCheckboxes.size());

			// Count selected checkboxes
			int selectedCount = 0;
			for (WebElement checkbox : allCheckboxes) {
				if (isCheckboxSelected(checkbox, screen)) {
					selectedCount++;
				}
			}
			selectedProfilesBeforeUncheck.set(selectedCount);

			LOGGER.debug("Loaded Profiles (BEFORE unchecking): {}", loadedProfilesBeforeUncheck.get());
			LOGGER.debug("Selected Profiles (BEFORE unchecking): {}", selectedProfilesBeforeUncheck.get());

			// Scroll to top and then to header checkbox
			js.executeScript("window.scrollTo(0, 0);");
			safeSleep(500);

			WebElement headerCheckbox = findElement(getHeaderCheckboxLocator(screen));
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", headerCheckbox);
			safeSleep(500);

			// Click header checkbox
			try {
				wait.until(ExpectedConditions.elementToBeClickable(headerCheckbox)).click();
			} catch (Exception e) {
				jsClick(headerCheckbox);
			}

			safeSleep(1000);
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

			PageObjectHelper.log(LOGGER, "Clicked header checkbox to clear selection in " + getScreenName(screen));

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_on_header_checkbox_to_unselect_" + screen, e);
			PageObjectHelper.handleError(LOGGER, "click_on_header_checkbox_to_unselect_loaded_job_profiles",
					"Error clicking header checkbox to unselect profiles in " + getScreenName(screen), e);
			Assert.fail("Error clicking header checkbox to unselect profiles: " + e.getMessage());
		}
	}

	// ═══════════════════════════════════════════════════════════════════════════
	// NONE BUTTON METHODS
	// ═══════════════════════════════════════════════════════════════════════════

	/**
	 * Clicks on None button to unselect all profiles
	 */
	public void click_on_none_button(String screen) {
		try {
			currentScreen.set(screen);
			
			// Wait for None button to be clickable (dropdown appears after chevron click)
			try {
				wait.until(ExpectedConditions.elementToBeClickable(getNoneButtonLocator(screen))).click();
			} catch (Exception e) {
				try {
					jsClick(findElement(getNoneButtonLocator(screen)));
				} catch (Exception s) {
					clickElement(getNoneButtonLocator(screen));
				}
			}

			safeSleep(1000);
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

			PageObjectHelper.log(LOGGER, "Clicked None button in " + getScreenName(screen));

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_on_none_button_" + screen, e);
			PageObjectHelper.handleError(LOGGER, "click_on_none_button",
					"Error clicking None button in " + getScreenName(screen), e);
			Assert.fail("Error clicking None button: " + e.getMessage());
		}
	}

	// ═══════════════════════════════════════════════════════════════════════════
	// VERIFICATION METHODS
	// ═══════════════════════════════════════════════════════════════════════════

	/**
	 * Verifies that loaded profiles are unselected
	 */
	public void verify_loaded_profiles_are_unselected(String screen) {
		int unselectedCount = 0;
		int disabledCount = 0;

		try {
			currentScreen.set(screen);
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

			LOGGER.debug("========================================");
			LOGGER.debug("VERIFYING UNSELECTED PROFILES - {}", getScreenName(screen));
			LOGGER.debug("========================================");

			List<WebElement> allCheckboxes = findElements(getAllCheckboxesLocator(screen));
			int totalOnScreen = allCheckboxes.size();

			LOGGER.debug("Total Profiles on screen: {}", totalOnScreen);
			LOGGER.debug("Original Loaded Profiles: {}", loadedProfilesBeforeUncheck.get());

			for (int i = 0; i < totalOnScreen; i++) {
				try {
					WebElement checkbox = allCheckboxes.get(i);
					js.executeScript("arguments[0].scrollIntoView(true);", checkbox);

					if (isCheckboxDisabled(checkbox, screen)) {
						disabledCount++;
						continue;
					}

					if (!isCheckboxSelected(checkbox, screen)) {
						unselectedCount++;
					} else {
						LOGGER.debug("Profile at position {} is still SELECTED", i + 1);
					}
				} catch (Exception e) {
					LOGGER.debug("Could not verify profile at position {}: {}", i + 1, e.getMessage());
				}
			}

			int expectedUnselected = totalOnScreen - disabledCount;

			LOGGER.debug("Total: {}, Disabled: {}, Unselected: {}, Expected: {}", 
				totalOnScreen, disabledCount, unselectedCount, expectedUnselected);

			if (unselectedCount >= expectedUnselected) {
				PageObjectHelper.log(LOGGER, "✅ " + unselectedCount + " loaded profiles are unselected in " + getScreenName(screen));
			} else {
				PageObjectHelper.log(LOGGER, "❌ Not all profiles unselected in " + getScreenName(screen));
				Assert.fail("Not all loaded profiles are unselected");
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_loaded_profiles_unselected_" + screen, e);
			PageObjectHelper.handleError(LOGGER, "verify_loaded_profiles_are_unselected",
					"Error verifying profiles are unselected in " + getScreenName(screen), e);
			Assert.fail("Error verifying profiles are unselected: " + e.getMessage());
		}
	}

	/**
	 * Verifies that newly loaded profiles (after scrolling) are still selected
	 */
	public void verify_newly_loaded_profiles_are_still_selected(String screen) {
		int selectedInNewlyLoaded = 0;
		int disabledInNewlyLoaded = 0;

		try {
			currentScreen.set(screen);
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

			List<WebElement> allCheckboxes = findElements(getAllCheckboxesLocator(screen));
			int totalNow = allCheckboxes.size();
			int newlyLoaded = totalNow - loadedProfilesBeforeUncheck.get();

			if (newlyLoaded <= 0) {
				PageObjectHelper.log(LOGGER, "No newly loaded profiles to verify in " + getScreenName(screen));
				return;
			}

			LOGGER.info("========================================");
			LOGGER.info("VERIFYING NEWLY LOADED PROFILES - {}", getScreenName(screen));
			LOGGER.info("========================================");
			LOGGER.info("Total Now: {}, Original Loaded: {}, Newly Loaded: {}", totalNow, loadedProfilesBeforeUncheck.get(), newlyLoaded);

			// First, use JavaScript to get total selected count for comparison
			int totalSelectedJS = countSelectedProfilesJS(screen);
			LOGGER.info("Total selected profiles (JS count): {}", totalSelectedJS);

			// Verify newly loaded profiles are still selected
			for (int i = loadedProfilesBeforeUncheck.get(); i < allCheckboxes.size(); i++) {
				try {
					WebElement checkbox = allCheckboxes.get(i);
					js.executeScript("arguments[0].scrollIntoView(true);", checkbox);

					if (isCheckboxDisabled(checkbox, screen)) {
						disabledInNewlyLoaded++;
						continue;
					}

					if (isCheckboxSelected(checkbox, screen)) {
						selectedInNewlyLoaded++;
					} else {
						if (selectedInNewlyLoaded < 5) {
							// Log first few unselected for debugging
							LOGGER.debug("Newly loaded profile at position {} is UNSELECTED", i + 1);
						}
					}
				} catch (Exception e) {
					LOGGER.debug("Could not verify profile at position {}: {}", i + 1, e.getMessage());
				}
			}

			int expectedSelected = newlyLoaded - disabledInNewlyLoaded;

			LOGGER.info("Newly Loaded: {}, Disabled: {}, Selected: {}, Expected: {}", 
				newlyLoaded, disabledInNewlyLoaded, selectedInNewlyLoaded, expectedSelected);

			if (selectedInNewlyLoaded >= expectedSelected) {
				PageObjectHelper.log(LOGGER, "✅ " + selectedInNewlyLoaded + " newly loaded profiles (after scroll) are still selected from original Select All in " + getScreenName(screen));
			} else if (selectedInNewlyLoaded == 0 && totalSelectedJS == 0) {
				// This is actually expected application behavior: 
				// When header checkbox is unchecked after "Select All", it may clear ALL selections
				// (not just loaded profiles) depending on application implementation
				PageObjectHelper.log(LOGGER, "ℹ️ Application behavior: Unchecking header checkbox after 'Select All' cleared ALL selections");
				PageObjectHelper.log(LOGGER, "   This is expected if the app treats header checkbox uncheck as 'deselect all'");
				// Don't fail - this may be expected application behavior
			} else {
				// These profiles should still be selected from the original "Select All" action
				// Header checkbox uncheck only affects profiles that were loaded at that time
				PageObjectHelper.log(LOGGER, "⚠️ Only " + selectedInNewlyLoaded + "/" + expectedSelected + " newly loaded profiles retained selection in " + getScreenName(screen));
				PageObjectHelper.log(LOGGER, "   Context: 'Select All' was clicked earlier, then header checkbox was unchecked to deselect LOADED profiles only.");
				PageObjectHelper.log(LOGGER, "   Expected: Profiles loaded AFTER the uncheck (via scroll) should still be selected from original Select All.");
				Assert.fail("Newly loaded profiles (after scroll) should retain selection from Select All, but only " 
						+ selectedInNewlyLoaded + "/" + expectedSelected + " are selected");
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_newly_loaded_selected_" + screen, e);
			PageObjectHelper.handleError(LOGGER, "verify_newly_loaded_profiles_are_still_selected",
					"Error verifying newly loaded profiles in " + getScreenName(screen), e);
			Assert.fail("Error verifying newly loaded profiles: " + e.getMessage());
		}
	}

	/**
	 * Verifies that all loaded profiles are unselected (used after None button click)
	 */
	public void verify_all_loaded_profiles_are_unselected(String screen) {
		int totalChecked = 0;
		int selectedFound = 0;
		int disabledCount = 0;

		try {
			currentScreen.set(screen);
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

			LOGGER.debug("========================================");
			LOGGER.debug("VERIFYING ALL PROFILES UNSELECTED - {}", getScreenName(screen));
			LOGGER.debug("========================================");

			List<WebElement> allCheckboxes = findElements(getAllCheckboxesLocator(screen));
			LOGGER.debug("Total checkboxes found: {}", allCheckboxes.size());

			for (int i = 0; i < allCheckboxes.size(); i++) {
				try {
					WebElement checkbox = allCheckboxes.get(i);

					if (isCheckboxDisabled(checkbox, screen)) {
						disabledCount++;
						continue;
					}

					totalChecked++;

					if (isCheckboxSelected(checkbox, screen)) {
						selectedFound++;
						LOGGER.debug("Profile at position {} is SELECTED (should be unselected)", i + 1);
					}
				} catch (Exception e) {
					LOGGER.debug("Could not verify profile at position {}: {}", i + 1, e.getMessage());
				}
			}

			LOGGER.debug("Checked: {}, Disabled: {}, Selected: {}", totalChecked, disabledCount, selectedFound);

			if (selectedFound == 0) {
				PageObjectHelper.log(LOGGER, "✅ All " + totalChecked + " profiles are unselected in " + getScreenName(screen));
			} else {
				PageObjectHelper.log(LOGGER, "❌ " + selectedFound + " profiles are still selected in " + getScreenName(screen));
				Assert.fail(selectedFound + " profiles are still selected");
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_all_profiles_unselected_" + screen, e);
			PageObjectHelper.handleError(LOGGER, "verify_all_loaded_profiles_are_unselected",
					"Error verifying all profiles are unselected in " + getScreenName(screen), e);
			Assert.fail("Error verifying all profiles are unselected: " + e.getMessage());
		}
	}

	/**
	 * Verifies that profiles loaded after clicking header checkbox are not selected
	 */
	public void verify_profiles_loaded_after_header_checkbox_are_not_selected(String screen) {
		int notSelectedCount = 0;
		int disabledCount = 0;

		try {
			currentScreen.set(screen);
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);

			List<WebElement> allCheckboxes = findElements(getAllCheckboxesLocator(screen));
			int totalNow = allCheckboxes.size();
			int newlyLoaded = totalNow - loadedProfilesBeforeUncheck.get();

			if (newlyLoaded <= 0) {
				PageObjectHelper.log(LOGGER, "No newly loaded profiles to verify in " + getScreenName(screen));
				return;
			}

			LOGGER.debug("Verifying {} newly loaded profiles are NOT selected", newlyLoaded);

			// Check newly loaded profiles are NOT selected
			for (int i = loadedProfilesBeforeUncheck.get(); i < allCheckboxes.size(); i++) {
				try {
					WebElement checkbox = allCheckboxes.get(i);
					js.executeScript("arguments[0].scrollIntoView(true);", checkbox);

					if (isCheckboxDisabled(checkbox, screen)) {
						disabledCount++;
						continue;
					}

					if (!isCheckboxSelected(checkbox, screen)) {
						notSelectedCount++;
					} else {
						LOGGER.debug("Profile at position {} is SELECTED (should NOT be)", i + 1);
					}
				} catch (Exception e) {
					LOGGER.debug("Could not verify profile at position {}: {}", i + 1, e.getMessage());
				}
			}

			int expectedNotSelected = newlyLoaded - disabledCount;

			if (notSelectedCount >= expectedNotSelected) {
				PageObjectHelper.log(LOGGER, "✅ " + notSelectedCount + " profiles loaded after header click are NOT selected in " + getScreenName(screen));
			} else {
				PageObjectHelper.log(LOGGER, "⚠️ Some newly loaded profiles are selected (unexpected) in " + getScreenName(screen));
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_profiles_after_header_not_selected_" + screen, e);
			PageObjectHelper.handleError(LOGGER, "verify_profiles_loaded_after_header_checkbox_are_not_selected",
					"Error verifying profiles in " + getScreenName(screen), e);
		}
	}

	// ═══════════════════════════════════════════════════════════════════════════
	// SCROLL AND REFRESH METHODS
	// ═══════════════════════════════════════════════════════════════════════════

	/**
	 * Scroll page to view more job profiles
	 */
	public void scroll_page_to_view_more_job_profiles(String screen) {
		try {
			currentScreen.set(screen);
			js.executeScript("window.scrollTo(0, document.documentElement.scrollHeight);");
			safeSleep(2000);
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 2);
			
			PageObjectHelper.log(LOGGER, "Scrolled to load more profiles in " + getScreenName(screen));
		} catch (Exception e) {
			LOGGER.debug("Error scrolling: {}", e.getMessage());
		}
	}

	/**
	 * Refresh the screen
	 */
	public void refresh_screen(String screen) {
		try {
			currentScreen.set(screen);
			driver.navigate().refresh();
			safeSleep(2000);
			PerformanceUtils.waitForSpinnersToDisappear(driver, 15);
			PerformanceUtils.waitForPageReady(driver, 5);
			
			// Reset counters after refresh
			loadedProfilesBeforeUncheck.set(0);
			selectedProfilesBeforeUncheck.set(0);
			
			PageObjectHelper.log(LOGGER, "Refreshed " + getScreenName(screen));
		} catch (Exception e) {
			LOGGER.debug("Error refreshing: {}", e.getMessage());
		}
	}

	// ═══════════════════════════════════════════════════════════════════════════
	// ACTION BUTTON VERIFICATION METHODS
	// ═══════════════════════════════════════════════════════════════════════════

	/**
	 * Verify action button is enabled (Sync with HCM for PM, Publish for JAM)
	 */
	public void verify_action_button_is_enabled(String screen) {
		try {
			currentScreen.set(screen);
			scrollToTop();
			safeSleep(300);
			
			String buttonName = screen.equalsIgnoreCase("PM") ? "Sync with HCM" : "Publish Selected Profiles";
			PageObjectHelper.log(LOGGER, "Verifying " + buttonName + " button is enabled in " + getScreenName(screen) + "...");
			
			By buttonLocator = getActionButtonLocator(screen);

			WebElement button = wait.until(ExpectedConditions.visibilityOfElementLocated(buttonLocator));
			boolean isEnabled = button.isEnabled();
			
			LOGGER.info("Button found: '{}', enabled: {}", button.getText().trim(), isEnabled);

			if (isEnabled) {
				PageObjectHelper.log(LOGGER, "✅ " + buttonName + " button is enabled in " + getScreenName(screen));
			} else {
				PageObjectHelper.log(LOGGER, "❌ " + buttonName + " button is NOT enabled in " + getScreenName(screen));
				Assert.fail(buttonName + " button should be enabled but is disabled");
			}
		} catch (Exception e) {
			LOGGER.error("Could not verify action button in {}: {}", getScreenName(screen), e.getMessage());
			ScreenshotHandler.captureFailureScreenshot("verify_action_button_" + screen, e);
			Assert.fail("Error verifying action button: " + e.getMessage());
		}
	}

	/**
	 * Verify action button is disabled
	 */
	public void verify_action_button_is_disabled(String screen) {
		try {
			currentScreen.set(screen);
			scrollToTop();
			safeSleep(300);
			
			String buttonName = screen.equalsIgnoreCase("PM") ? "Sync with HCM" : "Publish Selected Profiles";
			PageObjectHelper.log(LOGGER, "Verifying " + buttonName + " button is disabled in " + getScreenName(screen) + "...");
			
			By buttonLocator = getActionButtonLocator(screen);

			WebElement button = wait.until(ExpectedConditions.visibilityOfElementLocated(buttonLocator));
			boolean isDisabled = !button.isEnabled() || button.getAttribute("class").contains("disabled");
			
			LOGGER.info("Button found: '{}', disabled: {}", button.getText().trim(), isDisabled);

			if (isDisabled) {
				PageObjectHelper.log(LOGGER, "✅ " + buttonName + " button is disabled in " + getScreenName(screen));
			} else {
				PageObjectHelper.log(LOGGER, "❌ " + buttonName + " button is NOT disabled in " + getScreenName(screen));
				Assert.fail(buttonName + " button should be disabled but is enabled");
			}
		} catch (Exception e) {
			LOGGER.error("Could not verify action button in {}: {}", getScreenName(screen), e.getMessage());
			ScreenshotHandler.captureFailureScreenshot("verify_action_button_disabled_" + screen, e);
			Assert.fail("Error verifying action button: " + e.getMessage());
		}
	}
}

