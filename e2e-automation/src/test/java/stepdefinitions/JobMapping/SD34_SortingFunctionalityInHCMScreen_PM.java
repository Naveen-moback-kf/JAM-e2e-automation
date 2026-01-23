package stepdefinitions.JobMapping;

import java.io.IOException;

import com.kfonetalentsuite.manager.PageObjectManager;

import io.cucumber.java.en.Then;

public class SD34_SortingFunctionalityInHCMScreen_PM {	
	public SD34_SortingFunctionalityInHCMScreen_PM() {
		super();		
	}
	
	// ============================================================
	// PARAMETERIZED STEP DEFINITIONS FOR SCENARIO OUTLINE
	// Uses {string} to avoid ambiguity with specific step definitions
	// ============================================================
	
	@Then("Sort Profiles by {string} in {string} order in HCM Sync Profiles screen")
	public void sort_profiles_by_column_in_order_in_hcm_sync_profiles_screen(String column, String order) throws IOException {
		PageObjectManager.getInstance().getSortingFunctionalityInHCMScreen_PM().sort_profiles_by_column_in_order_in_hcm_sync_profiles_screen(column, order);
	}
	
	@Then("User should verify first hundred job profiles are correctly sorted by {string} in {string} order")
	public void user_should_verify_first_hundred_job_profiles_are_correctly_sorted_by_column_in_order(String column, String order) throws IOException {
		PageObjectManager.getInstance().getSortingFunctionalityInHCMScreen_PM().user_should_verify_first_hundred_job_profiles_are_correctly_sorted_by_column_in_order(column, order);
	}
	
	// ============================================================
	// EXISTING STEP DEFINITIONS (kept for backward compatibility)
	// ============================================================
	
	@Then("Sort Profiles by Name in Ascending order in HCM Sync Profiles screen")
	public void sort_profiles_by_name_in_ascending_order_in_hcm_sync_profiles_screen() throws IOException {
		PageObjectManager.getInstance().getSortingFunctionalityInHCMScreen_PM().sort_profiles_by_name_in_ascending_order_in_hcm_sync_profiles_screen();
	}
	
	@Then("User should verify first hundred job profiles are correctly sorted by Name in Ascending order")
	public void user_should_verify_first_hundred_job_profiles_are_correctly_sorted_by_name_in_ascending_order() throws IOException {
		PageObjectManager.getInstance().getSortingFunctionalityInHCMScreen_PM().user_should_verify_first_hundred_job_profiles_are_correctly_sorted_by_name_in_ascending_order();
	}
	
	@Then("Sort Profiles by Level in Descending order in HCM Sync Profiles screen")
	public void sort_profiles_by_level_in_descending_order_in_hcm_sync_profiles_screen() throws IOException {
		PageObjectManager.getInstance().getSortingFunctionalityInHCMScreen_PM().sort_profiles_by_level_in_descending_order_in_hcm_sync_profiles_screen();
	}
	
	@Then("User should verify first hundred job profiles are correctly sorted by Level in Descending order")
	public void user_should_verify_first_hundred_job_profiles_are_correctly_sorted_by_level_in_descending_order() throws IOException {
		PageObjectManager.getInstance().getSortingFunctionalityInHCMScreen_PM().user_should_verify_first_hundred_job_profiles_are_correctly_sorted_by_level_in_descending_order();
	}
	
	@Then("Sort Profiles by Job Status in Ascending order in HCM Sync Profiles screen")
	public void sort_profiles_by_job_status_in_ascending_order_in_hcm_sync_profiles_screen() throws IOException {
		PageObjectManager.getInstance().getSortingFunctionalityInHCMScreen_PM().sort_profiles_by_job_status_in_ascending_order_in_hcm_sync_profiles_screen();
	}
	
	@Then("Sort Profiles by Job Code in Ascending order in HCM Sync Profiles screen")
	public void sort_profiles_by_job_code_in_ascending_order_in_hcm_sync_profiles_screen() throws IOException {
		PageObjectManager.getInstance().getSortingFunctionalityInHCMScreen_PM().sort_profiles_by_job_code_in_ascending_order_in_hcm_sync_profiles_screen();
	}
	
	@Then("User should verify first hundred job profiles are correctly sorted by Job Code in Ascending order")
	public void user_should_verify_first_hundred_job_profiles_are_correctly_sorted_by_job_code_in_ascending_order() throws IOException {
		PageObjectManager.getInstance().getSortingFunctionalityInHCMScreen_PM().user_should_verify_first_hundred_job_profiles_are_correctly_sorted_by_job_code_in_ascending_order();
	}
	
	@Then("Sort Profiles by Function in Descending order in HCM Sync Profiles screen")
	public void sort_profiles_by_function_in_descending_order_in_hcm_sync_profiles_screen() throws IOException {
		PageObjectManager.getInstance().getSortingFunctionalityInHCMScreen_PM().sort_profiles_by_function_in_descending_order_in_hcm_sync_profiles_screen();
	}
	
	@Then("User should verify first hundred job profiles are correctly sorted by Job Status in Ascending and Function in Descending order")
	public void user_should_verify_first_hundred_job_profiles_are_correctly_sorted_by_job_status_in_ascending_and_function_in_descending_order() throws IOException {
		PageObjectManager.getInstance().getSortingFunctionalityInHCMScreen_PM().user_should_verify_first_hundred_job_profiles_are_correctly_sorted_by_job_status_in_ascending_and_function_in_descending_order();
	}
	
	@Then("Sort Profiles by Export Status in Descending order in HCM Sync Profiles screen")
	public void sort_profiles_by_export_status_in_descending_order_in_hcm_sync_profiles_screen() throws IOException {
		PageObjectManager.getInstance().getSortingFunctionalityInHCMScreen_PM().sort_profiles_by_export_status_in_descending_order_in_hcm_sync_profiles_screen();
	}
	
	@Then("Sort Profiles by Function in Ascending order in HCM Sync Profiles screen")
	public void sort_profiles_by_function_in_ascending_order_in_hcm_sync_profiles_screen() throws IOException {
		PageObjectManager.getInstance().getSortingFunctionalityInHCMScreen_PM().sort_profiles_by_function_in_ascending_order_in_hcm_sync_profiles_screen();
	}
	
	@Then("User should verify first hundred job profiles are correctly sorted by Job Status Ascending, Export Status Descending and Function Ascending")
	public void user_should_verify_first_hundred_job_profiles_are_correctly_sorted_by_job_status_ascending_export_status_descending_and_function_ascending() throws IOException {
		PageObjectManager.getInstance().getSortingFunctionalityInHCMScreen_PM().user_should_verify_first_hundred_job_profiles_are_correctly_sorted_by_job_status_ascending_export_status_descending_and_function_ascending();
	}

}

