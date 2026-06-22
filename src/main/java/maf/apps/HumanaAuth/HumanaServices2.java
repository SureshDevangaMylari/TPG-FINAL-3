package maf.apps.HumanaAuth;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.sikuli.api.robot.Key;
import org.sikuli.api.robot.desktop.DesktopKeyboard;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Screen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import maf.apps.base.DriverManager;
import maf.apps.master.selenium.elementspath.Hoag;
import maf.apps.master.selenium.elementspath.Humana;
import maf.apps.util.FileUtil;
import maf.apps.util.ResultEnum;
import maf.apps.util.RootPath;
import maf.apps.util.Selenium_Actions_Enum;
import maf.apps.util.Selenium_Util;

public class HumanaServices2 {
	static Selenium_Actions_Enum senum;
	static Selenium_Util util;
	static Humana xpath;
	private static final Logger logger = LogManager.getLogger(HumanaServices2.class.getName());
	public static String auth = "";
	public static WebDriver driver;

	public HumanaServices2(WebDriver driver) {
		super();
		this.driver = driver;
	}

	public static void login() throws IOException, InterruptedException {

		String username = DriverManager.getpropertyvalue("humanaUser");
		String pass = DriverManager.getpropertyvalue("humanaPass");

		driver.get("https://apps.availity.com/web/onboarding/availity-fr-ui/?p:lm=1714010575#/login");

		util.action(driver, senum.sendText, xpath.username, username, 0, "entering login name");
		util.action(driver, senum.sendText, xpath.password, pass, 0, "entering login password");

		util.action(driver, senum.clickElement, xpath.submit, "", 0, "clicking login button");

		// newBodyFrame

	}

	public static String Auth(Map<String, String> data, List<String> dx, List<List> procedures) throws InterruptedException {
		Screen screen = new Screen();
		screen.click();
		logger.info(" data " + data);
		util.action(driver, senum.switchToIframe, "//*[@id='newBodyFrame']", "", 0, "switching frame");

		util.action(driver, senum.clickElement, xpath.authorization_ref, "", 0, "clicking authorization");

		util.action(driver, senum.clickElement, xpath.authReq, "", 0, "clicking authorization req");

		DesktopKeyboard keyboard = new DesktopKeyboard();

		util.action(driver, senum.clickElementById, xpath.Iorganization, "", 0, "clicking Iorganization");
//
		Thread.sleep(1000);
		keyboard.type("Golden Bear Physical");
		keyboard.keyDown(Key.ENTER);
		keyboard.keyUp(Key.ENTER);

//		util.action(driver, senum.sendText, "(//*[@class='row'])[1]//input", "Golden Bear Physical", 0, "sending golden bear");
//		Thread.sleep(1500);
//		keyboard.keyDown(Key.ENTER);
//		keyboard.keyUp(Key.ENTER);

		Thread.sleep(1000);
		util.action(driver, senum.sendText, "(//*[@class='row'])[2]//input", "HUMANA", 0, "sending payer");
		Thread.sleep(1500);
		keyboard.keyDown(Key.ENTER);
		keyboard.keyUp(Key.ENTER);
		Thread.sleep(1000);
		util.action(driver, senum.clickElement, " //*[contains(text(),'Select Authorization Type')]", "", 0, "clicking req type");
		Thread.sleep(1000);

		util.action(driver, senum.clickElement, "//div[contains(text(),'Outpatient Authorization/Referral')]", "", 0, "clicking req type");

		Thread.sleep(1000);
		util.action(driver, senum.sendTextById, xpath.ImemId, data.get("memberId"), 0, "sending member id");
		util.action(driver, senum.sendTextById, xpath.Idob, data.get("dob"), 0, "sending dob");
		Thread.sleep(1000);
		keyboard.keyDown(Key.ENTER);
		keyboard.keyUp(Key.ENTER);
		logger.info(" curr date" + data.get("currentdate"));
		Thread.sleep(1000);
		util.action(driver, senum.sendTextById, xpath.Ifrom, data.get("currentdate"), 0, "sending current date");
		Thread.sleep(1000);
		keyboard.keyDown(Key.ENTER);
		keyboard.keyUp(Key.ENTER);
		util.action(driver, senum.clickElement, "(//*[contains(text(),'Procedure Code')])[2]/parent::*", data.get("currentDate"), 0, "sending current date");
		Thread.sleep(1000);

		String[] cpts = new String[] { "97161", "97162", "97163", "97530", "97110", "97140", "97116", "97010", "97112" };

		keyboard.paste(cpts[0]);
		Thread.sleep(1000);
		keyboard.keyDown(Key.ENTER);
		keyboard.keyUp(Key.ENTER);

		util.action(driver, senum.sendTextById, xpath.Inpi, "1134758709", 0, "sending npi");
		util.action(driver, senum.sendTextById, xpath.Itaxid, "850572937", 0, "sending tax id");
		util.action(driver, senum.clickElementById, xpath.Inext, "", 0, "clicking next button");
		util.action(driver, senum.waitUntilLoad, "//*[text()='Status']", "", 0, "waiting for the status ");

		Thread.sleep(1000);
		String status = (String) util.action(driver, senum.getText, xpath.Xstatus, "", 0, "getting status");

		if (status.contains("AUTH/REFERRAL REQUIRED")) {
			logger.info("auth is required ");
			submission(data, dx, procedures);
		} else {
			logger.info("auth is not required ");
			util.action(driver, senum.clickElementById, xpath.IprintPage, "", 0, "printing page...");

			return ResultEnum.AuthIsnotRequired.name();
		}
		return status;

	}

	public static Map<String, Object> refdetails = new HashMap<String, Object>();

	public static String submit(Map<String, String> data, List<String> dx) throws InterruptedException {

		driver.switchTo().defaultContent();
		util.action(driver, senum.switchToIframe, "//*[@id='newBodyFrame']", "", 0, "switching frame");

		DesktopKeyboard keyboard = new DesktopKeyboard();

//		util.action(driver, senum.clickElementById, xpath.Iorganization, "", 0, "clicking Iorganization");
//
//		Thread.sleep(1000);
//		keyboard.type("Golden Bear Physical");
//		keyboard.keyDown(Key.ENTER);
//		keyboard.keyUp(Key.ENTER);

		util.action(driver, senum.sendText, "(//*[@class='row'])[1]//input", "Golden Bear Physical Therapy", 0, "sending golden bear");
		Thread.sleep(1500);
		keyboard.keyDown(Key.ENTER);
		keyboard.keyUp(Key.ENTER);
		util.action(driver, senum.sendText, "(//*[@class='row'])[2]//input", "HUMANA", 0, "sending payer");
		Thread.sleep(1500);
		keyboard.keyDown(Key.ENTER);
		keyboard.keyUp(Key.ENTER);
		Thread.sleep(1000);
		util.action(driver, senum.clickElement, " //*[contains(text(),'Select Authorization Type')]", "", 0, "clicking req type");
		Thread.sleep(1000);

		util.action(driver, senum.clickElement, "//div[contains(text(),'Outpatient Authorization/Referral')]", "", 0, "clicking req type");

		Thread.sleep(1000);
//		util.action(driver, senum.clickElement, xpath.Inext, "", 0, "clicking next button");

		return null;

	}

	public static String submit2(Map<String, String> data, List<String> dx) throws InterruptedException {
		DesktopKeyboard keyboard = new DesktopKeyboard();
		logger.info(" data " + data);
		util.action(driver, senum.switchToIframe, "//*[@id='newBodyFrame']", "", 0, "switching frame");
		util.action(driver, senum.sendTextById, xpath.ImemId, data.get("memberId"), 0, "sending member id");
		util.action(driver, senum.sendTextById, xpath.Idob, data.get("dob"), 0, "sending dob");
		Thread.sleep(1000);
		keyboard.keyDown(Key.ENTER);
		keyboard.keyUp(Key.ENTER);
		util.action(driver, senum.sendTextById, xpath.Ifrom, data.get("currentDate"), 0, "sending current date");
		Thread.sleep(1000);
		keyboard.keyDown(Key.ENTER);
		keyboard.keyUp(Key.ENTER);
		util.action(driver, senum.clickElement, "(//*[contains(text(),'Procedure Code')])[2]/parent::*", data.get("currentDate"), 0, "sending current date");
		Thread.sleep(1000);

		String[] cpts = new String[] { "97161", "97162", "97163", "97530", "97110", "97140", "97116", "97010", "97112" };

		keyboard.paste(cpts[0]);
		Thread.sleep(1000);
		keyboard.keyDown(Key.ENTER);
		keyboard.keyUp(Key.ENTER);

		util.action(driver, senum.sendTextById, xpath.Inpi, "1134758709", 0, "sending npi");
		util.action(driver, senum.sendTextById, xpath.Itaxid, "850572937", 0, "sending tax id");
		util.action(driver, senum.clickElementById, xpath.Inext, "", 0, "clicking next button");
//		
		return auth;

	}

	public static String submit3(Map<String, String> data, List<String> dx) throws InterruptedException {
		DesktopKeyboard keyboard = new DesktopKeyboard();
		util.action(driver, senum.switchToIframe, "//*[@id='newBodyFrame']", "", 0, "switching frame");

		util.action(driver, senum.waitUntilLoad, "//*[text()='Status']", "", 0, "waiting for the status ");

		Thread.sleep(1000);
		String status = (String) util.action(driver, senum.getText, xpath.Xstatus, "", 0, "getting status");

		if (status.equals("AUTH/REFERRAL REQUIRED")) {
			logger.info("auth is required ");
//			s?ubmission(data, dx);
		} else {
			logger.info("auth is not required ");
			return ResultEnum.AuthIsnotRequired.name();
		}

		return auth;

	}

	public static String submission(Map<String, String> data, List<String> dx, List<List> procedures) throws InterruptedException {
		Screen sc = new Screen();
		sc.click();
		DesktopKeyboard keyboard = new DesktopKeyboard();

		util.action(driver, senum.clickElementById, xpath.Icontinue, "", 0, "clicking continue button");
//		util.action(driver, senum.switchToIframe, "//*[@id='newBodyFrame']", "", 0, "switching frame");

		util.action(driver, senum.clickElement, xpath.Xselectprovider, "", 0, "selecting provider");
		Thread.sleep(1500);
		keyboard.paste("smith");
		Thread.sleep(2000);
		keyboard.keyDown(Key.ENTER);
		keyboard.keyUp(Key.ENTER);
		util.action(driver, senum.sendTextById, xpath.Icontactname, "Monique Vizcaino", 0, "sending contact name");
		util.action(driver, senum.sendTextById, xpath.Iphone, "209-343-3187", 0, "sending phone nbr");
		util.action(driver, senum.sendTextById, xpath.Ifax, "725-726-7876", 0, "sending fax");
		util.action(driver, senum.clickElementById, xpath.Inext, "", 0, "clicking next button");
		util.action(driver, senum.clickElement, xpath.Xservicetype, "", 0, "clicking service type");
		Thread.sleep(1000);
		keyboard.paste(data.get("serviceType"));
		Thread.sleep(1000);
		keyboard.keyDown(Key.ENTER);
		keyboard.keyUp(Key.ENTER);

		util.action(driver, senum.clickElement, xpath.XplaceofSer, "", 0, "clicking place of service");
		Thread.sleep(1000);
		keyboard.paste("office");
		Thread.sleep(1000);
		keyboard.keyDown(Key.ENTER);
		keyboard.keyUp(Key.ENTER);

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 90);
		Date updatedDate = cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		String formattedDate = sdf.format(updatedDate);

		util.action(driver, senum.sendTextById, xpath.Itodate, formattedDate, 0, "sending todate");

		util.action(driver, senum.clickElement, xpath.Xpatientcondition, "", 0, "click patient condtion");

		keyboard.paste("patient is");
		Thread.sleep(1500);
		keyboard.keyDown(Key.ENTER);
		keyboard.keyUp(Key.ENTER);
		Thread.sleep(1000);

		for (int x = 0; x < dx.size(); x++) {

			String dxX = "(//*[contains(text(),'Diagnosis Code')])[2]/parent::*/div";

			if (x != 0) {

				util.action(driver, senum.clickElement, "(//*[@ng-click='dcCtrl.addDiagnosis()'])", formattedDate, 0, "click add diganosis");

				dxX = "(//*[contains(text(),'Diagnosis Code " + (x + 1) + "')])[2]/parent::*";

			}

			Thread.sleep(1000);
			util.action(driver, senum.clickElement, dxX, formattedDate, 0, "click  diganosis");
			Thread.sleep(1500);
			keyboard.paste(dx.get(x));
			Thread.sleep(1500);
			keyboard.keyDown(Key.ENTER);
			keyboard.keyUp(Key.ENTER);
			Thread.sleep(1000);
		}
		Thread.sleep(1000);
		String Xproc = xpath.Xprocedure;
		String Iqnty = "procedures.0.quantity";
		for (int x = 0; x < procedures.get(1).size(); x++) {
			Iqnty = "procedures." + x + ".quantity";
			if (x != 0) {
				util.action(driver, senum.clickElement, xpath.Xaddprocedures, "", 0, "clicking add procedures");

				Xproc = "(//*[contains(text(),'Procedure Code " + (x + 1) + "' )])[2]/parent::*";

			}
			util.action(driver, senum.clickElement, Xproc, "", 0, "clicking procedures");
			Thread.sleep(1500);
			keyboard.paste(procedures.get(0).get(x).toString());
			Thread.sleep(1500);
			keyboard.keyDown(Key.ENTER);
			keyboard.keyUp(Key.ENTER);
			Thread.sleep(1000);
			util.action(driver, senum.sendTextById, Iqnty, procedures.get(1).get(x).toString(), 0, "sending qty");

		}

		util.action(driver, senum.sendTextById, xpath.Iprovidernotes, "requesting auth for " + procedures.get(1).get(5).toString() + " visits", 0, "entering provider notes");
		util.action(driver, senum.clickElementById, xpath.Inext, "", 0, "clicking next button");

		search(xpath.XroleCode, "location", 1500, "choosing role code");

		util.action(driver, senum.sendTextById, xpath.Inpi2, "1134758709", 0, "entering npi");

		search(xpath.Xspeciality, "Physical Medicine & Rehabilitation", 1500, "entering speciality");

		util.action(driver, senum.sendTextById, xpath.Icity, "Las Vegas", 0, "entering city");
		search(xpath.Xstate, "Nevada", 1500, "entering state");
		util.action(driver, senum.sendTextById, xpath.Izipcode, "89052", 0, "entering zipcode");
		Thread.sleep(1000);
		util.action(driver, senum.clickElement, xpath.Xretrieveinfo, "", 0, "clicking retriving info");

		Thread.sleep(1500);

		String npi = "search.renderingProviders.facility.0.npi";
		String speciality = "(//*[@id='s2id_search.specialtyCode'])[2]";
		String city = "search.renderingProviders.facility.0.city";
		String state = "//*[@id='s2id_search.renderingProviders.facility.0.stateCode']";
		String zipcode = "search.renderingProviders.facility.0.zipCode";
		String retrive = "(//*[@value='Retrieve Provider Info'])[2]";

		util.action(driver, senum.sendTextById, npi, "1134758709", 0, "entering npi");

		search(speciality, "Physical Medicine & Rehabilitation", 1500, "entering speciality");

		util.action(driver, senum.sendTextById, city, "Las Vegas", 0, "entering city");
		search(state, "Nevada", 1500, "entering state");
		util.action(driver, senum.sendTextById, zipcode, "89052", 0, "entering zipcode");
		Thread.sleep(1000);
		util.action(driver, senum.clickElement, retrive, "", 0, "clicking retriving info");
		String innetwork = "(//table)[1]//tr[td//p[contains(text(),'In Network: Y')]]/td[4]/input";
		util.action(driver, senum.clickElement, innetwork, "", 0, "clicking innetowrk");
		Thread.sleep(2000);
		util.action(driver, senum.clickElement, innetwork, "", 0, "clicking innetowrk");

		Thread.sleep(1500);
		util.action(driver, senum.clickElementById, xpath.Inext, "", 0, "clicking next button");
		// serviceType
		return auth;

	}

	public static String submission2(Map<String, String> data, List<String> dx, List<List> procedures) throws InterruptedException {
		DesktopKeyboard keyboard = new DesktopKeyboard();
		util.action(driver, senum.switchToIframe, "//*[@id='newBodyFrame']", "", 0, "switching frame");
		logger.info("procedures " + procedures);
		String Xproc = xpath.Xprocedure;
		String Iqnty = "procedures.0.quantity";
		for (int x = 0; x < procedures.get(1).size(); x++) {
			Iqnty = "procedures." + x + ".quantity";
			if (x != 0) {
				util.action(driver, senum.clickElement, xpath.Xaddprocedures, "", 0, "clicking add procedures");

				Xproc = "(//*[contains(text(),'Procedure Code " + (x + 1) + "' )])[2]/parent::*";

			}
			util.action(driver, senum.clickElement, Xproc, "", 0, "clicking procedures");
			Thread.sleep(1500);
			keyboard.paste(procedures.get(0).get(x).toString());
			Thread.sleep(1500);
			keyboard.keyDown(Key.ENTER);
			keyboard.keyUp(Key.ENTER);
			Thread.sleep(1000);
			util.action(driver, senum.sendTextById, Iqnty, procedures.get(1).get(x).toString(), 0, "sending qty");

		}

		util.action(driver, senum.sendTextById, xpath.Iprovidernotes, "requesting auth for " + procedures.get(1).get(5).toString() + " visits", 0, "entering provider notes");
		util.action(driver, senum.clickElementById, xpath.Inext, "", 0, "clicking next button");

		return auth;

	}

	public static String submission3(Map<String, String> data, List<String> dx, List<List> procedures) throws InterruptedException {
		DesktopKeyboard keyboard = new DesktopKeyboard();
		util.action(driver, senum.switchToIframe, "//*[@id='newBodyFrame']", "", 0, "switching frame");
		Thread.sleep(1000);
		search(xpath.XroleCode, "location", 1500, "choosing role code");

		util.action(driver, senum.sendTextById, xpath.Inpi2, "1134758709", 0, "entering npi");

		search(xpath.Xspeciality, "Physical Medicine & Rehabilitation", 1500, "entering speciality");

		util.action(driver, senum.sendTextById, xpath.Icity, "Las Vegas", 0, "entering city");
		search(xpath.Xstate, "Nevada", 1500, "entering state");
		util.action(driver, senum.sendTextById, xpath.Izipcode, "89052", 0, "entering zipcode");
		Thread.sleep(1000);
		util.action(driver, senum.clickElement, xpath.Xretrieveinfo, "", 0, "clicking retriving info");

		Thread.sleep(1500);

		String npi = "search.renderingProviders.facility.0.npi";
		String speciality = "(//*[@id='s2id_search.specialtyCode'])[2]";
		String city = "search.renderingProviders.facility.0.city";
		String state = "//*[@id='s2id_search.renderingProviders.facility.0.stateCode']";
		String zipcode = "search.renderingProviders.facility.0.zipCode";
		String retrive = "(//*[@value='Retrieve Provider Info'])[2]";

		util.action(driver, senum.sendTextById, npi, "1134758709", 0, "entering npi");

		search(speciality, "Physical Medicine & Rehabilitation", 1500, "entering speciality");

		util.action(driver, senum.sendTextById, city, "Las Vegas", 0, "entering city");
		search(state, "Nevada", 1500, "entering state");
		util.action(driver, senum.sendTextById, zipcode, "89052", 0, "entering zipcode");
		Thread.sleep(1000);
		util.action(driver, senum.clickElement, retrive, "", 0, "clicking retriving info");
		String innetwork = "(//table)[1]//tr[td//p[contains(text(),'In Network: Y')]]/td[4]/input";
		util.action(driver, senum.clickElement, innetwork, "", 0, "clicking innetowrk");

		util.action(driver, senum.clickElement, innetwork, "", 0, "clicking innetowrk");
		return auth;

	}

	public static void search(String xpath, String text, int time, String desc) throws InterruptedException {

		DesktopKeyboard keyboard = new DesktopKeyboard();
		util.action(driver, senum.clickElement, xpath, "", 0, desc);
		Thread.sleep(time);
		keyboard.paste(text);
		Thread.sleep(time);
		keyboard.keyDown(Key.ENTER);
		keyboard.keyUp(Key.ENTER);

	}
}
