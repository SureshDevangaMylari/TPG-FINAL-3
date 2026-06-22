package maf.apps.HumanaAuth;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
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
import maf.apps.util.SikuliUtility;

public class HumanaServices3 {
	static Selenium_Actions_Enum senum;
	static Selenium_Util util;
	static Humana xpath;
	private static final Logger logger = LogManager.getLogger(HumanaServices3.class.getName());
	public static String auth = "";
	public static WebDriver driver;

	public HumanaServices3(WebDriver driver) {
		super();
		this.driver = driver;
	}

	public static String authnbr = "";
	public static Map<String, Object> rtObj = new HashMap<String, Object>();

	public static void login(String username, String pass) throws IOException, InterruptedException {
		logger.info(" humana login");

		driver.get("https://apps.availity.com/web/onboarding/availity-fr-ui/?p:lm=1714010575#/login");

		util.action(driver, senum.sendText, xpath.username, username, 0, "entering login name");
		util.action(driver, senum.sendText, xpath.password, pass, 0, "entering login password");

		util.action(driver, senum.clickElement, xpath.submit, "", 0, "clicking login button");

		// newBodyFrame

	}

	public static String Auth(Map<String, String> data, List<String> dx, List<List> procedures)
			throws InterruptedException, ParseException, FindFailed {
		Screen screen = new Screen();
		screen.click();
		rtObj = new HashMap<String, Object>();
		auth = "";
		logger.info(" data " + data);
		driver.switchTo().defaultContent();

		util.action(driver, senum.clickElementById, "home-link", "", 0, "clicking home");

		// <button id="onetrust-accept-btn-handler"
		// class="__web-inspector-hide-shortcut__">Accept All Cookies</button>

		util.action(driver, senum.switchToIframe, "//*[@id='newBodyFrame']", "", 0, "switching frame");

		util.action(driver, senum.clickElement, xpath.authorization_ref, "", 0, "clicking authorization");

		util.action(driver, senum.clickElement, xpath.authReq, "", 0, "clicking authorization req");

		DesktopKeyboard keyboard = new DesktopKeyboard();

		util.action(driver, senum.clickElementById, xpath.Iorganization, "", 0, "clicking Iorganization");
//
		Thread.sleep(1000);
		if (data.get("code").contains("ST")) {

			keyboard.type("smith therapy");
			keyboard.keyDown(Key.ENTER);
			keyboard.keyUp(Key.ENTER);

		} else if (data.get("code").contains("FIT")) {
			keyboard.type("fit physical");
			keyboard.keyDown(Key.ENTER);
			keyboard.keyUp(Key.ENTER);
		}

//		util.action(driver, senum.sendText, "(//*[@class='row'])[1]//input", "Golden Bear Physical", 0, "sending golden bear");
//		Thread.sleep(1500);
//		keyboard.keyDown(Key.ENTER);
//		keyboard.keyUp(Key.ENTER);

		Thread.sleep(1000);

		util.action(driver, senum.clickElementById, "s2id_payer", "HUMANA", 0, " clicking human tag");
		util.action(driver, senum.sendTextById, "s2id_autogen2_search", "HUMANA", 0, "sending payer");
		Thread.sleep(1500);
		keyboard.keyDown(Key.ENTER);
		keyboard.keyUp(Key.ENTER);
		Thread.sleep(1000);
		util.action(driver, senum.clickElement, " //*[contains(text(),'Select Authorization Type')]", "", 0,
				"clicking req type");
		Thread.sleep(1000);

		util.action(driver, senum.clickElement, "//div[contains(text(),'Outpatient Authorization/Referral')]", "", 0,
				"clicking req type");

		Thread.sleep(1000);

		util.action(driver, senum.sendTextById, "serviceTypeCode-input", data.get("serviceType"), 0,
				"sending service type");

		util.action(driver, senum.clickElement, "//*[contains(text(),'" + data.get("serviceType") + "')]", "", 0,
				"clicking the servie type");

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
		util.action(driver, senum.clickElement, "(//*[contains(text(),'Procedure Code')])[2]/parent::*",
				data.get("currentDate"), 0, "sending current date");
		Thread.sleep(1000);

		String[] cpts = new String[] { "97161", "97162", "97163", "97530", "97110", "97140", "97116", "97010",
				"97112" };

		keyboard.paste("97530");
		Thread.sleep(2000);
		keyboard.keyDown(Key.ENTER);
		keyboard.keyUp(Key.ENTER);
		Thread.sleep(1000);
		util.action(driver, senum.sendTextById, xpath.Inpi, "1134758709", 0, "sending npi");
		util.action(driver, senum.sendTextById, xpath.Itaxid, "850572937", 0, "sending tax id");
		util.action(driver, senum.clickElementById, xpath.Inext, "", 0, "clicking next button");

		while (true) {
			try {
				driver.findElement(
						By.xpath("//*[text()='Patient Not Found or No Coverage for given Date of Service.']")).click();

				return ResultEnum.NoRecordsFound.name();

			} catch (Exception e) {

			}

			try {
				driver.findElement(By.xpath("//*[text()='Status']"));
				break;
			} catch (Exception e) {

			}
			// *[text()='Patient Not Found or No Coverage for given Date of Service.']
		}

		util.action(driver, senum.waitUntilLoad, "//*[text()='Status']", "", 0, "waiting for the status ");

		Thread.sleep(1000);
		String status = (String) util.action(driver, senum.getText, xpath.Xstatus, "", 0, "getting status");

		if (status.equals("AUTH/REFERRAL REQUIRED")) {
			logger.info("auth is required ");
			return submission(data, dx, procedures);
		} else if (status.equals("SEE MESSAGE")) {

			logger.info("see message ");

			util.action(driver, senum.clickElementById, xpath.IprintPage, "", 0, "printing page...");

			String message = (String) util.action(driver, senum.getText, "//*[text()='Message']/parent::*", "", 0,
					"getting status");

//			return submission(data, dx, procedures);

//			
//			String text=(String) util.action(driver, senum.getText, "//*[text()='Message']/parent::*", "", 0,
//					"getting message");

			// *[text()='Message']/following-sibling::*
			return message;
		}

		else {
			logger.info("auth is not required ");

			util.action(driver, senum.clickElementById, xpath.IprintPage, "", 0, "printing page...");

			return ResultEnum.AuthIsnotRequired.name();
		}

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

		util.action(driver, senum.sendText, "(//*[@class='row'])[1]//input", "Golden Bear Physical Therapy", 0,
				"sending golden bear");
		Thread.sleep(1500);
		keyboard.keyDown(Key.ENTER);
		keyboard.keyUp(Key.ENTER);
		util.action(driver, senum.sendText, "(//*[@class='row'])[2]//input", "HUMANA", 0, "sending payer");
		Thread.sleep(1500);
		keyboard.keyDown(Key.ENTER);
		keyboard.keyUp(Key.ENTER);
		Thread.sleep(1000);
		util.action(driver, senum.clickElement, " //*[contains(text(),'Select Authorization Type')]", "", 0,
				"clicking req type");
		Thread.sleep(1000);

		util.action(driver, senum.clickElement, "//div[contains(text(),'Outpatient Authorization/Referral')]", "", 0,
				"clicking req type");

		Thread.sleep(1000);
//		util.action(driver, senum.clickElement, xpath.Inext, "", 0, "clicking next button");

		return null;

	}

	public static String submission(Map<String, String> data, List<String> dx, List<List> procedures)
			throws InterruptedException, ParseException, FindFailed {
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
		String effDate = "";
		String toDate = "";
		if (data.get("effectDate") != null) {

			DateTimeFormatter inputFormatter = new DateTimeFormatterBuilder().appendPattern("MM-dd-")
					.appendValueReduced(ChronoField.YEAR, 2, 2, 2000).toFormatter();

			DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("M/d/yyyy");

			String effectD = data.get("effectDate");

			LocalDate dateDob = LocalDate.parse(effectD, inputFormatter);
			effDate = dateDob.format(outputFormatter);

			LocalDate dateto = dateDob.plusDays(90);
			toDate = dateto.format(outputFormatter);
			System.out.println(toDate);

			util.action(driver, senum.sendTextById, "fromDate", effDate, 0, "sending effDate");
			util.action(driver, senum.sendTextById, xpath.Itodate, toDate, 0, "sending todate");
		} else {
			util.action(driver, senum.sendTextById, xpath.Itodate, formattedDate, 0, "sending todate");

		}

		util.action(driver, senum.clickElement, xpath.Xpatientcondition, "", 0, "click patient condtion");

		keyboard.paste("patient is");
		Thread.sleep(1500);
		keyboard.keyDown(Key.ENTER);
		keyboard.keyUp(Key.ENTER);
		Thread.sleep(1000);

		for (int x = 0; x < dx.size(); x++) {

			String dxX = "(//*[contains(text(),'Diagnosis Code')])[2]/parent::*/div";

			if (x != 0) {

				util.action(driver, senum.clickElement, "(//*[@ng-click='dcCtrl.addDiagnosis()'])", formattedDate, 0,
						"click add diganosis");

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

		util.action(driver, senum.sendTextById, xpath.Iprovidernotes,
				"requesting auth for " + procedures.get(1).get(5).toString() + " visits", 0, "entering provider notes");
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
		util.action(driver, senum.waitUntilLoad, xpath.Xnextstep, "", 0, "waiting for the continu btn");

		Thread.sleep(1000);
		util.action(driver, senum.clickElement, xpath.Xnextstep, "", 0, "clicking continue");

		String status = "";
		Map<String, Object> rtData = new HashMap<String, Object>();
		Thread.sleep(1000);
		util.action(driver, senum.clickElementById, xpath.Inext, "", 0, "clicking submit button	");

		Thread.sleep(2000);
		util.action(driver, senum.waitUntilLoad, xpath.Xsubmissionstatus, "", 0, "waiting for the status");
		status = (String) util.action(driver, senum.getText, xpath.Xsubmissionstatus, "", 0, "getting status");
		Thread.sleep(3000);
		// Xsubmissionstatus
		rtObj.put("reqVisits", procedures.get(1).get(5).toString());
		if (status.contains("PENDED")) {
			logger.info(" pending");

//			RTservices.Pended(driver, rtData);
//			upload(driver, data);

			auth = (String) util.action(driver, senum.getText,
					"//*[contains(text(),'Reference Number')]/following-sibling::*", "", 0, "gettting auth number	");

			// *[contains(text(),'Add Clinical Documents')]
			rtObj.put("reqVisits", procedures.get(1).get(5).toString());

			return ResultEnum.PENDED.name();

		} else if (status.contains("NO ACTION REQUIRED")) {
			Thread.sleep(2000);

			return ResultEnum.NoAuthNeeded.name();
		} else

		{

			logger.info(" auto approval certified ");
			String d = (String) util.action(driver, senum.getText,
					"//*[text()='Service From - To Date']/following-sibling::*", "", 0, "gettting dates	");

			rtObj.put("reqVisits", procedures.get(1).get(5).toString());

			rtObj.put("fromDate", effDate);
			rtObj.put("toDate", toDate);
			util.action(driver, senum.clickElement, "//*[contains(text(),'Print')]", "", 0, "clicking print button	");
			Thread.sleep(2000);
			auth = (String) util.action(driver, senum.getText,
					"//*[contains(text(),'Certification Number')]/following-sibling::*", "", 0,
					"gettting auth number	");

			return ResultEnum.CERTIFIED.name();
//			RTservices.certified(driver, rtData);
		}
		// serviceType

	}

	public static String submission2(Map<String, String> data, List<String> dx, List<List> procedures)
			throws InterruptedException {
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

		util.action(driver, senum.sendTextById, xpath.Iprovidernotes,
				"requesting auth for " + procedures.get(1).get(5).toString() + " visits", 0, "entering provider notes");
		util.action(driver, senum.clickElementById, xpath.Inext, "", 0, "clicking next button");

		return auth;

	}

	public static String submission3(Map<String, String> data, List<String> dx, List<List> procedures)
			throws InterruptedException {
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

	public void print(WebDriver driver) throws InterruptedException {

		util.action(driver, senum.clickElementById, "printButton", "", 0, "clicking print button	");

	}

	public static void upload(WebDriver driver, Map<String, String> data) throws InterruptedException, FindFailed {
		Screen screen = new Screen();
		String folderPath = RootPath.BASE_DIR + "\\resources\\pdfDownload";
		File folder = new File(folderPath);
		File[] pdfFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));

		if (pdfFiles == null || pdfFiles.length == 0) {

		}
		String filename = pdfFiles[0].getAbsolutePath();
		System.out.println(pdfFiles[0].getAbsolutePath());

		util.action(driver, senum.clickElement, "//*[contains(text(),'Add Clinical Documents')]", "", 0,
				"clicking add clinic doc");

//		util.action(driver, senum.switchToIframe, "//*[@id='newBodyFrame']", "", 0, "switching frame");
		util.action(driver, senum.clickElement, "//*[text()='Add File']", "", 0, "clicking add file");

//		attachments-frame
		Thread.sleep(1000);
		SikuliUtility sikuli = new SikuliUtility();
		while (true) {
			try {
				if (screen.exists(RootPath.BASE_DIR + "//resources//sikuli//hpsj//Open.png") != null) {
					break;
				}
			} catch (Exception e) {

			}
		}

		Thread.sleep(1000);
		screen.paste(data.get("pdfFilename"));
		screen.click(RootPath.BASE_DIR + "//resources//sikuli//hpsj//Open.png");
		Thread.sleep(2000);
		while (true) {

			if (sikuli.checkElement(RootPath.BASE_DIR + "//resources//sikuli//humana//right.png")) {
				logger.info(" uploaded ");
				break;
			} else {
				logger.info(" not loaded");
			}

		}
		Thread.sleep(2000);
		util.action(driver, senum.clickElement, "//*[text()='Submit']", "", 0, "clicking submit file");
		Thread.sleep(1000);
//		util.action(driver, senum.clickElement, "//*[text()='OK']", "", 0, "clicking ok btn");
//		Thread.sleep(1000);
		driver.switchTo().defaultContent();
		util.action(driver, senum.switchToIframe, "//*[@id='newBodyFrame']", "", 0, "switching frame");

		try {

			driver.findElement(By.xpath("//*[@data-dismiss='modal']")).click();
		} catch (Exception e) {

		}

//		util.action(driver, senum.clickElement, "//*[@data-dismiss='modal']", "", 0, "clicking close button");

		// *[text()='OK']
		// *[@data-dismiss="modal"]
	}
}
