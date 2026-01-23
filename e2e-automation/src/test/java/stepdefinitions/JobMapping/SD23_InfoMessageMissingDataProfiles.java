package stepdefinitions.JobMapping;

import java.io.IOException;

import com.kfonetalentsuite.manager.PageObjectManager;
import com.kfonetalentsuite.webdriverManager.DriverManager;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

public class SD23_InfoMessageMissingDataProfiles extends DriverManager {	
	public SD23_InfoMessageMissingDataProfiles() {
		super();		
	}
	
	// Prerequisite Checks
	@Given("Skip scenario if first profile was not found")
	public void skip_scenario_if_first_profile_was_not_found() throws IOException {
		PageObjectManager.getInstance().getInfoMessageMissingDataProfiles().skipScenarioIfFirstProfileNotFound();
	}
	
	@Given("Skip scenario if second profile was not found")
	public void skip_scenario_if_second_profile_was_not_found() throws IOException {
		PageObjectManager.getInstance().getInfoMessageMissingDataProfiles().skipScenarioIfSecondProfileNotFound();
	}

	// Profile Info Message Verification Steps
	@Then("Find and verify profile with missing data has Info Message displayed")
	public void find_and_verify_profile_with_missing_data_has_info_message_displayed() throws IOException {
		PageObjectManager.getInstance().getInfoMessageMissingDataProfiles().find_and_verify_profile_with_missing_data_has_info_message_displayed();
	}

	@Then("Verify Info Message contains text about reduced match accuracy due to missing data")
	public void verify_info_message_contains_text_about_reduced_match_accuracy_due_to_missing_data() throws IOException {
		PageObjectManager.getInstance().getInfoMessageMissingDataProfiles().verify_info_message_contains_text_about_reduced_match_accuracy_due_to_missing_data();
	}

	// Second Profile Info Message Verification Steps
	@Then("Find and verify second profile with missing data has Info Message displayed")
	public void find_and_verify_second_profile_with_missing_data_has_info_message_displayed() throws IOException {
		PageObjectManager.getInstance().getInfoMessageMissingDataProfiles().find_and_verify_second_profile_with_missing_data_has_info_message_displayed();
	}

	@Then("Verify Info Message contains text about reduced match accuracy due to missing data for second profile")
	public void verify_info_message_contains_text_about_reduced_match_accuracy_due_to_missing_data_for_second_profile() throws IOException {
		PageObjectManager.getInstance().getInfoMessageMissingDataProfiles().verify_info_message_contains_text_about_reduced_match_accuracy_due_to_missing_data_for_second_profile();
	}

	// Job Comparison Navigation Steps
	@Then("Find profile with missing data and Info Message")
	public void find_profile_with_missing_data_and_info_message() throws IOException {
		PageObjectManager.getInstance().getInfoMessageMissingDataProfiles().find_profile_with_missing_data_and_info_message();
	}

	@Then("Find second profile with missing data and Info Message")
	public void find_second_profile_with_missing_data_and_info_message() throws IOException {
		PageObjectManager.getInstance().getInfoMessageMissingDataProfiles().find_second_profile_with_missing_data_and_info_message();
	}
	
	// Job Details Extraction and Comparison Steps
	@Then("Extract job details from profile with Info Message")
	public void extract_job_details_from_profile_with_info_message() throws IOException {
		PageObjectManager.getInstance().getInfoMessageMissingDataProfiles().extract_job_details_from_profile_with_info_message();
	}
	
	@Then("Extract job details from Job Comparison page")
	public void extract_job_details_from_job_comparison_page() throws IOException {
		PageObjectManager.getInstance().getInfoMessageMissingDataProfiles().extract_job_details_from_job_comparison_page();
	}
	
	@Then("Verify job details match between Job Mapping and Job Comparison pages")
	public void verify_job_details_match_between_job_mapping_and_job_comparison_pages() throws IOException {
		PageObjectManager.getInstance().getInfoMessageMissingDataProfiles().verify_job_details_match_between_job_mapping_and_job_comparison_pages();
	}

	// Second Profile Job Details Extraction and Comparison Steps
	@Then("Extract job details from second profile with Info Message")
	public void extract_job_details_from_second_profile_with_info_message() throws IOException {
		PageObjectManager.getInstance().getInfoMessageMissingDataProfiles().extract_job_details_from_second_profile_with_info_message();
	}
	
	@Then("Extract job details from Job Comparison page for second profile")
	public void extract_job_details_from_job_comparison_page_for_second_profile() throws IOException {
		PageObjectManager.getInstance().getInfoMessageMissingDataProfiles().extract_job_details_from_job_comparison_page_for_second_profile();
	}
	
	@Then("Verify job details match between Job Mapping and Job Comparison pages for second profile")
	public void verify_job_details_match_between_job_mapping_and_job_comparison_pages_for_second_profile() throws IOException {
		PageObjectManager.getInstance().getInfoMessageMissingDataProfiles().verify_job_details_match_between_job_mapping_and_job_comparison_pages_for_second_profile();
	}

	@Then("Click on {string} button for profile with Info Message")
	public void click_on_button_for_profile_with_info_message(String buttonText) throws IOException {
		PageObjectManager.getInstance().getInfoMessageMissingDataProfiles().click_on_button_for_profile_with_info_message(buttonText);
	}

	@Then("Click on {string} button for second profile with Info Message")
	public void click_on_button_for_second_profile_with_info_message(String buttonText) throws IOException {
		PageObjectManager.getInstance().getInfoMessageMissingDataProfiles().click_on_button_for_second_profile_with_info_message(buttonText);
	}

	@Then("Verify user is navigated to Job Comparison page")
	public void verify_user_is_navigated_to_job_comparison_page() throws IOException {
		PageObjectManager.getInstance().getInfoMessageMissingDataProfiles().verify_user_is_navigated_to_job_comparison_page();
	}

	// Job Comparison Page Verification Steps
	@Then("Verify Info Message is still displayed in Job Comparison page")
	public void verify_info_message_is_still_displayed_in_job_comparison_page() throws IOException {
		PageObjectManager.getInstance().getInfoMessageMissingDataProfiles().verify_info_message_is_still_displayed_in_job_comparison_page();
	}

	@Then("Verify Info Message contains same text about reduced match accuracy")
	public void verify_info_message_contains_same_text_about_reduced_match_accuracy() throws IOException {
		PageObjectManager.getInstance().getInfoMessageMissingDataProfiles().verify_info_message_contains_same_text_about_reduced_match_accuracy();
	}

	// Second Profile Job Comparison Page Verification Steps
	@Then("Verify Info Message is still displayed in Job Comparison page for second profile")
	public void verify_info_message_is_still_displayed_in_job_comparison_page_for_second_profile() throws IOException {
		PageObjectManager.getInstance().getInfoMessageMissingDataProfiles().verify_info_message_is_still_displayed_in_job_comparison_page_for_second_profile();
	}

	@Then("Verify Info Message contains same text about reduced match accuracy for second profile")
	public void verify_info_message_contains_same_text_about_reduced_match_accuracy_for_second_profile() throws IOException {
		PageObjectManager.getInstance().getInfoMessageMissingDataProfiles().verify_info_message_contains_same_text_about_reduced_match_accuracy_for_second_profile();
	}

	@Then("Navigate back to Job Mapping page from Job Comparison")
	public void navigate_back_to_job_mapping_page_from_job_comparison() throws IOException {
		PageObjectManager.getInstance().getInfoMessageMissingDataProfiles().navigate_back_to_job_mapping_page_from_job_comparison();
	}

}

