
package maf.apps.util;

import static org.hamcrest.CoreMatchers.notNullValue;

import java.awt.event.KeyEvent;

import java.io.File;

import java.io.FileWriter;

import java.io.IOException;

import java.util.ArrayList;

import java.util.LinkedHashMap;

import java.util.List;

import java.util.Map;

import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.sikuli.script.FindFailed;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Operations {

	private static final Logger logger = LogManager.getLogger(Operations.class.getName());

	static WebDriver driver;

	public Operations() {

		this.driver = driver;

	}

	public Operations(WebDriver driver) {

		this.driver = driver;

	}

	/*
	 * 
	 * // Use for the clicking web element // takes two parameteres // 1. xpath //
	 * 
	 * 2. Web driver
	 * 
	 */

	public static void clickElement(String element, WebDriver driver) throws InterruptedException {

		WebElement tmp = null;
		Actions actions = new Actions(driver);
		int errCounter = 11;

		while (true) {

			try {

				tmp = driver.findElement(By.xpath(element));
				actions.moveToElement(tmp);

				Thread.sleep(2000);
				tmp.click();

				logger.info("clicked.. " + element);

				break;

			} catch (Exception e) {

				Thread.sleep(1500);

				errCounter += 1;

				if (errCounter > 10) {
					e.printStackTrace();
					logger.error("element not found:  " + element);

					errCounter = 0;

				}

			}

		}

	}
	/*
	 * 
	 * // Use for the clicking web element // takes two parameteres // 1. id //
	 * 
	 * 2. Web driver
	 * 
	 */

	public static void clickElementById(String element, WebDriver driver) throws InterruptedException {

		WebElement tmp = null;

		int errCounter = 0;

		while (true) {

			try {

				tmp = driver.findElement(By.id(element));

				tmp.click();

				logger.info("clicking " + element);

				break;

			} catch (Exception e) {

				Thread.sleep(1000);

				errCounter += 1;

				if (errCounter > 10) {

					logger.error("element not found:  " + element);

					errCounter = 0;

				}

			}

		}

	}
	/*
	 * 
	 * send text to webelements
	 * 
	 * 
	 * 
	 * @param web element xpath
	 * 
	 * 
	 * 
	 * @param text to enter
	 * 
	 * 
	 * 
	 * @param driver
	 * 
	 */

	public static void sendText(String element, String text, WebDriver driver) throws InterruptedException {

		int errCounter = 5;

		while (true) {

			try {

				WebElement ele = driver.findElement(By.xpath(element));

				ele.clear();

				ele.sendKeys(text);

				logger.info("sended text " + text);

				break;

			} catch (Exception e) {

				errCounter += 1;

				if (errCounter >= 5) {

					logger.error("element not found:  " + element);
//					e.printStackTrace();
					Thread.sleep(1000);
					errCounter = 0;

				}

			}

		}

	}

	/*
	 * 
	 * clicks and send text to webelements
	 * 
	 * 
	 * 
	 * @param web element xpath
	 * 
	 * 
	 * 
	 * @param text to enter
	 * 
	 * 
	 * 
	 * @param driver
	 * 
	 */
	public static void clickAndsendText(String element, String text, WebDriver driver) throws InterruptedException {

		int errCounter = 5;

		while (true) {

			try {

				WebElement ele = driver.findElement(By.xpath(element));

				ele.click();
				Thread.sleep(500);
				ele.clear();

				ele.sendKeys(text);

				logger.info("sended text " + text);

				break;

			} catch (Exception e) {

				Thread.sleep(500);
				errCounter += 1;

				if (errCounter >= 5) {

					logger.error("element not found:  " + element);

					errCounter = 0;

				}

			}

		}

	}

	/*
	 * 
	 * checks until element loading
	 * 
	 * 
	 * 
	 * @param xpath of the element
	 * 
	 * 
	 * 
	 * @param driver
	 * 
	 */

	public static void waitUntilLoad(String xpath, WebDriver driver) throws InterruptedException {

		int errCounter = 10;

		while (true) {

			try {

				driver.findElement(By.xpath(xpath));
				logger.info("element found:  " + xpath);

				break;

			} catch (Exception e) {

				errCounter++;

				if (errCounter >= 10) {

					logger.error("element not found:  " + xpath);

					errCounter = 0;

				}

				Thread.sleep(1000);

			}

		}

	}

	/*
	 * 
	 * checks until element loading
	 * 
	 * 
	 * 
	 * @param xpath of the element
	 * 
	 * 
	 * 
	 * @param driver
	 * 
	 */

	public static void waitwhileLoading(String xpath, WebDriver driver) throws InterruptedException {

		int errCounter = 10;

		while (true) {

			try {

				driver.findElement(By.xpath(xpath));
				logger.info("Loading:  " + xpath);

			} catch (Exception e) {

				Thread.sleep(1000);

				errCounter += 1;

				if (errCounter >= 10) {

					logger.error("Loaded:  " + xpath);

					errCounter = 0;

				}

				Thread.sleep(1000);
				break;
			}

		}

	}

	/*
	 * 
	 * goback and creates json file
	 * 
	 * 
	 * 
	 * @param driver
	 * 
	 * 
	 * 
	 * @param Map to print in json file
	 * 
	 * 
	 * 
	 * @data for the filename
	 * 
	 */

	public static void goBackAndPrint(WebDriver driver, Map<String, Object> MainJSOn, String name)

			throws JsonProcessingException, IOException, InterruptedException {

		File datafile;

		FileWriter fileWriter2 = null;

		ObjectMapper mapper = new ObjectMapper();

		try {

			// datafile = new File(path + "\\" + data[0].substring(1, data[0].length()) +

			// "-" + d + ".json");

			datafile = new File(RootPath.BASE_DIR + "\\resources\\output\\json\\" + name + ".json");

			fileWriter2 = new FileWriter(datafile);

			fileWriter2.write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(MainJSOn).toString());

			fileWriter2.close();

		} catch (IOException e) {

			logger.info(e);

		}

		while (true) {
			try {

				List<WebElement> hidden = Operations.getWebElements("//*[@class='screen-status screen-status-hidden']",
						driver);

				logger.info("hidden size" + hidden.size());

				if (hidden.size() == 1) {

					logger.info("got patient files");

					break;

				}

				else {

					try {

						Thread.sleep(500);
					}

					catch (Exception e2) {

					}

				}

			} catch (Exception e) {

				Thread.sleep(500);

				e.printStackTrace();

				try {

				}

				catch (Exception e2) {

				}

			}

		}

	}

	/*
	 * 
	 * get text of web element
	 * 
	 * 
	 * 
	 * @param xpath of the web element
	 * 
	 * 
	 * 
	 * @param driver
	 * 
	 */

	public static String getText(String xpath, WebDriver driver) throws InterruptedException {

		logger.info("Xpath " + xpath);

		String text = "";

		try {

			text = driver.findElement(By.xpath(xpath)).getText();
			logger.info("text " + text);

		} catch (Exception e) {

			logger.error("element  text not found:  " + e.getMessage());

		}

		return text.replace("\n", "");

	}

	public static String getTextRaw(String xpath, WebDriver driver) throws InterruptedException {

		logger.info("Xpath " + xpath);

		String text = "";

		try {

			text = driver.findElement(By.xpath(xpath)).getText();
			logger.info("text " + text);

		} catch (Exception e) {

			logger.error("element  text not found:  " + e.getMessage());

		}

		return text;

	}
	/*
	 * 
	 * get text of web element
	 * 
	 * 
	 * 
	 * @param id of the web element
	 * 
	 * 
	 * 
	 * @param driver
	 * 
	 */

	public static String getTextById(String id, WebDriver driver) throws InterruptedException {

		logger.info("id" + id);

		String text = "";

		try {

			text = driver.findElement(By.id(id)).getText();

		} catch (Exception e) {

			logger.error("element not found:  " + e.getMessage());

		}

		return text;

	}

	/*
	 * 
	 * to get table in list of objects
	 * 
	 * 
	 * 
	 * @param xpath of rows of tables
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @param driver
	 * 
	 */

	public static List<Object> getTable(String xpath, WebDriver driver) throws InterruptedException, IOException {

		List<WebElement> eles = new ArrayList<>();

		List<Object> table = new ArrayList<>();

		Map<String, Object> tableObj = new LinkedHashMap<>();

		List<WebElement> row = new ArrayList<>();

		List<String> headers = new ArrayList<>();

		try {

			eles = driver.findElements(By.xpath(xpath));
			logger.info("table size " + eles.size());

			row = eles.get(0).findElements(By.tagName("th"));

			for (int x = 0; x < row.size(); x++) {

				headers.add(row.get(x).getText());

			}

			for (int i = 1; i < eles.size(); i++) {

				row = eles.get(i).findElements(By.tagName("td"));

				tableObj = new LinkedHashMap<>();

				for (int j = 0; j < headers.size(); j++) {

					tableObj.put(headers.get(j), row.get(j).getText());

				}

				table.add(tableObj);

			}

		} catch (Exception e) {

			logger.error("element not found:  " + e.getMessage());

		}

		logger.info("table" + table);

		return table;

	}

	/*
	 * 
	 * returns list of web elements(webelement type)
	 * 
	 * 
	 * 
	 * @param xpath of the input web element
	 * 
	 * 
	 * 
	 * @param driver
	 * 
	 */

	public static List<WebElement> getWebElements(String xpath, WebDriver driver) throws InterruptedException {

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

	/*
	 * 
	 * get text from input tag
	 * 
	 * 
	 * 
	 * @param xpath of the input web element
	 * 
	 * 
	 * 
	 * @param driver
	 * 
	 */

	public static String getTextFromInput(String xpath, WebDriver driver) throws InterruptedException {

		logger.info("Xpath" + xpath);

		String text = "";

		try {

			text = driver.findElement(By.xpath(xpath)).getAttribute("value");

		} catch (Exception e) {

			logger.error("element not found:  " + e.getMessage());

		}

		logger.info("text" + text);

		return text;

	}

	/*
	 * 
	 * returns radio button's result true or false
	 * 
	 * 
	 * 
	 * @param xpath of the input web element
	 * 
	 * 
	 * 
	 * @param driver
	 * 
	 */

	public static boolean isSelected(String xpath, WebDriver driver) throws InterruptedException {

		boolean isSelected = false;
		while (true) {
			try {

				isSelected = driver.findElement(By.xpath(xpath)).isSelected();
				break;
			} catch (Exception e) {

				logger.error("element not found:  " + e.getMessage());

			}
		}

		logger.info(isSelected);
		return isSelected;

	}

	/*
	 * 
	 * return the table in map of objects
	 * 
	 * 
	 * 
	 * @param xpath of rows of tables
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @param driver
	 * 
	 */

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
			System.out.println("th " + row.size());
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

					tableObj = new LinkedHashMap<>();

					for (int j = 0; j < headers.size(); j++) {

						tableObj.put(headers.get(j), row.get(j).getText());
						if (row.get(j).getText().isEmpty()) {
							skip = true;
						}

					}

				} catch (Exception e) {

				}
				if (!skip) {
					data.put(name + count, tableObj);
					count++;
				}
			}

		} catch (Exception e) {

			logger.info("element not found:  " + e);

		}

		logger.info("table" + data);

		return data;

	}

	/*
	 * 
	 * send text to webelements
	 * 
	 * 
	 * 
	 * @param web element id
	 * 
	 * 
	 * 
	 * @param text to enter
	 * 
	 * 
	 * 
	 * @param driver
	 * 
	 */

	public static void sendTextById(String element, String text, WebDriver driver) throws InterruptedException {

		int errCounter = 10;

		while (true) {

			try {

				WebElement ele = driver.findElement(By.id(element));

				ele.click();

				ele.clear();

				ele.sendKeys(text);

				break;

			} catch (Exception e) {

				errCounter += 1;

				if (errCounter >= 10) {

					logger.error("element not found:  " + element);

					errCounter = 0;

				}

				try {

				} catch (Exception e2) {

					// logger.info("element not found: //*[@data-widgetname=\\'btncontinue\\']");

					Thread.sleep(1500);

				}

			}

		}

	}

	/*
	 * 
	 * @param driver
	 * 
	 * 
	 * 
	 * @param Map to print in json file
	 * 
	 * 
	 * 
	 * @data for the filename
	 * 
	 */

	public static void Print(WebDriver driver, Map<String, Object> MainJSOn, String[] data)

			throws JsonProcessingException, IOException, InterruptedException {

		String d = data[1].replace('/', '-');

		File datafile;

		FileWriter fileWriter2 = null;

		ObjectMapper mapper = new ObjectMapper();

		try {

			// datafile = new File(path + "\\" + data[0].substring(1, data[0].length()) +

			// "-" + d + ".json");

			datafile = new File(
					RootPath.BASE_DIR + "\\resources\\output\\json\\" + data[0].substring(1, data[0].length()) + "-"

							+ d + ".json");

			fileWriter2 = new FileWriter(datafile);

			fileWriter2.write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(MainJSOn).toString());

			fileWriter2.close();

		} catch (IOException e) {

			logger.info(e);

			// e.printStackTrace();

		}

	}
	/*
	 * 
	 * @param driver
	 * 
	 * 
	 * 
	 * @param Map to print in json file
	 * 
	 * 
	 * 
	 * filename for the filename
	 * 
	 */

	public static void PrintJsonFile(WebDriver driver, Map<String, Object> MainJSOn, String filename)

			throws JsonProcessingException, IOException, InterruptedException {
		System.out.println("in json print");
		File datafile;

		FileWriter fileWriter2 = null;

		ObjectMapper mapper = new ObjectMapper();

		try {

			// datafile = new File(path + "\\" + data[0].substring(1, data[0].length()) +

			// "-" + d + ".json");

			datafile = new File(RootPath.BASE_DIR + "\\resources\\output\\json\\" + filename + ".json");

			fileWriter2 = new FileWriter(datafile);

			fileWriter2.write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(MainJSOn).toString());

			fileWriter2.close();

		} catch (IOException e) {

			logger.info(e);

		}

	}

	/*
	 * 
	 * clicks and send text to webelements
	 * 
	 * 
	 * 
	 * @param web element id
	 * 
	 * 
	 * 
	 * @param text to enter
	 * 
	 * 
	 * 
	 * @param driver
	 * 
	 */
	public static void clickAndsendTextById(String element, String text, WebDriver driver) throws InterruptedException {

		int errCounter = 1000;

		while (true) {

			try {

				WebElement ele = driver.findElement(By.id(element));

				ele.click();

				ele.clear();

				ele.sendKeys(text);

				logger.info("sended text " + text);

				break;

			} catch (Exception e) {

				Thread.sleep(1000);

				errCounter += 1;

				if (errCounter >= 5) {

					logger.error("element not found:  " + element);

					errCounter = 0;

				}

				try {

				} catch (Exception e2) {

					logger.info("element not found: //*[@data-widgetname=\\'btncontinue\\']");

					Thread.sleep(1500);

				}

			}

		}

	}

	/*
	 * selects by value from the given value to input tag
	 * 
	 * @param xpath of web element
	 * 
	 * @param index value
	 * 
	 * @param driver
	 */
	public static void selectCheckboxByValue(String xpath, String value, WebDriver driver) throws InterruptedException {
		int errCounter = 10;
		while (true) {

			try {
				Select s = new Select(driver.findElement(By.xpath(xpath)));
				s.selectByValue(value);
				break;
			} catch (Exception e) {
				Thread.sleep(1000);

				errCounter += 1;

				if (errCounter >= 10) {

					e.printStackTrace();
					errCounter = 0;

				}

			}
		}

	}
	/*
	 * selects by index from the input tag
	 * 
	 * @param xpath of web element
	 * 
	 * @param index value
	 * 
	 * @param driver
	 */

	public static void selectCheckboxByIndex(String xpath, int value, WebDriver driver) throws InterruptedException {
		int errCounter = 10;
		while (true) {

			try {
				Select s = new Select(driver.findElement(By.xpath(xpath)));
				s.selectByIndex(value);
				logger.info("slectedd " + xpath + " with " + value);
				break;
			} catch (Exception e) {
//				Thread.sleep(1000);

				if (errCounter >= 10) {
					errCounter += 1;
					e.printStackTrace();
					errCounter = 0;

				}

			}
		}

	}

	/*
	 * clicks the link text
	 * 
	 * @param link
	 * 
	 * @param webdriver
	 */
	public static void clickLinkText(String link, WebDriver driver) {

		while (true) {

			try {

				driver.findElement(By.linkText(link)).click();
				;
				break;
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	public static void doubleClickElement(String element, WebDriver driver) throws InterruptedException {
		Actions actions = new Actions(driver);
		WebElement tmp = null;

		int errCounter = 10;

		while (true) {

			try {

				tmp = driver.findElement(By.xpath(element));
				actions.moveToElement(tmp);

				actions.doubleClick(tmp).build().perform();

				logger.info("double clicked.. " + element);

				break;

			} catch (Exception e) {

				Thread.sleep(500);

				errCounter += 1;

				if (errCounter > 10) {
					e.printStackTrace();
					logger.error("element not found:  " + element);

					errCounter = 0;

				}

			}

		}

	}

}
