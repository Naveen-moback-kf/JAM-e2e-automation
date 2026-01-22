@Validate_Reupload_Missing_Data_Profiles
Feature: Validate Re-uploading Jobs with Missing Data by Filling Details in Excel
  
  This feature validates fixing jobs with missing data by:
  - Navigating to Jobs Missing Data screen
  - Exporting top 10 profiles with missing data to Excel file
  - Filling missing details in the exported Excel
  - Re-uploading the corrected file
  - Verifying the jobs no longer show missing data warning

  @Navigate_To_Job_Mapping
  Scenario: Navigate to Job Mapping page
    Then Navigate to Job Mapping page from KFONE Global Menu in PM
    Then User should verify Job Mapping logo is displayed on screen

  @Navigate_To_Missing_Data_Screen
  Scenario: Navigate to Jobs Missing Data screen from Job Mapping page
    When User is in Job Mapping page
    Given Skip scenario if Missing Data Tip Message is not displayed
    Then Verify Missing Data Tip Message is displaying on Job Mapping page
    And Verify Missing Data Tip Message contains correct count of jobs with missing data
    When Click on "View & Re-upload jobs" link in Missing Data Tip Message
    Then Verify user is navigated to Jobs with Missing "<DataType>" Data screen
    And Verify Re-upload button is displayed on Jobs Missing Data screen

  @Export_Missing_Data_Profiles_To_Excel
  Scenario: Fetch profiles with missing data and export to Excel file (up to 10 or all if less)
    Given Skip scenario if Missing Data Tip Message is not displayed
    When User is in Jobs Missing Data screen
    Then Capture total count of profiles in Jobs Missing Data screen
    And Extract details of profiles from Jobs Missing Data screen up to 10 or all if less
    Then Create Excel file with extracted profiles in Job Catalog format
      # Excel Format: Client Job Code,Client Job Title,Department,Job Family,Job Sub Family,Job Grade,Is Executive
    And Save Excel file as "MissingDataProfiles_ToFix.xlsx" in test resources folder
    And Verify Excel file is created successfully with correct headers
    And Verify Excel file contains extracted profile data
    And Click on Close button to return to Job Mapping page from "<DataType>" validation

  @Fill_Missing_Grade_Data_In_Excel
  Scenario: Fill missing GRADE data in exported Excel file
    Given Skip scenario if Missing Data Tip Message is not displayed
    And Excel file with missing data profiles exists
    Then Read the exported Excel file with missing data profiles
    And Identify profiles with missing Grade value in Excel
    Then Fill missing Grade values with appropriate grade codes
      # Grade values: JGL01, JGL02, JGL03, etc.
    And Save the updated Excel file with filled Grade data
    And Verify updated Excel file has no empty Grade values

  @Fill_Missing_Department_Data_In_Excel
  Scenario: Fill missing DEPARTMENT data in exported Excel file
    Given Skip scenario if Missing Data Tip Message is not displayed
    And Excel file with missing data profiles exists
    Then Read the exported Excel file with missing data profiles
    And Identify profiles with missing Department value in Excel
    Then Fill missing Department values with appropriate department names
      # Department values: Engineering, Finance, HR, etc.
    And Save the updated Excel file with filled Department data
    And Verify updated Excel file has no empty Department values

  @Fill_All_Missing_Data_In_Excel
  Scenario: Fill all missing data fields in exported Excel file
    Given Skip scenario if Missing Data Tip Message is not displayed
    And Excel file with missing data profiles exists
    Then Read the exported Excel file with missing data profiles
    And Identify all profiles with any missing values in Excel
    Then Fill missing Grade values with default grade JGL01
    And Fill missing Department values with default department Engineering
    And Fill missing Job Family values with default value General
    And Fill missing Job Sub Family values with default value Operations
    And Save the updated Excel file with all missing data filled
    And Verify updated Excel file has no empty required fields

  @Reupload_Corrected_Excel_Via_Reupload_Button
  Scenario: Re-upload corrected Excel file using Re-upload button in Missing Data screen
    Given Skip scenario if Missing Data Tip Message is not displayed
    When User is in Job Mapping page
    Then Capture the count of jobs with missing data before re-upload
    And Capture Total Results count before re-upload
    # Total Results validation: Clears any search filter using CTRL+A→DELETE→ENTER to get accurate total count
    When Click on "View & Re-upload jobs" link in Missing Data Tip Message
    Then Verify user is navigated to Jobs with Missing "<DataType>" Data screen
    And Verify Re-upload button is displayed on Jobs Missing Data screen
    When Click on Re-upload button in Jobs Missing Data screen
    Then User should be landed on KFONE Add Job Data page
    
  @UploadGeneratedExcelFile
  Scenario: User uploads generated Excel file through manual upload
    Given Skip scenario if Missing Data Tip Message is not displayed
    When User is in KFONE Add Job Data page
    Then User should click on Manual Upload button
    Then Verify Jobs count in KFONE Add Job Data screen before adding more jobs
    # OPTIONAL: Informational only - logs warning if fails, doesn't stop test execution
    Then Click on Done button in KFONE Add Job Data page
    # OPTIONAL: Informational only - logs warning if fails, doesn't stop test execution
    Then Upload generated Excel file using Browse Files button
    Then User should verify File Close button displaying and clickable
    Then Upload generated Excel file using Browse Files button
    Then Click on Continue button in Add Job data screen

  @ValidateJobDataUpdateSuccess
  Scenario: User validates successful job data update (re-upload updates existing profiles, not adding new ones)
    Given Skip scenario if Missing Data Tip Message is not displayed
    When User is in KFONE Add Job Data page after uploading file
    Then User should Validate Job Data Upload is in Progress
    # OPTIONAL: Informational only - logs warning if fails, doesn't stop test execution
    Then User should validate Job Data added successfully
    # OPTIONAL: Informational only - logs warning if fails, doesn't stop test execution
    # NOTE: For re-upload, job count should remain UNCHANGED (updating existing jobs, not adding new ones)
    Then Verify Jobs count remains unchanged after re-uploading jobs
    Then Close Add Job Data screen
    Then User should be landed on Job Mapping page
    # NOTE: Unpublished count may or may not change - skip this verification for re-upload scenario

  @Wait_For_Backend_Processing
  Scenario: Wait for backend to process uploaded data and refresh page
    Given Skip scenario if Missing Data Tip Message is not displayed
    When User is in Job Mapping page
    Then Wait for backend processing and refresh Job Mapping page
    # Simple 3-minute wait - Profile verification scenario will have smart retries to check actual data

  @Verify_Profiles_Fixed_In_Job_Mapping
  Scenario: Verify re-uploaded profile is fixed with corrected data (with dynamic polling)
    Given Skip scenario if Missing Data Tip Message is not displayed
    When User is in Job Mapping page
    Then Search for first re-uploaded profile by Job Name from Excel
    # Search retries: 20 attempts × 30 seconds = up to 10 minutes with dynamic polling
    And Verify profile is found in Job Mapping search results
    # Search by Job Name may return multiple profiles - validates specific Job Code match
    Then Verify profile does NOT display Missing Data info icon
    And Verify profile displays the corrected data values

  @Verify_Missing_Data_Count_Reduced
  Scenario: Verify missing data count has reduced after re-upload
    Given Skip scenario if Missing Data Tip Message is not displayed
    When User is in Job Mapping page
    Then Verify Missing Data Tip Message is displaying on Job Mapping page
    And Capture the count of jobs with missing data after re-upload
    Then Verify missing data count has decreased compared to before re-upload

  @Verify_Total_Results_Count_Unchanged
  Scenario: Verify Total Results count remains unchanged after re-upload (updating existing profiles, not adding new)
    Given Skip scenario if Missing Data Tip Message is not displayed
    When User is in Job Mapping page
    Then Click on View Published toggle button to turn on
    Then Click on View Published toggle button to turn off
    #Then Clear Search bar in Job Mapping page
    Then Capture Total Results count after re-upload
    And Verify Total Results count remains unchanged after re-upload

  @Verify_Profiles_Removed_From_Missing_Data_Screen
  Scenario: Verify re-uploaded profiles are removed from Missing Data screen
    Given Skip scenario if Missing Data Tip Message is not displayed
    When User is in Job Mapping page
    Then Click on "View & Re-upload jobs" link in Missing Data Tip Message
    Then Verify user is navigated to Jobs with Missing "<DataType>" Data screen
    Then Search for first re-uploaded profile by Job Code in Missing Data screen
    And Verify profile is NOT found in Jobs Missing Data screen
    And Click on Close button to return to Job Mapping page from "<DataType>" validation
