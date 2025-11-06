@Validate_Search_Results_in_Screen1
Feature: Validate Search Results in Job Mapping Screen

  @Navigate_To_Job_Mapping
  Scenario: Navigate to Job Mapping page 
    Then Navigate to Job Mapping page from KFONE Global Menu in PM
    Then User should verify Job Mapping logo is displayed on screen

  @SearchJobProfileWithJobNameSubstring
  Scenario: Search for the Job Profile using Job Name Substring
    When User is in Job Mapping page
    Then Verify job profiles count is displaying on the page
    Then Verify Organization Jobs Search bar text box is clickable
    Then Verify Organization Jobs Search bar placeholder text
    Then Enter Job name substring in search bar
    Then User should verify count of job profiles is correctly showing on top of Job Profiles listing table

  @ValidateSearchResults_UnpublishedJobs
  Scenario: Validate Un-Published Job Profiles Search Results listing table contains Searched substring in Organization Job Name
    # Note: These steps will be automatically skipped if search returns "Showing 0 of X results" (no results found)
    Then User should scroll down to view last search result
    Then User should validate all search results contains substring used for searching

  @ValidateSearchResults_PublishedJobs
  Scenario: Validate Published Job Profiles Search Results listing table contains Searched substring in Organization Job Name
    When User is in Job Mapping page
    Then Click on View Published toggle button to turn on
    Then Verify job profiles count is displaying on the page
    Then Enter Job name substring in search bar
    Then User should verify count of job profiles is correctly showing on top of Job Profiles listing table
    # Note: These steps will be automatically skipped if search returns "Showing 0 of X results" (no results found)
    Then User should scroll down to view last search result
    Then User should validate all search results contains substring used for searching
