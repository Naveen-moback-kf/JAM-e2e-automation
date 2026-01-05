@Validate_Profile_Level_Functionality
Feature: Validate Profile Level Functionality in Profile Details Popup and in Job Comparison page

  @Navigate_To_Job_Mapping
  Scenario: Navigate to Job Mapping page 
    Then Navigate to Job Mapping page from KFONE Global Menu in PM
    Then User should verify Job Mapping logo is displayed on screen

  @VerifyOrganizationJobDetails
  Scenario: Verify Organization Job details on Profile with View Other Matches button in Profiles List and click on Matched Success Profile
    When User is in Job Mapping page
    Then Search for Job Profile with View Other Matches button
    Then User should verify Organization Job Name and Job Code Values of Job Profile with View Other Matches button
    Then User should verify Organization Job Grade and Department values of Job Profile with View Other Matches button
    Then User should verify Organization Job Function or Sub-function of Job Profile with View Other Matches button
    Then Click on matched profile of Job Profile with View Other Matches button
    Then Verify Profile details popup is displayed

  @ChangeProfileLevelandVerifyDetails
  Scenario: Verify details of Changed profile level value in Job profile details popup
    When User is on profile details popup
    Then Verify profile header matches with matched profile name
    Then User should verify Profile Level dropdown is available and Validate levels present inside dropdown
    Then Change Profile Level
    Then User should verify profile header matches with changed profile level in Job profile details popup
    Then Verify profile details displaying on the popup
    Then Validate Role Summary is displaying
    Then Validate data in RESPONSIBILITIES screen
    Then Validate data in BEHAVIOURAL COMPETENCIES screen
    Then Validate data in SKILLS screen
    And Click on close button in profile details popup
    Then User is in Job Mapping page

  @Navigate_to_Job_Comparison_page
  Scenario: Navigate to Job Comparison page by clicking on View Other Matches button
    Then Search for Job Profile with View Other Matches button
    Then Click on View Other Matches button
    Then Verify user navigated to job comparison page

  @Validate_Organization_Job_Details
  Scenario: Validate Organization Job details in Job Comparison Page
    When User is in Job Comparison Page
    Then Validate organization job name and code in job comparison page
    And User should validate organization job grade department and function or subfunction in job comparison page

  @ChangeProfileLevelInJobComparison
  Scenario: Change profile level and validate header updates in Job Comparison page
    When User is in Job Comparison Page
    Then User should verify Recommended Profile Name matches with Matched Success Profile Name
    Then User should verify Recommended tag and Select button is displaying on the profile
    Then User should verify Profile Level dropdown is available and Validate levels present inside dropdown
    Then Change Profile Level in Job Comparison Page
    Then User should verify Recommended Profile Name matches with changed profile level

  @ValidateChangedProfileMetadata
  Scenario: Validate metadata fields after changing profile level in Job Comparison page
    When User is in Job Comparison Page after changing profile level
    Then Validate Recommended Profile Grade matches with Matched Success Profile Grade
    Then Validate Recommended Profile Level Sublevels matches with Matched Success Profile Level Sublevels
    Then Validate Recommended Profile Function Subfunction matches with Matched Success Profile Function Subfunction
    Then Validate Recommended Profile Seniority level matches with Matched Success Profile Seniority level

  @ValidateChangedProfileExperienceAndEducation
  Scenario: Validate experience and education after changing profile level in Job Comparison page
    When User is in Job Comparison Page after changing profile level
    Then Validate Recommended Profile Managerial Experience matches with Matched Success Profile Managerial Experience
    Then Validate Recommended Profile Education matches with Matched Success Profile Education
    Then Validate Recommended Profile General Experience matches with Matched Success Profile General Experience

  @ValidateChangedProfileContentSections
  Scenario: Validate content sections after changing profile level in Job Comparison page
    When User is in Job Comparison Page after changing profile level
    Then Validate Recommended Profile Role Summary matches with Matched Success Profile Role Summary
    Then Validate Recommended Profile Responsibilities matches with Matched Success Profile Responsibilities
    Then Validate Recommended Profile Behavioural competencies matches with Matched Success Profile Behavioural competencies
    Then Validate Recommended Profile Skills matches with Matched Success Profile Skills
