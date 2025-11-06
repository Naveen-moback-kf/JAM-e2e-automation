package stepdefinitions.functional.JobMapping;

import java.io.IOException;

import com.JobMapping.manager.PageObjectManager;

import io.cucumber.java.en.Then;

public class SD05_ValidateJobProfileDetailsPopup {
	PageObjectManager validateJobProfileDetailsPopup = new PageObjectManager();
	
	public SD05_ValidateJobProfileDetailsPopup() {
		super();		
	}

	
	@Then("Verify profile header matches with matched profile name")
	public void verify_profile_header_matches_with_matched_profile_name() throws IOException {
		validateJobProfileDetailsPopup.getValidateJobProfileDetailsPopup().verify_profile_header_matches_with_matched_profile_name();
	}

	@Then("Verify profile details displaying on the popup")
	public void verify_profile_details_displaying_on_the_popup() throws IOException {
		validateJobProfileDetailsPopup.getValidateJobProfileDetailsPopup().verify_profile_details_displaying_on_the_popup();
	}

	@Then("User should verify Profile Level dropdown is available and Validate levels present inside dropdown")
	public void user_should_verify_profile_level_dropdown_is_available_and_validate_levels_present_inside_dropdown() throws IOException {
		validateJobProfileDetailsPopup.getValidateJobProfileDetailsPopup().user_should_verify_profile_level_dropdown_is_available_and_validate_levels_present_inside_dropdown();
	}

	@Then("Validate Role Summary is displaying")
	public void validate_role_summary_is_displaying() throws IOException {
		validateJobProfileDetailsPopup.getValidateJobProfileDetailsPopup().validate_role_summary_is_displaying();
	}

	@Then("Validate data in RESPONSIBILITIES screen")
	public void validate_data_in_responsibilities_tab() throws IOException {
		validateJobProfileDetailsPopup.getValidateJobProfileDetailsPopup().validate_data_in_responsibilities_tab();
	}

	@Then("Validate data in BEHAVIOURAL COMPETENCIES screen")
	public void validate_data_in_behavioural_competencies_tab() throws IOException {
		validateJobProfileDetailsPopup.getValidateJobProfileDetailsPopup().validate_data_in_behavioural_competencies_tab();
	}

	@Then("Validate data in SKILLS screen")
	public void validate_data_in_skills_tab() throws IOException {
		validateJobProfileDetailsPopup.getValidateJobProfileDetailsPopup().validate_data_in_skills_tab();
	}

	@Then("User should verify publish profile button is available on popup screen")
	public void user_should_verify_publish_profile_button_is_available_on_popup_screen() throws IOException {
		validateJobProfileDetailsPopup.getValidateJobProfileDetailsPopup().user_should_verify_publish_profile_button_is_available_on_popup_screen();
	}


}
