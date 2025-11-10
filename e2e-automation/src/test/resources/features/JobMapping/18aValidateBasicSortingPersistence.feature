@Validate_Basic_Sorting_Persistence
Feature: Validate Basic Sorting Persistence in Job Mapping UI

  @Navigate_To_Job_Mapping
  Scenario: Navigate to Job Mapping page 
    Then Navigate to Job Mapping page from KFONE Global Menu in PM
    Then User should verify Job Mapping logo is displayed on screen
    
  @ApplyMultiLevelSorting
  Scenario: Apply multi-level sorting by Organization Grade and Job Name
    When User is in Job Mapping page
    Then Verify job profiles count is displaying on the page
    Then Sort Job Profiles by Organization Grade in Ascending order
    Then Sort Job Profiles by Organization Job Name in Ascending order

  @ValidateSortingAfterPageRefresh
  Scenario: Verify multi-level sorting persists after page refresh
    When User is in Job Mapping page with multi-level sorting applied
    Then Refresh Job Mapping page
    And Verify Applied Sorting persist on Job Mapping UI

  @ValidateSortingAfterJobComparisonNavigation
  Scenario: Verify sorting persists after navigating to Job Comparison page and back
    When User is in Job Mapping page with multi-level sorting applied
    Then Search for Job Profile with View Other Matches button
    Then Click on View Other Matches button
    Then Verify user navigated to job comparison page
    Then Click on Browser back button
    Then User should be landed on Job Mapping page
    When User is in Job Mapping page
    And Verify Applied Sorting persist on Job Mapping UI

  @ValidateSortingAfterProfileManagerNavigation
  Scenario: Verify sorting persists after navigating to Profile Manager and back
    When User is in Job Mapping page with multi-level sorting applied
    Then Click on KFONE Global Menu in Job Mapping UI
    Then Click on Profile Manager application button in KFONE Global Menu
    Then Verify user should land on Profile Manager dashboard page
    When User is on Profile Manager page
    Then Navigate to Job Mapping page from KFONE Global Menu in PM
    Then User should be landed on Job Mapping page
    Then Verify Applied Sorting persist on Job Mapping UI

