package stepdefinitions.functional.JobMapping;

import java.io.IOException;

import com.JobMapping.manager.PageObjectManager;
import com.JobMapping.webdriverManager.DriverManager;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.Scenario;
import stepdefinitions.hooks.ScenarioHooks;

public class SD04_VerifyJobMappingPageComponents extends DriverManager{
	PageObjectManager verifyJobMappingPageComponents = new PageObjectManager();
	
	public SD04_VerifyJobMappingPageComponents() {
		super();		
	}
	

	@Then("User should be landed on Job Mapping page")
	public void user_should_be_landed_on_job_profile_mapping_page() throws IOException {
		verifyJobMappingPageComponents.getVerifyJobMappingPageComponents().user_should_be_landed_on_job_mapping_page();
	}
	
	@When("User is in Job Mapping page")
	public void user_is_in_job_mapping_page() throws IOException, InterruptedException {
		// Get scenario from ScenarioHooks ThreadLocal storage
		Scenario scenario = ScenarioHooks.getCurrentScenario();
		
		if (scenario != null) {
			String featureUri = scenario.getUri().toString();
			boolean isMissingDataFeature = featureUri.contains("29ValidateJobsWithMissingGRADEdataInJobMapping") ||
			                                featureUri.contains("30ValidateJobsWithMissingDEPARTMENTdataInJobMapping") ||
			                                featureUri.contains("31ValidateJobsWithMissingFUNCTIONdataInJobMapping") ||
			                                featureUri.contains("32ValidateJobsWithMissingSUBFUNCTIONdataInJobMapping");
			
			if (isMissingDataFeature) {
				try {
					verifyJobMappingPageComponents.getValidateJobsWithMissingGRADEdataInJobMapping().ensure_on_job_mapping_page_for_next_scenario();
				} catch (Exception e) {
					System.out.println("Cleanup step completed (expected for fresh scenarios)");
				}
			}
		}
		
		verifyJobMappingPageComponents.getVerifyJobMappingPageComponents().user_is_in_job_mapping_page();
	}
	
	@Then("User should verify Job Mapping logo is displayed on screen")
	public void user_should_verify_job_mapping_logo_is_displayed_on_screen() throws IOException, InterruptedException {
		verifyJobMappingPageComponents.getVerifyJobMappingPageComponents().user_should_verify_job_mapping_logo_is_displayed_on_screen();
	}
	
	@Then("Navigate to Job Mapping page from KFONE Global Menu in PM")
	public void navigate_to_job_mapping_page_from_kfone_global_menu_in_pm() throws IOException, InterruptedException {
		verifyJobMappingPageComponents.getVerifyJobMappingPageComponents().navigate_to_job_mapping_page_from_kfone_global_menu_in_pm();
	}

	@Then("Verify title header is correctly displaying")
	public void verify_title_header_is_correctly_displaying() throws IOException, InterruptedException {
		verifyJobMappingPageComponents.getVerifyJobMappingPageComponents().verify_title_header_is_correctly_displaying();
	}

	@Then("Verify title description below the title header")
	public void verify_title_description_below_the_title_header() throws IOException {
		verifyJobMappingPageComponents.getVerifyJobMappingPageComponents().verify_title_description_below_the_title_header();
	}
	
	
	@Then("Verify Organization Jobs Search bar text box is clickable")
	public void verify_organization_jobs_search_bar_text_box_is_clickable() throws IOException {
		verifyJobMappingPageComponents.getVerifyJobMappingPageComponents().verify_organization_jobs_search_bar_text_box_is_clickable();
	}
	
	@Then("Verify Organization Jobs Search bar placeholder text")
	public void verify_organization_jobs_search_bar_placeholder_text() throws IOException {
		verifyJobMappingPageComponents.getVerifyJobMappingPageComponents().verify_organization_jobs_search_bar_placeholder_text();
	}

	@Then("Enter Job name substring in search bar")
	public void enter_job_name_substring_in_search_bar() throws IOException {
		verifyJobMappingPageComponents.getVerifyJobMappingPageComponents().enter_job_name_substring_in_search_bar();
	}

//	@Then("User should verify job name matching profile is displaying in first row in Organization jobs profile list")
//	public void user_should_verify_job_name_matching_profile_is_displaying_in_first_row_in_organization_jobs_profile_list() throws Exception {
//		verifyJobMappingPageComponents.getVerifyJobMappingPageComponents().user_should_verify_job_name_matching_profile_is_displaying_in_first_row_in_organization_jobs_profile_list();
//	}
	
	
	@Then("Click on matched profile of job in first row")
	public void click_on_matched_profile_of_job_in_first_row() throws IOException {
		verifyJobMappingPageComponents.getVerifyJobMappingPageComponents().click_on_matched_profile_of_job_in_first_row();
	}

	@Then("Verify Profile details popup is displayed")
	public void verify_profile_details_popup_is_displayed() throws IOException {
		verifyJobMappingPageComponents.getVerifyJobMappingPageComponents().verify_profile_details_popup_is_displayed();
	}

	@Then("Click on close button in profile details popup")
	public void click_on_close_button_in_profile_details_popup() throws IOException {
		verifyJobMappingPageComponents.getVerifyJobMappingPageComponents().click_on_close_button_in_profile_details_popup();
	}
	
	@Then("Click on Filters dropdown button")
	public void click_on_filters_dropdown_button() throws IOException {
		verifyJobMappingPageComponents.getVerifyJobMappingPageComponents().click_on_filters_dropdown_button();
	}

	@Then("Verify options available inside Filters dropdown")
	public void verify_options_available_inside_filters_dropdown() throws IOException {
		verifyJobMappingPageComponents.getVerifyJobMappingPageComponents().verify_options_available_inside_filters_dropdown();
	}

	@Then("Close the Filters dropdown")
	public void close_the_filters_dropdown() throws IOException {
		verifyJobMappingPageComponents.getVerifyJobMappingPageComponents().close_the_filters_dropdown();
	}
	
	@Then("User should see Add more jobs button is displaying")
	public void user_should_see_add_more_jobs_button_is_displaying() throws IOException {
		verifyJobMappingPageComponents.getVerifyJobMappingPageComponents().user_should_see_add_more_jobs_button_is_displaying();
	}

	@Then("Verify Add more jobs button is clickable")
	public void verify_add_more_jobs_button_is_clickable() throws IOException, InterruptedException {
		verifyJobMappingPageComponents.getVerifyJobMappingPageComponents().verify_add_more_jobs_button_is_clickable();
	}
	
	@Then("Verify User landed on Add more jobs page")
	public void verify_user_landed_on_add_more_jobs_page() throws IOException {
		verifyJobMappingPageComponents.getVerifyJobMappingPageComponents().verify_user_landed_on_add_more_jobs_page();
	}

	@Then("Close Add more jobs page")
	public void close_add_more_jobs_page() throws IOException {
		verifyJobMappingPageComponents.getVerifyJobMappingPageComponents().close_add_more_jobs_page();
	}

	@Then("User should verify Publish Selected Profiles button is disabled")
	public void user_should_verify_publish_selected_profiles_button_is_disabled() throws IOException {
		verifyJobMappingPageComponents.getVerifyJobMappingPageComponents().user_should_verify_publish_selected_profiles_button_is_disabled();
	}
	

	@Then("Click on header checkbox to select loaded job profiles in Job Mapping screen")
	public void click_on_header_checkbox_to_select_loaded_job_profiles_in_job_mapping_screen() throws IOException {
		verifyJobMappingPageComponents.getVerifyJobMappingPageComponents().click_on_header_checkbox_to_select_loaded_job_profiles_in_job_mapping_screen();
	}
	
	@Then("Clear Search bar in Job Mapping page")
	public void clear_search_bar_in_job_mapping_page() throws IOException {
		verifyJobMappingPageComponents.getVerifyJobMappingPageComponents().clear_search_bar_in_job_mapping_page();
	}

	@Then("User should verify Publish Selected Profiles button is enabled")
	public void user_should_verify_publish_selected_profiles_button_is_enabled() throws IOException {
		verifyJobMappingPageComponents.getVerifyJobMappingPageComponents().user_should_verify_publish_selected_profiles_button_is_enabled();
	}

	@Then("User should uncheck header checkbox to deselect selected job profiles")
	public void user_should_uncheck_header_checkbox_to_deselect_selected_job_profiles() throws IOException {
		verifyJobMappingPageComponents.getVerifyJobMappingPageComponents().user_should_uncheck_header_checkbox_to_deselect_selected_job_profiles();
	}
	
	@Then("Click on checkbox of first job profile")
	public void click_on_checkbox_of_first_job_profile() throws IOException {
		verifyJobMappingPageComponents.getVerifyJobMappingPageComponents().click_on_checkbox_of_first_job_profile();
	}

	@Then("Click on checkbox of second job profile")
	public void click_on_checkbox_of_second_job_profile() throws IOException {
		verifyJobMappingPageComponents.getVerifyJobMappingPageComponents().click_on_checkbox_of_second_job_profile();
	}
	
	@Then("Click on Publish Selected Profiles button")
	public void click_on_publish_selected_profiles_button() throws IOException {
		verifyJobMappingPageComponents.getVerifyJobMappingPageComponents().click_on_publish_selected_profiles_button();
	}
	
	@Then("Verify job profiles count is displaying on the page")
	public void verify_job_profiles_count_is_displaying_on_the_page() throws IOException {
		verifyJobMappingPageComponents.getVerifyJobMappingPageComponents().verify_job_profiles_count_is_displaying_on_the_page();
	}
	
	@Then("Scroll page to view more job profiles")
	public void scroll_page_to_view_more_job_profiles() throws IOException, InterruptedException {
		verifyJobMappingPageComponents.getVerifyJobMappingPageComponents().scroll_page_to_view_more_job_profiles();
	}

	@Then("User should verify count of job profiles is correctly showing on top of Job Profiles listing table")
	public void user_should_verify_count_of_job_profiles_is_correctly_showing_on_top_of_job_profiles_listing_table() throws IOException, InterruptedException {
		verifyJobMappingPageComponents.getVerifyJobMappingPageComponents().user_should_verify_count_of_job_profiles_is_correctly_showing_on_top_of_job_profiles_listing_table();
	}
	
	@Then("User should verify view published toggle button is displaying")
	public void user_should_verify_view_published_toggle_button_is_displaying() throws IOException {
		verifyJobMappingPageComponents.getVerifyJobMappingPageComponents().user_should_verify_view_published_toggle_button_is_displaying();
	}
	
	@Then("User should verify Organization Job Name and Job Code values of first profile")
	public void user_should_verify_organization_job_name_and_job_code_values_of_first_profile() throws IOException {
		verifyJobMappingPageComponents.getVerifyJobMappingPageComponents().user_should_verify_organization_job_name_and_job_code_values_of_first_profile();
	}
	
	@Then("User should verify Organization Job Grade and Department values of first profile")
	public void user_should_verify_organization_job_grade_and_department_values_of_first_profile() throws IOException {
		verifyJobMappingPageComponents.getVerifyJobMappingPageComponents().user_should_verify_organization_job_grade_and_department_values_of_first_profile();
	}
	
	@Then("User should verify Organization Job Function or Subfunction of first profile")
	public void user_should_verify_organization_job_function_or_sub_function_of_first_profile() throws IOException {
		verifyJobMappingPageComponents.getVerifyJobMappingPageComponents().user_should_verify_organization_job_function_or_sub_function_of_first_profile();
	}

	@Then("Click on toggle button to turn on")
	public void click_on_toggle_button_to_turn_on() throws IOException, InterruptedException {
		verifyJobMappingPageComponents.getVerifyJobMappingPageComponents().click_on_toggle_button_to_turn_on();
	}

	@Then("User should verify published jobs are displaying in the listing table")
	public void user_should_verify_published_jobs_are_displaying_in_the_listing_table() throws IOException {
		verifyJobMappingPageComponents.getVerifyJobMappingPageComponents().user_should_verify_published_jobs_are_displaying_in_the_listing_table();
	}

	@Then("Click on toggle button to turn off")
	public void click_on_toggle_button_to_turn_off() throws IOException, InterruptedException {
		verifyJobMappingPageComponents.getVerifyJobMappingPageComponents().click_on_toggle_button_to_turn_off();
	}

	@Then("User should verify unpublished jobs are displaying in the listing table")
	public void user_should_verify_unpublished_jobs_are_displaying_in_the_listing_table() throws IOException {
		verifyJobMappingPageComponents.getVerifyJobMappingPageComponents().user_should_verify_unpublished_jobs_are_displaying_in_the_listing_table();
	}
	
	@Then("User should verify Organization jobs table title and headers")
	public void user_should_verify_organization_jobs_table_title_and_headers() throws IOException {
		verifyJobMappingPageComponents.getVerifyJobMappingPageComponents().user_should_verify_organization_jobs_table_title_and_headers();
	}

	@Then("User should verify Matched success profiles table title and headers")
	public void user_should_verify_matched_success_profiles_table_title_and_headers() throws IOException {
		verifyJobMappingPageComponents.getVerifyJobMappingPageComponents().user_should_verify_matched_success_profiles_table_title_and_headers();
	}
	
	
	@Then("Click on View Other Matches button")
	public void click_on_view_other_matches_button() throws IOException {
		verifyJobMappingPageComponents.getVerifyJobMappingPageComponents().click_on_view_other_matches_button();
	}

	@Then("Verify user landed on job compare page")
	public void verify_user_landed_on_job_compare_page() throws IOException, InterruptedException {
		verifyJobMappingPageComponents.getVerifyJobMappingPageComponents().verify_user_landed_on_job_compare_page();
	}

	@Then("User should verify Publish button on Matched success profile is displaying and clickable")
	public void user_should_verify_publish_button_on_matched_success_profile_is_displaying_and_clickable() throws IOException {
		verifyJobMappingPageComponents.getVerifyJobMappingPageComponents().user_should_verify_publish_button_on_matched_success_profile_is_displaying_and_clickable();
	}
	
	@Then("User should get success profile published popup")
	public void user_should_get_success_profile_published_popup() throws IOException {
		verifyJobMappingPageComponents.getVerifyJobMappingPageComponents().user_should_get_success_profile_published_popup();
	}
	
	@Then("Close the success profile published popup")
	public void close_the_success_profile_published_popup() throws IOException {
		verifyJobMappingPageComponents.getVerifyJobMappingPageComponents().close_the_success_profile_published_popup();
	}	
}
