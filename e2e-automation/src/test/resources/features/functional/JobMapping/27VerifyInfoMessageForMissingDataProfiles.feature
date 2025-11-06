@Verify_Info_Message_Missing_Data_Profiles  @Smoke_Test
Feature: Verify Info Message for Multiple Profiles with Missing Data

  @Navigate_To_Job_Mapping
  Scenario: Navigate to Job Mapping page 
    Then Navigate to Job Mapping page from KFONE Global Menu in PM
    Then User should verify Job Mapping logo is displayed on screen
    
  @Verify_Profile_Info_Message
  Scenario: Verify Info Message displays on first profile with missing data
    # Validates info message presence on first profile with missing Grade/Department/Function/Subfunction
    When User is in Job Mapping page
    Then Find and verify profile with missing data has Info Message displayed
    And Verify Info Message contains text about reduced match accuracy due to missing data
  
  @Verify_Job_Details_Extraction_And_Comparison
  Scenario: Verify job details extraction and comparison for first profile between Job Mapping and Job Comparison pages
    # Validates that job details (Name, Code, Grade, Department, Function/Sub-function) are accurately extracted from Job Mapping page and match on Job Comparison page for first profile
    When User is in Job Mapping page
    Then Find profile with missing data and Info Message
    And Extract job details from profile with Info Message
    And Click on "View Other Matches" button for profile with Info Message
    Then Verify user is navigated to Job Comparison page
    And Extract job details from Job Comparison page
    And Verify job details match between Job Mapping and Job Comparison pages
   
  @Verify_Info_Message_Persistence
  Scenario: Verify Info Message persists in Job Comparison page for first profile
    # Validates info message persistence when navigating to Job Comparison via "View Other Matches" for first profile
    When User is in Job Comparison Page
    Then Verify Info Message is still displayed in Job Comparison page
    And Verify Info Message contains same text about reduced match accuracy
    And Navigate back to Job Mapping page from Job Comparison
  
  @Verify_Second_Profile_Info_Message
  Scenario: Verify Info Message displays on second profile with missing data
    # Validates info message presence on second profile with missing Grade/Department/Function/Subfunction
    When User is in Job Mapping page
    Then Find and verify second profile with missing data has Info Message displayed
    And Verify Info Message contains text about reduced match accuracy due to missing data for second profile  
  
  @Verify_Second_Job_Details_Extraction_And_Comparison
  Scenario: Verify job details extraction and comparison for second profile between Job Mapping and Job Comparison pages
    # Validates that job details (Name, Code, Grade, Department, Function/Sub-function) are accurately extracted from Job Mapping page and match on Job Comparison page for second profile
    When User is in Job Mapping page
    Then Find second profile with missing data and Info Message
    And Extract job details from second profile with Info Message
    And Click on "View Other Matches" button for second profile with Info Message
    Then Verify user is navigated to Job Comparison page
    And Extract job details from Job Comparison page for second profile
    And Verify job details match between Job Mapping and Job Comparison pages for second profile
      
  @Verify_Second_Info_Message_Persistence
  Scenario: Verify Info Message persists in Job Comparison page for second profile
    # Validates info message persistence when navigating to Job Comparison via "View Other Matches" for second profile
    When User is in Job Comparison Page
    And Verify Info Message is still displayed in Job Comparison page for second profile
    And Verify Info Message contains same text about reduced match accuracy for second profile
    
