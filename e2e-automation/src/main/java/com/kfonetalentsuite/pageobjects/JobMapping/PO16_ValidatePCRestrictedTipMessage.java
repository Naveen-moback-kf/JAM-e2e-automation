package com.kfonetalentsuite.pageobjects.JobMapping;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.kfonetalentsuite.utils.JobMapping.Utilities;
import com.kfonetalentsuite.utils.JobMapping.PerformanceUtils;
import com.kfonetalentsuite.utils.JobMapping.ScreenshotHandler;
import com.kfonetalentsuite.utils.JobMapping.SmartWaits;
import com.kfonetalentsuite.webdriverManager.DriverManager;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;


public class PO16_ValidatePCRestrictedTipMessage {
	WebDriver driver = DriverManager.getDriver();	

	protected static final Logger LOGGER = (Logger) LogManager.getLogger();
	PO16_ValidatePCRestrictedTipMessage validatePCRestrictedTipMessage;
	

	public PO16_ValidatePCRestrictedTipMessage() throws IOException {
		// super();
		PageFactory.initElements(driver, this);
		// TODO Auto-generated constructor stub
	}

	WebDriverWait wait = DriverManager.getWait();
	Utilities utils = new Utilities();
	JavascriptExecutor js = (JavascriptExecutor) driver;
	LocalDate currentdate = LocalDate.now();
	Month currentMonth = currentdate.getMonth();
	
	public static String teamName;
	public static String pcName;
	public static String spNameString = "Account Manager";
	
	//XAPTHs
	@FindBy(xpath = "//*[@class='blocking-loader']//img")
	@CacheLookup
	WebElement pageLoadSpinner;
	
	@FindBy(xpath = "//*[contains(@class,'kfheader-settings')]")
	@CacheLookup
	WebElement settingsIcon;
	
	@FindBy(xpath = "//*[contains(text(),'User Admin')]")
	@CacheLookup
	WebElement UAMbutton;
	
	@FindBy(xpath = "//*[contains(text(),'Configuration')]")
	@CacheLookup
	WebElement SystemConfigurationHeaderText;
	
	@FindBy(xpath = "//*[contains(@class,'kfcon-header')]")
	@CacheLookup
	WebElement clientNameText;
	
	@FindBy(xpath = "//h1[contains(text(),'Dashboard')]")
	@CacheLookup
	WebElement clientDashboard;
	
	@FindBy(xpath = "//*[contains(@icon,'usergroups')]")
	@CacheLookup
	WebElement teamsSection;
	
	@FindBy(xpath = "//*[contains(@class,'teams-header')]")
	@CacheLookup
	WebElement teamsPageHeader;
	
	@FindBy(xpath = "//*[contains(text(),'A Team is a group')]")
	@CacheLookup
	WebElement teamsPageDesc;
	
	@FindBy(xpath = "//*[contains(@placeholder,'Search Teams')]")
	@CacheLookup
	WebElement teamsPageSearchbar;
	
	@FindBy(xpath = "//*[contains(text(),'CREATE TEAMS')]")
	@CacheLookup
	WebElement teamsPageCreateTeamsBtn;
	
	@FindBy(xpath = "//*[contains(text(),'Step 1 of 2')]")
	@CacheLookup
	WebElement createTeamsFirstStepHeader;
	
	@FindBy(xpath = "//input[contains(@class,'primary')]")
	@CacheLookup
	WebElement teamNameTextbox;
	
	@FindBy(xpath = "//textarea[contains(@class,'primary')]")
	@CacheLookup
	WebElement teamDescTextbox;
	
	@FindBy(xpath = "//span[contains(text(),'Next')]")
	@CacheLookup
	WebElement nextBtn;
	
	@FindBy(xpath = "//*[contains(text(),'Step 2 of 2')]")
	@CacheLookup
	WebElement createTeamsSecondStepHeader;
	
	@FindBy(xpath = "//*[contains(@placeholder,'User Search')]")
	@CacheLookup
	WebElement createTeamsSecondStepSearchbar;
	
	@FindBy(xpath = "//*[contains(text(),'Team Members (')]")
	@CacheLookup
	WebElement teamMembersHeader;
	
	@FindBy(xpath = "//td[contains(@class,'check-box')]//kf-checkbox")
	@CacheLookup
	WebElement createTeamsTopRowCheckbox;
	
	@FindBy(xpath = "//*[contains(text(),'Showing 1')]")
	@CacheLookup
	WebElement userCount;
	
	@FindBy(xpath = "//span[contains(text(),'Save')]")
	@CacheLookup
	WebElement saveBtn;
	
	@FindBy(xpath = "//*[contains(text(),'successfully')]")
	@CacheLookup
	WebElement createTeamsSuccessPopup;
	
	@FindBy(xpath = "//*[contains(text(),'Team already exists')]")
	@CacheLookup
	WebElement createTeamsFailurePopup;
	
	@FindBy(xpath = "//td//div//span//a[not(contains(text(),'CREATE'))]")
	@CacheLookup
	WebElement teamNameinTopRow;
	
	@FindBy(xpath = "//*[contains(@icon,'dots-three')]")
	@CacheLookup
	WebElement teamsPageThreeDots;
	
	@FindBy(xpath = "//*[contains(text(),'Delete Team')]")
	@CacheLookup
	WebElement deleteTeamBtn;
	
	@FindBy(xpath = "//*[contains(@class,'kf-icon-profilecollection')]")
	@CacheLookup
	WebElement pcSection;
	
	@FindBy(xpath = "//*[contains(@class,'profile-collection-header')]")
	@CacheLookup
	WebElement pcPageHeader;
	
	@FindBy(xpath = "//*[contains(text(),'create a Profile Collection')]")
	@CacheLookup
	WebElement pcPageDesc;
	
	@FindBy(xpath = "//*[contains(text(),'CREATE PROFILE')]")
	@CacheLookup
	WebElement createPCBtn;
	
	@FindBy(xpath = "//*[contains(text(),'Step 1 of 3')]")
	@CacheLookup
	WebElement createPCFirstStepHeader;
	
	@FindBy(xpath = "//*[contains(text(),'Step 2 of 3')]")
	@CacheLookup
	WebElement createPCSecondStepHeader;
	
	@FindBy(xpath = "//*[contains(text(),'Step 3 of 3')]")
	@CacheLookup
	WebElement createPCThirdStepHeader;
	
	@FindBy(xpath = "//input[contains(@class,'primary')]")
	@CacheLookup
	WebElement pcNameTextbox;
	
	@FindBy(xpath = "//textarea[contains(@class,'primary')]")
	@CacheLookup
	WebElement pcDescTextbox;
	
	@FindBy(xpath = "//*[contains(text(),' ALL TEAMS (')]")
	@CacheLookup
	WebElement allTeamsHeader;
	
	@FindBy(xpath = "//td[contains(@class,'check-box')]//kf-checkbox")
	@CacheLookup
	WebElement selectTeamTopRowCheckbox;
	
	@FindBy(xpath = "//*[contains(text(),' SELECTED TEAMS (')]")
	@CacheLookup
	WebElement SelectedTeamsHeader;
	
	@FindBy(xpath = "//*[contains(text(),'Page 1')]")
	@CacheLookup
	WebElement teamCount;
	
	@FindBy(xpath = "//*[contains(text(),' ADD ADDITIONAL SUCCESS PROFILES (')]")
	@CacheLookup
	WebElement addAdditionalSPHeader;
	
	@FindBy(xpath = "//*[contains(text(),' SUCCESS PROFILES (')]")
	@CacheLookup
	WebElement SPHeaderinPC;
	
	@FindBy(xpath = "//*[contains(@placeholder,'Search success profiles')]")
	@CacheLookup
	WebElement pcThirdStepSearchbar;
	
	@FindBy(xpath = "//*/kf-page-content/kfconf-profile-collection-addedit/div/div[3]/div/div[4]/kf-checkbox/div")
	@CacheLookup
	WebElement selectAllCheckbox;
	
	@FindBy(xpath = "//*[contains(text(),'Showing ')]")
	@CacheLookup
	WebElement pcCount;
	
	@FindBy(xpath = "//*[contains(text(),'Done')]")
	@CacheLookup
	WebElement doneBtn;
	
	@FindBy(xpath = "//*[contains(text(),'been saved')]")
	@CacheLookup
	WebElement pcSuccessPopup;
	
	@FindBy(xpath = "//*[contains(@placeholder,'Profile Collection')]")
	@CacheLookup
	WebElement pcSearchbar;
	
	@FindBy(xpath = "//*[contains(@icon,'dots-three')]")
	@CacheLookup
	WebElement topRowThreeDots;
	
	@FindBy(xpath = "//div[@class='item-top-row']//*[contains(text(),'Delete')]")
	@CacheLookup
	WebElement topRowDeletebtn;
	
	@FindBy(xpath = "//footer//*[contains(text(),'Delete')]")
	@CacheLookup
	WebElement deleteConfirmbtn;
	
	@FindBy(xpath = "//*[contains(text(),'deleted')]")
	@CacheLookup
	WebElement deletionSuccessPopup;
	
	@FindBy(xpath = "//*[contains(text(), 'permissioning restrictions ')]")
	@CacheLookup
	public WebElement tipMsg2Text;
	
	@FindBy(xpath = "//*[contains(text(), 'permissioning restrictions ')]//..//button")
	@CacheLookup
	public WebElement tipMsg2CloseBtn;
	
	@FindBy(xpath = "//button[@id='global-nav-menu-btn']")
	@CacheLookup
	public WebElement KfoneMenu;
	
	@FindBy(xpath = "//button[@aria-label='User Access']")
	@CacheLookup
	public WebElement UserAccessBtn;
	
	@FindBy(xpath = "//*[@class='blocking-loader']//img")
	@CacheLookup
	WebElement pageLoadSpinner1;
	
	@FindBy(xpath = "//div[@data-testid='loader']//img")
	@CacheLookup
	WebElement pageLoadSpinner2;
	
	//Methods
	public void navigate_to_system_configuration_page_from_kfone_global_menu() {
		try {
			// Wait for page load spinner to disappear
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner1));
			
		LOGGER.info("Clicking KFONE Global Menu...");
		ExtentCucumberAdapter.addTestStepLog("Clicking KFONE Global Menu...");
		
		// Scroll element into view and click on KFONE Global Menu button
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", KfoneMenu);
		try { Thread.sleep(500); } catch (InterruptedException ie) {}
		try {
			wait.until(ExpectedConditions.elementToBeClickable(KfoneMenu)).click();
		} catch (Exception e) {
			try {
				js.executeScript("arguments[0].click();", KfoneMenu);
			} catch (Exception s) {
				utils.jsClick(driver, KfoneMenu);
			}
		}
		
		LOGGER.info("Successfully clicked KFONE Global Menu");
		ExtentCucumberAdapter.addTestStepLog("Successfully clicked KFONE Global Menu");
		
		PerformanceUtils.waitForPageReady(driver, 1);
		
		LOGGER.info("Clicking Job Mapping button in KFONE menu...");
		ExtentCucumberAdapter.addTestStepLog("Clicking Job Mapping button in KFONE menu...");
		
		// Scroll element into view and click on User Access button in the menu
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", UserAccessBtn);
		try { Thread.sleep(500); } catch (InterruptedException ie) {}
		try {
			wait.until(ExpectedConditions.elementToBeClickable(UserAccessBtn)).click();
		} catch (Exception e) {
			try {
				js.executeScript("arguments[0].click();", UserAccessBtn);
			} catch (Exception s) {
				utils.jsClick(driver, UserAccessBtn);
			}
		}
			
			LOGGER.info("Successfully clicked User Access button in KFONE menu");
			ExtentCucumberAdapter.addTestStepLog("Successfully clicked User Access button in KFONE menu");
			
			// Wait for navigation to complete
			wait.until(ExpectedConditions.invisibilityOfAllElements(pageLoadSpinner2));
			
			try {
				wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner));
//				String text = wait.until(ExpectedConditions.visibilityOf(SystemConfigurationHeaderText)).getText();
//				Assert.assertEquals(text, "SYSTEM CONFIGURATION");
				wait.until(ExpectedConditions.visibilityOf(UAMbutton)).isDisplayed();
				LOGGER.info("User landed on the SYSTEM CONFIGURATION page successfully");
				ExtentCucumberAdapter.addTestStepLog("User landed on the SYSTEM CONFIGURATION page successfully");
			} catch (Exception e) {
				LOGGER.error("Issue in landing on System Configuration page - Method: navigate_to_system_configuration_page_from_kfone_global_menu", e);
				e.printStackTrace();
				ExtentCucumberAdapter.addTestStepLog("Issue in landing on System Configuration page....Please Investigate!!!");
				Assert.fail("Issue in landing on System Configuration page....Please Investigate!!!");
			}
			
	} catch (Exception e) {
			ScreenshotHandler.captureFailureScreenshot("navigate_to_system_configuration_page_from_kfone_global_menu", e);
			LOGGER.error("Error navigating to System Configuration page from KFONE Global Menu: " + e.getMessage());
			ExtentCucumberAdapter.addTestStepLog("Error navigating to System Configuration page from KFONE Global Menu: " + e.getMessage());
			throw new RuntimeException("Failed to navigate to System Configuration page from KFONE Global Menu", e);
		}
	}
	
	public void click_on_user_admin_module_button() {
		try {
			// Scroll element into view before clicking
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", UAMbutton);
			try { Thread.sleep(500); } catch (InterruptedException ie) {}
			try {
				wait.until(ExpectedConditions.elementToBeClickable(UAMbutton)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", UAMbutton);
				} catch (Exception s) {
					utils.jsClick(driver, UAMbutton);
				}
			}
			LOGGER.info("Clicked on User Admin Module button....");
			ExtentCucumberAdapter.addTestStepLog("Clicked on User Admin Module button....");
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner));
		} catch (Exception e) {
			LOGGER.error("❌ Issue in clicking on User Admin Module button - Method: click_user_admin_module_button", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("❌ Issue in clicking on User Admin Module button...Please Investigate!!!");
			Assert.fail("Issue in clicking on User Admin Module button...Please Investigate!!!");
		}
	}
	
	public void user_should_be_landed_on_clients_dashboard_page() {
		try {
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner));
			String text = wait.until(ExpectedConditions.visibilityOf(clientDashboard)).getText();
			Assert.assertEquals(text, "DASHBOARD");
			String clientname = wait.until(ExpectedConditions.visibilityOf(clientNameText)).getText();
			LOGGER.info("User successfully landed on the Dashboard page of the client : " + clientname);
			ExtentCucumberAdapter.addTestStepLog("User successfully landed on the Dashboard page of the client : " + clientname);
		} catch (Exception e) {
			LOGGER.error("Issue in landing on Clients Dashboard page - Method: verify_clients_dashboard_page", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in landing on Clients Dashboard page....Please Investigate!!!");
			Assert.fail("Issue in landing on Clients Dashboard page....Please Investigate!!!");
		}
	}
	
	public void user_is_on_clients_dashboard_page() {
		String clientname = wait.until(ExpectedConditions.visibilityOf(clientNameText)).getText();
		LOGGER.info("User is on the Dashboard page of the client : " + clientname);
		ExtentCucumberAdapter.addTestStepLog("User is on the Dashboard page of the client : " + clientname);
	}
	
	public void click_on_teams_section() {
		try {
			// Scroll element into view before clicking
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", teamsSection);
			try { Thread.sleep(500); } catch (InterruptedException ie) {}
			try {
				wait.until(ExpectedConditions.elementToBeClickable(teamsSection)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", teamsSection);
				} catch (Exception s) {
					utils.jsClick(driver, teamsSection);
				}
			}
			LOGGER.info("Clicked on Teams section in User Admin Module....");
			ExtentCucumberAdapter.addTestStepLog("Clicked on Teams section in User Admin Module....");
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner));
		} catch (Exception e) {
			LOGGER.error("❌ Issue in clicking on Teams section in User Admin Module - Method: click_teams_section_user_admin", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("❌ Issue in clicking on Teams section in User Admin Module...Please Investigate!!!");
			Assert.fail("Issue in clicking on Teams section in User Admin Module...Please Investigate!!!");
		}
	}
	
	public void verify_user_landed_on_teams_page() {
		try {
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner));
			String text = wait.until(ExpectedConditions.visibilityOf(teamsPageHeader)).getText();
			Assert.assertEquals(text, "TEAMS");
			LOGGER.info("User landed on the " + text + " page successfully");
			ExtentCucumberAdapter.addTestStepLog("User landed on the " + text + " page successfully");
			String text1 = wait.until(ExpectedConditions.visibilityOf(teamsPageDesc)).getText();
			LOGGER.info(text1);
			ExtentCucumberAdapter.addTestStepLog(text1);
		} catch (Exception e) {
			LOGGER.error("❌ Issue in landing on Teams page - Method: verify_teams_page_landing", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("❌ Issue in landing on Teams page....Please Investigate!!!");
			Assert.fail("Issue in landing on Teams page....Please Investigate!!!");
		}
	}
	
	public void click_on_create_teams_button() {
		try {
			// Scroll element into view before clicking
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", teamsPageCreateTeamsBtn);
			try { Thread.sleep(500); } catch (InterruptedException ie) {}
			try {
				wait.until(ExpectedConditions.elementToBeClickable(teamsPageCreateTeamsBtn)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", teamsPageCreateTeamsBtn);
				} catch (Exception s) {
					utils.jsClick(driver, teamsPageCreateTeamsBtn);
				}
			}
			LOGGER.info("Clicked on " + "CREATE TEAMS" +" button in Teams section....");
			ExtentCucumberAdapter.addTestStepLog("Clicked on " + "CREATE TEAMS" +" button in Teams section....");
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner));
		} catch (Exception e) {
			LOGGER.error("❌ Issue in clicking on Create Teams button - Method: click_create_teams_button", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("❌ Issue in clicking on Create Teams button in Teams section...Please Investigate!!!");
			Assert.fail("Issue in clicking on Create Teams button in Teams section...Please Investigate!!!");
		}
	}
	
	public void user_should_be_navigated_to_first_step_of_creating_a_team() {
		try {
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner));
			String text = wait.until(ExpectedConditions.visibilityOf(createTeamsFirstStepHeader)).getText();
			Assert.assertEquals(text, "Step 1 of 2: Name and describe your team and select a Profile Collection");
			LOGGER.info("User successfully navigated to first step of creating a team");
			ExtentCucumberAdapter.addTestStepLog("User successfully navigated to first step of creating a team");
			LOGGER.info(text);
			ExtentCucumberAdapter.addTestStepLog(text);
		} catch (Exception e) {
			LOGGER.error("❌ Issue in navigating to first step of creating a team - Method: navigate_first_step_creating_team", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("❌ Issue in navigating to first step of creating a team....Please Investigate!!!");
			Assert.fail("Issue in navigating to first step of creating a team....Please Investigate!!!");
		}
	}
	
	public void user_should_enter_team_name_and_team_description() {
		try {
			int currentYear = currentdate.getYear();
			int currentDay = currentdate.getDayOfMonth();
			String cd;
			String todayDate;
			if(Integer.toString(currentDay).length() == 1) {
				DecimalFormat df = new DecimalFormat("00"); 
				cd = df.format(currentDay);
				todayDate = currentMonth.toString().substring(0,1) + currentMonth.toString().substring(1,3).toLowerCase() + " " + cd + ", " + Integer.toString(currentYear);
			} else {
				todayDate = currentMonth.toString().substring(0,1) + currentMonth.toString().substring(1,3).toLowerCase() + " " + Integer.toString(currentDay) + ", " + Integer.toString(currentYear);
			}
			teamName = "Team_" + todayDate;
			wait.until(ExpectedConditions.visibilityOf(teamNameTextbox)).sendKeys(teamName);
			LOGGER.info("Entered Team Name : " + teamName);
			ExtentCucumberAdapter.addTestStepLog("Entered Team Name : " + teamName);
			js.executeScript("arguments[0].scrollIntoView(true);", teamDescTextbox);
			wait.until(ExpectedConditions.visibilityOf(teamDescTextbox)).sendKeys("Creating a Team through Automation on " + todayDate);
			LOGGER.info("Entered Team Description : " + "Creating a Team through Automation on " + todayDate);
			ExtentCucumberAdapter.addTestStepLog("Entered Team Description : " + "Creating a Team through Automation on " + todayDate);
		} catch (Exception e) {
			LOGGER.error("❌ Issue in entering Team name and description - Method: enter_team_name_description", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("❌ Issue in entering Team name and Team Description in the first step of creating a team....Please Investigate!!!");
			Assert.fail("Issue in entering Team name and Team Description in the first step of creating a team....Please Investigate!!!");
		}
	}
	
	public void click_on_next_button_in_create_team_page() {
		try {
			// Scroll element into view before clicking
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", nextBtn);
			try { Thread.sleep(500); } catch (InterruptedException ie) {}
			try {
				wait.until(ExpectedConditions.elementToBeClickable(nextBtn)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", nextBtn);
				} catch (Exception s) {
					utils.jsClick(driver, nextBtn);
				}
			}
			LOGGER.info("Clicked on Next button in Create Teams page....");
			ExtentCucumberAdapter.addTestStepLog("Clicked on Next button in Create Teams page....");
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner));
		} catch (Exception e) {
			LOGGER.error("❌ Issue in clicking on Next button in Create Teams page - Method: click_next_button_create_teams", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("❌ Issue in clicking on Next button in Create Teams page...Please Investigate!!!");
			Assert.fail("Issue in clicking on Next button in Create Teams page...Please Investigate!!!");
		}
	}
	
	public void user_should_be_navigated_to_second_step_of_creating_a_team() {
		try {
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner));
			String text = wait.until(ExpectedConditions.visibilityOf(createTeamsSecondStepHeader)).getText();
			Assert.assertEquals(text, "Step 2 of 2: Select Team Members");
			LOGGER.info("User successfully navigated to Second step of creating a team");
			ExtentCucumberAdapter.addTestStepLog("User successfully navigated to Second step of creating a team");
			LOGGER.info(text);
			ExtentCucumberAdapter.addTestStepLog(text);
		} catch (Exception e) {
			LOGGER.error("❌ Issue in navigating to second step of creating a team - Method: navigate_second_step_creating_team", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("❌ Issue in navigating to Second step of creating a team....Please Investigate!!!");
			Assert.fail("Issue in navigating to Second step of creating a team....Please Investigate!!!");
		}
	}
	
	public void search_user_to_add_as_team_member() {
		try {
			wait.until(ExpectedConditions.visibilityOf(createTeamsSecondStepSearchbar)).sendKeys(PO01_KFoneLogin.username);
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner));
			LOGGER.info("Entered User name : " + PO01_KFoneLogin.username + " in the search bar to add as Team Member");
			ExtentCucumberAdapter.addTestStepLog("Entered User name : " + PO01_KFoneLogin.username + " in the search bar to add as Team Member");
		} catch (Exception e) {
			LOGGER.error("❌ Issue in searching a user to add as Team Member - Method: search_user_add_team_member", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("❌ Issue in Searching a user to add as Team Member....Please Investigate!!!");
			Assert.fail("Issue in Searching a user to add as Team Member....Please Investigate!!!");
		}
	}
	
	public void select_searched_user_to_add_as_team_member() {
		try {
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner));
		js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
		wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner));
		// PERFORMANCE: Replaced Thread.sleep(2000) with smart page ready wait
		PerformanceUtils.waitForPageReady(driver, 2);
		// Scroll element into view before clicking
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", createTeamsTopRowCheckbox);
		try { Thread.sleep(500); } catch (InterruptedException ie) {}
		try {
				wait.until(ExpectedConditions.elementToBeClickable(createTeamsTopRowCheckbox)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", createTeamsTopRowCheckbox);
				} catch (Exception s) {
					utils.jsClick(driver, createTeamsTopRowCheckbox);
				}
			}
			LOGGER.info("User " + PO01_KFoneLogin.username + " is Selected to add as Team Member");
			ExtentCucumberAdapter.addTestStepLog("User " + PO01_KFoneLogin.username + " is Selected to add as Team Member");
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner));
		} catch (Exception e) {
			LOGGER.error("❌ Issue in selecting user to add as Team Member - Method: select_user_add_team_member", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("❌ Issue in selecting user to add as Team Member...Please Investigate!!!");
			Assert.fail("Issue in selecting user to add as Team Member...Please Investigate!!!");
		}
	}
	
	public void click_on_team_members_header_and_verify_user_is_added_successfully_as_team_member() {
		try {
			// Scroll element into view before clicking
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", teamMembersHeader);
			try { Thread.sleep(500); } catch (InterruptedException ie) {}
			try {
				wait.until(ExpectedConditions.elementToBeClickable(teamMembersHeader)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", teamMembersHeader);
				} catch (Exception s) {
					utils.jsClick(driver, teamMembersHeader);
				}
			}
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner));
			LOGGER.info("Clicked on Team Members header in Create Teams page....");
			ExtentCucumberAdapter.addTestStepLog("Clicked on Team Members header in Create Teams page....");
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner));
		} catch (Exception e) {
			LOGGER.error("❌ Issue in clicking on Team Members header - Method: click_team_members_header", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("❌ Issue in clicking on Team Members header...Please Investigate!!!");
			Assert.fail("Issue in clicking on Team Members header...Please Investigate!!!");
		}
		
		try {
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner));
			wait.until(ExpectedConditions.visibilityOf(userCount)).isDisplayed();
			String text = wait.until(ExpectedConditions.visibilityOf(userCount)).getText();
			Assert.assertEquals(text,"Showing 1 of 1 Users");
			LOGGER.info("User " + PO01_KFoneLogin.username + " successfully added as Team Member");
			ExtentCucumberAdapter.addTestStepLog("User " + PO01_KFoneLogin.username + " successfully added as Team Member");
		} catch (Exception e) {
			LOGGER.error("❌ Issue in verifying user added as Team Member - Method: verify_user_added_team_member", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("❌ Issue in Verifying User added as Team Member....Please Investigate!!!");
			Assert.fail("Issue in Verifying User added as Team Member....Please Investigate!!!");
		}
	}
	
	public void click_on_save_button_on_team_page_and_verify_success_popup_appears() {
		try {
			// Scroll element into view before clicking
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(saveBtn));
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
			try { Thread.sleep(500); } catch (InterruptedException ie) {}
			try {
				element.click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", saveBtn);
				} catch (Exception s) {
					utils.jsClick(driver, saveBtn);
				}
			}
			LOGGER.info("Clicked on Save button in Create Teams page....");
			ExtentCucumberAdapter.addTestStepLog("Clicked on Save button in Create Teams page....");
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner));
		} catch (Exception e) {
			LOGGER.error("❌ Issue in clicking on Save button in Create Teams page - Method: click_save_button_create_teams", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("❌ Issue in clicking on Save button in Create Teams page...Please Investigate!!!");
			Assert.fail("Issue in clicking on Save button in Create Teams page...Please Investigate!!!");
		}
		
		try {
//			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner));
			SmartWaits.shortWait(driver);
			try {
				// PERFORMANCE: Replaced Thread.sleep(1000) with smart element wait
				PerformanceUtils.waitForElement(driver, createTeamsSuccessPopup);
				createTeamsSuccessPopup.isDisplayed();
				String SuccesstText = wait.until(ExpectedConditions.visibilityOf(createTeamsSuccessPopup)).getText();
				LOGGER.info(SuccesstText);
				ExtentCucumberAdapter.addTestStepLog(SuccesstText);
			} catch (Exception e) {
				createTeamsFailurePopup.isDisplayed();
				String FailureText = wait.until(ExpectedConditions.visibilityOf(createTeamsFailurePopup)).getText();
				LOGGER.info(FailureText);
				ExtentCucumberAdapter.addTestStepLog(FailureText);
			}
		} catch (Exception e) {
			LOGGER.error("❌ Issue in verifying popup after creating team - Method: verify_popup_after_creating_team", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("❌ Issue in verifying appearing of popup after creating a team....Please Investigate!!!");
			Assert.fail("Issue in verifying appearing of popup after creating a team....Please Investigate!!!");
		}
	}
	
	public void search_for_team_name_in_teams_page_and_verify_team_is_created_successfully() {
		try {
	wait.until(ExpectedConditions.visibilityOf(teamsPageSearchbar)).sendKeys(teamName);
	wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner));
	// PERFORMANCE: Replaced Thread.sleep(2000) with smart element wait
	PerformanceUtils.waitForElement(driver, teamNameinTopRow, 2);
	String text = wait.until(ExpectedConditions.visibilityOf(teamNameinTopRow)).getText();
			Assert.assertEquals(text,teamName);
			LOGGER.info("Team with name : " + teamName + " is successfully created");
			ExtentCucumberAdapter.addTestStepLog("Team with name : " + teamName + " is successfully created");
		} catch (Exception e) {
			LOGGER.error("❌ Issue in searching and verifying team created - Method: search_verify_team_created", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("❌ Issue in Searching and Verifying a Team is created successfully....Please Investigate!!!");
			Assert.fail("Issue in Searching and Verifying a Team is created successfully....Please Investigate!!!");
		}
	}
	
	public void click_on_profile_collections_section() {
		try {
			// Scroll element into view before clicking
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(pcSection));
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
			try { Thread.sleep(500); } catch (InterruptedException ie) {}
			try {
				element.click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", pcSection);
				} catch (Exception s) {
					utils.jsClick(driver, pcSection);
				}
			}
			LOGGER.info("Clicked on Profile Collections section in User Admin Module....");
			ExtentCucumberAdapter.addTestStepLog("Clicked on Profile Collections section in User Admin Module....");
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner));
		} catch (Exception e) {
			LOGGER.error("❌ Issue in clicking on Profile Collections section - Method: click_profile_collections_section", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("❌ Issue in clicking on Profile Collections section in User Admin Module...Please Investigate!!!");
			Assert.fail("Issue in clicking on Profile Collections section in User Admin Module...Please Investigate!!!");
		}
	}
	
	public void verify_user_navigated_to_manage_success_profiles_page() {
		try {
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner));
			String text = wait.until(ExpectedConditions.visibilityOf(pcPageHeader)).getText();
			Assert.assertEquals(text, "MANAGE SUCCESS PROFILES");
			LOGGER.info("User landed on the " + text + " page successfully");
			ExtentCucumberAdapter.addTestStepLog("User landed on the " + text + " page successfully");
			String text1 = wait.until(ExpectedConditions.visibilityOf(pcPageDesc)).getText();
			LOGGER.info(text1);
			ExtentCucumberAdapter.addTestStepLog(text1);
		} catch (Exception e) {
			LOGGER.error("❌ Issue in landing on Manage Success Profiles page - Method: verify_manage_success_profiles_page", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("❌ Issue in landing on Manage Success Profiles page....Please Investigate!!!");
			Assert.fail("Issue in landing on Manage Success Profiles page....Please Investigate!!!");
		}
	}
	
	public void click_on_create_profile_collection_button() {
		try {
			// Scroll element into view before clicking
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(createPCBtn));
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
			try { Thread.sleep(500); } catch (InterruptedException ie) {}
			try {
				element.click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", createPCBtn);
				} catch (Exception s) {
					utils.jsClick(driver, createPCBtn);
				}
			}
			LOGGER.info("Clicked on " + "CREATE PRFOILE COLLECTION" +" button in Teams section....");
			ExtentCucumberAdapter.addTestStepLog("Clicked on " + "CREATE PRFOILE COLLECTION" +" button in Teams section....");
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner));
		} catch (Exception e) {
			LOGGER.error("❌ Issue in clicking on CREATE PROFILE COLLECTION button - Method: click_create_profile_collection_button", e);
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("❌ Issue in clicking on CREATE PROFILE COLLECTION button in Teams section...Please Investigate!!!");
			Assert.fail("Issue in clicking on CREATE PROFILE COLLECTION button in Teams section...Please Investigate!!!");
		}
	}
	
	public void user_should_navigated_to_first_step_of_creating_a_profile_collection() {
		try {
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner));
			String text = wait.until(ExpectedConditions.visibilityOf(createPCFirstStepHeader)).getText();
			Assert.assertEquals(text, "Step 1 of 3: Name and describe your Profile Collection");
			LOGGER.info("User successfully navigated to first step of creating a team");
			ExtentCucumberAdapter.addTestStepLog("User successfully navigated to first step of creating a team");
			LOGGER.info(text);
			ExtentCucumberAdapter.addTestStepLog(text);
		} catch (Exception e) {
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in navigating to first step of creating a Profile Collection....Please Investigate!!!");
			Assert.fail("Issue in navigating to first step of creating a Profile Collection....Please Investigate!!!");
		}
	}
	
	public void user_should_enter_profile_collection_name_and_profile_collection_description() {
		try {
			int currentYear = currentdate.getYear();
			int currentDay = currentdate.getDayOfMonth();
			String cd;
			String todayDate;
			if(Integer.toString(currentDay).length() == 1) {
				DecimalFormat df = new DecimalFormat("00"); 
				cd = df.format(currentDay);
				todayDate = currentMonth.toString().substring(0,1) + currentMonth.toString().substring(1,3).toLowerCase() + " " + cd + ", " + Integer.toString(currentYear);
			} else {
				todayDate = currentMonth.toString().substring(0,1) + currentMonth.toString().substring(1,3).toLowerCase() + " " + Integer.toString(currentDay) + ", " + Integer.toString(currentYear);
			}
			pcName = "PC_" + todayDate;
			// Scroll element into view before clicking
			WebElement nameElement = wait.until(ExpectedConditions.elementToBeClickable(pcNameTextbox));
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", nameElement);
			try { Thread.sleep(500); } catch (InterruptedException ie) {}
			try {
				nameElement.click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", pcNameTextbox);
				} catch (Exception s) {
					utils.jsClick(driver, pcNameTextbox);
				}
			}
			wait.until(ExpectedConditions.visibilityOf(pcNameTextbox)).sendKeys(pcName);
			LOGGER.info("Entered Profile Collection Name : " + pcName);
			ExtentCucumberAdapter.addTestStepLog("Entered Profile Collection Name : " + pcName);
			// Scroll element into view before clicking
			WebElement descElement = wait.until(ExpectedConditions.elementToBeClickable(pcDescTextbox));
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", descElement);
			try { Thread.sleep(500); } catch (InterruptedException ie) {}
			try {
				descElement.click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", pcDescTextbox);
				} catch (Exception s) {
					utils.jsClick(driver, pcDescTextbox);
				}
			}
			wait.until(ExpectedConditions.visibilityOf(pcDescTextbox)).sendKeys("Creating a Profile Collection through Automation on " + todayDate);
			LOGGER.info("Entered Profile Collection Description : " + "Creating a Profile Collection through Automation on " + todayDate);
			ExtentCucumberAdapter.addTestStepLog("Entered Profile Collection Description : " + "Creating a Team through Automation on " + todayDate);
		} catch (Exception e) {
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in entering Profile Collection name and Profile Collection Description in the first step of creating a Profile Collection....Please Investigate!!!");
			Assert.fail("Issue in entering Profile Collection name and Profile Collection Description in the first step of creating a Profile Collection....Please Investigate!!!");
		}
	}
	
	public void click_on_next_button_in_create_profile_collection_page() {
		try {
			// Scroll element into view before clicking
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(nextBtn));
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
			try { Thread.sleep(500); } catch (InterruptedException ie) {}
			try {
				element.click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", nextBtn);
				} catch (Exception s) {
					utils.jsClick(driver, nextBtn);
				}
			}
			LOGGER.info("Clicked on Next button in Create Profile Collection page....");
			ExtentCucumberAdapter.addTestStepLog("Clicked on Next button in Create Profile Collection page....");
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner));
		} catch (Exception e) {
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in clicking on Next button in Create Profile Collection page...Please Investigate!!!");
			Assert.fail("Issue in clicking on Next button in Create Profile Collection page...Please Investigate!!!");
		}
	}
	
	public void user_should_be_navigated_to_second_step_of_creating_a_profile_collection() {
		try {
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner));
			String text = wait.until(ExpectedConditions.visibilityOf(createPCSecondStepHeader)).getText();
			Assert.assertEquals(text, "Step 2 of 3: Select Team(s) for your Profile Collection");
			LOGGER.info("User successfully navigated to Second step of creating a Profile Collection");
			ExtentCucumberAdapter.addTestStepLog("User successfully navigated to Second step of creating a Profile Collection");
			LOGGER.info(text);
			ExtentCucumberAdapter.addTestStepLog(text);
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner));
		} catch (Exception e) {
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in navigating to Second step of creating a Profile Collection....Please Investigate!!!");
			Assert.fail("Issue in navigating to Second step of creating a Profile Collection....Please Investigate!!!");
		}
	}
	
	public void click_on_all_teams_header() {
		try {
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner));
			// PERFORMANCE: Replaced Thread.sleep(2000) with smart page ready wait
			PerformanceUtils.waitForPageReady(driver);
			// Scroll element into view before clicking
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(allTeamsHeader));
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
			try { Thread.sleep(500); } catch (InterruptedException ie) {}
			try {
				element.click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", allTeamsHeader);
				} catch (Exception s) {
					utils.jsClick(driver, allTeamsHeader);
				}
			}
			LOGGER.info("Clicked on All Teams button in Create Profile Collection page....");
			ExtentCucumberAdapter.addTestStepLog("Clicked on All Teams button in Create Profile Collection page....");
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner));
		} catch (Exception e) {
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in clicking on All Teams header button in Create Profile Collection page...Please Investigate!!!");
			Assert.fail("Issue in clicking on All Teams header button in Create Profile Collection page...Please Investigate!!!");
		}
	}
	
	public void select_recently_created_team_name() {
		try {
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner));
		js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
		wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner));
		// PERFORMANCE: Replaced Thread.sleep(2000) with smart page ready wait
		PerformanceUtils.waitForPageReady(driver, 2);
		// Scroll element into view before clicking
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(selectTeamTopRowCheckbox));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
		try { Thread.sleep(500); } catch (InterruptedException ie) {}
		try {
				element.click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", selectTeamTopRowCheckbox);
				} catch (Exception s) {
					utils.jsClick(driver, selectTeamTopRowCheckbox);
				}
			}
			LOGGER.info("Recently Created Team with name " + teamName + " is Selected for Profile Collection");
			ExtentCucumberAdapter.addTestStepLog("Recently Created Team with name " + teamName + " is Selected for Profile Collection");
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner));
		} catch (Exception e) {
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in selecting Recently Created Team for Profile Collection...Please Investigate!!!");
			Assert.fail("Issue in selecting Recently Created Team for Profile Collection...Please Investigate!!!");
		}
	}
	
public void click_on_selected_teams_header() {
	try {
		PerformanceUtils.waitForPageReady(driver, 2);
		// Scroll element into view before clicking
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(SelectedTeamsHeader));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
		try { Thread.sleep(500); } catch (InterruptedException ie) {}
		try {
			element.click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", SelectedTeamsHeader);
				} catch (Exception s) {
					utils.jsClick(driver, SelectedTeamsHeader);
				}
			}
			LOGGER.info("Clicked on Selected Teams button in Create Profile Collection page....");
			ExtentCucumberAdapter.addTestStepLog("Clicked on Selected Teams button in Create Profile Collection page....");
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner));
		} catch (Exception e) {
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in clicking on Selected Teams header button in Create Profile Collection page...Please Investigate!!!");
			Assert.fail("Issue in clicking on Selected Teams header button in Create Profile Collection page...Please Investigate!!!");
		}
	}
	
	public void user_should_verify_recently_created_team_name_is_available_in_selected_teams() {
		try {
		wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner));
		PerformanceUtils.waitForPageReady(driver, 2);
		wait.until(ExpectedConditions.visibilityOf(teamCount)).isDisplayed();
			String text = wait.until(ExpectedConditions.visibilityOf(teamCount)).getText();
			Assert.assertEquals(text,"Page 1 of 1");
			LOGGER.info("Recently created team " + teamName + " is available in Selected Teams as Expected");
			ExtentCucumberAdapter.addTestStepLog("Recently created team " + teamName + " is available in Selected Teams as Expected");
		} catch (Exception e) {
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifing Recently created team name is available in selected teams....Please Investigate!!!");
			Assert.fail("Issue in Verifing Recently created team name is available in selected teams....Please Investigate!!!");
		}
	}
	
	public void user_should_be_navigated_to_third_step_of_creating_a_profile_collection() {
		try {
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner));
			String text = wait.until(ExpectedConditions.visibilityOf(createPCThirdStepHeader)).getText();
			Assert.assertEquals(text, "Step 3 of 3: Select Success Profiles for your Profile Collection");
			LOGGER.info("User successfully navigated to Third step of creating a Profile Collection");
			ExtentCucumberAdapter.addTestStepLog("User successfully navigated to Third step of creating a Profile Collection");
			LOGGER.info(text);
			ExtentCucumberAdapter.addTestStepLog(text);
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner));
		} catch (Exception e) {
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in navigating to Third step of creating a Profile Collection....Please Investigate!!!");
			Assert.fail("Issue in navigating to Third step of creating a Profile Collection....Please Investigate!!!");
		}
	}
	
public void click_on_add_additional_success_profiles_header() {
	try {
		PerformanceUtils.waitForPageReady(driver, 2);
		// Scroll element into view before clicking
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(addAdditionalSPHeader));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
		try { Thread.sleep(500); } catch (InterruptedException ie) {}
		try {
			element.click();
		} catch (Exception e) {
			try {
				js.executeScript("arguments[0].click();", addAdditionalSPHeader);
			} catch (Exception s) {
				utils.jsClick(driver, addAdditionalSPHeader);
			}
		}
		LOGGER.info("Clicked on ADD ADDITIONAL SUCCESS PROFILES button in Create Profile Collection page....");
		ExtentCucumberAdapter.addTestStepLog("Clicked on ADD ADDITIONAL SUCCESS PROFILES button in Create Profile Collection page....");
		wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner));
		PerformanceUtils.waitForPageReady(driver, 2);
		} catch (Exception e) {
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in clicking on ADD ADDITIONAL SUCCESS PROFILES header button in Create Profile Collection page...Please Investigate!!!");
			Assert.fail("Issue in clicking on ADD ADDITIONAL SUCCESS PROFILES header button in Create Profile Collection page...Please Investigate!!!");
		}
	}
	
	public void search_and_add_success_profiles_to_profile_collection() {
		try {
			wait.until(ExpectedConditions.visibilityOf(pcThirdStepSearchbar)).sendKeys(spNameString);
		wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner));
		PerformanceUtils.waitForPageReady(driver, 2);
		// Scroll element into view before clicking
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(selectAllCheckbox));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
		try { Thread.sleep(500); } catch (InterruptedException ie) {}
		try {
				element.click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", selectAllCheckbox);
				} catch (Exception s) {
					utils.jsClick(driver, selectAllCheckbox);
				}
			}
			LOGGER.info("Success Profiles which contains " + spNameString + " as Sub-string are selected to add to Profile Collection");
			ExtentCucumberAdapter.addTestStepLog("Success Profiles which contains " + spNameString + " as Sub-string are selected to add to Profile Collection");
		} catch (Exception e) {
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in Searching and adding Success Profiles to Profile Collection....Please Investigate!!!");
			Assert.fail("Issue in Searching and adding Success Profiles to Profile Collection....Please Investigate!!!");
		}
	}
	
public void click_on_success_profiles_header() {
	try {
		PerformanceUtils.waitForPageReady(driver, 2);
		// Scroll element into view before clicking
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(SPHeaderinPC));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
		try { Thread.sleep(500); } catch (InterruptedException ie) {}
		try {
			element.click();
		} catch (Exception e) {
			try {
				js.executeScript("arguments[0].click();", SPHeaderinPC);
			} catch (Exception s) {
				utils.jsClick(driver, SPHeaderinPC);
			}
		}
		LOGGER.info("Clicked on SUCCESS PROFILES header button after selecting Success Profiles to Profile Collection....");
		ExtentCucumberAdapter.addTestStepLog("Clicked on SUCCESS PROFILES header button after selecting Success Profiles to Profile Collection....");
		wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner));
		PerformanceUtils.waitForPageReady(driver, 2);
		} catch (Exception e) {
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in clicking on SUCCESS PROFILES header button after selecting Success Profiles to Profile Collection...Please Investigate!!!");
			Assert.fail("Issue in clicking on SUCCESS PROFILES header button after selecting Success Profiles to Profile Collection...Please Investigate!!!");
		}
	}
	
	public void user_should_verify_added_profiles_are_available_in_success_profiles_header() {
		try {
		wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner));
		PerformanceUtils.waitForPageReady(driver, 2);
		wait.until(ExpectedConditions.visibilityOf(pcCount)).isDisplayed();
			String resultsCountText = wait.until(ExpectedConditions.visibilityOf(pcCount)).getText();
			String[] resultsCountText_split = resultsCountText.split(" ");
			LOGGER.info(resultsCountText_split[3] + " Success Profiles " + "which contains " + spNameString + " as Sub-string are added to Profile Collection");
			ExtentCucumberAdapter.addTestStepLog(resultsCountText_split[3] + "Success Profiles " + "which contains " + spNameString + " as Sub-string are added to Profile Collection");
		} catch (Exception e) {
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in Verifing added profiles availablity in success profiles for Profile Collection....Please Investigate!!!");
			Assert.fail("Issue in Verifing added profiles availablity in success profiles for Profile Collection....Please Investigate!!!");
		}
	}
	
	public void click_on_done_button_on_create_profile_collection_page_and_verify_success_popup_appears() {
		try {
			// Scroll element into view before clicking
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(doneBtn));
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
			try { Thread.sleep(500); } catch (InterruptedException ie) {}
			try {
				element.click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", doneBtn);
				} catch (Exception s) {
					utils.jsClick(driver, doneBtn);
				}
			}
			LOGGER.info("Clicked on Done button in Create Profile Collections page....");
			ExtentCucumberAdapter.addTestStepLog("Clicked on Done button in Create Profile Collections page....");
//			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner));
		} catch (Exception e) {
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in clicking on Done button in Create Profile Collections page.......Please Investigate!!!");
			Assert.fail("Issue in clicking on Done button in Create Profile Collections page.......Please Investigate!!!");
		}
		
		try {
			SmartWaits.shortWait(driver);
			wait.until(ExpectedConditions.visibilityOf(pcSuccessPopup)).isDisplayed();
			String SuccesstText = wait.until(ExpectedConditions.visibilityOf(pcSuccessPopup)).getText();
			LOGGER.info(SuccesstText);
			ExtentCucumberAdapter.addTestStepLog(SuccesstText);
		} catch (Exception e) {
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying appearing of success popup after creating a Profile Collection....Please Investigate!!!");
			Assert.fail("Issue in verifying appearing of success popup after creating a Profile Collection....Please Investigate!!!");
		}
		LOGGER.info("Profile Collection with name " + pcName + " is created successfully");
		ExtentCucumberAdapter.addTestStepLog("Profile Collection with name " + pcName + " is created successfully");
	}
	
	public void search_and_delete_profile_collection() {
		try {
		wait.until(ExpectedConditions.visibilityOf(pcSearchbar)).sendKeys(pcName);
		PerformanceUtils.waitForUIStability(driver, 1);			
		wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner));
		wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner));
		PerformanceUtils.waitForPageReady(driver, 3);
		wait.until(ExpectedConditions.elementToBeClickable(topRowThreeDots)).click();
		PerformanceUtils.waitForUIStability(driver, 2);
		wait.until(ExpectedConditions.elementToBeClickable(topRowDeletebtn)).click();
		PerformanceUtils.waitForUIStability(driver, 2);
		wait.until(ExpectedConditions.elementToBeClickable(deleteConfirmbtn)).click();
		wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner));
		PerformanceUtils.waitForPageReady(driver, 3);
		String text = wait.until(ExpectedConditions.visibilityOf(deletionSuccessPopup)).getText();
			LOGGER.info(text);
			ExtentCucumberAdapter.addTestStepLog(text);
		} catch (Exception e) {
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in Searching and Deleting Profile Collection....Please Investigate!!!");
			Assert.fail("Issue in Searching and Deleting Profile Collection....Please Investigate!!!");
		}
	}
	
	public void search_for_team_name_in_teams_page_and_delete_team() {
		try {
			wait.until(ExpectedConditions.visibilityOf(teamsPageSearchbar)).sendKeys(teamName);
		wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner));
		PerformanceUtils.waitForPageReady(driver, 3);
		wait.until(ExpectedConditions.elementToBeClickable(topRowThreeDots)).click();
		PerformanceUtils.waitForUIStability(driver, 2);
		wait.until(ExpectedConditions.elementToBeClickable(topRowDeletebtn)).click();
		PerformanceUtils.waitForUIStability(driver, 2);
		wait.until(ExpectedConditions.elementToBeClickable(deleteConfirmbtn)).click();
		wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner));
		PerformanceUtils.waitForPageReady(driver, 3);
		String text = wait.until(ExpectedConditions.visibilityOf(deletionSuccessPopup)).getText();
			LOGGER.info(text);
			ExtentCucumberAdapter.addTestStepLog(text);
		} catch (Exception e) {
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in Searching and Deleting Team....Please Investigate!!!");
			Assert.fail("Issue in Searching and Deleting Team....Please Investigate!!!");
		}
	}
	
	public void verify_pc_restricted_tip_message_is_displaying_on_job_mapping_page() {
		try {
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(tipMsg2Text)).isDisplayed());
			String TipMessage2 = tipMsg2Text.getText();
			ExtentCucumberAdapter.addTestStepLog(TipMessage2);
			LOGGER.info(TipMessage2);
			wait.until(ExpectedConditions.visibilityOf(tipMsg2CloseBtn)).click();
			LOGGER.info("PC Restricted Tip Message verified and closed successfully...");
			ExtentCucumberAdapter.addTestStepLog("PC Restricted Tip Message verified and closed successfully...");
		} catch (Exception e) {
			e.printStackTrace();
			ExtentCucumberAdapter.addTestStepLog("Issue in verifying PC Restricted Tip Message on Job Mapping page...Please Investigate!!!");
			Assert.fail("Issue in verifying PC Restricted Tip Message on Job Mapping page...Please Investigate!!!");
		}
	}
	
	public void user_is_on_teams_page() {
		PerformanceUtils.waitForPageReady(driver, 1);
		LOGGER.info("User is on TEAMS page");
		ExtentCucumberAdapter.addTestStepLog("User is on TEAMS page");
	}
	
	public void user_is_on_second_step_of_creating_a_team() {
		PerformanceUtils.waitForPageReady(driver, 1);
		LOGGER.info("User is on second step of creating a team");
		ExtentCucumberAdapter.addTestStepLog("User is on second step of creating a team");
	}
	
	public void user_is_on_team_creation_page_with_team_members_added() {
		PerformanceUtils.waitForPageReady(driver, 1);
		LOGGER.info("User is on Team creation page with team members added");
		ExtentCucumberAdapter.addTestStepLog("User is on Team creation page with team members added");
	}
	
	public void user_is_on_manage_success_profiles_page() {
		PerformanceUtils.waitForPageReady(driver, 1);
		LOGGER.info("User is on Manage Success Profiles page");
		ExtentCucumberAdapter.addTestStepLog("User is on Manage Success Profiles page");
	}
	
	public void user_is_on_second_step_of_creating_a_profile_collection() {
		PerformanceUtils.waitForPageReady(driver, 1);
		LOGGER.info("User is on second step of creating a Profile Collection");
		ExtentCucumberAdapter.addTestStepLog("User is on second step of creating a Profile Collection");
	}
	
	public void user_is_on_third_step_of_creating_a_profile_collection() {
		PerformanceUtils.waitForPageReady(driver, 1);
		LOGGER.info("User is on third step of creating a Profile Collection");
		ExtentCucumberAdapter.addTestStepLog("User is on third step of creating a Profile Collection");
	}
	
	public void user_is_on_profile_collection_page_with_profiles_added() {
		PerformanceUtils.waitForPageReady(driver, 1);
		LOGGER.info("User is on Profile Collection creation page with profiles added");
		ExtentCucumberAdapter.addTestStepLog("User is on Profile Collection creation page with profiles added");
	}
}
