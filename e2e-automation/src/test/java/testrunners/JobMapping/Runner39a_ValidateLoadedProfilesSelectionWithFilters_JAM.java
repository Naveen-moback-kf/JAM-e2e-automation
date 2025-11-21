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
			"src/test/resources/features/JobMapping/39aValidateLoadedProfilesSelectionWithFilters_JAM.feature"
		},
		tags = "@SSO_Login_via_KFONE or @NON_SSO_Login_via_KFONE or @Client_with_PM_Access or @Select_Job_Mapping_Loaded_Profiles_With_Filter_JAM",
		glue = {"stepdefinitions.JobMapping", "hooks.JobMapping"},
		dryRun = false,
		plugin = {"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:" }
		)

public class Runner39a_ValidateLoadedProfilesSelectionWithFilters_JAM extends CustomizeTestNGCucumberRunner {
	protected static final Logger LOGGER = (Logger) LogManager.getLogger();

	@Override
	protected String getTagExpressionTemplate() {
		return "@SSO_Login_via_KFONE or @NON_SSO_Login_via_KFONE or @Client_with_PM_Access or @Select_Job_Mapping_Loaded_Profiles_With_Filter_JAM";
	}

	@Override
	protected String resolveLoginTag() {
		String loginTag = DynamicTagResolver.getKFoneLoginTag();
		LOGGER.info(" Using KFone login tag: " + loginTag);
		return loginTag;
	}

	@AfterTest
	public void after_test() {
		LOGGER.info("Successfully completed validation of Loaded Profiles Selection with Filters in Job Mapping screen");
		LOGGER.info("Login Type Used: " + DynamicTagResolver.getCurrentLoginType());
		DriverManager.closeBrowser();
	}
}

