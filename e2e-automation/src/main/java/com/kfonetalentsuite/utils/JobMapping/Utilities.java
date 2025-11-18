package com.kfonetalentsuite.utils.JobMapping;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.kfonetalentsuite.utils.common.CommonVariable;
import com.kfonetalentsuite.webdriverManager.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilities {
	private static final Logger LOGGER = LogManager.getLogger(Utilities.class);
	public static WebDriver driver;
	List<String> dropDownItems = new ArrayList<String>();
	ArrayList<String> verifiLst = new ArrayList<String>();
	Random rand = new Random();
	WebDriverWait wait = DriverManager.getWait();

	@FindBy(xpath="//*[@class='blocking-loader']//img")
	WebElement pageLoadSpinner;

	By loaderSelector = By.cssSelector("div.blocking-loader");

	public List<String> randomSelection(List<WebElement> AllCheckboxes, int count, By path, WebDriver driver) {
		int i = 1;
		List<String> addItems = new ArrayList<String>();
		try {
			addItems.clear();
			for (@SuppressWarnings("unused") WebElement ignored : AllCheckboxes) {
				while (i <= count) {
					List<WebElement> options = driver.findElements(path);
					int list = rand.nextInt(options.size());
					String isVisible = options.get(list).getAttribute("class");
					LOGGER.debug("Element visibility class: {}", isVisible);
					if (!isVisible.contains("selected")) {
					
					try {
						options.get(list).click();
					} catch (Exception e) {
						delaySeconds(10);
					}
					LOGGER.debug("Element selected status: {}", options.get(list).isSelected());
					// options.get(list).isSelected();
					addItems.add(options.get(list).getText());
					i++;
				}
				}

				break;
			}
			addItems.stream().sorted().collect(Collectors.toList());
		} catch (Exception e) {
			LOGGER.error("Error in randomSelection method: {}", e.getMessage(), e);
		}
		return addItems;
	}

	public void jsClick(WebDriver driver, WebElement element) {

		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].click();", element);
	}

	public void jsScroll(WebDriver driver, String xpath) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		try {
			js.executeScript("window.scrollBy(0,500)");
		} catch (Exception e) {
			// TODO: handle exception
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();",
					driver.findElements(By.xpath(xpath)));
		}
		
	}
	
	public void scrollToElement(WebElement webElement) throws Exception {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoViewIfNeeded()", webElement);
		// IMPROVED: Replaced Thread.sleep(500) with reduced intelligent wait
		try {
			Thread.sleep(300); // Reduced from 500ms for scroll completion
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	public void scrollToBottom() throws Exception {
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
		// IMPROVED: Replaced Thread.sleep(500) with reduced intelligent wait
		try {
			Thread.sleep(300); // Reduced from 500ms for scroll completion
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
	
	public void closeSurveyPopup() {
		// div class smcx-modal-close
		try {
			LOGGER.debug("Trying to close survey pop-up if it exists");
			WebElement surveyClose = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='smcx-modal-close']")));
			scrollToElement(surveyClose);
			surveyClose.click();
		} catch (Exception ex) {
			LOGGER.debug("Survey pop-up not found or already closed: {}", ex.getMessage());
		}
	}
	
//	public void checkAndWaitForLoaderCompletion() throws Exception{
//		if(waitForLoaderToBeDisplayed()) {		
//			waitForLoaderToComplete(); 
//		}
//	}
//	
//	public void closeSurveyPopup() {
//		// div class smcx-modal-close
//		try {
//			System.out.println("Trying to close survey pop-up if it exists");
//			WebElement surveyClose = longWait
//					.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='smcx-modal-close']")));
//			scrollToElement(surveyClose);
//			surveyClose.click();
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//	}
//
////	WebDriverWait longWait = new WebDriverWait(driver, 50);
//
//	private boolean waitForLoaderToBeDisplayed() throws Exception {
//		WebElement element = tryToGetLoader();
//		return element != null;
//	}
//
//	private WebElement tryToGetLoader() throws Exception {
//		WebElement loader = null;
//		try {
//			System.out.println("Checking for loader");
//			loader = longWait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("kf-blocking-loader")));
//			System.out.println("Found loader");
//		} catch (Exception ex) {
//			System.out.println("NO LOADER HERE");
//		}
//		return loader;
//	}
//
//	private void waitForLoaderToComplete() throws Exception {
//		// class blocking-loader
//		try {
//			WebElement loader = tryToGetLoader();
//			if (loader != null) {
//				System.out.println("Waiting for loader to complete");
//				longWait.until(ExpectedConditions
//						.not(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.tagName("kf-blocking-loader"))));
//				System.out.println("Loader is now invisible");
//			}
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//	}
//
//	public void scrollToElement(WebElement webElement) throws Exception {
//		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoViewIfNeeded()", webElement);
//		Thread.sleep(500);
//	}


	public List<String> strtoLst(String list) {

		try {
			// addItems.clear();
			String str[] = list.split("[\\r?\\n|\\r]");
			verifiLst = new ArrayList<String>(Arrays.asList(str));
			// verifiLst = (ArrayList<String>) Arrays.asList(str);
			verifiLst.remove(0);
			verifiLst.stream().sorted().collect(Collectors.toList());
		} catch (Exception e) {
			LOGGER.error("Error in verification list processing: {}", e.getMessage(), e);
		}
		return verifiLst;

	}

	public List<String> dropdown(WebDriver driver, By element, int value) {

		List<WebElement> dropDownTxt = driver.findElements(element);
		try {
			dropDownItems.clear();
			int size = dropDownTxt.size();
			/*
			 * for (int i = 0; i < size - 3; i++) { AllCheckboxes.get(i).click(); }
			 */
			if (size < value) {
				for (int i = 0; i < 3; i++) {
					dropDownTxt.get(i).click();
					dropDownItems.add(dropDownTxt.get(i).getText());
				}
			} else {
				for (int i = 0; i < value; i++) {
					dropDownTxt.get(i).click();
					dropDownItems.add(dropDownTxt.get(i).getText());
				}

			}
		} catch (Exception e) {
			LOGGER.error("Error in dropdown processing: {}", e.getMessage(), e);
		}
		return dropDownItems;

	}

	// Test method removed - JUnit dependency not available in this TestNG project
	public boolean whenTestingForOrderAgnosticEquality_ShouldBeTrue(List<String> first, List<String> second) {
		return first.size() == second.size() || (first.containsAll(second) && second.containsAll(first));
	}

	public boolean isClickable(WebElement element, WebDriver driver) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
			wait.until(ExpectedConditions.elementToBeClickable(element));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public List<String> randomSelectionCountry(List<WebElement> AllCheckboxes, int count, By path, WebDriver driver) {
		List<String> addItems = new ArrayList<String>();
		int i = 1;
		boolean pass = true;
		try {
			addItems.clear();
			for (@SuppressWarnings("unused") WebElement ignored : AllCheckboxes) {
				while (i <= count) {
					do {
						List<WebElement> options = driver.findElements(path);
						if (!options.isEmpty()) {
						int list = rand.nextInt(options.size());
						String isVisibl = options.get(list).getText();
						LOGGER.debug("Selected option text: {}", isVisibl);
						String isVisible = options.get(list).getAttribute("class");

						if (isVisible.equalsIgnoreCase("item visible")) {
							LOGGER.debug("Current environment: {}", CommonVariable.ENVIRONMENT);
							
							options.get(list).click();
							addItems.add(isVisibl);
							pass = false;
						}
						i++;
						}
					} while (pass);

				}
				break;
			}
			// addItems.stream().sorted().collect(Collectors.toList());
		} catch (Exception e) {
			LOGGER.error("Error in randomSelectionSeveralOptions method: {}", e.getMessage(), e);
		}
		return addItems;
	}

	public void delaySeconds(long secs) throws InterruptedException {
		TimeUnit.SECONDS.sleep(secs);
	}

	public boolean isEnable(WebDriver driver, By xpath) {
		boolean isGradeEnanble = false;
		try {
			isGradeEnanble = driver.findElement((xpath)).isEnabled();
			LOGGER.debug("Element enabled status before return: {}", isGradeEnanble);
		} catch (Exception e) {
			LOGGER.error("Error checking if element is enabled: {}", e.getMessage(), e);
		}
		return isGradeEnanble;
	}

	public static String currentDateTime() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH-mm-ss");
		LocalDateTime now = LocalDateTime.now();
		// System.out.println(a);
		return dtf.format(now);
	}

	public void onClickByXpath(String value, WebDriver driver) {

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		WebElement webWElement;
		webWElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(value)));
		webWElement.click();
		// driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	public void onSendValue(String xpath, String value, WebDriver driver) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		WebElement webWElement;
		webWElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
		webWElement.sendKeys(value);
		// driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

	}

	public String readText(By value, WebDriver driver) {
		String text = null;
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));
			// WebElement webWElement;
			text = wait.until(ExpectedConditions.visibilityOfElementLocated((value))).getText();
			// driver.findElement(xpath).getText();

		} catch (Exception e) {

		}
		return text;
	}

	public long currentMillis() {
		long cm = System.currentTimeMillis();
		
		return cm;
	}

	public String UniqueDateTime() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HHmmssyyyyMMdd");
		LocalDateTime now = LocalDateTime.now();
		String dateTime = dtf.format(now);
//		System.out.println(dtf.format(now));
		return dateTime;
	}

	public int randomNumberFromAndTo(int lowValue, int highValue) {

		Random random = new Random();
		int low = lowValue;
		int high = highValue;
		int result = random.nextInt(high - low) + low;

		return result;

	}

	public List<String> randomFilterSelection(List<WebElement> AllCheckboxes, int count, By path, WebDriver driver, WebElement filterItem) {
		int i = 1;
		List<String> addItems = new ArrayList<String>();


		try {
			addItems.clear();
			for (@SuppressWarnings("unused") WebElement ignored : AllCheckboxes) {
				while (i <= count) {
					List<WebElement> options = driver.findElements(path);
					if (!options.isEmpty()) {
						int list = rand.nextInt(options.size());
						// System.out.println(list);
						String checkbox = "";
							checkbox = options.get(list).getAttribute("class");
							if (!checkbox.contains("selected")) {
								//act.moveToElement(options.get(list)).click().perform();
								try {
									LOGGER.debug("Attempting to click checkbox");
									//delaySeconds(5);
									wait.until(ExpectedConditions.elementToBeClickable(options.get(list))).click();
								} 
								catch (Exception e) {
									LOGGER.debug("Scroll action required for checkbox click");
									delaySeconds(5);
									LOGGER.debug("Check box index: {}", list); 
									jsScroll(driver,0,-350);
									options.get(list).click();
								}
								LOGGER.debug("Checkbox class: {}", checkbox);
								addItems.add(options.get(list).getText());
								i++;
						}
					}
				}
				break;
			}
			addItems.stream().sorted().collect(Collectors.toList());
		} catch (Exception e) {
			LOGGER.error("Error in randomSelectionSeveralOptions method: {}", e.getMessage(), e);
		}
		return addItems;
	}

	public void jsScroll(WebDriver driver, int xaxis, int yaxis) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(" + xaxis + "," + yaxis + ")", "");
	}

	
	
	public List<String> randomSelectionMP(List<WebElement> AllCheckboxes, int count, By path, WebDriver driver) {
		int i = 1;
		List<String> addItems = new ArrayList<String>();
		try {
			addItems.clear();
			for (@SuppressWarnings("unused") WebElement ignored : AllCheckboxes) {
				while (i <= count) {
					List<WebElement> options = driver.findElements(path);
					int list = rand.nextInt(options.size());
					/*String isVisibl = options.get(list).getText();
					System.out.println(isVisibl);*/
					String isVisible = options.get(list).getAttribute("class");
					LOGGER.debug("Random selection element class: {}", isVisible);
					if (!isVisible.contains("selected")) {
					options.get(list).click();
					//System.out.println(options.get(list).isSelected());
					// options.get(list).isSelected();
					addItems.add(options.get(list).getText());
					i++;
					}
				}

				break;
			}
			addItems.stream().sorted().collect(Collectors.toList());
		} catch (Exception e) {
			LOGGER.error("Error in randomSelectionSeveralOptions method: {}", e.getMessage(), e);
		}
		return addItems;
	}
	
	
	public List<String> randomSelectionPGF(List<WebElement> AllCheckboxes, int count, By path, WebDriver driver) {
		int i = 1;
		List<String> addItems = new ArrayList<String>();
		try {
			addItems.clear();
			for (@SuppressWarnings("unused") WebElement ignored : AllCheckboxes) {
				while (i <= count) {
					List<WebElement> options = driver.findElements(path);
					if (!options.isEmpty()) {
					int list = rand.nextInt(options.size());
					/*String isVisibl = options.get(list).getText();
					System.out.println(isVisibl);*/
					String isVisible = options.get(list).getAttribute("class");
					LOGGER.debug("Success profile selection element class: {}", isVisible);
					if (!isVisible.contains("selected")) {
					options.get(list).click();
					addItems.add(options.get(list).getText());
					i++;
					}
					delaySeconds(8);
					//wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner));
				}
				}
				break;
			}
			addItems.stream().sorted().collect(Collectors.toList());
		} catch (Exception e) {
			LOGGER.error("Error in randomSelectionSeveralOptions method: {}", e.getMessage(), e);
		}
		return addItems;
	}
	
	
	public Integer countInteger(String strng) {

		 char[] chars = strng.toCharArray();
	      StringBuilder sb = new StringBuilder();
	      for(char c : chars){
	         if(Character.isDigit(c)){
	            sb.append(c);
	         }
	      }
	      return Integer.valueOf(sb.toString());
	}	
	
	public String extractNumericValue(String str)
    {       
        str = str.replaceAll("[^0-9]", ""); // regular expression     
        str = str.replaceAll(" +", ""); 
        if (str.equals(""))
            return "-1";
 
        return str.trim();
    }
	
	public void pageLoader() {
		if (wait.until(ExpectedConditions.visibilityOf(pageLoadSpinner)) != null) {
			LOGGER.debug("Page loading spinner is visible");
			wait.until(ExpectedConditions.invisibilityOf(pageLoadSpinner));
			LOGGER.debug("Page loading spinner is now dismissed");
		}
	}

	public void waitForLoaderToHide() throws InterruptedException {
		if (driver == null) {
			driver = DriverManager.getDriver();
		}

		TimeUnit.MILLISECONDS.sleep(200);
		List<WebElement> loader = driver.findElements(loaderSelector);

		if (!loader.isEmpty())
			wait.until(ExpectedConditions.invisibilityOf(loader.get(0)));
	}

	public void waitForLoaderToAppear() throws InterruptedException {
		if (driver == null) {
			driver = DriverManager.getDriver();
		}

		for (int i = 0; i < 20; i++) {
			List<WebElement> loader = driver.findElements(loaderSelector);

			TimeUnit.MILLISECONDS.sleep(500);

			if (!loader.isEmpty())
				break;
		}
	}

	public void click(WebElement element) throws InterruptedException {
		waitForLoaderToHide();
		wait.until(ExpectedConditions.elementToBeClickable(element));
		element.click();
		waitForLoaderToHide();
	}

	public void sendText(WebElement element, String text) throws InterruptedException {
		clearInput(element);
		element.sendKeys(text);
		delaySeconds(1);
		waitForLoaderToHide();
	}

	public void clearInput(WebElement element) throws InterruptedException {
		String existingText = element.getAttribute("value");

		if (!existingText.isEmpty()) {
			element.sendKeys(Keys.CONTROL + "a");
			element.sendKeys(Keys.DELETE);
			waitForLoaderToHide();
		}
	}

	public void hover(WebElement element, WebDriver driver) {
		Actions actions = new Actions(driver);

		actions.moveToElement(element).perform();
	}

	public static String EnvironmentUrl() {
		switch (CommonVariable.ENVIRONMENT) {
			case "ProdCN":
				return CommonVariable.PRODCNURL;

			case "ProdEU":
				return CommonVariable.PRODEUURL;

			case "ProdUS":
				return CommonVariable.PRODUSURL;

			case "Stage":
				return CommonVariable.STAGEURL;

			default:
				return CommonVariable.TESTURL;
		}
	}

	public void waitForDisplayed(WebElement element) {
		wait.until(ExpectedConditions.elementToBeClickable(element));
	}
	
	public static boolean isFileDownload(String expectedFileName, String fileExtension, int timeout) throws IOException {
		
		//Download Folder Path
		String folderName = System.getProperty("user.dir")+File.separator+"externalFiles\\downloadFiles";
		File[] listOfFiles;
		String fileName;
		boolean fileDownload = false;
		
		long startTime = Instant.now().toEpochMilli();
		long waitTime = startTime + timeout;
		
		while(Instant.now().toEpochMilli() < waitTime) {
			//get all the files of Downloaded Folder
			listOfFiles = new File(folderName).listFiles();
			
			for(File file:listOfFiles) {
				
				fileName = file.getName().toLowerCase();
				
				if (fileName.contains(expectedFileName.toLowerCase()) && fileName.contains(fileExtension.toLowerCase()) && !fileName.contains("crDownload") && file.lastModified() > startTime) {
					fileDownload = true;
					file.delete(); // Delete downloaded File after verifying
					LOGGER.info("Downloaded file {} deleted successfully from folder {}", fileName, folderName);
//					FileUtils.cleanDirectory(new File(folderName)); // Delete all files in the directory which contains downloaded file
//					System.out.println("Folder " + folderName + " with downloaded files is cleaned....");
					break;
				}
			}
			if(fileDownload == true) {
				break;
			}
		}
		
		return fileDownload;
		
	}
	
	public static void takeSnapShot(WebDriver webdriver,String fileWithPath) throws Exception{

        //Convert web driver object to TakeScreenshot

        TakesScreenshot scrShot =((TakesScreenshot)webdriver);

        //Call getScreenshotAs method to create image file

                File SrcFile=scrShot.getScreenshotAs(OutputType.FILE);

            //Move image file to new destination

                File DestFile=new File(fileWithPath);

                //Copy file at destination

                FileUtils.copyFile(SrcFile, DestFile);

    }
	
	// ========================================
	// CONSOLIDATED DATA PARSING METHODS
	// (From DataParsingHelper - added for consolidation)
	// ========================================
	
	/**
	 * Extract integer value from XML content using regex pattern
	 */
	public static int extractIntFromXML(String content, String pattern, int defaultValue) {
		try {
			Pattern regex = Pattern.compile(pattern);
			Matcher matcher = regex.matcher(content);
			if (matcher.find()) {
				return Integer.parseInt(matcher.group(1));
			}
		} catch (Exception e) {
			LOGGER.warn("Could not extract int with pattern '{}': {}", pattern, e.getMessage());
		}
		return defaultValue;
	}
	
	/**
	 * Extract string value from XML content using regex pattern
	 */
	public static String extractStringFromXML(String content, String pattern) {
		try {
			Pattern regex = Pattern.compile(pattern);
			Matcher matcher = regex.matcher(content);
			if (matcher.find()) {
				return matcher.group(1);
			}
		} catch (Exception e) {
			LOGGER.warn("Could not extract string with pattern '{}': {}", pattern, e.getMessage());
		}
		return null;
	}
	
	/**
	 * Extract user role from session storage and store it globally
	 * Navigates: Session Storage -> HayGroup.user.roles -> [0] -> name
	 * Stores the role in CommonVariable.CURRENT_USER_ROLE (ThreadLocal) for use across all feature files
	 * 
	 * @return User role name (e.g., "KF Super User") or null if not found
	 */
	public static String getUserRoleFromSessionStorage() {
		try {
			WebDriver driver = DriverManager.getDriver();
			JavascriptExecutor js = (JavascriptExecutor) driver;
			
			LOGGER.info("Extracting user role from session storage...");
			
			// Execute JavaScript to get user role from session storage
			String script = """
				try {
					// Get the HayGroup.user.roles from session storage
					var userRolesData = sessionStorage.getItem('HayGroup.user.roles');
					
					if (!userRolesData) {
						return 'SESSION_STORAGE_KEY_NOT_FOUND';
					}
					
					// Parse the JSON data
					var rolesArray = JSON.parse(userRolesData);
					
					if (!Array.isArray(rolesArray) || rolesArray.length === 0) {
						return 'ROLES_ARRAY_EMPTY';
					}
					
					// Get the first role (index 0) and extract the name
					var firstRole = rolesArray[0];
					
					if (!firstRole || !firstRole.name) {
						return 'ROLE_NAME_NOT_FOUND';
					}
					
					return firstRole.name;
					
				} catch (error) {
					return 'ERROR: ' + error.message;
				}
			""";
			
			Object result = js.executeScript(script);
			String roleName = (result != null) ? result.toString() : null;
			
			if (roleName != null && !roleName.startsWith("ERROR") && 
				!roleName.equals("SESSION_STORAGE_KEY_NOT_FOUND") && 
				!roleName.equals("ROLES_ARRAY_EMPTY") && 
				!roleName.equals("ROLE_NAME_NOT_FOUND")) {
			
			// Store the role globally for use across all feature files
			CommonVariable.CURRENT_USER_ROLE.set(roleName);
			LOGGER.info("Current User Role : '{}'", roleName);
			return roleName;
		} else {
			LOGGER.warn("Failed to extract user role. Result: {}", roleName);
			CommonVariable.CURRENT_USER_ROLE.set(null);
			return null;
		}
		
	} catch (Exception e) {
		LOGGER.error("Exception while extracting user role from session storage: {}", e.getMessage());
		CommonVariable.CURRENT_USER_ROLE.set(null);
			return null;
		}
	}
	
	/**
	 * Get the currently stored user role (set after login)
	 * 
	 * @return Current user role from global variable, or null if not set
	 */
	public static String getCurrentUserRole() {
		return CommonVariable.CURRENT_USER_ROLE.get();
	}
	
	/**
	 * Check if user has a specific role
	 * 
	 * @param expectedRole Role to check against
	 * @return true if current user role matches expected role
	 */
	public static boolean hasRole(String expectedRole) {
		String currentRole = CommonVariable.CURRENT_USER_ROLE.get();
		if (currentRole == null || expectedRole == null) {
			return false;
		}
		return currentRole.equals(expectedRole);
	}
	
	/**
	 * Set user role after login and store it globally
	 * This method fetches the role from session storage and stores it for use across all feature files
	 * 
	 * @return true if role was successfully retrieved and stored, false otherwise
	 */
	public static boolean setCurrentUserRoleFromSessionStorage() {
		String role = getUserRoleFromSessionStorage();
		return role != null;
	}
	
	/**
	 * Clear the stored user role (useful for cleanup between tests)
	 */
	public static void clearCurrentUserRole() {
		CommonVariable.CURRENT_USER_ROLE.set(null);
		LOGGER.info("Cleared stored user role");
	}
	
	/**
	 * Verify user role after login and log the result
	 * Now uses the globally stored role for validation across feature files
	 * 
	 * @param expectedRole Expected role name (optional - if null, just logs the found role)
	 * @return true if role matches expected (or if no expected role provided), false otherwise
	 */
	public static boolean verifyUserRole(String expectedRole) {
		try {
			// First try to get from global variable
			String actualRole = CommonVariable.CURRENT_USER_ROLE.get();
			
			// If not set globally, fetch from session storage and store it
			if (actualRole == null) {
				LOGGER.info("User role not stored globally, fetching from session storage...");
				actualRole = getUserRoleFromSessionStorage();
			}
			
			if (actualRole == null) {
				LOGGER.error(" Could not retrieve user role from session storage");
				return false;
			}
			
			LOGGER.info(" Current User Role: '{}'", actualRole);
			
			if (expectedRole != null && !expectedRole.trim().isEmpty()) {
				if (actualRole.equals(expectedRole)) {
					LOGGER.info("... User role verification PASSED: Expected '{}', Found '{}'", expectedRole, actualRole);
					return true;
				} else {
					LOGGER.error(" User role verification FAILED: Expected '{}', Found '{}'", expectedRole, actualRole);
					return false;
				}
			} else {
				LOGGER.info("... User role successfully retrieved: '{}'", actualRole);
				return true;
			}
			
		} catch (Exception e) {
			LOGGER.error("Exception during user role verification: {}", e.getMessage());
			return false;
		}
	}
	
	/**
	 * Get all available user roles from session storage
	 * 
	 * @return List of all role names, or empty list if none found
	 */
	public static List<String> getAllUserRolesFromSessionStorage() {
		List<String> roleNames = new ArrayList<>();
		
		try {
			WebDriver driver = DriverManager.getDriver();
			JavascriptExecutor js = (JavascriptExecutor) driver;
			
			LOGGER.info("Extracting all user roles from session storage...");
			
			String script = """
				try {
					var userRolesData = sessionStorage.getItem('HayGroup.user.roles');
					
					if (!userRolesData) {
						return [];
					}
					
					var rolesArray = JSON.parse(userRolesData);
					
					if (!Array.isArray(rolesArray)) {
						return [];
					}
					
					var roleNames = [];
					for (var i = 0; i < rolesArray.length; i++) {
						if (rolesArray[i] && rolesArray[i].name) {
							roleNames.push(rolesArray[i].name);
						}
					}
					
					return roleNames;
					
				} catch (error) {
					return [];
				}
			""";
			
			@SuppressWarnings("unchecked")
			List<String> result = (List<String>) js.executeScript(script);
			
			if (result != null && !result.isEmpty()) {
				roleNames.addAll(result);
				LOGGER.info("Successfully extracted {} user roles: {}", roleNames.size(), roleNames);
			} else {
				LOGGER.warn("No user roles found in session storage");
			}
			
		} catch (Exception e) {
			LOGGER.error("Exception while extracting all user roles: {}", e.getMessage());
		}
		
		return roleNames;
	}
	
	/**
	 * Extract JSON string value by index (simplified approach)
	 */
	public static String extractJsonString(String jsonBlock, int index) {
		try {
			// Simple pattern to extract string values from JSON
			Pattern pattern = Pattern.compile("\"([^\"]+)\"");
			Matcher matcher = pattern.matcher(jsonBlock);
			
			int count = 0;
			while (matcher.find()) {
				if (count == index) {
					return matcher.group(1);
				}
				count++;
			}
		} catch (Exception e) {
			LOGGER.warn("Could not extract JSON string at index {}: {}", index, e.getMessage());
		}
		return "";
	}
}
