# Job Mapping - Enterprise QA Automation Framework

A comprehensive, enterprise-grade end-to-end automation testing framework for Korn Ferry's AI-powered Job Mapping and Profile Management functionality. This framework focuses on Job Mapping workflows with specialized manual mapping capabilities across multiple environments, featuring robust error handling, visual reporting, and session recovery mechanisms.

## 🚀 Latest Enhancements (November 2025)

### **📊 Test Suite Organization & Coverage Enhancement (NEW)**
- **Hierarchical Test Suites** - 3 strategically organized suites: Sanity (7 tests), Smoke (18 tests), Regression (55 tests)
- **Smart Execution Strategy** - Clear guidelines for optimal test execution at different stages
- **JAM Test Suites** - Standardized naming: JAM_SanityTestSuite.xml, JAM_SmokeTestSuite.xml, JAM_RegressionTestSuite.xml
- **Coverage Documentation** - Complete TEST_SUITE_COVERAGE.md for transparent suite mapping
- **Execution Efficiency** - Reduced feedback time from 60-90 min (full suite) to 5-10 min (sanity) or 15-25 min (smoke)
- **Build Verification** - Quick sanity checks ensure build stability before deeper testing

### **📁 Framework Modernization & Refactoring**
- **Codebase Restructuring** - Complete migration from `AIautoMap` to `JobMapping` package naming
- **Scenario Breakdown & Optimization** - Large multi-step scenarios refactored into focused, maintainable tests
- **Tag Cleanup** - Simplified tagging system with one primary tag per scenario for better organization
- **Import Optimization** - Consistent import statements and package references across all Java files
- **Documentation Updates** - Comprehensive README and inline documentation updates

### **🌐 Cross-Browser Testing Suite**
- **Multi-Browser Support** - Chrome, Firefox, and Edge parallel execution
- **26 Cross-Browser Runners** with automatic browser configuration
- **Cross-Browser Dashboard** - Dedicated analytics sheet for browser compatibility
- **Browser Status Columns** - Chrome/Firefox/Edge status tracking in Excel reports
- **Thread-Safe Execution** - Custom thread naming and isolation for parallel browser tests
- **Compatibility Scoring** - Business impact assessment based on browser compatibility

### **📊 Enhanced Excel Reporting System**
- **Dual Dashboard Architecture** - Separate dashboards for Normal and Cross-Browser execution
- **Browser-Specific Reporting** - Individual browser status columns (Chrome/Firefox/Edge)
- **Execution Type Detection** - Smart differentiation between Normal and Cross-Browser runs
- **Scenario Order Consistency** - Cross-browser scenarios match normal execution sequence
- **Professional Color Coding** - Complete visual styling for all value cells and metrics
- **Business Impact Logic** - Accurate risk assessment for both execution types

### **📸 Advanced Screenshot Functionality**
- **Automatic failure capture** with visual evidence for all test failures
- **ExtentReports integration** with embedded screenshots
- **Structured storage** organized by date and test method
- **31+ Page Objects enhanced** with consistent screenshot handling
- **Configurable capture** with cleanup management

### **🔄 Session Recovery System**
- **Screen-off immunity** preventing session failures during unattended execution
- **Automatic session restoration** with retry mechanisms (up to 3 attempts)
- **Enhanced Chrome configurations** with 13+ power management prevention options
- **SessionRecoveryWrapper** utility for operation-level protection

### **⚙️ Robust Configuration Management**
- **Headless mode configuration** via `config.properties` (prevents screen-off issues)
- **Excel reporting toggle** with `excel.reporting=true/false` for performance control
- **Enhanced logging system** with optimized verbosity and clear console vs file separation  
- **Automatic property loading** with fail-safe defaults
- **Environment-specific configurations** for all supported regions
- **Clean codebase** with consistent package naming throughout

---

## Project Architecture

This project is built using modern, enterprise-ready technologies:

- **Java 17** - Core programming language with latest LTS features
- **Maven 3.x** - Dependency management and build automation
- **Selenium WebDriver 4.25.0** - Advanced web automation with session recovery
- **Cucumber BDD 7.18.1** - Behavior-driven development framework
- **TestNG 7.x** - Comprehensive test execution and management
- **ExtentReports** - Visual test reporting with screenshot integration
- **Log4j2 2.25.1** - Professional logging with configurable outputs
- **Apache POI 5.3.0** - Excel file handling for data-driven testing and reporting

## 📁 Project Structure

```
e2e-automation/
├── src/
│   ├── main/java/com/JobMapping/
│   │   ├── listeners/           # TestNG listeners with Excel integration
│   │   │   └── ExcelReportListener.java      # Enhanced Excel reporting (Normal and Cross-Browser execution)
│   │   ├── manager/            # Page Object Manager
│   │   │   └── PageObjectManager.java        # Centralized page object management
│   │   ├── pageobjects/        # 31 Page Objects with screenshot functionality
│   │   │   ├── PO01_LoginPage.java
│   │   │   ├── PO01_KFoneLogin.java
│   │   │   ├── PO02_VerifyJobMappingPageComponents.java
│   │   │   ├── PO03-PO32...                 # Full suite of page objects
│   │   │   └── ...
│   │   ├── testNGAnalyzer/     # Retry analyzer and transformers
│   │   │   ├── RetryAnalyzer.java
│   │   │   └── MyTransform.java
│   │   ├── utils/              # Enhanced utilities and helpers
│   │   │   ├── ScreenshotHandler.java        # Advanced screenshot management
│   │   │   ├── DailyExcelTracker.java       # Dual-dashboard Excel reporting system
│   │   │   ├── PDFReportGenerator.java      # PDF conversion utilities
│   │   │   ├── Utilities.java               # Common utilities
│   │   │   ├── ExcelStyleHelper.java        # Excel formatting utilities
│   │   │   ├── EnhancedLogger.java          # Advanced logging features
│   │   │   ├── SmartWaits.java              # Intelligent wait strategies
│   │   │   ├── API/                         # API utilities
│   │   │   │   └── kfApi.java
│   │   │   └── common/                      # Configuration management
│   │   │       ├── CommonVariable.java
│   │   │       ├── VariableManager.java
│   │   │       └── DynamicTagResolver.java
│   │   └── webdriverManager/   # Enhanced WebDriver with session recovery
│   │       ├── DriverManager.java            # Normal execution browser management
│   │       ├── CrossBrowserDriverManager.java # Cross-browser execution management
│   │       └── CustomizeTestNGCucumberRunner.java
│   └── test/
│       ├── java/
│       │   ├── stepdefinitions/ # Cucumber step definitions
│       │   │   ├── functional/
│       │   │   │   └── JobMapping/    # Job Mapping step definitions (31 files)
│       │   │   │       ├── SD01_LoginPage.java
│       │   │   │       ├── SD01_KFoneLogin.java
│       │   │   │       ├── SD02-SD32...
│       │   │   │       └── ...
│       │   │   └── hooks/       # Test hooks and scenario management
│       │   │       ├── ScenarioHooks.java
│       │   │       └── ConditionalScenarioSkip.java
│       │   └── testrunners/     # Test runners (33 Normal + 26 Cross-Browser)
│       │       └── functional/JobMapping/
│       │           ├── Runner01_LoginPage.java
│       │           ├── Runner01_KFoneLogin.java
│       │           ├── Runner02-Runner32... (33 Normal Test Runners)
│       │           └── crossbrowser/         # Cross-Browser Test Runners (26 files)
│       │               ├── CrossBrowser01_LoginPageRunner.java
│       │               ├── CrossBrowser02-CrossBrowser32...
│       │               └── ...
│       └── resources/
│           ├── features/        # Cucumber feature files
│           │   ├── functional/
│           │   │   ├── 01LoginPage.feature
│           │   │   ├── 01KFoneLogin.feature
│           │   │   └── JobMapping/    # 31 Job Mapping feature files
│           │   │       ├── 02VerifyJobMappingPageComponents.feature
│           │   │       ├── 03PublishJobProfile.feature
│           │   │       ├── 04-32...
│           │   │       └── ...
│           ├── config.properties      # Enhanced configuration with Excel reporting toggle
│           ├── cucumber.properties    # Cucumber specific configuration
│           ├── extent-config.xml     # ExtentReports configuration
│           ├── extent.properties     # ExtentReports properties
│           └── log4j2.properties    # Professional logging setup
├── Screenshots/                # Organized screenshot storage
│   └── FailureScreenshots/     # Date-organized failure screenshots
├── ExcelReports/              # Business-friendly Excel reports
│   ├── JobMappingAutomationTestResults.xlsx
│   └── Backup/                # Historical backup files
├── JobCatalogBackups/         # Job Catalog CSV backup files (created before each refresh)
├── logs/                      # Application logs (console & file)
├── Report/                    # ExtentReports with visual evidence
├── test-output/              # TestNG comprehensive output
├── pom.xml                   # Maven project configuration
├── JAM_SanityTestSuite.xml   # Sanity test suite (7 critical path tests - 5-10 min)
├── JAM_SmokeTestSuite.xml    # Smoke test suite (18 key feature tests - 15-25 min)
├── JAM_RegressionTestSuite.xml # Regression test suite (55 comprehensive tests - 60-90 min)
└── TEST_SUITE_COVERAGE.md    # Complete test suite coverage mapping
```

## ✨ Key Features

### 🌐 Cross-Browser Compatibility Testing
- **Multi-Browser Execution**: Parallel testing across Chrome, Firefox, and Edge
- **26 Self-Contained Runners**: Independent cross-browser test runners with automatic browser configuration
- **Thread-Safe Architecture**: Custom thread naming and isolation for reliable parallel execution
- **Browser Compatibility Analytics**: Comprehensive cross-browser performance metrics and scoring
- **Unified Reporting**: Browser-specific status tracking with consolidated Excel reporting
- **Business Impact Assessment**: Compatibility-based risk evaluation for stakeholder insights

### 🛡️ Enterprise-Grade Reliability
- **Session Recovery**: Automatic handling of browser session failures
- **Screen-off Immunity**: Tests continue even when screen locks or goes to sleep
- **Retry Mechanisms**: Built-in retry logic with configurable attempts
- **Robust Error Handling**: Comprehensive exception management with visual evidence

### 📸 Advanced Visual Documentation
- **Automatic Screenshots**: Every failure captured with contextual information
- **ExtentReports Integration**: Visual test reports with embedded evidence
- **Structured Storage**: Date-organized screenshots for easy tracking
- **Cleanup Management**: Configurable retention policies for storage optimization

### 📊 Professional Reporting
- **Dual Excel Dashboards**: Separate dashboards for Normal and Cross-Browser execution analytics
- **Cross-Browser Metrics**: Compatibility scores, browser-specific results, and business impact assessment
- **Smart Daily Reset**: Intelligent handling of same-day vs new-day executions
- **Browser Status Tracking**: Individual Chrome/Firefox/Edge status columns in detailed reports
- **ExtentReports**: Technical reports with timeline views and visual evidence
- **Multiple Output Formats**: HTML, Excel, JSON, and XML reporting options

### 🎯 Comprehensive Test Coverage
- **Login Authentication**: Multiple methods (Standard, SAML, SSO via KFOne)
- **Job Mapping Workflows**: Intelligent job profile mapping with AI recommendations
- **Manual Mapping**: Specialized manual mapping capabilities within workflows
- **Profile Management**: Job profile lifecycle management and publishing
- **Search & Filtering**: Advanced search with multiple filter combinations
- **Data Export**: Export functionality validation across all formats
- **HCM Sync**: Profile synchronization with external systems
- **Content Validation**: Comprehensive job mapping content verification
- **Missing Data Handling**: Validation of jobs with missing Grade/Department/Function/Subfunction data
- **Info Messages**: Verification of profile status messages and warnings

### 🌐 Multi-Environment Support
- **Dev Environment**: `https://devproductshub.kornferry.com/`
- **QA Environment**: `https://testproductshub.kornferry.com/`
- **Stage Environment**: `https://stagingproductshub.kornferry.com/`
- **Production EU**: `https://products.kornferry.eu/`
- **Production US**: `https://products.kornferry.com/`
- **KFOne Portal**: Multiple regional instances with SSO integration

### 🔐 Multiple Authentication Methods
- Standard username/password credentials
- SAML/Microsoft Azure AD authentication
- KFOne SSO (Single Sign-On) with regional support
- KFOne non-SSO authentication for legacy systems
- Dynamic login type switching via configuration

## 🚀 Setup Instructions

### Prerequisites
- **Java JDK 17** or higher (LTS recommended)
- **Maven 3.6+** for dependency management
- **Chrome, Firefox, or Edge browser** (Chrome recommended for headless stability)
- **Git** for version control
- **Windows 10/11** (for optimal session recovery features)

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd "e2e-automation"
   ```

2. **Install Maven dependencies**
   ```bash
   mvn clean install
   ```

3. **Configure test properties**
   ```properties
   # Essential Configuration in src/test/resources/config.properties
   
   # Browser Configuration
   browser=chrome
   
   # Headless Mode (prevents screen-off session failures)
   headless.mode=true    # For CI/CD and unattended execution
   # headless.mode=false # For debugging and development
   
   # Excel Reporting Toggle
   excel.reporting=true  # Enable Excel reports
   # excel.reporting=false # Disable for faster execution
   
   # Login Type Configuration
   login.type=NON_SSO    # Options: SSO, NON_SSO
   
   # Environment Selection
   Environment=QA        # Options: Dev, QA, Stage, ProdEU, ProdUS
   
   # Test Credentials - KF HUB NON SSO
   username=your.test.user@example.com
   password=YourPassword123!
   
   # SSO Login Credentials - KFONE
   SSO_Login_Username=sso.user@domain.com
   SSO_Login_Password=SSOPassword123!
   
   # NON SSO Login Credentials - KFONE
   NON_SSO_Login_Username=nonsso.user@domain.com
   NON_SSO_Login_Password=NonSSOPassword123!
   
   # SAML Account Credentials - KF HUB SSO
   AI_AUTO_SAML_Username=saml.user@domain.com
   AI_AUTO_SAML_Password=SAMLPassword123!
   ```

4. **Configure logging (Optional)**
   ```properties
   # In src/main/resources/log4j2.properties
   # Console logging (default - active)
   appenders = console
   
   # File logging (uncomment to enable)
   # appenders = file, rollingFile
   ```

## 🏃‍♂️ Test Execution

### Cross-Browser Test Execution

1. **Cross-Browser Login Tests (Chrome + Firefox + Edge)**
   ```bash
   mvn test -Dtest=CrossBrowser01_LoginPageRunner
   ```

2. **Cross-Browser Job Profile Details Tests**
   ```bash
   mvn test -Dtest=CrossBrowser04_JobProfileDetailsPopupRunner
   ```

3. **Cross-Browser Header Section Tests**
   ```bash
   mvn test -Dtest=CrossBrowser05_HeaderSectionRunner
   ```

4. **All Cross-Browser Tests**
   ```bash
   mvn test -Dtest="CrossBrowser*"
   ```

### Test Suite Execution Strategy

#### **📊 Test Suite Hierarchy**
- **Sanity Suite** → 7 tests (5-10 min) - Verify application is minimally functional
- **Smoke Suite** → 18 tests (15-25 min) - Validate major features before release
- **Regression Suite** → 55 tests (60-90 min) - Complete coverage for nightly/weekly runs

#### **1. Sanity Tests (Critical Path - Build Verification)**
```bash
mvn test -DsuiteXmlFile=JAM_SanityTestSuite.xml
```
**Purpose:** Quick verification that the build is not broken. If these fail, stop further testing.  
**When to Run:** Every build, before any other testing

#### **2. Smoke Tests (Pre-Release Validation)**
```bash
mvn test -DsuiteXmlFile=JAM_SmokeTestSuite.xml
```
**Purpose:** Validate major features work correctly before release to QA/Staging.  
**When to Run:** Before each release, after code merges

#### **3. Regression Tests (Comprehensive Coverage)**
```bash
mvn test -DsuiteXmlFile=JAM_RegressionTestSuite.xml
```
**Purpose:** Complete end-to-end testing with all edge cases and scenarios.  
**When to Run:** Nightly builds, weekly regression cycles, before production release

---

### 📊 Test Suite Coverage Details

#### **🔴 Sanity Test Suite (7 Runners)**
**Critical path tests that verify basic functionality:**
- Login functionality (KFone)
- Add More Jobs functionality
- Job Mapping page components
- Publish job from details popup
- Function/Subfunction filters
- HCM Sync Profiles Tab (PM)
- Select and Publish Loaded Profiles (JAM)

**Quick Reference:**
```bash
mvn test -DsuiteXmlFile=JAM_SanityTestSuite.xml
```

#### **🟡 Smoke Test Suite (18 Runners)**
**Includes all 7 Sanity tests + 11 additional major feature tests:**
- All Sanity tests
- Additional publish workflows
- Search functionality
- Sorting and persistence
- AI mapping features
- PM publish center
- Data validation messages
- Profile selection and sync

**Quick Reference:**
```bash
mvn test -DsuiteXmlFile=JAM_SmokeTestSuite.xml
```

#### **🟢 Regression Test Suite (55 Runners)**
**Complete coverage including all 18 Smoke tests + 37 additional tests:**
- All Smoke tests
- Extended basic operations (5 tests)
- Extended filter tests (3 tests)
- Persistence tests (2 tests)
- Profile & validation tests (4 tests)
- Mapping tests (2 tests)
- Profile Manager extended (3 tests)
- Missing data validation (6 tests)
- Profile selection PM extended (5 tests)
- Profile selection JAM extended (5 tests)
- Performance & edge cases (2 tests)

**Quick Reference:**
```bash
mvn test -DsuiteXmlFile=JAM_RegressionTestSuite.xml
```

**📋 Coverage Documentation:**  
See `TEST_SUITE_COVERAGE.md` for complete runner-to-suite mapping.

---

### Running Specific Test Runners

```bash
# Login functionality tests
mvn test -Dtest=Runner01_LoginPage

# Job Mapping component validation
mvn test -Dtest=Runner02_VerifyJobMappingPageComponents

# Filters functionality
mvn test -Dtest=Runner11_ValidateJobMappingFiltersFunctionality

# Missing data validation
mvn test -Dtest=Runner29_ValidateJobsWithMissingGRADEdataInJobMapping
```

### Running Tests by Tags

```bash
# Smoke tests (critical functionality)
mvn test -Dcucumber.filter.tags="@smoke"

# Login tests
mvn test -Dcucumber.filter.tags="@DYNAMIC_LOGIN"

# Filter functionality
mvn test -Dcucumber.filter.tags="@Validate_Job_Mapping_Filters_Functionality"

# Profile management features
mvn test -Dcucumber.filter.tags="@Client_with_PM_Access"
```

### Running All Tests
```bash
# Complete test suite execution
mvn clean test

# With specific browser
mvn test -Dbrowser=chrome

# With headless mode override
mvn test -Dheadless.mode=true

# With Excel reporting disabled
mvn test -Dexcel.reporting=false
```

## 📊 Enhanced Reporting System

### 📈 Excel Business Reports (Automatic)

**Location**: `ExcelReports/JobMappingAutomationTestResults.xlsx`

**Features**:
- **Dual Dashboard Architecture**: Separate "Automation QA Dashboard" and "Cross-Browser QA Dashboard" sheets
- **Cross-Browser Analytics**: Browser compatibility scores, reliability rankings, and compatibility metrics
- **Browser Status Columns**: Individual Chrome/Firefox/Edge status tracking in Test Results Summary
- **Executive Summary**: High-level pass/fail metrics with business context for both execution types
- **Detailed Breakdowns**: Feature-wise results with scenario-level details and browser-specific outcomes
- **Smart Daily Reset**: Automatically resets for new day, appends same-day executions
- **Historical Tracking**: Enhanced execution history with browser results and execution type detection
- **Professional Format**: Business-ready Excel suitable for stakeholder distribution

### 🌐 Cross-Browser Dashboard Features

**Sheet**: "Cross-Browser QA Dashboard"

**Specialized Metrics**:
- **Compatibility Score**: Overall browser compatibility percentage
- **Business Impact Assessment**: Risk evaluation based on browser compatibility results  
- **Browser Breakdown**: Individual Chrome/Firefox/Edge performance analytics
- **Cross-Browser Coverage**: Multi-platform testing value and reliability rankings
- **Most Reliable Browser**: Automated browser performance ranking system

**Auto-Generation**: Automatically created after each test execution via `@Listeners` annotation

**Manual Generation** (if needed):
```java
// Trigger manual Excel report generation
DailyExcelTracker.generateDailyReport();
```

**PDF Conversion** (Optional):
```bash
# Convert Excel report to PDF
java -cp "target/classes:lib/*" com.JobMapping.utils.PDFReportGenerator
```

### 📸 ExtentReports (Technical + Visual)

**Location**: `Report/AI-AutoMap-Report/`

**Features**:
- **Visual Evidence**: Embedded failure screenshots with contextual information
- **Timeline View**: Chronological test execution with duration metrics
- **Dashboard Metrics**: Pass/fail ratios, execution trends, performance insights
- **Step-by-Step Details**: Granular execution logs with BDD step mapping
- **Exception Tracking**: Complete stack traces with screenshot correlation

### 📋 TestNG Reports (Suite Analysis)

**Location**: `test-output/`

**Features**:
- **Suite-level Results**: Overall test suite performance and statistics
- **Failure Analysis**: Detailed failure categorization and root cause analysis
- **Execution Timeline**: Test method execution order and duration tracking
- **Retry Analysis**: Failed test retry attempts and success patterns

### 🥒 Cucumber Reports (BDD Analysis)

**Location**: `target/cucumber-reports/`

**Features**:
- **BDD Scenario Results**: Business-readable test outcomes
- **Step Definition Mapping**: Traceability from feature files to implementation
- **Tag-based Analysis**: Results grouped by functional areas and criticality

## 🛡️ Advanced Error Handling & Recovery

### Screenshot Capture System

**Automatic Capture**: Every test failure automatically captures visual evidence
```java
// Example of enhanced catch block (implemented across 31+ page objects)
} catch (Exception e) {
    ScreenshotHandler.handleTestFailure("method_name", e, 
        "Descriptive error message with business context");
}
```

**Manual Screenshot Capture**:
```java
// Capture screenshot without failing test (for documentation)
ScreenshotHandler.captureAndLog("method_name", "Issue detected for investigation");

// Custom screenshot with description
ScreenshotHandler.captureScreenshotWithDescription("Custom validation checkpoint");
```

**Configuration**:
```java
// Enable/disable screenshot capture
ScreenshotHandler.setScreenshotEnabled(true);

// Cleanup old screenshots (older than 7 days)
ScreenshotHandler.cleanupOldScreenshots(7);
```

### Session Recovery System

**Automatic Recovery**: Handles browser session failures transparently
```java
// Session-safe operations handled by DriverManager
WebDriver driver = DriverManager.getDriver();
// Enhanced driver includes automatic session recovery
```

**Configuration**: Automatically enabled via enhanced `DriverManager` with 13+ Chrome stability options

## 🎯 Test Scenario Coverage

### 🔐 Authentication Module (2 feature files)
- **01LoginPage.feature**: Standard credential authentication with validation
- **01KFoneLogin.feature**: KFOne SSO and non-SSO authentication flows
- SAML/Microsoft Azure AD integration testing
- Session management and logout verification

### 🗂️ Job Mapping Module (31 feature files - JobMapping/)

#### **Core Functionality**
- **02VerifyJobMappingPageComponents**: Component validation and interaction testing
- **03PublishJobProfile**: Publishing workflow validation with approval processes
- **04ValidateJobProfileDetailsPopup**: Profile details popup validation
- **05ValidateJobMappingHeaderSection**: Header section components validation
- **06PublishSelectedProfiles**: Bulk publishing functionality
- **07ValidateRecommendedProfileDetails**: AI-powered profile recommendation accuracy

#### **Search & Filtering**
- **08ValidateScreen1SearchResults**: Search functionality validation
- **11ValidateJobMappingFiltersFunctionality**: Advanced filtering capabilities
  - Grades filter validation
  - Departments filter validation
  - Functions/Subfunctions filter validation
  - Mapping status filter validation
  - Multiple filter combinations

#### **Data Management**
- **09ValidateAddMoreJobsFunctionality**: Job catalog upload and validation
- **10AddandVerifyCustomSPinJobComparisonPage**: Custom success profile creation

#### **Profile Management**
- **12ValidateHCMSyncProfilesTab_PM**: HCM sync profile management
- **13ValidateProfileLevelFunctionality**: Profile level functionality testing
- **15ValidatePublishCenter_PM**: Publish center workflows
- **20ValidateExportStatusFunctionality_PM**: Export functionality validation
- **21VerifyProfileswithNoJobCode_PM**: Profiles without job codes

#### **Sorting & Persistence**
- **14ValidateSortingFunctionality**: Multi-level sorting capabilities
  - Organization Job Name sorting (ascending/descending)
  - Matched Success Profile Grade sorting
  - Multi-level sorting combinations
- **22ValidatePersistanceOfFilters**: Filter persistence across navigation
- **23ValidatePersistanceofSorting**: Sorting persistence validation

#### **Manual Mapping Workflows**
- **17VerifyJobsWithNoBICMappings**: Jobs without BIC mappings validation
- **18ManualMappingofSPinAutoAI**: Manual mapping using "Find Match" button
- **19MapDifferentSPtoProfileInAutoAI**: Manual mapping using "Search a Different Profile" button
- **24PublishJobFromComparisonScreen**: Publishing from comparison screen
- **25PublishJobFromDetailsPopup**: Publishing from details popup

#### **Advanced Validation Scenarios**
- **16ValidatePCRestrictedTipMessage**: Profile Collections restricted tip message validation
- **26VerifyJobsMissingDataTipMessage**: Missing data tip message validation
- **27VerifyInfoMessageForMissingDataProfiles**: Info messages for profiles with missing data
- **28VerifyInfoMessageForManualMappingProfiles**: Info messages for manually mapped profiles

#### **Missing Data Validation (Forward & Reverse Flows)**
- **29ValidateJobsWithMissingGRADEdataInJobMapping**: Grade missing data validation
- **30ValidateJobsWithMissingDEPARTMENTdataInJobMapping**: Department missing data validation
- **31ValidateJobsWithMissingFUNCTIONdataInJobMapping**: Function missing data validation
- **32ValidateJobsWithMissingSUBFUNCTIONdataInJobMapping**: Subfunction missing data validation

## ⚙️ Configuration Management

### Main Configuration (`config.properties`)
```properties
# Browser Configuration
browser=chrome                    # Options: chrome, firefox, edge

# Headless Mode Configuration
headless.mode=true               # Prevents screen-off session failures

# Excel Reporting Configuration
excel.reporting=true             # Set to false to disable Excel reporting

# Login Type Configuration
login.type=NON_SSO              # Options: SSO, NON_SSO

# Client Verification Settings
target.pams.id=5120             # PAMS ID to verify (leave empty for all)

# Environment Selection  
Environment=QA                   # Options: Dev, QA, Stage, ProdEU, ProdUS

# Performance Settings
screenshot.enabled=true
screenshot.performance.mode=true
session.recovery.enabled=true
extended.timeouts.enabled=true
```

### Logging Configuration (`log4j2.properties`)
```properties
# Console Logging (Default - Active)
appenders = console
rootLogger.appenderRefs = stdout

# File Logging (Uncomment to enable)  
# appenders = file, rollingFile
# rootLogger.appenderRefs = file, rollingFile

# Logging Levels
rootLogger.level = debug          # Options: trace, debug, info, warn, error
```

### ExtentReports Configuration (`extent-config.xml`)
- Professional report themes and styling
- Timeline view configuration for execution tracking
- Screenshot integration settings
- Custom JavaScript and CSS for enhanced visualization

## 🏗️ Architecture Components

### Enhanced Page Objects (31 Classes)
All page objects now include:
- **Screenshot functionality** for failure documentation
- **Session recovery support** for reliability  
- **Standardized error handling** with business-friendly messages
- **ExtentReports integration** for visual documentation
- **Clean, professional code** with consistent package naming

**Key Page Objects**:
- `PO01_LoginPage.java` - Multi-method authentication workflows
- `PO01_KFoneLogin.java` - KFOne authentication (SSO and non-SSO)
- `PO02_VerifyJobMappingPageComponents.java` - Core job mapping interface validation
- `PO03_PublishJobProfile.java` - Publishing functionality with approval workflows
- `PO11_ValidateJobMappingFiltersFunctionality.java` - Advanced search and filtering
- `PO20_ValidateExportStatusFunctionality_PM.java` - Export operations across formats
- `PO29-PO32_*` - Missing data validation page objects

### Enhanced Utilities & Helpers

**Core Utilities**:
- `ScreenshotHandler.java` - Advanced screenshot management with ExtentReports integration
- `DriverManager.java` - Enhanced WebDriver management with session recovery
- `CrossBrowserDriverManager.java` - Multi-browser WebDriver management for cross-browser testing
- `DailyExcelTracker.java` - Dual-dashboard Excel reporting with cross-browser analytics
- `ExcelReportListener.java` - Unified Excel report generation for both Normal and Cross-Browser execution
- `PDFReportGenerator.java` - Excel to PDF conversion utilities

**Supporting Utilities**:
- `Utilities.java` - Common UI operations with enhanced wait strategies
- `SmartWaits.java` - Intelligent wait strategies
- `ExcelStyleHelper.java` - Excel formatting and styling
- `EnhancedLogger.java` - Advanced logging capabilities
- `RetryAnalyzer.java` - Intelligent failed test retry mechanism
- `CommonVariable.java` - Configuration management with automatic property loading
- `DynamicTagResolver.java` - Dynamic tag resolution for login type switching
- `VariableManager.java` - Variable management across test execution

## 🐛 Troubleshooting

### Common Issues & Solutions

1. **Browser Session Failures (Screen-off scenarios)**
   ```
   Error: "invalid session id" or "Browser appears to be closed"
   Solution: Enable headless mode (headless.mode=true) in config.properties
   Check: session.recovery.enabled=true in config.properties
   ```

2. **Screenshot Capture Failures**
   ```
   Error: Screenshots not being captured on failures
   Solution: Verify screenshot.enabled=true in config.properties
   Check: ScreenshotHandler import in page objects
   ```

3. **Excel Report Generation Issues**  
   ```
   Error: Excel reports not generated or incorrect data
   Solution: Ensure @Listeners({ExcelReportListener.class}) on test runners
   Check: excel.reporting=true in config.properties
   Check: ExcelReports directory permissions and disk space
   ```

4. **Package/Import Errors**
   ```
   Error: Cannot resolve symbol 'AIautoMap'
   Solution: All packages have been renamed to 'JobMapping'
   Update: Run mvn clean install to refresh dependencies
   ```

5. **Environment Access Issues**
   ```
   Error: Unable to connect to test environment
   Solution: Verify VPN connection for internal environments
   Check: User permissions and credentials in config.properties
   ```

### 📋 Logging & Debugging

**Log Locations**:
- **Application Logs**: `logs/automation_current.log` (latest execution)
- **Historical Logs**: `logs/automation_logs_YYYY-MM-DD.log` (date-specific)
- **Console Output**: Real-time execution feedback during test runs

**Log Configuration**:
```properties
# Console logging (immediate feedback)
appenders = console

# File logging (permanent record) - uncomment to enable
# appenders = file, rollingFile  
```

**Debug Mode**:
```bash
# Run with debug logging
mvn test -Dlog.level=debug

# Run with screenshot debugging
mvn test -Dscreenshot.enabled=true -Dscreenshot.debug=true
```

## 📈 Best Practices

### 1. **Test Data Management**
- Use external CSV/Excel files for data-driven testing
- Maintain separate test data sets per environment
- Implement data cleanup strategies for test isolation

### 2. **Page Object Model Excellence**
- Follow consistent POM patterns across all page objects
- Implement screenshot functionality for all failure scenarios
- Use meaningful method names and comprehensive documentation

### 3. **Wait Strategies**
- Implement explicit waits with business-relevant timeout values
- Use ExpectedConditions for reliable element interactions
- Leverage SmartWaits utility for intelligent waiting

### 4. **Visual Documentation**
- Capture screenshots for all failure scenarios automatically
- Include contextual information in screenshot file names
- Integrate visual evidence with ExtentReports for comprehensive documentation

### 5. **Environment Management**
- Maintain environment-specific configurations
- Use headless mode for CI/CD and unattended execution
- Implement proper session management for long-running tests

### 6. **Reporting Excellence**
- Generate both technical (ExtentReports) and business (Excel) reports
- Include visual evidence (screenshots) in all failure reports
- Maintain historical tracking for trend analysis

### 7. **Code Quality**
- Maintain professional, clean codebase
- Implement consistent error handling patterns
- Use meaningful variable names and comprehensive documentation
- Follow package naming conventions (com.JobMapping.*)

## 🤝 Contributing

### Development Guidelines
1. **Code Standards**: Follow existing code structure and naming conventions
2. **Package Naming**: Use `com.JobMapping.*` package structure
3. **Documentation**: Add comprehensive javadoc for new methods and classes
4. **Testing**: Implement both positive and negative test scenarios
5. **Error Handling**: Include ScreenshotHandler for all new page object methods
6. **Reporting**: Ensure proper ExtentReports integration for new features

### Submission Process
1. **Feature Development**: Create feature branches for new functionality
2. **Testing**: Run full regression suite before submitting changes
3. **Documentation**: Update README.md and feature files for new test scenarios
4. **Review**: Ensure proper error handling and screenshot integration

## 📞 Support & Resources

### Technical Support Resources
- **Execution Logs**: `logs/` directory for detailed execution information
- **ExtentReports**: Visual test results with embedded screenshots and timelines
- **TestNG Reports**: Suite-level analysis and failure categorization  
- **Excel Reports**: Business-friendly insights in `ExcelReports/JobMappingAutomationTestResults.xlsx`

### Quick Reference Commands
```bash
# Run sanity tests (critical path - fastest verification)
mvn test -DsuiteXmlFile=JAM_SanityTestSuite.xml

# Run smoke tests with visual reporting
mvn test -DsuiteXmlFile=JAM_SmokeTestSuite.xml

# Run full regression suite
mvn test -DsuiteXmlFile=JAM_RegressionTestSuite.xml

# Debug mode with detailed logging
mvn test -Dlog.level=debug -Dscreenshot.enabled=true

# Headless execution for CI/CD
mvn test -Dheadless.mode=true

# Generate manual Excel report
mvn exec:java -Dexec.mainClass="com.JobMapping.utils.DailyExcelTracker"

# Convert Excel to PDF
java -cp "target/classes:lib/*" com.JobMapping.utils.PDFReportGenerator
```

---

## 📊 Project Statistics

- **Test Feature Files**: 55 comprehensive feature files covering complete Job Mapping workflows
- **Page Objects**: 31 enhanced classes with screenshot functionality and clean code
- **Test Runners**: 55 specialized test runners (Runner01-Runner46 + KFone Login)
- **Test Suites**: 3 hierarchical test suites (Sanity: 7 tests, Smoke: 18 tests, Regression: 55 tests)
- **Cross-Browser Coverage**: 26 cross-browser runners supporting Chrome, Firefox, and Edge
- **Step Definitions**: 31 step definition files for complete BDD coverage
- **Supported Environments**: 5 environments (Dev, QA, Stage, ProdEU, ProdUS) + KFOne integration
- **Authentication Methods**: 4 different authentication workflows (Standard, SAML, SSO, Non-SSO)
- **Reporting Formats**: Dual Excel dashboards (Normal + Cross-Browser), HTML, JSON, XML with visual evidence
- **Dashboard Analytics**: 2 specialized dashboards with browser compatibility metrics
- **Code Quality**: Professional codebase with consistent package naming and optimized logging
- **Test Suite Coverage**: Complete documentation in TEST_SUITE_COVERAGE.md

---

**Last Updated**: November 2025  
**Framework Version**: 2.2.0-SNAPSHOT  
**Package Namespace**: com.JobMapping  
**Maintained By**: QA Automation Team  

### 🚀 Release History

#### **v2.2.0 (November 2025)** - Test Suite Organization & Coverage Enhancement
- 📊 **Test Suite Restructuring**: Complete reorganization into 3 hierarchical suites (Sanity/Smoke/Regression)
- 🎯 **Smart Test Categorization**: 7 Sanity tests, 18 Smoke tests, 55 Regression tests for optimal execution
- 📋 **Coverage Documentation**: New TEST_SUITE_COVERAGE.md for complete suite mapping
- ⚡ **Execution Strategy**: Clear guidelines for when to run each test suite (build/release/nightly)
- 🔤 **Suite Naming Convention**: Standardized JAM_ prefix for all test suite XML files
- 📈 **Improved Efficiency**: Strategic test selection reduces feedback time while maintaining coverage
- 📚 **Enhanced Documentation**: Comprehensive README updates with test suite hierarchy and coverage details

#### **v2.1.0 (October 2025)** - Framework Modernization & Code Quality Release
- 📁 **Framework Restructuring**: Complete migration from `AIautoMap` to `JobMapping` package naming
- 🔧 **Scenario Breakdown**: Large multi-step scenarios refactored into 100+ focused, maintainable tests
- 🏷️ **Tag Cleanup**: Simplified tagging system with one primary tag per scenario
- 📝 **Import Optimization**: Consistent import statements across all 59 test runners and 31 page objects
- 🗂️ **Package Consistency**: Updated all package declarations, imports, and references
- 📚 **Documentation Updates**: Comprehensive README and inline documentation improvements
- 🎯 **Enhanced Maintainability**: Improved code organization for better debugging and reporting
- 🌐 **Cross-Browser Support**: 26 cross-browser runners with complete Chrome/Firefox/Edge coverage
- 📊 **Dual Dashboard Architecture**: Separate dashboards for Normal and Cross-Browser execution analytics
- 🎯 **Browser Compatibility Analytics**: Compatibility scoring and business impact assessment
- 📈 **Enhanced Excel Reporting**: Browser status columns and execution type detection
- ⚙️ **Configuration Improvements**: Unified configuration management with better defaults

#### **v2.0.0 (September 2025)** - Advanced Analytics & Cross-Browser Release
- 🌐 **Cross-Browser Testing Suite**: Multi-browser parallel execution framework
- 📊 **Advanced Analytics**: Specialized cross-browser compatibility metrics
- 🎨 **Professional Color Coding**: Complete visual styling system for dashboards
- 🔄 **Thread-Safe Execution**: Reliable parallel cross-browser testing
- ✨ **Advanced Screenshot System**: Comprehensive failure capture across all page objects
- 🔄 **Session Recovery**: Screen-off immunity with automatic session restoration  
- ⚙️ **Robust Configuration**: Headless mode management and enhanced property loading
- 🛡️ **Error Handling Excellence**: Standardized exception management with visual evidence

#### **v1.0.0 (January 2025)** - Foundation Release  
- 📈 Complete Excel reporting integration across all test runners
- 🧹 Professional codebase cleanup and maintainability improvements
- 📊 Business-friendly reporting with automatic generation capabilities
- 📝 Enhanced logging framework and improved documentation

### 🎯 Roadmap
- **Enhanced Analytics**: Machine learning-based test execution trend analysis
- **Performance Benchmarking**: Response time analysis and performance metrics
- **Cross-Browser CI/CD**: Enhanced pipeline support with parallel execution
- **Mobile Testing**: Extension to mobile automation capabilities
- **API Integration**: Comprehensive API testing framework integration
- **Visual Regression**: Screenshot comparison and visual testing capabilities
- **AI-Powered Insights**: Intelligent failure analysis and predictive analytics
