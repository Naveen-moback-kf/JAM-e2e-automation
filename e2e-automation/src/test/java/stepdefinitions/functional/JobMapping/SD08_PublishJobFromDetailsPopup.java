package stepdefinitions.functional.JobMapping;

import java.io.IOException;

import com.JobMapping.manager.PageObjectManager;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SD08_PublishJobFromDetailsPopup {
	
	PageObjectManager publishJobFromDetailsPopup = new PageObjectManager();
	
	public SD08_PublishJobFromDetailsPopup() {
		super();		
	}

	@When("User is on profile details popup")
	public void user_is_on_profile_details_popup() throws IOException {
		publishJobFromDetailsPopup.getPublishJobFromDetailsPopup().user_is_on_profile_details_popup();  
	}

	@Then("Click on Publish Profile button in profile details popup")
	public void click_on_publish_profile_button_in_profile_details_popup() throws IOException {
		publishJobFromDetailsPopup.getPublishJobFromDetailsPopup().click_on_publish_profile_button_in_profile_details_popup();
	}
}
