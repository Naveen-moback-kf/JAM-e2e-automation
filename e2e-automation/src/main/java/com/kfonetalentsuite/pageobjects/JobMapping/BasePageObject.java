package com.kfonetalentsuite.pageobjects.JobMapping;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.PageObjectHelper;
import com.kfonetalentsuite.webdriverManager.DriverManager;

/**
 * BasePageObject - Centralized repository for common locators and utilities
 * 
 * PURPOSE:
 * - Single source of truth for shared XPaths across all page objects
 * - Common utility methods for element interactions
 * - Reduces duplication and simplifies maintenance
 * 
 * USAGE:
 * - Extend this class in your page objects: public class PO04_Example extends BasePageObject
 * - Access locators: driver.findElement(Locators.Spinners.PAGE_LOAD_SPINNER)
 * - Use utility methods: clickElement(element), waitForElement(locator)
 * 
 * MAINTENANCE:
 * - When UI changes, update XPaths here ONLY
 * - All page objects automatically get the updated locators
 */
public class BasePageObject {

	// =============================================
	// CORE COMPONENTS - Available to all page objects
	// =============================================
	protected WebDriver driver;
	protected WebDriverWait wait;
	protected JavascriptExecutor js;
	protected static final Logger LOGGER = LogManager.getLogger(BasePageObject.class);

	/**
	 * Default constructor - initializes WebDriver and common components
	 */
	public BasePageObject() {
		this.driver = DriverManager.getDriver();
		this.wait = DriverManager.getWait();
		this.js = (JavascriptExecutor) driver;
		PageFactory.initElements(driver, this);
	}

	// =============================================
	// CENTRALIZED LOCATORS - Update XPaths here only
	// =============================================

	/**
	 * Locators class - Centralized repository for all shared XPaths
	 * Organized by UI component/section for easy navigation
	 */
	public static class Locators {

		// -----------------------------------------
		// SPINNERS & LOADERS (Used in 20+ page objects)
		// -----------------------------------------
		public static class Spinners {
			public static final By PAGE_LOAD_SPINNER = By.xpath("//*[@class='blocking-loader']//img");
			public static final By DATA_LOADER = By.xpath("//div[@data-testid='loader']//img");
			public static final By KF_LOADER = By.xpath("//div[@id='kf-loader']//*");
		}

		// -----------------------------------------
		// GLOBAL NAVIGATION (Header components)
		// -----------------------------------------
		public static class Navigation {
			public static final By KF_TALENT_SUITE_LOGO = By.xpath("//div[contains(@class,'global-nav-title-icon-container')]");
			public static final By GLOBAL_NAV_MENU_BTN = By.xpath("//button[@id='global-nav-menu-btn']");
			public static final By WAFFLE_MENU = By.xpath("//div[@data-testid='menu-icon']");
			public static final By CLIENT_NAME = By.xpath("//button[contains(@class,'global-nav-client-name')]//span");
			public static final By JOB_MAPPING_BTN = By.xpath("//button[@aria-label='Job Mapping']");
		}

		// -----------------------------------------
		// USER PROFILE (Profile dropdown)
		// -----------------------------------------
		public static class UserProfile {
			public static final By PROFILE_AVATAR = By.xpath("//*[@data-testid='global-nav-user-avatar-avatar-0']");
			public static final By PROFILE_BTN = By.xpath("//button[@id='global-nav-user-dropdown-btn']");
			public static final By PROFILE_USER_NAME = By.xpath("//div[@class='nav-profile-user-name']");
			public static final By PROFILE_EMAIL = By.xpath("//div[@class='nav-profile-email']");
			public static final By SIGN_OUT_BTN = By.xpath("//span[text()='Sign Out']");
			public static final By MY_PROFILE_BTN = By.xpath("//button[text()='My Profile']");
		}

		// -----------------------------------------
		// SEARCH & FILTERS (Common across JAM/PM screens)
		// -----------------------------------------
		public static class SearchAndFilters {
			public static final By SEARCH_BAR = By.xpath("//input[contains(@id,'search-job-title')]");
			public static final By FILTERS_BTN = By.xpath("//button[@id='filters-btn']");
			public static final By CLEAR_FILTERS_BTN = By.xpath("//button[text()='Clear']");
			public static final By APPLY_FILTERS_BTN = By.xpath("//button[text()='Apply']");
			public static final By FILTER_OPTIONS_PANEL = By.xpath("//div[@id='filters-search-btns']//div[2]//div");
		}

		// -----------------------------------------
		// TABLE & GRID (Common table elements)
		// -----------------------------------------
		public static class Table {
			public static final By HEADER_CHECKBOX = By.xpath("//th[@scope='col']//input[@type='checkbox']");
			public static final By HEADER_CHEVRON_BTN = By.xpath("//th[@scope='col']//div[@class='relative inline-block']//div//*[contains(@class,'cursor-pointer')]");
			public static final By ROW_CHECKBOXES = By.xpath("//tbody//tr//td[1]//input[@type='checkbox']");
			public static final By SELECT_ALL_BTN = By.xpath("//*[contains(text(),'Select All')]");
			public static final By SELECT_NONE_BTN = By.xpath("//*[contains(text(),'None')]");
			public static final By RESULTS_COUNT_TEXT = By.xpath("//div[@id='results-toggle-container']//p//span");
		}

		// -----------------------------------------
		// TOGGLES & BUTTONS (Common action buttons)
		// -----------------------------------------
		public static class Actions {
			public static final By VIEW_PUBLISHED_TOGGLE = By.xpath("//input[@id='toggleSwitch']");
			public static final By PUBLISH_BTN = By.xpath("//button[@id='publish-approved-mappings-btn']");
			public static final By ACCEPT_COOKIES_BTN = By.xpath("//button[@id='ensAcceptAll']");
		}

		// -----------------------------------------
		// MODALS & POPUPS (Common modal elements)
		// -----------------------------------------
		public static class Modals {
			public static final By SUCCESS_MODAL_HEADER = By.xpath("//h2[@id='modal-header']");
			public static final By SUCCESS_MODAL_MESSAGE = By.xpath("//*[@id='modal-description']");
			public static final By SUCCESS_MODAL_CLOSE_BTN = By.xpath("//button[@id='close-success-modal-btn']");
			public static final By PROFILE_DETAILS_POPUP_HEADER = By.xpath("//h2[@id='summary-modal']");
			public static final By PROFILE_DETAILS_CLOSE_BTN = By.xpath("//button[@id='close-profile-summary']");
		}

		// -----------------------------------------
		// JOB MAPPING SPECIFIC (JAM screen elements)
		// -----------------------------------------
		public static class JobMapping {
			public static final By PAGE_CONTAINER = By.xpath("//div[@id='org-job-container']");
			public static final By MAIN_HEADER = By.xpath("//h2[contains(text(),'JOB MAPPING')]");
			public static final By PAGE_TITLE_HEADER = By.xpath("//div[@id='page-heading']//h1");
			public static final By ASYNC_MESSAGE = By.xpath("//*[contains(text(),'Publish process') or contains(text(),'Please check')]");
		}

		// -----------------------------------------
		// KFONE / LOGIN (KFONE portal elements)
		// -----------------------------------------
		public static class KFONE {
			public static final By LANDING_PAGE_TITLE = By.xpath("//*[@id='title-svg-icon']");
			public static final By CLIENTS_PAGE_HEADER = By.xpath("//*[@data-testid='clients']//h2");
			public static final By CLIENTS_PAGE_TITLE = By.xpath("//h2[text()='Clients']");
			public static final By LOGIN_PAGE_TEXT = By.xpath("//*[text()='Sign in to your account']");
		}

		// -----------------------------------------
		// COMPARISON PAGE (Used in PO04,PO07,PO15,PO27)
		// -----------------------------------------
		public static class ComparisonPage {
			public static final By COMPARE_HEADER = By.xpath("//h1[@id='compare-desc']");
			public static final By CARD_HEADER = By.xpath("//div[@class='shadow']//div[contains(@id,'card-header')][1]//span");
			public static final By CARD_TITLE = By.xpath("//div[@class='shadow']//div[contains(@id,'card-title')]");
		}

		// -----------------------------------------
		// PROFILE DETAILS POPUP (Used in PO05,PO14,PO21)
		// -----------------------------------------
		public static class ProfileDetails {
			public static final By PROFILE_LEVEL_DROPDOWN = By.xpath("//select[contains(@id,'profileLevel') or @id='profile-level']");
			public static final By PUBLISH_PROFILE_BTN = By.xpath("//button[@id='publish-job-profile']");
			public static final By PROFILE_HEADER_TEXT = By.xpath("//h2[@id='summary-modal']//p");
			public static final By ROLE_SUMMARY = By.xpath("//div[@id='role-summary-container']//p");
			public static final By DETAILS_CONTAINER = By.xpath("//div[@id='details-container']//div[contains(@class,'mt-2')]");
			public static final By RESPONSIBILITIES_DATA = By.xpath("//div[@id='responsibilities-panel']//div[@id='item-container']");
			public static final By BEHAVIOUR_DATA = By.xpath("//div[@id='behavioural-panel']//div[@id='item-container']");
			public static final By SKILLS_DATA = By.xpath("//div[@id='skills-panel']//div[@id='item-container']");
			public static final By SKILLS_TAB = By.xpath("//button[text()='SKILLS']");
			public static final By BEHAVIOUR_TAB = By.xpath("//button[contains(text(),'BEHAVIOUR')]");
		}

		// -----------------------------------------
		// HCM SYNC PROFILES (Used in PO22,PO23,PO25)
		// -----------------------------------------
		public static class HCMSyncProfiles {
			public static final By SYNC_PROFILES_TITLE = By.xpath("//h1[contains(text(),'Sync Profiles')]");
			public static final By PROFILE_MANAGER_HEADER = By.xpath("//h1[contains(text(),'Profile Manager')]");
			public static final By SHOWING_RESULTS_COUNT = By.xpath("//div[contains(text(),'Showing')]");
			public static final By TABLE_HEADER_NAME = By.xpath("//thead//tr//div[@kf-sort-header='name']//div");
			public static final By TABLE_HEADER_STATUS = By.xpath("//thead//tr//div//div[text()=' Status ']");
			public static final By TABLE_HEADER_KF_GRADE = By.xpath("//thead//tr//div//div[text()=' kf grade ']");
			public static final By TABLE_HEADER_LEVEL = By.xpath("//thead//tr//div//div[text()=' Level ']");
			public static final By TABLE_HEADER_FUNCTION = By.xpath("//thead//tr//div//div[text()=' Function ']");
			public static final By TABLE_HEADER_CREATED_BY = By.xpath("//thead//tr//div//div[text()=' Created By ']");
			public static final By TABLE_HEADER_LAST_MODIFIED = By.xpath("//thead//tr//div//div[text()=' Last Modified ']");
			public static final By TABLE_HEADER_EXPORT_STATUS = By.xpath("//thead//tr//div//div[text()=' Export status ']");
			public static final By DOWNLOAD_BTN = By.xpath("//button[contains(@class,'border-button')]");
		}

		// -----------------------------------------
		// JOB MAPPING RESULTS (Used in 7+ files)
		// -----------------------------------------
		public static class JobMappingResults {
			public static final By SHOWING_JOB_RESULTS = By.xpath("//div[contains(@id,'results-toggle')]//*[contains(text(),'Showing')]");
			public static final By FIRST_ROW_PROFILE_MATCH = By.xpath("//tbody//tr[1]//td[2]//div[contains(text(),'(')]");
			public static final By FIRST_ROW_JOB_TITLE = By.xpath("//div[@id='kf-job-container']//div//table//tbody//tr[1]//td[1]//div");
			public static final By SEARCH_INPUT = By.xpath("//input[@type='search']");
		}
	}

	// =============================================
	// COMMON UTILITY METHODS
	// =============================================

	/**
	 * Click on element with multiple fallback strategies
	 * Tries: Regular click -> JS click -> JS executor click
	 */
	protected void clickElement(WebElement element) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(element)).click();
		} catch (Exception e) {
			try {
				js.executeScript("arguments[0].click();", element);
			} catch (Exception ex) {
				LOGGER.warn("Click failed for element: {}", ex.getMessage());
			}
		}
	}

	/**
	 * Click on element by locator with fallback strategies
	 * PARALLEL EXECUTION ENHANCED: Handles stale elements and page readiness
	 */
	protected void clickElement(By locator) {
		clickElementSafely(locator, 10);
	}

	/**
	 * PARALLEL EXECUTION FIX: Safe element click with stale element retry and page readiness check
	 * @param locator Element locator
	 * @param timeoutSeconds Maximum wait time
	 */
	protected void clickElementSafely(By locator, int timeoutSeconds) {
		// Wait for page to be ready before interaction
		PerformanceUtils.waitForPageReady(driver, 2);
		
		WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
		int maxRetries = 3;
		
		for (int attempt = 1; attempt <= maxRetries; attempt++) {
			try {
				// Re-fetch element on each attempt to avoid stale references
				WebElement element = customWait.until(ExpectedConditions.elementToBeClickable(locator));
				element.click();
				// Wait for UI to stabilize after click
				PerformanceUtils.waitForUIStability(driver, 1);
				return; // Success
			} catch (org.openqa.selenium.StaleElementReferenceException e) {
				if (attempt == maxRetries) {
					LOGGER.error("Element remained stale after {} attempts for locator: {}", maxRetries, locator);
					// Fallback to JavaScript click
					try {
						WebElement element = driver.findElement(locator);
						js.executeScript("arguments[0].click();", element);
						PerformanceUtils.waitForUIStability(driver, 1);
						return;
					} catch (Exception ex) {
						LOGGER.error("JavaScript click also failed: {}", ex.getMessage());
						throw new RuntimeException("Failed to click element after all retries", ex);
					}
				}
				LOGGER.debug("Stale element on attempt {}/{}, retrying...", attempt, maxRetries);
				try {
					Thread.sleep(300); // Brief pause before retry
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
					throw new RuntimeException("Interrupted during retry", ie);
				}
			} catch (Exception e) {
				if (attempt == maxRetries) {
					// Last attempt failed, try JavaScript click as fallback
					try {
						WebElement element = driver.findElement(locator);
						js.executeScript("arguments[0].click();", element);
						PerformanceUtils.waitForUIStability(driver, 1);
						LOGGER.debug("Used JavaScript click fallback for locator: {}", locator);
						return;
					} catch (Exception ex) {
						LOGGER.warn("Click failed for locator after {} attempts: {}", maxRetries, ex.getMessage());
						throw new RuntimeException("Failed to click element", ex);
					}
				}
				LOGGER.debug("Click attempt {} failed, retrying: {}", attempt, e.getMessage());
				try {
					Thread.sleep(300);
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
					throw new RuntimeException("Interrupted during retry", ie);
				}
			}
		}
	}

	/**
	 * JavaScript click - bypasses overlay issues
	 */
	protected void jsClick(WebElement element) {
		js.executeScript("arguments[0].click();", element);
	}

	/**
	 * Try multiple click strategies for stubborn elements
	 * Strategies: Regular click -> JS click -> Actions click
	 * @return true if any strategy succeeded
	 */
	protected boolean tryClickWithStrategies(WebElement element) {
		try {
			if (element.isDisplayed() && element.isEnabled()) {
				element.click();
				PerformanceUtils.waitForUIStability(driver, 1);
				return true;
			}
		} catch (Exception e) {
		}

		try {
			js.executeScript("arguments[0].click();", element);
			PerformanceUtils.waitForUIStability(driver, 1);
			return true;
		} catch (Exception e) {
		}

		try {
			new Actions(driver).moveToElement(element).click().perform();
			PerformanceUtils.waitForUIStability(driver, 1);
			return true;
		} catch (Exception e) {
		}

		return false;
	}

	/**
	 * Try multiple click strategies using By locator with logging
	 */
	protected boolean tryClickWithStrategies(By locator, String elementName) {
		try {
			WebElement element = waitForClickable(locator);
			boolean result = tryClickWithStrategies(element);
			if (result) {
				LOGGER.debug("Clicked on {}", elementName);
			}
			return result;
		} catch (Exception e) {
			LOGGER.warn("Failed to click on {}: {}", elementName, e.getMessage());
			return false;
		}
	}

	/**
	 * Handle cookies banner if present.
	 * Uses short wait (5 seconds) to avoid blocking if banner doesn't appear.
	 */
	protected void handleCookiesBanner() {
		try {
			WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
			WebElement cookiesBtn = shortWait.until(ExpectedConditions.elementToBeClickable(Locators.Actions.ACCEPT_COOKIES_BTN));
			tryClickWithStrategies(cookiesBtn);
			LOGGER.debug("Accepted cookies banner");
		} catch (Exception e) {
			// Cookie popup not present or already handled - continue silently
		}
	}

	/**
	 * Wait for element to be visible and return it
	 */
	protected WebElement waitForElement(By locator) {
		return waitForElement(locator, 10);
	}

	protected WebElement waitForElement(By locator, int timeoutSeconds) {
		WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
		return customWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	/**
	 * Wait for element to be clickable and return it
	 */
	protected WebElement waitForClickable(By locator) {
		return wait.until(ExpectedConditions.elementToBeClickable(locator));
	}

	/**
	 * Find element by locator - wrapper for driver.findElement
	 */
	protected WebElement findElement(By locator) {
		return driver.findElement(locator);
	}

	/**
	 * Find elements by locator - wrapper for driver.findElements
	 */
	protected java.util.List<WebElement> findElements(By locator) {
		return driver.findElements(locator);
	}

	/**
	 * Safely get the first element from a locator, returns null if no elements found.
	 * Use this instead of findElements(locator).get(0) to avoid IndexOutOfBoundsException.
	 */
	protected WebElement getFirstElementOrNull(By locator) {
		List<WebElement> elements = driver.findElements(locator);
		return elements.isEmpty() ? null : elements.get(0);
	}

	/**
	 * Check if elements exist for a locator (returns true if at least one element found)
	 */
	protected boolean hasElements(By locator) {
		return !driver.findElements(locator).isEmpty();
	}

	/**
	 * Check if element is displayed (with safe handling)
	 */
	protected boolean isElementDisplayed(By locator) {
		try {
			return driver.findElement(locator).isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Get text from element safely
	 */
	protected String getElementText(By locator) {
		try {
			return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText();
		} catch (Exception e) {
			LOGGER.debug("Could not get text from element: {}", e.getMessage());
			return "";
		}
	}

	/**
	 * Scroll element into view
	 */
	protected void scrollToElement(WebElement element) {
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * Scroll to top of page
	 */
	protected void scrollToTop() {
		js.executeScript("window.scrollTo(0, 0);");
	}

	/**
	 * Scroll to bottom of page
	 */
	protected void scrollToBottom() {
		js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
	}

	// =============================================
	// COMMON PAGE OPERATIONS
	// =============================================

	/**
	 * Wait for page to be fully loaded (spinners disappeared)
	 */
	protected void waitForPageLoad() {
		PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
		PerformanceUtils.waitForPageReady(driver, 3);
	}

	/**
	 * Wait for spinners to disappear with enhanced timeout and fallback
	 * PARALLEL EXECUTION FIX: Increased timeout and added fallback for persistent spinners
	 */
	protected void waitForSpinners() {
		waitForSpinners(15); // Increased default timeout from 10s to 15s
	}
	
	/**
	 * Wait for spinners with custom timeout
	 * @param timeoutSeconds Maximum time to wait for spinners to disappear
	 */
	protected void waitForSpinners(int timeoutSeconds) {
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, timeoutSeconds);
		} catch (Exception e) {
			LOGGER.warn("Spinner wait encountered issue, checking page readiness as fallback: {}", e.getMessage());
			// Fallback: Check if page is actually ready even if spinner is still visible
			try {
				PerformanceUtils.waitForPageReady(driver, 3);
				LOGGER.info("Page appears ready despite spinner visibility - continuing");
			} catch (Exception fallbackEx) {
				LOGGER.warn("Page readiness check also failed, but continuing execution: {}", fallbackEx.getMessage());
			}
		}
	}

	/**
	 * Refresh the page and wait for load
	 */
	protected void refreshPage() {
		driver.navigate().refresh();
		waitForPageLoad();
	}

	// acceptCookiesIfPresent() removed - use handleCookiesBanner() instead

	// =============================================
	// NAVIGATION HELPERS
	// =============================================

	/**
	 * Click on KF Talent Suite logo
	 */
	protected void clickLogo() {
		clickElement(Locators.Navigation.KF_TALENT_SUITE_LOGO);
		PageObjectHelper.log(LOGGER, "Clicked on KF Talent Suite Logo");
	}

	/**
	 * Open global navigation menu
	 */
	protected void openGlobalNavMenu() {
		clickElement(Locators.Navigation.GLOBAL_NAV_MENU_BTN);
		PerformanceUtils.waitForUIStability(driver, 1);
	}

	/**
	 * Navigate to Job Mapping from global menu
	 */
	protected void navigateToJobMapping() {
		openGlobalNavMenu();
		clickElement(Locators.Navigation.JOB_MAPPING_BTN);
		waitForPageLoad();
		PageObjectHelper.log(LOGGER, "Navigated to Job Mapping screen");
	}

	/**
	 * Open user profile dropdown
	 */
	protected void openUserProfile() {
		clickElement(Locators.UserProfile.PROFILE_BTN);
		PerformanceUtils.waitForUIStability(driver, 1);
	}

	/**
	 * Sign out from application
	 */
	protected void signOut() {
		openUserProfile();
		clickElement(Locators.UserProfile.SIGN_OUT_BTN);
		waitForPageLoad();
		PageObjectHelper.log(LOGGER, "Signed out from application");
	}

	// =============================================
	// TABLE HELPERS
	// =============================================

	/**
	 * Get results count from "Showing X of Y results" text element.
	 * Uses parseProfileCountFromText() for parsing logic.
	 * @return Total count (Y value) or 0 if not found
	 */
	protected int getResultsCount() {
		String countText = getElementText(Locators.Table.RESULTS_COUNT_TEXT);
		return parseProfileCountFromText(countText);
	}

	/**
	 * Click Select All button (after clicking chevron)
	 */
	protected void selectAllProfiles() {
		clickElement(Locators.Table.HEADER_CHEVRON_BTN);
		PerformanceUtils.waitForPageReady(driver, 1);
		clickElement(Locators.Table.SELECT_ALL_BTN);
		waitForSpinners();
		PageObjectHelper.log(LOGGER, "Selected all profiles");
	}

	/**
	 * Click None button to deselect all
	 */
	protected void deselectAllProfiles() {
		clickElement(Locators.Table.HEADER_CHEVRON_BTN);
		PerformanceUtils.waitForPageReady(driver, 1);
		clickElement(Locators.Table.SELECT_NONE_BTN);
		waitForSpinners();
		PageObjectHelper.log(LOGGER, "Deselected all profiles");
	}

	// =============================================
	// FILTER HELPERS
	// =============================================

	/**
	 * Open filters panel
	 */
	protected void openFilters() {
		clickElement(Locators.SearchAndFilters.FILTERS_BTN);
		PerformanceUtils.waitForUIStability(driver, 1);
	}

	/**
	 * Clear all filters
	 */
	protected void clearFilters() {
		openFilters();
		clickElement(Locators.SearchAndFilters.CLEAR_FILTERS_BTN);
		waitForSpinners();
		PageObjectHelper.log(LOGGER, "Cleared all filters");
	}

	/**
	 * Enter text in search bar
	 */
	protected void searchFor(String searchText) {
		WebElement searchBar = waitForElement(Locators.SearchAndFilters.SEARCH_BAR);
		searchBar.clear();
		searchBar.sendKeys(searchText);
		waitForSpinners();
		PageObjectHelper.log(LOGGER, "Searched for: " + searchText);
	}

	// ==================== COMMON HELPER METHODS ====================

	/**
	 * Safe sleep with exception handling - use sparingly, prefer explicit waits
	 */
	protected void safeSleep(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * Expand all "View More" buttons within a section
	 */
	protected void expandAllViewMoreButtons(By viewMoreLocator) {
		try {
			List<WebElement> viewMoreBtns = findElements(viewMoreLocator);
			for (WebElement btn : viewMoreBtns) {
				try {
					js.executeScript("arguments[0].scrollIntoView(true);", btn);
					safeSleep(200);
					jsClick(btn);
					safeSleep(300);
				} catch (Exception e) {
					LOGGER.debug("Could not click View More button: " + e.getMessage());
				}
			}
		} catch (Exception e) {
			LOGGER.debug("No View More buttons found or error expanding: " + e.getMessage());
		}
	}

	/**
	 * Extract text from a table cell element
	 */
	protected String extractCellText(WebElement cell) {
		if (cell == null) return "";
		try {
			String text = cell.getText();
			if (text == null || text.trim().isEmpty()) {
				text = cell.getAttribute("innerText");
			}
			return text != null ? text.trim() : "";
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * Format current date as MM/dd/yyyy
	 */
	protected String formatCurrentDate() {
		java.time.LocalDate today = java.time.LocalDate.now();
		java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("MM/dd/yyyy");
		return today.format(formatter);
	}

	/**
	 * Clean job name by removing extra characters
	 */
	protected String cleanJobName(String rawJobName) {
		if (rawJobName == null || rawJobName.isEmpty()) return "";
		// Remove leading/trailing whitespace and normalize
		String cleaned = rawJobName.trim();
		// Remove any parenthetical suffixes like "(Code123)"
		if (cleaned.contains("(")) {
			cleaned = cleaned.substring(0, cleaned.indexOf("(")).trim();
		}
		return cleaned;
	}

	/**
	 * Extract job name for search from full job name string
	 */
	protected String extractJobNameForSearch(String fullJobName) {
		if (fullJobName == null || fullJobName.isEmpty()) return "";
		String cleaned = cleanJobName(fullJobName);
		// Get first few words for search (to handle long names)
		String[] words = cleaned.split("\\s+");
		if (words.length > 3) {
			return words[0] + " " + words[1] + " " + words[2];
		}
		return cleaned;
	}

	/**
	 * Check if a field value indicates missing data.
	 * Returns true for: null, empty, whitespace-only, "n/a", "-", "na", "null", "none"
	 * This is the primary method for checking missing/empty values.
	 */
	protected boolean isMissingData(String value) {
		if (value == null || value.trim().isEmpty()) return true;
		String normalized = value.trim().toLowerCase();
		return normalized.equals("n/a") || normalized.equals("-") || normalized.equals("na") 
				|| normalized.equals("null") || normalized.equals("none");
	}

	/**
	 * Normalize field value for comparison
	 */
	protected String normalizeFieldValue(String fieldValue) {
		if (fieldValue == null) return "";
		String normalized = fieldValue.trim();
		// Remove common prefixes/suffixes
		if (normalized.startsWith("-")) normalized = normalized.substring(1).trim();
		if (normalized.endsWith("-")) normalized = normalized.substring(0, normalized.length() - 1).trim();
		return normalized;
	}

	/**
	 * Verify search results contain the search term
	 */
	protected void verifySearchResultsContainSearchTerm(String searchTerm, By resultsLocator) {
		try {
			List<WebElement> results = findElements(resultsLocator);
			boolean found = false;
			for (WebElement result : results) {
				if (result.getText().toLowerCase().contains(searchTerm.toLowerCase())) {
					found = true;
					break;
				}
			}
			if (!found) {
				LOGGER.warn("Search term '{}' not found in results", searchTerm);
			}
		} catch (Exception e) {
			LOGGER.debug("Error verifying search results: " + e.getMessage());
		}
	}

	/**
	 * Format current date for display in "Mon DD, YYYY" format (e.g., "Dec 04, 2025")
	 * Used for verifying publish dates and other date comparisons
	 */
	protected String formatDateForDisplay() {
		LocalDate currentDate = LocalDate.now();
		int currentYear = currentDate.getYear();
		int currentDay = currentDate.getDayOfMonth();
		Month currentMonth = currentDate.getMonth();
		String dayStr = (currentDay < 10) ? new DecimalFormat("00").format(currentDay) : String.valueOf(currentDay);
		return currentMonth.toString().substring(0, 1) + currentMonth.toString().substring(1, 3).toLowerCase() + " " + dayStr + ", " + currentYear;
	}

	/**
	 * Get row index from a table row element
	 */
	protected int getRowIndex(WebElement rowElement) {
		try {
			// Try to get from data attribute first
			String dataIndex = rowElement.getAttribute("data-row-index");
			if (dataIndex != null && !dataIndex.isEmpty()) {
				return Integer.parseInt(dataIndex);
			}
			// Fallback: count preceding siblings
			List<WebElement> allRows = driver.findElements(By.xpath("//tbody//tr"));
			for (int i = 0; i < allRows.size(); i++) {
				if (allRows.get(i).equals(rowElement)) {
					return i + 1; // 1-based index
				}
			}
		} catch (Exception e) {
			LOGGER.debug("Could not get row index: " + e.getMessage());
		}
		return -1;
	}

	/**
	 * Check new profiles for invalid selections (used in Select All validations)
	 */
	protected int checkNewProfilesForInvalidSelections(int startIndex, int endIndex, By checkboxLocator, boolean expectSelected) {
		int invalidCount = 0;
		try {
			List<WebElement> allCheckboxes = findElements(checkboxLocator);
			for (int i = startIndex; i < Math.min(endIndex, allCheckboxes.size()); i++) {
				WebElement checkbox = allCheckboxes.get(i);
				boolean isSelected = checkbox.isSelected();
				boolean isDisabled = !checkbox.isEnabled();
				
				if (!isDisabled) {
					if (expectSelected && !isSelected) {
						invalidCount++;
						LOGGER.debug("Profile at index {} should be selected but isn't", i);
					} else if (!expectSelected && isSelected) {
						invalidCount++;
						LOGGER.debug("Profile at index {} should NOT be selected but is", i);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.debug("Error checking profiles for invalid selections: " + e.getMessage());
		}
		return invalidCount;
	}

	// ==================== COMMON PARSING HELPERS ====================

	/**
	 * Parses profile count from "Showing X of Y results" text.
	 * Returns the total count (Y) from the text.
	 * 
	 * @param countText Text like "Showing 10 of 1428 results"
	 * @return The total count (1428) or 0 if parsing fails
	 */
	protected int parseProfileCountFromText(String countText) {
		try {
			if (countText != null && countText.contains("of")) {
				String afterOf = countText.substring(countText.indexOf("of") + 2).trim();
				String totalCountStr = afterOf.split("\\s+")[0].replaceAll("[^0-9]", "");
				if (!totalCountStr.isEmpty()) {
					return Integer.parseInt(totalCountStr);
				}
			}
		} catch (Exception e) {
			LOGGER.debug("Could not parse profile count from: {}", countText);
		}
		return 0;
	}

	/**
	 * Gets value or returns "[EMPTY]" placeholder for missing data.
	 * Useful for logging table cell values.
	 * Uses isMissingData() for consistency.
	 * 
	 * @param value The value to check
	 * @return The value or "[EMPTY]" if missing
	 */
	protected String getValueOrEmpty(String value) {
		return isMissingData(value) ? "[EMPTY]" : value;
	}

	// ==================== COMMON CHECKBOX COUNTING HELPERS ====================
	// Supports both kf-checkbox components (PM screens) and native checkboxes (JAM screens)

	/**
	 * Counts selected (checked) checkboxes.
	 * Supports both PM screens (kf-icon with checkbox-check) and JAM screens (native checkbox).
	 * 
	 * @param containerSelector CSS selector for the container (e.g., "tbody tr")
	 * @return Count of selected checkboxes
	 */
	protected int countSelectedCheckboxes(String containerSelector) {
		try {
			// First try PM screens - kf-icon with checkbox-check icon (XPath-based, more reliable)
			int count = driver.findElements(By.xpath(
					"//" + containerSelector.replace(" ", "//") + "[.//kf-icon[@icon='checkbox-check' and contains(@class,'ng-star-inserted')]]")).size();
			
			if (count == 0) {
				// Fallback to JAM screens - native checkbox
				JavascriptExecutor js = (JavascriptExecutor) driver;
				String jsScriptNative = "return document.querySelectorAll('" + containerSelector + 
						" td:first-child input[type=\"checkbox\"]:checked').length;";
				Object result = js.executeScript(jsScriptNative);
				count = ((Long) result).intValue();
			}
			return count;
		} catch (Exception e) {
			LOGGER.debug("Checkbox counting failed: {}", e.getMessage());
			return 0;
		}
	}

	/**
	 * Counts disabled checkboxes using JavaScript for performance.
	 * Supports both kf-checkbox components (PM screens) and native checkboxes (JAM screens).
	 * 
	 * @param containerSelector CSS selector for the container
	 * @return Count of disabled checkboxes
	 */
	protected int countDisabledCheckboxes(String containerSelector) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		try {
			// First try kf-checkbox with 'disable' class (PM screens)
			String jsScriptKfCheckbox = "return document.querySelectorAll('" + containerSelector + 
					" td:first-child kf-checkbox div.disable').length;";
			Object result = js.executeScript(jsScriptKfCheckbox);
			int count = ((Long) result).intValue();
			
			if (count == 0) {
				// Fallback to native checkbox (JAM screens)
				String jsScriptNative = "return document.querySelectorAll('" + containerSelector + 
						" td:first-child input[type=\"checkbox\"]:disabled').length;";
				result = js.executeScript(jsScriptNative);
				count = ((Long) result).intValue();
			}
			return count;
		} catch (Exception e) {
			LOGGER.debug("JavaScript disabled checkbox counting failed: {}", e.getMessage());
			return 0;
		}
	}

	/**
	 * Counts total checkboxes using JavaScript for performance.
	 * Supports both kf-checkbox components (PM screens) and native checkboxes (JAM screens).
	 * 
	 * @param containerSelector CSS selector for the container
	 * @return Count of all checkboxes
	 */
	protected int countTotalCheckboxes(String containerSelector) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		try {
			// First try kf-checkbox (PM screens)
			String jsScriptKfCheckbox = "return document.querySelectorAll('" + containerSelector + 
					" td:first-child kf-checkbox div').length;";
			Object result = js.executeScript(jsScriptKfCheckbox);
			int count = ((Long) result).intValue();
			
			if (count == 0) {
				// Fallback to native checkbox (JAM screens)
				String jsScriptNative = "return document.querySelectorAll('" + containerSelector + 
						" td:first-child input[type=\"checkbox\"]').length;";
				result = js.executeScript(jsScriptNative);
				count = ((Long) result).intValue();
			}
			return count;
		} catch (Exception e) {
			LOGGER.debug("JavaScript total checkbox counting failed: {}", e.getMessage());
			return 0;
		}
	}
	
	/**
	 * Logs all 3 profile categories: Selected, Unselected (enabled but not selected), and Disabled.
	 * 
	 * @param total Total number of profiles
	 * @param selected Count of selected profiles
	 * @param disabled Count of disabled profiles
	 */
	protected void logProfileCountSummary(int total, int selected, int disabled) {
		int unselected = total - selected - disabled;
		LOGGER.info("=== PROFILE COUNT SUMMARY ===");
		LOGGER.info("Total Profiles: {}", total);
		LOGGER.info(" Selected (to be processed): {}", selected);
		LOGGER.info(" Unselected (enabled but not selected): {}", unselected);
		LOGGER.info(" Disabled (cannot be selected): {}", disabled);
	}

	// ==================== COMMON SORTING VALIDATION HELPERS ====================

	/**
	 * Normalizes a string for sorting comparison.
	 * Removes hyphens, quotes, parentheses, and other punctuation that UI may ignore.
	 * 
	 * @param value The value to normalize
	 * @return Normalized string for comparison
	 */
	protected String normalizeForSorting(String value) {
		if (value == null) return "";
		return value.replaceAll("[-'\"()\\[\\].,:;!?]", "").trim();
	}

	/**
	 * Validates that a list of strings is in ascending order.
	 * Uses normalized comparison to match UI sorting behavior.
	 * 
	 * @param values List of values to validate
	 * @return Number of sort violations found
	 */
	protected int validateAscendingOrder(List<String> values) {
		int violations = 0;
		for (int i = 0; i < values.size() - 1; i++) {
			String currentNormalized = normalizeForSorting(values.get(i));
			String nextNormalized = normalizeForSorting(values.get(i + 1));

			// Skip comparison if both are non-ASCII (UI may sort differently)
			if (isNonAscii(currentNormalized) && isNonAscii(nextNormalized)) {
				continue;
			}

			if (currentNormalized.compareToIgnoreCase(nextNormalized) > 0) {
				violations++;
				LOGGER.error("SORT VIOLATION at Row {} -> Row {}: '{}' > '{}' (NOT in Ascending Order!)",
						(i + 1), (i + 2), values.get(i), values.get(i + 1));
			}
		}
		return violations;
	}

	/**
	 * Validates that a list of strings is in descending order.
	 * Uses normalized comparison to match UI sorting behavior.
	 * 
	 * @param values List of values to validate
	 * @return Number of sort violations found
	 */
	protected int validateDescendingOrder(List<String> values) {
		int violations = 0;
		for (int i = 0; i < values.size() - 1; i++) {
			String currentNormalized = normalizeForSorting(values.get(i));
			String nextNormalized = normalizeForSorting(values.get(i + 1));

			// Skip comparison if both are non-ASCII (UI may sort differently)
			if (isNonAscii(currentNormalized) && isNonAscii(nextNormalized)) {
				continue;
			}

			if (currentNormalized.compareToIgnoreCase(nextNormalized) < 0) {
				violations++;
				LOGGER.error("SORT VIOLATION at Row {} -> Row {}: '{}' < '{}' (NOT in Descending Order!)",
						(i + 1), (i + 2), values.get(i), values.get(i + 1));
			}
		}
		return violations;
	}

	/**
	 * Checks if a string starts with a non-ASCII character.
	 * 
	 * @param value The value to check
	 * @return true if first character is non-ASCII
	 */
	protected boolean isNonAscii(String value) {
		return value != null && !value.isEmpty() && value.charAt(0) > 127;
	}

	/**
	 * Checks if a value should be skipped in sorting validation.
	 * Uses isMissingData() for consistency with other empty/missing checks.
	 * 
	 * @param value The value to check
	 * @return true if value should be skipped
	 */
	protected boolean shouldSkipInSortValidation(String value) {
		return isMissingData(value);
	}
}

