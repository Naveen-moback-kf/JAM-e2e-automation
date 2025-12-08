@Select_HCM_Sync_Loaded_Profiles_With_Search_PM
Feature: Search for profiles and Select Loaded Profiles in HCM Sync Profiles screen in PM

	@Navigate_HCM_Sync_Profiles
  	Scenario: Verify Title and Description of HCM Sync Profiles
    	Then Skip scenario if user does not have HCM Sync access
    	When User is on Profile Manager page
    	Then Click on HCM Sync Profiles header button
    	Then User should be navigated to HCM Sync Profiles screen
    	And Verify title is correctly displaying in HCM Sync Profiles screen
    	And Verify description below the title is correctly displaying in HCM Sync Profiles screen
    	
     @SearchJobProfileWithJobNameSubstring
  	 Scenario: Search for the Job Profile using Job Name Substring
    	Then Skip scenario if user does not have HCM Sync access
    	When User is in HCM Sync Profiles screen
    	Then Verify job profiles count is displaying on the page in HCM Sync Profiles screen
    	Then Verify Search bar text box is clickable in HCM Sync Profiles screen
   	 	Then Verify Search bar placeholder text in HCM Sync Profiles screen
    	Then Enter Job profile name in search bar in HCM Sync Profiles screen
    	Then User should verify count of job profiles is correctly showing on top of Job Profiles listing table in HCM Sync Profiles screen
    	
    @Select_Loaded_Profiles_PM
    Scenario: Select Loaded Profiles with Header Checkbox
    	Then Skip scenario if user does not have HCM Sync access
    	When User is in HCM Sync Profiles screen
    	Then Click on header checkbox to select loaded job profiles in HCM Sync Profiles screen
    	Then Scroll page to view more job profiles in HCM Sync Profiles screen
    	Then Verify Profiles loaded after clicking Header checkbox are not selected in HCM Sync Profiles Screen
    	When User is in HCM Sync Profiles screen with Selected Search Results
		Then Clear Search bar in HCM Sync Profiles screen
		Then User should verify Sync with HCM button is enabled in HCM Sync Profiles screen
		
	@Verify_Profiles_Selection
	Scenario: Verify only Searched Profiles are selected after clearing search bar
	 	Then Skip scenario if user does not have HCM Sync access
	 	Then Verify only Searched Profiles are selected after clearing search bar in HCM Sync Profiles screen
	 	
	#@Alternative_Validation_With_Second_Search
	#Scenario: Search with different substring and verify those results are NOT selected
	 	#Then Skip scenario if user does not have HCM Sync access
	 	#When User is in HCM Sync Profiles screen
	 	#Then Enter different Job name substring in search bar for alternative validation in HCM Sync Profiles screen
	 	#Then User should verify count of job profiles is correctly showing on top of Job Profiles listing table in HCM Sync Profiles screen
	 	#Then Scroll down to load all second search results in HCM Sync Profiles screen
	 	#Then Verify all loaded profiles in second search are NOT selected in HCM Sync Profiles screen
