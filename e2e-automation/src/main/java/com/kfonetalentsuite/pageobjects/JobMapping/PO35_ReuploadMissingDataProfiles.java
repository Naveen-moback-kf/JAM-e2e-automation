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
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.PageObjectHelper;


public class PO35_ReuploadMissingDataProfiles extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO35_ReuploadMissingDataProfiles.class);

	// ==================== THREAD-SAFE STATE ====================
	public static ThreadLocal<Integer> missingDataCountBefore = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<Integer> missingDataCountAfter = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<Integer> totalProfilesInMissingDataScreen = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<List<String[]>> extractedProfiles = ThreadLocal.withInitial(ArrayList::new);
	public static ThreadLocal<String> firstProfileJobCode = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<String> firstProfileJobName = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<String> csvFilePath = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<Integer> searchResultsFoundCount = ThreadLocal.withInitial(() -> 0);

	// ==================== CONSTANTS ====================
	private static final String CSV_HEADER = "Client Job Code,Client Job Title,Department,Job Family,Job Sub Family,Job Grade,Is Executive";
	private static final String DEFAULT_CSV_FILENAME = "MissingDataProfiles_ToFix.csv";

	// ==================== LOCATORS ====================
	private static final By REUPLOAD_BUTTON = By.xpath("//button[contains(text(), 'Re-upload')] | //button[contains(text(), 'upload')]");
	private static final By CLOSE_BUTTON = By.xpath("//button[contains(@class, 'border-[#007BC7]') and contains(text(), 'Close')]");
	private static final By JOB_ROWS_MISSING_DATA = By.xpath("//table//tbody//tr[td]");
	private static final By SEARCH_INPUT_MISSING_DATA = By.xpath("//input[@placeholder='Search'] | //input[contains(@id, 'search')]");
	private static final By JOB_SEARCH_INPUT = By.xpath("//input[@id='search-job-title-input-search-input']");
	private static final By MISSING_DATA_SCREEN_TITLE = By.xpath("//h1[contains(text(), 'Jobs With Missing Data')] | //h1[contains(text(), 'Missing Data')] | //*[contains(text(), 'Organization Jobs With Missing Data')]");
	private static final By TIP_MESSAGE = By.xpath("//p[contains(text(), 'jobs have missing data')]");
	private static final By SEARCH_RESULTS_ROWS = By.xpath("//div[@id='org-job-container']//tbody//tr[.//td[1]//input[@type='checkbox']]");

	public PO35_ReuploadMissingDataProfiles() {
		super();
	}

	// ==================== NAVIGATION METHODS ====================

	public void verify_user_is_navigated_to_jobs_with_missing_data_screen() {
		try {
			PerformanceUtils.waitForPageReady(driver, 5);
			wait.until(ExpectedConditions.visibilityOfElementLocated(MISSING_DATA_SCREEN_TITLE));
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
			PerformanceUtils.waitForPageReady(driver, 3);
			wait.until(ExpectedConditions.visibilityOfElementLocated(REUPLOAD_BUTTON));
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
			PerformanceUtils.waitForPageReady(driver, 5);
			safeSleep(2000);

			int maxRetries = 3;
			for (int attempt = 1; attempt <= maxRetries; attempt++) {
				try {
					WebElement reuploadBtn = wait.until(ExpectedConditions.elementToBeClickable(REUPLOAD_BUTTON));
					scrollToElement(reuploadBtn);
					safeSleep(500);

					reuploadBtn = findElement(REUPLOAD_BUTTON);
					jsClick(reuploadBtn);

					PageObjectHelper.log(LOGGER, "Clicked on Re-upload button in Jobs Missing Data screen");
					PerformanceUtils.waitForPageReady(driver, 5);
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

	// ==================== EXTRACT PROFILES METHODS ====================

	public void capture_total_count_of_profiles_in_jobs_missing_data_screen() {
		try {
			PerformanceUtils.waitForPageReady(driver, 3);
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

	public void extract_details_of_top_100_profiles_from_jobs_missing_data_screen() {
		try {
			PerformanceUtils.waitForPageReady(driver, 3);
			List<String[]> profiles = new ArrayList<>();

			List<WebElement> currentRows = findElements(JOB_ROWS_MISSING_DATA);
			int totalAvailable = currentRows.size();
			int maxProfiles = Math.min(100, totalAvailable);

			if (totalAvailable < 100) {
				PageObjectHelper.log(LOGGER, "Found " + totalAvailable + " profiles (less than 100). Fetching all available profiles.");
			} else {
				PageObjectHelper.log(LOGGER, "Found " + totalAvailable + " profiles. Fetching top 100 profiles.");
			}

			LOGGER.info("Extracting details of {} profiles from Missing Data screen", maxProfiles);
			int extractedCount = 0;

			for (int i = 0; i < maxProfiles && extractedCount < 100; i++) {
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

					if (extractedCount % 20 == 0) {
						LOGGER.info("Extracted {} of {} profiles...", extractedCount, maxProfiles);
					}

				} catch (Exception rowEx) {
					LOGGER.warn("Could not extract data from row {}: {}", i + 1, rowEx.getMessage());
				}
			}

			extractedProfiles.set(profiles);
			PageObjectHelper.log(LOGGER, "Successfully extracted " + profiles.size() + " profiles from Missing Data screen");

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "extract_top_100_profiles",
					"Failed to extract profiles from Missing Data screen", e);
		}
	}

	// ==================== CSV FILE METHODS ====================

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

	// ==================== FILL MISSING DATA METHODS ====================

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

			for (String[] profile : profiles) {
				if (profile.length > 5 && (profile[5] == null || profile[5].trim().isEmpty())) {
					profile[5] = defaultGrade;
					filledCount++;
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

			for (String[] profile : profiles) {
				if (profile.length > 2 && (profile[2] == null || profile[2].trim().isEmpty())) {
					profile[2] = defaultDepartment;
					filledCount++;
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

			for (String[] profile : profiles) {
				if (profile.length > 3 && (profile[3] == null || profile[3].trim().isEmpty())) {
					profile[3] = defaultValue;
					filledCount++;
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

			for (String[] profile : profiles) {
				if (profile.length > 4 && (profile[4] == null || profile[4].trim().isEmpty())) {
					profile[4] = defaultValue;
					filledCount++;
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

	// ==================== COUNT CAPTURE/VERIFY METHODS ====================

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
			PerformanceUtils.waitForPageReady(driver, 3);

			WebElement tipMessage = findElement(TIP_MESSAGE);
			String text = tipMessage.getText();

			java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("(\\d+)\\s+jobs");
			java.util.regex.Matcher matcher = pattern.matcher(text);

			if (matcher.find()) {
				int count = Integer.parseInt(matcher.group(1));
				missingDataCountAfter.set(count);
				PageObjectHelper.log(LOGGER, "Missing data count AFTER re-upload: " + count);
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

	// ==================== SEARCH/VERIFY METHODS ====================

	public void wait_and_refresh_job_mapping_page_after_upload() {
		try {
			PageObjectHelper.log(LOGGER, "Waiting 2 minutes for data processing...");
			safeSleep(120000);

			PageObjectHelper.log(LOGGER, "Refreshing Job Mapping page...");
			driver.navigate().refresh();

			PerformanceUtils.waitForPageReady(driver, 5);
			PerformanceUtils.waitForSpinnersToDisappear(driver, 15);

			PageObjectHelper.log(LOGGER, "Page refreshed - ready for verification");

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

			PerformanceUtils.waitForPageReady(driver, 3);
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);

			WebElement searchInput = wait.until(ExpectedConditions.elementToBeClickable(JOB_SEARCH_INPUT));
			searchInput.clear();
			safeSleep(500);
			searchInput.sendKeys(jobName);

			safeSleep(2000);
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 3);

			PageObjectHelper.log(LOGGER, "Search completed for Job Name: " + jobName);

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

			PerformanceUtils.waitForPageReady(driver, 2);
			WebElement searchInput = wait.until(ExpectedConditions.elementToBeClickable(SEARCH_INPUT_MISSING_DATA));
			searchInput.clear();
			searchInput.sendKeys(jobCode);

			PerformanceUtils.waitForPageReady(driver, 3);
			PageObjectHelper.log(LOGGER, "Searched for profile by Job Code in Missing Data screen: " + jobCode);

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "search_by_job_code_missing_data",
					"Failed to search in Missing Data screen", e);
		}
	}

	public void verify_profile_is_found_in_job_mapping_search_results() {
		try {
			PerformanceUtils.waitForPageReady(driver, 3);
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);

			List<WebElement> searchResults = findElements(SEARCH_RESULTS_ROWS);
			int resultCount = searchResults.size();

			searchResultsFoundCount.set(resultCount);

			if (resultCount == 0) {
				String jobName = firstProfileJobName.get();
				String errorMsg = "ERROR: Profile NOT found in search results for Job Name: " + jobName;
				PageObjectHelper.log(LOGGER, errorMsg);
				Assert.fail(errorMsg);
			} else {
				PageObjectHelper.log(LOGGER, "✓ Profile found in Job Mapping search results (" + resultCount + " results)");
			}

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_profile_found", "Error checking search results", e);
		}
	}

	public void verify_profile_does_not_display_missing_data_info_icon() {
		try {
			int resultsCount = searchResultsFoundCount.get();
			if (resultsCount == 0) {
				String errorMsg = "CANNOT verify missing data icon - no search results found!";
				PageObjectHelper.log(LOGGER, errorMsg);
				Assert.fail(errorMsg);
				return;
			}

			PerformanceUtils.waitForPageReady(driver, 2);

			List<WebElement> rows = findElements(SEARCH_RESULTS_ROWS);
			if (rows.isEmpty()) {
				Assert.fail("No profile rows found to verify missing data icon");
				return;
			}

			WebElement firstRow = rows.get(0);
			List<WebElement> infoIcons = firstRow.findElements(
					By.xpath(".//div[@role='button' and @aria-label='Reduced match accuracy due to missing data']"));

			Assert.assertTrue(infoIcons.isEmpty(), "Profile should NOT display Missing Data info icon after re-upload");

			PageObjectHelper.log(LOGGER, "✓ Profile successfully fixed - no missing data warning");

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_no_missing_data_icon",
					"Profile still displays Missing Data info icon", e);
		}
	}

	public void verify_profile_displays_the_corrected_data_values() {
		try {
			int resultsCount = searchResultsFoundCount.get();
			if (resultsCount == 0) {
				String errorMsg = "CANNOT verify corrected data values - no search results found!";
				PageObjectHelper.log(LOGGER, errorMsg);
				Assert.fail(errorMsg);
				return;
			}

			PerformanceUtils.waitForPageReady(driver, 2);

			List<WebElement> rows = findElements(SEARCH_RESULTS_ROWS);
			if (rows.isEmpty()) {
				Assert.fail("No profile rows found to verify corrected data values");
				return;
			}

			WebElement firstRow = rows.get(0);
			List<WebElement> cells = firstRow.findElements(By.tagName("td"));

			if (cells.size() > 5) {
				String gradeValue = cells.get(5).getText().trim();
				Assert.assertFalse(gradeValue.isEmpty() || gradeValue.equalsIgnoreCase("N/A"),
						"Grade value should be filled after re-upload");
				PageObjectHelper.log(LOGGER, "✓ Profile displays corrected Grade value: " + gradeValue);
			}

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_corrected_values", "Failed to verify corrected data values", e);
		}
	}

	public void verify_profile_is_not_found_in_jobs_missing_data_screen() {
		try {
			PerformanceUtils.waitForPageReady(driver, 3);

			boolean profileFound = false;
			String searchedJobCode = firstProfileJobCode.get();

			List<WebElement> rows = findElements(JOB_ROWS_MISSING_DATA);
			for (WebElement row : rows) {
				String rowText = row.getText();
				if (rowText.contains(searchedJobCode)) {
					profileFound = true;
					break;
				}
			}

			Assert.assertFalse(profileFound, "Profile should NOT be found in Jobs Missing Data screen after re-upload");

			PageObjectHelper.log(LOGGER, "✓ Profile no longer appears in Missing Data screen");

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_profile_not_in_missing_data",
					"Profile still found in Missing Data screen", e);
		}
	}

	public void click_on_close_button_to_return_to_job_mapping_page() {
		try {
			PerformanceUtils.waitForPageReady(driver, 2);
			wait.until(ExpectedConditions.elementToBeClickable(CLOSE_BUTTON));
			jsClick(findElement(CLOSE_BUTTON));
			PerformanceUtils.waitForPageReady(driver, 3);
			PageObjectHelper.log(LOGGER, "Clicked Close button to return to Job Mapping page");

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_close_button", "Failed to click Close button", e);
		}
	}

	// ==================== HELPER METHODS ====================

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
		totalProfilesInMissingDataScreen.remove();
		extractedProfiles.remove();
		firstProfileJobCode.remove();
		firstProfileJobName.remove();
		csvFilePath.remove();
		searchResultsFoundCount.remove();
	}

	// ==================== PRIVATE HELPER METHODS ====================

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
}
