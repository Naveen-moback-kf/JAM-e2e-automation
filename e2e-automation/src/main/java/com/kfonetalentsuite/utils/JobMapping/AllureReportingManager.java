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

import com.kfonetalentsuite.utils.common.ScreenshotHandler;
import org.testng.ITestResult;

import com.kfonetalentsuite.utils.common.CommonVariableManager;
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
			LOGGER.info("Allure Reporting: Disabled");
			return;
		}
		
		LOGGER.info("Allure Reporting: Enabled");
		
		try {
			createBackupDirectories();
			boolean isNewDay = isNewDayDetected();
			
			if (isNewDay) {
				performDailyReset();
			}
		} catch (Exception e) {
			LOGGER.warn("Allure daily reset failed: {}", e.getMessage());
		}
	}

	public static void generateEnvironmentInfo() {
		if (!isAllureReportingEnabled()) {
			return;
		}
		
		try {
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
		} catch (Exception e) {
			LOGGER.warn("Allure environment generation failed: {}", e.getMessage());
		}
	}

	@Override
	public void onTestFailure(ITestResult result) {
		if (!isAllureReportingEnabled()) {
			return;
		}
		
		try {
			String methodName = result.getMethod().getMethodName();
			Throwable throwable = result.getThrowable();
			String errorMessage = throwable != null ? throwable.getMessage() : "Test failed";
			String screenshotPath = ScreenshotHandler.captureFailureScreenshot(methodName, errorMessage);
			
			if (screenshotPath != null) {
				File screenshotFile = new File(screenshotPath);
				
				if (!screenshotFile.exists()) {
					try {
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
				}
			}
		} catch (Exception e) {
			LOGGER.warn("Screenshot attachment failed: {}", e.getMessage());
		}
	}

	public static void attachScreenshotToAllure(String screenshotPath, String attachmentName) {
		try {
			File screenshotFile = new File(screenshotPath);
			
			if (!screenshotFile.exists()) {
				return;
			}
			
			Allure.addAttachment(
				attachmentName != null ? attachmentName : "Screenshot",
				"image/png",
				Files.newInputStream(screenshotFile.toPath()),
				"png"
			);
		} catch (IOException e) {
			LOGGER.warn("Screenshot attachment failed: {}", e.getMessage());
		}
	}

	public static void attachScreenshotBytesToAllure(byte[] screenshotBytes, String attachmentName) {
		try {
			if (screenshotBytes == null || screenshotBytes.length == 0) {
				return;
			}
			
			Allure.addAttachment(
				attachmentName != null ? attachmentName : "Screenshot",
				"image/png",
				new java.io.ByteArrayInputStream(screenshotBytes),
				"png"
			);
		} catch (Exception e) {
			LOGGER.warn("Screenshot attachment failed: {}", e.getMessage());
		}
	}

	public static boolean captureAndAttachScreenshot(String attachmentName) {
		try {
			WebDriver driver = DriverManager.getDriver();
			
			if (driver == null) {
				return false;
			}
			
			TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
			byte[] screenshotBytes = takesScreenshot.getScreenshotAs(OutputType.BYTES);
			attachScreenshotBytesToAllure(screenshotBytes, attachmentName);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private static boolean isNewDayDetected() {
		try {
			String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			File markerFile = new File(LAST_RESET_MARKER);

			if (!markerFile.exists()) {
				return true;
			}

			String lastResetDate = new String(Files.readAllBytes(Paths.get(LAST_RESET_MARKER))).trim();
			return !lastResetDate.equals(currentDate);
		} catch (Exception e) {
			return true;
		}
	}

	private static void performDailyReset() {
		try {
			createBackupDirectories();

			File resultsDir = new File(ALLURE_RESULTS_DIR);
			if (resultsDir.exists() && resultsDir.listFiles() != null && resultsDir.listFiles().length > 0) {
				backupAllureResults(resultsDir);
			}

			File reportDir = new File(ALLURE_REPORT_DIR);
			if (reportDir.exists() && reportDir.listFiles() != null && reportDir.listFiles().length > 0) {
				backupAllureReport(reportDir);
			}

			cleanAllureDirectories();
			updateResetMarker();
		} catch (Exception e) {
			LOGGER.warn("Allure daily reset failed: {}", e.getMessage());
		}
	}

	private static void createBackupDirectories() {
		try {
			new File(ALLURE_REPORTS_DIR).mkdirs();
			new File(ALLURE_BACKUP_DIR).mkdirs();
			new File(ALLURE_RESULTS_DIR).mkdirs();
			new File(ALLURE_REPORT_DIR).mkdirs();
			
			File markerParent = new File(LAST_RESET_MARKER).getParentFile();
			if (markerParent != null) {
				markerParent.mkdirs();
			}
		} catch (Exception e) {
			LOGGER.warn("Directory creation failed: {}", e.getMessage());
		}
	}

	private static void backupAllureResults(File resultsDir) {
		try {
			String todayDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			String backupPattern = "allure-results_" + todayDate;

			File backupDir = new File(ALLURE_BACKUP_DIR);
			File[] existingBackups = backupDir.listFiles((_, name) -> 
				name.startsWith(backupPattern) && new File(backupDir, name).isDirectory()
			);

			if (existingBackups != null && existingBackups.length > 0) {
				return;
			}

			String backupName = String.format("allure-results_%s_%s",
					todayDate,
					LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH-mm-ss")));
			File backupLocation = new File(ALLURE_BACKUP_DIR, backupName);

			copyDirectory(resultsDir, backupLocation);
		} catch (Exception e) {
			LOGGER.warn("Backup failed: {}", e.getMessage());
		}
	}

	private static void backupAllureReport(File reportDir) {
		try {
			String todayDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			String backupPattern = "allure-report_" + todayDate;

			File backupDir = new File(ALLURE_BACKUP_DIR);
			File[] existingBackups = backupDir.listFiles((_, name) -> 
				name.startsWith(backupPattern) && new File(backupDir, name).isDirectory()
			);

			if (existingBackups != null && existingBackups.length > 0) {
				return;
			}

			String backupName = String.format("allure-report_%s_%s",
					todayDate,
					LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH-mm-ss")));
			File backupLocation = new File(ALLURE_BACKUP_DIR, backupName);

			copyDirectory(reportDir, backupLocation);
		} catch (Exception e) {
			LOGGER.warn("Backup failed: {}", e.getMessage());
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
			}

			File reportDir = new File(ALLURE_REPORT_DIR);
			if (reportDir.exists()) {
				deleteDirectoryContents(reportDir);
			}
		} catch (Exception e) {
			LOGGER.warn("Cleanup failed: {}", e.getMessage());
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
		} catch (Exception e) {
			LOGGER.warn("Marker update failed: {}", e.getMessage());
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
		
		String environment = CommonVariableManager.ENVIRONMENT != null ? 
			CommonVariableManager.ENVIRONMENT.toUpperCase() : "QA";
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
			if (CommonVariableManager.BROWSER != null && !CommonVariableManager.BROWSER.isEmpty()) {
				return CommonVariableManager.BROWSER.toUpperCase();
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
			if (CommonVariableManager.ENVIRONMENT != null) {
				String env = CommonVariableManager.ENVIRONMENT.toUpperCase();
				if (env.contains("TEST") && CommonVariableManager.TESTURL != null) {
					return CommonVariableManager.TESTURL;
				} else if (env.contains("STAGE") && CommonVariableManager.STAGEURL != null) {
					return CommonVariableManager.STAGEURL;
				} else if (env.contains("PROD") && CommonVariableManager.PRODUSURL != null) {
					return CommonVariableManager.PRODUSURL;
				}
			}
			
			if (CommonVariableManager.TESTURL != null) {
				return CommonVariableManager.TESTURL;
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
		return CommonVariableManager.ALLURE_REPORTING_ENABLED == null
				|| !CommonVariableManager.ALLURE_REPORTING_ENABLED.equalsIgnoreCase("false");
	}
}

