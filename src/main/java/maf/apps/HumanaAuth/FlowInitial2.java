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
import maf.apps.util.Excel_create;
import maf.apps.util.FileUtil;
import maf.apps.util.RTService;
import maf.apps.util.ResultEnum;
import maf.apps.util.RootPath;
import maf.apps.util.SeleniumUtil;
import maf.apps.util.Selenium_Util;
import maf.apps.util.Selenium_Actions_Enum;
import maf.apps.util.Selenium_operations;

public class FlowInitial2 {

	static WebDriver driver;
	List<Map<String, String>> exceldata;

	public FlowInitial2(WebDriver d) {
		driver = d;

	}

	String[] insurarername = { "SMCA2", "", "", "" };
	Selenium_Util Selenium_Util = new Selenium_Util();
	public static final Logger logger = LogManager.getLogger(FlowInitial2.class);
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
		logger.error("pdf path " + folderPath);
		Screen screen = new Screen();
		Properties p = new Properties();
		Properties configP = new Properties();

		LocalDate date = LocalDate.now();
		LocalDate next6day = date.plusDays(6);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
		String next6Date = formatter.format(next6day);
		String currentdate = formatter.format(date);

		System.out.println(" in the flow");
		FileReader countReader = new FileReader(RootPath.BASE_DIR + "/config.properties");
		System.out.println(" in the flow");
		configP.load(countReader);
		String status = DriverManager.getpropertyvalue("subStatus");

		Map<String, String> creds = new HashMap<>();
		creds.put("userName", "RThumanauser");
		creds.put("pass", "RThumamapass");

		RTService.login(driver, creds);

		Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoadandClickElement, RaintreeLocator.Xdashboard, null, 0, "clicking the Dashboard menu");
		Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoadandClickElement, RaintreeLocator.Xauth, null, 0, "clicking the auth icon");

		Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoadandClickElement, RaintreeLocator.XmoreFilters, null, 0, "clicking the more filters icon");
		// login humana
		HumanaServices hm = new HumanaServices(driver);
		((JavascriptExecutor) driver).executeScript("window.open();");
		Thread.sleep(2000);
		Selenium_Util.action(driver, senum.switchTab, "", "", 1, "switching the tabs to 1");
//		hm.login();
		Selenium_Util.action(driver, senum.switchTab, "", "", 0, "switching the tabs to 1");

		Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoadandClearElement, RaintreeLocator.Xuser_owner, null, 0, "clearing the userid in User/Owner field");

		Selenium_Util.action(driver, Selenium_Actions_Enum.sendText, RaintreeLocator.Xinsurance, "ST279", 0, "Entering the insurance code");
// substatus
		Selenium_Util.action(driver, Selenium_Actions_Enum.selectCheckboxByValue, RaintreeLocator.Xsubstatus, "Created from case adding", 0, "Selecting the status");
		Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, RaintreeLocator.Xsubstatusinclude, "", 0, "include/exclude clicking");
		Thread.sleep(2000);
		WebElement exclude = driver.findElement(By.xpath("(//*[@id='id._RINC!!8.codelist']//table//tr)[2]"));
		actions.contextClick(exclude).perform();
		Thread.sleep(1000);

		Selenium_Util.action(driver, senum.clickElement, "(//*[@class='popup-item popup-item-openview']/span)[1]", "", 0, "clicking add");

		Selenium_Util.action(driver, senum.sendText, "//*[@name=\"code\"]", "PENDP", 0, " PENDP ");
		Selenium_Util.action(driver, senum.clickElementById, "id._RCDS!!9.btnFilterSave", "", 0, " clicking save ");
		Thread.sleep(1000);
		Selenium_Util.action(driver, senum.clickElementById, "ToolbarButton.Save", "", 0, " clicking save ");

		// end substatus
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
				logger.error(" position => " + pos);
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
				logger.error(" eles size " + eles.size());
				if (eles.size() < pos && pos != 1) {
					logger.error(" breaking => end of row", eles.size());
					QuitBot quitBot = QuitBot.getObj();
					quitBot.setQuit(true);
					break;
				}
				if (eles.size() == 0) {
					logger.error(" breaking => 0 size");
					QuitBot quitBot = QuitBot.getObj();
					quitBot.setQuit(true);
					break;
				}
				logger.error(" yes  table", eles.size());

				File dir = new File(folderPath);
				for (File file : dir.listFiles())
					if (!file.isDirectory()) {
//						file.delete();
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

				data.put("fullname", fullname);
				DateTimeFormatter inputFormatter = new DateTimeFormatterBuilder().appendPattern("MM-dd-").appendValueReduced(ChronoField.YEAR, 2, 2, 1900).toFormatter();

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
					logger.error(temp);
					if (temp.equals("true")) {
						Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, RaintreeLocator.XbackButton, null, 0, "clicking the back buttton");

						pos++;
						obj.put("ICD codes", "");
						obj.put("Auth#", "");
						obj.put("details", "account using by someone");

						List li = new ArrayList();
						li.addAll(obj.values());

						FileUtil.PrintJsonFile(driver, obj, obj.get("patientName") + "-" + pinNumber + "-" + caseNumber);
						FileUtil.addRow(li, "HumanaAuth");
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
//		for (int x = 0; x < lines.length; x++) {
//
//		    ArrayList<String> list = new ArrayList<>(Arrays.asList(lines[x].split(" ")));
//		    logger.error(lines[x]);
//
//		    for (int m = 0; m < list.size(); m++) {
//			try {
//			    if (list.get(m).contains("2x") || list.get(m).contains("2X") || list.get(m).contains("1x")
//				    || list.get(m).contains("1X")) {
//
//				cptcodeFreq = list.get(m).replace(".", "").replace(" ", "").toLowerCase();
//
//				logger.error("cptcodeFreq " + cptcodeFreq);
//				visits = true;
//				break;
//
//			    }
//
//			} catch (Exception e) {
//
//			}
//		    }
//		    if (visits) {
//			break;
//		    }
//		}
//		if (!visits) {
//		    obj.put("ICD codes", "");
//		    obj.put("Auth#", "");
//		    obj.put("details", "No  visit freq in comment box");
//		    List li = new ArrayList();
//		    li.addAll(obj.values());
//
//		    FileUtil.PrintJsonFile(driver, obj, obj.get("patientName") + "-" + pinNumber + "-" + caseNumber);
//		    FileUtil.addRow(li, "HumanaAuth");
//		    pos++;
//		    gobackToDashboard();
//		    countUp(pos);
//		    continue;
//
//		}

//		if (cptcodeFreq == " " || Integer.valueOf(cptcodeFreq.split("x")[1]) > 12) {
//
//		    obj.put("ICD codes", "");
//		    obj.put("Auth#", "");
//		    obj.put("details", "Not valid visit feq in comment box");
//		    List li = new ArrayList();
//		    li.addAll(obj.values());
//
//		    FileUtil.PrintJsonFile(driver, obj, patient_Name + "-" + pinNumber + "-" + caseNumber);
//		    FileUtil.addRow(li, "HumanaAuth");
//		    pos++;
//		    gobackToDashboard();
//		    countUp(pos);
//		    continue;
//		}

				if (caseNumber.equals("00000") || caseNumber.equals("0000") || caseNumber.equals("000")) {
					caseNumber = "PT001";
				}

				String serviceLine = (String) Selenium_Util.action(driver, Selenium_Actions_Enum.getTextFromInput, RaintreeLocator.XserviceLine, "", 0, "getting service line");
				if (serviceLine.length() <= 0) {
					obj.put("ICD codes", "");
					obj.put("Auth#", "");
					obj.put("details", "No Physical therapthy service line");
					List li = new ArrayList();
					li.addAll(obj.values());

					FileUtil.PrintJsonFile(driver, obj, patient_Name + "-" + pinNumber + "-" + caseNumber);
					FileUtil.addRow(li, "HumanaAuth");
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
				Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, goToMenu, null, 0, "waiting for the gotomenu");

				Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, goToMenu, null, 0, "waiting for the authorization");
				String patientChart = "//div[@id='id.QKS!!9.1!sGotoMenuitem_patChart']/div[1]/span[1]";

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
					FileUtil.addRow(li, "HumanaAuth");
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
				try {
//					icdCodes = Operations.getTextFromInput("//*[@name='diag1']", driver);
					icdCodes = (String) Selenium_Util.action(driver, Selenium_Actions_Enum.getText, RaintreeLocator.XicdCodes, null, 0, "getting icd codes");
					obj.put("ICD codes", icdCodes);

					System.out.println("ICD codes are " + icdCodes);
				} catch (Exception e) {
					icdCodes = "";

					obj.put("details", "ICD codes not available");

				}
				String Provider =

						(String) Selenium_Util.action(driver, Selenium_Actions_Enum.getTextFromInput, RaintreeLocator.Xprovider, null, 0, "getting Provider");
				data.put("provider", Provider);

				boolean matches = icdCodes.matches("^.[0-9].*");
				if (!matches) {

					// back

					Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, RaintreeLocator.XbackButton, null, 0, "clicking the back buttton");
					Thread.sleep(1000);

					try {

						WebElement yes = driver.findElement(By.xpath("//*[text()='es']"));
//						actions.doubleClick(yes).build();
						yes.click();
						System.out.println("clicked yes btn");
					} catch (Exception e) {

					}

					Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, RaintreeLocator.XpatientChart, null, 0, "waiting for the Patient Chart");

					Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, RaintreeLocator.XbackButton, null, 0, "clicking the back buttton");
					Thread.sleep(1000);

					Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, RaintreeLocator.Xauthorization, null, 0, "waiting for the Authorization");

					Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, RaintreeLocator.XbackButton, null, 0, "clicking the back buttton");
					Thread.sleep(1000);

					pos++;
					logger.error(" no icd codes ");
					obj.put("ICD codes", "");
					obj.put("Auth#", "");
					List li = new ArrayList();

					obj.put("details", "no icd codes");

					li.addAll(obj.values());
					System.out.println("list" + li);
					FileUtil.PrintJsonFile(driver, obj, patient_Name + "-" + pinNumber + "-" + caseNumber);
					FileUtil.addRow(li, "HumanaAuth");
					Thread.sleep(1000);
					gobackToDashboard();
					countUp(pos);
					continue;
				} else {

					data.put("icdCodes", icdCodes.toString());

					String provider = (String) Selenium_Util.action(driver, senum.getTextFromInput, RaintreeLocator.Xprovider, "", 0, "getting provider name");
					data.put("provider", provider);
					String back = "//div[@class='backdown']";
					Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, RaintreeLocator.XbackButton, null, 0, "clicking the back buttton");
					String documentsTasks = "//span[text()='ocuments/Tasks']";
					Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, documentsTasks, null, 0, "waiting for document tasks");

					Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, documentsTasks, null, 0, "clicking the  document tasks");

					while (true) {

						try {
							driver.findElement(By.xpath("(//table[@class='w-listbox-table']/following::table)[1]/tbody[1]/tr[1]/td[4]"));
							break;

						}

						catch (Exception e) {

						}
					}

					List<WebElement> cases = driver.findElements(By.xpath("(//table[@class='w-listbox-table']/following::table)[1]/tbody[1]/tr"));
					WebElement docEle = null;
					try {
						docEle = driver.findElement(By.xpath("(//table[@class='w-listbox-table']/following::table)[1]/tbody[1]/tr[td[text()='RX'] and td[text()='" + caseNumber
								+ "'] ] | (//table[@class='w-listbox-table']/following::table)[1]/tbody[1]/tr[td[text()='LEAD']  and td[text()='" + caseNumber + "']]"));

					} catch (Exception e) {

					}

					if (docEle != null) {
						actions.doubleClick(docEle).build().perform();
					} else {
						pos++;
						logger.error(" no document found - with case and lead/rx ");
						obj.put("ICD codes", "");
						obj.put("Auth#", "");
						List li = new ArrayList();

						obj.put("details", "nno document found - with case and lead/rx");

						li.addAll(obj.values());
						FileUtil.PrintJsonFile(driver, obj, patient_Name + "-" + pinNumber + "-" + caseNumber);
						FileUtil.addRow(li, "HumanaAuth");
						Thread.sleep(1000);
						gobackToDashboard();
						countUp(pos);
						continue;
					}

					String print = "//div[text()='Print / Fax']";

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
								logger.error("pdf downloaded");
								break;
							} else {
								logger.error("not loaded");
							}
						} catch (Exception e) {

						}

					}
					String goback1 = "//div[@title='Close']";
					String goback2 = "//i[@title='Click to go back, hold to see previous screens']";
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

					String age = (String) Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoad, "//*[text()='Age']//parent::div//parent::div//following-sibling::*", null, 0,
							"waiting for the gotomenu");
					List<List> procedures = ProcedureCodes.getProcedures("initial", serviceLine, cptcodeFreq);
					Selenium_Util.action(driver, Selenium_Actions_Enum.switchTab, goToMenu, null, 1, "switching to RT tab");
					List<String> dxcodes = new ArrayList<String>();
					dxcodes.add(icdCodes);
					String authentication = hm.Auth(data, dxcodes, procedures);
//			    AllcareSubmit.submit(driver, data, cptcodeFreq, effectDate, age);
					Thread.sleep(1000);
					Selenium_Util.action(driver, Selenium_Actions_Enum.switchTab, goToMenu, null, 0, "switching to RT tab");
					Thread.sleep(2000);
					logger.error("auth " + authentication);
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
						FileUtil.addRow(li, "HumanaAuth");
						gobackToDashboard();
						countUp(pos);
						continue;
					}

					String authNum = authentication;
					String reqDate = currentdate;
					obj.put("Auth#", authNum);

					FileWriter file = new FileWriter(RootPath.BASE_DIR + "\\resources\\output\\json\\" + patient_Name + ".json");
					ObjectMapper mapper = new ObjectMapper();
					String outputjson = mapper.writeValueAsString(obj);
					file.write(outputjson);
					file.flush();
					nextActivityComment = driver.findElement(By.id("id.AUTH!!8.nextActivityComment"));
					temp = nextActivityComment.getText();
					JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

//				nextActivityComment.sendKeys(org.openqa.selenium.Keys.CONTROL);
					nextActivityComment.click();

					Thread.sleep(1000);

					jsExecutor.executeScript("arguments[0].innerHTML = arguments[1];", nextActivityComment, "");
					Thread.sleep(10000);
					jsExecutor.executeScript("arguments[0].innerHTML = arguments[1];", nextActivityComment, "");
					screen.paste(

							currentdate + " AUTH#  " + "Submitted  ");

					nextActivityComment.sendKeys(

							authNum

					);
					nextActivityComment.sendKeys(org.openqa.selenium.Keys.ENTER);
					nextActivityComment.sendKeys(

							temp

					);

					// Execute the JavaScript
					Selenium_Util.action(driver, Selenium_Actions_Enum.sendText, RaintreeLocator.XauthInput, authNum, 0, " sending auth number");
					driver.findElement(By.xpath("//*[@name='auth']"))

							.sendKeys(org.openqa.selenium.Keys.ENTER);
					Selenium_Util.action(driver, Selenium_Actions_Enum.sendTextById, "id.CONFIRMAUTH.WidgetAt_3_2", authNum, 0, " sending confitm auth number");

					driver.findElement(By.id("id.CONFIRMAUTH.WidgetAt_3_2"))

							.sendKeys(org.openqa.selenium.Keys.ENTER);
					Thread.sleep(1000);
//			
// for the date

					LocalDate currentDate = LocalDate.now();

					LocalDate _workingDate = null;
					if (caseName.contains("L1") || caseName.contains("SP")) {

						// thre days
						_workingDate = currentDate.plusDays(3);
					} else {
						// 7days
						_workingDate = currentDate.plusDays(6);

					}

					if (_workingDate.getDayOfWeek().toString().equals("SATURDAY")) {
						_workingDate = _workingDate.plusDays(2);
						logger.error(_workingDate);
					} else

					if (_workingDate.getDayOfWeek().toString().equals("SUNDAY")) {
						_workingDate = _workingDate.plusDays(1);

					}
					String formattedDate6 = formatter.format(_workingDate).replace("/", "-");
					logger.error(formatter);

					Selenium_Util.action(driver, senum.selectCheckboxByValue, RaintreeLocator.XstatusCategory, "Pending with Insurance", 0, " selecting \"Pending with Insurance\"");

//					Selenium_Util.action(driver, senum.sendText, RaintreeLocator.XauthInput, "Pending with Insurance", 0, " selecting \"Pending with Insurance\"");
					Selenium_Util.action(driver, Selenium_Actions_Enum.sendText, RaintreeLocator.XnextActivity, formattedDate6, 0, "sending 6 working date");

					Selenium_Util.action(driver, Selenium_Actions_Enum.sendTextById, RaintreeLocator.IreqDate, reqDate, 0, " sending requsting currentdate");

					Selenium_Util.action(driver, Selenium_Actions_Enum.sendTextById, RaintreeLocator.IreqVisit, "06", 0, " sending requestedVisits");

//					Selenium_Util.action(driver, Selenium_Actions_Enum.clickElementById, RaintreeLocator.Isave, authNum, 0, " clicking save btn");

//					pos++;

					List li = new ArrayList();
					obj.put("details", "completed");
					li.addAll(obj.values());

					FileUtil.PrintJsonFile(driver, obj, patient_Name + "-" + pinNumber + "-" + caseNumber);
					FileUtil.addRow(li, "HumanaAuth");
					Thread.sleep(2000);

				}
			}
		}

		System.out.println("======The end=====");
		System.out.println("======The end=====");
		System.out.println("======The end=====");
		System.out.println("======The end=====");

		/// end of the loop

		QuitBot quitBot = QuitBot.getObj();
		quitBot.setQuit(true);
		logger.error("closing the browser");
	}

	public static void gobackToDashboard() throws InterruptedException {
		Screen sc = new Screen();
		Actions actions = new Actions(driver);
		WebElement tmp = null;

		int errCounter = 10;

		while (true) {

			try {
				if (sc.exists(RootPath.BASE_DIR + "//resources//sikuli//Tpg//dashboard.png") != null) {

					logger.error("foyund");

					break;
				} else {
					try {

						driver.findElement(By.xpath(RaintreeLocator.XbackButton)).click();
						logger.error("back brn clicked");
						Thread.sleep(2000);
					} catch (Exception e2) {

					}
				}

			} catch (Exception e) {

			}

		}
	}

	static void countUp(int pos) {

		logger.error(pos);
		Properties properties = new Properties();

		properties.setProperty("rowPos", "" + pos);
		try (FileOutputStream out = new FileOutputStream("config.properties")) {
			properties.store(out, "Updated properties");

			logger.error("Property updated successfully.");
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
