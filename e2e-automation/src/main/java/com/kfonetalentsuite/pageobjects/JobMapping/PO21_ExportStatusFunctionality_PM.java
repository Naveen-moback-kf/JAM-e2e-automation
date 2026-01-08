package com.kfonetalentsuite.pageobjects.JobMapping;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.testng.Assert;
import com.kfonetalentsuite.utils.JobMapping.PageObjectHelper;

public class PO21_ExportStatusFunctionality_PM extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO21_ExportStatusFunctionality_PM.class);

	public static ThreadLocal<Integer> rowNumber = ThreadLocal.withInitial(() -> 0);
	public static ThreadLocal<String> SPJobName = ThreadLocal.withInitial(() -> "NOT_SET");

	public PO21_ExportStatusFunctionality_PM() {
		super();
	}

	private static final By THREE_DOTS_SP_DETAILS = By.xpath("//kf-icon[contains(@class,'dots-three')]");
	private static final By EDIT_SP_BUTTON = By.xpath("//*[contains(text(),'Edit Success')]");
	private static final By EDIT_DETAILS_BTN = By.xpath("//*[contains(@class,'editDetails')]");
	private static final By FUNCTION_DROPDOWN = By.xpath("//label[contains(text(),'Function')]//..//..//button");
	private static final By SUBFUNCTION_DROPDOWN = By.xpath("//label[contains(text(),'Subfunction')]//..//..//button");
	private static final By DONE_BTN = By.xpath("//*[contains(text(),'Done')]");
	private static final By SAVE_BTN = By.xpath("//button[text()='Save']");
	private static final By DROPDOWN_OPTIONS = By.xpath("//kf-dropdown-item//div//span");

	private WebElement findTableElement(int rowNum, int colNum) {
		String xpath = String.format("//tbody//tr[%d]//td[%d]//*", rowNum, colNum);
		try {
			return PageObjectHelper.waitForPresent(wait, By.xpath(xpath));
		} catch (Exception e) {
			waitForSpinners();
			safeSleep(1000);
				return driver.findElement(By.xpath(xpath));
		}
	}

	public void user_should_search_for_a_profile_with_export_status_as_not_exported() {
		try {
			waitForSpinners();

			for (int i = 1; i <= 1000; i++) {
				rowNumber.set(i);

				try {
					WebElement exportStatus = findExportStatusElement(i);
					scrollToElement(exportStatus);

					String statusText = exportStatus.getText();
					if (statusText.contains("Not Exported")) {
						PageObjectHelper.log(LOGGER, "Job profile with export Status as Not Exported is found");

						if (isProfileExportable(i)) {
							break;
						} else {
							PageObjectHelper.log(LOGGER,
									"But Success profile with No Job Code assigned cannot be Exported...So Searching for another Profile....");
						}
					}
				} catch (NoSuchElementException e) {
					LOGGER.debug("Row {} not accessible, continuing search", i);
					continue;
				} catch (StaleElementReferenceException e) {
					LOGGER.warn("Stale element encountered at row {}, retrying once", i);
					waitForSpinners();
					safeSleep(1000);
					i--;
					continue;
				}
			}
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_search_for_a_profile_with_export_status_as_not_exported",
					"Profile search operation", e);
		}
	}

	private WebElement findExportStatusElement(int rowNum) {
		String xpath = String.format("//tbody//tr[%d]//td[9]//span", rowNum);
		try {
			return driver.findElement(By.xpath(xpath));
		} catch (Exception e) {
			waitForSpinners();
			safeSleep(1000);
			return driver.findElement(By.xpath(xpath));
		}
	}

	private boolean isProfileExportable(int rowNum) {
		try {
			String checkboxXpath = String.format("//tbody//tr[%d]//td[1]//*//..//div//kf-checkbox//div", rowNum);
			WebElement checkbox = driver.findElement(By.xpath(checkboxXpath));
			scrollToElement(checkbox);
			String attributeValue = checkbox.getAttribute("class");
			return !attributeValue.contains("disable");
		} catch (Exception e) {
			return false;
		}
	}

	public void verify_details_of_the_not_exported_success_profile_in_hcm_sync_profiles_tab() {
		try {
			WebElement jobNameElement = findTableElement(rowNumber.get(), 1);
			scrollToElement(jobNameElement);
			SPJobName.set(jobNameElement.getText());

			StringBuilder profileDetails = buildProfileDetailsString();

			String message = "Below are the details of the Not Exported Success Profile in HCM Sync Profiles screen in PM : \n"
					+ profileDetails.toString();
			PageObjectHelper.log(LOGGER, message);
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"verify_details_of_the_not_exported_success_profile_in_hcm_sync_profiles_tab",
					"Profile details operation", e);
		}
	}

	private StringBuilder buildProfileDetailsString() {
		StringBuilder details = new StringBuilder();
		By[] headers = { 
				Locators.HCMSyncProfiles.TABLE_HEADER_NAME, 
				Locators.HCMSyncProfiles.TABLE_HEADER_STATUS, 
				Locators.HCMSyncProfiles.TABLE_HEADER_JOB_CODE, 
				Locators.HCMSyncProfiles.TABLE_HEADER_KF_GRADE, 
				Locators.HCMSyncProfiles.TABLE_HEADER_LEVEL, 
				Locators.HCMSyncProfiles.TABLE_HEADER_FUNCTION,
				Locators.HCMSyncProfiles.TABLE_HEADER_CREATED_BY, 
				Locators.HCMSyncProfiles.TABLE_HEADER_LAST_MODIFIED, 
				Locators.HCMSyncProfiles.TABLE_HEADER_EXPORT_STATUS 
		};

		for (int col = 1; col <= 8; col++) {
			WebElement dataElement = findTableElement(rowNumber.get(), col);
			String headerText = getElementText(headers[col - 1]).replaceAll("\\s+[^\\w\\s]+$", "");
			details.append(headerText).append(" : ").append(dataElement.getText()).append("   ");
		}
		return details;
	}

	public void verify_success_profile_checkbox_is_enabled_and_able_to_perform_export_operation() {
		try {
			String checkboxXpath = String.format("//tbody//tr[%d]//td[1]//*//..//div//kf-checkbox", rowNumber.get());
			WebElement checkbox = waitForElement(By.xpath(checkboxXpath));
			scrollToElement(checkbox);

			if (!checkbox.isDisplayed()) {
				throw new AssertionError("Checkbox is not displayed for profile: " + SPJobName.get());
			}

			if (!checkbox.isEnabled()) {
				throw new AssertionError(
						"Checkbox is disabled for profile: " + SPJobName.get() + ". This profile cannot be exported.");
			}

			WebElement clickable = waitForClickable(By.xpath(checkboxXpath));
			Assert.assertTrue(clickable.isEnabled());

			PageObjectHelper.log(LOGGER, "Checkbox of the SP with Name " + SPJobName.get()
					+ " is Enabled and able to Perform Export Operation");
		} catch (AssertionError e) {
			String errorMsg = "Checkbox verification for profile: " + SPJobName.get();
			LOGGER.error(errorMsg, e);
			throw new RuntimeException(errorMsg, e);
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER,
					"verify_success_profile_checkbox_is_enabled_and_able_to_perform_export_operation",
					"Checkbox operation for profile: " + SPJobName.get(), e);
		}
	}

	public void click_on_checkbox_of_success_profile_with_export_status_as_not_exported() {
		try {
			String checkboxXpath = String.format("//tbody//tr[%d]//td[1]//*//..//div//kf-checkbox", rowNumber.get());
			By checkboxLocator = By.xpath(checkboxXpath);
			WebElement checkbox = waitForClickable(checkboxLocator);

			if (!checkbox.isEnabled()) {
				LOGGER.warn("Checkbox is disabled for profile: {}", SPJobName.get());
				throw new IllegalStateException("Checkbox is disabled for profile: " + SPJobName.get());
			}

			// Check if already selected - skip if yes
			try {
				driver.findElement(By.xpath(String.format("//tbody//tr[%d]//td[1]//kf-checkbox//kf-icon[@icon='checkbox-check']", rowNumber.get())));
				PO18_HCMSyncProfilesTab_PM.profilesCount.set(1);
				PageObjectHelper.log(LOGGER, "Checkbox already selected for Success profile with name: " + SPJobName.get());
				return;
			} catch (NoSuchElementException e) {
				// Not selected, proceed to click
			}

			clickElement(checkboxLocator);
			safeSleep(200);
			
			// Only set count if checkbox is actually selected after clicking
			try {
				driver.findElement(By.xpath(String.format("//tbody//tr[%d]//td[1]//kf-checkbox//kf-icon[@icon='checkbox-check']", rowNumber.get())));
				PO18_HCMSyncProfilesTab_PM.profilesCount.set(1);
				PageObjectHelper.log(LOGGER, "Clicked checkbox of Success profile with name: " + SPJobName.get());
			} catch (NoSuchElementException e) {
				// Checkbox not selected after click - don't set count
			}
		} catch (Exception e) {
			PageObjectHelper.handleWithContext(
					"click_on_checkbox_of_success_profile_with_export_status_as_not_exported", e,
					"Checkbox operation for profile: " + SPJobName.get());
		}
	}

	public void refresh_hcm_sync_profiles_tab() {
		try {
			refreshPage();
			waitForSpinners();
			PageObjectHelper.log(LOGGER, "HCM Sync Profiles screen page is Refreshed....");
		} catch (Exception e) {
			PageObjectHelper.handleWithContext("refresh_hcm_sync_profiles_tab", e, "Page refresh operation");
		}
	}

	public void user_should_verify_export_status_of_sp_updated_as_exported() {
		try {
			waitForSpinners();

			scrollThroughTableRows(rowNumber.get() + 2);

			WebElement jobNameElement = findTableElement(rowNumber.get(), 1);
			scrollToElement(jobNameElement);

			String actualJobName = jobNameElement.getText();
			if (!actualJobName.equals(SPJobName.get())) {
				LOGGER.warn("Job name mismatch at row {}. Expected: '{}', Actual: '{}'", rowNumber.get(),
						SPJobName.get(), actualJobName);
			}
			SPJobName.set(actualJobName);

			WebElement exportStatusElement = findTableElement(rowNumber.get(), 9);
			String actualStatus = exportStatusElement.getText();

			if (!"Exported".equals(actualStatus)) {
				throw new AssertionError(String.format(
						"Export status verification failed for profile '%s' at row %d. Expected: 'Exported', Actual: '%s'",
						SPJobName.get(), rowNumber.get(), actualStatus));
			}

			PageObjectHelper.log(LOGGER, "Export Status of SP with name " + SPJobName.get() + " updated as " + actualStatus
					+ " as expected");
		} catch (AssertionError e) {
			PageObjectHelper.handleWithContext("user_should_verify_export_status_of_sp_updated_as_exported", e,
					"Export status assertion for profile: " + SPJobName.get());
		} catch (Exception e) {
			PageObjectHelper.handleWithContext("user_should_verify_export_status_of_sp_updated_as_exported", e,
					"Export status verification for profile: " + SPJobName.get());
		}
	}

	private void scrollThroughTableRows(int maxRows) {
		int lastSuccessfulRow = 0;
		int scrollAttempts = 0;
		int maxScrollAttempts = 10;

		for (int j = 1; j <= maxRows; j++) {
			try {
				String xpath = String.format("//tbody//tr[%d]//td[1]//*", j);
				WebElement rowElement = driver.findElement(By.xpath(xpath));
				scrollToElement(rowElement);
				lastSuccessfulRow = j;
				scrollAttempts = 0;
			} catch (NoSuchElementException e) {
				if (lastSuccessfulRow > 0) {
					try {
						String lastRowXpath = String.format("//tbody//tr[%d]//td[1]//*", lastSuccessfulRow);
						WebElement lastRow = driver.findElement(By.xpath(lastRowXpath));

						scrollToElement(lastRow);
						PageObjectHelper.waitForUIStability(driver, 1);
						scrollToBottom();
						PageObjectHelper.waitForUIStability(driver, 1);

						try {
							String retryXpath = String.format("//tbody//tr[%d]//td[1]//*", j);
							WebElement rowElement = driver.findElement(By.xpath(retryXpath));
							scrollToElement(rowElement);
							lastSuccessfulRow = j;
							scrollAttempts = 0;
						} catch (NoSuchElementException retryEx) {
							scrollAttempts++;
							if (scrollAttempts >= maxScrollAttempts) {
								break;
							}
							j--;
						}
					} catch (Exception scrollEx) {
						scrollAttempts++;
						if (scrollAttempts >= maxScrollAttempts) {
							break;
						}
						j--;
					}
				} else {
					break;
				}
			} catch (Exception e) {
				LOGGER.debug("Error accessing row {}: {}", j, e.getMessage());
			}
		}
	}

	public void user_should_click_on_recently_exported_success_profile_job_name() {
		try {
			waitForSpinners(15);
			PageObjectHelper.waitForPageReady(driver, 3);

			// Scroll to appropriate position
			if (rowNumber.get() == 1) {
				scrollToElement(waitForElement(Locators.HCMSyncProfiles.SHOWING_RESULTS_COUNT));
			} else if (rowNumber.get() < 5) {
				WebElement spJobName = waitForElement(By.xpath("//tbody//tr[1]//td[1]//*"));
				scrollToElement(spJobName);
			} else if (rowNumber.get() > 5) {
				WebElement spJobName = waitForElement(
						By.xpath("//tbody//tr[" + (rowNumber.get() - 5) + "]//td[1]//*"));
				scrollToElement(spJobName);
			}

			safeSleep(1000);
			PageObjectHelper.waitForUIStability(driver, 2);

			// Find the job name link element (must be <a> tag for navigation)
			String linkXpath = "//tbody//tr[" + rowNumber.get() + "]//td[1]//a";
			
			WebElement jobNameLink = waitForElement(By.xpath(linkXpath), 10);

			// Verify it's the right element
			Assert.assertEquals(jobNameLink.getText(), SPJobName.get());
			
			// Scroll element to center of viewport
			js.executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", jobNameLink);
			safeSleep(500);
			waitForSpinners();

			// Clear any text selection that might interfere
			js.executeScript("window.getSelection().removeAllRanges();");

			boolean navigationSuccessful = false;
			String currentUrl = driver.getCurrentUrl();

			// Strategy 1: Regular click on link element
			if (!navigationSuccessful) {
				try {
					WebElement clickableLink = PageObjectHelper.waitForClickable(wait, jobNameLink);
					clickableLink.click();
					safeSleep(1500);
					
					// Check if URL changed
					if (!driver.getCurrentUrl().equals(currentUrl)) {
						waitForSpinners(10);
						navigationSuccessful = true;
						LOGGER.info("Regular click successful");
					}
				} catch (Exception e) {
					LOGGER.debug("Regular click failed: {}", e.getMessage());
				}
			}

			// Strategy 2: JS click on link element
			if (!navigationSuccessful) {
				try {
					// Re-find element to avoid stale reference
					jobNameLink = driver.findElement(By.xpath(linkXpath));
					js.executeScript("arguments[0].click();", jobNameLink);
					safeSleep(1500);
					
					// Check if URL changed
					if (!driver.getCurrentUrl().equals(currentUrl)) {
						waitForSpinners(10);
						navigationSuccessful = true;
						LOGGER.info("JS click successful");
					}
				} catch (Exception e) {
					LOGGER.debug("JS click failed: {}", e.getMessage());
				}
			}

			// Strategy 3: Navigate directly via href attribute
			if (!navigationSuccessful) {
				try {
					jobNameLink = driver.findElement(By.xpath(linkXpath));
					String href = jobNameLink.getAttribute("href");
					if (href != null && !href.isEmpty()) {
						LOGGER.info("Navigating directly to: {}", href);
						driver.navigate().to(href);
						safeSleep(1500);
						waitForSpinners(10);
						navigationSuccessful = true;
						LOGGER.info("Direct navigation successful via href");
					}
				} catch (Exception e) {
					LOGGER.debug("Direct navigation failed: {}", e.getMessage());
				}
			}

			// Strategy 4: Actions single click with move
			if (!navigationSuccessful) {
				try {
					jobNameLink = driver.findElement(By.xpath(linkXpath));
					org.openqa.selenium.interactions.Actions actions = new org.openqa.selenium.interactions.Actions(driver);
					actions.moveToElement(jobNameLink).pause(java.time.Duration.ofMillis(500))
							.click().build().perform();
					safeSleep(1500);
					
					// Check if URL changed
					if (!driver.getCurrentUrl().equals(currentUrl)) {
						waitForSpinners(10);
						navigationSuccessful = true;
						LOGGER.info("Actions click successful");
					}
				} catch (Exception e) {
					LOGGER.debug("Actions click failed: {}", e.getMessage());
				}
			}

			if (!navigationSuccessful) {
				LOGGER.error("All navigation strategies failed for profile: {} (row {})", SPJobName.get(), rowNumber.get());
				LOGGER.error("Current URL: {}", driver.getCurrentUrl());
				LOGGER.error("Expected to navigate from: {}", currentUrl);
				throw new Exception("Failed to navigate to Success Profile details page for: " + SPJobName.get());
			}

			PageObjectHelper.log(LOGGER, "Clicked on Recently Exported Success Profile with name: " + SPJobName.get());
			
		} catch (Exception e) {
			PageObjectHelper.handleWithContext("user_should_click_on_recently_exported_success_profile_job_name", e,
					"Click operation failed");
		}
	}

	public void user_should_be_navigated_to_sp_details_page() {
		try {
			waitForSpinners();

			try {
				WebElement pageText = waitForElement(Locators.HCMSyncProfiles.SP_DETAILS_PAGE_TEXT);
				Assert.assertTrue(pageText.isDisplayed());
			} catch (Exception e) {
				String currentUrl = driver.getCurrentUrl();
				String urlPath = "";
				try {
					java.net.URL url = new java.net.URL(currentUrl);
					urlPath = url.getPath() + (url.getRef() != null ? "#" + url.getRef() : "");
				} catch (Exception urlEx) {
					urlPath = currentUrl;
				}

				boolean isDetailsPage = urlPath.contains("/sp/") || urlPath.contains("/success-profile/")
						|| urlPath.contains("details") || (urlPath.contains("profile") && !urlPath.contains("/search"));

				if (!isDetailsPage || urlPath.contains("/search")) {
					throw new AssertionError("Navigation failed - not on SP details page. URL: " + currentUrl);
				}
			}

			PageObjectHelper.log(LOGGER,
					"SP details page opens as expected on click of Recently Exported Success Profile Job name in HCM Sync Profiles screen in PM....");
		} catch (Exception e) {
			PageObjectHelper.handleWithContext("user_should_be_navigated_to_sp_details_page", e,
					"SP details page verification");
		}
	}

	public void click_on_three_dots_in_sp_details_page() {
		try {
			waitForSpinners();
			clickElement(THREE_DOTS_SP_DETAILS);
			PageObjectHelper.log(LOGGER, "Clicked Three Dots in SP Details Page");
			waitForSpinners();
		} catch (Exception e) {
			PageObjectHelper.handleWithContext("click_on_three_dots_in_sp_details_page", e, "Three dots menu");
		}
	}

	public void click_on_edit_success_profile_option() {
		try {
			waitForSpinners();
			clickElement(EDIT_SP_BUTTON);
			PageObjectHelper.log(LOGGER, "Clicked Edit Success Profile option in SP Details Page");
			waitForSpinners();
		} catch (Exception e) {
			PageObjectHelper.handleWithContext("click_on_edit_success_profile_option", e,
					"Edit Success Profile option");
		}
	}

	public void click_on_edit_button_of_details_section() {
		try {
			waitForSpinners();
			clickElement(EDIT_DETAILS_BTN);
			PageObjectHelper.log(LOGGER, "Clicked Edit button of Success Profile Details section");
			waitForSpinners();
		} catch (Exception e) {
			PageObjectHelper.handleWithContext("click_on_edit_button_of_details_section", e,
					"Edit button of Details section");
		}
	}

	public void modify_function_and_sub_function_values_of_the_success_profile() {
		try {
			waitForSpinners();

			clickElement(FUNCTION_DROPDOWN);
			PageObjectHelper.log(LOGGER, "Clicked Function dropdown in Success Profile Details section");
			PageObjectHelper.waitForUIStability(driver, 1);

			List<WebElement> functionOptions = driver.findElements(DROPDOWN_OPTIONS);
			if (functionOptions.isEmpty()) {
				throw new AssertionError("No Function options available in dropdown");
			}

			WebElement selectedFunctionOption = null;
			String functionValue = null;
			for (WebElement option : functionOptions) {
				if (option.isDisplayed() && option.isEnabled()) {
					functionValue = option.getText().trim();
					selectedFunctionOption = option;
					break;
				}
			}

			if (selectedFunctionOption == null || functionValue == null || functionValue.isEmpty()) {
				throw new AssertionError("No valid Function option found to select");
			}

			jsClick(selectedFunctionOption);
			PageObjectHelper.log(LOGGER, "Function value Modified to '" + functionValue + "' in Success Profile Details section");

			PageObjectHelper.waitForUIStability(driver, 2);

			clickElement(SUBFUNCTION_DROPDOWN);
			PageObjectHelper.log(LOGGER, "Clicked Subfunction dropdown in Success Profile Details section");
			PageObjectHelper.waitForUIStability(driver, 1);

			List<WebElement> subFunctionOptions = driver.findElements(DROPDOWN_OPTIONS);
			if (subFunctionOptions.isEmpty()) {
				throw new AssertionError("No Sub-Function options available in dropdown");
			}

			WebElement selectedSubFunctionOption = null;
			String subFunctionValue = null;
			for (WebElement option : subFunctionOptions) {
				if (option.isDisplayed() && option.isEnabled()) {
					subFunctionValue = option.getText().trim();
					selectedSubFunctionOption = option;
					break;
				}
			}

			if (selectedSubFunctionOption == null || subFunctionValue == null || subFunctionValue.isEmpty()) {
				throw new AssertionError("No valid Sub-Function option found to select");
			}

			jsClick(selectedSubFunctionOption);
			PageObjectHelper.log(LOGGER,
					"Sub-Function value Modified to '" + subFunctionValue + "' in Success Profile Details section");
		} catch (Exception e) {
			PageObjectHelper.handleWithContext("modify_function_and_sub_function_values_of_the_success_profile", e,
					"Function/SubFunction modification");
		}
	}

	public void click_on_done_button() {
		try {
			waitForSpinners();
			waitForPageLoad();
			clickElement(DONE_BTN);
			PageObjectHelper.log(LOGGER, "Clicked Done button of Success Profile Details section");
			waitForSpinners();
		} catch (Exception e) {
			PageObjectHelper.handleWithContext("click_on_done_button", e, "Done button operation");
		}
	}

	public void click_on_save_button() {
		try {
			waitForSpinners();
			clickElement(SAVE_BTN);
			PageObjectHelper.log(LOGGER, "Clicked Save button of Success Profile");
			waitForSpinners();
		} catch (Exception e) {
			PageObjectHelper.handleWithContext("click_on_save_button", e, "Save button operation");
		}
	}

	public void user_should_be_navigated_to_sp_details_page_after_saving_sp_details() {
		try {
			waitForSpinners();
			WebElement pageText = waitForElement(Locators.HCMSyncProfiles.SP_DETAILS_PAGE_TEXT);
			Assert.assertTrue(pageText.isDisplayed());
			PageObjectHelper.log(LOGGER, "Navigated to SP details page as expected after Saving SP details....");
		} catch (AssertionError e) {
			PageObjectHelper.handleWithContext("user_should_be_navigated_to_sp_details_page_after_saving_sp_details",
					e, "SP details page assertion after save");
		} catch (Exception e) {
			PageObjectHelper.handleWithContext("user_should_be_navigated_to_sp_details_page_after_saving_sp_details",
					e, "SP details page verification after save");
		}
	}

	public void user_should_verify_recently_modified_success_profile_is_displaying_on_top_of_the_job_proifles_list() {
		try {
			waitForSpinners();
			WebElement topJobName = findTableElement(1, 1);
			Assert.assertEquals(topJobName.getText(), SPJobName.get());
			PageObjectHelper.log(LOGGER, "Recently Modified SP with name " + topJobName.getText()
					+ " is displaying on the Top of the Profiles List as Expected in HCM Sync Profiles screen");
		} catch (AssertionError e) {
			PageObjectHelper.handleWithContext(
					"user_should_verify_recently_modified_success_profile_is_displaying_on_top_of_the_job_proifles_list",
					e, "Profile position assertion");
		} catch (Exception e) {
			PageObjectHelper.handleWithContext(
					"user_should_verify_recently_modified_success_profile_is_displaying_on_top_of_the_job_proifles_list",
					e, "Profile list verification");
		}
	}

	public void user_should_verify_recently_exported_and_modified_success_profile_export_status_updated_as_exported_modfied() {
		try {
			waitForSpinners();
			WebElement topJobName = findTableElement(1, 1);
			Assert.assertEquals(topJobName.getText(), SPJobName.get());
			SPJobName.set(topJobName.getText());

			WebElement exportStatus = findTableElement(1, 9);
			Assert.assertEquals(exportStatus.getText(), "Exported - Modified");
			PageObjectHelper.log(LOGGER, "Export Status of recently Exported and Modfied SP with name " + SPJobName.get()
					+ " updated as " + exportStatus.getText() + " as expected...");
		} catch (AssertionError e) {
			PageObjectHelper.handleWithContext(
					"user_should_verify_recently_exported_and_modified_success_profile_export_status_updated_as_exported_modfied",
					e, "Export status assertion for modified profile");
		} catch (Exception e) {
			PageObjectHelper.handleWithContext(
					"user_should_verify_recently_exported_and_modified_success_profile_export_status_updated_as_exported_modfied",
					e, "Modified profile export status verification");
		}
	}

}
