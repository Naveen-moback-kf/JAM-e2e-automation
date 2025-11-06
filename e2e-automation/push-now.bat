@echo off
REM ========================================
REM Quick Push to GitHub (Files Already Added)
REM ========================================

echo.
echo ╔════════════════════════════════════════════════════════════╗
echo ║     Quick Push to GitHub                                   ║
echo ╚════════════════════════════════════════════════════════════╝
echo.

cd /d "%~dp0"
cd ..

echo 📋 Current status:
git status
echo.

REM Check if there are changes to commit
git diff-index --quiet HEAD --
if errorlevel 1 (
    echo 📦 New changes detected, creating commit...
    git add .
    git commit -m "Initial commit: Job Mapping E2E Automation Framework"
    if errorlevel 1 (
        echo ⚠️  Commit failed
        pause
        exit /b 1
    )
) else (
    echo ℹ️  No new changes, using existing commit
)

echo.
echo 📤 Pushing to GitHub...
git branch -M main
git push -u origin main

if errorlevel 1 (
    echo.
    echo ❌ Push failed!
    echo.
    echo 💡 Authentication Tips:
    echo    Username: Naveen-moback-kf
    echo    Password: Use your GitHub Personal Access Token
    echo.
    echo Get token at: https://github.com/settings/tokens
    echo.
    pause
    exit /b 1
)

echo.
echo ╔════════════════════════════════════════════════════════════╗
echo ║                  ✅ SUCCESS!                               ║
echo ╚════════════════════════════════════════════════════════════╝
echo.
echo 🎉 Your project is now on GitHub!
echo 🔗 https://github.com/Naveen-moback-kf/JAM-e2e-automation
echo.
echo Next: Go to Actions tab to run your tests!
echo.
pause

