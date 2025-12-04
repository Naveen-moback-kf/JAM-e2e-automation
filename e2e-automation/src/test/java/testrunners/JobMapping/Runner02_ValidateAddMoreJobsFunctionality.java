package testrunners.JobMapping;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;

import com.kfonetalentsuite.listeners.ExcelReportListener;
import com.kfonetalentsuite.utils.JobMapping.JobCatalogRefresher;
import com.kfonetalentsuite.utils.common.DynamicTagResolver;
import com.kfonetalentsuite.webdriverManager.CustomizeTestNGCucumberRunner;
import com.kfonetalentsuite.webdriverManager.DriverManager;

import io.cucumber.testng.CucumberOptions;

@Listeners({
	ExcelReportListener.class
})

@CucumberOptions(
		features = {
			"src/test/resources/features/01KFoneLogin.feature",
			"src/test/resources/features/JobMapping/02ValidateAddMoreJobsFunctionality.feature"
		},
		tags = "@SSO_Login_via_KFONE or @NON_SSO_Login_via_KFONE or @Client_with_PM_Access or @Validate_Add_More_Jobs_Functionality",
		glue = {"stepdefinitions.JobMapping", "hooks.JobMapping"},
		dryRun = false,
		plugin = {"html:target/cucumber-reports/cucumber.html", "json:target/cucumber-reports/cucumber.json"}
		)

public class Runner02_ValidateAddMoreJobsFunctionality extends CustomizeTestNGCucumberRunner {
	protected static final Logger LOGGER = (Logger) LogManager.getLogger();
	
	/**
	 * Refreshes Job Catalog CSV with unique Job Codes and Job Titles
	 * This runs BEFORE the test class setup to ensure fresh unique data
	 */
	@BeforeClass(alwaysRun = true)
	public void refreshJobCatalogBeforeTests() {
		try {
			LOGGER.info("Runner02: Refreshing Job Catalog Before Execution    ");
			
			boolean success = JobCatalogRefresher.refreshJobCatalog();
			
			if (success) {
				LOGGER.info("Job Catalog refreshed successfully for Runner02");
			} else {
				LOGGER.warn("Job Catalog refresh encountered issues - check logs");
			}
			
		} catch (Exception e) {
			LOGGER.error("Job Catalog refresh failed: {}", e.getMessage(), e);
			// Don't fail the test execution, just log the error
		}
	}
	
	@Override
	protected String getTagExpressionTemplate() {
		return "@SSO_Login_via_KFONE or @NON_SSO_Login_via_KFONE or @Client_with_PM_Access or @Validate_Add_More_Jobs_Functionality";
	}
	
	@Override
	protected String resolveLoginTag() {
		String loginTag = DynamicTagResolver.getKFoneLoginTag();
		LOGGER.info("Using KFone login tag: " + loginTag);
		return loginTag;
	}
	
	@AfterTest
	public void after_test() {
		LOGGER.info("Successfully completed testing the functionality of Add More Jobs to AI Auto Job Mapping page");
		LOGGER.info("Login Type Used: " + DynamicTagResolver.getCurrentLoginType());
		DriverManager.closeBrowser();
		
	}
}

