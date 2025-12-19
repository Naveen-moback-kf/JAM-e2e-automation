package stepdefinitions.JobMapping;

import java.io.IOException;

import com.kfonetalentsuite.manager.PageObjectManager;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SD13_PCRestrictedTipMessage {
	PageObjectManager validatePCRestrictedTipMessage = new PageObjectManager();
	
	public SD13_PCRestrictedTipMessage() {
		super();		
	}
	
	@Then("Navigate to System Configuration Page from KFONE Global Menu")
	public void navigate_to_system_configuration_page_from_kfone_global_menu() throws IOException {
		validatePCRestrictedTipMessage.getPCRestrictedTipMessage().navigate_to_system_configuration_page_from_kfone_global_menu();
	}

	@Then("Click on User Admin Module button")
	public void click_on_user_admin_module_button() throws IOException {
		validatePCRestrictedTipMessage.getPCRestrictedTipMessage().click_on_user_admin_module_button();
	}

	@Then("User should be landed on Clients Dashboard page")
	public void user_should_be_landed_on_clients_dashboard_page() throws IOException {
		validatePCRestrictedTipMessage.getPCRestrictedTipMessage().user_should_be_landed_on_clients_dashboard_page();
	}
	
	@When("User is on Clients Dashboard page")
	public void user_is_on_clients_dashboard_page() throws IOException {
	    validatePCRestrictedTipMessage.getPCRestrictedTipMessage().user_is_on_clients_dashboard_page();
	}

	@Then("Click on Teams section")
	public void click_on_teams_section() throws IOException {
		validatePCRestrictedTipMessage.getPCRestrictedTipMessage().click_on_teams_section();
	}

	@Then("Verify User landed on Teams page")
	public void verify_user_landed_on_teams_page() throws IOException {
		validatePCRestrictedTipMessage.getPCRestrictedTipMessage().verify_user_landed_on_teams_page();
	}

	@Then("Click on Create Teams button")
	public void click_on_create_teams_button() throws IOException {
		validatePCRestrictedTipMessage.getPCRestrictedTipMessage().click_on_create_teams_button();
	}

	@Then("User should be navigated to first step of creating a team")
	public void user_should_be_navigated_to_first_step_of_creating_a_team() throws IOException {
		validatePCRestrictedTipMessage.getPCRestrictedTipMessage().user_should_be_navigated_to_first_step_of_creating_a_team();
	}

	@Then("User should enter Team name and Team Description")
	public void user_should_enter_team_name_and_team_description() throws IOException {
		validatePCRestrictedTipMessage.getPCRestrictedTipMessage().user_should_enter_team_name_and_team_description();
	}

	@Then("Click on Next button in Create Team page")
	public void click_on_next_button_in_create_team_page() throws IOException {
		validatePCRestrictedTipMessage.getPCRestrictedTipMessage().click_on_next_button_in_create_team_page();
	}

	@Then("User should be navigated to second step of creating a team")
	public void user_should_be_navigated_to_second_step_of_creating_a_team() throws IOException {
		validatePCRestrictedTipMessage.getPCRestrictedTipMessage().user_should_be_navigated_to_second_step_of_creating_a_team();
	}

	@Then("Search user to add as team member")
	public void search_user_to_add_as_team_member() throws IOException {
		validatePCRestrictedTipMessage.getPCRestrictedTipMessage().search_user_to_add_as_team_member();
	}

	@Then("Select searched user to add as team member")
	public void select_searched_user_to_add_as_team_member() throws IOException {
		validatePCRestrictedTipMessage.getPCRestrictedTipMessage().select_searched_user_to_add_as_team_member();
	}

	@Then("Click on Team Members header and Verify User is added successfully as team member")
	public void click_on_team_members_header_and_verify_user_is_added_successfully_as_team_member() throws IOException {
		validatePCRestrictedTipMessage.getPCRestrictedTipMessage().click_on_team_members_header_and_verify_user_is_added_successfully_as_team_member();
	}

	@Then("Click on Save button on Team page and verify success popup appears")
	public void click_on_save_button_on_team_page_and_verify_success_popup_appears() throws IOException {
		validatePCRestrictedTipMessage.getPCRestrictedTipMessage().click_on_save_button_on_team_page_and_verify_success_popup_appears();
	}

	@Then("Search for Team name in Teams page and verify Team is created successfully")
	public void search_for_team_name_in_teams_page_and_verify_team_is_created_successfully() throws IOException {
		validatePCRestrictedTipMessage.getPCRestrictedTipMessage().search_for_team_name_in_teams_page_and_verify_team_is_created_successfully();
	}
	
	@Then("Click on Profile Collections Section")
	public void click_on_profile_collections_section() throws IOException {
		validatePCRestrictedTipMessage.getPCRestrictedTipMessage().click_on_profile_collections_section();
	}
	
	@Then("Verify User navigated to Manage Success Profiles page")
	public void verify_user_navigated_to_manage_success_profiles_page() throws IOException {
		validatePCRestrictedTipMessage.getPCRestrictedTipMessage().verify_user_navigated_to_manage_success_profiles_page();
	}
	
	@Then("Click on Create Profile Collection button")
	public void click_on_create_profile_collection_button() throws IOException {
		validatePCRestrictedTipMessage.getPCRestrictedTipMessage().click_on_create_profile_collection_button();
	}
	
	@Then("User should navigated to first step of creating a Profile Collection")
	public void user_should_navigated_to_first_step_of_creating_a_profile_collection() throws IOException {
		validatePCRestrictedTipMessage.getPCRestrictedTipMessage().user_should_navigated_to_first_step_of_creating_a_profile_collection();
	}

	@Then("User should enter Profile Collection Name and Profile Collection Description")
	public void user_should_enter_profile_collection_name_and_profile_collection_description() throws IOException {
		validatePCRestrictedTipMessage.getPCRestrictedTipMessage().user_should_enter_profile_collection_name_and_profile_collection_description();
	}

	@Then("Click on Next button in Create Profile Collection page")
	public void click_on_next_button_in_create_profile_collection_page() throws IOException {
		validatePCRestrictedTipMessage.getPCRestrictedTipMessage().click_on_next_button_in_create_profile_collection_page();
	}

	@Then("User should be navigated to second step of creating a Profile Collection")
	public void user_should_be_navigated_to_second_step_of_creating_a_profile_collection() throws IOException {
		validatePCRestrictedTipMessage.getPCRestrictedTipMessage().user_should_be_navigated_to_second_step_of_creating_a_profile_collection();
	}

	@Then("Click on All Teams header")
	public void click_on_all_teams_header() throws IOException {
		validatePCRestrictedTipMessage.getPCRestrictedTipMessage().click_on_all_teams_header();
	}
	
	@Then("Select Recently created team name")
	public void select_recently_created_team_name() throws IOException {
		validatePCRestrictedTipMessage.getPCRestrictedTipMessage().select_recently_created_team_name();
	}

	@Then("Click on Selected Teams header")
	public void click_on_selected_teams_header() throws IOException {
		validatePCRestrictedTipMessage.getPCRestrictedTipMessage().click_on_selected_teams_header();
	}

	@Then("User should verify Recently created team name is available in Selected Teams")
	public void user_should_verify_recently_created_team_name_is_available_in_selected_teams() throws IOException {
		validatePCRestrictedTipMessage.getPCRestrictedTipMessage().user_should_verify_recently_created_team_name_is_available_in_selected_teams();
	}
	
	@Then("User should be navigated to third step of creating a Profile Collection")
	public void user_should_be_navigated_to_third_step_of_creating_a_profile_collection() throws IOException {
		validatePCRestrictedTipMessage.getPCRestrictedTipMessage().user_should_be_navigated_to_third_step_of_creating_a_profile_collection();
	}

	@Then("Click on Add Additional Success Profiles header")
	public void click_on_add_additional_success_profiles_header() throws IOException {
		validatePCRestrictedTipMessage.getPCRestrictedTipMessage().click_on_add_additional_success_profiles_header();
	}
	
	@Then("Search and add Success Profiles to Profile Collection")
	public void search_and_add_success_profiles_to_profile_collection() throws IOException {
		validatePCRestrictedTipMessage.getPCRestrictedTipMessage().search_and_add_success_profiles_to_profile_collection();
	}
	
	@Then("Click on Success Profiles header")
	public void click_on_success_profiles_header() throws IOException {
		validatePCRestrictedTipMessage.getPCRestrictedTipMessage().click_on_success_profiles_header();
	}
	
	@Then("User should verify added Profiles are available in Success Profiles header")
	public void user_should_verify_added_profiles_are_available_in_success_profiles_header() throws IOException {
		validatePCRestrictedTipMessage.getPCRestrictedTipMessage().user_should_verify_added_profiles_are_available_in_success_profiles_header();
	}
	
	@Then("Click on Done button on Create Profile Collection page and verify success popup appears")
	public void click_on_done_button_on_create_profile_collection_page_and_verify_success_popup_appears() throws IOException {
		validatePCRestrictedTipMessage.getPCRestrictedTipMessage().click_on_done_button_on_create_profile_collection_page_and_verify_success_popup_appears();
	}
	
	@Then("Verify PC Restricted Tip Message is displaying on Job Mapping page")
	public void verify_pc_restricted_tip_message_is_displaying_on_job_mapping_page() throws IOException {
		validatePCRestrictedTipMessage.getPCRestrictedTipMessage().verify_pc_restricted_tip_message_is_displaying_on_job_mapping_page();
	}
	
	@Then("Search and delete Profile Collection")
	public void search_and_delete_profile_collection() throws IOException {
		validatePCRestrictedTipMessage.getPCRestrictedTipMessage().search_and_delete_profile_collection();
	}
	
	@Then("Search for Team name in Teams page and delete team")
	public void search_for_team_name_in_teams_page_and_delete_team() throws IOException {
		validatePCRestrictedTipMessage.getPCRestrictedTipMessage().search_for_team_name_in_teams_page_and_delete_team();
	}
	
	@When("User is on Teams page")
	public void user_is_on_teams_page() throws IOException {
		validatePCRestrictedTipMessage.getPCRestrictedTipMessage().user_is_on_teams_page();
	}
	
	@When("User is on second step of creating a team")
	public void user_is_on_second_step_of_creating_a_team() throws IOException {
		validatePCRestrictedTipMessage.getPCRestrictedTipMessage().user_is_on_second_step_of_creating_a_team();
	}
	
	@When("User is on Team creation page with team members added")
	public void user_is_on_team_creation_page_with_team_members_added() throws IOException {
		validatePCRestrictedTipMessage.getPCRestrictedTipMessage().user_is_on_team_creation_page_with_team_members_added();
	}
	
	@When("User is on Manage Success Profiles page")
	public void user_is_on_manage_success_profiles_page() throws IOException {
		validatePCRestrictedTipMessage.getPCRestrictedTipMessage().user_is_on_manage_success_profiles_page();
	}
	
	@When("User is on second step of creating a Profile Collection")
	public void user_is_on_second_step_of_creating_a_profile_collection() throws IOException {
		validatePCRestrictedTipMessage.getPCRestrictedTipMessage().user_is_on_second_step_of_creating_a_profile_collection();
	}
	
	@When("User is on third step of creating a Profile Collection")
	public void user_is_on_third_step_of_creating_a_profile_collection() throws IOException {
		validatePCRestrictedTipMessage.getPCRestrictedTipMessage().user_is_on_third_step_of_creating_a_profile_collection();
	}
	
	@When("User is on Profile Collection creation page with profiles added")
	public void user_is_on_profile_collection_page_with_profiles_added() throws IOException {
		validatePCRestrictedTipMessage.getPCRestrictedTipMessage().user_is_on_profile_collection_page_with_profiles_added();
	}
}
