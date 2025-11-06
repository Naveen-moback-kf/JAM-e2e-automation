package stepdefinitions.functional.JobMapping;

import java.io.IOException;

import com.JobMapping.manager.PageObjectManager;

import io.cucumber.java.en.Then;

public class SD23_VerifyProfileswithNoJobCode_PM {
	PageObjectManager verifyProfileswithNoJobCode_PM = new PageObjectManager();
	
	public SD23_VerifyProfileswithNoJobCode_PM() {
		super();		
	}
	
	@Then("User should search for Success Profile with No Job Code assigned")
	public void user_should_search_for_success_profile_with_no_job_code_assigned() throws IOException {
		verifyProfileswithNoJobCode_PM.getVerifyProfileswithNoJobCode_PM().user_should_search_for_success_profile_with_no_job_code_assigned();
	}
	
	@Then("User should verify Tooltip is displaying on checkbox of Success Profile with No Job Code")
	public void user_should_verify_tooltip_is_displaying_on_checkbox_of_success_profile_with_no_job_code() throws IOException {
		verifyProfileswithNoJobCode_PM.getVerifyProfileswithNoJobCode_PM().user_should_verify_tooltip_is_displaying_on_checkbox_of_success_profile_with_no_job_code();
	}
	
	@Then("Verify details of the Success Profile with No Job Code assigned in HCM Sync Profiles screen")
	public void verify_details_of_the_success_profile_with_no_job_code_assigned_in_hcm_sync_profiles_tab() throws IOException {
		verifyProfileswithNoJobCode_PM.getVerifyProfileswithNoJobCode_PM().verify_details_of_the_success_profile_with_no_job_code_assigned_in_hcm_sync_profiles_tab();
	}

}
