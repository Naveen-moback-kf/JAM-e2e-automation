@Validate_Basic_Filter_Persistence
Feature: Validate Basic Filter Persistence in Job Mapping UI

  @Navigate_To_Job_Mapping
  Scenario: Navigate to Job Mapping page 
    Then Navigate to Job Mapping page from KFONE Global Menu in PM
    Then User should verify Job Mapping logo is displayed on screen

  @ApplyGradesFilters
  Scenario: Apply Grades filters in Job Mapping page
    When User is in Job Mapping page
    Then Verify job profiles count is displaying on the page
    Then Click on Filters dropdown button
    Then Click on Grades Filters dropdown button
    And Select two options in Grades Filters dropdown
    And Close the Filters dropdown
    And User should verify count of job profiles is correctly showing on top of Job Profiles listing table

  @ValidateFiltersAfterPageRefresh
  Scenario: Verify Grades filters persist after page refresh
    When User is in Job Mapping page with Grades filters applied
    Then Refresh Job Mapping page
    And Verify Applied Filters persist on Job Mapping UI

  @ValidateFiltersAfterJobComparisonNavigation
  Scenario: Verify Grades filters persist after navigating to Job Comparison page and back
    When User is in Job Mapping page with Grades filters applied
    Then Search for Job Profile with View Other Matches button
    Then Click on View Other Matches button
    Then Verify user navigated to job comparison page
    Then Click on Browser back button
    Then User should be landed on Job Mapping page
    When User is in Job Mapping page
    And Verify Applied Filters persist on Job Mapping UI

  @ApplyMappingStatusFilters
  Scenario: Apply Mapping Status filters in Job Mapping page
    When User is in Job Mapping page
    Then Verify job profiles count is displaying on the page
    Then Click on Filters dropdown button
    Then Click on Mapping Status Filters dropdown button
    And Select one option in Mapping Status Filters dropdown
    And Close the Filters dropdown
    And User should verify count of job profiles is correctly showing on top of Job Profiles listing table
    
  @ValidateFiltersAfterProfileManagerNavigation
  Scenario: Verify Mapping Status filters persist after navigating to Profile Manager and back
    When User is in Job Mapping page with Mapping Status filters applied
    Then Click on KFONE Global Menu in Job Mapping UI
    Then Click on Profile Manager application button in KFONE Global Menu
    Then Verify user should land on Profile Manager dashboard page
    When User is on Profile Manager page
    Then Navigate to Job Mapping page from KFONE Global Menu in PM
    Then User should be landed on Job Mapping page
    Then Verify Applied Filters persist on Job Mapping UI

