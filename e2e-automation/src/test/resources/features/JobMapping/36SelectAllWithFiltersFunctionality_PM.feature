@SelectAll_With_Filters_PM
Feature: Validate Select All and Loaded Profiles Selection with Filters - PM (HCM Sync Profiles)

  @SelectAll_Filter_PM @SelectAll_PM
  Scenario: PM - Navigate to HCM Sync Profiles and Select All Filtered Profiles
    Then Skip scenario if user does not have HCM Sync access
    When User is on Profile Manager page
    Then Click on HCM Sync Profiles header button
    Then User should be navigated to HCM Sync Profiles screen
    Then Verify job profiles count is displaying on the page in HCM Sync Profiles screen
    Then Click on Filters dropdown button in HCM Sync Profiles screen
    Then Apply filter and verify profiles count in "PM" screen
    Then User should verify count of job profiles is correctly showing on top of Job Profiles listing table in HCM Sync Profiles screen
    Then User should scroll down to view last filtered result in "PM" screen
    Then User should validate all filtered results match the applied filter in "PM" screen
    Then Click on Chevron button beside header checkbox in "PM" screen
    Then Click on Select All button in "PM" screen
    Then User should verify action button is enabled in "PM" screen
    Then Capture baseline of selected profiles in "PM" screen
    Then Click on Clear All Filters button in "PM" screen
    Then User should verify action button is enabled in "PM" screen

  @SelectAll_Filter_PM_Alternative @SelectAll_PM
  Scenario: PM - Alternative Validation with Different Filter
    Then Skip scenario if user does not have HCM Sync access
    When User is in HCM Sync Profiles screen
    Then Click on Filters dropdown button in HCM Sync Profiles screen
    Then Apply different filter for alternative validation in "PM" screen
    Then User should verify count of job profiles is correctly showing on top of Job Profiles listing table in HCM Sync Profiles screen
    Then Scroll down to load all second filter results in "PM" screen
    Then Verify all loaded profiles in second filter are NOT selected in "PM" screen
    Then Click on Clear All Filters button in "PM" screen

  @SelectAll_Filter_PM_Sync @SelectAll_PM
  Scenario: PM - Sync Selected Profiles with HCM
    Then Skip scenario if user does not have HCM Sync access
    When User is in HCM Sync Profiles screen
    Then Click on Sync with HCM button in HCM Sync Profiles screen
    Then User should verify Sync with HCM success popup appears on screen in HCM Sync Profiles screen

  @SelectAll_Filter_PM_History @SelectAll_PM
  Scenario: PM - Verify Export History in Publish Center
    Then Skip scenario if user does not have HCM Sync access
    When User is in HCM Sync Profiles screen after syncing profiles
    Then Click on Publish Center button
    Then Verify user navigated to Job Profile History screen succcessfully
    Then Verify Recently exported job profiles history is in top row
    Then Verify details of the Recently exported job profiles in Job Profile History screen

  # ═══════════════════════════════════════════════════════════════════════════════
  # Loaded Profiles Selection with Filters
  # ═══════════════════════════════════════════════════════════════════════════════

  @LoadedProfiles_Filter_PM @LoadedProfiles_PM
  Scenario: PM - Navigate to HCM Sync Profiles and Select Loaded Profiles with Filters
    Then Skip scenario if user does not have HCM Sync access
    Then Click on HCM Sync Profiles header button 
    Then User should be navigated to HCM Sync Profiles screen
    When User is in HCM Sync Profiles screen
    Then Verify job profiles count is displaying on the page in HCM Sync Profiles screen
    Then Click on Filters dropdown button in HCM Sync Profiles screen
    Then Apply filter and verify profiles count in "PM" screen
    Then User should verify count of job profiles is correctly showing on top of Job Profiles listing table in HCM Sync Profiles screen
    Then Select loaded job profiles using header checkbox in "PM" screen
    Then Scroll to load more profiles in "PM" screen
    Then Verify newly loaded profiles after header checkbox are NOT selected in "PM" screen
    Then Capture baseline of selected profiles in "PM" screen
    Then Click on Clear All Filters button in "PM" screen
    Then User should verify action button is enabled in "PM" screen
    Then Verify only Filtered Profiles are selected after clearing all filters in "PM" screen
