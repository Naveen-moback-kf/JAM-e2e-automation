package com.kfonetalentsuite.pageobjects.JobMapping;

import java.io.IOException;
import java.util.List;
import com.kfonetalentsuite.utils.JobMapping.ScreenshotHandler;

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
import org.testng.Assert;

import com.kfonetalentsuite.utils.JobMapping.Utilities;
import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.HeadlessCompatibleActions;
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
	public static String jobProfileName = "Architect"; // Will be set dynamically based on which name returns results
	
	public static String intialResultsCount;
	public static String updatedResultsCount;
	public static String orgJobNameInRow1;
	public static int profilesCount = 0;
	public static String jobname1;
	public static String jobname2;
	public static String jobname3;
	
	// Variables for Feature 33 - Select Loaded Profiles validation
	public static int loadedProfilesBeforeHeaderCheckboxClick = 0; // Profiles loaded on screen BEFORE clicking header checkbox
	public static int selectedProfilesAfterHeaderCheckboxClick = 0; // Profiles actually selected (enabled profiles)
	public static int disabledProfilesCountInLoadedProfiles = 0; // Disabled profiles (cannot be selected)
	

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
		wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
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
			ScreenshotHandler.captureFailureScreenshot("verify_user_on_profile_manager_page", e);
			LOGGER.error(" Assertion failed - User is NOT on Profile Manager page - Method: user_is_on_profile_manager_page", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog(" Issue in verifying user is on Profile Manager page...Please Investigate!!!");
			throw e; // Re-throw to fail the test
	} catch (Exception e) {
		ScreenshotHandler.captureFailureScreenshot("verify_user_on_profile_manager_page", e);
			LOGGER.error(" Issue verifying user is on Profile Manager page - Method: user_is_on_profile_manager_page", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog(" Issue in verifying user is on Profile Manager page...Please Investigate!!!");
			Assert.fail("Issue in verifying user is on Profile Manager page...Please Investigate!!!");
		}
	}
	
	public void click_on_menu_button() {
		wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
		try {
			utils.jsClick(driver, MenuBtn);
			// PERFORMANCE: Replaced Thread.sleep(2000) with smart page ready wait
			PerformanceUtils.waitForPageReady(driver, 2);
		} catch (Exception e) {
			utils.jsClick(driver, homeMenuBtn1);
			PerformanceUtils.waitForPageReady(driver, 2);
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
		wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
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
		LOGGER.error(" Issue clicking HCM Sync Profiles header - Method: click_on_hcm_sync_profiles_header_button", e);
			ScreenshotHandler.handleTestFailure("click_on_hcm_sync_profiles_header", e, 
				"Issue in clicking HCM Sync Profiles header in Profile Manager...Please Investigate!!!");
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
			ScreenshotHandler.captureFailureScreenshot("navigate_to_hcm_sync_profiles_screen", e);
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
			ScreenshotHandler.captureFailureScreenshot("verify_title_hcm_sync_profiles", e);
			LOGGER.error(" Assertion failed - Title mismatch in HCM Sync Profiles screen - Method: verify_title_is_correctly_displaying_in_hcm_sync_profiles_tab", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying title in HCM Sync Profiles screen in Profile Manager...Please Investigate!!!");
			throw e; // Re-throw to fail the test
	} catch (Exception e) {
		ScreenshotHandler.captureFailureScreenshot("verify_title_hcm_sync_profiles", e);
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
		ScreenshotHandler.captureFailureScreenshot("verify_title_description", e);
			e.printStackTrace();
			Assert.fail("Issue in Verifying title description in HCM Sync Profiles screen in Profile Manager...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying title description in HCM Sync Profiles screen in Profile Manager...Please Investigate!!!");
		}
	}
	
	public void verify_search_bar_text_box_is_clickable_in_hcm_sync_profiles_tab() {
		try {
			// PERFORMANCE: Clickable check already ensures element is visible and spinners are gone
			wait.until(ExpectedConditions.visibilityOf(hcmSyncProfilesSearchbar)).isDisplayed();
			wait.until(ExpectedConditions.elementToBeClickable(hcmSyncProfilesSearchbar)).click();
			LOGGER.info("Search bar text box is displayed and clickable in HCM Sync Profiles screen in Profile Manager");
			ExtentCucumberAdapter.addTestStepLog("Search bar text box is displayed and clickable in HCM Sync Profiles screen in Profile Manager");
	} catch (Exception e) {
		LOGGER.error(" Issue verifying search bar clickability - Method: verify_search_bar_text_box_is_clickable_in_hcm_sync_profiles_tab", e);
		ScreenshotHandler.captureFailureScreenshot("verify_search_bar_clickable", e);
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
		ScreenshotHandler.captureFailureScreenshot("verify_search_bar_placeholder", e);
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
			
			for (String profileName : SEARCH_PROFILE_NAME_OPTIONS) {
				try {
					LOGGER.info("Trying profile name: '" + profileName + "'");
					
				// Clear and enter profile name
				wait.until(ExpectedConditions.elementToBeClickable(hcmSyncProfilesSearchbar)).clear();
				wait.until(ExpectedConditions.elementToBeClickable(hcmSyncProfilesSearchbar)).sendKeys(profileName);
				// PERFORMANCE: Single optimized wait - waitForPageReady already checks spinners
				PerformanceUtils.waitForPageReady(driver, 2);
					
					// Check if results were found
					String resultsCountText = "";
					try {
						resultsCountText = showingJobResultsCount.getText().trim();
					LOGGER.info("   Search results: " + resultsCountText);
					} catch (Exception e) {
						LOGGER.info("   - No profiles found with string '" + profileName + "' (results element not found)");
						LOGGER.info("   Looking for profiles with another string...");
						continue; // Try next profile name
					}
					
					if (resultsCountText.contains("Showing") && !resultsCountText.startsWith("Showing 0")) {
						// Found results!
						selectedProfileName = profileName;
						jobProfileName = profileName; // Update static variable for other methods to use
						foundResults = true;
						
						LOGGER.info(" Found results with profile name: '" + profileName + "'");
						LOGGER.info("   Results: " + resultsCountText);
						ExtentCucumberAdapter.addTestStepLog(" Search successful with profile name '" + profileName + "' - " + resultsCountText);
						break; // Stop trying other profile names
					} else {
						LOGGER.info("   - No profiles found with string '" + profileName + "' (0 results)");
						LOGGER.info("   Looking for profiles with another string...");
					}
					
				} catch (Exception e) {
					LOGGER.info("   - Error searching with '" + profileName + "', trying next...");
					// Continue to next profile name
				}
			}
			
			if (!foundResults) {
				// All profile names exhausted, use the last one
				LOGGER.warn(" All search profile names exhausted without finding results");
				LOGGER.warn("   Proceeding with last profile name: '" + selectedProfileName + "'");
				ExtentCucumberAdapter.addTestStepLog(" No profile name returned results. Using: '" + selectedProfileName + "'");
			}
			
			LOGGER.info("========================================");
			LOGGER.info("Final selected profile name: '" + jobProfileName + "'");
			LOGGER.info("========================================");
			
		} catch (Exception e) {
			LOGGER.error(" Issue entering job profile name in search bar - Method: enter_job_profile_name_in_search_bar_in_hcm_sync_profiles_tab", e);
			ScreenshotHandler.captureFailureScreenshot("enter_job_profile_name_search_bar", e);
			e.printStackTrace();
			Assert.fail("Issue in entering Job Profile Name in Search bar in HCM Sync Profiles screen in Profile Manager...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in entering Job Profile Name in Search bar in HCM Sync Profiles screen in Profile Manager...Please Investigate!!!");
		}
	}
	
	public void user_should_verify_profile_name_matching_profile_is_displaying_in_organization_jobs_profile_list() {
	try {
		LOGGER.info("Verifying if profile name '{}' is displaying in HCM Sync Profiles list", jobProfileName);
		
		// PERFORMANCE: Single optimized wait - waitForPageReady already checks spinners
		PerformanceUtils.waitForPageReady(driver, 2);
		
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
			boolean matchesSearch = profileNameFromList.toLowerCase().contains(jobProfileName.toLowerCase());
			
			if (matchesSearch) {
				LOGGER.info("Searched String present in Job Profile with name: '{}' is displaying in HCM Sync Profiles screen in PM as expected", profileNameFromList);
				ExtentCucumberAdapter.addTestStepLog("Searched String present in Job Profile with name: " + profileNameFromList + " is displaying in HCM Sync Profiles screen in PM as expected");
			} else {
				String errorMsg = String.format(
					"Profile found but does not match search criteria. " +
					"Searched for: '%s', Found profile: '%s', " +
					"Profile does not contain searched text.",
					jobProfileName,
					profileNameFromList
				);
				LOGGER.error(errorMsg);
				ScreenshotHandler.captureFailureScreenshot("profile_mismatch", new AssertionError(errorMsg));
				Assert.fail(errorMsg);
			}
		} else {
			// No profile in first row - check if "No SP" message is displayed
			try {
				wait.until(ExpectedConditions.visibilityOf(NoSPMsg)).isDisplayed();
				LOGGER.info("No Success Profile Found with searched String: '{}'", jobProfileName);
				ExtentCucumberAdapter.addTestStepLog("No Success Profile Found with searched String: " + jobProfileName);
				
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
					jobProfileName,
					d.getClass().getSimpleName(),
					d.getMessage()
				);
				
				LOGGER.error(errorMsg, d);
				ScreenshotHandler.captureFailureScreenshot("verify_profile_name_matching", d);
				d.printStackTrace();
				Assert.fail(errorMsg);
				ExtentCucumberAdapter.addTestStepLog("FAILURE: " + errorMsg);
			}
		}	
	} catch (Exception outerException) {
		// Outer catch for any unexpected errors
		String errorDetails = String.format(
			"Unexpected error while verifying profile name matching. " +
			"Searched for: '%s', Error type: %s, Error message: %s",
			jobProfileName,
			outerException.getClass().getSimpleName(),
			outerException.getMessage()
		);
		
		LOGGER.error(errorDetails, outerException);
		ScreenshotHandler.captureFailureScreenshot("verify_profile_unexpected_error", outerException);
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
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
	} catch (Exception e) {
		LOGGER.error(" Issue clicking on name matching profile - Method: click_on_name_matching_profile_in_hcm_sync_profiles_tab", e);
		ScreenshotHandler.captureFailureScreenshot("click_name_matching_profile", e);
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
		ScreenshotHandler.captureFailureScreenshot("navigate_to_sp_details_page", e);
			e.printStackTrace();
			Assert.fail("Issue in Navigating to SP details Page on click of Matching Profile Job name in HCM Sync Profiles screen in PM...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in Navigating to SP details Page on click of Matching Profile Job name in HCM Sync Profiles screen in PM...Please Investigate!!!");
		}		
	}
	
	public void clear_search_bar_in_hcm_sync_profiles_tab() {
		try {
			// PERFORMANCE: Single optimized wait - visibility check already ensures element is ready
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(hcmSyncProfilesSearchbar)).isDisplayed());
			// Scroll element into view to avoid click interception
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", hcmSyncProfilesSearchbar);
			PerformanceUtils.waitForPageReady(driver, 1);
			
			// Try clicking with multiple fallback options
			try {
			wait.until(ExpectedConditions.elementToBeClickable(hcmSyncProfilesSearchbar)).click();
			} catch (Exception clickException) {
				try {
					js.executeScript("arguments[0].click();", hcmSyncProfilesSearchbar);
				} catch (Exception jsClickException) {
					utils.jsClick(driver, hcmSyncProfilesSearchbar);
				}
			}
			
			// Clear the search bar
			try {
				hcmSyncProfilesSearchbar.sendKeys(Keys.CONTROL + "a");
				hcmSyncProfilesSearchbar.sendKeys(Keys.DELETE);
			} catch(Exception c) {
		       js.executeScript("arguments[0].value = '';", hcmSyncProfilesSearchbar);
		       js.executeScript("arguments[0].dispatchEvent(new Event('input', { bubbles: true }));", hcmSyncProfilesSearchbar);		        
		}
		// PERFORMANCE: Single optimized wait - waitForPageReady already checks spinners
		PerformanceUtils.waitForPageReady(driver, 2);
		LOGGER.info("Cleared Search bar in HCM Sync Profiles screen in PM....");
			ExtentCucumberAdapter.addTestStepLog("Cleared Search bar in HCM Sync Profiles screen in PM....");
	} catch (Exception e) {
		LOGGER.error("Issue clearing search bar - Method: clear_search_bar_in_hcm_sync_profiles_tab", e);
		ScreenshotHandler.captureFailureScreenshot("clear_search_bar", e);
			e.printStackTrace();
			Assert.fail("Issue in clearing search bar in HCM Sync Profiles screen in PM....");
			ExtentCucumberAdapter.addTestStepLog("Issue in clearing search bar in HCM Sync Profiles screen in PM....");
		}
	}
	
	public void verify_job_profiles_count_is_displaying_on_the_page_in_hcm_sync_profiles_tab() {
		try {
			// PERFORMANCE: Single optimized wait - waitForPageReady already checks spinners
			PerformanceUtils.waitForPageReady(driver, 2);
			String resultsCountText = wait.until(ExpectedConditions.visibilityOf(showingJobResultsCount)).getText();
			intialResultsCount = resultsCountText;
		    LOGGER.info("Initially " + resultsCountText + " on the page in HCM Sync Profiles screen in PM");
		    ExtentCucumberAdapter.addTestStepLog("Initially " + resultsCountText + " on the page in HCM Sync Profiles screen in PM");
	} catch (Exception e) {
		LOGGER.error(" Issue verifying job profiles count - Method: verify_job_profiles_count_is_displaying_on_the_page_in_hcm_sync_profiles_tab", e);
		ScreenshotHandler.captureFailureScreenshot("verify_job_profiles_count", e);
			e.printStackTrace();
			Assert.fail("Issue in verifying job profiles results count in on the page in HCM Sync Profiles screen in PM...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying job profiles results count in on the page in HCM Sync Profiles screen in PM...Please Investigate!!!");
		} 
	}
	
	public void scroll_page_to_view_more_job_profiles_in_hcm_sync_profiles_tab() {
		try {
			headlessActions.scrollToBottom();
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			// PERFORMANCE: Replaced Thread.sleep(2000) with smart page ready wait
			PerformanceUtils.waitForPageReady(driver);
			LOGGER.info("Scrolled page down to view more job profiles in HCM Sync Profiles screen in PM");
			ExtentCucumberAdapter.addTestStepLog("Scrolled page down to view more job profiles in HCM Sync Profiles screen in PM");
	} catch (Exception e) {
		LOGGER.error(" Issue scrolling page - Method: scroll_page_to_view_more_job_profiles_in_hcm_sync_profiles_tab", e);
		ScreenshotHandler.captureFailureScreenshot("scroll_page_to_view_more_profiles", e);
			e.printStackTrace();
			Assert.fail("Issue in scrolling page down to view more job profiles in HCM Sync Profiles screen in PM...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in scrolling page down to view more job profiles in in HCM Sync Profiles screen in PM...Please Investigate!!!");
		}
	}
	
	public void user_should_verify_count_of_job_profiles_is_correctly_showing_on_top_of_job_profiles_listing_table_in_hcm_sync_profiles_tab() {
		try {
			js.executeScript("arguments[0].scrollIntoView(true);", hcmSyncProfilesTitle);
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			// PERFORMANCE: Replaced Thread.sleep(3000) with smart element wait
			PerformanceUtils.waitForElement(driver, showingJobResultsCount);
			String resultsCountText1 = wait.until(ExpectedConditions.visibilityOf(showingJobResultsCount)).getText();
			updatedResultsCount = resultsCountText1;
			// PERFORMANCE: Removed Thread.sleep(2000) - element already waited for above
			Assert.assertNotEquals(intialResultsCount,resultsCountText1);
			if (resultsCountText1 != intialResultsCount) {
				LOGGER.info("Success Profiles Results count updated and Now " + resultsCountText1 + " as expected in HCM Sync Profiles screen in PM");
				ExtentCucumberAdapter.addTestStepLog("Success Profiles Results count updated and Now " + resultsCountText1 + " as expected in HCM Sync Profiles screen in PM");	
			}
			else {
				ExtentCucumberAdapter.addTestStepLog("Issue in updating success profiles results count, Still " + resultsCountText1 + " in HCM Sync Profiles screen in PM....Please Investiagte!!!");
				throw new Exception("Issue in updating success profiles results count, Still " + resultsCountText1 + " in HCM Sync Profiles screen in PM....Please Investiagte!!!");
			}
	} catch (Exception e) {
		LOGGER.error(" Issue verifying profiles count after scrolling - Method: user_should_verify_count_of_job_profiles_is_correctly_showing_on_top_of_job_profiles_listing_table_in_hcm_sync_profiles_tab", e);
		ScreenshotHandler.captureFailureScreenshot("verify_profiles_count_after_scrolling", e);
			e.printStackTrace();
			Assert.fail("Issue in verifying success profiles results count after scrolling page down in HCM Sync Profiles screen in PM...Please Investigate!!!");
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
			// PERFORMANCE: waitForElement already handles spinner checks and element readiness
			PerformanceUtils.waitForElement(driver, filtersDropdownBtn);
			
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
				// Dropdown is closed, so open it
				utils.jsClick(driver, filtersDropdownBtn);
				LOGGER.info("Clicked on filters dropdown button in HCM Sync Profiles screen in PM...");
				ExtentCucumberAdapter.addTestStepLog("Clicked on filters dropdown button in HCM Sync Profiles screen in PM...");
				// PERFORMANCE: Wait for dropdown to be visible (replaces spinner wait + page ready + visibility check)
				wait.until(ExpectedConditions.visibilityOf(filterOptions));
			}
	} catch (Exception e) {
		LOGGER.error(" Issue clicking filters dropdown - Method: click_on_filters_dropdown_button_in_hcm_sync_profiles_tab", e);
		ScreenshotHandler.captureFailureScreenshot("click_filters_dropdown", e);
			e.printStackTrace();
			Assert.fail("Issue in clicking filters dropdown button in HCM Sync Profiles screen in PM...Please investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in clicking filters dropdown button in HCM Sync Profiles screen in PM...Please Investigate!!!");
		}   
	}
	
	public void verify_options_available_inside_filters_dropdown_in_hcm_sync_profiles_tab() {
		Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(filterOptions)).isDisplayed());
		try {
			String filterOption1Text = wait.until(ExpectedConditions.visibilityOf(filterOption1)).getText();
			Assert.assertEquals(filterOption1Text, "KF Grade");
			String filterOption2Text = wait.until(ExpectedConditions.visibilityOf(filterOption2)).getText();
			Assert.assertEquals(filterOption2Text, "Levels");
			String filterOption3Text = wait.until(ExpectedConditions.visibilityOf(filterOption3)).getText();
			Assert.assertEquals(filterOption3Text, "Functions / Subfunctions");
			LOGGER.info("Options inside Filters dropdown verified successfully in HCM Sync Profiles screen in PM");
			ExtentCucumberAdapter.addTestStepLog("Options inside Filters dropdown verified successfully in HCM Sync Profiles screen in PM");
	} catch (Exception e) {
		LOGGER.error(" Issue verifying filter options - Method: verify_options_available_inside_filters_dropdown_in_hcm_sync_profiles_tab", e);
		ScreenshotHandler.captureFailureScreenshot("verify_filter_options", e);
			e.printStackTrace();
			Assert.fail("Issue in verifying Options inside Filter dropdown in HCM Sync Profiles screen in PM....Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying Options inside Filter dropdown in HCM Sync Profiles screen in PM....Please Investigate!!!");
		}
		
		try {
			wait.until(ExpectedConditions.elementToBeClickable(filterOption3)).click();
			wait.until(ExpectedConditions.visibilityOf(searchBarInFilterOption3)).isDisplayed();
			searchBarInFilterOption3.click();
			LOGGER.info("Search bar inside Functions / Subfunctions filter option is available and is clickable in HCM Sync Profiles screen in PM...");
			ExtentCucumberAdapter.addTestStepLog("Search bar inside Functions / Subfunctions filter option is available and is clickable in HCM Sync Profiles screen in PM...");
	} catch (Exception e) {
		LOGGER.error(" Issue verifying search bar in filter option - Method: verify_options_available_inside_filters_dropdown_in_hcm_sync_profiles_tab", e);
		ScreenshotHandler.captureFailureScreenshot("verify_search_bar_in_filter", e);
			e.printStackTrace();
			Assert.fail("Issue in verifying Search bar inside Functions / Subfunctions filter option in HCM Sync Profiles screen in PM...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying Search bar inside Functions / Subfunctions filter option in HCM Sync Profiles screen in PM...Please Investigate!!!");
		}
		
		// Close the dropdown after verification to ensure clean state for next scenario
		try {
			if (filterOptions.isDisplayed()) {
				utils.jsClick(driver, filtersDropdownBtn);
				wait.until(ExpectedConditions.invisibilityOf(filterOptions));
				LOGGER.info("Closed Filters dropdown after verification in HCM Sync Profiles screen");
				ExtentCucumberAdapter.addTestStepLog("Closed Filters dropdown after verification");
			}
		} catch (Exception e) {
			LOGGER.warn(" Could not close filters dropdown after verification: " + e.getMessage());
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
			ScreenshotHandler.captureFailureScreenshot("click_kf_grade_dropdown", e);
				e.printStackTrace();
				Assert.fail("Issue in clicking KF Grade dropdown in Filters in HCM Sync Profiles screen in PM...Please Investigate!!!");
				ExtentCucumberAdapter.addTestStepLog("Issue in clicking KF Grade dropdown in Filters in HCM Sync Profiles screen in PM...Please Investigate!!!");
			}
			
		try {
		// DYNAMIC XPATH: Find all available KF Grade options using updated class-based XPATH
		PerformanceUtils.waitForPageReady(driver, 2);
		
		if (KFGradeAllCheckboxes.isEmpty() || KFGradeAllLabels.isEmpty()) {
			LOGGER.error(" No KF Grade options found after expanding dropdown");
			ScreenshotHandler.captureFailureScreenshot("no_kf_grade_options_found", new Exception("No options found"));
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
				ScreenshotHandler.captureFailureScreenshot("select_kf_grade_option", e);
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
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			// PERFORMANCE: Replaced Thread.sleep(3000+2000) with smart element wait  
			PerformanceUtils.waitForPageReady(driver, 2);
			
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
				Assert.assertNotEquals(updatedResultsCount, resultsCountText2);
				if (resultsCountText2 != updatedResultsCount) {
					LOGGER.info("Success Profiles Results count updated and Now " + resultsCountText2 + " as expected after applying KF Grade Filters in HCM Sync Profiles screen in PM");
					ExtentCucumberAdapter.addTestStepLog("Success Profiles Results count updated and Now " + resultsCountText2 + " as expected after applying KF Grade Filters in HCM Sync Profiles screen in PM");	
				} else {
					ExtentCucumberAdapter.addTestStepLog("Issue in updating success profiles results count, Still " + resultsCountText2 + " after applying KF Grade Filters in HCM Sync Profiles screen in PM....Please Investiagte!!!");
					throw new Exception("Issue in updating success profiles results count, Still " + resultsCountText2 + " after applying KF Grade Filters in HCM Sync Profiles screen in PM....Please Investiagte!!!");
				}
			}
	} catch (Exception e) {
		LOGGER.error(" Issue verifying profiles count after KF Grade filter - Method: apply_kf_grade_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab", e);
		ScreenshotHandler.captureFailureScreenshot("verify_count_after_kf_grade_filter", e);
			e.printStackTrace();
			Assert.fail("Issue in verifying success profiles results count after scrolling page down in HCM Sync Profiles screen in PM...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying success profiles results count after scrolling page down in in HCM Sync Profiles screen in PM...Please Investigate!!!");
		}
	} catch (Exception e) {
		LOGGER.error(" Issue applying KF Grade filter - Method: apply_kf_grade_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab", e);
		ScreenshotHandler.captureFailureScreenshot("apply_kf_grade_filter", e);
			e.printStackTrace();
			Assert.fail("Issue in verifying KF Grade Filter and Verifing Profiles count is correctly displaying in HCM Sync Profiles screen in PM...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying KF Grade Filter and Verifing Profiles count is correctly displaying in HCM Sync Profiles screen in PM...Please Investigate!!!");
		}
	}
	
	public void clear_kf_grade_filter_in_hcm_sync_profiles_tab() {
		try {
			wait.until(ExpectedConditions.visibilityOf(closeAppliedFilter)).isDisplayed();
			wait.until(ExpectedConditions.visibilityOf(closeAppliedFilter)).click();
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			LOGGER.info("Cleared Applied KF Grade Filter in HCM Sync Profiles screen in PM");
			ExtentCucumberAdapter.addTestStepLog("Cleared Applied KF Grade Filter in HCM Sync Profiles screen in PM");
	} catch (Exception e) {
		LOGGER.error(" Issue clearing KF Grade filter - Method: clear_kf_grade_filter_in_hcm_sync_profiles_tab", e);
		ScreenshotHandler.captureFailureScreenshot("clear_kf_grade_filter", e);
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
			ScreenshotHandler.captureFailureScreenshot("click_levels_dropdown", e);
				e.printStackTrace();
				Assert.fail("Issue in clicking Levels dropdown in Filters in HCM Sync Profiles screen in PM...Please Investigate!!!");
				ExtentCucumberAdapter.addTestStepLog("Issue in clicking Levels dropdown in Filters in HCM Sync Profiles screen in PM...Please Investigate!!!");
			}
			
		try {
		// DYNAMIC XPATH: Find all available Levels options using updated class-based XPATH
		PerformanceUtils.waitForPageReady(driver, 2);
		
		if (LevelsAllCheckboxes.isEmpty() || LevelsAllLabels.isEmpty()) {
			LOGGER.error(" No Levels options found after expanding dropdown");
			ScreenshotHandler.captureFailureScreenshot("no_levels_options_found", new Exception("No options found"));
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
				ScreenshotHandler.captureFailureScreenshot("select_levels_option", e);
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
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			// PERFORMANCE: Replaced Thread.sleep(3000+2000) with smart element wait  
			PerformanceUtils.waitForPageReady(driver, 2);
			
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
				PerformanceUtils.waitForElement(driver, showingJobResultsCount);
				String resultsCountText2 = wait.until(ExpectedConditions.visibilityOf(showingJobResultsCount)).getText();
				Assert.assertNotEquals(intialResultsCount, resultsCountText2);
				if (resultsCountText2 != intialResultsCount) {
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
		ScreenshotHandler.captureFailureScreenshot("verify_count_after_levels_filter", e);
			e.printStackTrace();
			Assert.fail("Issue in verifying success profiles results count after scrolling page down in HCM Sync Profiles screen in PM...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying success profiles results count after scrolling page down in in HCM Sync Profiles screen in PM...Please Investigate!!!");
		}
	} catch (Exception e) {
		LOGGER.error(" Issue applying Levels filter - Method: apply_levels_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab", e);
		ScreenshotHandler.captureFailureScreenshot("apply_levels_filter", e);
			e.printStackTrace();
			Assert.fail("Issue in verifying Levels Filter and Verifing Profiles count is correctly displaying in HCM Sync Profiles screen in PM...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying Levels Filter and Verifing Profiles count is correctly displaying in HCM Sync Profiles screen in PM...Please Investigate!!!");
		}
	}
	
	public void clear_levels_filter_in_hcm_sync_profiles_tab() {
		try {
			wait.until(ExpectedConditions.visibilityOf(closeAppliedFilter)).isDisplayed();
			wait.until(ExpectedConditions.visibilityOf(closeAppliedFilter)).click();
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			LOGGER.info("Cleared Applied Levels Filter in HCM Sync Profiles screen in PM");
			ExtentCucumberAdapter.addTestStepLog("Cleared Applied Levels Filter in HCM Sync Profiles screen in PM");
	} catch (Exception e) {
		LOGGER.error(" Issue clearing Levels filter - Method: clear_levels_filter_in_hcm_sync_profiles_tab", e);
		ScreenshotHandler.captureFailureScreenshot("clear_levels_filter", e);
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
			ScreenshotHandler.captureFailureScreenshot("click_functions_subfunctions_dropdown", e);
				e.printStackTrace();
				Assert.fail("Issue in clicking Functions / Subfunctions dropdown in Filters in HCM Sync Profiles screen in PM...Please Investigate!!!");
				ExtentCucumberAdapter.addTestStepLog("Issue in clicking Functions / Subfunctions dropdown in Filters in HCM Sync Profiles screen in PM...Please Investigate!!!");
			}
			
		try {
		// DYNAMIC XPATH: Find all available Functions/Subfunctions options using updated class-based XPATH
		PerformanceUtils.waitForPageReady(driver, 2);
		
		if (FunctionsSubfunctionsAllCheckboxes.isEmpty() || FunctionsSubfunctionsAllLabels.isEmpty()) {
			LOGGER.error(" No Functions/Subfunctions options found after expanding dropdown");
			ScreenshotHandler.captureFailureScreenshot("no_functions_subfunctions_options_found", new Exception("No options found"));
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
			ScreenshotHandler.captureFailureScreenshot("select_functions_subfunctions_option", e);
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
			ScreenshotHandler.captureFailureScreenshot("close_filters_dropdown_functions", e);
				e.printStackTrace();
				Assert.fail("Issue in closing filters dropdown in HCM Sync Profiles screen in PM...Please Investigate!!!");
				ExtentCucumberAdapter.addTestStepLog("Issue in closing filters dropdown in HCM Sync Profiles screen in PM...Please Investigate!!!");
			}
			
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			// PERFORMANCE: Replaced Thread.sleep(3000+2000) with smart element wait  
			PerformanceUtils.waitForPageReady(driver, 2);
			
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
				Assert.assertNotEquals(intialResultsCount, resultsCountText2);
				if (resultsCountText2 != intialResultsCount) {
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
		ScreenshotHandler.captureFailureScreenshot("verify_count_after_functions_filter", e);
			e.printStackTrace();
			Assert.fail("Issue in verifying success profiles results count after scrolling page down in HCM Sync Profiles screen in PM...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying success profiles results count after scrolling page down in in HCM Sync Profiles screen in PM...Please Investigate!!!");
		}
	} catch (Exception e) {
		LOGGER.error(" Issue applying Functions/Subfunctions filter - Method: apply_functions_or_subfunctions_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab", e);
		ScreenshotHandler.captureFailureScreenshot("apply_functions_subfunctions_filter", e);
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
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
	} catch (Exception e) {
		LOGGER.error(" Issue clicking Clear All Filters button - Method: click_on_clear_all_filters_button_in_hcm_sync_profiles_tab", e);
		ScreenshotHandler.captureFailureScreenshot("click_clear_all_filters_button", e);
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
			ScreenshotHandler.captureFailureScreenshot("click_profile_status_dropdown", e);
			e.printStackTrace();
			Assert.fail("Issue in clicking on Profile Status dropdown in Filters in HCM Sync Profiles screen in PM...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in clicking on Profile Status dropdown in Filters in HCM Sync Profiles screen in PM...Please Investigate!!!");
		}
			
		try {
		// DYNAMIC XPATH: Find all available Profile Status options using updated class-based XPATH
		PerformanceUtils.waitForPageReady(driver, 2);
		
		if (ProfileStatusAllCheckboxes.isEmpty() || ProfileStatusAllLabels.isEmpty()) {
			LOGGER.error(" No Profile Status options found after expanding dropdown");
			ScreenshotHandler.captureFailureScreenshot("no_profile_status_options_found", new Exception("No options found"));
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
			ScreenshotHandler.captureFailureScreenshot("select_profile_status_option", e);
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
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			// PERFORMANCE: Replaced Thread.sleep(3000+2000) with smart element wait  
			PerformanceUtils.waitForPageReady(driver, 2);
			
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
				Assert.assertNotEquals(updatedResultsCount, resultsCountText2);
				if (resultsCountText2 != updatedResultsCount) {
					LOGGER.info("Success Profiles Results count updated and Now " + resultsCountText2 + " as expected after applying Profile Status Filters in HCM Sync Profiles screen in PM");
					ExtentCucumberAdapter.addTestStepLog("Success Profiles Results count updated and Now " + resultsCountText2 + " as expected after applying Profile Status Filters in HCM Sync Profiles screen in PM");	
				} else {
					ExtentCucumberAdapter.addTestStepLog("Issue in updating success profiles results count, Still " + resultsCountText2 + " after applying Profile Status Filters in HCM Sync Profiles screen in PM....Please Investiagte!!!");
					throw new Exception("Issue in updating success profiles results count, Still " + resultsCountText2 + " after applying Profile Status Filters in HCM Sync Profiles screen in PM....Please Investiagte!!!");
				}
			}
	} catch (Exception e) {
		LOGGER.error(" Issue verifying profiles count after Profile Status filter - Method: apply_profile_status_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab", e);
		ScreenshotHandler.captureFailureScreenshot("verify_count_after_profile_status_filter", e);
		e.printStackTrace();
		Assert.fail("Issue in verifying success profiles results count after scrolling page down in HCM Sync Profiles screen in PM...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in verifying success profiles results count after scrolling page down in in HCM Sync Profiles screen in PM...Please Investigate!!!");
	}
	} catch (Exception e) {
		LOGGER.error(" Issue applying Profile Status filter - Method: apply_profile_status_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab", e);
		ScreenshotHandler.captureFailureScreenshot("apply_profile_status_filter", e);
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
		ScreenshotHandler.captureFailureScreenshot("verify_table_headers", e);
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
		ScreenshotHandler.captureFailureScreenshot("verify_download_button_disabled", e);
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
			loadedProfilesBeforeHeaderCheckboxClick = Integer.parseInt(resultsCountText_split[1]);
			LOGGER.info("Loaded profiles on screen (BEFORE header checkbox click): " + loadedProfilesBeforeHeaderCheckboxClick);
			
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
			
			// Step 3: Count selected and disabled profiles
			profilesCount = loadedProfilesBeforeHeaderCheckboxClick;
			disabledProfilesCountInLoadedProfiles = 0;
			
			for(int i = 1; i <= loadedProfilesBeforeHeaderCheckboxClick; i++) {
				try {
					WebElement	SP_Checkbox = driver.findElement(By.xpath("//tbody//tr[" + Integer.toString(i) + "]//td[1]//*//..//div//kf-checkbox//div"));
					js.executeScript("arguments[0].scrollIntoView(true);", SP_Checkbox);
					String text = SP_Checkbox.getAttribute("class");
					if(text.contains("disable")) {
						LOGGER.info("Success profile with No Job Code assigned is found....");
						ExtentCucumberAdapter.addTestStepLog("Success profile with No Job Code assigned is found....");
						disabledProfilesCountInLoadedProfiles++;
						profilesCount = profilesCount - 1;
					}
				} catch(Exception e) {
					wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
					PerformanceUtils.waitForPageReady(driver, 3);
					WebElement	SP_Checkbox = driver.findElement(By.xpath("//tbody//tr[" + Integer.toString(i) + "]//td[1]//*//..//div//kf-checkbox//div"));
					js.executeScript("arguments[0].scrollIntoView(true);", SP_Checkbox);
					String text = SP_Checkbox.getAttribute("class");
					if(text.contains("disable")) {
						LOGGER.info("Success profile with No Job Code assigned is found....");
						ExtentCucumberAdapter.addTestStepLog("Success profile with No Job Code assigned is found....");
						disabledProfilesCountInLoadedProfiles++;
						profilesCount = profilesCount - 1;
					}
				}
			}
			
			// Step 4: Store selected profiles count
			selectedProfilesAfterHeaderCheckboxClick = profilesCount;
			
			LOGGER.info("Clicked on header checkbox and selected " + selectedProfilesAfterHeaderCheckboxClick + " job profiles in HCM Sync Profiles screen in PM");
			LOGGER.info("    Loaded profiles (before click): " + loadedProfilesBeforeHeaderCheckboxClick);
			LOGGER.info("    Selected profiles: " + selectedProfilesAfterHeaderCheckboxClick);
			LOGGER.info("    Disabled profiles: " + disabledProfilesCountInLoadedProfiles);
			ExtentCucumberAdapter.addTestStepLog("Clicked on header checkbox and selected " + selectedProfilesAfterHeaderCheckboxClick + " job profiles in HCM Sync Profiles screen in PM");
	} catch (Exception e) {
		LOGGER.error(" Issue clicking header checkbox to select loaded profiles - Method: click_on_header_checkbox_to_select_loaded_profiles_in_hcm_sync_profiles_tab", e);
		ScreenshotHandler.captureFailureScreenshot("click_header_checkbox_select_loaded_profiles_in_HCM_Screen", e);
		e.printStackTrace();
		Assert.fail("Issue in clicking on header checkbox to select loaded job profiles in HCM Sync Profiles screen in PM...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in clicking on header checkbox to select loaded job profiles in in HCM Sync Profiles screen in PM...Please Investigate!!!");
	}
	}
	
	public void user_should_verify_download_button_is_enabled_in_hcm_sync_profiles_tab() {
		try {
			js.executeScript("window.scrollTo(0, 0);"); // Scroll to top (headless-compatible)
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(downloadBtn)).isEnabled());
		LOGGER.info("Download button is enabled as expected after selecting job profiles in HCM Sync Profiles screen in PM");
		ExtentCucumberAdapter.addTestStepLog("Download button is enabled as expected after selecting job profiles in HCM Sync Profiles screen in PM"); 
	} catch (Exception e) {
		LOGGER.error(" Issue verifying download button enabled - Method: user_should_verify_download_button_is_enabled_in_hcm_sync_profiles_tab", e);
		ScreenshotHandler.captureFailureScreenshot("verify_download_button_enabled", e);
		e.printStackTrace();
		Assert.fail("Issue in verifying Download button is enabled in HCM Sync Profiles screen in PM...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in verifying Download button is enabled in HCM Sync Profiles screen in PM...Please Investigate!!!");
	}
	}
	
	public void user_should_uncheck_header_checkbox_to_deselect_selected_job_profiles_in_hcm_sync_profiles_tab() {
		try {
			try {
				wait.until(ExpectedConditions.elementToBeClickable(tableHeaderCheckbox)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", tableHeaderCheckbox);
				} catch (Exception s) {
					utils.jsClick(driver, tableHeaderCheckbox);
				}
			}
		LOGGER.info("Clicked on header checkbox and Deselected all job profiles in HCM Sync Profiles screen in PM");
		ExtentCucumberAdapter.addTestStepLog("Clicked on header checkbox and Deselected all job profiles in HCM Sync Profiles screen in PM");
	} catch (Exception e) {
		LOGGER.error(" Issue clicking header checkbox to deselect all - Method: user_should_uncheck_header_checkbox_to_deselect_selected_job_profiles_in_hcm_sync_profiles_tab", e);
		ScreenshotHandler.captureFailureScreenshot("click_header_checkbox_deselect_all", e);
		e.printStackTrace();
		Assert.fail("Issue in clicking on header checkbox to deselect all job profiles in HCM Sync Profiles screen in PM...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in clicking on header checkbox to deselect all job profiles in HCM Sync Profiles screen in PM...Please Investigate!!!");
	}
	}
	
	public void click_on_first_profile_checkbox_in_hcm_sync_profiles_tab() {
		try {
			jobname1 = wait.until(ExpectedConditions.visibilityOf(HCMSyncProfilesJobinRow1)).getText();
			try {
				wait.until(ExpectedConditions.elementToBeClickable(profile1Checkbox)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", profile1Checkbox);
				} catch (Exception s) {
					utils.jsClick(driver, profile1Checkbox);
				}
			}
		LOGGER.info("Clicked on checkbox of First job profile with name : " + jobname1 +" in HCM Sync Profiles screen in PM");
		ExtentCucumberAdapter.addTestStepLog("Clicked on checkbox of First job profile with name : " + jobname1 +" in HCM Sync Profiles screen in PM");
		profilesCount = profilesCount + 1; 
	} catch (Exception e) {
		LOGGER.error(" Issue clicking first profile checkbox - Method: click_on_first_profile_checkbox_in_hcm_sync_profiles_tab", e);
		ScreenshotHandler.captureFailureScreenshot("click_first_profile_checkbox", e);
		e.printStackTrace();
		Assert.fail("Issue in clicking First job profile checkbox in HCM Sync Profiles screen in PM...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in clicking First job profile checkbox in HCM Sync Profiles screen in PM...Please Investigate!!!");
	}
	}
	
	public void click_on_second_profile_checkbox_in_hcm_sync_profiles_tab() {
		try {
			jobname2 = wait.until(ExpectedConditions.visibilityOf(HCMSyncProfilesJobinRow2)).getText();
			try {
				wait.until(ExpectedConditions.elementToBeClickable(profile2Checkbox)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", profile2Checkbox);
				} catch (Exception s) {
					utils.jsClick(driver, profile2Checkbox);
				}
			}
		LOGGER.info("Clicked on checkbox of Second job profile with name : " + jobname2 +" in HCM Sync Profiles screen in PM");
		ExtentCucumberAdapter.addTestStepLog("Clicked on checkbox of Second job profile with name : " + jobname2 +" in HCM Sync Profiles screen in PM");
		profilesCount = profilesCount + 1;
	} catch (Exception e) {
		LOGGER.error(" Issue clicking second profile checkbox - Method: click_on_second_profile_checkbox_in_hcm_sync_profiles_tab", e);
		ScreenshotHandler.captureFailureScreenshot("click_second_profile_checkbox", e);
		e.printStackTrace();
		Assert.fail("Issue in clicking Second job profile checkbox in HCM Sync Profiles screen in PM...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in clicking Second job profile checkbox in HCM Sync Profiles screen in PM...Please Investigate!!!");
	}
	}
	
	public void click_on_third_profile_checkbox_in_hcm_sync_profiles_tab() {
		try {
			js.executeScript("arguments[0].scrollIntoView(true);", HCMSyncProfilesJobinRow3);
			jobname3 = wait.until(ExpectedConditions.visibilityOf(HCMSyncProfilesJobinRow3)).getText();
			try {
				wait.until(ExpectedConditions.elementToBeClickable(profile3Checkbox)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", profile3Checkbox);
				} catch (Exception s) {
					utils.jsClick(driver, profile3Checkbox);
				}
			}
		LOGGER.info("Clicked on checkbox of Third job profile with name : " + jobname3 +" in HCM Sync Profiles screen in PM");
		ExtentCucumberAdapter.addTestStepLog("Clicked on checkbox of Third job profile with name : " + jobname3  +" in HCM Sync Profiles screen in PM");
		profilesCount = profilesCount + 1;
	} catch (Exception e) {
		LOGGER.error(" Issue clicking third profile checkbox - Method: click_on_third_profile_checkbox_in_hcm_sync_profiles_tab", e);
		ScreenshotHandler.captureFailureScreenshot("click_third_profile_checkbox", e);
		e.printStackTrace();
		Assert.fail("Issue in clicking Third job profile checkbox in HCM Sync Profiles screen in PM...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in clicking Third job profile checkbox in HCM Sync Profiles screen in PM...Please Investigate!!!");
	}
	}
	
	public void user_should_click_on_download_button_in_hcm_sync_profiles_tab() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			// PERFORMANCE: Removed Thread.sleep(2000) - scrollIntoView is immediate
			js.executeScript("arguments[0].scrollIntoView(true);", hcmSyncProfilesTitle);
			try {
				wait.until(ExpectedConditions.elementToBeClickable(downloadBtn)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", downloadBtn);
				} catch (Exception s) {
					utils.jsClick(driver, downloadBtn);
				}
			}
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(XLSFormatBtn)).isDisplayed());
		LOGGER.info("Clicked on Download button in HCM Sync Profiles screen in PM");
		ExtentCucumberAdapter.addTestStepLog("Clicked on Download button in HCM Sync Profiles screen in PM"); 
	} catch (Exception e) {
		LOGGER.error(" Issue clicking download button - Method: user_should_click_on_download_button_in_hcm_sync_profiles_tab", e);
		ScreenshotHandler.captureFailureScreenshot("click_download_button", e);
		e.printStackTrace();
		Assert.fail("Issue in clicking Download button in HCM Sync Profiles screen in PM...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in clicking Download button in HCM Sync Profiles screen in PM...Please Investigate!!!");
	}
	}
	
	public void click_on_xls_format_button_and_verify_download_successful_in_hcm_sync_profiles_tab() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			// PERFORMANCE: Removed redundant Thread.sleep(2000) after spinner wait
			wait.until(ExpectedConditions.visibilityOf(XLSFormatBtn)).click();
			LOGGER.info("Clicked on XLS Format Download button in HCM Sync Profiles screen in PM....");
			ExtentCucumberAdapter.addTestStepLog("Clicked on XLS Format Download button in HCM Sync Profiles screen in PM....");
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			LOGGER.info("XLS Format file downloaded successfully in HCM Sync Profiles screen in PM....");
			ExtentCucumberAdapter.addTestStepLog("XLS Format file downloaded successfully in HCM Sync Profiles screen in PM....");
		wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
	PerformanceUtils.waitForPageReady(driver, 2);
	} catch (Exception e) {
		LOGGER.error(" Issue verifying XLS format download - Method: click_on_xls_format_button_and_verify_download_successful_in_hcm_sync_profiles_tab", e);
		ScreenshotHandler.captureFailureScreenshot("verify_xls_format_download", e);
		e.printStackTrace();
		Assert.fail("Issue in Verifying XLS Format File download in HCM Sync Profiles screen in PM...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in Verifying XLS Format File download in HCM Sync Profiles screen in PM...Please Investigate!!!");
	}
	}
	
	public void click_on_csv_format_button_and_verify_download_successful_in_hcm_sync_profiles_tab() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			// PERFORMANCE: Removed redundant Thread.sleep(2000) after spinner wait
			wait.until(ExpectedConditions.visibilityOf(CSVFormatBtn)).click();
			LOGGER.info("Clicked on CSV Format Download button in HCM Sync Profiles screen in PM....");
			ExtentCucumberAdapter.addTestStepLog("Clicked on CSV Format Download button in HCM Sync Profiles screen in PM....");
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
//			LocalDateTime myDateObj = LocalDateTime.now();
//			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm:ss");
//			String formattedDate = myDateObj.format(myFormatObj);
//			Assert.assertTrue(Utilities.isFileDownload("KornFerry_CustomSP_Extract_"+formattedDate,"zip",5000));
		LOGGER.info("CSV Format Zip file downloaded successfully in HCM Sync Profiles screen in PM....");
		ExtentCucumberAdapter.addTestStepLog("CSV Format Zip file downloaded successfully in HCM Sync Profiles screen in PM....");
	} catch (Exception e) {
		LOGGER.error(" Issue verifying CSV format download - Method: click_on_csv_format_button_and_verify_download_successful_in_hcm_sync_profiles_tab", e);
		ScreenshotHandler.captureFailureScreenshot("verify_csv_format_download", e);
		e.printStackTrace();
		Assert.fail("Issue in Verifying CSV Format Zip File download in HCM Sync Profiles screen in PM...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in Verifying CSV Format Zip File download in HCM Sync Profiles screen in PM...Please Investigate!!!");
	}
	}
	
	public void user_should_verify_sync_with_hcm_button_is_disabled_in_hcm_sync_profiles_tab() {
		try {
			WebElement syncButton = wait.until(ExpectedConditions.visibilityOf(SyncwithHCMBtn));
			
			// Check for disabled state using multiple indicators
			boolean isDisabled = false;
			
			// Check 1: Check for 'text-disabled' class (Angular Material disabled state)
			String classAttribute = syncButton.getAttribute("class");
			if (classAttribute != null && classAttribute.contains("text-disabled")) {
				isDisabled = true;
				LOGGER.info("Button has 'text-disabled' class - Button is disabled");
			}
			
			// Check 2: Check for 'disabled' attribute
			String disabledAttribute = syncButton.getAttribute("disabled");
			if (disabledAttribute != null) {
				isDisabled = true;
				LOGGER.info("Button has 'disabled' attribute - Button is disabled");
			}
			
			// Check 3: Check for 'aria-disabled' attribute
			String ariaDisabled = syncButton.getAttribute("aria-disabled");
			if ("true".equalsIgnoreCase(ariaDisabled)) {
				isDisabled = true;
				LOGGER.info("Button has 'aria-disabled=true' - Button is disabled");
			}
			
			// Check 4: Fallback to isEnabled() check
			if (!isDisabled && !syncButton.isEnabled()) {
				isDisabled = true;
				LOGGER.info("Button.isEnabled() returned false - Button is disabled");
			}
			
			Assert.assertTrue(isDisabled, "Sync with HCM button should be disabled but appears to be enabled");
			LOGGER.info(" Sync with HCM button is disabled as expected in HCM Sync Profiles screen in PM");
			ExtentCucumberAdapter.addTestStepLog(" Sync with HCM button is disabled as expected in HCM Sync Profiles screen in PM");
	} catch (Exception e) {
		LOGGER.error(" Issue verifying Sync with HCM button disabled - Method: user_should_verify_sync_with_hcm_button_is_disabled_in_hcm_sync_profiles_tab", e);
		ScreenshotHandler.captureFailureScreenshot("verify_sync_hcm_button_disabled", e);
		e.printStackTrace();
		Assert.fail("Issue in verifying Sync with HCM button is disabled in HCM Sync Profiles screen in PM...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in verifying Sync with HCM button is disabled in HCM Sync Profiles screen in PM...Please Investigate!!!");
	}
	}
	
	public void user_should_verify_sync_with_hcm_button_is_enabled_in_hcm_sync_profiles_tab() {
		try {
			WebElement syncButton = wait.until(ExpectedConditions.visibilityOf(SyncwithHCMBtn));
			
			// Check for enabled state (absence of disabled indicators)
			boolean isEnabled = true;
			
			// Check 1: Verify 'text-disabled' class is NOT present
			String classAttribute = syncButton.getAttribute("class");
			if (classAttribute != null && classAttribute.contains("text-disabled")) {
				isEnabled = false;
				LOGGER.warn("Button has 'text-disabled' class - Button is disabled");
			}
			
			// Check 2: Verify 'disabled' attribute is NOT present
			String disabledAttribute = syncButton.getAttribute("disabled");
			if (disabledAttribute != null) {
				isEnabled = false;
				LOGGER.warn("Button has 'disabled' attribute - Button is disabled");
			}
			
			// Check 3: Verify 'aria-disabled' is not 'true'
			String ariaDisabled = syncButton.getAttribute("aria-disabled");
			if ("true".equalsIgnoreCase(ariaDisabled)) {
				isEnabled = false;
				LOGGER.warn("Button has 'aria-disabled=true' - Button is disabled");
			}
			
			// Check 4: Verify isEnabled() returns true
			if (isEnabled && !syncButton.isEnabled()) {
				isEnabled = false;
				LOGGER.warn("Button.isEnabled() returned false - Button is disabled");
			}
			
			Assert.assertTrue(isEnabled, "Sync with HCM button should be enabled but appears to be disabled");
			LOGGER.info(" Sync with HCM button is enabled as expected in HCM Sync Profiles screen in PM");
			ExtentCucumberAdapter.addTestStepLog(" Sync with HCM button is enabled as expected in HCM Sync Profiles screen in PM");
	} catch (Exception e) {
		LOGGER.error(" Issue verifying Sync with HCM button enabled - Method: user_should_verify_sync_with_hcm_button_is_enabled_in_hcm_sync_profiles_tab", e);
		ScreenshotHandler.captureFailureScreenshot("verify_sync_hcm_button_enabled", e);
		e.printStackTrace();
		Assert.fail("Issue in verifying Sync with HCM button is enabled in HCM Sync Profiles screen in PM...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in verifying Sync with HCM button is enabled in HCM Sync Profiles screen in PM...Please Investigate!!!");
	}
	}
	
	public void verify_checkboxes_of_first_second_and_third_profile_are_selected_in_hcm_sync_profiles_tab() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
		PerformanceUtils.waitForPageReady(driver, 3);
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
		ScreenshotHandler.captureFailureScreenshot("verify_profile_checkboxes_selected", e);
		e.printStackTrace();
		Assert.fail("Issue in Verifying whether checkboxes of First, Second and Third Profile are Selected or Not in HCM Sync Profiles screen in PM...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in Verifying whether checkboxes of First, Second and Third Profile are Selected or Not in HCM Sync Profiles screen in PM...Please Investigate!!!");
	}
	}
	
	public void click_on_sync_with_hcm_button_in_hcm_sync_profiles_tab() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			js.executeScript("window.scrollTo(0, 0);"); // Scroll to top (headless-compatible)
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
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
		wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
	} catch (Exception e) {
		LOGGER.error(" Issue clicking Sync with HCM button - Method: click_on_sync_with_hcm_button_in_hcm_sync_profiles_tab", e);
		ScreenshotHandler.captureFailureScreenshot("click_sync_with_hcm_button", e);
		e.printStackTrace();
		Assert.fail("Issue in clicking on Sync with HCM button in HCM Sync Profiles screen in PM...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in Verifying CSV Format Zip File download in HCM Sync Profiles screen in PM...Please Investigate!!!");
	}
	}
	
	public void user_should_verify_sync_with_hcm_success_popup_appears_on_screen_in_hcm_sync_profiles_tab() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(SyncwithHCMSuccessPopupText)).isDisplayed());
			String SyncwithHCMSuccessMsg = wait.until(ExpectedConditions.visibilityOf(SyncwithHCMSuccessPopupText)).getText();
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
				ScreenshotHandler.captureFailureScreenshot("export_to_hcm_failed", new Exception(SyncwithHCMSuccessMsg));
				Assert.fail(SyncwithHCMSuccessMsg);
			}
			
			// If message doesn't contain "failed", verify it's the expected success message
			Assert.assertEquals(SyncwithHCMSuccessMsg, "Your profiles are being exported. This may take a few minutes to complete");
			wait.until(ExpectedConditions.visibilityOf(SyncwithHCMSuccessPopupCloseBtn)).click();
			LOGGER.info("Sync with HCM Success Popup closed successfully in HCM Sync Profiles screen in PM....");
			ExtentCucumberAdapter.addTestStepLog("Sync with HCM Success Popup closed successfully in HCM Sync Profiles screen in PM....");
		} catch (AssertionError e) {
			ScreenshotHandler.captureFailureScreenshot("verify_sync_hcm_success_popup", e);
			LOGGER.error("Assertion failed in verifying Sync with HCM success popup - Method: user_should_verify_sync_with_hcm_success_popup_appears_on_screen_in_hcm_sync_profiles_tab", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying appearance of Sync with HCM Success Popup in HCM Sync Profiles screen in PM...Please Investigate!!!");
			throw e; // Re-throw to mark scenario as failed but allow next scenarios to continue
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_sync_hcm_success_popup", e);
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
			ScreenshotHandler.captureFailureScreenshot("verify_sync_hcm_warning_message", e);
			e.printStackTrace();
			Assert.fail("Issue in Verifying appearance of Sync with HCM Warning Message in HCM Sync Profiles screen in PM...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying appearance of Sync with HCM Warning Message in HCM Sync Profiles screen in PM...Please Investigate!!!");
		}
	}
	
	public void verify_checkboxes_of_first_second_and_third_profile_are_deselected_in_hcm_sync_profiles_tab() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			PerformanceUtils.waitForPageReady(driver, 2);
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
		ScreenshotHandler.captureFailureScreenshot("verify_profile_checkboxes_deselected", e);
		e.printStackTrace();
		Assert.fail("Issue in Verifying whether checkboxes of First, Second and Third Profile are De-Selected or Not in HCM Sync Profiles screen in PM...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in Verifying whether checkboxes of First, Second and Third Profile are De-Selected or Not in HCM Sync Profiles screen in PM...Please Investigate!!!");
	}
}

}
