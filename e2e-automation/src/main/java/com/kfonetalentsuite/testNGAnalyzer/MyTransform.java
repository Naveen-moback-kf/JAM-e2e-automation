package com.kfonetalentsuite.testNGAnalyzer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

/**
 * MyTransform - Annotation Transformer for Retry Logic
 * 
 * This class implements TestNG's IAnnotationTransformer to automatically
 * apply the RetryAnalyzer to all test methods at runtime.
 * 
 * This eliminates the need to add @Test(retryAnalyzer = RetryAnalyzer.class)
 * annotation to each test method individually.
 * 
 * Usage: Add this listener to TestNG suite XML:
 * <listener class-name="com.kfonetalentsuite.testNGAnalyzer.MyTransform" />
 * 
 * @author Automation Team
 */
public class MyTransform implements IAnnotationTransformer {

	private static final Logger LOGGER = LogManager.getLogger(MyTransform.class);
	private static boolean loggedOnce = false;

	/**
	 * Transforms test annotations to apply RetryAnalyzer.
	 * Called by TestNG for each test method during initialization.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void transform(ITestAnnotation annotation, Class testClass,
			Constructor testConstructor, Method testMethod) {

		// Log only once at startup to avoid log spam
		if (!loggedOnce) {
			LOGGER.info("╔═══════════════════════════════════════════════════════════════╗");
			LOGGER.info("║            AUTO-RETRY ENABLED FOR FAILED TESTS                ║");
			LOGGER.info("║            Max Retry Attempts: {}                              ║", 
					RetryAnalyzer.getMaxRetryCount());
			LOGGER.info("╚═══════════════════════════════════════════════════════════════╝");
			loggedOnce = true;
		}

		// Apply RetryAnalyzer to all test methods
		annotation.setRetryAnalyzer(RetryAnalyzer.class);
	}
}
