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
		features = "src/test/resources/features/01KFoneLogin.feature",
		tags = "@DYNAMIC_LOGIN or @Client_with_PM_Access",
		glue = {"stepdefinitions.JobMapping", "hooks.JobMapping"},
		dryRun = false,
		plugin = {"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:" }
		)

public class Runner01_KFoneLogin extends CustomizeTestNGCucumberRunner {
	protected static final Logger LOGGER = (Logger) LogManager.getLogger();
	
	@Override
	protected String getTagExpressionTemplate() {
		return "@DYNAMIC_LOGIN or @Client_with_PM_Access";
	}
	
	@Override
	protected String resolveLoginTag() {
		String loginTag = DynamicTagResolver.getKFoneLoginTag();
		LOGGER.info("Using KFone login tag: " + loginTag);
		return loginTag;
	}
	
	@AfterTest
	public void after_test() {
		LOGGER.info("Successfully Completed Testing of Login to KFONE Application and Find client with access to Profile Manager Application");
		LOGGER.info("Login Type Used: " + DynamicTagResolver.getCurrentLoginType());
		DriverManager.closeBrowser();
	}
	}
