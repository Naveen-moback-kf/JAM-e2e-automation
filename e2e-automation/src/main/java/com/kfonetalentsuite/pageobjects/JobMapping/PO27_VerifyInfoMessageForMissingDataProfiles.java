package com.kfonetalentsuite.pageobjects.JobMapping;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.testng.Assert;
import org.testng.SkipException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.PageObjectHelper;
import com.kfonetalentsuite.webdriverManager.DriverManager;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

public class PO27_VerifyInfoMessageForMissingDataProfiles extends DriverManager {
	
	private static final Logger LOGGER = (Logger) LogManager.getLogger(PO27_VerifyInfoMessageForMissingDataProfiles.class);
	
	WebDriver driver;
	WebDriverWait wait;
	JavascriptExecutor jsExecutor;
	
	public PO27_VerifyInfoMessageForMissingDataProfiles() {
		this.driver = getDriver();
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		this.jsExecutor = (JavascriptExecutor) driver;
		PageFactory.initElements(driver, this);
	}

	// Page Elements
	@FindBy(xpath = "//div[@role='button' and @aria-label='Reduced match accuracy due to missing data']")
	private List<WebElement> infoMessageContainers;
	
	@FindBy(xpath = "//div[@role='button' and @aria-label='Reduced match accuracy due to missing data']//div[contains(text(), 'Reduced match accuracy due to missing data')]")
	private List<WebElement> infoMessageTexts;
	
	@FindBy(xpath = "//div[@role='button' and @aria-label='Reduced match accuracy due to missing data']//svg")
	private List<WebElement> infoMessageIcons;
	
	@FindBy(xpath = "//td[contains(@class, 'border-gray-200')]/div[contains(@class, 'max-w-[120px]') and text()='-']")
	private List<WebElement> jobsWithMissingData;
	
	@FindBy(xpath = "//button[@id='view-matches' and @aria-label='View other matches']")
	private List<WebElement> viewOtherMatchesButtons;
	
	// Fields for dynamic row-based approach (used by old methods - kept for backward compatibility)
	@SuppressWarnings("unused")
	private List<WebElement> profilesWithInfoMessages = new ArrayList<>();
	@SuppressWarnings("unused")
	private List<Integer> rowIndicesWithInfoMessages = new ArrayList<>();
	private int currentRowIndex = -1;
	private int secondCurrentRowIndex = -1; // Non-static for local use
	
	// Legacy fields for old methods (kept for backward compatibility)
	@SuppressWarnings("unused")
	private int globalFirstProfileRowIndex = -1;
	@SuppressWarnings("unused")
	private int globalFirstProfileNumber = -1;
	@SuppressWarnings("unused")
	private String globalFirstJobNameWithInfoMessage = "";
	@SuppressWarnings("unused")
	private String globalFirstJobCodeWithInfoMessage = "";
	
	// Job details for the first profile with info message (STATIC to persist across page object instances)
	private static String jobNameWithInfoMessage = "";
	private static String jobCodeWithInfoMessage = "";
	private static String gradeWithInfoMessage = "";
	private static String departmentWithInfoMessage = "";
	private static String functionSubfunctionWithInfoMessage = "";
	@SuppressWarnings("unused")  // Used by old methods
	private static int currentRowIndexStatic = -1;
	
	// Job details for the second profile with info message (STATIC to persist across page object instances)
	private static String secondJobNameWithInfoMessage = "";
	private static String secondJobCodeWithInfoMessage = "";
	private static String secondGradeWithInfoMessage = "";
	private static String secondDepartmentWithInfoMessage = "";
	private static String secondFunctionSubfunctionWithInfoMessage = "";
	@SuppressWarnings("unused")  // Used by old methods
	private static int secondCurrentRowIndexStatic = -1;

	// Helper Methods
	private void safeSleep(int milliseconds) {
	try {
		PerformanceUtils.waitForUIStability(driver, milliseconds / 1000);
	} catch (Exception e) {
			Thread.currentThread().interrupt();
			LOGGER.warn("Sleep interrupted: " + e.getMessage());
		}
	}
	
	/**
	 * SIMPLE: Scroll down to load more profiles (at least 50 profiles total)
	 * @return true if additional profiles were loaded after scrolling
	 */
	private boolean scrollDownAndFindMoreAutoMappedProfiles() {
		try {
			LOGGER.info("SCROLL: Loading more profiles (target: at least 50 profiles)...");
			
			// Get initial count
			int initialRowCount = driver.findElements(By.xpath("//div[@id='org-job-container']//tbody//tr")).size();
			int initialProfiles = initialRowCount / 3; // Each profile spans 3 rows
			
			LOGGER.info("STATUS: Starting with {} profiles", initialProfiles);
			
			// Target: at least 50 profiles (150 rows)
			int targetProfiles = 50;
			int maxScrolls = 50; // Prevent infinite scrolling
			int scrollCount = 0;
			
			while (scrollCount < maxScrolls) {
				// Simple scroll down
				jsExecutor.executeScript("window.scrollBy(0, window.innerHeight);");
				safeSleep(1000);
				scrollCount++;
				
				// Check current profile count
				int currentRowCount = driver.findElements(By.xpath("//div[@id='org-job-container']//tbody//tr")).size();
				int currentProfiles = currentRowCount / 3;
				
				// Log progress every 10 scrolls
				if (scrollCount % 10 == 0) {
					LOGGER.info("STATUS: Scroll #{}: {} profiles loaded", scrollCount, currentProfiles);
				}
				
				// Stop if we have enough profiles
				if (currentProfiles >= targetProfiles) {
					LOGGER.info("SUCCESS: Target reached: {} profiles loaded (target: {})", currentProfiles, targetProfiles);
					break;
				}
				
				// Stop if no new content for 5 consecutive scrolls
				if (currentRowCount == initialRowCount) {
					if (scrollCount >= 5) {
						LOGGER.info("STOP: No more content loading after {} scrolls", scrollCount);
						break;
					}
				} else {
					initialRowCount = currentRowCount; // Update baseline
				}
			}
			
			int finalRowCount = driver.findElements(By.xpath("//div[@id='org-job-container']//tbody//tr")).size();
			int finalProfiles = finalRowCount / 3;
			
			LOGGER.info("STATUS: Scrolling complete: {} profiles loaded", finalProfiles);
			return finalProfiles >= targetProfiles;
			
		} catch (Exception e) {
			LOGGER.warn("WARNING: Error during scrolling: " + e.getMessage());
			return false;
		}
	}

	/**
	 * ENHANCED: Extract job details directly from info message element (more reliable after scrolling)
	 * @param infoMessageElement The info message WebElement
	 * @param isSecondProfile Whether this is for the second profile validation
	 */
	private void extractJobDetailsDirectlyFromInfoMessage(WebElement infoMessageElement, boolean isSecondProfile) throws IOException {
		try {
			LOGGER.info("INFO: Extracting job details directly from info message element (second profile: {})", isSecondProfile);
			
			// Find the row containing this info message
			WebElement rowWithInfoMessage = infoMessageElement.findElement(By.xpath("./ancestor::tr"));
			
			// Get the row index to calculate job details row
			int infoRowIndex = getRowIndex(rowWithInfoMessage);
			if (infoRowIndex <= 0) {
				throw new IOException("Could not determine row index for info message element");
			}
			
			int jobDetailsRowIndex = infoRowIndex - 1; // Job details are in the previous row
			LOGGER.info("STATUS: Direct extraction: Info message row {}, job details row {}", infoRowIndex, jobDetailsRowIndex);
			
			// Find the job details row using the calculated index
			WebElement jobDetailsRow = driver.findElement(
				By.xpath(String.format("//div[@id='org-job-container']//tbody//tr[%d]", jobDetailsRowIndex))
			);
			
			if (!jobDetailsRow.isDisplayed()) {
				throw new IOException("Job details row " + jobDetailsRowIndex + " is not visible");
			}
			
			// Extract job name and code from first column
			try {
				WebElement jobNameCell = jobDetailsRow.findElement(By.xpath(".//td[2]//div"));
				String fullJobText = jobNameCell.getText().trim();
				
				if (fullJobText.contains(" - (") && fullJobText.contains(")")) {
					String[] parts = fullJobText.split(" - \\(");
					if (isSecondProfile) {
						secondJobNameWithInfoMessage = parts[0].trim();
						secondJobCodeWithInfoMessage = parts[1].replace(")", "").trim();
					} else {
						jobNameWithInfoMessage = parts[0].trim();
						jobCodeWithInfoMessage = parts[1].replace(")", "").trim();
					}
				} else {
					if (isSecondProfile) {
						secondJobNameWithInfoMessage = fullJobText;
						secondJobCodeWithInfoMessage = "";
					} else {
						jobNameWithInfoMessage = fullJobText;
						jobCodeWithInfoMessage = "";
					}
				}
				
				LOGGER.info("SUCCESS: Direct extraction successful - Job: '{}' Code: '{}'", 
					isSecondProfile ? secondJobNameWithInfoMessage : jobNameWithInfoMessage,
					isSecondProfile ? secondJobCodeWithInfoMessage : jobCodeWithInfoMessage);
					
			} catch (Exception e) {
				LOGGER.warn("WARNING: Could not extract job name/code using direct method: " + e.getMessage());
				
				// Try alternative approach - get all cell texts
				try {
					List<WebElement> allCells = jobDetailsRow.findElements(By.xpath(".//td"));
					if (!allCells.isEmpty()) {
						String cellText = allCells.get(0).getText().trim();
						if (isSecondProfile) {
							secondJobNameWithInfoMessage = cellText;
							secondJobCodeWithInfoMessage = "";
						} else {
							jobNameWithInfoMessage = cellText;
							jobCodeWithInfoMessage = "";
						}
						LOGGER.info("STATUS: Alternative extraction - Job: '{}'", cellText);
					}
				} catch (Exception e2) {
					LOGGER.error("ERROR: All direct extraction attempts failed: " + e2.getMessage());
					throw new IOException("Failed to extract job details using direct method: " + e2.getMessage());
				}
			}
			
		} catch (Exception e) {
			LOGGER.error("ERROR: Direct extraction failed: " + e.getMessage());
			throw new IOException("Failed to extract job details directly from info message: " + e.getMessage());
		}
	}

	/**
	 * Get the row index (1-based) of a table row element within its tbody
	 */
	private int getRowIndex(WebElement rowElement) {
		try {
			// Find all tr elements in the same tbody
			WebElement tbody = rowElement.findElement(By.xpath("./ancestor::tbody"));
			List<WebElement> allRows = tbody.findElements(By.xpath("./tr"));
			
			// Find the index of our row (1-based)
			for (int i = 0; i < allRows.size(); i++) {
				if (allRows.get(i).equals(rowElement)) {
					return i + 1; // Return 1-based index
				}
			}
		} catch (Exception e) {
			LOGGER.warn("Could not determine row index: " + e.getMessage());
		}
		return -1;
	}

	/**
	 * Calculate profile number based on row index (each profile spans 3 rows)
	 */
	private int getProfileNumber(int rowIndex) {
		if (rowIndex <= 0) return -1;
		return (int) Math.ceil((double) rowIndex / 3.0);
	}

	/**
	 * ENHANCED: Efficiently check current visible profiles for AutoMapped ones with info messages
	 * Returns immediately upon finding the first AutoMapped profile to optimize performance
	 * @param infoMessages List of current info message elements
	 * @param searchAttempt Current search attempt number for logging
	 * @return true if AutoMapped profile found and details extracted, false otherwise
	 */
	@SuppressWarnings("unused")
	private boolean checkCurrentProfilesForAutoMapped(List<WebElement> infoMessages, int searchAttempt) {
		LOGGER.info("INFO: Checking {} visible profiles for AutoMapped ones (search attempt {})", infoMessages.size(), searchAttempt);
		
		for (WebElement infoMessage : infoMessages) {
			if (infoMessage.isDisplayed()) {
				try {
					// Find the table row containing this info message
					WebElement jobRow = infoMessage.findElement(By.xpath("./ancestor::tr"));
					int rowIndex = getRowIndex(jobRow);
					
					if (rowIndex > 0) {
						// CRITICAL: Validate this is an AutoMapped profile (has "View Other Matches")
						if (!isAutoMappedProfile(rowIndex)) {
							int profileNumber = getProfileNumber(rowIndex);
							LOGGER.info("SKIP: Skipping Profile {} (row {}) - Manual Mapping profile", profileNumber, rowIndex);
							continue; // Skip this profile and look for AutoMapped ones
						}
						
						// FOUND: AutoMapped profile with info message!
						profilesWithInfoMessages.add(infoMessage);
						rowIndicesWithInfoMessages.add(rowIndex);
						currentRowIndex = rowIndex;
						
						// Calculate profile number (each profile spans 3 rows)
						int profileNumber = getProfileNumber(rowIndex);
						
						// ENHANCED: Extract job details with robust error handling and validation
						try {
							// Extract job details from the job details row (n-1)
							extractJobDetailsFromRow(rowIndex - 1);
							
							// Extract function/subfunction from current row (n) 
							extractFunctionSubfunctionFromRow(rowIndex);
							
							// CRITICAL: Validate that extraction was successful
							if (jobNameWithInfoMessage.isEmpty() && jobCodeWithInfoMessage.isEmpty()) {
								LOGGER.warn("WARNING: First extraction attempt failed for first profile at row {} - trying alternative approach", rowIndex);
								
								// Try alternative extraction using the info message element directly
								extractJobDetailsDirectlyFromInfoMessage(infoMessage, false);
							}
							
							// Final validation
							if (jobNameWithInfoMessage.isEmpty() && jobCodeWithInfoMessage.isEmpty()) {
								throw new IOException("Failed to extract job details for first profile even with alternative methods - row " + rowIndex);
							}
							
						} catch (Exception extractionError) {
							LOGGER.error("ERROR: Failed to extract job details for first profile at row {}: {}", rowIndex, extractionError.getMessage());
							
							// Try one more time with the direct approach
							try {
								LOGGER.info("INFO: Attempting direct extraction from info message element for first profile...");
								extractJobDetailsDirectlyFromInfoMessage(infoMessage, false);
								
								if (!jobNameWithInfoMessage.isEmpty() || !jobCodeWithInfoMessage.isEmpty()) {
									LOGGER.info("SUCCESS: Direct extraction successful for first profile");
								} else {
									throw new IOException("All extraction methods failed for first profile");
								}
							} catch (Exception finalError) {
								LOGGER.error("ERROR: All extraction attempts failed for first profile: {}", finalError.getMessage());
								throw new IOException("Failed to extract first profile details after multiple attempts: " + finalError.getMessage());
							}
						}
						
						// GLOBAL TRACKING: Store first profile information for duplicate prevention
						globalFirstProfileRowIndex = rowIndex;
						globalFirstProfileNumber = profileNumber;
						globalFirstJobNameWithInfoMessage = jobNameWithInfoMessage;
						globalFirstJobCodeWithInfoMessage = jobCodeWithInfoMessage;
						
						LOGGER.info("SUCCESS: AutoMapped Profile {} found with Info Message (table row {})", profileNumber, rowIndex);
						LOGGER.info("  Job Name: {}", jobNameWithInfoMessage);
						LOGGER.info("  Job Code: {}", jobCodeWithInfoMessage);
						
						return true; // Found and extracted successfully!
					}
				} catch (Exception e) {
					LOGGER.warn("Error processing info message: " + e.getMessage());
					// Continue with next info message
				}
			}
		}
		
		return false; // No AutoMapped profile found in current batch
	}

	/**
	 * ENHANCED: Check if a given row index represents an AutoMapped profile
	 * AutoMapped profiles have "View Other Matches" buttons while Manual Mapping profiles have "Search a different profile" buttons
	 * @param rowIndex The row index to check (1-based)
	 * @return true if AutoMapped profile, false if Manual Mapping profile
	 */
	private boolean isAutoMappedProfile(int rowIndex) {
		try {
		// Check for "View Other Matches" button (AutoMapped profiles) - in RIGHT table (kf-job-container)
		List<WebElement> viewMatchesButtons = driver.findElements(
			By.xpath(String.format("//div[@id='kf-job-container']//tbody//tr[%d]//button[@id='view-matches'] | " +
			                      "//div[@id='kf-job-container']//tbody//tr[%d]//button[contains(@aria-label, 'View other matches')] | " +
			                      "//div[@id='kf-job-container']//tbody//tr[%d]//button[contains(text(), 'View') and contains(text(), 'Other Matches')]", rowIndex, rowIndex, rowIndex))
		);
			
			if (!viewMatchesButtons.isEmpty()) {
				for (WebElement button : viewMatchesButtons) {
					if (button.isDisplayed()) {
						return true; // This is an AutoMapped profile
					}
				}
			}
			
		// Check for "Search a different profile" button (Manual Mapping profiles) - in RIGHT table (kf-job-container)
		List<WebElement> searchButtons = driver.findElements(
			By.xpath(String.format("//div[@id='kf-job-container']//tbody//tr[%d]//button[contains(@aria-label, 'Search a different profile') or contains(text(), 'Search a different profile')]", rowIndex))
		);
			
			if (!searchButtons.isEmpty()) {
				for (WebElement button : searchButtons) {
					if (button.isDisplayed()) {
						return false; // This is a Manual Mapping profile
					}
				}
			}
			
			// If neither button type is found, assume AutoMapped for safety
			LOGGER.debug("Could not determine profile type for row {} - assuming AutoMapped", rowIndex);
			return true;
			
		} catch (Exception e) {
			LOGGER.warn("Error determining profile type for row {}: {}", rowIndex, e.getMessage());
			return false; // Conservative approach - assume Manual Mapping if error
		}
	}

	private void extractJobDetailsFromRow(int jobDetailsRowIndex) {
		try {
			// Get the job details row (contains job name, code, grade, department)
			WebElement jobDetailsRow = driver.findElement(
				By.xpath(String.format("//div[@id='org-job-container']//tbody//tr[%d]", jobDetailsRowIndex))
			);
			
			// Extract Job Name and Code (usually in first column)
			try {
				WebElement jobNameCell = jobDetailsRow.findElement(By.xpath(".//td[2]//div"));
				String fullJobText = jobNameCell.getText().trim();
				if (fullJobText.contains(" - (") && fullJobText.contains(")")) {
					String[] parts = fullJobText.split(" - \\(");
					jobNameWithInfoMessage = parts[0].trim();
					jobCodeWithInfoMessage = parts[1].replace(")", "").trim();
				} else {
					jobNameWithInfoMessage = fullJobText;
					jobCodeWithInfoMessage = "";
				}
			} catch (Exception e) {
				LOGGER.debug("Could not extract job name/code: " + e.getMessage());
			}
		} catch (Exception e) {
			LOGGER.warn("Error extracting job details: " + e.getMessage());
		}
	}

	private void extractFunctionSubfunctionFromRow(int infoMessageRowIndex) {
		try {
			WebElement infoMessageRow = driver.findElement(
				By.xpath(String.format("//div[@id='org-job-container']//tbody//tr[%d]", infoMessageRowIndex))
			);
			
			// Look for Function/Sub-function text with multiple patterns
			try {
				String rowText = infoMessageRow.getText();
				
				String[] functionPatterns = {
					"Function / Sub-function:",
					"Function/Sub-function:",
					"Function:",
					"Sub-function:"
				};
				
				for (String pattern : functionPatterns) {
					if (rowText.contains(pattern)) {
						String[] parts = rowText.split(pattern);
						if (parts.length > 1) {
							String functionPart = parts[1].trim();
							
							// Clean up the function part
							if (functionPart.contains("Reduced match accuracy")) {
								functionPart = functionPart.split("Reduced match accuracy")[0].trim();
							}
							functionPart = functionPart.replaceAll("\\n", " ").replaceAll("\\s+", " ").trim();
							
							functionSubfunctionWithInfoMessage = functionPart;
							break;
						}
					}
				}
			} catch (Exception e) {
				LOGGER.debug("Could not extract function/subfunction: " + e.getMessage());
			}
		} catch (Exception e) {
			LOGGER.warn("Error extracting function/subfunction: " + e.getMessage());
		}
	}

	public void find_and_verify_profile_with_missing_data_has_info_message_displayed() throws IOException {
		try {
			PageObjectHelper.log(LOGGER, "Finding and verifying profile with missing data has Info Message displayed");
			
			// Wait for page to load
			safeSleep(2000);
			
			// ENHANCED: First try to find info messages in currently visible content
			List<WebElement> infoMessages = driver.findElements(
				By.xpath("//div[@id='org-job-container']//div[@role='button' and @aria-label='Reduced match accuracy due to missing data']")
			);
			
			// If no info messages found in visible content, try scrolling to find more profiles
			if (infoMessages.isEmpty()) {
				LOGGER.info("SCROLL: No info messages found in current view, scrolling to load more profiles...");
				
				// Scroll down to load more profiles with info messages
				boolean foundMoreProfiles = scrollDownAndFindMoreAutoMappedProfiles();
				
				if (foundMoreProfiles) {
					// Re-check for info messages after scrolling
					infoMessages = driver.findElements(
						By.xpath("//div[@id='org-job-container']//div[@role='button' and @aria-label='Reduced match accuracy due to missing data']")
					);
					LOGGER.info("STATUS: After scrolling: Found {} info messages", infoMessages.size());
				}
				
				// If still no info messages found, try alternative approaches
				if (infoMessages.isEmpty()) {
					String[] alternativeXPaths = {
						"//div[@id='org-job-container']//div[@aria-label='Reduced match accuracy due to missing data']",
						"//div[@id='org-job-container']//div[contains(@aria-label, 'Reduced match accuracy')]",
						"//div[@id='org-job-container']//div[contains(text(), 'Reduced match accuracy due to missing data')]"
					};
					
					for (String xpath : alternativeXPaths) {
						infoMessages = driver.findElements(By.xpath(xpath));
						if (!infoMessages.isEmpty()) {
							LOGGER.info("STATUS: Found {} info messages using alternative XPath: {}", infoMessages.size(), xpath);
							break;
						}
					}
				}
			}
			
			// Assert that we found info messages (with or without scrolling)
			Assert.assertFalse(infoMessages.isEmpty(), 
				"No Info Messages found for jobs with missing data after checking visible content and scrolling");
			LOGGER.info("STATUS: Found {} Info Messages for profiles with missing data", infoMessages.size());
			
			// Verify at least one Info Message is displayed
			boolean infoMessageDisplayed = false;
			for (WebElement infoMessage : infoMessages) {
				if (infoMessage.isDisplayed()) {
					infoMessageDisplayed = true;
					LOGGER.info("SUCCESS: Found displayed info message");
					break;
				}
			}
			
			Assert.assertTrue(infoMessageDisplayed, 
				"Info Message should be displayed for profiles with missing data (checked visible content + scrolled for more)");
			
			PageObjectHelper.log(LOGGER, "Successfully found and verified profile with missing data has Info Message displayed (searched across multiple pages)");
			
	} catch (Exception e) {
		PageObjectHelper.handleError(LOGGER, "find_and_verify_profile_with_missing_data_has_info_message_displayed",
			"Failed to find and verify profile with missing data has Info Message", e);
	}
	}

	public void find_profile_with_missing_data_and_info_message() throws IOException {
		LOGGER.info("==============================================");
		PageObjectHelper.log(LOGGER, "Searching for AutoMapped profile with missing data");

		try {
			safeSleep(2000);

		int profilesChecked = 0;
		int scrollAttempts = 0;
		int maxScrollAttempts = 50; // Increased scroll attempts for large datasets
		Set<String> processedJobCodes = new HashSet<>(); // Track which jobs we've already checked (for virtual scrolling)
		int previousRowCount = 0; // Track if new rows are being loaded
		int noNewRowsCount = 0; // Track consecutive scrolls with no new rows
		int maxNoNewRowsCount = 5; // Allow multiple consecutive scrolls with no new rows before giving up

	while (scrollAttempts < maxScrollAttempts) {
		scrollAttempts++;

		// Get current visible job rows (each profile has 3 rows: job data + function + info/separator)
		// Note: Table uses VIRTUAL SCROLLING - only ~30 rows in DOM, so process ALL rows each time
		List<WebElement> currentRows = driver.findElements(By.xpath("//div[@id='org-job-container']//tbody//tr"));
		LOGGER.info("Current visible rows: " + currentRows.size() + " (Scroll attempt " + scrollAttempts + ") - Total profiles checked so far: " + profilesChecked);

		// PERFORMANCE: Get RIGHT table rows ONCE per scroll (not per profile!)
		List<WebElement> rightTableAllRows = driver.findElements(By.xpath("//div[@id='kf-job-container']//tbody//tr"));
		// Pre-fetch all row classes using JavaScript (instant, no round-trips)
		JavascriptExecutor js = (JavascriptExecutor) driver;
		@SuppressWarnings("unchecked")
		List<String> rightTableRowClasses = (List<String>) js.executeScript(
			"var rows = document.querySelectorAll('#kf-job-container tbody tr');" +
			"return Array.from(rows).map(r => r.className || '');"
		);

		// Process ALL visible rows (virtual scrolling means indices reset, so can't use lastProcessedRowIndex)
		for (int i = 0; i < currentRows.size() - 2; i += 3) {
			try {
			WebElement jobDataRow = currentRows.get(i);
			WebElement functionRow = currentRows.get(i + 1);
			// Note: Row i+2 is gray separator row (not used)
			
			// Extract job code to check if we've already processed this job (virtual scrolling)
				String jobCode = "";
				try {
					List<WebElement> jobNameElements = jobDataRow.findElements(By.xpath(".//td[2]//div"));
					if (!jobNameElements.isEmpty()) {
						String jobNameText = jobNameElements.get(0).getAttribute("textContent");
						if (jobNameText != null && jobNameText.contains("(") && jobNameText.contains(")")) {
							int startIdx = jobNameText.lastIndexOf("(") + 1;
							int endIdx = jobNameText.lastIndexOf(")");
							if (startIdx > 0 && endIdx > startIdx) {
								jobCode = jobNameText.substring(startIdx, endIdx).trim();
							}
						}
					}
				} catch (Exception e) {
					// Continue without job code
				}
				
				// Skip if we've already processed this job
				if (!jobCode.isEmpty() && processedJobCodes.contains(jobCode)) {
					continue; // Already checked this job
				}
				
				// Mark this job as processed
				if (!jobCode.isEmpty()) {
					processedJobCodes.add(jobCode);
				}
				profilesChecked++;

				// STEP 1: Check for info message in LEFT table (org-job-container)
					// Info message is in the FUNCTION ROW (row i+1), not a separate info row
					boolean hasInfoMsg = false;
					try {
						// Look for info message indicator in the function row
						List<WebElement> infoElements = functionRow.findElements(By.xpath(
							".//td[@colspan]//div[@id='matches-profiles-action-container']//div[contains(text(), 'Reduced match accuracy')] | " +
							".//td[@colspan]//div[contains(@aria-label, 'Reduced match accuracy')] | " +
							".//td[@colspan]//div[contains(text(), 'missing data')]"
						));
						hasInfoMsg = !infoElements.isEmpty();
					} catch (Exception e) {
						// Continue
					}
					
					if (!hasInfoMsg) {
						continue; // Skip - no info message (don't log to save time)
					}

			// STEP 2: Check RIGHT table for "View Other Matches" button using SEPARATOR-BASED BOUNDARIES
			// Simple logic: Each profile bounded by separators has 1 or 2 data rows
			// - 1 data row = Unmapped (has "Find Match" button)
			// - 2 data rows with "View Other Matches" button = AutoMapped
			// - 2 data rows without "View Other Matches" button = Manual Mapping
			boolean isAutoMapped = false;
			int foundAtRow = -1;
			int rightDataRowCount = 0;
			
		// Determine which profile number this is (0-based)
		int profileNumber = i / 3;  // Profile 0, 1, 2, 3...
		
		// Use cached RIGHT table rows and classes (fetched once per scroll, not per profile)
		try {
			// Find the boundaries of our target profile using separator counting
				int currentProfile = -1;
				int startIdx = 0;
				int rightProfileStartRow = -1;
				int rightProfileEndRow = -1;
				
				for (int r = 0; r < rightTableAllRows.size(); r++) {
					String rowClass = rightTableRowClasses.get(r);
						
						if (rowClass != null && rowClass.contains("bg-gray")) {
							// Found a separator - marks end of a profile
							currentProfile++;
							
							if (currentProfile == profileNumber) {
								// This separator ends our target profile
								rightProfileStartRow = startIdx;
								rightProfileEndRow = r - 1;
								break;
							}
							// Next profile starts after this separator
							startIdx = r + 1;
						}
					}
					
					// If we're looking for the last profile and didn't find ending separator
					if (currentProfile == profileNumber - 1 && rightProfileEndRow == -1) {
						rightProfileStartRow = startIdx;
						rightProfileEndRow = rightTableAllRows.size() - 1;
					}
					
					// Now check the profile's data rows
					if (rightProfileStartRow >= 0 && rightProfileEndRow >= rightProfileStartRow) {
					// Count non-separator data rows
					List<WebElement> dataRows = new ArrayList<>();
					for (int r = rightProfileStartRow; r <= rightProfileEndRow; r++) {
						String rowClass = rightTableRowClasses.get(r);
						if (rowClass == null || !rowClass.contains("bg-gray")) {
							dataRows.add(rightTableAllRows.get(r));
						}
					}
						
					rightDataRowCount = dataRows.size();
					
					// Rule: Check 2nd row ONLY if profile has 2 data rows
					if (dataRows.size() == 2) {
						WebElement secondRow = dataRows.get(1);
						
						// PERFORMANCE: Use JavaScript to check for button (instant, no waits)
						Boolean hasButton = (Boolean) js.executeScript(
							"return arguments[0].querySelector('button#view-matches') !== null;",
							secondRow
						);
						
						if (hasButton != null && hasButton) {
							// Found "View Other Matches" button
							isAutoMapped = true;
							// Find actual row index for logging and clicking
							for (int r = rightProfileStartRow; r <= rightProfileEndRow; r++) {
								if (rightTableAllRows.get(r) == secondRow) {
									foundAtRow = r + 1;  // ✅ Convert to 1-based for XPath position()
									break;
								}
							}
						}
						// If no button found in 2nd row = Manual Mapping (has "Search a different profile")
					}
				// If 1 data row = Unmapped (has "Find Match" button) - also has info message
			}
		} catch (Exception e) {
			LOGGER.debug("Error checking RIGHT table boundaries: {}", e.getMessage());
		}
			
			// Log profile analysis (extract job name only when needed)
			if (hasInfoMsg) {
				// Extract job name only when we need to log it
				String jobName = "Unknown";
				try {
					List<WebElement> jobNameElements = jobDataRow.findElements(By.xpath(".//td[2]//div"));
					if (!jobNameElements.isEmpty()) {
						String jobNameCodeText = jobNameElements.get(0).getAttribute("textContent");
						if (jobNameCodeText != null) {
							jobNameCodeText = jobNameCodeText.trim();
							if (jobNameCodeText.contains(" - (")) {
								int dashIndex = jobNameCodeText.lastIndexOf(" - (");
								jobName = jobNameCodeText.substring(0, dashIndex).trim();
							} else {
								jobName = jobNameCodeText;
							}
						}
					}
				} catch (Exception e) {
					// Keep default "Unknown"
				}
				
				// Determine profile type and log cleanly
				String profileType;
				if (isAutoMapped) {
					profileType = "AutoMapped (View Other Matches @row" + foundAtRow + ")";
				} else if (rightDataRowCount == 2) {
					profileType = "Manual Mapping (Search different profile)";
				} else if (rightDataRowCount == 1) {
					profileType = "Unmapped (Find Match)";
				} else {
					profileType = "Unknown";
				}
				LOGGER.info("Profile {} ({}) - {}", profilesChecked, jobName, profileType);
			}
					
					// STEP 3: Valid profile = InfoMsg=true AND AutoMapped=true
					if (!isAutoMapped) {
						continue; // Skip - Manual Mapping profile
					}

						// Profile has info message AND is AutoMapped - extract details
						// PERFORMANCE: Use getAttribute('textContent') instead of getText() for speed
						
						// Extract job name for the matching profile
						String jobName = "Unknown";
						try {
							List<WebElement> jobNameElements = jobDataRow.findElements(By.xpath(".//td[2]//div"));
							if (!jobNameElements.isEmpty()) {
								String jobNameCodeText = jobNameElements.get(0).getAttribute("textContent");
								if (jobNameCodeText != null) {
									jobNameCodeText = jobNameCodeText.trim();
									if (jobNameCodeText.contains(" - (")) {
										int dashIndex = jobNameCodeText.lastIndexOf(" - (");
										jobName = jobNameCodeText.substring(0, dashIndex).trim();
									} else {
										jobName = jobNameCodeText;
									}
								}
							}
						} catch (Exception e) {
							// Keep default "Unknown"
						}

						// Extract Grade (column 3 in LEFT table)
						List<WebElement> gradeElements = jobDataRow.findElements(By.xpath(".//td[3]//div"));
						String grade = "";
						if (!gradeElements.isEmpty()) {
							String gradeText = gradeElements.get(0).getAttribute("textContent");
							grade = (gradeText != null) ? gradeText.trim() : "";
						}

						// Extract Department (column 4 in LEFT table)
						List<WebElement> deptElements = jobDataRow.findElements(By.xpath(".//td[4]//div"));
						String department = "";
						if (!deptElements.isEmpty()) {
							String deptText = deptElements.get(0).getAttribute("textContent");
							department = (deptText != null) ? deptText.trim() : "";
						}

						// Extract Function/Sub-function from the function row (after "Function / Sub-function: " text)
						String functionSubfunction = "";
						try {
							String fullText = functionRow.getAttribute("textContent");
							if (fullText != null && fullText.contains("Function / Sub-function:")) {
								// Extract text after "Function / Sub-function: " label
								String[] parts = fullText.split("Function / Sub-function:");
								if (parts.length > 1) {
									functionSubfunction = parts[1].split("Reduced match accuracy")[0].trim();
								}
							}
						} catch (Exception e) {
							functionSubfunction = "";
						}

						// Check if at least one field is missing (N/A or -)
						boolean hasMissingData = false;
						if (grade.isEmpty() || grade.equals("N/A") || grade.equals("-")) {
							hasMissingData = true;
						}
						if (department.isEmpty() || department.equals("N/A") || department.equals("-")) {
							hasMissingData = true;
						}
						if (functionSubfunction.isEmpty() || functionSubfunction.equals("N/A") || functionSubfunction.equals("-") || 
							functionSubfunction.equals("N/A | N/A") || functionSubfunction.equals("- | -") || functionSubfunction.equals("- | N/A")) {
							hasMissingData = true;
						}

					if (hasMissingData) {
						LOGGER.info("✓ Found AutoMapped profile: {} (Profile #{})", jobName, profilesChecked);
						LOGGER.info("  Details - Grade: {}, Dept: {}, Func: {}", grade, department, functionSubfunction);
						ExtentCucumberAdapter.addTestStepLog("Found AutoMapped profile: " + jobName);

							// Store in scenario context (using existing variable names)
							jobNameWithInfoMessage = jobName;
							gradeWithInfoMessage = grade;
							departmentWithInfoMessage = department;
							functionSubfunctionWithInfoMessage = functionSubfunction;
							
							// Store row index for button click (RIGHT table row number where button was found)
							currentRowIndex = foundAtRow;
							currentRowIndexStatic = foundAtRow; // Also store in static for persistence

						LOGGER.debug("Stored row index: {}", currentRowIndex);
						return; // Found it - exit method
						}

					} catch (Exception e) {
						LOGGER.debug("Error processing profile at position " + profilesChecked + ": " + e.getMessage());
						continue; // Skip this profile if extraction fails
					}
				}

			// Scroll down to load next batch of profiles (lazy loading)
			LOGGER.debug("Scrolling to load more profiles...");
			if (!currentRows.isEmpty()) {
				// Scroll to last row to trigger lazy loading
				WebElement lastRow = currentRows.get(currentRows.size() - 1);
				jsExecutor.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'end'});", lastRow);
				safeSleep(500);
				
				// Scroll the container itself (not window) to trigger lazy loading
				jsExecutor.executeScript(
					"var container = document.getElementById('org-job-container');" +
					"if (container) { container.scrollTop = container.scrollHeight; }"
				);
				
				// Wait for lazy loading to complete (increased from 1s to 4s total)
				safeSleep(2000); // Wait for server response
				PerformanceUtils.waitForSpinnersToDisappear(driver, 5); // Wait for loading spinners
				safeSleep(1000); // Additional buffer for DOM to render
			} else {
				jsExecutor.executeScript("window.scrollTo(0, document.body.scrollHeight);");
				safeSleep(2000);
				PerformanceUtils.waitForSpinnersToDisappear(driver, 5);
			}

				// Check if no new rows loaded after scroll (improved detection)
				List<WebElement> rowsAfterScroll = driver.findElements(By.xpath("//div[@id='org-job-container']//tbody//tr"));
				int rowsAfterScrollCount = rowsAfterScroll.size();
				
				if (rowsAfterScrollCount == previousRowCount && previousRowCount > 0) {
				noNewRowsCount++;
				LOGGER.debug("No new profiles after scroll {} (consecutive: {}/{})", scrollAttempts, noNewRowsCount, maxNoNewRowsCount);
				
				// Only exit if we've had multiple consecutive scrolls with no new rows
				if (noNewRowsCount >= maxNoNewRowsCount) {
					LOGGER.info("Reached end - Total profiles checked: {}", profilesChecked);
					break;
				}
			} else {
				// Reset counter if new rows were loaded
				noNewRowsCount = 0;
				LOGGER.debug("More profiles loaded (visible rows: {})", rowsAfterScrollCount);
			}
			
			previousRowCount = rowsAfterScrollCount;
			}

		// If we reach here, no suitable profile was found
		LOGGER.info(" Fallback search complete: Checked ALL " + profilesChecked + " profiles, none have missing data with info message");
		ExtentCucumberAdapter.addTestStepLog(" No AutoMapped profiles found with missing data and info message (checked all " + profilesChecked + " profiles)");
		LOGGER.warn("SKIPPING SCENARIO: No AutoMapped profiles with missing data and info message found (checked all " + profilesChecked + " profiles)");
		
		// Throw SkipException to mark scenario as SKIPPED in TestNG (not FAILED)
		throw new SkipException("SKIPPED: No AutoMapped profiles with missing data and info message found after checking all " + profilesChecked + " profiles. This scenario requires at least one profile with missing data to execute.");
		
	} catch (Exception e) {
		PageObjectHelper.handleError(LOGGER, "find_profile_with_missing_data_and_info_message",
			"Error searching for AutoMapped profile with missing data", e);
	}
	}

	public void find_second_profile_with_missing_data_and_info_message() throws IOException {
		LOGGER.info("==============================================");
		LOGGER.info("Searching for SECOND AutoMapped profile with missing data");
		ExtentCucumberAdapter.addTestStepLog("Searching for SECOND AutoMapped profile with missing data");

		String firstJobName = jobNameWithInfoMessage;
		LOGGER.debug("First job to skip: '{}'", firstJobName);
		
		if (firstJobName == null || firstJobName.isEmpty()) {
			LOGGER.warn("WARNING: First job name is null or empty - cannot skip first profile");
		}

		try {
			safeSleep(2000);

		int profilesChecked = 0;
		int scrollAttempts = 0;
		int maxScrollAttempts = 200; // Increased scroll attempts for large datasets
		Set<String> processedJobCodes = new HashSet<>(); // Track which jobs we've already checked (for virtual scrolling)
		int previousRowCount = 0; // Track if new rows are being loaded
		int noNewRowsCount = 0; // Track consecutive scrolls with no new rows
		int maxNoNewRowsCount = 5; // Allow multiple consecutive scrolls with no new rows before giving up

		while (scrollAttempts < maxScrollAttempts) {
			scrollAttempts++;

			// Get current visible job rows (each profile has 3 rows: job data + function + info/separator)
			// Note: Table uses VIRTUAL SCROLLING - only ~30 rows in DOM, so process ALL rows each time
			List<WebElement> currentRows = driver.findElements(By.xpath("//div[@id='org-job-container']//tbody//tr"));
			LOGGER.info("Current visible rows: " + currentRows.size() + " (Scroll attempt " + scrollAttempts + ") - Total profiles checked so far: " + profilesChecked);

		// PERFORMANCE: Get RIGHT table rows ONCE per scroll (not per profile!)
		List<WebElement> rightTableAllRows = driver.findElements(By.xpath("//div[@id='kf-job-container']//tbody//tr"));
		// Pre-fetch all row classes using JavaScript (instant, no round-trips)
		JavascriptExecutor js2 = (JavascriptExecutor) driver;
		@SuppressWarnings("unchecked")
		List<String> rightTableRowClasses = (List<String>) js2.executeScript(
			"var rows = document.querySelectorAll('#kf-job-container tbody tr');" +
			"return Array.from(rows).map(r => r.className || '');"
		);

		// Process ALL visible rows (virtual scrolling means indices reset, so can't use lastProcessedRowIndex)
		for (int i = 0; i < currentRows.size() - 2; i += 3) {
			try {
			WebElement jobDataRow = currentRows.get(i);
			WebElement functionRow = currentRows.get(i + 1);
			// Note: Row i+2 is gray separator row (not used)

		// Extract job name AND job code for tracking
		// Job code used to skip already-processed jobs (virtual scrolling)
		String jobCode = "";
					// PERFORMANCE: Use getAttribute('textContent') instead of getText() (much faster)
					String jobName = "Unknown";
					try {
						List<WebElement> jobNameElements = jobDataRow.findElements(By.xpath(".//td[2]//div"));
						if (!jobNameElements.isEmpty()) {
							// Use textContent attribute (instant) instead of getText() (slow - waits for visibility)
							String jobNameCodeText = jobNameElements.get(0).getAttribute("textContent");
							if (jobNameCodeText != null) {
								jobNameCodeText = jobNameCodeText.trim();
								// Extract job name
								if (jobNameCodeText.contains(" - (")) {
									int dashIndex = jobNameCodeText.lastIndexOf(" - (");
									jobName = jobNameCodeText.substring(0, dashIndex).trim();
								} else {
									jobName = jobNameCodeText;
								}
								// Extract job code
								if (jobNameCodeText.contains("(") && jobNameCodeText.contains(")")) {
									int startIdx = jobNameCodeText.lastIndexOf("(") + 1;
									int endIdx = jobNameCodeText.lastIndexOf(")");
									if (startIdx > 0 && endIdx > startIdx) {
										jobCode = jobNameCodeText.substring(startIdx, endIdx).trim();
									}
								}
							}
						}
					} catch (Exception e) {
						// Continue
					}
					
					// Skip if we've already processed this job (virtual scrolling)
					if (!jobCode.isEmpty() && processedJobCodes.contains(jobCode)) {
						continue; // Already checked this job
					}
					
				// Skip if this is the first job
				if (firstJobName != null && !firstJobName.isEmpty() && jobName.equals(firstJobName)) {
						LOGGER.debug("Skipping first profile: {}", jobName);
						// Still mark as processed to avoid rechecking
						if (!jobCode.isEmpty()) {
							processedJobCodes.add(jobCode);
						}
						continue;
					}
						
						// Mark this job as processed
						if (!jobCode.isEmpty()) {
							processedJobCodes.add(jobCode);
						}
						profilesChecked++;

					// STEP 1: Check for info message in LEFT table (org-job-container)
					// Info message is in the FUNCTION ROW (row i+1), not a separate info row
					boolean hasInfoMsg = false;
					try {
						// Look for info message indicator in the function row
						List<WebElement> infoElements = functionRow.findElements(By.xpath(
							".//td[@colspan]//div[@id='matches-profiles-action-container']//div[contains(text(), 'Reduced match accuracy')] | " +
							".//td[@colspan]//div[contains(@aria-label, 'Reduced match accuracy')] | " +
							".//td[@colspan]//div[contains(text(), 'missing data')]"
						));
						hasInfoMsg = !infoElements.isEmpty();
					} catch (Exception e) {
						// Continue
					}
					
				if (!hasInfoMsg) {
					continue; // Skip - no info message (don't log to save time)
				}

			// STEP 2: Check RIGHT table for "View Other Matches" button using SEPARATOR-BASED BOUNDARIES
			// Same logic as first method: use separators to find exact boundaries
			boolean isAutoMapped = false;
			int foundAtRow = -1;
			int rightDataRowCount = 0;
			
		// Determine which profile number this is (0-based)
		int profileNumber = i / 3;
		
		// Use cached RIGHT table rows and classes (fetched once per scroll, not per profile)
		try {
			// Find the boundaries of our target profile using separator counting
			int currentProfile = -1;
			int startIdx = 0;
			int rightProfileStartRow = -1;
			int rightProfileEndRow = -1;
			
			for (int r = 0; r < rightTableAllRows.size(); r++) {
				String rowClass = rightTableRowClasses.get(r);
					
					if (rowClass != null && rowClass.contains("bg-gray")) {
						// Found a separator - marks end of a profile
						currentProfile++;
						
						if (currentProfile == profileNumber) {
							// This separator ends our target profile
							rightProfileStartRow = startIdx;
							rightProfileEndRow = r - 1;
							break;
						}
						// Next profile starts after this separator
						startIdx = r + 1;
					}
				}
				
				// If we're looking for the last profile and didn't find ending separator
				if (currentProfile == profileNumber - 1 && rightProfileEndRow == -1) {
					rightProfileStartRow = startIdx;
					rightProfileEndRow = rightTableAllRows.size() - 1;
				}
				
				// Now check the profile's data rows
				if (rightProfileStartRow >= 0 && rightProfileEndRow >= rightProfileStartRow) {
					// Count non-separator data rows
					List<WebElement> dataRows = new ArrayList<>();
					for (int r = rightProfileStartRow; r <= rightProfileEndRow; r++) {
						String rowClass = rightTableRowClasses.get(r);
						if (rowClass == null || !rowClass.contains("bg-gray")) {
							dataRows.add(rightTableAllRows.get(r));
						}
					}
						
						rightDataRowCount = dataRows.size();
						
						// Rule: Check 2nd row ONLY if profile has 2 data rows
					if (dataRows.size() == 2) {
						WebElement secondRow = dataRows.get(1);
						
						// PERFORMANCE: Use JavaScript to check for button (instant, no waits)
						Boolean hasButton = (Boolean) js2.executeScript(
							"return arguments[0].querySelector('button#view-matches') !== null;",
							secondRow
						);
						
						if (hasButton != null && hasButton) {
							// Found "View Other Matches" button
							isAutoMapped = true;
							// Find actual row index for logging and clicking
							for (int r = rightProfileStartRow; r <= rightProfileEndRow; r++) {
								if (rightTableAllRows.get(r) == secondRow) {
									foundAtRow = r + 1;  // ✅ Convert to 1-based for XPath position()
									break;
								}
							}
						}
					}
					}
				} catch (Exception e) {
					LOGGER.debug("Error checking RIGHT table boundaries for second profile: {}", e.getMessage());
				}
				
				// Log profile analysis (cleaner format)
				if (hasInfoMsg) {
					String profileType;
					if (isAutoMapped) {
						profileType = "AutoMapped (View Other Matches @row" + foundAtRow + ")";
					} else if (rightDataRowCount == 2) {
						profileType = "Manual Mapping (Search different profile)";
					} else if (rightDataRowCount == 1) {
						profileType = "Unmapped (Find Match)";
					} else {
						profileType = "Unknown";
					}
					LOGGER.info("Profile {} ({}) - {}", profilesChecked, jobName, profileType);
				}
					
					// STEP 3: Valid profile = InfoMsg=true AND AutoMapped=true
					if (!isAutoMapped) {
						continue; // Skip - Manual Mapping profile
					}

						// Profile has info message AND is AutoMapped - extract details
						// PERFORMANCE: Use getAttribute('textContent') instead of getText() for speed
						// Note: jobName already extracted earlier in the loop for firstJobName comparison

						// Extract Grade (column 3 in LEFT table)
						List<WebElement> gradeElements = jobDataRow.findElements(By.xpath(".//td[3]//div"));
						String grade = "";
						if (!gradeElements.isEmpty()) {
							String gradeText = gradeElements.get(0).getAttribute("textContent");
							grade = (gradeText != null) ? gradeText.trim() : "";
						}

						// Extract Department (column 4 in LEFT table)
						List<WebElement> deptElements = jobDataRow.findElements(By.xpath(".//td[4]//div"));
						String department = "";
						if (!deptElements.isEmpty()) {
							String deptText = deptElements.get(0).getAttribute("textContent");
							department = (deptText != null) ? deptText.trim() : "";
						}

						// Extract Function/Sub-function from the function row (after "Function / Sub-function: " text)
						String functionSubfunction = "";
						try {
							String fullText = functionRow.getAttribute("textContent");
							if (fullText != null && fullText.contains("Function / Sub-function:")) {
								// Extract text after "Function / Sub-function: " label
								String[] parts = fullText.split("Function / Sub-function:");
								if (parts.length > 1) {
									functionSubfunction = parts[1].split("Reduced match accuracy")[0].trim();
								}
							}
						} catch (Exception e) {
							functionSubfunction = "";
						}

						// Check if at least one field is missing (N/A or -)
						boolean hasMissingData = false;
						if (grade.isEmpty() || grade.equals("N/A") || grade.equals("-")) {
							hasMissingData = true;
						}
						if (department.isEmpty() || department.equals("N/A") || department.equals("-")) {
							hasMissingData = true;
						}
						if (functionSubfunction.isEmpty() || functionSubfunction.equals("N/A") || functionSubfunction.equals("-") || 
							functionSubfunction.equals("N/A | N/A") || functionSubfunction.equals("- | -") || functionSubfunction.equals("- | N/A")) {
							hasMissingData = true;
						}

					if (hasMissingData) {
						LOGGER.info("✓ Found SECOND AutoMapped profile: {} (Profile #{})", jobName, profilesChecked);
						LOGGER.info("  Details - Grade: {}, Dept: {}, Func: {}", grade, department, functionSubfunction);
						ExtentCucumberAdapter.addTestStepLog("Found SECOND AutoMapped profile: " + jobName);

							// Store in scenario context (using existing variable names)
							secondJobNameWithInfoMessage = jobName;
							secondGradeWithInfoMessage = grade;
							secondDepartmentWithInfoMessage = department;
							secondFunctionSubfunctionWithInfoMessage = functionSubfunction;
							
							// Store row index for button click (RIGHT table row number where button was found)
							secondCurrentRowIndex = foundAtRow;
							secondCurrentRowIndexStatic = foundAtRow; // Also store in static for persistence

						LOGGER.debug("Stored row index: {}", secondCurrentRowIndex);
						return; // Found it - exit method
						}

				} catch (Exception e) {
						LOGGER.debug("Error processing profile at position " + profilesChecked + ": " + e.getMessage());
						continue; // Skip this profile if extraction fails
					}
				}

			// Scroll down to load next batch of profiles (lazy loading)
			LOGGER.debug("Scrolling to load more profiles...");
			if (!currentRows.isEmpty()) {
				// Scroll to last row to trigger lazy loading
				WebElement lastRow = currentRows.get(currentRows.size() - 1);
				jsExecutor.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'end'});", lastRow);
				safeSleep(500);
				
				// Scroll the container itself (not window) to trigger lazy loading
				jsExecutor.executeScript(
					"var container = document.getElementById('org-job-container');" +
					"if (container) { container.scrollTop = container.scrollHeight; }"
				);
				
				// Wait for lazy loading to complete (increased from 1s to 4s total)
				safeSleep(2000); // Wait for server response
				PerformanceUtils.waitForSpinnersToDisappear(driver, 5); // Wait for loading spinners
				safeSleep(1000); // Additional buffer for DOM to render
			} else {
				jsExecutor.executeScript("window.scrollTo(0, document.body.scrollHeight);");
				safeSleep(2000);
				PerformanceUtils.waitForSpinnersToDisappear(driver, 5);
			}

				// Check if no new rows loaded after scroll (improved detection)
				List<WebElement> rowsAfterScroll = driver.findElements(By.xpath("//div[@id='org-job-container']//tbody//tr"));
				int rowsAfterScrollCount = rowsAfterScroll.size();
				
				if (rowsAfterScrollCount == previousRowCount && previousRowCount > 0) {
				noNewRowsCount++;
				LOGGER.debug("No new profiles after scroll {} (consecutive: {}/{})", scrollAttempts, noNewRowsCount, maxNoNewRowsCount);
				
				// Only exit if we've had multiple consecutive scrolls with no new rows
				if (noNewRowsCount >= maxNoNewRowsCount) {
					LOGGER.info("Reached end - Total profiles checked: {}", profilesChecked);
					break;
				}
			} else {
				// Reset counter if new rows were loaded
				noNewRowsCount = 0;
				LOGGER.debug("More profiles loaded (visible rows: {})", rowsAfterScrollCount);
			}
			
			previousRowCount = rowsAfterScrollCount;
			}

		// If we reach here, no suitable second profile was found
		LOGGER.info(" Fallback search complete: Checked ALL " + profilesChecked + " profiles, no SECOND profile found with missing data and info message");
		ExtentCucumberAdapter.addTestStepLog(" No SECOND AutoMapped profile found with missing data and info message (checked all " + profilesChecked + " profiles)");
		LOGGER.warn("SKIPPING SCENARIO: No SECOND AutoMapped profile with missing data and info message found (checked all " + profilesChecked + " profiles)");
		
		// Throw SkipException to mark scenario as SKIPPED in TestNG (not FAILED)
		throw new SkipException("SKIPPED: No SECOND AutoMapped profile with missing data and info message found after checking all " + profilesChecked + " profiles. This scenario requires at least two profiles with missing data to execute.");
			
		} catch (Exception e) {
		PageObjectHelper.handleError(LOGGER, "find_second_profile_with_missing_data_and_info_message",
			"Error searching for SECOND AutoMapped profile with missing data", e);
	}
	}
	
	// Helper methods for second profile extraction
	@SuppressWarnings("unused")
	private void extractJobDetailsFromRowForSecondProfile(int jobDetailsRowIndex) {
		try {
			// Get the job details row (contains job name, code, grade, department)
			WebElement jobDetailsRow = driver.findElement(
				By.xpath(String.format("//div[@id='org-job-container']//tbody//tr[%d]", jobDetailsRowIndex))
			);
			
			// Extract Job Name and Code (usually in first column)
			try {
				WebElement jobNameCell = jobDetailsRow.findElement(By.xpath(".//td[2]//div"));
				String fullJobText = jobNameCell.getText().trim();
				if (fullJobText.contains(" - (") && fullJobText.contains(")")) {
					String[] parts = fullJobText.split(" - \\(");
					secondJobNameWithInfoMessage = parts[0].trim();
					secondJobCodeWithInfoMessage = parts[1].replace(")", "").trim();
				} else {
					secondJobNameWithInfoMessage = fullJobText;
					secondJobCodeWithInfoMessage = "";
				}
			} catch (Exception e) {
				LOGGER.debug("Could not extract second profile job name/code: " + e.getMessage());
			}
		} catch (Exception e) {
			LOGGER.warn("Error extracting second profile job details: " + e.getMessage());
		}
	}
	
	@SuppressWarnings("unused")
	private void extractFunctionSubfunctionFromRowForSecondProfile(int infoMessageRowIndex) {
		try {
			WebElement infoMessageRow = driver.findElement(
				By.xpath(String.format("//div[@id='org-job-container']//tbody//tr[%d]", infoMessageRowIndex))
			);
			
			// Look for Function/Sub-function text
			try {
				String rowText = infoMessageRow.getText();
				String[] functionPatterns = {
					"Function / Sub-function:",
					"Function/Sub-function:",
					"Function:",
					"Sub-function:"
				};
				
				for (String pattern : functionPatterns) {
					if (rowText.contains(pattern)) {
						String[] parts = rowText.split(pattern);
						if (parts.length > 1) {
							String functionPart = parts[1].trim();
							
							// Clean up the function part
							if (functionPart.contains("Reduced match accuracy")) {
								functionPart = functionPart.split("Reduced match accuracy")[0].trim();
							}
							functionPart = functionPart.replaceAll("\\n", " ").replaceAll("\\s+", " ").trim();
							
							secondFunctionSubfunctionWithInfoMessage = functionPart;
							break;
						}
					}
				}
			} catch (Exception e) {
				LOGGER.warn("Error extracting second profile function/subfunction: " + e.getMessage());
			}
		} catch (Exception e) {
			LOGGER.warn("Error extracting second profile function/subfunction: " + e.getMessage());
		}
	}

	public void extract_job_details_from_second_profile_with_info_message() throws IOException {
		// NOTE: Job details are already extracted and logged during find_second_profile_with_missing_data_and_info_message()
		// This method validates that extraction was successful
		try {
			if (secondJobNameWithInfoMessage.isEmpty() && secondJobCodeWithInfoMessage.isEmpty()) {
				throw new IOException("Second profile job details not found. Please call find_second_profile_with_missing_data_and_info_message() first.");
			}
			// Details already logged during search - no need to log again
		} catch (Exception e) {
			LOGGER.error("Error: Second profile job details not found: " + e.getMessage());
			throw new IOException("Failed to validate second profile job details: " + e.getMessage());
		}
	}

	// Missing methods needed by Step Definitions
	public void verify_info_message_contains_text_about_reduced_match_accuracy_due_to_missing_data() throws IOException {
		try {
			LOGGER.info("Verifying Info Message contains correct text about reduced match accuracy");
			
			List<WebElement> infoMessageTexts = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
				By.xpath("//div[@role='button' and @aria-label='Reduced match accuracy due to missing data']//div[contains(text(), 'Reduced match accuracy due to missing data')]")
			));
			
			Assert.assertFalse(infoMessageTexts.isEmpty(), "Info Message text elements not found");
			
			boolean correctTextFound = false;
			for (WebElement messageText : infoMessageTexts) {
				String actualText = messageText.getText().trim();
				if (actualText.contains("Reduced match accuracy due to missing data")) {
					correctTextFound = true;
					LOGGER.info("Found correct Info Message text: " + actualText);
					break;
				}
			}
			
			Assert.assertTrue(correctTextFound, "Info Message should contain text about 'Reduced match accuracy due to missing data'");
			
			ExtentCucumberAdapter.addTestStepLog("Successfully verified Info Message contains correct text about reduced match accuracy due to missing data");
			LOGGER.info("Successfully verified Info Message text");
			
	} catch (Exception e) {
		PageObjectHelper.handleError(LOGGER, "verify_info_message_contains_text_about_reduced_match_accuracy_due_to_missing_data",
			"Failed to verify Info Message text", e);
	}
	}

	public void find_and_verify_second_profile_with_missing_data_has_info_message_displayed() throws IOException {
		try {
			LOGGER.info("Finding and verifying second profile with missing data has Info Message displayed");
			safeSleep(2000);
			
			// Use same scrolling approach as first profile
			List<WebElement> infoMessages = driver.findElements(
				By.xpath("//div[@id='org-job-container']//div[@role='button' and @aria-label='Reduced match accuracy due to missing data']")
			);
			
			if (infoMessages.isEmpty()) {
				LOGGER.info("SCROLL: No info messages found in current view for second profile, scrolling...");
				scrollDownAndFindMoreAutoMappedProfiles();
				
				infoMessages = driver.findElements(
					By.xpath("//div[@id='org-job-container']//div[@role='button' and @aria-label='Reduced match accuracy due to missing data']")
				);
				LOGGER.info("STATUS: After scrolling: Found {} info messages for second profile verification", infoMessages.size());
			}
			
			Assert.assertFalse(infoMessages.isEmpty(), "No Info Messages found for second profile with missing data");
			LOGGER.info("STATUS: Found {} Info Messages for second profile verification", infoMessages.size());
			
			// Verify at least one is displayed
			boolean infoMessageDisplayed = false;
			for (WebElement infoMessage : infoMessages) {
				if (infoMessage.isDisplayed()) {
					infoMessageDisplayed = true;
					LOGGER.info("SUCCESS: Found displayed info message for second profile");
					break;
				}
			}
			
			Assert.assertTrue(infoMessageDisplayed, "Info Message should be displayed for second profile with missing data");
			
			ExtentCucumberAdapter.addTestStepLog("Successfully found and verified second profile with missing data has Info Message displayed");
			LOGGER.info("SUCCESS: Successfully verified second profile with missing data has Info Message displayed");
			
		} catch (Exception e) {
			LOGGER.error("ERROR: Error finding second profile with missing data and Info Message: " + e.getMessage());
			throw new IOException("Failed to find and verify second profile with missing data has Info Message: " + e.getMessage());
		}
	}

	public void extract_job_details_from_profile_with_info_message() throws IOException {
		// NOTE: Job details are already extracted and logged during find_profile_with_missing_data_and_info_message()
		// This method validates that extraction was successful
		try {
			if (jobNameWithInfoMessage.isEmpty() && jobCodeWithInfoMessage.isEmpty()) {
				throw new IOException("First profile job details not found. Please call find_profile_with_missing_data_and_info_message() first.");
			}
			// Details already logged during search - no need to log again
	} catch (Exception e) {
		PageObjectHelper.handleError(LOGGER, "extract_job_details_from_profile_with_info_message",
			"Failed to validate first profile job details", e);
	}
	}

	public void verify_info_message_is_still_displayed_in_job_comparison_page() throws IOException {
		try {
			LOGGER.info("Verifying Info Message is still displayed in Job Comparison page for first profile");
			safeSleep(2000);
			
			// Look for info messages in Job Comparison page
			List<WebElement> infoMessages = driver.findElements(
				By.xpath("//div[@role='button' and @aria-label='Reduced match accuracy due to missing data'] | //div[contains(text(), 'Reduced match accuracy due to missing data')]")
			);
			
			Assert.assertFalse(infoMessages.isEmpty(), "Info Message should still be displayed in Job Comparison page for first profile");
			
			boolean infoMessageDisplayed = false;
			for (WebElement infoMessage : infoMessages) {
				if (infoMessage.isDisplayed()) {
					infoMessageDisplayed = true;
					LOGGER.info("SUCCESS: Info Message is still displayed in Job Comparison page for first profile");
					break;
				}
			}
			
			Assert.assertTrue(infoMessageDisplayed, "Info Message should be visible in Job Comparison page for first profile");
			
			ExtentCucumberAdapter.addTestStepLog("Successfully verified Info Message persists in Job Comparison page for first profile");
			LOGGER.info("SUCCESS: Info Message persistence verified for first profile");
			
	} catch (Exception e) {
		PageObjectHelper.handleError(LOGGER, "verify_info_message_is_still_displayed_in_job_comparison_page",
			"Failed to verify Info Message persistence in Job Comparison page for first profile", e);
	}
	}

	public void verify_info_message_is_still_displayed_in_job_comparison_page_for_second_profile() throws IOException {
		try {
			LOGGER.info("Verifying Info Message is still displayed in Job Comparison page for second profile");
			safeSleep(2000);
			
			// Look for info messages in Job Comparison page
			List<WebElement> infoMessages = driver.findElements(
				By.xpath("//div[@role='button' and @aria-label='Reduced match accuracy due to missing data'] | //div[contains(text(), 'Reduced match accuracy due to missing data')]")
			);
			
			Assert.assertFalse(infoMessages.isEmpty(), "Info Message should still be displayed in Job Comparison page for second profile");
			
			boolean infoMessageDisplayed = false;
			for (WebElement infoMessage : infoMessages) {
				if (infoMessage.isDisplayed()) {
					infoMessageDisplayed = true;
					LOGGER.info("SUCCESS: Info Message is still displayed in Job Comparison page for second profile");
					break;
				}
			}
			
			Assert.assertTrue(infoMessageDisplayed, "Info Message should be visible in Job Comparison page for second profile");
			
			ExtentCucumberAdapter.addTestStepLog("Successfully verified Info Message persists in Job Comparison page for second profile");
			LOGGER.info("SUCCESS: Info Message persistence verified for second profile");
			
		} catch (Exception e) {
			LOGGER.error("ERROR: Error verifying Info Message persistence for second profile: " + e.getMessage());
			throw new IOException("Failed to verify Info Message persistence in Job Comparison page for second profile: " + e.getMessage());
		}
	}

	public void verify_info_message_contains_text_about_reduced_match_accuracy_due_to_missing_data_for_second_profile() throws IOException {
		try {
			LOGGER.info("Verifying Info Message contains correct text about reduced match accuracy for second profile");
			
			// Find Info Message texts specifically for second profile validation
			List<WebElement> infoMessageTexts = driver.findElements(
				By.xpath("//div[@role='button' and @aria-label='Reduced match accuracy due to missing data']//div[contains(text(), 'Reduced match accuracy due to missing data')]")
			);
			
			Assert.assertFalse(infoMessageTexts.isEmpty(), "Info Message text elements not found for second profile validation");
			
			boolean correctTextFound = false;
			WebElement targetMessageText = null;
			
			// If multiple info messages exist, try to use the second one for second profile validation
			if (infoMessageTexts.size() > 1) {
				targetMessageText = infoMessageTexts.get(1);
				LOGGER.info("Using second Info Message text for second profile validation");
			} else if (infoMessageTexts.size() == 1) {
				targetMessageText = infoMessageTexts.get(0);
				LOGGER.info("Only one Info Message text found, using it for second profile validation");
			}
			
			if (targetMessageText != null && targetMessageText.isDisplayed()) {
				String actualText = targetMessageText.getText().trim();
				if (actualText.contains("Reduced match accuracy due to missing data")) {
					correctTextFound = true;
					LOGGER.info("Found correct Info Message text for second profile: " + actualText);
				}
			}
			
			Assert.assertTrue(correctTextFound, "Info Message should contain text about 'Reduced match accuracy due to missing data' for second profile validation");
			
			ExtentCucumberAdapter.addTestStepLog("Successfully verified Info Message contains correct text for second profile");
			LOGGER.info("Successfully verified Info Message text for second profile");
			
		} catch (Exception e) {
			LOGGER.error("Error verifying second profile Info Message text: " + e.getMessage());
			throw new IOException("Failed to verify second profile Info Message text for second profile validation: " + e.getMessage());
		}
	}

	// Additional helper method for clicking buttons
	public void click_on_button_for_profile_with_info_message(String buttonText) throws IOException {
		try {
			LOGGER.info("Clicking '{}' button for profile with Info Message", buttonText);
			
			if (currentRowIndex <= 0) {
				throw new IOException("No valid profile row index found. Call find_profile_with_missing_data_and_info_message() first.");
			}
		
			// Check this is an AutoMapped profile before clicking
			if (!isAutoMappedProfile(currentRowIndex)) {
				throw new IOException("Profile at row " + currentRowIndex + " is not an AutoMapped profile - cannot click '" + buttonText + "' button.");
			}
			
			// Wait for any loaders to disappear
			try {
				WebDriverWait loaderWait = new WebDriverWait(driver, Duration.ofSeconds(10));
				loaderWait.until(ExpectedConditions.invisibilityOfElementLocated(
					By.xpath("//div[@data-testid='loader'] | //div[contains(@class, 'loader')] | //div[contains(@class, 'loading')]")
				));
			} catch (Exception e) {
				LOGGER.debug("No loader found or already hidden");
			}
			
		// Find "View Other Matches" button for AutoMapped profiles - in RIGHT table (kf-job-container)
		String[] viewMatchesButtonXPaths = {
			String.format("//div[@id='kf-job-container']//tbody//tr[%d]//button[@id='view-matches']", currentRowIndex),
			String.format("//div[@id='kf-job-container']//tbody//tr[%d]//button[contains(@aria-label, 'View other matches')]", currentRowIndex),
			String.format("//div[@id='kf-job-container']//tbody//tr[%d]//button[contains(text(), 'View') and contains(text(), 'Other Matches')]", currentRowIndex)
		};
			
			WebElement targetButton = null;
			
			for (String xpath : viewMatchesButtonXPaths) {
				try {
					List<WebElement> buttons = driver.findElements(By.xpath(xpath));
					for (WebElement button : buttons) {
						if (button.isDisplayed()) {
							targetButton = button;
							LOGGER.info("Found '{}' button using XPath: {}", buttonText, xpath);
							break;
						}
					}
					if (targetButton != null) break;
				} catch (Exception e) {
					LOGGER.debug("Could not find button using XPath: {} - {}", xpath, e.getMessage());
				}
			}
			
			if (targetButton == null) {
				throw new IOException("Could not find '" + buttonText + "' button for AutoMapped profile at row " + currentRowIndex);
			}
			
		// Perform robust click
		performRobustClick(targetButton, buttonText + " button");
		
		ExtentCucumberAdapter.addTestStepLog("Clicked '" + buttonText + "' button for profile with Info Message");
		LOGGER.info("Clicked '{}' button", buttonText);
			
		} catch (Exception e) {
			LOGGER.error("Error clicking button for profile with Info Message: " + e.getMessage());
			throw new IOException("Failed to click button for profile with Info Message: " + e.getMessage());
		}
	}

	private void performRobustClick(WebElement element, String elementName) throws IOException {
		try {
			jsExecutor.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});", element);
			safeSleep(200);
			
			wait.until(ExpectedConditions.elementToBeClickable(element));
			element.click();
			
		} catch (Exception e) {
			LOGGER.debug("Standard click failed for {}, using JavaScript click", elementName);
			try {
				jsExecutor.executeScript("arguments[0].click();", element);
			} catch (Exception jsException) {
				LOGGER.error("Click failed for " + elementName);
				throw new IOException("Failed to click " + elementName);
			}
		}
	}

	// Additional missing methods for complete functionality
	public void click_on_button_for_second_profile_with_info_message(String buttonText) throws IOException {
		try {
			LOGGER.info("Clicking '{}' button for second profile with Info Message", buttonText);
			
			if (secondCurrentRowIndex <= 0) {
				throw new IOException("No valid second profile row index found. Call find_second_profile_with_missing_data_and_info_message() first.");
			}
		
			// Check this is an AutoMapped profile before clicking
			if (!isAutoMappedProfile(secondCurrentRowIndex)) {
				throw new IOException("Second profile at row " + secondCurrentRowIndex + " is not an AutoMapped profile - cannot click '" + buttonText + "' button.");
			}
			
			// Wait for any loaders to disappear
			try {
				WebDriverWait loaderWait = new WebDriverWait(driver, Duration.ofSeconds(10));
				loaderWait.until(ExpectedConditions.invisibilityOfElementLocated(
					By.xpath("//div[@data-testid='loader'] | //div[contains(@class, 'loader')] | //div[contains(@class, 'loading')]")
				));
			} catch (Exception e) {
				LOGGER.debug("No loader found or already hidden for second profile");
			}
			
		// Find "View Other Matches" button for AutoMapped second profile - in RIGHT table (kf-job-container)
		String[] viewMatchesButtonXPaths = {
			String.format("//div[@id='kf-job-container']//tbody//tr[%d]//button[@id='view-matches']", secondCurrentRowIndex),
			String.format("//div[@id='kf-job-container']//tbody//tr[%d]//button[contains(@aria-label, 'View other matches')]", secondCurrentRowIndex),
			String.format("//div[@id='kf-job-container']//tbody//tr[%d]//button[contains(text(), 'View') and contains(text(), 'Other Matches')]", secondCurrentRowIndex)
		};
			
			WebElement targetButton = null;
			
			for (String xpath : viewMatchesButtonXPaths) {
				try {
					List<WebElement> buttons = driver.findElements(By.xpath(xpath));
					for (WebElement button : buttons) {
						if (button.isDisplayed()) {
							targetButton = button;
							LOGGER.info("Found '{}' button for second profile using XPath: {}", buttonText, xpath);
							break;
						}
					}
					if (targetButton != null) break;
				} catch (Exception e) {
					LOGGER.debug("Could not find button for second profile using XPath: {} - {}", xpath, e.getMessage());
				}
			}
			
			if (targetButton == null) {
				throw new IOException("Could not find '" + buttonText + "' button for second AutoMapped profile at row " + secondCurrentRowIndex);
			}
			
		// Perform robust click
		performRobustClick(targetButton, buttonText + " button for second profile");
		
		ExtentCucumberAdapter.addTestStepLog("Clicked '" + buttonText + "' button for second profile with Info Message");
		LOGGER.info("Clicked '{}' button for second profile", buttonText);
			
		} catch (Exception e) {
			LOGGER.error("Error clicking button for second profile with Info Message: " + e.getMessage());
			throw new IOException("Failed to click button for second profile with Info Message: " + e.getMessage());
		}
	}

	public void verify_user_is_navigated_to_job_comparison_page() throws IOException {
		try {
			LOGGER.info("Verifying user is navigated to Job Comparison page");
			safeSleep(3000); // Allow page transition
			
		// Look for Job Comparison page indicators - using working pattern from PO02
		WebElement compareAndSelectHeader = wait.until(ExpectedConditions.presenceOfElementLocated(
			By.xpath("//h1[@id='compare-desc']")
		));
		
		String compareAndSelectHeaderText = wait.until(ExpectedConditions.visibilityOf(compareAndSelectHeader)).getText();
		
		// Verify the expected text (from PO02)
		Assert.assertEquals(compareAndSelectHeaderText, "Which profile do you want to use for this job?", 
			"Job Comparison page header text does not match expected value");
		
		LOGGER.info("SUCCESS: Job Comparison page detected with header text: {}", compareAndSelectHeaderText);
			
			ExtentCucumberAdapter.addTestStepLog("Successfully verified navigation to Job Comparison page");
			LOGGER.info("SUCCESS: User is successfully navigated to Job Comparison page");
			
	} catch (Exception e) {
		PageObjectHelper.handleError(LOGGER, "verify_user_is_navigated_to_job_comparison_page",
			"Failed to verify navigation to Job Comparison page", e);
	}
	}

	public void extract_job_details_from_job_comparison_page() throws IOException {
		try {
			LOGGER.info("Extracting job details from Job Comparison page (Organization Job section)");
			safeSleep(2000);
			
			// Extract job details using text-based approach from the Organization Job section
			String comparisonJobName = "";
			String comparisonJobCode = "";
			String comparisonGrade = "";
			String comparisonDepartment = "";
			String comparisonFunction = "";
			
			// Get page text from the Organization Job section
			String pageText = "";
			try {
				WebElement orgJobSection = driver.findElement(By.xpath("//div[contains(@class, 'border-b-10') and contains(@class, 'bg-grey')]//h3[contains(text(), 'Organization Job')]/ancestor::div[contains(@class, 'border-b-10')]"));
				pageText = orgJobSection.getText();
				LOGGER.info("Organization Job section text captured successfully");
			} catch (Exception e) {
				LOGGER.warn("Could not find Organization Job section, trying broader approach: " + e.getMessage());
				try {
					WebElement mainContainer = driver.findElement(By.xpath("//main | //div[contains(@class, 'bg-grey')] | //body"));
					pageText = mainContainer.getText();
				} catch (Exception e2) {
					LOGGER.error("Could not extract page text: " + e2.getMessage());
				}
			}
			
			// Extract Job Name and Code (first line after "Organization Job")
			try {
				String[] lines = pageText.split("\n");
				for (int i = 0; i < lines.length; i++) {
					if (lines[i].contains("Organization Job") && i + 1 < lines.length) {
						String jobLine = lines[i + 1].trim();
						// Remove status badge text if present
						jobLine = jobLine.replaceAll("Needs Review|Auto-mapped", "").trim();
						
						if (jobLine.contains(" - (") && jobLine.contains(")")) {
							String[] parts = jobLine.split(" - \\(");
							comparisonJobName = parts[0].trim();
							comparisonJobCode = parts[1].replace(")", "").trim();
						} else {
							comparisonJobName = jobLine;
						}
						break;
					}
				}
			} catch (Exception e) {
				LOGGER.debug("Could not extract job name/code from text: " + e.getMessage());
			}
			
			// Extract Grade
			if (pageText.contains("Grade:")) {
				String[] parts = pageText.split("Grade:");
				if (parts.length > 1) {
					String gradePart = parts[1].trim();
					if (gradePart.contains("Department:")) {
						gradePart = gradePart.split("Department:")[0].trim();
					} else if (gradePart.contains("\n")) {
						gradePart = gradePart.split("\n")[0].trim();
					}
					comparisonGrade = gradePart.replaceAll("\\s+", " ").trim();
				}
			}
			
			// Extract Department
			if (pageText.contains("Department:")) {
				String[] parts = pageText.split("Department:");
				if (parts.length > 1) {
					String deptPart = parts[1].trim();
					if (deptPart.contains("Function")) {
						deptPart = deptPart.split("Function")[0].trim();
					} else if (deptPart.contains("\n")) {
						deptPart = deptPart.split("\n")[0].trim();
					}
					comparisonDepartment = deptPart.replaceAll("\\s+", " ").trim();
				}
			}
			
			// Extract Function/Sub-function
			if (pageText.contains("Function / Sub-function:")) {
				String[] parts = pageText.split("Function / Sub-function:");
				if (parts.length > 1) {
					String functionPart = parts[1].trim();
					if (functionPart.contains("\n")) {
						functionPart = functionPart.split("\n")[0].trim();
					}
					comparisonFunction = functionPart.replaceAll("\\s+", " ").trim();
				}
			}
			
			LOGGER.info("Extracted Job Details from Job Comparison page (Organization Job section):");
			LOGGER.info("  Job Name: {}", comparisonJobName);
			LOGGER.info("  Job Code: {}", comparisonJobCode);
			LOGGER.info("  Grade: {}", comparisonGrade);
			LOGGER.info("  Department: {}", comparisonDepartment);
			LOGGER.info("  Function/Sub-function: {}", comparisonFunction);
			
			// Now verify they match the stored values from Job Mapping page
			boolean allMatch = true;
			StringBuilder mismatchDetails = new StringBuilder();
			
			// Normalize for comparison (treat "-" as empty)
			String normalizedStoredGrade = gradeWithInfoMessage.equals("-") ? "" : gradeWithInfoMessage;
			String normalizedComparisonGrade = comparisonGrade.equals("-") ? "" : comparisonGrade;
			String normalizedStoredDept = departmentWithInfoMessage.equals("-") ? "" : departmentWithInfoMessage;
			String normalizedComparisonDept = comparisonDepartment.equals("-") ? "" : comparisonDepartment;
			
			if (!comparisonJobName.equals(jobNameWithInfoMessage)) {
				allMatch = false;
				mismatchDetails.append("\n  Job Name: Job Mapping='").append(jobNameWithInfoMessage).append("' vs Job Comparison='").append(comparisonJobName).append("'");
			}
			
			if (!normalizedComparisonGrade.equals(normalizedStoredGrade)) {
				allMatch = false;
				mismatchDetails.append("\n  Grade: Job Mapping='").append(gradeWithInfoMessage).append("' vs Job Comparison='").append(comparisonGrade).append("'");
			}
			
			if (!normalizedComparisonDept.equals(normalizedStoredDept)) {
				allMatch = false;
				mismatchDetails.append("\n  Department: Job Mapping='").append(departmentWithInfoMessage).append("' vs Job Comparison='").append(comparisonDepartment).append("'");
			}
			
			if (!comparisonFunction.equals(functionSubfunctionWithInfoMessage)) {
				allMatch = false;
				mismatchDetails.append("\n  Function/Sub-function: Job Mapping='").append(functionSubfunctionWithInfoMessage).append("' vs Job Comparison='").append(comparisonFunction).append("'");
			}
			
			if (allMatch) {
				LOGGER.info("... SUCCESS: All job details match between Job Mapping and Job Comparison pages");
				ExtentCucumberAdapter.addTestStepLog("... Job details match verified between Job Mapping and Job Comparison pages for: " + jobNameWithInfoMessage);
			} else {
				LOGGER.error(" MISMATCH: Job details do NOT match between pages:" + mismatchDetails.toString());
				ExtentCucumberAdapter.addTestStepLog(" Job details mismatch: " + mismatchDetails.toString());
				Assert.fail("Job details do NOT match between Job Mapping and Job Comparison pages:" + mismatchDetails.toString());
			}
			
		} catch (Exception e) {
			LOGGER.error("Error extracting job details from Job Comparison page: " + e.getMessage());
			throw new IOException("Failed to extract job details from Job Comparison page: " + e.getMessage());
		}
	}

	public void extract_job_details_from_job_comparison_page_for_second_profile() throws IOException {
		try {
			LOGGER.info("Extracting job details from Job Comparison page (Organization Job section) for second profile");
			safeSleep(2000);
			
			// Extract job details using text-based approach from the Organization Job section
			String comparisonJobName = "";
			String comparisonJobCode = "";
			String comparisonGrade = "";
			String comparisonDepartment = "";
			String comparisonFunction = "";
			
			// Get page text from the Organization Job section
			String pageText = "";
			try {
				WebElement orgJobSection = driver.findElement(By.xpath("//div[contains(@class, 'border-b-10') and contains(@class, 'bg-grey')]//h3[contains(text(), 'Organization Job')]/ancestor::div[contains(@class, 'border-b-10')]"));
				pageText = orgJobSection.getText();
				LOGGER.info("Organization Job section text captured successfully for second profile");
			} catch (Exception e) {
				LOGGER.warn("Could not find Organization Job section, trying broader approach: " + e.getMessage());
				try {
					WebElement mainContainer = driver.findElement(By.xpath("//main | //div[contains(@class, 'bg-grey')] | //body"));
					pageText = mainContainer.getText();
				} catch (Exception e2) {
					LOGGER.error("Could not extract page text for second profile: " + e2.getMessage());
				}
			}
			
			// Extract Job Name and Code (first line after "Organization Job")
			try {
				String[] lines = pageText.split("\n");
				for (int i = 0; i < lines.length; i++) {
					if (lines[i].contains("Organization Job") && i + 1 < lines.length) {
						String jobLine = lines[i + 1].trim();
						// Remove status badge text if present
						jobLine = jobLine.replaceAll("Needs Review|Auto-mapped", "").trim();
						
						if (jobLine.contains(" - (") && jobLine.contains(")")) {
							String[] parts = jobLine.split(" - \\(");
							comparisonJobName = parts[0].trim();
							comparisonJobCode = parts[1].replace(")", "").trim();
						} else {
							comparisonJobName = jobLine;
						}
						break;
					}
				}
			} catch (Exception e) {
				LOGGER.debug("Could not extract job name/code from text for second profile: " + e.getMessage());
			}
			
			// Extract Grade
			if (pageText.contains("Grade:")) {
				String[] parts = pageText.split("Grade:");
				if (parts.length > 1) {
					String gradePart = parts[1].trim();
					if (gradePart.contains("Department:")) {
						gradePart = gradePart.split("Department:")[0].trim();
					} else if (gradePart.contains("\n")) {
						gradePart = gradePart.split("\n")[0].trim();
					}
					comparisonGrade = gradePart.replaceAll("\\s+", " ").trim();
				}
			}
			
			// Extract Department
			if (pageText.contains("Department:")) {
				String[] parts = pageText.split("Department:");
				if (parts.length > 1) {
					String deptPart = parts[1].trim();
					if (deptPart.contains("Function")) {
						deptPart = deptPart.split("Function")[0].trim();
					} else if (deptPart.contains("\n")) {
						deptPart = deptPart.split("\n")[0].trim();
					}
					comparisonDepartment = deptPart.replaceAll("\\s+", " ").trim();
				}
			}
			
			// Extract Function/Sub-function
			if (pageText.contains("Function / Sub-function:")) {
				String[] parts = pageText.split("Function / Sub-function:");
				if (parts.length > 1) {
					String functionPart = parts[1].trim();
					if (functionPart.contains("\n")) {
						functionPart = functionPart.split("\n")[0].trim();
					}
					comparisonFunction = functionPart.replaceAll("\\s+", " ").trim();
				}
			}
			
			LOGGER.info("Extracted Job Details from Job Comparison page (Organization Job section) for second profile:");
			LOGGER.info("  Job Name: {}", comparisonJobName);
			LOGGER.info("  Job Code: {}", comparisonJobCode);
			LOGGER.info("  Grade: {}", comparisonGrade);
			LOGGER.info("  Department: {}", comparisonDepartment);
			LOGGER.info("  Function/Sub-function: {}", comparisonFunction);
			
			// Now verify they match the stored values from Job Mapping page
			boolean allMatch = true;
			StringBuilder mismatchDetails = new StringBuilder();
			
			// Normalize for comparison (treat "-" as empty)
			String normalizedStoredGrade = secondGradeWithInfoMessage.equals("-") ? "" : secondGradeWithInfoMessage;
			String normalizedComparisonGrade = comparisonGrade.equals("-") ? "" : comparisonGrade;
			String normalizedStoredDept = secondDepartmentWithInfoMessage.equals("-") ? "" : secondDepartmentWithInfoMessage;
			String normalizedComparisonDept = comparisonDepartment.equals("-") ? "" : comparisonDepartment;
			
			if (!comparisonJobName.equals(secondJobNameWithInfoMessage)) {
				allMatch = false;
				mismatchDetails.append("\n  Job Name: Job Mapping='").append(secondJobNameWithInfoMessage).append("' vs Job Comparison='").append(comparisonJobName).append("'");
			}
			
			if (!normalizedComparisonGrade.equals(normalizedStoredGrade)) {
				allMatch = false;
				mismatchDetails.append("\n  Grade: Job Mapping='").append(secondGradeWithInfoMessage).append("' vs Job Comparison='").append(comparisonGrade).append("'");
			}
			
			if (!normalizedComparisonDept.equals(normalizedStoredDept)) {
				allMatch = false;
				mismatchDetails.append("\n  Department: Job Mapping='").append(secondDepartmentWithInfoMessage).append("' vs Job Comparison='").append(comparisonDepartment).append("'");
			}
			
			if (!comparisonFunction.equals(secondFunctionSubfunctionWithInfoMessage)) {
				allMatch = false;
				mismatchDetails.append("\n  Function/Sub-function: Job Mapping='").append(secondFunctionSubfunctionWithInfoMessage).append("' vs Job Comparison='").append(comparisonFunction).append("'");
			}
			
			if (allMatch) {
				LOGGER.info("... SUCCESS: All job details match between Job Mapping and Job Comparison pages for second profile");
				ExtentCucumberAdapter.addTestStepLog("... Job details match verified between Job Mapping and Job Comparison pages for second profile: " + secondJobNameWithInfoMessage);
			} else {
				LOGGER.error(" MISMATCH: Job details do NOT match between pages for second profile:" + mismatchDetails.toString());
				ExtentCucumberAdapter.addTestStepLog(" Job details mismatch for second profile: " + mismatchDetails.toString());
				Assert.fail("Job details do NOT match between Job Mapping and Job Comparison pages for second profile:" + mismatchDetails.toString());
			}
			
	} catch (Exception e) {
		PageObjectHelper.handleError(LOGGER, "extract_job_details_from_job_comparison_page_for_second_profile",
			"Failed to extract job details from Job Comparison page for second profile", e);
	}
	}

	public void verify_job_details_match_between_job_mapping_and_job_comparison_pages() throws IOException {
		// NOTE: Verification is now done inline within extract_job_details_from_job_comparison_page()
		// This method is kept for backward compatibility with step definitions - no additional action needed
	}

	public void verify_job_details_match_between_job_mapping_and_job_comparison_pages_for_second_profile() throws IOException {
		// NOTE: Verification is now done inline within extract_job_details_from_job_comparison_page_for_second_profile()
		// This method is kept for backward compatibility with step definitions - no additional action needed
	}

	public void verify_info_message_contains_same_text_about_reduced_match_accuracy() throws IOException {
		try {
			LOGGER.info("Verifying Info Message contains same text about reduced match accuracy in Job Comparison page");
			safeSleep(2000);
			
			// Look for info message text in Job Comparison page
			List<WebElement> infoMessageTexts = driver.findElements(
				By.xpath("//div[contains(text(), 'Reduced match accuracy due to missing data')] | //div[@aria-label='Reduced match accuracy due to missing data']")
			);
			
			Assert.assertFalse(infoMessageTexts.isEmpty(), "Info Message text not found in Job Comparison page");
			
			boolean correctTextFound = false;
			for (WebElement messageText : infoMessageTexts) {
				if (messageText.isDisplayed()) {
					String actualText = messageText.getText().trim();
					if (actualText.contains("Reduced match accuracy due to missing data")) {
						correctTextFound = true;
						LOGGER.info("Found correct Info Message text in Job Comparison page: {}", actualText);
						break;
					}
				}
			}
			
			Assert.assertTrue(correctTextFound, "Info Message should contain same text about reduced match accuracy in Job Comparison page");
			
			ExtentCucumberAdapter.addTestStepLog("Successfully verified Info Message contains same text in Job Comparison page");
			LOGGER.info("SUCCESS: Info Message text consistency verified in Job Comparison page");
			
	} catch (Exception e) {
		PageObjectHelper.handleError(LOGGER, "verify_info_message_contains_same_text_about_reduced_match_accuracy",
			"Failed to verify Info Message text consistency in Job Comparison page", e);
	}
	}

	public void verify_info_message_contains_same_text_about_reduced_match_accuracy_for_second_profile() throws IOException {
		try {
			LOGGER.info("Verifying Info Message contains same text about reduced match accuracy for second profile in Job Comparison page");
			safeSleep(2000);
			
			// Look for info message text in Job Comparison page for second profile
			List<WebElement> infoMessageTexts = driver.findElements(
				By.xpath("//div[contains(text(), 'Reduced match accuracy due to missing data')] | //div[@aria-label='Reduced match accuracy due to missing data']")
			);
			
			Assert.assertFalse(infoMessageTexts.isEmpty(), "Info Message text not found in Job Comparison page for second profile");
			
			boolean correctTextFound = false;
			for (WebElement messageText : infoMessageTexts) {
				if (messageText.isDisplayed()) {
					String actualText = messageText.getText().trim();
					if (actualText.contains("Reduced match accuracy due to missing data")) {
						correctTextFound = true;
						LOGGER.info("Found correct Info Message text in Job Comparison page for second profile: {}", actualText);
						break;
					}
				}
			}
			
			Assert.assertTrue(correctTextFound, "Info Message should contain same text about reduced match accuracy in Job Comparison page for second profile");
			
			ExtentCucumberAdapter.addTestStepLog("Successfully verified Info Message contains same text in Job Comparison page for second profile");
			LOGGER.info("SUCCESS: Info Message text consistency verified in Job Comparison page for second profile");
			
		} catch (Exception e) {
			LOGGER.error("ERROR: Error verifying Info Message text consistency for second profile: " + e.getMessage());
			throw new IOException("Failed to verify Info Message text consistency in Job Comparison page for second profile: " + e.getMessage());
		}
	}

	public void navigate_back_to_job_mapping_page_from_job_comparison() throws IOException {
		try {
			LOGGER.info("Navigating back to Job Mapping page from Job Comparison");
			safeSleep(2000);
			
			// Look for back button or close button
			List<WebElement> backButtons = driver.findElements(
				By.xpath("//button[contains(text(), 'Back')] | //button[contains(text(), 'Close')] | //button[@aria-label='Close'] | //button[contains(@class, 'close-button')]")
			);
			
			if (!backButtons.isEmpty()) {
				WebElement backButton = backButtons.get(0);
				performRobustClick(backButton, "Back/Close button");
				LOGGER.info("SUCCESS: Clicked back button to return to Job Mapping page");
			} else {
				// Try browser back button as fallback
				jsExecutor.executeScript("window.history.back();");
				LOGGER.info("INFO: Used browser back button to return to Job Mapping page");
			}
			
			safeSleep(3000); // Allow page transition
			
			ExtentCucumberAdapter.addTestStepLog("Successfully navigated back to Job Mapping page from Job Comparison");
			LOGGER.info("SUCCESS: Navigation back to Job Mapping page completed");
			
	} catch (Exception e) {
		PageObjectHelper.handleError(LOGGER, "navigate_back_to_job_mapping_page_from_job_comparison",
			"Failed to navigate back to Job Mapping page from Job Comparison", e);
	}
	}

}

