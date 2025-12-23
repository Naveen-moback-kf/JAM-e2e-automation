package stepdefinitions.JobMapping;

import java.io.IOException;

import com.kfonetalentsuite.manager.PageObjectManager;

import io.cucumber.java.en.Then;

public class SD08_JobMappingFilters {
	PageObjectManager validateJobMappingFiltersFunctionality = new PageObjectManager();
	
	public SD08_JobMappingFilters() {
		super();		
	}
	
	@Then("Click on Grades Filters dropdown button")
	public void click_on_grades_filters_dropdown_button() throws IOException {
		validateJobMappingFiltersFunctionality.getJobMappingFilters().click_on_grades_filters_dropdown_button();
	}

	@Then("Select one option in Grades Filters dropdown")
	public void select_one_option_in_grades_filters_dropdown() throws Exception {
		validateJobMappingFiltersFunctionality.getJobMappingFilters().select_one_option_in_grades_filters_dropdown();
	}

	@Then("Validate Job Mapping Profiles are correctly filtered with applied Grades Options")
	public void validate_job_mapping_profiles_are_correctly_filtered_with_applied_grades_options() throws Exception {
		validateJobMappingFiltersFunctionality.getJobMappingFilters().validate_job_mapping_profiles_are_correctly_filtered_with_applied_grades_options();
	}
	
	@Then("Click on clear\\(x) applied filter")
	public void click_on_clear_x_applied_filter() throws InterruptedException, Exception {
		validateJobMappingFiltersFunctionality.getJobMappingFilters().click_on_clear_x_applied_filter();
	}
	
	@Then("User should scroll down to view last result with applied filters")
	public void user_should_scroll_down_to_view_last_result_with_applied_filters() throws IOException, InterruptedException {
		validateJobMappingFiltersFunctionality.getJobMappingFilters().user_should_scroll_down_to_view_last_result_with_applied_filters();
	}

	@Then("Select two options in Grades Filters dropdown")
	public void select_two_options_in_grades_filters_dropdown() throws InterruptedException, Exception {
		validateJobMappingFiltersFunctionality.getJobMappingFilters().select_two_options_in_grades_filters_dropdown();
	}

	@Then("Select different option in Grades Filters dropdown")
	public void select_different_option_in_grades_filters_dropdown() throws Exception {
		validateJobMappingFiltersFunctionality.getJobMappingFilters().select_different_option_in_grades_filters_dropdown();
	}

	@Then("Click on Clear Filters button")
	public void click_on_clear_filters_button() throws InterruptedException, Exception {
		validateJobMappingFiltersFunctionality.getJobMappingFilters().click_on_clear_filters_button();
	}
	
	@Then("Click on Departments Filters dropdown button")
	public void click_on_departments_filters_dropdown_button() throws InterruptedException, Exception {
	    validateJobMappingFiltersFunctionality.getJobMappingFilters().click_on_departments_filters_dropdown_button();
	}

	@Then("Select one option in Departments Filters dropdown")
	public void select_one_option_in_departments_filters_dropdown() throws InterruptedException, Exception {
		validateJobMappingFiltersFunctionality.getJobMappingFilters().select_one_option_in_departments_filters_dropdown();
	}

	@Then("Validate Job Mapping Profiles are correctly filtered with applied Departments Options")
	public void validate_job_mapping_profiles_are_correctly_filtered_with_applied_departments_options() throws Exception {
		validateJobMappingFiltersFunctionality.getJobMappingFilters().validate_job_mapping_profiles_are_correctly_filtered_with_applied_departments_options();
	}

	@Then("Select two options in Departments Filters dropdown")
	public void select_two_options_in_departments_filters_dropdown() throws InterruptedException, Exception {
		validateJobMappingFiltersFunctionality.getJobMappingFilters().select_two_options_in_departments_filters_dropdown();
	}
	
	@Then("Click on Functions Subfunctions Filters dropdown button")
	public void click_on_functions_subfunctions_filters_dropdown_button() throws InterruptedException, Exception {
		validateJobMappingFiltersFunctionality.getJobMappingFilters().click_on_functions_subfunctions_filters_dropdown_button();
	}

	@Then("Select a Function and verify all Subfunctions inside Function are selected automatically")
	public void select_a_function_and_verify_all_subfunctions_inside_function_are_selected_automatically() throws InterruptedException, Exception {
		validateJobMappingFiltersFunctionality.getJobMappingFilters().select_a_function_and_verify_all_subfunctions_inside_function_are_selected_automatically();
	}

	@Then("Validate Job Mapping Profiles are correctly filtered with applied Functions Subfunctions Options")
	public void validate_job_mapping_profiles_are_correctly_filtered_with_applied_functions_subfunctions_options() throws Exception {
		validateJobMappingFiltersFunctionality.getJobMappingFilters().validate_job_mapping_profiles_are_correctly_filtered_with_applied_functions_subfunctions_options();
	}

	@Then("User should verify Search bar is available in Functions Subfunctions Filters dropdown")
	public void user_should_verify_search_bar_is_available_in_functions_subfunctions_filters_dropdown() throws Exception {
		validateJobMappingFiltersFunctionality.getJobMappingFilters().user_should_verify_search_bar_is_available_in_functions_subfunctions_filters_dropdown();
	}

	@Then("Click inside search bar and enter function name")
	public void click_inside_search_bar_and_enter_function_name() throws Exception {
		validateJobMappingFiltersFunctionality.getJobMappingFilters().click_inside_search_bar_and_enter_function_name();
	}

	@Then("User should click on dropdown button of Searched function name")
	public void user_should_click_on_dropdown_button_of_searched_function_name() throws InterruptedException, Exception {
		validateJobMappingFiltersFunctionality.getJobMappingFilters().user_should_click_on_dropdown_button_of_searched_function_name();
	}

	@Then("Select one Subfunction option inside Function Name dropdown")
	public void select_one_subfunction_option_inside_function_name_dropdown() throws InterruptedException, Exception {
		validateJobMappingFiltersFunctionality.getJobMappingFilters().select_one_subfunction_option_inside_function_name_dropdown();
	}

	@Then("User should verify Function Name is automatically selected after selecting Subfunction option")
	public void user_should_verify_function_name_is_automatically_selected_after_selecting_subfunction_option() throws Exception {
		validateJobMappingFiltersFunctionality.getJobMappingFilters().user_should_verify_function_name_is_automatically_selected_after_selecting_subfunction_option();
	}

	@Then("Select two subfunction options inside Function Name Filters dropdown")
	public void select_two_subfunction_options_inside_function_name_filters_dropdown() throws InterruptedException, Exception {
		validateJobMappingFiltersFunctionality.getJobMappingFilters().select_two_subfunction_options_inside_function_name_filters_dropdown();
	}
	
	@Then("Validate Job Mapping Profiles are correctly filtered with applied Grades Departments and Functions Subfunctions Options")
	public void validate_job_mapping_profiles_are_correctly_filtered_with_applied_grades_departments_and_functions_subfunctions_options() throws Exception {
		validateJobMappingFiltersFunctionality.getJobMappingFilters().validate_job_mapping_profiles_are_correctly_filtered_with_applied_grades_departments_and_functions_subfunctions_options();
	}
	
	@Then("Click on Mapping Status Filters dropdown button")
	public void click_on_mapping_status_filters_dropdown_button() throws Exception {
		validateJobMappingFiltersFunctionality.getJobMappingFilters().click_on_mapping_status_filters_dropdown_button();
	}

	@Then("Select one option in Mapping Status Filters dropdown")
	public void select_one_option_in_mapping_status_filters_dropdown() throws Exception {
		validateJobMappingFiltersFunctionality.getJobMappingFilters().select_one_option_in_mapping_status_filters_dropdown();
	}

	// ═══════════════════════════════════════════════════════════════════════════
	// CONSOLIDATED/PARAMETERIZED STEP DEFINITIONS - For unified filter handling
	// ═══════════════════════════════════════════════════════════════════════════

	@Then("Click on {string} Filters dropdown button")
	public void click_on_filter_dropdown_button(String filterType) throws Exception {
		validateJobMappingFiltersFunctionality.getJobMappingFilters().click_on_filter_dropdown_button(filterType);
	}

	@Then("Select one option in {string} Filters dropdown")
	public void select_one_option_in_filter_dropdown(String filterType) throws Exception {
		validateJobMappingFiltersFunctionality.getJobMappingFilters().select_one_option_in_filter_dropdown(filterType);
	}

	@Then("Select two options in {string} Filters dropdown")
	public void select_two_options_in_filter_dropdown(String filterType) throws Exception {
		validateJobMappingFiltersFunctionality.getJobMappingFilters().select_two_options_in_filter_dropdown(filterType);
	}

	@Then("Validate Job Mapping Profiles are correctly filtered with applied {string} Options")
	public void validate_filter_results(String filterType) throws Exception {
		validateJobMappingFiltersFunctionality.getJobMappingFilters().validate_filter_results(filterType);
	}

	@Then("Validate Job Mapping Profiles are correctly filtered with applied Grades Departments and FunctionsSubfunctions Options")
	public void validate_job_mapping_profiles_are_correctly_filtered_with_applied_grades_departments_and_functionssubfunctions_options() throws Exception {
		validateJobMappingFiltersFunctionality.getJobMappingFilters().validate_job_mapping_profiles_are_correctly_filtered_with_applied_grades_departments_and_functions_subfunctions_options();
	}
	
}
