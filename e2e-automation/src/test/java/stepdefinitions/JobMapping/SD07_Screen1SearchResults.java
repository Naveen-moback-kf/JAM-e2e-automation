package stepdefinitions.JobMapping;

import java.io.IOException;

import com.kfonetalentsuite.manager.PageObjectManager;

import io.cucumber.java.en.Then;

public class SD07_Screen1SearchResults {	
	public SD07_Screen1SearchResults() {
		super();		
	}
	
	@Then("User should scroll down to view last search result")
	public void user_should_scroll_down_to_view_last_search_result() throws IOException, InterruptedException {
		PageObjectManager.getInstance().getScreen1SearchResults().user_should_scroll_down_to_view_last_search_result();
	}

	@Then("User should validate all search results contains substring used for searching")
	public void user_should_validate_all_search_results_contains_substring_used_for_searching() throws IOException {
		PageObjectManager.getInstance().getScreen1SearchResults().user_should_validate_all_search_results_contains_substring_used_for_searching();
	}

}

