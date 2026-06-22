package maf.apps.util;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.By;

import java.sql.DriverManager;
import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.concurrent.TimeUnit;

public class ConnectBrwSession2 {
	public static void main(String[] args) throws InterruptedException {
		System.setProperty("webdriver.chrome.driver", base.DriverManager.BASE_DIR+"\\chromedriver.exe");

		ChromeOptions o = new ChromeOptions();
		o.setExperimentalOption("debuggerAddress", "localhost:57655");
		// add options to browser capabilities

		WebDriver driver = new ChromeDriver(o);
		Capabilities c = ((RemoteWebDriver) driver).getCapabilities();

		Map<String, Object> m = c.asMap();

		m.forEach((key, value) -> {
			System.out.println("Key is: " + key + " Value is: " + value);

		});
		driver.get("https://www.google.com/");
		// set implicit wait
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		// identify element

		// remove existing data in edit box

	}
}

//debuggerAddress=localhost:63742