package stepdefinitions.JobMapping;

import com.kfonetalentsuite.manager.PageObjectManager;
import com.kfonetalentsuite.webdriverManager.DriverManager;

import io.cucumber.java.en.Then;

public class SD32_ClearProfileSelectionFunctionality extends DriverManager {
	
	private PageObjectManager pageObjectManager = new PageObjectManager();
	
	public SD32_ClearProfileSelectionFunctionality() {
		super();
	}

	// ═══════════════════════════════════════════════════════════════════════════
	// HEADER CHECKBOX STEPS - Parameterized
	// ═══════════════════════════════════════════════════════════════════════════

	@Then("Click on header checkbox to select loaded job profiles in {string} screen")
	public void click_on_header_checkbox_to_select_loaded_job_profiles(String screen) throws Exception {
		pageObjectManager.getClearProfileSelectionFunctionality()
			.click_on_header_checkbox_to_select_loaded_job_profiles(screen);
	}

	@Then("Click on header checkbox to Unselect loaded job profiles in {string} screen")
	public void click_on_header_checkbox_to_unselect_loaded_job_profiles(String screen) throws Exception {
		pageObjectManager.getClearProfileSelectionFunctionality()
			.click_on_header_checkbox_to_unselect_loaded_job_profiles(screen);
	}

	// ═══════════════════════════════════════════════════════════════════════════
	// NONE BUTTON STEPS - Parameterized
	// ═══════════════════════════════════════════════════════════════════════════

	@Then("Click on None button in {string} screen")
	public void click_on_none_button(String screen) throws Exception {
		pageObjectManager.getClearProfileSelectionFunctionality().click_on_none_button(screen);
	}

	// ═══════════════════════════════════════════════════════════════════════════
	// VERIFICATION STEPS - Parameterized
	// ═══════════════════════════════════════════════════════════════════════════

	@Then("Verify Loaded Profiles are unselected in {string} screen")
	public void verify_loaded_profiles_are_unselected(String screen) throws Exception {
		pageObjectManager.getClearProfileSelectionFunctionality()
			.verify_loaded_profiles_are_unselected(screen);
	}

	@Then("Verify newly loaded profiles are still Selected in {string} screen")
	public void verify_newly_loaded_profiles_are_still_selected(String screen) throws Exception {
		pageObjectManager.getClearProfileSelectionFunctionality()
			.verify_newly_loaded_profiles_are_still_selected(screen);
	}

	@Then("Verify all Loaded Profiles are Unselected in {string} screen")
	public void verify_all_loaded_profiles_are_unselected(String screen) throws Exception {
		pageObjectManager.getClearProfileSelectionFunctionality()
			.verify_all_loaded_profiles_are_unselected(screen);
	}

	@Then("Verify Profiles loaded after clicking Header checkbox are not selected in {string} screen")
	public void verify_profiles_loaded_after_header_checkbox_are_not_selected(String screen) throws Exception {
		pageObjectManager.getClearProfileSelectionFunctionality()
			.verify_profiles_loaded_after_header_checkbox_are_not_selected(screen);
	}

	// ═══════════════════════════════════════════════════════════════════════════
	// SCROLL AND REFRESH STEPS - Parameterized
	// ═══════════════════════════════════════════════════════════════════════════

	@Then("Scroll page to view more job profiles in {string} screen")
	public void scroll_page_to_view_more_job_profiles(String screen) throws Exception {
		pageObjectManager.getClearProfileSelectionFunctionality()
			.scroll_page_to_view_more_job_profiles(screen);
	}

	@Then("Refresh {string} screen")
	public void refresh_screen(String screen) throws Exception {
		pageObjectManager.getClearProfileSelectionFunctionality().refresh_screen(screen);
	}

	// ═══════════════════════════════════════════════════════════════════════════
	// ACTION BUTTON VERIFICATION STEPS - Parameterized
	// ═══════════════════════════════════════════════════════════════════════════

	@Then("User should verify action button is enabled in {string} screen")
	public void verify_action_button_is_enabled(String screen) throws Exception {
		pageObjectManager.getClearProfileSelectionFunctionality().verify_action_button_is_enabled(screen);
	}

	@Then("User should verify action button is disabled in {string} screen")
	public void verify_action_button_is_disabled(String screen) throws Exception {
		pageObjectManager.getClearProfileSelectionFunctionality().verify_action_button_is_disabled(screen);
	}
}

