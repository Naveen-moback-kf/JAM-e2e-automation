package com.kfonetalentsuite.utils.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import com.kfonetalentsuite.pageobjects.JobMapping.PO01_KFoneLogin;
import com.kfonetalentsuite.webdriverManager.DriverManager;

import java.time.Duration;
import java.time.Instant;

public class SessionManager {
	protected static final Logger LOGGER = (Logger) LogManager.getLogger();

	// THREAD-SAFE: Each thread tracks its own session state
	private static ThreadLocal<Instant> lastSessionCheck = ThreadLocal.withInitial(() -> Instant.now());
	private static ThreadLocal<Boolean> isAuthenticated = ThreadLocal.withInitial(() -> false);

	// Configuration: Session validation interval (default: 5 minutes)
	private static final Duration SESSION_CHECK_INTERVAL = Duration.ofMinutes(5);

	// Configuration: Maximum re-login attempts
	private static final int MAX_RELOGIN_ATTEMPTS = 2;

	public static boolean isSessionValid() {
		try {
			WebDriver driver = DriverManager.getDriver();
			if (driver == null) {
				LOGGER.warn("Driver is null - session invalid");
				return false;
			}

			// Check 1: WebDriver session active
			if (!DriverManager.isSessionActive()) {
				LOGGER.warn("WebDriver session is not active");
				return false;
			}

			// Check 2: Current URL (if on login page, session expired)
			String currentUrl = driver.getCurrentUrl();
			if (currentUrl.contains("login") || currentUrl.contains("signin") || currentUrl.contains("auth")) {
				LOGGER.warn("User redirected to login page - session expired");
				isAuthenticated.set(false);
				return false;
			}

			// Check 3: Session storage validation (check for auth tokens)
			JavascriptExecutor js = (JavascriptExecutor) driver;
			String sessionStorageCheck = (String) js.executeScript("try {"
					+ "  var userRoles = sessionStorage.getItem('HayGroup.user.roles');"
					+ "  var authToken = sessionStorage.getItem('authToken') || sessionStorage.getItem('access_token');"
					+ "  if (userRoles || authToken) return 'VALID';" + "  return 'INVALID';"
					+ "} catch(e) { return 'ERROR'; }");

			if (!"VALID".equals(sessionStorageCheck)) {
				LOGGER.warn("Session storage validation failed: {}", sessionStorageCheck);
				isAuthenticated.set(false);
				return false;
			}

			// Check 4: Check for 401 error indicators in page content
			String pageSource = driver.getPageSource();
			if (pageSource.contains("401") || pageSource.contains("Unauthorized")
					|| pageSource.contains("Session expired") || pageSource.contains("Please log in again")) {
				LOGGER.warn("401 Unauthorized or session expiration detected in page content");
				isAuthenticated.set(false);
				return false;
			}

			// All checks passed
			lastSessionCheck.set(Instant.now());
			isAuthenticated.set(true);
			return true;

		} catch (WebDriverException e) {
			LOGGER.error("WebDriver exception during session validation: {}", e.getMessage());
			isAuthenticated.set(false);
			return false;
		} catch (Exception e) {
			LOGGER.error("Unexpected error during session validation: {}", e.getMessage());
			isAuthenticated.set(false);
			return false;
		}
	}

	public static boolean validateSession() {
		try {
			// Check if we need to validate (based on interval)
			Instant now = Instant.now();
			Duration timeSinceLastCheck = Duration.between(lastSessionCheck.get(), now);

			if (timeSinceLastCheck.compareTo(SESSION_CHECK_INTERVAL) < 0 && isAuthenticated.get()) {
				// Recently validated and authenticated - skip check
				return true;
			}

			LOGGER.info("Validating session (last check: {} seconds ago)", timeSinceLastCheck.getSeconds());

			// Perform validation
			if (isSessionValid()) {
				LOGGER.info("✅ Session is valid");
				return true;
			}

			// Session invalid - attempt re-login
			LOGGER.warn("⚠️ Session invalid - attempting automatic re-login");
			return reLogin();

		} catch (Exception e) {
			LOGGER.error("Error during session validation: {}", e.getMessage(), e);
			return false;
		}
	}

	private static boolean reLogin() {
		LOGGER.info("🔄 Starting automatic re-login process...");

		for (int attempt = 1; attempt <= MAX_RELOGIN_ATTEMPTS; attempt++) {
			try {
				LOGGER.info("Re-login attempt {}/{}", attempt, MAX_RELOGIN_ATTEMPTS);

				WebDriver driver = DriverManager.getDriver();

				// Step 1: Navigate to login page
				String loginUrl = CommonVariableManager.KFONE_QAURL;
				if (loginUrl == null || loginUrl.isEmpty()) {
					LOGGER.error("Login URL not configured in CommonVariableManager.KFONE_QAURL");
					continue;
				}

				LOGGER.info("Navigating to login page: {}", loginUrl);
				driver.get(loginUrl);
				Utilities.waitForPageReady(driver, 5);

				// Step 2: Perform login using PO01_KFoneLogin
				PO01_KFoneLogin loginPage = new PO01_KFoneLogin();

				// Determine login type based on username
				boolean isSSOLogin = CommonVariableManager.SSO_USERNAME != null && !CommonVariableManager.SSO_USERNAME.isEmpty();

				if (isSSOLogin) {
					LOGGER.info("Performing SSO re-login for user: {}", CommonVariableManager.SSO_USERNAME);
					loginPage.provide_sso_login_username_and_click_sign_in_button_in_kfone_login_page();
					Utilities.waitForPageReady(driver, 3);
					loginPage.provide_sso_login_password_and_click_sign_in();
				} else {
					LOGGER.info("Performing non-SSO re-login for user: {}", CommonVariableManager.NON_SSO_USERNAME);
					loginPage.provide_non_sso_login_username_and_click_sign_in_button_in_kfone_login_page();
					Utilities.waitForPageReady(driver, 3);
					loginPage.provide_non_sso_login_password_and_click_sign_in_button_in_kfone_login_page();
				}

				// Step 3: Wait for login to complete
				Utilities.waitForPageReady(driver, 10);
				Thread.sleep(2000); // Allow session to establish

				// Step 4: Validate session after re-login
				if (isSessionValid()) {
					LOGGER.info("✅ Re-login successful on attempt {}", attempt);
					isAuthenticated.set(true);
					lastSessionCheck.set(Instant.now());
					return true;
				} else {
					LOGGER.warn("❌ Re-login validation failed on attempt {}", attempt);
				}

			} catch (Exception e) {
				LOGGER.error("Re-login attempt {} failed: {}", attempt, e.getMessage(), e);
				if (attempt == MAX_RELOGIN_ATTEMPTS) {
					LOGGER.error("❌ All re-login attempts exhausted");
					return false;
				}
			}
		}

		return false;
	}

	public static <T> T executeWithSessionValidation(java.util.function.Supplier<T> operation, String operationName) {
		return executeWithSessionValidation(operation, operationName, 2);
	}

	public static <T> T executeWithSessionValidation(java.util.function.Supplier<T> operation, String operationName,
			int maxRetries) {
		// Validate session before operation
		if (!validateSession()) {
			throw new RuntimeException("Session validation failed before executing: " + operationName);
		}

		for (int attempt = 1; attempt <= maxRetries; attempt++) {
			try {
				LOGGER.debug("Executing operation '{}' (attempt {}/{})", operationName, attempt, maxRetries);
				return operation.get();

			} catch (Exception e) {
				String errorMsg = e.getMessage() != null ? e.getMessage().toLowerCase() : "";

				// Check if error is session-related
				boolean isSessionError = errorMsg.contains("401") || errorMsg.contains("unauthorized")
						|| errorMsg.contains("session") || errorMsg.contains("authentication")
						|| errorMsg.contains("login");

				if (isSessionError && attempt < maxRetries) {
					LOGGER.warn("⚠️ Session error detected in '{}': {} - Attempting re-login", operationName,
							e.getMessage());

					// Attempt re-login
					if (reLogin()) {
						LOGGER.info("🔄 Re-login successful - Retrying operation '{}'", operationName);
						continue; // Retry operation
					} else {
						throw new RuntimeException("Re-login failed for operation: " + operationName, e);
					}
				} else {
					// Not a session error or max retries reached
					if (attempt == maxRetries) {
						throw new RuntimeException(
								"Operation failed after " + maxRetries + " attempts: " + operationName, e);
					}
					throw e;
				}
			}
		}

		throw new RuntimeException("Unexpected state in executeWithSessionValidation for: " + operationName);
	}

	public static void executeWithSessionValidation(Runnable operation, String operationName) {
		executeWithSessionValidation(() -> {
			operation.run();
			return null;
		}, operationName);
	}

	public static void markAuthenticated() {
		isAuthenticated.set(true);
		lastSessionCheck.set(Instant.now());
		LOGGER.info("✅ Session marked as authenticated");
	}

	public static void markExpired() {
		isAuthenticated.set(false);
		LOGGER.warn("⚠️ Session marked as expired");
	}

	public static void reset() {
		isAuthenticated.set(false);
		lastSessionCheck.set(Instant.now());
		LOGGER.info("Session state reset");
	}

	public static Duration getTimeSinceLastCheck() {
		return Duration.between(lastSessionCheck.get(), Instant.now());
	}

	public static boolean isMarkedAuthenticated() {
		return isAuthenticated.get();
	}
}

