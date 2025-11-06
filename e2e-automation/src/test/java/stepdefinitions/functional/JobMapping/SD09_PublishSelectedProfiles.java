package stepdefinitions.functional.JobMapping;

import java.io.IOException;

import com.JobMapping.manager.PageObjectManager;

import io.cucumber.java.en.Then;

public class SD09_PublishSelectedProfiles {
	PageObjectManager publishSelectedProfiles = new PageObjectManager();
	
	public SD09_PublishSelectedProfiles() {
		super();		
	}
	
	@Then("Search for Published Job name1")
	public void search_for_published_job_name1() throws IOException {
		publishSelectedProfiles.getPublishSelectedProfiles().search_for_published_job_name1();
	}
	
	@Then("User should verify Published first job profile is displayed in Row1 in View Published screen")
	public void user_should_verify_published_first_job_profile_is_displayed_in_row1_in_view_published_screen() throws IOException {
		publishSelectedProfiles.getPublishSelectedProfiles().user_should_verify_published_first_job_profile_is_displayed_in_row1_in_view_published_screen();
	}
	
	@Then("Search for Published Job name2")
	public void search_for_published_job_name2() throws IOException {
		publishSelectedProfiles.getPublishSelectedProfiles().search_for_published_job_name2();
	}
	
	@Then("User should verify Published second job profile is displayed in Row1 in View Published screen")
	public void user_should_verify_published_second_job_profile_is_displayed_in_row1_in_view_published_screen() throws IOException {
		publishSelectedProfiles.getPublishSelectedProfiles().user_should_verify_published_second_job_profile_is_displayed_in_row1_in_view_published_screen();
	}
	
	@Then("User should verify Date on Published First Job matches with current date")
	public void user_should_verify_date_on_published_first_job_matches_with_current_date() throws IOException {
		publishSelectedProfiles.getPublishSelectedProfiles().user_should_verify_date_on_published_first_job_matches_with_current_date();
	}

	@Then("User should verify Date on Published Second Job matches with current date")
	public void user_should_verify_date_on_published_second_job_matches_with_current_date() throws IOException {
		publishSelectedProfiles.getPublishSelectedProfiles().user_should_verify_date_on_published_second_job_matches_with_current_date();
	}
	
	@Then("Search for Published Job name2 in HCM Sync Profiles screen in PM")
	public void search_for_published_job_name2_in_hcm_sync_profiles_tab_in_pm() throws IOException {
		publishSelectedProfiles.getPublishSelectedProfiles().search_for_published_job_name2_in_hcm_sync_profiles_tab_in_pm();
	}

	@Then("User should verify Published Second Job Profile is displayed in Row1 in HCM Sync Profiles screen in PM")
	public void user_should_verify_published_second_job_profile_is_displayed_in_row1_in_hcm_sync_profiles_tab_in_pm() throws IOException {
		publishSelectedProfiles.getPublishSelectedProfiles().user_should_verify_published_second_job_profile_is_displayed_in_row1_in_hcm_sync_profiles_tab_in_pm();
	}

}
