package com.kfonetalentsuite.pageobjects.JobMapping;

import java.io.IOException;
import java.util.NoSuchElementException;

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

import com.kfonetalentsuite.utils.JobMapping.Utilities;
import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.PageObjectHelper;
import com.kfonetalentsuite.webdriverManager.DriverManager;

public class PO20_ManualMappingofSPinAutoAI {
	WebDriver driver = DriverManager.getDriver();	

	protected static final Logger LOGGER = (Logger) LogManager.getLogger();
	PO20_ManualMappingofSPinAutoAI manualMappingofSPinAutoAI;
	
	// THREAD-SAFE: Each thread gets its own isolated state for parallel execution
	public static ThreadLocal<Integer> row = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<String> orgJobName = ThreadLocal.withInitial(() -> null);
	public static ThreadLocal<String> orgJobCode = ThreadLocal.withInitial(() -> null);

	public PO20_ManualMappingofSPinAutoAI() throws IOException {
		// super();
		PageFactory.initElements(driver, this);
		// TODO Auto-generated constructor stub
	}

	WebDriverWait wait = DriverManager.getWait();
	Utilities utils = new Utilities();
	JavascriptExecutor js = (JavascriptExecutor) driver;
	
	//XPATHs
	@FindBy(xpath = "//div[@data-testid='loader']//img")
	@CacheLookup
	WebElement pageLoadSpinner2;
	
	@FindBy(xpath = "//tbody//tr[1]//button[contains(text(),'Find')]")
	@CacheLookup
	WebElement findMatchBtn;
	
	@FindBy(xpath = "//tbody//tr[2]//button[contains(text(),'different profile')]")
	@CacheLookup
	WebElement searchDifferntSPBtn;
	
	//METHODs
	public void verify_profile_with_no_bic_mapping_is_displaying_on_top_after_sorting() throws InterruptedException {
		try {
			// PERFORMANCE: Single comprehensive wait
			PerformanceUtils.waitForSpinnersToDisappear(driver);
			PerformanceUtils.waitForPageReady(driver, 3);
			
			// Find the button to determine profile state
			WebElement button = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//tbody//tr[2]//button[not(contains(@id,'view'))] | //tbody//tr[1]//button[contains(text(),'Find')]")));
			js.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});", button);
			String text = button.getText();
			
			if(text.contains("Publish")) {
				// All profiles are already mapped
				PO21_MapDifferentSPtoProfileInAutoAI.mapSP.set(false);
				PageObjectHelper.log(LOGGER, "Currently, All Profiles in Job Mapping are Mapped with BIC Profiles");
				
				driver.navigate().refresh();
				PerformanceUtils.waitForSpinnersToDisappear(driver);
				PerformanceUtils.waitForPageReady(driver, 3);
			} else {
				// Found profile with no BIC mapping
				PO21_MapDifferentSPtoProfileInAutoAI.mapSP.set(true);
				PageObjectHelper.log(LOGGER, "Job profile with No BIC Profile Mapping is found");
				
				// PERFORMANCE: Single wait for page ready
				PerformanceUtils.waitForPageReady(driver, 3);
				
				// Extract job details
				WebElement jobName = wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("//tbody//tr[1]//td[2]//div[contains(text(),'(')]")));
				String jobname1 = jobName.getText();
				
				PO21_MapDifferentSPtoProfileInAutoAI.orgJobName.set(jobname1.split("-", 2)[0].trim());
				PO21_MapDifferentSPtoProfileInAutoAI.orgJobCode.set(
					jobname1.split("-", 2)[1].trim().substring(1, jobname1.split("-", 2)[1].length()-2));
				
				PageObjectHelper.log(LOGGER, "Organization Job name / Job Code of Profile with No BIC Profile Mapped : " + 
					PO21_MapDifferentSPtoProfileInAutoAI.orgJobName.get() + "/" + PO21_MapDifferentSPtoProfileInAutoAI.orgJobCode.get());
				
				// Extract job grade
				WebElement jobGrade = driver.findElement(By.xpath("//tbody//tr[1]//td[3]//div[1]"));
				String gradeText = jobGrade.getText();
				PO21_MapDifferentSPtoProfileInAutoAI.orgJobGrade.set(
					(gradeText.equals("-") || gradeText.isEmpty() || gradeText.isBlank()) ? "NULL" : gradeText);
				
				// Extract job department
				WebElement jobDepartment = driver.findElement(By.xpath("//tbody//tr[1]//td[4]//div[1]"));
				String deptText = jobDepartment.getText();
				PO21_MapDifferentSPtoProfileInAutoAI.orgJobDepartment.set(
					(deptText.equals("-") || deptText.isEmpty() || deptText.isBlank()) ? "NULL" : deptText);
				
				// Extract job function
				WebElement jobFunction = driver.findElement(By.xpath("//tbody//tr[2]//div//span[2]"));
				String funcText = jobFunction.getText();
				PO21_MapDifferentSPtoProfileInAutoAI.orgJobFunction.set(
					(funcText.equals("-") || funcText.isEmpty() || funcText.isBlank()) ? "NULL" : funcText);
			}
		} catch(NoSuchElementException e) {
			PO21_MapDifferentSPtoProfileInAutoAI.mapSP.set(false);
			PageObjectHelper.log(LOGGER, "Currently, All Profiles in Job Mapping are Mapped with BIC Profiles");
			PageObjectHelper.handleError(LOGGER, "verify_profile_with_no_bic_mapping_is_displaying_on_top_after_sorting",
				"No profile with No BIC mapping found", e);
		}
	}
	
	public void click_on_find_match_button() {
		if(PO21_MapDifferentSPtoProfileInAutoAI.mapSP.get()) {
			try {
				// Click with fallback strategies
				try {
					wait.until(ExpectedConditions.elementToBeClickable(findMatchBtn)).click();
				} catch (Exception e) {
					try {
						js.executeScript("arguments[0].click();", findMatchBtn);
					} catch (Exception s) {
						utils.jsClick(driver, findMatchBtn);
					}
				}
				
				PageObjectHelper.log(LOGGER, "Clicked on Find Match button of Organization Job : " + 
					PO21_MapDifferentSPtoProfileInAutoAI.orgJobName.get());
				
				// PERFORMANCE: Wait for spinners to disappear
				PerformanceUtils.waitForSpinnersToDisappear(driver);
			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER, "click_on_find_match_button",
					"Issue in clicking on Find Match button of Organization job name " + 
					PO21_MapDifferentSPtoProfileInAutoAI.orgJobName.get(), e);
			}
		}
	}
	
	public void user_should_verify_search_a_different_profile_button_is_displaying_on_manually_mapped_success_profile() {
		if(PO21_MapDifferentSPtoProfileInAutoAI.manualMapping.get()) {
			try {
				PerformanceUtils.waitForPageReady(driver, 2);
				
				Assert.assertTrue(wait.until(ExpectedConditions.elementToBeClickable(searchDifferntSPBtn)).isDisplayed(),
					"Search a Different Profile button should be visible");
				
				PageObjectHelper.log(LOGGER, "Search a Different Profile is Displaying on matched success profile which is on the Top of the Profiles List");
			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER, "user_should_verify_search_a_different_profile_button_is_displaying_on_manually_mapped_success_profile",
					"Search a different profile is NOT displaying on Manually Mapped success profile which is on the Top of the Profiles List", e);
			}
		}
	}
	
	public void click_on_manually_mapped_profile_name_of_job_profile_on_top_of_profiles_list() {
		if(PO21_MapDifferentSPtoProfileInAutoAI.manualMapping.get()) { 
			try {
				// PERFORMANCE: Wait for spinners to disappear
				PerformanceUtils.waitForSpinnersToDisappear(driver);
				
				// Locate and get profile name
				WebElement linkedMappedProfile = wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("//div[@id='kf-job-container']//div//table//tbody//tr[1]//td[1]//div")));
				String MappedProfileNameText = linkedMappedProfile.getText();
				PO21_MapDifferentSPtoProfileInAutoAI.mappedSuccessPrflName.set(MappedProfileNameText);
				
				// Click with fallback strategies
				try {
					linkedMappedProfile.click();
				} catch (Exception e) {
					try {
						js.executeScript("arguments[0].click();", linkedMappedProfile);
					} catch (Exception s) {
						utils.jsClick(driver, linkedMappedProfile);
					}
				}
				
				PageObjectHelper.log(LOGGER, "Clicked on Manually Mapped Profile with name " + MappedProfileNameText + 
					" of Organization Job " + PO21_MapDifferentSPtoProfileInAutoAI.orgJobName.get() + 
					" which is on the Top of the Profiles List");
				
				// PERFORMANCE: Wait for spinners after click
				PerformanceUtils.waitForSpinnersToDisappear(driver);
			} catch (Exception e) {
				PageObjectHelper.handleError(LOGGER, "click_on_manually_mapped_profile_name_of_job_profile_on_top_of_profiles_list",
					"Issue in clicking Manually Mapped Profile linked with job name " + 
					PO21_MapDifferentSPtoProfileInAutoAI.orgJobName.get(), e);
			}
		}
	}
		
}
