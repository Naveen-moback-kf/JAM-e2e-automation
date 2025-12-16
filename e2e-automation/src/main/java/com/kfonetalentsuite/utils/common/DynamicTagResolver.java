package com.kfonetalentsuite.utils.common;

/**
 * Dynamically resolves login tags based on environment configuration.
 * Supports CI/CD overrides via system properties.
 * 
 * Usage:
 *   mvn test -Denv=qa -Dlogin.type=SSO    → Uses SSO login tag
 *   mvn test -Denv=qa -Dlogin.type=NON_SSO → Uses NON_SSO login tag
 */
public class DynamicTagResolver {

	public static final String SSO_LOGIN = "SSO";
	public static final String NON_SSO_LOGIN = "NON_SSO";

	private static final String SSO_LOGIN_TAG = "@SSO_Login_via_KFONE";
	private static final String NON_SSO_LOGIN_TAG = "@NON_SSO_Login_via_KFONE";

	// Cache to avoid duplicate lookups
	private static String cachedLoginTag = null;
	private static String cachedLoginType = null;

	/**
	 * Get login tag based on configured login type
	 */
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

	/**
	 * Get login type from configuration
	 * Priority: System Property > Environment Config > Default (NON_SSO)
	 */
	public static String getLoginType() {
		if (cachedLoginType != null) {
			return cachedLoginType;
		}

		// Ensure config is loaded
		VariableManager.getInstance().loadProperties();

		// Get from CommonVariable (already handles priority: system prop > env config > fallback)
		String loginType = CommonVariable.LOGIN_TYPE;
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
