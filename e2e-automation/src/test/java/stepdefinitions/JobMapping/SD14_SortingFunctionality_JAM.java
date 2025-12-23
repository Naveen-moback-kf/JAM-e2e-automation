package stepdefinitions.JobMapping;

import java.io.IOException;

import com.kfonetalentsuite.manager.PageObjectManager;

import io.cucumber.java.en.Then;

public class SD14_SortingFunctionality_JAM {
	PageObjectManager validateSortingFunctionality_JAM = new PageObjectManager();
	
	public SD14_SortingFunctionality_JAM() {
		super();		
	}
	
	// ============================================================
	// PARAMETERIZED STEP DEFINITIONS FOR SCENARIO OUTLINE
	// Uses {string} to avoid ambiguity with specific step definitions
	// ============================================================
	
	@Then("Sort Job Profiles by {string} in {string} order")
	public void sort_job_profiles_by_column_in_order(String column, String order) throws IOException {
		validateSortingFunctionality_JAM.getSortingFunctionality_JAM().sort_job_profiles_by_column_in_order(column, order);
	}
	
	@Then("User should verify first thirty job profiles sorted by {string} in {string} order")
	public void user_should_verify_first_thirty_job_profiles_sorted_by_column_in_order(String column, String order) throws IOException {
		validateSortingFunctionality_JAM.getSortingFunctionality_JAM().user_should_verify_first_thirty_job_profiles_sorted_by_column_in_order(column, order);
	}
	
	// ============================================================
	// EXISTING STEP DEFINITIONS (kept for backward compatibility)
	// ============================================================
	
	@Then("User should scroll page down two times to view first thirty job profiles")
	public void user_should_scroll_page_down_two_times_to_view_first_thirty_job_profiles() throws IOException {
		validateSortingFunctionality_JAM.getSortingFunctionality_JAM().user_should_scroll_page_down_two_times_to_view_first_thirty_job_profiles();
	}

	
	@Then("User should verify first thirty job profiles in default order before applying sorting")
	public void user_should_verify_first_thirty_job_profiles_in_default_order_before_applying_sorting() throws IOException {
		validateSortingFunctionality_JAM.getSortingFunctionality_JAM().user_should_verify_first_thirty_job_profiles_in_default_order_before_applying_sorting();
	}
	
	@Then("Sort Job Profiles by Organization Job Name in Ascending order")
	public void sort_job_profiles_by_organiztion_job_name_in_ascending_order() throws IOException {
		validateSortingFunctionality_JAM.getSortingFunctionality_JAM().sort_job_profiles_by_organiztion_job_name_in_ascending_order();
	}

	@Then("User should verify first thirty job profiles sorted by Organization Job Name in Ascending order")
	public void user_should_verify_first_thirty_job_profiles_sorted_by_organiztion_job_name_in_ascending_order() throws IOException {
		validateSortingFunctionality_JAM.getSortingFunctionality_JAM().user_should_verify_first_thirty_job_profiles_sorted_by_organiztion_job_name_in_ascending_order();
	}
	
	@Then("User should Refresh Job Mapping and Verify Job Profiles are in default order")
	public void user_should_refresh_job_mapping_and_verify_job_profiles_are_in_default_order() throws IOException {
		validateSortingFunctionality_JAM.getSortingFunctionality_JAM().user_should_refresh_job_mapping_and_verify_job_profiles_are_in_default_order();
	}
	
	@Then("Sort Job Profiles by Organization Job Name in Descending order")
	public void sort_job_profiles_by_name_in_descending_order() throws IOException {
		validateSortingFunctionality_JAM.getSortingFunctionality_JAM().sort_job_profiles_by_organiztion_job_name_in_descending_order();
	}

	@Then("User should verify first thirty job profiles sorted by Organization Job Name in Descending order")
	public void user_should_verify_first_thirty_job_profiles_sorted_by_name_in_descending_order() throws IOException {
		validateSortingFunctionality_JAM.getSortingFunctionality_JAM().user_should_verify_first_thirty_job_profiles_sorted_by_organiztion_job_name_in_descending_order();
	}
	
	@Then("Sort Job Profiles by Matched Success Profile Grade in Ascending order")
	public void sort_job_profiles_by_matched_success_profile_grade_in_ascending_order() throws IOException {
		validateSortingFunctionality_JAM.getSortingFunctionality_JAM().sort_job_profiles_by_matched_success_profile_grade_in_ascending_order();
	}

	@Then("User should verify first thirty job profiles sorted by Matched Success Profile Grade in Ascending order")
	public void user_should_verify_first_thirty_job_profiles_sorted_by_matched_success_profile_grade_in_ascending_order() throws IOException {
		validateSortingFunctionality_JAM.getSortingFunctionality_JAM().user_should_verify_first_thirty_job_profiles_sorted_by_matched_success_profile_grade_in_ascending_order();
	}

	@Then("Sort Job Profiles by Matched Success Profile Grade in Descending order")
	public void sort_job_profiles_by_matched_success_profile_grade_in_descending_order() throws IOException {
		validateSortingFunctionality_JAM.getSortingFunctionality_JAM().sort_job_profiles_by_matched_success_profile_grade_in_descending_order();
	}

	@Then("User should verify first thirty job profiles sorted by Matched Success Profile Grade in Descending order")
	public void user_should_verify_first_thirty_job_profiles_sorted_by_matched_success_profile_grade_in_descending_order() throws IOException {
		validateSortingFunctionality_JAM.getSortingFunctionality_JAM().user_should_verify_first_thirty_job_profiles_sorted_by_matched_success_profile_grade_in_descending_order();
	}
	
	@Then("Sort Job Profiles by Matched Success Profile Name in Ascending order")
	public void sort_job_profiles_by_matched_success_profile_name_in_ascending_order() throws IOException {
		validateSortingFunctionality_JAM.getSortingFunctionality_JAM().sort_job_profiles_by_matched_success_profile_name_in_ascending_order();
	}

	@Then("User should verify first thirty job profiles sorted by Matched Success Profile Name in Ascending order")
	public void user_should_verify_first_thirty_job_profiles_sorted_by_matched_success_profile_name_in_ascending_order() throws IOException {
		validateSortingFunctionality_JAM.getSortingFunctionality_JAM().user_should_verify_first_thirty_job_profiles_sorted_by_matched_success_profile_name_in_ascending_order();
	}
	
	@Then("Sort Job Profiles by Organization Grade in Ascending order")
	public void sort_job_profiles_by_organization_grade_in_ascending_order() throws IOException {
		validateSortingFunctionality_JAM.getSortingFunctionality_JAM().sort_job_profiles_by_organization_grade_in_ascending_order();
	}

	@Then("User should verify first thirty job profiles sorted by Organization Grade and Organization Job Name in Ascending order")
	public void user_should_verify_first_thirty_job_profiles_sorted_by_organization_grade_and_organization_job_name_in_ascending_order() throws IOException {
		validateSortingFunctionality_JAM.getSortingFunctionality_JAM().user_should_verify_first_thirty_job_profiles_sorted_by_organization_grade_and_organization_job_name_in_ascending_order();
	}
	
	@Then("Sort Job Profiles by Organization Grade in Descending order")
	public void sort_job_profiles_by_organization_grade_in_descending_order() throws IOException {
		validateSortingFunctionality_JAM.getSortingFunctionality_JAM().sort_job_profiles_by_organization_grade_in_descending_order();
	}

	@Then("User should verify first thirty job profiles sorted by Organization Grade in Descending order and Organization Job Name in Ascending order")
	public void user_should_verify_first_thirty_job_profiles_sorted_by_organization_grade_in_descending_order_and_organization_job_name_in_ascending_order() throws IOException {
		validateSortingFunctionality_JAM.getSortingFunctionality_JAM().user_should_verify_first_thirty_job_profiles_sorted_by_organization_grade_in_descending_order_and_organization_job_name_in_ascending_order();
	}


}
