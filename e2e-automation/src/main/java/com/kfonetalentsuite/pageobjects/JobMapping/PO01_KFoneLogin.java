package com.kfonetalentsuite.pageobjects.JobMapping;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.SessionManager;
import com.kfonetalentsuite.utils.JobMapping.PageObjectHelper;
import com.kfonetalentsuite.utils.common.CommonVariable;
import com.kfonetalentsuite.utils.common.ExcelConfigProvider;
import com.kfonetalentsuite.utils.common.ExcelDataProvider;
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
	private static final By PM_HEADER = By.xpath("//h1[contains(text(),'Profile Manager')]");
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
			PageObjectHelper.log(LOGGER, "Successfully Launched KFONE " + environment + " Environment URL: " + url);
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "launch_the_kfone_application", "Issue in launching KFONE application", e);
		}
	}

	public void provide_sso_login_username_and_click_sign_in_button_in_kfone_login_page() {
		try {
			handleCookiesBanner();
			String ssoUsername = getCredential("SSO", "Username");
			WebElement userNameTxt = wait.until(ExpectedConditions.elementToBeClickable(USERNAME_INPUT));
			userNameTxt.sendKeys(ssoUsername);
			username.set(ssoUsername);
			jsClick(driver.findElement(KFONE_SIGNIN_BTN));
			safeSleep(2000);
			PageObjectHelper.log(LOGGER, "Entered SSO username and proceeded to Microsoft login");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "provide_sso_login_username_and_click_sign_in_button_in_kfone_login_page",
					"Issue in providing SSO login username and clicking sign in button", e);
		}
	}

	public void user_should_navigate_to_microsoft_login_page() {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(MICROSOFT_PASSWORD_HEADER)).isDisplayed();
			PageObjectHelper.log(LOGGER, "Navigated to Microsoft Login page");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_navigate_to_microsoft_login_page", "Issue in navigating to Microsoft Login page", e);
		}
	}

	public void provide_sso_login_password_and_click_sign_in() {
		try {
			String ssoPassword = getCredential("SSO", "Password");
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(MICROSOFT_PASSWORD_HEADER)).isDisplayed());
			wait.until(ExpectedConditions.elementToBeClickable(PASSWORD_INPUT)).sendKeys(ssoPassword);
			jsClick(driver.findElement(MICROSOFT_SUBMIT_BTN));
			wait.until(ExpectedConditions.elementToBeClickable(MICROSOFT_SUBMIT_BTN)).click();
			PageObjectHelper.log(LOGGER, "Entered SSO password and completed Microsoft login");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "provide_sso_login_password_and_click_sign_in", "Issue in providing SSO login password", e);
		}
	}

	public void provide_non_sso_login_username_and_click_sign_in_button_in_kfone_login_page() {
		handleCookiesBanner();
		String nonSsoUsername = getCredential("NON_SSO", "Username");
		wait.until(ExpectedConditions.elementToBeClickable(USERNAME_INPUT)).sendKeys(nonSsoUsername);
		username.set(nonSsoUsername);
		jsClick(wait.until(ExpectedConditions.elementToBeClickable(KFONE_SIGNIN_BTN)));
		wait.until(ExpectedConditions.elementToBeClickable(PASSWORD_INPUT));
		PageObjectHelper.log(LOGGER, "Entered username and proceeded to password screen");
	}

	public void provide_non_sso_login_password_and_click_sign_in_button_in_kfone_login_page() {
		String nonSsoPassword = getCredential("NON_SSO", "Password");
		wait.until(ExpectedConditions.elementToBeClickable(PASSWORD_INPUT)).sendKeys(nonSsoPassword);
		jsClick(wait.until(ExpectedConditions.elementToBeClickable(KFONE_SIGNIN_BTN)));
		PerformanceUtils.waitForPageReady(driver, 10);
		PageObjectHelper.log(LOGGER, "Entered password and submitted login");
	}

	public void login_using_excel_data(String testId) {
		try {
			Map<String, String> testData = ExcelDataProvider.getTestData("LoginData", testId);
			String userType = testData.get("UserType");
			String excelUsername = testData.get("Username");
			String excelPassword = testData.get("Password");

			PageObjectHelper.log(LOGGER, "Data-Driven Login: TestID=" + testId + ", UserType=" + userType);
			handleCookiesBanner();

			wait.until(ExpectedConditions.elementToBeClickable(USERNAME_INPUT)).sendKeys(excelUsername);
			username.set(excelUsername);
			jsClick(driver.findElement(KFONE_SIGNIN_BTN));

			if ("SSO".equalsIgnoreCase(userType)) {
				wait.until(ExpectedConditions.visibilityOfElementLocated(MICROSOFT_PASSWORD_HEADER));
				wait.until(ExpectedConditions.elementToBeClickable(PASSWORD_INPUT)).sendKeys(excelPassword);
				jsClick(driver.findElement(MICROSOFT_SUBMIT_BTN));
				wait.until(ExpectedConditions.elementToBeClickable(MICROSOFT_SUBMIT_BTN)).click();
			} else {
				wait.until(ExpectedConditions.elementToBeClickable(PASSWORD_INPUT)).sendKeys(excelPassword);
				jsClick(driver.findElement(KFONE_SIGNIN_BTN));
			}
			PerformanceUtils.waitForPageReady(driver, 10);
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "login_using_excel_data", "Failed to login using Excel data for TestID: " + testId, e);
		}
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
						PageObjectHelper.log(LOGGER, "Accepted KFONE terms and conditions");
					} catch (NoSuchElementException | TimeoutException e) {
						LOGGER.debug("Proceed button detected but couldn't be clicked: " + e.getMessage());
					}
				}
			} finally {
				driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
			}

			PerformanceUtils.waitForPageReady(driver, 10);
			WebElement clientsHeader = waitForElement(Locators.KFONE.CLIENTS_PAGE_HEADER);
			Assert.assertEquals("Clients", clientsHeader.getText(), "Expected 'Clients' header on KFONE landing page");
			PageObjectHelper.log(LOGGER, "Landed on KFONE Clients Page as Expected");
			SessionManager.markAuthenticated();
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_the_kfone_landing_page", "Issue in verifying KFONE landing page after login", e);
		}
	}

	public void verify_user_seemlessly_landed_on_profile_manager_application_in_kf_hub() {
		try {
			int maxRetries = 2;
			boolean pmHeaderFound = false;

			for (int retry = 1; retry <= maxRetries && !pmHeaderFound; retry++) {
				try {
					WebDriverWait extendedWait = new WebDriverWait(driver, Duration.ofSeconds(30));
					PerformanceUtils.waitForPageReady(driver, 10);
					PerformanceUtils.waitForSpinnersToDisappear(driver, 15);
					PerformanceUtils.safeSleep(driver, 2000);

					WebElement pmHeader = extendedWait.until(ExpectedConditions.visibilityOfElementLocated(PM_HEADER));
					if (pmHeader.isDisplayed()) {
						pmHeaderFound = true;
						PageObjectHelper.log(LOGGER, "User Successfully landed on the " + pmHeader.getText() + " Dashboard Page");
						break;
					}
				} catch (Exception retryEx) {
					if (retry < maxRetries) {
						LOGGER.warn("Blank page detected (attempt {}/{}) - refreshing page...", retry, maxRetries);
						driver.navigate().refresh();
						PerformanceUtils.waitForPageReady(driver, 10);
					}
				}
			}
			if (!pmHeaderFound) {
				throw new RuntimeException("Profile Manager header not found after " + maxRetries + " attempts");
			}
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_user_seemlessly_landed_on_profile_manager_application_in_kf_hub",
					"Issue with seamless navigation from KFONE to Profile Manager Application", e);
		}
	}

	public void user_is_in_kfone_clients_page() {
		try {
			waitForPageLoad();
			wait.until(ExpectedConditions.visibilityOfElementLocated(CLIENTS_TABLE)).isDisplayed();
			PageObjectHelper.log(LOGGER, "User is successfully on KFONE Clients page");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_is_in_kfone_clients_page", "Issue in verifying user is on KFONE Clients page", e);
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
				wait.until(ExpectedConditions.visibilityOfElementLocated(CLIENTS_TABLE_BODY));
			}

			List<WebElement> clientRows = driver.findElements(CLIENT_ROWS);
			Assert.assertTrue(clientRows.size() > 0, "No client rows found in the table");
			PageObjectHelper.log(LOGGER, "Found " + clientRows.size() + " client(s) in the table");

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
			PageObjectHelper.log(LOGGER, "Successfully verified products for " + verifiedClientCount + " client(s)");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_products_that_client_can_access", "Issue in verifying products", e);
		}
	}

	public void verify_client_name_based_on_pams_id() {
		try {
			String targetPamsId = getPamsId();
			if (targetPamsId == null || targetPamsId.isEmpty()) {
				PageObjectHelper.log(LOGGER, "No specific PAMS ID configured - skipping");
				return;
			}

			wait.until(ExpectedConditions.visibilityOfElementLocated(CLIENTS_TABLE_BODY));
			List<WebElement> clientRows = driver.findElements(CLIENT_ROWS);

			for (WebElement row : clientRows) {
				String pamsId = row.findElement(CLIENT_PAMS_ID_CELL).getText().trim();
				if (pamsId.equals(targetPamsId)) {
					clientName.set(getClientName(row));
					PageObjectHelper.log(LOGGER, "Found target client: " + clientName.get());
					return;
				}
			}
			Assert.fail("Client with PAMS ID " + targetPamsId + " was not found");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_client_name_based_on_pams_id", "Issue in verifying client name", e);
		}
	}

	public void search_for_client_with_pams_id() {
		try {
			String targetPamsId = getPamsId();
			if (targetPamsId == null || targetPamsId.isEmpty()) {
				PageObjectHelper.log(LOGGER, "SKIPPING: No PAMS ID configured");
				return;
			}

			wait.until(ExpectedConditions.visibilityOfElementLocated(CLIENTS_TABLE));
			WebElement searchBar = wait.until(ExpectedConditions.elementToBeClickable(CLIENT_SEARCH_BAR));
			searchBar.clear();
			searchBar.sendKeys(targetPamsId);
			PageObjectHelper.log(LOGGER, "Searching for client with PAMS ID: " + targetPamsId);

			PerformanceUtils.waitForPageReady(driver, 3);
			safeSleep(2000);

			List<WebElement> filteredRows = driver.findElements(CLIENT_ROWS);
			if (filteredRows.isEmpty()) {
				Assert.fail("No clients found with PAMS ID: " + targetPamsId);
			}
			PageObjectHelper.log(LOGGER, "Found " + filteredRows.size() + " matching client(s)");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "search_for_client_with_pams_id", "Issue in searching for client", e);
		}
	}

	public void click_on_client_with_access_to_profile_manager_application() {
		try {
			String targetPamsId = getPamsId();
			wait.until(ExpectedConditions.visibilityOfElementLocated(CLIENTS_TABLE_BODY));
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
						PerformanceUtils.waitForElementClickable(driver, nameElement);
						clickElement(nameElement);
						clientName.set(clientNameStr);
						PageObjectHelper.log(LOGGER, "Clicked on client: " + clientNameStr + " (PAMS ID: " + pamsId + ")");
						PerformanceUtils.waitForPageReady(driver, 5);
						return;
					}
				} catch (Exception e) {
					continue;
				}
			}
			Assert.fail("No suitable client found to click");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_client_with_access_to_profile_manager_application", "Issue clicking client", e);
		}
	}

	public void verify_user_navigated_to_kfone_home_page() {
		try {
			waitForPageLoad();
			waitForElement(Locators.KFONE.LANDING_PAGE_TITLE);
			WebElement homeHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(KFONE_HOME_HEADER));
			LOGGER.info(homeHeader.getText() + " is displaying on KFONE Home Page");
			WebElement productsSection = wait.until(ExpectedConditions.visibilityOfElementLocated(YOUR_PRODUCTS_SECTION));
			Assert.assertEquals("Your products", productsSection.getText(), "User is not on KFONE Home page");
			PageObjectHelper.log(LOGGER, "User successfully navigated to KFONE Home Page");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_user_navigated_to_kfone_home_page", "Issue verifying KFONE Home Page", e);
		}
	}

	public void click_on_profile_manager_application_in_your_products_section() {
		try {
			scrollToElement(driver.findElement(YOUR_PRODUCTS_SECTION));
			WebElement pmTile = wait.until(ExpectedConditions.elementToBeClickable(PM_IN_PRODUCTS_SECTION));
			waitForPageLoad();
			clickElement(pmTile);
			PageObjectHelper.log(LOGGER, "Clicked on Profile Manager application");
			waitForPageLoad();
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_profile_manager_application_in_your_products_section", "Issue clicking PM tile", e);
		}
	}

	// =============================================
	// PRIVATE HELPER METHODS (PO01-specific config)
	// =============================================

	private String getEnvironment() {
		Map<String, String> login = ExcelConfigProvider.getDefaultLogin();
		if (login != null && login.get("Environment") != null && !login.get("Environment").isEmpty()) {
			return login.get("Environment").trim();
		}
		return CommonVariable.ENVIRONMENT != null ? CommonVariable.ENVIRONMENT : "QA";
	}

	private String getUrlForEnvironment(String env) {
		return switch (env) {
			case "Stage" -> CommonVariable.KFONE_STAGEURL;
			case "ProdEU" -> CommonVariable.KFONE_PRODEUURL;
			case "ProdUS" -> CommonVariable.KFONE_PRODUSURL;
			case "Dev" -> CommonVariable.KFONE_DEVURL;
			default -> CommonVariable.KFONE_QAURL;
		};
	}

	private String getCredential(String userType, String field) {
		try {
			Map<String, String> login = ExcelConfigProvider.getLoginByType(userType);
			if (login != null) return login.get(field);
		} catch (Exception e) {
			LOGGER.debug("Excel read failed: {}", e.getMessage());
		}
		if ("SSO".equalsIgnoreCase(userType)) {
			return "Username".equals(field) ? CommonVariable.SSO_USERNAME : CommonVariable.SSO_PASSWORD;
		}
		return "Username".equals(field) ? CommonVariable.NON_SSO_USERNAME : CommonVariable.NON_SSO_PASSWORD;
	}

	private String getPamsId() {
		String pamsId = ExcelConfigProvider.getActivePamsId();
		return (pamsId != null && !pamsId.trim().isEmpty()) ? pamsId.trim() : CommonVariable.TARGET_PAMS_ID;
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
