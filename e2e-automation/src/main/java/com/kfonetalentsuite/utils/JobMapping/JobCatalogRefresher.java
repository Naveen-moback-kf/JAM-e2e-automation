package com.kfonetalentsuite.utils.JobMapping;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * JobCatalogRefresher - Automatically refreshes Job Catalog CSV with unique identifiers
 * 
 * This utility runs automatically before each Runner execution to ensure
 * unique Job Codes and Job Titles for every test run.
 * 
 * @author AI Assistant
 */
public class JobCatalogRefresher {
    
    private static final Logger LOGGER = LogManager.getLogger(JobCatalogRefresher.class);
    private static final String CSV_FILE_PATH = "src/test/resources/Job Catalog with 100 profiles.csv";
    private static final String BACKUP_DIR = "src/test/resources/JobCatalogBackups";
    private static boolean hasRefreshedThisSession = false;
    
    /**
     * Refreshes the Job Catalog CSV with unique identifiers based on current timestamp
     * Only refreshes once per JVM session to avoid multiple refreshes
     * 
     * @return true if refresh successful or already refreshed, false otherwise
     */
    public static synchronized boolean refreshJobCatalog() {
        // Skip if already refreshed in this session
        if (hasRefreshedThisSession) {
            LOGGER.info("Job Catalog already refreshed in this session - skipping");
            return true;
        }
        
        try {
            LOGGER.info("");
            LOGGER.info("    Refreshing Job Catalog with Unique Identifiers");
            LOGGER.info("");
            
            // Read and update CSV
            Path csvPath = Paths.get(CSV_FILE_PATH);
            
            if (!Files.exists(csvPath)) {
                LOGGER.error(" CSV file not found: {}", CSV_FILE_PATH);
                return false;
            }
            
            // Create backup before modification
            createBackup(csvPath);
            
            // Generate unique suffix based on current timestamp
            String uniqueSuffix = generateUniqueTimestamp();
            LOGGER.info(" Generated unique timestamp: {}", uniqueSuffix);
            
            List<String> lines = Files.readAllLines(csvPath, StandardCharsets.UTF_8);
            
            if (lines.isEmpty() || lines.size() < 2) {
                LOGGER.error(" CSV file is empty or has no data rows!");
                return false;
            }
            
            // Update all data rows (skip header)
            List<String> updatedLines = new ArrayList<>();
            updatedLines.add(lines.get(0)); // Keep header unchanged
            
            int updatedCount = 0;
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (!line.isEmpty()) {
                    String updatedLine = updateJobCodeAndTitle(line, uniqueSuffix, i);
                    updatedLines.add(updatedLine);
                    updatedCount++;
                }
            }
            
            // Write updated content back to file
            Files.write(csvPath, updatedLines, StandardCharsets.UTF_8);
            
            hasRefreshedThisSession = true;
            
            LOGGER.info("... Successfully refreshed {} job profiles", updatedCount);
            LOGGER.info(" Unique Suffix Applied: {}", uniqueSuffix);
            LOGGER.info(" File Location: {}", CSV_FILE_PATH);
            LOGGER.info("");
            
            return true;
            
        } catch (Exception e) {
            LOGGER.error(" Failed to refresh Job Catalog: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Generates unique timestamp suffix in format: YYYYMMDDHHmmss
     * Example: 20251023143025 (Oct 23, 2025 at 2:30:25 PM)
     */
    private static String generateUniqueTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(new Date());
    }
    
    /**
     * Updates a single CSV line with new unique Job Code and Title
     */
    private static String updateJobCodeAndTitle(String line, String uniqueSuffix, int lineNumber) {
        try {
            // Split CSV line preserving commas
            String[] fields = line.split(",", -1);
            
            if (fields.length < 2) {
                LOGGER.warn(" Line {} has insufficient fields, keeping as-is", lineNumber);
                return line;
            }
            
            // Update Job Code (first column) - Add timestamp suffix
            String jobCode = fields[0].trim();
            String baseJobCode = extractBaseJobCode(jobCode);
            String newJobCode = String.format("%s%02d-%s", baseJobCode, lineNumber, uniqueSuffix);
            
            // Update Job Title (second column) - Add timestamp suffix
            String jobTitle = fields[1].trim();
            String baseJobTitle = extractBaseJobTitle(jobTitle);
            String newJobTitle = String.format("%s %s", baseJobTitle, uniqueSuffix);
            
            // Rebuild the line with updated values
            fields[0] = newJobCode;
            fields[1] = " " + newJobTitle; // Preserve leading space for consistency
            
            return String.join(",", fields);
            
        } catch (Exception e) {
            LOGGER.error(" Error updating line {}: {}", lineNumber, e.getMessage());
            return line;
        }
    }
    
    /**
     * Extracts base job code (e.g., "JOB-ABCDE01-20251023143025" -> "JOB-ABCDE")
     * Also handles: "JOB-ABCDE01" -> "JOB-ABCDE"
     */
    private static String extractBaseJobCode(String jobCode) {
        // Remove timestamp suffix (e.g., "-20251023143025" or similar patterns)
        String withoutTimestamp = jobCode.replaceAll("-\\d{10,}$", "");
        
        // Remove trailing digits to get base code (e.g., "JOB-ABCDE01" -> "JOB-ABCDE")
        String base = withoutTimestamp.replaceAll("\\d+$", "");
        
        return base.isEmpty() ? "JOB-ABCDE" : base;
    }
    
    /**
     * Extracts base job title (removes old timestamp suffix)
     * Example: "Automation Engineer ABCDE0809" -> "Automation Engineer"
     */
    private static String extractBaseJobTitle(String jobTitle) {
        // Remove timestamp-like suffix (alphanumeric pattern at end)
        String base = jobTitle.replaceAll("\\s+[A-Z0-9]+\\d{4,}\\s*$", "");
        return base.isEmpty() ? jobTitle : base.trim();
    }
    
    /**
     * Force refresh even if already refreshed (useful for testing)
     */
    public static synchronized boolean forceRefresh() {
        hasRefreshedThisSession = false;
        return refreshJobCatalog();
    }
    
    /**
     * Reset the session flag (useful for testing)
     */
    public static synchronized void resetSessionFlag() {
        hasRefreshedThisSession = false;
    }
    
    /**
     * Create backup of Job Catalog CSV for every refresh
     * Backups are stored in src/test/resources/JobCatalogBackups/ directory with unique timestamps
     */
    private static void createBackup(Path originalFile) {
        try {
            // Create backup directory if it doesn't exist
            Path backupDir = Paths.get(BACKUP_DIR);
            if (!Files.exists(backupDir)) {
                Files.createDirectories(backupDir);
                LOGGER.info(" Created backup directory: {}", BACKUP_DIR);
            }
            
            // Create new backup with full timestamp (including milliseconds for uniqueness)
            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS").format(new Date());
            String backupFileName = String.format("JobCatalog_Backup_%s.csv", timestamp);
            Path backupFile = backupDir.resolve(backupFileName);
            
            // Copy original to backup
            Files.copy(originalFile, backupFile, StandardCopyOption.REPLACE_EXISTING);
            
            LOGGER.info(" Backup created: {}", backupFileName);
            LOGGER.info(" Backup location: {}", backupFile.toAbsolutePath());
            
        } catch (Exception e) {
            LOGGER.warn(" Could not create backup: {}", e.getMessage());
            // Don't fail the main operation if backup fails
        }
    }
}

