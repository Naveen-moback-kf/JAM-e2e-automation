package com.kfonetalentsuite.utils;

/**
 * Status Converter - Centralized status conversion utilities
 * 
 * Extracted from DailyExcelTracker to reduce complexity and ensure consistent
 * status handling across TestNG, Cucumber, and business reporting formats.
 */
public class StatusConverter {
    
    /**
     * Convert TestNG status to business-friendly status
     */
    public static String convertTestNGStatusToBusiness(String testngStatus) {
        if (testngStatus == null) {
            return "UNKNOWN";
        }
        
        switch (testngStatus.toUpperCase()) {
            case "PASS":
                return "PASSED";
            case "FAIL":
                return "FAILED";
            case "SKIP":
                return "SKIPPED";
            default:
                return "UNKNOWN";
        }
    }
    
    /**
     * Convert Cucumber status to business status
     */
    public static String convertCucumberStatusToBusiness(String cucumberStatus) {
        if (cucumberStatus == null) {
            return "UNKNOWN";
        }
        
        switch (cucumberStatus.toUpperCase()) {
            case "PASSED":
                return "PASSED";
            case "FAILED":
                return "FAILED";
            case "SKIPPED":
            case "PENDING":
                return "SKIPPED";
            default:
                return "UNKNOWN";
        }
    }
    
    /**
     * Generate business-friendly comment for failed scenarios
     */
    public static String generateBusinessFriendlyComment(String scenarioName, String status) {
        if ("PASSED".equals(status)) {
            return ""; // No comment needed for passed tests
        }
        
        if (scenarioName == null) {
            return "Test execution issue - please investigate";
        }
        
        String lowerScenario = scenarioName.toLowerCase();
        
        // Pattern matching for common failure scenarios
        if (lowerScenario.contains("login") || lowerScenario.contains("authentication")) {
            return "Login process failed - check credentials and system access";
        } else if (lowerScenario.contains("upload") || lowerScenario.contains("file")) {
            return "File upload functionality not working - verify file path and permissions";
        } else if (lowerScenario.contains("navigation") || lowerScenario.contains("page")) {
            return "Page navigation issue - check if page loads correctly";
        } else if (lowerScenario.contains("data") || lowerScenario.contains("form")) {
            return "Data entry or form submission failed - verify input fields";
        } else if (lowerScenario.contains("filter") || lowerScenario.contains("search")) {
            return "Search or filter functionality not responding - check data availability";
        } else if (lowerScenario.contains("profile") || lowerScenario.contains("user")) {
            return "User profile related functionality failed - verify user permissions";
        } else if (lowerScenario.contains("button") || lowerScenario.contains("click")) {
            return "Button click or UI interaction failed - check element availability";
        } else if (lowerScenario.contains("validate") || lowerScenario.contains("verify")) {
            return "Validation or verification step failed - check expected vs actual results";
        } else {
            return "Test step failed - please review execution details for root cause";
        }
    }
    
    /**
     * Enhanced business-friendly comment with step analysis
     */
    public static String generateEnhancedBusinessFriendlyComment(String scenarioName, String status) {
        if ("PASSED".equals(status)) {
            return "";
        }
        
        if (scenarioName == null) {
            return "Test execution issue - please investigate";
        }
        
        // Try step-specific analysis first
        String stepSpecificComment = extractFailureStepDetails(scenarioName);
        if (!stepSpecificComment.isEmpty()) {
            return stepSpecificComment;
        }
        
        // Fall back to pattern-based analysis
        return generateBusinessFriendlyComment(scenarioName, status);
    }
    
    /**
     * Extract failure step details for more specific comments
     */
    private static String extractFailureStepDetails(String scenarioName) {
        if (scenarioName == null) {
            return "";
        }
        
        String lowerName = scenarioName.toLowerCase();
        
        // Step-specific keyword matching
        if (lowerName.contains("click")) {
            if (lowerName.contains("button")) {
                return "Button click failed - element may not be clickable or visible";
            } else if (lowerName.contains("link")) {
                return "Link click failed - check if link is active and accessible";
            } else {
                return "Click operation failed - verify element is available and clickable";
            }
        } else if (lowerName.contains("upload")) {
            return "File upload step failed - check file path, permissions, and upload functionality";
        } else if (lowerName.contains("navigate")) {
            return "Page navigation step failed - verify URL accessibility and page loading";
        } else if (lowerName.contains("validate") || lowerName.contains("verify")) {
            return "Validation step failed - expected vs actual values don't match";
        } else if (lowerName.contains("enter") || lowerName.contains("input")) {
            return "Data entry step failed - check field availability and input validation";
        } else if (lowerName.contains("select") || lowerName.contains("choose")) {
            return "Selection step failed - verify dropdown/option is available";
        }
        
        return ""; // No specific step identified
    }
    
    /**
     * Determine overall daily status based on cumulative results
     */
    public static String determineDailyStatus(int passedTests, int failedTests, int skippedTests) {
        if (failedTests == 0 && passedTests > 0) {
            return "ALL TESTS PASSED";
        } else if (failedTests > 0 && passedTests > 0) {
            return "MIXED RESULTS";
        } else if (failedTests > 0) {
            return "TESTS FAILED";
        } else if (skippedTests > 0) {
            return "NO TESTS EXECUTED";
        } else {
            return "NO DATA AVAILABLE";
        }
    }
    
    /**
     * Check if scenario was actually executed (not just a placeholder)
     */
    public static boolean wasScenarioActuallyExecuted(String scenarioName, String status) {
        if (status == null) {
            return false;
        }
        
        String lowerStatus = status.toLowerCase();
        
        // Skip clearly non-executed scenarios
        if (lowerStatus.contains("not_executed") || lowerStatus.contains("placeholder")) {
            return false;
        }
        
        // Consider it executed if it has a real execution status
        return lowerStatus.contains("passed") || lowerStatus.contains("failed") || 
               lowerStatus.contains("skipped");
    }
}
