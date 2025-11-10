package com.kfonetalentsuite.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Utility class for handling large datasets in web automation
 * Prevents browser unresponsiveness during extensive scrolling operations
 * 
 * @author QA Automation Team
 * @version 1.0
 */
public class LargeDatasetHandler {
	
	private static final Logger LOGGER = LogManager.getLogger(LargeDatasetHandler.class);
	
	/**
	 * Performs throttled scrolling with browser stabilization to prevent unresponsiveness
	 * 
	 * @param driver WebDriver instance
	 * @param wait WebDriverWait instance
	 * @param maxScrolls Maximum number of scroll iterations
	 * @param scrollDelay Delay between scrolls in milliseconds
	 * @param spinnerLocator Locator for loading spinner (can be null)
	 * @return Number of scrolls performed
	 */
	public static int throttledScrollToLoadData(WebDriver driver, WebDriverWait wait, 
			int maxScrolls, int scrollDelay, By spinnerLocator) {
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		int scrollCount = 0;
		Long previousHeight = 0L;
		int noChangeCount = 0;
		int maxNoChangeAllowed = 3;
		
		LOGGER.info("🔄 Starting throttled scroll to load large dataset...");
		LOGGER.info("⚙️ Max scrolls: {}, Scroll delay: {}ms", maxScrolls, scrollDelay);
		
		try {
			while (scrollCount < maxScrolls) {
				// Get current scroll height
				Long currentHeight = (Long) js.executeScript("return document.body.scrollHeight");
				
				// Check if content is still loading
				if (currentHeight.equals(previousHeight)) {
					noChangeCount++;
					LOGGER.debug("No height change detected. Count: {}/{}", noChangeCount, maxNoChangeAllowed);
					
					if (noChangeCount >= maxNoChangeAllowed) {
						LOGGER.info("✅ Reached end of content. No more data to load.");
						break;
					}
				} else {
					noChangeCount = 0; // Reset counter when new content loads
				}
				
				// Perform scroll
				js.executeScript("window.scrollTo({top: document.body.scrollHeight, behavior: 'smooth'});");
				scrollCount++;
				
				// Wait for scroll animation and content loading
				Thread.sleep(scrollDelay);
				
				// Wait for loading spinner to disappear (if provided)
				if (spinnerLocator != null) {
					try {
						wait.until(ExpectedConditions.invisibilityOfElementLocated(spinnerLocator));
					} catch (Exception e) {
						// Spinner might not appear for every scroll
					}
				}
				
				// Wait for page to stabilize
				PerformanceUtils.waitForPageReady(driver, 1);
				
				// Clear browser console periodically to free memory
				if (scrollCount % 10 == 0) {
					js.executeScript("console.clear();");
					LOGGER.debug("🧹 Cleared browser console at scroll " + scrollCount);
				}
				
				previousHeight = currentHeight;
				
				if (scrollCount % 5 == 0) {
					LOGGER.info("📊 Progress: {} scrolls completed. Current height: {}", scrollCount, currentHeight);
				}
			}
			
			LOGGER.info("✅ Scroll operation completed. Total scrolls: {}", scrollCount);
			
		} catch (InterruptedException e) {
			LOGGER.error("❌ Scroll operation interrupted", e);
			Thread.currentThread().interrupt();
		} catch (Exception e) {
			LOGGER.error("❌ Error during scroll operation", e);
		}
		
		return scrollCount;
	}
	
	/**
	 * Processes large dataset in chunks to prevent browser overload
	 * 
	 * @param driver WebDriver instance
	 * @param recordsPerChunk Number of records to process per chunk
	 * @param totalRecordsNeeded Total records needed
	 * @return Number of records processed
	 */
	public static int processInChunks(WebDriver driver, int recordsPerChunk, int totalRecordsNeeded) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		int totalProcessed = 0;
		int chunksProcessed = 0;
		
		LOGGER.info("🔄 Starting chunk-based processing...");
		LOGGER.info("⚙️ Records per chunk: {}, Target total: {}", recordsPerChunk, totalRecordsNeeded);
		
		try {
			while (totalProcessed < totalRecordsNeeded) {
				// Scroll to load next chunk
				for (int i = 0; i < 5; i++) {
					js.executeScript("window.scrollBy(0, 500);");
					Thread.sleep(500);
				}
				
				// Get currently loaded records
				List<WebElement> loadedRecords = driver.findElements(By.cssSelector("tbody tr"));
				int currentCount = loadedRecords.size();
				
				// Process current chunk
				totalProcessed = currentCount;
				chunksProcessed++;
				
				LOGGER.info("📊 Chunk {} processed. Total records: {}", chunksProcessed, totalProcessed);
				
				// Clear browser memory every 5 chunks
				if (chunksProcessed % 5 == 0) {
					clearBrowserMemory(driver);
				}
				
				// Check if we've reached the end
				Long scrollTop = (Long) js.executeScript("return window.pageYOffset || document.documentElement.scrollTop;");
				Long scrollHeight = (Long) js.executeScript("return document.body.scrollHeight;");
				Long clientHeight = (Long) js.executeScript("return document.documentElement.clientHeight;");
				
				if (scrollTop + clientHeight >= scrollHeight - 100) {
					LOGGER.info("✅ Reached bottom of page");
					break;
				}
			}
			
			LOGGER.info("✅ Chunk processing completed. Total processed: {}", totalProcessed);
			
		} catch (Exception e) {
			LOGGER.error("❌ Error during chunk processing", e);
		}
		
		return totalProcessed;
	}
	
	/**
	 * Clears browser memory to prevent slowdowns during large dataset operations
	 * 
	 * @param driver WebDriver instance
	 */
	public static void clearBrowserMemory(WebDriver driver) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			
			// Clear console logs
			js.executeScript("console.clear();");
			
			// Trigger JavaScript garbage collection (if available)
			js.executeScript("if (window.gc) { window.gc(); }");
			
			// Clear browser cache for Chrome
			if (driver instanceof ChromeDriver) {
				try {
					((ChromeDriver) driver).executeCdpCommand("Network.clearBrowserCache", new HashMap<>());
					LOGGER.debug("🧹 Browser cache cleared");
				} catch (Exception e) {
					LOGGER.debug("Cache clear not available or failed");
				}
			}
			
			LOGGER.info("🧹 Browser memory cleanup performed");
			
		} catch (Exception e) {
			LOGGER.warn("⚠️ Could not clear browser memory: " + e.getMessage());
		}
	}
	
	/**
	 * Extracts data directly via JavaScript, bypassing UI scrolling
	 * Use this for maximum performance when you don't need to interact with UI elements
	 * 
	 * @param driver WebDriver instance
	 * @param rowSelector CSS selector for data rows
	 * @param columnSelectors Map of column names to CSS selectors
	 * @return List of extracted data records
	 */
	@SuppressWarnings("unchecked")
	public static List<Map<String, String>> extractDataViaJavaScript(WebDriver driver, 
			String rowSelector, Map<String, String> columnSelectors) {
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		
		LOGGER.info("🔄 Extracting data directly via JavaScript...");
		
		// Build JavaScript extraction script
		StringBuilder script = new StringBuilder();
		script.append("var allData = []; ");
		script.append("var rows = document.querySelectorAll('").append(rowSelector).append("'); ");
		script.append("rows.forEach(function(row) { ");
		script.append("  var rowData = {}; ");
		
		for (Map.Entry<String, String> entry : columnSelectors.entrySet()) {
			script.append("  rowData['").append(entry.getKey()).append("'] = ");
			script.append("row.querySelector('").append(entry.getValue()).append("')?.textContent?.trim() || ''; ");
		}
		
		script.append("  allData.push(rowData); ");
		script.append("}); ");
		script.append("return allData;");
		
		try {
			List<Map<String, String>> extractedData = 
				(List<Map<String, String>>) js.executeScript(script.toString());
			
			LOGGER.info("✅ Extracted {} records via JavaScript", extractedData.size());
			return extractedData;
			
		} catch (Exception e) {
			LOGGER.error("❌ Error extracting data via JavaScript", e);
			return new ArrayList<>();
		}
	}
	
	/**
	 * Scrolls to load specific number of records with progress monitoring
	 * 
	 * @param driver WebDriver instance
	 * @param wait WebDriverWait instance
	 * @param targetRecordCount Target number of records to load
	 * @param recordSelector CSS selector for record elements
	 * @param maxScrollAttempts Maximum scroll attempts
	 * @return Number of records loaded
	 */
	public static int scrollToLoadRecordCount(WebDriver driver, WebDriverWait wait,
			int targetRecordCount, String recordSelector, int maxScrollAttempts) {
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		int scrollAttempts = 0;
		int loadedRecords = 0;
		
		LOGGER.info("🔄 Loading {} records via scrolling...", targetRecordCount);
		
		try {
			while (scrollAttempts < maxScrollAttempts) {
				// Get current record count
				List<WebElement> records = driver.findElements(By.cssSelector(recordSelector));
				loadedRecords = records.size();
				
				if (loadedRecords >= targetRecordCount) {
					LOGGER.info("✅ Target record count reached: {}", loadedRecords);
					break;
				}
				
				// Scroll incrementally
				js.executeScript("window.scrollBy(0, 800);");
				Thread.sleep(1000);
				PerformanceUtils.waitForPageReady(driver, 1);
				
				scrollAttempts++;
				
				if (scrollAttempts % 10 == 0) {
					LOGGER.info("📊 Progress: {} records loaded, {} scrolls performed", 
						loadedRecords, scrollAttempts);
				}
			}
			
			LOGGER.info("✅ Scroll loading completed. Records loaded: {}, Scrolls: {}", 
				loadedRecords, scrollAttempts);
			
		} catch (Exception e) {
			LOGGER.error("❌ Error during scroll loading", e);
		}
		
		return loadedRecords;
	}
}

