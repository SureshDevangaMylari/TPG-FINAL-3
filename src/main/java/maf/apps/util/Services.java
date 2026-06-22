package maf.apps.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.sikuli.api.robot.Key;
import org.sikuli.api.robot.desktop.DesktopKeyboard;
import org.sikuli.script.Screen;
 

public class Services {

	static Screen screen = new Screen();
	static DesktopKeyboard keyboard = new DesktopKeyboard();
	public static final Logger logger = LogManager.getLogger(Services.class.getName());

	public Services(WebDriver driver) {
		super();
		this.driver = driver;
	}

	static WebDriver driver;

	static int printSeq = 0;

	/*
	 * saves pdf files with the given name in output folder
	 * 
	 * @param file name
	 */

	public static void print(String fileName) throws InterruptedException {
		System.out.println("printing");

	 
		while (true) {

			try {
				keyboard.keyDown(Key.ENTER);
				keyboard.keyUp(Key.ENTER);

				break;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		while (true) {

			try {
				while (true) {

					if ((screen.exists(RootPath.BASE_DIR + "\\resources\\sikuli\\fileName.png") != null)) {
						logger.info("filename loaded");
						break;
					} else {
						logger.info("not loaded");
					}

				}
				Thread.sleep(1000);
				keyboard.keyDown(Key.BACKSPACE);
				keyboard.keyUp(Key.BACKSPACE);
				screen.click(RootPath.BASE_DIR + "\\resources\\sikuli\\fileName.png");
				if (printSeq == 0) {

					screen.paste(RootPath.BASE_DIR + "\\resources\\output\\pdf\\" + fileName);
					printSeq = 1;
				} else {
					System.out.println("pin =" + fileName);

					screen.paste(fileName);

				}

				break;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		while (true) {

			try {
				keyboard.keyDown(Key.ENTER);
				keyboard.keyUp(Key.ENTER);

				break;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static Map<String, Object> getTable(String xpath, WebDriver driver, String name)

			throws InterruptedException, IOException {
		logger.info("xpath " + xpath);
		List<WebElement> eles = new ArrayList<>();

		List<Object> table = new ArrayList<>();

		Map<String, Object> data = new LinkedHashMap<>();

		Map<String, Object> tableObj = new LinkedHashMap<>();

		List<WebElement> row = new ArrayList<>();

		List<String> headers = new ArrayList<>();

		try {

			eles = driver.findElements(By.xpath(xpath));
			System.out.println("table size " + eles.size());
			row = eles.get(0).findElements(By.tagName("th"));
			System.out.println("th " + row.get(0).getText());
			for (int x = 0; x < row.size(); x++) {

				headers.add(row.get(x).getText());

			}
			boolean skip = false;
			int count = 1;
			for (int i = 1; i < eles.size(); i++) {
				skip = false;
				try {
					row = eles.get(i).findElements(By.tagName("td"));

					logger.info("row" + eles.get(i).getText());

					logger.info(row.size());

					tableObj = new LinkedHashMap<>();

					for (int j = 0; j < headers.size(); j++) {

						tableObj.put(headers.get(j), row.get(j).getText());

					}

				} catch (Exception e) {

				}
				data.put(name + count, tableObj);
				count++;
			}

		} catch (Exception e) {
			logger.info("element not found:  " + e);

		}

		logger.info("table" + data);

		return data;

	}

	public static void swtitchTab(WebDriver driver, int tab) {
		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		while (true) {

			try {
				tabs = new ArrayList<String>(driver.getWindowHandles());
				logger.info(tabs.size() + "  ==>tabs");
				driver.switchTo().window(tabs.get(tab));
				logger.info(tabs.size() + "  ==>tabls changed to " + tab);
				break;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	// switches frames
	public static void swtitchFrame(String frame, WebDriver driver) {
		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		while (true) {

			try {
				driver.switchTo().frame(driver.findElement(By.xpath(frame)));
				logger.info("frame is changed to " + frame);

				break;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
