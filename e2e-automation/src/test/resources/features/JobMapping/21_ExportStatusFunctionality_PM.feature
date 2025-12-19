@Validate_Export_Status_in_PM
Feature: Validate Export Status behavior of Success Profiles in HCM Sync Profiles screen

  @Navigate_HCM_Sync_Profiles
  Scenario: Navigate to HCM Sync Profiles screen
    Then Skip scenario if user does not have HCM Sync access
    When User is on Profile Manager page
    When Click on HCM Sync Profiles header button
    Then User should be navigated to HCM Sync Profiles screen
    And Verify title is correctly displaying in HCM Sync Profiles screen
    And Verify description below the title is correctly displaying in HCM Sync Profiles screen

  @Not_Exported_Status
  Scenario: Find and Verify Success Profile with Export Status as Not Exported
    Then Skip scenario if user does not have HCM Sync access
    When User is in HCM Sync Profiles screen
    Then User should search for a Profile with Export Status as Not Exported
    Then Verify details of the Not Exported Success Profile in HCM Sync Profiles screen
    Then Verify Success Profile checkbox is enabled and able to perform export operation

  @Export_Status
  Scenario: Perform Sync with HCM Operation and Verify Export Status of the Success Profile
    Then Skip scenario if user does not have HCM Sync access
    Then Click on checkbox of Success Profile with Export Status as Not Exported
    Then User should verify Sync with HCM button is enabled in HCM Sync Profiles screen
    Then Click on Sync with HCM button in HCM Sync Profiles screen
    Then User should verify Sync with HCM success popup appears on screen in HCM Sync Profiles screen
    Then Refresh HCM Sync Profiles screen
    Then User should verify Export Status of SP updated as Exported

  @Exported_Modified_Status
  Scenario: Modify Details of Recently Exported Success Profile and Verify Export Status
    Then Skip scenario if user does not have HCM Sync access
    When User is in HCM Sync Profiles screen
    Then User should click on Recently Exported Success Profile Job Name
    Then User should be navigated to SP details page
    Then Click on three dots in SP details page
    Then Click on Edit Success Profile option
    Then Click on Edit button of Details section
    Then Modify Function and SubFunction values of the Success Profile
    Then Click on Done button
    Then Click on Save button
    Then User should be navigated to SP details page after Saving SP details
    Then Click on HCM Sync Profiles header button
    Then User should be navigated to HCM Sync Profiles screen
    Then User should verify Recently Modified Success Profile is displaying on Top of the Job Proifles List
    Then User should verify Recently Exported and Modified Success Profile Export Status updated as Exported-Modfied
