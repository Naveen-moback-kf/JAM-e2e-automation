@Verify_Job_Mapping_Page_Components @Smoke_Test
Feature: Verify Components in Job Mapping Page
  
  @Navigate_To_Job_Mapping
  Scenario: Navigate to Job Mapping page 
    Then Navigate to Job Mapping page from KFONE Global Menu in PM
    Then User should verify Job Mapping logo is displayed on screen
	
  @TitleHeader_Component
  Scenario: Validate page title and description are properly displayed
    When User is in Job Mapping page
    Then Verify title header is correctly displaying
    And Verify title description below the title header

  @OrganizationJobs_Searchbar_Component
  Scenario: Validate search bar for organization jobs
    Then Verify Organization Jobs Search bar text box is clickable
    Then Verify Organization Jobs Search bar placeholder text

  @FilterDropdown_Component
  Scenario: Validate filter dropdown and available options
    Then Click on Filters dropdown button
    Then Verify options available inside Filters dropdown
    And Close the Filters dropdown

  @AddmorejobsButton
  Scenario: Verify Add More Jobs button navigation
    When User is in Job Mapping page
    Then User should see Add more jobs button is displaying
    Then Verify Add more jobs button is clickable
    Then Verify User landed on Add more jobs page
    And Close Add more jobs page
    Then User should be landed on Job Mapping page

  @ProfileCount_Component
  Scenario: Verify count of job profiles is correctly showing on top of Job Profiles listing table
    When User is in Job Mapping page
    Then Verify job profiles count is displaying on the page
    Then Scroll page to view more job profiles
    And User should verify count of job profiles is correctly showing on top of Job Profiles listing table
    
  @ValidateFirstProfile_Details
  Scenario: Verify Organization Job details of first profile
  	 When User is in Job Mapping page
  	 Then User should verify Organization Job Name and Job Code values of first profile
  	 Then User should verify Organization Job Grade and Department values of first profile
  	 Then User should verify Organization Job Function or Subfunction of first profile
  
  @Profile_Details_Popup_Component
  Scenario: Verify Profile Details Popup opens on click of Matched Success Profile
    Then Search for Job Profile with View Other Matches button
    Then Click on matched profile of Job Profile with View Other Matches button
    Then Verify Profile details popup is displayed
    Then Click on close button in profile details popup

  @ViewOtherMatches_Component
  Scenario: Verify View Other Matches button is available on each Matched success profile
    Then Search for Job Profile with View Other Matches button
    Then Click on View Other Matches button
    And Verify user landed on job compare page

  @SearchJobProfileWithJobNameSubstring
  Scenario: Search for the Job Profile using Job Name Substring
    When User is in Job Mapping page
    Then Verify job profiles count is displaying on the page
    Then Enter Job name substring in search bar
    Then User should verify count of job profiles is correctly showing on top of Job Profiles listing table
    Then Clear Search bar in Job Mapping page

  @PublishSelectedProfiles_Component
  Scenario: Verify Publish Selected button is Enabled and Disabled based on profile selection and deselection respectively
    Then User should verify Publish Selected Profiles button is disabled
    Then Click on header checkbox to select loaded job profiles in Job Mapping screen
    Then User should verify Publish Selected Profiles button is enabled
    Then User should uncheck header checkbox to deselect selected job profiles
    Then User should verify Publish Selected Profiles button is disabled
    Then Verify job profiles count is displaying on the page
    Then User should verify Publish button is displaying on first job profile
    Then Click on checkbox of first job profile
    Then Click on checkbox of second job profile
    Then Click on Publish Selected Profiles button
    Then User should get success profile published popup
    And Close the success profile published popup
    Then User is in Job Mapping page
    And User should verify count of job profiles is correctly showing on top of Job Profiles listing table

  @JobProfilesListingTableHeader_Component
  Scenario: Verify job profiles listing table title and headers
    Then User should verify Organization jobs table title and headers
    Then User should verify Matched success profiles table title and headers
  
  @Publish_Component
  Scenario: User can publish matched success profiles
    When User is in Job Mapping page
    Then Verify job profiles count is displaying on the page
    Then User should verify Publish button on Matched success profile is displaying and clickable
    Then User should verify publish success popup appears on screen
    And User should verify count of job profiles is correctly showing on top of Job Profiles listing table

  @ViewPublishedToggleButton_Component
  Scenario: Verify view published toggle button functionality to display Unpublished and Published job profiles in the listing table
    Then User should verify view published toggle button is displaying
    Then Click on toggle button to turn on
    Then User should verify published jobs are displaying in the listing table
    Then Click on toggle button to turn off
    Then User should verify unpublished jobs are displaying in the listing table

		