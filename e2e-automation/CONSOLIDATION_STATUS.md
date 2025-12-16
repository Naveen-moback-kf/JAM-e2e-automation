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
| 42ClearProfileSelectionwithHeaderCheckbox_PM.feature | 42ClearProfileSelectionFunctionality.feature |
| 43ClearProfileSelectionwithNoneButton_PM.feature | |
| 44ClearProfileSelectionwithHeaderCheckbox_JAM.feature | |
| 45ClearProfileSelectionwithNoneButton_JAM.feature | |

**Files Created:**
- `42ClearProfileSelectionFunctionality.feature` - Parameterized with `<screen>` (PM/JAM) and `<method>` (HeaderCheckbox/NoneButton)
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

### 3. Filters Functionality (Feature #11)
**Consolidated:** Earlier

| Before (multiple filter files) | After (1 consolidated file) |
|-------------------------------|----------------------------|
| Multiple individual filter tests | 11ValidateJobMappingFiltersFunctionality.feature |

---

## 🎯 High Priority - Recommended Next Consolidations

### 1. Select All with Search (PM + JAM) → Could become Feature #35
**Current Files (4):**
- `35ValidateSelectAllWithSearchFunctionality_PM.feature`
- `35aValidateLoadedProfilesSelectionWithSearch_PM.feature`
- `38ValidateSelectAllWithSearchFunctionality_JAM.feature`
- `38aValidateLoadedProfilesSelectionWithSearch_JAM.feature`

**Consolidation Strategy:**
- Parameterize by `<screen>` (PM/JAM)
- Combine "Select All" and "Loaded Profiles" scenarios
- Estimated reduction: 4 files → 1 file

---

### 2. Select All with Filters (PM + JAM) → Could become Feature #36
**Current Files (4):**
- `36ValidateSelectAllWithFiltersFunctionality_PM.feature`
- `36aValidateLoadedProfilesSelectionWithFilters_PM.feature`
- `39ValidateSelectAllWithFiltersFunctionality_JAM.feature`
- `39aValidateLoadedProfilesSelectionWithFilters_JAM.feature`

**Consolidation Strategy:**
- Parameterize by `<screen>` (PM/JAM)
- Combine "Select All" and "Loaded Profiles" scenarios
- Estimated reduction: 4 files → 1 file

---

### 3. Select and Sync/Publish (PM + JAM) → Could become Feature #33
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

## 📋 Medium Priority Consolidations

### 4. Publish Job from Different Screens → Could become Feature #06
**Current Files (4):**
- `06PublishJobProfile.feature`
- `07PublishJobFromComparisonScreen.feature`
- `08PublishJobFromDetailsPopup.feature`
- `09PublishSelectedProfiles.feature`

**Consolidation Strategy:**
- Parameterize by `<source>` (JobMapping/Comparison/DetailsPopup/Selected)
- Estimated reduction: 4 files → 1 file

---

### 5. Filter Persistence (Basic + Advanced) → Could become Feature #12
**Current Files (2):**
- `12aValidateBasicFilterPersistence.feature`
- `12bValidateAdvancedFilterPersistence.feature`

**Consolidation Strategy:**
- Parameterize by `<filterType>` (Basic/Advanced)
- Estimated reduction: 2 files → 1 file

---

### 6. Sorting Persistence (Basic + Advanced) → Could become Feature #18
**Current Files (2):**
- `18aValidateBasicSortingPersistence.feature`
- `18bValidateAdvancedSortingPersistence.feature`

**Consolidation Strategy:**
- Parameterize by `<sortType>` (Basic/Advanced)
- Estimated reduction: 2 files → 1 file

---

## 📝 Low Priority / Future Consolidations

### 7. Info Messages for Various Scenarios
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
| ✅ Done | Filters (11) | Multiple | 5 | ~10 files |
| 🎯 Next | Search Selection (35,38) | 16 | 5 | 11 files |
| 🎯 Next | Filter Selection (36,39) | 16 | 5 | 11 files |
| 🎯 Next | Sync/Publish (33,34,37,40) | 16 | 5 | 11 files |
| 📋 Medium | Publish Sources (06-09) | 16 | 5 | 11 files |
| 📋 Medium | Filter Persistence (12a,12b) | 8 | 5 | 3 files |
| 📋 Medium | Sort Persistence (18a,18b) | 8 | 5 | 3 files |

**Total Potential Reduction:** ~70+ files

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

---

*Last Updated: December 16, 2024*

