package com.kfonetalentsuite.utils.JobMapping;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Utility class for handling Excel files containing profiles with missing data.
 * Supports reading, writing, and updating Excel files in the JobCatalogNewFormat structure.
 */
public class ExcelMissingDataHandler {

	private static final Logger LOGGER = LogManager.getLogger(ExcelMissingDataHandler.class);
	
	// Excel structure constants (matching JobCatalogNewFormat)
	private static final int HEADER_ROW = 4; // Row 5 in Excel (0-indexed = 4)
	private static final int DATA_START_ROW = 6; // Row 7 in Excel (0-indexed = 6)
	private static final int JOB_TITLE_COLUMN = 0; // Column A (0-indexed = 0)
	private static final int JOB_CODE_COLUMN = 1; // Column B (0-indexed = 1)
	private static final int JOB_FAMILY_COLUMN = 2; // Column C (0-indexed = 2) - Function
	private static final int JOB_SUBFAMILY_COLUMN = 3; // Column D (0-indexed = 3) - Subfunction
	private static final int JOB_GRADE_COLUMN = 4; // Column E (0-indexed = 4)
	private static final int DEPARTMENT_COLUMN = 10; // Column K (0-indexed = 10)
	private static final int IS_EXECUTIVE_COLUMN = 12; // Column M (0-indexed = 12)

	/**
	 * Creates an Excel file with extracted profiles using JobCatalogNewFormat structure
	 * 
	 * @param profiles List of profile data arrays
	 * @param filePath Output file path
	 * @param templatePath Path to template Excel file (optional)
	 * @return true if successful
	 */
	public static boolean createExcelFileWithProfiles(List<String[]> profiles, String filePath, String templatePath) {
		try {
			if (profiles == null || profiles.isEmpty()) {
				LOGGER.error("No profiles available to create Excel file");
				return false;
			}

			Workbook workbook;
			Sheet sheet;

			// Try to load template
			if (templatePath != null && new File(templatePath).exists()) {
				try (FileInputStream fis = new FileInputStream(templatePath)) {
					workbook = new XSSFWorkbook(fis);
					sheet = workbook.getSheetAt(0);

					// Delete all data rows (keep header)
					int lastRow = sheet.getLastRowNum();
					for (int i = lastRow; i >= DATA_START_ROW; i--) {
						Row row = sheet.getRow(i);
						if (row != null) {
							sheet.removeRow(row);
						}
					}

					LOGGER.info("Using template from: " + templatePath);
				}
			} else {
				LOGGER.warn("Template not found, creating new workbook with headers");
				workbook = new XSSFWorkbook();
				sheet = workbook.createSheet("Profiles");
				createExcelHeaders(sheet);
			}

			// Write profile data
			int rowNum = DATA_START_ROW;
			for (String[] profile : profiles) {
				Row row = sheet.createRow(rowNum++);
				writeProfileToExcelRow(row, profile);
			}

			// Auto-size columns
			for (int i = 0; i < 16; i++) {
				try {
					sheet.autoSizeColumn(i);
				} catch (Exception e) {
					// Ignore auto-size errors
				}
			}

			// Write to file
			try (FileOutputStream fos = new FileOutputStream(filePath)) {
				workbook.write(fos);
			}

			workbook.close();

			LOGGER.info("Excel file created with " + profiles.size() + " profiles at: " + filePath);
			return true;

		} catch (Exception e) {
			LOGGER.error("Failed to create Excel file: " + e.getMessage(), e);
			return false;
		}
	}

	/**
	 * Reads an Excel file and returns profile data
	 * 
	 * @param filePath Path to Excel file
	 * @return List of profile data arrays
	 */
	public static List<String[]> readExcelFile(String filePath) {
		List<String[]> profiles = new ArrayList<>();

		try (FileInputStream fis = new FileInputStream(filePath);
			 Workbook workbook = new XSSFWorkbook(fis)) {

			Sheet sheet = workbook.getSheetAt(0);
			int lastRow = sheet.getLastRowNum();

			for (int i = DATA_START_ROW; i <= lastRow; i++) {
				Row row = sheet.getRow(i);
				if (row == null) continue;

				String[] profile = readProfileFromExcelRow(row);
				profiles.add(profile);
			}

			LOGGER.info("Read " + profiles.size() + " profiles from Excel file");

		} catch (Exception e) {
			LOGGER.error("Failed to read Excel file: " + e.getMessage(), e);
		}

		return profiles;
	}

	/**
	 * Updates missing values in an Excel file
	 * 
	 * @param filePath Path to Excel file
	 * @param defaultGrade Default grade value (e.g., "JGL01")
	 * @param defaultDepartment Default department value (e.g., "Engineering")
	 * @param defaultFunction Default function value (e.g., "General")
	 * @param defaultSubfunction Default subfunction value (e.g., "Operations")
	 * @return true if successful
	 */
	public static boolean fillMissingDataInExcel(String filePath, String defaultGrade, String defaultDepartment, 
												   String defaultFunction, String defaultSubfunction) {
		try (FileInputStream fis = new FileInputStream(filePath);
			 Workbook workbook = new XSSFWorkbook(fis)) {

			Sheet sheet = workbook.getSheetAt(0);
			int lastRow = sheet.getLastRowNum();
			int updatedCount = 0;

			for (int i = DATA_START_ROW; i <= lastRow; i++) {
				Row row = sheet.getRow(i);
				if (row == null) continue;

				boolean updated = false;

				// Fill missing Grade (Column E)
				if (isCellEmpty(row.getCell(JOB_GRADE_COLUMN))) {
					row.createCell(JOB_GRADE_COLUMN).setCellValue(defaultGrade);
					updated = true;
				}

				// Fill missing Department (Column K)
				if (isCellEmpty(row.getCell(DEPARTMENT_COLUMN))) {
					row.createCell(DEPARTMENT_COLUMN).setCellValue(defaultDepartment);
					updated = true;
				}

				// Fill missing Function (Column C)
				if (isCellEmpty(row.getCell(JOB_FAMILY_COLUMN))) {
					row.createCell(JOB_FAMILY_COLUMN).setCellValue(defaultFunction);
					updated = true;
				}

				// Fill missing Subfunction (Column D)
				if (isCellEmpty(row.getCell(JOB_SUBFAMILY_COLUMN))) {
					row.createCell(JOB_SUBFAMILY_COLUMN).setCellValue(defaultSubfunction);
					updated = true;
				}

				if (updated) {
					updatedCount++;
				}
			}

			// Write back to file
			try (FileOutputStream fos = new FileOutputStream(filePath)) {
				workbook.write(fos);
			}

			LOGGER.info("Updated {} profiles with missing data", updatedCount);
			return true;

		} catch (Exception e) {
			LOGGER.error("Failed to fill missing data in Excel: " + e.getMessage(), e);
			return false;
		}
	}

	/**
	 * Creates a backup of the Excel file
	 * 
	 * @param originalFilePath Path to original file
	 * @param backupDir Backup directory path
	 * @return Path to backup file
	 */
	public static String createBackup(String originalFilePath, String backupDir) {
		try {
			File originalFile = new File(originalFilePath);
			if (!originalFile.exists()) {
				LOGGER.warn("Original file does not exist: " + originalFilePath);
				return null;
			}

			// Create backup directory
			Path backupDirPath = Paths.get(backupDir);
			if (!Files.exists(backupDirPath)) {
				Files.createDirectories(backupDirPath);
			}

			// Generate backup filename with timestamp
			String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			String backupFileName = "Backup_" + timestamp + "_" + originalFile.getName();
			Path backupFilePath = backupDirPath.resolve(backupFileName);

			// Copy file
			Files.copy(originalFile.toPath(), backupFilePath, StandardCopyOption.REPLACE_EXISTING);

			LOGGER.info("Created backup: " + backupFilePath);
			return backupFilePath.toString();

		} catch (Exception e) {
			LOGGER.error("Failed to create backup: " + e.getMessage(), e);
			return null;
		}
	}

	// ================== PRIVATE HELPER METHODS ==================

	private static void createExcelHeaders(Sheet sheet) {
		Row headerRow = sheet.createRow(HEADER_ROW);
		String[] headers = {
			"Client Job Title*",
			"Client Job Code*",
			"Client Job Family / Function (recommended)",
			"Client Sub Family / Function (recommended)",
			"Client Job Grade / Reference Level",
			"Industry",
			"Sector",
			"Korn Ferry Function (recommended)",
			"Korn Ferry Sub Function (recommended)",
			"Korn Ferry Grade / Reference Level (recommended)",
			"Client Department                                (recommended)",
			"Client Sub-Department",
			"Is Executive (recommended)",
			"Level",
			"Reporting Manager Client Job Title (recommended)",
			"Reporting Manager Client Job Code (recommended)"
		};

		CellStyle headerStyle = sheet.getWorkbook().createCellStyle();
		Font headerFont = sheet.getWorkbook().createFont();
		headerFont.setBold(true);
		headerStyle.setFont(headerFont);

		for (int i = 0; i < headers.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(headers[i]);
			cell.setCellStyle(headerStyle);
		}
	}

	private static void writeProfileToExcelRow(Row row, String[] profile) {
		// profile[] = {jobCode, jobTitle, department, jobFamily, jobSubFamily, jobGrade, isExecutive}
		// Map to Excel columns: A=Title, B=Code, C=Family, D=SubFamily, E=Grade, K=Department, M=IsExecutive

		row.createCell(JOB_TITLE_COLUMN).setCellValue(safeGetValue(profile, 1)); // Job Title -> Column A
		row.createCell(JOB_CODE_COLUMN).setCellValue(safeGetValue(profile, 0)); // Job Code -> Column B
		row.createCell(JOB_FAMILY_COLUMN).setCellValue(safeGetValue(profile, 3)); // Job Family -> Column C
		row.createCell(JOB_SUBFAMILY_COLUMN).setCellValue(safeGetValue(profile, 4)); // Job SubFamily -> Column D
		row.createCell(JOB_GRADE_COLUMN).setCellValue(safeGetValue(profile, 5)); // Job Grade -> Column E
		row.createCell(DEPARTMENT_COLUMN).setCellValue(safeGetValue(profile, 2)); // Department -> Column K
		row.createCell(IS_EXECUTIVE_COLUMN).setCellValue(safeGetValue(profile, 6)); // Is Executive -> Column M
	}

	private static String[] readProfileFromExcelRow(Row row) {
		// Returns: {jobCode, jobTitle, department, jobFamily, jobSubFamily, jobGrade, isExecutive}
		String[] profile = new String[7];

		profile[0] = getCellValueAsString(row.getCell(JOB_CODE_COLUMN)); // Job Code
		profile[1] = getCellValueAsString(row.getCell(JOB_TITLE_COLUMN)); // Job Title
		profile[2] = getCellValueAsString(row.getCell(DEPARTMENT_COLUMN)); // Department
		profile[3] = getCellValueAsString(row.getCell(JOB_FAMILY_COLUMN)); // Job Family
		profile[4] = getCellValueAsString(row.getCell(JOB_SUBFAMILY_COLUMN)); // Job SubFamily
		profile[5] = getCellValueAsString(row.getCell(JOB_GRADE_COLUMN)); // Job Grade
		profile[6] = getCellValueAsString(row.getCell(IS_EXECUTIVE_COLUMN)); // Is Executive

		return profile;
	}

	private static String getCellValueAsString(Cell cell) {
		if (cell == null) {
			return "";
		}

		switch (cell.getCellType()) {
			case STRING:
				return cell.getStringCellValue().trim();
			case NUMERIC:
				return String.valueOf((int) cell.getNumericCellValue());
			case BOOLEAN:
				return String.valueOf(cell.getBooleanCellValue()).toUpperCase();
			case FORMULA:
				try {
					return cell.getStringCellValue().trim();
				} catch (Exception e) {
					return String.valueOf(cell.getNumericCellValue());
				}
			default:
				return "";
		}
	}

	private static boolean isCellEmpty(Cell cell) {
		if (cell == null) {
			return true;
		}

		String value = getCellValueAsString(cell).trim();
		return value.isEmpty() || value.equals("-") || value.equalsIgnoreCase("N/A");
	}

	private static String safeGetValue(String[] array, int index) {
		if (array == null || index < 0 || index >= array.length) {
			return "";
		}
		return array[index] == null ? "" : array[index];
	}
}
