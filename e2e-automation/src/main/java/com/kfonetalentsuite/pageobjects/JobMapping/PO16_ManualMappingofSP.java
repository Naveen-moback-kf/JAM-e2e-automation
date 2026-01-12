package com.kfonetalentsuite.pageobjects.JobMapping;
import static com.kfonetalentsuite.pageobjects.JobMapping.BasePageObject.JobMappingPage.*;

import java.util.NoSuchElementException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import com.kfonetalentsuite.utils.JobMapping.Utilities;

public class PO16_ManualMappingofSP extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO16_ManualMappingofSP.class);

	public static ThreadLocal<Integer> row = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<String> orgJobName = ThreadLocal.withInitial(() -> "NOT_SET");
	public static ThreadLocal<String> orgJobCode = ThreadLocal.withInitial(() -> "NOT_SET");
	public PO16_ManualMappingofSP() {
		super();
	}

	public void verify_profile_with_no_bic_mapping_is_displaying_on_top_after_sorting() throws InterruptedException {
		try {
			waitForSpinners();
			Utilities.waitForPageReady(driver, 3);

		WebElement button = Utilities.waitForVisible(wait, By.xpath(
				"//tbody//tr[2]//button[not(contains(@id,'view'))] | //tbody//tr[1]//button[contains(text(),'Find')]"));
			scrollToElement(button);
			String text = button.getText();

			if (text.contains("Publish")) {
				PO17_MapDifferentSPtoProfile.mapSP.set(false);
				LOGGER.info("Currently, All Profiles in Job Mapping are Mapped with BIC Profiles");
				refreshPage();
				waitForSpinners();
				Utilities.waitForPageReady(driver, 3);
			} else {
				PO17_MapDifferentSPtoProfile.mapSP.set(true);
				LOGGER.info("Job profile with No BIC Profile Mapping is found");
				Utilities.waitForPageReady(driver, 3);

				WebElement jobName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody//tr[1]//td[2]//div[contains(text(),'(')]")));
				String jobname1 = jobName.getText();

				PO17_MapDifferentSPtoProfile.orgJobName.set(jobname1.split("-", 2)[0].trim());
				PO17_MapDifferentSPtoProfile.orgJobCode.set(jobname1.split("-", 2)[1].trim().substring(1, jobname1.split("-", 2)[1].length() - 2));

				LOGGER.info("Organization Job name / Job Code of Profile with No BIC Profile Mapped : "
						+ PO17_MapDifferentSPtoProfile.orgJobName.get() + "/" + PO17_MapDifferentSPtoProfile.orgJobCode.get());

				WebElement jobGrade = driver.findElement(By.xpath("//tbody//tr[1]//td[3]//div[1]"));
				String gradeText = jobGrade.getText();
				PO17_MapDifferentSPtoProfile.orgJobGrade.set((gradeText.equals("-") || gradeText.isEmpty() || gradeText.isBlank()) ? "NULL" : gradeText);

				WebElement jobDepartment = driver.findElement(By.xpath("//tbody//tr[1]//td[4]//div[1]"));
				String deptText = jobDepartment.getText();
				PO17_MapDifferentSPtoProfile.orgJobDepartment.set((deptText.equals("-") || deptText.isEmpty() || deptText.isBlank()) ? "NULL" : deptText);

				WebElement jobFunction = driver.findElement(By.xpath("//tbody//tr[2]//div//span[2]"));
				String funcText = jobFunction.getText();
				PO17_MapDifferentSPtoProfile.orgJobFunction.set((funcText.equals("-") || funcText.isEmpty() || funcText.isBlank()) ? "NULL" : funcText);
			}
		} catch (NoSuchElementException e) {
			PO17_MapDifferentSPtoProfile.mapSP.set(false);
			LOGGER.info("Currently, All Profiles in Job Mapping are Mapped with BIC Profiles");
			Utilities.handleError(LOGGER, "verify_profile_with_no_bic_mapping_is_displaying_on_top_after_sorting", "No profile with No BIC mapping found", e);
		}
	}

	public void click_on_find_match_button() {
		if (PO17_MapDifferentSPtoProfile.mapSP.get()) {
			try {
				clickElement(FIND_MATCH_BTN);
				LOGGER.info("Clicked on Find Match button of Organization Job : " + PO17_MapDifferentSPtoProfile.orgJobName.get());
				waitForSpinners();
			} catch (Exception e) {
				Utilities.handleError(LOGGER, "click_on_find_match_button", "Issue in clicking on Find Match button of Organization job name " + PO17_MapDifferentSPtoProfile.orgJobName.get(), e);
			}
		}
	}

	public void user_should_verify_search_a_different_profile_button_is_displaying_on_manually_mapped_success_profile() {
		if (PO17_MapDifferentSPtoProfile.manualMapping.get()) {
			try {
				Utilities.waitForPageReady(driver, 2);
				Assert.assertTrue(waitForClickable(SEARCH_DIFFERENT_SP_BTN).isDisplayed(), "Search a Different Profile button should be visible");
				LOGGER.info("Search a Different Profile is Displaying on matched success profile which is on the Top of the Profiles List");
			} catch (Exception e) {
				Utilities.handleError(LOGGER, "user_should_verify_search_a_different_profile_button_is_displaying_on_manually_mapped_success_profile", "Search a different profile is NOT displaying on Manually Mapped success profile which is on the Top of the Profiles List", e);
			}
		}
	}

	public void click_on_manually_mapped_profile_name_of_job_profile_on_top_of_profiles_list() {
		if (PO17_MapDifferentSPtoProfile.manualMapping.get()) {
			try {
				waitForSpinners();
				WebElement linkedMappedProfile = waitForClickable(By.xpath("//div[@id='kf-job-container']//div//table//tbody//tr[1]//td[1]//div"));
				String MappedProfileNameText = linkedMappedProfile.getText();
				PO17_MapDifferentSPtoProfile.mappedSuccessPrflName.set(MappedProfileNameText);
				tryClickWithStrategies(linkedMappedProfile);
				LOGGER.info("Clicked on Manually Mapped Profile with name " + MappedProfileNameText + " of Organization Job " + PO17_MapDifferentSPtoProfile.orgJobName.get() + " which is on the Top of the Profiles List");
				waitForSpinners();
			} catch (Exception e) {
				Utilities.handleError(LOGGER, "click_on_manually_mapped_profile_name_of_job_profile_on_top_of_profiles_list", "Issue in clicking Manually Mapped Profile linked with job name " + PO17_MapDifferentSPtoProfile.orgJobName.get(), e);
			}
		}
	}
}

