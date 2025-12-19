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
			"src/test/resources/features/JobMapping/14_SortingFunctionality_JAM.feature"
		},
		tags = "@SSO_Login_via_KFONE or @NON_SSO_Login_via_KFONE or @Client_with_PM_Access or @Validate_Sorting_Functionality",
		glue = {"stepdefinitions.JobMapping", "hooks.JobMapping"},
		dryRun = false,
		plugin = {
			"html:target/cucumber-reports/cucumber.html", 
			"json:target/cucumber-reports/cucumber.json",
			"io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
		}
		)

public class Runner14_SortingFunctionality_JAM extends CustomizeTestNGCucumberRunner {
	protected static final Logger LOGGER = (Logger) LogManager.getLogger();

	@Override
	protected String getTagExpressionTemplate() {
		return "@SSO_Login_via_KFONE or @NON_SSO_Login_via_KFONE or @Client_with_PM_Access or @Validate_Sorting_Functionality";
	}
	
	@Override
	protected String resolveLoginTag() {
		return DynamicTagResolver.getKFoneLoginTag();
	}

	@AfterTest
	public void after_test() {
		LOGGER.info("Successfully Validated Sorting Functionality in Job Mapping page");
		LOGGER.info("Login Type Used: " + DynamicTagResolver.getCurrentLoginType());
		DriverManager.closeBrowser();
	}
}

