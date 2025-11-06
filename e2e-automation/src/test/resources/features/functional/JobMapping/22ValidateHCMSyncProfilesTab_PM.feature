@Validate_HCMSyncProfiles_Tab_in_PM @Smoke_Test
Feature: Validate HCM Sync Profiles screen functionality in PM

  @Navigate_HCM_Sync_Profiles
  Scenario: Verify Title and Description of HCM Sync Profiles
    When User is on Profile Manager page
    When Click on HCM Sync Profiles header button
    Then User should be navigated to HCM Sync Profiles screen
    And Verify title is correctly displaying in HCM Sync Profiles screen
    And Verify description below the title is correctly displaying in HCM Sync Profiles screen

  @ProfileCount_Component
  Scenario: Verify count of job profiles is correctly showing on top of Job Profiles listing table in HCM Sync Profiles screen
    Then Verify job profiles count is displaying on the page in HCM Sync Profiles screen
    Then Scroll page to view more job profiles in HCM Sync Profiles screen
    And User should verify count of job profiles is correctly showing on top of Job Profiles listing table in HCM Sync Profiles screen

  @Searchbar_Component
  Scenario: Verify Search bar component is available in HCM Sync Profiles screen
    Then Verify Search bar text box is clickable in HCM Sync Profiles screen
    Then Verify Search bar placeholder text in HCM Sync Profiles screen
    Then Enter Job profile name in search bar in HCM Sync Profiles screen
    Then User should verify profile name matching profile is displaying in Organization jobs profile list
    Then Click on name matching profile in HCM Sync Profiles screen
    Then User should be navigated to SP details page on click of matching profile
    Then Click on HCM Sync Profiles header button
    And Clear Search bar in HCM Sync Profiles screen

  @VerifyFiltersDropdownOptions
  Scenario: Verify available filter options in Filters dropdown
    When User is in HCM Sync Profiles screen
    Then Verify job profiles count is displaying on the page in HCM Sync Profiles screen
    Then Click on Filters dropdown button in HCM Sync Profiles screen
    Then Verify options available inside Filters dropdown in HCM Sync Profiles screen

  @ApplyKFGradeFilter
  Scenario: Apply and clear KF Grade filter in HCM Sync Profiles screen
    When User is in HCM Sync Profiles screen
    Then Verify job profiles count is displaying on the page in HCM Sync Profiles screen
    Then Click on Filters dropdown button in HCM Sync Profiles screen
    Then Apply KF Grade filter and verify profiles count is correctly displaying in HCM Sync Profiles screen
    Then Clear KF Grade filter in HCM Sync Profiles screen
    Then Verify job profiles count is displaying on the page in HCM Sync Profiles screen

  @ApplyLevelsFilter
  Scenario: Apply and clear Levels filter in HCM Sync Profiles screen
    When User is in HCM Sync Profiles screen
    Then Verify job profiles count is displaying on the page in HCM Sync Profiles screen
    Then Click on Filters dropdown button in HCM Sync Profiles screen
    Then Apply Levels filter and verify profiles count is correctly displaying in HCM Sync Profiles screen
    Then Clear Levels filter in HCM Sync Profiles screen
    Then Verify job profiles count is displaying on the page in HCM Sync Profiles screen

  @ApplyFunctionsSubfunctionsFilter
  Scenario: Apply Functions or Subfunctions filter in HCM Sync Profiles screen
    When User is in HCM Sync Profiles screen
    Then Verify job profiles count is displaying on the page in HCM Sync Profiles screen
    Then Click on Filters dropdown button in HCM Sync Profiles screen
    Then Apply Functions or Subfunctions filter and verify profiles count is correctly displaying in HCM Sync Profiles screen
    Then Click on Clear All Filters button in HCM Sync Profiles screen
    Then Verify job profiles count is displaying on the page in HCM Sync Profiles screen

  @ApplyProfileStatusFilter
  Scenario: Apply Profile Status filter in HCM Sync Profiles screen
    When User is in HCM Sync Profiles screen
    Then Verify job profiles count is displaying on the page in HCM Sync Profiles screen
    Then Click on Filters dropdown button in HCM Sync Profiles screen
    Then Apply Profile Status filter and verify profiles count is correctly displaying in HCM Sync Profiles screen
    Then Click on Clear All Filters button in HCM Sync Profiles screen

  @JobProfilesListingTableHeader_Component
  Scenario: Verify job profiles listing table headers in HCM Sync Profiles screen
    Then User should verify Organization jobs table headers are correctly displaying in HCM Sync Profiles screen
    
  @SyncwithHCM_Component
  Scenario: Verify Sync with HCM button is Enabled and Disabled based on profile selection and deselection respectively in HCM Sync Profiles screen
    Then User should verify Sync with HCM button is disabled in HCM Sync Profiles screen
    Then Click on header checkbox to select loaded job profiles in HCM Sync Profiles screen
    Then User should verify Sync with HCM button is enabled in HCM Sync Profiles screen
    Then User should uncheck header checkbox to deselect selected job profiles in HCM Sync Profiles screen
    Then User should verify Sync with HCM button is disabled in HCM Sync Profiles screen

  @SyncwithHCM
  Scenario: Verify Sync with HCM button functionality and Verify PopUp
    When User is in HCM Sync Profiles screen
    Then Click on first profile checkbox in HCM Sync Profiles screen
    Then Click on second profile checkbox in HCM Sync Profiles screen
    Then Click on third profile checkbox in HCM Sync Profiles screen
    Then User should verify Sync with HCM button is enabled in HCM Sync Profiles screen
    Then Click on Sync with HCM button in HCM Sync Profiles screen
    Then User should verify Sync with HCM success popup appears on screen in HCM Sync Profiles screen
    Then Verify checkboxes of First, Second and Third Profile are deselected in HCM Sync Profiles screen
