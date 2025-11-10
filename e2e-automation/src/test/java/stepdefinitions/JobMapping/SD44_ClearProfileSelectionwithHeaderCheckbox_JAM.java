package stepdefinitions.JobMapping;

import java.io.IOException;

import com.kfonetalentsuite.manager.PageObjectManager;

import io.cucumber.java.en.Then;

public class SD44_ClearProfileSelectionwithHeaderCheckbox_JAM {
	PageObjectManager clearProfileSelectionwithHeaderCheckbox_JAM = new PageObjectManager();
	
	public SD44_ClearProfileSelectionwithHeaderCheckbox_JAM() {
		super();		
	}
	
	@Then("Click on header checkbox to Unselect loaded job profiles in Job Mapping screen")
	public void click_on_header_checkbox_to_unselect_loaded_job_profiles_in_job_mapping_screen() throws IOException {
		clearProfileSelectionwithHeaderCheckbox_JAM.getClearProfileSelectionwithHeaderCheckbox_JAM().click_on_header_checkbox_to_unselect_loaded_job_profiles_in_job_mapping_screen();
	}
	
	@Then("Verify Loaded Profiles are unselected in Job Mapping screen")
	public void verify_loaded_profiles_are_unselected_in_job_mapping_screen() throws IOException {
		clearProfileSelectionwithHeaderCheckbox_JAM.getClearProfileSelectionwithHeaderCheckbox_JAM().verify_loaded_profiles_are_unselected_in_job_mapping_screen();
	}
	
	@Then("Verify newly loaded profiles are still Selected in Job Mapping screen")
	public void verify_newly_loaded_profiles_are_still_selected_in_job_mapping_screen() throws IOException {
		clearProfileSelectionwithHeaderCheckbox_JAM.getClearProfileSelectionwithHeaderCheckbox_JAM().verify_newly_loaded_profiles_are_still_selected_in_job_mapping_screen();
	}

}

