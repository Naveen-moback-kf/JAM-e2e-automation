package com.kfonetalentsuite.utils.JobMapping;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * TestLogger - Centralized logging wrapper for test execution
 * 
 * This utility class provides a unified interface for logging test steps that:
 * - Always logs to Log4j (for file-based logs)
 * - Provides consistent logging patterns across the framework
 * 
 * Usage: TestLogger.log("message");
 * 
 * @author AI Auto Mapping Framework
 * @version 2.0 (Extent Reports removed)
 */
public class TestLogger {

	private static final Logger LOGGER = LogManager.getLogger(TestLogger.class);

	/**
	 * Log a test step message to Log4j
	 * 
	 * @param message The message to log
	 */
	public static void log(String message) {
		LOGGER.info(message);
	}

	/**
	 * Log a test step message with custom Log4j logger
	 * 
	 * @param logger  The Log4j logger to use
	 * @param message The message to log
	 */
	public static void log(Logger logger, String message) {
		logger.info(message);
	}

	/**
	 * Log an error message
	 * 
	 * @param message The error message to log
	 */
	public static void logError(String message) {
		LOGGER.error(message);
	}

	/**
	 * Log an error message with custom Log4j logger
	 * 
	 * @param logger  The Log4j logger to use
	 * @param message The error message to log
	 */
	public static void logError(Logger logger, String message) {
		logger.error(message);
	}

	/**
	 * Log a warning message
	 * 
	 * @param message The warning message to log
	 */
	public static void logWarning(String message) {
		LOGGER.warn(message);
	}

	/**
	 * Log a warning message with custom Log4j logger
	 * 
	 * @param logger  The Log4j logger to use
	 * @param message The warning message to log
	 */
	public static void logWarning(Logger logger, String message) {
		logger.warn(message);
	}

	/**
	 * Log screenshot capture (placeholder - screenshots saved to disk)
	 * 
	 * @param screenshotPath Path to the screenshot file
	 * @param message        Description/caption for the screenshot
	 */
	public static void addScreenshot(String screenshotPath, String message) {
		LOGGER.info("Screenshot captured: {} - {}", screenshotPath, message);
	}
}
