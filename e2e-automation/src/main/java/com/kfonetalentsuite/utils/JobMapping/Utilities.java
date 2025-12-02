package com.kfonetalentsuite.utils.JobMapping;

import java.time.Duration;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.kfonetalentsuite.utils.common.CommonVariable;
import com.kfonetalentsuite.webdriverManager.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Utilities {
	private static final Logger LOGGER = LogManager.getLogger(Utilities.class);

	public void jsClick(WebDriver driver, WebElement element) {
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].click();", element);
	}

	public String readText(By value, WebDriver driver) {
		String text = null;
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));
			text = wait.until(ExpectedConditions.visibilityOfElementLocated((value))).getText();
		} catch (Exception e) {
			LOGGER.debug("Could not read text from element: {}", e.getMessage());
		}
		return text;
	}


	/**
	 * Extract user role from session storage and store it globally Navigates:
	 * Session Storage -> HayGroup.user.roles -> [0] -> name Stores the role in
	 * CommonVariable.CURRENT_USER_ROLE (ThreadLocal) for use across all feature
	 * files
	 * 
	 * @return User role name (e.g., "KF Super User") or null if not found
	 */
	public static String getUserRoleFromSessionStorage() {
		try {
			WebDriver driver = DriverManager.getDriver();
			JavascriptExecutor js = (JavascriptExecutor) driver;

			LOGGER.info("Extracting user role from session storage...");

			// Execute JavaScript to get user role from session storage
			String script = """
						try {
							// Get the HayGroup.user.roles from session storage
							var userRolesData = sessionStorage.getItem('HayGroup.user.roles');

							if (!userRolesData) {
								return 'SESSION_STORAGE_KEY_NOT_FOUND';
							}

							// Parse the JSON data
							var rolesArray = JSON.parse(userRolesData);

							if (!Array.isArray(rolesArray) || rolesArray.length === 0) {
								return 'ROLES_ARRAY_EMPTY';
							}

							// Get the first role (index 0) and extract the name
							var firstRole = rolesArray[0];

							if (!firstRole || !firstRole.name) {
								return 'ROLE_NAME_NOT_FOUND';
							}

							return firstRole.name;

						} catch (error) {
							return 'ERROR: ' + error.message;
						}
					""";

			Object result = js.executeScript(script);
			String roleName = (result != null) ? result.toString() : null;

			if (roleName != null && !roleName.startsWith("ERROR") && !roleName.equals("SESSION_STORAGE_KEY_NOT_FOUND")
					&& !roleName.equals("ROLES_ARRAY_EMPTY") && !roleName.equals("ROLE_NAME_NOT_FOUND")) {

				// Store the role globally for use across all feature files
				CommonVariable.CURRENT_USER_ROLE.set(roleName);
				LOGGER.info("Current User Role : '{}'", roleName);
				return roleName;
			} else {
				LOGGER.warn("Failed to extract user role. Result: {}", roleName);
				CommonVariable.CURRENT_USER_ROLE.set(null);
				return null;
			}

		} catch (Exception e) {
			LOGGER.error("Exception while extracting user role from session storage: {}", e.getMessage());
			CommonVariable.CURRENT_USER_ROLE.set(null);
			return null;
		}
	}

	/**
	 * Get the currently stored user role (set after login)
	 * 
	 * @return Current user role from global variable, or null if not set
	 */
	public static String getCurrentUserRole() {
		return CommonVariable.CURRENT_USER_ROLE.get();
	}

	/**
	 * Check if user has a specific role
	 * 
	 * @param expectedRole Role to check against
	 * @return true if current user role matches expected role
	 */
	public static boolean hasRole(String expectedRole) {
		String currentRole = CommonVariable.CURRENT_USER_ROLE.get();
		if (currentRole == null || expectedRole == null) {
			return false;
		}
		return currentRole.equals(expectedRole);
	}

	/**
	 * Set user role after login and store it globally This method fetches the role
	 * from session storage and stores it for use across all feature files
	 * 
	 * @return true if role was successfully retrieved and stored, false otherwise
	 */
	public static boolean setCurrentUserRoleFromSessionStorage() {
		String role = getUserRoleFromSessionStorage();
		return role != null;
	}

	/**
	 * Clear the stored user role (useful for cleanup between tests)
	 */
	public static void clearCurrentUserRole() {
		CommonVariable.CURRENT_USER_ROLE.set(null);
		LOGGER.info("Cleared stored user role");
	}

	/**
	 * Verify user role after login and log the result Now uses the globally stored
	 * role for validation across feature files
	 * 
	 * @param expectedRole Expected role name (optional - if null, just logs the
	 *                     found role)
	 * @return true if role matches expected (or if no expected role provided),
	 *         false otherwise
	 */
	public static boolean verifyUserRole(String expectedRole) {
		try {
			// First try to get from global variable
			String actualRole = CommonVariable.CURRENT_USER_ROLE.get();

			// If not set globally, fetch from session storage and store it
			if (actualRole == null) {
				LOGGER.info("User role not stored globally, fetching from session storage...");
				actualRole = getUserRoleFromSessionStorage();
			}

			if (actualRole == null) {
				LOGGER.error(" Could not retrieve user role from session storage");
				return false;
			}

			LOGGER.info(" Current User Role: '{}'", actualRole);

			if (expectedRole != null && !expectedRole.trim().isEmpty()) {
				if (actualRole.equals(expectedRole)) {
					LOGGER.info("... User role verification PASSED: Expected '{}', Found '{}'", expectedRole,
							actualRole);
					return true;
				} else {
					LOGGER.error(" User role verification FAILED: Expected '{}', Found '{}'", expectedRole, actualRole);
					return false;
				}
			} else {
				LOGGER.info("... User role successfully retrieved: '{}'", actualRole);
				return true;
			}

		} catch (Exception e) {
			LOGGER.error("Exception during user role verification: {}", e.getMessage());
			return false;
		}
	}

}
