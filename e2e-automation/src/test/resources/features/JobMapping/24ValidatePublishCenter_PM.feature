@Validate_Publish_Center_in_PM
Feature: Validate Job Profiles History in Publish Center in HCM Sync Profiles screen

  @Navigate_HCM_Sync_Profiles
  Scenario: Navigate to HCM Sync Profiles screen
    Then Skip scenario if user does not have HCM Sync access
    When User is on Profile Manager page
    When Click on HCM Sync Profiles header button
    Then User should be navigated to HCM Sync Profiles screen
    And Verify title is correctly displaying in HCM Sync Profiles screen
    And Verify description below the title is correctly displaying in HCM Sync Profiles screen

  @SyncWithHCMFromHCMSyncProfiles
  Scenario: Sync job profiles with HCM and verify success popup
    Then Skip scenario if user does not have HCM Sync access
    When User is in HCM Sync Profiles screen
    Then Select Job Profiles in HCM Sync Profiles screen
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

  @VerifyProfilesExportedScreen
  Scenario: Verify details in Profiles Exported screen from Job Profile History
    Then Skip scenario if user does not have HCM Sync access
    When User is in Job Profile History screen
    Then Click on Profiles count of Recently exported job profiles in Job Profile History screen
    Then User should be navigated to Profiles Exported screen
    Then Verify details in Profiles Exported screen
    Then Close Profiles Exported screen
    Then Verify user navigated to Job Profile History screen succcessfully

  @VerifyDefaultOrderInJobProfileHistory
  Scenario: Verify default order of job profiles in Job Profile History screen
    Then Skip scenario if user does not have HCM Sync access
    When User is in Job Profile History screen
    Then User should scroll page down two times to view first thirty job profiles in Job Profile History screen
    Then User should verify first thirty job profiles in default order before applying sorting in Job Profile History screen

  @SortByNoOfProfilesAscending
  Scenario: Validate sorting by No. of Profiles in Ascending order in Job Profile History screen
    Then Skip scenario if user does not have HCM Sync access
    When User is in Job Profile History screen
    Then Sort Job Profiles by No. of Profiles in Ascending order in Job Profile History screen
    Then User should scroll page down two times to view first thirty job profiles in Job Profile History screen
    Then User should verify first thirty job profiles sorted by No. of Profiles in Ascending order in Job Profile History screen
    Then User should Refresh Job Profile History screen and Verify Job Profiles are in default order

  @SortByNoOfProfilesDescending
  Scenario: Validate sorting by No. of Profiles in Descending order in Job Profile History screen
    Then Skip scenario if user does not have HCM Sync access
    When User is in Job Profile History screen
    Then Sort Job Profiles by No. of Profiles in Descending order in Job Profile History screen
    Then User should scroll page down two times to view first thirty job profiles in Job Profile History screen
    Then User should verify first thirty job profiles sorted by No. of Profiles in Descending order in Job Profile History screen
    Then User should Refresh Job Profile History screen and Verify Job Profiles are in default order

  @SortByAccessedDateAscending
  Scenario: Validate sorting by Accessed Date in Ascending order in Job Profile History screen
    Then Skip scenario if user does not have HCM Sync access
    When User is in Job Profile History screen
    Then Sort Job Profiles by Accessed Date in Ascending order in Job Profile History screen
    Then User should scroll page down two times to view first thirty job profiles in Job Profile History screen
    Then User should verify first thirty job profiles sorted by Accessed Date in Ascending order in Job Profile History screen
    Then User should Refresh Job Profile History screen and Verify Job Profiles are in default order

  @SortByAccessedDateDescending
  Scenario: Validate sorting by Accessed Date in Descending order in Job Profile History screen
    Then Skip scenario if user does not have HCM Sync access
    When User is in Job Profile History screen
    Then Sort Job Profiles by Accessed Date in Descending order in Job Profile History screen
    Then User should scroll page down two times to view first thirty job profiles in Job Profile History screen
    Then User should verify first thirty job profiles sorted by Accessed Date in Descending order in Job Profile History screen
    Then User should Refresh Job Profile History screen and Verify Job Profiles are in default order
