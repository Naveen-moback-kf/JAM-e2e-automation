package com.kfonetalentsuite.utils.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

/**
 * CommonVariableManager - Central configuration and variable management
 * 
 * This class handles loading properties from config files and environment-specific
 * configurations, then populates static fields for use throughout the framework.
 * 
 * Configuration Priority:
 * 1. System properties (-Dproperty=value)
 * 2. Environment-specific config (/environments/{env}.properties)
 * 3. Common config.properties
 */
public class CommonVariableManager {

	private static final Logger LOGGER = (Logger) LogManager.getLogger(CommonVariableManager.class);
	private static final ReentrantLock lock = new ReentrantLock();
	private static volatile Properties config;
	private static volatile Properties envConfig;
	private static volatile boolean isInitialized = false;
	private static final String DEFAULT_ENV = "qa";

	// ==================== PUBLIC STATIC FIELDS ====================
	
	// Environment-Specific Values
	public static String ENVIRONMENT;
	public static String LOGIN_TYPE;
	public static String TARGET_PAMS_ID;
	public static String SSO_USERNAME;
	public static String SSO_PASSWORD;
	public static String NON_SSO_USERNAME;
	public static String NON_SSO_PASSWORD;
	
	// Common Settings
	public static String BROWSER;
	public static String HEADLESS_MODE;
	public static String EXCEL_REPORTING_ENABLED;
	public static String ALLURE_REPORTING_ENABLED;
	public static String KEEP_SYSTEM_AWAKE;
	
	// URLs
	public static String KFONE_DEVURL;
	public static String KFONE_QAURL;
	public static String KFONE_STAGEURL;
	public static String KFONE_PRODEUURL;
	public static String KFONE_PRODUSURL;
	
	// Legacy Fields (backward compatibility)
	public static String Super_USERNAME;
	public static String Super_PASSWORD;
	public static String USERNAME;
	public static String PASSWORD;
	public static String TESTURL;
	public static String STAGEURL;
	public static String PRODEUURL;
	public static String PRODUSURL;
	public static String PRODCNURL;
	public static String BENCHMARKING_TXT;
	public static String REGRESSION_TXT;
	public static String OVERVIEWHC_TXT;
	public static String EMPLOYEEALLOCATION_TXT;
	public static String PAYINTEQTY_TXT;
	public static String PAYINTEQTYDD_TXT;
	public static String CROSSFNANALYSIS_TXT;
	public static String MIXOFPAY_TXT;
	public static String PayPerformance_TXT;
	public static String PayPerformanceOT_TXT;
	public static String CompOverview_TXT;
	public static String CompDrilldown_TXT;
	public static String GENDERANALYSIS_TXT;
	public static String SHORTTERM_TXT;
	public static String LONGTERM_TXT;
	public static String CARSELGI_TXT;
	public static String COMPARE2MRKT_TXT;
	public static String ICENVIRONMENT;
	public static String ICUrl;
	public static String ICusername;
	public static String ICpassword;
	public static String DemoUsername;
	public static String DemoPassword;
	public static String IC_THusername;
	public static String IC_THpassword;
	public static String SAML_USERNAME;
	public static String SAML_PASSWORD;

	// ThreadLocal Fields (for parallel execution)
	public static ThreadLocal<String> CURRENT_USER_ROLE = ThreadLocal.withInitial(() -> null);
	public static ThreadLocal<Boolean> USER_LEVEL_JOB_MAPPING_ENABLED = ThreadLocal.withInitial(() -> null);
	public static ThreadLocal<String> USER_LEVEL_PERMISSION = ThreadLocal.withInitial(() -> null);

	// ==================== STATIC INITIALIZER ====================
	
	static {
		try {
			loadProperties();
		} catch (Exception e) {
			// Set default values if properties loading fails
			ENVIRONMENT = "Test";
			BROWSER = "chrome";
			HEADLESS_MODE = "true";
			EXCEL_REPORTING_ENABLED = "true";
			ALLURE_REPORTING_ENABLED = "true";
			System.err.println("Failed to load properties, using defaults: " + e.getMessage());
			e.printStackTrace();
		}
	}

	// ==================== PROPERTY LOADING METHODS ====================
	
	/**
	 * Load properties from config files.
	 * This method is thread-safe and will only load once.
	 * 
	 * @return true if properties were loaded successfully, false otherwise
	 */
	public static boolean loadProperties() {
		if (isInitialized) {
			return true;
		}

		lock.lock();
		try {
			if (isInitialized) {
				return true;
			}
			
			// Step 1: Load common config.properties
			config = new Properties();
			try (InputStream is = CommonVariableManager.class.getResourceAsStream("/config.properties")) {
				if (is == null) {
					LOGGER.error("config.properties not found in classpath");
					return false;
				}
				config.load(is);
			} catch (IOException e) {
				LOGGER.error("Failed to load config.properties: " + e.getMessage());
				return false;
			}
			
			// Step 2: Load environment-specific config
			loadEnvironmentConfig();
			
			// Step 3: Populate static fields
			populateFields();
			
			isInitialized = true;
			return true;

		} finally {
			lock.unlock();
		}
	}
	
	/**
	 * Load environment-specific configuration file.
	 * Environment is determined by system property 'env' (defaults to 'qa').
	 */
	private static void loadEnvironmentConfig() {
		String env = System.getProperty("env", DEFAULT_ENV).toLowerCase();
		String envFile = "/environments/" + env + ".properties";
		
		envConfig = new Properties();
		
		try (InputStream is = CommonVariableManager.class.getResourceAsStream(envFile)) {
			if (is != null) {
				envConfig.load(is);
				LOGGER.info("🌍 Environment configuration loaded: {}", env);
			} else {
				LOGGER.warn("⚠️ Environment file not found: {} - using config.properties fallback", envFile);
				LOGGER.info("   Available environments: dev, qa, stage, prod-us, prod-eu");
			}
		} catch (IOException e) {
			LOGGER.warn("⚠️ Failed to load {}: {} - using fallback", envFile, e.getMessage());
		}
	}
	
	/**
	 * Get property with priority order:
	 * 1. System property
	 * 2. Environment-specific config
	 * 3. Common config.properties
	 * 
	 * @param envKey Key name in environment config
	 * @param configKey Key name in config.properties
	 * @return Property value or null if not found
	 */
	private static String getProperty(String envKey, String configKey) {
		// Priority 1: System property
		String value = System.getProperty(envKey);
		if (value != null && !value.isEmpty()) {
			return value;
		}
		
		// Priority 2: Environment-specific config
		if (envConfig != null) {
			value = envConfig.getProperty(envKey);
			if (value != null && !value.isEmpty()) {
				return value;
			}
		}
		
		// Priority 3: Common config.properties
		return config.getProperty(configKey);
	}

	/**
	 * Populate all static fields from loaded properties.
	 */
	private static void populateFields() {
		// ========================================
		// Environment-Specific Values
		// ========================================
		ENVIRONMENT = getProperty("environment", "Environment");
		LOGIN_TYPE = getProperty("login.type", "login.type");
		TARGET_PAMS_ID = getProperty("pams.id", "target.pams.id");
		SSO_USERNAME = getProperty("sso.username", "SSO_Login_Username");
		SSO_PASSWORD = getProperty("sso.password", "SSO_Login_Password");
		NON_SSO_USERNAME = getProperty("nonsso.username", "NON_SSO_Login_Username");
		NON_SSO_PASSWORD = getProperty("nonsso.password", "NON_SSO_Login_Password");
		
		// ========================================
		// Common Settings (from config.properties)
		// ========================================
		String browserOverride = System.getProperty("browser");
		BROWSER = (browserOverride != null && !browserOverride.isEmpty()) 
				? browserOverride 
				: config.getProperty("browser");
		
		String headlessOverride = System.getProperty("headless.mode");
		HEADLESS_MODE = (headlessOverride != null && !headlessOverride.isEmpty()) 
				? headlessOverride 
				: config.getProperty("headless.mode");
		
		EXCEL_REPORTING_ENABLED = config.getProperty("excel.reporting");
		ALLURE_REPORTING_ENABLED = config.getProperty("allure.reporting");
		
		// Configure Allure system properties based on allure.reporting setting
		if (ALLURE_REPORTING_ENABLED != null 
				&& ALLURE_REPORTING_ENABLED.equalsIgnoreCase("false")) {
			// Skip Maven Allure plugin
			System.setProperty("skipAllure", "true");
			
			// Redirect to target directory (cleaned by Maven, won't persist)
			System.setProperty("allure.results.directory", "target/allure-results");
			LOGGER.info("⚠️ ALLURE REPORTING DISABLED");
		} else {
			// Enable Maven Allure plugin
			System.setProperty("skipAllure", "false");
			
			// Use AllureReports folder at project root (persistent)
			System.setProperty("allure.results.directory", "AllureReports/allure-results");
			
			LOGGER.debug("Allure reporting ENABLED - using AllureReports/ folder");
		}
		
		KEEP_SYSTEM_AWAKE = config.getProperty("keep.system.awake");
		
		// URLs
		KFONE_DEVURL = config.getProperty("KFONE_DevUrl");
		KFONE_QAURL = config.getProperty("KFONE_QAUrl");
		KFONE_STAGEURL = config.getProperty("KFONE_StageUrl");
		KFONE_PRODEUURL = config.getProperty("KFONE_ProdEUUrl");
		KFONE_PRODUSURL = config.getProperty("KFONE_ProdUSUrl");
		
		// ========================================
		// Legacy Fields (backward compatibility)
		// ========================================
		Super_USERNAME = config.getProperty("super_Username");
		Super_PASSWORD = config.getProperty("super_Password");
		USERNAME = config.getProperty("username");
		PASSWORD = config.getProperty("password");
		TESTURL = config.getProperty("TestUrl");
		STAGEURL = config.getProperty("StageUrl");
		PRODEUURL = config.getProperty("ProdEUUrl");
		PRODUSURL = config.getProperty("ProdUSUrl");
		PRODCNURL = config.getProperty("ProdCNUrl");
		BENCHMARKING_TXT = config.getProperty("rewardBenchMarkingTxt");
		REGRESSION_TXT = config.getProperty("regressionLineTxt");
		OVERVIEWHC_TXT = config.getProperty("overviewHCTxt");
		EMPLOYEEALLOCATION_TXT = config.getProperty("employeeallocationTxt");
		PAYINTEQTY_TXT = config.getProperty("payinternalequityTxt");
		PAYINTEQTYDD_TXT = config.getProperty("payinternalequityddTxt");
		CROSSFNANALYSIS_TXT = config.getProperty("crossfnanalysisTxt");
		MIXOFPAY_TXT = config.getProperty("mixofpayTxt");
		PayPerformance_TXT = config.getProperty("payperfomanceTxt");
		PayPerformanceOT_TXT = config.getProperty("payperfomanceOTTxt");
		CompOverview_TXT = config.getProperty("compoverviewTxt");
		CompDrilldown_TXT = config.getProperty("compDrilldownTxt");
		GENDERANALYSIS_TXT = config.getProperty("genderAnalysisTxt");
		SHORTTERM_TXT = config.getProperty("shortTermInceTxt");
		LONGTERM_TXT = config.getProperty("longTermInceTxt");
		CARSELGI_TXT = config.getProperty("carsElgiTxt");
		COMPARE2MRKT_TXT = config.getProperty("compare2mrktTxt");
		ICENVIRONMENT = config.getProperty("ICEnvironment");
		ICUrl = config.getProperty("ICUrl");
		ICusername = config.getProperty("ICusername");
		ICpassword = config.getProperty("ICpassword");
		DemoUsername = config.getProperty("demoUsername");
		DemoPassword = config.getProperty("demoPassword");
		IC_THusername = config.getProperty("IC_THusername");
		IC_THpassword = config.getProperty("IC_THpassword");
		SAML_USERNAME = config.getProperty("AI_AUTO_SAML_Username");
		SAML_PASSWORD = config.getProperty("AI_AUTO_SAML_Password");
		
		// Log configuration
		LOGGER.info("╔═══════════════════════════════════════════════════════════╗");
		LOGGER.info("║ CONFIGURATION LOADED                                      ║");
		LOGGER.info("╠═══════════════════════════════════════════════════════════╣");
		LOGGER.info("║ Environment  : {}", padRight(ENVIRONMENT, 43) + "║");
		LOGGER.info("║ Login Type   : {}", padRight(LOGIN_TYPE, 43) + "║");
		LOGGER.info("║ PAMS ID      : {}", padRight(TARGET_PAMS_ID, 43) + "║");
		LOGGER.info("║ Browser      : {}", padRight(BROWSER, 43) + "║");
		LOGGER.info("║ Headless     : {}", padRight(HEADLESS_MODE, 43) + "║");
		LOGGER.info("╚═══════════════════════════════════════════════════════════╝");
	}

	private static String padRight(String value, int length) {
		if (value == null) value = "N/A";
		if (value.length() >= length) return value.substring(0, length);
		return String.format("%-" + length + "s", value);
	}

	// ==================== UTILITY METHODS ====================
	
	/**
	 * Check if properties have been initialized.
	 * 
	 * @return true if properties are loaded
	 */
	public static boolean isInitialized() {
		return isInitialized;
	}

	/**
	 * Get the loaded Properties object for advanced usage.
	 * 
	 * @return Properties object or null if not loaded
	 */
	public static Properties getConfig() {
		return config;
	}
	
	// Private constructor to prevent instantiation
	private CommonVariableManager() {
		throw new UnsupportedOperationException("Utility class - do not instantiate");
	}
}

