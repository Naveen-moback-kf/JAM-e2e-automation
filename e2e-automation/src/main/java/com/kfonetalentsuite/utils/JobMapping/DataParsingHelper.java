package com.kfonetalentsuite.utils.JobMapping;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DataParsingHelper {

	private static final Logger LOGGER = LogManager.getLogger(DataParsingHelper.class);

	public static int extractIntFromXML(String content, String pattern, int defaultValue) {
		try {
			Pattern regex = Pattern.compile(pattern);
			Matcher matcher = regex.matcher(content);
			if (matcher.find()) {
				return Integer.parseInt(matcher.group(1));
			}
		} catch (Exception e) {
			LOGGER.debug("Could not extract int with pattern '{}': {}", pattern, e.getMessage());
		}
		return defaultValue;
	}

	public static long extractLongFromXML(String content, String pattern, long defaultValue) {
		try {
			Pattern regex = Pattern.compile(pattern);
			Matcher matcher = regex.matcher(content);
			if (matcher.find()) {
				return Long.parseLong(matcher.group(1));
			}
		} catch (Exception e) {
			LOGGER.debug("Could not extract long with pattern '{}': {}", pattern, e.getMessage());
		}
		return defaultValue;
	}

	public static String extractStringFromXML(String content, String pattern) {
		try {
			Pattern regex = Pattern.compile(pattern);
			Matcher matcher = regex.matcher(content);
			if (matcher.find()) {
				return matcher.group(1);
			}
		} catch (Exception e) {
			LOGGER.debug("Could not extract string with pattern '{}': {}", pattern, e.getMessage());
		}
		return null;
	}

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
			LOGGER.debug("Could not extract JSON string at index {}: {}", index, e.getMessage());
		}
		return "";
	}

	public static String extractScenarioStatus(String jsonBlock) {
		// Look for common status patterns in JSON
		if (jsonBlock.contains("\"passed\"") || jsonBlock.contains("\"status\":\"passed\"")) {
			return "PASSED";
		} else if (jsonBlock.contains("\"failed\"") || jsonBlock.contains("\"status\":\"failed\"")) {
			return "FAILED";
		} else if (jsonBlock.contains("\"skipped\"") || jsonBlock.contains("\"status\":\"skipped\"")) {
			return "SKIPPED";
		}
		return "UNKNOWN";
	}

	public static String formatDuration(long milliseconds) {
		if (milliseconds <= 0) {
			return "0m 0s";
		}

		long seconds = milliseconds / 1000;
		long minutes = seconds / 60;
		seconds = seconds % 60;

		if (minutes > 0) {
			return String.format("%dm %ds", minutes, seconds);
		} else {
			return String.format("%ds", seconds);
		}
	}

	public static long parseDurationToMs(String durationStr) {
		if (durationStr == null || durationStr.trim().isEmpty()) {
			return 0;
		}

		try {
			// Handle formats like "2m 30s", "45s", "1m", etc.
			long totalMs = 0;

			// Extract minutes
			Pattern minutePattern = Pattern.compile("(\\d+)m");
			Matcher minuteMatcher = minutePattern.matcher(durationStr);
			if (minuteMatcher.find()) {
				totalMs += Long.parseLong(minuteMatcher.group(1)) * 60 * 1000;
			}

			// Extract seconds
			Pattern secondPattern = Pattern.compile("(\\d+)s");
			Matcher secondMatcher = secondPattern.matcher(durationStr);
			if (secondMatcher.find()) {
				totalMs += Long.parseLong(secondMatcher.group(1)) * 1000;
			}

			return totalMs;
		} catch (Exception e) {
			LOGGER.debug("Could not parse duration '{}': {}", durationStr, e.getMessage());
			return 0;
		}
	}

	public static String cleanScenarioNameForExcelDisplay(String scenarioName) {
		if (scenarioName == null || scenarioName.trim().isEmpty()) {
			return "Unknown Scenario";
		}

		String cleaned = scenarioName.trim();

		// Remove common prefixes that clutter Excel display
		String[] prefixesToRemove = { "o:", "Scenario:", "Test:", "Execute:", "Run:" };

		for (String prefix : prefixesToRemove) {
			if (cleaned.toLowerCase().startsWith(prefix.toLowerCase())) {
				cleaned = cleaned.substring(prefix.length()).trim();
			}
		}

		// Remove runner class suffixes like "Runner01", "Runner02" etc.
		cleaned = cleaned.replaceAll("Runner\\d+\\s*$", "").trim();

		// If empty after cleaning, return original
		if (cleaned.isEmpty()) {
			return scenarioName.trim();
		}

		return cleaned;
	}

	public static String extractRunnerClassFromSignature(String methodBlock) {
		try {
			// Look for class name patterns in TestNG method signatures
			Pattern pattern = Pattern.compile("class-name=\"([^\"]+)\"");
			Matcher matcher = pattern.matcher(methodBlock);

			if (matcher.find()) {
				String fullClassName = matcher.group(1);
				// Extract just the class name without package
				if (fullClassName.contains(".")) {
					return fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
				}
				return fullClassName;
			}
		} catch (Exception e) {
			LOGGER.debug("Could not extract runner class from signature: {}", e.getMessage());
		}

		return null;
	}

	public static String makeBusinessFriendly(String technicalName) {
		if (technicalName == null || technicalName.trim().isEmpty()) {
			return "Business Process";
		}

		return technicalName.replaceAll("([A-Z])", " $1") // Add spaces before capitals
				.replaceAll("_", " ") // Replace underscores with spaces
				.replaceAll("\\s+", " ") // Normalize spaces
				.trim().replaceFirst("^.", technicalName.substring(0, 1).toUpperCase()); // Capitalize first letter
	}
}
