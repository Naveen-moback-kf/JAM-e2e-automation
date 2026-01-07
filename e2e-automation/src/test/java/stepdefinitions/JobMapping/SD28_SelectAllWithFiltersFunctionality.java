package stepdefinitions.JobMapping;

import java.io.IOException;

import com.kfonetalentsuite.manager.PageObjectManager;
import com.kfonetalentsuite.pageobjects.JobMapping.PO08_JobMappingFilters;
import com.kfonetalentsuite.pageobjects.JobMapping.PO18_HCMSyncProfilesTab_PM;
import com.kfonetalentsuite.pageobjects.JobMapping.PO28_SelectAllWithFiltersFunctionality;
import com.kfonetalentsuite.webdriverManager.DriverManager;

import io.cucumber.java.en.Then;

public class SD28_SelectAllWithFiltersFunctionality extends DriverManager {
	public SD28_SelectAllWithFiltersFunctionality() {
		super();
	}

	// ==================== FILTER VALUE STORAGE STEPS (Bridge PO08/PO18 -> PO28) ====================

	@Then("Store applied filter value for validation in {string} screen")
	public void store_applied_filter_value_for_validation(String screen) {
		// Copy filter value from PO08 to PO28 for later validation (JAM screen)
		PO28_SelectAllWithFiltersFunctionality.firstFilterType.set("Grades");
		PO28_SelectAllWithFiltersFunctionality.firstFilterValue.set(PO08_JobMappingFilters.GradesOption.get());
	}

	@Then("Store second filter value for validation in {string} screen")
	public void store_second_filter_value_for_validation(String screen) {
		// Copy second filter value from PO08 to PO28 for alternative validation (JAM screen)
		PO28_SelectAllWithFiltersFunctionality.secondFilterType.set("Grades");
		PO28_SelectAllWithFiltersFunctionality.secondFilterValue.set(PO08_JobMappingFilters.GradesOption2.get());
	}

	@Then("Store applied PM filter value for validation")
	public void store_applied_pm_filter_value_for_validation() {
		// Copy filter value from PO18 to PO28 for later validation (PM screen)
		PO28_SelectAllWithFiltersFunctionality.firstFilterType.set(PO18_HCMSyncProfilesTab_PM.appliedFilterType.get());
		PO28_SelectAllWithFiltersFunctionality.firstFilterValue.set(PO18_HCMSyncProfilesTab_PM.appliedFilterValue.get());
	}

	@Then("Store second PM filter value for validation")
	public void store_second_pm_filter_value_for_validation() {
		// Copy filter value from PO18 to PO28 for alternative validation (PM screen)
		PO28_SelectAllWithFiltersFunctionality.secondFilterType.set(PO18_HCMSyncProfilesTab_PM.appliedFilterType.get());
		PO28_SelectAllWithFiltersFunctionality.secondFilterValue.set(PO18_HCMSyncProfilesTab_PM.appliedFilterValue.get());
	}

	// ==================== FILTER APPLICATION STEPS ====================

	@Then("Apply filter and verify profiles count in {string} screen")
	public void apply_filter_and_verify_profiles_count(String screen) throws IOException {
		PageObjectManager.getInstance().getSelectAllWithFiltersFunctionality().apply_filter_and_verify_profiles_count(screen);
	}

	@Then("Close the Filters dropdown in {string} screen")
	public void close_filters_dropdown(String screen) throws IOException {
		PageObjectManager.getInstance().getSelectAllWithFiltersFunctionality().close_filters_dropdown(screen);
	}

	// ==================== SCROLL AND VALIDATION STEPS ====================

	@Then("User should scroll down to view last filtered result in {string} screen")
	public void user_should_scroll_down_to_view_last_filtered_result(String screen) throws IOException {
		PageObjectManager.getInstance().getSelectAllWithFiltersFunctionality().user_should_scroll_down_to_view_last_filtered_result(screen);
	}

	@Then("User should validate all filtered results match the applied filter in {string} screen")
	public void user_should_validate_all_filtered_results_match_the_applied_filter(String screen) throws IOException {
		PageObjectManager.getInstance().getSelectAllWithFiltersFunctionality().user_should_validate_all_filtered_results_match_the_applied_filter(screen);
	}

	// ==================== CLEAR FILTER STEPS ====================

	@Then("Click on Clear All Filters button in {string} screen")
	public void click_on_clear_all_filters_button(String screen) throws IOException {
		PageObjectManager.getInstance().getSelectAllWithFiltersFunctionality().click_on_clear_all_filters_button(screen);
	}

	// ==================== VERIFICATION STEPS ====================

	@Then("Verify only Filtered Profiles are selected after clearing all filters in {string} screen")
	public void verify_only_filtered_profiles_are_selected_after_clearing_all_filters(String screen) throws IOException {
		PageObjectManager.getInstance().getSelectAllWithFiltersFunctionality().verify_only_filtered_profiles_are_selected_after_clearing_all_filters(screen);
	}

	// ==================== ALTERNATIVE VALIDATION STEPS ====================

	@Then("Apply different filter for alternative validation in {string} screen")
	public void apply_different_filter_for_alternative_validation(String screen) throws IOException {
		PageObjectManager.getInstance().getSelectAllWithFiltersFunctionality().apply_different_filter_for_alternative_validation(screen);
	}

	@Then("Scroll down to load all second filter results in {string} screen")
	public void scroll_down_to_load_all_second_filter_results(String screen) throws IOException {
		PageObjectManager.getInstance().getSelectAllWithFiltersFunctionality().scroll_down_to_load_all_second_filter_results(screen);
	}

	@Then("Verify all loaded profiles in second filter are NOT selected in {string} screen")
	public void verify_all_loaded_profiles_in_second_filter_are_not_selected(String screen) throws IOException {
		PageObjectManager.getInstance().getSelectAllWithFiltersFunctionality().verify_all_loaded_profiles_in_second_filter_are_not_selected(screen);
	}
}

