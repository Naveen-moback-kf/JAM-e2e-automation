package stepdefinitions.JobMapping;

import java.io.IOException;

import com.kfonetalentsuite.manager.PageObjectManager;

import io.cucumber.java.en.Then;

public class SD26_SelectAndSyncProfiles_PM {	
	public SD26_SelectAndSyncProfiles_PM() {
		super();		
	}
	
	@Then("Verify Profiles loaded after clicking Header checkbox are not selected in HCM Sync Profiles Screen")
	public void verify_profiles_loaded_after_clicking_header_checkbox_are_not_selected_in_HCM_Sync_Profiles_screen() throws IOException {
		PageObjectManager.getInstance().getSelectAndSyncProfiles_PM().verify_profiles_loaded_after_clicking_header_checkbox_are_not_selected_in_HCM_Sync_Profiles_screen();
	}
	
	@Then("Click on Chevron button beside header checkbox in HCM Sync Profiles screen")
	public void click_on_chevron_button_beside_header_checkbox_in_hcm_sync_profiles_screen() throws IOException {
		PageObjectManager.getInstance().getSelectAndSyncProfiles_PM().click_on_chevron_button_beside_header_checkbox_in_hcm_sync_profiles_screen();
	}
	
	@Then("Click on Select All button in HCM Sync Profiles screen")
	public void click_on_select_all_button_in_hcm_sync_profiles_screen() throws IOException {
		PageObjectManager.getInstance().getSelectAndSyncProfiles_PM().click_on_select_all_button_in_hcm_sync_profiles_screen();
	}
	
	@Then("Verify count of selected profiles by scrolling through all profiles")
	public void verify_count_of_selected_profiles_by_scrolling_through_all_profiles() throws IOException {
		PageObjectManager.getInstance().getSelectAndSyncProfiles_PM().verify_count_of_selected_profiles_by_scrolling_through_all_profiles();
	}
}


