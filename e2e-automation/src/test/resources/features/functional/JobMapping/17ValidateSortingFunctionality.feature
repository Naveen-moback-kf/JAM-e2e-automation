@Validate_Sorting_Functionality
Feature: Validate Sorting Functionality in AI Auto Screen1

  @Navigate_To_Job_Mapping
  Scenario: Navigate to Job Mapping page 
    Then Navigate to Job Mapping page from KFONE Global Menu in PM
    Then User should verify Job Mapping logo is displayed on screen
    
  @VerifyDefaultOrderOfProfiles
  Scenario: Verify List of Profiles in their default order before applying Sorting
    When User is in Job Mapping page
    Then User should scroll page down two times to view first thirty job profiles
    Then User should verify first thirty job profiles in default order before applying sorting

  @SortByOrganizationNameAscending
  Scenario: Validate sorting by Organization Job Name in Ascending Order
    Then Sort Job Profiles by Organization Job Name in Ascending order
    Then User should scroll page down two times to view first thirty job profiles
    Then User should verify first thirty job profiles sorted by Organization Job Name in Ascending order
    Then Click on View Published toggle button to turn on
    Then Click on toggle button to turn off

  @SortByOrganizationNameDescending
  Scenario: Validate sorting by Organization Job Name in Descending Order
    Then Sort Job Profiles by Organization Job Name in Descending order
    Then User should scroll page down two times to view first thirty job profiles
    Then User should verify first thirty job profiles sorted by Organization Job Name in Descending order
    Then Click on View Published toggle button to turn on
    Then Click on toggle button to turn off

  @SortByMatchedSPGradeAscending
  Scenario: Validate sorting by Matched Success Profile Grade in Ascending Order
    Then Sort Job Profiles by Matched Success Profile Grade in Ascending order
    Then User should scroll page down two times to view first thirty job profiles
    Then User should verify first thirty job profiles sorted by Matched Success Profile Grade in Ascending order
    Then Click on View Published toggle button to turn on
    Then Click on toggle button to turn off

  @SortByMatchedSPGradeDescending
  Scenario: Validate sorting by Matched Success Profile Grade in Descending Order
    Then Sort Job Profiles by Matched Success Profile Grade in Descending order
    Then User should scroll page down two times to view first thirty job profiles
    Then User should verify first thirty job profiles sorted by Matched Success Profile Grade in Descending order
    Then Click on View Published toggle button to turn on
    Then Click on toggle button to turn off

  @SingleLevelSortingByMatchedSPNameInViewPublishedScreen
  Scenario: Validate Single level sorting by Matched Success Profile Name in Ascending Order on Job Profiles in View Published screen
    When User is in Job Mapping page
    Then Click on View Published toggle button to turn on
    Then User should scroll page down two times to view first thirty job profiles
    Then User should verify first thirty job profiles in default order before applying sorting
    Then Sort Job Profiles by Matched Success Profile Name in Ascending order
    Then User should scroll page down two times to view first thirty job profiles
    Then User should verify first thirty job profiles sorted by Matched Success Profile Name in Ascending order
    Then Refresh Job Mapping page
    Then Click on toggle button to turn off

  @MultiLevelSortingBothAscending
  Scenario: Validate multi level sorting by Organization Grade and Job Name both in Ascending Order
    When User is in Job Mapping page
    Then User should scroll page down two times to view first thirty job profiles
    Then User should verify first thirty job profiles in default order before applying sorting
    Then Sort Job Profiles by Organization Grade in Ascending order
    Then Sort Job Profiles by Organization Job Name in Ascending order
    Then User should scroll page down two times to view first thirty job profiles
    Then User should verify first thirty job profiles sorted by Organization Grade and Organization Job Name in Ascending order

  @MultiLevelSortingGradeDescendingNameAscending
  Scenario: Validate multi level sorting by Organization Grade Descending and Job Name Ascending
    When User is in Job Mapping page
    Then User should scroll page down two times to view first thirty job profiles
    Then User should verify first thirty job profiles in default order before applying sorting
    Then Sort Job Profiles by Organization Grade in Descending order
    Then Sort Job Profiles by Organization Job Name in Ascending order
    Then User should scroll page down two times to view first thirty job profiles
    Then User should verify first thirty job profiles sorted by Organization Grade in Descending order and Organization Job Name in Ascending order
