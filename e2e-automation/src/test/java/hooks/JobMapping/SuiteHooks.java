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

			// === MISSING DATA FEATURES ThreadLocals (PO29) ===
			PO29_ValidateMissingDataFunctionality.extractedJobName.remove();
			PO29_ValidateMissingDataFunctionality.extractedJobCode.remove();
			PO29_ValidateMissingDataFunctionality.forwardScenarioFoundProfile.remove();
			PO29_ValidateMissingDataFunctionality.forwardScenarioJobName.remove();
			PO29_ValidateMissingDataFunctionality.forwardScenarioJobCode.remove();
			PO29_ValidateMissingDataFunctionality.currentDataType.remove();
			PO29_ValidateMissingDataFunctionality.jobDetailsFromJobMappingPage.remove();
			PO29_ValidateMissingDataFunctionality.jobDetailsFromMissingDataScreen.remove();
			cleanedCount += 8;

			// === SELECT ALL FUNCTIONALITY ThreadLocals (PO35-PO39) ===

			// PO35 - Select All with Search (Consolidated PM/JAM)
			PO35_SelectAllWithSearchFunctionality.searchResultsCount.remove();
			PO35_SelectAllWithSearchFunctionality.alternativeSearchSubstring.remove();
			PO35_SelectAllWithSearchFunctionality.totalSecondSearchResults.remove();
			PO35_SelectAllWithSearchFunctionality.currentScreen.remove();
			PO35_SelectAllWithSearchFunctionality.loadedProfilesBeforeScroll.remove();
			cleanedCount += 5;

			// PO36 - Select All with Filters (Consolidated PM/JAM)
			PO36_SelectAllWithFiltersFunctionality.filterResultsCount.remove();
			PO36_SelectAllWithFiltersFunctionality.firstFilterType.remove();
			PO36_SelectAllWithFiltersFunctionality.firstFilterValue.remove();
			PO36_SelectAllWithFiltersFunctionality.secondFilterType.remove();
			PO36_SelectAllWithFiltersFunctionality.secondFilterValue.remove();
			PO36_SelectAllWithFiltersFunctionality.totalSecondFilterResults.remove();
			PO36_SelectAllWithFiltersFunctionality.currentScreen.remove();
			cleanedCount += 7;

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

			// PO42 - Clear Profile Selection Functionality (Consolidated PM/JAM)
			PO42_ClearProfileSelectionFunctionality.loadedProfilesBeforeUncheck.remove();
			PO42_ClearProfileSelectionFunctionality.selectedProfilesBeforeUncheck.remove();
			PO42_ClearProfileSelectionFunctionality.currentScreen.remove();
			cleanedCount += 3;

			// PO46 - Selection of Unmapped Jobs (JAM)
			PO46_VerifyUnmappedJobs_JAM.skipScenario.remove();
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
