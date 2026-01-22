package stepdefinitions.JobMapping;

import java.io.IOException;

import com.kfonetalentsuite.manager.PageObjectManager;
import com.kfonetalentsuite.webdriverManager.DriverManager;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SD25_MissingDataFunctionality extends DriverManager {	
	public SD25_MissingDataFunctionality() {
		super();
	}

	// Prerequisite Checks
	@Given("Skip scenario if profile with missing data was not found")
	public void skip_scenario_if_profile_not_found_in_forward_flow() throws IOException {
		PageObjectManager.getInstance().getMissingDataFunctionality().skip_scenario_if_profile_not_found_in_forward_flow();
	}

	@Then("Sort Job Profiles by {string} in Ascending order")
	public void sort_job_profiles_by_column_in_ascending_order(String sortColumn) throws IOException {
		PageObjectManager.getInstance().getMissingDataFunctionality().sort_job_profiles_by_column_in_ascending_order(sortColumn);
	}

	@Then("Find job profile in Job Mapping page where {string} is missing")
	public void find_job_profile_in_job_mapping_page_where_data_is_missing(String dataType) throws IOException {
		PageObjectManager.getInstance().getMissingDataFunctionality().find_job_profile_in_job_mapping_page_where_data_is_missing(dataType);
	}

	@Then("Extract {string} job details from found profile in Job Mapping page")
	public void extract_job_details_from_found_profile_in_job_mapping_page(String dataType) throws IOException {
		PageObjectManager.getInstance().getMissingDataFunctionality().extract_job_details_from_found_profile_in_job_mapping_page(dataType);
	}

	@Then("Verify user is navigated to Jobs with Missing {string} Data screen")
	public void verify_user_is_navigated_to_jobs_with_missing_data_screen(String dataType) throws IOException {
		PageObjectManager.getInstance().getMissingDataFunctionality().verify_user_is_navigated_to_jobs_with_missing_data_screen(dataType);
	}

	@Then("Search for the extracted {string} job profile by name in Jobs Missing Data screen")
	public void search_for_extracted_job_profile_in_jobs_missing_data_screen(String dataType) throws IOException {
		PageObjectManager.getInstance().getMissingDataFunctionality().search_for_extracted_job_profile_in_jobs_missing_data_screen(dataType);
	}

	@Then("Verify {string} job profile is found and displayed in Jobs Missing Data screen search results")
	public void verify_job_profile_found_in_missing_data_screen_search_results(String dataType) throws IOException {
		PageObjectManager.getInstance().getMissingDataFunctionality().verify_job_profile_found_in_jobs_missing_data_screen_search_results(dataType);
	}

	@Then("Extract {string} job details from found profile in Jobs Missing Data screen")
	public void extract_job_details_from_found_profile_in_jobs_missing_data_screen(String dataType) throws IOException {
		PageObjectManager.getInstance().getMissingDataFunctionality().extract_job_details_from_found_profile_in_jobs_missing_data_screen(dataType);
	}
	
	@Then("Verify all {string} job details match between Job Mapping page and Jobs Missing Data screen")
	public void verify_job_details_match_between_job_mapping_and_missing_data_screen(String dataType) throws IOException {
		PageObjectManager.getInstance().getMissingDataFunctionality().verify_all_job_details_match_forward_flow(dataType);
	}

	@Then("Click on Close button to return to Job Mapping page from {string} validation")
	public void click_close_button_to_return_to_job_mapping_page(String dataType) throws IOException {
		PageObjectManager.getInstance().getMissingDataFunctionality().click_on_close_button_to_return_to_job_mapping_page(dataType);
	}

	@Then("Verify user is back on Job Mapping page after {string} validation")
	public void verify_user_is_back_on_job_mapping_page(String dataType) throws IOException {
		PageObjectManager.getInstance().getMissingDataFunctionality().verify_user_is_back_on_job_mapping_page(dataType);
	}

	@Then("Find job in Jobs Missing {string} Data screen where {string} is N\\/A")
	public void find_job_in_jobs_missing_data_screen_where_data_is_na(String screenType, String dataType) throws IOException {
		PageObjectManager.getInstance().getMissingDataFunctionality().find_job_in_jobs_missing_data_screen_where_data_is_na(dataType);
	}

	@Then("Extract all available job details from Jobs with Missing {string} Data screen")
	public void extract_all_job_details_from_jobs_with_missing_data_screen(String dataType) throws IOException {
		PageObjectManager.getInstance().getMissingDataFunctionality().extract_all_available_job_details_from_jobs_with_missing_data_screen(dataType);
	}

	@When("Search for the extracted {string} job profile by name in Job Mapping page")
	public void search_for_extracted_job_profile_in_job_mapping_page(String dataType) throws IOException {
		PageObjectManager.getInstance().getMissingDataFunctionality().search_for_extracted_job_profile_in_job_mapping_page(dataType);
	}

	@Then("Verify {string} job profile is found and displayed in search results")
	public void verify_job_profile_is_found_in_search_results(String dataType) throws IOException {
		PageObjectManager.getInstance().getMissingDataFunctionality().verify_job_profile_found_in_job_mapping_page_search_results(dataType);
	}

	@Then("Extract {string} job details from searched profile in Job Mapping page")
	public void extract_job_details_from_searched_profile_in_job_mapping_page(String dataType) throws IOException {
		PageObjectManager.getInstance().getMissingDataFunctionality().extract_job_details_from_searched_profile_in_job_mapping_page(dataType);
	}

	@Then("Verify all {string} job details match between Jobs Missing Data screen and Job Mapping page")
	public void verify_job_details_match_between_missing_data_and_job_mapping(String dataType) throws IOException {
		PageObjectManager.getInstance().getMissingDataFunctionality().verify_all_job_details_match_reverse_flow(dataType);
	}

	@Then("Verify Info Message is displayed on searched profile indicating missing {string} data")
	public void verify_info_message_is_displayed_indicating_missing_data(String dataType) throws IOException {
		PageObjectManager.getInstance().getMissingDataFunctionality().verify_info_message_is_displayed_indicating_missing_data(dataType);
	}

	@Then("Verify Info Message contains text about reduced match accuracy due to missing {string} data")
	public void verify_info_message_contains_reduced_accuracy_text(String dataType) throws IOException {
		PageObjectManager.getInstance().getMissingDataFunctionality().verify_info_message_contains_reduced_accuracy_text(dataType);
	}
}


