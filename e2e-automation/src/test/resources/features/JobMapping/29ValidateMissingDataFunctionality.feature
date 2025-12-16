@Validate_Jobs_With_Missing_Data_In_JobMapping
Feature: Validate Jobs with Missing Data in Job Mapping UI
  # This feature tests validation of jobs with missing data:
  # - Grade, Department, Function, Subfunction
  #
  # Run specific data type using tags:
  # - @Missing_GRADE_Data - Test Grade missing data only
  # - @Missing_DEPARTMENT_Data - Test Department missing data only
  # - @Missing_FUNCTION_Data - Test Function missing data only
  # - @Missing_SUBFUNCTION_Data - Test Subfunction missing data only

  # ═══════════════════════════════════════════════════════════════════
  # NAVIGATION - Required first scenario
  # ═══════════════════════════════════════════════════════════════════

  @Navigate_To_Job_Mapping
  Scenario: Navigate to Job Mapping page
    Then Navigate to Job Mapping page from KFONE Global Menu in PM
    Then User should verify Job Mapping logo is displayed on screen

  # ═══════════════════════════════════════════════════════════════════
  # FORWARD FLOW - Find job in Job Mapping → Validate in Missing Data screen
  # Parameterized for all data types
  # ═══════════════════════════════════════════════════════════════════

  @Forward_Validate_Missing_Data_Flow
  Scenario Outline: Forward Flow - Find job with missing <DataType> in Job Mapping, then validate in Missing Data screen
    # Forward workflow: Job Mapping → find missing data job → Missing Data screen → validate
    When User is in Job Mapping page
    Then Sort Job Profiles by "<SortColumn>" in Ascending order
    Then Find job profile in Job Mapping page where "<DataType>" is missing
    And Extract "<DataType>" job details from found profile in Job Mapping page
    Then Click on "View & Re-upload jobs" link in Missing Data Tip Message
    Then Verify user is navigated to Jobs with Missing "<DataType>" Data screen
    And Search for the extracted "<DataType>" job profile by name in Jobs Missing Data screen
    Then Verify "<DataType>" job profile is found and displayed in Jobs Missing Data screen search results
    And Extract "<DataType>" job details from found profile in Jobs Missing Data screen
    And Verify all "<DataType>" job details match between Job Mapping page and Jobs Missing Data screen
    And Click on Close button to return to Job Mapping page from "<DataType>" validation
    Then Verify user is back on Job Mapping page after "<DataType>" validation
    Then Click on View Published toggle button to turn on
    Then Click on View Published toggle button to turn off

    @Missing_GRADE_Data @Forward_GRADE
    Examples:
      | DataType | SortColumn         |
      | Grade    | Organization Grade |

    @Missing_DEPARTMENT_Data @Forward_DEPARTMENT
    Examples:
      | DataType   | SortColumn |
      | Department | Department |

    @Missing_FUNCTION_Data @Forward_FUNCTION
    Examples:
      | DataType | SortColumn         |
      | Function | Organization Grade |

    @Missing_SUBFUNCTION_Data @Forward_SUBFUNCTION
    Examples:
      | DataType    | SortColumn         |
      | Subfunction | Organization Grade |

  # ═══════════════════════════════════════════════════════════════════
  # REVERSE FLOW - Missing Data screen → Validate in Job Mapping
  # Parameterized for all data types
  # ═══════════════════════════════════════════════════════════════════

  @Reverse_Validate_Missing_Data_Flow
  Scenario Outline: Reverse Flow - Validate jobs with missing <DataType> data from Missing Data screen to Job Mapping
    # Reverse workflow: Job Mapping → Missing Data screen → find missing data job → Job Mapping → validate
    When User is in Job Mapping page
    Then User should verify Job Mapping logo is displayed on screen
    Then Click on "View & Re-upload jobs" link in Missing Data Tip Message
    Then Verify user is navigated to Jobs with Missing "<DataType>" Data screen
    And Find job in Jobs Missing "<DataType>" Data screen where "<DataType>" is N/A
    And Extract all available job details from Jobs with Missing "<DataType>" Data screen
    And Click on Close button to return to Job Mapping page from "<DataType>" validation
    Then Verify user is back on Job Mapping page after "<DataType>" validation
    When Search for the extracted "<DataType>" job profile by name in Job Mapping page
    Then Verify "<DataType>" job profile is found and displayed in search results
    And Extract "<DataType>" job details from searched profile in Job Mapping page
    And Verify all "<DataType>" job details match between Jobs Missing Data screen and Job Mapping page
    And Verify Info Message is displayed on searched profile indicating missing "<DataType>" data
    And Verify Info Message contains text about reduced match accuracy due to missing "<DataType>" data

    @Missing_GRADE_Data @Reverse_GRADE
    Examples:
      | DataType |
      | Grade    |

    @Missing_DEPARTMENT_Data @Reverse_DEPARTMENT
    Examples:
      | DataType   |
      | Department |

    @Missing_FUNCTION_Data @Reverse_FUNCTION
    Examples:
      | DataType |
      | Function |

    @Missing_SUBFUNCTION_Data @Reverse_SUBFUNCTION
    Examples:
      | DataType    |
      | Subfunction |

