package com.kfonetalentsuite.pageobjects.JobMapping;

import java.io.IOException;
import java.time.Duration;
import java.util.NoSuchElementException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.kfonetalentsuite.utils.JobMapping.SmartWaits;
import com.kfonetalentsuite.utils.JobMapping.Utilities;
import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.ScreenshotHandler;
import com.kfonetalentsuite.utils.common.CommonVariable;
import com.kfonetalentsuite.webdriverManager.DriverManager;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

public class PO01_KFoneLogin {
	WebDriver driver;
	
	protected static final Logger LOGGER = (Logger) LogManager.getLogger();
	PO01_KFoneLogin kfoneLogin;
    
	// THREAD-SAFE: Each thread gets its own isolated state for parallel execution
    public static ThreadLocal<String> username = ThreadLocal.withInitial(() -> null);
    public static ThreadLocal<String> clientName = ThreadLocal.withInitial(() -> "");

	
	public PO01_KFoneLogin() throws IOException {
		// Initialize driver in constructor to ensure it's available
		this.driver = DriverManager.getDriver();
		if (this.driver == null) {
			LOGGER.error("Driver is null in PO01_KFoneLogin constructor - attempting to initialize");
			// Try to initialize driver if it's null
			DriverManager.launchBrowser();
			this.driver = DriverManager.getDriver();
		}
		
		if (this.driver == null) {
			throw new RuntimeException("Failed to initialize WebDriver in PO01_KFoneLogin");
		}
		
		PageFactory.initElements(driver, this);
		
		// Initialize other driver-dependent fields
		this.wait = DriverManager.getWait();
		this.js = (JavascriptExecutor) driver;
	}

	WebDriverWait wait;
	Utilities utils = new Utilities();
	JavascriptExecutor js;
	
	// XPATHs
	@FindBy(xpath = "//div[@id='kf-loader']//*")
	@CacheLookup
	WebElement pageLoadSpinner;
	
	@FindBy(xpath = "//input[@type='email']")
	@CacheLookup
	WebElement userNameTxt;

	@FindBy(xpath = "//input[@type='password']")
	@CacheLookup
	WebElement passwordTxt;
	
	@FindBy(xpath = "//button[@id='submit-button']")
	@CacheLookup
	WebElement kfoneSigninBtn;
	
	@FindBy(xpath = "//*[text()='Proceed']")
	@CacheLookup
	WebElement proceedBtn;
	
	@FindBy(xpath = "//img[@alt='Microsoft']")
	@CacheLookup
	WebElement MicrosoftLogo;

	@FindBy(xpath = "//input[@type='submit']")
	@CacheLookup
	WebElement MicrosoftNextBtn;
	
	@FindBy(xpath = "//div[text()='Enter password']")
	@CacheLookup
	WebElement MicrosoftPasswordPageHeader;
	
	@FindBy(xpath = "//input[@type='password']")
	@CacheLookup
	WebElement MicrosoftPasswordTxt;
	
	@FindBy(xpath = "//input[@type='submit']")
	@CacheLookup
	WebElement MicrosoftSignInBtn;
	
	@FindBy(xpath = "//input[@type='submit']")
	@CacheLookup
	WebElement MicrosoftYesBtn;
	
	@FindBy(xpath = "//button[@id='ensAcceptAll']")
	@CacheLookup
	WebElement acceptAllCookies;
	
	@FindBy(xpath = "//a[@id='Profile Manager']")
	@CacheLookup
	WebElement PMappTileInKFONE;
	
	@FindBy(xpath = "//h1[contains(text(),'Profile Manager')]")
	@CacheLookup
	public WebElement PMHeader;
	
	@FindBy(xpath = "//*[@id='title-svg-icon']")
	@CacheLookup
	public WebElement KFONE_landingPage_title;
	
	@FindBy(xpath = "//*[@data-testid='clients']//h2")
	@CacheLookup
	public WebElement KFONE_clientsPage_header;
	
	By KfoneClientsPageTitle = By.xpath("//h2[text()='Clients']");
	
	// WebElements for Clients page functionality
	@FindBy(xpath = "//table[@id='iam-clients-list-table-content']")
	@CacheLookup
	WebElement clientsTable;
	
	@FindBy(xpath = "//tbody[@class='table-body']")
	@CacheLookup
	WebElement clientsTableBody;
	
	@FindBy(xpath = "//td[@data-testid='iam-clients-list-clientProducts-table-data-cell-0']")
	@CacheLookup
	WebElement firstClientProducts;
	
	@FindBy(xpath = "//a[contains(@data-testid,'client-') and contains(text(),'KF_Dev_Test_All_Products')]")
	@CacheLookup
	WebElement kfDevTestAllProductsClient;
	
	@FindBy(xpath = "//a[contains(@data-testid,'client-') and contains(text(),'KF One Assess Select Client 1')]")
	@CacheLookup
	WebElement kfOneAssessSelectClient1;
	
	@FindBy(xpath = "//input[@id='search-client-input-search']")
	@CacheLookup
	WebElement clientSearchBar;
	
	@FindBy(xpath = "//h1[contains(text(),'Hi,')]")
	@CacheLookup
	WebElement KFONEHomePageHeader;
	
	@FindBy(xpath = "//h2[contains(text(),'Your products')]")
	@CacheLookup
	WebElement yourProductsSection;
	
	@FindBy(xpath = "//div[@data-testid='Profile Manager']")
	@CacheLookup
	WebElement profileManagerInProductsSection;
	
	
	
	public void launch_the_kfone_application() {
		try {
			// Additional safety check
			if (this.driver == null) {
				LOGGER.error("Driver is null in launch_the_kfone_application - attempting to reinitialize");
				this.driver = DriverManager.getDriver();
				if (this.driver == null) {
					DriverManager.launchBrowser();
					this.driver = DriverManager.getDriver();
				}
			}
			
			if (this.driver == null) {
				throw new RuntimeException("Cannot launch KFONE application - WebDriver is null");
			}
			
			switch (CommonVariable.ENVIRONMENT) {
			case "Stage":
				driver.get(CommonVariable.KFONE_STAGEURL);
				LOGGER.info("Successfully Launched KFONE " + CommonVariable.ENVIRONMENT + " Environment URL " + CommonVariable.KFONE_STAGEURL);
				ExtentCucumberAdapter.addTestStepLog("Verifying the Smoke Test in the "+CommonVariable.ENVIRONMENT);
				ExtentCucumberAdapter.addTestStepLog("The URL of the Environment is "+CommonVariable.KFONE_STAGEURL);
				break;

			case "ProdEU":
				driver.get(CommonVariable.KFONE_PRODEUURL);	
				LOGGER.info("Successfully Launched KFONE "+ CommonVariable.ENVIRONMENT + " Environment URL " + CommonVariable.KFONE_PRODEUURL);
				ExtentCucumberAdapter.addTestStepLog("Verifying the Smoke Test in the "+CommonVariable.ENVIRONMENT);
				ExtentCucumberAdapter.addTestStepLog("The URL of the Environment is "+CommonVariable.KFONE_PRODEUURL);
				break;
			case "ProdUS":
				driver.get(CommonVariable.KFONE_PRODUSURL);
				LOGGER.info("Successfully Launched KFONE "+ CommonVariable.ENVIRONMENT + " Environment URL " + CommonVariable.KFONE_PRODUSURL);
				ExtentCucumberAdapter.addTestStepLog("Verifying the Smoke Test in the "+CommonVariable.ENVIRONMENT);
				ExtentCucumberAdapter.addTestStepLog("The URL of the Environment is "+CommonVariable.KFONE_PRODUSURL);
				break;
				
			case "Test":
				driver.get(CommonVariable.KFONE_QAURL);	
				LOGGER.info("Successfully Launched KFONE " + CommonVariable.ENVIRONMENT + " Environment URL " +CommonVariable.KFONE_QAURL);
				ExtentCucumberAdapter.addTestStepLog("Verifying the Smoke Test in the "+CommonVariable.ENVIRONMENT);
				ExtentCucumberAdapter.addTestStepLog("The URL of the Environment is "+CommonVariable.KFONE_QAURL);
				break;
				
			case "Dev":
				driver.get(CommonVariable.KFONE_DEVURL);	
				LOGGER.info("Successfully Launched KFONE " + CommonVariable.ENVIRONMENT + " Environment URL " +CommonVariable.KFONE_DEVURL);
				ExtentCucumberAdapter.addTestStepLog("Verifying the Smoke Test in the "+CommonVariable.ENVIRONMENT);
				ExtentCucumberAdapter.addTestStepLog("The URL of the Environment is "+CommonVariable.KFONE_DEVURL);
				break;
			

			default:
				driver.get(CommonVariable.KFONE_QAURL);	
				LOGGER.info("Successfully Launched KFONE " + CommonVariable.ENVIRONMENT + " Environment URL " +CommonVariable.KFONE_QAURL);
				ExtentCucumberAdapter.addTestStepLog("LANDED IN DEFAULT URL");
				ExtentCucumberAdapter.addTestStepLog("The URL of the Environment is "+CommonVariable.KFONE_QAURL);
			}
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("launch_the_kfone_application", e);
			LOGGER.error("Issue in launching KFONE application in environment: {} - Method: launch_the_kfone_application", CommonVariable.ENVIRONMENT, e);
			ExtentCucumberAdapter.addTestStepLog("Issue in launching KFONE application in environment: " + CommonVariable.ENVIRONMENT);
			Assert.fail("Issue in launching KFONE application in environment: " + CommonVariable.ENVIRONMENT);
		}
	}
	
	public void provide_sso_login_username_and_click_sign_in_button_in_kfone_login_page() {
		try {
			// OPTIMIZED: Handle cookies banner with short timeout
			handleCookiesBanner();
			wait.until(ExpectedConditions.elementToBeClickable(userNameTxt)).sendKeys(CommonVariable.SSO_USERNAME);
			ExtentCucumberAdapter.addTestStepLog("Provided SSO Login Username as : " + CommonVariable.SSO_USERNAME + " in KFONE Login Page");
			LOGGER.info("Provided SSO Username as : " + CommonVariable.SSO_USERNAME + " in KFONE Login Page");
			username.set(CommonVariable.SSO_USERNAME);
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			utils.jsClick(driver, kfoneSigninBtn);
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			ExtentCucumberAdapter.addTestStepLog("Clicked on Sign in Button in KFONE Login Page");
			LOGGER.info("Clicked on Sign in Button in KFONE Login Page");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("provide_sso_login_username_and_click_sign_in_button_in_kfone_login_page", e);
			LOGGER.error("Issue in providing SSO login username - Method: provide_sso_login_username_and_click_sign_in_button_in_kfone_login_page", e);
			ExtentCucumberAdapter.addTestStepLog("Issue in providing SSO login username and clicking sign in button");
			Assert.fail("Issue in providing SSO login username and clicking sign in button");
		}
	}
	
	public void user_should_navigate_to_microsoft_login_page() {
		try {
			wait.until(ExpectedConditions.visibilityOf(MicrosoftPasswordPageHeader)).isDisplayed();
			LOGGER.info("User successfully navigated to Microsoft Login page");
			ExtentCucumberAdapter.addTestStepLog("User successfully navigated to Microsoft Login page");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_navigate_to_microsoft_login_page", e);
			LOGGER.error("Issue in navigating to Microsoft Login page - Method: user_should_navigate_to_microsoft_login_page", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in navigating to Microsoft Login page...Please Investigate!!!");
			Assert.fail("Issue in navigating to Microsoft Login page...Please Investigate!!!");
		}
	}
	
	public void provide_sso_login_password_and_click_sign_in() {
		try {
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(MicrosoftPasswordPageHeader)).isDisplayed());
			wait.until(ExpectedConditions.elementToBeClickable(MicrosoftPasswordTxt)).sendKeys(CommonVariable.SSO_PASSWORD);
			ExtentCucumberAdapter.addTestStepLog("Provided SSO Login Password in Microsoft Login Page");
			LOGGER.info("Provided SSO Login Password in Microsoft Login Page");
			utils.jsClick(driver, MicrosoftSignInBtn);
			ExtentCucumberAdapter.addTestStepLog("Clicked on Signin Button in Microsoft Login Page");
			LOGGER.info("Clicked on Signin Button in Microsoft Login Page");
			// Click Yes on Stay Signed in? 
			wait.until(ExpectedConditions.elementToBeClickable(MicrosoftYesBtn)).click();
			ExtentCucumberAdapter.addTestStepLog("Clicked on Yes Button in Microsoft Login Page");
			LOGGER.info("Clicked on Yes Button in Microsoft Login Page");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("provide_sso_login_password_and_click_sign_in", e);
			LOGGER.error("Issue in providing SSO login password - Method: provide_sso_login_password_and_click_sign_in", e);
			ExtentCucumberAdapter.addTestStepLog("Issue in providing SSO login password and clicking sign in");
			Assert.fail("Issue in providing SSO login password and clicking sign in");
		}
	}
	
	public void provide_non_sso_login_username_and_click_sign_in_button_in_kfone_login_page() {
		// OPTIMIZED: Handle cookies banner with short timeout
		handleCookiesBanner();
		
		wait.until(ExpectedConditions.elementToBeClickable(userNameTxt)).sendKeys(CommonVariable.NON_SSO_USERNAME);
		ExtentCucumberAdapter.addTestStepLog("Provided NON-SSO Login Username as : " + CommonVariable.NON_SSO_USERNAME + " in KFONE Login Page");
		LOGGER.info("Provided NON-SSO Username as : " + CommonVariable.NON_SSO_USERNAME + " in KFONE Login Page");
		username.set(CommonVariable.NON_SSO_USERNAME);
		wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
		utils.jsClick(driver, kfoneSigninBtn);
		wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
		ExtentCucumberAdapter.addTestStepLog("Clicked on Sign in Button in KFONE Login Page");
		LOGGER.info("Clicked on Sign in Button in KFONE Login Page");
	}
	
	public void provide_non_sso_login_password_and_click_sign_in_button_in_kfone_login_page() {
		wait.until(ExpectedConditions.elementToBeClickable(passwordTxt)).sendKeys(CommonVariable.NON_SSO_PASSWORD);
		ExtentCucumberAdapter.addTestStepLog("Provided NON-SSO Login Password in KFONE Login Page");
		LOGGER.info("Provided NON-SSO Login Password in KFONE Login Page");
		utils.jsClick(driver, kfoneSigninBtn);
		wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
		ExtentCucumberAdapter.addTestStepLog("Clicked on Sign in Button in KFONE Login Page");
		LOGGER.info("Clicked on Sign in Button in KFONE Login Page");
	}
	
	public void verify_the_kfone_landing_page()  {
		try {
			// Check for terms and conditions popup with shorter timeout
			WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
			try {
				shortWait.until(ExpectedConditions.elementToBeClickable(proceedBtn)).click();
				LOGGER.info("Accepted KFONE terms and conditions by clicking on Proceed button");
			} catch(NoSuchElementException | TimeoutException e) {
				LOGGER.info("User has already accepted KFONE terms and conditions");
			}
			
			// Wait for spinner to disappear and page to load
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			SmartWaits.waitForPageLoad(driver);
			
			// Verify landing page elements
			wait.until(ExpectedConditions.visibilityOf(KFONE_landingPage_title)).isDisplayed();
			wait.until(ExpectedConditions.visibilityOf(KFONE_clientsPage_header)).isDisplayed();
			String text1 = wait.until(ExpectedConditions.visibilityOf(KFONE_clientsPage_header)).getText();
			String obj1 = "Clients";
			Assert.assertEquals(obj1, text1);
			
			LOGGER.info("Landed on KFONE Clients Page as Expected");
			ExtentCucumberAdapter.addTestStepLog("Landed on KFONE Clients Page as Expected");
			ExtentCucumberAdapter.addTestStepLog("KFONE Landing Page Verification is Successful");
			LOGGER.info("KFONE Landing Page Verification is Successful");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_the_kfone_landing_page", e);
			LOGGER.error("Issue in verifying KFONE landing page after login - Method: verify_the_kfone_landing_page", e);
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying KFONE landing page after login");
			Assert.fail("Issue in verifying KFONE landing page after login");
		}

	}
	
	public void verify_user_seemlessly_landed_on_profile_manager_application_in_kf_hub() {
		try {
			LOGGER.info("Verifying navigation to Profile Manager page...");
			LOGGER.info("Current URL: " + driver.getCurrentUrl());
			
			// HEADLESS MODE OPTIMIZATION: Use shorter waits with multiple retries instead of one long wait
			int maxRetries = 3;
			boolean pmHeaderFound = false;
			
			for (int retry = 1; retry <= maxRetries && !pmHeaderFound; retry++) {
				try {
					LOGGER.info("Attempt " + retry + "/" + maxRetries + " - Waiting for PM page to load...");
					
					// Wait for spinners with shorter timeout (30 seconds max per attempt)
					WebDriverWait shortWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(30));
					try {
						shortWait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
						LOGGER.info("Page load spinner disappeared");
					} catch (Exception spinnerEx) {
						LOGGER.warn("Spinner wait timed out - continuing anyway (common in headless mode)");
					}
					
					// Wait for page to be ready
					PerformanceUtils.waitForPageReady(driver, 3);
					LOGGER.info("Page ready state achieved");
					
					// ENHANCED: Additional DOM stability check to prevent downstream stale elements
					Thread.sleep(800); // Allow JavaScript and AJAX to fully complete
					
					// Check if PM Header is visible with shorter wait
					if (shortWait.until(ExpectedConditions.visibilityOf(PMHeader)).isDisplayed()) {
						pmHeaderFound = true;
						String MainHeader = PMHeader.getText();
						LOGGER.info("✅ User Successfully landed on the " + MainHeader + " Dashboard Page");
						ExtentCucumberAdapter.addTestStepLog("User Successfully landed on the " + MainHeader + " Dashboard Page");
						LOGGER.info("Final URL: " + driver.getCurrentUrl());
						
						// Final stability check before proceeding to next steps
						Thread.sleep(500);
						LOGGER.debug("PM page fully stabilized - ready for next actions");
						break;
					}
					
				} catch (Exception retryEx) {
					LOGGER.warn("Attempt " + retry + " failed: " + retryEx.getMessage());
					
					if (retry < maxRetries) {
						LOGGER.info("Refreshing page and retrying...");
						driver.navigate().refresh();
						Thread.sleep(2000); // Brief pause before retry
					} else {
						// Last attempt failed - throw the exception
						throw retryEx;
					}
				}
			}
			
			if (!pmHeaderFound) {
				throw new RuntimeException("Profile Manager header not found after " + maxRetries + " attempts");
			}
			
		} catch (Exception e) {
			LOGGER.error("❌ FINAL FAILURE - Current URL: " + driver.getCurrentUrl());
			LOGGER.error("Page Source (first 500 chars): " + driver.getPageSource().substring(0, Math.min(500, driver.getPageSource().length())));
			ScreenshotHandler.captureFailureScreenshot("verify_user_seemlessly_landed_on_profile_manager_application_in_kf_hub", e);
			LOGGER.error("Issue with seamless navigation from KFONE to Profile Manager Application - Method: verify_user_seemlessly_landed_on_profile_manager_application_in_kf_hub", e);
			ExtentCucumberAdapter.addTestStepLog("Issue with seemless navigation from KFONE to Profile Manager Application in KF HUB...Please Investigate!!!");
			Assert.fail("Issue with seemless navigation from KFONE to Profile Manager Application in KF HUB...Please Investigate!!!");
		}
	}
	
	/**
	 * Optimized cookies banner handler with short timeout
	 * Handles cookies banner immediately after navigation to avoid long waits
	 */
	private void handleCookiesBanner() {
		try {
			// Use short timeout (5 seconds) for cookies banner - if not present, move on quickly
			WebDriverWait cookiesWait = new WebDriverWait(driver, Duration.ofSeconds(5));
			WebElement cookiesButton = cookiesWait.until(ExpectedConditions.elementToBeClickable(acceptAllCookies));
			
			// Try standard click first
			try {
				cookiesButton.click();
				LOGGER.info("Closed Cookies Banner by clicking on Accept All button");
				ExtentCucumberAdapter.addTestStepLog("Closed Cookies Banner by clicking on Accept All button");
				return;
			} catch (Exception e) {
				// If standard click fails, try JS click
				try {
					js.executeScript("arguments[0].click();", cookiesButton);
					LOGGER.info("Closed Cookies Banner using JavaScript click");
					ExtentCucumberAdapter.addTestStepLog("Closed Cookies Banner using JavaScript click");
					return;
				} catch (Exception jsError) {
					// Last resort: utils.jsClick
					utils.jsClick(driver, cookiesButton);
					LOGGER.info("Closed Cookies Banner using utils.jsClick");
					ExtentCucumberAdapter.addTestStepLog("Closed Cookies Banner using utils.jsClick");
				}
			}
		} catch (TimeoutException e) {
			// Cookies banner not present or already accepted - this is normal
			LOGGER.info("Cookies Banner is already accepted or not present");
			ExtentCucumberAdapter.addTestStepLog("Cookies Banner is already accepted");
		} catch (Exception e) {
			// Don't fail test if cookies handling fails - just log and continue
			LOGGER.warn("Could not handle cookies banner: " + e.getMessage());
		}
	}
	
	public void user_is_in_kfone_clients_page() {
		try {
			PerformanceUtils.waitForPageReady(driver, 2);
			wait.until(ExpectedConditions.visibilityOf(clientsTable)).isDisplayed();
			LOGGER.info("User is successfully on KFONE Clients page");
			ExtentCucumberAdapter.addTestStepLog("User is successfully on KFONE Clients page");
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_is_in_kfone_clients_page", e);
			LOGGER.error("Issue in verifying user is on KFONE Clients page - Method: user_is_in_kfone_clients_page", e);
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying user is on KFONE Clients page");
			Assert.fail("Issue in verifying user is on KFONE Clients page");
		}
	}
	
	public void verify_products_that_client_can_access() {
		try {
			String targetPamsId = CommonVariable.TARGET_PAMS_ID;
			
			wait.until(ExpectedConditions.visibilityOf(clientsTableBody)).isDisplayed();
			
			// Verify that the table contains client data
			Assert.assertTrue(clientsTableBody.isDisplayed(), "Clients table body is not displayed");
			
			// Get client rows (excluding header)
			java.util.List<WebElement> clientRows = driver.findElements(By.xpath("//tbody//tr[@class='table-row']"));
			Assert.assertTrue(clientRows.size() > 0, "No client rows found in the table");
			
			LOGGER.info("Found " + clientRows.size() + " client(s) in the table");
			ExtentCucumberAdapter.addTestStepLog("Found " + clientRows.size() + " client(s) in the table");
			
			// Determine verification scope based on PAMS ID configuration
			if (targetPamsId != null && !targetPamsId.trim().isEmpty()) {
				LOGGER.info("Verifying products for specific client with PAMS ID: " + targetPamsId);
				ExtentCucumberAdapter.addTestStepLog("Verifying products for specific client with PAMS ID: " + targetPamsId);
			} else {
				LOGGER.info("Verifying products for all available clients");
				ExtentCucumberAdapter.addTestStepLog("Verifying products for all available clients");
			}
			
		boolean targetClientFound = false;
		int verifiedClientCount = 0;
			
			// Iterate through each client row and verify products
			for (int i = 0; i < clientRows.size(); i++) {
				try {
					// Get client name - try both clickable link and non-clickable div
					String clientName = "";
					
					try {
						// First try to find clickable client link
						WebElement clientNameElement = clientRows.get(i).findElement(By.xpath(".//a[contains(@data-testid,'client-')]"));
						clientName = clientNameElement.getText();
					} catch (Exception e) {
						// If no clickable link, try to find non-clickable client name div
						WebElement clientNameElement = clientRows.get(i).findElement(By.xpath(".//div[contains(@class,'data-content-container')]//div[contains(@title, '')]"));
						clientName = clientNameElement.getAttribute("title");
					}
					
					// Get PAMS ID for this client
					WebElement pamsIdElement = clientRows.get(i).findElement(By.xpath(".//td[contains(@data-testid,'iam-clients-list-pams_id-table-data-cell-')]"));
					String pamsId = pamsIdElement.getText().trim();
					
					// Get products for this client
					WebElement productsElement = clientRows.get(i).findElement(By.xpath(".//td[contains(@data-testid,'iam-clients-list-clientProducts-table-data-cell-')]"));
					String productsText = productsElement.getText();
					
					// Check if we should verify this client based on PAMS ID configuration
					boolean shouldVerifyThisClient = false;
					if (targetPamsId != null && !targetPamsId.trim().isEmpty()) {
						// Verify only the specific client with target PAMS ID
						shouldVerifyThisClient = pamsId.equals(targetPamsId);
						if (shouldVerifyThisClient) {
							targetClientFound = true;
						}
					} else {
						// Verify all clients
						shouldVerifyThisClient = true;
					}
					
					if (shouldVerifyThisClient) {
						verifiedClientCount++;
						
						// Log detailed information for this client
						LOGGER.info("=== CLIENT PRODUCT ACCESS DETAILS ===");
						LOGGER.info("Client Name: " + clientName);
						LOGGER.info("PAMS ID: " + pamsId);
						
						// Split products by comma and log each product individually
						if (!productsText.isEmpty()) {
							String[] products = productsText.split(",");
							LOGGER.info("Below are the Products List that client can access:");
							for (int j = 0; j < products.length; j++) {
								String product = products[j].trim();
								LOGGER.info("  " + (j + 1) + ". " + product);
							}
							
							// Check if Profile Manager is in the products list
							boolean clientHasProfileManager = productsText.toLowerCase().contains("profile manager");
							LOGGER.info("Has Profile Manager Access: " + clientHasProfileManager);
						
						ExtentCucumberAdapter.addTestStepLog("Client: " + clientName + " | PAMS ID: " + pamsId + " | Products: " + productsText + " | Has Profile Manager: " + clientHasProfileManager);
						} else {
							LOGGER.info("No products information available for this client");
							ExtentCucumberAdapter.addTestStepLog("Client: " + clientName + " | PAMS ID: " + pamsId + " | No products information available");
						}
						
						LOGGER.info("=== END CLIENT DETAILS ===");
						
						// If we're looking for a specific PAMS ID, we can break after finding it
						if (targetPamsId != null && !targetPamsId.trim().isEmpty() && targetClientFound) {
							break;
						}
					}
					
				} catch (Exception clientException) {
					LOGGER.warn("Could not retrieve details for client row " + (i + 1) + ": " + clientException.getMessage());
				}
			}
			
			// Verify results based on configuration
			if (targetPamsId != null && !targetPamsId.trim().isEmpty()) {
				if (targetClientFound) {
					LOGGER.info("Successfully verified products for target client with PAMS ID: " + targetPamsId);
					ExtentCucumberAdapter.addTestStepLog("Successfully verified products for target client with PAMS ID: " + targetPamsId);
				} else {
					LOGGER.error("Target client with PAMS ID " + targetPamsId + " was not found");
					ExtentCucumberAdapter.addTestStepLog("Target client with PAMS ID " + targetPamsId + " was not found");
					Assert.fail("Target client with PAMS ID " + targetPamsId + " was not found");
				}
			} else {
				LOGGER.info("Successfully verified products for " + verifiedClientCount + " client(s)");
				ExtentCucumberAdapter.addTestStepLog("Successfully verified products for " + verifiedClientCount + " client(s)");
			}
			
			// Verify that products are displayed for at least one client
			wait.until(ExpectedConditions.visibilityOf(firstClientProducts)).isDisplayed();
			String firstClientProductsText = firstClientProducts.getText();
			Assert.assertFalse(firstClientProductsText.isEmpty(), "Products information is not displayed for the first client");
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_products_that_client_can_access", e);
			LOGGER.error("Issue in verifying products that client can access - Method: verify_products_that_client_can_access", e);
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying products that client can access");
			Assert.fail("Issue in verifying products that client can access");
		}
	}
	
	/**
	 * Verify client name based on PAMS ID
	 * Uses target.pams.id configuration to find and verify specific client name
	 */
	public void verify_client_name_based_on_pams_id() {
		try {
			String targetPamsId = CommonVariable.TARGET_PAMS_ID;
			
			// If no specific PAMS ID is configured, skip this verification
			if (targetPamsId == null || targetPamsId.trim().isEmpty()) {
				LOGGER.info("No specific PAMS ID configured - skipping client name verification");
				ExtentCucumberAdapter.addTestStepLog("No specific PAMS ID configured - skipping client name verification");
				return;
			}
			
			LOGGER.info("Verifying client name for PAMS ID: " + targetPamsId);
			ExtentCucumberAdapter.addTestStepLog("Verifying client name for PAMS ID: " + targetPamsId);
			
			// Wait for clients table to be visible
			wait.until(ExpectedConditions.visibilityOf(clientsTableBody)).isDisplayed();
			
			// Get client rows
			java.util.List<WebElement> clientRows = driver.findElements(By.xpath("//tbody//tr[@class='table-row']"));
			Assert.assertTrue(clientRows.size() > 0, "No client rows found in the table");
			
			LOGGER.info("Found " + clientRows.size() + " client(s) to search");
			ExtentCucumberAdapter.addTestStepLog("Found " + clientRows.size() + " client(s) to search");
			
			boolean targetClientFound = false;
			String foundClientName = "";
			
			// Iterate through each client row to find the target PAMS ID
			for (int i = 0; i < clientRows.size(); i++) {
				try {
					// Get client name - try both clickable link and non-clickable div
					
					
					try {
						// First try to find clickable client link
						WebElement clientNameElement = clientRows.get(i).findElement(By.xpath(".//a[contains(@data-testid,'client-')]"));
						clientName.set(clientNameElement.getText());
					} catch (Exception e) {
						// If no clickable link, try to find non-clickable client name div
						WebElement clientNameElement = clientRows.get(i).findElement(By.xpath(".//div[contains(@class,'data-content-container')]//div[contains(@title, '')]"));
						clientName.set(clientNameElement.getAttribute("title"));
					}
					
					// Get PAMS ID for this client
					WebElement pamsIdElement = clientRows.get(i).findElement(By.xpath(".//td[contains(@data-testid,'iam-clients-list-pams_id-table-data-cell-')]"));
					String pamsId = pamsIdElement.getText().trim();
					
					LOGGER.info("Checking client: " + clientName.get() + " | PAMS ID: " + pamsId);
					
					// Check if this is the target client
					if (pamsId.equals(targetPamsId)) {
						targetClientFound = true;
						foundClientName = clientName.get();
						LOGGER.info("Found target client with PAMS ID " + targetPamsId + ": " + clientName.get());
						ExtentCucumberAdapter.addTestStepLog("Found target client with PAMS ID " + targetPamsId + ": " + clientName.get());
						break; // Exit loop once we find the target client
					}
					
				} catch (Exception clientException) {
					LOGGER.warn("Could not check client row " + (i + 1) + ": " + clientException.getMessage());
				}
			}
			
			// Verify that the target client was found
			if (targetClientFound) {
				LOGGER.info("Client name verification successful for PAMS ID " + targetPamsId + ": " + foundClientName);
				ExtentCucumberAdapter.addTestStepLog("Client name verification successful for PAMS ID " + targetPamsId + ": " + foundClientName);
			} else {
				LOGGER.error("Client with PAMS ID " + targetPamsId + " was not found");
				ExtentCucumberAdapter.addTestStepLog("Client with PAMS ID " + targetPamsId + " was not found");
				Assert.fail("Client with PAMS ID " + targetPamsId + " was not found");
			}
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_client_name_based_on_pams_id", e);
			LOGGER.error("Issue in verifying client name based on PAMS ID - Method: verify_client_name_based_on_pams_id", e);
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying client name based on PAMS ID");
			Assert.fail("Issue in verifying client name based on PAMS ID");
		}
	}
	
	/**
	 * Searches for a client using PAMS ID in the KFone Clients page search bar.
	 * 
	 * Behavior:
	 * - If PAMS ID is configured (not blank) in config.properties  Performs search
	 * - If PAMS ID is blank or not configured  Skips search and continues
	 * 
	 * This allows flexible test execution where search is optional based on configuration.
	 */
	public void search_for_client_with_pams_id() {
		try {
			String targetPamsId = CommonVariable.TARGET_PAMS_ID;
			
			// If no specific PAMS ID is configured, skip search
			if (targetPamsId == null || targetPamsId.trim().isEmpty()) {
				LOGGER.info(" SKIPPING: No PAMS ID configured in config.properties - search step skipped");
				ExtentCucumberAdapter.addTestStepLog(" SKIPPING: No PAMS ID configured - search step skipped");
				return; // Continue to next step without search
			}
			
			LOGGER.info("========================================");
			LOGGER.info("SEARCHING FOR CLIENT WITH PAMS ID");
			LOGGER.info("========================================");
			LOGGER.info("Target PAMS ID: " + targetPamsId);
			
			// Wait for clients table and search bar to be visible
			wait.until(ExpectedConditions.visibilityOf(clientsTable)).isDisplayed();
			wait.until(ExpectedConditions.elementToBeClickable(clientSearchBar));
			
			// Clear search bar if it has any text
			clientSearchBar.clear();
			
			// Enter PAMS ID in search bar
			clientSearchBar.sendKeys(targetPamsId);
			
			LOGGER.info("Entered PAMS ID '" + targetPamsId + "' in client search bar");
			ExtentCucumberAdapter.addTestStepLog("Searching for client with PAMS ID: " + targetPamsId);
			
			// Wait for search results to filter
			PerformanceUtils.waitForPageReady(driver, 3);
			Thread.sleep(2000);
			
			// Verify that search results are displayed
			wait.until(ExpectedConditions.visibilityOf(clientsTableBody)).isDisplayed();
			
			// Get filtered client rows
			java.util.List<WebElement> filteredClientRows = driver.findElements(By.xpath("//tbody//tr[@class='table-row']"));
			
			LOGGER.info("========================================");
			LOGGER.info("SEARCH RESULTS");
			LOGGER.info("========================================");
			LOGGER.info("Filtered client rows count: " + filteredClientRows.size());
			
			if (filteredClientRows.size() > 0) {
				LOGGER.info(" Search successful - Found " + filteredClientRows.size() + " matching client(s)");
				ExtentCucumberAdapter.addTestStepLog(" Search successful - Found " + filteredClientRows.size() + " matching client(s) for PAMS ID: " + targetPamsId);
				
				// Log details of found clients
				for (int i = 0; i < filteredClientRows.size(); i++) {
					try {
						// Get client name
						String clientName = "";
						try {
							WebElement clientNameElement = filteredClientRows.get(i).findElement(By.xpath(".//a[contains(@data-testid,'client-')]"));
							clientName = clientNameElement.getText();
						} catch (Exception e) {
							WebElement clientNameElement = filteredClientRows.get(i).findElement(By.xpath(".//div[contains(@class,'data-content-container')]//div[contains(@title, '')]"));
							clientName = clientNameElement.getAttribute("title");
						}
						
						// Get PAMS ID
						WebElement pamsIdElement = filteredClientRows.get(i).findElement(By.xpath(".//td[contains(@data-testid,'iam-clients-list-pams_id-table-data-cell-')]"));
						String pamsId = pamsIdElement.getText().trim();
						
						LOGGER.info("Client " + (i + 1) + ": " + clientName + " | PAMS ID: " + pamsId);
						
					} catch (Exception e) {
						LOGGER.debug("Could not retrieve details for filtered client row " + (i + 1));
					}
				}
			} else {
				// FAIL TEST if no clients found with the searched PAMS ID
				String errorMsg = " No clients found matching PAMS ID: " + targetPamsId;
				LOGGER.error(errorMsg);
				ExtentCucumberAdapter.addTestStepLog(errorMsg);
				LOGGER.info("========================================");
				Assert.fail("No clients found with PAMS ID: " + targetPamsId + ". Please verify the PAMS ID is correct in config.properties");
			}
			
			LOGGER.info("========================================");
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("search_for_client_with_pams_id", e);
			LOGGER.error("Issue in searching for client with PAMS ID - Method: search_for_client_with_pams_id", e);
			ExtentCucumberAdapter.addTestStepLog("Issue in searching for client with PAMS ID");
			Assert.fail("Issue in searching for client with PAMS ID: " + e.getMessage());
		}
	}
	
	public void click_on_client_with_access_to_profile_manager_application() {
		try {
			String targetPamsId = CommonVariable.TARGET_PAMS_ID;
			
			// Wait for clients table to be visible
			wait.until(ExpectedConditions.visibilityOf(clientsTableBody)).isDisplayed();
			
			// Get client rows (excluding header) - use tbody to avoid header row
			java.util.List<WebElement> clientRows = driver.findElements(By.xpath("//tbody//tr[@class='table-row']"));
			Assert.assertTrue(clientRows.size() > 0, "No client rows found in the table");
			
			LOGGER.info("Clicking on client with Profile Manager access");
			ExtentCucumberAdapter.addTestStepLog("Clicking on client with Profile Manager access");
			
			boolean clientClicked = false;
			String selectedClientName = "";
			
			// Iterate through client rows to find and click the appropriate client
			for (int i = 0; i < clientRows.size(); i++) {
				try {
					// Get client name - try both clickable link and non-clickable div
					WebElement clientNameElement = null;
					String clientName = "";
					
					try {
						// First try to find clickable client link
						clientNameElement = clientRows.get(i).findElement(By.xpath(".//a[contains(@data-testid,'client-')]"));
						clientName = clientNameElement.getText();
					} catch (Exception e) {
						// If no clickable link, try to find non-clickable client name div
						clientNameElement = clientRows.get(i).findElement(By.xpath(".//div[contains(@class,'data-content-container')]//div[contains(@title, '')]"));
						clientName = clientNameElement.getAttribute("title");
						
						// If this client doesn't have a clickable link, skip it
						LOGGER.info("Client " + clientName + " is not clickable, skipping...");
						continue;
					}
					
					// Get PAMS ID for this client
					WebElement pamsIdElement = clientRows.get(i).findElement(By.xpath(".//td[contains(@data-testid,'iam-clients-list-pams_id-table-data-cell-')]"));
					String pamsId = pamsIdElement.getText().trim();
					
					// Get products for this client
					WebElement productsElement = clientRows.get(i).findElement(By.xpath(".//td[contains(@data-testid,'iam-clients-list-clientProducts-table-data-cell-')]"));
					String productsText = productsElement.getText();
					
					// Check if this client has Profile Manager access
					boolean hasProfileManager = productsText.toLowerCase().contains("profile manager");
					
					// Determine if this is the target client based on PAMS ID configuration
					boolean isTargetClient = false;
					if (targetPamsId != null && !targetPamsId.trim().isEmpty()) {
						isTargetClient = pamsId.equals(targetPamsId);
					} else {
						// If no specific PAMS ID, consider all clients
						isTargetClient = true;
					}
					
					// Click on client if it matches our criteria
					if (isTargetClient && hasProfileManager && !clientClicked) {
						// Scroll element into view
						js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", clientNameElement);
						SmartWaits.waitForElementClickable(driver, clientNameElement);
						
						// Click on this client
						clientNameElement.click();
						clientClicked = true;
						selectedClientName = clientName;
						
						LOGGER.info("Successfully clicked on client: " + clientName + " (PAMS ID: " + pamsId + ")");
						ExtentCucumberAdapter.addTestStepLog("Successfully clicked on client: " + clientName + " (PAMS ID: " + pamsId + ")");
						break; // Stop the loop after clicking
					}
					
				} catch (Exception clientException) {
					LOGGER.warn("Could not check client row " + (i + 1) + ": " + clientException.getMessage());
					// Continue to next client instead of failing
				}
			}
			
			// Verify that a client was clicked
			if (!clientClicked) {
				LOGGER.error("No suitable client found to click");
				ExtentCucumberAdapter.addTestStepLog("No suitable client found to click");
				Assert.fail("No suitable client found to click");
			}
			
			// Wait for navigation to complete
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			SmartWaits.waitForPageLoad(driver);
			
			LOGGER.info("Successfully navigated to client: " + selectedClientName);
			ExtentCucumberAdapter.addTestStepLog("Successfully navigated to client: " + selectedClientName);
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_on_client_with_access_to_profile_manager_application", e);
			LOGGER.error("Issue in clicking on client with access to Profile Manager Application - Method: click_on_client_with_access_to_profile_manager_application", e);
			ExtentCucumberAdapter.addTestStepLog("Issue in clicking on client with access to Profile Manager Application");
			Assert.fail("Issue in clicking on client with access to Profile Manager Application");
		}
	}
	
	public void verify_user_navigated_to_kfone_home_page() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			SmartWaits.waitForPageLoad(driver);
			PerformanceUtils.waitForPageReady(driver, 5);
			
			// Verify KFONE Home page header
			wait.until(ExpectedConditions.visibilityOf(KFONE_landingPage_title));
			String text1 = wait.until(ExpectedConditions.visibilityOf(KFONEHomePageHeader)).getText();
			LOGGER.info(text1 + " is displaying on KFONE Home Page as expected");
			
			// Verify that user is on KFONE Home page by checking for Your Products section
			// Get the element reference from the wait to avoid stale element issues
			WebElement productsElement = wait.until(ExpectedConditions.visibilityOf(yourProductsSection));
			String productsSectionText = productsElement.getText();
			Assert.assertEquals("Your products", productsSectionText, "User is not on KFONE Home page");
			
			LOGGER.info("User successfully navigated to KFONE Home Page");
			LOGGER.info("Verified 'Your products' section is displayed: " + productsSectionText);
			ExtentCucumberAdapter.addTestStepLog("User successfully navigated to KFONE Home Page");
			ExtentCucumberAdapter.addTestStepLog("Verified 'Your products' section is displayed: " + productsSectionText);
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_user_navigated_to_kfone_home_page", e);
			LOGGER.error("Issue in verifying user navigated to KFONE Home Page - Method: verify_user_navigated_to_kfone_home_page", e);
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying user navigated to KFONE Home Page");
			Assert.fail("Issue in verifying user navigated to KFONE Home Page");
		}
	}
	
	public void click_on_profile_manager_application_in_your_products_section() {
		try {
			// Scroll to the Your Products section to ensure it's visible
			js.executeScript("arguments[0].scrollIntoView(true);", yourProductsSection);
			SmartWaits.shortWait(driver);
			
			// Wait for Profile Manager application to be visible and clickable
			wait.until(ExpectedConditions.elementToBeClickable(profileManagerInProductsSection)).isDisplayed();
			PerformanceUtils.waitForPageReady(driver, 3);
			LOGGER.info("Profile Manager application tile is visible in Your Products section");
			ExtentCucumberAdapter.addTestStepLog("Profile Manager application tile is visible in Your Products section");
			
			// Click on Profile Manager application
			wait.until(ExpectedConditions.elementToBeClickable(profileManagerInProductsSection)).click();
			
			LOGGER.info("Successfully clicked on Profile Manager application in Your Products section");
			ExtentCucumberAdapter.addTestStepLog("Successfully clicked on Profile Manager application in Your Products section");
			
			// Wait for navigation to complete
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner));
			SmartWaits.waitForPageLoad(driver);
			
			// OPTIMIZATION: Handle cookies banner immediately after navigation (with short timeout)
//			handleCookiesBanner();
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_on_profile_manager_application_in_your_products_section", e);
			LOGGER.error("Issue in clicking on Profile Manager application in Your Products section - Method: click_on_profile_manager_application_in_your_products_section", e);
			ExtentCucumberAdapter.addTestStepLog("Issue in clicking on Profile Manager application in Your Products section");
			Assert.fail("Issue in clicking on Profile Manager application in Your Products section");
		}
	}
}
