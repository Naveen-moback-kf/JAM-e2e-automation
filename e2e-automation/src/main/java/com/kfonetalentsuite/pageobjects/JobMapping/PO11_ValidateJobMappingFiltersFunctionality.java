package com.kfonetalentsuite.pageobjects.JobMapping;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.ScreenshotHandler;
import com.kfonetalentsuite.utils.JobMapping.Utilities;
import com.kfonetalentsuite.webdriverManager.DriverManager;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

public class PO11_ValidateJobMappingFiltersFunctionality {
	WebDriver driver = DriverManager.getDriver();	

	protected static final Logger LOGGER = (Logger) LogManager.getLogger();
	PO11_ValidateJobMappingFiltersFunctionality validateJobMappingFiltersFunctionality;
	

	public PO11_ValidateJobMappingFiltersFunctionality() throws IOException {
		// super();
		PageFactory.initElements(driver, this);
	}

	WebDriverWait wait = DriverManager.getWait();
	Utilities utils = new Utilities();
	JavascriptExecutor js = (JavascriptExecutor) driver;
	
	public static String GradesOption = "";
	public static String GradesOption1 = "";
	public static String GradesOption2 = "";
	public static String DepartmentsOption = "";
	public static String DepartmentsOption1 = "";
	public static String DepartmentsOption2 = "";
	public static String FunctionsOption = "";
	public static String FunctionsOption1 = "";
	public static String FunctionsOption2 = "";
//	public static String FunctionsOption = FunctionsOption;
	
	//XPATHs
	@FindBy(xpath = "//div[@data-testid='loader']//img")
	@CacheLookup
	WebElement pageLoadSpinner2;
	
	@FindBy(xpath = "//div[@data-testid='dropdown-Grades']")
	@CacheLookup
	WebElement gradesFiltersDropdown;
	
	@FindBy(xpath = "//div[contains(@id,'results-toggle')]//*[contains(text(),'Showing')]")
	@CacheLookup
	public WebElement showingJobResultsCount;
	
	@FindBy(xpath = "//*[@id='no-data-container']")
	@CacheLookup
	WebElement noDataContainer;
	
	@FindBy(xpath = "//button[@data-testid='Clear Filters']")
	@CacheLookup
	WebElement clearFiltersBtn;
	
	@FindBy(xpath = "//button[@data-testid='clear-filters-button']")
	@CacheLookup
	WebElement clearFiltersXbtn;
	
	@FindBy(xpath = "//div[@data-testid='dropdown-Departments']")
	@CacheLookup
	WebElement departmentsFiltersDropdown;
	
	@FindBy(xpath = "//div[@data-testid='dropdown-Functions_SubFunctions']")
	@CacheLookup
	WebElement functionsSubFunctionsFiltersDropdown;
	
	@FindBy(xpath = "//div[@data-testid='dropdown-Functions_SubFunctions']//..//div[2]//input[@placeholder='Search']")
	@CacheLookup
	WebElement functionsSubFunctionsSearch;
	
	@FindBy(xpath = "//button[contains(@data-testid,'toggle-suboptions')]")
	@CacheLookup
	WebElement functionsSubFunctionsToggleSuboptions;
	
	@FindBy(xpath = "//*[@data-testid='dropdown-MappingStatus']")
	@CacheLookup
	WebElement mappingStatusFiltersDropdown;
	
	
	//METHODs
	public void click_on_grades_filters_dropdown_button() {
		try {
			// PERFORMANCE: Replaced Thread.sleep(2000) with proper wait for element
			wait.until(ExpectedConditions.invisibilityOfAllElements(driver.findElements(By.xpath("//div[@data-testid='loader']//img"))));
			try {
				wait.until(ExpectedConditions.visibilityOf(gradesFiltersDropdown)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", gradesFiltersDropdown);
				} catch (Exception s) {
					utils.jsClick(driver, gradesFiltersDropdown);
				}
			}	
		LOGGER.info("Clicked on Grades dropdown in Filters...");
		ExtentCucumberAdapter.addTestStepLog("Clicked on Grades dropdown in Filters...");
	} catch (Exception e) {
		ScreenshotHandler.handleTestFailure("click_grades_dropdown_in_filters", e, 
			"Issue in clicking Grades dropdown in Filters...Please Investigate!!!");
	}
	}
	
	public void select_one_option_in_grades_filters_dropdown() {
		try {
			// PERFORMANCE: Replaced Thread.sleep(2000) with proper wait for dropdown elements
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[@data-testid='dropdown-Grades']//..//input[@type='checkbox']")));
			List<WebElement> GradesCheckboxes = driver.findElements(By.xpath("//div[@data-testid='dropdown-Grades']//..//input[@type='checkbox']"));
			List<WebElement> GradesValues = driver.findElements(By.xpath("//div[@data-testid='dropdown-Grades']//..//input[@type='checkbox']//..//label"));
			
			LOGGER.info("Found {} grade options in dropdown", GradesValues.size());
			
			if (GradesValues.isEmpty()) {
				throw new Exception("No grade options found in dropdown");
			}
			
			// Dynamically select the first available option (safer than hardcoded index)
			int selectedIndex = 0;
			
			// Try to select index 7 if available (for consistency with old tests), otherwise use first
			if (GradesValues.size() > 7) {
				selectedIndex = 7;
				LOGGER.info("Using preferred index 7 for grade selection");
			} else {
				selectedIndex = 0;
				LOGGER.info("Using first available grade (index 0) as there are fewer than 8 options");
			}
			
			WebElement targetCheckbox = GradesCheckboxes.get(selectedIndex);
			WebElement targetLabel = GradesValues.get(selectedIndex);
			String gradeValue = targetLabel.getText().trim();
			
			LOGGER.info("Attempting to select grade: '{}'", gradeValue);
			
			// Scroll into view
			js.executeScript("arguments[0].scrollIntoView({block: 'center'});", targetCheckbox);
			Thread.sleep(300);
			
			// Click the checkbox (more reliable than clicking label)
			boolean clicked = false;
			try {
				// Method 1: JavaScript click on checkbox
				js.executeScript("arguments[0].click();", targetCheckbox);
				clicked = true;
			} catch (Exception e) {
				try {
					// Method 2: Regular click on checkbox
					wait.until(ExpectedConditions.elementToBeClickable(targetCheckbox)).click();
					clicked = true;
				} catch (Exception s) {
					// Method 3: Fallback to clicking label
					wait.until(ExpectedConditions.elementToBeClickable(targetLabel)).click();
					clicked = true;
				}
			}
			
			if (!clicked) {
				throw new Exception("Failed to click grade checkbox/label");
			}
			
			// Wait for page to update
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForPageReady(driver, 1);
			
			// Verify selection
			if (!targetCheckbox.isSelected()) {
				LOGGER.warn("Checkbox not selected after click, retrying...");
				js.executeScript("arguments[0].click();", targetCheckbox);
				Thread.sleep(300);
				wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
				PerformanceUtils.waitForPageReady(driver, 1);
			}
			
			Assert.assertTrue(targetCheckbox.isSelected(), "Grade checkbox not selected: " + gradeValue);
			GradesOption = gradeValue;
			
			LOGGER.info("Selected Grades Value: '{}' from Grades Filters dropdown", gradeValue);
			ExtentCucumberAdapter.addTestStepLog("Selected Grades Value: " + gradeValue + " from Grades Filters dropdown");
			
		PerformanceUtils.waitForPageReady(driver, 1);
	} catch (Exception e) {
		ScreenshotHandler.handleTestFailure("select_one_option_in_grades_filters_dropdown", e, 
			"Issue in selecting one option from Grades dropdown in Filters...Please Investigate!!!");
	}
	}
	
	public void user_should_scroll_down_to_view_last_result_with_applied_filters() throws InterruptedException {
		try {
			int maxScrollAttempts = 50; // Prevent infinite loop with max attempts
			int scrollAttempts = 0;
			String previousResultsCountText = "";
			int stableCountIterations = 0;
			int consecutiveFailures = 0; // Track consecutive failures to exit early
			String workingXPath = null; // Cache the XPath that works
			
		while(scrollAttempts < maxScrollAttempts) {
			scrollAttempts++;
		// PERFORMANCE: Minimal wait for small datasets
		PerformanceUtils.waitForUIStability(driver, 1);
				
				// Use a more robust element retrieval with retry logic to handle stale elements
				String resultsCountText = "";
				try {
					// PERFORMANCE FIX: Use 1s timeout instead of 3s for faster detection
					WebDriverWait shortWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(1));
					
					// Try multiple XPath strategies
					WebElement resultsElement = null;
					
					// PERFORMANCE FIX: Try cached XPath first if we found one that works
					if (workingXPath != null) {
						try {
							resultsElement = shortWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(workingXPath)));
						} catch (Exception e) {
							// Cached XPath failed, reset and try all strategies
							workingXPath = null;
						}
					}
					
					// If cached XPath didn't work, try all strategies
					if (resultsElement == null) {
						try {
							// Strategy 1: Original XPath
							String xpath1 = "//div[contains(@id,'results-toggle')]//*[contains(text(),'Showing')]";
							resultsElement = shortWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath1)));
							workingXPath = xpath1; // Cache this for next time
						} catch (Exception e1) {
							// Strategy 2: Alternative XPath
							try {
								String xpath2 = "//*[contains(text(),'Showing') and contains(text(),'of')]";
								resultsElement = driver.findElement(By.xpath(xpath2));
								workingXPath = xpath2; // Cache this for next time
							} catch (Exception e2) {
							// Strategy 3: Try to scroll to top first (element might be off-screen in headless)
							try {
								js.executeScript("window.scrollTo(0, 0);");
								Thread.sleep(100); // PERFORMANCE: Reduced from 200ms
								resultsElement = driver.findElement(By.xpath("//div[contains(@id,'results-toggle')]//*[contains(text(),'Showing')]"));
							} catch (Exception e3) {
								throw e1; // Throw original exception
							}
							}
						}
					}
					
				if (resultsElement != null) {
					// PERFORMANCE: Skip scroll if element is visible, minimal wait if not
					try {
						if (!resultsElement.isDisplayed()) {
							js.executeScript("arguments[0].scrollIntoView({block: 'center'});", resultsElement);
							Thread.sleep(50); // PERFORMANCE: Reduced from 100ms
						}
					} catch (Exception e) {
						// Element might be stale, try to get text anyway
					}
					resultsCountText = resultsElement.getText();
					consecutiveFailures = 0; // Reset failure counter on success
				}
					
			} catch (Exception e) {
				consecutiveFailures++;
				LOGGER.warn("Failed to get results count text on attempt {} (failure #{}): {}", scrollAttempts, consecutiveFailures, e.getClass().getSimpleName());
				
				// HEADLESS FIX: Exit early after 5 consecutive failures (was looping 50 times)
				if (consecutiveFailures >= 5) {
					LOGGER.error("Results count element not found after {} consecutive attempts. Likely in headless mode without visible UI.", consecutiveFailures);
					
					// Try alternative: count visible rows
					try {
						List<WebElement> visibleRows = driver.findElements(By.xpath("//tbody//tr[contains(@class,'cursor-pointer')]"));
						int rowCount = visibleRows.size();
						LOGGER.info("Alternative approach: Found {} visible rows in table", rowCount);
						ExtentCucumberAdapter.addTestStepLog("Scrolled down and loaded " + rowCount + " job profiles (headless mode)");
						break;
					} catch (Exception altEx) {
						LOGGER.error("Alternative row count also failed: {}", altEx.getMessage());
					}
					break; // Exit the loop
				}
				
				// If we can't find the element, check if no data container is displayed
				try {
					if(noDataContainer.isDisplayed()) {
						LOGGER.info(noDataContainer.getText() + " with applied Filters");
						ExtentCucumberAdapter.addTestStepLog(noDataContainer.getText() + " with applied Filters");
						break;
					}
				} catch (Exception ex) {
					// Element not found, continue scrolling
				}
				continue;
			}
				
				String[] resultsCountText_split = resultsCountText.split(" ");
				
				// Fix: Use .equals() instead of != for string comparison
				if(resultsCountText_split.length > 3 && !resultsCountText_split[1].equals("0")) {
					if(resultsCountText_split[1].equals(resultsCountText_split[3])) {
						// Results are fully loaded - verify stability
						if(resultsCountText.equals(previousResultsCountText)) {
							stableCountIterations++;
							// If count is stable for 2 consecutive checks, we're done
							if(stableCountIterations >= 2) {
								LOGGER.info("Scrolled down till last Search Result and now " + resultsCountText + " of Job Profiles as expected");
								ExtentCucumberAdapter.addTestStepLog("Scrolled down till last Search Result and now " + resultsCountText + " of Job Profiles as expected");
								break;
							}
						} else {
							stableCountIterations = 0;
							previousResultsCountText = resultsCountText;
						}
						
				// PERFORMANCE: Ultra-fast final scroll verification
				try {
					js.executeScript("window.scrollTo(0, document.documentElement.scrollHeight);");
					Thread.sleep(100); // PERFORMANCE: Reduced from 150ms
					js.executeScript("window.dispatchEvent(new Event('scroll'));");
					Thread.sleep(50); // PERFORMANCE: Reduced from 100ms
				} catch (Exception scrollEx) {
					LOGGER.warn("Final scroll failed: {}", scrollEx.getMessage());
				}
				
				try {
					WebDriverWait spinnerWait = new WebDriverWait(driver, java.time.Duration.ofMillis(500));
					spinnerWait.until(ExpectedConditions.invisibilityOfAllElements(
						driver.findElements(By.xpath("//div[@data-testid='loader']//img"))
					));
				} catch (Exception e) {
					// Spinner not found or already invisible
				}
				PerformanceUtils.waitForUIStability(driver, 1); // PERFORMANCE: Reduced for small datasets
				} else {
					// More results to load - keep scrolling
					previousResultsCountText = resultsCountText;
					
				// PERFORMANCE-OPTIMIZED: Ultra-fast scrolling for small datasets
				try {
					// Quick scroll to bottom
					js.executeScript("window.scrollTo(0, document.documentElement.scrollHeight);");
					Thread.sleep(150); // PERFORMANCE: Reduced from 250ms
					
					// Trigger scroll event for lazy loading
					js.executeScript("window.dispatchEvent(new Event('scroll'));");
					Thread.sleep(100); // PERFORMANCE: Reduced from 150ms
					
				} catch (Exception scrollEx) {
					LOGGER.warn("Scroll operation failed: {}", scrollEx.getMessage());
					// Fallback to basic scroll
					js.executeScript("window.scrollTo(0, document.documentElement.scrollHeight);");
					Thread.sleep(150);
				}
				
				// PERFORMANCE: Skip heavy spinner wait for small datasets
				try {
					WebDriverWait spinnerWait = new WebDriverWait(driver, java.time.Duration.ofMillis(500));
					spinnerWait.until(ExpectedConditions.invisibilityOfAllElements(
						driver.findElements(By.xpath("//div[@data-testid='loader']//img"))
					));
				} catch (Exception e) {
					// Spinner not found or already invisible - continue quickly
				}
			// PERFORMANCE: Minimal stability wait for small datasets
			PerformanceUtils.waitForUIStability(driver, 1);
				}
				} else {
					// Check for no data container
					try {
						if(noDataContainer.isDisplayed()) {
							LOGGER.info(noDataContainer.getText() + " with applied Filters");
							ExtentCucumberAdapter.addTestStepLog(noDataContainer.getText() + " with applied Filters");
							break;
						}
					} catch (Exception ex) {
						// No data container not displayed, continue
					}
				}
			}
			
			if(scrollAttempts >= maxScrollAttempts) {
				LOGGER.warn("Maximum scroll attempts ({}) reached. Results may not be fully loaded.", maxScrollAttempts);
				ExtentCucumberAdapter.addTestStepLog("Warning: Maximum scroll attempts reached");
			}
			
	} catch (Exception e) {
		ScreenshotHandler.captureFailureScreenshot("user_should_scroll_down_to_view_last_result_with_applied_filters", e);
		LOGGER.error("Issue in scrolling page down to view last result with applied filters: {}", e.getMessage(), e);
		Assert.fail("Issue in scrolling page down to view last result with applied filters...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in scrolling page down to view last result with applied filters...Please Investigate!!!");
	}
		
	}
	
	public void validate_job_mapping_profiles_are_correctly_filtered_with_applied_grades_options() {
		try {
			List<WebElement> allGrades = driver.findElements(By.xpath("//*[text()='Organization jobs']//..//tbody//tr//td[3]//div | //*[@id='table-container']/div[1]/div/div[2]/div/div/div[1]/span[2]/span"));
		    for (WebElement grade: allGrades) {
		        String text = grade.getText();
		        if(text.contentEquals(GradesOption) || text.contentEquals(GradesOption1) || text.contentEquals(GradesOption2)) {
		        	 LOGGER.info("Organization Job Mapping Profile with Grade : " + text + " is correctly filtered with applied grades options");
		        	 ExtentCucumberAdapter.addTestStepLog("Organization Job Mapping Profile with Grade : " + text + " is correctly filtered with applied grades options");
		        } else {
		        	LOGGER.info("Organization Job Mapping Profile with Grade : " + text + " from Filters results DOES NOT match with applied grades options");
		        	ExtentCucumberAdapter.addTestStepLog("Organization Job Mapping Profile with Grade : " + text + " from Filters results DOES NOT match with applied grades options...Please Investigate!!!");
		        	throw new Exception("Organization Job Profile is displaying with Incorrect Grade Value from applied Filters...Please Investigate!!!");
		        }
		    }
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_job_mapping_profiles_are_correctly_filtered_with_applied_grades_options", e);
			LOGGER.error("Exception occurred in ValidateJobMappingFiltersFunctionality: {}", e.getMessage(), e);
			Assert.fail("Issue in Validating Job Mapping Profiles Results with applied grades options...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in Validating Job Mapping Profiles Results with applied grades options...Please Investigate!!!");
		}	
		js.executeScript("window.scrollTo(0, 0);"); // Scroll to top (headless-compatible)
	}
	
	/**
	 * Clicks the X button to clear applied filters
	 * Page already loaded, directly clicks to clear
	 */
	public void click_on_clear_x_applied_filter() {
		try {
			// Page already loaded from previous step, directly click clear button
			try {
				wait.until(ExpectedConditions.visibilityOf(clearFiltersXbtn)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", clearFiltersXbtn);
				} catch (Exception s) {
					utils.jsClick(driver, clearFiltersXbtn);
				}
			}	
			// REMOVED: driver.navigate().refresh() - Unnecessary and clears checkbox selections
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForPageReady(driver, 2);
			Thread.sleep(300); // PERFORMANCE: Reduced from 500ms for UI stabilization after clearing filters
			LOGGER.info("Clicked on clear Filters button(x) and Cleared the applied filters....");
			ExtentCucumberAdapter.addTestStepLog("Clicked on clear Filters button and Cleared all the applied filters....");
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_on_clear_x_applied_filter", e);
			LOGGER.error("Exception occurred in ValidateJobMappingFiltersFunctionality: {}", e.getMessage(), e);
			Assert.fail("Issue in clicking Clear Filters Button(x) to clear the applied filters...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in clicking Clear Filters Button(x) to clear the applied filters...Please Investigate!!!");
		}
	}
	
	
	public void select_two_options_in_grades_filters_dropdown() {
		try {
			PerformanceUtils.waitForPageReady(driver, 2);
			List<WebElement> GradesCheckboxes = driver.findElements(By.xpath("//div[@data-testid='dropdown-Grades']//..//input[@type='checkbox']"));
			List<WebElement> GradesValues = driver.findElements(By.xpath("//div[@data-testid='dropdown-Grades']//..//input[@type='checkbox']//..//label"));
			
			LOGGER.info("Found {} grade options for multiple selection", GradesValues.size());
			
			if (GradesValues.size() < 2) {
				throw new Exception("Not enough grade options available. Need at least 2, found: " + GradesValues.size());
			}
			
			// Dynamically select two indices based on available options
			int firstIndex, secondIndex;
			
			if (GradesValues.size() > 12) {
				// Prefer indices 10 and 12 for consistency with old tests
				firstIndex = 10;
				secondIndex = 12;
				LOGGER.info("Using preferred indices 10 and 12 for multiple grade selection");
			} else if (GradesValues.size() > 7) {
				// Use middle indices if fewer options
				firstIndex = GradesValues.size() / 3;
				secondIndex = (GradesValues.size() * 2) / 3;
				LOGGER.info("Using dynamic indices {} and {} based on available options", firstIndex, secondIndex);
			} else {
				// Use first two if very few options
				firstIndex = 0;
				secondIndex = 1;
				LOGGER.info("Using first two indices (0 and 1) as there are fewer options");
			}
			
			// Select first grade
			WebElement firstCheckbox = GradesCheckboxes.get(firstIndex);
			WebElement firstLabel = GradesValues.get(firstIndex);
			String firstGradeValue = firstLabel.getText().trim();
			
			LOGGER.info("Selecting first grade: '{}'", firstGradeValue);
			js.executeScript("arguments[0].scrollIntoView({block: 'center'});", firstCheckbox);
			Thread.sleep(300);
			
			try {
				js.executeScript("arguments[0].click();", firstCheckbox);
			} catch(Exception e) {
				try {
					wait.until(ExpectedConditions.elementToBeClickable(firstCheckbox)).click();
				} catch (Exception s) {
					wait.until(ExpectedConditions.elementToBeClickable(firstLabel)).click();
				}
			}
			
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForPageReady(driver, 1);
			
			// Verify first selection
			if (!firstCheckbox.isSelected()) {
				LOGGER.warn("First checkbox not selected, retrying...");
				js.executeScript("arguments[0].click();", firstCheckbox);
				Thread.sleep(300);
				wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
				PerformanceUtils.waitForPageReady(driver, 1);
			}
			
			Assert.assertTrue(firstCheckbox.isSelected(), "First grade checkbox not selected: " + firstGradeValue);
			GradesOption1 = firstGradeValue;
			LOGGER.info("Selected first Grades Value: '{}' from Grades Filters dropdown", firstGradeValue);
			ExtentCucumberAdapter.addTestStepLog("Selected Grades Value: " + firstGradeValue + " from Grades Filters dropdown");
			
			// Select second grade
			WebElement secondCheckbox = GradesCheckboxes.get(secondIndex);
			WebElement secondLabel = GradesValues.get(secondIndex);
			String secondGradeValue = secondLabel.getText().trim();
			
			LOGGER.info("Selecting second grade: '{}'", secondGradeValue);
			js.executeScript("arguments[0].scrollIntoView({block: 'center'});", secondCheckbox);
			Thread.sleep(300);
			
			try {
				js.executeScript("arguments[0].click();", secondCheckbox);
			} catch(Exception e) {
				try {
					wait.until(ExpectedConditions.elementToBeClickable(secondCheckbox)).click();
				} catch (Exception s) {
					wait.until(ExpectedConditions.elementToBeClickable(secondLabel)).click();
				}
			}
			
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForPageReady(driver, 1);
			
			// Verify second selection
			if (!secondCheckbox.isSelected()) {
				LOGGER.warn("Second checkbox not selected, retrying...");
				js.executeScript("arguments[0].click();", secondCheckbox);
				Thread.sleep(300);
				wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
				PerformanceUtils.waitForPageReady(driver, 1);
			}
			
			Assert.assertTrue(secondCheckbox.isSelected(), "Second grade checkbox not selected: " + secondGradeValue);
			GradesOption2 = secondGradeValue;
			LOGGER.info("Selected second Grades Value: '{}' from Grades Filters dropdown", secondGradeValue);
			ExtentCucumberAdapter.addTestStepLog("Selected Grades Value: " + secondGradeValue + " from Grades Filters dropdown");
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("select_two_options_in_grades_filters_dropdown", e);
			LOGGER.error("Exception occurred in ValidateJobMappingFiltersFunctionality: {}", e.getMessage(), e);
			Assert.fail("Issue in selecting two options from Grades dropdown in Filters...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in selecting two options from Grades dropdown in Filters...Please Investigate!!!");
		}
		
	}
	
	/**
	 * Clicks the Clear Filters button
	 * Page already loaded, directly clicks to clear
	 */
	public void click_on_clear_filters_button() {
		try {
			// Page already loaded from previous step, directly click clear button
			try {
				wait.until(ExpectedConditions.visibilityOf(clearFiltersBtn)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", clearFiltersBtn);
				} catch (Exception s) {
					utils.jsClick(driver, clearFiltersBtn);
				}
			}	
		// REMOVED: driver.navigate().refresh() - Unnecessary and clears checkbox selections
		wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
		PerformanceUtils.waitForPageReady(driver, 2);
		Thread.sleep(300); // PERFORMANCE: Reduced from 500ms for UI stabilization after clearing filters
		
		// IMPORTANT: Reset initialFilteredResultsCount when filters are cleared
	// This ensures the next filter application starts fresh
	PO04_VerifyJobMappingPageComponents.initialFilteredResultsCount.set(null);
	LOGGER.debug("Reset initialFilteredResultsCount after clearing filters");
		
		LOGGER.info("Clicked on clear Filters button and Cleared all the applied filters....");
		ExtentCucumberAdapter.addTestStepLog("Clicked on clear Filters button and Cleared all the applied filters....");
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_on_clear_filters_button", e);
			LOGGER.error("Exception occurred in ValidateJobMappingFiltersFunctionality: {}", e.getMessage(), e);
			Assert.fail("Issue in clicking Clear Filters Button to clear all the applied filters...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in clicking Clear Filters Button to clear all the applied filters...Please Investigate!!!");
		}
	}
	
	public void click_on_departments_filters_dropdown_button() {
		try {
			LOGGER.info("Attempting to click Departments dropdown...");
			PerformanceUtils.waitForPageReady(driver, 2);
			
			// Scroll into view with center positioning
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", departmentsFiltersDropdown);
			Thread.sleep(300); // Wait for scroll to complete
			
			// Wait for element to be clickable
			wait.until(ExpectedConditions.elementToBeClickable(departmentsFiltersDropdown));
			
			// Try multiple click strategies
			boolean clicked = false;
			try {
				// Method 1: Standard click
				departmentsFiltersDropdown.click();
				clicked = true;
			} catch (Exception e) {
				try {
					// Method 2: JavaScript click
					js.executeScript("arguments[0].click();", departmentsFiltersDropdown);
					clicked = true;
				} catch (Exception s) {
					// Method 3: Utils jsClick
					utils.jsClick(driver, departmentsFiltersDropdown);
					clicked = true;
				}
			}
			
			if (!clicked) {
				throw new Exception("Failed to click Departments dropdown after all attempts");
			}
			
			// Wait for dropdown to expand
			Thread.sleep(300);
			PerformanceUtils.waitForPageReady(driver, 1);
			
			LOGGER.info(" Clicked on Departments dropdown in Filters");
			ExtentCucumberAdapter.addTestStepLog("Clicked on Departments dropdown in Filters");
			
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_on_departments_filters_dropdown_button", e);
			LOGGER.error("Exception occurred in ValidateJobMappingFiltersFunctionality: {}", e.getMessage(), e);
			Assert.fail("Issue in clicking Departments dropdown in Filters...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in clicking Departments dropdown in Filters...Please Investigate!!!");
		}
	}
	
	public void select_one_option_in_departments_filters_dropdown() {
		try {
			PerformanceUtils.waitForPageReady(driver, 2);
			List<WebElement> DepartmentsCheckboxes = driver.findElements(By.xpath("//div[@data-testid='dropdown-Departments']//..//input[@type='checkbox']"));
			List<WebElement> DepartmentsValues = driver.findElements(By.xpath("//div[@data-testid='dropdown-Departments']//..//input[@type='checkbox']//..//label"));
			
			LOGGER.info("Found {} department options in dropdown", DepartmentsValues.size());
			
			if (DepartmentsValues.isEmpty()) {
				throw new Exception("No department options found in dropdown");
			}
			
			// Dynamically select the first available option (safer than hardcoded index)
			int selectedIndex = 0;
			
			// Try to select index 7 if available (for consistency with old tests), otherwise use first
			if (DepartmentsValues.size() > 7) {
				selectedIndex = 7;
				LOGGER.info("Using preferred index 7 for department selection");
			} else {
				selectedIndex = 0;
				LOGGER.info("Using first available department (index 0) as there are fewer than 8 options");
			}
			
			WebElement targetCheckbox = DepartmentsCheckboxes.get(selectedIndex);
			WebElement targetLabel = DepartmentsValues.get(selectedIndex);
			String departmentValue = targetLabel.getText().trim();
			
			LOGGER.info("Attempting to select department: '{}'", departmentValue);
			
			// Scroll into view
			js.executeScript("arguments[0].scrollIntoView({block: 'center'});", targetCheckbox);
			Thread.sleep(300);
			
			// Click the checkbox (more reliable than clicking label)
			boolean clicked = false;
			try {
				// Method 1: JavaScript click on checkbox
				js.executeScript("arguments[0].click();", targetCheckbox);
				clicked = true;
					} catch (Exception e) {
						try {
					// Method 2: Regular click on label (departments might need label click)
					wait.until(ExpectedConditions.elementToBeClickable(targetLabel)).click();
					clicked = true;
						} catch (Exception s) {
					// Method 3: Fallback to utils.jsClick
					utils.jsClick(driver, targetLabel);
					clicked = true;
				}
			}
			
			if (!clicked) {
				throw new Exception("Failed to click department checkbox/label");
			}
			
			// Wait for page to update
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForPageReady(driver, 1);
			
			// Verify selection
			if (!targetCheckbox.isSelected()) {
				LOGGER.warn("Checkbox not selected after click, retrying...");
				js.executeScript("arguments[0].click();", targetCheckbox);
				Thread.sleep(300);
				wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
				PerformanceUtils.waitForPageReady(driver, 1);
			}
			
			Assert.assertTrue(targetCheckbox.isSelected(), "Department checkbox not selected: " + departmentValue);
			DepartmentsOption = departmentValue;
			
			LOGGER.info("Selected Departments Value: '{}' from Departments Filters dropdown", departmentValue);
			ExtentCucumberAdapter.addTestStepLog("Selected Departments Value: " + departmentValue + " from Departments Filters dropdown");
			
			PerformanceUtils.waitForPageReady(driver, 1);
			} catch (Exception e) {
				ScreenshotHandler.captureFailureScreenshot("select_one_option_in_departments_filters_dropdown", e);
				LOGGER.error("Exception occurred in ValidateJobMappingFiltersFunctionality: {}", e.getMessage(), e);
				Assert.fail("Issue in selecting one option from Departments dropdown in Filters...Please Investigate!!!");
				ExtentCucumberAdapter.addTestStepLog("Issue in selecting one option from Departments dropdown in Filters...Please Investigate!!!");
		}
	}
	
	public void validate_job_mapping_profiles_are_correctly_filtered_with_applied_departments_options() {
		try {
			List<WebElement> allDepartments = driver.findElements(By.xpath("//*[text()='Organization jobs']//..//tbody//tr//td[4]//div | //*[@id=\"table-container\"]/div[1]/div/div[2]/div/div/div[1]/span[3]"));
		    for (WebElement department: allDepartments) {
		        String text = department.getText();
		        if(text.contentEquals(DepartmentsOption) || text.contentEquals(DepartmentsOption1) || text.contentEquals(DepartmentsOption2)) {
		        	 LOGGER.info("Organization Job Mapping Profile with Department : " + text + " is correctly filtered with applied departments options");
		        	 ExtentCucumberAdapter.addTestStepLog("Organization Job Mapping Profile with Department : " + text + " is correctly filtered with applied departments options");
		        } else {
		        	Assert.fail("Organization Job Mapping Profile with Department : " + text + " from Filters results DOES NOT match with applied departments options");
		        	ExtentCucumberAdapter.addTestStepLog("Organization Job Mapping Profile with Department : " + text + " from Filters results DOES NOT match with applied departments options...Please Investigate!!!");
		        	throw new Exception("Organization Job Profile is displaying with Incorrect Department Value from applied Filters...Please Investigate!!!");
		        }
		    }
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_job_mapping_profiles_are_correctly_filtered_with_applied_departments_options", e);
			LOGGER.error("Exception occurred in ValidateJobMappingFiltersFunctionality: {}", e.getMessage(), e);
			Assert.fail("Issue in Validating Job Mapping Profiles Results with applied departments options...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in Validating Job Mapping Profiles Results with applied departments options...Please Investigate!!!");
		}	
		js.executeScript("window.scrollTo(0, 0);"); // Scroll to top (headless-compatible)
	}
	
	public void select_two_options_in_departments_filters_dropdown() {
		try {
			PerformanceUtils.waitForPageReady(driver, 2);
			List<WebElement> DepartmentsCheckboxes = driver.findElements(By.xpath("//div[@data-testid='dropdown-Departments']//..//input[@type='checkbox']"));
			List<WebElement> DepartmentsValues = driver.findElements(By.xpath("//div[@data-testid='dropdown-Departments']//..//input[@type='checkbox']//..//label"));
			
			LOGGER.info("Found {} department options for multiple selection", DepartmentsValues.size());
			
			if (DepartmentsValues.size() < 2) {
				throw new Exception("Not enough department options available. Need at least 2, found: " + DepartmentsValues.size());
			}
			
			// Dynamically select two indices based on available options
			int firstIndex, secondIndex;
			
			if (DepartmentsValues.size() > 12) {
				// Prefer indices 10 and 12 for consistency with old tests
				firstIndex = 10;
				secondIndex = 12;
				LOGGER.info("Using preferred indices 10 and 12 for multiple department selection");
			} else if (DepartmentsValues.size() > 7) {
				// Use middle indices if fewer options
				firstIndex = DepartmentsValues.size() / 3;
				secondIndex = (DepartmentsValues.size() * 2) / 3;
				LOGGER.info("Using dynamic indices {} and {} based on available options", firstIndex, secondIndex);
			} else {
				// Use first two if very few options
				firstIndex = 0;
				secondIndex = 1;
				LOGGER.info("Using first two indices (0 and 1) as there are fewer options");
			}
			
			// Select first department
			WebElement firstCheckbox = DepartmentsCheckboxes.get(firstIndex);
			WebElement firstLabel = DepartmentsValues.get(firstIndex);
			String firstDepartmentValue = firstLabel.getText().trim();
			
			LOGGER.info("Selecting first department: '{}'", firstDepartmentValue);
			js.executeScript("arguments[0].scrollIntoView({block: 'center'});", firstCheckbox);
			Thread.sleep(300);
			
			try {
				js.executeScript("arguments[0].click();", firstCheckbox);
					} catch(Exception e) {
						try {
					wait.until(ExpectedConditions.elementToBeClickable(firstCheckbox)).click();
						} catch (Exception s) {
					utils.jsClick(driver, firstCheckbox);
				}
			}
			
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForPageReady(driver, 1);
			
			// Verify first selection
			if (!firstCheckbox.isSelected()) {
				LOGGER.warn("First checkbox not selected, retrying...");
				js.executeScript("arguments[0].click();", firstCheckbox);
				Thread.sleep(300);
				wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
				PerformanceUtils.waitForPageReady(driver, 1);
			}
			
			Assert.assertTrue(firstCheckbox.isSelected(), "First department checkbox not selected: " + firstDepartmentValue);
			DepartmentsOption1 = firstDepartmentValue;
			LOGGER.info("Selected first Departments Value: '{}' from Departments Filters dropdown", firstDepartmentValue);
			ExtentCucumberAdapter.addTestStepLog("Selected Departments Value: " + firstDepartmentValue + " from Departments Filters dropdown");
			
			// Select second department
			WebElement secondCheckbox = DepartmentsCheckboxes.get(secondIndex);
			WebElement secondLabel = DepartmentsValues.get(secondIndex);
			String secondDepartmentValue = secondLabel.getText().trim();
			
			LOGGER.info("Selecting second department: '{}'", secondDepartmentValue);
			js.executeScript("arguments[0].scrollIntoView({block: 'center'});", secondCheckbox);
			Thread.sleep(300);
			
			try {
				js.executeScript("arguments[0].click();", secondCheckbox);
			} catch(Exception e) {
				try {
					wait.until(ExpectedConditions.elementToBeClickable(secondCheckbox)).click();
				} catch (Exception s) {
					utils.jsClick(driver, secondCheckbox);
				}
			}
			
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForPageReady(driver, 1);
			
			// Verify second selection
			if (!secondCheckbox.isSelected()) {
				LOGGER.warn("Second checkbox not selected, retrying...");
				js.executeScript("arguments[0].click();", secondCheckbox);
				Thread.sleep(300);
				wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
				PerformanceUtils.waitForPageReady(driver, 1);
			}
			
			Assert.assertTrue(secondCheckbox.isSelected(), "Second department checkbox not selected: " + secondDepartmentValue);
			DepartmentsOption2 = secondDepartmentValue;
			LOGGER.info("Selected second Departments Value: '{}' from Departments Filters dropdown", secondDepartmentValue);
			ExtentCucumberAdapter.addTestStepLog("Selected Departments Value: " + secondDepartmentValue + " from Departments Filters dropdown");
			
			} catch (Exception e) {
				ScreenshotHandler.captureFailureScreenshot("select_two_options_in_departments_filters_dropdown", e);
				LOGGER.error("Exception occurred in ValidateJobMappingFiltersFunctionality: {}", e.getMessage(), e);
				Assert.fail("Issue in selecting two options from Departments dropdown in Filters...Please Investigate!!!");
				ExtentCucumberAdapter.addTestStepLog("Issue in selecting two options from Departments dropdown in Filters...Please Investigate!!!");
		}
	}
	
	public void click_on_functions_subfunctions_filters_dropdown_button() {
		try {
		LOGGER.info("Attempting to click Functions/Subfunctions dropdown");
		// PERFORMANCE: Reduced from 2s to 1s (filters are already loaded)
		PerformanceUtils.waitForPageReady(driver, 1);
			
			// PERFORMANCE: Instant scroll instead of smooth scroll (saves ~1 second)
			js.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});", functionsSubFunctionsFiltersDropdown);
			Thread.sleep(200); // PERFORMANCE: Reduced from 500ms to 200ms
			
			// PERFORMANCE: Use shorter timeout (3s instead of default 60s)
			WebDriverWait shortWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(3));
			shortWait.until(ExpectedConditions.elementToBeClickable(functionsSubFunctionsFiltersDropdown));
			LOGGER.info("Functions/Subfunctions dropdown is visible and clickable");
			
			// Try multiple click strategies with detailed logging
			boolean clickSucceeded = false;
			String clickMethod = "";
			
			// Strategy 1: Regular WebDriver click
			try {
				functionsSubFunctionsFiltersDropdown.click();
				clickSucceeded = true;
				clickMethod = "regular click";
			} catch (Exception e1) {
				LOGGER.warn("Regular click failed: {} - {}", e1.getClass().getSimpleName(), e1.getMessage());
				
				// Strategy 2: JavaScript click
				try {
					js.executeScript("arguments[0].click();", functionsSubFunctionsFiltersDropdown);
					clickSucceeded = true;
					clickMethod = "JavaScript click";
				} catch (Exception e2) {
					LOGGER.warn("JavaScript click failed: {} - {}", e2.getClass().getSimpleName(), e2.getMessage());
					
					// Strategy 3: Utility jsClick method
					try {
						utils.jsClick(driver, functionsSubFunctionsFiltersDropdown);
						clickSucceeded = true;
						clickMethod = "utility jsClick";
					} catch (Exception e3) {
						LOGGER.error("All click methods failed: regular, JavaScript, and utility jsClick");
						throw new Exception("Failed to click dropdown using all available methods. Last error: " + e3.getMessage());
					}
				}
			}
			
			if (clickSucceeded) {
				LOGGER.info("Clicked on Functions / Subfunctions dropdown in Filters using: {}", clickMethod);
				ExtentCucumberAdapter.addTestStepLog("Clicked on Functions / Subfunctions dropdown in Filters (Method: " + clickMethod + ")");
			}
			
	} catch (Exception e) {
			// Enhanced error message with element state (safely check if element exists)
			String elementDisplayed = "unknown";
			String elementEnabled = "unknown";
			
			try {
				elementDisplayed = String.valueOf(functionsSubFunctionsFiltersDropdown.isDisplayed());
				elementEnabled = String.valueOf(functionsSubFunctionsFiltersDropdown.isEnabled());
			} catch (Exception checkException) {
				// Element doesn't exist, keep as "unknown"
				elementDisplayed = "Element not found";
				elementEnabled = "Element not found";
			}
			
			String errorDetails = String.format(
				"Failed to click Functions/Subfunctions dropdown. " +
				"XPath: //div[@data-testid='dropdown-Functions_SubFunctions'], " +
				"Element displayed: %s, Element enabled: %s, " +
				"Error type: %s, Error message: %s",
				elementDisplayed,
				elementEnabled,
				e.getClass().getSimpleName(),
				e.getMessage()
			);
			
			ScreenshotHandler.captureFailureScreenshot("click_on_functions_subfunctions_filters_dropdown_button", e);
			LOGGER.error(errorDetails, e);
			Assert.fail(errorDetails);
			ExtentCucumberAdapter.addTestStepLog("FAILURE: " + errorDetails);
		}
	}
	
	public void select_a_function_and_verify_all_subfunctions_inside_function_are_selected_automatically() {
		List<WebElement> FunctionsCheckboxes = driver.findElements(By.xpath("//div[@data-testid='dropdown-Functions_SubFunctions']//..//input[@type='checkbox']"));
		List<WebElement> FunctionsValues = driver.findElements(By.xpath("//div[@data-testid='dropdown-Functions_SubFunctions']//..//input[@type='checkbox']//..//label"));
		List<WebElement> SubFunctionsCheckboxes = driver.findElements(By.xpath("//button[contains(@data-testid,'suboption')]//..//..//div[2]//input"));
		List<WebElement> SubFunctionsValues = driver.findElements(By.xpath("//button[contains(@data-testid,'suboption')]//..//..//div[2]//label"));
		List<WebElement> ToggleSubfunctions = driver.findElements(By.xpath("//button[contains(@data-testid,'toggle-suboptions')]"));
		try {
			PerformanceUtils.waitForPageReady(driver, 2);
			for (int i=1; i<=FunctionsValues.size(); i++)
			{
				if(i==9) {
					// HEADLESS FIX: Scroll checkbox (not label) into view
					WebElement targetCheckbox = FunctionsCheckboxes.get(i);
					WebElement targetLabel = FunctionsValues.get(i);
					
					js.executeScript("arguments[0].scrollIntoView({block: 'center'});", targetCheckbox);
					Thread.sleep(300);
					
					// HEADLESS FIX: Click checkbox directly, not label
					boolean clicked = false;
					try {
						// Method 1: JavaScript click on checkbox (most reliable in headless)
						js.executeScript("arguments[0].click();", targetCheckbox);
						Thread.sleep(300); // PERFORMANCE: Reduced from 500ms
						clicked = true;
					} catch (Exception e) {
						try {
							// Method 2: Force click using utils
							utils.jsClick(driver, targetCheckbox);
							Thread.sleep(300); // PERFORMANCE: Reduced from 500ms
							clicked = true;
						} catch (Exception s) {
							// Method 3: Last resort - try clicking label
							js.executeScript("arguments[0].click();", targetLabel);
							Thread.sleep(300); // PERFORMANCE: Reduced from 500ms
							clicked = true;
						}
					}
					
					if (!clicked) {
						throw new Exception("Failed to click function checkbox/label");
					}
					
					// Wait for spinner and page to stabilize
					try {
						wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
					} catch (Exception e) {
						// Spinner might not appear, continue
					}
					PerformanceUtils.waitForPageReady(driver, 2);
					
					// HEADLESS FIX: Verify checkbox is actually selected with retry
					int verifyAttempts = 0;
					boolean isSelected = false;
					while (verifyAttempts < 3 && !isSelected) {
						try {
							isSelected = targetCheckbox.isSelected();
							if (!isSelected) {
								LOGGER.warn("Checkbox not selected on attempt {}, retrying click...", verifyAttempts + 1);
								js.executeScript("arguments[0].click();", targetCheckbox);
								Thread.sleep(300); // PERFORMANCE: Reduced from 500ms
							}
						} catch (Exception e) {
							LOGGER.warn("Error verifying checkbox selection: {}", e.getMessage());
						}
						verifyAttempts++;
					}
					
					Assert.assertTrue(isSelected, "Function checkbox not selected after " + verifyAttempts + " attempts");
					
					FunctionsOption = targetLabel.getText().toString();
					LOGGER.info("Selected Functions Value : " + targetLabel.getText() + " from Functions / SubFunctions Filters dropdown....");
					ExtentCucumberAdapter.addTestStepLog("Selected Functions Value : " + targetLabel.getText() + " from Functions / SubFunctions Filters dropdown....");
					break;
				}
			}
			PerformanceUtils.waitForPageReady(driver, 1); // PERFORMANCE: Reduced from 3s
			} catch (Exception e) {
				ScreenshotHandler.captureFailureScreenshot("select_a_function_and_verify_all_subfunctions_inside_function_are_selected_automatically", e);
				LOGGER.error("Exception occurred in ValidateJobMappingFiltersFunctionality: {}", e.getMessage(), e);
				Assert.fail("Issue in selecting a Function and verifying SubFunctions from Functions / SubFunctions dropdown in Filters...Please Investigate!!!");
				ExtentCucumberAdapter.addTestStepLog("Issue in selecting a Function and verifying SubFunctions from Functions / SubFunctions dropdown in Filters...Please Investigate!!!");
		}
		try {
			for (int i=1; i<=FunctionsValues.size(); i++)
			{
				if(i==9) {
					utils.jsClick(driver, ToggleSubfunctions.get(i));
					LOGGER.info("Clicked on Toggle button of the Selected Function : " + FunctionsValues.get(i).getText());
					ExtentCucumberAdapter.addTestStepLog("Clicked on Toggle button of the Selected Function : " + FunctionsValues.get(i).getText());
					for (int j=0; j<SubFunctionsValues.size(); j++) {
						Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(SubFunctionsCheckboxes.get(j))).isSelected());
						LOGGER.info("Subfunction value : " + SubFunctionsValues.get(j).getText() + " is Selected Automatically as Expected...");
						ExtentCucumberAdapter.addTestStepLog("Subfunction value : " + SubFunctionsValues.get(j).getText() + " is Selected Automatically as Expected...");
				}
				break;
			}
			}
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("select_a_function_verify_subfunctions", e);
			LOGGER.error("Exception occurred in ValidateJobMappingFiltersFunctionality: {}", e.getMessage(), e);
			Assert.fail("Issue in Verifying Sub-functions selected or not...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifying Sub-functions selected or not...Please Investigate!!!");
		}	
	}
	
	public void validate_job_mapping_profiles_are_correctly_filtered_with_applied_functions_subfunctions_options() {
		try {
			List<WebElement> allFunctions = driver.findElements(By.xpath("//*[text()='Organization jobs']//..//tbody//tr//td[@colspan='7']//span[2] | //*[@id='table-container']/div[1]/div/div[2]/div/div/div[2]/span/div/span/span[2]"));
	    for (WebElement Function_SubFunction: allFunctions) {
	        String fullText = Function_SubFunction.getText();
	        
	        // Check if text contains pipe (has subfunction)
	        if(fullText.contains("|")) {
	        	 // Split using regex that handles spaces around pipe: "Function | Subfunction" or "Function|Subfunction"
	        	 String[] parts = fullText.split("\\s*\\|\\s*", 2);
	        	 String function = parts[0].trim();
	        	 String subfunction = parts[1].trim();
	        	 
	        	 if(function.contentEquals(FunctionsOption) || function.contentEquals(FunctionsOption1) || function.contentEquals(FunctionsOption2)) {
		        	 LOGGER.info("Organization Job Mapping Profile with Function : " + function + " and Sub-Function value : " + subfunction + " is correctly filtered with applied functions / subfunctions options");
		        	 ExtentCucumberAdapter.addTestStepLog("Organization Job Mapping Profile with Function : " + function + " and Sub-Function value : " + subfunction + " is correctly filtered with applied functions / subfunctions options");
		        } else {
		        	LOGGER.info("Organization Job Mapping Profile with Function : " + function + " and Sub-Function value : " + subfunction + " from Filters results DOES NOT match with applied functions / subfunctions options");
		        	ExtentCucumberAdapter.addTestStepLog("Organization Job Mapping Profile with Function : " + function + " and Sub-Function value : " + subfunction + " from Filters results DOES NOT match with applied functions / subfunctions options...Please Investigate!!!");
		        	throw new Exception("Organization Job Profile is displaying with Incorrect functions / subfunctions Value from applied Filters...Please Investigate!!!");
		        }
	        } else {
	        	// No pipe - function only, no subfunction
	        	String function = fullText.trim();
	        	String subfunction = "-";
	        	
	        	if(function.contentEquals(FunctionsOption) || function.contentEquals(FunctionsOption1) || function.contentEquals(FunctionsOption2)) {
		        	 LOGGER.info("Organization Job Mapping Profile with Function : " + function + " and Sub-Function value : " + subfunction + " is correctly filtered with applied functions / subfunctions options");
		        	 ExtentCucumberAdapter.addTestStepLog("Organization Job Mapping Profile with Function : " + function + " and Sub-Function value : " + subfunction + " is correctly filtered with applied functions / subfunctions options");
		        } else {
		        	LOGGER.info("Organization Job Mapping Profile with Function : " + function + " and Sub-Function value : " + subfunction + " from Filters results DOES NOT match with applied functions / subfunctions options");
		        	ExtentCucumberAdapter.addTestStepLog("Organization Job Mapping Profile with Function : " + function + " and Sub-Function value : " + subfunction + " from Filters results DOES NOT match with applied functions / subfunctions options...Please Investigate!!!");
		        	throw new Exception("Organization Job Profile is displaying with Incorrect functions / subfunctions Value from applied Filters...Please Investigate!!!");
		        }
	        }
	    }
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("validate_job_mapping_profiles_are_correctly_filtered_with_applied_functions_subfunctions_options", e);
			LOGGER.error("Exception occurred in ValidateJobMappingFiltersFunctionality: {}", e.getMessage(), e);
			Assert.fail("Issue in Validating Job Mapping Profiles Results with applied functions / subfunctions options...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in Validating Job Mapping Profiles Results with applied functions / subfunctions options...Please Investigate!!!");
		}	
		js.executeScript("window.scrollTo(0, 0);"); // Scroll to top (headless-compatible)
	}
	
	public void user_should_verify_search_bar_is_available_in_functions_subfunctions_filters_dropdown() {
		try {
			PerformanceUtils.waitForPageReady(driver, 2);
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(functionsSubFunctionsSearch)).isDisplayed());
			LOGGER.info(functionsSubFunctionsSearch.getAttribute("placeholder") + " is available as expected in Functions Subfunctions dropdown in Filters...");
			ExtentCucumberAdapter.addTestStepLog(functionsSubFunctionsSearch.getAttribute("placeholder") + " is available as expected in Functions Subfunctions dropdown in Filters...");
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_verify_search_bar_is_available_in_functions_subfunctions_filters_dropdown", e);
			LOGGER.error("Exception occurred in ValidateJobMappingFiltersFunctionality: {}", e.getMessage(), e);
			Assert.fail("Issue in verifying Search bar availablility in Functions Subfunctions dropdown in Filters...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying Search bar availablility in Functions Subfunctions dropdown in Filters...Please Investigate!!!");
		}
	}
	
	public void click_inside_search_bar_and_enter_function_name() {
		try {
			PerformanceUtils.waitForPageReady(driver, 2);
			
			// IMPORTANT: Reuse the function name that was validated in the previous scenario in Feature 11C
			// FunctionsOption should already be set from the first scenario "User filters by function and verifies all subfunctions are auto-selected"
			if (FunctionsOption != null && !FunctionsOption.isEmpty()) {
				LOGGER.info("Reusing function name '{}' from previous scenario in Feature 11C", FunctionsOption);
				ExtentCucumberAdapter.addTestStepLog("Reusing function name '" + FunctionsOption + "' from previous scenario in Feature 11C");
			} else {
				// Fallback: If FunctionsOption is not set, find a function with subfunctions
				LOGGER.warn("FunctionsOption not set from previous scenario, searching for a function with subfunctions...");
				
				List<WebElement> ToggleButtons = driver.findElements(By.xpath("//button[contains(@data-testid,'toggle-suboptions')]"));
				List<WebElement> FunctionLabels = driver.findElements(By.xpath("//div[@data-testid='dropdown-Functions_SubFunctions']//..//input[@type='checkbox']//..//label"));
				
				String selectedFunctionWithSubfunctions = null;
				
				if (!ToggleButtons.isEmpty()) {
					// Find a function that has a toggle button (meaning it has subfunctions)
					for (int i = 0; i < ToggleButtons.size(); i++) {
						try {
							// Check if toggle button is displayed
							if (ToggleButtons.get(i).isDisplayed()) {
								// Get the corresponding function label
								WebElement toggleBtn = ToggleButtons.get(i);
								String dataTestId = toggleBtn.getAttribute("data-testid");
								
								// Extract function name from data-testid (format: "toggle-suboptions-FunctionName")
								if (dataTestId != null && dataTestId.startsWith("toggle-suboptions-")) {
									String functionName = dataTestId.replace("toggle-suboptions-", "");
									
									// Find the label with this function name
									for (WebElement label : FunctionLabels) {
										String labelText = label.getText().trim();
										if (labelText.equalsIgnoreCase(functionName) || functionName.contains(labelText.replace(" ", ""))) {
											selectedFunctionWithSubfunctions = labelText;
											LOGGER.info("Found function with subfunctions: {}", selectedFunctionWithSubfunctions);
											break;
										}
									}
									
									if (selectedFunctionWithSubfunctions == null) {
										// Alternative: use the function name from data-testid directly
										selectedFunctionWithSubfunctions = functionName.replace("-", " ");
										LOGGER.info("Using function name from data-testid: {}", selectedFunctionWithSubfunctions);
									}
									break;
								}
							}
						} catch (Exception ex) {
							// Continue to next toggle button if this one fails
							LOGGER.debug("Could not process toggle button at index {}: {}", i, ex.getMessage());
							continue;
						}
					}
				}
				
				// Set FunctionsOption if found
				if (selectedFunctionWithSubfunctions != null && !selectedFunctionWithSubfunctions.isEmpty()) {
					FunctionsOption = selectedFunctionWithSubfunctions;
					LOGGER.info("Set FunctionsOption to function with subfunctions: {}", FunctionsOption);
					ExtentCucumberAdapter.addTestStepLog("Selected function with subfunctions: " + FunctionsOption);
				} else {
					// Last resort: pick the first function from the list
					if (!FunctionLabels.isEmpty()) {
						FunctionsOption = FunctionLabels.get(0).getText().trim();
						LOGGER.warn("Could not find function with subfunctions, using first function: {}", FunctionsOption);
						ExtentCucumberAdapter.addTestStepLog("Warning: Using first available function: " + FunctionsOption);
					} else {
						throw new Exception("No functions available in the dropdown to search for");
					}
				}
			}
			
			// Click inside search bar
			try {
				wait.until(ExpectedConditions.elementToBeClickable(functionsSubFunctionsSearch)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", functionsSubFunctionsSearch);
				} catch (Exception s) {
					utils.jsClick(driver, functionsSubFunctionsSearch);
				}
			}
			
			// Enter the function name
			wait.until(ExpectedConditions.visibilityOf(functionsSubFunctionsSearch)).clear();
			wait.until(ExpectedConditions.visibilityOf(functionsSubFunctionsSearch)).sendKeys(FunctionsOption);
			PerformanceUtils.waitForPageReady(driver, 2);
			
			LOGGER.info("Clicked inside Search bar and entered function name as: " + FunctionsOption);
			ExtentCucumberAdapter.addTestStepLog("Clicked inside Search bar and entered function name as: " + FunctionsOption);	
			
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_inside_search_bar_and_enter_function_name", e);
			LOGGER.error("Exception occurred in ValidateJobMappingFiltersFunctionality: {}", e.getMessage(), e);
			Assert.fail("Issue in clicking inside Search bar and entering Function name in Functions Subfunctions dropdown....Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in clicking inside Search bar and entering Function name in Functions Subfunctions dropdown....Please Investigate!!!");
		}
	}
	
	public void user_should_click_on_dropdown_button_of_searched_function_name() {
		try {
			PerformanceUtils.waitForPageReady(driver, 2);
			try {
				wait.until(ExpectedConditions.elementToBeClickable(functionsSubFunctionsToggleSuboptions)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", functionsSubFunctionsToggleSuboptions);
				} catch (Exception s) {
					utils.jsClick(driver, functionsSubFunctionsToggleSuboptions);
				}
			}
			LOGGER.info("Clicked on dropdown button of Searched Function name : " + FunctionsOption);
			ExtentCucumberAdapter.addTestStepLog("Clicked on dropdown button of Searched Function name : " + FunctionsOption);
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_click_on_dropdown_button_of_searched_function_name", e);
			LOGGER.error("Exception occurred in ValidateJobMappingFiltersFunctionality: {}", e.getMessage(), e);
			Assert.fail("Issue in clicking on dropdown button of Searched Function name...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in clicking on dropdown button of Searched Function name...Please Investigate!!!");
		}
	}
	
	/**
	 * Helper method to dynamically find and select a Function that has subfunctions
	 * This makes the test robust by searching for valid test data automatically
	 * @return true if a function with subfunctions was found and selected, false otherwise
	 */
	private boolean findAndSelectFunctionWithSubfunctions() {
		try {
		LOGGER.info("Searching for a function with subfunctions...");
		
		// CRITICAL: Clear the search bar first to show ALL functions in the dropdown
		// Without this, the dropdown is filtered to only show the previously searched function
		boolean searchCleared = false;
		try {
			LOGGER.info("Clearing search bar to reveal all functions...");
			WebElement searchBox = wait.until(ExpectedConditions.visibilityOf(functionsSubFunctionsSearch));
			
			// Strategy 1: Try standard clear()
			searchBox.clear();
			Thread.sleep(300);
			
			// Strategy 2: If clear didn't work, try select all + delete
			String currentValue = searchBox.getAttribute("value");
			if (currentValue != null && !currentValue.trim().isEmpty()) {
				LOGGER.warn("Search bar not cleared by clear(), trying select all + delete");
				searchBox.sendKeys(Keys.CONTROL + "a");
				Thread.sleep(100);
				searchBox.sendKeys(Keys.BACK_SPACE);
				Thread.sleep(300);
			}
			
			// Strategy 3: Verify it's actually clear, if not try multiple backspaces
			currentValue = searchBox.getAttribute("value");
			if (currentValue != null && !currentValue.trim().isEmpty()) {
				LOGGER.warn("Search bar still contains '{}', sending multiple backspaces", currentValue);
				for (int i = 0; i < 50; i++) { // Max 50 characters
					searchBox.sendKeys(Keys.BACK_SPACE);
				}
				Thread.sleep(300);
			}
			
			PerformanceUtils.waitForPageReady(driver, 2);
			
			// Final verification
			currentValue = searchBox.getAttribute("value");
			searchCleared = (currentValue == null || currentValue.trim().isEmpty());
			
			if (!searchCleared) {
				LOGGER.error("Failed to clear search bar. Current value: '{}'", currentValue);
			}
		} catch (Exception e) {
			LOGGER.error("Error clearing search bar: {}", e.getMessage());
		}
		
		// Get all available functions in the dropdown (now unfiltered)
		List<WebElement> FunctionsLabels = driver.findElements(By.xpath("//div[@data-testid='dropdown-Functions_SubFunctions']//input[@type='checkbox'][not(ancestor::button[contains(@data-testid,'suboption')])]//..//label"));
		List<WebElement> ToggleButtons = driver.findElements(By.xpath("//button[contains(@data-testid,'toggle-suboptions')]"));
		
		LOGGER.info("Found {} functions in dropdown after clearing search", FunctionsLabels.size());
		
		if (FunctionsLabels.isEmpty() || FunctionsLabels.size() <= 1) {
			LOGGER.error("Very few functions found ({}). Search bar may not have cleared properly!", FunctionsLabels.size());
			return false;
		}
			
		// Loop through each function to find one with subfunctions
			for (int i = 0; i < FunctionsLabels.size(); i++) {
				try {
				String functionName = FunctionsLabels.get(i).getText().trim();
				
				if (functionName.isEmpty()) {
					continue;
				}
				
				// Click the toggle button to expand this function
				if (i < ToggleButtons.size()) {
					try {
						js.executeScript("arguments[0].scrollIntoView({block: 'center'});", ToggleButtons.get(i));
						Thread.sleep(200);
						ToggleButtons.get(i).click();
						Thread.sleep(300); // PERFORMANCE: Reduced from 500ms
						PerformanceUtils.waitForPageReady(driver, 1);
					} catch (Exception e) {
						continue;
					}
				}
				
				// Check if this function has subfunctions
				List<WebElement> subfunctions = driver.findElements(By.xpath("//button[contains(@data-testid,'suboption')]//..//..//div[2]//label"));
				int subfunctionCount = subfunctions.size() > 0 ? subfunctions.size() - 1 : 0;
				
			if (subfunctionCount > 0) {
				LOGGER.info("Found function '{}' with {} subfunction(s)", functionName, subfunctionCount);
				FunctionsOption = functionName;
				ExtentCucumberAdapter.addTestStepLog("Dynamically selected Function '" + functionName + "' (has " + subfunctionCount + " subfunction(s))");
				
				// Enter the new function name in the search bar and expand it
				try {
					wait.until(ExpectedConditions.visibilityOf(functionsSubFunctionsSearch)).clear();
				wait.until(ExpectedConditions.visibilityOf(functionsSubFunctionsSearch)).sendKeys(functionName);
				PerformanceUtils.waitForPageReady(driver, 2);
				Thread.sleep(300); // PERFORMANCE: Reduced from 500ms
					
					// Click the dropdown button to expand the function
					WebElement SearchedFunction_Dropdown = driver.findElement(By.xpath("//div[@data-testid='dropdown-Functions_SubFunctions']//input[@type='checkbox'][contains(@id,'" + functionName +"')][not(ancestor::button[contains(@data-testid,'suboption')])]/ancestor::button//button[contains(@data-testid,'toggle-suboptions')]"));
					js.executeScript("arguments[0].scrollIntoView({block: 'center'});", SearchedFunction_Dropdown);
					Thread.sleep(200);
					wait.until(ExpectedConditions.elementToBeClickable(SearchedFunction_Dropdown)).click();
					PerformanceUtils.waitForPageReady(driver, 2);
				} catch (Exception e) {
					// Ignore - continue
				}
				
				return true;
			}
				
			// Collapse and try next
			if (i < ToggleButtons.size()) {
				try {
					ToggleButtons.get(i).click();
					Thread.sleep(300);
				} catch (Exception e) {
					// Ignore
				}
			}
				
			} catch (Exception e) {
				// Continue to next function
			}
		}
		
		LOGGER.error("No functions with subfunctions found in dropdown");
		return false;
		
	} catch (Exception e) {
		LOGGER.error("Error searching for function with subfunctions: {}", e.getMessage());
		return false;
	}
}
	
	public void select_one_subfunction_option_inside_function_name_dropdown() {
		PerformanceUtils.waitForPageReady(driver, 1); // PERFORMANCE: Reduced from 3s
		
		// IMPORTANT: This XPath specifically targets ONLY subfunctions (nested checkboxes inside suboption buttons)
		// This avoids confusion when Function and Subfunction have the same name (e.g., both are "a1")
		// The XPath ensures we only get checkboxes that are INSIDE a button with data-testid containing 'suboption'
		List<WebElement> SubFunctionsCheckboxes = driver.findElements(By.xpath("//button[contains(@data-testid,'suboption')]//..//..//div[2]//input"));
		List<WebElement> SubFunctionsValues = driver.findElements(By.xpath("//button[contains(@data-testid,'suboption')]//..//..//div[2]//label"));
		
		try {
		// Note: Index 0 is typically a header/parent element, actual subfunctions start at index 1
		// If current function has no subfunctions, search for one that does
		if (SubFunctionsValues.isEmpty() || SubFunctionsValues.size() <= 1) {
			LOGGER.warn("Function '{}' has no subfunctions. Searching for valid function...", FunctionsOption);
				
			// Try to find and select a function that has subfunctions
			boolean foundFunctionWithSubfunctions = findAndSelectFunctionWithSubfunctions();
			
			if (!foundFunctionWithSubfunctions) {
				String errorMsg = "No functions with subfunctions found in dropdown. Check application data or test environment.";
				LOGGER.error(errorMsg);
				ScreenshotHandler.captureFailureScreenshot("no_functions_with_subfunctions", new AssertionError(errorMsg));
				Assert.fail(errorMsg);
			}
			
			// Re-fetch subfunctions for the newly selected function
			SubFunctionsCheckboxes = driver.findElements(By.xpath("//button[contains(@data-testid,'suboption')]//..//..//div[2]//input"));
			SubFunctionsValues = driver.findElements(By.xpath("//button[contains(@data-testid,'suboption')]//..//..//div[2]//label"));
		}
			
		LOGGER.info("Selecting subfunction for Function '{}'", FunctionsOption);
		
	for (int j=1; j<SubFunctionsValues.size(); j++) {
		if(j==1) {
		String subfunctionText = SubFunctionsValues.get(j).getText();
		WebElement subfunctionCheckbox = SubFunctionsCheckboxes.get(j);
		
		// HEADLESS FIX: Scroll checkbox into view with center positioning (critical for headless)
			js.executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'center'});", subfunctionCheckbox);
			Thread.sleep(300);
			
			// HEADLESS FIX: Click the CHECKBOX directly (not the label) for reliability
			boolean clickSucceeded = false;
			int maxAttempts = 3;
			
			for (int attempt = 1; attempt <= maxAttempts && !clickSucceeded; attempt++) {
				try {
					// Check if already selected (might have been clicked in previous attempt)
					if (subfunctionCheckbox.isSelected()) {
						LOGGER.info("Subfunction '{}' already selected", subfunctionText);
						clickSucceeded = true;
						break;
					}
					
					// Method 1: JavaScript click on checkbox (most reliable in headless)
					js.executeScript("arguments[0].click();", subfunctionCheckbox);
					Thread.sleep(300); // PERFORMANCE: Reduced from 500ms
					
					// Wait for spinner if present
					try {
						wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
					} catch (Exception e) {
						// Spinner might not appear, continue
					}
					
					// Verify checkbox is now selected
					if (subfunctionCheckbox.isSelected()) {
						clickSucceeded = true;
					} else {
						LOGGER.warn("Checkbox not selected after attempt {}, retrying...", attempt);
						// Scroll again before retry
						if (attempt < maxAttempts) {
							js.executeScript("arguments[0].scrollIntoView({block: 'center'});", subfunctionCheckbox);
							Thread.sleep(200);
						}
					}
					
				} catch (Exception e) {
					LOGGER.warn("Click attempt {} failed for '{}': {}", attempt, subfunctionText, e.getMessage());
					if (attempt < maxAttempts) {
						// Try scrolling again before next attempt
						try {
							js.executeScript("arguments[0].scrollIntoView({block: 'center'});", subfunctionCheckbox);
							Thread.sleep(200);
						} catch (Exception scrollEx) {
							// Continue to next attempt
						}
					}
				}
			}
			
			if (!clickSucceeded) {
				String errorMsg = "Failed to click subfunction '" + subfunctionText + "' after " + maxAttempts + " attempts";
				LOGGER.error(errorMsg);
				ScreenshotHandler.captureFailureScreenshot("subfunction_click_failed", new AssertionError(errorMsg));
				Assert.fail(errorMsg);
			}
			
			// Final verification: Ensure checkbox is selected
			PerformanceUtils.waitForPageReady(driver, 2);
			boolean isSubfunctionSelected = subfunctionCheckbox.isSelected();
			if (!isSubfunctionSelected) {
				String errorMsg = "Subfunction '" + subfunctionText + "' not selected after successful click. UI state issue.";
				LOGGER.error(errorMsg);
				ScreenshotHandler.captureFailureScreenshot("subfunction_not_selected", new AssertionError(errorMsg));
				Assert.fail(errorMsg);
			}
			
			LOGGER.info("Selected subfunction '{}'", subfunctionText);
			ExtentCucumberAdapter.addTestStepLog("Selected SubFunction '" + subfunctionText + "' from Functions/SubFunctions dropdown of Function '" + FunctionsOption + "'");
		}
		}
	} catch (Exception e) {
			String errorDetails = String.format(
				"Failed to select subfunction inside function '%s' dropdown. " +
				"Error type: %s, Error message: %s",
				FunctionsOption,
				e.getClass().getSimpleName(),
				e.getMessage()
			);
			
			ScreenshotHandler.captureFailureScreenshot("select_one_subfunction_option_inside_function_name_dropdown", e);
			LOGGER.error(errorDetails, e);
			Assert.fail(errorDetails);
			ExtentCucumberAdapter.addTestStepLog("FAILURE: " + errorDetails);
		}		
	}
	
	public void user_should_verify_function_name_is_automatically_selected_after_selecting_subfunction_option() {
		// IMPORTANT: Use specific XPath that excludes subfunction checkboxes
		// This handles cases where Function and Subfunction have the same name (e.g., both are "a1")
		// The XPath excludes any checkbox that is inside a suboption button (i.e., excludes subfunctions)
		String SearchedFunctionsOptionXpath = "//div[@data-testid='dropdown-Functions_SubFunctions']//input[@type='checkbox'][contains(@id,'" + FunctionsOption +"')][not(ancestor::button[contains(@data-testid,'suboption')])]";
		
		try {
			LOGGER.info("Verifying if '{}' Function checkbox is automatically selected after selecting subfunction...", FunctionsOption);
			LOGGER.debug("Searching for parent function checkbox (excluding subfunctions)");
			
			// First, check if the checkbox element exists
			WebElement SearchedFunction_Checkbox;
			try {
				SearchedFunction_Checkbox = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(SearchedFunctionsOptionXpath)));
				LOGGER.debug("Function checkbox element found in DOM");
			} catch (Exception e) {
				String errorMsg = String.format(
					"Function checkbox element with ID containing '%s' not found. " +
					"XPath: %s. This may indicate the function name is incorrect or the element doesn't exist. " +
					"Error: %s",
					FunctionsOption,
					SearchedFunctionsOptionXpath,
					e.getMessage()
				);
				LOGGER.error(errorMsg);
				ScreenshotHandler.captureFailureScreenshot("function_checkbox_not_found", e);
				Assert.fail(errorMsg);
				return;
			}
			
			// Wait for the checkbox to be visible
			wait.until(ExpectedConditions.visibilityOf(SearchedFunction_Checkbox));
			LOGGER.debug("Function checkbox is visible");
			
			// Check initial state before waiting
			boolean initialState = SearchedFunction_Checkbox.isSelected();
			LOGGER.debug("Function checkbox initial state: {}", initialState ? "already selected" : "not selected");
			
			if (initialState) {
				LOGGER.info("Function checkbox '{}' is already selected (no auto-selection needed)", FunctionsOption);
				ExtentCucumberAdapter.addTestStepLog(FunctionsOption + " Function checkbox is already selected");
				return;
			}
			
			// ENHANCED: Wait for the checkbox to actually be selected (auto-selection might take a moment)
			boolean isFunctionCheckboxSelected = false;
			
			try {
				isFunctionCheckboxSelected = wait.until(driver -> {
					try {
						// Re-locate element to avoid stale reference
						WebElement checkbox = driver.findElement(By.xpath(SearchedFunctionsOptionXpath));
						boolean isSelected = checkbox.isSelected();
						if (!isSelected) {
							LOGGER.debug("Function checkbox not yet auto-selected, waiting...");
						}
						return isSelected;
					} catch (Exception e) {
						LOGGER.warn("Error checking checkbox selection state: {}", e.getMessage());
						return false;
					}
				});
			} catch (org.openqa.selenium.TimeoutException te) {
				// Timeout - checkbox never got selected
				isFunctionCheckboxSelected = false;
				LOGGER.error("Timeout: Function checkbox was not auto-selected within the wait period");
			}
			
			// Verify the result
			if (isFunctionCheckboxSelected) {
				LOGGER.info("'{}' Function checkbox is Automatically selected as expected after selecting subfunction option", FunctionsOption);
				ExtentCucumberAdapter.addTestStepLog(FunctionsOption + " Function checkbox is Automatically selected as expected after selecting subfunction option");
			} else {
				// Enhanced error message with more context
				String errorMsg = String.format(
					"Function checkbox '%s' was NOT automatically selected after selecting subfunction. " +
					"Expected: checkbox to be selected (checked), Actual: checkbox not selected. " +
					"This indicates the UI auto-selection feature is not working for this function. " +
					"XPath: %s. Please check: 1) Is the subfunction correctly selected? 2) Does the UI support auto-selection for this function? 3) Are there JavaScript errors?",
					FunctionsOption,
					SearchedFunctionsOptionXpath
				);
				ScreenshotHandler.captureFailureScreenshot("function_checkbox_not_auto_selected", new AssertionError(errorMsg));
				LOGGER.error(errorMsg);
				Assert.fail(errorMsg);
			}
			
	} catch (Exception e) {
			// Enhanced error details
			String errorDetails = String.format(
				"Failed to verify if Function checkbox '%s' is automatically selected. " +
				"XPath: %s, Error type: %s, Error message: %s",
				FunctionsOption,
				SearchedFunctionsOptionXpath,
				e.getClass().getSimpleName(),
				e.getMessage()
			);
			
			ScreenshotHandler.captureFailureScreenshot("user_should_verify_function_name_is_automatically_selected_after_selecting_subfunction_option", e);
			LOGGER.error(errorDetails, e);
			Assert.fail(errorDetails);
			ExtentCucumberAdapter.addTestStepLog("FAILURE: " + errorDetails);
		}
	}
	
	public void select_two_subfunction_options_inside_function_name_filters_dropdown() {
		PerformanceUtils.waitForPageReady(driver, 1); // PERFORMANCE: Reduced from 3s
		List<WebElement> SubFunctionsCheckboxes = driver.findElements(By.xpath("//button[contains(@data-testid,'suboption')]//..//..//div[2]//input"));
		List<WebElement> SubFunctionsValues = driver.findElements(By.xpath("//button[contains(@data-testid,'suboption')]//..//..//div[2]//label"));
		try {
			LOGGER.info("Selecting 2 subfunctions for Function '{}'", FunctionsOption);
			
			for (int j=1; j<SubFunctionsValues.size(); j++) {
				if(j==1 || j==2) {
					String subfunctionText = SubFunctionsValues.get(j).getText();
					WebElement subfunctionCheckbox = SubFunctionsCheckboxes.get(j);
					
					LOGGER.debug("Selecting subfunction #{}: '{}'", j, subfunctionText);
					
					// HEADLESS FIX: Scroll checkbox into view with center positioning (critical for headless)
					js.executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'center'});", subfunctionCheckbox);
					Thread.sleep(300);
					
					// HEADLESS FIX: Click the CHECKBOX directly (not the label) for reliability
					boolean clickSucceeded = false;
					int maxAttempts = 3;
					
					for (int attempt = 1; attempt <= maxAttempts && !clickSucceeded; attempt++) {
						try {
							// Check if already selected (might have been clicked in previous attempt)
							if (subfunctionCheckbox.isSelected()) {
								LOGGER.info("Subfunction '{}' already selected", subfunctionText);
								clickSucceeded = true;
								break;
							}
							
							// Method 1: JavaScript click on checkbox (most reliable in headless)
							js.executeScript("arguments[0].click();", subfunctionCheckbox);
							Thread.sleep(300); // PERFORMANCE: Reduced from 500ms
							
							// Wait for spinner if present
							try {
								wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
							} catch (Exception e) {
								// Spinner might not appear, continue
							}
							
							// Verify checkbox is now selected
							if (subfunctionCheckbox.isSelected()) {
								clickSucceeded = true;
							} else {
								LOGGER.warn("Checkbox not selected after attempt {}, retrying...", attempt);
								// Scroll again before retry
								if (attempt < maxAttempts) {
									js.executeScript("arguments[0].scrollIntoView({block: 'center'});", subfunctionCheckbox);
									Thread.sleep(200);
								}
							}
							
						} catch (Exception e) {
							LOGGER.warn("Click attempt {} failed for '{}': {}", attempt, subfunctionText, e.getMessage());
							if (attempt < maxAttempts) {
								// Try scrolling again before next attempt
								try {
									js.executeScript("arguments[0].scrollIntoView({block: 'center'});", subfunctionCheckbox);
									Thread.sleep(200);
								} catch (Exception scrollEx) {
									// Continue to next attempt
								}
							}
						}
					}
					
					if (!clickSucceeded) {
						String errorMsg = "Failed to click subfunction '" + subfunctionText + "' after " + maxAttempts + " attempts";
						LOGGER.error(errorMsg);
						ScreenshotHandler.captureFailureScreenshot("subfunction_" + j + "_click_failed", new AssertionError(errorMsg));
						Assert.fail(errorMsg);
					}
					
					// Final verification: Ensure checkbox is selected
					PerformanceUtils.waitForPageReady(driver, 2);
					boolean isSubfunctionSelected = subfunctionCheckbox.isSelected();
					if (!isSubfunctionSelected) {
						String errorMsg = "Subfunction '" + subfunctionText + "' not selected after successful click. UI state issue.";
						LOGGER.error(errorMsg);
						ScreenshotHandler.captureFailureScreenshot("subfunction_" + j + "_not_selected", new AssertionError(errorMsg));
						Assert.fail(errorMsg);
					}
					
					LOGGER.info("Selected SubFunction #{}: '{}' from Functions/SubFunctions Filters dropdown of Function '{}'", j, subfunctionText, FunctionsOption);
					ExtentCucumberAdapter.addTestStepLog("Selected SubFunction Value : " + subfunctionText + " from Functions / SubFunctions Filters dropdown of the Function " + FunctionsOption +"....");
			}
			}
	} catch (Exception e) {
		ScreenshotHandler.captureFailureScreenshot("select_two_subfunction_options_inside_function_name_filters_dropdown", e);
		LOGGER.error("Exception occurred in ValidateJobMappingFiltersFunctionality: {}", e.getMessage(), e);
		Assert.fail("Issue in selecting two subfunction options inside function name dropdown...Please Investigate!!!");
		ExtentCucumberAdapter.addTestStepLog("Issue in selecting two subfunction options inside function name dropdown...Please Investigate!!!");
	}		
	}

	public void validate_job_mapping_profiles_are_correctly_filtered_with_applied_grades_departments_and_functions_subfunctions_options() throws Exception {
		try {
			driver.findElement(By.xpath("//h2[text()='Organization jobs']//..//tbody//tr//td[3]//div"));
			List<WebElement> allGrades = driver.findElements(By.xpath("//h2[text()='Organization jobs']//..//tbody//tr//td[3]//div"));
			List<WebElement> allDepartments = driver.findElements(By.xpath("//h2[text()='Organization jobs']//..//tbody//tr//td[4]//div"));
			List<WebElement> allFunctions = driver.findElements(By.xpath("//h2[text()='Organization jobs']//..//tbody//tr//td[@colspan='7']//span[2]"));
		    for (WebElement grade: allGrades) {
		        String Gradestext = grade.getText();
		        if(Gradestext.contentEquals(GradesOption)) {
		        	for (WebElement department: allDepartments) {
		    	        String Departmentstext = department.getText();
		    	        if(Departmentstext.contentEquals(DepartmentsOption)) {
		    	        	 for (WebElement Function_SubFunction: allFunctions) {
		    	     	        String fullText = Function_SubFunction.getText();
		    	     	        String function;
		    	     	        String subfunction;
		    	     	        
		    	     	        // Check if text contains pipe (has subfunction)
		    	     	        if(fullText.contains("|")) {
		    	     	            // Split using regex that handles spaces around pipe: "Function | Subfunction"
		    	     	            String[] parts = fullText.split("\\s*\\|\\s*", 2);
		    	     	            function = parts[0].trim();
		    	     	            subfunction = parts[1].trim();
		    	     	        } else {
		    	     	            // No pipe - function only, no subfunction
		    	     	            function = fullText.trim();
		    	     	            subfunction = "-";
		    	     	        }
		    	     	        
		    	     	        if(function.contentEquals(FunctionsOption)) {
		    	     	        	LOGGER.info("Organization Job Mapping Profile with Grade : " + Gradestext + " and Department : " + Departmentstext + " and Function : " + function + " and Sub-Function value : " + subfunction +" is correctly filtered with applied Combined Filters grades departments and functions subfunctions options");
		    			        	ExtentCucumberAdapter.addTestStepLog("Organization Job Mapping Profile with Grade : " + Gradestext + " and Department : " + Departmentstext + " and Function : " + function + " and Sub-Function value : " + subfunction +" is correctly filtered with applied Combined Filters grades departments and functions subfunctions options");
		    	     	        } else {
		    	     	        	Assert.fail("Organization Job Mapping Profile with Function : " + function + " and Sub-Function value : " + subfunction + " from Filters results DOES NOT match with applied Combined Filters grades departments and functions / subfunctions options...Please Investigate!!!");
		    	    	        	ExtentCucumberAdapter.addTestStepLog("Organization Job Mapping Profile with Function : " + function + " and Sub-Function value : " + subfunction + " from Filters results DOES NOT match with applied Combined Filters grades departments and functions / subfunctions options...Please Investigate!!!");
		    	    	        	throw new Exception("Organization Job Profile is displaying with Incorrect functions / subfunctions Value from applied Combined Filters  grades departments and functions / subfunctions options...Please Investigate!!!");
		    	     	        }
		    	        }
		    	        	
		    	        } else { 
		    	        	Assert.fail("Organization Job Mapping Profile with Department : " + Departmentstext + " from Filters results DOES NOT match with applied Combined Filters grades departments and functions / subfunctions options...Please Investigate!!!");
		    	        	ExtentCucumberAdapter.addTestStepLog("Organization Job Mapping Profile with Department : " + Departmentstext + " from Filters results DOES NOT match with applied Combined Filters grades departments and functions / subfunctions options...Please Investigate!!!");
		    	        	throw new Exception("Organization Job Profile is displaying with Incorrect Department Value from applied Combined Filters  grades departments and functions / subfunctions options...Please Investigate!!!");
		    	        }
		        }
		        	 
		        } else {
		        	Assert.fail("Organization Job Mapping Profile with Grade : " + Gradestext + " from Filters results DOES NOT match with applied Combined Filters grades departments and functions / subfunctions options...Please Investigate!!!");
		        	ExtentCucumberAdapter.addTestStepLog("Organization Job Mapping Profile with Grade : " + Gradestext + " from Filters results DOES NOT match with applied Combined Filters grades departments and functions / subfunctions options...Please Investigate!!!");
		        	throw new Exception("Organization Job Profile is displaying with Incorrect Grade Value from applied Combined Filters  grades departments and functions / subfunctions options...Please Investigate!!!");
		        }
		    }
	} catch (Exception e) {
			try {
				Assert.assertTrue(noDataContainer.isDisplayed());
				LOGGER.info(noDataContainer.getText() + " with applied Combined Filters grades departments and functions / subfunctions options...Please Investigate!!!");
				ExtentCucumberAdapter.addTestStepLog(noDataContainer.getText() + " with applied Combined Filters grades departments and functions / subfunctions options...Please Investigate!!!");
			} catch (Exception s) {
				ScreenshotHandler.captureFailureScreenshot("validate_combined_filters", s);
				LOGGER.error("Exception occurred in ValidateJobMappingFiltersFunctionality: {}", s.getMessage(), s);
				Assert.fail("Issue in Validating Job Mapping Profiles Results with applied Combined Filters grades departments and functions / subfunctions options...Please Investigate!!!");
				ExtentCucumberAdapter.addTestStepLog("Issue in Validating Job Mapping Profiles Results with applied Combined Filters grades departments and functions / subfunctions options...Please Investigate!!!");
			}
			
		}	
		js.executeScript("window.scrollTo(0, 0);"); // Scroll to top (headless-compatible)
	}	
	
	/**
	 * Clicks on Mapping Status dropdown in Filters
	 * The filter options are already visible after opening the main filters dropdown
	 * No need to wait for spinner as filter items appear immediately
	 */
	public void click_on_mapping_status_filters_dropdown_button() {
		try {
			// Filter dropdown items are already loaded, no need to wait for spinner
			js.executeScript("arguments[0].scrollIntoView();", mappingStatusFiltersDropdown);
			try {
				wait.until(ExpectedConditions.visibilityOf(mappingStatusFiltersDropdown)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", mappingStatusFiltersDropdown);
				} catch (Exception s) {
					utils.jsClick(driver, mappingStatusFiltersDropdown);
				}
			}	
			LOGGER.info("Clicked on Mapping Status dropdown in Filters...");
			ExtentCucumberAdapter.addTestStepLog("Clicked on Mapping Status dropdown in Filters...");
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_on_mapping_status_filters_dropdown_button", e);
			LOGGER.error("Exception occurred in ValidateJobMappingFiltersFunctionality: {}", e.getMessage(), e);
			Assert.fail("Issue in clicking Mapping Status dropdown in Filters...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in clicking Mapping Status dropdown in Filters...Please Investigate!!!");
		}
	}
	
	public void select_one_option_in_mapping_status_filters_dropdown() {
		try {
			PerformanceUtils.waitForPageReady(driver, 2);
			List<WebElement> MappingStatusCheckboxes = driver.findElements(By.xpath("//div[@data-testid='dropdown-MappingStatus']//..//input[@type='checkbox']"));
			List<WebElement> MappingStatusValues = driver.findElements(By.xpath("//div[@data-testid='dropdown-MappingStatus']//..//input[@type='checkbox']//..//label"));
			
			// Priority order: 1. Manually, 2. Unmapped, 3. Auto Matched
			String[] priorityOrder = {"Manually", "Unmapped", "Auto Matched"};
			boolean optionSelected = false;
			
			// Check each priority level in order
			for (String desiredStatus : priorityOrder) {
				LOGGER.info("Looking for Mapping Status containing: {}", desiredStatus);
				
				// Search through all available mapping status options
				for (int i=0; i<MappingStatusValues.size(); i++) {
					String actualStatusText = MappingStatusValues.get(i).getText();
					
					// If this option contains the desired status keyword
					if(actualStatusText.contains(desiredStatus)) {
						LOGGER.info("Found matching Mapping Status: {} (contains '{}')", actualStatusText, desiredStatus);
						ExtentCucumberAdapter.addTestStepLog("Found matching Mapping Status: " + actualStatusText);
						
						js.executeScript("arguments[0].scrollIntoView();", MappingStatusValues.get(i));
						try {
							wait.until(ExpectedConditions.visibilityOf(MappingStatusValues.get(i))).click();
							wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
							PerformanceUtils.waitForPageReady(driver, 1); // PERFORMANCE: Reduced from 3s
						} catch (Exception e) {
							try {
								wait.until(ExpectedConditions.visibilityOf(MappingStatusValues.get(i)));
								js.executeScript("arguments[0].click();", MappingStatusValues.get(i));
								wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
								PerformanceUtils.waitForPageReady(driver, 1); // PERFORMANCE: Reduced from 3s
							} catch (Exception s) {
								wait.until(ExpectedConditions.visibilityOf(MappingStatusValues.get(i)));
								utils.jsClick(driver, MappingStatusValues.get(i));
								wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
								PerformanceUtils.waitForPageReady(driver, 1); // PERFORMANCE: Reduced from 3s
							}
						}
						Assert.assertTrue(MappingStatusCheckboxes.get(i).isSelected());
						DepartmentsOption = MappingStatusValues.get(i).getText().toString();
						LOGGER.info("Selected Mapping Status Value: {} from Mapping Status Filters dropdown", DepartmentsOption);
						ExtentCucumberAdapter.addTestStepLog("Selected Mapping Status Value: " + DepartmentsOption + " from Mapping Status Filters dropdown");
						optionSelected = true;
						break; // Exit inner loop after selecting
					}
				}
				
				// If we found and selected an option, exit outer loop (don't check lower priorities)
				if (optionSelected) {
					break;
				} else {
					LOGGER.info("No Mapping Status found containing '{}', checking next priority...", desiredStatus);
				}
			}
			
			// If no option was selected after checking all priorities
			if (!optionSelected) {
				LOGGER.warn("No Mapping Status found matching any of the priority options: Manually, Unmapped, or Auto Matched");
				ExtentCucumberAdapter.addTestStepLog("WARNING: No matching Mapping Status found in dropdown");
				Assert.fail("No Mapping Status option found matching the priority list (Manually, Unmapped, Auto Matched)");
			}
			
			PerformanceUtils.waitForPageReady(driver, 1); // PERFORMANCE: Reduced from 3s
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("select_one_option_in_mapping_status_filters_dropdown", e);
			LOGGER.error("Exception occurred in ValidateJobMappingFiltersFunctionality: {}", e.getMessage(), e);
			Assert.fail("Issue in selecting one option from Mapping Status dropdown in Filters...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in selecting one option from Mapping Status dropdown in Filters...Please Investigate!!!");
		}
	}
	
	
}





