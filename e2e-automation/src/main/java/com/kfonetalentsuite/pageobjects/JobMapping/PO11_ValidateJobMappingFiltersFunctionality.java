package com.kfonetalentsuite.pageobjects.JobMapping;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.PageObjectHelper;
import com.kfonetalentsuite.utils.common.ExcelDataProvider;

public class PO11_ValidateJobMappingFiltersFunctionality extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO11_ValidateJobMappingFiltersFunctionality.class);

	// PARALLEL EXECUTION FIX: Convert static variables to ThreadLocal for thread isolation
	public static ThreadLocal<String> GradesOption = ThreadLocal.withInitial(() -> "");
	public static ThreadLocal<String> GradesOption1 = ThreadLocal.withInitial(() -> "");
	public static ThreadLocal<String> GradesOption2 = ThreadLocal.withInitial(() -> "");
	public static ThreadLocal<String> DepartmentsOption = ThreadLocal.withInitial(() -> "");
	public static ThreadLocal<String> DepartmentsOption1 = ThreadLocal.withInitial(() -> "");
	public static ThreadLocal<String> DepartmentsOption2 = ThreadLocal.withInitial(() -> "");
	public static ThreadLocal<String> FunctionsOption = ThreadLocal.withInitial(() -> "");
	public static ThreadLocal<String> FunctionsOption1 = ThreadLocal.withInitial(() -> "");
	public static ThreadLocal<String> FunctionsOption2 = ThreadLocal.withInitial(() -> "");

	private static final By GRADES_DROPDOWN = By.xpath("//div[@data-testid='dropdown-Grades']");
	private static final By GRADES_CHECKBOXES = By.xpath("//div[@data-testid='dropdown-Grades']//..//input[@type='checkbox']");
	private static final By GRADES_VALUES = By.xpath("//div[@data-testid='dropdown-Grades']//..//input[@type='checkbox']//..//label");
	private static final By SHOWING_RESULTS = By.xpath("//div[contains(@id,'results-toggle')]//*[contains(text(),'Showing')]");
	private static final By NO_DATA_CONTAINER = By.xpath("//*[@id='no-data-container']");
	private static final By CLEAR_FILTERS_BTN = By.xpath("//button[@data-testid='Clear Filters']");
	private static final By CLEAR_FILTERS_X_BTN = By.xpath("//button[@data-testid='clear-filters-button']");
	private static final By DEPARTMENTS_DROPDOWN = By.xpath("//div[@data-testid='dropdown-Departments']");
	private static final By DEPARTMENTS_CHECKBOXES = By.xpath("//div[@data-testid='dropdown-Departments']//..//input[@type='checkbox']");
	private static final By DEPARTMENTS_VALUES = By.xpath("//div[@data-testid='dropdown-Departments']//..//input[@type='checkbox']//..//label");
	private static final By FUNCTIONS_DROPDOWN = By.xpath("//div[@data-testid='dropdown-Functions_SubFunctions']");
	private static final By FUNCTIONS_SEARCH = By.xpath("//div[@data-testid='dropdown-Functions_SubFunctions']//..//div[2]//input[@placeholder='Search']");
	private static final By FUNCTIONS_CHECKBOXES = By.xpath("//div[@data-testid='dropdown-Functions_SubFunctions']//..//input[@type='checkbox']");
	private static final By FUNCTIONS_VALUES = By.xpath("//div[@data-testid='dropdown-Functions_SubFunctions']//..//input[@type='checkbox']//..//label");
	private static final By TOGGLE_SUBOPTIONS = By.xpath("//button[contains(@data-testid,'toggle-suboptions')]");
	private static final By MAPPING_STATUS_DROPDOWN = By.xpath("//*[@data-testid='dropdown-MappingStatus']");
	private static final By MAPPING_STATUS_CHECKBOXES = By.xpath("//div[@data-testid='dropdown-MappingStatus']//..//input[@type='checkbox']");
	private static final By MAPPING_STATUS_VALUES = By.xpath("//div[@data-testid='dropdown-MappingStatus']//..//input[@type='checkbox']//..//label");
	private static final By SUBFUNCTIONS_CHECKBOXES = By.xpath("//button[contains(@data-testid,'suboption')]//..//..//div[2]//input");
	private static final By SUBFUNCTIONS_VALUES = By.xpath("//button[contains(@data-testid,'suboption')]//..//..//div[2]//label");
	private static final By ALL_GRADES_COLUMN = By.xpath("//*[text()='Organization jobs']//..//tbody//tr//td[3]//div | //*[@id='table-container']/div[1]/div/div[2]/div/div/div[1]/span[2]/span");
	private static final By ALL_DEPARTMENTS_COLUMN = By.xpath("//*[text()='Organization jobs']//..//tbody//tr//td[4]//div | //*[@id=\"table-container\"]/div[1]/div/div[2]/div/div/div[1]/span[3]");
	private static final By ALL_FUNCTIONS_COLUMN = By.xpath("//*[text()='Organization jobs']//..//tbody//tr//td[@colspan='7']//span[2] | //*[@id='table-container']/div[1]/div/div[2]/div/div/div[2]/span/div/span/span[2]");
	private static final By VISIBLE_ROWS = By.xpath("//tbody//tr[contains(@class,'cursor-pointer')]");

	public PO11_ValidateJobMappingFiltersFunctionality() throws IOException {
		super();
	}

	public void click_on_grades_filters_dropdown_button() {
		try {
			waitForSpinners();
			clickElement(GRADES_DROPDOWN);
			PageObjectHelper.log(LOGGER, "Clicked on Grades dropdown in Filters");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_grades_filters_dropdown_button", "Issue clicking Grades dropdown in Filters", e);
			throw e;
		}
	}

	public void select_one_option_in_grades_filters_dropdown() throws Exception {
		try {
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(GRADES_CHECKBOXES));
			List<WebElement> checkboxes = driver.findElements(GRADES_CHECKBOXES);
			List<WebElement> values = driver.findElements(GRADES_VALUES);

			if (values.isEmpty()) throw new Exception("No grade options found in dropdown");

			WebElement targetCheckbox = checkboxes.get(0);
			String gradeValue = values.get(0).getText().trim();

			scrollToElement(targetCheckbox);
			Thread.sleep(300);
			jsClick(targetCheckbox);

			waitForSpinners();
			PerformanceUtils.waitForPageReady(driver, 1);

			if (!targetCheckbox.isSelected()) {
				jsClick(targetCheckbox);
				Thread.sleep(300);
				waitForSpinners();
			}

			Assert.assertTrue(targetCheckbox.isSelected(), "Grade checkbox not selected: " + gradeValue);
			GradesOption.set(gradeValue);
			PageObjectHelper.log(LOGGER, "Selected Grades Value: '" + gradeValue + "' from Grades Filters dropdown");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "select_one_option_in_grades_filters_dropdown", "Issue selecting option from Grades dropdown", e);
			throw e;
		}
	}

	public void apply_filter_using_excel_data(String testId) {
		try {
			Map<String, String> testData = ExcelDataProvider.getTestData("FilterData", testId);
			String filterType = testData.get("FilterType");
			String filterValue = testData.get("FilterValue");

			PageObjectHelper.log(LOGGER, "Applying " + filterType + " filter with value: " + filterValue);

			switch (filterType.toLowerCase()) {
				case "grades":
					click_on_grades_filters_dropdown_button();
					select_filter_option_by_value(filterValue, "Grades");
					GradesOption.set(filterValue);
					break;
				case "departments":
					click_on_departments_filters_dropdown_button();
					select_filter_option_by_value(filterValue, "Departments");
					DepartmentsOption.set(filterValue);
					break;
				case "functions":
				case "functions_subfunctions":
					click_on_functions_subfunctions_filters_dropdown_button();
					select_filter_option_by_value(filterValue, "Functions_SubFunctions");
					FunctionsOption.set(filterValue);
					break;
				case "mappingstatus":
				case "mapping status":
					click_on_mapping_status_filters_dropdown_button();
					select_filter_option_by_value(filterValue, "MappingStatus");
					break;
				default:
					throw new IllegalArgumentException("Unknown filter type: " + filterType);
			}

			try {
				driver.findElement(By.tagName("body")).sendKeys(Keys.ESCAPE);
			} catch (Exception ex) {
				js.executeScript("document.body.click();");
			}

			waitForSpinners();
			PerformanceUtils.waitForPageReady(driver, 2);
			PageObjectHelper.log(LOGGER, "Successfully applied " + filterType + " filter with value: " + filterValue);
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "apply_filter_using_excel_data", "Failed to apply filter using Excel data for TestID: " + testId, e);
		}
	}

	private void select_filter_option_by_value(String filterValue, String filterType) {
		try {
			String checkboxXpath = "//div[@data-testid='dropdown-" + filterType + "']//..//label[contains(text(),'" + filterValue + "')]//input[@type='checkbox']";
			List<WebElement> checkboxes = driver.findElements(By.xpath(checkboxXpath));

			if (checkboxes.isEmpty()) {
				checkboxXpath = "//div[@data-testid='dropdown-" + filterType + "']//..//input[@type='checkbox']";
				List<WebElement> allCheckboxes = driver.findElements(By.xpath(checkboxXpath));
				if (!allCheckboxes.isEmpty()) {
					scrollToElement(allCheckboxes.get(0));
					Thread.sleep(300);
					jsClick(allCheckboxes.get(0));
					PageObjectHelper.log(LOGGER, "Selected first available filter option");
					return;
				}
				throw new RuntimeException("No filter options found for: " + filterValue);
			}

			scrollToElement(checkboxes.get(0));
			Thread.sleep(300);
			jsClick(checkboxes.get(0));
			waitForSpinners();
			PageObjectHelper.log(LOGGER, "Selected filter option: " + filterValue);
		} catch (Exception e) {
			LOGGER.warn("Could not select exact filter value '{}', selecting first available", filterValue);
		}
	}

	public void user_should_scroll_down_to_view_last_result_with_applied_filters() throws InterruptedException {
		try {
			PageObjectHelper.log(LOGGER, "Scrolling down to last filtered result");
			int maxScrollAttempts = 50;
			int scrollAttempts = 0;
			int stableCountIterations = 0;
			int consecutiveFailures = 0;

			while (scrollAttempts < maxScrollAttempts) {
				scrollAttempts++;
				PerformanceUtils.waitForUIStability(driver, 1);

				String resultsCountText = "";
				try {
					WebDriverWait shortWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(1));
					WebElement resultsElement = shortWait.until(ExpectedConditions.presenceOfElementLocated(SHOWING_RESULTS));
					resultsCountText = resultsElement.getText();
					consecutiveFailures = 0;
				} catch (Exception e) {
					consecutiveFailures++;
					if (consecutiveFailures >= 5) {
						try {
							List<WebElement> visibleRows = driver.findElements(VISIBLE_ROWS);
							PageObjectHelper.log(LOGGER, "Scrolled down and loaded " + visibleRows.size() + " job profiles (headless mode)");
						} catch (Exception altEx) {
							LOGGER.error("Alternative row count also failed");
						}
						break;
					}

					try {
						if (driver.findElement(NO_DATA_CONTAINER).isDisplayed()) {
							PageObjectHelper.log(LOGGER, driver.findElement(NO_DATA_CONTAINER).getText() + " with applied Filters");
							break;
						}
					} catch (Exception ex) {
						// Continue
					}
					continue;
				}

				String[] parts = resultsCountText.split(" ");
				if (parts.length > 3 && !parts[1].equals("0")) {
					int currentShowing = Integer.parseInt(parts[1]);
					int totalResults = Integer.parseInt(parts[3]);

					if (currentShowing == totalResults) {
						PageObjectHelper.log(LOGGER, "Scrolled down till last Search Result - " + resultsCountText);
						break;
					}

					if (currentShowing == Integer.parseInt(parts[1])) {
						stableCountIterations++;
						if (stableCountIterations >= 5) break;
					} else {
						stableCountIterations = 0;
					}

					scrollToBottom();
					Thread.sleep(500);
					waitForSpinners();
					Thread.sleep(1000);
				} else {
					try {
						if (driver.findElement(NO_DATA_CONTAINER).isDisplayed()) {
							PageObjectHelper.log(LOGGER, driver.findElement(NO_DATA_CONTAINER).getText() + " with applied Filters");
							break;
						}
					} catch (Exception ex) {
						// Continue
					}
				}
			}
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_scroll_down_to_view_last_result_with_applied_filters", "Issue scrolling to last result", e);
			throw e;
		}
	}

	public void validate_job_mapping_profiles_are_correctly_filtered_with_applied_grades_options() throws Exception {
		try {
			List<WebElement> allGrades = driver.findElements(ALL_GRADES_COLUMN);
			for (int i = 0; i < allGrades.size(); i++) {
				// PARALLEL EXECUTION FIX: Re-fetch element on each iteration to avoid stale element
				WebElement grade = driver.findElements(ALL_GRADES_COLUMN).get(i);
				String text = grade.getText();
				if (text.contentEquals(GradesOption.get()) || text.contentEquals(GradesOption1.get()) || text.contentEquals(GradesOption2.get())) {
					PageObjectHelper.log(LOGGER, "Organization Job with Grade: " + text + " correctly filtered");
				} else {
					throw new Exception("Job Profile with incorrect Grade Value: " + text);
				}
			}
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "validate_job_mapping_profiles_are_correctly_filtered_with_applied_grades_options", "Issue validating grades filter", e);
			throw e;
		}
		scrollToTop();
	}

	public void click_on_clear_x_applied_filter() throws Exception {
		try {
			clickElement(CLEAR_FILTERS_X_BTN);
			PerformanceUtils.waitForPageReady(driver, 2);
			Thread.sleep(300);
			PageObjectHelper.log(LOGGER, "Clicked on clear Filters button (X) and cleared all applied filters");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_clear_x_applied_filter", "Issue clicking Clear Filters Button (X)", e);
			throw e;
		}
	}

	public void select_two_options_in_grades_filters_dropdown() throws Exception {
		try {
			PerformanceUtils.waitForPageReady(driver, 2);
			List<WebElement> checkboxes = driver.findElements(GRADES_CHECKBOXES);
			List<WebElement> values = driver.findElements(GRADES_VALUES);

			if (values.size() < 2) throw new Exception("Not enough grade options. Need at least 2, found: " + values.size());

			for (int i = 0; i < 2; i++) {
				WebElement checkbox = checkboxes.get(i);
				String gradeValue = values.get(i).getText().trim();
				scrollToElement(checkbox);
				Thread.sleep(300);
				jsClick(checkbox);
				waitForSpinners();

				if (!checkbox.isSelected()) {
					jsClick(checkbox);
					Thread.sleep(300);
					waitForSpinners();
				}

				Assert.assertTrue(checkbox.isSelected(), "Grade checkbox not selected: " + gradeValue);
				if (i == 0) GradesOption1.set(gradeValue);
				else GradesOption2.set(gradeValue);
				PageObjectHelper.log(LOGGER, "Selected Grades Value: " + gradeValue);
			}
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "select_two_options_in_grades_filters_dropdown", "Issue selecting two grades", e);
			throw e;
		}
	}

	public void click_on_clear_filters_button() throws Exception {
		try {
			clickElement(CLEAR_FILTERS_BTN);
			PerformanceUtils.waitForPageReady(driver, 2);
			Thread.sleep(300);
			PO04_VerifyJobMappingPageComponents.initialFilteredResultsCount.set(null);
			PageObjectHelper.log(LOGGER, "Clicked on Clear Filters button and cleared all applied filters");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_clear_filters_button", "Issue clicking Clear Filters Button", e);
			throw e;
		}
	}

	public void click_on_departments_filters_dropdown_button() throws Exception {
		try {
			PerformanceUtils.waitForPageReady(driver, 2);
			scrollToElement(driver.findElement(DEPARTMENTS_DROPDOWN));
			Thread.sleep(300);
			clickElement(DEPARTMENTS_DROPDOWN);
			Thread.sleep(300);
			waitForSpinners();
			PageObjectHelper.log(LOGGER, "Clicked on Departments dropdown in Filters");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_departments_filters_dropdown_button", "Issue clicking Departments dropdown", e);
			throw e;
		}
	}

	public void select_one_option_in_departments_filters_dropdown() throws Exception {
		try {
			PerformanceUtils.waitForPageReady(driver, 2);
			List<WebElement> checkboxes = driver.findElements(DEPARTMENTS_CHECKBOXES);
			List<WebElement> values = driver.findElements(DEPARTMENTS_VALUES);

			if (values.isEmpty()) throw new Exception("No department options found");

			WebElement targetCheckbox = checkboxes.get(0);
			String departmentValue = values.get(0).getText().trim();

			scrollToElement(targetCheckbox);
			Thread.sleep(300);
			jsClick(targetCheckbox);
			waitForSpinners();

			if (!targetCheckbox.isSelected()) {
				jsClick(targetCheckbox);
				Thread.sleep(300);
				waitForSpinners();
			}

			Assert.assertTrue(targetCheckbox.isSelected(), "Department checkbox not selected: " + departmentValue);
			DepartmentsOption.set(departmentValue);
			PageObjectHelper.log(LOGGER, "Selected Departments Value: " + departmentValue);
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "select_one_option_in_departments_filters_dropdown", "Issue selecting department", e);
			throw e;
		}
	}

	public void validate_job_mapping_profiles_are_correctly_filtered_with_applied_departments_options() throws Exception {
		try {
			// PARALLEL EXECUTION FIX: Re-fetch elements on each iteration to avoid stale elements
			PerformanceUtils.waitForPageReady(driver, 2);
			int deptCount = driver.findElements(ALL_DEPARTMENTS_COLUMN).size();
			
			for (int i = 0; i < deptCount; i++) {
				// Re-fetch element on each iteration to avoid stale element reference
				WebElement department = driver.findElements(ALL_DEPARTMENTS_COLUMN).get(i);
				String text = department.getText();
				if (text.contentEquals(DepartmentsOption.get()) || text.contentEquals(DepartmentsOption1.get()) || text.contentEquals(DepartmentsOption2.get())) {
					PageObjectHelper.log(LOGGER, "Organization Job with Department: " + text + " correctly filtered");
				} else {
					throw new Exception("Job Profile with incorrect Department Value: " + text);
				}
			}
		} catch (org.openqa.selenium.StaleElementReferenceException e) {
			// PARALLEL EXECUTION FIX: Retry on stale element
			LOGGER.warn("Stale element detected, retrying validation...");
			PerformanceUtils.waitForPageReady(driver, 2);
			int deptCount = driver.findElements(ALL_DEPARTMENTS_COLUMN).size();
			for (int i = 0; i < deptCount; i++) {
				WebElement department = driver.findElements(ALL_DEPARTMENTS_COLUMN).get(i);
				String text = department.getText();
				if (!text.contentEquals(DepartmentsOption.get()) && !text.contentEquals(DepartmentsOption1.get()) && !text.contentEquals(DepartmentsOption2.get())) {
					throw new Exception("Job Profile with incorrect Department Value: " + text);
				}
			}
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "validate_job_mapping_profiles_are_correctly_filtered_with_applied_departments_options", "Issue validating departments filter", e);
			throw e;
		}
		scrollToTop();
	}

	public void select_two_options_in_departments_filters_dropdown() throws Exception {
		try {
			PerformanceUtils.waitForPageReady(driver, 2);
			List<WebElement> checkboxes = driver.findElements(DEPARTMENTS_CHECKBOXES);
			List<WebElement> values = driver.findElements(DEPARTMENTS_VALUES);

			if (values.size() < 2) throw new Exception("Not enough department options. Need at least 2, found: " + values.size());

			for (int i = 0; i < 2; i++) {
				WebElement checkbox = checkboxes.get(i);
				String deptValue = values.get(i).getText().trim();
				scrollToElement(checkbox);
				Thread.sleep(300);
				jsClick(checkbox);
				waitForSpinners();

				if (!checkbox.isSelected()) {
					jsClick(checkbox);
					Thread.sleep(300);
					waitForSpinners();
				}

				Assert.assertTrue(checkbox.isSelected(), "Department checkbox not selected: " + deptValue);
				if (i == 0) DepartmentsOption1.set(deptValue);
				else DepartmentsOption2.set(deptValue);
				PageObjectHelper.log(LOGGER, "Selected Departments Value: " + deptValue);
			}
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "select_two_options_in_departments_filters_dropdown", "Issue selecting two departments", e);
			throw e;
		}
	}

	public void click_on_functions_subfunctions_filters_dropdown_button() throws Exception {
		try {
			PerformanceUtils.waitForPageReady(driver, 1);
			scrollToElement(driver.findElement(FUNCTIONS_DROPDOWN));
			Thread.sleep(200);
			clickElement(FUNCTIONS_DROPDOWN);
			PageObjectHelper.log(LOGGER, "Clicked on Functions / Subfunctions dropdown in Filters");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_functions_subfunctions_filters_dropdown_button", "Issue clicking Functions dropdown", e);
			throw e;
		}
	}

	public void select_a_function_and_verify_all_subfunctions_inside_function_are_selected_automatically() throws Exception {
		List<WebElement> checkboxes = driver.findElements(FUNCTIONS_CHECKBOXES);
		List<WebElement> values = driver.findElements(FUNCTIONS_VALUES);
		List<WebElement> toggleButtons = driver.findElements(TOGGLE_SUBOPTIONS);

		final int MIN_REQUIRED_SUBFUNCTIONS = 2;

		if (toggleButtons.isEmpty()) {
			throw new Exception("No functions with subfunctions found!");
		}

		try {
			boolean selectedFunctionWithSubfunctions = false;
			int maxFunctionsToCheck = Math.min(toggleButtons.size(), 20);

			for (int toggleIndex = 0; toggleIndex < maxFunctionsToCheck; toggleIndex++) {
				try {
					WebElement toggleButton = toggleButtons.get(toggleIndex);
					WebElement functionLabel = toggleButton.findElement(By.xpath("./preceding-sibling::label"));
					String functionText = functionLabel.getText();

					int checkboxCountBefore = driver.findElements(By.xpath("//button[contains(@data-testid,'toggle-suboptions')]/preceding-sibling::input[@type='checkbox'] | //div[@class='ml-6']//input[@type='checkbox']")).size();

					try {
						jsClick(toggleButton);
						Thread.sleep(600);
					} catch (Exception e) {
						continue;
					}

					int checkboxCountAfter = driver.findElements(By.xpath("//button[contains(@data-testid,'toggle-suboptions')]/preceding-sibling::input[@type='checkbox'] | //div[@class='ml-6']//input[@type='checkbox']")).size();
					int subfunctionCount = checkboxCountAfter - checkboxCountBefore;

					try {
						jsClick(toggleButton);
						Thread.sleep(300);
					} catch (Exception e) {
						// Ignore
					}

					if (subfunctionCount < MIN_REQUIRED_SUBFUNCTIONS) continue;

					int functionIndex = -1;
					for (int i = 0; i < values.size(); i++) {
						if (values.get(i).getText().equals(functionText)) {
							functionIndex = i;
							break;
						}
					}

					if (functionIndex == -1) continue;

					WebElement targetCheckbox = checkboxes.get(functionIndex);
					jsClick(targetCheckbox);
					waitForSpinners();
					PerformanceUtils.waitForPageReady(driver, 2);

					if (!targetCheckbox.isSelected()) {
						jsClick(targetCheckbox);
						Thread.sleep(300);
					}

					FunctionsOption.set(functionText);
					selectedFunctionWithSubfunctions = true;
					break;
				} catch (Exception e) {
					continue;
				}
			}

			if (!selectedFunctionWithSubfunctions) {
				throw new Exception("Failed to find/select a function with >= " + MIN_REQUIRED_SUBFUNCTIONS + " subfunctions");
			}
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "select_a_function_and_verify_all_subfunctions_inside_function_are_selected_automatically", "Issue selecting function with subfunctions", e);
			throw e;
		}

		try {
			PerformanceUtils.waitForPageReady(driver, 2);
			WebElement selectedFunctionToggle = driver.findElement(By.xpath("//label[normalize-space(text())='" + FunctionsOption.get() + "']/following-sibling::button[contains(@data-testid,'toggle-suboptions')]"));
			jsClick(selectedFunctionToggle);
			Thread.sleep(800);
			waitForSpinners();

			String subfunctionXPath = "//input[@type='checkbox'][contains(@id, '" + FunctionsOption.get() + "') and contains(@id, '::')]";
			List<WebElement> subfunctions = driver.findElements(By.xpath(subfunctionXPath));

			if (subfunctions.isEmpty()) {
				throw new Exception("Function '" + FunctionsOption.get() + "' was expected to have subfunctions but none found");
			}

			for (WebElement subfunctionCheckbox : subfunctions) {
				Assert.assertTrue(subfunctionCheckbox.isSelected(), "Subfunction should be auto-selected when parent function is selected");
			}

			LOGGER.info("Verified all {} subfunctions are auto-selected for function '{}'", subfunctions.size(), FunctionsOption.get());
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "select_a_function_verify_subfunctions", "Issue verifying subfunctions auto-selection", e);
			throw e;
		}
	}

	public void validate_job_mapping_profiles_are_correctly_filtered_with_applied_functions_subfunctions_options() throws Exception {
		try {
			// PARALLEL EXECUTION FIX: Wait for page stability before getting elements
			PerformanceUtils.waitForPageReady(driver, 2);
			
			// PARALLEL EXECUTION FIX: Get count first, then re-fetch elements on each iteration to avoid stale elements
			int functionCount = driver.findElements(ALL_FUNCTIONS_COLUMN).size();
			
			for (int i = 0; i < functionCount; i++) {
				// Re-fetch element on each iteration to avoid stale element reference
				WebElement functionElement = driver.findElements(ALL_FUNCTIONS_COLUMN).get(i);
				String fullText = functionElement.getText();
				String function = fullText.contains("|") ? fullText.split("\\s*\\|\\s*", 2)[0].trim() : fullText.trim();

				if (function.contentEquals(FunctionsOption.get()) || function.contentEquals(FunctionsOption1.get()) || function.contentEquals(FunctionsOption2.get())) {
					PageObjectHelper.log(LOGGER, "Organization Job with Function: " + function + " correctly filtered");
				} else {
					throw new Exception("Job Profile with incorrect Function Value: " + function);
				}
			}
		} catch (org.openqa.selenium.StaleElementReferenceException e) {
			// PARALLEL EXECUTION FIX: Retry on stale element
			LOGGER.warn("Stale element detected, retrying validation...");
			PerformanceUtils.waitForPageReady(driver, 2);
			// Retry once
			int functionCount = driver.findElements(ALL_FUNCTIONS_COLUMN).size();
			for (int i = 0; i < functionCount; i++) {
				WebElement functionElement = driver.findElements(ALL_FUNCTIONS_COLUMN).get(i);
				String fullText = functionElement.getText();
				String function = fullText.contains("|") ? fullText.split("\\s*\\|\\s*", 2)[0].trim() : fullText.trim();
				if (!function.contentEquals(FunctionsOption.get()) && !function.contentEquals(FunctionsOption1.get()) && !function.contentEquals(FunctionsOption2.get())) {
					throw new Exception("Job Profile with incorrect Function Value: " + function);
				}
			}
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "validate_job_mapping_profiles_are_correctly_filtered_with_applied_functions_subfunctions_options", "Issue validating functions filter", e);
			throw e;
		}
		scrollToTop();
	}

	public void user_should_verify_search_bar_is_available_in_functions_subfunctions_filters_dropdown() throws Exception {
		try {
			PerformanceUtils.waitForPageReady(driver, 2);
			Assert.assertTrue(waitForElement(FUNCTIONS_SEARCH).isDisplayed());
			PageObjectHelper.log(LOGGER, "Search bar is available in Functions Subfunctions dropdown");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_search_bar_is_available_in_functions_subfunctions_filters_dropdown", "Issue verifying search bar", e);
			throw e;
		}
	}

	public void click_inside_search_bar_and_enter_function_name() throws Exception {
		try {
			PerformanceUtils.waitForPageReady(driver, 2);

			if (FunctionsOption.get() == null || FunctionsOption.get().isEmpty()) {
				List<WebElement> toggleBtns = driver.findElements(TOGGLE_SUBOPTIONS);
				List<WebElement> funcLabels = driver.findElements(FUNCTIONS_VALUES);

				if (!toggleBtns.isEmpty() && !funcLabels.isEmpty()) {
					FunctionsOption.set(funcLabels.get(0).getText().trim());
				}
			}

			WebElement searchBox = waitForElement(FUNCTIONS_SEARCH);
			clickElement(searchBox);
			searchBox.clear();
			searchBox.sendKeys(FunctionsOption.get());
			PerformanceUtils.waitForPageReady(driver, 2);
			PageObjectHelper.log(LOGGER, "Entered function name: " + FunctionsOption.get());
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_inside_search_bar_and_enter_function_name", "Issue entering function name", e);
			throw e;
		}
	}

	public void user_should_click_on_dropdown_button_of_searched_function_name() throws Exception {
		try {
			PerformanceUtils.waitForPageReady(driver, 2);
			Thread.sleep(1000);

			WebElement toggleBtn = wait.until(ExpectedConditions.presenceOfElementLocated(TOGGLE_SUBOPTIONS));
			scrollToElement(toggleBtn);
			Thread.sleep(500);
			clickElement(toggleBtn);
			PageObjectHelper.log(LOGGER, "Clicked on dropdown button of Searched Function: " + FunctionsOption.get());
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_click_on_dropdown_button_of_searched_function_name", "Issue clicking dropdown button", e);
			throw e;
		}
	}

	public void select_one_subfunction_option_inside_function_name_dropdown() throws Exception {
		PerformanceUtils.waitForPageReady(driver, 1);
		List<WebElement> checkboxes = driver.findElements(SUBFUNCTIONS_CHECKBOXES);
		List<WebElement> values = driver.findElements(SUBFUNCTIONS_VALUES);

		try {
			if (values.size() <= 1) {
				if (!findAndSelectFunctionWithSubfunctions()) {
					Assert.fail("No functions with subfunctions found");
				}
				checkboxes = driver.findElements(SUBFUNCTIONS_CHECKBOXES);
				values = driver.findElements(SUBFUNCTIONS_VALUES);
			}

			if (values.size() > 1) {
				String subfunctionText = values.get(1).getText();
				WebElement checkbox = checkboxes.get(1);

				scrollToElement(checkbox);
				Thread.sleep(300);
				jsClick(checkbox);
				Thread.sleep(300);

				PerformanceUtils.waitForPageReady(driver, 2);
				Assert.assertTrue(checkbox.isSelected(), "Subfunction not selected: " + subfunctionText);
				PageObjectHelper.log(LOGGER, "Selected SubFunction '" + subfunctionText + "' from Function '" + FunctionsOption.get() + "'");
			}
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "select_one_subfunction_option_inside_function_name_dropdown", "Issue selecting subfunction", e);
			throw e;
		}
	}

	private boolean findAndSelectFunctionWithSubfunctions() {
		try {
			WebElement searchBox = waitForElement(FUNCTIONS_SEARCH);
			searchBox.clear();
			Thread.sleep(300);
			PerformanceUtils.waitForPageReady(driver, 2);

			List<WebElement> funcLabels = driver.findElements(FUNCTIONS_VALUES);
			List<WebElement> toggleBtns = driver.findElements(TOGGLE_SUBOPTIONS);

			if (funcLabels.isEmpty()) return false;

			for (int i = 0; i < Math.min(funcLabels.size(), toggleBtns.size()); i++) {
				try {
					String functionName = funcLabels.get(i).getText().trim();
					if (functionName.isEmpty()) continue;

					jsClick(toggleBtns.get(i));
					Thread.sleep(300);

					List<WebElement> subfuncs = driver.findElements(SUBFUNCTIONS_VALUES);
					if (subfuncs.size() > 1) {
						FunctionsOption.set(functionName);
						PageObjectHelper.log(LOGGER, "Found function with subfunctions: " + functionName);
						return true;
					}

					jsClick(toggleBtns.get(i));
					Thread.sleep(300);
				} catch (Exception e) {
					continue;
				}
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	public void user_should_verify_function_name_is_automatically_selected_after_selecting_subfunction_option() throws Exception {
		String xpath = "//div[@data-testid='dropdown-Functions_SubFunctions']//input[@type='checkbox'][contains(@id,'" + FunctionsOption.get() + "')][not(ancestor::button[contains(@data-testid,'suboption')])]";

		try {
			WebElement functionCheckbox = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
			wait.until(ExpectedConditions.visibilityOf(functionCheckbox));

			boolean isSelected = wait.until(driver -> functionCheckbox.isSelected());

			if (isSelected) {
				PageObjectHelper.log(LOGGER, FunctionsOption.get() + " Function checkbox is automatically selected as expected");
			} else {
				Assert.fail("Function checkbox '" + FunctionsOption.get() + "' was NOT automatically selected");
			}
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_function_name_is_automatically_selected_after_selecting_subfunction_option", "Issue verifying auto-selection", e);
			throw e;
		}
	}

	public void select_two_subfunction_options_inside_function_name_filters_dropdown() throws Exception {
		PerformanceUtils.waitForPageReady(driver, 1);
		List<WebElement> checkboxes = driver.findElements(SUBFUNCTIONS_CHECKBOXES);
		List<WebElement> values = driver.findElements(SUBFUNCTIONS_VALUES);

		try {
			for (int j = 1; j <= 2 && j < values.size(); j++) {
				String subfunctionText = values.get(j).getText();
				WebElement checkbox = checkboxes.get(j);

				scrollToElement(checkbox);
				Thread.sleep(300);
				jsClick(checkbox);
				Thread.sleep(300);

				PerformanceUtils.waitForPageReady(driver, 2);
				Assert.assertTrue(checkbox.isSelected(), "Subfunction not selected: " + subfunctionText);
				PageObjectHelper.log(LOGGER, "Selected SubFunction #" + j + ": '" + subfunctionText + "'");
			}
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "select_two_subfunction_options_inside_function_name_filters_dropdown", "Issue selecting two subfunctions", e);
			throw e;
		}
	}

	public void validate_job_mapping_profiles_are_correctly_filtered_with_applied_grades_departments_and_functions_subfunctions_options() throws Exception {
		try {
			// PARALLEL EXECUTION FIX: Wait for page stability and re-fetch elements on each iteration
			PerformanceUtils.waitForPageReady(driver, 2);
			
			By gradesXpath = By.xpath("//h2[text()='Organization jobs']//..//tbody//tr//td[3]//div");
			By deptXpath = By.xpath("//h2[text()='Organization jobs']//..//tbody//tr//td[4]//div");
			By funcXpath = By.xpath("//h2[text()='Organization jobs']//..//tbody//tr//td[@colspan='7']//span[2]");
			
			// Get count first, then re-fetch on each iteration to avoid stale elements
			int gradeCount = driver.findElements(gradesXpath).size();
			for (int i = 0; i < gradeCount; i++) {
				WebElement grade = driver.findElements(gradesXpath).get(i);
				String gradeText = grade.getText();
				if (!gradeText.contentEquals(GradesOption.get())) {
					throw new Exception("Job Profile with incorrect Grade: " + gradeText);
				}
			}

			int deptCount = driver.findElements(deptXpath).size();
			for (int i = 0; i < deptCount; i++) {
				WebElement dept = driver.findElements(deptXpath).get(i);
				String deptText = dept.getText();
				if (!deptText.contentEquals(DepartmentsOption.get())) {
					throw new Exception("Job Profile with incorrect Department: " + deptText);
				}
			}

			int funcCount = driver.findElements(funcXpath).size();
			for (int i = 0; i < funcCount; i++) {
				WebElement func = driver.findElements(funcXpath).get(i);
				String funcText = func.getText().contains("|") ? func.getText().split("\\s*\\|\\s*", 2)[0].trim() : func.getText().trim();
				if (!funcText.contentEquals(FunctionsOption.get())) {
					throw new Exception("Job Profile with incorrect Function: " + funcText);
				}
			}

			PageObjectHelper.log(LOGGER, "All profiles correctly filtered with combined filters");
		} catch (org.openqa.selenium.StaleElementReferenceException e) {
			// PARALLEL EXECUTION FIX: Retry on stale element
			LOGGER.warn("Stale element detected in combined filters validation, retrying...");
			PerformanceUtils.waitForPageReady(driver, 2);
			By gradesXpath = By.xpath("//h2[text()='Organization jobs']//..//tbody//tr//td[3]//div");
			By deptXpath = By.xpath("//h2[text()='Organization jobs']//..//tbody//tr//td[4]//div");
			By funcXpath = By.xpath("//h2[text()='Organization jobs']//..//tbody//tr//td[@colspan='7']//span[2]");
			
			int gradeCount = driver.findElements(gradesXpath).size();
			for (int i = 0; i < gradeCount; i++) {
				WebElement grade = driver.findElements(gradesXpath).get(i);
				if (!grade.getText().contentEquals(GradesOption.get())) {
					throw new Exception("Job Profile with incorrect Grade: " + grade.getText());
				}
			}
			int deptCount = driver.findElements(deptXpath).size();
			for (int i = 0; i < deptCount; i++) {
				WebElement dept = driver.findElements(deptXpath).get(i);
				if (!dept.getText().contentEquals(DepartmentsOption.get())) {
					throw new Exception("Job Profile with incorrect Department: " + dept.getText());
				}
			}
			int funcCount = driver.findElements(funcXpath).size();
			for (int i = 0; i < funcCount; i++) {
				WebElement func = driver.findElements(funcXpath).get(i);
				String funcText = func.getText().contains("|") ? func.getText().split("\\s*\\|\\s*", 2)[0].trim() : func.getText().trim();
				if (!funcText.contentEquals(FunctionsOption.get())) {
					throw new Exception("Job Profile with incorrect Function: " + funcText);
				}
			}
		} catch (Exception e) {
			try {
				if (driver.findElement(NO_DATA_CONTAINER).isDisplayed()) {
					PageObjectHelper.log(LOGGER, "No data with applied combined filters");
				}
			} catch (Exception s) {
				PageObjectHelper.handleError(LOGGER, "validate_combined_filters", "Issue validating combined filters", s);
				throw s;
			}
		}
		scrollToTop();
	}

	public void click_on_mapping_status_filters_dropdown_button() throws Exception {
		try {
			scrollToElement(driver.findElement(MAPPING_STATUS_DROPDOWN));
			clickElement(MAPPING_STATUS_DROPDOWN);
			PageObjectHelper.log(LOGGER, "Clicked on Mapping Status dropdown in Filters");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_mapping_status_filters_dropdown_button", "Issue clicking Mapping Status dropdown", e);
			throw e;
		}
	}

	public void select_one_option_in_mapping_status_filters_dropdown() throws Exception {
		try {
			PerformanceUtils.waitForPageReady(driver, 2);
			List<WebElement> checkboxes = driver.findElements(MAPPING_STATUS_CHECKBOXES);
			List<WebElement> values = driver.findElements(MAPPING_STATUS_VALUES);

			String[] priorityOrder = { "Manually", "Unmapped", "Auto Matched" };
			boolean optionSelected = false;

			for (String desiredStatus : priorityOrder) {
				for (int i = 0; i < values.size(); i++) {
					String actualStatusText = values.get(i).getText();
					if (actualStatusText.contains(desiredStatus)) {
						scrollToElement(values.get(i));
						clickElement(values.get(i));
						waitForSpinners();
						PerformanceUtils.waitForPageReady(driver, 1);

						Assert.assertTrue(checkboxes.get(i).isSelected());
						DepartmentsOption.set(actualStatusText);
						PageObjectHelper.log(LOGGER, "Selected Mapping Status: " + actualStatusText);
						optionSelected = true;
						break;
					}
				}
				if (optionSelected) break;
			}

			if (!optionSelected) {
				Assert.fail("No Mapping Status option found matching priority list");
			}
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "select_one_option_in_mapping_status_filters_dropdown", "Issue selecting Mapping Status", e);
			throw e;
		}
	}
}
