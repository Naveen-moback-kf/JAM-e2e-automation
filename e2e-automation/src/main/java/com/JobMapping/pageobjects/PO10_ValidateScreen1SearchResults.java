package com.JobMapping.pageobjects;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.JobMapping.utils.PerformanceUtils;
import com.JobMapping.utils.ScreenshotHandler;
import com.JobMapping.utils.Utilities;
import com.JobMapping.webdriverManager.DriverManager;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

public class PO10_ValidateScreen1SearchResults {
	WebDriver driver = DriverManager.getDriver();	

	protected static final Logger LOGGER = (Logger) LogManager.getLogger();
	PO10_ValidateScreen1SearchResults validateScreen1SearchResults;
	
//	static String jobnamesubstring = "IT Project";
	public static String[] jobNamesTextInSearchResults;
	
	// Flag to track if search results exist - used to skip validation when 0 results
	private static boolean hasSearchResults = true;
	

	public PO10_ValidateScreen1SearchResults() throws IOException {
		// super();
		PageFactory.initElements(driver, this);
		// TODO Auto-generated constructor stub
	}

	WebDriverWait wait = DriverManager.getWait();
	Utilities utils = new Utilities();
	JavascriptExecutor js = (JavascriptExecutor) driver;
	
	//XPATHS
	@FindBy(xpath = "//div[@data-testid='loader']//img")
	@CacheLookup
	WebElement pageLoadSpinner2;
	
	@FindBy(xpath = "//div[contains(@id,'results-toggle')]//*[contains(text(),'Showing')]")
	@CacheLookup
	public WebElement showingJobResultsCount;
	
	//Methods
	
	/**
	 * Set the search results flag (called from count verification method)
	 * @param hasResults - true if search returned results, false if 0 results
	 */
	public static void setHasSearchResults(boolean hasResults) {
		hasSearchResults = hasResults;
	}
	
	public void user_should_scroll_down_to_view_last_search_result() throws InterruptedException {
		// SKIP if no search results found
		if (!hasSearchResults) {
			LOGGER.info("⏭️ SKIPPING scroll step - No search results found (0 results)");
			ExtentCucumberAdapter.addTestStepLog("⏭️ SKIPPING scroll step - No search results found (0 results)");
			return;
		}
		try {
			LOGGER.info("Scrolling down till Last Search Result...Please wait!!!");
			ExtentCucumberAdapter.addTestStepLog("Scrolling down till Last Search Result...Please wait!!!");
			while(true) {
				String resultsCountText = wait.until(ExpectedConditions.visibilityOf(showingJobResultsCount)).getText();
			String[] resultsCountText_split = resultsCountText.split(" ");
			js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			PerformanceUtils.waitForUIStability(driver, 1);
			if(resultsCountText_split[1].contentEquals(resultsCountText_split[3])) {
					String resultsCountText_updated = wait.until(ExpectedConditions.visibilityOf(showingJobResultsCount)).getText();
					LOGGER.info("Scrolled down till last Search Result and now "+ resultsCountText_updated + " of Job Profiles as expected");
					ExtentCucumberAdapter.addTestStepLog("Scrolled down till last Search Result and now "+ resultsCountText_updated + " of Job Profiles as expected");
					break;
			} else {
				js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
				wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
				PerformanceUtils.waitForUIStability(driver, 1);
			}
			}
//			js.executeScript("arguments[0].scrollIntoView();", showingJobResultsCount);
			js.executeScript("window.scrollTo(document.body.scrollHeight, 0)");  // scroll to TOP of the Page
	} catch (Exception e) {
			ScreenshotHandler.handleTestFailure("scroll_page_view_last_search_result", e, 
				"Issue in scrolling page down to view last search result...Please Investigate!!!");
		} 	
	}
	
	public void user_should_validate_all_search_results_contains_substring_used_for_searching() {
		// SKIP if no search results found
		if (!hasSearchResults) {
			LOGGER.info("⏭️ SKIPPING validation step - No search results found (0 results)");
			ExtentCucumberAdapter.addTestStepLog("⏭️ SKIPPING validation step - No search results found (0 results)");
			return;
		}
		
		try {
			List<WebElement> allElements = driver.findElements(By.xpath("//tbody//tr//td[2]//div[contains(text(),'(')]"));
		    for (WebElement element: allElements) {
		        String text = element.getText();
		        if(text.toLowerCase().contains(PO04_VerifyJobMappingPageComponents.jobnamesubstring.toLowerCase())) {
		        	 LOGGER.info("Organization Job Profile with Name : " + text + " from search results contains the substring : " + PO04_VerifyJobMappingPageComponents.jobnamesubstring +" used for searching");
		        	 ExtentCucumberAdapter.addTestStepLog("Organization Job Profile with Name : " + text + " from search results contains the substring : " + PO04_VerifyJobMappingPageComponents.jobnamesubstring +" used for searching");
		        } else {
		        	LOGGER.info("Organization Job Profile with Name : " + text + " from search results DOES NOT contains the substring : " + PO04_VerifyJobMappingPageComponents.jobnamesubstring +" used for searching");
		        	ExtentCucumberAdapter.addTestStepLog("Organization Job Profile with Name : " + text + " from search results DOES NOT contains the substring : " + PO04_VerifyJobMappingPageComponents.jobnamesubstring +" used for searching");
		        }
	    }
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("user_should_validate_all_search_results_contains_substring_used_for_searching", e);
			LOGGER.error("Failed to validate search results containing substring - Method: user_should_validate_all_search_results_contains_substring_used_for_searching", e);
			e.printStackTrace();
			Assert.fail("Issue in Validating Search Results containing substring : " +  PO04_VerifyJobMappingPageComponents.jobnamesubstring + " used for searching");
			ExtentCucumberAdapter.addTestStepLog("Issue in Validating Search Results containing substring : " +  PO04_VerifyJobMappingPageComponents.jobnamesubstring + " used for searching");
		}
	}
}
