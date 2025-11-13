package com.kfonetalentsuite.pageobjects.JobMapping;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.kfonetalentsuite.utils.JobMapping.Utilities;
import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.ScreenshotHandler;
import com.kfonetalentsuite.webdriverManager.DriverManager;
import org.openqa.selenium.interactions.Actions;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

public class PO02_ValidateAddMoreJobsFunctionality {
	WebDriver driver = DriverManager.getDriver();	

	protected static final Logger LOGGER = (Logger) LogManager.getLogger();
	PO02_ValidateAddMoreJobsFunctionality validateAddMoreJobsFunctionality;
	
	public static String lastSyncedInfo1;
	public static String ResultsCountBeforeAddingMoreJobs;
	public static String KFONEjobsCountBeforeAddingMoreJobs;

	public PO02_ValidateAddMoreJobsFunctionality() throws IOException {
		// super();
		PageFactory.initElements(driver, this);
		// TODO Auto-generated constructor stub
	}

	WebDriverWait wait = DriverManager.getWait();
	Utilities utils = new Utilities();
	JavascriptExecutor js = (JavascriptExecutor) driver;
	
	/**
	 * Helper method to try multiple click strategies for an element
	 */
	private boolean tryClickElement(WebElement element, String elementName) {
		try {
			// Strategy 1: Regular click
			if (element.isDisplayed() && element.isEnabled()) {
				element.click();
				// PERFORMANCE: Replaced Thread.sleep(1000) with UI stability wait
				PerformanceUtils.waitForUIStability(driver, 2);
				LOGGER.debug("Successfully clicked " + elementName + " using regular click");
				return true;
			}
		} catch (Exception e) {
			LOGGER.debug("Regular click failed for " + elementName + ": " + e.getMessage());
		}
		
		try {
			// Strategy 2: JavaScript click
			js.executeScript("arguments[0].click();", element);
			// PERFORMANCE: Replaced Thread.sleep(1000) with UI stability wait
			PerformanceUtils.waitForUIStability(driver, 2);
			LOGGER.debug("Successfully clicked " + elementName + " using JavaScript click");
			return true;
		} catch (Exception e) {
			LOGGER.debug("JavaScript click failed for " + elementName + ": " + e.getMessage());
		}
		
		try {
			// Strategy 3: Actions click
			new Actions(driver).moveToElement(element).click().perform();
			// PERFORMANCE: Replaced Thread.sleep(1000) with UI stability wait
			PerformanceUtils.waitForUIStability(driver, 2);
			LOGGER.debug("Successfully clicked " + elementName + " using Actions click");
			return true;
		} catch (Exception e) {
			LOGGER.debug("Actions click failed for " + elementName + ": " + e.getMessage());
		}
		
		return false;
	}
	
	//XPATHs
	@FindBy(xpath = "//div[@data-testid='loader']//img")
	@CacheLookup
	WebElement pageLoadSpinner2; 
	
	@FindBy(xpath = "//div[contains(@id,'results-toggle')]//*[contains(text(),'Showing')]")
	@CacheLookup
	public WebElement showingJobResultsCount;
	
	@FindBy(xpath = "//*[contains(text(),'Add Job Data')]")
	@CacheLookup
	public WebElement addMoreJobsPageHeader;
	
	@FindBy(xpath = "//button[@data-testid='manual-upload-btn']")
	public WebElement manualUploadBtn;
	
	@FindBy(xpath = "//a[@id='jobDownload']")
	@CacheLookup
	public WebElement downloadTemplateBtn;
	
	@FindBy(xpath = "//span[contains(@class,'regular-small')]")
	@CacheLookup
	public WebElement KFONEjobsCount;
	
	@FindBy(xpath = "//*[@aria-label='Browse Files']")
	public WebElement browseFilesBtn;
	
	@FindBy(xpath = "//div[contains(@class,'text-ods-font-styles-body-regular-small')]")
	@CacheLookup
	public WebElement attachedFileName;
	
	@FindBy(xpath = "//*[@aria-label='fileclose']//*[@stroke-linejoin='round']")
	@CacheLookup
	public WebElement fileCloseBtn;
	
	@FindBy(xpath = "//button[@id='btnContinue']")
	public WebElement continueBtn;
	
	@FindBy(xpath = "//p[contains(text(),'Upload in progress')]")
	@CacheLookup
	public WebElement uploadProgressText;
	
	@FindBy(xpath = "//strong[contains(text(),'refresh')]//..")
	@CacheLookup
	public WebElement refreshMessageInfo;
	
	@FindBy(xpath = "//a[@id='clickHere']")
	@CacheLookup
	public WebElement clickHereBtn;
	
	@FindBy(xpath = "//div[contains(text(),'Last Synced')]")
	@CacheLookup
	public WebElement lastsyncedInfo;
	
	@FindBy(xpath = "//span[contains(text(),'jobs')]")
	@CacheLookup
	public WebElement uploadedJobsCountInfo;
	
	@FindBy(xpath = "//strong[contains(text(),'Try uploading')]//..")
	@CacheLookup
	public WebElement uploadFailedWarningMessage;
	
	@FindBy(xpath = "//a[@id='btnClose']")
	@CacheLookup
	public WebElement messageCloseBtn;
	
	@FindBy(xpath = "//p[text()='Upload complete!']")
	@CacheLookup
	public WebElement uploadSuccessMessage;
	
	@FindBy(xpath = "//span[@title='Try Again']//..")
	@CacheLookup
	public WebElement tryAgainBtn;
	
	
	@FindBy(xpath = "//*[@data-testid='x-mark-icon']//*")
	@CacheLookup
	public WebElement addMoreJobsCloseBtn;
	
	@FindBy(xpath = "//button[@id='ensAcceptAll']")
	@CacheLookup
	WebElement acceptAllCookies;
	
	@FindBy(xpath = "//button[@id='btnDone']")
	public WebElement doneBtn;
	
	
	//METHODs
	public void verify_unpublished_jobs_count_before_adding_more_jobs() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			// PERFORMANCE: Replaced Thread.sleep(3000) with smart page ready wait
			PerformanceUtils.waitForPageReady(driver);
			
			// Try to find the results count element first
			try {
				String resultsCountText = wait.until(ExpectedConditions.visibilityOf(showingJobResultsCount)).getText();
				ResultsCountBeforeAddingMoreJobs = resultsCountText.split(" ")[3];
				LOGGER.info("Unpublished Job Profiles Count in AI Auto UI / Job Mapping page before Adding More Jobs : " + ResultsCountBeforeAddingMoreJobs);
				ExtentCucumberAdapter.addTestStepLog("Unpublished Job Profiles Count in AI Auto UI / Job Mapping page before Adding More Jobs : " + ResultsCountBeforeAddingMoreJobs);
			} catch (Exception e) {
				// If results count element not found, check for "Nothing to see here... yet!" message
				LOGGER.debug("Results count element not found, checking for 'Nothing to see here... yet!' message");
				try {
					WebElement nothingToSeeElement = wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//span[contains(@class,'font-proxima') and contains(@class,'text-[24px]') and contains(@class,'font-semibold') and contains(text(),'Nothing to see here... yet!')]")
					));
					
					if (nothingToSeeElement.isDisplayed()) {
						ResultsCountBeforeAddingMoreJobs = "0";
						LOGGER.info(" 'Nothing to see here... yet!' message displayed - Setting count to 0");
						LOGGER.info("Unpublished Job Profiles Count in AI Auto UI / Job Mapping page before Adding More Jobs : " + ResultsCountBeforeAddingMoreJobs);
						ExtentCucumberAdapter.addTestStepLog(" No unpublished jobs found - Count set to 0 (Empty state message displayed)");
					} else {
						throw new Exception("Neither results count nor empty state message found");
					}
				} catch (Exception emptyStateException) {
					// If "Nothing to see here... yet!" element is also not found, throw the original exception
					LOGGER.debug("Empty state message also not found, failing the step");
					throw e;
				}
			}
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_unpublished_jobs_count_before_adding_more_jobs", e);
			LOGGER.error(" Issue in verifying Unpublished job profiles count - Method: verify_unpublished_job_profiles_count", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog(" Issue in verifying Unpublished job profiles count in Job Mapping page before adding more jobs...Please Investigate!!!");
			Assert.fail("Issue in verifying Unpublished job profiles count in Job Mapping page before adding more jobs...Please Investigate!!!");
		}
	}
	public void user_should_be_landed_on_kfone_add_job_data_page() {
		try {
			wait.until(ExpectedConditions.visibilityOf(addMoreJobsPageHeader)).isDisplayed();
			String addMoreJobsPageheaderText = wait.until(ExpectedConditions.visibilityOf(addMoreJobsPageHeader)).getText();
			Assert.assertEquals(addMoreJobsPageheaderText, "Add Job Data");
			LOGGER.info("User landed on KFONE Add Job Data page Successfully");
			ExtentCucumberAdapter.addTestStepLog("User landed on KFONE Add Job Data page Successfully"); 
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_be_landed_on_kfone_add_job_data_page", e);
			LOGGER.error("Issue in landing KFONE Add Job Data page - Method: verify_landing_kfone_add_job_data_page", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in landing KFONE Add Job Data page...Please Investigate!!!");
			Assert.fail("Issue in landing KFONE Add Job Data page...Please Investigate!!!");
		}
	}
	
	public void user_is_in_kfone_add_job_data_page() {
		try {
			// First verify we're on the KFONE Add Job Data page
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(addMoreJobsPageHeader)).isDisplayed());
			String pageHeaderText = addMoreJobsPageHeader.getText();
			Assert.assertEquals(pageHeaderText, "Add Job Data");
			LOGGER.info("User is in KFONE Add Job Data page - Page header verified: " + pageHeaderText);
			ExtentCucumberAdapter.addTestStepLog("User is in KFONE Add Job Data page - Page header verified: " + pageHeaderText);
		
			// Handle cookies banner if present
			try {
				WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
				WebElement cookiesBtn = shortWait.until(ExpectedConditions.elementToBeClickable(acceptAllCookies));
				
				// Try multiple click strategies for cookies banner
				if (!tryClickElement(cookiesBtn, "cookies accept button")) {
					// Fallback to JavaScript click
					js.executeScript("arguments[0].click();", acceptAllCookies);
				}
				
				ExtentCucumberAdapter.addTestStepLog("Closed Cookies Banner by clicking on Accept All button");
				LOGGER.info("Closed Cookies Banner by clicking on Accept All button");
			} catch(Exception e) {
				ExtentCucumberAdapter.addTestStepLog("Cookies Banner is already accepted or not present");
				LOGGER.info("Cookies Banner is already accepted or not present");
			}
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_is_in_kfone_add_job_data_page", e);
			LOGGER.error("Issue in validating KFONE Add Job Data page - Method: validate_kfone_add_job_data_page", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in validating KFONE Add Job Data page...Please Investigate!!!");
			Assert.fail("Issue in validating KFONE Add Job Data page...Please Investigate!!!");
		}
	}
	
	public void user_should_click_on_manual_upload_button() {
		try {
			// Get text before clicking to avoid stale element issues
			WebElement manualUploadElement = wait.until(ExpectedConditions.visibilityOf(manualUploadBtn));
			String buttonText = manualUploadElement.getText();
			
			Assert.assertTrue(manualUploadElement.isDisplayed());
			wait.until(ExpectedConditions.elementToBeClickable(manualUploadBtn)).click();
			
			LOGGER.info(buttonText + " button is displaying on KFONE Add Data screen as expected and clicked on Button...");
			ExtentCucumberAdapter.addTestStepLog(buttonText +  " button is displaying on KFONE Add Data screen as expected and clicked on Button...");
		} catch (StaleElementReferenceException e) {
			try {
				// Re-locate the element using By locator instead of cached reference
				WebElement manualUploadElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@data-testid='manual-upload-btn']")));
				String buttonText = manualUploadElement.getText();
				
				Assert.assertTrue(manualUploadElement.isDisplayed());
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@data-testid='manual-upload-btn']"))).click();
				
				LOGGER.info(buttonText + " button is displaying on KFONE Add Data screen as expected and clicked on Button...");
				ExtentCucumberAdapter.addTestStepLog(buttonText +  " button is displaying on KFONE Add Data screen as expected and clicked on Button...");
			} catch(Exception s) {
				ScreenshotHandler.captureFailureScreenshot("user_should_click_on_manual_upload_button", s);
				LOGGER.error("Issue in clicking Manual upload Button - Method: click_manual_upload_button", s);
				s.printStackTrace();
				ExtentCucumberAdapter.addTestStepLog("Issue in Clicking Manual upload Button on KFONE Add Job Data screen...Please Investigate!!!");
				Assert.fail("Issue in Clicking Manual upload Button on KFONE Add Job Data screen...Please Investigate!!!");
			}
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_click_on_manual_upload_button", e);
			LOGGER.error("Issue in clicking Manual upload Button - Method: click_manual_upload_button", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in Clicking Manual upload Button on KFONE Add Job Data screen...Please Investigate!!!");
			Assert.fail("Issue in Clicking Manual upload Button on KFONE Add Job Data screen...Please Investigate!!!");
		}
	}
	
	// REMOVED: Unused method - corresponding step is commented out in feature file
	
	public void verify_jobs_count_in_kfone_add_job_data_screen_before_adding_more_jobs() {
		try {
			PerformanceUtils.waitForPageReady(driver, 2);
			KFONEjobsCountBeforeAddingMoreJobs = wait.until(ExpectedConditions.visibilityOf(KFONEjobsCount)).getText();
			LOGGER.info("Jobs count in KFONE Add Job Data screen before Adding More Jobs : " + KFONEjobsCountBeforeAddingMoreJobs);
			ExtentCucumberAdapter.addTestStepLog("Jobs count in KFONE Add Job Data screen before Adding More Jobs : " + KFONEjobsCountBeforeAddingMoreJobs);
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_jobs_count_in_kfone_add_job_data_screen_before_adding_more_jobs", e);
			LOGGER.error("Issue in verifying Jobs count before adding - Method: verify_jobs_count_before_adding", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying Jobs count in KFONE Add Job Data screen before Adding More Jobs...Please Investigate!!!");
			Assert.fail("Issue in verifying Jobs count in KFONE Add Job Data screen before Adding More Jobs...Please Investigate!!!");
		}
	}
	
	public void verify_last_synced_info_on_add_job_data_screen_before_adding_more_jobs() {
		try {
			js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
			PerformanceUtils.waitForUIStability(driver, 2);
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(lastsyncedInfo)).isDisplayed());
			String lastSyncedInfoText1 = lastsyncedInfo.getText();
			lastSyncedInfo1 = lastSyncedInfoText1;
			LOGGER.info("Last Synced Info on KFONE Add Job Data screen before adding More Jobs : " + lastSyncedInfoText1);
			ExtentCucumberAdapter.addTestStepLog("Last Synced Info on KFONE Add Job Data screen before adding More Jobs : " + lastSyncedInfoText1);
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_last_synced_info_on_add_job_data_screen_before_adding_more_jobs", e);
			LOGGER.error("Issue in verifying Last Synced Info before adding - Method: verify_last_synced_info_before_adding", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying Last Synced Info on KFONE Add Job Data screen before adding more jobs...Please Investigate!!!");
			Assert.fail("Issue in verifying Last Synced Info on KFONE Add Job Data screen before adding more jobs...Please Investigate!!!");
		}
	}
	
	public void upload_job_catalog_file_using_browse_files_button() {
		String uploadFilePath = System.getProperty("user.dir") + File.separator + "src" + File.separator + 
								"test" + File.separator + "resources" + File.separator + "Job Catalog with 100 profiles.csv";
		
		// Verify file exists before attempting upload
		File uploadFile = new File(uploadFilePath);
		if (!uploadFile.exists()) {
			String errorMsg = "Upload file does not exist at path: " + uploadFilePath;
			LOGGER.error(errorMsg);
			ExtentCucumberAdapter.addTestStepLog(errorMsg);
			Assert.fail(errorMsg);
		}
		
		try {
			LOGGER.info("Starting file upload process for headless execution...");
			ExtentCucumberAdapter.addTestStepLog("Starting file upload process for headless execution...");
			
			// Strategy 1: Try to find hidden file input element directly
			boolean uploadSuccess = tryDirectFileInput(uploadFilePath);
			
			// Strategy 2: If direct approach fails, try clicking upload button and finding input
			if (!uploadSuccess) {
				uploadSuccess = tryClickAndFindInput(uploadFilePath);
			}
			
			// Strategy 3: If both fail, try JavaScript approach
			if (!uploadSuccess) {
				uploadSuccess = tryJavaScriptUpload(uploadFilePath);
			}
			
			// Strategy 4: Last resort - try Robot class for non-headless environments
			if (!uploadSuccess && !isHeadlessMode()) {
				uploadSuccess = tryRobotClassUpload(uploadFilePath);
			}
			
			if (!uploadSuccess) {
				throw new RuntimeException("All file upload strategies failed");
			}
			
			// Verify upload success
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(attachedFileName)).isDisplayed());
			LOGGER.info("Job catalog file uploaded successfully using Browse Files button...");
			ExtentCucumberAdapter.addTestStepLog("Job catalog file uploaded successfully using Browse Files button...");
			
			String uploadedFileNameText = wait.until(ExpectedConditions.visibilityOf(attachedFileName)).getText();
			LOGGER.info("File name : " + uploadedFileNameText + " is displaying on KFONE Add Job Data screen as Expected");
			ExtentCucumberAdapter.addTestStepLog("File name : " + uploadedFileNameText + " is displaying on KFONE Add Job Data screen as Expected");
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("upload_job_catalog_file_using_attach_file_button", e);
			e.printStackTrace();
			String errorMsg = "Issue in uploading Job Catalog file using Upload File button on KFONE Add Job Data screen. Error: " + e.getMessage();
			LOGGER.error(errorMsg);
			ExtentCucumberAdapter.addTestStepLog(errorMsg);
			Assert.fail(errorMsg);
		}
	}
	
	/**
	 * Strategy 1: Try to find file input element directly without clicking upload button
	 */
	private boolean tryDirectFileInput(String uploadFilePath) {
		try {
			LOGGER.info("Attempting Strategy 1: Direct file input approach...");
			
			// Common patterns for file input elements
			String[] inputSelectors = {
				"//input[@type='file']",
				"//input[contains(@id,'upload')]",
				"//input[contains(@class,'upload')]",
				"//input[contains(@name,'file')]",
				"//input[contains(@accept,'.csv')]"
			};
			
			for (String selector : inputSelectors) {
				try {
					List<WebElement> fileInputs = driver.findElements(By.xpath(selector));
					for (WebElement input : fileInputs) {
						if (input.isEnabled()) {
							LOGGER.info("Found file input using selector: " + selector);
						input.sendKeys(uploadFilePath);
						PerformanceUtils.waitForUIStability(driver, 2);
						return true;
						}
					}
				} catch (Exception e) {
					// Continue to next selector
					continue;
				}
			}
			
			LOGGER.info("Strategy 1 failed - No accessible file input found");
			return false;
			
		} catch (Exception e) {
			LOGGER.warn("Strategy 1 failed with exception: " + e.getMessage());
			return false;
		}
	}
	
	/**
	 * Strategy 2: Click upload button and then find the file input element
	 */
	private boolean tryClickAndFindInput(String uploadFilePath) {
		try {
			LOGGER.info("Attempting Strategy 2: Click button then find input approach...");
			
			// Get button text before clicking to avoid stale element issues
			String buttonText = browseFilesBtn.getText();
			
			// Click the upload button to potentially reveal hidden input
			wait.until(ExpectedConditions.elementToBeClickable(browseFilesBtn)).click();
			LOGGER.info("Clicked on " + buttonText + " Button....");
			ExtentCucumberAdapter.addTestStepLog("Clicked on " + buttonText + " Button....");
			
			PerformanceUtils.waitForUIStability(driver, 1);
			
			// Now try to find file input elements
			String[] inputSelectors = {
				"//input[@type='file']",
				"//input[contains(@style,'opacity: 0') or contains(@style,'display: none')][@type='file']",
				"//input[contains(@id,'upload')]",
				"//input[contains(@class,'upload')]",
				"//input[contains(@name,'file')]"
			};
			
			for (String selector : inputSelectors) {
				try {
					WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
					List<WebElement> fileInputs = shortWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(selector)));
					
					for (WebElement input : fileInputs) {
						try {
							// Make input visible and interactable if hidden
							js.executeScript("arguments[0].style.display = 'block'; arguments[0].style.visibility = 'visible'; arguments[0].style.opacity = '1';", input);
						input.sendKeys(uploadFilePath);
						PerformanceUtils.waitForUIStability(driver, 2);
						LOGGER.info("Successfully uploaded file using selector: " + selector);
							return true;
						} catch (Exception e) {
							// Try next input element
							continue;
						}
					}
				} catch (Exception e) {
					// Try next selector
					continue;
				}
			}
			
			LOGGER.info("Strategy 2 failed - No file input found after clicking button");
			return false;
			
		} catch (Exception e) {
			LOGGER.warn("Strategy 2 failed with exception: " + e.getMessage());
			return false;
		}
	}
	
	/**
	 * Strategy 3: Use JavaScript to handle file upload
	 */
	private boolean tryJavaScriptUpload(String uploadFilePath) {
		try {
			LOGGER.info("Attempting Strategy 3: JavaScript upload approach...");
			
			// Try to create a file input dynamically and trigger upload
			String script = """
				var fileInput = document.createElement('input');
				fileInput.type = 'file';
				fileInput.style.display = 'none';
				document.body.appendChild(fileInput);
				
				var uploadBtn = document.getElementById('btnUpload');
				if (uploadBtn) {
					uploadBtn.onclick = function() {
						fileInput.click();
						return false;
					};
				}
				return fileInput;
			""";
			
			WebElement dynamicInput = (WebElement) js.executeScript(script);
			if (dynamicInput != null) {
			dynamicInput.sendKeys(uploadFilePath);
			PerformanceUtils.waitForUIStability(driver, 2);
			LOGGER.info("Successfully uploaded file using JavaScript approach");
				return true;
			}
			
			LOGGER.info("Strategy 3 failed - Could not create dynamic file input");
			return false;
			
		} catch (Exception e) {
			LOGGER.warn("Strategy 3 failed with exception: " + e.getMessage());
			return false;
		}
	}
	
	/**
	 * Strategy 4: Fallback to Robot class for non-headless environments
	 */
	private boolean tryRobotClassUpload(String uploadFilePath) {
		try {
			LOGGER.info("Attempting Strategy 4: Robot class fallback approach...");
			LOGGER.warn("Using Robot class - this will not work in headless mode");
			
			// Ensure upload button is clicked
			wait.until(ExpectedConditions.elementToBeClickable(browseFilesBtn)).click();
			
			// Using Robot Class Methods (original implementation)
			Robot rb = new Robot();
			rb.delay(2000);
			
			// Put the File into Clipboard
			StringSelection ss = new StringSelection(uploadFilePath);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
			
			// Press CTRL+V
			rb.keyPress(KeyEvent.VK_CONTROL);
			rb.keyPress(KeyEvent.VK_V);
			rb.delay(2000);
			
			// Release CTRL+V
			rb.keyRelease(KeyEvent.VK_CONTROL);
			rb.keyRelease(KeyEvent.VK_V);
			rb.delay(2000);
			
			// Press and Release ENTER Key
			rb.keyPress(KeyEvent.VK_ENTER);
			rb.keyRelease(KeyEvent.VK_ENTER);
			rb.delay(2000);
			
			LOGGER.info("Robot class upload completed");
			return true;
			
		} catch (Exception e) {
			LOGGER.warn("Strategy 4 (Robot class) failed with exception: " + e.getMessage());
			return false;
		}
	}
	
	/**
	 * Check if browser is running in headless mode
	 */
	private boolean isHeadlessMode() {
		try {
			// Try to get browser capabilities to detect headless mode
			return js.executeScript("return navigator.webdriver === true").toString().equals("true") ||
				   System.getProperty("java.awt.headless", "false").equals("true") ||
				   System.getProperty("webdriver.chrome.args", "").contains("--headless") ||
				   System.getProperty("webdriver.firefox.args", "").contains("--headless");
		} catch (Exception e) {
			// Default to assuming headless if we can't determine
			LOGGER.info("Could not determine headless mode, assuming headless: " + e.getMessage());
			return true;
		}
	}
		
	
	public void user_should_verify_file_close_button_displaying_and_clickable() {
		try {
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(fileCloseBtn)).isDisplayed());
			wait.until(ExpectedConditions.visibilityOf(fileCloseBtn)).click();
			LOGGER.info("File Close button is displaying on KFONE Add Data screen as expected and clicked on Button...");
			ExtentCucumberAdapter.addTestStepLog("File Close button is displaying on KFONE Add Data screen as expected and clicked on Button...");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_verify_file_close_button_displaying_and_clickable", e);
			LOGGER.error("Issue in verifying File Close Button - Method: user_should_verify_file_close_button_displaying_and_clickable", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying File Close Button on KFONE Add Job Data screen...Please Investigate!!!");
			Assert.fail("Issue in Verifying File Close Button on KFONE Add Job Data screen...Please Investigate!!!");
		}
	}
	
	public void click_on_continue_button_in_add_job_data_screen() {
		try {
			// Get text before clicking to avoid stale element issues
			WebElement continueBtnElement = wait.until(ExpectedConditions.visibilityOf(continueBtn));
			String buttonText = continueBtnElement.getText();
			
			Assert.assertTrue(continueBtnElement.isDisplayed());
			wait.until(ExpectedConditions.elementToBeClickable(continueBtn)).click();
			
			LOGGER.info(buttonText + " button is displaying on KFONE Add Data screen as expected and clicked on Button...");
			ExtentCucumberAdapter.addTestStepLog(buttonText +  " button is displaying on KFONE Add Data screen as expected and clicked on Button...");
		} catch (StaleElementReferenceException e) {
			try {
				// Re-locate the element using By locator instead of cached reference
				WebElement continueBtnElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("//button[@id='btnContinue']")));
				String buttonText = continueBtnElement.getText();
				
				Assert.assertTrue(continueBtnElement.isDisplayed());
				wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("//button[@id='btnContinue']"))).click();
				
				LOGGER.info(buttonText + " button is displaying on KFONE Add Data screen as expected and clicked on Button...");
				ExtentCucumberAdapter.addTestStepLog(buttonText +  " button is displaying on KFONE Add Data screen as expected and clicked on Button...");
			} catch(Exception s) {
				ScreenshotHandler.captureFailureScreenshot("click_on_continue_button_in_add_job_data_screen", s);
				LOGGER.error("Issue in clicking Continue Button - Method: click_on_continue_button_in_add_job_data_screen", s);
				s.printStackTrace();
				ExtentCucumberAdapter.addTestStepLog("Issue in Clicking Continue Button on KFONE Add Job Data screen...Please Investigate!!!");
				Assert.fail("Issue in Clicking Continue Button on KFONE Add Job Data screen...Please Investigate!!!");
			}
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_on_continue_button_in_add_job_data_screen", e);
			LOGGER.error("Issue in clicking Continue Button - Method: click_on_continue_button_in_add_job_data_screen", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in Clicking Continue Button on KFONE Add Job Data screen...Please Investigate!!!");
			Assert.fail("Issue in Clicking Continue Button on KFONE Add Job Data screen...Please Investigate!!!");
		}
	}
	
	public void user_should_validate_job_data_upload_is_in_progress() {
		try {
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(uploadProgressText)).isDisplayed());
			LOGGER.info("After Clickng Continue button, " + uploadProgressText.getText() + " is displaying on KFONE Add Data screen as expected");
			ExtentCucumberAdapter.addTestStepLog("After Clickng Continue button, " + uploadProgressText.getText() + " is displaying on KFONE Add Data screen as expected");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_validate_job_data_upload_is_in_progress", e);
			LOGGER.error("Issue in validating Upload in Progress on KFONE Add Job Data screen - Method: user_should_validate_job_data_upload_is_in_progress", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in validating Upload in Progress on KFONE Add Job Data screen...Please Investigate!!!");
			Assert.fail("Issue in validating Upload in Progress on KFONE Add Job Data screen...Please Investigate!!!");
		}
	}
	
	public void user_should_validate_job_data_added_successfully() {
		try {
			LOGGER.info("Waiting for 2 Minutes before Refreshing page on KFONE Add Job Data screen");
			ExtentCucumberAdapter.addTestStepLog("Waiting for 2 Minutes before Refreshing page on KFONE Add Job Data screen");
			Thread.sleep(120000);
			LOGGER.info("Completed 2 Minutes Waiting Period.....");
			wait.until(ExpectedConditions.elementToBeClickable(clickHereBtn)).click();
			LOGGER.info("Clicked on Click Here button on KFONE Add Job Data screen to Refresh the page");
			ExtentCucumberAdapter.addTestStepLog("Clicked on Click Here button on KFONE Add Job Data screen to Refresh the page");
			
			PerformanceUtils.waitForUIStability(driver, 2);
			LOGGER.info(uploadSuccessMessage.getText() + " on Add Job Data screen");
			ExtentCucumberAdapter.addTestStepLog(uploadSuccessMessage.getText() + " on Add Job Data screen");
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_validate_job_data_added_successfully", e);
			LOGGER.error("Issue in validating Job data added successfully - Method: validate_job_data_added_successfully", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in validating Job data added successfully or not in KFONE Add Job Data screen...Please Investigate!!!");
			Assert.fail("Issue in validating Job data added successfully or not in KFONE Add Job Data screen...Please Investigate!!!");
		}
	}
	
	public void verify_jobs_count_in_kfone_add_job_data_screen_after_adding_more_jobs() {
		try {
			PerformanceUtils.waitForUIStability(driver, 2);
			js.executeScript("window.scrollTo(document.body.scrollHeight, 0)");
			String KFONEjobsCountAfterAddingMoreJobs = wait.until(ExpectedConditions.visibilityOf(KFONEjobsCount)).getText();
			LOGGER.info("Jobs count in KFONE Add Job Data screen after Adding More Jobs : " + KFONEjobsCountAfterAddingMoreJobs);
			ExtentCucumberAdapter.addTestStepLog("Jobs count in KFONE Add Job Data screen after Adding More Jobs : " + KFONEjobsCountAfterAddingMoreJobs);
			if(!(KFONEjobsCountBeforeAddingMoreJobs.equals(KFONEjobsCountAfterAddingMoreJobs))) {
				LOGGER.info("KFONE Jobs count is UPDATED as Expected in KFONE Add Job Data screen after Adding More Jobs....");
				ExtentCucumberAdapter.addTestStepLog("KFONE Jobs count is UPDATED as Expected in KFONE Add Job Data screen after Adding More Jobs....");
			} else {
				Assert.fail("KFONE Jobs count in KFONE Add Job Data screen before Adding More Jobs : " + KFONEjobsCountBeforeAddingMoreJobs);
				throw new Exception("KFONE Jobs count is NOT UPDATED in KFONE Add Job Data screen after adding More Jobs...Please Investigate!!!");
			}
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_jobs_count_in_kfone_add_job_data_screen_after_adding_more_jobs", e);
			LOGGER.error(" Issue in verifying Jobs count after adding - Method: verify_jobs_count_after_adding", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog(" Issue in verifying Jobs count in KFONE Add Job Data screen after Adding More Jobs...Please Investigate!!!");
			Assert.fail("Issue in verifying Jobs count in KFONE Add Job Data screen after Adding More Jobs...Please Investigate!!!");
		}
	}
	
	public void verify_last_synced_info_on_add_job_data_screen_after_adding_more_jobs() {
		try {
			PerformanceUtils.waitForUIStability(driver, 2);
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(lastsyncedInfo)).isDisplayed());
			String lastSyncedInfoText2 = lastsyncedInfo.getText();
			LOGGER.info("Last Synced Info on KFONE Add Job Data screen after adding More Jobs : " + lastSyncedInfoText2);
			ExtentCucumberAdapter.addTestStepLog("Last Synced Info on KFONE Add Job Data screen before adding More Jobs : " + lastSyncedInfoText2);
			if(!(lastSyncedInfo1.equals(lastSyncedInfoText2))) {
				LOGGER.info("Last Synced Info on KFONE Add Job Data screen is UPDATED as Expected after adding More Jobs....");
				ExtentCucumberAdapter.addTestStepLog("Last Synced Info on KFONE Add Job Data screen is UPDATED as Expected after adding More Jobs....");
			} else {
				Assert.fail("Last Synced Info on KFONE Add Job Data screen before adding More Jobs : " + lastSyncedInfo1);
				throw new Exception("Last Synced Info on KFONE Add Job Data screen is NOT UPDATED after adding More Jobs...Please Investigate!!!");
			}
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_last_synced_info_on_add_job_data_screen_after_adding_more_jobs", e);
			LOGGER.error(" Issue in verifying Last Synced Info after adding - Method: verify_last_synced_info_after_adding", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog(" Issue in verifying Last Synced Info on KFONE Add Job Data screen...Please Investigate!!!");
			Assert.fail("Issue in verifying Last Synced Info on KFONE Add Job Data screen...Please Investigate!!!");
		}
	}
	
	public void close_add_job_data_screen() {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(addMoreJobsCloseBtn)).click();
			LOGGER.info("Clicked on Add more jobs Close button(X)");
			ExtentCucumberAdapter.addTestStepLog("Clicked on Add more jobs Close button(X)");
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("close_add_job_data_screen", e);
			LOGGER.error("Issue in closing Add more jobs page - Method: close_add_job_data_screen", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in Closing Add more jobs page...Please Investigate!!!");
			Assert.fail("Issue in Closing Add more jobs page...Please Investigate!!!");
		}
	}
	
	public void verify_unpublished_jobs_count_after_adding_more_jobs() {
		try {
			LOGGER.info("Waiting for 2 Minutes before validating uploaded jobs count in Job Mapping page.....");
			Thread.sleep(120000);
			LOGGER.info("Completed 2 Minutes Waiting Period in Job Mapping page.....");
			driver.navigate().refresh();
			LOGGER.info("Refreshed Job Mapping page....");
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForPageReady(driver, 3);
			String resultsCountText = wait.until(ExpectedConditions.visibilityOf(showingJobResultsCount)).getText();
			String ResultsCountAfterAddingMoreJobs = resultsCountText.split(" ")[3];
			LOGGER.info("Unpublished Job Profiles Count in Job Mapping page after Adding More Jobs : " + ResultsCountAfterAddingMoreJobs);
			ExtentCucumberAdapter.addTestStepLog("Unpublished Job Profiles Count in Job Mapping page after Adding More Jobs : " + ResultsCountAfterAddingMoreJobs);
			if(!(ResultsCountBeforeAddingMoreJobs.equals(ResultsCountAfterAddingMoreJobs))) {
				LOGGER.info("Unpublished Jobs count is UPDATED as Expected in Job Mapping page after Adding More Jobs....");
				ExtentCucumberAdapter.addTestStepLog("Unpublished Jobs count is UPDATED as Expected in Job Mapping page after Adding More Jobs....");
			} else {
				throw new Exception("Unpublished Jobs count is NOT UPDATED in Job Mapping page after adding More Jobs...Please Investigate!!!");
//				Assert.fail("Unpublished Job Profiles Count in Job Mapping page before Adding More Jobs : " + ResultsCountBeforeAddingMoreJobs);
			}
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_unpublished_jobs_count_after_adding_more_jobs", e);
			LOGGER.error("Unpublished Jobs count is NOT UPDATED in Job Mapping page after adding More Jobs - Method: verify_unpublished_job_profiles_count_after_adding", e);
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying Unpublished job profiles count in Job Mapping page after adding more jobs...Please Investigate!!!");
			Assert.fail("Unpublished Jobs count is NOT UPDATED in Job Mapping page after adding More Jobs...Please Investigate!!!");
		}
	}
	
	public void click_on_done_button_in_kfone_add_job_data_page() {
		try {
			// Get text before clicking to avoid stale element issues
			WebElement doneBtnElement = wait.until(ExpectedConditions.visibilityOf(doneBtn));
			String buttonText = doneBtnElement.getText();
			
			Assert.assertTrue(doneBtnElement.isDisplayed());
			wait.until(ExpectedConditions.elementToBeClickable(doneBtn)).click();
			
			LOGGER.info(buttonText + " button is displaying on KFONE Add Data screen as expected and clicked on Button...");
			ExtentCucumberAdapter.addTestStepLog(buttonText +  " button is displaying on KFONE Add Data screen as expected and clicked on Button...");
		} catch (StaleElementReferenceException e) {
			try {
				// Re-locate the element using By locator instead of cached reference
				WebElement doneBtnElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("//button[@id='btnDone']")));
				String buttonText = doneBtnElement.getText();
				
				Assert.assertTrue(doneBtnElement.isDisplayed());
				wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("//button[@id='btnDone']"))).click();
				
				LOGGER.info(buttonText + " button is displaying on KFONE Add Data screen as expected and clicked on Button...");
				ExtentCucumberAdapter.addTestStepLog(buttonText +  " button is displaying on KFONE Add Data screen as expected and clicked on Button...");
			} catch(Exception s) {
				ScreenshotHandler.captureFailureScreenshot("click_on_done_button_in_kfone_add_job_data_page", s);
				LOGGER.error("Issue in clicking Done Button - Method: click_on_done_button_in_kfone_add_job_data_page", s);
				s.printStackTrace();
				ExtentCucumberAdapter.addTestStepLog("Issue in Clicking Done Button on KFONE Add Job Data screen...Please Investigate!!!");
				Assert.fail("Issue in Clicking Done Button on KFONE Add Job Data screen...Please Investigate!!!");
			}
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_on_done_button_in_kfone_add_job_data_page", e);
			LOGGER.error("Issue in clicking Done Button - Method: click_on_done_button_in_kfone_add_job_data_page", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in Clicking Done Button on KFONE Add Job Data screen...Please Investigate!!!");
			Assert.fail("Issue in Clicking Done Button on KFONE Add Job Data screen...Please Investigate!!!");
		}
	}
	
	public void user_is_in_kfone_add_job_data_page_afer_uploading_file() {
		LOGGER.info("User is in KFONE Add Job Data page after uploading file");
		ExtentCucumberAdapter.addTestStepLog("User is in KFONE Add Job Data page after uploading file");
	}
	
}

