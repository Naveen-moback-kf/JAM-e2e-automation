package testrunners.JobMapping.crossbrowser;

import org.testng.annotations.Listeners;

import com.kfonetalentsuite.listeners.ExcelReportListener;
import io.qameta.allure.testng.AllureTestNg;
import com.kfonetalentsuite.utils.common.DynamicTagResolver;
import com.kfonetalentsuite.webdriverManager.CrossBrowserCucumberRunner;

import io.cucumber.testng.CucumberOptions;

@Listeners({
    ExcelReportListener.class,
    AllureTestNg.class
})

@CucumberOptions(
        features = {
            "src/test/resources/features/01KFoneLogin.feature",
            "src/test/resources/features/JobMapping/27_SelectAllWithSearchFunctionality_PM.feature"
        },
        tags = "@DYNAMIC_LOGIN or @SelectAll_With_Search_Functionality",
        glue = {"stepdefinitions.JobMapping", "hooks.JobMapping"},
        dryRun = false,
        plugin = {
            "html:target/cucumber-reports/cucumber.html", 
            "json:target/cucumber-reports/cucumber.json",
            "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        }
)

public class CrossBrowser27_SelectAllWithSearchFunctionalityRunner extends CrossBrowserCucumberRunner {

    @Override
    protected String getTagExpressionTemplate() {
        return "@DYNAMIC_LOGIN or @SelectAll_With_Search_Functionality";
    }

    @Override
    protected String resolveLoginTag() {
        return DynamicTagResolver.getKFoneLoginTag();
    }
}

