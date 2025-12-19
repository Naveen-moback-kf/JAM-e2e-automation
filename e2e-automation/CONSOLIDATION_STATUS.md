# Job Mapping Test Consolidation Status

## Overview
This document tracks the consolidation of redundant test files in the Job Mapping automation framework. The goal is to reduce code duplication by creating parameterized tests that handle multiple similar scenarios.

---

## ✅ Completed Consolidations

### 1. Missing Data Validation (Feature #29)
**Consolidated:** December 2024

| Before (4 separate files) | After (1 consolidated file) |
|--------------------------|----------------------------|
| 29ValidateMissingGradeData.feature | 29ValidateMissingDataFunctionality.feature |
| 30ValidateMissingDepartmentData.feature | |
| 31ValidateMissingFunctionData.feature | |
| 32ValidateMissingSubfunctionData.feature | |

**Files Created:**
- `29ValidateMissingDataFunctionality.feature` - Parameterized with `<DataType>` (Grade, Department, Function, Subfunction)
- `PO29_ValidateMissingDataFunctionality.java` - Unified Page Object
- `SD29_ValidateMissingDataFunctionality.java` - Step Definitions
- `Runner29_ValidateMissingDataFunctionality.java`
- `CrossBrowser29_ValidateMissingDataFunctionalityRunner.java`

**Key Features:**
- Scenario Outline with Examples table for each data type
- Forward Flow: Job Mapping → Missing Data Screen → Validate
- Reverse Flow: Missing Data Screen → Job Mapping → Validate
- Lazy loading support with scrolling
- Dynamic column indexing based on data type

---

### 2. Clear Profile Selection (Feature #42)
**Consolidated:** December 2024

| Before (4 separate files) | After (1 consolidated file) |
|--------------------------|----------------------------|
| 42ClearProfileSelectionwithHeaderCheckbox_PM.feature | 42_ClearProfileSelectionFunctionality.feature |
| 43ClearProfileSelectionwithNoneButton_PM.feature | |
| 44ClearProfileSelectionwithHeaderCheckbox_JAM.feature | |
| 45ClearProfileSelectionwithNoneButton_JAM.feature | |

**Files Created:**
- `42_ClearProfileSelectionFunctionality.feature` - Parameterized with `<screen>` (PM/JAM) and `<method>` (HeaderCheckbox/NoneButton)
- `PO42_ClearProfileSelectionFunctionality.java` - Unified Page Object
- `SD42_ClearProfileSelectionFunctionality.java` - Step Definitions
- `Runner42_ClearProfileSelectionFunctionality.java`
- `CrossBrowser42_ClearProfileSelectionFunctionalityRunner.java`

**Key Features:**
- Screen-agnostic methods (PM = HCM Sync Profiles, JAM = Job Architecture Manager)
- Two clear methods: Header Checkbox and None Button
- Verifies loaded profiles unselected after clear
- Verifies newly loaded profiles (after scroll) retain original selection

---

### 3. Select All with Search (Feature #35)
**Consolidated:** December 18, 2024

| Before (4 separate files) | After (consolidated files) |
|--------------------------|----------------------------|
| 35ValidateSelectAllWithSearchFunctionality_PM.feature | 35SelectAllWithSearchFunctionality_PM.feature |
| 35aValidateLoadedProfilesSelectionWithSearch_PM.feature | 35SelectAllWithSearchFunctionality_JAM.feature |
| 38ValidateSelectAllWithSearchFunctionality_JAM.feature | |
| 38aValidateLoadedProfilesSelectionWithSearch_JAM.feature | |

**Files Created:**
- `35SelectAllWithSearchFunctionality_PM.feature` - PM scenarios (Select All, Alternative Validation, Sync, History, Loaded Profiles)
- `35SelectAllWithSearchFunctionality_JAM.feature` - JAM scenarios (Select All, Alternative Validation, Loaded Profiles)
- `PO35_SelectAllWithSearchFunctionality.java` - Unified Page Object for both PM and JAM
- `SD35_SelectAllWithSearchFunctionality.java` - Step Definitions with parameterized `{string} screen`
- `Runner35_SelectAllWithSearchFunctionality_PM.java`
- `Runner35_SelectAllWithSearchFunctionality_JAM.java`
- `CrossBrowser35_SelectAllWithSearchFunctionalityRunner.java`

**Key Features:**
- Screen parameter (PM/JAM) for all methods
- Full page scroll (`window.scrollTo`) for proper lazy loading
- 100K API background data load wait support
- Different scroll wait times: JAM (500ms), PM (1500ms)
- Profiles per scroll: JAM (~10), PM (~50)
- JavaScript-based checkbox counting for performance
- Alternative validation with second search substring
- Clear search bar with stale element handling

**Files Deleted:**
- `PO35_ValidateSelectAllWithSearchFunctionality_PM.java`
- `PO38_ValidateSelectAllWithSearchFunctionality_JAM.java`
- `SD35_ValidateSelectAllWithSearchFunctionality_PM.java`
- `SD38_ValidateSelectAllWithSearchFunctionality_JAM.java`
- Runner35, Runner35a, Runner38, Runner38a
- Old feature files (35Validate, 35a, 38Validate, 38a)

---

### 4. Select All with Filters (Feature #36)
**Consolidated:** December 18, 2024

| Before (4 separate files) | After (consolidated files) |
|--------------------------|----------------------------|
| 36ValidateSelectAllWithFiltersFunctionality_PM.feature | 36_SelectAllWithFiltersFunctionality_PM.feature |
| 36aValidateLoadedProfilesSelectionWithFilters_PM.feature | 36_SelectAllWithFiltersFunctionality_JAM.feature |
| 39ValidateSelectAllWithFiltersFunctionality_JAM.feature | |
| 39aValidateLoadedProfilesSelectionWithFilters_JAM.feature | |

**Files Created:**
- `36_SelectAllWithFiltersFunctionality_PM.feature` - PM scenarios (Select All with Filters, Alternative Filter, Sync, History, Loaded Profiles)
- `36_SelectAllWithFiltersFunctionality_JAM.feature` - JAM scenarios (Select All with Filters, Alternative Filter, Publish, History, Loaded Profiles)
- `PO36_SelectAllWithFiltersFunctionality.java` - Unified Page Object for both PM and JAM (filter-specific methods only)
- `SD36_SelectAllWithFiltersFunctionality.java` - Step Definitions (filter-specific steps only, reuses SD35 common steps)
- `Runner36_SelectAllWithFiltersFunctionality_PM.java`
- `Runner36_SelectAllWithFiltersFunctionality_JAM.java`

**Key Features:**
- Screen parameter (PM/JAM) for all methods
- PM Filters: KF Grade, Levels
- JAM Filters: Grades, Departments
- **Reuses SD35 step definitions** for common operations (chevron, select all, header checkbox, scroll, verify, capture baseline)
- **Reuses SD42 step definition** for action button verification
- Filter-specific: apply filter, clear filters, validate filtered results, alternative filter validation
- JavaScript-based checkbox counting inherited from BasePageObject

**Files Deleted:**
- `PO36_ValidateSelectAllWithFiltersFunctionality_PM.java`
- `PO39_ValidateSelectAllWithFiltersFunctionality_JAM.java`
- `SD36_ValidateSelectAllWithFiltersFunctionality_PM.java`
- `SD39_ValidateSelectAllWithFiltersFunctionality_JAM.java`
- Runner36_Validate, Runner36a, Runner39_Validate, Runner39a
- Old feature files (36Validate, 36a, 39Validate, 39a)

**Code Reuse Optimization:**
- SD36 reduced from 16 steps to 9 steps (removed 7 duplicate steps)
- PO36 reduced from 16 methods to 9 methods (removed 7 duplicate methods)
- Common locators and helpers moved to `BasePageObject`

---

### 5. Filters Functionality (Feature #11)
**Consolidated:** Earlier

| Before (multiple filter files) | After (1 consolidated file) |
|-------------------------------|----------------------------|
| Multiple individual filter tests | 11ValidateJobMappingFiltersFunctionality.feature |

---

### 6. Filter Persistence (Feature #12)
**Consolidated:** December 19, 2024

| Before (2 separate files) | After (1 consolidated file) |
|--------------------------|----------------------------|
| 12aValidateBasicFilterPersistence.feature | 12_FilterPersistence.feature |
| 12bValidateAdvancedFilterPersistence.feature | |

**Files Created:**
- `12_FilterPersistence.feature` - All filter persistence scenarios organized by type
- `Runner12_FilterPersistence.java`

**Key Features:**
- Basic Filter Persistence: Grades filters, Mapping Status filters
- Advanced Filter Persistence: Manual Mapping navigation, View Published screen
- Tests persistence after: Page refresh, Job Comparison, Profile Manager, Manual Mapping

**Files Deleted:**
- `12aValidateBasicFilterPersistence.feature`
- `12bValidateAdvancedFilterPersistence.feature`
- `Runner12a_ValidateBasicFilterPersistence.java`
- `Runner12b_ValidateAdvancedFilterPersistence.java`
- CrossBrowser runners (12a, 12b)

---

### 7. Sorting Persistence (Feature #18)
**Consolidated:** December 19, 2024

| Before (2 separate files) | After (1 consolidated file) |
|--------------------------|----------------------------|
| 18aValidateBasicSortingPersistence.feature | 18_SortingPersistence.feature |
| 18bValidateAdvancedSortingPersistence.feature | |

**Files Created:**
- `18_SortingPersistence.feature` - All sorting persistence scenarios organized by type
- `Runner18_SortingPersistence.java`

**Key Features:**
- Basic Sorting Persistence: Multi-level sorting (Grade + Job Name)
- Advanced Sorting Persistence: Sorting with filters, View Published screen
- Tests persistence after: Page refresh, Job Comparison, Profile Manager, Manual Mapping

**Files Deleted:**
- `18aValidateBasicSortingPersistence.feature`
- `18bValidateAdvancedSortingPersistence.feature`
- `Runner18a_ValidateBasicSortingPersistence.java`
- `Runner18b_ValidateAdvancedSortingPersistence.java`
- CrossBrowser runners (18a, 18b)

### 8. Unmapped Jobs Validation (Feature #46)
**Consolidated:** December 19, 2024

| Before (2 separate files) | After (1 consolidated file) |
|--------------------------|----------------------------|
| 19VerifyJobsWithNoBICMappings.feature | 46VerifyUnmappedJobs_JAM.feature |
| 46ValidateSelectionOfUnmappedJobs_JAM.feature | |

**Files Created:**
- `46VerifyUnmappedJobs_JAM.feature` - Combined unmapped jobs scenarios

**Key Features:**
- Verify jobs with no BIC mappings
- Selection behavior for unmapped jobs
- JAM-specific validation

**Files Deleted:**
- `19VerifyJobsWithNoBICMappings.feature`
- Old `46ValidateSelectionOfUnmappedJobs_JAM.feature`
- Related runner files

---

### 9. Profile Details Popup (Feature #05 → #15)
**Consolidated:** December 19, 2024

| Before (duplicate functionality) | After (merged into existing) |
|--------------------------------|----------------------------|
| 05ValidateJobProfileDetailsPopup.feature | (Merged into 15ValidateRecommendedProfileDetails.feature) |

**Changes:**
- Moved all methods from `PO05_ValidateJobProfileDetailsPopup.java` to `PO15_ValidateRecommendedProfileDetails.java`
- Moved all step definitions from `SD05` to `SD15`
- Feature 05 scenarios were already present in Feature 15

**Files Deleted:**
- `PO05_ValidateJobProfileDetailsPopup.java`
- `SD05_ValidateJobProfileDetailsPopup.java`
- `05ValidateJobProfileDetailsPopup.feature`
- Related runner files

---

### 10. Single Job Publish (Features #06, #07, #08)
**Consolidated:** December 19, 2024

| Before (3 separate files) | After (1 consolidated file) |
|--------------------------|----------------------------|
| 06PublishJobProfile.feature | 06_PublishJobProfile.feature |
| 07PublishJobFromComparisonScreen.feature | |
| 08PublishJobFromDetailsPopup.feature | |

**Files Created:**
- `06_PublishJobProfile.feature` - 3 distinct flows with separate tags

**Key Features:**
- **Flow 1** (`@Publish_From_Listing`): Publish from Jobs Listing Table
- **Flow 2** (`@Publish_From_Comparison`): Publish from Job Comparison Screen
- **Flow 3** (`@Publish_From_Popup`): Publish from Profile Details Popup
- Each flow verifies publish in: View Published, HCM Sync Profiles, Architect
- Job code extraction and global storage (`job1OrgCode` ThreadLocal)
- Publish success verification for all flows

**Files Deleted:**
- `PO07_PublishJobFromComparisonScreen.java`
- `PO08_PublishJobFromDetailsPopup.java`
- `SD07_PublishJobFromComparisonScreen.java`
- `SD08_PublishJobFromDetailsPopup.java`
- `Runner07_PublishJobFromComparisonScreen.java`
- `Runner08_PublishJobFromDetailsPopup.java`
- `07PublishJobFromComparisonScreen.feature`
- `08PublishJobFromDetailsPopup.feature`

---

## 🎯 High Priority - Recommended Next Consolidations

### 1. Select and Sync/Publish (PM + JAM) → Could become Feature #33
**Current Files (4):**
- `33ValidateSelectAndHCMSyncLoadedProfiles_PM.feature`
- `34ValidateSelectAndSyncAllProfiles_PM.feature`
- `37ValidateSelectAndPublishLoadedProfiles_JAM.feature`
- `40ValidateSelectAndPublishAllJobProfiles_JAM.feature`

**Consolidation Strategy:**
- Parameterize by `<screen>` (PM/JAM) and `<scope>` (Loaded/All)
- Note: PM uses "Sync", JAM uses "Publish" - may need action parameterization
- Estimated reduction: 4 files → 1 file

---

## 📝 Low Priority / Future Consolidations

### 6. Info Messages for Various Scenarios
**Current Files (3):**
- `26VerifyJobsMissingDataTipMessage.feature`
- `27VerifyInfoMessageForMissingDataProfiles.feature`
- `28VerifyInfoMessageForManualMappingProfiles.feature`

**Consolidation Strategy:**
- Parameterize by `<messageType>` (MissingDataTip/MissingDataInfo/ManualMapping)
- May have different validation logic - needs analysis

---

## 📊 Consolidation Summary

| Status | Feature Groups | Files Before | Files After | Reduction |
|--------|---------------|--------------|-------------|-----------|
| ✅ Done | Missing Data (29-32) | 16 | 5 | 11 files |
| ✅ Done | Clear Selection (42-45) | 16 | 5 | 11 files |
| ✅ Done | Search Selection (35,38) | 12 | 7 | 5 files |
| ✅ Done | Filter Selection (36,39) | 12 | 6 | 6 files |
| ✅ Done | Filters (11) | Multiple | 5 | ~10 files |
| ✅ Done | Filter Persistence (12a,12b) | 8 | 2 | 6 files |
| ✅ Done | Sort Persistence (18a,18b) | 8 | 2 | 6 files |
| ✅ Done | Unmapped Jobs (19,46) | 8 | 2 | 6 files |
| ✅ Done | Profile Details (05→15) | 4 | 0 | 4 files |
| ✅ Done | Single Job Publish (06,07,08) | 12 | 4 | 8 files |
| 🎯 Next | Sync/Publish (33,34,37,40) | 16 | 5 | 11 files |

**Total Files Reduced So Far:** ~73 files
**Total Potential Reduction:** ~85+ files

---

## 🔧 Consolidation Checklist

When consolidating, ensure:
- [ ] Create parameterized feature file with Scenario Outline
- [ ] Create unified Page Object with dynamic locators
- [ ] Create Step Definitions with parameterized methods
- [ ] Create Runner and CrossBrowser Runner
- [ ] Update PageObjectManager with new getter
- [ ] Update SuiteHooks for ThreadLocal cleanup
- [ ] Delete old individual files
- [ ] Test all scenarios pass
- [ ] Push changes to Git

---

## 📅 Change Log

| Date | Consolidation | Files Reduced | Contributor |
|------|--------------|---------------|-------------|
| Dec 2024 | Missing Data Validation (29-32) | 11 files | Naveen |
| Dec 2024 | Clear Profile Selection (42-45) | 11 files | Naveen |
| Dec 18, 2024 | Select All with Search (35, 35a, 38, 38a) | 5 files | Naveen |
| Dec 18, 2024 | Select All with Filters (36, 36a, 39, 39a) | 6 files | Naveen |
| Dec 19, 2024 | Filter Persistence (12a, 12b) | 6 files | Naveen |
| Dec 19, 2024 | Sorting Persistence (18a, 18b) | 6 files | Naveen |
| Dec 19, 2024 | Unmapped Jobs (19, 46) | 6 files | Naveen |
| Dec 19, 2024 | Profile Details Popup (05 → 15) | 4 files | Naveen |
| Dec 19, 2024 | Single Job Publish (06, 07, 08) | 8 files | Naveen |

---

*Last Updated: December 19, 2024*

