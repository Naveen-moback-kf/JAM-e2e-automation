# 🚀 Getting Started - Job Mapping E2E Automation Framework

**Quick Start Guide for Team Members**

---

## 📋 Table of Contents

- [Prerequisites](#prerequisites)
- [Initial Setup](#initial-setup)
- [Running Tests](#running-tests)
- [Viewing Reports](#viewing-reports)
- [Configuration](#configuration)
- [Troubleshooting](#troubleshooting)
- [Quick Reference](#quick-reference)

---

## ✅ Prerequisites

Before you begin, ensure you have the following installed:

### Required Software

| Software | Version | Download Link |
|----------|---------|---------------|
| **Java JDK** | 17 or higher | [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) |
| **Maven** | 3.8+ | [Maven Download](https://maven.apache.org/download.cgi) |
| **Git** | Latest | [Git Download](https://git-scm.com/downloads) |
| **Chrome** | Latest | Auto-updates |
| **IDE** | Eclipse/IntelliJ | Your choice |

### Verify Installation

```bash
# Check Java version
java -version
# Expected: java version "17.0.x"

# Check Maven version
mvn -version
# Expected: Apache Maven 3.8.x or higher

# Check Git
git --version
# Expected: git version 2.x
```

---

## 🔧 Initial Setup

### 1. Clone the Repository

```bash
git clone https://github.com/Naveen-moback-kf/JAM-e2e-automation.git
cd JAM-e2e-automation
```

### 2. Configure Environment

Edit the configuration file:
```
e2e-automation/src/test/resources/config.properties
```

**Key Settings:**
```properties
# Browser Configuration
browser=chrome
headless.mode=false

# Reporting
excel.reporting=true
allure.reporting=true

# Keep system awake during long test runs
keep.system.awake=true
```

### 3. Set Up Environment Files

Create environment-specific configuration (optional):
```
e2e-automation/src/test/resources/environments/qa.properties
```

**Example:**
```properties
environment=QA
login.type=NON_SSO
pams.id=YOUR_PAMS_ID
sso.username=your.email@example.com
sso.password=YourPassword
nonsso.username=your_username
nonsso.password=YourPassword
```

### 4. Install Dependencies

```bash
cd e2e-automation
mvn clean install -DskipTests
```

---

## ▶️ Running Tests

### Quick Start - Run All Tests

```bash
# Run complete regression suite (all tests)
mvn clean test
```

### Test Suites

#### 1. **Smoke Tests** (15-20 minutes)
Quick validation of critical functionality:
```bash
mvn clean test -DsuiteXmlFile=src/test/resources/testng/JAM_SmokeTestSuite.xml
```

#### 2. **Sanity Tests** (20-25 minutes)
Core functionality validation:
```bash
mvn clean test -DsuiteXmlFile=src/test/resources/testng/JAM_SanityTestSuite.xml
```

#### 3. **Regression Tests** (60-90 minutes)
Complete test coverage:
```bash
mvn clean test -DsuiteXmlFile=src/test/resources/testng/JAM_RegressionTestSuite.xml
```

### Running Individual Test Runners

```bash
# Run specific runner by class name
mvn clean test -Dtest=Runner01_KFoneLogin

# Run multiple specific runners
mvn clean test -Dtest=Runner01_KFoneLogin,Runner02_AddMoreJobsFunctionality
```

### Running Tests in IDE (Eclipse/IntelliJ)

1. Right-click on test runner file (e.g., `Runner01_KFoneLogin.java`)
2. Select **Run As** → **TestNG Test**
3. View results in IDE's TestNG console

---

## 🌍 Environment Configuration

### Run Tests Against Different Environments

```bash
# QA Environment (default)
mvn clean test -Denv=qa

# Stage Environment
mvn clean test -Denv=stage

# Production EU
mvn clean test -Denv=prod-eu

# Production US
mvn clean test -Denv=prod-us
```

### Login Type Selection

```bash
# Run with SSO Login
mvn clean test -Dlogin.type=SSO

# Run with NON-SSO Login (default)
mvn clean test -Dlogin.type=NON_SSO
```

### Custom Browser Configuration

```bash
# Run in Chrome (default)
mvn clean test -Dbrowser=chrome

# Run in Firefox
mvn clean test -Dbrowser=firefox

# Run in Headless Mode
mvn clean test -Dheadless.mode=true

# Run with visible browser
mvn clean test -Dheadless.mode=false
```

### Parallel Execution

```bash
# Run with 5 threads (default)
mvn clean test

# Run with 10 threads (faster on powerful machines)
mvn clean test -Dthread.count=10

# Run with 2 threads (local development)
mvn clean test -Dthread.count=2
```

---

## 📊 Viewing Reports

### 1. Excel Report

**Location:** `e2e-automation/ExcelReports/`

**File:** `JobMappingAutomationTestResults.xlsx`

**How to View:**
- Navigate to the `ExcelReports` folder
- Open the latest `.xlsx` file
- Review detailed test execution results

**Sections:**
- Test execution summary
- Pass/Fail status per scenario
- Screenshots on failures
- Execution timestamps

### 2. Allure Report

**Generate and View:**
```bash
# Generate Allure report
mvn allure:serve

# Or manually generate
mvn allure:report
# Then open: target/site/allure-maven-plugin/index.html
```

**Features:**
- 📊 Visual dashboards
- 📈 Trend analysis
- 🖼️ Screenshot attachments
- ⏱️ Execution timeline
- 📋 Test case details

### 3. Cucumber HTML Report

**Location:** `e2e-automation/target/cucumber-reports/`

**File:** `cucumber-html-report.html`

**How to View:**
- Open the HTML file in any browser
- Review feature-wise execution results

### 4. TestNG Report

**Location:** `e2e-automation/test-output/`

**File:** `index.html`

**How to View:**
- Open `test-output/index.html` in browser
- Review suite execution details

---

## ⚙️ Configuration Options

### All Available System Properties

```bash
mvn clean test \
  -Denv=qa \
  -Dlogin.type=NON_SSO \
  -Dbrowser=chrome \
  -Dheadless.mode=false \
  -Dthread.count=5 \
  -Dpams.id=YOUR_PAMS_ID \
  -Dsso.username=user@example.com \
  -Dsso.password=password
```

### Configuration Priority

The framework uses the following priority order:

1. **System Properties** (highest priority)
   - `-Denv=qa` from command line
2. **Environment-Specific Config**
   - `environments/qa.properties`
3. **Common Config** (lowest priority)
   - `config.properties`

---

## 🛠️ Troubleshooting

### Common Issues and Solutions

#### 1. **Tests Not Starting**

**Problem:** Maven build fails or tests don't run

**Solution:**
```bash
# Clean and rebuild
mvn clean install -DskipTests

# Clear Maven cache
mvn dependency:purge-local-repository

# Update dependencies
mvn clean install -U
```

#### 2. **Browser Not Launching**

**Problem:** ChromeDriver/WebDriver errors

**Solution:**
- Ensure Chrome browser is updated to latest version
- Check `webdrivermanager` automatically downloads correct driver
- Try running in headless mode: `-Dheadless.mode=true`

#### 3. **Element Not Found Errors**

**Problem:** `NoSuchElementException` or timeouts

**Solution:**
- Increase implicit wait in code if needed
- Check if application is slow to load
- Verify correct environment is selected
- Check network connectivity

#### 4. **Parallel Execution Failures**

**Problem:** Tests fail when running in parallel

**Solution:**
```bash
# Reduce thread count
mvn clean test -Dthread.count=2

# Run specific suite instead of all
mvn clean test -DsuiteXmlFile=src/test/resources/testng/JAM_SmokeTestSuite.xml
```

#### 5. **Port Already in Use**

**Problem:** WebDriver port conflicts

**Solution:**
- Close all browser instances
- Restart your machine
- Kill any remaining ChromeDriver processes

**Windows:**
```bash
taskkill /F /IM chromedriver.exe
taskkill /F /IM chrome.exe
```

**Mac/Linux:**
```bash
killall chromedriver
killall chrome
```

#### 6. **Memory Issues**

**Problem:** `OutOfMemoryError` during test execution

**Solution:**
```bash
# Increase Maven memory
set MAVEN_OPTS=-Xmx2048m -XX:MaxPermSize=512m

# Or in pom.xml surefire plugin configuration
```

#### 7. **Screenshots Not Attached**

**Problem:** Screenshots not showing in reports

**Solution:**
- Check `AllureReports` folder exists
- Verify `allure.reporting=true` in config.properties
- Ensure write permissions on report directories

---

## 📚 Quick Reference

### Essential Commands

```bash
# Quick smoke test run
mvn clean test -DsuiteXmlFile=src/test/resources/testng/JAM_SmokeTestSuite.xml -Dheadless.mode=true

# Debug mode (visible browser, single thread)
mvn clean test -Dheadless.mode=false -Dthread.count=1

# Full regression with custom environment
mvn clean test -Denv=stage -Dlogin.type=SSO

# View Allure report
mvn allure:serve

# Clean everything and rebuild
mvn clean install -DskipTests
```

### Project Structure

```
e2e-automation/
├── src/main/java/
│   ├── pageobjects/JobMapping/      # Page Object classes
│   ├── utils/                        # Utility classes
│   ├── webdriverManager/             # WebDriver management
│   └── manager/                      # Page Object Manager
├── src/test/java/
│   ├── stepdefinitions/             # Cucumber step definitions
│   ├── testrunners/                 # TestNG runners
│   └── hooks/                       # Cucumber hooks
├── src/test/resources/
│   ├── features/                    # Cucumber feature files
│   ├── testng/                      # TestNG suite files
│   ├── environments/                # Environment configs
│   └── config.properties            # Main configuration
├── AllureReports/                   # Allure reports
├── ExcelReports/                    # Excel reports
└── test-output/                     # TestNG reports
```

### Key Files to Know

| File | Purpose |
|------|---------|
| `config.properties` | Main configuration file |
| `environments/*.properties` | Environment-specific configs |
| `PageObjectManager.java` | Centralized Page Object access |
| `DriverManager.java` | WebDriver lifecycle management |
| `BasePageObject.java` | Common Page Object utilities |

---

## 🤝 Getting Help

### Team Resources

- **Framework Documentation:** See `FRAMEWORK_OPTIMIZATION_RECOMMENDATIONS.md`
- **Git Repository:** [JAM E2E Automation](https://github.com/Naveen-moback-kf/JAM-e2e-automation)
- **Ask the Team:** Reach out on your team's communication channel

### Useful Links

- [Selenium Documentation](https://www.selenium.dev/documentation/)
- [Cucumber Documentation](https://cucumber.io/docs/cucumber/)
- [TestNG Documentation](https://testng.org/doc/documentation-main.html)
- [Allure Report](https://docs.qameta.io/allure/)

---

## 🎯 Next Steps

Now that you're set up:

1. ✅ Run a smoke test to verify your setup
2. ✅ Explore the feature files to understand test scenarios
3. ✅ Review Page Object classes to understand the framework
4. ✅ Try running tests against different environments
5. ✅ Generate and review Allure reports

---

## 📝 Best Practices

### Before Committing Code

```bash
# Always run tests locally first
mvn clean test -DsuiteXmlFile=src/test/resources/testng/JAM_SmokeTestSuite.xml

# Check for compilation errors
mvn clean compile

# Verify no linter errors in your IDE
```

### Daily Development

- Pull latest changes before starting work
- Run smoke tests after making changes
- Use meaningful commit messages
- Keep feature files updated with code changes

### When Tests Fail

1. Check the error message in console
2. Review screenshot in AllureReports folder
3. Check if application is accessible
4. Verify environment configuration
5. Run the same test in isolation to confirm

---

## 🎉 You're Ready!

Your framework is now set up and ready to use. Start with a smoke test run to validate everything works:

```bash
mvn clean test -DsuiteXmlFile=src/test/resources/testng/JAM_SmokeTestSuite.xml
```

**Happy Testing! 🚀**

---

*Last Updated: January 2026*  
*Framework Version: 2.1.0*

