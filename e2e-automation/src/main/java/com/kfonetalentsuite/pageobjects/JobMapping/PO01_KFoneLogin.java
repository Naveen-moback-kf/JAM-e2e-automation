package com.kfonetalentsuite.pageobjects.JobMapping;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.kfonetalentsuite.utils.common.SessionManager;
import com.kfonetalentsuite.utils.JobMapping.Utilities;
import com.kfonetalentsuite.utils.common.CommonVariableManager;
import com.kfonetalentsuite.webdriverManager.DriverManager;

public class PO01_KFoneLogin extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO01_KFoneLogin.class);

	public static ThreadLocal<String> username = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<String> clientName = ThreadLocal.withInitial(() -> "");

	private static final By USERNAME_INPUT = By.xpath("//input[@type='email']");
	private static final By PASSWORD_INPUT = By.xpath("//input[@type='password']");
	private static final By KFONE_SIGNIN_BTN = By.xpath("//button[@id='submit-button']");
	private static final By PROCEED_BTN = By.xpath("//*[text()='Proceed']");
	private static final By MICROSOFT_SUBMIT_BTN = By.xpath("//input[@type='submit']");
	private static final By MICROSOFT_PASSWORD_HEADER = By.xpath("//div[text()='Enter password']");
	private static final By PM_HEADER = Locators.HCMSyncProfiles.PROFILE_MANAGER_HEADER;
	private static final By CLIENTS_TABLE = By.xpath("//table[@id='iam-clients-list-table-content']");
	private static final By CLIENTS_TABLE_BODY = By.xpath("//tbody[@class='table-body']");
	private static final By CLIENT_SEARCH_BAR = By.xpath("//input[@id='search-client-input-search']");
	private static final By KFONE_HOME_HEADER = By.xpath("//h1[contains(text(),'Hi,')]");
	private static final By YOUR_PRODUCTS_SECTION = By.xpath("//h2[contains(text(),'Your products')]");
	private static final By PM_IN_PRODUCTS_SECTION = By.xpath("//div[@data-testid='Profile Manager']");
	private static final By CLIENT_ROWS = By.xpath("//tbody//tr[@class='table-row']");
	private static final By CLIENT_NAME_LINK = By.xpath(".//a[contains(@data-testid,'client-')]");
	private static final By CLIENT_NAME_DIV = By.xpath(".//div[contains(@class,'data-content-container')]//div[contains(@title, '')]");
	private static final By CLIENT_PAMS_ID_CELL = By.xpath(".//td[contains(@data-testid,'iam-clients-list-pams_id-table-data-cell-')]");
	private static final By CLIENT_PRODUCTS_CELL = By.xpath(".//td[contains(@data-testid,'iam-clients-list-clientProducts-table-data-cell-')]");

	public PO01_KFoneLogin() {
		super();
		if (this.driver == null) {
			DriverManager.launchBrowser();
			this.driver = DriverManager.getDriver();
		}
		if (this.driver == null) {
			throw new RuntimeException("Failed to initialize WebDriver in PO01_KFoneLogin");
		}
	}

	public void launch_the_kfone_application() {
		try {
			if (this.driver == null) {
				this.driver = DriverManager.getDriver();
				if (this.driver == null) {
					DriverManager.launchBrowser();
					this.driver = DriverManager.getDriver();
				}
			}
			if (this.driver == null) {
				throw new RuntimeException("Cannot launch KFONE application - WebDriver is null");
			}

			String environment = getEnvironment();
			String url = getUrlForEnvironment(environment);
			driver.get(url);
			
			// ANTI-BOT DETECTION: Hide WebDriver properties immediately after page load
			hideWebDriverProperties();
			
			LOGGER.info("Successfully Launched KFONE " + environment + " Environment URL: " + url);
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "launch_the_kfone_application", "Issue in launching KFONE application", e);
		}
	}
	
	private void hideWebDriverProperties() {
		try {
			js.executeScript(
				"Object.defineProperty(navigator, 'webdriver', {get: () => undefined});" +
				"Object.defineProperty(navigator, 'plugins', {get: () => [1, 2, 3, 4, 5]});" +
				"Object.defineProperty(navigator, 'languages', {get: () => ['en-US', 'en']});" +
				"window.navigator.chrome = {runtime: {}};" +
				"Object.defineProperty(navigator, 'permissions', {get: () => ({query: () => Promise.resolve({state: 'granted'})})});" +
				"delete navigator.__proto__.webdriver;"
			);
			LOGGER.debug("WebDriver properties hidden to prevent bot detection");
		} catch (Exception e) {
			// Non-critical - log but don't fail
			LOGGER.warn("Could not hide WebDriver properties: {}", e.getMessage());
		}
	}

	public void provide_sso_login_username_and_click_sign_in_button_in_kfone_login_page() {
		try {
			handleCookiesBanner();
			String ssoUsername = getCredential("SSO", "Username");
			Utilities.waitAndSendKeys(wait, USERNAME_INPUT, ssoUsername);
			username.set(ssoUsername);
			jsClick(driver.findElement(KFONE_SIGNIN_BTN));
			safeSleep(2000);
			LOGGER.info("Entered SSO username and proceeded to Microsoft login");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "provide_sso_login_username_and_click_sign_in_button_in_kfone_login_page",
					"Issue in providing SSO login username and clicking sign in button", e);
		}
	}

	public void user_should_navigate_to_microsoft_login_page() {
		try {
			Utilities.waitForVisible(wait, MICROSOFT_PASSWORD_HEADER);
			LOGGER.info("Navigated to Microsoft Login page");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_should_navigate_to_microsoft_login_page", "Issue in navigating to Microsoft Login page", e);
		}
	}

	public void provide_sso_login_password_and_click_sign_in() {
		try {
			String ssoPassword = getCredential("SSO", "Password");
			Assert.assertTrue(Utilities.waitForVisible(wait, MICROSOFT_PASSWORD_HEADER).isDisplayed());
			Utilities.waitAndSendKeys(wait, PASSWORD_INPUT, ssoPassword);
			jsClick(driver.findElement(MICROSOFT_SUBMIT_BTN));
			Utilities.waitAndClick(wait, MICROSOFT_SUBMIT_BTN);
			LOGGER.info("Entered SSO password and completed Microsoft login");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "provide_sso_login_password_and_click_sign_in", "Issue in providing SSO login password", e);
		}
	}

	public void provide_non_sso_login_username_and_click_sign_in_button_in_kfone_login_page() {
		handleCookiesBanner();
		String nonSsoUsername = getCredential("NON_SSO", "Username");
		Utilities.waitAndSendKeys(wait, USERNAME_INPUT, nonSsoUsername);
		username.set(nonSsoUsername);
		jsClick(Utilities.waitForClickable(wait, KFONE_SIGNIN_BTN));
		Utilities.waitForClickable(wait, PASSWORD_INPUT);
		LOGGER.info("Entered username and proceeded to password screen");
	}

	public void provide_non_sso_login_password_and_click_sign_in_button_in_kfone_login_page() {
		String nonSsoPassword = getCredential("NON_SSO", "Password");
		Utilities.waitAndSendKeys(wait, PASSWORD_INPUT, nonSsoPassword);
		jsClick(Utilities.waitForClickable(wait, KFONE_SIGNIN_BTN));
		Utilities.waitForPageReady(driver, 10);
		LOGGER.info("Entered password and submitted login");
	}

	public void verify_the_kfone_landing_page() {
		try {
			try {
				driver.manage().timeouts().implicitlyWait(Duration.ZERO);
				List<WebElement> proceedButtons = driver.findElements(PROCEED_BTN);
				if (!proceedButtons.isEmpty()) {
					driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
					try {
						WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
						shortWait.until(ExpectedConditions.elementToBeClickable(PROCEED_BTN)).click();
						LOGGER.info("Accepted KFONE terms and conditions");
					} catch (NoSuchElementException | TimeoutException e) {
						LOGGER.debug("Proceed button detected but couldn't be clicked: " + e.getMessage());
					}
				}
			} finally {
				driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
			}

			Utilities.waitForPageReady(driver, 10);
			WebElement clientsHeader = waitForElement(Locators.KFONE.CLIENTS_PAGE_HEADER);
			Assert.assertEquals("Clients", clientsHeader.getText(), "Expected 'Clients' header on KFONE landing page");
			LOGGER.info("Landed on KFONE Clients Page");
			SessionManager.markAuthenticated();
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_the_kfone_landing_page", "Issue in verifying KFONE landing page after login", e);
		}
	}

	public void verify_user_seemlessly_landed_on_profile_manager_application_in_kf_hub() {
		try {
			int maxRetries = 2;
			boolean pmHeaderFound = false;

			for (int retry = 1; retry <= maxRetries && !pmHeaderFound; retry++) {
				try {
				WebDriverWait extendedWait = new WebDriverWait(driver, Duration.ofSeconds(30));
				Utilities.waitForPageReady(driver, 10);
				Utilities.waitForSpinnersToDisappear(driver, 15);
				Utilities.waitForUIStability(driver, 2);

					WebElement pmHeader = extendedWait.until(ExpectedConditions.visibilityOfElementLocated(PM_HEADER));
					if (pmHeader.isDisplayed()) {
						pmHeaderFound = true;
						LOGGER.info("User Successfully landed on the " + pmHeader.getText() + " Dashboard Page");
						break;
					}
				} catch (Exception retryEx) {
					if (retry < maxRetries) {
						LOGGER.warn("Blank page detected (attempt {}/{}) - refreshing page...", retry, maxRetries);
						driver.navigate().refresh();
						Utilities.waitForPageReady(driver, 10);
					}
				}
			}
			if (!pmHeaderFound) {
				throw new RuntimeException("Profile Manager header not found after " + maxRetries + " attempts");
			}
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_user_seemlessly_landed_on_profile_manager_application_in_kf_hub",
					"Issue with seamless navigation from KFONE to Profile Manager Application", e);
		}
	}

	public void user_is_in_kfone_clients_page() {
		try {
			waitForPageLoad();
			Utilities.waitForVisible(wait, CLIENTS_TABLE).isDisplayed();
			LOGGER.info("User is successfully on KFONE Clients page");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "user_is_in_kfone_clients_page", "Issue in verifying user is on KFONE Clients page", e);
		}
	}

	public void verify_products_that_client_can_access() {
		try {
			String targetPamsId = getPamsId();
			verifyOnClientsPage();

			WebDriverWait extendedWait = new WebDriverWait(driver, Duration.ofSeconds(45));
			try {
				extendedWait.until(ExpectedConditions.visibilityOfElementLocated(CLIENTS_TABLE_BODY));
			} catch (Exception e) {
				safeSleep(2000);
				Utilities.waitForVisible(wait, CLIENTS_TABLE_BODY);
			}

			List<WebElement> clientRows = driver.findElements(CLIENT_ROWS);
			Assert.assertTrue(clientRows.size() > 0, "No client rows found in the table");
			LOGGER.info("Found " + clientRows.size() + " client(s) in the table");

			boolean targetClientFound = false;
			int verifiedClientCount = 0;

			for (int i = 0; i < clientRows.size(); i++) {
				try {
					String clientNameStr = getClientName(clientRows.get(i));
					String pamsId = clientRows.get(i).findElement(CLIENT_PAMS_ID_CELL).getText().trim();
					String productsText = clientRows.get(i).findElement(CLIENT_PRODUCTS_CELL).getText();

					boolean shouldVerify = (targetPamsId == null || targetPamsId.isEmpty()) || pamsId.equals(targetPamsId);
					if (shouldVerify && pamsId.equals(targetPamsId)) targetClientFound = true;

					if (shouldVerify) {
						verifiedClientCount++;
						LOGGER.info("Client: {} | PAMS ID: {} | Products: {}", clientNameStr, pamsId, productsText);
						if (targetClientFound) break;
					}
				} catch (Exception clientException) {
					LOGGER.warn("Could not retrieve details for client row " + (i + 1));
				}
			}

			if (targetPamsId != null && !targetPamsId.isEmpty() && !targetClientFound) {
				Assert.fail("Target client with PAMS ID " + targetPamsId + " was not found");
			}
			LOGGER.info("Successfully verified products for " + verifiedClientCount + " client(s)");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_products_that_client_can_access", "Issue in verifying products", e);
		}
	}

	public void verify_client_name_based_on_pams_id() {
		try {
			String targetPamsId = getPamsId();
			if (targetPamsId == null || targetPamsId.isEmpty()) {
				LOGGER.info("No specific PAMS ID configured - skipping");
				return;
			}

			Utilities.waitForVisible(wait, CLIENTS_TABLE_BODY);
			List<WebElement> clientRows = driver.findElements(CLIENT_ROWS);

			for (WebElement row : clientRows) {
				String pamsId = row.findElement(CLIENT_PAMS_ID_CELL).getText().trim();
				if (pamsId.equals(targetPamsId)) {
					clientName.set(getClientName(row));
					LOGGER.info("Found target client: " + clientName.get());
					return;
				}
			}
			Assert.fail("Client with PAMS ID " + targetPamsId + " was not found");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_client_name_based_on_pams_id", "Issue in verifying client name", e);
		}
	}

	public void search_for_client_with_pams_id() {
		try {
			String targetPamsId = getPamsId();
			if (targetPamsId == null || targetPamsId.isEmpty()) {
				LOGGER.info("SKIPPING: No PAMS ID configured");
				return;
			}

			Utilities.waitForVisible(wait, CLIENTS_TABLE);
			WebElement searchBar = Utilities.waitForClickable(wait, CLIENT_SEARCH_BAR);
			searchBar.clear();
			searchBar.sendKeys(targetPamsId);
			LOGGER.info("Searching for client with PAMS ID: " + targetPamsId);

			Utilities.waitForPageReady(driver, 3);
			safeSleep(2000);

			List<WebElement> filteredRows = driver.findElements(CLIENT_ROWS);
			if (filteredRows.isEmpty()) {
				Assert.fail("No clients found with PAMS ID: " + targetPamsId);
			}
			LOGGER.info("Found " + filteredRows.size() + " matching client(s)");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "search_for_client_with_pams_id", "Issue in searching for client", e);
		}
	}

	public void click_on_client_with_access_to_profile_manager_application() {
		try {
			String targetPamsId = getPamsId();
			Utilities.waitForVisible(wait, CLIENTS_TABLE_BODY);
			List<WebElement> clientRows = driver.findElements(CLIENT_ROWS);
			Assert.assertTrue(clientRows.size() > 0, "No client rows found");

			for (WebElement row : clientRows) {
				try {
					WebElement nameElement = row.findElement(CLIENT_NAME_LINK);
					String clientNameStr = nameElement.getText();
					String pamsId = row.findElement(CLIENT_PAMS_ID_CELL).getText().trim();
					String products = row.findElement(CLIENT_PRODUCTS_CELL).getText();

					boolean isTarget = (targetPamsId == null || targetPamsId.isEmpty()) || pamsId.equals(targetPamsId);
					boolean hasPM = products.toLowerCase().contains("profile manager");

				if (isTarget && hasPM) {
					scrollToElement(nameElement);
					Utilities.waitForClickable(wait, nameElement);
					clickElement(nameElement);
					clientName.set(clientNameStr);
						LOGGER.info("Clicked on client: " + clientNameStr + " (PAMS ID: " + pamsId + ")");
						Utilities.waitForPageReady(driver, 5);
						return;
					}
				} catch (Exception e) {
					continue;
				}
			}
			Assert.fail("No suitable client found to click");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "click_on_client_with_access_to_profile_manager_application", "Issue clicking client", e);
		}
	}

	public void verify_user_navigated_to_kfone_home_page() {
		try {
			waitForPageLoad();
			waitForElement(Locators.KFONE.LANDING_PAGE_TITLE);
			WebElement homeHeader = Utilities.waitForVisible(wait, KFONE_HOME_HEADER);
			LOGGER.info(homeHeader.getText() + " is displaying on KFONE Home Page");
			WebElement productsSection = Utilities.waitForVisible(wait, YOUR_PRODUCTS_SECTION);
			Assert.assertEquals("Your products", productsSection.getText(), "User is not on KFONE Home page");
			LOGGER.info("User successfully navigated to KFONE Home Page");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_user_navigated_to_kfone_home_page", "Issue verifying KFONE Home Page", e);
		}
	}

	public void click_on_profile_manager_application_in_your_products_section() {
		try {
			scrollToElement(driver.findElement(YOUR_PRODUCTS_SECTION));
			WebElement pmTile = Utilities.waitForClickable(wait, PM_IN_PRODUCTS_SECTION);
			waitForPageLoad();
			clickElement(pmTile);
			LOGGER.info("Clicked on Profile Manager application");
			waitForPageLoad();
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "click_on_profile_manager_application_in_your_products_section", "Issue clicking PM tile", e);
		}
	}
	// PRIVATE HELPER METHODS (PO01-specific config)

	private String getEnvironment() {
		return CommonVariableManager.ENVIRONMENT != null ? CommonVariableManager.ENVIRONMENT : "qa";
	}

	private String getUrlForEnvironment(String env) {
		if (env == null) return CommonVariableManager.KFONE_QAURL;
		
		return switch (env.toLowerCase()) {
			case "stage" -> CommonVariableManager.KFONE_STAGEURL;
			case "prod-eu", "prodeu" -> CommonVariableManager.KFONE_PRODEUURL;
			case "prod-us", "produs" -> CommonVariableManager.KFONE_PRODUSURL;
			case "dev" -> CommonVariableManager.KFONE_DEVURL;
			default -> CommonVariableManager.KFONE_QAURL; // qa is default
		};
	}

	private String getCredential(String userType, String field) {
		if ("SSO".equalsIgnoreCase(userType)) {
			return "Username".equals(field) ? CommonVariableManager.SSO_USERNAME : CommonVariableManager.SSO_PASSWORD;
		}
		return "Username".equals(field) ? CommonVariableManager.NON_SSO_USERNAME : CommonVariableManager.NON_SSO_PASSWORD;
	}

	private String getPamsId() {
		return CommonVariableManager.TARGET_PAMS_ID;
	}

	private void verifyOnClientsPage() {
		String url = driver.getCurrentUrl();
		if (!url.contains("/client")) {
			throw new RuntimeException("Not on clients page. URL: " + url);
		}
	}

	private String getClientName(WebElement row) {
		try {
			return row.findElement(CLIENT_NAME_LINK).getText();
		} catch (Exception e) {
			return row.findElement(CLIENT_NAME_DIV).getAttribute("title");
		}
	}
}


