package com.kfonetalentsuite.utils.JobMapping;

import org.apache.poi.ss.usermodel.*;

/**
 * Excel Style Helper - Centralized styling for Excel reports
 * 
 * Extracted from DailyExcelTracker to reduce complexity and improve maintainability.
 * Handles all Excel cell styling, colors, fonts, and formatting.
 */
public class ExcelStyleHelper {
    
    /**
     * Create header style for Excel sheets
     */
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
    
    /**
     * Create data style for Excel sheets
     */
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
    
    /**
     * Create status-specific style based on test result
     */
    public static CellStyle createStatusStyle(Workbook workbook, String status) {
        CellStyle statusStyle = workbook.createCellStyle();
        Font statusFont = workbook.createFont();
        
        // Base font settings
        statusFont.setBold(true);
        statusFont.setFontName("Arial");
        statusFont.setFontHeightInPoints((short) 10);
        
        // Status-specific colors
        if ("PASSED".equalsIgnoreCase(status)) {
            statusStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            statusFont.setColor(IndexedColors.DARK_GREEN.getIndex());
        } else if ("FAILED".equalsIgnoreCase(status)) {
            statusStyle.setFillForegroundColor(IndexedColors.ROSE.getIndex());
            statusFont.setColor(IndexedColors.DARK_RED.getIndex());
        } else if ("SKIPPED".equalsIgnoreCase(status)) {
            statusStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
            statusFont.setColor(IndexedColors.DARK_YELLOW.getIndex());
        } else {
            statusStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            statusFont.setColor(IndexedColors.BLACK.getIndex());
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
    
    /**
     * Create summary style for summary rows
     */
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
    
    /**
     * Set borders for a cell style
     */
    private static void setBorders(CellStyle style, BorderStyle borderStyle) {
        style.setBorderTop(borderStyle);
        style.setBorderBottom(borderStyle);
        style.setBorderLeft(borderStyle);
        style.setBorderRight(borderStyle);
    }
    
    /**
     * Set column widths for standard report layout
     */
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
    
    /**
     * Set column widths for ENHANCED execution history layout (13 columns)
     * UPDATED: Added Execution Type & Browser Results columns
     */
    public static void setExecutionHistoryColumnWidths(Sheet sheet) {
        String[] columnNames = {"Testing Date", "Time", "Environment", "Execution Type", "Browser Results", 
                               "Runner File", "Functions Tested", "Working", "Issues Found", "Skipped", 
                               "Success Rate", "Duration", "Quality Status"};
        
        int[] widths = {4000, 3000, 3000, 4000, 6000, 6000, 4000, 3000, 3000, 3000, 4000, 3000, 4000};
        
        for (int i = 0; i < columnNames.length && i < widths.length; i++) {
            sheet.setColumnWidth(i, widths[i]);
        }
    }
    
    // ==================================================================================
    // 🎨 ROW-LEVEL COLORING ENHANCEMENT
    // ==================================================================================
    // Added: January 2025 - Full row background coloring based on Quality Status
    // Purpose: Apply same background color to entire row (A-I) as Quality Status column (J)
    
    /**
     * Create row-level data style with status-based background coloring
     * This applies the same background color to columns A-I as used in the status column J
     * 
     * @param workbook - The Excel workbook
     * @param status - Test status (PASSED, FAILED, SKIPPED)
     * @return CellStyle with appropriate background color for the entire row
     */
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
        
        // Apply status-specific background colors (SIMPLIFIED: 3 statuses only)
        if ("PASSED".equalsIgnoreCase(status)) {
            rowStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            rowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        } else if ("FAILED".equalsIgnoreCase(status)) {
            rowStyle.setFillForegroundColor(IndexedColors.ROSE.getIndex());
            rowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        } else if ("SKIPPED".equalsIgnoreCase(status)) {
            // SKIPPED status uses yellow color styling for execution history rows
            rowStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
            rowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }
        // If status is unknown/null, no background color (default white)
        
        // Borders
        setBorders(rowStyle, BorderStyle.THIN);
        
        return rowStyle;
    }
    
    /**
     * Create enhanced status style for the Quality Status column (J)
     * Maintains bold formatting while ensuring consistent coloring with row
     * 
     * @param workbook - The Excel workbook
     * @param status - Test status (PASSED, FAILED, SKIPPED)  
     * @return CellStyle for the status column with bold font and matching colors
     */
    public static CellStyle createEnhancedStatusStyle(Workbook workbook, String status) {
        CellStyle statusStyle = workbook.createCellStyle();
        Font statusFont = workbook.createFont();
        
        // Bold font settings for status column
        statusFont.setBold(true);
        statusFont.setFontName("Arial");
        statusFont.setFontHeightInPoints((short) 10);
        
        // Status-specific colors (SIMPLIFIED: 3 statuses only)
        if ("PASSED".equalsIgnoreCase(status)) {
            statusStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            statusFont.setColor(IndexedColors.DARK_GREEN.getIndex());
        } else if ("FAILED".equalsIgnoreCase(status)) {
            statusStyle.setFillForegroundColor(IndexedColors.ROSE.getIndex());
            statusFont.setColor(IndexedColors.DARK_RED.getIndex());
        } else if ("SKIPPED".equalsIgnoreCase(status)) {
            // SKIPPED status uses yellow color styling
            statusStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
            statusFont.setColor(IndexedColors.DARK_YELLOW.getIndex());
        } else {
            statusStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            statusFont.setColor(IndexedColors.BLACK.getIndex());
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
    
    /**
     * Apply row-level styling to an entire data row
     * This method applies the status-based background color to columns A-I
     * and the enhanced status style to column J
     * 
     * @param workbook - The Excel workbook
     * @param dataRow - The row to style
     * @param status - Test status (PASSED, FAILED, SKIPPED)
     * @param numDataColumns - Number of data columns (usually 9 for A-I)
     * @param statusColumnIndex - Index of status column (usually 9 for J)
     */
    public static void applyRowLevelStyling(Workbook workbook, Row dataRow, String status, 
                                          int numDataColumns, int statusColumnIndex) {
        
        // Create styles
        CellStyle rowStatusStyle = createRowStatusStyle(workbook, status);
        CellStyle enhancedStatusStyle = createEnhancedStatusStyle(workbook, status);
        
        // Apply row-level background coloring to data columns (A-I)
        for (int i = 0; i < numDataColumns; i++) {
            Cell cell = dataRow.getCell(i);
            if (cell != null) {
                cell.setCellStyle(rowStatusStyle);
            }
        }
        
        // Apply enhanced status style to status column (J)
        Cell statusCell = dataRow.getCell(statusColumnIndex);
        if (statusCell != null) {
            statusCell.setCellStyle(enhancedStatusStyle);
        }
    }
    
    /**
     * Apply row-level styling to specific columns only (CROSS-BROWSER FIX)
     * This preserves existing formatting in other columns (like bold browser status columns)
     * 
     * @param workbook - Excel workbook for creating styles
     * @param dataRow - Row to apply styling to
     * @param status - Test status (PASSED, FAILED, SKIPPED)  
     * @param columnIndices - Array of column indices to apply styling to
     */
    public static void applyRowLevelStylingToSpecificColumns(Workbook workbook, Row dataRow, String status, int[] columnIndices) {
        // Create row status style
        CellStyle rowStatusStyle = createRowStatusStyle(workbook, status);
        
        // Apply row-level background coloring to specified columns only
        for (int columnIndex : columnIndices) {
            Cell cell = dataRow.getCell(columnIndex);
            if (cell != null) {
                cell.setCellStyle(rowStatusStyle);
            }
        }
    }
}
