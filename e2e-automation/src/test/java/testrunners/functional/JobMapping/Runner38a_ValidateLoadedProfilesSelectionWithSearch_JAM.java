package testrunners.functional.JobMapping;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Listeners;

import com.JobMapping.listeners.ExcelReportListener;
import com.JobMapping.utils.common.DynamicTagResolver;
import com.JobMapping.webdriverManager.CustomizeTestNGCucumberRunner;
import com.JobMapping.webdriverManager.DriverManager;

import io.cucumber.testng.CucumberOptions;

@Listeners({
	ExcelReportListener.class
})

@CucumberOptions(
		features = "src/test/resources/features/functional",
		tags = "@DYNAMIC_LOGIN or @Client_with_PM_Access or @Select_Loaded_Profiles_With_Search_JAM",
		glue = {"stepdefinitions.functional.JobMapping", "stepdefinitions.hooks"},
		dryRun = false,
		plugin = {"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:" }
		)

public class Runner38a_ValidateLoadedProfilesSelectionWithSearch_JAM extends CustomizeTestNGCucumberRunner {
	protected static final Logger LOGGER = (Logger) LogManager.getLogger();

	@Override
	protected String getTagExpressionTemplate() {
		return "@DYNAMIC_LOGIN or @Client_with_PM_Access or @Select_Loaded_Profiles_With_Search_JAM";
	}

	@Override
	protected String resolveLoginTag() {
		String loginTag = DynamicTagResolver.getKFoneLoginTag();
		LOGGER.info("🔄 Using KFone login tag: " + loginTag);
		return loginTag;
	}

	@AfterTest
	public void after_test() {
		LOGGER.info("Successfully completed validation of Loaded Profiles Selection with Search in Job Mapping screen");
		LOGGER.info("Login Type Used: " + DynamicTagResolver.getCurrentLoginType());
		DriverManager.closeBrowser();
	}
}

