package com.kfonetalentsuite.utils.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DynamicTagResolver {

	private static final Logger LOGGER = LogManager.getLogger(DynamicTagResolver.class);

	// Login type constants
	public static final String SSO_LOGIN = "SSO";
	public static final String NON_SSO_LOGIN = "NON_SSO";

	// Tag mapping for different login types
	private static final String SSO_LOGIN_TAG = "@SSO_Login_via_KFONE";
	private static final String NON_SSO_LOGIN_TAG = "@NON_SSO_Login_via_KFONE";

	public static String getLoginTag() {
		String loginType = CommonVariable.LOGIN_TYPE;

		if (loginType == null || loginType.trim().isEmpty()) {
			LOGGER.warn("Login type not configured, defaulting to NON_SSO");
			return NON_SSO_LOGIN_TAG;
		}

		switch (loginType.toUpperCase().trim()) {
		case SSO_LOGIN:
			return SSO_LOGIN_TAG;

		case NON_SSO_LOGIN:
			return NON_SSO_LOGIN_TAG;

		default:
			LOGGER.warn("Unknown login type '" + loginType + "', defaulting to NON_SSO");
			return NON_SSO_LOGIN_TAG;
		}
	}

	public static String getKFoneLoginTag() {
		String loginType = CommonVariable.LOGIN_TYPE;

		if (loginType == null || loginType.trim().isEmpty()) {
			LOGGER.warn("Login type not configured, defaulting to NON_SSO for KFONE");
			return NON_SSO_LOGIN_TAG;
		}

		switch (loginType.toUpperCase().trim()) {
		case SSO_LOGIN:
			return SSO_LOGIN_TAG;

		case NON_SSO_LOGIN:
			return NON_SSO_LOGIN_TAG;

		default:
			LOGGER.warn("Unknown login type '" + loginType + "', defaulting to NON_SSO for KFONE");
			return NON_SSO_LOGIN_TAG;
		}
	}

	public static String buildTagExpression(String featureTag) {
		String loginTag = getLoginTag();
		String tagExpression = loginTag + " or " + featureTag;

		LOGGER.info("Built tag expression: " + tagExpression);
		return tagExpression;
	}

	public static String buildKFoneTagExpression(String featureTag) {
		String loginTag = getKFoneLoginTag();
		String tagExpression = loginTag + " or " + featureTag;

		LOGGER.info("Built KFONE tag expression: " + tagExpression);
		return tagExpression;
	}

	public static String getCurrentLoginType() {
		return CommonVariable.LOGIN_TYPE != null ? CommonVariable.LOGIN_TYPE : NON_SSO_LOGIN;
	}

	public static boolean isSSOLogin() {
		return SSO_LOGIN.equalsIgnoreCase(getCurrentLoginType());
	}

	public static boolean isNonSSOLogin() {
		return NON_SSO_LOGIN.equalsIgnoreCase(getCurrentLoginType());
	}
}
