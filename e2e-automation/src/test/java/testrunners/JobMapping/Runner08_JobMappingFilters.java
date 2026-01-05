package testrunners.JobMapping;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Listeners;

import com.kfonetalentsuite.listeners.ExcelReportListener;
import io.qameta.allure.testng.AllureTestNg;
import com.kfonetalentsuite.utils.common.DynamicTagResolver;
import com.kfonetalentsuite.webdriverManager.CustomizeTestNGCucumberRunner;
import com.kfonetalentsuite.webdriverManager.DriverManager;

import io.cucumber.testng.CucumberOptions;

@Listeners({
	ExcelReportListener.class,
	AllureTestNg.class
})

@CucumberOptions(
		features = {
			"src/test/resources/features/01KFoneLogin.feature",
			"src/test/resources/features/JobMapping/08_JobMappingFilters.feature"
		},
		tags = "@SSO_Login_via_KFONE or @NON_SSO_Login_via_KFONE or @Client_with_PM_Access or @Validate_Filters_Functionality_JAM",
		glue = {"stepdefinitions.JobMapping", "hooks.JobMapping"},
		dryRun = false,
		plugin = {
			"html:target/cucumber-reports/cucumber.html", 
			"json:target/cucumber-reports/cucumber.json",
			"io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
		}
		)

public class Runner08_JobMappingFilters extends CustomizeTestNGCucumberRunner {
	
	protected static final Logger LOGGER = (Logger) LogManager.getLogger();
	
	@Override
	protected String getTagExpressionTemplate() {
		// Feature-level tag includes all scenarios - login filtering handled by base class
		return "@SSO_Login_via_KFONE or @NON_SSO_Login_via_KFONE or @Client_with_PM_Access or @Validate_Filters_Functionality_JAM";
	}
	
	@Override
	protected String resolveLoginTag() {
		return DynamicTagResolver.getKFoneLoginTag();
	}
	
	@AfterTest
	public void after_test() {
		LOGGER.info("Successfully completed testing of Filters Functionality in Job Mapping page");
		LOGGER.info("Login Type Used: " + DynamicTagResolver.getCurrentLoginType());
		DriverManager.closeBrowser();
	}
}

