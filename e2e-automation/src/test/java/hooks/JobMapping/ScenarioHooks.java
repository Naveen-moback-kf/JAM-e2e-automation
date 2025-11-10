package hooks.JobMapping;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kfonetalentsuite.listeners.ExcelReportListener;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class ScenarioHooks {
    
	protected static final Logger LOGGER = (Logger) LogManager.getLogger(ScenarioHooks.class);
    
    // ThreadLocal to store current scenario for access in step definitions
    private static final ThreadLocal<Scenario> currentScenario = new ThreadLocal<>();
    
    /**
     * Get the current scenario for the running thread
     */
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
        
        // Clear scenario information from ExcelReportListener
        String threadId = Thread.currentThread().getName();
        ExcelReportListener.clearCurrentScenario(threadId);
        
        // Clear scenario from ThreadLocal to avoid memory leaks
        currentScenario.remove();
    }
}
