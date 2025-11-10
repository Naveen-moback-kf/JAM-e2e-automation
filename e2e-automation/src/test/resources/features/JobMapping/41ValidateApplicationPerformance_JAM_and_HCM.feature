@PerformanceTest_JAM_HCM
Feature: Validate Application Performance of Job Mapping and HCM screens

  This feature validates the performance and responsiveness of the Job Mapping application
  across various operations including page loads, data fetching, filtering, searching, and bulk operations.


  @Validate_Page_Load_Performance
  Scenario: Navigate to Job Mapping page and Validate Initial Page Load Performance
    Then Navigate to Job Mapping page from KFONE Global Menu in PM
    And User measures the time taken to load Job Mapping page
    Then User validates page load time is within acceptable threshold
    And User should verify Job Mapping logo is displayed on screen
    And User verifies all critical page components are loaded
    
  @Validate_Search_Performance
  Scenario: Validate Search Functionality Performance
    Given User is on Job Mapping page with loaded profiles
    When User measures time to perform search with dynamic keyword
    Then User validates search response time is within acceptable threshold
    And User verifies search results are accurate
    And User validates search suggestions appear instantly
    
  @Validate_Clear_Search_Performance
  Scenario: Validate Clear Search Performance
    Given User has performed search operation with filtered results
    When User measures time to clear search
    Then User validates clear search time is within acceptable threshold
    And User verifies all profiles are restored correctly
    And User validates UI remains responsive during clear operation
    
  @Validate_Single_Filter_Performance
  Scenario: Validate Filter Application Performance with Single Filter
    Given User is on Job Mapping page with loaded profiles
    When User measures time to open Filters dropdown
    And User measures time to apply single filter dynamically
    Then User validates filter application time is within acceptable threshold
    And User verifies filtered results are displayed correctly
    And User validates UI remains responsive during filter operation
    
  @Validate_Multiple_Filters_Performance
  Scenario: Validate Filter Application Performance with Multiple Filters
    Given User is on Job Mapping page with loaded profiles
    When User measures time to apply multiple filters dynamically from available options
    Then User validates multiple filter application time is within acceptable threshold
    And User verifies combined filtered results are displayed correctly
    And User validates no UI lag during multi-filter operation
    
  @Validate_Clear_Filters_Performance
  Scenario: Validate Clear Filters Performance
    Given User has applied filters and has filtered results
    When User measures time to click Clear All Filters button
    Then User validates clear filters operation time is within acceptable threshold
    And User verifies all profiles are restored correctly after clearing filters
    And User validates no UI freeze during filter clearing
    
  @Validate_Scroll_And_Lazy_Load_Performance
  Scenario: Validate Scroll and Lazy Load Performance
    Given User is on Job Mapping page with loaded profiles
    When User measures time to scroll through all profiles
    And User validates lazy loading triggers at appropriate intervals
    Then User verifies scroll performance is smooth without lag
    And User validates newly loaded profiles render within acceptable time
    And User verifies total time to load all profiles via scrolling

  @Validate_Screen_Navigation_Performance
  Scenario: Validate Navigation Between Job Mapping and Job Comparison Screen Performance
    Given User is on Job Mapping page with loaded profiles
    When User measures time to navigate to Job Comparison screen from Job Mapping
    Then User validates navigation time is within acceptable threshold
    And User verifies Job Comparison screen loads without delay
    When User measures time to navigate back to Job Mapping screen
    Then User validates back navigation time is within acceptable threshold
    And User verifies Job Mapping screen loads correctly after navigation
  
  @Validate_Sort_Performance
  Scenario: Validate Sort Operation Performance on Large Dataset
    Given User is on Job Mapping page with maximum loaded profiles
    When User measures time to sort profiles by Job Title
    And User measures time to sort profiles by Grade
    Then User validates sorting operation time is within acceptable threshold
    And User verifies sorted results are accurate
    And User validates UI remains responsive during sorting
  
  @Validate_Select_All_Performance
  Scenario: Validate Select All Profiles Performance with Large Dataset
    Given User is on Job Mapping page with maximum loaded profiles
    When User measures time to click chevron button beside header checkbox
    And User measures time to click Select All option
    Then User validates select all operation time is within acceptable threshold
    And User verifies all profiles are selected
    And User validates UI remains responsive during bulk selection
  
  @Validate_HCM_Page_Load_Performance
  Scenario: Validate HCM Sync Profiles Page Load Performance
    Given User is in Job Mapping page
    When User navigates to Profile Manager screen
    And User clicks on HCM Sync Profiles tab
    And User measures time to load HCM Sync Profiles page
    Then User validates HCM page load time is within acceptable threshold
    And User verifies all HCM profiles are loaded correctly
  
  @Validate_HCM_Sync_Performance
  Scenario: Validate Sync Operation Performance in HCM Sync Profiles
    Given User is on HCM Sync Profiles page with selected profiles
    When User measures time to click Sync Selected Profiles button
    And User validates sync operation processing time
    Then User verifies sync operation completes within acceptable threshold
    And User validates sync status updates appear promptly


