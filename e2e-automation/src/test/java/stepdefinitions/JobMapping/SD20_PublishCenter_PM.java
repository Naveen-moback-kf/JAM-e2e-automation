package stepdefinitions.JobMapping;

import java.io.IOException;

import com.kfonetalentsuite.manager.PageObjectManager;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SD20_PublishCenter_PM {	
	public SD20_PublishCenter_PM() {
		super();		
	}
	
	@Then("User is in HCM Sync Profiles screen after syncing profiles")
	public void user_is_in_hcm_sync_profiles_screen_after_syncing_profiles() throws IOException {
		PageObjectManager.getInstance().getPublishCenter_PM().user_is_in_hcm_sync_profiles_screen_after_syncing_profiles();
	}
	
	@Then("Click on Publish Center button")
	public void click_on_publish_center_button() throws IOException {
		PageObjectManager.getInstance().getPublishCenter_PM().click_on_publish_center_button();
	}

	@Then("Verify user navigated to Job Profile History screen succcessfully")
	public void verify_user_navigated_to_job_profile_history_screen_succcessfully() throws IOException {
		PageObjectManager.getInstance().getPublishCenter_PM().verify_user_navigated_to_job_profile_history_screen_succcessfully();
	}

	@Then("Verify Recently downloaded job profiles history is in top row")
	public void verify_recently_downloaded_job_profiles_history_is_in_top_row() throws IOException {
		PageObjectManager.getInstance().getPublishCenter_PM().verify_recently_downloaded_job_profiles_history_is_in_top_row();
	}

	@Then("Verify details of the Recently downloaded job profiles in Job Profile History screen")
	public void verify_details_of_the_recently_downloaded_job_profiles_in_job_profile_history_screen() throws IOException {
		PageObjectManager.getInstance().getPublishCenter_PM().verify_details_of_the_recently_downloaded_job_profiles_in_job_profile_history_screen();
	}

	@Then("Click on Profiles count of Recently downloaded job profiles in Job Profile History screen")
	public void click_on_profiles_count_of_recently_downloaded_job_profiles_in_job_profile_history_screen() throws IOException {
		PageObjectManager.getInstance().getPublishCenter_PM().click_on_profiles_count_of_recently_downloaded_job_profiles_in_job_profile_history_screen();
	}

	@Then("User should be navigated to Profiles Downloaded screen")
	public void user_should_be_navigated_to_profiles_downloaded_screen() throws IOException {
		PageObjectManager.getInstance().getPublishCenter_PM().user_should_be_navigated_to_profiles_downloaded_screen();
	}

	@Then("Verify details in Profiles Downloaded screen")
	public void verify_details_in_profiles_downloaded_screen() throws IOException {
		PageObjectManager.getInstance().getPublishCenter_PM().verify_details_in_profiles_downloaded_screen();
	}

	@Then("Close Profiles Downloaded screen")
	public void close_profiles_downloaded_screen() throws IOException {
		PageObjectManager.getInstance().getPublishCenter_PM().close_profiles_downloaded_screen();
	}
	
	@Then("Select Job Profiles in HCM Sync Profiles screen")
	public void select_job_profiles_in_hcm_sync_profiles_tab() throws IOException {
		PageObjectManager.getInstance().getPublishCenter_PM().select_job_profiles_in_hcm_sync_profiles_tab();
	}

	@Then("Verify Recently exported job profiles history is in top row")
	public void verify_recently_exported_job_profiles_history_is_in_top_row() throws IOException {
		PageObjectManager.getInstance().getPublishCenter_PM().verify_recently_exported_job_profiles_history_is_in_top_row();
	}

	@Then("Verify details of the Recently exported job profiles in Job Profile History screen")
	public void verify_details_of_the_recently_exported_job_profiles_in_job_profile_history_screen() throws IOException {
		PageObjectManager.getInstance().getPublishCenter_PM().verify_details_of_the_recently_exported_job_profiles_in_job_profile_history_screen();
	}

	@Then("Click on Profiles count of Recently exported job profiles in Job Profile History screen")
	public void click_on_profiles_count_of_recently_exported_job_profiles_in_job_profile_history_screen() throws IOException {
		PageObjectManager.getInstance().getPublishCenter_PM().click_on_profiles_count_of_recently_exported_job_profiles_in_job_profile_history_screen();
	}

	@Then("User should be navigated to Profiles Exported screen")
	public void user_should_be_navigated_to_profiles_exported_screen() throws IOException {
		PageObjectManager.getInstance().getPublishCenter_PM().user_should_be_navigated_to_profiles_exported_screen();
	}

	@Then("Verify details in Profiles Exported screen")
	public void verify_details_in_profiles_exported_screen() throws IOException {
		PageObjectManager.getInstance().getPublishCenter_PM().verify_details_in_profiles_exported_screen();
	}

	@Then("Close Profiles Exported screen")
	public void close_profiles_exported_screen() throws IOException {
		PageObjectManager.getInstance().getPublishCenter_PM().close_profiles_exported_screen();
	}
	
	@When("User is in Job Profile History screen")
	public void user_is_in_job_profile_history_screen() throws IOException {
		PageObjectManager.getInstance().getPublishCenter_PM().user_is_in_job_profile_history_screen();
	}
	
	@Then("User should scroll page down two times to view first thirty job profiles in Job Profile History screen")
	public void user_should_scroll_page_down_two_times_to_view_first_thirty_job_profiles_in_job_profile_history_screen() throws IOException {
		PageObjectManager.getInstance().getPublishCenter_PM().user_should_scroll_page_down_two_times_to_view_first_thirty_job_profiles_in_job_profile_history_screen();
	}

	@Then("User should verify first thirty job profiles in default order before applying sorting in Job Profile History screen")
	public void user_should_verify_first_thirty_job_profiles_in_default_order_before_applying_sorting_in_job_profile_history_screen() throws IOException {
		PageObjectManager.getInstance().getPublishCenter_PM().user_should_verify_first_thirty_job_profiles_in_default_order_before_applying_sorting_in_job_profile_history_screen();
	}
	
	@Then("Sort Job Profiles by No. of Profiles in Ascending order in Job Profile History screen")
	public void sort_job_profiles_by_no_of_profiles_in_ascending_order_in_job_profile_history_screen() throws IOException {
		PageObjectManager.getInstance().getPublishCenter_PM().sort_job_profiles_by_no_of_profiles_in_ascending_order_in_job_profile_history_screen();
	}

	@Then("User should verify first thirty job profiles sorted by No. of Profiles in Ascending order in Job Profile History screen")
	public void user_should_verify_first_thirty_job_profiles_sorted_by_no_of_profiles_in_ascending_order_in_job_profile_history_screen() throws IOException {
		PageObjectManager.getInstance().getPublishCenter_PM().user_should_verify_first_thirty_job_profiles_sorted_by_no_of_profiles_in_ascending_order_in_job_profile_history_screen();
	}
	
	@Then("User should Refresh Job Profile History screen and Verify Job Profiles are in default order")
	public void user_should_refresh_job_profile_history_screen_and_verify_job_profiles_are_in_default_order() throws IOException {
		PageObjectManager.getInstance().getPublishCenter_PM().user_should_refresh_job_profile_history_screen_and_verify_job_profiles_are_in_default_order();
	}
	
	@Then("Sort Job Profiles by No. of Profiles in Descending order in Job Profile History screen")
	public void sort_job_profiles_by_no_of_profiles_in_descending_order_in_job_profile_history_screen() throws IOException {
		PageObjectManager.getInstance().getPublishCenter_PM().sort_job_profiles_by_no_of_profiles_in_descending_order_in_job_profile_history_screen();
	}

	@Then("User should verify first thirty job profiles sorted by No. of Profiles in Descending order in Job Profile History screen")
	public void user_should_verify_first_thirty_job_profiles_sorted_by_no_of_profiles_in_descending_order_in_job_profile_history_screen() throws IOException {
		PageObjectManager.getInstance().getPublishCenter_PM().user_should_verify_first_thirty_job_profiles_sorted_by_no_of_profiles_in_descending_order_in_job_profile_history_screen();
	}
	
	@Then("Sort Job Profiles by Accessed Date in Ascending order in Job Profile History screen")
	public void sort_job_profiles_by_accessed_date_in_ascending_order_in_job_profile_history_screen() throws IOException {
		PageObjectManager.getInstance().getPublishCenter_PM().sort_job_profiles_by_accessed_date_in_ascending_order_in_job_profile_history_screen();
	}

	@Then("User should verify first thirty job profiles sorted by Accessed Date in Ascending order in Job Profile History screen")
	public void user_should_verify_first_thirty_job_profiles_sorted_by_accessed_date_in_ascending_order_in_job_profile_history_screen() throws IOException {
		PageObjectManager.getInstance().getPublishCenter_PM().user_should_verify_first_thirty_job_profiles_sorted_by_accessed_date_in_ascending_order_in_job_profile_history_screen();
	}

	@Then("Sort Job Profiles by Accessed Date in Descending order in Job Profile History screen")
	public void sort_job_profiles_by_accessed_date_in_descending_order_in_job_profile_history_screen() throws IOException {
		PageObjectManager.getInstance().getPublishCenter_PM().sort_job_profiles_by_accessed_date_in_descending_order_in_job_profile_history_screen();
	}

	@Then("User should verify first thirty job profiles sorted by Accessed Date in Descending order in Job Profile History screen")
	public void user_should_verify_first_thirty_job_profiles_sorted_by_accessed_date_in_descending_order_in_job_profile_history_screen() throws IOException {
		PageObjectManager.getInstance().getPublishCenter_PM().user_should_verify_first_thirty_job_profiles_sorted_by_accessed_date_in_descending_order_in_job_profile_history_screen();
	}

}

