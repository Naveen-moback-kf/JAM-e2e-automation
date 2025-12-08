package com.kfonetalentsuite.utils.JobMapping;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.util.List;

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

	// =============================================
	// FILE UPLOAD UTILITIES
	// =============================================

	public static boolean uploadFile(WebDriver driver, String filePath, By browseButtonLocator) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		
		if (tryDirectFileInput(driver, filePath)) return true;
		if (tryClickAndFindInput(driver, js, filePath, browseButtonLocator)) return true;
		if (tryJavaScriptUpload(js, filePath)) return true;
		if (!isHeadlessMode(js) && tryRobotUpload(driver, filePath, browseButtonLocator)) return true;
		
		return false;
	}

	public static boolean tryDirectFileInput(WebDriver driver, String filePath) {
		String[] selectors = {
			"//input[@type='file']",
			"//input[contains(@id,'upload')]",
			"//input[contains(@class,'upload')]",
			"//input[contains(@name,'file')]",
			"//input[contains(@accept,'.csv')]"
		};

		for (String selector : selectors) {
			try {
				List<WebElement> inputs = driver.findElements(By.xpath(selector));
				for (WebElement input : inputs) {
					if (input.isEnabled()) {
						input.sendKeys(filePath);
						PerformanceUtils.waitForUIStability(driver, 2);
						return true;
					}
				}
			} catch (Exception e) {
				continue;
			}
		}
		return false;
	}

	public static boolean tryClickAndFindInput(WebDriver driver, JavascriptExecutor js, String filePath, By browseBtn) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			wait.until(ExpectedConditions.elementToBeClickable(browseBtn)).click();
			PerformanceUtils.waitForUIStability(driver, 1);

			String[] selectors = {
				"//input[@type='file']",
				"//input[contains(@style,'opacity: 0') or contains(@style,'display: none')][@type='file']",
				"//input[contains(@id,'upload')]",
				"//input[contains(@class,'upload')]"
			};

			for (String selector : selectors) {
				try {
					WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
					List<WebElement> inputs = shortWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(selector)));

					for (WebElement input : inputs) {
						try {
							js.executeScript("arguments[0].style.display='block'; arguments[0].style.visibility='visible'; arguments[0].style.opacity='1';", input);
							input.sendKeys(filePath);
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
		} catch (Exception e) {
		}
		return false;
	}

	public static boolean tryJavaScriptUpload(JavascriptExecutor js, String filePath) {
		try {
			String script = """
				var fileInput = document.createElement('input');
				fileInput.type = 'file';
				fileInput.style.display = 'none';
				document.body.appendChild(fileInput);
				return fileInput;
			""";

			WebElement input = (WebElement) js.executeScript(script);
			if (input != null) {
				input.sendKeys(filePath);
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	public static boolean tryRobotUpload(WebDriver driver, String filePath, By browseBtn) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			wait.until(ExpectedConditions.elementToBeClickable(browseBtn)).click();

			Robot rb = new Robot();
			rb.delay(2000);

			StringSelection ss = new StringSelection(filePath);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);

			rb.keyPress(KeyEvent.VK_CONTROL);
			rb.keyPress(KeyEvent.VK_V);
			rb.delay(1000);
			rb.keyRelease(KeyEvent.VK_V);
			rb.keyRelease(KeyEvent.VK_CONTROL);
			rb.delay(1000);

			rb.keyPress(KeyEvent.VK_ENTER);
			rb.keyRelease(KeyEvent.VK_ENTER);
			rb.delay(2000);

			return true;
		} catch (Exception e) {
		}
		return false;
	}

	public static boolean isHeadlessMode(JavascriptExecutor js) {
		try {
			return js.executeScript("return navigator.webdriver === true").toString().equals("true")
				|| System.getProperty("java.awt.headless", "false").equals("true")
				|| System.getProperty("webdriver.chrome.args", "").contains("--headless");
		} catch (Exception e) {
			return true;
		}
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
				
				// Also fetch and store userLevelJobMappingEnabled and userLevelPermission
				fetchAndStoreUserLevelSettings(js);
				
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
	 * Fetch userLevelJobMappingEnabled and userLevelPermission from session storage
	 * and store them globally for use across all feature files.
	 * 
	 * @param js JavascriptExecutor instance
	 */
	private static void fetchAndStoreUserLevelSettings(JavascriptExecutor js) {
		try {
			// Fetch userLevelJobMappingEnabled
			String jobMappingEnabledScript = """
						try {
							var value = sessionStorage.getItem('userLevelJobMappingEnabled');
							return value !== null ? value : 'NOT_FOUND';
						} catch (error) {
							return 'ERROR: ' + error.message;
						}
					""";

			Object jobMappingResult = js.executeScript(jobMappingEnabledScript);
			String jobMappingValue = (jobMappingResult != null) ? jobMappingResult.toString() : null;

			if (jobMappingValue != null && !jobMappingValue.equals("NOT_FOUND") && !jobMappingValue.startsWith("ERROR")) {
				Boolean isEnabled = "true".equalsIgnoreCase(jobMappingValue);
				CommonVariable.USER_LEVEL_JOB_MAPPING_ENABLED.set(isEnabled);
				LOGGER.info("User Level Job Mapping Enabled: '{}'", isEnabled);
			} else {
				CommonVariable.USER_LEVEL_JOB_MAPPING_ENABLED.set(null);
				LOGGER.debug("userLevelJobMappingEnabled not found in session storage");
			}

			// Fetch userLevelPermission
			String permissionScript = """
						try {
							var value = sessionStorage.getItem('userLevelPermission');
							return value !== null ? value : 'NOT_FOUND';
						} catch (error) {
							return 'ERROR: ' + error.message;
						}
					""";

			Object permissionResult = js.executeScript(permissionScript);
			String permissionValue = (permissionResult != null) ? permissionResult.toString() : null;

			if (permissionValue != null && !permissionValue.equals("NOT_FOUND") && !permissionValue.startsWith("ERROR")) {
				CommonVariable.USER_LEVEL_PERMISSION.set(permissionValue);
				LOGGER.info("User Level Permission: '{}'", permissionValue);
			} else {
				CommonVariable.USER_LEVEL_PERMISSION.set(null);
				LOGGER.debug("userLevelPermission not found in session storage");
			}

		} catch (Exception e) {
			LOGGER.warn("Failed to fetch user level settings from session storage: {}", e.getMessage());
			CommonVariable.USER_LEVEL_JOB_MAPPING_ENABLED.set(null);
			CommonVariable.USER_LEVEL_PERMISSION.set(null);
		}
	}

	/**
	 * Get the current user level job mapping enabled status
	 * 
	 * @return Boolean value or null if not set
	 */
	public static Boolean isUserLevelJobMappingEnabled() {
		return CommonVariable.USER_LEVEL_JOB_MAPPING_ENABLED.get();
	}

	/**
	 * Check if current user has access to Job Mapping screen.
	 * This is determined by the userLevelJobMappingEnabled session storage value.
	 * 
	 * @return true if user has Job Mapping access, false otherwise
	 */
	public static boolean hasJobMappingAccess() {
		Boolean enabled = CommonVariable.USER_LEVEL_JOB_MAPPING_ENABLED.get();
		return Boolean.TRUE.equals(enabled);
	}

	/**
	 * Get the current user level permission
	 * 
	 * @return Permission string ("true"/"false") or null if not set
	 */
	public static String getUserLevelPermission() {
		return CommonVariable.USER_LEVEL_PERMISSION.get();
	}

	/**
	 * Check if current user has access to HCM Sync Profiles screen in Profile Manager.
	 * This is determined by the userLevelPermission session storage value.
	 * 
	 * @return true if user has HCM Sync access, false otherwise
	 */
	public static boolean hasHCMSyncAccess() {
		String permission = CommonVariable.USER_LEVEL_PERMISSION.get();
		return "true".equalsIgnoreCase(permission);
	}

	/**
	 * Clear all user level settings (useful for cleanup between tests)
	 */
	public static void clearUserLevelSettings() {
		CommonVariable.USER_LEVEL_JOB_MAPPING_ENABLED.set(null);
		CommonVariable.USER_LEVEL_PERMISSION.set(null);
		LOGGER.info("Cleared user level settings");
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
	 * Set user session details after login and store them globally.
	 * This method fetches the following from session storage:
	 * - User Role (from HayGroup.user.roles)
	 * - userLevelJobMappingEnabled
	 * - userLevelPermission
	 * 
	 * @return true if details were successfully retrieved and stored, false otherwise
	 */
	public static boolean setUserSessionDetailsFromSessionStorage() {
		String role = getUserRoleFromSessionStorage();
		return role != null;
	}
	
	/**
	 * @deprecated Use {@link #setUserSessionDetailsFromSessionStorage()} instead
	 */
	@Deprecated
	public static boolean setCurrentUserRoleFromSessionStorage() {
		return setUserSessionDetailsFromSessionStorage();
	}

	/**
	 * Clear the stored user role and user level settings (useful for cleanup between tests)
	 */
	public static void clearCurrentUserRole() {
		CommonVariable.CURRENT_USER_ROLE.set(null);
		CommonVariable.USER_LEVEL_JOB_MAPPING_ENABLED.set(null);
		CommonVariable.USER_LEVEL_PERMISSION.set(null);
		LOGGER.info("Cleared stored user role and user level settings");
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
