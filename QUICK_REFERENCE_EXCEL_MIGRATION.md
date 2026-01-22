# Quick Reference: New Excel Format Migration

## ✅ What Was Done

### Code Changes Completed:
1. **Created:** `ExcelJobCatalogRefresher.java` - Handles Excel file timestamp suffixing
2. **Updated:** `PO02_AddMoreJobsFunctionality.java` - Auto-detects Excel vs CSV
3. **Updated:** `Runner02_AddMoreJobsFunctionality.java` - Calls correct refresher based on file type

### Key Points:
- ✅ Timestamp suffixing: `{JobTitle} {timestamp}` and `{JobCode}{num}-{timestamp}`
- ✅ Backward compatible: Works with both Excel and CSV
- ✅ Auto-detection: Prefers Excel, falls back to CSV
- ✅ No breaking changes: Existing tests continue to work

---

## 🎯 Next Steps for You

### 1. Prepare Your Excel File
Fill the `JobCatalogNewFormat.xlsx` file with your job data:
- **Row 7+:** Your job data (one job per row)
- **Column A:** Client Job Title* (REQUIRED)
- **Column B:** Client Job Code* (REQUIRED)
- **Other columns:** Optional but recommended

### 2. Place the File
Copy your filled Excel file to:
```
C:\Job Mapping\e2e-automation\src\test\resources\JobCatalogNewFormat.xlsx
```

### 3. Run Tests
Execute the test runner:
```bash
# Your existing test execution command
# Example: mvn test -Dtest=Runner02_AddMoreJobsFunctionality
```

### 4. Verify
Check the logs for:
- ✅ "Using Excel Job Catalog Refresher"
- ✅ "Job Catalog refreshed: X profiles updated"
- ✅ Jobs uploaded with timestamp suffixes

---

## 📋 Format Requirements

### Excel File Structure
```
Row 1: Title
Row 2: Instructions
Row 3-4: Empty
Row 5: Headers (Client Job Title*, Client Job Code*, ...)
Row 6: Field descriptions
Row 7+: YOUR DATA HERE
```

### Required Columns (minimum):
- **Column A (1):** Client Job Title* - e.g., "Software Engineer"
- **Column B (2):** Client Job Code* - e.g., "SE001"

### Example Data:
| Client Job Title* | Client Job Code* | Client Job Family | ... |
|-------------------|------------------|-------------------|-----|
| Software Engineer | SE001 | Engineering | ... |
| Product Manager | PM001 | Product | ... |

---

## 🔍 How Timestamp Works

### Before Upload:
```
Job Title: "Software Engineer"
Job Code: "SE001"
```

### After Auto-Refresh (by automation):
```
Job Title: "Software Engineer 20260122154530"
Job Code: "SE01-20260122154530"
```

### Why?
- Prevents duplicate entries on repeated test runs
- Each test execution gets unique job identifiers

---

## ⚠️ Important Notes

1. **File Location:** Must be exactly `src/test/resources/JobCatalogNewFormat.xlsx`
2. **File Name:** Case-sensitive, must match exactly
3. **Data Start:** Row 7 (automation knows to skip headers)
4. **Column Order:** Job Title FIRST, Job Code SECOND (different from old CSV!)
5. **Backup:** Automation creates backup before modification

---

## 🧪 Testing the Migration

### Option 1: Full Migration
```
1. Add JobCatalogNewFormat.xlsx to resources folder
2. Run tests
3. Verify Excel is used (check logs)
```

### Option 2: Side-by-Side Comparison
```
1. Keep both CSV and Excel files
2. Run test - Excel will be used
3. Rename Excel (e.g., to .xlsx.backup)
4. Run test again - CSV will be used
5. Compare results
```

---

## 🐛 Troubleshooting

### Problem: "No job catalog file found"
- **Cause:** Neither Excel nor CSV file present
- **Solution:** Add your Excel file to `src/test/resources/`

### Problem: "Excel file not found"
- **Cause:** File name doesn't match exactly
- **Solution:** Rename to `JobCatalogNewFormat.xlsx` (exact match)

### Problem: "No data rows found"
- **Cause:** Excel file has no data starting from row 7
- **Solution:** Add job data starting at row 7

### Problem: Jobs not showing timestamp
- **Cause:** Refresher not running
- **Solution:** Check logs for "Job Catalog refreshed" message

---

## 📞 Support

### Check Logs:
Look for these messages in test execution logs:
```
"Using Excel Job Catalog Refresher for: JobCatalogNewFormat.xlsx"
"Job Catalog refreshed: X profiles updated with suffix YYYYMMDDHHMMSS"
"Using new Excel format: JobCatalogNewFormat.xlsx"
```

### Files to Check:
1. **Test logs:** Check for refresher execution
2. **Backup folder:** `src/test/resources/JobCatalogBackups/`
3. **Original file:** Should have timestamps added after first run

---

## ✅ Summary

**Status:** READY TO USE  
**Action Required:** Place your filled Excel file in resources folder  
**Testing:** Run your existing test command  
**Rollback:** Remove Excel file, automation falls back to CSV  

---

**Created:** January 22, 2026  
**For:** Feature 02 - Add More Jobs Functionality
