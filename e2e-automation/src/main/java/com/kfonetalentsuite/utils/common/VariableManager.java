package com.kfonetalentsuite.utils.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

public class VariableManager {

	private static final Logger LOGGER = (Logger) LogManager.getLogger(VariableManager.class);
	private static volatile VariableManager instance;
	private static final ReentrantLock lock = new ReentrantLock();
	private static volatile Properties config;
	private static volatile Properties envConfig;
	private static volatile boolean isInitialized = false;
	
	private static final String DEFAULT_ENV = "qa";

	private VariableManager() {
	}

	public static VariableManager getInstance() {
		if (instance == null) {
			lock.lock();
			try {
				if (instance == null) {
					instance = new VariableManager();
				}
			} finally {
				lock.unlock();
			}
		}
		return instance;
	}

	public boolean loadProperties() {
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
			try {
				config.load(getClass().getResourceAsStream("/config.properties"));
			} catch (IOException e) {
				LOGGER.error("Failed to load config.properties: " + e.getMessage());
				return false;
			}
			
			// Step 2: Load environment-specific config
			loadEnvironmentConfig();
			
			// Step 3: Populate CommonVariable fields
			populateCommonVariables();
			
			isInitialized = true;
			return true;

		} finally {
			lock.unlock();
		}
	}
	
	private void loadEnvironmentConfig() {
		String env = System.getProperty("env", DEFAULT_ENV).toLowerCase();
		String envFile = "/environments/" + env + ".properties";
		
		envConfig = new Properties();
		
		try (InputStream is = getClass().getResourceAsStream(envFile)) {
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
	
	private String getProperty(String envKey, String configKey) {
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

	private void populateCommonVariables() {
		// ========================================
		// Environment-Specific Values
		// ========================================
		CommonVariable.ENVIRONMENT = getProperty("environment", "Environment");
		CommonVariable.LOGIN_TYPE = getProperty("login.type", "login.type");
		CommonVariable.TARGET_PAMS_ID = getProperty("pams.id", "target.pams.id");
		CommonVariable.SSO_USERNAME = getProperty("sso.username", "SSO_Login_Username");
		CommonVariable.SSO_PASSWORD = getProperty("sso.password", "SSO_Login_Password");
		CommonVariable.NON_SSO_USERNAME = getProperty("nonsso.username", "NON_SSO_Login_Username");
		CommonVariable.NON_SSO_PASSWORD = getProperty("nonsso.password", "NON_SSO_Login_Password");
		
		// ========================================
		// Common Settings (from config.properties)
		// ========================================
		String browserOverride = System.getProperty("browser");
		CommonVariable.BROWSER = (browserOverride != null && !browserOverride.isEmpty()) 
				? browserOverride 
				: config.getProperty("browser");
		
		String headlessOverride = System.getProperty("headless.mode");
		CommonVariable.HEADLESS_MODE = (headlessOverride != null && !headlessOverride.isEmpty()) 
				? headlessOverride 
				: config.getProperty("headless.mode");
		
		CommonVariable.EXCEL_REPORTING_ENABLED = config.getProperty("excel.reporting");
		CommonVariable.ALLURE_REPORTING_ENABLED = config.getProperty("allure.reporting");
		
		// Configure Allure system properties based on allure.reporting setting
		if (CommonVariable.ALLURE_REPORTING_ENABLED != null 
				&& CommonVariable.ALLURE_REPORTING_ENABLED.equalsIgnoreCase("false")) {
			// Skip Maven Allure plugin
			System.setProperty("skipAllure", "true");
			
			// Redirect to target directory (cleaned by Maven, won't persist)
			// allure.properties now defaults to target/ so this reinforces it
			System.setProperty("allure.results.directory", "target/allure-results");
			LOGGER.info("⚠️ ALLURE REPORTING DISABLED");
		} else {
			// Enable Maven Allure plugin
			System.setProperty("skipAllure", "false");
			
			// Use AllureReports folder at project root (persistent)
			System.setProperty("allure.results.directory", "AllureReports/allure-results");
			
			LOGGER.debug("Allure reporting ENABLED - using AllureReports/ folder");
		}
		
		CommonVariable.KEEP_SYSTEM_AWAKE = config.getProperty("keep.system.awake");
		
		// URLs
		CommonVariable.KFONE_DEVURL = config.getProperty("KFONE_DevUrl");
		CommonVariable.KFONE_QAURL = config.getProperty("KFONE_QAUrl");
		CommonVariable.KFONE_STAGEURL = config.getProperty("KFONE_StageUrl");
		CommonVariable.KFONE_PRODEUURL = config.getProperty("KFONE_ProdEUUrl");
		CommonVariable.KFONE_PRODUSURL = config.getProperty("KFONE_ProdUSUrl");
		
		// ========================================
		// Legacy Fields (backward compatibility)
		// ========================================
		CommonVariable.Super_USERNAME = config.getProperty("super_Username");
		CommonVariable.Super_PASSWORD = config.getProperty("super_Password");
		CommonVariable.USERNAME = config.getProperty("username");
		CommonVariable.PASSWORD = config.getProperty("password");
		CommonVariable.TESTURL = config.getProperty("TestUrl");
		CommonVariable.STAGEURL = config.getProperty("StageUrl");
		CommonVariable.PRODEUURL = config.getProperty("ProdEUUrl");
		CommonVariable.PRODUSURL = config.getProperty("ProdUSUrl");
		CommonVariable.PRODCNURL = config.getProperty("ProdCNUrl");
		CommonVariable.BENCHMARKING_TXT = config.getProperty("rewardBenchMarkingTxt");
		CommonVariable.REGRESSION_TXT = config.getProperty("regressionLineTxt");
		CommonVariable.OVERVIEWHC_TXT = config.getProperty("overviewHCTxt");
		CommonVariable.EMPLOYEEALLOCATION_TXT = config.getProperty("employeeallocationTxt");
		CommonVariable.PAYINTEQTY_TXT = config.getProperty("payinternalequityTxt");
		CommonVariable.PAYINTEQTYDD_TXT = config.getProperty("payinternalequityddTxt");
		CommonVariable.CROSSFNANALYSIS_TXT = config.getProperty("crossfnanalysisTxt");
		CommonVariable.MIXOFPAY_TXT = config.getProperty("mixofpayTxt");
		CommonVariable.PayPerformance_TXT = config.getProperty("payperfomanceTxt");
		CommonVariable.PayPerformanceOT_TXT = config.getProperty("payperfomanceOTTxt");
		CommonVariable.CompOverview_TXT = config.getProperty("compoverviewTxt");
		CommonVariable.CompDrilldown_TXT = config.getProperty("compDrilldownTxt");
		CommonVariable.GENDERANALYSIS_TXT = config.getProperty("genderAnalysisTxt");
		CommonVariable.SHORTTERM_TXT = config.getProperty("shortTermInceTxt");
		CommonVariable.LONGTERM_TXT = config.getProperty("longTermInceTxt");
		CommonVariable.CARSELGI_TXT = config.getProperty("carsElgiTxt");
		CommonVariable.COMPARE2MRKT_TXT = config.getProperty("compare2mrktTxt");
		CommonVariable.ICENVIRONMENT = config.getProperty("ICEnvironment");
		CommonVariable.ICUrl = config.getProperty("ICUrl");
		CommonVariable.ICusername = config.getProperty("ICusername");
		CommonVariable.ICpassword = config.getProperty("ICpassword");
		CommonVariable.DemoUsername = config.getProperty("demoUsername");
		CommonVariable.DemoPassword = config.getProperty("demoPassword");
		CommonVariable.IC_THusername = config.getProperty("IC_THusername");
		CommonVariable.IC_THpassword = config.getProperty("IC_THpassword");
		CommonVariable.SAML_USERNAME = config.getProperty("AI_AUTO_SAML_Username");
		CommonVariable.SAML_PASSWORD = config.getProperty("AI_AUTO_SAML_Password");
		
		// Log configuration
		LOGGER.info("╔═══════════════════════════════════════════════════════════╗");
		LOGGER.info("║ CONFIGURATION LOADED                                      ║");
		LOGGER.info("╠═══════════════════════════════════════════════════════════╣");
		LOGGER.info("║ Environment  : {}", padRight(CommonVariable.ENVIRONMENT, 43) + "║");
		LOGGER.info("║ Login Type   : {}", padRight(CommonVariable.LOGIN_TYPE, 43) + "║");
		LOGGER.info("║ PAMS ID      : {}", padRight(CommonVariable.TARGET_PAMS_ID, 43) + "║");
		LOGGER.info("║ Browser      : {}", padRight(CommonVariable.BROWSER, 43) + "║");
		LOGGER.info("║ Headless     : {}", padRight(CommonVariable.HEADLESS_MODE, 43) + "║");
		LOGGER.info("╚═══════════════════════════════════════════════════════════╝");
	}

	private String padRight(String value, int length) {
		if (value == null) value = "N/A";
		if (value.length() >= length) return value.substring(0, length);
		return String.format("%-" + length + "s", value);
	}

	public static boolean isInitialized() {
		return isInitialized;
	}

	public static Properties getConfig() {
		return config;
	}
}
