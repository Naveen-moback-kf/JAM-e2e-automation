package stepdefinitions.JobMapping;

import java.io.IOException;

import com.kfonetalentsuite.manager.PageObjectManager;

import io.cucumber.java.en.Then;

public class SD29_SelectAndPublishLoadedProfiles_JAM {
	public SD29_SelectAndPublishLoadedProfiles_JAM() {
		super();
	}

	@Then("Verify Profiles loaded after clicking Header checkbox are not selected in Job Mapping Screen")
	public void verify_profiles_loaded_after_clicking_header_checkbox_are_not_selected_in_job_mapping_screen() throws IOException {
		PageObjectManager.getInstance().getSelectAndPublishLoadedProfiles_JAM().verify_profiles_loaded_after_clicking_header_checkbox_are_not_selected_in_job_mapping_screen();
	}
}


