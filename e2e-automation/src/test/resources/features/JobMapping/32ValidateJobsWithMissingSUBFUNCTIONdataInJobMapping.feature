@Validate_Jobs_With_Missing_SUBFUNCTION_Data_In_JobMapping
Feature: Validate Jobs with Missing SUBFUNCTION Data in Job Mapping UI

  # This feature validates two independent scenarios for missing SUBFUNCTION data:
  # 
  # FORWARD SCENARIO (Scenario 1):
  # - Start from Job Mapping page → Find job with missing Subfunction → Navigate to Missing Data screen → Validate
  # - If NO matching profile found: SKIP Forward scenario
  # 
  # REVERSE SCENARIO (Scenario 2):
  # - Start from Job Mapping page → Navigate to Missing Data screen → Find job with missing Subfunction → Validate in Job Mapping
  # - Runs INDEPENDENTLY of Forward scenario (even if Forward was skipped)
  # - If Forward scenario found a profile: Skip that profile to test a different one
  # - If Forward scenario did NOT find a profile: Test any matching profile
  # - If NO matching profile found: SKIP Reverse scenario
  
  @Navigate_To_Job_Mapping
  Scenario: Navigate to Job Mapping page 
    Then Navigate to Job Mapping page from KFONE Global Menu in PM
    Then User should verify Job Mapping logo is displayed on screen

  @Forward_Validate_Missing_SUBFUNCTION_Flow
  Scenario: Forward Flow - Find job with missing SUBFUNCTION in Job Mapping first, then validate in Jobs Missing Data screen
    When User is in Job Mapping page
    Then Find job profile in Job Mapping page where Subfunction is missing
    And Extract SUBFUNCTION job details from found profile in Job Mapping page
    Then Click on "View & Re-upload jobs" link in Missing Data Tip Message
    Then Verify user is navigated to Jobs with Missing SUBFUNCTION Data screen
    And Search for the extracted SUBFUNCTION job profile by name in Jobs Missing Data screen
    Then Verify SUBFUNCTION job profile is found and displayed in Jobs Missing Data screen search results
    And Extract SUBFUNCTION job details from found profile in Jobs Missing Data screen
    And Verify all SUBFUNCTION job details match between Job Mapping page and Jobs Missing Data screen
    And Click on Close button to return to Job Mapping page from SUBFUNCTION validation
    Then Verify user is back on Job Mapping page after SUBFUNCTION validation
    
  @Reverse_Validate_Missing_SUBFUNCTION_Flow
  Scenario: Reverse Flow - Validate jobs with missing SUBFUNCTION data from Missing Data screen to Job Mapping
    When User is in Job Mapping page
    Then User should verify Job Mapping logo is displayed on screen
    Then Click on "View & Re-upload jobs" link in Missing Data Tip Message
    Then Verify user is navigated to Jobs with Missing SUBFUNCTION Data screen
    And Find job in Jobs Missing SUBFUNCTION Data screen where Subfunction is N/A
    And Extract all available job details from Jobs with Missing SUBFUNCTION Data screen
    And Click on Close button to return to Job Mapping page from SUBFUNCTION validation
    Then Verify user is back on Job Mapping page after SUBFUNCTION validation
    When Search for the extracted SUBFUNCTION job profile by name in Job Mapping page
    Then Verify SUBFUNCTION job profile is found and displayed in search results
    And Extract SUBFUNCTION job details from searched profile in Job Mapping page
    And Verify all SUBFUNCTION job details match between Jobs Missing Data screen and Job Mapping page
    And Verify Info Message is displayed on searched profile indicating missing SUBFUNCTION data
    And Verify Info Message contains text about reduced match accuracy due to missing SUBFUNCTION data


