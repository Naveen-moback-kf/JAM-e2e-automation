@SelectAll_With_Search_JAM
Feature: Validate Select All and Loaded Profiles Selection with Search - JAM (Job Mapping)
  
  @Navigate_To_Job_Mapping
  Scenario: Navigate to Job Mapping page 
    Then Navigate to Job Mapping page from KFONE Global Menu in PM
    Then User should verify Job Mapping logo is displayed on screen

  @SelectAll_Search_JAM @SelectAll_JAM
  Scenario: JAM - Search and Select All Profiles
    When User is in Job Mapping page
    Then Verify job profiles count is displaying on the page
    Then Verify Organization Jobs Search bar text box is clickable
    Then Verify Organization Jobs Search bar placeholder text
    Then Enter Job name substring in search bar
    Then User should verify count of job profiles is correctly showing on top of Job Profiles listing table
    Then User should scroll down to view last search result in "JAM" screen
    Then User should validate all search results contains substring used for searching in "JAM" screen
    Then Click on Chevron button beside header checkbox in "JAM" screen
    Then Click on Select All button in "JAM" screen
    Then User should verify action button is enabled in "JAM" screen
    Then Capture baseline of selected profiles in "JAM" screen
    Then Clear Search bar in "JAM" screen

  @SelectAll_Search_JAM_Alternative @SelectAll_JAM
  Scenario: JAM - Alternative Validation with Second Search
    When User is in Job Mapping page
    Then Enter different Job name substring in search bar for alternative validation in "JAM" screen
    Then User should verify count of job profiles is correctly showing on top of Job Profiles listing table
    Then Validate second search results in "JAM" screen
    Then Verify all loaded profiles in second search are NOT selected in "JAM" screen
    Then Clear Search bar in "JAM" screen

  # ═══════════════════════════════════════════════════════════════════════════════
  # Loaded Profiles Selection with Search
  # ═══════════════════════════════════════════════════════════════════════════════

  @LoadedProfiles_Search_JAM @LoadedProfiles_JAM
  Scenario: JAM - Navigate to Job Mapping and Select Loaded Profiles with Search
    When User is in Job Mapping page
    Then Refresh Job Mapping page
    Then User should verify Job Mapping logo is displayed on screen
    Then Verify job profiles count is displaying on the page
    Then Verify Organization Jobs Search bar text box is clickable
    Then Verify Organization Jobs Search bar placeholder text
    Then Enter Job name substring in search bar
    Then User should verify count of job profiles is correctly showing on top of Job Profiles listing table
    Then Select loaded job profiles using header checkbox in "JAM" screen
    Then Scroll to load more profiles in "JAM" screen
    Then Verify newly loaded profiles after header checkbox are NOT selected in "JAM" screen
    Then Capture baseline of selected profiles in "JAM" screen
    Then Clear Search bar in "JAM" screen
    Then User should verify action button is enabled in "JAM" screen
    Then Verify only searched profiles are selected after clearing search bar in "JAM" screen

