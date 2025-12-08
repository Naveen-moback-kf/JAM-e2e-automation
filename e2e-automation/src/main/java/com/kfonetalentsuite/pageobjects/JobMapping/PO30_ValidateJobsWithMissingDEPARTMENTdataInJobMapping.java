package com.kfonetalentsuite.pageobjects.JobMapping;

import java.io.IOException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import java.time.Duration;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.PageObjectHelper;

public class PO30_ValidateJobsWithMissingDEPARTMENTdataInJobMapping extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO30_ValidateJobsWithMissingDEPARTMENTdataInJobMapping.class);

	// Static variables for storing extracted job details
	public static Map<String, String> jobDetailsFromMissingDataScreen = new HashMap<>();
	public static Map<String, String> jobDetailsFromJobMappingPage = new HashMap<>();
	// THREAD-SAFE: Each thread gets its own isolated state for parallel execution
	public static ThreadLocal<String> extractedJobName = ThreadLocal.withInitial(() -> "NOT_SET");
	public static WebElement foundJobRow = null;
	public static WebElement foundProfile = null;
	public static WebElement matchingJobRow = null;

	// Scenario coordination
	public static ThreadLocal<Boolean> forwardScenarioFoundProfile = ThreadLocal.withInitial(() -> false);
	public static ThreadLocal<String> forwardScenarioJobName = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<String> forwardScenarioJobCode = ThreadLocal.withInitial(() -> "NOT_SET");

	public PO30_ValidateJobsWithMissingDEPARTMENTdataInJobMapping() {
		super();
	}

	// ==================== LOCATORS ====================
	private static final By REUPLOAD_PAGE_TITLE_DESC = By.xpath("//div//p[contains(text(), 're-upload the jobs')]");
	private static final By CLOSE_REUPLOAD_JOBS_PAGE_BUTTON = By.xpath("//button[contains(@class, 'border-[#007BC7]') and contains(text(), 'Close')]");
	private static final By REUPLOAD_BUTTON = By.xpath("//button[contains(text(), 'Re-upload')] | //button[contains(text(), 'upload')]");
	private static final By JOB_ROWS_IN_MISSING_DATA_SCREEN = By.xpath("//table//tr[contains(@class, 'border-b')]");
	private static final By JOB_SEARCH_INPUT = By.xpath("//input[@id='search-job-title-input-search-input']");
	private static final By JOB_ROWS_IN_JOB_MAPPING_PAGE = By.xpath("//div[@id='org-job-container']//tbody//tr");


	/**
	 * Verify user is navigated to Jobs with Missing Data screen (Modal/Overlay -
	 * URL doesn't change)
	 */
	public void verify_user_is_navigated_to_jobs_with_missing_data_screen() throws IOException {
		try {
			LOGGER.info("Verifying Jobs with Missing Data screen is displayed (modal/overlay)");
			PerformanceUtils.safeSleep(driver, 2000); // Wait for modal/overlay to fully load

			boolean pageVerified = false;
			String verificationResults = "";

			// Check for page title description (most important indicator)
			try {
				WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(15));
				shortWait.until(ExpectedConditions.visibilityOfElementLocated(REUPLOAD_PAGE_TITLE_DESC));
				if (findElement(REUPLOAD_PAGE_TITLE_DESC).isDisplayed()) {
					String pageTitle = findElement(REUPLOAD_PAGE_TITLE_DESC).getText();
					verificationResults += " Page title description found: " + pageTitle + "; ";
					pageVerified = true;
				}
			} catch (Exception e) {
				verificationResults += "- Page title description not found; ";
			}

			// Check for Close button (essential for closing modal)
			try {
				WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(10));
				shortWait.until(ExpectedConditions.visibilityOfElementLocated(CLOSE_REUPLOAD_JOBS_PAGE_BUTTON));
				if (findElement(CLOSE_REUPLOAD_JOBS_PAGE_BUTTON).isDisplayed()) {
					verificationResults += " Close button found; ";
					pageVerified = true;
				}
			} catch (Exception e) {
				verificationResults += "- Close button not found; ";
			}

			// Check for Re-upload button (confirms this is the upload screen)
			try {
				WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(8));
				shortWait.until(ExpectedConditions.visibilityOfElementLocated(REUPLOAD_BUTTON));
				if (findElement(REUPLOAD_BUTTON).isDisplayed()) {
					verificationResults += " Re-upload button found; ";
				}
			} catch (Exception e) {
				verificationResults += "- Re-upload button not found; ";
			}

			// Check for job table/rows (confirms data is loaded)
			try {
				List<WebElement> jobRows = driver.findElements(By.xpath("//table//tr[contains(@class, 'border-b')]"));
				if (!jobRows.isEmpty()) {
					verificationResults += " Job data table found (" + jobRows.size() + " rows); ";
				} else {
					verificationResults += "- No job data rows found; ";
				}
			} catch (Exception e) {
				verificationResults += "- Job data table check failed; ";
			}

			// Alternative verification: Check page source for key text (fallback)
			if (!pageVerified) {
				try {
					String pageSource = driver.getPageSource().toLowerCase();
					if (pageSource.contains("missing data") || pageSource.contains("re-upload")
							|| pageSource.contains("organization jobs")
							|| pageSource.contains("jobs with missing data")) {
						verificationResults += " Page content indicates correct screen via source scan; ";
						pageVerified = true;
						PageObjectHelper.log(LOGGER, " Verified via page source analysis");
					}
				} catch (Exception e) {
					verificationResults += "- Page source check failed; ";
				}
			}

			// Final assertion
			if (pageVerified) {
				PageObjectHelper.log(LOGGER, " Successfully verified Jobs with Missing Data screen is displayed");
				PageObjectHelper.log(LOGGER, "  Verification results: " + verificationResults);
				LOGGER.info("Successfully verified Jobs with Missing Data screen is displayed");
			} else {
				String errorMsg = "Failed to verify Jobs with Missing Data screen. Results: " + verificationResults;
				PageObjectHelper.log(LOGGER, "- " + errorMsg);
				Assert.fail(errorMsg);
			}

		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_user_is_navigated_to_jobs_with_missing_data_screen",
					"Failed to verify Jobs with Missing Data screen", e);
		}
	}

	/**
	 * Find job with missing GRADE data - REVERSE SCENARIO (Scenario 2): - Used in
	 * Reverse flow: Job Mapping Missing Data screen find job with Grade=N/A -
	 * Searches Missing Data screen for FIRST job where ONLY Grade is N/A -
	 * PREFERRED: Job where ONLY Grade is N/A (Department AND Function/Subfunction
	 * have data) - Conditional behavior: If Forward scenario found NO profile, SKIP
	 * Reverse scenario steps - If Forward scenario found profile: Search for
	 * PREFERRED jobs ONLY (no fallback)
	 */
	public void find_job_in_jobs_missing_data_screen_where_department_is_na() throws IOException {
		try {
			// ===== CLEAR STATIC VARIABLES FOR FRESH SCENARIO EXECUTION =====
			jobDetailsFromJobMappingPage.clear();
			jobDetailsFromMissingDataScreen.clear();
			extractedJobName.set("");
			foundJobRow = null;
			foundProfile = null;

			LOGGER.info("REVERSE SCENARIO (Scenario 2): Conditional behavior based on Forward scenario results");

			// Check if Forward scenario found a profile
			if (!forwardScenarioFoundProfile.get()) {
				PageObjectHelper.log(LOGGER, " SKIPPING Reverse scenario - Forward scenario found no suitable profiles");
				throw new org.testng.SkipException(
						"SKIPPING Reverse scenario - Forward scenario found no suitable profiles");
			}

			PageObjectHelper.log(LOGGER, " REVERSE SCENARIO (Scenario 2): Finding job with missing Department data...");

			// Get ALL job rows at once (no lazy loading, all jobs are loaded in DOM)
			LOGGER.info("Getting all job rows from Jobs with Missing Data screen (no lazy loading)");
			List<WebElement> allJobRows = driver.findElements(By.xpath("//tbody//tr[contains(@class,'border')]"));
			LOGGER.info("Found " + allJobRows.size() + " total job rows to search");

			WebElement preferredJobRow = null;

			// Search for jobs with missing Department (ignoring Grade and Function data)
			// Skip the same profile that was validated in Forward scenario
			LOGGER.info("Searching for jobs with missing Department (ignoring Grade and Function data)");
			LOGGER.info("Will skip Forward scenario profile: " + forwardScenarioJobName.get() + " ("
					+ forwardScenarioJobCode.get() + ")");

			for (int i = 0; i < allJobRows.size(); i++) {
				WebElement row = allJobRows.get(i);
				List<WebElement> cells = row.findElements(By.xpath(".//td"));

				if (cells.size() >= 4) {
					String jobName = cells.get(0).getText().trim();
					String grade = cells.get(1).getText().trim();
					String department = cells.get(2).getText().trim();
					String functionSubfunction = cells.get(3).getText().trim();

					boolean departmentIsMissing = "N/A".equalsIgnoreCase(department);
					// Note: Grade and Function data validation removed as per requirements

					// Check if this is the same profile from Forward scenario - skip it
					String cleanedJobName = cleanJobNameLocal(jobName);

					// Extract job code from the current job name for comparison
					String currentJobCode = "";
					String currentJobNameOnly = cleanedJobName; // Default to full cleaned name
					if (jobName.contains("(") && jobName.contains(")")) {
						int startParen = jobName.lastIndexOf("(");
						int endParen = jobName.lastIndexOf(")");
						if (startParen > 0 && endParen > startParen) {
							currentJobCode = jobName.substring(startParen + 1, endParen).trim();
							// Extract just the job name part (without the code in parentheses)
							currentJobNameOnly = jobName.substring(0, startParen).trim();
						}
					}

					// Compare both job name and code to determine if it's the same profile
					boolean nameMatches = forwardScenarioJobName.equals(currentJobNameOnly);
					boolean codeMatches = forwardScenarioJobCode.equals(currentJobCode);
					boolean isSameProfile = nameMatches && codeMatches;

					if (isSameProfile) {
						LOGGER.info("Skipping Forward scenario profile: " + cleanedJobName + " (" + currentJobCode
								+ ") - already validated");
						continue;
					}

					// Scroll to current job row to show search progress
					if (i % 5 == 0) { // Scroll every 5th job to provide visual feedback
						js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", row);
						PerformanceUtils.safeSleep(driver, 100); // Brief pause to make scrolling visible
					}

					// Look for match: Department missing (ignoring Grade and Function data)
					if (departmentIsMissing) {
						preferredJobRow = row;
						foundJobRow = row; // Store for precise extraction
						extractedJobName.set(cleanJobNameLocal(jobName)); // Store cleaned job name
						LOGGER.info("Found DIFFERENT job at position " + (i + 1) + ": " + extractedJobName.get()
								+ " (Grade: " + grade + ", Dept: " + department + ", Func: " + functionSubfunction
								+ ")");
						break; // Found match, stop searching
					}
				}
			}

			// Check if job was found
			if (preferredJobRow != null) {
				// Job found - continue with scenario
				PageObjectHelper.log(LOGGER, 
						"... Found job profile where Department is missing - proceeding with validation");

				// Scroll to the found job row
				js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});",
						preferredJobRow);
				PerformanceUtils.safeSleep(driver, 500); // Wait for scroll to complete

				// Extract details from the preferred job row
				List<WebElement> cells = preferredJobRow.findElements(By.xpath(".//td"));

				if (cells.size() >= 4) {
					// Extract job name and clean up any formatting issues
					String rawJobName = cells.get(0).getText().trim();
					String jobName = cleanJobNameLocal(rawJobName);
					String grade = cells.get(1).getText().trim();
					String department = cells.get(2).getText().trim();
					String functionSubfunction = cells.get(3).getText().trim();

					// Store the cleaned job name for later use
					extractedJobName.set(jobName);

					// Log details of the matching profile
					PageObjectHelper.log(LOGGER, " Found matching job profile: " + jobName);
					PageObjectHelper.log(LOGGER, "   Grade: " + grade);
					PageObjectHelper.log(LOGGER, "   Department: " + department + " (Missing)");
					PageObjectHelper.log(LOGGER, "   Function/Subfunction: " + functionSubfunction);

				} else {
					throw new IOException("Found selected job row but could not extract all required cell data");
				}

			} else {
				// No preferred job found - FAIL the scenario since Reverse scenario found a
				// profile
				String failMsg = "BUG DETECTED: Reverse scenario found suitable profile, but Forward scenario found NO suitable jobs (ONLY Grade=N/A) in "
						+ allJobRows.size() + " total jobs";
				PageObjectHelper.log(LOGGER, " BUG: " + failMsg);

				// This is a BUG - data inconsistency between scenarios
				throw new IOException("BUG: Data inconsistency - " + failMsg);
			}

		} catch (org.testng.SkipException e) {
			// Re-throw SkipException to properly skip the scenario
			throw e;
		} catch (Exception e) {
			PageObjectHelper.log(LOGGER, "- Failed to find job with specified criteria: " + e.getMessage());
			throw new IOException("Failed to find job with specified criteria", e);
		}
	}

	/**
	 * Extract all available job details from Jobs with Missing Data screen Uses the
	 * exact row found during search to avoid re-search issues
	 */
	public void extract_all_available_job_details_from_jobs_with_missing_data_screen() throws IOException {
		try {
			LOGGER.info("Extracting job details for: " + extractedJobName.get());

			// Use the stored row from the search phase for precise extraction
			if (foundJobRow != null) {
				LOGGER.info("Using stored job row for precise extraction (avoids re-search issues)");

				List<WebElement> cells = foundJobRow.findElements(By.xpath(".//td"));

				if (cells.size() >= 4) {
					// Extract and clean job name to fix parsing issues
					String rawJobName = cells.get(0).getText().trim();
					String cleanedJobName = cleanJobNameLocal(rawJobName);

					// Extract job code from the raw job name and clean the job name
					String jobCode = "";
					if (rawJobName.contains("(") && rawJobName.contains(")")) {
						int startParen = rawJobName.lastIndexOf("(");
						int endParen = rawJobName.lastIndexOf(")");
						if (startParen > 0 && endParen > startParen) {
							jobCode = rawJobName.substring(startParen + 1, endParen).trim();
							// Clean the job name by removing the job code part
							cleanedJobName = rawJobName.substring(0, startParen).trim();
						}
					}
					String grade = cells.get(1).getText().trim();
					String department = cells.get(2).getText().trim();
					String functionSubfunction = cells.get(3).getText().trim();

					// Validate this is still the correct job (Grade=N/A)
					boolean departmentIsMissing = "N/A".equalsIgnoreCase(department);
					// Note: Grade and Function data validation removed as per requirements

					if (!departmentIsMissing) {
						throw new IOException("Stored job row no longer meets criteria. Grade: " + grade
								+ ", Department: " + department + ", Function: " + functionSubfunction);
					}

					// Store job details in static map
					jobDetailsFromMissingDataScreen.clear();
					jobDetailsFromMissingDataScreen.put("jobName", cleanedJobName);
					jobDetailsFromMissingDataScreen.put("jobCode", jobCode);
					jobDetailsFromMissingDataScreen.put("grade", grade);
					jobDetailsFromMissingDataScreen.put("department", department);
					jobDetailsFromMissingDataScreen.put("functionSubfunction", functionSubfunction);

					PageObjectHelper.log(LOGGER, "Extracted job details - Name: " + cleanedJobName + ", Code: " + jobCode);
					return;

				} else {
					throw new IOException("Stored job row does not have expected number of cells: " + cells.size());
				}
			} else {
				// Fallback to original search method if no stored row
				LOGGER.warn("No stored job row available. Falling back to re-search method (less reliable)");

				for (WebElement row : findElements(JOB_ROWS_IN_MISSING_DATA_SCREEN)) {
					List<WebElement> cells = row.findElements(By.xpath(".//td"));

					if (cells.size() >= 4 && cells.get(0).getText().contains(extractedJobName.get().split(" ")[0])) {
						// Extract and clean job name to fix parsing issues
						String rawJobName = cells.get(0).getText().trim();
						String cleanedJobName = cleanJobNameLocal(rawJobName);
						String grade = cells.get(1).getText().trim();
						String department = cells.get(2).getText().trim();
						String functionSubfunction = cells.get(3).getText().trim();

						// Store job details in static map
						jobDetailsFromMissingDataScreen.clear();
						jobDetailsFromMissingDataScreen.put("jobName", cleanedJobName);
						jobDetailsFromMissingDataScreen.put("grade", grade);
						jobDetailsFromMissingDataScreen.put("department", department);
						jobDetailsFromMissingDataScreen.put("functionSubfunction", functionSubfunction);

												PageObjectHelper.log(LOGGER, 
								"Successfully extracted job details from Jobs with Missing Data screen");
						return;
					}
				}

				throw new IOException("Could not extract details for job: " + extractedJobName);
			}

		} catch (Exception e) {
			PageObjectHelper.log(LOGGER, "Failed to extract job details: " + e.getMessage());
			throw new IOException("Failed to extract job details", e);
		}
	}

	/**
	 * Click on Close button to return to Job Mapping page
	 */
	public void click_on_close_button_to_return_to_job_mapping_page() throws IOException {
		try {
			LOGGER.info("Closing Missing Data screen to allow next scenario...");

			WebElement closeBtn = wait.until(ExpectedConditions.elementToBeClickable(findElement(CLOSE_REUPLOAD_JOBS_PAGE_BUTTON)));
			js.executeScript("arguments[0].click();", closeBtn);

			PageObjectHelper.log(LOGGER, "Closed Missing Data screen - ready for next scenario");

		} catch (Exception e) {
			PageObjectHelper.log(LOGGER, "Failed to close Missing Data screen: " + e.getMessage());
			throw new IOException("Failed to close Missing Data screen", e);
		}
	}

	/**
	 * Force close Missing Data screen even if previous steps failed This ensures
	 * Scenario 2 can run even if Scenario 1 fails
	 */
	public void force_close_missing_data_screen_for_next_scenario() {
		try {
			LOGGER.info("Force closing Missing Data screen for next scenario...");

			// Strategy 1: Try to find the main close button first
			try {
				WebElement closeBtn = findElement(CLOSE_REUPLOAD_JOBS_PAGE_BUTTON);
				if (closeBtn != null) {
					js.executeScript("arguments[0].click();", closeBtn);
					LOGGER.info("Used main close button");
					PerformanceUtils.safeSleep(driver, 2000);
					return;
				}
			} catch (Exception e) {
				// Main close button not clickable
			}

			// Strategy 2: Try to find any close button
			String[] closeButtonXPaths = { "//button[contains(text(),'Close')]",
					"//button[contains(@aria-label,'close')]", "//button[contains(@class,'close')]",
					"//button[@data-testid='close-button']", "//a[contains(text(),'Close')]",
					"//div[contains(@class,'close') and @role='button']" };

			for (String xpath : closeButtonXPaths) {
				try {
					List<WebElement> closeButtons = driver.findElements(By.xpath(xpath));
					for (WebElement closeButton : closeButtons) {
						if (closeButton.isDisplayed() && closeButton.isEnabled()) {
							js.executeScript("arguments[0].click();", closeButton);
							LOGGER.info("Force closed Missing Data screen using XPath: " + xpath);
							PerformanceUtils.safeSleep(driver, 2000);
							return;
						}
					}
				} catch (Exception e) {
					// XPath failed, try next strategy
				}
			}

			// Strategy 3: Try browser back navigation as last resort
			try {
				driver.navigate().back();
				LOGGER.info("Used browser back navigation to close Missing Data screen");
				PerformanceUtils.safeSleep(driver, 2000);
			} catch (Exception e) {
				LOGGER.warn("Browser back navigation also failed: " + e.getMessage());
			}

		} catch (Exception e) {
			LOGGER.warn("Could not force close Missing Data screen: " + e.getMessage());
			// Don't throw exception - just log and continue
		}
	}

	/**
	 * Ensure we're on Job Mapping page - only close Missing Data screen if it's
	 * actually open
	 */
	public void ensure_on_job_mapping_page_for_next_scenario() {
		try {
			LOGGER.info("Checking page state for next scenario...");

			// First, check if we're already on Job Mapping page
			try {
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
						"//input[@placeholder='Search jobs...'] | //input[contains(@placeholder,'Search')] | //div[@id='org-job-container']")));
				LOGGER.info("Already on Job Mapping page - ready for next scenario");
				return;
			} catch (Exception e) {
				// Not on Job Mapping page, checking for Missing Data screen
			}

			// Check if Missing Data screen is open (only close if it actually exists)
			if (isMissingDataScreenOpen()) {
				LOGGER.info("Missing Data screen is open, closing it for next scenario");
				force_close_missing_data_screen_for_next_scenario();

				// Verify we're back on Job Mapping page after closing
				try {
					wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
							"//input[@placeholder='Search jobs...'] | //input[contains(@placeholder,'Search')] | //div[@id='org-job-container']")));
					LOGGER.info("Successfully closed Missing Data screen and returned to Job Mapping page");
					return;
				} catch (Exception e) {
					LOGGER.warn("Failed to return to Job Mapping page after closing Missing Data screen");
				}
			}

			// Try to navigate to Job Mapping page if needed
			try {
				// Try clicking on Job Mapping navigation link
				List<WebElement> jobMappingLinks = driver.findElements(By.xpath(
						"//a[contains(text(),'Job Mapping') or contains(@href,'job-mapping')] | //button[contains(text(),'Job Mapping')]"));
				for (WebElement link : jobMappingLinks) {
					if (link.isDisplayed() && link.isEnabled()) {
						js.executeScript("arguments[0].click();", link);
						LOGGER.info("Navigated to Job Mapping page using link");
						PerformanceUtils.safeSleep(driver, 3000);
						return;
					}
				}

				// If navigation fails, refresh page as last resort
				driver.navigate().refresh();
				LOGGER.info("Refreshed page to ensure clean state");
				PerformanceUtils.safeSleep(driver, 3000);

			} catch (Exception e) {
				LOGGER.warn("Could not navigate to Job Mapping page: " + e.getMessage());
			}

		} catch (Exception e) {
			LOGGER.error("Failed to ensure Job Mapping page state: " + e.getMessage());
		}
	}

	/**
	 * Check if Missing Data screen is currently open
	 */
	private boolean isMissingDataScreenOpen() {
		try {
			// Look for indicators that Missing Data screen is open
			String[] missingDataScreenIndicators = {
					"//h1[contains(text(), 'Jobs with Missing Data')] | //h2[contains(text(), 'Jobs with Missing Data')]",
					"//button[contains(text(),'Close')] | //button[contains(@aria-label,'close')]",
					"//div[contains(@class, 'modal')] | //div[contains(@class, 'dialog')]",
					"//table[contains(@class, 'missing-data')] | //div[contains(@class, 'missing-data')]" };

			for (String indicator : missingDataScreenIndicators) {
				try {
					List<WebElement> elements = driver.findElements(By.xpath(indicator));
					if (!elements.isEmpty() && elements.get(0).isDisplayed()) {
						return true;
					}
				} catch (Exception e) {
					// Indicator check failed
				}
			}

			// Check if we're NOT on Job Mapping page (might be on Missing Data screen)
			try {
				WebElement jobMappingIndicator = driver.findElement(By.xpath(
						"//input[@placeholder='Search jobs...'] | //input[contains(@placeholder,'Search')] | //div[@id='org-job-container']"));
				if (!jobMappingIndicator.isDisplayed()) {
					return true;
				}
			} catch (Exception e) {
				return true;
			}

			return false;

		} catch (Exception e) {
			// When in doubt, assume it might be open to be safe
			return false;
		}
	}

	/**
	 * Verify user is back on Job Mapping page
	 */
	public void verify_user_is_back_on_job_mapping_page() throws IOException {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(JOB_SEARCH_INPUT));

			Assert.assertTrue(findElement(JOB_SEARCH_INPUT).isDisplayed(), "Job search input not visible - not on Job Mapping page");

			PageObjectHelper.log(LOGGER, "Successfully verified user is back on Job Mapping page");

		} catch (Exception e) {
			PageObjectHelper.log(LOGGER, "Failed to verify user is back on Job Mapping page: " + e.getMessage());
			throw new IOException("Failed to verify user is back on Job Mapping page", e);
		}
	}

	/**
	 * Search for the extracted job profile by name in Job Mapping page Uses full
	 * job name for more precise search results
	 */
	public void search_for_the_extracted_job_profile_by_name_in_job_mapping_page() throws IOException {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(findElement(JOB_SEARCH_INPUT)));

			// Use full job name for search (including timestamp for uniqueness)
			String fullJobName = jobDetailsFromMissingDataScreen.get("jobName");
			String searchTerm = fullJobName;

			findElement(JOB_SEARCH_INPUT).clear();
			findElement(JOB_SEARCH_INPUT).sendKeys(searchTerm);
			findElement(JOB_SEARCH_INPUT).sendKeys(Keys.ENTER);

			LOGGER.info("Searching for: {}", searchTerm);

			// Wait for loader to disappear
			wait.until(ExpectedConditions
					.invisibilityOfAllElements(driver.findElements(By.xpath("//div[@data-testid='loader']//img"))));

			// Wait for "No data available" message to disappear (temporary loading state)
			try {
				WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
				shortWait.until(ExpectedConditions
						.invisibilityOfElementLocated(By.xpath("//*[contains(text(), 'No data available')]")));
			} catch (Exception e) {
				// Message already gone or not present
			}

			// Wait for actual job rows to appear
			try {
				WebDriverWait rowWait = new WebDriverWait(driver, Duration.ofSeconds(10));
				rowWait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//div[@id='org-job-container']//tbody//tr[not(contains(@class, 'bg-gray'))]")));
			} catch (Exception e) {
				LOGGER.warn("No job rows appeared after search");
			}

			// Additional wait for results to stabilize
			PerformanceUtils.safeSleep(driver, 2000);
			PerformanceUtils.waitForPageReady(driver, 3);

			// Verify search results
			verifySearchResultsContainSearchTerm(searchTerm);

			PageObjectHelper.log(LOGGER, "Searched for job profile: " + searchTerm);

		} catch (Exception e) {
			PageObjectHelper.log(LOGGER, "Failed to search for job profile: " + e.getMessage());
			throw new IOException("Failed to search for job profile", e);
		}
	}

	/**
	 * Verify that search results actually contain the search term (to detect search
	 * filtering issues) Uses bidirectional contains to handle full job names with
	 * timestamps
	 */
	private void verifySearchResultsContainSearchTerm(String searchTerm) throws IOException {
		try {
			// Get all job rows from search results
			List<WebElement> jobRows = driver.findElements(By.xpath("//div[@id='org-job-container']//tbody//tr"));

			if (jobRows.isEmpty()) {
				LOGGER.warn("No job rows found after search");
				return;
			}

			// Check if any job row contains the search term - using PO29 approach
			boolean foundMatchingResult = false;
			int totalRows = jobRows.size();
			String searchTermLower = searchTerm.toLowerCase();
			int rowsChecked = 0;

			for (int i = 0; i < Math.min(totalRows, 10); i++) {
				try {
					WebElement row = jobRows.get(i);

					// Skip separator rows (gray background)
					String rowClass = row.getAttribute("class");
					if (rowClass != null && rowClass.contains("bg-gray")) {
						continue;
					}

					// Use row.getText() like PO29 does (simple and reliable)
					String rowText = row.getText().trim();

					// Skip non-job rows
					if (rowText.isEmpty() || rowText.toLowerCase().contains("function")
							|| rowText.toLowerCase().contains("sub-function")
							|| rowText.toLowerCase().contains("reduced match accuracy")
							|| rowText.toLowerCase().contains("info message")) {
						continue;
					}

					rowsChecked++;

					// Extract job code from row text
					String jobCodeText = "";
					String cleanJobName = rowText;
					if (rowText.contains("(") && rowText.contains(")")) {
						int startParen = rowText.lastIndexOf("(");
						int endParen = rowText.lastIndexOf(")");
						if (startParen > 0 && endParen > startParen) {
							jobCodeText = rowText.substring(startParen + 1, endParen).trim();
							cleanJobName = rowText.substring(0, startParen).trim();
						}
					}

					String jobNameLower = cleanJobName.toLowerCase();

					// Bidirectional contains check (handles partial matches and full names)
					boolean matchA = searchTermLower.contains(jobNameLower);
					boolean matchB = jobNameLower.contains(searchTermLower);

					if (matchA || matchB) {
						foundMatchingResult = true;
						LOGGER.info("✓ Search verified: {} ({})", cleanJobName, jobCodeText);
						break;
					}
				} catch (Exception e) {
					continue; // Skip problematic rows
				}
			}

			if (!foundMatchingResult) {
				LOGGER.error("Search term '{}' not found in {} results", searchTerm, rowsChecked);
				throw new IOException(
						"Search filtering not working - search term '" + searchTerm + "' not found in results");
			}

		} catch (IOException e) {
			throw e; // Re-throw IOException as-is
		} catch (Exception e) {
			LOGGER.error("Unexpected error in search verification: {}", e.getMessage(), e);
			throw new IOException("Search verification failed: " + e.getMessage());
		}
	}

	/**
	 * Verify job profile is found and displayed in search results - Using Features
	 * 27/28 proven approach
	 */
	public void verify_job_profile_is_found_and_displayed_in_search_results() throws IOException {
		try {
			// Wait for org-job-container to be available
			PerformanceUtils.safeSleep(driver, 2000);
			PerformanceUtils.waitForPageReady(driver, 2);

			// Get all job rows from org-job-container structure
			List<WebElement> jobRows = driver.findElements(By.xpath("//div[@id='org-job-container']//tbody//tr"));

			if (jobRows.isEmpty()) {
				LOGGER.warn("org-job-container not found, trying fallback");
				jobRows = findElements(JOB_ROWS_IN_JOB_MAPPING_PAGE);
			}

			if (jobRows.isEmpty()) {
				String errorMsg = "No job rows found on Job Mapping page";
				PageObjectHelper.log(LOGGER, errorMsg);
				throw new IOException(errorMsg);
			}

			// Get job name and code from Missing Data screen for exact matching
			String targetJobName = jobDetailsFromMissingDataScreen.get("jobName");
			String targetJobCode = jobDetailsFromMissingDataScreen.get("jobCode");

			LOGGER.info("Finding exact match: {} ({})", targetJobName, targetJobCode);

			// Search through job rows using Features 27/28 approach with scrolling
			boolean jobFound = false;
			WebElement matchingRow = null;
			int totalRowsChecked = 0;
			boolean hasMoreRows = true;

			// Keep scrolling and searching until we find the job or reach the end
			while (!jobFound && hasMoreRows) {
				// Get current job rows
				List<WebElement> currentJobRows = driver
						.findElements(By.xpath("//div[@id='org-job-container']//tbody//tr"));

				if (currentJobRows.isEmpty()) {
					// Try alternative approach if org-job-container not found
					LOGGER.warn("org-job-container structure not found. Trying fallback...");
					currentJobRows = findElements(JOB_ROWS_IN_JOB_MAPPING_PAGE);
				}

				LOGGER.debug("Checking rows: {} total", currentJobRows.size());

				// Search through current batch of rows (start from where we left off)
				for (int i = totalRowsChecked; i < currentJobRows.size(); i++) {
					WebElement row = currentJobRows.get(i);
					totalRowsChecked++;

					// Scroll to current row every 10 rows for progress visibility
					if (totalRowsChecked % 10 == 0) {
						js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", row);
						PerformanceUtils.safeSleep(driver, 200);
						LOGGER.info("Searching... checked " + totalRowsChecked + " rows so far");
					}

					try {
						// Skip rows that are not job data rows (Function/Sub-function rows, empty rows,
						// etc.)
						String rowText = row.getText().trim();
						if (rowText.isEmpty() || rowText.toLowerCase().contains("function")
								|| rowText.toLowerCase().contains("sub-function")
								|| rowText.toLowerCase().contains("reduced match accuracy")
								|| rowText.toLowerCase().contains("info message")) {
							continue; // Skip non-job rows
						}

						String jobNameText = "";
						String jobCodeText = "";

						// Try multiple approaches to find job name and code
						String[] jobNameXPaths = { ".//td[1]//div", // Features 27/28 approach
								".//td[1]", // Direct cell content
								".//td[2]//div", // Sometimes job name is in 2nd column
								".//td[2]", // Direct 2nd cell content
								".//*[contains(@class, 'job-name')] | .//*[contains(@class, 'name')]", // Class-based
								".//span | .//div | .//p" // Any text container
						};

						// Try each XPath until we find readable text
						for (String xpath : jobNameXPaths) {
							try {
								WebElement jobNameCell = row.findElement(By.xpath(xpath));
								String text = jobNameCell.getText().trim();
								if (!text.isEmpty() && !text.toLowerCase().contains("function")
										&& !text.toLowerCase().contains("sub-function")) {
									jobNameText = text;
									break;
								}
							} catch (Exception e) {
								// Try next XPath
								continue;
							}
						}

						// Skip if no valid job name found
						if (jobNameText.isEmpty()) {
							continue;
						}

						// Extract job code from the job name text if it contains parentheses
						if (jobNameText.contains("(") && jobNameText.contains(")")) {
							int startParen = jobNameText.lastIndexOf("(");
							int endParen = jobNameText.lastIndexOf(")");
							if (startParen > 0 && endParen > startParen) {
								jobCodeText = jobNameText.substring(startParen + 1, endParen).trim();
								jobNameText = jobNameText.substring(0, startParen).trim();
							}
						}

						// Clean job name by removing trailing hyphen and extra spaces
						jobNameText = jobNameText.replaceAll("\\s*-\\s*$", "").trim();

						// Skip if no valid job name after extraction
						if (jobNameText.isEmpty()) {
							continue;
						}

						// Check if this row matches: Contains match for Job Name AND Exact match for
						// Job Code
						// Bidirectional contains for name flexibility (handles partial names,
						// timestamps, etc.)
						String targetNameLower = targetJobName.toLowerCase();
						String jobNameLower = jobNameText.toLowerCase();
						boolean nameMatches = targetNameLower.contains(jobNameLower)
								|| jobNameLower.contains(targetNameLower);

						// Exact match for job code
						boolean codeMatches = targetJobCode != null && !targetJobCode.isEmpty()
								&& targetJobCode.equals(jobCodeText);

						LOGGER.debug("Checking: {} ({})", jobNameText, jobCodeText);

						if (nameMatches && codeMatches) {
							matchingRow = row;
							matchingJobRow = row; // Store for extraction step
							jobFound = true;
							LOGGER.info("✓ Found match: {} ({})", jobNameText, jobCodeText);
							break;
						}
					} catch (Exception e) {
						// Error reading row, continue to next
						LOGGER.warn("Error reading row " + totalRowsChecked + ": " + e.getMessage());
					}
				}

				if (!jobFound) {
					// Check if there are more rows to load by scrolling down
					int currentRowCount = currentJobRows.size();
					js.executeScript("window.scrollTo(0, document.documentElement.scrollHeight);"); // Scroll DOWN
																									// (headless-compatible)
					PerformanceUtils.safeSleep(driver, 2000); // Wait for potential lazy loading

					List<WebElement> newJobRows = driver
							.findElements(By.xpath("//div[@id='org-job-container']//tbody//tr"));
					int newRowCount = newJobRows.size();

					if (newRowCount <= currentRowCount) {
						// No more rows loaded - we've reached the end
						hasMoreRows = false;
						LOGGER.info("Reached end of search results. Total rows checked: " + totalRowsChecked);
					} else {
						LOGGER.info("Loaded " + (newRowCount - currentRowCount) + " more rows. Continuing search...");
					}
				}
			}

			// Scroll to the found job profile for better visibility
			if (matchingRow != null) {
				js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", matchingRow);
				PerformanceUtils.safeSleep(driver, 500);
			}

			Assert.assertTrue(jobFound, "No exact matching job profile found in search results with Name='"
					+ targetJobName + "' AND Code='" + targetJobCode + "'");

			PageObjectHelper.log(LOGGER, "Job profile verified in search results");

		} catch (Exception e) {
			PageObjectHelper.log(LOGGER, "Failed to verify job profile in search results: " + e.getMessage());
			throw new IOException("Failed to verify job profile in search results", e);
		}
	}

	/**
	 * Extract job details from searched profile in Job Mapping page - Enhanced with
	 * debugging and robust extraction
	 */
	public void extract_job_details_from_searched_profile_in_job_mapping_page() throws IOException {
		try {
			// Check if we have the job name from Missing Data screen
			String jobNameFromMissingData = jobDetailsFromMissingDataScreen.get("jobName");

			if (jobNameFromMissingData == null || jobNameFromMissingData.trim().isEmpty()) {
				throw new IOException("No job name available from Missing Data screen for extraction");
			}

			// Use the stored matching row from verification step
			if (matchingJobRow != null) {
				WebElement matchingRow = matchingJobRow;

				// Extract all cells from the matching row
				List<WebElement> allCells = matchingRow.findElements(By.xpath(".//td"));

				// Extract job name
				String jobNameFromMapping = "";
				if (allCells.size() >= 2) {
					jobNameFromMapping = extractCellTextLocal(allCells.get(1)); // Cell[1] = Job Name
					jobNameFromMapping = cleanJobNameLocal(jobNameFromMapping);
				} else {
					// Fallback to text parsing
					String rowText = matchingRow.getText();
					String[] spaceSplit = rowText.split("\\s{2,}");
					String[] screenSplit = rowText.split("\\t+");

					if (spaceSplit.length > 0 && spaceSplit[0].trim().length() > 0) {
						jobNameFromMapping = spaceSplit[0].trim();
					} else if (screenSplit.length > 0 && screenSplit[0].trim().length() > 0) {
						jobNameFromMapping = screenSplit[0].trim();
					} else {
						jobNameFromMapping = jobNameFromMissingData;
					}
				}

				// Extract other fields using enhanced approach
				String gradeFromMapping = "";
				String departmentFromMapping = "";
				String functionFromMapping = "";

				try {
					// Enhanced cell extraction - Based on DOM structure:
					// Main row: Cell[0] = Checkbox, Cell[1] = Job Name, Cell[2] = Grade, Cell[3] =
					// Department
					// Function appears in the NEXT ROW with pattern "Function / Sub-function:
					// [value]"

					if (allCells.size() >= 3) {
						gradeFromMapping = extractCellTextLocal(allCells.get(2)); // Cell[2] = Grade
						gradeFromMapping = normalizeFieldValue(gradeFromMapping, "Grade");
					}
					if (allCells.size() >= 4) {
						departmentFromMapping = extractCellTextLocal(allCells.get(3)); // Cell[3] = Department
						departmentFromMapping = normalizeFieldValue(departmentFromMapping, "Department");
					}

					// Function is in the next row
					functionFromMapping = "";
					try {
						List<WebElement> followingRows = matchingRow.findElements(By.xpath(".//following-sibling::tr"));

						for (int fIdx = 0; fIdx < Math.min(followingRows.size(), 2); fIdx++) {
							WebElement followingRow = followingRows.get(fIdx);
							String rowText = followingRow.getText();

							if (rowText.toLowerCase().contains("function") && rowText.contains("|")) {
								String[] parts = rowText.split("Function\\s*/\\s*Sub-function:\\s*", 2);
								if (parts.length > 1) {
									String functionPart = parts[1].trim();

									// Clean up function text - remove info messages
									String[] functionLines = functionPart.split("\\n");
									functionPart = functionLines[0].trim();
									functionPart = functionPart.replaceAll("\\s*Reduced\\s+match\\s+accuracy.*$", "");
									functionPart = functionPart.replaceAll("\\s*due\\s+to\\s+missing\\s+data.*$", "");
									functionPart = functionPart.replaceAll("\\s*Info\\s+Message.*$", "");

									functionFromMapping = normalizeFieldValue(functionPart.trim(),
											"Function/Subfunction");
									break;
								} else if (rowText.contains("|") && rowText.contains(":")) {
									String[] colonParts = rowText.split(":", 2);
									if (colonParts.length > 1) {
										String functionPart = colonParts[1].trim();
										String[] functionLines = functionPart.split("\\n");
										functionPart = functionLines[0].trim();
										functionPart = functionPart.replaceAll("\\s*Reduced\\s+match\\s+accuracy.*$",
												"");
										functionPart = functionPart.replaceAll("\\s*due\\s+to\\s+missing\\s+data.*$",
												"");
										functionPart = functionPart.replaceAll("\\s*Info\\s+Message.*$", "");

										functionFromMapping = normalizeFieldValue(functionPart.trim(),
												"Function/Subfunction");
										break;
									}
								}
							}
						}
					} catch (Exception e) {
						LOGGER.warn("Could not extract Function from following rows: " + e.getMessage());
					}

					if (functionFromMapping.isEmpty()) {
						functionFromMapping = normalizeFieldValue("", "Function/Subfunction");
					}

					// Enhanced validation - don't fail if Grade looks unusual since we're
					// validating missing Grade
					if (departmentFromMapping.contains(jobNameFromMapping) && departmentFromMapping.length() > 50) {
						LOGGER.warn("Department extraction might be incorrect - contains job name");
					}

				} catch (Exception e) {
					LOGGER.warn("Structured cell extraction failed, trying text-based extraction");

					// Fallback: text-based extraction
					String rowText = matchingRow.getText();
					String[] strategies = { "\\n", "\\t+", "\\s{3,}", "\\s{2,}", "\\|" };

					boolean extractionSuccess = false;
					for (String strategy : strategies) {
						try {
							String[] textParts = rowText.split(strategy);
							if (textParts.length >= 3) {
								gradeFromMapping = normalizeFieldValue(textParts[1].trim(), "Grade");
								departmentFromMapping = normalizeFieldValue(textParts[2].trim(), "Department");

								if (!gradeFromMapping.contains(jobNameFromMapping) && gradeFromMapping.length() < 100) {
									extractionSuccess = true;
									break;
								}
							}
						} catch (Exception ex) {
							continue;
						}
					}

					// Function extraction fallback
					if (functionFromMapping.isEmpty()) {
						try {
							List<WebElement> followingRows = matchingRow
									.findElements(By.xpath(".//following-sibling::tr"));
							for (WebElement followingRow : followingRows) {
								String followingRowText = followingRow.getText();
								if (followingRowText.toLowerCase().contains("function")
										&& followingRowText.contains("|")) {
									String[] parts = followingRowText.split("Function\\s*/\\s*Sub-function:\\s*", 2);
									if (parts.length > 1) {
										String functionPart = parts[1].trim();
										String[] functionLines = functionPart.split("\\n");
										functionPart = functionLines[0].trim();
										functionPart = functionPart.replaceAll("\\s*Reduced\\s+match\\s+accuracy.*$",
												"");
										functionPart = functionPart.replaceAll("\\s*due\\s+to\\s+missing\\s+data.*$",
												"");
										functionPart = functionPart.replaceAll("\\s*Info\\s+Message.*$", "");

										functionFromMapping = normalizeFieldValue(functionPart.trim(),
												"Function/Subfunction");
										break;
									}
								}
							}
						} catch (Exception funcEx) {
							// Ignore fallback errors
						}
					}

					if (!extractionSuccess) {
						gradeFromMapping = normalizeFieldValue("", "Grade");
						departmentFromMapping = normalizeFieldValue("", "Department");
					}

					if (functionFromMapping.isEmpty()) {
						functionFromMapping = normalizeFieldValue("", "Function/Subfunction");
					}
				}

				// Store extracted job details
				jobDetailsFromJobMappingPage.clear();
				jobDetailsFromJobMappingPage.put("jobName", cleanJobNameLocal(jobNameFromMapping));
				jobDetailsFromJobMappingPage.put("grade", gradeFromMapping);
				jobDetailsFromJobMappingPage.put("department", departmentFromMapping);
				jobDetailsFromJobMappingPage.put("functionSubfunction", functionFromMapping);

				LOGGER.info("Extracted job details - Job: '" + cleanJobNameLocal(jobNameFromMapping) + "', Grade: '"
						+ gradeFromMapping + "', Department: '" + departmentFromMapping + "', Function: '"
						+ functionFromMapping + "'");

				PageObjectHelper.log(LOGGER, "Successfully extracted job details from Job Mapping page");
			} else {
				throw new IOException("No matching job row available from verification step");
			}

		} catch (Exception e) {
			PageObjectHelper.log(LOGGER, "Failed to extract job details: " + e.getMessage());
			throw new IOException("Failed to extract job details from Job Mapping page", e);
		}
	}

	/**
	 * Verify all job details match between Jobs Missing Data screen and Job Mapping
	 * page - Using Feature 27 logic
	 */
	public void verify_all_job_details_match_between_jobs_missing_data_screen_and_job_mapping_page()
			throws IOException {
		try {
			LOGGER.debug("Comparing job details between screens");

			// Get details from both screens with null safety
			String nameFromMissingData = jobDetailsFromMissingDataScreen.get("jobName") != null
					? jobDetailsFromMissingDataScreen.get("jobName")
					: "";
			String nameFromJobMapping = jobDetailsFromJobMappingPage.get("jobName") != null
					? jobDetailsFromJobMappingPage.get("jobName")
					: "";
			String gradeFromMissingData = jobDetailsFromMissingDataScreen.get("grade") != null
					? jobDetailsFromMissingDataScreen.get("grade")
					: "";
			String gradeFromJobMapping = jobDetailsFromJobMappingPage.get("grade") != null
					? jobDetailsFromJobMappingPage.get("grade")
					: "";
			String deptFromMissingData = jobDetailsFromMissingDataScreen.get("department") != null
					? jobDetailsFromMissingDataScreen.get("department")
					: "";
			String deptFromJobMapping = jobDetailsFromJobMappingPage.get("department") != null
					? jobDetailsFromJobMappingPage.get("department")
					: "";
			String funcFromMissingData = jobDetailsFromMissingDataScreen.get("functionSubfunction") != null
					? jobDetailsFromMissingDataScreen.get("functionSubfunction")
					: "";
			String funcFromJobMapping = jobDetailsFromJobMappingPage.get("functionSubfunction") != null
					? jobDetailsFromJobMappingPage.get("functionSubfunction")
					: "";

			// Compare job details
			// Verify job details match between screens

			// Verify job name matches (allowing for format differences like Feature 27)
			if (!nameFromMissingData.isEmpty() && !nameFromJobMapping.isEmpty()) {
				String searchNameMissingData = extractJobNameForSearchLocal(nameFromMissingData);
				String searchNameJobMapping = extractJobNameForSearchLocal(nameFromJobMapping);
				boolean jobNameMatches = searchNameMissingData.toLowerCase()
						.contains(searchNameJobMapping.toLowerCase())
						|| searchNameJobMapping.toLowerCase().contains(searchNameMissingData.toLowerCase());
				if (!jobNameMatches) {
					String errorMsg = "Job Name mismatch: MissingData='" + nameFromMissingData + "' vs JobMapping='"
							+ nameFromJobMapping + "'";
					LOGGER.error(errorMsg);
					Assert.fail(errorMsg);
				}
			} else {
				LOGGER.warn("Job Name comparison skipped - one or both values are empty");
			}

			// Compare all fields using unified N/A "-" mapping logic
			// Compare Grade
			if (!compareFieldValues("Grade", gradeFromMissingData, gradeFromJobMapping)) {
				Assert.fail("Grade comparison failed - see logs for details");
			}

			// Compare Department
			if (!compareFieldValues("Department", deptFromMissingData, deptFromJobMapping)) {
				Assert.fail("Department comparison failed - see logs for details");
			}

			// Compare Function/Sub-function
			if (!compareFieldValues("Function/Sub-function", funcFromMissingData, funcFromJobMapping)) {
				Assert.fail("Function/Sub-function comparison failed - see logs for details");
			}

			PageObjectHelper.log(LOGGER, "Successfully verified job details match between both screens");

		} catch (Exception e) {
			PageObjectHelper.log(LOGGER, "Job details do not match between screens: " + e.getMessage());
			throw new IOException("Job details do not match between screens", e);
		}
	}

	/**
	 * Verify Info Message is displayed on searched profile indicating missing
	 * Department data - Using Features 27/28 proven approach
	 */
	public void verify_info_message_is_displayed_on_searched_profile_indicating_missing_department_data()
			throws IOException {
		try {
			String fullJobName = jobDetailsFromMissingDataScreen.get("jobName");
			LOGGER.info("Verifying Info Message for: {}", fullJobName);

			// Check if we have a matching job row from verification step
			if (matchingJobRow == null) {
				throw new IOException("No matching job row available for info message verification");
			}

			// Use JavaScript for faster info message detection
			boolean infoMessageFound = (Boolean) js.executeScript("const row = arguments[0];" +
			// Check same row
					"if (row.querySelector('div[role=\"button\"][aria-label*=\"Reduced match accuracy\"]')) return true;"
					+
					// Check following siblings (N+1, N+2, N+3)
					"let nextRow = row.nextElementSibling;" + "for (let i = 0; i < 3 && nextRow; i++) {"
					+ "  if (nextRow.querySelector('div[role=\"button\"][aria-label*=\"Reduced match accuracy\"]')) return true;"
					+ "  nextRow = nextRow.nextElementSibling;" + "}" +
					// Check preceding siblings
					"let prevRow = row.previousElementSibling;" + "for (let i = 0; i < 2 && prevRow; i++) {"
					+ "  if (prevRow.querySelector('div[role=\"button\"][aria-label*=\"Reduced match accuracy\"]')) return true;"
					+ "  prevRow = prevRow.previousElementSibling;" + "}" + "return false;", matchingJobRow);

			Assert.assertTrue(infoMessageFound,
					"Info message not found on searched profile indicating missing Department data");

			PageObjectHelper.log(LOGGER, "Info Message verified for profile with missing Department data");

		} catch (Exception e) {
			PageObjectHelper.log(LOGGER, "Failed to verify Info Message: " + e.getMessage());
			throw new IOException("Failed to verify Info Message", e);
		}
	}

	/**
	 * Helper method to normalize field values consistently across all fields
	 * Handles the mapping where "-" and empty values represent "N/A" For
	 * Function/Sub-function fields, also handles cases where "-" and "N/A" are
	 * equivalent
	 */
	private String normalizeFieldValue(String fieldValue, String fieldName) {
		if (fieldValue == null || fieldValue.trim().isEmpty() || "-".equals(fieldValue.trim())) {
			return "N/A";
		}

		// Special handling for Function/Sub-function fields
		if ("Function/Subfunction".equals(fieldName) || "Function".equals(fieldName)
				|| "Function/Sub-function".equals(fieldName)) {
			// For Function fields, treat both "-" and "N/A" as equivalent (both represent
			// missing data)
			if ("N/A".equalsIgnoreCase(fieldValue.trim())) {
				return "N/A";
			}
			// Handle cases like "- | N/A" vs "N/A | N/A" or "MNGR | -" vs "MNGR | N/A" -
			// normalize both parts
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
					if ("-".equals(subfunctionPart) || subfunctionPart.isEmpty()
							|| "N/A".equalsIgnoreCase(subfunctionPart)) {
						subfunctionPart = "N/A";
					}

					return functionPart + " | " + subfunctionPart;
				}
			}
		}

		return fieldValue.trim();
	}

	/**
	 * Helper method to compare field values with N/A "-" mapping support
	 */
	private boolean compareFieldValues(String fieldName, String missingDataValue, String jobMappingValue) {
		if (missingDataValue == null || missingDataValue.isEmpty() || jobMappingValue == null
				|| jobMappingValue.isEmpty()) {
			return true; // Skip comparison for empty values
		}

		// Normalize both values for comparison
		String normalizedMissingData = normalizeFieldValue(missingDataValue, fieldName);
		String normalizedJobMapping = normalizeFieldValue(jobMappingValue, fieldName);

		boolean matches = normalizedMissingData.equalsIgnoreCase(normalizedJobMapping);

		if (matches) {
			LOGGER.info(" " + fieldName + " matches");
		} else {
			String errorMsg = fieldName + " mismatch: '" + missingDataValue + "' vs '" + jobMappingValue + "'";
			PageObjectHelper.log(LOGGER, "- " + errorMsg);
		}

		return matches;
	}

	/**
	 * Helper method to extract text from a table cell using multiple strategies
	 */
	private String extractCellTextLocal(WebElement cell) {
		try {
			// Strategy 1: Try to find div within cell (Features 27/28 approach)
			try {
				WebElement div = cell.findElement(By.xpath(".//div"));
				String text = div.getText().trim();
				if (!text.isEmpty()) {
					return text;
				}
			} catch (Exception e) {
				// Continue to next strategy
			}

			// Strategy 2: Try to find span within cell
			try {
				WebElement span = cell.findElement(By.xpath(".//span"));
				String text = span.getText().trim();
				if (!text.isEmpty()) {
					return text;
				}
			} catch (Exception e) {
				// Continue to next strategy
			}

			// Strategy 3: Get direct cell text
			String cellText = cell.getText().trim();
			if (!cellText.isEmpty()) {
				return cellText;
			}

			// Strategy 4: Try any text-containing element
			try {
				List<WebElement> textElements = cell.findElements(By.xpath(".//*[text()]"));
				if (!textElements.isEmpty()) {
					for (WebElement elem : textElements) {
						String text = elem.getText().trim();
						if (!text.isEmpty()) {
							return text;
						}
					}
				}
			} catch (Exception e) {
				// Continue to final strategy
			}

			// Strategy 5: Get attribute value if it exists
			try {
				String value = cell.getAttribute("value");
				if (value != null && !value.isEmpty()) {
					return value;
				}
			} catch (Exception e) {
				// Final fallback
			}

			return ""; // Return empty if no text found

		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * Helper method to check if Function/Subfunction field has data Format:
	 * "Function | Subfunction" where: - "- | -" = Both empty (NO DATA) - "Value |
	 * -" = Function has data (HAS DATA) - "- | Value" = Subfunction has data (HAS
	 * DATA)
	 */
	@SuppressWarnings("unused")
	private boolean hasFunctionSubfunctionData(String functionSubfunction, String context) {
		if (functionSubfunction == null || functionSubfunction.trim().isEmpty()
				|| "NO_FUNCTION_CELL".equals(functionSubfunction)) {
			LOGGER.info("  Function/Subfunction (" + context + "): No data or empty");
			return false;
		}

		String[] functionParts = functionSubfunction.split("\\|");
		if (functionParts.length >= 2) {
			String functionPart = functionParts[0].trim();
			String subfunctionPart = functionParts[1].trim();

			// Has data if either function OR subfunction has actual value (not "-")
			boolean functionPartHasData = !"-".equals(functionPart) && !functionPart.isEmpty()
					&& !"N/A".equalsIgnoreCase(functionPart);
			boolean subfunctionPartHasData = !"-".equals(subfunctionPart) && !subfunctionPart.isEmpty()
					&& !"N/A".equalsIgnoreCase(subfunctionPart);

			boolean hasData = functionPartHasData || subfunctionPartHasData;

			LOGGER.info("  Function/Subfunction parsing (" + context + "):");
			LOGGER.info("    Full value: '" + functionSubfunction + "'");
			LOGGER.info("    Function part: '" + functionPart + "'  Has data: " + functionPartHasData);
			LOGGER.info("    Subfunction part: '" + subfunctionPart + "'  Has data: " + subfunctionPartHasData);
			LOGGER.info("    Overall has data: " + hasData + " (at least one part has data)");

			return hasData;
		} else {
			// Single value without "|" - treat as function part only
			boolean hasData = !"-".equals(functionSubfunction.trim()) && !functionSubfunction.trim().isEmpty()
					&& !"N/A".equalsIgnoreCase(functionSubfunction.trim());
			LOGGER.info("  Function/Subfunction (no separator, " + context + "): '" + functionSubfunction
					+ "'  Has data: " + hasData);
			return hasData;
		}
	}

	// ===== REVERSE SCENARIO METHODS =====

	/**
	 * Sort Job Profiles by Department in Ascending order - REVERSE SCENARIO
	 * (Scenario 2): - Used in Reverse flow to get profiles with missing Department
	 * first (Department="-" or empty) - Helps locate jobs with info messages about
	 * missing Department data more efficiently
	 */
	public void sort_job_profiles_by_department_in_ascending_order() throws IOException {
		try {
			PageObjectHelper.log(LOGGER, 
					"Sorting profiles by Department (ascending) to get missing Department profiles first...");

			// Look for Department column header to sort
			// Note: Text is nested inside <div> within <th>, so we use . instead of text()
			List<WebElement> departmentHeaders = driver
					.findElements(By.xpath("//th[contains(normalize-space(.), 'DEPARTMENT')]"));

			if (departmentHeaders.isEmpty()) {
				// Try case-insensitive search
				departmentHeaders = driver.findElements(By.xpath(
						"//th[contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'department')]"));
			}

			if (!departmentHeaders.isEmpty()) {
				WebElement departmentHeader = departmentHeaders.get(0);

				// Scroll to header and click to sort
				js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});",
						departmentHeader);
				PerformanceUtils.safeSleep(driver, 500);

				departmentHeader.click();
				PerformanceUtils.safeSleep(driver, 2000); // Wait for sorting to complete

				PageObjectHelper.log(LOGGER, 
						"... Sorted profiles by Department - missing Department profiles should appear first");

			} else {
				LOGGER.warn("Department column header not found, proceeding without sorting");
				PageObjectHelper.log(LOGGER, " Department column not found for sorting, proceeding with current order");
			}

		} catch (Exception e) {
			LOGGER.warn("Failed to sort by Department column: " + e.getMessage() + ". Proceeding without sorting.");
			PageObjectHelper.log(LOGGER, " Sorting failed, proceeding with current order");
		}
	}

	/**
	 * Find job profile with missing DEPARTMENT in Job Mapping page - FORWARD
	 * SCENARIO (Scenario 1): - Used in Forward flow: Find job on Job Mapping page
	 * with missing Department + info message - Searches Job Mapping screen for
	 * FIRST job with missing Department and info message - Required conditions:
	 * Department missing + Info message displayed (ignoring Grade/Function data)
	 */
	public void find_job_profile_in_job_mapping_page_where_department_is_missing() throws IOException {
		try {
			// ===== CLEAR STATIC VARIABLES FOR FRESH SCENARIO EXECUTION =====
			jobDetailsFromJobMappingPage.clear();
			jobDetailsFromMissingDataScreen.clear();
			extractedJobName.set("");
			foundJobRow = null;
			foundProfile = null;
			forwardScenarioFoundProfile.set(false);
			; // Reset flag at start of Forward scenario
			forwardScenarioJobName.set(""); // Reset Forward scenario tracking
			forwardScenarioJobCode.set(""); // Reset Forward scenario tracking

			LOGGER.info(
					"FORWARD SCENARIO (Scenario 1): Searching for first job with info message where Department is missing");
			PageObjectHelper.log(LOGGER, 
					" FORWARD SCENARIO (Scenario 1): Finding FIRST job with info message and missing Department...");

			// Progressive search: Check initial batch, then scroll and check new batch if
			// no match
			LOGGER.info(
					"Starting progressive search: check batch  if no match  scroll  check new batch  repeat (max 15 scrolls, up to ~50 profiles)");
			PageObjectHelper.log(LOGGER, " Progressive search: checking profiles in batches with on-demand scrolling...");

			int maxScrollAttempts = 15;
			int scrollAttempt = 0;
			int lastCheckedIndex = 0;
			boolean matchFound = false;

			// Loop: Check current visible profiles If no match Scroll to load more Repeat
			while (scrollAttempt <= maxScrollAttempts && !matchFound) {
				// Get current info message rows
				List<WebElement> infoMessageRows = driver.findElements(By.xpath(
						"//div[@id='org-job-container']//tr[.//div[@role='button' and contains(@aria-label, 'Reduced match accuracy')]]"));
				int currentCount = infoMessageRows.size();

				// Debug: Log total job rows to understand the page state
				int totalJobRows = driver.findElements(By.xpath("//div[@id='org-job-container']//tbody//tr")).size();
				LOGGER.debug("Page state - Total job rows: {}, Info message rows: {}", totalJobRows, currentCount);

				if (scrollAttempt == 0) {
					LOGGER.info("Batch 0 (Initial): Checking " + currentCount + " profiles with info messages");
				} else {
					int newProfiles = currentCount - lastCheckedIndex;
					LOGGER.info("Batch " + scrollAttempt + " (After scroll " + scrollAttempt + "): Total="
							+ currentCount + ", New=" + newProfiles);

					// Only stop if we've already found some profiles and no new ones appear
					// If lastCheckedIndex == 0 (no profiles found yet), keep scrolling
					if (newProfiles == 0 && lastCheckedIndex > 0) {
						LOGGER.info("No new profiles loaded. Stopping search.");
						break;
					}
				}

				// Check only NEW profiles (from lastCheckedIndex onwards)
				for (int i = lastCheckedIndex; i < infoMessageRows.size(); i++) {
					WebElement infoMessageRow = infoMessageRows.get(i);
					int profileNumber = i + 1;

					LOGGER.info("Checking Profile " + profileNumber + "...");

					// Scroll to profile for visibility (INSTANT scroll - no smooth animation)
					js.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});",
							infoMessageRow);
					PerformanceUtils.safeSleep(driver, 200); // Reduced from 500ms to 200ms

					try {
						// 1. Extract Function/Subfunction using DIRECT JavaScript text extraction
						// (ULTRA-FAST)
						String functionSubfunction = "";

						try {
							// ULTRA-OPTIMIZED: Use JavaScript to extract text directly without any XPath
							// searching
							String rowText = (String) js.executeScript("return arguments[0].textContent;",
									infoMessageRow);
							rowText = rowText.trim();

							// Parse function from the text using known patterns
							String[] functionPatterns = { "Function / Sub-function:", "Function/Sub-function:",
									"Function:", "Sub-function:" };

							for (String pattern : functionPatterns) {
								if (rowText.contains(pattern)) {
									String[] parts = rowText.split(pattern);
									if (parts.length > 1) {
										String functionPart = parts[1].trim();

										// Clean up the function part
										if (functionPart.contains("Reduced match accuracy")) {
											functionPart = functionPart.split("Reduced match accuracy")[0].trim();
										}
										functionPart = functionPart.replaceAll("\\n", " ").replaceAll("\\s+", " ")
												.trim();

										if (!functionPart.isEmpty()) {
											functionSubfunction = functionPart;
											break;
										}
									}
								}
							}
							LOGGER.info("Profile " + profileNumber + " - Function extracted: '" + functionSubfunction
									+ "'");

						} catch (Exception e1) {
							LOGGER.debug("Could not extract function: " + e1.getMessage());
						}

						// 2. Get job data from previous row
						WebElement jobDataRow = null;
						try {
							jobDataRow = infoMessageRow.findElement(By.xpath("./preceding-sibling::tr[1]"));
							LOGGER.info("Profile " + profileNumber + " - Found job data row (previous row)");
						} catch (Exception e) {
							LOGGER.warn(
									"Profile " + profileNumber + " - Could not find job data row: " + e.getMessage());
							continue;
						}

						// 3. Extract job data using PROVEN logic from Features 27/28 (OPTIMIZED)
						String jobName = "";
						String jobCode = "";
						String grade = "";
						String department = "";

						// Extract Job Name and Code from column 2 (NAME / JOB CODE column) - Feature 28
						// approach (OPTIMIZED)
						try {
							List<WebElement> jobNameElements = jobDataRow
									.findElements(By.xpath(".//td[2]//div | .//td[position()=2]//div"));
							if (!jobNameElements.isEmpty()) {
								String jobNameCodeText = jobNameElements.get(0).getAttribute("textContent").trim();

								// Parse job name and code from format: "Job Name - (JOB-CODE)"
								if (jobNameCodeText.contains(" - (") && jobNameCodeText.contains(")")) {
									int dashIndex = jobNameCodeText.lastIndexOf(" - (");
									jobName = jobNameCodeText.substring(0, dashIndex).trim();
									jobCode = jobNameCodeText.substring(dashIndex + 4).replace(")", "").trim();
								} else {
									jobName = jobNameCodeText;
								}
							}
						} catch (Exception e) {
							// Could not extract job name/code
						}

						// Extract Grade from column 3 (GRADE column) - Feature 28 approach (OPTIMIZED)
						try {
							List<WebElement> gradeElements = jobDataRow
									.findElements(By.xpath(".//td[3]//div | .//td[position()=3]//div"));
							if (!gradeElements.isEmpty()) {
								grade = gradeElements.get(0).getAttribute("textContent").trim();
							}
						} catch (Exception e) {
							// Could not extract grade
						}

						// Extract Department from column 4 (DEPARTMENT column) - Feature 28 approach
						// (OPTIMIZED)
						try {
							List<WebElement> departmentElements = jobDataRow
									.findElements(By.xpath(".//td[4]//div | .//td[position()=4]//div"));
							if (!departmentElements.isEmpty()) {
								department = departmentElements.get(0).getAttribute("textContent").trim();
							}
						} catch (Exception e) {
							// Could not extract department
						}

						LOGGER.info("Extracted: " + jobName + " (" + jobCode + ") | Grade: " + grade + " | Dept: "
								+ department + " | Func: " + functionSubfunction);

						// Check if Department is missing
						boolean departmentIsMissing = "-".equals(department.trim()) || department.trim().isEmpty();

						LOGGER.info("Profile " + profileNumber + " - Department Missing: " + departmentIsMissing
								+ " (Required: TRUE)");

						// If match found, store and STOP
						if (departmentIsMissing) {
														PageObjectHelper.log(LOGGER, "Found suitable profile: " + jobName + " - Department missing");

							// Store the found profile
							foundJobRow = jobDataRow;
							foundProfile = infoMessageRow;
							extractedJobName.set(cleanJobNameLocal(jobName));
							forwardScenarioFoundProfile.set(true);
							forwardScenarioJobName.set(cleanJobNameLocal(jobName));
							forwardScenarioJobCode.set(jobCode);

							LOGGER.info("FORWARD SCENARIO (Scenario 1) will validate: " + extractedJobName.get()
									+ " (Code: " + jobCode + ")");
							matchFound = true;
							break; // Exit for loop - match found!
						}

					} catch (Exception e) {
						LOGGER.error("Profile " + profileNumber + " - Error during analysis: " + e.getMessage());
						e.printStackTrace();
					}
				}

				// Update last checked index
				lastCheckedIndex = currentCount;

				// If match found, stop searching
				if (matchFound) {
					LOGGER.info("... Match found! No further scrolling needed.");
					break; // Exit while loop
				}

				// If no match in current batch and haven't reached max scrolls, scroll to load
				// more
				if (scrollAttempt < maxScrollAttempts) {
					LOGGER.info("No match in batch " + scrollAttempt + ". Scrolling to load next batch...");

					// If no profiles have been checked yet (lastCheckedIndex == 0), do FULL PAGE
					// scroll
					if (lastCheckedIndex == 0) {
						LOGGER.info("No info messages found yet - performing FULL PAGE scroll to load profiles");
						js.executeScript("window.scrollTo(0, document.body.scrollHeight);");

						// CRITICAL: Use Thread.sleep for guaranteed waits
						try {
							Thread.sleep(2000); // 2 seconds - wait for lazy loading to start
						} catch (InterruptedException e) {
							Thread.currentThread().interrupt();
						}

						// Wait for spinner to disappear after scrolling
						PerformanceUtils.waitForSpinnersToDisappear(driver, 10);

						// Additional wait for profiles to render
						try {
							Thread.sleep(2000); // 2 seconds
						} catch (InterruptedException e) {
							Thread.currentThread().interrupt();
						}
						PerformanceUtils.waitForPageReady(driver, 5);

						// Scroll back to top to ensure info messages are in viewport
						js.executeScript("window.scrollTo(0, 0);");
						try {
							Thread.sleep(1000); // 1 second
						} catch (InterruptedException e) {
							Thread.currentThread().interrupt();
						}

						// CRITICAL: Wait for spinners again after scrolling to top (new spinners may
						// appear!)
						PerformanceUtils.waitForSpinnersToDisappear(driver, 10);

						// Final wait to ensure data is fully loaded
						try {
							Thread.sleep(2000); // 2 seconds
						} catch (InterruptedException e) {
							Thread.currentThread().interrupt();
						}
						PerformanceUtils.waitForPageReady(driver, 5);

						LOGGER.info("Full page scroll completed - checking for new profiles...");
					} else {
						// Normal incremental scroll when we've already found and checked some profiles
						// AGGRESSIVE SCROLL: Scroll to bottom to trigger lazy loading of more profiles
						LOGGER.info("Scrolling to load more profiles...");
						js.executeScript("window.scrollTo(0, document.body.scrollHeight);");

						// CRITICAL: Give page time to start lazy loading BEFORE checking for spinners
						try {
							Thread.sleep(5000); // 5 seconds - use Thread.sleep directly
						} catch (InterruptedException e) {
							Thread.currentThread().interrupt();
						}

						// Wait for spinner to disappear after scrolling
						PerformanceUtils.waitForSpinnersToDisappear(driver, 15);

						// EXTENDED wait for new profiles to fully render
						try {
							Thread.sleep(4000); // 4 seconds - use Thread.sleep directly
						} catch (InterruptedException e) {
							Thread.currentThread().interrupt();
						}
						PerformanceUtils.waitForPageReady(driver, 10);

						// Scroll back to top to ensure info messages are in viewport
						js.executeScript("window.scrollTo(0, 0);");

						// CRITICAL: Give page time to re-render after scroll to top
						try {
							Thread.sleep(3000); // 3 seconds - use Thread.sleep directly
						} catch (InterruptedException e) {
							Thread.currentThread().interrupt();
						}

						// CRITICAL: Wait for spinners again after scrolling to top (new spinners may
						// appear!)
						PerformanceUtils.waitForSpinnersToDisappear(driver, 15);

						// Final wait to ensure data is fully loaded and visible
						try {
							Thread.sleep(3000); // 3 seconds - use Thread.sleep directly
						} catch (InterruptedException e) {
							Thread.currentThread().interrupt();
						}
						PerformanceUtils.waitForPageReady(driver, 10);

						LOGGER.info("Scroll completed");
					}

					scrollAttempt++;
				} else {
					LOGGER.info("Reached maximum scroll attempts (" + maxScrollAttempts + "). Stopping search.");
					break;
				}
			}

			// If no match found after all batches
			if (!matchFound) {
				LOGGER.info(" No profiles with missing Department found (checked " + lastCheckedIndex
						+ " profiles across " + scrollAttempt + " batches)");
				PageObjectHelper.log(LOGGER, " No profiles found with missing Department");

				String skipMsg = "SKIPPING SCENARIO: No profiles found with missing Department (checked "
						+ lastCheckedIndex + " profiles)";
				PageObjectHelper.log(LOGGER, " " + skipMsg);
				throw new org.testng.SkipException(skipMsg);
			}

		} catch (org.testng.SkipException e) {
			throw e; // Re-throw SkipException
		} catch (Exception e) {
			PageObjectHelper.log(LOGGER, "- Failed to find suitable job profile: " + e.getMessage());
			throw new IOException("Failed to find suitable job profile", e);
		}
	}

	/**
	 * Extract job details from found profile in Job Mapping page (reverse scenario)
	 * Updated to handle corrected DOM structure: Grade/Dept from (n-1)th row,
	 * Function from nth row
	 */
	public void extract_job_details_from_found_profile_in_job_mapping_page() throws IOException {
		try {
			LOGGER.info("... Extracting job details using proven Features 27/28 logic");

			if (foundJobRow == null || foundProfile == null) {
				throw new IOException("No job row or profile found to extract details from");
			}

			// ===== FEATURE 28 PROVEN EXTRACTION LOGIC =====
			// foundJobRow = job data row (has Job Name, Grade, Department in td[2], td[3],
			// td[4])
			// foundProfile = info message row (has Function/Subfunction)

			String jobName = "";
			String jobCode = "";
			String grade = "";
			String department = "";
			String functionSubfunction = "";

			// Extract Job Name and Code from column 2 (Feature 28 approach)
			try {
				WebElement jobNameElement = foundJobRow
						.findElement(By.xpath(".//td[2]//div | .//td[position()=2]//div"));
				String jobNameCodeText = jobNameElement.getText().trim();

				// Parse job name and code from format: "Job Name - (JOB-CODE)"
				if (jobNameCodeText.contains(" - (") && jobNameCodeText.contains(")")) {
					int dashIndex = jobNameCodeText.lastIndexOf(" - (");
					jobName = cleanJobNameLocal(jobNameCodeText.substring(0, dashIndex).trim());
					jobCode = jobNameCodeText.substring(dashIndex + 4).replace(")", "").trim();
				} else {
					jobName = cleanJobNameLocal(jobNameCodeText);
				}
			} catch (Exception e) {
				LOGGER.warn("Could not extract job name/code using Feature 28 logic: " + e.getMessage());
			}

			// Extract Grade from column 3 (Feature 28 approach)
			try {
				WebElement gradeElement = foundJobRow.findElement(By.xpath(".//td[3]//div | .//td[position()=3]//div"));
				grade = normalizeFieldValue(gradeElement.getText().trim(), "Grade");
			} catch (Exception e) {
				LOGGER.warn("Could not extract grade using Feature 28 logic: " + e.getMessage());
			}

			// Extract Department from column 4 (Feature 28 approach)
			try {
				WebElement departmentElement = foundJobRow
						.findElement(By.xpath(".//td[4]//div | .//td[position()=4]//div"));
				department = normalizeFieldValue(departmentElement.getText().trim(), "Department");
			} catch (Exception e) {
				LOGGER.warn("Could not extract department using Feature 28 logic: " + e.getMessage());
			}

			// Extract Function/Subfunction from info message row (ULTRA-FAST JavaScript
			// approach)
			try {
				// ULTRA-OPTIMIZED: Use JavaScript to extract text directly without any XPath
				// searching
				JavascriptExecutor js = (JavascriptExecutor) driver;
				String rowText = (String) js.executeScript("return arguments[0].textContent;", foundProfile);
				rowText = rowText.trim();

				// Parse function from the text using known patterns
				String[] functionPatterns = { "Function / Sub-function:", "Function/Sub-function:", "Function:",
						"Sub-function:" };

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

							if (!functionPart.isEmpty()) {
								functionSubfunction = functionPart;
								break;
							}
						}
					}
				}

				// Normalize the value
				functionSubfunction = normalizeFieldValue(functionSubfunction, "Function/Subfunction");

			} catch (Exception e) {
				LOGGER.warn("Could not extract function/subfunction: " + e.getMessage());
			}

			LOGGER.info("Extracted Job Details: " + jobName + " | Code: " + jobCode + " | Grade: " + grade + " | Dept: "
					+ department + " | Func: " + functionSubfunction);

			// Store job details from Job Mapping page (source for reverse scenario)
			jobDetailsFromJobMappingPage.clear();
			jobDetailsFromJobMappingPage.put("jobName", jobName);
			jobDetailsFromJobMappingPage.put("jobCode", jobCode);
			jobDetailsFromJobMappingPage.put("grade", grade);
			jobDetailsFromJobMappingPage.put("department", department);
			jobDetailsFromJobMappingPage.put("functionSubfunction", functionSubfunction);

			// Update extracted job name for search
			extractedJobName.set(jobName);

			PageObjectHelper.log(LOGGER, "Extracted job details: " + jobName + " (" + jobCode + ")");

		} catch (Exception e) {
			PageObjectHelper.log(LOGGER, "- Failed to extract job details: " + e.getMessage());
			throw new IOException("Failed to extract job details from Job Mapping page", e);
		}
	}

	/**
	 * Search for the extracted job profile by name AND job code in Jobs Missing
	 * Data screen Note: Missing Data screen has NO search functionality, so we must
	 * traverse ALL jobs Enhanced to match both job name AND job code to handle
	 * multiple jobs with same name
	 */
	public void search_for_the_extracted_job_profile_by_name_in_jobs_missing_data_screen() throws IOException {
		try {
			PageObjectHelper.log(LOGGER, "Traversing all jobs in Missing Data screen (no search functionality)...");

			if (extractedJobName.get() == null || extractedJobName.get().isEmpty()) {
				throw new IOException("No job name available for search");
			}

			String searchTerm = extractJobNameForSearchLocal(extractedJobName.get());
			LOGGER.info("Using search term: " + searchTerm);

			// Get the expected job code from Job Mapping page for precise matching
			String expectedJobCode = jobDetailsFromJobMappingPage.get("jobCode");
			if (expectedJobCode != null && !expectedJobCode.isEmpty()) {
				PageObjectHelper.log(LOGGER, 
						"Looking for job with name '" + searchTerm + "' AND code '" + expectedJobCode + "'");
			} else {
				LOGGER.warn(
						"No job code available - will match by name only (may find wrong job if multiple jobs have same name)");
			}

			foundJobRow = null; // Reset for Missing Data screen search
			int totalJobsChecked = 0;
			boolean hasMoreJobs = true;

			// Since there's no search, we need to traverse ALL jobs in Missing Data screen
			while (hasMoreJobs && foundJobRow == null) {
				// Get currently loaded job rows
				List<WebElement> currentJobRows = driver
						.findElements(By.xpath("//tbody//tr[contains(@class,'border')]"));

				if (currentJobRows.isEmpty()) {
					throw new IOException("No job rows found in Jobs Missing Data screen");
				}

				LOGGER.info("Checking " + currentJobRows.size() + " job rows in current load (total checked: "
						+ totalJobsChecked + ")");

				// Search through current batch of jobs
				for (int i = 0; i < currentJobRows.size(); i++) {
					WebElement row = currentJobRows.get(i);
					totalJobsChecked++;

					// Scroll to current job row to show search progress (every 10th job)
					if (totalJobsChecked % 10 == 0) {
						js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", row);
						PerformanceUtils.safeSleep(driver, 200);
						LOGGER.info("Searching... checked " + totalJobsChecked + " jobs so far");
					}

					try {
						List<WebElement> cells = row.findElements(By.xpath(".//td"));
						if (cells.size() >= 1) {
							String jobNameInRow = cells.get(0).getText().trim();
							String cleanJobNameInRow = cleanJobNameLocal(jobNameInRow);

							// Extract job code from job name if present (format: "Job Name (CODE)")
							String jobCodeInRow = "";
							if (jobNameInRow.contains("(") && jobNameInRow.contains(")")) {
								int startParen = jobNameInRow.lastIndexOf("(");
								int endParen = jobNameInRow.lastIndexOf(")");
								if (startParen < endParen) {
									jobCodeInRow = jobNameInRow.substring(startParen + 1, endParen).trim();
								}
							}

							// Try multiple matching strategies for job name
							boolean nameMatches = false;

							// Strategy 1: Direct substring match
							if (cleanJobNameInRow.toLowerCase().contains(searchTerm.toLowerCase())) {
								nameMatches = true;
							}
							// Strategy 2: Reverse substring match
							else if (searchTerm.toLowerCase().contains(cleanJobNameInRow.toLowerCase())) {
								nameMatches = true;
							}
							// Strategy 3: Remove common suffixes/prefixes and try again
							else {
								String simplifiedExtracted = searchTerm.replaceAll("\\s*\\([^)]*\\)\\s*", "").trim();
								String simplifiedRow = cleanJobNameInRow.replaceAll("\\s*\\([^)]*\\)\\s*", "").trim();

								if (simplifiedExtracted.toLowerCase().contains(simplifiedRow.toLowerCase())
										|| simplifiedRow.toLowerCase().contains(simplifiedExtracted.toLowerCase())) {
									nameMatches = true;
								}
							}

							// Enhanced validation: Check both name AND job code
							boolean isExactMatch = false;
							if (nameMatches) {
								if (expectedJobCode != null && !expectedJobCode.isEmpty() && !jobCodeInRow.isEmpty()) {
									// Both job codes available - must match exactly
									if (expectedJobCode.equals(jobCodeInRow)) {
										isExactMatch = true;
										LOGGER.info("PERFECT MATCH found at position " + totalJobsChecked + ": "
												+ cleanJobNameInRow + " (Code: " + jobCodeInRow + ")");
									} else {
										LOGGER.info("Name matches but code differs at position " + totalJobsChecked
												+ ": " + cleanJobNameInRow + " (Expected: " + expectedJobCode
												+ ", Found: " + jobCodeInRow + ") - SKIPPING");
										continue; // Skip this job - wrong code
									}
								} else {
									// Job code not available or not found - fall back to name-only matching
									isExactMatch = true;
									LOGGER.warn("Job code comparison not possible - using name-only match: "
											+ cleanJobNameInRow);
								}
							}

							if (isExactMatch) {
								foundJobRow = row;
								LOGGER.info("FOUND job at position " + totalJobsChecked + ": " + cleanJobNameInRow
										+ (jobCodeInRow.isEmpty() ? "" : " (Code: " + jobCodeInRow + ")"));

								// Scroll to found row for visibility
								js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});",
										row);
								PerformanceUtils.safeSleep(driver, 500);

								PageObjectHelper.log(LOGGER, "FOUND job in Missing Data screen: "
										+ cleanJobNameInRow + " (position " + totalJobsChecked + ")"
										+ (jobCodeInRow.isEmpty() ? "" : " - Code: " + jobCodeInRow));
								return; // Found the job, exit
							}
						}
					} catch (Exception e) {
						continue; // Try next row
					}
				}

				// Check if there are more jobs to load (scroll to bottom to trigger loading)
				int currentJobCount = currentJobRows.size();
				js.executeScript("window.scrollTo(0, document.documentElement.scrollHeight);"); // Scroll DOWN
																								// (headless-compatible)
				PerformanceUtils.safeSleep(driver, 2000); // Wait for potential lazy loading

				List<WebElement> newJobRows = driver.findElements(By.xpath("//tbody//tr[contains(@class,'border')]"));
				int newJobCount = newJobRows.size();

				if (newJobCount <= currentJobCount) {
					// No more jobs loaded - we've reached the end
					hasMoreJobs = false;
					LOGGER.info("Reached end of Missing Data screen. Total jobs checked: " + totalJobsChecked);
				} else {
					LOGGER.info("Loaded " + (newJobCount - currentJobCount) + " more jobs. Continuing search...");
				}
			}

			// Job not found - Data consistency issue or wrong job code
			if (foundJobRow == null) {
				String errorMsg;
				if (expectedJobCode != null && !expectedJobCode.isEmpty()) {
					errorMsg = "Job '" + searchTerm + "' with code '" + expectedJobCode
							+ "' found in Job Mapping but NOT FOUND in Missing Data screen (checked " + totalJobsChecked
							+ " jobs)";
					LOGGER.error(
							"BUG: Data consistency issue between screens OR multiple jobs with same name but different codes");
										PageObjectHelper.log(LOGGER, "BUG: Job name '" + searchTerm + "' with code '"
							+ expectedJobCode + "' not found in Missing Data screen");
				} else {
					errorMsg = "Job '" + searchTerm
							+ "' found in Job Mapping but NOT FOUND in Missing Data screen (checked " + totalJobsChecked
							+ " jobs)";
										PageObjectHelper.log(LOGGER, "BUG: " + errorMsg);
				}

				throw new IOException("BUG: Data consistency issue - " + errorMsg);
			}

		} catch (Exception e) {
			PageObjectHelper.log(LOGGER, " REVERSE SCENARIO FAILED: " + e.getMessage());

			// Force close Missing Data screen to ensure next scenario can run
			LOGGER.warn("Force closing Missing Data screen to ensure next scenario can start properly");
			PageObjectHelper.log(LOGGER, " Force closing Missing Data screen for next scenario");
			force_close_missing_data_screen_for_next_scenario();

			// Set flag to indicate scenario failed but don't stop execution
			throw new IOException("REVERSE_SCENARIO_FAILED: " + e.getMessage());
		}
	}

	/**
	 * Verify job profile is found and displayed in Jobs Missing Data screen search
	 * results
	 */
	public void verify_job_profile_is_found_and_displayed_in_jobs_missing_data_screen_search_results()
			throws IOException {
		try {
			LOGGER.info("Verifying job profile is found and displayed in Missing Data screen");

			if (foundJobRow == null) {
				String errorMsg = " BUG: No job row found in Missing Data screen - job should exist since it was found with missing data in Job Mapping screen";
				PageObjectHelper.log(LOGGER, " " + errorMsg);
				throw new IOException(errorMsg);
			}

			// Verify the found row is visible and contains expected data
			if (foundJobRow.isDisplayed()) {
				PageObjectHelper.log(LOGGER, "... Job profile verified in Missing Data screen");
			} else {
				String errorMsg = " BUG: Found job row exists but is not visible - display issue in Missing Data screen";
				PageObjectHelper.log(LOGGER, " " + errorMsg);
				throw new IOException(errorMsg);
			}

		} catch (Exception e) {
			PageObjectHelper.log(LOGGER, "Failed to verify job profile: " + e.getMessage());

			// Force close Missing Data screen to ensure next scenario can run
			LOGGER.warn("Force closing Missing Data screen due to verification failure");
			force_close_missing_data_screen_for_next_scenario();

			throw new IOException("BUG: Failed to verify job profile in Missing Data screen", e);
		}
	}

	/**
	 * Extract job details from found profile in Jobs Missing Data screen Used by
	 * both Forward and Reverse scenarios
	 */
	public void extract_job_details_from_found_profile_in_jobs_missing_data_screen() throws IOException {
		try {
			LOGGER.info("Extracting job details from Missing Data screen");

			if (foundJobRow == null) {
				throw new IOException("No job row found to extract details from");
			}

			// Extract details from the found job row in Missing Data screen
			List<WebElement> cells = foundJobRow.findElements(By.xpath(".//td"));

			if (cells.size() >= 4) {
				String jobName = cleanJobNameLocal(cells.get(0).getText().trim());
				String grade = normalizeFieldValue(cells.get(1).getText().trim(), "Grade");
				String department = normalizeFieldValue(cells.get(2).getText().trim(), "Department");
				String functionSubfunction = normalizeFieldValue(cells.get(3).getText().trim(), "Function");

				// Store job details from Missing Data screen (target for reverse scenario
				// comparison)
				jobDetailsFromMissingDataScreen.clear();
				jobDetailsFromMissingDataScreen.put("jobName", jobName);
				jobDetailsFromMissingDataScreen.put("grade", grade);
				jobDetailsFromMissingDataScreen.put("department", department);
				jobDetailsFromMissingDataScreen.put("functionSubfunction", functionSubfunction);

				PageObjectHelper.log(LOGGER, "Successfully extracted job details from Missing Data screen");

			} else {
				throw new IOException("Insufficient cells found for job detail extraction in Missing Data screen");
			}

		} catch (Exception e) {
			PageObjectHelper.log(LOGGER, "- Failed to extract job details: " + e.getMessage());

			// Force close Missing Data screen to ensure next scenario can run
			LOGGER.warn("Force closing Missing Data screen due to extraction failure");
			force_close_missing_data_screen_for_next_scenario();

			throw new IOException("Failed to extract job details from Missing Data screen", e);
		}
	}

	/**
	 * Verify all job details match between Job Mapping page and Jobs Missing Data
	 * screen (reverse scenario) This is the reverse comparison - Job Mapping page
	 * data is compared against Missing Data screen data
	 */
	public void verify_all_job_details_match_between_job_mapping_page_and_jobs_missing_data_screen()
			throws IOException {
		try {
			LOGGER.debug("Comparing job details between screens (reverse scenario)");

			// Get details from both screens (reverse order)
			String nameFromJobMapping = jobDetailsFromJobMappingPage.get("jobName") != null
					? jobDetailsFromJobMappingPage.get("jobName")
					: "";
			String nameFromMissingData = jobDetailsFromMissingDataScreen.get("jobName") != null
					? jobDetailsFromMissingDataScreen.get("jobName")
					: "";
			String gradeFromJobMapping = jobDetailsFromJobMappingPage.get("grade") != null
					? jobDetailsFromJobMappingPage.get("grade")
					: "";
			String gradeFromMissingData = jobDetailsFromMissingDataScreen.get("grade") != null
					? jobDetailsFromMissingDataScreen.get("grade")
					: "";
			String deptFromJobMapping = jobDetailsFromJobMappingPage.get("department") != null
					? jobDetailsFromJobMappingPage.get("department")
					: "";
			String deptFromMissingData = jobDetailsFromMissingDataScreen.get("department") != null
					? jobDetailsFromMissingDataScreen.get("department")
					: "";
			String funcFromJobMapping = jobDetailsFromJobMappingPage.get("functionSubfunction") != null
					? jobDetailsFromJobMappingPage.get("functionSubfunction")
					: "";
			String funcFromMissingData = jobDetailsFromMissingDataScreen.get("functionSubfunction") != null
					? jobDetailsFromMissingDataScreen.get("functionSubfunction")
					: "";

			// Verify job details match between screens
			// Verify job details match between screens (reverse scenario)

			// Verify job name matches (allowing for format differences)
			if (!nameFromJobMapping.isEmpty() && !nameFromMissingData.isEmpty()) {
				String searchNameJobMapping = extractJobNameForSearchLocal(nameFromJobMapping);
				String searchNameMissingData = extractJobNameForSearchLocal(nameFromMissingData);
				boolean jobNameMatches = searchNameJobMapping.toLowerCase()
						.contains(searchNameMissingData.toLowerCase())
						|| searchNameMissingData.toLowerCase().contains(searchNameJobMapping.toLowerCase());
				if (!jobNameMatches) {
					String errorMsg = "Job Name mismatch: JobMapping='" + nameFromJobMapping + "' vs MissingData='"
							+ nameFromMissingData + "'";
					LOGGER.error(errorMsg);
					Assert.fail(errorMsg);
				}
			} else {
				LOGGER.warn("Job Name comparison skipped - one or both values are empty");
			}

			// Compare all fields using unified N/A "-" mapping logic (reverse order)
			// Compare Grade
			if (!compareFieldValues("Grade", gradeFromJobMapping, gradeFromMissingData)) {
				Assert.fail("Grade comparison failed - see logs for details");
			}

			// Compare Department
			if (!compareFieldValues("Department", deptFromJobMapping, deptFromMissingData)) {
				Assert.fail("Department comparison failed - see logs for details");
			}

			// Compare Function/Sub-function
			if (!compareFieldValues("Function/Sub-function", funcFromJobMapping, funcFromMissingData)) {
				Assert.fail("Function/Sub-function comparison failed - see logs for details");
			}

			PageObjectHelper.log(LOGGER, "Successfully verified job details match between screens (reverse flow)");

		} catch (Exception e) {
			PageObjectHelper.log(LOGGER, "- Failed to verify job details match: " + e.getMessage());
			throw new IOException("Failed to verify job details match (reverse scenario)", e);
		}
	}

	/**
	 * Helper method to clean job name and fix parsing issues
	 */
	private String cleanJobNameLocal(String rawJobName) {
		if (rawJobName == null || rawJobName.isEmpty()) {
			return "";
		}

		String cleaned = rawJobName.trim();

		// Fix common parsing issues:
		// 1. Handle multiple closing parentheses like "Name (Code))" -> "Name (Code)"
		cleaned = cleaned.replaceAll("\\)\\)+", ")");

		// 2. Remove trailing ) that might be left hanging without opening parenthesis
		if (cleaned.endsWith(")") && !cleaned.contains("(")) {
			cleaned = cleaned.substring(0, cleaned.length() - 1).trim();
		}

		// 3. Fix specific case where job code parentheses are duplicated
		// Pattern: "+/-- (3000340))" -> "+/-- (3000340)"
		// Use simple replacement pattern for job code with extra parentheses
		cleaned = cleaned.replaceAll("(\\([^)]*\\))\\)+", "$1");

		// 4. Clean up any remaining double parentheses
		while (cleaned.contains("))")) {
			cleaned = cleaned.replace("))", ")");
		}

		// 5. Remove extra whitespace
		cleaned = cleaned.replaceAll("\\s+", " ").trim();

		return cleaned;
	}

	/**
	 * Helper method to extract job name for search (removes job codes and extra
	 * formatting)
	 */
	private String extractJobNameForSearchLocal(String fullJobName) {
		if (fullJobName == null || fullJobName.isEmpty()) {
			return "";
		}

		// First clean the job name to fix any parsing issues
		String cleaned = cleanJobNameLocal(fullJobName);

		// Remove job codes in parentheses and extra whitespace
		String cleanName = cleaned.replaceAll("\\([^)]*\\)", "").trim();

		// Remove any remaining stray parentheses or extra characters
		cleanName = cleanName.replaceAll("[()]+", "").trim();

		// Take first few words for search - improved to handle job titles better
		String[] words = cleanName.split("\\s+");
		if (words.length > 3) {
			// For longer job titles, take first 3 words (e.g., "Video Game Tester")
			String searchTerm = words[0] + " " + words[1] + " " + words[2];
			return searchTerm;
		} else if (words.length > 2) {
			// For 3 words, take first 3 words (e.g., "Video Game Tester")
			String searchTerm = words[0] + " " + words[1] + " " + words[2];
			return searchTerm;
		} else {
			// For 2 or fewer words, return full name
			return cleanName;
		}
	}
}
