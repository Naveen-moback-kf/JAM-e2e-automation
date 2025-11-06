@Select_Job_Mapping_Loaded_Profiles_With_Filter_JAM @Smoke_Test
Feature: Filter profiles and Select Loaded Profiles in Job Mapping screen

	@Navigate_To_Job_Mapping
  	Scenario: Navigate to Job Mapping page 
    	Then Navigate to Job Mapping page from KFONE Global Menu in PM
    	Then User should verify Job Mapping logo is displayed on screen

    @ApplyFilterToJobProfiles
  	Scenario: Apply filter to Job Profiles
    	When User is in Job Mapping page
    	Then Verify job profiles count is displaying on the page
    	Then Click on Filters dropdown button
    	Then Apply filter and verify profiles count in Job Mapping screen for feature39
    	Then User should verify count of job profiles is correctly showing on top of Job Profiles listing table
    	
   	@Select_Loaded_Profiles_JAM
    Scenario: Select Loaded Profiles with Header Checkbox
    	When User is in Job Mapping page
    	Then Click on header checkbox to select loaded job profiles in Job Mapping screen
    	Then Scroll page to view more job profiles
    	Then Verify Profiles loaded after clicking Header checkbox are not selected in Job Mapping Screen
    	When User is in Job Mapping page with Selected Filter Results for feature39
		Then Click on Clear Filters button
		Then User should verify Publish Selected Profiles button is enabled
		
	#@Verify_Profiles_Selection
	#Scenario: Verify only Filtered Profiles are selected after clearing selected filters
	 	#Then Verify only Filtered Profiles are selected after clearing all filters in Job Mapping screen
	 	
	@Alternative_Validation_With_Different_Filter
	Scenario: Alternative Validation - Apply different filter and verify those results are NOT selected
	 	When User is in Job Mapping page
	 	Then Click on Filters dropdown button
	 	Then Apply different filter for alternative validation in Job Mapping screen for feature39
	 	Then User should verify count of job profiles is correctly showing on top of Job Profiles listing table
	 	Then Scroll down to load all second filter results in Job Mapping screen for feature39
	 	Then Verify all loaded profiles in second filter are NOT selected in Job Mapping screen for feature39

