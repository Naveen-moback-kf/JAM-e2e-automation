@Verify_Jobs_Missing_Data_Tip_Message
Feature: Verify Missing Data Tip Message in Job Mapping UI
  
  @Navigate_To_Job_Mapping
  Scenario: Navigate to Job Mapping page 
    Then Navigate to Job Mapping page from KFONE Global Menu in PM
    Then User should verify Job Mapping logo is displayed on screen
    
  @Verify_Missing_Data_Tip_Message_Core
  Scenario: Verify Missing Data Tip Message displays correctly
    When User is in Job Mapping page
    Then Verify Missing Data Tip Message is displaying on Job Mapping page
    And Verify Missing Data Tip Message contains correct count of jobs with missing data
    And Verify Missing Data Tip Message contains text about jobs having missing data
    And Verify Missing Data Tip Message contains text about reduced match accuracy
    
  @Verify_Missing_Data_Tip_Message_Functions
  Scenario: Verify Missing Data Tip Message interactive elements
    When User is in Job Mapping page
    Then Verify Missing Data Tip Message is displaying on Job Mapping page
    And Verify "View & Re-upload jobs" link is present in Missing Data Tip Message
    When Click on "View & Re-upload jobs" link in Missing Data Tip Message
    Then Verify user is navigated to appropriate page for viewing and re-uploading jobs
    And Navigate back to Job Mapping page
    Then Verify Missing Data Tip Message is displaying on Job Mapping page
    When Click on Close button in Missing Data Tip Message
    Then Verify Missing Data Tip Message is no longer displayed on Job Mapping page
    
  
