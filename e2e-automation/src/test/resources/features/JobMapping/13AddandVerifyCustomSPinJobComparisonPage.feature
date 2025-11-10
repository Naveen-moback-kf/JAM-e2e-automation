@Add_and_Verify_CustomSP_In_Job_Comparison_Page
Feature: Add and Verify Custom Success Profile in Job Comparsion page
  
  @Navigate_To_Job_Mapping
  Scenario: Navigate to Job Mapping page 
    Then Navigate to Job Mapping page from KFONE Global Menu in PM
    Then User should verify Job Mapping logo is displayed on screen

  @VerifyOrganizationJobDetails
  Scenario: Verify Organization Job details on Profile with View Other Matches button in Profiles List
    When User is in Job Mapping page
    Then Search for Job Profile with View Other Matches button
    Then User should verify Organization Job Name and Job Code Values of Job Profile with View Other Matches button
    Then User should verify Organization Job Grade and Department values of Job Profile with View Other Matches button
    Then User should verify Organization Job Function or Sub-function of Job Profile with View Other Matches button
    
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

  @Add_Custom_SP
  Scenario: Search and Add Custom SP to Profiles List in Job Comparison page
    When User is in Job Comparison Page
    Then Click on Search bar in Job Comparison Page
    Then Verify Search bar Placeholder text in Job Comparison Page
    Then User should enter Custom SP Search String in the search bar
    Then Select first Custom SP from search results
    Then Verify Custom SP added to Profiles List in Job Comparison page
    Then User should verify Custom SP Name and close button are displaying in search bar after adding Custom SP

  @Verify_CustomSP_Basic_Details
  Scenario: Verify Custom SP basic profile information
    When User added Custom SP to Profiles List in Job Comparison page
    Then Validate Custom SP Profile Name matches with Selected Custom SP Name from Search Results
    Then User should verify Close button and Select button are displaying on the profile
    Then Verify Custom SP Profile Grade
    Then Verify Custom SP Profile Level Sublevels
    Then Verify Custom SP Profile Function Subfunction

  @Verify_CustomSP_Experience_Details
  Scenario: Verify Custom SP experience and seniority information
    When User added Custom SP to Profiles List in Job Comparison page
    Then Verify Custom SP Profile Seniority level
    Then Verify Custom SP Profile Managerial Experience
    Then Verify Custom SP Profile Education
    Then Verify Custom SP Profile General Experience

  @Verify_CustomSP_Skills_Competencies
  Scenario: Verify Custom SP skills and competencies
    When User added Custom SP to Profiles List in Job Comparison page
    Then Verify Custom SP Profile Role Summary
    Then Verify Custom SP Profile Responsibilities
    Then Verify Custom SP Profile Behavioural competencies
    Then Verify Custom SP Profile Skills

  @Clear_Search_bar_text
  Scenario: Verify Added Custom SP is not cleared when Search bar text is cleared
    When User added Custom SP to Profiles List in Job Comparison page
    Then Clear text in Search bar with clear button in the Search bar
    Then User should Verify added Custom SP is not cleared from Profiles List in Job Comparison page

  @Add_Custom_SP
  Scenario: Search, Add and Replace New Custom SP with existing Custom SP in Profiles List in Job Comparison page
    When User is in Job Comparison Page
    Then Click on Search bar in Job Comparison Page
    Then Verify Search bar Placeholder text in Job Comparison Page
    Then User should enter Custom SP Search String in the search bar
    Then Select Third Custom SP from search results
    Then Verify New Custom SP replaces existing Custom SP in Profiles List in Job Comparison page

  @Close_CustomSP_Profile
  Scenario: Verify Added Custom SP can be closed with close button on the Profile
    When User added Custom SP to Profiles List in Job Comparison page
    Then Clear Custom SP and text in Search bar with close button on the Profile
