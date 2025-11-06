package testrunners.functional.JobMapping.crossbrowser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.JobMapping.listeners.ExcelReportListener;
import com.JobMapping.webdriverManager.CustomizeTestNGCucumberRunner;
import com.JobMapping.webdriverManager.CrossBrowserDriverManager;
import com.JobMapping.webdriverManager.DriverManager;

import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.PickleWrapper;
import io.cucumber.testng.TestNGCucumberRunner;

/**
 * Cross-Browser KFone Login Test Runner
 * 
 * This runner executes KFone Login tests across multiple browsers (Chrome, Firefox, Edge)
 * with parallel execution and comprehensive Excel reporting.
 * 
 * Command: mvn test -Dtest=CrossBrowser01_KFoneLoginRunner
 * Result: Chrome + Firefox + Edge run automatically in parallel
 * 
 * @author AI Assistant
 */
@Listeners({
    ExcelReportListener.class
})
@CucumberOptions(
    features = "src/test/resources/features/functional/01KFoneLogin.feature",
    tags = "@DYNAMIC_LOGIN or @Client_with_PM_Access",
    glue = {"stepdefinitions.functional.JobMapping", "stepdefinitions.hooks"},
    dryRun = false,
    plugin = {"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"}
)
public class CrossBrowser01_KFoneLoginRunner extends CustomizeTestNGCucumberRunner {
    
    private static final Logger LOGGER = (Logger) LogManager.getLogger();
    private static final ThreadLocal<TestNGCucumberRunner> runnerThreadLocal = new ThreadLocal<>();
    
    /**
     * Provides the tag expression template for this runner
     * Implementation of abstract method from base class
     * 
     * @return The tag expression template for Cross-Browser KFone Login functionality
     */
    @Override
    protected String getTagExpressionTemplate() {
        return "@DYNAMIC_LOGIN or @Client_with_PM_Access";
    }
    
    /**
     * Resolves the dynamic login tag for KFone login
     * 
     * @return The resolved KFone login tag
     */
    @Override
    protected String resolveLoginTag() {
        String loginTag = com.JobMapping.utils.common.DynamicTagResolver.getKFoneLoginTag();
        LOGGER.info("Using KFone login tag: " + loginTag);
        return loginTag;
    }
    
    /**
     * Provides browser configurations for cross-browser testing
     * 
     * @return Array of browser configurations [browserName, version, platform]
     */
    @DataProvider(name = "browsers", parallel = true)
    public Object[][] getBrowsers() {
        return new Object[][] {
            {"chrome", "latest", "Windows"},
            {"firefox", "latest", "Windows"},
            {"edge", "latest", "Windows"}
        };
    }
    
    /**
     * Main test method that runs KFone Login scenarios across different browsers
     * 
     * @param browserName Name of the browser (chrome, firefox, edge)
     * @param browserVersion Version of the browser (typically "latest")
     * @param platform Operating system platform
     */
    @Test(dataProvider = "browsers", description = "Cross-Browser KFone Login Testing")
    public void runCrossBrowserTest(String browserName, String browserVersion, String platform) {
        String originalThreadName = Thread.currentThread().getName();
        String customThreadName = browserName.toUpperCase();
        Thread.currentThread().setName(customThreadName);
        
        try {
            // Set browser name for reporting
            CrossBrowserDriverManager.setBrowserNameForCurrentThread(browserName);
            
            // Set runner class information for Excel reporting
            System.setProperty("current.runner.class." + Thread.currentThread().getName(), 
                              this.getClass().getName());
            System.setProperty("current.browser.name." + Thread.currentThread().getName(), browserName);
            
            LOGGER.info("Initializing Cross-Browser KFone Login Test for {}", browserName.toUpperCase());
            
            // Initialize Cucumber runner
            TestNGCucumberRunner runner = new TestNGCucumberRunner(this.getClass());
            runnerThreadLocal.set(runner);
            
            // Initialize browser
            CrossBrowserDriverManager.initializeBrowser(browserName, browserVersion, platform);
            
            // Run scenarios
            Object[][] scenarios = runner.provideScenarios();
            LOGGER.info("Running {} scenarios on {} for KFone Login", scenarios.length, browserName.toUpperCase());
            
            // Cross-browser scenario count tracking handled by Enhanced Excel Reporting
            
            for (Object[] scenario : scenarios) {
                PickleWrapper pickleWrapper = (PickleWrapper) scenario[0];
                String scenarioName = pickleWrapper.getPickle().getName();
                
                try {
                    LOGGER.info("Executing scenario '{}' on {}", scenarioName, browserName.toUpperCase());
                    
                    // Synchronize to prevent race conditions
                    synchronized(CrossBrowser01_KFoneLoginRunner.class) {
                        injectDriverIntoFramework();
                        runner.runScenario(pickleWrapper.getPickle());
                    }
                    
                    LOGGER.info("Scenario '{}' completed successfully on {}", scenarioName, browserName.toUpperCase());
                    
                } catch (Exception e) {
                    LOGGER.error("Scenario '{}' failed on {}: {}", scenarioName, browserName.toUpperCase(), e.getMessage());
                }
            }
            
            LOGGER.info("{} browser KFone Login testing completed", browserName.toUpperCase());
            
        } catch (Exception e) {
            LOGGER.error("{} browser KFone Login test failed: {}", browserName.toUpperCase(), e.getMessage());
        } finally {
            cleanup(browserName, customThreadName, originalThreadName);
        }
    }
    
    /**
     * Thread-safe driver injection for cross-browser compatibility
     * Ensures the correct driver is used in each thread
     */
    private void injectDriverIntoFramework() {
        try {
            var driver = CrossBrowserDriverManager.getDriver();
            if (driver != null) {
                synchronized(DriverManager.class) {
                    var driverField = DriverManager.class.getDeclaredField("driver");
                    driverField.setAccessible(true);
                    driverField.set(null, driver);
                    
                    var waitField = DriverManager.class.getDeclaredField("wait");
                    waitField.setAccessible(true);
                    waitField.set(null, CrossBrowserDriverManager.getWait());
                }
            }
        } catch (Exception e) {
            LOGGER.warn("Driver injection failed: {}", e.getMessage());
        }
    }
    
    /**
     * Cleanup method to properly close resources after test execution
     * 
     * @param browserName Name of the browser being cleaned up
     * @param customThreadName Custom thread name used during test
     * @param originalThreadName Original thread name to restore
     */
    private void cleanup(String browserName, String customThreadName, String originalThreadName) {
        try {
            if (CrossBrowserDriverManager.isDriverAvailable()) {
                CrossBrowserDriverManager.quitDriver();
                LOGGER.info("{} browser closed successfully", browserName.toUpperCase());
            }
            TestNGCucumberRunner runner = runnerThreadLocal.get();
            if (runner != null) {
                runner.finish();
                runnerThreadLocal.remove();
            }
        } catch (Exception e) {
            LOGGER.warn("Cleanup issue for {}: {}", browserName.toUpperCase(), e.getMessage());
        } finally {
            Thread.currentThread().setName(originalThreadName);
        }
    }
}

