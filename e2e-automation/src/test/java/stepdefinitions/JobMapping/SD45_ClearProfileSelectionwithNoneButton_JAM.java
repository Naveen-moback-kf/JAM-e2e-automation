package stepdefinitions.JobMapping;

import java.io.IOException;

import com.kfonetalentsuite.manager.PageObjectManager;

import io.cucumber.java.en.Then;

public class SD45_ClearProfileSelectionwithNoneButton_JAM {
	PageObjectManager clearProfileSelectionwithNoneButton_JAM = new PageObjectManager();
	
	public SD45_ClearProfileSelectionwithNoneButton_JAM() {
		super();		
	}
	
	@Then("Click on None button in Job Mapping screen")
	public void click_on_none_button_in_job_mapping_screen() throws IOException {
		clearProfileSelectionwithNoneButton_JAM.getClearProfileSelectionwithNoneButton_JAM().click_on_none_button_in_job_mapping_screen();
	}
	
	@Then("Verify all Loaded Profiles are Unselected in Job Mapping screen")
	public void verify_all_loaded_profiles_are_unselected_in_job_mapping_screen() throws IOException {
		clearProfileSelectionwithNoneButton_JAM.getClearProfileSelectionwithNoneButton_JAM().verify_all_loaded_profiles_are_unselected_in_job_mapping_screen();
	}

}

