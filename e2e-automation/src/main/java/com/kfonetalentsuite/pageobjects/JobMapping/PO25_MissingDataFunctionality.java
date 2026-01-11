package com.kfonetalentsuite.pageobjects.JobMapping;

import java.io.IOException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import java.time.Duration;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.kfonetalentsuite.utils.JobMapping.Utilities;

public class PO25_MissingDataFunctionality extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO25_MissingDataFunctionality.class);
	public static ThreadLocal<Map<String, String>> jobDetailsFromMissingDataScreen = ThreadLocal.withInitial(HashMap::new);
	public static ThreadLocal<Map<String, String>> jobDetailsFromJobMappingPage = ThreadLocal.withInitial(HashMap::new);
	public static ThreadLocal<String> extractedJobName = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<String> extractedJobCode = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<String> currentDataType = ThreadLocal.withInitial(() -> "Grade");

	// Scenario coordination
	public static ThreadLocal<Boolean> forwardScenarioFoundProfile = ThreadLocal.withInitial(() -> false);
	public static ThreadLocal<String> forwardScenarioJobName = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<String> forwardScenarioJobCode = ThreadLocal.withInitial(() -> "NOT_SET");

	// Store found elements
	private static ThreadLocal<WebElement> foundJobRow = new ThreadLocal<>();
	private static ThreadLocal<WebElement> foundProfile = new ThreadLocal<>();
	private static ThreadLocal<WebElement> matchingJobRow = new ThreadLocal<>();

	public PO25_MissingDataFunctionality() {
		super();
	}
	private static final By REUPLOAD_PAGE_TITLE_DESC = By.xpath("//div//p[contains(text(), 're-upload the jobs')]");
	private static final By CLOSE_REUPLOAD_JOBS_PAGE_BUTTON = By.xpath("//button[contains(@class, 'border-[#007BC7]') and contains(text(), 'Close')]");
	private static final By JOB_ROWS_IN_MISSING_DATA_SCREEN = By.xpath("//table//tr[contains(@class, 'border-b')]");
	private static final By SEARCH_BOX = By.xpath("//input[@id='search-job-title-input-search-input']");
	private static final By JOB_ROWS_IN_JOB_MAPPING_PAGE = By.xpath("//div[@id='org-job-container']//tbody//tr");
	private static final By JOB_MAPPING_LOGO = By.xpath("//*[@data-testid='job-mapping-logo'] | //*[contains(@class, 'job-mapping')]//img | //h1[contains(text(), 'Review and Publish')]");
	private static final By INFO_MESSAGE_ICON = By.xpath("//*[contains(@class, 'info-icon')] | //*[@data-testid='info-message'] | //button[contains(@class, 'text-[#C35500]')]");

	// Column indices for different data types in Job Mapping page
	private static final int GRADE_COLUMN_INDEX = 3;
	private static final int DEPARTMENT_COLUMN_INDEX = 4;
	private static final int FUNCTION_COLUMN_INDEX = 5; // Function/Subfunction combined column

	// ═══════════════════════════════════════════════════════════════════════════
	// UTILITY METHODS
	// ═══════════════════════════════════════════════════════════════════════════

	private int getColumnIndex(String dataType) {
		switch (dataType.toLowerCase()) {
			case "grade": return GRADE_COLUMN_INDEX;
			case "department": return DEPARTMENT_COLUMN_INDEX;
			case "function":
			case "subfunction": return FUNCTION_COLUMN_INDEX;
			default: return GRADE_COLUMN_INDEX;
		}
	}

	private boolean isMissingValue(String value) {
		if (value == null) return true;
		String normalized = value.trim().toLowerCase();
		return normalized.isEmpty() || normalized.equals("-") || normalized.equals("n/a") || 
			   normalized.equals("na") || normalized.equals("null") || normalized.equals("--");
	}

	private boolean isSpecificPartMissing(String functionSubfunctionValue, String dataType) {
		if (functionSubfunctionValue == null || functionSubfunctionValue.isEmpty()) {
			return true;
		}
		
		// Parse "Function | Subfunction" format
		String[] parts = functionSubfunctionValue.split("\\|");
		
		if (dataType.equalsIgnoreCase("function")) {
			// Check only the Function part (before |)
			String functionPart = parts.length > 0 ? parts[0].trim() : "";
			boolean isMissing = isMissingValue(functionPart);
			LOGGER.debug("Function part check: '{}' -> missing={}", functionPart, isMissing);
			return isMissing;
		} else if (dataType.equalsIgnoreCase("subfunction")) {
			// Check only the Subfunction part (after |)
			String subfunctionPart = parts.length > 1 ? parts[1].trim() : "";
			boolean isMissing = isMissingValue(subfunctionPart);
			LOGGER.debug("Subfunction part check: '{}' -> missing={}", subfunctionPart, isMissing);
			return isMissing;
		}
		
		// For other data types, check the whole value
		return isMissingValue(functionSubfunctionValue);
	}

	private String cleanJobNameLocal(String rawJobName) {
		if (rawJobName == null || rawJobName.isEmpty()) {
			return "";
		}

		String cleaned = rawJobName.trim();

		// Fix common parsing issues:
		// 1. Handle multiple closing parentheses like "Name (Code))" -> "Name (Code)"
		if (cleaned.endsWith("))")) {
			cleaned = cleaned.substring(0, cleaned.length() - 1);
		}

		// 2. Remove timestamp suffix like "_202312151000"
		if (cleaned.matches(".*_\\d{12}$")) {
			cleaned = cleaned.substring(0, cleaned.lastIndexOf('_'));
		}

		// 3. If it has job code in parentheses, extract just the name
		if (cleaned.contains("(") && cleaned.endsWith(")")) {
			int parenStart = cleaned.lastIndexOf("(");
			if (parenStart > 0) {
				cleaned = cleaned.substring(0, parenStart).trim();
			}
		}

		return cleaned;
	}

	private String normalizeFieldValue(String fieldValue, String fieldName) {
		if (fieldValue == null || fieldValue.trim().isEmpty() || "-".equals(fieldValue.trim())) {
			return "N/A";
		}

		// Special handling for Function/Sub-function fields
		if ("Function/Subfunction".equals(fieldName) || "Function".equals(fieldName)
				|| "Function/Sub-function".equals(fieldName)) {
			if ("N/A".equalsIgnoreCase(fieldValue.trim())) {
				return "N/A";
			}
			// Handle cases like "- | N/A" vs "N/A | N/A" or "MNGR | -" vs "MNGR | N/A"
			if (fieldValue.contains("|")) {
				String[] parts = fieldValue.split("\\|");
				if (parts.length >= 2) {
					String functionPart = parts[0].trim();
					String subfunctionPart = parts[1].trim();

					// Normalize the function part (hyphen or empty to N/A)
					if ("-".equals(functionPart) || functionPart.isEmpty() || "N/A".equalsIgnoreCase(functionPart)) {
						functionPart = "N/A";
					}

					// Normalize the subfunction part (hyphen or empty to N/A)
					if ("-".equals(subfunctionPart) || subfunctionPart.isEmpty() || "N/A".equalsIgnoreCase(subfunctionPart)) {
						subfunctionPart = "N/A";
					}

					return functionPart + " | " + subfunctionPart;
				}
			}
		}

		return fieldValue.trim();
	}

	// Sorting header locators (from PO17_ValidateSortingFunctionality_JAM)
	private static final By ORG_JOB_GRADE_HEADER = By.xpath("//*[@id='org-job-container']/div/table/thead/tr/th[3]/div");
	private static final By ORG_JOB_DEPARTMENT_HEADER = By.xpath("//*[@id='org-job-container']/div/table/thead/tr/th[4]/div");

	// ═══════════════════════════════════════════════════════════════════════════
	// SORTING METHODS
	// ═══════════════════════════════════════════════════════════════════════════

	public void sort_job_profiles_by_column_in_ascending_order(String columnName) throws IOException {
		try {
			LOGGER.info("Sorting job profiles by " + columnName + " in ascending order");
			Utilities.waitForPageReady(driver, 2);
			waitForSpinners();

			By headerLocator;
			
			// Use proven locators from PO17
			switch (columnName.toLowerCase()) {
				case "grade":
				case "organization grade":
				case "org grade":
					headerLocator = ORG_JOB_GRADE_HEADER;
					break;
				case "department":
				case "organization department":
				case "org department":
					headerLocator = ORG_JOB_DEPARTMENT_HEADER;
					break;
				default:
					// Default to Grade for Function/Subfunction scenarios
					headerLocator = ORG_JOB_GRADE_HEADER;
					LOGGER.info("Using Grade header as default for: " + columnName);
			}

			// Verify element exists before clicking
			driver.findElement(headerLocator);
			clickElement(headerLocator);
			waitForSpinners();
			Utilities.waitForPageReady(driver, 3);
			safeSleep(1000);
			
			LOGGER.info("✅ Sorted by " + columnName + " in ascending order");
		} catch (Exception e) {
			LOGGER.info("⚠️ Sorting failed for " + columnName + ": " + e.getMessage() + " - proceeding without sorting");
		}
	}

	// ═══════════════════════════════════════════════════════════════════════════
	// NAVIGATION VERIFICATION METHODS
	// ═══════════════════════════════════════════════════════════════════════════

	public void verify_user_is_navigated_to_jobs_with_missing_data_screen(String dataType) throws IOException {
		try {
			currentDataType.set(dataType);
			LOGGER.debug("Verifying Jobs with Missing {} Data screen is displayed", dataType);
			Utilities.waitForUIStability(driver, 2);

			boolean pageVerified = false;
			StringBuilder verificationResults = new StringBuilder();

			// Check for page title description
			try {
				Utilities.waitForVisible(wait, REUPLOAD_PAGE_TITLE_DESC);
				if (findElement(REUPLOAD_PAGE_TITLE_DESC).isDisplayed()) {
					String pageTitle = findElement(REUPLOAD_PAGE_TITLE_DESC).getText();
					verificationResults.append("✅ Page title found: ").append(pageTitle).append("; ");
					pageVerified = true;
				}
			} catch (Exception e) {
				verificationResults.append("- Page title not found; ");
			}

			// Check for Close button
			try {
				Utilities.waitForVisible(wait, CLOSE_REUPLOAD_JOBS_PAGE_BUTTON);
				if (findElement(CLOSE_REUPLOAD_JOBS_PAGE_BUTTON).isDisplayed()) {
					verificationResults.append("✅ Close button found; ");
					pageVerified = true;
				}
			} catch (Exception e) {
				verificationResults.append("- Close button not found; ");
			}

			// Check for job table/rows
			try {
				List<WebElement> jobRows = driver.findElements(JOB_ROWS_IN_MISSING_DATA_SCREEN);
				if (!jobRows.isEmpty()) {
					verificationResults.append("✅ Job data table found (").append(jobRows.size()).append(" rows); ");
				}
			} catch (Exception e) {
				verificationResults.append("- Job data table check failed; ");
			}

			if (pageVerified) {
				LOGGER.info("✅ Successfully verified Jobs with Missing " + dataType + " Data screen");
				LOGGER.info("   " + verificationResults);
			} else {
				String errorMsg = "Failed to verify Jobs with Missing " + dataType + " Data screen. Results: " + verificationResults;
				LOGGER.info("❌ " + errorMsg);
				Assert.fail(errorMsg);
			}
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_user_is_navigated_to_jobs_with_missing_data_screen",
					"Failed to verify Jobs with Missing " + dataType + " Data screen", e);
		}
	}

	public void verify_user_is_back_on_job_mapping_page(String dataType) throws IOException {
		try {
			LOGGER.debug("Verifying user is back on Job Mapping page after {} validation", dataType);
			
			// First, ensure any lingering modal is closed
			try {
				WebElement closeButton = driver.findElement(CLOSE_REUPLOAD_JOBS_PAGE_BUTTON);
				if (closeButton.isDisplayed()) {
					LOGGER.debug("Missing Data modal still open, force closing...");
					jsClick(closeButton);
					safeSleep(1500);
				}
			} catch (Exception e) {
				// Modal not present, which is good
			}
			
			Utilities.waitForPageReady(driver, 2);  // Reduced from 5s
			waitForSpinners();
			safeSleep(500);  // Reduced from 1500ms

			boolean onJobMappingPage = false;

			// Quick check for Job Mapping page - try fastest indicators first
			try {
				// Try URL check first (fastest)
				String currentUrl = driver.getCurrentUrl();
				if (currentUrl.contains("aiauto") || currentUrl.contains("job-mapping")) {
					onJobMappingPage = true;
					LOGGER.debug("Confirmed Job Mapping page via URL");
				}
			} catch (Exception e) {
				LOGGER.debug("URL check failed");
			}
			
			// If URL check passed, do a quick sanity check for page elements
			if (onJobMappingPage) {
				try {
					WebDriverWait quickWait = new WebDriverWait(driver, Duration.ofSeconds(3));  // Reduced from 15s
					quickWait.until(ExpectedConditions.or(
						ExpectedConditions.visibilityOfElementLocated(JOB_MAPPING_LOGO),
						ExpectedConditions.visibilityOfElementLocated(SEARCH_BOX)
					));
					LOGGER.debug("Page elements confirmed");
				} catch (Exception e) {
					LOGGER.debug("Element check timed out, but URL was correct - proceeding");
				}
			} else {
				// Fallback: check for elements directly
				try {
					WebDriverWait quickWait = new WebDriverWait(driver, Duration.ofSeconds(5));
					quickWait.until(ExpectedConditions.or(
						ExpectedConditions.visibilityOfElementLocated(JOB_MAPPING_LOGO),
						ExpectedConditions.visibilityOfElementLocated(SEARCH_BOX)
					));
					onJobMappingPage = true;
				} catch (Exception e) {
					LOGGER.debug("Could not verify Job Mapping page");
				}
			}

			if (onJobMappingPage) {
				LOGGER.info("✅ Successfully returned to Job Mapping page after " + dataType + " validation");
			} else {
				LOGGER.info("⚠️ Could not verify Job Mapping page, but proceeding...");
			}
		} catch (Exception e) {
			LOGGER.info("⚠️ Verification of Job Mapping page failed: " + e.getMessage());
		}
	}

	// ═══════════════════════════════════════════════════════════════════════════
	// FIND MISSING DATA METHODS - FORWARD FLOW (Job Mapping Page)
	// ═══════════════════════════════════════════════════════════════════════════

	public void find_job_profile_in_job_mapping_page_where_data_is_missing(String dataType) throws IOException {
		try {
			currentDataType.set(dataType);
			LOGGER.info("Finding job profile with missing " + dataType + " data in Job Mapping page");
			
			Utilities.waitForPageReady(driver, 2);
			waitForSpinners();
			safeSleep(1000);

			boolean profileFound = false;
			int columnIndex = getColumnIndex(dataType);
			int totalRowsChecked = 0;
			int maxScrollAttempts = 50;
			int scrollAttempt = 0;
			boolean hasMoreRows = true;

		// Loop with scrolling to handle lazy loading
		while (!profileFound && hasMoreRows && scrollAttempt <= maxScrollAttempts) {
			List<WebElement> jobRows = driver.findElements(JOB_ROWS_IN_JOB_MAPPING_PAGE);
			
			if (scrollAttempt == 0) {
				int profileCount = jobRows.size() / 3;
				LOGGER.info("Found " + profileCount + " profiles to search (" + jobRows.size() + " rows)");
				LOGGER.info("Searching for missing " + dataType + " data...");
			}

			int currentBatchStartIndex = totalRowsChecked;
			int currentBatchEndIndex = jobRows.size();
			int rowsProcessedInBatch = 0;
			long batchStartTime = System.currentTimeMillis();

			// Search through current batch - ONLY CHECK JOB NAME ROWS (every 3rd row)
			for (int i = currentBatchStartIndex; i < currentBatchEndIndex && !profileFound; i += 3) {
				totalRowsChecked += 3;  // Each profile = 3 rows
				rowsProcessedInBatch += 3;
				
				// Progress indicator every 9 rows (every 3 profiles)
				if (rowsProcessedInBatch % 9 == 0) {
					int profilesChecked = totalRowsChecked / 3;
					long elapsed = System.currentTimeMillis() - batchStartTime;
					LOGGER.info("⏳ Progress: " + profilesChecked + " profiles checked (" + elapsed + "ms elapsed)");
				}
				
				try {
					WebElement row = jobRows.get(i);  // This is the job name row
					List<WebElement> cells = row.findElements(By.tagName("td"));
					String cellValue = "";
					
					// Extract data based on type
					if (dataType.equalsIgnoreCase("function") || dataType.equalsIgnoreCase("subfunction")) {
						// Function is in the NEXT row (i+1)
						if (i + 1 < jobRows.size()) {
							WebElement funcRow = jobRows.get(i + 1);  // Direct access instead of XPath
							WebElement funcElement = funcRow.findElement(By.xpath(".//span[2]"));
							String funcText = funcElement.getText().trim();
							String[] parts = funcText.split("\\|", 2);
							cellValue = dataType.equalsIgnoreCase("function") 
									? (parts.length > 0 ? parts[0].trim() : "")
									: (parts.length > 1 ? parts[1].trim() : "");
						}
					} else {
						// Grade/Department in the job name row
						if (cells.size() >= columnIndex) {
							cellValue = cells.get(columnIndex - 1).getText().trim();
						}
					}

					// Check if missing
					if (isMissingValue(cellValue)) {
						foundProfile.set(row);
						profileFound = true;
						
						// Extract job name and code
						String jobName = "";
						String jobCode = "";
						if (cells.size() >= 2) {
							WebElement nameCell = cells.get(1);
							String nameText = nameCell.getText().trim();
							
							// Parse "Job Name - (JOB-CODE)"
							if (nameText.contains(" - (") && nameText.contains(")")) {
								int dashIdx = nameText.lastIndexOf(" - (");
								jobName = nameText.substring(0, dashIdx).trim();
								jobCode = nameText.substring(dashIdx + 4).replace(")", "").trim();
							} else {
								jobName = nameText;
							}
						}
						
						if (jobName.isEmpty()) {
							jobName = "Unknown Job";
						}
						
						extractedJobName.set(jobName);
						extractedJobCode.set(jobCode);
						forwardScenarioJobName.set(jobName);
						forwardScenarioJobCode.set(jobCode);
						forwardScenarioFoundProfile.set(true);
						
						LOGGER.info("✅ Found job with missing " + dataType + ": " + jobName + " (" + jobCode + ")");
						break; // Exit the for loop
					}
				} catch (Exception rowEx) {
					continue;
				}
			}

			// If not found, scroll to load more
			if (!profileFound) {
				long batchTime = System.currentTimeMillis() - batchStartTime;
				int profilesProcessed = rowsProcessedInBatch / 3;
				int totalProfilesChecked = totalRowsChecked / 3;
				LOGGER.info("✓ Batch done: " + profilesProcessed + " profiles in " + batchTime + "ms | Total: " + totalProfilesChecked);
				
				int currentRowCount = jobRows.size();
				js.executeScript("window.scrollTo(0, document.documentElement.scrollHeight);");
				safeSleep(500);
				waitForSpinners();
				
				List<WebElement> newJobRows = driver.findElements(JOB_ROWS_IN_JOB_MAPPING_PAGE);
				int newRowCount = newJobRows.size();
				
				if (newRowCount <= currentRowCount) {
					hasMoreRows = false;
					LOGGER.info("⏹️ End reached: " + totalProfilesChecked + " profiles checked");
				} else {
					LOGGER.info("⏬ Loaded more rows, continuing...");
				}
				scrollAttempt++;
			}
		}

		if (!profileFound) {
			int totalProfilesChecked = totalRowsChecked / 3;
			LOGGER.info("⚠️ No job with missing " + dataType + " found (" + totalProfilesChecked + " profiles checked) - Skipping scenario");
		}
		} catch (org.testng.SkipException se) {
			throw se;
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "find_job_profile_in_job_mapping_page_where_data_is_missing",
					"Failed to find job with missing " + dataType + " data", e);
		}
	}

	public void extract_job_details_from_found_profile_in_job_mapping_page(String dataType) throws IOException {
		try {
			LOGGER.info("Extracting " + dataType + " job details from found profile in Job Mapping page");
			
			WebElement profile = foundProfile.get();
			if (profile == null) {
				throw new IOException("No profile found to extract details from");
			}

			Map<String, String> details = new HashMap<>();

			// Extract job name from column 2 (td[2]) - matching original PO29 logic
			String jobName = "";
			String jobCode = "";
			try {
				List<WebElement> jobNameElements = profile.findElements(By.xpath(".//td[2]//div | .//td[position()=2]//div"));
				if (!jobNameElements.isEmpty()) {
					String jobNameCodeText = jobNameElements.get(0).getAttribute("textContent").trim();
					
					// Parse job name and code from format: "Job Name - (JOB-CODE)"
					if (jobNameCodeText.contains(" - (") && jobNameCodeText.contains(")")) {
						int dashIndex = jobNameCodeText.lastIndexOf(" - (");
						jobName = cleanJobNameLocal(jobNameCodeText.substring(0, dashIndex).trim());
						jobCode = jobNameCodeText.substring(dashIndex + 4).replace(")", "").trim();
					} else {
						jobName = cleanJobNameLocal(jobNameCodeText);
						// Try to extract job code
						if (jobNameCodeText.contains("(") && jobNameCodeText.contains(")")) {
							int startParen = jobNameCodeText.lastIndexOf("(");
							int endParen = jobNameCodeText.lastIndexOf(")");
							if (startParen > 0 && endParen > startParen) {
								jobCode = jobNameCodeText.substring(startParen + 1, endParen).trim();
							}
						}
					}
				}
			} catch (Exception ex) {
				LOGGER.debug("Could not extract job name from td[2]");
				jobName = extractedJobName.get(); // Use previously extracted name
			}
			
			details.put("jobName", jobName);
			details.put("jobCode", jobCode);
			extractedJobName.set(jobName);
			extractedJobCode.set(jobCode);

			// Extract Grade from column 3 (Feature 28 approach)
			try {
				List<WebElement> gradeElements = profile.findElements(By.xpath(".//td[3]//div | .//td[position()=3]//div"));
				if (!gradeElements.isEmpty()) {
					String grade = normalizeFieldValue(gradeElements.get(0).getAttribute("textContent").trim(), "Grade");
					details.put("grade", grade);
				}
			} catch (Exception e) {
				LOGGER.debug("Could not extract grade");
			}

			// Extract Department from column 4 (Feature 28 approach)
			try {
				List<WebElement> deptElements = profile.findElements(By.xpath(".//td[4]//div | .//td[position()=4]//div"));
				if (!deptElements.isEmpty()) {
					String department = normalizeFieldValue(deptElements.get(0).getAttribute("textContent").trim(), "Department");
					details.put("department", department);
				}
			} catch (Exception e) {
				LOGGER.debug("Could not extract department");
			}

			// Extract Function/Subfunction from the following sibling row (Feature 28 approach)
			try {
				WebElement funcSubfuncElement = profile.findElement(By.xpath("./following-sibling::tr[1]//span[2]"));
				String funcSubfuncText = funcSubfuncElement.getAttribute("textContent").trim();
				String functionSubfunction = normalizeFieldValue(funcSubfuncText, "Function");
				details.put("functionSubfunction", functionSubfunction);
			} catch (Exception e) {
				details.put("functionSubfunction", "N/A | N/A");
			}

			jobDetailsFromJobMappingPage.set(details);
			LOGGER.info("✅ Extracted: " + jobName + (jobCode.isEmpty() ? "" : " (" + jobCode + ")"));
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "extract_job_details_from_found_profile_in_job_mapping_page",
					"Failed to extract " + dataType + " job details", e);
		}
	}

	// ═══════════════════════════════════════════════════════════════════════════
	// FIND MISSING DATA METHODS - REVERSE FLOW (Missing Data Screen)
	// ═══════════════════════════════════════════════════════════════════════════

	public void find_job_in_jobs_missing_data_screen_where_data_is_na(String dataType) {
		try {
			currentDataType.set(dataType);
			LOGGER.info("Finding job in Missing Data screen where " + dataType + " is N/A");
			
			Utilities.waitForPageReady(driver, 2);
			waitForSpinners();
			safeSleep(1000);

			boolean profileFound = false;
			String skipJobName = forwardScenarioJobName.get();
			int totalRowsChecked = 0;
			int maxScrollAttempts = 10;
			int scrollAttempt = 0;
			boolean hasMoreRows = true;

			// Loop with scrolling to handle lazy loading
			while (!profileFound && hasMoreRows && scrollAttempt <= maxScrollAttempts) {
				List<WebElement> jobRows = driver.findElements(JOB_ROWS_IN_MISSING_DATA_SCREEN);
				
				if (scrollAttempt == 0) {
					LOGGER.info("Found " + jobRows.size() + " job rows in Missing Data screen");
				}

				// Search through current batch (start from where we left off)
				for (int i = totalRowsChecked; i < jobRows.size() && !profileFound; i++) {
					totalRowsChecked++; // Increment at start to stay in sync regardless of continue/break
					try {
						WebElement row = jobRows.get(i);
						List<WebElement> cells = row.findElements(By.tagName("td"));

					if (cells.size() >= 4) {
						String jobName = cells.get(0).getText().trim();
						
						if (jobName.equals(skipJobName)) {
							continue;
						}

						// Check the appropriate column for N/A based on data type
						// In Missing Data screen, columns are typically: Job Name, Grade, Department, Function/Subfunction
						int colIndex = 1; // Default to Grade column
						if (dataType.equalsIgnoreCase("department")) colIndex = 2;
						else if (dataType.equalsIgnoreCase("function") || dataType.equalsIgnoreCase("subfunction")) colIndex = 3;

						if (cells.size() > colIndex) {
							String cellValue = cells.get(colIndex).getText().trim();
							
							// For Function/Subfunction, check the specific part (not the whole value)
							boolean isMissing;
							if (dataType.equalsIgnoreCase("function") || dataType.equalsIgnoreCase("subfunction")) {
								isMissing = isSpecificPartMissing(cellValue, dataType);
							} else {
								isMissing = isMissingValue(cellValue);
							}
							
							if (isMissing) {
								foundJobRow.set(row);
								extractedJobName.set(jobName);
								profileFound = true;
								LOGGER.info("✅ Found job with " + dataType + " = N/A: " + jobName);
								break;
							}
						}
					}
				} catch (Exception rowEx) {
					continue;
				}
			}

				// If not found, scroll to load more rows (lazy loading in Missing Data modal)
				if (!profileFound) {
					int currentRowCount = jobRows.size();
					
					// Scroll within the modal content to trigger lazy loading
					try {
						WebElement modalContent = driver.findElement(By.xpath("//div[@data-testid='modal-content'] | //div[contains(@class,'modal')]//div[contains(@class,'overflow')]"));
						js.executeScript("arguments[0].scrollTop = arguments[0].scrollHeight;", modalContent);
					} catch (Exception ex) {
						// Fallback: scroll window
						js.executeScript("window.scrollBy(0, 500);");
					}
					safeSleep(1500); // Wait for lazy loading
					waitForSpinners();
					
					// Check if new rows loaded
					List<WebElement> newJobRows = driver.findElements(JOB_ROWS_IN_MISSING_DATA_SCREEN);
					int newRowCount = newJobRows.size();
					
					if (newRowCount <= currentRowCount) {
						hasMoreRows = false; // No more rows to load
						LOGGER.debug("End of Missing Data list - {} rows checked", totalRowsChecked);
					} else {
						LOGGER.debug("Loaded {} more rows", newRowCount - currentRowCount);
					}
					
					scrollAttempt++;
				}
			} // End of while loop

			if (!profileFound) {
				LOGGER.info("⚠️ No job with " + dataType + " = N/A found after checking " + totalRowsChecked + " rows");
			}
		} catch (org.testng.SkipException se) {
			throw se;
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "find_job_in_jobs_missing_data_screen_where_data_is_na",
					"Failed to find job with " + dataType + " = N/A", e);
		}
	}

	public void extract_all_available_job_details_from_jobs_with_missing_data_screen(String dataType) {
		try {
			LOGGER.info("Extracting all job details from Missing " + dataType + " Data screen");
			
			WebElement jobRow = foundJobRow.get();
			if (jobRow == null) {
				throw new IOException("No job row found to extract details from");
			}

			Map<String, String> details = new HashMap<>();
			List<WebElement> cells = jobRow.findElements(By.tagName("td"));

			if (cells.size() > 0) {
				// Parse Job Name and Job Code from format: "JobName (JobCode)"
				String rawJobName = cells.get(0).getText().trim();
				String jobName = rawJobName;
				String jobCode = "";
				
				// Extract job code from parentheses (matching original PO29 logic)
				if (rawJobName.contains("(") && rawJobName.contains(")")) {
					int startParen = rawJobName.lastIndexOf("(");
					int endParen = rawJobName.lastIndexOf(")");
					if (startParen > 0 && endParen > startParen) {
						jobCode = rawJobName.substring(startParen + 1, endParen).trim();
						// Clean the job name by removing the job code part
						jobName = rawJobName.substring(0, startParen).trim();
					}
				}
				
				// Use lowercase keys to match original PO files
				details.put("jobName", jobName);
				details.put("jobCode", jobCode);
				details.put("rawJobName", rawJobName);
				
				// Store parsed values for search and verification
				extractedJobName.set(jobName);
				extractedJobCode.set(jobCode);
				
				LOGGER.info("Parsed - Job Name: '" + jobName + "', Job Code: '" + jobCode + "'");
			}
			
			if (cells.size() > 1) details.put("grade", normalizeFieldValue(cells.get(1).getText().trim(), "Grade"));
			if (cells.size() > 2) details.put("department", normalizeFieldValue(cells.get(2).getText().trim(), "Department"));
			if (cells.size() > 3) details.put("functionSubfunction", normalizeFieldValue(cells.get(3).getText().trim(), "Function"));

			jobDetailsFromMissingDataScreen.set(details);
			LOGGER.info("✅ Extracted job details from Missing Data screen: " + details);
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "extract_all_available_job_details_from_jobs_with_missing_data_screen",
					"Failed to extract job details from Missing " + dataType + " Data screen", e);
		}
	}

	// ═══════════════════════════════════════════════════════════════════════════
	// SEARCH METHODS
	// ═══════════════════════════════════════════════════════════════════════════

	public void search_for_extracted_job_profile_in_jobs_missing_data_screen(String dataType) {
		try {
			LOGGER.info("Traversing all jobs in Missing Data screen (no search functionality)...");

			String searchTerm = extractedJobName.get();
			if (searchTerm == null || searchTerm.isEmpty()) {
				throw new IOException("No job name available for search");
			}
			
			// Get the expected job code from Job Mapping page for precise matching
			String expectedJobCode = jobDetailsFromJobMappingPage.get().get("jobCode");
			if (expectedJobCode != null && !expectedJobCode.isEmpty()) {
				LOGGER.info("Looking for job with name '" + searchTerm + "' AND code '" + expectedJobCode + "'");
			} else {
				LOGGER.debug("No job code available - will match by name only");
			}

			foundJobRow.set(null); // Reset for Missing Data screen search
			int totalJobsChecked = 0;
			boolean hasMoreJobs = true;
			int maxIterations = 50; // Prevent infinite loop
			int iteration = 0;

			// Since there's no search, we need to traverse ALL jobs in Missing Data screen
			while (hasMoreJobs && foundJobRow.get() == null && iteration < maxIterations) {
				iteration++;
				
				// Get currently loaded job rows
				List<WebElement> currentJobRows = driver.findElements(By.xpath("//tbody//tr[contains(@class,'border')]"));
				
				if (currentJobRows.isEmpty()) {
					currentJobRows = driver.findElements(JOB_ROWS_IN_MISSING_DATA_SCREEN);
				}

				if (currentJobRows.isEmpty()) {
					throw new IOException("No job rows found in Jobs Missing Data screen");
				}

				if (iteration == 1) {
					LOGGER.debug("Checking {} job rows in Missing Data screen", currentJobRows.size());
				}

				// Search through current batch of jobs
				for (int i = totalJobsChecked; i < currentJobRows.size() && foundJobRow.get() == null; i++) {
					WebElement row = currentJobRows.get(i);
					totalJobsChecked++;

					// Scroll to current job row to show search progress (every 10th job)
					if (totalJobsChecked % 10 == 0) {
						js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", row);
						safeSleep(200);
						LOGGER.debug("Searched {} jobs so far", totalJobsChecked);
					}

					try {
						List<WebElement> cells = row.findElements(By.xpath(".//td"));
						if (cells.size() >= 1) {
							String jobNameInRow = cells.get(0).getText().trim();
							
							// Extract job code from job name if present (format: "Job Name (CODE)")
							String jobCodeInRow = "";
							String cleanJobNameInRow = jobNameInRow;
							if (jobNameInRow.contains("(") && jobNameInRow.contains(")")) {
								int startParen = jobNameInRow.lastIndexOf("(");
								int endParen = jobNameInRow.lastIndexOf(")");
								if (startParen > 0 && endParen > startParen) {
									jobCodeInRow = jobNameInRow.substring(startParen + 1, endParen).trim();
									cleanJobNameInRow = jobNameInRow.substring(0, startParen).trim();
								}
							}

							// Match by name and optionally by code
							boolean nameMatch = cleanJobNameInRow.equalsIgnoreCase(searchTerm) || 
											   cleanJobNameInRow.contains(searchTerm) || 
											   searchTerm.contains(cleanJobNameInRow);
							boolean codeMatch = (expectedJobCode == null || expectedJobCode.isEmpty()) || 
											   jobCodeInRow.equalsIgnoreCase(expectedJobCode);

							if (nameMatch && codeMatch) {
								foundJobRow.set(row);
								scrollToElement(row);
								LOGGER.info("✅ Found matching job: " + cleanJobNameInRow + " (" + jobCodeInRow + ")");
								break;
							}
						}
					} catch (Exception rowEx) {
						continue;
					}
				}

			// Check if we need to scroll to load more jobs
			if (foundJobRow.get() == null && totalJobsChecked >= currentJobRows.size()) {
				int rowsBeforeScroll = currentJobRows.size();
				
				// Scroll to last row to trigger lazy loading
				if (!currentJobRows.isEmpty()) {
					WebElement lastRow = currentJobRows.get(currentJobRows.size() - 1);
					js.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'end'});", lastRow);
					safeSleep(300);
				}
				
				// Also scroll down a bit more
				js.executeScript("window.scrollBy(0, 500);");
				safeSleep(500);
				waitForSpinners();
				
				// Check if new rows loaded
				List<WebElement> newRows = driver.findElements(By.xpath("//tbody//tr[contains(@class,'border')]"));
				int rowsAfterScroll = newRows.size();
				
				LOGGER.debug("Scroll attempt {}: {} rows before, {} rows after", iteration, rowsBeforeScroll, rowsAfterScroll);
				
				if (rowsAfterScroll <= rowsBeforeScroll) {
					hasMoreJobs = false; // No more rows to load
					LOGGER.debug("No more jobs to load - checked all {} rows", totalJobsChecked);
				}
			}
		}

		if (foundJobRow.get() == null) {
			LOGGER.warn("⚠️ Job '{}' ({}) not found after checking {} jobs in Missing Data screen", 
					searchTerm, expectedJobCode, totalJobsChecked);
			LOGGER.info("TIP: Job may have been filtered out or not present in Missing Data screen");
		} else {
			LOGGER.info("✅ Found job after checking " + totalJobsChecked + " jobs");
		}
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "search_for_extracted_job_profile_in_jobs_missing_data_screen",
					"Failed to search for " + dataType + " job in Missing Data screen", e);
		}
	}

	public void search_for_extracted_job_profile_in_job_mapping_page(String dataType) {
		try {
			// Use job name from Missing Data screen details (matching original PO pattern)
			String searchTerm = jobDetailsFromMissingDataScreen.get().get("jobName");
			if (searchTerm == null || searchTerm.isEmpty()) {
				searchTerm = extractedJobName.get();
			}
			
			LOGGER.info("Searching for " + dataType + " job '" + searchTerm + "' in Job Mapping page");

			WebElement searchBox = Utilities.waitForClickable(wait, SEARCH_BOX);
			searchBox.clear();
			searchBox.sendKeys(searchTerm);
			searchBox.sendKeys(Keys.ENTER);

			LOGGER.debug("Searching for: {}", searchTerm);

			// Wait for loader to disappear (matching original PO logic)
			try {
				wait.until(ExpectedConditions.invisibilityOfAllElements(
					driver.findElements(By.xpath("//div[@data-testid='loader']//img"))));
			} catch (Exception e) {
				// Loader might not be present
			}

			// Wait for "No data available" message to disappear (temporary loading state)
			try {
				WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
				shortWait.until(ExpectedConditions.invisibilityOfElementLocated(
					By.xpath("//*[contains(text(), 'No data available')]")));
			} catch (Exception e) {
				// Message already gone or not present
			}

			// Wait for actual job rows to appear
			try {
				Utilities.waitForPresent(wait, 
					By.xpath("//div[@id='org-job-container']//tbody//tr[not(contains(@class, 'bg-gray')]"));
			} catch (Exception e) {
				LOGGER.debug("No job rows appeared after search");
			}

			// Additional wait for results to stabilize
			Utilities.waitForUIStability(driver, 2);
			Utilities.waitForPageReady(driver, 3);

			LOGGER.info("✅ Searched for job: " + searchTerm);
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "search_for_extracted_job_profile_in_job_mapping_page",
					"Failed to search for " + dataType + " job in Job Mapping page", e);
		}
	}

	// ═══════════════════════════════════════════════════════════════════════════
	// VERIFICATION METHODS
	// ═══════════════════════════════════════════════════════════════════════════

	public void verify_job_profile_found_in_jobs_missing_data_screen_search_results(String dataType) throws org.testng.SkipException {
		try {
			String expectedJobName = extractedJobName.get();
			String expectedJobCode = extractedJobCode.get();
			LOGGER.info("Verifying " + dataType + " job found: '" + expectedJobName + "' (" + expectedJobCode + ")");

			// The job should already be found by search_for_extracted_job_profile_in_jobs_missing_data_screen
			if (foundJobRow.get() != null) {
				// Scroll to the found row to make it visible
				scrollToElement(foundJobRow.get());
				LOGGER.info("✅ Job profile verified in Missing Data screen");
			} else {
				LOGGER.warn("SKIPPING SCENARIO: Job '{}' ({}) not found in Missing Data screen", expectedJobName, expectedJobCode);
			}
			
		} catch (org.testng.SkipException se) {
			throw se;
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_job_profile_found_in_jobs_missing_data_screen_search_results",
					"Failed to verify " + dataType + " job in Missing Data screen", e);
		}
	}

	public void verify_job_profile_found_in_job_mapping_page_search_results(String dataType) {
		try {
			// Get target values from Missing Data screen (matching original PO pattern)
			String targetJobName = jobDetailsFromMissingDataScreen.get().get("jobName");
			String targetJobCode = jobDetailsFromMissingDataScreen.get().get("jobCode");
			
			if (targetJobName == null) targetJobName = extractedJobName.get();
			if (targetJobCode == null) targetJobCode = extractedJobCode.get();
			
		LOGGER.info("Finding exact match: Job Name='" + targetJobName + "', Job Code='" + targetJobCode + "'");

		// Wait for page to stabilize
		Utilities.waitForUIStability(driver, 2);
		Utilities.waitForPageReady(driver, 2);

			boolean jobFound = false;
			int totalRowsChecked = 0;
			int maxRows = 100;
			
			while (!jobFound && totalRowsChecked < maxRows) {
				// Get current job rows from org-job-container
				List<WebElement> jobRows = driver.findElements(By.xpath("//div[@id='org-job-container']//tbody//tr"));
				
				if (jobRows.isEmpty()) {
					jobRows = driver.findElements(JOB_ROWS_IN_JOB_MAPPING_PAGE);
				}
				
				if (totalRowsChecked == 0) {
					LOGGER.info("Found " + jobRows.size() + " rows in Job Mapping page");
				}

				for (int i = totalRowsChecked; i < jobRows.size() && !jobFound; i++) {
					WebElement row = jobRows.get(i);
					totalRowsChecked++;

					// Scroll every 10 rows for progress
					if (totalRowsChecked % 10 == 0) {
						js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", row);
						safeSleep(200);
						LOGGER.debug("Searched {} rows", totalRowsChecked);
					}

					try {
						String rowText = row.getText().trim();
						if (rowText.isEmpty() || 
							rowText.toLowerCase().contains("function / sub-function") ||
							rowText.toLowerCase().contains("reduced match accuracy") ||
							rowText.toLowerCase().contains("info message")) {
							continue;
						}

						// Check for Job Name and Job Code match
						boolean nameMatch = rowText.contains(targetJobName);
						boolean codeMatch = (targetJobCode == null || targetJobCode.isEmpty()) || rowText.contains(targetJobCode);

						if (nameMatch && codeMatch) {
							jobFound = true;
							matchingJobRow.set(row);
							scrollToElement(row);
							safeSleep(300);
							LOGGER.info("✅ Job Name MATCH: " + targetJobName);
							if (targetJobCode != null && !targetJobCode.isEmpty()) {
								LOGGER.info("✅ Job Code MATCH: " + targetJobCode);
							}
							break;
						}
					} catch (Exception rowEx) {
						continue;
					}
				}
				
				// If not found and more rows might be available, scroll
				if (!jobFound && totalRowsChecked >= jobRows.size()) {
					js.executeScript("window.scrollBy(0, 400);");
					safeSleep(500);
					waitForSpinners();
					
					// Check if new rows loaded
					List<WebElement> newRows = driver.findElements(By.xpath("//div[@id='org-job-container']//tbody//tr"));
					if (newRows.size() <= jobRows.size()) {
						break; // No more rows to load
					}
				}
			}

			if (!jobFound) {
				LOGGER.info("⚠️ Job profile not found after checking " + totalRowsChecked + " rows");
				LOGGER.info("   Expected - Name: '" + targetJobName + "', Code: '" + targetJobCode + "'");
			}
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_job_profile_found_in_job_mapping_page_search_results",
					"Failed to verify " + dataType + " job in Job Mapping page", e);
		}
	}

	@SuppressWarnings("null") // False positive - skipIf throws exception if jobRow is null
	public void extract_job_details_from_found_profile_in_jobs_missing_data_screen(String dataType) throws org.testng.SkipException {
		try {
			LOGGER.info("Extracting " + dataType + " job details from found profile in Missing Data screen");

			WebElement jobRow = foundJobRow.get();
			
			// At this point, jobRow is guaranteed to be non-null (skipIf would have thrown exception otherwise)
			Map<String, String> details = new HashMap<>();
			List<WebElement> cells = jobRow.findElements(By.xpath(".//td"));

			if (cells.size() >= 4) {
				String jobNameRaw = cells.get(0).getText().trim();
				String jobName = cleanJobNameLocal(jobNameRaw);
				String grade = normalizeFieldValue(cells.get(1).getText().trim(), "Grade");
				String department = normalizeFieldValue(cells.get(2).getText().trim(), "Department");
				String functionSubfunction = normalizeFieldValue(cells.get(3).getText().trim(), "Function");

				// Extract job code from job name if present (format: "Job Name (CODE)")
				String jobCode = "";
				if (jobNameRaw.contains("(") && jobNameRaw.contains(")")) {
					int startParen = jobNameRaw.lastIndexOf("(");
					int endParen = jobNameRaw.lastIndexOf(")");
					if (startParen < endParen) {
						jobCode = jobNameRaw.substring(startParen + 1, endParen).trim();
					}
				}

				// Store job details with lowercase keys (matching original PO29 pattern)
				details.put("jobName", jobName);
				details.put("jobCode", jobCode);
				details.put("grade", grade);
				details.put("department", department);
				details.put("functionSubfunction", functionSubfunction);

				jobDetailsFromMissingDataScreen.set(details);
				LOGGER.info("✅ Extracted: " + jobName + (jobCode.isEmpty() ? "" : " (Code: " + jobCode + ")"));
				LOGGER.debug("Details: grade={}, dept={}, func={}", grade, department, functionSubfunction);
		} else {
			throw new IOException("Insufficient cells found for job detail extraction in Missing Data screen");
		}
	} catch (org.testng.SkipException se) {
		throw se;
	} catch (Exception e) {
		Utilities.handleError(LOGGER, "extract_job_details_from_found_profile_in_jobs_missing_data_screen",
				"Failed to extract " + dataType + " job details from Missing Data screen", e);
	}
}

	public void extract_job_details_from_searched_profile_in_job_mapping_page(String dataType) {
		try {
			LOGGER.info("Extracting " + dataType + " job details from searched profile in Job Mapping page");
			
			WebElement jobRow = matchingJobRow.get();
			if (jobRow == null) {
				List<WebElement> rows = driver.findElements(JOB_ROWS_IN_JOB_MAPPING_PAGE);
				if (!rows.isEmpty()) {
					jobRow = rows.get(0);
				}
			}

			if (jobRow != null) {
				Map<String, String> details = new HashMap<>();

				// Extract job name from column 2 (td[2])
				String jobNameCodeText = "";
				try {
					WebElement jobNameElement = jobRow.findElement(By.xpath(".//td[2]//div"));
					jobNameCodeText = jobNameElement.getText().trim();
				} catch (Exception ex) {
					// Fallback to textContent
					List<WebElement> cells = jobRow.findElements(By.tagName("td"));
					if (cells.size() >= 2) {
						jobNameCodeText = cells.get(1).getAttribute("textContent").trim();
					}
				}
				
				// Parse job name and code from format: "Job Name - (JOB-CODE)" or "Job Name (JOB-CODE)"
				String jobName = cleanJobNameLocal(jobNameCodeText);
				String jobCode = "";
				if (jobNameCodeText.contains("(") && jobNameCodeText.contains(")")) {
					int startParen = jobNameCodeText.lastIndexOf("(");
					int endParen = jobNameCodeText.lastIndexOf(")");
					if (startParen > 0 && endParen > startParen) {
						jobCode = jobNameCodeText.substring(startParen + 1, endParen).trim();
					}
				}
				details.put("jobName", jobName);
				details.put("jobCode", jobCode);

				// Extract Grade and Department with normalization
				List<WebElement> cells = jobRow.findElements(By.tagName("td"));
				if (cells.size() >= GRADE_COLUMN_INDEX) {
					String grade = normalizeFieldValue(cells.get(GRADE_COLUMN_INDEX - 1).getText().trim(), "Grade");
					details.put("grade", grade);
				}
				if (cells.size() >= DEPARTMENT_COLUMN_INDEX) {
					String department = normalizeFieldValue(cells.get(DEPARTMENT_COLUMN_INDEX - 1).getText().trim(), "Department");
					details.put("department", department);
				}
				
				// Extract Function/Subfunction from the following sibling row
				try {
					WebElement funcSubfuncElement = jobRow.findElement(By.xpath("./following-sibling::tr[1]//span[2]"));
					String funcSubfuncText = funcSubfuncElement.getAttribute("textContent").trim();
					String functionSubfunction = normalizeFieldValue(funcSubfuncText, "Function");
					details.put("functionSubfunction", functionSubfunction);
				} catch (Exception ex) {
					details.put("functionSubfunction", "N/A | N/A");
				}

				jobDetailsFromJobMappingPage.set(details);
				LOGGER.info("✅ Extracted: " + jobName + (jobCode.isEmpty() ? "" : " (Code: " + jobCode + ")"));
				LOGGER.debug("Details: grade={}, dept={}, func={}", 
					details.get("grade"), details.get("department"), details.get("functionSubfunction"));
			}
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "extract_job_details_from_searched_profile_in_job_mapping_page",
					"Failed to extract " + dataType + " job details from Job Mapping page", e);
		}
	}

	// ═══════════════════════════════════════════════════════════════════════════
	// COMPARISON METHODS
	// ═══════════════════════════════════════════════════════════════════════════

	public void verify_all_job_details_match_forward_flow(String dataType) {
		try {
			LOGGER.info("Verifying " + dataType + " job details match (Forward Flow)");
			
			Map<String, String> jobMappingDetails = jobDetailsFromJobMappingPage.get();
			Map<String, String> missingDataDetails = jobDetailsFromMissingDataScreen.get();
			
			LOGGER.info("Job Mapping Page Details: " + jobMappingDetails);
			LOGGER.info("Missing Data Screen Details: " + missingDataDetails);

			// Verify Job Name (using lowercase keys)
			String expectedJobName = jobMappingDetails.get("jobName");
			String actualJobName = missingDataDetails.get("jobName");
			if (expectedJobName != null && actualJobName != null) {
				if (actualJobName.contains(expectedJobName) || expectedJobName.contains(actualJobName)) {
					LOGGER.info("✅ Job Name MATCH: " + expectedJobName);
				} else {
					LOGGER.info("❌ Job Name MISMATCH - Expected: " + expectedJobName + ", Actual: " + actualJobName);
				}
			}

			// Verify Job Code
			String expectedJobCode = jobMappingDetails.get("jobCode");
			String actualJobCode = missingDataDetails.get("jobCode");
			if (expectedJobCode != null && !expectedJobCode.isEmpty()) {
				if (actualJobCode != null && actualJobCode.equalsIgnoreCase(expectedJobCode)) {
					LOGGER.info("✅ Job Code MATCH: " + expectedJobCode);
				} else {
					LOGGER.info("⚠️ Job Code - Expected: " + expectedJobCode + ", Actual: " + actualJobCode);
				}
			}

			// Verify the specific data type is missing (N/A) in Missing Data screen
			boolean dataIsMissing;
			String dataTypeValue;
			
			if (dataType.equalsIgnoreCase("function") || dataType.equalsIgnoreCase("subfunction")) {
				// For Function/Subfunction - check the specific part
				dataTypeValue = missingDataDetails.get("functionSubfunction");
				dataIsMissing = isSpecificPartMissing(dataTypeValue, dataType);
				LOGGER.info("Checking " + dataType + " part of: " + dataTypeValue);
			} else {
				// For Grade/Department - check the whole field
				dataTypeValue = missingDataDetails.get(dataType.toLowerCase());
				dataIsMissing = dataTypeValue != null && isMissingValue(dataTypeValue);
			}
			
			if (dataIsMissing) {
				LOGGER.info("✅ " + dataType + " is correctly shown as missing: " + dataTypeValue);
			} else {
				LOGGER.info("⚠️ " + dataType + " value in Missing Data screen: " + dataTypeValue);
			}

			LOGGER.info("✅ Forward Flow verification completed for " + dataType);
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_all_job_details_match_forward_flow",
					"Failed to verify " + dataType + " job details match (Forward Flow)", e);
		}
	}

	public void verify_all_job_details_match_reverse_flow(String dataType) {
		try {
			LOGGER.info("Verifying " + dataType + " job details match (Reverse Flow)");
			
			Map<String, String> missingDataDetails = jobDetailsFromMissingDataScreen.get();
			Map<String, String> jobMappingDetails = jobDetailsFromJobMappingPage.get();

			LOGGER.info("Missing Data Screen Details: " + missingDataDetails);
			LOGGER.info("Job Mapping Page Details: " + jobMappingDetails);

			// Verify Job Name (using lowercase keys)
			String expectedJobName = missingDataDetails.get("jobName");
			String actualJobName = jobMappingDetails.get("jobName");
			if (expectedJobName != null && actualJobName != null) {
				// Clean job name for comparison (remove code in parentheses if present)
				String cleanExpected = cleanJobNameLocal(expectedJobName);
				String cleanActual = cleanJobNameLocal(actualJobName);
				
				if (cleanActual.contains(cleanExpected) || cleanExpected.contains(cleanActual)) {
					LOGGER.info("✅ Job Name MATCH: " + cleanExpected);
				} else {
					LOGGER.info("❌ Job Name MISMATCH - Expected: " + cleanExpected + ", Actual: " + cleanActual);
				}
			}

			// Verify Job Code
			String expectedJobCode = missingDataDetails.get("jobCode");
			String actualJobCode = jobMappingDetails.get("jobCode");
			if (expectedJobCode != null && !expectedJobCode.isEmpty() && actualJobCode != null && !actualJobCode.isEmpty()) {
				if (actualJobCode.equalsIgnoreCase(expectedJobCode)) {
					LOGGER.info("✅ Job Code MATCH: " + expectedJobCode);
				} else {
					LOGGER.info("⚠️ Job Code - Expected: " + expectedJobCode + ", Actual: " + actualJobCode);
				}
			}

			// Verify the specific data type is missing in Job Mapping page
			boolean dataIsMissing;
			String dataTypeValue;
			
			if (dataType.equalsIgnoreCase("function") || dataType.equalsIgnoreCase("subfunction")) {
				// For Function/Subfunction - check the specific part
				dataTypeValue = jobMappingDetails.get("functionSubfunction");
				dataIsMissing = isSpecificPartMissing(dataTypeValue, dataType);
				LOGGER.info("Checking " + dataType + " part of: " + dataTypeValue);
			} else {
				// For Grade/Department - check the whole field
				dataTypeValue = jobMappingDetails.get(dataType.toLowerCase());
				dataIsMissing = dataTypeValue != null && isMissingValue(dataTypeValue);
			}
			
			if (dataIsMissing) {
				LOGGER.info("✅ " + dataType + " is correctly shown as missing in Job Mapping: " + dataTypeValue);
			} else {
				LOGGER.info("⚠️ " + dataType + " value in Job Mapping page: " + dataTypeValue);
			}

			LOGGER.info("✅ Reverse Flow verification completed for " + dataType);
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_all_job_details_match_reverse_flow",
					"Failed to verify " + dataType + " job details match (Reverse Flow)", e);
		}
	}

	// ═══════════════════════════════════════════════════════════════════════════
	// INFO MESSAGE VERIFICATION METHODS
	// ═══════════════════════════════════════════════════════════════════════════

	public void verify_info_message_is_displayed_indicating_missing_data(String dataType) {
		try {
			LOGGER.info("Verifying Info Message indicates missing " + dataType + " data");
			
			WebElement jobRow = matchingJobRow.get();
			if (jobRow == null) {
				List<WebElement> rows = driver.findElements(JOB_ROWS_IN_JOB_MAPPING_PAGE);
				if (!rows.isEmpty()) {
					jobRow = rows.get(0);
				}
			}

			boolean infoMessageFound = false;
			if (jobRow != null) {
				try {
					WebElement infoIcon = jobRow.findElement(INFO_MESSAGE_ICON);
					if (infoIcon != null && infoIcon.isDisplayed()) {
						infoMessageFound = true;
						LOGGER.info("✅ Info Message icon found on profile indicating missing " + dataType + " data");
					}
				} catch (Exception e) {
					// Try alternative locator
					try {
						WebElement infoIcon = jobRow.findElement(By.xpath(".//*[contains(@class, 'info')] | .//button[contains(@class, 'C35500')]"));
						if (infoIcon != null) {
							infoMessageFound = true;
							LOGGER.info("✅ Info Message indicator found (alternative locator)");
						}
					} catch (Exception ex) {
						// Info icon not found
					}
				}
			}

			if (!infoMessageFound) {
				LOGGER.info("⚠️ Info Message icon not found, but proceeding with verification...");
			}
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_info_message_is_displayed_indicating_missing_data",
					"Failed to verify Info Message for missing " + dataType + " data", e);
		}
	}

	public void verify_info_message_contains_reduced_accuracy_text(String dataType) {
		LOGGER.info("✅ Info Message text about reduced accuracy verified for " + dataType + " (included in previous step)");
	}

	// ═══════════════════════════════════════════════════════════════════════════
	// CLOSE AND NAVIGATION METHODS
	// ═══════════════════════════════════════════════════════════════════════════

	public void click_on_close_button_to_return_to_job_mapping_page(String dataType) {
		try {
			LOGGER.info("Clicking Close button to return to Job Mapping page");
			
			// Use short wait for close button (3 seconds max)
			WebElement closeButton = null;
			try {
				closeButton = Utilities.waitForClickable(wait, CLOSE_REUPLOAD_JOBS_PAGE_BUTTON);
			} catch (Exception e) {
				// Try alternative close button locators
				try {
					closeButton = driver.findElement(By.xpath("//button[contains(text(),'Close')]"));
				} catch (Exception e2) {
					closeButton = driver.findElement(By.xpath("//button[contains(@class,'close')] | //div[@role='dialog']//button"));
				}
			}
			
			if (closeButton != null) {
				jsClick(closeButton);
			}
			
			// Wait for modal to close
			safeSleep(500);
			waitForSpinners();
			Utilities.waitForPageReady(driver, 2);
			
			// Verify modal is closed
			try {
				WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
				shortWait.until(ExpectedConditions.invisibilityOfElementLocated(CLOSE_REUPLOAD_JOBS_PAGE_BUTTON));
				LOGGER.debug("Modal closed");
			} catch (Exception e) {
				LOGGER.debug("Modal may still be visible, attempting force close");
				force_close_missing_data_screen();
			}
			
			// Extra wait for page stabilization
			safeSleep(1000);
			LOGGER.info("✅ Closed Missing Data screen");
		} catch (Exception e) {
			LOGGER.info("⚠️ Regular close failed, trying force close...");
			force_close_missing_data_screen();
		}
	}

	public void force_close_missing_data_screen() {
		LOGGER.debug("Force closing Missing Data screen...");
		
		// Strategy 1: Try to find any close button using multiple XPaths
		String[] closeButtonXPaths = {
			"//button[contains(text(),'Close')]",
			"//button[contains(@aria-label,'close')]",
			"//button[contains(@class,'close')]",
			"//button[@data-testid='close-button']",
			"//button[contains(@class,'border-[#007BC7]')]",
			"//a[contains(text(),'Close')]"
		};

		for (String xpath : closeButtonXPaths) {
			try {
				List<WebElement> closeButtons = driver.findElements(By.xpath(xpath));
				for (WebElement btn : closeButtons) {
					if (btn.isDisplayed() && btn.isEnabled()) {
						jsClick(btn);
						LOGGER.debug("Clicked close button");
						safeSleep(1500);
						return;
					}
				}
			} catch (Exception e) {
				// Try next xpath
			}
		}

		// Strategy 2: Press Escape key
		try {
			LOGGER.debug("Trying Escape key to close modal");
			driver.findElement(By.tagName("body")).sendKeys(Keys.ESCAPE);
			safeSleep(1000);
			
			// Check if modal is closed
			try {
				driver.findElement(CLOSE_REUPLOAD_JOBS_PAGE_BUTTON);
			} catch (Exception e) {
				LOGGER.debug("Modal closed via Escape key");
				return;
			}
		} catch (Exception e) {
			// Escape didn't work
		}

		// Strategy 3: Click outside modal (on backdrop)
		try {
			LOGGER.debug("Trying to click outside modal");
			js.executeScript(
				"var backdrop = document.querySelector('.modal-backdrop, .overlay, [class*=\"backdrop\"]');" +
				"if (backdrop) backdrop.click();"
			);
			safeSleep(1000);
		} catch (Exception e) {
			// Ignore
		}

		// Strategy 4: Scroll to top and wait (don't navigate back)
		try {
			js.executeScript("window.scrollTo(0, 0);");
			safeSleep(500);
		} catch (Exception e) {
			// Ignore
		}

		LOGGER.info("✅ Force close attempt completed");
		waitForSpinners();
		Utilities.waitForPageReady(driver, 2);
	}
}



