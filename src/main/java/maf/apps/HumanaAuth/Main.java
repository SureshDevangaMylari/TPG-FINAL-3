package maf.apps.HumanaAuth;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import maf.apps.base.DriverManager;
import maf.apps.master.selenium.elementspath.RaintreeLocator;
import maf.apps.util.RTService;
import maf.apps.util.Selenium_Actions_Enum;
import maf.apps.util.Selenium_Util;

public class Main implements Runnable {

	static WebDriver driver;
	List<Map<String, String>> exceldata;

	public Main(WebDriver d) {
		driver = d;

	}

	static Selenium_Actions_Enum senum;
	static boolean isInitial = true;

	@Override
	public void run() {
		// TODO Auto-generated method stub

		Map<String, String> creds = new HashMap<>();
		creds.put("userName", "RThumanauser");
		creds.put("pass", "RThumamapass");
		String type = "";
		try {
			type = DriverManager.getpropertyvalue("type");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
//			driver.manage().window().maximize();

			RTService.login(driver, creds);
			Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoadandClickElement, RaintreeLocator.Xdashboard,
					null, 0, "clicking the Dashboard menu");
			Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoadandClickElement, RaintreeLocator.Xauth,
					null, 0, "clicking the auth icon");
			HumanaServices hm = new HumanaServices(driver);
			((JavascriptExecutor) driver).executeScript("window.open();");
			Thread.sleep(2000);
			Selenium_Util.action(driver, senum.switchTab, "", "", 1, "switching the tabs to 1");
			String username = DriverManager.getpropertyvalue("humanaUser");
			String pass = DriverManager.getpropertyvalue("humanaPass");
			Thread.sleep(1000);
			hm.login(username, pass);
			Selenium_Util.action(driver, senum.switchTab, "", "", 0, "switching the tabs to 1");
			Thread.sleep(1000);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			if (type.equals("all")) {
				FlowInitial initial = new FlowInitial(driver);
				FlowContinuation continuation = new FlowContinuation(driver);
				initial.extractData();
				continuation.extractData();
			} else if (type.equals("initial")) {
				FlowInitial initial = new FlowInitial(driver);
				initial.extractData();
			} else {
				FlowContinuation continuation = new FlowContinuation(driver);
				continuation.extractData();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
