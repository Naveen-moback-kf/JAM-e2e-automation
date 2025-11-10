package stepdefinitions.JobMapping;

import java.io.IOException;

import com.kfonetalentsuite.manager.PageObjectManager;

import io.cucumber.java.en.Then;

public class SD42_ClearProfileSelectionwithHeaderCheckbox_PM {
	PageObjectManager clearProfileSelectionwithHeaderCheckbox_PM = new PageObjectManager();
	
	public SD42_ClearProfileSelectionwithHeaderCheckbox_PM() {
		super();		
	}
	
	@Then("Click on header checkbox to Unselect loaded job profiles in HCM Sync Profiles screen")
	public void click_on_header_checkbox_to_unselect_loaded_job_profiles_in_hcm_sync_profiles_screen() throws IOException {
		clearProfileSelectionwithHeaderCheckbox_PM.getClearProfileSelectionwithHeaderCheckbox_PM().click_on_header_checkbox_to_unselect_loaded_job_profiles_in_hcm_sync_profiles_screen();
	}
	
	@Then("Verify Loaded Profiles are unselected in HCM Sync Profiles screen")
	public void verify_loaded_profiles_are_unselected_in_hcm_sync_profiles_screen() throws IOException {
		clearProfileSelectionwithHeaderCheckbox_PM.getClearProfileSelectionwithHeaderCheckbox_PM().verify_loaded_profiles_are_unselected_in_hcm_sync_profiles_screen();
	}
	
	@Then("Verify newly loaded profiles are still Selected in HCM Sync Profiles screen")
	public void verify_newly_loaded_profiles_are_still_selected_in_hcm_sync_profiles_screen() throws IOException {
		clearProfileSelectionwithHeaderCheckbox_PM.getClearProfileSelectionwithHeaderCheckbox_PM().verify_newly_loaded_profiles_are_still_selected_in_hcm_sync_profiles_screen();
	}

}

