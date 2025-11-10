package stepdefinitions.JobMapping;

import java.io.IOException;

import com.kfonetalentsuite.manager.PageObjectManager;

import io.cucumber.java.en.Then;

public class SD06_PublishJobProfile {
	
	PageObjectManager publishJobProfile = new PageObjectManager();
	
	public SD06_PublishJobProfile() {
		super();		
	}
	
	@Then("User should verify Publish button is displaying on first job profile")
	public void user_should_verify_publish_button_is_displaying_on_first_job_profile() throws IOException {
		publishJobProfile.getPublishJobProfile().user_should_verify_publish_button_is_displaying_on_first_job_profile();
	}

	@Then("Click on Publish button on first job profile")
	public void click_on_publish_button_on_first_job_profile() throws IOException {
		publishJobProfile.getPublishJobProfile().click_on_publish_button_on_first_job_profile();
	}

	@Then("User should verify publish success popup appears on screen")
	public void user_should_verify_publish_success_popup_appears_on_screen() throws IOException {
		publishJobProfile.getPublishJobProfile().user_should_verify_publish_success_popup_appears_on_screen();
	}
	
	@Then("Click on View Published toggle button to turn on")
	public void click_on_view_published_toggle_button_to_turn_on() throws IOException {
		publishJobProfile.getPublishJobProfile().click_on_view_published_toggle_button_to_turn_on();
	}

	@Then("Search for Published Job name in View Published screen")
	public void search_for_published_job_name_in_view_published_screen() throws IOException {
		publishJobProfile.getPublishJobProfile().search_for_published_job_name_in_view_published_screen();
	}
	
	@Then("User should verify Published job is displayed in View Published screen")
	public void user_should_verify_published_job_is_displayed_in_view_published_screen() throws IOException {
		publishJobProfile.getPublishJobProfile().user_should_verify_published_job_is_displayed_in_view_published_screen();
	}
	
	@Then("Verify user should land on Profile Manager dashboard page")
	public void verify_user_should_land_on_profile_manager_page() throws IOException {
		publishJobProfile.getPublishJobProfile().verify_user_should_land_on_profile_manager_dashboard_page();
	}
	
	@Then("User should navigate to HCM Sync Profiles screen in PM")
	public void user_should_navigate_to_hcm_sync_profiles_tab_in_pm() throws IOException {
		publishJobProfile.getPublishJobProfile().user_should_navigate_to_hcm_sync_profiles_tab_in_pm();
	}
	
	@Then("Search for Published Job name in HCM Sync Profiles screen in PM")
	public void search_for_published_job_name_in_hcm_sync_profiles_tab_in_pm() throws IOException {
		publishJobProfile.getPublishJobProfile().search_for_published_job_name_in_hcm_sync_profiles_tab_in_pm();
	}

	@Then("User should verify Published Job is displayed in HCM Sync Profiles screen in PM")
	public void user_should_verify_published_job_is_displayed_in_hcm_sync_profiles_tab_in_pm() throws IOException {
		publishJobProfile.getPublishJobProfile().user_should_verify_published_job_is_displayed_in_hcm_sync_profiles_tab_in_pm();
	}
	
	@Then("User should verify Date on Published Job matches with current date")
	public void user_should_verify_date_on_published_job_matches_with_current_date() throws IOException {
		publishJobProfile.getPublishJobProfile().user_should_verify_date_on_published_job_matches_with_current_date();
	}
	
	@Then("User should verify SP details page opens on click of Published Job name")
	public void user_should_verify_sp_details_page_opens_on_click_of_published_job_name() throws IOException {
		publishJobProfile.getPublishJobProfile().user_should_verify_sp_details_page_opens_on_click_of_published_job_name();
	}
	
	@Then("Click on KFONE Global Menu in Job Mapping UI")
	public void click_on_kfone_global_menu_in_job_mapping_ui() throws IOException {
		publishJobProfile.getPublishJobProfile().click_on_kfone_global_menu_in_job_mapping_ui();
	}
	
	@Then("Click on Profile Manager application button in KFONE Global Menu")
	public void click_on_profile_manager_application_button_in_kfone_global_menu() throws IOException {
		publishJobProfile.getPublishJobProfile().click_on_profile_manager_application_button_in_kfone_global_menu();
	}
	
	@Then("Click on Architect application button in KFONE Global Menu")
	public void click_on_architect_application_button_in_kfone_global_menu() throws IOException {
		publishJobProfile.getPublishJobProfile().click_on_architect_application_button_in_kfone_global_menu();
	}
	
	@Then("Verify user should land on Architect dashboard page")
	public void verify_user_should_land_on_architect_page() throws IOException {
		publishJobProfile.getPublishJobProfile().verify_user_should_land_on_architect_dashboard_page();
	}
	
	@Then("User should navigate to Jobs page in Architect")
	public void user_should_navigate_to_jobs_page_in_architect() throws IOException {
		publishJobProfile.getPublishJobProfile().user_should_navigate_to_jobs_page_in_architect();
	}
	
	@Then("Search for Published Job name in Jobs page in Architect")
	public void search_for_published_job_name_in_jobs_page_in_architect() throws IOException {
		publishJobProfile.getPublishJobProfile().search_for_published_job_name_in_jobs_page_in_architect();
	}
	
	@Then("User should verify Published Job is displayed in Jobs page in Architect")
	public void user_should_verify_published_job_is_displayed_in_jobs_page_in_architect() throws IOException {
		publishJobProfile.getPublishJobProfile().user_should_verify_published_job_is_displayed_in_jobs_page_in_architect();
	}
	
	@Then("User should verify Updated Date on Published Job matches with current date in Architect")
	public void user_should_verify__updated_date_on_published_job_matches_with_current_date_in_architect() throws IOException {
		publishJobProfile.getPublishJobProfile().user_should_verify__updated_date_on_published_job_matches_with_current_date_in_architect();
	}

}
