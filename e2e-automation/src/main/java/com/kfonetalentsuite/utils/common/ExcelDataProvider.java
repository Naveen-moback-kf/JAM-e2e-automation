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

    public static void setCustomFilePath(String filePath) {
        customFilePath.set(filePath);
        LOGGER.info("Custom Excel file path set: {}", filePath);
    }

    public static void clearCustomFilePath() {
        customFilePath.remove();
    }

    public static String getCurrentFilePath() {
        String custom = customFilePath.get();
        return (custom != null) ? custom : DEFAULT_TEST_DATA_PATH + DEFAULT_TEST_DATA_FILE;
    }

    // ========================================================================
    // MAIN DATA RETRIEVAL METHODS
    // ========================================================================

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

    public static Object[][] createDataProvider(String sheetName) {
        List<Map<String, String>> data = getSheetData(sheetName);
        Object[][] result = new Object[data.size()][1];

        for (int i = 0; i < data.size(); i++) {
            result[i][0] = data.get(i);
        }

        LOGGER.info("Created DataProvider with {} test cases from sheet '{}'", data.size(), sheetName);
        return result;
    }

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

    public static boolean sheetExists(String sheetName) {
        return getAvailableSheets().contains(sheetName);
    }

    public static int getRowCount(String sheetName) {
        return getSheetData(sheetName).size();
    }

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

    public static int getIntValue(String sheetName, String testId, String columnName) {
        String value = getValue(sheetName, testId, columnName);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static boolean getBooleanValue(String sheetName, String testId, String columnName) {
        String value = getValue(sheetName, testId, columnName).toLowerCase();
        return "true".equals(value) || "yes".equals(value) || "1".equals(value);
    }
}
