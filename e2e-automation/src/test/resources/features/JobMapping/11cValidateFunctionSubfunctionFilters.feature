@Validate_Function_Subfunction_Filters
Feature: Validate Function and Subfunction Filters Functionality in Job Mapping UI

  @Navigate_To_Job_Mapping
  Scenario: Navigate to Job Mapping page 
    Then Navigate to Job Mapping page from KFONE Global Menu in PM
    Then User should verify Job Mapping logo is displayed on screen
  
  @ApplyFunctionFilterWithAutoSelectSubfunctions
  Scenario: User filters by function and verifies all subfunctions are auto-selected
    When User is in Job Mapping page
    Then Verify job profiles count is displaying on the page
    Then Click on Filters dropdown button
    Then Click on Functions Subfunctions Filters dropdown button
    Then Select a Function and verify all Subfunctions inside Function are selected automatically
    And Close the Filters dropdown
    And User should verify count of job profiles is correctly showing on top of Job Profiles listing table
    Then User should scroll down to view last result with applied filters
    Then Validate Job Mapping Profiles are correctly filtered with applied Functions Subfunctions Options
    Then Click on Clear Filters button

  @ValidateFunctionSubfunctionSearchFunctionality
  Scenario: User validates search bar functionality in Functions Subfunctions filter
    When User is in Job Mapping page
    Then Verify job profiles count is displaying on the page
    Then Click on Filters dropdown button
    Then Click on Functions Subfunctions Filters dropdown button
    Then User should verify Search bar is available in Functions Subfunctions Filters dropdown
    Then Click inside search bar and enter function name
    Then User should click on dropdown button of Searched function name
    And Close the Filters dropdown

  @ApplySpecificSubfunctionFilters
  Scenario: User filters by specific subfunctions within a function
    When User is in Job Mapping page
    Then Verify job profiles count is displaying on the page
    Then Click on Filters dropdown button
    Then Click on Functions Subfunctions Filters dropdown button
    Then Click inside search bar and enter function name
    Then User should click on dropdown button of Searched function name
    And Select one Subfunction option inside Function Name dropdown
    Then User should verify Function Name is automatically selected after selecting Subfunction option
    And Close the Filters dropdown
    And User should verify count of job profiles is correctly showing on top of Job Profiles listing table
    Then User should scroll down to view last result with applied filters
    Then Validate Job Mapping Profiles are correctly filtered with applied Functions Subfunctions Options
    Then Click on Clear Filters button

  @ApplyMultipleSubfunctionFilters
  Scenario: User filters by multiple subfunctions within a function
    When User is in Job Mapping page
    Then Verify job profiles count is displaying on the page
    Then Click on Filters dropdown button
    Then Click on Functions Subfunctions Filters dropdown button
    Then Click inside search bar and enter function name
    Then User should click on dropdown button of Searched function name
    And Select two subfunction options inside Function Name Filters dropdown
    And Close the Filters dropdown
    And User should verify count of job profiles is correctly showing on top of Job Profiles listing table
    Then User should scroll down to view last result with applied filters
    Then Validate Job Mapping Profiles are correctly filtered with applied Functions Subfunctions Options
    Then Click on Clear Filters button

  @ValidateClearFunctionSubfunctionFilter
  Scenario: User clears individual function subfunction filter using clear(x) button
    When User is in Job Mapping page
    Then Verify job profiles count is displaying on the page
    Then Click on Filters dropdown button
    Then Click on Functions Subfunctions Filters dropdown button
    Then Select a Function and verify all Subfunctions inside Function are selected automatically
    And Close the Filters dropdown
    And User should verify count of job profiles is correctly showing on top of Job Profiles listing table
    Then Click on clear(x) applied filter
    Then Verify job profiles count is displaying on the page

