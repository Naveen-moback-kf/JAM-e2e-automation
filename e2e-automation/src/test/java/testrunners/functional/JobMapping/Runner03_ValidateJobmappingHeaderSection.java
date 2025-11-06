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
		tags = "@DYNAMIC_LOGIN or @Client_with_PM_Access or @Validate_Job_Mapping_UI_Header_Section",
		glue = {"stepdefinitions.functional.JobMapping", "stepdefinitions.hooks"},
		dryRun = false,
		plugin = {"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:" }
		)

public class Runner03_ValidateJobmappingHeaderSection extends CustomizeTestNGCucumberRunner {
	
	protected static final Logger LOGGER = (Logger) LogManager.getLogger();
	
	@Override
	protected String getTagExpressionTemplate() {
		return "@DYNAMIC_LOGIN or @Client_with_PM_Access or @Validate_Job_Mapping_UI_Header_Section";
	}
	
	@Override
	protected String resolveLoginTag() {
		String loginTag = DynamicTagResolver.getKFoneLoginTag();
		LOGGER.info("🔄 Using KFone login tag: " + loginTag);
		return loginTag;
	}
	
	@AfterTest
	public void after_test() {
		LOGGER.info("Job Mapping UI Header section validated successfully");
		LOGGER.info("Login Type Used: " + DynamicTagResolver.getCurrentLoginType());
		DriverManager.closeBrowser();

	}
}
