@Verify_Profiles_with_No_JobCode_in_PM
Feature: Find and Verify Success Profiles with No Job Code in HCM Sync Profiles screen
 
  @Navigate_HCM_Sync_Profiles
  Scenario: Navigate to HCM Sync Profiles screen
    When User is on Profile Manager page
    When Click on HCM Sync Profiles header button
    Then User should be navigated to HCM Sync Profiles screen
    And Verify title is correctly displaying in HCM Sync Profiles screen
    And Verify description below the title is correctly displaying in HCM Sync Profiles screen

  @Profile_with_No_JobCode
  Scenario: Find and Verify details of the Success Profile with No Job Code in HCM Sync Profiles screen
    When User is in HCM Sync Profiles screen
    Then User should search for Success Profile with No Job Code assigned
    Then User should verify Tooltip is displaying on checkbox of Success Profile with No Job Code
    Then Verify details of the Success Profile with No Job Code assigned in HCM Sync Profiles screen
