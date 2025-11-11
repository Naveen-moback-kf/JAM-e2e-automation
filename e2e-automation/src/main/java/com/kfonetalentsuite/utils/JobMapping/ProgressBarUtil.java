package com.kfonetalentsuite.utils.JobMapping;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Progress Bar Utility for Test Suite Execution
 * 
 * FEATURES:
 * - Displays colorful progress bar during test suite execution
 * - Shows runner completion count (e.g., "3/10 runners completed")
 * - Works with both console and file logging
 * - Green color coding for visual appeal
 * - Thread-safe implementation
 * 
 * INTEGRATION:
 * - Called from ExcelReportListener during test execution
 * - Automatically detects console vs file logging
 * - Non-intrusive - doesn't affect test execution
 */
public class ProgressBarUtil {
    
    private static final Logger LOGGER = LogManager.getLogger(ProgressBarUtil.class);
    
    // Progress tracking
    private static int totalRunners = 0;
    private static int completedRunners = 0;
    
    // Configuration
    private static final boolean PROGRESS_BAR_ENABLED = 
        Boolean.parseBoolean(System.getProperty("progress.bar.enabled", "true"));
    
    // ANSI Color codes for console output
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_BRIGHT_GREEN = "\u001B[92m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_CYAN = "\u001B[36m";
    
    // ENHANCED: Simple colors for clean solid bar appearance
    private static final String ANSI_DARK_GREY = "\u001B[90m";         // Dark grey for empty sections
    
    // Progress bar characters - solid blocks for clean appearance
    private static final String PROGRESS_FILLED = "█";     // Solid block for filled sections
    private static final String PROGRESS_EMPTY = "█";      // Solid block for empty sections too
    private static final String PROGRESS_ARROW = "█";      // Solid block for arrow too (cleaner look)
    
    /**
     * Initialize progress tracking for a new test suite execution
     * @param totalCount Total number of runners in the test suite
     */
    public static synchronized void initializeProgress(int totalCount) {
        if (!PROGRESS_BAR_ENABLED || totalCount <= 0) {
            return;
        }
        
        totalRunners = totalCount;
        completedRunners = 0;
        
        // Display initial progress bar
        logWithProgress("🚀 Starting test suite execution with {} runners", totalRunners);
        displayProgressBar("Initializing...", false);
    }
    
    /**
     * Update progress when a runner completes
     * @param runnerName Name of the completed runner
     */
    public static synchronized void updateProgress(String runnerName) {
        if (!PROGRESS_BAR_ENABLED || totalRunners <= 0) {
            return;
        }
        
        completedRunners++;
        
        String cleanRunnerName = cleanRunnerName(runnerName);
        
        // Display updated progress bar
        displayProgressBar("✅ Completed: " + cleanRunnerName, true);
        
        // Special message when all runners complete
        if (completedRunners >= totalRunners) {
            logWithProgress("🎉 All runners completed successfully! ({}/{})", completedRunners, totalRunners);
        }
    }
    
    /**
     * Display the progress bar with current status
     * @param statusMessage Current status message
     * @param showCompleted Whether to show completion indicator
     */
    private static void displayProgressBar(String statusMessage, boolean showCompleted) {
        if (totalRunners <= 0) return;
        
        double progressPercent = (double) completedRunners / totalRunners;
        int barWidth = 40; // Width of the progress bar
        int filledWidth = (int) (progressPercent * barWidth);
        
        StringBuilder progressBar = new StringBuilder();
        
        // Build clean solid colored progress bar
        if (supportsColors()) {
            // Add progress bar with solid colors (clean charging bar effect)
            progressBar.append("[");
            for (int i = 0; i < barWidth; i++) {
                if (i < filledWidth) {
                    // SOLID GREEN bars for completed sections
                    progressBar.append(ANSI_BRIGHT_GREEN)
                              .append(PROGRESS_FILLED)
                              .append(ANSI_RESET);
                } else if (i == filledWidth && showCompleted) {
                    // SOLID GREEN bar for current progress position
                    progressBar.append(ANSI_GREEN)
                              .append(PROGRESS_ARROW)
                              .append(ANSI_RESET);
                } else {
                    // SOLID GREY bars for empty sections
                    progressBar.append(ANSI_DARK_GREY)
                              .append(PROGRESS_EMPTY)
                              .append(ANSI_RESET);
                }
            }
            progressBar.append("]");
            
            // Add percentage and count with colors
            progressBar.append(ANSI_CYAN)
                      .append(String.format(" %3.0f%% ", progressPercent * 100))
                      .append(ANSI_YELLOW)
                      .append(String.format("(%d/%d runners)", completedRunners, totalRunners))
                      .append(ANSI_RESET);
        } else {
            // Plain text version for file logging
            progressBar.append("[");
            for (int i = 0; i < barWidth; i++) {
                if (i < filledWidth) {
                    progressBar.append("=");
                } else if (i == filledWidth && showCompleted) {
                    progressBar.append(">");
                } else {
                    progressBar.append("-");
                }
            }
            progressBar.append("]");
            progressBar.append(String.format(" %3.0f%% (%d/%d runners)", 
                                           progressPercent * 100, completedRunners, totalRunners));
        }
        
        // Log the progress bar
        if (statusMessage != null && !statusMessage.isEmpty()) {
            LOGGER.info("📊 PROGRESS: {} {}", statusMessage, progressBar.toString());
        } else {
            LOGGER.info("📊 PROGRESS: {}", progressBar.toString());
        }
    }
    
    /**
     * Clean up runner name for display
     */
    private static String cleanRunnerName(String runnerName) {
        if (runnerName == null) return "Unknown Runner";
        
        // Remove package names and "Runner" suffix
        String cleaned = runnerName;
        if (cleaned.contains(".")) {
            cleaned = cleaned.substring(cleaned.lastIndexOf('.') + 1);
        }
        if (cleaned.endsWith("Runner")) {
            cleaned = cleaned.substring(0, cleaned.length() - 6);
        }
        
        // Add spaces before capital letters for readability
        cleaned = cleaned.replaceAll("([A-Z])", " $1").trim();
        
        // Limit length for display
        if (cleaned.length() > 35) {
            cleaned = cleaned.substring(0, 32) + "...";
        }
        
        return cleaned;
    }
    
    /**
     * Check if the current logging environment supports ANSI colors
     * ENHANCED: Better Windows support and force enable option
     */
    private static boolean supportsColors() {
        // Check if colors are explicitly forced on
        boolean forceColors = Boolean.parseBoolean(System.getProperty("progress.colors.force", "false"));
        if (forceColors) {
            // LOGGER.debug("🎨 ANSI colors FORCE ENABLED via system property");
            return true;
        }
        
        // Check if explicitly disabled
        boolean colorsDisabled = Boolean.parseBoolean(System.getProperty("progress.colors.disabled", "false"));
        if (colorsDisabled) {
            // LOGGER.debug("🚫 ANSI colors explicitly DISABLED");
            return false;
        }
        
        // Check if running in console (not file logging)
        String logAppenders = System.getProperty("log4j.configuration.appenders", "");
        boolean isConsoleLogging = logAppenders.contains("console") || logAppenders.isEmpty();
        
        // ENHANCED: Better terminal detection for Windows and IDE environments
        String term = System.getenv("TERM");
        String os = System.getProperty("os.name", "").toLowerCase();
        boolean isWindows = os.contains("windows");
        
        // Windows 10+ Command Prompt and PowerShell support ANSI colors
        boolean isTerminal = term != null && !term.equals("dumb");
        if (isWindows) {
            // For Windows, be more permissive - modern Windows supports ANSI
            String wtSession = System.getenv("WT_SESSION"); // Windows Terminal
            String conEmuANSI = System.getenv("ConEmuANSI"); // ConEmu
            isTerminal = isTerminal || wtSession != null || conEmuANSI != null || forceColors;
        }
        
        boolean colorsSupported = isConsoleLogging && isTerminal;
        
        // DEBUG: Log color support detection details
        // LOGGER.debug("🎨 ANSI Color Support Detection:");
        // LOGGER.debug("  - Console Logging: {}", isConsoleLogging);
        // LOGGER.debug("  - TERM Variable: '{}'", term != null ? term : "null");
        // LOGGER.debug("  - OS: {}", os);
        // LOGGER.debug("  - Windows Terminal Session: {}", System.getenv("WT_SESSION") != null);
        // LOGGER.debug("  - Colors Supported: {}", colorsSupported);
        
        return colorsSupported;
    }
    
    /**
     * Log a message with progress context
     */
    private static void logWithProgress(String message, Object... args) {
        if (PROGRESS_BAR_ENABLED) {
            LOGGER.info("📈 " + message, args);
        }
    }
    
    /**
     * Get current progress information
     */
    public static ProgressInfo getProgressInfo() {
        return new ProgressInfo(completedRunners, totalRunners);
    }
    
    /**
     * Check if progress bar is enabled
     */
    public static boolean isProgressBarEnabled() {
        return PROGRESS_BAR_ENABLED;
    }
    
    /**
     * Reset progress tracking (for cleanup)
     */
    public static synchronized void resetProgress() {
        totalRunners = 0;
        completedRunners = 0;
    }
    
    /**
     * Display a simple progress update without full bar (for quick updates)
     */
    public static synchronized void quickUpdate(String message) {
        if (!PROGRESS_BAR_ENABLED || totalRunners <= 0) {
            return;
        }
        
        double progressPercent = (double) completedRunners / totalRunners;
        
        if (supportsColors()) {
            LOGGER.info("📊 {} {} {}({}/{} - {:.0f}%){}", 
                       ANSI_GREEN, message, ANSI_CYAN, 
                       completedRunners, totalRunners, progressPercent * 100, ANSI_RESET);
        } else {
            LOGGER.info("📊 {} ({}/{} - {:.0f}%)", 
                       message, completedRunners, totalRunners, progressPercent * 100);
        }
    }
    
    /**
     * Data class for progress information
     */
    public static class ProgressInfo {
        public final int completed;
        public final int total;
        public final double percentage;
        
        public ProgressInfo(int completed, int total) {
            this.completed = completed;
            this.total = total;
            this.percentage = total > 0 ? (double) completed / total * 100 : 0;
        }
        
        @Override
        public String toString() {
            return String.format("ProgressInfo{%d/%d (%.1f%%)}", completed, total, percentage);
        }
    }
}
