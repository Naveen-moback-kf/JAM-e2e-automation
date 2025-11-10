@Select_HCM_Sync_Loaded_Profiles_With_Filter_PM
Feature: Filter profiles and Select Loaded Profiles in HCM Sync Profiles screen in PM

	@Navigate_HCM_Sync_Profiles
  	Scenario: Navigate to HCM Sync Profiles screen
    	When User is on Profile Manager page
    	Then Click on HCM Sync Profiles header button
    	Then User should be navigated to HCM Sync Profiles screen

    @ApplyFilterToJobProfiles
  	Scenario: Apply filter to Job Profiles
    	When User is in HCM Sync Profiles screen
    	Then Verify job profiles count is displaying on the page in HCM Sync Profiles screen
    	Then Click on Filters dropdown button in HCM Sync Profiles screen
    	Then Apply filter and verify profiles count in HCM Sync Profiles screen for feature36
    	Then User should verify count of job profiles is correctly showing on top of Job Profiles listing table in HCM Sync Profiles screen
    	
   	@Select_Loaded_Profiles_PM
    Scenario: Select Loaded Profiles with Header Checkbox
    	When User is in HCM Sync Profiles screen
    	Then Click on header checkbox to select loaded job profiles in HCM Sync Profiles screen
    	Then Scroll page to view more job profiles in HCM Sync Profiles screen
    	Then Verify Profiles loaded after clicking Header checkbox are not selected in HCM Sync Profiles Screen
    	When User is in HCM Sync Profiles screen with Selected Filter Results for feature36
		Then Click on Clear All Filters button in HCM Sync Profiles screen
		Then User should verify Sync with HCM button is enabled in HCM Sync Profiles screen
		
	#@Verify_Profiles_Selection
	#Scenario: Verify only Filtered Profiles are selected after clearing selected filters
	 	#Then Verify only Filtered Profiles are selected after clearing all filters in HCM Sync Profiles screen
	 	
	@Alternative_Validation_With_Different_Filter
	Scenario: Alternative Validation - Apply different filter and verify those results are NOT selected
	 	When User is in HCM Sync Profiles screen
	 	Then Click on Filters dropdown button in HCM Sync Profiles screen
	 	Then Apply different filter for alternative validation in HCM Sync Profiles screen for feature36
	 	Then User should verify count of job profiles is correctly showing on top of Job Profiles listing table in HCM Sync Profiles screen
	 	Then Scroll down to load all second filter results in HCM Sync Profiles screen for feature36
	 	Then Verify all loaded profiles in second filter are NOT selected in HCM Sync Profiles screen for feature36