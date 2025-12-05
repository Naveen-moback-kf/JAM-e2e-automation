package com.kfonetalentsuite.utils.common;

public class CommonVariable {

	// Static fields
	public static String PRODUSURL;
	public static String BROWSER;
	public static String Super_USERNAME;
	public static String Super_PASSWORD;
	public static String USERNAME;
	public static String PASSWORD;
	public static String TESTURL;
	public static String STAGEURL;
	public static String PRODEUURL;
	public static String ENVIRONMENT;
	public static String PRODCNURL;
	public static String BENCHMARKING_TXT;
	public static String REGRESSION_TXT;
	public static String OVERVIEWHC_TXT;
	public static String EMPLOYEEALLOCATION_TXT;
	public static String PAYINTEQTY_TXT;
	public static String PAYINTEQTYDD_TXT;
	public static String CROSSFNANALYSIS_TXT;
	public static String MIXOFPAY_TXT;
	public static String PayPerformance_TXT;
	public static String PayPerformanceOT_TXT;
	public static String CompOverview_TXT;
	public static String CompDrilldown_TXT;
	public static String GENDERANALYSIS_TXT;
	public static String SHORTTERM_TXT;
	public static String LONGTERM_TXT;
	public static String CARSELGI_TXT;
	public static String COMPARE2MRKT_TXT;
	public static String ICUrl;
	public static String ICusername;
	public static String ICpassword;
	public static String ICENVIRONMENT;
	public static String DemoUsername;
	public static String DemoPassword;
	public static String IC_THusername;
	public static String IC_THpassword;
	public static String SAML_USERNAME;
	public static String SAML_PASSWORD;
	public static String KFONE_QAURL;
	public static String KFONE_DEVURL;
	public static String KFONE_STAGEURL;
	public static String KFONE_PRODEUURL;
	public static String KFONE_PRODUSURL;
	public static String SSO_USERNAME;
	public static String SSO_PASSWORD;
	public static String NON_SSO_USERNAME;
	public static String NON_SSO_PASSWORD;
	public static String HEADLESS_MODE;
	public static String EXCEL_REPORTING_ENABLED;
	public static String ALLURE_REPORTING_ENABLED;
	public static String LOGIN_TYPE;
	public static String KEEP_SYSTEM_AWAKE;
	public static String TARGET_PAMS_ID;

	// Global User Role - Thread-local to support parallel execution
	// Set after login and used across all feature files
	public static ThreadLocal<String> CURRENT_USER_ROLE = ThreadLocal.withInitial(() -> null);
	static {
		try {
			// Use optimized singleton VariableManager
			VariableManager.getInstance().loadProperties();
		} catch (Exception e) {
			// Set default values if properties loading fails
			ENVIRONMENT = "Test"; // Default environment
			BROWSER = "chrome"; // Default browser
			HEADLESS_MODE = "true"; // Default to headless mode
			EXCEL_REPORTING_ENABLED = "true"; // Default to Excel reporting enabled
			ALLURE_REPORTING_ENABLED = "true"; // Default to Allure reporting enabled
			System.err.println("Failed to load properties, using defaults: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
