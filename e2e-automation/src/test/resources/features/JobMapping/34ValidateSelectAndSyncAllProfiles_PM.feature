@SelectAll_Profiles_And_Sync
Feature: Select All Profiles in HCM Sync Profiles screen and Sync with HCM
	
	@Navigate_HCM_Sync_Profiles
  	Scenario: Verify Title and Description of HCM Sync Profiles
    	When User is on Profile Manager page
    	Then Click on HCM Sync Profiles header button
    	Then User should be navigated to HCM Sync Profiles screen
    	And Verify title is correctly displaying in HCM Sync Profiles screen
    	And Verify description below the title is correctly displaying in HCM Sync Profiles screen
	
	@SelectALLProfiles_PM
	Scenario: Select All Profiles in HCM Sync Profiles screen
		When User is in HCM Sync Profiles screen
		Then Click on Chevron button beside header checkbox in HCM Sync Profiles screen
		Then Click on Select All button in HCM Sync Profiles screen
		Then Verify count of selected profiles by scrolling through all profiles
		
	@SyncwithHCM_Component
  	Scenario: Verify Sync with HCM button is Enabled in HCM Sync Profiles screen
    	Then User should verify Sync with HCM button is enabled in HCM Sync Profiles screen
    	
    @SyncwithHCM
  	Scenario: Verify Sync with HCM button functionality and Verify PopUp
    	When User is in HCM Sync Profiles screen
    	Then Click on Sync with HCM button in HCM Sync Profiles screen
    	Then User should verify Sync with HCM success popup appears on screen in HCM Sync Profiles screen
    	
   @NavigateToPublishCenterAndVerifyHistory
   Scenario: Navigate to Publish Center and verify recently exported profiles history
    	When User is in HCM Sync Profiles screen after syncing profiles
    	Then Click on Publish Center button
    	Then Verify user navigated to Job Profile History screen succcessfully
    	Then Verify Recently exported job profiles history is in top row
    	Then Verify details of the Recently exported job profiles in Job Profile History screen
  