package com.kfonetalentsuite.webdriverManager;

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


import com.kfonetalentsuite.utils.common.CommonVariable;
import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;


import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverManager {
	// THREAD-SAFE: Each thread gets its own WebDriver instance for parallel execution
	private static ThreadLocal<WebDriver> driver = ThreadLocal.withInitial(() -> null);
	protected static final Logger LOGGER = (Logger) LogManager.getLogger();
	
	// THREAD-SAFE: Each thread gets its own WebDriverWait instance
	private static ThreadLocal<WebDriverWait> wait = ThreadLocal.withInitial(() -> null);
	
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
		// Get thread ID for logging
		long threadId = Thread.currentThread().getId();
		String threadName = Thread.currentThread().getName();
		
		// Check if driver is already initialized AND session is active
		if (driver.get() != null && isSessionActive()) {
			LOGGER.info("[Thread-{}:{}] Driver already initialized with active session, skipping browser launch", threadId, threadName);
			return;
		}
		
		// If driver exists but session is inactive, clean it up
		if (driver.get() != null && !isSessionActive()) {
			LOGGER.warn("[Thread-{}:{}] Driver exists but session is inactive - cleaning up before re-initialization", threadId, threadName);
			try {
				driver.get().quit();
			} catch (Exception e) {
				LOGGER.debug("[Thread-{}:{}] Error during driver cleanup: {}", threadId, threadName, e.getMessage());
			}
			driver.set(null);
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
			
			// PERFORMANCE OPTIMIZATION: Always use incognito mode for clean state between runs
			options.addArguments("--incognito");
			LOGGER.debug("Chrome launched in incognito mode for clean browser state");
			
			if (isHeadless) {
				LOGGER.info("Running Chrome in HEADLESS mode");
				options.addArguments("--headless=new");
				options.addArguments("--window-size=1920,1080");
				// HEADLESS STABILITY FIXES
				options.addArguments("--disable-gpu"); // GPU issues in headless
				options.addArguments("--disable-dev-shm-usage"); // Overcome limited resource problems
				options.addArguments("--no-sandbox"); // Bypass OS security model (CI/CD)
				options.addArguments("--disable-software-rasterizer"); // Avoid rendering issues
				options.addArguments("--remote-allow-origins=*"); // CORS issues in headless
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
				driver.set(new ChromeDriver(options));
				LOGGER.info("[Thread-{}:{}] Chrome launched successfully", threadId, threadName);
				break;

			case "firefox":
				WebDriverManager.firefoxdriver().setup();
				FirefoxOptions fireoptions = new FirefoxOptions(); 
				
				String firefoxHeadlessMode = CommonVariable.HEADLESS_MODE != null ? CommonVariable.HEADLESS_MODE : "true";
				boolean isFirefoxHeadless = Boolean.parseBoolean(firefoxHeadlessMode);
				
				if (isFirefoxHeadless) {
					LOGGER.info("[Thread-{}:{}] Running Firefox in HEADLESS mode", threadId, threadName);
					fireoptions.addArguments("--headless");
					fireoptions.addArguments("--width=1920");
					fireoptions.addArguments("--height=1080");
				} else {
					LOGGER.info("[Thread-{}:{}] Running Firefox in WINDOWED mode", threadId, threadName);
				}
				
				// Common stability options for Firefox
				fireoptions.addArguments("--disable-gpu");
				fireoptions.addArguments("--no-sandbox");
				
				// Session stability preferences
				fireoptions.addPreference("dom.webnotifications.enabled", false);
				fireoptions.addPreference("media.volume_scale", "0.0");
				fireoptions.addPreference("browser.sessionstore.resume_from_crash", false);
				
				driver.set(new FirefoxDriver(fireoptions));
				LOGGER.info("[Thread-{}:{}] Firefox launched successfully in {} mode", threadId, threadName, isFirefoxHeadless ? "HEADLESS" : "WINDOWED");
				break;
			case "edge":
				WebDriverManager.edgedriver().setup();
				driver.set(new EdgeDriver());
				LOGGER.info("[Thread-{}:{}] Edge launched successfully", threadId, threadName);
				break;

			default:
				WebDriverManager.chromedriver().setup();
				driver.set(new ChromeDriver());
				LOGGER.info("[Thread-{}:{}] Chrome launched successfully", threadId, threadName);
				break;
			}
			
			// PERFORMANCE OPTIMIZATION: Configure timeouts based on execution mode
			String headlessModeCheck = CommonVariable.HEADLESS_MODE != null ? CommonVariable.HEADLESS_MODE : "true";
			boolean isHeadlessModeActive = Boolean.parseBoolean(headlessModeCheck);
			
			if (isHeadlessModeActive) {
				// Increased timeouts for headless mode stability (slower JavaScript execution)
				driver.get().manage().timeouts().pageLoadTimeout(java.time.Duration.ofSeconds(120)); // Increased from default 60s
				driver.get().manage().timeouts().scriptTimeout(java.time.Duration.ofSeconds(90)); // Increased for slow AJAX calls
				driver.get().manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(15)); // Slightly increased for element waits
				LOGGER.debug("[Thread-{}:{}] Configured extended timeouts for headless mode (page: 120s, script: 90s, implicit: 15s)", threadId, threadName);
			} else {
				// Standard timeouts for windowed mode
				driver.get().manage().timeouts().pageLoadTimeout(java.time.Duration.ofSeconds(60));
				driver.get().manage().timeouts().scriptTimeout(java.time.Duration.ofSeconds(45));
				driver.get().manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(10));
				LOGGER.debug("[Thread-{}:{}] Configured standard timeouts for windowed mode", threadId, threadName);
			}
			
			driver.get().manage().deleteAllCookies();
			try {
				// Only maximize if not in headless mode to prevent session issues
				String browser = CommonVariable.BROWSER;
				if (!"headless".equalsIgnoreCase(browser)) {
					driver.get().manage().window().maximize();
				}
			} catch (Exception e) {
				LOGGER.warn("[Thread-{}:{}] Window maximization failed (normal in headless mode): {}", threadId, threadName, e.getMessage());
			}
			
			// UNATTENDED EXECUTION: Enhanced timeout configuration for headless stability
			if (Boolean.parseBoolean(CommonVariable.HEADLESS_MODE != null ? CommonVariable.HEADLESS_MODE : "true")) {
				// Longer timeouts for unattended headless execution (prevents session timeouts)
				driver.get().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(120)); // Increased for stability
				driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));    // Increased for reliability
				driver.get().manage().timeouts().scriptTimeout(Duration.ofSeconds(90));     // Added for async operations
				wait.set(new WebDriverWait(driver.get(), Duration.ofSeconds(180)));        // Extended for complex operations
				LOGGER.info("[Thread-{}:{}] HEADLESS MODE: Extended timeouts configured for session stability", threadId, threadName);
			} else {
				// Standard timeouts for windowed debugging mode
				driver.get().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
				driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
				driver.get().manage().timeouts().scriptTimeout(Duration.ofSeconds(30));
				wait.set(new WebDriverWait(driver.get(), Duration.ofSeconds(60)));
				LOGGER.info("[Thread-{}:{}] WINDOWED MODE: Standard timeouts configured for debugging", threadId, threadName);
			}

		} catch (Exception e) {

		}
	}

	/**
	 * THREAD-SAFE: Gets the WebDriverWait instance for the current thread
	 */
	public static WebDriverWait getWait() {
		return wait.get();
	}

	/**
	 * THREAD-SAFE: Sets the WebDriverWait instance for the current thread
	 */
	public static void setWait(WebDriverWait newWait) {
		wait.set(newWait);
	}

	/**
	 * THREAD-SAFE: Gets the WebDriver instance for the current thread
	 * Each thread maintains its own isolated WebDriver instance
	 */
	public static WebDriver getDriver() {
		// Ensure driver is initialized before returning
		if (driver.get() == null) {
			long threadId = Thread.currentThread().getId();
			String threadName = Thread.currentThread().getName();
			LOGGER.warn("[Thread-{}:{}] Driver is null in getDriver() - attempting to initialize", threadId, threadName);
			launchBrowser();
		}
		return driver.get();
	}
	
	/**
	 * THREAD-SAFE: Checks if the WebDriver session is active for the current thread
	 */
	public static boolean isSessionActive() {
		if (driver.get() == null) {
			return false;
		}
		
		long threadId = Thread.currentThread().getId();
		String threadName = Thread.currentThread().getName();
		
		try {
			// Check session ID first to avoid accessing an inactive session
			if (driver.get().toString().contains("null")) {
				return false;
			}
			
			// Multiple checks to ensure session is truly active
			String title = driver.get().getTitle();
			String currentUrl = driver.get().getCurrentUrl();
			LOGGER.debug("[Thread-{}:{}] Session check passed - Title: {}, URL: {}", threadId, threadName, title, currentUrl != null ? currentUrl.substring(0, Math.min(50, currentUrl.length())) + "..." : "null");
			return true;
		} catch (org.openqa.selenium.NoSuchSessionException e) {
			// Session was explicitly closed/quit - don't log as warning
			LOGGER.debug("[Thread-{}:{}] Session no longer exists (expected after quit)", threadId, threadName);
			return false;
		} catch (org.openqa.selenium.WebDriverException e) {
			// Only warn for unexpected WebDriver exceptions
			if (!e.getMessage().contains("Session ID is null")) {
				LOGGER.warn("[Thread-{}:{}] Session check failed: {} - Session is inactive", threadId, threadName, e.getMessage());
			}
			return false;
		} catch (Exception e) {
			LOGGER.debug("[Thread-{}:{}] Session check failed: {}", threadId, threadName, e.getMessage());
			return false;
		}
	}
	
	/**
	 * THREAD-SAFE: Attempts to recover an inactive WebDriver session for the current thread
	 */
	public static boolean recoverSession() {
		long threadId = Thread.currentThread().getId();
		String threadName = Thread.currentThread().getName();
		
		if (!isSessionActive()) {
			LOGGER.warn("[Thread-{}:{}] SESSION RECOVERY: Attempting to recover inactive session...", threadId, threadName);
			
			try {
				// Close existing driver if it exists
				if (driver.get() != null) {
					try {
						driver.get().quit();
					} catch (Exception e) {
						LOGGER.debug("[Thread-{}:{}] Error closing inactive driver: {}", threadId, threadName, e.getMessage());
					}
					driver.set(null);
				}
				
				// Reinitialize browser
				launchBrowser();
				
				if (isSessionActive()) {
					LOGGER.info("[Thread-{}:{}] SESSION RECOVERY: Successfully recovered browser session", threadId, threadName);
					return true;
				} else {
					LOGGER.error("[Thread-{}:{}] SESSION RECOVERY: Failed to recover browser session", threadId, threadName);
					return false;
				}
			} catch (Exception e) {
				LOGGER.error("[Thread-{}:{}] SESSION RECOVERY: Error during session recovery - {}", threadId, threadName, e.getMessage());
				return false;
			}
		}
		return true; // Session is already active
	}
	
	public static <T> T executeWithRecovery(java.util.function.Supplier<T> operation, String operationName) {
		return executeWithRecovery(operation, operationName, 3);
	}
	
	/**
	 * THREAD-SAFE: Executes an operation with automatic session recovery for the current thread
	 */
	public static <T> T executeWithRecovery(java.util.function.Supplier<T> operation, String operationName, int maxRetries) {
		long threadId = Thread.currentThread().getId();
		String threadName = Thread.currentThread().getName();
		
		for (int attempt = 1; attempt <= maxRetries; attempt++) {
			try {
				// Check if session is active before executing operation
				if (!isSessionActive() && attempt == 1) {
					LOGGER.warn("[Thread-{}:{}] Session is inactive before executing: {}, attempting recovery...", threadId, threadName, operationName);
					recoverSession();
				}
				
				// Execute the operation
				T result = operation.get();
				
				if (attempt > 1) {
					LOGGER.info("[Thread-{}:{}] Operation '{}' succeeded after {} attempts", threadId, threadName, operationName, attempt);
				}
				
				return result;
				
			} catch (org.openqa.selenium.WebDriverException e) {
				
				LOGGER.warn("[Thread-{}:{}] Session error during '{}' (attempt {}/{}): {}", 
					threadId, threadName, operationName, attempt, maxRetries, e.getMessage());
				
				if (attempt < maxRetries) {
					try {
						LOGGER.info("[Thread-{}:{}] Attempting session recovery for operation: {}", threadId, threadName, operationName);
						recoverSession();
						
						PerformanceUtils.waitForPageReady(driver.get(), 1);
					} catch (Exception recoveryException) {
						LOGGER.error("[Thread-{}:{}] Session recovery failed for operation '{}': {}", 
							threadId, threadName, operationName, recoveryException.getMessage());
						
						if (attempt == maxRetries) {
							throw new RuntimeException("Failed to recover session after " + maxRetries + " attempts for operation: " + operationName, e);
						}
					}
				} else {
					LOGGER.error("[Thread-{}:{}] Operation '{}' failed after {} attempts, giving up", threadId, threadName, operationName, maxRetries);
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
	 * THREAD-SAFE: Properly closes the browser and cleans up the driver instance for the current thread
	 * This method should be called in @AfterTest to ensure clean shutdown
	 */
	public static void closeBrowser() {
		long threadId = Thread.currentThread().getId();
		String threadName = Thread.currentThread().getName();
		
		if (driver.get() != null) {
			try {
				driver.get().quit();
				LOGGER.info("[Thread-{}:{}] Browser closed successfully", threadId, threadName);
			} catch (Exception e) {
				LOGGER.debug("[Thread-{}:{}] Error during browser closure (may already be closed): {}", threadId, threadName, e.getMessage());
			} finally {
				// CRITICAL: Remove ThreadLocal values to prevent memory leaks when threads are reused
				driver.remove();
				wait.remove();
				LOGGER.debug("[Thread-{}:{}] ThreadLocal variables cleaned up", threadId, threadName);
			}
		} else {
			LOGGER.debug("[Thread-{}:{}] Browser was already closed or never initialized", threadId, threadName);
			// Still clean up ThreadLocal even if driver was null
			driver.remove();
			wait.remove();
		}
	}
	
	/**
	 * PERFORMANCE OPTIMIZATION: Force cleanup of all Chrome/ChromeDriver processes
	 * This ensures clean state between test suite executions and prevents process accumulation
	 * Call this in @AfterSuite to prevent second run slowdown
	 * 
	 * @implNote Windows-specific implementation using taskkill
	 */
	public static void forceKillChromeProcesses() {
		try {
			LOGGER.info("Force terminating all Chrome and ChromeDriver processes...");
			
			// Kill all Chrome processes (forcefully, including children)
			Process killChrome = Runtime.getRuntime().exec("taskkill /F /IM chrome.exe /T");
			killChrome.waitFor(2, java.util.concurrent.TimeUnit.SECONDS);
			
			// Kill all ChromeDriver processes
			Process killChromeDriver = Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe /T");
			killChromeDriver.waitFor(2, java.util.concurrent.TimeUnit.SECONDS);
			
			// Short pause to ensure processes are terminated
			Thread.sleep(500);
			
			LOGGER.info("✓ Chrome process cleanup completed");
		} catch (Exception e) {
			// This is expected if no Chrome processes are running - don't log as error
			LOGGER.debug("Chrome process cleanup completed (no processes found or already terminated)");
		}
	}
		
}
