package maf.apps.util;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.By;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ConnectBrwSession {
	public static void main(String[] args) throws InterruptedException {
		System.setProperty("webdriver.chrome.driver", RootPath.BASE_DIR + "\\chromedriver.exe");
		ChromeOptions o = new ChromeOptions();
		// setting debuggerAddress value
		// o.setExperimentalOption("debuggerAddress", "localhost:64205");
		WebDriver driver = new ChromeDriver();
		// get browser capabilities in key value pairs
		Capabilities c = ((RemoteWebDriver) driver).getCapabilities();
		Map<String, Object> m = c.asMap();

		m.forEach((key, value) -> {
			// System.out.println("Key is: " + key + " Value is: " + value);
		});
//		System.out.println(m);
		System.out.println(m.get("goog:chromeOptions").toString().split(":")[1].replace("}", ""));
// set implicit wait
//		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}
}

//debuggerAddress=localhost:64851