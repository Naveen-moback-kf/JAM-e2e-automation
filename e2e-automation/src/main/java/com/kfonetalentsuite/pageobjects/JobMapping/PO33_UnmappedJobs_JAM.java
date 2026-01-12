package com.kfonetalentsuite.pageobjects.JobMapping;
import static com.kfonetalentsuite.pageobjects.JobMapping.BasePageObject.Locators.JobMappingScreen.*;
import static com.kfonetalentsuite.pageobjects.JobMapping.BasePageObject.Locators.JAMSelectionScreen.*;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.SkipException;
import com.kfonetalentsuite.utils.common.ScreenshotHandler;
import com.kfonetalentsuite.utils.JobMapping.Utilities;

public class PO33_UnmappedJobs_JAM extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO33_UnmappedJobs_JAM.class);

	public static ThreadLocal<Boolean> skipScenario = ThreadLocal.withInitial(() -> false);

	// Locators
	public PO33_UnmappedJobs_JAM() {
		super();
	}

	public void select_unmapped_jobs_option_in_mapping_status_filters_dropdown() {
		skipScenario.set(false);

		try {
			Utilities.waitForSpinnersToDisappear(driver, 10);
			Utilities.waitForPageReady(driver, 2);

			List<WebElement> mappingStatusCheckboxes = findElements(MAPPING_STATUS_CHECKBOXES);
			List<WebElement> mappingStatusValues = findElements(MAPPING_STATUS_VALUES);

			boolean unmappedFound = false;

			for (int i = 0; i < mappingStatusValues.size(); i++) {
				String statusText = mappingStatusValues.get(i).getText();

				if (statusText.contains("Unmapped")) {
					LOGGER.info("Found Unmapped option: " + statusText);

					js.executeScript("arguments[0].scrollIntoView();", mappingStatusValues.get(i));

					try {
						Utilities.waitForClickable(wait, mappingStatusValues.get(i)).click();
					} catch (Exception e) {
						try {
							js.executeScript("arguments[0].click();", mappingStatusValues.get(i));
						} catch (Exception s) {
							jsClick(mappingStatusValues.get(i));
						}
					}

					Utilities.waitForSpinnersToDisappear(driver, 10);
					Utilities.waitForPageReady(driver, 3);

					Assert.assertTrue(mappingStatusCheckboxes.get(i).isSelected(), "Unmapped option should be selected");
					LOGGER.info("Selected Unmapped jobs option from Mapping Status Filters");

					unmappedFound = true;
					break;
				}
			}

			if (!unmappedFound) {
				skipScenario.set(true);
				String skipMessage = "Unmapped option not found in Mapping Status Filters dropdown - Skipping Feature 46 validation";
				LOGGER.info(skipMessage);
				throw new SkipException(skipMessage);
			}

		} catch (SkipException e) {
			throw e;
		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("select_unmapped_jobs_option_in_mapping_status_filters_dropdown", e);
			Utilities.handleError(LOGGER, "select_unmapped_jobs_option_in_mapping_status_filters_dropdown",
					"Error selecting Unmapped jobs option", e);
			Assert.fail("Error selecting Unmapped jobs option: " + e.getMessage());
		}
	}

	public void verify_header_checkbox_is_disabled_in_job_mapping_screen() {
		if (skipScenario.get()) {
			throw new SkipException("Skipping validation - Unmapped Jobs option not available in Mapping Status Filters");
		}

		try {
			Utilities.waitForPageReady(driver, 2);

			WebElement headerCheckbox = findElement(HEADER_CHECKBOX);
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", headerCheckbox);
			safeSleep(500);

			boolean isEnabled = headerCheckbox.isEnabled();
			Assert.assertFalse(isEnabled, "Header Checkbox should be disabled when Unmapped filter is applied");

			LOGGER.info("Header Checkbox is disabled as expected (Unmapped jobs cannot be selected)");

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_header_checkbox_is_disabled_in_job_mapping_screen", e);
			Utilities.handleError(LOGGER, "verify_header_checkbox_is_disabled_in_job_mapping_screen",
					"Error verifying header checkbox is disabled", e);
			Assert.fail("Error verifying header checkbox is disabled: " + e.getMessage());
		}
	}

	public void verify_chevron_button_is_disabled_in_job_mapping_screen() {
		if (skipScenario.get()) {
			throw new SkipException("Skipping validation - Unmapped option not available in Mapping Status Filters");
		}

		try {
			Utilities.waitForPageReady(driver, 1);
			js.executeScript("window.scrollTo(0, 0);");
			safeSleep(300);

			List<WebElement> disabledChevrons = findElements(DISABLED_CHEVRONS);

			if (disabledChevrons.isEmpty()) {
				LOGGER.error("Disabled chevron button not found in Job Mapping screen");
				List<WebElement> anyChevrons = findElements(ANY_CHEVRONS);

				if (!anyChevrons.isEmpty()) {
					String actualClasses = anyChevrons.get(0).getAttribute("class");
					LOGGER.error("Found chevron with classes: {}", actualClasses);
					Assert.fail("Chevron button is NOT disabled. Expected 'cursor-not-allowed opacity-30' classes but found: " + actualClasses);
				} else {
					Assert.fail("Chevron button not found in Job Mapping screen header.");
				}
			}

			WebElement chevronButton = disabledChevrons.get(0);
			String classAttribute = chevronButton.getAttribute("class");

			Assert.assertTrue(classAttribute.contains("cursor-not-allowed"),
					"Chevron button should have 'cursor-not-allowed' class");
			Assert.assertTrue(classAttribute.contains("opacity-30"),
					"Chevron button should have 'opacity-30' class");

			LOGGER.info("Chevron button is disabled as expected (Unmapped jobs cannot be selected)");

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_chevron_button_is_disabled_in_job_mapping_screen", e);
			Utilities.handleError(LOGGER, "verify_chevron_button_is_disabled_in_job_mapping_screen",
					"Error verifying chevron button is disabled", e);
			Assert.fail("Error verifying chevron button is disabled: " + e.getMessage());
		}
	}

	public void verify_checkbox_of_all_unmapped_jobs_is_disabled_with_tooltip() {
		if (skipScenario.get()) {
			throw new SkipException("Skipping validation - Unmapped option not available in Mapping Status Filters");
		}

		int totalCheckboxes = 0;
		int disabledCheckboxes = 0;
		int checkboxesWithTooltip = 0;
		String expectedTooltipText = "No Success Profile have been mapped to this Job.";

		try {
			Utilities.waitForPageReady(driver, 1);

			LOGGER.info("========================================");
			LOGGER.info("VERIFYING UNMAPPED JOB CHECKBOXES");
			LOGGER.info("========================================");

			List<WebElement> allCheckboxes = findElements(ALL_CHECKBOXES);
			totalCheckboxes = allCheckboxes.size();
			LOGGER.info("Total checkboxes found: {}", totalCheckboxes);

			int samplesToCheck = Math.min(1, totalCheckboxes);
			List<WebElement> allTooltipContainers = findElements(TOOLTIP_CONTAINERS);
			LOGGER.info("Total tooltip containers found: {}", allTooltipContainers.size());

			for (int i = 0; i < allCheckboxes.size(); i++) {
				try {
					WebElement checkbox = allCheckboxes.get(i);
					boolean isDisabled = !checkbox.isEnabled();

					if (isDisabled) {
						disabledCheckboxes++;
					} else {
						LOGGER.warn("Checkbox at position {} is NOT disabled", (i + 1));
					}

					// Check for tooltip on first checkbox only
					if (i < samplesToCheck && i < allTooltipContainers.size()) {
						try {
							WebElement tooltipContainer = allTooltipContainers.get(i);
							js.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});", tooltipContainer);
							safeSleep(200);

							String tooltipText = null;

							// Try to get tooltip from attributes
							tooltipText = tooltipContainer.getAttribute("aria-label");
							if (tooltipText == null || tooltipText.isEmpty()) {
								tooltipText = tooltipContainer.getAttribute("title");
							}
							if (tooltipText == null || tooltipText.isEmpty()) {
								tooltipText = tooltipContainer.getAttribute("data-tooltip");
							}

							// Try JavaScript to trigger tooltip
							if (tooltipText == null || tooltipText.isEmpty()) {
								js.executeScript("arguments[0].dispatchEvent(new MouseEvent('mouseover', {bubbles: true}));", tooltipContainer);
								safeSleep(300);
								List<WebElement> tooltips = driver.findElements(By.xpath("//div[@data-testid='tooltip']"));
								if (!tooltips.isEmpty()) {
									tooltipText = tooltips.get(0).getText();
								}
							}

							if (tooltipText != null && tooltipText.contains(expectedTooltipText)) {
								checkboxesWithTooltip++;
								LOGGER.debug("Checkbox {} has correct tooltip", (i + 1));
							}

						} catch (Exception tooltipException) {
							LOGGER.debug("Could not verify tooltip for checkbox at position {}", (i + 1));
						}
					}

				} catch (Exception e) {
					LOGGER.debug("Could not verify checkbox at position {}: {}", (i + 1), e.getMessage());
				}
			}

			LOGGER.info("========================================");
			LOGGER.info("VALIDATION RESULTS");
			LOGGER.info("========================================");
			LOGGER.info("Total Checkboxes: {}", totalCheckboxes);
			LOGGER.info("Disabled Checkboxes: {}", disabledCheckboxes);
			LOGGER.info("Checkbox with Tooltip verified: {}", checkboxesWithTooltip > 0 ? "Yes" : "No");
			LOGGER.info("========================================");

			Assert.assertEquals(disabledCheckboxes, totalCheckboxes, "All checkboxes should be disabled for Unmapped jobs");

			if (samplesToCheck > 0) {
				Assert.assertTrue(checkboxesWithTooltip > 0,
						"First checkbox should have tooltip. Expected: " + expectedTooltipText);
			}

			LOGGER.info("Validation PASSED: All " + disabledCheckboxes + " unmapped job checkboxes are disabled");
			LOGGER.info("First checkbox has correct tooltip: '" + expectedTooltipText + "'");

		} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("verify_checkbox_of_all_unmapped_jobs_is_disabled_with_tooltip", e);
			Utilities.handleError(LOGGER, "verify_checkbox_of_all_unmapped_jobs_is_disabled_with_tooltip",
					"Error verifying unmapped job checkboxes", e);
			Assert.fail("Error verifying unmapped job checkboxes: " + e.getMessage());
		}
	}
}

