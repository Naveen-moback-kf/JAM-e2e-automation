@Select_And_HCM_Sync_Loaded_Profiles_PM
Feature: Select Loaded Profiles and Sync with HCM in HCM Sync Profiles screen in PM

	@Navigate_HCM_Sync_Profiles
  	Scenario: Verify Title and Description of HCM Sync Profiles
    	Then Skip scenario if user does not have HCM Sync access
    	When User is on Profile Manager page
    	Then Click on HCM Sync Profiles header button
    	Then User should be navigated to HCM Sync Profiles screen
    	And Verify title is correctly displaying in HCM Sync Profiles screen
    	And Verify description below the title is correctly displaying in HCM Sync Profiles screen
    	
    @Select_Loaded_Profiles_PM
    Scenario: Select Loaded Profiles with Header Checkbox
    	Then Skip scenario if user does not have HCM Sync access
    	When User is in HCM Sync Profiles screen
    	Then Scroll page to view more job profiles in HCM Sync Profiles screen
    	Then Scroll page to view more job profiles in HCM Sync Profiles screen
    	Then Click on header checkbox to select loaded job profiles in HCM Sync Profiles screen
    	Then Scroll page to view more job profiles in HCM Sync Profiles screen
    	Then Verify Profiles loaded after clicking Header checkbox are not selected in HCM Sync Profiles Screen
    	
    @SyncwithHCM
  	Scenario: Verify Sync with HCM button functionality and Verify PopUp
    	Then Skip scenario if user does not have HCM Sync access
    	When User is in HCM Sync Profiles screen
    	Then User should verify Sync with HCM button is enabled in HCM Sync Profiles screen
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
