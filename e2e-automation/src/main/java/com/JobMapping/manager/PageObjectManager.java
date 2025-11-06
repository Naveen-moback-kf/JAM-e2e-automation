package com.JobMapping.manager;

import java.io.IOException;

import com.JobMapping.pageobjects.*;

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
	public PO17_ValidateSortingFunctionality validateSortingFunctionality;
	public PO20_ManualMappingofSPinAutoAI manualMappingofSPinAutoAI;
	public PO21_MapDifferentSPtoProfileInAutoAI mapDifferentSPtoProfileInAutoAI;
	public PO22_ValidateHCMSyncProfilesScreen_PM validateHCMSyncProfilesTab_PM;
	public PO23_VerifyProfileswithNoJobCode_PM verifyProfileswithNoJobCode_PM;
	public PO24_ValidatePublishCenter_PM validatePublishCenter_PM;
	public PO25_ValidateExportStatusFunctionality_PM validateExportStatusFunctionality_PM;
	public PO26_VerifyJobsMissingDataTipMessage verifyJobsMissingDataTipMessage;
	public PO27_VerifyInfoMessageForMissingDataProfiles verifyInfoMessageForMissingDataProfiles;
	public PO28_VerifyInfoMessageForManualMappingProfiles verifyInfoMessageForManualMappingProfiles;
	public PO29_ValidateJobsWithMissingGRADEdataInJobMapping validateJobsWithMissingGRADEDataInJobMapping;
	public PO30_ValidateJobsWithMissingDEPARTMENTdataInJobMapping validateJobsWithMissingDEPARTMENTDataInJobMapping;
	public PO31_ValidateJobsWithMissingFUNCTIONdataInJobMapping validateJobsWithMissingFUNCTIONDataInJobMapping;
	public PO32_ValidateJobsWithMissingSUBFUNCTIONdataInJobMapping validateJobsWithMissingSUBFUNCTIONDataInJobMapping;
	public PO33_ValidateSelectAndHCMSyncLoadedProfiles_PM validateSelectAndHCMSyncLoadedProfiles_PM;
	public PO34_ValidateSelectAndSyncAllProfiles_PM validateSelectAndSyncAllProfiles_PM;
	public PO35_ValidateSelectAllWithSearchFunctionality_PM validateSelectAllWithSearchFunctionality_PM;
	public PO36_ValidateSelectAllWithFiltersFunctionality_PM validateSelectAllWithFiltersFunctionality_PM;
	public PO37_ValidateSelectAndPublishLoadedProfiles_JAM validateSelectAndPublishLoadedProfiles_JAM;
	public PO38_ValidateSelectAllWithSearchFunctionality_JAM validateSelectAllWithSearchFunctionality_JAM;
	public PO39_ValidateSelectAllWithFiltersFunctionality_JAM validateSelectAllWithFiltersFunctionality_JAM;
	public PO40_ValidateSelectAndPublishAllJobProfilesinJAM validateSelectAndPublishAllJobProfilesinJAM;
	public PO41_ValidateApplicationPerformance_JAM_and_HCM validateApplicationPerformance_JAM_and_HCM;
	public PO42_ClearProfileSelectionwithHeaderCheckbox_PM clearProfileSelectionwithHeaderCheckbox_PM;
	public PO43_ClearProfileSelectionwithNoneButton_PM clearProfileSelectionwithNoneButton_PM;
	public PO44_ClearProfileSelectionwithHeaderCheckbox_JAM clearProfileSelectionwithHeaderCheckbox_JAM;
	public PO45_ClearProfileSelectionwithNoneButton_JAM clearProfileSelectionwithNoneButton_JAM;
	public PO46_ValidateSelectionOfUnmappedJobs_JAM validateSelectionOfUnmappedJobs_JAM;
	
	
	public PO01_KFoneLogin getKFoneLogin() throws IOException {
		
		// Singleton pattern: reuse existing instance to prevent multiple initializations
		if (kfoneLogin == null) {
			kfoneLogin = new PO01_KFoneLogin();
		}
		return kfoneLogin;
	}
	
	public PO02_ValidateAddMoreJobsFunctionality getValidateAddMoreJobsFunctionality() throws IOException {
		
		validateAddMoreJobsFunctionality = new PO02_ValidateAddMoreJobsFunctionality();
		return validateAddMoreJobsFunctionality;
	}
	
	public PO03_ValidateJobmappingHeaderSection getValidateJobmappingHeaderSection() throws IOException {
		validateJobmappingHeaderSection = new PO03_ValidateJobmappingHeaderSection();
		return validateJobmappingHeaderSection;
	}
	
	public PO04_VerifyJobMappingPageComponents getVerifyJobMappingPageComponents() throws IOException {
		
		// Singleton pattern: reuse existing instance to prevent multiple initializations
		if (verifyJobMappingPageComponents == null) {
			verifyJobMappingPageComponents = new PO04_VerifyJobMappingPageComponents();
		}
		return verifyJobMappingPageComponents;
		
	}
	
	public PO05_ValidateJobProfileDetailsPopup getValidateJobProfileDetailsPopup() throws IOException {
		
		validateJobProfileDetailsPopup = new PO05_ValidateJobProfileDetailsPopup();
		return validateJobProfileDetailsPopup;
	}
	
	public PO06_PublishJobProfile getPublishJobProfile() throws IOException {
		
		publishJobProfile = new PO06_PublishJobProfile();
		return publishJobProfile;
	}
	
	public PO07_PublishJobFromComparisonScreen getPublishJobFromComparisonScreen() throws IOException {
		
		publishJobFromComparisonScreen = new PO07_PublishJobFromComparisonScreen();
		return publishJobFromComparisonScreen;
	}
	
	public PO08_PublishJobFromDetailsPopup getPublishJobFromDetailsPopup() throws IOException {
		
		publishJobFromDetailsPopup = new PO08_PublishJobFromDetailsPopup();
		return publishJobFromDetailsPopup;
	}
	
	public PO09_PublishSelectedProfiles getPublishSelectedProfiles() throws IOException {
		publishSelectedProfiles = new PO09_PublishSelectedProfiles();
		return publishSelectedProfiles;
	}
	
	public PO10_ValidateScreen1SearchResults getValidateScreen1SearchResults() throws IOException {
		
		validateScreen1SearchResults = new PO10_ValidateScreen1SearchResults();
		return validateScreen1SearchResults;
	}
	
	public PO11_ValidateJobMappingFiltersFunctionality getValidateJobMappingFiltersFunctionality() throws IOException {
		
		validateJobMappingFiltersFunctionality = new PO11_ValidateJobMappingFiltersFunctionality();
		return validateJobMappingFiltersFunctionality;
	}
	
	public PO12_ValidatePersistanceOfFilters getValidatePersistanceOfFilters() throws IOException {
		
		validatePersistanceOfFilters = new PO12_ValidatePersistanceOfFilters();
		return validatePersistanceOfFilters;
	}
	
	public PO13_AddandVerifyCustomSPinJobComparisonPage getAddandVerifyCustomSPinJobComparisonPage() throws IOException {
		
		addandVerifyCustomSPinJobComparisonPage = new PO13_AddandVerifyCustomSPinJobComparisonPage();
		return addandVerifyCustomSPinJobComparisonPage;
	}
	
	public PO14_ValidateProfileLevelFunctionality getValidateProfileLevelFunctionality() throws IOException {
		
		validateProfileLevelFunctionality = new PO14_ValidateProfileLevelFunctionality();
		return validateProfileLevelFunctionality;
	}
	
	public PO15_ValidateRecommendedProfileDetails getValidateRecommendedProfileDetails() throws IOException {
		validateRecommendedProfileDetails = new PO15_ValidateRecommendedProfileDetails();
		return validateRecommendedProfileDetails;
	}
	
	public PO16_ValidatePCRestrictedTipMessage getValidatePCRestrictedTipMessage() throws IOException {
		 
		validatePCRestrictedTipMessage = new PO16_ValidatePCRestrictedTipMessage();
		return validatePCRestrictedTipMessage;
	}
	
	public PO17_ValidateSortingFunctionality getValidateSortingFunctionality() throws IOException {
		
		validateSortingFunctionality = new PO17_ValidateSortingFunctionality();
		return validateSortingFunctionality;
	}
	
	public PO20_ManualMappingofSPinAutoAI getManualMappingofSPinAutoAI() throws IOException {
		
		manualMappingofSPinAutoAI = new PO20_ManualMappingofSPinAutoAI();
		return manualMappingofSPinAutoAI;
	}
	
	public PO21_MapDifferentSPtoProfileInAutoAI getMapDifferentSPtoProfileInAutoAI() throws IOException {
		
		mapDifferentSPtoProfileInAutoAI = new PO21_MapDifferentSPtoProfileInAutoAI();
		return mapDifferentSPtoProfileInAutoAI;
	}
	
	public PO22_ValidateHCMSyncProfilesScreen_PM getValidateHCMSyncProfilesTab_PM() throws IOException {
		
		validateHCMSyncProfilesTab_PM = new PO22_ValidateHCMSyncProfilesScreen_PM();
		return validateHCMSyncProfilesTab_PM;
	}
	
	public PO23_VerifyProfileswithNoJobCode_PM getVerifyProfileswithNoJobCode_PM() throws IOException {
		
		verifyProfileswithNoJobCode_PM = new PO23_VerifyProfileswithNoJobCode_PM();
		return verifyProfileswithNoJobCode_PM;
	}
	
	public PO24_ValidatePublishCenter_PM getValidatePublishCenter_PM() throws IOException {
		
		validatePublishCenter_PM = new PO24_ValidatePublishCenter_PM();
		return validatePublishCenter_PM;
	}
	
	public PO25_ValidateExportStatusFunctionality_PM getValidateExportStatusFunctionality_PM() throws IOException {
		
		validateExportStatusFunctionality_PM = new PO25_ValidateExportStatusFunctionality_PM();
		return validateExportStatusFunctionality_PM;
	}
	
	public PO26_VerifyJobsMissingDataTipMessage getVerifyJobsMissingDataTipMessage() throws IOException {
		 
		verifyJobsMissingDataTipMessage = new PO26_VerifyJobsMissingDataTipMessage();
		return verifyJobsMissingDataTipMessage;
	}
	
	public PO27_VerifyInfoMessageForMissingDataProfiles getVerifyInfoMessageForMissingDataProfiles() throws IOException {
		
		if (verifyInfoMessageForMissingDataProfiles == null) {
			verifyInfoMessageForMissingDataProfiles = new PO27_VerifyInfoMessageForMissingDataProfiles();
		}
		return verifyInfoMessageForMissingDataProfiles;
	}
	
	public PO28_VerifyInfoMessageForManualMappingProfiles getVerifyInfoMessageForManualMappingProfiles() throws IOException {
		
		// Singleton pattern: reuse existing instance to preserve field values between method calls
		if (verifyInfoMessageForManualMappingProfiles == null) {
			verifyInfoMessageForManualMappingProfiles = new PO28_VerifyInfoMessageForManualMappingProfiles();
		}
		return verifyInfoMessageForManualMappingProfiles;
	}
	
	public PO29_ValidateJobsWithMissingGRADEdataInJobMapping getValidateJobsWithMissingGRADEdataInJobMapping() throws IOException {
		
		// Singleton pattern: reuse existing instance to preserve field values between method calls
		if (validateJobsWithMissingGRADEDataInJobMapping == null) {
			validateJobsWithMissingGRADEDataInJobMapping = new PO29_ValidateJobsWithMissingGRADEdataInJobMapping();
		}
		return validateJobsWithMissingGRADEDataInJobMapping;
	}
	
	public PO30_ValidateJobsWithMissingDEPARTMENTdataInJobMapping getValidateJobsWithMissingDEPARTMENTdataInJobMapping() throws IOException {
		
		// Singleton pattern: reuse existing instance to preserve field values between method calls
		if (validateJobsWithMissingDEPARTMENTDataInJobMapping == null) {
			validateJobsWithMissingDEPARTMENTDataInJobMapping = new PO30_ValidateJobsWithMissingDEPARTMENTdataInJobMapping();
		}
		return validateJobsWithMissingDEPARTMENTDataInJobMapping;
	}
	
	public PO31_ValidateJobsWithMissingFUNCTIONdataInJobMapping getValidateJobsWithMissingFUNCTIONdataInJobMapping() throws IOException {
		
		// Singleton pattern: reuse existing instance to preserve field values between method calls
		if (validateJobsWithMissingFUNCTIONDataInJobMapping == null) {
			validateJobsWithMissingFUNCTIONDataInJobMapping = new PO31_ValidateJobsWithMissingFUNCTIONdataInJobMapping();
		}
		return validateJobsWithMissingFUNCTIONDataInJobMapping;
	}
	
	public PO32_ValidateJobsWithMissingSUBFUNCTIONdataInJobMapping getValidateJobsWithMissingSUBFUNCTIONdataInJobMapping() throws IOException {
		
		// Singleton pattern: reuse existing instance to preserve field values between method calls
		if (validateJobsWithMissingSUBFUNCTIONDataInJobMapping == null) {
			validateJobsWithMissingSUBFUNCTIONDataInJobMapping = new PO32_ValidateJobsWithMissingSUBFUNCTIONdataInJobMapping();
		}
		return validateJobsWithMissingSUBFUNCTIONDataInJobMapping;
	}
	
	public PO33_ValidateSelectAndHCMSyncLoadedProfiles_PM getValidateSelectAndHCMSyncLoadedProfiles_PM() throws IOException {
		
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
	
	public PO35_ValidateSelectAllWithSearchFunctionality_PM getValidateSelectAllWithSearchFunctionality_PM() throws IOException {
		
		if (validateSelectAllWithSearchFunctionality_PM == null) {
			validateSelectAllWithSearchFunctionality_PM = new PO35_ValidateSelectAllWithSearchFunctionality_PM();
		}
		return validateSelectAllWithSearchFunctionality_PM;
	}
	
	public PO36_ValidateSelectAllWithFiltersFunctionality_PM getValidateSelectAllWithFiltersFunctionality_PM() throws IOException {
		
		if (validateSelectAllWithFiltersFunctionality_PM == null) {
			validateSelectAllWithFiltersFunctionality_PM = new PO36_ValidateSelectAllWithFiltersFunctionality_PM();
		}
		return validateSelectAllWithFiltersFunctionality_PM;
	}
	
	public PO37_ValidateSelectAndPublishLoadedProfiles_JAM getValidateSelectAndPublishLoadedProfiles_JAM() throws IOException {
		
		if (validateSelectAndPublishLoadedProfiles_JAM == null) {
			validateSelectAndPublishLoadedProfiles_JAM = new PO37_ValidateSelectAndPublishLoadedProfiles_JAM();
		}
		return validateSelectAndPublishLoadedProfiles_JAM;
	}
	
	public PO38_ValidateSelectAllWithSearchFunctionality_JAM getValidateSelectAllWithSearchFunctionality_JAM() throws IOException {
		
		if (validateSelectAllWithSearchFunctionality_JAM == null) {
			validateSelectAllWithSearchFunctionality_JAM = new PO38_ValidateSelectAllWithSearchFunctionality_JAM();
		}
		return validateSelectAllWithSearchFunctionality_JAM;
	}
	
	public PO39_ValidateSelectAllWithFiltersFunctionality_JAM getValidateSelectAllWithFiltersFunctionality_JAM() throws IOException {
		
		if (validateSelectAllWithFiltersFunctionality_JAM == null) {
			validateSelectAllWithFiltersFunctionality_JAM = new PO39_ValidateSelectAllWithFiltersFunctionality_JAM();
		}
		return validateSelectAllWithFiltersFunctionality_JAM;
	}
	
	public PO40_ValidateSelectAndPublishAllJobProfilesinJAM getValidateSelectAndPublishAllJobProfilesinJAM() throws IOException {
		
		if (validateSelectAndPublishAllJobProfilesinJAM == null) {
			validateSelectAndPublishAllJobProfilesinJAM = new PO40_ValidateSelectAndPublishAllJobProfilesinJAM();
		}
		return validateSelectAndPublishAllJobProfilesinJAM;
	}
	
	public PO41_ValidateApplicationPerformance_JAM_and_HCM getValidateApplicationPerformance_JAM_and_HCM() throws IOException {
		
		if (validateApplicationPerformance_JAM_and_HCM == null) {
			validateApplicationPerformance_JAM_and_HCM = new PO41_ValidateApplicationPerformance_JAM_and_HCM();
		}
		return validateApplicationPerformance_JAM_and_HCM;
	}
	
	public PO42_ClearProfileSelectionwithHeaderCheckbox_PM getClearProfileSelectionwithHeaderCheckbox_PM() throws IOException {
		
		if (clearProfileSelectionwithHeaderCheckbox_PM == null) {
			clearProfileSelectionwithHeaderCheckbox_PM = new PO42_ClearProfileSelectionwithHeaderCheckbox_PM();
		}
		return clearProfileSelectionwithHeaderCheckbox_PM;
	}
	
	public PO43_ClearProfileSelectionwithNoneButton_PM getClearProfileSelectionwithNoneButton_PM() throws IOException {
		
		if (clearProfileSelectionwithNoneButton_PM == null) {
			clearProfileSelectionwithNoneButton_PM = new PO43_ClearProfileSelectionwithNoneButton_PM();
		}
		return clearProfileSelectionwithNoneButton_PM;
	}
	
	public PO44_ClearProfileSelectionwithHeaderCheckbox_JAM getClearProfileSelectionwithHeaderCheckbox_JAM() throws IOException {
		
		if (clearProfileSelectionwithHeaderCheckbox_JAM == null) {
			clearProfileSelectionwithHeaderCheckbox_JAM = new PO44_ClearProfileSelectionwithHeaderCheckbox_JAM();
		}
		return clearProfileSelectionwithHeaderCheckbox_JAM;
	}
	
	public PO45_ClearProfileSelectionwithNoneButton_JAM getClearProfileSelectionwithNoneButton_JAM() throws IOException {
		
		if (clearProfileSelectionwithNoneButton_JAM == null) {
			clearProfileSelectionwithNoneButton_JAM = new PO45_ClearProfileSelectionwithNoneButton_JAM();
		}
		return clearProfileSelectionwithNoneButton_JAM;
	}
	
	public PO46_ValidateSelectionOfUnmappedJobs_JAM getValidateSelectionOfUnmappedJobs_JAM() throws IOException {
		
		if (validateSelectionOfUnmappedJobs_JAM == null) {
			validateSelectionOfUnmappedJobs_JAM = new PO46_ValidateSelectionOfUnmappedJobs_JAM();
		}
		return validateSelectionOfUnmappedJobs_JAM;
	}
	
	/**
	 * Reset all page object instances (useful for cleanup between tests)
	 */
	public void resetPageObjects() {
		kfoneLogin = null;
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
		validateSortingFunctionality = null;
		manualMappingofSPinAutoAI = null;
		mapDifferentSPtoProfileInAutoAI = null;
		validateHCMSyncProfilesTab_PM = null;
		verifyProfileswithNoJobCode_PM = null;
		validatePublishCenter_PM = null;
		validateExportStatusFunctionality_PM = null;
		verifyJobsMissingDataTipMessage = null;
		verifyInfoMessageForMissingDataProfiles = null;
		verifyInfoMessageForManualMappingProfiles = null;
		validateJobsWithMissingGRADEDataInJobMapping = null;
		validateJobsWithMissingDEPARTMENTDataInJobMapping = null;
		validateJobsWithMissingFUNCTIONDataInJobMapping = null;
		validateJobsWithMissingSUBFUNCTIONDataInJobMapping = null;
		validateSelectAndHCMSyncLoadedProfiles_PM = null;
		validateSelectAndSyncAllProfiles_PM = null;
		validateSelectAndPublishLoadedProfiles_JAM = null;
		validateSelectAllWithSearchFunctionality_JAM = null;
		validateSelectAllWithFiltersFunctionality_JAM = null;
		validateSelectAndPublishAllJobProfilesinJAM = null;
		validateApplicationPerformance_JAM_and_HCM = null;
		clearProfileSelectionwithHeaderCheckbox_PM = null;
		clearProfileSelectionwithNoneButton_PM = null;
		clearProfileSelectionwithHeaderCheckbox_JAM = null;
		clearProfileSelectionwithNoneButton_JAM = null;
		validateSelectionOfUnmappedJobs_JAM = null;
	}

}
