package com.kfonetalentsuite.utils.JobMapping;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Enhanced logging utility to replace System.out.println and printStackTrace
 * calls. Provides consistent logging across the framework.
 * 
 * @author QA Automation Team
 * @version 3.0 (Extent Reports removed)
 */
public class EnhancedLogger {
	private static final Logger LOGGER = LogManager.getLogger(EnhancedLogger.class);

	/**
	 * Replace System.out.println with proper info logging
	 * 
	 * @param message - The message to log
	 */
	public static void info(String message) {
		LOGGER.info(message);
	}

	/**
	 * Debug level logging
	 * 
	 * @param message - The debug message
	 */
	public static void debug(String message) {
		LOGGER.debug(message);
	}

	/**
	 * Replace printStackTrace with proper error logging
	 * 
	 * @param message - Context message describing the error
	 * @param e       - The exception to log
	 */
	public static void error(String message, Exception e) {
		LOGGER.error(message, e);
	}

	/**
	 * Warning level logging
	 * 
	 * @param message - The warning message
	 */
	public static void warn(String message) {
		LOGGER.warn(message);
	}

	/**
	 * Success logging with indicator
	 * 
	 * @param message - The success message
	 */
	public static void success(String message) {
		LOGGER.info("... " + message);
	}

	/**
	 * Performance monitoring utility
	 * 
	 * @param methodName - Name of the method being monitored
	 * @param startTime  - Start time in milliseconds
	 */
	public static void logExecutionTime(String methodName, long startTime) {
		long duration = System.currentTimeMillis() - startTime;
		String message = String.format("Method '%s' executed in %d ms", methodName, duration);
		LOGGER.info(message);

		// Log performance warnings for slow operations
		if (duration > 10000) { // 10 seconds
			LOGGER.warn("Slow operation detected: {} took {} ms", methodName, duration);
		}
	}

	/**
	 * Step logging for test clarity
	 * 
	 * @param stepDescription - Description of the test step
	 */
	public static void step(String stepDescription) {
		String message = " Step: " + stepDescription;
		LOGGER.info(message);
	}
}
