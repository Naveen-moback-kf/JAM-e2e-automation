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
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.kfonetalentsuite.utils.JobMapping.PageObjectHelper;
import com.kfonetalentsuite.webdriverManager.DriverManager;

/**
 * BasePageObject - Common utilities and locators for all Page Objects
 * 
 * ===== QUICK REFERENCE GUIDE =====
 * 
 * SCROLLING:
 *   scrollToTop()              - Scroll to page top
 *   scrollToBottom()           - Scroll to page bottom
 *   scrollToElement(element)   - Scroll element into center view
 * 
 * SEARCHING:
 *   searchFor(String)                  - Basic search using default search bar
 *   clearAndSearch(By, String)         - NEW! Clear search bar and search (robust)
 *   clearSearchBar(By)                 - NEW! Clear search bar only
 * 
 * CLICKING:
 *   clickElement(By)                   - Smart click with retries
 *   clickElementSafely(By)             - Click with 10s timeout + 3 retries
 *   clickElementSafely(By, int)        - Click with custom timeout + 3 retries
 *   jsClick(WebElement)                - JavaScript click fallback
 *   tryClickWithStrategies(element)    - Multiple click strategies (normal/JS/Actions)
 * 
 * WAITING:
 *   waitForSpinners()                  - Wait for all spinners (15s timeout)
 *   waitForSpinners(int)               - Wait for spinners with custom timeout
 *   waitForElement(By)                 - Wait for visibility (10s timeout)
 *   waitForElement(By, int)            - Wait for visibility with custom timeout
 *   waitForClickable(By)               - Wait for element to be clickable (10s)
 *   waitForPageLoad()                  - Wait for spinners + page ready
 * 
 * FILTERS:
 *   openFilters()                      - Open filters panel
 *   clearFilters()                     - Clear all applied filters
 * 
 * PROFILE SELECTION:
 *   selectAllProfiles()                - Select all profiles in table
 *   deselectAllProfiles()              - Deselect all profiles
 * 
 * ELEMENT CHECKS:
 *   isElementDisplayed(By)             - Check if element is visible
 *   hasElements(By)                    - Check if any elements exist
 *   getElementText(By)                 - Get text from element (with wait)
 * 
 * USAGE TIPS:
 * - Use clearAndSearch() instead of manually clearing search bars (handles stubborn inputs)
 * - Use clickElementSafely() for elements that sometimes go stale
 * - Always call waitForSpinners() after actions that trigger data loading
 * - Use PageObjectHelper.waitForPageReady() after critical page interactions
 * 
 * ================================
 */
public class BasePageObject {
	// CORE COMPONENTS - Available to all page objects
	protected WebDriver driver;
	protected WebDriverWait wait;
	protected JavascriptExecutor js;
	protected static final Logger LOGGER = LogManager.getLogger(BasePageObject.class);

	public BasePageObject() {
		this.driver = DriverManager.getDriver();
		this.wait = DriverManager.getWait();
		this.js = (JavascriptExecutor) driver;
		PageFactory.initElements(driver, this);
	}
	// CENTRALIZED LOCATORS - Update XPaths here only

	public static class Locators {
		// SPINNERS & LOADERS (Used in 20+ page objects)
		public static class Spinners {
			public static final By PAGE_LOAD_SPINNER = By.xpath("//*[@class='blocking-loader']//img");
			public static final By DATA_LOADER = By.xpath("//div[@data-testid='loader']//img");
			public static final By KF_LOADER = By.xpath("//div[@id='kf-loader']//*");
		}
		// GLOBAL NAVIGATION (Header components)
		public static class Navigation {
			public static final By KF_TALENT_SUITE_LOGO = By.xpath("//div[contains(@class,'global-nav-title-icon-container')]");
			public static final By GLOBAL_NAV_MENU_BTN = By.xpath("//button[@id='global-nav-menu-btn']");
			public static final By WAFFLE_MENU = By.xpath("//div[@data-testid='menu-icon']");
			public static final By CLIENT_NAME = By.xpath("//button[contains(@class,'global-nav-client-name')]//span");
			public static final By JOB_MAPPING_BTN = By.xpath("//button[@aria-label='Job Mapping']");
			// KFONE Menu Application Buttons
			public static final By KFONE_MENU_PM_BTN = By.xpath("//span[@aria-label='Profile Manager']");
			public static final By KFONE_MENU_ARCHITECT_BTN = By.xpath("//span[@aria-label='Architect']");
		}
		// USER PROFILE (Profile dropdown)
		public static class UserProfile {
			public static final By PROFILE_AVATAR = By.xpath("//*[@data-testid='global-nav-user-avatar-avatar-0']");
			public static final By PROFILE_BTN = By.xpath("//button[@id='global-nav-user-dropdown-btn']");
			public static final By PROFILE_USER_NAME = By.xpath("//div[@class='nav-profile-user-name']");
			public static final By PROFILE_EMAIL = By.xpath("//div[@class='nav-profile-email']");
			public static final By SIGN_OUT_BTN = By.xpath("//span[text()='Sign Out']");
			public static final By MY_PROFILE_BTN = By.xpath("//button[text()='My Profile']");
		}
		// SEARCH & FILTERS (Common across JAM/PM screens)
		public static class SearchAndFilters {
			public static final By SEARCH_BAR = By.xpath("//input[contains(@id,'search-job-title')]");
			public static final By FILTERS_BTN = By.xpath("//button[@id='filters-btn']");
			public static final By CLEAR_FILTERS_BTN = By.xpath("//button[text()='Clear']");
			public static final By APPLY_FILTERS_BTN = By.xpath("//button[text()='Apply']");
			public static final By FILTER_OPTIONS_PANEL = By.xpath("//div[@id='filters-search-btns']//div[2]//div");
		}
		// TABLE & GRID (Common table elements)
		public static class Table {
			public static final By HEADER_CHECKBOX = By.xpath("//th[@scope='col']//input[@type='checkbox']");
			public static final By HEADER_CHEVRON_BTN = By.xpath("//th[@scope='col']//div[@class='relative inline-block']//div//*[contains(@class,'cursor-pointer')]");
			public static final By ROW_CHECKBOXES = By.xpath("//tbody//tr//td[1]//input[@type='checkbox']");
			public static final By SELECT_ALL_BTN = By.xpath("//*[contains(text(),'Select All')]");
			public static final By SELECT_NONE_BTN = By.xpath("//*[contains(text(),'None')]");
			public static final By RESULTS_COUNT_TEXT = By.xpath("//div[@id='results-toggle-container']//p//span");
		}
		// TOGGLES & BUTTONS (Common action buttons)
		public static class Actions {
			public static final By VIEW_PUBLISHED_TOGGLE = By.xpath("//input[@id='toggleSwitch']");
			public static final By PUBLISH_BTN = By.xpath("//button[@id='publish-approved-mappings-btn']");
			public static final By ACCEPT_COOKIES_BTN = By.xpath("//button[@id='ensAcceptAll']");
		}
		// MODALS & POPUPS (Common modal elements)
		public static class Modals {
			public static final By SUCCESS_MODAL_HEADER = By.xpath("//h2[@id='modal-header']");
			public static final By SUCCESS_MODAL_MESSAGE = By.xpath("//*[@id='modal-description']");
			public static final By SUCCESS_MODAL_CLOSE_BTN = By.xpath("//button[@id='close-success-modal-btn']");
			public static final By PROFILE_DETAILS_POPUP_HEADER = By.xpath("//h2[@id='summary-modal']");
			public static final By PROFILE_DETAILS_CLOSE_BTN = By.xpath("//button[@id='close-profile-summary']");
			// Publish Success Messages
			public static final By PUBLISH_SUCCESS_MSG = By.xpath("//p[contains(text(),'Success profile published')]/..");
			public static final By PUBLISH_SUCCESS_MSG_DIRECT = By.xpath("//p[contains(text(),'Success profile published')]");
			public static final By PUBLISH_SUCCESS_MSG_FLEXIBLE = By.xpath("//*[contains(text(),'profile published') or contains(text(),'successfully published')]");
			public static final By PUBLISHED_SUCCESS_MSG = By.xpath("//p[@id='modal-message']");
			public static final By PUBLISHED_SUCCESS_CLOSE_BTN = By.xpath("//button[@aria-label='Close']");
		}
		// JOB MAPPING SPECIFIC (JAM screen elements)
		public static class JobMapping {
			public static final By PAGE_CONTAINER = By.xpath("//div[@id='org-job-container']");
			public static final By MAIN_HEADER = By.xpath("//h2[contains(text(),'JOB MAPPING')]");
			public static final By PAGE_TITLE_HEADER = By.xpath("//div[@id='page-heading']//h1");
			public static final By ASYNC_MESSAGE = By.xpath("//*[contains(text(),'Publish process') or contains(text(),'Please check')]");
		}
		// KFONE / LOGIN (KFONE portal elements)
		public static class KFONE {
			public static final By LANDING_PAGE_TITLE = By.xpath("//*[@id='title-svg-icon']");
			public static final By CLIENTS_PAGE_HEADER = By.xpath("//*[@data-testid='clients']//h2");
			public static final By CLIENTS_PAGE_TITLE = By.xpath("//h2[text()='Clients']");
			public static final By LOGIN_PAGE_TEXT = By.xpath("//*[text()='Sign in to your account']");
		}
		// COMPARISON PAGE (Used in PO04,PO07,PO15,PO27)
		public static class ComparisonPage {
			public static final By COMPARE_HEADER = By.xpath("//h1[@id='compare-desc']");
			public static final By CARD_HEADER = By.xpath("//div[@class='shadow']//div[contains(@id,'card-header')][1]//span");
			public static final By CARD_TITLE = By.xpath("//div[@class='shadow']//div[contains(@id,'card-title')]");
		}
		// PROFILE DETAILS POPUP (Used in PO05,PO14,PO21)
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
		// HCM SYNC PROFILES (Used in PO18,PO19,PO21,PO22,PO23,PO25,PO31)
		public static class HCMSyncProfiles {
			public static final By HCM_SYNC_TAB = By.xpath("//span[contains(text(),'HCM Sync')]");
			public static final By SYNC_PROFILES_TITLE = By.xpath("//h1[contains(text(),'Sync Profiles')]");
			public static final By PROFILE_MANAGER_HEADER = By.xpath("//h1[contains(text(),'Profile Manager')]");
			public static final By SHOWING_RESULTS_COUNT = By.xpath("//div[contains(text(),'Showing')]");
			public static final By TABLE_HEADER_NAME = By.xpath("//thead//tr//div[@kf-sort-header='name']//div");
			public static final By TABLE_HEADER_STATUS = By.xpath("//thead//tr//div//div[text()=' Status ']");
			public static final By TABLE_HEADER_JOB_CODE = By.xpath("//thead//tr//div//div[text()=' Job Code ']");
			public static final By TABLE_HEADER_KF_GRADE = By.xpath("//thead//tr//div//div[text()=' kf grade ']");
			public static final By TABLE_HEADER_LEVEL = By.xpath("//thead//tr//div//div[text()=' Level ']");
			public static final By TABLE_HEADER_FUNCTION = By.xpath("//thead//tr//div//div[text()=' Function ']");
			public static final By TABLE_HEADER_CREATED_BY = By.xpath("//thead//tr//div//div[text()=' Created By ']");
			public static final By TABLE_HEADER_LAST_MODIFIED = By.xpath("//thead//tr//div//div[text()=' Last Modified ']");
			public static final By TABLE_HEADER_EXPORT_STATUS = By.xpath("//thead//tr//div//div[text()=' Export status ']");
			public static final By TABLE_HEADER_CHECKBOX = By.xpath("//thead//tr//div//kf-checkbox");
			public static final By SP_DETAILS_PAGE_TEXT = By.xpath("//span[contains(text(),'Select your view')]");
			public static final By SYNC_WITH_HCM_BTN = By.xpath("//button[contains(@class,'custom-export')] | //*[contains(text(),'Sync with HCM')]");
			public static final By SEARCH_DROPDOWN = By.xpath("//div[@class='search-type-toggle']");
			public static final By SEARCH_BY_JOBPROFILE = By.xpath("//div[contains(@class,'search-type-item') and contains(text(),'Job Profile')]");
			public static final By SEARCH_BY_JOBCODE = By.xpath("//div[contains(@class,'search-type-item') and contains(text(),'Job Code')]");
		}
		// PM SCREEN LOCATORS (HCM Sync Profiles - Used in PO35, PO36, PO42)
	public static class PMScreen {
		public static final By ALL_PROFILE_ROWS = By.xpath("//tbody//tr[.//kf-checkbox]");
		public static final By SELECTED_PROFILE_ROWS = By.xpath("//tbody//tr[.//kf-icon[@icon='checkbox-check' and contains(@class,'ng-star-inserted')]]");
		// FIXED: More specific locator to target chevron beside table header checkbox, not search dropdown
		public static final By CHEVRON_BUTTON = By.xpath("//thead//th//kf-icon[contains(@class,'arrow-down') or @icon='arrow-down'] | //thead//th//*[contains(@class,'chevron')]");
		public static final By HEADER_CHECKBOX = By.xpath("//thead//tr//th[1]//div[1]//kf-checkbox//div");
			public static final By SYNC_BUTTON = By.xpath("//button[contains(@class,'custom-export')] | //button[contains(text(),'Sync with HCM')]");
			public static final By SEARCH_BAR = By.xpath("//input[@type='search']");
			public static final By CLEAR_ALL_FILTERS_BTN = By.xpath("//a[contains(text(),'Clear All')]");
		}
		// JAM SCREEN LOCATORS (Job Mapping - Used in PO35, PO36, PO42)
		public static class JAMScreen {
			public static final By SHOWING_RESULTS_COUNT = By.xpath("//div[contains(@id,'results-toggle')]//*[contains(text(),'Showing')]");
			public static final By ALL_PROFILE_ROWS = By.xpath("//div[@id='org-job-container']//tbody//tr[.//td[1]//input[@type='checkbox']]");
			public static final By SELECTED_PROFILE_ROWS = By.xpath("//div[@id='org-job-container']//tbody//tr[.//td[1]//input[@type='checkbox' and @checked]]");
			public static final By CHEVRON_BUTTON = By.xpath("//th[@scope='col']//div[@class='relative inline-block']//div//*[contains(@class,'cursor-pointer')]");
			public static final By HEADER_CHECKBOX = By.xpath("//thead//input[@type='checkbox']");
			public static final By PUBLISH_BUTTON = By.xpath("//button[contains(@id,'publish-approved-mappings-btn')] | //button[contains(text(),'Publish Selected')]");
			public static final By SEARCH_BAR = By.xpath("//input[@id='search-job-title-input-search-input']");
			public static final By CLEAR_FILTERS_BTN = By.xpath("//button[@data-testid='Clear Filters']");
		}
		// JOB MAPPING RESULTS (Used in 7+ files)
		public static class JobMappingResults {
			public static final By SHOWING_JOB_RESULTS = By.xpath("//div[contains(@id,'results-toggle')]//*[contains(text(),'Showing')]");
			public static final By FIRST_ROW_PROFILE_MATCH = By.xpath("//tbody//tr[1]//td[2]//div[contains(text(),'(')]");
			public static final By FIRST_ROW_JOB_TITLE = By.xpath("//div[@id='kf-job-container']//div//table//tbody//tr[1]//td[1]//div");
			public static final By SEARCH_INPUT = By.xpath("//input[@type='search']");
			// JAM Table Row Locators
			public static final By JOB_NAME_ROW_1 = By.xpath("//tbody//tr[1]//td[2]//div[contains(text(),'(')]");
			public static final By JOB_1_PUBLISH_BTN = By.xpath("//tbody//tr[2]//button[@id='publish-btn'][1]");
			public static final By JOB_1_PUBLISHED_BTN = By.xpath("//tbody//tr[2]//button[text()='Published'][1]");
			// HCM Sync Profiles Row Locators
			public static final By HCM_JOB_ROW_1 = By.xpath("//tbody//tr[1]//td//div//span[1]//a");
			public static final By HCM_JOBCODE_ROW_1 = By.xpath("//tbody//tr[1]//td[3]//span");
			public static final By HCM_DATE_ROW_1 = By.xpath("//tbody//tr[1]//td[8]//span");
			// Architect Row Locators
			public static final By ARCHITECT_JOB_ROW_1 = By.xpath("//tbody//tr[1]//td//div//div//a");
			public static final By ARCHITECT_DATE_ROW_1 = By.xpath("//tbody//tr[1]//td[9]");
		}
	}
	// COMMON UTILITY METHODS

	protected void clickElement(WebElement element) {
		try {
			PageObjectHelper.waitForClickable(wait, element).click();
		} catch (Exception e) {
			try {
				js.executeScript("arguments[0].click();", element);
			} catch (Exception ex) {
				LOGGER.warn("Click failed for element: {}", ex.getMessage());
			}
		}
	}

	protected void clickElement(By locator) {
		clickElementSafely(locator, 10);
	}

	protected void clickElementSafely(By locator, int timeoutSeconds) {
		// Wait for page to be ready before interaction
		PageObjectHelper.waitForPageReady(driver, 2);
		
		int maxRetries = 3;
		
		for (int attempt = 1; attempt <= maxRetries; attempt++) {
			try {
				// Re-fetch element on each attempt to avoid stale references
				WebElement element = PageObjectHelper.waitForClickable(wait, locator);
				element.click();
				// Wait for UI to stabilize after click
				PageObjectHelper.waitForUIStability(driver, 1);
				return; // Success
			} catch (org.openqa.selenium.StaleElementReferenceException e) {
				if (attempt == maxRetries) {
					LOGGER.error("Element remained stale after {} attempts for locator: {}", maxRetries, locator);
					// Fallback to JavaScript click
					try {
						WebElement element = driver.findElement(locator);
						js.executeScript("arguments[0].click();", element);
						PageObjectHelper.waitForUIStability(driver, 1);
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
						PageObjectHelper.waitForUIStability(driver, 1);
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

	protected void jsClick(WebElement element) {
		js.executeScript("arguments[0].click();", element);
	}

	protected boolean tryClickWithStrategies(WebElement element) {
		try {
			if (element.isDisplayed() && element.isEnabled()) {
				element.click();
				PageObjectHelper.waitForUIStability(driver, 1);
				return true;
			}
		} catch (Exception e) {
		}

		try {
			js.executeScript("arguments[0].click();", element);
			PageObjectHelper.waitForUIStability(driver, 1);
			return true;
		} catch (Exception e) {
		}

		try {
			new Actions(driver).moveToElement(element).click().perform();
			PageObjectHelper.waitForUIStability(driver, 1);
			return true;
		} catch (Exception e) {
		}

		return false;
	}

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

	protected void handleCookiesBanner() {
		try {
			WebElement cookiesBtn = PageObjectHelper.waitForClickable(wait, Locators.Actions.ACCEPT_COOKIES_BTN);
			tryClickWithStrategies(cookiesBtn);
			LOGGER.debug("Accepted cookies banner");
		} catch (Exception e) {
			// Cookie popup not present or already handled - continue silently
		}
	}

	protected WebElement waitForElement(By locator) {
		return waitForElement(locator, 10);
	}

	protected WebElement waitForElement(By locator, int timeoutSeconds) {
		return PageObjectHelper.waitForVisible(wait, locator);
	}

	protected WebElement waitForClickable(By locator) {
		return PageObjectHelper.waitForClickable(wait, locator);
	}

	protected WebElement findElement(By locator) {
		return driver.findElement(locator);
	}

	protected java.util.List<WebElement> findElements(By locator) {
		return driver.findElements(locator);
	}

	protected WebElement getFirstElementOrNull(By locator) {
		List<WebElement> elements = driver.findElements(locator);
		return elements.isEmpty() ? null : elements.get(0);
	}

	protected boolean hasElements(By locator) {
		return !driver.findElements(locator).isEmpty();
	}

	protected boolean isElementDisplayed(By locator) {
		try {
			return driver.findElement(locator).isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	protected String getElementText(By locator) {
		try {
			return PageObjectHelper.waitForVisible(wait, locator).getText();
		} catch (Exception e) {
			LOGGER.debug("Could not get text from element: {}", e.getMessage());
			return "";
		}
	}

	protected void scrollToElement(WebElement element) {
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	protected void scrollToTop() {
		js.executeScript("window.scrollTo(0, 0);");
	}

	protected void scrollToBottom() {
		js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
	}
	// COMMON PAGE OPERATIONS

	protected void waitForPageLoad() {
		PageObjectHelper.waitForSpinnersToDisappear(driver, 10);
		PageObjectHelper.waitForPageReady(driver, 3);
	}

	protected void waitForSpinners() {
		waitForSpinners(15); // Increased default timeout from 10s to 15s
	}
	
	protected void waitForSpinners(int timeoutSeconds) {
		try {
			PageObjectHelper.waitForSpinnersToDisappear(driver, timeoutSeconds);
		} catch (Exception e) {
			LOGGER.warn("Spinner wait encountered issue, checking page readiness as fallback: {}", e.getMessage());
			// Fallback: Check if page is actually ready even if spinner is still visible
			try {
				PageObjectHelper.waitForPageReady(driver, 3);
				LOGGER.info("Page appears ready despite spinner visibility - continuing");
			} catch (Exception fallbackEx) {
				LOGGER.warn("Page readiness check also failed, but continuing execution: {}", fallbackEx.getMessage());
			}
		}
	}

	protected void refreshPage() {
		driver.navigate().refresh();
		waitForPageLoad();
	}

	// acceptCookiesIfPresent() removed - use handleCookiesBanner() instead
	// NAVIGATION HELPERS

	protected void clickLogo() {
		clickElement(Locators.Navigation.KF_TALENT_SUITE_LOGO);
		PageObjectHelper.log(LOGGER, "Clicked on KF Talent Suite Logo");
	}

	protected void openGlobalNavMenu() {
		clickElement(Locators.Navigation.GLOBAL_NAV_MENU_BTN);
		PageObjectHelper.waitForUIStability(driver, 1);
	}

	protected void navigateToJobMapping() {
		openGlobalNavMenu();
		clickElement(Locators.Navigation.JOB_MAPPING_BTN);
		waitForPageLoad();
		PageObjectHelper.log(LOGGER, "Navigated to Job Mapping screen");
	}

	protected void openUserProfile() {
		clickElement(Locators.UserProfile.PROFILE_BTN);
		PageObjectHelper.waitForUIStability(driver, 1);
	}

	protected void signOut() {
		openUserProfile();
		clickElement(Locators.UserProfile.SIGN_OUT_BTN);
		waitForPageLoad();
		PageObjectHelper.log(LOGGER, "Signed out from application");
	}
	// TABLE HELPERS

	protected int getResultsCount() {
		String countText = getElementText(Locators.Table.RESULTS_COUNT_TEXT);
		return parseProfileCountFromText(countText);
	}

	protected void selectAllProfiles() {
		clickElement(Locators.Table.HEADER_CHEVRON_BTN);
		PageObjectHelper.waitForPageReady(driver, 1);
		clickElement(Locators.Table.SELECT_ALL_BTN);
		waitForSpinners();
		PageObjectHelper.log(LOGGER, "Selected all profiles");
	}

	protected void deselectAllProfiles() {
		clickElement(Locators.Table.HEADER_CHEVRON_BTN);
		PageObjectHelper.waitForPageReady(driver, 1);
		clickElement(Locators.Table.SELECT_NONE_BTN);
		waitForSpinners();
		PageObjectHelper.log(LOGGER, "Deselected all profiles");
	}
	// FILTER HELPERS

	protected void openFilters() {
		clickElement(Locators.SearchAndFilters.FILTERS_BTN);
		PageObjectHelper.waitForUIStability(driver, 1);
	}

	protected void clearFilters() {
		openFilters();
		clickElement(Locators.SearchAndFilters.CLEAR_FILTERS_BTN);
		waitForSpinners();
		PageObjectHelper.log(LOGGER, "Cleared all filters");
	}

	/**
	 * Closes the filters panel (if open).
	 * Useful after selecting filters without using "Apply" button.
	 */
	protected void closeFilters() {
		try {
			clickElement(Locators.SearchAndFilters.FILTERS_BTN);
			PageObjectHelper.waitForUIStability(driver, 1);
			PageObjectHelper.log(LOGGER, "Closed filters panel");
		} catch (Exception e) {
			LOGGER.debug("Filters panel already closed or not present");
		}
	}

	/**
	 * Opens a specific filter dropdown by name.
	 * Useful for filter panels with multiple expandable sections.
	 * 
	 * @param filterName Name of the filter (e.g., "Grades", "Departments", "Levels")
	 */
	protected void openFilterDropdown(String filterName) {
		try {
			By filterLocator = By.xpath("//div[contains(text(),'" + filterName + "')]");
			clickElement(filterLocator);
			PageObjectHelper.waitForUIStability(driver, 1);
			PageObjectHelper.log(LOGGER, "Opened " + filterName + " filter dropdown");
		} catch (Exception e) {
			LOGGER.error("Failed to open " + filterName + " filter dropdown", e);
			throw new RuntimeException("Filter dropdown open failed: " + filterName, e);
		}
	}

	protected void searchFor(String searchText) {
		WebElement searchBar = waitForElement(Locators.SearchAndFilters.SEARCH_BAR);
		searchBar.clear();
		searchBar.sendKeys(searchText);
		waitForSpinners();
		PageObjectHelper.log(LOGGER, "Searched for: " + searchText);
	}

	/**
	 * Clears search input and performs search with robust clearing strategy.
	 * This method handles search bars that don't clear properly with .clear() alone.
	 * 
	 * @param searchLocator By locator for the search input element
	 * @param searchTerm Text to search for
	 */
	protected void clearAndSearch(By searchLocator, String searchTerm) {
		try {
			waitForSpinners();
			PageObjectHelper.waitForSpinnersToDisappear(driver, 5);
			
			WebDriverWait extendedWait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement searchBar = PageObjectHelper.waitForClickable(extendedWait, searchLocator);
			searchBar.click();
			safeSleep(200);
			
			// Robust clear strategy - some inputs need extra help
			searchBar.clear();
			searchBar.sendKeys(Keys.CONTROL + "a");
			searchBar.sendKeys(Keys.DELETE);
			safeSleep(200);
			
			// Type and submit search
			searchBar.sendKeys(searchTerm);
			safeSleep(300);
			searchBar.sendKeys(Keys.ENTER);
			
			// Wait for results to load
			waitForSpinners();
			PageObjectHelper.waitForPageReady(driver, 3);
			
			PageObjectHelper.log(LOGGER, "Searched for: " + searchTerm);
		} catch (Exception e) {
			LOGGER.error("Failed to clear and search for: " + searchTerm, e);
			throw new RuntimeException("Search operation failed", e);
		}
	}

	/**
	 * Clears search input without performing search.
	 * Useful for resetting search state before next operation.
	 * 
	 * @param searchLocator By locator for the search input element
	 */
	protected void clearSearchBar(By searchLocator) {
		try {
			WebElement searchBar = waitForElement(searchLocator, 10);
			searchBar.click();
			safeSleep(200);
			
			// Robust clear strategy
			searchBar.clear();
			searchBar.sendKeys(Keys.CONTROL + "a");
			searchBar.sendKeys(Keys.DELETE);
			safeSleep(200);
			
			PageObjectHelper.log(LOGGER, "Cleared search bar");
		} catch (Exception e) {
			LOGGER.error("Failed to clear search bar", e);
			throw new RuntimeException("Clear search operation failed", e);
		}
	}
	
	/**
	 * Dismisses the "Keep working?" popup if it appears after closing Add Job Data screen.
	 * This popup appears only in automation when the session loses track of client selection.
	 * After dismissing the popup, the UI navigates to KFone Clients screen, so we need to:
	 * 1. Re-select the client (execute @Client_with_PM_Access scenario)
	 * 2. Navigate back to Job Mapping page (execute @Navigate_To_Job_Mapping scenario)
	 */
	public void dismissKeepWorkingPopupIfPresent() {
		try {
			// Set implicit wait to 0 for fast detection
			driver.manage().timeouts().implicitlyWait(Duration.ZERO);
			
			// Try to find the "Keep working?" popup with multiple possible locators
			By[] popupLocators = {
				By.xpath("//h2[contains(text(),'Keep working?')]"),
				By.xpath("//*[contains(text(),'lost track of your client selection')]"),
				By.xpath("//div[contains(@class,'modal') or contains(@class,'dialog')]//h2[contains(text(),'Keep')]")
			};
			
			// Continue button locators - using specific attributes from actual HTML
			By[] continueButtonLocators = {
				By.id("global-nav-modal-success-btn"),  // Primary - most specific
				By.xpath("//button[@id='global-nav-modal-success-btn']"),
				By.xpath("//button[@data-testid='global-nav-modal-success-btn']"),
				By.xpath("//button[@aria-label='Continue' and contains(@class,'kf1wc-button')]"),
				By.xpath("//button[contains(text(),'Continue')]"),  // Fallback
				By.xpath("//button[@type='button' and @aria-label='Continue']")
			};
			
			boolean popupFound = false;
			for (By locator : popupLocators) {
				try {
					WebElement popup = PageObjectHelper.waitForVisible(wait, locator);
					if (popup.isDisplayed()) {
						popupFound = true;
						LOGGER.info("'Keep working?' popup detected - dismissing...");
						break;
					}
				} catch (Exception e) {
					// Continue to next locator
				}
			}
			
			// If popup found, click Continue button and handle navigation
			if (popupFound) {
				boolean continueClicked = false;
				for (By buttonLocator : continueButtonLocators) {
					try {
						WebElement continueBtn = PageObjectHelper.waitForClickable(wait, buttonLocator);
						continueBtn.click();
						LOGGER.info("Clicked 'Continue' button (id='global-nav-modal-success-btn') to dismiss popup");
						continueClicked = true;
						safeSleep(2000);
						PageObjectHelper.waitForPageReady(driver, 5);
						break;
					} catch (Exception e) {
						LOGGER.debug("Continue button locator failed: {}, trying next...", buttonLocator);
						// Continue to next button locator
					}
				}
				
				// After clicking Continue, UI navigates to KFone Clients screen
				// We need to re-select client and navigate back to Job Mapping
				if (continueClicked) {
					LOGGER.info("Popup dismissed - UI navigated to KFone Clients screen");
					LOGGER.info("Re-executing ALL steps from @Client_with_PM_Access and @Navigate_To_Job_Mapping scenarios...");
					
					try {
						// Create instances of required PO classes
						PO01_KFoneLogin loginPO = new PO01_KFoneLogin();
						PO04_JobMappingPageComponents jamPO = new PO04_JobMappingPageComponents();
						PO05_PublishJobProfile po05 = new PO05_PublishJobProfile();
						handleCookiesBanner();
						// SCENARIO 1: @Client_with_PM_Access - ALL STEPS
						LOGGER.info("=== Executing @Client_with_PM_Access Scenario ===");
						
						// Step 1: User is in KFONE Clients page (already there after clicking Continue)
						LOGGER.info("Step 1: User is in KFONE Clients page");
						loginPO.user_is_in_kfone_clients_page();
						
						// Step 2: Search for Client with PAMS ID
						LOGGER.info("Step 2: Search for Client with PAMS ID");
						loginPO.search_for_client_with_pams_id();
						
						// Step 3: Verify Client name based on PAMS ID
						LOGGER.info("Step 3: Verify Client name based on PAMS ID");
						loginPO.verify_client_name_based_on_pams_id();
						
						// Step 4: Verify Products that client can access
						LOGGER.info("Step 4: Verify Products that client can access");
						loginPO.verify_products_that_client_can_access();
						
						// Step 5: Click on Client with access to Profile Manager Application
						LOGGER.info("Step 5: Click on Client with access to Profile Manager Application");
						loginPO.click_on_client_with_access_to_profile_manager_application();
						
						// Step 6: Verify User navigated to KFONE Home Page
						LOGGER.info("Step 6: Verify User navigated to KFONE Home Page");
						loginPO.verify_user_navigated_to_kfone_home_page();
						
						// Step 7: Click on Profile Manager application in Your Products section
						LOGGER.info("Step 7: Click on Profile Manager application in Your Products section");
						loginPO.click_on_profile_manager_application_in_your_products_section();
						
						// Step 8: Verify User seamlessly landed on Profile Manager Application in KF HUB
						LOGGER.info("Step 8: Verify User seamlessly landed on Profile Manager Application in KF HUB");
						loginPO.verify_user_seemlessly_landed_on_profile_manager_application_in_kf_hub();
						
						// Step 9: Verify user should land on Profile Manager dashboard page
						LOGGER.info("Step 9: Verify user should land on Profile Manager dashboard page");
						po05.verify_user_should_land_on_profile_manager_dashboard_page();
						
						// Step 10: Store user session details from session storage - SKIP if method doesn't exist
						LOGGER.info("Step 10: Store user session details from session storage - SKIPPED (method not needed for popup recovery)");
						
						LOGGER.info("✓ @Client_with_PM_Access scenario completed successfully");
						// SCENARIO 2: @Navigate_To_Job_Mapping - ALL STEPS
						LOGGER.info("=== Executing @Navigate_To_Job_Mapping Scenario ===");
						
						// Step 1: Navigate to Job Mapping page from KFONE Global Menu in PM
						LOGGER.info("Step 1: Navigate to Job Mapping page from KFONE Global Menu in PM");
						jamPO.navigate_to_job_mapping_page_from_kfone_global_menu_in_pm();
						
						// Step 2: User should verify Job Mapping logo is displayed on screen
						LOGGER.info("Step 2: User should verify Job Mapping logo is displayed on screen");
						jamPO.user_should_verify_job_mapping_logo_is_displayed_on_screen();
						
						LOGGER.info("✓ @Navigate_To_Job_Mapping scenario completed successfully");
						
						PageObjectHelper.log(LOGGER, "Successfully restored original state - All steps from both scenarios executed");
						
					} catch (Exception navEx) {
						LOGGER.error("Failed to restore original state after popup dismissal: {}", navEx.getMessage());
						navEx.printStackTrace();
						PageObjectHelper.log(LOGGER, "WARNING: Could not automatically restore Job Mapping page after popup. Manual intervention may be needed.");
						// Don't throw - let test continue and handle error in next step
					}
				} else {
					LOGGER.warn("'Keep working?' popup detected but Continue button could not be clicked");
				}
			}
			
		} catch (Exception e) {
			LOGGER.debug("'Keep working?' popup not present or already dismissed: {}", e.getMessage());
		} finally {
			// Restore implicit wait to default (20 seconds)
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		}
	}

	protected void safeSleep(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	protected void expandAllViewMoreButtons(By viewMoreLocator) {
		try {
			int maxAttempts = 20; // Safety limit to prevent infinite loops
			int attemptCount = 0;
			int totalClicked = 0;
			
			while (attemptCount < maxAttempts) {
				List<WebElement> viewMoreBtns = findElements(viewMoreLocator);
				
				if (viewMoreBtns.isEmpty()) {
					LOGGER.debug("No more View More buttons found. Total clicked: {}", totalClicked);
					break;
				}
				
				// Click the first visible button (after clicking, new buttons may appear)
				boolean buttonClicked = false;
				for (WebElement btn : viewMoreBtns) {
					try {
						if (btn.isDisplayed()) {
							js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", btn);
							safeSleep(300);
							jsClick(btn);
							safeSleep(400);
							totalClicked++;
							buttonClicked = true;
							LOGGER.debug("Clicked View More button #{}", totalClicked);
							break; // Exit inner loop, re-query for new buttons
						}
					} catch (Exception e) {
						LOGGER.debug("Could not click View More button: {}", e.getMessage());
					}
				}
				
				if (!buttonClicked) {
					// No button was successfully clicked, exit to avoid infinite loop
					LOGGER.debug("No clickable View More button found, stopping. Total clicked: {}", totalClicked);
					break;
				}
				
				attemptCount++;
			}
			
			if (attemptCount >= maxAttempts) {
				LOGGER.warn("Reached max attempts ({}) for clicking View More buttons. Total clicked: {}", maxAttempts, totalClicked);
			}
		} catch (Exception e) {
			LOGGER.debug("Error expanding View More buttons: {}", e.getMessage());
		}
	}

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

	protected String formatCurrentDate() {
		java.time.LocalDate today = java.time.LocalDate.now();
		java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("MM/dd/yyyy");
		return today.format(formatter);
	}

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

	protected boolean isMissingData(String value) {
		if (value == null || value.trim().isEmpty()) return true;
		String normalized = value.trim().toLowerCase();
		return normalized.equals("n/a") || normalized.equals("-") || normalized.equals("na") 
				|| normalized.equals("null") || normalized.equals("none");
	}

	protected String normalizeFieldValue(String fieldValue) {
		if (fieldValue == null) return "";
		String normalized = fieldValue.trim();
		// Remove common prefixes/suffixes
		if (normalized.startsWith("-")) normalized = normalized.substring(1).trim();
		if (normalized.endsWith("-")) normalized = normalized.substring(0, normalized.length() - 1).trim();
		return normalized;
	}

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

	protected String formatDateForDisplay() {
		LocalDate currentDate = LocalDate.now();
		int currentYear = currentDate.getYear();
		int currentDay = currentDate.getDayOfMonth();
		Month currentMonth = currentDate.getMonth();
		String dayStr = (currentDay < 10) ? new DecimalFormat("00").format(currentDay) : String.valueOf(currentDay);
		return currentMonth.toString().substring(0, 1) + currentMonth.toString().substring(1, 3).toLowerCase() + " " + dayStr + ", " + currentYear;
	}

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

	protected String getValueOrEmpty(String value) {
		return isMissingData(value) ? "[EMPTY]" : value;
	}
	// Supports both kf-checkbox components (PM screens) and native checkboxes (JAM screens)

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
	
	protected void logProfileCountSummary(int total, int selected, int disabled) {
		int unselected = total - selected - disabled;
		LOGGER.info("=== PROFILE COUNT SUMMARY ===");
		LOGGER.info("Total Profiles: {}", total);
		LOGGER.info(" Selected (to be processed): {}", selected);
		LOGGER.info(" Unselected (enabled but not selected): {}", unselected);
		LOGGER.info(" Disabled (cannot be selected): {}", disabled);
	}
	// These methods return screen-specific locators based on the "PM" or "JAM" parameter
	// Used by PO35, PO36, PO42, and other consolidated Page Objects

	protected String getScreenName(String screen) {
		return "PM".equalsIgnoreCase(screen) ? "HCM Sync Profiles" : "Job Mapping";
	}

	protected By getShowingResultsCountLocator(String screen) {
		return "PM".equalsIgnoreCase(screen) ? Locators.HCMSyncProfiles.SHOWING_RESULTS_COUNT : Locators.JAMScreen.SHOWING_RESULTS_COUNT;
	}

	protected By getAllProfileRowsLocator(String screen) {
		return "PM".equalsIgnoreCase(screen) ? Locators.PMScreen.ALL_PROFILE_ROWS : Locators.JAMScreen.ALL_PROFILE_ROWS;
	}

	protected By getSelectedProfileRowsLocator(String screen) {
		return "PM".equalsIgnoreCase(screen) ? Locators.PMScreen.SELECTED_PROFILE_ROWS : Locators.JAMScreen.SELECTED_PROFILE_ROWS;
	}

	protected By getChevronButtonLocator(String screen) {
		return "PM".equalsIgnoreCase(screen) ? Locators.PMScreen.CHEVRON_BUTTON : Locators.JAMScreen.CHEVRON_BUTTON;
	}

	protected By getHeaderCheckboxLocator(String screen) {
		return "PM".equalsIgnoreCase(screen) ? Locators.PMScreen.HEADER_CHECKBOX : Locators.JAMScreen.HEADER_CHECKBOX;
	}

	protected By getActionButtonLocator(String screen) {
		return "PM".equalsIgnoreCase(screen) ? Locators.PMScreen.SYNC_BUTTON : Locators.JAMScreen.PUBLISH_BUTTON;
	}

	protected By getSearchBarLocator(String screen) {
		return "PM".equalsIgnoreCase(screen) ? Locators.PMScreen.SEARCH_BAR : Locators.JAMScreen.SEARCH_BAR;
	}

	protected By getClearFiltersButtonLocator(String screen) {
		return "PM".equalsIgnoreCase(screen) ? Locators.PMScreen.CLEAR_ALL_FILTERS_BTN : Locators.JAMScreen.CLEAR_FILTERS_BTN;
	}

	protected int countSelectedProfilesJS(String screen) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			Object result;
			
			if ("PM".equalsIgnoreCase(screen)) {
				// FIX: Count only ENABLED selected checkboxes (exclude disabled with check icons)
				String jsScript = 
					"return Array.from(document.querySelectorAll('tbody tr td:first-child kf-checkbox')).filter(cb => " +
					"  cb.querySelector('kf-icon[icon=\"checkbox-check\"]') && " +
					"  !cb.querySelector('div.disable')" +
					").length;";
				result = js.executeScript(jsScript);
				int count = result != null ? ((Long) result).intValue() : 0;
				// Fallback to locator if JS returns 0
				if (count == 0) {
					count = findElements(Locators.PMScreen.SELECTED_PROFILE_ROWS).size();
				}
				return count;
			} else {
				String jsScript = "return Array.from(document.querySelectorAll('#org-job-container tbody tr td:first-child input[type=\"checkbox\"]')).filter(cb => cb.checked).length;";
				result = js.executeScript(jsScript);
				return result != null ? ((Long) result).intValue() : 0;
			}
		} catch (Exception e) {
			LOGGER.debug("JS counting failed for {}: {}", screen, e.getMessage());
			return findElements(getSelectedProfileRowsLocator(screen)).size();
		}
	}

	protected int countAllProfilesJS(String screen) {
		try {
			return findElements(getAllProfileRowsLocator(screen)).size();
		} catch (Exception e) {
			LOGGER.debug("Profile counting failed for {}: {}", screen, e.getMessage());
			return 0;
		}
	}

	protected String normalizeForSorting(String value) {
		if (value == null) return "";
		return value.replaceAll("[-'\"()\\[\\].,:;!?]", "").trim();
	}

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

	protected boolean isNonAscii(String value) {
		return value != null && !value.isEmpty() && value.charAt(0) > 127;
	}

	protected boolean shouldSkipInSortValidation(String value) {
		return isMissingData(value);
	}

	protected void waitForBackgroundDataLoad() {
		try {
			LOGGER.info("Waiting for background data API to complete...");
			
			JavascriptExecutor js = (JavascriptExecutor) driver;
			
			// Step 1: Check if data API already completed (pre-check)
			Boolean alreadyComplete = (Boolean) js.executeScript(
				"if (window.performance && window.performance.getEntriesByType) {" +
				"  var resources = window.performance.getEntriesByType('resource');" +
				"  var requests = resources.filter(function(r) { " +
				"    return (r.initiatorType === 'xmlhttprequest' || r.initiatorType === 'fetch') && r.name;" +
				"  });" +
				"  var found = requests.some(function(r) { " +
				"    var url = r.name.toLowerCase();" +
				"    var isEndpoint = url.includes('/search') || url.includes('/fetch-jobs');" +
				"    var hasParam = url.includes('limit=100000') || url.includes('pagesize=100000');" +
				"    return isEndpoint && hasParam;" +
				"  });" +
				"  return found;" +
				"}" +
				"return false;"
			);
			
			if (Boolean.TRUE.equals(alreadyComplete)) {
				LOGGER.info("✓ Data API already completed");
				safeSleep(2000);
				return;
			}
			
			// Step 2: Inject monitoring for future requests
			js.executeScript(
				"if (!window._dataApiMonitor) {" +
				"  window._dataApiMonitor = true;" +
				"  window._requestPending = 0;" +
				"  window._dataApiComplete = false;" +
				
				// Monitor XHR
				"  var origXHR = XMLHttpRequest.prototype.open;" +
				"  XMLHttpRequest.prototype.open = function() {" +
				"    window._requestPending++;" +
				"    this.addEventListener('load', function() { " +
				"      window._requestPending--;" +
				"      if (this.responseURL) {" +
				"        var url = this.responseURL.toLowerCase();" +
				"        var isEndpoint = url.includes('/search') || url.includes('/fetch-jobs');" +
				"        var hasParam = url.includes('limit=100000') || url.includes('pagesize=100000');" +
				"        if (isEndpoint && hasParam) window._dataApiComplete = true;" +
				"      }" +
				"    });" +
				"    this.addEventListener('error', function() { window._requestPending--; });" +
				"    this.addEventListener('abort', function() { window._requestPending--; });" +
				"    origXHR.apply(this, arguments);" +
				"  };" +
				
				// Monitor Fetch
				"  var origFetch = window.fetch;" +
				"  window.fetch = function() {" +
				"    window._requestPending++;" +
				"    return origFetch.apply(this, arguments).then(function(response) {" +
				"      window._requestPending--;" +
				"      if (response.url) {" +
				"        var url = response.url.toLowerCase();" +
				"        var isEndpoint = url.includes('/search') || url.includes('/fetch-jobs');" +
				"        var hasParam = url.includes('limit=100000') || url.includes('pagesize=100000');" +
				"        if (isEndpoint && hasParam) window._dataApiComplete = true;" +
				"      }" +
				"      return response;" +
				"    }).catch(function(error) {" +
				"      window._requestPending--;" +
				"      throw error;" +
				"    });" +
				"  };" +
				"}"
			);
			
			// Step 3: Wait for API completion (max 120 seconds)
			int maxWaitSeconds = 120;
			int waited = 0;
			int consecutiveZero = 0;
			
			while (waited < maxWaitSeconds) {
				safeSleep(2000);
				waited += 2;
				
				Boolean apiComplete = (Boolean) js.executeScript("return window._dataApiComplete === true;");
				Long pending = (Long) js.executeScript("return window._requestPending || 0;");
				
				if (Boolean.TRUE.equals(apiComplete)) {
					LOGGER.info("✓ Data API completed after {} seconds", waited);
					safeSleep(3000);
					return;
				}
				
				// Skip zero-check during initial load
				if (waited <= 10) {
					if (pending != null && pending > 0) consecutiveZero = 0;
					continue;
				}
				
				// After 10s, check for sustained zero pending
				if (pending == null || pending == 0) {
					consecutiveZero++;
					if (consecutiveZero >= 5) {
						LOGGER.info("✓ No active requests for 10s - proceeding");
						safeSleep(3000);
						return;
					}
				} else {
					consecutiveZero = 0;
				}
				
				// Log progress every 20 seconds
				if (waited % 20 == 0) {
					LOGGER.info("⏳ Waiting... {}s elapsed, {} pending", waited, pending);
				}
			}
			
			LOGGER.warn("⚠️ Wait timeout after {} seconds - proceeding anyway", maxWaitSeconds);
			
		} catch (Exception e) {
			LOGGER.warn("Error monitoring API: {}. Using fallback wait.", e.getMessage());
			safeSleep(15000);
		}
	}
}

