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

    private TestNGCucumberRunner testNGCucumberRunner;
    protected static final Logger LOGGER = (Logger) LogManager.getLogger(CustomizeTestNGCucumberRunner.class);
    protected abstract String getTagExpressionTemplate();
    
    @BeforeTest
    public final void setupDynamicTags() {
        try {
            VariableManager.getInstance().loadProperties();
            String loginTag = resolveLoginTag();
            String tagExpression = buildTagExpression(loginTag);
            System.setProperty("cucumber.filter.tags", tagExpression);
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
        testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
    }

    @SuppressWarnings("unused")
    @Test(groups = "cucumber", description = "Runs Cucumber Scenarios", dataProvider = "scenarios")
    public void runScenario(PickleWrapper pickleWrapper, FeatureWrapper featureWrapper) {
        testNGCucumberRunner.runScenario(pickleWrapper.getPickle());
    }

    
    @DataProvider
    public Object[][] scenarios() {
        if (testNGCucumberRunner == null) {
            return new Object[0][0];
        }
        return testNGCucumberRunner.provideScenarios();
    }

    @AfterClass(alwaysRun = true)
    public void tearDownClass() {
        if (testNGCucumberRunner == null) {
            return;
        }
        testNGCucumberRunner.finish();
    }
}
