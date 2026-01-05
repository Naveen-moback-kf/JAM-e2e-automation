package com.kfonetalentsuite.utils.JobMapping;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.kfonetalentsuite.utils.common.CommonVariable;

import java.io.*;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeepAwakeUtil {

	private static final Logger LOGGER = (Logger) LogManager.getLogger();
	private static boolean isInitialized = false;
	private static final Object lock = new Object();
	private static final String BACKUP_FILE = "system_power_settings_backup.properties";
	private static boolean isSuiteExecution = false;
	private static final String BACKUP_MARKER_FILE = ".power_settings_modified";

	public static void initialize() {
		initializeInternal(false);
	}

	private static void initializeInternal(boolean isSuite) {
		synchronized (lock) {
			try {
				// CRITICAL SAFETY CHECK: Always check and restore if settings were left
				// modified
				// This runs even when flag=false to clean up from previous runs
				checkAndRestoreIfNeeded();

				// Check if already initialized (prevent duplicate calls)
				if (isInitialized) {
					return;
				}

				// Store execution type
				isSuiteExecution = isSuite;

				if (isKeepAwakeEnabled()) {
					// ENHANCED: Backup current settings before modifying
					if (backupCurrentPowerSettings()) {
						enableKeepAwake();
						createModificationMarker();
						isInitialized = true;
						// Keep System Awake enabled - no log needed
					} else {
						LOGGER.warn(
								" Failed to backup current power settings - Keep Awake feature disabled for safety");
					}
				} else {
					// Apply Windows recommended power settings
					applyRecommendedPowerSettings();
				}
			} catch (Exception e) {
				LOGGER.error(" Failed to initialize Keep System Awake functionality: " + e.getMessage(), e);
			}
		}
	}

	public static void shutdown() {
		synchronized (lock) {
			try {
				// Check if already shut down (prevent duplicate calls)
				if (!isInitialized) {
					return;
				}

				if (isKeepAwakeEnabled()) {
					// ENHANCED: Restore original settings from backup
					if (restoreOriginalPowerSettings()) {
						// Successfully restored - no log needed
					} else {
						LOGGER.warn(" Could not restore from backup - applying safe default values");
						disableKeepAwake(); // Fallback to safe defaults
					}

					removeModificationMarker();
					cleanupBackupFile();
					isInitialized = false;
					isSuiteExecution = false;
					LOGGER.debug("Keep System Awake functionality disabled successfully");
				}
			} catch (Exception e) {
				LOGGER.error(" Failed to shutdown Keep System Awake functionality: " + e.getMessage(), e);
			}
		}
	}

	private static boolean isKeepAwakeEnabled() {
		return CommonVariable.KEEP_SYSTEM_AWAKE != null && CommonVariable.KEEP_SYSTEM_AWAKE.equalsIgnoreCase("true");
	}

	private static boolean backupCurrentPowerSettings() {
		try {
			// Backing up current power settings silently

			Properties backup = new Properties();

			// Query and backup each setting
			backup.setProperty("standby.ac", queryPowerSetting("standby-timeout-ac"));
			backup.setProperty("standby.dc", queryPowerSetting("standby-timeout-dc"));
			backup.setProperty("monitor.ac", queryPowerSetting("monitor-timeout-ac"));
			backup.setProperty("monitor.dc", queryPowerSetting("monitor-timeout-dc"));
			backup.setProperty("hibernate.ac", queryPowerSetting("hibernate-timeout-ac"));
			backup.setProperty("hibernate.dc", queryPowerSetting("hibernate-timeout-dc"));
			backup.setProperty("backup.timestamp", String.valueOf(System.currentTimeMillis()));

			// Save to file
			File backupFile = new File(BACKUP_FILE);
			try (FileOutputStream out = new FileOutputStream(backupFile)) {
				backup.store(out, "Power Settings Backup - DO NOT EDIT MANUALLY");
			}

			// Power settings backed up successfully

			return true;

		} catch (Exception e) {
			LOGGER.error(" Failed to backup power settings: " + e.getMessage(), e);
			return false;
		}
	}

	private static String queryPowerSetting(String settingName) {
		// Return null on non-Windows systems
		if (!isWindows()) {
			return null;
		}

		try {
			// Determine which subgroup to query based on setting type
			String subgroup = settingName.contains("monitor") ? "SUB_VIDEO" : "SUB_SLEEP";

			// Map setting names to their GUIDs for more reliable querying
			String settingGuid = getSettingGuid(settingName);

			ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c",
					"powercfg /q SCHEME_CURRENT " + subgroup);
			processBuilder.redirectErrorStream(true);
			Process process = processBuilder.start();

			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line;
			boolean inTargetSetting = false;
			String targetType = settingName.contains("-ac") ? "Current AC Power Setting Index:"
					: "Current DC Power Setting Index:";

			while ((line = reader.readLine()) != null) {
				// Look for the setting GUID or friendly name
				if (settingGuid != null && line.contains(settingGuid)) {
					inTargetSetting = true;
				} else if (line.toLowerCase().contains(getSettingFriendlyName(settingName).toLowerCase())) {
					inTargetSetting = true;
				}

				// If we found our setting, look for the power index
				if (inTargetSetting && line.contains(targetType)) {
					// Extract hex value and convert to minutes
					Pattern pattern = Pattern.compile("0x([0-9a-fA-F]+)");
					Matcher matcher = pattern.matcher(line);
					if (matcher.find()) {
						String hexValue = matcher.group(1);
						long seconds = Long.parseLong(hexValue, 16);
						long minutes = seconds / 60;
						reader.close();
						process.waitFor();
						return String.valueOf(minutes);
					}
				}
			}

			reader.close();
			process.waitFor();

			// If we couldn't query, return safe default
			LOGGER.debug("Could not query " + settingName + ", using default value 30");
			return "30";

		} catch (Exception e) {
			LOGGER.debug("Error querying " + settingName + ": " + e.getMessage());
			return "30"; // Safe default
		}
	}

	private static String getSettingFriendlyName(String settingName) {
		if (settingName.contains("standby")) {
			return "Sleep after";
		} else if (settingName.contains("monitor")) {
			return "Turn off display after";
		} else if (settingName.contains("hibernate")) {
			return "Hibernate after";
		}
		return settingName;
	}

	private static String getSettingGuid(String settingName) {
		// Common power setting GUIDs
		if (settingName.contains("standby")) {
			return "29f6c1db-86da-48c5-9fdb-f2b67b1f44da"; // Sleep timeout
		} else if (settingName.contains("monitor")) {
			return "3c0bc021-c8a8-4e07-a973-6b14cbcb2b7e"; // Display timeout
		} else if (settingName.contains("hibernate")) {
			return "9d7815a6-7ee4-497e-8888-515a05f02364"; // Hibernate timeout
		}
		return null;
	}

	private static boolean restoreOriginalPowerSettings() {
		try {
			File backupFile = new File(BACKUP_FILE);

			if (!backupFile.exists()) {
				LOGGER.debug("Backup file not found: " + BACKUP_FILE);
				return false;
			}

			// Restoring original power settings from backup silently

			Properties backup = new Properties();
			try (FileInputStream in = new FileInputStream(backupFile)) {
				backup.load(in);
			}

			// Restore each setting from backup
			String standbyAC = backup.getProperty("standby.ac", "30");
			String standbyDC = backup.getProperty("standby.dc", "30");
			String monitorAC = backup.getProperty("monitor.ac", "15");
			String monitorDC = backup.getProperty("monitor.dc", "15");
			String hibernateAC = backup.getProperty("hibernate.ac", "60");
			String hibernateDC = backup.getProperty("hibernate.dc", "60");

			// Apply restored settings
			executePowerCommand("powercfg /change standby-timeout-ac " + standbyAC);
			executePowerCommand("powercfg /change standby-timeout-dc " + standbyDC);
			executePowerCommand("powercfg /change monitor-timeout-ac " + monitorAC);
			executePowerCommand("powercfg /change monitor-timeout-dc " + monitorDC);
			executePowerCommand("powercfg /change hibernate-timeout-ac " + hibernateAC);
			executePowerCommand("powercfg /change hibernate-timeout-dc " + hibernateDC);

			// Original power settings restored successfully

			return true;

		} catch (Exception e) {
			LOGGER.error(" Failed to restore power settings from backup: " + e.getMessage(), e);
			return false;
		}
	}

	private static void checkAndRestoreIfNeeded() {
		try {
			File markerFile = new File(BACKUP_MARKER_FILE);

			if (markerFile.exists()) {
				// Silent restoration - only log if it fails

				if (restoreOriginalPowerSettings()) {
					// Successfully restored - no log needed
					removeModificationMarker();
					cleanupBackupFile();
				} else {
					LOGGER.debug("Could not restore from backup - please check your power settings manually");
				}
			}
		} catch (Exception e) {
			LOGGER.debug("Error during safety check: " + e.getMessage());
		}
	}

	private static void applyRecommendedPowerSettings() {
		try {
			// Windows Recommended values (as per Windows 11 defaults)
			final int RECOMMENDED_SCREEN_AC = 5; // 5 minutes (Plugged in)
			final int RECOMMENDED_SLEEP_AC = 5; // 5 minutes (Plugged in)
			final int RECOMMENDED_SCREEN_DC = 3; // 3 minutes (On battery)
			final int RECOMMENDED_SLEEP_DC = 3; // 3 minutes (On battery)

			// Apply screen timeout settings
			executePowerCommand("powercfg /change monitor-timeout-ac " + RECOMMENDED_SCREEN_AC);
			executePowerCommand("powercfg /change monitor-timeout-dc " + RECOMMENDED_SCREEN_DC);

			// Apply sleep timeout settings
			executePowerCommand("powercfg /change standby-timeout-ac " + RECOMMENDED_SLEEP_AC);
			executePowerCommand("powercfg /change standby-timeout-dc " + RECOMMENDED_SLEEP_DC);

		} catch (Exception e) {
			// Silently ignore - non-critical operation
		}
	}

	private static void createModificationMarker() {
		try {
			File markerFile = new File(BACKUP_MARKER_FILE);
			if (!markerFile.exists()) {
				markerFile.createNewFile();
				// Created modification marker file
			}
		} catch (Exception e) {
			LOGGER.warn(" Could not create modification marker: " + e.getMessage());
		}
	}

	private static void removeModificationMarker() {
		try {
			File markerFile = new File(BACKUP_MARKER_FILE);
			if (markerFile.exists()) {
				markerFile.delete();
				// Removed modification marker file
			}
		} catch (Exception e) {
			LOGGER.warn(" Could not remove modification marker: " + e.getMessage());
		}
	}

	private static void cleanupBackupFile() {
		try {
			File backupFile = new File(BACKUP_FILE);
			if (backupFile.exists()) {
				backupFile.delete();
				// Cleaned up backup file
			}
		} catch (Exception e) {
			LOGGER.warn(" Could not cleanup backup file: " + e.getMessage());
		}
	}

	private static void enableKeepAwake() {
		try {
			int timeout;

			if (isSuiteExecution) {
				// Test Suite execution: Never sleep
				timeout = 0;
			} else {
				// Individual Runner execution: 30 minutes
				timeout = 30;
			}

			// Apply sleep timeout
			executePowerCommand("powercfg /change standby-timeout-ac " + timeout);
			executePowerCommand("powercfg /change standby-timeout-dc " + timeout);

			// Apply display timeout
			executePowerCommand("powercfg /change monitor-timeout-ac " + timeout);
			executePowerCommand("powercfg /change monitor-timeout-dc " + timeout);

			// Apply hibernate timeout
			executePowerCommand("powercfg /change hibernate-timeout-ac " + timeout);
			executePowerCommand("powercfg /change hibernate-timeout-dc " + timeout);

			// Power settings configured successfully

		} catch (Exception e) {
			LOGGER.warn(" Failed to configure system power settings: " + e.getMessage());
			LOGGER.warn(" System may still go to sleep during test runs");
		}
	}

	private static void disableKeepAwake() {
		try {
			// Restore safe default sleep timeout (30 minutes)
			executePowerCommand("powercfg /change standby-timeout-ac 30");
			executePowerCommand("powercfg /change standby-timeout-dc 30");

			// Restore safe default display timeout (15 minutes)
			executePowerCommand("powercfg /change monitor-timeout-ac 15");
			executePowerCommand("powercfg /change monitor-timeout-dc 15");

			// Restore safe default hibernate timeout (60 minutes)
			executePowerCommand("powercfg /change hibernate-timeout-ac 60");
			executePowerCommand("powercfg /change hibernate-timeout-dc 60");

			// System power settings restored to safe default values

		} catch (Exception e) {
			LOGGER.warn(" Failed to restore system power settings: " + e.getMessage());
		}
	}

	private static boolean isWindows() {
		String os = System.getProperty("os.name").toLowerCase();
		return os.contains("win");
	}

	private static void executePowerCommand(String command) {
		// Skip power commands on non-Windows systems (Linux, Mac, etc.)
		if (!isWindows()) {
			return;
		}

		try {
			ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
			processBuilder.redirectErrorStream(true);
			Process process = processBuilder.start();

			int exitCode = process.waitFor();
			if (exitCode != 0) {
				LOGGER.warn(" Power command failed with exit code " + exitCode + ": " + command);
			}

		} catch (Exception e) {
			LOGGER.warn(" Failed to execute power command: " + command + " - " + e.getMessage());
		}
	}

}
