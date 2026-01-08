package hooks.JobMapping;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ISuite;
import org.testng.ISuiteListener;

import com.kfonetalentsuite.listeners.ExcelReportListener;
import com.kfonetalentsuite.manager.PageObjectManager;
import com.kfonetalentsuite.pageobjects.JobMapping.PO01_KFoneLogin;
import com.kfonetalentsuite.pageobjects.JobMapping.PO18_HCMSyncProfilesTab_PM;
import com.kfonetalentsuite.pageobjects.JobMapping.PO19_ProfileswithNoJobCode_PM;
import com.kfonetalentsuite.pageobjects.JobMapping.PO21_ExportStatusFunctionality_PM;
import com.kfonetalentsuite.pageobjects.JobMapping.PO22_MissingDataTipMessage;
import com.kfonetalentsuite.pageobjects.JobMapping.PO25_MissingDataFunctionality;
import com.kfonetalentsuite.pageobjects.JobMapping.PO27_SelectAllWithSearchFunctionality;
import com.kfonetalentsuite.pageobjects.JobMapping.PO28_SelectAllWithFiltersFunctionality;
import com.kfonetalentsuite.pageobjects.JobMapping.PO30_SelectAndPublishAllJobProfiles_JAM;
import com.kfonetalentsuite.pageobjects.JobMapping.PO32_ClearProfileSelectionFunctionality;
import com.kfonetalentsuite.pageobjects.JobMapping.PO33_UnmappedJobs_JAM;
import com.kfonetalentsuite.utils.JobMapping.AllureReportingManager;
import com.kfonetalentsuite.utils.common.ScreenshotHandler;
import com.kfonetalentsuite.utils.common.SessionManager;
import com.kfonetalentsuite.utils.common.CommonVariableManager;
import com.kfonetalentsuite.webdriverManager.DriverManager;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

/**
 * Unified Hooks for Test Suite and Scenario Lifecycle Management
 * Combines TestNG suite-level hooks and Cucumber scenario-level hooks
 */
public class Hooks implements ISuiteListener {

	protected static final Logger LOGGER = LogManager.getLogger(Hooks.class);
	private static final ThreadLocal<Scenario> currentScenario = new ThreadLocal<>();

	// ==================== SCENARIO LIFECYCLE (Cucumber Hooks) ====================

	/**
	 * Returns the current Cucumber scenario for the executing thread
	 */
	public static Scenario getCurrentScenario() {
		return currentScenario.get();
	}

	/**
	 * Executed before each Cucumber scenario
	 */
	@Before
	public void beforeScenario(Scenario scenario) {
		currentScenario.set(scenario);
		String scenarioName = scenario.getName();
		String featureName = scenario.getUri().toString();
		String featureFileName = featureName.substring(featureName.lastIndexOf("/") + 1);
		String threadId = Thread.currentThread().getName();
		ExcelReportListener.setCurrentScenario(threadId, scenarioName);

		LOGGER.info("==================================================");
		LOGGER.info("SCENARIO STARTED: {} | Feature: {}", scenarioName, featureFileName);
		LOGGER.info("Scenario Tags: {}", scenario.getSourceTagNames());
		LOGGER.info("==================================================");
	}

	/**
	 * Executed after each Cucumber scenario
	 */
	@After
	public void afterScenario(Scenario scenario) {
		String scenarioName = scenario.getName();
		String status = scenario.getStatus().toString();

		LOGGER.info("==================================================");
		LOGGER.info("SCENARIO FINISHED: {} | Status: {}", scenarioName, status);

		if (scenario.isFailed()) {
			LOGGER.error("SCENARIO FAILED: {} - Check test execution details", scenarioName);
		} else {
			LOGGER.info("SCENARIO PASSED: {} - Executed successfully", scenarioName);
		}
		LOGGER.info("==================================================");

		// Cleanup scenario-level resources
		PageObjectManager.reset();
		LOGGER.debug("PageObjectManager reset completed for thread: {}", Thread.currentThread().getName());
		ScreenshotHandler.resetCounters();
		currentScenario.remove();
	}

	// ==================== SUITE LIFECYCLE (TestNG Listener) ====================

	/**
	 * Executed before the entire test suite starts
	 */
	@Override
	public void onStart(ISuite suite) {
		LOGGER.info("==================================================");
		LOGGER.info("TEST SUITE STARTED: {}", suite.getName());
		LOGGER.info("==================================================");

		AllureReportingManager.checkAndPerformDailyReset();
		AllureReportingManager.generateEnvironmentInfo();
	}

	/**
	 * Executed after the entire test suite completes
	 */
	@Override
	public void onFinish(ISuite suite) {
		LOGGER.info("==================================================");
		LOGGER.info("TEST SUITE COMPLETED: {}", suite.getName());
		LOGGER.info("Performing cleanup operations...");
		LOGGER.info("==================================================");

		cleanupAllThreadLocals();
		DriverManager.forceKillChromeProcesses();

		LOGGER.info("==================================================");
		LOGGER.info("CLEANUP COMPLETED - Ready for next execution");
		LOGGER.info("==================================================");
	}

	// ==================== THREADLOCAL CLEANUP ====================

	/**
	 * Cleans up all ThreadLocal variables to prevent memory leaks
	 */
	private void cleanupAllThreadLocals() {
		try {
			LOGGER.info("Cleaning up ThreadLocal variables...");
			int cleanedCount = 0;

			// Core Framework
			SessionManager.reset();
			CommonVariableManager.CURRENT_USER_ROLE.remove();
			cleanedCount += 2;

			// Login & Client
			PO01_KFoneLogin.username.remove();
			PO01_KFoneLogin.clientName.remove();
			cleanedCount += 2;

			// Missing Data Features
			PO25_MissingDataFunctionality.extractedJobName.remove();
			PO25_MissingDataFunctionality.extractedJobCode.remove();
			PO25_MissingDataFunctionality.forwardScenarioFoundProfile.remove();
			PO25_MissingDataFunctionality.forwardScenarioJobName.remove();
			PO25_MissingDataFunctionality.forwardScenarioJobCode.remove();
			PO25_MissingDataFunctionality.currentDataType.remove();
			PO25_MissingDataFunctionality.jobDetailsFromJobMappingPage.remove();
			PO25_MissingDataFunctionality.jobDetailsFromMissingDataScreen.remove();
			cleanedCount += 8;

			// Select All Functionality
			PO27_SelectAllWithSearchFunctionality.searchResultsCount.remove();
			PO27_SelectAllWithSearchFunctionality.alternativeSearchSubstring.remove();
			PO27_SelectAllWithSearchFunctionality.totalSecondSearchResults.remove();
			PO27_SelectAllWithSearchFunctionality.currentScreen.remove();
			PO27_SelectAllWithSearchFunctionality.loadedProfilesBeforeScroll.remove();
			cleanedCount += 5;

			PO28_SelectAllWithFiltersFunctionality.filterResultsCount.remove();
			PO28_SelectAllWithFiltersFunctionality.firstFilterType.remove();
			PO28_SelectAllWithFiltersFunctionality.firstFilterValue.remove();
			PO28_SelectAllWithFiltersFunctionality.secondFilterType.remove();
			PO28_SelectAllWithFiltersFunctionality.secondFilterValue.remove();
			PO28_SelectAllWithFiltersFunctionality.totalSecondFilterResults.remove();
			PO28_SelectAllWithFiltersFunctionality.currentScreen.remove();
			cleanedCount += 7;

			// Publish & Selection
			PO30_SelectAndPublishAllJobProfiles_JAM.unpublishedProfilesCountBefore.remove();
			PO30_SelectAndPublishAllJobProfiles_JAM.publishedProfilesCountBefore.remove();
			PO30_SelectAndPublishAllJobProfiles_JAM.unpublishedProfilesCountAfter.remove();
			PO30_SelectAndPublishAllJobProfiles_JAM.publishedProfilesCountAfter.remove();
			PO30_SelectAndPublishAllJobProfiles_JAM.totalPublishedCount.remove();
			PO30_SelectAndPublishAllJobProfiles_JAM.expectedTotalMinutes.remove();
			PO30_SelectAndPublishAllJobProfiles_JAM.profilesToBePublished.remove();
			PO30_SelectAndPublishAllJobProfiles_JAM.selectedProfilesCount.remove();
			cleanedCount += 8;

			PO32_ClearProfileSelectionFunctionality.loadedProfilesBeforeUncheck.remove();
			PO32_ClearProfileSelectionFunctionality.selectedProfilesBeforeUncheck.remove();
			PO32_ClearProfileSelectionFunctionality.currentScreen.remove();
			cleanedCount += 3;

			PO33_UnmappedJobs_JAM.skipScenario.remove();
			cleanedCount++;

			// HCM & Profile Manager
			PO18_HCMSyncProfilesTab_PM.jobProfileName.remove();
			PO18_HCMSyncProfilesTab_PM.intialResultsCount.remove();
			PO18_HCMSyncProfilesTab_PM.updatedResultsCount.remove();
			PO18_HCMSyncProfilesTab_PM.orgJobNameInRow1.remove();
			PO18_HCMSyncProfilesTab_PM.profilesCount.remove();
			PO18_HCMSyncProfilesTab_PM.isProfilesCountComplete.remove();
			cleanedCount += 6;

			PO19_ProfileswithNoJobCode_PM.rowNumber.remove();
			PO19_ProfileswithNoJobCode_PM.SPJobName.remove();
			PO19_ProfileswithNoJobCode_PM.noJobCode.remove();
			cleanedCount += 3;

			PO21_ExportStatusFunctionality_PM.rowNumber.remove();
			PO21_ExportStatusFunctionality_PM.SPJobName.remove();
			cleanedCount += 2;

			PO22_MissingDataTipMessage.initialJobCount.remove();
			cleanedCount++;

			LOGGER.info("✅ ThreadLocal cleanup completed - {} variables cleaned", cleanedCount);

		} catch (Exception e) {
			LOGGER.warn("ThreadLocal cleanup encountered an issue: {}", e.getMessage());
		}
	}
}

