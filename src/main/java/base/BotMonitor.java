package base;

import java.io.File;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import maf.apps.util.RootPath;

public class BotMonitor implements Runnable {

private static volatile long lastActivityTime = System.currentTimeMillis();

private static volatile boolean alertSent = false;

// 1 minute for testing
private static final long TIMEOUT = 5 * 60 * 1000;

public static void updateHeartbeat() {

    lastActivityTime = System.currentTimeMillis();
    //System.out.println("BOT last activity time: "+LocalTime.now());

    // Reset alert if bot becomes active again
    alertSent = false;

}

@Override
public void run() {

    while (true) {

        try {

            long currentTime =
                    System.currentTimeMillis();

            long inactivityTime =
                    currentTime - lastActivityTime;

            System.out.println(
                    "Monitor Check -> Inactivity: "
                    + inactivityTime/1000 + " seconds");

            if (inactivityTime >= TIMEOUT
                    && !alertSent) {

                System.out.println(
                        "BOT STUCK DETECTED");
                System.out.println(
                	    "Trying to send stuck alert email...");

                boolean mailSent = sendStuckAlertEmail( inactivityTime);
                if(mailSent) { 
                	alertSent = true; 
                	System.out.println("Alert email sent successfully");
                }

            }

            Thread.sleep(3000);

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

}

		public static boolean sendStuckAlertEmail(
		        long inactivityTime) {
		
		    try {
		
		        String subject =
		                "TPG POC BOT ALERT";
		
		        String body =
		                "Hi Team,\n\n"
		                + "TPG Bot appears to be stuck "
		                + "or inactive.\n"
		                + "No activity detected for : "
		                + (inactivityTime / 1000)
		                + " seconds.\n\n"
		                +"Location ID: "+Flow.currentLocation
		                +"\nPN Number: "+Flow.currentPnNumber
		                +"\nPlease find so far processed accounts in the attached report and UI screenshot for you reference"
		                + "\n\nThanks & Regards\n"
		                + "RPA BOT Support";
		        
		        //Include report as well
		        File outputReportFile = null;

		        if(Flow.reportFilePath != null) {
	
			        outputReportFile =
			                new File(Flow.reportFilePath);
	
			        if(!outputReportFile.exists()) {
	
			            outputReportFile = null;
	
			            System.out.println(
			                    "Report file does not exist");
			        }

		        }

		        
		        // TAKE SCREENSHOT 
		        File screenshotFile = takeScreenshot(); 
		        
		        // ATTACH SCREENSHOT 
		        File[] attachments;

		        if(outputReportFile != null) {
			        attachments = new File[] {
			                outputReportFile,
			                screenshotFile
			        };
		        } else {
			        attachments = new File[] {
			                screenshotFile
			        };
		        
		        }

		
		        EmailSender.sendEmailWithAttachments(
		                Flow.toEmails,
		                Flow.ccEmails,
		                subject,
		                body,
		                attachments);
		
		        System.out.println(
		                "Stuck alert email sent.");
		        return true;
		
		    } catch (Exception e) {
		
		    	System.out.println( "Failed to send alert email");
		    	e.printStackTrace(); 
		    	return false;
		
		    }
		
		}

		public static File takeScreenshot()
			throws Exception {
			
			TakesScreenshot ts =
			        (TakesScreenshot) Flow.driver;
			
			File sourceFile =
			        ts.getScreenshotAs(
			                OutputType.FILE);
			
			 String timestamp = LocalDateTime.now()
         	        .format(DateTimeFormatter.ofPattern("dd-MM-yy_HH-mm-ss"));
			
			 String screenshotPath = RootPath.BASE_DIR 
						+"\\resources"
				        +"\\screenshots\\"
				        + "BOT_STUCK_Screenshot"
				        + timestamp
				        + ".png";
			
			File destinationFile =
			        new File(screenshotPath);
			
			FileUtils.copyFile(
			        sourceFile,
			        destinationFile);
			
			System.out.println(
			        "Screenshot captured: "
			        + screenshotPath);
			
			return destinationFile;
			
		}


}
