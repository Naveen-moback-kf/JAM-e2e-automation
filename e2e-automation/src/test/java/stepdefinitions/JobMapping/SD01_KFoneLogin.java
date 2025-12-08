package stepdefinitions.JobMapping;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kfonetalentsuite.manager.PageObjectManager;
import com.kfonetalentsuite.utils.JobMapping.PageObjectHelper;
import com.kfonetalentsuite.utils.JobMapping.Utilities;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

public class SD01_KFoneLogin {
	PageObjectManager kfoneLogin = new PageObjectManager();
	// WebDriver driver = DriverManager.getDriver();
	
	private static final Logger LOGGER = LogManager.getLogger(SD01_KFoneLogin.class);

	public SD01_KFoneLogin() {
		super();
	}

	@Given("Launch the KFONE application")
	public void launch_the_kfone_application() throws IOException {
		kfoneLogin.getKFoneLogin().launch_the_kfone_application();
	}

	@Then("Provide SSO Login username and click Sign in button in KFONE login page")
	public void provide_sso_login_username_and_click_sign_in_button_in_kfone_login_page() throws IOException {
		kfoneLogin.getKFoneLogin().provide_sso_login_username_and_click_sign_in_button_in_kfone_login_page();
	}

	@Then("User should navigate to Microsoft Login page")
	public void user_should_navigate_to_microsoft_login_page() throws IOException {
		kfoneLogin.getKFoneLogin().user_should_navigate_to_microsoft_login_page();
	}

	@Then("Provide SSO Login Password and click Sign In")
	public void provide_sso_login_password_and_click_sign_in() throws IOException {
		kfoneLogin.getKFoneLogin().provide_sso_login_password_and_click_sign_in();
	}

	@Then("Provide NON_SSO Login username and click Sign in button in KFONE login page")
	public void provide_non_sso_login_username_and_click_sign_in_button_in_kfone_login_page() throws IOException {
		kfoneLogin.getKFoneLogin().provide_non_sso_login_username_and_click_sign_in_button_in_kfone_login_page();
	}

	@Then("Provide NON_SSO Login password and click Sign in button in KFONE login page")
	public void provide_non_sso_login_password_and_click_sign_in_button_in_kfone_login_page() throws IOException {
		kfoneLogin.getKFoneLogin().provide_non_sso_login_password_and_click_sign_in_button_in_kfone_login_page();
	}

	@Then("Verify the KFONE landing page")
	public void verify_the_kfone_landing_page() throws IOException {
		kfoneLogin.getKFoneLogin().verify_the_kfone_landing_page();
	}

	@Given("User is in KFONE Clients page")
	public void user_is_in_kfone_clients_page() throws IOException {
		kfoneLogin.getKFoneLogin().user_is_in_kfone_clients_page();
	}

	@Then("Verify Products that client can acceess")
	public void verify_products_that_client_can_access() throws IOException {
		kfoneLogin.getKFoneLogin().verify_products_that_client_can_access();
	}

	@Then("Verify Client name based on PAMS ID")
	public void verify_client_name_based_on_pams_id() throws IOException {
		kfoneLogin.getKFoneLogin().verify_client_name_based_on_pams_id();
	}

	@Then("Search for Client with PAMS ID")
	public void search_for_client_with_pams_id() throws IOException {
		kfoneLogin.getKFoneLogin().search_for_client_with_pams_id();
	}

	@Then("Click on Client with access to Profile Manager Application")
	public void click_on_client_with_access_to_profile_manager_application() throws IOException {
		kfoneLogin.getKFoneLogin().click_on_client_with_access_to_profile_manager_application();
	}

	@Then("Verify User navigated to KFONE Home Page")
	public void verify_user_navigated_to_kfone_home_page() throws IOException {
		kfoneLogin.getKFoneLogin().verify_user_navigated_to_kfone_home_page();
	}

	@Then("Click on Profile Manager application in Your Products section")
	public void click_on_profile_manager_application_in_your_products_section() throws IOException {
		kfoneLogin.getKFoneLogin().click_on_profile_manager_application_in_your_products_section();
	}

	@Then("Verify User seemlessly landed on Profile Manager Application in KF HUB")
	public void verify_user_seemlessly_landed_on_profile_manager_application_in_kf_hub() throws IOException {
		kfoneLogin.getKFoneLogin().verify_user_seemlessly_landed_on_profile_manager_application_in_kf_hub();
	}

	// ===== USER ROLE VERIFICATION STEP DEFINITIONS =====

	@Then("Store user session details from session storage")
	public void store_user_session_details_from_session_storage() {
		boolean success = Utilities.setUserSessionDetailsFromSessionStorage();
		if (!success) {
			throw new AssertionError("Failed to retrieve and store user session details from session storage");
		}
	}

	@Then("Verify user role from session storage")
	public void verify_user_role_from_session_storage() {
		Utilities.verifyUserRole(null); // Just retrieve and log the role
	}

	@Then("Verify current user has role {string}")
	public void verify_current_user_has_role(String expectedRole) {
		boolean hasRole = Utilities.hasRole(expectedRole);
		if (!hasRole) {
			String currentRole = Utilities.getCurrentUserRole();
			throw new AssertionError(
					"User role mismatch. Expected: '" + expectedRole + "', Current: '" + currentRole + "'");
		}
	}

	@Then("Clear stored user role")
	public void clear_stored_user_role() {
		Utilities.clearCurrentUserRole();
	}

	@Then("Log current user role")
	public void log_current_user_role() {
		String currentRole = Utilities.getCurrentUserRole();
		if (currentRole != null) {
			PageObjectHelper.log(LOGGER," Current User Role: " + currentRole);
		} else {
			PageObjectHelper.log(LOGGER," No user role currently stored");
		}
	}

	// ===== CONDITIONAL EXECUTION BASED ON USER ROLE =====

	@Then("Skip scenario if user role is not {string}")
	public void skip_scenario_if_user_role_is_not(String requiredRole) {
		String currentRole = Utilities.getCurrentUserRole();

		// If role not stored globally, try to fetch from session storage
		if (currentRole == null) {
			currentRole = Utilities.getUserRoleFromSessionStorage();
		}

		if (currentRole == null) {
			throw new org.testng.SkipException(
					"SCENARIO SKIPPED: Could not retrieve user role from session storage. Required role: '"
							+ requiredRole + "'");
		}

		if (!currentRole.equals(requiredRole)) {
			throw new org.testng.SkipException("SCENARIO SKIPPED: Only users with role '" + requiredRole
					+ "' can validate this feature. Current user role: '" + currentRole + "'");
		}

		// If we reach here, user has the required role
		PageObjectHelper.log(LOGGER,"User role validation passed. Current role: '" + currentRole + "' matches required role: '"
				+ requiredRole + "'");
	}

	@Then("Skip scenario if user is not KF Super User")
	public void skip_scenario_if_user_is_not_kf_super_user() {
		skip_scenario_if_user_role_is_not("KF Super User");
	}

	// ===== CONDITIONAL EXECUTION BASED ON USER LEVEL PERMISSIONS =====

	@Then("Skip scenario if user does not have Job Mapping access")
	public void skip_scenario_if_user_does_not_have_job_mapping_access() {
		Boolean jobMappingEnabled = Utilities.isUserLevelJobMappingEnabled();

		// If not stored globally, the session details might not have been fetched
		if (jobMappingEnabled == null) {
			throw new org.testng.SkipException(
					"SCENARIO SKIPPED: Could not determine Job Mapping access. "
							+ "Ensure 'Store user session details from session storage' step is executed first.");
		}

		if (!jobMappingEnabled) {
			throw new org.testng.SkipException(
					"SCENARIO SKIPPED: User does not have Job Mapping access. "
							+ "userLevelJobMappingEnabled = false");
		}

		// If we reach here, user has Job Mapping access
		PageObjectHelper.log(LOGGER,"Job Mapping access validation passed. userLevelJobMappingEnabled = true");
	}

	@Then("Skip scenario if user does not have HCM Sync access")
	public void skip_scenario_if_user_does_not_have_hcm_sync_access() {
		String userLevelPermission = Utilities.getUserLevelPermission();

		// If not stored globally, the session details might not have been fetched
		if (userLevelPermission == null) {
			throw new org.testng.SkipException(
					"SCENARIO SKIPPED: Could not determine HCM Sync access. "
							+ "Ensure 'Store user session details from session storage' step is executed first.");
		}

		if (!Utilities.hasHCMSyncAccess()) {
			throw new org.testng.SkipException(
					"SCENARIO SKIPPED: User does not have HCM Sync Profiles access. "
							+ "userLevelPermission = '" + userLevelPermission + "'");
		}

		// If we reach here, user has HCM Sync access
		PageObjectHelper.log(LOGGER,"HCM Sync access validation passed. userLevelPermission = 'true'");
	}
}
