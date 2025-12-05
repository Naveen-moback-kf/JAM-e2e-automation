package com.kfonetalentsuite.utils.JobMapping;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.kfonetalentsuite.webdriverManager.DriverManager;
import com.kfonetalentsuite.utils.common.CommonVariable;

/**
 * Allure Environment Information Generator
 * 
 * Generates environment.properties file for Allure reports with:
 * - Browser version
 * - Operating system
 * - Test environment (QA, UAT, Prod)
 * - Application version
 * - Execution date/time
 * 
 * This file is automatically read by Allure when generating reports.
 */
public class AllureEnvironmentInfo {

	private static final Logger LOGGER = LogManager.getLogger(AllureEnvironmentInfo.class);
	
	// Environment properties file location (in Allure results directory)
	private static final String ENV_PROPERTIES_FILE = "AllureReports/allure-results/environment.properties";
	
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	/**
	 * Generate environment.properties file for Allure reports
	 * Called automatically from SuiteHooks.onStart()
	 */
	public static void generateEnvironmentInfo() {
		// CONFIGURATION CHECK: Skip Allure environment info if disabled in config.properties
		if (CommonVariable.ALLURE_REPORTING_ENABLED != null
				&& CommonVariable.ALLURE_REPORTING_ENABLED.equalsIgnoreCase("false")) {
			LOGGER.info(
					"Allure reporting is disabled in config.properties (allure.reporting=false) - Skipping Allure environment info generation");
			return; // Exit early - Allure reporting is disabled
		}
		
		try {
			LOGGER.info("Generating Allure environment information...");
			
			// Ensure directory exists
			File envFile = new File(ENV_PROPERTIES_FILE);
			envFile.getParentFile().mkdirs();
			
			// Generate environment information
			try (FileWriter writer = new FileWriter(envFile)) {
				writer.write("# Allure Environment Information\n");
				writer.write("# Generated automatically on test execution\n");
				writer.write("# " + LocalDateTime.now().format(DATE_TIME_FORMATTER) + "\n\n");
				
				// Browser Information
				writeBrowserInfo(writer);
				
				// Operating System
				writeOperatingSystemInfo(writer);
				
				// Test Environment
				writeTestEnvironmentInfo(writer);
				
				// Application Version
				writeApplicationVersionInfo(writer);
				
				// Execution Information
				writeExecutionInfo(writer);
				
				// Java Information
				writeJavaInfo(writer);
				
				writer.flush();
			}
			
			LOGGER.info("✅ Allure environment information generated: {}", ENV_PROPERTIES_FILE);
			
		} catch (Exception e) {
			LOGGER.warn("Failed to generate Allure environment information: {}", e.getMessage());
			LOGGER.debug("Stack trace:", e);
			// Don't fail test execution if environment info generation fails
		}
	}

	/**
	 * Write browser information to environment file
	 */
	private static void writeBrowserInfo(FileWriter writer) throws IOException {
		writer.write("# Browser Information\n");
		
		try {
			WebDriver driver = DriverManager.getDriver();
			
			if (driver != null && driver instanceof RemoteWebDriver) {
				Capabilities caps = ((RemoteWebDriver) driver).getCapabilities();
				
				// Browser Name
				String browserName = caps.getBrowserName();
				if (browserName != null) {
					writer.write("Browser=" + capitalize(browserName) + "\n");
				}
				
				// Browser Version
				Object browserVersion = caps.getCapability("browserVersion");
				if (browserVersion == null) {
					browserVersion = caps.getCapability("version");
				}
				if (browserVersion != null) {
					writer.write("Browser Version=" + browserVersion + "\n");
				}
				
				// Driver Version (if available)
				Object driverVersion = caps.getCapability("driverVersion");
				if (driverVersion != null) {
					writer.write("Driver Version=" + driverVersion + "\n");
				}
			} else {
				// Fallback: Try to get browser from CommonVariable
				String browser = getBrowserFromConfig();
				if (browser != null) {
					writer.write("Browser=" + browser + "\n");
				} else {
					writer.write("Browser=Unknown\n");
				}
			}
		} catch (Exception e) {
			LOGGER.debug("Could not get browser information: {}", e.getMessage());
			writer.write("Browser=Unknown\n");
		}
		
		writer.write("\n");
	}

	/**
	 * Write operating system information
	 */
	private static void writeOperatingSystemInfo(FileWriter writer) throws IOException {
		writer.write("# Operating System\n");
		writer.write("OS=" + System.getProperty("os.name") + "\n");
		writer.write("OS Version=" + System.getProperty("os.version") + "\n");
		writer.write("OS Architecture=" + System.getProperty("os.arch") + "\n");
		writer.write("\n");
	}

	/**
	 * Write test environment information
	 */
	private static void writeTestEnvironmentInfo(FileWriter writer) throws IOException {
		writer.write("# Test Environment\n");
		
		// Get environment from config.properties or default to QA
		String environment = CommonVariable.ENVIRONMENT != null ? 
			CommonVariable.ENVIRONMENT.toUpperCase() : "QA";
		writer.write("Environment=" + environment + "\n");
		
		// Get base URL if available (try different URL variables)
		String baseUrl = getBaseUrl();
		if (baseUrl != null && !baseUrl.isEmpty()) {
			writer.write("Base URL=" + baseUrl + "\n");
		}
		
		writer.write("\n");
	}

	/**
	 * Write application version information
	 */
	private static void writeApplicationVersionInfo(FileWriter writer) throws IOException {
		writer.write("# Application Information\n");
		
		// Try to get version from config or system properties
		String appVersion = System.getProperty("app.version");
		if (appVersion == null || appVersion.isEmpty()) {
			appVersion = "Not Specified";
		}
		writer.write("Application Version=" + appVersion + "\n");
		
		writer.write("\n");
	}

	/**
	 * Write execution information
	 */
	private static void writeExecutionInfo(FileWriter writer) throws IOException {
		writer.write("# Execution Information\n");
		writer.write("Execution Date=" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "\n");
		writer.write("Execution Time=" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "\n");
		writer.write("Execution DateTime=" + LocalDateTime.now().format(DATE_TIME_FORMATTER) + "\n");
		writer.write("User=" + System.getProperty("user.name") + "\n");
		writer.write("Host=" + getHostName() + "\n");
		writer.write("\n");
	}

	/**
	 * Write Java information
	 */
	private static void writeJavaInfo(FileWriter writer) throws IOException {
		writer.write("# Java Information\n");
		writer.write("Java Version=" + System.getProperty("java.version") + "\n");
		writer.write("Java Vendor=" + System.getProperty("java.vendor") + "\n");
		writer.write("Java Home=" + System.getProperty("java.home") + "\n");
		writer.write("\n");
	}

	/**
	 * Get browser name from configuration
	 */
	private static String getBrowserFromConfig() {
		try {
			// Try CommonVariable.BROWSER
			if (CommonVariable.BROWSER != null && !CommonVariable.BROWSER.isEmpty()) {
				return CommonVariable.BROWSER.toUpperCase();
			}
			
			// Try system property
			String browser = System.getProperty("browser.name");
			if (browser != null && !browser.isEmpty()) {
				return browser.toUpperCase();
			}
			
			return null;
		} catch (Exception e) {
			LOGGER.debug("Could not get browser from config: {}", e.getMessage());
			return null;
		}
	}

	/**
	 * Get hostname
	 */
	private static String getHostName() {
		try {
			return java.net.InetAddress.getLocalHost().getHostName();
		} catch (Exception e) {
			return "Unknown";
		}
	}

	/**
	 * Get base URL from configuration
	 */
	private static String getBaseUrl() {
		try {
			// Try different URL variables based on environment
			if (CommonVariable.ENVIRONMENT != null) {
				String env = CommonVariable.ENVIRONMENT.toUpperCase();
				if (env.contains("TEST") && CommonVariable.TESTURL != null) {
					return CommonVariable.TESTURL;
				} else if (env.contains("STAGE") && CommonVariable.STAGEURL != null) {
					return CommonVariable.STAGEURL;
				} else if (env.contains("PROD") && CommonVariable.PRODUSURL != null) {
					return CommonVariable.PRODUSURL;
				}
			}
			
			// Fallback to TESTURL
			if (CommonVariable.TESTURL != null) {
				return CommonVariable.TESTURL;
			}
			
			return null;
		} catch (Exception e) {
			LOGGER.debug("Could not get base URL: {}", e.getMessage());
			return null;
		}
	}

	/**
	 * Capitalize first letter of string
	 */
	private static String capitalize(String str) {
		if (str == null || str.isEmpty()) {
			return str;
		}
		return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
	}
}

