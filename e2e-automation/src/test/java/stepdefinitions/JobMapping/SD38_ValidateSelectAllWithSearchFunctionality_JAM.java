package stepdefinitions.JobMapping;

import java.io.IOException;

import com.kfonetalentsuite.manager.PageObjectManager;

import io.cucumber.java.en.Then;

public class SD38_ValidateSelectAllWithSearchFunctionality_JAM {
	PageObjectManager validateSelectAllWithSearchFunctionality_JAM = new PageObjectManager();

	public SD38_ValidateSelectAllWithSearchFunctionality_JAM() {
		super();
	}
	
	@Then("User is in Job Mapping page with Selected Search Results")
	public void user_is_in_job_mapping_page_with_selected_search_results() throws IOException {
		validateSelectAllWithSearchFunctionality_JAM.getValidateSelectAllWithSearchFunctionality_JAM().user_is_in_job_mapping_page_with_selected_search_results();
	}

	@Then("Verify only Searched Profiles are selected after clearing search bar in JAM screen")
	public void verify_only_searched_profiles_are_selected_after_clearing_search_bar_in_JAM_screen() throws IOException {
		validateSelectAllWithSearchFunctionality_JAM.getValidateSelectAllWithSearchFunctionality_JAM().verify_only_searched_profiles_are_selected_after_clearing_search_bar_in_JAM_screen();
	}
	
	// ========================================================================================
	// Alternative Validation Strategy - Using Second Search to verify first search selection
	// ========================================================================================
	
	@Then("Enter different Job name substring in search bar for alternative validation")
	public void enter_different_job_name_substring_in_search_bar_for_alternative_validation() throws IOException {
		validateSelectAllWithSearchFunctionality_JAM.getValidateSelectAllWithSearchFunctionality_JAM().enter_different_job_name_substring_in_search_bar_for_alternative_validation();
	}
	
	@Then("Scroll down to load all second search results")
	public void scroll_down_to_load_all_second_search_results() throws IOException, InterruptedException {
		validateSelectAllWithSearchFunctionality_JAM.getValidateSelectAllWithSearchFunctionality_JAM().scroll_down_to_load_all_second_search_results();
	}
	
	@Then("Verify all loaded profiles in second search are NOT selected")
	public void verify_all_loaded_profiles_in_second_search_are_not_selected() throws IOException {
		validateSelectAllWithSearchFunctionality_JAM.getValidateSelectAllWithSearchFunctionality_JAM().verify_all_loaded_profiles_in_second_search_are_not_selected();
	}
}

