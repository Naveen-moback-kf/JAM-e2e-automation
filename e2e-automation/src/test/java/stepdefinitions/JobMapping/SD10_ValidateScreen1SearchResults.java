package stepdefinitions.JobMapping;

import java.io.IOException;

import com.kfonetalentsuite.manager.PageObjectManager;

import io.cucumber.java.en.Then;

public class SD10_ValidateScreen1SearchResults {
	PageObjectManager validateScreen1SearchResults = new PageObjectManager();
	
	public SD10_ValidateScreen1SearchResults() {
		super();		
	}
	
	@Then("User should scroll down to view last search result")
	public void user_should_scroll_down_to_view_last_search_result() throws IOException, InterruptedException {
		validateScreen1SearchResults.getValidateScreen1SearchResults().user_should_scroll_down_to_view_last_search_result();
	}

	@Then("User should validate all search results contains substring used for searching")
	public void user_should_validate_all_search_results_contains_substring_used_for_searching() throws IOException {
		validateScreen1SearchResults.getValidateScreen1SearchResults().user_should_validate_all_search_results_contains_substring_used_for_searching();
	}

}
