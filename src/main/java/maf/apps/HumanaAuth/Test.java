
package maf.apps.HumanaAuth;

import java.awt.AWTException;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.sikuli.api.robot.Key;
import org.sikuli.script.FindFailed;

import maf.apps.HumanaRetrieval.Humanaservice;
import maf.apps.util.ResultEnum;
import maf.apps.util.RootPath;
import maf.apps.util.Selenium_Actions_Enum;
import maf.apps.util.Selenium_Util;

public class Test {
	private static final Logger logger = LogManager.getLogger(Test.class.getName());

	public static void main(String args[])
			throws IOException, InterruptedException, FindFailed, ParseException, AWTException {

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
		o.setExperimentalOption("debuggerAddress", "localhost:55962");

		ChromeOptions options1 = new ChromeOptions();
		options1.setExperimentalOption("debuggerAddress", "127.0.0.1:9222");
		WebDriver driver = new ChromeDriver(options1);
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("memberId", "H7771916800");
		data.put("name", "Christine Cifaldi");
		data.put("dob", "09-26-1956");
		data.put("currentdate", "11/14/2024");
		data.put("provider", "MISS1");
		data.put("code", "ST279");
		data.put("effectDate", "09-24-25");
		data.put("cptcodeFreq", "1x6");
		data.put("pdfFilename", "C:\\Users\\UmeshK\\Desktop\\Practice\\MAF_WL-main\\maf\\resources\\ViewOutput46.pdf");
		data.put("serviceType", "Physical Therapy");
		data.put("caseStartDate", "09-23-25");
		data.put("caseNumber", "PT004");
		data.put("reqVisits", "12");
		data.put("pin","0289091");
		data.put("fullname", "freddie jones");
		List<Map<String, String>> auth = new ArrayList<Map<String, String>>();
		Map<String, String> obj = new HashMap<String, String>();
		obj.put("Auth", "2848196");
		Map<String, String> obj2 = new HashMap<String, String>();
		obj2.put("Auth", "3033427");
		auth.add(obj);
		auth.add(obj2);
		List<String> dx = new ArrayList<String>();
		dx.add("M54.50");
		dx.add("G91.2");
		String status = "initial";
		String serviceLine = "Physical Therapy";

		String cptcodeFreq = "3x6";
		List<List> procedure = Temp.getProcedures(status, serviceLine, cptcodeFreq);
		HumanaServices hm = new HumanaServices(driver);
//		Golden Bear Physical
//		hm.chooseState(driver1, data);

		// util.action(driver, senum.switchTab, "//*[@id='newBodyFrame']", "", 1,
		// "switching frame");
//		util.action(driver, senum.switchToIframe, "//*[@id='newBodyFrame']", "", 0, "switching frame");
//		 hm.login("tgreenwood3", "Butterfly@500");
		// hm.Auth(data, dx, procedure);
//		hm.submission(data, dx, procedure);
		driver.switchTo().defaultContent();
//		util.action(driver, senum.switchTab, "", "", 0, "switchTab  ");

		RTservices.certified(driver, data);
		util.action(driver, senum.switchToIframe, "//*[@id='content-iframe']//iframe", "", 1, "switching frame");

		testMethod(data, dx, procedure, driver);

//		hm.submit(data,dx);
//		hm.submit2(data,dx);

		Map<String, String> rt = new HashMap<String, String>();
		rt.put("authorized_from", "8/30/2024");
		rt.put("authorized_expires", "2/28/2025");
		rt.put("req", "12");
//		hm.submission3(data, dx,procedure);
//		RTservices.retrieval(driver,rt);
		FlowInitial initial = new FlowInitial(driver);
//		FlowContinuation continuation = new FlowContinuation(driver);
		rt.put("date", "2024-10-25 - 2025-01-25");
		rt.put("auth", "200613080");
//		hm.login();

	}

	static Selenium_Actions_Enum senum;
	static Selenium_Util util;

	public static void testMethod(Map<String, String> data, List<String> dx, List<List> procedures, WebDriver driver)
			throws InterruptedException {

		String folderPath = RootPath.BASE_DIR + "\\resources\\pdfDownload";
		File folder = new File(folderPath);
		File[] pdfFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));
		File pdfFiles2 = Arrays.stream(pdfFiles).max(Comparator.comparingLong(File::lastModified)).orElse(null);

		String filename = pdfFiles2.getAbsolutePath();

		Map<String, String> cpts = new HashMap<String, String>();
		for (int x = 0; x < procedures.get(1).size(); x++) {
			cpts.put(procedures.get(0).get(x).toString(), procedures.get(1).get(x).toString());

		}
		System.out.println(cpts);
		List procedureDetails = (List) util.action(driver, senum.getWebElements,
				"//*[@class='BlockProcedures card']//*[@class=\"tile result\"]", "", 0,
				"getting count for require cpt codes");
		System.out.println(procedureDetails.size());
		for (int x = 0; x < procedureDetails.size(); x++) {
			String cptcodeX = "(//*[@class='BlockProcedures card']//*[@class='tile-header'])[" + (x + 1) + "]";
			String cptCode = (String) util.action(driver, senum.getText, cptcodeX, "", 0, "getting code");
			cptCode = cptCode.substring(0, 5);
			String dxX = "(//*[contains(text(),'Diagnosis Code')])[2]/parent::*/div";

			util.action(driver, senum.sendTextById, "procedures[" + x + "]-quantity", cpts.get(cptCode) + "", 0,
					"sending the to date");

			Thread.sleep(1000);

		}

	}
}
