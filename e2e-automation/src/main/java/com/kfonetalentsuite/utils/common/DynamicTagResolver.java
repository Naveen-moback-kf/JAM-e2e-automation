package com.kfonetalentsuite.utils.common;

public class DynamicTagResolver {

	public static final String SSO_LOGIN = "SSO";
	public static final String NON_SSO_LOGIN = "NON_SSO";

	private static final String SSO_LOGIN_TAG = "@SSO_Login_via_KFONE";
	private static final String NON_SSO_LOGIN_TAG = "@NON_SSO_Login_via_KFONE";

	// Cache to avoid duplicate lookups
	private static String cachedLoginTag = null;
	private static String cachedLoginType = null;

	public static String getKFoneLoginTag() {
		if (cachedLoginTag != null) {
			return cachedLoginTag;
		}

		String loginType = getLoginType();
		
		if (SSO_LOGIN.equalsIgnoreCase(loginType)) {
			cachedLoginTag = SSO_LOGIN_TAG;
		} else {
			cachedLoginTag = NON_SSO_LOGIN_TAG;
		}
		
		return cachedLoginTag;
	}

	public static String getLoginType() {
		if (cachedLoginType != null) {
			return cachedLoginType;
		}

		// Ensure config is loaded
		CommonVariableManager.loadProperties();

		// Get from CommonVariableManager (already handles priority: system prop > env config -> fallback)
		String loginType = CommonVariableManager.LOGIN_TYPE;
		cachedLoginType = (loginType != null && !loginType.isEmpty()) 
				? loginType.toUpperCase().trim() 
				: NON_SSO_LOGIN;
		
		return cachedLoginType;
	}

	public static String getLoginTag() {
		return getKFoneLoginTag();
	}

	public static String getCurrentLoginType() {
		return getLoginType();
	}

	public static boolean isSSOLogin() {
		return SSO_LOGIN.equalsIgnoreCase(getCurrentLoginType());
	}

	public static boolean isNonSSOLogin() {
		return NON_SSO_LOGIN.equalsIgnoreCase(getCurrentLoginType());
	}

	public static String buildTagExpression(String featureTag) {
		return getLoginTag() + " or " + featureTag;
	}

	public static String buildKFoneTagExpression(String featureTag) {
		return getKFoneLoginTag() + " or " + featureTag;
	}

	public static void clearCache() {
		cachedLoginTag = null;
		cachedLoginType = null;
	}
}
