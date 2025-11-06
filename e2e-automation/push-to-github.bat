@echo off
REM ========================================
REM Push Job Mapping E2E Automation to GitHub
REM Repository: https://github.com/Naveen-moback-kf/JAM-e2e-automation.git
REM ========================================

echo.
echo ╔════════════════════════════════════════════════════════════╗
echo ║     Push Job Mapping E2E Project to GitHub                ║
echo ║     Repository: JAM-e2e-automation                         ║
echo ╚════════════════════════════════════════════════════════════╝
echo.

cd /d "%~dp0"
cd ..

REM Check if git is installed
git --version >nul 2>&1
if errorlevel 1 (
    echo ❌ ERROR: Git is not installed!
    echo Please install Git from https://git-scm.com/download/win
    pause
    exit /b 1
)

echo ✅ Git is installed
echo.

REM Check if already a git repository
if exist ".git" (
    echo ℹ️  Git repository already initialized
) else (
    echo 📦 Initializing Git repository...
    git init
    echo ✅ Git repository initialized
)

echo.
echo 📋 Current Git status:
git status --short
echo.

REM Check if remote already exists
git remote get-url origin >nul 2>&1
if errorlevel 1 (
    echo 🔗 Adding remote repository...
    git remote add origin https://github.com/Naveen-moback-kf/JAM-e2e-automation.git
    echo ✅ Remote 'origin' added
) else (
    echo ℹ️  Remote 'origin' already exists:
    git remote get-url origin
    echo.
    set /p update_remote="Update remote URL? (Y/N): "
    if /i "%update_remote%"=="Y" (
        git remote set-url origin https://github.com/Naveen-moback-kf/JAM-e2e-automation.git
        echo ✅ Remote URL updated
    )
)

echo.
echo 📂 Files to be committed:
echo    • Source code (src/)
echo    • Test resources (features, properties, CSV)
echo    • Configuration files (pom.xml, TestNG suites)
echo    • GitHub Actions workflow (.github/)
echo    • Documentation (README.md, guides)
echo    • Excel reports templates
echo.

set /p confirm="Continue with commit and push? (Y/N): "
if /i not "%confirm%"=="Y" (
    echo.
    echo ❌ Operation cancelled.
    pause
    exit /b 0
)

echo.
echo 📦 Adding all files to Git...
git add .

echo.
echo 💾 Creating initial commit...
git commit -m "Initial commit: Job Mapping E2E Automation Framework

- Complete BDD framework with Cucumber + TestNG
- 46+ Feature files covering Job Mapping functionality
- 70+ Page Objects with Page Object Model pattern
- 134+ Test Runners (Normal + Cross-Browser)
- GitHub Actions CI/CD workflow configured
- Dual Excel reporting system (Normal + Cross-Browser)
- ExtentReports HTML dashboard
- WebDriverManager integration
- Multi-environment support (Dev, QA, Stage, Prod)
- Comprehensive documentation and guides
- Performance optimizations applied
- Screenshot capture on failures
- Log4j2 logging configuration

Framework Version: 2.1.0-SNAPSHOT
Package: com.JobMapping
Java Version: 17
Maven: 3.x"

if errorlevel 1 (
    echo.
    echo ⚠️  Commit failed or nothing to commit
    echo.
    git status
    pause
    exit /b 1
)

echo ✅ Commit created successfully
echo.

echo 📤 Pushing to GitHub...
echo    Repository: https://github.com/Naveen-moback-kf/JAM-e2e-automation.git
echo    Branch: main
echo.

git branch -M main
git push -u origin main

if errorlevel 1 (
    echo.
    echo ❌ ERROR: Push failed!
    echo.
    echo Possible reasons:
    echo 1. Authentication required - Please enter your GitHub credentials
    echo 2. Network connection issue
    echo 3. Repository permissions issue
    echo.
    echo 💡 TIP: If authentication fails, you may need to:
    echo    - Use a Personal Access Token instead of password
    echo    - Configure Git credentials: git config --global credential.helper wincred
    echo.
    pause
    exit /b 1
)

echo.
echo ╔════════════════════════════════════════════════════════════╗
echo ║                  ✅ SUCCESS!                               ║
echo ╚════════════════════════════════════════════════════════════╝
echo.
echo Your Job Mapping E2E Automation project has been pushed to:
echo 🔗 https://github.com/Naveen-moback-kf/JAM-e2e-automation
echo.
echo 🎯 Next Steps:
echo    1. Visit the repository URL above to verify
echo    2. Check GitHub Actions tab for CI/CD workflows
echo    3. Review README.md in the repository
echo    4. Invite team members as collaborators
echo.
echo 🚀 GitHub Actions Workflow:
echo    • Go to: Actions tab in your repository
echo    • Click: "Job Mapping E2E Tests"
echo    • Click: "Run workflow" to execute tests
echo.

pause

