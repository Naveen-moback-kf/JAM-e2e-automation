@Validate_Mapping_Status_And_Multiple_Filters
Feature: Validate Mapping Status and Multiple Filters Functionality in Job Mapping UI

  @Navigate_To_Job_Mapping
  Scenario: Navigate to Job Mapping page 
    Then Navigate to Job Mapping page from KFONE Global Menu in PM
    Then User should verify Job Mapping logo is displayed on screen
  
  @ApplySingleMappingStatusFilter
  Scenario: User filters job profiles by single mapping status option
    When User is in Job Mapping page
    Then Verify job profiles count is displaying on the page
    Then Click on Filters dropdown button
    Then Click on Mapping Status Filters dropdown button
    And Select one option in Mapping Status Filters dropdown
    And Close the Filters dropdown
    And User should verify count of job profiles is correctly showing on top of Job Profiles listing table
    Then User should scroll down to view last result with applied filters
    Then Click on Clear Filters button

  @ValidateClearMappingStatusFilter
  Scenario: User clears individual mapping status filter using clear(x) button
    When User is in Job Mapping page
    Then Verify job profiles count is displaying on the page
    Then Click on Filters dropdown button
    Then Click on Mapping Status Filters dropdown button
    And Select one option in Mapping Status Filters dropdown
    And Close the Filters dropdown
    And User should verify count of job profiles is correctly showing on top of Job Profiles listing table
    Then Click on clear(x) applied filter
    Then Verify job profiles count is displaying on the page
    
  @ApplyMultipleFiltersInViewPublished
  Scenario: Apply multiple filters in View Published screen
    When User is in Job Mapping page
    Then Click on View Published toggle button to turn on
    Then Verify job profiles count is displaying on the page
    Then Click on Filters dropdown button
    Then Click on Grades Filters dropdown button
    And Select one option in Grades Filters dropdown
    Then Click on Departments Filters dropdown button
    And Select one option in Departments Filters dropdown
    Then Click on Functions Subfunctions Filters dropdown button
    Then Select a Function and verify all Subfunctions inside Function are selected automatically
    And Close the Filters dropdown

  @ValidateMultipleFiltersResults
  Scenario: Validate results with multiple filters applied in View Published screen
    When User is in View Published screen with multiple filters applied
    Then Validate Job Mapping Profiles are correctly filtered with applied Grades Departments and Functions Subfunctions Options

