package stepdefinitions.JobMapping;

import java.io.IOException;

import com.kfonetalentsuite.manager.PageObjectManager;
import com.kfonetalentsuite.webdriverManager.DriverManager;

import io.cucumber.java.en.Then;

/**
 * Consolidated Step Definitions for Select All / Loaded Profiles Selection with Filters functionality.
 * Supports both PM (HCM Sync Profiles) and JAM (Job Mapping) screens.
 * 
 * NOTE: This SD only contains FILTER-SPECIFIC steps.
 * Common steps (chevron, select all, header checkbox, scroll, verify, capture baseline)
 * are reused from SD35_SelectAllWithSearchFunctionality and SD42_ClearProfileSelectionFunctionality.
 */
public class SD36_SelectAllWithFiltersFunctionality extends DriverManager {

	private PageObjectManager pageObjectManager = new PageObjectManager();

	public SD36_SelectAllWithFiltersFunctionality() {
		super();
	}

	// ==================== FILTER APPLICATION STEPS ====================

	@Then("Apply filter and verify profiles count in {string} screen")
	public void apply_filter_and_verify_profiles_count(String screen) throws IOException {
		pageObjectManager.getSelectAllWithFiltersFunctionality().apply_filter_and_verify_profiles_count(screen);
	}

	@Then("Close the Filters dropdown in {string} screen")
	public void close_filters_dropdown(String screen) throws IOException {
		pageObjectManager.getSelectAllWithFiltersFunctionality().close_filters_dropdown(screen);
	}

	// ==================== SCROLL AND VALIDATION STEPS ====================

	@Then("User should scroll down to view last filtered result in {string} screen")
	public void user_should_scroll_down_to_view_last_filtered_result(String screen) throws IOException {
		pageObjectManager.getSelectAllWithFiltersFunctionality().user_should_scroll_down_to_view_last_filtered_result(screen);
	}

	@Then("User should validate all filtered results match the applied filter in {string} screen")
	public void user_should_validate_all_filtered_results_match_the_applied_filter(String screen) throws IOException {
		pageObjectManager.getSelectAllWithFiltersFunctionality().user_should_validate_all_filtered_results_match_the_applied_filter(screen);
	}

	// ==================== CLEAR FILTER STEPS ====================

	@Then("Click on Clear All Filters button in {string} screen")
	public void click_on_clear_all_filters_button(String screen) throws IOException {
		pageObjectManager.getSelectAllWithFiltersFunctionality().click_on_clear_all_filters_button(screen);
	}

	// ==================== VERIFICATION STEPS ====================

	@Then("Verify only Filtered Profiles are selected after clearing all filters in {string} screen")
	public void verify_only_filtered_profiles_are_selected_after_clearing_all_filters(String screen) throws IOException {
		pageObjectManager.getSelectAllWithFiltersFunctionality().verify_only_filtered_profiles_are_selected_after_clearing_all_filters(screen);
	}

	// ==================== ALTERNATIVE VALIDATION STEPS ====================

	@Then("Apply different filter for alternative validation in {string} screen")
	public void apply_different_filter_for_alternative_validation(String screen) throws IOException {
		pageObjectManager.getSelectAllWithFiltersFunctionality().apply_different_filter_for_alternative_validation(screen);
	}

	@Then("Scroll down to load all second filter results in {string} screen")
	public void scroll_down_to_load_all_second_filter_results(String screen) throws IOException {
		pageObjectManager.getSelectAllWithFiltersFunctionality().scroll_down_to_load_all_second_filter_results(screen);
	}

	@Then("Verify all loaded profiles in second filter are NOT selected in {string} screen")
	public void verify_all_loaded_profiles_in_second_filter_are_not_selected(String screen) throws IOException {
		pageObjectManager.getSelectAllWithFiltersFunctionality().verify_all_loaded_profiles_in_second_filter_are_not_selected(screen);
	}
}
