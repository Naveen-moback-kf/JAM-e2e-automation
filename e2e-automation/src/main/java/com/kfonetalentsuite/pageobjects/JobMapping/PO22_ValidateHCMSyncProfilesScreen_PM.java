package com.kfonetalentsuite.pageobjects.JobMapping;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.TimeoutException;
import org.testng.Assert;

import com.kfonetalentsuite.utils.JobMapping.Utilities;
import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.HeadlessCompatibleActions;
import com.kfonetalentsuite.utils.PageObjectHelper;
import com.kfonetalentsuite.webdriverManager.DriverManager;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

public class PO22_ValidateHCMSyncProfilesScreen_PM {
	
	WebDriver driver = DriverManager.getDriver();
	private HeadlessCompatibleActions headlessActions;	

	protected static final Logger LOGGER = (Logger) LogManager.getLogger();
	PO22_ValidateHCMSyncProfilesScreen_PM validateHCMSyncProfilesTab_PM;
	
	// Dynamic search profile names with fallback options
	// The system will try each profile name in order until results are found
	public static String[] SEARCH_PROFILE_NAME_OPTIONS = {"Architect", "Manager", "Analyst", "Senior", "Engineer", "Administrator", "Assistant", "Coordinator", "Technician", "Director"};
	
	// THREAD-SAFE: Each thread gets its own isolated state for parallel execution
	public static ThreadLocal<String> jobProfileName = ThreadLocal.withInitial(() -> "Architect"); // Will be set dynamically based on which name returns results
	
	public static ThreadLocal<String> intialResultsCount = ThreadLocal.withInitial(() -> null);
	public static ThreadLocal<String> updatedResultsCount = ThreadLocal.withInitial(() -> null);
	public static ThreadLocal<String> orgJobNameInRow1 = ThreadLocal.withInitial(() -> null);
	public static ThreadLocal<Integer> profilesCount = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<Boolean> isProfilesCountComplete = ThreadLocal.withInitial(() -> true); // Flag: true = all profiles loaded & counted, false = maxScrollAttempts reached (incomplete count)
	public static ThreadLocal<String> jobname1 = ThreadLocal.withInitial(() -> null);
	public static ThreadLocal<String> jobname2 = ThreadLocal.withInitial(() -> null);
	public static ThreadLocal<String> jobname3 = ThreadLocal.withInitial(() -> null);
	
	// Variables for Feature 33 - Select Loaded Profiles validation
	// THREAD-SAFE: Each thread maintains its own counts
	public static ThreadLocal<Integer> loadedProfilesBeforeHeaderCheckboxClick = ThreadLocal.withInitial(() -> 0); // Profiles loaded on screen BEFORE clicking header checkbox
	public static ThreadLocal<Integer> selectedProfilesAfterHeaderCheckboxClick = ThreadLocal.withInitial(() -> 0); // Profiles actually selected (enabled profiles)
	public static ThreadLocal<Integer> disabledProfilesCountInLoadedProfiles = ThreadLocal.withInitial(() -> 0); // Disabled profiles (cannot be selected)
	

	public PO22_ValidateHCMSyncProfilesScreen_PM() throws IOException {
		// super();
		PageFactory.initElements(driver, this);
		this.headlessActions = new HeadlessCompatibleActions(driver);
		// TODO Auto-generated constructor stub
	}

	WebDriverWait wait = DriverManager.getWait();
	Utilities utils = new Utilities();
	JavascriptExecutor js = (JavascriptExecutor) driver;
	
	//XAPTHs
	@FindBy(xpath = "//*[@class='blocking-loader']//img")
	@CacheLookup
	WebElement pageLoadSpinner;
	
	@FindBy(xpath = "//span[contains(text(),'Hi')]//following::img[1]")
	@CacheLookup
	public WebElement MenuBtn;

	@FindBy(xpath = "//*[*[contains(text(),'Hi, ')]]/div[2]/img")
	@CacheLookup
	WebElement homeMenuBtn1;

	@FindBy(xpath = "//a[contains(text(),'Profile Manager')]")
	@CacheLookup
	public WebElement PMBtn;

	@FindBy(xpath = "//h1[contains(text(),'Profile Manager')]")
	@CacheLookup
	public WebElement PMHeader;
	
	@FindBy(xpath = "//span[contains(text(),'HCM Sync Profiles')]")
	@CacheLookup
	public WebElement hcmSyncProfilesHeader;
	
	@FindBy(xpath = "//h1[contains(text(),'Sync Profiles')]")
	@CacheLookup
	public WebElement hcmSyncProfilesTitle;
	
	@FindBy(xpath = "//p[contains(text(),'Select a job profile')]")
	@CacheLookup
	public WebElement hcmSyncProfilesTitleDesc;
	
	@FindBy(xpath = "//input[@type='search']")
	@CacheLookup
	public WebElement hcmSyncProfilesSearchbar;
	
	@FindBy(xpath = "//tbody//tr[1]//td//div//span[1]//a")
	@CacheLookup
	public WebElement HCMSyncProfilesJobinRow1;
	
	@FindBy(xpath = "//div[contains(text(),'no Success Profiles')]")
	@CacheLookup
	public WebElement NoSPMsg;
	
	@FindBy(xpath = "//*[contains(text(),'no results available') or contains(text(),'There are no results')]")
	@CacheLookup
	public WebElement noResultsMessage;
	
	@FindBy(xpath = "//tbody//tr[2]//td//div//span[1]//a")
	@CacheLookup
	public WebElement HCMSyncProfilesJobinRow2;
	
	@FindBy(xpath = "//tbody//tr[3]//td//div//span[1]//a")
	@CacheLookup
	public WebElement HCMSyncProfilesJobinRow3;
	
	@FindBy(xpath = "//span[contains(text(),'Select your view')]")
	@CacheLookup
	public WebElement SPdetailsPageText;
	
	@FindBy(xpath = "//div[contains(text(),'Showing')]")
	@CacheLookup
	public WebElement showingJobResultsCount;
	
	@FindBy(xpath = "//span[text()='Filters']")
	@CacheLookup
	public WebElement filtersDropdownBtn;
	
	@FindBy(xpath = "//*[@class='accordion']")
	@CacheLookup
	public WebElement filterOptions;
	
	@FindBy(xpath = "//*[contains(@class,'sp-search-filter-expansion-panel')][1]//span//div")
	@CacheLookup
	public WebElement filterOption1;
	
	@FindBy(xpath = "//*[contains(@class,'sp-search-filter-expansion-panel')][2]//span//div")
	@CacheLookup
	public WebElement filterOption2;
	
	@FindBy(xpath = "//*[contains(@class,'sp-search-filter-expansion-panel')][3]//span//div")
	@CacheLookup
	public WebElement filterOption3;
	
	@FindBy(xpath = "//*[contains(@class,'sp-search-filter-expansion-panel')][3]//input")
	@CacheLookup
	public WebElement searchBarInFilterOption3;
	
	@FindBy(xpath = "//thcl-expansion-panel-header//div[text()=' KF Grade ']")
	@CacheLookup
	WebElement KFGradeFiltersDropdown;
	
	@FindBy(xpath = "//thcl-expansion-panel[contains(@class,'sp-search-filter-expansion-panel')][1]//kf-checkbox")
	List<WebElement> KFGradeAllCheckboxes;
	
	@FindBy(xpath = "//thcl-expansion-panel[contains(@class,'sp-search-filter-expansion-panel')][1]//div[contains(@class,'body-text')]")
	List<WebElement> KFGradeAllLabels;
	
	@FindBy(xpath = "(//thcl-expansion-panel[contains(@class,'sp-search-filter-expansion-panel')][1]//div[contains(@class,'body-text')])[4]")
	@CacheLookup
	WebElement KFGradeOption3;
	
	@FindBy(xpath = "(//thcl-expansion-panel[contains(@class,'sp-search-filter-expansion-panel')][1]//kf-checkbox)[3]")
	@CacheLookup
	WebElement KFGradeOption3Checkbox;
	
	@FindBy(xpath = "//div[contains(@class,'applied-filters')]//span//kf-icon")
	@CacheLookup
	WebElement closeAppliedFilter;
	
	@FindBy(xpath = "//thcl-expansion-panel-header//div[text()=' Levels ']")
	@CacheLookup
	WebElement LevelsFiltersDropdown;
	
	@FindBy(xpath = "//thcl-expansion-panel[contains(@class,'sp-search-filter-expansion-panel')][2]//kf-checkbox")
	List<WebElement> LevelsAllCheckboxes;
	
	@FindBy(xpath = "//thcl-expansion-panel[contains(@class,'sp-search-filter-expansion-panel')][2]//div[contains(@class,'body-text')]")
	List<WebElement> LevelsAllLabels;
	
	@FindBy(xpath = "(//thcl-expansion-panel[contains(@class,'sp-search-filter-expansion-panel')][2]//div[contains(@class,'body-text')])[3]")
	@CacheLookup
	WebElement LevelsSecondOption;
	
	@FindBy(xpath = "(//thcl-expansion-panel[contains(@class,'sp-search-filter-expansion-panel')][2]//kf-checkbox)[2]")
	@CacheLookup
	WebElement LevelsSecondOptionCheckbox;
	
	@FindBy(xpath = "//thcl-expansion-panel-header//div[text()=' Functions / Subfunctions ']")
	@CacheLookup
	WebElement FunctionsSubfunctionsFiltersDropdown;
	
	@FindBy(xpath = "//thcl-expansion-panel[contains(@class,'sp-search-filter-expansion-panel')][3]//thcl-expansion-panel//kf-checkbox")
	List<WebElement> FunctionsSubfunctionsAllCheckboxes;
	
	@FindBy(xpath = "//thcl-expansion-panel[contains(@class,'sp-search-filter-expansion-panel')][3]//thcl-expansion-panel-header//span[contains(@class,'text-break')]")
	List<WebElement> FunctionsSubfunctionsAllLabels;
	
	@FindBy(xpath = "//thcl-expansion-panel-header[@id='expansion-panel-header-2']//..//span[contains(text(),'Human')]//..//kf-checkbox")
	@CacheLookup
	WebElement HumanResourcesFunctionOptionCheckbox;
	
	@FindBy(xpath = "//thcl-expansion-panel-header//div[text()=' Profile Status ']")
	@CacheLookup
	WebElement ProfileStatusFiltersDropdown;
	
	@FindBy(xpath = "//thcl-expansion-panel-header//div[text()=' Profile Status ']/ancestor::thcl-expansion-panel//kf-checkbox")
	List<WebElement> ProfileStatusAllCheckboxes;
	
	@FindBy(xpath = "//thcl-expansion-panel-header//div[text()=' Profile Status ']/ancestor::thcl-expansion-panel//span[contains(@class,'wrapped') or contains(@class,'body-text')]")
	List<WebElement> ProfileStatusAllLabels;
	
	@FindBy(xpath = "//thcl-expansion-panel-header//div[text()=' Profile Status ']//..//div//span[contains(text(),'Active')]")
	@CacheLookup
	WebElement ProfileStatusFirstOption;
	
	@FindBy(xpath = "//thcl-expansion-panel-header//div[text()=' Profile Status ']//..//div//span[contains(text(),'Active')]//..//..//kf-checkbox")
	@CacheLookup
	WebElement ProfileStatusFirstOptionCheckbox;
	
	@FindBy(xpath = "//a[contains(text(),'Clear All')]")
	@CacheLookup
	WebElement clearAllFiltersBtn;
	
	@FindBy(xpath = "//thead//tr//div[@kf-sort-header='name']//div")
	@CacheLookup
	WebElement tableHeader1;
	
	@FindBy(xpath = "//thead//tr//div//div[text()=' Status ']")
	@CacheLookup
	WebElement tableHeader2;
	
	@FindBy(xpath = "//thead//tr//div//div[text()=' kf grade ']")
	@CacheLookup
	WebElement tableHeader3;
	
	@FindBy(xpath = "//thead//tr//div//div[text()=' Level ']")
	@CacheLookup
	WebElement tableHeader4;
	
	@FindBy(xpath = "//thead//tr//div//div[text()=' Function ']")
	@CacheLookup
	WebElement tableHeader5;
	
	@FindBy(xpath = "//thead//tr//div//div[text()=' Created By ']")
	@CacheLookup
	WebElement tableHeader6;
	
	@FindBy(xpath = "//thead//tr//div//div[text()=' Last Modified ']")
	@CacheLookup
	WebElement tableHeader7;
	
	@FindBy(xpath = "//thead//tr//div//div[text()=' Export status ']")
	@CacheLookup
	WebElement tableHeader8;
	
	@FindBy(xpath = "//button[contains(@class,'border-button')]")
	@CacheLookup
	WebElement downloadBtn;
	
	@FindBy(xpath = "//thead//tr//div//kf-checkbox")
	@CacheLookup
	WebElement tableHeaderCheckbox;
	
	@FindBy(xpath = "//tbody//tr[1]//div[1]//kf-checkbox")
	@CacheLookup
	WebElement profile1Checkbox;
	
	@FindBy(xpath = "//tbody//tr[2]//div[1]//kf-checkbox")
	@CacheLookup
	WebElement profile2Checkbox;
	
	@FindBy(xpath = "//tbody//tr[3]//div[1]//kf-checkbox")
	@CacheLookup
	WebElement profile3Checkbox;
	
	@FindBy(xpath = "//span[text()='XLS Format']//..")
	@CacheLookup
	WebElement XLSFormatBtn;
	
	@FindBy(xpath = "//span[text()='CSV Format']//..")
	@CacheLookup
	WebElement CSVFormatBtn;
	
	@FindBy(xpath = "//button[contains(@class,'custom-export')] | //*[contains(text(),'Sync with HCM')]")
	@CacheLookup
	WebElement SyncwithHCMBtn;
	
	@FindBy(xpath = "//div[@class='p-toast-detail']")
	@CacheLookup
	WebElement SyncwithHCMSuccessPopupText;
	
	@FindBy(xpath = "//button[contains(@class,'p-toast-icon-close')]")
	@CacheLookup
	WebElement SyncwithHCMSuccessPopupCloseBtn;
	
	@FindBy(xpath = "//span[contains(@class,'message')]")
	@CacheLookup
	WebElement SyncwithHCMWarningMsg;
	
	@FindBy(xpath = "//button[contains(@class,'close-btn')]")
	@CacheLookup
	WebElement SyncwithHCMWarningMsgCloseBtn;
	
	
	//METHODs
	public void user_is_on_architect_dashboard_page() {
		PerformanceUtils.waitForSpinnersToDisappear(driver);
		LOGGER.info("User is on Architect Dashboard Page");
		ExtentCucumberAdapter.addTestStepLog("User is on Architect Dashboard Page");
	}
	
	public void user_is_on_profile_manager_page() {
		try {
			// PERFORMANCE: Visibility check already ensures spinners are gone
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(PMHeader)).isDisplayed());
			String PMHeaderText = wait.until(ExpectedConditions.visibilityOf(PMHeader)).getText();
			LOGGER.info("User is on " + PMHeaderText + " page as expected");
			ExtentCucumberAdapter.addTestStepLog("User is on " + PMHeaderText + " page as expected");
	} catch (AssertionError e) {
		PageObjectHelper.handleError(LOGGER, "user_is_on_profile_manager_page",
			"Assertion failed - User is NOT on Profile Manager page", new Exception(e));
		throw e; // Re-throw to fail the test
	} catch (Exception e) {
		PageObjectHelper.handleError(LOGGER, "user_is_on_profile_manager_page",
			"Issue verifying user is on Profile Manager page", e);
		Assert.fail("Issue in verifying user is on Profile Manager page...Please Investigate!!!");
	}
	}
	
	public void click_on_menu_button() {
		// PERFORMANCE: Single comprehensive wait
		PerformanceUtils.waitForPageReady(driver, 3);
		try {
			utils.jsClick(driver, MenuBtn);
		} catch (Exception e) {
			utils.jsClick(driver, homeMenuBtn1);
		}
		LOGGER.info("Able to click on Menu Button");
		ExtentCucumberAdapter.addTestStepLog("Able to click on Menu Button");

	}

	public void click_on_profile_manager_button() {

		try {
			utils.jsClick(driver, PMBtn);
		} catch (Exception e) {

			wait.until(ExpectedConditions.elementToBeClickable(PMBtn)).click();
		}
		LOGGER.info("Able to click on Profile Manager Button");
		ExtentCucumberAdapter.addTestStepLog("Able to click on Profile Manager Button");
	}

	public void user_should_be_landed_to_pm_dashboard() {
		PerformanceUtils.waitForSpinnersToDisappear(driver);
//		Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(PMHeader)).isDisplayed());
//		String MainHeader = wait.until(ExpectedConditions.visibilityOf(PMHeader)).getText();
		LOGGER.info("User Successfully landed on the PROFILE MANAGER Dashboard Page");
		ExtentCucumberAdapter.addTestStepLog("User Successfully landed on the PROFILE MANAGER Dashboard Page");
	}
	
	public void click_on_hcm_sync_profiles_header_button() {
		try {
			try {
				wait.until(ExpectedConditions.elementToBeClickable(hcmSyncProfilesHeader)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", hcmSyncProfilesHeader);
				} catch (Exception s) {
					utils.jsClick(driver, hcmSyncProfilesHeader);
				}
			}
		LOGGER.info("Clicked on HCM Sync Profiles header in Profile Manager");
		ExtentCucumberAdapter.addTestStepLog("Clicked on HCM Sync Profiles header in Profile Manager");
		// PERFORMANCE: Single optimized wait - waitForPageReady already checks spinners
		PerformanceUtils.waitForPageReady(driver, 2);
	} catch (Exception e) {
		PageObjectHelper.handleError(LOGGER, "click_on_hcm_sync_profiles_header_button",
			"Issue in clicking HCM Sync Profiles header in Profile Manager", e);
		Assert.fail("Issue in clicking HCM Sync Profiles header in Profile Manager...Please Investigate!!!");
		}
	}
	
	public void user_should_be_navigated_to_hcm_sync_profiles_screen() {
		try {
			// PERFORMANCE: Visibility check already ensures spinners are gone
			wait.until(ExpectedConditions.visibilityOf(hcmSyncProfilesTitle)).isDisplayed();
			LOGGER.info("User should be Navigated to HCM Sync Profiles screen in Profile Manager");
			ExtentCucumberAdapter.addTestStepLog("User should be Navigated to HCM Sync Profiles screen in Profile Manager");
	} catch (Exception e) {
		LOGGER.error(" Issue navigating to HCM Sync Profiles screen - Method: user_should_be_navigated_to_hcm_sync_profiles_screen", e);
			e.printStackTrace();
			Assert.fail("Issue in Navigating to HCM Sync Profiles screen in Profile Manager...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in Navigating to HCM Sync Profiles screen in Profile Manager...Please Investigate!!!");
		}
	}
	
	public void verify_title_is_correctly_displaying_in_hcm_sync_profiles_tab() {
		try {
			String titleText = wait.until(ExpectedConditions.visibilityOf(hcmSyncProfilesTitle)).getText();
			Assert.assertEquals(titleText,"HCM Sync Profiles");
			LOGGER.info("Title is correctly displaying in HCM Sync Profiles screen in Profile Manager");
			ExtentCucumberAdapter.addTestStepLog("Title is correctly displaying in HCM Sync Profiles screen in Profile Manager");
		} catch (AssertionError e) {
			LOGGER.error(" Assertion failed - Title mismatch in HCM Sync Profiles screen - Method: verify_title_is_correctly_displaying_in_hcm_sync_profiles_tab", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying title in HCM Sync Profiles screen in Profile Manager...Please Investigate!!!");
			throw e; // Re-throw to fail the test
	} catch (Exception e) {
			LOGGER.error(" Issue verifying title - Method: verify_title_is_correctly_displaying_in_hcm_sync_profiles_tab", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying title in HCM Sync Profiles screen in Profile Manager...Please Investigate!!!");
			Assert.fail("Issue in Verifying title in HCM Sync Profiles screen in Profile Manager...Please Investigate!!!");
		}
	}
	
	public void verify_description_below_the_title_is_correctly_displaying_in_hcm_sync_profiles_tab() {
		try {
			String titleDesc = wait.until(ExpectedConditions.visibilityOf(hcmSyncProfilesTitleDesc)).getText();
			Assert.assertEquals(titleDesc,"Select a job profile to sync with HCM.");
			LOGGER.info("Title Description below the title is correctly displaying in HCM Sync Profiles screen in Profile Manager");
			ExtentCucumberAdapter.addTestStepLog("Title Description below the title is correctly displaying in HCM Sync Profiles screen in Profile Manager");
	} catch (Exception e) {
		LOGGER.error(" Issue verifying title description - Method: verify_description_below_the_title_is_correctly_displaying_in_hcm_sync_profiles_tab", e);
			e.printStackTrace();
			Assert.fail("Issue in Verifying title description in HCM Sync Profiles screen in Profile Manager...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying title description in HCM Sync Profiles screen in Profile Manager...Please Investigate!!!");
		}
	}
	
	public void verify_search_bar_text_box_is_clickable_in_hcm_sync_profiles_tab() {
		try {
			// ENHANCED FIX: Explicitly wait for blocking loader to disappear
			PerformanceUtils.waitForPageReady(driver, 15);
			
			// Additional explicit wait for blocking loader to be invisible
			try {
				WebDriverWait shortWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(5));
				shortWait.until(ExpectedConditions.invisibilityOfElementLocated(
					By.xpath("//div[contains(@class,'blocking-loader')]")));
				LOGGER.info("✅ Blocking loader disappeared");
			} catch (TimeoutException e) {
				LOGGER.warn("⚠️ Blocking loader check timed out (may not be present) - continuing...");
			}
			
			// Scroll search bar into view
			js.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});", hcmSyncProfilesSearchbar);
			
			// Small wait after scroll for stability
			Thread.sleep(500);
			
			// Now wait for search bar to be clickable and click
			wait.until(ExpectedConditions.elementToBeClickable(hcmSyncProfilesSearchbar)).click();
			LOGGER.info("Search bar text box is displayed and clickable in HCM Sync Profiles screen in Profile Manager");
			ExtentCucumberAdapter.addTestStepLog("Search bar text box is displayed and clickable in HCM Sync Profiles screen in Profile Manager");
	} catch (Exception e) {
		LOGGER.error(" Issue verifying search bar clickability - Method: verify_search_bar_text_box_is_clickable_in_hcm_sync_profiles_tab", e);
			e.printStackTrace();
			Assert.fail("Issue in Verifying Search bar is clickable or not in HCM Sync Profiles screen in Profile Manager...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying Search bar is clickable or not in HCM Sync Profiles screen in Profile Manager...Please Investigate!!!");
		}
	}
	
	public void verify_search_bar_placeholder_text_in_hcm_sync_profiles_tab() {
		try {
			String placeHolderText = wait.until(ExpectedConditions.visibilityOf(hcmSyncProfilesSearchbar)).getAttribute("placeholder");
			Assert.assertEquals(placeHolderText,"Search job profiles within your organization...");;
			LOGGER.info(placeHolderText + " is displaying as Placeholder Text in Search bar in HCM Sync Profiles screen in Profile Manager");
			ExtentCucumberAdapter.addTestStepLog(placeHolderText + " is displaying as Placeholder Text in Search bar in HCM Sync Profiles screen in Profile Manager");
	} catch (Exception e) {
		LOGGER.error(" Issue verifying search bar placeholder text - Method: verify_search_bar_placeholder_text_in_hcm_sync_profiles_tab", e);
			e.printStackTrace();
			Assert.fail("Issue in Verifying Search bar Placeholder Text in HCM Sync Profiles screen in Profile Manager...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying Search bar Placeholder Text in HCM Sync Profiles screen in Profile Manager...Please Investigate!!!");
		}
	}
	
	/**
	 * Enters job profile name in search bar with dynamic fallback retry logic.
	 * 
	 * Strategy:
	 * 1. Try each profile name from SEARCH_PROFILE_NAME_OPTIONS array
	 * 2. Check if search returns results (not "Showing 0 of X")
	 * 3. If results found, use that profile name and stop
	 * 4. If no results, try next profile name
	 * 5. If all profile names fail, use the last one and proceed
	 * 
	 * This ensures we never get 0 results when searching (unless all options exhausted).
	 */
	public void enter_job_profile_name_in_search_bar_in_hcm_sync_profiles_tab() {
		boolean foundResults = false;
		String selectedProfileName = SEARCH_PROFILE_NAME_OPTIONS[0]; // Default to first option
		
		try {
			LOGGER.info("========================================");
			LOGGER.info("DYNAMIC SEARCH WITH FALLBACK RETRY - HCM SYNC PROFILES");
			LOGGER.info("========================================");
			LOGGER.info("Attempting to find a profile name that returns results...");
			ExtentCucumberAdapter.addTestStepLog("Searching with dynamic profile name (with fallback retry until results found)...");
			
			int attemptNumber = 0;
			for (String profileName : SEARCH_PROFILE_NAME_OPTIONS) {
				try {
					attemptNumber++;
					LOGGER.info("🔍 Attempt #{}: Trying profile name: '{}'", attemptNumber, profileName);
					
					// Clear and enter profile name
					wait.until(ExpectedConditions.elementToBeClickable(hcmSyncProfilesSearchbar)).clear();
					wait.until(ExpectedConditions.elementToBeClickable(hcmSyncProfilesSearchbar)).sendKeys(profileName);
					
					// CRITICAL FIX: Press Enter to trigger search
					hcmSyncProfilesSearchbar.sendKeys(Keys.ENTER);
					
					// CRITICAL: Wait for spinners after search entry before page ready check
					PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
					
					// PERFORMANCE: Single comprehensive wait for search results
					PerformanceUtils.waitForPageReady(driver, 5);
					
					// Check if results were found
					String resultsCountText = "";
					try {
						resultsCountText = showingJobResultsCount.getText().trim();
						LOGGER.info("📊 Search results: {}", resultsCountText);
					} catch (Exception e) {
						LOGGER.info("⚠️  No profiles found with string '{}' (results element not found)", profileName);
						LOGGER.info("   Looking for profiles with another string...");
						continue; // Try next profile name
					}
					
					if (resultsCountText.contains("Showing") && !resultsCountText.startsWith("Showing 0")) {
						// Results found - now verify first result actually contains the search term
						try {
							WebDriverWait shortWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(3));
							String firstRowText = shortWait.until(ExpectedConditions.visibilityOf(HCMSyncProfilesJobinRow1)).getText();
							String firstProfileName = firstRowText.split("-", 2)[0].trim();
							
							LOGGER.info("📝 First result profile name: '{}'", firstProfileName);
							
							if (firstProfileName.toLowerCase().contains(profileName.toLowerCase())) {
								// Perfect match! First result contains the search term
								selectedProfileName = profileName;
								jobProfileName.set(profileName); // Update ThreadLocal variable for other methods to use
								foundResults = true;
								
								LOGGER.info("✅ Found matching result with profile name: '{}'", profileName);
								LOGGER.info("   First profile: '{}' contains search term", firstProfileName);
								LOGGER.info("   Results: {}", resultsCountText);
								ExtentCucumberAdapter.addTestStepLog("✅ Search successful with profile name '" + profileName + "' - " + resultsCountText);
								break; // Stop trying other profile names
							} else {
								LOGGER.info("⚠️  First result '{}' does NOT contain search term '{}'", firstProfileName, profileName);
								LOGGER.info("   Looking for profiles with another string...");
							}
						} catch (Exception e) {
							LOGGER.info("⚠️  Could not verify first result, trying next search term...");
						}
					} else {
						LOGGER.info("⚠️  No profiles found with string '{}' (0 results)", profileName);
						LOGGER.info("   Looking for profiles with another string...");
					}
					
				} catch (Exception e) {
					LOGGER.warn("❌ Error searching with '{}': {}", profileName, e.getMessage());
					LOGGER.info("   Trying next profile name...");
					// Continue to next profile name
				}
			}
			
			if (!foundResults) {
				// All profile names exhausted, use the last one
				LOGGER.warn("⚠️  All search profile names exhausted without finding results");
				LOGGER.warn("   Proceeding with last profile name: '{}'", selectedProfileName);
				ExtentCucumberAdapter.addTestStepLog("⚠️ No profile name returned results. Using: '" + selectedProfileName + "'");
			}
			
			LOGGER.info("========================================");
			LOGGER.info("✅ Final selected profile name: '{}'", jobProfileName.get());
			LOGGER.info("========================================");
			
		} catch (Exception e) {
			LOGGER.error("❌ Issue entering job profile name in search bar - Method: enter_job_profile_name_in_search_bar_in_hcm_sync_profiles_tab", e);
			e.printStackTrace();
			Assert.fail("Issue in entering Job Profile Name in Search bar in HCM Sync Profiles screen in Profile Manager...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in entering Job Profile Name in Search bar in HCM Sync Profiles screen in Profile Manager...Please Investigate!!!");
		}
	}
	
	public void user_should_verify_profile_name_matching_profile_is_displaying_in_organization_jobs_profile_list() {
	try {
		// Validate that job profile name is available
		if (jobProfileName.get() == null || jobProfileName.get().isEmpty()) {
			throw new IllegalStateException("Job profile name (jobProfileName) is not set. Ensure a profile was searched in a previous step.");
		}
		
		String searchedProfileName = jobProfileName.get();
		LOGGER.info("Verifying if profile name '{}' is displaying in HCM Sync Profiles list", searchedProfileName);
		
		// PERFORMANCE: Single comprehensive wait
		PerformanceUtils.waitForPageReady(driver, 5);
		
		// ENHANCED: First check if profile is found or "No SP" message is displayed
		boolean profileFound = false;
		String job1NameText = "";
		
		try {
			// Try to get the first row profile name with a shorter wait
			WebDriverWait shortWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(5));
			job1NameText = shortWait.until(ExpectedConditions.visibilityOf(HCMSyncProfilesJobinRow1)).getText();
			profileFound = true;
			LOGGER.info("Found profile in first row: '{}'", job1NameText);
		} catch (Exception e) {
			LOGGER.debug("No profile found in first row, checking for 'No SP' message...");
			profileFound = false;
		}
		
		if (profileFound) {
		// Profile exists - verify it matches search criteria
		String profileNameFromList = job1NameText.split("-", 2)[0].trim();
		boolean matchesSearch = profileNameFromList.toLowerCase().contains(searchedProfileName.toLowerCase());
			
			if (matchesSearch) {
				LOGGER.info("Searched String present in Job Profile with name: '{}' is displaying in HCM Sync Profiles screen in PM as expected", profileNameFromList);
				ExtentCucumberAdapter.addTestStepLog("Searched String present in Job Profile with name: " + profileNameFromList + " is displaying in HCM Sync Profiles screen in PM as expected");
			} else {
			String errorMsg = String.format(
				"Profile found but does not match search criteria. " +
				"Searched for: '%s', Found profile: '%s', " +
				"Profile does not contain searched text.",
				searchedProfileName,
				profileNameFromList
			);
				LOGGER.error(errorMsg);
				Assert.fail(errorMsg);
			}
		} else {
			// No profile in first row - check if "No SP" message is displayed
			try {
			wait.until(ExpectedConditions.visibilityOf(NoSPMsg)).isDisplayed();
			LOGGER.info("No Success Profile Found with searched String: '{}'", searchedProfileName);
			ExtentCucumberAdapter.addTestStepLog("No Success Profile Found with searched String: " + searchedProfileName);
				
				// Clear the search bar
				Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(hcmSyncProfilesSearchbar)).isDisplayed());
				Actions actions = new Actions(driver);

				actions.click(hcmSyncProfilesSearchbar)
				    .keyDown(Keys.CONTROL)
				    .sendKeys("a")
				    .keyUp(Keys.CONTROL)
				    .sendKeys(Keys.BACK_SPACE)
				    .build()
				    .perform();
				LOGGER.info("Cleared Search bar in HCM Sync Profiles screen in PM");
				ExtentCucumberAdapter.addTestStepLog("Cleared Search bar in HCM Sync Profiles screen in PM");
			} catch(Exception d) {
				// Neither profile nor "No SP" message found
			String errorMsg = String.format(
				"Failed to verify profile name matching. " +
				"Searched for: '%s', " +
				"Neither matching profile nor 'No Success Profile' message was found. " +
				"Error type: %s, Error message: %s",
				searchedProfileName,
				d.getClass().getSimpleName(),
				d.getMessage()
			);
				
				LOGGER.error(errorMsg, d);
				d.printStackTrace();
				Assert.fail(errorMsg);
				ExtentCucumberAdapter.addTestStepLog("FAILURE: " + errorMsg);
			}
		}	
	} catch (Exception outerException) {
		// Outer catch for any unexpected errors
		// Safely get profile name for error message
		String profileNameForError = "N/A";
		try {
			profileNameForError = (jobProfileName.get() != null) ? jobProfileName.get() : "N/A";
		} catch (Exception e) {
			// Ignore - use default "N/A"
		}
		
	String errorDetails = String.format(
		"Unexpected error while verifying profile name matching. " +
		"Searched for: '%s', Error type: %s, Error message: %s",
		profileNameForError,
		outerException.getClass().getSimpleName(),
		outerException.getMessage()
	);
		
		LOGGER.error(errorDetails, outerException);
		outerException.printStackTrace();
		Assert.fail(errorDetails);
		ExtentCucumberAdapter.addTestStepLog("FAILURE: " + errorDetails);
	}
	}
	
	public void click_on_name_matching_profile_in_hcm_sync_profiles_tab() {
		try {
			String job1NameText = wait.until(ExpectedConditions.visibilityOf(HCMSyncProfilesJobinRow1)).getText();
			wait.until(ExpectedConditions.elementToBeClickable(HCMSyncProfilesJobinRow1)).click();
			LOGGER.info("Clicked on profile with name : " + job1NameText.split("-", 2)[0].trim() + " in Row1 in HCM Sync Profiles screen in PM");
			ExtentCucumberAdapter.addTestStepLog("Clicked on profile with name : " + job1NameText.split("-", 2)[0].trim() + " in Row1 in HCM Sync Profiles screen in PM");
			PerformanceUtils.waitForSpinnersToDisappear(driver);
	} catch (Exception e) {
		LOGGER.error(" Issue clicking on name matching profile - Method: click_on_name_matching_profile_in_hcm_sync_profiles_tab", e);
			e.printStackTrace();
			Assert.fail("Issue in Clicking on Searched name Matching profile in Row1 in HCM Sync Profiles screen in PM...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in Clicking on Searched name Matching profile in Row1 in HCM Sync Profiles screen in PM...Please Investigate!!!");
		}	
	}
	
	public void user_should_be_navigated_to_sp_details_page_on_click_of_matching_profile() {
		try {
			// PERFORMANCE: Visibility check already ensures spinners are gone
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(SPdetailsPageText)).isDisplayed());
			LOGGER.info("User navigated to SP details page as expected on click of Matching Profile Job name in HCM Sync Profiles screen in PM....");
			ExtentCucumberAdapter.addTestStepLog("User navigated to SP details page as expected on click of Matching Profile Job name in HCM Sync Profiles screen in PM....");
	} catch (Exception e) {
		LOGGER.error(" Issue navigating to SP details page - Method: user_should_be_navigated_to_sp_details_page_on_click_of_matching_profile", e);
			e.printStackTrace();
			Assert.fail("Issue in Navigating to SP details Page on click of Matching Profile Job name in HCM Sync Profiles screen in PM...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in Navigating to SP details Page on click of Matching Profile Job name in HCM Sync Profiles screen in PM...Please Investigate!!!");
		}		
	}
	
	public void clear_search_bar_in_hcm_sync_profiles_tab() {
		try {
			// PARALLEL EXECUTION FIX: Extended wait for page readiness
			PerformanceUtils.waitForPageReady(driver, 3);
			
			// PARALLEL EXECUTION FIX: Wait for element to be present in DOM (avoid stale element)
			WebDriverWait extendedWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(20));
			
			WebElement searchBar = extendedWait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//input[@type='search']")));
			
			// Wait for element visibility
			searchBar = extendedWait.until(ExpectedConditions.visibilityOf(searchBar));
			
			Assert.assertTrue(searchBar.isDisplayed(), "Search bar not visible in HCM Sync Profiles screen");
			
			// HEADLESS FIX: Use 'auto' instead of 'smooth' for instant scroll in headless mode
			js.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});", searchBar);
			
			// PARALLEL EXECUTION FIX: Additional wait for scroll and DOM stability
			try {
				Thread.sleep(1000); // Critical for parallel execution stability
			} catch (InterruptedException ie) {
				Thread.currentThread().interrupt();
			}
			
			// HEADLESS FIX: Longer wait for page to stabilize after scroll
			PerformanceUtils.waitForPageReady(driver, 2);
			
			// PARALLEL EXECUTION FIX: Re-fetch element to ensure it's fresh and clickable
			searchBar = extendedWait.until(ExpectedConditions.elementToBeClickable(
				By.xpath("//input[@type='search']")));
			
			// Try clicking with multiple fallback options
			try {
				searchBar.click();
			} catch (Exception clickException) {
				try {
					js.executeScript("arguments[0].click();", searchBar);
				} catch (Exception jsClickException) {
					utils.jsClick(driver, searchBar);
				}
			}
			
			// HEADLESS FIX: Use JavaScript to clear instead of keyboard combinations
			// This is more reliable across headless and windowed modes
			try {
				// First try WebDriver clear (fastest)
				searchBar.clear();
			} catch(Exception clearException) {
				// Fallback to JavaScript clear (most reliable for headless)
				js.executeScript("arguments[0].value = '';", searchBar);
				js.executeScript("arguments[0].dispatchEvent(new Event('input', { bubbles: true }));", searchBar);
				js.executeScript("arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", searchBar);
			}
			
			// CRITICAL HEADLESS FIX: Wait for spinners after clearing search - triggers data reload
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			
			// PERFORMANCE: Single comprehensive wait after clearing
			PerformanceUtils.waitForPageReady(driver, 5);
			
			LOGGER.info("Cleared Search bar in HCM Sync Profiles screen in PM");
			ExtentCucumberAdapter.addTestStepLog("Cleared Search bar in HCM Sync Profiles screen in PM");
		} catch (Exception e) {
			LOGGER.error("Issue clearing search bar - Method: clear_search_bar_in_hcm_sync_profiles_tab", e);
			e.printStackTrace();
			Assert.fail("Issue in clearing search bar in HCM Sync Profiles screen in PM");
			ExtentCucumberAdapter.addTestStepLog("Issue in clearing search bar in HCM Sync Profiles screen in PM");
		}
	}
	
	public void verify_job_profiles_count_is_displaying_on_the_page_in_hcm_sync_profiles_tab() {
		try {
			// PARALLEL EXECUTION FIX: Extended wait for page readiness
			PerformanceUtils.waitForPageReady(driver, 5);
			
			// PARALLEL EXECUTION FIX: Use dynamic element location with retry for stale elements
			String resultsCountText = "";
			int retryAttempts = 0;
			int maxRetries = 5;
			
			while (retryAttempts < maxRetries) {
				try {
					// Get fresh element on each attempt to avoid stale reference
					WebElement resultsCountElement = driver.findElement(
						By.xpath("//div[contains(text(),'Showing')]"));
					resultsCountText = resultsCountElement.getText().trim();
					
					// Break if we got valid text
					if (!resultsCountText.isEmpty()) {
						break;
					}
					
					Thread.sleep(500);
					retryAttempts++;
				} catch (org.openqa.selenium.StaleElementReferenceException e) {
					LOGGER.warn("Stale element on attempt " + (retryAttempts + 1) + ", retrying...");
					retryAttempts++;
					try {
						Thread.sleep(500);
					} catch (InterruptedException ie) {
						Thread.currentThread().interrupt();
					}
				}
			}
			
			if (resultsCountText.isEmpty()) {
				throw new Exception("Could not retrieve results count text after " + maxRetries + " retries");
			}
			
			intialResultsCount.set(resultsCountText);
			LOGGER.info("Initially " + resultsCountText + " on the page in HCM Sync Profiles screen in PM");
			ExtentCucumberAdapter.addTestStepLog("Initially " + resultsCountText + " on the page in HCM Sync Profiles screen in PM");
		} catch (Exception e) {
			LOGGER.error(" Issue verifying job profiles count - Method: verify_job_profiles_count_is_displaying_on_the_page_in_hcm_sync_profiles_tab", e);
			e.printStackTrace();
			Assert.fail("Issue in verifying job profiles results count in on the page in HCM Sync Profiles screen in PM...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying job profiles results count in on the page in HCM Sync Profiles screen in PM...Please Investigate!!!");
		} 
	}
	
	public void scroll_page_to_view_more_job_profiles_in_hcm_sync_profiles_tab() {
		try {
			headlessActions.scrollToBottom();
			// CRITICAL: Wait for spinners after scroll before page ready check
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			// PERFORMANCE: Single comprehensive wait after scroll
			PerformanceUtils.waitForPageReady(driver, 5);
			LOGGER.info("Scrolled page down to view more job profiles in HCM Sync Profiles screen in PM");
			ExtentCucumberAdapter.addTestStepLog("Scrolled page down to view more job profiles in HCM Sync Profiles screen in PM");
	} catch (Exception e) {
		LOGGER.error(" Issue scrolling page - Method: scroll_page_to_view_more_job_profiles_in_hcm_sync_profiles_tab", e);
			e.printStackTrace();
			Assert.fail("Issue in scrolling page down to view more job profiles in HCM Sync Profiles screen in PM...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in scrolling page down to view more job profiles in in HCM Sync Profiles screen in PM...Please Investigate!!!");
		}
	}
	
	public void user_should_verify_count_of_job_profiles_is_correctly_showing_on_top_of_job_profiles_listing_table_in_hcm_sync_profiles_tab() {
		try {
			js.executeScript("arguments[0].scrollIntoView(true);", hcmSyncProfilesTitle);
			
			// CRITICAL: Wait for spinners to disappear first after scrolling
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			
			// PARALLEL EXECUTION FIX: Extended wait for page readiness after scrolling
			PerformanceUtils.waitForPageReady(driver, 5);
			
			// PARALLEL EXECUTION FIX: Wait for the results count text to actually CHANGE (not just be present)
			// This prevents reading stale/cached count text and is critical for parallel execution
			String resultsCountText1 = "";
			int retryAttempts = 0;
			int maxRetries = 15; // Increased for parallel execution - lazy loading can be slower
			long startTime = System.currentTimeMillis();
			String initialCount = intialResultsCount.get();
			
			LOGGER.info("Waiting for results count to change from initial: " + initialCount);
			
			while (retryAttempts < maxRetries) {
				try {
					// PARALLEL EXECUTION FIX: Get fresh element on each attempt
					WebElement resultsElement = driver.findElement(
						By.xpath("//div[contains(text(),'Showing')]"));
					String currentText = resultsElement.getText().trim();
					
					// CRITICAL: Check if text has CHANGED from initial count (scrolling loaded more profiles)
					if (!currentText.isEmpty() && !currentText.equals(initialCount)) {
						resultsCountText1 = currentText;
						long elapsedTime = System.currentTimeMillis() - startTime;
						LOGGER.info("Results count updated from '" + initialCount + "' to '" + currentText + "' after " + elapsedTime + "ms");
						break; // Success - count has updated
					}
					
					// If text hasn't changed yet, wait before retrying
					if (retryAttempts == 0) {
						Thread.sleep(1000); // Initial wait - give page time to start updating
					} else if (retryAttempts < 5) {
						Thread.sleep(500); // Medium wait for early attempts
					} else {
						Thread.sleep(300); // Shorter wait for later attempts
					}
					
					retryAttempts++;
					
				} catch (org.openqa.selenium.StaleElementReferenceException e) {
					LOGGER.warn("Stale element on attempt " + (retryAttempts + 1) + ", retrying...");
					retryAttempts++;
					try {
						Thread.sleep(300);
					} catch (InterruptedException ie) {
						Thread.currentThread().interrupt();
					}
				} catch (org.openqa.selenium.NoSuchElementException e) {
					LOGGER.warn("Element not found on attempt " + (retryAttempts + 1) + ", retrying...");
					retryAttempts++;
					try {
						Thread.sleep(300);
					} catch (InterruptedException ie) {
						Thread.currentThread().interrupt();
					}
				}
			}
			
			long totalElapsedTime = System.currentTimeMillis() - startTime;
			
			// Ensure we got an updated count
			if (resultsCountText1.isEmpty() || resultsCountText1.equals(initialCount)) {
				throw new Exception("Results count did not update after scrolling. Initial: '" + initialCount + 
					"', Current: '" + resultsCountText1 + "' (waited " + totalElapsedTime + "ms, " + retryAttempts + " attempts)");
			}
			
			updatedResultsCount.set(resultsCountText1);
			
			// Final assertion to confirm counts are different
			Assert.assertNotEquals(initialCount, resultsCountText1, 
				"Results count should have changed after scrolling. Initial: " + initialCount + ", Updated: " + resultsCountText1);
			
			LOGGER.info("Success: Profiles Results count updated from '" + initialCount + "' to '" + resultsCountText1 + "' as expected in HCM Sync Profiles screen in PM");
			ExtentCucumberAdapter.addTestStepLog("Success Profiles Results count updated and Now " + resultsCountText1 + " as expected in HCM Sync Profiles screen in PM");
			
		} catch (Exception e) {
			LOGGER.error(" Issue verifying profiles count after scrolling - Method: user_should_verify_count_of_job_profiles_is_correctly_showing_on_top_of_job_profiles_listing_table_in_hcm_sync_profiles_tab", e);
			e.printStackTrace();
			Assert.fail("Issue in verifying success profiles results count after scrolling page down in HCM Sync Profiles screen in PM...Please Investigate!!! Error: " + e.getMessage());
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying success profiles results count after scrolling page down in in HCM Sync Profiles screen in PM...Please Investigate!!!");
		}		
	}
	
	public void user_is_in_hcm_sync_profiles_screen() {
		LOGGER.info("User is in HCM Sync Profiles screen in PM.....");
		ExtentCucumberAdapter.addTestStepLog("User is in HCM Sync Profiles screen in PM.....");
	}
	
	public void click_on_filters_dropdown_button_in_hcm_sync_profiles_tab() {
		try {
			js.executeScript("window.scrollTo(0, 0);"); // Instant scroll to top
			
			// PERFORMANCE: Ensure page is ready before interacting
			PerformanceUtils.waitForPageReady(driver, 5);
			
			// Wait for filters button to be clickable
			WebElement filtersBtn = wait.until(ExpectedConditions.elementToBeClickable(filtersDropdownBtn));
			
			// Check if dropdown is already open by checking visibility of filterOptions
			boolean isDropdownOpen = false;
			try {
				isDropdownOpen = filterOptions.isDisplayed();
			} catch (Exception ex) {
				// Dropdown is not open, which is expected
				isDropdownOpen = false;
			}
			
			if (isDropdownOpen) {
				LOGGER.info("Filters dropdown is already open in HCM Sync Profiles screen - skipping click");
				ExtentCucumberAdapter.addTestStepLog("Filters dropdown is already open in HCM Sync Profiles screen");
		} else {
			// Dropdown is closed, so open it - filtersBtn is already clickable from line 846
			try {
				wait.until(ExpectedConditions.elementToBeClickable(filtersBtn)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", filtersBtn);
				} catch (Exception s) {
					utils.jsClick(driver, filtersBtn);
				}
			}
			LOGGER.info("Clicked on filters dropdown button in HCM Sync Profiles screen in PM...");
			ExtentCucumberAdapter.addTestStepLog("Clicked on filters dropdown button in HCM Sync Profiles screen in PM...");
				
				// PERFORMANCE: Wait for dropdown to be visible with shorter timeout
				WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(10));
				try {
					shortWait.until(ExpectedConditions.visibilityOf(filterOptions));
					LOGGER.info("Filters dropdown opened successfully");
				} catch (TimeoutException te) {
					// If accordion doesn't appear, check if we're on the right page
					String currentUrl = driver.getCurrentUrl();
					LOGGER.error("Filters dropdown did not appear after clicking. Current URL: {}", currentUrl);
					throw new Exception("Filters dropdown (accordion) did not appear after 10 seconds. " +
							"This might indicate the page is not in the expected state. Current URL: " + currentUrl);
				}
			}
	} catch (Exception e) {
		LOGGER.error(" Issue clicking filters dropdown - Method: click_on_filters_dropdown_button_in_hcm_sync_profiles_tab", e);
			e.printStackTrace();
			Assert.fail("Issue in clicking filters dropdown button in HCM Sync Profiles screen in PM...Please investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in clicking filters dropdown button in HCM Sync Profiles screen in PM...Please Investigate!!!");
		}   
	}
	
	public void verify_options_available_inside_filters_dropdown_in_hcm_sync_profiles_tab() {
		long startTime = System.currentTimeMillis();
		int maxRetries = 3;
		
		try {
			LOGGER.info("═══════════════════════════════════════════════════════════");
			LOGGER.info("🔍 DEBUG: Verify Filter Options in HCM Sync Profiles");
			LOGGER.info("═══════════════════════════════════════════════════════════");
			LOGGER.info("🧵 Thread: " + Thread.currentThread().getName() + " (ID: " + Thread.currentThread().getId() + ")");
			
			// PERFORMANCE: Wait for dropdown to be visible
			long stepStart = System.currentTimeMillis();
			wait.until(ExpectedConditions.visibilityOf(filterOptions));
			LOGGER.info("✅ Filter dropdown visible after " + (System.currentTimeMillis() - stepStart) + "ms");
			
			// Wait for spinners to ensure page is stable
			stepStart = System.currentTimeMillis();
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			long spinnerWait = System.currentTimeMillis() - stepStart;
			if (spinnerWait > 1000) {
				LOGGER.info("⏱️  Spinner wait: " + spinnerWait + "ms");
			}
			
			// PARALLEL EXECUTION FIX: Verify filter options with retry mechanism for stale elements
			String filterOption1Text = "";
			String filterOption2Text = "";
			String filterOption3Text = "";
			
			for (int attempt = 1; attempt <= maxRetries; attempt++) {
				try {
					LOGGER.info("🔄 Attempt " + attempt + " to read filter options...");
					
					// Use visibilityOfElementLocated instead of visibilityOf to avoid stale references
					filterOption1Text = wait.until(ExpectedConditions.visibilityOfElementLocated(
						By.xpath("//*[contains(@class,'sp-search-filter-expansion-panel')][1]//span//div"))).getText();
					LOGGER.info("   - Option 1: '" + filterOption1Text + "'");
					
					filterOption2Text = wait.until(ExpectedConditions.visibilityOfElementLocated(
						By.xpath("//*[contains(@class,'sp-search-filter-expansion-panel')][2]//span//div"))).getText();
					LOGGER.info("   - Option 2: '" + filterOption2Text + "'");
					
					filterOption3Text = wait.until(ExpectedConditions.visibilityOfElementLocated(
						By.xpath("//*[contains(@class,'sp-search-filter-expansion-panel')][3]//span//div"))).getText();
					LOGGER.info("   - Option 3: '" + filterOption3Text + "'");
					
					// If we got here, all reads were successful
					LOGGER.info("✅ Successfully read all filter options on attempt " + attempt);
					break;
					
				} catch (org.openqa.selenium.StaleElementReferenceException e) {
					if (attempt < maxRetries) {
						LOGGER.warn("⚠️  Stale element on attempt " + attempt + ", retrying...");
						Thread.sleep(300);
					} else {
						LOGGER.error("❌ Failed after " + maxRetries + " attempts due to stale elements");
						throw e;
					}
				}
			}
			
			// Verify the text values
			Assert.assertEquals(filterOption1Text, "KF Grade", "Option 1 should be 'KF Grade'");
			Assert.assertEquals(filterOption2Text, "Levels", "Option 2 should be 'Levels'");
			Assert.assertEquals(filterOption3Text, "Functions / Subfunctions", "Option 3 should be 'Functions / Subfunctions'");
			
			LOGGER.info("✅ Filter options verified successfully");
			ExtentCucumberAdapter.addTestStepLog("Options inside Filters dropdown verified successfully in HCM Sync Profiles screen in PM");
			
		} catch (Exception e) {
			long totalDuration = System.currentTimeMillis() - startTime;
			LOGGER.error("═══════════════════════════════════════════════════════════");
			LOGGER.error("❌ Filter Options Verification FAILED after " + totalDuration + "ms");
			LOGGER.error("🧵 Thread: " + Thread.currentThread().getName());
			LOGGER.error("💥 Error: " + e.getMessage());
			LOGGER.error("═══════════════════════════════════════════════════════════");
			e.printStackTrace();
			Assert.fail("Issue in verifying Options inside Filter dropdown in HCM Sync Profiles screen in PM....Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying Options inside Filter dropdown in HCM Sync Profiles screen in PM....Please Investigate!!!");
		}
		
		// Verify Functions/Subfunctions search bar
		try {
			LOGGER.info("🔍 Verifying Functions/Subfunctions search bar...");
			
			// PARALLEL EXECUTION FIX: Click with retry for stale elements
			for (int attempt = 1; attempt <= maxRetries; attempt++) {
				try {
					long stepStart = System.currentTimeMillis();
					
					// Use elementToBeClickable with locator instead of element
					WebElement option3ToClick = wait.until(ExpectedConditions.elementToBeClickable(
						By.xpath("//*[contains(@class,'sp-search-filter-expansion-panel')][3]//span//div")));
					
					try {
						option3ToClick.click();
						LOGGER.info("✅ Clicked option 3 using WebDriver click in " + (System.currentTimeMillis() - stepStart) + "ms");
					} catch (Exception e) {
						LOGGER.warn("⚠️  WebDriver click failed, trying JavaScript...");
						try {
							js.executeScript("arguments[0].click();", option3ToClick);
							LOGGER.info("✅ Clicked option 3 using JavaScript click");
						} catch (Exception s) {
							LOGGER.warn("⚠️  JavaScript click failed, trying utility click...");
							utils.jsClick(driver, option3ToClick);
							LOGGER.info("✅ Clicked option 3 using utility click");
						}
					}
					
					// If we got here, click was successful
					break;
					
				} catch (org.openqa.selenium.StaleElementReferenceException e) {
					if (attempt < maxRetries) {
						LOGGER.warn("⚠️  Stale element on click attempt " + attempt + ", retrying...");
						Thread.sleep(300);
					} else {
						throw e;
					}
				}
			}
			
			// PARALLEL EXECUTION FIX: Verify search bar with retry
			for (int attempt = 1; attempt <= maxRetries; attempt++) {
				try {
					long stepStart = System.currentTimeMillis();
					
					WebElement searchBar = wait.until(ExpectedConditions.visibilityOfElementLocated(
						By.xpath("//*[contains(@class,'sp-search-filter-expansion-panel')][3]//input")));
					searchBar.click();
					
					LOGGER.info("✅ Search bar verified and clicked in " + (System.currentTimeMillis() - stepStart) + "ms");
					break;
					
				} catch (org.openqa.selenium.StaleElementReferenceException e) {
					if (attempt < maxRetries) {
						LOGGER.warn("⚠️  Stale element on search bar attempt " + attempt + ", retrying...");
						Thread.sleep(300);
					} else {
						throw e;
					}
				}
			}
			
			LOGGER.info("Search bar inside Functions / Subfunctions filter option is available and is clickable in HCM Sync Profiles screen in PM...");
			ExtentCucumberAdapter.addTestStepLog("Search bar inside Functions / Subfunctions filter option is available and is clickable in HCM Sync Profiles screen in PM...");
			
		} catch (Exception e) {
			long totalDuration = System.currentTimeMillis() - startTime;
			LOGGER.error("═══════════════════════════════════════════════════════════");
			LOGGER.error("❌ Search Bar Verification FAILED after " + totalDuration + "ms");
			LOGGER.error("🧵 Thread: " + Thread.currentThread().getName());
			LOGGER.error("💥 Error: " + e.getMessage());
			LOGGER.error("═══════════════════════════════════════════════════════════");
			e.printStackTrace();
			Assert.fail("Issue in verifying Search bar inside Functions / Subfunctions filter option in HCM Sync Profiles screen in PM...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying Search bar inside Functions / Subfunctions filter option in HCM Sync Profiles screen in PM...Please Investigate!!!");
		}
		
		// Close the dropdown after verification to ensure clean state for next scenario
		try {
			LOGGER.info("🔍 Closing filters dropdown...");
			
			// PARALLEL EXECUTION FIX: Check visibility with retry
			for (int attempt = 1; attempt <= maxRetries; attempt++) {
				try {
					WebElement filterOptionsElement = driver.findElement(By.xpath("//*[@class='accordion']"));
					if (filterOptionsElement.isDisplayed()) {
						utils.jsClick(driver, filtersDropdownBtn);
						wait.until(ExpectedConditions.invisibilityOf(filterOptionsElement));
						LOGGER.info("✅ Closed Filters dropdown after verification");
						ExtentCucumberAdapter.addTestStepLog("Closed Filters dropdown after verification");
					}
					break;
					
				} catch (org.openqa.selenium.StaleElementReferenceException e) {
					if (attempt < maxRetries) {
						LOGGER.warn("⚠️  Stale element on close attempt " + attempt + ", retrying...");
						Thread.sleep(300);
					} else {
						LOGGER.warn("⚠️  Could not close filters dropdown after " + maxRetries + " attempts: " + e.getMessage());
					}
				}
			}
			
			long totalDuration = System.currentTimeMillis() - startTime;
			LOGGER.info("═══════════════════════════════════════════════════════════");
			LOGGER.info("✅ Filter Options Verification COMPLETED in " + totalDuration + "ms");
			LOGGER.info("═══════════════════════════════════════════════════════════");
			
		} catch (Exception e) {
			LOGGER.warn("⚠️  Could not close filters dropdown after verification: " + e.getMessage());
		}
	}
	
	public void apply_kf_grade_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab() {
		try {
			try {
				PerformanceUtils.waitForElement(driver, KFGradeFiltersDropdown);
				PerformanceUtils.waitForElement(driver, driver.findElement(By.xpath("//div")));
				wait.until(ExpectedConditions.visibilityOf(KFGradeFiltersDropdown)).isDisplayed();
				try {
					wait.until(ExpectedConditions.elementToBeClickable(KFGradeFiltersDropdown)).click();
				} catch (Exception e) {
					try {
						js.executeScript("arguments[0].click();", KFGradeFiltersDropdown);
					} catch (Exception s) {
						utils.jsClick(driver, KFGradeFiltersDropdown);
					}
				}
				LOGGER.info("Clicked on KF Grade dropdown in Filters in HCM Sync Profiles screen in PM...");
				ExtentCucumberAdapter.addTestStepLog("Clicked on KF Grade dropdown in Filters in HCM Sync Profiles screen in PM...");
	} catch (Exception e) {
		LOGGER.error("Issue clicking KF Grade dropdown - Method: apply_kf_grade_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab", e);
				e.printStackTrace();
				Assert.fail("Issue in clicking KF Grade dropdown in Filters in HCM Sync Profiles screen in PM...Please Investigate!!!");
				ExtentCucumberAdapter.addTestStepLog("Issue in clicking KF Grade dropdown in Filters in HCM Sync Profiles screen in PM...Please Investigate!!!");
			}
			
		try {
		// DYNAMIC XPATH: Find all available KF Grade options using updated class-based XPATH
		PerformanceUtils.waitForPageReady(driver, 2);
		
		if (KFGradeAllCheckboxes.isEmpty() || KFGradeAllLabels.isEmpty()) {
			LOGGER.error(" No KF Grade options found after expanding dropdown");
			throw new Exception("No KF Grade filter options found");
		}
		
		// Get the first checkbox and its corresponding label
		WebElement firstCheckbox = KFGradeAllCheckboxes.get(0);
		String kfGradeValue = KFGradeAllLabels.get(0).getText().trim();
		
		LOGGER.info("Found KF Grade option: " + kfGradeValue);
		
		// Scroll to element and click
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", firstCheckbox);
		Thread.sleep(300);
		
		try {
			wait.until(ExpectedConditions.elementToBeClickable(firstCheckbox)).click();
			LOGGER.info(" Clicked KF Grade option using standard click");
		} catch (Exception e) {
			LOGGER.warn("Standard click failed, trying JS click...");
			js.executeScript("arguments[0].click();", firstCheckbox);
			LOGGER.info(" Clicked KF Grade option using JS click");
		}
		
		Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(closeAppliedFilter)).isDisplayed());
		LOGGER.info("Selected KF Grade Value : " + kfGradeValue + " from Filters dropdown in HCM Sync Profiles screen in PM....");
		ExtentCucumberAdapter.addTestStepLog("Selected KF Grade Value : " + kfGradeValue + " from Filters dropdown in HCM Sync Profiles screen in PM....");
	} catch (Exception e) {
				LOGGER.error(" Issue selecting KF Grade option - Method: apply_kf_grade_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab", e);
				e.printStackTrace();
				Assert.fail("Issue in selecting a option from KF Grade dropdown in Filters in HCM Sync Profiles screen in PM...Please Investigate!!!");
				ExtentCucumberAdapter.addTestStepLog("Issue in selecting a option from KF Grade dropdown in Filters in HCM Sync Profiles screen in PM...Please Investigate!!!");
			}
			
			try { 
							// PERFORMANCE: Removed Thread.sleep(2000) - scrolling doesn't need fixed delay
			js.executeScript("window.scrollTo(0, 0);"); // Scroll to top (headless-compatible)
				try {
					wait.until(ExpectedConditions.elementToBeClickable(hcmSyncProfilesHeader)).click();
				} catch (Exception e) {
					try {
						js.executeScript("arguments[0].click();", hcmSyncProfilesHeader);
					} catch (Exception s) {
						utils.jsClick(driver, hcmSyncProfilesHeader);
					}
				}
				Assert.assertTrue(wait.until(ExpectedConditions.invisibilityOf(filterOptions)));
				LOGGER.info("Filters dropdown closed successfully in HCM Sync Profiles screen in PM...");
				ExtentCucumberAdapter.addTestStepLog("Filters dropdown closed successfully in HCM Sync Profiles screen in PM...");
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail("Issue in closing filters dropdown in HCM Sync Profiles screen in PM...Please Investigate!!!");
				ExtentCucumberAdapter.addTestStepLog("Issue in closing filters dropdown in HCM Sync Profiles screen in PM...Please Investigate!!!");
			}
			
		try {
			// PERFORMANCE: Single comprehensive wait after closing dropdown
			PerformanceUtils.waitForPageReady(driver, 5);
			
			// Check if there are no results after applying the filter
			boolean noResults = false;
			try {
				if (noResultsMessage.isDisplayed() || NoSPMsg.isDisplayed()) {
					noResults = true;
				}
			} catch (Exception e) {
				// Elements not found, meaning there are results
				noResults = false;
			}
			
			if (noResults) {
				LOGGER.warn(" NO RESULTS FOUND after applying KF Grade filter - This is acceptable, the selected filter value returned 0 results");
				ExtentCucumberAdapter.addTestStepLog(" NO RESULTS - The applied KF Grade filter returned 0 results. This is an expected scenario.");
				try {
					String countText = showingJobResultsCount.getText();
					LOGGER.info("Results count shows: " + countText);
					ExtentCucumberAdapter.addTestStepLog("Results count shows: " + countText);
				} catch (Exception ex) {
					LOGGER.info("No results count displayed - 0 profiles match the filter");
				}
			} else {
				// There are results, verify count changed
				PerformanceUtils.waitForElement(driver, showingJobResultsCount);
			String resultsCountText2 = wait.until(ExpectedConditions.visibilityOf(showingJobResultsCount)).getText();
			Assert.assertNotEquals(updatedResultsCount.get(), resultsCountText2);
			if (!resultsCountText2.equals(updatedResultsCount.get())) {
				LOGGER.info("Success Profiles Results count updated and Now " + resultsCountText2 + " as expected after applying KF Grade Filters in HCM Sync Profiles screen in PM");
				ExtentCucumberAdapter.addTestStepLog("Success Profiles Results count updated and Now " + resultsCountText2 + " as expected after applying KF Grade Filters in HCM Sync Profiles screen in PM");	
			} else {
					ExtentCucumberAdapter.addTestStepLog("Issue in updating success profiles results count, Still " + resultsCountText2 + " after applying KF Grade Filters in HCM Sync Profiles screen in PM....Please Investiagte!!!");
					throw new Exception("Issue in updating success profiles results count, Still " + resultsCountText2 + " after applying KF Grade Filters in HCM Sync Profiles screen in PM....Please Investiagte!!!");
				}
			}
	} catch (Exception e) {
		LOGGER.error(" Issue verifying profiles count after KF Grade filter - Method: apply_kf_grade_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab", e);
			e.printStackTrace();
			Assert.fail("Issue in verifying success profiles results count after scrolling page down in HCM Sync Profiles screen in PM...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying success profiles results count after scrolling page down in in HCM Sync Profiles screen in PM...Please Investigate!!!");
		}
	} catch (Exception e) {
		LOGGER.error(" Issue applying KF Grade filter - Method: apply_kf_grade_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab", e);
			e.printStackTrace();
			Assert.fail("Issue in verifying KF Grade Filter and Verifing Profiles count is correctly displaying in HCM Sync Profiles screen in PM...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying KF Grade Filter and Verifing Profiles count is correctly displaying in HCM Sync Profiles screen in PM...Please Investigate!!!");
		}
	}
	
	public void clear_kf_grade_filter_in_hcm_sync_profiles_tab() {
		try {
			WebElement closeFilterElement = wait.until(ExpectedConditions.visibilityOf(closeAppliedFilter));
			closeFilterElement.click();
			PerformanceUtils.waitForSpinnersToDisappear(driver);
			LOGGER.info("Cleared Applied KF Grade Filter in HCM Sync Profiles screen in PM");
			ExtentCucumberAdapter.addTestStepLog("Cleared Applied KF Grade Filter in HCM Sync Profiles screen in PM");
	} catch (Exception e) {
		LOGGER.error(" Issue clearing KF Grade filter - Method: clear_kf_grade_filter_in_hcm_sync_profiles_tab", e);
			e.printStackTrace();
			Assert.fail("Issue in Clearing Applied KF Grade Filter in HCM Sync Profiles screen in PM...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in Clearing Applied KF Grade Filter in HCM Sync Profiles screen in PM...Please Investigate!!!");
		}
	}
	
	public void apply_levels_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab() {
		try {
			try {
				PerformanceUtils.waitForElement(driver, LevelsFiltersDropdown);
				wait.until(ExpectedConditions.visibilityOf(LevelsFiltersDropdown)).isDisplayed();
				utils.jsClick(driver, LevelsFiltersDropdown);
				LOGGER.info("Clicked on Levels dropdown in Filters in HCM Sync Profiles screen in PM...");
				ExtentCucumberAdapter.addTestStepLog("Clicked on Levels dropdown in Filters in HCM Sync Profiles screen in PM...");
	} catch (Exception e) {
		LOGGER.error(" Issue clicking Levels dropdown - Method: apply_levels_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab", e);
				e.printStackTrace();
				Assert.fail("Issue in clicking Levels dropdown in Filters in HCM Sync Profiles screen in PM...Please Investigate!!!");
				ExtentCucumberAdapter.addTestStepLog("Issue in clicking Levels dropdown in Filters in HCM Sync Profiles screen in PM...Please Investigate!!!");
			}
			
		try {
		// DYNAMIC XPATH: Find all available Levels options using updated class-based XPATH
		PerformanceUtils.waitForPageReady(driver, 2);
		
		if (LevelsAllCheckboxes.isEmpty() || LevelsAllLabels.isEmpty()) {
			LOGGER.error(" No Levels options found after expanding dropdown");
			throw new Exception("No Levels filter options found");
		}
		
		// Get the first checkbox and its corresponding label
		WebElement firstCheckbox = LevelsAllCheckboxes.get(0);
		String levelsValue = LevelsAllLabels.get(0).getText().trim();
		
		LOGGER.info("Found Levels option: " + levelsValue);
		
		// Scroll to element and click
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", firstCheckbox);
		Thread.sleep(300);
		
		try {
			wait.until(ExpectedConditions.elementToBeClickable(firstCheckbox)).click();
			LOGGER.info(" Clicked Levels option using standard click");
		} catch (Exception e) {
			LOGGER.warn("Standard click failed, trying JS click...");
			js.executeScript("arguments[0].click();", firstCheckbox);
			LOGGER.info(" Clicked Levels option using JS click");
		}
		
		Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(closeAppliedFilter)).isDisplayed());
		LOGGER.info("Selected Levels Value : " + levelsValue + " from Filters dropdown in HCM Sync Profiles screen in PM....");
		ExtentCucumberAdapter.addTestStepLog("Selected Levels Value : " + levelsValue + " from Filters dropdown in HCM Sync Profiles screen in PM....");
	} catch (Exception e) {
		LOGGER.error(" Issue selecting Levels option - Method: apply_levels_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab", e);
					e.printStackTrace();
					Assert.fail("Issue in selecting a option from Levels dropdown in Filters in HCM Sync Profiles screen in PM...Please Investigate!!!");
					ExtentCucumberAdapter.addTestStepLog("Issue in selecting a option from Levels dropdown in Filters in HCM Sync Profiles screen in PM...Please Investigate!!!");
			}
			
			try { 
							// PERFORMANCE: Removed Thread.sleep(2000) - scrolling doesn't need fixed delay
			js.executeScript("window.scrollTo(0, 0);"); // Scroll to top (headless-compatible)
				try {
					wait.until(ExpectedConditions.visibilityOf(hcmSyncProfilesHeader)).click();
				}  catch (Exception e) {
					try {
						js.executeScript("arguments[0].click();", hcmSyncProfilesHeader);
					} catch (Exception s) {
						utils.jsClick(driver, hcmSyncProfilesHeader);
					}
				}
				Assert.assertTrue(wait.until(ExpectedConditions.invisibilityOf(filterOptions)));
				LOGGER.info("Filters dropdown closed successfully in HCM Sync Profiles screen in PM...");
				ExtentCucumberAdapter.addTestStepLog("Filters dropdown closed successfully in HCM Sync Profiles screen in PM...");
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail("Issue in closing filters dropdown in HCM Sync Profiles screen in PM...Please Investigate!!!");
				ExtentCucumberAdapter.addTestStepLog("Issue in closing filters dropdown in HCM Sync Profiles screen in PM...Please Investigate!!!");
			}
			
		try {
			// PERFORMANCE: Single comprehensive wait after closing dropdown
			PerformanceUtils.waitForPageReady(driver, 5);
			
			// Check if there are no results after applying the filter
			boolean noResults = false;
			try {
				if (noResultsMessage.isDisplayed() || NoSPMsg.isDisplayed()) {
					noResults = true;
				}
			} catch (Exception e) {
				// Elements not found, meaning there are results
				noResults = false;
			}
			
			if (noResults) {
				LOGGER.warn(" NO RESULTS FOUND after applying Levels filter - This is acceptable, the selected filter value returned 0 results");
				ExtentCucumberAdapter.addTestStepLog(" NO RESULTS - The applied Levels filter returned 0 results. This is an expected scenario.");
				try {
					String countText = showingJobResultsCount.getText();
					LOGGER.info("Results count shows: " + countText);
					ExtentCucumberAdapter.addTestStepLog("Results count shows: " + countText);
				} catch (Exception ex) {
					LOGGER.info("No results count displayed - 0 profiles match the filter");
				}
			} else {
				// There are results, verify count changed
				// PARALLEL EXECUTION FIX: Add spinner wait before checking count
				PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
				PerformanceUtils.waitForPageReady(driver, 5);
				
				// PARALLEL EXECUTION FIX: Use locator-based wait to avoid stale element
				String resultsCountText2 = wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("//div[contains(text(),'Showing')]"))).getText();
					
				Assert.assertNotEquals(intialResultsCount.get(), resultsCountText2,
					"Results count should change after applying Levels filter");
					
			if (!resultsCountText2.equals(intialResultsCount.get())) {
				LOGGER.info("Success Profiles Results count updated and Now " + resultsCountText2 + " as expected after applying Levels Filters in HCM Sync Profiles screen in PM");
				ExtentCucumberAdapter.addTestStepLog("Success Profiles Results count updated and Now " + resultsCountText2 + " as expected after applying Levels Filters in HCM Sync Profiles screen in PM");	
			} else {
					Assert.fail("Issue in updating success profiles results count, Still " + resultsCountText2 + " after applying Levels Filters in HCM Sync Profiles screen in PM....Please Investiagte!!!");
					ExtentCucumberAdapter.addTestStepLog("Issue in updating success profiles results count, Still " + resultsCountText2 + " after applying Levels Filters in HCM Sync Profiles screen in PM....Please Investiagte!!!");
					throw new Exception("Issue in updating success profiles results count, Still " + resultsCountText2 + " after applying Levels Filters in HCM Sync Profiles screen in PM....Please Investiagte!!!");
				}
			}
	} catch (Exception e) {
		LOGGER.error(" Issue verifying profiles count after Levels filter - Method: apply_levels_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab", e);
			e.printStackTrace();
			Assert.fail("Issue in verifying success profiles results count after scrolling page down in HCM Sync Profiles screen in PM...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying success profiles results count after scrolling page down in in HCM Sync Profiles screen in PM...Please Investigate!!!");
		}
	} catch (Exception e) {
		LOGGER.error(" Issue applying Levels filter - Method: apply_levels_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab", e);
			e.printStackTrace();
			Assert.fail("Issue in verifying Levels Filter and Verifing Profiles count is correctly displaying in HCM Sync Profiles screen in PM...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying Levels Filter and Verifing Profiles count is correctly displaying in HCM Sync Profiles screen in PM...Please Investigate!!!");
		}
	}
	
	public void clear_levels_filter_in_hcm_sync_profiles_tab() {
		try {
			WebElement closeFilterElement = wait.until(ExpectedConditions.visibilityOf(closeAppliedFilter));
			closeFilterElement.click();
			PerformanceUtils.waitForSpinnersToDisappear(driver);
			LOGGER.info("Cleared Applied Levels Filter in HCM Sync Profiles screen in PM");
			ExtentCucumberAdapter.addTestStepLog("Cleared Applied Levels Filter in HCM Sync Profiles screen in PM");
	} catch (Exception e) {
		LOGGER.error(" Issue clearing Levels filter - Method: clear_levels_filter_in_hcm_sync_profiles_tab", e);
			e.printStackTrace();
			Assert.fail("Issue in Clearing Applied Levels Filter in HCM Sync Profiles screen in PM...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in Clearing Applied Levels Filter in HCM Sync Profiles screen in PM...Please Investigate!!!");
		}
	}
	
	public void apply_functions_or_subfunctions_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab() {
		try {
			try {
				PerformanceUtils.waitForElement(driver, FunctionsSubfunctionsFiltersDropdown);
				wait.until(ExpectedConditions.visibilityOf(FunctionsSubfunctionsFiltersDropdown)).isDisplayed();
				utils.jsClick(driver, FunctionsSubfunctionsFiltersDropdown);
				LOGGER.info("Clicked on Functions / Subfunctions dropdown in Filters in HCM Sync Profiles screen in PM...");
				ExtentCucumberAdapter.addTestStepLog("Clicked on Functions / Subfunctions dropdown in Filters in HCM Sync Profiles screen in PM...");
	} catch (Exception e) {
		LOGGER.error(" Issue clicking Functions/Subfunctions dropdown - Method: apply_functions_or_subfunctions_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab", e);
				e.printStackTrace();
				Assert.fail("Issue in clicking Functions / Subfunctions dropdown in Filters in HCM Sync Profiles screen in PM...Please Investigate!!!");
				ExtentCucumberAdapter.addTestStepLog("Issue in clicking Functions / Subfunctions dropdown in Filters in HCM Sync Profiles screen in PM...Please Investigate!!!");
			}
			
		try {
		// DYNAMIC XPATH: Find all available Functions/Subfunctions options using updated class-based XPATH
		PerformanceUtils.waitForPageReady(driver, 2);
		
		if (FunctionsSubfunctionsAllCheckboxes.isEmpty() || FunctionsSubfunctionsAllLabels.isEmpty()) {
			LOGGER.error(" No Functions/Subfunctions options found after expanding dropdown");
			throw new Exception("No Functions/Subfunctions filter options found");
		}
		
		// Get the first checkbox and its corresponding label
		WebElement firstCheckbox = FunctionsSubfunctionsAllCheckboxes.get(0);
		String functionsValue = FunctionsSubfunctionsAllLabels.get(0).getText().trim();
		
		LOGGER.info("Found Functions/Subfunctions option: " + functionsValue);
		
		// Scroll to element and click
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", firstCheckbox);
		Thread.sleep(300);
		
		try {
			wait.until(ExpectedConditions.elementToBeClickable(firstCheckbox)).click();
			LOGGER.info(" Clicked Functions/Subfunctions option using standard click");
		} catch (Exception e) {
			LOGGER.warn("Standard click failed, trying JS click...");
			js.executeScript("arguments[0].click();", firstCheckbox);
			LOGGER.info(" Clicked Functions/Subfunctions option using JS click");
		}
		
		Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(clearAllFiltersBtn)).isDisplayed());
		LOGGER.info("Selected Function Value : " + functionsValue + " from Filters dropdown in HCM Sync Profiles screen in PM....");
		ExtentCucumberAdapter.addTestStepLog("Selected Function Value : " + functionsValue + " from Filters dropdown in HCM Sync Profiles screen in PM....");
	} catch (Exception e) {
		LOGGER.error(" Issue selecting Functions/Subfunctions option - Method: apply_functions_or_subfunctions_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab", e);
				e.printStackTrace();
				Assert.fail("Issue in selecting a option from Functions / Subfunctions dropdown in Filters in HCM Sync Profiles screen in PM...Please Investigate!!!");
				ExtentCucumberAdapter.addTestStepLog("Issue in selecting a option from Functions / Subfunctions dropdown in Filters in HCM Sync Profiles screen in PM...Please Investigate!!!");
		}
			
			try { 
							// PERFORMANCE: Removed Thread.sleep(2000) - scrolling doesn't need fixed delay
			js.executeScript("window.scrollTo(0, 0);"); // Scroll to top (headless-compatible)
				try {
					wait.until(ExpectedConditions.visibilityOf(hcmSyncProfilesHeader)).click();
				}  catch (Exception e) {
					try {
						js.executeScript("arguments[0].click();", hcmSyncProfilesHeader);
					} catch (Exception s) {
						utils.jsClick(driver, hcmSyncProfilesHeader);
					}
				}
				Assert.assertTrue(wait.until(ExpectedConditions.invisibilityOf(filterOptions)));
				LOGGER.info("Filters dropdown closed successfully in HCM Sync Profiles screen in PM...");
				ExtentCucumberAdapter.addTestStepLog("Filters dropdown closed successfully in HCM Sync Profiles screen in PM...");
	} catch (Exception e) {
		LOGGER.error(" Issue closing filters dropdown - Method: apply_functions_or_subfunctions_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab", e);
				e.printStackTrace();
				Assert.fail("Issue in closing filters dropdown in HCM Sync Profiles screen in PM...Please Investigate!!!");
				ExtentCucumberAdapter.addTestStepLog("Issue in closing filters dropdown in HCM Sync Profiles screen in PM...Please Investigate!!!");
			}
			
		try {
			// PERFORMANCE: Single comprehensive wait after closing dropdown
			PerformanceUtils.waitForPageReady(driver, 5);
			
			// Check if there are no results after applying the filter
			boolean noResults = false;
			try {
				if (noResultsMessage.isDisplayed() || NoSPMsg.isDisplayed()) {
					noResults = true;
				}
			} catch (Exception e) {
				// Elements not found, meaning there are results
				noResults = false;
			}
			
			if (noResults) {
				LOGGER.warn(" NO RESULTS FOUND after applying Functions/Subfunctions filter - This is acceptable, the selected filter value returned 0 results");
				ExtentCucumberAdapter.addTestStepLog(" NO RESULTS - The applied Functions/Subfunctions filter returned 0 results. This is an expected scenario.");
				try {
					String countText = showingJobResultsCount.getText();
					LOGGER.info("Results count shows: " + countText);
					ExtentCucumberAdapter.addTestStepLog("Results count shows: " + countText);
				} catch (Exception ex) {
					LOGGER.info("No results count displayed - 0 profiles match the filter");
				}
			} else {
				// There are results, verify count changed
				PerformanceUtils.waitForElement(driver, showingJobResultsCount);
			String resultsCountText2 = wait.until(ExpectedConditions.visibilityOf(showingJobResultsCount)).getText();
			Assert.assertNotEquals(intialResultsCount.get(), resultsCountText2);
			if (!resultsCountText2.equals(intialResultsCount.get())) {
				LOGGER.info("Success Profiles Results count updated and Now " + resultsCountText2 + " as expected after applying Functions / Subfunctions Filters in HCM Sync Profiles screen in PM");
				ExtentCucumberAdapter.addTestStepLog("Success Profiles Results count updated and Now " + resultsCountText2 + " as expected after applying Functions / Subfunctions Filters in HCM Sync Profiles screen in PM");	
			} else {
					Assert.fail("Issue in updating success profiles results count, Still " + resultsCountText2 + " after applying Functions / Subfunctions Filters in HCM Sync Profiles screen in PM....Please Investiagte!!!");
					ExtentCucumberAdapter.addTestStepLog("Issue in updating success profiles results count, Still " + resultsCountText2 + " after applying Functions / Subfunctions Filters in HCM Sync Profiles screen in PM....Please Investiagte!!!");
					throw new Exception("Issue in updating success profiles results count, Still " + resultsCountText2 + " after applying Functions / Subfunctions Filters in HCM Sync Profiles screen in PM....Please Investiagte!!!");
				}
			}
	} catch (Exception e) {
		LOGGER.error(" Issue verifying profiles count after Functions filter - Method: apply_functions_or_subfunctions_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab", e);
			e.printStackTrace();
			Assert.fail("Issue in verifying success profiles results count after scrolling page down in HCM Sync Profiles screen in PM...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying success profiles results count after scrolling page down in in HCM Sync Profiles screen in PM...Please Investigate!!!");
		}
	} catch (Exception e) {
		LOGGER.error(" Issue applying Functions/Subfunctions filter - Method: apply_functions_or_subfunctions_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab", e);
			e.printStackTrace();
			Assert.fail("Issue in verifying Functions / Subfunctions Filter and Verifing Profiles count is correctly displaying in HCM Sync Profiles screen in PM...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying Functions / Subfunctions Filter and Verifing Profiles count is correctly displaying in HCM Sync Profiles screen in PM...Please Investigate!!!");
		}
	}
	
	public void click_on_clear_all_filters_button_in_hcm_sync_profiles_tab() {
		try {
			// Scroll to top of page to avoid header interception
			js.executeScript("window.scrollTo(0, 0);");
			// PERFORMANCE: No wait needed for instant scroll
			
			// Scroll element into view and click
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", clearAllFiltersBtn);
			// PERFORMANCE: Reduced wait for smooth scroll animation (300ms is enough)
			Thread.sleep(300);
			
		try {
			wait.until(ExpectedConditions.elementToBeClickable(clearAllFiltersBtn)).click();
				LOGGER.info("Clicked on Clear All Filters button using standard click");
			} catch (Exception e) {
				LOGGER.warn("Standard click failed, trying JS click...");
				js.executeScript("arguments[0].click();", clearAllFiltersBtn);
				LOGGER.info("Clicked on Clear All Filters button using JS click");
			}
			
			LOGGER.info("Clicked on Clear All Filters button in HCM Sync Profiles screen in PM");
			ExtentCucumberAdapter.addTestStepLog("Clicked on Clear All Filters button in HCM Sync Profiles screen in PM");
			PerformanceUtils.waitForSpinnersToDisappear(driver);
	} catch (Exception e) {
		LOGGER.error(" Issue clicking Clear All Filters button - Method: click_on_clear_all_filters_button_in_hcm_sync_profiles_tab", e);
			e.printStackTrace();
			Assert.fail("Issue in clicking on Clear All Filters button in HCM Sync Profiles screen in PM...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in clicking on Clear All Filters button in HCM Sync Profiles screen in PM...Please Investigate!!!");
		}
	}
	
	public void apply_profile_status_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab() {
		try {
			try {
				PerformanceUtils.waitForElement(driver, ProfileStatusFiltersDropdown);
				wait.until(ExpectedConditions.visibilityOf(ProfileStatusFiltersDropdown)).isDisplayed();
				utils.jsClick(driver, ProfileStatusFiltersDropdown);
				LOGGER.info("Clicked on Profile Status dropdown in Filters in HCM Sync Profiles screen in PM...");
				ExtentCucumberAdapter.addTestStepLog("Clicked on Profile Status dropdown in Filters in HCM Sync Profiles screen in PM...");
	} catch (Exception e) {
			LOGGER.error(" Issue clicking Profile Status dropdown - Method: apply_profile_status_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab", e);
			e.printStackTrace();
			Assert.fail("Issue in clicking on Profile Status dropdown in Filters in HCM Sync Profiles screen in PM...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in clicking on Profile Status dropdown in Filters in HCM Sync Profiles screen in PM...Please Investigate!!!");
		}
			
		try {
		// DYNAMIC XPATH: Find all available Profile Status options using updated class-based XPATH
		PerformanceUtils.waitForPageReady(driver, 2);
		
		if (ProfileStatusAllCheckboxes.isEmpty() || ProfileStatusAllLabels.isEmpty()) {
			LOGGER.error(" No Profile Status options found after expanding dropdown");
			throw new Exception("No Profile Status filter options found");
		}
		
		// Get the first checkbox and its corresponding label
		WebElement firstCheckbox = ProfileStatusAllCheckboxes.get(0);
		String profileStatusValue = ProfileStatusAllLabels.get(0).getText().trim();
		
		LOGGER.info("Found Profile Status option: " + profileStatusValue);
		
		// Scroll to element and click
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", firstCheckbox);
		Thread.sleep(300);
		
		try {
			wait.until(ExpectedConditions.elementToBeClickable(firstCheckbox)).click();
			LOGGER.info(" Clicked Profile Status option using standard click");
		} catch (Exception e) {
			LOGGER.warn("Standard click failed, trying JS click...");
			js.executeScript("arguments[0].click();", firstCheckbox);
			LOGGER.info(" Clicked Profile Status option using JS click");
		}
		
		Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(closeAppliedFilter)).isDisplayed());
		LOGGER.info("Selected Profile Status Value : " + profileStatusValue + " from Filters dropdown in HCM Sync Profiles screen in PM....");
		ExtentCucumberAdapter.addTestStepLog("Selected Profile Status Value : " + profileStatusValue + " from Filters dropdown in HCM Sync Profiles screen in PM....");
	} catch (Exception e) {
			LOGGER.error(" Issue selecting Profile Status option - Method: apply_profile_status_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab", e);
			e.printStackTrace();
			Assert.fail("Issue in selecting a option from Profile Status dropdown in Filters in HCM Sync Profiles screen in PM...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in selecting a option from Profile Status dropdown in Filters in HCM Sync Profiles screen in PM...Please Investigate!!!");
	}
			
			try { 
							// PERFORMANCE: Removed Thread.sleep(2000) - scrolling doesn't need fixed delay
			js.executeScript("window.scrollTo(0, 0);"); // Scroll to top (headless-compatible)
				try {
					wait.until(ExpectedConditions.elementToBeClickable(hcmSyncProfilesHeader)).click();
				} catch (Exception e) {
					try {
						js.executeScript("arguments[0].click();", hcmSyncProfilesHeader);
					} catch (Exception s) {
						utils.jsClick(driver, hcmSyncProfilesHeader);
					}
				}
				Assert.assertTrue(wait.until(ExpectedConditions.invisibilityOf(filterOptions)));
				LOGGER.info("Filters dropdown closed successfully in HCM Sync Profiles screen in PM...");
				ExtentCucumberAdapter.addTestStepLog("Filters dropdown closed successfully in HCM Sync Profiles screen in PM...");
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail("Issue in closing filters dropdown in HCM Sync Profiles screen in PM...Please Investigate!!!");
				ExtentCucumberAdapter.addTestStepLog("Issue in closing filters dropdown in HCM Sync Profiles screen in PM...Please Investigate!!!");
			}
			
		try {
			// PERFORMANCE: Single comprehensive wait after closing dropdown
			PerformanceUtils.waitForPageReady(driver, 5);
			
			// Check if there are no results after applying the filter
			boolean noResults = false;
			try {
				if (noResultsMessage.isDisplayed() || NoSPMsg.isDisplayed()) {
					noResults = true;
				}
			} catch (Exception e) {
				// Elements not found, meaning there are results
				noResults = false;
			}
			
			if (noResults) {
				LOGGER.warn(" NO RESULTS FOUND after applying Profile Status filter - This is acceptable, the selected filter value returned 0 results");
				ExtentCucumberAdapter.addTestStepLog(" NO RESULTS - The applied Profile Status filter returned 0 results. This is an expected scenario.");
				try {
					String countText = showingJobResultsCount.getText();
					LOGGER.info("Results count shows: " + countText);
					ExtentCucumberAdapter.addTestStepLog("Results count shows: " + countText);
				} catch (Exception ex) {
					LOGGER.info("No results count displayed - 0 profiles match the filter");
				}
			} else {
				// There are results, verify count changed
			PerformanceUtils.waitForElement(driver, showingJobResultsCount);
			String resultsCountText2 = wait.until(ExpectedConditions.visibilityOf(showingJobResultsCount)).getText();
			Assert.assertNotEquals(updatedResultsCount.get(), resultsCountText2);
			if (!resultsCountText2.equals(updatedResultsCount.get())) {
				LOGGER.info("Success Profiles Results count updated and Now " + resultsCountText2 + " as expected after applying Profile Status Filters in HCM Sync Profiles screen in PM");
				ExtentCucumberAdapter.addTestStepLog("Success Profiles Results count updated and Now " + resultsCountText2 + " as expected after applying Profile Status Filters in HCM Sync Profiles screen in PM");
				} else {
					ExtentCucumberAdapter.addTestStepLog("Issue in updating success profiles results count, Still " + resultsCountText2 + " after applying Profile Status Filters in HCM Sync Profiles screen in PM....Please Investiagte!!!");
					throw new Exception("Issue in updating success profiles results count, Still " + resultsCountText2 + " after applying Profile Status Filters in HCM Sync Profiles screen in PM....Please Investiagte!!!");
				}
			}
	} catch (Exception e) {
		LOGGER.error(" Issue verifying profiles count after Profile Status filter - Method: apply_profile_status_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab", e);
		e.printStackTrace();
		Assert.fail("Issue in verifying success profiles results count after scrolling page down in HCM Sync Profiles screen in PM...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in verifying success profiles results count after scrolling page down in in HCM Sync Profiles screen in PM...Please Investigate!!!");
	}
	} catch (Exception e) {
		LOGGER.error(" Issue applying Profile Status filter - Method: apply_profile_status_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab", e);
		e.printStackTrace();
		Assert.fail("Issue in verifying Profile Status Filter and Verifing Profiles count is correctly displaying in HCM Sync Profiles screen in PM...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in verifying Profile Status Filter and Verifing Profiles count is correctly displaying in HCM Sync Profiles screen in PM...Please Investigate!!!");
	}
	}
	
	public void user_should_verify_organization_jobs_table_headers_are_correctly_displaying_in_hcm_sync_profiles_tab() {
		try {
			String tableHeader1Text = wait.until(ExpectedConditions.visibilityOf(tableHeader1)).getText();
			Assert.assertEquals(tableHeader1Text, "NAME");
			String tableHeader2Text = wait.until(ExpectedConditions.visibilityOf(tableHeader2)).getText();
			Assert.assertEquals(tableHeader2Text, "STATUS");
			String tableHeader3Text = wait.until(ExpectedConditions.visibilityOf(tableHeader3)).getText();
			Assert.assertEquals(tableHeader3Text, "KF GRADE");
			String tableHeader4Text = wait.until(ExpectedConditions.visibilityOf(tableHeader4)).getText();
			Assert.assertEquals(tableHeader4Text, "LEVEL");
			String tableHeader5Text = wait.until(ExpectedConditions.visibilityOf(tableHeader5)).getText();
			Assert.assertEquals(tableHeader5Text, "FUNCTION");
			String tableHeader6Text = wait.until(ExpectedConditions.visibilityOf(tableHeader6)).getText();
			Assert.assertEquals(tableHeader6Text, "CREATED BY");
			String tableHeader7Text = wait.until(ExpectedConditions.visibilityOf(tableHeader7)).getText();
			Assert.assertEquals(tableHeader7Text, "LAST MODIFIED");
			String tableHeader8Text = wait.until(ExpectedConditions.visibilityOf(tableHeader8)).getText();
			Assert.assertEquals(tableHeader8Text, "EXPORT STATUS");
		LOGGER.info("Organization jobs table headers verified successfully in HCM Sync Profiles screen in PM");
		ExtentCucumberAdapter.addTestStepLog("Organization jobs table headers verified successfully in HCM Sync Profiles screen in PM");
	} catch (Exception e) {
		LOGGER.error(" Issue verifying table headers - Method: user_should_verify_organization_jobs_table_headers_are_correctly_displaying_in_hcm_sync_profiles_tab", e);
		e.printStackTrace();
		Assert.fail("Issue in verifying organization jobs table headers in HCM Sync Profiles screen in PM....Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in verifying organization jobs table headers in HCM Sync Profiles screen in PM....Please Investigate!!!");
	}
	}
	
	public void user_should_verify_download_button_is_disabled_in_hcm_sync_profiles_tab() {
		try {
			Assert.assertTrue(!(wait.until(ExpectedConditions.visibilityOf(downloadBtn)).isEnabled()));
		LOGGER.info("Download button is disabled as expected in HCM Sync Profiles screen in PM");
		ExtentCucumberAdapter.addTestStepLog("Download button is disabled as expected in HCM Sync Profiles screen in PM");
	} catch (Exception e) {
		LOGGER.error(" Issue verifying download button disabled - Method: user_should_verify_download_button_is_disabled_in_hcm_sync_profiles_tab", e);
		e.printStackTrace();
		Assert.fail("Issue in verifying Download button is disabled in HCM Sync Profiles screen in PM...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in verifying Download button is disabled in HCM Sync Profiles screen in PM...Please Investigate!!!");
	}
	}
	
	public void click_on_header_checkbox_to_select_loaded_job_profiles_in_hcm_sync_profiles_tab() {
		try {
			// Step 1: Store count of profiles loaded BEFORE clicking header checkbox
		String resultsCountText = wait.until(ExpectedConditions.visibilityOf(showingJobResultsCount)).getText();
		String[] resultsCountText_split = resultsCountText.split(" ");
		loadedProfilesBeforeHeaderCheckboxClick.set(Integer.parseInt(resultsCountText_split[1]));
		LOGGER.info("Loaded profiles on screen (BEFORE header checkbox click): " + loadedProfilesBeforeHeaderCheckboxClick.get());
		
		// Step 2: Click header checkbox
		try {
			wait.until(ExpectedConditions.elementToBeClickable(tableHeaderCheckbox)).click();
		} catch (Exception e) {
			try {
				js.executeScript("arguments[0].click();", tableHeaderCheckbox);
			} catch (Exception s) {
				utils.jsClick(driver, tableHeaderCheckbox);
			}
		}
		
	// Step 3: Count selected and disabled profiles (without scrolling)
	profilesCount.set(loadedProfilesBeforeHeaderCheckboxClick.get());
	disabledProfilesCountInLoadedProfiles.set(0);
	
	for(int i = 1; i <= loadedProfilesBeforeHeaderCheckboxClick.get(); i++) {
		try {
			WebElement	SP_Checkbox = driver.findElement(By.xpath("//tbody//tr[" + Integer.toString(i) + "]//td[1]//*//..//div//kf-checkbox//div"));
			// REMOVED: Scroll operation - js.executeScript("arguments[0].scrollIntoView(true);", SP_Checkbox);
			String text = SP_Checkbox.getAttribute("class");
			if(text.contains("disable")) {
				LOGGER.info("Success profile with No Job Code assigned is found....");
				ExtentCucumberAdapter.addTestStepLog("Success profile with No Job Code assigned is found....");
				disabledProfilesCountInLoadedProfiles.set(disabledProfilesCountInLoadedProfiles.get() + 1);
				profilesCount.set(profilesCount.get() - 1);
			}
		} catch(Exception e) {
			// PERFORMANCE: Single comprehensive wait
			PerformanceUtils.waitForPageReady(driver, 5);
			WebElement	SP_Checkbox = driver.findElement(By.xpath("//tbody//tr[" + Integer.toString(i) + "]//td[1]//*//..//div//kf-checkbox//div"));
			// REMOVED: Scroll operation - js.executeScript("arguments[0].scrollIntoView(true);", SP_Checkbox);
			String text = SP_Checkbox.getAttribute("class");
			if(text.contains("disable")) {
				LOGGER.info("Success profile with No Job Code assigned is found....");
				ExtentCucumberAdapter.addTestStepLog("Success profile with No Job Code assigned is found....");
				disabledProfilesCountInLoadedProfiles.set(disabledProfilesCountInLoadedProfiles.get() + 1);
				profilesCount.set(profilesCount.get() - 1);
			}
		}
	}
		
		// Step 4: Store selected profiles count
		selectedProfilesAfterHeaderCheckboxClick.set(profilesCount.get());
			
			LOGGER.info("Clicked on header checkbox and selected " + selectedProfilesAfterHeaderCheckboxClick.get() + " job profiles in HCM Sync Profiles screen in PM");
			LOGGER.info("    Loaded profiles (before click): " + loadedProfilesBeforeHeaderCheckboxClick.get());
			LOGGER.info("    Selected profiles: " + selectedProfilesAfterHeaderCheckboxClick.get());
			LOGGER.info("    Disabled profiles: " + disabledProfilesCountInLoadedProfiles.get());
			ExtentCucumberAdapter.addTestStepLog("Clicked on header checkbox and selected " + selectedProfilesAfterHeaderCheckboxClick.get() + " job profiles in HCM Sync Profiles screen in PM");
	} catch (Exception e) {
		LOGGER.error(" Issue clicking header checkbox to select loaded profiles - Method: click_on_header_checkbox_to_select_loaded_profiles_in_hcm_sync_profiles_tab", e);
		e.printStackTrace();
		Assert.fail("Issue in clicking on header checkbox to select loaded job profiles in HCM Sync Profiles screen in PM...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in clicking on header checkbox to select loaded job profiles in in HCM Sync Profiles screen in PM...Please Investigate!!!");
	}
	}
	
	public void user_should_verify_download_button_is_enabled_in_hcm_sync_profiles_tab() {
		try {
			js.executeScript("window.scrollTo(0, 0);"); // Scroll to top (headless-compatible)
			PerformanceUtils.waitForSpinnersToDisappear(driver);
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(downloadBtn)).isEnabled());
		LOGGER.info("Download button is enabled as expected after selecting job profiles in HCM Sync Profiles screen in PM");
		ExtentCucumberAdapter.addTestStepLog("Download button is enabled as expected after selecting job profiles in HCM Sync Profiles screen in PM"); 
	} catch (Exception e) {
		LOGGER.error(" Issue verifying download button enabled - Method: user_should_verify_download_button_is_enabled_in_hcm_sync_profiles_tab", e);
		e.printStackTrace();
		Assert.fail("Issue in verifying Download button is enabled in HCM Sync Profiles screen in PM...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in verifying Download button is enabled in HCM Sync Profiles screen in PM...Please Investigate!!!");
	}
	}
	
	public void user_should_uncheck_header_checkbox_to_deselect_selected_job_profiles_in_hcm_sync_profiles_tab() {
		try {
			js.executeScript("window.scrollTo(0, 0);"); // Scroll to top (headless-compatible)
			PerformanceUtils.waitForSpinnersToDisappear(driver);
		// Step 1: Store count of profiles BEFORE unchecking header checkbox
		int profilesBeforeDeselect = selectedProfilesAfterHeaderCheckboxClick.get();
		LOGGER.info("Selected profiles count (BEFORE unchecking header checkbox): " + profilesBeforeDeselect);
		
		// Step 2: Click header checkbox to deselect all
		try {
			wait.until(ExpectedConditions.elementToBeClickable(tableHeaderCheckbox)).click();
		} catch (Exception e) {
			try {
				js.executeScript("arguments[0].click();", tableHeaderCheckbox);
			} catch (Exception s) {
				utils.jsClick(driver, tableHeaderCheckbox);
			}
		}
		
		// Step 3: Wait for action to complete
		PerformanceUtils.waitForPageReady(driver, 2);
		
		// Step 4: Reset profiles count to 0 (all deselected)
		profilesCount.set(0);
			
			LOGGER.info("Clicked on header checkbox and deselected all job profiles in HCM Sync Profiles screen in PM");
			LOGGER.info("    Previously selected profiles: " + profilesBeforeDeselect);
			LOGGER.info("    Currently selected profiles: 0");
			ExtentCucumberAdapter.addTestStepLog("Clicked on header checkbox and deselected all " + profilesBeforeDeselect + " job profiles in HCM Sync Profiles screen in PM");
	} catch (Exception e) {
		LOGGER.error(" Issue clicking header checkbox to deselect all - Method: user_should_uncheck_header_checkbox_to_deselect_selected_job_profiles_in_hcm_sync_profiles_tab", e);
		e.printStackTrace();
		Assert.fail("Issue in clicking on header checkbox to deselect all job profiles in HCM Sync Profiles screen in PM...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in clicking on header checkbox to deselect all job profiles in HCM Sync Profiles screen in PM...Please Investigate!!!");
	}
	}
	
	public void click_on_first_profile_checkbox_in_hcm_sync_profiles_tab() {
		try {
			jobname1.set(wait.until(ExpectedConditions.visibilityOf(HCMSyncProfilesJobinRow1)).getText());
			try {
				wait.until(ExpectedConditions.elementToBeClickable(profile1Checkbox)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", profile1Checkbox);
				} catch (Exception s) {
					utils.jsClick(driver, profile1Checkbox);
				}
			}
		LOGGER.info("Clicked on checkbox of First job profile with name : " + jobname1.get() +" in HCM Sync Profiles screen in PM");
		ExtentCucumberAdapter.addTestStepLog("Clicked on checkbox of First job profile with name : " + jobname1.get() +" in HCM Sync Profiles screen in PM");
		profilesCount.set(profilesCount.get() + 1); 
	} catch (Exception e) {
		LOGGER.error(" Issue clicking first profile checkbox - Method: click_on_first_profile_checkbox_in_hcm_sync_profiles_tab", e);
		e.printStackTrace();
		Assert.fail("Issue in clicking First job profile checkbox in HCM Sync Profiles screen in PM...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in clicking First job profile checkbox in HCM Sync Profiles screen in PM...Please Investigate!!!");
	}
	}
	
	public void click_on_second_profile_checkbox_in_hcm_sync_profiles_tab() {
		try {
			Thread.sleep(500);
			js.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});", HCMSyncProfilesJobinRow2);
			Thread.sleep(300);
			
			jobname2.set(wait.until(ExpectedConditions.visibilityOf(HCMSyncProfilesJobinRow2)).getText());
			try {
				wait.until(ExpectedConditions.elementToBeClickable(profile2Checkbox)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", profile2Checkbox);
				} catch (Exception s) {
					utils.jsClick(driver, profile2Checkbox);
				}
			}
		LOGGER.info("Clicked on checkbox of Second job profile with name : " + jobname2.get() +" in HCM Sync Profiles screen in PM");
		ExtentCucumberAdapter.addTestStepLog("Clicked on checkbox of Second job profile with name : " + jobname2.get() +" in HCM Sync Profiles screen in PM");
		profilesCount.set(profilesCount.get() + 1);
	} catch (Exception e) {
		LOGGER.error(" Issue clicking second profile checkbox - Method: click_on_second_profile_checkbox_in_hcm_sync_profiles_tab", e);
		e.printStackTrace();
		Assert.fail("Issue in clicking Second job profile checkbox in HCM Sync Profiles screen in PM...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in clicking Second job profile checkbox in HCM Sync Profiles screen in PM...Please Investigate!!!");
	}
	}
	
	public void click_on_third_profile_checkbox_in_hcm_sync_profiles_tab() {
		try {
			// Wait for table to load and stabilize
			Thread.sleep(1000);
			PerformanceUtils.waitForPageReady(driver, 2);
			
			// Scroll table container to trigger lazy loading if needed
			try {
				WebElement tableContainer = driver.findElement(By.xpath("//div[contains(@class, 'table') or contains(@class, 'list')]"));
				js.executeScript("arguments[0].scrollTop = arguments[0].scrollHeight;", tableContainer);
				Thread.sleep(500);
			} catch (Exception ignored) {
				// Table container not found or not scrollable, continue
			}
			
			// Wait explicitly for third row to be present
			WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement row3 = shortWait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//tbody//tr[3]//td//div//span[1]//a")));
			
			js.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});", row3);
			Thread.sleep(500);
			
			jobname3.set(shortWait.until(ExpectedConditions.visibilityOf(row3)).getText());
			
			try {
				wait.until(ExpectedConditions.elementToBeClickable(profile3Checkbox)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", profile3Checkbox);
				} catch (Exception s) {
					utils.jsClick(driver, profile3Checkbox);
				}
			}
		LOGGER.info("Clicked on checkbox of Third job profile with name : " + jobname3.get() +" in HCM Sync Profiles screen in PM");
		ExtentCucumberAdapter.addTestStepLog("Clicked on checkbox of Third job profile with name : " + jobname3.get()  +" in HCM Sync Profiles screen in PM");
		profilesCount.set(profilesCount.get() + 1);
	} catch (Exception e) {
		LOGGER.error(" Issue clicking third profile checkbox - Method: click_on_third_profile_checkbox_in_hcm_sync_profiles_tab", e);
		e.printStackTrace();
		Assert.fail("Issue in clicking Third job profile checkbox in HCM Sync Profiles screen in PM...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in clicking Third job profile checkbox in HCM Sync Profiles screen in PM...Please Investigate!!!");
	}
	}
	
	public void user_should_verify_sync_with_hcm_button_is_disabled_in_hcm_sync_profiles_tab() {
		try {
			// PERFORMANCE: Single comprehensive wait before checking button state
			PerformanceUtils.waitForPageReady(driver, 5);
			
			// Wait for button state to update with retry logic
			WebDriverWait buttonWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
			boolean isDisabled = buttonWait.until(driver -> {
				try {
					WebElement syncButton = wait.until(ExpectedConditions.visibilityOf(SyncwithHCMBtn));
					
					// Check 1: Check for 'text-disabled' class (Angular Material disabled state)
					String classAttribute = syncButton.getAttribute("class");
					if (classAttribute != null && classAttribute.contains("text-disabled")) {
						LOGGER.debug("Button has 'text-disabled' class - Button is disabled");
						return true;
					}
					
					// Check 2: Check for 'disabled' attribute
					String disabledAttribute = syncButton.getAttribute("disabled");
					if (disabledAttribute != null) {
						LOGGER.debug("Button has 'disabled' attribute - Button is disabled");
						return true;
					}
					
					// Check 3: Check for 'aria-disabled' attribute
					String ariaDisabled = syncButton.getAttribute("aria-disabled");
					if ("true".equalsIgnoreCase(ariaDisabled)) {
						LOGGER.debug("Button has 'aria-disabled=true' - Button is disabled");
						return true;
					}
					
					// Check 4: Fallback to isEnabled() check
					if (!syncButton.isEnabled()) {
						LOGGER.debug("Button.isEnabled() returned false - Button is disabled");
						return true;
					}
					
					LOGGER.debug("Button not yet disabled, retrying...");
					return false;
				} catch (Exception e) {
					LOGGER.debug("Error checking button state, retrying...");
					return false;
				}
			});
			
			Assert.assertTrue(isDisabled, "Sync with HCM button should be disabled but appears to be enabled");
			LOGGER.info(" Sync with HCM button is disabled as expected in HCM Sync Profiles screen in PM");
			ExtentCucumberAdapter.addTestStepLog(" Sync with HCM button is disabled as expected in HCM Sync Profiles screen in PM");
	} catch (Exception e) {
		LOGGER.error(" Issue verifying Sync with HCM button disabled - Method: user_should_verify_sync_with_hcm_button_is_disabled_in_hcm_sync_profiles_tab", e);
		e.printStackTrace();
		Assert.fail("Issue in verifying Sync with HCM button is disabled in HCM Sync Profiles screen in PM...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in verifying Sync with HCM button is disabled in HCM Sync Profiles screen in PM...Please Investigate!!!");
	}
	}
	
	public void user_should_verify_sync_with_hcm_button_is_enabled_in_hcm_sync_profiles_tab() {
		try {
			// PERFORMANCE: Single comprehensive wait before checking button state
			PerformanceUtils.waitForPageReady(driver, 5);
			
			// Wait for button state to update with retry logic
			WebDriverWait buttonWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
			boolean isEnabled = buttonWait.until(driver -> {
				try {
					WebElement syncButton = wait.until(ExpectedConditions.visibilityOf(SyncwithHCMBtn));
					
					// Check 1: Verify 'text-disabled' class is NOT present
					String classAttribute = syncButton.getAttribute("class");
					if (classAttribute != null && classAttribute.contains("text-disabled")) {
						LOGGER.debug("Button has 'text-disabled' class - Button still disabled, retrying...");
						return false;
					}
					
					// Check 2: Verify 'disabled' attribute is NOT present
					String disabledAttribute = syncButton.getAttribute("disabled");
					if (disabledAttribute != null) {
						LOGGER.debug("Button has 'disabled' attribute - Button still disabled, retrying...");
						return false;
					}
					
					// Check 3: Verify 'aria-disabled' is not 'true'
					String ariaDisabled = syncButton.getAttribute("aria-disabled");
					if ("true".equalsIgnoreCase(ariaDisabled)) {
						LOGGER.debug("Button has 'aria-disabled=true' - Button still disabled, retrying...");
						return false;
					}
					
					// Check 4: Verify isEnabled() returns true
					if (!syncButton.isEnabled()) {
						LOGGER.debug("Button.isEnabled() returned false - Button still disabled, retrying...");
						return false;
					}
					
					LOGGER.debug("Button is enabled!");
					return true;
				} catch (Exception e) {
					LOGGER.debug("Error checking button state, retrying...");
					return false;
				}
			});
			
			Assert.assertTrue(isEnabled, "Sync with HCM button should be enabled but appears to be disabled");
			LOGGER.info(" Sync with HCM button is enabled as expected in HCM Sync Profiles screen in PM");
			ExtentCucumberAdapter.addTestStepLog(" Sync with HCM button is enabled as expected in HCM Sync Profiles screen in PM");
	} catch (Exception e) {
		LOGGER.error(" Issue verifying Sync with HCM button enabled - Method: user_should_verify_sync_with_hcm_button_is_enabled_in_hcm_sync_profiles_tab", e);
		e.printStackTrace();
		Assert.fail("Issue in verifying Sync with HCM button is enabled in HCM Sync Profiles screen in PM...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in verifying Sync with HCM button is enabled in HCM Sync Profiles screen in PM...Please Investigate!!!");
	}
	}
	
	public void verify_checkboxes_of_first_second_and_third_profile_are_selected_in_hcm_sync_profiles_tab() {
		try {
			// PERFORMANCE: Single comprehensive wait
			PerformanceUtils.waitForPageReady(driver, 5);
			String jobname1 = wait.until(ExpectedConditions.visibilityOf(HCMSyncProfilesJobinRow1)).getText();
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(profile1Checkbox)).isSelected());
			LOGGER.info("First job profile with name : " + jobname1 +" is Already Selected in HCM Sync Profiles screen in PM");
			ExtentCucumberAdapter.addTestStepLog("First job profile with name : " + jobname1 +" is Already Selected in HCM Sync Profiles screen in PM");
			
			String jobname2 = wait.until(ExpectedConditions.visibilityOf(HCMSyncProfilesJobinRow2)).getText();
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(profile2Checkbox)).isSelected());
			LOGGER.info("Second job profile with name : " + jobname2 +" is Already Selected in HCM Sync Profiles screen in PM");
			ExtentCucumberAdapter.addTestStepLog("Second job profile with name : " + jobname2 +" is Already Selected in HCM Sync Profiles screen in PM");
			
			String jobname3 = wait.until(ExpectedConditions.visibilityOf(HCMSyncProfilesJobinRow3)).getText();
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(profile3Checkbox)).isSelected());
		LOGGER.info("Third job profile with name : " + jobname3 +" is Already Selected in HCM Sync Profiles screen in PM");
		ExtentCucumberAdapter.addTestStepLog("Third job profile with name : " + jobname3 +" is Already Selected in HCM Sync Profiles screen in PM");
	} catch (Exception e) {
		LOGGER.error(" Issue verifying profile checkboxes selected - Method: verify_checkboxes_of_first_second_and_third_profile_are_selected_in_hcm_sync_profiles_tab", e);
		e.printStackTrace();
		Assert.fail("Issue in Verifying whether checkboxes of First, Second and Third Profile are Selected or Not in HCM Sync Profiles screen in PM...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in Verifying whether checkboxes of First, Second and Third Profile are Selected or Not in HCM Sync Profiles screen in PM...Please Investigate!!!");
	}
	}
	
	public void click_on_sync_with_hcm_button_in_hcm_sync_profiles_tab() {
		try {
			js.executeScript("window.scrollTo(0, 0);"); // Scroll to top (headless-compatible)
			PerformanceUtils.waitForSpinnersToDisappear(driver);
			try {
				wait.until(ExpectedConditions.elementToBeClickable(SyncwithHCMBtn)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", SyncwithHCMBtn);
				} catch (Exception s) {
					utils.jsClick(driver, SyncwithHCMBtn);
				}
			}
		LOGGER.info("Clicked on Sync with HCM button in HCM Sync Profiles screen in PM....");
		ExtentCucumberAdapter.addTestStepLog("Clicked on Sync with HCM button in HCM Sync Profiles screen in PM....");
		PerformanceUtils.waitForSpinnersToDisappear(driver);
	} catch (Exception e) {
		LOGGER.error(" Issue clicking Sync with HCM button - Method: click_on_sync_with_hcm_button_in_hcm_sync_profiles_tab", e);
		e.printStackTrace();
		Assert.fail("Issue in clicking on Sync with HCM button in HCM Sync Profiles screen in PM...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in Verifying CSV Format Zip File download in HCM Sync Profiles screen in PM...Please Investigate!!!");
	}
	}
	
	public void user_should_verify_sync_with_hcm_success_popup_appears_on_screen_in_hcm_sync_profiles_tab() {
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver);
			WebElement successPopup = wait.until(ExpectedConditions.visibilityOf(SyncwithHCMSuccessPopupText));
			String SyncwithHCMSuccessMsg = successPopup.getText();
			LOGGER.info("Sync with HCM Success Popup Message : " + SyncwithHCMSuccessMsg);
			ExtentCucumberAdapter.addTestStepLog("Sync with HCM Success Popup Message : " + SyncwithHCMSuccessMsg);
			
			// Check if message contains "failed" - if so, mark scenario as failed but continue
			if (SyncwithHCMSuccessMsg != null && SyncwithHCMSuccessMsg.toLowerCase().contains("failed")) {
				LOGGER.error("Message: " + SyncwithHCMSuccessMsg);
				ExtentCucumberAdapter.addTestStepLog("Message: " + SyncwithHCMSuccessMsg);
				
				// Close the popup before failing
				try {
					wait.until(ExpectedConditions.visibilityOf(SyncwithHCMSuccessPopupCloseBtn)).click();
					LOGGER.info("Failure popup closed");
				} catch (Exception closeEx) {
					LOGGER.warn("Could not close failure popup: " + closeEx.getMessage());
				}
				
				// Capture screenshot and fail the scenario
				Assert.fail(SyncwithHCMSuccessMsg);
			}
			
			// If message doesn't contain "failed", verify it's the expected success message
			Assert.assertEquals(SyncwithHCMSuccessMsg, "Your profiles are being exported. This may take a few minutes to complete");
			wait.until(ExpectedConditions.visibilityOf(SyncwithHCMSuccessPopupCloseBtn)).click();
			LOGGER.info("Sync with HCM Success Popup closed successfully in HCM Sync Profiles screen in PM....");
			ExtentCucumberAdapter.addTestStepLog("Sync with HCM Success Popup closed successfully in HCM Sync Profiles screen in PM....");
		} catch (AssertionError e) {
			LOGGER.error("Assertion failed in verifying Sync with HCM success popup - Method: user_should_verify_sync_with_hcm_success_popup_appears_on_screen_in_hcm_sync_profiles_tab", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying appearance of Sync with HCM Success Popup in HCM Sync Profiles screen in PM...Please Investigate!!!");
			throw e; // Re-throw to mark scenario as failed but allow next scenarios to continue
		} catch (Exception e) {
			LOGGER.error("Issue verifying Sync with HCM success popup - Method: user_should_verify_sync_with_hcm_success_popup_appears_on_screen_in_hcm_sync_profiles_tab", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying appearance of Sync with HCM Success Popup in HCM Sync Profiles screen in PM...Please Investigate!!!");
			Assert.fail("Issue in Verifying appearance of Sync with HCM Success Popup in HCM Sync Profiles screen in PM...Please Investigate!!!");
		}
		
		try {
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(SyncwithHCMWarningMsg)).isDisplayed());
			String SyncwithHCMWarningMsgText = wait.until(ExpectedConditions.visibilityOf(SyncwithHCMWarningMsg)).getText();
			LOGGER.info("Sync with HCM Warning Message : " + SyncwithHCMWarningMsgText);
			ExtentCucumberAdapter.addTestStepLog("Sync with HCM Warning Message : " + SyncwithHCMWarningMsgText);
			wait.until(ExpectedConditions.visibilityOf(SyncwithHCMWarningMsgCloseBtn)).click();
			LOGGER.info("Sync with HCM Warning Message closed successfully in HCM Sync Profiles screen in PM....");
			ExtentCucumberAdapter.addTestStepLog("Sync with HCM Warning Message closed successfully in HCM Sync Profiles screen in PM....");
		} catch (Exception e) {
			LOGGER.error("Issue verifying Sync with HCM warning message - Method: user_should_verify_sync_with_hcm_success_popup_appears_on_screen_in_hcm_sync_profiles_tab", e);
			e.printStackTrace();
			Assert.fail("Issue in Verifying appearance of Sync with HCM Warning Message in HCM Sync Profiles screen in PM...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying appearance of Sync with HCM Warning Message in HCM Sync Profiles screen in PM...Please Investigate!!!");
		}
	}
	
	public void verify_checkboxes_of_first_second_and_third_profile_are_deselected_in_hcm_sync_profiles_tab() {
		try {
			PerformanceUtils.waitForPageReady(driver, 5);
			String jobname1 = wait.until(ExpectedConditions.visibilityOf(HCMSyncProfilesJobinRow1)).getText();
			Assert.assertTrue(!(wait.until(ExpectedConditions.visibilityOf(profile1Checkbox)).isSelected()));
			LOGGER.info("First job profile with name : " + jobname1 +" is De-Selected as expected after Sync with HCM is successful in HCM Sync Profiles screen in PM");
			ExtentCucumberAdapter.addTestStepLog("First job profile with name : " + jobname1 +" is De-Selected as expected after Sync with HCM is successful in HCM Sync Profiles screen in PM");

			String jobname2 = wait.until(ExpectedConditions.visibilityOf(HCMSyncProfilesJobinRow2)).getText();
			Assert.assertTrue(!(wait.until(ExpectedConditions.visibilityOf(profile2Checkbox)).isSelected()));
			LOGGER.info("Second job profile with name : " + jobname2 +" is De-Selected as expected after Sync with HCM is successful in HCM Sync Profiles screen in PM");
			ExtentCucumberAdapter.addTestStepLog("Second job profile with name : " + jobname2 +" is De-Selected as expected after Sync with HCM is successful in HCM Sync Profiles screen in PM");

			String jobname3 = wait.until(ExpectedConditions.visibilityOf(HCMSyncProfilesJobinRow3)).getText();
			Assert.assertTrue(!(wait.until(ExpectedConditions.visibilityOf(profile3Checkbox)).isSelected()));
			LOGGER.info("Third job profile with name : " + jobname3 +" is De-Selected as expected after Sync with HCM is successful in HCM Sync Profiles screen in PM");
			ExtentCucumberAdapter.addTestStepLog("Third job profile with name : " + jobname3 +" is De-Selected as expected after Sync with HCM is successful in HCM Sync Profiles screen in PM");
	} catch (Exception e) {
		LOGGER.error(" Issue verifying profile checkboxes deselected - Method: verify_checkboxes_of_first_second_and_third_profile_are_deselected_in_hcm_sync_profiles_tab", e);
		e.printStackTrace();
		Assert.fail("Issue in Verifying whether checkboxes of First, Second and Third Profile are De-Selected or Not in HCM Sync Profiles screen in PM...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in Verifying whether checkboxes of First, Second and Third Profile are De-Selected or Not in HCM Sync Profiles screen in PM...Please Investigate!!!");
	}
}

}
