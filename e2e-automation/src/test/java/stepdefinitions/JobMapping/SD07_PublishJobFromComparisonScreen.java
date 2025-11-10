package stepdefinitions.JobMapping;

import java.io.IOException;

import com.kfonetalentsuite.manager.PageObjectManager;

import io.cucumber.java.en.Then;

public class SD07_PublishJobFromComparisonScreen {
	
	PageObjectManager publishJobFromComparisonScreen = new PageObjectManager();
	
	public SD07_PublishJobFromComparisonScreen() {
		super();		
	}
	
	@Then("Verify user landed on job comparison screen")
	public void verify_user_landed_on_job_comparison_screen() throws IOException {
		publishJobFromComparisonScreen.getPublishJobFromComparisonScreen().verify_user_landed_on_job_comparison_screen();
	}
	
	@Then("Select second profile from DS Suggestions of Organization Job")
	public void select_second_profile_from_ds_suggestions_of_organization_job() throws IOException {
		publishJobFromComparisonScreen.getPublishJobFromComparisonScreen().select_second_profile_from_ds_suggestions_of_organization_job();
	}

	@Then("Click on Publish Selected button in Job Comparison page")
	public void click_on_publish_selected_button_in_job_comparison_page() throws IOException {
		publishJobFromComparisonScreen.getPublishJobFromComparisonScreen().click_on_publish_selected_button_in_job_comparison_page();
	}
}
