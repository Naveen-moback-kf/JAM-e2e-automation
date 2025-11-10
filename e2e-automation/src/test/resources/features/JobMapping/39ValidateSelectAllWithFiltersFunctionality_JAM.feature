@SelectAll_With_Filters_Functionality_in_JAM
Feature: Validate Select All Profiles with Filters Functionality in Job Mapping screen

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
    	Then Close the Filters dropdown
    	Then User should verify count of job profiles is correctly showing on top of Job Profiles listing table
    
    @ValidateFilteredResults_Job_Mapping
	Scenario: Validate Filtered Job Profiles Results listing table
    # Note: These steps will be automatically skipped if filter returns "Showing 0 of X results" (no results found)
    	Then User should scroll down to view last filtered result in Job Mapping screen for feature39
    	Then User should validate all filtered results match the applied filter in Job Mapping screen for feature39

	@SelectAll_Filtered_Job_Profiles_JAM
	Scenario: Select All Filtered Job Profiles in Job Mapping screen
		When User is in Job Mapping page
		Then Click on Chevron button beside header checkbox in Job Mapping screen
		Then Click on Select All button in Job Mapping screen
		Then User should verify Publish Selected Profiles button is enabled
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
	 	Then Close the Filters dropdown
	 	Then User should verify count of job profiles is correctly showing on top of Job Profiles listing table
	 	Then Scroll down to load all second filter results in Job Mapping screen for feature39
	 	Then Verify all loaded profiles in second filter are NOT selected in Job Mapping screen for feature39


