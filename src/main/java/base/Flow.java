package base;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
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
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sikuli.script.Screen;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.Deserializers.Base;
import com.itextpdf.text.log.SysoCounter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import base.DriverManager;
import base.QuitBot;
import selenium_elements_path.RaintreeLocator;
import maf.apps.util.Excel_create;
import maf.apps.util.RTService;
import maf.apps.util.ResultEnum;
import maf.apps.util.RootPath;
import maf.apps.util.SeleniumUtil;
import maf.apps.util.Selenium_Util;
import maf.apps.util.Selenium_Actions_Enum;
import maf.apps.util.Selenium_operations;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import base.ReportRow;
import base.EmailSender;
import base.BotMonitor;


public class Flow implements Runnable {
	//Send in BotMonitor email
	public static volatile String currentLocation = "";
	public static volatile String currentPnNumber = null;
	//Send in BotMonitor email
	
	//Different kinds of keywords
//	List<String> firstAttemptsList=Arrays.asList("1st attempt","1stattempt","1 attempt","1attempt","first attempt","firstattempt");
//	List<String> secondAttemptsList=Arrays.asList("2nd attempt","2ndattempt","2 attempt","2attempt","second attempt","secondattempt");
//	List<String> thirdAttemptsList=Arrays.asList("3rd attempt","3rdattempt","3 attempt","3attempt","third attempt","thirdattempt");
//	List<String> fourthAttemptsList=Arrays.asList("4th attempt","4thattempt","4 attempt","4attempt","fourth attempt","fourthattempt");
	
	
	Flow(){
		
	}
	//private static List<ReportRow> reportRows = new ArrayList<>();
	
	//Set of processed errored on numbers
	
	
	//Set of processed tasks
	static Set<String> processedTasks = new HashSet<>();

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
	static String remote_excel_output = "\\\\10.1.240.40\\Waterlabs RCM\\AR\\TPGPT-Regular\\BOT Output\\"
			+ formattedCurrentDate;
	static Selenium_Actions_Enum senum;
	
	int count=0;
	
	//Error Records File path
	public static String ErrorReportFilePath;
	
	//Location IDs
	public static String reportFilePath;
	
	static List<String> locationIds = new ArrayList<>();
	static String location_id="";
	
	
	//Assets required to send email
	
		 //TO Emails
	    static String toEmails[] = //{"Suresh.DevangaMylari@waterlabs.ai"};
	    	{"Reddy.Baba@waterlabs.ai","Arijeet.Guha@waterlabs.ai","devappa.shankreppa@waterlabs.ai","kishore.shivananda@waterlabs.ai"};
	    
	    //CC emails
	    static String ccEmails[]=//null;
	    	{"Adarsh.Sundarraj@waterlabs.ai","Pavan.Raj@waterlabs.ai","Yeshwanth.Nadavathiashoka@waterlabs.ai","Suresh.DevangaMylari@waterlabs.ai","Jaideep.Reddy@waterlabs.ai","monish.reddy@waterlabs.ai"};
	    
	    //email body
	    static String emailBody;
	    
	    //email subject
	    static String emailSubject;
	    
	    //Convert the reportFilePath into File Object, so we can send this output report in email
	    static File outputReportFile;
	    
	  //Convert the masterReportFile into File Object, so we can send this output report in email
	   // static File masterReportFile;
	    
	    static File errorOutputReportFile;
    
	//Assets required to send email
	
	
	
	public static List<String> getLocationIds(String filePath) throws Exception {
        FileInputStream fis = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);

        // Skip header row
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {

            Row row = sheet.getRow(i);

            if (row != null) {
                Cell cell = row.getCell(0); // First column

                if (cell != null) {
                    locationIds.add(cell.toString().trim());
                }
            }
        }
        workbook.close();
        fis.close();
        return locationIds;
	}

	void extractData() throws Exception {
		     
	        //Get location ids from Excel 
		     locationIds=getLocationIds(RootPath.BASE_DIR +"\\resources\\TPG_Input\\TPG_UAT.xlsx");
             locationIds.forEach(location -> System.out.println(location));
             
             
             //Create excel file with header
             String timestamp = LocalDateTime.now()
            	        .format(DateTimeFormatter.ofPattern("dd-MM-yy_HH-mm-ss"));

             reportFilePath =
         			RootPath.BASE_DIR +"\\resources\\TPG_Output\\TPG_POC_Output_"
         	        + timestamp
         	        + ".xlsx";

            	createReportFile();
             //Create excel file with header
            	
            	//Create excel file with header for Error Records
            	ErrorReportFilePath = RootPath.BASE_DIR + "\\resources\\TPG_Error_Folder\\Errored_Records_"
            	        +timestamp
            	        + ".xlsx";
             
            	createErrorRecordsReportFile();
            	//Create excel file with header for Error Records
		
			System.out.println(" in the flow");
			Robot robot = new Robot();
			try {
				driver.manage().window().maximize();
			} catch (Exception e) {

			}
			System.out.println(" in the extractData");
			Actions actions = new Actions(driver);
			//String folderPath = RootPath.BASE_DIR + "\\resources\\pdfDownload";
			//System.out.println("pdf path " + folderPath);
			Screen screen = new Screen();
			Properties p = new Properties();
			Properties configP = new Properties();

			LocalDate date = LocalDate.now();
			LocalDate next6day = date.plusDays(6);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
			String next6Date = formatter.format(next6day);
			String currentdate = formatter.format(date);

			
		//	FileReader countReader = new FileReader(RootPath.BASE_DIR + "/config.properties");
		//	System.out.println(" in the flow");
		//	configP.load(countReader);
			String status = DriverManager.getpropertyvalue("subStatus");

			Map<String, String> creds = new HashMap<>();
			creds.put("userName", "RThoagUser");
			creds.put("pass", "RThoagPass");

			RTService.login(driver, creds);

			Selenium_Util.action(driver, Selenium_Actions_Enum.waitUntilLoadandClickElement, RaintreeLocator.Xdashboard,
					null, 0, "clicking the Dashboard menu");
			// Start from here
			
			
			//Debug
			System.out.println("Current URL: " + driver.getCurrentUrl());

			WebElement active =
			    driver.switchTo().activeElement();

			System.out.println(
			    "Active Element: " + active.getTagName());

			
		Selenium_Util.action(driver,
		        Selenium_Actions_Enum.waitUntilLoadandClickElement,
		        RaintreeLocator.XopenTask,//Insert Xpath
		        null,
		        0,
		        "Clicking Open Task");
		System.out.println("Clicked Open Task successfully!");
			
		// Select Clinic Location from User / Provider tab
		// clinicLocation is read from Excel
		
		
	
for(int i=0;i<locationIds.size();i++) {
	//Check BOT is alive
      BotMonitor.updateHeartbeat();
    //Check BOT is alive
	
   
  	try {
  	 location_id=locationIds.get(i);
  	 //update "currentLocation" to include in BotMonitor email
  	 currentLocation=location_id;
  	//update "currentLocation" to include in BotMonitor email
  	
  	 //Checking which location is currently being processed
  	 System.out.println("==============================");
  		System.out.println("Location currently processsing is :"+location_id);
  		System.out.println("==============================");
  		
  		Thread.sleep(3000); //NEWLY ADDED
  		
  		//Waiting for User/Provider field
  				Selenium_Util.action(driver,
  				        Selenium_Actions_Enum.waitUntilLoad,
  				        RaintreeLocator.XuserProvider,
  				        null,
  				        0,
  				        "Waiting for User/Provider");
  				
  				
  				
  				//TESTING PURPOSE ONLY
  				//Check if BOT STUCK EMAIL is sent then only perform below action.
//  				if(BotMonitor.alertSent) {
//  					//check pos is lesser than afterCount if so there are still accounts left to process in the previous location
//  					if(pos <= recordCountAfter) {
//  						System.out.println("BOT STUCK Email is sent, resuming from where it was stucked.....");
//  						//so keep
//  						i=i-1; pos=pos+1;
//  						continue;		
//  					}
//  				}
  			    //TESTING PURPOSE ONLY
  				
  				
  	  	
  	Selenium_Util.action(driver,
  	        Selenium_Actions_Enum.clearElement,
  	        RaintreeLocator.XuserProvider,//Insert Xpath
  	        null,
  	        0,
  	        "Clear Clinic Location");
  	
  	Thread.sleep(1000); //NEWLY ADDED
  	
  		Selenium_Util.action(driver,
  		        Selenium_Actions_Enum.sendText,
  		        RaintreeLocator.XuserProvider,//Insert Xpath
  		        location_id,
  		        0,
  		        "Selecting Clinic Location");
  		
  		Thread.sleep(3000); //NEWLY ADDED
   
  		// Click More Filters
   
  		Selenium_Util.action(driver,
  		        Selenium_Actions_Enum.waitUntilLoadandClickElement,
  		        RaintreeLocator.XmoreFilters,
  		        null,
  		        0,
  		        "Clicking More Filters");
  		
  		Thread.sleep(3000); //NEWLY ADDED
  		
  		//Waiting for task drop down
  		Selenium_Util.action(driver,
  		        Selenium_Actions_Enum.waitUntilLoad,
  		        RaintreeLocator.Xtype,
  		        null,
  		        0,
  		        "Waiting for Task Type dropdown");
  		
  		
  		Thread.sleep(1000); //NEWLY ADDED
   
  		// Type = Plan of care
   
  		Selenium_Util.action(driver,
  		        Selenium_Actions_Enum.selectByVisibleText,
  		        RaintreeLocator.Xtype,
  		        "Plan of Care",
  		        0,
  		        "Selecting Plan of care");
  		
  		Thread.sleep(2000); //NEWLY ADDED
   
  		// Next Activity Date = Blank
   
  		Selenium_Util.action(driver,
  		        Selenium_Actions_Enum.waitUntilLoadandClearElement,
  		        RaintreeLocator.XnextActivityDate,
  		        null,
  		        0,
  		        "Clearing Next Activity Date");
  		
  		Thread.sleep(2000); //NEWLY ADDED
   
  		//CLICK Apply Filters
  		System.out.println("Clicking Apply Filter");
  		WebElement applyFilters =
  			    driver.findElement(
  			        By.xpath(RaintreeLocator.XapplyFilters));
   
  			JavascriptExecutor js =
  			    (JavascriptExecutor) driver;
   
  		  //Check BOT is alive
  	         BotMonitor.updateHeartbeat();
  	      //Check BOT is alive
  	         
  			js.executeScript(
  			    "arguments[0].click();",
  			    applyFilters);
  		//CLICK Apply Filters
  		
  	
  			
  		Thread.sleep(2000); //NEWLY ADDED

		// Sort Open Tasks by Patient Name (ascending) so duplicates are adjacent
		sortOpenTasksByPatientName();

		// Build set of patient names that appear more than once (match key: Patient Name only)
		Set<String> duplicatePatientNames = findDuplicatePatientNames();
		System.out.println("Duplicate patients to skip entirely: " + duplicatePatientNames);
		
		
		int pos = 1;
		
		while (true) {
			int recordCountBefore=0;
			String rowXpath=null;
			boolean isDuplicatePatient = false;
			String bgColor="";
			try {

				    Thread.sleep(2000);
		
				    List<WebElement> rows =
				            (List<WebElement>) Selenium_Util.action(
				                    driver,
				                    Selenium_Actions_Enum.getWebElements,
				                    RaintreeLocator.Xtable,
				                    null,
				                    0,
				                    "Getting task rows");
				    
				    //Record count before processing
				    String countText = Selenium_Util.action(
				            driver,
				            Selenium_Actions_Enum.getText,
				            RaintreeLocator.XrecordCount,
				            null,
				            0,
				            "Get number of records"
				    ).toString();
		
				     recordCountBefore = Integer.parseInt(
				            countText.replaceAll("[^0-9]", "")
				    );
		
				    System.out.println("Record count before processing = " + recordCountBefore);
				    
		
				    if (rows == null || rows.size() == 0) {
				        System.out.println("No tasks found.");
				        break;
				    }
		
				    if (pos > rows.size()) {
				        System.out.println("All tasks processed.");
				        break;
				    }
		
				     rowXpath =
				            RaintreeLocator.Xtable + "[" + pos + "]";
				    
				    
				 // Before processing each record, verify the Background color.
				    WebElement row = driver.findElement(
				            By.xpath(rowXpath));

				    // Detect duplicate patients (regardless of row color) — open only to capture PN#, then skip
				    String patientNameOnRow = getPatientNameFromRow(row);
				    if (duplicatePatientNames.contains(patientNameOnRow)) {
				        System.out.println(
				                "Duplicate patient found on Row : " + pos +
				                " (" + patientNameOnRow + ") — will open to capture PN# then skip");
				        isDuplicatePatient = true;
				    }
		
				     bgColor = row.getCssValue("background-color");
		
				    System.out.println("Background Color = " + bgColor);
		
				    // Skip green rows only when not a duplicate (duplicates still open to capture PN#)
				    if (!isDuplicatePatient && "rgba(189, 242, 164, 1)".contains(bgColor)) {
				        System.out.println(
				                "Skipping Row : " + pos +
				                " because background color is " + bgColor);
		
				        pos++;
				        continue;			
				    }						
		}catch(Exception beforDoubleClickException) {
			 //send an email having locationID and PNNUmber(if present) or row number, do not click goBackToDashBoard()

			//Send email if error occurs at any location level
			   //sendErrorEmailRowLevel(pos);
			//Send email if error occurs at any location level
			
			//Create Error record in excel file
			String errorMessage=beforDoubleClickException.getMessage();
			createErrorRecordReportRow(errorMessage);
			   
			   pos++;
			   //continue to next row
			   continue;
		}
		   

			try {
				
				    System.out.println("Processing Row : " + pos);
				    currentPnNumber = null;
		
				    Selenium_Util.action(driver,
				            Selenium_Actions_Enum.doubleClickElement,
				            rowXpath,
				            null,
				            0,
				            "Opening Task");
		
				    Thread.sleep(2000);
				   
			}catch(Exception doubleClickError) {
				System.out.println("Error while double click on account.........");
				  //send an email having locationID and PNNUmber(if present) or row number, do not click goBackToDashBoard()

				//Send email if error occurs at any location level
				   //sendErrorEmailRowLevel(pos);
				//Send email if error occurs at any location level
				
				//Create Error record in excel file
				String errorMessage=doubleClickError.getMessage();
				createErrorRecordReportRow(errorMessage);
				
				   pos++;
				   //continue to next account
				   continue;
				
			}
				    
				    try {
				    
				    //Check if continue button is present or not
				    checkForContinueButton();

				    if (isDuplicatePatient) {
				        // Opened only to capture PN# — do not process the duplicate account
				        String pnNumber = capturePnNumberFromOpenTask();
				        logDuplicatePatientToErrorReport(pnNumber, bgColor);
				        System.out.println(
				                "Duplicate record skipped after capturing PN#: " + pnNumber);
				    } else {
				        System.out.println("Call processTask()");
				    
				        //Process each task
				    	 processTask();	
				        //Process each task
				    }
			}catch(Exception duringProcessTask) {
			
				  //send an email having locationID and PNNUmber(if present) or row number, then click goBackToDashBoard()

				//Send email if error occurs at any location level
				   //sendErrorEmailRowLevel(pos);
				//Send email if error occurs at any location level
				
				//Create Error record in excel file
				String errorMessage=duringProcessTask.getMessage();
				createErrorRecordReportRow(errorMessage);
				   
				   pos++;
				   //Click Back Button
				   Thread.sleep(1000);
				    gobackToDashboard();
				    //continue to next account
				    continue;
				
			}
		    	 
		    Thread.sleep(1000);
		    
		    //gobackToDashboard() method may fail so include in try-catch
		    try {

		        gobackToDashboard();

		    } catch(Exception backException) {

		        //sendErrorEmailRowLevel(pos);
		    	
		    	//Create Error record in excel file
		    	String errorMessage = backException.getMessage();
				createErrorRecordReportRow(errorMessage);

		        pos++;

		        continue;
		    }

		    Thread.sleep(2000);
		    
		    try {
				    //Record count after processing
				    String countTextAfter = Selenium_Util.action(
				            driver,
				            Selenium_Actions_Enum.getText,
				            RaintreeLocator.XrecordCount,
				            null,
				            0,
				            "Get number of records"
				    ).toString();
		
				    int recordCountAfter = Integer.parseInt(
				            countTextAfter.replaceAll("[^0-9]", "")
				    );
		
				    System.out.println("Record count after processing = " + recordCountAfter);
				    
				    if(recordCountBefore==recordCountAfter) {
				    	 pos++;
				    }
		    }catch(Exception afterProcessingTask) {	
		           //Do not send email here because this row is already processed
		    	   //Move to next row
		    	    pos++;
		           //continue to next row
		           continue;
		    }
		   
		}


		System.out.println("=================================");
		System.out.println("ALL ACCOUNTS PROCESSED");
		System.out.println("=================================");
		
		

			System.out.println("======The end=====");
			System.out.println("======The end=====");
			System.out.println("======The end=====");
			System.out.println("======The end=====");
			
} catch (Exception e) {
	System.out.println(e.getMessage());
	System.out.println("Error processing location: " + currentLocation);
	System.out.println("Error processing PN Number: " + currentPnNumber);
    
    System.out.println("Sending email due to error occurance in the program");
          
    //Send email if error occurs at any location level
       sendErrorEmailLocationLevel();
    //Send email if error occurs at any location level
	    continue; // move to next location

       }

		//Closing the browser after all locations are processed
		QuitBot quitBot = QuitBot.getObj();
		quitBot.setQuit(true);
		System.out.println("closing the browser");
		
      }//End of for loop

}//End of extractData() method
	
	
	public static void sendErrorEmailRowLevel() {
		System.out.println("Sending email due to error occurance in the ROW LEVEL");
	    try {
		emailSubject="TPG POC - Errored Records";
		emailBody=
				           "Hi Team,\n\n"
			                +"Please find the accounts that were error while processing"
			                + "\n\nThanks & Regards\n"
			                + "RPA BOT Support";
		
	    
	    //ATTACH OUTPUT REPORT
		errorOutputReportFile=new File(ErrorReportFilePath);
	   File[] fileAttachments= {errorOutputReportFile};
		        EmailSender.sendEmailWithAttachments(toEmails, ccEmails, emailSubject, emailBody, fileAttachments);	
	      }
		    catch(Exception mailException) {
		    	System.out.println( "Failed to send exception email"); 
		    	mailException.printStackTrace();
		    }
	}
	
	public static void sendErrorEmailLocationLevel() {
		System.out.println("Sending email due to error occurance in the LOCATION LEVEL");
	    try {
		emailSubject="TPG POC - ALERT";
		emailBody=
				           "Hi Team,\n\n"
			                + "TPG Bot appears to be errored.\n"
				            +"Errored while processing:\n"
			                +"Location ID: "+currentLocation
			                +"\nPN Number: "+currentPnNumber
			                +"\n\nLOCATION LEVEL error:"
			                + "\nError while processing a Location."    
			                +"\n\nPlease find so far processed accounts in the attached report, continuing with next Location"
			                + "\n\nThanks & Regards\n"
			                + "RPA BOT Support";
		
		//TAKE SCREENSHOT and ATTACH
	    File screenshotFile = BotMonitor.takeScreenshot();
	    
	    //ATTACH OUTPUT REPORT
	   outputReportFile=new File(reportFilePath);
	   File[] fileAttachments= {outputReportFile, screenshotFile};
		        EmailSender.sendEmailWithAttachments(toEmails, ccEmails, emailSubject,emailBody, fileAttachments);	
	      }
		    catch(Exception mailException) {
		    	System.out.println( "Failed to send exception email"); 
		    	mailException.printStackTrace();
		    }
	}

	public static void checkForContinueButton(){
		try {

		    List<WebElement> continueBtns =
		            driver.findElements(
		                    By.xpath(RaintreeLocator.Xcontinue));

		    if (!continueBtns.isEmpty()) {

		    	 System.out.println(
		    	            "Continue button found. Clicking it.");

		    	    WebElement continueBtn =
		    	            continueBtns.get(0);

		    	    JavascriptExecutor js =
		    	            (JavascriptExecutor) driver;

		    	    js.executeScript(
		    	            "arguments[0].click();",
		    	            continueBtn);

		    	    Thread.sleep(1000);

		    } else {

		        System.out.println("Continue button not present. Proceeding normally.");

		    }

		} catch (Exception e) {

		    System.out.println("Error while checking Continue button");
		    e.printStackTrace();

		}
	}
	

	
	//Count number of attempts already completed
	// Compares last 7 digits of history "Fax Sent to" numbers against send_fax field on the task screen
	public static int countAttempts(String history) {

		String uiFaxRaw = "";
		try {
			WebElement faxField = driver.findElement(
					By.xpath(RaintreeLocator.XsendFax));
			uiFaxRaw = faxField.getAttribute("value");
			if (uiFaxRaw == null) {
				uiFaxRaw = "";
			}
		} catch (Exception e) {
			System.out.println("Could not read send_fax field: " + e.getMessage());
			return 1001;
		}

		// Remove spaces, (), -, and any other non-digits
		String uiDigits = uiFaxRaw.replaceAll("[^0-9]", "");
		System.out.println("UI send_fax raw = [" + uiFaxRaw + "], digits = [" + uiDigits + "]");

		if (uiDigits.length() < 7) {
			System.out.println("UI fax number empty/too short after cleaning — returning 1001");
			return 1001;
		}

		String uiLast7 = uiDigits.substring(uiDigits.length() - 7);
		System.out.println("UI fax last 7 digits = " + uiLast7);

		Pattern pattern = Pattern.compile(
				"Successfully Sent\\s+.*?Fax Sent to\\s*:\\s*(\\d{8,15})",
				Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

		Matcher matcher = pattern.matcher(history);

		int count = 0;

		System.out.println("\nMatching history fax numbers to UI fax (last 7 digits):");

		while (matcher.find()) {
			String historyFax = matcher.group(1);
			String historyDigits = historyFax.replaceAll("[^0-9]", "");

			if (historyDigits.length() < 7) {
				System.out.println(historyFax + "  --> SKIP (too short)");
				continue;
			}

			String historyLast7 = historyDigits.substring(historyDigits.length() - 7);

			if (historyLast7.equals(uiLast7)) {
				count++;
				System.out.println(
						historyFax + "  --> MATCH last7=" + historyLast7
								+ ", Count = " + count);
			} else {
				System.out.println(
						historyFax + "  --> MISMATCH last7=" + historyLast7
								+ " vs UI " + uiLast7);
			}
		}

		System.out.println("Attempt count for current UI fax = " + count);
		return count;
	}
	
	
	//Process Each Task
	 void processTask() throws Exception{
		
	  //Check BOT is alive
         BotMonitor.updateHeartbeat();
      //Check BOT is alive
		
		 String softwareNotes="";
		
		 long successCount=-1;
		 //long FaxSentCount=-1;
		 Thread.sleep(2000);
			
		 count++;
		 String historyComments="";
		 List<WebElement> divs =
				    driver.findElements(By.xpath("//div[contains(@class,'w-memo-content')]"));
 
				System.out.println("Found: " + divs.size());
				
				//=========== Get PN Number for current account ======//
				
				Thread.sleep(1000);
	    		WebElement pnField = driver.findElement(
				        By.xpath(RaintreeLocator.XpnNumber));
	    		String pnNumber = pnField.getAttribute("value");
	    		
	    		//Update currentPnNumber to include in BotMonitor email
	    		currentPnNumber=pnNumber;
	    		//Update currentPnNumber to include in BotMonitor email
	    		
	    		//=========== Get PN Number for current account ======//
	    		
	    		//Check if this account is processed today, if processed just skip it.
	    		if(processedTasks.contains(pnNumber)) {
	    			return;
	    		}
 
				for(int i=0;i<divs.size();i++) {
				    System.out.println("Div " + i);
				    System.out.println("TEXT=[" + divs.get(i).getText() + "]");
				}
				if(divs.size()==2) {
					String insurancePayer="";
					
					 historyComments=divs.get(1).getText();	
					 //Check how many attempts are successfully completed
					 successCount = countAttempts(historyComments);
					
//					 successCount = Pattern.compile(Pattern.quote("Successfully Sent"))
//	                         .matcher(historyComments)
//	                         .results()
//	                         .count();
					
//					 FaxSentCount = Pattern.compile(Pattern.quote("Fax Sent to"))
//	                         .matcher(historyComments)
//	                         .results()
//	                         .count();
					
	                //Get Insurance payer
					  WebElement payerLabel=null;
					  try {
					   payerLabel = driver.findElement(
							    By.xpath(RaintreeLocator.XpayerLabel));
					   insurancePayer = payerLabel.getText().trim();
					  }catch (Exception e) {
	
				    		Thread.sleep(1000);
							e.printStackTrace();
							System.out.println("Did not find Insurance Payer for "+pnNumber);
							Thread.sleep(1000);
							
							insurancePayer="UNKNOWN";
							System.out.println("========= Continue with Common steps ==========");
							
					}
 
							System.out.println("Payer Name = " + insurancePayer);
		
						if (insurancePayer.contains("- MC")) {
							 System.out.println("Medicare payer found");
							
							WebElement sourceText = driver.findElement(
								    By.xpath("//div[contains(@class,'w-emrlink-button')]//div[contains(@class,'w-button-text')]"));
 
								String checkInitialEvaluation = sourceText.getText();
 
								System.out.println("Text = " + checkInitialEvaluation);
							if(checkInitialEvaluation.toLowerCase().contains("initial evaluation")) {
								//skip this account
								//Before you skip , call createReportRow()
								
								//Add PN number to output report
								System.out.println("PN Number = " + pnNumber);
								
					    		softwareNotes="Exception, found \"Initial Evaluation\" in the source field";
					    		successCount=-1;
					    		createReportRow(pnNumber, softwareNotes, successCount);
					    		 Thread.sleep(1000);
					    		 
					    		//Add current pnNumber to processedTasks set
					    		 processedTasks.add(currentPnNumber);
					    		 return;
								
							}
							
							Thread.sleep(2000);
						    //Check for "Signature needed"
						    String description = driver.findElement(
						    		By.id("id.TASK!!8.emrdesc"))
						            .getAttribute("value");
						    System.out.println("Checking what is present in owner field:"+description);
						    if(description.toLowerCase().contains("signature needed")) {
						    	//proceed with same steps
						    	System.out.println("Found Signature needed ============ continue with same process");
						    	this.commonStepToProcessAccounts(historyComments,successCount);
						    	
						    }else {
						    	//check for chaitra.prasad or koppula.ganesh
						    	if(historyComments.contains("chaitra.prasad") || historyComments.contains("koppula.ganesh")) {
						    		//proceed with same steps
						    		System.out.println("Found Chaitra and Koppula ============ continue with same process");
						    		this.commonStepToProcessAccounts(historyComments,successCount);
						    	}else {
						    		System.out.println("Neither \"Signature needed\" nor \"chaitra.prasad/koppula.ganesh\" found ,so Skipping this Medicare account..............");
						    		//Before you skip , call createReportRow()
									
									//Add PN number to output report
									System.out.println("PN Number = " + pnNumber);
									
						    		softwareNotes="Exception, Neither \"Signature needed\" nor \"chaitra.prasad/koppula.ganesh\" found";
						    		successCount=-1;
						    		createReportRow(pnNumber, softwareNotes, successCount);
						    		 Thread.sleep(1000);
						    		 
						    		 //Add current pnNumber to processedTasks set
						    		 processedTasks.add(currentPnNumber);
						    		 return;
						    	}
						    }
						    
						    
						} else {
						    System.out.println("Not Medicare ================ Proceeding with same steps");
						    this.commonStepToProcessAccounts(historyComments,successCount);
						}
				}else {
					System.out.println("Account cannot be processed==============");
					return;
				}
				
				//Add the processed tasks into processedTasks Set.
				processedTasks.add(pnNumber);
	}
	
	
	 public void commonStepToProcessAccounts(String historyComments,long successCount) throws Exception{
		
		 Thread.sleep(1000);
		 //Validate which attempt to work, return successCount
		  // =findAttempt();
		 //
		
		 String successString="successfully sent";
		
		 //Convert comments to lower case
		 historyComments=historyComments.toLowerCase();
		
		 WebElement pnField = driver.findElement(
			        By.xpath(RaintreeLocator.XpnNumber));
			String pnNumber = pnField.getAttribute("value");
			//Add PN number to output report
			System.out.println("PN Number = " + pnNumber);
			
			//Update currentPnNumber to include in BotMonitor email
    		currentPnNumber=pnNumber;
    		//Update currentPnNumber to include in BotMonitor email
		
		 String softwareNotes="";
		
		 if(historyComments.contains("we have exhausted attempts")) {
			 System.out.println("We have exhausted attempts =============== skipping this account");
			 softwareNotes="Exception, 5 attempts done";
			 //Before you skip , call createReportRow()
			 Thread.sleep(1000);
			 createReportRow(pnNumber, softwareNotes, successCount);
			 //gobackToDashboard();
			 Thread.sleep(1000);
		 }else if(successCount==0) {
			 System.out.println("Success Count is ============ 0");
			 Thread.sleep(3000);
			//========Do not write comment========//
			 softwareNotes="Exception ,1st attempt not completed";
				//===========Do not write comment==============//
				
				//Create report row before clicking save button
				createReportRow(pnNumber,softwareNotes,successCount);
				Thread.sleep(2000);
			
			 System.out.println("Commented as =============== "+softwareNotes);
		 }
		 else if(successCount==4) {
			
		/*	 String attempt= getAttemptKeywordInHistoryComments(fourthAttemptsList, historyComments);
			  if(checkForWordSuccessfullySent(historyComments, attempt)) {
		*/	
			
			
			//Check if this account can be processed on 7th or above date if not just skip this account.  
				  if(!checkIfDateIsSeventhOrAbove(successString , historyComments)){
					  //write exception only in output report
					  softwareNotes="Exception, cannot work today, as lastWorkedDate+7 days is lesser than today's date";
					  createReportRow(pnNumber,softwareNotes,4);
					  return;
				  }
				  
				  //Work on 5th attempt
				  workOnFifthAttempt( softwareNotes,  pnNumber);
				  
				
		/*	  }else {
				//Successfully sent word is not present before the word "4th attempt"
				//So Work on 4th attempt only
				  this.processThirdOrFourthAttempt(3,pnNumber,softwareNotes);
			  }
	    */
			  
		 }else if(successCount==3) {
		/*	 String attempt= getAttemptKeywordInHistoryComments(thirdAttemptsList, historyComments);
			Also check if "3rd attempt" is successfully sent or not
			 if(checkForWordSuccessfullySent(historyComments, attempt)) {
		*/		
			
			
				//Check if this account can be processed on 7th or above date if not just skip this account.
				  if(!checkIfDateIsSeventhOrAbove(successString, historyComments)){
					//write exception only in output report
					  softwareNotes="Exception, cannot work today, as lastWorkedDate+7 days is lesser than today's date";
					  createReportRow(pnNumber,softwareNotes,3);
					  return;
				  }
				
				//work on 4th attempt
				 this.processThirdOrFourthAttempt(3,pnNumber,softwareNotes);
				
				
		/*	 }else {
				//Successfully sent word is not present before the word "3rd attempt"
				//So just work on 3rd attempt only
				 this.processThirdOrFourthAttempt(2,pnNumber,softwareNotes);
			 }
		*/
				
		 }else if(successCount==2) {
			/*   String attempt = getAttemptKeywordInHistoryComments(secondAttemptsList, historyComments);
			  if(checkForWordSuccessfullySent(historyComments,attempt)) {
				Also check if "2nd attempt" is present or not and then check for word "Successfully Sent" then go ahead for 3rd attempt
			*/
				  
                //Check if this account can be processed on 7th or above date if not just skip this account.
				  if(!checkIfDateIsSeventhOrAbove(successString, historyComments)){
					//write exception only in output report
					  softwareNotes="Exception, cannot work today, as lastWorkedDate+7 days is lesser than today's date";
					  createReportRow(pnNumber,softwareNotes,2);
					  return;
				  }
				
				 //work on 3rd attempt
				 this.processThirdOrFourthAttempt(2,pnNumber,softwareNotes);  
				
				
			/*  }else {
				//Successfully sent word is not present before the word "2nd attempt"
				//So just write as "Exception ,2nd attempt not completed" in the report
				  writeSecondAttemptInReport(softwareNotes,pnNumber,successCount);
			  }
		 */
		 }else if(successCount==1) {
			/* String attempt = getAttemptKeywordInHistoryComments(firstAttemptsList, historyComments);
			 check if "1st attempt" is present or not and then check for word "Successfully Sent" then go ahead for 2nd attempt
			  if(checkForWordSuccessfullySent(historyComments, attempt)) {
			*/	
			
			
			
			 //So just write as "Exception ,2nd attempt not completed" in the report
					 writeSecondAttemptInReport(softwareNotes,pnNumber,successCount);
					
					
			/*  }else {
				//Successfully sent word is not present before the word "2nd attempt"
				//So just write as "Exception ,1st attempt not completed" in the report
				  writeFirstAttemptInReport(softwareNotes,pnNumber,successCount);
			  }
			*/
		 }
		 else if(successCount==5) {
			 System.out.println("Encountered 5th attempt ============= Attempts are exhausted");
			 softwareNotes="Exception";
			
			//Before you skip , call createReportRow()
			 Thread.sleep(1000);
			 createReportRow(pnNumber, softwareNotes, successCount);
			 //gobackToDashboard();
			 Thread.sleep(1000);
		 }else if(successCount==1001) {
			 System.out.println("UI does not have fax number field, so skipping this account");
			 softwareNotes="UI does not have fax number field, so skipping this account";
			 createReportRow(pnNumber, softwareNotes, successCount);
		 }
		
	 }
		
		 public String getAttemptKeywordInHistoryComments(List<String> attemptsKeywordList,String historyComments) {
			 //return first found attemptWord from historyComments.
			 return attemptsKeywordList.stream()
			            .filter(k -> historyComments.contains(k))
			            .findFirst()
			            .orElse(null);
		 }
		
		 public boolean checkAttemptKeywordInHistoryComments(List<String> attemptsList,String historyComments) {
			return attemptsList.stream().anyMatch(k -> historyComments.contains(k));
		 }
		
		
		 public void writeFirstAttemptInReport(String softwareNotes,String pnNumber, long successCount) throws Exception{
			 System.out.println("Success Count is ============ 0");
			 Thread.sleep(3000);
			//========Do not write comment========//
			 softwareNotes="Exception ,1st attempt not completed";
				//===========Do not write comment==============//
				
				//Create report row before clicking save button
				createReportRow(pnNumber,softwareNotes,successCount);
				Thread.sleep(2000);
			
			 System.out.println("Commented as =============== "+softwareNotes);
		 }
		
		 public void writeSecondAttemptInReport(String softwareNotes,String pnNumber, long successCount) throws Exception{
			 System.out.println("Success Count is ============ 1");
			 Thread.sleep(3000);
			//========Do not write comment========//
			 softwareNotes="Exception ,2nd attempt not completed";
				//===========Do not write comment==============//
				
				//Create report row before clicking save button
				createReportRow(pnNumber,softwareNotes,successCount);
				Thread.sleep(2000);
			
			 System.out.println("Commented as =============== "+softwareNotes);
		 }
		
		 public boolean checkForWordSuccessfullySent(String historyComments,String attempt) {
			 //Find index of first occurrence of the string "attempt"
			 int index=historyComments.indexOf(attempt);
			
			 //After you find first occurrence, check if string "successfully sent" is present
			 String remainingPart=historyComments.substring(0,index);
			 if(remainingPart.contains("successfully sent"))
				 return true;
			
			 return false;
		 }
		
		 public boolean checkIfDateIsSeventhOrAbove(String successString, String historyComments) {
			 //Find index of first occurrence of the string "successString"
			 int index=historyComments.indexOf(successString);
			
			 //Find remaining part from 0 to "successString"
			 String remainingPart=historyComments.substring(0,index);
			
			 //Get the date from string "remainingPart". Start searching for date from end of the string
			 try {
	 
			        // Get last occurrence of date
			        Pattern pattern =
			                Pattern.compile("\\d{1,2}-\\d{1,2}-\\d{2}");
	 
			        Matcher matcher =
			                pattern.matcher(remainingPart);
	 
			        String lastDate = null;
	 
			        while (matcher.find()) {
			            lastDate = matcher.group();
			        }
	 
			        System.out.println(
			                "Last date found = " + lastDate);
	 
			        // No date found
			        if(lastDate == null) {
			            return false;
			        }
			        
				 //Add 7 days to that lastDate, if the newly obtained date is greater than or equal to current system date then return true else false.
	 
	 
			        // FORMAT => MM-dd-yy
			        DateTimeFormatter formatter =
			                DateTimeFormatter.ofPattern("MM-dd-yy");
	 
			        // Convert string to LocalDate
			        LocalDate extractedDate =
			                LocalDate.parse(lastDate, formatter);
	 
			        System.out.println(
			                "Extracted date = " + extractedDate);
	 
			        // Add 7 days
			        LocalDate dateAfterSevenDays = extractedDate.plusDays(7);
	 
			        System.out.println("Date after 7 days = "+ dateAfterSevenDays);
	 
			        // Current system date
			        LocalDate currentDate = LocalDate.now();
	 
			        System.out.println(
			                "Current system date = "
			                + currentDate);
	 
			        // Check if new date >= current date
			        return currentDate
			                .isEqual(dateAfterSevenDays)
			                ||
			               currentDate
			                .isAfter(dateAfterSevenDays);
	 
			    } catch (Exception e) {
	 
			        e.printStackTrace();
			        return false;
			    }	        
			
		 }
		
		 public void updateDescriptionElementAtTop() throws Exception{
			 Selenium_Util.action(driver,
				        Selenium_Actions_Enum.waitUntilLoad,
				        RaintreeLocator.XinputDescription,
				        null,
				        0,
				        "Wait for element to load");
			
			 Selenium_Util.action(driver,
				        Selenium_Actions_Enum.clearElement,
				        RaintreeLocator.XinputDescription,
				        null,
				        0,
				        "Clearing Description");
			 Thread.sleep(2000);
	 
				Selenium_Util.action(driver,
				        Selenium_Actions_Enum.sendText,
				        RaintreeLocator.XinputDescription,
				        "PCC to work",
				        0,
				        "Updating Description");
				 Thread.sleep(2000);
			
				System.out.println("Updated description as ================ PCC to work");
		 }
		
		
		
		 //Work on 5th attempt
		
		 public void workOnFifthAttempt(String softwareNotes, String pnNumber) throws Exception{
			 //work on 5th attempt
			 System.out.println("Success Count is ============ 4");
			
			  //Update description as "PCC to work"
			 //Before updating description, check if element is present or not
				 List<WebElement> descriptionElements =
					        driver.findElements(
					                By.xpath(RaintreeLocator.XinputDescription));
	 
				if(!descriptionElements.isEmpty()) {
	 
				    System.out.println(
				        "Description textbox already present");
	 
				    updateDescriptionElementAtTop();
	 
				}else {
				 //desccriptionElement not found
				 System.out.println("Description Element not found, because it is dropdown list now, so changing it into editable text field");
				 Selenium_Util.action(driver,
					        Selenium_Actions_Enum.clickElement,
					        RaintreeLocator.XfreeformDescription,
					        null,
					        0,
					        "Clicking Freeform Description Checkbox");
				 Thread.sleep(1000);
				//Update description to "Pcc to work"
				 updateDescriptionElementAtTop();
			 }
			
			//Before updating description, check if element is present or not
			
			
			//========Comment box START========//
				String comment="We have exhausted attempts to obtain signature from MD. Please notify patient to assist.";
				System.out.println("Success Count is ============ 4");
				 Thread.sleep(3000);
				
				softwareNotes=comment;
				writeComment(comment);
				//===========Comment box END==============//
				Thread.sleep(2000);
				
				//Create report row before clicking save button
				createReportRow(pnNumber,softwareNotes,4);
				Thread.sleep(1000);
			
				// Save
				Selenium_Util.action(driver,
				        Selenium_Actions_Enum.click,
				        RaintreeLocator.Isave,
				        null,
				        0,
				        "Clicking Save Button");
				
				System.out.println("Commented as =========== "+comment);
				System.out.println("Save button clicked");
				
				Thread.sleep(2000);
		 }
		
		
		 //3rd or 4th attempt common method
		 public void processThirdOrFourthAttempt(long successCount,String pnNumber, String softwareNotes) throws Exception{
	 
			// work on 3rd or 4th attempt
						 System.out.println("Success Count is ============ "+successCount);
						  //Add "SEND" text to send field
						 WebElement ownerField = driver.findElement(
							        By.cssSelector("input[name='emrowner']"));
							//ownerField.click();
							ownerField.clear();
							ownerField.sendKeys("SEND");
							 System.out.println("Adding the word ============= SEND ");
							
							 Thread.sleep(1000);
							
						 // Add "Normal" text to Priority field
							WebElement priorityDropdown = driver.findElement(
							        By.cssSelector("select[name='priority']"));
							Select priority = new Select(priorityDropdown);
							priority.selectByVisibleText("Normal");
							System.out.println("Adding the word ============= Normal ");
							
							Thread.sleep(1000);
							
						    //Add 7 days to current date
							String updatedDateStr=addSevenDaysToCurrentDate();
							System.out.println("Updated Due Date (+7 days): " + updatedDateStr);
						
							Thread.sleep(3000);
							
							System.out.println("Success Count is ============ 2");
							
						     
							//========Comment box START========//
			                                   String comment="";
			                                if(successCount == 2){
			                                     comment="3rd attempt";
			                                   }else
			                                     comment="4th attempt";
			                                
							softwareNotes=comment;
							writeComment(comment);
							//===========Comment box END==============//
							
							Thread.sleep(2000);
							
							//Create report row before clicking save button
							createReportRow(pnNumber,softwareNotes,successCount);
							Thread.sleep(1000);
							
							// Save
							Selenium_Util.action(driver,
							        Selenium_Actions_Enum.click,
							        RaintreeLocator.Isave,
							        null,
							        0,
							        "Clicking Save Button");
							
							 System.out.println("Completed ============= "+comment);
							 Thread.sleep(2000);
	 
			}
	 
	 
	 public static void createReportRow(String pnNumber, String softwareNotes,long successCount) throws Exception{
	  //Check BOT is alive
         BotMonitor.updateHeartbeat();
      //Check BOT is alive
         
		//OUTPUT REPORT GENERATION
		 
		//Create Object of ReportRow
		 ReportRow reportRow = new ReportRow();
			
		 //Add date to output report
		 reportRow.setDate(
		         LocalDate.now().format(
		                 DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		 System.out.println("Today's date for output report =========== "+reportRow.getDate());
		 //Add PN number to output report
		 reportRow.setPnNumber(pnNumber);
		 System.out.println("PN number for output report ============ "+reportRow.getPnNumber());
		 //Add location
		 reportRow.setLocation(location_id);//Remove quotes later
		 
		 //Add number of attempts
		 reportRow.setNoOfAttempts(
			        String.valueOf(successCount));
		 
		 //Add Patient name, DOB
		 String captionText = driver.findElement(
			        By.xpath("//span[contains(@class,'screen-caption') and contains(text(),'DOB')]"))
			        .getText();
			System.out.println("Caption Text First DEBUG: " + captionText);
			
			
		//DOB regex
			Pattern dobPattern =
			        Pattern.compile("DOB\\s*-\\s*(\\d{2}-\\d{2}-\\d{2})");

			Matcher dobMatcher =
			        dobPattern.matcher(captionText);

			String dob = "";

			if (dobMatcher.find()) {
			    dob = dobMatcher.group(1);
			}

			System.out.println("DOB = " + dob);
			
			//Patient name pattern
			Pattern namePattern =
			        Pattern.compile("Task\\s*-\\s*\\d+\\s+(.*?)\\s+DOB");

			Matcher nameMatcher =
			        namePattern.matcher(captionText);

			String patientName = "";

			if (nameMatcher.find()) {
			    patientName = nameMatcher.group(1).trim();
			}

			System.out.println("Patient Name = " + patientName);
			reportRow.setDob(dob);
			reportRow.setPatientName(patientName);
		 
		 //Add Noridian Letter
			//Filling dummy values for testing
			reportRow.setNoridianLetter("Yes/No");
			
		 //Add "In Lieu Form"
			//Filling dummy values for testing
		    reportRow.setInLieuForm("Yes/No");
		    
		 //Add Claim Status
		  //Filling dummy values for testing
		    reportRow.setClaimStatus("");
		 
		 //Add software notes
		  //Filling dummy values for testing
		    reportRow.setSoftwareNotes(softwareNotes);
		    reportRow.setLocation(location_id);
		 
		 //Add reportRow to List
		// reportRows.add(reportRow);
		 
		 // Update Master Report immediately
		   // MasterReport.appendReportRow(reportRow);
//		    logger.info(reportRow);
//		    logger.info("Appending to master report");
		 
		// Immediately persist to report
		    appendRowToExcel(reportRow);
	 }
	 
	 
	 public void createErrorRecordReportRow(String errorMessage) throws Exception {

		    // CHECK NULL OR EMPTY
		    if(currentPnNumber == null)
		     {

		        System.out.println(
		                "PN Number is empty. Skipping append.");

		        return;
		    }

		    // REMOVE EXTRA SPACES
		    String pnNumber = currentPnNumber;


		    // CREATE REPORT OBJECT
		    ErrorReportRow errorReportRow =
		            new ErrorReportRow();

		    // DATE
		    errorReportRow.setDate(
		            LocalDate.now().format(
		                    DateTimeFormatter
		                    .ofPattern("dd/MM/yyyy")));

		    System.out.println(
		            "Today's date for output report =========== "
		            + errorReportRow.getDate());

		    // PN NUMBER
		    errorReportRow.setPnNumber(pnNumber);

		    System.out.println(
		            "PN number for output report ============ "
		            + errorReportRow.getPnNumber());

		    // LOCATION
		    errorReportRow.setLocation(currentLocation);
		    
		 // Error Message
		    errorReportRow.setErrorMessage(errorMessage);
		    
		    // APPEND TO EXCEL
		    appendErrorRowToExcel(errorReportRow);

		    System.out.println(
		            "Error row appended successfully.");
		}

	 // Click Patient Name header once so Open Tasks sort ascending by patient name
	 public void sortOpenTasksByPatientName() throws Exception {
		 System.out.println("Sorting Open Tasks by Patient Name (ascending)...");
		 Selenium_Util.action(driver,
		         Selenium_Actions_Enum.waitUntilLoadandClickElement,
		         RaintreeLocator.XpatientNameHeader,
		         null,
		         0,
		         "Clicking Patient Name column header to sort ascending");
		 Thread.sleep(2000);
	 }

	 // Read Patient Name cell text from a task table row
	 public String getPatientNameFromRow(WebElement row) {
		 try {
			 WebElement nameCell = row.findElement(
			         By.xpath(RaintreeLocator.XpatientNameCellRelative));
			 String name = nameCell.getText();
			 return name == null ? "" : name.trim();
		 } catch (Exception e) {
			 System.out.println("Could not read Patient Name from row: " + e.getMessage());
			 return "";
		 }
	 }

	 // Scan all Open Task rows and return Patient Names that appear more than once
	 public Set<String> findDuplicatePatientNames() {
		 Map<String, Integer> nameCounts = new HashMap<>();
		 List<WebElement> rows = driver.findElements(By.xpath(RaintreeLocator.Xtable));

		 for (WebElement row : rows) {
			 String patientName = getPatientNameFromRow(row);
			 if (patientName.isEmpty()) {
				 continue;
			 }
			 nameCounts.put(patientName, nameCounts.getOrDefault(patientName, 0) + 1);
		 }

		 Set<String> duplicates = new HashSet<>();
		 for (Map.Entry<String, Integer> entry : nameCounts.entrySet()) {
			 if (entry.getValue() > 1) {
				 duplicates.add(entry.getKey());
			 }
		 }
		 return duplicates;
	 }

	 // Capture PN# from the opened task screen (does not process the account)
	 public String capturePnNumberFromOpenTask() throws Exception {
		 Thread.sleep(1000);
		 WebElement pnField = driver.findElement(
		         By.xpath(RaintreeLocator.XpnNumber));
		 String pnNumber = pnField.getAttribute("value");
		 if (pnNumber != null) {
			 pnNumber = pnNumber.trim();
		 }
		 currentPnNumber = pnNumber;
		 System.out.println("Captured PN Number from open task = " + pnNumber);
		 return pnNumber;
	 }

	 // Log skipped duplicate patient into Error Report using captured PN#
	 public void logDuplicatePatientToErrorReport(String pnNumber, String bgColor) throws Exception {
		 if (pnNumber == null || pnNumber.isEmpty()) {
			 System.out.println("PN Number is empty for duplicate skip. Skipping append.");
			 return;
		 }

		 ErrorReportRow errorReportRow = new ErrorReportRow();
		 errorReportRow.setDate(
		         LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		 errorReportRow.setPnNumber(pnNumber);
		 errorReportRow.setLocation(currentLocation);
		 errorReportRow.setErrorMessage(
		         "Exception, duplicate patient records found - patient skipped entirely");
		 String status="";
		 //If green put status as "Non-Workable" or else "Workable"
		 if("rgba(189, 242, 164, 1)".contains(bgColor)){
			 status="Non-Workable";
		 }else  status="Workable";
		 errorReportRow.setStatus(status);

		 appendErrorRowToExcel(errorReportRow);
		 System.out.println(
		         "Logged duplicate skip to Error Report with PN#: " + pnNumber);
	 }
	 
	 
	 
	 static void writeComment(String comment) throws Exception {
		
		 Thread.sleep(1000);
		 
		 WebElement commentBox = driver.findElement(
			        By.xpath( RaintreeLocator.XcommentSection));

			JavascriptExecutor js = (JavascriptExecutor) driver;

			// Scroll element into view
			js.executeScript(
			        "arguments[0].scrollIntoView({block:'center', inline:'nearest'});",
			        commentBox);
			
			Thread.sleep(1000);

		// Click comment section
		 SeleniumUtil.action(
		         driver,
		         Selenium_Actions_Enum.waitUntilLoadandClickElement,
		         RaintreeLocator.XcommentSection,
		         null, null, null, null, 0,
		         "Clicking Comment Section");

		 // Click fullscreen editor
		 SeleniumUtil.action(
		         driver,
		         Selenium_Actions_Enum.waitUntilLoadandClickElement,
		         "//button[@title='Open fullscreen editor']",
		         null, null, null, null, 0,
		         "Opening Fullscreen Editor");

		 Thread.sleep(3000);

		 // Switch to iframe
		//*[@id="tinymce"]
		 WebElement iframe =
		         driver.findElement(
		                 By.id("TinyMceHtmlEditor_ifr"));

		 driver.switchTo().frame(iframe);

		 // Enter comment
		 WebElement editor =
		         driver.findElement(
		                 By.cssSelector("[contenteditable='true']"));

		 editor.click();

		 Actions actions = new Actions(driver);

		 //Send comment to editor
		 actions.keyDown(Keys.CONTROL)
		        .sendKeys("a")
		        .keyUp(Keys.CONTROL)
		        .sendKeys(Keys.DELETE)
		        .sendKeys(comment)
		        .build()
		        .perform();
		 
		 //Press Ctrl S
		 actions.keyDown(Keys.CONTROL)
	       .sendKeys("s")
	       .keyUp(Keys.CONTROL)
	       .build()
	       .perform();

		 driver.switchTo().defaultContent();
		}
	 
	 static String addSevenDaysToCurrentDate() throws Exception{
		 WebElement dueDateField = driver.findElement(
			        By.cssSelector("input[name='duedate']"));

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yy");

			// Add 7 days to current date
			LocalDate updatedDate = LocalDate.now().plusDays(7);
			
			// Convert back to String
			String updatedDateStr = updatedDate.format(formatter);

			// Delete existing date and update the field
			dueDateField.sendKeys(Keys.CONTROL + "a");
			dueDateField.sendKeys(Keys.DELETE);
			dueDateField.sendKeys(updatedDateStr);
			
			return updatedDateStr;
	 }
	 
	 
	 //Creating report file with only headers.
	 public static void createReportFile() throws Exception {

		    Workbook workbook = new XSSFWorkbook();
		    Sheet sheet = workbook.createSheet("TPG Report");

		    String[] headers = {
		            "Date",
		            "PN#",
		            "DOB",
		            "Patient Name",
		            "Location",
		            "No of attempts",
		            "Noridian Letter",
		            "In Lieu Form",
		            "claim status",
		            "Software Notes"
		    };

		    Row headerRow = sheet.createRow(0);

		    for (int i = 0; i < headers.length; i++) {
		        headerRow.createCell(i).setCellValue(headers[i]);
		    }

		    FileOutputStream out = new FileOutputStream(reportFilePath);
		    workbook.write(out);

		    out.close();
		    workbook.close();
		}
	 
	 
	 //Creating report file with only header - for Error Records
	 public static void createErrorRecordsReportFile() throws Exception {

		    Workbook workbook = new XSSFWorkbook();
		    Sheet sheet = workbook.createSheet("TPG Error Report");

		    String[] headers = {
		            "Date",
		            "PN#",
		            "Location",
		            "Error_Message",
		            "Status"
		    };

		    Row headerRow = sheet.createRow(0);

		    for (int i = 0; i < headers.length; i++) {
		        headerRow.createCell(i).setCellValue(headers[i]);
		    }

		    FileOutputStream out = new FileOutputStream(ErrorReportFilePath);
		    workbook.write(out);

		    out.close();
		    workbook.close();
		}
	 
	 
	 //Append rows to excel immediately
	 public static synchronized void appendRowToExcel(ReportRow report)
		        throws Exception {

		    FileInputStream fis = new FileInputStream(reportFilePath);

		    Workbook workbook = new XSSFWorkbook(fis);

		    Sheet sheet = workbook.getSheet("TPG Report");

		    int rowNum = sheet.getLastRowNum() + 1;

		    Row row = sheet.createRow(rowNum);

		    row.createCell(0).setCellValue(report.getDate());
		    row.createCell(1).setCellValue(report.getPnNumber());
		    row.createCell(2).setCellValue(report.getDob());
		    row.createCell(3).setCellValue(report.getPatientName());
		    row.createCell(4).setCellValue(report.getLocation());
		    row.createCell(5).setCellValue(report.getNoOfAttempts());
		    row.createCell(6).setCellValue(report.getNoridianLetter());
		    row.createCell(7).setCellValue(report.getInLieuForm());
		    row.createCell(8).setCellValue(report.getClaimStatus());
		    row.createCell(9).setCellValue(report.getSoftwareNotes());

		    fis.close();

		    FileOutputStream fos = new FileOutputStream(reportFilePath);

		    workbook.write(fos);

		    fos.close();
		    workbook.close();
		}
	 
	 
	 public static synchronized void appendErrorRowToExcel(ErrorReportRow errorReportRow) throws Exception{
		 FileInputStream fis = new FileInputStream(ErrorReportFilePath);

		    Workbook workbook = new XSSFWorkbook(fis);

		    Sheet sheet = workbook.getSheet("TPG Error Report");

		    int rowNum = sheet.getLastRowNum() + 1;

		    Row errorRow = sheet.createRow(rowNum);

		    errorRow.createCell(0).setCellValue(errorReportRow.getDate());
		    errorRow.createCell(1).setCellValue(errorReportRow.getPnNumber());
		    errorRow.createCell(2).setCellValue(errorReportRow.getLocation());
		    errorRow.createCell(3).setCellValue(errorReportRow.getErrorMessage());
		    errorRow.createCell(4).setCellValue(errorReportRow.getStatus());
		    
		    fis.close();

		    FileOutputStream fos = new FileOutputStream(ErrorReportFilePath);

		    workbook.write(fos);

		    fos.close();
		    workbook.close();
	 }
	
	
	@Override
	public void run() {
		try {
			System.out.println(" ========");
			//Send email "Agent started"
			System.out.println("==== Sending email as Agent has started =====");
			emailSubject="TPG POC";
			emailBody="Hi Team,\r\n"
					+ " \r\n"
					+ "Agent TPG POC has been started.\r\n"
					+ " \r\n"
					+ "Thanks,\r\n"
					+ "RPA BOT Support";
			EmailSender.sendEmailWithAttachments(toEmails,ccEmails,emailSubject,emailBody,null);
			Thread.sleep(2000);
			
			extractData();
			
			Thread.sleep(2000);
			
			//Send report email
		        emailSubject="TPG POC";
		        emailBody="Hi Team,\r\n"
		        		+ " \r\n"
		        		+ "Agent TPG POC has been successfully completed. Please find the attached daily report\r\n"
		        		+ " \r\n"
		        		+ "Thanks,\r\n"
		        		+ "RPA BOT Support";
		        
		        //Daily output Report file
		        outputReportFile=new File(reportFilePath);
		        //Master report file
		        //File masterReportFile = new File(MasterReport.getMasterReportPath());
		        
		        File[] fileAttachments= {outputReportFile};
		        
		        System.out.println("Email sent from try block-----------");
		        EmailSender.sendEmailWithAttachments(toEmails,ccEmails,emailSubject,emailBody,fileAttachments);
	        //Send report email
			
		} catch (Exception e) {
			e.printStackTrace();
			//Send email when exception or error occurs in the program
			System.out.println("Sending email due to error occurance in the program");
			try {
			emailSubject="TPG POC BOT ALERT";
			emailBody=
					           "Hi Team,\n\n"
				                + "TPG Bot appears to be errored."
					            +"Errored while processing:"
				                +"Location ID: "+currentLocation
				                +"\nPN Number: "+currentPnNumber
				                +"Error occurred inside extractData() method.........."
				                +e.getMessage()
				                +"\nPlease find so far processed accounts in the attached report."
				                + "\n\nThanks & Regards\n"
				                + "RPA BOT Support";
			
			//TAKE SCREENSHOT and ATTACH
		     File screenshotFile = BotMonitor.takeScreenshot();
		     //ATTACH OUTPUT REPORT
		    outputReportFile=new File(reportFilePath);
		    File[] fileAttachments= {outputReportFile, screenshotFile};
		    
	        EmailSender.sendEmailWithAttachments(toEmails, ccEmails, emailSubject,emailBody, fileAttachments);	
	        
			}catch(Exception mailException) {
				System.out.println( "Failed to send exception email"); 
		    	mailException.printStackTrace();
			}
	        
			
		}finally {
	        System.out.println("Report available at: " + reportFilePath);
	        //Send ERROR REPORT at the end
	        sendErrorEmailRowLevel();
	        System.out.println("Error Report available at: "+ErrorReportFilePath);
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

					System.out.println("foyund");

					break;
				} else {
					try {

						driver.findElement(By.xpath(RaintreeLocator.XbackButton)).click();
						System.out.println("back brn clicked");
						Thread.sleep(2000);
					} catch (Exception e2) {

					}
				}

			} catch (Exception e) {

			}

		}
	}

}
