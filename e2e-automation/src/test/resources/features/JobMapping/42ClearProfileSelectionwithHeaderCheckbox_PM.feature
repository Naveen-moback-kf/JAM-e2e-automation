@Clear_Profile_Selection_With_Header_Checkbox_PM
Feature: Validate Clear selection of Profiles in HCM Sync Profiles screen with Header Checkbox
	
	@Navigate_HCM_Sync_Profiles
  	Scenario: Verify Title and Description of HCM Sync Profiles
    	Then Skip scenario if user does not have HCM Sync access
    	When User is on Profile Manager page
    	Then Click on HCM Sync Profiles header button
    	Then User should be navigated to HCM Sync Profiles screen
    	And Verify title is correctly displaying in HCM Sync Profiles screen
    	And Verify description below the title is correctly displaying in HCM Sync Profiles screen
	
	@SelectALLProfiles_PM
	Scenario: Select All Profiles in HCM Sync Profiles screen
		Then Skip scenario if user does not have HCM Sync access
		When User is in HCM Sync Profiles screen
		Then Click on Chevron button beside header checkbox in HCM Sync Profiles screen
		Then Click on Select All button in HCM Sync Profiles screen
		
	@SyncwithHCM_Component
  	Scenario: Verify Sync with HCM button is Enabled in HCM Sync Profiles screen
    	Then Skip scenario if user does not have HCM Sync access
    	Then User should verify Sync with HCM button is enabled in HCM Sync Profiles screen
    	
    @Uncheck_Header_Checkbox_PM
    Scenario: Uncheck Header Checkbox to clear selection of Loaded Profiles in HCM
    	Then Skip scenario if user does not have HCM Sync access
    	When User is in HCM Sync Profiles screen
    	Then Click on header checkbox to Unselect loaded job profiles in HCM Sync Profiles screen
    	Then User should verify Sync with HCM button is enabled in HCM Sync Profiles screen	
    	Then Verify Loaded Profiles are unselected in HCM Sync Profiles screen
    	Then Scroll page to view more job profiles in HCM Sync Profiles screen
    	Then Scroll page to view more job profiles in HCM Sync Profiles screen
    	And Verify newly loaded profiles are still Selected in HCM Sync Profiles screen
    	Then Refresh HCM Sync Profiles screen
    	
    @Select_Loaded_Profiles_PM
    Scenario: Select Loaded Profiles with Header Checkbox in HCM
    	Then Skip scenario if user does not have HCM Sync access
    	When User is in HCM Sync Profiles screen
    	Then Click on header checkbox to select loaded job profiles in HCM Sync Profiles screen
    	Then Scroll page to view more job profiles in HCM Sync Profiles screen
    	Then Verify Profiles loaded after clicking Header checkbox are not selected in HCM Sync Profiles Screen
    	
    @Uncheck_Header_Checkbox_PM
    Scenario: Uncheck Header Checkbox to clear selection of Loaded Profiles in HCM
    	Then Skip scenario if user does not have HCM Sync access
    	When User is in HCM Sync Profiles screen
    	Then Click on header checkbox to select loaded job profiles in HCM Sync Profiles screen
    	Then User should verify Sync with HCM button is enabled in HCM Sync Profiles screen	
    	Then Click on header checkbox to Unselect loaded job profiles in HCM Sync Profiles screen
    	Then User should verify Sync with HCM button is disabled in HCM Sync Profiles screen	
    	Then Scroll page to view more job profiles in HCM Sync Profiles screen
    	Then Scroll page to view more job profiles in HCM Sync Profiles screen
    	Then Verify Loaded Profiles are unselected in HCM Sync Profiles screen