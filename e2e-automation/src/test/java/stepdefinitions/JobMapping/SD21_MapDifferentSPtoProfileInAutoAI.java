package stepdefinitions.JobMapping;

import java.io.IOException;

import org.testng.SkipException;

import com.kfonetalentsuite.manager.PageObjectManager;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SD21_MapDifferentSPtoProfileInAutoAI {
	PageObjectManager mapDifferentSPtoProfileInAutoAI = new PageObjectManager();
	
	// Track if the foundational scenario is skipped
	private static boolean foundationalScenarioSkipped = false;
	private static final String FOUNDATIONAL_SCENARIO_TAG = "@VerifyOrganizationJobDetails";
	
	public SD21_MapDifferentSPtoProfileInAutoAI() {
		super();		
	}
	
	/**
	 * Before hook to check if dependent scenarios should be skipped
	 * If the foundational scenario (@VerifyOrganizationJobDetails) was skipped,
	 * all subsequent scenarios in this feature should also be skipped
	 */
	@Before(value = "@Map_Different_SP_in_AutoAI", order = 1)
	public void checkFoundationalScenarioStatus(Scenario scenario) {
		// Check if this is the foundational scenario
		if (scenario.getSourceTagNames().contains(FOUNDATIONAL_SCENARIO_TAG)) {
			// Reset flag at the start of foundational scenario
			foundationalScenarioSkipped = false;
			return; // Allow foundational scenario to run
		}
		
		// Skip all other scenarios if foundational scenario was skipped
		if (foundationalScenarioSkipped) {
			String scenarioName = scenario.getName();
			throw new SkipException("SKIPPED: Dependent scenario '" + scenarioName + "' skipped because foundational scenario " +
					"'Verify Organization Job details on Profile with Search a Different Profile button' was skipped. " +
					"No manually mapped profiles available for testing.");
		}
	}
	
	/**
	 * After hook to detect if foundational scenario was skipped
	 */
	@io.cucumber.java.After(value = "@VerifyOrganizationJobDetails", order = 1)
	public void captureFoundationalScenarioResult(Scenario scenario) {
		// Check if the scenario was skipped
		if (scenario.getStatus().toString().equals("SKIPPED")) {
			foundationalScenarioSkipped = true;
		}
	}
	
	@Then("User should search for Job Profile with Search a Different Profile button on Mapped Success Profile")
	public void user_should_search_for_job_profile_with_search_a_different_profile_button_on_mapped_success_profile() throws IOException {
		mapDifferentSPtoProfileInAutoAI.getMapDifferentSPtoProfileInAutoAI().user_should_search_for_job_profile_with_search_a_different_profile_button_on_mapped_success_profile();
	}

	@Then("User should verify Organization Job Name and Job Code Values of Job Profile with Search a Different Profile button")
	public void user_should_verify_organization_job_name_and_job_code_values_of_job_profile_with_search_a_different_profile_button() throws IOException {
		mapDifferentSPtoProfileInAutoAI.getMapDifferentSPtoProfileInAutoAI().user_should_verify_organization_job_name_and_job_code_values_of_job_profile_with_search_a_different_profile_button();
	}

	@Then("User should verify Organization Job Grade and Department values of Job Profile with Search a Different Profile button")
	public void user_should_verify_organization_job_grade_and_department_values_of_job_profile_with_search_a_different_profile_button() throws IOException {
		mapDifferentSPtoProfileInAutoAI.getMapDifferentSPtoProfileInAutoAI().user_should_verify_organization_job_grade_and_department_values_of_job_profile_with_search_a_different_profile_button();
	}

	@Then("User should verify Organization Job Function or Sub-function of Job Profile with Search a Different Profile button")
	public void user_should_verify_organization_job_function_or_sub_function_of_job_profile_with_search_a_different_profile_button() throws IOException {
		mapDifferentSPtoProfileInAutoAI.getMapDifferentSPtoProfileInAutoAI().user_should_verify_organization_job_function_or_sub_function_of_job_profile_with_search_a_different_profile_button();
	}

	@Then("Click on mapped profile of Job Profile with Search a Different Profile button")
	public void click_on_mapped_profile_of_job_profile_with_search_a_different_profile_button() throws IOException {
		mapDifferentSPtoProfileInAutoAI.getMapDifferentSPtoProfileInAutoAI().click_on_mapped_profile_of_job_profile_with_search_a_different_profile_button();
	}
	
	@Then("Verify Mapped Profile details popup is displayed")
	public void verify_mapped_profile_details_popup_is_displayed() throws IOException {
		mapDifferentSPtoProfileInAutoAI.getMapDifferentSPtoProfileInAutoAI().verify_mapped_profile_details_popup_is_displayed();
	}

	@When("User is on profile details popup of manually mapped profile")
	public void user_is_on_profile_details_popup_of_manually_mapped_profile() throws IOException {
		mapDifferentSPtoProfileInAutoAI.getMapDifferentSPtoProfileInAutoAI().user_is_on_profile_details_popup_of_manually_mapped_profile();
	}

	@Then("Verify profile header matches with mapped profile name")
	public void verify_profile_header_matches_with_mapped_profile_name() throws IOException {
		mapDifferentSPtoProfileInAutoAI.getMapDifferentSPtoProfileInAutoAI().verify_profile_header_matches_with_mapped_profile_name();
	}

	@Then("Verify mapped profile details displaying on the popup")
	public void verify_mapped_profile_details_displaying_on_the_popup() throws IOException {
		mapDifferentSPtoProfileInAutoAI.getMapDifferentSPtoProfileInAutoAI().verify_mapped_profile_details_displaying_on_the_popup();
	}

	@Then("User should verify Profile Level dropdown is available and Validate levels present inside dropdown of mapped profile")
	public void user_should_verify_profile_level_dropdown_is_available_and_validate_levels_present_inside_dropdown_of_mapped_profile() throws IOException {
		mapDifferentSPtoProfileInAutoAI.getMapDifferentSPtoProfileInAutoAI().user_should_verify_profile_level_dropdown_is_available_and_validate_levels_present_inside_dropdown_of_mapped_profile();
	}

	@Then("Validate Role Summary of mapped profile is displaying")
	public void validate_role_summary_of_mapped_profile_is_displaying() throws IOException {
		mapDifferentSPtoProfileInAutoAI.getMapDifferentSPtoProfileInAutoAI().validate_role_summary_of_mapped_profile_is_displaying();
	}

	@Then("Validate data in RESPONSIBILITIES screen of mapped profile")
	public void validate_data_in_responsibilities_tab_of_mapped_profile() throws IOException {
		mapDifferentSPtoProfileInAutoAI.getMapDifferentSPtoProfileInAutoAI().validate_data_in_responsibilities_tab_of_mapped_profile();
	}

	@Then("Validate data in BEHAVIOURAL COMPETENCIES screen of mapped profile")
	public void validate_data_in_behavioural_competencies_tab_of_mapped_profile() throws IOException {
		mapDifferentSPtoProfileInAutoAI.getMapDifferentSPtoProfileInAutoAI().validate_data_in_behavioural_competencies_tab_of_mapped_profile();
	}

	@Then("Validate data in SKILLS screen of mapped profile")
	public void validate_data_in_skills_tab_of_mapped_profile() throws IOException {
		mapDifferentSPtoProfileInAutoAI.getMapDifferentSPtoProfileInAutoAI().validate_data_in_skills_tab_of_mapped_profile();
	}

	@Then("User should verify publish profile button is available on popup screen of mapped profile")
	public void user_should_verify_publish_profile_button_is_available_on_popup_screen_of_mapped_profile() throws IOException {
		mapDifferentSPtoProfileInAutoAI.getMapDifferentSPtoProfileInAutoAI().user_should_verify_publish_profile_button_is_available_on_popup_screen_of_mapped_profile();
	}

	@Then("Click on close button in profile details popup of mapped profile")
	public void click_on_close_button_in_profile_details_popup_of_mapped_profile() throws IOException {
		mapDifferentSPtoProfileInAutoAI.getMapDifferentSPtoProfileInAutoAI().click_on_close_button_in_profile_details_popup_of_mapped_profile();
	}

	@Then("Click on Search a Different Profile button on Mapped Success Profile")
	public void click_on_search_a_different_profile_button_on_mapped_success_profile() throws IOException {
		mapDifferentSPtoProfileInAutoAI.getMapDifferentSPtoProfileInAutoAI().click_on_search_a_different_profile_button_on_mapped_success_profile();
	}

	@Then("User should be navigated to Manual Job Mapping screen")
	public void user_should_be_navigated_to_manual_job_mapping_screen() throws IOException {
		mapDifferentSPtoProfileInAutoAI.getMapDifferentSPtoProfileInAutoAI().user_should_be_navigated_to_manual_job_mapping_screen();
	}

	@Then("Verify Organization Job details in Manual Mapping screen")
	public void verify_organization_job_details_in_manual_mapping_screen() throws IOException {
		mapDifferentSPtoProfileInAutoAI.getMapDifferentSPtoProfileInAutoAI().verify_organization_job_details_in_manual_mapping_screen();
	}
	
	@Then("User should verify Last Saved Profile name is displaying in Manual Mapping screen")
	public void user_should_verify_last_saved_profile_name_is_displaying_in_manual_mapping_screen() throws IOException {
		mapDifferentSPtoProfileInAutoAI.getMapDifferentSPtoProfileInAutoAI().user_should_verify_last_saved_profile_name_is_displaying_in_manual_mapping_screen();
	}

	@Then("Click on Last Saved Profile name in Manual Mapping Screen")
	public void click_on_last_saved_profile_name_in_manual_mapping_screen() throws IOException {
		mapDifferentSPtoProfileInAutoAI.getMapDifferentSPtoProfileInAutoAI().click_on_last_saved_profile_name_in_manual_mapping_screen();
	}
	
	@Then("User should verify Last Saved Profile name in Manual Mapping Screen matches with Profile Name in details Popup")
	public void user_should_verify_last_saved_profile_name_in_manual_mapping_screen_matches_with_profile_name_in_details_popup() throws IOException {
		mapDifferentSPtoProfileInAutoAI.getMapDifferentSPtoProfileInAutoAI().user_should_verify_last_saved_profile_name_in_manual_mapping_screen_matches_with_profile_name_in_details_popup();
	}

	@Then("User should verify Profile Level dropdown is available on Last Saved Success Profile and Validate levels present inside dropdown")
	public void user_should_verify_profile_level_dropdown_is_available_on_last_saved_success_profile_and_validate_levels_present_inside_dropdown() throws IOException {
		mapDifferentSPtoProfileInAutoAI.getMapDifferentSPtoProfileInAutoAI().user_should_verify_profile_level_dropdown_is_available_on_last_saved_success_profile_and_validate_levels_present_inside_dropdown();
	}
	
	@Then("Validate Last Saved Success Profile Role Summary matches with Profile Role Summary in details popup")
	public void validate_last_saved_success_profile_role_summary_matches_with_profile_role_summary_in_details_popup() throws IOException {
		mapDifferentSPtoProfileInAutoAI.getMapDifferentSPtoProfileInAutoAI().validate_last_saved_success_profile_role_summary_matches_with_profile_role_summary_in_details_popup();
	}
	
	@Then("Validate Last Saved Success Profile Details matches with Profile Details in details popup")
	public void validate_last_saved_success_profile_details_matches_with_profile_details_in_details_popup() throws IOException {
		mapDifferentSPtoProfileInAutoAI.getMapDifferentSPtoProfileInAutoAI().validate_last_saved_success_profile_details_matches_with_profile_details_in_details_popup();
	}

	@Then("Validate Last Saved Success Profile Responsibilities matches with Profile Responsibilities in details popup")
	public void validate_last_saved_success_profile_responsibilities_matches_with_profile_responsibilities_in_details_popup() throws IOException {
		mapDifferentSPtoProfileInAutoAI.getMapDifferentSPtoProfileInAutoAI().validate_last_saved_success_profile_responsibilities_matches_with_profile_responsibilities_in_details_popup();
	}

	@Then("Validate Last Saved Success Profile Behavioural competencies matches with Profile Behavioural competencies in details popup")
	public void validate_last_saved_success_profile_behavioural_competencies_matches_with_profile_behavioural_competencies_in_details_popup() throws IOException {
		mapDifferentSPtoProfileInAutoAI.getMapDifferentSPtoProfileInAutoAI().validate_last_saved_success_profile_behavioural_competencies_matches_with_profile_behavioural_competencies_in_details_popup();
	}

	@Then("Validate Last Saved Success Profile Skills macthes with Profile Skills in details popup")
	public void validate_last_saved_success_profile_skills_macthes_with_profile_skills_in_details_popup() throws IOException {
		mapDifferentSPtoProfileInAutoAI.getMapDifferentSPtoProfileInAutoAI().validate_last_saved_success_profile_skills_macthes_with_profile_skills_in_details_popup();
	}
	
	@Then("Search for Success Profile in Manual Mapping screen")
	public void search_for_success_profile_in_manual_mapping_screen() throws IOException {
		mapDifferentSPtoProfileInAutoAI.getMapDifferentSPtoProfileInAutoAI().search_for_success_profile_in_manual_mapping_screen();
	}

	@Then("Select first Sucess Profile from search results in Manual Mapping screen")
	public void select_first_sucess_profile_from_search_results_in_manual_mapping_screen() throws IOException {
		mapDifferentSPtoProfileInAutoAI.getMapDifferentSPtoProfileInAutoAI().select_first_sucess_profile_from_search_results_in_manual_mapping_screen();
	}

	@Then("Verify Success Profile is added in Manual Job Mapping screen")
	public void verify_success_profile_is_added_in_manual_job_mapping_screen() throws IOException {
		mapDifferentSPtoProfileInAutoAI.getMapDifferentSPtoProfileInAutoAI().verify_success_profile_is_added_in_manual_job_mapping_screen();
	}

	@When("Success Profile is added in Manual Job Mapping screen")
	public void success_profile_is_added_in_manual_job_mapping_screen() throws IOException {
		// This step assumes the success profile is already added from previous scenario
		// No action needed as this is a state assumption step
	}

	@Then("User should verify Profile Level dropdown is available and Validate levels present inside dropdown in Manual Job Mapping screen")
	public void user_should_verify_profile_level_dropdown_is_available_and_validate_levels_present_inside_dropdown_in_manual_job_mapping_screen() throws IOException {
		mapDifferentSPtoProfileInAutoAI.getMapDifferentSPtoProfileInAutoAI().user_should_verify_profile_level_dropdown_is_available_and_validate_levels_present_inside_dropdown_in_manual_job_mapping_screen();
	}

	@Then("Change Profile Level in Manual Job Mapping screen")
	public void change_profile_level_in_manual_job_mapping_screen() throws IOException {
		mapDifferentSPtoProfileInAutoAI.getMapDifferentSPtoProfileInAutoAI().change_profile_level_in_manual_job_mapping_screen();
	}

	@Then("Validate Role Summary is displaying in Manual Job Mapping screen")
	public void validate_role_summary_is_displaying_in_manual_job_mapping_screen() throws IOException {
		mapDifferentSPtoProfileInAutoAI.getMapDifferentSPtoProfileInAutoAI().validate_role_summary_is_displaying_in_manual_job_mapping_screen();
	}

	@Then("Verify profile details displaying in Manual Job Mapping screen")
	public void verify_profile_details_displaying_in_manual_job_mapping_screen() throws IOException {
		mapDifferentSPtoProfileInAutoAI.getMapDifferentSPtoProfileInAutoAI().verify_profile_details_displaying_in_manual_job_mapping_screen();
	}

	@Then("Validate data in RESPONSIBILITIES screen in Manual Job Mapping screen")
	public void validate_data_in_responsibilities_tab_in_manual_job_mapping_screen() throws IOException {
		mapDifferentSPtoProfileInAutoAI.getMapDifferentSPtoProfileInAutoAI().validate_data_in_responsibilities_tab_in_manual_job_mapping_screen();
	}

	@Then("Validate data in BEHAVIOURAL COMPETENCIES screen in Manual Job Mapping screen")
	public void validate_data_in_behavioural_competencies_tab_in_manual_job_mapping_screen() throws IOException {
		mapDifferentSPtoProfileInAutoAI.getMapDifferentSPtoProfileInAutoAI().validate_data_in_behavioural_competencies_tab_in_manual_job_mapping_screen();
	}

	@Then("Validate data in SKILLS screen in Manual Job Mapping screen")
	public void validate_data_in_skills_tab_in_manual_job_mapping_screen() throws IOException {
		mapDifferentSPtoProfileInAutoAI.getMapDifferentSPtoProfileInAutoAI().validate_data_in_skills_tab_in_manual_job_mapping_screen();
	}

	@Then("Click on Save Selection button in Manual Job Mapping screen")
	public void click_on_save_selection_button_in_manual_job_mapping_screen() throws IOException {
		mapDifferentSPtoProfileInAutoAI.getMapDifferentSPtoProfileInAutoAI().click_on_save_selection_button_in_manual_job_mapping_screen();
	}

	@Then("User should be navigated to Job Mapping page")
	public void user_should_be_navigated_to_job_mapping_page() throws IOException {
		mapDifferentSPtoProfileInAutoAI.getMapDifferentSPtoProfileInAutoAI().user_should_be_navigated_to_job_mapping_page();
	}
	
	@Then("Verify Organization Job with new Mapped SP is displaying on Top of Profiles List")
	public void verify_organization_job_with_new_mapped_sp_is_displaying_on_top_of_profiles_list() throws IOException {
		mapDifferentSPtoProfileInAutoAI.getMapDifferentSPtoProfileInAutoAI().verify_organization_job_with_new_mapped_sp_is_displaying_on_top_of_profiles_list();
	}
	
	@Then("User should verify Profile Level dropdown is available in details popup and Validate levels present inside dropdown")
	public void user_should_verify_profile_level_dropdown_is_available_in_details_popup_and_validate_levels_present_inside_dropdown() throws IOException {
		mapDifferentSPtoProfileInAutoAI.getMapDifferentSPtoProfileInAutoAI().user_should_verify_profile_level_dropdown_is_available_in_details_popup_and_validate_levels_present_inside_dropdown();
	}

	@Then("Validate Profile Role Summary in details popup matches with Mapped Success Profile Role Summary")
	public void validate_profile_role_summary_in_details_popup_matches_with_mapped_success_profile_role_summary() throws IOException {
		mapDifferentSPtoProfileInAutoAI.getMapDifferentSPtoProfileInAutoAI().validate_profile_role_summary_in_details_popup_matches_with_mapped_success_profile_role_summary();
	}

	@Then("validate Profile Details in details popup matches with Mapped Success Profile details")
	public void validate_profile_details_in_details_popup_matches_with_mapped_success_profile_details() throws IOException {
		mapDifferentSPtoProfileInAutoAI.getMapDifferentSPtoProfileInAutoAI().validate_profile_details_in_details_popup_matches_with_mapped_success_profile_details();
	}

	@Then("Validate Profile Responsibilities in details popup matches with Mapped Success Profile Responsibilities")
	public void validate_profile_responsibilities_in_details_popup_matches_with_mapped_success_profile_responsibilities() throws IOException {
		mapDifferentSPtoProfileInAutoAI.getMapDifferentSPtoProfileInAutoAI().validate_profile_responsibilities_in_details_popup_matches_with_mapped_success_profile_responsibilities();
	}

	@Then("Validate Profile Behavioural competencies in details popup matches with Mapped Success Profile Behavioural competencies")
	public void validate_profile_behavioural_competencies_in_details_popup_matches_with_mapped_success_profile_behavioural_competencies() throws IOException {
		mapDifferentSPtoProfileInAutoAI.getMapDifferentSPtoProfileInAutoAI().validate_profile_behavioural_competencies_in_details_popup_matches_with_mapped_success_profile_behavioural_competencies();
	}

	@Then("Validate Profile Skills in details popup matches with Mapped Success Profile Skills")
	public void validate_profile_skills_in_details_popup_matches_with_mapped_success_profile_skills() throws IOException {
		mapDifferentSPtoProfileInAutoAI.getMapDifferentSPtoProfileInAutoAI().validate_profile_skills_in_details_popup_matches_with_mapped_success_profile_skills();
	}

	@Then("Search for Organization Job with Manually Mapped SP")
	public void search_for_organization_job_with_manually_mapped_sp() throws IOException {
		mapDifferentSPtoProfileInAutoAI.getMapDifferentSPtoProfileInAutoAI().search_for_organization_job_with_manually_mapped_sp();
	}


}
