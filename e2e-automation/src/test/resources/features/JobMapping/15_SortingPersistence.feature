@Sorting_Persistence
Feature: Validate Sorting Persistence in Job Mapping UI

  # ═══════════════════════════════════════════════════════════════════════════════
  # SETUP - Navigate to Job Mapping
  # ═══════════════════════════════════════════════════════════════════════════════

  @Sorting_Persistence_Setup
  Scenario: Navigate to Job Mapping page
    Then Navigate to Job Mapping page from KFONE Global Menu in PM
    Then User should verify Job Mapping logo is displayed on screen

  # ═══════════════════════════════════════════════════════════════════════════════
  # BASIC SORTING PERSISTENCE - Multi-Level Sorting
  # ═══════════════════════════════════════════════════════════════════════════════

  @Basic_Sorting_Persistence @MultiLevel_Sorting
  Scenario: Apply multi-level sorting by Organization Grade and Job Name
    When User is in Job Mapping page
    Then Verify job profiles count is displaying on the page
    Then Sort Job Profiles by Organization Grade in Ascending order
    Then Sort Job Profiles by Organization Job Name in Ascending order

  @Basic_Sorting_Persistence @MultiLevel_Sorting
  Scenario: Verify multi-level sorting persists after page refresh
    When User is in Job Mapping page with multi-level sorting applied
    Then Refresh Job Mapping page
    And Verify Applied Sorting persist on Job Mapping UI

  @Basic_Sorting_Persistence @MultiLevel_Sorting
  Scenario: Verify sorting persists after navigating to Job Comparison page and back
    When User is in Job Mapping page with multi-level sorting applied
    Then Search for Job Profile with View Other Matches button
    Then Click on View Other Matches button
    Then Verify user navigated to job comparison page
    Then Click on Browser back button
    Then User should be landed on Job Mapping page
    When User is in Job Mapping page
    And Verify Applied Sorting persist on Job Mapping UI

  @Basic_Sorting_Persistence @MultiLevel_Sorting
  Scenario: Verify sorting persists after navigating to Profile Manager and back
    When User is in Job Mapping page with multi-level sorting applied
    Then Click on KFONE Global Menu in Job Mapping UI
    Then Click on Profile Manager application button in KFONE Global Menu
    Then Verify user should land on Profile Manager dashboard page
    When User is on Profile Manager page
    Then Navigate to Job Mapping page from KFONE Global Menu in PM
    Then User should be landed on Job Mapping page
    Then Verify Applied Sorting persist on Job Mapping UI

  # ═══════════════════════════════════════════════════════════════════════════════
  # ADVANCED SORTING PERSISTENCE - Sorting with Filters
  # ═══════════════════════════════════════════════════════════════════════════════

  @Advanced_Sorting_Persistence @Sorting_With_Filters
  Scenario: Apply filters while sorting is active
    When User is in Job Mapping page
    Then Verify job profiles count is displaying on the page
    Then Sort Job Profiles by Organization Grade in Ascending order
    Then Click on Filters dropdown button
    Then Click on Mapping Status Filters dropdown button
    And Select one option in Mapping Status Filters dropdown
    And Close the Filters dropdown

  @Advanced_Sorting_Persistence @Sorting_With_Filters
  Scenario: Verify sorting persists after navigating to Manual Job Mapping screen and back
    When User is in Job Mapping page with sorting and filters applied
    Then User should search for Job Profile with Search a Different Profile button on Mapped Success Profile
    Then Click on Search a Different Profile button on Mapped Success Profile
    Then User should be navigated to Manual Job Mapping screen
    Then Click on Browser back button
    Then User should be landed on Job Mapping page
    When User is in Job Mapping page
    And Verify Applied Sorting persist on Job Mapping UI
    Then Click on Clear Filters button

  # ═══════════════════════════════════════════════════════════════════════════════
  # ADVANCED SORTING PERSISTENCE - View Published Screen
  # ═══════════════════════════════════════════════════════════════════════════════

  @Advanced_Sorting_Persistence @ViewPublished_Sorting
  Scenario: Apply sorting by Matched Success Profile Grade in View Published screen
    When User is in Job Mapping page
    Then Click on View Published toggle button to turn on
    Then Verify job profiles count is displaying on the page
    Then Sort Job Profiles by Matched Success Profile Grade in Ascending order
    Then User should scroll page down two times to view first thirty job profiles

  @Advanced_Sorting_Persistence @ViewPublished_Sorting
  Scenario: Verify View Published toggle and sorting persist after page refresh
    When User is in View Published screen with sorting applied
    Then Refresh Job Mapping page
    Then Verify View Published toggle button is Persisted
    And Verify Applied Sorting persist on Job Mapping UI

  @Advanced_Sorting_Persistence @ViewPublished_Sorting
  Scenario: Verify View Published toggle and sorting persist after Profile Manager navigation
    When User is in View Published screen with sorting applied
    Then Click on KFONE Global Menu in Job Mapping UI
    Then Click on Profile Manager application button in KFONE Global Menu
    Then Verify user should land on Profile Manager dashboard page
    When User is on Profile Manager page
    Then Navigate to Job Mapping page from KFONE Global Menu in PM
    Then User should be landed on Job Mapping page
    Then Verify View Published toggle button is Persisted
    Then Verify Applied Sorting persist on Job Mapping UI

