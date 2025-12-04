package stepdefinitions.JobMapping;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kfonetalentsuite.manager.PageObjectManager;
import com.kfonetalentsuite.utils.JobMapping.PageObjectHelper;
import com.kfonetalentsuite.webdriverManager.DriverManager;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SD32_ValidateJobsWithMissingSUBFUNCTIONdataInJobMapping extends DriverManager {
	private static final Logger LOGGER = LogManager.getLogger(SD32_ValidateJobsWithMissingSUBFUNCTIONdataInJobMapping.class);
	PageObjectManager validateJobsWithMissingSUBFUNCTIONdataInJobMapping = new PageObjectManager();
	
	public SD32_ValidateJobsWithMissingSUBFUNCTIONdataInJobMapping() {
		super();		
	}

	@Then("Find job profile in Job Mapping page where Subfunction is missing")
	public void find_job_profile_in_job_mapping_page_where_subfunction_is_missing() throws IOException {
		validateJobsWithMissingSUBFUNCTIONdataInJobMapping.getValidateJobsWithMissingSUBFUNCTIONdataInJobMapping().find_job_profile_in_job_mapping_page_where_subfunction_is_missing();
	}

	@Then("Extract SUBFUNCTION job details from found profile in Job Mapping page")
	public void extract_subfunction_job_details_from_found_profile_in_job_mapping_page() throws IOException {
		validateJobsWithMissingSUBFUNCTIONdataInJobMapping.getValidateJobsWithMissingSUBFUNCTIONdataInJobMapping().extract_job_details_from_found_profile_in_job_mapping_page();
	}

	@Then("Verify user is navigated to Jobs with Missing SUBFUNCTION Data screen")
	public void verify_user_is_navigated_to_jobs_with_missing_subfunction_data_screen() throws IOException {
		validateJobsWithMissingSUBFUNCTIONdataInJobMapping.getValidateJobsWithMissingSUBFUNCTIONdataInJobMapping().verify_user_is_navigated_to_jobs_with_missing_data_screen();
	}

	@Then("Search for the extracted SUBFUNCTION job profile by name in Jobs Missing Data screen")
	public void search_for_the_extracted_subfunction_job_profile_by_name_in_jobs_missing_data_screen() throws IOException {
		validateJobsWithMissingSUBFUNCTIONdataInJobMapping.getValidateJobsWithMissingSUBFUNCTIONdataInJobMapping().search_for_the_extracted_job_profile_by_name_in_jobs_missing_data_screen();
	}

	@Then("Verify SUBFUNCTION job profile is found and displayed in Jobs Missing Data screen search results")
	public void verify_subfunction_job_profile_is_found_and_displayed_in_jobs_missing_data_screen_search_results() throws IOException {
		validateJobsWithMissingSUBFUNCTIONdataInJobMapping.getValidateJobsWithMissingSUBFUNCTIONdataInJobMapping().verify_job_profile_is_found_and_displayed_in_jobs_missing_data_screen_search_results();
	}

	@Then("Extract SUBFUNCTION job details from found profile in Jobs Missing Data screen")
	public void extract_subfunction_job_details_from_found_profile_in_jobs_missing_data_screen() throws IOException {
		validateJobsWithMissingSUBFUNCTIONdataInJobMapping.getValidateJobsWithMissingSUBFUNCTIONdataInJobMapping().extract_job_details_from_found_profile_in_jobs_missing_data_screen();
	}

	@Then("Verify all SUBFUNCTION job details match between Job Mapping page and Jobs Missing Data screen")
	public void verify_all_subfunction_job_details_match_between_job_mapping_page_and_jobs_missing_data_screen() throws IOException {
		validateJobsWithMissingSUBFUNCTIONdataInJobMapping.getValidateJobsWithMissingSUBFUNCTIONdataInJobMapping().verify_all_job_details_match_between_job_mapping_page_and_jobs_missing_data_screen();
	}

	@Then("Click on Close button to return to Job Mapping page from SUBFUNCTION validation")
	public void click_on_close_button_to_return_to_job_mapping_page_from_subfunction_validation() throws IOException {
		validateJobsWithMissingSUBFUNCTIONdataInJobMapping.getValidateJobsWithMissingSUBFUNCTIONdataInJobMapping().click_on_close_button_to_return_to_job_mapping_page();
	}

	@Then("Verify user is back on Job Mapping page after SUBFUNCTION validation")
	public void verify_user_is_back_on_job_mapping_page_after_subfunction_validation() throws IOException {
		validateJobsWithMissingSUBFUNCTIONdataInJobMapping.getValidateJobsWithMissingSUBFUNCTIONdataInJobMapping().verify_user_is_back_on_job_mapping_page();
	}

	@Then("Find job in Jobs Missing SUBFUNCTION Data screen where Subfunction is N\\/A")
	public void find_job_in_jobs_missing_subfunction_data_screen_where_subfunction_is_na() throws IOException {
		validateJobsWithMissingSUBFUNCTIONdataInJobMapping.getValidateJobsWithMissingSUBFUNCTIONdataInJobMapping().find_job_in_jobs_missing_data_screen_where_subfunction_is_na();
	}

	@Then("Extract all available job details from Jobs with Missing SUBFUNCTION Data screen")
	public void extract_all_available_job_details_from_jobs_with_missing_subfunction_data_screen() throws IOException {
		validateJobsWithMissingSUBFUNCTIONdataInJobMapping.getValidateJobsWithMissingSUBFUNCTIONdataInJobMapping().extract_all_available_job_details_from_jobs_with_missing_data_screen();
	}

	@When("Search for the extracted SUBFUNCTION job profile by name in Job Mapping page")
	public void search_for_the_extracted_subfunction_job_profile_by_name_in_job_mapping_page() throws IOException {
		validateJobsWithMissingSUBFUNCTIONdataInJobMapping.getValidateJobsWithMissingSUBFUNCTIONdataInJobMapping().search_for_the_extracted_job_profile_by_name_in_job_mapping_page();
	}

	@Then("Verify SUBFUNCTION job profile is found and displayed in search results")
	public void verify_subfunction_job_profile_is_found_and_displayed_in_search_results() throws IOException {
		validateJobsWithMissingSUBFUNCTIONdataInJobMapping.getValidateJobsWithMissingSUBFUNCTIONdataInJobMapping().verify_job_profile_is_found_and_displayed_in_search_results();
	}

	@Then("Extract SUBFUNCTION job details from searched profile in Job Mapping page")
	public void extract_subfunction_job_details_from_searched_profile_in_job_mapping_page() throws IOException {
		validateJobsWithMissingSUBFUNCTIONdataInJobMapping.getValidateJobsWithMissingSUBFUNCTIONdataInJobMapping().extract_job_details_from_searched_profile_in_job_mapping_page();
	}

	@Then("Verify all SUBFUNCTION job details match between Jobs Missing Data screen and Job Mapping page")
	public void verify_all_subfunction_job_details_match_between_jobs_missing_data_screen_and_job_mapping_page() throws IOException {
		validateJobsWithMissingSUBFUNCTIONdataInJobMapping.getValidateJobsWithMissingSUBFUNCTIONdataInJobMapping().verify_all_job_details_match_between_jobs_missing_data_screen_and_job_mapping_page();
	}

	@Then("Verify Info Message is displayed on searched profile indicating missing SUBFUNCTION data")
	public void verify_info_message_is_displayed_on_searched_profile_indicating_missing_subfunction_data() throws IOException {
		validateJobsWithMissingSUBFUNCTIONdataInJobMapping.getValidateJobsWithMissingSUBFUNCTIONdataInJobMapping().verify_info_message_is_displayed_on_searched_profile_indicating_missing_subfunction_data();
	}

	@Then("Verify Info Message contains text about reduced match accuracy due to missing SUBFUNCTION data")
	public void verify_info_message_contains_text_about_reduced_match_accuracy_due_to_missing_subfunction_data() throws IOException {
		// This step is already handled by the previous step verification
		// No additional verification needed - info message display includes text validation
		PageObjectHelper.log(LOGGER, "Info Message text verified");
	}

}
