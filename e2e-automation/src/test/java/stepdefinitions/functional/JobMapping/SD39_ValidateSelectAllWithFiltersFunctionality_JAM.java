package stepdefinitions.functional.JobMapping;

import java.io.IOException;

import com.JobMapping.manager.PageObjectManager;

import io.cucumber.java.en.Then;

public class SD39_ValidateSelectAllWithFiltersFunctionality_JAM {
	PageObjectManager validateSelectAllWithFiltersFunctionality_JAM = new PageObjectManager();

	public SD39_ValidateSelectAllWithFiltersFunctionality_JAM() {
		super();
	}

	@Then("Apply filter and verify profiles count in Job Mapping screen for feature39")
	public void apply_filter_and_verify_profiles_count_in_job_mapping_screen_for_feature39() throws IOException {
		validateSelectAllWithFiltersFunctionality_JAM.getValidateSelectAllWithFiltersFunctionality_JAM().apply_filter_and_verify_profiles_count_in_job_mapping_screen_for_feature39();
	}

	@Then("User should scroll down to view last filtered result in Job Mapping screen for feature39")
	public void user_should_scroll_down_to_view_last_filtered_result_in_job_mapping_screen_for_feature39() throws IOException {
		validateSelectAllWithFiltersFunctionality_JAM.getValidateSelectAllWithFiltersFunctionality_JAM().user_should_scroll_down_to_view_last_filtered_result_in_job_mapping_screen_for_feature39();
	}

	@Then("User should validate all filtered results match the applied filter in Job Mapping screen for feature39")
	public void user_should_validate_all_filtered_results_match_the_applied_filter_in_job_mapping_screen_for_feature39() throws IOException {
		validateSelectAllWithFiltersFunctionality_JAM.getValidateSelectAllWithFiltersFunctionality_JAM().user_should_validate_all_filtered_results_match_the_applied_filter_in_job_mapping_screen_for_feature39();
	}

	@Then("User is in Job Mapping page with Selected Filter Results for feature39")
	public void user_is_in_job_mapping_page_with_selected_filter_results_for_feature39() throws IOException {
		validateSelectAllWithFiltersFunctionality_JAM.getValidateSelectAllWithFiltersFunctionality_JAM().user_is_in_job_mapping_page_with_selected_filter_results_for_feature39();
	}

	@Then("Clear applied filter in Job Mapping screen for feature39")
	public void clear_applied_filter_in_job_mapping_screen_for_feature39() throws IOException {
		validateSelectAllWithFiltersFunctionality_JAM.getValidateSelectAllWithFiltersFunctionality_JAM().clear_applied_filter_in_job_mapping_screen_for_feature39();
	}

	// ========================================================================================
	// Alternative Validation Strategy - Using Different Filter to verify first filter selection
	// ========================================================================================

	@Then("Apply different filter for alternative validation in Job Mapping screen for feature39")
	public void apply_different_filter_for_alternative_validation_in_job_mapping_screen_for_feature39() throws IOException {
		validateSelectAllWithFiltersFunctionality_JAM.getValidateSelectAllWithFiltersFunctionality_JAM().apply_different_filter_for_alternative_validation_in_job_mapping_screen_for_feature39();
	}

	@Then("Scroll down to load all second filter results in Job Mapping screen for feature39")
	public void scroll_down_to_load_all_second_filter_results_in_job_mapping_screen_for_feature39() throws IOException, InterruptedException {
		validateSelectAllWithFiltersFunctionality_JAM.getValidateSelectAllWithFiltersFunctionality_JAM().scroll_down_to_load_all_second_filter_results_in_job_mapping_screen_for_feature39();
	}

	@Then("Verify all loaded profiles in second filter are NOT selected in Job Mapping screen for feature39")
	public void verify_all_loaded_profiles_in_second_filter_are_not_selected_in_job_mapping_screen_for_feature39() throws IOException {
		validateSelectAllWithFiltersFunctionality_JAM.getValidateSelectAllWithFiltersFunctionality_JAM().verify_all_loaded_profiles_in_second_filter_are_not_selected_in_job_mapping_screen_for_feature39();
	}

	@Then("Verify only Filtered Profiles are selected after clearing all filters in Job Mapping screen")
	public void verify_only_filtered_profiles_are_selected_after_clearing_all_filters_in_job_mapping_screen() throws IOException {
		validateSelectAllWithFiltersFunctionality_JAM.getValidateSelectAllWithFiltersFunctionality_JAM().verify_only_filtered_profiles_are_selected_after_clearing_all_filters_in_job_mapping_screen();
	}
}

