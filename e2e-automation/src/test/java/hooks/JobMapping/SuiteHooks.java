package hooks.JobMapping;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ISuite;
import org.testng.ISuiteListener;

import com.kfonetalentsuite.webdriverManager.DriverManager;
import com.kfonetalentsuite.pageobjects.JobMapping.*;
import com.kfonetalentsuite.utils.common.CommonVariable;
import com.kfonetalentsuite.utils.JobMapping.SessionManager;
import com.kfonetalentsuite.utils.JobMapping.AllureReportingManager;

public class SuiteHooks implements ISuiteListener {

	protected static final Logger LOGGER = LogManager.getLogger(SuiteHooks.class);

	@Override
	public void onStart(ISuite suite) {
		LOGGER.info("==================================================");
		LOGGER.info("TEST SUITE STARTED: {}", suite.getName());
		LOGGER.info("==================================================");

		// Perform Allure daily reset check (similar to Excel reporting)
		// This will backup old reports and reset if it's a new day
		AllureReportingManager.checkAndPerformDailyReset();
		
		// Generate Allure environment information
		// This creates environment.properties with browser, OS, and environment details
		AllureReportingManager.generateEnvironmentInfo();
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

		// === MISSING DATA FEATURES ThreadLocals (PO25) ===
		PO25_MissingDataFunctionality.extractedJobName.remove();
		PO25_MissingDataFunctionality.extractedJobCode.remove();
		PO25_MissingDataFunctionality.forwardScenarioFoundProfile.remove();
		PO25_MissingDataFunctionality.forwardScenarioJobName.remove();
		PO25_MissingDataFunctionality.forwardScenarioJobCode.remove();
		PO25_MissingDataFunctionality.currentDataType.remove();
		PO25_MissingDataFunctionality.jobDetailsFromJobMappingPage.remove();
		PO25_MissingDataFunctionality.jobDetailsFromMissingDataScreen.remove();
		cleanedCount += 8;

		// === SELECT ALL FUNCTIONALITY ThreadLocals (PO27-PO28) ===

		// PO27 - Select All with Search (Consolidated PM/JAM)
		PO27_SelectAllWithSearchFunctionality.searchResultsCount.remove();
		PO27_SelectAllWithSearchFunctionality.alternativeSearchSubstring.remove();
		PO27_SelectAllWithSearchFunctionality.totalSecondSearchResults.remove();
		PO27_SelectAllWithSearchFunctionality.currentScreen.remove();
		PO27_SelectAllWithSearchFunctionality.loadedProfilesBeforeScroll.remove();
		cleanedCount += 5;

		// PO28 - Select All with Filters (Consolidated PM/JAM)
		PO28_SelectAllWithFiltersFunctionality.filterResultsCount.remove();
		PO28_SelectAllWithFiltersFunctionality.firstFilterType.remove();
		PO28_SelectAllWithFiltersFunctionality.firstFilterValue.remove();
		PO28_SelectAllWithFiltersFunctionality.secondFilterType.remove();
		PO28_SelectAllWithFiltersFunctionality.secondFilterValue.remove();
		PO28_SelectAllWithFiltersFunctionality.totalSecondFilterResults.remove();
		PO28_SelectAllWithFiltersFunctionality.currentScreen.remove();
		cleanedCount += 7;

		// === PUBLISH & SELECTION ThreadLocals (PO30-PO33) ===

		// PO30 - Select and Publish All Profiles (JAM)
		PO30_SelectAndPublishAllJobProfiles_JAM.unpublishedProfilesCountBefore.remove();
		PO30_SelectAndPublishAllJobProfiles_JAM.publishedProfilesCountBefore.remove();
		PO30_SelectAndPublishAllJobProfiles_JAM.unpublishedProfilesCountAfter.remove();
		PO30_SelectAndPublishAllJobProfiles_JAM.publishedProfilesCountAfter.remove();
		PO30_SelectAndPublishAllJobProfiles_JAM.totalPublishedCount.remove();
		PO30_SelectAndPublishAllJobProfiles_JAM.expectedTotalMinutes.remove();
		PO30_SelectAndPublishAllJobProfiles_JAM.profilesToBePublished.remove();
		PO30_SelectAndPublishAllJobProfiles_JAM.selectedProfilesCount.remove();
		cleanedCount += 8;

		// PO32 - Clear Profile Selection Functionality (Consolidated PM/JAM)
		PO32_ClearProfileSelectionFunctionality.loadedProfilesBeforeUncheck.remove();
		PO32_ClearProfileSelectionFunctionality.selectedProfilesBeforeUncheck.remove();
		PO32_ClearProfileSelectionFunctionality.currentScreen.remove();
		cleanedCount += 3;

		// PO33 - Selection of Unmapped Jobs (JAM)
		PO33_UnmappedJobs_JAM.skipScenario.remove();
		cleanedCount++;

		// === HCM & PROFILE MANAGER ThreadLocals (PO18-PO22) ===

		// PO18 - HCM Sync Profiles Screen (PM)
		PO18_HCMSyncProfilesTab_PM.jobProfileName.remove();
		PO18_HCMSyncProfilesTab_PM.intialResultsCount.remove();
		PO18_HCMSyncProfilesTab_PM.updatedResultsCount.remove();
		PO18_HCMSyncProfilesTab_PM.orgJobNameInRow1.remove();
		PO18_HCMSyncProfilesTab_PM.profilesCount.remove();
		PO18_HCMSyncProfilesTab_PM.isProfilesCountComplete.remove();
		cleanedCount += 6;

		// PO19 - Profiles with No Job Code (PM)
		PO19_ProfileswithNoJobCode_PM.rowNumber.remove();
		PO19_ProfileswithNoJobCode_PM.SPJobName.remove();
		PO19_ProfileswithNoJobCode_PM.noJobCode.remove();
		cleanedCount += 3;

		// PO21 - Export Status Functionality (PM)
		PO21_ExportStatusFunctionality_PM.rowNumber.remove();
		PO21_ExportStatusFunctionality_PM.SPJobName.remove();
		cleanedCount += 2;

		// PO22 - Jobs Missing Data Tip Message
		PO22_MissingDataTipMessage.initialJobCount.remove();
		cleanedCount++;

			LOGGER.info("✅ ThreadLocal cleanup completed - {} variables cleaned", cleanedCount);

		} catch (Exception e) {
			// Don't fail the suite if cleanup has issues - just log warning
			LOGGER.warn("ThreadLocal cleanup encountered an issue: {}", e.getMessage());
		}
	}
}
