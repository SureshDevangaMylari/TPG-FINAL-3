package maf.apps.hoagCC;

import java.io.File;
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
import org.sikuli.script.FindFailed;
import org.sikuli.script.Screen;

import maf.apps.master.selenium.elementspath.RaintreeLocator;
import maf.apps.util.FileUtil;
import maf.apps.util.RootPath;
import maf.apps.util.Selenium_Actions_Enum;
import maf.apps.util.Selenium_Util;

public class RTservices {
	public static final Logger logger = LogManager.getLogger(RTservices.class);
	static Selenium_Actions_Enum senum;

	public static void submission(WebDriver driver, Map<String, Object> data) throws InterruptedException {

		Screen screen = new Screen();
		LocalDate date = LocalDate.now();
		LocalDate next6day = date.plusDays(6);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
		String next6Date = formatter.format(next6day);
		String currentdate = formatter.format(date);

		String authNum = HoagServices.auth;
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
//
//		for the date

		LocalDate currentDate = LocalDate.now();

		LocalDate _workingDate = null;

		// 7days
		_workingDate = currentDate.plusDays(6);

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
		Selenium_Util.action(driver, senum.selectCheckboxByValue, RaintreeLocator.XstatusCategory, "Pending with Insurance", 0, " selecting \"Pending with Insurance\"");

//		Selenium_Util.action(driver, senum.sendText, RaintreeLocator.XauthInput, "Pending with Insurance", 0, " selecting \"Pending with Insurance\"");
		Selenium_Util.action(driver, Selenium_Actions_Enum.sendText, RaintreeLocator.XnextActivity, formattedDate6, 0, "sending 6 working date");

		Selenium_Util.action(driver, Selenium_Actions_Enum.sendTextById, RaintreeLocator.IreqDate, reqDate, 0, " sending requsting currentdate");

		Selenium_Util.action(driver, Selenium_Actions_Enum.sendTextById, RaintreeLocator.IreqVisit, data.get("req").toString(), 0, " sending requestedVisits");

//		Selenium_Util.action(driver, Selenium_Actions_Enum.clickElementById, RaintreeLocator.Isave, authNum, 0, " clicking save btn");

//		pos++;

	}

	public static void retrieval(WebDriver driver, Map<String, Object> data) throws InterruptedException, ParseException, FindFailed {
		logger.info("data " + data);
		String d = (String) data.get("authorized_from");
		String d2 = (String) data.get("authorized_expires");
		SimpleDateFormat formatter1 = new SimpleDateFormat("MM/dd/yyyy");
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
		Selenium_Util.action(driver, Selenium_Actions_Enum.sendText, RaintreeLocator.Xstatus, "Active", 0, "sending status");
		Thread.sleep(1000);
		Selenium_Util.action(driver, Selenium_Actions_Enum.selectCheckboxByValue, RaintreeLocator.XstatusCategory, "", 0, "Selecting the status");
		Selenium_Util.action(driver, Selenium_Actions_Enum.sendText, RaintreeLocator.XreceivedDate, reqDate, 0, "sending recieved date");
		Selenium_Util.action(driver, Selenium_Actions_Enum.sendText, RaintreeLocator.XreceivedVisits, data.get("req").toString(), 0, "sending recieved visits");

		Selenium_Util.action(driver, Selenium_Actions_Enum.sendText, RaintreeLocator.XeffectDate, from, 0, "sending effective date");

		Selenium_Util.action(driver, Selenium_Actions_Enum.sendText, RaintreeLocator.XeffecDateto, to, 0, "sending effective date to ");
		String simple = driver.findElement(By.xpath("//*[@aria-label='Simple visit based auth']")).getAttribute("readonly");
		logger.info(" simple" + simple);
		if (simple == null) {
			logger.info(" simple auth");
		} else {
			logger.info(" advance auth");
			Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, RaintreeLocator.Xsimpleauth, "", 0, " clicking simple auth");
			Thread.sleep(2000);
		}
		Selenium_Util.action(driver, Selenium_Actions_Enum.sendText, RaintreeLocator.Xallowed, data.get("req").toString(), 0, " sending allowed visits");

		uploadDoc(driver);
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
		String folderPath = "C:\\Users\\UmeshK\\Downloads\\";
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

	}

	public static void submitApproval(WebDriver driver, Map<String, Object> data) throws InterruptedException, ParseException, FindFailed {
		submission(driver, data);

		logger.info("data " + data);
		String d = (String) data.get("authorized_from");
		String d2 = (String) data.get("authorized_expires");
		SimpleDateFormat formatter1 = new SimpleDateFormat("MM/dd/yyyy");
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
		Selenium_Util.action(driver, Selenium_Actions_Enum.sendText, RaintreeLocator.Xstatus, "Active", 0, "sending status");
		Thread.sleep(1000);
		Selenium_Util.action(driver, Selenium_Actions_Enum.selectCheckboxByValue, RaintreeLocator.XstatusCategory, "", 0, "Selecting the status");
		Selenium_Util.action(driver, Selenium_Actions_Enum.sendText, RaintreeLocator.XreceivedDate, reqDate, 0, "sending recieved date");
		Selenium_Util.action(driver, Selenium_Actions_Enum.sendText, RaintreeLocator.XreceivedVisits, data.get("req").toString(), 0, "sending recieved visits");

		Selenium_Util.action(driver, Selenium_Actions_Enum.sendText, RaintreeLocator.XeffectDate, from, 0, "sending effective date");

		Selenium_Util.action(driver, Selenium_Actions_Enum.sendText, RaintreeLocator.XeffecDateto, to, 0, "sending effective date to ");
		Thread.sleep(1000);
		String simple = driver.findElement(By.xpath("//*[@aria-label='Simple visit based auth']")).getAttribute("readonly");
		logger.info(" simple" + simple);
		if (simple == null) {
			logger.info(" simple auth");
		} else {
			logger.info(" advance auth");
			Selenium_Util.action(driver, Selenium_Actions_Enum.clickElement, RaintreeLocator.Xsimpleauth, "", 0, " clicking simple auth");
			Thread.sleep(1000);
		}
		Selenium_Util.action(driver, Selenium_Actions_Enum.sendText, RaintreeLocator.Xallowed, data.get("req").toString(), 0, " sending allowed visits");

		uploadDoc(driver);
		Thread.sleep(1000);

		Selenium_Util.action(driver, Selenium_Actions_Enum.waitWhilelLoad, "//*[text()='Browse...']", from, 0, "uploading doc");

		Selenium_Util.action(driver, Selenium_Actions_Enum.clickElementById, RaintreeLocator.Isave, "", 0, " clicking save btn");

		Thread.sleep(4000);
		try {
			driver.findElement(By.xpath("//*[text()='Proceed with current dates']")).click();
		} catch (Exception e) {

		}
	}
	// *[@name='status']

}
