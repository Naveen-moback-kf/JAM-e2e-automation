@Validate_Advanced_Filter_Persistence
Feature: Validate Advanced Filter Persistence in Job Mapping UI

  @Navigate_To_Job_Mapping
  Scenario: Navigate to Job Mapping page 
    Then Navigate to Job Mapping page from KFONE Global Menu in PM
    Then User should verify Job Mapping logo is displayed on screen
    
  @ApplyMappingStatusFilters
  Scenario: Apply Mapping Status filters in Job Mapping page
    When User is in Job Mapping page
    Then Verify job profiles count is displaying on the page
    Then Click on Filters dropdown button
    Then Click on Mapping Status Filters dropdown button
    And Select one option in Mapping Status Filters dropdown
    And Close the Filters dropdown
    And User should verify count of job profiles is correctly showing on top of Job Profiles listing table

  @ValidateFiltersAfterManualMappingNavigation
  Scenario: Verify Mapping Status filters persist after navigating to Manual Job Mapping screen and back
   	When User is in Job Mapping page with Mapping Status filters applied
    Then User should search for Job Profile with Search a Different Profile button on Mapped Success Profile
    Then Click on Search a Different Profile button on Mapped Success Profile
    Then User should be navigated to Manual Job Mapping screen
    Then Click on Browser back button
    Then User should be landed on Job Mapping page
    When User is in Job Mapping page
    And Verify Applied Filters persist on Job Mapping UI
    Then Click on Clear Filters button

  @ApplyFiltersInViewPublishedScreen
  Scenario: Apply Grades filters in View Published screen
    When User is in Job Mapping page
    Then Click on View Published toggle button to turn on
    Then Verify job profiles count is displaying on the page
    Then Click on Filters dropdown button
    Then Click on Grades Filters dropdown button
    And Select two options in Grades Filters dropdown
    And Close the Filters dropdown
    And User should verify count of job profiles is correctly showing on top of Job Profiles listing table
    Then User should scroll page down two times to view first thirty job profiles

  @ValidateToggleAndFiltersAfterRefresh
  Scenario: Verify View Published toggle and filters persist after page refresh
    When User is in View Published screen with Grades filters applied
    Then Refresh Job Mapping page
    Then Verify View Published toggle button is Persisted
    And Verify Applied Filters persist on Job Mapping UI

  @ValidateToggleAndFiltersAfterPMNavigation
  Scenario: Verify View Published toggle and filters persist after Profile Manager navigation
    When User is in View Published screen with Grades filters applied
    Then Click on KFONE Global Menu in Job Mapping UI
    Then Click on Profile Manager application button in KFONE Global Menu
    Then Verify user should land on Profile Manager dashboard page
    When User is on Profile Manager page
    Then Navigate to Job Mapping page from KFONE Global Menu in PM
    Then User should be landed on Job Mapping page
    Then Verify View Published toggle button is Persisted
    Then Verify Applied Filters persist on Job Mapping UI
    Then Click on Clear Filters button

