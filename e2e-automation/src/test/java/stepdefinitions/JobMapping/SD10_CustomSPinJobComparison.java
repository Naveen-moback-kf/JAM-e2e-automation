package stepdefinitions.JobMapping;

import java.io.IOException;

import com.kfonetalentsuite.manager.PageObjectManager;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SD10_CustomSPinJobComparison {
	PageObjectManager addandVerifyCustomSPinJobComparisonPage = new PageObjectManager();
	
	public SD10_CustomSPinJobComparison() {
		super();		
	}
	
	@Then("Click on Search bar in Job Comparison Page")
	public void click_on_search_bar_in_job_comparison_page() throws IOException {
		addandVerifyCustomSPinJobComparisonPage.getCustomSPinJobComparison().click_on_search_bar_in_job_comparison_page();
	}

	@Then("Verify Search bar Placeholder text in Job Comparison Page")
	public void verify_search_bar_placeholder_text_in_job_comparison_page() throws IOException {
		addandVerifyCustomSPinJobComparisonPage.getCustomSPinJobComparison().verify_search_bar_placeholder_text_in_job_comparison_page();
	}

	@Then("User should enter Custom SP Search String in the search bar")
	public void user_should_enter_custom_sp_search_string_in_the_search_bar() throws IOException {
		addandVerifyCustomSPinJobComparisonPage.getCustomSPinJobComparison().user_should_enter_custom_sp_search_string_in_the_search_bar();
	}

	@Then("Select first Custom SP from search results")
	public void select_first_custom_sp_from_search_results() throws IOException {
		addandVerifyCustomSPinJobComparisonPage.getCustomSPinJobComparison().select_first_custom_sp_from_search_results();
	}

	@Then("Verify Custom SP added to Profiles List in Job Comparison page")
	public void verify_custom_sp_added_to_profiles_list_in_job_comparison_page() throws IOException {
		addandVerifyCustomSPinJobComparisonPage.getCustomSPinJobComparison().verify_custom_sp_added_to_profiles_list_in_job_comparison_page();
	}

	@Then("User should verify Custom SP Name and close button are displaying in search bar after adding Custom SP")
	public void user_should_verify_custom_sp_name_and_close_button_are_displaying_in_search_bar_after_adding_custom_sp() throws IOException {
		addandVerifyCustomSPinJobComparisonPage.getCustomSPinJobComparison().user_should_verify_custom_sp_name_and_close_button_are_displaying_in_search_bar_after_adding_custom_sp();
	}

	@When("User added Custom SP to Profiles List in Job Comparison page")
	public void user_added_custom_sp_to_profiles_list_in_job_comparison_page() throws IOException {
		addandVerifyCustomSPinJobComparisonPage.getCustomSPinJobComparison().user_added_custom_sp_to_profiles_list_in_job_comparison_page();
	}

	@Then("Validate Custom SP Profile Name matches with Selected Custom SP Name from Search Results")
	public void validate_custom_sp_profile_name_matches_with_selected_custom_sp_name_from_search_results() throws IOException {
		addandVerifyCustomSPinJobComparisonPage.getCustomSPinJobComparison().validate_custom_sp_profile_name_matches_with_selected_custom_sp_name_from_search_results();
	}

	@Then("User should verify Close button and Select button are displaying on the profile")
	public void user_should_verify_close_button_and_select_button_are_displaying_on_the_profile() throws IOException {
		addandVerifyCustomSPinJobComparisonPage.getCustomSPinJobComparison().user_should_verify_close_button_and_select_button_are_displaying_on_the_profile();
	}

	@Then("Verify Custom SP Profile Grade")
	public void verify_custom_sp_profile_grade() throws IOException {
		addandVerifyCustomSPinJobComparisonPage.getCustomSPinJobComparison().verify_custom_sp_profile_grade();
	}

	@Then("Verify Custom SP Profile Level Sublevels")
	public void verify_custom_sp_profile_level_sublevels() throws IOException {
		addandVerifyCustomSPinJobComparisonPage.getCustomSPinJobComparison().verify_custom_sp_profile_level_sublevels();
	}

	@Then("Verify Custom SP Profile Function Subfunction")
	public void verify_custom_sp_profile_function_subfunction() throws IOException {
		addandVerifyCustomSPinJobComparisonPage.getCustomSPinJobComparison().verify_custom_sp_profile_function_subfunction();
	}

	@Then("Verify Custom SP Profile Seniority level")
	public void verify_custom_sp_profile_seniority_level() throws IOException {
		addandVerifyCustomSPinJobComparisonPage.getCustomSPinJobComparison().verify_custom_sp_profile_seniority_level();
	}

	@Then("Verify Custom SP Profile Managerial Experience")
	public void verify_custom_sp_profile_managerial_experience() throws IOException {
		addandVerifyCustomSPinJobComparisonPage.getCustomSPinJobComparison().verify_custom_sp_profile_managerial_experience();
	}

	@Then("Verify Custom SP Profile Education")
	public void verify_custom_sp_profile_education() throws IOException {
		addandVerifyCustomSPinJobComparisonPage.getCustomSPinJobComparison().verify_custom_sp_profile_education();
	}

	@Then("Verify Custom SP Profile General Experience")
	public void verify_custom_sp_profile_general_experience() throws IOException {
		addandVerifyCustomSPinJobComparisonPage.getCustomSPinJobComparison().verify_custom_sp_profile_general_experience();
	}

	@Then("Verify Custom SP Profile Role Summary")
	public void verify_custom_sp_profile_role_summary() throws IOException {
		addandVerifyCustomSPinJobComparisonPage.getCustomSPinJobComparison().verify_custom_sp_profile_role_summary();
	}

	@Then("Verify Custom SP Profile Responsibilities")
	public void verify_custom_sp_profile_responsibilities() throws IOException {
		addandVerifyCustomSPinJobComparisonPage.getCustomSPinJobComparison().verify_custom_sp_profile_responsibilities();
	}

	@Then("Verify Custom SP Profile Behavioural competencies")
	public void verify_custom_sp_profile_behavioural_competencies() throws IOException {
		addandVerifyCustomSPinJobComparisonPage.getCustomSPinJobComparison().verify_custom_sp_profile_behavioural_competencies();
	}

	@Then("Verify Custom SP Profile Skills")
	public void verify_custom_sp_profile_skills() throws IOException {
		addandVerifyCustomSPinJobComparisonPage.getCustomSPinJobComparison().verify_custom_sp_profile_skills();
	}

	@Then("Clear text in Search bar with clear button in the Search bar")
	public void clear_text_in_search_bar_with_clear_button_in_the_search_bar() throws IOException {
		addandVerifyCustomSPinJobComparisonPage.getCustomSPinJobComparison().clear_text_in_search_bar_with_clear_button_in_the_search_bar();
	}

	@Then("User should Verify added Custom SP is not cleared from Profiles List in Job Comparison page")
	public void user_should_verify_added_custom_sp_is_not_cleared_from_profiles_list_in_job_comparison_page() throws IOException {
		addandVerifyCustomSPinJobComparisonPage.getCustomSPinJobComparison().user_should_verify_added_custom_sp_is_not_cleared_from_profiles_list_in_job_comparison_page();
	}

	@Then("Select Third Custom SP from search results")
	public void select_third_custom_sp_from_search_results() throws IOException {
		addandVerifyCustomSPinJobComparisonPage.getCustomSPinJobComparison().select_third_custom_sp_from_search_results();
	}
	
	@Then("Verify New Custom SP replaces existing Custom SP in Profiles List in Job Comparison page")
	public void verify_new_custom_sp_replaces_existing_custom_sp_in_profiles_list_in_job_comparison_page() throws IOException {
		addandVerifyCustomSPinJobComparisonPage.getCustomSPinJobComparison().verify_new_custom_sp_replaces_existing_custom_sp_in_profiles_list_in_job_comparison_page();
	}
	
	@Then("Clear Custom SP and text in Search bar with close button on the Profile")
	public void clear_custom_sp_and_text_in_search_bar_with_close_button_on_the_profile() throws IOException {
		addandVerifyCustomSPinJobComparisonPage.getCustomSPinJobComparison().clear_custom_sp_and_text_in_search_bar_with_close_button_on_the_profile();
	}
}
