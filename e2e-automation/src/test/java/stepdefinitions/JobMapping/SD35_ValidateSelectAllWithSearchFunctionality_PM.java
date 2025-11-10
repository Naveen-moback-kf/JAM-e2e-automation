package stepdefinitions.JobMapping;

import java.io.IOException;

import com.kfonetalentsuite.manager.PageObjectManager;

import io.cucumber.java.en.Then;

public class SD35_ValidateSelectAllWithSearchFunctionality_PM {
	PageObjectManager validateSelectAllWithSearchFunctionality_PM = new PageObjectManager();

	public SD35_ValidateSelectAllWithSearchFunctionality_PM() {
		super();
	}
	
	@Then("User should scroll down to view last search result in HCM Sync Profiles screen")
	public void user_should_scroll_down_to_view_last_search_result_in_hcm_sync_profiles_screen() throws IOException {
		validateSelectAllWithSearchFunctionality_PM.getValidateSelectAllWithSearchFunctionality_PM().user_should_scroll_down_to_view_last_search_result_in_hcm_sync_profiles_screen();
	}
	
	@Then("User should validate all search results contains substring used for searching in HCM Sync Profiles screen")
	public void user_should_validate_all_search_results_contains_substring_used_for_searching_in_hcm_sync_profiles_screen() throws IOException {
		validateSelectAllWithSearchFunctionality_PM.getValidateSelectAllWithSearchFunctionality_PM().user_should_validate_all_search_results_contains_substring_used_for_searching_in_hcm_sync_profiles_screen();
	}
	
	@Then("User is in HCM Sync Profiles screen with Selected Search Results")
	public void user_is_in_hcm_sync_profiles_screen_with_selected_search_results() throws IOException {
		validateSelectAllWithSearchFunctionality_PM.getValidateSelectAllWithSearchFunctionality_PM().user_is_in_hcm_sync_profiles_screen_with_selected_search_results();
	}

	@Then("Verify only Searched Profiles are selected after clearing search bar in HCM Sync Profiles screen")
	public void verify_only_searched_profiles_are_selected_after_clearing_search_bar_in_hcm_sync_profiles_screen() throws IOException {
		validateSelectAllWithSearchFunctionality_PM.getValidateSelectAllWithSearchFunctionality_PM().verify_only_searched_profiles_are_selected_after_clearing_search_bar_in_hcm_sync_profiles_screen();
	}
	
	// ========================================================================================
	// Alternative Validation Strategy - Using Second Search to verify first search selection
	// ========================================================================================
	
	@Then("Enter different Job name substring in search bar for alternative validation in HCM Sync Profiles screen")
	public void enter_different_job_name_substring_in_search_bar_for_alternative_validation_in_hcm_sync_profiles_screen() throws IOException {
		validateSelectAllWithSearchFunctionality_PM.getValidateSelectAllWithSearchFunctionality_PM().enter_different_job_name_substring_in_search_bar_for_alternative_validation_in_hcm_sync_profiles_screen();
	}
	
	@Then("Scroll down to load all second search results in HCM Sync Profiles screen")
	public void scroll_down_to_load_all_second_search_results_in_hcm_sync_profiles_screen() throws IOException, InterruptedException {
		validateSelectAllWithSearchFunctionality_PM.getValidateSelectAllWithSearchFunctionality_PM().scroll_down_to_load_all_second_search_results_in_hcm_sync_profiles_screen();
	}
	
	@Then("Verify all loaded profiles in second search are NOT selected in HCM Sync Profiles screen")
	public void verify_all_loaded_profiles_in_second_search_are_not_selected_in_hcm_sync_profiles_screen() throws IOException {
		validateSelectAllWithSearchFunctionality_PM.getValidateSelectAllWithSearchFunctionality_PM().verify_all_loaded_profiles_in_second_search_are_not_selected_in_hcm_sync_profiles_screen();
	}
}

