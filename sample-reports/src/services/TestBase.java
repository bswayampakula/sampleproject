package services;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

public class TestBase {
	protected WebDriver driver;

	public WebDriver getDriver() {
		return driver;
	}

	@Parameters("browser")
	@BeforeTest()
	public synchronized void createDriver(String browser) {
		switch (browser) {
		case "firefox":
			driver = new FirefoxDriver();
			break;
		case "safari":
			driver = new SafariDriver();
			break;
		case "chrome":
			System.setProperty("webdriver.chrome.driver",
					System.getProperty("user.dir") + "//driver//chromedriver");
			driver = new ChromeDriver();
			break;
		default:
			break;
		}
	}
}
