package hooks.JobMapping;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.SkipException;

import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

/**
 * Conditional Scenario Skip Manager
 * 
 * SPECIFIC USE CASE: ManualMappingofSPinAutoAI Feature
 * - Detects when "All Profiles are Mapped with BIC Profiles" condition occurs
 * - Automatically skips remaining scenarios in the feature file
 * - Completely safe - doesn't affect other test execution
 * 
 * TRIGGER CONDITION:
 * When log message contains: "Currently, All Profiles in Job Mapping are Mapped with BIC Profiles"
 * 
 * BEHAVIOR:
 *  First scenario runs normally and checks for unmapped profiles
 * ERROR: If no unmapped profiles found -> Skip all remaining scenarios in that feature
 *  Other features continue to run normally
 */
public class ConditionalScenarioSkip {
    
    private static final Logger LOGGER = LogManager.getLogger(ConditionalScenarioSkip.class);
    
    // Thread-safe storage for skip conditions per feature file
    private static final Set<String> skipEnabledFeatures = ConcurrentHashMap.newKeySet();
    

    
    // The feature file that this applies to
    private static final String TARGET_FEATURE = "18ManualMappingofSPinAutoAI.feature";
    
    @Before(order = 10) // Run after other hooks
    public void checkConditionalSkip(Scenario scenario) {
        String scenarioName = scenario.getName();
        String featureName = extractFeatureName(scenario.getUri().toString());
        
        // Only apply to the specific feature file
        if (!featureName.equals(TARGET_FEATURE)) {
            return; // Not the target feature - no action needed
        }
        
        // ENHANCED: Always allow @CloseBrowser scenarios to execute for proper cleanup
        if (isCloseBrowserScenario(scenario)) {
            LOGGER.info(" CLEANUP SCENARIO: '{}' will always execute for proper browser cleanup", scenarioName);
            return; // Skip the conditional check for browser cleanup scenarios
        }
        
        // Check if skip condition is enabled for this feature
        if (skipEnabledFeatures.contains(TARGET_FEATURE)) {
            String skipMessage = String.format(
                " CONDITIONAL SKIP: Scenario '%s' skipped because all profiles are already mapped with BIC profiles. " +
                "No unmapped jobs available for manual mapping in this test run.", 
                scenarioName);
            
            LOGGER.warn(skipMessage);
            ExtentCucumberAdapter.addTestStepLog(" " + skipMessage);
            
            // Skip this scenario
            throw new SkipException(skipMessage);
        }
        
        // First scenario in the feature - will run normally to check the condition
        LOGGER.info(" CONDITIONAL CHECK: Scenario '{}' will check for unmapped profiles", scenarioName);
    }
    
    @After(order = 10) // Run after other hooks
    public void detectSkipCondition(Scenario scenario) {
        String scenarioName = scenario.getName();
        String featureName = extractFeatureName(scenario.getUri().toString());
        
        // Only monitor the first scenario in the target feature
        if (!featureName.equals(TARGET_FEATURE)) {
            return;
        }
        
        // Check if this was the sorting/checking scenario
        if (scenarioName.toLowerCase().contains("descending") || 
            scenarioName.toLowerCase().contains("sorting") ||
            scenarioName.toLowerCase().contains("bic mapping")) {
            
            // This scenario should have detected the condition
            // The actual detection happens through monitoring logs/static flags
            // Let's check if the condition was detected
            boolean allMapped = checkIfAllProfilesMapped();
            
            if (allMapped) {
                // Enable skip for all remaining scenarios in this feature
                skipEnabledFeatures.add(TARGET_FEATURE);
                
                String message = String.format(
                    " SKIP CONDITION DETECTED: All profiles are mapped with BIC profiles. " +
                    "All remaining scenarios in '%s' will be skipped.", 
                    TARGET_FEATURE);
                
                LOGGER.warn(message);
                ExtentCucumberAdapter.addTestStepLog(" " + message);
                
                // Log which scenarios will be skipped
                LOGGER.info(" SCENARIOS TO BE SKIPPED:");
                LOGGER.info("    Click on Find Match button and Search for SP in Manual Mapping screen");
                LOGGER.info("    Validate and Manually Map SP to Organization Job in Auto AI");
                LOGGER.info("    Verify details of SP which is Mapped to Organization Job");
                LOGGER.info("    Validate content in manually mapped Job profile details popup");
                LOGGER.info(" SCENARIOS THAT WILL STILL EXECUTE:");
                LOGGER.info("    Close the Browser after Validation (@CloseBrowser scenarios always execute for cleanup)");
                ExtentCucumberAdapter.addTestStepLog(" Remaining functional scenarios will be skipped, but cleanup scenarios will still execute");
                
            } else {
                LOGGER.info(" PROFILES AVAILABLE: Found unmapped profiles - manual mapping scenarios will proceed");
                ExtentCucumberAdapter.addTestStepLog(" Found unmapped profiles - proceeding with manual mapping tests");
            }
        }
    }
    
    /**
     * Check if all profiles are mapped by looking for the static flag set by ManualMappingofSPinAutoAI
     * This integrates with your existing code without any modifications
     */
    private boolean checkIfAllProfilesMapped() {
        try {
            // Access the static flag set by MapDifferentSPtoProfileInAutoAI class
            // This flag is set to false when all profiles are mapped
            Class<?> mapClass = Class.forName("com.kfonetalentsuite.pageobjects.MapDifferentSPtoProfileInAutoAI");
            java.lang.reflect.Field mapSPField = mapClass.getDeclaredField("mapSP");
            mapSPField.setAccessible(true);
            Boolean mapSPValue = (Boolean) mapSPField.get(null);
            
            // If mapSP is false, it means all profiles are mapped
            boolean allMapped = (mapSPValue != null && !mapSPValue);
            
            if (allMapped) {
                LOGGER.info(" CONDITION CHECK: MapDifferentSPtoProfileInAutoAI.mapSP = false (All profiles mapped)");
            } else {
                LOGGER.info(" CONDITION CHECK: MapDifferentSPtoProfileInAutoAI.mapSP = true (Unmapped profiles available)");
            }
            
            return allMapped;
            
        } catch (Exception e) {
            LOGGER.warn("WARNING: Could not check mapSP flag, assuming profiles are available: " + e.getMessage());
            return false; // Default to not skipping if we can't determine the condition
        }
    }
    
    /**
     * Check if scenario is a browser cleanup scenario that should always execute
     */
    private boolean isCloseBrowserScenario(Scenario scenario) {
        // Check if scenario has @CloseBrowser tag
        return scenario.getSourceTagNames().contains("@CloseBrowser") ||
               scenario.getName().toLowerCase().contains("close") && 
               scenario.getName().toLowerCase().contains("browser");
    }
    
    /**
     * Extract feature file name from URI
     */
    private String extractFeatureName(String uri) {
        if (uri == null || uri.isEmpty()) {
            return "";
        }
        
        // Extract just the feature file name
        String[] parts = uri.split("[/\\\\]");
        String fileName = parts[parts.length - 1];
        
        return fileName;
    }
    
    /**
     * Utility method to manually enable skip condition (for testing)
     */
    public static void enableSkipForFeature(String featureName) {
        skipEnabledFeatures.add(featureName);
        LogManager.getLogger(ConditionalScenarioSkip.class)
                .info(" SKIP ENABLED: All scenarios in '{}' will be skipped", featureName);
    }
    
    /**
     * Utility method to clear skip conditions (for cleanup)
     */
    public static void clearSkipConditions() {
        skipEnabledFeatures.clear();
        LogManager.getLogger(ConditionalScenarioSkip.class)
                .info(" SKIP CONDITIONS CLEARED: All features reset to normal execution");
    }
    
    /**
     * Check if a feature is currently set to skip scenarios
     */
    public static boolean isSkipEnabled(String featureName) {
        return skipEnabledFeatures.contains(featureName);
    }
}
