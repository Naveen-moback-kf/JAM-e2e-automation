package com.kfonetalentsuite.utils.JobMapping;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.kfonetalentsuite.utils.common.CommonVariable;

import java.io.*;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ENHANCED Utility class to manage system power settings during test execution
 * Prevents system from going to sleep during long test runs
 * 
 * FEATURES:
 * - Intelligently backs up YOUR actual current power settings before modifying
 * - Restores YOUR original settings (not hardcoded defaults)
 * - Safety check on startup to detect and restore if settings were left modified
 * - Survives crashes and flag changes
 * 
 * POWER SETTINGS BEHAVIOR:
 * 
 * When keepSystemAwake=false:
 *    Applies Windows Recommended settings:
 *      Plugged in: Screen 5min, Sleep 5min
 *      On battery: Screen 3min, Sleep 3min
 * 
 * When keepSystemAwake=true:
 *    Individual Runner execution (initialize()):
 *      All timeouts: 30 minutes
 *    Test Suite execution (initializeForSuite()):
 *      All timeouts: Never (0)
 */
public class KeepAwakeUtil {
    
    private static final Logger LOGGER = (Logger) LogManager.getLogger();
    private static boolean isInitialized = false;
    private static final Object lock = new Object();
    private static final String BACKUP_FILE = "system_power_settings_backup.properties";
    private static boolean isSuiteExecution = false;
    private static final String BACKUP_MARKER_FILE = ".power_settings_modified";
    
    /**
     * Initialize keep awake functionality for INDIVIDUAL RUNNER execution
     * Sets timeouts to 30 minutes when keepSystemAwake=true
     */
    public static void initialize() {
        initializeInternal(false);
    }
    
    /**
     * Initialize keep awake functionality for TEST SUITE execution
     * Sets timeouts to Never (0) when keepSystemAwake=true
     */
    public static void initializeForSuite() {
        initializeInternal(true);
    }
    
    /**
     * Internal initialization method
     * SUITE-LEVEL: This method should be called ONCE at the start of test execution
     * Thread-safe implementation prevents duplicate initialization
     * 
     * ENHANCED: Now includes safety check to restore settings if they were left modified
     * CRITICAL: Safety check runs REGARDLESS of flag state to ensure system protection
     */
    private static void initializeInternal(boolean isSuite) {
        synchronized (lock) {
            try {
                // CRITICAL SAFETY CHECK: Always check and restore if settings were left modified
                // This runs even when flag=false to clean up from previous runs
                checkAndRestoreIfNeeded();
                
                // Check if already initialized (prevent duplicate calls)
                if (isInitialized) {
                    return;
                }
                
                // Store execution type
                isSuiteExecution = isSuite;
                
                if (isKeepAwakeEnabled()) {
                    String executionType = isSuiteExecution ? "Test Suite" : "Individual Runner";
                    LOGGER.info("Initializing Keep System Awake functionality for " + executionType + "...");
                    
                    // ENHANCED: Backup current settings before modifying
                    if (backupCurrentPowerSettings()) {
                        enableKeepAwake();
                        createModificationMarker();
                        isInitialized = true;
                        LOGGER.info("Keep System Awake functionality enabled successfully");
                        
                        if (isSuiteExecution) {
                            LOGGER.info("System will remain awake indefinitely (Never sleep) throughout test suite execution");
                        } else {
                            LOGGER.info("System will use 30-minute timeouts throughout individual runner execution");
                        }
                        LOGGER.info("Your original power settings have been backed up and will be restored after execution");
                    } else {
                        LOGGER.warn(" Failed to backup current power settings - Keep Awake feature disabled for safety");
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
    
    /**
     * Shutdown keep awake functionality
     * SUITE-LEVEL: This method should be called ONCE at the end of test suite execution
     * Thread-safe implementation prevents duplicate shutdown
     * 
     * ENHANCED: Now restores YOUR original settings (not hardcoded defaults)
     */
    public static void shutdown() {
        synchronized (lock) {
            try {
                // Check if already shut down (prevent duplicate calls)
                if (!isInitialized) {
                    return;
                }
                
                if (isKeepAwakeEnabled()) {
                    LOGGER.info(" Shutting down Keep System Awake functionality...");
                    
                    // ENHANCED: Restore original settings from backup
                    if (restoreOriginalPowerSettings()) {
                        LOGGER.info("... Your original power settings have been restored successfully");
                    } else {
                        LOGGER.warn(" Could not restore from backup - applying safe default values");
                        disableKeepAwake(); // Fallback to safe defaults
                    }
                    
                    removeModificationMarker();
                    cleanupBackupFile();
                    isInitialized = false;
                    isSuiteExecution = false;
                    LOGGER.info("... Keep System Awake functionality disabled successfully");
                }
            } catch (Exception e) {
                LOGGER.error(" Failed to shutdown Keep System Awake functionality: " + e.getMessage(), e);
            }
        }
    }
    
    /**
     * Check if keep awake functionality is enabled in configuration
     */
    private static boolean isKeepAwakeEnabled() {
        return CommonVariable.KEEP_SYSTEM_AWAKE != null && 
               CommonVariable.KEEP_SYSTEM_AWAKE.equalsIgnoreCase("true");
    }
    
    /**
     * ENHANCED: Backup current power settings before modifying them
     * Queries actual Windows power configuration and saves to file
     */
    private static boolean backupCurrentPowerSettings() {
        try {
            LOGGER.debug("Backing up current power settings...");
            
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
            
            LOGGER.debug("Power settings backed up successfully");
            
            return true;
            
        } catch (Exception e) {
            LOGGER.error(" Failed to backup power settings: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Query a specific power setting from Windows
     * Uses powercfg /query with appropriate subgroup (SUB_SLEEP or SUB_VIDEO)
     */
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
            
            ProcessBuilder processBuilder = new ProcessBuilder(
                "cmd.exe", "/c", "powercfg /q SCHEME_CURRENT " + subgroup
            );
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream())
            );
            
            String line;
            boolean inTargetSetting = false;
            String targetType = settingName.contains("-ac") ? "Current AC Power Setting Index:" : "Current DC Power Setting Index:";
            
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
    
    /**
     * Get the friendly name for a power setting
     */
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
    
    /**
     * Get the GUID for a power setting (for more reliable matching)
     */
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
    
    /**
     * ENHANCED: Restore original power settings from backup file
     */
    private static boolean restoreOriginalPowerSettings() {
        try {
            File backupFile = new File(BACKUP_FILE);
            
            if (!backupFile.exists()) {
                LOGGER.warn(" Backup file not found: " + BACKUP_FILE);
                return false;
            }
            
            LOGGER.debug("Restoring original power settings from backup...");
            
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
            
            LOGGER.debug("Original power settings restored successfully");
            
            return true;
            
        } catch (Exception e) {
            LOGGER.error(" Failed to restore power settings from backup: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * ENHANCED: Safety check - detect if settings were left modified from previous run
     */
    private static void checkAndRestoreIfNeeded() {
        try {
            File markerFile = new File(BACKUP_MARKER_FILE);
            
            if (markerFile.exists()) {
                LOGGER.warn(" DETECTED: Power settings were left modified from a previous run!");
                LOGGER.info(" Attempting automatic restoration...");
                
                if (restoreOriginalPowerSettings()) {
                    LOGGER.info("... Successfully restored your original power settings");
                    removeModificationMarker();
                    cleanupBackupFile();
                } else {
                    LOGGER.warn(" Could not restore from backup - please check your power settings manually");
                }
            }
        } catch (Exception e) {
            LOGGER.warn(" Error during safety check: " + e.getMessage());
        }
    }
    
    /**
     * NEW: Apply Windows Recommended power settings when Keep Awake is disabled
     * Sets power settings to Windows default recommended values:
     * - Plugged in: Screen 5min, Sleep 5min
     * - On battery: Screen 3min, Sleep 3min
     */
    private static void applyRecommendedPowerSettings() {
        try {
            // Windows Recommended values (as per Windows 11 defaults)
            final int RECOMMENDED_SCREEN_AC = 5;    // 5 minutes (Plugged in)
            final int RECOMMENDED_SLEEP_AC = 5;     // 5 minutes (Plugged in)
            final int RECOMMENDED_SCREEN_DC = 3;    // 3 minutes (On battery)
            final int RECOMMENDED_SLEEP_DC = 3;     // 3 minutes (On battery)
            
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
    
    /**
     * Create a marker file to indicate settings have been modified
     */
    private static void createModificationMarker() {
        try {
            File markerFile = new File(BACKUP_MARKER_FILE);
            if (!markerFile.exists()) {
                markerFile.createNewFile();
                LOGGER.debug("Created modification marker file");
            }
        } catch (Exception e) {
            LOGGER.warn(" Could not create modification marker: " + e.getMessage());
        }
    }
    
    /**
     * Remove the modification marker file
     */
    private static void removeModificationMarker() {
        try {
            File markerFile = new File(BACKUP_MARKER_FILE);
            if (markerFile.exists()) {
                markerFile.delete();
                LOGGER.debug("Removed modification marker file");
            }
        } catch (Exception e) {
            LOGGER.warn(" Could not remove modification marker: " + e.getMessage());
        }
    }
    
    /**
     * Clean up the backup file after successful restoration
     */
    private static void cleanupBackupFile() {
        try {
            File backupFile = new File(BACKUP_FILE);
            if (backupFile.exists()) {
                backupFile.delete();
                LOGGER.debug("Cleaned up backup file");
            }
        } catch (Exception e) {
            LOGGER.warn(" Could not cleanup backup file: " + e.getMessage());
        }
    }
    
    /**
     * Enable keep awake functionality using Windows powercfg commands
     * - Test Suite: Sets all timeouts to 0 (Never)
     * - Individual Runner: Sets all timeouts to 30 minutes
     */
    private static void enableKeepAwake() {
        try {
            int timeout;
            String timeoutDescription;
            
            if (isSuiteExecution) {
                // Test Suite execution: Never sleep
                timeout = 0;
                timeoutDescription = "Never";
            } else {
                // Individual Runner execution: 30 minutes
                timeout = 30;
                timeoutDescription = "30 minutes";
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
            
            LOGGER.debug("Power settings configured: " + timeoutDescription);
            
        } catch (Exception e) {
            LOGGER.warn(" Failed to configure system power settings: " + e.getMessage());
            LOGGER.warn(" System may still go to sleep during test runs");
        }
    }
    
    /**
     * FALLBACK: Disable keep awake functionality and restore safe default power settings
     * NOTE: This is only used as a fallback when backup restoration fails
     * Normal operation uses restoreOriginalPowerSettings() instead
     */
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
            
            LOGGER.info("... System power settings restored to safe default values");
            
        } catch (Exception e) {
            LOGGER.warn(" Failed to restore system power settings: " + e.getMessage());
        }
    }
    
    /**
     * Check if running on Windows OS
     */
    private static boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("win");
    }
    
    /**
     * Execute Windows powercfg command (Windows only)
     */
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
    
    /**
     * Get current keep awake status
     */
    public static boolean isKeepAwakeActive() {
        return isInitialized && isKeepAwakeEnabled();
    }
}
