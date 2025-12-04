package com.kfonetalentsuite.webdriverManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.kfonetalentsuite.utils.common.DynamicTagResolver;

import io.cucumber.testng.PickleWrapper;
import io.cucumber.testng.TestNGCucumberRunner;

/**
 * Base class for Cross-Browser Test Runners.
 * Simplifies cross-browser testing by handling browser initialization,
 * scenario execution, and cleanup automatically.
 * 
 * Usage: Extend this class and override getTagExpressionTemplate() and resolveLoginTag()
 * 
 * @author AI Assistant
 */
@Listeners({ com.kfonetalentsuite.listeners.ExcelReportListener.class })
public abstract class CrossBrowserCucumberRunner {

    protected static final Logger LOGGER = (Logger) LogManager.getLogger(CrossBrowserCucumberRunner.class);
    
    private TestNGCucumberRunner testNGCucumberRunner;
    
    /**
     * Override to provide the tag expression for this runner
     */
    protected abstract String getTagExpressionTemplate();
    
    /**
     * Override to customize login tag resolution
     */
    protected String resolveLoginTag() {
        return DynamicTagResolver.getKFoneLoginTag();
    }
    
    /**
     * Provides browser configurations for cross-browser testing.
     * Override to customize browsers.
     */
    @DataProvider(name = "browsers", parallel = false)
    public Object[][] getBrowsers() {
        return new Object[][] {
            {"chrome", "latest", "Windows"},
            {"firefox", "latest", "Windows"},
            {"edge", "latest", "Windows"}
        };
    }
    
    /**
     * Main test method - runs all scenarios on each browser
     */
    @Test(dataProvider = "browsers", description = "Cross-Browser Testing")
    public void runCrossBrowserTest(String browserName, String browserVersion, String platform) {
        String runnerName = this.getClass().getSimpleName();
        
        try {
            LOGGER.info("▶️  Starting {} on {} browser", runnerName, browserName.toUpperCase());
            
            // Set browser context for reporting
            CrossBrowserDriverManager.setBrowserNameForCurrentThread(browserName);
            System.setProperty("current.browser.name." + Thread.currentThread().getName(), browserName);
            
            // Initialize browser
            CrossBrowserDriverManager.initializeBrowser(browserName, browserVersion, platform);
            
            // Initialize Cucumber runner
            testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
            
            // Run all scenarios
            Object[][] scenarios = testNGCucumberRunner.provideScenarios();
            LOGGER.info("  Running {} scenarios on {}", scenarios.length, browserName.toUpperCase());
            
            for (Object[] scenario : scenarios) {
                PickleWrapper pickleWrapper = (PickleWrapper) scenario[0];
                String scenarioName = pickleWrapper.getPickle().getName();
                
                try {
                    LOGGER.debug("  Executing: '{}'", scenarioName);
                    testNGCucumberRunner.runScenario(pickleWrapper.getPickle());
                } catch (Exception e) {
                    LOGGER.error("  ❌ Scenario '{}' failed: {}", scenarioName, e.getMessage());
                }
            }
            
            LOGGER.info("⏹️  Completed {} on {}", runnerName, browserName.toUpperCase());
            
        } catch (Exception e) {
            LOGGER.error("❌ {} failed on {}: {}", runnerName, browserName.toUpperCase(), e.getMessage());
            throw e;
        }
    }
    
    @BeforeMethod
    public void beforeBrowserTest() {
        // Clear any previous browser data
        try {
            if (CrossBrowserDriverManager.isDriverAvailable()) {
                CrossBrowserDriverManager.getDriver().manage().deleteAllCookies();
            }
        } catch (Exception e) {
            LOGGER.debug("Could not clear cookies: {}", e.getMessage());
        }
    }
    
    @AfterMethod
    public void afterBrowserTest() {
        try {
            // Cleanup browser
            if (CrossBrowserDriverManager.isDriverAvailable()) {
                CrossBrowserDriverManager.quitDriver();
            }
            
            // Cleanup Cucumber runner
            if (testNGCucumberRunner != null) {
                testNGCucumberRunner.finish();
                testNGCucumberRunner = null;
            }
        } catch (Exception e) {
            LOGGER.warn("Cleanup issue: {}", e.getMessage());
        }
    }
}

