package stepdefinitions.functional.JobMapping;

import java.io.IOException;

import com.JobMapping.manager.PageObjectManager;
import com.JobMapping.webdriverManager.DriverManager;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SD28_VerifyInfoMessageForManualMappingProfiles extends DriverManager {
	PageObjectManager verifyInfoMessageForManualMappingProfiles = new PageObjectManager();
	
	public SD28_VerifyInfoMessageForManualMappingProfiles() {
		super();		
	}

	// Manual Mapping Filters
	@When("User is in Job Mapping page with Manual Mapping filters applied")
	public void user_is_in_job_mapping_page_with_manual_mapping_filters_applied() throws IOException {
		verifyInfoMessageForManualMappingProfiles.getVerifyInfoMessageForManualMappingProfiles().user_is_in_job_mapping_page_with_manual_mapping_filters_applied();
	}

	// First Manual Profile Info Message Verification Steps
	@Then("Find and verify manually mapped profile with missing data has Info Message displayed")
	public void find_and_verify_manually_mapped_profile_with_missing_data_has_info_message_displayed() throws IOException {
		verifyInfoMessageForManualMappingProfiles.getVerifyInfoMessageForManualMappingProfiles().find_and_verify_manually_mapped_profile_with_missing_data_has_info_message_displayed();
	}

	@Then("Verify Info Message contains text about reduced match accuracy due to missing data for manual mapping")
	public void verify_info_message_contains_text_about_reduced_match_accuracy_due_to_missing_data_for_manual_mapping() throws IOException {
		verifyInfoMessageForManualMappingProfiles.getVerifyInfoMessageForManualMappingProfiles().verify_info_message_contains_text_about_reduced_match_accuracy_due_to_missing_data_for_manual_mapping();
	}

	// Second Manual Profile Info Message Verification Steps
	@Then("Find and verify second manually mapped profile with missing data has Info Message displayed")
	public void find_and_verify_second_manually_mapped_profile_with_missing_data_has_info_message_displayed() throws IOException {
		verifyInfoMessageForManualMappingProfiles.getVerifyInfoMessageForManualMappingProfiles().find_and_verify_second_manually_mapped_profile_with_missing_data_has_info_message_displayed();
	}

	@Then("Verify Info Message contains text about reduced match accuracy due to missing data for second manually mapped profile")
	public void verify_info_message_contains_text_about_reduced_match_accuracy_due_to_missing_data_for_second_manually_mapped_profile() throws IOException {
		verifyInfoMessageForManualMappingProfiles.getVerifyInfoMessageForManualMappingProfiles().verify_info_message_contains_text_about_reduced_match_accuracy_due_to_missing_data_for_second_manually_mapped_profile();
	}

	// Manual Mapping Navigation Steps
	@Then("Find manually mapped profile with missing data and Info Message")
	public void find_manually_mapped_profile_with_missing_data_and_info_message() throws IOException {
		verifyInfoMessageForManualMappingProfiles.getVerifyInfoMessageForManualMappingProfiles().find_manually_mapped_profile_with_missing_data_and_info_message();
	}

	@Then("Find second manually mapped profile with missing data and Info Message")
	public void find_second_manually_mapped_profile_with_missing_data_and_info_message() throws IOException {
		verifyInfoMessageForManualMappingProfiles.getVerifyInfoMessageForManualMappingProfiles().find_second_manually_mapped_profile_with_missing_data_and_info_message();
	}
	
	// Job Details Extraction and Comparison Steps
	@Then("Extract job details from manually mapped profile with Info Message")
	public void extract_job_details_from_manually_mapped_profile_with_info_message() throws IOException {
		verifyInfoMessageForManualMappingProfiles.getVerifyInfoMessageForManualMappingProfiles().extract_job_details_from_manually_mapped_profile_with_info_message();
	}
	
	@Then("Extract job details from Manual Mapping page")
	public void extract_job_details_from_manual_mapping_page() throws IOException {
		verifyInfoMessageForManualMappingProfiles.getVerifyInfoMessageForManualMappingProfiles().extract_job_details_from_manual_mapping_page();
	}
	
	@Then("Verify job details match between Job Mapping and Manual Mapping pages")
	public void verify_job_details_match_between_job_mapping_and_manual_mapping_pages() throws IOException {
		verifyInfoMessageForManualMappingProfiles.getVerifyInfoMessageForManualMappingProfiles().verify_job_details_match_between_job_mapping_and_manual_mapping_pages();
	}

	// Second Manual Profile Job Details Extraction and Comparison Steps
	@Then("Extract job details from second manually mapped profile with Info Message")
	public void extract_job_details_from_second_manually_mapped_profile_with_info_message() throws IOException {
		verifyInfoMessageForManualMappingProfiles.getVerifyInfoMessageForManualMappingProfiles().extract_job_details_from_second_manually_mapped_profile_with_info_message();
	}
	
	@Then("Extract job details from Manual Mapping page for second manually mapped profile")
	public void extract_job_details_from_manual_mapping_page_for_second_manually_mapped_profile() throws IOException {
		verifyInfoMessageForManualMappingProfiles.getVerifyInfoMessageForManualMappingProfiles().extract_job_details_from_manual_mapping_page_for_second_manually_mapped_profile();
	}
	
	@Then("Verify job details match between Job Mapping and Manual Mapping pages for second manually mapped profile")
	public void verify_job_details_match_between_job_mapping_and_manual_mapping_pages_for_second_manually_mapped_profile() throws IOException {
		verifyInfoMessageForManualMappingProfiles.getVerifyInfoMessageForManualMappingProfiles().verify_job_details_match_between_job_mapping_and_manual_mapping_pages_for_second_manually_mapped_profile();
	}

	@Then("Click on {string} button for manually mapped profile with Info Message")
	public void click_on_button_for_manually_mapped_profile_with_info_message(String buttonText) throws IOException {
		verifyInfoMessageForManualMappingProfiles.getVerifyInfoMessageForManualMappingProfiles().click_on_button_for_manually_mapped_profile_with_info_message(buttonText);
	}

	@Then("Click on {string} button for second manually mapped profile with Info Message")
	public void click_on_button_for_second_manually_mapped_profile_with_info_message(String buttonText) throws IOException {
		verifyInfoMessageForManualMappingProfiles.getVerifyInfoMessageForManualMappingProfiles().click_on_button_for_second_manually_mapped_profile_with_info_message(buttonText);
	}

	@Then("Verify user is navigated to Manual Mapping page")
	public void verify_user_is_navigated_to_manual_mapping_page() throws IOException {
		verifyInfoMessageForManualMappingProfiles.getVerifyInfoMessageForManualMappingProfiles().verify_user_is_navigated_to_manual_mapping_page();
	}

	// Manual Mapping Page Verification Steps
	@Then("Verify Info Message is still displayed in Manual Mapping page")
	public void verify_info_message_is_still_displayed_in_manual_mapping_page() throws IOException {
		verifyInfoMessageForManualMappingProfiles.getVerifyInfoMessageForManualMappingProfiles().verify_info_message_is_still_displayed_in_manual_mapping_page();
	}

	@Then("Verify Info Message contains same text about reduced match accuracy for manual mapping")
	public void verify_info_message_contains_same_text_about_reduced_match_accuracy_for_manual_mapping() throws IOException {
		verifyInfoMessageForManualMappingProfiles.getVerifyInfoMessageForManualMappingProfiles().verify_info_message_contains_same_text_about_reduced_match_accuracy_for_manual_mapping();
	}

	// Second Manual Profile Manual Mapping Page Verification Steps
	@Then("Verify Info Message is still displayed in Manual Mapping page for second manually mapped profile")
	public void verify_info_message_is_still_displayed_in_manual_mapping_page_for_second_manually_mapped_profile() throws IOException {
		verifyInfoMessageForManualMappingProfiles.getVerifyInfoMessageForManualMappingProfiles().verify_info_message_is_still_displayed_in_manual_mapping_page_for_second_manually_mapped_profile();
	}

	@Then("Verify Info Message contains same text about reduced match accuracy for second manually mapped profile")
	public void verify_info_message_contains_same_text_about_reduced_match_accuracy_for_second_manually_mapped_profile() throws IOException {
		verifyInfoMessageForManualMappingProfiles.getVerifyInfoMessageForManualMappingProfiles().verify_info_message_contains_same_text_about_reduced_match_accuracy_for_second_manually_mapped_profile();
	}

	@Then("Navigate back to Job Mapping page from Manual Mapping")
	public void navigate_back_to_job_mapping_page_from_manual_mapping() throws IOException {
		verifyInfoMessageForManualMappingProfiles.getVerifyInfoMessageForManualMappingProfiles().navigate_back_to_job_mapping_page_from_manual_mapping();
	}

}
