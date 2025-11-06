@Validate_Jobs_With_Missing_DEPARTMENT_Data_In_JobMapping
Feature: Validate Jobs with Missing DEPARTMENT Data in Job Mapping UI

  @Navigate_To_Job_Mapping
  Scenario: Navigate to Job Mapping page 
    Then Navigate to Job Mapping page from KFONE Global Menu in PM
    Then User should verify Job Mapping logo is displayed on screen
    
  @Forward_Validate_Missing_DEPARTMENT_Flow
  Scenario: Forward Flow - Find job with missing DEPARTMENT in Job Mapping first, then validate in Jobs Missing Data screen
    # Forward workflow: Start from Job Mapping page → find missing Department job with info message → navigate to Missing Data screen → validate same job exists
    When User is in Job Mapping page
    Then Sort Job Profiles by Department in Ascending order
    Then Find job profile in Job Mapping page where Department is missing
    And Extract DEPARTMENT job details from found profile in Job Mapping page
    Then Click on "View & Re-upload jobs" link in Missing Data Tip Message
    Then Verify user is navigated to Jobs with Missing DEPARTMENT Data screen
    And Search for the extracted DEPARTMENT job profile by name in Jobs Missing Data screen
    Then Verify DEPARTMENT job profile is found and displayed in Jobs Missing Data screen search results
    And Extract DEPARTMENT job details from found profile in Jobs Missing Data screen
    And Verify all DEPARTMENT job details match between Job Mapping page and Jobs Missing Data screen
    And Click on Close button to return to Job Mapping page from DEPARTMENT validation
    Then Verify user is back on Job Mapping page after DEPARTMENT validation
    
  @Reverse_Validate_Missing_DEPARTMENT_Flow
  Scenario: Reverse Flow - Validate jobs with missing DEPARTMENT data from Missing Data screen to Job Mapping
    # Reverse workflow: Start from Job Mapping page → navigate to Missing Data screen → find job with missing Department → validate details match
    When User is in Job Mapping page
    Then User should verify Job Mapping logo is displayed on screen
    Then Click on "View & Re-upload jobs" link in Missing Data Tip Message
    Then Verify user is navigated to Jobs with Missing DEPARTMENT Data screen
    And Find job in Jobs Missing DEPARTMENT Data screen where Department is N/A
    And Extract all available job details from Jobs with Missing DEPARTMENT Data screen
    And Click on Close button to return to Job Mapping page from DEPARTMENT validation
    Then Verify user is back on Job Mapping page after DEPARTMENT validation
    When Search for the extracted DEPARTMENT job profile by name in Job Mapping page
    Then Verify DEPARTMENT job profile is found and displayed in search results
    And Extract DEPARTMENT job details from searched profile in Job Mapping page
    And Verify all DEPARTMENT job details match between Jobs Missing Data screen and Job Mapping page
    And Verify Info Message is displayed on searched profile indicating missing DEPARTMENT data
    And Verify Info Message contains text about reduced match accuracy due to missing DEPARTMENT data

