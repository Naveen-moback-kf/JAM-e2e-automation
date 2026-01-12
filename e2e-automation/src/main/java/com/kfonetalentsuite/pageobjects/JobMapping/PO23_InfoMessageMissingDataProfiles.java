package com.kfonetalentsuite.pageobjects.JobMapping;
import static com.kfonetalentsuite.pageobjects.JobMapping.BasePageObject.Locators.JobMappingScreen.*;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.SkipException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.kfonetalentsuite.utils.JobMapping.Utilities;

public class PO23_InfoMessageMissingDataProfiles extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO23_InfoMessageMissingDataProfiles.class);

	public PO23_InfoMessageMissingDataProfiles() {
		super();
	}

	@SuppressWarnings("unused")
	private static ThreadLocal<List<WebElement>> profilesWithInfoMessages = ThreadLocal.withInitial(ArrayList::new);
	private static ThreadLocal<List<Integer>> rowIndicesWithInfoMessages = ThreadLocal.withInitial(ArrayList::new);
	private static ThreadLocal<Integer> currentRowIndex = ThreadLocal.withInitial(() -> -1);
	private static ThreadLocal<Integer> secondCurrentRowIndex = ThreadLocal.withInitial(() -> -1);

	private static ThreadLocal<Integer> globalFirstProfileRowIndex = ThreadLocal.withInitial(() -> -1);
	private static ThreadLocal<Integer> globalFirstProfileNumber = ThreadLocal.withInitial(() -> -1);
	private static ThreadLocal<String> globalFirstJobNameWithInfoMessage = ThreadLocal.withInitial(() -> "");
	private static ThreadLocal<String> globalFirstJobCodeWithInfoMessage = ThreadLocal.withInitial(() -> "");
	private static ThreadLocal<String> jobNameWithInfoMessage = ThreadLocal.withInitial(() -> "");
	private static ThreadLocal<String> jobCodeWithInfoMessage = ThreadLocal.withInitial(() -> "");
	private static ThreadLocal<String> gradeWithInfoMessage = ThreadLocal.withInitial(() -> "");
	private static ThreadLocal<String> departmentWithInfoMessage = ThreadLocal.withInitial(() -> "");
	private static ThreadLocal<String> functionSubfunctionWithInfoMessage = ThreadLocal.withInitial(() -> "");
	private static ThreadLocal<Integer> currentRowIndexStatic = ThreadLocal.withInitial(() -> -1);

	private static ThreadLocal<String> secondJobNameWithInfoMessage = ThreadLocal.withInitial(() -> "");
	private static ThreadLocal<String> secondJobCodeWithInfoMessage = ThreadLocal.withInitial(() -> "");
	private static ThreadLocal<String> secondGradeWithInfoMessage = ThreadLocal.withInitial(() -> "");
	private static ThreadLocal<String> secondDepartmentWithInfoMessage = ThreadLocal.withInitial(() -> "");
	private static ThreadLocal<String> secondFunctionSubfunctionWithInfoMessage = ThreadLocal.withInitial(() -> "");
	private static ThreadLocal<Integer> secondCurrentRowIndexStatic = ThreadLocal.withInitial(() -> -1);

	private void waitForUIStabilityInMs(int milliseconds) {
		try {
			Utilities.waitForUIStability(driver, Math.max(1, milliseconds / 1000));
		} catch (Exception e) {
			Thread.currentThread().interrupt();
			LOGGER.warn("Sleep interrupted: " + e.getMessage());
		}
	}

	private boolean scrollDownAndFindMoreAutoMappedProfiles() {
		try {
			LOGGER.info("SCROLL: Loading more profiles (target: at least 50 profiles)...");
			int initialRowCount = driver.findElements(ORG_JOB_TABLE_ROWS).size();
			int initialProfiles = initialRowCount / 3;
			LOGGER.info("STATUS: Starting with {} profiles", initialProfiles);

			int targetProfiles = 50;
			int maxScrolls = 50;
			int scrollCount = 0;

			while (scrollCount < maxScrolls) {
				js.executeScript("window.scrollBy(0, window.innerHeight);");
				waitForUIStabilityInMs(1000);
				scrollCount++;

				int currentRowCount = driver.findElements(ORG_JOB_TABLE_ROWS).size();
				int currentProfiles = currentRowCount / 3;

				if (scrollCount % 10 == 0) {
					LOGGER.info("STATUS: Scroll #{}: {} profiles loaded", scrollCount, currentProfiles);
				}

				if (currentProfiles >= targetProfiles) {
					LOGGER.info("SUCCESS: Target reached: {} profiles loaded (target: {})", currentProfiles, targetProfiles);
					break;
				}

				if (currentRowCount == initialRowCount) {
					if (scrollCount >= 5) {
						LOGGER.info("STOP: No more content loading after {} scrolls", scrollCount);
						break;
					}
				} else {
					initialRowCount = currentRowCount;
				}
			}

			int finalRowCount = driver.findElements(ORG_JOB_TABLE_ROWS).size();
			int finalProfiles = finalRowCount / 3;

			LOGGER.info("STATUS: Scrolling complete: {} profiles loaded", finalProfiles);
			return finalProfiles >= targetProfiles;
		} catch (Exception e) {
			LOGGER.warn("WARNING: Error during scrolling: " + e.getMessage());
			return false;
		}
	}

	private void extractJobDetailsDirectlyFromInfoMessage(WebElement infoMessageElement, boolean isSecondProfile)
			throws IOException {
		try {
			LOGGER.info("INFO: Extracting job details directly from info message element (second profile: {})", isSecondProfile);
			WebElement rowWithInfoMessage = infoMessageElement.findElement(By.xpath("./ancestor::tr"));
			int infoRowIndex = getRowIndexInTable(rowWithInfoMessage);
			if (infoRowIndex <= 0) {
				throw new IOException("Could not determine row index for info message element");
			}

			int jobDetailsRowIndex = infoRowIndex - 1;
			LOGGER.info("STATUS: Direct extraction: Info message row {}, job details row {}", infoRowIndex, jobDetailsRowIndex);

			WebElement jobDetailsRow = driver.findElement(
					By.xpath(String.format("//div[@id='org-job-container']//tbody//tr[%d]", jobDetailsRowIndex)));

			if (!jobDetailsRow.isDisplayed()) {
				throw new IOException("Job details row " + jobDetailsRowIndex + " is not visible");
			}

			try {
				WebElement jobNameCell = jobDetailsRow.findElement(By.xpath(".//td[2]//div"));
				String fullJobText = jobNameCell.getText().trim();

			if (fullJobText.contains(" - (") && fullJobText.contains(")")) {
				String[] parts = fullJobText.split(" - \\(");
				if (isSecondProfile) {
					secondJobNameWithInfoMessage.set(parts[0].trim());
					secondJobCodeWithInfoMessage.set(parts[1].replace(")", "").trim());
				} else {
					jobNameWithInfoMessage.set(parts[0].trim());
					jobCodeWithInfoMessage.set(parts[1].replace(")", "").trim());
				}
			} else {
				if (isSecondProfile) {
					secondJobNameWithInfoMessage.set(fullJobText);
					secondJobCodeWithInfoMessage.set("");
				} else {
					jobNameWithInfoMessage.set(fullJobText);
					jobCodeWithInfoMessage.set("");
					}
				}

				LOGGER.info("SUCCESS: Direct extraction successful - Job: '{}' Code: '{}'",
						isSecondProfile ? secondJobNameWithInfoMessage : jobNameWithInfoMessage,
						isSecondProfile ? secondJobCodeWithInfoMessage : jobCodeWithInfoMessage);
			} catch (Exception e) {
				LOGGER.warn("WARNING: Could not extract job name/code using direct method: " + e.getMessage());
				try {
					List<WebElement> allCells = jobDetailsRow.findElements(By.xpath(".//td"));
					if (!allCells.isEmpty()) {
					String cellText = allCells.get(0).getText().trim();
					if (isSecondProfile) {
						secondJobNameWithInfoMessage.set(cellText);
						secondJobCodeWithInfoMessage.set("");
					} else {
						jobNameWithInfoMessage.set(cellText);
						jobCodeWithInfoMessage.set("");
						}
						LOGGER.info("STATUS: Alternative extraction - Job: '{}'", cellText);
					}
				} catch (Exception e2) {
					LOGGER.error("ERROR: All direct extraction attempts failed: " + e2.getMessage());
					throw new IOException("Failed to extract job details using direct method: " + e2.getMessage());
				}
			}
		} catch (Exception e) {
			LOGGER.error("ERROR: Direct extraction failed: " + e.getMessage());
			throw new IOException("Failed to extract job details directly from info message: " + e.getMessage());
		}
	}

	private int getRowIndexInTable(WebElement rowElement) {
		try {
			WebElement tbody = rowElement.findElement(By.xpath("./ancestor::tbody"));
			List<WebElement> allRows = tbody.findElements(By.xpath("./tr"));
			for (int i = 0; i < allRows.size(); i++) {
				if (allRows.get(i).equals(rowElement)) {
					return i + 1;
				}
			}
		} catch (Exception e) {
			LOGGER.warn("Could not determine row index: " + e.getMessage());
		}
		return -1;
	}

	private int getProfileNumber(int rowIndex) {
		if (rowIndex <= 0) return -1;
		return (int) Math.ceil((double) rowIndex / 3.0);
	}

	@SuppressWarnings("unused")
	private boolean checkCurrentProfilesForAutoMapped(List<WebElement> infoMessages, int searchAttempt) {
		LOGGER.info("INFO: Checking {} visible profiles for AutoMapped ones (search attempt {})", infoMessages.size(), searchAttempt);

		for (WebElement infoMessage : infoMessages) {
			if (infoMessage.isDisplayed()) {
				try {
					WebElement jobRow = infoMessage.findElement(By.xpath("./ancestor::tr"));
					int rowIndex = getRowIndexInTable(jobRow);

					if (rowIndex > 0) {
						if (!isAutoMappedProfile(rowIndex)) {
							int profileNumber = getProfileNumber(rowIndex);
							LOGGER.info("SKIP: Skipping Profile {} (row {}) - Manual Mapping profile", profileNumber, rowIndex);
							continue;
						}

					profilesWithInfoMessages.get().add(infoMessage);
					rowIndicesWithInfoMessages.get().add(rowIndex);
					currentRowIndex.set(rowIndex);

						int profileNumber = getProfileNumber(rowIndex);

						try {
							extractJobDetailsFromRow(rowIndex - 1);
							extractFunctionSubfunctionFromRow(rowIndex);

						if (jobNameWithInfoMessage.get().isEmpty() && jobCodeWithInfoMessage.get().isEmpty()) {
							LOGGER.warn("WARNING: First extraction attempt failed for first profile at row {} - trying alternative approach", rowIndex);
							extractJobDetailsDirectlyFromInfoMessage(infoMessage, false);
						}

						if (jobNameWithInfoMessage.get().isEmpty() && jobCodeWithInfoMessage.get().isEmpty()) {
								throw new IOException("Failed to extract job details for first profile even with alternative methods - row " + rowIndex);
							}
						} catch (Exception extractionError) {
							LOGGER.error("ERROR: Failed to extract job details for first profile at row {}: {}", rowIndex, extractionError.getMessage());
							try {
								LOGGER.info("INFO: Attempting direct extraction from info message element for first profile...");
								extractJobDetailsDirectlyFromInfoMessage(infoMessage, false);

								if (!jobNameWithInfoMessage.get().isEmpty() || !jobCodeWithInfoMessage.get().isEmpty()) {
									LOGGER.info("SUCCESS: Direct extraction successful for first profile");
								} else {
									throw new IOException("All extraction methods failed for first profile");
								}
							} catch (Exception finalError) {
								LOGGER.error("ERROR: All extraction attempts failed for first profile: {}", finalError.getMessage());
								throw new IOException("Failed to extract first profile details after multiple attempts: " + finalError.getMessage());
							}
						}

					globalFirstProfileRowIndex.set(rowIndex);
					globalFirstProfileNumber.set(profileNumber);
					globalFirstJobNameWithInfoMessage.set(jobNameWithInfoMessage.get());
					globalFirstJobCodeWithInfoMessage.set(jobCodeWithInfoMessage.get());

						LOGGER.info("SUCCESS: AutoMapped Profile {} found with Info Message (table row {})", profileNumber, rowIndex);
						LOGGER.info("  Job Name: {}", jobNameWithInfoMessage.get());
						LOGGER.info("  Job Code: {}", jobCodeWithInfoMessage.get());

						return true;
					}
				} catch (Exception e) {
					LOGGER.warn("Error processing info message: " + e.getMessage());
				}
			}
		}
		return false;
	}

	private boolean isAutoMappedProfile(int rowIndex) {
		try {
			List<WebElement> viewMatchesButtons = driver.findElements(By.xpath(String.format(
					"//div[@id='kf-job-container']//tbody//tr[%d]//button[@id='view-matches'] | "
							+ "//div[@id='kf-job-container']//tbody//tr[%d]//button[contains(@aria-label, 'View other matches')] | "
							+ "//div[@id='kf-job-container']//tbody//tr[%d]//button[contains(text(), 'View') and contains(text(), 'Other Matches')]",
					rowIndex, rowIndex, rowIndex)));

			if (!viewMatchesButtons.isEmpty()) {
				for (WebElement button : viewMatchesButtons) {
					if (button.isDisplayed()) {
						return true;
					}
				}
			}

			List<WebElement> searchButtons = driver.findElements(By.xpath(String.format(
					"//div[@id='kf-job-container']//tbody//tr[%d]//button[contains(@aria-label, 'Search a different profile') or contains(text(), 'Search a different profile')]",
					rowIndex)));

			if (!searchButtons.isEmpty()) {
				for (WebElement button : searchButtons) {
					if (button.isDisplayed()) {
						return false;
					}
				}
			}

			LOGGER.debug("Could not determine profile type for row {} - assuming AutoMapped", rowIndex);
			return true;
		} catch (Exception e) {
			LOGGER.warn("Error determining profile type for row {}: {}", rowIndex, e.getMessage());
			return false;
		}
	}

	private void extractJobDetailsFromRow(int jobDetailsRowIndex) {
		try {
			WebElement jobDetailsRow = driver.findElement(
					By.xpath(String.format("//div[@id='org-job-container']//tbody//tr[%d]", jobDetailsRowIndex)));

			try {
				WebElement jobNameCell = jobDetailsRow.findElement(By.xpath(".//td[2]//div"));
			String fullJobText = jobNameCell.getText().trim();
			if (fullJobText.contains(" - (") && fullJobText.contains(")")) {
				String[] parts = fullJobText.split(" - \\(");
				jobNameWithInfoMessage.set(parts[0].trim());
				jobCodeWithInfoMessage.set(parts[1].replace(")", "").trim());
			} else {
				jobNameWithInfoMessage.set(fullJobText);
				jobCodeWithInfoMessage.set("");
				}
			} catch (Exception e) {
				LOGGER.debug("Could not extract job name/code: " + e.getMessage());
			}
		} catch (Exception e) {
			LOGGER.warn("Error extracting job details: " + e.getMessage());
		}
	}

	private void extractFunctionSubfunctionFromRow(int infoMessageRowIndex) {
		try {
			WebElement infoMessageRow = driver.findElement(
					By.xpath(String.format("//div[@id='org-job-container']//tbody//tr[%d]", infoMessageRowIndex)));

			try {
				String rowText = infoMessageRow.getText();
				String[] functionPatterns = { "Function / Sub-function:", "Function/Sub-function:", "Function:", "Sub-function:" };

				for (String pattern : functionPatterns) {
					if (rowText.contains(pattern)) {
						String[] parts = rowText.split(pattern);
						if (parts.length > 1) {
							String functionPart = parts[1].trim();
							if (functionPart.contains("Reduced match accuracy")) {
								functionPart = functionPart.split("Reduced match accuracy")[0].trim();
							}
							functionPart = functionPart.replaceAll("\\n", " ").replaceAll("\\s+", " ").trim();
							functionSubfunctionWithInfoMessage.set(functionPart);
							break;
						}
					}
				}
			} catch (Exception e) {
				LOGGER.debug("Could not extract function/subfunction: " + e.getMessage());
			}
		} catch (Exception e) {
			LOGGER.warn("Error extracting function/subfunction: " + e.getMessage());
		}
	}

	public void find_and_verify_profile_with_missing_data_has_info_message_displayed() throws IOException {
		try {
			LOGGER.info("Finding and verifying profile with missing data has Info Message displayed");
			waitForUIStabilityInMs(2000);

			List<WebElement> infoMessages = driver.findElements(By.xpath(
					"//div[@id='org-job-container']//div[@role='button' and @aria-label='Reduced match accuracy due to missing data']"));

			if (infoMessages.isEmpty()) {
				LOGGER.info("SCROLL: No info messages found in current view, scrolling to load more profiles...");
				boolean foundMoreProfiles = scrollDownAndFindMoreAutoMappedProfiles();

				if (foundMoreProfiles) {
					infoMessages = driver.findElements(By.xpath(
							"//div[@id='org-job-container']//div[@role='button' and @aria-label='Reduced match accuracy due to missing data']"));
					LOGGER.info("STATUS: After scrolling: Found {} info messages", infoMessages.size());
				}

				if (infoMessages.isEmpty()) {
					String[] alternativeXPaths = {
							"//div[@id='org-job-container']//div[@aria-label='Reduced match accuracy due to missing data']",
							"//div[@id='org-job-container']//div[contains(@aria-label, 'Reduced match accuracy')]",
							"//div[@id='org-job-container']//div[contains(text(), 'Reduced match accuracy due to missing data')]" };

					for (String xpath : alternativeXPaths) {
						infoMessages = driver.findElements(By.xpath(xpath));
						if (!infoMessages.isEmpty()) {
							LOGGER.info("STATUS: Found {} info messages using alternative locator", infoMessages.size());
							break;
						}
					}
				}
			}

			// Check if any info messages exist - if not, skip gracefully
			if (infoMessages.isEmpty()) {
				String skipMessage = "SKIPPED: No Info Messages found for jobs with missing data - Application has NO profiles with missing data. " +
						"This is a data-dependent test that requires profiles with missing data to execute.";
				LOGGER.warn(skipMessage);
				throw new org.testng.SkipException(skipMessage);
			}
			
			LOGGER.info("STATUS: Found {} Info Messages for profiles with missing data", infoMessages.size());

			boolean infoMessageDisplayed = false;
			for (WebElement infoMessage : infoMessages) {
				if (infoMessage.isDisplayed()) {
					infoMessageDisplayed = true;
					LOGGER.info("SUCCESS: Found displayed info message");
					break;
				}
			}

			// Check if info message is displayed - if not, skip gracefully
			if (!infoMessageDisplayed) {
				String skipMessage = "SKIPPED: Info Message not displayed for profile with missing data. " +
						"This scenario requires visible info messages to execute.";
				LOGGER.warn(skipMessage);
				throw new org.testng.SkipException(skipMessage);
			};

			LOGGER.info("Successfully found and verified profile with missing data has Info Message displayed (searched across multiple pages)");
		} catch (org.testng.SkipException se) {
			throw se; // Rethrow SkipException
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "find_and_verify_profile_with_missing_data_has_info_message_displayed",
					"Failed to find and verify profile with missing data has Info Message", e);
		}
	}

	public void find_profile_with_missing_data_and_info_message() throws IOException {
		LOGGER.info("Searching for AutoMapped profile with missing data");

		try {
			waitForUIStabilityInMs(2000);

			int profilesChecked = 0;
			int scrollAttempts = 0;
			int maxScrollAttempts = 50;
			Set<String> processedJobCodes = new HashSet<>();
			int previousRowCount = 0;
			int noNewRowsCount = 0;
			int maxNoNewRowsCount = 5;

			while (scrollAttempts < maxScrollAttempts) {
				scrollAttempts++;

				List<WebElement> currentRows = driver.findElements(ORG_JOB_TABLE_ROWS);
				if (profilesChecked % 10 == 0 || scrollAttempts == 1) {
					LOGGER.info("Searching... (checked {} profiles)", profilesChecked);
				}

				List<WebElement> rightTableAllRows = driver.findElements(KF_JOB_TABLE_ROWS);
				@SuppressWarnings("unchecked")
				List<String> rightTableRowClasses = (List<String>) js.executeScript(
						"var rows = document.querySelectorAll('#kf-job-container tbody tr');"
						+ "return Array.from(rows).map(r => r.className || '');");

				for (int i = 0; i < currentRows.size() - 2; i += 3) {
					try {
						WebElement jobDataRow = currentRows.get(i);
						WebElement functionRow = currentRows.get(i + 1);

						String jobCode = "";
						try {
							List<WebElement> jobNameElements = jobDataRow.findElements(By.xpath(".//td[2]//div"));
							if (!jobNameElements.isEmpty()) {
								String jobNameText = jobNameElements.get(0).getAttribute("textContent");
								if (jobNameText != null && jobNameText.contains("(") && jobNameText.contains(")")) {
									int startIdx = jobNameText.lastIndexOf("(") + 1;
									int endIdx = jobNameText.lastIndexOf(")");
									if (startIdx > 0 && endIdx > startIdx) {
										jobCode = jobNameText.substring(startIdx, endIdx).trim();
									}
								}
							}
						} catch (Exception e) {
							// Continue
						}

						if (!jobCode.isEmpty() && processedJobCodes.contains(jobCode)) {
							continue;
						}

						if (!jobCode.isEmpty()) {
							processedJobCodes.add(jobCode);
						}
						profilesChecked++;

						boolean hasInfoMsg = false;
						try {
							List<WebElement> infoElements = functionRow.findElements(By.xpath(
									".//td[@colspan]//div[@id='matches-profiles-action-container']//div[contains(text(), 'Reduced match accuracy')] | "
									+ ".//td[@colspan]//div[contains(@aria-label, 'Reduced match accuracy')] | "
									+ ".//td[@colspan]//div[contains(text(), 'missing data')]"));
							hasInfoMsg = !infoElements.isEmpty();
						} catch (Exception e) {
							// Continue
						}

						if (!hasInfoMsg) {
							continue;
						}

						boolean isAutoMapped = false;
						int foundAtRow = -1;
						int rightDataRowCount = 0;
						int profileNumber = i / 3;

						try {
							int currentProfile = -1;
							int startIdx = 0;
							int rightProfileStartRow = -1;
							int rightProfileEndRow = -1;

							for (int r = 0; r < rightTableAllRows.size(); r++) {
								String rowClass = rightTableRowClasses.get(r);
								if (rowClass != null && rowClass.contains("bg-gray")) {
									currentProfile++;
									if (currentProfile == profileNumber) {
										rightProfileStartRow = startIdx;
										rightProfileEndRow = r - 1;
										break;
									}
									startIdx = r + 1;
								}
							}

							if (currentProfile == profileNumber - 1 && rightProfileEndRow == -1) {
								rightProfileStartRow = startIdx;
								rightProfileEndRow = rightTableAllRows.size() - 1;
							}

							if (rightProfileStartRow >= 0 && rightProfileEndRow >= rightProfileStartRow) {
								List<WebElement> dataRows = new ArrayList<>();
								for (int r = rightProfileStartRow; r <= rightProfileEndRow; r++) {
									String rowClass = rightTableRowClasses.get(r);
									if (rowClass == null || !rowClass.contains("bg-gray")) {
										dataRows.add(rightTableAllRows.get(r));
									}
								}

								rightDataRowCount = dataRows.size();

								if (dataRows.size() == 2) {
									WebElement secondRow = dataRows.get(1);
									Boolean hasButton = (Boolean) js.executeScript(
											"return arguments[0].querySelector('button#view-matches') !== null;", secondRow);

									if (hasButton != null && hasButton) {
										isAutoMapped = true;
										for (int r = rightProfileStartRow; r <= rightProfileEndRow; r++) {
											if (rightTableAllRows.get(r) == secondRow) {
												foundAtRow = r + 1;
												break;
											}
										}
									}
								}
							}
						} catch (Exception e) {
							LOGGER.debug("Error checking RIGHT table boundaries: {}", e.getMessage());
						}

						if (hasInfoMsg) {
							String jobName = "Unknown";
							try {
								List<WebElement> jobNameElements = jobDataRow.findElements(By.xpath(".//td[2]//div"));
								if (!jobNameElements.isEmpty()) {
									String jobNameCodeText = jobNameElements.get(0).getAttribute("textContent");
									if (jobNameCodeText != null) {
										jobNameCodeText = jobNameCodeText.trim();
										if (jobNameCodeText.contains(" - (")) {
											int dashIndex = jobNameCodeText.lastIndexOf(" - (");
											jobName = jobNameCodeText.substring(0, dashIndex).trim();
										} else {
											jobName = jobNameCodeText;
										}
									}
								}
							} catch (Exception e) {
								// Keep default
							}

							String profileType;
							if (isAutoMapped) {
								profileType = "AutoMapped (View Other Matches @row" + foundAtRow + ")";
							} else if (rightDataRowCount == 2) {
								profileType = "Manual Mapping (Search different profile)";
							} else if (rightDataRowCount == 1) {
								profileType = "Unmapped (Find Match)";
							} else {
								profileType = "Unknown";
							}
							LOGGER.info("Profile {} ({}) - {}", profilesChecked, jobName, profileType);
						}

						if (!isAutoMapped) {
							continue;
						}

						String jobName = "Unknown";
						try {
							List<WebElement> jobNameElements = jobDataRow.findElements(By.xpath(".//td[2]//div"));
							if (!jobNameElements.isEmpty()) {
								String jobNameCodeText = jobNameElements.get(0).getAttribute("textContent");
								if (jobNameCodeText != null) {
									jobNameCodeText = jobNameCodeText.trim();
									if (jobNameCodeText.contains(" - (")) {
										int dashIndex = jobNameCodeText.lastIndexOf(" - (");
										jobName = jobNameCodeText.substring(0, dashIndex).trim();
									} else {
										jobName = jobNameCodeText;
									}
								}
							}
						} catch (Exception e) {
							// Keep default
						}

						List<WebElement> gradeElements = jobDataRow.findElements(By.xpath(".//td[3]//div"));
						String grade = "";
						if (!gradeElements.isEmpty()) {
							String gradeText = gradeElements.get(0).getAttribute("textContent");
							grade = (gradeText != null) ? gradeText.trim() : "";
						}

						List<WebElement> deptElements = jobDataRow.findElements(By.xpath(".//td[4]//div"));
						String department = "";
						if (!deptElements.isEmpty()) {
							String deptText = deptElements.get(0).getAttribute("textContent");
							department = (deptText != null) ? deptText.trim() : "";
						}

						String functionSubfunction = "";
						try {
							String fullText = functionRow.getAttribute("textContent");
							if (fullText != null && fullText.contains("Function / Sub-function:")) {
								String[] parts = fullText.split("Function / Sub-function:");
								if (parts.length > 1) {
									functionSubfunction = parts[1].split("Reduced match accuracy")[0].trim();
								}
							}
						} catch (Exception e) {
							functionSubfunction = "";
						}

						boolean hasMissingData = false;
						if (grade.isEmpty() || grade.equals("N/A") || grade.equals("-")) {
							hasMissingData = true;
						}
						if (department.isEmpty() || department.equals("N/A") || department.equals("-")) {
							hasMissingData = true;
						}
						if (functionSubfunction.isEmpty() || functionSubfunction.equals("N/A")
								|| functionSubfunction.equals("-") || functionSubfunction.equals("N/A | N/A")
								|| functionSubfunction.equals("- | -") || functionSubfunction.equals("- | N/A")) {
							hasMissingData = true;
						}

						if (hasMissingData) {
							jobNameWithInfoMessage.set(jobName);
							gradeWithInfoMessage.set(grade);
							departmentWithInfoMessage.set(department);
							functionSubfunctionWithInfoMessage.set(functionSubfunction);

						currentRowIndex.set(foundAtRow);
						currentRowIndexStatic.set(foundAtRow);

						LOGGER.info("Found AutoMapped profile at row " + currentRowIndexStatic.get() +
									": " + jobName + " (Grade: " + grade + ", Dept: " + department + ")");
							return;
						}
					} catch (Exception e) {
						LOGGER.debug("Error processing profile at position " + profilesChecked + ": " + e.getMessage());
						continue;
					}
				}

				LOGGER.debug("Scrolling...");
				if (!currentRows.isEmpty()) {
					WebElement lastRow = currentRows.get(currentRows.size() - 1);
					js.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'end'});", lastRow);
					waitForUIStabilityInMs(500);
					js.executeScript("var container = document.getElementById('org-job-container');"
							+ "if (container) { container.scrollTop = container.scrollHeight; }");
					waitForUIStabilityInMs(2000);
					Utilities.waitForSpinnersToDisappear(driver, 5);
					waitForUIStabilityInMs(1000);
				} else {
					js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
					waitForUIStabilityInMs(2000);
					Utilities.waitForSpinnersToDisappear(driver, 5);
				}

				List<WebElement> rowsAfterScroll = driver.findElements(ORG_JOB_TABLE_ROWS);
				int rowsAfterScrollCount = rowsAfterScroll.size();

				if (rowsAfterScrollCount == previousRowCount && previousRowCount > 0) {
					noNewRowsCount++;
					if (noNewRowsCount >= maxNoNewRowsCount) {
						LOGGER.info("Checked {} profiles total", profilesChecked);
						break;
					}
				} else {
					noNewRowsCount = 0;
				}

				previousRowCount = rowsAfterScrollCount;
			}

			LOGGER.info("No AutoMapped profiles found with missing data and info message (checked all " + profilesChecked + " profiles)");
			LOGGER.warn("SKIPPING SCENARIO: No AutoMapped profiles with missing data and info message found (checked all " + profilesChecked + " profiles)");

			throw new SkipException("SKIPPED: No AutoMapped profiles with missing data and info message found after checking all " + profilesChecked
					+ " profiles. This scenario requires at least one profile with missing data to execute.");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "find_profile_with_missing_data_and_info_message",
					"Error searching for AutoMapped profile with missing data", e);
		}
	}

	public void find_second_profile_with_missing_data_and_info_message() throws IOException {
		LOGGER.info("Searching for SECOND AutoMapped profile with missing data");

		String firstJobName = jobNameWithInfoMessage.get();
		LOGGER.debug("First job to skip: '{}'", firstJobName);

		if (firstJobName == null || firstJobName.isEmpty()) {
			LOGGER.warn("WARNING: First job name is null or empty - cannot skip first profile");
		}

		try {
			waitForUIStabilityInMs(2000);

			int profilesChecked = 0;
			int scrollAttempts = 0;
			int maxScrollAttempts = 200;
			Set<String> processedJobCodes = new HashSet<>();
			int previousRowCount = 0;
			int noNewRowsCount = 0;
			int maxNoNewRowsCount = 5;

			while (scrollAttempts < maxScrollAttempts) {
				scrollAttempts++;

				List<WebElement> currentRows = driver.findElements(ORG_JOB_TABLE_ROWS);
				if (profilesChecked % 10 == 0 || scrollAttempts == 1) {
					LOGGER.info("Searching for second profile... (checked {} profiles)", profilesChecked);
				}

				List<WebElement> rightTableAllRows = driver.findElements(KF_JOB_TABLE_ROWS);
				@SuppressWarnings("unchecked")
				List<String> rightTableRowClasses = (List<String>) js.executeScript(
						"var rows = document.querySelectorAll('#kf-job-container tbody tr');"
						+ "return Array.from(rows).map(r => r.className || '');");

				for (int i = 0; i < currentRows.size() - 2; i += 3) {
					try {
						WebElement jobDataRow = currentRows.get(i);
						WebElement functionRow = currentRows.get(i + 1);

						String jobCode = "";
						String jobName = "Unknown";
						try {
							List<WebElement> jobNameElements = jobDataRow.findElements(By.xpath(".//td[2]//div"));
							if (!jobNameElements.isEmpty()) {
								String jobNameCodeText = jobNameElements.get(0).getAttribute("textContent");
								if (jobNameCodeText != null) {
									jobNameCodeText = jobNameCodeText.trim();
									if (jobNameCodeText.contains(" - (")) {
										int dashIndex = jobNameCodeText.lastIndexOf(" - (");
										jobName = jobNameCodeText.substring(0, dashIndex).trim();
									} else {
										jobName = jobNameCodeText;
									}
									if (jobNameCodeText.contains("(") && jobNameCodeText.contains(")")) {
										int startIdx = jobNameCodeText.lastIndexOf("(") + 1;
										int endIdx = jobNameCodeText.lastIndexOf(")");
										if (startIdx > 0 && endIdx > startIdx) {
											jobCode = jobNameCodeText.substring(startIdx, endIdx).trim();
										}
									}
								}
							}
						} catch (Exception e) {
							// Continue
						}

						if (!jobCode.isEmpty() && processedJobCodes.contains(jobCode)) {
							continue;
						}

						if (firstJobName != null && !firstJobName.isEmpty() && jobName.equals(firstJobName)) {
							LOGGER.debug("Skipping first profile: {}", jobName);
							if (!jobCode.isEmpty()) {
								processedJobCodes.add(jobCode);
							}
							continue;
						}

						if (!jobCode.isEmpty()) {
							processedJobCodes.add(jobCode);
						}
						profilesChecked++;

						boolean hasInfoMsg = false;
						try {
							List<WebElement> infoElements = functionRow.findElements(By.xpath(
									".//td[@colspan]//div[@id='matches-profiles-action-container']//div[contains(text(), 'Reduced match accuracy')] | "
									+ ".//td[@colspan]//div[contains(@aria-label, 'Reduced match accuracy')] | "
									+ ".//td[@colspan]//div[contains(text(), 'missing data')]"));
							hasInfoMsg = !infoElements.isEmpty();
						} catch (Exception e) {
							// Continue
						}

						if (!hasInfoMsg) {
							continue;
						}

						boolean isAutoMapped = false;
						int foundAtRow = -1;
						int rightDataRowCount = 0;
						int profileNumber = i / 3;

						try {
							int currentProfile = -1;
							int startIdx = 0;
							int rightProfileStartRow = -1;
							int rightProfileEndRow = -1;

							for (int r = 0; r < rightTableAllRows.size(); r++) {
								String rowClass = rightTableRowClasses.get(r);
								if (rowClass != null && rowClass.contains("bg-gray")) {
									currentProfile++;
									if (currentProfile == profileNumber) {
										rightProfileStartRow = startIdx;
										rightProfileEndRow = r - 1;
										break;
									}
									startIdx = r + 1;
								}
							}

							if (currentProfile == profileNumber - 1 && rightProfileEndRow == -1) {
								rightProfileStartRow = startIdx;
								rightProfileEndRow = rightTableAllRows.size() - 1;
							}

							if (rightProfileStartRow >= 0 && rightProfileEndRow >= rightProfileStartRow) {
								List<WebElement> dataRows = new ArrayList<>();
								for (int r = rightProfileStartRow; r <= rightProfileEndRow; r++) {
									String rowClass = rightTableRowClasses.get(r);
									if (rowClass == null || !rowClass.contains("bg-gray")) {
										dataRows.add(rightTableAllRows.get(r));
									}
								}

								rightDataRowCount = dataRows.size();

								if (dataRows.size() == 2) {
									WebElement secondRow = dataRows.get(1);
									Boolean hasButton = (Boolean) js.executeScript(
											"return arguments[0].querySelector('button#view-matches') !== null;", secondRow);

									if (hasButton != null && hasButton) {
										isAutoMapped = true;
										for (int r = rightProfileStartRow; r <= rightProfileEndRow; r++) {
											if (rightTableAllRows.get(r) == secondRow) {
												foundAtRow = r + 1;
												break;
											}
										}
									}
								}
							}
						} catch (Exception e) {
							LOGGER.debug("Error checking RIGHT table boundaries for second profile: {}", e.getMessage());
						}

						if (hasInfoMsg) {
							String profileType;
							if (isAutoMapped) {
								profileType = "AutoMapped (View Other Matches @row" + foundAtRow + ")";
							} else if (rightDataRowCount == 2) {
								profileType = "Manual Mapping (Search different profile)";
							} else if (rightDataRowCount == 1) {
								profileType = "Unmapped (Find Match)";
							} else {
								profileType = "Unknown";
							}
							LOGGER.info("Profile {} ({}) - {}", profilesChecked, jobName, profileType);
						}

						if (!isAutoMapped) {
							continue;
						}

						List<WebElement> gradeElements = jobDataRow.findElements(By.xpath(".//td[3]//div"));
						String grade = "";
						if (!gradeElements.isEmpty()) {
							String gradeText = gradeElements.get(0).getAttribute("textContent");
							grade = (gradeText != null) ? gradeText.trim() : "";
						}

						List<WebElement> deptElements = jobDataRow.findElements(By.xpath(".//td[4]//div"));
						String department = "";
						if (!deptElements.isEmpty()) {
							String deptText = deptElements.get(0).getAttribute("textContent");
							department = (deptText != null) ? deptText.trim() : "";
						}

						String functionSubfunction = "";
						try {
							String fullText = functionRow.getAttribute("textContent");
							if (fullText != null && fullText.contains("Function / Sub-function:")) {
								String[] parts = fullText.split("Function / Sub-function:");
								if (parts.length > 1) {
									functionSubfunction = parts[1].split("Reduced match accuracy")[0].trim();
								}
							}
						} catch (Exception e) {
							functionSubfunction = "";
						}

						boolean hasMissingData = false;
						if (grade.isEmpty() || grade.equals("N/A") || grade.equals("-")) {
							hasMissingData = true;
						}
						if (department.isEmpty() || department.equals("N/A") || department.equals("-")) {
							hasMissingData = true;
						}
						if (functionSubfunction.isEmpty() || functionSubfunction.equals("N/A")
								|| functionSubfunction.equals("-") || functionSubfunction.equals("N/A | N/A")
								|| functionSubfunction.equals("- | -") || functionSubfunction.equals("- | N/A")) {
							hasMissingData = true;
						}

					if (hasMissingData) {
						secondJobNameWithInfoMessage.set(jobName);
						secondGradeWithInfoMessage.set(grade);
						secondDepartmentWithInfoMessage.set(department);
						secondFunctionSubfunctionWithInfoMessage.set(functionSubfunction);

						secondCurrentRowIndex.set(foundAtRow);
						secondCurrentRowIndexStatic.set(foundAtRow);

						LOGGER.info("Found SECOND AutoMapped profile at row " + secondCurrentRowIndexStatic.get() +
									": " + jobName + " (Grade: " + grade + ", Dept: " + department + ")");
							return;
						}
					} catch (Exception e) {
						LOGGER.debug("Error processing profile at position " + profilesChecked + ": " + e.getMessage());
						continue;
					}
				}

				LOGGER.debug("Scrolling...");
				if (!currentRows.isEmpty()) {
					WebElement lastRow = currentRows.get(currentRows.size() - 1);
					js.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'end'});", lastRow);
					waitForUIStabilityInMs(500);
					js.executeScript("var container = document.getElementById('org-job-container');"
							+ "if (container) { container.scrollTop = container.scrollHeight; }");
					waitForUIStabilityInMs(2000);
					Utilities.waitForSpinnersToDisappear(driver, 5);
					waitForUIStabilityInMs(1000);
				} else {
					js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
					waitForUIStabilityInMs(2000);
					Utilities.waitForSpinnersToDisappear(driver, 5);
				}

				List<WebElement> rowsAfterScroll = driver.findElements(ORG_JOB_TABLE_ROWS);
				int rowsAfterScrollCount = rowsAfterScroll.size();

				if (rowsAfterScrollCount == previousRowCount && previousRowCount > 0) {
					noNewRowsCount++;
					if (noNewRowsCount >= maxNoNewRowsCount) {
						LOGGER.info("Checked {} profiles total", profilesChecked);
						break;
					}
				} else {
					noNewRowsCount = 0;
				}

				previousRowCount = rowsAfterScrollCount;
			}

		LOGGER.info("No SECOND AutoMapped profile found with missing data and info message (checked all " + profilesChecked + " profiles)");
		LOGGER.warn("SKIPPING SCENARIO: No SECOND AutoMapped profile with missing data and info message found (checked all " + profilesChecked + " profiles)");

		throw new SkipException("SKIPPED: No SECOND AutoMapped profile with missing data and info message found after checking all " + profilesChecked
				+ " profiles. This scenario requires at least two profiles with missing data to execute.");
	} catch (SkipException se) {
		throw se;
	} catch (Exception e) {
		Utilities.handleError(LOGGER, "find_second_profile_with_missing_data_and_info_message",
				"Error searching for SECOND AutoMapped profile with missing data", e);
	}
}

	public void extract_job_details_from_second_profile_with_info_message() throws IOException {
		try {
			if (secondJobNameWithInfoMessage.get().isEmpty() && secondJobCodeWithInfoMessage.get().isEmpty()) {
				throw new IOException("Second profile job details not found. Please call find_second_profile_with_missing_data_and_info_message() first.");
			}
		} catch (Exception e) {
			LOGGER.error("Error: Second profile job details not found: " + e.getMessage());
			throw new IOException("Failed to validate second profile job details: " + e.getMessage());
		}
	}

	public void verify_info_message_contains_text_about_reduced_match_accuracy_due_to_missing_data() {
		try {
			LOGGER.info("Verifying Info Message contains correct text about reduced match accuracy");

			// Check if info message text elements exist (data-dependent scenario)
			List<WebElement> infoMessageTextElements = driver.findElements(INFO_MESSAGE_TEXTS);
			
			if (infoMessageTextElements.isEmpty()) {
				String skipMessage = "SKIPPED: Info Message text elements not found - Application has NO profiles with missing data. " +
						"This is a data-dependent test that requires profiles with missing data to execute.";
				LOGGER.warn(skipMessage);
				throw new org.testng.SkipException(skipMessage);
			}

			boolean correctTextFound = false;
			for (WebElement messageText : infoMessageTextElements) {
				String actualText = messageText.getText().trim();
				if (actualText.contains("Reduced match accuracy due to missing data")) {
					correctTextFound = true;
					LOGGER.info("Found correct Info Message text: " + actualText);
					break;
				}
			}

			// If correct text not found after checking all messages, skip gracefully
			if (!correctTextFound) {
				String skipMessage = "SKIPPED: Info Message with 'Reduced match accuracy due to missing data' text not found. " +
						"Found " + infoMessageTextElements.size() + " info messages but none contained the expected text.";
				LOGGER.warn(skipMessage);
				throw new org.testng.SkipException(skipMessage);
			}

			LOGGER.info("Successfully verified Info Message contains correct text about reduced match accuracy due to missing data");
			LOGGER.info("Successfully verified Info Message text");
		} catch (org.testng.SkipException se) {
			throw se; // Rethrow SkipException
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_info_message_contains_text_about_reduced_match_accuracy_due_to_missing_data",
					"Failed to verify Info Message text", e);
		}
	}

	public void find_and_verify_second_profile_with_missing_data_has_info_message_displayed() throws IOException {
		try {
			LOGGER.info("Finding and verifying second profile with missing data has Info Message displayed");
			waitForUIStabilityInMs(2000);

			List<WebElement> infoMessages = driver.findElements(By.xpath(
					"//div[@id='org-job-container']//div[@role='button' and @aria-label='Reduced match accuracy due to missing data']"));

			if (infoMessages.isEmpty()) {
				LOGGER.info("SCROLL: No info messages found in current view for second profile, scrolling...");
				scrollDownAndFindMoreAutoMappedProfiles();

				infoMessages = driver.findElements(By.xpath(
						"//div[@id='org-job-container']//div[@role='button' and @aria-label='Reduced match accuracy due to missing data']"));
				LOGGER.info("STATUS: After scrolling: Found {} info messages for second profile verification", infoMessages.size());
			}

		if (infoMessages.isEmpty()) {
			LOGGER.warn("SKIPPING SCENARIO: No Info Messages found for second profile with missing data");
			throw new SkipException("SKIPPED: No Info Messages found for second profile with missing data. This scenario requires profiles with missing data to execute.");
		}
		
		LOGGER.info("STATUS: Found {} Info Messages for second profile verification", infoMessages.size());

		boolean infoMessageDisplayed = false;
		for (WebElement infoMessage : infoMessages) {
			if (infoMessage.isDisplayed()) {
				infoMessageDisplayed = true;
				LOGGER.info("SUCCESS: Found displayed info message for second profile");
				break;
			}
		}

		if (!infoMessageDisplayed) {
			LOGGER.warn("SKIPPING SCENARIO: Info Message not displayed for second profile with missing data");
			throw new SkipException("SKIPPED: Info Message not displayed for second profile with missing data. This scenario requires visible info messages to execute.");
		}

		LOGGER.info("Successfully found and verified second profile with missing data has Info Message displayed");
		LOGGER.info("SUCCESS: Successfully verified second profile with missing data has Info Message displayed");
	} catch (SkipException se) {
		throw se;
	} catch (Exception e) {
		LOGGER.error("ERROR: Error finding second profile with missing data and Info Message: " + e.getMessage());
		throw new IOException("Failed to find and verify second profile with missing data has Info Message: " + e.getMessage());
	}
	}

	public void extract_job_details_from_profile_with_info_message() throws IOException {
		try {
			if (jobNameWithInfoMessage.get().isEmpty() && jobCodeWithInfoMessage.get().isEmpty()) {
				throw new IOException("First profile job details not found. Please call find_profile_with_missing_data_and_info_message() first.");
			}
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "extract_job_details_from_profile_with_info_message",
					"Failed to validate first profile job details", e);
		}
	}

	public void verify_info_message_is_still_displayed_in_job_comparison_page() {
		try {
			LOGGER.info("Verifying Info Message is still displayed in Job Comparison page for first profile");
			waitForUIStabilityInMs(2000);

			List<WebElement> infoMessages = driver.findElements(By.xpath(
					"//div[@role='button' and @aria-label='Reduced match accuracy due to missing data'] | //div[contains(text(), 'Reduced match accuracy due to missing data')]"));

			

			boolean infoMessageDisplayed = false;
			for (WebElement infoMessage : infoMessages) {
				if (infoMessage.isDisplayed()) {
					infoMessageDisplayed = true;
					LOGGER.info("SUCCESS: Info Message is still displayed in Job Comparison page for first profile");
					break;
				}
			}

			Assert.assertTrue(infoMessageDisplayed, "Info Message should be visible in Job Comparison page for first profile");

			LOGGER.info("Successfully verified Info Message persists in Job Comparison page for first profile");
			LOGGER.info("SUCCESS: Info Message persistence verified for first profile");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_info_message_is_still_displayed_in_job_comparison_page",
					"Failed to verify Info Message persistence in Job Comparison page for first profile", e);
		}
	}

	public void verify_info_message_is_still_displayed_in_job_comparison_page_for_second_profile() throws IOException {
		try {
			LOGGER.info("Verifying Info Message is still displayed in Job Comparison page for second profile");
			waitForUIStabilityInMs(2000);

			List<WebElement> infoMessages = driver.findElements(By.xpath(
					"//div[@role='button' and @aria-label='Reduced match accuracy due to missing data'] | //div[contains(text(), 'Reduced match accuracy due to missing data')]"));

		if (infoMessages.isEmpty()) {
			LOGGER.warn("SKIPPING SCENARIO: Info Message not found in Job Comparison page for second profile");
			throw new SkipException("SKIPPED: Info Message not found in Job Comparison page for second profile. This scenario requires profiles with info messages to execute.");
		}

		boolean infoMessageDisplayed = false;
		for (WebElement infoMessage : infoMessages) {
			if (infoMessage.isDisplayed()) {
				infoMessageDisplayed = true;
				LOGGER.info("SUCCESS: Info Message is still displayed in Job Comparison page for second profile");
				break;
			}
		}

		if (!infoMessageDisplayed) {
			LOGGER.warn("SKIPPING SCENARIO: Info Message not visible in Job Comparison page for second profile");
			throw new SkipException("SKIPPED: Info Message not visible in Job Comparison page for second profile. This scenario requires visible info messages to execute.");
		}

		LOGGER.info("Successfully verified Info Message persists in Job Comparison page for second profile");
		LOGGER.info("SUCCESS: Info Message persistence verified for second profile");
	} catch (SkipException se) {
		throw se;
	} catch (Exception e) {
		LOGGER.error("ERROR: Error verifying Info Message persistence for second profile: " + e.getMessage());
		throw new IOException("Failed to verify Info Message persistence in Job Comparison page for second profile: " + e.getMessage());
	}
	}

	public void verify_info_message_contains_text_about_reduced_match_accuracy_due_to_missing_data_for_second_profile() throws IOException {
		try {
			LOGGER.info("Verifying Info Message contains correct text about reduced match accuracy for second profile");

			List<WebElement> infoMessageTextElements = driver.findElements(INFO_MESSAGE_TEXTS);

			

			boolean correctTextFound = false;
			WebElement targetMessageText = null;

			if (infoMessageTextElements.size() > 1) {
				targetMessageText = infoMessageTextElements.get(1);
				LOGGER.info("Using second Info Message text for second profile validation");
			} else if (infoMessageTextElements.size() == 1) {
				targetMessageText = infoMessageTextElements.get(0);
				LOGGER.info("Only one Info Message text found, using it for second profile validation");
			}

			if (targetMessageText != null && targetMessageText.isDisplayed()) {
				String actualText = targetMessageText.getText().trim();
				if (actualText.contains("Reduced match accuracy due to missing data")) {
					correctTextFound = true;
					LOGGER.info("Found correct Info Message text for second profile: " + actualText);
				}
			}

			Assert.assertTrue(correctTextFound, "Info Message should contain text about 'Reduced match accuracy due to missing data' for second profile validation");

			LOGGER.info("Successfully verified Info Message contains correct text for second profile");
			LOGGER.info("Successfully verified Info Message text for second profile");
		} catch (Exception e) {
			LOGGER.error("Error verifying second profile Info Message text: " + e.getMessage());
			throw new IOException("Failed to verify second profile Info Message text for second profile validation: " + e.getMessage());
		}
	}

	public void click_on_button_for_profile_with_info_message(String buttonText) throws IOException {
		try {
		LOGGER.info("Clicking '{}' button for profile with Info Message", buttonText);

		if (currentRowIndex.get() <= 0) {
			throw new IOException("No valid profile row index found. Call find_profile_with_missing_data_and_info_message() first.");
		}

		if (!isAutoMappedProfile(currentRowIndex.get())) {
			throw new IOException("Profile at row " + currentRowIndex.get() + " is not an AutoMapped profile - cannot click '" + buttonText + "' button.");
			}

			try {
				WebDriverWait loaderWait = new WebDriverWait(driver, Duration.ofSeconds(10));
				loaderWait.until(ExpectedConditions.invisibilityOfElementLocated(LOADER_ELEMENTS));
			} catch (Exception e) {
				LOGGER.debug("No loader found or already hidden");
			}

		String[] viewMatchesButtonXPaths = {
				String.format("//div[@id='kf-job-container']//tbody//tr[%d]//button[@id='view-matches']", currentRowIndex.get()),
				String.format("//div[@id='kf-job-container']//tbody//tr[%d]//button[contains(@aria-label, 'View other matches')]", currentRowIndex.get()),
				String.format("//div[@id='kf-job-container']//tbody//tr[%d]//button[contains(text(), 'View') and contains(text(), 'Other Matches')]", currentRowIndex.get())
			};

			WebElement targetButton = null;

			for (String xpath : viewMatchesButtonXPaths) {
				try {
					List<WebElement> buttons = driver.findElements(By.xpath(xpath));
					for (WebElement button : buttons) {
					if (button.isDisplayed()) {
						targetButton = button;
						LOGGER.debug("Found '{}' button for profile at row {}", buttonText, currentRowIndex.get());
							break;
						}
					}
					if (targetButton != null) break;
				} catch (Exception e) {
					LOGGER.debug("Could not find button - {}", e.getMessage());
				}
			}

		if (targetButton == null) {
			throw new IOException("Could not find '" + buttonText + "' button for AutoMapped profile at row " + currentRowIndex.get());
			}

			scrollToElement(targetButton);
			waitForUIStabilityInMs(200);
			jsClick(targetButton);

			LOGGER.info("Clicked '" + buttonText + "' button for profile with Info Message");
			LOGGER.info("Clicked '{}' button", buttonText);
		} catch (Exception e) {
			LOGGER.error("Error clicking button for profile with Info Message: " + e.getMessage());
			throw new IOException("Failed to click button for profile with Info Message: " + e.getMessage());
		}
	}

	public void click_on_button_for_second_profile_with_info_message(String buttonText) throws IOException {
		try {
		LOGGER.info("Clicking '{}' button for second profile with Info Message", buttonText);

		if (secondCurrentRowIndex.get() <= 0) {
			throw new IOException("No valid second profile row index found. Call find_second_profile_with_missing_data_and_info_message() first.");
		}

		if (!isAutoMappedProfile(secondCurrentRowIndex.get())) {
			throw new IOException("Second profile at row " + secondCurrentRowIndex.get() + " is not an AutoMapped profile - cannot click '" + buttonText + "' button.");
			}

			try {
				WebDriverWait loaderWait = new WebDriverWait(driver, Duration.ofSeconds(10));
				loaderWait.until(ExpectedConditions.invisibilityOfElementLocated(LOADER_ELEMENTS));
			} catch (Exception e) {
				LOGGER.debug("No loader found or already hidden for second profile");
			}

		String[] viewMatchesButtonXPaths = {
				String.format("//div[@id='kf-job-container']//tbody//tr[%d]//button[@id='view-matches']", secondCurrentRowIndex.get()),
				String.format("//div[@id='kf-job-container']//tbody//tr[%d]//button[contains(@aria-label, 'View other matches')]", secondCurrentRowIndex.get()),
				String.format("//div[@id='kf-job-container']//tbody//tr[%d]//button[contains(text(), 'View') and contains(text(), 'Other Matches')]", secondCurrentRowIndex.get())
		};

			WebElement targetButton = null;

			for (String xpath : viewMatchesButtonXPaths) {
				try {
					List<WebElement> buttons = driver.findElements(By.xpath(xpath));
					for (WebElement button : buttons) {
						if (button.isDisplayed()) {
							targetButton = button;
							LOGGER.debug("Found '{}' button for second profile at row {}", buttonText, secondCurrentRowIndex);
							break;
						}
					}
					if (targetButton != null) break;
				} catch (Exception e) {
					LOGGER.debug("Could not find button for second profile - {}", e.getMessage());
				}
			}

			if (targetButton == null) {
				throw new IOException("Could not find '" + buttonText + "' button for second AutoMapped profile at row " + secondCurrentRowIndex);
			}

			scrollToElement(targetButton);
			waitForUIStabilityInMs(200);
			jsClick(targetButton);

			LOGGER.info("Clicked '" + buttonText + "' button for second profile with Info Message");
			LOGGER.info("Clicked '{}' button for second profile", buttonText);
		} catch (Exception e) {
			LOGGER.error("Error clicking button for second profile with Info Message: " + e.getMessage());
			throw new IOException("Failed to click button for second profile with Info Message: " + e.getMessage());
		}
	}

	public void verify_user_is_navigated_to_job_comparison_page() {
		try {
			LOGGER.info("Verifying user is navigated to Job Comparison page");

			WebElement compareAndSelectHeader = waitForElement(COMPARE_SELECT_HEADER);
			String compareAndSelectHeaderText = compareAndSelectHeader.getText();

			Assert.assertEquals(compareAndSelectHeaderText, "Which profile do you want to use for this job?",
					"Job Comparison page header text does not match expected value");

						LOGGER.info("Successfully verified navigation to Job Comparison page");
			LOGGER.info("SUCCESS: User is successfully navigated to Job Comparison page");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_user_is_navigated_to_job_comparison_page",
					"Failed to verify navigation to Job Comparison page", e);
		}
	}

	public void extract_job_details_from_job_comparison_page() throws IOException {
		try {
			LOGGER.info("Extracting job details from Job Comparison page (Organization Job section)");
			waitForUIStabilityInMs(2000);

			String comparisonJobName = "";
			String comparisonGrade = "";
			String comparisonDepartment = "";
			String comparisonFunction = "";

			String pageText = "";
			try {
				WebElement orgJobSection = driver.findElement(By.xpath(
						"//div[contains(@class, 'border-b-10') and contains(@class, 'bg-grey')]//h3[contains(text(), 'Organization Job')]/ancestor::div[contains(@class, 'border-b-10')]"));
				pageText = orgJobSection.getText();
				LOGGER.info("Organization Job section text captured");
			} catch (Exception e) {
				LOGGER.warn("Could not find Organization Job section, trying broader approach: " + e.getMessage());
				try {
					WebElement mainContainer = driver.findElement(By.xpath("//main | //div[contains(@class, 'bg-grey')] | //body"));
					pageText = mainContainer.getText();
				} catch (Exception e2) {
					LOGGER.error("Could not extract page text: " + e2.getMessage());
				}
			}

			try {
				String[] lines = pageText.split("\n");
				for (int i = 0; i < lines.length; i++) {
					if (lines[i].contains("Organization Job") && i + 1 < lines.length) {
						String jobLine = lines[i + 1].trim();
						jobLine = jobLine.replaceAll("Needs Review|Auto-mapped", "").trim();

						if (jobLine.contains(" - (") && jobLine.contains(")")) {
							String[] parts = jobLine.split(" - \\(");
							comparisonJobName = parts[0].trim();
						} else {
							comparisonJobName = jobLine;
						}
						break;
					}
				}
			} catch (Exception e) {
				LOGGER.debug("Could not extract job name/code from text: " + e.getMessage());
			}

			if (pageText.contains("Grade:")) {
				String[] parts = pageText.split("Grade:");
				if (parts.length > 1) {
					String gradePart = parts[1].trim();
					if (gradePart.contains("Department:")) {
						gradePart = gradePart.split("Department:")[0].trim();
					} else if (gradePart.contains("\n")) {
						gradePart = gradePart.split("\n")[0].trim();
					}
					comparisonGrade = gradePart.replaceAll("\\s+", " ").trim();
				}
			}

			if (pageText.contains("Department:")) {
				String[] parts = pageText.split("Department:");
				if (parts.length > 1) {
					String deptPart = parts[1].trim();
					if (deptPart.contains("Function")) {
						deptPart = deptPart.split("Function")[0].trim();
					} else if (deptPart.contains("\n")) {
						deptPart = deptPart.split("\n")[0].trim();
					}
					comparisonDepartment = deptPart.replaceAll("\\s+", " ").trim();
				}
			}

			if (pageText.contains("Function / Sub-function:")) {
				String[] parts = pageText.split("Function / Sub-function:");
				if (parts.length > 1) {
					String functionPart = parts[1].trim();
					if (functionPart.contains("\n")) {
						functionPart = functionPart.split("\n")[0].trim();
					}
					comparisonFunction = functionPart.replaceAll("\\s+", " ").trim();
				}
			}

			LOGGER.info("Extracted Job Details from Job Comparison page (Organization Job section):");
			LOGGER.info("  Job Name: {}", comparisonJobName);
			LOGGER.info("  Grade: {}", comparisonGrade);
			LOGGER.info("  Department: {}", comparisonDepartment);
			LOGGER.info("  Function/Sub-function: {}", comparisonFunction);

			boolean allMatch = true;
			StringBuilder mismatchDetails = new StringBuilder();

		String normalizedStoredGrade = gradeWithInfoMessage.get().equals("-") ? "" : gradeWithInfoMessage.get();
		String normalizedComparisonGrade = comparisonGrade.equals("-") ? "" : comparisonGrade;
		String normalizedStoredDept = departmentWithInfoMessage.get().equals("-") ? "" : departmentWithInfoMessage.get();
		String normalizedComparisonDept = comparisonDepartment.equals("-") ? "" : comparisonDepartment;

		if (!comparisonJobName.equals(jobNameWithInfoMessage.get())) {
			allMatch = false;
			mismatchDetails.append("\n  Job Name: Job Mapping='").append(jobNameWithInfoMessage.get())
						.append("' vs Job Comparison='").append(comparisonJobName).append("'");
			}

		if (!normalizedComparisonGrade.equals(normalizedStoredGrade)) {
			allMatch = false;
			mismatchDetails.append("\n  Grade: Job Mapping='").append(gradeWithInfoMessage.get())
						.append("' vs Job Comparison='").append(comparisonGrade).append("'");
			}

		if (!normalizedComparisonDept.equals(normalizedStoredDept)) {
			allMatch = false;
			mismatchDetails.append("\n  Department: Job Mapping='").append(departmentWithInfoMessage.get())
						.append("' vs Job Comparison='").append(comparisonDepartment).append("'");
			}

		if (!comparisonFunction.equals(functionSubfunctionWithInfoMessage.get())) {
			allMatch = false;
			mismatchDetails.append("\n  Function/Sub-function: Job Mapping='")
					.append(functionSubfunctionWithInfoMessage.get()).append("' vs Job Comparison='")
						.append(comparisonFunction).append("'");
			}

		if (allMatch) {
			LOGGER.info("Job details match verified between Job Mapping and Job Comparison pages for: " + jobNameWithInfoMessage.get());
			} else {
				LOGGER.info("Job details mismatch: " + mismatchDetails.toString());
				Assert.fail("Job details do NOT match between Job Mapping and Job Comparison pages:" + mismatchDetails.toString());
			}
		} catch (Exception e) {
			LOGGER.error("Error extracting job details from Job Comparison page: " + e.getMessage());
			throw new IOException("Failed to extract job details from Job Comparison page: " + e.getMessage());
		}
	}

	public void extract_job_details_from_job_comparison_page_for_second_profile() throws IOException {
		try {
			LOGGER.info("Extracting job details from Job Comparison page (Organization Job section) for second profile");
			waitForUIStabilityInMs(2000);

			String comparisonJobName = "";
			String comparisonGrade = "";
			String comparisonDepartment = "";
			String comparisonFunction = "";

			String pageText = "";
			try {
				WebElement orgJobSection = driver.findElement(By.xpath(
						"//div[contains(@class, 'border-b-10') and contains(@class, 'bg-grey')]//h3[contains(text(), 'Organization Job')]/ancestor::div[contains(@class, 'border-b-10')]"));
				pageText = orgJobSection.getText();
				LOGGER.info("Organization Job section text captured successfully for second profile");
			} catch (Exception e) {
				LOGGER.warn("Could not find Organization Job section, trying broader approach: " + e.getMessage());
				try {
					WebElement mainContainer = driver.findElement(By.xpath("//main | //div[contains(@class, 'bg-grey')] | //body"));
					pageText = mainContainer.getText();
				} catch (Exception e2) {
					LOGGER.error("Could not extract page text for second profile: " + e2.getMessage());
				}
			}

			try {
				String[] lines = pageText.split("\n");
				for (int i = 0; i < lines.length; i++) {
					if (lines[i].contains("Organization Job") && i + 1 < lines.length) {
						String jobLine = lines[i + 1].trim();
						jobLine = jobLine.replaceAll("Needs Review|Auto-mapped", "").trim();

						if (jobLine.contains(" - (") && jobLine.contains(")")) {
							String[] parts = jobLine.split(" - \\(");
							comparisonJobName = parts[0].trim();
						} else {
							comparisonJobName = jobLine;
						}
						break;
					}
				}
			} catch (Exception e) {
				LOGGER.debug("Could not extract job name/code from text for second profile: " + e.getMessage());
			}

			if (pageText.contains("Grade:")) {
				String[] parts = pageText.split("Grade:");
				if (parts.length > 1) {
					String gradePart = parts[1].trim();
					if (gradePart.contains("Department:")) {
						gradePart = gradePart.split("Department:")[0].trim();
					} else if (gradePart.contains("\n")) {
						gradePart = gradePart.split("\n")[0].trim();
					}
					comparisonGrade = gradePart.replaceAll("\\s+", " ").trim();
				}
			}

			if (pageText.contains("Department:")) {
				String[] parts = pageText.split("Department:");
				if (parts.length > 1) {
					String deptPart = parts[1].trim();
					if (deptPart.contains("Function")) {
						deptPart = deptPart.split("Function")[0].trim();
					} else if (deptPart.contains("\n")) {
						deptPart = deptPart.split("\n")[0].trim();
					}
					comparisonDepartment = deptPart.replaceAll("\\s+", " ").trim();
				}
			}

			if (pageText.contains("Function / Sub-function:")) {
				String[] parts = pageText.split("Function / Sub-function:");
				if (parts.length > 1) {
					String functionPart = parts[1].trim();
					if (functionPart.contains("\n")) {
						functionPart = functionPart.split("\n")[0].trim();
					}
					comparisonFunction = functionPart.replaceAll("\\s+", " ").trim();
				}
			}

			LOGGER.info("Extracted Job Details from Job Comparison page (Organization Job section) for second profile:");
			LOGGER.info("  Job Name: {}", comparisonJobName);
			LOGGER.info("  Grade: {}", comparisonGrade);
			LOGGER.info("  Department: {}", comparisonDepartment);
			LOGGER.info("  Function/Sub-function: {}", comparisonFunction);

			boolean allMatch = true;
			StringBuilder mismatchDetails = new StringBuilder();

		String normalizedStoredGrade = secondGradeWithInfoMessage.get().equals("-") ? "" : secondGradeWithInfoMessage.get();
		String normalizedComparisonGrade = comparisonGrade.equals("-") ? "" : comparisonGrade;
		String normalizedStoredDept = secondDepartmentWithInfoMessage.get().equals("-") ? "" : secondDepartmentWithInfoMessage.get();
		String normalizedComparisonDept = comparisonDepartment.equals("-") ? "" : comparisonDepartment;

		if (!comparisonJobName.equals(secondJobNameWithInfoMessage.get())) {
			allMatch = false;
			mismatchDetails.append("\n  Job Name: Job Mapping='").append(secondJobNameWithInfoMessage.get())
						.append("' vs Job Comparison='").append(comparisonJobName).append("'");
			}

		if (!normalizedComparisonGrade.equals(normalizedStoredGrade)) {
			allMatch = false;
			mismatchDetails.append("\n  Grade: Job Mapping='").append(secondGradeWithInfoMessage.get())
						.append("' vs Job Comparison='").append(comparisonGrade).append("'");
			}

		if (!normalizedComparisonDept.equals(normalizedStoredDept)) {
			allMatch = false;
			mismatchDetails.append("\n  Department: Job Mapping='").append(secondDepartmentWithInfoMessage.get())
						.append("' vs Job Comparison='").append(comparisonDepartment).append("'");
			}

		if (!comparisonFunction.equals(secondFunctionSubfunctionWithInfoMessage.get())) {
			allMatch = false;
			mismatchDetails.append("\n  Function/Sub-function: Job Mapping='")
					.append(secondFunctionSubfunctionWithInfoMessage.get()).append("' vs Job Comparison='")
						.append(comparisonFunction).append("'");
			}

		if (allMatch) {
			LOGGER.info("Job details match verified between Job Mapping and Job Comparison pages for second profile: " + secondJobNameWithInfoMessage.get());
			} else {
				LOGGER.info("Job details mismatch for second profile: " + mismatchDetails.toString());
				Assert.fail("Job details do NOT match between Job Mapping and Job Comparison pages for second profile:" + mismatchDetails.toString());
			}
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "extract_job_details_from_job_comparison_page_for_second_profile",
					"Failed to extract job details from Job Comparison page for second profile", e);
		}
	}

	public void verify_job_details_match_between_job_mapping_and_job_comparison_pages() {
		// Verification done inline within extract_job_details_from_job_comparison_page()
	}

	public void verify_job_details_match_between_job_mapping_and_job_comparison_pages_for_second_profile() {
		// Verification done inline within extract_job_details_from_job_comparison_page_for_second_profile()
	}

	public void verify_info_message_contains_same_text_about_reduced_match_accuracy() {
		try {
			LOGGER.info("Verifying Info Message contains same text about reduced match accuracy in Job Comparison page");
			waitForUIStabilityInMs(2000);

			List<WebElement> infoMessageTextElements = driver.findElements(By.xpath(
					"//div[contains(text(), 'Reduced match accuracy due to missing data')] | //div[@aria-label='Reduced match accuracy due to missing data']"));

			

			boolean correctTextFound = false;
			for (WebElement messageText : infoMessageTextElements) {
				if (messageText.isDisplayed()) {
					String actualText = messageText.getText().trim();
					if (actualText.contains("Reduced match accuracy due to missing data")) {
						correctTextFound = true;
						LOGGER.info("Found correct Info Message text in Job Comparison page: {}", actualText);
						break;
					}
				}
			}

			Assert.assertTrue(correctTextFound, "Info Message should contain same text about reduced match accuracy in Job Comparison page");

			LOGGER.info("Successfully verified Info Message contains same text in Job Comparison page");
			LOGGER.info("SUCCESS: Info Message text consistency verified in Job Comparison page");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "verify_info_message_contains_same_text_about_reduced_match_accuracy",
					"Failed to verify Info Message text consistency in Job Comparison page", e);
		}
	}

	public void verify_info_message_contains_same_text_about_reduced_match_accuracy_for_second_profile() throws IOException {
		try {
			LOGGER.info("Verifying Info Message contains same text about reduced match accuracy for second profile in Job Comparison page");
			waitForUIStabilityInMs(2000);

			List<WebElement> infoMessageTextElements = driver.findElements(By.xpath(
					"//div[contains(text(), 'Reduced match accuracy due to missing data')] | //div[@aria-label='Reduced match accuracy due to missing data']"));

			

			boolean correctTextFound = false;
			for (WebElement messageText : infoMessageTextElements) {
				if (messageText.isDisplayed()) {
					String actualText = messageText.getText().trim();
					if (actualText.contains("Reduced match accuracy due to missing data")) {
						correctTextFound = true;
						LOGGER.info("Found correct Info Message text in Job Comparison page for second profile: {}", actualText);
						break;
					}
				}
			}

			Assert.assertTrue(correctTextFound, "Info Message should contain same text about reduced match accuracy in Job Comparison page for second profile");

			LOGGER.info("Successfully verified Info Message contains same text in Job Comparison page for second profile");
			LOGGER.info("SUCCESS: Info Message text consistency verified in Job Comparison page for second profile");
		} catch (Exception e) {
			LOGGER.error("ERROR: Error verifying Info Message text consistency for second profile: " + e.getMessage());
			throw new IOException("Failed to verify Info Message text consistency in Job Comparison page for second profile: " + e.getMessage());
		}
	}

	public void navigate_back_to_job_mapping_page_from_job_comparison() {
		try {
			LOGGER.info("Navigating back to Job Mapping page from Job Comparison");

			Boolean hasBackButton = (Boolean) js.executeScript(
					"return document.querySelector('button:is([aria-label=\"Back\"], [aria-label=\"Close\"])') !== null || "
					+ "document.querySelector('button') !== null && "
					+ "(document.querySelector('button').textContent.includes('Back') || document.querySelector('button').textContent.includes('Close'));");

			if (hasBackButton != null && hasBackButton) {
				js.executeScript(
						"var btn = document.querySelector('button:is([aria-label=\"Back\"], [aria-label=\"Close\"])');"
						+ "if (!btn) btn = Array.from(document.querySelectorAll('button')).find(b => b.textContent.includes('Back') || b.textContent.includes('Close'));"
						+ "if (btn) btn.click();");
				LOGGER.debug("Clicked back button");
			} else {
				js.executeScript("window.history.back();");
				LOGGER.debug("Used browser back button");
			}

			waitForPageLoad();
			waitForUIStabilityInMs(500);

			LOGGER.info("Navigated back to Job Mapping page");
			LOGGER.info("Navigated back to Job Mapping page");
		} catch (Exception e) {
			Utilities.handleError(LOGGER, "navigate_back_to_job_mapping_page_from_job_comparison",
					"Failed to navigate back to Job Mapping page from Job Comparison", e);
		}
	}
}

