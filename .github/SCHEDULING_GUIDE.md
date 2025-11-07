# 📅 GitHub Actions Test Scheduling Guide

## Overview
Your Job Mapping E2E Tests workflow now supports both **manual execution** and **automatic scheduled runs**. This guide explains how to configure and manage scheduled test runs.

---

## 🎯 Quick Start

### Current Status
❌ **Scheduling is CURRENTLY DISABLED** - Manual execution only during testing phase.

All scheduling infrastructure is in place and ready to enable when needed.

### To Enable Scheduling
Uncomment the `schedule:` block in `.github/workflows/jobmapping_e2e_tests.yml`:

**Currently (Disabled):**
```yaml
# schedule:
  # Daily at 2:00 AM UTC (runs smoke tests by default)
  # - cron: '0 2 * * *'
```

**To Enable:**
```yaml
schedule:
  # Daily at 2:00 AM UTC (runs smoke tests by default)
  - cron: '0 2 * * *'
```

---

## ⚙️ Configuration

### 1. Schedule Times (in the `schedule:` section)

**Current Active Schedule:**
- Daily at 2:00 AM UTC (runs smoke tests by default)

**Available Schedule Templates:**

| Schedule | Cron Expression | Description |
|----------|----------------|-------------|
| Daily 2 AM UTC | `'0 2 * * *'` | ✅ Currently Active |
| Daily 6 AM UTC | `'0 6 * * *'` | Morning run |
| Daily 2 PM UTC | `'0 14 * * *'` | Afternoon run |
| Daily 10 PM UTC | `'0 22 * * *'` | Evening run |
| Twice daily | `'0 6,18 * * *'` | 6 AM & 6 PM UTC |
| Three times daily | `'0 6,14,22 * * *'` | 6 AM, 2 PM, 10 PM UTC |
| Every 6 hours | `'0 */6 * * *'` | Continuous monitoring |
| Every 12 hours | `'0 */12 * * *'` | Twice daily |
| Every Monday 9 AM | `'0 9 * * 1'` | Weekly Monday |
| Every Friday 5 PM | `'0 17 * * 5'` | Weekly Friday |
| Weekdays 7 AM | `'0 7 * * 1-5'` | Mon-Fri only |
| Weekends 10 AM | `'0 10 * * 6,0'` | Sat-Sun only |
| First of month | `'0 0 1 * *'` | Monthly run |
| Sunday midnight | `'0 0 * * 0'` | Weekly full regression |

### 2. Scheduled Run Defaults (in the `env:` section)

Configure what runs automatically in the environment variables section:

```yaml
env:
  SCHEDULED_TEST_SUITE: 'smoke_tests.xml'      # Change to: regression_tests.xml, all_tests.xml
  SCHEDULED_BROWSER: 'chrome'                   # Change to: firefox, edge
  SCHEDULED_ENVIRONMENT: 'qa'                   # Change to: dev, stage, prodeu, produs
  SCHEDULED_HEADLESS: 'true'                    # Change to: false (for debugging)
  SCHEDULED_LOGIN_TYPE: 'NON_SSO'               # Change to: SSO
  SCHEDULED_PAMS_ID: '23139'                    # Change to your target PAMS ID
```

---

## 📝 How to Use

### Enable a Schedule

1. Open `.github/workflows/jobmapping_e2e_tests.yml`
2. Find the `schedule:` section (around line 171)
3. Uncomment the schedule you want by removing the `#`:

**Before:**
```yaml
# Every Friday at 5:00 PM UTC (weekly regression)
# - cron: '0 17 * * 5'
```

**After:**
```yaml
# Every Friday at 5:00 PM UTC (weekly regression)
- cron: '0 17 * * 5'
```

### Multiple Schedules

You can enable multiple schedules simultaneously:

```yaml
schedule:
  # Smoke tests daily at 2 AM
  - cron: '0 2 * * *'
  
  # Regression tests on Friday evening
  - cron: '0 17 * * 5'
  
  # Full tests on Sunday midnight
  - cron: '0 0 * * 0'
```

**Note:** Each schedule will run with the same configuration specified in `SCHEDULED_*` environment variables. To run different test suites at different times, you'd need separate workflow files.

### Custom Schedule

Use [Crontab Guru](https://crontab.guru/) to create custom cron expressions:

**Example:** Run tests at 3:30 PM UTC, Monday through Wednesday:
```yaml
- cron: '30 15 * * 1-3'
```

---

## 🌍 Time Zone Conversion

**All times are in UTC.** Convert your local time to UTC:

| Your Location | UTC Offset | Example: 9 AM Local → UTC |
|---------------|------------|---------------------------|
| PST (US West) | UTC-8 | 5:00 PM UTC |
| EST (US East) | UTC-5 | 2:00 PM UTC |
| GMT (UK) | UTC+0 | 9:00 AM UTC |
| CET (Europe) | UTC+1 | 8:00 AM UTC |
| IST (India) | UTC+5:30 | 3:30 AM UTC |

**Quick Tip:** If you want tests to run at 9 AM India time:
- 9:00 AM IST = 3:30 AM UTC
- Cron: `'30 3 * * *'`

---

## 🔍 Common Scenarios

### Scenario 1: Daily Smoke Tests
**Goal:** Run smoke tests every day at 2 AM UTC

**Configuration:**
```yaml
schedule:
  - cron: '0 2 * * *'

env:
  SCHEDULED_TEST_SUITE: 'smoke_tests.xml'
```

### Scenario 2: Weekend Regression Tests
**Goal:** Run full regression tests every Saturday at midnight

**Configuration:**
```yaml
schedule:
  - cron: '0 0 * * 6'

env:
  SCHEDULED_TEST_SUITE: 'regression_tests.xml'
```

### Scenario 3: Business Hours Monitoring
**Goal:** Run tests every 4 hours during weekdays

**Configuration:**
```yaml
schedule:
  - cron: '0 */4 * * 1-5'

env:
  SCHEDULED_TEST_SUITE: 'smoke_tests.xml'
```

### Scenario 4: Multiple Environments
**Note:** For different environments at different times, create separate workflow files:
- `jobmapping_e2e_tests_qa.yml` (QA environment)
- `jobmapping_e2e_tests_prod.yml` (Prod environment)

---

## 📊 Monitoring Scheduled Runs

### View Scheduled Runs
1. Go to your GitHub repository
2. Click **Actions** tab
3. Select **🧪 Job Mapping E2E Tests** workflow
4. Look for runs with "⏰ Scheduled" label

### Notifications
Configure GitHub Actions notifications:
1. Go to **Settings** → **Notifications** → **GitHub Actions**
2. Choose when to receive notifications (failures only, all runs, etc.)

### Email Reports
Scheduled runs will:
- ✅ Generate ExtentReports
- ✅ Create Excel Reports
- ✅ Capture screenshots (on failures)
- ✅ Upload all artifacts

---

## 🛠️ Advanced Configuration

### Different Schedules for Different Suites

Create multiple workflow files with different schedules:

**File:** `.github/workflows/smoke_daily.yml`
```yaml
name: Daily Smoke Tests
on:
  schedule:
    - cron: '0 2 * * *'
env:
  SCHEDULED_TEST_SUITE: 'smoke_tests.xml'
```

**File:** `.github/workflows/regression_weekly.yml`
```yaml
name: Weekly Regression
on:
  schedule:
    - cron: '0 0 * * 0'
env:
  SCHEDULED_TEST_SUITE: 'regression_tests.xml'
```

### Conditional Scheduling

Want to pause scheduled runs temporarily without editing the YAML?

**Option 1:** Disable the workflow in GitHub UI:
1. Go to **Actions** tab
2. Select your workflow
3. Click **•••** (three dots)
4. Click **Disable workflow**

**Option 2:** Comment out the schedule block (as shown earlier)

---

## ❓ FAQ

### Q: Why isn't my scheduled run executing?
**A:** Check these items:
1. ✅ Is the cron line uncommented (no `#` at the start)?
2. ✅ Is the workflow enabled in GitHub Actions?
3. ✅ Did you push the changes to your default branch (main/master)?
4. ⚠️ GitHub may delay scheduled runs by up to 15 minutes during high load

### Q: Can I run different runners on a schedule?
**A:** Not directly. Scheduled runs always use test suites (XML files). For individual runners, use manual execution via the "Run workflow" button.

### Q: How do I change which test suite runs on schedule?
**A:** Edit the `SCHEDULED_TEST_SUITE` environment variable in the workflow file:
```yaml
env:
  SCHEDULED_TEST_SUITE: 'regression_tests.xml'  # Change this
```

### Q: Can I schedule individual runners?
**A:** Not currently. Scheduled runs are designed for test suites. Individual runners are for manual/ad-hoc testing.

### Q: How do I schedule tests for different time zones?
**A:** Convert your local time to UTC and use that in the cron expression. See the "Time Zone Conversion" section above.

---

## 📚 Resources

- **Cron Expression Generator:** https://crontab.guru/
- **GitHub Actions Cron Documentation:** https://docs.github.com/en/actions/using-workflows/events-that-trigger-workflows#schedule
- **Workflow File Location:** `.github/workflows/jobmapping_e2e_tests.yml`

---

## 🎯 Quick Reference: Cron Format

```
┌───────────── minute (0 - 59)
│ ┌───────────── hour (0 - 23)
│ │ ┌───────────── day of month (1 - 31)
│ │ │ ┌───────────── month (1 - 12)
│ │ │ │ ┌───────────── day of week (0 - 6) (Sunday to Saturday)
│ │ │ │ │
│ │ │ │ │
* * * * *
```

**Examples:**
- `0 2 * * *` → Every day at 2:00 AM UTC
- `0 */6 * * *` → Every 6 hours
- `0 9 * * 1` → Every Monday at 9:00 AM UTC
- `30 14 1 * *` → 2:30 PM UTC on the 1st of every month
- `0 0 * * 0` → Every Sunday at midnight UTC

---

**Happy Testing! 🚀**

