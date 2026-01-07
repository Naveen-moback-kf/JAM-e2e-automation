package stepdefinitions.JobMapping;

import java.io.IOException;

import com.kfonetalentsuite.manager.PageObjectManager;

import io.cucumber.java.en.Then;

public class SD36_DeleteJobProfiles {	
	public SD36_DeleteJobProfiles() {
		super();
	}
	
	@Then("User should verify Delete button is disabled")
	public void user_should_verify_delete_button_is_disabled() throws IOException {
		PageObjectManager.getInstance().getDeleteJobProfiles().user_should_verify_delete_button_is_disabled();
	}
	
	@Then("User should verify Delete button is enabled")
	public void user_should_verify_delete_button_is_enabled() throws IOException {
		PageObjectManager.getInstance().getDeleteJobProfiles().user_should_verify_delete_button_is_enabled();
	}
	
	@Then("Click on Delete button in Job Mapping")
	public void click_on_delete_button_in_job_mapping() throws IOException {
		PageObjectManager.getInstance().getDeleteJobProfiles().click_on_delete_button_in_job_mapping();
	}
	
	@Then("Verify Delete Confirmation popup is displayed")
	public void verify_delete_confirmation_popup_is_displayed() throws IOException {
		PageObjectManager.getInstance().getDeleteJobProfiles().verify_delete_confirmation_popup_is_displayed();
	}
	
	@Then("Click on Cancel button on Delete Confirmation popup")
	public void click_on_cancel_button_on_delete_confirmation_popup() throws IOException {
		PageObjectManager.getInstance().getDeleteJobProfiles().click_on_cancel_button_on_delete_confirmation_popup();
	}
	
	@Then("Click on Delete button on Delete Confirmation popup")
	public void click_on_delete_button_on_delete_confirmation_popup() throws IOException {
		PageObjectManager.getInstance().getDeleteJobProfiles().click_on_delete_button_on_delete_confirmation_popup();
	}
	
	@Then("User should verify delete success popup appears on screen")
	public void user_should_verify_delete_success_popup_appears_on_screen() throws IOException {
		PageObjectManager.getInstance().getDeleteJobProfiles().user_should_verify_delete_success_popup_appears_on_screen();
	}

}

