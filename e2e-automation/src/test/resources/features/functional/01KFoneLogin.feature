@KFoneLogin
Feature: Login to KFONE Application and Find client with access to Profile Manager Application

	@NON_SSO_Login_via_KFONE
  	Scenario: User logs in to KFOne with standard credentials
    	Given Launch the KFONE application
    	Then Provide NON_SSO Login username and click Sign in button in KFONE login page
    	Then Provide NON_SSO Login password and click Sign in button in KFONE login page
    	Then Verify the KFONE landing page
    	
    @SSO_Login_via_KFONE
  	Scenario: User logs in to KFOne with SSO credentials
    	Given Launch the KFONE application
    	Then Provide SSO Login username and click Sign in button in KFONE login page
    	Then User should navigate to Microsoft Login page
    	Then Provide SSO Login Password and click Sign In
    	Then Verify the KFONE landing page
    	
    @Client_with_PM_Access
    Scenario: Find client with access to Profile Manager Application
    	Given User is in KFONE Clients page
    	Then Search for Client with PAMS ID
    	Then Verify Client name based on PAMS ID
    	Then Verify Products that client can acceess
    	Then Click on Client with access to Profile Manager Application
    	Then Verify User navigated to KFONE Home Page
    	Then Click on Profile Manager application in Your Products section
    	Then Verify User seemlessly landed on Profile Manager Application in KF HUB
    	Then Verify user should land on Profile Manager dashboard page
    	Then Store user role from session storage
   