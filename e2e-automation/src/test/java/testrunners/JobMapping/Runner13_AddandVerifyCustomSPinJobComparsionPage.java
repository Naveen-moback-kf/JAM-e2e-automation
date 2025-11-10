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
		features = "src/test/resources/features/JobMapping",
		tags = "@DYNAMIC_LOGIN or @Client_with_PM_Access or @Add_and_Verify_CustomSP_In_Job_Comparison_Page",
		glue = {"stepdefinitions.JobMapping", "stepdefinitions.hooks"},
		dryRun = false,
		plugin = {"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:" }
		)

public class Runner13_AddandVerifyCustomSPinJobComparsionPage extends CustomizeTestNGCucumberRunner {
	protected static final Logger LOGGER = (Logger) LogManager.getLogger();
	
	@Override
	protected String getTagExpressionTemplate() {
		return "@DYNAMIC_LOGIN or @Client_with_PM_Access or @Add_and_Verify_CustomSP_In_Job_Comparison_Page";
	}
	
	@Override
	protected String resolveLoginTag() {
		String loginTag = DynamicTagResolver.getKFoneLoginTag();
		LOGGER.info("🔄 Using KFone login tag: " + loginTag);
		return loginTag;
	}
	
	@AfterTest
	public void after_test() {
		LOGGER.info("Successfully completed testing the functionality of adding and verifying Custom SP in Job Comparison page");
		LOGGER.info("Login Type Used: " + DynamicTagResolver.getCurrentLoginType());	
		DriverManager.closeBrowser();
		
	}
}

