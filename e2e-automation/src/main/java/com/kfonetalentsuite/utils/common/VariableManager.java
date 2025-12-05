package com.kfonetalentsuite.utils.common;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

public class VariableManager {

	private static final Logger LOGGER = (Logger) LogManager.getLogger(VariableManager.class);
	private static volatile VariableManager instance;
	private static final ReentrantLock lock = new ReentrantLock();
	private static volatile Properties config;
	private static volatile boolean isInitialized = false;

	private VariableManager() {

	}

	public static VariableManager getInstance() {
		if (instance == null) {
			lock.lock();
			try {
				if (instance == null) {
					instance = new VariableManager();

				}
			} finally {
				lock.unlock();
			}
		}
		return instance;
	}

	public boolean loadProperties() {
		if (isInitialized) {
			return true;
		}

		lock.lock();
		try {
			if (isInitialized) {
				return true;
			}
			config = new Properties();
			try {
				config.load(getClass().getResourceAsStream("/config.properties"));
			} catch (IOException e) {
				LOGGER.error("Failed to load config.properties: " + e.getMessage());
				return false;
			}
			populateCommonVariables();
			isInitialized = true;
			return true;

		} finally {
			lock.unlock();
		}
	}

	private void populateCommonVariables() {
		CommonVariable.ENVIRONMENT = config.getProperty("Environment");
		CommonVariable.BROWSER = config.getProperty("browser");
		CommonVariable.Super_USERNAME = config.getProperty("super_Username");
		CommonVariable.Super_PASSWORD = config.getProperty("super_Password");
		CommonVariable.USERNAME = config.getProperty("username");
		CommonVariable.PASSWORD = config.getProperty("password");
		CommonVariable.TESTURL = config.getProperty("TestUrl");
		CommonVariable.STAGEURL = config.getProperty("StageUrl");
		CommonVariable.PRODEUURL = config.getProperty("ProdEUUrl");
		CommonVariable.PRODUSURL = config.getProperty("ProdUSUrl");
		CommonVariable.PRODCNURL = config.getProperty("ProdCNUrl");
		CommonVariable.BENCHMARKING_TXT = config.getProperty("rewardBenchMarkingTxt");
		CommonVariable.REGRESSION_TXT = config.getProperty("regressionLineTxt");
		CommonVariable.OVERVIEWHC_TXT = config.getProperty("overviewHCTxt");
		CommonVariable.EMPLOYEEALLOCATION_TXT = config.getProperty("employeeallocationTxt");
		CommonVariable.PAYINTEQTY_TXT = config.getProperty("payinternalequityTxt");
		CommonVariable.PAYINTEQTYDD_TXT = config.getProperty("payinternalequityddTxt");
		CommonVariable.CROSSFNANALYSIS_TXT = config.getProperty("crossfnanalysisTxt");
		CommonVariable.MIXOFPAY_TXT = config.getProperty("mixofpayTxt");
		CommonVariable.PayPerformance_TXT = config.getProperty("payperfomanceTxt");
		CommonVariable.PayPerformanceOT_TXT = config.getProperty("payperfomanceOTTxt");
		CommonVariable.CompOverview_TXT = config.getProperty("compoverviewTxt");
		CommonVariable.CompDrilldown_TXT = config.getProperty("compDrilldownTxt");
		CommonVariable.GENDERANALYSIS_TXT = config.getProperty("genderAnalysisTxt");
		CommonVariable.SHORTTERM_TXT = config.getProperty("shortTermInceTxt");
		CommonVariable.LONGTERM_TXT = config.getProperty("longTermInceTxt");
		CommonVariable.CARSELGI_TXT = config.getProperty("carsElgiTxt");
		CommonVariable.COMPARE2MRKT_TXT = config.getProperty("compare2mrktTxt");
		CommonVariable.ICENVIRONMENT = config.getProperty("ICEnvironment");
		CommonVariable.ICUrl = config.getProperty("ICUrl");
		CommonVariable.ICusername = config.getProperty("ICusername");
		CommonVariable.ICpassword = config.getProperty("ICpassword");
		CommonVariable.DemoUsername = config.getProperty("demoUsername");
		CommonVariable.DemoPassword = config.getProperty("demoPassword");
		CommonVariable.IC_THusername = config.getProperty("IC_THusername");
		CommonVariable.IC_THpassword = config.getProperty("IC_THpassword");
		CommonVariable.SAML_USERNAME = config.getProperty("AI_AUTO_SAML_Username");
		CommonVariable.SAML_PASSWORD = config.getProperty("AI_AUTO_SAML_Password");
		CommonVariable.KFONE_QAURL = config.getProperty("KFONE_QAUrl");
		CommonVariable.KFONE_DEVURL = config.getProperty("KFONE_DevUrl");
		CommonVariable.KFONE_STAGEURL = config.getProperty("KFONE_StageUrl");
		CommonVariable.KFONE_PRODEUURL = config.getProperty("KFONE_ProdEUUrl");
		CommonVariable.KFONE_PRODUSURL = config.getProperty("KFONE_ProdUSUrl");
		CommonVariable.SSO_USERNAME = config.getProperty("SSO_Login_Username");
		CommonVariable.SSO_PASSWORD = config.getProperty("SSO_Login_Password");
		CommonVariable.NON_SSO_USERNAME = config.getProperty("NON_SSO_Login_Username");
		CommonVariable.NON_SSO_PASSWORD = config.getProperty("NON_SSO_Login_Password");
		CommonVariable.HEADLESS_MODE = config.getProperty("headless.mode");
		CommonVariable.EXCEL_REPORTING_ENABLED = config.getProperty("excel.reporting");
		CommonVariable.ALLURE_REPORTING_ENABLED = config.getProperty("allure.reporting");
		CommonVariable.LOGIN_TYPE = config.getProperty("login.type");
		CommonVariable.KEEP_SYSTEM_AWAKE = config.getProperty("keep.system.awake");
		CommonVariable.TARGET_PAMS_ID = config.getProperty("target.pams.id");
	}

	public static boolean isInitialized() {
		return isInitialized;
	}

	public static Properties getConfig() {
		return config;
	}
}
