package com.kfonetalentsuite.utils.common;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Excel Config Provider - Reads login config from TestData.xlsx
 * 
 * Usage:
 * - Set Execute=YES in LoginData sheet for the row you want to use
 * - Environment, Username, Password are fetched from that row
 * 
 * @author Automation Team
 */
public class ExcelConfigProvider {

    private static final Logger LOGGER = LogManager.getLogger(ExcelConfigProvider.class);

    // Cache to avoid multiple Excel reads
    private static Map<String, String> cachedLogin = null;
    private static boolean cacheLoaded = false;

    /**
     * Get the Execute=YES login row from Excel (cached)
     */
    public static Map<String, String> getDefaultLogin() {
        if (cacheLoaded) {
            return cachedLogin;
        }

        try {
            // Clear any stale cache
            cachedLogin = null;
            // Check for TestID override from System Property (CI/CD)
            String testIdOverride = System.getProperty("login.testid");
            if (testIdOverride != null && !testIdOverride.isEmpty()) {
                cachedLogin = ExcelDataProvider.getTestData("LoginData", testIdOverride);
                cacheLoaded = true;
                LOGGER.info("Using {} credentials from Excel (TestID: {}) [CI/CD Override]", 
                        cachedLogin.get("UserType"), cachedLogin.get("TestID"));
                return cachedLogin;
            }

            // Find Execute=YES row
            List<Map<String, String>> allLogins = ExcelDataProvider.getSheetData("LoginData");
            
            for (Map<String, String> login : allLogins) {
                String executeValue = login.get("Execute");
                
                if (executeValue != null && "YES".equalsIgnoreCase(executeValue.trim())) {
                    cachedLogin = login;
                    cacheLoaded = true;
                    LOGGER.info("Using {} credentials for {} environment from Excel (TestID: {})", 
                            login.get("UserType"), 
                            getEnvironmentValue(login),
                            login.get("TestID"));
                    return cachedLogin;
                }
            }

            LOGGER.warn("No Execute=YES row found in LoginData sheet. Check Excel column 'Execute' has value 'YES'");

        } catch (Exception e) {
            LOGGER.error("Failed to read Excel: {}", e.getMessage());
        }

        cacheLoaded = true;
        return null;
    }

    /**
     * Get Environment from the Execute=YES row
     */
    public static String getActiveEnvironmentName() {
        // CI/CD override
        String envOverride = System.getProperty("Environment");
        if (envOverride != null && !envOverride.isEmpty()) {
            return envOverride;
        }

        // Get from cached login
        Map<String, String> login = getDefaultLogin();
        if (login != null) {
            String env = getEnvironmentValue(login);
            if (env != null && !env.isEmpty()) {
                return env;
            }
        }

        return null; // Will use config.properties fallback
    }

    /**
     * Get login by UserType (uses cached data)
     */
    public static Map<String, String> getLoginByType(String userType) {
        Map<String, String> login = getDefaultLogin();
        if (login != null && userType.equalsIgnoreCase(login.get("UserType"))) {
            return login;
        }
        LOGGER.warn("No Execute=YES login for UserType={}", userType);
        return null;
    }

    /**
     * Get PAMS_ID from cached login
     */
    public static String getActivePamsId() {
        // CI/CD override - Check system property first
        String pamsIdOverride = System.getProperty("target.pams.id");
        if (pamsIdOverride != null && !pamsIdOverride.isEmpty()) {
            LOGGER.info("Using PAMS_ID from CI/CD override: {}", pamsIdOverride);
            return pamsIdOverride.trim();
        }
        
        // Get from Excel
        Map<String, String> login = getDefaultLogin();
        return (login != null) ? login.get("PAMS_ID") : null;
    }
    
    /**
     * Get Login Type (UserType) with CI/CD override support
     */
    public static String getActiveLoginType() {
        // CI/CD override - Check system property first
        String loginTypeOverride = System.getProperty("login.type");
        if (loginTypeOverride != null && !loginTypeOverride.isEmpty()) {
            LOGGER.info("Using Login Type from CI/CD override: {}", loginTypeOverride);
            return loginTypeOverride.trim().toUpperCase();
        }
        
        // Get from Excel
        Map<String, String> login = getDefaultLogin();
        if (login != null) {
            String userType = login.get("UserType");
            if (userType != null && !userType.isEmpty()) {
                return userType.trim().toUpperCase();
            }
        }
        
        return null;
    }

    /**
     * Helper: Get Environment value (case-insensitive key lookup)
     */
    private static String getEnvironmentValue(Map<String, String> login) {
        // Try exact match first
        String env = login.get("Environment");
        if (env != null && !env.isEmpty()) {
            return env.trim();
        }
        
        // Try case-insensitive lookup
        for (String key : login.keySet()) {
            if ("environment".equalsIgnoreCase(key.trim())) {
                env = login.get(key);
                if (env != null && !env.isEmpty()) {
                    return env.trim();
                }
            }
        }
        
        return null;
    }

    /**
     * Clear cache (for testing)
     */
    public static void clearCache() {
        cachedLogin = null;
        cacheLoaded = false;
    }
}
