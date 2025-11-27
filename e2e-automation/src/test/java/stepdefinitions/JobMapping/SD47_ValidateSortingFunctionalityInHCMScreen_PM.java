package stepdefinitions.JobMapping;

import java.io.IOException;

import com.kfonetalentsuite.manager.PageObjectManager;

import io.cucumber.java.en.Then;

public class SD47_ValidateSortingFunctionalityInHCMScreen_PM {
	PageObjectManager validateSortingFunctionalityInHCMScreen_PM = new PageObjectManager();
	
	public SD47_ValidateSortingFunctionalityInHCMScreen_PM() {
		super();		
	}
	
	@Then("Sort Profiles by Name in Ascending order in HCM Sync Profiles screen")
	public void sort_profiles_by_name_in_ascending_order_in_hcm_sync_profiles_screen() throws IOException {
		validateSortingFunctionalityInHCMScreen_PM.getValidateSortingFunctionalityInHCMScreen_PM().sort_profiles_by_name_in_ascending_order_in_hcm_sync_profiles_screen();
	}
	
	@Then("User should verify first hundred job profiles are correctly sorted by Name in Ascending order")
	public void user_should_verify_first_hundred_job_profiles_are_correctly_sorted_by_name_in_ascending_order() throws IOException {
		validateSortingFunctionalityInHCMScreen_PM.getValidateSortingFunctionalityInHCMScreen_PM().user_should_verify_first_hundred_job_profiles_are_correctly_sorted_by_name_in_ascending_order();
	}
	
	@Then("Sort Profiles by Level in Descending order in HCM Sync Profiles screen")
	public void sort_profiles_by_level_in_descending_order_in_hcm_sync_profiles_screen() throws IOException {
		validateSortingFunctionalityInHCMScreen_PM.getValidateSortingFunctionalityInHCMScreen_PM().sort_profiles_by_level_in_descending_order_in_hcm_sync_profiles_screen();
	}
	
	@Then("User should verify first hundred job profiles are correctly sorted by Level in Descending order")
	public void user_should_verify_first_hundred_job_profiles_are_correctly_sorted_by_level_in_descending_order() throws IOException {
		validateSortingFunctionalityInHCMScreen_PM.getValidateSortingFunctionalityInHCMScreen_PM().user_should_verify_first_hundred_job_profiles_are_correctly_sorted_by_level_in_descending_order();
	}
	
	@Then("Sort Profiles by Job Status in Ascending order in HCM Sync Profiles screen")
	public void sort_profiles_by_job_status_in_ascending_order_in_hcm_sync_profiles_screen() throws IOException {
		validateSortingFunctionalityInHCMScreen_PM.getValidateSortingFunctionalityInHCMScreen_PM().sort_profiles_by_job_status_in_ascending_order_in_hcm_sync_profiles_screen();
	}
	
	@Then("Sort Profiles by Function in Descending order in HCM Sync Profiles screen")
	public void sort_profiles_by_function_in_descending_order_in_hcm_sync_profiles_screen() throws IOException {
		validateSortingFunctionalityInHCMScreen_PM.getValidateSortingFunctionalityInHCMScreen_PM().sort_profiles_by_function_in_descending_order_in_hcm_sync_profiles_screen();
	}
	
	@Then("User should verify first hundred job profiles are correctly sorted by Job Status in Ascending and Function in Descending order")
	public void user_should_verify_first_hundred_job_profiles_are_correctly_sorted_by_job_status_in_ascending_and_function_in_descending_order() throws IOException {
		validateSortingFunctionalityInHCMScreen_PM.getValidateSortingFunctionalityInHCMScreen_PM().user_should_verify_first_hundred_job_profiles_are_correctly_sorted_by_job_status_in_ascending_and_function_in_descending_order();
	}
	
	@Then("Sort Profiles by Export Status in Descending order in HCM Sync Profiles screen")
	public void sort_profiles_by_export_status_in_descending_order_in_hcm_sync_profiles_screen() throws IOException {
		validateSortingFunctionalityInHCMScreen_PM.getValidateSortingFunctionalityInHCMScreen_PM().sort_profiles_by_export_status_in_descending_order_in_hcm_sync_profiles_screen();
	}
	
	@Then("Sort Profiles by Function in Ascending order in HCM Sync Profiles screen")
	public void sort_profiles_by_function_in_ascending_order_in_hcm_sync_profiles_screen() throws IOException {
		validateSortingFunctionalityInHCMScreen_PM.getValidateSortingFunctionalityInHCMScreen_PM().sort_profiles_by_function_in_ascending_order_in_hcm_sync_profiles_screen();
	}
	
	@Then("User should verify first hundred job profiles are correctly sorted by Job Status Ascending, Export Status Descending and Function Ascending")
	public void user_should_verify_first_hundred_job_profiles_are_correctly_sorted_by_job_status_ascending_export_status_descending_and_function_ascending() throws IOException {
		validateSortingFunctionalityInHCMScreen_PM.getValidateSortingFunctionalityInHCMScreen_PM().user_should_verify_first_hundred_job_profiles_are_correctly_sorted_by_job_status_ascending_export_status_descending_and_function_ascending();
	}

}

