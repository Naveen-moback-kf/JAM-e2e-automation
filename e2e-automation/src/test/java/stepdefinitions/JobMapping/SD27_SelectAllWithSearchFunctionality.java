package stepdefinitions.JobMapping;

import java.io.IOException;

import com.kfonetalentsuite.manager.PageObjectManager;
import com.kfonetalentsuite.webdriverManager.DriverManager;

import io.cucumber.java.en.Then;

public class SD27_SelectAllWithSearchFunctionality extends DriverManager {
	public SD27_SelectAllWithSearchFunctionality() {
		super();
	}

	// ==================== SCROLL AND VIEW STEPS ====================

	@Then("User should scroll down to view last search result in {string} screen")
	public void user_should_scroll_down_to_view_last_search_result(String screen) throws IOException {
		PageObjectManager.getInstance().getSelectAllWithSearchFunctionality().user_should_scroll_down_to_view_last_search_result(screen);
	}

	@Then("User should validate all search results contains substring used for searching in {string} screen")
	public void user_should_validate_all_search_results_contains_substring(String screen) throws IOException {
		PageObjectManager.getInstance().getSelectAllWithSearchFunctionality().user_should_validate_all_search_results_contains_substring(screen);
	}

	// ==================== SELECTION STEPS ====================

	@Then("Click on Chevron button beside header checkbox in {string} screen")
	public void click_on_chevron_button_beside_header_checkbox(String screen) throws IOException {
		PageObjectManager.getInstance().getSelectAllWithSearchFunctionality().click_on_chevron_button_beside_header_checkbox(screen);
	}

	@Then("Click on Select All button in {string} screen")
	public void click_on_select_all_button(String screen) throws IOException {
		PageObjectManager.getInstance().getSelectAllWithSearchFunctionality().click_on_select_all_button(screen);
	}

	@Then("Select loaded job profiles using header checkbox in {string} screen")
	public void select_loaded_job_profiles_using_header_checkbox(String screen) throws IOException {
		PageObjectManager.getInstance().getSelectAllWithSearchFunctionality().click_on_header_checkbox_to_select_loaded_profiles(screen);
	}

	@Then("Verify action button is enabled after search selection in {string} screen")
	public void verify_action_button_is_enabled_after_search_selection(String screen) throws IOException {
		PageObjectManager.getInstance().getSelectAllWithSearchFunctionality().verify_action_button_is_enabled(screen);
	}

	@Then("Scroll to load more profiles in {string} screen")
	public void scroll_to_load_more_profiles(String screen) throws IOException {
		PageObjectManager.getInstance().getSelectAllWithSearchFunctionality().scroll_page_to_view_more_job_profiles(screen);
	}

	@Then("Verify newly loaded profiles after header checkbox are NOT selected in {string} screen")
	public void verify_newly_loaded_profiles_after_header_checkbox_are_not_selected(String screen) throws IOException {
		PageObjectManager.getInstance().getSelectAllWithSearchFunctionality().verify_profiles_loaded_after_header_checkbox_are_not_selected(screen);
	}

	@Then("Capture baseline of selected profiles in {string} screen")
	public void capture_baseline_of_selected_profiles(String screen) throws IOException {
		PageObjectManager.getInstance().getSelectAllWithSearchFunctionality().capture_baseline_of_selected_profiles(screen);
	}

	@Then("Clear Search bar in {string} screen")
	public void clear_search_bar(String screen) throws IOException {
		PageObjectManager.getInstance().getSelectAllWithSearchFunctionality().clear_search_bar(screen);
	}

	// ==================== VERIFICATION STEPS ====================

	@Then("Verify only searched profiles are selected after clearing search bar in {string} screen")
	public void verify_only_searched_profiles_are_selected_after_clearing_search_bar(String screen) throws IOException {
		PageObjectManager.getInstance().getSelectAllWithSearchFunctionality().verify_only_searched_profiles_are_selected_after_clearing_search_bar(screen);
	}

	// ==================== ALTERNATIVE VALIDATION STEPS ====================

	@Then("Enter different Job name substring in search bar for alternative validation in {string} screen")
	public void enter_different_job_name_substring_for_alternative_validation(String screen) throws IOException {
		PageObjectManager.getInstance().getSelectAllWithSearchFunctionality().enter_different_job_name_substring_for_alternative_validation(screen);
	}

	@Then("Validate second search results in {string} screen")
	public void validate_second_search_results(String screen) throws IOException, InterruptedException {
		PageObjectManager.getInstance().getSelectAllWithSearchFunctionality().scroll_down_to_load_all_second_search_results(screen);
	}

	@Then("Verify all loaded profiles in second search are NOT selected in {string} screen")
	public void verify_all_loaded_profiles_in_second_search_are_not_selected(String screen) throws IOException {
		PageObjectManager.getInstance().getSelectAllWithSearchFunctionality().verify_all_loaded_profiles_in_second_search_are_not_selected(screen);
	}
}


