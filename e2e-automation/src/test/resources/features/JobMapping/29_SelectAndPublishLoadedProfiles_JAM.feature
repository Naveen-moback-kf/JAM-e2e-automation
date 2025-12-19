@Select_And_Publish_Loaded_Profiles_JAM
Feature: Select Loaded Profiles and Publish Selected Profiles in Job Mapping screen

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
    
    @Select_Loaded_Profiles_JAM
    Scenario: Select Loaded Profiles in Job Mapping screen
    	When User is in Job Mapping page
    	#Then Scroll page to view more job profiles
    	Then Click on header checkbox to select loaded job profiles in Job Mapping screen
    	Then Scroll page to view more job profiles
    	Then Verify Profiles loaded after clicking Header checkbox are not selected in Job Mapping Screen
    	
    @Publish_Selected_Profiles
    Scenario: Publish Selected profiles in Job Mapping screen
    	When User is in Job Mapping page
    	Then Verify job profiles count is displaying on the page
    	Then User should verify Publish Selected Profiles button is enabled
    	Then Click on Publish Selected Profiles button
    	Then User should get success profile published popup
    	And Close the success profile published popup
    	Then User is in Job Mapping page
    	And User should verify count of job profiles is correctly showing on top of Job Profiles listing table
    	
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

    	