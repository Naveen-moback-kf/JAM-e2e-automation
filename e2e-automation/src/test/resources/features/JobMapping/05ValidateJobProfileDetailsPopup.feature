@Validate_Job_Profile_Details_Popup
Feature: Validate Job Profile Details Popup that opens on clicking Matched Success Profile

  @Navigate_To_Job_Mapping
  Scenario: Navigate to Job Mapping page 
    Then Navigate to Job Mapping page from KFONE Global Menu in PM
    Then User should verify Job Mapping logo is displayed on screen
    
  @VerifyOrganizationJobDetails
  Scenario: User views organization job details and opens matched success profile details
    When User is in Job Mapping page
    Then Search for Job Profile with View Other Matches button
    Then User should verify Organization Job Name and Job Code Values of Job Profile with View Other Matches button
    Then User should verify Organization Job Grade and Department values of Job Profile with View Other Matches button
    Then User should verify Organization Job Function or Sub-function of Job Profile with View Other Matches button
    Then Click on matched profile of Job Profile with View Other Matches button
    Then Verify Profile details popup is displayed

  @JobProfileDetailsPopup
  Scenario: User reviews detailed profile information in popup
    When User is on profile details popup
    Then Verify profile header matches with matched profile name
    Then Verify profile details displaying on the popup
    Then User should verify Profile Level dropdown is available and Validate levels present inside dropdown
    Then Validate Role Summary is displaying
    Then Validate data in RESPONSIBILITIES screen
    Then Validate data in BEHAVIOURAL COMPETENCIES screen
    Then Validate data in SKILLS screen
    Then User should verify publish profile button is available on popup screen
    And Click on close button in profile details popup
    Then User is in Job Mapping page
