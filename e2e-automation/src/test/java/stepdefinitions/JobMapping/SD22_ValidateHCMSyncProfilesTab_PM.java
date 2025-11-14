package stepdefinitions.JobMapping;

import java.io.IOException;

import com.kfonetalentsuite.manager.PageObjectManager;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SD22_ValidateHCMSyncProfilesTab_PM {
	PageObjectManager validateHCMSyncProfilesTab_PM = new PageObjectManager();
	
	public SD22_ValidateHCMSyncProfilesTab_PM() {
		super();		
	}
	
	@When("User is on Architect Dashboard page")
	public void user_is_on_architect_dashboard_page() throws IOException {
		validateHCMSyncProfilesTab_PM.getValidateHCMSyncProfilesTab_PM().user_is_on_architect_dashboard_page();
	}

	@When("User is on Profile Manager page")
	public void user_is_on_profile_manager_page() throws IOException {
		validateHCMSyncProfilesTab_PM.getValidateHCMSyncProfilesTab_PM().user_is_on_profile_manager_page();
	}

	@Then("Click on Menu button")
	public void click_on_menu_button() throws IOException {
		validateHCMSyncProfilesTab_PM.getValidateHCMSyncProfilesTab_PM().click_on_menu_button();
	}

	@Then("Click on Profile Manager button")
	public void click_on_profile_manager_button() throws IOException {
		validateHCMSyncProfilesTab_PM.getValidateHCMSyncProfilesTab_PM().click_on_profile_manager_button();
	}

	@Then("User should be landed to PM dashboard")
	public void user_should_be_landed_to_pm_dashboard() throws IOException  {
		validateHCMSyncProfilesTab_PM.getValidateHCMSyncProfilesTab_PM().user_should_be_landed_to_pm_dashboard();
	}
	
	@Then("Click on HCM Sync Profiles header button")
	public void click_on_hcm_sync_profiles_header_button() throws Exception {
		validateHCMSyncProfilesTab_PM.getValidateHCMSyncProfilesTab_PM().click_on_hcm_sync_profiles_header_button();
	}

	@Then("User should be navigated to HCM Sync Profiles screen")
	public void user_should_be_navigated_to_hcm_sync_profiles_screen() throws Exception {
		validateHCMSyncProfilesTab_PM.getValidateHCMSyncProfilesTab_PM().user_should_be_navigated_to_hcm_sync_profiles_screen();
	}

	@Then("Verify title is correctly displaying in HCM Sync Profiles screen")
	public void verify_title_is_correctly_displaying_in_hcm_sync_profiles_tab() throws Exception {
		validateHCMSyncProfilesTab_PM.getValidateHCMSyncProfilesTab_PM().verify_title_is_correctly_displaying_in_hcm_sync_profiles_tab();
	}

	@Then("Verify description below the title is correctly displaying in HCM Sync Profiles screen")
	public void verify_description_below_the_title_is_correctly_displaying_in_hcm_sync_profiles_tab() throws Exception {
		validateHCMSyncProfilesTab_PM.getValidateHCMSyncProfilesTab_PM().verify_description_below_the_title_is_correctly_displaying_in_hcm_sync_profiles_tab();
	}
	
	@Then("Verify Search bar text box is clickable in HCM Sync Profiles screen")
	public void verify_search_bar_text_box_is_clickable_in_hcm_sync_profiles_tab() throws IOException {
		validateHCMSyncProfilesTab_PM.getValidateHCMSyncProfilesTab_PM().verify_search_bar_text_box_is_clickable_in_hcm_sync_profiles_tab();
	}

	@Then("Verify Search bar placeholder text in HCM Sync Profiles screen")
	public void verify_search_bar_placeholder_text_in_hcm_sync_profiles_tab() throws IOException {
		validateHCMSyncProfilesTab_PM.getValidateHCMSyncProfilesTab_PM().verify_search_bar_placeholder_text_in_hcm_sync_profiles_tab();
	}

	@Then("Enter Job profile name in search bar in HCM Sync Profiles screen")
	public void enter_job_profile_name_in_search_bar_in_hcm_sync_profiles_tab() throws IOException {
		validateHCMSyncProfilesTab_PM.getValidateHCMSyncProfilesTab_PM().enter_job_profile_name_in_search_bar_in_hcm_sync_profiles_tab();
	}

	@Then("User should verify profile name matching profile is displaying in Organization jobs profile list")
	public void user_should_verify_profile_name_matching_profile_is_displaying_in_organization_jobs_profile_list() throws IOException {
		validateHCMSyncProfilesTab_PM.getValidateHCMSyncProfilesTab_PM().user_should_verify_profile_name_matching_profile_is_displaying_in_organization_jobs_profile_list();
	}

	@Then("Click on name matching profile in HCM Sync Profiles screen")
	public void click_on_name_matching_profile_in_hcm_sync_profiles_tab() throws IOException {
		validateHCMSyncProfilesTab_PM.getValidateHCMSyncProfilesTab_PM().click_on_name_matching_profile_in_hcm_sync_profiles_tab();
	}
	
	@Then("User should be navigated to SP details page on click of matching profile")
	public void user_should_be_navigated_to_sp_details_page_on_click_of_matching_profile() throws IOException {
		validateHCMSyncProfilesTab_PM.getValidateHCMSyncProfilesTab_PM().user_should_be_navigated_to_sp_details_page_on_click_of_matching_profile();
	}
	
	@Then("Clear Search bar in HCM Sync Profiles screen")
	public void clear_search_bar_in_hcm_sync_profiles_tab() throws IOException {
		validateHCMSyncProfilesTab_PM.getValidateHCMSyncProfilesTab_PM().clear_search_bar_in_hcm_sync_profiles_tab();
	}
	
	@Then("Verify job profiles count is displaying on the page in HCM Sync Profiles screen")
	public void verify_job_profiles_count_is_displaying_on_the_page_in_hcm_sync_profiles_tab() throws IOException {
		validateHCMSyncProfilesTab_PM.getValidateHCMSyncProfilesTab_PM().verify_job_profiles_count_is_displaying_on_the_page_in_hcm_sync_profiles_tab();
	}

	@Then("Scroll page to view more job profiles in HCM Sync Profiles screen")
	public void scroll_page_to_view_more_job_profiles_in_hcm_sync_profiles_tab() throws IOException {
		validateHCMSyncProfilesTab_PM.getValidateHCMSyncProfilesTab_PM().scroll_page_to_view_more_job_profiles_in_hcm_sync_profiles_tab();
	}

	@Then("User should verify count of job profiles is correctly showing on top of Job Profiles listing table in HCM Sync Profiles screen")
	public void user_should_verify_count_of_job_profiles_is_correctly_showing_on_top_of_job_profiles_listing_table_in_hcm_sync_profiles_tab() throws IOException {
		validateHCMSyncProfilesTab_PM.getValidateHCMSyncProfilesTab_PM().user_should_verify_count_of_job_profiles_is_correctly_showing_on_top_of_job_profiles_listing_table_in_hcm_sync_profiles_tab();
	}

	
	@When("User is in HCM Sync Profiles screen")
	public void user_is_in_hcm_sync_profiles_screen() throws IOException {
		validateHCMSyncProfilesTab_PM.getValidateHCMSyncProfilesTab_PM().user_is_in_hcm_sync_profiles_screen();
	}

	@Then("Click on Filters dropdown button in HCM Sync Profiles screen")
	public void click_on_filters_dropdown_button_in_hcm_sync_profiles_tab() throws IOException {
		validateHCMSyncProfilesTab_PM.getValidateHCMSyncProfilesTab_PM().click_on_filters_dropdown_button_in_hcm_sync_profiles_tab();
	}

	@Then("Verify options available inside Filters dropdown in HCM Sync Profiles screen")
	public void verify_options_available_inside_filters_dropdown_in_hcm_sync_profiles_tab() throws IOException {
		validateHCMSyncProfilesTab_PM.getValidateHCMSyncProfilesTab_PM().verify_options_available_inside_filters_dropdown_in_hcm_sync_profiles_tab();
	}

	@Then("Apply KF Grade filter and verify profiles count is correctly displaying in HCM Sync Profiles screen")
	public void apply_kf_grade_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab() throws IOException {
		validateHCMSyncProfilesTab_PM.getValidateHCMSyncProfilesTab_PM().apply_kf_grade_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab();
	}

	@Then("Clear KF Grade filter in HCM Sync Profiles screen")
	public void clear_kf_grade_filter_in_hcm_sync_profiles_tab() throws IOException {
		validateHCMSyncProfilesTab_PM.getValidateHCMSyncProfilesTab_PM().clear_kf_grade_filter_in_hcm_sync_profiles_tab();
	}

	@Then("Apply Levels filter and verify profiles count is correctly displaying in HCM Sync Profiles screen")
	public void apply_levels_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab() throws IOException {
		validateHCMSyncProfilesTab_PM.getValidateHCMSyncProfilesTab_PM().apply_levels_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab();
	}

	@Then("Clear Levels filter in HCM Sync Profiles screen")
	public void clear_levels_filter_in_hcm_sync_profiles_tab() throws IOException {
		validateHCMSyncProfilesTab_PM.getValidateHCMSyncProfilesTab_PM().clear_levels_filter_in_hcm_sync_profiles_tab();
	}

	@Then("Apply Functions or Subfunctions filter and verify profiles count is correctly displaying in HCM Sync Profiles screen")
	public void apply_functions_or_subfunctions_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab() throws IOException {
		validateHCMSyncProfilesTab_PM.getValidateHCMSyncProfilesTab_PM().apply_functions_or_subfunctions_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab();
	}

	@Then("Click on Clear All Filters button in HCM Sync Profiles screen")
	public void click_on_clear_all_filters_button_in_hcm_sync_profiles_tab() throws IOException {
		validateHCMSyncProfilesTab_PM.getValidateHCMSyncProfilesTab_PM().click_on_clear_all_filters_button_in_hcm_sync_profiles_tab();
	}
	
	@Then("Apply Profile Status filter and verify profiles count is correctly displaying in HCM Sync Profiles screen")
	public void apply_profile_status_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab() throws IOException {
		validateHCMSyncProfilesTab_PM.getValidateHCMSyncProfilesTab_PM().apply_profile_status_filter_and_verify_profiles_count_is_correctly_displaying_in_hcm_sync_profiles_tab();
	}
	
	@Then("User should verify Organization jobs table headers are correctly displaying in HCM Sync Profiles screen")
	public void user_should_verify_organization_jobs_table_headers_are_correctly_displaying_in_hcm_sync_profiles_tab() throws IOException {
		validateHCMSyncProfilesTab_PM.getValidateHCMSyncProfilesTab_PM().user_should_verify_organization_jobs_table_headers_are_correctly_displaying_in_hcm_sync_profiles_tab();
	}
	
	@Then("User should verify Download button is disabled in HCM Sync Profiles screen")
	public void user_should_verify_download_button_is_disabled_in_hcm_sync_profiles_tab() throws IOException {
		validateHCMSyncProfilesTab_PM.getValidateHCMSyncProfilesTab_PM().user_should_verify_download_button_is_disabled_in_hcm_sync_profiles_tab();
	}

	@Then("Click on header checkbox to select loaded job profiles in HCM Sync Profiles screen")
	public void click_on_header_checkbox_to_select_loaded_job_profiles_in_hcm_sync_profiles_tab() throws IOException {
		validateHCMSyncProfilesTab_PM.getValidateHCMSyncProfilesTab_PM().click_on_header_checkbox_to_select_loaded_job_profiles_in_hcm_sync_profiles_tab();
	}

	@Then("User should verify Download button is enabled in HCM Sync Profiles screen")
	public void user_should_verify_download_button_is_enabled_in_hcm_sync_profiles_tab() throws IOException {
		validateHCMSyncProfilesTab_PM.getValidateHCMSyncProfilesTab_PM().user_should_verify_download_button_is_enabled_in_hcm_sync_profiles_tab();
	}

	@Then("User should uncheck header checkbox to deselect selected job profiles in HCM Sync Profiles screen")
	public void user_should_uncheck_header_checkbox_to_deselect_selected_job_profiles_in_hcm_sync_profiles_tab() throws IOException {
		validateHCMSyncProfilesTab_PM.getValidateHCMSyncProfilesTab_PM().user_should_uncheck_header_checkbox_to_deselect_selected_job_profiles_in_hcm_sync_profiles_tab();
	}
	
	@Then("Click on first profile checkbox in HCM Sync Profiles screen")
	public void click_on_first_profile_checkbox_in_hcm_sync_profiles_tab() throws IOException {
		validateHCMSyncProfilesTab_PM.getValidateHCMSyncProfilesTab_PM().click_on_first_profile_checkbox_in_hcm_sync_profiles_tab();
	}

	@Then("Click on second profile checkbox in HCM Sync Profiles screen")
	public void click_on_second_profile_checkbox_in_hcm_sync_profiles_tab() throws IOException {
		validateHCMSyncProfilesTab_PM.getValidateHCMSyncProfilesTab_PM().click_on_second_profile_checkbox_in_hcm_sync_profiles_tab();
	}

	@Then("Click on third profile checkbox in HCM Sync Profiles screen")
	public void click_on_third_profile_checkbox_in_hcm_sync_profiles_tab() throws IOException {
		validateHCMSyncProfilesTab_PM.getValidateHCMSyncProfilesTab_PM().click_on_third_profile_checkbox_in_hcm_sync_profiles_tab();
	}
	
	@Then("User should verify Sync with HCM button is disabled in HCM Sync Profiles screen")
	public void user_should_verify_sync_with_hcm_button_is_disabled_in_hcm_sync_profiles_tab() throws IOException {
		validateHCMSyncProfilesTab_PM.getValidateHCMSyncProfilesTab_PM().user_should_verify_sync_with_hcm_button_is_disabled_in_hcm_sync_profiles_tab();
	}

	@Then("User should verify Sync with HCM button is enabled in HCM Sync Profiles screen")
	public void user_should_verify_sync_with_hcm_button_is_enabled_in_hcm_sync_profiles_tab() throws IOException {
		validateHCMSyncProfilesTab_PM.getValidateHCMSyncProfilesTab_PM().user_should_verify_sync_with_hcm_button_is_enabled_in_hcm_sync_profiles_tab();
	}
	
	@Then("Verify checkboxes of First, Second and Third Profile are selected in HCM Sync Profiles screen")
	public void verify_checkboxes_of_first_second_and_third_profile_are_selected_in_hcm_sync_profiles_tab() throws IOException {
		validateHCMSyncProfilesTab_PM.getValidateHCMSyncProfilesTab_PM().verify_checkboxes_of_first_second_and_third_profile_are_selected_in_hcm_sync_profiles_tab();
	}

	@Then("Click on Sync with HCM button in HCM Sync Profiles screen")
	public void click_on_sync_with_hcm_button_in_hcm_sync_profiles_tab() throws IOException {
		validateHCMSyncProfilesTab_PM.getValidateHCMSyncProfilesTab_PM().click_on_sync_with_hcm_button_in_hcm_sync_profiles_tab();
	}

	@Then("User should verify Sync with HCM success popup appears on screen in HCM Sync Profiles screen")
	public void user_should_verify_sync_with_hcm_success_popup_appears_on_screen_in_hcm_sync_profiles_tab() throws IOException {
		validateHCMSyncProfilesTab_PM.getValidateHCMSyncProfilesTab_PM().user_should_verify_sync_with_hcm_success_popup_appears_on_screen_in_hcm_sync_profiles_tab();
	}

	@Then("Verify checkboxes of First, Second and Third Profile are deselected in HCM Sync Profiles screen")
	public void verify_checkboxes_of_first_second_and_third_profile_are_deselected_in_hcm_sync_profiles_tab() throws IOException {
		validateHCMSyncProfilesTab_PM.getValidateHCMSyncProfilesTab_PM().verify_checkboxes_of_first_second_and_third_profile_are_deselected_in_hcm_sync_profiles_tab();
	}

}
