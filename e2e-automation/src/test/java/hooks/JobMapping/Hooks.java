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
// import com.kfonetalentsuite.webdriverManager.DriverManager; // Commented out - only needed if forceKillChromeProcesses is re-enabled

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks implements ISuiteListener {

	protected static final Logger LOGGER = LogManager.getLogger(Hooks.class);
	private static final ThreadLocal<Scenario> currentScenario = new ThreadLocal<>();

	public static Scenario getCurrentScenario() {
		return currentScenario.get();
	}
	
	@Before
	public void beforeScenario(Scenario scenario) {
		currentScenario.set(scenario);
		String scenarioName = scenario.getName();
		String featureName = scenario.getUri().toString();
		String featureFileName = featureName.substring(featureName.lastIndexOf("/") + 1);
		String threadId = Thread.currentThread().getName();
		ExcelReportListener.setCurrentScenario(threadId, scenarioName);
		LOGGER.info("▶️  START: {} [{}]", scenarioName, featureFileName);
	}

	@After
	public void afterScenario(Scenario scenario) {
		String scenarioName = scenario.getName();
		
		if (scenario.isFailed()) {
			LOGGER.error("❌ FAILED: {}", scenarioName);
		} else {
			LOGGER.info("✅ PASSED: {}", scenarioName);
		}
		
		PageObjectManager.reset();
		ScreenshotHandler.resetCounters();
		currentScenario.remove();
	}

	@Override
	public void onStart(ISuite suite) {
		String suiteName = suite.getName();
		boolean isDefaultSuite = "Default suite".equals(suiteName);
		
		if (isDefaultSuite) {
			// Running individual runner, not a test suite
			LOGGER.info("🎯 EXECUTION: Individual Test Runner");
		} else {
			// Running actual test suite
			LOGGER.info("📋 TEST SUITE: {}", suiteName);
		}
		
		AllureReportingManager.checkAndPerformDailyReset();
		AllureReportingManager.generateEnvironmentInfo();
	}

	@Override
	public void onFinish(ISuite suite) {
		String suiteName = suite.getName();
		boolean isDefaultSuite = "Default suite".equals(suiteName);
		LOGGER.info("🧹 CLEANUP: Finalizing execution...");
		cleanupAllThreadLocals();
		
		// DISABLED: Force kill closes ALL Chrome processes including manually opened browsers
		// Only use this if you need aggressive cleanup and no other Chrome instances should be running
		// DriverManager.forceKillChromeProcesses();
		
		if (isDefaultSuite) {
			LOGGER.info("🏁 EXECUTION COMPLETED");
		} else {
			LOGGER.info("🏁 TEST SUITE COMPLETED: {}", suiteName);
		}
	}

	private void cleanupAllThreadLocals() {
		try {
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

			LOGGER.info("🧹 ThreadLocal cleanup: {} variables cleared", cleanedCount);

		} catch (Exception e) {
			LOGGER.warn("⚠️  ThreadLocal cleanup failed: {}", e.getMessage());
		}
	}
}

