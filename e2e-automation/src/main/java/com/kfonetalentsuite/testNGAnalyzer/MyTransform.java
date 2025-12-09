package com.kfonetalentsuite.testNGAnalyzer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

/**
 * MyTransform - Annotation Transformer (Immediate Retry DISABLED)
 * 
 * IMPORTANT: Immediate retry is DISABLED in favor of "Retry at End of Suite" pattern.
 * 
 * The SuiteRetryListener now handles retries:
 * 1. All tests run without immediate retry
 * 2. Failed tests are collected during suite execution
 * 3. At END of suite, failed tests are retried with fresh browser sessions
 * 
 * This approach is better because:
 * - Transient issues often resolve by end of suite
 * - Fresh browser session for each retry
 * - Clean reporting: see all failures first, then retries
 * - Better for CI/CD pipelines
 * 
 * To re-enable immediate retry, uncomment the setRetryAnalyzer line below.
 * 
 * @author Automation Team
 */
public class MyTransform implements IAnnotationTransformer {

	private static final Logger LOGGER = LogManager.getLogger(MyTransform.class);
	private static boolean loggedOnce = false;

	/**
	 * Transforms test annotations.
	 * NOTE: Immediate retry is DISABLED - SuiteRetryListener handles retries at end of suite.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void transform(ITestAnnotation annotation, Class testClass,
			Constructor testConstructor, Method testMethod) {

		// Log only once at startup
		if (!loggedOnce) {
			LOGGER.info("╔═══════════════════════════════════════════════════════════════╗");
			LOGGER.info("║     RETRY MODE: End of Suite (Immediate retry DISABLED)       ║");
			LOGGER.info("║     Failed tests will be retried AFTER all tests complete     ║");
			LOGGER.info("║     Max Retry Attempts: {}                                     ║", 
					SuiteRetryListener.getMaxRetryAttempts());
			LOGGER.info("╚═══════════════════════════════════════════════════════════════╝");
			loggedOnce = true;
		}

		// DISABLED: Immediate retry - now handled by SuiteRetryListener at end of suite
		// To re-enable immediate retry, uncomment the following line:
		// annotation.setRetryAnalyzer(RetryAnalyzer.class);
	}
}
