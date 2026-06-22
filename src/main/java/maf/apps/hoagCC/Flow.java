package maf.apps.hoagCC;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.sikuli.script.Screen;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.Deserializers.Base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import maf.apps.base.DriverManager;
import maf.apps.base.QuitBot;
import maf.apps.master.selenium.elementspath.RaintreeLocator;
import maf.apps.util.Excel_create;
import maf.apps.util.FileUtil;
import maf.apps.util.RTService;
import maf.apps.util.ResultEnum;
import maf.apps.util.RootPath;
import maf.apps.util.SeleniumUtil;
import maf.apps.util.Selenium_Util;
import maf.apps.util.Selenium_Actions_Enum;
import maf.apps.util.Selenium_operations;

public class Flow implements Runnable {

	static WebDriver driver;
	List<Map<String, String>> exceldata;

	public Flow(WebDriver d) {
		driver = d;

	}

	String[] insurarername = { "SMCA2", "", "", "" };
	Selenium_Util Selenium_Util = new Selenium_Util();
	public static final Logger logger = LogManager.getLogger(Flow.class);
	static List<ArrayList<String>> tableRowValues = new ArrayList<ArrayList<String>>();
	static Map<String, Object> tableObj = null;
	static Map<String, Object> MainJSON = new LinkedHashMap<>();
	static Map<String, Object> tempMap = new LinkedHashMap<>();

	static LocalDateTime date = LocalDateTime.now();
	static DateTimeFormatter format1 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	static String formattedCurrentDate = date.format(format1);
	static String remote_excel_output = "\\\\10.1.240.40\\Waterlabs RCM\\AR\\TPGPT-Regular\\BOT Output\\" + formattedCurrentDate;
	static Selenium_Actions_Enum senum;

	void extractData() throws Exception {
		Robot robot = new Robot();
		try {
			driver.manage().window().maximize();
		} catch (Exception e) {

		}
		Actions actions = new Actions(driver);
		String folderPath = RootPath.BASE_DIR + "\\resources\\pdfDownload";
		logger.info("pdf path " + folderPath);
		Screen screen = new Screen();
		Properties p = new Properties();
		Properties configP = new Properties();

		LocalDate date = LocalDate.now();
		LocalDate next6day = date.plusDays(6);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
		String next6Date = formatter.format(next6day);
		String currentdate = formatter.format(date);

		logger.info(" in the flow");
		FileReader countReader = new FileReader(RootPath.BASE_DIR + "/config.properties");
		logger.info(" in the flow");
		configP.load(countReader);
		String status = DriverManager.getpropertyvalue("subStatus");

		Map<String, String> creds = new HashMap<>();
		creds.put("userName", "RThoagUser");
		creds.put("pass", "RThoagPass");

		RTService.login(driver, creds);

		Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoadandClickElement, RaintreeLocator.Xdashboard, null, 0, "clicking the Dashboard menu");
		Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoadandClickElement, RaintreeLocator.Xauth, null, 0, "clicking the auth icon");

		Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoadandClickElement, RaintreeLocator.XmoreFilters, null, 0, "clicking the more filters icon");
		// login sequoia
		((JavascriptExecutor) driver).executeScript("window.open();");
		Thread.sleep(2000);
		Selenium_Util.action(driver, senum.switchTab, "", "", 1, "switching the tabs to 1");
		HoagServices.login(driver);
		Selenium_Util.action(driver, senum.switchTab, "", "", 0, "switching the tabs to 1");

		Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoadandClearElement, RaintreeLocator.Xuser_owner, null, 0, "clearing the userid in User/Owner field");

		Selenium_Util.action(driver, Selenium_Actions_Enum.sendText, RaintreeLocator.Xinsurance, "PS244", 0, "Entering the insurance code");

		Selenium_Util.action(driver, Selenium_Actions_Enum.selectCheckboxByValue, RaintreeLocator.Xsubstatus, status, 0, "Selecting the status");
		Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoadandClickElement, RaintreeLocator.XapplyFilters, null, 0, "Clicking apply filters icon");
		Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoadandClickElement, RaintreeLocator.Xnad, null, 0, "clicking the NAD for ascending order date in the table");

		Thread.sleep(2000);
		int tableSize;

		Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, RaintreeLocator.Xtable, "", 0, " waiting for the table");
		List<WebElement> eles = (List<WebElement>) Selenium_Util.action(driver, Selenium_Actions_Enum.getWebElements, RaintreeLocator.Xtable, null, 0, "getting the list of input records");

		tableSize = eles.size();
		String[] headers = { "patientName", "bc", "payor", "NAD", "NAC", "Case", "Effective Date", "Sub status", "Request Date", "Allowed", "Remaining", "NextVisit", "LastVisit", "Member ID",
				"Authorization no", "Status", "Documents Uploaded?" };
		int pos = Integer.valueOf(configP.getProperty("rowPos"));
		if (tableSize > 0) {

			// write for here
			while (true) {
				logger.info(" position => " + pos);
				Map<String, String> data = new HashMap<>();

				Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, RaintreeLocator.XRefresh, "", 0, " clicking refresh button ");
				eles = new ArrayList();
				eles = (List<WebElement>) Selenium_Util.action(driver, Selenium_Actions_Enum.getWebElements, RaintreeLocator.Xtable, null, 0, "getting web elements");
				Thread.sleep(1000);
				if (pos > eles.size()) {
					break;
				}
				String inputRow = RaintreeLocator.Xtable + "[" + pos + "]";
				Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, inputRow, "", 0, " wait for table ");
				logger.info(" eles size " + eles.size());
				if (eles.size() < pos && pos != 1) {
					logger.info(" breaking => end of row", eles.size());
					QuitBot quitBot = QuitBot.getObj();
					quitBot.setQuit(true);
					break;
				}
				if (eles.size() == 0) {
					logger.info(" breaking => 0 size");
					QuitBot quitBot = QuitBot.getObj();
					quitBot.setQuit(true);
					break;
				}
				logger.info(" yes  table", eles.size());

				File dir = new File(folderPath);
				for (File file : dir.listFiles())
					if (!file.isDirectory()) {
						file.delete();
					}

				Map<String, Object> obj = new LinkedHashMap();

				for (int x = 1; x <= 14; x++) {

					if (x == 13) {
						break;
					}
					String temp = driver.findElement(By.xpath("//table/tbody/tr[" + pos + "]/td[" + x + "]")).getText();
					logger.info(temp);
					obj.put(headers[x - 1], temp.replace("\"", ""));

				}
				logger.info(obj);
				Selenium_Util.action(driver, Selenium_Actions_Enum.doubleClickElement, inputRow, null, 0, "Double clicking the input row");

				Thread.sleep(1000);

				Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, "//*[text()='Authorization']", null, 0, "waiting for the authorization");

				SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yy");

				String memberID = (String) Selenium_Util.action(driver, Selenium_Actions_Enum.getText, RaintreeLocator.XmembID, "", 0, "getting memeber id");

				String effectDate = (String) Selenium_Util.action(driver, Selenium_Actions_Enum.getTextFromInput, RaintreeLocator.XmembID, "", 0, "getting effect date");

				String pinNumber = (String) Selenium_Util.action(driver, Selenium_Actions_Enum.getText, RaintreeLocator.Xpin, "", 0, "getting pin number");
				String caseNumber = (String) Selenium_Util.action(driver, Selenium_Actions_Enum.getTextFromInput, RaintreeLocator.XcaseNum, "", 0, "getting case number");
				String fullname = (String) Selenium_Util.action(driver, Selenium_Actions_Enum.getText, RaintreeLocator.fullname, "", 0, "getting fullname");
				String dob = (String) Selenium_Util.action(driver, Selenium_Actions_Enum.getText, RaintreeLocator.dob, "", 0, "getting dob");
				String patient_Name = (String) obj.get("patientName");
				obj.put("effectDate", effectDate);
				obj.put("Member ID", memberID);
				data.put("memberId", memberID.trim().replace(" ", ""));
				obj.put("Patient ID", pinNumber);
				data.put("name", fullname);
				DateTimeFormatter inputFormatter = new DateTimeFormatterBuilder().appendPattern("MM-dd-").appendValueReduced(ChronoField.YEAR, 2, 2, 1900).toFormatter();

				DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("M/d/yyyy");

				// Parse and format the date
				LocalDate dateDob = LocalDate.parse(dob, inputFormatter);
				String newd = dateDob.format(outputFormatter);

				logger.info("Formatted Date: " + newd);
				data.put("dob", newd);
				data.put("fullname", fullname);
				data.put("currentDate", currentdate);
				try {
					String temp = driver.findElement(By.xpath("//*[@name='auth']")).getAttribute("disabled");
					logger.info(temp);
					if (temp.equals("true")) {
						Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, RaintreeLocator.XbackButton, null, 0, "clicking the back buttton");

						pos++;
						obj.put("ICD codes", "");
						obj.put("Auth#", "");
						obj.put("details", "account using by someone");

						List li = new ArrayList();
						li.addAll(obj.values());

						FileUtil.PrintJsonFile(driver, obj, obj.get("patientName") + "-" + pinNumber + "-" + caseNumber);
						FileUtil.addRow(li, "HoagContinuation");
						gobackToDashboard();
						countUp(pos);
						continue;
					}
				} catch (Exception e) {

				}

				WebElement nextActivityComment = driver.findElement(By.id("id.AUTH!!8.nextActivityComment"));
				String temp = nextActivityComment.getText();
				String[] lines = temp.split("\\r?\\n");
				boolean visits = false;
				String cptcodeFreq = "";
//
				for (int x = 0; x < lines.length; x++) {

					ArrayList<String> list = new ArrayList<>(Arrays.asList(lines[x].split(" ")));
					logger.info(lines[x]);

					for (int m = 0; m < list.size(); m++) {
						try {
							if (list.get(m).contains("2x") || list.get(m).contains("2X") || list.get(m).contains("1x") || list.get(m).contains("1X")) {

								cptcodeFreq = list.get(m).replace(".", "").replace(" ", "").toLowerCase().replace(",", "");

								logger.info("cptcodeFreq " + cptcodeFreq);
								visits = true;
								break;

							}

						} catch (Exception e) {

						}
					}
					if (visits) {
						break;
					}
				}

				if (!visits) {
					obj.put("ICD codes", "");
					obj.put("Auth#", "");
					obj.put("details", "No  visit freq in comment box");
					List<Object> li = new ArrayList<>();
					li.addAll(obj.values());

					FileUtil.PrintJsonFile(driver, obj, obj.get("patientName") + "-" + pinNumber + "-" + caseNumber);
					logger.info(" list " + li);
					FileUtil.addRow(li, "HoagContinuation");
					pos++;
					gobackToDashboard();
					countUp(pos);
					continue;

				}

				if (cptcodeFreq == " " || Integer.valueOf(cptcodeFreq.split("x")[1]) > 12 || Integer.valueOf(cptcodeFreq.split("x")[0]) > 3) {

					obj.put("ICD codes", "");
					obj.put("Auth#", "");
					obj.put("details", "Not valid visit feq in comment box");
					List li = new ArrayList();
					li.addAll(obj.values());

					FileUtil.PrintJsonFile(driver, obj, patient_Name + "-" + pinNumber + "-" + caseNumber);
					FileUtil.addRow(li, "HoagContinuation");
					pos++;
					gobackToDashboard();
					countUp(pos);
					data.put("cptcodeFreq", cptcodeFreq);
					continue;
				}
				if (caseNumber.equals("00000") || caseNumber.equals("0000") || caseNumber.equals("000")) {
					caseNumber = "PT001";
				}
				// for getting patient menu data
				WebElement menu = driver.findElement(By.xpath("//i[@class='mdi mdi-menu']"));
				menu.click();
				String goToMenu = "//div[text()='GoTo Menu']";
				String patientToMenu = "//*[text()='atient Menu']";

				Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, goToMenu, null, 0, "waiting for the gotomenu");
				Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, goToMenu, null, 0, "waiting for the gotomenu");

				String patientChart = "//div[@id='id.QKS!!9.1!sGotoMenuitem_patChart']/div[1]/span[1]";

				Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, patientToMenu, null, 0, "waiting for the patientmenu");
				Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, patientToMenu, null, 0, "waiting for the patientmenu");
				String insurance = "//*[text()='nsurance / Payor']";
				Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, insurance, null, 0, "clicking the insurance/payor");

				String caseXpath = "//div[text()='" + caseNumber + "']";

				if (caseNumber.equals("PT001")) {
					caseXpath = "//*[@class='screen screen-active screen-List']//div[text()='00000'] | //*[@class='screen screen-active screen-List']//div[text()='PT001']";
				} else {
					caseXpath = "//*[@class='screen screen-active screen-List']//div[text()='Hoag Clinic']";

				}
				// *[text()='Insurance Information'] //*[@class='screen screen-active
				// screen-List']//div[text()='00000']
				Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, "//*[text()='Insurance Information']", null, 0, "waiting for the Insurance Information ");

				// Selenium_Util.action(driver, Selenium_Actions_Enum.doubleClickElement,
				// caseXpath, null, 0, "clicking the insurance/payor");

				boolean casem = false;

				try {
					WebElement ele = driver.findElement(By.xpath(caseXpath));
					Actions ac = new Actions(driver);
					ac.doubleClick(ele).build().perform();

				} catch (Exception e) {
					obj.put("ICD codes", "");
					obj.put("Auth#", "");
					List li = new ArrayList();

					obj.put("details", "no matched case number ");

					li.addAll(obj.values());
					FileUtil.PrintJsonFile(driver, obj, patient_Name + "-" + pinNumber + "-" + caseNumber);
					FileUtil.addRow(li, "HoagContinuation");
					driver.findElement(By.xpath(RaintreeLocator.XbackButton)).click();

					Thread.sleep(1000);
					driver.findElement(By.xpath(RaintreeLocator.XbackButton)).click();
					driver.findElement(By.xpath(RaintreeLocator.XbackButton)).click();
					pos++;
					gobackToDashboard();
					countUp(pos);
					continue;

				}
				Thread.sleep(1000);
				Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, RaintreeLocator.XinsuranceTable, null, 0, "waiting for table");

				List<Map<String, String>> insuranceobj = (List<Map<String, String>>) Selenium_Util.action(driver, Selenium_Actions_Enum.getTable, RaintreeLocator.XinsuranceTable, null, 0,
						"getting the insurance data");
				logger.info("insurance");
				logger.info(insuranceobj);

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
				menu.click();

				Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, goToMenu, null, 0, "waiting for the gotomenu");

				Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, goToMenu, null, 0, "waiting for the authorization");

				Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, patientChart, null, 0, "waiting for the patientChart");

				Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, patientChart, null, 0, "waiting for the patientChart");
				String visitInfo = "//span[text()='isit Info']";

				Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, visitInfo, null, 0, "waiting for the visitInfo");
				List<WebElement> totalcase = driver.findElements(By.xpath("(//table[@class='w-listbox-table']/following::table)[1]/tbody[1]/tr"));
				boolean caseMatch = false;

				String caseName = "";
				for (int visitCasecount = 1; visitCasecount <= totalcase.size(); visitCasecount++) {
					WebElement visitCase = driver.findElement(By.xpath("(//table[@class='w-listbox-table']/following::table)[1]/tbody[1]/tr[" + visitCasecount + "]/td[1]"));
					String visitCaseno = visitCase.getText();
					if (caseNumber.equals(visitCaseno)) {
						actions.doubleClick(visitCase).build().perform();
						Thread.sleep(3000);
						caseName = driver.findElement(By.xpath("(//table[@class='w-listbox-table']/following::table)[1]/tbody[1]/tr[" + visitCasecount + "]/td[1]//parent::tr/td[3]")).getText();

						if (caseName.contains("Aquatic")) {
							cptcodeFreq = cptcodeFreq;
						}
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
					FileUtil.addRow(li, "HoagContinuation");
					driver.findElement(By.xpath(RaintreeLocator.XbackButton)).click();

					Thread.sleep(1000);
					driver.findElement(By.xpath(RaintreeLocator.XbackButton)).click();
					driver.findElement(By.xpath(RaintreeLocator.XbackButton)).click();
					pos++;
					gobackToDashboard();
					countUp(pos);
					continue;
				}

				Thread.sleep(2000);
				List<String> icdCodes = new ArrayList<String>();
				try {
					temp = (String) Selenium_Util.action(driver, Selenium_Actions_Enum.getTextFromInput, RaintreeLocator.XicdCodesDaig1, null, 0, "getting icd codes");

					if (temp.length() != 0) {
						icdCodes.add(temp);
					}

				} catch (Exception e) {

					obj.put("details", "ICD codes not available");

				}
				try {
					temp = (String) Selenium_Util.action(driver, Selenium_Actions_Enum.getTextFromInput, RaintreeLocator.XicdCodesDaig2, null, 0, "getting icd codes");

					if (temp.length() != 0) {
						icdCodes.add(temp);
					}
				} catch (Exception e) {
				}
				try {
					temp = (String) Selenium_Util.action(driver, Selenium_Actions_Enum.getTextFromInput, RaintreeLocator.XicdCodesDaig3, null, 0, "getting icd codes");

					if (temp.length() != 0) {
						icdCodes.add(temp);
					}
				} catch (Exception e) {
				}
				try {
					temp = (String) Selenium_Util.action(driver, Selenium_Actions_Enum.getTextFromInput, RaintreeLocator.XicdCodesDaig4, null, 0, "getting icd codes");

					if (temp.length() != 0) {
						icdCodes.add(temp);
					}
				} catch (Exception e) {
				}

//					icdCodes = (String) Selenium_Util.action(driver, Selenium_Actions_Enum.getText, RaintreeLocator.XicdCodes, null, 0, "getting icd codes");

				obj.put("ICD codes", icdCodes.toString());
				logger.info("ICD codes are " + icdCodes);

				String Provider =

						(String) Selenium_Util.action(driver, Selenium_Actions_Enum.getTextFromInput, RaintreeLocator.Xprovider, null, 0, "getting Provider");
				data.put("provider", Provider);

				boolean matches = icdCodes.get(0).matches("^.[0-9].*");
				if (!matches) {

					// back

					Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, RaintreeLocator.XbackButton, null, 0, "clicking the back buttton");
					Thread.sleep(1000);

					try {

						WebElement yes = driver.findElement(By.xpath("//*[text()='es']"));
//						actions.doubleClick(yes).build();
						yes.click();
						logger.info("clicked yes btn");
					} catch (Exception e) {

					}

					Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, RaintreeLocator.XpatientChart, null, 0, "waiting for the Patient Chart");

					Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, RaintreeLocator.XbackButton, null, 0, "clicking the back buttton");
					Thread.sleep(1000);

					Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, RaintreeLocator.Xauthorization, null, 0, "waiting for the Authorization");

					Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, RaintreeLocator.XbackButton, null, 0, "clicking the back buttton");
					Thread.sleep(1000);

					pos++;
					logger.info(" no icd codes ");
					obj.put("ICD codes", "");
					obj.put("Auth#", "");
					List li = new ArrayList();

					obj.put("details", "no icd codes");

					li.addAll(obj.values());
					logger.info("list" + li);
					FileUtil.PrintJsonFile(driver, obj, patient_Name + "-" + pinNumber + "-" + caseNumber);
					FileUtil.addRow(li, "HoagContinuation");
					Thread.sleep(1000);
					gobackToDashboard();
					countUp(pos);
					continue;
				} else {
					data.put("icdCodes", icdCodes.toString());
					String back = "//div[@class='backdown']";
					Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, RaintreeLocator.XbackButton, null, 0, "clicking the back buttton");

					String documentsTasks = "//span[text()='ocuments/Tasks']";
					Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, documentsTasks, null, 0, "waiting for the document tasks");
					Selenium_Util.action(driver, Selenium_Actions_Enum.selectCheckboxByValue, RaintreeLocator.XprogressNoteType, "Progress Note", 0, "selecting the progress notes");

					Thread.sleep(1000);

					WebElement docEle = null;
					try {

						docEle = driver.findElement(By.xpath("//*[@data-widgetname='visitview']//table/tbody/tr[td[text()='" + caseNumber + "'] and td[text()='Progress Note'] ]"));
					} catch (Exception e) {

					}
					if (docEle != null) {
						actions.doubleClick(docEle).build().perform();
					} else {

						pos++;
						logger.info(" no document found - no match with progress note& initial Eval and case ");
						obj.put("ICD codes", "");
						obj.put("Auth#", "");
						List li = new ArrayList();

						obj.put("details", "no document found - no match with progress note and case");

						li.addAll(obj.values());
						FileUtil.PrintJsonFile(driver, obj, patient_Name + "-" + pinNumber + "-" + caseNumber);
						FileUtil.addRow(li, "HoagContinuation");
						Thread.sleep(1000);
						gobackToDashboard();
						countUp(pos);
						continue;

					}
//					Operations.clickElement("//span[text()='View ']", driver);

					while (true) {

						try {
							driver.findElement(By.xpath("//span[text()='Preview ']")).click();
							break;
						} catch (Exception e) {

						}
						try {
							driver.findElement(By.xpath("//span[text()='View ']")).click();
							break;
						} catch (Exception e) {

						}
					}

					Thread.sleep(1000);

					String print = "//div[text()='Print']";

					Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, print, null, 0, "waiting for Print / Fax");

					Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, print, null, 0, "clicking the  Print / Fax");

					Thread.sleep(3000);
					robot.keyPress(KeyEvent.VK_CONTROL);
					robot.keyPress(KeyEvent.VK_P);
					robot.keyRelease(KeyEvent.VK_P);
					robot.keyRelease(KeyEvent.VK_CONTROL);
					String downloadPDF = "//div[@title='Download PDF']";

					Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, downloadPDF, null, 0, "waiting for downloadPDF");

					Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, downloadPDF, null, 0, "clicking the  downloadPDF");

					Thread.sleep(4500);
					while (true) {
						try {

							if ((screen.exists(RootPath.BASE_DIR + "\\resources\\sikuli\\goldcoast\\pdfLoading.png") != null)) {
								logger.info("pdf downloaded");
								break;
							} else {
								logger.info("not loaded");
							}
						} catch (Exception e) {

						}

					}
					String goback1 = "//div[@title='Close']";
					String goback2 = "//*[@class='backdown']";
					String goback3 = "//i[contains(@class,'backbtn mdi')]";
					Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, goback1, null, 0, "waiting for goback1");

					Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, goback1, null, 0, "clicking the  goback1");

					Thread.sleep(500);
					Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, goback2, null, 0, "waiting for goback2");

					Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, goback2, null, 0, "clicking the  goback2");
					Thread.sleep(1000);
					Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, goback3, null, 0, "waiting for goback2");

					Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, goback3, null, 0, "clicking the  goback2");
					Thread.sleep(1000);

					File mostRecentPdf = findMostRecentPdf(folderPath);

					if (mostRecentPdf != null) {
						logger.info("Most recent PDF file: " + mostRecentPdf.getName());
					} else {
						logger.info("No PDF files found in the specified folder.");
					}
					String filename = mostRecentPdf.getName();
					String pdfFilename = folderPath.concat("\\").concat(filename);
					logger.info("pdfFilename is " + pdfFilename);
					data.put("pdfFilename", pdfFilename);
					Thread.sleep(1000);

					String age = (String) Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, "//*[text()='Age']//parent::div//parent::div//following-sibling::*", null, 0,
							"waiting for the gotomenu");
					data.put("cptcodeFreq", cptcodeFreq);

					String result = "";
					Selenium_Util.action(driver, senum.switchTab, "", "", 1, "switching the tabs to 1");

					result = HoagServices.PreAuth(driver, data, insuranceobj, icdCodes);
					Selenium_Util.action(driver, senum.switchTab, "", "", 0, "switching the tabs to 1");

//							SequoiaService.submit(driver, data);
					String authentication = result;
//			    AllcareSubmit.submit(driver, data, cptcodeFreq, effectDate, age);
					Thread.sleep(1000);
					Selenium_Util.action(driver, Selenium_Actions_Enum.switchTab, goToMenu, null, 0, "switching to RT tab");
					Thread.sleep(2000);
					logger.info("auth " + authentication);
//					String auth[] = waterlabs.hpsj.AuthorizationRequest.created_from_payoradded.HpsjSubmit
//							.submit(driver, data);

					if (authentication.equals(ResultEnum.NotEligible.name()) || authentication.equals(ResultEnum.DuplicateError) || authentication.equals(ResultEnum.DxNotFound)) {
//						String back3 = "//i[@id='backbutton-AUTH!!8']";
//						Operations.waitUntilLoad(back3, driver);
//						Operations.clickElement(back3, driver);
						gobackToDashboard();
						pos++;
						obj.put("Auth#", "");
						List li = new ArrayList();
						li.addAll(obj.values());

						obj.put("details", authentication);
						FileUtil.PrintJsonFile(driver, obj, patient_Name + "-" + pinNumber + "-" + caseNumber);
						FileUtil.addRow(li, "HoagContinuation");
						gobackToDashboard();
						countUp(pos);
						continue;
					}

					String authNum = authentication;
					String reqDate = currentdate;
					obj.put("Auth#", HoagServices.auth);

					FileWriter file = new FileWriter(RootPath.BASE_DIR + "\\resources\\output\\json\\" + patient_Name + ".json");
					ObjectMapper mapper = new ObjectMapper();
					String outputjson = mapper.writeValueAsString(obj);
					file.write(outputjson);
					file.flush();
					Map<String, Object> refdetails = HoagServices.refdetails;
					// ResultEnum.Submit.toString()
					List li = new ArrayList();
					if (authentication.equals(ResultEnum.denied.name())) {

						// RTservices.denied(driver, refdetails);
						obj.put("details", "denied");
						li.addAll(obj.values());
						li.add("");

						FileUtil.addRow(li, "HoagContinuation");

					} else if (authentication.equals(ResultEnum.approval.name())) {

						RTservices.retrieval(driver, refdetails);

						obj.put("details", authentication);

						li.addAll(obj.values());
						li.add(" ");
						li.addAll(refdetails.values());
						FileUtil.addRow(li, "HoagContinuation");
					} else if (authentication.equals(ResultEnum.submitApproval.name())) {

						RTservices.submitApproval(driver, refdetails);

						obj.put("details", authentication);

						li.addAll(obj.values());
						li.add("");
						li.addAll(refdetails.values());
						FileUtil.addRow(li, "HoagContinuation");
					}

					FileUtil.PrintJsonFile(driver, obj, patient_Name + "-" + pinNumber + "-" + caseNumber);
//					FileUtil.addRow(li, "HoagContinuation");
					Thread.sleep(2000);

				}
			}
		}

		logger.info("======The end=====");
		logger.info("======The end=====");
		logger.info("======The end=====");
		logger.info("======The end=====");

		/// end of the loop

		QuitBot quitBot = QuitBot.getObj();
		quitBot.setQuit(true);
		logger.info("closing the browser");
	}

	@Override
	public void run() {
		try {
			extractData();
		} catch (Exception e) {

			e.printStackTrace();

		}
	}

	public static void gobackToDashboard() throws InterruptedException {
		Screen sc = new Screen();
		Actions actions = new Actions(driver);
		WebElement tmp = null;

		int errCounter = 10;

		while (true) {

			try {
				if (sc.exists(RootPath.BASE_DIR + "//resources//sikuli//Tpg//dashboard.png") != null) {

					logger.info("foyund");

					break;
				} else {
					try {

						driver.findElement(By.xpath(RaintreeLocator.XbackButton)).click();
						logger.info("back brn clicked");
						Thread.sleep(2000);
					} catch (Exception e2) {

					}
				}

			} catch (Exception e) {

			}

		}
	}

	static void countUp(int pos) {

		logger.info(pos);
		Properties properties = new Properties();

		properties.setProperty("rowPos", "" + pos);
		try (FileOutputStream out = new FileOutputStream("config.properties")) {
			properties.store(out, "Updated properties");

			logger.info("Property updated successfully.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static File findMostRecentPdf(String folderPath) {
		File folder = new File(folderPath);
		File[] pdfFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));

		if (pdfFiles == null || pdfFiles.length == 0) {
			return null;
		}

		return Arrays.stream(pdfFiles).max(Comparator.comparingLong(File::lastModified)).orElse(null);
	}
}
