package stepdefinitions.JobMapping;

import java.io.IOException;

import com.kfonetalentsuite.manager.PageObjectManager;
import com.kfonetalentsuite.webdriverManager.DriverManager;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SD29_ValidateJobsWithMissingGRADEdataInJobMapping extends DriverManager {
	PageObjectManager validateJobsWithMissingGRADEdataInJobMapping = new PageObjectManager();
	
	public SD29_ValidateJobsWithMissingGRADEdataInJobMapping() {
		super();		
	}

	// Jobs with Missing Data Screen Navigation and Verification Steps
	@Then("Verify user is navigated to Jobs with Missing Data screen")
	public void verify_user_is_navigated_to_jobs_with_missing_data_screen() throws IOException {
		validateJobsWithMissingGRADEdataInJobMapping.getValidateJobsWithMissingGRADEdataInJobMapping().verify_user_is_navigated_to_jobs_with_missing_data_screen();
	}

	@Then("Find job in Jobs Missing Data screen where Grade is N\\/A")
	public void find_job_in_jobs_missing_data_screen_where_grade_is_na() throws IOException {
		validateJobsWithMissingGRADEdataInJobMapping.getValidateJobsWithMissingGRADEdataInJobMapping().find_job_in_jobs_missing_data_screen_where_grade_is_na_but_department_or_function_subfunction_is_not_na();
	}

	@Then("Extract all available job details from Jobs with Missing Data screen")
	public void extract_all_available_job_details_from_jobs_with_missing_data_screen() throws IOException {
		validateJobsWithMissingGRADEdataInJobMapping.getValidateJobsWithMissingGRADEdataInJobMapping().extract_all_available_job_details_from_jobs_with_missing_data_screen();
	}

	@Then("Click on Close button to return to Job Mapping page")
	public void click_on_close_button_to_return_to_job_mapping_page() throws IOException {
		try {
			validateJobsWithMissingGRADEdataInJobMapping.getValidateJobsWithMissingGRADEdataInJobMapping().click_on_close_button_to_return_to_job_mapping_page();
		} catch (Exception e) {
			// If regular close fails, try force close to ensure Scenario 2 can run
			System.out.println("Regular close failed, trying force close for next scenario...");
			validateJobsWithMissingGRADEdataInJobMapping.getValidateJobsWithMissingGRADEdataInJobMapping().force_close_missing_data_screen_for_next_scenario();
		}
	}

	@Then("Verify user is back on Job Mapping page")
	public void verify_user_is_back_on_job_mapping_page() throws IOException {
		validateJobsWithMissingGRADEdataInJobMapping.getValidateJobsWithMissingGRADEdataInJobMapping().verify_user_is_back_on_job_mapping_page();
	}

	// Job Search and Verification Steps in Job Mapping Page
	@When("Search for the extracted job profile by name in Job Mapping page")
	public void search_for_the_extracted_job_profile_by_name_in_job_mapping_page() throws IOException {
		validateJobsWithMissingGRADEdataInJobMapping.getValidateJobsWithMissingGRADEdataInJobMapping().search_for_the_extracted_job_profile_by_name_in_job_mapping_page();
	}

	@Then("Verify job profile is found and displayed in search results")
	public void verify_job_profile_is_found_and_displayed_in_search_results() throws IOException {
		validateJobsWithMissingGRADEdataInJobMapping.getValidateJobsWithMissingGRADEdataInJobMapping().verify_job_profile_is_found_and_displayed_in_search_results();
	}

	@Then("Extract job details from searched profile in Job Mapping page")
	public void extract_job_details_from_searched_profile_in_job_mapping_page() throws IOException {
		validateJobsWithMissingGRADEdataInJobMapping.getValidateJobsWithMissingGRADEdataInJobMapping().extract_job_details_from_searched_profile_in_job_mapping_page();
	}

	// Job Details Comparison and Info Message Verification Steps
	@Then("Verify all job details match between Jobs Missing Data screen and Job Mapping page")
	public void verify_all_job_details_match_between_jobs_missing_data_screen_and_job_mapping_page() throws IOException {
		validateJobsWithMissingGRADEdataInJobMapping.getValidateJobsWithMissingGRADEdataInJobMapping().verify_all_job_details_match_between_jobs_missing_data_screen_and_job_mapping_page();
	}

	@Then("Verify Info Message is displayed on searched profile indicating missing Grade data")
	public void verify_info_message_is_displayed_on_searched_profile_indicating_missing_grade_data() throws IOException {
		validateJobsWithMissingGRADEdataInJobMapping.getValidateJobsWithMissingGRADEdataInJobMapping().verify_info_message_is_displayed_on_searched_profile_indicating_missing_grade_data();
	}

	// ===== REVERSE SCENARIO STEP DEFINITIONS =====
	
	@Then("Find job profile in Job Mapping page where Grade is missing")
	public void find_job_profile_in_job_mapping_page_where_grade_is_missing() throws IOException {
		validateJobsWithMissingGRADEdataInJobMapping.getValidateJobsWithMissingGRADEdataInJobMapping().find_job_profile_in_job_mapping_page_where_grade_is_missing();
	}

	@Then("Extract job details from found profile in Job Mapping page")
	public void extract_job_details_from_found_profile_in_job_mapping_page() throws IOException {
		validateJobsWithMissingGRADEdataInJobMapping.getValidateJobsWithMissingGRADEdataInJobMapping().extract_job_details_from_found_profile_in_job_mapping_page();
	}

	@Then("Search for the extracted job profile by name in Jobs Missing Data screen")
	public void search_for_the_extracted_job_profile_by_name_in_jobs_missing_data_screen() throws IOException {
		validateJobsWithMissingGRADEdataInJobMapping.getValidateJobsWithMissingGRADEdataInJobMapping().search_for_the_extracted_job_profile_by_name_in_jobs_missing_data_screen();
	}

	@Then("Verify job profile is found and displayed in Jobs Missing Data screen search results")
	public void verify_job_profile_is_found_and_displayed_in_jobs_missing_data_screen_search_results() throws IOException {
		validateJobsWithMissingGRADEdataInJobMapping.getValidateJobsWithMissingGRADEdataInJobMapping().verify_job_profile_is_found_and_displayed_in_jobs_missing_data_screen_search_results();
	}

	@Then("Extract job details from found profile in Jobs Missing Data screen")
	public void extract_job_details_from_found_profile_in_jobs_missing_data_screen() throws IOException {
		validateJobsWithMissingGRADEdataInJobMapping.getValidateJobsWithMissingGRADEdataInJobMapping().extract_job_details_from_found_profile_in_jobs_missing_data_screen();
	}

	@Then("Verify all job details match between Job Mapping page and Jobs Missing Data screen")
	public void verify_all_job_details_match_between_job_mapping_page_and_jobs_missing_data_screen() throws IOException {
		validateJobsWithMissingGRADEdataInJobMapping.getValidateJobsWithMissingGRADEdataInJobMapping().verify_all_job_details_match_between_job_mapping_page_and_jobs_missing_data_screen();
	}

}
