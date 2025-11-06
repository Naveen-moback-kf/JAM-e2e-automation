@Select_Loaded_Profiles_With_Search_JAM
Feature: Select Loaded Profiles with Search functionality in Job Mapping screen

	@Navigate_To_Job_Mapping
  	Scenario: Navigate to Job Mapping page 
    	Then Navigate to Job Mapping page from KFONE Global Menu in PM
    	Then User should verify Job Mapping logo is displayed on screen
     
    @SearchJobProfileWithJobNameSubstring
  	Scenario: Search for the Job Profile using Job Name Substring
    	When User is in Job Mapping page
    	Then Verify job profiles count is displaying on the page
    	Then Verify Organization Jobs Search bar text box is clickable
   	 	Then Verify Organization Jobs Search bar placeholder text
    	Then Enter Job name substring in search bar
    	Then User should verify count of job profiles is correctly showing on top of Job Profiles listing table	
    
    @Select_Loaded_Profiles_JAM
    Scenario: Select Loaded Profiles in Job Mapping screen
    	When User is in Job Mapping page
    	Then Click on header checkbox to select loaded job profiles in Job Mapping screen
    	Then Scroll page to view more job profiles
    	Then Verify Profiles loaded after clicking Header checkbox are not selected in Job Mapping Screen
    	
    @ValidateSearchResults_UnpublishedJobs
  	Scenario: Validate Un-Published Job Profiles Search Results and clear Search 
    	Then User should validate all search results contains substring used for searching
    	Then User should verify Publish Selected Profiles button is enabled
		When User is in Job Mapping page with Selected Search Results
		Then Clear Search bar in Job Mapping page
		
	@Verify_Profiles_Selection
	Scenario: Verify only Searched Profiles are selected after clearing search barn in Job Mapping screen
	 	Then Verify only Searched Profiles are selected after clearing search bar in JAM screen
	 	
	#@Alternative_Validation_With_Second_Search
	#Scenario: Alternative Validation - Search with different substring and verify those results are NOT selected
	 	#Then Enter different Job name substring in search bar for alternative validation
	 	#Then User should verify count of job profiles is correctly showing on top of Job Profiles listing table
	 	#Then Scroll down to load all second search results
	 	#Then Verify all loaded profiles in second search are NOT selected