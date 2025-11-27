@Validate_Sorting_HCM_PM
Feature: Validate Sorting of Profiles in HCM Sync Profiles screen
	
	@Navigate_HCM_Sync_Profiles
  	Scenario: Navigate to HCM Sync Profiles screen in PM
    	When User is on Profile Manager page
    	Then Click on HCM Sync Profiles header button
    	Then User should be navigated to HCM Sync Profiles screen
    	
    @SortByName_Ascending
  	Scenario: Validate sorting of Profiles by Name in Ascending Order in HCM Screen
  		When User is in HCM Sync Profiles screen
    	Then Sort Profiles by Name in Ascending order in HCM Sync Profiles screen
   		Then Scroll page to view more job profiles in HCM Sync Profiles screen
    	Then User should verify first hundred job profiles are correctly sorted by Name in Ascending order
    	Then Refresh HCM Sync Profiles screen
    	
    @SortByLevel_Descending
  	Scenario: Validate sorting of Profiles by Level in Descending Order in HCM Screen
  		When User is in HCM Sync Profiles screen
    	Then Sort Profiles by Level in Descending order in HCM Sync Profiles screen
   		Then Scroll page to view more job profiles in HCM Sync Profiles screen
    	Then User should verify first hundred job profiles are correctly sorted by Level in Descending order
    	Then Refresh HCM Sync Profiles screen
    	
   @SortByJobStatus_Ascending_Function_Descending
 	Scenario: Validate sorting of Profiles by Job Status in Ascending Order and Function in Descending Order in HCM Screen
 		When User is in HCM Sync Profiles screen
   		Then Sort Profiles by Job Status in Ascending order in HCM Sync Profiles screen
   		Then Sort Profiles by Function in Descending order in HCM Sync Profiles screen
  		Then Scroll page to view more job profiles in HCM Sync Profiles screen
   		Then User should verify first hundred job profiles are correctly sorted by Job Status in Ascending and Function in Descending order
   		Then Refresh HCM Sync Profiles screen
   	
   @SortByJobStatus_ExportStatus_Function_ThreeLevel
 	Scenario: Validate three level sorting by Job Status Ascending, Export Status Descending and Function Ascending in HCM Screen
 		When User is in HCM Sync Profiles screen
   		Then Sort Profiles by Job Status in Ascending order in HCM Sync Profiles screen
   		Then Sort Profiles by Export Status in Descending order in HCM Sync Profiles screen
   		Then Sort Profiles by Function in Ascending order in HCM Sync Profiles screen
  		Then Scroll page to view more job profiles in HCM Sync Profiles screen
   		Then User should verify first hundred job profiles are correctly sorted by Job Status Ascending, Export Status Descending and Function Ascending
   		Then Refresh HCM Sync Profiles screen