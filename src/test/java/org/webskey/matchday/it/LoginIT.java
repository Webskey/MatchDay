package org.webskey.matchday.it;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.webskey.matchday.pageobjects.BrowserDriver;
import org.webskey.matchday.pageobjects.LoginPage;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;

public class LoginIT {

	private WebDriver webDriver;
	private LoginPage loginPage;

	@Before("@login")
	public void setUp() {
		webDriver = BrowserDriver.INSTANCE.getWebDriver();
	}

	@After("@login")
	public void tearDown() {
		webDriver.manage().deleteAllCookies();
	}

	@Given("I am on Matchday login page")
	public void i_am_on_Matchday_login_page() {
		loginPage = new LoginPage(webDriver);
		loginPage.goToLoginPage();
	}

	@When("I fill username field with")
	public void i_fill_username_field_with(DataTable dataTable) {
		List<String> usernames = dataTable.asList();

		for(String username : usernames) {
			loginPage.fillUsernameField(username);
		}
	}

	@When("I fill password field with")
	public void i_fill_password_field_with(DataTable dataTable) {
		List<String> passwords = dataTable.asList();

		for(String password : passwords) {
			loginPage.fillPasswordField(password);
		}
	}
	
	@When("I submit")
	public void i_submit() {
	    loginPage.submit();
	}

	@Then("I am logged in to Matchday")
	public void i_am_logged_in_to_Matchday() {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

	@Then("I am not logged in to Matchday")
	public void i_am_not_logged_in_to_Matchday() {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}
}
