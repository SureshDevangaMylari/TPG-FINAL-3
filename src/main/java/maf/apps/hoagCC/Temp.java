package maf.apps.hoagCC;

import java.awt.AWTException;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.util.SystemOutLogger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Screen;

import maf.apps.master.selenium.elementspath.RaintreeLocator;
import maf.apps.util.RootPath;

public class Temp {
	public static String auth = "hello";

	public static void main(String[] args) throws ParseException, AWTException, InterruptedException, FindFailed {
		// TODO Auto-generated method stub
		HoagServices h = new HoagServices();
		Thread.sleep(2000);
//		h.saveFile();

		List<String> icdCodes = new ArrayList<String>();
		icdCodes.add("1");
		icdCodes.add("2");
//		test(icdCodes);

		String folderPath = "C:\\Users\\UmeshK\\Downloads\\";
		File mostRecentPdf = Flow.findMostRecentPdf(folderPath);

		if (mostRecentPdf != null) {
			System.out.println("Most recent PDF file: " + mostRecentPdf.getName());
		} else {
			System.out.println("No PDF files found in the specified folder.");
		}
		String filename = folderPath+mostRecentPdf.getName();
		String pdfFilename = folderPath.concat("\\").concat(filename);
		System.out.println(filename);
	}

	public static List<String> dxobj = new ArrayList<String>();

	public static void test(List<String> dxcodes) {

		List<String> icdcodes = new ArrayList<String>(dxcodes);
		System.out.println("icd codes " + icdcodes);
		dxobj = dxcodes;

		dxobj.remove("1");
		dxobj.remove("2");

		System.out.println(icdcodes);
		System.out.println(dxobj);
	}

}
