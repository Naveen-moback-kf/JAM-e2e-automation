@DeleteJobProfiles_JAM
Feature: Validate Single and Mass Delete of Job Profiles in Job Mapping

  @Navigate_To_Job_Mapping
  Scenario: Navigate to Job Mapping page
    Then Navigate to Job Mapping page from KFONE Global Menu in PM
    Then User should verify Job Mapping logo is displayed on screen
    
  @SingleJobDelete
  Scenario: Select and Delete Single Job in Job Mapping
  	When User is in Job Mapping page
  	Then User should verify Delete button is disabled
  	Then Click on checkbox of first job profile
  	Then User should verify Delete button is enabled
  	Then Click on Delete button in Job Mapping
  	Then Verify Delete Confirmation popup is displayed
  	Then Click on Cancel button on Delete Confirmation popup
  	Then User should verify Delete button is enabled
  	Then Click on Delete button in Job Mapping
  	Then Verify Delete Confirmation popup is displayed
  	Then Click on Delete button on Delete Confirmation popup
  	Then User should verify delete success popup appears on screen
  	
  @SingleJobDeleteWithSearch
  Scenario: Search for Job Profiles and Delete First Job from Search Results
    When User is in Job Mapping page
    Then Verify job profiles count is displaying on the page
    Then Verify Organization Jobs Search bar text box is clickable
    Then Verify Organization Jobs Search bar placeholder text
    Then Enter Job name substring in search bar
    Then User should verify count of job profiles is correctly showing on top of Job Profiles listing table
    Then User should verify Delete button is disabled
  	Then Click on checkbox of first job profile
  	Then User should verify Delete button is enabled
  	Then Click on Delete button in Job Mapping
  	Then Verify Delete Confirmation popup is displayed
  	Then Click on Delete button on Delete Confirmation popup
  	Then User should verify delete success popup appears on screen
  	
  @MassJobDeleteWithFilters
  Scenario: Filter for Job Profiles and Delete Loaded from Filtered Results
  	When User is in Job Mapping page
    Then Verify job profiles count is displaying on the page
    Then Click on Filters dropdown button
    Then Click on Grades Filters dropdown button
    And Select two options in Grades Filters dropdown
    And Close the Filters dropdown
    And User should verify count of job profiles is correctly showing on top of Job Profiles listing table
    Then User should verify Delete button is disabled
    Then Click on header checkbox to select loaded job profiles in Job Mapping screen
    Then Scroll page to view more job profiles
    Then Verify job profiles count is displaying on the page
    Then Verify Profiles loaded after clicking Header checkbox are not selected in Job Mapping Screen
    Then User should verify Delete button is enabled
  	Then Click on Delete button in Job Mapping
  	Then Verify Delete Confirmation popup is displayed
  	Then Click on Delete button on Delete Confirmation popup
  	Then User should verify delete success popup appears on screen
  	Then User should verify count of job profiles is correctly showing on top of Job Profiles listing table
  	
  @MassJobDeleteWithSearch
  Scenario: Search for Job Profiles and Mass Delete Search Results
    When User is in Job Mapping page
    Then Verify job profiles count is displaying on the page
    Then Verify Organization Jobs Search bar text box is clickable
    Then Verify Organization Jobs Search bar placeholder text
    Then Enter Job name substring in search bar
    Then User should verify count of job profiles is correctly showing on top of Job Profiles listing table
    Then User should verify Delete button is disabled
  	Then Click on Chevron button beside header checkbox in "JAM" screen
    Then Click on Select All button in "JAM" screen
  	Then User should verify Delete button is enabled
  	Then Verify job profiles count is displaying on the page
  	Then Click on Delete button in Job Mapping
  	Then Verify Delete Confirmation popup is displayed
  	Then Click on Delete button on Delete Confirmation popup
  	Then User should verify delete success popup appears on screen
  	Then User should verify count of job profiles is correctly showing on top of Job Profiles listing table
    
  	