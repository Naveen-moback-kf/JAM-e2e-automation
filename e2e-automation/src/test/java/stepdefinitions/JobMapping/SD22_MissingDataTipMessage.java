package stepdefinitions.JobMapping;

import java.io.IOException;

import com.kfonetalentsuite.manager.PageObjectManager;
import com.kfonetalentsuite.webdriverManager.DriverManager;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SD22_MissingDataTipMessage extends DriverManager {	
	public SD22_MissingDataTipMessage() {
		super();		
	}

	// Basic Verification Steps
	@Then("Verify Missing Data Tip Message is displaying on Job Mapping page")
	public void verify_missing_data_tip_message_is_displaying_on_job_mapping_page() throws IOException {
		PageObjectManager.getInstance().getMissingDataTipMessage().verify_missing_data_tip_message_is_displaying_on_job_mapping_page();
	}

	@Then("Verify Missing Data Tip Message contains correct count of jobs with missing data")
	public void verify_missing_data_tip_message_contains_correct_count_of_jobs_with_missing_data() throws IOException {
		PageObjectManager.getInstance().getMissingDataTipMessage().verify_missing_data_tip_message_contains_correct_count_of_jobs_with_missing_data();
	}

	// Content Verification Steps
	@Then("Verify Missing Data Tip Message contains text about jobs having missing data")
	public void verify_missing_data_tip_message_contains_text_about_jobs_having_missing_data() throws IOException {
		PageObjectManager.getInstance().getMissingDataTipMessage().verify_missing_data_tip_message_contains_text_about_jobs_having_missing_data();
	}

	@Then("Verify Missing Data Tip Message contains text about reduced match accuracy")
	public void verify_missing_data_tip_message_contains_text_about_reduced_match_accuracy() throws IOException {
		PageObjectManager.getInstance().getMissingDataTipMessage().verify_missing_data_tip_message_contains_text_about_reduced_match_accuracy();
	}

	// Interactive Elements Verification Steps
	@Then("Verify {string} link is present in Missing Data Tip Message")
	public void verify_link_is_present_in_missing_data_tip_message(String linkText) throws IOException {
		PageObjectManager.getInstance().getMissingDataTipMessage().verify_link_is_present_in_missing_data_tip_message(linkText);
	}

	@When("Click on {string} link in Missing Data Tip Message")
	public void click_on_link_in_missing_data_tip_message(String linkText) throws IOException {
		PageObjectManager.getInstance().getMissingDataTipMessage().click_on_link_in_missing_data_tip_message(linkText);
	}

	@Then("Verify user is navigated to appropriate page for viewing and re-uploading jobs")
	public void verify_user_is_navigated_to_appropriate_page_for_viewing_and_re_uploading_jobs() throws IOException {
		PageObjectManager.getInstance().getMissingDataTipMessage().verify_user_is_navigated_to_appropriate_page_for_viewing_and_re_uploading_jobs();
	}

	@Then("Navigate back to Job Mapping page")
	public void navigate_back_to_job_mapping_page() throws IOException {
		PageObjectManager.getInstance().getMissingDataTipMessage().navigate_back_to_job_mapping_page();
	}

	@Then("Verify Missing Data Tip Message is still displaying on Job Mapping page")
	public void verify_missing_data_tip_message_is_still_displaying_on_job_mapping_page() throws IOException {
		PageObjectManager.getInstance().getMissingDataTipMessage().verify_missing_data_tip_message_is_still_displaying_on_job_mapping_page();
	}

	@When("Click on Close button in Missing Data Tip Message")
	public void click_on_close_button_in_missing_data_tip_message() throws IOException {
		PageObjectManager.getInstance().getMissingDataTipMessage().click_on_close_button_in_missing_data_tip_message();
	}

	@Then("Verify Missing Data Tip Message is no longer displayed on Job Mapping page")
	public void verify_missing_data_tip_message_is_no_longer_displayed_on_job_mapping_page() throws Exception {
		PageObjectManager.getInstance().getMissingDataTipMessage().verify_missing_data_tip_message_is_no_longer_displayed_on_job_mapping_page();
	}

}

