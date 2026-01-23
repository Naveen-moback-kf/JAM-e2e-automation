package stepdefinitions.JobMapping;

import java.io.IOException;

import com.kfonetalentsuite.manager.PageObjectManager;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SD12_RecommendedProfileDetails {	
	public SD12_RecommendedProfileDetails() {
		super();		
	}
	
	@Then("Search for Job Profile with View Other Matches button")
	public void search_for_job_profile_with_view_other_matches_button() throws IOException {
		PageObjectManager.getInstance().getRecommendedProfileDetails().search_for_job_profile_with_view_other_matches_button();
	}
	
	@Then("User should verify Organization Job Name and Job Code Values of Job Profile with View Other Matches button")
	public void user_should_verify_organization_job_name_and_job_code_values_of_job_profile_with_view_other_matches_button() throws IOException {
		PageObjectManager.getInstance().getRecommendedProfileDetails().user_should_verify_organization_job_name_and_job_code_values_of_job_profile_with_view_other_matches_button();
	}
	
	@Then("User should verify Organization Job Grade and Department values of Job Profile with View Other Matches button")
	public void user_should_verify_organization_job_grade_and_department_values_of_job_profile_with_view_other_matches_button() throws IOException {
		PageObjectManager.getInstance().getRecommendedProfileDetails().user_should_verify_organization_job_grade_and_department_values_of_job_profile_with_view_other_matches_button();
	}

	@Then("User should verify Organization Job Function or Sub-function of Job Profile with View Other Matches button")
	public void user_should_verify_organization_job_function_or_sub_function_of_job_profile_with_view_other_matches_button() throws IOException {
		PageObjectManager.getInstance().getRecommendedProfileDetails().user_should_verify_organization_job_function_or_sub_function_of_job_profile_with_view_other_matches_button();
	}
	
	@Then("Click on matched profile of Job Profile with View Other Matches button")
	public void click_on_matched_profile_of_job_profile_with_view_other_matches_button() throws IOException {
		PageObjectManager.getInstance().getRecommendedProfileDetails().click_on_matched_profile_of_job_profile_with_view_other_matches_button();
	}
	
	@Then("Verify user navigated to job comparison page")
	public void verify_user_navigated_to_job_comparison_page() throws IOException {
		PageObjectManager.getInstance().getRecommendedProfileDetails().verify_user_navigated_to_job_comparison_page();
	}
	
	@When("User is in Job Comparison Page")
	public void user_is_in_job_comparison_page() throws IOException {
		PageObjectManager.getInstance().getRecommendedProfileDetails().user_is_in_job_comparison_page();
	}

	@Then("Validate organization job name and code in job comparison page")
	public void validate_organization_job_name_and_code() throws IOException {
		PageObjectManager.getInstance().getRecommendedProfileDetails().validate_organization_job_name_and_code_in_job_comparison_page();
	}
	
	@Then("User should validate organization job grade department and function or subfunction in job comparison page")
	public void user_should_validate_organization_job_grade_department_and_function_or_subfunction_in_job_comparison_page() throws IOException {
		PageObjectManager.getInstance().getRecommendedProfileDetails().user_should_validate_organization_job_grade_department_and_function_or_subfunction_in_job_comparison_page();
	}
	
	@Then("User should verify Recommended Profile Name matches with Matched Success Profile Name")
	public void user_should_verify_recommended_profile_name_matches_with_matched_success_profile_name() throws IOException {
		PageObjectManager.getInstance().getRecommendedProfileDetails().user_should_verify_recommended_profile_name_matches_with_matched_success_profile_name();
	}

	@Then("User should verify Recommended tag and Select button is displaying on the profile")
	public void user_should_verify_recommended_tag_and_select_button_is_displaying_on_the_profile() throws IOException {
		PageObjectManager.getInstance().getRecommendedProfileDetails().user_should_verify_recommended_tag_and_select_button_is_displaying_on_the_profile();
	}

	@Then("Validate Recommended Profile Grade matches with Matched Success Profile Grade")
	public void validate_recommended_profile_grade_matches_with_matched_success_profile_grade() throws IOException {
		PageObjectManager.getInstance().getRecommendedProfileDetails().validate_recommended_profile_grade_matches_with_matched_success_profile_grade();
	}

	@Then("Validate Recommended Profile Level Sublevels matches with Matched Success Profile Level Sublevels")
	public void validate_recommended_profile_level_sublevels_matches_with_matched_success_profile_level_sublevels() throws IOException {
		PageObjectManager.getInstance().getRecommendedProfileDetails().validate_recommended_profile_level_sublevels_matches_with_matched_success_profile_level_sublevels();
	}

	@Then("Validate Recommended Profile Function Subfunction matches with Matched Success Profile Function Subfunction")
	public void validate_recommended_profile_function_subfunction_matches_with_matched_success_profile_function_subfunction() throws IOException {
		PageObjectManager.getInstance().getRecommendedProfileDetails().validate_recommended_profile_function_subfunction_matches_with_matched_success_profile_function_subfunction();
	}

	@Then("Validate Recommended Profile Seniority level matches with Matched Success Profile Seniority level")
	public void validate_recommended_profile_seniority_level_matches_with_matched_success_profile_seniority_level() throws IOException {
		PageObjectManager.getInstance().getRecommendedProfileDetails().validate_recommended_profile_seniority_level_matches_with_matched_success_profile_seniority_level();
	}

	@Then("Validate Recommended Profile Managerial Experience matches with Matched Success Profile Managerial Experience")
	public void validate_recommended_profile_managerial_experience_matches_with_matched_success_profile_managerial_experience() throws IOException {
		PageObjectManager.getInstance().getRecommendedProfileDetails().validate_recommended_profile_managerial_experience_matches_with_matched_success_profile_managerial_experience();
	}

	@Then("Validate Recommended Profile Education matches with Matched Success Profile Education")
	public void validate_recommended_profile_education_matches_with_matched_success_profile_education() throws IOException {
		PageObjectManager.getInstance().getRecommendedProfileDetails().validate_recommended_profile_education_matches_with_matched_success_profile_education();
	}

	@Then("Validate Recommended Profile General Experience matches with Matched Success Profile General Experience")
	public void validate_recommended_profile_general_experience_matches_with_matched_success_profile_general_experience() throws IOException {
		PageObjectManager.getInstance().getRecommendedProfileDetails().validate_recommended_profile_general_experience_matches_with_matched_success_profile_general_experience();
	}

	@Then("Validate Recommended Profile Role Summary matches with Matched Success Profile Role Summary")
	public void validate_recommended_profile_role_summary_matches_with_matched_success_profile_role_summary() throws IOException {
		PageObjectManager.getInstance().getRecommendedProfileDetails().validate_recommended_profile_role_summary_matches_with_matched_success_profile_role_summary();
	}

	@Then("Validate Recommended Profile Responsibilities matches with Matched Success Profile Responsibilities")
	public void validate_recommended_profile_responsibilities_matches_with_matched_success_profile_responsibilities() throws IOException {
		PageObjectManager.getInstance().getRecommendedProfileDetails().validate_recommended_profile_responsibilities_matches_with_matched_success_profile_responsibilities();
	}

	@Then("Validate Recommended Profile Behavioural competencies matches with Matched Success Profile Behavioural competencies")
	public void validate_recommended_profile_behavioural_competencies_matches_with_matched_success_profile_behavioural_competencies() throws IOException {
		PageObjectManager.getInstance().getRecommendedProfileDetails().validate_recommended_profile_behavioural_competencies_matches_with_matched_success_profile_behavioural_competencies();
	}

	@Then("Validate Recommended Profile Skills matches with Matched Success Profile Skills")
	public void validate_recommended_profile_skills_matches_with_matched_success_profile_skills() throws IOException {
		PageObjectManager.getInstance().getRecommendedProfileDetails().validate_recommended_profile_skills_matches_with_matched_success_profile_skills();
	}

	// ============================================================
	// Methods moved from SD05_ValidateJobProfileDetailsPopup
	// Now calling PO15 methods directly (methods moved from PO05)
	// ============================================================

	@Then("Verify profile header matches with matched profile name")
	public void verify_profile_header_matches_with_matched_profile_name() throws IOException {
		PageObjectManager.getInstance().getRecommendedProfileDetails().verify_profile_header_matches_with_matched_profile_name();
	}

	@Then("Verify profile details displaying on the popup")
	public void verify_profile_details_displaying_on_the_popup() throws IOException {
		PageObjectManager.getInstance().getRecommendedProfileDetails().verify_profile_details_displaying_on_the_popup();
	}

	@Then("User should verify Profile Level dropdown is available and Validate levels present inside dropdown")
	public void user_should_verify_profile_level_dropdown_is_available_and_validate_levels_present_inside_dropdown() throws IOException {
		PageObjectManager.getInstance().getRecommendedProfileDetails().user_should_verify_profile_level_dropdown_is_available_and_validate_levels_present_inside_dropdown();
	}

	@Then("Validate Role Summary is displaying")
	public void validate_role_summary_is_displaying() throws IOException {
		PageObjectManager.getInstance().getRecommendedProfileDetails().validate_role_summary_is_displaying();
	}

	@Then("Validate data in RESPONSIBILITIES screen")
	public void validate_data_in_responsibilities_tab() throws IOException {
		PageObjectManager.getInstance().getRecommendedProfileDetails().validate_data_in_responsibilities_tab();
	}

	@Then("Validate data in BEHAVIOURAL COMPETENCIES screen")
	public void validate_data_in_behavioural_competencies_tab() throws IOException {
		PageObjectManager.getInstance().getRecommendedProfileDetails().validate_data_in_behavioural_competencies_tab();
	}

	@Then("Validate data in SKILLS screen")
	public void validate_data_in_skills_tab() throws IOException {
		PageObjectManager.getInstance().getRecommendedProfileDetails().validate_data_in_skills_tab();
	}

	@Then("User should verify publish profile button is available on popup screen")
	public void user_should_verify_publish_profile_button_is_available_on_popup_screen() throws IOException {
		PageObjectManager.getInstance().getRecommendedProfileDetails().user_should_verify_publish_profile_button_is_available_on_popup_screen();
	}

}

