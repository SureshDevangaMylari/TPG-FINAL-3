package maf.apps.HumanaAuth;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import maf.apps.util.RootPath;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

public class ChromeDriverManager {
	private static final String DEBUGGER_ADDRESS = "127.0.0.1";
	private static final int DEBUGGER_PORT = 9222;
	private static final String USER_DATA_DIR = RootPath.BASE_DIR + "/chromedriver.exe"; // persistent profile dir
	private static final String DOWNLOAD_DIR = RootPath.BASE_DIR + "\\resources\\pdfDownload";

	public static WebDriver getDriver() {
		if (isDebuggerPortOpen(DEBUGGER_ADDRESS, DEBUGGER_PORT)) {
			System.out.println("Connecting to existing Chrome instance...");
			ChromeOptions options = new ChromeOptions();
			options.setExperimentalOption("debuggerAddress", DEBUGGER_ADDRESS + ":" + DEBUGGER_PORT);
			return new ChromeDriver(options);
		} else {
			System.out.println("No existing Chrome found. Launching new Chrome...");
			HashMap<String, Object> prefs = new HashMap<>();
			prefs.put("download.default_directory", DOWNLOAD_DIR);
			prefs.put("download.prompt_for_download", false);
			prefs.put("profile.default_content_settings.popups", 0);

			ChromeOptions options = new ChromeOptions();
			options.setExperimentalOption("prefs", prefs);
			options.addArguments("--remote-debugging-port=" + DEBUGGER_PORT);
			options.addArguments("--user-data-dir=" + USER_DATA_DIR);

			return new ChromeDriver(options);
		}
	}

	private static boolean isDebuggerPortOpen(String host, int port) {
		try (Socket socket = new Socket(host, port)) {
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public static void main(String[] args) {
		WebDriver driver = getDriver();

		driver.get("https://www.google.com");

		// Your test code here...

		// Note: If you attached to an existing browser, closing driver will not close
		// Chrome
		// If you launched new Chrome, driver.quit() will close it.
	}
}
