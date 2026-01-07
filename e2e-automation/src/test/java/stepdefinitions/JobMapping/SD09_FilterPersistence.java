package stepdefinitions.JobMapping;

import java.io.IOException;

import com.kfonetalentsuite.manager.PageObjectManager;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SD09_FilterPersistence {	
	public SD09_FilterPersistence() {
		super();		
	}
	
	@Then("Refresh Job Mapping page")
	public void refresh_job_mapping_page() throws IOException {
		PageObjectManager.getInstance().getFilterPersistence().refresh_job_mapping_page();
	}

	@Then("Verify Applied Filters persist on Job Mapping UI")
	public void verify_applied_filters_persist_on_job_mapping_ui() throws IOException {
		PageObjectManager.getInstance().getFilterPersistence().verify_applied_filters_persist_on_job_mapping_ui();
	}

	@Then("Click on Browser back button")
	public void click_on_browser_back_button() throws IOException {
		PageObjectManager.getInstance().getFilterPersistence().click_on_browser_back_button();
	}
	
	@Then("Verify View Published toggle button is Persisted")
	public void verify_view_published_toggle_button_is_persisted() throws IOException {
		PageObjectManager.getInstance().getFilterPersistence().verify_view_published_toggle_button_is_persisted();
	}
	
	@Then("Verify Applied Sorting persist on Job Mapping UI")
	public void verify_applied_sorting_persist_on_job_mapping_ui() throws IOException {
		PageObjectManager.getInstance().getFilterPersistence().verify_applied_sorting_persist_on_job_mapping_ui();
	}
	
	@When("User is in Job Mapping page with Grades filters applied")
	public void user_is_in_job_mapping_page_with_grades_filters_applied() throws IOException {
		PageObjectManager.getInstance().getFilterPersistence().user_is_in_job_mapping_page_with_grades_filters_applied();
	}
	
	@When("User is in Job Mapping page with Mapping Status filters applied")
	public void user_is_in_job_mapping_page_with_mapping_status_filters_applied() throws IOException {
		PageObjectManager.getInstance().getFilterPersistence().user_is_in_job_mapping_page_with_mapping_status_filters_applied();
	}
	
	@When("User is in View Published screen with Grades filters applied")
	public void user_is_in_view_published_screen_with_grades_filters_applied() throws IOException {
		PageObjectManager.getInstance().getFilterPersistence().user_is_in_view_published_screen_with_grades_filters_applied();
	}
	
	@When("User is in View Published screen with multiple filters applied")
	public void user_is_in_view_published_screen_with_multiple_filters_applied() throws IOException {
		PageObjectManager.getInstance().getFilterPersistence().user_is_in_view_published_screen_with_multiple_filters_applied();
	}
	
	@When("User is in Job Mapping page with multi-level sorting applied")
	public void user_is_in_job_mapping_page_with_multi_level_sorting_applied() throws IOException {
		PageObjectManager.getInstance().getFilterPersistence().user_is_in_job_mapping_page_with_multi_level_sorting_applied();
	}
	
	@When("User is in Job Mapping page with sorting and filters applied")
	public void user_is_in_job_mapping_page_with_sorting_and_filters_applied() throws IOException {
		PageObjectManager.getInstance().getFilterPersistence().user_is_in_job_mapping_page_with_sorting_and_filters_applied();
	}
	
	@When("User is in View Published screen with sorting applied")
	public void user_is_in_view_published_screen_with_sorting_applied() throws IOException {
		PageObjectManager.getInstance().getFilterPersistence().user_is_in_view_published_screen_with_sorting_applied();
	}

}

