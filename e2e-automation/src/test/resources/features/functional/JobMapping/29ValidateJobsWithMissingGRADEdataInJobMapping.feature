@Validate_Jobs_With_Missing_GRADE_Data_In_JobMapping
Feature: Validate Jobs with Missing GRADE Data in Job Mapping UI

  @Navigate_To_Job_Mapping
  Scenario: Navigate to Job Mapping page 
    Then Navigate to Job Mapping page from KFONE Global Menu in PM
    Then User should verify Job Mapping logo is displayed on screen

  @Forward_Validate_Missing_GRADE_Flow
  Scenario: Forward Flow - Find job with missing GRADE in Job Mapping first, then validate in Jobs Missing Data screen
    # Forward workflow: Start from Job Mapping page → find missing Grade job with info message → navigate to Missing Data screen → validate same job exists
    When User is in Job Mapping page
    Then Sort Job Profiles by Organization Grade in Ascending order
    Then Find job profile in Job Mapping page where Grade is missing
    And Extract job details from found profile in Job Mapping page
    Then Click on "View & Re-upload jobs" link in Missing Data Tip Message
    Then Verify user is navigated to Jobs with Missing Data screen
    And Search for the extracted job profile by name in Jobs Missing Data screen
    Then Verify job profile is found and displayed in Jobs Missing Data screen search results
    And Extract job details from found profile in Jobs Missing Data screen
    And Verify all job details match between Job Mapping page and Jobs Missing Data screen
    And Click on Close button to return to Job Mapping page
    Then Verify user is back on Job Mapping page
    
  @Reverse_Validate_Missing_GRADE_Flow
  Scenario: Reverse Flow - Validate jobs with missing GRADE data from Missing Data screen to Job Mapping
    # Reverse workflow: Start from Job Mapping page → navigate to Missing Data screen → find job with missing Grade → validate details match
    When User is in Job Mapping page
    Then User should verify Job Mapping logo is displayed on screen
    Then Click on "View & Re-upload jobs" link in Missing Data Tip Message
    Then Verify user is navigated to Jobs with Missing Data screen
    And Find job in Jobs Missing Data screen where Grade is N/A
    And Extract all available job details from Jobs with Missing Data screen
    And Click on Close button to return to Job Mapping page
    Then Verify user is back on Job Mapping page
    When Search for the extracted job profile by name in Job Mapping page
    Then Verify job profile is found and displayed in search results
    And Extract job details from searched profile in Job Mapping page
    And Verify all job details match between Jobs Missing Data screen and Job Mapping page
    And Verify Info Message is displayed on searched profile indicating missing Grade data
    And Verify Info Message contains text about reduced match accuracy due to missing data

