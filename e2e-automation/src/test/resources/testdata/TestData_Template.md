# TestData.xlsx - Excel Data-Driven Testing Guide

## 📋 Overview

This guide explains how to create and use `TestData.xlsx` for data-driven testing.
The Excel file should be placed in: `src/test/resources/testdata/TestData.xlsx`

---

## 🎯 Configuration Sources

| What | Source | Location |
|------|--------|----------|
| **Login Credentials** | Excel | LoginData sheet |
| **Environment** | Excel | LoginData.Environment column |
| **PAMS_ID** | Excel | LoginData.PAMS_ID column |
| **Browser** | config.properties | browser=chrome |
| **Headless Mode** | config.properties | headless.mode=false |
| **URLs** | config.properties | KFONE_*Url values |

**CI/CD Override:** Use Maven parameters like `-DEnvironment=Stage`

---

## 🚀 Quick Start

### Step 1: Create TestData.xlsx
Create a new Excel file with the sheets described below.

### Step 2: Set Execute=YES
In LoginData sheet, set `Execute=YES` for the row you want to use.

### Step 3: Run Test
```bash
mvn test -Dcucumber.filter.tags="@NON_SSO_Login_via_KFONE"
```

---

## 📊 Sheet Structures

### Sheet 1: LoginData (MAIN SHEET)
Login credentials and environment selection. **Execute=YES row is used for testing.**

| Column | Required | Description |
|--------|----------|-------------|
| Execute | Yes | **YES** = Use this row, **NO** = Skip |
| TestID | Yes | Unique identifier (L001, L002, etc.) |
| UserType | Yes | SSO or NON_SSO |
| Environment | Yes | Dev, QA, Stage - **Determines which URL to use** |
| Username | Yes | Login email/username |
| Password | Yes | Login password |
| PAMS_ID | No | PAMS ID for this user |
| Description | No | Description of this test user |

**Sample Data:**
```
| Execute | TestID | UserType | Environment | Username                                        | Password           | PAMS_ID | Description              |
|---------|--------|----------|-------------|-------------------------------------------------|--------------------|---------|--------------------------|
| NO      | L001   | NON_SSO  | Dev         | clm.user.one@testkfy.com                        | 202510DigitalLog!  | 23139   | NON-SSO Dev User         |
| NO      | L002   | SSO      | Dev         | AIAuto.user.one@kfdbhdevoutlook.onmicrosoft.com | 202410TestDigital! | 4790    | SSO Dev User             |
| YES     | L003   | NON_SSO  | QA          | clm.user.one@testkfy.com                        | 202510DigitalLog!  | 23139   | NON-SSO QA User          |
| NO      | L004   | SSO      | QA          | AIAuto.user.one@kfdbhdevoutlook.onmicrosoft.com | 202410TestDigital! | 4790    | SSO QA User              |
| NO      | L005   | NON_SSO  | Stage       | clm.user.one@testkfy.com                        | 202510DigitalLog!  | 12024   | NON-SSO Stage User       |
```

**How to switch environment & login:** Just change `Execute` to `YES` for the desired row!

When Execute=YES for L003:
- Uses **QA** Environment (from Environment column)
- Uses **NON_SSO** credentials (from Username/Password columns)
- Opens **QA URL** (from config.properties)

---

### Sheet 3: SearchData
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

### Sheet 2: LoginData
For login functionality testing.

| Column | Required | Description |
|--------|----------|-------------|
| TestID | Yes | Unique identifier (e.g., L001, L002) |
| UserType | Yes | SSO or NON_SSO |
| Environment | No | QA, Stage, Prod |
| Username | Yes | Login username/email |
| Password | Yes | Login password |
| PAMS_ID | No | PAMS ID for client verification |
| ExpectedRole | No | Expected user role after login |
| Description | No | Test case description |

**Sample Data:**
```
| TestID | UserType | Environment | Username              | Password   | PAMS_ID | ExpectedRole |
|--------|----------|-------------|-----------------------|------------|---------|--------------|
| L001   | NON_SSO  | QA          | admin@testkfy.com     | Pass123!   | 23139   | Admin        |
| L002   | NON_SSO  | QA          | viewer@testkfy.com    | Pass456!   | 23140   | Viewer       |
| L003   | SSO      | QA          | sso.user@outlook.com  | SSOPass!   | 4790    | Admin        |
| L004   | NON_SSO  | Stage       | stage@testkfy.com     | Stage123!  | 12024   | Admin        |
```

---

### Sheet 3: FilterData
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

### Sheet 4: SortingData
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

### Sheet 5: MissingDataTests
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

  # Data-driven login
  Scenario: Login using Excel data
    Given Launch the KFONE application
    When User logs in using Excel test data "L001"
    Then Verify the KFONE landing page

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
List<Map<String, String>> allUsers = ExcelDataProvider.getSheetData("LoginData");

// Get specific row by TestID
Map<String, String> testData = ExcelDataProvider.getTestData("SearchData", "S001");
String searchTerm = testData.get("SearchTerm");

// Get single value directly
String username = ExcelDataProvider.getValue("LoginData", "L001", "Username");

// Filter data by column
List<Map<String, String>> qaUsers = ExcelDataProvider.getDataByColumn("LoginData", "Environment", "QA");

// Use as TestNG DataProvider
@DataProvider(name = "searchTests")
public Object[][] getSearchTests() {
    return ExcelDataProvider.createDataProvider("SearchData");
}
```

---

## 📁 File Location

```
e2e-automation/
└── src/test/resources/
    └── testdata/
        ├── TestData.xlsx          ← Your Excel file goes here
        └── TestData_Template.md   ← This guide
```

---

## ⚠️ Important Notes

1. **TestID Column**: Required in every sheet for lookup functionality
2. **First Row**: Must be column headers
3. **File Format**: Save as .xlsx (Excel 2007+)
4. **Empty Rows**: Automatically skipped
5. **Column Names**: Case-sensitive, must match exactly
6. **Special Characters**: Use quotes in Excel for values with commas

---

## 🔄 For Other Teams

If your team needs a separate test data file:

```java
// Set custom file path before using
ExcelDataProvider.setCustomFilePath("src/test/resources/myteam/MyTestData.xlsx");

// Now all calls will use your custom file
Map<String, String> data = ExcelDataProvider.getTestData("MySheet", "TC001");

// Reset to default when done
ExcelDataProvider.clearCustomFilePath();
```

---

## 📞 Support

For questions or issues, contact the Automation Team.
