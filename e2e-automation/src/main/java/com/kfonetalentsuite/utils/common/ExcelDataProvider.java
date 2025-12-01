package com.kfonetalentsuite.utils.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * ============================================================================
 * EXCEL DATA PROVIDER - Data-Driven Testing Utility
 * ============================================================================
 * 
 * A simple, reusable utility for reading test data from Excel files.
 * Designed for easy adoption by all teams in the organization.
 * 
 * ============================================================================
 * QUICK START GUIDE
 * ============================================================================
 * 
 * 1. CREATE YOUR EXCEL FILE:
 *    Location: src/test/resources/testdata/TestData.xlsx
 *    - First row = Column headers (e.g., TestID, Username, Password)
 *    - Each subsequent row = One test case
 *    - "TestID" column is required for lookup by ID
 * 
 * 2. BASIC USAGE IN YOUR CODE:
 * 
 *    // Get all rows from a sheet
 *    List<Map<String, String>> allData = ExcelDataProvider.getSheetData("LoginData");
 *    
 *    // Get specific row by TestID
 *    Map<String, String> testData = ExcelDataProvider.getTestData("LoginData", "TC001");
 *    String username = testData.get("Username");
 *    
 *    // Get single value directly
 *    String password = ExcelDataProvider.getValue("LoginData", "TC001", "Password");
 * 
 * 3. USE IN CUCUMBER STEP DEFINITION:
 * 
 *    @When("User logs in with test data {string}")
 *    public void login_with_test_data(String testId) {
 *        Map<String, String> data = ExcelDataProvider.getTestData("LoginData", testId);
 *        loginPage.login(data.get("Username"), data.get("Password"));
 *    }
 * 
 * 4. USE IN TESTNG DATA PROVIDER:
 * 
 *    @DataProvider(name = "loginTests")
 *    public Object[][] getLoginData() {
 *        return ExcelDataProvider.createDataProvider("LoginData");
 *    }
 *    
 *    @Test(dataProvider = "loginTests")
 *    public void testLogin(Map<String, String> testData) {
 *        String username = testData.get("Username");
 *        // ... test logic
 *    }
 * 
 * ============================================================================
 * EXCEL FILE STRUCTURE EXAMPLE
 * ============================================================================
 * 
 * Sheet: LoginData
 * +--------+------------------+------------+--------+
 * | TestID | Username         | Password   | Role   |
 * +--------+------------------+------------+--------+
 * | TC001  | admin@test.com   | Pass123!   | Admin  |
 * | TC002  | user@test.com    | User456!   | User   |
 * +--------+------------------+------------+--------+
 * 
 * ============================================================================
 * 
 * @author Automation Team
 * @version 2.0
 */
public class ExcelDataProvider {

    private static final Logger LOGGER = LogManager.getLogger(ExcelDataProvider.class);
    
    // Default test data file location
    private static final String DEFAULT_TEST_DATA_PATH = "src/test/resources/testdata/";
    private static final String DEFAULT_TEST_DATA_FILE = "TestData.xlsx";
    
    // Thread-safe storage for custom file paths (for teams with different data files)
    private static ThreadLocal<String> customFilePath = new ThreadLocal<>();

    // ========================================================================
    // CONFIGURATION METHODS
    // ========================================================================

    /**
     * Set a custom Excel file path for the current thread.
     * Useful when different teams have their own test data files.
     * 
     * Example:
     *   ExcelDataProvider.setCustomFilePath("src/test/resources/myteam/MyTestData.xlsx");
     *   Map<String, String> data = ExcelDataProvider.getTestData("MySheet", "TC001");
     * 
     * @param filePath Full path to the Excel file
     */
    public static void setCustomFilePath(String filePath) {
        customFilePath.set(filePath);
        LOGGER.info("Custom Excel file path set: {}", filePath);
    }

    /**
     * Clear custom file path and use default
     */
    public static void clearCustomFilePath() {
        customFilePath.remove();
    }

    /**
     * Get the current Excel file path being used
     */
    public static String getCurrentFilePath() {
        String custom = customFilePath.get();
        return (custom != null) ? custom : DEFAULT_TEST_DATA_PATH + DEFAULT_TEST_DATA_FILE;
    }

    // ========================================================================
    // MAIN DATA RETRIEVAL METHODS
    // ========================================================================

    /**
     * Get all rows from a sheet as List of Maps.
     * Each map represents one row with column headers as keys.
     * 
     * @param sheetName Name of the Excel sheet
     * @return List of Maps (each map = one row)
     * 
     * Example:
     *   List<Map<String, String>> users = ExcelDataProvider.getSheetData("Users");
     *   for (Map<String, String> user : users) {
     *       System.out.println(user.get("Username"));
     *   }
     */
    public static List<Map<String, String>> getSheetData(String sheetName) {
        String filePath = getCurrentFilePath();
        List<Map<String, String>> data = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                LOGGER.error("Sheet '{}' not found in file: {}", sheetName, filePath);
                throw new RuntimeException("Sheet not found: " + sheetName + " in file: " + filePath);
            }

            // Get headers from first row
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                LOGGER.warn("Sheet '{}' has no header row", sheetName);
                return data;
            }

            List<String> headers = new ArrayList<>();
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                String header = getCellValue(headerRow.getCell(i));
                headers.add(header);
            }

            // Read data rows (starting from row 1)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Map<String, String> rowData = new HashMap<>();
                boolean hasData = false;

                for (int j = 0; j < headers.size(); j++) {
                    String value = getCellValue(row.getCell(j));
                    rowData.put(headers.get(j), value);
                    if (!value.isEmpty()) hasData = true;
                }

                // Skip completely empty rows
                if (hasData) {
                    data.add(rowData);
                }
            }


        } catch (IOException e) {
            LOGGER.error("Failed to read Excel file: {}", filePath, e);
            throw new RuntimeException("Failed to read Excel file: " + filePath, e);
        }

        return data;
    }

    /**
     * Get a specific row by TestID.
     * The sheet must have a "TestID" column.
     * 
     * @param sheetName Name of the Excel sheet
     * @param testId Value in the TestID column
     * @return Map of column name -> value
     * 
     * Example:
     *   Map<String, String> data = ExcelDataProvider.getTestData("LoginData", "TC001");
     *   String username = data.get("Username");
     *   String password = data.get("Password");
     */
    public static Map<String, String> getTestData(String sheetName, String testId) {
        List<Map<String, String>> allData = getSheetData(sheetName);

        for (Map<String, String> row : allData) {
            String rowTestId = row.get("TestID");
            if (testId.equals(rowTestId)) {
                LOGGER.debug("Found test data for TestID: {} in sheet: {}", testId, sheetName);
                return row;
            }
        }

        LOGGER.error("TestID '{}' not found in sheet '{}'", testId, sheetName);
        throw new RuntimeException("TestID not found: " + testId + " in sheet: " + sheetName);
    }

    /**
     * Get a single value from Excel.
     * Shortcut method for getting one specific value.
     * 
     * @param sheetName Name of the Excel sheet
     * @param testId Value in the TestID column
     * @param columnName Column name to retrieve
     * @return Value from the specified column (empty string if not found)
     * 
     * Example:
     *   String username = ExcelDataProvider.getValue("LoginData", "TC001", "Username");
     */
    public static String getValue(String sheetName, String testId, String columnName) {
        try {
            Map<String, String> row = getTestData(sheetName, testId);
            String value = row.get(columnName);
            return (value != null) ? value : "";
        } catch (Exception e) {
            LOGGER.warn("Could not get value for column '{}' from TestID '{}': {}", 
                    columnName, testId, e.getMessage());
            return "";
        }
    }

    // ========================================================================
    // FILTERING METHODS
    // ========================================================================

    /**
     * Get test data filtered by any column value.
     * Useful for getting subset of data matching certain criteria.
     * 
     * @param sheetName Name of the Excel sheet
     * @param columnName Column to filter by
     * @param columnValue Value to match
     * @return List of matching rows
     * 
     * Example:
     *   // Get all admin users
     *   List<Map<String, String>> admins = 
     *       ExcelDataProvider.getDataByColumn("Users", "Role", "Admin");
     */
    public static List<Map<String, String>> getDataByColumn(
            String sheetName, String columnName, String columnValue) {

        List<Map<String, String>> allData = getSheetData(sheetName);
        List<Map<String, String>> filteredData = new ArrayList<>();

        for (Map<String, String> row : allData) {
            String value = row.get(columnName);
            if (columnValue.equals(value)) {
                filteredData.add(row);
            }
        }

        LOGGER.debug("Found {} rows where {}='{}' in sheet '{}'",
                filteredData.size(), columnName, columnValue, sheetName);

        return filteredData;
    }

    /**
     * Get test data filtered by multiple criteria.
     * 
     * @param sheetName Name of the Excel sheet
     * @param filters Map of column names to expected values
     * @return List of matching rows
     * 
     * Example:
     *   Map<String, String> filters = new HashMap<>();
     *   filters.put("Environment", "QA");
     *   filters.put("UserType", "Admin");
     *   List<Map<String, String>> results = 
     *       ExcelDataProvider.getDataByFilters("Users", filters);
     */
    public static List<Map<String, String>> getDataByFilters(
            String sheetName, Map<String, String> filters) {

        List<Map<String, String>> allData = getSheetData(sheetName);
        List<Map<String, String>> filteredData = new ArrayList<>();

        for (Map<String, String> row : allData) {
            boolean matches = true;
            for (Map.Entry<String, String> filter : filters.entrySet()) {
                String actualValue = row.get(filter.getKey());
                if (!filter.getValue().equals(actualValue)) {
                    matches = false;
                    break;
                }
            }
            if (matches) {
                filteredData.add(row);
            }
        }

        return filteredData;
    }

    // ========================================================================
    // TESTNG DATA PROVIDER METHODS
    // ========================================================================

    /**
     * Create TestNG DataProvider array from a sheet.
     * Each row becomes one test iteration.
     * 
     * @param sheetName Name of the Excel sheet
     * @return Object[][] for TestNG DataProvider
     * 
     * Example in Test Class:
     *   @DataProvider(name = "loginTests")
     *   public Object[][] getLoginData() {
     *       return ExcelDataProvider.createDataProvider("LoginData");
     *   }
     *   
     *   @Test(dataProvider = "loginTests")
     *   public void testLogin(Map<String, String> testData) {
     *       loginPage.login(testData.get("Username"), testData.get("Password"));
     *   }
     */
    public static Object[][] createDataProvider(String sheetName) {
        List<Map<String, String>> data = getSheetData(sheetName);
        Object[][] result = new Object[data.size()][1];

        for (int i = 0; i < data.size(); i++) {
            result[i][0] = data.get(i);
        }

        LOGGER.info("Created DataProvider with {} test cases from sheet '{}'", data.size(), sheetName);
        return result;
    }

    /**
     * Create filtered DataProvider.
     * Only includes rows matching the filter criteria.
     * 
     * @param sheetName Name of the Excel sheet
     * @param filterColumn Column to filter by
     * @param filterValue Value to match
     * @return Object[][] for TestNG DataProvider
     * 
     * Example:
     *   // Only get QA environment test data
     *   return ExcelDataProvider.createFilteredDataProvider("LoginData", "Environment", "QA");
     */
    public static Object[][] createFilteredDataProvider(
            String sheetName, String filterColumn, String filterValue) {

        List<Map<String, String>> data = getDataByColumn(sheetName, filterColumn, filterValue);
        Object[][] result = new Object[data.size()][1];

        for (int i = 0; i < data.size(); i++) {
            result[i][0] = data.get(i);
        }

        return result;
    }

    // ========================================================================
    // UTILITY METHODS
    // ========================================================================

    /**
     * List all available sheets in the Excel file.
     * Helpful for debugging and verification.
     * 
     * @return List of sheet names
     */
    public static List<String> getAvailableSheets() {
        String filePath = getCurrentFilePath();
        List<String> sheets = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                sheets.add(workbook.getSheetName(i));
            }

        } catch (IOException e) {
            LOGGER.error("Failed to read sheets from: {}", filePath, e);
        }

        return sheets;
    }

    /**
     * Check if a sheet exists in the Excel file.
     * 
     * @param sheetName Name of the sheet to check
     * @return true if sheet exists
     */
    public static boolean sheetExists(String sheetName) {
        return getAvailableSheets().contains(sheetName);
    }

    /**
     * Get count of rows in a sheet (excluding header).
     * 
     * @param sheetName Name of the Excel sheet
     * @return Number of data rows
     */
    public static int getRowCount(String sheetName) {
        return getSheetData(sheetName).size();
    }

    /**
     * Convert cell value to String.
     * Handles different cell types safely.
     */
    private static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                double numValue = cell.getNumericCellValue();
                // Return as integer if it's a whole number
                if (numValue == Math.floor(numValue) && !Double.isInfinite(numValue)) {
                    return String.valueOf((long) numValue);
                }
                return String.valueOf(numValue);
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return cell.getStringCellValue().trim();
                } catch (Exception e) {
                    try {
                        return String.valueOf(cell.getNumericCellValue());
                    } catch (Exception ex) {
                        return "";
                    }
                }
            case BLANK:
                return "";
            default:
                return "";
        }
    }

    // ========================================================================
    // CONVENIENCE METHODS FOR COMMON DATA TYPES
    // ========================================================================

    /**
     * Get value as Integer.
     * Returns 0 if value is empty or not a valid number.
     */
    public static int getIntValue(String sheetName, String testId, String columnName) {
        String value = getValue(sheetName, testId, columnName);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Get value as Boolean.
     * Returns true if value is "true", "yes", or "1" (case-insensitive).
     */
    public static boolean getBooleanValue(String sheetName, String testId, String columnName) {
        String value = getValue(sheetName, testId, columnName).toLowerCase();
        return "true".equals(value) || "yes".equals(value) || "1".equals(value);
    }
}
