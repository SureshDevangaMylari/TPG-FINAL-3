package maf.apps.util;

import java.io.IOException;
import java.util.Map;

import org.openqa.selenium.WebDriver;

import base.DriverManager;
import maf.apps.master.selenium.elementspath.RaintreeLocator;

public class RTService {

	
	
	public static void login(WebDriver driver,Map<String,String> creds) throws IOException, InterruptedException
	{
		
		String username = DriverManager.getpropertyvalue(creds.get("userName"));
		String password = DriverManager.getpropertyvalue(creds.get("pass"));
		System.out.println(" pass "+password);
		driver.get(DriverManager.getpropertyvalue("TPGurl"));
      
		Selenium_Util.action(driver, Selenium_Actions_Enum.sendText,  RaintreeLocator.XuserName, username,0, "passing TPG username");
		
	 
		Selenium_Util.action(driver, Selenium_Actions_Enum.sendText,  RaintreeLocator.Xpassword, password, 0,"passing TPG password");
		
		Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement,  RaintreeLocator.XloginBtn, null, 0, "clicking login button");
		
		Thread.sleep(3000);
		if((Selenium_Util.action(driver, Selenium_Actions_Enum.isElementDisplayed,  RaintreeLocator.Xacknowledge, null,0, "checking acknowledge button is present or not ")=="true"))
		{
			Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement,  RaintreeLocator.Xacknowledge, null, 0, "clicking acknowledge button");
			Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoadandClickElement,  RaintreeLocator.XacknowledgeWarning, null, 0, "clicking the acknowledge warning ok button");
			Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoadandSendText,  RaintreeLocator.Xuserid, username,   0,"Entering the tpg username in userid field");
			Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement,  RaintreeLocator.Xacknowledge, null, 0, "Again clicking the acknowledge button");
		}
		
		
		
	}
}
