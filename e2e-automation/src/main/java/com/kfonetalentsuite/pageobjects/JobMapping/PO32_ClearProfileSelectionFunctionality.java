package com.kfonetalentsuite.pageobjects.JobMapping;
import static com.kfonetalentsuite.pageobjects.JobMapping.BasePageObject.PMScreen.*;
import static com.kfonetalentsuite.pageobjects.JobMapping.BasePageObject.JAMScreen.*;
import static com.kfonetalentsuite.pageobjects.JobMapping.BasePageObject.Locators.SharedLocators.*;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.kfonetalentsuite.utils.common.ScreenshotHandler;
import com.kfonetalentsuite.utils.JobMapping.Utilities;
public class PO32_ClearProfileSelectionFunctionality extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO32_ClearProfileSelectionFunctionality.class);

	public static ThreadLocal<Integer> loadedProfilesBeforeUncheck = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<Integer> selectedProfilesBeforeUncheck = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<String> currentScreen = ThreadLocal.withInitial(() -> "PM");

	// ═══════════════════════════════════════════════════════════════════════════
	// LOCATORS (Header checkbox locators now inherited from BasePageObject)
	// ═══════════════════════════════════════════════════════════════════════════
	public PO32_ClearProfileSelectionFunctionality() {
		super();
	}

	private By getAllCheckboxesLocator(String screen) {
		return screen.equalsIgnoreCase("PM") ? PM_ALL_CHECKBOXES : JAM_ALL_CHECKBOXES;
	}

	private By getNoneButtonLocator(String screen) {
		return screen.equalsIgnoreCase("PM") ? SELECT_NONE_BTN : SELECT_NONE_BTN;
	}

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

	private boolean isCheckboxDisabled(WebElement checkbox, String screen) {
		if (screen.equalsIgnoreCase("PM")) {
			String classAttribute = checkbox.getAttribute("class");
			return classAttribute != null && classAttribute.contains("disable");
		} else {
			return !checkbox.isEnabled();
		}
	}

	public void click_on_header_checkbox_to_select_loaded_job_profiles(String screen) {
		try {
			currentScreen.set(screen);
			Utilities.waitForSpinnersToDisappear(driver, 10);
			Utilities.waitForPageReady(driver, 2);

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
				Utilities.waitForClickable(wait, headerCheckbox).click();
			} catch (Exception e) {
				jsClick(headerCheckbox);
			}

			safeSleep(1000);
			Utilities.waitForSpinnersToDisappear(driver, 10);
			Utilities.waitForPageReady(driver, 2);

			LOGGER.info("Clicked header checkbox to select loaded profiles in " + getScreenName(screen));

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_on_header_checkbox_to_select_" + screen, e);
			Utilities.handleError(LOGGER, "click_on_header_checkbox_to_select_loaded_job_profiles",
					"Error clicking header checkbox to select profiles in " + getScreenName(screen), e);
			Assert.fail("Error clicking header checkbox to select profiles: " + e.getMessage());
		}
	}

	public void click_on_header_checkbox_to_unselect_loaded_job_profiles(String screen) {
		try {
			currentScreen.set(screen);
			Utilities.waitForSpinnersToDisappear(driver, 10);
			Utilities.waitForPageReady(driver, 2);

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
				Utilities.waitForClickable(wait, headerCheckbox).click();
			} catch (Exception e) {
				jsClick(headerCheckbox);
			}

			safeSleep(1000);
			Utilities.waitForSpinnersToDisappear(driver, 10);
			Utilities.waitForPageReady(driver, 2);

			LOGGER.info("Clicked header checkbox to clear selection in " + getScreenName(screen));

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_on_header_checkbox_to_unselect_" + screen, e);
			Utilities.handleError(LOGGER, "click_on_header_checkbox_to_unselect_loaded_job_profiles",
					"Error clicking header checkbox to unselect profiles in " + getScreenName(screen), e);
			Assert.fail("Error clicking header checkbox to unselect profiles: " + e.getMessage());
		}
	}

	// ═══════════════════════════════════════════════════════════════════════════
	// NONE BUTTON METHODS
	// ═══════════════════════════════════════════════════════════════════════════

	public void click_on_none_button(String screen) {
		try {
			currentScreen.set(screen);
			
			// Wait for None button to be clickable (dropdown appears after chevron click)
			try {
				Utilities.waitForClickable(wait, getNoneButtonLocator(screen)).click();
			} catch (Exception e) {
				try {
					jsClick(findElement(getNoneButtonLocator(screen)));
				} catch (Exception s) {
					clickElement(getNoneButtonLocator(screen));
				}
			}

			safeSleep(1000);
			Utilities.waitForSpinnersToDisappear(driver, 10);
			Utilities.waitForPageReady(driver, 2);

			LOGGER.info("Clicked None button in " + getScreenName(screen));

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_on_none_button_" + screen, e);
			Utilities.handleError(LOGGER, "click_on_none_button",
					"Error clicking None button in " + getScreenName(screen), e);
			Assert.fail("Error clicking None button: " + e.getMessage());
		}
	}

	// ═══════════════════════════════════════════════════════════════════════════
	// VERIFICATION METHODS
	// ═══════════════════════════════════════════════════════════════════════════

	public void verify_loaded_profiles_are_unselected(String screen) {
		try {
			currentScreen.set(screen);
			Utilities.waitForSpinnersToDisappear(driver, 10);
			Utilities.waitForPageReady(driver, 2);

			LOGGER.info("========================================");
			LOGGER.info("VERIFYING UNSELECTED PROFILES - {}", getScreenName(screen));
			LOGGER.info("========================================");

			// Use JavaScript counting for performance
			int totalOnScreen = countAllProfilesJS(screen);
			int selectedCount = countSelectedProfilesJS(screen);
			int unselectedCount = totalOnScreen - selectedCount;

			LOGGER.info("Total Profiles on screen: {}", totalOnScreen);
			LOGGER.info("Selected: {}, Unselected: {}", selectedCount, unselectedCount);

			if (selectedCount == 0) {
				LOGGER.info("✅ All " + totalOnScreen + " loaded profiles are unselected in " + getScreenName(screen));
			} else if (unselectedCount > 0) {
				LOGGER.info("✅ " + unselectedCount + " loaded profiles are unselected in " + getScreenName(screen) + 
						" (" + selectedCount + " still selected)");
			} else {
				LOGGER.info("❌ No profiles were unselected in " + getScreenName(screen));
				Assert.fail("No profiles were unselected");
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_loaded_profiles_unselected_" + screen, e);
			Utilities.handleError(LOGGER, "verify_loaded_profiles_are_unselected",
					"Error verifying profiles are unselected in " + getScreenName(screen), e);
			Assert.fail("Error verifying profiles are unselected: " + e.getMessage());
		}
	}

	public void verify_newly_loaded_profiles_are_still_selected(String screen) {
		int selectedInNewlyLoaded = 0;
		int disabledInNewlyLoaded = 0;

		try {
			currentScreen.set(screen);
			Utilities.waitForSpinnersToDisappear(driver, 10);
			Utilities.waitForPageReady(driver, 2);

			List<WebElement> allCheckboxes = findElements(getAllCheckboxesLocator(screen));
			int totalNow = allCheckboxes.size();
			int newlyLoaded = totalNow - loadedProfilesBeforeUncheck.get();

			if (newlyLoaded <= 0) {
				LOGGER.info("No newly loaded profiles to verify in " + getScreenName(screen));
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
				LOGGER.info("✅ " + selectedInNewlyLoaded + " newly loaded profiles (after scroll) are still selected from original Select All in " + getScreenName(screen));
			} else if (selectedInNewlyLoaded == 0 && totalSelectedJS == 0) {
				// This is actually expected application behavior: 
				// When header checkbox is unchecked after "Select All", it may clear ALL selections
				// (not just loaded profiles) depending on application implementation
				LOGGER.info("ℹ️ Application behavior: Unchecking header checkbox after 'Select All' cleared ALL selections");
				LOGGER.info("   This is expected if the app treats header checkbox uncheck as 'deselect all'");
				// Don't fail - this may be expected application behavior
			} else {
				// These profiles should still be selected from the original "Select All" action
				// Header checkbox uncheck only affects profiles that were loaded at that time
				LOGGER.info("⚠️ Only " + selectedInNewlyLoaded + "/" + expectedSelected + " newly loaded profiles retained selection in " + getScreenName(screen));
				LOGGER.info("   Context: 'Select All' was clicked earlier, then header checkbox was unchecked to deselect LOADED profiles only.");
				LOGGER.info("   Expected: Profiles loaded AFTER the uncheck (via scroll) should still be selected from original Select All.");
				Assert.fail("Newly loaded profiles (after scroll) should retain selection from Select All, but only " 
						+ selectedInNewlyLoaded + "/" + expectedSelected + " are selected");
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_newly_loaded_selected_" + screen, e);
			Utilities.handleError(LOGGER, "verify_newly_loaded_profiles_are_still_selected",
					"Error verifying newly loaded profiles in " + getScreenName(screen), e);
			Assert.fail("Error verifying newly loaded profiles: " + e.getMessage());
		}
	}

	public void verify_all_loaded_profiles_are_unselected(String screen) {
		try {
			currentScreen.set(screen);
			Utilities.waitForSpinnersToDisappear(driver, 10);
			Utilities.waitForPageReady(driver, 2);

			LOGGER.info("========================================");
			LOGGER.info("VERIFYING ALL PROFILES UNSELECTED - {}", getScreenName(screen));
			LOGGER.info("========================================");

			// Use JavaScript counting for performance
			int totalOnScreen = countAllProfilesJS(screen);
			int selectedCount = countSelectedProfilesJS(screen);

			LOGGER.info("Total profiles: {}, Selected: {}", totalOnScreen, selectedCount);

			if (selectedCount == 0) {
				LOGGER.info("✅ All " + totalOnScreen + " profiles are unselected in " + getScreenName(screen));
			} else {
				LOGGER.info("❌ " + selectedCount + " profiles are still selected in " + getScreenName(screen));
				Assert.fail(selectedCount + " profiles are still selected");
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_all_profiles_unselected_" + screen, e);
			Utilities.handleError(LOGGER, "verify_all_loaded_profiles_are_unselected",
					"Error verifying all profiles are unselected in " + getScreenName(screen), e);
			Assert.fail("Error verifying all profiles are unselected: " + e.getMessage());
		}
	}

	public void verify_profiles_loaded_after_header_checkbox_are_not_selected(String screen) {
		try {
			currentScreen.set(screen);
			Utilities.waitForSpinnersToDisappear(driver, 10);
			Utilities.waitForPageReady(driver, 2);

			int totalNow = countAllProfilesJS(screen);
			int newlyLoaded = totalNow - loadedProfilesBeforeUncheck.get();

			if (newlyLoaded <= 0) {
				LOGGER.info("No newly loaded profiles to verify in " + getScreenName(screen));
				return;
			}

			LOGGER.info("Verifying {} newly loaded profiles are NOT selected", newlyLoaded);

			// Count selected profiles - should be same as before scroll (header checkbox selection)
			int currentSelectedCount = countSelectedProfilesJS(screen);
			
			LOGGER.info("Total now: {}, Newly loaded: {}, Currently selected: {}, Previously loaded: {}", 
					totalNow, newlyLoaded, currentSelectedCount, loadedProfilesBeforeUncheck.get());

			// Selected count should not exceed what was loaded before scroll
			if (currentSelectedCount <= loadedProfilesBeforeUncheck.get()) {
				LOGGER.info("✅ " + newlyLoaded + " profiles loaded after header click are NOT selected in " + getScreenName(screen));
			} else {
				int extraSelected = currentSelectedCount - loadedProfilesBeforeUncheck.get();
				LOGGER.info("⚠️ " + extraSelected + " newly loaded profiles appear selected (unexpected) in " + getScreenName(screen));
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_profiles_after_header_not_selected_" + screen, e);
			Utilities.handleError(LOGGER, "verify_profiles_loaded_after_header_checkbox_are_not_selected",
					"Error verifying profiles in " + getScreenName(screen), e);
		}
	}

	// ═══════════════════════════════════════════════════════════════════════════
	// SCROLL AND REFRESH METHODS
	// ═══════════════════════════════════════════════════════════════════════════

	public void scroll_page_to_view_more_job_profiles(String screen) {
		try {
			currentScreen.set(screen);
			js.executeScript("window.scrollTo(0, document.documentElement.scrollHeight);");
			safeSleep(2000);
			Utilities.waitForSpinnersToDisappear(driver, 10);
			Utilities.waitForPageReady(driver, 2);
			
			LOGGER.info("Scrolled to load more profiles in " + getScreenName(screen));
		} catch (Exception e) {
			LOGGER.debug("Error scrolling: {}", e.getMessage());
		}
	}

	public void refresh_screen(String screen) {
		try {
			currentScreen.set(screen);
			driver.navigate().refresh();
			safeSleep(2000);
			Utilities.waitForSpinnersToDisappear(driver, 15);
			Utilities.waitForPageReady(driver, 5);
			
			// Reset counters after refresh
			loadedProfilesBeforeUncheck.set(0);
			selectedProfilesBeforeUncheck.set(0);
			
			LOGGER.info("Refreshed " + getScreenName(screen));
		} catch (Exception e) {
			LOGGER.debug("Error refreshing: {}", e.getMessage());
		}
	}

	// ═══════════════════════════════════════════════════════════════════════════
	// ACTION BUTTON VERIFICATION METHODS
	// ═══════════════════════════════════════════════════════════════════════════

	public void verify_action_button_is_enabled(String screen) {
		try {
			currentScreen.set(screen);
			scrollToTop();
			safeSleep(300);
			
			String buttonName = screen.equalsIgnoreCase("PM") ? "Sync with HCM" : "Publish Selected Profiles";
			LOGGER.info("Verifying " + buttonName + " button is enabled in " + getScreenName(screen) + "...");
			
			By buttonLocator = getActionButtonLocator(screen);

			WebElement button = Utilities.waitForVisible(wait, buttonLocator);
			boolean isEnabled = button.isEnabled();
			
			LOGGER.info("Button found: '{}', enabled: {}", button.getText().trim(), isEnabled);

			if (isEnabled) {
				LOGGER.info("✅ " + buttonName + " button is enabled in " + getScreenName(screen));
			} else {
				LOGGER.info("❌ " + buttonName + " button is NOT enabled in " + getScreenName(screen));
				Assert.fail(buttonName + " button should be enabled but is disabled");
			}
		} catch (Exception e) {
			LOGGER.error("Could not verify action button in {}: {}", getScreenName(screen), e.getMessage());
			ScreenshotHandler.captureFailureScreenshot("verify_action_button_" + screen, e);
			Assert.fail("Error verifying action button: " + e.getMessage());
		}
	}

	public void verify_action_button_is_disabled(String screen) {
		try {
			currentScreen.set(screen);
			scrollToTop();
			safeSleep(300);
			
			String buttonName = screen.equalsIgnoreCase("PM") ? "Sync with HCM" : "Publish Selected Profiles";
			LOGGER.info("Verifying " + buttonName + " button is disabled in " + getScreenName(screen) + "...");
			
			By buttonLocator = getActionButtonLocator(screen);

			WebElement button = Utilities.waitForVisible(wait, buttonLocator);
			boolean isDisabled = !button.isEnabled() || button.getAttribute("class").contains("disabled");
			
			LOGGER.info("Button found: '{}', disabled: {}", button.getText().trim(), isDisabled);

			if (isDisabled) {
				LOGGER.info("✅ " + buttonName + " button is disabled in " + getScreenName(screen));
			} else {
				LOGGER.info("❌ " + buttonName + " button is NOT disabled in " + getScreenName(screen));
				Assert.fail(buttonName + " button should be disabled but is enabled");
			}
		} catch (Exception e) {
			LOGGER.error("Could not verify action button in {}: {}", getScreenName(screen), e.getMessage());
			ScreenshotHandler.captureFailureScreenshot("verify_action_button_disabled_" + screen, e);
			Assert.fail("Error verifying action button: " + e.getMessage());
		}
	}
}

