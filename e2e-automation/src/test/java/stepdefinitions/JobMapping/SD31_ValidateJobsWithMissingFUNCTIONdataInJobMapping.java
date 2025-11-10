package stepdefinitions.JobMapping;

import java.io.IOException;

import com.kfonetalentsuite.manager.PageObjectManager;
import com.kfonetalentsuite.webdriverManager.DriverManager;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SD31_ValidateJobsWithMissingFUNCTIONdataInJobMapping extends DriverManager {
	PageObjectManager validateJobsWithMissingFUNCTIONdataInJobMapping = new PageObjectManager();
	
	public SD31_ValidateJobsWithMissingFUNCTIONdataInJobMapping() {
		super();		
	}

	@Then("Find job profile in Job Mapping page where Function is missing")
	public void find_job_profile_in_job_mapping_page_where_function_is_missing() throws IOException {
		validateJobsWithMissingFUNCTIONdataInJobMapping.getValidateJobsWithMissingFUNCTIONdataInJobMapping().find_job_profile_in_job_mapping_page_where_function_is_missing();
	}

	@Then("Extract FUNCTION job details from found profile in Job Mapping page")
	public void extract_job_details_from_found_profile_in_job_mapping_page() throws IOException {
		validateJobsWithMissingFUNCTIONdataInJobMapping.getValidateJobsWithMissingFUNCTIONdataInJobMapping().extract_job_details_from_found_profile_in_job_mapping_page();
	}

	@Then("Verify user is navigated to Jobs with Missing FUNCTION Data screen")
	public void verify_user_is_navigated_to_jobs_with_missing_function_data_screen() throws IOException {
		validateJobsWithMissingFUNCTIONdataInJobMapping.getValidateJobsWithMissingFUNCTIONdataInJobMapping().verify_user_is_navigated_to_jobs_with_missing_data_screen();
	}

	@Then("Search for the extracted FUNCTION job profile by name in Jobs Missing Data screen")
	public void search_for_the_extracted_function_job_profile_by_name_in_jobs_missing_data_screen() throws IOException {
		validateJobsWithMissingFUNCTIONdataInJobMapping.getValidateJobsWithMissingFUNCTIONdataInJobMapping().search_for_the_extracted_job_profile_by_name_in_jobs_missing_data_screen();
	}

	@Then("Verify FUNCTION job profile is found and displayed in Jobs Missing Data screen search results")
	public void verify_function_job_profile_is_found_and_displayed_in_jobs_missing_data_screen_search_results() throws IOException {
		validateJobsWithMissingFUNCTIONdataInJobMapping.getValidateJobsWithMissingFUNCTIONdataInJobMapping().verify_job_profile_is_found_and_displayed_in_jobs_missing_data_screen_search_results();
	}

	@Then("Extract FUNCTION job details from found profile in Jobs Missing Data screen")
	public void extract_function_job_details_from_found_profile_in_jobs_missing_data_screen() throws IOException {
		validateJobsWithMissingFUNCTIONdataInJobMapping.getValidateJobsWithMissingFUNCTIONdataInJobMapping().extract_job_details_from_found_profile_in_jobs_missing_data_screen();
	}

	@Then("Verify all FUNCTION job details match between Job Mapping page and Jobs Missing Data screen")
	public void verify_all_function_job_details_match_between_job_mapping_page_and_jobs_missing_data_screen() throws IOException {
		validateJobsWithMissingFUNCTIONdataInJobMapping.getValidateJobsWithMissingFUNCTIONdataInJobMapping().verify_all_job_details_match_between_job_mapping_page_and_jobs_missing_data_screen();
	}

	@Then("Click on Close button to return to Job Mapping page from FUNCTION validation")
	public void click_on_close_button_to_return_to_job_mapping_page_from_function_validation() throws IOException {
		validateJobsWithMissingFUNCTIONdataInJobMapping.getValidateJobsWithMissingFUNCTIONdataInJobMapping().click_on_close_button_to_return_to_job_mapping_page();
	}

	@Then("Verify user is back on Job Mapping page after FUNCTION validation")
	public void verify_user_is_back_on_job_mapping_page_after_function_validation() throws IOException {
		validateJobsWithMissingFUNCTIONdataInJobMapping.getValidateJobsWithMissingFUNCTIONdataInJobMapping().verify_user_is_back_on_job_mapping_page();
	}

	@Then("Find job in Jobs Missing FUNCTION Data screen where Function is N\\/A")
	public void find_job_in_jobs_missing_function_data_screen_where_function_is_na() throws IOException {
		validateJobsWithMissingFUNCTIONdataInJobMapping.getValidateJobsWithMissingFUNCTIONdataInJobMapping().find_job_in_jobs_missing_data_screen_where_function_is_na();
	}

	@Then("Extract all available job details from Jobs with Missing FUNCTION Data screen")
	public void extract_all_available_job_details_from_jobs_with_missing_function_data_screen() throws IOException {
		validateJobsWithMissingFUNCTIONdataInJobMapping.getValidateJobsWithMissingFUNCTIONdataInJobMapping().extract_all_available_job_details_from_jobs_with_missing_data_screen();
	}

	@When("Search for the extracted FUNCTION job profile by name in Job Mapping page")
	public void search_for_the_extracted_function_job_profile_by_name_in_job_mapping_page() throws IOException {
		validateJobsWithMissingFUNCTIONdataInJobMapping.getValidateJobsWithMissingFUNCTIONdataInJobMapping().search_for_the_extracted_job_profile_by_name_in_job_mapping_page();
	}

	@Then("Verify FUNCTION job profile is found and displayed in search results")
	public void verify_function_job_profile_is_found_and_displayed_in_search_results() throws IOException {
		validateJobsWithMissingFUNCTIONdataInJobMapping.getValidateJobsWithMissingFUNCTIONdataInJobMapping().verify_job_profile_is_found_and_displayed_in_search_results();
	}

	@Then("Extract FUNCTION job details from searched profile in Job Mapping page")
	public void extract_function_job_details_from_searched_profile_in_job_mapping_page() throws IOException {
		validateJobsWithMissingFUNCTIONdataInJobMapping.getValidateJobsWithMissingFUNCTIONdataInJobMapping().extract_job_details_from_searched_profile_in_job_mapping_page();
	}

	@Then("Verify all FUNCTION job details match between Jobs Missing Data screen and Job Mapping page")
	public void verify_all_function_job_details_match_between_jobs_missing_data_screen_and_job_mapping_page() throws IOException {
		validateJobsWithMissingFUNCTIONdataInJobMapping.getValidateJobsWithMissingFUNCTIONdataInJobMapping().verify_all_job_details_match_between_jobs_missing_data_screen_and_job_mapping_page();
	}

	@Then("Verify Info Message is displayed on searched profile indicating missing FUNCTION data")
	public void verify_info_message_is_displayed_on_searched_profile_indicating_missing_function_data() throws IOException {
		validateJobsWithMissingFUNCTIONdataInJobMapping.getValidateJobsWithMissingFUNCTIONdataInJobMapping().verify_info_message_is_displayed_on_searched_profile_indicating_missing_function_data();
	}

	@Then("Verify Info Message contains text about reduced match accuracy due to missing FUNCTION data")
	public void verify_info_message_contains_text_about_reduced_match_accuracy_due_to_missing_function_data() throws IOException {
		// This step is typically handled by the info message display verification
		// The text verification is already included in the previous step
		validateJobsWithMissingFUNCTIONdataInJobMapping.getValidateJobsWithMissingFUNCTIONdataInJobMapping().verify_info_message_is_displayed_on_searched_profile_indicating_missing_function_data();
	}

}
