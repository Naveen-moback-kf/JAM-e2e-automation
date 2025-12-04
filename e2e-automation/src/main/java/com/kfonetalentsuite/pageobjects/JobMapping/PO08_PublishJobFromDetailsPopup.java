package com.kfonetalentsuite.pageobjects.JobMapping;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import com.kfonetalentsuite.utils.JobMapping.PageObjectHelper;

public class PO08_PublishJobFromDetailsPopup extends BasePageObject {

	private static final Logger LOGGER = LogManager.getLogger(PO08_PublishJobFromDetailsPopup.class);

	public static ThreadLocal<String> job1OrgName = ThreadLocal.withInitial(() -> "NOT_SET");

	private static final By PUBLISH_PROFILE_BTN = By.xpath("//button[@id='publish-job-profile']");

	public PO08_PublishJobFromDetailsPopup() throws IOException {
		super();
	}

	public void user_is_on_profile_details_popup() {
		PageObjectHelper.log(LOGGER, "User is on Profile details Popup");
	}

	public void click_on_publish_profile_button_in_profile_details_popup() {
		try {
			clickElement(PUBLISH_PROFILE_BTN);
			PageObjectHelper.log(LOGGER, "Clicked Publish Profile button in Profile Details popup");
			waitForSpinners();
		} catch (Exception e) {
			PageObjectHelper.handleError(LOGGER, "click_on_publish_profile_button_in_profile_details_popup", "Failed to click Publish Profile button in popup", e);
		}
	}
}
