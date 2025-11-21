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

@Listeners({com.kfonetalentsuite.listeners.ExcelReportListener.class})
public abstract class CustomizeTestNGCucumberRunner extends DriverManager{

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
            String tagExpression = buildTagExpression(loginTag);
            
            // Set Cucumber tag filter (replaces @DYNAMIC_LOGIN with actual login tag)
            System.setProperty("cucumber.filter.tags", tagExpression);
            
            LOGGER.debug("Dynamic tags configured for {} with login: {}", runnerName, loginTag);
            
            ensureDriverInitialized();
            
        } catch (Exception e) {
            LOGGER.error("Failed to setup dynamic tags: " + e.getMessage());
            throw new RuntimeException("Dynamic tag setup failed", e);
        }
    }
    
    protected String resolveLoginTag() {
        return DynamicTagResolver.getLoginTag();
    }
    
    private String buildTagExpression(String loginTag) {
        String template = getTagExpressionTemplate();
        return template.replace("@DYNAMIC_LOGIN", loginTag);
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
        String runnerName = this.getClass().getSimpleName();
        
        LOGGER.info("▶️  Starting: {}", runnerName);
        
        // CACHE CLEANUP BEFORE RUNNER: Clear all browser data before runner starts (defensive)
        try {
            DriverManager.clearAllBrowserData();
        } catch (Exception e) {
            LOGGER.warn("Failed to clear browser data: {}", e.getMessage());
        }
        
        testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
    }

    @SuppressWarnings("unused")
    @Test(groups = "cucumber", description = "Runs Cucumber Scenarios", dataProvider = "scenarios")
    public void runScenario(PickleWrapper pickleWrapper, FeatureWrapper featureWrapper) {
        testNGCucumberRunner.runScenario(pickleWrapper.getPickle());
    }

    
    /**
     * THREAD-SAFE DataProvider for Cucumber scenarios
     * 
     * IMPORTANT: Do NOT use parallel=true here!
     * - parallel=true would parallelize scenarios WITHIN a runner, causing chaos
     * - We want each runner to execute its scenarios sequentially on its own thread
     * - TestNG parallel="tests" already ensures each runner runs on a separate thread
     * 
     * Thread Safety:
     * - Each runner class instance has its own testNGCucumberRunner
     * - TestNG creates separate instances for each <test> in parallel execution
     * - This ensures scenarios from Runner01 stay with Thread-1, Runner02 with Thread-2, etc.
     */
    @DataProvider
    public Object[][] scenarios() {
        if (testNGCucumberRunner == null) {
            return new Object[0][0];
        }
        
        Object[][] scenarios = testNGCucumberRunner.provideScenarios();
        LOGGER.debug("{} providing {} scenarios", this.getClass().getSimpleName(), scenarios.length);
        
        return scenarios;
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
