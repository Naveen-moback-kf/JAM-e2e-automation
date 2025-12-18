package com.kfonetalentsuite.manager;

import java.io.IOException;

import com.kfonetalentsuite.pageobjects.JobMapping.*;

public class PageObjectManager {

	public PO01_KFoneLogin kfoneLogin;
	public PO02_ValidateAddMoreJobsFunctionality validateAddMoreJobsFunctionality;
	public PO03_ValidateJobmappingHeaderSection validateJobmappingHeaderSection;
	public PO04_VerifyJobMappingPageComponents verifyJobMappingPageComponents;
	public PO05_ValidateJobProfileDetailsPopup validateJobProfileDetailsPopup;
	public PO06_PublishJobProfile publishJobProfile;
	public PO07_PublishJobFromComparisonScreen publishJobFromComparisonScreen;
	public PO08_PublishJobFromDetailsPopup publishJobFromDetailsPopup;
	public PO09_PublishSelectedProfiles publishSelectedProfiles;
	public PO10_ValidateScreen1SearchResults validateScreen1SearchResults;
	public PO11_ValidateJobMappingFiltersFunctionality validateJobMappingFiltersFunctionality;
	public PO12_ValidatePersistanceOfFilters validatePersistanceOfFilters;
	public PO13_AddandVerifyCustomSPinJobComparisonPage addandVerifyCustomSPinJobComparisonPage;
	public PO14_ValidateProfileLevelFunctionality validateProfileLevelFunctionality;
	public PO15_ValidateRecommendedProfileDetails validateRecommendedProfileDetails;
	public PO16_ValidatePCRestrictedTipMessage validatePCRestrictedTipMessage;
	public PO17_ValidateSortingFunctionality_JAM validateSortingFunctionality_JAM;
	public PO20_ManualMappingofSPinAutoAI manualMappingofSPinAutoAI;
	public PO21_MapDifferentSPtoProfileInAutoAI mapDifferentSPtoProfileInAutoAI;
	public PO22_ValidateHCMSyncProfilesScreen_PM validateHCMSyncProfilesTab_PM;
	public PO23_VerifyProfileswithNoJobCode_PM verifyProfileswithNoJobCode_PM;
	public PO24_ValidatePublishCenter_PM validatePublishCenter_PM;
	public PO25_ValidateExportStatusFunctionality_PM validateExportStatusFunctionality_PM;
	public PO26_VerifyJobsMissingDataTipMessage verifyJobsMissingDataTipMessage;
	public PO27_VerifyInfoMessageForMissingDataProfiles verifyInfoMessageForMissingDataProfiles;
	public PO28_VerifyInfoMessageForManualMappingProfiles verifyInfoMessageForManualMappingProfiles;
	public PO33_ValidateSelectAndHCMSyncLoadedProfiles_PM validateSelectAndHCMSyncLoadedProfiles_PM;
	public PO34_ValidateSelectAndSyncAllProfiles_PM validateSelectAndSyncAllProfiles_PM;
	public PO36_ValidateSelectAllWithFiltersFunctionality_PM validateSelectAllWithFiltersFunctionality_PM;
	public PO37_ValidateSelectAndPublishLoadedProfiles_JAM validateSelectAndPublishLoadedProfiles_JAM;
	public PO39_ValidateSelectAllWithFiltersFunctionality_JAM validateSelectAllWithFiltersFunctionality_JAM;
	public PO40_ValidateSelectAndPublishAllJobProfilesinJAM validateSelectAndPublishAllJobProfilesinJAM;
	public PO41_ValidateApplicationPerformance_JAM_and_HCM validateApplicationPerformance_JAM_and_HCM;
	public PO42_ClearProfileSelectionFunctionality clearProfileSelectionFunctionality;
	public PO35_SelectAllWithSearchFunctionality selectAllWithSearchFunctionality;
	public PO46_ValidateSelectionOfUnmappedJobs_JAM validateSelectionOfUnmappedJobs_JAM;
	public PO47_ValidateSortingFunctionalityInHCMScreen_PM validateSortingFunctionalityInHCMScreen_PM;
	public PO48_ValidateReuploadMissingDataProfiles validateReuploadMissingDataProfiles;
	public PO29_ValidateMissingDataFunctionality validateMissingDataFunctionality;

	public PO01_KFoneLogin getKFoneLogin() throws IOException {
		if (kfoneLogin == null) {
			kfoneLogin = new PO01_KFoneLogin();
		}
		return kfoneLogin;
	}

	public PO02_ValidateAddMoreJobsFunctionality getValidateAddMoreJobsFunctionality() throws IOException {
		if (validateAddMoreJobsFunctionality == null) {
			validateAddMoreJobsFunctionality = new PO02_ValidateAddMoreJobsFunctionality();
		}
		return validateAddMoreJobsFunctionality;
	}

	public PO03_ValidateJobmappingHeaderSection getValidateJobmappingHeaderSection() throws IOException {
		if (validateJobmappingHeaderSection == null) {
			validateJobmappingHeaderSection = new PO03_ValidateJobmappingHeaderSection();
		}
		return validateJobmappingHeaderSection;
	}

	public PO04_VerifyJobMappingPageComponents getVerifyJobMappingPageComponents() throws IOException {
		if (verifyJobMappingPageComponents == null) {
			verifyJobMappingPageComponents = new PO04_VerifyJobMappingPageComponents();
		}
		return verifyJobMappingPageComponents;

	}

	public PO05_ValidateJobProfileDetailsPopup getValidateJobProfileDetailsPopup() throws IOException {
		if (validateJobProfileDetailsPopup == null) {
			validateJobProfileDetailsPopup = new PO05_ValidateJobProfileDetailsPopup();
		}
		return validateJobProfileDetailsPopup;
	}

	public PO06_PublishJobProfile getPublishJobProfile() throws IOException {
		if (publishJobProfile == null) {
			publishJobProfile = new PO06_PublishJobProfile();
		}
		return publishJobProfile;
	}

	public PO07_PublishJobFromComparisonScreen getPublishJobFromComparisonScreen() throws IOException {
		if (publishJobFromComparisonScreen == null) {
			publishJobFromComparisonScreen = new PO07_PublishJobFromComparisonScreen();
		}
		return publishJobFromComparisonScreen;
	}

	public PO08_PublishJobFromDetailsPopup getPublishJobFromDetailsPopup() throws IOException {
		if (publishJobFromDetailsPopup == null) {
			publishJobFromDetailsPopup = new PO08_PublishJobFromDetailsPopup();
		}
		return publishJobFromDetailsPopup;
	}

	public PO09_PublishSelectedProfiles getPublishSelectedProfiles() throws IOException {
		if (publishSelectedProfiles == null) {
			publishSelectedProfiles = new PO09_PublishSelectedProfiles();
		}
		return publishSelectedProfiles;
	}

	public PO10_ValidateScreen1SearchResults getValidateScreen1SearchResults() throws IOException {
		if (validateScreen1SearchResults == null) {
			validateScreen1SearchResults = new PO10_ValidateScreen1SearchResults();
		}
		return validateScreen1SearchResults;
	}

	public PO11_ValidateJobMappingFiltersFunctionality getValidateJobMappingFiltersFunctionality() throws IOException {
		if (validateJobMappingFiltersFunctionality == null) {
			validateJobMappingFiltersFunctionality = new PO11_ValidateJobMappingFiltersFunctionality();
		}
		return validateJobMappingFiltersFunctionality;
	}

	public PO12_ValidatePersistanceOfFilters getValidatePersistanceOfFilters() throws IOException {
		if (validatePersistanceOfFilters == null) {
			validatePersistanceOfFilters = new PO12_ValidatePersistanceOfFilters();
		}
		return validatePersistanceOfFilters;
	}

	public PO13_AddandVerifyCustomSPinJobComparisonPage getAddandVerifyCustomSPinJobComparisonPage()
			throws IOException {
		if (addandVerifyCustomSPinJobComparisonPage == null) {
			addandVerifyCustomSPinJobComparisonPage = new PO13_AddandVerifyCustomSPinJobComparisonPage();
		}
		return addandVerifyCustomSPinJobComparisonPage;
	}

	public PO14_ValidateProfileLevelFunctionality getValidateProfileLevelFunctionality() throws IOException {
		if (validateProfileLevelFunctionality == null) {
			validateProfileLevelFunctionality = new PO14_ValidateProfileLevelFunctionality();
		}
		return validateProfileLevelFunctionality;
	}

	public PO15_ValidateRecommendedProfileDetails getValidateRecommendedProfileDetails() throws IOException {
		if (validateRecommendedProfileDetails == null) {
			validateRecommendedProfileDetails = new PO15_ValidateRecommendedProfileDetails();
		}
		return validateRecommendedProfileDetails;
	}

	public PO16_ValidatePCRestrictedTipMessage getValidatePCRestrictedTipMessage() throws IOException {
		if (validatePCRestrictedTipMessage == null) {
			validatePCRestrictedTipMessage = new PO16_ValidatePCRestrictedTipMessage();
		}
		return validatePCRestrictedTipMessage;
	}

	public PO17_ValidateSortingFunctionality_JAM getValidateSortingFunctionality_JAM() throws IOException {
		if (validateSortingFunctionality_JAM == null) {
			validateSortingFunctionality_JAM = new PO17_ValidateSortingFunctionality_JAM();
		}
		return validateSortingFunctionality_JAM;
	}

	public PO20_ManualMappingofSPinAutoAI getManualMappingofSPinAutoAI() throws IOException {
		if (manualMappingofSPinAutoAI == null) {
			manualMappingofSPinAutoAI = new PO20_ManualMappingofSPinAutoAI();
		}
		return manualMappingofSPinAutoAI;
	}

	public PO21_MapDifferentSPtoProfileInAutoAI getMapDifferentSPtoProfileInAutoAI() throws IOException {
		if (mapDifferentSPtoProfileInAutoAI == null) {
			mapDifferentSPtoProfileInAutoAI = new PO21_MapDifferentSPtoProfileInAutoAI();
		}
		return mapDifferentSPtoProfileInAutoAI;
	}

	public PO22_ValidateHCMSyncProfilesScreen_PM getValidateHCMSyncProfilesTab_PM() throws IOException {
		if (validateHCMSyncProfilesTab_PM == null) {
			validateHCMSyncProfilesTab_PM = new PO22_ValidateHCMSyncProfilesScreen_PM();
		}
		return validateHCMSyncProfilesTab_PM;
	}

	public PO23_VerifyProfileswithNoJobCode_PM getVerifyProfileswithNoJobCode_PM() throws IOException {
		if (verifyProfileswithNoJobCode_PM == null) {
			verifyProfileswithNoJobCode_PM = new PO23_VerifyProfileswithNoJobCode_PM();
		}
		return verifyProfileswithNoJobCode_PM;
	}

	public PO24_ValidatePublishCenter_PM getValidatePublishCenter_PM() throws IOException {
		if (validatePublishCenter_PM == null) {
			validatePublishCenter_PM = new PO24_ValidatePublishCenter_PM();
		}
		return validatePublishCenter_PM;
	}

	public PO25_ValidateExportStatusFunctionality_PM getValidateExportStatusFunctionality_PM() throws IOException {
		if (validateExportStatusFunctionality_PM == null) {
			validateExportStatusFunctionality_PM = new PO25_ValidateExportStatusFunctionality_PM();
		}
		return validateExportStatusFunctionality_PM;
	}

	public PO26_VerifyJobsMissingDataTipMessage getVerifyJobsMissingDataTipMessage() throws IOException {
		if (verifyJobsMissingDataTipMessage == null) {
			verifyJobsMissingDataTipMessage = new PO26_VerifyJobsMissingDataTipMessage();
		}
		return verifyJobsMissingDataTipMessage;
	}

	public PO27_VerifyInfoMessageForMissingDataProfiles getVerifyInfoMessageForMissingDataProfiles()
			throws IOException {
		if (verifyInfoMessageForMissingDataProfiles == null) {
			verifyInfoMessageForMissingDataProfiles = new PO27_VerifyInfoMessageForMissingDataProfiles();
		}
		return verifyInfoMessageForMissingDataProfiles;
	}

	public PO28_VerifyInfoMessageForManualMappingProfiles getVerifyInfoMessageForManualMappingProfiles()
			throws IOException {
		if (verifyInfoMessageForManualMappingProfiles == null) {
			verifyInfoMessageForManualMappingProfiles = new PO28_VerifyInfoMessageForManualMappingProfiles();
		}
		return verifyInfoMessageForManualMappingProfiles;
	}

	public PO33_ValidateSelectAndHCMSyncLoadedProfiles_PM getValidateSelectAndHCMSyncLoadedProfiles_PM()
			throws IOException {
		if (validateSelectAndHCMSyncLoadedProfiles_PM == null) {
			validateSelectAndHCMSyncLoadedProfiles_PM = new PO33_ValidateSelectAndHCMSyncLoadedProfiles_PM();
		}
		return validateSelectAndHCMSyncLoadedProfiles_PM;
	}

	public PO34_ValidateSelectAndSyncAllProfiles_PM getValidateSelectAndSyncAllProfiles_PM() throws IOException {
		if (validateSelectAndSyncAllProfiles_PM == null) {
			validateSelectAndSyncAllProfiles_PM = new PO34_ValidateSelectAndSyncAllProfiles_PM();
		}
		return validateSelectAndSyncAllProfiles_PM;
	}

	public PO36_ValidateSelectAllWithFiltersFunctionality_PM getValidateSelectAllWithFiltersFunctionality_PM()
			throws IOException {
		if (validateSelectAllWithFiltersFunctionality_PM == null) {
			validateSelectAllWithFiltersFunctionality_PM = new PO36_ValidateSelectAllWithFiltersFunctionality_PM();
		}
		return validateSelectAllWithFiltersFunctionality_PM;
	}

	public PO37_ValidateSelectAndPublishLoadedProfiles_JAM getValidateSelectAndPublishLoadedProfiles_JAM()
			throws IOException {
		if (validateSelectAndPublishLoadedProfiles_JAM == null) {
			validateSelectAndPublishLoadedProfiles_JAM = new PO37_ValidateSelectAndPublishLoadedProfiles_JAM();
		}
		return validateSelectAndPublishLoadedProfiles_JAM;
	}

	public PO39_ValidateSelectAllWithFiltersFunctionality_JAM getValidateSelectAllWithFiltersFunctionality_JAM()
			throws IOException {
		if (validateSelectAllWithFiltersFunctionality_JAM == null) {
			validateSelectAllWithFiltersFunctionality_JAM = new PO39_ValidateSelectAllWithFiltersFunctionality_JAM();
		}
		return validateSelectAllWithFiltersFunctionality_JAM;
	}

	public PO40_ValidateSelectAndPublishAllJobProfilesinJAM getValidateSelectAndPublishAllJobProfilesinJAM()
			throws IOException {
		if (validateSelectAndPublishAllJobProfilesinJAM == null) {
			validateSelectAndPublishAllJobProfilesinJAM = new PO40_ValidateSelectAndPublishAllJobProfilesinJAM();
		}
		return validateSelectAndPublishAllJobProfilesinJAM;
	}

	public PO41_ValidateApplicationPerformance_JAM_and_HCM getValidateApplicationPerformance_JAM_and_HCM()
			throws IOException {
		if (validateApplicationPerformance_JAM_and_HCM == null) {
			validateApplicationPerformance_JAM_and_HCM = new PO41_ValidateApplicationPerformance_JAM_and_HCM();
		}
		return validateApplicationPerformance_JAM_and_HCM;
	}

	public PO42_ClearProfileSelectionFunctionality getClearProfileSelectionFunctionality() throws IOException {
		if (clearProfileSelectionFunctionality == null) {
			clearProfileSelectionFunctionality = new PO42_ClearProfileSelectionFunctionality();
		}
		return clearProfileSelectionFunctionality;
	}

	public PO35_SelectAllWithSearchFunctionality getSelectAllWithSearchFunctionality() throws IOException {
		if (selectAllWithSearchFunctionality == null) {
			selectAllWithSearchFunctionality = new PO35_SelectAllWithSearchFunctionality();
		}
		return selectAllWithSearchFunctionality;
	}

	public PO46_ValidateSelectionOfUnmappedJobs_JAM getValidateSelectionOfUnmappedJobs_JAM() throws IOException {
		if (validateSelectionOfUnmappedJobs_JAM == null) {
			validateSelectionOfUnmappedJobs_JAM = new PO46_ValidateSelectionOfUnmappedJobs_JAM();
		}
		return validateSelectionOfUnmappedJobs_JAM;
	}

	public PO47_ValidateSortingFunctionalityInHCMScreen_PM getValidateSortingFunctionalityInHCMScreen_PM()
			throws IOException {
		if (validateSortingFunctionalityInHCMScreen_PM == null) {
			validateSortingFunctionalityInHCMScreen_PM = new PO47_ValidateSortingFunctionalityInHCMScreen_PM();
		}
		return validateSortingFunctionalityInHCMScreen_PM;
	}

	public PO48_ValidateReuploadMissingDataProfiles getValidateReuploadMissingDataProfiles() throws IOException {
		// Singleton pattern: reuse existing instance to prevent multiple initializations
		if (validateReuploadMissingDataProfiles == null) {
			validateReuploadMissingDataProfiles = new PO48_ValidateReuploadMissingDataProfiles();
		}
		return validateReuploadMissingDataProfiles;
	}

	public PO29_ValidateMissingDataFunctionality getValidateMissingDataFunctionality() throws IOException {
		if (validateMissingDataFunctionality == null) {
			validateMissingDataFunctionality = new PO29_ValidateMissingDataFunctionality();
		}
		return validateMissingDataFunctionality;
	}

	/**
	 * Reset all page object instances (useful for cleanup between tests) This
	 * ensures a fresh start for each test scenario when needed
	 */
	public void resetPageObjects() {
		// Login Page Object
		kfoneLogin = null;

		// Job Mapping Core Page Objects (PO02-PO17)
		validateAddMoreJobsFunctionality = null;
		validateJobmappingHeaderSection = null;
		verifyJobMappingPageComponents = null;
		validateJobProfileDetailsPopup = null;
		publishJobProfile = null;
		publishJobFromComparisonScreen = null;
		publishJobFromDetailsPopup = null;
		publishSelectedProfiles = null;
		validateScreen1SearchResults = null;
		validateJobMappingFiltersFunctionality = null;
		validatePersistanceOfFilters = null;
		addandVerifyCustomSPinJobComparisonPage = null;
		validateProfileLevelFunctionality = null;
		validateRecommendedProfileDetails = null;
		validatePCRestrictedTipMessage = null;
		validateSortingFunctionality_JAM = null;

		// Manual Mapping Page Objects (PO20-PO21)
		manualMappingofSPinAutoAI = null;
		mapDifferentSPtoProfileInAutoAI = null;

		// Profile Manager Page Objects (PO22-PO26)
		validateHCMSyncProfilesTab_PM = null;
		verifyProfileswithNoJobCode_PM = null;
		validatePublishCenter_PM = null;
		validateExportStatusFunctionality_PM = null;
		verifyJobsMissingDataTipMessage = null;

		// Info Message Page Objects (PO27-PO28)
		verifyInfoMessageForMissingDataProfiles = null;
		verifyInfoMessageForManualMappingProfiles = null;

		// Missing Data Validation Page Objects (PO29)
		validateMissingDataFunctionality = null;

		// Profile Selection PM Page Objects (PO33-PO36)
		validateSelectAndHCMSyncLoadedProfiles_PM = null;
		validateSelectAndSyncAllProfiles_PM = null;
		validateSelectAllWithFiltersFunctionality_PM = null;

		// Profile Selection JAM Page Objects (PO37-PO40)
		validateSelectAndPublishLoadedProfiles_JAM = null;
		validateSelectAllWithFiltersFunctionality_JAM = null;
		validateSelectAndPublishAllJobProfilesinJAM = null;

		// Performance & Utility Page Objects (PO41-PO47)
		validateApplicationPerformance_JAM_and_HCM = null;
		clearProfileSelectionFunctionality = null;
		selectAllWithSearchFunctionality = null;
		validateSelectionOfUnmappedJobs_JAM = null;
		validateSortingFunctionalityInHCMScreen_PM = null;
		validateReuploadMissingDataProfiles = null;
	}

}
