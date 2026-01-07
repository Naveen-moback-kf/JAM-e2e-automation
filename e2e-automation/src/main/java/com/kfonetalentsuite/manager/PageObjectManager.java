package com.kfonetalentsuite.manager;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kfonetalentsuite.pageobjects.JobMapping.*;

/**
 * Thread-safe singleton manager for Page Objects.
 * Uses ThreadLocal to ensure each test thread gets its own isolated instance.
 */
public class PageObjectManager {
	
	private static final Logger LOGGER = LogManager.getLogger(PageObjectManager.class);
	private static ThreadLocal<PageObjectManager> instance = new ThreadLocal<>();
	
	public static PageObjectManager getInstance() {
		if (instance.get() == null) {
			instance.set(new PageObjectManager());
			LOGGER.debug("Created new PageObjectManager instance for thread: {}", 
					Thread.currentThread().getName());
		}
		return instance.get();
	}
	
	public static void reset() {
		if (instance.get() != null) {
			instance.get().resetPageObjects();
			instance.remove();
			LOGGER.debug("Reset PageObjectManager for thread: {}", 
					Thread.currentThread().getName());
		}
	}

	public PO01_KFoneLogin kfoneLogin;
	public PO02_AddMoreJobsFunctionality addMoreJobsFunctionality;
	public PO03_JobMappingHeaderSection jobMappingHeaderSection;
	public PO04_JobMappingPageComponents jobMappingPageComponents;
	public PO05_PublishJobProfile publishJobProfile;
	public PO06_PublishSelectedProfiles publishSelectedProfiles;
	public PO07_Screen1SearchResults screen1SearchResults;
	public PO08_JobMappingFilters jobMappingFilters;
	public PO09_FilterPersistence filterPersistence;
	public PO10_CustomSPinJobComparison customSPinJobComparison;
	public PO11_ProfileLevelFunctionality profileLevelFunctionality;
	public PO12_RecommendedProfileDetails recommendedProfileDetails;
	public PO13_PCRestrictedTipMessage pcRestrictedTipMessage;
	public PO14_SortingFunctionality_JAM sortingFunctionality_JAM;
	public PO16_ManualMappingofSP manualMappingofSP;
	public PO17_MapDifferentSPtoProfile mapDifferentSPtoProfile;
	public PO18_HCMSyncProfilesTab_PM hcmSyncProfilesTab_PM;
	public PO19_ProfileswithNoJobCode_PM profileswithNoJobCode_PM;
	public PO20_PublishCenter_PM publishCenter_PM;
	public PO21_ExportStatusFunctionality_PM exportStatusFunctionality_PM;
	public PO22_MissingDataTipMessage missingDataTipMessage;
	public PO23_InfoMessageMissingDataProfiles infoMessageMissingDataProfiles;
	public PO24_InfoMessageManualMappingProfiles infoMessageManualMappingProfiles;
	public PO25_MissingDataFunctionality missingDataFunctionality;
	public PO26_SelectAndSyncProfiles_PM selectAndSyncProfiles_PM;
	public PO27_SelectAllWithSearchFunctionality selectAllWithSearchFunctionality;
	public PO28_SelectAllWithFiltersFunctionality selectAllWithFiltersFunctionality;
	public PO29_SelectAndPublishLoadedProfiles_JAM selectAndPublishLoadedProfiles_JAM;
	public PO30_SelectAndPublishAllJobProfiles_JAM selectAndPublishAllJobProfiles_JAM;
	public PO31_ApplicationPerformance_JAM_and_HCM applicationPerformance_JAM_and_HCM;
	public PO32_ClearProfileSelectionFunctionality clearProfileSelectionFunctionality;
	public PO33_UnmappedJobs_JAM unmappedJobs_JAM;
	public PO34_SortingFunctionalityInHCMScreen_PM sortingFunctionalityInHCMScreen_PM;
	public PO35_ReuploadMissingDataProfiles reuploadMissingDataProfiles;
	public PO36_DeleteJobProfiles deleteJobProfiles;

	public PO01_KFoneLogin getKFoneLogin() throws IOException {
		if (kfoneLogin == null) {
			kfoneLogin = new PO01_KFoneLogin();
		}
		return kfoneLogin;
	}

	public PO02_AddMoreJobsFunctionality getAddMoreJobsFunctionality() throws IOException {
		if (addMoreJobsFunctionality == null) {
			addMoreJobsFunctionality = new PO02_AddMoreJobsFunctionality();
		}
		return addMoreJobsFunctionality;
	}

	public PO03_JobMappingHeaderSection getJobMappingHeaderSection() throws IOException {
		if (jobMappingHeaderSection == null) {
			jobMappingHeaderSection = new PO03_JobMappingHeaderSection();
		}
		return jobMappingHeaderSection;
	}

	public PO04_JobMappingPageComponents getJobMappingPageComponents() throws IOException {
		if (jobMappingPageComponents == null) {
			jobMappingPageComponents = new PO04_JobMappingPageComponents();
		}
		return jobMappingPageComponents;
	}

	public PO05_PublishJobProfile getPublishJobProfile() throws IOException {
		if (publishJobProfile == null) {
			publishJobProfile = new PO05_PublishJobProfile();
		}
		return publishJobProfile;
	}

	public PO06_PublishSelectedProfiles getPublishSelectedProfiles() throws IOException {
		if (publishSelectedProfiles == null) {
			publishSelectedProfiles = new PO06_PublishSelectedProfiles();
		}
		return publishSelectedProfiles;
	}

	public PO07_Screen1SearchResults getScreen1SearchResults() throws IOException {
		if (screen1SearchResults == null) {
			screen1SearchResults = new PO07_Screen1SearchResults();
		}
		return screen1SearchResults;
	}

	public PO08_JobMappingFilters getJobMappingFilters() throws IOException {
		if (jobMappingFilters == null) {
			jobMappingFilters = new PO08_JobMappingFilters();
		}
		return jobMappingFilters;
	}

	public PO09_FilterPersistence getFilterPersistence() throws IOException {
		if (filterPersistence == null) {
			filterPersistence = new PO09_FilterPersistence();
		}
		return filterPersistence;
	}

	public PO10_CustomSPinJobComparison getCustomSPinJobComparison() throws IOException {
		if (customSPinJobComparison == null) {
			customSPinJobComparison = new PO10_CustomSPinJobComparison();
		}
		return customSPinJobComparison;
	}

	public PO11_ProfileLevelFunctionality getProfileLevelFunctionality() throws IOException {
		if (profileLevelFunctionality == null) {
			profileLevelFunctionality = new PO11_ProfileLevelFunctionality();
		}
		return profileLevelFunctionality;
	}

	public PO12_RecommendedProfileDetails getRecommendedProfileDetails() throws IOException {
		if (recommendedProfileDetails == null) {
			recommendedProfileDetails = new PO12_RecommendedProfileDetails();
		}
		return recommendedProfileDetails;
	}

	public PO13_PCRestrictedTipMessage getPCRestrictedTipMessage() throws IOException {
		if (pcRestrictedTipMessage == null) {
			pcRestrictedTipMessage = new PO13_PCRestrictedTipMessage();
		}
		return pcRestrictedTipMessage;
	}

	public PO14_SortingFunctionality_JAM getSortingFunctionality_JAM() throws IOException {
		if (sortingFunctionality_JAM == null) {
			sortingFunctionality_JAM = new PO14_SortingFunctionality_JAM();
		}
		return sortingFunctionality_JAM;
	}

	public PO16_ManualMappingofSP getManualMappingofSP() throws IOException {
		if (manualMappingofSP == null) {
			manualMappingofSP = new PO16_ManualMappingofSP();
		}
		return manualMappingofSP;
	}

	public PO17_MapDifferentSPtoProfile getMapDifferentSPtoProfile() throws IOException {
		if (mapDifferentSPtoProfile == null) {
			mapDifferentSPtoProfile = new PO17_MapDifferentSPtoProfile();
		}
		return mapDifferentSPtoProfile;
	}

	public PO18_HCMSyncProfilesTab_PM getHCMSyncProfilesTab_PM() throws IOException {
		if (hcmSyncProfilesTab_PM == null) {
			hcmSyncProfilesTab_PM = new PO18_HCMSyncProfilesTab_PM();
		}
		return hcmSyncProfilesTab_PM;
	}

	public PO19_ProfileswithNoJobCode_PM getProfileswithNoJobCode_PM() throws IOException {
		if (profileswithNoJobCode_PM == null) {
			profileswithNoJobCode_PM = new PO19_ProfileswithNoJobCode_PM();
		}
		return profileswithNoJobCode_PM;
	}

	public PO20_PublishCenter_PM getPublishCenter_PM() throws IOException {
		if (publishCenter_PM == null) {
			publishCenter_PM = new PO20_PublishCenter_PM();
		}
		return publishCenter_PM;
	}

	public PO21_ExportStatusFunctionality_PM getExportStatusFunctionality_PM() throws IOException {
		if (exportStatusFunctionality_PM == null) {
			exportStatusFunctionality_PM = new PO21_ExportStatusFunctionality_PM();
		}
		return exportStatusFunctionality_PM;
	}

	public PO22_MissingDataTipMessage getMissingDataTipMessage() throws IOException {
		if (missingDataTipMessage == null) {
			missingDataTipMessage = new PO22_MissingDataTipMessage();
		}
		return missingDataTipMessage;
	}

	public PO23_InfoMessageMissingDataProfiles getInfoMessageMissingDataProfiles() throws IOException {
		if (infoMessageMissingDataProfiles == null) {
			infoMessageMissingDataProfiles = new PO23_InfoMessageMissingDataProfiles();
		}
		return infoMessageMissingDataProfiles;
	}

	public PO24_InfoMessageManualMappingProfiles getInfoMessageManualMappingProfiles() throws IOException {
		if (infoMessageManualMappingProfiles == null) {
			infoMessageManualMappingProfiles = new PO24_InfoMessageManualMappingProfiles();
		}
		return infoMessageManualMappingProfiles;
	}

	public PO25_MissingDataFunctionality getMissingDataFunctionality() throws IOException {
		if (missingDataFunctionality == null) {
			missingDataFunctionality = new PO25_MissingDataFunctionality();
		}
		return missingDataFunctionality;
	}

	public PO26_SelectAndSyncProfiles_PM getSelectAndSyncProfiles_PM() throws IOException {
		if (selectAndSyncProfiles_PM == null) {
			selectAndSyncProfiles_PM = new PO26_SelectAndSyncProfiles_PM();
		}
		return selectAndSyncProfiles_PM;
	}

	public PO27_SelectAllWithSearchFunctionality getSelectAllWithSearchFunctionality() throws IOException {
		if (selectAllWithSearchFunctionality == null) {
			selectAllWithSearchFunctionality = new PO27_SelectAllWithSearchFunctionality();
		}
		return selectAllWithSearchFunctionality;
	}

	public PO28_SelectAllWithFiltersFunctionality getSelectAllWithFiltersFunctionality() throws IOException {
		if (selectAllWithFiltersFunctionality == null) {
			selectAllWithFiltersFunctionality = new PO28_SelectAllWithFiltersFunctionality();
		}
		return selectAllWithFiltersFunctionality;
	}

	public PO29_SelectAndPublishLoadedProfiles_JAM getSelectAndPublishLoadedProfiles_JAM() throws IOException {
		if (selectAndPublishLoadedProfiles_JAM == null) {
			selectAndPublishLoadedProfiles_JAM = new PO29_SelectAndPublishLoadedProfiles_JAM();
		}
		return selectAndPublishLoadedProfiles_JAM;
	}

	public PO30_SelectAndPublishAllJobProfiles_JAM getSelectAndPublishAllJobProfiles_JAM() throws IOException {
		if (selectAndPublishAllJobProfiles_JAM == null) {
			selectAndPublishAllJobProfiles_JAM = new PO30_SelectAndPublishAllJobProfiles_JAM();
		}
		return selectAndPublishAllJobProfiles_JAM;
	}

	public PO31_ApplicationPerformance_JAM_and_HCM getApplicationPerformance_JAM_and_HCM() throws IOException {
		if (applicationPerformance_JAM_and_HCM == null) {
			applicationPerformance_JAM_and_HCM = new PO31_ApplicationPerformance_JAM_and_HCM();
		}
		return applicationPerformance_JAM_and_HCM;
	}

	public PO32_ClearProfileSelectionFunctionality getClearProfileSelectionFunctionality() throws IOException {
		if (clearProfileSelectionFunctionality == null) {
			clearProfileSelectionFunctionality = new PO32_ClearProfileSelectionFunctionality();
		}
		return clearProfileSelectionFunctionality;
	}

	public PO33_UnmappedJobs_JAM getUnmappedJobs_JAM() throws IOException {
		if (unmappedJobs_JAM == null) {
			unmappedJobs_JAM = new PO33_UnmappedJobs_JAM();
		}
		return unmappedJobs_JAM;
	}

	public PO34_SortingFunctionalityInHCMScreen_PM getSortingFunctionalityInHCMScreen_PM() throws IOException {
		if (sortingFunctionalityInHCMScreen_PM == null) {
			sortingFunctionalityInHCMScreen_PM = new PO34_SortingFunctionalityInHCMScreen_PM();
		}
		return sortingFunctionalityInHCMScreen_PM;
	}

	public PO35_ReuploadMissingDataProfiles getReuploadMissingDataProfiles() throws IOException {
		if (reuploadMissingDataProfiles == null) {
			reuploadMissingDataProfiles = new PO35_ReuploadMissingDataProfiles();
		}
		return reuploadMissingDataProfiles;
	}
	
	public PO36_DeleteJobProfiles getDeleteJobProfiles() throws IOException {
		if(deleteJobProfiles == null) {
			deleteJobProfiles = new PO36_DeleteJobProfiles();
		}
		return deleteJobProfiles;
	}

	public void resetPageObjects() {
		kfoneLogin = null;
		addMoreJobsFunctionality = null;
		jobMappingHeaderSection = null;
		jobMappingPageComponents = null;
		publishJobProfile = null;
		publishSelectedProfiles = null;
		screen1SearchResults = null;
		jobMappingFilters = null;
		filterPersistence = null;
		customSPinJobComparison = null;
		profileLevelFunctionality = null;
		recommendedProfileDetails = null;
		pcRestrictedTipMessage = null;
		sortingFunctionality_JAM = null;
		manualMappingofSP = null;
		mapDifferentSPtoProfile = null;
		hcmSyncProfilesTab_PM = null;
		profileswithNoJobCode_PM = null;
		publishCenter_PM = null;
		exportStatusFunctionality_PM = null;
		missingDataTipMessage = null;
		infoMessageMissingDataProfiles = null;
		infoMessageManualMappingProfiles = null;
		missingDataFunctionality = null;
		selectAndSyncProfiles_PM = null;
		selectAllWithSearchFunctionality = null;
		selectAllWithFiltersFunctionality = null;
		selectAndPublishLoadedProfiles_JAM = null;
		selectAndPublishAllJobProfiles_JAM = null;
		applicationPerformance_JAM_and_HCM = null;
		clearProfileSelectionFunctionality = null;
		unmappedJobs_JAM = null;
		sortingFunctionalityInHCMScreen_PM = null;
		reuploadMissingDataProfiles = null;
		deleteJobProfiles = null;
	}

}
