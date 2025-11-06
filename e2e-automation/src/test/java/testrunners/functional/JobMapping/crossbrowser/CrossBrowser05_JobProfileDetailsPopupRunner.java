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
 * Cross-Browser Job Profile Details Popup Test Runner
 * 
 * Command: mvn test -Dtest=CrossBrowser05_JobProfileDetailsPopupRunner
 * Result: Chrome + Firefox + Edge run automatically
 * Tests: Job Profile Details Popup functionality across browsers
 * 
 * @author AI Assistant
 */
@Listeners({
    ExcelReportListener.class
})
@CucumberOptions(
    features = "src/test/resources/features/functional",
    tags = "@DYNAMIC_LOGIN or @Client_with_PM_Access or @Validate_Job_Profile_Details_Popup",
		glue = {"stepdefinitions.functional.JobMapping", "stepdefinitions.hooks"},
    dryRun = false,
    plugin = {"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"}
)
public class CrossBrowser05_JobProfileDetailsPopupRunner extends CustomizeTestNGCucumberRunner {
    
    private static final Logger LOGGER = (Logger) LogManager.getLogger();
    private static final ThreadLocal<TestNGCucumberRunner> runnerThreadLocal = new ThreadLocal<>();
    
    /**
     * Provides the tag expression template for this runner
     * Implementation of abstract method from base class
     * 
     * @return The tag expression template for Job Profile Details Popup validation
     */
    @Override
    protected String getTagExpressionTemplate() {
        return "@DYNAMIC_LOGIN or @Client_with_PM_Access or @Validate_Job_Profile_Details_Popup";
    }    
    @Override
    protected String resolveLoginTag() {
        String loginTag = com.JobMapping.utils.common.DynamicTagResolver.getKFoneLoginTag();
        LOGGER.info("Using KFone login tag: " + loginTag);
        return loginTag;
    }
    
    @DataProvider(name = "browsers", parallel = true)
    public Object[][] getBrowsers() {
        return new Object[][] {
            {"chrome", "latest", "Windows"},
            {"firefox", "latest", "Windows"},
            {"edge", "latest", "Windows"}
        };
    }
    
    @Test(dataProvider = "browsers", description = "Cross-Browser Job Profile Details Popup Testing")
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
            LOGGER.info("Running {} scenarios for Job Profile Details Popup on {}", scenarios.length, browserName.toUpperCase());
            
            // Cross-browser scenario count tracking handled by Enhanced Excel Reporting
            
            for (Object[] scenario : scenarios) {
                PickleWrapper pickleWrapper = (PickleWrapper) scenario[0];
                String scenarioName = pickleWrapper.getPickle().getName();
                
                try {
                    // Synchronize to prevent race conditions
                    synchronized(CrossBrowser05_JobProfileDetailsPopupRunner.class) {
                        injectDriverIntoFramework();
                        runner.runScenario(pickleWrapper.getPickle());
                    }
               
	} catch (Exception e) {
                    LOGGER.error("Scenario '{}' failed on {}: {}", scenarioName, browserName.toUpperCase(), e.getMessage());
                }
           
	}
            
            LOGGER.info("{} browser Job Profile Details Popup testing completed", browserName.toUpperCase());
            
        } catch (Exception e) {
            LOGGER.error("{} browser Job Profile Details Popup test failed: {}", browserName.toUpperCase(), e.getMessage());
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
