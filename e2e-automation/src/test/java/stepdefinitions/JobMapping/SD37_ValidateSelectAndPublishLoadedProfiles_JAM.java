package stepdefinitions.JobMapping;

import java.io.IOException;

import com.kfonetalentsuite.manager.PageObjectManager;

import io.cucumber.java.en.Then;

public class SD37_ValidateSelectAndPublishLoadedProfiles_JAM {
	PageObjectManager validateSelectAndPublishLoadedProfiles_JAM = new PageObjectManager();

	public SD37_ValidateSelectAndPublishLoadedProfiles_JAM() {
		super();
	}

	@Then("Verify Profiles loaded after clicking Header checkbox are not selected in Job Mapping Screen")
	public void verify_profiles_loaded_after_clicking_header_checkbox_are_not_selected_in_job_mapping_screen() throws IOException {
		validateSelectAndPublishLoadedProfiles_JAM.getValidateSelectAndPublishLoadedProfiles_JAM().verify_profiles_loaded_after_clicking_header_checkbox_are_not_selected_in_job_mapping_screen();
	}
}

