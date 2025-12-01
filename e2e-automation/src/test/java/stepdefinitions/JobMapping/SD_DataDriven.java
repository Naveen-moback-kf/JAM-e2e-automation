package stepdefinitions.JobMapping;

import java.io.IOException;
import java.util.Map;

import com.kfonetalentsuite.manager.PageObjectManager;
import com.kfonetalentsuite.utils.common.ExcelDataProvider;
import com.kfonetalentsuite.webdriverManager.DriverManager;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * ============================================================================
 * DATA-DRIVEN STEP DEFINITIONS
 * ============================================================================
 * 
 * Generic step definitions for Excel data-driven testing.
 * Use these steps in your feature files to perform data-driven tests.
 * 
 * ============================================================================
 * USAGE IN FEATURE FILES
 * ============================================================================
 * 
 * # Search using Excel data
 * Then Search using Excel test data "S001"
 * 
 * # Login using Excel data
 * When User logs in using Excel test data "L001"
 * 
 * # Apply filter using Excel data
 * Then Apply filter using Excel test data "F001"
 * 
 * # Apply sorting using Excel data
 * Then Apply sorting using Excel test data "SORT001"
 * 
 * ============================================================================
 * 
 * @author Automation Team
 */
public class SD_DataDriven extends DriverManager {

    PageObjectManager pageObjectManager = new PageObjectManager();

    public SD_DataDriven() {
        super();
    }

    // ========================================================================
    // SEARCH - DATA DRIVEN
    // ========================================================================

    /**
     * Search using Excel test data.
     * Sheet: SearchData
     * Required columns: TestID, SearchTerm
     */
    @Then("Search using Excel test data {string}")
    public void search_using_excel_test_data(String testId) throws IOException {
        pageObjectManager.getVerifyJobMappingPageComponents().search_using_excel_data(testId);
    }

    // ========================================================================
    // LOGIN - DATA DRIVEN
    // ========================================================================

    /**
     * Login using Excel test data.
     * Sheet: LoginData
     * Required columns: TestID, UserType, Username, Password
     */
    @When("User logs in using Excel test data {string}")
    public void user_logs_in_using_excel_test_data(String testId) throws IOException {
        pageObjectManager.getKFoneLogin().login_using_excel_data(testId);
    }

    // ========================================================================
    // FILTER - DATA DRIVEN
    // ========================================================================

    /**
     * Apply filter using Excel test data.
     * Sheet: FilterData
     * Required columns: TestID, FilterType, FilterValue
     */
    @Then("Apply filter using Excel test data {string}")
    public void apply_filter_using_excel_test_data(String testId) throws IOException {
        pageObjectManager.getValidateJobMappingFiltersFunctionality().apply_filter_using_excel_data(testId);
    }

    // ========================================================================
    // SORTING - DATA DRIVEN
    // ========================================================================

    /**
     * Apply sorting using Excel test data.
     * Sheet: SortingData
     * Required columns: TestID, Column, SortOrder
     */
    @Then("Apply sorting using Excel test data {string}")
    public void apply_sorting_using_excel_test_data(String testId) throws IOException {
        pageObjectManager.getValidateSortingFunctionality_JAM().apply_sorting_using_excel_data(testId);
    }

    // ========================================================================
    // GENERIC DATA ACCESS STEPS
    // ========================================================================

    /**
     * Load test data from any sheet for use in subsequent steps.
     * Stores data in a thread-local variable for use in the same scenario.
     */
    private static ThreadLocal<Map<String, String>> currentTestData = new ThreadLocal<>();

    @Given("Test data is loaded from sheet {string} with TestID {string}")
    public void test_data_is_loaded(String sheetName, String testId) {
        Map<String, String> data = ExcelDataProvider.getTestData(sheetName, testId);
        currentTestData.set(data);
        System.out.println("Loaded test data: " + data);
    }

    @Then("Verify loaded test data has {string} as {string}")
    public void verify_loaded_test_data(String columnName, String expectedValue) {
        Map<String, String> data = currentTestData.get();
        String actualValue = data.get(columnName);
        if (!expectedValue.equals(actualValue)) {
            throw new AssertionError("Expected " + columnName + " to be '" + expectedValue 
                    + "' but was '" + actualValue + "'");
        }
    }

    /**
     * Get the currently loaded test data (for use in other step definitions)
     */
    public static Map<String, String> getCurrentTestData() {
        return currentTestData.get();
    }

    /**
     * Clear the current test data (call in @After hooks if needed)
     */
    public static void clearCurrentTestData() {
        currentTestData.remove();
    }
}

