package stepdefinitions.JobMapping;

import java.io.IOException;

import com.kfonetalentsuite.manager.PageObjectManager;

import io.cucumber.java.en.Then;

public class SD33_UnmappedJobs_JAM {	
	public SD33_UnmappedJobs_JAM() {
		super();		
	}
	
	@Then("Select Unmapped jobs option in Mapping Status Filters dropdown")
	public void select_unmapped_jobs_option_in_mapping_status_filters_dropdown() throws IOException {
		PageObjectManager.getInstance().getUnmappedJobs_JAM().select_unmapped_jobs_option_in_mapping_status_filters_dropdown();
	}
	
	@Then("Verify Header Checkbox is disabled in Job Mapping screen")
	public void verify_header_checkbox_is_disabled_in_job_mapping_screen() throws IOException {
		PageObjectManager.getInstance().getUnmappedJobs_JAM().verify_header_checkbox_is_disabled_in_job_mapping_screen();
	}
	
	@Then("Verify Chevron button is disabled in Job Mapping screen")
	public void verify_chevron_button_is_disabled_in_job_mapping_screen() throws IOException {
		PageObjectManager.getInstance().getUnmappedJobs_JAM().verify_chevron_button_is_disabled_in_job_mapping_screen();
	}
	
	@Then("Verify Checkbox of all Unmapped Jobs is disabled with ToolTip")
	public void verify_checkbox_of_all_unmapped_jobs_is_disabled_with_tooltip() throws IOException {
		PageObjectManager.getInstance().getUnmappedJobs_JAM().verify_checkbox_of_all_unmapped_jobs_is_disabled_with_tooltip();
	}

}


