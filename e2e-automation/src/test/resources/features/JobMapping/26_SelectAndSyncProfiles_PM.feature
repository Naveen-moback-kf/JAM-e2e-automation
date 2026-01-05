@Select_And_Sync_Profiles_PM
Feature: Select and Sync Profiles with HCM in HCM Sync Profiles screen (PM)
  This feature validates two selection methods in HCM Sync Profiles screen:
  1. Header Checkbox - Selects only LOADED profiles (lazy loading aware)
  2. Select All - Selects ALL profiles including those not yet loaded

  # ============================================
  # COMMON SETUP - Navigate to HCM Sync Profiles
  # ============================================
  
  @Navigate_HCM_Sync_Profiles @Common_Setup
  Scenario: Navigate to HCM Sync Profiles and Verify Title/Description
    Then Skip scenario if user does not have HCM Sync access
    When User is on Profile Manager page
    Then Click on HCM Sync Profiles header button
    Then User should be navigated to HCM Sync Profiles screen
    And Verify title is correctly displaying in HCM Sync Profiles screen
    And Verify description below the title is correctly displaying in HCM Sync Profiles screen

  # ============================================
  # FLOW 1: SELECT LOADED PROFILES (Header Checkbox)
  # ============================================
  
  @Select_Loaded_Profiles @Header_Checkbox_Selection
  Scenario: Select Loaded Profiles with Header Checkbox
    Then Skip scenario if user does not have HCM Sync access
    When User is in HCM Sync Profiles screen
    Then Scroll page to view more job profiles in HCM Sync Profiles screen
    Then Scroll page to view more job profiles in HCM Sync Profiles screen
    Then Click on header checkbox to select loaded job profiles in HCM Sync Profiles screen
    Then Scroll page to view more job profiles in HCM Sync Profiles screen
    Then Verify Profiles loaded after clicking Header checkbox are not selected in HCM Sync Profiles Screen

  @Sync_Loaded_Profiles @Header_Checkbox_Selection
  Scenario: Sync Loaded Profiles with HCM
    Then Skip scenario if user does not have HCM Sync access
    When User is in HCM Sync Profiles screen
    Then User should verify Sync with HCM button is enabled in HCM Sync Profiles screen
    Then Click on Sync with HCM button in HCM Sync Profiles screen
    Then User should verify Sync with HCM success popup appears on screen in HCM Sync Profiles screen

  @Verify_Loaded_Profiles_History @Header_Checkbox_Selection
  Scenario: Verify Loaded Profiles Sync History in Publish Center
    Then Skip scenario if user does not have HCM Sync access
    When User is in HCM Sync Profiles screen after syncing profiles
    Then Click on Publish Center button
    Then Verify user navigated to Job Profile History screen succcessfully
    Then Verify Recently exported job profiles history is in top row
    Then Verify details of the Recently exported job profiles in Job Profile History screen

  # ============================================
  # FLOW 2: SELECT ALL PROFILES (Chevron + Select All)
  # ============================================
  
  @SelectAll_Profiles @Select_All_Selection
  Scenario: Select All Profiles in HCM Sync Profiles screen
    Then Skip scenario if user does not have HCM Sync access
    Then Click on HCM Sync Profiles header button
    Then User should be navigated to HCM Sync Profiles screen
    Then Click on Chevron button beside header checkbox in HCM Sync Profiles screen
    Then Click on Select All button in HCM Sync Profiles screen
    Then Verify count of selected profiles by scrolling through all profiles

  @Verify_Sync_Button_Enabled @Select_All_Selection
  Scenario: Verify Sync with HCM button is Enabled after Select All
    Then Skip scenario if user does not have HCM Sync access
    Then User should verify Sync with HCM button is enabled in HCM Sync Profiles screen

  @Sync_All_Profiles @Select_All_Selection
  Scenario: Sync All Profiles with HCM
    Then Skip scenario if user does not have HCM Sync access
    When User is in HCM Sync Profiles screen
    Then Click on Sync with HCM button in HCM Sync Profiles screen
    Then User should verify Sync with HCM success popup appears on screen in HCM Sync Profiles screen

  @Verify_All_Profiles_History @Select_All_Selection
  Scenario: Verify All Profiles Sync History in Publish Center
    Then Skip scenario if user does not have HCM Sync access
    When User is in HCM Sync Profiles screen after syncing profiles
    Then Click on Publish Center button
    Then Verify user navigated to Job Profile History screen succcessfully
    Then Verify Recently exported job profiles history is in top row
    Then Verify details of the Recently exported job profiles in Job Profile History screen

