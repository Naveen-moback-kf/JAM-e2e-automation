@Verify_Info_Message_Manual_Mapping_Profiles  
Feature: Verify Info Message for Multiple Profiles with Missing Data in Manual Mapping
 
  @Navigate_To_Job_Mapping
  Scenario: Navigate to Job Mapping page 
    Then Navigate to Job Mapping page from KFONE Global Menu in PM 
    Then User should verify Job Mapping logo is displayed on screen
    Then Verify job profiles count is displaying on the page
    
  @Filter_Manually_Mapped_Jobs
  Scenario: Filter Manually Mapped Jobs in Job Mapping page
    When User is in Job Mapping page
    Then Click on Filters dropdown button
    Then Click on Mapping Status Filters dropdown button
    And Select one option in Mapping Status Filters dropdown
    And Close the Filters dropdown
    And User should verify count of job profiles is correctly showing on top of Job Profiles listing table
    
  @Verify_Manual_Profile_Info_Message
  Scenario: Verify Info Message displays on first manually mapped profile with missing data
    # Validates info message presence on first manually mapped profile with missing Grade/Department/Function/Subfunction
    When User is in Job Mapping page with Manual Mapping filters applied
    Then Find and verify manually mapped profile with missing data has Info Message displayed
    And Verify Info Message contains text about reduced match accuracy due to missing data for manual mapping
  
  @Verify_Manual_Job_Details_Extraction_And_Comparison
  Scenario: Verify job details extraction and comparison for first manually mapped profile between Job Mapping and Manual Mapping pages
    # Validates that job details (Name, Code, Grade, Department, Function/Sub-function) are accurately extracted from Job Mapping page and match on Manual Mapping page for first manually mapped profile
    When User is in Job Mapping page with Manual Mapping filters applied
    Then Find manually mapped profile with missing data and Info Message
    And Extract job details from manually mapped profile with Info Message
    And Click on "Search a Different Profile" button for manually mapped profile with Info Message
    Then Verify user is navigated to Manual Mapping page
    And Extract job details from Manual Mapping page
    And Verify job details match between Job Mapping and Manual Mapping pages
   
  @Verify_Manual_Info_Message_Persistence
  Scenario: Verify Info Message persists in Manual Mapping page for first manually mapped profile
    # Validates info message persistence when navigating to Manual Mapping via "Search a Different Profile" for first manually mapped profile
    Then Verify Info Message is still displayed in Manual Mapping page
    And Verify Info Message contains same text about reduced match accuracy for manual mapping
    And Navigate back to Job Mapping page from Manual Mapping
    
  @Verify_Second_Manual_Profile_Info_Message
  Scenario: Verify Info Message displays on second manually mapped profile with missing data
    # Validates info message presence on second manually mapped profile with missing Grade/Department/Function/Subfunction
    When User is in Job Mapping page with Manual Mapping filters applied
    Then Find and verify second manually mapped profile with missing data has Info Message displayed
    And Verify Info Message contains text about reduced match accuracy due to missing data for second manually mapped profile  
  
  @Verify_Second_Manual_Job_Details_Extraction_And_Comparison
  Scenario: Verify job details extraction and comparison for second manually mapped profile between Job Mapping and Manual Mapping pages
    # Validates that job details (Name, Code, Grade, Department, Function/Sub-function) are accurately extracted from Job Mapping page and match on Manual Mapping page for second manually mapped profile
    When User is in Job Mapping page with Manual Mapping filters applied
    Then Find second manually mapped profile with missing data and Info Message
    And Extract job details from second manually mapped profile with Info Message
    And Click on "Search a Different Profile" button for second manually mapped profile with Info Message
    Then Verify user is navigated to Manual Mapping page
    And Extract job details from Manual Mapping page for second manually mapped profile
    And Verify job details match between Job Mapping and Manual Mapping pages for second manually mapped profile
      
  @Verify_Second_Manual_Info_Message_Persistence
  Scenario: Verify Info Message persists in Manual Mapping page for second manually mapped profile
    # Validates info message persistence when navigating to Manual Mapping via "Search a Different Profile" for second manually mapped profile
    Then Verify Info Message is still displayed in Manual Mapping page for second manually mapped profile
    And Verify Info Message contains same text about reduced match accuracy for second manually mapped profile
    
