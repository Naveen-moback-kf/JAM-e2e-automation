package stepdefinitions.JobMapping;

import java.io.IOException;

import com.kfonetalentsuite.manager.PageObjectManager;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SD40_ValidateSelectAndPublishAllJobProfilesinJAM {
	PageObjectManager validateSelectAndPublishAllJobProfilesinJAM = new PageObjectManager();
	
	public SD40_ValidateSelectAndPublishAllJobProfilesinJAM() {
		super();		
	}
	
	@Then("Verify count of total Un-Published Profiles before Publishing selected profiles")
	public void verify_count_of_total_un_published_profiles_before_publishing_selected_profiles() throws IOException {
		validateSelectAndPublishAllJobProfilesinJAM.getValidateSelectAndPublishAllJobProfilesinJAM().verify_count_of_total_un_published_profiles_before_publishing_selected_profiles();
	}
	
	@Then("Verify count of total Published Profiles before Publishing selected profiles")
	public void verify_count_of_total_published_profiles_before_publishing_selected_profiles() throws IOException {
		validateSelectAndPublishAllJobProfilesinJAM.getValidateSelectAndPublishAllJobProfilesinJAM().verify_count_of_total_published_profiles_before_publishing_selected_profiles();
	}
	
	@Then("Click on View Published toggle button to turn off")
	public void click_on_view_published_toggle_button_to_turn_off() throws IOException {
		validateSelectAndPublishAllJobProfilesinJAM.getValidateSelectAndPublishAllJobProfilesinJAM().click_on_view_published_toggle_button_to_turn_off();
	}
	
	@Then("Click on Chevron button beside header checkbox in Job Mapping screen")
	public void click_on_chevron_button_beside_header_checkbox_in_job_mapping_screen() throws IOException {
		validateSelectAndPublishAllJobProfilesinJAM.getValidateSelectAndPublishAllJobProfilesinJAM().click_on_chevron_button_beside_header_checkbox_in_job_mapping_screen();
	}
	
	@Then("Click on Select All button in Job Mapping screen")
	public void click_on_select_all_button_in_job_mapping_screen() throws IOException {
		validateSelectAndPublishAllJobProfilesinJAM.getValidateSelectAndPublishAllJobProfilesinJAM().click_on_select_all_button_in_job_mapping_screen();
	}
	
	@Then("Verify count of selected profiles by scrolling through all profiles in Job Mapping screen")
	public void verify_count_of_selected_profiles_by_scrolling_through_all_profiles_in_job_mapping_screen() throws IOException {
		validateSelectAndPublishAllJobProfilesinJAM.getValidateSelectAndPublishAllJobProfilesinJAM().verify_count_of_selected_profiles_by_scrolling_through_all_profiles_in_job_mapping_screen();
	}
	
	@Then("Verify Async functionality message is displayed on JAM screen")
	public void verify_async_functionality_message_is_displayed_on_jam_screen() throws IOException {
		validateSelectAndPublishAllJobProfilesinJAM.getValidateSelectAndPublishAllJobProfilesinJAM().verify_async_functionality_message_is_displayed_on_jam_screen();
	}
	
	@Then("Refresh Job Mapping page after specified time in message")
	public void refresh_job_mapping_page_after_specified_time_in_message() throws IOException, InterruptedException {
		validateSelectAndPublishAllJobProfilesinJAM.getValidateSelectAndPublishAllJobProfilesinJAM().refresh_job_mapping_page_after_specified_time_in_message();
	}
	
	@Then("Verify count of total Un-Published Profiles after Publishing selected profiles")
	public void verify_count_of_total_un_published_profiles_after_publishing_selected_profiles() throws IOException {
		validateSelectAndPublishAllJobProfilesinJAM.getValidateSelectAndPublishAllJobProfilesinJAM().verify_count_of_total_un_published_profiles_after_publishing_selected_profiles();
	}
	
	@Then("Get count of Published profiles in Job Mapping screen")
	public void get_count_of_published_profiles_in_job_mapping_screen() throws IOException {
		validateSelectAndPublishAllJobProfilesinJAM.getValidateSelectAndPublishAllJobProfilesinJAM().get_count_of_published_profiles_in_job_mapping_screen();
	}
	
	@Then("Verify count of total Published Profiles after Publishing selected profiles")
	public void verify_count_of_total_published_profiles_after_publishing_selected_profiles() throws IOException {
		validateSelectAndPublishAllJobProfilesinJAM.getValidateSelectAndPublishAllJobProfilesinJAM().verify_count_of_total_published_profiles_after_publishing_selected_profiles();
	}
	
	@Then("Verify Published Profiles count matches in View Published screen")
	public void verify_published_profiles_count_matches_in_view_published_screen() throws IOException {
		validateSelectAndPublishAllJobProfilesinJAM.getValidateSelectAndPublishAllJobProfilesinJAM().verify_published_profiles_count_matches_in_view_published_screen();
	}
	
	@When("User is in Job Mapping page after initial refresh")
	public void user_is_in_job_mapping_page_after_initial_refresh() throws IOException {
		validateSelectAndPublishAllJobProfilesinJAM.getValidateSelectAndPublishAllJobProfilesinJAM().user_is_in_job_mapping_page_after_initial_refresh();
	}
	
	@Then("Calculate expected total time for batch publishing based on profile count")
	public void calculate_expected_total_time_for_batch_publishing_based_on_profile_count() throws IOException {
		validateSelectAndPublishAllJobProfilesinJAM.getValidateSelectAndPublishAllJobProfilesinJAM().calculate_expected_total_time_for_batch_publishing_based_on_profile_count();
	}
	
	@Then("Monitor and validate progressive batch publishing until completion")
	public void monitor_and_validate_progressive_batch_publishing_until_completion() throws IOException, InterruptedException {
		validateSelectAndPublishAllJobProfilesinJAM.getValidateSelectAndPublishAllJobProfilesinJAM().monitor_and_validate_progressive_batch_publishing_until_completion();
	}
	
	@Then("Verify all profiles are published successfully")
	public void verify_all_profiles_are_published_successfully() throws IOException {
		validateSelectAndPublishAllJobProfilesinJAM.getValidateSelectAndPublishAllJobProfilesinJAM().verify_all_profiles_are_published_successfully();
	}

}

