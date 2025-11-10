@Clear_Profile_Selection_With_None_Button_JAM
Feature: Validate Clear selection of Profiles in Job Mapping screen with None Button
	
	@Navigate_To_Job_Mapping
  	Scenario: Navigate to Job Mapping page 
    	Then Navigate to Job Mapping page from KFONE Global Menu in PM
    	Then User should verify Job Mapping logo is displayed on screen
	
	@SelectAll_Job_Profiles_JAM
	Scenario: Select All Job Profiles in Job Mapping screen
		When User is in Job Mapping page
		Then Click on Chevron button beside header checkbox in Job Mapping screen
		Then Click on Select All button in Job Mapping screen
		Then User should verify Publish Selected Profiles button is enabled
		
	@UnSelectAllProfiles_JAM
	Scenario: UnSelect All Profiles in Job Mapping screen with None button
		When User is in Job Mapping page
		Then Click on Chevron button beside header checkbox in Job Mapping screen
		Then Click on None button in Job Mapping screen
		Then User should verify Publish Selected Profiles button is disabled
		Then Scroll page to view more job profiles
    	Then Scroll page to view more job profiles
		Then Verify all Loaded Profiles are Unselected in Job Mapping screen
		
	@Select_Loaded_Profiles_JAM
    Scenario: Select Loaded Profiles in Job Mapping screen
    	When User is in Job Mapping page
    	Then Click on header checkbox to select loaded job profiles in Job Mapping screen
    	Then Scroll page to view more job profiles
    	Then Verify Profiles loaded after clicking Header checkbox are not selected in Job Mapping Screen
    	Then User should verify Publish Selected Profiles button is enabled
    	
    @UnSelectAllProfiles_JAM
	Scenario: UnSelect All Profiles in Job Mapping screen with None button
		When User is in Job Mapping page
		Then Click on Chevron button beside header checkbox in Job Mapping screen
		Then Click on None button in Job Mapping screen
		Then User should verify Publish Selected Profiles button is disabled
		Then Scroll page to view more job profiles
    	Then Scroll page to view more job profiles
		Then Verify all Loaded Profiles are Unselected in Job Mapping screen