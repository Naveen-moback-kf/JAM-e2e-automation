package com.kfonetalentsuite.listeners;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.kfonetalentsuite.utils.JobMapping.ScreenshotHandler;
import com.kfonetalentsuite.utils.common.CommonVariable;
import com.kfonetalentsuite.webdriverManager.DriverManager;

import io.qameta.allure.Allure;

public class AllureScreenshotListener implements ITestListener {

	private static final Logger LOGGER = LogManager.getLogger(AllureScreenshotListener.class);

	@Override
	public void onTestFailure(ITestResult result) {
		// CONFIGURATION CHECK: Skip Allure screenshot attachment if disabled
		if (!isAllureReportingEnabled()) {
			LOGGER.debug("Allure reporting is disabled - Skipping screenshot attachment");
			return;
		}
		
		try {
			LOGGER.debug("Test failure detected - Attaching screenshot to Allure report");
			
			// Capture screenshot using existing ScreenshotHandler
			String methodName = result.getMethod().getMethodName();
			Throwable throwable = result.getThrowable();
			String errorMessage = throwable != null ? throwable.getMessage() : "Test failed";
			
			// Capture screenshot
			String screenshotPath = ScreenshotHandler.captureFailureScreenshot(methodName, errorMessage);
			
			if (screenshotPath != null && new File(screenshotPath).exists()) {
				// Attach screenshot to Allure report
				attachScreenshotToAllure(screenshotPath, "Test Failure Screenshot");
				LOGGER.info("✅ Screenshot attached to Allure report: {}", screenshotPath);
			} else {
				LOGGER.warn("Screenshot not captured or file not found - skipping Allure attachment");
			}
			
		} catch (Exception e) {
			LOGGER.error("Error attaching screenshot to Allure report: {}", e.getMessage(), e);
			// Don't fail the test if screenshot attachment fails
		}
	}

	public static void attachScreenshotToAllure(String screenshotPath, String attachmentName) {
		try {
			File screenshotFile = new File(screenshotPath);
			
			if (!screenshotFile.exists()) {
				LOGGER.warn("Screenshot file does not exist: {}", screenshotPath);
				return;
			}
			
			// Attach screenshot to Allure using Allure.addAttachment
			// Format: addAttachment(name, contentType, stream, extension)
			Allure.addAttachment(
				attachmentName != null ? attachmentName : "Screenshot",
				"image/png",
				Files.newInputStream(screenshotFile.toPath()),
				"png"
			);
			
			LOGGER.debug("Screenshot attached to Allure: {} ({})", attachmentName, screenshotPath);
			
		} catch (IOException e) {
			LOGGER.error("Failed to attach screenshot to Allure: {}", e.getMessage(), e);
		}
	}

	public static void attachScreenshotBytesToAllure(byte[] screenshotBytes, String attachmentName) {
		try {
			if (screenshotBytes == null || screenshotBytes.length == 0) {
				LOGGER.warn("Screenshot bytes are empty - skipping Allure attachment");
				return;
			}
			
			// Attach screenshot bytes to Allure
			Allure.addAttachment(
				attachmentName != null ? attachmentName : "Screenshot",
				"image/png",
				new java.io.ByteArrayInputStream(screenshotBytes),
				"png"
			);
			
			LOGGER.debug("Screenshot bytes attached to Allure: {}", attachmentName);
			
		} catch (Exception e) {
			LOGGER.error("Failed to attach screenshot bytes to Allure: {}", e.getMessage(), e);
		}
	}

	public static boolean captureAndAttachScreenshot(String attachmentName) {
		try {
			org.openqa.selenium.WebDriver driver = DriverManager.getDriver();
			
			if (driver == null) {
				LOGGER.warn("WebDriver is null - cannot capture screenshot");
				return false;
			}
			
			// Capture screenshot as bytes
			org.openqa.selenium.TakesScreenshot takesScreenshot = (org.openqa.selenium.TakesScreenshot) driver;
			byte[] screenshotBytes = takesScreenshot.getScreenshotAs(org.openqa.selenium.OutputType.BYTES);
			
			// Attach to Allure
			attachScreenshotBytesToAllure(screenshotBytes, attachmentName);
			
			LOGGER.debug("Screenshot captured and attached to Allure: {}", attachmentName);
			return true;
			
		} catch (Exception e) {
			LOGGER.error("Failed to capture and attach screenshot: {}", e.getMessage(), e);
			return false;
		}
	}

	private static boolean isAllureReportingEnabled() {
		return CommonVariable.ALLURE_REPORTING_ENABLED == null
				|| !CommonVariable.ALLURE_REPORTING_ENABLED.equalsIgnoreCase("false");
	}
}

