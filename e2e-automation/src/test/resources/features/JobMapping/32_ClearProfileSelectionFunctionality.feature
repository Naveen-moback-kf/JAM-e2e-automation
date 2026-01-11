@Clear_Profile_Selection
Feature: Validate Clear Profile Selection Functionality in JAM and PM

  # ═══════════════════════════════════════════════════════════════════
  # PM (HCM SYNC PROFILES) - HEADER CHECKBOX METHOD
  # ═══════════════════════════════════════════════════════════════════

  @Clear_PM_HeaderCheckbox @Clear_PM
  Scenario: PM - Navigate to HCM Sync Profiles and Select All
    Then Skip scenario if user does not have HCM Sync access
    When User is on Profile Manager page
    Then Click on HCM Sync Profiles header button
    Then User should be navigated to HCM Sync Profiles screen
    And Verify title is correctly displaying in HCM Sync Profiles screen
    Then Click on Chevron button beside header checkbox in HCM Sync Profiles screen
    Then Click on Select All button in HCM Sync Profiles screen
    Then User should verify Sync with HCM button is enabled in HCM Sync Profiles screen

  @Clear_PM_HeaderCheckbox @Clear_PM
  Scenario: PM - Clear Selection with Header Checkbox and Verify Loaded Profiles Unselected
    Then Skip scenario if user does not have HCM Sync access
    When User is in HCM Sync Profiles screen
    Then Click on header checkbox to Unselect loaded job profiles in "PM" screen
    Then User should verify action button is enabled in "PM" screen
    Then Verify Loaded Profiles are unselected in "PM" screen
    Then Scroll page to view more job profiles in "PM" screen
    Then Scroll page to view more job profiles in "PM" screen
    And Verify newly loaded profiles are still Selected in "PM" screen
    Then Refresh "PM" screen

  @Clear_PM_HeaderCheckbox @Clear_PM
  Scenario: PM - Select Loaded Profiles and Verify Header Checkbox Behavior
    Then Skip scenario if user does not have HCM Sync access
    When User is in HCM Sync Profiles screen
    Then Click on header checkbox to select loaded job profiles in "PM" screen
    Then Scroll page to view more job profiles in "PM" screen
    Then Verify Profiles loaded after clicking Header checkbox are not selected in "PM" screen

  @Clear_PM_HeaderCheckbox @Clear_PM
  Scenario: PM - Uncheck Header Checkbox and Verify Button Disabled
    Then Skip scenario if user does not have HCM Sync access
    When User is in HCM Sync Profiles screen
    Then Click on header checkbox to select loaded job profiles in "PM" screen
    Then User should verify action button is enabled in "PM" screen
    Then Click on header checkbox to Unselect loaded job profiles in "PM" screen
    Then User should verify action button is disabled in "PM" screen
    Then Scroll page to view more job profiles in "PM" screen
    Then Scroll page to view more job profiles in "PM" screen
    Then Verify Loaded Profiles are unselected in "PM" screen

  # ═══════════════════════════════════════════════════════════════════
  # PM (HCM SYNC PROFILES) - NONE BUTTON METHOD
  # ═══════════════════════════════════════════════════════════════════

  @Clear_PM_NoneButton @Clear_PM
  Scenario: PM - Navigate to HCM Sync Profiles and Select All for None Button Test
    Then Skip scenario if user does not have HCM Sync access
    When User is on Profile Manager page
    Then Click on HCM Sync Profiles header button
    Then User should be navigated to HCM Sync Profiles screen
    And Verify title is correctly displaying in HCM Sync Profiles screen
    Then Click on Chevron button beside header checkbox in HCM Sync Profiles screen
    Then Click on Select All button in HCM Sync Profiles screen
    Then User should verify Sync with HCM button is enabled in HCM Sync Profiles screen

  @Clear_PM_NoneButton @Clear_PM
  Scenario: PM - Clear All Selection with None Button
    Then Skip scenario if user does not have HCM Sync access
    When User is in HCM Sync Profiles screen
    Then Click on Chevron button beside header checkbox in HCM Sync Profiles screen
    Then Click on None button in "PM" screen
    Then User should verify action button is disabled in "PM" screen
    Then Scroll page to view more job profiles in "PM" screen
    Then Scroll page to view more job profiles in "PM" screen
    Then Verify all Loaded Profiles are Unselected in "PM" screen
    Then Refresh "PM" screen

  @Clear_PM_NoneButton @Clear_PM
  Scenario: PM - Select Loaded and Clear with None Button
    Then Skip scenario if user does not have HCM Sync access
    When User is in HCM Sync Profiles screen
    Then Click on header checkbox to select loaded job profiles in "PM" screen
    Then Scroll page to view more job profiles in "PM" screen
    Then Verify Profiles loaded after clicking Header checkbox are not selected in "PM" screen
    Then Click on Chevron button beside header checkbox in HCM Sync Profiles screen
    Then Click on None button in "PM" screen
    Then User should verify action button is disabled in "PM" screen
    Then Scroll page to view more job profiles in "PM" screen
    Then Scroll page to view more job profiles in "PM" screen
    Then Verify all Loaded Profiles are Unselected in "PM" screen
    Then Refresh "PM" screen
    
  @SelectAll_Filter_PM @SelectAll_PM
  Scenario: PM - Navigate to HCM Sync Profiles and Select All Filtered Profiles
    Then Skip scenario if user does not have HCM Sync access
    When User is in HCM Sync Profiles screen
    Then Verify job profiles count is displaying on the page in HCM Sync Profiles screen
    Then Click on Filters dropdown button in HCM Sync Profiles screen
    Then Apply Levels filter and verify profiles count is correctly displaying in HCM Sync Profiles screen
    Then Store applied PM filter value for validation
    Then User should verify count of job profiles is correctly showing on top of Job Profiles listing table in HCM Sync Profiles screen
    Then User should scroll down to view last filtered result in "PM" screen
    Then User should validate all filtered results match the applied filter in "PM" screen
    Then Click on Chevron button beside header checkbox in "PM" screen
    Then Click on Select All button in "PM" screen
    Then User should verify action button is enabled in "PM" screen
    Then Capture baseline of selected profiles in "PM" screen
    Then Click on Clear All Filters button in HCM Sync Profiles screen
    Then User should verify action button is enabled in "PM" screen
    
  @Clear_PM_NoneButton @Clear_PM
  Scenario: PM - Clear All Selection with None Button
    Then Skip scenario if user does not have HCM Sync access
    When User is in HCM Sync Profiles screen
    Then Click on Chevron button beside header checkbox in HCM Sync Profiles screen
    Then Click on None button in "PM" screen
    Then User should verify action button is disabled in "PM" screen
    Then Scroll page to view more job profiles in "PM" screen
    Then Scroll page to view more job profiles in "PM" screen
    Then Verify all Loaded Profiles are Unselected in "PM" screen
    Then Refresh "PM" screen

  # ═══════════════════════════════════════════════════════════════════
  # JAM (JOB MAPPING) - HEADER CHECKBOX METHOD
  # ═══════════════════════════════════════════════════════════════════

  @Clear_JAM_HeaderCheckbox @Clear_JAM
  Scenario: JAM - Navigate to Job Mapping and Select All
    Then Navigate to Job Mapping page from KFONE Global Menu in PM
    Then User should verify Job Mapping logo is displayed on screen
    When User is in Job Mapping page
    Then Click on Chevron button beside header checkbox in Job Mapping screen
    Then Click on Select All button in Job Mapping screen
    Then User should verify Publish Selected Profiles button is enabled

  @Clear_JAM_HeaderCheckbox @Clear_JAM
  Scenario: JAM - Clear Selection with Header Checkbox and Verify Loaded Profiles Unselected
    When User is in Job Mapping page
    Then Click on header checkbox to Unselect loaded job profiles in "JAM" screen
    Then User should verify action button is enabled in "JAM" screen
    Then Verify Loaded Profiles are unselected in "JAM" screen
    Then Scroll page to view more job profiles in "JAM" screen
    Then Scroll page to view more job profiles in "JAM" screen
    And Verify newly loaded profiles are still Selected in "JAM" screen
    Then Refresh "JAM" screen

  @Clear_JAM_HeaderCheckbox @Clear_JAM
  Scenario: JAM - Select Loaded Profiles and Verify Header Checkbox Behavior
    When User is in Job Mapping page
    Then Click on header checkbox to select loaded job profiles in "JAM" screen
    Then Scroll page to view more job profiles in "JAM" screen
    Then Verify Profiles loaded after clicking Header checkbox are not selected in "JAM" screen

  @Clear_JAM_HeaderCheckbox @Clear_JAM
  Scenario: JAM - Uncheck Header Checkbox and Verify Button Disabled
    When User is in Job Mapping page
    Then Click on header checkbox to select loaded job profiles in "JAM" screen
    Then User should verify action button is enabled in "JAM" screen
    Then Click on header checkbox to Unselect loaded job profiles in "JAM" screen
    Then User should verify action button is disabled in "JAM" screen
    Then Scroll page to view more job profiles in "JAM" screen
    Then Scroll page to view more job profiles in "JAM" screen
    Then Verify Loaded Profiles are unselected in "JAM" screen

  # ═══════════════════════════════════════════════════════════════════
  # JAM (JOB MAPPING) - NONE BUTTON METHOD
  # ═══════════════════════════════════════════════════════════════════

  @Clear_JAM_NoneButton @Clear_JAM
  Scenario: JAM - Navigate to Job Mapping and Select All for None Button Test
    When User is in Job Mapping page
    Then User should verify Job Mapping logo is displayed on screen
    Then Click on Chevron button beside header checkbox in Job Mapping screen
    Then Click on Select All button in Job Mapping screen
    Then User should verify Publish Selected Profiles button is enabled

  @Clear_JAM_NoneButton @Clear_JAM
  Scenario: JAM - Clear All Selection with None Button
    When User is in Job Mapping page
    Then Click on Chevron button beside header checkbox in Job Mapping screen
    Then Click on None button in "JAM" screen
    Then User should verify action button is disabled in "JAM" screen
    Then Scroll page to view more job profiles in "JAM" screen
    Then Scroll page to view more job profiles in "JAM" screen
    Then Verify all Loaded Profiles are Unselected in "JAM" screen

  @Clear_JAM_NoneButton @Clear_JAM
  Scenario: JAM - Select Loaded and Clear with None Button
    When User is in Job Mapping page
    Then Click on header checkbox to select loaded job profiles in "JAM" screen
    Then Scroll page to view more job profiles in "JAM" screen
    Then Verify Profiles loaded after clicking Header checkbox are not selected in "JAM" screen
    Then User should verify action button is enabled in "JAM" screen
    Then Click on Chevron button beside header checkbox in Job Mapping screen
    Then Click on None button in "JAM" screen
    Then User should verify action button is disabled in "JAM" screen
    Then Scroll page to view more job profiles in "JAM" screen
    Then Scroll page to view more job profiles in "JAM" screen
    Then Verify all Loaded Profiles are Unselected in "JAM" screen
    
   @SelectAll_Search_JAM @SelectAll_JAM
   Scenario: JAM - Search and Select All Profiles
    When User is in Job Mapping page
    Then Verify job profiles count is displaying on the page
    Then Verify Organization Jobs Search bar text box is clickable
    Then Verify Organization Jobs Search bar placeholder text
    Then Enter Job name substring in search bar
    Then User should verify count of job profiles is correctly showing on top of Job Profiles listing table
    Then User should scroll down to view last search result in "JAM" screen
    Then User should validate all search results contains substring used for searching in "JAM" screen
    Then Click on Chevron button beside header checkbox in "JAM" screen
    Then Click on Select All button in "JAM" screen
    Then User should verify action button is enabled in "JAM" screen
    Then Capture baseline of selected profiles in "JAM" screen
    Then Clear Search bar in "JAM" screen
    
   @Clear_JAM_NoneButton @Clear_JAM
   Scenario: JAM - Clear All Selection with None Button
    When User is in Job Mapping page
    Then Click on Chevron button beside header checkbox in Job Mapping screen
    Then Click on None button in "JAM" screen
    Then User should verify action button is disabled in "JAM" screen
    Then Scroll page to view more job profiles in "JAM" screen
    Then Scroll page to view more job profiles in "JAM" screen
    Then Verify all Loaded Profiles are Unselected in "JAM" screen

