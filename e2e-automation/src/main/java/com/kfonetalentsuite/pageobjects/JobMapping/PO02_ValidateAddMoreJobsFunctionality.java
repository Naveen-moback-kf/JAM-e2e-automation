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
import com.kfonetalentsuite.utils.PageObjectHelper;
import com.kfonetalentsuite.webdriverManager.DriverManager;
import org.openqa.selenium.interactions.Actions;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

public class PO02_ValidateAddMoreJobsFunctionality {
	WebDriver driver = DriverManager.getDriver();

	protected static final Logger LOGGER = (Logger) LogManager.getLogger();
	PO02_ValidateAddMoreJobsFunctionality validateAddMoreJobsFunctionality;

	// THREAD-SAFE: Each thread gets its own isolated state for parallel execution
	public static ThreadLocal<String> lastSyncedInfo1 = ThreadLocal.withInitial(() -> null);
	public static ThreadLocal<String> ResultsCountBeforeAddingMoreJobs = ThreadLocal.withInitial(() -> null);
	public static ThreadLocal<String> KFONEjobsCountBeforeAddingMoreJobs = ThreadLocal.withInitial(() -> null);

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
				return true;
			}
		} catch (Exception e) {
			// Strategy failed, try next
		}

		try {
			// Strategy 2: JavaScript click
			js.executeScript("arguments[0].click();", element);
			// PERFORMANCE: Replaced Thread.sleep(1000) with UI stability wait
			PerformanceUtils.waitForUIStability(driver, 2);
			return true;
		} catch (Exception e) {
			// Strategy failed, try next
		}

		try {
			// Strategy 3: Actions click
			new Actions(driver).moveToElement(element).click().perform();
			// PERFORMANCE: Replaced Thread.sleep(1000) with UI stability wait
			PerformanceUtils.waitForUIStability(driver, 2);
			return true;
		} catch (Exception e) {
			// All strategies failed
		}

		return false;
	}

	// XPATHs
	@FindBy(xpath = "//div[@data-testid='loader']//img")
	@CacheLookup
	WebElement pageLoadSpinner2;

	@FindBy(xpath = "//div[contains(@id,'results-toggle')]//*[contains(text(),'Showing')]")
	@CacheLookup
	public WebElement showingJobResultsCount;

	@FindBy(xpath = "//*[contains(text(),'Add Job Data')]")
	// @CacheLookup removed - element becomes stale after page refresh
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

	// METHODs
	public void verify_unpublished_jobs_count_before_adding_more_jobs() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForPageReady(driver);

			try {
				String resultsCountText = wait.until(ExpectedConditions.visibilityOf(showingJobResultsCount)).getText();
				ResultsCountBeforeAddingMoreJobs.set(resultsCountText.split(" ")[3]);
				PageObjectHelper.log(LOGGER, "Unpublished Job Profiles Count before Adding More Jobs: "
						+ ResultsCountBeforeAddingMoreJobs.get());
			} catch (Exception e) {
				try {
					WebElement nothingToSeeElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
							"//span[contains(@class,'font-proxima') and contains(@class,'text-[24px]') and contains(@class,'font-semibold') and contains(text(),'Nothing to see here... yet!')]")));

					if (nothingToSeeElement.isDisplayed()) {
						ResultsCountBeforeAddingMoreJobs.set("0");
						PageObjectHelper.log(LOGGER,
								"No unpublished jobs found - Count set to 0 (Empty state message displayed)");
					} else {
						throw new Exception("Neither results count nor empty state message found");
					}
				} catch (Exception emptyStateException) {
					throw e;
				}
			}
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_unpublished_jobs_count_before_adding_more_jobs", e);
			PageObjectHelper.handleError(LOGGER, "verify_unpublished_jobs_count_before_adding_more_jobs",
					"Issue in verifying Unpublished job profiles count", e);
		}
	}

	public void user_should_be_landed_on_kfone_add_job_data_page() {
		try {
			String headerText = PageObjectHelper.retryOnStaleElement(LOGGER, () -> {
				WebElement header = wait.until(ExpectedConditions.visibilityOf(addMoreJobsPageHeader));
				return header.getText();
			});

			Assert.assertEquals(headerText, "Add Job Data");
			PageObjectHelper.log(LOGGER, "User landed on KFONE Add Job Data page Successfully");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_be_landed_on_kfone_add_job_data_page", e);
			PageObjectHelper.handleError(LOGGER, "user_should_be_landed_on_kfone_add_job_data_page",
					"Issue in landing KFONE Add Job Data page", e);
		}
	}

	public void user_is_in_kfone_add_job_data_page() {
		try {
			WebElement header = wait.until(ExpectedConditions.visibilityOf(addMoreJobsPageHeader));
			Assert.assertTrue(header.isDisplayed());
			Assert.assertEquals(header.getText(), "Add Job Data");
			PageObjectHelper.log(LOGGER, "User is in KFONE Add Job Data page - Page header verified");

			// Handle cookies banner if present
			try {
				WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
				WebElement cookiesBtn = shortWait.until(ExpectedConditions.elementToBeClickable(acceptAllCookies));

				if (!tryClickElement(cookiesBtn, "cookies accept button")) {
					js.executeScript("arguments[0].click();", acceptAllCookies);
				}
				PageObjectHelper.log(LOGGER, "Closed Cookies Banner");
			} catch (Exception e) {
				PageObjectHelper.log(LOGGER, "Cookies Banner already accepted or not present");
			}

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_is_in_kfone_add_job_data_page", e);
			PageObjectHelper.handleError(LOGGER, "user_is_in_kfone_add_job_data_page",
					"Issue in validating KFONE Add Job Data page", e);
		}
	}

	public void user_should_click_on_manual_upload_button() {
		try {
			String buttonText = PageObjectHelper.retryOnStaleElement(LOGGER, () -> {
				WebElement element = wait.until(ExpectedConditions.visibilityOf(manualUploadBtn));
				return element.getText();
			});

			wait.until(ExpectedConditions.elementToBeClickable(manualUploadBtn)).click();
			PageObjectHelper.log(LOGGER, buttonText + " button clicked successfully");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_click_on_manual_upload_button", e);
			PageObjectHelper.handleError(LOGGER, "user_should_click_on_manual_upload_button",
					"Issue in clicking Manual upload Button", e);
		}
	}

	// REMOVED: Unused method - corresponding step is commented out in feature file

	public void verify_jobs_count_in_kfone_add_job_data_screen_before_adding_more_jobs() {
		try {
			PerformanceUtils.waitForPageReady(driver, 2);
			KFONEjobsCountBeforeAddingMoreJobs
					.set(wait.until(ExpectedConditions.visibilityOf(KFONEjobsCount)).getText());
			PageObjectHelper.log(LOGGER,
					"Jobs count before Adding More Jobs: " + KFONEjobsCountBeforeAddingMoreJobs.get());
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot(
					"verify_jobs_count_in_kfone_add_job_data_screen_before_adding_more_jobs", e);
			PageObjectHelper.handleError(LOGGER,
					"verify_jobs_count_in_kfone_add_job_data_screen_before_adding_more_jobs",
					"Issue in verifying Jobs count before adding", e);
		}
	}

	public void verify_last_synced_info_on_add_job_data_screen_before_adding_more_jobs() {
		try {
			js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
			PerformanceUtils.waitForUIStability(driver, 2);
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(lastsyncedInfo)).isDisplayed());
			String lastSyncedInfoText1 = lastsyncedInfo.getText();
			lastSyncedInfo1.set(lastSyncedInfoText1);
			PageObjectHelper.log(LOGGER, "Last Synced Info before adding More Jobs: " + lastSyncedInfoText1);
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot(
					"verify_last_synced_info_on_add_job_data_screen_before_adding_more_jobs", e);
			PageObjectHelper.handleError(LOGGER,
					"verify_last_synced_info_on_add_job_data_screen_before_adding_more_jobs",
					"Issue in verifying Last Synced Info before adding", e);
		}
	}

	public void upload_job_catalog_file_using_browse_files_button() {
		String uploadFilePath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
				+ File.separator + "resources" + File.separator + "Job Catalog with 100 profiles.csv";

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

			// Strategy 2: If direct approach fails, try clicking upload button and finding
			// input
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
			String uploadedFileNameText = wait.until(ExpectedConditions.visibilityOf(attachedFileName)).getText();
			PageObjectHelper.log(LOGGER, "Job catalog file uploaded successfully. File name: " + uploadedFileNameText);

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("upload_job_catalog_file_using_attach_file_button", e);
			PageObjectHelper.handleError(LOGGER, "upload_job_catalog_file_using_attach_file_button",
					"Issue in uploading Job Catalog file: " + e.getMessage(), e);
		}
	}

	/**
	 * Strategy 1: Try to find file input element directly without clicking upload
	 * button
	 */
	private boolean tryDirectFileInput(String uploadFilePath) {
		try {
			String[] inputSelectors = { "//input[@type='file']", "//input[contains(@id,'upload')]",
					"//input[contains(@class,'upload')]", "//input[contains(@name,'file')]",
					"//input[contains(@accept,'.csv')]" };

			for (String selector : inputSelectors) {
				try {
					List<WebElement> fileInputs = driver.findElements(By.xpath(selector));
					for (WebElement input : fileInputs) {
						if (input.isEnabled()) {
							input.sendKeys(uploadFilePath);
							PerformanceUtils.waitForUIStability(driver, 2);
							return true;
						}
					}
				} catch (Exception e) {
					continue;
				}
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Strategy 2: Click upload button and then find the file input element
	 */
	private boolean tryClickAndFindInput(String uploadFilePath) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(browseFilesBtn)).click();
			PageObjectHelper.log(LOGGER, "Clicked Browse Files button");
			PerformanceUtils.waitForUIStability(driver, 1);

			String[] inputSelectors = { "//input[@type='file']",
					"//input[contains(@style,'opacity: 0') or contains(@style,'display: none')][@type='file']",
					"//input[contains(@id,'upload')]", "//input[contains(@class,'upload')]",
					"//input[contains(@name,'file')]" };

			for (String selector : inputSelectors) {
				try {
					WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
					List<WebElement> fileInputs = shortWait
							.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(selector)));

					for (WebElement input : fileInputs) {
						try {
							js.executeScript(
									"arguments[0].style.display = 'block'; arguments[0].style.visibility = 'visible'; arguments[0].style.opacity = '1';",
									input);
							input.sendKeys(uploadFilePath);
							PerformanceUtils.waitForUIStability(driver, 2);
							return true;
						} catch (Exception e) {
							continue;
						}
					}
				} catch (Exception e) {
					continue;
				}
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Strategy 3: Use JavaScript to handle file upload
	 */
	private boolean tryJavaScriptUpload(String uploadFilePath) {
		try {
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
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Strategy 4: Fallback to Robot class for non-headless environments
	 */
	private boolean tryRobotClassUpload(String uploadFilePath) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(browseFilesBtn)).click();

			Robot rb = new Robot();
			rb.delay(2000);

			StringSelection ss = new StringSelection(uploadFilePath);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);

			rb.keyPress(KeyEvent.VK_CONTROL);
			rb.keyPress(KeyEvent.VK_V);
			rb.delay(2000);

			rb.keyRelease(KeyEvent.VK_CONTROL);
			rb.keyRelease(KeyEvent.VK_V);
			rb.delay(2000);

			rb.keyPress(KeyEvent.VK_ENTER);
			rb.keyRelease(KeyEvent.VK_ENTER);
			rb.delay(2000);

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Check if browser is running in headless mode
	 */
	private boolean isHeadlessMode() {
		try {
			return js.executeScript("return navigator.webdriver === true").toString().equals("true")
					|| System.getProperty("java.awt.headless", "false").equals("true")
					|| System.getProperty("webdriver.chrome.args", "").contains("--headless")
					|| System.getProperty("webdriver.firefox.args", "").contains("--headless");
		} catch (Exception e) {
			return true;
		}
	}

	public void user_should_verify_file_close_button_displaying_and_clickable() {
		try {
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(fileCloseBtn)).isDisplayed());
			wait.until(ExpectedConditions.visibilityOf(fileCloseBtn)).click();
			PageObjectHelper.log(LOGGER, "File Close button clicked successfully");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_verify_file_close_button_displaying_and_clickable",
					e);
			PageObjectHelper.handleError(LOGGER, "user_should_verify_file_close_button_displaying_and_clickable",
					"Issue in verifying File Close Button", e);
		}
	}

	public void click_on_continue_button_in_add_job_data_screen() {
		try {
			String buttonText = PageObjectHelper.retryOnStaleElement(LOGGER, () -> {
				WebElement element = wait.until(ExpectedConditions.visibilityOf(continueBtn));
				return element.getText();
			});

			wait.until(ExpectedConditions.elementToBeClickable(continueBtn)).click();
			PageObjectHelper.log(LOGGER, buttonText + " button clicked successfully");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_on_continue_button_in_add_job_data_screen", e);
			PageObjectHelper.handleError(LOGGER, "click_on_continue_button_in_add_job_data_screen",
					"Issue in clicking Continue Button", e);
		}
	}

	public void user_should_validate_job_data_upload_is_in_progress() {
		try {
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(uploadProgressText)).isDisplayed());
			PageObjectHelper.log(LOGGER, "Upload in progress - " + uploadProgressText.getText());
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_validate_job_data_upload_is_in_progress", e);
			PageObjectHelper.handleError(LOGGER, "user_should_validate_job_data_upload_is_in_progress",
					"Issue in validating Upload in Progress", e);
		}
	}

	public void user_should_validate_job_data_added_successfully() {
		try {
			PageObjectHelper.log(LOGGER, "Waiting for 2 minutes before refreshing page...");
			Thread.sleep(120000);

			wait.until(ExpectedConditions.elementToBeClickable(clickHereBtn)).click();
			PageObjectHelper.log(LOGGER, "Clicked on Click Here button to refresh the page");

			PerformanceUtils.waitForUIStability(driver, 2);
			PageObjectHelper.log(LOGGER, uploadSuccessMessage.getText() + " - Job data added successfully");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_validate_job_data_added_successfully", e);
			PageObjectHelper.handleError(LOGGER, "user_should_validate_job_data_added_successfully",
					"Issue in validating Job data added successfully", e);
		}
	}

	public void verify_jobs_count_in_kfone_add_job_data_screen_after_adding_more_jobs() {
		try {
			PerformanceUtils.waitForUIStability(driver, 2);
			js.executeScript("window.scrollTo(document.body.scrollHeight, 0)");
			String KFONEjobsCountAfterAddingMoreJobs = wait.until(ExpectedConditions.visibilityOf(KFONEjobsCount))
					.getText();
			PageObjectHelper.log(LOGGER, "Jobs count after Adding More Jobs: " + KFONEjobsCountAfterAddingMoreJobs);

			if (!KFONEjobsCountBeforeAddingMoreJobs.get().equals(KFONEjobsCountAfterAddingMoreJobs)) {
				PageObjectHelper.log(LOGGER, "KFONE Jobs count UPDATED as expected");
			} else {
				throw new Exception("KFONE Jobs count NOT UPDATED after adding More Jobs (Before: "
						+ KFONEjobsCountBeforeAddingMoreJobs.get() + ", After: " + KFONEjobsCountAfterAddingMoreJobs
						+ ")");
			}
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot(
					"verify_jobs_count_in_kfone_add_job_data_screen_after_adding_more_jobs", e);
			PageObjectHelper.handleError(LOGGER,
					"verify_jobs_count_in_kfone_add_job_data_screen_after_adding_more_jobs",
					"Issue in verifying Jobs count after adding", e);
		}
	}

	public void verify_last_synced_info_on_add_job_data_screen_after_adding_more_jobs() {
		try {
			PerformanceUtils.waitForUIStability(driver, 2);
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(lastsyncedInfo)).isDisplayed());
			String lastSyncedInfoText2 = lastsyncedInfo.getText();
			PageObjectHelper.log(LOGGER, "Last Synced Info after adding More Jobs: " + lastSyncedInfoText2);

			if (!lastSyncedInfo1.get().equals(lastSyncedInfoText2)) {
				PageObjectHelper.log(LOGGER, "Last Synced Info UPDATED as expected");
			} else {
				throw new Exception("Last Synced Info NOT UPDATED after adding More Jobs (Before: "
						+ lastSyncedInfo1.get() + ", After: " + lastSyncedInfoText2 + ")");
			}
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot(
					"verify_last_synced_info_on_add_job_data_screen_after_adding_more_jobs", e);
			PageObjectHelper.handleError(LOGGER,
					"verify_last_synced_info_on_add_job_data_screen_after_adding_more_jobs",
					"Issue in verifying Last Synced Info after adding", e);
		}
	}

	public void close_add_job_data_screen() {
		try {
			PageObjectHelper.retryOnStaleElement(LOGGER, () -> {
				WebElement closeBtn = wait.until(ExpectedConditions.elementToBeClickable(addMoreJobsCloseBtn));
				closeBtn.click();
			});

			PageObjectHelper.log(LOGGER, "Clicked on Add more jobs Close button");
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("close_add_job_data_screen", e);
			PageObjectHelper.handleError(LOGGER, "close_add_job_data_screen", "Issue in closing Add more jobs page", e);
		}
	}

	public void verify_unpublished_jobs_count_after_adding_more_jobs() {
		try {
			PageObjectHelper.log(LOGGER, "Waiting for 2 minutes before validating uploaded jobs count...");
			Thread.sleep(120000);

			driver.navigate().refresh();
			PageObjectHelper.log(LOGGER, "Refreshed Job Mapping page");

			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForPageReady(driver, 3);

			String resultsCountText = wait.until(ExpectedConditions.visibilityOf(showingJobResultsCount)).getText();
			String ResultsCountAfterAddingMoreJobs = resultsCountText.split(" ")[3];
			PageObjectHelper.log(LOGGER,
					"Unpublished Job Profiles Count after Adding More Jobs: " + ResultsCountAfterAddingMoreJobs);

			if (!ResultsCountBeforeAddingMoreJobs.get().equals(ResultsCountAfterAddingMoreJobs)) {
				PageObjectHelper.log(LOGGER, "Unpublished Jobs count UPDATED as expected");
			} else {
				throw new Exception("Unpublished Jobs count NOT UPDATED (Before: "
						+ ResultsCountBeforeAddingMoreJobs.get() + ", After: " + ResultsCountAfterAddingMoreJobs + ")");
			}
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_unpublished_jobs_count_after_adding_more_jobs", e);
			PageObjectHelper.handleError(LOGGER, "verify_unpublished_jobs_count_after_adding_more_jobs",
					"Issue in verifying Unpublished job profiles count after adding", e);
		}
	}

	public void click_on_done_button_in_kfone_add_job_data_page() {
		try {
			String buttonText = PageObjectHelper.retryOnStaleElement(LOGGER, () -> {
				WebElement element = wait.until(ExpectedConditions.visibilityOf(doneBtn));
				return element.getText();
			});

			wait.until(ExpectedConditions.elementToBeClickable(doneBtn)).click();
			PageObjectHelper.log(LOGGER, buttonText + " button clicked successfully");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_on_done_button_in_kfone_add_job_data_page", e);
			PageObjectHelper.handleError(LOGGER, "click_on_done_button_in_kfone_add_job_data_page",
					"Issue in clicking Done Button", e);
		}
	}

	public void user_is_in_kfone_add_job_data_page_afer_uploading_file() {
		PageObjectHelper.log(LOGGER, "User is in KFONE Add Job Data page after uploading file");
	}

}