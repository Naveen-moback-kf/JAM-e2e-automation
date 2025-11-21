package com.kfonetalentsuite.pageobjects.JobMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.openqa.selenium.By;
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
import com.kfonetalentsuite.utils.PageObjectHelper;
import com.kfonetalentsuite.webdriverManager.DriverManager;

public class PO03_ValidateJobmappingHeaderSection {
	WebDriver driver = DriverManager.getDriver();

	protected static final Logger LOGGER = (Logger) LogManager.getLogger();
	PO03_ValidateJobmappingHeaderSection validateJobmappingHeaderSection;

	public static List<String> jobMappingWaffleMenuApplications = new ArrayList<String>();
	public static List<String> jobMappingWaffleMenuUtilities = new ArrayList<String>();
	public static List<String> PMWaffleMenuApplications = new ArrayList<String>();
	public static List<String> PMWaffleMenuUtilities = new ArrayList<String>();

	public PO03_ValidateJobmappingHeaderSection() throws IOException {
		// super();
		PageFactory.initElements(driver, this);
		// TODO Auto-generated constructor stub
	}

	WebDriverWait wait = DriverManager.getWait();
	Utilities utils = new Utilities();
	JavascriptExecutor js = (JavascriptExecutor) driver;

	// XPATHs
	@FindBy(xpath = "//*[@class='blocking-loader']//img")
	@CacheLookup
	WebElement pageLoadSpinner1;

	@FindBy(xpath = "//div[@data-testid='loader']//img")
	@CacheLookup
	WebElement pageLoadSpinner2;

	@FindBy(xpath = "//div[contains(@class,'global-nav-title-icon-container')]")
	@CacheLookup
	WebElement KFTalentSuitelogo;

	@FindBy(xpath = "//div[@data-testid='menu-icon']")
	@CacheLookup
	WebElement jobMappingWaffleMenu;

	@FindBy(xpath = "//*[@alt='Navigation Menu']")
	@CacheLookup
	WebElement waffleMenu;

	@FindBy(xpath = "//*[@alt='Job Mapping']")
	@CacheLookup
	WebElement jobMappingBtn;

	@FindBy(xpath = "//div//p[contains(text(),'SUBSCRIPTION')]")
	@CacheLookup
	WebElement waffleMenuHeader;

	@FindBy(xpath = "//p[text()='Applications']//..//div[1]//div[@data-testid='product']//div[2]")
	@CacheLookup
	List<WebElement> applicationsNamesInJobMappingWaffleMenu;

	@FindBy(xpath = "//p[text()='Applications']//..//div[1]//div[@data-testid='product']")
	@CacheLookup
	List<WebElement> applicationsButtonsInJobMappingWaffleMenu;

	@FindBy(xpath = "//p[text()='Utilities']//..//div[1]//div[@data-testid='product']//div[2]")
	@CacheLookup
	List<WebElement> utilitiesNamesInJobMappingWaffleMenu;

	@FindBy(xpath = "//p[text()='Utilities']//..//div[1]//div[@data-testid='product']")
	@CacheLookup
	List<WebElement> utilitiesButtonsInJobMappingWaffleMenu;

	@FindBy(xpath = "//div[not(contains(@class,'utilities')) and contains(@class,'applications')]//*[contains(@class,'link-sm')]")
	@CacheLookup
	List<WebElement> applicationsNamesInPMWaffleMenu;

	@FindBy(xpath = "//div[contains(@class,'utilities')]//*[contains(@class,'link-sm') or contains(@class,'link-aiauto')]")
	@CacheLookup
	List<WebElement> utilitiesNamesInPMWaffleMenu;

	@FindBy(xpath = "//p[text()='Applications']//..//div[1]//div[@data-testid='product']//div[contains(text(),'Profile')]")
	@CacheLookup
	WebElement PMapplicationName;

	@FindBy(xpath = "//div[contains(text(),'Profile')]//..//div")
	@CacheLookup
	WebElement PMapplicationButton;

	@FindBy(xpath = "//h1[contains(@class,'middle') and text()='Profile Manager']")
	@CacheLookup
	WebElement PMHeader;

	@FindBy(xpath = "//a[text()='Learn More']")
	@CacheLookup
	WebElement learnmoreBtn;

	@FindBy(xpath = "//*[contains(@class,'fa-search')]")
	@CacheLookup
	WebElement searchIconInLearnMorePage;

	@FindBy(xpath = "//button[contains(@class,'global-nav-client-name')]//span")
	@CacheLookup
	WebElement clientname;

	@FindBy(xpath = "//*[@data-testid='global-nav-user-avatar-avatar-0']")
	@CacheLookup
	WebElement profileLogo;

	@FindBy(xpath = "//button[@id='global-nav-user-dropdown-btn']")
	@CacheLookup
	WebElement profileBtn;

	@FindBy(xpath = "//button[text()='My Profile']")
	@CacheLookup
	WebElement myProfileBtn;

	@FindBy(xpath = "//div[@class='nav-profile-user-name']")
	@CacheLookup
	WebElement profileUserName;

	@FindBy(xpath = "//div[@class='nav-profile-email']")
	@CacheLookup
	WebElement profileUserEmail;

	@FindBy(xpath = "//span[text()='Sign Out']")
	@CacheLookup
	WebElement signoutBtn;

	@FindBy(xpath = "//*[text()='Sign in to your account']")
	@CacheLookup
	WebElement loginPageTextXapth;

	@FindBy(xpath = "//button[@id='ensAcceptAll']")
	@CacheLookup
	WebElement acceptAllCookies;

	@FindBy(xpath = "//button[@id='global-nav-menu-btn']")
	@CacheLookup
	public WebElement KfoneMenu;

	@FindBy(xpath = "//button[@aria-label='Job Mapping']")
	@CacheLookup
	public WebElement KfoneMenuJAMBtn;

	@FindBy(xpath = "//*[//*[@id='title-svg-icon']]")
	@CacheLookup
	public WebElement KFONE_landingPage_title;

	@FindBy(xpath = "//*[@class='blocking-loader']//img")
	@CacheLookup
	WebElement pageLoadSpinner;

	By KfoneClientsPageTitle = By.xpath("//h2[text()='Clients']");

	public void verify_kf_talent_suite_logo_is_displaying() {
		try {
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(KFTalentSuitelogo)).isDisplayed());
			PageObjectHelper.log(LOGGER,
					"KORN FERRY TALENT SUITE Logo is displaying as Expected on Job Mapping UI Header");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_kf_talent_suite_logo_is_displaying",
					"Issue in displaying KF Talent Suite logo in Job Mapping UI Header", e);
		}
	}

	public void click_on_kf_talent_suite_logo() {
		try {
			try {
				wait.until(ExpectedConditions.elementToBeClickable(KFTalentSuitelogo)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", KFTalentSuitelogo);
				} catch (Exception s) {
					utils.jsClick(driver, KFTalentSuitelogo);
				}
			}
			PageObjectHelper.log(LOGGER, "Clicked on KORN FERRY TALENT SUITE Logo in Job Mapping UI Header");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_kf_talent_suite_logo",
					"Issue in clicking on KORN FERRY TALENT SUITE Logo in Job Mapping UI Header", e);
		}
	}

	public void navigate_to_job_mapping_page_from_kfone_global_menu() {
		try {
			PerformanceUtils.waitForSpinnersToDisappear(driver, 10);

			// Click on KFONE Global Menu button
			try {
				wait.until(ExpectedConditions.elementToBeClickable(KfoneMenu)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", KfoneMenu);
				} catch (Exception s) {
					utils.jsClick(driver, KfoneMenu);
				}
			}

			PerformanceUtils.waitForPageReady(driver, 1);

			// Click on Job Mapping button in the menu
			try {
				wait.until(ExpectedConditions.elementToBeClickable(KfoneMenuJAMBtn)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", KfoneMenuJAMBtn);
				} catch (Exception s) {
					utils.jsClick(driver, KfoneMenuJAMBtn);
				}
			}

		// Wait for navigation to complete
		PerformanceUtils.waitForSpinnersToDisappear(driver, 10);
		PerformanceUtils.waitForPageReady(driver, 2);
		PageObjectHelper.log(LOGGER, "Successfully Navigated to Job Mapping screen");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "navigate_to_job_mapping_page_from_kfone_global_menu",
					"Failed to navigate to Job Mapping page from KFONE Global Menu", e);
		}
	}

	public void user_should_verify_client_name_is_correctly_displaying() {
		try {
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(clientname)).isDisplayed());
			String clientnameText = wait.until(ExpectedConditions.visibilityOf(clientname)).getText();
			Assert.assertEquals(PO01_KFoneLogin.clientName.get(), clientnameText);
			PageObjectHelper.log(LOGGER,
					"Client name correctly displaying on the Job Mapping UI Header: " + clientnameText);
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_verify_client_name_is_correctly_displaying",
					"Issue in displaying correct client name in Job Mapping UI Header", e);
		}
	}

	public void click_on_client_name_in_job_mapping_ui_header() {
		try {
			try {
				wait.until(ExpectedConditions.elementToBeClickable(clientname)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", clientname);
				} catch (Exception s) {
					utils.jsClick(driver, clientname);
				}
			}
			PageObjectHelper.log(LOGGER, "Clicked on client name in Job Mapping UI Header");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_client_name_in_job_mapping_ui_header",
					"Issue in clicking on client name in Job Mapping UI Header", e);
		}
	}

	public void verify_user_navigated_to_kfone_clients_page() {
	try {
		// Wait for spinner to disappear and page to load
		// OPTIMIZED: Single comprehensive wait
		PerformanceUtils.waitForPageReady(driver, 5);

			// Verify landing page elements
			wait.until(ExpectedConditions.visibilityOf(KFONE_landingPage_title)).isDisplayed();
			String text1 = utils.readText(KfoneClientsPageTitle, driver);
			String obj1 = "Clients";
			Assert.assertEquals(obj1, text1);

			PageObjectHelper.log(LOGGER, "User navigated to KFONE Clients Page as Expected");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_user_navigated_to_kfone_clients_page",
					"Issue in navigating to KFONE clients page", e);
		}
	}

	public void verify_user_profile_logo_is_displaying_and_clickable() {
		try {
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(profileLogo)).isDisplayed());
			try {
				wait.until(ExpectedConditions.elementToBeClickable(profileBtn)).click();
			} catch (Exception e) {
				try {
					js.executeScript("arguments[0].click();", profileBtn);
				} catch (Exception s) {
					utils.jsClick(driver, profileBtn);
				}
			}
			PageObjectHelper.log(LOGGER, "User Profile logo is displaying in Job Mapping Header and clicked on it");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_user_profile_logo_is_displaying_and_clickable",
					"Issue in displaying or clicking User Profile logo in Job Mapping Header", e);
		}
	}

	public void verify_user_profile_menu_is_opened() {
		try {
			PerformanceUtils.waitForPageReady(driver, 2);
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(profileUserName)).isDisplayed());
			PageObjectHelper.log(LOGGER, "User profile menu is opened on click of user profile logo");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_user_profile_menu_is_opened",
					"Issue in opening User Profile Menu", e);
		}
	}

	public void verify_user_name_is_displayed_in_profile_menu() {
		try {
			String profileUserNameText = wait.until(ExpectedConditions.visibilityOf(profileUserName)).getText();
			PageObjectHelper.log(LOGGER,
					"User Name: " + profileUserNameText + " is displayed in Profile Menu as expected");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_user_name_is_displayed_in_profile_menu",
					"Issue in displaying User name in User Profile Menu", e);
		}
	}

	public void verify_user_email_is_displayed_in_profile_menu() {
		try {
			String profileUserEmailText = wait.until(ExpectedConditions.visibilityOf(profileUserEmail)).getText();
			// Case-insensitive email comparison
			Assert.assertTrue(PO01_KFoneLogin.username.get().equalsIgnoreCase(profileUserEmailText),
					"Expected email: " + PO01_KFoneLogin.username.get() + " but found: " + profileUserEmailText);
			PageObjectHelper.log(LOGGER,
					"User Email: " + profileUserEmailText + " is displayed in Profile Menu as expected");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "verify_user_email_is_displayed_in_profile_menu",
					"Issue in displaying User email in User Profile Menu", e);
		}
	}

	public void click_on_signout_button_in_user_profile_menu() {
		try {
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(signoutBtn)).isDisplayed());
			try {
				wait.until(ExpectedConditions.elementToBeClickable(signoutBtn)).click();
			} catch (Exception e) {
				try {
					utils.jsClick(driver, signoutBtn);
				} catch (Exception s) {
					wait.until(ExpectedConditions.elementToBeClickable(signoutBtn)).click();
				}
			}
			PageObjectHelper.log(LOGGER, "Clicked on logout button in User Profile Menu");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_signout_button_in_user_profile_menu",
					"Issue in clicking logout button in User Profile Menu", e);
		}
	}

	public void user_should_be_signed_out_from_the_application() {
		try {
			// OPTIMIZED: Single comprehensive wait
			PerformanceUtils.waitForPageReady(driver, 3);
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(loginPageTextXapth)).isDisplayed());
			PageObjectHelper.log(LOGGER,
					"User signed out successfully and navigated back to Korn Ferry Talent Suite Sign In page");
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "user_should_be_signed_out_from_the_application",
					"Issue in signing out from Application", e);
		}
	}

}
