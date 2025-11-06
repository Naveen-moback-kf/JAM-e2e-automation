package stepdefinitions.functional.JobMapping;

import java.io.IOException;

import com.JobMapping.manager.PageObjectManager;

import io.cucumber.java.en.Then;

public class SD36_ValidateSelectAllWithFiltersFunctionality_PM {
	PageObjectManager validateSelectAllWithFiltersFunctionality_PM = new PageObjectManager();

	public SD36_ValidateSelectAllWithFiltersFunctionality_PM() {
		super();
	}

	@Then("Apply filter and verify profiles count in HCM Sync Profiles screen for feature36")
	public void apply_filter_and_verify_profiles_count_in_hcm_sync_profiles_screen_for_feature36() throws IOException {
		validateSelectAllWithFiltersFunctionality_PM.getValidateSelectAllWithFiltersFunctionality_PM().apply_filter_and_verify_profiles_count_in_hcm_sync_profiles_screen_for_feature36();
	}

	@Then("User should scroll down to view last filtered result in HCM Sync Profiles screen for feature36")
	public void user_should_scroll_down_to_view_last_filtered_result_in_hcm_sync_profiles_screen_for_feature36() throws IOException {
		validateSelectAllWithFiltersFunctionality_PM.getValidateSelectAllWithFiltersFunctionality_PM().user_should_scroll_down_to_view_last_filtered_result_in_hcm_sync_profiles_screen_for_feature36();
	}

	@Then("User should validate all filtered results match the applied filter in HCM Sync Profiles screen for feature36")
	public void user_should_validate_all_filtered_results_match_the_applied_filter_in_hcm_sync_profiles_screen_for_feature36() throws IOException {
		validateSelectAllWithFiltersFunctionality_PM.getValidateSelectAllWithFiltersFunctionality_PM().user_should_validate_all_filtered_results_match_the_applied_filter_in_hcm_sync_profiles_screen_for_feature36();
	}

	@Then("User is in HCM Sync Profiles screen with Selected Filter Results for feature36")
	public void user_is_in_hcm_sync_profiles_screen_with_selected_filter_results_for_feature36() throws IOException {
		validateSelectAllWithFiltersFunctionality_PM.getValidateSelectAllWithFiltersFunctionality_PM().user_is_in_hcm_sync_profiles_screen_with_selected_filter_results_for_feature36();
	}

	@Then("Clear applied filter in HCM Sync Profiles screen for feature36")
	public void clear_applied_filter_in_hcm_sync_profiles_screen_for_feature36() throws IOException {
		validateSelectAllWithFiltersFunctionality_PM.getValidateSelectAllWithFiltersFunctionality_PM().clear_applied_filter_in_hcm_sync_profiles_screen_for_feature36();
	}

	// ========================================================================================
	// Alternative Validation Strategy - Using Different Filter to verify first filter selection
	// ========================================================================================

	@Then("Apply different filter for alternative validation in HCM Sync Profiles screen for feature36")
	public void apply_different_filter_for_alternative_validation_in_hcm_sync_profiles_screen_for_feature36() throws IOException {
		validateSelectAllWithFiltersFunctionality_PM.getValidateSelectAllWithFiltersFunctionality_PM().apply_different_filter_for_alternative_validation_in_hcm_sync_profiles_screen_for_feature36();
	}

	@Then("Scroll down to load all second filter results in HCM Sync Profiles screen for feature36")
	public void scroll_down_to_load_all_second_filter_results_in_hcm_sync_profiles_screen_for_feature36() throws IOException, InterruptedException {
		validateSelectAllWithFiltersFunctionality_PM.getValidateSelectAllWithFiltersFunctionality_PM().scroll_down_to_load_all_second_filter_results_in_hcm_sync_profiles_screen_for_feature36();
	}

	@Then("Verify all loaded profiles in second filter are NOT selected in HCM Sync Profiles screen for feature36")
	public void verify_all_loaded_profiles_in_second_filter_are_not_selected_in_hcm_sync_profiles_screen_for_feature36() throws IOException {
		validateSelectAllWithFiltersFunctionality_PM.getValidateSelectAllWithFiltersFunctionality_PM().verify_all_loaded_profiles_in_second_filter_are_not_selected_in_hcm_sync_profiles_screen_for_feature36();
	}

	@Then("Verify only Filtered Profiles are selected after clearing all filters in HCM Sync Profiles screen")
	public void verify_only_filtered_profiles_are_selected_after_clearing_all_filters_in_hcm_sync_profiles_screen() throws IOException {
		validateSelectAllWithFiltersFunctionality_PM.getValidateSelectAllWithFiltersFunctionality_PM().verify_only_filtered_profiles_are_selected_after_clearing_all_filters_in_hcm_sync_profiles_screen();
	}
}

