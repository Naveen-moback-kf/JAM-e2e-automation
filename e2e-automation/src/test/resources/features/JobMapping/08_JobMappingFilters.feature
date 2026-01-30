@Validate_Filters_Functionality_JAM
Feature: Validate All Filter Types in Job Mapping UI

  @Navigate_To_Job_Mapping
  Scenario: Navigate to Job Mapping page
    Then Navigate to Job Mapping page from KFONE Global Menu in PM
    Then User should verify Job Mapping logo is displayed on screen

  @ApplyAndValidateSingleFilter
  Scenario Outline: User filters job profiles by single <FilterType> option
    When User is in Job Mapping page
    Then Verify job profiles count is displaying on the page
    Then Click on Filters dropdown button
    Then Click on "<FilterType>" Filters dropdown button
    And Select one option in "<FilterType>" Filters dropdown
    And Close the Filters dropdown
    And User should verify count of job profiles is correctly showing on top of Job Profiles listing table
    Then User should scroll down to view last result with applied filters
    Then Validate Job Mapping Profiles are correctly filtered with applied "<FilterType>" Options
    Then Click on Clear Filters button

    @Grades_Filter
    Examples:
      | FilterType |
      | Grades     |

    @Departments_Filter
    Examples:
      | FilterType  |
      | Departments |

    @MappingStatus_Filter
    Examples:
      | FilterType    |
      | MappingStatus |
  
  @ApplyAndValidateMultipleFilters
  Scenario Outline: User filters job profiles by multiple <FilterType> options
    When User is in Job Mapping page
    Then Verify job profiles count is displaying on the page
    Then Click on Filters dropdown button
    Then Click on "<FilterType>" Filters dropdown button
    And Select two options in "<FilterType>" Filters dropdown
    And Close the Filters dropdown
    And User should verify count of job profiles is correctly showing on top of Job Profiles listing table
    Then User should scroll down to view last result with applied filters
    Then Validate Job Mapping Profiles are correctly filtered with applied "<FilterType>" Options
    Then Click on Clear Filters button

    @Grades_MultiSelect
    Examples:
      | FilterType |
      | Grades     |

    @Departments_MultiSelect
    Examples:
      | FilterType  |
      | Departments |
  
  @ValidateClearIndividualFilter
  Scenario Outline: User clears individual <FilterType> filter using clear(x) button
    When User is in Job Mapping page
    Then Verify job profiles count is displaying on the page
    Then Click on Filters dropdown button
    Then Click on "<FilterType>" Filters dropdown button
    And Select one option in "<FilterType>" Filters dropdown
    And Close the Filters dropdown
    And User should verify count of job profiles is correctly showing on top of Job Profiles listing table
    Then Click on clear(x) applied filter
    Then Verify job profiles count is displaying on the page

    @Grades_Clear
    Examples:
      | FilterType |
      | Grades     |

    @Departments_Clear
    Examples:
      | FilterType  |
      | Departments |

    @MappingStatus_Clear
    Examples:
      | FilterType    |
      | MappingStatus |
  
  @FunctionsSubfunctions_AutoSelect
  Scenario: User filters by function and verifies all subfunctions are auto-selected
    When User is in Job Mapping page
    Then Verify job profiles count is displaying on the page
    Then Click on Filters dropdown button
    Then Click on "FunctionsSubfunctions" Filters dropdown button
    Then Select a Function and verify all Subfunctions inside Function are selected automatically
    And Close the Filters dropdown
    And User should verify count of job profiles is correctly showing on top of Job Profiles listing table
    Then User should scroll down to view last result with applied filters
    Then Validate Job Mapping Profiles are correctly filtered with applied "FunctionsSubfunctions" Options
    Then Click on Clear Filters button

  @FunctionsSubfunctions_SpecificSubfunction
  Scenario: User validates search bar and filters by specific subfunctions within a function
    When User is in Job Mapping page
    Then Verify job profiles count is displaying on the page
    Then Click on Filters dropdown button
    Then Click on "FunctionsSubfunctions" Filters dropdown button
    Then Click inside search bar and enter function name
    Then User should click on dropdown button of Searched function name
    And Select one Subfunction option inside Function Name dropdown
    And Close the Filters dropdown
    And User should verify count of job profiles is correctly showing on top of Job Profiles listing table
    Then User should scroll down to view last result with applied filters
    Then Validate Job Mapping Profiles are correctly filtered with applied "FunctionsSubfunctions" Options
    Then Click on Clear Filters button

  @FunctionsSubfunctions_MultipleSubfunctions
  Scenario: User filters by multiple subfunctions within a function
    When User is in Job Mapping page
    Then Verify job profiles count is displaying on the page
    Then Click on Filters dropdown button
    Then Click on "FunctionsSubfunctions" Filters dropdown button
    Then Click inside search bar and enter function name
    Then User should click on dropdown button of Searched function name
    And Select two subfunction options inside Function Name Filters dropdown
    And Close the Filters dropdown
    And User should verify count of job profiles is correctly showing on top of Job Profiles listing table
    Then User should scroll down to view last result with applied filters
    Then Validate Job Mapping Profiles are correctly filtered with applied "FunctionsSubfunctions" Options
    Then Click on Clear Filters button

  @FunctionsSubfunctions_Clear
  Scenario: User clears individual function subfunction filter using clear(x) button
    When User is in Job Mapping page
    Then Verify job profiles count is displaying on the page
    Then Click on Filters dropdown button
    Then Click on "FunctionsSubfunctions" Filters dropdown button
    Then Select a Function and verify all Subfunctions inside Function are selected automatically
    And Close the Filters dropdown
    And User should verify count of job profiles is correctly showing on top of Job Profiles listing table
    Then Click on clear(x) applied filter
    Then Verify job profiles count is displaying on the page
  
  @ApplyMultipleFiltersInViewPublished
  Scenario: Apply multiple filters in View Published screen
    When User is in Job Mapping page
    Then Click on View Published toggle button to turn on
    Then Verify job profiles count is displaying on the page
    Then Click on Filters dropdown button
    Then Click on "Grades" Filters dropdown button
    And Select one option in "Grades" Filters dropdown
    Then Click on "Departments" Filters dropdown button
    And Select one option in "Departments" Filters dropdown
    Then Click on "FunctionsSubfunctions" Filters dropdown button
    Then Select a Function and verify all Subfunctions inside Function are selected automatically
    And Close the Filters dropdown
    Then Validate Job Mapping Profiles are correctly filtered with applied Grades Departments and FunctionsSubfunctions Options
