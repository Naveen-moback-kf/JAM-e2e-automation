package com.kfonetalentsuite.webdriverManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.kfonetalentsuite.utils.common.DynamicTagResolver;
import com.kfonetalentsuite.utils.common.VariableManager;

import io.cucumber.testng.PickleWrapper;
import io.cucumber.testng.TestNGCucumberRunner;

@Listeners({ com.kfonetalentsuite.listeners.ExcelReportListener.class })
public abstract class CustomizeTestNGCucumberRunner extends DriverManager {

	// THREAD-SAFE: Each runner instance gets its own TestNGCucumberRunner
	// This prevents scenario mixing between parallel test executions
	private TestNGCucumberRunner testNGCucumberRunner;

	protected static final Logger LOGGER = (Logger) LogManager.getLogger(CustomizeTestNGCucumberRunner.class);

	protected abstract String getTagExpressionTemplate();

	@BeforeTest
	public final void setupDynamicTags() {
		try {
			String runnerName = this.getClass().getSimpleName();

			VariableManager.getInstance().loadProperties();
			String loginTag = resolveLoginTag();

			LOGGER.info("▶️  Starting: {} with login: {}", runnerName, loginTag);

			ensureDriverInitialized();

		} catch (Exception e) {
			LOGGER.error("Failed to setup dynamic tags: " + e.getMessage());
			throw new RuntimeException("Dynamic tag setup failed", e);
		}
	}

	protected String resolveLoginTag() {
		return DynamicTagResolver.getLoginTag();
	}

	private void ensureDriverInitialized() {
		if (DriverManager.getDriver() == null) {
			String runnerName = this.getClass().getSimpleName();
			LOGGER.warn(" Driver is null in " + runnerName + " - initializing");
			DriverManager.launchBrowser();
			LOGGER.info("... Driver initialization verified in " + runnerName);
		}
	}

	@BeforeClass(alwaysRun = true)
	public void setUpClass() {
		// CACHE CLEANUP BEFORE RUNNER: Clear all browser data before runner starts
		// (defensive)
		try {
			DriverManager.clearAllBrowserData();
		} catch (Exception e) {
			LOGGER.warn("Failed to clear browser data: {}", e.getMessage());
		}

		testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
	}

	/**
	 * FEATURE-LEVEL RETRY: Run ALL scenarios in a single @Test method
	 * 
	 * This enables FEATURE-LEVEL retry behavior:
	 * - When ANY scenario fails, the entire feature will be retried
	 * - RetryAnalyzer will retry this entire method (all scenarios)
	 * - Up to MAX_RETRY_COUNT attempts for the whole feature
	 * 
	 * Previous behavior (DataProvider): Each scenario was a separate test,
	 * so retry only retried the failed scenario.
	 * 
	 * Current behavior: All scenarios run together, so retry re-runs ALL scenarios.
	 */
	@Test(groups = "cucumber", description = "Runs All Cucumber Scenarios in Feature")
	public void runFeature() {
		if (testNGCucumberRunner == null) {
			LOGGER.error("TestNGCucumberRunner is null - cannot execute feature");
			return;
		}

		String runnerName = this.getClass().getSimpleName();
		Object[][] allScenarios = testNGCucumberRunner.provideScenarios();

		// Filter scenarios based on login type
		java.util.List<Object[]> filteredScenarios = filterScenariosByLoginType(allScenarios);

		LOGGER.info("╔═══════════════════════════════════════════════════════════════╗");
		LOGGER.info("║ FEATURE EXECUTION: {} ", runnerName);
		LOGGER.info("║ Total Scenarios: {} ", filteredScenarios.size());
		LOGGER.info("╚═══════════════════════════════════════════════════════════════╝");

		int scenarioIndex = 0;
		int totalScenarios = filteredScenarios.size();

		// Execute ALL scenarios sequentially within this single @Test method
		for (Object[] scenario : filteredScenarios) {
			scenarioIndex++;
			PickleWrapper pickleWrapper = (PickleWrapper) scenario[0];
			String scenarioName = pickleWrapper.getPickle().getName();

			LOGGER.info("  ▶ [{}/{}] Executing: {}", scenarioIndex, totalScenarios, scenarioName);

			// Run the scenario - if it fails, the exception propagates up
			// and RetryAnalyzer will retry the ENTIRE runFeature() method
			testNGCucumberRunner.runScenario(pickleWrapper.getPickle());
		}

		LOGGER.info("✅ {} - All {} scenarios completed successfully", runnerName, totalScenarios);
	}

	/**
	 * Filter scenarios based on login type from config.
	 * This ensures each runner only executes scenarios for the configured login type.
	 */
	private java.util.List<Object[]> filterScenariosByLoginType(Object[][] allScenarios) {
		String configuredLoginTag = resolveLoginTag();

		java.util.List<Object[]> filteredScenarios = new java.util.ArrayList<>();
		for (Object[] scenario : allScenarios) {
			PickleWrapper pickleWrapper = (PickleWrapper) scenario[0];
			java.util.List<String> tags = pickleWrapper.getPickle().getTags();

			// Check if this scenario should be included
			boolean includeScenario = true;

			// If scenario has a login tag, it must match the configured login type
			boolean hasSSO = tags.contains("@SSO_Login_via_KFONE");
			boolean hasNonSSO = tags.contains("@NON_SSO_Login_via_KFONE");

			if (hasSSO || hasNonSSO) {
				// This is a login scenario - only include if it matches config
				includeScenario = tags.contains(configuredLoginTag);
			}
			// If no login tag, include the scenario (it's not a login scenario)

			if (includeScenario) {
				filteredScenarios.add(scenario);
			}
		}

		return filteredScenarios;
	}

	@AfterClass(alwaysRun = true)
	public void tearDownClass() {
		String runnerName = this.getClass().getSimpleName();

		LOGGER.info("⏹️  Completed: {}", runnerName);

		// CACHE CLEANUP AFTER RUNNER: Clean up browser data for good hygiene
		try {
			DriverManager.clearAllBrowserData();
		} catch (Exception e) {
			LOGGER.warn("Failed to clear browser data: {}", e.getMessage());
		}

		if (testNGCucumberRunner == null) {
			return;
		}
		testNGCucumberRunner.finish();
	}
}
