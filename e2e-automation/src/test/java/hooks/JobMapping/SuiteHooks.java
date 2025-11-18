package hooks.JobMapping;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ISuite;
import org.testng.ISuiteListener;

import com.kfonetalentsuite.webdriverManager.DriverManager;

/**
 * Suite-level hooks for test execution lifecycle management
 * Implements ISuiteListener to properly integrate with TestNG
 */
public class SuiteHooks implements ISuiteListener {
    
    protected static final Logger LOGGER = LogManager.getLogger(SuiteHooks.class);
    
    @Override
    public void onStart(ISuite suite) {
        LOGGER.info("==================================================");
        LOGGER.info("TEST SUITE STARTED: {}", suite.getName());
        LOGGER.info("==================================================");
    }
    
    @Override
    public void onFinish(ISuite suite) {
        LOGGER.info("==================================================");
        LOGGER.info("TEST SUITE COMPLETED: {}", suite.getName());
        LOGGER.info("Performing cleanup operations...");
        LOGGER.info("==================================================");
        
        // PERFORMANCE OPTIMIZATION: Force kill all Chrome processes
        // This ensures clean state for next test run and prevents slowdown
        DriverManager.forceKillChromeProcesses();
        
        LOGGER.info("==================================================");
        LOGGER.info("CLEANUP COMPLETED - Ready for next execution");
        LOGGER.info("==================================================");
    }
}
