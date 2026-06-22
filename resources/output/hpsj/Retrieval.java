package maf.apps.TricareRetrieval;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Screen;

import maf.apps.HumanaAuth.HumanaServices;
import maf.apps.hoagCC.Flow;
import maf.apps.master.selenium.elementspath.Humana;
import maf.apps.master.selenium.elementspath.RaintreeLocator;
import maf.apps.util.RootPath;
import maf.apps.util.Selenium_Actions_Enum;
import maf.apps.util.Selenium_Util;

public class Retrieval {
	private static final Logger logger = LogManager.getLogger(HumanaServices.class.getName());
	static Selenium_Actions_Enum senum;
	static Selenium_Util util;
	static Humana xpath;
	static WebDriver driver;
	
	public static void doRetrieval(WebDriver driver, Map<String, Object> data) throws InterruptedException, ParseException, FindFailed {

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
		Selenium_Util.action(driver, senum.waitUntilLoad, "//*[@id='id.AUTH!!8.nextActivityComment']", "", 0,"waiting for the cmnt box");
		WebElement nextActivityComment = driver.findElement(By.id("id.AUTH!!8.nextActivityComment"));
		String temp = nextActivityComment.getText();
		JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

//		nextActivityComment.sendKeys(org.openqa.selenium.Keys.CONTROL);
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
		Selenium_Util.action(driver, Selenium_Actions_Enum.sendText, RaintreeLocator.Xstatus, "Active", 0, "sending status");
		Thread.sleep(1000);
		Selenium_Util.action(driver, Selenium_Actions_Enum.selectCheckboxByValue, RaintreeLocator.XstatusCategory, "", 0, "Selecting the status");
		Selenium_Util.action(driver, Selenium_Actions_Enum.sendText, RaintreeLocator.XreceivedDate, reqDate, 0, "sending recieved date");
		Selenium_Util.action(driver, Selenium_Actions_Enum.sendText, RaintreeLocator.XreceivedVisits, data.get("reqVisits").toString(), 0, "sending recieved visits");

		Selenium_Util.action(driver, Selenium_Actions_Enum.sendText, RaintreeLocator.XeffectDate, from, 0, "sending effective date");

		Selenium_Util.action(driver, Selenium_Actions_Enum.sendText, RaintreeLocator.XeffecDateto, to, 0, "sending effective date to ");
		String simple = driver.findElement(By.xpath("//*[@aria-label='Simple visit based auth']")).getAttribute("readonly");
		logger.info(" simple" + simple);
		if (simple == null) {
			logger.info(" simple auth");
		} else {
			logger.info(" advance auth");
			Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, RaintreeLocator.Xsimpleauth, "", 0, " clicking simple auth");
			Thread.sleep(1000);
		}

		Selenium_Util.action(driver, Selenium_Actions_Enum.sendText, RaintreeLocator.Xallowed, data.get("reqVisits").toString(), 0, " sending allowed visits");
		uploadDoc(driver);
		Thread.sleep(1000);
		Selenium_Util.action(driver, Selenium_Actions_Enum.clickElementById, RaintreeLocator.Isave, "", 0, " clicking save btn");

	}
	public static void uploadDoc(WebDriver driver) throws InterruptedException, FindFailed {
		driver.findElement(By.xpath("//*[text()='Browse...']")).click();
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
		Selenium_Util.action(driver, Selenium_Actions_Enum.waitWhilelLoad, "//*[text()='Browse...']", "", 0, "uploading doc");

	}
}
