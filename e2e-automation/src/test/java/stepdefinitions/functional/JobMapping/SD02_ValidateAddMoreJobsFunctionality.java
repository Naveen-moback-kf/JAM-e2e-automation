package stepdefinitions.functional.JobMapping;

import java.io.IOException;

import com.JobMapping.manager.PageObjectManager;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SD02_ValidateAddMoreJobsFunctionality {
	PageObjectManager validateAddMoreJobsFunctionality = new PageObjectManager();
	
	public SD02_ValidateAddMoreJobsFunctionality() {
		super();		
	}
	
	@Then("Verify Unpublished Jobs count before adding more jobs")
	public void verify_unpublished_jobs_count_before_adding_more_jobs() throws IOException {
		validateAddMoreJobsFunctionality.getValidateAddMoreJobsFunctionality().verify_unpublished_jobs_count_before_adding_more_jobs();
	}
	
	@Then("User should be landed on KFONE Add Job Data page")
	public void user_should_be_landed_on_kfone_add_job_data_page() throws IOException {
		validateAddMoreJobsFunctionality.getValidateAddMoreJobsFunctionality().user_should_be_landed_on_kfone_add_job_data_page();
	}

	@When("User is in KFONE Add Job Data page")
	public void user_is_in_kfone_add_job_data_page() throws IOException {
		validateAddMoreJobsFunctionality.getValidateAddMoreJobsFunctionality().user_is_in_kfone_add_job_data_page();
	}
	
	@Then("User should click on Manual Upload button")
	public void user_should_click_on_manual_upload_button() throws IOException {
		validateAddMoreJobsFunctionality.getValidateAddMoreJobsFunctionality().user_should_click_on_manual_upload_button();
	}
	
	@Then("Verify Jobs count in KFONE Add Job Data screen before adding more jobs")
	public void verify_jobs_count_in_kfone_add_job_data_screen_before_adding_more_jobs() throws IOException {
		validateAddMoreJobsFunctionality.getValidateAddMoreJobsFunctionality().verify_jobs_count_in_kfone_add_job_data_screen_before_adding_more_jobs();
	}

	
	@Then("Verify Last Synced Info on Add Job Data screen before adding more jobs")
	public void verify_last_synced_info_on_add_job_data_screen_before_adding_more_jobs() throws IOException {
		validateAddMoreJobsFunctionality.getValidateAddMoreJobsFunctionality().verify_last_synced_info_on_add_job_data_screen_before_adding_more_jobs();
	}

	@Then("Upload Job Catalog file using Browse Files button")
	public void upload_job_catalog_file_using_browse_files_button() throws IOException {
		validateAddMoreJobsFunctionality.getValidateAddMoreJobsFunctionality().upload_job_catalog_file_using_browse_files_button();
	}

	@Then("User should verify File Close button displaying and clickable")
	public void user_should_verify_file_close_button_displaying_and_clickable() throws IOException {
		validateAddMoreJobsFunctionality.getValidateAddMoreJobsFunctionality().user_should_verify_file_close_button_displaying_and_clickable();
	}

	@Then("Click on Continue button in Add Job data screen")
	public void click_on_continue_button_in_add_job_data_screen() throws IOException {
		validateAddMoreJobsFunctionality.getValidateAddMoreJobsFunctionality().click_on_continue_button_in_add_job_data_screen();
	}
	
	@Then("User should Validate Job Data Upload is in Progress")
	public void user_should_validate_job_data_upload_is_in_progress() throws IOException {
		validateAddMoreJobsFunctionality.getValidateAddMoreJobsFunctionality().user_should_validate_job_data_upload_is_in_progress();
	}
	
	@Then("User should validate Job Data added successfully")
	public void user_should_validate_job_data_added_successfully() throws IOException {
		validateAddMoreJobsFunctionality.getValidateAddMoreJobsFunctionality().user_should_validate_job_data_added_successfully();
	}
	
	@Then("Verify Jobs count in KFONE Add Job Data screen after adding more jobs")
	public void verify_jobs_count_in_kfone_add_job_data_screen_after_adding_more_jobs() throws IOException {
		validateAddMoreJobsFunctionality.getValidateAddMoreJobsFunctionality().verify_jobs_count_in_kfone_add_job_data_screen_after_adding_more_jobs();
	}
	
	@Then("Verify Last Synced Info on Add Job Data screen after adding more jobs")
	public void verify_last_synced_info_on_add_job_data_screen_after_adding_more_jobs() throws IOException {
		validateAddMoreJobsFunctionality.getValidateAddMoreJobsFunctionality().verify_last_synced_info_on_add_job_data_screen_after_adding_more_jobs();
	}
	
	@Then("Close Add Job Data screen")
	public void close_add_job_data_screen() throws IOException {
		validateAddMoreJobsFunctionality.getValidateAddMoreJobsFunctionality().close_add_job_data_screen();
	}
	
	@Then("Verify Unpublished Jobs count after adding more jobs")
	public void verify_unpublished_jobs_count_after_adding_more_jobs() throws IOException {
		validateAddMoreJobsFunctionality.getValidateAddMoreJobsFunctionality().verify_unpublished_jobs_count_after_adding_more_jobs();
	}
	
	@Then("Click on Done button in KFONE Add Job Data page")
	public void click_on_done_button_in_kfone_add_job_data_page() throws IOException {
		validateAddMoreJobsFunctionality.getValidateAddMoreJobsFunctionality().click_on_done_button_in_kfone_add_job_data_page();
	}
	
	@Then("User is in KFONE Add Job Data page after uploading file")
	public void user_is_in_kfone_add_job_data_page_afer_uploading_file() throws IOException {
		validateAddMoreJobsFunctionality.getValidateAddMoreJobsFunctionality().user_is_in_kfone_add_job_data_page_afer_uploading_file();
	}



}
