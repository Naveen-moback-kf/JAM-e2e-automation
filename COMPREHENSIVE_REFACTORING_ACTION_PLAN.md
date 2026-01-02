# 🎯 COMPREHENSIVE REFACTORING ACTION PLAN
## E2E Automation Framework - Job Mapping Module

**Analysis Date:** 2026-01-02  
**Total Files Analyzed:** 56 Java files  
**Total Lines of Code:** ~37,000 lines  
**Estimated Duplicate/Refactorable Code:** ~4,500 lines (12%)

---

## 📊 EXECUTIVE SUMMARY

### Critical Metrics
| Metric | Current State | Target State | Improvement |
|--------|---------------|--------------|-------------|
| **Total Page Object Lines** | 33,116 lines | 28,000 lines | -15% (5,116 lines) |
| **Duplicate Search Patterns** | 50+ instances | 1 method | -98% |
| **Avg File Size** | 920 lines | 650 lines | -29% |
| **Files > 1500 lines** | 5 files | 0 files | -100% |
| **Test Execution Time** | Baseline | -20% (estimate) | Faster |

---

## 🔴 PHASE 1: CRITICAL - IMMEDIATE WINS (Week 1)
**Effort:** 12 hours | **Impact:** HIGH | **Risk:** LOW

### 1.1 Add Common Search Helper to BasePageObject ⏱️ 2 hours
**Priority: P0 - Critical**

**Problem:** Search bar clear/type pattern duplicated 50+ times across 18 files

```java
// Current duplication (50+ places):
searchBar.click();
safeSleep(200);
searchBar.clear();
searchBar.sendKeys(Keys.CONTROL + "a");
searchBar.sendKeys(Keys.DELETE);
safeSleep(200);
searchBar.sendKeys(searchTerm);
searchBar.sendKeys(Keys.ENTER);
waitForSpinners();
```

**Solution:** Add to BasePageObject.java

```java
/**
 * Clears search input and performs search
 * @param searchLocator By locator for search input
 * @param searchTerm Text to search for
 */
protected void clearAndSearch(By searchLocator, String searchTerm) {
    WebElement searchBar = waitForElement(searchLocator, 10);
    searchBar.click();
    safeSleep(200);
    searchBar.clear();
    searchBar.sendKeys(Keys.CONTROL + "a");
    searchBar.sendKeys(Keys.DELETE);
    safeSleep(200);
    searchBar.sendKeys(searchTerm);
    safeSleep(300);
    searchBar.sendKeys(Keys.ENTER);
    waitForSpinners();
    PerformanceUtils.waitForPageReady(driver, 3);
}

/**
 * Clears search input only (no search execution)
 */
protected void clearSearchBar(By searchLocator) {
    WebElement searchBar = waitForElement(searchLocator, 10);
    searchBar.click();
    safeSleep(200);
    searchBar.clear();
    searchBar.sendKeys(Keys.CONTROL + "a");
    searchBar.sendKeys(Keys.DELETE);
    safeSleep(200);
}
```

**Files to Update:**
- PO05_PublishJobProfile.java (3 instances)
- PO06_PublishSelectedProfiles.java (2 instances)
- PO08_JobMappingFilters.java (2 instances)
- PO18_HCMSyncProfilesTab_PM.java (1 instance)
- PO25_MissingDataFunctionality.java (1 instance)
- PO27_SelectAllWithSearchFunctionality.java (5 instances)
- PO31_ApplicationPerformance_JAM_and_HCM.java (1 instance)
- ... and 11 more files

**Impact:** -200+ lines, standardized search behavior

---

### 1.2 Remove Duplicate Filter Dropdown Methods ⏱️ 1 hour
**Priority: P0 - Critical**

**Problem:** PO08_JobMappingFilters has 5 duplicate methods when a generic one exists

**Current (Lines 61, 362, 519, 1051):**
```java
public void click_on_grades_filters_dropdown_button()
public void click_on_departments_filters_dropdown_button()
public void click_on_functions_subfunctions_filters_dropdown_button()
public void click_on_mapping_status_filters_dropdown_button()
```

**Already exists at line 1103:**
```java
public void click_on_filter_dropdown_button(String filterType) // Generic!
```

**Action:**
1. Delete lines 61-66 (click_on_grades_filters_dropdown_button)
2. Delete lines 362-369 (click_on_departments_filters_dropdown_button)
3. Delete lines 519-526 (click_on_functions_subfunctions_filters_dropdown_button)
4. Delete lines 1051-1057 (click_on_mapping_status_filters_dropdown_button)
5. Update step definitions to use generic method:
   - `click_on_grades_filters_dropdown_button()` → `click_on_filter_dropdown_button("Grades")`

**Impact:** -30 lines, better maintainability

---

### 1.3 Standardize Scroll Operations ⏱️ 1 hour
**Priority: P1 - High**

**Problem:** 161 instances of scroll operations across 26 files with inconsistent patterns

**Current patterns:**
```java
scrollToTop();
scrollToElement(element);
js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
```

**Action:**
All methods already exist in BasePageObject (lines 435-450). **Just need to promote usage!**

**Create:** `BasePageObject` documentation comment block at top of class:

```java
/**
 * BasePageObject - Common utilities for all Page Objects
 * 
 * SCROLLING:
 *   - scrollToTop() - Scroll to page top
 *   - scrollToBottom() - Scroll to page bottom  
 *   - scrollToElement(WebElement) - Scroll element into view
 * 
 * SEARCHING:
 *   - searchFor(String) - Basic search (line 561)
 *   - clearAndSearch(By, String) - Clear and search (NEW)
 * 
 * CLICKING:
 *   - clickElement(By) - Smart click with retries
 *   - clickElementSafely(By, int) - Click with custom timeout
 *   - jsClick(WebElement) - JavaScript click fallback
 * 
 * WAITING:
 *   - waitForSpinners() - Wait for all spinners
 *   - waitForElement(By) - Wait for visibility
 *   - waitForClickable(By) - Wait for clickable
 */
```

**Impact:** +1 documentation block, prevents future duplication

---

### 1.4 Add Filter Helpers to BasePageObject ⏱️ 1 hour
**Priority: P1 - High**

**Problem:** Filter operations duplicated across multiple Page Objects

**Current (already exists but underutilized):**
```java
protected void openFilters() // Line 549
protected void clearFilters() // Line 554
protected void selectAllProfiles() // Line 526
protected void deselectAllProfiles() // Line 537
```

**Add more helpers:**
```java
/**
 * Clicks a specific filter dropdown by name
 * @param filterName Filter name (e.g., "Grades", "Departments")
 */
protected void openFilterDropdown(String filterName) {
    By filterLocator = By.xpath("//div[@data-testid='dropdown-" + filterName + "']");
    clickElement(filterLocator);
    PerformanceUtils.waitForUIStability(driver, 1);
}

/**
 * Closes filters panel
 */
protected void closeFilters() {
    try {
        clickElement(Locators.SearchAndFilters.FILTERS_BTN);
        PerformanceUtils.waitForUIStability(driver, 1);
    } catch (Exception e) {
        LOGGER.debug("Filters already closed or not present");
    }
}
```

**Impact:** +20 lines in BasePageObject, prevents future duplication

---

### 1.5 Document BasePageObject Helper Methods ⏱️ 30 minutes
**Priority: P1 - High**

**Action:** Create `BASEPAGEOBJECT_HELPERS.md` with usage guide

**Impact:** Developer education, prevents reinventing the wheel

---

## 🟡 PHASE 2: HIGH IMPACT - LARGE FILE REFACTORING (Week 2-3)
**Effort:** 40 hours | **Impact:** VERY HIGH | **Risk:** MEDIUM

### 2.1 Refactor PO31_ApplicationPerformance_JAM_and_HCM ⏱️ 12 hours
**Priority: P1 - High**

**Current:** 2,253 lines (LARGEST FILE)  
**Target:** 1,200 lines (-47%)

**Analysis:**
- 60+ public methods (performance tests)
- 45+ Thread.sleep/safeSleep calls
- 44+ waitForElement calls
- Likely has duplicate wait/assertion patterns

**Action Plan:**
1. **Hour 1-2:** Analyze file, identify duplicate patterns
2. **Hour 3-6:** Extract common performance test utilities:
   - Create `PerformanceTestHelper.java` utility class
   - Move assertion patterns to helper
   - Create `assertPageLoadTime(String pageName, long maxMs)` method
   - Create `measureAndLogPerformance(String operation, Runnable action)` method
3. **Hour 7-10:** Refactor PO31 to use helpers
4. **Hour 11-12:** Test, verify no regressions

**Expected Result:**
- PO31: 2,253 → 1,200 lines (-1,053 lines)
- New file: PerformanceTestHelper.java (300 lines)
- **Net reduction:** -753 lines

---

### 2.2 Refactor PO24_InfoMessageManualMappingProfiles ⏱️ 10 hours
**Priority: P1 - High**

**Current:** 2,095 lines (2nd LARGEST)  
**Target:** 1,000 lines (-52%)

**Analysis:**
- Info message validation logic
- Likely shares patterns with PO23_InfoMessageMissingDataProfiles (1,700 lines)

**Action Plan:**
1. **Hour 1-3:** Compare PO23 and PO24, identify common patterns
2. **Hour 4-7:** Create `InfoMessageValidator.java` utility:
   ```java
   public class InfoMessageValidator {
       public static void validateInfoMessage(WebDriver driver, String expectedMessage);
       public static void validateInfoIconPresent(WebDriver driver);
       public static void clickInfoIcon(WebDriver driver);
       public static String extractInfoMessageText(WebDriver driver);
   }
   ```
3. **Hour 8-10:** Refactor both PO23 and PO24 to use validator

**Expected Result:**
- PO23: 1,700 → 900 lines (-800 lines)
- PO24: 2,095 → 1,000 lines (-1,095 lines)
- New file: InfoMessageValidator.java (400 lines)
- **Net reduction:** -1,495 lines

---

### 2.3 Refactor PO18_HCMSyncProfilesTab_PM ⏱️ 8 hours
**Priority: P2 - Medium**

**Current:** 2,056 lines (3rd LARGEST)  
**Target:** 1,200 lines (-42%)

**Analysis:**
- HCM Sync profile operations
- 116+ waitForElement calls (most in codebase!)
- 42 public methods

**Action Plan:**
1. **Hour 1-2:** Identify duplicate HCM Sync patterns
2. **Hour 3-5:** Extract to `HCMSyncHelper.java`:
   - `navigateToHCMSync()`
   - `searchInHCMSync(String searchTerm)`
   - `selectProfileInHCMSync(String profileName)`
   - `validateProfileInHCMSync(String profileName)`
3. **Hour 6-8:** Refactor PO18, test

**Expected Result:**
- PO18: 2,056 → 1,200 lines (-856 lines)
- New file: HCMSyncHelper.java (350 lines)
- **Net reduction:** -506 lines

---

### 2.4 Refactor PO17_MapDifferentSPtoProfile ⏱️ 8 hours
**Priority: P2 - Medium**

**Current:** 2,007 lines (4th LARGEST)  
**Target:** 1,100 lines (-45%)

**Analysis:**
- Profile mapping operations
- 114+ waitForElement calls (2nd most!)
- 63+ safeSleep calls
- 48 public methods

**Action Plan:**
1. **Hour 1-2:** Analyze duplicate mapping patterns
2. **Hour 3-6:** Extract to `ProfileMappingHelper.java`:
   - `searchForProfile(String profileName)`
   - `selectProfile(String profileName)`
   - `mapProfile(String jobName, String profileName)`
   - `validateMappingSuccess(String jobName, String profileName)`
3. **Hour 7-8:** Refactor PO17, test

**Expected Result:**
- PO17: 2,007 → 1,100 lines (-907 lines)
- New file: ProfileMappingHelper.java (400 lines)
- **Net reduction:** -507 lines

---

### 2.5 Refactor PO25_MissingDataFunctionality ⏱️ 6 hours
**Priority: P2 - Medium**

**Current:** 1,481 lines  
**Target:** 900 lines (-39%)

**Action Plan:**
1. Hour 1-2: Analyze missing data patterns
2. Hour 3-5: Extract to MissingDataHelper.java
3. Hour 6: Refactor PO25, test

**Expected Result:**
- PO25: 1,481 → 900 lines (-581 lines)
- New file: MissingDataHelper.java (250 lines)
- **Net reduction:** -331 lines

---

## 🟢 PHASE 3: OPTIMIZATION - INCREMENTAL IMPROVEMENTS (Week 4)
**Effort:** 16 hours | **Impact:** MEDIUM | **Risk:** LOW

### 3.1 Reduce safeSleep/Thread.sleep Usage ⏱️ 8 hours
**Priority: P2 - Medium**

**Current:** 452 instances across 31 files

**Problem:** Hardcoded sleeps slow down tests unnecessarily

**Action:**
1. **Hour 1-3:** Analyze sleep patterns, identify common use cases:
   - After click: `safeSleep(200)` → Use `PerformanceUtils.waitForUIStability()`
   - After search: `safeSleep(1500)` → Use `waitForSpinners()` + `waitForPageReady()`
   - Before element interaction: Remove (use proper waits)

2. **Hour 4-7:** Create smart wait wrapper:
   ```java
   /**
    * Waits for element and UI stability before returning
    */
   protected WebElement waitForElementAndStability(By locator, int timeoutSec) {
       WebElement element = waitForElement(locator, timeoutSec);
       PerformanceUtils.waitForUIStability(driver, 1);
       return element;
   }
   ```

3. **Hour 8:** Update top 5 files with most sleeps:
   - PO08 (41 sleeps)
   - PO06 (38 sleeps)
   - PO05 (27 sleeps)

**Expected Result:**
- Replace ~50 hardcoded sleeps with smart waits
- Test execution time: -10% (estimate)

---

### 3.2 Consolidate Locators ⏱️ 4 hours
**Priority: P3 - Low**

**Problem:** Some locators duplicated across Page Objects

**Action:**
1. Hour 1-2: Audit all locators, find duplicates
2. Hour 3-4: Move common locators to BasePageObject.Locators

**Expected Result:**
- -50 lines of duplicate locators
- Single source of truth for common elements

---

### 3.3 Standardize Method Naming ⏱️ 4 hours
**Priority: P3 - Low**

**Problem:** Inconsistent naming conventions

**Current patterns:**
- `user_should_verify_...` (BDD style)
- `click_on_...` (action style)
- `validate...` (assertion style)

**Action:**
1. Create naming convention guide
2. Update 20 most confusing method names
3. Add JavaDoc to clarify intent

**Expected Result:**
- Improved code readability
- Easier onboarding for new developers

---

## 📈 PHASE 4: MAINTENANCE - LONG-TERM IMPROVEMENTS (Ongoing)
**Effort:** Ongoing | **Impact:** HIGH | **Risk:** LOW

### 4.1 Create Code Review Checklist
**Priority: P2 - Medium**

**Checklist:**
- [ ] Used BasePageObject helpers instead of duplicating code?
- [ ] Avoided hardcoded `Thread.sleep()` / `safeSleep()` where proper waits exist?
- [ ] Used `clearAndSearch()` instead of manual clear + type + enter?
- [ ] Used `clickElementSafely()` for problematic clicks?
- [ ] Added methods to BasePageObject if used in 3+ Page Objects?
- [ ] File size < 1000 lines? (Extract helper if larger)

---

### 4.2 Setup Automated Code Quality Checks
**Priority: P3 - Low**

**Tools:**
- PMD/Checkstyle for code duplication detection
- SonarQube for code smell detection
- Custom script to flag files > 1000 lines

---

### 4.3 Quarterly Refactoring Review
**Priority: P3 - Low**

**Schedule:**
- Q1: Review largest files
- Q2: Review most-changed files
- Q3: Review slowest tests
- Q4: Review code duplication metrics

---

## 📊 EXPECTED OUTCOMES

### By End of Phase 1 (Week 1)
- **Lines removed:** 230 lines
- **Files improved:** 18+ files
- **Execution time:** -5%
- **Developer experience:** Documented helpers

### By End of Phase 2 (Week 3)
- **Lines removed:** 3,500+ lines
- **New utility files:** 5 helpers
- **Files < 1500 lines:** All (0 oversized files)
- **Execution time:** -15%

### By End of Phase 3 (Week 4)
- **Lines removed:** 4,200+ lines
- **Total reduction:** 12% of codebase
- **Execution time:** -20%
- **Maintainability:** HIGH (from MEDIUM)

---

## 🚀 IMPLEMENTATION ROADMAP

### Week 1: CRITICAL WINS
- **Mon:** 1.1 Add clearAndSearch() helper
- **Tue:** 1.2 Remove duplicate filter methods
- **Wed:** 1.3 Standardize scroll operations
- **Thu:** 1.4 Add filter helpers
- **Fri:** 1.5 Documentation + Phase 1 testing

### Week 2-3: LARGE FILE REFACTORING
- **Week 2 Mon-Wed:** 2.1 Refactor PO31 (Performance)
- **Week 2 Thu-Fri:** 2.2 Start PO24 (InfoMessage)
- **Week 3 Mon-Tue:** 2.2 Complete PO24 + PO23
- **Week 3 Wed-Thu:** 2.3 Refactor PO18 (HCMSync)
- **Week 3 Fri:** Phase 2 testing

### Week 4: OPTIMIZATION
- **Mon-Tue:** 3.1 Reduce sleep usage
- **Wed:** 3.2 Consolidate locators
- **Thu:** 3.3 Standardize naming
- **Fri:** Phase 3 testing + retrospective

---

## ⚠️ RISKS & MITIGATION

### Risk 1: Breaking Existing Tests
**Mitigation:** 
- Refactor one file at a time
- Run full regression after each change
- Keep git commits small and atomic

### Risk 2: Time Overruns
**Mitigation:**
- Focus on Phase 1 first (highest ROI)
- Phase 2-3 can be done incrementally
- Skip Phase 3 if timeline is tight

### Risk 3: Merge Conflicts (Parallel Development)
**Mitigation:**
- Coordinate with team on large file refactors
- Use feature branches
- Prioritize files with least active development

---

## 📋 SUCCESS METRICS

| Metric | Baseline | Target | Measure |
|--------|----------|--------|---------|
| **Total LOC** | 33,116 | 28,000 | Git stats |
| **Duplicate Code %** | 12% | 3% | SonarQube |
| **Files > 1500 lines** | 5 | 0 | File count |
| **Avg Method Length** | 35 lines | 20 lines | PMD |
| **Test Execution Time** | 100% | 80% | Jenkins metrics |
| **Code Review Time** | 60 min/PR | 30 min/PR | Team feedback |

---

## 🎯 PRIORITY SUMMARY

### Do First (Week 1):
1. ✅ Add `clearAndSearch()` helper → BasePageObject
2. ✅ Remove duplicate filter methods → PO08
3. ✅ Document BasePageObject helpers
4. ✅ Add filter helpers → BasePageObject

### Do Next (Week 2-3):
1. ⏭️ Refactor PO31 (2,253 lines → 1,200 lines)
2. ⏭️ Refactor PO24 + PO23 (3,795 lines → 1,900 lines)
3. ⏭️ Refactor PO18 (2,056 lines → 1,200 lines)
4. ⏭️ Refactor PO17 (2,007 lines → 1,100 lines)

### Consider Later (Week 4+):
1. 🔜 Reduce sleep usage
2. 🔜 Consolidate locators
3. 🔜 Standardize naming
4. 🔜 Setup code quality checks

---

**Total Estimated Effort:** 68 hours (1.5 sprints)  
**Total Expected LOC Reduction:** 5,116 lines (15%)  
**ROI:** Very High ✅

---

**Next Step:** Start with Phase 1, Task 1.1 - Add `clearAndSearch()` helper (2 hours)


