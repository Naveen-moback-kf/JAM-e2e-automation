@Selection_of_Unmapped_jobs_JAM
Feature: Verify Unmapped jobs cannot be selected on Job Mapping screen

  @Navigate_To_Job_Mapping
  Scenario: Navigate to Job Mapping page 
    Then Navigate to Job Mapping page from KFONE Global Menu in PM
    Then User should verify Job Mapping logo is displayed on screen
  
  @ApplyUnmappedJobsFilter_JAM
  Scenario: Apply Unmapped Jobs filter in Job Mapping screen
  #Note: Skip this scenario if No Unmapped Jobs option in Mapping Status Filters dropdown
    When User is in Job Mapping page
    Then Verify job profiles count is displaying on the page
    Then Click on Filters dropdown button
    Then Click on Mapping Status Filters dropdown button
    And Select Unmapped jobs option in Mapping Status Filters dropdown
    And Close the Filters dropdown
    And User should verify count of job profiles is correctly showing on top of Job Profiles listing table
   
  @VerifyHeaderCheckboxDisabled_JAM
  Scenario: Verify Header Checkbox and Chevron button Disabled in Job Mapping screen
  #Note: Skip this scenario if No Unmapped Jobs option in Mapping Status Filters dropdown
  	When User is in Job Mapping page with Mapping Status filters applied
  	Then Verify Header Checkbox is disabled in Job Mapping screen
  	Then Verify Chevron button is disabled in Job Mapping screen
  	Then User should verify Publish Selected Profiles button is disabled
  	Then User should scroll down to view last result with applied filters
  	
  @VerifyToolTipOfUnmappedJobs_JAM
  Scenario: Verify Checkbox of each Unmapped Job is Disabled and have ToolTip
  #Note: Skip this scenario if No Unmapped Jobs option in Mapping Status Filters dropdown
    When User is in Job Mapping page with Mapping Status filters applied
    Then Verify Checkbox of all Unmapped Jobs is disabled with ToolTip
    Then Click on Clear Filters button
    Then Verify job profiles count is displaying on the page
