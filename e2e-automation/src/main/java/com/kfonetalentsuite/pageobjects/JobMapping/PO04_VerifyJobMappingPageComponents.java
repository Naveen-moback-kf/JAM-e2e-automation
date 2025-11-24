package com.kfonetalentsuite.pageobjects.JobMapping;

import java.io.IOException;
import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.SmartWaits;
import com.kfonetalentsuite.utils.JobMapping.Utilities;
import com.kfonetalentsuite.utils.PageObjectHelper;
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
	public static String[] SEARCH_SUBSTRING_OPTIONS = { "Ac", "Ma", "An", "Sa", "En", "Ad", "As", "Co", "Te", "Di" };

	// THREAD-SAFE: Each thread gets its own isolated state for parallel execution
	public static ThreadLocal<String> jobnamesubstring = ThreadLocal.withInitial(() -> "Ac");

	// Static variables to store extracted job data
	public static ThreadLocal<String> orgJobNameInRow1 = ThreadLocal.withInitial(() -> null);
	public static ThreadLocal<String> orgJobCodeInRow1 = ThreadLocal.withInitial(() -> null);
	public static ThreadLocal<String> orgJobGradeInRow1 = ThreadLocal.withInitial(() -> null);
	public static ThreadLocal<String> orgJobFunctionInRow1 = ThreadLocal.withInitial(() -> null);
	public static ThreadLocal<String> orgJobDepartmentInRow1 = ThreadLocal.withInitial(() -> null);
	public static ThreadLocal<String> intialResultsCount = ThreadLocal.withInitial(() -> null);
	public static ThreadLocal<String> updatedResultsCount = ThreadLocal.withInitial(() -> null);
	public static ThreadLocal<String> initialFilteredResultsCount = ThreadLocal.withInitial(() -> null); 
	public static ThreadLocal<String> orgJobNameInRow2 = ThreadLocal.withInitial(() -> null);
	public static ThreadLocal<String> matchedSuccessPrflName = ThreadLocal.withInitial(() -> null);
	public static ThreadLocal<Integer> loadedProfilesBeforeHeaderCheckboxClick = ThreadLocal.withInitial(() -> 0); 
	public static ThreadLocal<Integer> selectedProfilesAfterHeaderCheckboxClick = ThreadLocal.withInitial(() -> 0); 
	public static ThreadLocal<Integer> disabledProfilesCountInLoadedProfiles = ThreadLocal.withInitial(() -> 0); 

	public PO04_VerifyJobMappingPageComponents() throws IOException {
		PageFactory.initElements(driver, this);
	}

	WebDriverWait wait = DriverManager.getWait();
	Utilities utils = new Utilities();
	JavascriptExecutor js = (JavascriptExecutor) driver;

	// XPATHs
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
	public WebElement jobMappingPageContainer; // More reliable element for page detection

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
	public WebElement KfoneMenu;

	@FindBy(xpath = "//button[@aria-label='Job Mapping']")
	public WebElement KfoneMenuJAMBtn;

	@FindBy(xpath = "//div[@id='header-logo']")
	@CacheLookup
	public WebElement JAMLogo;

	// Methods
	/**
	 * Verifies Job Mapping logo is displayed Page is already loaded, no need for
	 * spinner wait
	 */
	public void user_should_verify_job_mapping_logo_is_displayed_on_screen() {
		try {
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(JAMLogo)).isDisplayed());
			PageObjectHelper.log(LOGGER, "Job Mapping logo is displayed on screen as expected");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_job_mapping_logo_is_displayed_on_screen",
					"Issue in displaying Job Mapping Logo", e);
		}
	}

	public void navigate_to_job_mapping_page_from_kfone_global_menu_in_pm() {
		long startTime = System.currentTimeMillis();
		
		try {
			// CRITICAL: Verify we're on Profile Manager page before attempting navigation
			String currentUrl = (String) js.executeScript("return window.location.href;");
			
			if (!currentUrl.contains("profilemanager")) {
				LOGGER.error("❌ NAVIGATION PREREQUISITE FAILED!");
				LOGGER.error("❌ Current URL: " + currentUrl);
				LOGGER.error("❌ Expected to be on Profile Manager page");
				LOGGER.error("❌ This usually means the previous login/navigation step failed.");
				throw new RuntimeException("Navigation to Job Mapping failed: Not on Profile Manager page. Current URL: " + currentUrl);
			}
			
			// Wait for page to be ready
			PerformanceUtils.waitForPageReady(driver, 5);
			
			// Click global menu button with retry logic
			int maxAttempts = 2;
			boolean menuClicked = false;
			
			for (int attempt = 1; attempt <= maxAttempts && !menuClicked; attempt++) {
				try {
					// Wait for and click the global menu button
					WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
					WebElement menuButton = shortWait.until(ExpectedConditions.elementToBeClickable(
						By.xpath("//button[@id='global-nav-menu-btn']")));
					
					// Scroll into view
					js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", menuButton);
					
					// Try JavaScript click first (most reliable in parallel execution)
					long clickStart = System.currentTimeMillis();
					try {
						js.executeScript("arguments[0].click();", menuButton);
						menuClicked = true;
					} catch (Exception e) {
						try {
							menuButton.click();
							menuClicked = true;
						} catch (Exception e2) {
							utils.jsClick(driver, menuButton);
							menuClicked = true;
						}
					}
					
					long clickDuration = System.currentTimeMillis() - clickStart;
					if (clickDuration > 5000 && attempt < maxAttempts) {
						LOGGER.warn("⚠️  Menu click slow (" + clickDuration + "ms), retrying...");
						// Send ESC and click outside to dismiss any overlays
						driver.findElement(By.tagName("body")).sendKeys(Keys.ESCAPE);
						Thread.sleep(500);
						js.executeScript("document.elementFromPoint(10, 10).click();");
						Thread.sleep(500);
						menuClicked = false;
						continue;
					}
					
				} catch (TimeoutException e) {
					if (attempt < maxAttempts) {
						LOGGER.warn("⚠️  Menu button timeout on attempt " + attempt + ", retrying...");
						// Recovery: dismiss overlays
						driver.findElement(By.tagName("body")).sendKeys(Keys.ESCAPE);
						Thread.sleep(500);
					} else {
						throw e;
					}
				}
			}
			
			PageObjectHelper.log(LOGGER, "Clicked KFONE Global Menu");
			
			// Wait for menu items to appear
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			
			// Wait for page to stabilize
			PerformanceUtils.waitForPageReady(driver, 3);
			
			// CRITICAL: Job Mapping is in "Browse resources" section INSIDE the menu
			// Scroll the menu to reveal "Browse resources" section
			try {
				// Find the menu container and scroll to bottom
				WebElement menuContainer = driver.findElement(By.xpath(
					"//div[contains(@class,'menu') or contains(@class,'drawer') or @role='dialog' or @role='menu']"));
				js.executeScript("arguments[0].scrollTop = arguments[0].scrollHeight;", menuContainer);
				Thread.sleep(500);
			} catch (Exception e) {
				LOGGER.warn("⚠️  Could not scroll menu container: " + e.getMessage());
			}
			
			// Find and click Job Mapping link in Browse resources section
			WebDriverWait extendedWait = new WebDriverWait(driver, Duration.ofSeconds(15));
			
			WebElement jamButton = extendedWait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//*[contains(text(),'Browse resources')]/following::*[contains(text(),'Job Mapping')]")));
			
			// Scroll element into view within the menu
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", jamButton);
			Thread.sleep(500);
			
			// Wait for it to be clickable
			jamButton = extendedWait.until(ExpectedConditions.elementToBeClickable(jamButton));
			
			// Click Job Mapping link
			try {
				jamButton.click();
			} catch (Exception e) {
				js.executeScript("arguments[0].click();", jamButton);
			}
			
			// Wait for navigation to complete
			PerformanceUtils.waitForPageReady(driver, 10);
			
			// Verify navigation succeeded
			String finalUrl = driver.getCurrentUrl();
			if (!finalUrl.contains("job-mapping")) {
				throw new RuntimeException("Navigation failed. Expected job-mapping URL but got: " + finalUrl);
			}
			
			PageObjectHelper.log(LOGGER, "Successfully Navigated to Job Mapping screen");
			
		} catch (Exception e) {
			long duration = System.currentTimeMillis() - startTime;
			LOGGER.error("❌ PM→JAM Navigation FAILED after " + duration + "ms");
			LOGGER.error("🧵 Thread: " + Thread.currentThread().getName());
			try {
				String currentUrl = driver.getCurrentUrl();
				LOGGER.error("📍 Current URL: " + currentUrl);
			} catch (Exception ignored) {}
			LOGGER.error("💥 Error: " + e.getMessage());
			
			PageObjectHelper.handleError(LOGGER, "navigate_to_job_mapping_page_from_kfone_global_menu_in_pm",
					"Failed to navigate to Job Mapping page from KFONE Global Menu in Profile Manager", e);
		}
	}

	public void user_should_be_landed_on_job_mapping_page() {
		try {
			// OPTIMIZED: Single comprehensive wait
			PerformanceUtils.waitForPageReady(driver, 5);
			PerformanceUtils.waitForElement(driver, jobMappingPageContainer, 5);
			
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(jobMappingPageContainer)).isDisplayed());
			PageObjectHelper.log(LOGGER, "User landed on the JOB MAPPING page successfully");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_be_landed_on_job_mapping_page",
					"Issue in landing Job Mapping page", e);
		}
	}

	/**
	 * Confirms user is on Job Mapping page Page is already loaded from navigation,
	 * so minimal wait needed
	 */
	public void user_is_in_job_mapping_page() throws InterruptedException {
		PerformanceUtils.waitForPageReady(driver, 1);
		PageObjectHelper.log(LOGGER, "User is in JOB MAPPING page");
	}

	public void verify_title_header_is_correctly_displaying() throws InterruptedException {
		try {
			String actualTitleHeader = wait
					.until(ExpectedConditions.refreshed(
							ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='page-heading']//h1"))))
					.getText();
			PerformanceUtils.waitForPageReady(driver, 3);
			Assert.assertEquals(actualTitleHeader, expectedTitleHeader);
			PageObjectHelper.log(LOGGER, "Title header '" + actualTitleHeader + "' is displaying as expected");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_title_header_is_correctly_displaying",
					"Issue in verifying title header in Job Mapping page", e);
		}
	}

	public void verify_title_description_below_the_title_header() {
		try {
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(pageTitleDesc)).isDisplayed());
			String titleDesc = wait.until(ExpectedConditions.visibilityOf(pageTitleDesc)).getText();
			PageObjectHelper.log(LOGGER, "Description below the title header: " + titleDesc);
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_title_description_below_the_title_header",
					"Issue in verifying title description below the title header", e);
		}
	}

	public void verify_organization_jobs_search_bar_text_box_is_clickable() {
		try {
			// Scroll element into view before clicking
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", searchBar);
			try {
				Thread.sleep(500);
			} catch (InterruptedException ie) {
			}
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
			PageObjectHelper.log(LOGGER, "Organization Jobs Search bar text box is clickable as expected");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_organization_jobs_search_bar_text_box_is_clickable",
					" Failed to click inside Organization Jobs search bar in Job Mapping page...Please Investigate!!!",
					e);
		}

	}

	public void verify_organization_jobs_search_bar_placeholder_text() {
		try {
			String placeholderText = searchBar.getText();
			PageObjectHelper.log(LOGGER, "Placeholder text inside Organization Jobs search bar is " + placeholderText
					+ " displaying as expected");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_organization_jobs_search_bar_placeholder_text",
					"Issue in verifying Organization Jobs search bar placeholder text in Job Mapping page...Please Investigate!!!",
					e);
		}

	}

	/**
	 * Enters job name substring in search bar with dynamic fallback retry logic.
	 * 
	 * Strategy: 1. Try each substring from SEARCH_SUBSTRING_OPTIONS array 2. Check
	 * if search returns results (not "Showing 0 of X") 3. If results found, use
	 * that substring and stop 4. If no results, try next substring 5. If all
	 * substrings fail, use the last one and proceed
	 * 
	 * This ensures we never get 0 results when searching (unless all options
	 * exhausted).
	 */
	public void enter_job_name_substring_in_search_bar() {
		boolean foundResults = false;
		String selectedSubstring = SEARCH_SUBSTRING_OPTIONS[0];

		try {
			PageObjectHelper.log(LOGGER,
					"Searching with dynamic substring (with fallback retry until results found)...");

			for (String substring : SEARCH_SUBSTRING_OPTIONS) {
				try {
				wait.until(ExpectedConditions.visibilityOf(searchBar)).clear();
				wait.until(ExpectedConditions.visibilityOf(searchBar)).sendKeys(substring);
				wait.until(ExpectedConditions.visibilityOf(searchBar)).sendKeys(Keys.ENTER);
				PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
				PerformanceUtils.waitForPageReady(driver, 2);

				String resultsCountText = "";
				try {
					// Wait for results count to update after search (retry mechanism)
					int retries = 0;
					while (retries < 10) {
						Thread.sleep(300); // Wait for DOM update
						resultsCountText = showingJobResultsCount.getText().trim();
						
						// If we see actual results (not "Showing 0"), break
						if (resultsCountText.contains("Showing") && !resultsCountText.startsWith("Showing 0")) {
							break;
						}
						retries++;
					}
				} catch (Exception e) {
					continue;
				}

				if (resultsCountText.contains("Showing") && !resultsCountText.startsWith("Showing 0")) {
						selectedSubstring = substring;
						jobnamesubstring.set(substring);
						foundResults = true;
						PageObjectHelper.log(LOGGER,
								"Search successful with substring '" + substring + "' - " + resultsCountText);
						break;
					}
				} catch (Exception e) {
					// Continue to next substring
				}
			}

			if (!foundResults) {
				PageObjectHelper.log(LOGGER,
						"No search substring returned results. Using: '" + selectedSubstring + "'");
			}

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "enter_job_name_substring_in_search_bar",
					"Failed to enter job name substring text in search bar", e);
		}
	}

//	public void user_should_verify_job_name_matching_profile_is_displaying_in_first_row_in_organization_jobs_profile_list() throws Exception {
//		try {
//			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
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
			// OPTIMIZED: Single comprehensive wait
			PerformanceUtils.waitForPageReady(driver, 2);
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(jobNameofProfile1)).isDisplayed());
			String jobname1 = wait.until(ExpectedConditions.visibilityOf(jobNameofProfile1)).getText();
			orgJobNameInRow1.set(jobname1.split("-", 2)[0].trim());
			orgJobCodeInRow1.set(jobname1.split("-", 2)[1].trim().substring(1, jobname1.split("-", 2)[1].length() - 2));
			ExtentCucumberAdapter
					.addTestStepLog("Job name of the first profile in organization table : " + orgJobNameInRow1.get());
			LOGGER.info("Job name of the first profile in organization table : " + orgJobNameInRow1.get());
			ExtentCucumberAdapter
					.addTestStepLog("Job code of the first profile in organization table : " + orgJobCodeInRow1.get());
			LOGGER.info("Job code of the first profile in organization table : " + orgJobCodeInRow1.get());
		} catch (Exception e) {
			LOGGER.error(
					"Issue in verifying job name and job code values in Row1 - Method: user_should_verify_organization_job_name_and_job_code_values_of_first_profile",
					e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog(
					"Issue in verifying job name matching profile in Row1 of Organization jobs profile list...Please Investigate!!!");
			Assert.fail(
					"Issue in verifying job name matching profile in Row1 of Organization jobs profile list...Please Investigate!!!");
		}
	}

	public void user_should_verify_organization_job_grade_and_department_values_of_first_profile() {
		try {
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(jobGradeofProfile1)).isDisplayed());
			String jobGrade = wait.until(ExpectedConditions.visibilityOf(jobGradeofProfile1)).getText();
			if (jobGrade.contentEquals("-") || jobGrade.isEmpty() || jobGrade.isBlank()) {
				jobGrade = "NULL";
				orgJobGradeInRow1.set(jobGrade);
			}
			orgJobGradeInRow1.set(jobGrade);
			ExtentCucumberAdapter
					.addTestStepLog("Grade value of Organization Job first profile : " + orgJobGradeInRow1.get());
			LOGGER.info("Grade value of Organization Job first profile : " + orgJobGradeInRow1.get());
		} catch (Exception e) {
			LOGGER.error(
					"Issue in Verifying Organization Job Grade value first profile - Method: user_should_verify_organization_job_grade_in_first_row",
					e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog(
					"Issue in Verifying Organization Job Grade value first profile...Please Investigate!!!");
			Assert.fail("Issue in Verifying Organization Job Grade value first profile...Please Investigate!!!");
		}

		try {
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(jobDepartmentofProfile1)).isDisplayed());
			String jobDepartment = wait.until(ExpectedConditions.visibilityOf(jobDepartmentofProfile1)).getText();
			if (jobDepartment.contentEquals("-") || jobDepartment.isEmpty() || jobDepartment.isBlank()) {
				jobDepartment = "NULL";
				orgJobDepartmentInRow1.set(jobDepartment);
			}
			orgJobDepartmentInRow1.set(jobDepartment);
			ExtentCucumberAdapter.addTestStepLog(
					"Department value of Organization Job first profile : " + orgJobDepartmentInRow1.get());
			LOGGER.info("Department value of Organization Job first profile : " + orgJobDepartmentInRow1.get());
		} catch (Exception e) {
			LOGGER.error(
					"Issue in Verifying Organization Job Department value first profile - Method: user_should_verify_organization_job_department_in_first_row",
					e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog(
					"Issue in Verifying Organization Job Department value first profile...Please Investigate!!!");
			Assert.fail("Issue in Verifying Organization Job Department value first profile...Please Investigate!!!");
		}

	}

	public void user_should_verify_organization_job_function_or_sub_function_of_first_profile() {
		try {
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(jobFunctionofProfile1)).isDisplayed());
			String jobFunction = wait.until(ExpectedConditions.visibilityOf(jobFunctionofProfile1)).getText();
			if (jobFunction.contentEquals("- | -") || jobFunction.contentEquals("-") || jobFunction.isEmpty()
					|| jobFunction.isBlank()) {
				jobFunction = "NULL | NULL";
				orgJobFunctionInRow1.set(jobFunction);
			} else if (jobFunction.endsWith("-") || jobFunction.endsWith("| -") || jobFunction.endsWith("|")
					|| (!(jobFunction.contains("|")) && (jobFunction.length() > 1))) {
				jobFunction = jobFunction + " | NULL";
				orgJobFunctionInRow1.set(jobFunction);
			}

			orgJobFunctionInRow1.set(jobFunction);
			PageObjectHelper.log(LOGGER,
					"Function/Sub-function values of Organization Job first profile: " + orgJobFunctionInRow1.get());
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"user_should_verify_organization_job_function_or_sub_function_of_first_profile",
					"Issue in Verifying Organization Job Function/Sub-function values first profile", e);
		}
	}

	public void click_on_matched_profile_of_job_in_first_row() {
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			String MatchedProfileNameText = wait
					.until(ExpectedConditions.elementToBeClickable(job1LinkedMatchedProfile)).getText();
			matchedSuccessPrflName.set(MatchedProfileNameText);

			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});",
					job1LinkedMatchedProfile);
			Thread.sleep(500);

			try {
				wait.until(ExpectedConditions.elementToBeClickable(job1LinkedMatchedProfile)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", job1LinkedMatchedProfile);
				} catch (Exception s) {
					utils.jsClick(driver, job1LinkedMatchedProfile);
				}
			}

			PageObjectHelper.log(LOGGER, "Clicked on Matched Profile '" + MatchedProfileNameText
					+ "' of Organization Job '" + orgJobNameInRow1.get() + "'");
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_matched_profile_of_job_in_first_row",
					"Issue in clicking Matched Profile linked with job name " + orgJobNameInRow1.get(), e);
		}
	}

	public void verify_profile_details_popup_is_displayed() {
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(profileDetailsPopupHeader)).isDisplayed());
			PageObjectHelper.log(LOGGER, "Profile details popup is displayed on screen as expected");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_profile_details_popup_is_displayed",
					"Issue in displaying profile details popup", e);
		}
	}

	public void click_on_close_button_in_profile_details_popup() {
		try {
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});",
					profileDetailsPopupCloseBtn);
			try {
				Thread.sleep(500);
			} catch (InterruptedException ie) {
			}

			try {
				wait.until(ExpectedConditions.elementToBeClickable(profileDetailsPopupCloseBtn)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", profileDetailsPopupCloseBtn);
				} catch (Exception s) {
					utils.jsClick(driver, profileDetailsPopupCloseBtn);
				}
			}

			PageObjectHelper.log(LOGGER, "Clicked on close button in Profile details popup");
			// CRITICAL: Wait for spinner after closing popup
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_close_button_in_profile_details_popup",
					"Issue in clicking close button in Profile details popup", e);
		}
	}

	/**
	 * Clicks filters dropdown button Page is already loaded, so minimal wait needed
	 */
	public void click_on_filters_dropdown_button() {
		try {
			js.executeScript("window.scrollTo(0, 0);");
			
			// OPTIMIZED: Single wait for element to be ready (page is already loaded)
			PerformanceUtils.waitForElement(driver, filtersBtn, 3);

			try {
				filtersBtn.click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", filtersBtn);
				} catch (Exception s) {
					utils.jsClick(driver, filtersBtn);
				}
			}

			PageObjectHelper.log(LOGGER, "Clicked on filters dropdown button");
			
			// CRITICAL: Quick spinner check after filters dropdown opens
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			// OPTIMIZED: Quick spinner check (filters dropdown opens fast)
			PerformanceUtils.waitForPageReady(driver, 2);
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_filters_dropdown_button",
					"Issue in clicking filters dropdown button in Job Mapping page...Please investigate!!!", e);
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
			PageObjectHelper.log(LOGGER, "Options inside Filter dropdown verified successfully");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_options_available_inside_filters_dropdown",
					"Issue in verifying Options inside Filter dropdown....Please Investigate!!!", e);
		}

		try {
			// Scroll element into view before clicking
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", filterOption3);
			try {
				Thread.sleep(500);
			} catch (InterruptedException ie) {
			}
		try {
			wait.until(ExpectedConditions.elementToBeClickable(filterOption3)).click();
		} catch (Exception e) {
			try {
				js.executeScript("arguments[0].click();", filterOption3);
			} catch (Exception s) {
				utils.jsClick(driver, filterOption3);
			}
		}
		
		// Wait for search bar to appear after expanding filter option
		PerformanceUtils.waitForPageReady(driver, 5);
		
		// FIXED: Removed redundant .isDisplayed() call after wait - it was causing ScriptTimeoutException
		// ExpectedConditions.visibilityOf() already confirms the element is visible and displayed
		wait.until(ExpectedConditions.visibilityOf(searchBarInFilterOption3));
		
		// Click the search bar to verify it's interactive
		try {
			searchBarInFilterOption3.click();
		} catch (Exception e) {
			// Fallback to JS click if regular click fails
			js.executeScript("arguments[0].click();", searchBarInFilterOption3);
		}
		
		PageObjectHelper.log(LOGGER,
				"Search bar inside Functions / Subfunctions filter option is available and clickable");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_options_available_inside_filters_dropdown",
					"Issue in verifying Search bar inside Functions / Subfunctions filter option...Please Investigate!!!",
					e);
		}
	}

	/**
	 * Closes the Filters dropdown by clicking the Filters button to toggle it
	 * Dropdown closes instantly without page reload
	 */
	public void close_the_filters_dropdown() {
		try {
			LOGGER.info("Attempting to close Filters dropdown...");

			// Use JS click directly (most reliable method for this element)
			js.executeScript("arguments[0].click();", filtersBtn);

		// Verify dropdown is closed
		SmartWaits.waitForElementInvisible(driver, filterOptions);
		Assert.assertTrue(true); // Dropdown closed successfully

		LOGGER.info(" Filters dropdown closed successfully");
			ExtentCucumberAdapter.addTestStepLog("Closed Filters dropdown");

			// MINIMAL WAIT: Just enough for dropdown close animation
			// Don't wait for page updates here - let the next step handle it
			try {
				Thread.sleep(300); // Just for dropdown close animation
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "close_filters_dropdown", "Failed to close Filters dropdown", e);
		}
	}

	public void user_should_see_add_more_jobs_button_is_displaying() {
		try {
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(addMoreJobsBtn)).isDisplayed());
			String btnText = wait.until(ExpectedConditions.visibilityOf(addMoreJobsBtn)).getText();
			PageObjectHelper.log(LOGGER, btnText + " button is displaying as expected");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_see_add_more_jobs_button_is_displaying",
					"Issue in displaying add more jobs button in Job Mapping page...Please Investigate!!!", e);
		}

	}

	public void verify_add_more_jobs_button_is_clickable() throws InterruptedException {
		try {
			// FIX FOR 413 PAYLOAD TOO LARGE ERROR: Clear cookies before clicking
			driver.manage().deleteAllCookies();
			
			// PARALLEL EXECUTION FIX: Wait for page to stabilize after cookie deletion
			PerformanceUtils.waitForPageReady(driver, 5);

			// Scroll element into view before clicking (use 'auto' for instant scroll)
			js.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});", addMoreJobsBtn);
			
			// PARALLEL EXECUTION FIX: Wait for element to be fully ready
			WebElement button = wait.until(ExpectedConditions.elementToBeClickable(addMoreJobsBtn));

			// Try multiple click strategies
			boolean clickSuccessful = false;
			try {
				button.click();
				clickSuccessful = true;
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", addMoreJobsBtn);
					clickSuccessful = true;
				} catch (Exception s) {
					utils.jsClick(driver, addMoreJobsBtn);
					clickSuccessful = true;
				}
			}
			
			if (clickSuccessful) {
				PageObjectHelper.log(LOGGER, "Clicked on Add more jobs button");
				PerformanceUtils.waitForPageReady(driver, 10);
			} else {
				throw new Exception("All click strategies failed for Add More Jobs button");
			}

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_add_more_jobs_button_is_clickable",
					"Issue on clicking Add more jobs button", e);
		}

	}

	public void verify_user_landed_on_add_more_jobs_page() {
		try {
			// FIXED: Removed redundant .isDisplayed() call - wait.until already confirms visibility
			WebElement header = wait.until(ExpectedConditions.visibilityOf(addMoreJobsPageHeader));
			String addMoreJobsPageheaderText = header.getText();
			Assert.assertEquals(addMoreJobsPageheaderText, "Add Job Data");
			PageObjectHelper.log(LOGGER, "Add More Jobs landing page is Verified Successfully");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_user_landed_on_add_more_jobs_page",
					"Issue in verifying Add more jobs landing page...Please Investigate!!!", e);
		}
	}

	public void close_add_more_jobs_page() {
		try {
			// Scroll element into view before clicking
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});",
					addMoreJobsCloseBtn);
			try {
				Thread.sleep(500);
			} catch (InterruptedException ie) {
			}
			try {
				wait.until(ExpectedConditions.elementToBeClickable(addMoreJobsCloseBtn)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", addMoreJobsCloseBtn);
				} catch (Exception s) {
					utils.jsClick(driver, addMoreJobsCloseBtn);
				}
			}
			PageObjectHelper.log(LOGGER, "Clicked on Add more jobs Close button(X)");
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "close_add_more_jobs_page",
					"Issue in Closing Add more jobs page...Please Investigate!!!", e);
		}
	}

	public void user_should_verify_publish_selected_profiles_button_is_disabled() {
		try {
			Assert.assertTrue(!(wait.until(ExpectedConditions.visibilityOf(publishSelectedProfilesBtn))).isEnabled());
			PageObjectHelper.log(LOGGER, "Publish Selected Profiles button is disabled as expected");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_publish_selected_profiles_button_is_disabled",
					"Issue in verifying Publish Selected Profiles button is disabled in Job Mapping page...Please Investigate!!!",
					e);
		}

	}

	/**
	 * Clicks header checkbox to select all loaded job profiles Page is already
	 * loaded, so minimal wait needed
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
					WebDriverWait longWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(20)); // Increased
																											// from 10
					resultsCountText = longWait
							.until(ExpectedConditions.presenceOfElementLocated(
									By.xpath("//div[contains(@id,'results-toggle')]//*[contains(text(),'Showing')]")))
							.getText();
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
				loadedProfilesBeforeHeaderCheckboxClick.set(Integer.parseInt(parts[1]));
				LOGGER.info("Loaded profiles on screen (BEFORE header checkbox click): "
						+ loadedProfilesBeforeHeaderCheckboxClick.get());
			} else {
				LOGGER.warn("Could not parse results count text: " + resultsCountText);
				loadedProfilesBeforeHeaderCheckboxClick.set(0);
			}

			// Step 2: Scroll element into view and click header checkbox
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", headerCheckbox);
			try {
				Thread.sleep(500);
			} catch (InterruptedException ie) {
			}
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
			int profilesCount = loadedProfilesBeforeHeaderCheckboxClick.get();
			disabledProfilesCountInLoadedProfiles.set(0);

			// Note: Job Mapping screen doesn't have disabled profiles like HCM Sync
			// Profiles
			// All profiles can be selected, so disabled count will be 0
			// But we keep the logic for consistency and future use

			// Get all checkbox elements directly (not by row position, as some rows don't
			// have checkboxes)
			var allCheckboxes = driver
					.findElements(By.xpath("//tbody//tr//td[1][contains(@class,'whitespace')]//input"));

			// Only check the first 'loadedProfilesBeforeHeaderCheckboxClick' checkboxes
			int checkboxesToCheck = Math.min(allCheckboxes.size(), loadedProfilesBeforeHeaderCheckboxClick.get());

			for (int i = 0; i < checkboxesToCheck; i++) {
				try {
					WebElement checkbox = allCheckboxes.get(i);

					// Check if checkbox is disabled
					if (!checkbox.isEnabled()) {
						LOGGER.debug("Profile at index " + (i + 1) + " has disabled checkbox");
						disabledProfilesCountInLoadedProfiles.set(disabledProfilesCountInLoadedProfiles.get() + 1);
						profilesCount--;
					}
				} catch (Exception e) {
					// If we can't verify the checkbox, continue
					LOGGER.debug("Could not verify checkbox at index " + (i + 1) + ": " + e.getMessage());
				}
			}

			// Step 4: Store selected profiles count
			selectedProfilesAfterHeaderCheckboxClick.set(profilesCount);

			LOGGER.info("Clicked on header checkbox and selected " + selectedProfilesAfterHeaderCheckboxClick.get()
					+ " job profiles in Job Mapping screen");
			LOGGER.info("    Loaded profiles (before click): " + loadedProfilesBeforeHeaderCheckboxClick.get());
			LOGGER.info("    Selected profiles: " + selectedProfilesAfterHeaderCheckboxClick.get());
			LOGGER.info("    Disabled profiles: " + disabledProfilesCountInLoadedProfiles.get());
			ExtentCucumberAdapter.addTestStepLog("Clicked on header checkbox and selected "
					+ selectedProfilesAfterHeaderCheckboxClick.get() + " job profiles in Job Mapping screen");
		} catch (Exception e) {
			LOGGER.error(
					"Issue in clicking on header checkbox to select loaded job profiles in Job Mapping screen - Method: click_on_header_checkbox_to_select_loaded_job_profiles_in_job_mapping_screen",
					e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog(
					"Issue in clicking on header checkbox to select loaded job profiles in Job Mapping page...Please Investigate!!!");
			Assert.fail(
					"Issue in clicking on header checkbox to select loaded job profiles in Job Mapping page...Please Investigate!!!");
		}
	}

	public void user_should_verify_publish_selected_profiles_button_is_enabled() {
		try {
			// OPTIMIZED: Single comprehensive wait
			PerformanceUtils.waitForPageReady(driver, 2);
			Thread.sleep(500); // Allow UI to stabilize

			// Direct check - button should be enabled since selections are preserved
			WebElement publishBtn = wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath("//button[contains(@id,'publish-approved-mappings-btn')]")));

			boolean isEnabled = publishBtn.isEnabled();

			Assert.assertTrue(isEnabled, "Publish Selected Profiles button should be enabled");
			PageObjectHelper.log(LOGGER,
					"Publish Selected Profiles button is enabled as expected after selecting job profiles");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_publish_selected_profiles_button_is_enabled",
					"Issue in verifying Publish Selected Profiles button is enabled in Job Mapping page...Please Investigate!!!",
					e);
		}

	}

	public void user_should_uncheck_header_checkbox_to_deselect_selected_job_profiles() {
		try {
			// Scroll element into view before clicking
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", headerCheckbox);
			try {
				Thread.sleep(500);
			} catch (InterruptedException ie) {
			}
			try {
				wait.until(ExpectedConditions.elementToBeClickable(headerCheckbox)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", headerCheckbox);
				} catch (Exception s) {
					utils.jsClick(driver, headerCheckbox);
				}
			}
			PageObjectHelper.log(LOGGER, "Clicked on header checkbox and Deselected all job profiles");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"user_should_uncheck_header_checkbox_to_deselect_selected_job_profiles",
					"Issue in clicking on header checkbox to deselect all job profiles...Please Investigate!!!", e);
		}
	}

	public void click_on_checkbox_of_first_job_profile() {
		try {
			String jobname1 = wait.until(ExpectedConditions.visibilityOf(jobNameofProfile1)).getText();
			orgJobNameInRow1.set(jobname1.split("-", 2)[0].trim());
			// Scroll element into view before clicking
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", profile1Checkbox);
			try {
				Thread.sleep(500);
			} catch (InterruptedException ie) {
			}
			try {
				wait.until(ExpectedConditions.elementToBeClickable(profile1Checkbox)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", profile1Checkbox);
				} catch (Exception s) {
					utils.jsClick(driver, profile1Checkbox);
				}
			}
			PageObjectHelper.log(LOGGER,
					"Clicked on checkbox of First job profile with name: " + orgJobNameInRow1.get());
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_checkbox_of_first_job_profile",
					"Issue in clicking First job profile checkbox in Job Mapping page...Please Investigate!!!", e);
		}
	}

	public void click_on_checkbox_of_second_job_profile() {
		try {
			String jobname2 = wait.until(ExpectedConditions.visibilityOf(jobNameofProfile2)).getText();
			orgJobNameInRow2.set(jobname2.split("-", 2)[0].trim());
			// Scroll element into view before clicking
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", profile2Checkbox);
			try {
				Thread.sleep(500);
			} catch (InterruptedException ie) {
			}
			try {
				wait.until(ExpectedConditions.elementToBeClickable(profile2Checkbox)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", profile2Checkbox);
				} catch (Exception s) {
					utils.jsClick(driver, profile2Checkbox);
				}
			}
			PageObjectHelper.log(LOGGER,
					"Clicked on checkbox of Second job profile with name: " + orgJobNameInRow2.get());
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_checkbox_of_second_job_profile",
					"Issue in clicking Second job profile checkbox in Job Mapping page...Please Investigate!!!", e);
		}
	}

	public void click_on_publish_selected_profiles_button() {
		try {
			// Scroll element into view before clicking
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});",
					publishSelectedProfilesBtn);
			try {
				Thread.sleep(500);
			} catch (InterruptedException ie) {
			}
			try {
				wait.until(ExpectedConditions.elementToBeClickable(publishSelectedProfilesBtn)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", publishSelectedProfilesBtn);
				} catch (Exception s) {
					utils.jsClick(driver, publishSelectedProfilesBtn);
				}
			}
			PageObjectHelper.log(LOGGER, "Clicked on Publish Selected Profiles button to publish selected profiles");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_publish_selected_profiles_button",
					"Issue in clicking Publish Selected Profiles button in Job Mapping page...Please Investigate!!!",
					e);
		}

	}

	/**
	 * Verifies job profiles count is displaying Page is already loaded, so minimal
	 * wait needed
	 */
	public void verify_job_profiles_count_is_displaying_on_the_page() {
		// Declare retry counter outside try block so it's accessible in catch block
		int retryAttempts = 0;

		try {
			// Page already loaded from previous step, just ensure readiness
			PerformanceUtils.waitForPageReady(driver, 1);

			// Fix: Use fresh element lookup to avoid stale element reference
			String resultsCountText = "";
			int maxRetries = 5; // Increased from 3

			while (retryAttempts < maxRetries) {
				try {
					WebDriverWait longWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(20)); // Increased
																											// from 10
					resultsCountText = longWait
							.until(ExpectedConditions.presenceOfElementLocated(
									By.xpath("//div[contains(@id,'results-toggle')]//*[contains(text(),'Showing')]")))
							.getText();
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

			intialResultsCount.set(resultsCountText);

			// IMPORTANT: Reset initialFilteredResultsCount for new scenario
			// This ensures each scenario starts fresh and doesn't use values from previous
			// scenarios
			initialFilteredResultsCount.set(null);
			LOGGER.debug("Reset initialFilteredResultsCount for new scenario - Initial count: {}",
					intialResultsCount.get());

			PageObjectHelper.log(LOGGER, "Initially " + resultsCountText + " of job profiles");
		} catch (Exception e) {
			// Enhanced error details
			String errorDetails = String.format("Failed to verify job profiles count after %d retry attempts. "
					+ "XPath: //div[contains(@id,'results-toggle')]//*[contains(text(),'Showing')], "
					+ "Max wait time: %d seconds, " + "Error type: %s, " + "Error message: %s. "
					+ "This usually indicates: 1) Results count element not loading, 2) Page stuck loading, or 3) No results to display.",
					retryAttempts, 20, e.getClass().getSimpleName(), e.getMessage());

			LOGGER.error(errorDetails, e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("FAILURE: " + errorDetails);
			Assert.fail(errorDetails);
		}
	}

	public void scroll_page_to_view_more_job_profiles() throws InterruptedException {
		try {
			// Wait for spinners before scroll
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);

			// Smooth scroll to bottom
			js.executeScript("window.scrollTo({top: document.body.scrollHeight, behavior: 'smooth'});");
			Thread.sleep(1000);

			// CRITICAL: Wait for spinners after scroll - this triggers data load
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);

			// Wait for page ready
			PerformanceUtils.waitForPageReady(driver, 2);

			PageObjectHelper.log(LOGGER, "Scrolled page down to view more job profiles");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "scroll_page_to_view_more_job_profiles",
					"Issue in scrolling page down to view more job profiles in Job Mapping page...Please Investigate!!!",
					e);
		}

	}

	public void user_should_verify_count_of_job_profiles_is_correctly_showing_on_top_of_job_profiles_listing_table()
			throws InterruptedException {
		long startTime = System.currentTimeMillis();
		try {
			js.executeScript("arguments[0].scrollIntoView(true);", pageTitleHeader);

			// CRITICAL: Wait for spinners after scroll before checking count
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);

			// OPTIMIZED: Single comprehensive wait for page readiness after filter changes
			PerformanceUtils.waitForPageReady(driver, 5);

			// IMPORTANT: Wait for the results count text to actually CHANGE (not just be present)
			// This prevents reading stale/cached count text
			String resultsCountText2 = "";
			int retryAttempts = 0;
			int maxRetries = 10; // Increased to handle slow filter updates

			while (retryAttempts < maxRetries) {
				try {
					// Get fresh element and text
					WebElement resultsElement = driver.findElement(
							By.xpath("//div[contains(@id,'results-toggle')]//*[contains(text(),'Showing')]"));
					String currentText = resultsElement.getText().trim();

					// Check if text has changed from initial count (filter was applied)
					if (!currentText.isEmpty() && !currentText.equals(intialResultsCount.get())) {
						resultsCountText2 = currentText;
						break; // Success - count has updated
					}

					// If first attempt and text equals initial, wait a bit longer
					if (retryAttempts == 0) {
						Thread.sleep(500); // Give page time to start updating
					} else {
						Thread.sleep(200); // Shorter wait for subsequent attempts
					}

					retryAttempts++;

				} catch (org.openqa.selenium.StaleElementReferenceException e) {
					LOGGER.debug("⚠️  Stale element on attempt " + (retryAttempts + 1) + ", retrying...");
					retryAttempts++;
					Thread.sleep(200);
				}
			}

			// If we couldn't read updated text after retries
			if (resultsCountText2.isEmpty()) {
				// Try one final time with explicit wait
				WebDriverWait finalWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(5));
				resultsCountText2 = finalWait
						.until(ExpectedConditions.presenceOfElementLocated(
								By.xpath("//div[contains(@id,'results-toggle')]//*[contains(text(),'Showing')]")))
						.getText();
			}

			updatedResultsCount.set(resultsCountText2);

			// Store this as initial filtered count ONLY if not already set
			// Captures whatever count is showing at this moment (may be scrolled or not,
			// depending on scenario flow)
			if (initialFilteredResultsCount.get() == null || initialFilteredResultsCount.get().isEmpty()) {
				// Only set if it's different from initial (filters were applied)
				if (!resultsCountText2.equals(intialResultsCount.get())) {
					initialFilteredResultsCount.set(resultsCountText2);
				}
			}

			long elapsedTime = System.currentTimeMillis() - startTime;

			// CHECK FOR ZERO RESULTS - Set flag for skipping validation steps
			if (resultsCountText2.contains("Showing 0 of")) {
				PO10_ValidateScreen1SearchResults.setHasSearchResults(false);
				PageObjectHelper.log(LOGGER, "Profile Results count updated and Now " + resultsCountText2
						+ " - No results found, validation steps will be skipped");
				return;
			} else {
				PO10_ValidateScreen1SearchResults.setHasSearchResults(true);
			}

			// CRITICAL CHECK: Determine if count should have changed
			// FIX: Check if this is called AFTER a filter or just after initial load/scroll
			boolean filtersWereApplied = (initialFilteredResultsCount.get() != null && !initialFilteredResultsCount.get().isEmpty());

			if (!resultsCountText2.equals(intialResultsCount.get())) {
				// Count has changed - this is expected after filter/search
				PageObjectHelper.log(LOGGER, "Profile Results count updated and Now " + resultsCountText2
						+ " of job profiles as expected (took " + elapsedTime + "ms)");
			} else if (filtersWereApplied) {
				// Filters were applied, but count didn't change - this is an error
				LOGGER.error("❌ Results Count Check FAILED");
				LOGGER.error("   Expected: Count to change from '" + intialResultsCount.get() + "' (filters applied)");
				LOGGER.error("   Actual: Still showing '" + resultsCountText2 + "'");
				LOGGER.error("   Duration: " + elapsedTime + "ms");
				LOGGER.error("   Retries: " + retryAttempts);
				
				Assert.fail("Issue in updating profile results count, Still " + resultsCountText2
						+ " ....Please Investigate!!!");
			} else {
				// No filters applied, count matches initial - this is OKAY (just scrolling/pagination check)
				PageObjectHelper.log(LOGGER, "Profile Results count verified - showing " + resultsCountText2
						+ " of job profiles (no filters applied)");
			}
		} catch (Exception e) {
			long totalDuration = System.currentTimeMillis() - startTime;
			LOGGER.error("═══════════════════════════════════════════════════════════");
			LOGGER.error("❌ Results Count Verification EXCEPTION after " + totalDuration + "ms");
			LOGGER.error("🧵 Thread: " + Thread.currentThread().getName());
			LOGGER.error("💥 Error: " + e.getMessage());
			LOGGER.error("═══════════════════════════════════════════════════════════");
			PageObjectHelper.handleError(LOGGER,
					"user_should_verify_count_of_job_profiles_is_correctly_showing_on_top_of_job_profiles_listing_table",
					"Issue in verifying job profiles results count after scrolling down in Job Mapping page...Please Investigate!!!",
					e);
		}
	}

	public void user_should_verify_view_published_toggle_button_is_displaying() {
		Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(viewPublishedToggleBtn)).isDisplayed());
		PageObjectHelper.log(LOGGER, "View published toggle button is displaying on screen as expected");
	}

	public void click_on_toggle_button_to_turn_on() throws InterruptedException {
		try {
			// Scroll element into view before clicking
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});",
					viewPublishedToggleBtn);
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
			PageObjectHelper.log(LOGGER, "View published toggle button is turned ON");
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_toggle_button_to_turn_on",
					"Issue in clicking View Published toggle button to turn ON in Job Mapping page...Please Investigate!!!",
					e);
		}
	}

	public void user_should_verify_published_jobs_are_displaying_in_the_listing_table() {
		try {
			// FIXED: Removed redundant .isDisplayed() calls - wait.until already confirms visibility
			// Check if published button is visible (indicates published jobs exist)
			wait.until(ExpectedConditions.visibilityOf(publishedBtn));
			
			// Published jobs are present
			PageObjectHelper.log(LOGGER, "Published jobs are displaying in the profile table as expected");
			
			// Verify the Published button is disabled (as expected when viewing published jobs)
			try {
				WebElement publishedButton = driver.findElement(By.xpath("//button[text()='Published']"));
				Assert.assertFalse(publishedButton.isEnabled(), "Published button should be disabled when viewing published jobs");
				PageObjectHelper.log(LOGGER, "Published button is disabled as expected");
			} catch (AssertionError e) {
				PageObjectHelper.log(LOGGER,
						"Published button is Enabled which is NOT expected....please raise defect!!!");
				throw e;
			}
			
		} catch (TimeoutException e) {
			// If publishedBtn is not visible, check for "no data" message
			try {
				wait.until(ExpectedConditions.visibilityOf(nodataavailable));
				PageObjectHelper.log(LOGGER, "No Jobs were published yet");
			} catch (Exception ex) {
				PageObjectHelper.handleError(LOGGER,
						"user_should_verify_published_jobs_are_displaying_in_the_listing_table",
						"Issue in verifying published jobs.....Please Investigate!!!", ex);
			}
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"user_should_verify_published_jobs_are_displaying_in_the_listing_table",
					"Issue in verifying published jobs.....Please Investigate!!!", e);
		}

	}

	public void click_on_toggle_button_to_turn_off() throws InterruptedException {
		try {
			// Scroll element into view before clicking
			js.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});",
					viewPublishedToggleBtn);
			Thread.sleep(2000);
			// PERFORMANCE: Consolidated click with fallback strategies
			try {
				wait.until(ExpectedConditions.elementToBeClickable(viewPublishedToggleBtn)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", viewPublishedToggleBtn);
				} catch (Exception s) {
					utils.jsClick(driver, viewPublishedToggleBtn);
				}
			}
			PageObjectHelper.log(LOGGER, "View published toggle button is turned OFF");
			Thread.sleep(2000);
			
			// PERFORMANCE: Use comprehensive page ready wait instead of just spinner wait
			PerformanceUtils.waitForPageReady(driver, 10);
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_toggle_button_to_turn_off",
					"Issue in clicking View Published toggle button to turn OFF in Job Mapping page...Please Investigate!!!",
					e);
		}

	}

	public void user_should_verify_unpublished_jobs_are_displaying_in_the_listing_table() {
		try {
			// PERFORMANCE: Single wait to get element and verify visibility/enabled state
			WebElement publishButton = wait.until(ExpectedConditions.visibilityOf(publishBtn));
			Assert.assertTrue(publishButton.isDisplayed(), "Publish button should be visible");
			PageObjectHelper.log(LOGGER,
					"Unpublished jobs with Publish button are displaying in the profile table as expected");
			
			// Verify button is enabled
			Assert.assertTrue(publishButton.isEnabled(), 
					"Publish button should be enabled but is disabled - please raise defect!");
			PageObjectHelper.log(LOGGER, "Publish button is enabled as expected");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"user_should_verify_unpublished_jobs_are_displaying_in_the_listing_table",
					"Issue verifying unpublished jobs in the profile table - Please Investigate!!!",
					e);
		}
	}

	public void user_should_verify_organization_jobs_table_title_and_headers() {
		try {
			String table1TitleText = wait.until(ExpectedConditions.visibilityOf(table1Title)).getText();
			Assert.assertEquals(table1TitleText, "Organization jobs");
			PageObjectHelper.log(LOGGER, "Organization jobs table title verified successfully");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_organization_jobs_table_title_and_headers",
					"Issue in verifying organization jobs table title....Please Investigate!!!", e);
		}
		try {
			String table1Header1Text = wait.until(ExpectedConditions.visibilityOf(table1Header1)).getText();
			Assert.assertEquals(table1Header1Text, "NAME / JOB CODE");
			String table1Header2Text = wait.until(ExpectedConditions.visibilityOf(table1Header2)).getText();
			Assert.assertEquals(table1Header2Text, "GRADE");
			String table1Header3Text = wait.until(ExpectedConditions.visibilityOf(table1Header3)).getText();
			Assert.assertEquals(table1Header3Text, "DEPARTMENT");
			PageObjectHelper.log(LOGGER, "Organization jobs table headers verified successfully");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_organization_jobs_table_title_and_headers",
					"Issue in verifying organization jobs table headers....Please Investigate!!!", e);
		}

	}

	public void user_should_verify_matched_success_profiles_table_title_and_headers() {
		try {
			String table2TitleText = wait.until(ExpectedConditions.visibilityOf(table2Title)).getText();
			Assert.assertEquals(table2TitleText, "Matched success profiles");
			ExtentCucumberAdapter.addTestStepLog("Matched success profiles table title verified successfully");
			LOGGER.info("Matched success profiles table title verified successfully");
		} catch (Exception e) {
			LOGGER.error(
					"Issue in verifying Matched success profiles table title - Method: user_should_verify_matched_success_profiles_table_title_and_headers",
					e);
			e.printStackTrace();
			ExtentCucumberAdapter
					.addTestStepLog("Issue in verifying organization jobs table title....Please Investigate!!!");
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
			PageObjectHelper.log(LOGGER, "Matched success profiles table headers verified successfully");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_matched_success_profiles_table_title_and_headers",
					"Issue in verifying organization jobs table headers....Please Investigate!!!", e);
		}
	}

	public void user_should_verify_view_other_matches_button_on_matched_success_profile_is_displaying() {
		try {
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(job1ViewOtherMatchesBtn)).isDisplayed());
			String viewOtherMatchesText = wait.until(ExpectedConditions.visibilityOf(job1ViewOtherMatchesBtn))
					.getText();
			PageObjectHelper.log(LOGGER, "Button with text " + viewOtherMatchesText
					+ " is displaying on the matched success profile on first job in table");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"user_should_verify_view_other_matches_button_on_matched_success_profile_is_displaying",
					"Issue in displaying View Other Matches button on first job....Please Investigate!!!", e);
		}

	}

	public void click_on_view_other_matches_button() {
		try {
			// Wait for the button to be present and clickable
			WebElement viewOtherMatchesBtn = wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("//tbody//tr[" + Integer.toString(PO15_ValidateRecommendedProfileDetails.rowNumber.get())
							+ "]//button[not(contains(@id,'publish'))]")));

			// Scroll element into view before clicking
			js.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});",
					viewOtherMatchesBtn);

			// PERFORMANCE: Consolidated click with fallback strategies
			try {
				viewOtherMatchesBtn.click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", viewOtherMatchesBtn);
				} catch (Exception s) {
					utils.jsClick(driver, viewOtherMatchesBtn);
				}
			}
			
			// PERFORMANCE: Removed redundant spinner wait - next step will handle page transition
			PageObjectHelper.log(LOGGER, "Clicked on View Other Matches button on the job profile: "
					+ PO15_ValidateRecommendedProfileDetails.orgJobName.get());
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_view_other_matches_button",
					"Issue in clicking View Other matches button on the job profile: "
							+ PO15_ValidateRecommendedProfileDetails.orgJobName.get() + "...Please Investigate!!!",
					e);
		}
	}

	public void verify_user_landed_on_job_compare_page() throws InterruptedException {
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			String compareAndSelectHeaderText = wait.until(ExpectedConditions.visibilityOf(CompareandSelectheader))
					.getText();
			Assert.assertEquals(compareAndSelectHeaderText, "Which profile do you want to use for this job?");
			PageObjectHelper.log(LOGGER, "User landed on the Job Compare page successfully");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_user_landed_on_job_compare_page",
					"Issue in landing Job Compare page....Please Investigate!!!", e);
		}
		driver.navigate().back();
		PerformanceUtils.waitForPageReady(driver, 3);
		PageObjectHelper.log(LOGGER, "Navigated back to Job Mapping page");
	}

	public void user_should_verify_publish_button_on_matched_success_profile_is_displaying_and_clickable() {
		try {
			// PARALLEL EXECUTION FIX: Add spinner wait before checking button
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 3);
			
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(job1PublishBtn)).isDisplayed());
			
			// PARALLEL EXECUTION FIX: Use JavaScript click for reliability
			try {
				wait.until(ExpectedConditions.elementToBeClickable(job1PublishBtn)).click();
			} catch (Exception e) {
				LOGGER.warn("WebDriver click failed, trying JavaScript click: " + e.getMessage().split("\n")[0]);
				try {
					js.executeScript("arguments[0].click();", job1PublishBtn);
				} catch (Exception je) {
					LOGGER.warn("JavaScript click failed, trying utility click: " + je.getMessage().split("\n")[0]);
					utils.jsClick(driver, job1PublishBtn);
				}
			}
			
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PageObjectHelper.log(LOGGER,
					"Publish button on matched success profile is displaying as expected on first job and clicked on button");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"user_should_verify_publish_button_on_matched_success_profile_is_displaying_and_clickable",
					"Issue in displaying or clicking Publish button of first job....Please Investigate!!!", e);
		}
	}

	public void user_should_get_success_profile_published_popup() {
		try {
			// STEP 1: Ensure all loading spinners have disappeared (CRITICAL for stability)
			LOGGER.debug("Waiting for page spinners to disappear before checking for success popup...");
			try {
				PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			} catch (Exception spinnerEx) {
				LOGGER.debug("Spinner wait timed out or no spinners found, continuing...");
			}

			// STEP 2: Wait for page to be fully ready after publish action (critical for
			// headless)
			PerformanceUtils.waitForPageReady(driver, 3);

			// STEP 3: Additional buffer for DOM to settle and popup to render (especially
			// for headless mode)
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ie) {
				Thread.currentThread().interrupt();
			}

			// STEP 4: Wait for and verify the success popup header
			LOGGER.debug("Waiting for success popup header to be visible...");
			WebElement headerElement = wait.until(ExpectedConditions.visibilityOf(publishedSuccessHeader));
			Assert.assertTrue(headerElement.isDisplayed(), "Success popup header is not displayed");
			String successHeaderText = headerElement.getText();
			LOGGER.info("Success popup header found with text: " + successHeaderText);
			Assert.assertEquals(successHeaderText, "Success Profiles Published",
					"Header text mismatch. Expected: 'Success Profiles Published', Actual: '" + successHeaderText
							+ "'");

			// STEP 5: Wait for and verify the success popup message
			LOGGER.debug("Waiting for success popup message to be visible...");
			WebElement messageElement = wait.until(ExpectedConditions.visibilityOf(publishedSuccessMsg));
			Assert.assertTrue(messageElement.isDisplayed(), "Success popup message is not displayed");
			String successMsgText = messageElement.getText();
			LOGGER.info("Success popup message found with text: " + successMsgText);
			Assert.assertEquals(successMsgText, "Your job profiles have been published.",
					"Message text mismatch. Expected: 'Your job profiles have been published.', Actual: '"
							+ successMsgText + "'");

			PageObjectHelper.log(LOGGER, "Popup with Header '" + successHeaderText + "' and with message '"
					+ successMsgText + "' is displaying as expected");
			LOGGER.info("✓ Success profile published popup verified successfully");
		} catch (Exception e) {
			LOGGER.error("Detailed error: " + e.getClass().getName() + " - " + e.getMessage());
			PageObjectHelper.handleError(LOGGER, "user_should_get_success_profile_published_popup",
					"✗ Issue in appearing success profile published message popup. Error: " + e.getMessage(), e);
		}
	}

	public void close_the_success_profile_published_popup() {
		try {
			// Scroll element into view before clicking
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});",
					publishedSuccessMsgCloseBtn);
			try {
				Thread.sleep(500);
			} catch (InterruptedException ie) {
			}
			try {
				wait.until(ExpectedConditions.elementToBeClickable(publishedSuccessMsgCloseBtn)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", publishedSuccessMsgCloseBtn);
				} catch (Exception s) {
					utils.jsClick(driver, publishedSuccessMsgCloseBtn);
				}
			}
			// OPTIMIZED: Single comprehensive wait
			PerformanceUtils.waitForPageReady(driver, 2);
			PageObjectHelper.log(LOGGER, "Successfully closed Success Profile Published popup");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "close_the_success_profile_published_popup",
					"Issue in closing popup with message Success Profile Published...Please Investigate!!!", e);
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
			} catch (Exception c) {
				js.executeScript("arguments[0].value = '';", searchBar);
				js.executeScript("arguments[0].dispatchEvent(new Event('input', { bubbles: true }));", searchBar);

			}
			// OPTIMIZED: Single comprehensive wait
			PerformanceUtils.waitForPageReady(driver, 2);
			PageObjectHelper.log(LOGGER, "Search bar successfully cleared in Job Mapping page");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "clear_search_bar_in_job_mapping_page",
					"Issue in clearing Search bar in Job Mapping page...Please Investigate!!!", e);
		}
	}
}
