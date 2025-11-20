package com.kfonetalentsuite.pageobjects.JobMapping;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
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

import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.ScreenshotHandler;
import com.kfonetalentsuite.utils.JobMapping.Utilities;
import com.kfonetalentsuite.utils.PageObjectHelper;
import com.kfonetalentsuite.webdriverManager.DriverManager;

public class PO07_PublishJobFromComparisonScreen {
	WebDriver driver = DriverManager.getDriver();	

	protected static final Logger LOGGER = (Logger) LogManager.getLogger();
	PO07_PublishJobFromComparisonScreen publishJobFromComparisonScreen;
	
	// THREAD-SAFE: Each thread gets its own isolated state for parallel execution
	public static ThreadLocal<String> job1OrgName = ThreadLocal.withInitial(() -> null);

	public PO07_PublishJobFromComparisonScreen() throws IOException {
		// super();
		PageFactory.initElements(driver, this);
	}

	WebDriverWait wait = DriverManager.getWait();
	Utilities utils = new Utilities();
	JavascriptExecutor js = (JavascriptExecutor) driver;
	LocalDate currentdate = LocalDate.now();
	Month currentMonth = currentdate.getMonth();
	
	// WebElements for Job Comparison Screen functionality
	@FindBy(xpath = "//h1[@id='compare-desc']")
	public WebElement CompareandSelectheader;
	
	@FindBy(xpath = "//span[@aria-label='Loading']")
	public WebElement pageLoadSpinner2;
	
	@FindBy(xpath = "//div[contains(@class, 'text-[24px] font-semibold')] | //h2[contains(@class, 'job-title')]")
	public WebElement JCpageOrgJobTitleHeader;
	
	@FindBy(xpath = "//button[@id='publish-select-btn']")
	public WebElement JCPagePublishSelectBtn;

	public void verify_user_landed_on_job_comparison_screen() {
		try {
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			String compareAndSelectHeaderText = wait.until(ExpectedConditions.visibilityOf(CompareandSelectheader)).getText();
			Assert.assertEquals(compareAndSelectHeaderText, "Which profile do you want to use for this job?");
			PageObjectHelper.log(LOGGER, "User landed on the Job Comparison screen successfully");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_user_landed_on_job_comparison_screen", 
				"Issue in landing Job Comparison screen", e);
		}
	}
	
public void select_second_profile_from_ds_suggestions_of_organization_job() {
	try {
		PerformanceUtils.waitForPageReady(driver, 2);
		List<WebElement> SelectBtnsInJcPage = driver.findElements(By.xpath("//div[@class='shadow']//div[contains(@id,'card-header')][1]//span"));
			for (int i=0; i<=SelectBtnsInJcPage.size(); i++)
			{
				if(i==2) {
					try {
						wait.until(ExpectedConditions.visibilityOf(SelectBtnsInJcPage.get(i))).click();
					} catch (Exception e) {
						try {
							wait.until(ExpectedConditions.visibilityOf(SelectBtnsInJcPage.get(i)));
							js.executeScript("arguments[0].click();", SelectBtnsInJcPage.get(i));
						} catch (Exception s) {
							wait.until(ExpectedConditions.visibilityOf(SelectBtnsInJcPage.get(i)));
							utils.jsClick(driver, SelectBtnsInJcPage.get(i));
						}
					}
					String JCpageOrgJobTitleHeaderText = wait.until(ExpectedConditions.visibilityOf(JCpageOrgJobTitleHeader)).getText();
					PageObjectHelper.log(LOGGER, "Second Profile selected from DS Suggestions for job: " + JCpageOrgJobTitleHeaderText);
			}
		}
		PerformanceUtils.waitForUIStability(driver, 1);
	} catch (Exception e) {
		PageObjectHelper.handleError(LOGGER, "select_second_profile_from_ds_suggestions_of_organization_job",
			"Issue in selecting Second Profile from DS Suggestions", e);
	}
	}

	public void click_on_publish_selected_button_in_job_comparison_page() {
		try {
		String JCpageOrgJobTitleHeaderText = wait.until(ExpectedConditions.visibilityOf(JCpageOrgJobTitleHeader)).getText();
		job1OrgName.set(JCpageOrgJobTitleHeaderText.split("-", 2)[0].trim());
		try {
				wait.until(ExpectedConditions.elementToBeClickable(JCPagePublishSelectBtn)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", JCPagePublishSelectBtn);
				} catch (Exception s) {
					utils.jsClick(driver, JCPagePublishSelectBtn);
				}
		}
		PageObjectHelper.log(LOGGER, "Clicked Publish Selected button for job: " + job1OrgName.get());
	} catch (Exception e) {
		PageObjectHelper.handleError(LOGGER, "click_on_publish_selected_button_in_job_comparison_page",
			"Issue clicking Publish Selected button", e);
	}
}
}

