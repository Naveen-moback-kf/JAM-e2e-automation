@Verify_Jobs_with_No_BIC_Mappings
Feature: Verify Jobs that have No BIC Mappings for the Organization Profiles in Job Mapping
  
  @Navigate_To_Job_Mapping
  Scenario: Navigate to Job Mapping page 
    Then Navigate to Job Mapping page from KFONE Global Menu in PM
    Then User should verify Job Mapping logo is displayed on screen

  @SortByOrganizationNameDescending
  Scenario: Apply descending order sorting on Organization Job Name
    When User is in Job Mapping page
    Then Sort Job Profiles by Organization Job Name in Descending order

  @VerifyUnmappedJobsAtTopAfterSorting
  Scenario: Verify jobs with no BIC mappings display at top after sorting by Organization Name descending
    When User is in Job Mapping page
    Then Verify Profile with No BIC Mapping is displaying on Top after sorting