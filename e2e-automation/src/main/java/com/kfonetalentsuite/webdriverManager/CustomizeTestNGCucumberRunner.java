package com.kfonetalentsuite.webdriverManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.kfonetalentsuite.utils.common.DynamicTagResolver;
import com.kfonetalentsuite.utils.common.VariableManager;

import io.cucumber.testng.FeatureWrapper;
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
	 * Run individual Cucumber scenario via DataProvider.
	 * 
	 * RETRY BEHAVIOR: End-of-Suite Retry (via SuiteRetryListener)
	 * - Each scenario runs as a separate test (for proper Excel reporting)
	 * - If ANY scenario fails, the entire Runner class is marked as failed
	 * - SuiteRetryListener collects failed Runner classes at end of suite
	 * - Failed Runners are re-executed (all their scenarios run again)
	 * 
	 * This approach gives us:
	 * - Individual scenario tracking for Excel reporting (via DataProvider params)
	 * - Feature-level retry at end of suite (entire Runner re-runs)
	 */
	@SuppressWarnings("unused")
	@Test(groups = "cucumber", description = "Runs Cucumber Scenarios", dataProvider = "scenarios")
	public void runScenario(PickleWrapper pickleWrapper, FeatureWrapper featureWrapper) {
		testNGCucumberRunner.runScenario(pickleWrapper.getPickle());
	}

	/**
	 * THREAD-SAFE DataProvider for Cucumber scenarios
	 * 
	 * IMPORTANT: Do NOT use parallel=true here! - parallel=true would parallelize
	 * scenarios WITHIN a runner, causing chaos - We want each runner to execute its
	 * scenarios sequentially on its own thread - TestNG parallel="tests" already
	 * ensures each runner runs on a separate thread
	 * 
	 * Thread Safety: - Each runner class instance has its own testNGCucumberRunner
	 * - TestNG creates separate instances for each <test> in parallel execution -
	 * This ensures scenarios from Runner01 stay with Thread-1, Runner02 with
	 * Thread-2, etc.
	 */
	@DataProvider
	public Object[][] scenarios() {
		if (testNGCucumberRunner == null) {
			return new Object[0][0];
		}

		Object[][] allScenarios = testNGCucumberRunner.provideScenarios();

		// THREAD-SAFE: Filter scenarios based on login type from config
		// This ensures each runner only executes scenarios for the configured login
		// type
		String configuredLoginTag = resolveLoginTag();
		String runnerName = this.getClass().getSimpleName();

		// Filter out scenarios with the wrong login tag
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

		Object[][] result = filteredScenarios.toArray(new Object[0][0]);
		LOGGER.info("  ▶ {} executing {} scenarios", runnerName, result.length);

		return result;
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
