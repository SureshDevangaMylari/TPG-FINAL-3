
package maf.apps.hoagCC;

import java.awt.AWTException;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.sikuli.script.FindFailed;

import maf.apps.base.DriverManager;
import maf.apps.base.PopupsHandler;
import maf.apps.util.FileUtil;
import maf.apps.util.RootPath;

public class Init extends DriverManager {

	static DriverManager dm = new DriverManager();
	public static final Logger logger = LogManager.getLogger(Init.class);
// Proceed with current dates
	public static void main(String[] args) throws IOException, FindFailed, InterruptedException, AWTException, ParseException {
		logger.info("Sequoia  bot started");
		System.setProperty("webdriver.chrome.driver", RootPath.BASE_DIR + "\\chromedriver.exe");
		String downloadFilepath = RootPath.BASE_DIR + "\\resources\\pdfDownload";
		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("download.default_directory", downloadFilepath);
		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("prefs", chromePrefs);
		DesiredCapabilities cap = DesiredCapabilities.chrome();
		cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		cap.setCapability(ChromeOptions.CAPABILITY, options);
		ChromeOptions o = new ChromeOptions();
		// setting debuggerAddress value
		o.setExperimentalOption("debuggerAddress", "localhost:60764");
		WebDriver driver = new ChromeDriver(cap);

//		toolbar3button1	
		
		// Read input excel and fetch data from excel to MAP (key, value) pair  id.ACTIVEOVERLAP!!11.btnProceed

		Flow dp = new Flow(driver);
		PopupsHandler popupsHandler = new PopupsHandler();
		Thread thread1 = new Thread(dp);
		Thread thread2 = new Thread(popupsHandler.threadtwofun(driver));
		Thread thread3 = new Thread(popupsHandler.threadokwarningfun(driver));
		Thread thread4 = new Thread(popupsHandler.threadcontinuefun(driver));

		Thread thread5 = new Thread(popupsHandler.threadtalertfun(driver));
		Thread thread6 = new Thread(popupsHandler.threadtalertclosefun(driver));
		logger.info("app");
		thread1.start();
		thread2.start();

		thread3.start();
		thread4.start();
		thread5.start();
		thread6.start();
	}

}
