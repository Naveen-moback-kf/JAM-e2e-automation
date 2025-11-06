@Validate_Job_Mapping_UI_Header_Section
Feature: Validate Header section in Job Mapping UI

   @Navigate_To_Job_Mapping
   Scenario: Navigate to Job Mapping page 
     Then Navigate to Job Mapping page from KFONE Global Menu in PM

  @KFD_Logo
  Scenario: Header displays KF Talent Suite logo that navigates to KFONE Home page when clicked
    When User is in Job Mapping page
    Then Verify KF Talent Suite logo is displaying
    Then Click on KF Talent Suite logo
    Then Verify User navigated to KFONE Home Page
    
  @Username
  Scenario: Verify Header displays Client name and navigates to KFONE Clients page when clicked
    Then Navigate to Job Mapping page from KFONE Global Menu
    When User is in Job Mapping page
    Then User should verify client name is correctly displaying
    Then Click on client name in Job Mapping UI header
    Then Verify user navigated to KFONE Clients Page
    
   @NavigateToKFoneHome
   Scenario: Navigate to KFONE Home Page
    	Given User is in KFONE Clients page
    	Then Search for Client with PAMS ID
    	Then Verify Client name based on PAMS ID
    	Then Verify Products that client can acceess
    	Then Click on Client with access to Profile Manager Application
    	Then Verify User navigated to KFONE Home Page

  @UserProfile
  Scenario: Validate User Profile and Logout functionality from Job Mapping UI
    Then Navigate to Job Mapping page from KFONE Global Menu
    When User is in Job Mapping page
    Then Verify User Profile logo is displaying and clickable
    Then Verify User Profile Menu is opened
    Then Verify User name is displayed in Profile Menu
    Then Verify User email is displayed in Profile Menu
    Then Click on Sing Out button in User Profile Menu
    Then User should be signed out from the Application
