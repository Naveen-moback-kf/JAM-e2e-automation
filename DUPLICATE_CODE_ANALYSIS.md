# 🔍 Duplicate Code Analysis Report

## Executive Summary

Analysis of `e2e-automation/src/main/java/com/kfonetalentsuite/pageobjects/JobMapping/` revealed **multiple categories of duplication** across 36 Page Object files.

---

## 📊 Key Findings

### 1. **Search Bar Clear/Type Pattern** ❌ **HIGHLY DUPLICATED**

**Found in 18+ files**, repeated ~50+ times:

```java
// Pattern repeated everywhere:
searchBar.click();
safeSleep(200);
searchBar.clear();
searchBar.sendKeys(Keys.CONTROL + "a");
searchBar.sendKeys(Keys.DELETE);
safeSleep(200);
searchBar.sendKeys(searchTerm);
searchBar.sendKeys(Keys.ENTER);
```

**Files affected:**
- PO05_PublishJobProfile (3 occurrences)
- PO06_PublishSelectedProfiles (2 occurrences)
- PO08_JobMappingFilters (2 occurrences)
- PO18_HCMSyncProfilesTab_PM (1 occurrence)
- PO25_MissingDataFunctionality (1 occurrence)
- PO27_SelectAllWithSearchFunctionality (multiple with JS fallback)
- PO31_ApplicationPerformance (1 occurrence)
- ... and 11 more files

**Recommendation:** ✅ **Create `BasePageObject.clearAndSearch(By locator, String text)` method**

```java
protected void clearAndSearch(By searchLocator, String searchTerm) {
    WebElement searchBar = waitForElement(searchLocator, 10);
    searchBar.click();
    safeSleep(200);
    searchBar.clear();
    searchBar.sendKeys(Keys.CONTROL + "a");
    searchBar.sendKeys(Keys.DELETE);
    safeSleep(200);
    searchBar.sendKeys(searchTerm);
    searchBar.sendKeys(Keys.ENTER);
    waitForSpinners();
}
```

**Impact:** Reduces ~200+ lines across codebase

---

### 2. **Spinner Waiting** ⚠️ **INCONSISTENT USAGE**

Three different patterns used:

```java
// Pattern 1: Direct method (395 occurrences across 35 files)
waitForSpinners();

// Pattern 2: Via PerformanceUtils
PerformanceUtils.waitForSpinnersToDisappear(driver, 10);

// Pattern 3: With timeout parameter
waitForSpinners(15);
```

**Recommendation:** ✅ **Standardize on `waitForSpinners()` from BasePageObject**

---

### 3. **Filter Dropdown Click Methods** ❌ **DUPLICATED**

In `PO08_JobMappingFilters.java`:

```java
// 6 nearly identical methods:
public void click_on_grades_filters_dropdown_button()
public void click_on_departments_filters_dropdown_button()
public void click_on_functions_subfunctions_filters_dropdown_button()
public void click_on_mapping_status_filters_dropdown_button()
public void click_on_filter_dropdown_button(String filterType) // Generic version exists!
```

**Issue:** Generic method `click_on_filter_dropdown_button()` already exists at line 1103 but specific methods still used!

**Recommendation:** ✅ **Remove specific methods, use generic method**

```java
// Instead of:
click_on_grades_filters_dropdown_button();

// Use:
click_on_filter_dropdown_button("Grades");
```

---

### 4. **Search Methods with Similar Names** ❌ **CONFUSING**

**PO05_PublishJobProfile:**
- `search_for_published_job_name_in_view_published_screen()` - Line 229
- `search_for_published_job_name_in_hcm_sync_profiles_tab_in_pm()` - Line 354
- `search_for_published_job_code_in_hcm_sync_profiles_tab_in_pm()` - Line 451

**PO06_PublishSelectedProfiles:**
- `search_for_published_job_name1()` - Line 33
- `search_for_published_job_name2()` - Line 228

**Recommendation:** ⚠️ **Keep separate** - Different contexts (View Published vs HCM Sync), but could extract common search logic

---

### 5. **Large Files with Potential Duplication** 🔴 **HIGH PRIORITY**

| File | Lines | Potential Issues |
|------|-------|------------------|
| PO17_MapDifferentSPtoProfile | 1906 | Very large, likely has duplication |
| PO18_HCMSyncProfilesTab_PM | 1841 | Very large, needs analysis |
| PO31_ApplicationPerformance_JAM_and_HCM | 1875 | Very large, performance tests |
| PO24_InfoMessageManualMappingProfiles | 1790 | Large, info message handling |
| PO23_InfoMessageMissingDataProfiles | 1451 | Large, info message handling |
| PO25_MissingDataFunctionality | 1333 | Large, missing data logic |
| PO14_SortingFunctionality_JAM | 1112 | Large, sorting tests |
| PO08_JobMappingFilters | 1093 | Large, filter logic |
| BasePageObject | 1008 | Base class, acceptable |

**Recommendation:** ✅ **Analyze PO17, PO18, PO31 for extraction opportunities**

---

### 6. **BasePageObject Already Has Helper Methods** ✅ **UNDERUTILIZED**

`BasePageObject` already provides:

```java
protected void searchFor(String searchText) // Line 561 - UNDERUTILIZED!
protected void openFilters() // Line 549
protected void clearFilters() // Line 554
protected void selectAllProfiles() // Line 526
protected void deselectAllProfiles() // Line 537
```

**Issue:** Many Page Objects duplicate this logic instead of using BasePageObject methods!

**Recommendation:** ✅ **Use existing `searchFor()` method instead of duplicating search logic**

---

### 7. **JavaScript Search Implementation** ⚠️ **COMPLEX DUPLICATION**

`PO27_SelectAllWithSearchFunctionality` has elaborate JS-based search (Lines 686-727):

```java
String clearAndSearchScript = 
    "var searchBar = document.querySelector('#search-job-title-input-search-input');" +
    "if (searchBar) {" +
    "  searchBar.focus();" +
    "  searchBar.value = '';" +
    "  searchBar.dispatchEvent(new Event('input', { bubbles: true }));" +
    "  searchBar.value = '" + substring + "';" +
    "  searchBar.dispatchEvent(new Event('input', { bubbles: true }));" +
    "  searchBar.dispatchEvent(new KeyboardEvent('keydown', { key: 'Enter', keyCode: 13, bubbles: true }));" +
    "  return true;" +
    "} return false;";
```

**Recommendation:** ⚠️ **Keep separate** - Advanced search with fallback logic, specific use case

---

## 🎯 Actionable Recommendations

### Priority 1: **High Impact, Low Effort**

1. ✅ **Add `clearAndSearch()` to BasePageObject**
   - Impact: Eliminates 200+ lines
   - Effort: 1 hour
   - Files affected: 18+

2. ✅ **Remove duplicate filter dropdown methods in PO08**
   - Impact: Eliminates 30+ lines
   - Effort: 30 minutes
   - Files affected: 1

3. ✅ **Promote `searchFor()` usage from BasePageObject**
   - Impact: Standardization
   - Effort: Documentation update
   - Files affected: All POs

### Priority 2: **Medium Impact, Medium Effort**

4. ⚠️ **Analyze PO17 (1906 lines) for extraction**
   - Impact: Potentially 500+ lines
   - Effort: 4-6 hours
   - Needs detailed analysis

5. ⚠️ **Analyze PO18 (1841 lines) for extraction**
   - Impact: Potentially 400+ lines
   - Effort: 4-6 hours
   - Needs detailed analysis

6. ⚠️ **Consolidate Info Message handling** (PO23, PO24)
   - Impact: 1000+ lines potentially
   - Effort: 6-8 hours
   - Create InfoMessageHelper utility

### Priority 3: **Low Impact (Already Good)**

7. ✅ **No action needed:**
   - BasePageObject (well-structured)
   - Search methods with different contexts (keep separate)
   - JS-based search in PO27 (specialized logic)

---

## 📈 Estimated Impact

### If Priority 1 & 2 Recommendations Implemented:

| Metric | Current | After Cleanup | Improvement |
|--------|---------|---------------|-------------|
| Total PO Lines | ~25,000 | ~22,500 | -10% (2,500 lines) |
| Duplicate Search Logic | 50+ instances | 1 method | -98% duplication |
| Filter Dropdown Methods | 6 methods | 1 method | -83% duplication |
| Maintainability | Medium | High | ✅ Improved |

---

## 🚀 Next Steps

1. **Immediate:** Implement Priority 1 items (1.5 hours total)
2. **This Week:** Analyze PO17 and PO18 for specific duplication patterns
3. **This Sprint:** Create InfoMessageHelper utility
4. **Ongoing:** Promote BasePageObject helper method usage in code reviews

---

## 📝 Notes

- ✅ Recent cleanup already removed search retry logic (good!)
- ✅ DailyExcelTracker already optimized (4,093 lines, consolidated StatusConverter)
- ✅ ProgressBarUtil merged into ExcelReportListener (good!)
- ⚠️ Focus next on Page Objects, especially largest files

---

**Report Generated:** 2026-01-02
**Total Files Analyzed:** 36 Page Objects + BasePageObject
**Total Lines Analyzed:** ~25,000 lines

