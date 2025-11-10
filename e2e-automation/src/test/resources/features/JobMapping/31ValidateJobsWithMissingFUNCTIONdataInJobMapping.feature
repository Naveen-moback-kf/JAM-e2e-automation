@Validate_Jobs_With_Missing_FUNCTION_Data_In_JobMapping
Feature: Validate Jobs with Missing FUNCTION Data in Job Mapping UI

  @Navigate_To_Job_Mapping
  Scenario: Navigate to Job Mapping page 
    Then Navigate to Job Mapping page from KFONE Global Menu in PM
    Then User should verify Job Mapping logo is displayed on screen

  @Forward_Validate_Missing_FUNCTION_Flow
  Scenario: Forward Flow - Find job with missing FUNCTION in Job Mapping first, then validate in Jobs Missing Data screen
    # Forward workflow: Start from Job Mapping page → find missing Function job (- | -) → navigate to Missing Data screen → validate same job exists
    When User is in Job Mapping page
    Then Find job profile in Job Mapping page where Function is missing
    And Extract FUNCTION job details from found profile in Job Mapping page
    Then Click on "View & Re-upload jobs" link in Missing Data Tip Message
    Then Verify user is navigated to Jobs with Missing FUNCTION Data screen
    And Search for the extracted FUNCTION job profile by name in Jobs Missing Data screen
    Then Verify FUNCTION job profile is found and displayed in Jobs Missing Data screen search results
    And Extract FUNCTION job details from found profile in Jobs Missing Data screen
    And Verify all FUNCTION job details match between Job Mapping page and Jobs Missing Data screen
    And Click on Close button to return to Job Mapping page from FUNCTION validation
    Then Verify user is back on Job Mapping page after FUNCTION validation
    
  @Reverse_Validate_Missing_FUNCTION_Flow
  Scenario: Reverse Flow - Validate jobs with missing FUNCTION data from Missing Data screen to Job Mapping
    # Reverse workflow: Start from Job Mapping page → navigate to Missing Data screen → find job with missing Function (N/A) → validate details match
    When User is in Job Mapping page
    Then User should verify Job Mapping logo is displayed on screen
    Then Click on "View & Re-upload jobs" link in Missing Data Tip Message
    Then Verify user is navigated to Jobs with Missing FUNCTION Data screen
    And Find job in Jobs Missing FUNCTION Data screen where Function is N/A
    And Extract all available job details from Jobs with Missing FUNCTION Data screen
    And Click on Close button to return to Job Mapping page from FUNCTION validation
    Then Verify user is back on Job Mapping page after FUNCTION validation
    When Search for the extracted FUNCTION job profile by name in Job Mapping page
    Then Verify FUNCTION job profile is found and displayed in search results
    And Extract FUNCTION job details from searched profile in Job Mapping page
    And Verify all FUNCTION job details match between Jobs Missing Data screen and Job Mapping page
    And Verify Info Message is displayed on searched profile indicating missing FUNCTION data
    And Verify Info Message contains text about reduced match accuracy due to missing FUNCTION data

