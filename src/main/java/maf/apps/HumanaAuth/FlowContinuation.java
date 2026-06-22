package maf.apps.HumanaAuth;

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
import maf.apps.util.BOTloggerAPIService;
import maf.apps.util.Excel_create;
import maf.apps.util.FileUtil;
import maf.apps.util.RTService;
import maf.apps.util.ResultEnum;
import maf.apps.util.RootPath;
import maf.apps.util.SeleniumUtil;
import maf.apps.util.Selenium_Util;
import maf.apps.util.Selenium_Actions_Enum;
import maf.apps.util.Selenium_operations;

public class FlowContinuation implements Runnable {

	static WebDriver driver;
	List<Map<String, String>> exceldata;

	public FlowContinuation(WebDriver d) {
		driver = d;

	}

	String[] insurarername = { "SMCA2", "", "", "" };
	Selenium_Util Selenium_Util = new Selenium_Util();
	public static final Logger logger = LogManager.getLogger(FlowContinuation.class);
	static List<ArrayList<String>> tableRowValues = new ArrayList<ArrayList<String>>();
	static Map<String, Object> tableObj = null;
	static Map<String, Object> MainJSON = new LinkedHashMap<>();
	static Map<String, Object> tempMap = new LinkedHashMap<>();

	static LocalDateTime date = LocalDateTime.now();
	static DateTimeFormatter format1 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	static String formattedCurrentDate = date.format(format1);
	static String remote_excel_output = "\\\\10.1.240.40\\Waterlabs RCM\\AR\\TPGPT-Regular\\BOT Output\\"
			+ formattedCurrentDate;
	static Selenium_Actions_Enum senum;

	void extractData() throws Exception {
		Robot robot = new Robot();
		Main.isInitial = false;
		HumanaServices hm = new HumanaServices(driver);
		countUp(1);
		Actions actions = new Actions(driver);
		String folderPath = RootPath.BASE_DIR + "\\resources\\pdfDownload";
		logger.info("pdf path " + folderPath);
		Screen screen = new Screen();
		Properties p = new Properties();
		Properties configP = new Properties();

		LocalDate date = LocalDate.now();
		LocalDate next6day = date.plusDays(1);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
		String next6Date = formatter.format(next6day);
		String currentdate = formatter.format(date);

		System.out.println(" in the flow");
		FileReader countReader = new FileReader(RootPath.BASE_DIR + "/config.properties");
		System.out.println(" in the flow");
		configP.load(countReader);

		Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoadandClickElement, RaintreeLocator.XmoreFilters,
				null, 0, "clicking the more filters icon");
		Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoadandClearElement, RaintreeLocator.Xuser_owner,
				null, 0, "clearing the userid in User/Owner field");
		Selenium_Util.action(driver, Selenium_Actions_Enum.sendText, RaintreeLocator.Xinsurance, "ST279", 0,
				"Entering the insurance code");
		Thread.sleep(1000);
		Selenium_Util.action(driver, Selenium_Actions_Enum.clickElementById, "id.DASH!!7.btn_penAuthIns", "", 0,
				"include/exclude clicking");
		Thread.sleep(2000);
		WebElement exclude = driver.findElement(By.xpath("(//*[@id='id._RINC!!8.codelist']//table//tr)[2]"));
		actions.contextClick(exclude).perform();
		Thread.sleep(1000);

		Selenium_Util.action(driver, senum.clickElement, "(//*[@class='popup-item popup-item-openview']/span)[1]", "",
				0, "clicking add");

		Selenium_Util.action(driver, senum.sendText, "//*[@name='code']", "FT638", 0, " Ft638 ");
		Selenium_Util.action(driver, senum.clickElementById, "id._RCDS!!9.btnFilterSave", "", 0, " clicking save ");
		Thread.sleep(1500);
		Selenium_Util.action(driver, senum.clickElementById, "ToolbarButton.Save", "", 0, " clicking save ");
		Thread.sleep(1500);
		Selenium_Util.action(driver, Selenium_Actions_Enum.selectCheckboxByValue, RaintreeLocator.Xsubstatus,
				"Created from Follow Up", 0, "Selecting the status");
		Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoadandClickElement, RaintreeLocator.XapplyFilters,
				null, 0, "Clicking apply filters icon");
		Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoadandClickElement, RaintreeLocator.Xnad, null, 0,
				"clicking the NAD for ascending order date in the table");

		Thread.sleep(2000);
		int tableSize;

		Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, RaintreeLocator.Xtable, "", 0,
				" waiting for the table");
		List<WebElement> eles = (List<WebElement>) Selenium_Util.action(driver, Selenium_Actions_Enum.getWebElements,
				RaintreeLocator.Xtable, null, 0, "getting the list of input records");

		tableSize = eles.size();
		String[] headers = { "patientName", "bc", "payor", "NAD", "NAC", "Case", "Effective Date", "Sub status",
				"Request Date", "Allowed", "Remaining", "NextVisit", "LastVisit", "Member ID", "Authorization no",
				"Status", "Documents Uploaded?" };
		int pos = Integer.valueOf(configP.getProperty("rowPos"));
		Map<String, Object> body = new HashMap<>();
		body.put("bot_name", "Humana_CC");

		BOTloggerAPIService.getID(body);
		Map<String, Object> patch = new HashMap<>();
		patch.put("failed", 0);
		patch.put("success", 1);
		patch.put("is_bot_stopped", false);
		patch.put("client_name", "TerapyPartnersGroup");
		patch.put("bot_name", "Humana_CC");
		patch.put("instance", 1);
		patch.put("bot_processed", false);

		if (tableSize > 0) {

			// write for here
			while (true) {
				logger.info(" position => " + pos);
				Map<String, String> data = new HashMap<>();

				Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, RaintreeLocator.XRefresh, "", 0,
						" clicking refresh button ");
				eles = new ArrayList();
				Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, "//table/tbody", "", 0,
						" waiting for the table");
				eles = (List<WebElement>) Selenium_Util.action(driver, Selenium_Actions_Enum.getWebElements,
						RaintreeLocator.Xtable, null, 0, "getting web elements");
				Thread.sleep(1000);
				if (pos > eles.size()) {
					patch.put("bot_processed", true);
					BOTloggerAPIService.PutReq(patch);
					break;
				}
				String inputRow = RaintreeLocator.Xtable + "[" + pos + "]";
				Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, inputRow, "", 0, " wait for table ");
				logger.info(" eles size " + eles.size());
				if (eles.size() < pos && pos != 1) {
					logger.info(" breaking => end of row", eles.size());
					QuitBot quitBot = QuitBot.getObj();
					quitBot.setQuit(true);
					patch.put("bot_processed", true);
					BOTloggerAPIService.PutReq(patch);
					break;
				}
				if (eles.size() == 0) {
					logger.info(" breaking => 0 size");
					QuitBot quitBot = QuitBot.getObj();
					quitBot.setQuit(true);
					patch.put("bot_processed", true);
					BOTloggerAPIService.PutReq(patch);
					break;
				}
				logger.info(" yes  table", eles.size());

				File dir = new File(folderPath);
				for (File file : dir.listFiles())
					if (!file.isDirectory()) {
						file.delete();
					}

				Map obj = new LinkedHashMap();

				for (int x = 1; x <= 14; x++) {

					if (x == 13) {
						break;
					}
					String temp = driver.findElement(By.xpath("//table/tbody/tr[" + pos + "]/td[" + x + "]")).getText();
					System.out.println(temp);
					obj.put(headers[x - 1], temp.replace("\"", ""));

				}
				System.out.println(obj);
				Selenium_Util.action(driver, Selenium_Actions_Enum.doubleClickElement, inputRow, null, 0,
						"Double clicking the input row");

				Thread.sleep(1000);

				Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, "//*[text()='Authorization']", null,
						0, "waiting for the authorization");

				SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yy");

				String memberID = (String) Selenium_Util.action(driver, Selenium_Actions_Enum.getText,
						RaintreeLocator.XmembID, "", 0, "getting memeber id");

				String effectDate = (String) Selenium_Util.action(driver, Selenium_Actions_Enum.getTextFromInput,
						RaintreeLocator.XeffectDate, "", 0, "getting effect date");

				String pinNumber = (String) Selenium_Util.action(driver, Selenium_Actions_Enum.getText,
						RaintreeLocator.Xpin, "", 0, "getting pin number");
				String caseNumber = (String) Selenium_Util.action(driver, Selenium_Actions_Enum.getTextFromInput,
						RaintreeLocator.XcaseNum, "", 0, "getting case number");
				String fullname = (String) Selenium_Util.action(driver, Selenium_Actions_Enum.getText,
						RaintreeLocator.fullname, "", 0, "getting fullname");
				String dob = (String) Selenium_Util.action(driver, Selenium_Actions_Enum.getText, RaintreeLocator.dob,
						"", 0, "getting dob");
				String code =

						(String) Selenium_Util.action(driver, Selenium_Actions_Enum.getText, RaintreeLocator.code, null,
								0, "getting code");
				String patient_Name = (String) obj.get("patientName");
				obj.put("effectDate", effectDate);
				obj.put("Member ID", memberID);
				data.put("memberId", memberID.trim().replace(" ", ""));
				obj.put("Patient ID", pinNumber);
				data.put("dob", dob);
				data.put("fullname", fullname);
				data.put("code", code);
				data.put("effectDate", effectDate);
				data.put("pin", pinNumber);
				data.put("caseNumber", caseNumber);
				DateTimeFormatter inputFormatter = new DateTimeFormatterBuilder().appendPattern("MM-dd-")
						.appendValueReduced(ChronoField.YEAR, 2, 2, 1900).toFormatter();

				DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
				String currDate = outputFormatter.format(date);
				data.put("currentdate", currDate);
				logger.info(" curr " + currDate);
				// Parse and format the date
				LocalDate dateDob = LocalDate.parse(dob, inputFormatter);
				String newd = dateDob.format(outputFormatter);
				data.put("dob", newd);
				logger.info("dob " + newd);
				try {
					String temp = driver.findElement(By.xpath("//*[@name='auth']")).getAttribute("disabled");
					logger.info(temp);
					if (temp.equals("true")) {
						Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, RaintreeLocator.XbackButton,
								null, 0, "clicking the back buttton");

						pos++;
						obj.put("ICD codes", "");
						obj.put("Auth#", "");
						obj.put("details", "account using by someone");

						List li = new ArrayList();
						li.addAll(obj.values());

						FileUtil.PrintJsonFile(driver, obj,
								obj.get("patientName") + "-" + pinNumber + "-" + caseNumber);
						FileUtil.addRow(li, "HumanaAuth");
						BOTloggerAPIService.PutReq(patch);
						gobackToDashboard();
						countUp(pos);
						continue;
					}
				} catch (Exception e) {

				}

				WebElement nextActivityComment = null;
				try {
					nextActivityComment = driver.findElement(By.id("id.AUTH!!8.nextActivityComment"));

				} catch (Exception e) {

					nextActivityComment = driver
							.findElement(By.xpath("//*[@data-widgetname=\"nextactivitycomment\"]/div"));
				}

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
							String freq = list.get(m).toLowerCase();
							if (freq.matches("\\d[A-Za-z]\\d+")) {

								cptcodeFreq = list.get(m).replace(".", "").replace(" ", "").toLowerCase();

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
					List li = new ArrayList();
					li.addAll(obj.values());

					FileUtil.PrintJsonFile(driver, obj, obj.get("patientName") + "-" + pinNumber + "-" + caseNumber);
					FileUtil.addRow(li, "HumanaAuth");
					BOTloggerAPIService.PutReq(patch);
					pos++;
					gobackToDashboard();
					countUp(pos);
					continue;

				}

				if (cptcodeFreq == " " || Integer.valueOf(cptcodeFreq.split("x")[1]) > 12
						|| Integer.valueOf(cptcodeFreq.split("x")[0]) > 10) {

					obj.put("ICD codes", "");
					obj.put("Auth#", "");
					obj.put("details", "Not valid visit feq in comment box");
					List li = new ArrayList();
					li.addAll(obj.values());

					FileUtil.PrintJsonFile(driver, obj, patient_Name + "-" + pinNumber + "-" + caseNumber);
					FileUtil.addRow(li, "HumanaAuth");
					BOTloggerAPIService.PutReq(patch);
					pos++;
					gobackToDashboard();
					countUp(pos);
					data.put("cptcodeFreq", cptcodeFreq);
					continue;
				}

				if (caseNumber.equals("00000") || caseNumber.equals("0000") || caseNumber.equals("000")) {
					caseNumber = "PT001";
				}

				String serviceLine = (String) Selenium_Util.action(driver, Selenium_Actions_Enum.getTextFromInput,
						RaintreeLocator.XserviceLine, "", 0, "getting service line");
				if (serviceLine.length() <= 0) {
					obj.put("ICD codes", "");
					obj.put("Auth#", "");
					obj.put("details", "No Physical therapthy service line");
					List li = new ArrayList();
					li.addAll(obj.values());

					FileUtil.PrintJsonFile(driver, obj, patient_Name + "-" + pinNumber + "-" + caseNumber);
					FileUtil.addRow(li, "HumanaAuth");
					BOTloggerAPIService.PutReq(patch);
					pos++;
					gobackToDashboard();
					countUp(pos);
					continue;

				}
				serviceLine = serviceLine.split("    ")[1];
				data.put("serviceType", serviceLine);
				WebElement menu = driver.findElement(By.xpath("//i[@class='mdi mdi-menu']"));
				menu.click();
				String goToMenu = "//div[text()='GoTo Menu']";
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
				String caseStartDate = "";
				for (int visitCasecount = 1; visitCasecount <= totalcase.size(); visitCasecount++) {
					WebElement visitCase = driver
							.findElement(By.xpath("(//table[@class='w-listbox-table']/following::table)[1]/tbody[1]/tr["
									+ visitCasecount + "]/td[1]"));
					String visitCaseno = visitCase.getText();
					if (caseNumber.equals(visitCaseno)) {

						caseName = driver.findElement(
								By.xpath("(//table[@class='w-listbox-table']/following::table)[1]/tbody[1]/tr["
										+ visitCasecount + "]/td[1]//parent::tr/td[3]"))
								.getText();
						caseStartDate = driver.findElement(
								By.xpath("(//table[@class='w-listbox-table']/following::table)[1]/tbody[1]/tr["
										+ visitCasecount + "]/td[1]//parent::tr/td[4]"))
								.getText();
						actions.doubleClick(visitCase).build().perform();
						Thread.sleep(3000);
						caseMatch = true;
						break;
					} else {

					}
				}

				// for case name
				data.put("caseStartDate", caseStartDate);
				data.put("caseName", caseName);

				if (!caseMatch) {
					obj.put("ICD codes", "");
					obj.put("Auth#", "");
					List li = new ArrayList();

					obj.put("details", "no matched case number ");

					li.addAll(obj.values());
					FileUtil.PrintJsonFile(driver, obj, patient_Name + "-" + pinNumber + "-" + caseNumber);
					FileUtil.addRow(li, "HumanaAuth");
					BOTloggerAPIService.PutReq(patch);
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
				String icdCodes = "";
				List<String> dxcodes = new ArrayList<String>();

				try {
					temp = (String) Selenium_Util.action(driver, Selenium_Actions_Enum.getTextFromInput,
							RaintreeLocator.XicdCodesDaig1, null, 0, "getting icd codes");

					if (temp.length() != 0) {
						dxcodes.add(temp);
					}

				} catch (Exception e) {

					obj.put("details", "ICD codes not available");

				}
				try {
					temp = (String) Selenium_Util.action(driver, Selenium_Actions_Enum.getTextFromInput,
							RaintreeLocator.XicdCodesDaig2, null, 0, "getting icd codes");

					if (temp.length() != 0) {
						dxcodes.add(temp);
					}
				} catch (Exception e) {
				}
				try {
					temp = (String) Selenium_Util.action(driver, Selenium_Actions_Enum.getTextFromInput,
							RaintreeLocator.XicdCodesDaig3, null, 0, "getting icd codes");

					if (temp.length() != 0) {
						dxcodes.add(temp);
					}
				} catch (Exception e) {
				}
				try {
					temp = (String) Selenium_Util.action(driver, Selenium_Actions_Enum.getTextFromInput,
							RaintreeLocator.XicdCodesDaig4, null, 0, "getting icd codes");

					if (temp.length() != 0) {
						dxcodes.add(temp);
					}
				} catch (Exception e) {
				}
				String Provider =

						(String) Selenium_Util.action(driver, Selenium_Actions_Enum.getTextFromInput,
								RaintreeLocator.Xprovider, null, 0, "getting Provider");
				data.put("provider", Provider);
				boolean matches = dxcodes.get(0).matches("^.[0-9].*");
				if (!matches) {

					// back

					Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, RaintreeLocator.XbackButton, null,
							0, "clicking the back buttton");
					Thread.sleep(1000);

					try {

						WebElement yes = driver.findElement(By.xpath("//*[text()='es']"));
//						actions.doubleClick(yes).build();
						yes.click();
						System.out.println("clicked yes btn");
					} catch (Exception e) {

					}

					Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, RaintreeLocator.XpatientChart,
							null, 0, "waiting for the Patient Chart");

					Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, RaintreeLocator.XbackButton, null,
							0, "clicking the back buttton");
					Thread.sleep(1000);

					Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, RaintreeLocator.Xauthorization,
							null, 0, "waiting for the Authorization");

					Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, RaintreeLocator.XbackButton, null,
							0, "clicking the back buttton");
					Thread.sleep(1000);

					pos++;
					logger.info(" no icd codes ");
					obj.put("ICD codes", "");
					obj.put("Auth#", "");
					List li = new ArrayList();

					obj.put("details", "no icd codes");

					li.addAll(obj.values());
					System.out.println("list" + li);
					FileUtil.PrintJsonFile(driver, obj, patient_Name + "-" + pinNumber + "-" + caseNumber);
					FileUtil.addRow(li, "HumanaAuth");
					BOTloggerAPIService.PutReq(patch);
					Thread.sleep(1000);
					gobackToDashboard();
					countUp(pos);
					continue;
				} else {
					data.put("icdCodes", dxcodes.toString());

					String back = "//div[@class='backdown']";
					Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, RaintreeLocator.XbackButton, null,
							0, "clicking the back buttton");

					String documentsTasks = "//span[text()='ocuments/Tasks']";
					Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, documentsTasks, null, 0,
							"waiting for the document tasks");
					Selenium_Util.action(driver, Selenium_Actions_Enum.selectCheckboxByValue,
							RaintreeLocator.XprogressNoteType, "Progress Note", 0, "selecting the progress notes");

					Thread.sleep(1000);

					WebElement docEle = null;
					try {

						docEle = driver
								.findElement(By.xpath("//*[@data-widgetname='visitview']//table/tbody/tr[td[text()='"
										+ caseNumber + "'] and td[text()='Progress Note'] ]"));
					} catch (Exception e) {

					}
					if (docEle != null) {
						actions.doubleClick(docEle).build().perform();
					} else {

						pos++;
						logger.info(" no document found - no match with progress note ");
						obj.put("ICD codes", "");
						obj.put("Auth#", "");
						List li = new ArrayList();

						obj.put("details", "no document found - no match with progress note and case");

						li.addAll(obj.values());
						FileUtil.PrintJsonFile(driver, obj, patient_Name + "-" + pinNumber + "-" + caseNumber);
						FileUtil.addRow(li, "HumanaAuth");
						BOTloggerAPIService.PutReq(patch);
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

					Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, print, null, 0,
							"waiting for Print / Fax");

					Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, print, null, 0,
							"clicking the  Print / Fax");

					Thread.sleep(3000);
					robot.keyPress(KeyEvent.VK_CONTROL);
					robot.keyPress(KeyEvent.VK_P);
					robot.keyRelease(KeyEvent.VK_P);
					robot.keyRelease(KeyEvent.VK_CONTROL);
					String downloadPDF = "//div[@title='Download PDF']";

					Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, downloadPDF, null, 0,
							"waiting for downloadPDF");

					Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, downloadPDF, null, 0,
							"clicking the  downloadPDF");

					Thread.sleep(4500);
					while (true) {
						try {

							if ((screen
									.exists(RootPath.BASE_DIR + "\\resources\\sikuli\\Tpg\\pdfLoading.png") != null)) {
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
					Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, goback1, null, 0,
							"waiting for goback1");

					Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, goback1, null, 0,
							"clicking the  goback1");

					Thread.sleep(500);
					Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, goback2, null, 0,
							"waiting for goback2");

					Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, goback2, null, 0,
							"clicking the  goback2");
					Thread.sleep(1000);
					Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, goback3, null, 0,
							"waiting for goback2");

					Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, goback3, null, 0,
							"clicking the  goback2");
					Thread.sleep(1000);

					File mostRecentPdf = findMostRecentPdf(folderPath);

					if (mostRecentPdf != null) {
						System.out.println("Most recent PDF file: " + mostRecentPdf.getName());
					} else {
						System.out.println("No PDF files found in the specified folder.");
					}
					String filename = mostRecentPdf.getName();
					String pdfFilename = folderPath.concat("\\").concat(filename);
					System.out.println("pdfFilename is " + pdfFilename);
					data.put("pdfFilename", pdfFilename);
					data.put("serviceLine", serviceLine);
					Thread.sleep(1000);

					String age = (String) Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad,
							"//*[text()='Age']//parent::div//parent::div//following-sibling::*", null, 0,
							"waiting for the gotomenu");
					data.put("cptcodeFreq", cptcodeFreq);

					List<List> procedures = ProcedureCodes.getProcedures("continuation", serviceLine, cptcodeFreq);
					Selenium_Util.action(driver, Selenium_Actions_Enum.switchTab, goToMenu, null, 1,
							"switching to RT tab");

					String authentication = hm.Auth(data, dxcodes, procedures);
//			    AllcareSubmit.submit(driver, data, cptcodeFreq, effectDate, age);
					Thread.sleep(1000);
					Selenium_Util.action(driver, Selenium_Actions_Enum.switchTab, goToMenu, null, 0,
							"switching to RT tab");
					Thread.sleep(2000);
					logger.info("auth " + authentication);
//					String auth[] = waterlabs.hpsj.AuthorizationRequest.created_from_payoradded.HpsjSubmit
//							.submit(driver, data);

					Map<String, String> rtobj = HumanaServices.rtObj;
					rtobj.putAll(data);

					if (authentication.equals(ResultEnum.CERTIFIED.name())) {
//						String back3 = "//i[@id='backbutton-AUTH!!8']";
//						Operations.waitUntilLoad(back3, driver);
//						Operations.clickElement(back3, driver);
						RTservices.certified(driver, rtobj);
						gobackToDashboard();

						obj.put("Auth#", HumanaServices.auth);
						obj.put("details", authentication);
						List li = new ArrayList();
						li.addAll(obj.values());

						FileUtil.PrintJsonFile(driver, obj, patient_Name + "-" + pinNumber + "-" + caseNumber);
						FileUtil.addRow(li, "HumanaAuth");
						BOTloggerAPIService.PutReq(patch);
						gobackToDashboard();
						countUp(pos);
						continue;
					} else if (authentication.equals(ResultEnum.PENDED.name())) {
						RTservices.Pended(driver, rtobj);
						gobackToDashboard();

						obj.put("Auth#", HumanaServices.auth);
						obj.put("details", authentication);
						List li = new ArrayList();
						li.addAll(obj.values());

						FileUtil.PrintJsonFile(driver, obj, patient_Name + "-" + pinNumber + "-" + caseNumber);
						FileUtil.addRow(li, "HumanaAuth");
						BOTloggerAPIService.PutReq(patch);
						gobackToDashboard();
						countUp(pos);
						continue;

					} else if (authentication.equals(ResultEnum.AuthIsnotRequired.name())) {
						RTservices.AuthIsnotRequired(driver, rtobj);
						gobackToDashboard();
						pos++;
						obj.put("Auth#", "");
						obj.put("details", authentication);
						List li = new ArrayList();
						li.addAll(obj.values());

						FileUtil.PrintJsonFile(driver, obj, patient_Name + "-" + pinNumber + "-" + caseNumber);
						FileUtil.addRow(li, "HumanaAuth");
						BOTloggerAPIService.PutReq(patch);
						gobackToDashboard();
						countUp(pos);
						continue;
					} else if (authentication.equals(ResultEnum.NoRecordsFound.name())) {
						gobackToDashboard();
						pos++;
						obj.put("Auth#", "");
						obj.put("details", authentication.toString());
						List li = new ArrayList();
						li.addAll(obj.values());

						FileUtil.PrintJsonFile(driver, obj, patient_Name + "-" + pinNumber + "-" + caseNumber);
						FileUtil.addRow(li, "HumanaAuth");
						BOTloggerAPIService.PutReq(patch);
						gobackToDashboard();
						countUp(pos);
						continue;
					}

					else {
						gobackToDashboard();
						pos++;
						String authNum = HumanaServices.authnbr;
						String reqDate = currentdate;
						obj.put("Auth#", authNum);
						obj.put("details", "unknown error");
						FileWriter file = new FileWriter(
								RootPath.BASE_DIR + "\\resources\\output\\json\\" + patient_Name + ".json");
						ObjectMapper mapper = new ObjectMapper();
						String outputjson = mapper.writeValueAsString(obj);
						file.write(outputjson);
						file.flush();

						List li = new ArrayList();

						li.addAll(obj.values());

						FileUtil.PrintJsonFile(driver, obj, patient_Name + "-" + pinNumber + "-" + caseNumber);
						FileUtil.addRow(li, "HumanaAuth");
						BOTloggerAPIService.PutReq(patch);
					}

//					 
				}
			}
		}
		((JavascriptExecutor) driver).executeScript("window.open();");
		Thread.sleep(2000);
		driver.get("https://forms.office.com/r/AhyVQ7ZfJs");
		SeleniumUtil.action(driver, Selenium_Actions_Enum.switchTab, null, null, null, null, null, 2,
				"Switching to the submit  Tab 2");

		Thread.sleep(1000);

		SeleniumUtil.action(driver, Selenium_Actions_Enum.waitUntilLoadandClickElement,
				"//div[@data-automation-id='choiceItem'][1]//input", null, null, null, null, 2,
				"clicking the browse element to upload pdf");
		Thread.sleep(1000);
		SeleniumUtil.action(driver, Selenium_Actions_Enum.waitUntilLoadandClickElement, "//button[text()='Submit']",
				null, null, null, null, 2, "clicking the browse element to upload pdf");
		Thread.sleep(2000);

		System.out.println("======The end=====");
		System.out.println("======The end=====");
		System.out.println("======The end=====");
		System.out.println("======The end=====");

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
