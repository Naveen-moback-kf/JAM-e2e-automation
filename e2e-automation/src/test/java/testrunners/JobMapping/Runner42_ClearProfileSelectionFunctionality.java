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

/**
 * Clear Profile Selection Functionality Runner
 * 
 * Run specific combinations using tags:
 * - @Clear_Profile_Selection - Run all clear selection tests
 * - @Clear_PM - Run PM (HCM Sync Profiles) tests only
 * - @Clear_JAM - Run JAM (Job Mapping) tests only
 * - @Clear_PM_HeaderCheckbox - PM with Header Checkbox
 * - @Clear_PM_NoneButton - PM with None Button
 * - @Clear_JAM_HeaderCheckbox - JAM with Header Checkbox
 * - @Clear_JAM_NoneButton - JAM with None Button
 */
@Listeners({
	ExcelReportListener.class,
	AllureTestNg.class
})

@CucumberOptions(
		features = {
			"src/test/resources/features/01KFoneLogin.feature",
			"src/test/resources/features/JobMapping/42ClearProfileSelectionFunctionality.feature"
		},
		tags = "@SSO_Login_via_KFONE or @NON_SSO_Login_via_KFONE or @Client_with_PM_Access or @Clear_Profile_Selection",
		glue = {"stepdefinitions.JobMapping", "hooks.JobMapping"},
		dryRun = false,
		plugin = {
			"html:target/cucumber-reports/cucumber.html", 
			"json:target/cucumber-reports/cucumber.json",
			"io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
		}
		)

public class Runner42_ClearProfileSelectionFunctionality extends CustomizeTestNGCucumberRunner {
	protected static final Logger LOGGER = (Logger) LogManager.getLogger();

	@Override
	protected String getTagExpressionTemplate() {
		return "@SSO_Login_via_KFONE or @NON_SSO_Login_via_KFONE or @Client_with_PM_Access or @Clear_Profile_Selection";
	}
	
	@Override
	protected String resolveLoginTag() {
		return DynamicTagResolver.getKFoneLoginTag();
	}

	@AfterTest
	public void after_test() {
		LOGGER.info("Successfully completed Clear Profile Selection Functionality validation");
		LOGGER.info("Login Type Used: " + DynamicTagResolver.getCurrentLoginType());
		DriverManager.closeBrowser();
	}
}

