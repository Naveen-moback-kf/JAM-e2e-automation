package com.kfonetalentsuite.pageobjects.JobMapping;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
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
import com.kfonetalentsuite.utils.JobMapping.SessionManager;
import com.kfonetalentsuite.utils.PageObjectHelper;
import com.kfonetalentsuite.utils.common.CommonVariable;
import com.kfonetalentsuite.webdriverManager.DriverManager;

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
				PageObjectHelper.log(LOGGER, "Successfully Launched KFONE " + CommonVariable.ENVIRONMENT
						+ " Environment URL: " + CommonVariable.KFONE_STAGEURL);
				break;

			case "ProdEU":
				driver.get(CommonVariable.KFONE_PRODEUURL);
				PageObjectHelper.log(LOGGER, "Successfully Launched KFONE " + CommonVariable.ENVIRONMENT
						+ " Environment URL: " + CommonVariable.KFONE_PRODEUURL);
				break;
			case "ProdUS":
				driver.get(CommonVariable.KFONE_PRODUSURL);
				PageObjectHelper.log(LOGGER, "Successfully Launched KFONE " + CommonVariable.ENVIRONMENT
						+ " Environment URL: " + CommonVariable.KFONE_PRODUSURL);
				break;
			case "Test":
				driver.get(CommonVariable.KFONE_QAURL);
				PageObjectHelper.log(LOGGER, "Successfully Launched KFONE " + CommonVariable.ENVIRONMENT
						+ " Environment URL: " + CommonVariable.KFONE_QAURL);
				break;
			case "Dev":
				driver.get(CommonVariable.KFONE_DEVURL);
				PageObjectHelper.log(LOGGER, "Successfully Launched KFONE " + CommonVariable.ENVIRONMENT
						+ " Environment URL: " + CommonVariable.KFONE_DEVURL);
				break;
			default:
				driver.get(CommonVariable.KFONE_QAURL);
				PageObjectHelper.log(LOGGER, "Launched KFONE (DEFAULT) " + CommonVariable.ENVIRONMENT
						+ " Environment URL: " + CommonVariable.KFONE_QAURL);
			}

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "launch_the_kfone_application",
					"Issue in launching KFONE application in environment: " + CommonVariable.ENVIRONMENT, e);
		}
	}

	public void provide_sso_login_username_and_click_sign_in_button_in_kfone_login_page() {
		try {
			handleCookiesBanner();
			wait.until(ExpectedConditions.elementToBeClickable(userNameTxt)).sendKeys(CommonVariable.SSO_USERNAME);
			PageObjectHelper.log(LOGGER, "Provided SSO Login Username: " + CommonVariable.SSO_USERNAME);
			username.set(CommonVariable.SSO_USERNAME);
			utils.jsClick(driver, kfoneSigninBtn);
			Thread.sleep(2000);
			PageObjectHelper.log(LOGGER, "Clicked on Sign in Button in KFONE Login Page");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"provide_sso_login_username_and_click_sign_in_button_in_kfone_login_page",
					"Issue in providing SSO login username and clicking sign in button", e);
		}
	}

	public void user_should_navigate_to_microsoft_login_page() {
		try {
			wait.until(ExpectedConditions.visibilityOf(MicrosoftPasswordPageHeader)).isDisplayed();
			PageObjectHelper.log(LOGGER, "User successfully navigated to Microsoft Login page");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_navigate_to_microsoft_login_page",
					"Issue in navigating to Microsoft Login page", e);
		}
	}

	public void provide_sso_login_password_and_click_sign_in() {
		try {
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(MicrosoftPasswordPageHeader)).isDisplayed());
			wait.until(ExpectedConditions.elementToBeClickable(MicrosoftPasswordTxt))
					.sendKeys(CommonVariable.SSO_PASSWORD);
			PageObjectHelper.log(LOGGER, "Provided SSO Login Password in Microsoft Login Page");

			utils.jsClick(driver, MicrosoftSignInBtn);
			PageObjectHelper.log(LOGGER, "Clicked on Signin Button in Microsoft Login Page");

			wait.until(ExpectedConditions.elementToBeClickable(MicrosoftYesBtn)).click();
			PageObjectHelper.log(LOGGER, "Clicked on Yes Button in Microsoft Login Page");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "provide_sso_login_password_and_click_sign_in",
					"Issue in providing SSO login password and clicking sign in", e);
		}
	}

	public void provide_non_sso_login_username_and_click_sign_in_button_in_kfone_login_page() {
		handleCookiesBanner();

		wait.until(ExpectedConditions.elementToBeClickable(userNameTxt)).sendKeys(CommonVariable.NON_SSO_USERNAME);
		PageObjectHelper.log(LOGGER, "Provided NON-SSO Login Username: " + CommonVariable.NON_SSO_USERNAME);
		username.set(CommonVariable.NON_SSO_USERNAME);

		// PARALLEL EXECUTION FIX: Wait for button to be ready, then click
		WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(kfoneSigninBtn));
		utils.jsClick(driver, signInButton);

		// PERFORMANCE FIX: After username submit, wait for PASSWORD field (not
		// spinners!)
		// The page transitions to password screen - no spinners appear here
		wait.until(ExpectedConditions.elementToBeClickable(passwordTxt));

		PageObjectHelper.log(LOGGER, "Clicked on Sign in Button in KFONE Login Page");
	}

	public void provide_non_sso_login_password_and_click_sign_in_button_in_kfone_login_page() {
		wait.until(ExpectedConditions.elementToBeClickable(passwordTxt)).sendKeys(CommonVariable.NON_SSO_PASSWORD);
		PageObjectHelper.log(LOGGER, "Provided NON-SSO Login Password in KFONE Login Page");

		// PARALLEL EXECUTION FIX: Ensure Sign In button is ready before clicking
		WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(kfoneSigninBtn));
		utils.jsClick(driver, signInButton);
		PageObjectHelper.log(LOGGER, "Clicked on Sign in Button in KFONE Login Page");
		PerformanceUtils.waitForPageReady(driver, 10);
	}

	public void verify_the_kfone_landing_page() {
		try {
			// PARALLEL EXECUTION FIX: Disable implicit wait for quick check
			try {
				driver.manage().timeouts().implicitlyWait(Duration.ZERO);

				// OPTIMIZED: Check if proceedBtn exists before waiting (instant if not present)
				List<WebElement> proceedButtons = driver.findElements(By.xpath("//*[text()='Proceed']"));

				if (!proceedButtons.isEmpty()) {
					driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20)); // Restore for click
					try {
						WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
						shortWait.until(ExpectedConditions.elementToBeClickable(proceedBtn)).click();
						PageObjectHelper.log(LOGGER, "Accepted KFONE terms and conditions");
					} catch (NoSuchElementException | TimeoutException e) {
						// Terms popup appeared but couldn't be clicked - continue
						LOGGER.debug("Proceed button detected but couldn't be clicked: " + e.getMessage());
					}
				}
			} finally {
				// Always restore implicit wait
				driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
			}

			// PERFORMANCE: Single comprehensive wait replaces redundant spinner + page load
			// waits
			// This handles both spinners and page readiness in one efficient call
			PerformanceUtils.waitForPageReady(driver, 10);

			// Verify landing page elements - use single wait to get element and verify
			WebElement clientsHeader = wait.until(ExpectedConditions.visibilityOf(KFONE_clientsPage_header));
			String headerText = clientsHeader.getText();
			Assert.assertEquals("Clients", headerText, "Expected 'Clients' header on KFONE landing page");

			PageObjectHelper.log(LOGGER, "Landed on KFONE Clients Page as Expected");
			PageObjectHelper.log(LOGGER, "KFONE Landing Page Verification is Successful");

			// SESSION MANAGEMENT: Mark session as authenticated after successful login
			SessionManager.markAuthenticated();
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_the_kfone_landing_page",
					"Issue in verifying KFONE landing page after login", e);
		}

	}

	public void verify_user_seemlessly_landed_on_profile_manager_application_in_kf_hub() {
		try {
			int maxRetries = 2;
			boolean pmHeaderFound = false;

			for (int retry = 1; retry <= maxRetries && !pmHeaderFound; retry++) {
				try {
					// EXTENDED WAIT: Give page more time to load fully (especially for slow
					// environments)
					WebDriverWait extendedWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(30));

					// Wait for page ready first (includes spinner wait)
					PerformanceUtils.waitForPageReady(driver, 10); // Increased from 5 to 10 seconds

					// Additional spinner wait to ensure all loaders are gone
					PerformanceUtils.waitForSpinnersToDisappear(driver, 15);

					// Small stabilization wait after spinners disappear
					PerformanceUtils.safeSleep(driver, 2000);

					// Check if PM Header is visible
					if (extendedWait.until(ExpectedConditions.visibilityOf(PMHeader)).isDisplayed()) {
						pmHeaderFound = true;
						String MainHeader = PMHeader.getText();
						PageObjectHelper.log(LOGGER,
								"✅ User Successfully landed on the " + MainHeader + " Dashboard Page");
						break;
					}

				} catch (Exception retryEx) {
					if (retry < maxRetries) {
						LOGGER.warn("Blank page detected (attempt {}/{}) - refreshing page...", retry, maxRetries);
						driver.navigate().refresh();
						PerformanceUtils.waitForPageReady(driver, 10); // Wait after refresh (increased from 5 to 10)
					}
				}
			}

			if (!pmHeaderFound) {
				throw new RuntimeException("Profile Manager header not found after " + maxRetries + " attempts");
			}

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"verify_user_seemlessly_landed_on_profile_manager_application_in_kf_hub",
					"Issue with seamless navigation from KFONE to Profile Manager Application", e);
		}
	}

	/**
	 * Optimized cookies banner handler with short timeout Handles cookies banner
	 * immediately after navigation to avoid long waits
	 */
	private void handleCookiesBanner() {
		try {
			// Use short timeout (5 seconds) for cookies banner - if not present, move on
			// quickly
			WebDriverWait cookiesWait = new WebDriverWait(driver, Duration.ofSeconds(5));
			WebElement cookiesButton = cookiesWait.until(ExpectedConditions.elementToBeClickable(acceptAllCookies));

			// Try standard click first
			try {
				cookiesButton.click();
				PageObjectHelper.log(LOGGER, "Closed Cookies Banner by clicking on Accept All button");
				return;
			} catch (Exception e) {
				// If standard click fails, try JS click
				try {
					js.executeScript("arguments[0].click();", cookiesButton);
					LOGGER.debug("Used JavaScript click for cookies banner");
					PageObjectHelper.log(LOGGER, "Closed Cookies Banner by clicking on Accept All button");
					return;
				} catch (Exception jsError) {
					// Last resort: utils.jsClick
					utils.jsClick(driver, cookiesButton);
					LOGGER.debug("Used utility click for cookies banner");
					PageObjectHelper.log(LOGGER, "Closed Cookies Banner by clicking on Accept All button");
				}
			}
		} catch (TimeoutException e) {
			// Cookies banner not present or already accepted - this is normal
			PageObjectHelper.log(LOGGER, "Cookies Banner is already accepted or not present");
		} catch (Exception e) {
			// Don't fail test if cookies handling fails - just log and continue
			LOGGER.warn("Could not handle cookies banner: " + e.getMessage());
		}
	}

	public void user_is_in_kfone_clients_page() {
		try {
			PerformanceUtils.waitForPageReady(driver, 2);
			wait.until(ExpectedConditions.visibilityOf(clientsTable)).isDisplayed();
			PageObjectHelper.log(LOGGER, "User is successfully on KFONE Clients page");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_is_in_kfone_clients_page",
					"Issue in verifying user is on KFONE Clients page", e);
		}
	}

	public void verify_products_that_client_can_access() {
		try {
			String targetPamsId = CommonVariable.TARGET_PAMS_ID;

			// CRITICAL: Ensure we're on the clients page before proceeding
			try {
				String currentUrl = (String) js.executeScript("return window.location.href;");
				LOGGER.info("📍 Verifying products on page: " + currentUrl);

				if (!currentUrl.contains("/client")) {
					LOGGER.error("❌ Not on clients page! Current URL: " + currentUrl);
					throw new RuntimeException("Cannot verify products: Not on clients page. URL: " + currentUrl);
				}
			} catch (ClassCastException e) {
				// Fallback if JS returns null
				String currentUrl = driver.getCurrentUrl();
				if (!currentUrl.contains("/client")) {
					throw new RuntimeException("Cannot verify products: Not on clients page. URL: " + currentUrl);
				}
			}

			// Wait for table with extended timeout and retry logic
			WebDriverWait extendedWait = new WebDriverWait(driver, Duration.ofSeconds(45));
			try {
				extendedWait.until(ExpectedConditions.visibilityOf(clientsTableBody)).isDisplayed();
			} catch (Exception e) {
				LOGGER.warn("⚠️  First attempt to find clients table failed, retrying...");
				Thread.sleep(2000);
				wait.until(ExpectedConditions.visibilityOf(clientsTableBody)).isDisplayed();
			}

			// Verify that the table contains client data
			Assert.assertTrue(clientsTableBody.isDisplayed(), "Clients table body is not displayed");

			// Get client rows (excluding header)
			java.util.List<WebElement> clientRows = driver.findElements(By.xpath("//tbody//tr[@class='table-row']"));
			Assert.assertTrue(clientRows.size() > 0, "No client rows found in the table");

			PageObjectHelper.log(LOGGER, "Found " + clientRows.size() + " client(s) in the table");

			// Determine verification scope based on PAMS ID configuration
			if (targetPamsId != null && !targetPamsId.trim().isEmpty()) {
				PageObjectHelper.log(LOGGER, "Verifying products for specific client with PAMS ID: " + targetPamsId);
			} else {
				PageObjectHelper.log(LOGGER, "Verifying products for all available clients");
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
						WebElement clientNameElement = clientRows.get(i)
								.findElement(By.xpath(".//a[contains(@data-testid,'client-')]"));
						clientName = clientNameElement.getText();
					} catch (Exception e) {
						// If no clickable link, try to find non-clickable client name div
						WebElement clientNameElement = clientRows.get(i).findElement(By
								.xpath(".//div[contains(@class,'data-content-container')]//div[contains(@title, '')]"));
						clientName = clientNameElement.getAttribute("title");
					}

					// Get PAMS ID for this client
					WebElement pamsIdElement = clientRows.get(i).findElement(
							By.xpath(".//td[contains(@data-testid,'iam-clients-list-pams_id-table-data-cell-')]"));
					String pamsId = pamsIdElement.getText().trim();

					// Get products for this client
					WebElement productsElement = clientRows.get(i).findElement(By
							.xpath(".//td[contains(@data-testid,'iam-clients-list-clientProducts-table-data-cell-')]"));
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

							PageObjectHelper.log(LOGGER, "Client: " + clientName + " | PAMS ID: " + pamsId
									+ " | Products: " + productsText + " | Has PM: " + clientHasProfileManager);
						} else {
							PageObjectHelper.log(LOGGER, "Client: " + clientName + " | PAMS ID: " + pamsId
									+ " | No products information available");
						}

						LOGGER.info("=== END CLIENT DETAILS ===");

						// If we're looking for a specific PAMS ID, we can break after finding it
						if (targetPamsId != null && !targetPamsId.trim().isEmpty() && targetClientFound) {
							break;
						}
					}

				} catch (Exception clientException) {
					LOGGER.warn("Could not retrieve details for client row " + (i + 1) + ": "
							+ clientException.getMessage());
				}
			}

			// Verify results based on configuration
			if (targetPamsId != null && !targetPamsId.trim().isEmpty()) {
				if (targetClientFound) {
					PageObjectHelper.log(LOGGER,
							"Successfully verified products for target client with PAMS ID: " + targetPamsId);
				} else {
					LOGGER.error("Target client with PAMS ID " + targetPamsId + " was not found");
					Assert.fail("Target client with PAMS ID " + targetPamsId + " was not found");
				}
			} else {
				PageObjectHelper.log(LOGGER,
						"Successfully verified products for " + verifiedClientCount + " client(s)");
			}

			// Verify that products are displayed for at least one client
			wait.until(ExpectedConditions.visibilityOf(firstClientProducts)).isDisplayed();
			String firstClientProductsText = firstClientProducts.getText();
			Assert.assertFalse(firstClientProductsText.isEmpty(),
					"Products information is not displayed for the first client");

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_products_that_client_can_access",
					"Issue in verifying products that client can access", e);
		}
	}

	/**
	 * Verify client name based on PAMS ID Uses target.pams.id configuration to find
	 * and verify specific client name
	 */
	public void verify_client_name_based_on_pams_id() {
		try {
			String targetPamsId = CommonVariable.TARGET_PAMS_ID;

			// If no specific PAMS ID is configured, skip this verification
			if (targetPamsId == null || targetPamsId.trim().isEmpty()) {
				PageObjectHelper.log(LOGGER, "No specific PAMS ID configured - skipping client name verification");
				return;
			}

			PageObjectHelper.log(LOGGER, "Verifying client name for PAMS ID: " + targetPamsId);

			// Wait for clients table to be visible
			wait.until(ExpectedConditions.visibilityOf(clientsTableBody)).isDisplayed();

			// Get client rows
			java.util.List<WebElement> clientRows = driver.findElements(By.xpath("//tbody//tr[@class='table-row']"));
			Assert.assertTrue(clientRows.size() > 0, "No client rows found in the table");

			PageObjectHelper.log(LOGGER, "Found " + clientRows.size() + " client(s) to search");

			boolean targetClientFound = false;
			String foundClientName = "";

			// Iterate through each client row to find the target PAMS ID
			for (int i = 0; i < clientRows.size(); i++) {
				try {
					// Get client name - try both clickable link and non-clickable div

					try {
						// First try to find clickable client link
						WebElement clientNameElement = clientRows.get(i)
								.findElement(By.xpath(".//a[contains(@data-testid,'client-')]"));
						clientName.set(clientNameElement.getText());
					} catch (Exception e) {
						// If no clickable link, try to find non-clickable client name div
						WebElement clientNameElement = clientRows.get(i).findElement(By
								.xpath(".//div[contains(@class,'data-content-container')]//div[contains(@title, '')]"));
						clientName.set(clientNameElement.getAttribute("title"));
					}

					// Get PAMS ID for this client
					WebElement pamsIdElement = clientRows.get(i).findElement(
							By.xpath(".//td[contains(@data-testid,'iam-clients-list-pams_id-table-data-cell-')]"));
					String pamsId = pamsIdElement.getText().trim();

					LOGGER.info("Checking client: " + clientName.get() + " | PAMS ID: " + pamsId);

					// Check if this is the target client
					if (pamsId.equals(targetPamsId)) {
						targetClientFound = true;
						foundClientName = clientName.get();
						PageObjectHelper.log(LOGGER,
								"Found target client with PAMS ID " + targetPamsId + ": " + clientName.get());
						break; // Exit loop once we find the target client
					}

				} catch (Exception clientException) {
					LOGGER.warn("Could not check client row " + (i + 1) + ": " + clientException.getMessage());
				}
			}

			// Verify that the target client was found
			if (targetClientFound) {
				PageObjectHelper.log(LOGGER,
						"Client name verification successful for PAMS ID " + targetPamsId + ": " + foundClientName);
			} else {
				LOGGER.error("Client with PAMS ID " + targetPamsId + " was not found");
				Assert.fail("Client with PAMS ID " + targetPamsId + " was not found");
			}

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_client_name_based_on_pams_id",
					"Issue in verifying client name based on PAMS ID", e);
		}
	}

	/**
	 * Searches for a client using PAMS ID in the KFone Clients page search bar.
	 * 
	 * Behavior: - If PAMS ID is configured (not blank) in config.properties
	 * Performs search - If PAMS ID is blank or not configured Skips search and
	 * continues
	 * 
	 * This allows flexible test execution where search is optional based on
	 * configuration.
	 */
	public void search_for_client_with_pams_id() {
		try {
			String targetPamsId = CommonVariable.TARGET_PAMS_ID;

			// If no specific PAMS ID is configured, skip search
			if (targetPamsId == null || targetPamsId.trim().isEmpty()) {
				PageObjectHelper.log(LOGGER,
						" SKIPPING: No PAMS ID configured in config.properties - search step skipped");
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

			PageObjectHelper.log(LOGGER, "Searching for client with PAMS ID: " + targetPamsId);

			// Wait for search results to filter
			PerformanceUtils.waitForPageReady(driver, 3);
			Thread.sleep(2000);

			// Verify that search results are displayed
			wait.until(ExpectedConditions.visibilityOf(clientsTableBody)).isDisplayed();

			// Get filtered client rows
			java.util.List<WebElement> filteredClientRows = driver
					.findElements(By.xpath("//tbody//tr[@class='table-row']"));

			LOGGER.info("========================================");
			LOGGER.info("SEARCH RESULTS");
			LOGGER.info("========================================");
			LOGGER.info("Filtered client rows count: " + filteredClientRows.size());

			if (filteredClientRows.size() > 0) {
				PageObjectHelper.log(LOGGER, " Search successful - Found " + filteredClientRows.size()
						+ " matching client(s) for PAMS ID: " + targetPamsId);

				// Log details of found clients
				for (int i = 0; i < filteredClientRows.size(); i++) {
					try {
						// Get client name
						String clientName = "";
						try {
							WebElement clientNameElement = filteredClientRows.get(i)
									.findElement(By.xpath(".//a[contains(@data-testid,'client-')]"));
							clientName = clientNameElement.getText();
						} catch (Exception e) {
							WebElement clientNameElement = filteredClientRows.get(i).findElement(By.xpath(
									".//div[contains(@class,'data-content-container')]//div[contains(@title, '')]"));
							clientName = clientNameElement.getAttribute("title");
						}

						// Get PAMS ID
						WebElement pamsIdElement = filteredClientRows.get(i).findElement(
								By.xpath(".//td[contains(@data-testid,'iam-clients-list-pams_id-table-data-cell-')]"));
						String pamsId = pamsIdElement.getText().trim();

						LOGGER.info("Client " + (i + 1) + ": " + clientName + " | PAMS ID: " + pamsId);

					} catch (Exception e) {
						LOGGER.debug("Could not retrieve details for filtered client row " + (i + 1));
					}
				}
			} else {
				// FAIL TEST if no clients found with the searched PAMS ID
				LOGGER.error(" No clients found matching PAMS ID: " + targetPamsId);
				LOGGER.info("========================================");
				Assert.fail("No clients found with PAMS ID: " + targetPamsId
						+ ". Please verify the PAMS ID is correct in config.properties");
			}

			LOGGER.info("========================================");

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "search_for_client_with_pams_id",
					"Issue in searching for client with PAMS ID: " + e.getMessage(), e);
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

			PageObjectHelper.log(LOGGER, "Clicking on client with Profile Manager access");

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
						clientNameElement = clientRows.get(i)
								.findElement(By.xpath(".//a[contains(@data-testid,'client-')]"));
						clientName = clientNameElement.getText();
					} catch (Exception e) {
						// If no clickable link, try to find non-clickable client name div
						clientNameElement = clientRows.get(i).findElement(By
								.xpath(".//div[contains(@class,'data-content-container')]//div[contains(@title, '')]"));
						clientName = clientNameElement.getAttribute("title");

						// If this client doesn't have a clickable link, skip it
						LOGGER.info("Client " + clientName + " is not clickable, skipping...");
						continue;
					}

					// Get PAMS ID for this client
					WebElement pamsIdElement = clientRows.get(i).findElement(
							By.xpath(".//td[contains(@data-testid,'iam-clients-list-pams_id-table-data-cell-')]"));
					String pamsId = pamsIdElement.getText().trim();

					// Get products for this client
					WebElement productsElement = clientRows.get(i).findElement(By
							.xpath(".//td[contains(@data-testid,'iam-clients-list-clientProducts-table-data-cell-')]"));
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
						js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});",
								clientNameElement);
						SmartWaits.waitForElementClickable(driver, clientNameElement);

						// Click on this client
						clientNameElement.click();
						clientClicked = true;
						selectedClientName = clientName;

						// CRITICAL FIX: Store client name in ThreadLocal for Excel reporting
						PO01_KFoneLogin.clientName.set(clientName);
						LOGGER.info("✅ Stored client name for Excel reporting: {}", clientName);

						PageObjectHelper.log(LOGGER,
								"Successfully clicked on client: " + clientName + " (PAMS ID: " + pamsId + ")");
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
				Assert.fail("No suitable client found to click");
			}

			// Wait for navigation to complete
			// OPTIMIZED: Single comprehensive wait (replaces spinner + pageLoad redundancy)
			PerformanceUtils.waitForPageReady(driver, 5);

			PageObjectHelper.log(LOGGER, "Successfully navigated to client: " + selectedClientName);

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_client_with_access_to_profile_manager_application",
					"Issue in clicking on client with access to Profile Manager Application", e);
		}
	}

	public void verify_user_navigated_to_kfone_home_page() {
		try {
			// OPTIMIZED: Single comprehensive wait (replaces 3 redundant waits)
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

			PageObjectHelper.log(LOGGER, "User successfully navigated to KFONE Home Page");
			PageObjectHelper.log(LOGGER, "Verified 'Your products' section is displayed: " + productsSectionText);

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_user_navigated_to_kfone_home_page",
					"Issue in verifying user navigated to KFONE Home Page", e);
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
			PageObjectHelper.log(LOGGER, "Profile Manager application tile is visible in Your Products section");

			// Click on Profile Manager application with fallback for frozen page
			try {
				wait.until(ExpectedConditions.elementToBeClickable(profileManagerInProductsSection)).click();
			} catch (Exception e) {
				LOGGER.warn("WebDriver click failed, trying JavaScript click: " + e.getMessage().split("\n")[0]);
				try {
					js.executeScript("arguments[0].click();", profileManagerInProductsSection);
				} catch (Exception je) {
					LOGGER.warn("JavaScript click failed, trying utility click: " + je.getMessage().split("\n")[0]);
					utils.jsClick(driver, profileManagerInProductsSection);
				}
			}

			PageObjectHelper.log(LOGGER,
					"Successfully clicked on Profile Manager application in Your Products section");

			// Wait for navigation to complete
			// OPTIMIZED: Single comprehensive wait (replaces spinner + pageLoad redundancy)
			PerformanceUtils.waitForPageReady(driver, 5);

			// OPTIMIZATION: Handle cookies banner immediately after navigation (with short
			// timeout)
			// handleCookiesBanner();

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_profile_manager_application_in_your_products_section",
					"Issue in clicking on Profile Manager application in Your Products section", e);
		}
	}
}
