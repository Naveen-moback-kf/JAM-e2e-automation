@Manual_Mapping_SP_in_AutoAI
Feature: Using Find Match button, Manually Map Success Profile for the Organization Profile in Auto AI Job Mapping

  @Navigate_To_Job_Mapping
  Scenario: Navigate to Job Mapping page 
    Then Navigate to Job Mapping page from KFONE Global Menu in PM
    Then User should verify Job Mapping logo is displayed on screen

  @Sort_Organization_Name_in_Descending_Order
  Scenario: Apply Descending Order sorting on Organization Job Name to get Jobs with No BIC Mappings to Top
    When User is in Job Mapping page
    Then Sort Job Profiles by Organization Job Name in Descending order
    Then Verify Profile with No BIC Mapping is displaying on Top after sorting

  @Find_Match
  Scenario: Click on Find Match button and Search for SP in Manual Mapping screen
    Given Skip scenario if all profiles are already mapped
    Then Click on Find Match button
    Then User should be navigated to Manual Job Mapping screen
    Then Verify Organization Job details in Manual Mapping screen
    Then Search for Success Profile in Manual Mapping screen
    Then Select first Sucess Profile from search results in Manual Mapping screen

  @Map_SP_to_Organization_Job
  Scenario: Validate and Manually Map SP to Organization Job in Auto AI
    Given Skip scenario if all profiles are already mapped
    When Verify Success Profile is added in Manual Job Mapping screen
    Then User should verify Profile Level dropdown is available and Validate levels present inside dropdown in Manual Job Mapping screen
    Then Change Profile Level in Manual Job Mapping screen
    Then Validate Role Summary is displaying in Manual Job Mapping screen
    Then Verify profile details displaying in Manual Job Mapping screen
    Then Validate data in RESPONSIBILITIES screen in Manual Job Mapping screen
    Then Validate data in BEHAVIOURAL COMPETENCIES screen in Manual Job Mapping screen
    Then Validate data in SKILLS screen in Manual Job Mapping screen
    Then Click on Save Selection button in Manual Job Mapping screen
    Then User should be navigated to Job Mapping page
    Then Click on View Published toggle button to turn on
    Then Click on toggle button to turn off

  @Verify_Mapped_SP_Details
  Scenario: Verify details of SP which is Mapped to Organization Job
    When User is in Job Mapping page
    #Then Search for Organization Job with Manually Mapped SP
    Then Verify Organization Job with new Mapped SP is displaying on Top of Profiles List
    Then User should verify Search a Different Profile button is displaying on manually mapped success profile
    Then Click on manually mapped profile name of Job Profile on Top of Profiles List
    Then Verify Profile details popup is displayed

  @JobProfileDetailsPopup
  Scenario: Validate content in manually mapped Job profile details popup
    When User is on profile details popup of manually mapped profile
    Then Verify profile header matches with mapped profile name
    Then User should verify Profile Level dropdown is available in details popup and Validate levels present inside dropdown
    Then Validate Profile Role Summary in details popup matches with Mapped Success Profile Role Summary
    #Then validate Profile Details in details popup matches with Mapped Success Profile details
    Then Validate Profile Responsibilities in details popup matches with Mapped Success Profile Responsibilities
    Then Validate Profile Behavioural competencies in details popup matches with Mapped Success Profile Behavioural competencies
    Then Validate Profile Skills in details popup matches with Mapped Success Profile Skills
