package testrunners.JobMapping;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Listeners;

import com.kfonetalentsuite.listeners.ExcelReportListener;
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
			"src/test/resources/features/JobMapping/18aValidateBasicSortingPersistence.feature"
		},
		tags = "@SSO_Login_via_KFONE or @NON_SSO_Login_via_KFONE or @Client_with_PM_Access or @Validate_Basic_Sorting_Persistence",
		glue = {"stepdefinitions.JobMapping", "hooks.JobMapping"},
		dryRun = false,
		plugin = {"html:target/cucumber-reports/cucumber.html", "json:target/cucumber-reports/cucumber.json"}
		)

public class Runner18a_ValidateBasicSortingPersistence extends CustomizeTestNGCucumberRunner {
	
	protected static final Logger LOGGER = (Logger) LogManager.getLogger();
	
	@Override
	protected String getTagExpressionTemplate() {
		return "@SSO_Login_via_KFONE or @NON_SSO_Login_via_KFONE or @Client_with_PM_Access or @Validate_Basic_Sorting_Persistence";
	}
	
	@Override
	protected String resolveLoginTag() {
		return DynamicTagResolver.getKFoneLoginTag();
	}
	
	@AfterTest
	public void after_test() {
		LOGGER.info("Successfully completed testing Basic Sorting Persistence functionality in Job Mapping page");
		LOGGER.info("Login Type Used: " + DynamicTagResolver.getCurrentLoginType());
		DriverManager.closeBrowser();
	}
}

