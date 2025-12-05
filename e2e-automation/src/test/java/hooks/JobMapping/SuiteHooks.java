package hooks.JobMapping;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ISuite;
import org.testng.ISuiteListener;

import com.kfonetalentsuite.webdriverManager.DriverManager;
import com.kfonetalentsuite.pageobjects.JobMapping.*;
import com.kfonetalentsuite.utils.common.CommonVariable;
import com.kfonetalentsuite.utils.JobMapping.SessionManager;
import com.kfonetalentsuite.utils.JobMapping.DailyAllureManager;
import com.kfonetalentsuite.utils.JobMapping.AllureEnvironmentInfo;

/**
 * Suite-level hooks for test execution lifecycle management
 * Implements ISuiteListener to properly integrate with TestNG
 * 
 * PARALLEL EXECUTION: Includes ThreadLocal cleanup to prevent memory leaks
 */
public class SuiteHooks implements ISuiteListener {

	protected static final Logger LOGGER = LogManager.getLogger(SuiteHooks.class);

	@Override
	public void onStart(ISuite suite) {
		LOGGER.info("==================================================");
		LOGGER.info("TEST SUITE STARTED: {}", suite.getName());
		LOGGER.info("==================================================");

		// Perform Allure daily reset check (similar to Excel reporting)
		// This will backup old reports and reset if it's a new day
		DailyAllureManager.checkAndPerformDailyReset();
		
		// Generate Allure environment information
		// This creates environment.properties with browser, OS, and environment details
		AllureEnvironmentInfo.generateEnvironmentInfo();
	}

	@Override
	public void onFinish(ISuite suite) {
		LOGGER.info("==================================================");
		LOGGER.info("TEST SUITE COMPLETED: {}", suite.getName());
		LOGGER.info("Performing cleanup operations...");
		LOGGER.info("==================================================");

		// MEMORY OPTIMIZATION: Clean up all ThreadLocal variables to prevent memory leaks
		cleanupAllThreadLocals();

		// PERFORMANCE OPTIMIZATION: Force kill all Chrome processes
		// This ensures clean state for next test run and prevents slowdown
		DriverManager.forceKillChromeProcesses();

		LOGGER.info("==================================================");
		LOGGER.info("CLEANUP COMPLETED - Ready for next execution");
		LOGGER.info("==================================================");
	}

	/**
	 * PARALLEL EXECUTION OPTIMIZATION: Clean up all ThreadLocal variables
	 * Prevents memory leaks in long-running test suites
	 * 
	 * ThreadLocal variables store thread-specific data. When tests complete,
	 * these variables should be cleared to free memory and prevent stale data
	 * from affecting subsequent test runs.
	 */
	private void cleanupAllThreadLocals() {
		try {
			LOGGER.info("Cleaning up ThreadLocal variables...");
			int cleanedCount = 0;

			// === CORE FRAMEWORK ThreadLocals ===

			// SessionManager - Session state tracking
			SessionManager.reset();
			cleanedCount++;

			// CommonVariable - User role tracking
			CommonVariable.CURRENT_USER_ROLE.remove();
			cleanedCount++;

			// === LOGIN & CLIENT ThreadLocals ===

			// PO01_KFoneLogin - Username and client name
			PO01_KFoneLogin.username.remove();
			PO01_KFoneLogin.clientName.remove();
			cleanedCount += 2;

			// === MISSING DATA FEATURES ThreadLocals (PO29-PO32) ===

			// PO29 - Missing GRADE data
			PO29_ValidateJobsWithMissingGRADEdataInJobMapping.extractedJobName.remove();
			PO29_ValidateJobsWithMissingGRADEdataInJobMapping.forwardScenarioFoundProfile.remove();
			PO29_ValidateJobsWithMissingGRADEdataInJobMapping.forwardScenarioJobName.remove();
			PO29_ValidateJobsWithMissingGRADEdataInJobMapping.forwardScenarioJobCode.remove();
			cleanedCount += 4;

			// PO30 - Missing DEPARTMENT data
			PO30_ValidateJobsWithMissingDEPARTMENTdataInJobMapping.extractedJobName.remove();
			PO30_ValidateJobsWithMissingDEPARTMENTdataInJobMapping.forwardScenarioFoundProfile.remove();
			PO30_ValidateJobsWithMissingDEPARTMENTdataInJobMapping.forwardScenarioJobName.remove();
			PO30_ValidateJobsWithMissingDEPARTMENTdataInJobMapping.forwardScenarioJobCode.remove();
			cleanedCount += 4;

			// PO31 - Missing FUNCTION data
			PO31_ValidateJobsWithMissingFUNCTIONdataInJobMapping.extractedJobName.remove();
			PO31_ValidateJobsWithMissingFUNCTIONdataInJobMapping.forwardScenarioFoundProfile.remove();
			PO31_ValidateJobsWithMissingFUNCTIONdataInJobMapping.forwardScenarioJobName.remove();
			PO31_ValidateJobsWithMissingFUNCTIONdataInJobMapping.forwardScenarioJobCode.remove();
			cleanedCount += 4;

			// PO32 - Missing SUBFUNCTION data
			PO32_ValidateJobsWithMissingSUBFUNCTIONdataInJobMapping.extractedJobName.remove();
			PO32_ValidateJobsWithMissingSUBFUNCTIONdataInJobMapping.forwardScenarioFoundProfile.remove();
			PO32_ValidateJobsWithMissingSUBFUNCTIONdataInJobMapping.forwardScenarioJobName.remove();
			PO32_ValidateJobsWithMissingSUBFUNCTIONdataInJobMapping.forwardScenarioJobCode.remove();
			cleanedCount += 4;

			// === SELECT ALL FUNCTIONALITY ThreadLocals (PO35-PO39) ===

			// PO35 - Select All with Search (PM)
			PO35_ValidateSelectAllWithSearchFunctionality_PM.searchResultsCount.remove();
			PO35_ValidateSelectAllWithSearchFunctionality_PM.alternativeSearchSubstring.remove();
			PO35_ValidateSelectAllWithSearchFunctionality_PM.totalSecondSearchResults.remove();
			cleanedCount += 3;

			// PO36 - Select All with Filters (PM)
			PO36_ValidateSelectAllWithFiltersFunctionality_PM.filterResultsCount.remove();
			PO36_ValidateSelectAllWithFiltersFunctionality_PM.firstFilterType.remove();
			PO36_ValidateSelectAllWithFiltersFunctionality_PM.firstFilterValue.remove();
			PO36_ValidateSelectAllWithFiltersFunctionality_PM.secondFilterType.remove();
			PO36_ValidateSelectAllWithFiltersFunctionality_PM.secondFilterValue.remove();
			PO36_ValidateSelectAllWithFiltersFunctionality_PM.totalSecondFilterResults.remove();
			cleanedCount += 6;

			// PO38 - Select All with Search (JAM)
			PO38_ValidateSelectAllWithSearchFunctionality_JAM.searchResultsCount.remove();
			PO38_ValidateSelectAllWithSearchFunctionality_JAM.alternativeSearchSubstring.remove();
			PO38_ValidateSelectAllWithSearchFunctionality_JAM.totalSecondSearchResults.remove();
			cleanedCount += 3;

			// PO39 - Select All with Filters (JAM)
			PO39_ValidateSelectAllWithFiltersFunctionality_JAM.filterResultsCount.remove();
			PO39_ValidateSelectAllWithFiltersFunctionality_JAM.firstFilterType.remove();
			PO39_ValidateSelectAllWithFiltersFunctionality_JAM.firstFilterValue.remove();
			PO39_ValidateSelectAllWithFiltersFunctionality_JAM.secondFilterType.remove();
			PO39_ValidateSelectAllWithFiltersFunctionality_JAM.secondFilterValue.remove();
			PO39_ValidateSelectAllWithFiltersFunctionality_JAM.totalSecondFilterResults.remove();
			cleanedCount += 6;

			// === PUBLISH & SELECTION ThreadLocals (PO40-PO46) ===

			// PO40 - Select and Publish All Profiles (JAM)
			PO40_ValidateSelectAndPublishAllJobProfilesinJAM.unpublishedProfilesCountBefore.remove();
			PO40_ValidateSelectAndPublishAllJobProfilesinJAM.publishedProfilesCountBefore.remove();
			PO40_ValidateSelectAndPublishAllJobProfilesinJAM.unpublishedProfilesCountAfter.remove();
			PO40_ValidateSelectAndPublishAllJobProfilesinJAM.publishedProfilesCountAfter.remove();
			PO40_ValidateSelectAndPublishAllJobProfilesinJAM.totalPublishedCount.remove();
			PO40_ValidateSelectAndPublishAllJobProfilesinJAM.expectedTotalMinutes.remove();
			PO40_ValidateSelectAndPublishAllJobProfilesinJAM.profilesToBePublished.remove();
			PO40_ValidateSelectAndPublishAllJobProfilesinJAM.selectedProfilesCount.remove();
			cleanedCount += 8;

			// PO42 - Clear Profile Selection with Header Checkbox (PM)
			PO42_ClearProfileSelectionwithHeaderCheckbox_PM.loadedProfilesBeforeUncheck.remove();
			PO42_ClearProfileSelectionwithHeaderCheckbox_PM.selectedProfilesBeforeUncheck.remove();
			cleanedCount += 2;

			// PO44 - Clear Profile Selection with Header Checkbox (JAM)
			PO44_ClearProfileSelectionwithHeaderCheckbox_JAM.loadedProfilesBeforeUncheck.remove();
			PO44_ClearProfileSelectionwithHeaderCheckbox_JAM.selectedProfilesBeforeUncheck.remove();
			cleanedCount += 2;

			// PO46 - Selection of Unmapped Jobs (JAM)
			PO46_ValidateSelectionOfUnmappedJobs_JAM.skipScenario.remove();
			cleanedCount++;

			// === HCM & PROFILE MANAGER ThreadLocals (PO22-PO26) ===

			// PO22 - HCM Sync Profiles Screen (PM)
			PO22_ValidateHCMSyncProfilesScreen_PM.jobProfileName.remove();
			PO22_ValidateHCMSyncProfilesScreen_PM.intialResultsCount.remove();
			PO22_ValidateHCMSyncProfilesScreen_PM.updatedResultsCount.remove();
			PO22_ValidateHCMSyncProfilesScreen_PM.orgJobNameInRow1.remove();
			PO22_ValidateHCMSyncProfilesScreen_PM.profilesCount.remove();
			PO22_ValidateHCMSyncProfilesScreen_PM.isProfilesCountComplete.remove();
			cleanedCount += 6;

			// PO23 - Profiles with No Job Code (PM)
			PO23_VerifyProfileswithNoJobCode_PM.rowNumber.remove();
			PO23_VerifyProfileswithNoJobCode_PM.SPJobName.remove();
			PO23_VerifyProfileswithNoJobCode_PM.noJobCode.remove();
			cleanedCount += 3;

			// PO25 - Export Status Functionality (PM)
			PO25_ValidateExportStatusFunctionality_PM.rowNumber.remove();
			PO25_ValidateExportStatusFunctionality_PM.SPJobName.remove();
			cleanedCount += 2;

			// PO26 - Jobs Missing Data Tip Message
			PO26_VerifyJobsMissingDataTipMessage.initialJobCount.remove();
			cleanedCount++;

			LOGGER.info("✅ ThreadLocal cleanup completed - {} variables cleaned", cleanedCount);

		} catch (Exception e) {
			// Don't fail the suite if cleanup has issues - just log warning
			LOGGER.warn("ThreadLocal cleanup encountered an issue: {}", e.getMessage());
		}
	}
}
