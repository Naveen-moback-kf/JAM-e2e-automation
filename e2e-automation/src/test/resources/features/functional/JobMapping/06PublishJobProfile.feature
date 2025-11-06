@Validate_Publish_Job_Profile_Functionality
Feature: Publishing job profiles from Jobs listing table

  @Navigate_To_Job_Mapping
  Scenario: Navigate to Job Mapping page 
    Then Navigate to Job Mapping page from KFONE Global Menu in PM
    Then User should verify Job Mapping logo is displayed on screen

  @PublishjobProfileFromJobsListingTable
  Scenario: User publishes job profile directly from the jobs listing table
    When User is in Job Mapping page
    Then Verify job profiles count is displaying on the page
    Then User should verify Publish button is displaying on first job profile
    Then Click on Publish button on first job profile
    #Then User should verify publish success popup appears on screen
    And User should verify count of job profiles is correctly showing on top of Job Profiles listing table

  @VerifyPublishedJobInViewPublished
  Scenario: Published job profile appears in the View Published section
    When User is in Job Mapping page
    Then Click on View Published toggle button to turn on
    Then Search for Published Job name in View Published screen
    Then User should verify Published job is displayed in View Published screen
    Then Click on toggle button to turn off

  @VerifyPublishedJobInHCMSyncProfilesTabInPM
  Scenario: Verify Published Job Profile is displaying as Custom Profile in HCM Sync Profiles screen in PM
    Then Click on KFONE Global Menu in Job Mapping UI
    Then Click on Profile Manager application button in KFONE Global Menu
    Then Verify user should land on Profile Manager dashboard page
    Then User should navigate to HCM Sync Profiles screen in PM
    Then Search for Published Job name in HCM Sync Profiles screen in PM
    Then User should verify Published Job is displayed in HCM Sync Profiles screen in PM
    Then User should verify Date on Published Job matches with current date
    Then User should verify SP details page opens on click of Published Job name
    
  @VerifyPublishedJobInArchitect
  Scenario: Verify Published Job Profile is displaying in jobs page in Architect Application
    Then Click on KFONE Global Menu in Job Mapping UI
    Then Click on Architect application button in KFONE Global Menu
    Then Verify user should land on Architect dashboard page
    Then User should navigate to Jobs page in Architect
    Then Search for Published Job name in Jobs page in Architect
    Then User should verify Published Job is displayed in Jobs page in Architect
    Then User should verify Updated Date on Published Job matches with current date in Architect
   