@SelectAll_And_Publish_Job_Profiles_in_JAM
Feature: Select All and Publish Job Profiles in Job Mapping screen

	@Navigate_To_Job_Mapping
  	Scenario: Navigate to Job Mapping page 
    	Then Navigate to Job Mapping page from KFONE Global Menu in PM
    	Then User should verify Job Mapping logo is displayed on screen
    	
    @UnPublished_Profiles_Count_Before_Publishing
    Scenario: Verify Unpublished Profiles count in Job Mapping Screen BEFORE Publishing Selected Profiles in JAM
    	When User is in Job Mapping page
    	Then Verify count of total Un-Published Profiles before Publishing selected profiles
    	
    @Published_Profiles_Count_Before_Publishing
    Scenario: Verify Published Profiles count in View Published Screen BEFORE Publishing Selected Profiles in JAM
    	When User is in Job Mapping page
    	Then Click on View Published toggle button to turn on
    	Then Verify count of total Published Profiles before Publishing selected profiles
    	Then Click on View Published toggle button to turn off
	
	@SelectAll_Job_Profiles_JAM
	Scenario: Select All Job Profiles in Job Mapping screen
		When User is in Job Mapping page
		Then Click on Chevron button beside header checkbox in Job Mapping screen
		Then Click on Select All button in Job Mapping screen
		Then Verify count of selected profiles from results text in Job Mapping screen
		Then User should verify Publish Selected Profiles button is enabled
		
	@PublishSelectedProfiles_After_SelectAll
  	Scenario: Click Publish Selected Profiles button after Selecting All and verify Async Functionality
  		When User is in Job Mapping page
  		Then Click on Publish Selected Profiles button
  		Then Verify Async functionality message is displayed on JAM screen
  		Then Refresh Job Mapping page after specified time in message
  		
  	@Validate_Progressive_Batch_Publishing
  	Scenario: Validate Progressive Batch Publishing of Profiles in Job Mapping screen
  		When User is in Job Mapping page after initial refresh
  		Then Calculate expected total time for batch publishing based on profile count
  		Then Monitor and validate progressive batch publishing until completion
  		Then Verify all profiles are published successfully
  		
  	@UnPublished_Profiles_Count_After_Publishing
    Scenario: Verify Unpublished Profiles count in Job Mapping Screen AFTER Publishing Selected Profiles in JAM
    	When User is in Job Mapping page
    	Then Verify count of total Un-Published Profiles after Publishing selected profiles
    	Then Get count of Published profiles in Job Mapping screen
    	
    @Published_Profiles_Count_After_Publishing_All
    Scenario: Verify Published Profiles count in View Published Screen AFTER Publishing Selected Profiles in JAM
    	When User is in Job Mapping page
    	Then Click on View Published toggle button to turn on
    	Then Verify count of total Published Profiles after Publishing selected profiles
    	Then Verify Published Profiles count matches in View Published screen
    	Then Click on View Published toggle button to turn off
    	
    	
  

