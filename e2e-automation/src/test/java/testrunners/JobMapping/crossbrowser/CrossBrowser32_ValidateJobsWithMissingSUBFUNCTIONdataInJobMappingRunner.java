package testrunners.JobMapping.crossbrowser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.testng.annotations.Listeners;

import com.kfonetalentsuite.listeners.ExcelReportListener;
import com.kfonetalentsuite.utils.common.DynamicTagResolver;
import com.kfonetalentsuite.webdriverManager.CrossBrowserCucumberRunner;

import io.cucumber.testng.CucumberOptions;

/**
 * Cross-Browser Jobs With Missing SUBFUNCTION Data Test Runner
 * 
 * Command: mvn test -Dtest=CrossBrowser32_ValidateJobsWithMissingSUBFUNCTIONdataInJobMappingRunner
 * Result: Chrome + Firefox + Edge run automatically
 */
@Listeners({
    ExcelReportListener.class
})
@CucumberOptions(
    features = {
        "src/test/resources/features/01KFoneLogin.feature",
        "src/test/resources/features/JobMapping/32ValidateJobsWithMissingSUBFUNCTIONdataInJobMapping.feature"
    },
    tags = "@DYNAMIC_LOGIN or @Client_with_PM_Access or @Validate_Jobs_With_Missing_SUBFUNCTION_Data_In_JobMapping",
    glue = {"stepdefinitions.JobMapping", "hooks.JobMapping"},
    dryRun = false,
    plugin = {"html:target/cucumber-reports/cucumber.html", "json:target/cucumber-reports/cucumber.json"}
)
public class CrossBrowser32_ValidateJobsWithMissingSUBFUNCTIONdataInJobMappingRunner extends CrossBrowserCucumberRunner {
    
    protected static final Logger LOGGER = (Logger) LogManager.getLogger();
    
    @Override
    protected String getTagExpressionTemplate() {
        return "@DYNAMIC_LOGIN or @Client_with_PM_Access or @Validate_Jobs_With_Missing_SUBFUNCTION_Data_In_JobMapping";
    }
    
    @Override
    protected String resolveLoginTag() {
        return DynamicTagResolver.getKFoneLoginTag();
    }
}
