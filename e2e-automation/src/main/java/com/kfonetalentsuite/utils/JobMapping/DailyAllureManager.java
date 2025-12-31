package com.kfonetalentsuite.utils.JobMapping;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kfonetalentsuite.utils.common.CommonVariable;

/**
 * Daily Allure Report Manager
 * 
 * Similar to DailyExcelTracker, this utility handles:
 * - Daily reset of Allure reports (detects new day)
 * - Automatic backup of old reports to Backup folder
 * - Prevents accumulation of old report data
 * 
 * Integration: Called from SuiteHooks.onStart() before test execution
 */
public class DailyAllureManager {

	private static final Logger LOGGER = LogManager.getLogger(DailyAllureManager.class);

	// Allure directories (similar to ExcelReports structure)
	private static final String ALLURE_REPORTS_DIR = "AllureReports";
	private static final String ALLURE_RESULTS_DIR = "AllureReports/allure-results";
	private static final String ALLURE_REPORT_DIR = "AllureReports/allure-report";
	private static final String ALLURE_BACKUP_DIR = "AllureReports/Backup";

	// Marker file to track last reset date
	private static final String LAST_RESET_MARKER = "AllureReports/.last-reset-date";

	/**
	 * Main entry point: Check for daily reset and perform backup if needed
	 * Called from SuiteHooks.onStart() before test execution
	 */
	public static void checkAndPerformDailyReset() {
		// CONFIGURATION CHECK: Skip Allure daily reset if disabled in config.properties
		if (CommonVariable.ALLURE_REPORTING_ENABLED != null
				&& CommonVariable.ALLURE_REPORTING_ENABLED.equalsIgnoreCase("false")) {
			LOGGER.info("Allure reporting is disabled in config.properties (allure.reporting=false) - Skipping Allure daily reset");
			return; // Exit early - Allure reporting is disabled
		}
		
		try {
			LOGGER.info("=== ALLURE DAILY RESET CHECK ===");
			
			// Ensure AllureReports directory structure exists
			createBackupDirectories();
			
			// Check if it's a new day
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
			// Don't fail the test execution if reset has issues
		}
	}

	/**
	 * Check if it's a new day by comparing current date with last reset date
	 */
	private static boolean isNewDayDetected() {
		try {
			String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			File markerFile = new File(LAST_RESET_MARKER);

			// If marker file doesn't exist, it's a new day (first run ever)
			if (!markerFile.exists()) {
				LOGGER.info("No reset marker found - First execution detected");
				return true;
			}

			// Read last reset date from marker file
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
			// If we can't determine, assume it's a new day to be safe
			return true;
		}
	}

	/**
	 * Perform daily reset: Backup existing reports and clean results
	 */
	private static void performDailyReset() {
		try {
			LOGGER.info("Starting Allure daily reset process...");

			// Step 1: Ensure directory structure exists
			createBackupDirectories();

			// Step 2: Backup existing Allure results (if they exist)
			File resultsDir = new File(ALLURE_RESULTS_DIR);
			if (resultsDir.exists() && resultsDir.listFiles() != null && resultsDir.listFiles().length > 0) {
				backupAllureResults(resultsDir);
			} else {
				LOGGER.debug("No existing Allure results found - skipping backup");
			}

			// Step 3: Backup existing Allure report (if it exists)
			File reportDir = new File(ALLURE_REPORT_DIR);
			if (reportDir.exists() && reportDir.listFiles() != null && reportDir.listFiles().length > 0) {
				backupAllureReport(reportDir);
			} else {
				LOGGER.debug("No existing Allure report found - skipping backup");
			}

			// Step 4: Clean existing results and report directories
			cleanAllureDirectories();

			// Step 5: Update reset marker with current date
			updateResetMarker();

			LOGGER.info("✅ Allure daily reset completed successfully");
		} catch (Exception e) {
			LOGGER.error("Error during Allure daily reset: {}", e.getMessage(), e);
			// Don't throw - allow tests to continue even if reset fails
		}
	}

	/**
	 * Create AllureReports directory structure (similar to ExcelReports)
	 */
	private static void createBackupDirectories() {
		try {
			// Create main AllureReports directory
			File allureReportsDir = new File(ALLURE_REPORTS_DIR);
			if (!allureReportsDir.exists()) {
				allureReportsDir.mkdirs();
				LOGGER.debug("Created AllureReports directory: {}", allureReportsDir.getAbsolutePath());
			}

			// Create backup directory
			File backupDir = new File(ALLURE_BACKUP_DIR);
			if (!backupDir.exists()) {
				backupDir.mkdirs();
				LOGGER.debug("Created backup directory: {}", backupDir.getAbsolutePath());
			}

			// Create results directory
			File resultsDir = new File(ALLURE_RESULTS_DIR);
			if (!resultsDir.exists()) {
				resultsDir.mkdirs();
				LOGGER.debug("Created results directory: {}", resultsDir.getAbsolutePath());
			}

			// Create report directory
			File reportDir = new File(ALLURE_REPORT_DIR);
			if (!reportDir.exists()) {
				reportDir.mkdirs();
				LOGGER.debug("Created report directory: {}", reportDir.getAbsolutePath());
			}

			// Create parent directory for marker file
			File markerParent = new File(LAST_RESET_MARKER).getParentFile();
			if (markerParent != null && !markerParent.exists()) {
				markerParent.mkdirs();
			}
		} catch (Exception e) {
			LOGGER.warn("Error creating AllureReports directories: {}", e.getMessage());
		}
	}

	/**
	 * Backup Allure results directory to Backup folder (once per day only)
	 */
	private static void backupAllureResults(File resultsDir) {
		try {
			String todayDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			String backupPattern = "allure-results_" + todayDate;

			// Check if backup already exists for today
			File backupDir = new File(ALLURE_BACKUP_DIR);
			File[] existingBackups = backupDir.listFiles((dir, name) -> 
				name.startsWith(backupPattern) && new File(dir, name).isDirectory()
			);

			if (existingBackups != null && existingBackups.length > 0) {
				LOGGER.debug("Backup already exists for today: {}", existingBackups[0].getName());
				LOGGER.debug("Skipping backup creation to avoid duplicates");
				return;
			}

			// Create backup with timestamp
			String backupName = String.format("allure-results_%s_%s",
					todayDate,
					LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH-mm-ss")));
			File backupLocation = new File(ALLURE_BACKUP_DIR, backupName);

			// Copy entire results directory
			copyDirectory(resultsDir, backupLocation);

			LOGGER.info("✅ Allure results backed up: {}", backupName);
		} catch (Exception e) {
			LOGGER.warn("Error backing up Allure results: {}", e.getMessage());
		}
	}

	/**
	 * Backup Allure report directory to Backup folder (once per day only)
	 */
	private static void backupAllureReport(File reportDir) {
		try {
			String todayDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			String backupPattern = "allure-report_" + todayDate;

			// Check if backup already exists for today
			File backupDir = new File(ALLURE_BACKUP_DIR);
			File[] existingBackups = backupDir.listFiles((dir, name) -> 
				name.startsWith(backupPattern) && new File(dir, name).isDirectory()
			);

			if (existingBackups != null && existingBackups.length > 0) {
				LOGGER.debug("Report backup already exists for today: {}", existingBackups[0].getName());
				LOGGER.debug("Skipping backup creation to avoid duplicates");
				return;
			}

			// Create backup with timestamp
			String backupName = String.format("allure-report_%s_%s",
					todayDate,
					LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH-mm-ss")));
			File backupLocation = new File(ALLURE_BACKUP_DIR, backupName);

			// Copy entire report directory
			copyDirectory(reportDir, backupLocation);

			LOGGER.info("✅ Allure report backed up: {}", backupName);
		} catch (Exception e) {
			LOGGER.warn("Error backing up Allure report: {}", e.getMessage());
		}
	}

	/**
	 * Copy directory recursively
	 */
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

	/**
	 * Clean Allure results and report directories
	 */
	private static void cleanAllureDirectories() {
		try {
			// Clean results directory
			File resultsDir = new File(ALLURE_RESULTS_DIR);
			if (resultsDir.exists()) {
				deleteDirectoryContents(resultsDir);
				LOGGER.debug("Cleaned Allure results directory");
			}

			// Clean report directory
			File reportDir = new File(ALLURE_REPORT_DIR);
			if (reportDir.exists()) {
				deleteDirectoryContents(reportDir);
				LOGGER.debug("Cleaned Allure report directory");
			}
		} catch (Exception e) {
			LOGGER.warn("Error cleaning Allure directories: {}", e.getMessage());
		}
	}

	/**
	 * Delete all contents of a directory (but keep the directory itself)
	 */
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

	/**
	 * Delete directory recursively
	 */
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

	/**
	 * Update reset marker file with current date
	 */
	private static void updateResetMarker() {
		try {
			String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			File markerFile = new File(LAST_RESET_MARKER);
			
			// Create parent directory if it doesn't exist
			File parentDir = markerFile.getParentFile();
			if (parentDir != null && !parentDir.exists()) {
				parentDir.mkdirs();
			}

			// Write current date to marker file
			try (FileOutputStream fos = new FileOutputStream(markerFile)) {
				fos.write(currentDate.getBytes());
			}

			LOGGER.debug("Reset marker updated: {}", currentDate);
		} catch (Exception e) {
			LOGGER.warn("Error updating reset marker: {}", e.getMessage());
		}
	}
	
}

