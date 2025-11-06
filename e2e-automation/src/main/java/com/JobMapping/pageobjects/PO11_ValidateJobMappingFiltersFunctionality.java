package com.JobMapping.pageobjects;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.JobMapping.utils.PerformanceUtils;
import com.JobMapping.utils.ScreenshotHandler;
import com.JobMapping.utils.Utilities;
import com.JobMapping.webdriverManager.DriverManager;
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
	
	@FindBy(xpath = "//button[text()='×']")
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
			for (int i=1; i<=GradesValues.size(); i++)
			{
				if(i==7) {
					js.executeScript("arguments[0].scrollIntoView();", GradesValues.get(i));
					try {
					wait.until(ExpectedConditions.visibilityOf(GradesValues.get(i))).click();
					wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
					// PERFORMANCE: Replaced Thread.sleep(3000) with smart page ready wait
					PerformanceUtils.waitForPageReady(driver, 3);
				} catch (Exception e) {
					try {
						wait.until(ExpectedConditions.visibilityOf(GradesValues.get(i)));
						js.executeScript("arguments[0].click();", GradesValues.get(i));
						wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
						// PERFORMANCE: Replaced Thread.sleep(3000) with smart page ready wait
						PerformanceUtils.waitForPageReady(driver, 3);
					} catch (Exception s) {
						wait.until(ExpectedConditions.visibilityOf(GradesValues.get(i)));
						utils.jsClick(driver, GradesValues.get(i));
						wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
						// PERFORMANCE: Replaced Thread.sleep(3000) with smart page ready wait
						PerformanceUtils.waitForPageReady(driver, 3);
					}
				}
					Assert.assertTrue(GradesCheckboxes.get(i).isSelected());
					GradesOption = GradesValues.get(i).getText().toString();
					LOGGER.info("Selected Grades Value : " + GradesValues.get(i).getText() + " from Grades Filters dropdown....");
					ExtentCucumberAdapter.addTestStepLog("Selected Grades Value : " + GradesValues.get(i).getText() + " from Grades Filters dropdown....");
				}
			}
		// PERFORMANCE: Replaced Thread.sleep(3000) with smart page ready wait
		PerformanceUtils.waitForPageReady(driver, 3);
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
			
			while(scrollAttempts < maxScrollAttempts) {
				scrollAttempts++;
				PerformanceUtils.waitForPageReady(driver, 3);
				
				// Use a more robust element retrieval with retry logic to handle stale elements
				String resultsCountText = "";
				try {
					// Wait for element with reduced timeout to avoid script timeout
					WebDriverWait shortWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
					resultsCountText = shortWait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//div[contains(@id,'results-toggle')]//*[contains(text(),'Showing')]")
					)).getText();
			} catch (Exception e) {
				LOGGER.warn("Failed to get results count text on attempt {}: {}", scrollAttempts, e.getMessage());
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
						
						// One more scroll to ensure all content is loaded
						js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
						try {
							wait.until(ExpectedConditions.invisibilityOfAllElements(
								driver.findElements(By.xpath("//div[@data-testid='loader']//img"))
							));
						} catch (Exception e) {
							// Spinner not found or already invisible
						}
						PerformanceUtils.waitForUIStability(driver, 1);
					} else {
						// More results to load - keep scrolling
						previousResultsCountText = resultsCountText;
						js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
						try {
							wait.until(ExpectedConditions.invisibilityOfAllElements(
								driver.findElements(By.xpath("//div[@data-testid='loader']//img"))
							));
						} catch (Exception e) {
							// Spinner not found or already invisible
						}
						PerformanceUtils.waitForUIStability(driver, 2);
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
		js.executeScript("window.scrollTo(document.body.scrollHeight, 0)");
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
			Thread.sleep(500); // Allow UI to stabilize after clearing filters
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
			for (int i=1; i<=GradesCheckboxes.size(); i++)
			{
				if(i==10 || i==12) {
					js.executeScript("arguments[0].scrollIntoView();", GradesValues.get(i));
					try {
						wait.until(ExpectedConditions.visibilityOf(GradesCheckboxes.get(i))).click();
						wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
						PerformanceUtils.waitForPageReady(driver, 3);
					} catch(Exception e) {
						try {
							wait.until(ExpectedConditions.visibilityOf(GradesCheckboxes.get(i)));
							js.executeScript("arguments[0].click();", GradesCheckboxes.get(i));
							wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
							PerformanceUtils.waitForPageReady(driver, 3);
						} catch (Exception s) {
							wait.until(ExpectedConditions.visibilityOf(GradesCheckboxes.get(i)));
							utils.jsClick(driver, GradesCheckboxes.get(i));
							wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
							PerformanceUtils.waitForPageReady(driver, 3);
						}
					}
					Assert.assertTrue(GradesCheckboxes.get(i).isSelected());
					if(i==10) {
						GradesOption1 = GradesValues.get(i).getText().toString();
					} else {
						GradesOption2 = GradesValues.get(i).getText().toString();
					}
					LOGGER.info("Selected Grades Value : " + GradesValues.get(i).getText() + " from Grades Filters dropdown....");
					ExtentCucumberAdapter.addTestStepLog("Selected Grades Value : " + GradesValues.get(i).getText() + " from Grades Filters dropdown....");
				}
			} 
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
			Thread.sleep(500); // Allow UI to stabilize after clearing filters
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
			PerformanceUtils.waitForPageReady(driver, 2);
			js.executeScript("arguments[0].scrollIntoView();", departmentsFiltersDropdown);
			try {
				wait.until(ExpectedConditions.visibilityOf(departmentsFiltersDropdown)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", departmentsFiltersDropdown);
				} catch (Exception s) {
					utils.jsClick(driver, departmentsFiltersDropdown);
				}
			}	
			LOGGER.info("Clicked on Departments dropdown in Filters...");
			ExtentCucumberAdapter.addTestStepLog("Clicked on Departments dropdown in Filters...");
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
			for (int i=1; i<=DepartmentsValues.size(); i++)
			{
				if(i==7) {
					js.executeScript("arguments[0].scrollIntoView();", DepartmentsValues.get(i));
					try {
						wait.until(ExpectedConditions.visibilityOf(DepartmentsValues.get(i))).click();
						wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
						PerformanceUtils.waitForPageReady(driver, 3);
					} catch (Exception e) {
						try {
							wait.until(ExpectedConditions.visibilityOf(DepartmentsValues.get(i)));
							js.executeScript("arguments[0].click();", DepartmentsValues.get(i));
							wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
							PerformanceUtils.waitForPageReady(driver, 3);
						} catch (Exception s) {
							wait.until(ExpectedConditions.visibilityOf(DepartmentsValues.get(i)));
							utils.jsClick(driver, DepartmentsValues.get(i));
							wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
							PerformanceUtils.waitForPageReady(driver, 3);
						}
					}
					Assert.assertTrue(DepartmentsCheckboxes.get(i).isSelected());
					DepartmentsOption = DepartmentsValues.get(i).getText().toString();
					LOGGER.info("Selected Departments Value : " + DepartmentsValues.get(i).getText() + " from Departments Filters dropdown....");
					ExtentCucumberAdapter.addTestStepLog("Selected Departments Value : " + DepartmentsValues.get(i).getText() + " from Departments Filters dropdown....");
				}
			}
			PerformanceUtils.waitForPageReady(driver, 3);
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
		js.executeScript("window.scrollTo(document.body.scrollHeight, 0)");
	}
	
	public void select_two_options_in_departments_filters_dropdown() {
		try {
			PerformanceUtils.waitForPageReady(driver, 2);
			List<WebElement> DepartmentsCheckboxes = driver.findElements(By.xpath("//div[@data-testid='dropdown-Departments']//..//input[@type='checkbox']"));
			List<WebElement> DepartmentsValues = driver.findElements(By.xpath("//div[@data-testid='dropdown-Departments']//..//input[@type='checkbox']//..//label"));
			for (int i=1; i<=DepartmentsCheckboxes.size(); i++)
			{
				if(i==10 || i==12) {
					js.executeScript("arguments[0].scrollIntoView();", DepartmentsValues.get(i));
					try {
						wait.until(ExpectedConditions.visibilityOf(DepartmentsCheckboxes.get(i))).click();
						wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
						PerformanceUtils.waitForPageReady(driver, 3);
					} catch(Exception e) {
						try {
							wait.until(ExpectedConditions.visibilityOf(DepartmentsCheckboxes.get(i)));
							js.executeScript("arguments[0].click();", DepartmentsCheckboxes.get(i));
							wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
							PerformanceUtils.waitForPageReady(driver, 3);
						} catch (Exception s) {
							wait.until(ExpectedConditions.visibilityOf(DepartmentsCheckboxes.get(i)));
							utils.jsClick(driver, DepartmentsCheckboxes.get(i));
							wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
							PerformanceUtils.waitForPageReady(driver, 3);
						}
					}
					Assert.assertTrue(DepartmentsCheckboxes.get(i).isSelected());
					if(i==10) {
						DepartmentsOption1 = DepartmentsValues.get(i).getText().toString();
					} else {
						DepartmentsOption2 = DepartmentsValues.get(i).getText().toString();
					}
					LOGGER.info("Selected Departments Value : " + DepartmentsValues.get(i).getText() + " from Departments Filters dropdown....");
					ExtentCucumberAdapter.addTestStepLog("Selected Departments Value : " + DepartmentsValues.get(i).getText() + " from Departments Filters dropdown....");
				}
			} 
			} catch (Exception e) {
				ScreenshotHandler.captureFailureScreenshot("select_two_options_in_departments_filters_dropdown", e);
				LOGGER.error("Exception occurred in ValidateJobMappingFiltersFunctionality: {}", e.getMessage(), e);
				Assert.fail("Issue in selecting two options from Departments dropdown in Filters...Please Investigate!!!");
				ExtentCucumberAdapter.addTestStepLog("Issue in selecting two options from Departments dropdown in Filters...Please Investigate!!!");
		}
	}
	
	public void click_on_functions_subfunctions_filters_dropdown_button() {
		try {
			PerformanceUtils.waitForPageReady(driver, 2);
			js.executeScript("arguments[0].scrollIntoView();", functionsSubFunctionsFiltersDropdown);
			try {
				wait.until(ExpectedConditions.visibilityOf(functionsSubFunctionsFiltersDropdown)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", functionsSubFunctionsFiltersDropdown);
				} catch (Exception s) {
					utils.jsClick(driver, functionsSubFunctionsFiltersDropdown);
				}
			}	
			LOGGER.info("Clicked on Functions / Subfunctions dropdown in Filters...");
			ExtentCucumberAdapter.addTestStepLog("Clicked on Functions / Subfunctions dropdown in Filters...");
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("click_on_functions_subfunctions_filters_dropdown_button", e);
			LOGGER.error("Exception occurred in ValidateJobMappingFiltersFunctionality: {}", e.getMessage(), e);
			Assert.fail("Issue in clicking Functions / Subfunctions dropdown in Filters...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in clicking Functions / Subfunctions dropdown in Filters...Please Investigate!!!");
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
					js.executeScript("arguments[0].scrollIntoView();", FunctionsValues.get(i));
					try {
						wait.until(ExpectedConditions.visibilityOf(FunctionsValues.get(i))).click();
						wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
						PerformanceUtils.waitForPageReady(driver, 3);
					} catch (Exception e) {
						try {
							wait.until(ExpectedConditions.visibilityOf(FunctionsValues.get(i)));
							js.executeScript("arguments[0].click();", FunctionsValues.get(i));
							wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
							PerformanceUtils.waitForPageReady(driver, 3);
						} catch (Exception s) {
							wait.until(ExpectedConditions.visibilityOf(FunctionsValues.get(i)));
							utils.jsClick(driver, FunctionsValues.get(i));
							wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
							PerformanceUtils.waitForPageReady(driver, 3);
						}
					}
					Assert.assertTrue(FunctionsCheckboxes.get(i).isSelected());
					FunctionsOption = FunctionsValues.get(i).getText().toString();
					LOGGER.info("Selected Functions Value : " + FunctionsValues.get(i).getText() + " from Functions / SubFunctions Filters dropdown....");
					ExtentCucumberAdapter.addTestStepLog("Selected Functions Value : " + FunctionsValues.get(i).getText() + " from Functions / SubFunctions Filters dropdown....");
					break;
				}
			}
			PerformanceUtils.waitForPageReady(driver, 3);
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
		        String function = Function_SubFunction.getText().split("\\|",2)[0].trim();
		        if(function.contains("|")) {
		        	 String subfunction = Function_SubFunction.getText().split("\\|",2)[1].trim();
		        	 if(function.contentEquals(FunctionsOption) || function.contentEquals(FunctionsOption1) || function.contentEquals(FunctionsOption2)) {
			        	 LOGGER.info("Organization Job Mapping Profile with Function : " + function + " and Sub-Function value : " + subfunction + " is correctly filtered with applied functions / subfunctions options");
			        	 ExtentCucumberAdapter.addTestStepLog("Organization Job Mapping Profile with Function : " + function + " and Sub-Function value : " + subfunction + " is correctly filtered with applied functions / subfunctions options");
			        } else {
			        	LOGGER.info("Organization Job Mapping Profile with Function : " + function + " and Sub-Function value : " + subfunction + " from Filters results DOES NOT match with applied functions / subfunctions options");
			        	ExtentCucumberAdapter.addTestStepLog("Organization Job Mapping Profile with Function : " + function + " and Sub-Function value : " + subfunction + " from Filters results DOES NOT match with applied functions / subfunctions options...Please Investigate!!!");
			        	throw new Exception("Organization Job Profile is displaying with Incorrect functions / subfunctions Value from applied Filters...Please Investigate!!!");
			        }
		        } else {
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
		js.executeScript("window.scrollTo(document.body.scrollHeight, 0)");
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
			
			// ENHANCEMENT: Intelligently find a function that has subfunctions (toggle button available)
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
							// Navigate up from toggle button to find the label
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
			
			// If we found a function with subfunctions, use it; otherwise fall back to existing FunctionsOption
			if (selectedFunctionWithSubfunctions != null && !selectedFunctionWithSubfunctions.isEmpty()) {
				FunctionsOption = selectedFunctionWithSubfunctions;
				LOGGER.info("Updated FunctionsOption to function with subfunctions: {}", FunctionsOption);
				ExtentCucumberAdapter.addTestStepLog("Selected function with subfunctions: " + FunctionsOption);
			} else if (FunctionsOption == null || FunctionsOption.isEmpty()) {
				// Last resort: pick the first function from the list
				if (!FunctionLabels.isEmpty()) {
					FunctionsOption = FunctionLabels.get(0).getText().trim();
					LOGGER.warn("Could not find function with subfunctions, using first function: {}", FunctionsOption);
					ExtentCucumberAdapter.addTestStepLog("Warning: Using first available function: " + FunctionsOption);
				} else {
					throw new Exception("No functions available in the dropdown to search for");
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
	
	public void select_one_subfunction_option_inside_function_name_dropdown() {
		PerformanceUtils.waitForPageReady(driver, 3);
		List<WebElement> SubFunctionsCheckboxes = driver.findElements(By.xpath("//button[contains(@data-testid,'suboption')]//..//..//div[2]//input"));
		List<WebElement> SubFunctionsValues = driver.findElements(By.xpath("//button[contains(@data-testid,'suboption')]//..//..//div[2]//label"));
		try {
			for (int j=1; j<SubFunctionsValues.size(); j++) {
				if(j==1) {
					js.executeScript("arguments[0].scrollIntoView();", SubFunctionsValues.get(j));
					try {
						wait.until(ExpectedConditions.visibilityOf(SubFunctionsValues.get(j))).click();
						wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
						PerformanceUtils.waitForPageReady(driver, 3);
					} catch (Exception e) {
						try {
							wait.until(ExpectedConditions.visibilityOf(SubFunctionsValues.get(j)));
							js.executeScript("arguments[0].click();", SubFunctionsValues.get(j));
							wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
							PerformanceUtils.waitForPageReady(driver, 3);
						} catch (Exception s) {
							wait.until(ExpectedConditions.visibilityOf(SubFunctionsValues.get(j)));
							utils.jsClick(driver, SubFunctionsValues.get(j));
							wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
							PerformanceUtils.waitForPageReady(driver, 3);
						}
					}
					Assert.assertTrue(SubFunctionsCheckboxes.get(j).isSelected());
					LOGGER.info("Selected SubFunction Value : " + SubFunctionsValues.get(j).getText() + " from Functions / SubFunctions Filters dropdown of the Function " + FunctionsOption +"....");
					ExtentCucumberAdapter.addTestStepLog("Selected SubFunction Value : " + SubFunctionsValues.get(j).getText() + " from Functions / SubFunctions Filters dropdown of the Function " + FunctionsOption +"....");
			}
			}
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("select_one_subfunction_option_inside_function_name_dropdown", e);
			LOGGER.error("Exception occurred in ValidateJobMappingFiltersFunctionality: {}", e.getMessage(), e);
			Assert.fail("Issue in selecting one subfunction option inside function name dropdown...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in selecting one subfunction option inside function name dropdown...Please Investigate!!!");
		}		
	}
	
	public void user_should_verify_function_name_is_automatically_selected_after_selecting_subfunction_option() {
		String SearchedFunctionsOptionXpath = "//input[contains(@id,'" + FunctionsOption +"')]";
		WebElement SearchedFunction_Checkbox = driver.findElement(By.xpath(SearchedFunctionsOptionXpath));
		try {
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(SearchedFunction_Checkbox)).isSelected());
			LOGGER.info(FunctionsOption + " Function checkbox is Automatically selected as expected after selecting subfunction option");
			ExtentCucumberAdapter.addTestStepLog(FunctionsOption + " Function checkbox is Automatically selected as expected after selecting subfunction option");
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_verify_function_name_is_automatically_selected_after_selecting_subfunction_option", e);
			LOGGER.error("Exception occurred in ValidateJobMappingFiltersFunctionality: {}", e.getMessage(), e);
			Assert.fail("Issue in verifying function name is automatically selected or not after selecting subfunction option...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying function name is automatically selected or not after selecting subfunction option...Please Investigate!!!");
		}
	}
	
	public void select_two_subfunction_options_inside_function_name_filters_dropdown() {
		PerformanceUtils.waitForPageReady(driver, 3);
		List<WebElement> SubFunctionsCheckboxes = driver.findElements(By.xpath("//button[contains(@data-testid,'suboption')]//..//..//div[2]//input"));
		List<WebElement> SubFunctionsValues = driver.findElements(By.xpath("//button[contains(@data-testid,'suboption')]//..//..//div[2]//label"));
		try {
			for (int j=1; j<SubFunctionsValues.size(); j++) {
				if(j==1 || j==2) {
					js.executeScript("arguments[0].scrollIntoView();", SubFunctionsValues.get(j));
					try {
						wait.until(ExpectedConditions.visibilityOf(SubFunctionsValues.get(j))).click();
						wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
						PerformanceUtils.waitForPageReady(driver, 3);
					} catch (Exception e) {
						try {
							wait.until(ExpectedConditions.visibilityOf(SubFunctionsValues.get(j)));
							js.executeScript("arguments[0].click();", SubFunctionsValues.get(j));
							wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
							PerformanceUtils.waitForPageReady(driver, 3);
						} catch (Exception s) {
							wait.until(ExpectedConditions.visibilityOf(SubFunctionsValues.get(j)));
							utils.jsClick(driver, SubFunctionsValues.get(j));
							wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
							PerformanceUtils.waitForPageReady(driver, 3);
						}
					}
					Assert.assertTrue(SubFunctionsCheckboxes.get(j).isSelected());
					LOGGER.info("Selected SubFunction Value : " + SubFunctionsValues.get(j).getText() + " from Functions / SubFunctions Filters dropdown of the Function " + FunctionsOption +"....");
					ExtentCucumberAdapter.addTestStepLog("Selected SubFunction Value : " + SubFunctionsValues.get(j).getText() + " from Functions / SubFunctions Filters dropdown of the Function " + FunctionsOption +"....");
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
		    	     	        String function = Function_SubFunction.getText().split("\\|",2)[0].trim();
		    	     	        String subfunction = Function_SubFunction.getText().split("\\|",2)[1].trim();
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
		js.executeScript("window.scrollTo(document.body.scrollHeight, 0)");
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
							PerformanceUtils.waitForPageReady(driver, 3);
						} catch (Exception e) {
							try {
								wait.until(ExpectedConditions.visibilityOf(MappingStatusValues.get(i)));
								js.executeScript("arguments[0].click();", MappingStatusValues.get(i));
								wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
								PerformanceUtils.waitForPageReady(driver, 3);
							} catch (Exception s) {
								wait.until(ExpectedConditions.visibilityOf(MappingStatusValues.get(i)));
								utils.jsClick(driver, MappingStatusValues.get(i));
								wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
								PerformanceUtils.waitForPageReady(driver, 3);
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
			
			PerformanceUtils.waitForPageReady(driver, 3);
			
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("select_one_option_in_mapping_status_filters_dropdown", e);
			LOGGER.error("Exception occurred in ValidateJobMappingFiltersFunctionality: {}", e.getMessage(), e);
			Assert.fail("Issue in selecting one option from Mapping Status dropdown in Filters...Please Investigate!!!");
			ExtentCucumberAdapter.addTestStepLog("Issue in selecting one option from Mapping Status dropdown in Filters...Please Investigate!!!");
		}
	}
	
	
}

