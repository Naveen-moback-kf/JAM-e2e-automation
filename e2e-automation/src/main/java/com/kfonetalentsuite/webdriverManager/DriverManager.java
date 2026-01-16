package com.kfonetalentsuite.webdriverManager;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import com.kfonetalentsuite.utils.common.CommonVariableManager;
import com.kfonetalentsuite.utils.common.Utilities;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverManager {
	// THREAD-SAFE: Each thread gets its own WebDriver instance for parallel
	// execution
	private static ThreadLocal<WebDriver> driver = ThreadLocal.withInitial(() -> null);
	protected static final Logger LOGGER = LogManager.getLogger(DriverManager.class);

	// THREAD-SAFE: Each thread gets its own WebDriverWait instance
	private static ThreadLocal<WebDriverWait> wait = ThreadLocal.withInitial(() -> null);

	@BeforeMethod
	public void initializeDriver() {
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
			// Get browser config from config.properties
			boolean isHeadless = Boolean
					.parseBoolean(CommonVariableManager.HEADLESS_MODE != null ? CommonVariableManager.HEADLESS_MODE : "false");

			// Launch browser based on configuration with retry mechanism
			int maxRetries = 3;
			boolean browserLaunched = false;
			Exception lastException = null;

			for (int attempt = 1; attempt <= maxRetries && !browserLaunched; attempt++) {
				try {
					if (attempt > 1) {
						LOGGER.debug("Browser launch attempt {}/{} - Thread: {}", attempt, maxRetries,
								Thread.currentThread().threadId());
					}

					switch (CommonVariableManager.BROWSER) {
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
				throw new RuntimeException("Browser initialization failed after " + maxRetries + " attempts",
						lastException);
			}

			LOGGER.info("✓ Browser launched: {} ({})", CommonVariableManager.BROWSER, isHeadless ? "headless" : "windowed");

			// Configure timeouts and browser
			configureTimeoutsAndBrowser(isHeadless);

		} catch (Exception e) {
			LOGGER.error("✗ Browser launch failed: {}", e.getMessage());
			throw new RuntimeException("Browser initialization failed", e);
		}
	}

	// Static flags to ensure drivers are downloaded only once (volatile for thread visibility)
	private static volatile boolean chromeDriverDownloaded = false;
	private static volatile boolean firefoxDriverDownloaded = false;
	private static volatile boolean edgeDriverDownloaded = false;

	private static synchronized void setupChromeDriver() {
		if (chromeDriverDownloaded) {
			return;
		}

		try {
			String cacheDir = System.getProperty("user.home") + File.separator + ".cache" + File.separator + "selenium";
			WebDriverManager.chromedriver()
					.cachePath(cacheDir)
					.timeout(180)
					.avoidShutdownHook()
					.setup();

			chromeDriverDownloaded = true;
			LOGGER.info("✓ ChromeDriver setup completed");
		} catch (Exception e) {
			LOGGER.warn("ChromeDriver setup failed, retrying...: {}", e.getMessage());
			try {
				Thread.sleep(2000);
				WebDriverManager.chromedriver().timeout(180).avoidShutdownHook().setup();
				chromeDriverDownloaded = true;
				LOGGER.info("✓ ChromeDriver setup completed with fallback");
			} catch (Exception fallbackException) {
				LOGGER.error("✗ ChromeDriver setup failed", fallbackException);
				throw new RuntimeException("Failed to setup ChromeDriver after retry", fallbackException);
			}
		}
	}

	private static synchronized void setupFirefoxDriver() {
		if (firefoxDriverDownloaded) {
			return;
		}

		try {
			String cacheDir = System.getProperty("user.home") + File.separator + ".cache" + File.separator + "selenium";
			WebDriverManager.firefoxdriver()
					.cachePath(cacheDir)
					.timeout(180)
					.avoidShutdownHook()
					.setup();
			firefoxDriverDownloaded = true;
			LOGGER.info("✓ FirefoxDriver setup completed");
		} catch (Exception e) {
			LOGGER.error("✗ FirefoxDriver setup failed", e);
			throw new RuntimeException("Failed to setup FirefoxDriver", e);
		}
	}

	private static synchronized void setupEdgeDriver() {
		if (edgeDriverDownloaded) {
			return;
		}

		try {
			String cacheDir = System.getProperty("user.home") + File.separator + ".cache" + File.separator + "selenium";
			WebDriverManager.edgedriver()
					.cachePath(cacheDir)
					.timeout(180)
					.avoidShutdownHook()
					.setup();
			edgeDriverDownloaded = true;
			LOGGER.info("✓ EdgeDriver setup completed");
		} catch (Exception e) {
			LOGGER.error("✗ EdgeDriver setup failed", e);
			throw new RuntimeException("Failed to setup EdgeDriver", e);
		}
	}

	private static ChromeOptions configureChromeOptions(boolean isHeadless) {
		ChromeOptions options = new ChromeOptions();

		// Download directory configuration
		Map<String, Object> prefs = new HashMap<>();
		prefs.put("download.default_directory",
				System.getProperty("user.dir") + File.separator + "externalFiles" + File.separator + "downloadFiles");
		prefs.put("profile.default_content_settings.popups", 0);
		prefs.put("profile.default_content_setting_values.automatic_downloads", 1);
		prefs.put("download.prompt_for_download", false);
		
		// ANTI-BOT DETECTION: Hide automation flags from JavaScript
		prefs.put("credentials_enable_service", false);
		prefs.put("profile.password_manager_enabled", false);
		
		options.setExperimentalOption("prefs", prefs);
		
		// ANTI-BOT DETECTION: Critical - removes automation detection flags
		options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation", "enable-logging"});
		options.setExperimentalOption("useAutomationExtension", false);

		// Core Chrome arguments for stability
		options.addArguments(
				"--remote-allow-origins=*",
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
				"--allow-running-insecure-content",
				"--disable-crash-reporter",
				"--disable-in-process-stack-traces",
				// ANTI-BOT DETECTION: Additional stealth arguments
				"--disable-blink-features=AutomationControlled",
				"--disable-features=IsolateOrigins,site-per-process",
				"--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36"
		);

		// Headless-specific configuration
		if (isHeadless) {
			options.addArguments(
					"--headless=new",
					"--window-size=1920,1080",
					"--disable-software-rasterizer",
					// MEMORY OPTIMIZATION: Prevent TimeoutException in parallel execution
					"--memory-pressure-off",
					"--max-old-space-size=4096",
					"--disable-features=VizDisplayCompositor",
					"--single-process" // Reduces memory footprint significantly
			);
			// Note: --disable-gpu, --no-sandbox, --disable-dev-shm-usage already added above
		} else {
			options.addArguments("--start-maximized");
		}
		
		// TIMEOUT PREVENTION: Increase page unload timeout
		options.setPageLoadStrategy(org.openqa.selenium.PageLoadStrategy.NORMAL);

		return options;
	}

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

	public static WebDriverWait getWait() {
		return wait.get();
	}

	public static void setWait(WebDriverWait newWait) {
		wait.set(newWait);
	}

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
					"try { window.localStorage.clear(); } catch(e) {}"
							+ "try { window.sessionStorage.clear(); } catch(e) {}"
							+ "try { if (window.indexedDB && window.indexedDB.databases) {"
							+ "  window.indexedDB.databases().then(dbs => {"
							+ "    dbs.forEach(db => { window.indexedDB.deleteDatabase(db.name); });" + "  });"
							+ "}} catch(e) {}");
			executeActionSilently(() -> currentDriver.get("about:blank"));

		} catch (Exception e) {
			LOGGER.warn("Cache clear failed: {}", e.getMessage());
		}
	}

	private static void executeJavaScriptSilently(org.openqa.selenium.JavascriptExecutor js, String script) {
		try {
			js.executeScript(script);
		} catch (Exception e) {
			// Silently ignore - expected for some storage types
		}
	}

	private static void executeActionSilently(Runnable action) {
		try {
			action.run();
		} catch (Exception e) {
			// Silently ignore
		}
	}

	public static void clearCookiesAndStorage() {
		if (driver.get() == null) {
			return;
		}

		try {
			driver.get().manage().deleteAllCookies();
			((org.openqa.selenium.JavascriptExecutor) driver.get())
					.executeScript("window.localStorage.clear(); window.sessionStorage.clear();");
		} catch (Exception e) {
			// Ignore errors
		}
	}

	public static WebDriver getDriver() {
		if (driver.get() == null) {
			launchBrowser();
		}
		return driver.get();
	}

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

	public static <T> T executeWithRecovery(java.util.function.Supplier<T> operation, String operationName,
			int maxRetries) {
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
						Utilities.waitForPageReady(driver.get(), 1);
					} catch (Exception recoveryException) {
						if (attempt == maxRetries) {
							throw new RuntimeException("Session recovery failed for: " + operationName, e);
						}
					}
				} else {
					throw new RuntimeException("Operation failed after " + maxRetries + " attempts: " + operationName,
							e);
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

	private static void cleanupChromeProfile() {
		try {
			long threadId = Thread.currentThread().threadId();
			String userDataDir = System.getProperty("user.dir") + File.separator + "chrome-profile-" + threadId;
			File profileDir = new File(userDataDir);

			if (profileDir.exists()) {
				deleteDirectory(profileDir);
			}
		} catch (Exception e) {
			LOGGER.warn("Failed to cleanup Chrome profile directory: {}", e.getMessage());
		}
	}

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

	public static void forceKillChromeProcesses() {
		try {
			Process killChrome = Runtime.getRuntime().exec(new String[]{"taskkill", "/F", "/IM", "chrome.exe", "/T"});
			killChrome.waitFor(2, java.util.concurrent.TimeUnit.SECONDS);

			Process killChromeDriver = Runtime.getRuntime().exec(new String[]{"taskkill", "/F", "/IM", "chromedriver.exe", "/T"});
			killChromeDriver.waitFor(2, java.util.concurrent.TimeUnit.SECONDS);

			Thread.sleep(500);
		} catch (Exception e) {
			// Ignore - no processes to clean
		}
	}

}
