package com.kfonetalentsuite.utils.JobMapping;

import org.apache.poi.ss.usermodel.*;

public class ExcelStyleHelper {

	public static CellStyle createHeaderStyle(Workbook workbook) {
		CellStyle headerStyle = workbook.createCellStyle();
		Font headerFont = workbook.createFont();

		// Header font settings
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 12);
		headerFont.setFontName("Arial");

		// Header style settings
		headerStyle.setFont(headerFont);
		headerStyle.setAlignment(HorizontalAlignment.CENTER);
		headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		// Borders
		setBorders(headerStyle, BorderStyle.THIN);

		return headerStyle;
	}

	public static CellStyle createDataStyle(Workbook workbook) {
		CellStyle dataStyle = workbook.createCellStyle();
		Font dataFont = workbook.createFont();

		// Data font settings
		dataFont.setFontName("Arial");
		dataFont.setFontHeightInPoints((short) 10);

		// Data style settings
		dataStyle.setFont(dataFont);
		dataStyle.setAlignment(HorizontalAlignment.LEFT);
		dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		// Borders
		setBorders(dataStyle, BorderStyle.THIN);

		return dataStyle;
	}

	public static CellStyle createStatusStyle(Workbook workbook, String status) {
		CellStyle statusStyle = workbook.createCellStyle();
		Font statusFont = workbook.createFont();

		// Base font settings
		statusFont.setBold(true);
		statusFont.setFontName("Arial");
		statusFont.setFontHeightInPoints((short) 10);

		// Normalize status for comparison
		String normalizedStatus = (status != null) ? status.toUpperCase().trim() : "";

		// Status-specific colors - FIXED: Handle variations like "PASS", "FAIL", "SKIP"
		if (normalizedStatus.contains("FAIL")) {
			statusStyle.setFillForegroundColor(IndexedColors.ROSE.getIndex());
			statusFont.setColor(IndexedColors.DARK_RED.getIndex());
		} else if (normalizedStatus.contains("SKIP")) {
			statusStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
			statusFont.setColor(IndexedColors.DARK_YELLOW.getIndex());
		} else {
			// PASSED, PASS, or any other status - default to green
			statusStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
			statusFont.setColor(IndexedColors.DARK_GREEN.getIndex());
		}

		// Apply style settings
		statusStyle.setFont(statusFont);
		statusStyle.setAlignment(HorizontalAlignment.CENTER);
		statusStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		statusStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		// Borders
		setBorders(statusStyle, BorderStyle.THIN);

		return statusStyle;
	}

	public static CellStyle createSummaryStyle(Workbook workbook) {
		CellStyle summaryStyle = workbook.createCellStyle();
		Font summaryFont = workbook.createFont();

		// Summary font settings
		summaryFont.setBold(true);
		summaryFont.setFontName("Arial");
		summaryFont.setFontHeightInPoints((short) 11);

		// Summary style settings
		summaryStyle.setFont(summaryFont);
		summaryStyle.setAlignment(HorizontalAlignment.LEFT);
		summaryStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		summaryStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
		summaryStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		// Borders
		setBorders(summaryStyle, BorderStyle.MEDIUM);

		return summaryStyle;
	}

	private static void setBorders(CellStyle style, BorderStyle borderStyle) {
		style.setBorderTop(borderStyle);
		style.setBorderBottom(borderStyle);
		style.setBorderLeft(borderStyle);
		style.setBorderRight(borderStyle);
	}

	public static void setStandardColumnWidths(Sheet sheet) {
		// Feature name column - wider
		sheet.setColumnWidth(0, 8000);

		// Scenario name column - widest
		sheet.setColumnWidth(1, 12000);

		// Status column - narrow
		sheet.setColumnWidth(2, 3000);

		// Details column - medium
		sheet.setColumnWidth(3, 6000);

		// Comments column - wide
		sheet.setColumnWidth(4, 10000);
	}

	public static void setExecutionHistoryColumnWidths(Sheet sheet) {
		String[] columnNames = { "User Name", "Client Name", "Testing Date", "Time", "Environment", "Execution Type",
				"Browser Results", "Runner / Suite File", "Functions Tested", "Working", "Issues Found", "Skipped",
				"Success Rate", "Duration", "Quality Status" };

		int[] widths = { 4000, 6000, 4000, 3000, 3000, 4000, 6000, 6000, 4000, 3000, 3000, 3000, 4000, 3000, 4000 };

		for (int i = 0; i < columnNames.length && i < widths.length; i++) {
			sheet.setColumnWidth(i, widths[i]);
		}
	}

	// ==================================================================================
	// ROW-LEVEL COLORING ENHANCEMENT
	// ==================================================================================
	// Added: January 2025 - Full row background coloring based on Quality Status
	// Purpose: Apply same background color to entire row (A-I) as Quality Status
	// column (J)

	public static CellStyle createRowStatusStyle(Workbook workbook, String status) {
		CellStyle rowStyle = workbook.createCellStyle();
		Font dataFont = workbook.createFont();

		// Base font settings (same as regular data style)
		dataFont.setFontName("Arial");
		dataFont.setFontHeightInPoints((short) 10);

		// Base style settings
		rowStyle.setFont(dataFont);
		rowStyle.setAlignment(HorizontalAlignment.LEFT);
		rowStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		// Normalize status for comparison
		String normalizedStatus = (status != null) ? status.toUpperCase().trim() : "";

		// Apply status-specific background colors
		// FIXED: Handle variations like "PASS", "FAIL", "SKIP"
		if (normalizedStatus.contains("FAIL")) {
			// FAILED or FAIL
			rowStyle.setFillForegroundColor(IndexedColors.ROSE.getIndex());
			rowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		} else if (normalizedStatus.contains("SKIP")) {
			// SKIPPED or SKIP
			rowStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
			rowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		} else {
			// PASSED, PASS, or any other status (including null/empty) - default to green
			// This ensures all cells get a background color for visual consistency
			rowStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
			rowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		}

		// Borders
		setBorders(rowStyle, BorderStyle.THIN);

		return rowStyle;
	}

	public static CellStyle createRowStatusStyleWithWrapping(Workbook workbook, String status) {
		CellStyle rowStyle = createRowStatusStyle(workbook, status);
		rowStyle.setWrapText(true); // Enable text wrapping for multi-line content
		rowStyle.setVerticalAlignment(VerticalAlignment.TOP); // Align text to top for better readability
		return rowStyle;
	}

	public static CellStyle createEnhancedStatusStyle(Workbook workbook, String status) {
		CellStyle statusStyle = workbook.createCellStyle();
		Font statusFont = workbook.createFont();

		// Bold font settings for status column
		statusFont.setBold(true);
		statusFont.setFontName("Arial");
		statusFont.setFontHeightInPoints((short) 10);

		// Normalize status for comparison
		String normalizedStatus = (status != null) ? status.toUpperCase().trim() : "";

		// Status-specific colors - FIXED: Handle variations
		if (normalizedStatus.contains("FAIL")) {
			statusStyle.setFillForegroundColor(IndexedColors.ROSE.getIndex());
			statusFont.setColor(IndexedColors.DARK_RED.getIndex());
		} else if (normalizedStatus.contains("SKIP")) {
			statusStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
			statusFont.setColor(IndexedColors.DARK_YELLOW.getIndex());
		} else {
			// PASSED, PASS, or any other status - default to green
			statusStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
			statusFont.setColor(IndexedColors.DARK_GREEN.getIndex());
		}

		// Apply style settings
		statusStyle.setFont(statusFont);
		statusStyle.setAlignment(HorizontalAlignment.CENTER);
		statusStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		statusStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		// Borders
		setBorders(statusStyle, BorderStyle.THIN);

		return statusStyle;
	}

	public static void applyRowLevelStyling(Workbook workbook, Row dataRow, String status, int numDataColumns,
			int statusColumnIndex) {

		// Create styles
		CellStyle rowStatusStyle = createRowStatusStyle(workbook, status);
		CellStyle enhancedStatusStyle = createEnhancedStatusStyle(workbook, status);

		// Apply row-level background coloring to data columns (A-I)
		// FIXED: Get or create cell to ensure styling is always applied
		for (int i = 0; i < numDataColumns; i++) {
			Cell cell = dataRow.getCell(i);
			if (cell == null) {
				cell = dataRow.createCell(i);
			}
			cell.setCellStyle(rowStatusStyle);
		}

		// Apply enhanced status style to status column (J)
		// FIXED: Get or create cell to ensure styling is always applied
		Cell statusCell = dataRow.getCell(statusColumnIndex);
		if (statusCell == null) {
			statusCell = dataRow.createCell(statusColumnIndex);
		}
		statusCell.setCellStyle(enhancedStatusStyle);
	}

	public static void applyRowLevelStylingToSpecificColumns(Workbook workbook, Row dataRow, String status,
			int[] columnIndices) {
		// Create row status style
		CellStyle rowStatusStyle = createRowStatusStyle(workbook, status);

		// Apply row-level background coloring to specified columns only
		// FIXED: Get or create cell to ensure styling is always applied
		for (int columnIndex : columnIndices) {
			Cell cell = dataRow.getCell(columnIndex);
			if (cell == null) {
				cell = dataRow.createCell(columnIndex);
			}
			cell.setCellStyle(rowStatusStyle);
		}
	}
}
