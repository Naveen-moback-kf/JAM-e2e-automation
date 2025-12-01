package testrunners.JobMapping.crossbrowser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.kfonetalentsuite.listeners.ExcelReportListener;
import com.kfonetalentsuite.webdriverManager.CustomizeTestNGCucumberRunner;
import com.kfonetalentsuite.webdriverManager.CrossBrowserDriverManager;
import com.kfonetalentsuite.webdriverManager.DriverManager;

import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.PickleWrapper;
import io.cucumber.testng.TestNGCucumberRunner;

/**
 * Cross-Browser Recommended Profile Details Test Runner
 * 
 * Command: mvn test -Dtest=CrossBrowser15_ValidateRecommendedProfileDetailsRunner
 * Result: Chrome + Firefox + Edge run automatically
 * Tests: Recommended Profile Details validation across browsers
 * 
 * @author AI Assistant
 */
@Listeners({
    ExcelReportListener.class
})
@CucumberOptions(
    features = "src/test/resources/features",
    tags = "@DYNAMIC_LOGIN or @Client_with_PM_Access or @Validate_Recommended_Profile_Details",
		glue = {"stepdefinitions.JobMapping", "hooks.JobMapping"},
    dryRun = false,
    plugin = {"html:target/cucumber-reports/cucumber.html", "json:target/cucumber-reports/cucumber.json"}
)
public class CrossBrowser15_ValidateRecommendedProfileDetailsRunner extends CustomizeTestNGCucumberRunner {
    
    private static final Logger LOGGER = (Logger) LogManager.getLogger();
    private static final ThreadLocal<TestNGCucumberRunner> runnerThreadLocal = new ThreadLocal<>();
    
    /**
     * Provides the tag expression template for this runner
     * Implementation of abstract method from base class
     * 
     * @return The tag expression template for Cross-Browser Recommended Profile Details validation
     */
    @Override
    protected String getTagExpressionTemplate() {
        return "@DYNAMIC_LOGIN or @Client_with_PM_Access or @Validate_Recommended_Profile_Details";
    }    
    @Override
    protected String resolveLoginTag() {
        String loginTag = com.kfonetalentsuite.utils.common.DynamicTagResolver.getKFoneLoginTag();
        LOGGER.info("Using KFone login tag: " + loginTag);
        return loginTag;
    }
    
    @DataProvider(name = "browsers", parallel = false)
    public Object[][] getBrowsers() {
        return new Object[][] {
            {"chrome", "latest", "Windows"},
            {"firefox", "latest", "Windows"},
            {"edge", "latest", "Windows"}
        };
    }
    
    @Test(dataProvider = "browsers", description = "Cross-Browser Recommended Profile Details Testing")
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
            
            // Initialize Cucumber runner
            TestNGCucumberRunner runner = new TestNGCucumberRunner(this.getClass());
            runnerThreadLocal.set(runner);
            
            // Initialize browser
            CrossBrowserDriverManager.initializeBrowser(browserName, browserVersion, platform);
            
            // Run scenarios
            Object[][] scenarios = runner.provideScenarios();
            LOGGER.info("Running {} scenarios for Recommended Profile Details validation on {}", scenarios.length, browserName.toUpperCase());
            
            // Cross-browser scenario count tracking handled by Enhanced Excel Reporting
            
            for (Object[] scenario : scenarios) {
                PickleWrapper pickleWrapper = (PickleWrapper) scenario[0];
                String scenarioName = pickleWrapper.getPickle().getName();
                
                try {
                    // Synchronize to prevent race conditions
                    synchronized(CrossBrowser15_ValidateRecommendedProfileDetailsRunner.class) {
                        injectDriverIntoFramework();
                        runner.runScenario(pickleWrapper.getPickle());
                    }
               
	} catch (Exception e) {
                    LOGGER.error("Scenario '{}' failed on {}: {}", scenarioName, browserName.toUpperCase(), e.getMessage());
                }
           
	}
            
            LOGGER.info("{} browser Recommended Profile Details testing completed", browserName.toUpperCase());
            
        } catch (Exception e) {
            LOGGER.error("{} browser Recommended Profile Details test failed: {}", browserName.toUpperCase(), e.getMessage());
        } finally {
            cleanup(browserName, customThreadName, originalThreadName);
        }
   
	}
    
    /**
     * Thread-safe driver injection for cross-browser compatibility
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
    
    private void cleanup(String browserName, String customThreadName, String originalThreadName) {
        try {
            if (CrossBrowserDriverManager.isDriverAvailable()) {
                CrossBrowserDriverManager.quitDriver();
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
