@SelectAll_With_Filters_JAM
Feature: Validate Select All and Loaded Profiles Selection with Filters - JAM (Job Mapping)

  @Navigate_To_Job_Mapping
  Scenario: Navigate to Job Mapping page 
    Then Navigate to Job Mapping page from KFONE Global Menu in PM
    Then User should verify Job Mapping logo is displayed on screen

  @SelectAll_Filter_JAM @SelectAll_JAM
  Scenario: JAM - Apply Filter and Select All Filtered Profiles
    When User is in Job Mapping page
    Then Verify job profiles count is displaying on the page
    Then Click on Filters dropdown button
    Then Click on Grades Filters dropdown button
    Then Select one option in Grades Filters dropdown
    Then Store applied filter value for validation in "JAM" screen
    Then Close the Filters dropdown in "JAM" screen
    Then User should verify count of job profiles is correctly showing on top of Job Profiles listing table
    Then User should scroll down to view last filtered result in "JAM" screen
    Then Validate Job Mapping Profiles are correctly filtered with applied Grades Options
    Then Click on Chevron button beside header checkbox in "JAM" screen
    Then Click on Select All button in "JAM" screen
    Then User should verify action button is enabled in "JAM" screen
    Then Capture baseline of selected profiles in "JAM" screen
    Then Click on Clear All Filters button in "JAM" screen
    Then User should verify action button is enabled in "JAM" screen

  @SelectAll_Filter_JAM_Alternative @SelectAll_JAM
  Scenario: JAM - Alternative Validation with Different Filter
    When User is in Job Mapping page
    Then Click on Filters dropdown button
    Then Click on Grades Filters dropdown button
    Then Select different option in Grades Filters dropdown
    Then Store second filter value for validation in "JAM" screen
    Then Close the Filters dropdown in "JAM" screen
    Then User should verify count of job profiles is correctly showing on top of Job Profiles listing table
    Then Scroll down to load all second filter results in "JAM" screen
    Then Verify all loaded profiles in second filter are NOT selected in "JAM" screen
    Then Click on Clear All Filters button in "JAM" screen
    Then Click on View Published toggle button to turn on
    Then Click on View Published toggle button to turn off


  # ═══════════════════════════════════════════════════════════════════════════════
  # Loaded Profiles Selection with Filters
  # ═══════════════════════════════════════════════════════════════════════════════

  @LoadedProfiles_Filter_JAM @LoadedProfiles_JAM
  Scenario: JAM - Navigate to Job Mapping and Select Loaded Profiles with Filters
    When User is in Job Mapping page
    Then Verify job profiles count is displaying on the page
    Then Click on Filters dropdown button
    Then Click on Grades Filters dropdown button
    Then Select one option in Grades Filters dropdown
    Then Store applied filter value for validation in "JAM" screen
    Then Close the Filters dropdown in "JAM" screen
    Then User should verify count of job profiles is correctly showing on top of Job Profiles listing table
    Then Select loaded job profiles using header checkbox in "JAM" screen
    Then Scroll to load more profiles in "JAM" screen
    Then Verify newly loaded profiles after header checkbox are NOT selected in "JAM" screen
    Then Capture baseline of selected profiles in "JAM" screen
    Then Click on Clear All Filters button in "JAM" screen
    Then User should verify action button is enabled in "JAM" screen
    Then Verify only Filtered Profiles are selected after clearing all filters in "JAM" screen
