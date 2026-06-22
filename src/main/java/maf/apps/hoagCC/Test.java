package maf.apps.hoagCC;

import java.awt.AWTException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.sikuli.script.FindFailed;

import maf.apps.util.RootPath;

public class Test {

	public static void main(String args[]) throws IOException, InterruptedException, FindFailed, ParseException, AWTException {

		System.setProperty("webdriver.chrome.driver", RootPath.BASE_DIR + "\\chromedriver.exe");
		String downloadFilepath = RootPath.BASE_DIR + "\\resources\\pdfDownload";
		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("download.default_directory", downloadFilepath);
		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("prefs", chromePrefs);
		DesiredCapabilities cap = DesiredCapabilities.chrome();
		cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		cap.setCapability(ChromeOptions.CAPABILITY, options);
		ChromeOptions o = new ChromeOptions();
		// setting debuggerAddress value
		o.setExperimentalOption("debuggerAddress", "localhost:64936");
		WebDriver driver = new ChromeDriver(o);

		HashMap<String, String> data = new HashMap<String, String>();
		data.put("name", "GEORGE Freundner");
		data.put("dob", "10/13/1954");
		data.put("provider", "MISS1");
		data.put("cptcodeFreq", "1x6");
		data.put("pdfFilename", "C:\\Users\\UmeshK\\Desktop\\Practice\\MAF_WL-main\\maf\\resources\\ViewOutput46.pdf");

		List<Map<String, String>> auth = new ArrayList<Map<String, String>>();
		Map<String, String> obj = new HashMap<String, String>();
		obj.put("Auth", "2848196");
		Map<String, String> obj2 = new HashMap<String, String>();
		obj2.put("Auth", "3033427");
		auth.add(obj);
		auth.add(obj2);
		List<String> dx = new ArrayList<String>();
		dx.add("Z98.1");
//		dx.add("M54.50");

		HoagServices.login(driver);
		HoagServices.PreAuth(driver, data,auth,dx);
//		HoagServices.test(driver, data, auth,dx);
//		HoagServices.submit(driver, dx,data);
		HoagServices.getRefDetails(driver);
	
		
		Map<String, Object> rt = new HashMap<String, Object>();
		rt.put("authorized_from", "8/30/2024");
		rt.put("authorized_expires", "2/28/2025");
		
//		RTservices.retrieval(driver,rt);
		
			
	}
}
