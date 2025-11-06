package stepdefinitions.functional.JobMapping;

import java.io.IOException;

import com.JobMapping.manager.PageObjectManager;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SD14_ValidateProfileLevelFunctionality {
	PageObjectManager validateProfileLevelFunctionality = new PageObjectManager();
	
	public SD14_ValidateProfileLevelFunctionality() {
		super();		
	}
	
	@Then("Change Profile Level")
	public void change_profile_level() throws IOException {
		validateProfileLevelFunctionality.getValidateProfileLevelFunctionality().change_profile_level();
	}
	
	@Then("Change Profile Level in Job Comparison Page")
	public void change_profile_level_in_job_comparison_page() throws IOException {
		validateProfileLevelFunctionality.getValidateProfileLevelFunctionality().change_profile_level_in_job_comparison_page();
	}
	
	@Then("User should verify profile header matches with changed profile level in Job profile details popup")
	public void user_should_verify_profile_header_matches_with_changed_profile_level_in_job_profile_details_popup() throws IOException {
		validateProfileLevelFunctionality.getValidateProfileLevelFunctionality().user_should_verify_profile_header_matches_with_changed_profile_level_in_job_profile_details_popup();
	}
	
	@Then("User should verify Recommended Profile Name matches with changed profile level")
	public void user_should_verify_recommended_profile_name_matches_with_changed_profile_level() throws IOException {
		validateProfileLevelFunctionality.getValidateProfileLevelFunctionality(). user_should_verify_recommended_profile_name_matches_with_changed_profile_level();
	}
	
	@When("User is in Job Comparison Page after changing profile level")
	public void user_is_in_job_comparison_page_after_changing_profile_level() throws IOException {
		validateProfileLevelFunctionality.getValidateProfileLevelFunctionality().user_is_in_job_comparison_page_after_changing_profile_level();
	}

}
