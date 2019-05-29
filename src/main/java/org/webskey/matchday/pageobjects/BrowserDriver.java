package org.webskey.matchday.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public enum BrowserDriver {

	INSTANCE;

	private final WebDriver driver;

	private BrowserDriver(){
		System.setProperty("webdriver.gecko.driver", "c:/SeleniumGecko/geckodriver.exe");
		this.driver = new FirefoxDriver();
	}

	public WebDriver getWebDriver() {
		return driver;
	}
}
