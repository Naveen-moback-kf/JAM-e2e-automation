package com.kfonetalentsuite.utils.JobMapping;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;

public class ExcelReportingHelper {

	private static final Logger LOGGER = LogManager.getLogger(ExcelReportingHelper.class);

	// ==================================================================================
	// DATA PARSING METHODS
	// ==================================================================================
	// Purpose: Extract and parse data from XML, JSON, and other formats for Excel reports

	public static int extractIntFromXML(String content, String pattern, int defaultValue) {
		try {
			Pattern regex = Pattern.compile(pattern);
			Matcher matcher = regex.matcher(content);
			if (matcher.find()) {
				return Integer.parseInt(matcher.group(1));
			}
		} catch (Exception e) {
			LOGGER.debug("Could not extract int with pattern '{}': {}", pattern, e.getMessage());
		}
		return defaultValue;
	}

	public static long extractLongFromXML(String content, String pattern, long defaultValue) {
		try {
			Pattern regex = Pattern.compile(pattern);
			Matcher matcher = regex.matcher(content);
			if (matcher.find()) {
				return Long.parseLong(matcher.group(1));
			}
		} catch (Exception e) {
			LOGGER.debug("Could not extract long with pattern '{}': {}", pattern, e.getMessage());
		}
		return defaultValue;
	}

	public static String extractStringFromXML(String content, String pattern) {
		try {
			Pattern regex = Pattern.compile(pattern);
			Matcher matcher = regex.matcher(content);
			if (matcher.find()) {
				return matcher.group(1);
			}
		} catch (Exception e) {
			LOGGER.debug("Could not extract string with pattern '{}': {}", pattern, e.getMessage());
		}
		return null;
	}

	public static String extractJsonString(String jsonBlock, int index) {
		try {
			Pattern pattern = Pattern.compile("\"([^\"]+)\"");
			Matcher matcher = pattern.matcher(jsonBlock);

			int count = 0;
			while (matcher.find()) {
				if (count == index) {
					return matcher.group(1);
				}
				count++;
			}
		} catch (Exception e) {
			LOGGER.debug("Could not extract JSON string at index {}: {}", index, e.getMessage());
		}
		return "";
	}

	public static String extractScenarioStatus(String jsonBlock) {
		if (jsonBlock.contains("\"passed\"") || jsonBlock.contains("\"status\":\"passed\"")) {
			return "PASSED";
		} else if (jsonBlock.contains("\"failed\"") || jsonBlock.contains("\"status\":\"failed\"")) {
			return "FAILED";
		} else if (jsonBlock.contains("\"skipped\"") || jsonBlock.contains("\"status\":\"skipped\"")) {
			return "SKIPPED";
		}
		return "UNKNOWN";
	}

	public static String formatDuration(long milliseconds) {
		if (milliseconds <= 0) {
			return "0m 0s";
		}

		long seconds = milliseconds / 1000;
		long minutes = seconds / 60;
		seconds = seconds % 60;

		if (minutes > 0) {
			return String.format("%dm %ds", minutes, seconds);
		} else {
			return String.format("%ds", seconds);
		}
	}

	public static long parseDurationToMs(String durationStr) {
		if (durationStr == null || durationStr.trim().isEmpty()) {
			return 0;
		}

		try {
			long totalMs = 0;

			Pattern minutePattern = Pattern.compile("(\\d+)m");
			Matcher minuteMatcher = minutePattern.matcher(durationStr);
			if (minuteMatcher.find()) {
				totalMs += Long.parseLong(minuteMatcher.group(1)) * 60 * 1000;
			}

			Pattern secondPattern = Pattern.compile("(\\d+)s");
			Matcher secondMatcher = secondPattern.matcher(durationStr);
			if (secondMatcher.find()) {
				totalMs += Long.parseLong(secondMatcher.group(1)) * 1000;
			}

			return totalMs;
		} catch (Exception e) {
			LOGGER.debug("Could not parse duration '{}': {}", durationStr, e.getMessage());
			return 0;
		}
	}

	public static String cleanScenarioNameForExcelDisplay(String scenarioName) {
		if (scenarioName == null || scenarioName.trim().isEmpty()) {
			return "Unknown Scenario";
		}

		String cleaned = scenarioName.trim();

		String[] prefixesToRemove = { "o:", "Scenario:", "Test:", "Execute:", "Run:" };

		for (String prefix : prefixesToRemove) {
			if (cleaned.toLowerCase().startsWith(prefix.toLowerCase())) {
				cleaned = cleaned.substring(prefix.length()).trim();
			}
		}

		cleaned = cleaned.replaceAll("Runner\\d+\\s*$", "").trim();

		if (cleaned.isEmpty()) {
			return scenarioName.trim();
		}

		return cleaned;
	}

	public static String extractRunnerClassFromSignature(String methodBlock) {
		try {
			Pattern pattern = Pattern.compile("class-name=\"([^\"]+)\"");
			Matcher matcher = pattern.matcher(methodBlock);

			if (matcher.find()) {
				String fullClassName = matcher.group(1);
				if (fullClassName.contains(".")) {
					return fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
				}
				return fullClassName;
			}
		} catch (Exception e) {
			LOGGER.debug("Could not extract runner class from signature: {}", e.getMessage());
		}

		return null;
	}

	public static String makeBusinessFriendly(String technicalName) {
		if (technicalName == null || technicalName.trim().isEmpty()) {
			return "Business Process";
		}

		return technicalName.replaceAll("([A-Z])", " $1")
				.replaceAll("_", " ")
				.replaceAll("\\s+", " ")
				.trim().replaceFirst("^.", technicalName.substring(0, 1).toUpperCase());
	}

	// ==================================================================================
	// EXCEL STYLING METHODS
	// ==================================================================================
	// Purpose: Create and apply cell styles, colors, and formatting for Excel reports

	public static CellStyle createHeaderStyle(Workbook workbook) {
		CellStyle headerStyle = workbook.createCellStyle();
		Font headerFont = workbook.createFont();

		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 12);
		headerFont.setFontName("Arial");

		headerStyle.setFont(headerFont);
		headerStyle.setAlignment(HorizontalAlignment.CENTER);
		headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		setBorders(headerStyle, BorderStyle.THIN);

		return headerStyle;
	}

	public static CellStyle createDataStyle(Workbook workbook) {
		CellStyle dataStyle = workbook.createCellStyle();
		Font dataFont = workbook.createFont();

		dataFont.setFontName("Arial");
		dataFont.setFontHeightInPoints((short) 10);

		dataStyle.setFont(dataFont);
		dataStyle.setAlignment(HorizontalAlignment.LEFT);
		dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		setBorders(dataStyle, BorderStyle.THIN);

		return dataStyle;
	}

	public static CellStyle createStatusStyle(Workbook workbook, String status) {
		CellStyle statusStyle = workbook.createCellStyle();
		Font statusFont = workbook.createFont();

		statusFont.setBold(true);
		statusFont.setFontName("Arial");
		statusFont.setFontHeightInPoints((short) 10);

		String normalizedStatus = (status != null) ? status.toUpperCase().trim() : "";

		if (normalizedStatus.contains("FAIL")) {
			statusStyle.setFillForegroundColor(IndexedColors.ROSE.getIndex());
			statusFont.setColor(IndexedColors.DARK_RED.getIndex());
		} else if (normalizedStatus.contains("SKIP")) {
			statusStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
			statusFont.setColor(IndexedColors.DARK_YELLOW.getIndex());
		} else {
			statusStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
			statusFont.setColor(IndexedColors.DARK_GREEN.getIndex());
		}

		statusStyle.setFont(statusFont);
		statusStyle.setAlignment(HorizontalAlignment.CENTER);
		statusStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		statusStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		setBorders(statusStyle, BorderStyle.THIN);

		return statusStyle;
	}

	public static CellStyle createSummaryStyle(Workbook workbook) {
		CellStyle summaryStyle = workbook.createCellStyle();
		Font summaryFont = workbook.createFont();

		summaryFont.setBold(true);
		summaryFont.setFontName("Arial");
		summaryFont.setFontHeightInPoints((short) 11);

		summaryStyle.setFont(summaryFont);
		summaryStyle.setAlignment(HorizontalAlignment.LEFT);
		summaryStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		summaryStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
		summaryStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		setBorders(summaryStyle, BorderStyle.MEDIUM);

		return summaryStyle;
	}

	public static CellStyle createRowStatusStyle(Workbook workbook, String status) {
		CellStyle rowStyle = workbook.createCellStyle();
		Font dataFont = workbook.createFont();

		dataFont.setFontName("Arial");
		dataFont.setFontHeightInPoints((short) 10);

		rowStyle.setFont(dataFont);
		rowStyle.setAlignment(HorizontalAlignment.LEFT);
		rowStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		String normalizedStatus = (status != null) ? status.toUpperCase().trim() : "";

		if (normalizedStatus.contains("FAIL")) {
			rowStyle.setFillForegroundColor(IndexedColors.ROSE.getIndex());
			rowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		} else if (normalizedStatus.contains("SKIP")) {
			rowStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
			rowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		} else {
			rowStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
			rowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		}

		setBorders(rowStyle, BorderStyle.THIN);

		return rowStyle;
	}

	public static CellStyle createRowStatusStyleWithWrapping(Workbook workbook, String status) {
		CellStyle rowStyle = createRowStatusStyle(workbook, status);
		rowStyle.setWrapText(true);
		rowStyle.setVerticalAlignment(VerticalAlignment.TOP);
		return rowStyle;
	}

	public static CellStyle createEnhancedStatusStyle(Workbook workbook, String status) {
		CellStyle statusStyle = workbook.createCellStyle();
		Font statusFont = workbook.createFont();

		statusFont.setBold(true);
		statusFont.setFontName("Arial");
		statusFont.setFontHeightInPoints((short) 10);

		String normalizedStatus = (status != null) ? status.toUpperCase().trim() : "";

		if (normalizedStatus.contains("FAIL")) {
			statusStyle.setFillForegroundColor(IndexedColors.ROSE.getIndex());
			statusFont.setColor(IndexedColors.DARK_RED.getIndex());
		} else if (normalizedStatus.contains("SKIP")) {
			statusStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
			statusFont.setColor(IndexedColors.DARK_YELLOW.getIndex());
		} else {
			statusStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
			statusFont.setColor(IndexedColors.DARK_GREEN.getIndex());
		}

		statusStyle.setFont(statusFont);
		statusStyle.setAlignment(HorizontalAlignment.CENTER);
		statusStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		statusStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		setBorders(statusStyle, BorderStyle.THIN);

		return statusStyle;
	}

	private static void setBorders(CellStyle style, BorderStyle borderStyle) {
		style.setBorderTop(borderStyle);
		style.setBorderBottom(borderStyle);
		style.setBorderLeft(borderStyle);
		style.setBorderRight(borderStyle);
	}

	public static void setStandardColumnWidths(Sheet sheet) {
		sheet.setColumnWidth(0, 8000);
		sheet.setColumnWidth(1, 12000);
		sheet.setColumnWidth(2, 3000);
		sheet.setColumnWidth(3, 6000);
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

	public static void applyRowLevelStyling(Workbook workbook, Row dataRow, String status, int numDataColumns,
			int statusColumnIndex) {

		CellStyle rowStatusStyle = createRowStatusStyle(workbook, status);
		CellStyle enhancedStatusStyle = createEnhancedStatusStyle(workbook, status);

		for (int i = 0; i < numDataColumns; i++) {
			Cell cell = dataRow.getCell(i);
			if (cell == null) {
				cell = dataRow.createCell(i);
			}
			cell.setCellStyle(rowStatusStyle);
		}

		Cell statusCell = dataRow.getCell(statusColumnIndex);
		if (statusCell == null) {
			statusCell = dataRow.createCell(statusColumnIndex);
		}
		statusCell.setCellStyle(enhancedStatusStyle);
	}

	public static void applyRowLevelStylingToSpecificColumns(Workbook workbook, Row dataRow, String status,
			int[] columnIndices) {
		CellStyle rowStatusStyle = createRowStatusStyle(workbook, status);

		for (int columnIndex : columnIndices) {
			Cell cell = dataRow.getCell(columnIndex);
			if (cell == null) {
				cell = dataRow.createCell(columnIndex);
			}
			cell.setCellStyle(rowStatusStyle);
		}
	}
}

