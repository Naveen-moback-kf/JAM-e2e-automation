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
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.JobMapping.utils.common.CommonVariable;

import io.github.bonigarcia.wdm.WebDriverManager;

public class CrossBrowserDriverManager {
    
    private static final Logger LOGGER = (Logger) LogManager.getLogger();
    private static ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<WebDriverWait> waitThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<String> browserNameThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<String> testNameThreadLocal = new ThreadLocal<>();
    
    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }
    
    public static WebDriverWait getWait() {
        return waitThreadLocal.get();
    }
    
    public static String getBrowserName() {
        return browserNameThreadLocal.get();
    }
    
    public static String getTestName() {
        return testNameThreadLocal.get();
    }
    
    public static void setTestName(String testName) {
        testNameThreadLocal.set(testName);
    }
    
    public static void setBrowserNameForCurrentThread(String browserName) {
        browserNameThreadLocal.set(browserName.toLowerCase());
        LOGGER.debug("Browser name set for thread {}: {}", Thread.currentThread().getName(), browserName);
    }
    
    @Parameters({"browserName", "browserVersion", "platform"})
    @BeforeTest
    public void launchBrowser(
            @Optional("chrome") String browserName,
            @Optional("latest") String browserVersion, 
            @Optional("Windows") String platform) {
        
        initializeBrowser(browserName, browserVersion, platform);
    }
    
    public static void initializeBrowser(String browserName, String browserVersion, String platform) {
        try {
            // Store browser info for current thread
            browserNameThreadLocal.set(browserName.toLowerCase());
            
            // Log browser initialization
            LOGGER.info("CROSS-BROWSER INIT - Starting {} browser on {} platform", 
                       browserName.toUpperCase(), platform);
            
            // Log system information for Edge troubleshooting
            if ("edge".equalsIgnoreCase(browserName)) {
                logSystemInformation();
            }
            
            WebDriver driver = createBrowserDriver(browserName, browserVersion, platform);
            driverThreadLocal.set(driver);
            
            // Configure common driver settings
            configureDriver(driver);
            
            // Create WebDriverWait for current thread
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            waitThreadLocal.set(wait);
            
            // Log browser initialization (ExtentReports integration handled by Cucumber scenarios)
            LOGGER.info("Browser initialized: {} {} on {} for thread: {}", 
                       browserName.toUpperCase(), browserVersion, platform, Thread.currentThread().getName());
            
            LOGGER.info("{} browser launched successfully for thread: {}", 
                       browserName.toUpperCase(), Thread.currentThread().getName());
                       
        } catch (Exception e) {
            LOGGER.error("Failed to initialize {} browser: {}", browserName, e.getMessage());
            throw new RuntimeException("Browser initialization failed: " + e.getMessage(), e);
        }
    }
    
    private static WebDriver createBrowserDriver(String browserName, String browserVersion, String platform) {
        WebDriver driver;
        boolean isHeadless = isHeadlessMode();
        
        switch (browserName.toLowerCase()) {
            case "chrome":
                driver = createChromeDriver(isHeadless);
                break;
            case "firefox":
                driver = createFirefoxDriver(isHeadless);
                break;
            case "edge":
                driver = createEdgeDriver(isHeadless);
                break;
            default:
                LOGGER.warn("Unknown browser: {}, defaulting to Chrome", browserName);
                driver = createChromeDriver(isHeadless);
                break;
        }
        
        return driver;
    }
    
    private static WebDriver createChromeDriver(boolean isHeadless) {
        WebDriverManager.chromedriver().setup();
        
        // Download preferences
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", 
                 System.getProperty("user.dir") + File.separator + "externalFiles" + File.separator + "downloadFiles");
        prefs.put("profile.default_content_settings.popups", 0);
        prefs.put("download.prompt_for_download", false);
        
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);
        
        // Security and stability options
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--ignore-ssl-errors=yes");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--disable-web-security");
        options.addArguments("--allow-running-insecure-content");
        options.addArguments("--disable-extensions");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        
        // Cross-browser session stability
        options.addArguments("--disable-background-timer-throttling");
        options.addArguments("--disable-backgrounding-occluded-windows");
        options.addArguments("--disable-renderer-backgrounding");
        options.addArguments("--disable-hang-monitor");
        
        // Headless configuration
        if (isHeadless) {
            LOGGER.info("Chrome configured in HEADLESS mode");
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
        } else {
            LOGGER.info("Chrome configured in WINDOWED mode");
            options.addArguments("--start-maximized");
        }
        
        return new ChromeDriver(options);
    }
    
    private static WebDriver createFirefoxDriver(boolean isHeadless) {
        WebDriverManager.firefoxdriver().setup();
        
        FirefoxOptions options = new FirefoxOptions();
        
        // Common stability options
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        
        // Session stability preferences
        options.addPreference("dom.webnotifications.enabled", false);
        options.addPreference("media.volume_scale", "0.0");
        options.addPreference("browser.sessionstore.resume_from_crash", false);
        options.addPreference("browser.tabs.crashReporting.sendReport", false);
        
        // Download preferences
        options.addPreference("browser.download.folderList", 2);
        options.addPreference("browser.download.dir", 
                             System.getProperty("user.dir") + File.separator + "externalFiles" + File.separator + "downloadFiles");
        options.addPreference("browser.helperApps.neverAsk.saveToDisk", "application/pdf,application/zip");
        
        // Headless configuration
        if (isHeadless) {
            LOGGER.info("Firefox configured in HEADLESS mode");
            options.addArguments("--headless");
            options.addArguments("--width=1920");
            options.addArguments("--height=1080");
        } else {
            LOGGER.info("Firefox configured in WINDOWED mode");
        }
        
        return new FirefoxDriver(options);
    }
    
    private static WebDriver createEdgeDriver(boolean isHeadless) {
        // Check for force offline mode
        boolean forceOffline = "true".equalsIgnoreCase(System.getProperty("webdriver.force.offline"));
        
        // Strategy 1: Try system property first (if manually configured)
        String systemEdgeDriver = System.getProperty("webdriver.edge.driver");
        if (systemEdgeDriver != null && !systemEdgeDriver.isEmpty()) {
            LOGGER.info("Using system-configured Edge driver: {}", systemEdgeDriver);
        } else {
            // Strategy 2: Try various WebDriverManager approaches with enhanced error handling
            boolean driverSetupSuccessful = false;
            
            if (!forceOffline) {
                // Attempt 1: Check network connectivity first
                if (checkNetworkConnectivity()) {
                    // Normal online setup with proxy support
                    try {
                        configureWebDriverManagerProxy();
                        WebDriverManager.edgedriver().setup();
                        driverSetupSuccessful = true;
                        LOGGER.info("Edge driver setup successful (online)");
                    } catch (Exception e) {
                        LOGGER.warn("Online Edge driver setup failed: {}", e.getMessage());
                        
                        // Try alternative download approach
                        try {
                            LOGGER.info("Trying alternative Edge driver setup...");
                            WebDriverManager.edgedriver()
                                .clearDriverCache()
                                .avoidFallback()
                                .setup();
                            driverSetupSuccessful = true;
                            LOGGER.info("Edge driver setup successful (alternative method)");
                        } catch (Exception altEx) {
                            LOGGER.warn("Alternative Edge driver setup failed: {}", altEx.getMessage());
                        }
                    }
                } else {
                    LOGGER.warn("Network connectivity check failed, skipping online setup");
                }
            } else {
                LOGGER.info("Force offline mode enabled, skipping online setup");
            }
            
            // Attempt 2: Enhanced offline/cached mode
            if (!driverSetupSuccessful) {
                try {
                    String cacheDir = System.getProperty("user.home") + "/.cache/selenium";
                    LOGGER.info("Trying cached Edge driver from: {}", cacheDir);
                    
                    WebDriverManager.edgedriver()
                        .avoidBrowserDetection()
                        .avoidFallback()
                        .cachePath(cacheDir)
                        .setup();
                    driverSetupSuccessful = true;
                    LOGGER.info("Edge driver setup successful (cached)");
                } catch (Exception e) {
                    LOGGER.warn("Cached Edge driver setup failed: {}", e.getMessage());
                }
            }
            
            // Attempt 3: Try to use local Edge driver files
            if (!driverSetupSuccessful) {
                try {
                    // Extended search paths for Edge driver
                    String userHome = System.getProperty("user.home");
                    String currentDir = System.getProperty("user.dir");
                    
                    String[] possiblePaths = {
                        // User Downloads folder (most common)
                        userHome + "\\Downloads\\msedgedriver.exe",
                        userHome + "\\Downloads\\MicrosoftWebDriver.exe",
                        // Desktop (second most common)
                        userHome + "\\Desktop\\msedgedriver.exe",
                        userHome + "\\Desktop\\MicrosoftWebDriver.exe",
                        // Project directory
                        currentDir + "\\drivers\\msedgedriver.exe",
                        currentDir + "\\msedgedriver.exe",
                        // System Edge installation paths
                        "C:\\Program Files (x86)\\Microsoft\\Edge\\Application\\msedgedriver.exe",
                        "C:\\Program Files\\Microsoft\\Edge\\Application\\msedgedriver.exe",
                        // Alternative download locations
                        "C:\\drivers\\msedgedriver.exe",
                        "C:\\selenium\\msedgedriver.exe"
                    };
                    
                    LOGGER.info("Searching for local Edge driver in {} locations...", possiblePaths.length);
                    
                    for (String path : possiblePaths) {
                        LOGGER.debug("Checking: {}", path);
                        java.io.File driverFile = new java.io.File(path);
                        if (driverFile.exists() && driverFile.canExecute()) {
                            System.setProperty("webdriver.edge.driver", path);
                            LOGGER.info("Found local Edge driver: {}", path);
                            LOGGER.info("File size: {} bytes", driverFile.length());
                            driverSetupSuccessful = true;
                            break;
                        }
                    }
                    
                    if (!driverSetupSuccessful) {
                        LOGGER.warn("No Edge driver found in any of the {} search locations", possiblePaths.length);
                        LOGGER.info("Searched locations:");
                        for (String path : possiblePaths) {
                            LOGGER.info("   - {}", path);
                        }
                    }
                } catch (Exception e) {
                    LOGGER.warn("Local Edge driver search failed: {}", e.getMessage());
                }
            }
            
            // Final attempt: Give user comprehensive troubleshooting instructions
            if (!driverSetupSuccessful) {
                LOGGER.error("Edge driver setup failed due to network connectivity issues.");
                LOGGER.error("NETWORK DIAGNOSTICS:");
                performNetworkDiagnostics();
                
                LOGGER.error("SOLUTIONS:");
                LOGGER.error("1. MANUAL DOWNLOAD: https://developer.microsoft.com/en-us/microsoft-edge/tools/webdriver/");
                LOGGER.error("2. PLACE IN: {}\\Downloads\\msedgedriver.exe", System.getProperty("user.home"));
                LOGGER.error("3. SYSTEM PROPERTY: -Dwebdriver.edge.driver=C:\\path\\to\\msedgedriver.exe");
                LOGGER.error("4. FORCE OFFLINE: -Dwebdriver.force.offline=true");
                LOGGER.error("5. PROXY CONFIG: -Dwebdriver.proxy.host=proxy.company.com -Dwebdriver.proxy.port=8080");
                LOGGER.error("6. SKIP EDGE: Use ChromeFirefox runner instead");
                
                throw new RuntimeException("Edge driver setup failed. Network connectivity issue detected - see log for solutions.");
            }
        }
        
        EdgeOptions options = new EdgeOptions();
        
        // Security and stability options
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--ignore-ssl-errors=yes");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--disable-web-security");
        options.addArguments("--allow-running-insecure-content");
        options.addArguments("--disable-extensions");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        
        // Download preferences  
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", 
                 System.getProperty("user.dir") + File.separator + "externalFiles" + File.separator + "downloadFiles");
        prefs.put("profile.default_content_settings.popups", 0);
        prefs.put("download.prompt_for_download", false);
        options.setExperimentalOption("prefs", prefs);
        
        // Headless configuration
        if (isHeadless) {
            LOGGER.info("Edge configured in HEADLESS mode");
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
        } else {
            LOGGER.info("Edge configured in WINDOWED mode");
            options.addArguments("--start-maximized");
        }
        
        return new EdgeDriver(options);
    }
    
    private static boolean checkNetworkConnectivity() {
        try {
            LOGGER.debug("Checking network connectivity to Edge driver repository...");
            
            // Test DNS resolution first
            java.net.InetAddress.getByName("msedgedriver.azureedge.net");
            
            // Test HTTP connection
            java.net.HttpURLConnection connection = (java.net.HttpURLConnection) 
                new java.net.URL("https://msedgedriver.azureedge.net").openConnection();
            connection.setConnectTimeout(5000); // 5 seconds timeout
            connection.setReadTimeout(5000);
            
            // Set user agent to avoid blocking
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
            
            int responseCode = connection.getResponseCode();
            connection.disconnect();
            
            boolean isConnected = (responseCode >= 200 && responseCode < 400);
            LOGGER.debug("Network connectivity check: {} (HTTP {})", 
                        isConnected ? "SUCCESS" : "FAILED", responseCode);
            return isConnected;
            
        } catch (java.net.UnknownHostException e) {
            LOGGER.debug("Network connectivity check failed - DNS resolution error: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            LOGGER.debug("Network connectivity check failed: {}", e.getMessage());
            return false;
        }
    }
    
    private static void configureWebDriverManagerProxy() {
        String proxyHost = System.getProperty("webdriver.proxy.host");
        String proxyPort = System.getProperty("webdriver.proxy.port");
        
        if (proxyHost != null && !proxyHost.isEmpty()) {
            LOGGER.info("Configuring proxy: {}:{}", proxyHost, proxyPort != null ? proxyPort : "8080");
            
            // Set system properties for WebDriverManager
            System.setProperty("https.proxyHost", proxyHost);
            System.setProperty("https.proxyPort", proxyPort != null ? proxyPort : "8080");
            System.setProperty("http.proxyHost", proxyHost);
            System.setProperty("http.proxyPort", proxyPort != null ? proxyPort : "8080");
            
            // Also try environment variables (common in corporate environments)
            if (System.getenv("HTTP_PROXY") != null || System.getenv("HTTPS_PROXY") != null) {
                LOGGER.info("Environment proxy variables detected");
            }
        }
    }
    
    private static void performNetworkDiagnostics() {
        LOGGER.error("  - DNS Resolution Test:");
        try {
            java.net.InetAddress.getByName("msedgedriver.azureedge.net");
            LOGGER.error("[OK] DNS resolution successful");
        } catch (Exception e) {
            LOGGER.error("[FAIL] DNS resolution failed: {}", e.getMessage());
            LOGGER.error("[TIP] Try: ipconfig /flushdns or check corporate DNS settings");
        }
        
        LOGGER.error("   - Proxy Configuration:");
        String httpProxy = System.getProperty("http.proxyHost");
        String httpsProxy = System.getProperty("https.proxyHost");
        if (httpProxy != null || httpsProxy != null) {
            LOGGER.error("[INFO] HTTP Proxy: {}", httpProxy != null ? httpProxy : "Not set");
            LOGGER.error("[INFO] HTTPS Proxy: {}", httpsProxy != null ? httpsProxy : "Not set");
        } else {
            LOGGER.error("     [WARN] No proxy configured (may be required in corporate networks)");
        }
        
        LOGGER.error("   - Environment Variables:");
        String envHttpProxy = System.getenv("HTTP_PROXY");
        String envHttpsProxy = System.getenv("HTTPS_PROXY");
        if (envHttpProxy != null || envHttpsProxy != null) {
            LOGGER.error("[INFO] HTTP_PROXY: {}", envHttpProxy != null ? envHttpProxy : "Not set");
            LOGGER.error("[INFO] HTTPS_PROXY: {}", envHttpsProxy != null ? envHttpsProxy : "Not set");
        } else {
            LOGGER.error("[WARN] No environment proxy variables found");
        }
        
        LOGGER.error("   - Firewall/Corporate Network:");
        LOGGER.error("     [TIP] Check if msedgedriver.azureedge.net is blocked by firewall");
        LOGGER.error("Contact IT team to whitelist selenium download URLs");
    }
    
    private static void logSystemInformation() {
        LOGGER.debug("System Information:");
        LOGGER.debug("OS: {} {}", System.getProperty("os.name"), System.getProperty("os.version"));
        LOGGER.debug("Java: {} ({})", System.getProperty("java.version"), System.getProperty("java.vendor"));
        LOGGER.debug("User: {}", System.getProperty("user.name"));
        LOGGER.debug("Working Dir: {}", System.getProperty("user.dir"));
        LOGGER.debug("Internet Connection: {}", checkInternetConnection() ? "Available" : "Limited/None");
    }
    
    private static boolean checkInternetConnection() {
        try {
            // Try multiple reliable hosts
            String[] testHosts = {"www.google.com", "www.microsoft.com", "github.com"};
            for (String host : testHosts) {
                try {
                    java.net.InetAddress.getByName(host);
                    return true;
                } catch (Exception ignored) {
                    // Continue to next host
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    private static void configureDriver(WebDriver driver) {
        // Delete cookies and configure timeouts
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));
        
        // Maximize window (only for non-headless mode)
        if (!isHeadlessMode()) {
            try {
                driver.manage().window().maximize();
            } catch (Exception e) {
                LOGGER.warn("Could not maximize window: {}", e.getMessage());
            }
        }
    }
    
    private static boolean isHeadlessMode() {
        String headlessMode = CommonVariable.HEADLESS_MODE != null ? CommonVariable.HEADLESS_MODE : "true";
        return Boolean.parseBoolean(headlessMode);
    }
    
    @BeforeMethod
    public void CreateDriver() {
        if (getDriver() == null) {
            String defaultBrowser = CommonVariable.BROWSER != null ? CommonVariable.BROWSER : "chrome";
            initializeBrowser(defaultBrowser, "latest", "Windows");
        }
    }
    
    public static void quitDriver() {
        try {
            WebDriver driver = getDriver();
            if (driver != null) {
                String browserName = getBrowserName();
                LOGGER.info("Closing {} browser for thread: {}", 
                           browserName != null ? browserName.toUpperCase() : "UNKNOWN", 
                           Thread.currentThread().getName());
                
                driver.quit();
                LOGGER.info("{} browser closed successfully", 
                           browserName != null ? browserName.toUpperCase() : "Browser");
            }
        } catch (Exception e) {
            LOGGER.error("Error closing browser: {}", e.getMessage());
        } finally {
            // Clean up ThreadLocal variables
            driverThreadLocal.remove();
            waitThreadLocal.remove();
            browserNameThreadLocal.remove();
            testNameThreadLocal.remove();
        }
    }
    

    public static String getBrowserInfo() {
        String browserName = getBrowserName();
        if (browserName == null) {
            return "Unknown Browser";
        }
        
        return String.format("%s Browser (Thread: %s)", 
                           browserName.toUpperCase(), 
                           Thread.currentThread().getName());
    }
    

    public static boolean isDriverAvailable() {
        return getDriver() != null;
    }
}
