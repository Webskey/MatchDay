package org.webskey.matchday.pageobjects;

import org.openqa.selenium.WebDriver;

public class BasePage {

	private WebDriver driver;

	public BasePage(WebDriver driver) {
		this.driver = driver;
	}

	public void goTo(String adress) {
		driver.get(adress);
	}
}
