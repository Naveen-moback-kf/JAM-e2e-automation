@Validate_Department_Filters
Feature: Validate Department Filters Functionality in Job Mapping UI

  @Navigate_To_Job_Mapping
  Scenario: Navigate to Job Mapping page 
    Then Navigate to Job Mapping page from KFONE Global Menu in PM
    Then User should verify Job Mapping logo is displayed on screen
  
  @ApplyandValidateSingleDepartmentFilter
  Scenario: User filters job profiles by single department option
    When User is in Job Mapping page
    Then Verify job profiles count is displaying on the page
    Then Click on Filters dropdown button
    Then Click on Departments Filters dropdown button
    And Select one option in Departments Filters dropdown
    And Close the Filters dropdown
    And User should verify count of job profiles is correctly showing on top of Job Profiles listing table
    Then User should scroll down to view last result with applied filters
    Then Validate Job Mapping Profiles are correctly filtered with applied Departments Options
    Then Click on Clear Filters button

  @ApplyandValidateMultipleDepartmentsFilters
  Scenario: User filters job profiles by multiple department options
    When User is in Job Mapping page
    Then Verify job profiles count is displaying on the page
    Then Click on Filters dropdown button
    Then Click on Departments Filters dropdown button
    And Select two options in Departments Filters dropdown
    And Close the Filters dropdown
    And User should verify count of job profiles is correctly showing on top of Job Profiles listing table
    Then User should scroll down to view last result with applied filters
    Then Validate Job Mapping Profiles are correctly filtered with applied Departments Options
    Then Click on Clear Filters button

  @ValidateClearSingleDepartmentFilter
  Scenario: User clears individual department filter using clear(x) button
    When User is in Job Mapping page
    Then Verify job profiles count is displaying on the page
    Then Click on Filters dropdown button
    Then Click on Departments Filters dropdown button
    And Select one option in Departments Filters dropdown
    And Close the Filters dropdown
    And User should verify count of job profiles is correctly showing on top of Job Profiles listing table
    Then Click on clear(x) applied filter
    Then Verify job profiles count is displaying on the page

