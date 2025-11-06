@Validate_Publish_Selected_Job_Profiles_Functionality
Feature: Validate functionality of publishing one or more than one job profiles using Publish Selected Profiles

  @Navigate_To_Job_Mapping
  Scenario: Navigate to Job Mapping page 
    Then Navigate to Job Mapping page from KFONE Global Menu in PM
    Then User should verify Job Mapping logo is displayed on screen

  @PublishSelectedProfiles
  Scenario: User selects multiple job profiles and publishes them together
    When User is in Job Mapping page
    Then User should verify Publish Selected Profiles button is disabled
    Then Verify job profiles count is displaying on the page
    Then Click on checkbox of first job profile
    Then User should verify Publish Selected Profiles button is enabled
    Then Click on checkbox of second job profile
    Then Click on Publish Selected Profiles button
    Then User should get success profile published popup
    And Close the success profile published popup
    Then User is in Job Mapping page
    And User should verify count of job profiles is correctly showing on top of Job Profiles listing table

  @VerifyPublishedJobsInViewPublished
  Scenario: Verify Published Job Profiles moved to View Published screen
    When User is in Job Mapping page
    Then Click on View Published toggle button to turn on
    Then Search for Published Job name1
    Then User should verify Published first job profile is displayed in Row1 in View Published screen
    Then Clear Search bar in Job Mapping page
    Then Search for Published Job name2
    Then User should verify Published second job profile is displayed in Row1 in View Published screen
    Then Click on toggle button to turn off

  @VerifyPublishedJobsInHCMSyncProfilesTabInPM
  Scenario: Verify Published Job Profiles is displaying as Custom Profile in HCM Sync Profiles screen in PM
   	Then Click on KFONE Global Menu in Job Mapping UI
    Then Click on Profile Manager application button in KFONE Global Menu
    Then Verify user should land on Profile Manager dashboard page
    Then User should navigate to HCM Sync Profiles screen in PM
    Then User should verify Date on Published First Job matches with current date
    Then User should verify Date on Published Second Job matches with current date
    Then Search for Published Job name2 in HCM Sync Profiles screen in PM
    Then User should verify Published Second Job Profile is displayed in Row1 in HCM Sync Profiles screen in PM
    Then User should verify SP details page opens on click of Published Job name
