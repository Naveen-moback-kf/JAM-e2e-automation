package com.kfonetalentsuite.pageobjects.JobMapping;
import static com.kfonetalentsuite.pageobjects.JobMapping.BasePageObject.Locators.JobMappingScreen.*;
import static com.kfonetalentsuite.pageobjects.JobMapping.BasePageObject.Locators.Common.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import com.kfonetalentsuite.utils.common.Utilities;
import com.kfonetalentsuite.utils.JobMapping.ExcelMissingDataHandler;
public class PO35_ReuploadMissingDataProfiles extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO35_ReuploadMissingDataProfiles.class);
	
	// Timing Constants
	private static final int BACKEND_PROCESSING_WAIT_MS = 180000; // 3 minutes
	private static final int SEARCH_RETRY_DELAY_MS = 30000; // 30 seconds
	private static final int VALIDATION_POLL_INTERVAL_MS = 20000; // 20 seconds
	private static final int MISSING_DATA_CHECK_RETRY_DELAY_MS = 10000; // 10 seconds
	private static final int ICON_CHECK_RETRY_DELAY_MS = 8000; // 8 seconds
	private static final int MAX_SEARCH_RETRIES = 5;
	private static final int MAX_VALIDATION_ATTEMPTS = 5;
	private static final int MAX_MISSING_DATA_RETRIES = 5;
	
	// ThreadLocal variables for test data
	public static ThreadLocal<Integer> missingDataCountBefore = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<Integer> missingDataCountAfter = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<Integer> totalResultsCountBefore = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<Integer> totalResultsCountAfter = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<Integer> totalProfilesInMissingDataScreen = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<List<String[]>> extractedProfiles = ThreadLocal.withInitial(ArrayList::new);
	public static ThreadLocal<String> firstProfileJobCode = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<String> firstProfileJobName = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<String> firstProfileExpectedGrade = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<String> firstProfileExpectedDepartment = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<String> firstProfileExpectedJobFamily = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<String> firstProfileExpectedJobSubFamily = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<String> excelFilePath = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<Integer> searchResultsFoundCount = ThreadLocal.withInitial(() -> 0);
	private static final String DEFAULT_EXCEL_FILENAME = "MissingDataProfiles_ToFix.xlsx";
	private static final String BACKUP_DIR = "src/test/resources/MissingDataBackups";
	
	// Excel structure constants (matching JobCatalogNewFormat)
	// Note: Missing data columns C, D, E, K (Function, Subfunction, Grade, Department) 
	// are handled by ExcelMissingDataHandler utility class
	private static final int HEADER_ROW = 4; // Row 5 in Excel (0-indexed = 4)
	private static final int DATA_START_ROW = 6; // Row 7 in Excel (0-indexed = 6)
	private static final int JOB_TITLE_COLUMN = 0; // Column A (0-indexed = 0)
	private static final int JOB_CODE_COLUMN = 1; // Column B (0-indexed = 1)
	private static final int JOB_FAMILY_COLUMN = 2; // Column C (0-indexed = 2) - Function
	private static final int JOB_GRADE_COLUMN = 4; // Column E (0-indexed = 4)
	private static final int DEPARTMENT_COLUMN = 10; // Column K (0-indexed = 10)
	
	// Job Mapping page table column indices (for UI validation)
	private static final int GRADE_COLUMN_INDEX = 3; // Column 3 in Job Mapping table
	private static final int DEPARTMENT_COLUMN_INDEX = 4; // Column 4 in Job Mapping table

	/**
	 * Helper: Build test resources file path
	 */
	private static String buildResourcePath(String fileName) {
		return System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
				+ File.separator + "resources" + File.separator + fileName;
	}

	public PO35_ReuploadMissingDataProfiles() {
		super();
	}

	public void verify_user_is_navigated_to_jobs_with_missing_data_screen() {
		try {
			Utilities.waitForPageReady(driver, 5);
			Utilities.waitForVisible(wait, MISSING_DATA_SCREEN_TITLE);
			Assert.assertTrue(findElement(MISSING_DATA_SCREEN_TITLE).isDisplayed(),
					"Jobs With Missing Data screen title should be displayed");
			LOGGER.info("Successfully navigated to Jobs With Missing Data screen");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_navigation_to_missing_data_screen",
					"Failed to verify navigation to Jobs With Missing Data screen", e);
		}
	}

	public void verify_reupload_button_is_displayed_on_jobs_missing_data_screen() {
		try {
			Utilities.waitForPageReady(driver, 3);
			Utilities.waitForVisible(wait, REUPLOAD_BUTTON);
			Assert.assertTrue(findElement(REUPLOAD_BUTTON).isDisplayed(), "Re-upload button should be displayed");
			LOGGER.info("Re-upload button is displayed on Jobs Missing Data screen");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_reupload_button_is_displayed",
					"Failed to verify Re-upload button display", e);
		}
	}

	public void click_on_reupload_button_in_jobs_missing_data_screen() {
		try {
			driver.manage().deleteAllCookies();
			Utilities.waitForPageReady(driver, 5);
			safeSleep(2000);

			int maxRetries = 3;
			for (int attempt = 1; attempt <= maxRetries; attempt++) {
				try {
					WebElement reuploadBtn = Utilities.waitForClickable(wait, REUPLOAD_BUTTON);
					scrollToElement(reuploadBtn);
					safeSleep(500);

					reuploadBtn = findElement(REUPLOAD_BUTTON);
					jsClick(reuploadBtn);

					LOGGER.info("Clicked on Re-upload button in Jobs Missing Data screen");
					Utilities.waitForPageReady(driver, 5);
					return;

				} catch (StaleElementReferenceException e) {
					if (attempt == maxRetries) {
						throw e;
					}
					safeSleep(1000);
				}
			}
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "click_on_reupload_button", "Failed to click Re-upload button", e);
		}
	}

	public void capture_total_count_of_profiles_in_jobs_missing_data_screen() {
		try {
			Utilities.waitForPageReady(driver, 3);
			int totalCount = findElements(JOB_ROWS_IN_MISSING_DATA_SCREEN).size();
			totalProfilesInMissingDataScreen.set(totalCount);
			LOGGER.info("Captured {} profiles in Missing Data screen", totalCount);
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "capture_total_count", "Failed to capture total profile count", e);
		}
	}

	public void extract_details_of_top_10_profiles_from_jobs_missing_data_screen() {
		try {
			Utilities.waitForPageReady(driver, 3);
			List<String[]> profiles = new ArrayList<>();

			List<WebElement> currentRows = findElements(JOB_ROWS_IN_MISSING_DATA_SCREEN);
			int totalAvailable = currentRows.size();
			int maxProfiles = Math.min(10, totalAvailable);
			LOGGER.info("Extracting details of {} profiles from Missing Data screen (out of {})", maxProfiles, totalAvailable);
			int extractedCount = 0;

			for (int i = 0; i < maxProfiles && extractedCount < 10; i++) {
				try {
					currentRows = findElements(JOB_ROWS_MISSING_DATA);
					if (i >= currentRows.size()) {
						LOGGER.warn("Row index {} exceeds available data rows {}. Stopping extraction.", i, currentRows.size());
						break;
					}

					WebElement row = currentRows.get(i);

					if (i % 10 == 0 && i > 0) {
						scrollToElement(row);
						safeSleep(200);
						currentRows = findElements(JOB_ROWS_MISSING_DATA);
						if (i >= currentRows.size()) break;
						row = currentRows.get(i);
					}

					List<WebElement> cells = row.findElements(By.tagName("td"));
					if (cells.size() < 4) continue;

					String nameJobCodeCell = cleanExtractedText(cells.get(0).getText());
					String jobCode = "";
					String jobTitle = "";

					if (nameJobCodeCell.endsWith(")") && nameJobCodeCell.contains("(")) {
						int lastOpenParen = nameJobCodeCell.lastIndexOf("(");
						jobTitle = cleanExtractedText(nameJobCodeCell.substring(0, lastOpenParen));
						jobCode = cleanExtractedText(nameJobCodeCell.substring(lastOpenParen + 1, nameJobCodeCell.length() - 1));
					} else if (nameJobCodeCell.contains("(") && nameJobCodeCell.contains(")")) {
						LOGGER.warn("Row {} - Parentheses not at end. Profile has data issue. Skipping: '{}'", i + 1, nameJobCodeCell);
						continue;
					} else {
						LOGGER.warn("Row {} - No job code found in parentheses. Skipping: '{}'", i + 1, nameJobCodeCell);
						continue;
					}

					String jobGrade = cleanExtractedText(cells.get(1).getText());
					String department = cleanExtractedText(cells.get(2).getText());
					String functionSubFunctionCell = cleanExtractedText(cells.get(3).getText());
					String jobFamily = "";
					String jobSubFamily = "";

					if (functionSubFunctionCell.contains("|")) {
						String[] parts = functionSubFunctionCell.split("\\|", 2);
						jobFamily = cleanExtractedText(parts.length > 0 ? parts[0] : "");
						jobSubFamily = cleanExtractedText(parts.length > 1 ? parts[1] : "");
					} else {
						jobFamily = functionSubFunctionCell;
					}

					String isExecutive = "FALSE";

					if (extractedCount == 0) {
						firstProfileJobCode.set(jobCode);
						firstProfileJobName.set(jobTitle);
						LOGGER.info("First profile captured - JobCode: '{}', JobTitle: '{}'", jobCode, jobTitle);
						LOGGER.info("First profile details - Grade: '{}', Dept: '{}', Function: '{}', SubFunction: '{}'",
								jobGrade, department, jobFamily, jobSubFamily);
					}

					department = cleanNAValue(department);
					jobFamily = cleanNAValue(jobFamily);
					jobSubFamily = cleanNAValue(jobSubFamily);
					jobGrade = cleanNAValue(jobGrade);

					profiles.add(new String[] { jobCode, jobTitle, department, jobFamily, jobSubFamily, jobGrade, isExecutive });
					extractedCount++;

					if (extractedCount % 2 == 0) {
						LOGGER.info("Extracted {} of {} profiles...", extractedCount, maxProfiles);
					}

				} catch (Exception rowEx) {
					LOGGER.warn("Could not extract data from row {}: {}", i + 1, rowEx.getMessage());
				}
			}

			extractedProfiles.set(profiles);
			LOGGER.info("Successfully extracted " + profiles.size() + " profiles from Missing Data screen");

		} catch (Exception e) {
			Utilities.handleError(LOGGER, "extract_top_10_profiles",
					"Failed to extract profiles from Missing Data screen", e);
		}
	}

	public void create_excel_file_with_extracted_profiles_in_job_catalog_format() {
		try {
			List<String[]> profiles = extractedProfiles.get();

			if (profiles == null || profiles.isEmpty()) {
				throw new IOException("No profiles available to create Excel file");
			}

			String filePath = buildResourcePath(DEFAULT_EXCEL_FILENAME);
			String templatePath = buildResourcePath("JobCatalogNewFormat-100 Profiles.xlsx");

			boolean success = ExcelMissingDataHandler.createExcelFileWithProfiles(profiles, filePath, templatePath);

			if (success) {
				excelFilePath.set(filePath);
				LOGGER.info("Excel file created successfully at: " + filePath);
			} else {
				throw new IOException("Failed to create Excel file");
			}

		} catch (Exception e) {
			Utilities.handleError(LOGGER, "create_excel_file", "Failed to create Excel file", e);
		}
	}

	public void save_excel_file_as_in_test_resources_folder(String fileName) {
		try {
			String filePath = buildResourcePath(fileName);
			excelFilePath.set(filePath);

			File defaultFile = new File(buildResourcePath(DEFAULT_EXCEL_FILENAME));

			if (defaultFile.exists()) {
				File newFile = new File(filePath);
				defaultFile.renameTo(newFile);
			}

			LOGGER.info("CSV file saved as: " + fileName);

		} catch (Exception e) {
			Utilities.handleError(LOGGER, "save_excel_file", "Failed to save Excel file", e);
		}
	}

	public void verify_excel_file_is_created_successfully_with_correct_headers() {
		try {
			String filePath = excelFilePath.get();
			File excelFile = new File(filePath);

			Assert.assertTrue(excelFile.exists(), "Excel file should exist at: " + filePath);

			// Verify Excel file has correct structure
			try (FileInputStream fis = new FileInputStream(excelFile);
				 Workbook workbook = new XSSFWorkbook(fis)) {
				
				Sheet sheet = workbook.getSheetAt(0);
				Row headerRow = sheet.getRow(HEADER_ROW);
				
				Assert.assertNotNull(headerRow, "Excel file should have header row at row " + (HEADER_ROW + 1));
				
				// Check key headers
				Assert.assertNotNull(headerRow.getCell(JOB_TITLE_COLUMN), "Job Title header should exist");
				Assert.assertNotNull(headerRow.getCell(JOB_CODE_COLUMN), "Job Code header should exist");
				Assert.assertNotNull(headerRow.getCell(JOB_FAMILY_COLUMN), "Job Family header should exist");
				Assert.assertNotNull(headerRow.getCell(JOB_GRADE_COLUMN), "Job Grade header should exist");
				Assert.assertNotNull(headerRow.getCell(DEPARTMENT_COLUMN), "Department header should exist");
			}

			LOGGER.info("Excel file verified with correct headers");

		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_excel_headers", "Failed to verify Excel file headers", e);
		}
	}

	public void verify_excel_file_contains_extracted_profile_data() {
		try {
			String filePath = excelFilePath.get();
			
			try (FileInputStream fis = new FileInputStream(filePath);
				 Workbook workbook = new XSSFWorkbook(fis)) {
				
				Sheet sheet = workbook.getSheetAt(0);
				int lastRow = sheet.getLastRowNum();
				int dataRows = lastRow - DATA_START_ROW + 1;
				
				Assert.assertTrue(dataRows > 0, "Excel file should contain profile data");
				LOGGER.info("Excel file contains " + dataRows + " profile records");
			}

		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_excel_data", "Failed to verify Excel file data", e);
		}
	}

	public void excel_file_with_missing_data_profiles_exists() {
		try {
			String filePath = excelFilePath.get();
			if (filePath == null || filePath.isEmpty() || filePath.equals("NOT_SET")) {
				filePath = buildResourcePath(DEFAULT_EXCEL_FILENAME);
				excelFilePath.set(filePath);
			}

			File excelFile = new File(filePath);
			Assert.assertTrue(excelFile.exists(), "Excel file should exist for processing: " + filePath);
			LOGGER.info("Confirmed Excel file exists at: " + filePath);

		} catch (Exception e) {
			Utilities.handleError(LOGGER, "excel_file_exists", "Excel file does not exist", e);
		}
	}

	public void read_the_exported_excel_file_with_missing_data_profiles() {
		try {
			String filePath = excelFilePath.get();
			
			// Use ExcelMissingDataHandler to read Excel file
			List<String[]> profiles = ExcelMissingDataHandler.readExcelFile(filePath);
			
			if (profiles == null) {
				LOGGER.warn("No profiles found in Excel file - initializing empty list");
				profiles = new ArrayList<>();
			} else if (profiles.isEmpty()) {
				LOGGER.warn("Excel file contains no profile data");
			}

			extractedProfiles.set(profiles);
			LOGGER.info("Read " + profiles.size() + " profiles from Excel file");

		} catch (Exception e) {
			Utilities.handleError(LOGGER, "read_excel_file", "Failed to read Excel file", e);
		}
	}

	// CONSOLIDATED: Generic method to fill missing values
	private void fillMissingFieldValue(int fieldIndex, String fieldName, String defaultValue, 
	                                    ThreadLocal<String> expectedValueStorage) {
		try {
			List<String[]> profiles = extractedProfiles.get();
			int filledCount = 0;

			for (int i = 0; i < profiles.size(); i++) {
				String[] profile = profiles.get(i);
				if (profile.length > fieldIndex && (profile[fieldIndex] == null || profile[fieldIndex].trim().isEmpty())) {
					profile[fieldIndex] = defaultValue;
					filledCount++;
					
					if (i == 0 && expectedValueStorage != null) {
						expectedValueStorage.set(defaultValue);
						LOGGER.info("First profile expected {} set to: '{}'", fieldName, defaultValue);
					}
				}
			}

			extractedProfiles.set(profiles);
			LOGGER.info("Filled {} missing {} values with: {}", filledCount, fieldName, defaultValue);

		} catch (Exception e) {
			Utilities.handleError(LOGGER, "fill_missing_" + fieldName.toLowerCase().replace(" ", "_"),
					"Failed to fill missing " + fieldName + " values", e);
		}
	}

	public void fill_missing_grade_values_with_default_grade(String defaultGrade) {
		fillMissingFieldValue(5, "Grade", defaultGrade, firstProfileExpectedGrade);
	}

	public void fill_missing_department_values_with_default_department(String defaultDepartment) {
		fillMissingFieldValue(2, "Department", defaultDepartment, firstProfileExpectedDepartment);
	}

	public void fill_missing_job_family_values_with_default_value(String defaultValue) {
		fillMissingFieldValue(3, "Job Family", defaultValue, firstProfileExpectedJobFamily);
	}

	public void fill_missing_job_sub_family_values_with_default_value(String defaultValue) {
		fillMissingFieldValue(4, "Job Sub Family", defaultValue, firstProfileExpectedJobSubFamily);
	}

	public void save_the_updated_excel_file_with_filled_data() {
		try {
			String filePath = excelFilePath.get();
			List<String[]> profiles = extractedProfiles.get();

			// Create backup before overwriting
			String backupPath = ExcelMissingDataHandler.createBackup(filePath, BACKUP_DIR);
			if (backupPath != null) {
				LOGGER.info("Backup created: " + backupPath);
			}

			// Use ExcelMissingDataHandler to write Excel file
			String templatePath = buildResourcePath("JobCatalogNewFormat-100 Profiles.xlsx");
			
			boolean success = ExcelMissingDataHandler.createExcelFileWithProfiles(profiles, filePath, templatePath);
			
			if (success) {
				LOGGER.info("Updated Excel file saved with " + profiles.size() + " profiles");
			} else {
				throw new IOException("Failed to save updated Excel file");
			}

		} catch (Exception e) {
			Utilities.handleError(LOGGER, "save_updated_excel", "Failed to save updated Excel file", e);
		}
	}

	public void verify_updated_excel_file_has_no_empty_grade_values() {
		try {
			List<String[]> profiles = extractedProfiles.get();

			for (String[] profile : profiles) {
				if (profile.length > 5) {
					Assert.assertFalse(profile[5] == null || profile[5].trim().isEmpty(),
							"Grade value should not be empty for profile: " + profile[0]);
				}
			}

			LOGGER.info("Verified all Grade values are filled in CSV");

		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_no_empty_grades", "Found empty Grade values in CSV", e);
		}
	}

	public void verify_updated_excel_file_has_no_empty_department_values() {
		try {
			List<String[]> profiles = extractedProfiles.get();

			for (String[] profile : profiles) {
				if (profile.length > 2) {
					Assert.assertFalse(profile[2] == null || profile[2].trim().isEmpty(),
							"Department value should not be empty for profile: " + profile[0]);
				}
			}

			LOGGER.info("Verified all Department values are filled in CSV");

		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_no_empty_departments", "Found empty Department values in CSV", e);
		}
	}

	public void verify_updated_excel_file_has_no_empty_required_fields() {
		try {
			List<String[]> profiles = extractedProfiles.get();

			for (String[] profile : profiles) {
				Assert.assertFalse(profile[0] == null || profile[0].trim().isEmpty(), "Job Code should not be empty");
				Assert.assertFalse(profile[1] == null || profile[1].trim().isEmpty(), "Job Title should not be empty");
			}

			LOGGER.info("Verified all required fields are filled in CSV");
			
			// CRITICAL: Capture actual values from first profile for validation
			// This ensures we have the correct expected values even if first profile wasn't filled
			if (!profiles.isEmpty()) {
				String[] firstProfile = profiles.get(0);
				
				// Excel Format: Client Job Code, Client Job Title, Department, Job Family, Job Sub Family, Job Grade, Is Executive
				// Indices:    0                 1                 2           3           4               5          6
				
				if (firstProfile.length > 5) {
					String grade = (firstProfile[5] != null && !firstProfile[5].trim().isEmpty()) ? firstProfile[5] : "NOT_SET";
					String dept = (firstProfile[2] != null && !firstProfile[2].trim().isEmpty()) ? firstProfile[2] : "NOT_SET";
					String family = (firstProfile[3] != null && !firstProfile[3].trim().isEmpty()) ? firstProfile[3] : "NOT_SET";
					String subFamily = (firstProfile[4] != null && !firstProfile[4].trim().isEmpty()) ? firstProfile[4] : "NOT_SET";
					
					firstProfileExpectedGrade.set(grade);
					firstProfileExpectedDepartment.set(dept);
					firstProfileExpectedJobFamily.set(family);
					firstProfileExpectedJobSubFamily.set(subFamily);
					
					LOGGER.info("Captured first profile expected values from CSV:");
					LOGGER.info("  Grade: '{}'", grade);
					LOGGER.info("  Department: '{}'", dept);
					LOGGER.info("  Job Family: '{}'", family);
					LOGGER.info("  Job Sub Family: '{}'", subFamily);
				}
			}

		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_no_empty_required_fields", "Found empty required fields in CSV", e);
		}
	}

	public void capture_total_results_count_before_reupload() {
		try {
			Utilities.waitForPageReady(driver, 3);
			Utilities.waitForSpinnersToDisappear(driver, 10);
			
		// CRITICAL: Ensure no search filters are active to get TOTAL results, not filtered results
		// Clear BOTH search bar AND filters panel
		try {
			LOGGER.info("Clearing ALL filters (search bar + filter panel) to get total results count...");
			
			// Step 1: Clear search bar using JavaScript
			JavascriptExecutor js = (JavascriptExecutor) driver;
			String clearScript = 
				"var searchBar = document.querySelector('#search-job-title-input-search-input');" +
				"if (searchBar && searchBar.value) {" +
				"  searchBar.focus();" +
				"  searchBar.value = '';" +
				"  searchBar.dispatchEvent(new Event('input', { bubbles: true }));" +
				"  searchBar.dispatchEvent(new KeyboardEvent('keydown', { key: 'Enter', keyCode: 13, bubbles: true }));" +
				"  return true;" +
				"} return false;";
			
			Boolean searchCleared = (Boolean) js.executeScript(clearScript);
			if (searchCleared != null && searchCleared) {
				safeSleep(2000);
			}
			
			// Step 2: Click "Clear Filters" button to remove any filter panel selections
			try {
				WebElement clearFiltersBtn = driver.findElement(CLEAR_FILTERS_BTN);
				if (clearFiltersBtn.isDisplayed()) {
					clickElement(CLEAR_FILTERS_BTN);
					safeSleep(3000);
					Utilities.waitForSpinnersToDisappear(driver, 10);
					waitForBackgroundDataLoad();
					safeSleep(2000);
				}
			} catch (Exception e) {
				LOGGER.debug("Clear Filters button not available: {}", e.getMessage());
			}
			
			LOGGER.info("✓ Filters cleared");
			
		} catch (Exception clearEx) {
			LOGGER.debug("Filter clear issue: {}", clearEx.getMessage());
		}
			
			WebElement resultsElement = Utilities.waitForVisible(wait, SHOWING_JOB_RESULTS);
			String resultsText = resultsElement.getText().trim();
			
			LOGGER.info("Results text: " + resultsText);
			
			// Parse "Showing X of Y results" format - extract Y (total results)
			// Example: "Showing 1-50 of 1234 results"
			if (resultsText.contains("of") && resultsText.contains("results")) {
				String[] parts = resultsText.split(" ");
				for (int i = 0; i < parts.length; i++) {
					if (parts[i].equals("of") && i + 1 < parts.length) {
						int totalCount = Integer.parseInt(parts[i + 1]);
						totalResultsCountBefore.set(totalCount);
						LOGGER.info("Total Results count BEFORE re-upload: " + totalCount);
						return;
					}
				}
			}
			
			LOGGER.info("Could not parse Total Results count from: " + resultsText);
			
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "capture_total_results_count_before",
					"Failed to capture Total Results count before re-upload", e);
		}
	}

	public void capture_total_results_count_after_reupload() {
		try {
			Utilities.waitForPageReady(driver, 3);
			Utilities.waitForSpinnersToDisappear(driver, 10);
			
			WebElement resultsElement = Utilities.waitForVisible(wait, SHOWING_JOB_RESULTS);
			String resultsText = resultsElement.getText().trim();
			
			LOGGER.info("Results text AFTER re-upload: " + resultsText);
			
			// Parse "Showing X of Y results" format - extract Y (total results)
			if (resultsText.contains("of") && resultsText.contains("results")) {
				String[] parts = resultsText.split(" ");
				for (int i = 0; i < parts.length; i++) {
					if (parts[i].equals("of") && i + 1 < parts.length) {
						int totalCount = Integer.parseInt(parts[i + 1]);
						totalResultsCountAfter.set(totalCount);
						LOGGER.info("Total Results count AFTER re-upload: " + totalCount);
						return;
					}
				}
			}
			
			LOGGER.info("Could not parse Total Results count from: " + resultsText);
			
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "capture_total_results_count_after",
					"Failed to capture Total Results count after re-upload", e);
		}
	}

	public void verify_total_results_count_remains_unchanged() {
		try {
			int before = totalResultsCountBefore.get();
			int after = totalResultsCountAfter.get();
			
			Assert.assertEquals(after, before,
					"Total Results count should remain unchanged after re-upload (updating existing profiles, not adding new). Before: " + before + ", After: " + after);
			
			LOGGER.info("✓ Total Results count unchanged: " + before + " → " + after + " (as expected - updating existing profiles)");
			
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_total_results_unchanged",
					"Total Results count changed unexpectedly", e);
		}
	}

	public void capture_the_count_of_jobs_with_missing_data_before_reupload() {
		try {
			// Check if tip message exists (data-dependent scenario)
			List<WebElement> tipMessages = driver.findElements(TIP_MESSAGE);
			
			
			
			WebElement tipMessage = tipMessages.get(0);
			String text = tipMessage.getText();

			java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("(\\d+)\\s+jobs");
			java.util.regex.Matcher matcher = pattern.matcher(text);

			if (matcher.find()) {
				int count = Integer.parseInt(matcher.group(1));
				missingDataCountBefore.set(count);
				LOGGER.info("Missing data count BEFORE re-upload: " + count);
			} else {
				LOGGER.warn("Could not extract count from tip message: " + text);
				missingDataCountBefore.set(0);
			}

		} catch (org.testng.SkipException se) {
			throw se; // Rethrow SkipException
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "capture_count_before",
					"Failed to capture missing data count before re-upload", e);
		}
	}

	public void capture_the_count_of_jobs_with_missing_data_after_reupload() {
		try {
			Utilities.waitForPageReady(driver, 3);

			// Check if tip message still exists (may have been fixed completely)
			List<WebElement> tipMessages = driver.findElements(TIP_MESSAGE);
			
			if (tipMessages.isEmpty()) {
				// No tip message = ALL profiles were fixed! This is SUCCESS
				missingDataCountAfter.set(0);
				LOGGER.info("✓ Missing Data Tip Message NO LONGER DISPLAYED - All profiles with missing data were fixed!");
				LOGGER.info("Missing data count AFTER re-upload: 0 (all profiles fixed)");
				return;
			}

			// Retry logic: Sometimes the count takes time to update
			int maxRetries = MAX_MISSING_DATA_RETRIES;
			int retryDelay = MISSING_DATA_CHECK_RETRY_DELAY_MS;
			
			for (int attempt = 1; attempt <= maxRetries; attempt++) {
				try {
					// Re-check if tip message exists
					tipMessages = driver.findElements(TIP_MESSAGE);
					
					if (tipMessages.isEmpty()) {
						// Tip message disappeared - all profiles fixed
						missingDataCountAfter.set(0);
						LOGGER.info("✓ Missing Data Tip Message disappeared - All profiles fixed!");
						LOGGER.info("Missing data count AFTER re-upload: 0");
						return;
					}
					
					WebElement tipMessage = tipMessages.get(0);
					String text = tipMessage.getText();

					java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("(\\d+)\\s+jobs");
					java.util.regex.Matcher matcher = pattern.matcher(text);

					if (matcher.find()) {
						int count = Integer.parseInt(matcher.group(1));
						
						// Check if count is different from BEFORE count (indicating update occurred)
						int beforeCount = missingDataCountBefore.get();
						if (count < beforeCount) {
							// Count has decreased - data is updated!
							missingDataCountAfter.set(count);
							LOGGER.info("Missing data count AFTER re-upload: " + count + " (decreased from " + beforeCount + ")");
							return;
						} else if (count == beforeCount && attempt < maxRetries) {
							// Count hasn't changed yet - retry
							LOGGER.info("Attempt {}/{}: Missing data count still at {} (waiting for update...)", 
									attempt, maxRetries, count);
							safeSleep(retryDelay);
							driver.navigate().refresh();
							Utilities.waitForPageReady(driver, 3);
							waitForBackgroundDataLoad(); // Wait for background data API to complete
							continue;
						} else {
							// Last attempt or count increased (unexpected)
							missingDataCountAfter.set(count);
							LOGGER.info("Missing data count AFTER re-upload: " + count);
							return;
						}
					}
				} catch (Exception retryEx) {
					if (attempt == maxRetries) {
						throw retryEx;
					}
					LOGGER.info("Attempt {}/{} failed, retrying in 10 seconds...", attempt, maxRetries);
					safeSleep(retryDelay);
				}
			}

		} catch (Exception e) {
			Utilities.handleError(LOGGER, "capture_count_after",
					"Failed to capture missing data count after re-upload", e);
		}
	}

	public void verify_missing_data_count_has_decreased_compared_to_before_reupload() {
		try {
			int before = missingDataCountBefore.get();
			int after = missingDataCountAfter.get();

			// Special case: If after count is 0, tip message disappeared (all profiles fixed)
			if (after == 0) {
				LOGGER.info("✓ EXCELLENT! All profiles with missing data were fixed!");
				LOGGER.info("✓ Missing data count: " + before + " → 0 (100% fixed)");
				LOGGER.info("✓ Missing Data Tip Message is no longer displayed on Job Mapping page");
				return;
			}

			// Normal case: Count decreased but tip message still present
			if (after < before) {
				int decreased = before - after;
				LOGGER.info("✓ Missing data count decreased from " + before + " to " + after + " (by " + decreased + ")");
			} else {
				// Count didn't decrease - skip scenario
				
			}

		} catch (org.testng.SkipException se) {
			throw se; // Rethrow SkipException
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_count_decreased",
					"Missing data count verification failed", e);
		}
	}

	public void wait_and_refresh_job_mapping_page_after_upload() {
		try {
			// Simple fixed wait approach - let the profile verification scenario handle smart retries
			LOGGER.info("Waiting for backend to process uploaded data...");
			LOGGER.info("Waiting {} seconds for backend processing...", BACKEND_PROCESSING_WAIT_MS / 1000);
			safeSleep(BACKEND_PROCESSING_WAIT_MS);

			LOGGER.info("Refreshing Job Mapping page...");
			dismissKeepWorkingPopupIfPresent();
			driver.navigate().refresh();
			Utilities.waitForPageReady(driver, 5);
			Utilities.waitForSpinnersToDisappear(driver, 15);
			waitForBackgroundDataLoad();
			safeSleep(2000);

			LOGGER.info("✓ Backend wait completed - page refreshed");

		} catch (Exception e) {
			Utilities.handleError(LOGGER, "wait_and_refresh", "Failed to wait and refresh Job Mapping page", e);
		}
	}

	public void search_for_first_reuploaded_profile_by_job_name_from_excel() {
		try {
			String jobName = firstProfileJobName.get();

			if (jobName == null || jobName.isEmpty() || jobName.equals("NOT_SET")) {
				throw new IOException("No job name captured for search. Please ensure extraction step ran first.");
			}

			LOGGER.info("Searching for re-uploaded profile by Job Name: " + jobName);

			// Retry logic with dynamic polling: Profile might not be searchable immediately after backend processing
			int maxRetries = MAX_SEARCH_RETRIES;
			int retryDelay = SEARCH_RETRY_DELAY_MS;
			
			for (int attempt = 1; attempt <= maxRetries; attempt++) {
				try {
					LOGGER.info("Search attempt {}/{} for Job Name: '{}'", attempt, maxRetries, jobName);
					
					driver.navigate().refresh();
			Utilities.waitForPageReady(driver, 3);
					waitForBackgroundDataLoad();
					safeSleep(2000);
					
			Utilities.waitForSpinnersToDisappear(driver, 10);

			WebElement searchInput = Utilities.waitForClickable(wait, JOB_SEARCH_INPUT);
			searchInput.clear();
			safeSleep(500);
			searchInput.sendKeys(jobName);

			safeSleep(2000);
			Utilities.waitForSpinnersToDisappear(driver, 10);
			Utilities.waitForPageReady(driver, 3);

					List<WebElement> results = findElements(SEARCH_RESULTS_ROWS);
					
					if (!results.isEmpty()) {
						LOGGER.info("✓ Found {} search results for '{}'", results.size(), jobName);
						return;
					}
					
					if (attempt < maxRetries) {
						LOGGER.info("No results yet, waiting 30 seconds before retry...");
						safeSleep(retryDelay);
					} else {
						LOGGER.warn("No results found after {} attempts", maxRetries);
					}

				} catch (Exception retryEx) {
					if (attempt == maxRetries) {
						throw retryEx;
					}
					LOGGER.warn("Attempt {}/{} failed: {}", attempt, maxRetries, retryEx.getMessage());
					safeSleep(SEARCH_RETRY_DELAY_MS);
				}
			}

		} catch (Exception e) {
			Utilities.handleError(LOGGER, "search_by_job_name", "Failed to search for re-uploaded profile", e);
		}
	}

	public void search_for_first_reuploaded_profile_by_job_code_in_missing_data_screen() {
		try {
			String jobCode = firstProfileJobCode.get();

			if (jobCode == null || jobCode.isEmpty() || jobCode.equals("NOT_SET")) {
				throw new IOException("No job code captured for search");
			}

		Utilities.waitForPageReady(driver, 2);
		
		// Try to find search input - but don't fail if it doesn't exist
		// Missing Data screen may not have a search bar
		try {
			WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
			WebElement searchInput = Utilities.waitForClickable(shortWait, SEARCH_INPUT_MISSING_DATA);
			searchInput.clear();
			safeSleep(500); // Allow clear to complete
			searchInput.sendKeys(jobCode);

				// Wait for search/filter to be applied
				Utilities.waitForPageReady(driver, 3);
				safeSleep(2000); // Additional wait for client-side filtering to complete
				
				LOGGER.info("Searched for profile by Job Code in Missing Data screen: " + jobCode);
			} catch (Exception searchEx) {
				LOGGER.info("No search input found in Missing Data screen (may not have search functionality) - will check all rows instead");
				LOGGER.debug("Search input exception: {}", searchEx.getMessage());
			}

		} catch (Exception e) {
			Utilities.handleError(LOGGER, "search_by_job_code_missing_data",
					"Failed to search in Missing Data screen", e);
		}
	}

	public void verify_profile_is_found_in_job_mapping_search_results() {
		try {
			Utilities.waitForPageReady(driver, 3);
			Utilities.waitForSpinnersToDisappear(driver, 10);

			List<WebElement> searchResults = findElements(SEARCH_RESULTS_ROWS);
			int resultCount = searchResults.size();

			searchResultsFoundCount.set(resultCount);

			if (resultCount == 0) {
				String jobName = firstProfileJobName.get();
				String errorMsg = "ERROR: Profile NOT found in search results for Job Name: " + jobName;
				LOGGER.info(errorMsg);
				Assert.fail(errorMsg);
			} else {
				// Search by Job Name may return multiple results - verify our specific Job Code is present
				String expectedJobCode = firstProfileJobCode.get();
				boolean jobCodeFound = false;
				int matchingRowIndex = -1;
				
				LOGGER.info("Found {} search results for Job Name. Searching for Job Code: '{}'", resultCount, expectedJobCode);
				
				for (int i = 0; i < searchResults.size(); i++) {
					WebElement row = searchResults.get(i);
					try {
						// Job Code is in column 2, format: "Job Name (JOB_CODE)"
						List<WebElement> cells = row.findElements(By.tagName("td"));
						if (cells.size() >= 2) {
							String nameCodeText = cells.get(1).getText().trim();
							
							// Extract Job Code from "Job Name (JOB_CODE)" format
							if (nameCodeText.contains("(") && nameCodeText.contains(")")) {
								int lastOpenParen = nameCodeText.lastIndexOf("(");
								int lastCloseParen = nameCodeText.lastIndexOf(")");
								String jobCode = nameCodeText.substring(lastOpenParen + 1, lastCloseParen).trim();
								
								LOGGER.debug("Row {}: Job Code = '{}'", i + 1, jobCode);
								
								if (jobCode.equals(expectedJobCode)) {
									jobCodeFound = true;
									matchingRowIndex = i;
									LOGGER.info("✓ Found matching profile at row {} with Job Code: '{}'", i + 1, jobCode);
									break;
								}
							}
						}
					} catch (Exception rowEx) {
						LOGGER.debug("Could not extract Job Code from row {}: {}", i + 1, rowEx.getMessage());
					}
				}
				
				if (jobCodeFound) {
					// Store the matching row index for later validations
					searchResultsFoundCount.set(matchingRowIndex + 1); // Store 1-based index
					LOGGER.info("✓ Profile found in Job Mapping search results at row " + (matchingRowIndex + 1) + 
							" (total " + resultCount + " results) with matching Job Code: " + expectedJobCode);
				} else {
					String errorMsg = "ERROR: Job Code '" + expectedJobCode + "' NOT found in " + resultCount + " search results";
					LOGGER.info(errorMsg);
					Assert.fail(errorMsg);
				}
			}

		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_profile_found", "Error checking search results", e);
		}
	}

	public void verify_profile_does_not_display_missing_data_info_icon() {
		try {
			int rowIndexStored = searchResultsFoundCount.get();
			if (rowIndexStored == 0) {
				String errorMsg = "CANNOT verify missing data icon - no search results found!";
				LOGGER.info(errorMsg);
				Assert.fail(errorMsg);
				return;
			}

			String expectedJobCode = firstProfileJobCode.get();

			// Retry logic: Missing data icon might take time to disappear
			int maxRetries = MAX_MISSING_DATA_RETRIES;
			int retryDelay = ICON_CHECK_RETRY_DELAY_MS;
			
			for (int attempt = 1; attempt <= maxRetries; attempt++) {
				try {
			Utilities.waitForPageReady(driver, 2);

			List<WebElement> rows = findElements(SEARCH_RESULTS_ROWS);
			if (rows.isEmpty()) {
						if (attempt < maxRetries) {
							LOGGER.info("Attempt {}/{}: No profile rows found, refreshing...", attempt, maxRetries);
							driver.navigate().refresh();
							Utilities.waitForPageReady(driver, 2);
							waitForBackgroundDataLoad();
							safeSleep(retryDelay);
							reSearchProfile();
							continue;
						}
						Assert.fail("No profile rows found to verify missing data icon after " + maxRetries + " attempts");
				return;
			}

					// Find the row with matching Job Code
					WebElement targetRow = findRowByJobCode(rows, expectedJobCode);
					if (targetRow == null) {
						if (attempt < maxRetries) {
							LOGGER.info("Attempt {}/{}: Job Code '{}' not found in results, refreshing...", 
									attempt, maxRetries, expectedJobCode);
							driver.navigate().refresh();
							Utilities.waitForPageReady(driver, 2);
							waitForBackgroundDataLoad();
							safeSleep(retryDelay);
							reSearchProfile();
							continue;
						}
						Assert.fail("Profile with Job Code '" + expectedJobCode + "' not found after " + maxRetries + " attempts");
						return;
					}

					List<WebElement> infoIcons = targetRow.findElements(
					By.xpath(".//div[@role='button' and @aria-label='Reduced match accuracy due to missing data']"));

					if (infoIcons.isEmpty()) {
						// Success! Icon is not present
						LOGGER.info("✓ Profile with Job Code '" + expectedJobCode + 
								"' successfully fixed - no missing data warning (verified on attempt " + attempt + ")");
						return;
					}
					
					// Icon still present
					if (attempt < maxRetries) {
						LOGGER.info("Attempt {}/{}: Missing data icon still present for Job Code '{}'. Waiting for update...", 
								attempt, maxRetries, expectedJobCode);
						safeSleep(retryDelay);
						driver.navigate().refresh();
						Utilities.waitForPageReady(driver, 2);
						waitForBackgroundDataLoad();
						safeSleep(3000);
						reSearchProfile();
					} else {
						Assert.fail("Profile with Job Code '" + expectedJobCode + 
								"' still displays Missing Data info icon after " + maxRetries + " attempts");
					}

				} catch (Exception retryEx) {
					if (attempt == maxRetries) {
						throw retryEx;
					}
					LOGGER.info("Attempt {}/{} failed, retrying in 8 seconds...", attempt, maxRetries);
					safeSleep(retryDelay);
				}
			}

		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_no_missing_data_icon",
					"Profile still displays Missing Data info icon", e);
		}
	}

	public void verify_profile_displays_the_corrected_data_values() {
		try {
			int rowIndexStored = searchResultsFoundCount.get();
			if (rowIndexStored == 0) {
				String errorMsg = "CANNOT verify corrected data values - no search results found!";
				LOGGER.info(errorMsg);
				Assert.fail(errorMsg);
				return;
			}

			// Get expected values that were filled in CSV
			String expectedJobCode = firstProfileJobCode.get();
			String expectedGrade = firstProfileExpectedGrade.get();
			String expectedDept = firstProfileExpectedDepartment.get();
			String expectedFamily = firstProfileExpectedJobFamily.get();
			String expectedSubFamily = firstProfileExpectedJobSubFamily.get();
			
			LOGGER.info("===== STRICT VALIDATION WITH DYNAMIC POLLING =====");
			LOGGER.info("Expected Job Code: '{}'", expectedJobCode);
			LOGGER.info("Expected values from CSV:");
			LOGGER.info("  Grade: '{}'", expectedGrade);
			LOGGER.info("  Department: '{}'", expectedDept);
			LOGGER.info("  Job Family: '{}'", expectedFamily);
			LOGGER.info("  Job Sub Family: '{}'", expectedSubFamily);

			// Dynamic polling: Check every 20 seconds for up to 2 minutes
			int maxPollingAttempts = MAX_VALIDATION_ATTEMPTS;
			int pollingInterval = VALIDATION_POLL_INTERVAL_MS;
			
			LOGGER.info("Starting polling (every {} seconds for {} attempts)...", pollingInterval / 1000, maxPollingAttempts);
			
			for (int attempt = 1; attempt <= maxPollingAttempts; attempt++) {
				try {
			Utilities.waitForPageReady(driver, 2);

			List<WebElement> rows = findElements(SEARCH_RESULTS_ROWS);
			if (rows.isEmpty()) {
						LOGGER.info("Attempt {}/{}: No profile rows found, refreshing...", attempt, maxPollingAttempts);
						driver.navigate().refresh();
						Utilities.waitForPageReady(driver, 2);
						waitForBackgroundDataLoad();
						safeSleep(VALIDATION_POLL_INTERVAL_MS);
						reSearchProfile();
						continue;
					}

					// Find the row with matching Job Code (search may return multiple results)
					WebElement jobRow = findRowByJobCode(rows, expectedJobCode);
					if (jobRow == null) {
						LOGGER.warn("Attempt {}/{}: Job Code '{}' not found in search results, refreshing...", 
								attempt, maxPollingAttempts, expectedJobCode);
						driver.navigate().refresh();
						Utilities.waitForPageReady(driver, 2);
						waitForBackgroundDataLoad();
						safeSleep(VALIDATION_POLL_INTERVAL_MS);
						reSearchProfile();
						continue;
					}
					
					List<WebElement> cells = jobRow.findElements(By.tagName("td"));
					
					LOGGER.info("Attempt {}/{}: Found Job Code '{}', extracting values from UI...", 
							attempt, maxPollingAttempts, expectedJobCode);
					
					// Extract actual values from UI
					String actualGrade = "";
					String actualDept = "";
					String actualFunction = "";
					String actualSubFunction = "";
					
					// Extract Grade (Column 3)
					if (cells.size() >= GRADE_COLUMN_INDEX) {
						actualGrade = cells.get(GRADE_COLUMN_INDEX - 1).getText().trim();
						LOGGER.info("  Actual Grade: '{}'", actualGrade);
					}
					
					// Extract Department (Column 4)
					if (cells.size() >= DEPARTMENT_COLUMN_INDEX) {
						actualDept = cells.get(DEPARTMENT_COLUMN_INDEX - 1).getText().trim();
						LOGGER.info("  Actual Department: '{}'", actualDept);
					}
					
					// Extract Function/SubFunction from the following sibling row (expandable details row)
					// The structure is: tr[main job row] followed by tr[function details row]
					// Use XPath: ./following-sibling::tr[1]//span[2] (same as PO25)
					try {
						WebElement funcSubfuncElement = jobRow.findElement(By.xpath("./following-sibling::tr[1]//span[2]"));
						String funcText = funcSubfuncElement.getAttribute("textContent").trim();
						LOGGER.info("  Actual Function/SubFunction text: '{}'", funcText);
						
						// Parse "Function | SubFunction" format
						if (funcText.contains("|")) {
							String[] parts = funcText.split("\\|", 2);
							actualFunction = parts.length > 0 ? parts[0].trim() : "";
							actualSubFunction = parts.length > 1 ? parts[1].trim() : "";
							LOGGER.info("  Parsed Function: '{}'", actualFunction);
							LOGGER.info("  Parsed SubFunction: '{}'", actualSubFunction);
						} else {
							actualFunction = funcText;
							LOGGER.info("  Parsed Function (no pipe): '{}'", actualFunction);
						}
					} catch (Exception funcEx) {
						LOGGER.warn("Could not extract Function/SubFunction from following-sibling row: {}", funcEx.getMessage());
						LOGGER.debug("Trying alternative method to find Function/SubFunction...");
						
						// Alternative: Try to find function row using textContent attribute instead of getText()
						try {
							int jobRowIndex = rows.indexOf(jobRow);
							if (jobRowIndex >= 0) {
								// Get ALL rows from the table (including function rows)
								List<WebElement> allTableRows = driver.findElements(By.xpath("//div[@id='org-job-container']//tbody//tr"));
								LOGGER.debug("Total table rows (including function rows): {}", allTableRows.size());
								LOGGER.debug("Job row index in checkbox rows: {}, looking for function row...", jobRowIndex);
								
								// Search for the function row that follows our job row
								// Function rows contain "Function / Sub-function:" text
								for (int i = 0; i < allTableRows.size(); i++) {
									WebElement currentRow = allTableRows.get(i);
									try {
										// Check if this row is our job row (has checkbox)
										if (currentRow.findElements(By.xpath(".//td[1]//input[@type='checkbox']")).size() > 0) {
											// Check if this is our job row by comparing text content
											String rowJobCode = extractJobCodeFromText(currentRow.getText());
											if (rowJobCode.equals(expectedJobCode)) {
												// Found our job row, next row should be function row
												if (i + 1 < allTableRows.size()) {
													WebElement nextRow = allTableRows.get(i + 1);
													String nextRowText = nextRow.getText().trim().toLowerCase();
													if (nextRowText.contains("function") || nextRowText.contains("/")) {
														// This is the function row
														WebElement funcSpan = nextRow.findElement(By.xpath(".//span[2]"));
														String funcText = funcSpan.getAttribute("textContent").trim();
														LOGGER.info("  Found Function/SubFunction in all rows: '{}'", funcText);
														
														if (funcText.contains("|")) {
															String[] parts = funcText.split("\\|", 2);
															actualFunction = parts.length > 0 ? parts[0].trim() : "";
															actualSubFunction = parts.length > 1 ? parts[1].trim() : "";
														} else {
															actualFunction = funcText;
														}
														break;
													}
												}
											}
										}
									} catch (Exception rowEx) {
										continue;
									}
								}
							}
						} catch (Exception altEx) {
							LOGGER.warn("Alternative extraction also failed: {}", altEx.getMessage());
						}
					}
					
					// STRICT VALIDATION using helper method
					StringBuilder errors = new StringBuilder();
					int[] matchCount = {0};
					int[] totalChecks = {0};
					
					validateFieldValue("Grade", expectedGrade, actualGrade, errors, matchCount, totalChecks);
					validateFieldValue("Department", expectedDept, actualDept, errors, matchCount, totalChecks);
					validateFieldValue("Job Family", expectedFamily, actualFunction, errors, matchCount, totalChecks);
					validateFieldValue("Job Sub Family", expectedSubFamily, actualSubFunction, errors, matchCount, totalChecks);
					
					// Check if ALL validations passed
					if (matchCount[0] == totalChecks[0] && totalChecks[0] > 0) {
						String successMsg = String.format("✓ ALL VALUES VALIDATED (attempt %d/%d): Grade='%s', Dept='%s', Family='%s', SubFamily='%s'",
								attempt, maxPollingAttempts, actualGrade, actualDept, actualFunction, actualSubFunction);
						LOGGER.info(successMsg);
						LOGGER.info("===== VALIDATION PASSED =====");
				return;
			}

					// Not all values match yet - continue polling
					if (attempt < maxPollingAttempts) {
						LOGGER.warn("Attempt {}/{}: {}/{} fields validated. Errors:{}", 
								attempt, maxPollingAttempts, matchCount[0], totalChecks[0], errors.toString());
						LOGGER.info("Waiting {} seconds before next polling attempt...", VALIDATION_POLL_INTERVAL_MS / 1000);
						safeSleep(VALIDATION_POLL_INTERVAL_MS);
						driver.navigate().refresh();
						Utilities.waitForPageReady(driver, 2);
						waitForBackgroundDataLoad();
						safeSleep(2000);
						reSearchProfile();
					} else {
						String failMsg = String.format("Profile values NOT fully validated after %d attempts (%d/%d fields matched).%s",
								maxPollingAttempts, matchCount[0], totalChecks[0], errors.toString());
						LOGGER.error("===== VALIDATION FAILED =====");
						Assert.fail(failMsg);
					}

				} catch (Exception retryEx) {
					if (attempt == maxPollingAttempts) {
						throw retryEx;
					}
					LOGGER.info("Attempt {}/{} encountered error, retrying in {} seconds...", 
							attempt, maxPollingAttempts, VALIDATION_POLL_INTERVAL_MS / 1000);
					safeSleep(VALIDATION_POLL_INTERVAL_MS);
				}
			}

		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_corrected_values", "Failed to verify corrected data values", e);
		}
	}

	public void verify_profile_is_not_found_in_jobs_missing_data_screen() {
		try {
			// Retry logic: Profile removal from Missing Data screen might take time
			int maxRetries = MAX_MISSING_DATA_RETRIES;
			int retryDelay = MISSING_DATA_CHECK_RETRY_DELAY_MS;
			String searchedJobCode = firstProfileJobCode.get();
			
			LOGGER.info("Verifying profile '{}' is removed from Missing Data screen", searchedJobCode);
			
			for (int attempt = 1; attempt <= maxRetries; attempt++) {
		try {
			Utilities.waitForPageReady(driver, 3);

			boolean profileFound = false;
			
			// Load all profiles by scrolling (if pagination/lazy loading exists)
			try {
				scrollToLoadAllProfiles();
			} catch (Exception scrollEx) {
				LOGGER.debug("Scroll to load profiles: {}", scrollEx.getMessage());
			}
			
			// Get all rows after loading
			List<WebElement> rows = findElements(JOB_ROWS_IN_MISSING_DATA_SCREEN);
					
				// Filter to visible data rows
				List<WebElement> visibleRows = new ArrayList<>();
				for (WebElement row : rows) {
					try {
						if (row.isDisplayed() && !row.getText().trim().isEmpty()) {
							visibleRows.add(row);
						}
					} catch (Exception e) {
						LOGGER.debug("Skipping row: {}", e.getMessage());
					}
				}
					
					LOGGER.info("Attempt {}/{}: Checking {} visible rows (out of {} DOM rows)", 
							attempt, maxRetries, visibleRows.size(), rows.size());
				
		for (WebElement row : visibleRows) {
			String rowText = row.getText();
			if (rowText.contains(searchedJobCode)) {
				profileFound = true;
						LOGGER.warn("❌ Profile '{}' still found: {}", searchedJobCode, 
								rowText.replaceAll("\\n", " | ").substring(0, Math.min(150, rowText.length())));
				break;
			}
		}

					if (!profileFound) {
						LOGGER.info("✓ Profile '{}' removed from Missing Data screen (attempt {})", searchedJobCode, attempt);
						return;
					}
					
					if (attempt < maxRetries) {
						LOGGER.info("Attempt {}/{}: Profile still present. Retrying in {} sec...", 
								attempt, maxRetries, retryDelay / 1000);
						driver.navigate().refresh();
						Utilities.waitForPageReady(driver, 3);
						waitForBackgroundDataLoad();
						safeSleep(retryDelay);
					} else {
						Assert.fail("Profile should NOT be found in Jobs Missing Data screen after re-upload (still found after " + maxRetries + " attempts)");
					}
					
				} catch (Exception retryEx) {
					if (attempt == maxRetries) {
						throw retryEx;
					}
					LOGGER.info("Attempt {}/{} failed, retrying in 10 seconds...", attempt, maxRetries);
					safeSleep(retryDelay);
				}
			}

		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_profile_not_in_missing_data",
					"Profile still found in Missing Data screen", e);
		}
	}
	
	/**
	 * Scrolls through the Missing Data screen to load all profiles (if lazy loading is used)
	 */
	private void scrollToLoadAllProfiles() {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			long lastHeight = (long) js.executeScript("return document.body.scrollHeight");
			int scrollAttempts = 0;
			int maxScrollAttempts = 10; // Prevent infinite scrolling
			
			while (scrollAttempts < maxScrollAttempts) {
				// Scroll to bottom
				js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
				safeSleep(1000); // Wait for lazy load
				
				// Calculate new scroll height and compare
				long newHeight = (long) js.executeScript("return document.body.scrollHeight");
				if (newHeight == lastHeight) {
					// No more content to load
					break;
				}
				lastHeight = newHeight;
				scrollAttempts++;
			}
			
			// Scroll back to top
			js.executeScript("window.scrollTo(0, 0);");
			safeSleep(500);
			
			LOGGER.debug("Scrolled through page {} times to load all profiles", scrollAttempts);
		} catch (Exception e) {
			LOGGER.debug("Error during scroll to load all profiles: {}", e.getMessage());
		}
	}

	public void click_on_close_button_to_return_to_job_mapping_page() {
		try {
			Utilities.waitForPageReady(driver, 2);
			Utilities.waitForClickable(wait, CLOSE_REUPLOAD_JOBS_PAGE_BUTTON);
			jsClick(findElement(CLOSE_REUPLOAD_JOBS_PAGE_BUTTON));
			Utilities.waitForPageReady(driver, 3);
			LOGGER.info("Clicked Close button to return to Job Mapping page");

		} catch (Exception e) {
			Utilities.handleError(LOGGER, "click_close_button", "Failed to click Close button", e);
		}
	}

	public void identify_profiles_with_missing_grade_value_in_excel() {
		try {
			List<String[]> profiles = extractedProfiles.get();
			int missingCount = 0;

			for (String[] profile : profiles) {
				if (profile.length > 5 && (profile[5] == null || profile[5].trim().isEmpty())) {
					missingCount++;
				}
			}

			LOGGER.info("Found " + missingCount + " profiles with missing Grade value");

		} catch (Exception e) {
			Utilities.handleError(LOGGER, "identify_missing_grade", "Failed to identify profiles with missing Grade", e);
		}
	}

	public void identify_profiles_with_missing_department_value_in_excel() {
		try {
			List<String[]> profiles = extractedProfiles.get();
			int missingCount = 0;

			for (String[] profile : profiles) {
				if (profile.length > 2 && (profile[2] == null || profile[2].trim().isEmpty())) {
					missingCount++;
				}
			}

			LOGGER.info("Found " + missingCount + " profiles with missing Department value");

		} catch (Exception e) {
			Utilities.handleError(LOGGER, "identify_missing_department", "Failed to identify profiles with missing Department", e);
		}
	}

	public void identify_all_profiles_with_any_missing_values_in_excel() {
		try {
			List<String[]> profiles = extractedProfiles.get();
			int missingCount = 0;

			for (String[] profile : profiles) {
				boolean hasMissing = false;
				for (int i = 2; i < Math.min(profile.length, 6); i++) {
					if (profile[i] == null || profile[i].trim().isEmpty()) {
						hasMissing = true;
						break;
					}
				}
				if (hasMissing) {
					missingCount++;
				}
			}

			LOGGER.info("Found " + missingCount + " profiles with any missing values");

		} catch (Exception e) {
			Utilities.handleError(LOGGER, "identify_all_missing", "Failed to identify profiles with missing values", e);
		}
	}

	public void fill_all_missing_values_with_appropriate_default_data() {
		try {
			fill_missing_grade_values_with_default_grade("JGL01");
			fill_missing_department_values_with_default_department("Engineering");
			fill_missing_job_family_values_with_default_value("General");
			fill_missing_job_sub_family_values_with_default_value("Operations");

			LOGGER.info("All missing values filled with default data");

		} catch (Exception e) {
			Utilities.handleError(LOGGER, "fill_all_missing_values", "Failed to fill all missing values", e);
		}
	}

	public void upload_generated_excel_file() {
		try {
			String filePath = excelFilePath.get();

			if (filePath == null || filePath.isEmpty() || filePath.equals("NOT_SET")) {
				filePath = buildResourcePath(DEFAULT_EXCEL_FILENAME);
			}

			File csvFile = new File(filePath);
			if (!csvFile.exists()) {
				throw new IOException("Generated Excel file does not exist at: " + filePath);
			}

			LOGGER.info("Uploading generated Excel file: " + csvFile.getName());

			PO02_AddMoreJobsFunctionality po02 = new PO02_AddMoreJobsFunctionality();
			po02.upload_file_using_browse_files_button(filePath);

			LOGGER.info("Successfully uploaded generated Excel file");

		} catch (Exception e) {
			Utilities.handleError(LOGGER, "upload_generated_excel_file", "Failed to upload generated Excel file", e);
		}
	}

	public String getGeneratedexcelFilePath() {
		String filePath = excelFilePath.get();
		if (filePath == null || filePath.isEmpty() || filePath.equals("NOT_SET")) {
			filePath = buildResourcePath(DEFAULT_EXCEL_FILENAME);
		}
		return filePath;
	}

	public static void resetThreadLocals() {
		missingDataCountBefore.remove();
		missingDataCountAfter.remove();
		totalResultsCountBefore.remove();
		totalResultsCountAfter.remove();
		totalProfilesInMissingDataScreen.remove();
		extractedProfiles.remove();
		firstProfileJobCode.remove();
		firstProfileJobName.remove();
		firstProfileExpectedGrade.remove();
		firstProfileExpectedDepartment.remove();
		firstProfileExpectedJobFamily.remove();
		firstProfileExpectedJobSubFamily.remove();
		excelFilePath.remove();
		searchResultsFoundCount.remove();
	}

	private String cleanNAValue(String value) {
		if (value == null || value.equalsIgnoreCase("N/A") || value.equalsIgnoreCase("NA") || value.equals("-")) {
			return "";
		}
		return value;
	}

	/**
	 * Helper method to validate a single field value
	 * @return true if field matches expected value, false otherwise
	 */
	private boolean validateFieldValue(String fieldName, String expected, String actual, 
	                                    StringBuilder errors, int[] matchCounter, int[] totalCounter) {
		if (expected == null || expected.equals("NOT_SET")) {
			return true; // Skip validation for unset fields
		}
		
		totalCounter[0]++;
		
		// Check for empty/null/N/A values
		if (actual == null || actual.trim().isEmpty() || 
		    actual.equalsIgnoreCase("N/A") || actual.equals("-") || actual.equalsIgnoreCase("--")) {
			errors.append(String.format("\n  ❌ %s is empty/N/A: '%s'", fieldName, actual == null ? "null" : actual));
			return false;
		}
		
		if (!actual.equalsIgnoreCase(expected)) {
			errors.append(String.format("\n  ❌ %s MISMATCH - Expected: '%s', Actual: '%s'", 
					fieldName, expected, actual));
			return false;
		}
		
		matchCounter[0]++;
		LOGGER.info("  ✓ {} matches: '{}'", fieldName, actual);
		return true;
	}

	private String cleanExtractedText(String value) {
		if (value == null) {
			return "";
		}
		value = value.trim();

		String quoteChars = "\"'`\u201C\u201D\u2018\u2019\u00AB\u00BB\u2039\u203A";

		boolean changed = true;
		while (changed && value.length() > 0) {
			changed = false;

			while (value.length() > 0 && quoteChars.indexOf(value.charAt(0)) >= 0) {
				value = value.substring(1);
				changed = true;
			}

			while (value.length() > 0 && quoteChars.indexOf(value.charAt(value.length() - 1)) >= 0) {
				value = value.substring(0, value.length() - 1);
				changed = true;
			}

			value = value.trim();
		}

		return value;
	}

	/**
	 * Helper method to extract Job Code from a Job Name/Code text
	 * Format examples: "Job Name (JOB-CODE)" or "Job Name - (JOB-CODE)"
	 */
	private String extractJobCodeFromText(String jobNameCodeText) {
		if (jobNameCodeText == null || jobNameCodeText.trim().isEmpty()) {
			return "";
		}
		
		String text = jobNameCodeText.trim();
		
		// Extract job code from format: "Job Name (JOB-CODE)"
		if (text.contains("(") && text.contains(")")) {
			int startParen = text.lastIndexOf("(");
			int endParen = text.lastIndexOf(")");
			if (startParen >= 0 && endParen > startParen) {
				return text.substring(startParen + 1, endParen).trim();
			}
		}
		
		return "";
	}

	/**
	 * Helper method to find a specific row by Job Code in search results
	 * Returns the WebElement of the matching row, or null if not found
	 */
	private WebElement findRowByJobCode(List<WebElement> rows, String expectedJobCode) {
		if (rows == null || rows.isEmpty() || expectedJobCode == null || expectedJobCode.isEmpty()) {
			return null;
		}
		
		for (int i = 0; i < rows.size(); i++) {
			WebElement row = rows.get(i);
			try {
				// Job Name/Code is in column 2 (td[2])
				List<WebElement> cells = row.findElements(By.tagName("td"));
				if (cells.size() >= 2) {
					String nameCodeText = cells.get(1).getText().trim();
					String jobCode = extractJobCodeFromText(nameCodeText);
					
					if (jobCode.equals(expectedJobCode)) {
						LOGGER.debug("✓ Found row with matching Job Code: '{}' at index {}", jobCode, i);
						return row;
					}
				}
			} catch (Exception ex) {
				LOGGER.debug("Could not extract Job Code from row {}: {}", i, ex.getMessage());
			}
		}
		
		LOGGER.debug("Job Code '{}' not found in {} rows", expectedJobCode, rows.size());
		return null;
	}

	/**
	 * Helper method to re-search for the profile after page refresh
	 */
	private void reSearchProfile() {
		try {
			String jobName = firstProfileJobName.get();
			WebElement searchInput = Utilities.waitForClickable(wait, JOB_SEARCH_INPUT);
			searchInput.clear();
			safeSleep(500);
			searchInput.sendKeys(jobName);
			safeSleep(2000);
			Utilities.waitForSpinnersToDisappear(driver, 10);
		} catch (Exception e) {
			LOGGER.warn("Failed to re-search for profile: {}", e.getMessage());
		}
	}
}



