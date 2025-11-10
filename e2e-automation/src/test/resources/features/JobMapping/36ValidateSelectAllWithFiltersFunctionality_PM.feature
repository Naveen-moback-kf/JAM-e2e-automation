@SelectAll_With_Filters_Functionality_in_HCM_PM
Feature: Validate Select All Profiles with Filters Functionality in HCM Sync Profiles screen

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
    
    @ValidateFilteredResults_HCM_Sync_Profiles
	Scenario: Validate Filtered Job Profiles Results listing table
    # Note: These steps will be automatically skipped if filter returns "Showing 0 of X results" (no results found)
    	Then User should scroll down to view last filtered result in HCM Sync Profiles screen for feature36
    	Then User should validate all filtered results match the applied filter in HCM Sync Profiles screen for feature36

	@SelectAll_Filtered_Job_Profiles_HCM_Sync
	Scenario: Select All Filtered Job Profiles in HCM Sync Profiles screen
		When User is in HCM Sync Profiles screen
		Then Click on Chevron button beside header checkbox in HCM Sync Profiles screen
		Then Click on Select All button in HCM Sync Profiles screen
		Then User should verify Sync with HCM button is enabled in HCM Sync Profiles screen
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

	@SyncwithHCM
  	Scenario: Verify Sync with HCM button functionality and Verify PopUp
    	When User is in HCM Sync Profiles screen
    	Then Click on Sync with HCM button in HCM Sync Profiles screen
    	Then User should verify Sync with HCM success popup appears on screen in HCM Sync Profiles screen
    	
   @NavigateToPublishCenterAndVerifyHistory
   Scenario: Navigate to Publish Center and verify recently exported profiles history
    	When User is in HCM Sync Profiles screen after syncing profiles
    	Then Click on Publish Center button
    	Then Verify user navigated to Job Profile History screen succcessfully
    	Then Verify Recently exported job profiles history is in top row
    	Then Verify details of the Recently exported job profiles in Job Profile History screen
  