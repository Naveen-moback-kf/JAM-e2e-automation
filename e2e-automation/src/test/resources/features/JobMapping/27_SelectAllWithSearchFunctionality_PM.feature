@SelectAll_With_Search_PM
Feature: Validate Select All and Loaded Profiles Selection with Search - PM (HCM Sync Profiles)

  @SelectAll_Search_PM @SelectAll_PM
  Scenario: PM - Navigate to HCM Sync Profiles and Select All Searched Profiles
    Then Skip scenario if user does not have HCM Sync access
    When User is on Profile Manager page
    Then Click on HCM Sync Profiles header button
    Then User should be navigated to HCM Sync Profiles screen
    Then Verify job profiles count is displaying on the page in HCM Sync Profiles screen
    Then Verify Search bar text box is clickable in HCM Sync Profiles screen
    Then Verify Search bar placeholder text in HCM Sync Profiles screen
    Then Enter Job profile name in search bar in HCM Sync Profiles screen
    Then User should verify count of job profiles is correctly showing on top of Job Profiles listing table in HCM Sync Profiles screen
    Then User should scroll down to view last search result in "PM" screen
    Then User should validate all search results contains substring used for searching in "PM" screen
    Then Click on Chevron button beside header checkbox in "PM" screen
    Then Click on Select All button in "PM" screen
    Then User should verify action button is enabled in "PM" screen
    Then Capture baseline of selected profiles in "PM" screen
    Then Clear Search bar in "PM" screen

  @SelectAll_Search_PM_Alternative @SelectAll_PM
  Scenario: PM - Alternative Validation with Second Search
    Then Skip scenario if user does not have HCM Sync access
    When User is in HCM Sync Profiles screen
    Then Enter different Job name substring in search bar for alternative validation in "PM" screen
    Then User should verify count of job profiles is correctly showing on top of Job Profiles listing table in HCM Sync Profiles screen
    Then Validate second search results in "PM" screen
    Then Verify all loaded profiles in second search are NOT selected in "PM" screen
    Then Clear Search bar in "PM" screen

  @SelectAll_Search_PM_Sync @SelectAll_PM
  Scenario: PM - Sync Selected Profiles with HCM
    Then Skip scenario if user does not have HCM Sync access
    When User is in HCM Sync Profiles screen
    Then Click on Sync with HCM button in HCM Sync Profiles screen
    Then User should verify Sync with HCM success popup appears on screen in HCM Sync Profiles screen

  @SelectAll_Search_PM_History @SelectAll_PM
  Scenario: PM - Verify Export History in Publish Center
    Then Skip scenario if user does not have HCM Sync access
    When User is in HCM Sync Profiles screen after syncing profiles
    Then Click on Publish Center button
    Then Verify user navigated to Job Profile History screen succcessfully
    Then Verify Recently exported job profiles history is in top row
    Then Verify details of the Recently exported job profiles in Job Profile History screen

  # ═══════════════════════════════════════════════════════════════════════════════
  # Loaded Profiles Selection with Search
  # ═══════════════════════════════════════════════════════════════════════════════

  @LoadedProfiles_Search_PM @LoadedProfiles_PM
  Scenario: PM - Navigate to HCM Sync Profiles and Select Loaded Profiles with Search
    Then Skip scenario if user does not have HCM Sync access
    Then Click on HCM Sync Profiles header button
    When User is in HCM Sync Profiles screen
    Then User should be navigated to HCM Sync Profiles screen
    Then Verify job profiles count is displaying on the page in HCM Sync Profiles screen
    Then Verify Search bar text box is clickable in HCM Sync Profiles screen
    Then Verify Search bar placeholder text in HCM Sync Profiles screen
    Then Enter Job profile name in search bar in HCM Sync Profiles screen
    Then User should verify count of job profiles is correctly showing on top of Job Profiles listing table in HCM Sync Profiles screen
    Then Select loaded job profiles using header checkbox in "PM" screen
    Then Scroll to load more profiles in "PM" screen
    Then Verify newly loaded profiles after header checkbox are NOT selected in "PM" screen
    Then Capture baseline of selected profiles in "PM" screen
    Then Clear Search bar in "PM" screen
    Then User should verify action button is enabled in "PM" screen
    Then Verify only searched profiles are selected after clearing search bar in "PM" screen

