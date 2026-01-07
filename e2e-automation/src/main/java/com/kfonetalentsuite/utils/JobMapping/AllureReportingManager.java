package com.kfonetalentsuite.utils.JobMapping;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.kfonetalentsuite.utils.common.CommonVariable;
import com.kfonetalentsuite.webdriverManager.DriverManager;

import io.qameta.allure.Allure;

public class AllureReportingManager implements ITestListener {

	private static final Logger LOGGER = LogManager.getLogger(AllureReportingManager.class);

	private static final String ALLURE_REPORTS_DIR = "AllureReports";
	private static final String ALLURE_RESULTS_DIR = "AllureReports/allure-results";
	private static final String ALLURE_REPORT_DIR = "AllureReports/allure-report";
	private static final String ALLURE_BACKUP_DIR = "AllureReports/Backup";
	private static final String LAST_RESET_MARKER = "AllureReports/.last-reset-date";
	private static final String ENV_PROPERTIES_FILE = "AllureReports/allure-results/environment.properties";
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public static void checkAndPerformDailyReset() {
		if (!isAllureReportingEnabled()) {
			LOGGER.info("Allure reporting is disabled in config.properties (allure.reporting=false) - Skipping Allure daily reset");
			return;
		}
		
		try {
			LOGGER.info("=== ALLURE DAILY RESET CHECK ===");
			createBackupDirectories();
			boolean isNewDay = isNewDayDetected();
			
			if (isNewDay) {
				LOGGER.info("🔄 New day detected - Performing Allure report daily reset");
				performDailyReset();
			} else {
				LOGGER.debug("Same day detected - Continuing with existing Allure results");
			}
			
			LOGGER.info("=== ALLURE DAILY RESET CHECK COMPLETE ===");
		} catch (Exception e) {
			LOGGER.warn("Allure daily reset check encountered an issue: {}", e.getMessage());
			LOGGER.debug("Stack trace:", e);
		}
	}

	public static void generateEnvironmentInfo() {
		if (!isAllureReportingEnabled()) {
			LOGGER.info("Allure reporting is disabled in config.properties (allure.reporting=false) - Skipping Allure environment info generation");
			return;
		}
		
		try {
			LOGGER.info("Generating Allure environment information...");
			File envFile = new File(ENV_PROPERTIES_FILE);
			envFile.getParentFile().mkdirs();
			
			try (FileWriter writer = new FileWriter(envFile)) {
				writer.write("# Allure Environment Information\n");
				writer.write("# Generated automatically on test execution\n");
				writer.write("# " + LocalDateTime.now().format(DATE_TIME_FORMATTER) + "\n\n");
				
				writeBrowserInfo(writer);
				writeOperatingSystemInfo(writer);
				writeTestEnvironmentInfo(writer);
				writeApplicationVersionInfo(writer);
				writeExecutionInfo(writer);
				writeJavaInfo(writer);
				
				writer.flush();
			}
			
			LOGGER.info("✅ Allure environment information generated: {}", ENV_PROPERTIES_FILE);
			
		} catch (Exception e) {
			LOGGER.warn("Failed to generate Allure environment information: {}", e.getMessage());
			LOGGER.debug("Stack trace:", e);
		}
	}

	@Override
	public void onTestFailure(ITestResult result) {
		if (!isAllureReportingEnabled()) {
			LOGGER.debug("Allure reporting is disabled - Skipping screenshot attachment");
			return;
		}
		
		try {
			LOGGER.debug("Test failure detected - Attaching screenshot to Allure report");
			String methodName = result.getMethod().getMethodName();
			Throwable throwable = result.getThrowable();
			String errorMessage = throwable != null ? throwable.getMessage() : "Test failed";
			String screenshotPath = ScreenshotHandler.captureFailureScreenshot(methodName, errorMessage);
			
			if (screenshotPath != null) {
				File screenshotFile = new File(screenshotPath);
				
				// ADDITIONAL SAFEGUARD: If file doesn't exist immediately, wait briefly for async write
				// This handles edge cases where async write might still be in progress
				if (!screenshotFile.exists()) {
					LOGGER.debug("Screenshot file not immediately available, waiting briefly...");
					try {
						// Wait up to 2 seconds for file to appear (checking every 500ms)
						int maxRetries = 4;
						for (int i = 0; i < maxRetries && !screenshotFile.exists(); i++) {
							Thread.sleep(500);
						}
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
				
				if (screenshotFile.exists()) {
					attachScreenshotToAllure(screenshotPath, "Test Failure Screenshot");
					LOGGER.info("✅ Screenshot attached to Allure report: {}", screenshotPath);
				} else {
					LOGGER.warn("Screenshot not captured or file not found after wait - skipping Allure attachment");
				}
			} else {
				LOGGER.warn("Screenshot path is null - skipping Allure attachment");
			}
			
		} catch (Exception e) {
			LOGGER.error("Error attaching screenshot to Allure report: {}", e.getMessage(), e);
		}
	}

	public static void attachScreenshotToAllure(String screenshotPath, String attachmentName) {
		try {
			File screenshotFile = new File(screenshotPath);
			
			if (!screenshotFile.exists()) {
				LOGGER.warn("Screenshot file does not exist: {}", screenshotPath);
				return;
			}
			
			Allure.addAttachment(
				attachmentName != null ? attachmentName : "Screenshot",
				"image/png",
				Files.newInputStream(screenshotFile.toPath()),
				"png"
			);
			
			LOGGER.debug("Screenshot attached to Allure: {} ({})", attachmentName, screenshotPath);
			
		} catch (IOException e) {
			LOGGER.error("Failed to attach screenshot to Allure: {}", e.getMessage(), e);
		}
	}

	public static void attachScreenshotBytesToAllure(byte[] screenshotBytes, String attachmentName) {
		try {
			if (screenshotBytes == null || screenshotBytes.length == 0) {
				LOGGER.warn("Screenshot bytes are empty - skipping Allure attachment");
				return;
			}
			
			Allure.addAttachment(
				attachmentName != null ? attachmentName : "Screenshot",
				"image/png",
				new java.io.ByteArrayInputStream(screenshotBytes),
				"png"
			);
			
			LOGGER.debug("Screenshot bytes attached to Allure: {}", attachmentName);
			
		} catch (Exception e) {
			LOGGER.error("Failed to attach screenshot bytes to Allure: {}", e.getMessage(), e);
		}
	}

	public static boolean captureAndAttachScreenshot(String attachmentName) {
		try {
			WebDriver driver = DriverManager.getDriver();
			
			if (driver == null) {
				LOGGER.warn("WebDriver is null - cannot capture screenshot");
				return false;
			}
			
			TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
			byte[] screenshotBytes = takesScreenshot.getScreenshotAs(OutputType.BYTES);
			attachScreenshotBytesToAllure(screenshotBytes, attachmentName);
			
			LOGGER.debug("Screenshot captured and attached to Allure: {}", attachmentName);
			return true;
			
		} catch (Exception e) {
			LOGGER.error("Failed to capture and attach screenshot: {}", e.getMessage(), e);
			return false;
		}
	}

	private static boolean isNewDayDetected() {
		try {
			String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			File markerFile = new File(LAST_RESET_MARKER);

			if (!markerFile.exists()) {
				LOGGER.info("No reset marker found - First execution detected");
				return true;
			}

			String lastResetDate = new String(Files.readAllBytes(Paths.get(LAST_RESET_MARKER))).trim();
			boolean isNewDay = !lastResetDate.equals(currentDate);
			
			if (isNewDay) {
				LOGGER.info("Date change detected: Last reset '{}' vs Current '{}'", lastResetDate, currentDate);
			} else {
				LOGGER.debug("Same day: Last reset '{}' == Current '{}'", lastResetDate, currentDate);
			}

			return isNewDay;
		} catch (Exception e) {
			LOGGER.warn("Error checking for new day: {}", e.getMessage());
			return true;
		}
	}

	private static void performDailyReset() {
		try {
			LOGGER.info("Starting Allure daily reset process...");
			createBackupDirectories();

			File resultsDir = new File(ALLURE_RESULTS_DIR);
			if (resultsDir.exists() && resultsDir.listFiles() != null && resultsDir.listFiles().length > 0) {
				backupAllureResults(resultsDir);
			} else {
				LOGGER.debug("No existing Allure results found - skipping backup");
			}

			File reportDir = new File(ALLURE_REPORT_DIR);
			if (reportDir.exists() && reportDir.listFiles() != null && reportDir.listFiles().length > 0) {
				backupAllureReport(reportDir);
			} else {
				LOGGER.debug("No existing Allure report found - skipping backup");
			}

			cleanAllureDirectories();
			updateResetMarker();

			LOGGER.info("✅ Allure daily reset completed successfully");
		} catch (Exception e) {
			LOGGER.error("Error during Allure daily reset: {}", e.getMessage(), e);
		}
	}

	private static void createBackupDirectories() {
		try {
			File allureReportsDir = new File(ALLURE_REPORTS_DIR);
			if (!allureReportsDir.exists()) {
				allureReportsDir.mkdirs();
				LOGGER.debug("Created AllureReports directory: {}", allureReportsDir.getAbsolutePath());
			}

			File backupDir = new File(ALLURE_BACKUP_DIR);
			if (!backupDir.exists()) {
				backupDir.mkdirs();
				LOGGER.debug("Created backup directory: {}", backupDir.getAbsolutePath());
			}

			File resultsDir = new File(ALLURE_RESULTS_DIR);
			if (!resultsDir.exists()) {
				resultsDir.mkdirs();
				LOGGER.debug("Created results directory: {}", resultsDir.getAbsolutePath());
			}

			File reportDir = new File(ALLURE_REPORT_DIR);
			if (!reportDir.exists()) {
				reportDir.mkdirs();
				LOGGER.debug("Created report directory: {}", reportDir.getAbsolutePath());
			}

			File markerParent = new File(LAST_RESET_MARKER).getParentFile();
			if (markerParent != null && !markerParent.exists()) {
				markerParent.mkdirs();
			}
		} catch (Exception e) {
			LOGGER.warn("Error creating AllureReports directories: {}", e.getMessage());
		}
	}

	private static void backupAllureResults(File resultsDir) {
		try {
			String todayDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			String backupPattern = "allure-results_" + todayDate;

			File backupDir = new File(ALLURE_BACKUP_DIR);
			File[] existingBackups = backupDir.listFiles((dir, name) -> 
				name.startsWith(backupPattern) && new File(dir, name).isDirectory()
			);

			if (existingBackups != null && existingBackups.length > 0) {
				LOGGER.debug("Backup already exists for today: {}", existingBackups[0].getName());
				LOGGER.debug("Skipping backup creation to avoid duplicates");
				return;
			}

			String backupName = String.format("allure-results_%s_%s",
					todayDate,
					LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH-mm-ss")));
			File backupLocation = new File(ALLURE_BACKUP_DIR, backupName);

			copyDirectory(resultsDir, backupLocation);

			LOGGER.info("✅ Allure results backed up: {}", backupName);
		} catch (Exception e) {
			LOGGER.warn("Error backing up Allure results: {}", e.getMessage());
		}
	}

	private static void backupAllureReport(File reportDir) {
		try {
			String todayDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			String backupPattern = "allure-report_" + todayDate;

			File backupDir = new File(ALLURE_BACKUP_DIR);
			File[] existingBackups = backupDir.listFiles((dir, name) -> 
				name.startsWith(backupPattern) && new File(dir, name).isDirectory()
			);

			if (existingBackups != null && existingBackups.length > 0) {
				LOGGER.debug("Report backup already exists for today: {}", existingBackups[0].getName());
				LOGGER.debug("Skipping backup creation to avoid duplicates");
				return;
			}

			String backupName = String.format("allure-report_%s_%s",
					todayDate,
					LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH-mm-ss")));
			File backupLocation = new File(ALLURE_BACKUP_DIR, backupName);

			copyDirectory(reportDir, backupLocation);

			LOGGER.info("✅ Allure report backed up: {}", backupName);
		} catch (Exception e) {
			LOGGER.warn("Error backing up Allure report: {}", e.getMessage());
		}
	}

	private static void copyDirectory(File sourceDir, File destDir) throws IOException {
		if (!destDir.exists()) {
			destDir.mkdirs();
		}

		File[] files = sourceDir.listFiles();
		if (files == null) {
			return;
		}

		for (File file : files) {
			File destFile = new File(destDir, file.getName());
			if (file.isDirectory()) {
				copyDirectory(file, destFile);
			} else {
				try (FileInputStream fis = new FileInputStream(file);
						FileOutputStream fos = new FileOutputStream(destFile)) {
					byte[] buffer = new byte[1024];
					int length;
					while ((length = fis.read(buffer)) > 0) {
						fos.write(buffer, 0, length);
					}
				}
			}
		}
	}

	private static void cleanAllureDirectories() {
		try {
			File resultsDir = new File(ALLURE_RESULTS_DIR);
			if (resultsDir.exists()) {
				deleteDirectoryContents(resultsDir);
				LOGGER.debug("Cleaned Allure results directory");
			}

			File reportDir = new File(ALLURE_REPORT_DIR);
			if (reportDir.exists()) {
				deleteDirectoryContents(reportDir);
				LOGGER.debug("Cleaned Allure report directory");
			}
		} catch (Exception e) {
			LOGGER.warn("Error cleaning Allure directories: {}", e.getMessage());
		}
	}

	private static void deleteDirectoryContents(File directory) throws IOException {
		File[] files = directory.listFiles();
		if (files == null) {
			return;
		}

		for (File file : files) {
			if (file.isDirectory()) {
				deleteDirectory(file);
			} else {
				Files.delete(file.toPath());
			}
		}
	}

	private static void deleteDirectory(File directory) throws IOException {
		File[] files = directory.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					deleteDirectory(file);
				} else {
					Files.delete(file.toPath());
				}
			}
		}
		Files.delete(directory.toPath());
	}

	private static void updateResetMarker() {
		try {
			String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			File markerFile = new File(LAST_RESET_MARKER);
			
			File parentDir = markerFile.getParentFile();
			if (parentDir != null && !parentDir.exists()) {
				parentDir.mkdirs();
			}

			try (FileOutputStream fos = new FileOutputStream(markerFile)) {
				fos.write(currentDate.getBytes());
			}

			LOGGER.debug("Reset marker updated: {}", currentDate);
		} catch (Exception e) {
			LOGGER.warn("Error updating reset marker: {}", e.getMessage());
		}
	}

	private static void writeBrowserInfo(FileWriter writer) throws IOException {
		writer.write("# Browser Information\n");
		
		try {
			WebDriver driver = DriverManager.getDriver();
			
			if (driver != null && driver instanceof RemoteWebDriver) {
				Capabilities caps = ((RemoteWebDriver) driver).getCapabilities();
				
				String browserName = caps.getBrowserName();
				if (browserName != null) {
					writer.write("Browser=" + capitalize(browserName) + "\n");
				}
				
				Object browserVersion = caps.getCapability("browserVersion");
				if (browserVersion == null) {
					browserVersion = caps.getCapability("version");
				}
				if (browserVersion != null) {
					writer.write("Browser Version=" + browserVersion + "\n");
				}
				
				Object driverVersion = caps.getCapability("driverVersion");
				if (driverVersion != null) {
					writer.write("Driver Version=" + driverVersion + "\n");
				}
			} else {
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

	private static void writeOperatingSystemInfo(FileWriter writer) throws IOException {
		writer.write("# Operating System\n");
		writer.write("OS=" + System.getProperty("os.name") + "\n");
		writer.write("OS Version=" + System.getProperty("os.version") + "\n");
		writer.write("OS Architecture=" + System.getProperty("os.arch") + "\n");
		writer.write("\n");
	}

	private static void writeTestEnvironmentInfo(FileWriter writer) throws IOException {
		writer.write("# Test Environment\n");
		
		String environment = CommonVariable.ENVIRONMENT != null ? 
			CommonVariable.ENVIRONMENT.toUpperCase() : "QA";
		writer.write("Environment=" + environment + "\n");
		
		String baseUrl = getBaseUrl();
		if (baseUrl != null && !baseUrl.isEmpty()) {
			writer.write("Base URL=" + baseUrl + "\n");
		}
		
		writer.write("\n");
	}

	private static void writeApplicationVersionInfo(FileWriter writer) throws IOException {
		writer.write("# Application Information\n");
		
		String appVersion = System.getProperty("app.version");
		if (appVersion == null || appVersion.isEmpty()) {
			appVersion = "Not Specified";
		}
		writer.write("Application Version=" + appVersion + "\n");
		
		writer.write("\n");
	}

	private static void writeExecutionInfo(FileWriter writer) throws IOException {
		writer.write("# Execution Information\n");
		writer.write("Execution Date=" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "\n");
		writer.write("Execution Time=" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "\n");
		writer.write("Execution DateTime=" + LocalDateTime.now().format(DATE_TIME_FORMATTER) + "\n");
		writer.write("User=" + System.getProperty("user.name") + "\n");
		writer.write("Host=" + getHostName() + "\n");
		writer.write("\n");
	}

	private static void writeJavaInfo(FileWriter writer) throws IOException {
		writer.write("# Java Information\n");
		writer.write("Java Version=" + System.getProperty("java.version") + "\n");
		writer.write("Java Vendor=" + System.getProperty("java.vendor") + "\n");
		writer.write("Java Home=" + System.getProperty("java.home") + "\n");
		writer.write("\n");
	}

	private static String getBrowserFromConfig() {
		try {
			if (CommonVariable.BROWSER != null && !CommonVariable.BROWSER.isEmpty()) {
				return CommonVariable.BROWSER.toUpperCase();
			}
			
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

	private static String getHostName() {
		try {
			return java.net.InetAddress.getLocalHost().getHostName();
		} catch (Exception e) {
			return "Unknown";
		}
	}

	private static String getBaseUrl() {
		try {
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
			
			if (CommonVariable.TESTURL != null) {
				return CommonVariable.TESTURL;
			}
			
			return null;
		} catch (Exception e) {
			LOGGER.debug("Could not get base URL: {}", e.getMessage());
			return null;
		}
	}

	private static String capitalize(String str) {
		if (str == null || str.isEmpty()) {
			return str;
		}
		return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
	}

	private static boolean isAllureReportingEnabled() {
		return CommonVariable.ALLURE_REPORTING_ENABLED == null
				|| !CommonVariable.ALLURE_REPORTING_ENABLED.equalsIgnoreCase("false");
	}
}

