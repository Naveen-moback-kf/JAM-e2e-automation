package com.JobMapping.webdriverManager;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;


import com.JobMapping.utils.common.CommonVariable;
import com.JobMapping.utils.PerformanceUtils;


import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverManager {
	private static WebDriver driver = null;
	protected static final Logger LOGGER = (Logger) LogManager.getLogger();
	public static WebDriverWait wait;

	public static ThreadLocal<WebDriver> webDriver = new ThreadLocal<WebDriver>();
	
	@BeforeMethod
	public void CreateDriver() {
		if (DriverManager.getDriver() == null) {
			try {
				DriverManager.launchBrowser();
			} catch (Exception e) {
				LOGGER.error("Exception in DriverManager initialization: {}", e.getMessage(), e);
			}
		}
	
	}
	
	@BeforeTest
	public static void launchBrowser() {
		// Check if driver is already initialized AND session is active
		if (driver != null && isSessionActive()) {
			LOGGER.info("Driver already initialized with active session, skipping browser launch");
			return;
		}
		
		// If driver exists but session is inactive, clean it up
		if (driver != null && !isSessionActive()) {
			LOGGER.warn("Driver exists but session is inactive - cleaning up before re-initialization");
			try {
				driver.quit();
			} catch (Exception e) {
				LOGGER.debug("Error during driver cleanup: {}", e.getMessage());
			}
			driver = null;
		}
		
		try {
	switch (CommonVariable.BROWSER) {
			case "chrome":
				WebDriverManager.chromedriver().setup();
				// Setting new download directory path
				Map<String, Object> prefs = new HashMap<String, Object>();
				// Use File.separator as it will work on any OS
				prefs.put("download.default_directory", System.getProperty("user.dir") + File.separator
						+ "externalFiles" + File.separator + "downloadFiles");
				// Adding capabilities to ChromeOptions
				ChromeOptions options = new ChromeOptions();
				options.setExperimentalOption("prefs", prefs);
				options.addArguments("--remote-allow-origins=*");
				
				// SSL and Security bypass options
				options.addArguments("--ignore-ssl-errors=yes");
				options.addArguments("--ignore-certificate-errors");
				options.addArguments("--ignore-certificate-errors-spki-list");
				options.addArguments("--disable-web-security");
				options.addArguments("--allow-running-insecure-content");
				options.addArguments("--disable-extensions");
				
				options.addArguments("--no-sandbox");
				options.addArguments("--disable-dev-shm-usage");
				options.addArguments("--disable-background-timer-throttling");
				options.addArguments("--disable-backgrounding-occluded-windows");
				options.addArguments("--disable-renderer-backgrounding");
				options.addArguments("--disable-features=TranslateUI");
				options.addArguments("--disable-background-media-audio");
				
				// Basic Chrome options
				options.addArguments("--disable-hang-monitor");
				options.addArguments("--disable-prompt-on-repost");
				options.addArguments("--disable-default-apps");
				options.addArguments("--disable-popup-blocking");
				options.addArguments("--disable-translate");
				
				// Basic Chrome options for stability
				options.addArguments("--disable-infobars");
				options.addArguments("--disable-web-security");
				options.addArguments("--disable-features=TranslateUI");
				
				// HEADLESS MODE CONFIGURATION - Read from config.properties
				String headlessMode = CommonVariable.HEADLESS_MODE != null ? CommonVariable.HEADLESS_MODE : "true"; // Default to headless
				boolean isHeadless = Boolean.parseBoolean(headlessMode);
				
				if (isHeadless) {
					LOGGER.info("Running Chrome in HEADLESS mode");
					options.addArguments("--headless=new");
					options.addArguments("--window-size=1920,1080");
				} else {
					// LOGGER.info("Running Chrome in WINDOWED mode");
					options.addArguments("--start-maximized");
				}
				
				// Basic session management
				options.addArguments("--disable-hang-monitor");
				// To Turns off multiple download warning
				prefs.put("profile.default_content_settings.popups", 0);
				//prefs.put("profile.content_settings.pattern_pairs.*.multiple-automatic-downloads", 1);
				prefs.put("profile.default_content_setting_values.automatic_downloads", 1);
				// Turns off download prompt
				prefs.put("download.prompt_for_download", false);
				// options.setPageLoadStrategy(PageLoadStrategy.EAGER);
				// Launching browser with desired capabilities
				driver = new ChromeDriver(options);
				LOGGER.info("Chrome launched successfully");
				break;

			case "firefox":
				WebDriverManager.firefoxdriver().setup();
				FirefoxOptions fireoptions = new FirefoxOptions(); 
				
				String firefoxHeadlessMode = CommonVariable.HEADLESS_MODE != null ? CommonVariable.HEADLESS_MODE : "true";
				boolean isFirefoxHeadless = Boolean.parseBoolean(firefoxHeadlessMode);
				
				if (isFirefoxHeadless) {
					LOGGER.info("Running Firefox in HEADLESS mode");
					fireoptions.addArguments("--headless");
					fireoptions.addArguments("--width=1920");
					fireoptions.addArguments("--height=1080");
				} else {
					LOGGER.info("Running Firefox in WINDOWED mode");
				}
				
				// Common stability options for Firefox
				fireoptions.addArguments("--disable-gpu");
				fireoptions.addArguments("--no-sandbox");
				
				// Session stability preferences
				fireoptions.addPreference("dom.webnotifications.enabled", false);
				fireoptions.addPreference("media.volume_scale", "0.0");
				fireoptions.addPreference("browser.sessionstore.resume_from_crash", false);
				
				driver = new FirefoxDriver(fireoptions);
				LOGGER.info("Firefox launched successfully in {} mode", isFirefoxHeadless ? "HEADLESS" : "WINDOWED");
				break;
			case "edge":
				WebDriverManager.edgedriver().setup();
				driver = new EdgeDriver();
				LOGGER.info("edge launched successfully");
				break;

			default:
				WebDriverManager.chromedriver().setup();
				driver = new ChromeDriver();
				LOGGER.info("chrome launched successfully");
				break;
			}
			
			driver.manage().deleteAllCookies();
			try {
				// Only maximize if not in headless mode to prevent session issues
				String browser = CommonVariable.BROWSER;
				if (!"headless".equalsIgnoreCase(browser)) {
					driver.manage().window().maximize();
				}
			} catch (Exception e) {
				LOGGER.warn("Window maximization failed (normal in headless mode): {}", e.getMessage());
			}
			
			// UNATTENDED EXECUTION: Enhanced timeout configuration for headless stability
			if (Boolean.parseBoolean(CommonVariable.HEADLESS_MODE != null ? CommonVariable.HEADLESS_MODE : "true")) {
				// Longer timeouts for unattended headless execution (prevents session timeouts)
				driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(120)); // Increased for stability
				driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));    // Increased for reliability
				driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(90));     // Added for async operations
				wait = new WebDriverWait(driver, Duration.ofSeconds(180));            // Extended for complex operations
				LOGGER.info("HEADLESS MODE: Extended timeouts configured for session stability");
			} else {
				// Standard timeouts for windowed debugging mode
				driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
				driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
				driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));
				wait = new WebDriverWait(driver, Duration.ofSeconds(60));
				LOGGER.info("WINDOWED MODE: Standard timeouts configured for debugging");
			}

		} catch (Exception e) {

		}
	}

	public static WebDriverWait getWait() {
		return wait;
	}

	public static void setWait(WebDriverWait wait) {
		DriverManager.wait = wait;
	}

	public static WebDriver getDriver() {
		// Ensure driver is initialized before returning
		if (driver == null) {
			LOGGER.warn("Driver is null in getDriver() - attempting to initialize");
			launchBrowser();
		}
		return driver;
	}
	
	public static boolean isSessionActive() {
		if (driver == null) {
			return false;
		}
		
		try {
			// Check session ID first to avoid accessing an inactive session
			if (driver.toString().contains("null")) {
				return false;
			}
			
			// Multiple checks to ensure session is truly active
			String title = driver.getTitle();
			String currentUrl = driver.getCurrentUrl();
			LOGGER.debug("Session check passed - Title: {}, URL: {}", title, currentUrl != null ? currentUrl.substring(0, Math.min(50, currentUrl.length())) + "..." : "null");
			return true;
		} catch (org.openqa.selenium.NoSuchSessionException e) {
			// Session was explicitly closed/quit - don't log as warning
			LOGGER.debug("Session no longer exists (expected after quit)");
			return false;
		} catch (org.openqa.selenium.WebDriverException e) {
			// Only warn for unexpected WebDriver exceptions
			if (!e.getMessage().contains("Session ID is null")) {
				LOGGER.warn("Session check failed: {} - Session is inactive", e.getMessage());
			}
			return false;
		} catch (Exception e) {
			LOGGER.debug("Session check failed: {}", e.getMessage());
			return false;
		}
	}
	
	public static boolean recoverSession() {
		if (!isSessionActive()) {
			LOGGER.warn("SESSION RECOVERY: Attempting to recover inactive session...");
			
			
			try {
				// Close existing driver if it exists
				if (driver != null) {
					try {
						driver.quit();
					} catch (Exception e) {
						LOGGER.debug("Error closing inactive driver: {}", e.getMessage());
					}
					driver = null;
				}
				
				// Reinitialize browser
				launchBrowser();
				
				if (isSessionActive()) {
					LOGGER.info("SESSION RECOVERY: Successfully recovered browser session with network validation");
					return true;
				} else {
					LOGGER.error("SESSION RECOVERY: Failed to recover browser session despite network connectivity");
					return false;
				}
			} catch (Exception e) {
				LOGGER.error("SESSION RECOVERY: Error during session recovery - {}", e.getMessage());
				return false;
			}
		}
		return true; // Session is already active
	}
	
	public static <T> T executeWithRecovery(java.util.function.Supplier<T> operation, String operationName) {
		return executeWithRecovery(operation, operationName, 3);
	}
	
	public static <T> T executeWithRecovery(java.util.function.Supplier<T> operation, String operationName, int maxRetries) {
		for (int attempt = 1; attempt <= maxRetries; attempt++) {
			try {
				// Check if session is active before executing operation
				if (!isSessionActive() && attempt == 1) {
					LOGGER.warn("Session is inactive before executing: {}, attempting recovery...", operationName);
					recoverSession();
				}
				
				// Execute the operation
				T result = operation.get();
				
				if (attempt > 1) {
					LOGGER.info("Operation '{}' succeeded after {} attempts", operationName, attempt);
				}
				
				return result;
				
			} catch (org.openqa.selenium.WebDriverException e) {
				
				LOGGER.warn("Session error during '{}' (attempt {}/{}): {}", 
					operationName, attempt, maxRetries, e.getMessage());
				
				if (attempt < maxRetries) {
					try {
						LOGGER.info("Attempting session recovery for operation: {}", operationName);
						recoverSession();
						
						PerformanceUtils.waitForPageReady(driver, 1);
					} catch (Exception recoveryException) {
						LOGGER.error("Session recovery failed for operation '{}': {}", 
							operationName, recoveryException.getMessage());
						
						if (attempt == maxRetries) {
							throw new RuntimeException("Failed to recover session after " + maxRetries + " attempts for operation: " + operationName, e);
						}
					}
				} else {
					LOGGER.error("Operation '{}' failed after {} attempts, giving up", operationName, maxRetries);
					throw new RuntimeException("Operation failed after session recovery attempts: " + operationName, e);
				}
			}
		}
		
		throw new RuntimeException("Unexpected state in executeWithRecovery for: " + operationName);
	}
	
	public static void executeWithRecovery(Runnable operation, String operationName) {
		executeWithRecovery(() -> {
			operation.run();
			return null;
		}, operationName);
	}
	
	/**
	 * Properly closes the browser and cleans up the driver instance
	 * This method should be called in @AfterTest to ensure clean shutdown
	 */
	public static void closeBrowser() {
		if (driver != null) {
			try {
				driver.quit();
				LOGGER.info("Browser closed successfully");
			} catch (Exception e) {
				LOGGER.debug("Error during browser closure (may already be closed): {}", e.getMessage());
			} finally {
				// Always set driver to null after quit to prevent session check warnings
				driver = null;
			}
		} else {
			LOGGER.debug("Browser was already closed or never initialized");
		}
	}
		
}
