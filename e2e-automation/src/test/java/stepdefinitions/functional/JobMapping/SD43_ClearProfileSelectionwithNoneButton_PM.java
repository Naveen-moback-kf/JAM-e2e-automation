package stepdefinitions.functional.JobMapping;

import java.io.IOException;

import com.JobMapping.manager.PageObjectManager;

import io.cucumber.java.en.Then;

public class SD43_ClearProfileSelectionwithNoneButton_PM {
	PageObjectManager clearProfileSelectionwithNoneButton_PM = new PageObjectManager();
	
	public SD43_ClearProfileSelectionwithNoneButton_PM() {
		super();		
	}
	
	@Then("Click on None button in HCM Sync Profiles screen")
	public void click_on_none_button_in_hcm_sync_profiles_screen() throws IOException {
		clearProfileSelectionwithNoneButton_PM.getClearProfileSelectionwithNoneButton_PM().click_on_none_button_in_hcm_sync_profiles_screen();
	}
	
	@Then("Verify all Loaded Profiles are Unselected in HCM Sync Profiles screen")
	public void verify_all_loaded_profiles_are_unselected_in_hcm_sync_profiles_screen() throws IOException {
		clearProfileSelectionwithNoneButton_PM.getClearProfileSelectionwithNoneButton_PM().verify_all_loaded_profiles_are_unselected_in_hcm_sync_profiles_screen();
	}

}

