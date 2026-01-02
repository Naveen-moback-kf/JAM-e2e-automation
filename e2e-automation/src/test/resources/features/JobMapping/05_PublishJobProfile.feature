@Publish_Job_Profile
Feature: Publish Job Profile from Different Sources in Job Mapping

  # ============================================================
  # FLOW 1: PUBLISH FROM JOBS LISTING TABLE
  # ============================================================
  
  @Publish_From_Listing @Navigate_To_Job_Mapping
  Scenario: Navigate to Job Mapping page for Listing Table publish
    Then Navigate to Job Mapping page from KFONE Global Menu in PM
    Then User should verify Job Mapping logo is displayed on screen

  @Publish_From_Listing @PublishFromListingTable
  Scenario: User publishes job profile directly from the jobs listing table
    When User is in Job Mapping page
    Then Verify job profiles count is displaying on the page
    Then User should verify Publish button is displaying on first job profile
    Then Click on Publish button on first job profile
    Then User should verify publish success popup appears on screen
    And User should verify count of job profiles is correctly showing on top of Job Profiles listing table

  @Publish_From_Listing @VerifyInViewPublished
  Scenario: Verify job published from listing table appears in View Published
    When User is in Job Mapping page
    Then Click on View Published toggle button to turn on
    Then Search for Published Job name in View Published screen
    Then User should verify Published job is displayed in View Published screen
    Then Click on toggle button to turn off

  @Publish_From_Listing @VerifyInHCMSync
  Scenario: Verify job published from listing table appears in HCM Sync Profiles with Job Name
    Then Skip scenario if user does not have HCM Sync access
    Then Click on KFONE Global Menu in Job Mapping UI
    Then Click on Profile Manager application button in KFONE Global Menu
    Then Verify user should land on Profile Manager dashboard page
    Then User should navigate to HCM Sync Profiles screen in PM
    Then User should verify Search dropdown is displaying in HCM Sync Profiles screen in PM
    Then Search for Published Job name in HCM Sync Profiles screen in PM
    Then User should verify Published Job is displayed in HCM Sync Profiles screen in PM
    Then User should verify Published Job Code in HCM Sync Profiles screen in PM
    Then User should verify Date on Published Job matches with current date
    
  @Publish_From_Listing @VerifyInHCMSync_ByJobCode
  Scenario: Verify job published from listing table appears in HCM Sync Profiles with Job Code
    Then Skip scenario if user does not have HCM Sync access
    Then Change search type to search by Job Code in HCM Sync Profiles screen in PM 
    Then Search for Published Job Code in HCM Sync Profiles screen in PM
    Then User should verify Published Job is displayed in HCM Sync Profiles screen in PM
    Then User should verify Published Job Code in HCM Sync Profiles screen in PM
    Then User should verify Date on Published Job matches with current date
    Then User should verify SP details page opens on click of Published Job name

  @Publish_From_Listing @VerifyInArchitect
  Scenario: Verify job published from listing table appears in Architect
    Then Click on KFONE Global Menu in Job Mapping UI
    Then Click on Architect application button in KFONE Global Menu
    Then Verify user should land on Architect dashboard page
    Then User should navigate to Jobs page in Architect
    Then Search for Published Job name in Jobs page in Architect
    Then User should verify Published Job is displayed in Jobs page in Architect
    Then User should verify Updated Date on Published Job matches with current date in Architect

  # ============================================================
  # FLOW 2: PUBLISH FROM JOB COMPARISON SCREEN
  # ============================================================

  @Publish_From_Comparison @Navigate_To_Job_Mapping
  Scenario: Navigate to Job Mapping page for Comparison screen publish
    Then Navigate to Job Mapping page from KFONE Global Menu
    Then User should verify Job Mapping logo is displayed on screen

  @Publish_From_Comparison @PublishFromComparisonScreen
  Scenario: Publish Job from DS Suggestions in Job Comparison screen
    When User is in Job Mapping page
    Then Search for Job Profile with View Other Matches button
    Then Click on View Other Matches button
    And Verify user landed on job comparison screen
    Then Select second profile from DS Suggestions of Organization Job
    Then Click on Publish Selected button in Job Comparison page
    Then User should verify publish success popup appears on screen
    Then User should be landed on Job Mapping page
    Then User should verify Job Mapping logo is displayed on screen

  @Publish_From_Comparison @VerifyInViewPublished
  Scenario: Verify job published from comparison screen appears in View Published
    When User is in Job Mapping page
    Then Click on View Published toggle button to turn on
    Then User should verify Published job is displayed in View Published screen
    Then Click on toggle button to turn off

  @Publish_From_Comparison @VerifyInHCMSync_ByJobname
  Scenario: Verify job published from comparison screen appears in HCM Sync Profiles with Job Name
    Then Skip scenario if user does not have HCM Sync access
    Then Click on KFONE Global Menu in Job Mapping UI
    Then Click on Profile Manager application button in KFONE Global Menu
    Then Verify user should land on Profile Manager dashboard page
    Then User should navigate to HCM Sync Profiles screen in PM
    Then User should verify Search dropdown is displaying in HCM Sync Profiles screen in PM
    Then Search for Published Job name in HCM Sync Profiles screen in PM
    Then User should verify Published Job is displayed in HCM Sync Profiles screen in PM
    Then User should verify Published Job Code in HCM Sync Profiles screen in PM
    Then User should verify Date on Published Job matches with current date
    #Then User should verify SP details page opens on click of Published Job name
    
  @Publish_From_Comparison @VerifyInHCMSync_ByJobCode
  Scenario: Verify job published from comparison screen appears in HCM Sync Profiles with Job Code
    Then Skip scenario if user does not have HCM Sync access
    Then Skip scenario if user does not have HCM Sync access
    Then Change search type to search by Job Code in HCM Sync Profiles screen in PM 
    Then Search for Published Job Code in HCM Sync Profiles screen in PM
    Then User should verify Published Job is displayed in HCM Sync Profiles screen in PM
    Then User should verify Published Job Code in HCM Sync Profiles screen in PM
    Then User should verify Date on Published Job matches with current date
    Then User should verify SP details page opens on click of Published Job name

  @Publish_From_Comparison @VerifyInArchitect
  Scenario: Verify job published from comparison screen appears in Architect
    Then Click on KFONE Global Menu in Job Mapping UI
    Then Click on Architect application button in KFONE Global Menu
    Then Verify user should land on Architect dashboard page
    Then User should navigate to Jobs page in Architect
    Then Search for Published Job name in Jobs page in Architect
    Then User should verify Published Job is displayed in Jobs page in Architect
    Then User should verify Updated Date on Published Job matches with current date in Architect

  # ============================================================
  # FLOW 3: PUBLISH FROM PROFILE DETAILS POPUP
  # ============================================================

  @Publish_From_Popup @Navigate_To_Job_Mapping
  Scenario: Navigate to Job Mapping page for Details Popup publish
    Then Navigate to Job Mapping page from KFONE Global Menu
    Then User should verify Job Mapping logo is displayed on screen

  @Publish_From_Popup @PublishFromDetailsPopup
  Scenario: User publishes job profile from the profile details popup
    When User is in Job Mapping page
    Then Search for Job Profile with View Other Matches button
    Then User should verify Organization Job Name and Job Code Values of Job Profile with View Other Matches button
    Then Click on matched profile of Job Profile with View Other Matches button
    When User is on profile details popup
    Then Click on Publish Profile button in profile details popup
    Then User should verify publish success popup appears on screen

  @Publish_From_Popup @VerifyInViewPublished
  Scenario: Verify job published from details popup appears in View Published
    When User is in Job Mapping page
    Then User should verify Job Mapping logo is displayed on screen
    Then Click on View Published toggle button to turn on
    Then User should verify Published job is displayed in View Published screen
    Then Click on toggle button to turn off
    
  @Publish_From_Popup @VerifyInHCMSync_withJobCode  
  Scenario: Verify job published from details popup appears in HCM Sync Profiles with Job Code
    Then Skip scenario if user does not have HCM Sync access
    Then Click on KFONE Global Menu in Job Mapping UI
    Then Click on Profile Manager application button in KFONE Global Menu
    Then Verify user should land on Profile Manager dashboard page
    Then User should navigate to HCM Sync Profiles screen in PM
    Then User should verify Search dropdown is displaying in HCM Sync Profiles screen in PM
    Then Change search type to search by Job Code in HCM Sync Profiles screen in PM 
    Then Search for Published Job Code in HCM Sync Profiles screen in PM
    Then User should verify Published Job is displayed in HCM Sync Profiles screen in PM
    Then User should verify Published Job Code in HCM Sync Profiles screen in PM
    Then User should verify Date on Published Job matches with current date

  @Publish_From_Popup @VerifyInHCMSync
  Scenario: Verify job published from details popup appears in HCM Sync Profiles with Job Name
    Then Skip scenario if user does not have HCM Sync access
    Then Change search type to search by Job Profile in HCM Sync Profiles screen in PM
    Then Search for Published Job name in HCM Sync Profiles screen in PM
    Then User should verify Published Job is displayed in HCM Sync Profiles screen in PM
    Then User should verify Published Job Code in HCM Sync Profiles screen in PM
    Then User should verify Date on Published Job matches with current date
    Then User should verify SP details page opens on click of Published Job name

  @Publish_From_Popup @VerifyInArchitect
  Scenario: Verify job published from details popup appears in Architect
    Then Click on KFONE Global Menu in Job Mapping UI
    Then Click on Architect application button in KFONE Global Menu
    Then Verify user should land on Architect dashboard page
    Then User should navigate to Jobs page in Architect
    Then Search for Published Job name in Jobs page in Architect
    Then User should verify Published Job is displayed in Jobs page in Architect
    Then User should verify Updated Date on Published Job matches with current date in Architect

