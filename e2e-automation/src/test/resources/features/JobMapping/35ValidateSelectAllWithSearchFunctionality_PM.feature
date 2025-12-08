@SelectAll_With_Search_Functionality_in_HCM_PM
Feature: Validate Select All Profiles with Search Functionality in HCM Sync Profiles screen

	@Navigate_HCM_Sync_Profiles
  	Scenario: Navigate to HCM Sync Profiles screen 
    	Then Skip scenario if user does not have HCM Sync access
    	When User is on Profile Manager page
    	Then Click on HCM Sync Profiles header button
    	Then User should be navigated to HCM Sync Profiles screen
    	
    @SearchJobProfileWithJobNameSubstring
  	Scenario: Search for the Job Profile using Job Name Substring
    	Then Skip scenario if user does not have HCM Sync access
    	When User is in HCM Sync Profiles screen
    	Then Verify job profiles count is displaying on the page in HCM Sync Profiles screen
    	Then Verify Search bar text box is clickable in HCM Sync Profiles screen
   	 	Then Verify Search bar placeholder text in HCM Sync Profiles screen
    	Then Enter Job profile name in search bar in HCM Sync Profiles screen
    	Then User should verify count of job profiles is correctly showing on top of Job Profiles listing table in HCM Sync Profiles screen
    	
    @ValidateSearchResults_HCM_Sync_Profiles
  	Scenario: Validate Job Profiles Search Results listing table contains Searched substring in Organization Job Name
    # Note: These steps will be automatically skipped if search returns "Showing 0 of X results" (no results found)
    	Then Skip scenario if user does not have HCM Sync access
    	Then User should scroll down to view last search result in HCM Sync Profiles screen
    	Then User should validate all search results contains substring used for searching in HCM Sync Profiles screen
    	
	@SelectAll_Job_Profiles_HCM_Sync
	Scenario: Select All Searched Job Profiles in HCM Sync Profiles screen and clear search 
		Then Skip scenario if user does not have HCM Sync access
		When User is in HCM Sync Profiles screen
		Then Click on Chevron button beside header checkbox in HCM Sync Profiles screen
		Then Click on Select All button in HCM Sync Profiles screen
		Then User should verify Sync with HCM button is enabled in HCM Sync Profiles screen
		When User is in HCM Sync Profiles screen with Selected Search Results
		Then Clear Search bar in HCM Sync Profiles screen
		
	#@Verify_Profiles_Selection
	#Scenario: Verify only Searched Profiles are selected after clearing search bar
	 	#Then Skip scenario if user does not have HCM Sync access
	 	#Then Verify only Searched Profiles are selected after clearing search bar in HCM Sync Profiles screen
	 	
	@Alternative_Validation_With_Second_Search
	Scenario: Search with different substring and verify those results are NOT selected
	 	Then Skip scenario if user does not have HCM Sync access
	 	When User is in HCM Sync Profiles screen
	 	Then Enter different Job name substring in search bar for alternative validation in HCM Sync Profiles screen
	 	Then User should verify count of job profiles is correctly showing on top of Job Profiles listing table in HCM Sync Profiles screen
	 	Then Scroll down to load all second search results in HCM Sync Profiles screen
	 	Then Verify all loaded profiles in second search are NOT selected in HCM Sync Profiles screen
	 		
   @SyncwithHCM
  	Scenario: Verify Sync with HCM button functionality and Verify PopUp
    	Then Skip scenario if user does not have HCM Sync access
    	When User is in HCM Sync Profiles screen
    	Then Click on Sync with HCM button in HCM Sync Profiles screen
    	Then User should verify Sync with HCM success popup appears on screen in HCM Sync Profiles screen
    	
   @NavigateToPublishCenterAndVerifyHistory
   Scenario: Navigate to Publish Center and verify recently exported profiles history
    	Then Skip scenario if user does not have HCM Sync access
    	When User is in HCM Sync Profiles screen after syncing profiles
    	Then Click on Publish Center button
    	Then Verify user navigated to Job Profile History screen succcessfully
    	Then Verify Recently exported job profiles history is in top row
    	Then Verify details of the Recently exported job profiles in Job Profile History screen
  
  


