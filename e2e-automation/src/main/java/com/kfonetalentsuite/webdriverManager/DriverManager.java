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
		// Check if driver is already initialized AND session is active
		if (driver.get() != null && isSessionActive()) {
			return; // Already initialized, skip
		}
		
		// Clean up inactive driver if exists
		if (driver.get() != null && !isSessionActive()) {
			try {
				driver.get().quit();
			} catch (Exception e) {
				// Ignore cleanup errors
			}
			driver.set(null);
		}
		
	try {
	boolean isHeadless = Boolean.parseBoolean(
		CommonVariable.HEADLESS_MODE != null ? CommonVariable.HEADLESS_MODE : "true"
	);
	
	// Launch browser based on configuration with retry mechanism
	int maxRetries = 3;
	boolean browserLaunched = false;
	Exception lastException = null;
	
	for (int attempt = 1; attempt <= maxRetries && !browserLaunched; attempt++) {
		try {
			if (attempt > 1) {
				LOGGER.debug("Browser launch attempt {}/{} - Thread: {}", attempt, maxRetries, Thread.currentThread().getId());
			}
			
	switch (CommonVariable.BROWSER) {
			case "chrome":
					setupChromeDriver();
					driver.set(new ChromeDriver(configureChromeOptions(isHeadless)));
					break;
				case "firefox":
					setupFirefoxDriver();
					driver.set(new FirefoxDriver(configureFirefoxOptions(isHeadless)));
					break;
				case "edge":
					setupEdgeDriver();
					driver.set(new EdgeDriver());
					break;
				default:
					setupChromeDriver();
					driver.set(new ChromeDriver(configureChromeOptions(isHeadless)));
					break;
			}
			browserLaunched = true;
			if (attempt > 1) {
				LOGGER.debug("✓ Browser launched successfully on attempt {}/{}", attempt, maxRetries);
			}
		} catch (Exception e) {
			lastException = e;
			LOGGER.warn("✗ Browser launch attempt {}/{} failed: {}", attempt, maxRetries, e.getMessage());
			
			// Clean up failed driver instance
			if (driver.get() != null) {
				try {
					driver.get().quit();
				} catch (Exception quitEx) {
					// Ignore cleanup errors
				}
				driver.set(null);
			}
			
			if (attempt < maxRetries) {
				// Progressive backoff: 3s, 6s, 10s
				int delaySec = (attempt == 1) ? 3 : (attempt == 2) ? 6 : 10;
				try {
					LOGGER.debug("Retrying browser launch after {} seconds...", delaySec);
					Thread.sleep(delaySec * 1000);
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
					LOGGER.warn("Retry delay interrupted");
				}
			}
		}
	}
	
	if (!browserLaunched) {
		LOGGER.error("✗ Failed to launch browser after {} attempts", maxRetries);
		throw new RuntimeException("Browser initialization failed after " + maxRetries + " attempts", lastException);
	}
	
	LOGGER.info("✓ Browser launched: {} ({})", 
		CommonVariable.BROWSER, isHeadless ? "headless" : "windowed");
	
	// Configure timeouts and browser
	configureTimeoutsAndBrowser(isHeadless);
	
} catch (Exception e) {
	LOGGER.error("✗ Browser launch failed: {}", e.getMessage());
	throw new RuntimeException("Browser initialization failed", e);
}
	}
	
	// Static flag to ensure driver is downloaded only once
	private static volatile boolean driverDownloaded = false;
	private static final Object driverLock = new Object();
	
	/**
	 * Setup ChromeDriver with parallel execution optimization
	 * Synchronized to prevent race conditions during parallel test execution
	 */
	private static synchronized void setupChromeDriver() {
		// If driver already downloaded, skip setup
		if (driverDownloaded) {
			return;
		}
		
		synchronized (driverLock) {
			// Double-check after acquiring lock
			if (driverDownloaded) {
				return;
			}
			
			try {
				// Determine cache path (handle both Linux and Windows)
				String cacheDir = System.getProperty("user.home") + File.separator + ".cache" + File.separator + "selenium";
				
				WebDriverManager.chromedriver()
					.cachePath(cacheDir)          // Use explicit cache path
					.timeout(180)                 // Increased timeout for slow networks/CI
					.avoidShutdownHook()         // Prevent shutdown conflicts in parallel execution
					.setup();
					
				driverDownloaded = true;
				LOGGER.info("✓ ChromeDriver setup completed");
			} catch (Exception e) {
				LOGGER.warn("ChromeDriver setup failed, retrying...: {}", e.getMessage());
				
				// Fallback: Try with default settings
				try {
					Thread.sleep(2000); // Brief pause before retry
					WebDriverManager.chromedriver()
						.timeout(180)
						.avoidShutdownHook()
						.setup();
					driverDownloaded = true;
					LOGGER.info("✓ ChromeDriver setup completed with fallback");
				} catch (Exception fallbackException) {
					LOGGER.error("✗ ChromeDriver setup failed", fallbackException);
					throw new RuntimeException("Failed to setup ChromeDriver after retry", fallbackException);
				}
			}
		}
	}
	
	/**
	 * Setup FirefoxDriver with parallel execution optimization
	 */
	private static synchronized void setupFirefoxDriver() {
		try {
			String cacheDir = System.getProperty("user.home") + File.separator + ".cache" + File.separator + "selenium";
			WebDriverManager.firefoxdriver()
				.cachePath(cacheDir)
				.timeout(180)
				.avoidShutdownHook()
				.setup();
			LOGGER.info("✓ FirefoxDriver setup completed");
		} catch (Exception e) {
			LOGGER.error("✗ FirefoxDriver setup failed", e);
			throw new RuntimeException("Failed to setup FirefoxDriver", e);
		}
	}
	
	/**
	 * Setup EdgeDriver with parallel execution optimization
	 */
	private static synchronized void setupEdgeDriver() {
		try {
			String cacheDir = System.getProperty("user.home") + File.separator + ".cache" + File.separator + "selenium";
			WebDriverManager.edgedriver()
				.cachePath(cacheDir)
				.timeout(180)
				.avoidShutdownHook()
				.setup();
			LOGGER.info("✓ EdgeDriver setup completed");
		} catch (Exception e) {
			LOGGER.error("✗ EdgeDriver setup failed", e);
			throw new RuntimeException("Failed to setup EdgeDriver", e);
		}
	}
	
	/**
	 * Configure Chrome options with all necessary arguments
	 * PARALLEL EXECUTION OPTIMIZED: Isolates each Chrome instance with unique user-data-dir
	 */
	private static ChromeOptions configureChromeOptions(boolean isHeadless) {
		ChromeOptions options = new ChromeOptions();
		
		// CRITICAL FOR PARALLEL EXECUTION: Isolate user data directory per thread
		// This prevents port conflicts and profile locking issues
		long threadId = Thread.currentThread().getId();
		String userDataDir = System.getProperty("user.dir") + File.separator + "chrome-profile-" + threadId;
		options.addArguments("--user-data-dir=" + userDataDir);
		
		// Download directory configuration
		Map<String, Object> prefs = new HashMap<>();
		prefs.put("download.default_directory", 
			System.getProperty("user.dir") + File.separator + "externalFiles" + File.separator + "downloadFiles");
		prefs.put("profile.default_content_settings.popups", 0);
		prefs.put("profile.default_content_setting_values.automatic_downloads", 1);
		prefs.put("download.prompt_for_download", false);
		options.setExperimentalOption("prefs", prefs);
		
		// Core Chrome arguments (deduplicated)
		options.addArguments(
			"--remote-allow-origins=*",
			"--incognito",  // Clean state between runs
			"--no-sandbox",
			"--disable-dev-shm-usage",
			"--disable-gpu",
			"--disable-extensions",
			"--disable-hang-monitor",
			"--disable-prompt-on-repost",
			"--disable-default-apps",
			"--disable-popup-blocking",
			"--disable-translate",
			"--disable-infobars",
			"--disable-web-security",
			"--disable-features=TranslateUI",
			"--disable-background-timer-throttling",
			"--disable-backgrounding-occluded-windows",
			"--disable-renderer-backgrounding",
			"--disable-background-media-audio",
			"--ignore-ssl-errors=yes",
			"--ignore-certificate-errors",
			"--ignore-certificate-errors-spki-list",
			"--allow-running-insecure-content"
		);
		
		// Headless-specific configuration
			if (isHeadless) {
			options.addArguments("--headless=new", "--window-size=1920,1080", "--disable-software-rasterizer");
			} else {
				options.addArguments("--start-maximized");
			}
				
		return options;
	}
	
	/**
	 * Configure Firefox options
	 * Extracted to reduce code duplication
	 */
	private static FirefoxOptions configureFirefoxOptions(boolean isHeadless) {
		FirefoxOptions options = new FirefoxOptions();
		
		// Core Firefox arguments
		options.addArguments("--disable-gpu", "--no-sandbox");
		
		// Headless configuration
		if (isHeadless) {
			options.addArguments("--headless", "--width=1920", "--height=1080");
		}
		
		// Firefox preferences
		options.addPreference("dom.webnotifications.enabled", false);
		options.addPreference("media.volume_scale", "0.0");
		options.addPreference("browser.sessionstore.resume_from_crash", false);
		
		return options;
	}
	
	/**
	 * Configure timeouts and browser window settings
	 */
	private static void configureTimeoutsAndBrowser(boolean isHeadless) {
		WebDriver currentDriver = driver.get();
		
		// Set timeouts based on mode
		if (isHeadless) {
			currentDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(180));
			currentDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
			currentDriver.manage().timeouts().scriptTimeout(Duration.ofSeconds(120));
			wait.set(new WebDriverWait(currentDriver, Duration.ofSeconds(90)));
			} else {
			currentDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
			currentDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
			currentDriver.manage().timeouts().scriptTimeout(Duration.ofSeconds(45));
			wait.set(new WebDriverWait(currentDriver, Duration.ofSeconds(60)));
		}
		
		// Clean browser state and maximize window
		currentDriver.manage().deleteAllCookies();
		if (!isHeadless) {
			try {
				currentDriver.manage().window().maximize();
			} catch (Exception e) {
				// Ignore maximization errors
			}
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
	 * Clears all browser data for clean test state
	 */
	public static void clearAllBrowserData() {
		WebDriver currentDriver = driver.get();
		if (currentDriver == null) {
			return;
		}
		
		try {
			org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) currentDriver;
			
			// Clear all browser data
			currentDriver.manage().deleteAllCookies();
			executeJavaScriptSilently(js, 
				"try { window.localStorage.clear(); } catch(e) {}" +
				"try { window.sessionStorage.clear(); } catch(e) {}" +
				"try { if (window.indexedDB && window.indexedDB.databases) {" +
				"  window.indexedDB.databases().then(dbs => {" +
				"    dbs.forEach(db => { window.indexedDB.deleteDatabase(db.name); });" +
				"  });" +
				"}} catch(e) {}"
			);
			executeActionSilently(() -> currentDriver.get("about:blank"));
			
		} catch (Exception e) {
			LOGGER.warn("Cache clear failed: {}", e.getMessage());
		}
	}
	
	/**
	 * Helper method to execute JavaScript silently (ignore errors)
	 */
	private static void executeJavaScriptSilently(org.openqa.selenium.JavascriptExecutor js, String script) {
		try {
			js.executeScript(script);
		} catch (Exception e) {
			// Silently ignore - expected for some storage types
		}
	}
	
	/**
	 * Helper method to execute actions silently (ignore errors)
	 */
	private static void executeActionSilently(Runnable action) {
		try {
			action.run();
		} catch (Exception e) {
			// Silently ignore
		}
	}
	
	/**
	 * Quick cache clear - lightweight version for between test steps
	 */
	public static void clearCookiesAndStorage() {
		if (driver.get() == null) {
			return;
		}
		
		try {
			driver.get().manage().deleteAllCookies();
			((org.openqa.selenium.JavascriptExecutor) driver.get()).executeScript(
				"window.localStorage.clear(); window.sessionStorage.clear();"
			);
		} catch (Exception e) {
			// Ignore errors
		}
	}

	/**
	 * Gets the WebDriver instance for the current thread
	 */
	public static WebDriver getDriver() {
		if (driver.get() == null) {
			launchBrowser();
		}
		return driver.get();
	}
	
	/**
	 * Checks if the WebDriver session is active
	 */
	public static boolean isSessionActive() {
		if (driver.get() == null) {
			return false;
		}
		
		try {
			if (driver.get().toString().contains("null")) {
				return false;
			}
			driver.get().getTitle(); // Test if session is alive
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Attempts to recover an inactive WebDriver session
	 */
	public static boolean recoverSession() {
		if (!isSessionActive()) {
			try {
				if (driver.get() != null) {
					try {
						driver.get().quit();
					} catch (Exception e) {
						// Ignore
					}
					driver.set(null);
				}
				launchBrowser();
				return isSessionActive();
			} catch (Exception e) {
				LOGGER.error("Session recovery failed: {}", e.getMessage());
				return false;
			}
		}
		return true;
	}
	
	public static <T> T executeWithRecovery(java.util.function.Supplier<T> operation, String operationName) {
		return executeWithRecovery(operation, operationName, 3);
	}
	
	/**
	 * Executes an operation with automatic session recovery
	 */
	public static <T> T executeWithRecovery(java.util.function.Supplier<T> operation, String operationName, int maxRetries) {
		for (int attempt = 1; attempt <= maxRetries; attempt++) {
			try {
				if (!isSessionActive() && attempt == 1) {
					recoverSession();
				}
				return operation.get();
			} catch (org.openqa.selenium.WebDriverException e) {
				if (attempt < maxRetries) {
					try {
						recoverSession();
						PerformanceUtils.waitForPageReady(driver.get(), 1);
					} catch (Exception recoveryException) {
						if (attempt == maxRetries) {
							throw new RuntimeException("Session recovery failed for: " + operationName, e);
						}
					}
				} else {
					throw new RuntimeException("Operation failed after " + maxRetries + " attempts: " + operationName, e);
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
	 * Closes the browser and cleans up resources
	 * PARALLEL EXECUTION OPTIMIZED: Also cleans up isolated Chrome profile directory
	 */
	public static void closeBrowser() {
		if (driver.get() != null) {
			try {
				driver.get().quit();
			} catch (Exception e) {
				// Ignore errors during close
			} finally {
				driver.remove();
				wait.remove();
				cleanupChromeProfile();
			}
		} else {
			driver.remove();
			wait.remove();
			cleanupChromeProfile();
		}
	}
	
	/**
	 * Cleans up the isolated Chrome profile directory created for this thread
	 * Prevents disk space issues in parallel execution
	 */
	private static void cleanupChromeProfile() {
		try {
			long threadId = Thread.currentThread().getId();
			String userDataDir = System.getProperty("user.dir") + File.separator + "chrome-profile-" + threadId;
			File profileDir = new File(userDataDir);
			
			if (profileDir.exists()) {
				deleteDirectory(profileDir);
			}
		} catch (Exception e) {
			LOGGER.warn("Failed to cleanup Chrome profile directory: {}", e.getMessage());
		}
	}
	
	/**
	 * Recursively deletes a directory and its contents
	 */
	private static void deleteDirectory(File directory) {
		File[] files = directory.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					deleteDirectory(file);
				} else {
					file.delete();
				}
			}
		}
		directory.delete();
	}
	
	/**
	 * Force cleanup of all Chrome/ChromeDriver processes
	 */
	public static void forceKillChromeProcesses() {
		try {
			Process killChrome = Runtime.getRuntime().exec("taskkill /F /IM chrome.exe /T");
			killChrome.waitFor(2, java.util.concurrent.TimeUnit.SECONDS);
			
			Process killChromeDriver = Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe /T");
			killChromeDriver.waitFor(2, java.util.concurrent.TimeUnit.SECONDS);
			
			Thread.sleep(500);
		} catch (Exception e) {
			// Ignore - no processes to clean
		}
	}
		
}
