package stepdefinitions.JobMapping;

import java.io.IOException;

import com.kfonetalentsuite.manager.PageObjectManager;
import com.kfonetalentsuite.webdriverManager.DriverManager;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SD31_ApplicationPerformance_JAM_and_HCM extends DriverManager {	
	public SD31_ApplicationPerformance_JAM_and_HCM() {
		super();		
	}
	
	@When("User measures the time taken to load Job Mapping page")
	public void user_measures_the_time_taken_to_load_job_mapping_page() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_measures_the_time_taken_to_load_job_mapping_page();
	}
	
	@Then("User validates page load time is within acceptable threshold")
	public void user_validates_page_load_time_is_within_acceptable_threshold() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_validates_page_load_time_is_within_acceptable_threshold();
	}
	
	@Then("User verifies all critical page components are loaded")
	public void user_verifies_all_critical_page_components_are_loaded() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_verifies_all_critical_page_components_are_loaded();
	}
	
	@Given("User is on Job Mapping page with loaded profiles")
	public void user_is_on_job_mapping_page_with_loaded_profiles() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_is_on_job_mapping_page_with_loaded_profiles();
	}
	
	@When("User measures time to perform search with dynamic keyword")
	public void user_measures_time_to_perform_search_with_dynamic_keyword() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_measures_time_to_perform_search_with_dynamic_keyword();
	}
	
	@Then("User validates search response time is within acceptable threshold")
	public void user_validates_search_response_time_is_within_acceptable_threshold() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_validates_search_response_time_is_within_acceptable_threshold();
	}
	
	@Then("User verifies search results are accurate")
	public void user_verifies_search_results_are_accurate() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_verifies_search_results_are_accurate();
	}
	
	@Then("User validates search suggestions appear instantly")
	public void user_validates_search_suggestions_appear_instantly() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_validates_search_suggestions_appear_instantly();
	}
	
	@Given("User has performed search operation with filtered results")
	public void user_has_performed_search_operation_with_filtered_results() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_has_performed_search_operation_with_filtered_results();
	}
	
	@When("User measures time to clear search")
	public void user_measures_time_to_clear_search() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_measures_time_to_clear_search();
	}
	
	@Then("User validates clear search time is within acceptable threshold")
	public void user_validates_clear_search_time_is_within_acceptable_threshold() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_validates_clear_search_time_is_within_acceptable_threshold();
	}
	
	@Then("User verifies all profiles are restored correctly")
	public void user_verifies_all_profiles_are_restored_correctly() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_verifies_all_profiles_are_restored_correctly();
	}
	
	@Then("User validates UI remains responsive during clear operation")
	public void user_validates_ui_remains_responsive_during_clear_operation() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_validates_ui_remains_responsive_during_clear_operation();
	}
	
	@When("User measures time to open Filters dropdown")
	public void user_measures_time_to_open_filters_dropdown() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_measures_time_to_open_filters_dropdown();
	}
	
	@When("User measures time to apply single filter dynamically")
	public void user_measures_time_to_apply_single_filter_dynamically() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_measures_time_to_apply_single_filter_dynamically();
	}
	
	@Then("User validates filter application time is within acceptable threshold")
	public void user_validates_filter_application_time_is_within_acceptable_threshold() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_validates_filter_application_time_is_within_acceptable_threshold();
	}
	
	@Then("User verifies filtered results are displayed correctly")
	public void user_verifies_filtered_results_are_displayed_correctly() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_verifies_filtered_results_are_displayed_correctly();
	}
	
	@Then("User validates UI remains responsive during filter operation")
	public void user_validates_ui_remains_responsive_during_filter_operation() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_validates_ui_remains_responsive_during_filter_operation();
	}
	
	@When("User measures time to apply multiple filters dynamically from available options")
	public void user_measures_time_to_apply_multiple_filters_dynamically_from_available_options() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_measures_time_to_apply_multiple_filters_dynamically_from_available_options();
	}
	
	@Then("User validates multiple filter application time is within acceptable threshold")
	public void user_validates_multiple_filter_application_time_is_within_acceptable_threshold() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_validates_multiple_filter_application_time_is_within_acceptable_threshold();
	}
	
	@Then("User verifies combined filtered results are displayed correctly")
	public void user_verifies_combined_filtered_results_are_displayed_correctly() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_verifies_combined_filtered_results_are_displayed_correctly();
	}
	
	@Then("User validates no UI lag during multi-filter operation")
	public void user_validates_no_ui_lag_during_multi_filter_operation() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_validates_no_ui_lag_during_multi_filter_operation();
	}
	
	@Given("User has applied filters and has filtered results")
	public void user_has_applied_filters_and_has_filtered_results() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_has_applied_filters_and_has_filtered_results();
	}
	
	@When("User measures time to click Clear All Filters button")
	public void user_measures_time_to_click_clear_all_filters_button() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_measures_time_to_click_clear_all_filters_button();
	}
	
	@Then("User validates clear filters operation time is within acceptable threshold")
	public void user_validates_clear_filters_operation_time_is_within_acceptable_threshold() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_validates_clear_filters_operation_time_is_within_acceptable_threshold();
	}
	
	@Then("User verifies all profiles are restored correctly after clearing filters")
	public void user_verifies_all_profiles_are_restored_correctly_after_clearing_filters() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_verifies_all_profiles_are_restored_correctly_after_clearing_filters();
	}
	
	@Then("User validates no UI freeze during filter clearing")
	public void user_validates_no_ui_freeze_during_filter_clearing() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_validates_no_ui_freeze_during_filter_clearing();
	}
	
	@When("User measures time to scroll through all profiles")
	public void user_measures_time_to_scroll_through_all_profiles() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_measures_time_to_scroll_through_all_profiles();
	}
	
	@When("User validates lazy loading triggers at appropriate intervals")
	public void user_validates_lazy_loading_triggers_at_appropriate_intervals() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_validates_lazy_loading_triggers_at_appropriate_intervals();
	}
	
	@Then("User verifies scroll performance is smooth without lag")
	public void user_verifies_scroll_performance_is_smooth_without_lag() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_verifies_scroll_performance_is_smooth_without_lag();
	}
	
	@Then("User validates newly loaded profiles render within acceptable time")
	public void user_validates_newly_loaded_profiles_render_within_acceptable_time() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_validates_newly_loaded_profiles_render_within_acceptable_time();
	}
	
	@Then("User verifies total time to load all profiles via scrolling")
	public void user_verifies_total_time_to_load_all_profiles_via_scrolling() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_verifies_total_time_to_load_all_profiles_via_scrolling();
	}
	
	// =========================================
	// SCENARIO 8: NAVIGATION PERFORMANCE
	// =========================================
	
	@When("User measures time to navigate to Job Comparison screen from Job Mapping")
	public void user_measures_time_to_navigate_to_job_comparison_screen_from_job_mapping() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_measures_time_to_navigate_to_job_comparison_screen_from_job_mapping();
	}
	
	@Then("User validates navigation time is within acceptable threshold")
	public void user_validates_navigation_time_is_within_acceptable_threshold() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_validates_navigation_time_is_within_acceptable_threshold();
	}
	
	@And("User verifies Job Comparison screen loads without delay")
	public void user_verifies_job_comparison_screen_loads_without_delay() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_verifies_job_comparison_screen_loads_without_delay();
	}
	
	@When("User measures time to navigate back to Job Mapping screen")
	public void user_measures_time_to_navigate_back_to_job_mapping_screen() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_measures_time_to_navigate_back_to_job_mapping_screen();
	}
	
	@Then("User validates back navigation time is within acceptable threshold")
	public void user_validates_back_navigation_time_is_within_acceptable_threshold() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_validates_back_navigation_time_is_within_acceptable_threshold();
	}
	
	@And("User verifies Job Mapping screen loads correctly after navigation")
	public void user_verifies_job_mapping_screen_loads_correctly_after_navigation() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_verifies_job_mapping_screen_loads_correctly_after_navigation();
	}
	
	// =========================================
	// SCENARIO 9: SORT PERFORMANCE
	// =========================================
	
	@Given("User is on Job Mapping page with maximum loaded profiles")
	public void user_is_on_job_mapping_page_with_maximum_loaded_profiles() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_is_on_job_mapping_page_with_maximum_loaded_profiles();
	}
	
	@When("User measures time to sort profiles by Job Title")
	public void user_measures_time_to_sort_profiles_by_job_title() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_measures_time_to_sort_profiles_by_job_title();
	}
	
	@And("User measures time to sort profiles by Grade")
	public void user_measures_time_to_sort_profiles_by_grade() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_measures_time_to_sort_profiles_by_grade();
	}
	
	@Then("User validates sorting operation time is within acceptable threshold")
	public void user_validates_sorting_operation_time_is_within_acceptable_threshold() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_validates_sorting_operation_time_is_within_acceptable_threshold();
	}
	
	@And("User verifies sorted results are accurate")
	public void user_verifies_sorted_results_are_accurate() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_verifies_sorted_results_are_accurate();
	}
	
	@And("User validates UI remains responsive during sorting")
	public void user_validates_ui_remains_responsive_during_sorting() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_validates_ui_remains_responsive_during_sorting();
	}
	
	// =========================================
	// SCENARIO 10: SELECT ALL PERFORMANCE
	// =========================================
	
	@When("User measures time to click chevron button beside header checkbox")
	public void user_measures_time_to_click_chevron_button_beside_header_checkbox() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_measures_time_to_click_chevron_button_beside_header_checkbox();
	}
	
	@And("User measures time to click Select All option")
	public void user_measures_time_to_click_select_all_option() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_measures_time_to_click_select_all_option();
	}
	
	@Then("User validates select all operation time is within acceptable threshold")
	public void user_validates_select_all_operation_time_is_within_acceptable_threshold() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_validates_select_all_operation_time_is_within_acceptable_threshold();
	}
	
	@And("User verifies all profiles are selected")
	public void user_verifies_all_profiles_are_selected() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_verifies_all_profiles_are_selected();
	}
	
	@And("User validates UI remains responsive during bulk selection")
	public void user_validates_ui_remains_responsive_during_bulk_selection() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_validates_ui_remains_responsive_during_bulk_selection();
	}
	
	// =========================================
	// SCENARIO 11: HCM PAGE LOAD PERFORMANCE
	// =========================================
	
	@When("User navigates to Profile Manager screen")
	public void user_navigates_to_profile_manager_screen() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_navigates_to_profile_manager_screen();
	}
	
	@And("User clicks on HCM Sync Profiles tab")
	public void user_clicks_on_hcm_sync_profiles_tab() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_clicks_on_hcm_sync_profiles_tab();
	}
	
	@And("User measures time to load HCM Sync Profiles page")
	public void user_measures_time_to_load_hcm_sync_profiles_page() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_measures_time_to_load_hcm_sync_profiles_page();
	}
	
	@Then("User validates HCM page load time is within acceptable threshold")
	public void user_validates_hcm_page_load_time_is_within_acceptable_threshold() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_validates_hcm_page_load_time_is_within_acceptable_threshold();
	}
	
	@And("User verifies all HCM profiles are loaded correctly")
	public void user_verifies_all_hcm_profiles_are_loaded_correctly() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_verifies_all_hcm_profiles_are_loaded_correctly();
	}
	
	// =========================================
	// SCENARIO 12: HCM SYNC PERFORMANCE
	// =========================================
	
	@Given("User is on HCM Sync Profiles page with selected profiles")
	public void user_is_on_hcm_sync_profiles_page_with_selected_profiles() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_is_on_hcm_sync_profiles_page_with_selected_profiles();
	}
	
	@When("User measures time to click Sync Selected Profiles button")
	public void user_measures_time_to_click_sync_selected_profiles_button() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_measures_time_to_click_sync_selected_profiles_button();
	}
	
	@And("User validates sync operation processing time")
	public void user_validates_sync_operation_processing_time() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_validates_sync_operation_processing_time();
	}
	
	@Then("User verifies sync operation completes within acceptable threshold")
	public void user_verifies_sync_operation_completes_within_acceptable_threshold() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_verifies_sync_operation_completes_within_acceptable_threshold();
	}
	
	@And("User validates sync status updates appear promptly")
	public void user_validates_sync_status_updates_appear_promptly() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getApplicationPerformance_JAM_and_HCM().user_validates_sync_status_updates_appear_promptly();
	}
}


