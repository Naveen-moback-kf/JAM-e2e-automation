package com.kfonetalentsuite.pageobjects.JobMapping;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import com.kfonetalentsuite.utils.JobMapping.PageObjectHelper;

public class PO35_ReuploadMissingDataProfiles extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO35_ReuploadMissingDataProfiles.class);
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
	public static ThreadLocal<String> csvFilePath = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<Integer> searchResultsFoundCount = ThreadLocal.withInitial(() -> 0);
	private static final String CSV_HEADER = "Client Job Code,Client Job Title,Department,Job Family,Job Sub Family,Job Grade,Is Executive";
	private static final String DEFAULT_CSV_FILENAME = "MissingDataProfiles_ToFix.csv";
	private static final By REUPLOAD_BUTTON = By.xpath("//button[contains(text(), 'Re-upload')] | //button[contains(text(), 'upload')]");
	private static final By CLOSE_BUTTON = By.xpath("//button[contains(@class, 'border-[#007BC7]') and contains(text(), 'Close')]");
	private static final By JOB_ROWS_MISSING_DATA = By.xpath("//table//tbody//tr[td]");
	private static final By SEARCH_INPUT_MISSING_DATA = By.xpath("//input[@placeholder='Search'] | //input[contains(@id, 'search')]");
	private static final By JOB_SEARCH_INPUT = By.xpath("//input[@id='search-job-title-input-search-input']");
	private static final By MISSING_DATA_SCREEN_TITLE = By.xpath("//h1[contains(text(), 'Jobs With Missing Data')] | //h1[contains(text(), 'Missing Data')] | //*[contains(text(), 'Organization Jobs With Missing Data')]");
	private static final By TIP_MESSAGE = By.xpath("//p[contains(text(), 'jobs have missing data')]");
	private static final By SEARCH_RESULTS_ROWS = By.xpath("//div[@id='org-job-container']//tbody//tr[.//td[1]//input[@type='checkbox']]");
	private static final By SHOWING_JOB_RESULTS = By.xpath("//div[contains(@id,'results-toggle')]//*[contains(text(),'Showing')]");
	
	// Column indices for Job Mapping table (same as PO25)
	// Column 1 = Checkbox, Column 2 = Job Name/Code, Column 3 = Grade, Column 4 = Department
	private static final int GRADE_COLUMN_INDEX = 3;
	private static final int DEPARTMENT_COLUMN_INDEX = 4;
	// Function/SubFunction are in the next row (expandable row) under span[2]

	public PO35_ReuploadMissingDataProfiles() {
		super();
	}

	public void verify_user_is_navigated_to_jobs_with_missing_data_screen() {
		try {
			PageObjectHelper.waitForPageReady(driver, 5);
			PageObjectHelper.waitForVisible(wait, MISSING_DATA_SCREEN_TITLE);
			Assert.assertTrue(findElement(MISSING_DATA_SCREEN_TITLE).isDisplayed(),
					"Jobs With Missing Data screen title should be displayed");
			PageObjectHelper.log(LOGGER, "Successfully navigated to Jobs With Missing Data screen");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_navigation_to_missing_data_screen",
					"Failed to verify navigation to Jobs With Missing Data screen", e);
		}
	}

	public void verify_reupload_button_is_displayed_on_jobs_missing_data_screen() {
		try {
			PageObjectHelper.waitForPageReady(driver, 3);
			PageObjectHelper.waitForVisible(wait, REUPLOAD_BUTTON);
			Assert.assertTrue(findElement(REUPLOAD_BUTTON).isDisplayed(), "Re-upload button should be displayed");
			PageObjectHelper.log(LOGGER, "Re-upload button is displayed on Jobs Missing Data screen");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_reupload_button_is_displayed",
					"Failed to verify Re-upload button display", e);
		}
	}

	public void click_on_reupload_button_in_jobs_missing_data_screen() {
		try {
			driver.manage().deleteAllCookies();
			PageObjectHelper.waitForPageReady(driver, 5);
			safeSleep(2000);

			int maxRetries = 3;
			for (int attempt = 1; attempt <= maxRetries; attempt++) {
				try {
					WebElement reuploadBtn = PageObjectHelper.waitForClickable(wait, REUPLOAD_BUTTON);
					scrollToElement(reuploadBtn);
					safeSleep(500);

					reuploadBtn = findElement(REUPLOAD_BUTTON);
					jsClick(reuploadBtn);

					PageObjectHelper.log(LOGGER, "Clicked on Re-upload button in Jobs Missing Data screen");
					PageObjectHelper.waitForPageReady(driver, 5);
					return;

				} catch (StaleElementReferenceException e) {
					if (attempt == maxRetries) {
						throw e;
					}
					safeSleep(1000);
				}
			}
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_reupload_button", "Failed to click Re-upload button", e);
		}
	}

	public void capture_total_count_of_profiles_in_jobs_missing_data_screen() {
		try {
			PageObjectHelper.waitForPageReady(driver, 3);
			int totalCount = findElements(JOB_ROWS_MISSING_DATA).size();

			if (totalCount > 0) {
				totalProfilesInMissingDataScreen.set(totalCount);
				PageObjectHelper.log(LOGGER, "Captured total profiles in Missing Data screen: " + totalCount);
			} else {
				PageObjectHelper.log(LOGGER, "No profiles found in Missing Data screen");
			}
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "capture_total_count", "Failed to capture total profile count", e);
		}
	}

	public void extract_details_of_top_10_profiles_from_jobs_missing_data_screen() {
		try {
			PageObjectHelper.waitForPageReady(driver, 3);
			List<String[]> profiles = new ArrayList<>();

			List<WebElement> currentRows = findElements(JOB_ROWS_MISSING_DATA);
			int totalAvailable = currentRows.size();
			int maxProfiles = Math.min(10, totalAvailable);

			if (totalAvailable < 10) {
				PageObjectHelper.log(LOGGER, "Found " + totalAvailable + " profiles (less than 10). Fetching all available profiles.");
			} else {
				PageObjectHelper.log(LOGGER, "Found " + totalAvailable + " profiles. Fetching top 10 profiles.");
			}

			LOGGER.info("Extracting details of {} profiles from Missing Data screen", maxProfiles);
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
			PageObjectHelper.log(LOGGER, "Successfully extracted " + profiles.size() + " profiles from Missing Data screen");

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "extract_top_10_profiles",
					"Failed to extract profiles from Missing Data screen", e);
		}
	}

	public void create_csv_file_with_extracted_profiles_in_job_catalog_format() {
		try {
			List<String[]> profiles = extractedProfiles.get();

			if (profiles == null || profiles.isEmpty()) {
				throw new IOException("No profiles available to create CSV file");
			}

			String filePath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
					+ File.separator + "resources" + File.separator + DEFAULT_CSV_FILENAME;

			csvFilePath.set(filePath);

			try (FileWriter writer = new FileWriter(filePath)) {
				writer.write(CSV_HEADER + "\n");
				for (String[] profile : profiles) {
					writer.write(profileToCsvLine(profile) + "\n");
				}
			}

			PageObjectHelper.log(LOGGER, "CSV file created with " + profiles.size() + " profiles at: " + filePath);

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "create_csv_file", "Failed to create CSV file", e);
		}
	}

	public void save_csv_file_as_in_test_resources_folder(String fileName) {
		try {
			String filePath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
					+ File.separator + "resources" + File.separator + fileName;

			csvFilePath.set(filePath);

			File defaultFile = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator
					+ "test" + File.separator + "resources" + File.separator + DEFAULT_CSV_FILENAME);

			if (defaultFile.exists()) {
				File newFile = new File(filePath);
				defaultFile.renameTo(newFile);
			}

			PageObjectHelper.log(LOGGER, "CSV file saved as: " + fileName);

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "save_csv_file", "Failed to save CSV file", e);
		}
	}

	public void verify_csv_file_is_created_successfully_with_correct_headers() {
		try {
			String filePath = csvFilePath.get();
			File csvFile = new File(filePath);

			Assert.assertTrue(csvFile.exists(), "CSV file should exist at: " + filePath);

			try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
				String headerLine = reader.readLine();
				Assert.assertNotNull(headerLine, "CSV file should have header line");
				Assert.assertEquals(headerLine.trim(), CSV_HEADER, "CSV header should match expected format");
			}

			PageObjectHelper.log(LOGGER, "CSV file verified with correct headers");

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_csv_headers", "Failed to verify CSV file headers", e);
		}
	}

	public void verify_csv_file_contains_extracted_profile_data() {
		try {
			String filePath = csvFilePath.get();
			int lineCount = 0;

			try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
				while (reader.readLine() != null) {
					lineCount++;
				}
			}

			int dataRows = lineCount - 1;
			Assert.assertTrue(dataRows > 0, "CSV file should contain profile data");

			PageObjectHelper.log(LOGGER, "CSV file contains " + dataRows + " profile records");

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_csv_data", "Failed to verify CSV file data", e);
		}
	}

	public void csv_file_with_missing_data_profiles_exists() {
		try {
			String filePath = csvFilePath.get();
			if (filePath == null || filePath.isEmpty() || filePath.equals("NOT_SET")) {
				filePath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
						+ File.separator + "resources" + File.separator + DEFAULT_CSV_FILENAME;
				csvFilePath.set(filePath);
			}

			File csvFile = new File(filePath);
			Assert.assertTrue(csvFile.exists(), "CSV file should exist for processing");
			PageObjectHelper.log(LOGGER, "Confirmed CSV file exists at: " + filePath);

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "csv_file_exists", "CSV file does not exist", e);
		}
	}

	public void read_the_exported_csv_file_with_missing_data_profiles() {
		try {
			String filePath = csvFilePath.get();
			List<String[]> profiles = new ArrayList<>();

			try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
				String line;
				boolean isHeader = true;

				while ((line = reader.readLine()) != null) {
					if (isHeader) {
						isHeader = false;
						continue;
					}
					String[] values = parseCsvLine(line);
					profiles.add(values);
				}
			}

			extractedProfiles.set(profiles);
			PageObjectHelper.log(LOGGER, "Read " + profiles.size() + " profiles from CSV file");

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "read_csv_file", "Failed to read CSV file", e);
		}
	}

	public void fill_missing_grade_values_with_default_grade(String defaultGrade) {
		try {
			List<String[]> profiles = extractedProfiles.get();
			int filledCount = 0;

			for (int i = 0; i < profiles.size(); i++) {
				String[] profile = profiles.get(i);
				if (profile.length > 5 && (profile[5] == null || profile[5].trim().isEmpty())) {
					profile[5] = defaultGrade;
					filledCount++;
					
					// Store expected value for first profile
					if (i == 0) {
						firstProfileExpectedGrade.set(defaultGrade);
						LOGGER.info("First profile expected Grade value set to: '{}'", defaultGrade);
					}
				}
			}

			extractedProfiles.set(profiles);
			PageObjectHelper.log(LOGGER, "Filled " + filledCount + " missing Grade values with: " + defaultGrade);

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "fill_missing_grade", "Failed to fill missing Grade values", e);
		}
	}

	public void fill_missing_department_values_with_default_department(String defaultDepartment) {
		try {
			List<String[]> profiles = extractedProfiles.get();
			int filledCount = 0;

			for (int i = 0; i < profiles.size(); i++) {
				String[] profile = profiles.get(i);
				if (profile.length > 2 && (profile[2] == null || profile[2].trim().isEmpty())) {
					profile[2] = defaultDepartment;
					filledCount++;
					
					// Store expected value for first profile
					if (i == 0) {
						firstProfileExpectedDepartment.set(defaultDepartment);
						LOGGER.info("First profile expected Department value set to: '{}'", defaultDepartment);
					}
				}
			}

			extractedProfiles.set(profiles);
			PageObjectHelper.log(LOGGER, "Filled " + filledCount + " missing Department values with: " + defaultDepartment);

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "fill_missing_department", "Failed to fill missing Department values", e);
		}
	}

	public void fill_missing_job_family_values_with_default_value(String defaultValue) {
		try {
			List<String[]> profiles = extractedProfiles.get();
			int filledCount = 0;

			for (int i = 0; i < profiles.size(); i++) {
				String[] profile = profiles.get(i);
				if (profile.length > 3 && (profile[3] == null || profile[3].trim().isEmpty())) {
					profile[3] = defaultValue;
					filledCount++;
					
					// Store expected value for first profile
					if (i == 0) {
						firstProfileExpectedJobFamily.set(defaultValue);
						LOGGER.info("First profile expected Job Family value set to: '{}'", defaultValue);
					}
				}
			}

			extractedProfiles.set(profiles);
			PageObjectHelper.log(LOGGER, "Filled " + filledCount + " missing Job Family values with: " + defaultValue);

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "fill_missing_job_family", "Failed to fill missing Job Family values", e);
		}
	}

	public void fill_missing_job_sub_family_values_with_default_value(String defaultValue) {
		try {
			List<String[]> profiles = extractedProfiles.get();
			int filledCount = 0;

			for (int i = 0; i < profiles.size(); i++) {
				String[] profile = profiles.get(i);
				if (profile.length > 4 && (profile[4] == null || profile[4].trim().isEmpty())) {
					profile[4] = defaultValue;
					filledCount++;
					
					// Store expected value for first profile
					if (i == 0) {
						firstProfileExpectedJobSubFamily.set(defaultValue);
						LOGGER.info("First profile expected Job Sub Family value set to: '{}'", defaultValue);
					}
				}
			}

			extractedProfiles.set(profiles);
			PageObjectHelper.log(LOGGER, "Filled " + filledCount + " missing Job Sub Family values with: " + defaultValue);

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "fill_missing_job_sub_family", "Failed to fill missing Job Sub Family values", e);
		}
	}

	public void save_the_updated_csv_file_with_filled_data() {
		try {
			String filePath = csvFilePath.get();
			List<String[]> profiles = extractedProfiles.get();

			try (FileWriter writer = new FileWriter(filePath)) {
				writer.write(CSV_HEADER + "\n");
				for (String[] profile : profiles) {
					writer.write(profileToCsvLine(profile) + "\n");
				}
			}

			PageObjectHelper.log(LOGGER, "Updated CSV file saved with " + profiles.size() + " profiles");

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "save_updated_csv", "Failed to save updated CSV file", e);
		}
	}

	public void verify_updated_csv_file_has_no_empty_grade_values() {
		try {
			List<String[]> profiles = extractedProfiles.get();

			for (String[] profile : profiles) {
				if (profile.length > 5) {
					Assert.assertFalse(profile[5] == null || profile[5].trim().isEmpty(),
							"Grade value should not be empty for profile: " + profile[0]);
				}
			}

			PageObjectHelper.log(LOGGER, "Verified all Grade values are filled in CSV");

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_no_empty_grades", "Found empty Grade values in CSV", e);
		}
	}

	public void verify_updated_csv_file_has_no_empty_department_values() {
		try {
			List<String[]> profiles = extractedProfiles.get();

			for (String[] profile : profiles) {
				if (profile.length > 2) {
					Assert.assertFalse(profile[2] == null || profile[2].trim().isEmpty(),
							"Department value should not be empty for profile: " + profile[0]);
				}
			}

			PageObjectHelper.log(LOGGER, "Verified all Department values are filled in CSV");

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_no_empty_departments", "Found empty Department values in CSV", e);
		}
	}

	public void verify_updated_csv_file_has_no_empty_required_fields() {
		try {
			List<String[]> profiles = extractedProfiles.get();

			for (String[] profile : profiles) {
				Assert.assertFalse(profile[0] == null || profile[0].trim().isEmpty(), "Job Code should not be empty");
				Assert.assertFalse(profile[1] == null || profile[1].trim().isEmpty(), "Job Title should not be empty");
			}

			PageObjectHelper.log(LOGGER, "Verified all required fields are filled in CSV");

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_no_empty_required_fields", "Found empty required fields in CSV", e);
		}
	}

	public void capture_total_results_count_before_reupload() {
		try {
			PageObjectHelper.waitForPageReady(driver, 3);
			PageObjectHelper.waitForSpinnersToDisappear(driver, 10);
			
			// CRITICAL: Ensure no search filters are active to get TOTAL results, not filtered results
			// Use JavaScript to clear - most reliable approach (same as PO27)
			try {
				PageObjectHelper.log(LOGGER, "Clearing search filter to get total results count...");
				
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
				
				Boolean cleared = (Boolean) js.executeScript(clearScript);
				
				if (cleared != null && cleared) {
					LOGGER.info("Search filter cleared using JavaScript");
					safeSleep(3000);
					PageObjectHelper.waitForSpinnersToDisappear(driver, 10);
					waitForBackgroundDataLoad(); // Wait for full data to reload
					safeSleep(2000);
				} else {
					LOGGER.info("Search bar was already empty or not found");
				}
			} catch (Exception clearEx) {
				LOGGER.debug("Could not clear search filter: {}", clearEx.getMessage());
			}
			
			WebElement resultsElement = PageObjectHelper.waitForVisible(wait, SHOWING_JOB_RESULTS);
			String resultsText = resultsElement.getText().trim();
			
			PageObjectHelper.log(LOGGER, "Results text: " + resultsText);
			
			// Parse "Showing X of Y results" format - extract Y (total results)
			// Example: "Showing 1-50 of 1234 results"
			if (resultsText.contains("of") && resultsText.contains("results")) {
				String[] parts = resultsText.split(" ");
				for (int i = 0; i < parts.length; i++) {
					if (parts[i].equals("of") && i + 1 < parts.length) {
						int totalCount = Integer.parseInt(parts[i + 1]);
						totalResultsCountBefore.set(totalCount);
						PageObjectHelper.log(LOGGER, "Total Results count BEFORE re-upload: " + totalCount);
						return;
					}
				}
			}
			
			PageObjectHelper.log(LOGGER, "Could not parse Total Results count from: " + resultsText);
			
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "capture_total_results_count_before",
					"Failed to capture Total Results count before re-upload", e);
		}
	}

	public void capture_total_results_count_after_reupload() {
		try {
			PageObjectHelper.waitForPageReady(driver, 3);
			PageObjectHelper.waitForSpinnersToDisappear(driver, 10);
			
			// CRITICAL: Clear any active search filters to get TOTAL results, not filtered results
			// Use JavaScript to clear - most reliable approach (same as PO27)
			try {
				PageObjectHelper.log(LOGGER, "Clearing search filter to get total results count...");
				
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
				
				Boolean cleared = (Boolean) js.executeScript(clearScript);
				
				if (cleared != null && cleared) {
					LOGGER.info("Search filter cleared using JavaScript");
					safeSleep(3000);
					PageObjectHelper.waitForSpinnersToDisappear(driver, 10);
					waitForBackgroundDataLoad(); // Wait for full data to reload
					safeSleep(2000);
				} else {
					LOGGER.info("Search bar was already empty or not found");
				}
			} catch (Exception clearEx) {
				LOGGER.debug("Could not clear search filter: {}", clearEx.getMessage());
			}
			
			// Retry logic: Ensure results element is stable
			int maxRetries = 3;
			int retryDelay = 5000; // 5 seconds
			
			for (int attempt = 1; attempt <= maxRetries; attempt++) {
				try {
					WebElement resultsElement = PageObjectHelper.waitForVisible(wait, SHOWING_JOB_RESULTS);
					String resultsText = resultsElement.getText().trim();
					
					PageObjectHelper.log(LOGGER, "Attempt " + attempt + " - Results text: " + resultsText);
					
					// Parse "Showing X of Y results" format - extract Y (total results)
					if (resultsText.contains("of") && resultsText.contains("results")) {
						String[] parts = resultsText.split(" ");
						for (int i = 0; i < parts.length; i++) {
							if (parts[i].equals("of") && i + 1 < parts.length) {
								int totalCount = Integer.parseInt(parts[i + 1]);
								
								// Sanity check: Total count should be reasonable (> 10)
								if (totalCount < 10) {
									LOGGER.warn("Attempt {}/{}: Total count seems too low: {} - might still be filtered", 
											attempt, maxRetries, totalCount);
									if (attempt < maxRetries) {
										safeSleep(retryDelay);
										continue;
									}
								}
								
								totalResultsCountAfter.set(totalCount);
								PageObjectHelper.log(LOGGER, "Total Results count AFTER re-upload: " + totalCount);
								return;
							}
						}
					}
					
					if (attempt < maxRetries) {
						LOGGER.info("Attempt {}/{}: Could not parse results from '{}', retrying...", 
								attempt, maxRetries, resultsText);
						safeSleep(retryDelay);
					}
					
				} catch (Exception retryEx) {
					if (attempt == maxRetries) {
						throw retryEx;
					}
					LOGGER.info("Attempt {}/{} failed, retrying in 5 seconds...", attempt, maxRetries);
					safeSleep(retryDelay);
				}
			}
			
			PageObjectHelper.log(LOGGER, "Could not capture Total Results count after all retry attempts");
			
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "capture_total_results_count_after",
					"Failed to capture Total Results count after re-upload", e);
		}
	}

	public void verify_total_results_count_remains_unchanged() {
		try {
			int before = totalResultsCountBefore.get();
			int after = totalResultsCountAfter.get();
			
			Assert.assertEquals(after, before,
					"Total Results count should remain unchanged after re-upload (updating existing profiles, not adding new). Before: " + before + ", After: " + after);
			
			PageObjectHelper.log(LOGGER, "✓ Total Results count unchanged: " + before + " → " + after + " (as expected - updating existing profiles)");
			
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_total_results_unchanged",
					"Total Results count changed unexpectedly", e);
		}
	}

	public void capture_the_count_of_jobs_with_missing_data_before_reupload() {
		try {
			WebElement tipMessage = findElement(TIP_MESSAGE);
			String text = tipMessage.getText();

			java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("(\\d+)\\s+jobs");
			java.util.regex.Matcher matcher = pattern.matcher(text);

			if (matcher.find()) {
				int count = Integer.parseInt(matcher.group(1));
				missingDataCountBefore.set(count);
				PageObjectHelper.log(LOGGER, "Missing data count BEFORE re-upload: " + count);
			}

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "capture_count_before",
					"Failed to capture missing data count before re-upload", e);
		}
	}

	public void capture_the_count_of_jobs_with_missing_data_after_reupload() {
		try {
			PageObjectHelper.waitForPageReady(driver, 3);

			// Retry logic: Sometimes the count takes time to update
			int maxRetries = 5;
			int retryDelay = 10000; // 10 seconds
			
			for (int attempt = 1; attempt <= maxRetries; attempt++) {
				try {
			WebElement tipMessage = findElement(TIP_MESSAGE);
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
							PageObjectHelper.log(LOGGER, "Missing data count AFTER re-upload: " + count + " (decreased from " + beforeCount + ")");
							return;
						} else if (count == beforeCount && attempt < maxRetries) {
							// Count hasn't changed yet - retry
							LOGGER.info("Attempt {}/{}: Missing data count still at {} (waiting for update...)", 
									attempt, maxRetries, count);
							safeSleep(retryDelay);
							driver.navigate().refresh();
							PageObjectHelper.waitForPageReady(driver, 3);
							waitForBackgroundDataLoad(); // Wait for background data API to complete
							continue;
						} else {
							// Last attempt or count increased (unexpected)
				missingDataCountAfter.set(count);
				PageObjectHelper.log(LOGGER, "Missing data count AFTER re-upload: " + count);
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
			PageObjectHelper.handleError(LOGGER, "capture_count_after",
					"Failed to capture missing data count after re-upload", e);
		}
	}

	public void verify_missing_data_count_has_decreased_compared_to_before_reupload() {
		try {
			int before = missingDataCountBefore.get();
			int after = missingDataCountAfter.get();

			Assert.assertTrue(after < before,
					"Missing data count should decrease after re-upload. Before: " + before + ", After: " + after);

			int decreased = before - after;
			PageObjectHelper.log(LOGGER, "✓ Missing data count decreased from " + before + " to " + after + " (by " + decreased + ")");

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_count_decreased",
					"Missing data count did not decrease as expected", e);
		}
	}

	public void wait_and_refresh_job_mapping_page_after_upload() {
		try {
			// Simple fixed wait approach - let the profile verification scenario handle smart retries
			PageObjectHelper.log(LOGGER, "Waiting for backend to process uploaded data...");
			PageObjectHelper.log(LOGGER, "Waiting 3 minutes for backend processing (profile validation will have smart retries)...");
			safeSleep(180000); // 3 minutes fixed wait

			PageObjectHelper.log(LOGGER, "Refreshing Job Mapping page...");
			driver.navigate().refresh();
			PageObjectHelper.waitForPageReady(driver, 5);
			PageObjectHelper.waitForSpinnersToDisappear(driver, 15);
			waitForBackgroundDataLoad(); // Wait for background data API to complete
			safeSleep(2000);

			PageObjectHelper.log(LOGGER, "✓ Backend wait completed - page refreshed and ready for validation");
			PageObjectHelper.log(LOGGER, "Note: Profile verification scenario will retry if data is not yet updated");

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "wait_and_refresh", "Failed to wait and refresh Job Mapping page", e);
		}
	}

	public void search_for_first_reuploaded_profile_by_job_name_from_csv() {
		try {
			String jobName = firstProfileJobName.get();

			if (jobName == null || jobName.isEmpty() || jobName.equals("NOT_SET")) {
				throw new IOException("No job name captured for search. Please ensure extraction step ran first.");
			}

			PageObjectHelper.log(LOGGER, "Searching for re-uploaded profile by Job Name: " + jobName);

			// Retry logic with dynamic polling: Profile might not be searchable immediately after backend processing
			// Use same polling strategy as profile validation - 30 second intervals, up to 20 attempts (10 minutes)
			int maxRetries = 20; // 20 attempts × 30 seconds = 10 minutes
			int retryDelay = 30000; // 30 seconds
			
			for (int attempt = 1; attempt <= maxRetries; attempt++) {
				try {
					LOGGER.info("=== Search Attempt {}/{} for Job Name: '{}' ===", attempt, maxRetries, jobName);
					
					// Refresh page before each attempt to ensure fresh data
					LOGGER.debug("Refreshing page to get latest data...");
					driver.navigate().refresh();
			PageObjectHelper.waitForPageReady(driver, 3);
					waitForBackgroundDataLoad();
					safeSleep(2000);
					LOGGER.debug("Page refreshed and ready");
					
			PageObjectHelper.waitForSpinnersToDisappear(driver, 10);
					LOGGER.debug("Spinners cleared");

					LOGGER.debug("Waiting for search input element to be clickable...");
			WebElement searchInput = PageObjectHelper.waitForClickable(wait, JOB_SEARCH_INPUT);
					LOGGER.debug("Search input element is clickable");
					
			searchInput.clear();
			safeSleep(500);
					LOGGER.debug("Search input cleared, sending keys: '{}'", jobName);
					
			searchInput.sendKeys(jobName);
					LOGGER.debug("Keys sent successfully");

			safeSleep(2000);
					LOGGER.debug("Waiting for spinners after search...");
			PageObjectHelper.waitForSpinnersToDisappear(driver, 10);
			PageObjectHelper.waitForPageReady(driver, 3);
					LOGGER.debug("Search completed, checking results...");

					// Check if search returned results
					List<WebElement> results = findElements(SEARCH_RESULTS_ROWS);
					LOGGER.info("Search returned {} results", results.size());
					
					if (!results.isEmpty()) {
						PageObjectHelper.log(LOGGER, "✓ Search completed for Job Name: " + jobName + " (found " + results.size() + " results)");
						return;
					}
					
					// No results yet
					if (attempt < maxRetries) {
						LOGGER.info("Attempt {}/{}: Profile not found yet. Waiting 30 seconds before next attempt...", attempt, maxRetries);
						safeSleep(retryDelay);
						// Page will be refreshed at the start of the next attempt
					} else {
						PageObjectHelper.log(LOGGER, "Search completed for Job Name: " + jobName + " (no results found after " + maxRetries + " attempts - 10 minutes)");
					}

				} catch (Exception retryEx) {
					if (attempt == maxRetries) {
						LOGGER.error("Final attempt {}/{} failed with exception: {} - {}", 
								attempt, maxRetries, retryEx.getClass().getSimpleName(), retryEx.getMessage());
						throw retryEx;
					}
					LOGGER.warn("Attempt {}/{} failed with exception: {} - {}", 
							attempt, maxRetries, retryEx.getClass().getSimpleName(), retryEx.getMessage());
					LOGGER.info("Waiting 30 seconds before next attempt...");
					safeSleep(retryDelay);
					// Page will be refreshed at the start of the next attempt
				}
			}

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "search_by_job_name", "Failed to search for re-uploaded profile", e);
		}
	}

	public void search_for_first_reuploaded_profile_by_job_code_in_missing_data_screen() {
		try {
			String jobCode = firstProfileJobCode.get();

			if (jobCode == null || jobCode.isEmpty() || jobCode.equals("NOT_SET")) {
				throw new IOException("No job code captured for search");
			}

			PageObjectHelper.waitForPageReady(driver, 2);
			WebElement searchInput = PageObjectHelper.waitForClickable(wait, SEARCH_INPUT_MISSING_DATA);
			searchInput.clear();
			searchInput.sendKeys(jobCode);

			PageObjectHelper.waitForPageReady(driver, 3);
			PageObjectHelper.log(LOGGER, "Searched for profile by Job Code in Missing Data screen: " + jobCode);

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "search_by_job_code_missing_data",
					"Failed to search in Missing Data screen", e);
		}
	}

	public void verify_profile_is_found_in_job_mapping_search_results() {
		try {
			PageObjectHelper.waitForPageReady(driver, 3);
			PageObjectHelper.waitForSpinnersToDisappear(driver, 10);

			List<WebElement> searchResults = findElements(SEARCH_RESULTS_ROWS);
			int resultCount = searchResults.size();

			searchResultsFoundCount.set(resultCount);

			if (resultCount == 0) {
				String jobName = firstProfileJobName.get();
				String errorMsg = "ERROR: Profile NOT found in search results for Job Name: " + jobName;
				PageObjectHelper.log(LOGGER, errorMsg);
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
					PageObjectHelper.log(LOGGER, "✓ Profile found in Job Mapping search results at row " + (matchingRowIndex + 1) + 
							" (total " + resultCount + " results) with matching Job Code: " + expectedJobCode);
				} else {
					String errorMsg = "ERROR: Job Code '" + expectedJobCode + "' NOT found in " + resultCount + " search results";
					PageObjectHelper.log(LOGGER, errorMsg);
					Assert.fail(errorMsg);
				}
			}

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_profile_found", "Error checking search results", e);
		}
	}

	public void verify_profile_does_not_display_missing_data_info_icon() {
		try {
			int rowIndexStored = searchResultsFoundCount.get();
			if (rowIndexStored == 0) {
				String errorMsg = "CANNOT verify missing data icon - no search results found!";
				PageObjectHelper.log(LOGGER, errorMsg);
				Assert.fail(errorMsg);
				return;
			}

			String expectedJobCode = firstProfileJobCode.get();

			// Retry logic: Missing data icon might take time to disappear
			int maxRetries = 5;
			int retryDelay = 8000; // 8 seconds
			
			for (int attempt = 1; attempt <= maxRetries; attempt++) {
				try {
			PageObjectHelper.waitForPageReady(driver, 2);

			List<WebElement> rows = findElements(SEARCH_RESULTS_ROWS);
			if (rows.isEmpty()) {
						if (attempt < maxRetries) {
							LOGGER.info("Attempt {}/{}: No profile rows found, refreshing...", attempt, maxRetries);
							driver.navigate().refresh();
							PageObjectHelper.waitForPageReady(driver, 2);
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
							PageObjectHelper.waitForPageReady(driver, 2);
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
						PageObjectHelper.log(LOGGER, "✓ Profile with Job Code '" + expectedJobCode + 
								"' successfully fixed - no missing data warning (verified on attempt " + attempt + ")");
						return;
					}
					
					// Icon still present
					if (attempt < maxRetries) {
						LOGGER.info("Attempt {}/{}: Missing data icon still present for Job Code '{}'. Waiting for update...", 
								attempt, maxRetries, expectedJobCode);
						safeSleep(retryDelay);
						driver.navigate().refresh();
						PageObjectHelper.waitForPageReady(driver, 2);
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
			PageObjectHelper.handleError(LOGGER, "verify_no_missing_data_icon",
					"Profile still displays Missing Data info icon", e);
		}
	}

	public void verify_profile_displays_the_corrected_data_values() {
		try {
			int rowIndexStored = searchResultsFoundCount.get();
			if (rowIndexStored == 0) {
				String errorMsg = "CANNOT verify corrected data values - no search results found!";
				PageObjectHelper.log(LOGGER, errorMsg);
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

			// Dynamic polling: Check every 20 seconds for up to 10 minutes
			int maxPollingAttempts = 30; // 30 attempts × 20 seconds = 10 minutes
			int pollingInterval = 20000; // 20 seconds
			long startTime = System.currentTimeMillis();
			
			PageObjectHelper.log(LOGGER, "Starting dynamic polling (checking every 20 seconds for up to 10 minutes)...");
			
			for (int attempt = 1; attempt <= maxPollingAttempts; attempt++) {
				try {
			PageObjectHelper.waitForPageReady(driver, 2);

			List<WebElement> rows = findElements(SEARCH_RESULTS_ROWS);
			if (rows.isEmpty()) {
						LOGGER.info("Attempt {}/{}: No profile rows found, refreshing...", attempt, maxPollingAttempts);
						driver.navigate().refresh();
						PageObjectHelper.waitForPageReady(driver, 2);
						waitForBackgroundDataLoad();
						safeSleep(pollingInterval);
						reSearchProfile();
						continue;
					}

					// Find the row with matching Job Code (search may return multiple results)
					WebElement jobRow = findRowByJobCode(rows, expectedJobCode);
					if (jobRow == null) {
						LOGGER.warn("Attempt {}/{}: Job Code '{}' not found in search results, refreshing...", 
								attempt, maxPollingAttempts, expectedJobCode);
						driver.navigate().refresh();
						PageObjectHelper.waitForPageReady(driver, 2);
						waitForBackgroundDataLoad();
						safeSleep(pollingInterval);
						reSearchProfile();
						continue;
					}
					
					List<WebElement> cells = jobRow.findElements(By.tagName("td"));
					
					long elapsedSeconds = (System.currentTimeMillis() - startTime) / 1000;
					LOGGER.info("Attempt {}/{} (elapsed: {}s): Found Job Code '{}', extracting values from UI...", 
							attempt, maxPollingAttempts, elapsedSeconds, expectedJobCode);
					
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
					
					// STRICT VALIDATION - All fields must match
					StringBuilder errors = new StringBuilder();
					int matchCount = 0;
					int totalChecks = 0;
					
					// Validate Grade (STRICT)
					if (!expectedGrade.equals("NOT_SET")) {
						totalChecks++;
						if (actualGrade.isEmpty() || actualGrade.equalsIgnoreCase("N/A") || 
							actualGrade.equals("-") || actualGrade.equalsIgnoreCase("--")) {
							errors.append(String.format("\n  ❌ Grade is empty/N/A: '%s'", actualGrade));
						} else if (!actualGrade.equalsIgnoreCase(expectedGrade)) {
							errors.append(String.format("\n  ❌ Grade MISMATCH - Expected: '%s', Actual: '%s'", 
									expectedGrade, actualGrade));
						} else {
							matchCount++;
							LOGGER.info("  ✓ Grade matches: '{}'", actualGrade);
						}
					}
					
					// Validate Department (STRICT)
					if (!expectedDept.equals("NOT_SET")) {
						totalChecks++;
						if (actualDept.isEmpty() || actualDept.equalsIgnoreCase("N/A") || 
							actualDept.equals("-") || actualDept.equalsIgnoreCase("--")) {
							errors.append(String.format("\n  ❌ Department is empty/N/A: '%s'", actualDept));
						} else if (!actualDept.equalsIgnoreCase(expectedDept)) {
							errors.append(String.format("\n  ❌ Department MISMATCH - Expected: '%s', Actual: '%s'", 
									expectedDept, actualDept));
						} else {
							matchCount++;
							LOGGER.info("  ✓ Department matches: '{}'", actualDept);
						}
					}
					
					// Validate Job Family (STRICT)
					if (!expectedFamily.equals("NOT_SET")) {
						totalChecks++;
						if (actualFunction.isEmpty() || actualFunction.equalsIgnoreCase("N/A") || 
							actualFunction.equals("-") || actualFunction.equalsIgnoreCase("--")) {
							errors.append(String.format("\n  ❌ Job Family is empty/N/A: '%s'", actualFunction));
						} else if (!actualFunction.equalsIgnoreCase(expectedFamily)) {
							errors.append(String.format("\n  ❌ Job Family MISMATCH - Expected: '%s', Actual: '%s'", 
									expectedFamily, actualFunction));
						} else {
							matchCount++;
							LOGGER.info("  ✓ Job Family matches: '{}'", actualFunction);
						}
					}
					
					// Validate Job Sub Family (STRICT)
					if (!expectedSubFamily.equals("NOT_SET")) {
						totalChecks++;
						if (actualSubFunction.isEmpty() || actualSubFunction.equalsIgnoreCase("N/A") || 
							actualSubFunction.equals("-") || actualSubFunction.equalsIgnoreCase("--")) {
							errors.append(String.format("\n  ❌ Job Sub Family is empty/N/A: '%s'", actualSubFunction));
						} else if (!actualSubFunction.equalsIgnoreCase(expectedSubFamily)) {
							errors.append(String.format("\n  ❌ Job Sub Family MISMATCH - Expected: '%s', Actual: '%s'", 
									expectedSubFamily, actualSubFunction));
						} else {
							matchCount++;
							LOGGER.info("  ✓ Job Sub Family matches: '{}'", actualSubFunction);
						}
					}
					
					// Check if ALL validations passed
					if (matchCount == totalChecks && totalChecks > 0) {
						// SUCCESS! All values are correct
						long totalWaitSeconds = (System.currentTimeMillis() - startTime) / 1000;
						long totalWaitMinutes = totalWaitSeconds / 60;
						String successMsg = String.format("✓ ALL VALUES VALIDATED after %d min %d sec (attempt %d/%d): Grade='%s', Dept='%s', Family='%s', SubFamily='%s'",
								totalWaitMinutes, totalWaitSeconds % 60, attempt, maxPollingAttempts, 
								actualGrade, actualDept, actualFunction, actualSubFunction);
						PageObjectHelper.log(LOGGER, successMsg);
						LOGGER.info("===== VALIDATION PASSED =====");
				return;
			}

					// Not all values match yet - continue polling
					if (attempt < maxPollingAttempts) {
						LOGGER.warn("Attempt {}/{}: {}/{} fields validated. Errors:{}", 
								attempt, maxPollingAttempts, matchCount, totalChecks, errors.toString());
						LOGGER.info("Waiting 20 seconds before next polling attempt...");
						safeSleep(pollingInterval);
						driver.navigate().refresh();
						PageObjectHelper.waitForPageReady(driver, 2);
						waitForBackgroundDataLoad();
						safeSleep(2000);
						reSearchProfile();
					} else {
						long totalWaitSeconds = (System.currentTimeMillis() - startTime) / 1000;
						long totalWaitMinutes = totalWaitSeconds / 60;
						String failMsg = String.format("Profile values NOT fully validated after %d min %d sec (%d attempts, %d/%d fields matched).%s",
								totalWaitMinutes, totalWaitSeconds % 60, maxPollingAttempts, matchCount, totalChecks, errors.toString());
						LOGGER.error("===== VALIDATION FAILED =====");
						Assert.fail(failMsg);
					}

				} catch (Exception retryEx) {
					if (attempt == maxPollingAttempts) {
						throw retryEx;
					}
					LOGGER.info("Attempt {}/{} encountered error, continuing polling in 20 seconds...", attempt, maxPollingAttempts);
					safeSleep(pollingInterval);
				}
			}

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_corrected_values", "Failed to verify corrected data values", e);
		}
	}

	public void verify_profile_is_not_found_in_jobs_missing_data_screen() {
		try {
			// Retry logic: Profile removal from Missing Data screen might take time
			int maxRetries = 5;
			int retryDelay = 10000; // 10 seconds
			String searchedJobCode = firstProfileJobCode.get();
			
			for (int attempt = 1; attempt <= maxRetries; attempt++) {
		try {
			PageObjectHelper.waitForPageReady(driver, 3);

			boolean profileFound = false;
			List<WebElement> rows = findElements(JOB_ROWS_MISSING_DATA);
					
					LOGGER.info("Attempt {}/{}: Checking if profile '{}' is removed from Missing Data screen ({} rows total)", 
							attempt, maxRetries, searchedJobCode, rows.size());
					
			for (WebElement row : rows) {
				String rowText = row.getText();
				if (rowText.contains(searchedJobCode)) {
					profileFound = true;
							LOGGER.info("Profile '{}' still found in row: {}", searchedJobCode, rowText.substring(0, Math.min(100, rowText.length())));
					break;
				}
			}

					if (!profileFound) {
						// Success! Profile not found
						PageObjectHelper.log(LOGGER, "✓ Profile no longer appears in Missing Data screen (verified on attempt " + attempt + ")");
						return;
					}
					
					// Profile still found
					if (attempt < maxRetries) {
						LOGGER.info("Attempt {}/{}: Profile still in Missing Data screen. Refreshing and retrying in 10 seconds...", 
								attempt, maxRetries);
						driver.navigate().refresh();
						PageObjectHelper.waitForPageReady(driver, 3);
						waitForBackgroundDataLoad(); // Wait for background data API to complete
						safeSleep(retryDelay);
						
						// Re-search for the profile
						try {
							WebElement searchInput = PageObjectHelper.waitForClickable(wait, SEARCH_INPUT_MISSING_DATA);
							searchInput.clear();
							safeSleep(500);
							searchInput.sendKeys(searchedJobCode);
							safeSleep(2000);
						} catch (Exception searchEx) {
							LOGGER.debug("Could not perform search in Missing Data screen: {}", searchEx.getMessage());
						}
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
			PageObjectHelper.handleError(LOGGER, "verify_profile_not_in_missing_data",
					"Profile still found in Missing Data screen", e);
		}
	}

	public void click_on_close_button_to_return_to_job_mapping_page() {
		try {
			PageObjectHelper.waitForPageReady(driver, 2);
			PageObjectHelper.waitForClickable(wait, CLOSE_BUTTON);
			jsClick(findElement(CLOSE_BUTTON));
			PageObjectHelper.waitForPageReady(driver, 3);
			PageObjectHelper.log(LOGGER, "Clicked Close button to return to Job Mapping page");

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_close_button", "Failed to click Close button", e);
		}
	}

	public void identify_profiles_with_missing_grade_value_in_csv() {
		try {
			List<String[]> profiles = extractedProfiles.get();
			int missingCount = 0;

			for (String[] profile : profiles) {
				if (profile.length > 5 && (profile[5] == null || profile[5].trim().isEmpty())) {
					missingCount++;
				}
			}

			PageObjectHelper.log(LOGGER, "Found " + missingCount + " profiles with missing Grade value");

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "identify_missing_grade", "Failed to identify profiles with missing Grade", e);
		}
	}

	public void identify_profiles_with_missing_department_value_in_csv() {
		try {
			List<String[]> profiles = extractedProfiles.get();
			int missingCount = 0;

			for (String[] profile : profiles) {
				if (profile.length > 2 && (profile[2] == null || profile[2].trim().isEmpty())) {
					missingCount++;
				}
			}

			PageObjectHelper.log(LOGGER, "Found " + missingCount + " profiles with missing Department value");

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "identify_missing_department", "Failed to identify profiles with missing Department", e);
		}
	}

	public void identify_all_profiles_with_any_missing_values_in_csv() {
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

			PageObjectHelper.log(LOGGER, "Found " + missingCount + " profiles with any missing values");

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "identify_all_missing", "Failed to identify profiles with missing values", e);
		}
	}

	public void fill_all_missing_values_with_appropriate_default_data() {
		try {
			fill_missing_grade_values_with_default_grade("JGL01");
			fill_missing_department_values_with_default_department("Engineering");
			fill_missing_job_family_values_with_default_value("General");
			fill_missing_job_sub_family_values_with_default_value("Operations");

			PageObjectHelper.log(LOGGER, "All missing values filled with default data");

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "fill_all_missing_values", "Failed to fill all missing values", e);
		}
	}

	public void upload_generated_csv_file() {
		try {
			String filePath = csvFilePath.get();

			if (filePath == null || filePath.isEmpty() || filePath.equals("NOT_SET")) {
				filePath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
						+ File.separator + "resources" + File.separator + DEFAULT_CSV_FILENAME;
			}

			File csvFile = new File(filePath);
			if (!csvFile.exists()) {
				throw new IOException("Generated CSV file does not exist at: " + filePath);
			}

			PageObjectHelper.log(LOGGER, "Uploading generated CSV file: " + csvFile.getName());

			PO02_AddMoreJobsFunctionality po02 = new PO02_AddMoreJobsFunctionality();
			po02.upload_file_using_browse_files_button(filePath);

			PageObjectHelper.log(LOGGER, "Successfully uploaded generated CSV file");

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "upload_generated_csv_file", "Failed to upload generated CSV file", e);
		}
	}

	public String getGeneratedCsvFilePath() {
		String filePath = csvFilePath.get();
		if (filePath == null || filePath.isEmpty() || filePath.equals("NOT_SET")) {
			filePath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
					+ File.separator + "resources" + File.separator + DEFAULT_CSV_FILENAME;
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
		csvFilePath.remove();
		searchResultsFoundCount.remove();
	}

	private String cleanNAValue(String value) {
		if (value == null || value.equalsIgnoreCase("N/A") || value.equalsIgnoreCase("NA") || value.equals("-")) {
			return "";
		}
		return value;
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

	private String escapeCsvField(String value) {
		if (value == null || value.isEmpty()) {
			return "";
		}
		value = cleanExtractedText(value);

		if (value.isEmpty()) {
			return "";
		}

		String escaped = value.replace("\"", "\"\"");
		return "\"" + escaped + "\"";
	}

	private String profileToCsvLine(String[] profile) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < profile.length; i++) {
			if (i > 0) {
				sb.append(",");
			}
			sb.append(escapeCsvField(profile[i]));
		}
		return sb.toString();
	}

	private String[] parseCsvLine(String line) {
		List<String> values = new ArrayList<>();
		StringBuilder currentValue = new StringBuilder();
		boolean insideQuotes = false;

		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);

			if (c == '"') {
				if (insideQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
					currentValue.append('"');
					i++;
				} else {
					insideQuotes = !insideQuotes;
				}
			} else if (c == ',' && !insideQuotes) {
				values.add(currentValue.toString());
				currentValue = new StringBuilder();
			} else {
				currentValue.append(c);
			}
		}
		values.add(currentValue.toString());

		return values.toArray(new String[0]);
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
			WebElement searchInput = PageObjectHelper.waitForClickable(wait, JOB_SEARCH_INPUT);
			searchInput.clear();
			safeSleep(500);
			searchInput.sendKeys(jobName);
			safeSleep(2000);
			PageObjectHelper.waitForSpinnersToDisappear(driver, 10);
		} catch (Exception e) {
			LOGGER.warn("Failed to re-search for profile: {}", e.getMessage());
		}
	}
}
