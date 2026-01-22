package com.kfonetalentsuite.pageobjects.JobMapping;


import java.time.Duration;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.kfonetalentsuite.utils.common.Utilities;
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
 *   clearAndSearch(By, String)         - Clear search bar and search (robust)
 *   clearSearchBar(By)                 - Clear search bar only
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
 * - Use waitForPageReady() after critical page interactions
 * 
 * ================================
 */
public class BasePageObject {

	// ==================== CORE COMPONENTS ====================
	protected WebDriver driver;
	protected WebDriverWait wait;
	protected JavascriptExecutor js;
	protected static final Logger LOGGER = LogManager.getLogger(BasePageObject.class);

	// ==================== CONSTRUCTOR ====================
	public BasePageObject() {
		this.driver = DriverManager.getDriver();
		this.wait = DriverManager.getWait();
		this.js = (JavascriptExecutor) driver;
		PageFactory.initElements(driver, this);
	}

	// ==================== CENTRALIZED LOCATORS ====================
	// ALL LOCATORS CONSOLIDATED HERE FOR CONSISTENT MAINTENANCE
	public static class Locators {
		
		// ==================== SHARED/COMMON LOCATORS (Universal - Used across all pages) ====================
		public static class Common {
			
			// SPINNERS & LOADERS (Used in 20+ page objects)
			public static final By PAGE_LOAD_SPINNER = By.xpath("//*[@class='blocking-loader']//img");
			public static final By DATA_LOADER = By.xpath("//div[@data-testid='loader']//img");
			public static final By KF_LOADER = By.xpath("//div[@id='kf-loader']//*");
			
			// GLOBAL NAVIGATION (Header components)
			public static final By KF_TALENT_SUITE_LOGO = By.xpath("//div[contains(@class,'global-nav-title-icon-container')]");
			public static final By GLOBAL_NAV_MENU_BTN = By.xpath("//button[@id='global-nav-menu-btn']");
			public static final By WAFFLE_MENU = By.xpath("//div[@data-testid='menu-icon']");
			public static final By CLIENT_NAME = By.xpath("//button[contains(@class,'global-nav-client-name')]//span");
			public static final By JOB_MAPPING_BTN = By.xpath("//button[@aria-label='Job Mapping']");
			public static final By KFONE_MENU_PM_BTN = By.xpath("//span[@aria-label='Profile Manager']");
			public static final By KFONE_MENU_ARCHITECT_BTN = By.xpath("//span[@aria-label='Architect']");
			
			// USER PROFILE (Profile dropdown)
			public static final By PROFILE_AVATAR = By.xpath("//*[@data-testid='global-nav-user-avatar-avatar-0']");
			public static final By PROFILE_BTN = By.xpath("//button[@id='global-nav-user-dropdown-btn']");
			public static final By PROFILE_USER_NAME = By.xpath("//div[@class='nav-profile-user-name']");
			public static final By PROFILE_EMAIL = By.xpath("//div[@class='nav-profile-email']");
			public static final By SIGN_OUT_BTN = By.xpath("//span[text()='Sign Out']");
			public static final By MY_PROFILE_BTN = By.xpath("//button[text()='My Profile']");
			
			// SEARCH & FILTERS (Universal across all screens)
			public static final By SEARCH_BAR = By.xpath("//input[contains(@id,'search-job-title')] | //input[@type='search']");
			public static final By FILTERS_BTN = By.xpath("//button[@id='filters-btn']");
			public static final By CLEAR_FILTERS_BTN = By.xpath("//button[text()='Clear'] | //button[@data-testid='Clear Filters'] | //a[contains(text(),'Clear All')]");
			public static final By APPLY_FILTERS_BTN = By.xpath("//button[text()='Apply']");
			public static final By FILTER_OPTIONS_PANEL = By.xpath("//div[@id='filters-search-btns']//div[2]//div");
			public static final By CLOSE_APPLIED_FILTER = By.xpath("//div[contains(@class,'applied-filters')]//span//kf-icon");
			
			// TABLE & GRID (Universal - Works for both PM and JAM)
			public static final By HEADER_CHECKBOX = By.xpath("//thead//input[@type='checkbox'] | //thead//tr//th[1]//div[1]//kf-checkbox//div");
			public static final By ROW_CHECKBOXES = By.xpath("//tbody//tr//td[1]//input[@type='checkbox'] | //tbody//tr//td[1]//kf-checkbox");
			public static final By ALL_ROW_CHECKBOXES = By.xpath("//tbody//tr//td[1][contains(@class,'whitespace')]//input | //tbody//tr//td[1]//kf-checkbox");
			public static final By HEADER_CHEVRON_BTN = By.xpath("//thead//th//kf-icon[contains(@class,'arrow-down') or @icon='arrow-down'] | //th[@scope='col']//div[@class='relative inline-block']//div//*[contains(@class,'cursor-pointer')]");
			public static final By SELECT_ALL_BTN = By.xpath("//*[contains(text(),'Select All')]");
			public static final By SELECT_NONE_BTN = By.xpath("//*[contains(text(),'None')]");
			public static final By RESULTS_COUNT_TEXT = By.xpath("//div[@id='results-toggle-container']//p//span | //div[contains(text(),'Showing')] | //*[contains(text(),'Showing')]");
			public static final By ALL_PROFILE_ROWS = By.xpath("//tbody//tr[.//kf-checkbox] | //div[@id='org-job-container']//tbody//tr[.//td[1]//input[@type='checkbox']]");
			public static final By SELECTED_PROFILE_ROWS = By.xpath("//tbody//tr[.//kf-icon[@icon='checkbox-check' and contains(@class,'ng-star-inserted')]] | //div[@id='org-job-container']//tbody//tr[.//td[1]//input[@type='checkbox' and @checked]]");
			
			// ACTIONS & BUTTONS (Common action buttons)
			public static final By PC_VIEW_PUBLISHED_TOGGLE = By.xpath("//input[@id='toggleSwitch']");
			public static final By PC_PUBLISH_BTN = By.xpath("//button[@id='publish-approved-mappings-btn'] | //button[contains(@id,'publish-approved-mappings-btn')] | //button[contains(text(),'Publish Selected')]");
			public static final By ACCEPT_COOKIES_BTN = By.xpath("//button[@id='ensAcceptAll']");
			public static final By SYNC_BUTTON = By.xpath("//button[contains(@class,'custom-export')] | //button[contains(text(),'Sync with HCM')]");
			public static final By ACTION_BUTTON = By.xpath("//button[contains(@class,'custom-export')] | //button[contains(text(),'Sync with HCM')] | //button[contains(@id,'publish-approved-mappings-btn')] | //button[contains(text(),'Publish Selected')]");
			
			// MODALS & POPUPS (Common modal elements)
			public static final By SUCCESS_MODAL_HEADER = By.xpath("//h2[@id='modal-header']");
			public static final By SUCCESS_MODAL_MESSAGE = By.xpath("//*[@id='modal-description']");
			public static final By SUCCESS_MODAL_CLOSE_BTN = By.xpath("//button[@id='close-success-modal-btn']");
			public static final By PROFILE_DETAILS_POPUP_HEADER = By.xpath("//h2[@id='summary-modal']");
			public static final By PROFILE_DETAILS_CLOSE_BTN = By.xpath("//button[@id='close-profile-summary']");
			public static final By PUBLISH_SUCCESS_MSG = By.xpath("//p[contains(text(),'Success profile published')]/..");
			public static final By PUBLISH_SUCCESS_MSG_DIRECT = By.xpath("//p[contains(text(),'Success profile published')]");
			public static final By PUBLISH_SUCCESS_MSG_FLEXIBLE = By.xpath("//*[contains(text(),'profile published') or contains(text(),'successfully published')]");
			public static final By PUBLISHED_SUCCESS_MSG = By.xpath("//p[@id='modal-message']");
			public static final By PUBLISHED_SUCCESS_CLOSE_BTN = By.xpath("//button[@aria-label='Close']");
		}
		
		// ==================== LOGIN SCREEN ====================
		public static class LoginScreen {
			// KFONE Portal Elements
			public static final By LANDING_PAGE_TITLE = By.xpath("//*[@id='title-svg-icon']");
			public static final By CLIENTS_PAGE_HEADER = By.xpath("//*[@data-testid='clients']//h2");
			public static final By CLIENTS_PAGE_TITLE = By.xpath("//h2[text()='Clients']");
			public static final By LOGIN_PAGE_TEXT = By.xpath("//*[text()='Sign in to your account']");
			
			// Authentication
			public static final By USERNAME_INPUT = By.xpath("//input[@type='email']");
			public static final By PASSWORD_INPUT = By.xpath("//input[@type='password']");
			public static final By KFONE_SIGNIN_BTN = By.xpath("//button[@id='submit-button']");
			public static final By PROCEED_BTN = By.xpath("//*[text()='Proceed']");
			public static final By MICROSOFT_SUBMIT_BTN = By.xpath("//input[@type='submit']");
			public static final By MICROSOFT_PASSWORD_HEADER = By.xpath("//div[text()='Enter password']");
			
			// KFONE Home
			public static final By KFONE_HOME_HEADER = By.xpath("//h1[contains(text(),'Hi,')]");
			public static final By YOUR_PRODUCTS_SECTION = By.xpath("//h2[contains(text(),'Your products')]");
			public static final By PM_IN_PRODUCTS_SECTION = By.xpath("//div[@data-testid='Profile Manager']");
			
			// Client Selection
			public static final By CLIENTS_TABLE = By.xpath("//table[@id='iam-clients-list-table-content']");
			public static final By CLIENTS_TABLE_BODY = By.xpath("//tbody[@class='table-body']");
			public static final By CLIENT_SEARCH_BAR = By.xpath("//input[@id='search-client-input-search']");
			public static final By CLIENT_ROWS = By.xpath("//tbody//tr[@class='table-row']");
			public static final By CLIENT_NAME_LINK = By.xpath(".//a[contains(@data-testid,'client-')]");
			public static final By CLIENT_NAME_DIV = By.xpath(".//div[contains(@class,'data-content-container')]//div[contains(@title, '')]");
			public static final By CLIENT_PAMS_ID_CELL = By.xpath(".//td[contains(@data-testid,'iam-clients-list-pams_id-table-data-cell-')]");
			public static final By CLIENT_PRODUCTS_CELL = By.xpath(".//td[contains(@data-testid,'iam-clients-list-clientProducts-table-data-cell-')]");
		}
		
		// ==================== FEATURE AREAS ====================
		
		// COMPARISON (Used in PO04,PO07,PO15,PO27)
		public static class Comparison {
			public static final By COMPARE_HEADER = By.xpath("//h1[@id='compare-desc']");
			public static final By CARD_HEADER = By.xpath("//div[@class='shadow']//div[contains(@id,'card-header')][1]//span");
			public static final By CARD_TITLE = By.xpath("//div[@class='shadow']//div[contains(@id,'card-title')]");
			public static final By SELECT_BTNS_IN_JC = By.xpath("//button[contains(@id,'select-btn') or contains(text(),'Select')]");
		}
		
		// PROFILE DETAILS POPUP (Used in PO05,PO14,PO21)
		public static class ProfileDetails {
			public static final By PROFILE_LEVEL_DROPDOWN = By.xpath("//select[contains(@id,'profileLevel') or @id='profile-level']");
			public static final By PUBLISH_PROFILE_BTN = By.xpath("//button[@id='publish-job-profile']");
			public static final By POPUP_PUBLISH_PROFILE_BTN = By.xpath("//button[@id='publish-job-profile']");
			public static final By PROFILE_HEADER_TEXT = By.xpath("//h2[@id='summary-modal']//p");
			public static final By ROLE_SUMMARY = By.xpath("//div[@id='role-summary-container']//p");
			public static final By DETAILS_CONTAINER = By.xpath("//div[@id='details-container']//div[contains(@class,'mt-2')]");
			public static final By RESPONSIBILITIES_DATA = By.xpath("//div[@id='responsibilities-panel']//div[@id='item-container']");
			public static final By BEHAVIOUR_DATA = By.xpath("//div[@id='behavioural-panel']//div[@id='item-container']");
			public static final By SKILLS_DATA = By.xpath("//div[@id='skills-panel']//div[@id='item-container']");
			public static final By SKILLS_TAB = By.xpath("//button[text()='SKILLS']");
			public static final By BEHAVIOUR_TAB = By.xpath("//button[contains(text(),'BEHAVIOUR')]");
			public static final By VIEW_MORE_BEHAVIOUR = By.xpath("//div[@id='behavioural-panel']//button[contains(text(),'View')]");
		}
		
		// HCM SYNC PROFILES (Used in PO18,PO19,PO21,PO22,PO23,PO25,PO31)
		public static class HCMSyncProfiles {
			public static final By HCM_SYNC_TAB = By.xpath("//span[contains(text(),'HCM Sync')]");
			public static final By HCM_SYNC_HEADER = By.xpath("//h1[contains(text(),'HCM Sync Profiles')]");
			public static final By SYNC_PROFILES_TITLE = By.xpath("//h1[contains(text(),'Sync Profiles')]");
			public static final By PROFILE_MANAGER_HEADER = By.xpath("//h1[contains(text(),'Profile Manager')]");
			public static final By PROFILES_SEARCH = By.xpath("//input[@type='search' or contains(@placeholder,'Search job profiles')]");
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
		
		// PUBLISH CENTER (Used in PO20)
		public static class PublishCenter {
			public static final By PUBLISH_CENTER_BTN = By.xpath("//button[contains(@class,'publish-center')]");
			public static final By JPH_SCREEN_TITLE = By.xpath("//*[contains(text(),'Job Profile History')]");
			public static final By JPH_PROFILES_COUNT_ROW1 = By.xpath("//*/kf-page-content/div[2]/div[2]/div[1]/div[1]/span");
			public static final By JPH_ACCESSED_BY_ROW1 = By.xpath("//*/kf-page-content/div[2]/div[2]/div[1]/div[2]/span");
			public static final By JPH_ACCESSED_DATE_ROW1 = By.xpath("//*/kf-page-content/div[2]/div[2]/div[1]/div[3]/span");
			public static final By JPH_ACTION_TAKEN_ROW1 = By.xpath("//*/kf-page-content/div[2]/div[2]/div[1]/div[4]/span");
			public static final By JPH_STATUS_ROW1 = By.xpath("//*/kf-page-content/div[2]/div[2]/div[1]/div[5]/span");
			public static final By JPH_HEADER1 = By.xpath("//*/div/kf-page-content/div[2]/div[1]/div[1]");
			public static final By JPH_HEADER2 = By.xpath("//*/div/kf-page-content/div[2]/div[1]/div[2]");
			public static final By JPH_HEADER3 = By.xpath("//*/div/kf-page-content/div[2]/div[1]/div[3]");
			public static final By JPH_HEADER4 = By.xpath("//*/div/kf-page-content/div[2]/div[1]/div[4]");
			public static final By JPH_HEADER5 = By.xpath("//*/div/kf-page-content/div[2]/div[1]/div[5]");
			public static final By PROFILES_DOWNLOADED_TITLE = By.xpath("//*[contains(text(),'Profiles Downloaded')]");
			public static final By PROFILES_DOWNLOADED_HEADER = By.xpath("//*[contains(@class,'header-details')]");
			public static final By CLOSE_BTN = By.xpath("//*[contains(text(),'Close')]//..");
			public static final By PD_HEADER1 = By.xpath("//*/div[2]/div/div[2]/div[1]/div[1]");
			public static final By PD_HEADER2 = By.xpath("//*/div[2]/div/div[2]/div[1]/div[2]");
			public static final By PD_HEADER3 = By.xpath("//*/div[2]/div/div[2]/div[1]/div[3]");
			public static final By PD_HEADER4 = By.xpath("//*/div[2]/div/div[2]/div[1]/div[4]");
			public static final By PROFILES_EXPORTED_TITLE = By.xpath("//*[contains(text(),'Profiles Exported')]");
		}
		
		// MODALS (Used in PO30, PO36, and other page objects)
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
		}
		
		// ==================== APPLICATION SCREENS ====================
		// Main application context screens (JAM, PM, KFOne, etc.)
		
			// JOB MAPPING SCREEN (JAM Context - Used in most Page Objects)
		public static class JobMappingScreen {
			// Page Container & Headers
			public static final By PAGE_CONTAINER = By.xpath("//div[@id='org-job-container']");
			public static final By MAIN_HEADER = By.xpath("//h2[contains(text(),'JOB MAPPING')]");
			public static final By PAGE_TITLE_HEADER = By.xpath("//div[@id='page-heading']//h1");
			public static final By ASYNC_MESSAGE = By.xpath("//*[contains(text(),'Publish process') or contains(text(),'Please check')]");
		
			// Table Headers (PO14 - Sorting)
			public static final By ORG_JOB_NAME_HEADER = By.xpath("//*[@id='org-job-container']/div/table/thead/tr/th[2]/div");
			public static final By ORG_JOB_GRADE_HEADER = By.xpath("//*[@id='org-job-container']/div/table/thead/tr/th[3]/div");
			public static final By MATCHED_SP_NAME_HEADER = By.xpath("//*[@id='kf-job-container']/div/table/thead/tr/th[1]/div");
			public static final By MATCHED_SP_GRADE_HEADER = By.xpath("//*[@id='kf-job-container']/div/table/thead/tr/th[2]");
		
			// Results (from JobMappingResults class)
			public static final By SHOWING_JOB_RESULTS = By.xpath("//div[contains(@id,'results-toggle')]//*[contains(text(),'Showing')]");
			public static final By FIRST_ROW_PROFILE_MATCH = By.xpath("//tbody//tr[1]//td[2]//div[contains(text(),'(')]");
			public static final By FIRST_ROW_JOB_TITLE = By.xpath("//div[@id='kf-job-container']//div//table//tbody//tr[1]//td[1]//div");
			public static final By SEARCH_INPUT = By.xpath("//input[@type='search']");
			public static final By JOB_NAME_ROW_1 = By.xpath("//tbody//tr[1]//td[2]//div[contains(text(),'(')]");
			public static final By JOB_1_PUBLISH_BTN = By.xpath("//tbody//tr[2]//button[@id='publish-btn'][1]");
			public static final By JOB_1_PUBLISHED_BTN = By.xpath("//tbody//tr[2]//button[text()='Published'][1]");
			public static final By HCM_JOB_ROW_1 = By.xpath("//tbody//tr[1]//td//div//span[1]//a");
			public static final By HCM_JOBCODE_ROW_1 = By.xpath("//tbody//tr[1]//td[3]//span");
			public static final By HCM_DATE_ROW_1 = By.xpath("//tbody//tr[1]//td[8]//span");
			public static final By ARCHITECT_JOB_ROW_1 = By.xpath("//tbody//tr[1]//td//div//div//a");
			public static final By ARCHITECT_DATE_ROW_1 = By.xpath("//tbody//tr[1]//td[9]");
		
			// Page Components (PO04-specific)
			public static final By PAGE_TITLE_DESC = By.xpath("//div[@id='page-title']//p[1]");
			public static final By JOB_GRADE_PROFILE_1 = By.xpath("//tbody//tr[1]//td[3]//div[1]");
			public static final By JOB_FUNCTION_PROFILE_1 = By.xpath("//tbody//tr[2]//div//span[2]");
			public static final By JOB_DEPARTMENT_PROFILE_1 = By.xpath("//tbody//tr[1]//td[4]//div");
			public static final By JOB_NAME_PROFILE_2 = By.xpath("//tbody//tr[4]//td[2]//div[contains(text(),'(')]");
			public static final By FILTER_OPTION_1 = By.xpath("//div[@id='filters-search-btns']//div[2]//div//div/h3");
			public static final By FILTER_OPTION_2 = By.xpath("//div[@id='filters-search-btns']//div[2]//div[2]//div/h3");
			public static final By FILTER_OPTION_3 = By.xpath("//div[@id='filters-search-btns']//div[2]//div[3]//div/h3");
			public static final By FILTER_OPTION_4 = By.xpath("//div[@id='filters-search-btns']//div[2]//div[4]//div/h3");
			public static final By SEARCH_BAR_FILTER_OPTION_3 = By.xpath("//div[@id='filters-search-btns']//div[2]//div[3]//input[contains(@placeholder,'Search')]");
			public static final By ADD_MORE_JOBS_BTN = By.xpath("//span[contains(text(),'Add more jobs')] | //button[@id='add-more-jobs-btn']");
			public static final By PROFILE_1_CHECKBOX = By.xpath("//tbody//tr[1]//td[1][contains(@class,'whitespace')]//input");
			public static final By PROFILE_2_CHECKBOX = By.xpath("//tbody//tr[4]//td[1][contains(@class,'whitespace')]//input");
			public static final By NO_DATA_AVAILABLE = By.xpath("//td[@id='no-data-container']");
			public static final By TABLE_1_TITLE = By.xpath("//*[contains(text(),'Organization jobs')]");
			public static final By TABLE_1_HEADER_1 = By.xpath("//*[@id='org-job-container']/div/table/thead/tr/th[2]/div | //*[@id='table-container']/div[1]/div/div[1]/div/span[1]");
			public static final By TABLE_1_HEADER_2 = By.xpath("//*[@id='org-job-container']/div/table/thead/tr/th[3]/div | //*[@id='table-container']/div[1]/div/div[1]/div/span[2]");
			public static final By TABLE_1_HEADER_3 = By.xpath("//*[@id='org-job-container']/div/table/thead/tr/th[4]/div | //*[@id='table-container']/div[1]/div/div[1]/div/span[3]");
			public static final By TABLE_2_TITLE = By.xpath("//*[contains(text(),'Matched success profiles')]");
			public static final By TABLE_2_HEADER_1 = By.xpath("//*[@id='kf-job-container']/div/table/thead/tr/th[1]/div");
			public static final By TABLE_2_HEADER_2 = By.xpath("//*[@id='kf-job-container']/div/table/thead/tr/th[2]/div");
			public static final By TABLE_2_HEADER_3 = By.xpath("//*[@id='kf-job-container']/div/table/thead/tr/th[3]/div");
			public static final By TABLE_2_HEADER_4 = By.xpath("//*[@id='kf-job-container']/div/table/thead/tr/th[4]/div");
			public static final By TABLE_2_HEADER_5 = By.xpath("//*[@id='kf-job-container']/div/table/thead/tr/th[5]/div");
			public static final By JOB_1_VIEW_OTHER_MATCHES = By.xpath("//tbody//tr[2]//button[@id='view-matches']");
			public static final By JAM_LOGO = By.xpath("//div[@id='header-logo']");
			public static final By BROWSE_RESOURCES_JAM = By.xpath("//*[contains(text(),'Browse resources')]/following::*[contains(text(),'Job Mapping')]");
		
			// PO05 - Publish Job Profile
			public static final By JOBS_LINK = By.xpath("//span[text()='Jobs']");
			public static final By JC_ORG_JOB_TITLE = By.xpath("//div[contains(@class, 'text-[24px] font-semibold')]");
			public static final By JC_PUBLISH_SELECT_BTN = By.xpath("//button[@id='publish-select-btn']");
		
			// PO06 - Publish Selected Profiles
			public static final By HCM_DATE_ROW_2 = By.xpath("//tbody//tr[2]//td[8]//span");
		
			// PO07 - Screen1 Search Results
			public static final By JOB_NAMES_IN_RESULTS = By.xpath("//tbody//tr//td[2]//div[contains(text(),'(')]");
		
			// PO08 - Job Mapping Filters
			public static final By CLEAR_FILTERS_X_BTN = By.xpath("//button[@data-testid='clear-filters-button']");
			public static final By GRADES_DROPDOWN = By.xpath("//div[@data-testid='dropdown-Grades']");
			public static final By GRADES_CHECKBOXES = By.xpath("//div[@data-testid='dropdown-Grades']//..//input[@type='checkbox']");
			public static final By GRADES_VALUES = By.xpath("//div[@data-testid='dropdown-Grades']//..//input[@type='checkbox']//..//label");
			public static final By LEVELS_DROPDOWN = By.xpath("//div[@data-testid='dropdown-Levels']");
			public static final By LEVELS_CHECKBOXES = By.xpath("//div[@data-testid='dropdown-Levels']//..//input[@type='checkbox']");
			public static final By LEVELS_VALUES = By.xpath("//div[@data-testid='dropdown-Levels']//..//input[@type='checkbox']//..//label");
			public static final By FUNCTIONS_DROPDOWN = By.xpath("//div[@data-testid='dropdown-Functions_SubFunctions']");
			public static final By FUNCTIONS_CHECKBOXES = By.xpath("//div[@data-testid='dropdown-Functions_SubFunctions']//..//input[@type='checkbox']");
			public static final By FUNCTIONS_VALUES = By.xpath("//div[@data-testid='dropdown-Functions_SubFunctions']//..//input[@type='checkbox']//..//label");
			public static final By SUBFUNCTIONS_DROPDOWN = By.xpath("//div[@data-testid='dropdown-Functions_SubFunctions']//..//button");
			public static final By SUBFUNCTIONS_CHECKBOXES = By.xpath("//div[@data-testid='dropdown-Functions_SubFunctions']//..//input[@type='checkbox']");
			public static final By SUBFUNCTIONS_VALUES = By.xpath("//div[@data-testid='dropdown-Functions_SubFunctions']//..//input[@type='checkbox']//..//label");
			public static final By NO_DATA_CONTAINER = By.xpath("//*[@id='no-data-container']");
			public static final By FILTER_TYPE_DROPDOWN_SEARCH_INPUT = By.xpath("//div[@data-testid='dropdown-filter-type']//input[@placeholder='Search']");
			public static final By FILTER_SECTIONS_TITLES = By.xpath("//div[@id='filters-search-btns']//h3");
			public static final By FILTER_SECTIONS_DIVS = By.xpath("//div[@id='filters-search-btns']//div[contains(@class,'dropdown-component')]");
			public static final By GRADE_FILTER_CHECKBOXES = By.xpath("//div[@data-testid='dropdown-Grades']//..//input[@type='checkbox']");
			public static final By LEVEL_FILTER_CHECKBOXES = By.xpath("//div[@data-testid='dropdown-Levels']//..//input[@type='checkbox']");
			public static final By FUNCTION_FILTER_CHECKBOXES = By.xpath("//div[@data-testid='dropdown-Functions_SubFunctions']//..//input[@type='checkbox']");
			public static final By SUBFUNCTION_FILTER_CHECKBOXES = By.xpath("//div[@data-testid='dropdown-Functions_SubFunctions']//..//input[@type='checkbox']");
			public static final By FILTER_CHECKBOXES = By.xpath("//div[@id='filters-search-btns']//input[@type='checkbox']");
			// Additional PO08 locators
			public static final By DEPARTMENTS_DROPDOWN = By.xpath("//div[@data-testid='dropdown-Departments']");
			public static final By DEPARTMENTS_CHECKBOXES = By.xpath("//div[@data-testid='dropdown-Departments']//..//input[@type='checkbox']");
			public static final By DEPARTMENTS_VALUES = By.xpath("//div[@data-testid='dropdown-Departments']//..//input[@type='checkbox']//..//label");
			public static final By FUNCTIONS_SEARCH = By.xpath("//div[@data-testid='dropdown-Functions_SubFunctions']//..//div[2]//input[@placeholder='Search']");
			public static final By TOGGLE_SUBOPTIONS = By.xpath("//button[contains(@data-testid,'toggle-suboptions')]");
			public static final By MAPPING_STATUS_DROPDOWN = By.xpath("//*[@data-testid='dropdown-MappingStatus']");
			public static final By MAPPING_STATUS_CHECKBOXES = By.xpath("//div[@data-testid='dropdown-MappingStatus']//..//input[@type='checkbox']");
			public static final By MAPPING_STATUS_VALUES = By.xpath("//div[@data-testid='dropdown-MappingStatus']//..//input[@type='checkbox']//..//label");
			public static final By ALL_GRADES_COLUMN = By.xpath("//*[text()='Organization jobs']//..//tbody//tr//td[3]//div | //*[@id='table-container']/div[1]/div/div[2]/div/div/div[1]/span[2]/span");
			public static final By ALL_DEPARTMENTS_COLUMN = By.xpath("//*[text()='Organization jobs']//..//tbody//tr//td[4]//div | //*[@id='table-container']/div[1]/div/div[2]/div/div/div[1]/span[3]");
			public static final By ALL_FUNCTIONS_COLUMN = By.xpath("//*[text()='Organization jobs']//..//tbody//tr//td[@colspan='7']//span[2] | //*[@id='table-container']/div[1]/div/div[2]/div/div/div[2]/span/div/span/span[2]");
			public static final By VISIBLE_ROWS = By.xpath("//tbody//tr[contains(@class,'cursor-pointer')]");
		
			// PO09 - Filter Persistence
			public static final By ORG_JOB_GRADE_SORT_ICON = By.xpath("//*[@id='org-job-container']/div/table/thead/tr/th[3]/div/span/*[contains(@class,'blue')]");
			public static final By MATCHED_SP_GRADE_SORT_ICON = By.xpath("//*[@id='kf-job-container']/div/table/thead/tr/th[2]/div/span/*[contains(@class,'blue')]");
		
			// PO10/PO12 - Shared profile details
			public static final By PROFILE_1_GRADE = By.xpath("(//div[@class='shadow']//div[contains(@id,'grade')])[1]");
			public static final By PROFILE_1_LEVEL = By.xpath("(//div[@class='shadow']//div[contains(@id,'level')])[1]");
			public static final By PROFILE_1_FUNCTION = By.xpath("(//div[@class='shadow']//div[contains(@id,'function')])[1]");
			public static final By PROFILE_1_SENIORITY = By.xpath("(//div[@class='shadow']//div[contains(@id,'seniority')])[1]");
			public static final By PROFILE_2_GRADE = By.xpath("(//div[@class='shadow']//div[contains(@id,'grade')])[2]");
			public static final By PROFILE_2_LEVEL = By.xpath("(//div[@class='shadow']//div[contains(@id,'level')])[2]");
			public static final By PROFILE_2_FUNCTION = By.xpath("(//div[@class='shadow']//div[contains(@id,'function')])[2]");
			public static final By PROFILE_2_SENIORITY = By.xpath("(//div[@class='shadow']//div[contains(@id,'seniority')])[2]");
			public static final By PROFILE_3_GRADE = By.xpath("(//div[@class='shadow']//div[contains(@id,'grade')])[3]");
			public static final By PROFILE_3_LEVEL = By.xpath("(//div[@class='shadow']//div[contains(@id,'level')])[3]");
			public static final By PROFILE_3_FUNCTION = By.xpath("(//div[@class='shadow']//div[contains(@id,'function')])[3]");
			public static final By PROFILE_3_SENIORITY = By.xpath("(//div[@class='shadow']//div[contains(@id,'seniority')])[3]");
			// Additional profile details (PO10, PO12)
			public static final By PROFILE_1_TITLE = By.xpath("(//div[@class='shadow']//div[contains(@id,'card-title')])[1]");
			public static final By PROFILE_1_SELECT_BTN = By.xpath("(//div[@class='shadow']//div[contains(@id,'card-header')][1]//span)[1]");
			public static final By PROFILE_1_RECOMMENDED_TAG = By.xpath("//div[@class='shadow']//div[contains(@id,'recommended-title')]");
			public static final By PROFILE_1_MANAGERIAL = By.xpath("(//div[@class='shadow']//div[contains(@id,'managerial-experience')])[1]");
			public static final By PROFILE_1_EDUCATION = By.xpath("(//div[@class='shadow']//div[contains(@id,'education')])[1]");
			public static final By PROFILE_1_GENERAL_EXP = By.xpath("(//div[@class='shadow']//div[contains(@id,'general-experience')])[1]");
			public static final By PROFILE_1_ROLE_SUMMARY = By.xpath("(//div[@class='shadow']//div[contains(@id,'role-summary')])[1]");
			public static final By PROFILE_1_RESPONSIBILITIES = By.xpath("(//div[@class='shadow']//div[contains(@id,'responsibilities')])[1]");
			public static final By VIEW_MORE_RESPONSIBILITIES = By.xpath("(//div[contains(@id,'responsibilities')]//button[@data-testid='view-more-responsibilities'])[1]");
			public static final By PROFILE_1_COMPETENCIES = By.xpath("(//div[@class='shadow']//div[contains(@id,'behavioural-competencies')])[1]");
			public static final By VIEW_MORE_COMPETENCIES = By.xpath("(//div[contains(@id,'behavioural-competencies')]//button[@data-testid='view-more-competencies'])[1]");
			public static final By PROFILE_1_SKILLS = By.xpath("(//div[@class='shadow']//div[contains(@id,'skills')])[1]");
			public static final By VIEW_MORE_SKILLS = By.xpath("(//div[contains(@id,'skills')]//button[@data-testid='view-more-skills'])[1]");
		
			// PO10 - Custom SP in Job Comparison
			public static final By SEARCH_BAR_JC = By.xpath("//input[contains(@id,'jobcompare-search')]");
			public static final By SEARCH_BAR_CANCEL_BTN = By.xpath("//*[contains(@id,'cancel-icon')]//..");
			public static final By FIRST_SEARCH_RESULT_TEXT = By.xpath("//ul//li[1]//button//div");
			public static final By FIRST_SEARCH_RESULT_BTN = By.xpath("//ul//li[1]//button");
			public static final By THIRD_SEARCH_RESULT_BTN = By.xpath("//ul//li[3]//button");
			public static final By THIRD_SEARCH_RESULT_TEXT = By.xpath("//ul//li[3]//button//div");
			public static final By CUSTOM_SP_CLOSE_BTN = By.xpath("//div[@class='shadow']//div[contains(@id,'card-header')]//button");
		
			// PO12 - Recommended Profile Details
			public static final By ORG_JOB_TITLE_HEADER = By.xpath("//div[contains(@class,'leading')]//div[1]//div[1]");
			public static final By ORG_JOB_GRADE_VALUE = By.xpath("//div[contains(@class,'leading')]//div[contains(text(),'Grade')]//span");
			public static final By ORG_JOB_DEPARTMENT_VALUE = By.xpath("//div[contains(@class,'leading')]//div[contains(text(),'Department')]//span");
			public static final By ORG_JOB_FUNCTION_VALUE = By.xpath("//div[contains(@class,'leading')]//div[contains(text(),'Function')]//span");
			public static final By POPUP_CONTAINER = By.xpath("//div[contains(@class, 'modal-body') or contains(@class,'popup-container')]");
			public static final By POPUP_VIEW_MORE_RESPONSIBILITIES = By.xpath("//div[@id='responsibilities-panel']//button[contains(text(),'View')]");
			public static final By POPUP_VIEW_MORE_BEHAVIOUR = By.xpath("//div[@id='behavioural-panel']//button[contains(text(),'View')]");
			public static final By POPUP_VIEW_MORE_SKILLS = By.xpath("//div[@id='skills-panel']//button[contains(text(),'View')]");
			public static final By SECOND_SEARCH_RESULT_TEXT = By.xpath("//ul//li[2]//button//div");
			public static final By SECOND_SEARCH_RESULT_BTN = By.xpath("//ul//li[2]//button");
			public static final By CUSTOM_PROFILE_LABEL = By.xpath("//*[contains(text(),'Custom Profile')]");
			public static final By PROFILE_1_CUSTOM_LABEL = By.xpath("//div[@class='shadow']//div[contains(@id,'card-header')][1]//..//*[contains(text(),'Custom')]");
			public static final By PROFILE_HEADER = By.xpath("//h2[@id='summary-modal']//p");
			public static final By PROFILE_DETAILS = By.xpath("//div[@id='details-container']//div[contains(@class,'mt-2')]");
			public static final By POPUP_RESPONSIBILITIES_DATA = By.xpath("//div[@id='responsibilities-panel']//div[@id='item-container']");
			public static final By BEHAVIOUR_TAB_BTN = By.xpath("//button[contains(text(),'BEHAVIOUR')]");
			public static final By POPUP_BEHAVIOUR_DATA = By.xpath("//div[@id='behavioural-panel']//div[@id='item-container']");
			public static final By SKILLS_TAB_BTN = By.xpath("//button[text()='SKILLS']");
			public static final By POPUP_SKILLS_DATA = By.xpath("//div[@id='skills-panel']//div[@id='item-container']");
			public static final By COMPARE_AND_SELECT_HEADER = By.xpath("//h1[@id='compare-desc']");
		
			// PO17 - Manual Mapping / Map Different SP to Profile
			public static final By MANUAL_MAPPING_ORG_JOB_TITLE = By.xpath("//div[contains(@class,'leading')]//div[1]//div[1]");
			public static final By MANUAL_MAPPING_ORG_JOB_GRADE = By.xpath("//div[contains(@class,'leading')]//div[contains(text(),'Grade')]//span");
			public static final By MANUAL_MAPPING_ORG_JOB_DEPARTMENT = By.xpath("//div[contains(@class,'leading')]//div[contains(text(),'Department')]//span");
			public static final By MANUAL_MAPPING_ORG_JOB_FUNCTION = By.xpath("//div[contains(@class,'leading')]//div[contains(text(),'Function')]//span");
			public static final By LAST_SAVED_PROFILE = By.xpath("//*[contains(text(),'Last')]//..");
			public static final By LAST_SAVED_PROFILE_NAME_BTN = By.xpath("//*[contains(text(),'Last')]//..//span[2]");
			public static final By MANUAL_MAPPING_PROFILE_TITLE = By.xpath("//*[@id='summary-title']//p");
			public static final By MANUAL_MAPPING_ROLE_SUMMARY = By.xpath("//div[contains(@id,'role-summary')]//p");
			public static final By MANUAL_MAPPING_PROFILE_DETAILS = By.xpath("//span[contains(text(),'Grade')]//..");
			public static final By MANUAL_MAPPING_RESPONSIBILITIES_TAB = By.xpath("//button[contains(text(),'RESPONSIBILITIES')]");
			public static final By MANUAL_MAPPING_VIEW_MORE_RESPONSIBILITIES = By.xpath("//div[contains(@id,'responsibilities')]//button[contains(text(),'more...')]");
			public static final By MANUAL_MAPPING_RESPONSIBILITIES_DATA = By.xpath("//div[contains(@id,'responsibilities')]");
			public static final By MANUAL_MAPPING_BEHAVIOUR_TAB = By.xpath("//button[contains(text(),'BEHAVIOURAL COMPETENCIES')]");
			public static final By MANUAL_MAPPING_VIEW_MORE_BEHAVIOUR = By.xpath("//div[contains(@id,'behavioural-panel')]//button[contains(text(),'more...')]");
			public static final By MANUAL_MAPPING_BEHAVIOUR_DATA = By.xpath("//div[contains(@id,'behavioural-panel')]");
			public static final By MANUAL_MAPPING_SKILLS_TAB = By.xpath("//button[contains(text(),'SKILLS')]");
			public static final By MANUAL_MAPPING_VIEW_MORE_SKILLS = By.xpath("//div[contains(@id,'skills')]//button[contains(text(),'more...')]");
			public static final By MANUAL_MAPPING_SKILLS_DATA = By.xpath("//div[contains(@id,'skills')]");
			public static final By KF_SP_SEARCH_BAR = By.xpath("//input[contains(@placeholder,'Search Korn Ferry')]");
			public static final By SAVE_SELECTION_BTN = By.xpath("//button[contains(text(),'Save selection')]");
		
			// PO16 - Manual Mapping buttons
			public static final By FIND_MATCH_BTN = By.xpath("//tbody//tr[1]//button[contains(text(),'Find')]");
			public static final By SEARCH_DIFFERENT_SP_BTN = By.xpath("//tbody//tr[2]//button[contains(text(),'different profile')]");
		
		// PO22/PO25 - Missing Data Functionality
		// Multiple fallback XPaths to handle different DOM structures
		public static final By MISSING_DATA_TIP_MESSAGE_CONTAINER = By.xpath(
			"//div[@id='warning-message-container'] | " +
			"//*[contains(text(), 'jobs have missing data')] | " +
			"//p[contains(text(), 'jobs have missing data and can reduce match accuracy')]/.. | " +
			"//div[contains(@class, 'inline-flex') and contains(., 'jobs have missing data')]"
		);
		public static final By MISSING_DATA_COUNT_AND_TEXT = By.xpath("//p[contains(text(), 'jobs have missing data and can reduce match accuracy')]");
		public static final By VIEW_REUPLOAD_JOBS_LINK = By.xpath("//a[contains(text(), 'View & Re-upload jobs') and contains(@href, 'aiauto')]");
			public static final By CLOSE_TIP_MESSAGE_BUTTON = By.xpath("//p[contains(text(),'have missing data')]//..//button[@aria-label='Dismiss warning']");
			public static final By CLOSE_REUPLOAD_JOBS_PAGE_BUTTON = By.xpath("//button[contains(@class, 'border-[#007BC7]') and contains(text(), 'Close')]");
			public static final By REUPLOAD_BUTTON = By.xpath("//button[contains(text(), 'Re-upload')] | //button[contains(text(), 'upload')]");
			public static final By JOB_TABLE_ROWS = By.xpath("//table//tr[contains(@class, 'border-b')]");
			public static final By ALTERNATIVE_CLOSE_BUTTON = By.xpath("//button[contains(text(), 'Close')] | //button[@aria-label='Close'] | //*[text()='Close']");
			public static final By REUPLOAD_PAGE_TITLE_DESC = By.xpath("//div//p[contains(text(), 're-upload the jobs')]");
			public static final By JOB_ROWS_IN_MISSING_DATA_SCREEN = By.xpath("//table//tr[contains(@class, 'border-b')]");
			public static final By SEARCH_BOX = By.xpath("//input[@id='search-job-title-input-search-input']");
			public static final By JOB_ROWS_IN_JOB_MAPPING_PAGE = By.xpath("//div[@id='org-job-container']//tbody//tr");
			public static final By JOB_MAPPING_LOGO = By.xpath("//*[@data-testid='job-mapping-logo'] | //*[contains(@class, 'job-mapping')]//img | //h1[contains(text(), 'Review and Publish')]");
			public static final By INFO_MESSAGE_ICON = By.xpath("//*[contains(@class, 'info-icon')] | //*[@data-testid='info-message'] | //button[contains(@class, 'text-[#C35500]')]");
			public static final By ORG_JOB_DEPARTMENT_HEADER = By.xpath("//*[@id='org-job-container']/div/table/thead/tr/th[4]/div");
		
			// PO23 - Info Message Missing Data Profiles
			public static final By INFO_MESSAGE_CONTAINERS = By.xpath("//div[@role='button' and @aria-label='Reduced match accuracy due to missing data']");
			public static final By INFO_MESSAGE_TEXTS = By.xpath("//div[@role='button' and @aria-label='Reduced match accuracy due to missing data']//div[contains(text(), 'Reduced match accuracy due to missing data')]");
			public static final By ORG_JOB_TABLE_ROWS = By.xpath("//div[@id='org-job-container']//tbody//tr");
			public static final By KF_JOB_TABLE_ROWS = By.xpath("//div[@id='kf-job-container']//tbody//tr");
			public static final By LOADER_ELEMENTS = By.xpath("//div[@data-testid='loader'] | //div[contains(@class, 'loader')] | //div[contains(@class, 'loading')]");
		
			// PO35 - Reupload Missing Data Profiles
			public static final By MISSING_DATA_SCREEN_TITLE = By.xpath("//h1[contains(text(), 'Jobs With Missing Data')] | //h1[contains(text(), 'Missing Data')] | //*[contains(text(), 'Organization Jobs With Missing Data')]");
			public static final By JOB_ROWS_MISSING_DATA = By.xpath("//table//tbody//tr[td]");
			public static final By SEARCH_INPUT_MISSING_DATA = By.xpath("//input[@placeholder='Search'] | //input[contains(@id, 'search')]");
			public static final By JOB_SEARCH_INPUT = By.xpath("//input[@id='search-job-title-input-search-input']");
			public static final By TIP_MESSAGE = By.xpath("//p[contains(text(), 'jobs have missing data')]");
			public static final By SEARCH_RESULTS_ROWS = By.xpath("//div[@id='org-job-container']//tbody//tr[.//td[1]//input[@type='checkbox']]");
		
			// PO36 - Delete Job Profiles
			public static final By DELETE_BUTTON = By.id("delete-selected-mappings-btn");
			public static final By DELETE_POPUP_TITLE = By.xpath("//h2[@id='delete-modal-title']");
			public static final By DELETE_POPUP_MESSAGE = By.xpath("//div[@role='document']//p[contains(text(),'Are you sure you want to delete')]");
			public static final By DELETE_POPUP_CANCEL_BTN = By.xpath("//div[@role='document']//button[text()='Cancel']");
			public static final By DELETE_POPUP_CONFIRM_BTN = By.xpath("//div[@role='document']//button[text()='Delete']");
			public static final By DELETE_SUCCESS_TITLE = By.xpath("//p[text()='Job Profiles Deleted Successfully']");
			public static final By DELETE_SUCCESS_MSG = By.xpath("//p[contains(text(),'Successfully deleted')]");
		
			// PO04 - Additional Job Mapping Components
			public static final By JOB_NAME_PROFILE_1 = By.xpath("//tbody//tr[1]//td[2]//div[contains(text(),'(')]");  // Alias for JOB_NAME_ROW_1
			public static final By JOB_1_MATCHED_PROFILE = By.xpath("//div[@id='kf-job-container']//div//table//tbody//tr[1]//td[1]//div");  // Alias for FIRST_ROW_JOB_TITLE
			public static final By PUBLISH_SELECTED_BTN = By.xpath("//button[contains(@id,'publish-approved-mappings-btn')] | //button[@id='publish-select-btn']");
			public static final By COMPARE_SELECT_HEADER = By.xpath("//h1[@id='compare-desc']");  // Alias for Compare Page header
			public static final By PUBLISHED_SUCCESS_HEADER = By.xpath("//h2[@id='modal-header']");
			public static final By SHOWING_RESULTS_COUNT = By.xpath("//div[contains(@id,'results-toggle')]//*[contains(text(),'Showing')]");  // Alias for SHOWING_JOB_RESULTS
			public static final By FILTER_OPTIONS = By.xpath("//div[@id='filters-search-btns']//div[2]//div");  // Alias for FILTER_OPTIONS_PANEL
			public static final By VIEW_PUBLISHED_TOGGLE = By.xpath("//div[contains(@id,'results-toggle')]//label//div[2]");
			public static final By PUBLISH_BTN = By.xpath("//button[@id='publish-btn']");
			public static final By PUBLISHED_BTN = By.xpath("//button[text()='Published']");
		}
		
		// ADD JOB DATA (Used in PO11)
		public static class AddJobData {
			// Page Headers
			public static final By ADD_MORE_JOBS_PAGE_HEADER = By.xpath("//*[contains(text(),'Add Job Data')]");
			
			// Upload Controls
			public static final By MANUAL_UPLOAD_BTN = By.xpath("//button[@data-testid='manual-upload-btn']");
			public static final By BROWSE_FILES_BTN = By.xpath("//*[@aria-label='Browse Files']");
			public static final By ATTACHED_FILE_NAME = By.xpath("//div[contains(@class,'text-ods-font-styles-body-regular-small')]");
			public static final By FILE_CLOSE_BTN = By.xpath("//*[@aria-label='fileclose']//*[@stroke-linejoin='round']");
			
			// Action Buttons
			public static final By CONTINUE_BTN = By.xpath("//button[@id='btnContinue']");
			public static final By DONE_BTN = By.xpath("//button[@id='btnDone']");
			public static final By ADD_MORE_JOBS_CLOSE_BTN = By.xpath("//*[@data-testid='x-mark-icon']//*");
			public static final By ADD_MORE_JOBS_CLOSE_BTN_ALT = By.xpath("//*[@aria-label='Close']//*");
			public static final By CLICK_HERE_BTN = By.xpath("//a[@id='clickHere']");
			
			// Status & Info
			public static final By KFONE_JOBS_COUNT = By.xpath("//span[contains(@class,'regular-small')]");
			public static final By LAST_SYNCED_INFO = By.xpath("//div[contains(text(),'Last Synced')]");
			public static final By UPLOAD_PROGRESS_TEXT = By.xpath("//p[contains(text(),'Upload in progress')]");
			public static final By UPLOAD_SUCCESS_MESSAGE = By.xpath("//p[text()='Upload complete!']");
			public static final By NOTHING_TO_SEE_MESSAGE = By.xpath("//span[contains(@class,'font-proxima') and contains(@class,'text-[24px]') and contains(@class,'font-semibold') and contains(text(),'Nothing to see here... yet!')]");
		}
		
		// PROFILE MANAGER SCREEN (PM Context - HCM Sync Profiles)
		public static class ProfileManagerScreen {
		// Navigation
			public static final By MENU_BTN = By.xpath("//span[contains(text(),'Hi')]//following::img[1]");
			public static final By HOME_MENU_BTN = By.xpath("//*[*[contains(text(),'Hi, ')]]/div[2]/img");
			public static final By PM_BTN = By.xpath("//a[contains(text(),'Profile Manager')]");
		
		// Page Headers
			public static final By HCM_SYNC_PROFILES_HEADER = By.xpath("//span[contains(text(),'HCM Sync Profiles')] | //div[contains(@class,'menu-tem') and contains(text(),'HCM Sync')]");
			public static final By HCM_SYNC_PROFILES_TITLE = By.xpath("//h1[contains(text(),'HCM Sync Profiles')]");
			public static final By HCM_SYNC_PROFILES_TITLE_DESC = By.xpath("//p[contains(text(),'Select a job profile')]");
			public static final By NO_SP_MSG = By.xpath("//div[contains(text(),'no Success Profiles')]");
			public static final By NO_RESULTS_MESSAGE = By.xpath("//*[contains(text(),'no results available') or contains(text(),'There are no results')]");
		
		// Job Rows
			public static final By HCM_SYNC_PROFILES_JOB_ROW1 = By.xpath("//tbody//tr[1]//td//div//span[1]//a");
			public static final By HCM_SYNC_PROFILES_JOB_ROW2 = By.xpath("//tbody//tr[2]//td//div//span[1]//a");
			public static final By HCM_SYNC_PROFILES_JOB_ROW3 = By.xpath("//tbody//tr[3]//td//div//span[1]//a");
		
		// Profile Checkboxes
			public static final By PROFILE1_CHECKBOX = By.xpath("//tbody//tr[1]//div[1]//kf-checkbox");
			public static final By PROFILE2_CHECKBOX = By.xpath("//tbody//tr[2]//div[1]//kf-checkbox");
			public static final By PROFILE3_CHECKBOX = By.xpath("//tbody//tr[3]//div[1]//kf-checkbox");
		
		// Filters
			public static final By FILTERS_DROPDOWN_BTN = By.xpath("//span[text()='Filters']");
			public static final By FILTER_OPTIONS = By.xpath("//*[@class='accordion']");
		
		// KF Grade Filters
			public static final By KF_GRADE_FILTERS_DROPDOWN = By.xpath("//thcl-expansion-panel-header//div[text()=' KF Grade ']");
			public static final By KF_GRADE_ALL_CHECKBOXES = By.xpath("//thcl-expansion-panel[contains(@class,'sp-search-filter-expansion-panel')][1]//kf-checkbox");
			public static final By KF_GRADE_ALL_LABELS = By.xpath("//thcl-expansion-panel[contains(@class,'sp-search-filter-expansion-panel')][1]//div[contains(@class,'body-text')]");
		
		// Levels Filters
			public static final By LEVELS_FILTERS_DROPDOWN = By.xpath("//thcl-expansion-panel-header//div[text()=' Levels ']");
			public static final By LEVELS_ALL_CHECKBOXES = By.xpath("//thcl-expansion-panel[contains(@class,'sp-search-filter-expansion-panel')][2]//kf-checkbox");
			public static final By LEVELS_ALL_LABELS = By.xpath("//thcl-expansion-panel[contains(@class,'sp-search-filter-expansion-panel')][2]//div[contains(@class,'body-text')]");
		
		// Functions/Subfunctions Filters
			public static final By FUNCTIONS_SUBFUNCTIONS_FILTERS_DROPDOWN = By.xpath("//thcl-expansion-panel-header//div[text()=' Functions / Subfunctions ']");
			public static final By FUNCTIONS_SUBFUNCTIONS_ALL_CHECKBOXES = By.xpath("//thcl-expansion-panel[contains(@class,'sp-search-filter-expansion-panel')][3]//thcl-expansion-panel-header//kf-checkbox");
			public static final By FUNCTIONS_SUBFUNCTIONS_ALL_LABELS = By.xpath("//thcl-expansion-panel[contains(@class,'sp-search-filter-expansion-panel')][3]//thcl-expansion-panel-header//span[contains(@class,'text-break')]");
		
		// Profile Status Filters
			public static final By PROFILE_STATUS_FILTERS_DROPDOWN = By.xpath("//thcl-expansion-panel-header//div[text()=' Profile Status ']");
			public static final By PROFILE_STATUS_ALL_CHECKBOXES = By.xpath("//thcl-expansion-panel-header//div[text()=' Profile Status ']/ancestor::thcl-expansion-panel//kf-checkbox");
			public static final By PROFILE_STATUS_ALL_LABELS = By.xpath("//thcl-expansion-panel-header//div[text()=' Profile Status ']/ancestor::thcl-expansion-panel//span[contains(@class,'wrapped') or contains(@class,'body-text')]");
		
		// Sync Messages
			public static final By SYNC_WITH_HCM_SUCCESS_POPUP_TEXT = By.xpath("//div[@class='p-toast-detail']");
			public static final By SYNC_WITH_HCM_SUCCESS_POPUP_CLOSE_BTN = By.xpath("//button[contains(@class,'p-toast-icon-close')]");
			public static final By SYNC_WITH_HCM_WARNING_MSG = By.xpath("//span[contains(@class,'message')]");
			public static final By SYNC_WITH_HCM_WARNING_MSG_CLOSE_BTN = By.xpath("//button[contains(@class,'close-btn')]");
		
		// PO19 - Profiles with No Job Code
			public static final By NO_JOB_CODE_TOOLTIP = By.xpath("//div[@class='p-tooltip-text']");
		
		// PO21 - Export Status / Edit Success Profile
			public static final By THREE_DOTS_SP_DETAILS = By.xpath("//kf-icon[contains(@class,'dots-three')]");
			public static final By EDIT_SP_BUTTON = By.xpath("//*[contains(text(),'Edit Success')]");
			public static final By EDIT_DETAILS_BTN = By.xpath("//*[contains(@class,'editDetails')]");
			public static final By FUNCTION_DROPDOWN = By.xpath("//label[contains(text(),'Function')]//..//..//button");
			public static final By SUBFUNCTION_DROPDOWN = By.xpath("//label[contains(text(),'Subfunction')]//..//..//button");
			public static final By DROPDOWN_OPTIONS = By.xpath("//kf-dropdown-item//div//span");
		
		// PO28 - Select All With Filters (PM Screen)
			public static final By PM_KF_GRADE_HEADER = By.xpath("//thcl-expansion-panel-header[@id='expansion-panel-header-0']");
			public static final By PM_LEVELS_HEADER = By.xpath("//thcl-expansion-panel-header[@id='expansion-panel-header-1']");
		
		// PO34 - Sorting Functionality in HCM Screen
			public static final By PROFILE_NAME_ELEMENTS = By.xpath("//tbody//tr//td//div//span[1]//a");
			public static final By LEVEL_ELEMENTS = By.xpath("//tbody//tr//td[4]//div//span[1]");
			public static final By JOB_STATUS_ELEMENTS = By.xpath("//tbody//tr//td[2]//div//span[1]");
			public static final By JOB_CODE_ELEMENTS = By.xpath("//tbody//tr//td[3]//div//span[1]");
			public static final By FUNCTION_ELEMENTS = By.xpath("//tbody//tr//td[5]//div//span[1]");
			public static final By EXPORT_STATUS_ELEMENTS = By.xpath("//tbody//tr//td[8]//div//span[1]");
		}
		
		// KFONE SCREEN (KFONE Navigation & Teams)
		public static class KFOneScreen {
		// UAM & Navigation
		public static final By UAM_BUTTON = By.xpath("//*[contains(text(),'User Admin')]");
		public static final By CLIENT_NAME_TEXT = By.xpath("//*[contains(@class,'kfcon-header')]");
		public static final By USER_ACCESS_BTN = By.xpath("//button[@aria-label='User Access']");
		
		// Teams Management
		public static final By TEAMS_SECTION = By.xpath("//*[contains(@icon,'usergroups')]");
		public static final By TEAMS_PAGE_HEADER = By.xpath("//*[contains(@class,'teams-header')]");
		public static final By TEAMS_PAGE_DESC = By.xpath("//*[contains(text(),'A Team is a group')]");
		public static final By TEAMS_PAGE_SEARCHBAR = By.xpath("//*[contains(@placeholder,'Search Teams')]");
		public static final By CREATE_TEAMS_BTN = By.xpath("//*[contains(text(),'CREATE TEAMS')]");
		public static final By CREATE_TEAMS_STEP1_HEADER = By.xpath("//*[contains(text(),'Step 1 of 2')]");
		public static final By CREATE_TEAMS_STEP2_HEADER = By.xpath("//*[contains(text(),'Step 2 of 2')]");
		public static final By CREATE_TEAMS_STEP2_SEARCHBAR = By.xpath("//*[contains(@placeholder,'User Search')]");
		public static final By TEAM_NAME_TEXTBOX = By.xpath("//input[contains(@class,'primary')]");
		public static final By TEAM_DESC_TEXTBOX = By.xpath("//textarea[contains(@class,'primary')]");
		public static final By TEAM_MEMBERS_HEADER = By.xpath("//*[contains(text(),'Team Members (')]");
		public static final By USER_COUNT = By.xpath("//*[contains(text(),'Showing 1')]");
		public static final By TEAM_NAME_IN_TOP_ROW = By.xpath("//td//div//span//a[not(contains(text(),'CREATE'))]");
		public static final By NEXT_BTN = By.xpath("//span[contains(text(),'Next')]");
		}
		
		// PROFILE MANAGER - COLLECTIONS & TEAMS (Used in PO20 profile collections)
		public static class ProfileManagerPage {
			// Dashboard
			public static final By CLIENT_DASHBOARD = By.xpath("//h1[contains(text(),'Dashboard')]");
			public static final By PM_HEADER = By.xpath("//h1[contains(text(),'Profile Manager')]");
		
		// Profile Collections
		public static final By PC_SECTION = By.xpath("//*[contains(@class,'kf-icon-profilecollection')]");
		public static final By PC_PAGE_HEADER = By.xpath("//*[contains(@class,'profile-collection-header')]");
		public static final By PC_PAGE_DESC = By.xpath("//*[contains(text(),'create a Profile Collection')]");
		public static final By CREATE_PC_BTN = By.xpath("//*[contains(text(),'CREATE PROFILE')]");
		public static final By CREATE_PC_STEP1_HEADER = By.xpath("//*[contains(text(),'Step 1 of 3')]");
		public static final By CREATE_PC_STEP2_HEADER = By.xpath("//*[contains(text(),'Step 2 of 3')]");
		public static final By CREATE_PC_STEP3_HEADER = By.xpath("//*[contains(text(),'Step 3 of 3')]");
		public static final By PC_NAME_TEXTBOX = By.xpath("//input[contains(@class,'primary')]");
		public static final By PC_DESC_TEXTBOX = By.xpath("//textarea[contains(@class,'primary')]");
		public static final By PC_SEARCHBAR = By.xpath("//*[contains(@placeholder,'Profile Collection')]");
		public static final By PC_COUNT = By.xpath("//*[contains(text(),'Showing ')]");
		public static final By PC_SUCCESS_POPUP = By.xpath("//*[contains(text(),'been saved')]");
		public static final By SAVE_BTN = By.xpath("//span[contains(text(),'Save')]");
		public static final By DONE_BTN = By.xpath("//*[contains(text(),'Done')]");
		public static final By DELETE_CONFIRM_BTN = By.xpath("//footer//*[contains(text(),'Delete')]");
		public static final By DELETION_SUCCESS_POPUP = By.xpath("//*[contains(text(),'deleted')]");
		
		// Teams in Profile Collections
		public static final By ALL_TEAMS_HEADER = By.xpath("//*[contains(text(),' ALL TEAMS (')]");
		public static final By SELECTED_TEAMS_HEADER = By.xpath("//*[contains(text(),' SELECTED TEAMS (')]");
		public static final By TEAM_COUNT = By.xpath("//*[contains(text(),'Page 1')]");
		public static final By TOP_ROW_THREE_DOTS = By.xpath("//*[contains(@icon,'dots-three')]");
		
		// Success Profiles in Profile Collections
		public static final By ADD_ADDITIONAL_SP_HEADER = By.xpath("//*[contains(text(),' ADD ADDITIONAL SUCCESS PROFILES (')]");
		public static final By SP_HEADER_IN_PC = By.xpath("//*[contains(text(),' SUCCESS PROFILES (')]");
		public static final By PC_STEP3_SEARCHBAR = By.xpath("//*[contains(@placeholder,'Search success profiles')]");
		public static final By SELECT_ALL_CHECKBOX = By.xpath("//*/kf-page-content/kfconf-profile-collection-addedit/div/div[3]/div/div[4]/kf-checkbox/div");
		
		// Success Messages
		public static final By CREATE_TEAMS_SUCCESS_POPUP = By.xpath("//*[contains(text(),'successfully')]");
		public static final By CREATE_TEAMS_FAILURE_POPUP = By.xpath("//*[contains(text(),'Team already exists')]");
		
		// Tip Messages
		public static final By TIP_MSG2_TEXT = By.xpath("//*[contains(text(), 'permissioning restrictions ')]");
		public static final By TIP_MSG2_CLOSE_BTN = By.xpath("//*[contains(text(), 'permissioning restrictions ')]//..//button");
		}
		
		// PM SELECTION/SYNC SCREEN (Used in PO26, PO28, PO32)
	public static class PMSelectionScreen {
		public static final By ALL_PROFILE_ROWS = By.xpath("//tbody//tr[.//kf-checkbox]");
		public static final By SELECTED_PROFILE_ROWS = By.xpath("//tbody//tr[.//kf-icon[@icon='checkbox-check' and contains(@class,'ng-star-inserted')]]");
		// FIXED: More specific locator to target chevron beside table header checkbox, not search dropdown
		public static final By CHEVRON_BUTTON = By.xpath("//thead//th//kf-icon[contains(@class,'arrow-down') or @icon='arrow-down'] | //thead//th//*[contains(@class,'chevron')]");
		public static final By HEADER_CHECKBOX = By.xpath("//thead//tr//th[1]//div[1]//kf-checkbox//div");
		public static final By SYNC_BUTTON = By.xpath("//button[contains(@class,'custom-export')] | //button[contains(text(),'Sync with HCM')]");
		public static final By SEARCH_BAR = By.xpath("//input[@type='search']");
		public static final By CLEAR_ALL_FILTERS_BTN = By.xpath("//a[contains(text(),'Clear All')]");
		
		// PO26 - Select and Sync Profiles
		public static final By ALL_CHECKBOXES = By.xpath("//tbody//tr//td[1]//div[1]//kf-checkbox");
		public static final By ALL_ROWS = By.xpath("//tbody//tr");
		public static final By SHOWING_RESULTS_COUNT = By.xpath("//div[contains(text(),'Showing') or contains(text(),'Success Profiles')]");
		
		// PO32 - Clear Profile Selection (PM specific)
		public static final By PM_ALL_CHECKBOXES = By.xpath("//tbody//tr//td[1]//div[1]//kf-checkbox");
		public static final By PM_NONE_BTN = By.xpath("//*[contains(text(),'None')]");
	}
		
		// JAM SELECTION SCREEN (Used in PO29, PO30, PO31, PO33, PO35, PO36)
		public static class JAMSelectionScreen {
			public static final By SHOWING_RESULTS_COUNT = By.xpath("//div[contains(@id,'results-toggle')]//*[contains(text(),'Showing')]");
			public static final By ALL_PROFILE_ROWS = By.xpath("//div[@id='org-job-container']//tbody//tr[.//td[1]//input[@type='checkbox']]");
			public static final By SELECTED_PROFILE_ROWS = By.xpath("//div[@id='org-job-container']//tbody//tr[.//td[1]//input[@type='checkbox' and @checked]]");
			public static final By CHEVRON_BUTTON = By.xpath("//th[@scope='col']//div[@class='relative inline-block']//div//*[contains(@class,'cursor-pointer')]");
			public static final By HEADER_CHECKBOX = By.xpath("//thead//input[@type='checkbox']");
			public static final By PUBLISH_BUTTON = By.xpath("//button[contains(@id,'publish-approved-mappings-btn')] | //button[contains(text(),'Publish Selected')]");
			
			// PO29, PO31, PO32, PO33 - JAM specific checkboxes
			public static final By ALL_CHECKBOXES = By.xpath("//tbody//tr//td[1][contains(@class,'whitespace')]//input");
			public static final By JAM_ALL_CHECKBOXES = By.xpath("//tbody//tr//td[1][contains(@class,'whitespace')]//input");
			public static final By JAM_NONE_BTN = By.xpath("//*[contains(text(),'None')]");
			
			// PO31 - Application Performance
			public static final By PROFILE_ROWS = By.xpath("//div[@id='org-job-container']//tbody//tr//td[2]//div[contains(text(),'(')]");
			public static final By HCM_PROFILE_CHECKBOXES = By.xpath("//tbody//tr//td[1]//div[1]//kf-checkbox");
			
			// PO33 - Unmapped Jobs
			public static final By DISABLED_CHEVRONS = By.xpath("//th[@scope='col']//div[@class='relative inline-block']//*[contains(@class,'cursor-not-allowed opacity-30')]");
			public static final By ANY_CHEVRONS = By.xpath("//th[@scope='col']//div[@class='relative inline-block']//div//*");
			public static final By TOOLTIP_CONTAINERS = By.xpath("//tbody//tr//td[1][contains(@class,'whitespace')]//div[@data-testid='tooltip-container']");
		}
		
	}  // END OF LOCATORS CLASS

	// ==================== WAIT UTILITIES ====================
	// Instance helper method - delegate to Utilities for actual wait logic
	
	protected void waitForInvisibility(By locator, int timeoutSeconds) {
		try {
			WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
			Utilities.waitForInvisible(customWait, locator);
		} catch (Exception e) {
			// Element not found or already invisible - this is acceptable
			LOGGER.debug("Element not found or already invisible: " + locator);
		}
	}

	protected void waitForPageLoad() {
		Utilities.waitForSpinnersToDisappear(driver, 10);
		Utilities.waitForPageReady(driver, 3);
	}

	protected void waitForSpinners() {
		waitForSpinners(15);
	}
	
	protected void waitForSpinners(int timeoutSeconds) {
		try {
			Utilities.waitForSpinnersToDisappear(driver, timeoutSeconds);
		} catch (Exception e) {
			LOGGER.warn("Spinner wait encountered issue, checking page readiness as fallback: {}", e.getMessage());
			try {
				Utilities.waitForPageReady(driver, 3);
				LOGGER.info("Page appears ready despite spinner visibility - continuing");
			} catch (Exception fallbackEx) {
				LOGGER.warn("Page readiness check also failed, but continuing execution: {}", fallbackEx.getMessage());
			}
		}
	}

	protected void safeSleep(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	// ==================== ELEMENT FINDING UTILITIES ====================
	// Delegate to Utilities for implementation
	
	protected WebElement findElement(By locator) {
		return driver.findElement(locator);
	}

	protected List<WebElement> findElements(By locator) {
		return driver.findElements(locator);
	}

	protected WebElement getFirstElementOrNull(By locator) {
		return Utilities.getFirstElementOrNull(driver, locator);
	}

	protected boolean hasElements(By locator) {
		return Utilities.hasElements(driver, locator);
	}

	protected boolean isElementDisplayed(By locator) {
		return Utilities.isElementDisplayed(driver, locator);
	}

	protected String getElementText(By locator) {
		return Utilities.getElementText(wait, locator, LOGGER);
	}

	// ==================== CLICK UTILITIES ====================
	// Delegate to Utilities for implementation
	
	protected void clickElement(WebElement element) {
		Utilities.clickElement(driver, wait, js, element);
	}

	protected void clickElement(By locator) {
		Utilities.clickElement(driver, wait, js, locator);
	}

	protected void clickElementSafely(By locator, int timeoutSeconds) {
		Utilities.clickElementSafely(driver, wait, js, locator, timeoutSeconds);
	}

	protected void jsClick(WebElement element) {
		Utilities.jsClick(js, element);
	}

	protected boolean tryClickWithStrategies(WebElement element) {
		return Utilities.tryClickWithStrategies(driver, js, element);
	}

	protected boolean tryClickWithStrategies(By locator, String elementName) {
		try {
			WebElement element = Utilities.waitForClickable(wait, locator);
			boolean result = Utilities.tryClickWithStrategies(driver, js, element);
			if (result) {
				LOGGER.debug("Clicked on {}", elementName);
			}
			return result;
		} catch (Exception e) {
			LOGGER.warn("Failed to click on {}: {}", elementName, e.getMessage());
			return false;
		}
	}

	// ==================== SCROLL UTILITIES ====================
	// Delegate to Utilities for implementation
	
	protected void scrollToElement(WebElement element) {
		Utilities.scrollToElement(js, element);
	}

	protected void scrollToTop() {
		Utilities.scrollToTop(js);
	}

	protected void scrollToBottom() {
		Utilities.scrollToBottom(js);
	}

	// ==================== PAGE OPERATIONS ====================
	
	protected void refreshPage() {
		Utilities.refreshPage(driver);
	}

	// ==================== POPUP & BANNER HANDLERS ====================
	
	protected void handleCookiesBanner() {
		try {
			WebDriverWait quickWait = new WebDriverWait(driver, Duration.ofSeconds(2));
			WebElement cookiesBtn = Utilities.waitForClickable(quickWait, Locators.Common.ACCEPT_COOKIES_BTN);
			tryClickWithStrategies(cookiesBtn);
			LOGGER.debug("Accepted cookies banner");
		} catch (Exception e) {
			// Cookie popup not present or already handled - continue silently
		}
	}

	public void dismissKeepWorkingPopupIfPresent() {
		try {
			driver.manage().timeouts().implicitlyWait(Duration.ZERO);
			
			WebDriverWait quickWait = new WebDriverWait(driver, Duration.ofSeconds(2));
			
			By[] popupLocators = {
				By.xpath("//h2[contains(text(),'Keep working?')]"),
				By.xpath("//*[contains(text(),'lost track of your client selection')]"),
				By.xpath("//div[contains(@class,'modal') or contains(@class,'dialog')]//h2[contains(text(),'Keep')]")
			};
			
			By[] continueButtonLocators = {
				By.id("global-nav-modal-success-btn"),
				By.xpath("//button[@id='global-nav-modal-success-btn']"),
				By.xpath("//button[@data-testid='global-nav-modal-success-btn']"),
				By.xpath("//button[@aria-label='Continue' and contains(@class,'kf1wc-button')]"),
				By.xpath("//button[contains(text(),'Continue')]"),
				By.xpath("//button[@type='button' and @aria-label='Continue']")
			};
			
			boolean popupFound = false;
			for (By locator : popupLocators) {
				try {
					WebElement popup = Utilities.waitForVisible(quickWait, locator);
					if (popup.isDisplayed()) {
						popupFound = true;
						LOGGER.info("'Keep working?' popup detected - dismissing...");
						break;
					}
				} catch (Exception e) {
					// Continue to next locator
				}
			}
			
			if (popupFound) {
				boolean continueClicked = false;
				for (By buttonLocator : continueButtonLocators) {
					try {
						WebElement continueBtn = Utilities.waitForClickable(quickWait, buttonLocator);
						continueBtn.click();
						continueClicked = true;
						safeSleep(2000);
						Utilities.waitForPageReady(driver, 5);
						break;
					} catch (Exception e) {
						LOGGER.debug("Continue button locator failed: {}, trying next...", buttonLocator);
					}
				}
				
				if (continueClicked) {
					LOGGER.info("Popup dismissed - UI navigated to KFone Clients screen");
					try {
						PO01_KFoneLogin loginPO = new PO01_KFoneLogin();
						PO04_JobMappingPageComponents jamPO = new PO04_JobMappingPageComponents();
						PO05_PublishJobProfile po05 = new PO05_PublishJobProfile();
						handleCookiesBanner();
						LOGGER.info("=== Executing @Client_with_PM_Access Scenario ===");
						loginPO.user_is_in_kfone_clients_page();
						loginPO.search_for_client_with_pams_id();
						loginPO.verify_client_name_based_on_pams_id();
						loginPO.verify_products_that_client_can_access();
						loginPO.click_on_client_with_access_to_profile_manager_application();
						loginPO.verify_user_navigated_to_kfone_home_page();
						loginPO.click_on_profile_manager_application_in_your_products_section();
						loginPO.verify_user_seemlessly_landed_on_profile_manager_application_in_kf_hub();
						po05.verify_user_should_land_on_profile_manager_dashboard_page();
						LOGGER.info("=== Executing @Navigate_To_Job_Mapping Scenario ===");
						jamPO.navigate_to_job_mapping_page_from_kfone_global_menu_in_pm();
						jamPO.user_should_verify_job_mapping_logo_is_displayed_on_screen();
						LOGGER.info("Successfully restored original state - All steps from both scenarios executed");
						
					} catch (Exception navEx) {
						LOGGER.error("Failed to restore original state after popup dismissal: {}", navEx.getMessage());
						navEx.printStackTrace();
						LOGGER.info("WARNING: Could not automatically restore Job Mapping page after popup. Manual intervention may be needed.");
					}
				} else {
					LOGGER.warn("'Keep working?' popup detected but Continue button could not be clicked");
				}
			}
			
		} catch (Exception e) {
			LOGGER.debug("'Keep working?' popup not present or already dismissed: {}", e.getMessage());
		} finally {
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		}
	}

	// ==================== NAVIGATION HELPERS ====================
	
	protected void clickLogo() {
		clickElement(Locators.Common.KF_TALENT_SUITE_LOGO);
		LOGGER.info("Clicked on KF Talent Suite Logo");
	}

	protected void openGlobalNavMenu() {
		clickElement(Locators.Common.GLOBAL_NAV_MENU_BTN);
		Utilities.waitForUIStability(driver, 1);
	}

	protected void navigateToJobMapping() {
		openGlobalNavMenu();
		clickElement(Locators.Common.JOB_MAPPING_BTN);
		waitForPageLoad();
		LOGGER.info("Navigated to Job Mapping screen");
	}

	protected void openUserProfile() {
		clickElement(Locators.Common.PROFILE_BTN);
		Utilities.waitForUIStability(driver, 1);
	}

	protected void signOut() {
		openUserProfile();
		clickElement(Locators.Common.SIGN_OUT_BTN);
		waitForPageLoad();
		LOGGER.info("Signed out from application");
	}

	// ==================== SEARCH UTILITIES ====================
	
	protected void searchFor(String searchText) {
		WebElement searchBar = Utilities.waitForVisible(wait, Locators.Common.SEARCH_BAR);
		searchBar.clear();
		searchBar.sendKeys(searchText);
		waitForSpinners();
		LOGGER.info("Searched for: " + searchText);
	}

	protected void clearAndSearch(By searchLocator, String searchTerm) {
		try {
			waitForSpinners();
			Utilities.waitForSpinnersToDisappear(driver, 10);
			Utilities.waitForPageReady(driver, 5);
			
			// Wait for blocking loader to disappear
			waitForInvisibility(By.xpath("//div[contains(@class, 'blocking-loader')]"), 5);
			
			WebDriverWait extendedWait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement searchBar = Utilities.waitForClickable(extendedWait, searchLocator);
			
			// Use JavaScript click as fallback to avoid interception
			try {
				searchBar.click();
			} catch (ElementClickInterceptedException e) {
				LOGGER.warn("Click intercepted, using JavaScript click");
				js.executeScript("arguments[0].click();", searchBar);
			}
			safeSleep(200);
			
			searchBar.clear();
			searchBar.sendKeys(Keys.CONTROL + "a");
			searchBar.sendKeys(Keys.DELETE);
			safeSleep(200);
			
			searchBar.sendKeys(searchTerm);
			safeSleep(300);
			searchBar.sendKeys(Keys.ENTER);
			
			waitForSpinners();
			Utilities.waitForPageReady(driver, 3);
			
			LOGGER.info("Searched for: " + searchTerm);
		} catch (Exception e) {
			LOGGER.error("Failed to clear and search for: " + searchTerm, e);
			throw new RuntimeException("Search operation failed", e);
		}
	}

	protected void clearSearchBar(By searchLocator) {
		try {
			WebElement searchBar = Utilities.waitForVisible(wait, searchLocator);
			searchBar.click();
			safeSleep(200);
			
			searchBar.clear();
			searchBar.sendKeys(Keys.CONTROL + "a");
			searchBar.sendKeys(Keys.DELETE);
			safeSleep(200);
			
			LOGGER.info("Cleared search bar");
		} catch (Exception e) {
			LOGGER.error("Failed to clear search bar", e);
			throw new RuntimeException("Clear search operation failed", e);
		}
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

	// ==================== FILTER HELPERS ====================
	
	protected void openFilters() {
		clickElement(Locators.Common.FILTERS_BTN);
		Utilities.waitForUIStability(driver, 1);
	}

	protected void clearFilters() {
		openFilters();
		clickElement(Locators.Common.CLEAR_FILTERS_BTN);
		waitForSpinners();
		LOGGER.info("Cleared all filters");
	}

	protected void closeFilters() {
		try {
			clickElement(Locators.Common.FILTERS_BTN);
			Utilities.waitForUIStability(driver, 1);
			LOGGER.info("Closed filters panel");
		} catch (Exception e) {
			LOGGER.debug("Filters panel already closed or not present");
		}
	}

	protected void openFilterDropdown(String filterName) {
		try {
			By filterLocator = By.xpath("//div[contains(text(),'" + filterName + "')]");
			clickElement(filterLocator);
			Utilities.waitForUIStability(driver, 1);
			LOGGER.info("Opened " + filterName + " filter dropdown");
		} catch (Exception e) {
			LOGGER.error("Failed to open " + filterName + " filter dropdown", e);
			throw new RuntimeException("Filter dropdown open failed: " + filterName, e);
		}
	}

	// ==================== TABLE HELPERS ====================
	
	protected int getResultsCount() {
		String countText = getElementText(Locators.Common.RESULTS_COUNT_TEXT);
		return Utilities.parseProfileCountFromText(countText);
	}

	protected void selectAllProfiles() {
		clickElement(Locators.Common.HEADER_CHEVRON_BTN);
		Utilities.waitForPageReady(driver, 1);
		clickElement(Locators.Common.SELECT_ALL_BTN);
		waitForSpinners();
		LOGGER.info("Selected all profiles");
	}

	protected void deselectAllProfiles() {
		clickElement(Locators.Common.HEADER_CHEVRON_BTN);
		Utilities.waitForPageReady(driver, 1);
		clickElement(Locators.Common.SELECT_NONE_BTN);
		waitForSpinners();
		LOGGER.info("Deselected all profiles");
	}

	// ==================== CHECKBOX UTILITIES ====================
	
	protected int countSelectedCheckboxes(String containerSelector) {
		try {
			int count = driver.findElements(By.xpath(
					"//" + containerSelector.replace(" ", "//") + "[.//kf-icon[@icon='checkbox-check' and contains(@class,'ng-star-inserted')]]")).size();
			
			if (count == 0) {
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
			String jsScriptKfCheckbox = "return document.querySelectorAll('" + containerSelector + 
					" td:first-child kf-checkbox div.disable').length;";
			Object result = js.executeScript(jsScriptKfCheckbox);
			int count = ((Long) result).intValue();
			
			if (count == 0) {
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
			String jsScriptKfCheckbox = "return document.querySelectorAll('" + containerSelector + 
					" td:first-child kf-checkbox div').length;";
			Object result = js.executeScript(jsScriptKfCheckbox);
			int count = ((Long) result).intValue();
			
			if (count == 0) {
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

	protected void logProfileCountSummary(int total, int selected, int disabled) {
		int unselected = total - selected - disabled;
		LOGGER.info("=== PROFILE COUNT SUMMARY ===");
		LOGGER.info("Total Profiles: {}", total);
		LOGGER.info(" Selected (to be processed): {}", selected);
		LOGGER.info(" Unselected (enabled but not selected): {}", unselected);
		LOGGER.info(" Disabled (cannot be selected): {}", disabled);
	}

	// ==================== SCREEN-SPECIFIC LOCATOR HELPERS ====================
	// These helpers now use universal Locators.Common - no more screen-specific differences!
	
	protected String getScreenName(String screen) {
		return "PM".equalsIgnoreCase(screen) ? "HCM Sync Profiles" : "Job Mapping";
	}

	protected By getShowingResultsCountLocator(String screen) {
		return Locators.Common.RESULTS_COUNT_TEXT;
	}

	protected By getAllProfileRowsLocator(String screen) {
		return Locators.Common.ALL_PROFILE_ROWS;
	}

	protected By getSelectedProfileRowsLocator(String screen) {
		return Locators.Common.SELECTED_PROFILE_ROWS;
	}

	protected By getChevronButtonLocator(String screen) {
		return Locators.Common.HEADER_CHEVRON_BTN;
	}

	protected By getHeaderCheckboxLocator(String screen) {
		return Locators.Common.HEADER_CHECKBOX;
	}

	protected By getActionButtonLocator(String screen) {
		return Locators.Common.ACTION_BUTTON;
	}

	protected By getSearchBarLocator(String screen) {
		return Locators.Common.SEARCH_BAR;
	}

	protected By getClearFiltersButtonLocator(String screen) {
		return Locators.Common.CLEAR_FILTERS_BTN;
	}

	protected int countSelectedProfilesJS(String screen) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			Object result;
			
			if ("PM".equalsIgnoreCase(screen)) {
				String jsScript = 
					"return Array.from(document.querySelectorAll('tbody tr td:first-child kf-checkbox')).filter(cb => " +
					"  cb.querySelector('kf-icon[icon=\"checkbox-check\"]') && " +
					"  !cb.querySelector('div.disable')" +
					").length;";
				result = js.executeScript(jsScript);
				int count = result != null ? ((Long) result).intValue() : 0;
				if (count == 0) {
					count = findElements(Locators.Common.SELECTED_PROFILE_ROWS).size();
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

	// ==================== STRING UTILITIES ====================
	
	// ==================== TEXT PROCESSING UTILITIES ====================
	// Generic methods delegated to Utilities
	
	// Job Mapping specific (kept in BasePageObject)
	protected String cleanJobName(String rawJobName) {
		if (rawJobName == null || rawJobName.isEmpty()) return "";
		String cleaned = rawJobName.trim();
		if (cleaned.contains("(")) {
			cleaned = cleaned.substring(0, cleaned.indexOf("(")).trim();
		}
		return cleaned;
	}

	protected String extractJobNameForSearch(String fullJobName) {
		if (fullJobName == null || fullJobName.isEmpty()) return "";
		String cleaned = cleanJobName(fullJobName);
		String[] words = cleaned.split("\\s+");
		if (words.length > 3) {
			return words[0] + " " + words[1] + " " + words[2];
		}
		return cleaned;
	}

	// ==================== PARSING UTILITIES ====================
	// Only methods that need driver instance are kept here
	
	protected int getRowIndex(WebElement rowElement) {
		return Utilities.getRowIndex(driver, rowElement);
	}

	// ==================== SORTING VALIDATION UTILITIES ====================
	// Validation with logging - uses Utilities for text processing
	
	protected int validateAscendingOrder(List<String> values) {
		int violations = 0;
		for (int i = 0; i < values.size() - 1; i++) {
			String currentNormalized = Utilities.normalizeForSorting(values.get(i));
			String nextNormalized = Utilities.normalizeForSorting(values.get(i + 1));

			if (Utilities.isNonAscii(currentNormalized) && Utilities.isNonAscii(nextNormalized)) {
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
			String currentNormalized = Utilities.normalizeForSorting(values.get(i));
			String nextNormalized = Utilities.normalizeForSorting(values.get(i + 1));

			if (Utilities.isNonAscii(currentNormalized) && Utilities.isNonAscii(nextNormalized)) {
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

	// ==================== UI ELEMENT EXPANSION UTILITIES ====================
	
	protected void expandAllViewMoreButtons(By viewMoreLocator) {
		try {
			int maxAttempts = 20;
			int attemptCount = 0;
			int totalClicked = 0;
			
			while (attemptCount < maxAttempts) {
				List<WebElement> viewMoreBtns = findElements(viewMoreLocator);
				
				if (viewMoreBtns.isEmpty()) {
					LOGGER.debug("No more View More buttons found. Total clicked: {}", totalClicked);
					break;
				}
				
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
							break;
						}
					} catch (Exception e) {
						LOGGER.debug("Could not click View More button: {}", e.getMessage());
					}
				}
				
				if (!buttonClicked) {
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

	// ==================== BACKGROUND DATA LOAD MONITORING ====================
	
	protected void waitForBackgroundDataLoad() {
		try {
			LOGGER.info("Waiting for background data API to complete...");
			
			JavascriptExecutor js = (JavascriptExecutor) driver;
			
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
			
			js.executeScript(
				"if (!window._dataApiMonitor) {" +
				"  window._dataApiMonitor = true;" +
				"  window._requestPending = 0;" +
				"  window._dataApiComplete = false;" +
				
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
				
				if (waited <= 10) {
					if (pending != null && pending > 0) consecutiveZero = 0;
					continue;
				}
				
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


