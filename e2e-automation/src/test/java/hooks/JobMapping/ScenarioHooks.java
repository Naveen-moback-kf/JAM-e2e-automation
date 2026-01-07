package hooks.JobMapping;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kfonetalentsuite.listeners.ExcelReportListener;
import com.kfonetalentsuite.manager.PageObjectManager;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class ScenarioHooks {

	protected static final Logger LOGGER = (Logger) LogManager.getLogger(ScenarioHooks.class);

	// ThreadLocal to store current scenario for access in step definitions
	private static final ThreadLocal<Scenario> currentScenario = new ThreadLocal<>();

	public static Scenario getCurrentScenario() {
		return currentScenario.get();
	}

	@Before
	public void beforeScenario(Scenario scenario) {
		// Store scenario in ThreadLocal for access in step definitions
		currentScenario.set(scenario);
		String scenarioName = scenario.getName();
		String featureName = scenario.getUri().toString();

		// Extract just the feature file name from the URI
		String featureFileName = featureName.substring(featureName.lastIndexOf("/") + 1);

		// Share scenario information with ExcelReportListener for better logging
		String threadId = Thread.currentThread().getName();
		ExcelReportListener.setCurrentScenario(threadId, scenarioName);

		LOGGER.info("==================================================");
		LOGGER.info("SCENARIO STARTED: {} | Feature: {}", scenarioName, featureFileName);
		LOGGER.info("Scenario Tags: {}", scenario.getSourceTagNames());
		LOGGER.info("==================================================");

		// NOTE: Cache clearing moved to TestRunnerHooks (@BeforeClass/@AfterClass)
		// Cache is now cleared once per test runner, not per scenario
		// This allows scenarios within a runner to share state (e.g., login once)
	}

	@After
	public void afterScenario(Scenario scenario) {
		String scenarioName = scenario.getName();
		String status = scenario.getStatus().toString();

		LOGGER.info("==================================================");
		LOGGER.info("SCENARIO FINISHED: {} | Status: {}", scenarioName, status);

		if (scenario.isFailed()) {
			LOGGER.error("SCENARIO FAILED: {} - Check test execution details", scenarioName);
		} else {
			LOGGER.info("SCENARIO PASSED: {} - Executed successfully", scenarioName);
		}

		LOGGER.info("==================================================");

		// NOTE: Cache clearing moved to TestRunnerHooks (@BeforeClass/@AfterClass)
		// Cache is now cleared once per test runner, not per scenario
		// This allows scenarios within a runner to share state (e.g., login once)

		// DON'T clear scenario information yet - TestNG listener needs it for exception
		// capture
		// The ExcelReportListener will clear it after capturing exception details
		// This ensures proper exception-to-scenario mapping in Excel reports

		// Reset PageObjectManager to clean up Page Object instances and prevent memory leaks
		PageObjectManager.reset();
		LOGGER.debug("PageObjectManager reset completed for thread: {}", Thread.currentThread().getName());

		// Reset screenshot counters to prevent memory leaks
		com.kfonetalentsuite.utils.JobMapping.ScreenshotHandler.resetCounters();

		// Clear scenario from ThreadLocal to avoid memory leaks
		currentScenario.remove();
	}
}
