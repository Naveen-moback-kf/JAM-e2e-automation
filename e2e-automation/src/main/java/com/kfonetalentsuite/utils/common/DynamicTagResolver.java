package com.kfonetalentsuite.utils.common;

import java.util.Map;

/**
 * Dynamically resolves login tags based on Excel Execute=YES row.
 */
public class DynamicTagResolver {

	public static final String SSO_LOGIN = "SSO";
	public static final String NON_SSO_LOGIN = "NON_SSO";

	private static final String SSO_LOGIN_TAG = "@SSO_Login_via_KFONE";
	private static final String NON_SSO_LOGIN_TAG = "@NON_SSO_Login_via_KFONE";

	// Cache to avoid duplicate logs
	private static String cachedLoginTag = null;
	private static String cachedLoginType = null;

	/**
	 * Get login tag based on Excel Execute=YES row (cached)
	 */
	public static String getKFoneLoginTag() {
		if (cachedLoginTag != null) {
			return cachedLoginTag;
		}

		String loginType = getLoginTypeFromExcel();
		
		if (SSO_LOGIN.equalsIgnoreCase(loginType)) {
			cachedLoginTag = SSO_LOGIN_TAG;
		} else {
			cachedLoginTag = NON_SSO_LOGIN_TAG;
		}
		
		return cachedLoginTag;
	}

	/**
	 * Get login type from Excel (cached)
	 */
	public static String getLoginTypeFromExcel() {
		if (cachedLoginType != null) {
			return cachedLoginType;
		}

		try {
			Map<String, String> login = ExcelConfigProvider.getDefaultLogin();
			if (login != null) {
				String userType = login.get("UserType");
				if (userType != null && !userType.isEmpty()) {
					cachedLoginType = userType.toUpperCase().trim();
					return cachedLoginType;
				}
			}
		} catch (Exception e) {
			// Ignore
		}
		
		// Fallback to config.properties
		String configLoginType = CommonVariable.LOGIN_TYPE;
		cachedLoginType = (configLoginType != null && !configLoginType.isEmpty()) 
				? configLoginType.toUpperCase().trim() 
				: NON_SSO_LOGIN;
		return cachedLoginType;
	}

	public static String getLoginTag() {
		return getKFoneLoginTag();
	}

	public static String getCurrentLoginType() {
		return getLoginTypeFromExcel();
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
