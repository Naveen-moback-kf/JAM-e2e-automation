package stepdefinitions.JobMapping;

import java.io.IOException;

import com.kfonetalentsuite.manager.PageObjectManager;

import io.cucumber.java.en.Then;

public class SD33_ValidateSelectAndHCMSyncLoadedProfiles_PM {
	PageObjectManager validateSelectAndHCMSyncLoadedProfiles_PM = new PageObjectManager();
	
	public SD33_ValidateSelectAndHCMSyncLoadedProfiles_PM() {
		super();		
	}
	
	@Then("Verify Profiles loaded after clicking Header checkbox are not selected in HCM Sync Profiles Screen")
	public void verify_profiles_loaded_after_clicking_header_checkbox_are_not_selected_in_HCM_Sync_Profiles_screen() throws IOException {
		validateSelectAndHCMSyncLoadedProfiles_PM.getValidateSelectAndHCMSyncLoadedProfiles_PM().verify_profiles_loaded_after_clicking_header_checkbox_are_not_selected_in_HCM_Sync_Profiles_screen();
	}

}

