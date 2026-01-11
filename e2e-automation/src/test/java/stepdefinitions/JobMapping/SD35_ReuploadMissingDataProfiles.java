package stepdefinitions.JobMapping;

import java.io.IOException;

import com.kfonetalentsuite.manager.PageObjectManager;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SD35_ReuploadMissingDataProfiles {	
	public SD35_ReuploadMissingDataProfiles() {
		super();
	}

	@Given("Skip scenario if Missing Data Tip Message is not displayed")
	public void skip_scenario_if_missing_data_tip_message_is_not_displayed() throws IOException {
		PageObjectManager.getInstance().getMissingDataTipMessage().skipScenarioIfMissingDataTipMessageNotDisplayed();
	}

	@Then("Verify Re-upload button is displayed on Jobs Missing Data screen")
	public void verify_reupload_button_is_displayed_on_jobs_missing_data_screen() throws IOException {
		PageObjectManager.getInstance().getReuploadMissingDataProfiles().verify_reupload_button_is_displayed_on_jobs_missing_data_screen();
	}

	@When("Click on Re-upload button in Jobs Missing Data screen")
	public void click_on_reupload_button_in_jobs_missing_data_screen() throws IOException {
		PageObjectManager.getInstance().getReuploadMissingDataProfiles().click_on_reupload_button_in_jobs_missing_data_screen();
	}

	@When("User is in Jobs Missing Data screen")
	public void user_is_in_jobs_missing_data_screen() throws IOException {
		PageObjectManager.getInstance().getReuploadMissingDataProfiles().verify_reupload_button_is_displayed_on_jobs_missing_data_screen();
	}

	@Then("Capture total count of profiles in Jobs Missing Data screen")
	public void capture_total_count_of_profiles_in_jobs_missing_data_screen() throws IOException {
		PageObjectManager.getInstance().getReuploadMissingDataProfiles().capture_total_count_of_profiles_in_jobs_missing_data_screen();
	}

	@And("Extract details of profiles from Jobs Missing Data screen up to 10 or all if less")
	public void extract_details_of_profiles_from_jobs_missing_data_screen_up_to_10_or_all() throws IOException {
		PageObjectManager.getInstance().getReuploadMissingDataProfiles().extract_details_of_top_10_profiles_from_jobs_missing_data_screen();
	}

	@Then("Create CSV file with extracted profiles in Job Catalog format")
	public void create_csv_file_with_extracted_profiles_in_job_catalog_format() throws IOException {
		PageObjectManager.getInstance().getReuploadMissingDataProfiles().create_csv_file_with_extracted_profiles_in_job_catalog_format();
	}

	@And("Save CSV file as {string} in test resources folder")
	public void save_csv_file_as_in_test_resources_folder(String fileName) throws IOException {
		PageObjectManager.getInstance().getReuploadMissingDataProfiles().save_csv_file_as_in_test_resources_folder(fileName);
	}

	@And("Verify CSV file is created successfully with correct headers")
	public void verify_csv_file_is_created_successfully_with_correct_headers() throws IOException {
		PageObjectManager.getInstance().getReuploadMissingDataProfiles().verify_csv_file_is_created_successfully_with_correct_headers();
	}

	@And("Verify CSV file contains extracted profile data")
	public void verify_csv_file_contains_extracted_profile_data() throws IOException {
		PageObjectManager.getInstance().getReuploadMissingDataProfiles().verify_csv_file_contains_extracted_profile_data();
	}

	@Given("CSV file with missing data profiles exists")
	public void csv_file_with_missing_data_profiles_exists() throws IOException {
		PageObjectManager.getInstance().getReuploadMissingDataProfiles().csv_file_with_missing_data_profiles_exists();
	}

	@Then("Read the exported CSV file with missing data profiles")
	public void read_the_exported_csv_file_with_missing_data_profiles() throws IOException {
		PageObjectManager.getInstance().getReuploadMissingDataProfiles().read_the_exported_csv_file_with_missing_data_profiles();
	}

	@And("Identify profiles with missing Grade value in CSV")
	public void identify_profiles_with_missing_grade_value_in_csv() throws IOException {
		PageObjectManager.getInstance().getReuploadMissingDataProfiles().identify_profiles_with_missing_grade_value_in_csv();
	}

	@And("Identify profiles with missing Department value in CSV")
	public void identify_profiles_with_missing_department_value_in_csv() throws IOException {
		PageObjectManager.getInstance().getReuploadMissingDataProfiles().identify_profiles_with_missing_department_value_in_csv();
	}

	@And("Identify all profiles with any missing values in CSV")
	public void identify_all_profiles_with_any_missing_values_in_csv() throws IOException {
		PageObjectManager.getInstance().getReuploadMissingDataProfiles().identify_all_profiles_with_any_missing_values_in_csv();
	}

	@Then("Fill missing Grade values with appropriate grade codes")
	public void fill_missing_grade_values_with_appropriate_grade_codes() throws IOException {
		PageObjectManager.getInstance().getReuploadMissingDataProfiles().fill_missing_grade_values_with_default_grade("JGL01");
	}

	@Then("Fill missing Grade values with default grade JGL01")
	public void fill_missing_grade_values_with_default_grade_jgl01() throws IOException {
		PageObjectManager.getInstance().getReuploadMissingDataProfiles().fill_missing_grade_values_with_default_grade("JGL01");
	}

	@Then("Fill missing Department values with appropriate department names")
	public void fill_missing_department_values_with_appropriate_department_names() throws IOException {
		PageObjectManager.getInstance().getReuploadMissingDataProfiles().fill_missing_department_values_with_default_department("Engineering");
	}

	@And("Fill missing Department values with default department Engineering")
	public void fill_missing_department_values_with_default_department_engineering() throws IOException {
		PageObjectManager.getInstance().getReuploadMissingDataProfiles().fill_missing_department_values_with_default_department("Engineering");
	}

	@And("Fill missing Job Family values with default value General")
	public void fill_missing_job_family_values_with_default_value_general() throws IOException {
		PageObjectManager.getInstance().getReuploadMissingDataProfiles().fill_missing_job_family_values_with_default_value("General");
	}

	@And("Fill missing Job Sub Family values with default value Operations")
	public void fill_missing_job_sub_family_values_with_default_value_operations() throws IOException {
		PageObjectManager.getInstance().getReuploadMissingDataProfiles().fill_missing_job_sub_family_values_with_default_value("Operations");
	}

	@And("Save the updated CSV file with filled Grade data")
	public void save_the_updated_csv_file_with_filled_grade_data() throws IOException {
		PageObjectManager.getInstance().getReuploadMissingDataProfiles().save_the_updated_csv_file_with_filled_data();
	}

	@And("Save the updated CSV file with filled Department data")
	public void save_the_updated_csv_file_with_filled_department_data() throws IOException {
		PageObjectManager.getInstance().getReuploadMissingDataProfiles().save_the_updated_csv_file_with_filled_data();
	}

	@And("Save the updated CSV file with all missing data filled")
	public void save_the_updated_csv_file_with_all_missing_data_filled() throws IOException {
		PageObjectManager.getInstance().getReuploadMissingDataProfiles().save_the_updated_csv_file_with_filled_data();
	}

	@And("Verify updated CSV file has no empty Grade values")
	public void verify_updated_csv_file_has_no_empty_grade_values() throws IOException {
		PageObjectManager.getInstance().getReuploadMissingDataProfiles().verify_updated_csv_file_has_no_empty_grade_values();
	}

	@And("Verify updated CSV file has no empty Department values")
	public void verify_updated_csv_file_has_no_empty_department_values() throws IOException {
		PageObjectManager.getInstance().getReuploadMissingDataProfiles().verify_updated_csv_file_has_no_empty_department_values();
	}

	@And("Verify updated CSV file has no empty required fields")
	public void verify_updated_csv_file_has_no_empty_required_fields() throws IOException {
		PageObjectManager.getInstance().getReuploadMissingDataProfiles().verify_updated_csv_file_has_no_empty_required_fields();
	}

	@Then("Capture the count of jobs with missing data before re-upload")
	public void capture_the_count_of_jobs_with_missing_data_before_reupload() throws IOException {
		PageObjectManager.getInstance().getReuploadMissingDataProfiles().capture_the_count_of_jobs_with_missing_data_before_reupload();
	}

	@Then("Capture Total Results count before re-upload")
	public void capture_total_results_count_before_reupload() throws IOException {
		PageObjectManager.getInstance().getReuploadMissingDataProfiles().capture_total_results_count_before_reupload();
	}

	@And("Capture the count of jobs with missing data after re-upload")
	public void capture_the_count_of_jobs_with_missing_data_after_reupload() throws IOException {
		PageObjectManager.getInstance().getReuploadMissingDataProfiles().capture_the_count_of_jobs_with_missing_data_after_reupload();
	}

	@Then("Capture Total Results count after re-upload")
	public void capture_total_results_count_after_reupload() throws IOException {
		PageObjectManager.getInstance().getReuploadMissingDataProfiles().capture_total_results_count_after_reupload();
	}

	@Then("Verify missing data count has decreased compared to before re-upload")
	public void verify_missing_data_count_has_decreased_compared_to_before_reupload() throws IOException {
		PageObjectManager.getInstance().getReuploadMissingDataProfiles().verify_missing_data_count_has_decreased_compared_to_before_reupload();
	}

	@Then("Verify Total Results count remains unchanged after re-upload")
	public void verify_total_results_count_remains_unchanged() throws IOException {
		PageObjectManager.getInstance().getReuploadMissingDataProfiles().verify_total_results_count_remains_unchanged();
	}

	@Then("Wait for backend processing and refresh Job Mapping page")
	public void wait_for_backend_processing_and_refresh_job_mapping_page() throws IOException {
		PageObjectManager.getInstance().getReuploadMissingDataProfiles().wait_and_refresh_job_mapping_page_after_upload();
	}

	@Then("Search for first re-uploaded profile by Job Name from CSV")
	public void search_for_first_reuploaded_profile_by_job_name_from_csv() throws IOException {
		PageObjectManager.getInstance().getReuploadMissingDataProfiles().search_for_first_reuploaded_profile_by_job_name_from_csv();
	}

	@Then("Search for first re-uploaded profile by Job Code in Missing Data screen")
	public void search_for_first_reuploaded_profile_by_job_code_in_missing_data_screen() throws IOException {
		PageObjectManager.getInstance().getReuploadMissingDataProfiles().search_for_first_reuploaded_profile_by_job_code_in_missing_data_screen();
	}

	@And("Verify profile is found in Job Mapping search results")
	public void verify_profile_is_found_in_job_mapping_search_results() throws IOException {
		PageObjectManager.getInstance().getReuploadMissingDataProfiles().verify_profile_is_found_in_job_mapping_search_results();
	}

	@Then("Verify profile does NOT display Missing Data info icon")
	public void verify_profile_does_not_display_missing_data_info_icon() throws IOException {
		PageObjectManager.getInstance().getReuploadMissingDataProfiles().verify_profile_does_not_display_missing_data_info_icon();
	}

	@And("Verify profile displays the corrected data values")
	public void verify_profile_displays_the_corrected_data_values() throws IOException {
		PageObjectManager.getInstance().getReuploadMissingDataProfiles().verify_profile_displays_the_corrected_data_values();
	}

	@And("Verify profile is NOT found in Jobs Missing Data screen")
	public void verify_profile_is_not_found_in_jobs_missing_data_screen() throws IOException {
		PageObjectManager.getInstance().getReuploadMissingDataProfiles().verify_profile_is_not_found_in_jobs_missing_data_screen();
	}

	@Then("Upload generated CSV file using Browse Files button")
	public void upload_generated_csv_file_using_browse_files_button() throws IOException {
		PageObjectManager.getInstance().getReuploadMissingDataProfiles().upload_generated_csv_file();
	}
}

