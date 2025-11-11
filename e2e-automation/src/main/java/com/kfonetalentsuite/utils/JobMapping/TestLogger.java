package com.kfonetalentsuite.utils.JobMapping;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;
import com.kfonetalentsuite.utils.common.CommonVariable;

/**
 * TestLogger - Centralized logging wrapper for test execution
 * 
 * This utility class provides a unified interface for logging test steps that:
 * - Always logs to Log4j (for file-based logs)
 * - Conditionally logs to Extent Reports (based on configuration flag)
 * - Provides consistent logging patterns across the framework
 * 
 * Configuration:
 * - Set extent.reporting.enabled=true in config.properties to enable Extent logging
 * - Set extent.reporting.enabled=false to disable Extent logging (Log4j still works)
 * 
 * Benefits:
 * - Single point of control for Extent reporting
 * - Easy to enable/disable Extent without code changes
 * - Maintains Log4j logging regardless of Extent status
 * - Improves performance when Extent is disabled
 * 
 * Usage:
 * Replace: ExtentCucumberAdapter.addTestStepLog("message");
 * With:    TestLogger.log("message");
 * 
 * @author AI Auto Mapping Framework
 * @version 1.0
 */
public class TestLogger {
    
    private static final Logger LOGGER = LogManager.getLogger(TestLogger.class);
    
    // Cache the configuration flag for better performance
    private static Boolean extentEnabled = null;
    
    /**
     * Check if Extent reporting is enabled
     * Caches the result to avoid repeated property lookups
     * 
     * @return true if Extent reporting is enabled, false otherwise
     */
    private static boolean isExtentEnabled() {
        if (extentEnabled == null) {
            try {
                String configValue = CommonVariable.EXTENT_REPORTING_ENABLED;
                extentEnabled = configValue == null || 
                               !configValue.trim().equalsIgnoreCase("false");
                
                if (extentEnabled) {
                    LOGGER.info("✅ Extent Reporting is ENABLED - HTML reports will be generated with detailed test steps");
                } else {
                    LOGGER.info("⚠️ Extent Reporting is DISABLED - Only Log4j and Excel reports will be generated");
                }
            } catch (Exception e) {
                // Default to enabled if there's any issue reading the configuration
                extentEnabled = true;
                LOGGER.warn("Could not read extent.reporting.enabled configuration, defaulting to ENABLED");
            }
        }
        return extentEnabled;
    }
    
    /**
     * Log a test step message
     * - Always logs to Log4j
     * - Conditionally logs to Extent Reports based on configuration
     * 
     * @param message The message to log
     */
    public static void log(String message) {
        // Always log to Log4j (for file-based logs)
        LOGGER.info(message);
        
        // Conditionally log to Extent Reports
        if (isExtentEnabled()) {
            try {
                ExtentCucumberAdapter.addTestStepLog(message);
            } catch (Exception e) {
                // Silently fail if Extent logging fails (don't break test execution)
                LOGGER.debug("Failed to log to Extent Reports: {}", e.getMessage());
            }
        }
    }
    
    /**
     * Log a test step message with custom Log4j logger
     * Useful when you want to use a specific class's logger
     * 
     * @param logger The Log4j logger to use
     * @param message The message to log
     */
    public static void log(Logger logger, String message) {
        // Log to the provided Log4j logger
        logger.info(message);
        
        // Conditionally log to Extent Reports
        if (isExtentEnabled()) {
            try {
                ExtentCucumberAdapter.addTestStepLog(message);
            } catch (Exception e) {
                // Silently fail if Extent logging fails
                logger.debug("Failed to log to Extent Reports: {}", e.getMessage());
            }
        }
    }
    
    /**
     * Log an error message
     * - Always logs to Log4j as ERROR level
     * - Conditionally logs to Extent Reports
     * 
     * @param message The error message to log
     */
    public static void logError(String message) {
        // Always log to Log4j as ERROR
        LOGGER.error(message);
        
        // Conditionally log to Extent Reports
        if (isExtentEnabled()) {
            try {
                ExtentCucumberAdapter.addTestStepLog("❌ ERROR: " + message);
            } catch (Exception e) {
                LOGGER.debug("Failed to log error to Extent Reports: {}", e.getMessage());
            }
        }
    }
    
    /**
     * Log an error message with custom Log4j logger
     * 
     * @param logger The Log4j logger to use
     * @param message The error message to log
     */
    public static void logError(Logger logger, String message) {
        // Log to the provided Log4j logger as ERROR
        logger.error(message);
        
        // Conditionally log to Extent Reports
        if (isExtentEnabled()) {
            try {
                ExtentCucumberAdapter.addTestStepLog("❌ ERROR: " + message);
            } catch (Exception e) {
                logger.debug("Failed to log error to Extent Reports: {}", e.getMessage());
            }
        }
    }
    
    /**
     * Log a warning message
     * - Always logs to Log4j as WARN level
     * - Conditionally logs to Extent Reports
     * 
     * @param message The warning message to log
     */
    public static void logWarning(String message) {
        // Always log to Log4j as WARN
        LOGGER.warn(message);
        
        // Conditionally log to Extent Reports
        if (isExtentEnabled()) {
            try {
                ExtentCucumberAdapter.addTestStepLog("⚠️ WARNING: " + message);
            } catch (Exception e) {
                LOGGER.debug("Failed to log warning to Extent Reports: {}", e.getMessage());
            }
        }
    }
    
    /**
     * Log a warning message with custom Log4j logger
     * 
     * @param logger The Log4j logger to use
     * @param message The warning message to log
     */
    public static void logWarning(Logger logger, String message) {
        // Log to the provided Log4j logger as WARN
        logger.warn(message);
        
        // Conditionally log to Extent Reports
        if (isExtentEnabled()) {
            try {
                ExtentCucumberAdapter.addTestStepLog("⚠️ WARNING: " + message);
            } catch (Exception e) {
                logger.debug("Failed to log warning to Extent Reports: {}", e.getMessage());
            }
        }
    }
    
    /**
     * Add a screenshot to Extent Reports (if enabled)
     * This method is specifically for Extent Reports screenshot integration
     * 
     * @param screenshotPath Path to the screenshot file
     * @param message Description/caption for the screenshot
     */
    public static void addScreenshot(String screenshotPath, String message) {
        if (isExtentEnabled()) {
            try {
                ExtentCucumberAdapter.addTestStepScreenCaptureFromPath(screenshotPath, message);
                LOGGER.debug("Screenshot added to Extent Reports: {}", screenshotPath);
            } catch (Exception e) {
                LOGGER.debug("Failed to add screenshot to Extent Reports: {}", e.getMessage());
            }
        } else {
            LOGGER.info("Screenshot captured: {} | {}", screenshotPath, message);
        }
    }
    
    /**
     * Reset the cached configuration flag
     * Useful for testing or when configuration changes at runtime
     */
    public static void resetConfiguration() {
        extentEnabled = null;
        LOGGER.debug("Extent reporting configuration cache cleared");
    }
    
    /**
     * Get the current Extent reporting status
     * 
     * @return true if Extent reporting is enabled, false otherwise
     */
    public static boolean getExtentStatus() {
        return isExtentEnabled();
    }
}

