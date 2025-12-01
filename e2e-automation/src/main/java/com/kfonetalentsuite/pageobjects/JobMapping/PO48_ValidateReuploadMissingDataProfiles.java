package com.kfonetalentsuite.pageobjects.JobMapping;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.Utilities;
import com.kfonetalentsuite.utils.PageObjectHelper;
import com.kfonetalentsuite.webdriverManager.DriverManager;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

/**
 * Page Object for Feature 48: Validate Re-uploading Jobs with Missing Data
 * 
 * This class handles: - Extracting profiles from Jobs Missing Data screen -
 * Creating/reading/writing CSV files - Filling missing data in CSV -
 * Verification of re-uploaded profiles
 */
public class PO48_ValidateReuploadMissingDataProfiles {

	WebDriver driver;
	WebDriverWait wait;
	JavascriptExecutor js;
	Utilities utils = new Utilities();

	protected static final Logger LOGGER = (Logger) LogManager.getLogger();

	// THREAD-SAFE: Each thread gets its own isolated state for parallel execution
	public static ThreadLocal<Integer> missingDataCountBefore = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<Integer> missingDataCountAfter = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<Integer> totalProfilesInMissingDataScreen = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<List<String[]>> extractedProfiles = ThreadLocal.withInitial(() -> new ArrayList<>());
	public static ThreadLocal<String> firstProfileJobCode = ThreadLocal.withInitial(() -> "");
	public static ThreadLocal<String> firstProfileJobName = ThreadLocal.withInitial(() -> "");
	public static ThreadLocal<String> csvFilePath = ThreadLocal.withInitial(() -> "");
	public static ThreadLocal<Integer> searchResultsFoundCount = ThreadLocal.withInitial(() -> 0);

	// CSV file constants
	private static final String CSV_HEADER = "Client Job Code,Client Job Title,Department,Job Family,Job Sub Family,Job Grade,Is Executive";
	private static final String DEFAULT_CSV_FILENAME = "MissingDataProfiles_ToFix.csv";

	public PO48_ValidateReuploadMissingDataProfiles() throws IOException {
		this.driver = DriverManager.getDriver();
		this.wait = DriverManager.getWait();
		this.js = (JavascriptExecutor) driver;
		PageFactory.initElements(driver, this);
	}

	// ==========================================
	// XPATHS - Missing Data Screen Elements
	// ==========================================

	// NOTE: Do NOT use @CacheLookup - element becomes stale after cookie cleanup
	@FindBy(xpath = "//button[contains(text(), 'Re-upload')] | //button[contains(text(), 'upload')]")
	WebElement reuploadButton;

	// NOTE: Do NOT use @CacheLookup - element may become stale after page
	// operations
	@FindBy(xpath = "//button[contains(@class, 'border-[#007BC7]') and contains(text(), 'Close')]")
	WebElement closeButton;

	// Table rows in Missing Data screen (using [td] to only match rows with actual
	// data cells)
	@FindBy(xpath = "//table//tbody//tr[td]")
	List<WebElement> jobRowsInMissingDataScreen;

	// Total count display
	@FindBy(xpath = "//*[contains(text(), 'Organization Jobs')] | //*[contains(text(), 'profiles')]")
	WebElement totalCountDisplay;

	// Search input in Missing Data screen
	@FindBy(xpath = "//input[@placeholder='Search'] | //input[contains(@id, 'search')]")
	WebElement searchInputMissingData;

	// Job Mapping page elements
	@FindBy(xpath = "//input[@id='search-job-title-input-search-input']")
	@CacheLookup
	WebElement jobSearchInput;

	@FindBy(xpath = "//div[@id='org-job-container']//tbody//tr")
	List<WebElement> jobRowsInJobMappingPage;

	// Info message for missing data
	@FindBy(xpath = "//div[@role='button' and @aria-label='Reduced match accuracy due to missing data']")
	List<WebElement> missingDataInfoIcons;

	// Screen title element
	@FindBy(xpath = "//h1[contains(text(), 'Jobs With Missing Data')] | //h1[contains(text(), 'Missing Data')] | //*[contains(text(), 'Organization Jobs With Missing Data')]")
	WebElement missingDataScreenTitle;

	// ==========================================
	// METHODS - Missing Data Screen Navigation
	// ==========================================

	public void verify_user_is_navigated_to_jobs_with_missing_data_screen() throws IOException {
		try {
			PerformanceUtils.waitForPageReady(driver, 5);
			wait.until(ExpectedConditions.visibilityOf(missingDataScreenTitle));
			Assert.assertTrue(missingDataScreenTitle.isDisplayed(),
					"Jobs With Missing Data screen title should be displayed");
			PageObjectHelper.log(LOGGER, "Successfully navigated to Jobs With Missing Data screen");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_navigation_to_missing_data_screen",
					"Failed to verify navigation to Jobs With Missing Data screen", e);
		}
	}

	public void verify_reupload_button_is_displayed_on_jobs_missing_data_screen() throws IOException {
		try {
			PerformanceUtils.waitForPageReady(driver, 3);
			wait.until(ExpectedConditions.visibilityOf(reuploadButton));
			Assert.assertTrue(reuploadButton.isDisplayed(), "Re-upload button should be displayed");
			PageObjectHelper.log(LOGGER, "Re-upload button is displayed on Jobs Missing Data screen");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_reupload_button_is_displayed",
					"Failed to verify Re-upload button display", e);
		}
	}

	public void click_on_reupload_button_in_jobs_missing_data_screen() throws IOException {
		try {
			// Clear cookies before clicking (fixes 413 Payload Too Large error)
			driver.manage().deleteAllCookies();
			PerformanceUtils.waitForPageReady(driver, 5);
			Thread.sleep(2000);

			String reuploadBtnXpath = "//button[contains(text(), 'Re-upload')] | //button[contains(text(), 'upload')]";

			// Retry logic for clicking - element may become stale
			int maxRetries = 3;
			for (int attempt = 1; attempt <= maxRetries; attempt++) {
				try {
					// Re-find element fresh for each attempt
					WebElement reuploadBtn = wait
							.until(ExpectedConditions.elementToBeClickable(By.xpath(reuploadBtnXpath)));

					// Scroll into view
					js.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});", reuploadBtn);
					Thread.sleep(500);

					// Re-find again right before clicking to avoid stale reference
					reuploadBtn = driver.findElement(By.xpath(reuploadBtnXpath));

					// Click using JavaScript directly (more reliable)
					js.executeScript("arguments[0].click();", reuploadBtn);

					PageObjectHelper.log(LOGGER, "Clicked on Re-upload button in Jobs Missing Data screen");
					PerformanceUtils.waitForPageReady(driver, 5);
					return; // Success - exit the method

				} catch (StaleElementReferenceException e) {
					if (attempt == maxRetries) {
						throw e; // Rethrow on last attempt
					}
					Thread.sleep(1000); // Wait before retry
				}
			}
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_reupload_button", "Failed to click Re-upload button", e);
		}
	}

	// ==========================================
	// METHODS - Extract Profiles to CSV
	// ==========================================

	public void capture_total_count_of_profiles_in_jobs_missing_data_screen() throws IOException {
		try {
			PerformanceUtils.waitForPageReady(driver, 3);

			// Try to get count from the page
			int totalCount = jobRowsInMissingDataScreen.size();

			// If table is paginated, scroll and count
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

	public void extract_details_of_top_100_profiles_from_jobs_missing_data_screen() throws IOException {
		try {
			PerformanceUtils.waitForPageReady(driver, 3);
			List<String[]> profiles = new ArrayList<>();

			// IMPORTANT: Use "//table//tbody//tr[td]" to only match rows with actual data
			// cells
			// The table has alternating spacer rows without <td> elements that must be
			// skipped
			String dataRowXpath = "//table//tbody//tr[td]";
			List<WebElement> currentRows = driver.findElements(By.xpath(dataRowXpath));
			int totalAvailable = currentRows.size();
			int maxProfiles = Math.min(100, totalAvailable);

			// If less than 100 profiles, fetch all available profiles
			if (totalAvailable < 100) {
				LOGGER.info("Less than 100 profiles available ({}). Fetching ALL profiles.", totalAvailable);
				ExtentCucumberAdapter.addTestStepLog(
						"Found " + totalAvailable + " profiles (less than 100). Fetching all available profiles.");
				maxProfiles = totalAvailable;
			} else {
				LOGGER.info("100+ profiles available. Fetching top 100 profiles.");
				ExtentCucumberAdapter
						.addTestStepLog("Found " + totalAvailable + " profiles. Fetching top 100 profiles.");
			}

			LOGGER.info("Extracting details of {} profiles from Missing Data screen", maxProfiles);
			int extractedCount = 0;

			for (int i = 0; i < maxProfiles && extractedCount < 100; i++) {
				try {
					// Re-fetch rows each iteration to avoid stale element (use same xpath that
					// filters for rows with cells)
					currentRows = driver.findElements(By.xpath(dataRowXpath));
					if (i >= currentRows.size()) {
						LOGGER.warn("Row index {} exceeds available data rows {}. Stopping extraction.", i,
								currentRows.size());
						break;
					}

					WebElement row = currentRows.get(i);

					// Scroll row into view if needed (only every 10 rows to avoid slowness)
					if (i % 10 == 0 && i > 0) {
						js.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});", row);
						PerformanceUtils.safeSleep(driver, 200);
						// Re-fetch after scroll to avoid stale elements
						currentRows = driver.findElements(By.xpath(dataRowXpath));
						if (i >= currentRows.size())
							break;
						row = currentRows.get(i);
					}

					// Extract data from each column based on actual screen structure:
					// Column 0: NAME / JOB CODE (format: "Job Name (Job Code)")
					// Column 1: GRADE
					// Column 2: DEPARTMENT
					// Column 3: FUNCTION / SUB-FUNCTION (format: "Function | Sub-Function")
					List<WebElement> cells = row.findElements(By.tagName("td"));

					// With the corrected XPath, rows should always have cells, but double-check
					if (cells.size() < 4) {
						continue;
					}

					// Column 0: Parse "Job Name (Job Code)" -> extract both
					String nameJobCodeCell = cleanExtractedText(cells.get(0).getText());

					String jobCode = "";
					String jobTitle = "";

					// Job Code Rules:
					// - Job code is ALWAYS at the END of the string
					// - Job code is ALWAYS inside parentheses
					// - If string does NOT end with ")", the parentheses are part of job title
					// (data issue)
					// Example of VALID: "Cryptographer 20251128030037 (JOB48-20251128030037)"
					// Example of INVALID: "Conversational AI Designer (Chatbot Developer)
					// 20251120122114"

					if (nameJobCodeCell.endsWith(")") && nameJobCodeCell.contains("(")) {
						// String ends with ")" - extract job code from last parentheses
						int lastOpenParen = nameJobCodeCell.lastIndexOf("(");

						jobTitle = cleanExtractedText(nameJobCodeCell.substring(0, lastOpenParen));
						jobCode = cleanExtractedText(
								nameJobCodeCell.substring(lastOpenParen + 1, nameJobCodeCell.length() - 1));

					} else if (nameJobCodeCell.contains("(") && nameJobCodeCell.contains(")")) {
						// Parentheses exist but NOT at the end - they are part of the job title
						// This profile has data issue (no proper job code) - skip it
						LOGGER.warn("Row {} - Parentheses not at end. Profile has data issue. Skipping: '{}'", i + 1,
								nameJobCodeCell);
						continue;
					} else {
						// No parentheses at all - no job code available, skip this profile
						LOGGER.warn("Row {} - No job code found in parentheses. Skipping: '{}'", i + 1,
								nameJobCodeCell);
						continue;
					}

					// Column 1: GRADE (N/A means missing) - clean up quotes
					String jobGrade = cleanExtractedText(cells.get(1).getText());

					// Column 2: DEPARTMENT - clean up quotes
					String department = cleanExtractedText(cells.get(2).getText());

					// Column 3: FUNCTION / SUB-FUNCTION (format: "Function | Sub-Function")
					String functionSubFunctionCell = cleanExtractedText(cells.get(3).getText());
					String jobFamily = "";
					String jobSubFamily = "";

					// Split by "|" to get Function and Sub-Function
					if (functionSubFunctionCell.contains("|")) {
						String[] parts = functionSubFunctionCell.split("\\|", 2); // Split into max 2 parts
						jobFamily = cleanExtractedText(parts.length > 0 ? parts[0] : "");
						jobSubFamily = cleanExtractedText(parts.length > 1 ? parts[1] : "");
					} else {
						jobFamily = functionSubFunctionCell;
						jobSubFamily = "";
					}

					String isExecutive = "FALSE"; // Default value

					// Store first profile for verification later
					if (extractedCount == 0) {
						firstProfileJobCode.set(jobCode);
						firstProfileJobName.set(jobTitle);
						LOGGER.info("First profile captured - JobCode: '{}', JobTitle: '{}'", jobCode, jobTitle);
						LOGGER.info(
								"First profile details - Grade: '{}', Dept: '{}', Function: '{}', SubFunction: '{}'",
								jobGrade, department, jobFamily, jobSubFamily);
					}

					// Clean up "N/A" values to empty for CSV (N/A represents missing data)
					department = cleanNAValue(department);
					jobFamily = cleanNAValue(jobFamily);
					jobSubFamily = cleanNAValue(jobSubFamily);
					jobGrade = cleanNAValue(jobGrade);

					// CSV Format: Client Job Code, Client Job Title, Department, Job Family, Job
					// Sub Family, Job Grade, Is Executive
					profiles.add(new String[] { jobCode, jobTitle, department, jobFamily, jobSubFamily, jobGrade,
							isExecutive });
					extractedCount++;

					// Log progress every 20 rows
					if (extractedCount % 20 == 0) {
						LOGGER.info("Extracted {} of {} profiles...", extractedCount, maxProfiles);
					}

				} catch (Exception rowEx) {
					LOGGER.warn("Could not extract data from row {}: {}", i + 1, rowEx.getMessage());
				}
			}

			extractedProfiles.set(profiles);
			PageObjectHelper.log(LOGGER,
					"Successfully extracted " + profiles.size() + " profiles from Missing Data screen");
			ExtentCucumberAdapter.addTestStepLog("Extracted " + profiles.size() + " profiles for CSV export");

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "extract_top_100_profiles",
					"Failed to extract profiles from Missing Data screen", e);
		}
	}

	private String cleanNAValue(String value) {
		if (value == null || value.equalsIgnoreCase("N/A") || value.equalsIgnoreCase("NA") || value.equals("-")) {
			return "";
		}
		return value;
	}

	/**
	 * Clean extracted text by removing leading/trailing quotes and extra
	 * whitespace. This handles cases where the web page displays values with quotes
	 * like "Manager" Also handles Unicode quotes.
	 */
	private String cleanExtractedText(String value) {
		if (value == null) {
			return "";
		}
		// Trim whitespace first
		value = value.trim();

		// Define all quote-like characters to remove (standard + Unicode)
		// Standard: " ' `
		// Unicode left/right double quotes: \u201C \u201D
		// Unicode left/right single quotes: \u2018 \u2019
		// Guillemets: \u00AB \u00BB \u2039 \u203A
		String quoteChars = "\"'`\u201C\u201D\u2018\u2019\u00AB\u00BB\u2039\u203A";

		// Keep removing quotes from both ends until none remain
		boolean changed = true;
		while (changed && value.length() > 0) {
			changed = false;

			// Remove leading quote characters
			while (value.length() > 0 && quoteChars.indexOf(value.charAt(0)) >= 0) {
				value = value.substring(1);
				changed = true;
			}

			// Remove trailing quote characters
			while (value.length() > 0 && quoteChars.indexOf(value.charAt(value.length() - 1)) >= 0) {
				value = value.substring(0, value.length() - 1);
				changed = true;
			}

			// Trim any whitespace that was hidden behind quotes
			value = value.trim();
		}

		return value;
	}

	/**
	 * Properly escape a CSV field value. ALWAYS wraps in double quotes to avoid any
	 * comma/quote issues. Double quotes within the value are escaped by doubling
	 * them.
	 */
	private String escapeCsvField(String value) {
		if (value == null || value.isEmpty()) {
			return "";
		}
		// First, clean the value of any stray quotes
		value = cleanExtractedText(value);

		// If empty after cleaning, return empty
		if (value.isEmpty()) {
			return "";
		}

		// Escape any internal double quotes by doubling them
		String escaped = value.replace("\"", "\"\"");

		// ALWAYS wrap in double quotes to be safe (handles commas, special chars, etc.)
		return "\"" + escaped + "\"";
	}

	/**
	 * Convert a profile array to a properly escaped CSV line. Each field is quoted
	 * to ensure proper CSV parsing.
	 */
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

	public void create_csv_file_with_extracted_profiles_in_job_catalog_format() throws IOException {
		try {
			List<String[]> profiles = extractedProfiles.get();

			if (profiles == null || profiles.isEmpty()) {
				throw new IOException("No profiles available to create CSV file");
			}

			String filePath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
					+ File.separator + "resources" + File.separator + DEFAULT_CSV_FILENAME;

			csvFilePath.set(filePath);

			try (FileWriter writer = new FileWriter(filePath)) {
				// Write header
				writer.write(CSV_HEADER + "\n");

				// Write profile data with proper CSV escaping
				for (String[] profile : profiles) {
					writer.write(profileToCsvLine(profile) + "\n");
				}
			}

			PageObjectHelper.log(LOGGER, "CSV file created with " + profiles.size() + " profiles at: " + filePath);
			ExtentCucumberAdapter.addTestStepLog("CSV file created with " + profiles.size() + " profiles");

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "create_csv_file", "Failed to create CSV file", e);
		}
	}

	public void save_csv_file_as_in_test_resources_folder(String fileName) throws IOException {
		try {
			String filePath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
					+ File.separator + "resources" + File.separator + fileName;

			csvFilePath.set(filePath);

			// If file already exists with default name, rename it
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

	public void verify_csv_file_is_created_successfully_with_correct_headers() throws IOException {
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

	public void verify_csv_file_contains_extracted_profile_data() throws IOException {
		try {
			String filePath = csvFilePath.get();
			int lineCount = 0;

			try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
				while (reader.readLine() != null) {
					lineCount++;
				}
			}

			// Subtract 1 for header
			int dataRows = lineCount - 1;
			Assert.assertTrue(dataRows > 0, "CSV file should contain profile data");

			PageObjectHelper.log(LOGGER, "CSV file contains " + dataRows + " profile records");

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_csv_data", "Failed to verify CSV file data", e);
		}
	}

	// ==========================================
	// METHODS - Fill Missing Data in CSV
	// ==========================================

	public void csv_file_with_missing_data_profiles_exists() throws IOException {
		try {
			String filePath = csvFilePath.get();
			if (filePath == null || filePath.isEmpty()) {
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

	public void read_the_exported_csv_file_with_missing_data_profiles() throws IOException {
		try {
			String filePath = csvFilePath.get();
			List<String[]> profiles = new ArrayList<>();

			try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
				String line;
				boolean isHeader = true;

				while ((line = reader.readLine()) != null) {
					if (isHeader) {
						isHeader = false;
						continue; // Skip header
					}
					// Use proper CSV parsing that handles quoted fields
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

	/**
	 * Parse a CSV line properly handling quoted fields. Commas inside quoted fields
	 * are preserved, not treated as delimiters.
	 */
	private String[] parseCsvLine(String line) {
		List<String> values = new ArrayList<>();
		StringBuilder currentValue = new StringBuilder();
		boolean insideQuotes = false;

		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);

			if (c == '"') {
				// Check for escaped quote (double quote inside quoted field)
				if (insideQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
					currentValue.append('"');
					i++; // Skip the next quote
				} else {
					// Toggle quote state
					insideQuotes = !insideQuotes;
				}
			} else if (c == ',' && !insideQuotes) {
				// End of field
				values.add(currentValue.toString());
				currentValue = new StringBuilder();
			} else {
				currentValue.append(c);
			}
		}
		// Add the last field
		values.add(currentValue.toString());

		return values.toArray(new String[0]);
	}

	public void fill_missing_grade_values_with_default_grade(String defaultGrade) throws IOException {
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

	public void fill_missing_department_values_with_default_department(String defaultDepartment) throws IOException {
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
			PageObjectHelper.log(LOGGER,
					"Filled " + filledCount + " missing Department values with: " + defaultDepartment);

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "fill_missing_department", "Failed to fill missing Department values",
					e);
		}
	}

	public void fill_missing_job_family_values_with_default_value(String defaultValue) throws IOException {
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
			PageObjectHelper.handleError(LOGGER, "fill_missing_job_family", "Failed to fill missing Job Family values",
					e);
		}
	}

	public void fill_missing_job_sub_family_values_with_default_value(String defaultValue) throws IOException {
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
			PageObjectHelper.log(LOGGER,
					"Filled " + filledCount + " missing Job Sub Family values with: " + defaultValue);

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "fill_missing_job_sub_family",
					"Failed to fill missing Job Sub Family values", e);
		}
	}

	public void save_the_updated_csv_file_with_filled_data() throws IOException {
		try {
			String filePath = csvFilePath.get();
			List<String[]> profiles = extractedProfiles.get();

			try (FileWriter writer = new FileWriter(filePath)) {
				// Write header
				writer.write(CSV_HEADER + "\n");

				// Write updated profile data with proper CSV escaping
				for (String[] profile : profiles) {
					writer.write(profileToCsvLine(profile) + "\n");
				}
			}

			PageObjectHelper.log(LOGGER, "Updated CSV file saved with " + profiles.size() + " profiles");
			ExtentCucumberAdapter.addTestStepLog("CSV file updated with filled missing data");

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "save_updated_csv", "Failed to save updated CSV file", e);
		}
	}

	public void verify_updated_csv_file_has_no_empty_grade_values() throws IOException {
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

	public void verify_updated_csv_file_has_no_empty_department_values() throws IOException {
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
			PageObjectHelper.handleError(LOGGER, "verify_no_empty_departments", "Found empty Department values in CSV",
					e);
		}
	}

	public void verify_updated_csv_file_has_no_empty_required_fields() throws IOException {
		try {
			List<String[]> profiles = extractedProfiles.get();

			for (String[] profile : profiles) {
				Assert.assertFalse(profile[0] == null || profile[0].trim().isEmpty(), "Job Code should not be empty");
				Assert.assertFalse(profile[1] == null || profile[1].trim().isEmpty(), "Job Title should not be empty");
			}

			PageObjectHelper.log(LOGGER, "Verified all required fields are filled in CSV");

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_no_empty_required_fields",
					"Found empty required fields in CSV", e);
		}
	}

	// ==========================================
	// METHODS - Capture and Verify Counts
	// ==========================================

	public void capture_the_count_of_jobs_with_missing_data_before_reupload() throws IOException {
		try {
			// This reuses logic from PO26 - extract count from tip message
			WebElement tipMessage = driver.findElement(By.xpath("//p[contains(text(), 'jobs have missing data')]"));
			String text = tipMessage.getText();

			// Extract number using regex
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

	public void capture_the_count_of_jobs_with_missing_data_after_reupload() throws IOException {
		try {
			PerformanceUtils.waitForPageReady(driver, 3);

			WebElement tipMessage = driver.findElement(By.xpath("//p[contains(text(), 'jobs have missing data')]"));
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

	public void verify_missing_data_count_has_decreased_compared_to_before_reupload() throws IOException {
		try {
			int before = missingDataCountBefore.get();
			int after = missingDataCountAfter.get();

			Assert.assertTrue(after < before,
					"Missing data count should decrease after re-upload. Before: " + before + ", After: " + after);

			int decreased = before - after;
			PageObjectHelper.log(LOGGER, "Missing data count decreased by " + decreased + " profiles");
			ExtentCucumberAdapter.addTestStepLog("✓ Missing data count decreased from " + before + " to " + after);

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_count_decreased",
					"Missing data count did not decrease as expected", e);
		}
	}

	// ==========================================
	// METHODS - Search and Verify Profiles
	// ==========================================

	/**
	 * Wait for backend to process uploaded data and refresh Job Mapping page. Same
	 * approach as Feature 2's
	 * verify_unpublished_jobs_count_after_adding_more_jobs()
	 */
	public void wait_and_refresh_job_mapping_page_after_upload() throws IOException {
		try {
			PageObjectHelper.log(LOGGER, "Waiting for 2 minutes for backend to process uploaded data...");
			ExtentCucumberAdapter.addTestStepLog("Waiting 2 minutes for data processing...");
			Thread.sleep(120000); // 2 minutes

			PageObjectHelper.log(LOGGER, "Refreshing Job Mapping page...");
			driver.navigate().refresh();

			PerformanceUtils.waitForPageReady(driver, 5);
			PerformanceUtils.waitForSpinnersToDisappear(driver, 15);

			PageObjectHelper.log(LOGGER, "Job Mapping page refreshed successfully");
			ExtentCucumberAdapter.addTestStepLog("Page refreshed - ready for verification");

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "wait_and_refresh", "Failed to wait and refresh Job Mapping page", e);
		}
	}

	public void search_for_first_reuploaded_profile_by_job_name_from_csv() throws IOException {
		try {
			String jobName = firstProfileJobName.get();

			if (jobName == null || jobName.isEmpty()) {
				throw new IOException("No job name captured for search. Please ensure extraction step ran first.");
			}

			PageObjectHelper.log(LOGGER, "Searching for re-uploaded profile by Job Name: " + jobName);

			PerformanceUtils.waitForPageReady(driver, 3);
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);

			// Re-find search input dynamically
			WebElement searchInput = wait.until(ExpectedConditions
					.elementToBeClickable(By.xpath("//input[@id='search-job-title-input-search-input']")));
			searchInput.clear();
			Thread.sleep(500);
			searchInput.sendKeys(jobName);

			// Wait for search results to load
			Thread.sleep(2000);
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
			PerformanceUtils.waitForPageReady(driver, 3);

			PageObjectHelper.log(LOGGER, "Search completed for Job Name: " + jobName);

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "search_by_job_name", "Failed to search for re-uploaded profile", e);
		}
	}

	public void search_for_first_reuploaded_profile_by_job_code_in_missing_data_screen() throws IOException {
		try {
			String jobCode = firstProfileJobCode.get();

			if (jobCode == null || jobCode.isEmpty()) {
				throw new IOException("No job code captured for search");
			}

			PerformanceUtils.waitForPageReady(driver, 2);
			wait.until(ExpectedConditions.elementToBeClickable(searchInputMissingData));
			searchInputMissingData.clear();
			searchInputMissingData.sendKeys(jobCode);

			PerformanceUtils.waitForPageReady(driver, 3);
			PageObjectHelper.log(LOGGER, "Searched for profile by Job Code in Missing Data screen: " + jobCode);

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "search_by_job_code_missing_data",
					"Failed to search in Missing Data screen", e);
		}
	}

	public void verify_profile_is_found_in_job_mapping_search_results() throws IOException {
		try {
			PerformanceUtils.waitForPageReady(driver, 3);
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);

			// Re-find results dynamically to avoid stale elements
			String resultsXpath = "//div[@id='org-job-container']//tbody//tr[.//td[1]//input[@type='checkbox']]";
			List<WebElement> searchResults = driver.findElements(By.xpath(resultsXpath));

			int resultCount = searchResults.size();

			// Store result count for subsequent steps to use
			searchResultsFoundCount.set(resultCount);

			if (resultCount == 0) {
				String jobName = firstProfileJobName.get();
				String errorMsg = "ERROR: Profile NOT found in search results for Job Name: " + jobName
						+ ". This is a BUG - we are updating the profile, not deleting it!";
				PageObjectHelper.log(LOGGER, errorMsg);
				ExtentCucumberAdapter.addTestStepLog(errorMsg);
				Assert.fail(errorMsg);
			} else {
				PageObjectHelper.log(LOGGER,
						"✓ Profile found in Job Mapping search results (" + resultCount + " results)");
				ExtentCucumberAdapter.addTestStepLog("Profile found with " + resultCount + " search results");
			}

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_profile_found", "Error checking search results", e);
		}
	}

	public void verify_profile_does_not_display_missing_data_info_icon() throws IOException {
		try {
			// CRITICAL: First check if search results were found
			int resultsCount = searchResultsFoundCount.get();
			if (resultsCount == 0) {
				String errorMsg = "CANNOT verify missing data icon - no search results found! "
						+ "Previous step should have failed. This indicates a test logic error.";
				PageObjectHelper.log(LOGGER, errorMsg);
				Assert.fail(errorMsg);
				return;
			}

			PerformanceUtils.waitForPageReady(driver, 2);

			// Re-find rows dynamically to avoid stale elements
			String rowsXpath = "//div[@id='org-job-container']//tbody//tr[.//td[1]//input[@type='checkbox']]";
			List<WebElement> rows = driver.findElements(By.xpath(rowsXpath));

			if (rows.isEmpty()) {
				Assert.fail("No profile rows found to verify missing data icon");
				return;
			}

			// Check if the first result has missing data icon
			WebElement firstRow = rows.get(0);
			List<WebElement> infoIcons = firstRow.findElements(
					By.xpath(".//div[@role='button' and @aria-label='Reduced match accuracy due to missing data']"));

			Assert.assertTrue(infoIcons.isEmpty(), "Profile should NOT display Missing Data info icon after re-upload");

			PageObjectHelper.log(LOGGER, "✓ Profile no longer displays Missing Data info icon");
			ExtentCucumberAdapter.addTestStepLog("✓ Profile successfully fixed - no missing data warning");

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_no_missing_data_icon",
					"Profile still displays Missing Data info icon", e);
		}
	}

	public void verify_profile_displays_the_corrected_data_values() throws IOException {
		try {
			// CRITICAL: First check if search results were found
			int resultsCount = searchResultsFoundCount.get();
			if (resultsCount == 0) {
				String errorMsg = "CANNOT verify corrected data values - no search results found!";
				PageObjectHelper.log(LOGGER, errorMsg);
				Assert.fail(errorMsg);
				return;
			}

			PerformanceUtils.waitForPageReady(driver, 2);

			// Re-find rows dynamically to avoid stale elements
			String rowsXpath = "//div[@id='org-job-container']//tbody//tr[.//td[1]//input[@type='checkbox']]";
			List<WebElement> rows = driver.findElements(By.xpath(rowsXpath));

			if (rows.isEmpty()) {
				Assert.fail("No profile rows found to verify corrected data values");
				return;
			}

			WebElement firstRow = rows.get(0);
			List<WebElement> cells = firstRow.findElements(By.tagName("td"));

			// Verify Grade is not empty or N/A
			if (cells.size() > 5) {
				String gradeValue = cells.get(5).getText().trim();
				Assert.assertFalse(gradeValue.isEmpty() || gradeValue.equalsIgnoreCase("N/A"),
						"Grade value should be filled after re-upload");
				PageObjectHelper.log(LOGGER, "✓ Profile displays corrected Grade value: " + gradeValue);
			}

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_corrected_values", "Failed to verify corrected data values",
					e);
		}
	}

	public void verify_profile_is_not_found_in_jobs_missing_data_screen() throws IOException {
		try {
			PerformanceUtils.waitForPageReady(driver, 3);

			// After search, check if no results or profile not in list
			boolean profileFound = false;
			String searchedJobCode = firstProfileJobCode.get();

			for (WebElement row : jobRowsInMissingDataScreen) {
				String rowText = row.getText();
				if (rowText.contains(searchedJobCode)) {
					profileFound = true;
					break;
				}
			}

			Assert.assertFalse(profileFound, "Profile should NOT be found in Jobs Missing Data screen after re-upload");

			PageObjectHelper.log(LOGGER, "✓ Profile removed from Jobs Missing Data screen");
			ExtentCucumberAdapter.addTestStepLog("✓ Profile no longer appears in Missing Data screen");

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_profile_not_in_missing_data",
					"Profile still found in Missing Data screen", e);
		}
	}

	public void click_on_close_button_to_return_to_job_mapping_page() throws IOException {
		try {
			PerformanceUtils.waitForPageReady(driver, 2);
			wait.until(ExpectedConditions.elementToBeClickable(closeButton));
			utils.jsClick(driver, closeButton);
			PerformanceUtils.waitForPageReady(driver, 3);
			PageObjectHelper.log(LOGGER, "Clicked Close button to return to Job Mapping page");

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_close_button", "Failed to click Close button", e);
		}
	}

	// ==========================================
	// HELPER METHODS
	// ==========================================

	public void identify_profiles_with_missing_grade_value_in_csv() throws IOException {
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
			PageObjectHelper.handleError(LOGGER, "identify_missing_grade",
					"Failed to identify profiles with missing Grade", e);
		}
	}

	public void identify_profiles_with_missing_department_value_in_csv() throws IOException {
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
			PageObjectHelper.handleError(LOGGER, "identify_missing_department",
					"Failed to identify profiles with missing Department", e);
		}
	}

	public void identify_all_profiles_with_any_missing_values_in_csv() throws IOException {
		try {
			List<String[]> profiles = extractedProfiles.get();
			int missingCount = 0;

			for (String[] profile : profiles) {
				boolean hasMissing = false;
				for (int i = 2; i < Math.min(profile.length, 6); i++) { // Check columns 2-5
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
			PageObjectHelper.handleError(LOGGER, "identify_all_missing",
					"Failed to identify profiles with missing values", e);
		}
	}

	public void fill_all_missing_values_with_appropriate_default_data() throws IOException {
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

	// ==========================================
	// METHODS - Upload Generated CSV File
	// ==========================================

	/**
	 * Upload the generated CSV file (MissingDataProfiles_ToFix.csv) using the
	 * existing upload mechanism This method delegates to PO02's upload method with
	 * the custom file path
	 */
	public void upload_generated_csv_file() throws IOException {
		try {
			String filePath = csvFilePath.get();

			if (filePath == null || filePath.isEmpty()) {
				// Default to the expected file name
				filePath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
						+ File.separator + "resources" + File.separator + DEFAULT_CSV_FILENAME;
			}

			File csvFile = new File(filePath);
			if (!csvFile.exists()) {
				throw new IOException("Generated CSV file does not exist at: " + filePath);
			}

			PageObjectHelper.log(LOGGER, "Uploading generated CSV file: " + csvFile.getName());

			// Use PO02's upload method with the custom file path
			PO02_ValidateAddMoreJobsFunctionality po02 = new PO02_ValidateAddMoreJobsFunctionality();
			po02.upload_file_using_browse_files_button(filePath);

			PageObjectHelper.log(LOGGER, "Successfully uploaded generated CSV file");

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "upload_generated_csv_file", "Failed to upload generated CSV file", e);
		}
	}

	/**
	 * Get the path of the generated CSV file
	 */
	public String getGeneratedCsvFilePath() {
		String filePath = csvFilePath.get();
		if (filePath == null || filePath.isEmpty()) {
			filePath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
					+ File.separator + "resources" + File.separator + DEFAULT_CSV_FILENAME;
		}
		return filePath;
	}

	/**
	 * Reset all ThreadLocal variables - called from SuiteHooks
	 */
	public static void resetThreadLocals() {
		missingDataCountBefore.remove();
		missingDataCountAfter.remove();
		totalProfilesInMissingDataScreen.remove();
		extractedProfiles.remove();
		firstProfileJobCode.remove();
		firstProfileJobName.remove();
		csvFilePath.remove();
	}
}
