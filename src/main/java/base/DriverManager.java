package base;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Properties;
import java.util.regex.Pattern;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.github.bonigarcia.wdm.WebDriverManager;
import maf.apps.util.RootPath;

public class DriverManager {

	public static String BASE_DIR = Paths.get(".").toAbsolutePath().normalize().toString();
	public String base_input_screens_path;
	public static boolean osFlag = System.getProperty("os.name").toString().contains("linux");
	public static FileReader reader;
	public static String separator = "\\";
	static final Object monitor = new Object();

	public static WebDriver getdriver() {

		WebDriverManager.chromedriver().setup();
		String os_user = System.getProperty("user.name");
		
		String latest_driver_path = "C:\\Users\\" + os_user + "\\.cache\\selenium\\chromedriver\\win64\\";
		File folder = new File(latest_driver_path);
		File[] listOfFiles = folder.listFiles();
		String[] arrValues = listOfFiles[0].toString().split(Pattern.quote(separator));
		String version = "";
		for (int i = 0; i < arrValues.length; i++) {
			version = arrValues[arrValues.length - 1];
		}
		String downloadFilepath = RootPath.BASE_DIR + "\\resources\\pdfDownload";
		
		System.setProperty("webdriver.chrome.driver", BASE_DIR + "//chromedriver.exe");
		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("download.default_directory", downloadFilepath);
		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("prefs", chromePrefs);
		options.addArguments("--remote-allow-origins=*");
		DesiredCapabilities cap = DesiredCapabilities.chrome();
		cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		cap.setCapability(ChromeOptions.CAPABILITY, options);
		ChromeOptions o = new ChromeOptions();
		// setting debuggerAddress value
		o.setExperimentalOption("debuggerAddress", "localhost:50735");
		@SuppressWarnings("deprecation")
		
		
		WebDriver driver = new ChromeDriver(cap);
		driver.manage().window().maximize();

		return driver;
	}

	public static String getpropertyvalue(String property) throws IOException {
		if (osFlag) {
			reader = new FileReader(BASE_DIR + "/resources/env.properties");
		} else {
			reader = new FileReader(BASE_DIR + "\\resources\\env.properties");
		}

		Properties p = new Properties();
		p.load(reader);
		System.out.println("val " + p.getProperty(property));
		return p.getProperty(property);
	}

	public static void sleep(int time) throws InterruptedException {
		synchronized (monitor) {
			monitor.wait(time);
		}
	}

	public static void createDir(String path) {
		File f1 = new File(path);
		f1.mkdir();
	}

}