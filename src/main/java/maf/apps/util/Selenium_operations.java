package maf.apps.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;



public class Selenium_operations {
	private static final Logger logger = LogManager.getLogger(Selenium_operations.class.getName());
	public static List<WebElement> getWebElements(WebDriver driver,  Selenium_Actions_Enum actiontype, String xpath, String inputtext, String sikuliKey,
			String keyUnicode, String patternpath, int tab,String description) throws InterruptedException {

		int errCounter = 0;

		List<WebElement> eles = new ArrayList<>();

		logger.info("xpath " + xpath);

		while (true) {

			try {

				eles = driver.findElements(By.xpath(xpath));

				break;

			} catch (Exception e) {

				Thread.sleep(1000);

				errCounter += 1;

				if (errCounter > 100) {

					logger.error("element not found:  " + e.getMessage());

					errCounter = 0;

				}

			}

		}

		return eles;

	}
}
