package stepdefinitions.JobMapping;

import java.io.IOException;

import com.kfonetalentsuite.manager.PageObjectManager;

import io.cucumber.java.en.Then;

public class SD03_JobMappingHeaderSection {	
	public SD03_JobMappingHeaderSection() {
		super();		
	}
	
	@Then("Verify KF Talent Suite logo is displaying")
	public void verify_kf_talent_suite_logo_is_displaying() throws IOException {
		PageObjectManager.getInstance().getJobMappingHeaderSection().verify_kf_talent_suite_logo_is_displaying();
	}
	
	@Then("Click on KF Talent Suite logo")
	public void click_on_kf_talent_suite_logo() throws IOException {
		PageObjectManager.getInstance().getJobMappingHeaderSection().click_on_kf_talent_suite_logo();
	}
	
	@Then("Navigate to Job Mapping page from KFONE Global Menu")
	public void navigate_to_job_mapping_page_from_kfone_global_menu() throws IOException {
		PageObjectManager.getInstance().getJobMappingHeaderSection().navigate_to_job_mapping_page_from_kfone_global_menu();
	}

	@Then("User should verify client name is correctly displaying")
	public void user_should_verify_client_name_is_correctly_displaying() throws IOException {
		PageObjectManager.getInstance().getJobMappingHeaderSection().user_should_verify_client_name_is_correctly_displaying();
	}
	
	@Then("Click on client name in Job Mapping UI header")
	public void click_on_client_name_in_job_mapping_ui_header() throws IOException {
		PageObjectManager.getInstance().getJobMappingHeaderSection().click_on_client_name_in_job_mapping_ui_header();
	}
	
	@Then("Verify user navigated to KFONE Clients Page")
	public void verify_user_navigated_to_kfone_clients_page() throws IOException {
		PageObjectManager.getInstance().getJobMappingHeaderSection().verify_user_navigated_to_kfone_clients_page();
	}
	
	@Then("Verify User Profile logo is displaying and clickable")
	public void verify_user_profile_logo_is_displaying_and_clickable() throws IOException {
		PageObjectManager.getInstance().getJobMappingHeaderSection().verify_user_profile_logo_is_displaying_and_clickable();
	}

	@Then("Verify User Profile Menu is opened")
	public void verify_user_profile_menu_is_opened() throws IOException {
		PageObjectManager.getInstance().getJobMappingHeaderSection().verify_user_profile_menu_is_opened();
	}
	
	@Then("Verify User name is displayed in Profile Menu")
	public void verify_user_name_is_displayed_in_profile_menu() throws IOException {
		PageObjectManager.getInstance().getJobMappingHeaderSection().verify_user_name_is_displayed_in_profile_menu();
	}
	
	@Then("Verify User email is displayed in Profile Menu")
	public void verify_user_email_is_displayed_in_profile_menu() throws IOException {
		PageObjectManager.getInstance().getJobMappingHeaderSection().verify_user_email_is_displayed_in_profile_menu();
	}

	@Then("Click on Sing Out button in User Profile Menu")
	public void click_on_signout_button_in_user_profile_menu() throws IOException {
		PageObjectManager.getInstance().getJobMappingHeaderSection().click_on_signout_button_in_user_profile_menu();
	}

	@Then("User should be signed out from the Application")
	public void user_should_be_signed_out_from_the_application() throws IOException {
		PageObjectManager.getInstance().getJobMappingHeaderSection().user_should_be_signed_out_from_the_application();
	}

}

