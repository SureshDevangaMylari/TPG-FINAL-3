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
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import base.ReportRow;
import base.EmailSender;

public class Flow implements Runnable {
	Flow(){
		
	}
	private static List<ReportRow> reportRows = new ArrayList<>();
	
	//Set of processed tasks
	Set<String> processedTasks = new HashSet<>();

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
	
	
	//Location IDs
	public static String reportFilePath;
	
	static List<String> locationIds = new ArrayList<>();
	static String location_id="";
	
	
	//Assets required to send email
	
		 //TO Emails
	    static String toEmails[] = {"Suresh.DevangaMylari@waterlabs.ai"};
	    
	    //CC emails
	    static String ccEmails[]= null;
	    
	    //email body
	    static String emailBody;
	    
	    //email subject
	    static String emailSubject;
	    
	    //Convert the reportFilePath into File Object, so we can send this output report in email
	    static File outputReportFile;
    
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
		     locationIds=getLocationIds("C:\\Users\\sureshd\\Downloads\\TPG_POC\\TPG_UAT.xlsx");
             locationIds.forEach(location -> System.out.println(location));
             
             
             //Create excel file with header
             String timestamp = LocalDateTime.now()
            	        .format(DateTimeFormatter.ofPattern("dd-MM-yy_HH-mm-ss"));

            	reportFilePath =
            	        "C:\\Users\\sureshd\\Downloads\\TPG_POC\\output\\POC_Output_"
            	        + timestamp
            	        + ".xlsx";

            	createReportFile();
             //Create excel file with header
             
		
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
	try {
	 location_id=locationIds.get(i);
	
	 //Checking which location is currently being processed
	 System.out.println("==============================");
		System.out.println("Location currently processsing is :"+location_id);
		System.out.println("==============================");
		
		//Waiting for User/Provider field
				Selenium_Util.action(driver,
				        Selenium_Actions_Enum.waitUntilLoad,
				        RaintreeLocator.XuserProvider,
				        null,
				        0,
				        "Waiting for User/Provider");
	  	
	Selenium_Util.action(driver,
	        Selenium_Actions_Enum.clearElement,
	        RaintreeLocator.XuserProvider,//Insert Xpath
	        null,
	        0,
	        "Clear Clinic Location");
	
		Selenium_Util.action(driver,
		        Selenium_Actions_Enum.sendText,
		        RaintreeLocator.XuserProvider,//Insert Xpath
		        location_id,
		        0,
		        "Selecting Clinic Location");

		// Click More Filters

		Selenium_Util.action(driver,
		        Selenium_Actions_Enum.waitUntilLoadandClickElement,
		        RaintreeLocator.XmoreFilters,
		        null,
		        0,
		        "Clicking More Filters");
		
		//Waiting for task drop down
		Selenium_Util.action(driver,
		        Selenium_Actions_Enum.waitUntilLoad,
		        RaintreeLocator.Xtype,
		        null,
		        0,
		        "Waiting for Task Type dropdown");


		// Type = Plan of care

		Selenium_Util.action(driver,
		        Selenium_Actions_Enum.selectByVisibleText,
		        RaintreeLocator.Xtype,
		        "Plan of Care", 
		        0,
		        "Selecting Plan of care");

		// Next Activity Date = Blank

		Selenium_Util.action(driver,
		        Selenium_Actions_Enum.waitUntilLoadandClearElement,
		        RaintreeLocator.XnextActivityDate,
		        null,
		        0,
		        "Clearing Next Activity Date");

		// Apply Filters

		Selenium_Util.action(driver,
		        Selenium_Actions_Enum.waitUntilLoadandClickElement,
		        RaintreeLocator.XapplyFilters,
		        null,
		        0,
		        "Clicking Apply Filters");
		
		
	
		int pos = 1;
		//int testCount=4;
		
		while (true) {

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

		    int recordCountBefore = Integer.parseInt(
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

		    String rowXpath =
		            RaintreeLocator.Xtable + "[" + pos + "]";
		    
		    
		 // Before processing each record, verify the Background color.
		    WebElement row = driver.findElement(
		            By.xpath(rowXpath));

		    String bgColor = row.getCssValue("background-color");

		    System.out.println("Background Color = " + bgColor);

		    // Skip green rows
		    if ("rgba(189, 242, 164, 1)".contains(bgColor)) {

		        System.out.println(
		                "Skipping Row : " + pos +
		                " because background color is " + bgColor);

		        pos++;
		        continue;
		    }
			    
		   

		    System.out.println("Processing Row : " + pos);

		    Selenium_Util.action(driver,
		            Selenium_Actions_Enum.doubleClickElement,
		            rowXpath,
		            null,
		            0,
		            "Opening Task");

		    Thread.sleep(3000);
		    
		    //Check if continue button is present or not
		    checkForContinueButton();
		    
		    
		    System.out.println("Call processTask()");
		    try {
		    	 processTask();	
		    }catch(Exception e) {
		    	e.printStackTrace();
		    	System.out.println("Error occured processing row number :"+pos);
		    }
		    
		    Thread.sleep(2000);
		    gobackToDashboard();

		    Thread.sleep(2000);
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
		    
//		    if(testCount==6)
//		    	break;
//		    else 
//		    	testCount++;
		   
		}


		System.out.println("=================================");
		System.out.println("ALL ACCOUNTS PROCESSED");
		System.out.println("=================================");
		
		

			System.out.println("======The end=====");
			System.out.println("======The end=====");
			System.out.println("======The end=====");
			System.out.println("======The end=====");
			
} catch (Exception e) {

    logger.error("Error processing location: " + location_id, e);
    continue; // move to next location

       }

		//Closing the browser after all locations are processed
		QuitBot quitBot = QuitBot.getObj();
		quitBot.setQuit(true);
		System.out.println("closing the browser");
		
  }//End of for loop

	}//End of extractData() method

	public static void checkForContinueButton(){
		try {

		    List<WebElement> continueBtns =
		            driver.findElements(
		                    By.xpath("//div[text()='Continue']"));

		    if (!continueBtns.isEmpty()) {

		        System.out.println("Continue button found. Clicking it.");

		        continueBtns.get(0).click();

		        Thread.sleep(2000);

		    } else {

		        System.out.println("Continue button not present. Proceeding normally.");

		    }

		} catch (Exception e) {

		    System.out.println("Error while checking Continue button");
		    e.printStackTrace();

		}
	}
	

	
	//Process Each Task
	 void processTask() throws Exception{
		 String softwareNotes="";
		 
		 long successCount=-1;
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
	    		
	    		//=========== Get PN Number for current account ======//

				for(int i=0;i<divs.size();i++) {
				    System.out.println("Div " + i);
				    System.out.println("TEXT=[" + divs.get(i).getText() + "]");
				}
				if(divs.size()==2) {
					String insurancePayer="";
					
					 historyComments=divs.get(1).getText();	
					 successCount = Pattern.compile(Pattern.quote("Successfully Sent"))
	                         .matcher(historyComments)
	                         .results()
	                         .count();
					 
	                //Get Insurance payer
					  WebElement payerLabel=null;
					  try {
					   payerLabel = driver.findElement(
							    By.xpath("//label[normalize-space()='Primary Ins FC']//following::div[1]//div//label[1]"));
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
					    		 return;
								
							}
							
							
						    //Check for "Signature needed"
						    String description = driver.findElement(
						            By.cssSelector("input[name='emrdesc']"))
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
 	}
	 
	 
	 public void commonStepToProcessAccounts(String historyComments,long successCount) throws Exception{
		 
		 Thread.sleep(1000);
		 
		 WebElement pnField = driver.findElement(
			        By.xpath(RaintreeLocator.XpnNumber));
			String pnNumber = pnField.getAttribute("value");
			//Add PN number to output report
			System.out.println("PN Number = " + pnNumber);
		 
		 String softwareNotes="";
		 
		 if(historyComments.toLowerCase().contains("we have exhausted attempts")) {
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
		 else if(successCount==1) {
			 
			 System.out.println("Success Count is ============ 1");
			 Thread.sleep(3000);
			//========Do not write comment========//
			 softwareNotes="Exception ,2nd attempt not completed";
				//===========Do not write comment==============//
				
				//Create report row before clicking save button
				createReportRow(pnNumber,softwareNotes,successCount);
				Thread.sleep(2000);
			 
			 System.out.println("Commented as =============== "+softwareNotes);
			 
		 }else if(successCount==2) {
			 this.processThirdOrFourthAttempt(successCount,pnNumber,softwareNotes);
				 
		 }else if(successCount==3) {
			 this.processThirdOrFourthAttempt(successCount,pnNumber,softwareNotes);
				
		 }else if(successCount==4) {
			 //work on 5th attempt
			 System.out.println("Success Count is ============ 4");
			  //Update description as "PCC to work" 
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
			 
			//========Comment box START========//
				String comment="We have exhausted attempts to obtain signature from MD. Please notify patient to assist.";
				System.out.println("Success Count is ============ 4");
				 Thread.sleep(3000);
				
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
				
				System.out.println("Commented as =========== "+comment);
				System.out.println("Save button clicked");
				
				Thread.sleep(2000);
		
					 
		 }else if(successCount==5) {
			 System.out.println("Encounter 5th attempt ============= Attempts are exhausted");
			 softwareNotes="Exception";
			 
			//Before you skip , call createReportRow()
			 Thread.sleep(1000);
			 createReportRow(pnNumber, softwareNotes, successCount);
			 //gobackToDashboard();
			 Thread.sleep(1000);
		 }
		 
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
		 reportRows.add(reportRow);
		 
		// Immediately persist to report
		    appendRowToExcel(reportRow);
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
		        		+ "Agent TPG POC has been successfully completed. Please find the attachment\r\n"
		        		+ " \r\n"
		        		+ "Thanks,\r\n"
		        		+ "RPA BOT Support";
		        
		        outputReportFile=new File(reportFilePath);
		        File[] fileAttachments= {outputReportFile};
		        System.out.println("Email sent from try block-----------");
		        EmailSender.sendEmailWithAttachments(toEmails,ccEmails,emailSubject,emailBody,fileAttachments);
	        //Send report email
			
		} catch (Exception e) {
			e.printStackTrace();
			//Send email when exception or error occurs in the program
			System.out.println("Sending email due to error occurance in the program");
			emailSubject="TPG POC Error";
			emailBody=e.getMessage();
	        
	        outputReportFile=new File(reportFilePath);
	        File[] fileAttachments= {outputReportFile};
	        try {
				EmailSender.sendEmailWithAttachments(toEmails,ccEmails,emailSubject,emailBody,fileAttachments);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}finally {
	        System.out.println("Report available at: " + reportFilePath);
	    }
	}
	
	
	
	//Create emailBody
	static String emailBody() {
		return "Please find the attached output report file for all processed account";
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

	static void countUp(int pos) {

		System.out.println(pos);
		Properties properties = new Properties();

		properties.setProperty("rowPos", "" + pos);
		try (FileOutputStream out = new FileOutputStream("config.properties")) {
			properties.store(out, "Updated properties");

			System.out.println("Property updated successfully.");
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
