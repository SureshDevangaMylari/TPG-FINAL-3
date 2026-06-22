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
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Screen;

import com.fasterxml.jackson.core.JsonProcessingException;

import maf.apps.hoagCC.Flow;
import maf.apps.master.selenium.elementspath.RaintreeLocator;
import maf.apps.util.FileUtil;
import maf.apps.util.Operations;
import maf.apps.util.RootPath;
import maf.apps.util.SeleniumUtil;
import maf.apps.util.Selenium_Actions_Enum;
import maf.apps.util.Selenium_Util;

public class RTservices2 {
	public static final Logger logger = LogManager.getLogger(RTservices2.class);
	static Selenium_Actions_Enum senum;

	public static void Pended(WebDriver driver, Map<String, Object> data) throws InterruptedException, AWTException {
		Screen screen = new Screen();
		Robot robot = new Robot();
		LocalDate date = LocalDate.now();
		LocalDate nextday = date.plusDays(1);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
		String nextDate = formatter.format(nextday);
		String currentdate = formatter.format(date);

//		String authNum = HumanaServices.authnbr;
		String authNum = HumanaServices.auth;

		// Execute the JavaScript
		Selenium_Util.action(driver, Selenium_Actions_Enum.sendText, RaintreeLocator.XauthInput, authNum, 0,
				" sending auth number");
		driver.findElement(By.xpath("//*[@name='auth']"))

				.sendKeys(org.openqa.selenium.Keys.ENTER);
		Selenium_Util.action(driver, Selenium_Actions_Enum.sendTextById, "id.CONFIRMAUTH.WidgetAt_3_2", authNum, 0,
				" sending confitm auth number");

		driver.findElement(By.id("id.CONFIRMAUTH.WidgetAt_3_2"))

				.sendKeys(org.openqa.selenium.Keys.ENTER);
		Thread.sleep(1000);
//
//		for the datesis

		LocalDate currentDate = LocalDate.now();

		LocalDate _workingDate = null;

		// 7days
		_workingDate = currentDate.plusDays(1);

		if (_workingDate.getDayOfWeek().toString().equals("SATURDAY")) {
			_workingDate = _workingDate.plusDays(2);
			logger.info(_workingDate);
		} else

		if (_workingDate.getDayOfWeek().toString().equals("SUNDAY")) {
			_workingDate = _workingDate.plusDays(1);

		}
		Map obj = new LinkedHashMap();
		String formattedDate6 = formatter.format(_workingDate).replace("/", "-");
		logger.info(formatter);
		String reqDate = currentdate;
		Selenium_Util.action(driver, senum.selectCheckboxByValue, RaintreeLocator.XstatusCategory,
				"Pending with Insurance", 0, " selecting \"Pending with Insurance\"");

//		Selenium_Util.action(driver, senum.sendText, RaintreeLocator.XauthInput, "Pending with Insurance", 0, " selecting \"Pending with Insurance\"");
		Selenium_Util.action(driver, Selenium_Actions_Enum.sendText, RaintreeLocator.XnextActivity, formattedDate6, 0,
				"sending 6 working date");

		Selenium_Util.action(driver, Selenium_Actions_Enum.sendTextById, RaintreeLocator.IreqDate, reqDate, 0,
				" sending requsting currentdate");

		Selenium_Util.action(driver, Selenium_Actions_Enum.sendTextById, RaintreeLocator.IreqVisit,
				data.get("reqVisits").toString(), 0, " sending requestedVisits");

		Selenium_Util.action(driver, Selenium_Actions_Enum.clickElementById, RaintreeLocator.Isave, authNum, 0,
				" clicking save btn");

	}

//submitted and approved
	public static void certified(WebDriver driver, Map<String, Object> data) throws InterruptedException,
			ParseException, FindFailed, JsonProcessingException, IOException, AWTException {

		logger.info("data " + data);
		Screen screen = new Screen();
		String t = data.get("date").toString().trim();

		String d = t.substring(0, 10);
		String caseNumber = data.get("caseName").toString();
		String patient_Name = data.get("fullname").toString();
		String pinNumber = data.get("pin").toString();
		String d2 = t.substring(t.length() - 10, t.length());
		;
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatter2 = new SimpleDateFormat("MM-dd-yy");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
		LocalDate date = LocalDate.now();
		// Convert the string to a Date object
		Date datetemp = formatter1.parse(d);
		logger.info("Converted Date: " + datetemp);

		String from = formatter2.format(datetemp);

		datetemp = formatter1.parse(d2);
		logger.info(from);
		String to = formatter2.format(datetemp);
		logger.info(to);
		String currentdate = formatter.format(date);
		String reqDate = currentdate;

//		String authNum = HumanaServices.authnbr;
		String authNum = HumanaServices.auth;
		WebElement nextActivityComment = null;
		while (true) {
			try {
				nextActivityComment = driver.findElement(By.id("id.AUTH!!8.nextActivityComment"));
				break;
			} catch (Exception e) {
				try {
					nextActivityComment = driver
							.findElement(By.xpath("//*[@data-widgetname=\"nextactivitycomment\"]/div"));

					break;
				} catch (Exception e2) {
				}

			}
		}
		Operations.clickElementById("id.AUTH!!8.btnNextActivityComment", driver);

		Operations.waitUntilLoad("//*[text()='Comment History']", driver);
		Thread.sleep(1000);
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_A);
		robot.keyRelease(KeyEvent.VK_A);

		Operations.sendTextById("id.RTFHISTORY!!12.desc", "Auth", driver);

		Operations.clickElementById("id.RTFHISTORY!!12.notes", driver);
		Thread.sleep(10000);
		Operations.sendTextById("id.RTFHISTORY!!12.notes", currentdate + " RFA sent  AUTH#  " + "Submitted  " + authNum,
				driver);

		Thread.sleep(2000);
		Operations.clickElementById("ToolbarButton.Save", driver);
		Thread.sleep(2000);
		Operations.clickElementById("ToolbarButton.Save", driver);

		Thread.sleep(1500);

		Thread.sleep(1500);

		// Execute the JavaScript
		Selenium_Util.action(driver, Selenium_Actions_Enum.sendText, RaintreeLocator.XauthInput, authNum, 0,
				" sending auth number");
		driver.findElement(By.xpath("//*[@name='auth']"))

				.sendKeys(org.openqa.selenium.Keys.ENTER);
		Selenium_Util.action(driver, Selenium_Actions_Enum.sendTextById, "id.CONFIRMAUTH.WidgetAt_3_2", authNum, 0,
				" sending confitm auth number");

		driver.findElement(By.id("id.CONFIRMAUTH.WidgetAt_3_2"))

				.sendKeys(org.openqa.selenium.Keys.ENTER);
		Thread.sleep(1000);
		Selenium_Util.action(driver, Selenium_Actions_Enum.sendText, RaintreeLocator.Xstatus, "Active", 0,
				"sending status");
		Thread.sleep(1000);
		Selenium_Util.action(driver, Selenium_Actions_Enum.selectCheckboxByValue, RaintreeLocator.XstatusCategory, "",
				0, "Selecting the status");
		Selenium_Util.action(driver, Selenium_Actions_Enum.sendText, RaintreeLocator.XreceivedDate, reqDate, 0,
				"sending recieved date");
		Selenium_Util.action(driver, Selenium_Actions_Enum.sendText, RaintreeLocator.XreceivedVisits,
				data.get("reqVisits").toString(), 0, "sending recieved visits");

		Selenium_Util.action(driver, Selenium_Actions_Enum.sendText, RaintreeLocator.XeffectDate, from, 0,
				"sending effective date");

		Selenium_Util.action(driver, Selenium_Actions_Enum.sendText, RaintreeLocator.XeffecDateto, to, 0,
				"sending effective date to ");
		String simple = driver.findElement(By.xpath("//*[@aria-label='Simple visit based auth']"))
				.getAttribute("readonly");
		logger.info(" simple" + simple);
		if (simple == null) {
			logger.info(" simple auth");
		} else {
			logger.info(" advance auth");
			Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, RaintreeLocator.Xsimpleauth, "", 0,
					" clicking simple auth");
			Thread.sleep(1000);
		}

		Selenium_Util.action(driver, Selenium_Actions_Enum.sendText, RaintreeLocator.Xallowed,
				data.get("reqVisits").toString(), 0, " sending allowed visits");

		// arm start
		if (Main.isInitial) {
			Map obj = new LinkedHashMap();
			Actions actions = new Actions(driver);
			WebElement menu = driver.findElement(By.xpath("//i[@class='mdi mdi-menu']"));
			String goToMenu = "//div[text()='GoTo Menu']";
			menu.click();
			Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, goToMenu, null, 0,
					"waiting for the gotomenu");

			Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, goToMenu, null, 0,
					"waiting for the authorization");
			String patientChart = "//div[@id='id.QKS!!9.1!sGotoMenuitem_patChart']/div[1]/span[1]";

			Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, patientChart, null, 0,
					"waiting for the patientChart");

			Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, patientChart, null, 0,
					"waiting for the patientChart");
			String visitInfo = "//span[text()='isit Info']";

			Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, visitInfo, null, 0,
					"waiting for the visitInfo");
			List<WebElement> totalcase = driver
					.findElements(By.xpath("(//table[@class='w-listbox-table']/following::table)[1]/tbody[1]/tr"));
			boolean caseMatch = false;

			String caseName = "";
			for (int visitCasecount = 1; visitCasecount <= totalcase.size(); visitCasecount++) {
				WebElement visitCase = driver
						.findElement(By.xpath("(//table[@class='w-listbox-table']/following::table)[1]/tbody[1]/tr["
								+ visitCasecount + "]/td[1]"));
				String visitCaseno = visitCase.getText();
				if (caseNumber.equals(visitCaseno)) {
					actions.doubleClick(visitCase).build().perform();
					Thread.sleep(3000);
					caseName = driver
							.findElement(By.xpath("(//table[@class='w-listbox-table']/following::table)[1]/tbody[1]/tr["
									+ visitCasecount + "]/td[1]//parent::tr/td[3]"))
							.getText();
					caseMatch = true;
					break;
				} else {

				}
			}

			// for case name
			data.put("caseName", caseName);

			if (!caseMatch) {
				obj.put("ICD codes", "");
				obj.put("Auth#", "");
				List li = new ArrayList();

				obj.put("details", "no matched case number ");

				li.addAll(obj.values());
				FileUtil.PrintJsonFile(driver, obj, patient_Name + "-" + pinNumber + "-" + caseNumber);
				FileUtil.addRow(li, "HumanaAuthRetrieval");
				driver.findElement(By.xpath(RaintreeLocator.XbackButton)).click();

				Thread.sleep(1000);
				driver.findElement(By.xpath(RaintreeLocator.XbackButton)).click();
				driver.findElement(By.xpath(RaintreeLocator.XbackButton)).click();
				FlowInitial.gobackToDashboard();
			}

			Thread.sleep(2000);

			String casedocuments = "//span[text()='ocuments']";
			Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, casedocuments, null, 0,
					"waiting for document tasks");

			Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, casedocuments, null, 0,
					"clicking the  document tasks");

			while (true) {

				try {
					driver.findElement(By.xpath("//th[text()='User']//following::tbody[1]/tr"));
					break;

				}

				catch (Exception e) {

				}
			}

			Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, "//th[text()='User']/parent::*/th[2]", "",
					0, "clicking the template");
			WebElement docEle = null;

			docEle = driver.findElement(By.xpath(
					"//th[text()='User']//following::tbody[1]/tr[td[text()='ARM'] and td[text()='Referral Tracking']]"));

			if (docEle != null) {
				actions.doubleClick(docEle).build().perform();
			} else {
				logger.info(" no ARM record found");
				obj.put("ICD codes", "");
				obj.put("Auth#", "");
				List li = new ArrayList();

				obj.put("details", "no ARM record found");

				li.addAll(obj.values());
				FileUtil.PrintJsonFile(driver, obj, patient_Name + "-" + pinNumber + "-" + caseNumber);
				FileUtil.addRow(li, "HumanaAuthRetrieval");
				Thread.sleep(1000);
				FlowInitial.gobackToDashboard();
			}

			Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, RaintreeLocator.Xcheckboxpending, "", 0,
					"waiting for the page ");

			String pendingDate = (String) SeleniumUtil.action(driver, Selenium_Actions_Enum.getTextFromInput,
					RaintreeLocator.XpendingDate, null, null, null, null, 0, "getting pending date");

			if (pendingDate.length() <= 0) {

				logger.info(" notchecked Converted - Pending Schedule");
				Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, RaintreeLocator.Xcheckboxpending, "",
						0, "clicking the pending schedule");

			}

			Selenium_Util.action(driver, senum.clickElementById, "ToolbarButton.Save", "", 0, " clicking save ");
			Screen sc = new Screen();
			while (true) {

				try {
					if (sc.exists(RootPath.BASE_DIR + "//resources//sikuli//Tpg//auth.png") != null) {

						logger.info("foyund");

						break;
					} else {
						try {

							driver.findElement(By.xpath(RaintreeLocator.XbackButton)).click();
							logger.info("back brn clicked");
							Thread.sleep(200);
						} catch (Exception e2) {

						}
					}

				} catch (Exception e) {

				}

			}
		} else {
			Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, "// *[text()='Authorization Task']", "", 0,
					" clicking Authorization btn");

			// *[@name='type']
			Selenium_Util.action(driver, Selenium_Actions_Enum.selectCheckboxByValue, "//*[@name='type']",
					"Authorization Scheduling", 0, " selecting authorization");

			Selenium_Util.action(driver, Selenium_Actions_Enum.clickElementById, RaintreeLocator.Isave, "", 0,
					" clicking save btn");
			Thread.sleep(1000);
		}
		// arm code ends
		uploadDoc(driver);
		Thread.sleep(1000);
		Selenium_Util.action(driver, Selenium_Actions_Enum.clickElementById, RaintreeLocator.Isave, "", 0,
				" clicking save btn");
		Thread.sleep(4000);
		try {
			driver.findElement(By.xpath("//*[text()='Proceed with current dates']")).click();
		} catch (Exception e) {

		}
	}

	// *[@name='status']
	public static void uploadDoc(WebDriver driver) throws InterruptedException, FindFailed {

		while (true) {

			try {
				driver.findElement(By.xpath("//*[text()='Browse...']")).click();
				break;

			} catch (Exception e) {

			}
		}

		Screen screen = new Screen();
		Thread.sleep(1000);
		while (true) {
			try {
				if (screen.exists(RootPath.BASE_DIR + "//resources//sikuli//hpsj//Open.png") != null) {
					break;
				}
			} catch (Exception e) {

			}
		}
		Thread.sleep(1000);
		String folderPath = RootPath.BASE_DIR + "\\resources\\pdfDownload\\";
		File mostRecentPdf = Flow.findMostRecentPdf(folderPath);

		if (mostRecentPdf != null) {
			logger.info("Most recent PDF file: " + mostRecentPdf.getName());
		} else {
			logger.info("No PDF files found in the specified folder.");
		}
		String filename = folderPath + mostRecentPdf.getName();
		String pdfFilename = folderPath.concat("\\").concat(filename);
		screen.paste(filename);
		screen.click(RootPath.BASE_DIR + "//resources//sikuli//hpsj//Open.png");
		Thread.sleep(1000);
		Selenium_Util.action(driver, Selenium_Actions_Enum.waitWhilelLoad, "//*[text()='Browse...']", "", 0,
				"uploading doc");

	}

	public static void AuthIsnotRequired(WebDriver driver, Map<String, Object> rtobj)
			throws FindFailed, InterruptedException, AWTException {
		// TODO Auto-generated method stub
		Screen screen = new Screen();
		WebElement nextActivityComment = null;
		while (true) {
			try {
				nextActivityComment = driver.findElement(By.id("id.AUTH!!8.nextActivityComment"));
				break;
			} catch (Exception e) {
				try {
					nextActivityComment = driver
							.findElement(By.xpath("//*[@data-widgetname=\"nextactivitycomment\"]/div"));

					break;
				} catch (Exception e2) {
				}

			}
		}
		Operations.clickElementById("id.AUTH!!8.btnNextActivityComment", driver);

		Operations.waitUntilLoad("//*[text()='Comment History']", driver);
		Thread.sleep(1000);
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_A);
		robot.keyRelease(KeyEvent.VK_A);
		Operations.sendTextById("id.RTFHISTORY!!12.desc", "Auth", driver);

		Operations.clickElementById("id.RTFHISTORY!!12.notes", driver);
		Thread.sleep(10000);
		Operations.sendTextById("id.RTFHISTORY!!12.notes", "9999", driver);

		Thread.sleep(2000);
		Operations.clickElementById("ToolbarButton.Save", driver);
		Thread.sleep(2000);
		Operations.clickElementById("ToolbarButton.Save", driver);

		Thread.sleep(1500);
		Thread.sleep(1500);

		Selenium_Util.action(driver, Selenium_Actions_Enum.sendText, RaintreeLocator.XauthInput, "9999", 0,
				" sending auth number");
		driver.findElement(By.xpath("//*[@name='auth']"))

				.sendKeys(org.openqa.selenium.Keys.ENTER);
		Selenium_Util.action(driver, Selenium_Actions_Enum.sendTextById, "id.CONFIRMAUTH.WidgetAt_3_2", "9999", 0,
				" sending confitm auth number");

		driver.findElement(By.id("id.CONFIRMAUTH.WidgetAt_3_2"))

				.sendKeys(org.openqa.selenium.Keys.ENTER);
		Thread.sleep(1000);
		WebElement menu = driver.findElement(By.xpath("//i[@class='mdi mdi-menu']"));
		menu.click();
		String goToMenu = "//div[text()='GoTo Menu']";
		String patientToMenu = "//*[text()='atient Menu']";

		Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, goToMenu, null, 0,
				"waiting for the gotomenu");
		Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, goToMenu, null, 0, "waiting for the gotomenu");

		String patientChart = "//div[@id='id.QKS!!9.1!sGotoMenuitem_patChart']/div[1]/span[1]";

		Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, patientToMenu, null, 0,
				"waiting for the patientmenu");
		Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, patientToMenu, null, 0,
				"waiting for the patientmenu");
		String insurance = "//*[text()='nsurance / Payor']";
		Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, insurance, null, 0,
				"clicking the insurance/payor");

		String caseXpath = "//div[text()='B']";

		// *[text()='Insurance Information'] //*[@class='screen screen-active
		// screen-List']//div[text()='00000']
		Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, "//*[text()='Insurance Information']", null,
				0, "waiting for the Insurance Information ");

		// Selenium_Util.action(driver, Selenium_Actions_Enum.doubleClickElement,
		// caseXpath, null, 0, "clicking the insurance/payor");

		boolean casem = false;

		try {
			WebElement ele = driver.findElement(By.xpath(caseXpath));
			Actions ac = new Actions(driver);
			ac.doubleClick(ele).build().perform();

		} catch (Exception e) {

			driver.findElement(By.xpath(RaintreeLocator.XbackButton)).click();

			Thread.sleep(1000);
			driver.findElement(By.xpath(RaintreeLocator.XbackButton)).click();
			driver.findElement(By.xpath(RaintreeLocator.XbackButton)).click();

		}
		Thread.sleep(1000);
		Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, RaintreeLocator.authrequired, null, 0,
				"waiting for table");

		Selenium_Util.action(driver, Selenium_Actions_Enum.clickElementById, RaintreeLocator.authrequired, null, 0,
				"clicking auth required");

		Screen sc = new Screen();
		while (true) {

			try {
				if (sc.exists(RootPath.BASE_DIR + "//resources//sikuli//Tpg//auth.png") != null) {

					logger.info("foyund");

					break;
				} else {
					try {

						driver.findElement(By.xpath(RaintreeLocator.XbackButton)).click();
						logger.info("back brn clicked");
						Thread.sleep(200);
					} catch (Exception e2) {

					}
				}

			} catch (Exception e) {

			}

		}
		// *[@class='backdown']
		// end of the patient menu code
		Thread.sleep(1000);

		uploadDoc(driver);
		Selenium_Util.action(driver, Selenium_Actions_Enum.clickElementById, RaintreeLocator.Isave, "", 0,
				" clicking save btn");
		Thread.sleep(4000);
		try {
			driver.findElement(By.xpath("//*[text()='Proceed with current dates']")).click();
		} catch (Exception e) {

		}
	}

	public static String doSimpleRetrieval(WebDriver driver, Map<String, Object> data)
			throws InterruptedException, ParseException, FindFailed {
		logger.info("data " + data);
		Screen screen = new Screen();
		String t = data.get("date").toString().trim();

		String d = t.substring(0, 10);
		;
		String d2 = t.substring(t.length() - 10, t.length());
		;
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatter2 = new SimpleDateFormat("MM-dd-yy");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
		LocalDate date = LocalDate.now();
		// Convert the string to a Date object
		Date datetemp = formatter1.parse(d);
		logger.info("Converted Date: " + datetemp);

		String from = formatter2.format(datetemp);

		datetemp = formatter1.parse(d2);
		logger.info(from);
		String to = formatter2.format(datetemp);
		logger.info(to);
		String currentdate = formatter.format(date);
		String reqDate = currentdate;

//	String authNum = HumanaServices.authnbr;
		String authNum = HumanaServices.auth;

		WebElement nextActivityComment = null;
		while (true) {
			try {
				nextActivityComment = driver.findElement(By.id("id.AUTH!!8.nextActivityComment"));
				break;
			} catch (Exception e) {
				try {
					nextActivityComment = driver
							.findElement(By.xpath("//*[@data-widgetname=\"nextactivitycomment\"]/div"));

					break;
				} catch (Exception e2) {
				}

			}
		}

		String temp = nextActivityComment.getText();
		JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

//	nextActivityComment.sendKeys(org.openqa.selenium.Keys.CONTROL);
		nextActivityComment.click();

		Thread.sleep(1000);

		// Execute the JavaScript
		Selenium_Util.action(driver, Selenium_Actions_Enum.sendText, RaintreeLocator.Xstatus, "Active", 0,
				"sending status");
		Thread.sleep(1000);
		Selenium_Util.action(driver, Selenium_Actions_Enum.selectCheckboxByValue, RaintreeLocator.XstatusCategory, "",
				0, "Selecting the status");
		Selenium_Util.action(driver, Selenium_Actions_Enum.sendText, RaintreeLocator.XreceivedDate, reqDate, 0,
				"sending recieved date");
		Selenium_Util.action(driver, Selenium_Actions_Enum.sendText, RaintreeLocator.XreceivedVisits,
				data.get("reqVisits").toString(), 0, "sending recieved visits");

		Selenium_Util.action(driver, Selenium_Actions_Enum.sendText, RaintreeLocator.XeffectDate, from, 0,
				"sending effective date");

		Selenium_Util.action(driver, Selenium_Actions_Enum.sendText, RaintreeLocator.XeffecDateto, to, 0,
				"sending effective date to ");
		String simple = driver.findElement(By.xpath("//*[@aria-label='Simple visit based auth']"))
				.getAttribute("readonly");
		logger.info(" simple" + simple);
		if (simple == null) {
			logger.info(" simple auth");
		} else {
			logger.info(" advance auth");
			Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, RaintreeLocator.Xsimpleauth, "", 0,
					" clicking simple auth");

			Thread.sleep(1000);
			Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, RaintreeLocator.Xsimpleauth, "", 0,
					" clicking simple auth");
		}

		Selenium_Util.action(driver, Selenium_Actions_Enum.sendText, RaintreeLocator.Xallowed,
				data.get("reqVisits").toString(), 0, " sending allowed visits");
		uploadDoc(driver);
		Thread.sleep(1000);
		Selenium_Util.action(driver, Selenium_Actions_Enum.clickElementById, RaintreeLocator.Isave, "", 0,
				" clicking save btn");

		return "completed certified";
	}

	public static String retrieval(WebDriver driver, Map<String, Object> data)
			throws InterruptedException, ParseException, FindFailed {

		logger.info("data " + data);
		Screen screen = new Screen();
		String t = data.get("date").toString().trim();

		String d = t.substring(0, 10);
		;
		String d2 = t.substring(t.length() - 10, t.length());
		;
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatter2 = new SimpleDateFormat("MM-dd-yy");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
		LocalDate date = LocalDate.now();
		// Convert the string to a Date object
		Date datetemp = formatter1.parse(d);
		logger.info("Converted Date: " + datetemp);

		String from = formatter2.format(datetemp);

		datetemp = formatter1.parse(d2);
		logger.info(from);
		String to = formatter2.format(datetemp);
		logger.info(to);
		String currentdate = formatter.format(date);
		String reqDate = currentdate;

//		String authNum = HumanaServices.authnbr;
		String authNum = HumanaServices.auth;

		WebElement nextActivityComment = null;
		while (true) {
			try {
				nextActivityComment = driver.findElement(By.id("id.AUTH!!8.nextActivityComment"));
				break;
			} catch (Exception e) {
				try {
					nextActivityComment = driver
							.findElement(By.xpath("//*[@data-widgetname=\"nextactivitycomment\"]/div"));

					break;
				} catch (Exception e2) {
				}

			}
		}

		String temp = nextActivityComment.getText();
		JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

//		nextActivityComment.sendKeys(org.openqa.selenium.Keys.CONTROL);
		nextActivityComment.click();

		Thread.sleep(1000);

		// Execute the JavaScript
		Selenium_Util.action(driver, Selenium_Actions_Enum.sendText, RaintreeLocator.Xstatus, "Active", 0,
				"sending status");
		Thread.sleep(1000);
		Selenium_Util.action(driver, Selenium_Actions_Enum.selectCheckboxByValue, RaintreeLocator.XstatusCategory, "",
				0, "Selecting the status");
		Selenium_Util.action(driver, Selenium_Actions_Enum.sendText, RaintreeLocator.XreceivedDate, reqDate, 0,
				"sending recieved date");
		Selenium_Util.action(driver, Selenium_Actions_Enum.sendText, RaintreeLocator.XreceivedVisits,
				data.get("reqVisits").toString(), 0, "sending recieved visits");

		Selenium_Util.action(driver, Selenium_Actions_Enum.sendText, RaintreeLocator.XeffectDate, from, 0,
				"sending effective date");

		Selenium_Util.action(driver, Selenium_Actions_Enum.sendText, RaintreeLocator.XeffecDateto, to, 0,
				"sending effective date to ");
		String simple = driver.findElement(By.xpath("//*[@aria-label='Simple visit based auth']"))
				.getAttribute("readonly");
		logger.info(" simple" + simple);
		if (simple == null) {
			logger.info(" simple auth");
		} else {
			logger.info(" advance auth");
			Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, RaintreeLocator.Xsimpleauth, "", 0,
					" clicking simple auth");

			Thread.sleep(1000);
			Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, RaintreeLocator.Xsimpleauth, "", 0,
					" clicking simple auth");
		}

		Selenium_Util.action(driver, Selenium_Actions_Enum.sendText, RaintreeLocator.Xallowed,
				data.get("reqVisits").toString(), 0, " sending allowed visits");
		uploadDoc(driver);
		Thread.sleep(1000);

		// arm code

		Actions actions = new Actions(driver);
		WebElement menu = driver.findElement(By.xpath("//i[@class='mdi mdi-menu']"));
		String goToMenu = "//div[text()='GoTo Menu']";
		menu.click();
		Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, goToMenu, null, 0,
				"waiting for the gotomenu");

		Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, goToMenu, null, 0,
				"waiting for the authorization");
		String patientChart = "//div[@id='id.QKS!!9.1!sGotoMenuitem_patChart']/div[1]/span[1]";

		Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, patientChart, null, 0,
				"waiting for the patientChart");

		Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, patientChart, null, 0,
				"waiting for the patientChart");
		String visitInfo = "//span[text()='isit Info']";

		Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, visitInfo, null, 0,
				"waiting for the visitInfo");
		List<WebElement> totalcase = driver
				.findElements(By.xpath("(//table[@class='w-listbox-table']/following::table)[1]/tbody[1]/tr"));
		boolean caseMatch = false;

		String caseName = "";
		for (int visitCasecount = 1; visitCasecount <= totalcase.size(); visitCasecount++) {
			WebElement visitCase = driver
					.findElement(By.xpath("(//table[@class='w-listbox-table']/following::table)[1]/tbody[1]/tr["
							+ visitCasecount + "]/td[1]"));
			String visitCaseno = visitCase.getText();
			String caseNumber = data.get("caseName").toString();
			if (caseNumber.equals(visitCaseno)) {
				actions.doubleClick(visitCase).build().perform();
				Thread.sleep(3000);
				caseName = driver
						.findElement(By.xpath("(//table[@class='w-listbox-table']/following::table)[1]/tbody[1]/tr["
								+ visitCasecount + "]/td[1]//parent::tr/td[3]"))
						.getText();
				caseMatch = true;
				break;
			} else {

			}
		}

		// for case name
		data.put("caseName", caseName);

		if (!caseMatch) {

			driver.findElement(By.xpath(RaintreeLocator.XbackButton)).click();

			Thread.sleep(1000);
			driver.findElement(By.xpath(RaintreeLocator.XbackButton)).click();
			driver.findElement(By.xpath(RaintreeLocator.XbackButton)).click();
			FlowInitial.gobackToDashboard();
			return "case not matched";
		}

		Thread.sleep(2000);

		String casedocuments = "//span[text()='ocuments']";
		Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, casedocuments, null, 0,
				"waiting for document tasks");

		Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, casedocuments, null, 0,
				"clicking the  document tasks");

		while (true) {

			try {
				driver.findElement(By.xpath("//th[text()='User']//following::tbody[1]/tr"));
				break;

			}

			catch (Exception e) {

			}
		}

		Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, "//th[text()='User']/parent::*/th[2]", "", 0,
				"clicking the template");
		WebElement docEle = null;

		docEle = driver.findElement(By.xpath(
				"//th[text()='User']//following::tbody[1]/tr[td[text()='ARM'] and td[text()='Referral Tracking']]"));

		if (docEle != null) {
			actions.doubleClick(docEle).build().perform();
		} else {
			logger.info(" no ARM record found");

			Thread.sleep(1000);
			FlowInitial.gobackToDashboard();

			return "no arm record";
		}

		Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, RaintreeLocator.Xcheckboxpending, "", 0,
				"waiting for the page ");

		String pendingDate = (String) SeleniumUtil.action(driver, Selenium_Actions_Enum.getTextFromInput,
				RaintreeLocator.XpendingDate, null, null, null, null, 0, "getting pending date");

		if (pendingDate.length() <= 0) {

			logger.info(" notchecked Converted - Pending Schedule");
			Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, RaintreeLocator.Xcheckboxpending, "", 0,
					"clicking the pending schedule");

		}

		Selenium_Util.action(driver, senum.clickElementById, "ToolbarButton.Save", "", 0, " clicking save ");
		Screen sc = new Screen();
		while (true) {

			try {
				if (sc.exists(RootPath.BASE_DIR + "//resources//sikuli//Tpg//auth.png") != null) {

					logger.info("foyund");

					break;
				} else {
					try {

						driver.findElement(By.xpath(RaintreeLocator.XbackButton)).click();
						logger.info("back brn clicked");
						Thread.sleep(200);
					} catch (Exception e2) {

					}
				}

			} catch (Exception e) {

			}

		}
		// arm code ends
		Thread.sleep(1000);
		Selenium_Util.action(driver, Selenium_Actions_Enum.clickElementById, RaintreeLocator.Isave, "", 0,
				" clicking save btn");
		return "completed certified";

	}

}
