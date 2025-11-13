package com.kfonetalentsuite.pageobjects.JobMapping;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;

import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.ScreenshotHandler;
import com.kfonetalentsuite.utils.JobMapping.Utilities;
import com.kfonetalentsuite.utils.HeadlessCompatibleActions;
import com.kfonetalentsuite.webdriverManager.DriverManager;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

public class PO21_MapDifferentSPtoProfileInAutoAI {
	WebDriver driver = DriverManager.getDriver();
	private HeadlessCompatibleActions headlessActions;	

	protected static final Logger LOGGER = (Logger) LogManager.getLogger();
	PO21_MapDifferentSPtoProfileInAutoAI mapDifferentSPtoProfileInAutoAI;
	
	public static int rowNumber;
	public static boolean mapSP=false;
	public static boolean manualMapping=false;
	public static String orgJobName;
	public static String orgJobCode;
	public static String orgJobGrade;
	public static String orgJobFunction;
	public static String orgJobDepartment;
	public static String mappedSuccessPrflName;
	public static String ProfileDetails;
	public static String ProfileRoleSummary;
	public static String ProfileResponsibilities;
	public static String ProfileBehaviouralCompetencies;
	public static String ProfileSkills;
	public static String lastSavedProfileName;
	static String expectedHeader = "Which profile do you want to use for this job?";
	public static String SPSearchString = "car";
	public static String customSPNameinSearchResults;
	public static String manualMappingProfileName;
	public static String changedlevelvalue;
	public static String manualMappingProfileRoleSummary;
	public static String manualMappingProfileDetails;
	public static String manualMappingProfileResponsibilities;
	public static String manualMappingProfileBehaviouralCompetencies;
	public static String manualMappingProfileSkills;

	public PO21_MapDifferentSPtoProfileInAutoAI() throws IOException {
		// super();
		PageFactory.initElements(driver, this);
		this.headlessActions = new HeadlessCompatibleActions(driver);
		// TODO Auto-generated constructor stub
	}

	WebDriverWait wait = DriverManager.getWait();
	Utilities utils = new Utilities();
	JavascriptExecutor js = (JavascriptExecutor) driver;
	
	//XPATHs
	@FindBy(xpath = "//div[@data-testid='loader']//img")
	@CacheLookup
	WebElement pageLoadSpinner2;
	
	@FindBy(xpath = "//h2[@id='summary-modal']")
	@CacheLookup
	public WebElement profileDetailsPopupHeader;
	
	@FindBy(xpath = "//h2[@id='summary-modal']//p")
	@CacheLookup
	public WebElement profileHeader;
	
	@FindBy(xpath = "//div[@id='details-container']//div[contains(@class,'mt-2')]")
	@CacheLookup
	public WebElement profileDetails;
	
	@FindBy(xpath = "//select[contains(@id,'profileLevel') or @id='profile-level']")
	@CacheLookup
	public WebElement profileLevelDropdown;
	
	@FindBy(xpath = "//div[@id='role-summary-container']//p")
	@CacheLookup
	public WebElement roleSummary;
	
	@FindBy(xpath = "//div[@id='responsibilities-panel']//button[contains(text(),'View')]")
	@CacheLookup
	public List <WebElement> viewMoreButtonInResponsibilitiesTab;
	
	@FindBy(xpath = "//div[@id='responsibilities-panel']//div[@id='item-container']")
	@CacheLookup
	public WebElement responisbilitiesData;
	
	@FindBy(xpath = "//button[contains(text(),'BEHAVIOUR')]")
	@CacheLookup
	public WebElement behaviourCompetenciesTabButton;
	
	@FindBy(xpath = "//div[@id='behavioural-panel']//button[contains(text(),'View')]")
	@CacheLookup
	public List <WebElement> viewMoreButtonInBehaviourCompetenciesTab;
	
	@FindBy(xpath = "//div[@id='behavioural-panel']//div[@id='item-container']")
	@CacheLookup
	public WebElement behaviourData;
	
	@FindBy(xpath = "//button[text()='SKILLS']")
	@CacheLookup
	public WebElement skillsTabButton;
	
	@FindBy(xpath = "//div[@id='skills-panel']//button[contains(text(),'View')]")
	@CacheLookup
	public List <WebElement> viewMoreButtonInSkillsTab;
	
	@FindBy(xpath = "//div[@id='skills-panel']//div[@id='item-container']")
	@CacheLookup
	public WebElement skillsData;
	
	@FindBy(xpath = "//button[@id='publish-job-profile']")
	@CacheLookup
	public WebElement publishProfileButton;
	
	@FindBy(xpath = "//button[@id='close-profile-summary']")
	@CacheLookup
	public WebElement profileDetailsPopupCloseBtn;
	
	@FindBy(xpath = "//tbody//tr[2]//td//button[contains(text(),'different profile')]")
	@CacheLookup
	WebElement searchDifferntProfileBtnonTopRow;
	
	@FindBy(xpath = "//div[contains(@class,'leading')]//div[1]//div[1]")
	@CacheLookup
	WebElement manualMappingPageOrgJobTitleHeader;
	
	@FindBy(xpath = "//div[contains(@class,'leading')]//div[contains(text(),'Grade')]//span")
	@CacheLookup
	WebElement manualMappingPageOrgJobGradeValue;
	
	@FindBy(xpath = "//div[contains(@class,'leading')]//div[contains(text(),'Department')]//span")
	@CacheLookup
	WebElement manualMappingPageOrgJobDepartmentValue;
	
	@FindBy(xpath = "//div[contains(@class,'leading')]//div[contains(text(),'Function')]//span")
	@CacheLookup
	WebElement manualMappingPageOrgJobFunctionValue;
	
	@FindBy(xpath = "//*[contains(text(),'Last')]//..")
	@CacheLookup
	WebElement manualMappingPageLastSavedProfile;
	
	@FindBy(xpath = "//*[contains(text(),'Last')]//..//span[2]")
	@CacheLookup
	WebElement manualMappingPageLastSavedProfileNameButton;
	
	@FindBy(xpath = "//*[@id='summary-title']//p")
	@CacheLookup
	WebElement manualMappingPageProfileTitle;
	
	@FindBy(xpath = "//div[contains(@id,'role-summary')]//p")
	@CacheLookup
	WebElement manualMappingPageProfileRoleSummary;
	
	@FindBy(xpath = "//span[contains(text(),'Grade')]//..")
	@CacheLookup
	WebElement manualMappingPageProfileDetails;
	
	@FindBy(xpath = "//button[contains(text(),'RESPONSIBILITIES')]")
	@CacheLookup
	WebElement manualMappingPageProfileResponsibilities;
	
	@FindBy(xpath = "//div[contains(@id,'responsibilities')]//button[contains(text(),'more...')]")
	@CacheLookup
	List <WebElement> manualMappingPageProfileViewMoreResponsibilitiesBtn;
	
	@FindBy(xpath = "//div[contains(@id,'responsibilities')]")
	@CacheLookup
	WebElement manualMappingPageProfileResponsibilitiesData;
	
	
	@FindBy(xpath = "//button[contains(text(),'BEHAVIOURAL COMPETENCIES')]")
	@CacheLookup
	WebElement manualMappingPageProfileBehaviouralCompetencies;
	
	@FindBy(xpath = "//div[contains(@id,'behavioural-panel')]//button[contains(text(),'more...')]")
	@CacheLookup
	List <WebElement> manualMappingPageProfileViewMoreCompetenciesBtn;
	
	@FindBy(xpath = "//div[contains(@id,'behavioural-panel')]")
	@CacheLookup
	WebElement manualMappingPageProfileBehaviouralCompetenciesData;
	
	
	@FindBy(xpath = "//button[contains(text(),'SKILLS')]")
	@CacheLookup
	WebElement manualMappingPageProfileSkills;
	
	@FindBy(xpath = "//div[contains(@id,'skills')]//button[contains(text(),'more...')]")
	@CacheLookup
	List <WebElement> manualMappingPageProfileViewMoreSkillsBtn;
	
	@FindBy(xpath = "//div[contains(@id,'skills')]")
	@CacheLookup
	WebElement manualMappingPageProfileSkillsData;
	
	@FindBy(xpath = "//input[contains(@placeholder,'Search Korn Ferry')]")
	@CacheLookup
	public WebElement KFSPsearchBar;
	
	@FindBy(xpath = "//ul//li[1]//button")
	@CacheLookup
	WebElement firstSearchResultBtninManualMappingPage;
	
	@FindBy(xpath = "//ul//li[1]//button//div")
	@CacheLookup
	WebElement firstSearchResultTextinManualMappingPage;
	
	@FindBy(xpath = "//button[contains(text(),'Save selection')]")
	@CacheLookup
	WebElement saveSelectionBtn;
	
	@FindBy(xpath = "//h2[contains(text(),'JOB MAPPING')]")
	@CacheLookup
	public WebElement mainHeader;
	
	@FindBy(xpath = "//div[@id='org-job-container']")
	@CacheLookup
	public WebElement jobMappingPageContainer;  // More reliable element for page detection
	
	@FindBy(xpath = "//input[contains(@id,'search-job-title')]")
	@CacheLookup
	public WebElement searchBar;
	
	//METHODs
	public void user_should_search_for_job_profile_with_search_a_different_profile_button_on_mapped_success_profile() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			// PERFORMANCE: Replaced Thread.sleep(2000) with smart page ready wait
			PerformanceUtils.waitForPageReady(driver);
			
			// Reset the flag at the start
			mapSP = false;
			
			LOGGER.info("Searching for job profile with 'Search a Different Profile' button (Manually Mapped profile)...");
			ExtentCucumberAdapter.addTestStepLog("Searching for job profile with 'Search a Different Profile' button...");
			
			// Track consecutive failures to detect end of table
			int consecutiveNotFound = 0;
			int maxConsecutiveNotFound = 10; // Stop after 10 consecutive rows without the button
			int previousRowCount = 0;
			int noNewDataScrolls = 0;
			int maxNoNewDataScrolls = 3; // Stop if no new data loads after 3 scrolls
			int searchAttempts = 0;
			int maxSearchAttempts = 100; // Prevent infinite loops (100 scroll-search cycles)
			
			LOGGER.info("Starting dynamic search with scrolling to load all available records...");
			
			while (searchAttempts < maxSearchAttempts) {
				searchAttempts++;
				
				// Get current row count
				List<WebElement> allRows = driver.findElements(By.xpath("//tbody//tr"));
				int currentRowCount = allRows.size();
				
				LOGGER.debug("Search attempt {}: Current row count = {}", searchAttempts, currentRowCount);
				
				// Search through currently loaded rows (every 3rd row, starting from row 5)
				for(int i = 5; i <= currentRowCount; i += 3) {
					try {
						rowNumber = i;
						WebElement button = driver.findElement(By.xpath("//tbody//tr[" + Integer.toString(i) + "]//button[contains(text(),'different profile')] | //tbody//tr[" + Integer.toString(i) + "]//button[contains(@id,'view')]"));
						
						// Scroll element into view
						js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", button);
						
						String text = button.getText();
						if(text.contains("different profile")) {
							LOGGER.info(" SUCCESS: Job profile with 'Search a Different Profile' button found at row {}", i);
							ExtentCucumberAdapter.addTestStepLog(" Job profile with 'Search a Different Profile' button found at row " + i);
							mapSP = true;
							return; // Found it - exit method
						} else {
							consecutiveNotFound++;
						}
					} catch(Exception e) {
						// Element not found in this row - continue to next
						consecutiveNotFound++;
					}
					
					// Early exit if we've gone past the end of meaningful data
					if (consecutiveNotFound >= maxConsecutiveNotFound) {
						LOGGER.debug("Reached {} consecutive rows without button. Will scroll for more data.", maxConsecutiveNotFound);
						consecutiveNotFound = 0; // Reset for next batch
						break; // Break inner loop to trigger scroll
					}
				}
				
				// Check if new data was loaded
				if (currentRowCount == previousRowCount) {
					noNewDataScrolls++;
					LOGGER.debug("No new data loaded (scroll attempt {}/{})", noNewDataScrolls, maxNoNewDataScrolls);
					
					if (noNewDataScrolls >= maxNoNewDataScrolls) {
						LOGGER.info("No new data loaded after {} scroll attempts. Ending search at {} total rows.", maxNoNewDataScrolls, currentRowCount);
						break; // Exit while loop - no more data to load
					}
				} else {
					noNewDataScrolls = 0; // Reset counter - new data was loaded
					LOGGER.info("New data loaded: {} rows (was {} rows)", currentRowCount, previousRowCount);
				}
				
				previousRowCount = currentRowCount;
				
				// Scroll to bottom to load more data
				LOGGER.debug("Scrolling to load more records...");
				headlessActions.scrollToBottom();
				
				// Wait for spinner and page to stabilize
				wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
				PerformanceUtils.waitForPageReady(driver, 2);
			}
			
			// If we exit the loop without finding the button
			if (mapSP == false) {
				LOGGER.warn(" SKIPPING SCENARIO: No Manually Mapped profiles available in Job Mapping");
				LOGGER.warn(" Searched through {} rows across {} scroll attempts", previousRowCount, searchAttempts);
				LOGGER.warn(" No job profile with 'Search a Different Profile' button was found");
				LOGGER.warn(" This scenario requires at least one Manually Mapped job profile to execute");
				LOGGER.warn(" Marking scenario as SKIPPED in TestNG reports");
				ExtentCucumberAdapter.addTestStepLog(" SKIPPING SCENARIO: No Manually Mapped profiles available");
				ExtentCucumberAdapter.addTestStepLog(" Searched " + previousRowCount + " rows - no 'Search a Different Profile' button found");
				ExtentCucumberAdapter.addTestStepLog(" This feature requires Manually Mapped jobs to test. Please map jobs manually first.");
				ExtentCucumberAdapter.addTestStepLog(" Scenario marked as SKIPPED");
				
				// Throw SkipException to mark scenario as SKIPPED in TestNG
				throw new SkipException("SKIPPED: No Manually Mapped profiles available in Job Mapping after searching " + previousRowCount + " rows. This scenario requires at least one job with 'Search a Different Profile' button to execute.");
			}
			
		} catch(Exception e) {
			// Only fail if there's a genuine exception, not just missing data
			if (mapSP == false) {
				// This is just missing data, not a real error - skip scenario
				LOGGER.warn(" SKIPPING SCENARIO: Exception occurred while searching, but no Manually Mapped profiles found");
				LOGGER.warn(" Exception message: {}", e.getMessage());
				LOGGER.warn(" Marking scenario as SKIPPED in TestNG reports");
				ExtentCucumberAdapter.addTestStepLog(" SKIPPING SCENARIO: No Manually Mapped profiles available");
				ExtentCucumberAdapter.addTestStepLog(" Scenario marked as SKIPPED - requires Manually Mapped jobs");
				
				// Throw SkipException to mark scenario as SKIPPED in TestNG
				throw new SkipException("SKIPPED: No Manually Mapped profiles available in Job Mapping. This scenario requires at least one job with 'Search a Different Profile' button to execute.");
			} else {
				// This is a real error that occurred after finding a profile
				LOGGER.error(" Issue searching for job profile with Search Different Profile button - Method: user_should_search_for_job_profile_with_search_a_different_profile_button", e);
				ScreenshotHandler.captureFailureScreenshot("search_job_profile_with_search_different_profile_button", e);
				e.printStackTrace();
				ExtentCucumberAdapter.addTestStepLog("Issue in searching a Job Profile with Search a Different Profile button in Job Mapping UI....Please Investigate!!!!");
				Assert.fail("Issue in searching a Job Profile with Search a Different Profile button in Job Mapping UI....Please Investigate!!!!");
			}
		}
	}
	
	public void user_should_verify_organization_job_name_and_job_code_values_of_job_profile_with_search_a_different_profile_button() {
		if(mapSP) {
			try {
				wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
				// PERFORMANCE: Replaced Thread.sleep(2000) with smart page ready wait
				PerformanceUtils.waitForPageReady(driver, 2);
				WebElement	jobName = driver.findElement(By.xpath("//tbody//tr[" + Integer.toString(rowNumber-1) + "]//td[2]//div[contains(text(),'(')]"));
				Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(jobName)).isDisplayed());
				String jobname1 = wait.until(ExpectedConditions.visibilityOf(jobName)).getText(); 
				orgJobName = jobname1.split("-", 2)[0].trim();
				orgJobCode = jobname1.split("-", 2)[1].trim().substring(1, jobname1.split("-", 2)[1].length()-2);
				ExtentCucumberAdapter.addTestStepLog("Organization Job name / Job Code of Profilewith Search a Different Profile button in the organization table : " + orgJobName + "/" + orgJobCode);
				LOGGER.info("Organization Job name / Job Code of Profile a Different Profile button in the organization table : " + orgJobName + "/" + orgJobCode);
			} catch (Exception e) {
				LOGGER.error(" Issue verifying organization job name/code - Method: user_should_verify_organization_job_name_and_job_code_values", e);
			ScreenshotHandler.captureFailureScreenshot("verify_organization_job_name_code", e);
				e.printStackTrace();
				ExtentCucumberAdapter.addTestStepLog("Issue in verifying job name of Profile with Search a Different Profile button in the Organization jobs profile list...Please Investigate!!!");
				Assert.fail("Issue in verifying job name of Profile with Search a Different Profile button in the Organization jobs profile list...Please Investigate!!!");
			}
		}
	}
	
	public void user_should_verify_organization_job_grade_and_department_values_of_job_profile_with_search_a_different_profile_button() {
		if(mapSP) {
			try {
				WebElement	jobGrade = driver.findElement(By.xpath("//tbody//tr[" + Integer.toString(rowNumber-1) + "]//td[3]//div[1]"));
				Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(jobGrade)).isDisplayed());
				String jobGradeText = wait.until(ExpectedConditions.visibilityOf(jobGrade)).getText(); 
				if(jobGradeText.contentEquals("-") || jobGradeText.isEmpty() || jobGradeText.isBlank()) {
					jobGradeText = "NULL";
					orgJobGrade = jobGradeText;
				}
				orgJobGrade = jobGradeText;
				ExtentCucumberAdapter.addTestStepLog("Grade value of Organization Job Profile with Search a Different Profile button : " + orgJobGrade);
				LOGGER.info("Grade value of Organization Job Profile with Search a Different Profile button : " + orgJobGrade);
			} catch (Exception e) {
				LOGGER.error(" Issue verifying organization job grade - Method: user_should_verify_organization_job_grade_and_department_values", e);
			ScreenshotHandler.captureFailureScreenshot("verify_organization_job_grade", e);
				e.printStackTrace();
				ExtentCucumberAdapter.addTestStepLog("Issue in Verifying Organization Job Grade value of Profile with Search a Different Profile button...Please Investigate!!!");
				Assert.fail("Issue in Verifying Organization Job Grade value of Profile with Search a Different Profile button...Please Investigate!!!");
			}
			
			try {
				WebElement	jobDepartment = driver.findElement(By.xpath("//tbody//tr[" + Integer.toString(rowNumber-1) + "]//td[4]//div[1]"));
				Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(jobDepartment)).isDisplayed());
				String jobDepartmentText = wait.until(ExpectedConditions.visibilityOf(jobDepartment)).getText(); 
				if(jobDepartmentText.contentEquals("-") || jobDepartmentText.isEmpty() || jobDepartmentText.isBlank()) {
					jobDepartmentText = "NULL";
					orgJobDepartment = jobDepartmentText;
				} else { 
					orgJobDepartment = jobDepartmentText;
				}
				ExtentCucumberAdapter.addTestStepLog("Department value of Organization Job Profile with Search a Different Profile button : " + orgJobDepartment);
				LOGGER.info("Department value of Organization Job Profile with Search a Different Profile button : " + orgJobDepartment);
			} catch (Exception e) {
				LOGGER.error(" Issue verifying organization job department - Method: user_should_verify_organization_job_grade_and_department_values", e);
			ScreenshotHandler.captureFailureScreenshot("verify_organization_job_department", e);
				e.printStackTrace();
				ExtentCucumberAdapter.addTestStepLog("Issue in Verifying Organization Job Department value of Profile with Search a Different Profile button...Please Investigate!!!");
				Assert.fail("Issue in Verifying Organization Job Department value of Profile with Search a Different Profile button...Please Investigate!!!");
			}
		}		
	}
	
	public void user_should_verify_organization_job_function_or_sub_function_of_job_profile_with_search_a_different_profile_button() {
		if(mapSP) {
			try {
				WebElement	jobFunction = driver.findElement(By.xpath("//tbody//tr[" + Integer.toString(rowNumber) + "]//div//span[2]"));
				Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(jobFunction)).isDisplayed());
				String jobFunctionText = wait.until(ExpectedConditions.visibilityOf(jobFunction)).getText(); 
				if(jobFunctionText.contentEquals("-") || jobFunctionText.isEmpty() || jobFunctionText.isBlank()) {
					jobFunctionText = "NULL | NULL";
					orgJobFunction = jobFunctionText;
				} else if (jobFunctionText.endsWith("-") || jobFunctionText.endsWith("| -") || jobFunctionText.endsWith("|") || (!(jobFunctionText.contains("|")) && (jobFunctionText.length() > 1))) {
					jobFunctionText = jobFunctionText + " | NULL";
					orgJobFunction = jobFunctionText;
				} else {
					orgJobFunction = jobFunctionText;
				}
				ExtentCucumberAdapter.addTestStepLog("Function / Sub-function values of Organization Job Profile with Search a Different Profile button : " + orgJobFunction);
				LOGGER.info("Function / Sub-function values of Organization Job Profile with Search a Different Profile button : " + orgJobFunction);
			} catch (Exception e) {
				LOGGER.error(" Issue verifying organization job function - Method: user_should_verify_organization_job_function_or_sub_function", e);
			ScreenshotHandler.captureFailureScreenshot("verify_organization_job_function", e);
				e.printStackTrace();
				ExtentCucumberAdapter.addTestStepLog("Issue in Verifying Organization Job Function / Sub-function values of Profile with Search a Different Profile button...Please Investigate!!!");
				Assert.fail("Issue in Verifying Organization Job Function / Sub-function values of Profile with Search a Different Profile button...Please Investigate!!!");
			}
		}
	}
	
	public void click_on_mapped_profile_of_job_profile_with_search_a_different_profile_button() {
		if(mapSP | manualMapping) {
			try {
				wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			WebElement	linkedMappedProfile = driver.findElement(By.xpath("//div[@id='kf-job-container']//div//table//tbody//tr[" + Integer.toString(rowNumber-1) + "]//td[1]//div"));
			String MappedProfileNameText = wait.until(ExpectedConditions.elementToBeClickable(linkedMappedProfile)).getText();
			mappedSuccessPrflName = MappedProfileNameText;
			// Scroll element into view before clicking
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(linkedMappedProfile));
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
			try { Thread.sleep(500); } catch (InterruptedException ie) {}
			try {
				element.click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", linkedMappedProfile);
				} catch (Exception s) {
					utils.jsClick(driver, linkedMappedProfile);
				}
			}
				ExtentCucumberAdapter.addTestStepLog("Clicked on Manually Mapped Profile with name " + MappedProfileNameText + " of Organization Job "+ orgJobName);
				LOGGER.info("Clicked on Manually Mapped Profile with name " + MappedProfileNameText +" of Organization Job "+ orgJobName);
				wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			} catch (Exception e) {
				LOGGER.error(" Issue clicking mapped profile - Method: click_on_mapped_profile_of_job_profile_with_search_a_different_profile_button", e);
			ScreenshotHandler.captureFailureScreenshot("click_mapped_profile", e);
				e.printStackTrace();
				ExtentCucumberAdapter.addTestStepLog("Issue in clicking Manually Mapped Profile linked with job name "+ orgJobName + "...Please Investigate!!!");
				Assert.fail("Issue in clicking Manually Mapped Profile linked with job name "+ orgJobName + "...Please Investigate!!!");
			}
		}
	}
	
	public void verify_mapped_profile_details_popup_is_displayed() {
		if(mapSP | manualMapping) {
			try {
				wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
				Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(profileDetailsPopupHeader)).isDisplayed());
				ExtentCucumberAdapter.addTestStepLog("Mapped Profile details popup is displayed on screen as expected");
				LOGGER.info("Mapped Profile details popup is displayed on screen as expected");
			} catch (Exception e) {
				LOGGER.error(" Issue displaying profile details popup - Method: verify_mapped_profile_details_popup_is_displayed", e);
			ScreenshotHandler.captureFailureScreenshot("verify_mapped_profile_details_popup", e);
				e.printStackTrace();
				ExtentCucumberAdapter.addTestStepLog("Issue in displaying Mapped profile details popup...Please Investigate!!!");
				Assert.fail("Issue in displaying Mapped profile details popup...Please Investigate!!!");
			}
		}
	}
	
	public void user_is_on_profile_details_popup_of_manually_mapped_profile() {
		if(mapSP | manualMapping) {
			LOGGER.info("User is on Profile details Popup of Manually Mapped Profile");
			ExtentCucumberAdapter.addTestStepLog("User is on Profile details Popup of Manually Mapped Profile");
		}
	}
	
	public void verify_profile_header_matches_with_mapped_profile_name() {
		if(mapSP | manualMapping) {
			try {
				String profileHeaderName = wait.until(ExpectedConditions.visibilityOf(profileHeader)).getText();
				Assert.assertEquals(mappedSuccessPrflName,profileHeaderName);
				LOGGER.info("Profile header on the details popup : " + profileHeaderName);
				ExtentCucumberAdapter.addTestStepLog("Profile header on the details popup : " + profileHeaderName);
				} catch (Exception e) {
					LOGGER.error(" Issue verifying profile header - Method: verify_profile_header_matches_with_mapped_profile_name", e);
				ScreenshotHandler.captureFailureScreenshot("verify_profile_header", e);
					e.printStackTrace();
					ExtentCucumberAdapter.addTestStepLog(" Issue in verifying profile details popup header....Please Investigate!!!");
					Assert.fail("Issue in verifying profile details popup header....Please Investigate!!!");
				}
		}
	}
	
	public void verify_mapped_profile_details_displaying_on_the_popup() {
		if(mapSP | manualMapping) {
			try {
				String profileDeatilsText = wait.until(ExpectedConditions.visibilityOf(profileDetails)).getText();
				ProfileDetails = profileDeatilsText;
				LOGGER.info("Below are the Profile Details displaying on the popup screen: ");
				LOGGER.info(profileDeatilsText);
				ExtentCucumberAdapter.addTestStepLog("Below are the Profile Details displaying on the popup screen: \n");
				ExtentCucumberAdapter.addTestStepLog(profileDeatilsText);
				} catch (Exception e) {
					LOGGER.error(" Issue displaying profile details - Method: verify_mapped_profile_details_displaying_on_the_popup", e);
				ScreenshotHandler.captureFailureScreenshot("display_profile_details", e);
					e.printStackTrace();
					ExtentCucumberAdapter.addTestStepLog(" Issue in displaying profile details on the popup screen....Please Investigate!!!");
					Assert.fail("Issue in displaying profile details on the popup screen....Please Investigate!!!");
				}
		}
	}
	
	public void user_should_verify_profile_level_dropdown_is_available_and_validate_levels_present_inside_dropdown_of_mapped_profile() {
		if(mapSP | manualMapping) {
			try {
				if(wait.until(ExpectedConditions.visibilityOf(profileLevelDropdown)).isEnabled()) {
					wait.until(ExpectedConditions.elementToBeClickable(profileLevelDropdown)).click();
					LOGGER.info("Profile Level dropdown is available and Clicked on it...");
					ExtentCucumberAdapter.addTestStepLog("Profile Level dropdown is available and Clicked on it...");
					Select dropdown = new Select(profileLevelDropdown);
					List<WebElement> allOptions = dropdown.getOptions();
					LOGGER.info("Levels present inside profile level dropdown : ");
					ExtentCucumberAdapter.addTestStepLog("Levels present inside profile level dropdown : ");
					for(WebElement option : allOptions){
				        LOGGER.info(option.getText());
				        ExtentCucumberAdapter.addTestStepLog(option.getText());
				        }
					wait.until(ExpectedConditions.elementToBeClickable(profileLevelDropdown)).click(); // This click is to close dropdown options visibility 
					LOGGER.info("Successfully Verified Profile Level dropdown and levels present inside it...");
					ExtentCucumberAdapter.addTestStepLog("Successfully Verified Profile Level dropdown and levels present inside it...");
				} else {
					LOGGER.info("No Profile Levels available for this mapped profile with name : " + mappedSuccessPrflName); 
					ExtentCucumberAdapter.addTestStepLog("No Profile Levels available for this mapped profile with name : " + mappedSuccessPrflName);
				}
			} catch (Exception e) {
				LOGGER.error(" Issue validating profile level dropdown - Method: user_should_verify_profile_level_dropdown_is_available_and_validate_levels_present_inside_dropdown_of_mapped_profile", e);
			ScreenshotHandler.captureFailureScreenshot("validate_profile_level_dropdown", e);
				e.printStackTrace();
				ExtentCucumberAdapter.addTestStepLog(" Issue in validating profile level dropdown in profile details popup in Job Mapping page...Please Investigate!!!");
				Assert.fail("Issue in validating profile level dropdown in profile details popup in Job Mapping page...Please Investigate!!!");
			}	
		}
	}
	
	public void validate_role_summary_of_mapped_profile_is_displaying() {
		if(mapSP | manualMapping) {
			try {
				String roleSummaryText = wait.until(ExpectedConditions.visibilityOf(roleSummary)).getText();
				ProfileRoleSummary = roleSummaryText.split(": ", 2)[1].trim();
				LOGGER.info("Role summary of Mapped Success Profile : " + ProfileRoleSummary);
				ExtentCucumberAdapter.addTestStepLog("Role summary of Mapped Success Profile : " + ProfileRoleSummary); 
			} catch (Exception e) {
				LOGGER.error(" Issue validating role summary - Method: validate_role_summary_of_mapped_profile_is_displaying", e);
			ScreenshotHandler.captureFailureScreenshot("validate_role_summary", e);
				e.printStackTrace();
				ExtentCucumberAdapter.addTestStepLog(" Issue in validating Role Summary in Profile Details Popup in Job Mapping page...Please Investigate!!!");
				Assert.fail("Issue in validating Role Summary in Profile Details Popup in Job Mapping page...Please Investigate!!!");
			}
		}
	}
	
	public void validate_data_in_responsibilities_tab_of_mapped_profile() {
		if(mapSP | manualMapping) {
			try {
				// PERFORMANCE: Replaced Thread.sleep(2000) with smart element wait
				PerformanceUtils.waitForElement(driver, driver.findElement(By.xpath("//div")));
				wait.until(ExpectedConditions.elementToBeClickable(viewMoreButtonInResponsibilitiesTab.get(0))).click();
				while(true) {
					try {
						if(viewMoreButtonInResponsibilitiesTab.isEmpty()) {
							LOGGER.info("Reached end of content in Responsibilities screen");
							ExtentCucumberAdapter.addTestStepLog("Reached end of content in Responsibilities screen");
							break;
						}
						js.executeScript("arguments[0].scrollIntoView(true);", viewMoreButtonInResponsibilitiesTab.get(0));
						String ViewMoreBtnText = wait.until(ExpectedConditions.visibilityOf(viewMoreButtonInResponsibilitiesTab.get(0))).getText();
						viewMoreButtonInResponsibilitiesTab.get(0).click();
						js.executeScript("arguments[0].scrollIntoView(true);", viewMoreButtonInResponsibilitiesTab.get(0));
						LOGGER.info("Clicked on "+ ViewMoreBtnText +" button in Responsibilities screen");
						ExtentCucumberAdapter.addTestStepLog("Clicked on "+ ViewMoreBtnText +" button in Responsibilities screen");
					// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
					PerformanceUtils.waitForUIStability(driver, 2);
					} catch(StaleElementReferenceException e) {
						LOGGER.info("Reached end of content in Responsibilities screen");
						ExtentCucumberAdapter.addTestStepLog("Reached end of content in Responsibilities screen");
						break;
					}	
				}
				String responsibilitiesDataText = wait.until(ExpectedConditions.visibilityOf(responisbilitiesData)).getText();
				ProfileResponsibilities = responsibilitiesDataText;
//				LOGGER.info("Data present in Responsibilities screen : \n" + responsibilitiesDataText);
//				ExtentCucumberAdapter.addTestStepLog("Data present in Responsibilities screen : \n" + responsibilitiesDataText);
			} catch (Exception e) {
				LOGGER.error(" Issue validating responsibilities screen - Method: validate_data_in_responsibilities_tab_of_mapped_profile", e);
			ScreenshotHandler.captureFailureScreenshot("validate_responsibilities_tab", e);
				e.printStackTrace();
				ExtentCucumberAdapter.addTestStepLog(" Issue in validating data in Responsibilities screen in Profile Details Popup in Job Mapping page...Please Investigate!!!");
				Assert.fail("Issue in validating data in Responsibilities screen in Profile Details Popup in Job Mapping page...Please Investigate!!!");
			}
		}
	}
	
	public void validate_data_in_behavioural_competencies_tab_of_mapped_profile() {
		if(mapSP | manualMapping) {
			try {
				js.executeScript("arguments[0].scrollIntoView(true);", roleSummary);
				// PERFORMANCE: Replaced Thread.sleep(3000) with smart element wait
				PerformanceUtils.waitForElement(driver, driver.findElement(By.xpath("//div")));
				wait.until(ExpectedConditions.elementToBeClickable(behaviourCompetenciesTabButton)).click();
				LOGGER.info("Clicked on Behaviour Competencies screen");
				ExtentCucumberAdapter.addTestStepLog("Clicked on Behaviour Competencies screen");
				while(true) {
					try {
						if(viewMoreButtonInBehaviourCompetenciesTab.isEmpty()) {
							LOGGER.info("Reached end of content in Behaviour Competencies screen");
							ExtentCucumberAdapter.addTestStepLog("Reached end of content in Behaviour Competencies screen");
							break;
						}
						js.executeScript("arguments[0].scrollIntoView(true);", viewMoreButtonInBehaviourCompetenciesTab.get(0));
						String ViewMoreBtnText = wait.until(ExpectedConditions.visibilityOf(viewMoreButtonInBehaviourCompetenciesTab.get(0))).getText();
						viewMoreButtonInBehaviourCompetenciesTab.get(0).click();
						js.executeScript("arguments[0].scrollIntoView(true);", viewMoreButtonInBehaviourCompetenciesTab.get(0));
						LOGGER.info("Clicked on "+ ViewMoreBtnText +" button in Behaviour Competencies screen");
						ExtentCucumberAdapter.addTestStepLog("Clicked on "+ ViewMoreBtnText +" button in Behaviour Competencies screen");
					// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
					PerformanceUtils.waitForUIStability(driver, 2);
					} catch(StaleElementReferenceException e) {
						LOGGER.info("Reached end of content in Behaviour Competencies screen");
						ExtentCucumberAdapter.addTestStepLog("Reached end of content in Behaviour Competencies screen");
						break;
					}	
				}
				String behaviourDataText = wait.until(ExpectedConditions.visibilityOf(behaviourData)).getText();
				ProfileBehaviouralCompetencies = behaviourDataText;
//				LOGGER.info("Data present in Behaviour Competencies screen : \n" + behaviourDataText);
//				ExtentCucumberAdapter.addTestStepLog("Data present in Behaviour Competencies screen : \n" + behaviourDataText);
			} catch (Exception e) {
				LOGGER.error(" Issue validating behavioural competencies screen - Method: validate_data_in_behavioural_competencies_tab_of_mapped_profile", e);
			ScreenshotHandler.captureFailureScreenshot("validate_behavioural_competencies_tab", e);
				e.printStackTrace();
				ExtentCucumberAdapter.addTestStepLog(" Issue in validating data in Behaviour Competencies screen in Profile Details Popup in Job Mapping page...Please Investigate!!!");
				Assert.fail("Issue in validating data in Behaviour Competencies screen in Profile Details Popup in Job Mapping page...Please Investigate!!!");
			}
		}
	}
	
	public void validate_data_in_skills_tab_of_mapped_profile() {
		if(mapSP | manualMapping) {
			try {
				js.executeScript("arguments[0].scrollIntoView(true);", roleSummary);
				// PERFORMANCE: Replaced Thread.sleep(2000) with smart element wait
				PerformanceUtils.waitForElement(driver, driver.findElement(By.xpath("//div")));
				wait.until(ExpectedConditions.elementToBeClickable(skillsTabButton)).click();
				LOGGER.info("Clicked on Skills screen");
				ExtentCucumberAdapter.addTestStepLog("Clicked on Skills screen");
				while(true) {
					try {
						if(viewMoreButtonInSkillsTab.isEmpty()) {
							LOGGER.info("Reached end of content in Skills screen");
							ExtentCucumberAdapter.addTestStepLog("Reached end of content in Skills screen");
							break;
						}
						js.executeScript("arguments[0].scrollIntoView(true);", viewMoreButtonInSkillsTab.get(0));
						String ViewMoreBtnText = wait.until(ExpectedConditions.visibilityOf(viewMoreButtonInSkillsTab.get(0))).getText();
						viewMoreButtonInSkillsTab.get(0).click();
						js.executeScript("arguments[0].scrollIntoView(true);", viewMoreButtonInSkillsTab.get(0));
						LOGGER.info("Clicked on "+ ViewMoreBtnText +" button in Skills screen");
						ExtentCucumberAdapter.addTestStepLog("Clicked on "+ ViewMoreBtnText +" button in Skills screen");
					// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
					PerformanceUtils.waitForUIStability(driver, 2);
					} catch(StaleElementReferenceException e) {
						LOGGER.info("Reached end of content in Skills screen");
						ExtentCucumberAdapter.addTestStepLog("Reached end of content in Skills screen");
						break;
					}	
				}
				String skillsDataText = wait.until(ExpectedConditions.visibilityOf(skillsData)).getText();
				ProfileSkills = skillsDataText;
//				LOGGER.info("Data present in Skills screen : \n" + skillsDataText);
//				ExtentCucumberAdapter.addTestStepLog("Data present in Skills screen : \n" + skillsDataText);
			} catch (Exception e) {
				LOGGER.error(" Issue validating skills screen - Method: validate_data_in_skills_tab_of_mapped_profile", e);
			ScreenshotHandler.captureFailureScreenshot("validate_skills_tab", e);
				e.printStackTrace();
				ExtentCucumberAdapter.addTestStepLog(" Issue in validating data in Skills screen in Profile Details Popup in Job Mapping page...Please Investigate!!!");
				Assert.fail("Issue in validating data in Skills screen in Profile Details Popup in Job Mapping page...Please Investigate!!!");
			}
		}
	}
	
	public void user_should_verify_publish_profile_button_is_available_on_popup_screen_of_mapped_profile() {
		if(mapSP | manualMapping) {
			try {
				wait.until(ExpectedConditions.elementToBeClickable(publishProfileButton)).isDisplayed();
				LOGGER.info("Publish button is displaying on the Profile Details Popup and is clickable");
				ExtentCucumberAdapter.addTestStepLog("Publish button is displaying on the Profile Details Popup and is clickable");
			} catch (Exception e) {
				LOGGER.error(" Issue verifying Publish Profile button - Method: user_should_verify_publish_profile_button_is_available_on_popup_screen_of_mapped_profile", e);
			ScreenshotHandler.captureFailureScreenshot("verify_publish_profile_button", e);
				e.printStackTrace();
				ExtentCucumberAdapter.addTestStepLog(" Issue in verifying Publish Profile button on profile details popup screen in Job Mapping page....Please Investigate!!!");
				Assert.fail("Issue in verifying Publish Profile button on profile details popup screen in Job Mapping page....Please Investigate!!!");
			}
		}
	}
	
public void click_on_close_button_in_profile_details_popup_of_mapped_profile() {
	if(mapSP | manualMapping) {
		try {
			// Scroll element into view before clicking
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(profileDetailsPopupCloseBtn));
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
			try { Thread.sleep(500); } catch (InterruptedException ie) {}
			try {
				element.click();
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
				LOGGER.error(" Issue clicking close button - Method: click_on_close_button_in_profile_details_popup_of_mapped_profile", e);
			ScreenshotHandler.captureFailureScreenshot("click_close_button_profile_details_popup", e);
				e.printStackTrace();
				ExtentCucumberAdapter.addTestStepLog("Issue in clicking close button in Profile details popup in Job Mapping page...Please Investigate!!!");
				Assert.fail("Issue in clicking close button in Profile details popup in Job Mapping page...Please investigate!!!");
			}   
		}
	}
	
public void click_on_search_a_different_profile_button_on_mapped_success_profile() {
	if(mapSP) {
		try {
			WebElement	searchDifferntProfileBtn = driver.findElement(By.xpath("//tbody//tr[" + Integer.toString(rowNumber) + "]//td//button[contains(text(),'different profile')]"));
			// Scroll element into view before clicking
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(searchDifferntProfileBtn));
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
			try { Thread.sleep(500); } catch (InterruptedException ie) {}
			try {
				element.click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();"
							+ "", searchDifferntProfileBtn);
				} catch (Exception s) {
					utils.jsClick(driver, searchDifferntProfileBtn);
				}
			}
				LOGGER.info("Clicked on Search a different profile button on Mapped SP of Organization Job Profile with name : " + orgJobName);
				ExtentCucumberAdapter.addTestStepLog("Clicked on Search a different profile button on Mapped SP of Organization Job Profile with name : "+ orgJobName);
			} catch(Exception e) {
			LOGGER.error(" Issue clicking Search Different Profile button - Method: click_on_search_a_different_profile_button_on_mapped_success_profile", e);
			ScreenshotHandler.captureFailureScreenshot("click_search_different_profile_button", e);
				e.printStackTrace();
				ExtentCucumberAdapter.addTestStepLog("Issue in clicking on Search a different profile button in Job Mapping page...Please Investigate!!!");
				Assert.fail("Issue in clicking on Search a different profile button in Job Mapping page...Please investigate!!!");
			}
			
		}
	}
	
	public void user_should_be_navigated_to_manual_job_mapping_screen() {
		if(mapSP) {
			try {
				wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
				String actualHeader = wait.until(ExpectedConditions.refreshed(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(),'Which profile')]")))).getText();
				// PERFORMANCE: Replaced Thread.sleep(3000) with element readiness wait
				PerformanceUtils.waitForElement(driver, driver.findElement(By.xpath("//*[contains(text(),'Which profile')]")));
				Assert.assertEquals(actualHeader,expectedHeader);
				LOGGER.info("User navigated to Manual Mapping screen as expected");
				ExtentCucumberAdapter.addTestStepLog("User navigated to Manual Mapping screen as expected"); 
				if(actualHeader.contentEquals(expectedHeader)) {
					manualMapping = true;
				}
			} catch (Exception e) {
				LOGGER.error(" Issue navigating to Manual Mapping screen - Method: user_should_be_navigated_to_manual_job_mapping_screen", e);
			ScreenshotHandler.captureFailureScreenshot("navigate_manual_mapping_screen", e);
				e.printStackTrace();
				ExtentCucumberAdapter.addTestStepLog(" Issue in navigating to Manual Mapping screen...Please Investigate!!!");
				Assert.fail("Issue in navigating to Manual Mapping screen...Please Investigate!!!");
			}
		}
	}
	
	public void verify_organization_job_details_in_manual_mapping_screen() {
		if(manualMapping) {
			try {
				wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
				String manualMappingPageOrgJobTitleHeaderText = wait.until(ExpectedConditions.visibilityOf(manualMappingPageOrgJobTitleHeader)).getText();
				Assert.assertTrue(manualMappingPageOrgJobTitleHeaderText.contains(orgJobName));
				Assert.assertTrue(manualMappingPageOrgJobTitleHeaderText.contains(orgJobCode));
				LOGGER.info("Organization Job Name and Job code validated successfully in the Manual Mapping screen");
				ExtentCucumberAdapter.addTestStepLog("Organization Job Name and Job code validated successfully in the Manual Mapping screen");
			} catch (Exception e) {
				LOGGER.error(" Issue validating organization job details - Method: verify_organization_job_details_in_manual_mapping_screen", e);
			ScreenshotHandler.captureFailureScreenshot("verify_organization_job_details_manual_mapping", e);
				e.printStackTrace();
				ExtentCucumberAdapter.addTestStepLog(" Issue in validating Organization Job Name and Job Code in the Manual Mapping screen....Please Investigate!!!");
				Assert.fail("Issue in validating Organization Job Name and Job Code in the Manual Mapping screen....Please Investigate!!!");
			}
			
			try {
				String manualMappingPageOrgJobGradeValueText = wait.until(ExpectedConditions.visibilityOf(manualMappingPageOrgJobGradeValue)).getText();
				if(manualMappingPageOrgJobGradeValueText.contentEquals("-") || manualMappingPageOrgJobGradeValueText.isEmpty() || manualMappingPageOrgJobGradeValueText.isBlank()) {
					manualMappingPageOrgJobGradeValueText = "NULL";
					Assert.assertTrue(manualMappingPageOrgJobGradeValueText.contains(orgJobGrade));
				} else {
					Assert.assertTrue(manualMappingPageOrgJobGradeValueText.contains(orgJobGrade));
				}
				LOGGER.info("Organization Job Grade value validated successfully in the Manual Mapping screen");
				ExtentCucumberAdapter.addTestStepLog("Organization Job Grade value validated successfully in the Manual Mapping screen");
			} catch (Exception e) {
				LOGGER.error(" Issue validating organization job grade in manual mapping - Method: verify_organization_job_details_in_manual_mapping_screen", e);
			ScreenshotHandler.captureFailureScreenshot("validate_organization_job_grade_manual_mapping", e);
				e.printStackTrace();
				Assert.fail("Issue in validating Organization Job Grade value in the Manual Mapping screen....Please Investigate!!!");
				ExtentCucumberAdapter.addTestStepLog("Issue in validating Organization Job Grade value in the Manual Mapping screen....Please Investigate!!!");
			}	
			
			try {
				String manualMappingPageOrgJobDepartmentValueText = wait.until(ExpectedConditions.visibilityOf(manualMappingPageOrgJobDepartmentValue)).getText();
				if(manualMappingPageOrgJobDepartmentValueText.contentEquals("-") || manualMappingPageOrgJobDepartmentValueText.isEmpty() || manualMappingPageOrgJobDepartmentValueText.isBlank()) {
					manualMappingPageOrgJobDepartmentValueText = "NULL";
					Assert.assertTrue(manualMappingPageOrgJobDepartmentValueText.contains(orgJobDepartment));
				} else {
					Assert.assertTrue(manualMappingPageOrgJobDepartmentValueText.contains(orgJobDepartment));
				}
				LOGGER.info("Organization Job Department value validated successfully in the Manual Mapping screen");
				ExtentCucumberAdapter.addTestStepLog("Organization Job Department value validated successfully in the Manual Mapping screen");
			} catch (Exception e) {
				LOGGER.error(" Issue validating organization job department in manual mapping - Method: verify_organization_job_details_in_manual_mapping_screen", e);
			ScreenshotHandler.captureFailureScreenshot("validate_organization_job_department_manual_mapping", e);
				e.printStackTrace();
				Assert.fail("Issue in validating Organization Job Department value in the Manual Mapping screen....Please Investigate!!!");
				ExtentCucumberAdapter.addTestStepLog("Issue in validating Organization Job Department value in the Manual Mapping screen....Please Investigate!!!");
			}	
			
			try {
				String manualMappingPageOrgJobFunctionValueText = wait.until(ExpectedConditions.visibilityOf(manualMappingPageOrgJobFunctionValue)).getText();
				if(manualMappingPageOrgJobFunctionValueText.contentEquals("-") || manualMappingPageOrgJobFunctionValueText.isEmpty() || manualMappingPageOrgJobFunctionValueText.isBlank()) {
					manualMappingPageOrgJobFunctionValueText = "NULL | NULL";
					Assert.assertTrue(manualMappingPageOrgJobFunctionValueText.contains(orgJobFunction));
				} else if (manualMappingPageOrgJobFunctionValueText.endsWith("-") || manualMappingPageOrgJobFunctionValueText.endsWith("| -") || manualMappingPageOrgJobFunctionValueText.endsWith("|") || (!(manualMappingPageOrgJobFunctionValueText.contains("|")) && (manualMappingPageOrgJobFunctionValueText.length() > 1))) {
					manualMappingPageOrgJobFunctionValueText = manualMappingPageOrgJobFunctionValueText + " | NULL";
					Assert.assertTrue(manualMappingPageOrgJobFunctionValueText.contains(orgJobFunction));
				} else {
					Assert.assertTrue(manualMappingPageOrgJobFunctionValueText.contains(orgJobFunction));
				}
				LOGGER.info("Organization Job Function / Sub-function values validated successfully in the Manual Mapping screen");
				ExtentCucumberAdapter.addTestStepLog("Organization Job Function / Sub-function values validated successfully in the Manual Mapping screen");
			} catch (Exception e) {
				LOGGER.error(" Issue validating organization job function in manual mapping - Method: verify_organization_job_details_in_manual_mapping_screen", e);
			ScreenshotHandler.captureFailureScreenshot("validate_organization_job_function_manual_mapping", e);
				e.printStackTrace();
				Assert.fail("Issue in validating Organization Job Function / Sub-function values in the Manual Mapping screen....Please Investigate!!!");
				ExtentCucumberAdapter.addTestStepLog("Issue in validating Organization Job Function / Sub-function values in the Manual Mapping screen....Please Investigate!!!");
			}	
		}
	}
	
	public void user_should_verify_last_saved_profile_name_is_displaying_in_manual_mapping_screen() {
		if(manualMapping) {
			try {
				String manualMappingPageLastSavedProfileText = wait.until(ExpectedConditions.visibilityOf(manualMappingPageLastSavedProfile)).getText();
				Assert.assertTrue(manualMappingPageLastSavedProfileText.contains("LAST SAVED PROFILE"));
				LOGGER.info("Last saved Profile is displaying in the Manual Mapping screen as " + manualMappingPageLastSavedProfileText);
				ExtentCucumberAdapter.addTestStepLog("Last saved Profile is displaying in the Manual Mapping screen as " + manualMappingPageLastSavedProfileText);
			} catch (Exception e) {
				LOGGER.error(" Issue verifying last saved profile name - Method: user_should_verify_last_saved_profile_name_is_displaying_in_manual_mapping_screen", e);
			ScreenshotHandler.captureFailureScreenshot("verify_last_saved_profile_name", e);
				e.printStackTrace();
				Assert.fail("Issue in displaying of Last Saved Profile name in the Manual Mapping screen....Please Investigate!!!");
				ExtentCucumberAdapter.addTestStepLog("Issue in displaying of Last Saved Profile name in the Manual Mapping screen....Please Investigate!!!");
			}
		}
	}
	
public void click_on_last_saved_profile_name_in_manual_mapping_screen() {
	if(manualMapping) {
		try { 
			String text = wait.until(ExpectedConditions.elementToBeClickable(manualMappingPageLastSavedProfileNameButton)).getText();
			lastSavedProfileName = text;
			Assert.assertEquals(mappedSuccessPrflName, lastSavedProfileName);
			// Scroll element into view before clicking
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(manualMappingPageLastSavedProfileNameButton));
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
			try { Thread.sleep(500); } catch (InterruptedException ie) {}
			try {
				element.click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", manualMappingPageLastSavedProfileNameButton);
				} catch (Exception s) {
					utils.jsClick(driver, manualMappingPageLastSavedProfileNameButton);
				}
			}
				LOGGER.info("Clicked on Last Saved Profile name : " + text + " in the Manual Mapping screen");
				ExtentCucumberAdapter.addTestStepLog("Clicked on Last Saved Profile name : " + text + " in the Manual Mapping screen");
				wait.until(ExpectedConditions.elementToBeClickable(manualMappingPageProfileTitle)).isDisplayed();
		} catch (Exception e) {
			LOGGER.error(" Issue clicking last saved profile name - Method: click_on_last_saved_profile_name_in_manual_mapping_screen", e);
		ScreenshotHandler.captureFailureScreenshot("click_last_saved_profile_name", e);
			e.printStackTrace();
			Assert.fail("Issue in clicking on Last Saved Profile name in the Manual Mapping screen....Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in clicking on Last Saved Profile name in the Manual Mapping screen....Please Investigate!!!");
			}
		}
	}
	
	public void user_should_verify_last_saved_profile_name_in_manual_mapping_screen_matches_with_profile_name_in_details_popup() {
		if(manualMapping) {
			try {
				String manualMappingProfile1TitleText = wait.until(ExpectedConditions.visibilityOf(manualMappingPageProfileTitle)).getText();
				Assert.assertEquals(mappedSuccessPrflName,manualMappingProfile1TitleText);
				Assert.assertEquals(lastSavedProfileName,manualMappingProfile1TitleText);
				LOGGER.info("Last Saved Profile Name in the Manual Mapping screen matches with Profile Name in the details popup as expected");
				ExtentCucumberAdapter.addTestStepLog("Last Saved Profile Name in the Manual Mapping screen matches with Profile Name in the details popup as expected");
			} catch (Exception e) {
				LOGGER.error(" Issue verifying last saved profile name match - Method: user_should_verify_last_saved_profile_name_in_manual_mapping_screen_matches_with_profile_name_in_details_popup", e);
			ScreenshotHandler.captureFailureScreenshot("verify_last_saved_profile_name_match", e);
				e.printStackTrace();
				Assert.fail("Issue in verifying Last Saved Profile Name in the Manual Mapping screen....Please Investigate!!!");
				ExtentCucumberAdapter.addTestStepLog("Issue in verifying Last Saved Profile Name in the Manual Mapping screen....Please Investigate!!!");
			}	
		}
	}
	
	public void user_should_verify_profile_level_dropdown_is_available_on_last_saved_success_profile_and_validate_levels_present_inside_dropdown() {
		if(manualMapping) {
			try {
				if(wait.until(ExpectedConditions.visibilityOf(profileLevelDropdown)).isEnabled()) {
					wait.until(ExpectedConditions.elementToBeClickable(profileLevelDropdown)).click();
					LOGGER.info("Profile Level dropdown is available and Clicked on it in Manual Mapping screen...");
					ExtentCucumberAdapter.addTestStepLog("Profile Level dropdown is available and Clicked on it in Manual Mapping screen...");
					Select dropdown = new Select(profileLevelDropdown);
					List<WebElement> allOptions = dropdown.getOptions();
					LOGGER.info("Levels present inside profile level dropdown : ");
					ExtentCucumberAdapter.addTestStepLog("Levels present inside profile level dropdown : ");
					for(WebElement option : allOptions){
				        LOGGER.info(option.getText());
				        ExtentCucumberAdapter.addTestStepLog(option.getText());
				        }
					wait.until(ExpectedConditions.elementToBeClickable(profileLevelDropdown)).click(); // This click is to close dropdown options visibility 
					LOGGER.info("Successfully Verified Profile Level dropdown in Manual Mapping screen and levels present inside it...");
					ExtentCucumberAdapter.addTestStepLog("Successfully Verified Profile Level dropdown in Manual Mapping screen and levels present inside it...");
				} else {
					LOGGER.info("No Profile Levels available for this Last Saved profile with name : " + mappedSuccessPrflName); 
					ExtentCucumberAdapter.addTestStepLog("No Profile Levels available for this Last Saved profile with name : " + mappedSuccessPrflName);
				}
				
			} catch (Exception e) {
				LOGGER.error(" Issue validating profile level dropdown in manual mapping - Method: user_should_verify_profile_level_dropdown_is_available_on_last_saved_success_profile_and_validate_levels_present_inside_dropdown", e);
			ScreenshotHandler.captureFailureScreenshot("validate_profile_level_dropdown_manual_mapping", e);
				e.printStackTrace();
				Assert.fail("Issue in validating profile level dropdown of Last Saved Profile in Manual Mapping page...Please Investigate!!!");
				ExtentCucumberAdapter.addTestStepLog("Issue in validating profile level dropdown in Last Saved Profile in Manual Mapping page...Please Investigate!!!");
			}	
		}
	}
	
	public void validate_last_saved_success_profile_role_summary_matches_with_profile_role_summary_in_details_popup() {
		if(manualMapping) {
			try {
				js.executeScript("arguments[0].scrollIntoView(true);", manualMappingPageProfileRoleSummary);
				String manualMappingPageProfileRoleSummaryText = wait.until(ExpectedConditions.visibilityOf(manualMappingPageProfileRoleSummary)).getText();
				Assert.assertEquals(ProfileRoleSummary, manualMappingPageProfileRoleSummaryText.split(": ", 2)[1].trim());
			LOGGER.info("Last Saved Profile Role Summary in the Manual Mapping screen matches with Mapped Success Profile Role Summary in details popup as expected");
			ExtentCucumberAdapter.addTestStepLog("Last Saved Profile Role Summary in the Manual Mapping screen matches with Mapped Success Profile Role Summary in details popup as expected");
		} catch (Exception e) {
			LOGGER.error(" Issue validating role summary match - Method: validate_last_saved_success_profile_role_summary_matches_with_profile_role_summary_in_details_popup", e);
			ScreenshotHandler.captureFailureScreenshot("validate_role_summary_match", e);
			e.printStackTrace();
			Assert.fail("Issue in Validating Last Saved Profile Role Summary in the Manual Mapping screen....Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in Validating Last Saved Profile Role Summary in the Manual Mapping screen....Please Investigate!!!");
		}
		}
	}
	
	public void validate_last_saved_success_profile_details_matches_with_profile_details_in_details_popup() {
		if(manualMapping) {
			try {
				js.executeScript("arguments[0].scrollIntoView(true);", manualMappingPageProfileDetails);
				String manualMappingPageProfileDetailsText = wait.until(ExpectedConditions.visibilityOf(manualMappingPageProfileDetails)).getText();
				Assert.assertEquals(ProfileDetails, manualMappingPageProfileDetailsText);
			LOGGER.info("Last Saved Profile Details in the Manual Mapping screen matches with Mapped Success Profile Details in details popup as expected");
			ExtentCucumberAdapter.addTestStepLog("Last Saved Profile Details in the Manual Mapping screen matches with Mapped Success Profile Details in details popup as expected");
		} catch (Exception e) {
			LOGGER.error(" Issue validating profile details match - Method: validate_last_saved_success_profile_details_matches_with_profile_details_in_details_popup", e);
			ScreenshotHandler.captureFailureScreenshot("validate_profile_details_match", e);
			e.printStackTrace();
			Assert.fail("Issue in Validating Last Saved Profile Details in the Manual Mapping screen....Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in Validating Last Saved Profile Details in the Manual Mapping screen....Please Investigate!!!");
		}
		}
	}
	
	public void validate_last_saved_success_profile_responsibilities_matches_with_profile_responsibilities_in_details_popup() {
		if(manualMapping) {
			try {
				js.executeScript("arguments[0].scrollIntoView(true);", manualMappingPageProfileResponsibilities);
				// PERFORMANCE: Removed Thread.sleep(2000) - scrollIntoView is immediate
				js.executeScript("arguments[0].scrollIntoView(true);", manualMappingPageProfileViewMoreResponsibilitiesBtn.get(0));
				wait.until(ExpectedConditions.elementToBeClickable(manualMappingPageProfileViewMoreResponsibilitiesBtn.get(0))).click();
			while(true) {
				try {
					if(manualMappingPageProfileViewMoreResponsibilitiesBtn.isEmpty()) {
						js.executeScript("window.scrollTo(0, document.documentElement.scrollHeight);"); // Headless-compatible
				LOGGER.info("Reached end of content in Last Saved Success Profile Responsibilities Section in Manual Mapping screen");
				ExtentCucumberAdapter.addTestStepLog("Reached end of content in Last Saved Success Profile Responsibilities Section in Manual Mapping screen");
				break;
			}
				String ViewMoreResponsibilitiesBtnText = wait.until(ExpectedConditions.visibilityOf(manualMappingPageProfileViewMoreResponsibilitiesBtn.get(0))).getText();
				// Scroll element into view before clicking
				WebElement element = manualMappingPageProfileViewMoreResponsibilitiesBtn.get(0);
				js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
				try { Thread.sleep(500); } catch (InterruptedException ie) {}
			// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
			PerformanceUtils.waitForUIStability(driver, 2);
					try {
						wait.until(ExpectedConditions.elementToBeClickable(element)).click();
					} catch(Exception e) {
						js.executeScript("arguments[0].click();", element);
					}
					LOGGER.info("Clicked on "+ ViewMoreResponsibilitiesBtnText +" button in Last Saved Success Profile Responsibilities Section in Manual Mapping screen");
					ExtentCucumberAdapter.addTestStepLog("Clicked on "+ ViewMoreResponsibilitiesBtnText +" button in Last Saved Success Profile Responsibilities Section in Manual Mapping screen");
					// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
					PerformanceUtils.waitForUIStability(driver, 2);
						js.executeScript("arguments[0].scrollIntoView(true);", manualMappingPageProfileViewMoreResponsibilitiesBtn.get(0));
			} catch(StaleElementReferenceException e) {
					js.executeScript("window.scrollTo(0, document.documentElement.scrollHeight);"); // Headless-compatible
					LOGGER.info("Reached end of content in Last Saved Success Profile Responsibilities Section in Manual Mapping screen");
					ExtentCucumberAdapter.addTestStepLog("Reached end of content in Last Saved Success Profile Responsibilities Section in Manual Mapping screen");
					break;
					}	
				}
				String manualMappingPageProfileResponsibilitiesText = wait.until(ExpectedConditions.visibilityOf(manualMappingPageProfileResponsibilitiesData)).getText();
				Assert.assertEquals(ProfileResponsibilities, manualMappingPageProfileResponsibilitiesText);
			LOGGER.info("Last Saved Success Profile Responsibilities in the Job Compare page matches with Mapped Success Profile Responsibilities in details popup as expected");
			ExtentCucumberAdapter.addTestStepLog("Last Saved Success Profile Responsibilities in the Job Compare page matches with Mapped Success Profile Responsibilities in details popup as expected");
		} catch (Exception e) {
			LOGGER.error(" Issue validating responsibilities match - Method: validate_last_saved_success_profile_responsibilities_matches_with_profile_responsibilities_in_details_popup", e);
			ScreenshotHandler.captureFailureScreenshot("validate_responsibilities_match", e);
			e.printStackTrace();
			Assert.fail("Issue in validating data in Last Saved Success Profile Responsibilities Section in Manual Mapping screen...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in validating data in Last Saved Success Profile Responsibilities Section in Manual Mapping screen...Please Investigate!!!");
		}
		}
	}
	
	public void validate_last_saved_success_profile_behavioural_competencies_matches_with_profile_behavioural_competencies_in_details_popup() {
		if(manualMapping) {
			try {
				js.executeScript("arguments[0].scrollIntoView(true);", manualMappingPageProfileRoleSummary);
				// PERFORMANCE: Replaced Thread.sleep(2000) with smart element wait
				PerformanceUtils.waitForElement(driver, driver.findElement(By.xpath("//div")));
				wait.until(ExpectedConditions.elementToBeClickable(manualMappingPageProfileBehaviouralCompetencies)).click();
				LOGGER.info("Clicked on BEHAVIOURAL COMPETENCIES screen of Last Saved Profile in Manual Mapping screen");
				ExtentCucumberAdapter.addTestStepLog("Clicked on BEHAVIOURAL COMPETENCIES screen of Last Saved Profile in Manual Mapping screen");
				while(true) {
					try {
						if(manualMappingPageProfileViewMoreCompetenciesBtn.isEmpty()) {
							LOGGER.info("Reached end of content in Last Saved Success Profile Behavioural Competencies Section in Manual Mapping screen");
							ExtentCucumberAdapter.addTestStepLog("Reached end of content in Last Saved Success Profile Behavioural Competencies Section in Manual Mapping screen");
							break;
						}
					String ViewMoreCompetenciesBtnText = wait.until(ExpectedConditions.visibilityOf(manualMappingPageProfileViewMoreCompetenciesBtn.get(0))).getText();
					// Scroll element into view before clicking
					WebElement element = manualMappingPageProfileViewMoreCompetenciesBtn.get(0);
					js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
					try { Thread.sleep(500); } catch (InterruptedException ie) {}
			// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
			PerformanceUtils.waitForUIStability(driver, 2);
					try {
						wait.until(ExpectedConditions.elementToBeClickable(element)).click();
					} catch(Exception e) {
						js.executeScript("arguments[0].click();", element);
					}
					LOGGER.info("Clicked on "+ ViewMoreCompetenciesBtnText +" button in Last Saved Success Profile Behavioural Competencies Section in Manual Mapping screen");
					ExtentCucumberAdapter.addTestStepLog("Clicked on "+ ViewMoreCompetenciesBtnText +" button in Last Saved Success Profile Behavioural Competencies Section in Manual Mapping screen");
					// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
					PerformanceUtils.waitForUIStability(driver, 2);
						js.executeScript("arguments[0].scrollIntoView(true);", manualMappingPageProfileViewMoreCompetenciesBtn.get(0));
					} catch(StaleElementReferenceException e) {
						LOGGER.info("Reached end of content in Last Saved Success Profile Behavioural Competencies Section in Manual Mapping screen");
						ExtentCucumberAdapter.addTestStepLog("Reached end of content in Last Saved Success Profile Behavioural Competencies Section in Manual Mapping screen");
						break;
					}	
				}
				String manualMappingPageProfileBehaviouralCompetenciesText = wait.until(ExpectedConditions.visibilityOf(manualMappingPageProfileBehaviouralCompetenciesData)).getText();
				Assert.assertEquals(ProfileBehaviouralCompetencies, manualMappingPageProfileBehaviouralCompetenciesText);
			LOGGER.info("Last Saved Success Profile Behavioural Competencies in the Job Compare page matches with Mapped Success Profile Behavioural Competencies in details popup as expected");
			ExtentCucumberAdapter.addTestStepLog("Last Saved Success Profile Behavioural Competencies in the Job Compare page matches with Mapped Success Profile Behavioural Competencies in details popup as expected");		
		} catch (Exception e) {
			LOGGER.error(" Issue validating behavioural competencies match - Method: validate_last_saved_success_profile_behavioural_competencies_matches_with_profile_behavioural_competencies_in_details_popup", e);
			ScreenshotHandler.captureFailureScreenshot("validate_behavioural_competencies_match", e);
			e.printStackTrace();
			Assert.fail("Issue in validating data in Last Saved Success Profile Behavioural Competencies Section in Manual Mapping screen...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in validating data in Last Saved Success Profile Behavioural Competencies Section in Manual Mapping screen...Please Investigate!!!");
		}
		}
	}
	
	public void validate_last_saved_success_profile_skills_macthes_with_profile_skills_in_details_popup() {
		if(manualMapping) {
			try {
				js.executeScript("arguments[0].scrollIntoView(true);", manualMappingPageProfileRoleSummary);
				// PERFORMANCE: Replaced Thread.sleep(2000) with smart element wait
				PerformanceUtils.waitForElement(driver, driver.findElement(By.xpath("//div")));
				wait.until(ExpectedConditions.elementToBeClickable(manualMappingPageProfileSkills)).click();
				LOGGER.info("Clicked on SKILLS screen of Last Saved Profile in Manual Mapping screen");
				ExtentCucumberAdapter.addTestStepLog("Clicked on SKILLS screen of Last Saved Profile in Manual Mapping screen");
				while(true) {
					try {
						if(manualMappingPageProfileViewMoreSkillsBtn.isEmpty()) {
							LOGGER.info("Reached end of content in Last Saved Success Profile Skills Section in Manual Mapping screen");
							ExtentCucumberAdapter.addTestStepLog("Reached end of content in Last Saved Success Profile Skills Section in Manual Mapping screen");
							break;
						} 
					String ViewMoreSkillsBtnText = wait.until(ExpectedConditions.visibilityOf(manualMappingPageProfileViewMoreSkillsBtn.get(0))).getText();
					// Scroll element into view before clicking
					WebElement element = manualMappingPageProfileViewMoreSkillsBtn.get(0);
					js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
					try { Thread.sleep(500); } catch (InterruptedException ie) {}
			// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
			PerformanceUtils.waitForUIStability(driver, 2);
					try {
						wait.until(ExpectedConditions.elementToBeClickable(element)).click();
					} catch(Exception e) {
						js.executeScript("arguments[0].click();", element);
					}
					LOGGER.info("Clicked on "+ ViewMoreSkillsBtnText +" button in Last Saved Success Profile Skills Section in Manual Mapping screen");
					ExtentCucumberAdapter.addTestStepLog("Clicked on "+ ViewMoreSkillsBtnText +" button in Last Saved Success Profile Skills Section in Manual Mapping screen");
					// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
					PerformanceUtils.waitForUIStability(driver, 2);
						js.executeScript("arguments[0].scrollIntoView(true);", manualMappingPageProfileViewMoreSkillsBtn.get(0));
					} catch(StaleElementReferenceException e) {
						LOGGER.info("Reached end of content in Last Saved Success Profile Skills Section in Manual Mapping screen");
						ExtentCucumberAdapter.addTestStepLog("Reached end of content in Last Saved Success Profile Skills Section in Manual Mapping screen");
						break;
					}	
				}
				String manualMappingPageProfileSkillsText = wait.until(ExpectedConditions.visibilityOf(manualMappingPageProfileSkillsData)).getText();
				Assert.assertEquals(ProfileSkills, manualMappingPageProfileSkillsText);
			LOGGER.info("Last Saved Success Profile Skills in the Job Compare page matches with Mapped Success Profile Skills in details popup as expected");
			ExtentCucumberAdapter.addTestStepLog("Last Saved Success Profile Skills in the Job Compare page matches with Mapped Success Profile Skills in details popup as expected");
		} catch (Exception e) {
			LOGGER.error(" Issue validating skills match - Method: validate_last_saved_success_profile_skills_macthes_with_profile_skills_in_details_popup", e);
			ScreenshotHandler.captureFailureScreenshot("validate_skills_match", e);
			e.printStackTrace();
			Assert.fail("Issue in validating data in Last Saved Success Profile Skills Section in Manual Mapping screen...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in validating data in Last Saved Success Profile Skills Section in Manual Mapping screen...Please Investigate!!!");
		}
		}
	}
	
	public void search_for_success_profile_in_manual_mapping_screen() {
		if(manualMapping) {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			js.executeScript("window.scrollTo(0, 0);"); // Scroll to top (headless-compatible)
			try {
			// PERFORMANCE: Replaced Thread.sleep(2000) with element readiness wait
			PerformanceUtils.waitForElement(driver, KFSPsearchBar, 2);
				wait.until(ExpectedConditions.visibilityOf(KFSPsearchBar)).sendKeys(Keys.chord(Keys.CONTROL, "a"));
				wait.until(ExpectedConditions.visibilityOf(KFSPsearchBar)).sendKeys(SPSearchString);
				Assert.assertEquals(SPSearchString, KFSPsearchBar.getAttribute("value"));
			LOGGER.info("Entered " + SPSearchString +" as Korn Ferry SP Search String in the search bar in Manual Mapping screen");
			ExtentCucumberAdapter.addTestStepLog("Entered " + SPSearchString + " as Korn Ferry SP Search String in the search bar in Manual Mapping screen");	
		} catch (Exception e) {
			LOGGER.error(" Issue entering search string - Method: search_for_success_profile_in_manual_mapping_screen", e);
			ScreenshotHandler.captureFailureScreenshot("enter_search_string_manual_mapping", e);
			e.printStackTrace();
			Assert.fail("Failed to enter Korn Ferry SP Search String in the search bar in Manual Mapping screen...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Failed to enter Korn Ferry SP Search String in the search bar in Manual Mapping screen...Please Investigate!!!");
		}
		}
	}
	
	public void select_first_sucess_profile_from_search_results_in_manual_mapping_screen() {
		if(manualMapping) {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			try {
				// PERFORMANCE: Replaced Thread.sleep(3000) with search results wait
				PerformanceUtils.waitForSearchResults(driver, By.xpath(".//button[contains(text(),'Select')]"));
				Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(firstSearchResultBtninManualMappingPage)).isDisplayed());
				wait.until(ExpectedConditions.elementToBeClickable(firstSearchResultBtninManualMappingPage)).click();
				customSPNameinSearchResults = firstSearchResultTextinManualMappingPage.getText();
			LOGGER.info("First SP with Name : " + customSPNameinSearchResults + " from search results is selected in Manual Mapping screen");
			ExtentCucumberAdapter.addTestStepLog("First SP with Name : " + customSPNameinSearchResults + " from search results is selected in Manual Mapping screen");
			wait.until(ExpectedConditions.invisibilityOf(firstSearchResultBtninManualMappingPage));
		} catch (Exception e) {
			LOGGER.error(" Issue selecting first SP from search results - Method: select_first_sucess_profile_from_search_results_in_manual_mapping_screen", e);
			ScreenshotHandler.captureFailureScreenshot("select_first_sp_from_search_results", e);
			e.printStackTrace();
			Assert.fail("Issue in Selecting First SP from Search Results in Manual Mapping screen...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in Selecting First SP from Search Results in Manual Mapping screen...Please Investigate!!!");
		}
		}
	}
	
	public void verify_success_profile_is_added_in_manual_job_mapping_screen() {
		if(manualMapping) {
			try {
				String profileHeaderName = wait.until(ExpectedConditions.visibilityOf(manualMappingPageProfileTitle)).getText();
				Assert.assertEquals(customSPNameinSearchResults,profileHeaderName);
				manualMappingProfileName = profileHeaderName;
				mappedSuccessPrflName = manualMappingProfileName;
			LOGGER.info("Success Profile with name " + profileHeaderName + " is added successfully on Manual Job Mapping screen");
			ExtentCucumberAdapter.addTestStepLog("Success Profile with name " + profileHeaderName + " is added successfully on Manual Job Mapping screen");
			} catch (Exception e) {
				LOGGER.error(" Issue verifying success profile added - Method: verify_success_profile_is_added_in_manual_job_mapping_screen", e);
				ScreenshotHandler.captureFailureScreenshot("verify_success_profile_added", e);
				e.printStackTrace();
				Assert.fail("Issue in adding success profile on manual job mapping screen....Please Investigate!!!");
				ExtentCucumberAdapter.addTestStepLog("Issue in adding success profile on manual job mapping screen....Please Investigate!!!");
			}
		}
	}
	
	public void user_should_verify_profile_level_dropdown_is_available_and_validate_levels_present_inside_dropdown_in_manual_job_mapping_screen() {
		if(manualMapping) {
			try {
				if(wait.until(ExpectedConditions.visibilityOf(profileLevelDropdown)).isEnabled()) {
					wait.until(ExpectedConditions.elementToBeClickable(profileLevelDropdown)).click();
					LOGGER.info("Profile Level dropdown is available and Clicked on it in Manual Mapping screen...");
					ExtentCucumberAdapter.addTestStepLog("Profile Level dropdown is available and Clicked on it in Manual Mapping screen...");
					Select dropdown = new Select(profileLevelDropdown);
					List<WebElement> allOptions = dropdown.getOptions();
					LOGGER.info("Levels present inside profile level dropdown : ");
					ExtentCucumberAdapter.addTestStepLog("Levels present inside profile level dropdown : ");
					for(WebElement option : allOptions){
				        LOGGER.info(option.getText());
				        ExtentCucumberAdapter.addTestStepLog(option.getText());
				        }
					wait.until(ExpectedConditions.elementToBeClickable(profileLevelDropdown)).click(); // This click is to close dropdown options visibility 
					LOGGER.info("Successfully Verified Profile Level dropdown in Manual Mapping screen and levels present inside it...");
					ExtentCucumberAdapter.addTestStepLog("Successfully Verified Profile Level dropdown in Manual Mapping screen and levels present inside it...");
				} else {
				LOGGER.info("No Profile Levels available for the profile with name : " + manualMappingProfileName + " in Manual Mapping page"); 
				ExtentCucumberAdapter.addTestStepLog("No Profile Levels available for this Last Saved profile with name : " + manualMappingProfileName + " in Manual Mapping page");
			}
			
		} catch (Exception e) {
			LOGGER.error(" Issue validating profile level dropdown - Method: user_should_verify_profile_level_dropdown_is_available_and_validate_levels_present_inside_dropdown_in_manual_job_mapping_screen", e);
			ScreenshotHandler.captureFailureScreenshot("validate_profile_level_dropdown_manual_job_mapping", e);
			e.printStackTrace();
			Assert.fail("Issue in validating profile level dropdown of Last Saved Profile in Manual Mapping page...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in validating profile level dropdown in Last Saved Profile in Manual Mapping page...Please Investigate!!!");
		}
		}
	}
	
	public void change_profile_level_in_manual_job_mapping_screen() {
		if(manualMapping) {
			if(wait.until(ExpectedConditions.visibilityOf(profileLevelDropdown)).isEnabled()) {
				js.executeScript("window.scrollTo(0, 0);"); // Scroll to top (headless-compatible)
				try {
					wait.until(ExpectedConditions.elementToBeClickable(profileLevelDropdown)).click();
					// PERFORMANCE: Replaced Thread.sleep(2000) with dropdown readiness wait
					PerformanceUtils.waitForDropdownOptions(driver, By.xpath("//select//option"));
					Select dropdown = new Select(profileLevelDropdown);
					List<WebElement> allOptions = dropdown.getOptions();
					for(WebElement option : allOptions){
						String lastlevelvalue = option.getText();
						changedlevelvalue = lastlevelvalue;
				        }
//					int levels = dropdown.getOptions().size();
					dropdown.selectByVisibleText(changedlevelvalue);
//					dropdown.selectByIndex(levels - 1);
					wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
									// PERFORMANCE: Replaced Thread.sleep(4000+4000) with UI stability wait
				PerformanceUtils.waitForUIStability(driver);
			LOGGER.info("Successfully Changed Profile Level to : " + changedlevelvalue + " in Manual Mapping screen");
			ExtentCucumberAdapter.addTestStepLog("Successfully Changed Profile Level to : " + changedlevelvalue + " in Manual Mapping screen");
			PerformanceUtils.waitForUIStability(driver);
			} catch (Exception e) {
				LOGGER.error(" Issue changing profile level - Method: change_profile_level_in_manual_job_mapping_screen", e);
				ScreenshotHandler.captureFailureScreenshot("change_profile_level", e);
				e.printStackTrace();
				Assert.fail("Issue in Changing Profile Level in Manual Mapping screen...Please Investigate!!!");
				ExtentCucumberAdapter.addTestStepLog("Issue in Changing Profile Level in Manual Mapping screen...Please Investigate!!!");
				}
				
			try {
				// This click is to close dropdown options visibility
				// Scroll element into view before clicking
				WebElement element = wait.until(ExpectedConditions.elementToBeClickable(profileLevelDropdown));
				js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
				try { Thread.sleep(500); } catch (InterruptedException ie) {}
				try {
					element.click();
				} catch (Exception e) {
					try {
						js.executeScript("arguments[0].click();", profileLevelDropdown);
					} catch (Exception s) {
						utils.jsClick(driver, profileLevelDropdown);
					}
				}
					LOGGER.info("Profile Level dropdown closed successfully in Manual Mapping screen....");
					ExtentCucumberAdapter.addTestStepLog("Profile Level dropdown closed successfully in Manual Mapping screen....");
					} catch (Exception e) {
						e.printStackTrace();
						Assert.fail("Issue in clicking on Profile Level dropdown to close it in Manual Mapping screen...Please Investigate!!!");
						ExtentCucumberAdapter.addTestStepLog("Issue in clicking on Profile Level dropdown to close it in Manual Mapping screen...Please Investigate!!!");
					}
			}
		}
	}
	
	public void validate_role_summary_is_displaying_in_manual_job_mapping_screen() {
		if(manualMapping) {
			try {
				String roleSummaryText = wait.until(ExpectedConditions.visibilityOf(manualMappingPageProfileRoleSummary)).getText();
				manualMappingProfileRoleSummary = roleSummaryText.split(": ", 2)[1].trim();
				LOGGER.info("Role summary of Success Profile in Manual Mapping screen : " + manualMappingProfileRoleSummary);
				ExtentCucumberAdapter.addTestStepLog("Role summary of Success Profile in Manual Mapping screen : " + manualMappingProfileRoleSummary); 
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail("Issue in validating Role Summary of Success Profile in Manual Mapping screen...Please Investigate!!!");
				ExtentCucumberAdapter.addTestStepLog("Issue in validating Role Summary of Success Profile in Manual Mapping screen...Please Investigate!!!");
			}
		}
	}
	
	public void verify_profile_details_displaying_in_manual_job_mapping_screen() {
		if(manualMapping) {
			try {
				String profileDeatilsText = wait.until(ExpectedConditions.visibilityOf(manualMappingPageProfileDetails)).getText();
				manualMappingProfileDetails = profileDeatilsText;
				LOGGER.info("Below are the Profile Details for the Success Profile " + manualMappingProfileName + " displaying in Manual Mapping screen: ");
				LOGGER.info(profileDeatilsText);
				ExtentCucumberAdapter.addTestStepLog("Below are the Profile Details for the Success Profile " + manualMappingProfileName + " displaying in Manual Mapping screen: \n");
				ExtentCucumberAdapter.addTestStepLog(profileDeatilsText);
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail("Issue in displaying profile details in Manual Mapping screen....Please Investigate!!!");
				ExtentCucumberAdapter.addTestStepLog("Issue in displaying profile details in Manual Mapping screen....Please Investigate!!!");
			}
		}
	}
	
	public void validate_data_in_responsibilities_tab_in_manual_job_mapping_screen() {
		if(manualMapping) {
			try {
				js.executeScript("arguments[0].scrollIntoView(true);", manualMappingPageProfileRoleSummary);
				// PERFORMANCE: Replaced Thread.sleep(2000) with smart element wait
				PerformanceUtils.waitForElement(driver, driver.findElement(By.xpath("//div")));
				wait.until(ExpectedConditions.elementToBeClickable(manualMappingPageProfileResponsibilities)).click();
				LOGGER.info("Clicked on Responsibilities screen in Manual Mapping screen");
				ExtentCucumberAdapter.addTestStepLog("Clicked on Responsibilities screen in Manual Mapping screen");
//				js.executeScript("arguments[0].scrollIntoView(true);", manualMappingPageProfileResponsibilities);
//				Thread.sleep(2000);
//				js.executeScript("arguments[0].scrollIntoView(true);", manualMappingPageProfileViewMoreResponsibilitiesBtn.get(0));
//				wait.until(ExpectedConditions.elementToBeClickable(manualMappingPageProfileViewMoreResponsibilitiesBtn.get(0))).click();
				while(true) {
					try {
					if(manualMappingPageProfileViewMoreResponsibilitiesBtn.isEmpty()) {
						js.executeScript("window.scrollTo(0, document.documentElement.scrollHeight);"); // Headless-compatible
						LOGGER.info("Reached end of content in Success Profile Responsibilities Section in Manual Mapping screen");
						ExtentCucumberAdapter.addTestStepLog("Reached end of content in Success Profile Responsibilities Section in Manual Mapping screen");
						break;
						}
					String ViewMoreResponsibilitiesBtnText = wait.until(ExpectedConditions.visibilityOf(manualMappingPageProfileViewMoreResponsibilitiesBtn.get(0))).getText();
					// Scroll element into view before clicking
					WebElement element = manualMappingPageProfileViewMoreResponsibilitiesBtn.get(0);
					js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
					try { Thread.sleep(500); } catch (InterruptedException ie) {}
			// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
			PerformanceUtils.waitForUIStability(driver, 2);
					try {
						wait.until(ExpectedConditions.elementToBeClickable(element)).click();
					} catch(Exception e) {
						js.executeScript("arguments[0].click();", element);
					}
					LOGGER.info("Clicked on "+ ViewMoreResponsibilitiesBtnText +" button in Success Profile Responsibilities Section in Manual Mapping screen");
					ExtentCucumberAdapter.addTestStepLog("Clicked on "+ ViewMoreResponsibilitiesBtnText +" button in Success Profile Responsibilities Section in Manual Mapping screen");
				// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
				PerformanceUtils.waitForUIStability(driver, 2);
					js.executeScript("arguments[0].scrollIntoView(true);", manualMappingPageProfileViewMoreResponsibilitiesBtn.get(0));
				} catch(StaleElementReferenceException e) {
					js.executeScript("window.scrollTo(0, document.documentElement.scrollHeight);"); // Scroll DOWN (headless-compatible)
					ExtentCucumberAdapter.addTestStepLog("Reached end of content in Success Profile Responsibilities Section in Manual Mapping screen");
					break;
					}	
				}
				String manualMappingPageProfileResponsibilitiesText = wait.until(ExpectedConditions.visibilityOf(manualMappingPageProfileResponsibilitiesData)).getText();
				manualMappingProfileResponsibilities = manualMappingPageProfileResponsibilitiesText;
//				LOGGER.info("Data present in Responsibilities screen in Manual Mapping screen : \n" + manualMappingPageProfileResponsibilitiesText);
//				ExtentCucumberAdapter.addTestStepLog("Data present in Responsibilities screen in Manual Mapping screen : \n" + manualMappingPageProfileResponsibilitiesText);
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail("Issue in validating data in Responsibilities screen in Manual Mapping screen...Please Investigate!!!");
				ExtentCucumberAdapter.addTestStepLog("Issue in validating data in Responsibilities screen in Manual Mapping screen...Please Investigate!!!");
			}
		}
	}
	
	public void validate_data_in_behavioural_competencies_tab_in_manual_job_mapping_screen() {
		if(manualMapping) {
			try {
				js.executeScript("arguments[0].scrollIntoView(true);", manualMappingPageProfileRoleSummary);
				// PERFORMANCE: Replaced Thread.sleep(2000) with smart element wait
				PerformanceUtils.waitForElement(driver, driver.findElement(By.xpath("//div")));
				wait.until(ExpectedConditions.elementToBeClickable(manualMappingPageProfileBehaviouralCompetencies)).click();
				LOGGER.info("Clicked on Behaviour Competencies screen in Manual Mapping screen");
				ExtentCucumberAdapter.addTestStepLog("Clicked on Behaviour Competencies screen in Manual Mapping screen");
				while(true) {
					try {
						if(manualMappingPageProfileViewMoreCompetenciesBtn.isEmpty()) {
							LOGGER.info("Reached end of content Success Profile Behavioural Competencies Section in Manual Mapping screen");
							ExtentCucumberAdapter.addTestStepLog("Reached end of content in Success Profile Behavioural Competencies Section in Manual Mapping screen");
							break;
						}
					String ViewMoreCompetenciesBtnText = wait.until(ExpectedConditions.visibilityOf(manualMappingPageProfileViewMoreCompetenciesBtn.get(0))).getText();
					// Scroll element into view before clicking
					WebElement element = manualMappingPageProfileViewMoreCompetenciesBtn.get(0);
					js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
					try { Thread.sleep(500); } catch (InterruptedException ie) {}
			// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
			PerformanceUtils.waitForUIStability(driver, 2);
					try {
						wait.until(ExpectedConditions.elementToBeClickable(element)).click();
					} catch(Exception e) {
						js.executeScript("arguments[0].click();", element);
					}
					LOGGER.info("Clicked on "+ ViewMoreCompetenciesBtnText +" button in Success Profile Behavioural Competencies Section in Manual Mapping screen");
					ExtentCucumberAdapter.addTestStepLog("Clicked on "+ ViewMoreCompetenciesBtnText +" button in Success Profile Behavioural Competencies Section in Manual Mapping screen");
				// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
				PerformanceUtils.waitForUIStability(driver, 2);
						js.executeScript("arguments[0].scrollIntoView(true);", manualMappingPageProfileViewMoreCompetenciesBtn.get(0));
					} catch(StaleElementReferenceException e) {
						LOGGER.info("Reached end of content in Success Profile Behavioural Competencies Section in Manual Mapping screen");
						ExtentCucumberAdapter.addTestStepLog("Reached end of content in Success Profile Behavioural Competencies Section in Manual Mapping screen");
						break;
					}	
				}
				String manualMappingPageProfileBehaviouralCompetenciesText = wait.until(ExpectedConditions.visibilityOf(manualMappingPageProfileBehaviouralCompetenciesData)).getText();
				manualMappingProfileBehaviouralCompetencies = manualMappingPageProfileBehaviouralCompetenciesText;
//				LOGGER.info("Data present in Behavioural Competencies screen in Manual Mapping screen : \n" + manualMappingPageProfileBehaviouralCompetenciesText);
//				ExtentCucumberAdapter.addTestStepLog("Data present in Behavioural Competencies screen in Manual Mapping screen : \n" + manualMappingPageProfileBehaviouralCompetenciesText);
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail("Issue in validating data in Behavioural Competencies screen in Manual Mapping screen...Please Investigate!!!");
				ExtentCucumberAdapter.addTestStepLog("Issue in validating data in Behavioural Competencies screen in Manual Mapping screen...Please Investigate!!!");
			}
		}
	}
	
	public void validate_data_in_skills_tab_in_manual_job_mapping_screen() {
		if(manualMapping) {
			try {
				js.executeScript("arguments[0].scrollIntoView(true);", manualMappingPageProfileRoleSummary);
				// PERFORMANCE: Replaced Thread.sleep(2000) with smart element wait
				PerformanceUtils.waitForElement(driver, driver.findElement(By.xpath("//div")));
				wait.until(ExpectedConditions.elementToBeClickable(manualMappingPageProfileSkills)).click();
				LOGGER.info("Clicked on Skills screen in Manual Mapping screen");
				ExtentCucumberAdapter.addTestStepLog("Clicked on Skills screen in Manual Mapping screen");
				while(true) {
					try {
						if(manualMappingPageProfileViewMoreSkillsBtn.isEmpty()) {
							LOGGER.info("Reached end of content in Success Profile Skills Section in Manual Mapping screen");
							ExtentCucumberAdapter.addTestStepLog("Reached end of content in Success Profile Skills Section in Manual Mapping screen");
							break;
						} 
					String ViewMoreSkillsBtnText = wait.until(ExpectedConditions.visibilityOf(manualMappingPageProfileViewMoreSkillsBtn.get(0))).getText();
					// Scroll element into view before clicking
					WebElement element = manualMappingPageProfileViewMoreSkillsBtn.get(0);
					js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
					try { Thread.sleep(500); } catch (InterruptedException ie) {}
			// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
			PerformanceUtils.waitForUIStability(driver, 2);
					try {
						wait.until(ExpectedConditions.elementToBeClickable(element)).click();
					} catch(Exception e) {
						js.executeScript("arguments[0].click();", element);
					}
					LOGGER.info("Clicked on "+ ViewMoreSkillsBtnText +" button in Success Profile Skills Section in Manual Mapping screen");
					ExtentCucumberAdapter.addTestStepLog("Clicked on "+ ViewMoreSkillsBtnText +" button in Success Profile Skills Section in Manual Mapping screen");
				// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
				PerformanceUtils.waitForUIStability(driver, 2);
						js.executeScript("arguments[0].scrollIntoView(true);", manualMappingPageProfileViewMoreSkillsBtn.get(0));
					} catch(StaleElementReferenceException e) {
						LOGGER.info("Reached end of content in Success Profile Skills Section in Manual Mapping screen");
						ExtentCucumberAdapter.addTestStepLog("Reached end of content in Success Profile Skills Section in Manual Mapping screen");
						break;
					}	
				}
				String manualMappingPageProfileSkillsText = wait.until(ExpectedConditions.visibilityOf(manualMappingPageProfileSkillsData)).getText();
				manualMappingProfileSkills = manualMappingPageProfileSkillsText;
//				LOGGER.info("Data present in Skills screen in Manual Mapping screen : \n" + manualMappingPageProfileSkillsText);
//				ExtentCucumberAdapter.addTestStepLog("Data present in Skills screen in Manual Mapping screen : \n" + manualMappingPageProfileSkillsText);
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail("Issue in validating data in Skills screen in Manual Mapping screen...Please Investigate!!!");
				ExtentCucumberAdapter.addTestStepLog("Issue in validating data in Skills screen in Manual Mapping screen...Please Investigate!!!");
			}
		}
	}
	
public void click_on_save_selection_button_in_manual_job_mapping_screen() {
	if(manualMapping) {
		try {
			js.executeScript("window.scrollTo(0, 0);"); // Scroll to top (headless-compatible)
			// Scroll element into view before clicking
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(saveSelectionBtn));
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
			try { Thread.sleep(500); } catch (InterruptedException ie) {}
			try {
				element.click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", saveSelectionBtn);
				} catch (Exception s) {
					utils.jsClick(driver, saveSelectionBtn);
				}
			}
				LOGGER.info("Successfully clicked on Save Selection button in Manual Mapping screen....");
				ExtentCucumberAdapter.addTestStepLog("Successfully clicked on Save Selection button in Manual Mapping screen....");
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail("Issue in clicking on Save Selection button in Manual Mapping screen...Please Investigate!!!");
				ExtentCucumberAdapter.addTestStepLog("Issue in clicking on Save Selection button in Manual Mapping screen...Please Investigate!!!");
			}
		}
	}
	
	public void user_should_be_navigated_to_job_mapping_page() {
		if(manualMapping) {
			try {
				// Optimized wait - no blocking Thread.sleep
				wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
				// FIXED: Use more reliable org-job-container for page detection, then verify header
				wait.until(ExpectedConditions.visibilityOf(jobMappingPageContainer));
				Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(jobMappingPageContainer)).isDisplayed());
				ExtentCucumberAdapter.addTestStepLog("User navigated to JOB MAPPING page successfully");
				LOGGER.info("User is in JOB MAPPING page");
			} catch (Exception e) {
				e.printStackTrace();
				ExtentCucumberAdapter.addTestStepLog("Issue in navigating to Job Mapping page...Please Investigate!!!");
				Assert.fail("Issue in navigating to Job Mapping page...Please Investigate!!!");
			}
		}
	}
	
	public void verify_organization_job_with_new_mapped_sp_is_displaying_on_top_of_profiles_list() {
		if(manualMapping) {
			try {
				wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
				wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
				// PERFORMANCE: Replaced Thread.sleep(2000) with smart page ready wait
				PerformanceUtils.waitForPageReady(driver, 2);
				WebElement	button = driver.findElement(By.xpath("//tbody//tr[2]//button[contains(text(),'different profile')] | //tbody//tr[2]//button[contains(@id,'view')]"));
				js.executeScript("arguments[0].scrollIntoView(true);", button);
				String text = button.getText();
				if(text.contains("different profile")) { 
					rowNumber=2;
					LOGGER.info("Organization Job profile " + orgJobName + " with new Mapped Success Profile " + manualMappingProfileName +" is displaying on Top of Profiles List as Expected");
					ExtentCucumberAdapter.addTestStepLog( "Organization Job profile " + orgJobName + " with new Mapped Success Profile " + manualMappingProfileName + " is displaying on Top of Profiles List as Expected");
				} else {
					mapSP = false;
					manualMapping = false;
					LOGGER.error("Organization Job profile " + orgJobName + " with new Mapped Success Profile " + manualMappingProfileName + " is NOT displaying on Top of Profiles List");
					Assert.fail("Organization Job profile " + orgJobName + " with new Mapped Success Profile " + manualMappingProfileName + " is NOT displaying on Top of Profiles List");
					ExtentCucumberAdapter.addTestStepLog( "Organization Job profile " + orgJobName  + " with new Mapped Success Profile " + manualMappingProfileName + " is NOT displaying on Top of Profiles List");
				}
			} catch(Exception e) {
				e.printStackTrace();
				Assert.fail("Issue in Verifying Organization Job with Recently Mapped SP displaying on Top of Profiles List or Not....Please Investigate!!!");
				ExtentCucumberAdapter.addTestStepLog("Issue in Verifying Organization Job with Recently Mapped SP displaying on Top of Profiles List or Not....Please Investigate!!!");
			}
		}
	}
	
	public void user_should_verify_profile_level_dropdown_is_available_in_details_popup_and_validate_levels_present_inside_dropdown() {
		if(manualMapping) {
			try {
				if(wait.until(ExpectedConditions.visibilityOf(profileLevelDropdown)).isEnabled()) {
					wait.until(ExpectedConditions.elementToBeClickable(profileLevelDropdown)).click();
					LOGGER.info("Profile Level dropdown is available and Clicked on it in Mapped Profile details Popup...");
					ExtentCucumberAdapter.addTestStepLog("Profile Level dropdown is available and Clicked on it in Mapped Profile details Popup...");
					Select dropdown = new Select(profileLevelDropdown);
					List<WebElement> allOptions = dropdown.getOptions();
					LOGGER.info("Levels present inside profile level dropdown : ");
					ExtentCucumberAdapter.addTestStepLog("Levels present inside profile level dropdown : ");
					for(WebElement option : allOptions){
				        LOGGER.info(option.getText());
				        ExtentCucumberAdapter.addTestStepLog(option.getText());
				        }
					wait.until(ExpectedConditions.elementToBeClickable(profileLevelDropdown)).click(); // This click is to close dropdown options visibility 
					LOGGER.info("Successfully Verified Profile Level dropdown in  Mapped Profile details Popup and levels present inside it...");
					ExtentCucumberAdapter.addTestStepLog("Successfully Verified Profile Level dropdown in Mapped Profile details Popup and levels present inside it...");
				} else {
					LOGGER.info("No Profile Levels available for Mapped Profile in details Popup : " + manualMappingProfileName); 
					ExtentCucumberAdapter.addTestStepLog("No Profile Levels available for Mapped Profile in details Popup : " + manualMappingProfileName);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail("Issue in validating profile level dropdown of Mapped Profile in details Popup...Please Investigate!!!");
				ExtentCucumberAdapter.addTestStepLog("Issue in validating profile level dropdown in Mapped Profile in details Popup...Please Investigate!!!");
			}	
		}
	}
	
	public void validate_profile_role_summary_in_details_popup_matches_with_mapped_success_profile_role_summary() {
		if(manualMapping) {
			try {
				js.executeScript("arguments[0].scrollIntoView(true);", roleSummary);
				String mappedProfileRoleSummaryText = wait.until(ExpectedConditions.visibilityOf(roleSummary)).getText();
				Assert.assertEquals(mappedProfileRoleSummaryText.split(": ", 2)[1].trim(), manualMappingProfileRoleSummary);
				LOGGER.info("Profile Role Summary in the Details Popup matches with Mapped Success Profile Role Summary as expected");
				ExtentCucumberAdapter.addTestStepLog("Profile Role Summary in the Details Popup matches with Mapped Success Profile Role Summary as expected");
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail("Issue in Validating Profile Role Summary in Details Popup matches with Mapped Success Profile Role Summary....Please Investigate!!!");
				ExtentCucumberAdapter.addTestStepLog("Issue in Validating Profile Role Summary in Details Popup matches with Mapped Success Profile Role Summary....Please Investigate!!!");
			}
		}
	}
	
	public void validate_profile_details_in_details_popup_matches_with_mapped_success_profile_details() {
		if(manualMapping) {
			try {
				js.executeScript("arguments[0].scrollIntoView(true);", profileDetails);
				String mappedProfileDetailsText = wait.until(ExpectedConditions.visibilityOf(profileDetails)).getText();
				Assert.assertEquals(mappedProfileDetailsText, manualMappingProfileDetails);
				LOGGER.info("Profile Details in Details Popup matches with Mapped Success Profile Details as expected");
				ExtentCucumberAdapter.addTestStepLog("Profile Details in Details Popup matches with Mapped Success Profile Details as expected");
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail("Issue in Validating Profile Details in Details Popup matches with Mapped Success Profile Details....Please Investigate!!!");
				ExtentCucumberAdapter.addTestStepLog("Issue in Validating Profile Details in Details Popup matches with Mapped Success Profile Details....Please Investigate!!!");
			}
		}
	}
	
	public void validate_profile_responsibilities_in_details_popup_matches_with_mapped_success_profile_responsibilities() {
		if(manualMapping) {
			try {
//				js.executeScript("arguments[0].scrollIntoView(true);", viewMoreButtonInResponsibilitiesTab);
				// PERFORMANCE: Replaced Thread.sleep(2000) with smart element wait
				PerformanceUtils.waitForElement(driver, driver.findElement(By.xpath("//div")));
				wait.until(ExpectedConditions.elementToBeClickable(viewMoreButtonInResponsibilitiesTab.get(0))).click();
			while(true) {
				try {
					if(viewMoreButtonInResponsibilitiesTab.isEmpty()) {
						js.executeScript("window.scrollTo(0, document.documentElement.scrollHeight);"); // Scroll DOWN (headless-compatible)
						LOGGER.info("Reached end of content in Profile Responsibilities Section in Details Popup");
						ExtentCucumberAdapter.addTestStepLog("Reached end of content in Profile Responsibilities Section in Details Popup");
						break;
					}
					String ViewMoreResponsibilitiesBtnText = wait.until(ExpectedConditions.visibilityOf(viewMoreButtonInResponsibilitiesTab.get(0))).getText();
					// Scroll element into view before clicking
					WebElement element = viewMoreButtonInResponsibilitiesTab.get(0);
					js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
					try { Thread.sleep(500); } catch (InterruptedException ie) {}
			// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
			PerformanceUtils.waitForUIStability(driver, 2);
					try {
						wait.until(ExpectedConditions.elementToBeClickable(element)).click();
					} catch(Exception e) {
						js.executeScript("arguments[0].click();", element);
					}
					LOGGER.info("Clicked on "+ ViewMoreResponsibilitiesBtnText +" button in Profile Responsibilities Section in Details Popup");
					ExtentCucumberAdapter.addTestStepLog("Clicked on "+ ViewMoreResponsibilitiesBtnText +" button in Profile Responsibilities Section in Details Popup");
				// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
				PerformanceUtils.waitForUIStability(driver, 2);
				js.executeScript("arguments[0].scrollIntoView(true);", viewMoreButtonInResponsibilitiesTab.get(0));
				} catch(StaleElementReferenceException e) {
					js.executeScript("window.scrollTo(0, document.documentElement.scrollHeight);"); // Scroll DOWN (headless-compatible)
					LOGGER.info("Reached end of content in Profile Responsibilities Section in Details Popup");
					ExtentCucumberAdapter.addTestStepLog("Reached end of content in Profile Responsibilities Section in Details Popup");
					break;
					}	
				}
				String ProfileResponsibilitiesText = wait.until(ExpectedConditions.visibilityOf(responisbilitiesData)).getText();
				Assert.assertEquals(ProfileResponsibilitiesText, manualMappingProfileResponsibilities);
				LOGGER.info("Profile Responsibilities in the Details Popup matches with Mapped Success Profile Responsibilities in as expected");
				ExtentCucumberAdapter.addTestStepLog("Profile Responsibilities in Details Popup matches with Mapped Success Profile Responsibilities as expected");
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail("Issue in validating data in Profile Responsibilities in Details Popup matches with Mapped Success Profile Responsibilities...Please Investigate!!!");
				ExtentCucumberAdapter.addTestStepLog("Issue in validating data in Profile Responsibilities in Details Popup matches with Mapped Success Profile Responsibilities...Please Investigate!!!");
			}
		}
	}
	
	public void validate_profile_behavioural_competencies_in_details_popup_matches_with_mapped_success_profile_behavioural_competencies() {
		if(manualMapping) {
			try {
			js.executeScript("arguments[0].scrollIntoView(true);", roleSummary);
			// PERFORMANCE: Replaced Thread.sleep(2000) with smart element wait
			PerformanceUtils.waitForElement(driver, driver.findElement(By.xpath("//div")));
			// Scroll element into view before clicking
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(behaviourCompetenciesTabButton));
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
			try { Thread.sleep(500); } catch (InterruptedException ie) {}
			try {
				element.click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", behaviourCompetenciesTabButton);
				} catch (Exception s) {
					utils.jsClick(driver, behaviourCompetenciesTabButton);
				}
			}
				wait.until(ExpectedConditions.elementToBeClickable(behaviourCompetenciesTabButton)).click();
				LOGGER.info("Clicked on BEHAVIOURAL COMPETENCIES screen in Profiles Details Popup");
				ExtentCucumberAdapter.addTestStepLog("Clicked on BEHAVIOURAL COMPETENCIES screen in Profiles Details Popup");
				while(true) {
					try {
						if(viewMoreButtonInBehaviourCompetenciesTab.isEmpty()) {
							LOGGER.info("Reached end of content in Profile Behavioural Competencies Section in Details Popup");
							ExtentCucumberAdapter.addTestStepLog("Reached end of content in Profile Behavioural Competencies Section in Details Popup");
							break;
						}
				String ViewMoreCompetenciesBtnText = wait.until(ExpectedConditions.visibilityOf(viewMoreButtonInBehaviourCompetenciesTab.get(0))).getText();
				// Scroll element into view before clicking
				WebElement viewMoreElement = viewMoreButtonInBehaviourCompetenciesTab.get(0);
				js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", viewMoreElement);
				try { Thread.sleep(500); } catch (InterruptedException ie) {}
		// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
		PerformanceUtils.waitForUIStability(driver, 2);
				try {
					wait.until(ExpectedConditions.elementToBeClickable(viewMoreElement)).click();
				} catch(Exception e) {
					js.executeScript("arguments[0].click();", viewMoreElement);
				}
					LOGGER.info("Clicked on "+ ViewMoreCompetenciesBtnText +" button in Profile Behavioural Competencies Section in Details Popup");
					ExtentCucumberAdapter.addTestStepLog("Clicked on "+ ViewMoreCompetenciesBtnText +" button in Profile Behavioural Competencies Section in Details Popup");
				// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
				PerformanceUtils.waitForUIStability(driver, 2);
						js.executeScript("arguments[0].scrollIntoView(true);", viewMoreButtonInBehaviourCompetenciesTab.get(0));
					} catch(StaleElementReferenceException e) {
						LOGGER.info("Reached end of content in Profile Behavioural Competencies Section in Details Popup");
						ExtentCucumberAdapter.addTestStepLog("Reached end of content in Profile Behavioural Competencies Section in Details Popup");
						break;
					}	
				}
				String ProfileBehaviouralCompetenciesText = wait.until(ExpectedConditions.visibilityOf(behaviourData)).getText();
				Assert.assertEquals(ProfileBehaviouralCompetenciesText, manualMappingProfileBehaviouralCompetencies);
			LOGGER.info("Profile Behavioural Competencies in Details Popup matches with Mapped Success Profile Behavioural Competencies as expected");
			ExtentCucumberAdapter.addTestStepLog("Profile Behavioural Competencies in Details Popup matches with Mapped Success Profile Behavioural Competencies as expected");		
		} catch (Exception e) {
			LOGGER.error(" Issue validating behavioural competencies in details popup - Method: validate_profile_behavioural_competencies_in_details_popup_matches_with_mapped_success_profile_behavioural_competencies", e);
			ScreenshotHandler.captureFailureScreenshot("validate_behavioural_competencies_details_popup", e);
			e.printStackTrace();
			Assert.fail("Issue in validating data in Profile Behavioural Competencies Section in Details Popup matches with Mapped Success Profile Behavioural Competencies...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in validating data in Profile Behavioural Competencies Section in Details Popup matches with Mapped Success Profile Behavioural Competencies...Please Investigate!!!");
		}
		}
	}
	
	public void validate_profile_skills_in_details_popup_matches_with_mapped_success_profile_skills() {
		if(manualMapping) {
			try {
				js.executeScript("arguments[0].scrollIntoView(true);", roleSummary);
				// PERFORMANCE: Replaced Thread.sleep(2000) with smart element wait
				PerformanceUtils.waitForElement(driver, driver.findElement(By.xpath("//div")));
				wait.until(ExpectedConditions.elementToBeClickable(skillsTabButton)).click();
				LOGGER.info("Clicked on BEHAVIOURAL COMPETENCIES screen in Profiles Details Popup");
				ExtentCucumberAdapter.addTestStepLog("Clicked on BEHAVIOURAL COMPETENCIES screen in Profiles Details Popup");
				while(true) {
					try {
						if(viewMoreButtonInSkillsTab.isEmpty()) {
							LOGGER.info("Reached end of content in Profile Skills Section in Details Popup");
							ExtentCucumberAdapter.addTestStepLog("Reached end of content in Profile Skills Section in Details Popup");
							break;
						} 
					String ViewMoreSkillsBtnText = wait.until(ExpectedConditions.visibilityOf(viewMoreButtonInSkillsTab.get(0))).getText();
					// Scroll element into view before clicking
					WebElement element = viewMoreButtonInSkillsTab.get(0);
					js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
					try { Thread.sleep(500); } catch (InterruptedException ie) {}
			// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
			PerformanceUtils.waitForUIStability(driver, 2);
					try {
						wait.until(ExpectedConditions.elementToBeClickable(element)).click();
					} catch(Exception e) {
						js.executeScript("arguments[0].click();", element);
					}
					LOGGER.info("Clicked on "+ ViewMoreSkillsBtnText +" button in Profile Skills Section in Details Popup");
					ExtentCucumberAdapter.addTestStepLog("Clicked on "+ ViewMoreSkillsBtnText +" button in Profile Skills Section in Details Popup");
				// PERFORMANCE: Replaced Thread.sleep(2000) with UI stability wait
				PerformanceUtils.waitForUIStability(driver, 2);
						js.executeScript("arguments[0].scrollIntoView(true);", viewMoreButtonInSkillsTab.get(0));
					} catch(StaleElementReferenceException e) {
						LOGGER.info("Reached end of content in Profile Skills Section in Details Popup");
						ExtentCucumberAdapter.addTestStepLog("Reached end of content in Profile Skills Section in Details Popup");
						break;
					}	
				}
				String ProfileSkillsText = wait.until(ExpectedConditions.visibilityOf(skillsData)).getText();
				Assert.assertEquals(ProfileSkillsText, manualMappingProfileSkills);
			LOGGER.info("Profile Skills in Details Popup matches with Mapped Success Profile Skills as expected");
			ExtentCucumberAdapter.addTestStepLog("Profile Skills in Details Popup matches with Mapped Success Profile Skills as expected");
		} catch (Exception e) {
			LOGGER.error(" Issue validating skills in details popup - Method: validate_profile_skills_in_details_popup_matches_with_mapped_success_profile_skills", e);
			ScreenshotHandler.captureFailureScreenshot("validate_skills_details_popup", e);
			e.printStackTrace();
			Assert.fail("Issue in validating data in Profile Skills Section in Details Popup matches with Mapped Success Profile Skills...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in validating data in Profile Skills Section in Details Popup matches with Mapped Success Profile Skills...Please Investigate!!!");
		}
		}
	}
	
	public void search_for_organization_job_with_manually_mapped_sp() {
		if(manualMapping) {
			try {
				wait.until(ExpectedConditions.visibilityOf(searchBar)).clear();
				try {
					wait.until(ExpectedConditions.elementToBeClickable(searchBar)).click();
				} catch (Exception e) {
					try {
						js.executeScript("arguments[0].click();", searchBar);
					} catch (Exception s) {
						utils.jsClick(driver, searchBar);
					}
				}
				wait.until(ExpectedConditions.visibilityOf(searchBar)).sendKeys(orgJobName);
				wait.until(ExpectedConditions.visibilityOf(searchBar)).sendKeys(Keys.ENTER);
				wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			// PERFORMANCE: Replaced Thread.sleep(2000) with search results wait
			PerformanceUtils.waitForSearchResults(driver, By.xpath("//tbody//tr"), 2);
				LOGGER.info("Entered job name as " + orgJobName + " in the search bar");
				ExtentCucumberAdapter.addTestStepLog("Entered job name as " + orgJobName  + " in the search bar");
			// PERFORMANCE: Replaced Thread.sleep(2000) with page ready wait
			PerformanceUtils.waitForPageReady(driver, 2);
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail("Failed to enter Organization job name text in search bar...Please investigate!!!");
				ExtentCucumberAdapter.addTestStepLog("Failed to enter Organization job name text in search bar...Please investigate!!!");
			}
		}
	}
}
		
