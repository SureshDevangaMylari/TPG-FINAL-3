package maf.apps.hoagCC;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
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
import maf.apps.util.FileUtil;
import maf.apps.util.ResultEnum;
import maf.apps.util.RootPath;
import maf.apps.util.Selenium_Actions_Enum;
import maf.apps.util.Selenium_Util;
import maf.apps.util.SikuliUtility;

public class HoagServices2 {
	static Selenium_Actions_Enum senum;
	static Selenium_Util util;
	static Hoag xpath;
	private static final Logger logger = LogManager.getLogger(HoagServices2.class.getName());
	public static String auth = "";

	public static void login(WebDriver driver) throws IOException, InterruptedException {

		String username = DriverManager.getpropertyvalue("hoagUser");
		String pass = DriverManager.getpropertyvalue("hoagPass");

		driver.get("https://carelink.hoag.org/EpicCareLink_PRD/common/epic_login.asp");

		util.action(driver, senum.sendTextById, xpath.Iuser, username, 0, "entering login name");
		util.action(driver, senum.sendTextById, xpath.Ipass, pass, 0, "entering login password");

		util.action(driver, senum.clickElementById, xpath.Ilogin, "", 0, "clicking login button");

	}

	public static Map<String, Object> refdetails = new HashMap<String, Object>();

	public static String PreAuth(WebDriver driver, Map<String, String> data, List<Map<String, String>> authObj, List<String> dxcodes)

			throws IOException, InterruptedException, FindFailed, AWTException {
		List<String> icdcodes = new ArrayList<>(dxcodes);
		logger.info("icd codes " + icdcodes);
		dxobj = dxcodes;
		driver.switchTo().defaultContent();
		diagnosis = new ArrayList<String>();
		refdetails = new HashMap<String, Object>();
		auth = "";
		driver.switchTo().defaultContent();

		util.action(driver, senum.switchToIframe, "//*[@id='sHeaderFrame']", "", 0, "switching frame");

		util.action(driver, senum.clickElementById, Hoag.Ihome, "", 0, "clicking home");
		driver.switchTo().defaultContent();
		util.action(driver, senum.switchToIframe, "//*[@id='sBodyFrame']", "", 0, "switching frame");

		util.action(driver, senum.clickElement, xpath.Xselectpatient, "", 0, "clicking select patient button");
		Thread.sleep(2000);
		driver.switchTo().defaultContent();

//	
		util.action(driver, senum.switchToIframe, "//*[@id='HSWebFrame']", "", 0, "switching frame");
		util.action(driver, senum.switchToIframe, "//*[@id='HSWebContainer']", "", 0, "switching frame");
		util.action(driver, senum.switchToIframe, "//*[@class='vFr']", "", 0, "switching frame");
		Thread.sleep(2000);
//		driver.switchTo().frame(0); HSWebContainer

		String lname = data.get("name").split(" ")[1];

		util.action(driver, senum.sendText, xpath.XsearchBox, data.get("name"), 0, "sending last name");
		Thread.sleep(1000);

		WebElement search = driver.findElement(By.xpath(xpath.XsearchBox));
		search.sendKeys(org.openqa.selenium.Keys.ENTER);
//		util.action(driver, senum.clickElementById, xpath.XsearchBtn, "", 0, "clicking search button");
		Thread.sleep(3000);
		util.action(driver, senum.waitUntilLoad, Hoag.Xtablesearch, "", 0, "waiting for the results");

		List<WebElement> teles = (List) util.action(driver, senum.getWebElements, Hoag.Xtablesearch, "", 0, "getting web eles");

		if (teles.size() == 0) {

			logger.info("no data found");
		} else {
			String row = "(//*[contains(text(),'" + lname + "')])[1]";

			String rec = "//*[@class='grMt grNrw']/tbody//tr[td/div[contains(text(),'" + data.get("dob") + "')]]";
			Selenium_Util.action(driver, Selenium_Actions_Enum.doubleClickElement, rec, null, 0, "Double clicking the input row");
		}

		Thread.sleep(4000);
		driver.switchTo().defaultContent();

		util.action(driver, senum.switchToIframe, "//*[@id='sMenuFrame']", "", 0, "switching frame");

		util.action(driver, senum.clickElement, Hoag.Xnavoption, "", 0, "clicking the nav options");
		driver.switchTo().defaultContent();
		Thread.sleep(500);
		util.action(driver, senum.clickElement, Hoag.Xrefera, "", 0, "clicking the refferal by memeber");

		Thread.sleep(2000);

		driver.switchTo().defaultContent();
		util.action(driver, senum.switchToIframe, "//*[@id='sBodyFrame']", "", 0, "switching frame");

		util.action(driver, senum.waitUntilLoad, Hoag.Xreftable, "", 0, "waiting for the refferal table");

		Thread.sleep(1000);

		String xrefferals = "//*[@id='DefaultWTableID']//tr[td[text()='PROSPORT PHYSICAL THERAPY, INC']]/td[1]";

		List<WebElement> refEles = (List) util.action(driver, senum.getWebElements, xrefferals, "", 0, "getting refferal eles");
		logger.info("prosport size " + refEles.size());

		int dxcheck = dxcodes.size();

		if (refEles.size() <= 1) {
			logger.info("only one record in referral page so need to sumbit");

			logger.info(" have to submit");
			String status = submit(driver, icdcodes, data);
			if (status.equals("Authorized")) {
				List li = new ArrayList();

				refdetails.put("details", "approved");
				li.addAll(refdetails.values());

				FileUtil.PrintJsonFile(driver, refdetails, data.get("name"));
//				FileUtil.addRow(li, "HoagContinuation");
				saveFile();
				return ResultEnum.submitApproval.toString();
			} else {
				List li = new ArrayList();
				refdetails.put("details", "denied");
				li.addAll(refdetails.values());

				FileUtil.PrintJsonFile(driver, refdetails, data.get("name"));
//				FileUtil.addRow(li, "HoagContinuation");

				return ResultEnum.denied.toString();
			}

//			submit(driver, icdcodes,data);
//			return ResultEnum.Submit.toString();

			// return "";

		} else {

		}

		for (int x = 0; x < refEles.size(); x++) {
			logger.info(" x val " + x);
			util.action(driver, senum.waitUntilLoad, Hoag.Xreftable, "", 0, "waiting for the refferal table");

			refEles = (List) util.action(driver, senum.getWebElements, xrefferals, "", 0, "getting refferal eles");

			String id = refEles.get(x).getText();

			try {
				if (authObj.get(x).get("Auth").contains(id)) {

					logger.info("id is there in rt" + id);

					Actions action = new Actions(driver);
					action.doubleClick(refEles.get(x)).build().perform();

					util.action(driver, senum.waitWhilelLoad, Hoag.Xrefloading, "", 0, "waiting while loading");

					Thread.sleep(1500);
					util.action(driver, senum.switchToIframe, "//*[@id='ReportFrame']", "", 0, "switching frame");
					util.action(driver, senum.waitUntilLoad, "//*[text()='Diagnoses']", "", 0, "waiting while loading");

					validateDx(dxcodes, driver);
					refdetails = getRefDetails(driver);

					diagnosis = new ArrayList<String>();
					logger.info(" dxobj " + dxobj);
					if (!dxobj.isEmpty()) {
						driver.switchTo().defaultContent();
						util.action(driver, senum.switchToIframe, "//*[@id='sBodyFrame']", "", 0, "switching frame");

						util.action(driver, senum.clickElementById, Hoag.Iback, "", 0, "clicking back");

					} else {

						if (!authObj.get(x).get("Remaining").equals("12")) {
							logger.info("icd codes " + icdcodes);
							String status = submit(driver, icdcodes, data);
							if (status.equals("Authorized")) {
								List li = new ArrayList();

								refdetails.put("details", "approved");
								li.addAll(refdetails.values());

								FileUtil.PrintJsonFile(driver, refdetails, data.get("name"));
//								FileUtil.addRow(li, "HoagContinuation");
								saveFile();
								return ResultEnum.submitApproval.toString();
							} else {
								List li = new ArrayList();
								refdetails.put("details", "denied");
								li.addAll(refdetails.values());

								FileUtil.PrintJsonFile(driver, refdetails, data.get("name"));
//								FileUtil.addRow(li, "HoagContinuation");

								return ResultEnum.denied.toString();
							}
						} else {
							logger.info("approval process");
							saveFile();
							return ResultEnum.approval.toString();
						}

					}

				} else {

					logger.info("id is not there in rt else block" + id);
					Actions action = new Actions(driver);
					action.doubleClick(refEles.get(x)).build().perform();

					util.action(driver, senum.waitWhilelLoad, Hoag.Xrefloading, "", 0, "waiting while loading");

					Thread.sleep(1500);
					util.action(driver, senum.switchToIframe, "//*[@id='ReportFrame']", "", 0, "switching frame");
					util.action(driver, senum.waitUntilLoad, "//*[text()='Diagnoses']", "", 0, "waiting while loading");

					validateDx(dxcodes, driver);
					refdetails = getRefDetails(driver);

					diagnosis = new ArrayList<String>();
					logger.info(" dxobj " + dxobj);
					if (!dxobj.isEmpty()) {

						logger.info(" have to submit");
						String status = submit(driver, icdcodes, data);
						if (status.equals("Authorized")) {
							List li = new ArrayList();

							refdetails.put("details", "approved");
							li.addAll(refdetails.values());

							FileUtil.PrintJsonFile(driver, refdetails, data.get("name"));
//							FileUtil.addRow(li, "HoagContinuation");
							saveFile();
							return ResultEnum.submitApproval.toString();
						} else {
							List li = new ArrayList();
							refdetails.put("details", "denied");
							li.addAll(refdetails.values());

							FileUtil.PrintJsonFile(driver, refdetails, data.get("name"));
//							FileUtil.addRow(li, "HoagContinuation");

							return ResultEnum.denied.toString();
						}
//						return ResultEnum.Submit.toString();
					} else {

						if (!authObj.get(x).get("Remaining").equals("12")) {
							logger.info("icd codes " + icdcodes);
							String status = submit(driver, icdcodes, data);
							if (status.equals("Authorized")) {
								List li = new ArrayList();

								refdetails.put("details", "approved");
								li.addAll(refdetails.values());

								FileUtil.PrintJsonFile(driver, refdetails, data.get("name"));
//								FileUtil.addRow(li, "HoagContinuation");
								saveFile();
								return ResultEnum.submitApproval.toString();
							} else {
								List li = new ArrayList();
								refdetails.put("details", "denied");
								li.addAll(refdetails.values());

								FileUtil.PrintJsonFile(driver, refdetails, data.get("name"));
//								FileUtil.addRow(li, "HoagContinuation");

								return ResultEnum.denied.toString();
							}
						} else {
							logger.info("approval process");
							saveFile();
							return ResultEnum.approval.toString();
						}

					}

				}
				driver.switchTo().defaultContent();
				util.action(driver, senum.switchToIframe, "//*[@id='sBodyFrame']", "", 0, "switching frame");

			} catch (Exception e) {
				e.printStackTrace();
				diagnosis = new ArrayList<String>();
				logger.info(" extra auth in the hoag");
				logger.info("id is not there in rt" + id);
				logger.info("approval process");
				Actions action = new Actions(driver);
				action.doubleClick(refEles.get(x)).build().perform();

				util.action(driver, senum.waitWhilelLoad, Hoag.Xrefloading, "", 0, "waiting while loading");

				Thread.sleep(1500);
				util.action(driver, senum.switchToIframe, "//*[@id='ReportFrame']", "", 0, "switching frame");
				util.action(driver, senum.waitUntilLoad, "//*[text()='Diagnoses']", "", 0, "waiting while loading");

				validateDx(dxcodes, driver);
				refdetails = getRefDetails(driver);
				driver.switchTo().defaultContent();
				util.action(driver, senum.switchToIframe, "//*[@id='sBodyFrame']", "", 0, "switching frame");

				util.action(driver, senum.clickElementById, Hoag.Iback, "", 0, "clicking back");
				if (!dxobj.isEmpty()) {

					logger.info(" have to submit");
					String status = submit(driver, icdcodes, data);
					if (status.equals("Authorized")) {
						List li = new ArrayList();

						refdetails.put("details", "approved");
						li.addAll(refdetails.values());

						FileUtil.PrintJsonFile(driver, refdetails, data.get("name"));
//						FileUtil.addRow(li, "HoagContinuation");
						saveFile();
						return ResultEnum.submitApproval.toString();
					} else {
						List li = new ArrayList();
						refdetails.put("details", "denied");
						li.addAll(refdetails.values());

						FileUtil.PrintJsonFile(driver, refdetails, data.get("name"));
//						FileUtil.addRow(li, "HoagContinuation");

						return ResultEnum.denied.toString();
					}
//					return ResultEnum.Submit.toString();
				} else {
					if (!authObj.get(x).get("Remaining").equals("12")) {
						submit(driver, icdcodes, data);

					}
					logger.info("approval process");
					String status = driver.findElement(By.xpath("//*[text()='General Information']/parent::*/following-sibling::*//tbody/tr/td[5]")).getText();
					if (status.equals("Authorized")) {
						List li = new ArrayList();

						refdetails.put("details", "approved");
						li.addAll(refdetails.values());

						FileUtil.PrintJsonFile(driver, refdetails, data.get("name"));
//						FileUtil.addRow(li, "HoagContinuation");
						return ResultEnum.submitApproval.toString();
					} else {
						List li = new ArrayList();

						refdetails.put("details", "denied");
						li.addAll(refdetails.values());

						FileUtil.PrintJsonFile(driver, refdetails, data.get("name"));
//						FileUtil.addRow(li, "HoagContinuation");

						return ResultEnum.denied.toString();
					}

				}
			}

		}
		logger.info(" dx obj " + dxobj);

		if (!dxobj.isEmpty())

		{
			logger.info(" have to submit");
			String status = submit(driver, icdcodes, data);
			if (status.equals("Authorized")) {
				List li = new ArrayList();

				refdetails.put("details", "approved");
				li.addAll(refdetails.values());

				FileUtil.PrintJsonFile(driver, refdetails, data.get("name"));
//				FileUtil.addRow(li, "HoagContinuation");
				saveFile();
				return ResultEnum.submitApproval.toString();
			} else {
				List li = new ArrayList();
				refdetails.put("details", "denied");
				li.addAll(refdetails.values());

				FileUtil.PrintJsonFile(driver, refdetails, data.get("name"));
//				FileUtil.addRow(li, "HoagContinuation");

				return ResultEnum.denied.toString();
			}

//			submit(driver, dxobj);
		} else {

			logger.info("approval process");
			logger.info("no need to do anything already all updated");

		}
		return "";

	}

	public static List<String> dxobj = new ArrayList<String>();

	public static List<String> diagnosis = new ArrayList<String>();

	public static boolean validateDx(List<String> dxcodes, WebDriver driver) throws InterruptedException {
		logger.info("dxobj " + dxobj);
		List<WebElement> codes = (List<WebElement>) util.action(driver, senum.getWebElements, Hoag.Xdignosis, "", 0, "getting dxs");

		for (int x = 0; x < codes.size(); x++) {

			String temp = codes.get(x).getText().split(" ")[0];
			if (dxcodes.contains(temp)) {
				dxobj.remove(temp);
				diagnosis.add(temp);
				logger.info(" dx matched " + temp);
				logger.info(" dx obj " + dxobj);
			} else {
				logger.info(" dx not  matched " + temp);

			}

		}

		return false;
	}

	public static String submit(WebDriver driver, List<String> dx, Map<String, String> data) throws InterruptedException, FindFailed, AWTException {
		logger.info(" submitting ");
		logger.info("icd " + dx);
		DesktopKeyboard keyboard = new DesktopKeyboard();
		String freqsname = data.get("cptcodeFreq");

		String[] cpts = new String[] { "97110", "97140", "97530", "97112", "97014", "G0283", "97164" };
		Thread.sleep(2000);

		int freq = Integer.valueOf(freqsname.split("x")[1]);
		int series = Integer.valueOf(freqsname.split("x")[0]);
		String qty = (freq * series) + "";
		driver.switchTo().defaultContent();

		util.action(driver, senum.switchToIframe, "//*[@id='sHeaderFrame']", "", 0, "switching frame");

		util.action(driver, senum.clickElementById, Hoag.Ihome, "", 0, "clicking home");
		driver.switchTo().defaultContent();
		util.action(driver, senum.switchToIframe, "//*[@id='sBodyFrame']", "", 0, "switching frame");

		util.action(driver, senum.clickElement, Hoag.Xcreateref, "", 0, "clicking create referal");

		util.action(driver, senum.sendTextById, Hoag.Iprocdure, "97110", 0, "sending the procedure ");
		util.action(driver, senum.clickElementById, Hoag.Ibtn1, "97110", 0, "clicking next ");
		Thread.sleep(2000);
		util.action(driver, senum.clickElementById, Hoag.Ibtn1, "97110", 0, "clicking next ");
		util.action(driver, senum.waitUntilLoad, Hoag.Xgeneral, "", 0, "waiting ");
		Thread.sleep(1000);
		String type = "Comprehensive Outpatient Rehabilitation Facility (CORF) [AC1]";
		String reason = "Continuity of Care [" + qty;
		String refferby = "Prosport Physical Therapy, Inc [1003948]";
		util.action(driver, senum.sendTextById, Hoag.Itype, type, 0, "sending the type ");
		Thread.sleep(1000);
		util.action(driver, senum.sendTextById, Hoag.Ireason, reason, 0, "sending the reason ");

		util.action(driver, senum.sendTextById, Hoag.IrefBy, refferby, 0, "sending the IrefBy ");
		keyboard.keyDown(Key.ENTER);
		keyboard.keyUp(Key.ENTER);
		Thread.sleep(1000);
		util.action(driver, senum.waitWhilelLoad, Hoag.Xrefloading, "", 0, "waiting while loading");
		Thread.sleep(1000);
		util.action(driver, senum.clickElementById, Hoag.Ilocsearch, "97110", 0, "clicking location search ");
		Thread.sleep(1000);
		util.action(driver, senum.waitWhilelLoad, Hoag.Xrefloading, "", 0, "waiting while loading");
		Map<String, String> add = new HashMap<String, String>();

		add.put("COST1", "ProSport Physical Therapy & Performance - 2777 Bristol Street COSTA MESA CA 92626");

		add.put("FOOT1", "26672 Portola Pkwy Ste 116 Foothill Ranch CA 92610");
		add.put("LAGU1", "23001 Del Lago Drive Ste C-1 Laguna Hills CA 92653");
		add.put("MISS1", "26932 Oso Parkway Ste 260 Mission Viejo CA 92691");
		add.put("RANC2", "28924 S Western Ave Ste 101 Rancho Palos Verdes CA 90275");
		add.put("SANJ3", "32302 Camino Capistrano Ste 106 San Juan Capistrano CA 92675");

		util.action(driver, senum.sendTextById, Hoag.IprovAdd, add.get(data.get("provider")), 0, "sending the Provider addrs ");
		Thread.sleep(1000);
		util.action(driver, senum.sendTextById, Hoag.IprovAddTo, "PROSPORT", 0, "sending the Provider  TO ");

		keyboard.keyDown(Key.ENTER);
		keyboard.keyUp(Key.ENTER);
		Thread.sleep(2000);
		util.action(driver, senum.clickElement, "//*[@id='DefaultWTableID']//tr[3]/td[1]/input", "PROSPORT", 0, "selecting provider ");
		util.action(driver, senum.clickElement, Hoag.Xaccept, "", 0, "clicking accept ");
		util.action(driver, senum.clickElement, Hoag.Xyesbtn, "", 0, "clicking yes  ");
		util.action(driver, senum.clickElementById, Hoag.Ispeciality, "", 0, "clicking specility  ");
		driver.switchTo().defaultContent();
		util.action(driver, senum.switchToIframe, "//*[@id='RSIFrame']", "", 0, "switching frame");
		util.action(driver, senum.clickElement, "//*[@id='LookupTable']//tr[2]//a", "", 0, "clicking physical theraphy  ");

		driver.switchTo().defaultContent();
		util.action(driver, senum.switchToIframe, "//*[@id='sBodyFrame']", "", 0, "switching frame");
		util.action(driver, senum.sendTextById, Hoag.IprovAddress, add.get(data.get("provider")), 0, "sending the Provider addrs ");
		Thread.sleep(1000);
		util.action(driver, senum.clickElementById, Hoag.Ibtn2, "", 0, "clicking next ");
		Thread.sleep(1000);

		try {
			driver.findElement(By.xpath(Hoag.Ibtn2)).click();

		} catch (Exception e) {

		}

		for (int a = 1; a <= dx.size(); a++) {

			if (a != 1) {
				util.action(driver, senum.clickElementById, Hoag.Iadd, "", 0, "clicking add ");

			}
			Thread.sleep(1000);
			util.action(driver, senum.sendTextById, Hoag.Idignosis + a, dx.get(a - 1), 0, "sending the diagnosisi " + dx.get(a - 1));
			Thread.sleep(1000);
			util.action(driver, senum.clickElementById, Hoag.Iadd, "", 0, "clicking add ");
			util.action(driver, senum.waitWhilelLoad, Hoag.Xrefloading, "", 0, "waiting while loading");

		}

		for (int x = 1; x <= cpts.length; x++) {

			if (x == 7) {
				qty = "1";
			}
			if (x == 1) {

				util.action(driver, senum.sendTextById, Hoag.IcptQnty + x, qty, 0, "sending the cpts qnty ");
				util.action(driver, senum.clickElementById, "toolbar2button1", "", 0, "clicking add ");

			} else {
				util.action(driver, senum.sendTextById, Hoag.IcptPro + x, cpts[x - 1] + "", 0, "sending the cpts  ");

				util.action(driver, senum.sendTextById, Hoag.IcptQnty + x, qty, 0, "sending the cpts qnty for " + cpts[x - 1]);
				Thread.sleep(500);
				util.action(driver, senum.sendTextById, Hoag.IcptQnty + x, qty, 0, "sending the cpts qnty for " + cpts[x - 1]);
				util.action(driver, senum.clickElementById, "toolbar2button1", "", 0, "clicking add ");

			}

		}
		Thread.sleep(2000);

		try {

			driver.findElement(By.xpath("//*[@title='Add a new note']")).click();

		} catch (Exception e) {
			logger.info(" not clicked add note ");

		}
		String reasonfor = "Reason for Referral: continued care\r\n" + "Additional Detail: ";
		util.action(driver, senum.sendTextById, Hoag.Ireasonfor, reasonfor + "", 0, "sending the reason ");

		util.action(driver, senum.clickElementById, Hoag.Iaddfile, "", 0, "clicking add file ");

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
		screen.paste(data.get("pdfFilename"));
		screen.click(RootPath.BASE_DIR + "//resources//sikuli//hpsj//Open.png");
		Thread.sleep(1000);
		util.action(driver, senum.clickElementById, "toolbar4button2", "", 0, "clicking submit button ");
		Thread.sleep(2000);
		util.action(driver, senum.switchToIframe, "//*[@id='ReportFrame']", "", 0, "switching frame");
		util.action(driver, senum.waitUntilLoad, Hoag.Xauth, "", 0, "waiting auth");
		auth = (String) util.action(driver, senum.getText, Hoag.Xauth, "", 0, "getting auth");
		refdetails = getRefDetails(driver);
		auth = auth.split("#")[1];
		String status = driver.findElement(By.xpath("//*[text()='General Information']/parent::*/following-sibling::*//tbody/tr/td[5]")).getText();

		return status;

//		util.action(driver, senum.clickElementById, "toolbar3button2", "", 0, "clicking add file ");

	}

	public static Map<String, Object> getRefDetails(WebDriver driver) throws InterruptedException {
//		util.action(driver, senum.switchToIframe, "//*[@id='sBodyFrame']", "", 0, "switching frame");
//		util.action(driver, senum.switchToIframe, "//*[@id='ReportFrame']", "", 0, "switching frame");
		List<String> diagnosis = new ArrayList<String>();
		Map<String, Object> obj = new LinkedHashMap<String, Object>();
		String info = "//*[text()='General Information']/parent::*/following-sibling::*//tbody/tr/td[";
		String temp = driver.findElement(By.xpath(info + "1]")).getText();
		obj.put("authorized_from", temp);
		temp = driver.findElement(By.xpath(info + "2]")).getText();
		obj.put("authorized_expires", temp);
		temp = driver.findElement(By.xpath(info + "5]")).getText();
		obj.put("status", temp);
		temp = driver.findElement(By.xpath("//*[text()='Referred To']/parent::*/following-sibling::*//tr[2]//td[1]//span")).getText();
		// *[text()='Referred To']/parent::*/following-sibling::*//tr[2]//td[1]//span
		obj.put("refferedTo", temp);

		List<WebElement> codes = (List<WebElement>) util.action(driver, senum.getWebElements, Hoag.Xdignosis, "", 0, "getting dxs");

		for (int x = 0; x < codes.size(); x++) {

			temp = codes.get(x).getText().split(" ")[0];

			diagnosis.add(temp);

		}
		obj.put("diagnoses", diagnosis.toString());

		// *[text()='Services Details']/parent::*/following-sibling::*//tr/td

		List<WebElement> eles = driver.findElements(By.xpath("// *[text()='Services Details']/parent::*/following-sibling::*//tr/td[5]"));
		int largest = 0;
		for (int x = 0; x < eles.size(); x++) {
			int dummy = Integer.valueOf(eles.get(x).getText());
			if (dummy > largest) {
				largest = dummy;
			}
		}

		logger.info("largest " + largest);
		obj.put("req", largest);
		logger.info(obj);
		return obj;

	}

	public static void saveFile() throws AWTException, InterruptedException {
		Robot robot = new Robot();
		Screen screen = new Screen();

		String imgURL = RootPath.BASE_DIR + "\\resources\\sikuli\\chrome\\";
		SikuliUtility sikuli = new SikuliUtility();
		sikuli.click(imgURL + "print.png", 0);
		Thread.sleep(2000);
		if (sikuli.checkElement(imgURL + "printOptions.png")) {
			logger.info(" not in save as pdf");

			sikuli.click(imgURL + "printOptions.png", 0);
			sikuli.click(imgURL + "saveaspdf.png", 0);
			sikuli.click(imgURL + "save2.png", 0);
		} else {
			sikuli.click(imgURL + "save.png", 0);
		}

		Thread.sleep(1000);

		sikuli.waituntilLoad(imgURL + "savefile.png", 0);
		Thread.sleep(1000);
		sikuli.pressEnter();

		// retrieval
	}
}
// [{From Date=10-17-24, To Date=, Case=PT001, Auth=, Allowed=0, Remaining=0, Comment=}, {From Date=09-04-24, To Date=10-16-24, Case=PT001, Auth=3113998, Allowed=12, Remaining=0, Comment=Hoag}, {From Date=07-17-24, To Date=09-03-24, Case=PT001, Auth=3012108, Allowed=12, Remaining=0, Comment=Hoag}]
