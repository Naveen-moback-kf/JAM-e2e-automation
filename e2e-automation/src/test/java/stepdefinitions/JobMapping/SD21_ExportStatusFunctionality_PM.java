package stepdefinitions.JobMapping;

import java.io.IOException;

import com.kfonetalentsuite.manager.PageObjectManager;

import io.cucumber.java.en.Then;

public class SD21_ExportStatusFunctionality_PM {	
	public SD21_ExportStatusFunctionality_PM() {
		super();		
	}
	
	@Then("User should search for a Profile with Export Status as Not Exported")
	public void user_should_search_for_a_profile_with_export_status_as_not_exported() throws IOException {
		PageObjectManager.getInstance().getExportStatusFunctionality_PM().user_should_search_for_a_profile_with_export_status_as_not_exported();
	}

	@Then("Verify details of the Not Exported Success Profile in HCM Sync Profiles screen")
	public void verify_details_of_the_not_exported_success_profile_in_hcm_sync_profiles_tab() throws IOException {
		PageObjectManager.getInstance().getExportStatusFunctionality_PM().verify_details_of_the_not_exported_success_profile_in_hcm_sync_profiles_tab();
	}
	
	@Then("Verify Success Profile checkbox is enabled and able to perform export operation")
	public void verify_success_profile_checkbox_is_enabled_and_able_to_perform_export_operation() throws IOException {
		PageObjectManager.getInstance().getExportStatusFunctionality_PM().verify_success_profile_checkbox_is_enabled_and_able_to_perform_export_operation();
	}

	@Then("Click on checkbox of Success Profile with Export Status as Not Exported")
	public void click_on_checkbox_of_success_profile_with_export_status_as_not_exported() throws IOException {
		PageObjectManager.getInstance().getExportStatusFunctionality_PM().click_on_checkbox_of_success_profile_with_export_status_as_not_exported();
	}

	@Then("Refresh HCM Sync Profiles screen")
	public void refresh_hcm_sync_profiles_tab() throws IOException {
		PageObjectManager.getInstance().getExportStatusFunctionality_PM().refresh_hcm_sync_profiles_tab();
	}

	@Then("User should verify Export Status of SP updated as Exported")
	public void user_should_verify_export_status_of_sp_updated_as_exported() throws IOException {
		PageObjectManager.getInstance().getExportStatusFunctionality_PM().user_should_verify_export_status_of_sp_updated_as_exported();
	}
	
	@Then("User should click on Recently Exported Success Profile Job Name")
	public void user_should_click_on_recently_exported_success_profile_job_name() throws IOException {
		PageObjectManager.getInstance().getExportStatusFunctionality_PM().user_should_click_on_recently_exported_success_profile_job_name();
	}

	@Then("User should be navigated to SP details page")
	public void user_should_be_navigated_to_sp_details_page() throws IOException {
		PageObjectManager.getInstance().getExportStatusFunctionality_PM().user_should_be_navigated_to_sp_details_page();
	}
	
	@Then("Click on three dots in SP details page")
	public void click_on_three_dots_in_sp_details_page() throws IOException {
		PageObjectManager.getInstance().getExportStatusFunctionality_PM().click_on_three_dots_in_sp_details_page();
	}

	@Then("Click on Edit Success Profile option")
	public void click_on_edit_success_profile_option() throws IOException {
		PageObjectManager.getInstance().getExportStatusFunctionality_PM().click_on_edit_success_profile_option();
	}

	@Then("Click on Edit button of Details section")
	public void click_on_edit_button_of_details_section() throws IOException {
		PageObjectManager.getInstance().getExportStatusFunctionality_PM().click_on_edit_button_of_details_section();
	}

	@Then("Modify Function and SubFunction values of the Success Profile")
	public void modify_function_and_sub_function_values_of_the_success_profile() throws IOException {
		PageObjectManager.getInstance().getExportStatusFunctionality_PM().modify_function_and_sub_function_values_of_the_success_profile();
	}

	@Then("Click on Done button")
	public void click_on_done_button() throws IOException {
		PageObjectManager.getInstance().getExportStatusFunctionality_PM().click_on_done_button();
	}

	@Then("Click on Save button")
	public void click_on_save_button() throws IOException {
		PageObjectManager.getInstance().getExportStatusFunctionality_PM().click_on_save_button();
	}
	
	@Then("User should be navigated to SP details page after Saving SP details")
	public void user_should_be_navigated_to_sp_details_page_after_saving_sp_details() throws IOException {
		PageObjectManager.getInstance().getExportStatusFunctionality_PM().user_should_be_navigated_to_sp_details_page_after_saving_sp_details();
	}

	@Then("User should verify Recently Modified Success Profile is displaying on Top of the Job Proifles List")
	public void user_should_verify_recently_modified_success_profile_is_displaying_on_top_of_the_job_proifles_list() throws IOException {
		PageObjectManager.getInstance().getExportStatusFunctionality_PM().user_should_verify_recently_modified_success_profile_is_displaying_on_top_of_the_job_proifles_list();
	}

	@Then("User should verify Recently Exported and Modified Success Profile Export Status updated as Exported-Modfied")
	public void user_should_verify_recently_exported_and_modified_success_profile_export_status_updated_as_exported_modfied() throws IOException {
		PageObjectManager.getInstance().getExportStatusFunctionality_PM().user_should_verify_recently_exported_and_modified_success_profile_export_status_updated_as_exported_modfied();
	}

}

