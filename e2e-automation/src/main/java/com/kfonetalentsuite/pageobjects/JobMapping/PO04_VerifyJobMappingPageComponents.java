package com.kfonetalentsuite.pageobjects.JobMapping;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.ScreenshotHandler;
import com.kfonetalentsuite.utils.JobMapping.Utilities;
import com.kfonetalentsuite.webdriverManager.DriverManager;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

public class PO04_VerifyJobMappingPageComponents {
	WebDriver driver = DriverManager.getDriver();	

	protected static final Logger LOGGER = (Logger) LogManager.getLogger();
	PO04_VerifyJobMappingPageComponents verifyJobMappingPageComponents;
	
	static String expectedPageTitle = "Korn Ferry Digital";
	static String expectedTitleHeader = "Review and Publish Your Matched Job Profiles";
	
	// Dynamic search substrings with fallback options
	// The system will try each substring in order until results are found
	public static String[] SEARCH_SUBSTRING_OPTIONS = {"Ac", "Ma", "An", "Sa", "En", "Ad", "As", "Co", "Te", "Di"};
	public static String jobnamesubstring = "Ac"; // Will be set dynamically based on which substring returns results
	
	// Static variables to store extracted job data
	public static String orgJobNameInRow1;
	public static String orgJobCodeInRow1;
	public static String orgJobGradeInRow1;
	public static String orgJobFunctionInRow1;
	public static String orgJobDepartmentInRow1;
	public static String intialResultsCount;
	public static String updatedResultsCount;
	public static String orgJobNameInRow2;
	public static String matchedSuccessPrflName;
	
	// Variables for Select Loaded Profiles validation (similar to Feature 33 in HCM Sync)
	public static int loadedProfilesBeforeHeaderCheckboxClick = 0; // Profiles loaded on screen BEFORE clicking header checkbox
	public static int selectedProfilesAfterHeaderCheckboxClick = 0; // Profiles actually selected (enabled profiles)
	public static int disabledProfilesCountInLoadedProfiles = 0; // Disabled profiles (cannot be selected)


	public PO04_VerifyJobMappingPageComponents() throws IOException {
		PageFactory.initElements(driver, this);
	}

	WebDriverWait wait = DriverManager.getWait();
	Utilities utils = new Utilities();
	JavascriptExecutor js = (JavascriptExecutor) driver;
	
	//XPATHs
	@FindBy(xpath = "//*[@class='blocking-loader']//img")
	@CacheLookup
	WebElement pageLoadSpinner1;
	
	@FindBy(xpath = "//div[@data-testid='loader']//img")
	@CacheLookup
	WebElement pageLoadSpinner2;
	
	@FindBy(xpath = "//h2[contains(text(),'JOB MAPPING')]")
	@CacheLookup
	public WebElement mainHeader;
	
	@FindBy(xpath = "//div[@id='org-job-container']")
	@CacheLookup
	public WebElement jobMappingPageContainer;  // More reliable element for page detection
	
	@FindBy(xpath = "//div[@id='page-heading']//h1")
	@CacheLookup
	public WebElement pageTitleHeader;
	
	@FindBy(xpath = "//div[@id='page-title']//p[1]")
	@CacheLookup
	public WebElement pageTitleDesc;
	
	@FindBy(xpath = "//div[@id='page-title']//p[2]")
	@CacheLookup
	public WebElement importedDataInfo;
	
	@FindBy(xpath = "//input[contains(@id,'search-job-title')]")
	@CacheLookup
	public WebElement searchBar;
	
	@FindBy(xpath = "//tbody//tr[1]//td[2]//div[contains(text(),'(')]") 
	@CacheLookup
	public WebElement jobNameofProfile1;
	
	@FindBy(xpath = "//tbody//tr[1]//td[3]//div[1]")
	@CacheLookup
	public WebElement jobGradeofProfile1;
	
	@FindBy(xpath = "//tbody//tr[2]//div//span[2]")
	@CacheLookup
	public WebElement jobFunctionofProfile1;
	
	@FindBy(xpath = "//tbody//tr[1]//td[4]//div")
	@CacheLookup
	public WebElement jobDepartmentofProfile1;

	@FindBy(xpath = "//tbody//tr[4]//td[2]//div[contains(text(),'(')]")
	@CacheLookup
	public WebElement jobNameofProfile2;
	
	@FindBy(xpath = "//div[@id='kf-job-container']//div//table//tbody//tr[1]//td[1]//div")
	@CacheLookup
	public WebElement job1LinkedMatchedProfile;
	
	@FindBy(xpath = "//button[@id='close-profile-summary']")
	@CacheLookup
	public WebElement profileDetailsPopupCloseBtn;
	
	@FindBy(xpath = "//h2[@id='summary-modal']")
	@CacheLookup
	public WebElement profileDetailsPopupHeader;

	@FindBy(xpath = "//button[@id='filters-btn']")
	@CacheLookup
	public WebElement filtersBtn;
	
	@FindBy(xpath = "//div[@id='filters-search-btns']//div[2]//div")
	@CacheLookup
	public WebElement filterOptions;
	
	@FindBy(xpath = "//div[@id='filters-search-btns']//div[2]//div//div/h3")
	@CacheLookup
	public WebElement filterOption1;
	
	@FindBy(xpath = "//div[@id='filters-search-btns']//div[2]//div[2]//div/h3")
	@CacheLookup
	public WebElement filterOption2;
	
	@FindBy(xpath = "//div[@id='filters-search-btns']//div[2]//div[3]//div/h3")
	@CacheLookup
	public WebElement filterOption3;
	
	@FindBy(xpath = "//div[@id='filters-search-btns']//div[2]//div[4]//div/h3")
	@CacheLookup
	public WebElement filterOption4;
	
	@FindBy(xpath = "//div[@id='filters-search-btns']//div[2]//div[3]//input[contains(@placeholder,'Search')]")
	@CacheLookup
	public WebElement searchBarInFilterOption3;
	
	@FindBy(xpath = "//span[contains(text(),'Add more jobs')] | //button[@id='add-more-jobs-btn']")
	@CacheLookup
	public WebElement addMoreJobsBtn;
	
	@FindBy(xpath = "//div[contains(text(),'Add Job Data')]")
	@CacheLookup
	public WebElement addMoreJobsPageHeader;
	
	@FindBy(xpath = "//*[@aria-label='Close']//*")
	@CacheLookup
	public WebElement addMoreJobsCloseBtn;
	
	@FindBy(xpath = "//button[contains(@id,'publish-approved-mappings-btn')]")
	@CacheLookup
	public WebElement publishSelectedProfilesBtn;
	
	@FindBy(xpath = "//thead//input[@type='checkbox']")
	@CacheLookup
	public WebElement headerCheckbox;
	
	@FindBy(xpath = "//tbody//tr[1]//td[1][contains(@class,'whitespace')]//input")
	@CacheLookup
	public WebElement profile1Checkbox;
	
	@FindBy(xpath = "//tbody//tr[4]//td[1][contains(@class,'whitespace')]//input")
	@CacheLookup
	public WebElement profile2Checkbox;
	
	@FindBy(xpath = "//div[contains(@id,'results-toggle')]//*[contains(text(),'Showing')]")
	@CacheLookup
	public WebElement showingJobResultsCount;
	
	@FindBy(xpath = "//div[contains(@id,'results-toggle')]//label//div[2]")
	@CacheLookup
	public WebElement viewPublishedToggleBtn;
	
	@FindBy(xpath = "//button[text()='Published']")
	@CacheLookup
	public WebElement publishedBtn;
	
	@FindBy(xpath = "//td[@id='no-data-container']")
	@CacheLookup
	public WebElement nodataavailable;
	
	@FindBy(xpath = "//button[@id='publish-btn']")
	@CacheLookup
	public WebElement publishBtn;
	
	@FindBy(xpath = "//*[contains(text(),'Organization jobs')]") 
	@CacheLookup
	public WebElement table1Title;
	
	@FindBy(xpath = "//*[@id='org-job-container']/div/table/thead/tr/th[2]/div | //*[@id='table-container']/div[1]/div/div[1]/div/span[1]")
	@CacheLookup
	public WebElement table1Header1;
	
	@FindBy(xpath = "//*[@id='org-job-container']/div/table/thead/tr/th[3]/div | //*[@id='table-container']/div[1]/div/div[1]/div/span[2]")
	@CacheLookup
	public WebElement table1Header2;
	
	@FindBy(xpath = "//*[@id='org-job-container']/div/table/thead/tr/th[4]/div | //*[@id='table-container']/div[1]/div/div[1]/div/span[3]")
	@CacheLookup
	public WebElement table1Header3;
	
	@FindBy(xpath = "//*[contains(text(),'Matched success profiles')]")
	@CacheLookup
	public WebElement table2Title;
	
	@FindBy(xpath = "//*[@id='kf-job-container']/div/table/thead/tr/th[1]/div")
	@CacheLookup
	public WebElement table2Header1;
	
	@FindBy(xpath = "//*[@id='kf-job-container']/div/table/thead/tr/th[2]/div")
	@CacheLookup
	public WebElement table2Header2;
	
	@FindBy(xpath = "//*[@id='kf-job-container']/div/table/thead/tr/th[3]/div")
	@CacheLookup
	public WebElement table2Header3;
	
	@FindBy(xpath = "//*[@id='kf-job-container']/div/table/thead/tr/th[4]/div")
	@CacheLookup
	public WebElement table2Header4;
	
	@FindBy(xpath = "//*[@id='kf-job-container']/div/table/thead/tr/th[5]/div")
	@CacheLookup
	public WebElement table2Header5;
	
	@FindBy(xpath = "//tbody//tr[2]//button[@id='view-matches']")
	@CacheLookup
	public WebElement job1ViewOtherMatchesBtn;
	
	@FindBy(xpath = "//h1[@id='compare-desc']")
	@CacheLookup
	public WebElement CompareandSelectheader;
	
	@FindBy(xpath = "//tbody//tr[2]//button[@id='publish-btn'][1]")
	@CacheLookup
	public WebElement job1PublishBtn;
	
	@FindBy(xpath = "//h2[@id='modal-header']")
	@CacheLookup
	public WebElement publishedSuccessHeader;
	
	@FindBy(xpath = "//p[@id='modal-message']")
	@CacheLookup
	public WebElement publishedSuccessMsg;
	
	@FindBy(xpath = "//button[@aria-label='Close']")
	@CacheLookup
	public WebElement publishedSuccessMsgCloseBtn;
	
	@FindBy(xpath = "//*[contains(text(), 'uploading your Job Catalog')]")
	@CacheLookup
	public WebElement tipMsg1Text;
	
	@FindBy(xpath = "//*[contains(text(), 'uploading your Job Catalog')]//..//button")
	@CacheLookup
	public WebElement tipMsg1CloseBtn;
	
	@FindBy(xpath = "//button[@id='global-nav-menu-btn']")
	@CacheLookup
	public WebElement KfoneMenu;
	
	@FindBy(xpath = "//button[@aria-label='Job Mapping']")
	@CacheLookup
	public WebElement KfoneMenuJAMBtn;
	
	@FindBy(xpath = "//div[@id='header-logo']")
	@CacheLookup
	public WebElement JAMLogo;
	
	
	
	
	//Methods
	/**
	 * Verifies Job Mapping logo is displayed
	 * Page is already loaded, no need for spinner wait
	 */
	public void user_should_verify_job_mapping_logo_is_displayed_on_screen() {
		try { 
			// Page already loaded, directly check logo visibility
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(JAMLogo)).isDisplayed());
			ExtentCucumberAdapter.addTestStepLog("Job Mapping logo is displayed on screen as expected");
			LOGGER.info("Job Mapping logo is displayed on screen as expected");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_verify_job_mapping_logo_is_displayed_on_screen", e);
			LOGGER.error("Issue in displaying Job Mapping Logo - Method: user_should_verify_job_mapping_logo_is_displayed_on_screen", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog(" Issue in displaying Job Mapping Logo...Please Investigate!!!");
			Assert.fail("Issue in displaying Job Mapping Logo...Please Investigate!!!");
		}
	}
	
	public void navigate_to_job_mapping_page_from_kfone_global_menu_in_pm() {
		try {
			// Wait for page load spinner to disappear
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner1));
			
		LOGGER.info("Clicking KFONE Global Menu in Profile Manager...");
		ExtentCucumberAdapter.addTestStepLog("Clicking KFONE Global Menu in Profile Manager...");
		
		// Scroll element into view and click on KFONE Global Menu button
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", KfoneMenu);
		try { Thread.sleep(500); } catch (InterruptedException ie) {}
		try {
			wait.until(ExpectedConditions.elementToBeClickable(KfoneMenu)).click();
		} catch (Exception e) {
			try {
				js.executeScript("arguments[0].click();", KfoneMenu);
			} catch (Exception s) {
				utils.jsClick(driver, KfoneMenu);
			}
		}
		
		LOGGER.info("Successfully clicked KFONE Global Menu in Profile Manager");
		ExtentCucumberAdapter.addTestStepLog("Successfully clicked KFONE Global Menu in Profile Manager");
		
		PerformanceUtils.waitForPageReady(driver, 1);
		
		LOGGER.info("Clicking Job Mapping button in KFONE menu...");
		ExtentCucumberAdapter.addTestStepLog("Clicking Job Mapping button in KFONE menu...");
		
		// Scroll element into view and click on Job Mapping button in the menu
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", KfoneMenuJAMBtn);
		try { Thread.sleep(500); } catch (InterruptedException ie) {}
		try {
			wait.until(ExpectedConditions.elementToBeClickable(KfoneMenuJAMBtn)).click();
		} catch (Exception e) {
			try {
				js.executeScript("arguments[0].click();", KfoneMenuJAMBtn);
			} catch (Exception s) {
				utils.jsClick(driver, KfoneMenuJAMBtn);
			}
		}
			
			LOGGER.info("Successfully clicked Job Mapping button in KFONE menu");
			ExtentCucumberAdapter.addTestStepLog("Successfully clicked Job Mapping button in KFONE menu");
			
			// Wait for navigation to complete
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			
			LOGGER.info("Successfully Navigated to Job Mapping screen");
			ExtentCucumberAdapter.addTestStepLog("Successfully Navigated to Job Mapping screen");
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("navigate_to_job_mapping_page_from_kfone_global_menu_in_pm", e);
			LOGGER.error("Error navigating to Job Mapping page from KFONE Global Menu: " + e.getMessage());
			ExtentCucumberAdapter.addTestStepLog("Error navigating to Job Mapping page from KFONE Global Menu in Profile Manager: " + e.getMessage());
			throw new RuntimeException("Failed to navigate to Job Mapping page from KFONE Global Menu in Profile Manager", e);
		}
	}
	
	public void user_should_be_landed_on_job_mapping_page() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForElement(driver, jobMappingPageContainer);
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(jobMappingPageContainer)).isDisplayed());
			ExtentCucumberAdapter.addTestStepLog("User landed on the JOB MAPPING page successfully");
			LOGGER.info("User landed on the JOB MAPPING page successfully");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_be_landed_on_job_mapping_page", e);
			LOGGER.error("Issue in landing Job Mapping page - Method: user_should_be_landed_on_job_mapping_page", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog(" Issue in landing Job Mapping page...Please Investigate!!!");
			Assert.fail("Issue in landing Job Mapping page...Please Investigate!!!");
		}	
	}
	
	
	/**
	 * Confirms user is on Job Mapping page
	 * Page is already loaded from navigation, so minimal wait needed
	 */
	public void user_is_in_job_mapping_page() throws InterruptedException {	
		// Page is already loaded from previous navigation step, just confirm readiness
		PerformanceUtils.waitForPageReady(driver, 1);
		
		ExtentCucumberAdapter.addTestStepLog("User is in JOB MAPPING page");
		LOGGER.info("User is in JOB MAPPING page");
	}
	
	public void verify_title_header_is_correctly_displaying() throws InterruptedException {	
		try {
			String actualTitleHeader = wait.until(ExpectedConditions.refreshed(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='page-heading']//h1")))).getText();
			PerformanceUtils.waitForPageReady(driver, 3);
			Assert.assertEquals(actualTitleHeader,expectedTitleHeader);
			ExtentCucumberAdapter.addTestStepLog("Title header " + actualTitleHeader + " is displaying as expected"); 
			LOGGER.info("Title header " + actualTitleHeader + " is displaying as expected");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_title_header_is_correctly_displaying", e);
			LOGGER.error(" Issue in verifying title header - Method: verify_title_header_in_job_mapping_page", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog(" Issue in verifying title header in Job Mapping page...Please Investigate!!!");
			Assert.fail("Issue in verifying title header in Job Mapping page...Please Investigate!!!");
		}
		
	}

	public void verify_title_description_below_the_title_header() {
		try {
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(pageTitleDesc)).isDisplayed());
			String titleDesc = wait.until(ExpectedConditions.visibilityOf(pageTitleDesc)).getText();
			ExtentCucumberAdapter.addTestStepLog("Description below the title header : " + titleDesc);
			LOGGER.info("Description below the title header : " + titleDesc);
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_title_description_below_the_title_header", e);
			LOGGER.error(" Issue in verifying title description - Method: verify_title_description_below_the_title_header", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog(" Issue in verifying title description below the title header in Job Mapping page...Please Investigate!!!");
			Assert.fail("Issue in verifying title description below the title header in Job Mapping page...Please Investigate!!!");
		}	    
	}
	
	public void verify_organization_jobs_search_bar_text_box_is_clickable() {
		try {
			// Scroll element into view before clicking
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", searchBar);
			try { Thread.sleep(500); } catch (InterruptedException ie) {}
			try {
				wait.until(ExpectedConditions.elementToBeClickable(searchBar)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", searchBar);
				} catch (Exception s) {
					utils.jsClick(driver, searchBar);
				}
			}
			wait.until(ExpectedConditions.elementToBeClickable(searchBar)).click();
			ExtentCucumberAdapter.addTestStepLog("Organization Jobs Search bar text box is clickable as expected");
			LOGGER.info("Organization Jobs Search bar text box is clickable as expected");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_organization_jobs_search_bar_text_box_is_clickable", e);
			LOGGER.error(" Failed to click inside Organization Jobs search bar - Method: verify_organization_jobs_search_bar_text_box_is_clickable", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog(" Failed to click inside Organization Jobs search bar in Job Mapping page...Please Investigate!!!");
			Assert.fail("Failed to click inside Organization Jobs search bar in Job Mapping page...Please Investigate!!!");
		}
		
	}
	
	public void verify_organization_jobs_search_bar_placeholder_text() {
		try {
			String placeholderText = searchBar.getText();
			ExtentCucumberAdapter.addTestStepLog("Placeholder text inside Organization Jobs search bar is " + placeholderText + "displaying as expected");
			LOGGER.info("Placeholder text inside Organization Jobs search bar is " + placeholderText + "displaying as expected");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_organization_jobs_search_bar_placeholder_text", e);
			LOGGER.error("Issue in verifying Organization Jobs search bar placeholder text - Method: verify_organization_jobs_search_bar_placeholder_text", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying Organization Jobs search bar placeholder text in Job Mapping page...Please Investigate!!!");
			Assert.fail("Issue in verifying Organization Jobs search bar placeholder text in Job Mapping page...Please Investigate!!!");
		}
		
	}
	
	/**
	 * Enters job name substring in search bar with dynamic fallback retry logic.
	 * 
	 * Strategy:
	 * 1. Try each substring from SEARCH_SUBSTRING_OPTIONS array
	 * 2. Check if search returns results (not "Showing 0 of X")
	 * 3. If results found, use that substring and stop
	 * 4. If no results, try next substring
	 * 5. If all substrings fail, use the last one and proceed
	 * 
	 * This ensures we never get 0 results when searching (unless all options exhausted).
	 */
	public void enter_job_name_substring_in_search_bar() {
		boolean foundResults = false;
		String selectedSubstring = SEARCH_SUBSTRING_OPTIONS[0]; // Default to first option
		
		try {
			LOGGER.info("========================================");
			LOGGER.info("DYNAMIC SEARCH WITH FALLBACK RETRY");
			LOGGER.info("========================================");
			LOGGER.info("Attempting to find a search substring that returns results...");
			ExtentCucumberAdapter.addTestStepLog("Searching with dynamic substring (with fallback retry until results found)...");
			
			for (String substring : SEARCH_SUBSTRING_OPTIONS) {
				try {
					LOGGER.info("Trying search substring: '" + substring + "'");
					
					// Clear and enter substring
			wait.until(ExpectedConditions.visibilityOf(searchBar)).clear();
					wait.until(ExpectedConditions.visibilityOf(searchBar)).sendKeys(substring);
			wait.until(ExpectedConditions.visibilityOf(searchBar)).sendKeys(Keys.ENTER);
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForPageReady(driver, 2);
					
					// Check if results were found
					String resultsCountText = "";
					try {
						resultsCountText = showingJobResultsCount.getText().trim();
						LOGGER.info("   Search results: " + resultsCountText);
					} catch (Exception e) {
						LOGGER.info("   âœ— No profiles found with string '" + substring + "' (results element not found)");
						LOGGER.info("   Looking for profiles with another string...");
						continue; // Try next substring
					}
					
					if (resultsCountText.contains("Showing") && !resultsCountText.startsWith("Showing 0")) {
						// Found results!
						selectedSubstring = substring;
						jobnamesubstring = substring; // Update static variable for other methods to use
						foundResults = true;
						
						LOGGER.info("âœ“ Found results with substring: '" + substring + "'");
						LOGGER.info("   Results: " + resultsCountText);
						ExtentCucumberAdapter.addTestStepLog("âœ“ Search successful with substring '" + substring + "' - " + resultsCountText);
						break; // Stop trying other substrings
					} else {
						LOGGER.info("   âœ— No profiles found with string '" + substring + "' (0 results)");
						LOGGER.info("   Looking for profiles with another string...");
					}
					
				} catch (Exception e) {
					LOGGER.info("   âœ— Error searching with '" + substring + "', trying next...");
					// Continue to next substring
				}
			}
			
			if (!foundResults) {
				// All substrings exhausted, use the last one
				LOGGER.warn("âš ï¸ All search substrings exhausted without finding results");
				LOGGER.warn("   Proceeding with last substring: '" + selectedSubstring + "'");
				ExtentCucumberAdapter.addTestStepLog("âš ï¸ No search substring returned results. Using: '" + selectedSubstring + "'");
			}
			
			LOGGER.info("========================================");
			LOGGER.info("Final selected substring: '" + jobnamesubstring + "'");
			LOGGER.info("========================================");
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("enter_job_name_substring_in_search_bar", e);
			LOGGER.error("Failed to enter job name substring text in search bar - Method: enter_job_name_substring_in_search_bar", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Failed to enter job name substring text in search bar in Job Mapping page...Please Investigate!!!");
			Assert.fail("Failed to enter job name substring text in search bar in Job Mapping page...Please Investigate!!!");
		}
	}

//	public void user_should_verify_job_name_matching_profile_is_displaying_in_first_row_in_organization_jobs_profile_list() throws Exception {
//		try {
//			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
//			PerformanceUtils.waitForPageReady(driver, 2); 
//			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(jobNameofProfile1)).isDisplayed());
//			String jobname1 = wait.until(ExpectedConditions.visibilityOf(jobNameofProfile1)).getText(); 
//			orgJobNameInRow1 = jobname1.split("-", 2)[0].trim();
//			orgJobCodeInRow1 = jobname1.split("-", 2)[1].trim().substring(1, jobname1.split("-", 2)[1].length()-2);
//			ExtentCucumberAdapter.addTestStepLog("Job name of the first profile in organization table : " + orgJobNameInRow1);
//			LOGGER.info("Job name of the first profile in organization table : " + orgJobNameInRow1);
//			ExtentCucumberAdapter.addTestStepLog("Job code of the first profile in organization table : " + orgJobCodeInRow1);
//			LOGGER.info("Job code of the first profile in organization table : " + orgJobCodeInRow1);
//	} catch (Exception e) {
//			ScreenshotHandler.captureFailureScreenshot("user_should_verify_job_name_matching_profile_is_displaying_in_first_row_in_organization_jobs_profile_list", e);
//			LOGGER.error("Issue in verifying job name matching profile in Row1 - Method: user_should_verify_job_name_matching_profile_is_displaying_in_first_row_in_organization_jobs_profile_list", e);
//			e.printStackTrace();
//			ExtentCucumberAdapter.addTestStepLog("Issue in verifying job name matching profile in Row1 of Organization jobs profile list...Please Investigate!!!");
//			Assert.fail("Issue in verifying job name matching profile in Row1 of Organization jobs profile list...Please Investigate!!!");
//		}	
//	}
	
	public void user_should_verify_organization_job_name_and_job_code_values_of_first_profile() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForPageReady(driver, 2); 
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(jobNameofProfile1)).isDisplayed());
			String jobname1 = wait.until(ExpectedConditions.visibilityOf(jobNameofProfile1)).getText(); 
			orgJobNameInRow1 = jobname1.split("-", 2)[0].trim();
			orgJobCodeInRow1 = jobname1.split("-", 2)[1].trim().substring(1, jobname1.split("-", 2)[1].length()-2);
			ExtentCucumberAdapter.addTestStepLog("Job name of the first profile in organization table : " + orgJobNameInRow1);
			LOGGER.info("Job name of the first profile in organization table : " + orgJobNameInRow1);
			ExtentCucumberAdapter.addTestStepLog("Job code of the first profile in organization table : " + orgJobCodeInRow1);
			LOGGER.info("Job code of the first profile in organization table : " + orgJobCodeInRow1);
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_verify_organization_job_name_and_job_code_values_of_first_profile", e);
			LOGGER.error("Issue in verifying job name and job code values in Row1 - Method: user_should_verify_organization_job_name_and_job_code_values_of_first_profile", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying job name matching profile in Row1 of Organization jobs profile list...Please Investigate!!!");
			Assert.fail("Issue in verifying job name matching profile in Row1 of Organization jobs profile list...Please Investigate!!!");
		}	
	}
	
	public void user_should_verify_organization_job_grade_and_department_values_of_first_profile() {
		try {
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(jobGradeofProfile1)).isDisplayed());
			String jobGrade = wait.until(ExpectedConditions.visibilityOf(jobGradeofProfile1)).getText(); 
			if(jobGrade.contentEquals("-") || jobGrade.isEmpty() || jobGrade.isBlank()) {
				jobGrade = "NULL";
				orgJobGradeInRow1 = jobGrade;
			}
			orgJobGradeInRow1 = jobGrade;
			ExtentCucumberAdapter.addTestStepLog("Grade value of Organization Job first profile : " + orgJobGradeInRow1);
			LOGGER.info("Grade value of Organization Job first profile : " + orgJobGradeInRow1);
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_verify_organization_job_grade_and_department_values_of_first_profile", e);
			LOGGER.error("Issue in Verifying Organization Job Grade value first profile - Method: user_should_verify_organization_job_grade_in_first_row", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying Organization Job Grade value first profile...Please Investigate!!!");
			Assert.fail("Issue in Verifying Organization Job Grade value first profile...Please Investigate!!!");
		}
		
		try {
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(jobDepartmentofProfile1)).isDisplayed());
			String jobDepartment = wait.until(ExpectedConditions.visibilityOf(jobDepartmentofProfile1)).getText(); 
			if(jobDepartment.contentEquals("-") || jobDepartment.isEmpty() || jobDepartment.isBlank()) {
				jobDepartment = "NULL";
				orgJobDepartmentInRow1 = jobDepartment;
			}
			orgJobDepartmentInRow1 = jobDepartment;
			ExtentCucumberAdapter.addTestStepLog("Department value of Organization Job first profile : " + orgJobDepartmentInRow1);
			LOGGER.info("Department value of Organization Job first profile : " + orgJobDepartmentInRow1);
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_verify_organization_job_grade_and_department_values_of_first_profile", e);
			LOGGER.error("Issue in Verifying Organization Job Department value first profile - Method: user_should_verify_organization_job_department_in_first_row", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying Organization Job Department value first profile...Please Investigate!!!");
			Assert.fail("Issue in Verifying Organization Job Department value first profile...Please Investigate!!!");
		}
		
	}
	
	public void user_should_verify_organization_job_function_or_sub_function_of_first_profile() {
		try {
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(jobFunctionofProfile1)).isDisplayed());
			String jobFunction = wait.until(ExpectedConditions.visibilityOf(jobFunctionofProfile1)).getText(); 
			if(jobFunction.contentEquals("- | -") || jobFunction.contentEquals("-") || jobFunction.isEmpty() || jobFunction.isBlank()) {
				jobFunction = "NULL | NULL";
				orgJobFunctionInRow1 = jobFunction;
			} else if (jobFunction.endsWith("-") || jobFunction.endsWith("| -") || jobFunction.endsWith("|") || (!(jobFunction.contains("|")) && (jobFunction.length() > 1))) {
				jobFunction = jobFunction + " | NULL";
				orgJobFunctionInRow1 = jobFunction;
			}
			
			orgJobFunctionInRow1 = jobFunction;
			ExtentCucumberAdapter.addTestStepLog("Function / Sub-function values of Organization Job first profile : " + orgJobFunctionInRow1);
			LOGGER.info("Function / Sub-function values of Organization Job first profile : " + orgJobFunctionInRow1);
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_verify_organization_job_function_or_sub_function_of_first_profile", e);
			LOGGER.error("Issue in Verifying Organization Job Function/Sub-function values first profile - Method: user_should_verify_organization_job_function_or_sub_function_in_first_row", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying Organization Job Function / Sub-function values first profile...Please Investigate!!!");
			Assert.fail("Issue in Verifying Organization Job Function / Sub-function values first profile...Please Investigate!!!");
		}
		
	}
	
	public void click_on_matched_profile_of_job_in_first_row() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			String MatchedProfileNameText = wait.until(ExpectedConditions.elementToBeClickable(job1LinkedMatchedProfile)).getText();
			matchedSuccessPrflName = MatchedProfileNameText;
			// Scroll element into view before clicking
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", job1LinkedMatchedProfile);
			Thread.sleep(500); // Brief pause after scroll for smooth rendering
			try {
				wait.until(ExpectedConditions.elementToBeClickable(job1LinkedMatchedProfile)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", job1LinkedMatchedProfile);
				} catch (Exception s) {
					utils.jsClick(driver, job1LinkedMatchedProfile);
				}
			}
			ExtentCucumberAdapter.addTestStepLog("Clicked on Matched Profile with name "+ MatchedProfileNameText +" of Organization Job "+ orgJobNameInRow1 + " in first row");
			LOGGER.info("Clicked on Matched Profile with name "+ MatchedProfileNameText +" of Organization Job "+ orgJobNameInRow1 + " in first row");
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_on_matched_profile_of_job_in_first_row", e);
			LOGGER.error("Issue in clicking Matched Profile linked with job - Method: click_on_matched_profile_of_job_in_first_row", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in clicking Matched Profile linked with job name "+ orgJobNameInRow1 + "  in first row...Please Investigate!!!");
			Assert.fail("Issue in clicking Matched Profile linked with job name "+ orgJobNameInRow1 + " in first row...Please Investigate!!!");
		}
	}

	public void verify_profile_details_popup_is_displayed() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(profileDetailsPopupHeader)).isDisplayed());
			ExtentCucumberAdapter.addTestStepLog("Profile details popup is displayed on screen as expected");
			LOGGER.info("Profile details popup is displayed on screen as expected");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_profile_details_popup_is_displayed", e);
			LOGGER.error(" Issue in displaying profile details popup - Method: verify_the_profile_details_popup_should_display", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog(" Issue in displaying profile details popup...Please Investigate!!!");
			Assert.fail("Issue in displaying profile details popup...Please Investigate!!!");
		}
		
	}

	public void click_on_close_button_in_profile_details_popup() {
		try {
			// Scroll element into view before clicking
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", profileDetailsPopupCloseBtn);
			try { Thread.sleep(500); } catch (InterruptedException ie) {}
			try {
				wait.until(ExpectedConditions.elementToBeClickable(profileDetailsPopupCloseBtn)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", profileDetailsPopupCloseBtn);
				} catch (Exception s) {
					utils.jsClick(driver, profileDetailsPopupCloseBtn);
				}
			}
			ExtentCucumberAdapter.addTestStepLog("Clicked on close button in Profile details popup");
			LOGGER.info("Clicked on close button in Profile details popup");
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_on_close_button_in_profile_details_popup", e);
			LOGGER.error("Issue in clicking close button in Profile details popup - Method: click_on_close_button_in_profile_details_popup", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in clicking close button in Profile details popup in Job Mapping page...Please Investigate!!!");
			Assert.fail("Issue in clicking close button in Profile details popup in Job Mapping page...Please investigate!!!");
		}   
	}
	
	/**
	 * Clicks filters dropdown button
	 * Page is already loaded, so minimal wait needed
	 */
	public void click_on_filters_dropdown_button() {
		try {
			js.executeScript("window.scrollTo(0, 0);"); // Instant scroll to top
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForPageReady(driver, 1); 
			try {
				wait.until(ExpectedConditions.elementToBeClickable(filtersBtn)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", filtersBtn);
				} catch (Exception s) {
					utils.jsClick(driver, filtersBtn);
				}
			}
			ExtentCucumberAdapter.addTestStepLog("Clicked on filters dropdown button to open and verify options available...");
			LOGGER.info("Clicked on filters dropdown button to open and verify options available...");
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_on_filters_dropdown_button", e);
			LOGGER.error("Issue in clicking filters dropdown button - Method: click_on_filters_dropdown_button", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in clicking filters dropdown button in Job Mapping page...Please Investigate!!!");
			Assert.fail("Issue in clicking filters dropdown button in Job Mapping page...Please investigate!!!");
		}   
	}

	
	public void verify_options_available_inside_filters_dropdown() {
		Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(filterOptions)).isDisplayed());
		try {
			String filterOption1Text = wait.until(ExpectedConditions.visibilityOf(filterOption1)).getText();
			Assert.assertEquals(filterOption1Text, "Grades");
			String filterOption2Text = wait.until(ExpectedConditions.visibilityOf(filterOption2)).getText();
			Assert.assertEquals(filterOption2Text, "Departments");
			String filterOption3Text = wait.until(ExpectedConditions.visibilityOf(filterOption3)).getText();
			Assert.assertEquals(filterOption3Text, "Functions / Subfunctions");
			String filterOption4Text = wait.until(ExpectedConditions.visibilityOf(filterOption4)).getText();
			Assert.assertEquals(filterOption4Text, "Mapping Status");
			ExtentCucumberAdapter.addTestStepLog("Options inside Filter dropdown verified successfully");
			LOGGER.info("Options inside Filter dropdown verified successfully");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_options_available_inside_filters_dropdown", e);
			LOGGER.error("Issue in verifying Options inside Filter dropdown - Method: verify_options_available_inside_filters_dropdown", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying Options inside Filter dropdown....Please Investigate!!!");
			Assert.fail("Issue in verifying Options inside Filter dropdown....Please Investigate!!!");
		}
		
		try {
			// Scroll element into view before clicking
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", filterOption3);
			try { Thread.sleep(500); } catch (InterruptedException ie) {}
			try {
				wait.until(ExpectedConditions.elementToBeClickable(filterOption3)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", filterOption3);
				} catch (Exception s) {
					utils.jsClick(driver, filterOption3);
				}
			}
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			wait.until(ExpectedConditions.visibilityOf(searchBarInFilterOption3)).isDisplayed();
			searchBarInFilterOption3.click();
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			ExtentCucumberAdapter.addTestStepLog("Search bar inside Functions / Subfunctions filter option is available and clickable...");
			LOGGER.info("Search bar inside Functions / Subfunctions filter option is available and clickable...");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_options_available_inside_filters_dropdown_searchbar", e);
			LOGGER.error("Issue in verifying Search bar inside Functions / Subfunctions filter option - Method: verify_options_available_inside_filters_dropdown", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying Search bar inside Functions / Subfunctions filter option...Please Investigate!!!");
			Assert.fail("Issue in verifying Search bar inside Functions / Subfunctions filter option...Please Investigate!!!");
		} 
	}

	
	/**
	 * Closes the Filters dropdown by clicking the Filters button to toggle it
	 * Dropdown closes instantly without page reload, so no wait needed
	 */
	public void close_the_filters_dropdown() {
		try {
			LOGGER.info("Attempting to close Filters dropdown...");
			
			// Use JS click directly (most reliable method for this element)
			// Dropdown closes instantly, no page reload occurs
			js.executeScript("arguments[0].click();", filtersBtn);
			
			// Verify dropdown is closed (the wait is only for invisibility check)
			Assert.assertTrue(wait.until(ExpectedConditions.invisibilityOf(filterOptions)));
			
			LOGGER.info("âœ“ Filters dropdown closed successfully");
			ExtentCucumberAdapter.addTestStepLog("Closed Filters dropdown");
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("close_filters_dropdown", e);
			LOGGER.error("Issue closing Filters dropdown: {}", e.getMessage(), e);
			ExtentCucumberAdapter.addTestStepLog("Issue closing Filters dropdown");
			Assert.fail("Failed to close Filters dropdown");
		}
	}
	
	
	public void user_should_see_add_more_jobs_button_is_displaying() {
		try {
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(addMoreJobsBtn)).isDisplayed());
			String btnText = wait.until(ExpectedConditions.visibilityOf(addMoreJobsBtn)).getText();
			ExtentCucumberAdapter.addTestStepLog(btnText + " button is displaying as expected");
			LOGGER.info(btnText + " button is displaying as expected");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_see_add_more_jobs_button_is_displaying", e);
			LOGGER.error("Issue in displaying add more jobs button - Method: user_should_see_add_more_jobs_button_is_displaying", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in displaying add more jobs button in Job Mapping page...Please Investigate!!!");
			Assert.fail("Issue in displaying add more jobs button in Job Mapping page...Please Investigate!!!");
		}
		
	}

	
	public void verify_add_more_jobs_button_is_clickable() throws InterruptedException {
		try {
			// Scroll element into view before clicking
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", addMoreJobsBtn);
			Thread.sleep(500);
			try {
				wait.until(ExpectedConditions.elementToBeClickable(addMoreJobsBtn)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", addMoreJobsBtn);
				} catch (Exception s) {
					utils.jsClick(driver, addMoreJobsBtn);
				}
			}
			ExtentCucumberAdapter.addTestStepLog("Clicked on Add more jobs button");
			LOGGER.info("Clicked on Add more jobs button");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_add_more_jobs_button_is_clickable", e);
			LOGGER.error("Issue on clicking Add more jobs button - Method: verify_add_more_jobs_button_is_clickable", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue on clicking Add more jobs button in Job Mapping page...Please Investigate!!!");
			Assert.fail("Issue on clicking Add more jobs button in Job Mapping page...Please Investigate!!!");
		}
		PerformanceUtils.waitForPageReady(driver, 5);
		
		
	}
	
	public void verify_user_landed_on_add_more_jobs_page() {
		try {
			wait.until(ExpectedConditions.visibilityOf(addMoreJobsPageHeader)).isDisplayed();
			String addMoreJobsPageheaderText = wait.until(ExpectedConditions.visibilityOf(addMoreJobsPageHeader)).getText();
			Assert.assertEquals(addMoreJobsPageheaderText, "Add Job Data");
			ExtentCucumberAdapter.addTestStepLog("Add More Jobs landing page is Verified Successfully"); 
			LOGGER.info("Add More Jobs landing page is Verified Successfully");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_user_landed_on_add_more_jobs_page", e);
			LOGGER.error("Issue in verifying Add more jobs landing page - Method: verify_user_landed_on_add_more_jobs_page", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying Add more jobs landing page...Please Investigate!!!");
			Assert.fail("Issue in verifying Add more jobs landing page...Please Investigate!!!");
		}
	}
	
	public void close_add_more_jobs_page() {
		try {
			// Scroll element into view before clicking
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", addMoreJobsCloseBtn);
			try { Thread.sleep(500); } catch (InterruptedException ie) {}
			try {
				wait.until(ExpectedConditions.elementToBeClickable(addMoreJobsCloseBtn)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", addMoreJobsCloseBtn);
				} catch (Exception s) {
					utils.jsClick(driver, addMoreJobsCloseBtn);
				}
			}
			ExtentCucumberAdapter.addTestStepLog("Clicked on Add more jobs Close button(X)");
			LOGGER.info("Clicked on Add more jobs Close button(X)");
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("close_add_more_jobs_page", e);
			LOGGER.error("Issue in Closing Add more jobs page - Method: close_add_more_jobs_page", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in Closing Add more jobs page...Please Investigate!!!");
			Assert.fail("Issue in Closing Add more jobs page...Please Investigate!!!");
		}
	}
	
	public void user_should_verify_publish_selected_profiles_button_is_disabled() { 
		try {
			Assert.assertTrue(!(wait.until(ExpectedConditions.visibilityOf(publishSelectedProfilesBtn))).isEnabled());
			ExtentCucumberAdapter.addTestStepLog("Publish Selected Profiles button is disabled as expected");
			LOGGER.info("Publish Selected Profiles button is disabled as expected");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_verify_publish_selected_profiles_button_is_disabled", e);
			LOGGER.error("Issue in verifying Publish Selected Profiles button is disabled - Method: user_should_verify_publish_selected_profiles_button_is_disabled", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying Publish Selected Profiles button is disabled in Job Mapping page...Please Investigate!!!");
			Assert.fail("Issue in verifying Publish Selected Profiles button is disabled in Job Mapping page...Please Investigate!!!");
		}
		
	}

	
	/**
	 * Clicks header checkbox to select all loaded job profiles
	 * Page is already loaded, so minimal wait needed
	 */
	public void click_on_header_checkbox_to_select_loaded_job_profiles_in_job_mapping_screen() {
		try {
			// Page already loaded from previous step, just ensure readiness
			PerformanceUtils.waitForPageReady(driver, 1);
			
			// Step 1: Store count of profiles loaded BEFORE clicking header checkbox
			String resultsCountText = "";
			int retryAttempts = 0;
			int maxRetries = 3;
			
			while (retryAttempts < maxRetries) {
				try {
					WebDriverWait longWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(20)); // Increased from 10
					resultsCountText = longWait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//div[contains(@id,'results-toggle')]//*[contains(text(),'Showing')]")
					)).getText();
					break; // Success - exit retry loop
				} catch (org.openqa.selenium.StaleElementReferenceException | org.openqa.selenium.TimeoutException e) {
					retryAttempts++;
					if (retryAttempts >= maxRetries) {
						throw e; // Re-throw if max retries reached
					}
					LOGGER.warn("Attempt {} failed: {}, retrying...", retryAttempts, e.getClass().getSimpleName());
					PerformanceUtils.waitForPageReady(driver, 2);
					Thread.sleep(1000);
				}
			}
			
			// Parse "Showing 100 of 1428" to extract loaded count
			if (resultsCountText.contains("Showing") && resultsCountText.contains("of")) {
				String[] parts = resultsCountText.split("\\s+");
				// parts[0] = "Showing", parts[1] = "100", parts[2] = "of"
				loadedProfilesBeforeHeaderCheckboxClick = Integer.parseInt(parts[1]);
				LOGGER.info("Loaded profiles on screen (BEFORE header checkbox click): " + loadedProfilesBeforeHeaderCheckboxClick);
			} else {
				LOGGER.warn("Could not parse results count text: " + resultsCountText);
				loadedProfilesBeforeHeaderCheckboxClick = 0;
			}
		
		// Step 2: Scroll element into view and click header checkbox
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", headerCheckbox);
		try { Thread.sleep(500); } catch (InterruptedException ie) {}
		try {
			wait.until(ExpectedConditions.elementToBeClickable(headerCheckbox)).click();
		} catch (Exception e) {
			try {
				js.executeScript("arguments[0].click();", headerCheckbox);
			} catch (Exception s) {
				utils.jsClick(driver, headerCheckbox);
			}
		}
			
			// Step 3: Get all checkboxes and count selected and disabled profiles
			int profilesCount = loadedProfilesBeforeHeaderCheckboxClick;
			disabledProfilesCountInLoadedProfiles = 0;
			
			// Note: Job Mapping screen doesn't have disabled profiles like HCM Sync Profiles
			// All profiles can be selected, so disabled count will be 0
			// But we keep the logic for consistency and future use
			
			// Get all checkbox elements directly (not by row position, as some rows don't have checkboxes)
			var allCheckboxes = driver.findElements(
				By.xpath("//tbody//tr//td[1][contains(@class,'whitespace')]//input")
			);
			
			// Only check the first 'loadedProfilesBeforeHeaderCheckboxClick' checkboxes
			int checkboxesToCheck = Math.min(allCheckboxes.size(), loadedProfilesBeforeHeaderCheckboxClick);
			
			for (int i = 0; i < checkboxesToCheck; i++) {
				try {
					WebElement checkbox = allCheckboxes.get(i);
					
					// Check if checkbox is disabled
					if (!checkbox.isEnabled()) {
						LOGGER.debug("Profile at index " + (i+1) + " has disabled checkbox");
						disabledProfilesCountInLoadedProfiles++;
						profilesCount--;
					}
				} catch (Exception e) {
					// If we can't verify the checkbox, continue
					LOGGER.debug("Could not verify checkbox at index " + (i+1) + ": " + e.getMessage());
				}
			}
			
			// Step 4: Store selected profiles count
			selectedProfilesAfterHeaderCheckboxClick = profilesCount;
			
			LOGGER.info("Clicked on header checkbox and selected " + selectedProfilesAfterHeaderCheckboxClick + " job profiles in Job Mapping screen");
			LOGGER.info("   â†’ Loaded profiles (before click): " + loadedProfilesBeforeHeaderCheckboxClick);
			LOGGER.info("   â†’ Selected profiles: " + selectedProfilesAfterHeaderCheckboxClick);
			LOGGER.info("   â†’ Disabled profiles: " + disabledProfilesCountInLoadedProfiles);
			ExtentCucumberAdapter.addTestStepLog("Clicked on header checkbox and selected " + selectedProfilesAfterHeaderCheckboxClick + " job profiles in Job Mapping screen");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_on_header_checkbox_to_select_loaded_job_profiles_in_job_mapping_screen", e);
			LOGGER.error("Issue in clicking on header checkbox to select loaded job profiles in Job Mapping screen - Method: click_on_header_checkbox_to_select_loaded_job_profiles_in_job_mapping_screen", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in clicking on header checkbox to select loaded job profiles in Job Mapping page...Please Investigate!!!");
			Assert.fail("Issue in clicking on header checkbox to select loaded job profiles in Job Mapping page...Please Investigate!!!");
		}
	}

	
	public void user_should_verify_publish_selected_profiles_button_is_enabled() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForPageReady(driver, 2);
			Thread.sleep(500); // Allow UI to stabilize
			
			// Direct check - button should be enabled since selections are preserved
			WebElement publishBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//button[contains(@id,'publish-approved-mappings-btn')]")
			));
			
			boolean isEnabled = publishBtn.isEnabled();
			
			Assert.assertTrue(isEnabled, "Publish Selected Profiles button should be enabled");
			ExtentCucumberAdapter.addTestStepLog("Publish Selected Profiles button is enabled as expected after selecting job profiles"); 
			LOGGER.info("Publish Selected Profiles button is enabled as expected after selecting job profiles");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_verify_publish_selected_profiles_button_is_enabled", e);
			LOGGER.error("Issue in verifying Publish Selected Profiles button is enabled - Method: user_should_verify_publish_selected_profiles_button_is_enabled", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying Publish Selected Profiles button is enabled in Job Mapping page...Please Investigate!!!");
			Assert.fail("Issue in verifying Publish Selected Profiles button is enabled in Job Mapping page...Please Investigate!!!");
		}
		
	}

	
	public void user_should_uncheck_header_checkbox_to_deselect_selected_job_profiles() {
		try {
			// Scroll element into view before clicking
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", headerCheckbox);
			try { Thread.sleep(500); } catch (InterruptedException ie) {}
			try {
				wait.until(ExpectedConditions.elementToBeClickable(headerCheckbox)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", headerCheckbox);
				} catch (Exception s) {
					utils.jsClick(driver, headerCheckbox);
				}
			}
			ExtentCucumberAdapter.addTestStepLog("Clicked on header checkbox and Deselected all job profiles");
			LOGGER.info("Clicked on header checkbox and Deselected all job profiles");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_uncheck_header_checkbox_to_deselect_selected_job_profiles", e);
			LOGGER.error("Issue in clicking on header checkbox to deselect all job profiles - Method: user_should_uncheck_header_checkbox_to_deselect_selected_job_profiles", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in clicking on header checkbox to deselect all job profiles...Please Investigate!!!");
			Assert.fail("Issue in clicking on header checkbox to deselect all job profiles...Please Investigate!!!");
		} 
	}
	
	public void click_on_checkbox_of_first_job_profile() {
		try {
			String jobname1 = wait.until(ExpectedConditions.visibilityOf(jobNameofProfile1)).getText(); 
			orgJobNameInRow1 = jobname1.split("-", 2)[0].trim();
			// Scroll element into view before clicking
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", profile1Checkbox);
			try { Thread.sleep(500); } catch (InterruptedException ie) {}
			try {
				wait.until(ExpectedConditions.elementToBeClickable(profile1Checkbox)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", profile1Checkbox);
				} catch (Exception s) {
					utils.jsClick(driver, profile1Checkbox);
				}
			}
			ExtentCucumberAdapter.addTestStepLog("Clicked on checkbox of First job profile with name : " + orgJobNameInRow1);
			LOGGER.info("Clicked on checkbox of First job profile with name : " + orgJobNameInRow1);
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_on_checkbox_of_first_job_profile", e);
			LOGGER.error("Issue in clicking First job profile checkbox - Method: click_on_checkbox_of_first_job_profile", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in clicking First job profile checkbox in Job Mapping page...Please Investigate!!!");
			Assert.fail("Issue in clicking First job profile checkbox in Job Mapping page...Please Investigate!!!");
		} 
	}
	
	public void click_on_checkbox_of_second_job_profile() {
		try {
			String jobname2 = wait.until(ExpectedConditions.visibilityOf(jobNameofProfile2)).getText(); 
			orgJobNameInRow2 = jobname2.split("-", 2)[0].trim();
			// Scroll element into view before clicking
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", profile2Checkbox);
			try { Thread.sleep(500); } catch (InterruptedException ie) {}
			try {
				wait.until(ExpectedConditions.elementToBeClickable(profile2Checkbox)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", profile2Checkbox);
				} catch (Exception s) {
					utils.jsClick(driver, profile2Checkbox);
				}
			}
			ExtentCucumberAdapter.addTestStepLog("Clicked on checkbox of Second job profile with name : " + orgJobNameInRow2);
			LOGGER.info("Clicked on checkbox of Second job profile with name : " + orgJobNameInRow2);
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_on_checkbox_of_second_job_profile", e);
			LOGGER.error("Issue in clicking Second job profile checkbox - Method: click_on_checkbox_of_second_job_profile", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in clicking Second job profile checkbox in Job Mapping page...Please Investigate!!!");
			Assert.fail("Issue in clicking Second job profile checkbox in Job Mapping page...Please Investigate!!!");
		} 
	}
	
	public void click_on_publish_selected_profiles_button() {
		try {
			// Scroll element into view before clicking
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", publishSelectedProfilesBtn);
			try { Thread.sleep(500); } catch (InterruptedException ie) {}
			try {
				wait.until(ExpectedConditions.elementToBeClickable(publishSelectedProfilesBtn)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", publishSelectedProfilesBtn);
				} catch (Exception s) {
					utils.jsClick(driver, publishSelectedProfilesBtn);
				}
			}
			ExtentCucumberAdapter.addTestStepLog("Clicked on Publish Selected Profiles button to publish selected profiles");
			LOGGER.info("Clicked on Publish Selected Profiles button to publish selected profiles");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_on_publish_selected_profiles_button", e);
			LOGGER.error("Issue in clicking Publish Selected Profiles button - Method: click_on_publish_selected_profiles_button", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in clicking Publish Selected Profiles button in Job Mapping page...Please Investigate!!!");
			Assert.fail("Issue in clicking Publish Selected Profiles button in Job Mapping page...Please Investigate!!!");
		} 
		
	}
	
	/**
	 * Verifies job profiles count is displaying
	 * Page is already loaded, so minimal wait needed
	 */
	public void verify_job_profiles_count_is_displaying_on_the_page() {
		try {
			// Page already loaded from previous step, just ensure readiness
			PerformanceUtils.waitForPageReady(driver, 1);
			
			// Fix: Use fresh element lookup to avoid stale element reference
			String resultsCountText = "";
			int retryAttempts = 0;
			int maxRetries = 5; // Increased from 3
			
			while (retryAttempts < maxRetries) {
				try {
					WebDriverWait longWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(20)); // Increased from 10
					resultsCountText = longWait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//div[contains(@id,'results-toggle')]//*[contains(text(),'Showing')]")
					)).getText();
					break; // Success - exit retry loop
				} catch (org.openqa.selenium.StaleElementReferenceException | org.openqa.selenium.TimeoutException e) {
					retryAttempts++;
					if (retryAttempts >= maxRetries) {
						throw e; // Re-throw if max retries reached
					}
					LOGGER.warn("Attempt {} failed: {}, retrying...", retryAttempts, e.getClass().getSimpleName());
					PerformanceUtils.waitForPageReady(driver, 2);
					Thread.sleep(1000);
				}
			}
			
			intialResultsCount = resultsCountText;
		    ExtentCucumberAdapter.addTestStepLog("Initially " + resultsCountText + " of job profiles");
		    LOGGER.info("Initially " + resultsCountText + " of job profiles");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_job_profiles_count_is_displaying_on_the_page", e);
			LOGGER.error("Issue in verifying job profiles results count - Method: verify_job_profiles_count_is_displaying_on_the_page", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying job profiles results count in Job Mapping page...Please Investigate!!!");
			Assert.fail("Issue in verifying job profiles results count in Job Mapping page...Please Investigate!!!");
		} 
	}
	
	public void scroll_page_to_view_more_job_profiles() throws InterruptedException {
		try {
//			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			
			// âœ… IMPROVED: Use smooth scroll with throttling to prevent browser unresponsiveness
			js.executeScript("window.scrollTo({top: document.body.scrollHeight, behavior: 'smooth'});");
			Thread.sleep(1000); // Reduced from potential rapid scrolling
			
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForPageReady(driver, 2);
			
			ExtentCucumberAdapter.addTestStepLog("Scrolled page down to view more job profiles");
			LOGGER.info("Scrolled page down to view more job profiles");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("scroll_page_to_view_more_job_profiles", e);
			LOGGER.error("Issue in scrolling page down to view more job profiles - Method: scroll_page_to_view_more_job_profiles", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in scrolling page down to view more job profiles in Job Mapping page...Please Investigate!!!");
			Assert.fail("Issue in scrolling page down to view more job profiles in Job Mapping page...Please Investigate!!!");
		}
		
	}

	
	public void user_should_verify_count_of_job_profiles_is_correctly_showing_on_top_of_job_profiles_listing_table() throws InterruptedException {
		try {
			js.executeScript("arguments[0].scrollIntoView(true);", pageTitleHeader);
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			wait.until(ExpectedConditions.invisibilityOfAllElements(driver.findElements(By.xpath("//div[@data-testid='loader']//img"))));
			PerformanceUtils.waitForPageReady(driver, 5); 
			Thread.sleep(2000); // Additional wait for results count element to appear after filter application
			
			// Fix: Use fresh element lookup to avoid stale element reference
			String resultsCountText2 = "";
			int retryAttempts = 0;
			int maxRetries = 5; // Increased from 3
			
			while (retryAttempts < maxRetries) {
				try {
					WebDriverWait longWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(20)); // Increased from 10
					resultsCountText2 = longWait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//div[contains(@id,'results-toggle')]//*[contains(text(),'Showing')]")
					)).getText();
					break; // Success - exit retry loop
				} catch (org.openqa.selenium.StaleElementReferenceException | org.openqa.selenium.TimeoutException e) {
					retryAttempts++;
					if (retryAttempts >= maxRetries) {
						throw e; // Re-throw if max retries reached
					}
					LOGGER.warn("Attempt {} failed: {}, retrying...", retryAttempts, e.getClass().getSimpleName());
					PerformanceUtils.waitForPageReady(driver, 2);
					Thread.sleep(1000);
				}
			}
			
			updatedResultsCount = resultsCountText2;
			PerformanceUtils.waitForPageReady(driver, 3); 
			
			// CHECK FOR ZERO RESULTS - Set flag for skipping validation steps
			if (resultsCountText2.contains("Showing 0 of")) {
				PO10_ValidateScreen1SearchResults.setHasSearchResults(false);
				ExtentCucumberAdapter.addTestStepLog("Profile Results count updated and Now " + resultsCountText2 + " - No results found, validation steps will be skipped");
				LOGGER.info("Profile Results count updated and Now " + resultsCountText2 + " - No results found, validation steps will be skipped");
				return;
			} else {
				PO10_ValidateScreen1SearchResults.setHasSearchResults(true);
			}
			
			if (!resultsCountText2.equals(intialResultsCount)) {
				ExtentCucumberAdapter.addTestStepLog("Profile Results count updated and Now " + resultsCountText2 + " of job profiles as expected");
				LOGGER.info("Profile Results count updated and Now " + resultsCountText2 + " of job profiles as expected");
			} else {
				ExtentCucumberAdapter.addTestStepLog("Issue in updating profile results count, Still " + resultsCountText2 + " ....Please Investigate!!!");
				LOGGER.error("Issue in updating profile results count, Still " + resultsCountText2 + " ....Please Investigate!!!");
				Assert.fail("Issue in updating profile results count, Still " + resultsCountText2 + " ....Please Investigate!!!");
			}
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_verify_count_of_job_profiles_is_correctly_showing_on_top_of_job_profiles_listing_table", e);
			LOGGER.error("Issue in verifying job profiles results count after scrolling down - Method: user_should_verify_count_of_job_profiles_is_correctly_showing_on_top_of_job_profiles_listing_table", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying job profiles results count after scrolling down in Job Mapping page...Please Investigate!!!");
			Assert.fail("Issue in verifying job profiles results count after scrolling down in Job Mapping page...Please Investigate!!!");
		}		
	}
				
	public void user_should_verify_view_published_toggle_button_is_displaying() {
		Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(viewPublishedToggleBtn)).isDisplayed());
		ExtentCucumberAdapter.addTestStepLog("View published toggle button is displaying on screen as expected");
		LOGGER.info("View published toggle button is displaying on screen as expected");
	}

	
	public void click_on_toggle_button_to_turn_on() throws InterruptedException {
		try {
			// Scroll element into view before clicking
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", viewPublishedToggleBtn);
			Thread.sleep(500);
			try {
				wait.until(ExpectedConditions.elementToBeClickable(viewPublishedToggleBtn)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", viewPublishedToggleBtn);
				} catch (Exception s) {
					utils.jsClick(driver, viewPublishedToggleBtn);
				}
			}
			ExtentCucumberAdapter.addTestStepLog("View published toggle button is turned ON"); 
			LOGGER.info("View published toggle button is turned ON");
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_on_toggle_button_to_turn_on", e);
			LOGGER.error("Issue in clicking View Published toggle button to turn ON - Method: click_on_toggle_button_to_turn_on", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in clicking View Published toggle button to turn ON in Job Mapping page...Please Investigate!!!");
			Assert.fail("Issue in clicking View Published toggle button to turn ON in Job Mapping page...Please Investigate!!!");
		}	
	}

	
	public void user_should_verify_published_jobs_are_displaying_in_the_listing_table() {
		Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(publishedBtn)).isDisplayed());
		boolean dataavailable = publishedBtn.isDisplayed();
		try {
			if (dataavailable) {
				wait.until(ExpectedConditions.visibilityOf(publishedBtn)).isDisplayed();
				ExtentCucumberAdapter.addTestStepLog("Published jobs are displaying in the profile table as expected");
				LOGGER.info("Published jobs are displaying in the profile table as expected");
				try {
					Assert.assertTrue(!(driver.findElement(By.xpath("//button[text()='Published']")).isEnabled()));
					ExtentCucumberAdapter.addTestStepLog("Published button is disabled as expected");
					LOGGER.info("Published button is disabled as expected");
				} catch (Exception e) {
					ExtentCucumberAdapter.addTestStepLog("Published button is Enabled which is NOT expected....please raise defect!!!");
					LOGGER.info("Published button is Enabled which is NOT expected....please raise defect!!!");
					e.printStackTrace();
				}	
			} else {
				wait.until(ExpectedConditions.visibilityOf(nodataavailable)).isDisplayed();
				ExtentCucumberAdapter.addTestStepLog("No Jobs were published yet.....");
				LOGGER.info("No Jobs were published yet.....");
			}
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_verify_published_jobs_are_displaying_in_the_listing_table", e);
			LOGGER.error("Issue in verifying published jobs - Method: user_should_verify_published_jobs_are_displaying_in_the_listing_table", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying published jobs.....Please Investigate!!!");
			Assert.fail("Issue in verifying published jobs.....Please Investigate!!!");
		}
	    
	}

	public void click_on_toggle_button_to_turn_off() throws InterruptedException {
		try {
			// Scroll element into view before clicking
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", viewPublishedToggleBtn);
			Thread.sleep(500);
			try {
				wait.until(ExpectedConditions.elementToBeClickable(viewPublishedToggleBtn)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", viewPublishedToggleBtn);
				} catch (Exception s) {
					utils.jsClick(driver, viewPublishedToggleBtn);
				}
			}
			ExtentCucumberAdapter.addTestStepLog("View published toggle button is turned OFF");
			LOGGER.info("View published toggle button is turned OFF");
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_on_toggle_button_to_turn_off", e);
			LOGGER.error("Issue in clicking View Published toggle button to turn OFF - Method: click_on_toggle_button_to_turn_off", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in clicking View Published toggle button to turn OFF in Job Mapping page...Please Investigate!!!");
			Assert.fail("Issue in clicking View Published toggle button to turn OFF in Job Mapping page...Please Investigate!!!");
		}
		 
	}

	
	public void user_should_verify_unpublished_jobs_are_displaying_in_the_listing_table() {
		try {
			wait.until(ExpectedConditions.visibilityOf(publishBtn)).isDisplayed();
			ExtentCucumberAdapter.addTestStepLog("Unpublished jobs with Publish button are displaying in the profile table as expected");
			LOGGER.info("Unpublished jobs with Publish button are displaying in the profile table as expected");
			try {
				Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(publishBtn)).isEnabled());
				ExtentCucumberAdapter.addTestStepLog("Publish button is enabled as expected");
				LOGGER.info("Publish button is enabled as expected");
			} catch (Exception e) {
				ExtentCucumberAdapter.addTestStepLog("Publish button is disabled which is NOT expected....please raise defect!!!");
				Assert.fail("Publish button is disabled which is NOT expected....please raise defect!!!");
				e.printStackTrace();
			}
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_verify_unpublished_jobs_are_displaying_in_the_listing_table", e);
			LOGGER.error("Issue in verifying unpublished jobs - Method: user_should_verify_unpublished_jobs_are_displaying_in_the_listing_table", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Published jobs are displaying in the profile table which is not expected in View Published screen in Job Mapping page...Please Investigate!!!");
			Assert.fail("Published jobs are displaying in the profile table which is not expected in View Published screen in Job Mapping page...Please Investigate!!!");
		}        
	}
	
	public void user_should_verify_organization_jobs_table_title_and_headers() {
		try {
			String table1TitleText = wait.until(ExpectedConditions.visibilityOf(table1Title)).getText();
			Assert.assertEquals(table1TitleText, "Organization jobs");
			ExtentCucumberAdapter.addTestStepLog("Organization jobs table title verified successfully");
			LOGGER.info("Organization jobs table title verified successfully");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_verify_organization_jobs_table_title_and_headers_title", e);
			LOGGER.error("Issue in verifying organization jobs table title - Method: user_should_verify_organization_jobs_table_title_and_headers", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying organization jobs table title....Please Investigate!!!");
			Assert.fail("Issue in verifying organization jobs table title....Please Investigate!!!");
		}
		try {
			String table1Header1Text = wait.until(ExpectedConditions.visibilityOf(table1Header1)).getText();
			Assert.assertEquals(table1Header1Text, "NAME / JOB CODE");
			String table1Header2Text = wait.until(ExpectedConditions.visibilityOf(table1Header2)).getText();
			Assert.assertEquals(table1Header2Text, "GRADE");
			String table1Header3Text = wait.until(ExpectedConditions.visibilityOf(table1Header3)).getText();
			Assert.assertEquals(table1Header3Text, "DEPARTMENT");
			ExtentCucumberAdapter.addTestStepLog("Organization jobs table headers verified successfully");
			LOGGER.info("Organization jobs table headers verified successfully");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_verify_organization_jobs_table_title_and_headers_headers", e);
			LOGGER.error("Issue in verifying organization jobs table headers - Method: user_should_verify_organization_jobs_table_title_and_headers", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying organization jobs table headers....Please Investigate!!!");
			Assert.fail("Issue in verifying organization jobs table headers....Please Investigate!!!");
		}
		
	}

	
	public void user_should_verify_matched_success_profiles_table_title_and_headers() {
		try {
			String table2TitleText = wait.until(ExpectedConditions.visibilityOf(table2Title)).getText();
			Assert.assertEquals(table2TitleText, "Matched success profiles");
			ExtentCucumberAdapter.addTestStepLog("Matched success profiles table title verified successfully");
			LOGGER.info("Matched success profiles table title verified successfully");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_verify_matched_success_profiles_table_title_and_headers_title", e);
			LOGGER.error("Issue in verifying Matched success profiles table title - Method: user_should_verify_matched_success_profiles_table_title_and_headers", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying organization jobs table title....Please Investigate!!!");
			Assert.fail("Issue in verifying Matched success profiles table title....Please Investigate!!!");
		}
		try {
			String table2Header1Text = wait.until(ExpectedConditions.visibilityOf(table2Header1)).getText();
			Assert.assertEquals(table2Header1Text, "MATCHED PROFILE");
			String table2Header2Text = wait.until(ExpectedConditions.visibilityOf(table2Header2)).getText();
			Assert.assertEquals(table2Header2Text, "GRADE");
			String table2Header3Text = wait.until(ExpectedConditions.visibilityOf(table2Header3)).getText();
			Assert.assertEquals(table2Header3Text, "TOP RESPONSIBILITIES");
			String table2Header4Text = wait.until(ExpectedConditions.visibilityOf(table2Header4)).getText();
			Assert.assertEquals(table2Header4Text, "LEVEL / SUBLEVEL");
			String table2Header5Text = wait.until(ExpectedConditions.visibilityOf(table2Header5)).getText();
			Assert.assertEquals(table2Header5Text, "FUNCTION / SUBFUNCTION");
			ExtentCucumberAdapter.addTestStepLog("Matched success profiles table headers verified successfully");
			LOGGER.info("Matched success profiles table headers verified successfully");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_verify_matched_success_profiles_table_title_and_headers_headers", e);
			LOGGER.error("Issue in verifying Matched success profiles table headers - Method: user_should_verify_matched_success_profiles_table_title_and_headers", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying organization jobs table headers....Please Investigate!!!");
			Assert.fail("Issue in verifying organization jobs table headers....Please Investigate!!!");
		}
	}
	
	public void user_should_verify_view_other_matches_button_on_matched_success_profile_is_displaying() {
		try { 
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(job1ViewOtherMatchesBtn)).isDisplayed());
			String viewOtherMatchesText = wait.until(ExpectedConditions.visibilityOf(job1ViewOtherMatchesBtn)).getText();
			ExtentCucumberAdapter.addTestStepLog("Button with text " + viewOtherMatchesText + " is displaying on the matched success profile on first job in table");
			LOGGER.info("Button with text " + viewOtherMatchesText + " is displaying on the matched success profile on first job in table");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_verify_view_other_matches_button_on_matched_success_profile_is_displaying", e);
			LOGGER.error("Issue in displaying View Other Matches button on first job - Method: user_should_verify_view_other_matches_button_on_matched_success_profile_is_displaying", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in displaying View Other Matches button on first job....Please Investigate!!!");
			Assert.fail("Issue in displaying View Other Matches button on first job....Please Investigate!!!");
		}
		
	}
	
	public void click_on_view_other_matches_button() {
		try { 
			// Wait for the button to be present and visible before clicking
			WebElement	viewOtherMatchesBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//tbody//tr["+Integer.toString(PO15_ValidateRecommendedProfileDetails.rowNumber)+"]//button[not(contains(@id,'publish'))]")));
		
	// Scroll element into view before clicking
	js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", viewOtherMatchesBtn);
	try { Thread.sleep(500); } catch (InterruptedException ie) {}
	PerformanceUtils.waitForElement(driver, viewOtherMatchesBtn, 1);
	
		// Wait for visibility and clickability
		wait.until(ExpectedConditions.visibilityOf(viewOtherMatchesBtn));
		
		try {
			wait.until(ExpectedConditions.elementToBeClickable(viewOtherMatchesBtn)).click();
		} catch (Exception e) {
			try {
				js.executeScript("arguments[0].click();", viewOtherMatchesBtn);
			} catch (Exception s) {
				utils.jsClick(driver, viewOtherMatchesBtn);
			}
		}
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			ExtentCucumberAdapter.addTestStepLog("Clicked on View Other Matches button on the job profile : " + PO15_ValidateRecommendedProfileDetails.orgJobName);
			LOGGER.info("Clicked on View Other Matches button on the job profile : " + PO15_ValidateRecommendedProfileDetails.orgJobName);
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_on_view_other_matches_button", e);
			LOGGER.error("Issue in clicking View Other matches button on the job profile - Method: click_on_view_other_matches_button", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in clicking View Other matches button on the job profile : " + PO15_ValidateRecommendedProfileDetails.orgJobName + "...Please Investigate!!!");
			Assert.fail("Issue in clicking View Other matches button on the job profile : " + PO15_ValidateRecommendedProfileDetails.orgJobName + "...Please Investigate!!!");
		}
	}

	
	public void verify_user_landed_on_job_compare_page() throws InterruptedException {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			String compareAndSelectHeaderText = wait.until(ExpectedConditions.visibilityOf(CompareandSelectheader)).getText();
			Assert.assertEquals(compareAndSelectHeaderText, "Which profile do you want to use for this job?");
			ExtentCucumberAdapter.addTestStepLog("User landed on the Job Compare page successfully");
			LOGGER.info("User landed on the Job Compare page successfully");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_user_landed_on_job_compare_page", e);
			LOGGER.error("Issue in landing Job Compare page - Method: verify_user_landed_on_job_compare_page", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in landing Job Compare page....Please Investigate!!!");
			Assert.fail("Issue in landing Job Compare page....Please Investigate!!!");
		}
		driver.navigate().back();
		PerformanceUtils.waitForPageReady(driver, 3);
		ExtentCucumberAdapter.addTestStepLog("Navigated back to Job Mapping page");
		LOGGER.info("Navigated back to Job Mapping page");
	}

	
	public void user_should_verify_publish_button_on_matched_success_profile_is_displaying_and_clickable() {
		try { 
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(job1PublishBtn)).isDisplayed());
			wait.until(ExpectedConditions.elementToBeClickable(job1PublishBtn)).click();
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			ExtentCucumberAdapter.addTestStepLog("Publish button on matched success profile is displaying as expected on first job and clicked on button");		
			LOGGER.info("Publish button on matched success profile is displaying as expected on first job and clicked on button");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_verify_publish_button_on_matched_success_profile_is_displaying_and_clickable", e);
			LOGGER.error("Issue in displaying or clicking Publish button - Method: user_should_verify_publish_button_on_matched_success_profile_is_displaying_and_clickable", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in displaying or clicking Publish button of first job....Please Investigate!!!");
			Assert.fail("Issue in displaying or clicking Publish button of first job....Please Investigate!!!");
		}
	}
	
	public void user_should_get_success_profile_published_popup() {
		try { 
			wait.until(ExpectedConditions.visibilityOf(publishedSuccessHeader)).isDisplayed();
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(publishedSuccessHeader)).isDisplayed());
			String successHeaderText = wait.until(ExpectedConditions.visibilityOf(publishedSuccessHeader)).getText();
			Assert.assertEquals(successHeaderText, "Success Profiles Published");
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(publishedSuccessMsg)).isDisplayed());
			String successMsgText = wait.until(ExpectedConditions.visibilityOf(publishedSuccessMsg)).getText();
			Assert.assertEquals(successMsgText, "Your job profiles have been published.");
			ExtentCucumberAdapter.addTestStepLog("Popup with Header " + successHeaderText + " and with message " + successMsgText + " is displaying as expected");
			LOGGER.info("Popup with Header " + successHeaderText + " and with message " + successMsgText + " is displaying as expected");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_get_success_profile_published_popup", e);
			LOGGER.error("Issue in appearing success profile published message popup - Method: user_should_get_success_profile_published_popup", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in appearing success profile published message popup....Please Investigate!!!");
			Assert.fail("Issue in appearing success profile published message popup....Please Investigate!!!");
		}
	}
	
	public void close_the_success_profile_published_popup() {
		try {
			// Scroll element into view before clicking
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", publishedSuccessMsgCloseBtn);
			try { Thread.sleep(500); } catch (InterruptedException ie) {}
			try {
				wait.until(ExpectedConditions.elementToBeClickable(publishedSuccessMsgCloseBtn)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", publishedSuccessMsgCloseBtn);
				} catch (Exception s) {
					utils.jsClick(driver, publishedSuccessMsgCloseBtn);
				}
		}
		wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
		PerformanceUtils.waitForPageReady(driver, 2);
		ExtentCucumberAdapter.addTestStepLog("Successfully closed Success Profile Published popup");
			LOGGER.info("Successfully closed Success Profile Published popup");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("close_the_success_profile_published_popup", e);
			LOGGER.error("Issue in closing popup with message Success Profile Published - Method: close_the_success_profile_published_popup", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in closing popup with message Success Profile Published...Please Investigate!!!");
			Assert.fail("Issue in closing popup with message Success Profile Published...Please Investigate!!!");
		}
		   
	}
	
	public void clear_search_bar_in_job_mapping_page() {
		try {
			// Scroll search bar into view before clicking
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", searchBar);
			Thread.sleep(500); // Brief pause for smooth scroll
			
			wait.until(ExpectedConditions.elementToBeClickable(searchBar)).click();
			try {
				searchBar.sendKeys(Keys.CONTROL + "a");
		        searchBar.sendKeys(Keys.DELETE);
			} catch(Exception c) {
		        js.executeScript("arguments[0].value = '';", searchBar);
		        js.executeScript("arguments[0].dispatchEvent(new Event('input', { bubbles: true }));", searchBar);
		        
		}
		wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
		PerformanceUtils.waitForPageReady(driver, 2);
		ExtentCucumberAdapter.addTestStepLog("Search bar successfully cleared in Job Mapping page...");
			LOGGER.info("Search bar successfully cleared in Job Mapping page...");
		} catch(Exception e) {
			ScreenshotHandler.captureFailureScreenshot("clear_search_bar_in_job_mapping_page", e);
			ExtentCucumberAdapter.addTestStepLog("Issue in clearing Search bar in Job Mapping page...Please Investigate!!!");
			LOGGER.error("Issue in clearing Search bar in Job Mapping page - Method: clear_search_bar_in_job_mapping_page", e);
			e.printStackTrace();
			Assert.fail("Issue in clearing Search bar in Job Mapping page...Please Investigate!!!");
		}
	}
}
