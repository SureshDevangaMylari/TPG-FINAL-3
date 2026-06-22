package maf.apps.HumanaAuth;

import java.awt.AWTException;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
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

public class HumanaServices {
	static Selenium_Actions_Enum senum;
	static Selenium_Util util;
	static Humana xpath;
	private static final Logger logger = LogManager.getLogger(HumanaServices.class.getName());
	public static String auth = "";
	public static WebDriver driver;

	public HumanaServices(WebDriver driver) {
		super();
		this.driver = driver;
	}

	public static String authnbr = "";
	public static Map<String, String> rtObj = new HashMap<String, String>();

	public static void login(String username, String pass) throws IOException, InterruptedException {
		logger.info(" humana login");

		driver.get("https://apps.availity.com/web/onboarding/availity-fr-ui/?p:lm=1714010575#/login");

		util.action(driver, senum.sendText, xpath.username, username, 0, "entering login name");
		util.action(driver, senum.sendText, xpath.password, pass, 0, "entering login password");

		util.action(driver, senum.clickElement, xpath.submit, "", 0, "clicking login button");

		// newBodyFrame

	}

	public static String Auth(Map<String, String> data, List<String> dx, List<List> procedures)
			throws InterruptedException, ParseException, FindFailed, AWTException {
		Screen screen = new Screen();
		screen.click();
		rtObj = new HashMap<String, String>();
		auth = "";
		logger.info(" data " + data);
		util.action(driver, senum.waitUntilLoad, "//*[@id='home-logo']", "", 0, "wait home");

		chooseState(driver, data);

		driver.switchTo().defaultContent();

		util.action(driver, senum.clickElementById, "home-logo", "", 0, "clicking home");

		// <button id="onetrust-accept-btn-handler"
		// class="__web-inspector-hide-shortcut__">Accept All Cookies</button>

		util.action(driver, senum.switchToIframe, "//*[@id='newBodyFrame']", "", 0, "switching frame");

		util.action(driver, senum.clickElement, xpath.authorization_ref, "", 0, "clicking authorization");

		util.action(driver, senum.clickElement, xpath.authReq, "", 0, "clicking authorization req");

		DesktopKeyboard keyboard = new DesktopKeyboard();

		util.action(driver, senum.clickElementById, xpath.Iorganization, "", 0, "clicking Iorganization");
//
		Thread.sleep(1000);
		String provider = "";
		Thread.sleep(1000);
		if (data.get("code").contains("ST")) {

			provider = "SMITH THERAPY PARTNERS";

		} else if (data.get("code").contains("FIT")) {
			provider = "FIT Physical";
		}
		data.put("provider", provider);
		keyboard.type(provider);
		Thread.sleep(1000);
		keyboard.keyDown(Key.ENTER);
		keyboard.keyUp(Key.ENTER);

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

		String Xproc = xpath.Xprocedure;
		String Iqnty = "procedures.0.quantity";
		for (int x = 0; x < procedures.get(1).size(); x++) {
			Iqnty = "procedures." + x + ".quantity";
			if (x != 0) {
				util.action(driver, senum.clickElement, xpath.Xaddprocedures, "", 0, "clicking add procedures");

			}

			String icd = procedures.get(0).get(x).toString();
			util.action(driver, senum.sendTextById, "procedure-search-input", procedures.get(0).get(x).toString(), 0,"sending procedures");
			Thread.sleep(1500);

			Thread.sleep(1000);

			while (true) {
				try {//*[contains(text(),'

					driver.findElement(By.xpath("" + icd + "')]")).click();
					break;
				} catch (Exception e) {
					try {
						Thread.sleep(4000);
						driver.findElement(By.id("procedure-search-input")).click();
					} catch (Exception e2) {

					}
				}

			}
//			util.action(driver, senum.clickElement, "//*[contains(text(),'" + icd + "')]", "", 0, "clicking  code");

		}
		Thread.sleep(1000);
		String date = data.get("effectDate");

		String newDate = date.substring(0, 6) + "20" + date.substring(6, 8);
		util.action(driver, senum.sendTextById, "fromDate", newDate, 0, "sending effdate");
		rtObj.put("fromDate", newDate);
		Thread.sleep(1000);
		util.action(driver, senum.sendTextById, "provider-search-input", provider, 0, "sending todate");
		util.action(driver, senum.clickElement, "//*[contains(text(),'" + provider + "')]",
				"//*[contains(text(),'" + provider + "')]", 0, "clicking the provider");
		Thread.sleep(1000);
		util.action(driver, senum.sendTextById, "subscriber-memberId", data.get("memberId"), 0, "sending mem id");
		Thread.sleep(1000);
		util.action(driver, senum.sendTextById, "patient-birthDate", data.get("dob"), 0, "clicking the provider");
		Thread.sleep(1000);
		util.action(driver, senum.clickElement, "//*[text()='Search']", "", 0, "clicking the search");
		Thread.sleep(1000);
		util.action(driver, senum.clickElement, "//*[text()='Continue']", "", 0, "clicking the continue");

		/// end

		while (true) {
			try {
				driver.findElement(
						By.xpath("//*[text()='Patient Not Found or No Coverage for given Date of Service.']")).click();

				return ResultEnum.NoRecordsFound.name();

			} catch (Exception e) {

			}

			try {
				driver.findElement(By.xpath("//*[text()='Requested Services']"));
				break;
			} catch (Exception e) {

			}
			// *[text()='Patient Not Found or No Coverage for given Date of Service.']
		}

		Thread.sleep(2000);

		util.action(driver, senum.waitUntilLoad, "//*[@class='review-procedure']/div/span", "", 0,
				"waiting for the status ");

		Thread.sleep(1000);
		String status = (String) util.action(driver, senum.getText, "//*[@class='review-procedure']/div/span", "", 0,
				"getting status");
		return submission(data, dx, procedures);
//		if (status.equalsIgnoreCase("Auth/Referral Required")) {
//			logger.info("auth is required ");
//			return submission(data, dx, procedures);
//		} else if (status.equals("SEE MESSAGE")) {
//
//			logger.info("see message ");
//
//			util.action(driver, senum.clickElement, "//*[text()='Print']", "", 0, "printing page...");
//
//			String message = (String) util.action(driver, senum.getText, "//*[text()='Message']/parent::*", "", 0,
//					"getting status");
//
////			return submission(data, dx, procedures);
//
////			
////			String text=(String) util.action(driver, senum.getText, "//*[text()='Message']/parent::*", "", 0,
////					"getting message");
//
//			// *[text()='Message']/following-sibling::*
//			return message;
//		}

//		else {
//			logger.info("auth is not required ");
//
//			util.action(driver, senum.clickElementById, xpath.IprintPage, "", 0, "printing page...");
//
//			return ResultEnum.AuthIsnotRequired.name();
//		}

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
			throws InterruptedException, ParseException, FindFailed, AWTException {
		Screen sc = new Screen();
		sc.click();
		DesktopKeyboard keyboard = new DesktopKeyboard();
		util.action(driver, senum.clickElement, "//*[text()='Continue']", "", 0, "clicking the continue");

		util.action(driver, senum.sendTextById, "placeOfServiceCode-input", "Office", 0, "sending the pos");
		util.action(driver, senum.clickElement, "//*[contains(text(),'Office')]", "", 0, "clicking the Office");

		DateTimeFormatter inputFormatter = new DateTimeFormatterBuilder().appendPattern("MM-dd-")
				.appendValueReduced(ChronoField.YEAR, 2, 2, 2000).toFormatter();

		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		DateTimeFormatter outputFormatter2 = DateTimeFormatter.ofPattern("MM-dd-yy");

		String effectD = data.get("effectDate");

		LocalDate dateDob = LocalDate.parse(effectD, inputFormatter);
		String newd = dateDob.format(outputFormatter);

		LocalDate dateto = dateDob.plusDays(90);
		String toDate = dateto.format(outputFormatter);
		String toDateRT = dateto.format(outputFormatter2);
		System.out.println(toDate);
		Thread.sleep(1000);
		util.action(driver, senum.sendTextById, "toDate", toDate, 0, "sending to date");
		Thread.sleep(1000);
		util.action(driver, senum.selectByVisibleText, "//*[@id='quantityTypeCode']", "Visits", 0, "selecting visit");
		Thread.sleep(1000);
		util.action(driver, senum.sendTextById, "quantity", "12", 0, "selecting quantity");

		util.action(driver, senum.sendTextById, "patient-conditionCode-input", "patient is ambulatory", 0,
				"sending the patient is ambulatory");
		util.action(driver, senum.clickElement, "//*[contains(text(),'Patient is ambulatory')]", "", 0,
				"clicking the patient is ambulatory");

		// dx codes code
		String startDate = data.get("caseStartDate");
		String newStart = startDate.substring(0, 6) + "20" + startDate.substring(6, 8);
		for (int x = 0; x < dx.size(); x++) {

			String dxX = "(//*[contains(text(),'Diagnosis Code')])[2]/parent::*/div";

			if (x != 0) {
				Thread.sleep(1000);
				util.action(driver, senum.clickElement, "//*[text()='Add diagnosis']", "", 0, "click add diganosis");

				dxX = "(//*[contains(text(),'Diagnosis Code " + (x + 1) + "')])[2]/parent::*";

			}

			util.action(driver, senum.sendTextById, "diagnoses-search-input", dx.get(x), 0, "sending the patient dx");
			util.action(driver, senum.clickElement, "//*[contains(text(),'" + dx.get(x) + "')]", "", 0,
					"clicking the patient is dxds");
			Thread.sleep(1000);

			util.action(driver, senum.sendTextById, "diagnoses[" + x + "]-date", newStart, 0,
					"sending the start date ");

		}
		Thread.sleep(1000);

// procedures qty and dates

		// *[@class="tile result"]

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

		Thread.sleep(1000);
		String provider = data.get("provider");
		util.action(driver, senum.clickElement, "//*[text()='Continue']", "", 0, "clicking the continue");
		util.action(driver, senum.clickElement, "(//*[text()='Show Manual Entry'])", "", 0, "clicking the show manual");

		util.action(driver, senum.sendText, "(//*[@id='provider-search-input'])", provider, 0, "sending provider");
		util.action(driver, senum.clickElementById, "provider-search-server-list", " ", 0, "clicking the provider");

		Thread.sleep(2000);

		util.action(driver, senum.selectByVisibleText, "//*[@id='servicingProviders[0]-roleCode']", "Service Location",
				0, "chaning the locaton");

		Thread.sleep(1000);
		util.action(driver, senum.sendText, "//*[@class='tile no-result BlockProviderFacilitySplit']//*[@type='tel']",
				"7257267847", 0, "sending phone");

// for facility
		util.action(driver, senum.clickElement, "(//*[text()='Show Manual Entry'])", "", 0, "clicking the show manual");

		util.action(driver, senum.sendText, "(//*[@id='provider-search-input'])", provider, 0, "sending provider");
		util.action(driver, senum.clickElementById, "provider-search-server-list", " ", 0, "clicking the provider");

		Thread.sleep(2000);

		util.action(driver, senum.sendText, "//*[@class='tile no-result BlockProviderFacilitySplit']//*[@type='tel']",
				"7257267847", 0, "sending phone");
		util.action(driver, senum.clickElement, "//*[text()='Continue']", "", 0, "clicking the continue");

		Thread.sleep(3000);
		util.action(driver, senum.clickElement, "//*[text()='Continue']", "", 0, "clicking the continue");

		String status = "";
		Map<String, Object> rtData = new HashMap<String, Object>();
		Thread.sleep(1000);
		driver.switchTo().defaultContent();
		Thread.sleep(2000);
		util.action(driver, senum.switchToIframe, "//*[@id='content-iframe']//iframe", "", 1, "switching frame");
		String folderPath = RootPath.BASE_DIR + "\\resources\\pdfDownload";
		File folder = new File(folderPath);
		File[] pdfFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));
		File pdfFiles2 = Arrays.stream(pdfFiles).max(Comparator.comparingLong(File::lastModified)).orElse(null);

		String filename = pdfFiles2.getAbsolutePath();
		System.out.println(pdfFiles2.getAbsolutePath());
		util.action(driver, senum.sendTextById, "supplementalInformation-attachments", filename, 0, "sending file	");
		//

		Thread.sleep(2000);
		util.action(driver, senum.clickElement, "//*[text()='Submit']", "", 0, "clicking the continue");

		util.action(driver, senum.waitUntilLoad, "//*[contains(@class,'review-item reviewStatus')]", "", 0,
				"getting status");
		status = (String) util.action(driver, senum.getText, "//*[contains(@class,'review-item reviewStatus')]", "", 0,
				"waitUntilLoad status");

		auth = (String) util.action(driver, senum.getText, "//*[@class='review-item reviewStatus success']", "", 0,
				"waitUntilLoad status");
		Thread.sleep(3000);
		rtObj.put("toDate", toDateRT);
		// Xsubmissionstatus
		rtObj.put("reqVisits", procedures.get(1).getLast().toString());
		if (status.contains("PENDED")) {
			logger.info(" pending");

//			RTservices.Pended(driver, rtData);

			auth = (String) util.action(driver, senum.getText,
					"//*[contains(text(),'Reference Number')]/following-sibling::*", "", 0, "gettting auth number	");
			auth = auth.replace("Cert:", "").trim();
			// *[contains(text(),'Add Clinical Documents')]

			return ResultEnum.PENDED.name();

		} else if (status.contains("NO ACTION REQUIRED")) {
			Thread.sleep(2000);

			return ResultEnum.NoAuthNeeded.name();
		} else

		{

			logger.info(" auto approval certified ");
			String d = (String) util.action(driver, senum.getText,
					"//*[text()='Service From - To Date']/following-sibling::*", "", 0, "gettting dates	");

			rtObj.put("toDate", toDateRT);
			util.action(driver, senum.clickElement, "//*[contains(text(),'Print')]", "", 0, "clicking print button	");
			Thread.sleep(2000);
			auth = (String) util.action(driver, senum.getText, "//*[contains(text(),'Cert:')]", "", 0,
					"gettting auth number	");
			auth = auth.replace("CCert:", "").trim();
			return ResultEnum.CERTIFIED.name();
//			RTservices.certified(driver, rtData);
		}
		// serviceType

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

	static void chooseState(WebDriver driver, Map<String, String> row) throws InterruptedException {

		String code = row.get("code");

		String state = (String) Selenium_Util.action(driver, Selenium_Actions_Enum.getText,
				"//*[@id='region-select']//div", "", 0, "geting state");
		String ReqState = code.contains("ST") ? "Nevada" : "Utah";
		if (state.equalsIgnoreCase(ReqState)) {

			logger.info(" same state no neeed to change");

		} else {
			logger.info(" not same state  neeed to change");

			Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, "//*[@id='region-select']//div", "", 0,
					"clicking the state");
			Thread.sleep(1000);
			Selenium_Util.action(driver, Selenium_Actions_Enum.sendText, "//*[@id='regions_filter']", ReqState, 0,
					"clicking the state");
			Thread.sleep(1000);
			Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, "//*[text()='" + ReqState + "']", "", 0,
					"clicking the state");

		}

	}
}
