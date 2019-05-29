package org.webskey.matchday.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

	private WebDriver webDriver;
	By usernameField = By.name("username");
	By passwordField = By.name("password");
	By loginButton = By.id("login-submit-button");

	public LoginPage(WebDriver webDriver){
		super(webDriver);
		this.webDriver = webDriver;
	}

	public void goToLoginPage() {
		goTo("https://webskey-matchday.herokuapp.com/login");
	}

	public void fillUsernameField(String username) {
		webDriver.findElement(usernameField).sendKeys(username);
	}

	public void fillPasswordField(String password) {
		webDriver.findElement(passwordField).sendKeys(password);
	}

	public void submit() {
		webDriver.findElement(loginButton).click();
	}
}
