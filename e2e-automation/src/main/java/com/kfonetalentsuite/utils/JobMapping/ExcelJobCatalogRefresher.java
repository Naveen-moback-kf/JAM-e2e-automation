package com.kfonetalentsuite.utils.JobMapping;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Excel Job Catalog Refresher - Handles XLSX format job catalog files
 * Adds unique timestamps to Job Code and Job Title to prevent duplicate entries
 * 
 * EXCEL FORMAT STRUCTURE:
 * - Row 1: Title
 * - Row 2: Instructions  
 * - Row 3: Empty
 * - Row 4: Empty
 * - Row 5: Headers (Client Job Title*, Client Job Code*, etc.)
 * - Row 6: Field descriptions
 * - Row 7+: Actual data rows
 * 
 * KEY COLUMNS:
 * - Column 1 (A): Client Job Title*
 * - Column 2 (B): Client Job Code*
 */
public class ExcelJobCatalogRefresher {

	private static final Logger LOGGER = LogManager.getLogger(ExcelJobCatalogRefresher.class);
	private static final String EXCEL_FILE_PATH = "src/test/resources/JobCatalogNewFormat-100 Profiles.xlsx";
	private static final String BACKUP_DIR = "src/test/resources/JobCatalogBackups";
	private static boolean hasRefreshedThisSession = false;
	
	// Excel structure constants
	private static final int DATA_START_ROW = 6; // Row 7 in Excel (0-indexed = 6)
	private static final int JOB_TITLE_COLUMN = 0; // Column A (0-indexed = 0)
	private static final int JOB_CODE_COLUMN = 1; // Column B (0-indexed = 1)

	public static synchronized boolean refreshJobCatalog() {
		// Skip if already refreshed in this session
		if (hasRefreshedThisSession) {
			LOGGER.info("Excel Job Catalog already refreshed in this session - skipping");
			return true;
		}

		try {
			LOGGER.info("Refreshing Excel Job Catalog with unique identifiers");

			Path excelPath = Paths.get(EXCEL_FILE_PATH);

			if (!Files.exists(excelPath)) {
				LOGGER.error("Excel file not found: {}", EXCEL_FILE_PATH);
				return false;
			}

			// Create backup before modification
			createBackup(excelPath);

			// Generate unique suffix based on current timestamp
			String uniqueSuffix = generateUniqueTimestamp();

			// Read and update Excel file
			FileInputStream fis = new FileInputStream(excelPath.toFile());
			Workbook workbook = new XSSFWorkbook(fis);
			Sheet sheet = workbook.getSheetAt(0); // First sheet (Job_Catalog)
			fis.close();

			int updatedCount = 0;
			
			// Process all data rows (starting from DATA_START_ROW)
			for (int rowNum = DATA_START_ROW; rowNum <= sheet.getLastRowNum(); rowNum++) {
				Row row = sheet.getRow(rowNum);
				
				if (row == null) {
					continue; // Skip null rows
				}
				
				// Check if row has data (check Job Title cell)
				Cell jobTitleCell = row.getCell(JOB_TITLE_COLUMN);
				if (jobTitleCell == null || getCellValueAsString(jobTitleCell).trim().isEmpty()) {
					continue; // Skip empty rows
				}

				// Update Job Title (Column A)
				String currentJobTitle = getCellValueAsString(jobTitleCell).trim();
				String baseJobTitle = extractBaseJobTitle(currentJobTitle);
				String newJobTitle = String.format("%s %s", baseJobTitle, uniqueSuffix);
				jobTitleCell.setCellValue(newJobTitle);

				// Update Job Code (Column B)
				Cell jobCodeCell = row.getCell(JOB_CODE_COLUMN);
				if (jobCodeCell != null) {
					String currentJobCode = getCellValueAsString(jobCodeCell).trim();
					String baseJobCode = extractBaseJobCode(currentJobCode);
					String newJobCode = String.format("%s%02d-%s", baseJobCode, (rowNum - DATA_START_ROW + 1), uniqueSuffix);
					jobCodeCell.setCellValue(newJobCode);
				}

				updatedCount++;
			}

			// Write updated workbook back to file
			FileOutputStream fos = new FileOutputStream(excelPath.toFile());
			workbook.write(fos);
			fos.close();
			workbook.close();

			hasRefreshedThisSession = true;

			LOGGER.info("✅ Excel Job Catalog refreshed: {} profiles updated with suffix {}", updatedCount, uniqueSuffix);

			return true;

		} catch (Exception e) {
			LOGGER.error("Failed to refresh Excel Job Catalog: {}", e.getMessage(), e);
			return false;
		}
	}

	private static String getCellValueAsString(Cell cell) {
		if (cell == null) {
			return "";
		}

		switch (cell.getCellType()) {
			case STRING:
				return cell.getStringCellValue();
			case NUMERIC:
				// Format numeric values without scientific notation
				double numValue = cell.getNumericCellValue();
				if (numValue == (long) numValue) {
					return String.valueOf((long) numValue);
				} else {
					return String.valueOf(numValue);
				}
			case BOOLEAN:
				return String.valueOf(cell.getBooleanCellValue());
			case FORMULA:
				try {
					return cell.getStringCellValue();
				} catch (Exception e) {
					return cell.getCellFormula();
				}
			case BLANK:
				return "";
			default:
				return "";
		}
	}

	private static String generateUniqueTimestamp() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(new Date());
	}

	private static String extractBaseJobCode(String jobCode) {
		// Remove timestamp suffix (e.g., "-20251023143025" or similar patterns)
		String withoutTimestamp = jobCode.replaceAll("-\\d{10,}$", "");

		// Remove trailing digits to get base code (e.g., "JOB-ABCDE01" -> "JOB-ABCDE")
		String base = withoutTimestamp.replaceAll("\\d+$", "");

		return base.isEmpty() ? "JOB" : base;
	}

	private static String extractBaseJobTitle(String jobTitle) {
		// Remove timestamp-like suffix (alphanumeric pattern at end)
		String base = jobTitle.replaceAll("\\s+\\d{10,}\\s*$", "");
		return base.isEmpty() ? jobTitle : base.trim();
	}

	public static synchronized boolean forceRefresh() {
		hasRefreshedThisSession = false;
		return refreshJobCatalog();
	}

	public static synchronized void resetSessionFlag() {
		hasRefreshedThisSession = false;
	}

	private static void createBackup(Path originalFile) {
		try {
			// Create backup directory if it doesn't exist
			Path backupDir = Paths.get(BACKUP_DIR);
			if (!Files.exists(backupDir)) {
				Files.createDirectories(backupDir);
			}

			// Create new backup with full timestamp (including milliseconds for uniqueness)
			String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS").format(new Date());
			String backupFileName = String.format("JobCatalogExcel_Backup_%s.xlsx", timestamp);
			Path backupFile = backupDir.resolve(backupFileName);

			// Copy original to backup
			Files.copy(originalFile, backupFile, StandardCopyOption.REPLACE_EXISTING);

			LOGGER.info("Backup created: {}", backupFileName);

		} catch (Exception e) {
			LOGGER.warn("Could not create backup: {}", e.getMessage());
			// Don't fail the main operation if backup fails
		}
	}
}
