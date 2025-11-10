@Validate_Add_More_Jobs_Functionality
Feature: Validate Add More Jobs functionality in AI Auto Job Mapping Page

  @Navigate_To_Job_Mapping
  Scenario: Navigate to Job Mapping page 
    Then Navigate to Job Mapping page from KFONE Global Menu in PM

  @AddmorejobsButton
  Scenario: User accesses job upload functionality through Add More Jobs button
    When User is in Job Mapping page
    Then Verify Unpublished Jobs count before adding more jobs
    Then User should see Add more jobs button is displaying
    Then Verify Add more jobs button is clickable
    Then User should be landed on KFONE Add Job Data page

  @UploadJobCatalogFile
  Scenario: User uploads job catalog file through manual upload
    When User is in KFONE Add Job Data page
    Then User should click on Manual Upload button
    #Then User should verify Job Catalog template file can be downloaded
    Then Verify Jobs count in KFONE Add Job Data screen before adding more jobs
    #Then Verify Last Synced Info on Add Job Data screen before adding more jobs
    Then Click on Done button in KFONE Add Job Data page
    Then Upload Job Catalog file using Attach File button
    Then User should verify File Close button displaying and clickable
    Then Upload Job Catalog file using Attach File button
    Then Click on Continue button in Add Job data screen

  @ValidateJobDataUploadSuccess
  Scenario: User validates successful job data upload and verifies job count changes
    When User is in KFONE Add Job Data page after uploading file
    Then User should Validate Job Data Upload is in Progress
    Then User should validate Job Data added successfully
    Then Verify Jobs count in KFONE Add Job Data screen after adding more jobs
    #Then Verify Last Synced Info on Add Job Data screen after adding more jobs
    Then Close Add Job Data screen
    Then User should be landed on Job Mapping page
    Then Verify Unpublished Jobs count after adding more jobs
