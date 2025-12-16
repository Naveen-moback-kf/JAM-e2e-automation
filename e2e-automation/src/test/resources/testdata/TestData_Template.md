# TestData.xlsx - Excel Data-Driven Testing Guide

## 📋 Overview

This guide explains how to use `TestData.xlsx` for **test data only** (search terms, filters, etc.).

**IMPORTANT:** Environment configuration (credentials, URLs) are now managed via environment-specific property files, NOT Excel.

---

## 🎯 Configuration Architecture

### Environment Configuration (NEW)

| Setting | Source | How to Change |
|---------|--------|---------------|
| **Environment** | `environments/{env}.properties` | `-Denv=qa` or `-Denv=stage` |
| **Login Credentials** | `environments/{env}.properties` | Edit the env file or use `-Dsso.username=xxx` |
| **PAMS_ID** | `environments/{env}.properties` | Edit the env file or use `-Dpams.id=xxx` |
| **URLs** | `config.properties` | All URLs defined centrally |
| **Browser** | `config.properties` | `-Dbrowser=firefox` |
| **Headless Mode** | `config.properties` | `-Dheadless.mode=false` |

### Test Data (Excel)

| What | Source | Location |
|------|--------|----------|
| **Search Terms** | Excel | SearchData sheet |
| **Filter Values** | Excel | FilterData sheet |
| **Sorting Data** | Excel | SortingData sheet |
| **Other Test Data** | Excel | Various sheets |

---

## 🚀 Quick Start

### Running Tests with Different Environments

```bash
# Run tests on QA environment (default)
mvn test -Denv=qa

# Run tests on Stage environment
mvn test -Denv=stage

# Run tests on Dev environment
mvn test -Denv=dev

# Run tests with SSO login on QA
mvn test -Denv=qa -Dlogin.type=SSO

# Run tests with visible browser (non-headless)
mvn test -Denv=qa -Dheadless.mode=false

# Override credentials for CI/CD
mvn test -Denv=stage -Dsso.password=${SECRET_PASSWORD}
```

### Environment Files Location

```
src/test/resources/
├── config.properties              # Common settings (browser, headless, URLs)
├── environments/
│   ├── dev.properties             # Dev: credentials, PAMS_ID
│   ├── qa.properties              # QA: credentials, PAMS_ID
│   ├── stage.properties           # Stage: credentials, PAMS_ID
│   ├── prod-us.properties         # Prod-US: credentials, PAMS_ID
│   └── prod-eu.properties         # Prod-EU: credentials, PAMS_ID
└── testdata/
    ├── TestData.xlsx              # Test data ONLY (search, filters, etc.)
    └── TestData_Template.md       # This guide
```

---

## 📊 Excel Sheet Structures

### Sheet 1: SearchData
For search functionality testing.

| Column | Required | Description |
|--------|----------|-------------|
| TestID | Yes | Unique identifier (e.g., S001, S002) |
| SearchTerm | Yes | Text to search for |
| MinResults | No | Minimum expected results |
| MaxResults | No | Maximum expected results |
| Description | No | Test case description |

**Sample Data:**
```
| TestID | SearchTerm  | MinResults | MaxResults | Description          |
|--------|-------------|------------|------------|----------------------|
| S001   | Developer   | 5          | 100        | Search by job title  |
| S002   | Manager     | 10         | 50         | Search by role       |
| S003   | JOB01       | 1          | 10         | Search by job code   |
| S004   | Engineering | 3          | 30         | Search by department |
| S005   | ZZZZZ       | 0          | 0          | Invalid search       |
```

---

### Sheet 2: FilterData
For filter functionality testing.

| Column | Required | Description |
|--------|----------|-------------|
| TestID | Yes | Unique identifier (e.g., F001, F002) |
| FilterType | Yes | Grades, Departments, Functions, MappingStatus |
| FilterValue | Yes | Value to select in filter |
| SelectionType | No | Single or Multiple |
| Screen | No | JAM or PM |
| Description | No | Test case description |

**Sample Data:**
```
| TestID | FilterType  | FilterValue | SelectionType | Screen | Description          |
|--------|-------------|-------------|---------------|--------|----------------------|
| F001   | Grades      | JGL01       | Single        | JAM    | Filter by grade      |
| F002   | Grades      | JGL02       | Single        | JAM    | Filter by grade 2    |
| F003   | Departments | Engineering | Single        | JAM    | Filter by department |
| F004   | Departments | HR          | Single        | JAM    | Filter by HR dept    |
| F005   | Functions   | Finance     | Single        | JAM    | Filter by function   |
```

---

### Sheet 3: SortingData
For sorting functionality testing.

| Column | Required | Description |
|--------|----------|-------------|
| TestID | Yes | Unique identifier (e.g., SORT001) |
| Screen | No | JAM or PM |
| Column | Yes | Column name to sort by |
| SortOrder | Yes | Ascending or Descending |
| Description | No | Test case description |

**Sample Data:**
```
| TestID  | Screen | Column                | SortOrder  | Description              |
|---------|--------|-----------------------|------------|--------------------------|
| SORT001 | JAM    | Organization Job Name | Ascending  | Sort by name A-Z         |
| SORT002 | JAM    | Organization Job Name | Descending | Sort by name Z-A         |
| SORT003 | JAM    | Organization Grade    | Ascending  | Sort by grade low-high   |
| SORT004 | JAM    | Matched SP Grade      | Descending | Sort by SP grade high-low|
| SORT005 | PM     | Name                  | Ascending  | Sort PM profiles by name |
```

---

### Sheet 4: MissingDataTests
For missing data validation testing.

| Column | Required | Description |
|--------|----------|-------------|
| TestID | Yes | Unique identifier (e.g., MD001) |
| MissingField | Yes | GRADE, DEPARTMENT, FUNCTION, SUBFUNCTION |
| SortColumn | No | Column to sort by before finding |
| FlowType | Yes | Forward or Reverse |
| Description | No | Test case description |

**Sample Data:**
```
| TestID | MissingField | SortColumn         | FlowType | Description                |
|--------|--------------|--------------------| ---------|----------------------------|
| MD001  | GRADE        | Organization Grade | Forward  | Find missing grade forward |
| MD002  | GRADE        | Organization Grade | Reverse  | Find missing grade reverse |
| MD003  | DEPARTMENT   | Department         | Forward  | Find missing dept forward  |
| MD004  | FUNCTION     | Organization Grade | Forward  | Find missing func forward  |
```

---

## 🔧 Usage Examples

### In Feature Files

```gherkin
@DataDriven
Feature: Data-Driven Testing Examples

  # Simple data-driven search
  Scenario: Search using Excel data - S001
    When User is in Job Mapping page
    Then Search using Excel test data "S001"
    Then User should verify count of job profiles is correctly showing

  # Data-driven with Scenario Outline
  Scenario Outline: Data-Driven Search - <TestID>
    When User is in Job Mapping page
    Then Search using Excel test data "<TestID>"
    Then User should verify count of job profiles is correctly showing

    Examples:
      | TestID |
      | S001   |
      | S002   |
      | S003   |

  # Data-driven filter
  Scenario: Apply filter using Excel data
    When User is in Job Mapping page
    Then Click on Filters dropdown button
    Then Apply filter using Excel test data "F001"
    Then User should verify count of job profiles is correctly showing

  # Data-driven sorting
  Scenario: Apply sorting using Excel data
    When User is in Job Mapping page
    Then Apply sorting using Excel test data "SORT001"
    Then User should verify job profiles are sorted
```

### In Java Code

```java
// Get all data from a sheet
List<Map<String, String>> allSearchData = ExcelDataProvider.getSheetData("SearchData");

// Get specific row by TestID
Map<String, String> testData = ExcelDataProvider.getTestData("SearchData", "S001");
String searchTerm = testData.get("SearchTerm");

// Get single value directly
String filterValue = ExcelDataProvider.getValue("FilterData", "F001", "FilterValue");

// Filter data by column
List<Map<String, String>> jamFilters = ExcelDataProvider.getDataByColumn("FilterData", "Screen", "JAM");

// Use as TestNG DataProvider
@DataProvider(name = "searchTests")
public Object[][] getSearchTests() {
    return ExcelDataProvider.createDataProvider("SearchData");
}
```

### Using Configuration in Java Code

```java
// All config values are available via CommonVariable
String environment = CommonVariable.ENVIRONMENT;      // "QA", "Stage", etc.
String loginType = CommonVariable.LOGIN_TYPE;         // "SSO" or "NON_SSO"
String pamsId = CommonVariable.TARGET_PAMS_ID;        // "23139"
String browser = CommonVariable.BROWSER;              // "chrome"
String ssoUser = CommonVariable.SSO_USERNAME;
String nonSsoUser = CommonVariable.NON_SSO_USERNAME;

// URLs are also available
String qaUrl = CommonVariable.KFONE_QAURL;
String stageUrl = CommonVariable.KFONE_STAGEURL;
```

---

## 📁 File Locations

```
e2e-automation/
├── src/main/java/com/kfonetalentsuite/utils/common/
│   ├── VariableManager.java       ← Loads all config (env + common)
│   └── CommonVariable.java        ← Static config variables (use these!)
│
└── src/test/resources/
    ├── config.properties          ← Common settings
    ├── environments/
    │   ├── dev.properties         ← Dev environment
    │   ├── qa.properties          ← QA environment
    │   ├── stage.properties       ← Stage environment
    │   ├── prod-us.properties     ← Prod-US environment
    │   └── prod-eu.properties     ← Prod-EU environment
    └── testdata/
        ├── TestData.xlsx          ← Test data only
        └── TestData_Template.md   ← This guide
```

---

## ⚠️ Important Notes

1. **Environment Selection**: Always use `-Denv=xxx` to select environment
2. **Excel for Test Data Only**: Don't put credentials or environment config in Excel anymore
3. **CI/CD Secrets**: Use `-Dsso.password=${SECRET}` for sensitive values
4. **TestID Column**: Required in every Excel sheet for lookup functionality
5. **File Format**: Save Excel as .xlsx (Excel 2007+)

---

## 🔄 Migration from Excel-based Environment Config

If you were using the `LoginData` sheet with `Execute=YES` for environment switching:

**Old Way (Excel):**
1. Open TestData.xlsx
2. Find the row for your environment
3. Set Execute=YES for that row
4. Set Execute=NO for other rows
5. Run tests

**New Way (Environment Files):**
```bash
# Just add -Denv parameter!
mvn test -Denv=qa           # QA environment
mvn test -Denv=stage        # Stage environment
mvn test -Denv=dev          # Dev environment
```

---

## 📞 Support

For questions or issues, contact the Automation Team.
