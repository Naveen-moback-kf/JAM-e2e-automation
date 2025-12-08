package com.kfonetalentsuite.pageobjects.JobMapping;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.PageObjectHelper;

public class PO07_PublishJobFromComparisonScreen extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO07_PublishJobFromComparisonScreen.class);

	public static ThreadLocal<String> job1OrgName = ThreadLocal.withInitial(() -> "NOT_SET");

	private static final By COMPARE_SELECT_HEADER = By.xpath("//h1[@id='compare-desc']");
	private static final By JC_ORG_JOB_TITLE = By.xpath("//div[contains(@class, 'text-[24px] font-semibold')] | //h2[contains(@class, 'job-title')]");
	private static final By JC_PUBLISH_SELECT_BTN = By.xpath("//button[@id='publish-select-btn']");
	private static final By SELECT_BTNS_IN_JC = By.xpath("//div[@class='shadow']//div[contains(@id,'card-header')][1]//span");

	public PO07_PublishJobFromComparisonScreen() {
		super();
	}

	public void verify_user_landed_on_job_comparison_screen() {
		try {
			PerformanceUtils.waitForPageReady(driver, 10);
			String compareAndSelectHeaderText = getElementText(COMPARE_SELECT_HEADER);
			Assert.assertEquals(compareAndSelectHeaderText, "Which profile do you want to use for this job?");
			PageObjectHelper.log(LOGGER, "User landed on the Job Comparison screen successfully");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_user_landed_on_job_comparison_screen", "Issue in landing Job Comparison screen", e);
		}
	}

	public void select_second_profile_from_ds_suggestions_of_organization_job() {
		try {
			PerformanceUtils.waitForPageReady(driver, 2);
			List<WebElement> selectBtns = driver.findElements(SELECT_BTNS_IN_JC);

			for (int i = 0; i < selectBtns.size(); i++) {
				if (i == 2) {
					WebElement btn = selectBtns.get(i);
					wait.until(ExpectedConditions.visibilityOf(btn));
					if (!tryClickWithStrategies(btn)) {
						jsClick(btn);
					}
					String jobTitle = getElementText(JC_ORG_JOB_TITLE);
					PageObjectHelper.log(LOGGER, "Second Profile selected from DS Suggestions for job: " + jobTitle);
					break;
				}
			}
			PerformanceUtils.waitForUIStability(driver, 1);
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "select_second_profile_from_ds_suggestions_of_organization_job", "Issue in selecting Second Profile from DS Suggestions", e);
		}
	}

	public void click_on_publish_selected_button_in_job_comparison_page() {
		try {
			String jobTitle = getElementText(JC_ORG_JOB_TITLE);
			job1OrgName.set(jobTitle.split("-", 2)[0].trim());
			clickElement(JC_PUBLISH_SELECT_BTN);
			PageObjectHelper.log(LOGGER, "Clicked Publish Selected button for job: " + job1OrgName.get());
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_publish_selected_button_in_job_comparison_page", "Issue clicking Publish Selected button", e);
		}
	}
}
