package stepdefinitions.functional.JobMapping;

import java.io.IOException;

import com.JobMapping.manager.PageObjectManager;

import io.cucumber.java.en.Then;

public class SD20_ManualMappingofSPinAutoAI {
PageObjectManager manualMappingofSPinAutoAI = new PageObjectManager();
	
	public SD20_ManualMappingofSPinAutoAI() {
		super();		
	}
	
	@Then("Verify Profile with No BIC Mapping is displaying on Top after sorting")
	public void verify_profile_with_no_bic_mapping_is_displaying_on_top_after_sorting() throws InterruptedException, IOException {
		manualMappingofSPinAutoAI.getManualMappingofSPinAutoAI().verify_profile_with_no_bic_mapping_is_displaying_on_top_after_sorting();
	}
	
	@Then("Click on Find Match button")
	public void click_on_find_match_button() throws IOException {
		manualMappingofSPinAutoAI.getManualMappingofSPinAutoAI().click_on_find_match_button();
	}

	@Then("User should verify Search a Different Profile button is displaying on manually mapped success profile")
	public void user_should_verify_search_a_different_profile_button_is_displaying_on_manually_mapped_success_profile() throws IOException {
		manualMappingofSPinAutoAI.getManualMappingofSPinAutoAI().user_should_verify_search_a_different_profile_button_is_displaying_on_manually_mapped_success_profile();
	}
	
	@Then("Click on manually mapped profile name of Job Profile on Top of Profiles List")
	public void click_on_manually_mapped_profile_name_of_job_profile_on_top_of_profiles_list() throws IOException {
		manualMappingofSPinAutoAI.getManualMappingofSPinAutoAI().click_on_manually_mapped_profile_name_of_job_profile_on_top_of_profiles_list();
	}

}
