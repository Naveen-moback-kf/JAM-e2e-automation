package com.kfonetalentsuite.testNGAnalyzer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

public class MyTransform implements IAnnotationTransformer {

	private static final Logger LOGGER = LogManager.getLogger(MyTransform.class);
	private static boolean loggedOnce = false;

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
