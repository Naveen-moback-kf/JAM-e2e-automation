@Map_Different_SP_in_AutoAI
Feature: Manually Map different Success Profile for the Organization Profile in Auto AI Job Mapping

  @Navigate_To_Job_Mapping
  Scenario: Navigate to Job Mapping page 
    Then Navigate to Job Mapping page from KFONE Global Menu in PM
    Then User should verify Job Mapping logo is displayed on screen
    
  @ApplyMappingStatusFilters
  Scenario: Apply Mapping Status filters in Job Mapping page
    When User is in Job Mapping page
    Then Verify job profiles count is displaying on the page
    Then Click on Filters dropdown button
    Then Click on Mapping Status Filters dropdown button
    And Select one option in Mapping Status Filters dropdown
    And Close the Filters dropdown
    And User should verify count of job profiles is correctly showing on top of Job Profiles listing table

  @VerifyOrganizationJobDetails
  Scenario: Verify Organization Job details on Profile with Search a Different Profile button in Profiles List
    Given Skip scenario if all profiles are already mapped
    When User is in Job Mapping page
    Then User should search for Job Profile with Search a Different Profile button on Mapped Success Profile
    Then User should verify Organization Job Name and Job Code Values of Job Profile with Search a Different Profile button
    Then User should verify Organization Job Grade and Department values of Job Profile with Search a Different Profile button
    Then User should verify Organization Job Function or Sub-function of Job Profile with Search a Different Profile button
    Then Click on mapped profile of Job Profile with Search a Different Profile button
    Then Verify Mapped Profile details popup is displayed

  @JobProfileDetailsPopup
  Scenario: Validate content in Job profile details popup for the Profile with Search a Different Profile button
    Given Skip scenario if all profiles are already mapped
    When User is on profile details popup of manually mapped profile
    Then Verify profile header matches with mapped profile name
    Then Verify mapped profile details displaying on the popup
    Then User should verify Profile Level dropdown is available and Validate levels present inside dropdown of mapped profile
    Then Validate Role Summary of mapped profile is displaying
    Then Validate data in RESPONSIBILITIES screen of mapped profile
    Then Validate data in BEHAVIOURAL COMPETENCIES screen of mapped profile
    Then Validate data in SKILLS screen of mapped profile
    Then User should verify publish profile button is available on popup screen of mapped profile
    And Click on close button in profile details popup of mapped profile

  @Search_Different_Profile_button
  Scenario: Click on Search a Different Profile button
    Given Skip scenario if all profiles are already mapped
    Then Click on Search a Different Profile button on Mapped Success Profile
    Then User should be navigated to Manual Job Mapping screen
    Then Verify Organization Job details in Manual Mapping screen

  @Validate_Last_saved_SP_Details
  Scenario: Validate Last Saved Profile details in Manual Mapping Screen
    Given Skip scenario if all profiles are already mapped
    Then User should verify Last Saved Profile name is displaying in Manual Mapping screen
    Then Click on Last Saved Profile name in Manual Mapping Screen
    Then User should verify Last Saved Profile name in Manual Mapping Screen matches with Profile Name in details Popup
    Then User should verify Profile Level dropdown is available on Last Saved Success Profile and Validate levels present inside dropdown
    Then Validate Last Saved Success Profile Role Summary matches with Profile Role Summary in details popup
    #Then Validate Last Saved Success Profile Details matches with Profile Details in details popup
    Then Validate Last Saved Success Profile Responsibilities matches with Profile Responsibilities in details popup
    Then Validate Last Saved Success Profile Behavioural competencies matches with Profile Behavioural competencies in details popup
    Then Validate Last Saved Success Profile Skills macthes with Profile Skills in details popup

  @Search_Select_Success_Profile
  Scenario: Search and select Success Profile in Manual Mapping screen
    Given Skip scenario if all profiles are already mapped
    Then Search for Success Profile in Manual Mapping screen
    Then Select first Sucess Profile from search results in Manual Mapping screen
    Then Verify Success Profile is added in Manual Job Mapping screen

  @Validate_Profile_Details_Manual_Mapping
  Scenario: Validate profile details in Manual Job Mapping screen
    Given Skip scenario if all profiles are already mapped
    When Success Profile is added in Manual Job Mapping screen
    Then User should verify Profile Level dropdown is available and Validate levels present inside dropdown in Manual Job Mapping screen
    Then Change Profile Level in Manual Job Mapping screen
    Then Validate Role Summary is displaying in Manual Job Mapping screen
    Then Verify profile details displaying in Manual Job Mapping screen

  @Validate_Profile_Tabs_Manual_Mapping
  Scenario: Validate profile screens data in Manual Job Mapping screen
    Given Skip scenario if all profiles are already mapped
    When Success Profile is added in Manual Job Mapping screen
    Then Validate data in RESPONSIBILITIES screen in Manual Job Mapping screen
    Then Validate data in BEHAVIOURAL COMPETENCIES screen in Manual Job Mapping screen
    Then Validate data in SKILLS screen in Manual Job Mapping screen

  @Save_Profile_Selection
  Scenario: Save profile selection and navigate back to Job Mapping
    Given Skip scenario if all profiles are already mapped
    When Success Profile is added in Manual Job Mapping screen
    Then Click on Save Selection button in Manual Job Mapping screen
    Then User should be navigated to Job Mapping page
    Then User should verify Job Mapping logo is displayed on screen

  @Verify_Mapped_SP_Details
  Scenario: Verify details of different SP which is Mapped to Organization Job
    Given Skip scenario if all profiles are already mapped
    #Then Search for Organization Job with Manually Mapped SP
    Then Verify Organization Job with new Mapped SP is displaying on Top of Profiles List
    Then Click on mapped profile of Job Profile with Search a Different Profile button
    Then Verify Mapped Profile details popup is displayed

  @JobProfileDetailsPopup
  Scenario: Validate content in manually mapped Job profile details popup
    Given Skip scenario if all profiles are already mapped
    When User is on profile details popup of manually mapped profile
    Then Verify profile header matches with mapped profile name
    Then User should verify Profile Level dropdown is available in details popup and Validate levels present inside dropdown
    Then Validate Profile Role Summary in details popup matches with Mapped Success Profile Role Summary
    #Then validate Profile Details in details popup matches with Mapped Success Profile details
    Then Validate Profile Responsibilities in details popup matches with Mapped Success Profile Responsibilities
    Then Validate Profile Behavioural competencies in details popup matches with Mapped Success Profile Behavioural competencies
    Then Validate Profile Skills in details popup matches with Mapped Success Profile Skills
