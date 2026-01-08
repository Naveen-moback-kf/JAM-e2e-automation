package hooks.JobMapping;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.SkipException;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class ConditionalScenarioSkip {

	private static final Logger LOGGER = LogManager.getLogger(ConditionalScenarioSkip.class);

	// Thread-safe storage for skip conditions per feature file
	private static final Set<String> skipEnabledFeatures = ConcurrentHashMap.newKeySet();

	// The feature files that this applies to (Feature 20 and 21)
	private static final String TARGET_FEATURE_20 = "20ManualMappingofSPinAutoAI.feature";
	private static final String TARGET_FEATURE_21 = "21MapDifferentSPtoProfileInAutoAI.feature";

	@Before(order = 10) // Run after other hooks
	public void checkConditionalSkip(Scenario scenario) {
		String scenarioName = scenario.getName();
		String featureName = extractFeatureName(scenario.getUri().toString());

		// Only apply to Feature 20 and 21
		if (!isTargetFeature(featureName)) {
			return; // Not a target feature - no action needed
		}

		// ENHANCED: Always allow @CloseBrowser scenarios to execute for proper cleanup
		if (isCloseBrowserScenario(scenario)) {
			LOGGER.info(" CLEANUP SCENARIO: '{}' will always execute for proper browser cleanup", scenarioName);
			return; // Skip the conditional check for browser cleanup scenarios
		}

		// Check if skip condition is enabled for this feature
		if (skipEnabledFeatures.contains(featureName) || skipEnabledFeatures.contains("ALL_MAPPED")) {
		String skipMessage = String.format(
				"CONDITIONAL SKIP: Scenario '%s' skipped because all profiles are already mapped with BIC profiles. "
						+ "No unmapped jobs available for manual mapping in this test run.",
				scenarioName);

		LOGGER.warn(skipMessage);

			// Skip this scenario
			throw new SkipException(skipMessage);
		}

		// First scenario in the feature - will run normally to check the condition
		LOGGER.info(" CONDITIONAL CHECK: Scenario '{}' will check for manually mapped profiles", scenarioName);
	}

	@After(order = 10) // Run after other hooks
	public void detectSkipCondition(Scenario scenario) {
		String scenarioName = scenario.getName();
		String featureName = extractFeatureName(scenario.getUri().toString());

		// Only monitor scenarios in target features (Feature 20 and 21)
		if (!isTargetFeature(featureName)) {
			return;
		}

		// Check if this was the sorting/checking scenario (first scenario in Feature 20)
		if (scenarioName.toLowerCase().contains("descending") || scenarioName.toLowerCase().contains("sorting")
				|| scenarioName.toLowerCase().contains("bic mapping") || scenarioName.toLowerCase().contains("no bic")) {

			// Check if the condition was detected via the mapSP flag
			boolean allMapped = checkIfAllProfilesMapped();

			if (allMapped) {
				// Enable skip for all remaining scenarios in Feature 20 and 21
				skipEnabledFeatures.add(TARGET_FEATURE_20);
				skipEnabledFeatures.add(TARGET_FEATURE_21);
				skipEnabledFeatures.add("ALL_MAPPED"); // Global flag

			String message = String.format("SKIP CONDITION DETECTED: All profiles are mapped with BIC profiles. "
					+ "All remaining scenarios in Feature 20 and 21 will be skipped.");

			LOGGER.warn(message);

				// Log which scenarios will be skipped
				LOGGER.info(" SCENARIOS TO BE SKIPPED in Feature 20:");
				LOGGER.info("    - Click on Find Match button and Search for SP in Manual Mapping screen");
				LOGGER.info("    - Validate and Manually Map SP to Organization Job in Auto AI");
				LOGGER.info(" SCENARIOS TO BE SKIPPED in Feature 21:");
			LOGGER.info("    - All scenarios related to mapping different SP to profile");
			LOGGER.info(" SCENARIOS THAT WILL STILL EXECUTE:");
			LOGGER.info("    - @CloseBrowser scenarios (always execute for cleanup)");
			LOGGER.info("Remaining functional scenarios will be skipped, but cleanup scenarios will still execute");

		} else {
			LOGGER.info(" PROFILES AVAILABLE: Found unmapped profiles - manual mapping scenarios will proceed");
		}
	}
	}

	private boolean checkIfAllProfilesMapped() {
		try {
			// Access the ThreadLocal flag set by PO17_MapDifferentSPtoProfile class
			// This flag is set to false when all profiles are mapped
			Class<?> mapClass = Class.forName("com.kfonetalentsuite.pageobjects.JobMapping.PO17_MapDifferentSPtoProfile");
			java.lang.reflect.Field mapSPField = mapClass.getDeclaredField("mapSP");
			mapSPField.setAccessible(true);
			
			@SuppressWarnings("unchecked")
			ThreadLocal<Boolean> mapSPThreadLocal = (ThreadLocal<Boolean>) mapSPField.get(null);
			Boolean mapSPValue = mapSPThreadLocal.get();

			// If mapSP is false (or null), it means all profiles are mapped
			boolean allMapped = (mapSPValue == null || !mapSPValue);

			if (allMapped) {
				LOGGER.info("CONDITION CHECK: PO17_MapDifferentSPtoProfile.mapSP = {} (All profiles mapped)", mapSPValue);
			} else {
				LOGGER.info("CONDITION CHECK: PO17_MapDifferentSPtoProfile.mapSP = true (Unmapped profiles available)");
			}

			return allMapped;

		} catch (Exception e) {
			LOGGER.warn("WARNING: Could not check mapSP flag, assuming profiles are available: " + e.getMessage());
			return false; // Default to not skipping if we can't determine the condition
		}
	}

	private boolean isTargetFeature(String featureName) {
		return TARGET_FEATURE_20.equals(featureName) || TARGET_FEATURE_21.equals(featureName)
				|| featureName.contains("ManualMapping") || featureName.contains("MapDifferentSP");
	}

	private boolean isCloseBrowserScenario(Scenario scenario) {
		// Check if scenario has @CloseBrowser tag
		return scenario.getSourceTagNames().contains("@CloseBrowser")
				|| (scenario.getName().toLowerCase().contains("close")
						&& scenario.getName().toLowerCase().contains("browser"));
	}

	private String extractFeatureName(String uri) {
		if (uri == null || uri.isEmpty()) {
			return "";
		}

		// Extract just the feature file name
		String[] parts = uri.split("[/\\\\]");
		String fileName = parts[parts.length - 1];

		return fileName;
	}

	public static void enableSkipForFeature(String featureName) {
		skipEnabledFeatures.add(featureName);
		LogManager.getLogger(ConditionalScenarioSkip.class).info(" SKIP ENABLED: All scenarios in '{}' will be skipped",
				featureName);
	}

	public static void clearSkipConditions() {
		skipEnabledFeatures.clear();
		LogManager.getLogger(ConditionalScenarioSkip.class)
				.info(" SKIP CONDITIONS CLEARED: All features reset to normal execution");
	}

	public static boolean isSkipEnabled(String featureName) {
		return skipEnabledFeatures.contains(featureName);
	}
}
