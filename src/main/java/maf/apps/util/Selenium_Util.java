package maf.apps.util;

import base.DriverManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

public class Selenium_Util {
	private static final Logger logger = LogManager.getLogger(Selenium_Util.class.getName());

	public static Object action(WebDriver driver, Selenium_Actions_Enum actiontype, String xpath, String inputtext,
			int tab, String description) throws InterruptedException {
		String text = "";

		logger.info("descr => " + description);
		if (actiontype.equals(Selenium_Actions_Enum.click)) {
			// DriverManager.sleep(500);

			driver.findElement(By.xpath(xpath)).click();

		} else if (actiontype.equals(Selenium_Actions_Enum.type)) {
			// DriverManager.sleep(500);
			driver.findElement(By.xpath(xpath)).sendKeys(inputtext);
			logger.info("sended text" + inputtext);
			return "typed";

		} else if (actiontype.equals(Selenium_Actions_Enum.clickElement)) {

			WebElement tmp = null;

			int errCounter = 10;

			while (true) {

				try {

					tmp = driver.findElement(By.xpath(xpath));
					Actions actions = new Actions(driver);

//					actions.moveToElement(tmp);
					Thread.sleep(1500);
					tmp.click();

					logger.info("clicked.. " + xpath);
					logger.info("clicked " + xpath);
					break;

				} catch (Exception e) {

					Thread.sleep(1500);

					errCounter += 1;

					if (errCounter > 10) {
						e.printStackTrace();
						logger.info("element not found:  " + xpath);
						logger.info("element not found:  " + description);

						errCounter = 0;

					}

				}

			}

		} else if (actiontype.equals(Selenium_Actions_Enum.clearElement)) {
			WebElement tmp = null;

			int errCounter = 10;

			while (true) {

				try {

					tmp = driver.findElement(By.xpath(xpath));

					tmp.clear();

					logger.info("clicked.. " + xpath);

					break;

				} catch (Exception e) {

					Thread.sleep(1000);

					errCounter += 1;

					if (errCounter > 10) {
						e.printStackTrace();
						logger.info("element not found:  " + xpath);
						logger.info("element not found:  " + description);

						errCounter = 0;

					}

				}

			}

		} else if (actiontype.equals(Selenium_Actions_Enum.clickElementById)) {
			WebElement tmp = null;

			int errCounter = 0;

			while (true) {

				try {

					tmp = driver.findElement(By.id(xpath));

					JavascriptExecutor js = (JavascriptExecutor) driver;
					js.executeScript("arguments[0].scrollIntoView();", tmp);
					tmp.click();

					logger.info("clicked " + xpath);

					break;

				} catch (Exception e) {

					Thread.sleep(1000);

					errCounter += 1;

					if (errCounter > 10) {

						logger.info("element not found:  " + xpath);
						logger.info("element not found:  " + description);

						errCounter = 0;

					}

				}

			}

		}

		else if (actiontype.equals(Selenium_Actions_Enum.clickElementByCss)) {

			WebElement tmp = null;

			int errCounter = 0;

			while (true) {

				try {

					tmp = driver.findElement(By.cssSelector(xpath));

					tmp.click();

					logger.info("clicking " + xpath);

					break;

				} catch (Exception e) {

					Thread.sleep(1000);

					errCounter += 1;

					if (errCounter > 10) {

						logger.info("element not found:  " + xpath);
						logger.info("element not found:  " + description);
						errCounter = 0;

					}

				}

			}

		} else if (actiontype.equals(Selenium_Actions_Enum.sendText)) {
			int errCounter = 5;

			while (true) {

				try {

					WebElement ele = driver.findElement(By.xpath(xpath));

					ele.clear();

					ele.sendKeys(inputtext);

					logger.info("sended text " + inputtext);

					break;

				} catch (Exception e) {

					errCounter += 1;

					if (errCounter >= 5) {

						logger.info("element not found:  " + xpath);
						logger.info("element not found:  " + description);
						e.printStackTrace();
						Thread.sleep(1000);
						errCounter = 0;

					}

				}

			}

		} else if (actiontype.equals(Selenium_Actions_Enum.enter)) {
			int errCounter = 5;

			while (true) {

				try {

					WebElement ele = driver.findElement(By.xpath(xpath));

					ele.sendKeys(org.openqa.selenium.Keys.ENTER);
					logger.info("enterred");

					break;

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}

		else if (actiontype.equals(Selenium_Actions_Enum.enterbyId)) {
			int errCounter = 5;

			while (true) {

				try {

					WebElement ele = driver.findElement(By.id(xpath));

					ele.sendKeys(org.openqa.selenium.Keys.ENTER);
					logger.info("enterred");

					break;

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		} else if (actiontype.equals(Selenium_Actions_Enum.sendTextById)) {
			int errCounter = 5;

			while (true) {

				try {

					WebElement ele = driver.findElement(By.id(xpath));

					ele.clear();
					Thread.sleep(500);
					ele.sendKeys(inputtext);

					logger.info("sended text " + inputtext);

					break;

				} catch (Exception e) {

					errCounter += 1;

					if (errCounter >= 5) {

						logger.info("element not found:  " + xpath);
						logger.info("element not found:  " + description);
						e.printStackTrace();
						Thread.sleep(1000);
						errCounter = 0;

					}

				}

			}

		}

		else if (actiontype.equals(Selenium_Actions_Enum.clickAndsendText)) {
			int errCounter = 1000;

			while (true) {

				try {

					WebElement ele = driver.findElement(By.xpath(xpath));

					ele.click();

					ele.clear();

					ele.sendKeys(inputtext);

					logger.info("sended text " + inputtext);

					break;

				} catch (Exception e) {

					errCounter += 1;

					if (errCounter >= 5) {

						logger.info("element not found:  " + xpath);
						logger.info("element not found:  " + description);

						errCounter = 0;

					}

				}

			}

		} else if (actiontype.equals(Selenium_Actions_Enum.waitUntilLoad)) {
			int errCounter = 10;

			while (true) {

				try {

					driver.findElement(By.xpath(xpath));
					logger.info("element found:  " + xpath);

					break;

				} catch (Exception e) {

					Thread.sleep(1000);

					errCounter += 1;

					if (errCounter >= 10) {

						logger.info("element not found:  " + xpath);
						logger.info("element not found:  " + description);
						errCounter = 0;

					}

					Thread.sleep(1000);

				}

			}

		}

		else if (actiontype.equals(Selenium_Actions_Enum.waitWhilelLoad)) {
			int errCounter = 10;

			while (true) {

				try {

					driver.findElement(By.xpath(xpath));
					logger.info("loading :  " + xpath);
					Thread.sleep(1000);

				} catch (Exception e) {

					break;

				}

			}

		} else if (actiontype.equals(Selenium_Actions_Enum.waitUntilLoadandClickElement)) {
			int errCounter = 10;

			while (true) {

				try {

					driver.findElement(By.xpath(xpath));
					logger.info("element found:  " + xpath);

					break;

				} catch (Exception e) {

					Thread.sleep(1000);

					errCounter += 1;

					if (errCounter >= 10) {

						logger.info("element not found:  " + xpath);
						logger.info("element not found:  " + description);
						errCounter = 0;

					}

					// Thread.sleep(1000);

				}

			}
			WebElement tmp = null;

			int errCount = 10;

			while (true) {

				try {

					tmp = driver.findElement(By.xpath(xpath));

					tmp.click();

					logger.info("clicked.. " + xpath);

					break;

				} catch (Exception e) {

					Thread.sleep(1000);

					errCount += 1;

					if (errCount > 10) {
						e.printStackTrace();
						logger.info("element not found:  " + xpath);
						logger.info("element not found:  " + description);
						errCount = 0;

					}

				}

			}

		} else if (actiontype.equals(Selenium_Actions_Enum.waitUntilLoadandSendText)) {
			int errCounter = 10;

			while (true) {

				try {

					driver.findElement(By.xpath(xpath));
					logger.info("element found:  " + xpath);

					break;

				} catch (Exception e) {

					Thread.sleep(1000);

					errCounter += 1;

					if (errCounter >= 10) {

						logger.info("element not found:  " + xpath);
						logger.info("element not found:  " + description);
						errCounter = 0;

					}

					// Thread.sleep(1000);

				}

			}
			int errCount = 1000;

			while (true) {

				try {

					WebElement ele = driver.findElement(By.xpath(xpath));

					ele.click();

					ele.clear();

					ele.sendKeys(inputtext);

					logger.info("sended text " + inputtext);

					break;

				} catch (Exception e) {

					errCount += 1;

					if (errCount >= 5) {

						logger.info("element not found:  " + xpath);
						logger.info("element not found:  " + description);
						errCount = 0;

					}

				}

			}

		} else if (actiontype.equals(Selenium_Actions_Enum.waitUntilLoadandClearElement)) {
			int errCounter = 10;

			while (true) {

				try {

					driver.findElement(By.xpath(xpath));
					logger.info("element found:  " + xpath);

					break;

				} catch (Exception e) {

					Thread.sleep(1000);

					errCounter += 1;

					if (errCounter >= 10) {

						logger.info("element not found:  " + xpath);
						logger.info("element not found:  " + description);
						errCounter = 0;
						break;

					}

					// Thread.sleep(1000);

				}

			}
			WebElement tmp = null;

			int errCount = 10;

			while (true) {

				try {

					tmp = driver.findElement(By.xpath(xpath));

					tmp.clear();

					logger.info("clicked.. " + xpath);

					break;

				} catch (Exception e) {

					Thread.sleep(1000);

					errCount += 1;

					if (errCount > 10) {
						e.printStackTrace();
						logger.info("element not found:  " + xpath);
						logger.info("element not found:  " + description);
						errCount = 0;

					}

				}

			}

		} else if (actiontype.equals(Selenium_Actions_Enum.getText)) {
			logger.info("Xpath " + xpath);

			try {

				text = driver.findElement(By.xpath(xpath)).getText();
				text = text.replace("\n", "");
				logger.info("text " + text);

			} catch (Exception e) {

				logger.info("element  text not found:  " + e.getMessage());

			}
			return text;
		} else if (actiontype.equals(Selenium_Actions_Enum.getTextByid)) {
			logger.info("id " + xpath);

			try {

				text = driver.findElement(By.id(xpath)).getText();
				text = text.replace("\n", "");
				logger.info("text " + text);

			} catch (Exception e) {

				logger.info("element  text not found:  " + e.getMessage());

			}
			return text;
		} else if (actiontype.equals(Selenium_Actions_Enum.getTextFromInput)) {
			logger.info("Xpath" + xpath);

			// String text = "";

			try {

				text = driver.findElement(By.xpath(xpath)).getAttribute("value");

			} catch (Exception e) {

				logger.info("element not found:  " + e.getMessage());

			}

			logger.info("text" + text);

		} else if (actiontype.equals(Selenium_Actions_Enum.getTextFromInputById)) {
			logger.info("Xpath" + xpath);

			// String text = "";

			try {

				text = driver.findElement(By.id(xpath)).getAttribute("value");

			} catch (Exception e) {

				logger.info("element not found:  " + e.getMessage());

			}

			logger.info("text" + text);

		} else if (actiontype.equals(Selenium_Actions_Enum.doubleClickElement)) {
			Actions actions = new Actions(driver);
			WebElement tmp = null;

			int errCounter = 10;

			while (true) {

				try {

					tmp = driver.findElement(By.xpath(xpath));

					actions.doubleClick(tmp).build().perform();

					logger.info("double clicked.. " + xpath);

					break;

				} catch (Exception e) {

					Thread.sleep(2000);

					errCounter += 1;

					if (errCounter > 10) {
						e.printStackTrace();
						logger.info("element not found:  " + xpath);
						logger.info("element not found:  " + description);
						errCounter = 0;

					}

				}

			}

		} else if (actiontype.equals(Selenium_Actions_Enum.switchTab)) {

			ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
			while (true) {

				try {
					tabs = new ArrayList<String>(driver.getWindowHandles());
					logger.info("tabs " + tabs.size());
					driver.switchTo().window(tabs.get(tab));
					logger.info(" switched");
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (actiontype.equals(Selenium_Actions_Enum.isElementDisplayed)) {
			try {
				driver.findElement(By.xpath(xpath)).isEnabled();
				text = "true";
				logger.info("xpath " + xpath + " is present");
				return true;

			} catch (Exception e) {
				text = "false";
				logger.info("xpath " + xpath + " is not present");

				return false;
			}

		} else if (actiontype.equals(Selenium_Actions_Enum.openNewTab)) {

			((JavascriptExecutor) driver).executeScript("window.open();");
			ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
			Thread.sleep(1000);

		}

		else if (actiontype.equals(Selenium_Actions_Enum.getWebElements)) {

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

						logger.info("element not found:  " + e.getMessage());

						errCounter = 0;
						break;

					}

				}

			}

			return eles;
		}

		else if (actiontype.equals(Selenium_Actions_Enum.getWebElement)) {

			int errCounter = 0;

			WebElement eles = null;

			logger.info("xpath " + xpath);

			while (true) {

				try {

					eles = driver.findElement(By.xpath(xpath));

					break;

				} catch (Exception e) {

					Thread.sleep(1000);

					errCounter += 1;

					if (errCounter > 100) {

						logger.info("element not found:  " + e.getMessage());

						errCounter = 0;
						break;

					}

				}

			}

			return eles;
		} else if (actiontype.equals(Selenium_Actions_Enum.switchToIframe)) {

			int errCounter = 0;
			while (true) {
				try {
					WebElement iframeElement = driver.findElement(By.xpath(xpath));
					driver.switchTo().frame(iframeElement);

					logger.info("switched to iframe " + xpath);
					break;
				} catch (Exception e) {
					Thread.sleep(1000);
					e.printStackTrace();
					/*
					 * // * Thread.sleep(1000);
					 * 
					 * errCounter += 1;
					 * 
					 * if (errCounter > 100) {
					 * 
					 * logger.info("element not found:  " + e.getMessage());
					 * 
					 * errCounter = 0; break;
					 * 
					 * }
					 * 
					 * 
					 */
				}
			}

		} else if (actiontype.equals(Selenium_Actions_Enum.selectCheckboxByValue)) {

			int errCounter = 10;
			while (true) {

				try {
					Select s = new Select(driver.findElement(By.xpath(xpath)));
					s.selectByValue(inputtext);
					logger.info("selected =>  " + inputtext);
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

		else if (actiontype.equals(Selenium_Actions_Enum.selectCheckboxByText)) {

			int errCounter = 10;
			while (true) {

				try {
					Select s = new Select(driver.findElement(By.xpath(xpath)));
					s.selectByVisibleText(inputtext);
					logger.info(" selected " + inputtext);
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

		} else if (actiontype.equals(Selenium_Actions_Enum.selectByIndex)) {

			int errCounter = 10;
			while (true) {

				try {
					Select s = new Select(driver.findElement(By.xpath(xpath)));
					s.selectByIndex(Integer.valueOf(inputtext));
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

		} else if (actiontype.equals(Selenium_Actions_Enum.selectByVisibleText)) {

			int errCounter = 10;
			while (true) {

				try {
					Select s = new Select(driver.findElement(By.xpath(xpath)));
					s.selectByVisibleText(inputtext);
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

		} else if (actiontype.equals(Selenium_Actions_Enum.getTable)) {

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

				logger.info("element not found:  " + e.getMessage());

			}

			logger.info("table" + table);

			return table;
		}
		return text;
	}

	/*
	 * public static boolean isElementPresent(By by, WebDriver driver) { try {
	 * driver.findElement(by); return true; } catch (Exception e) { return false; }
	 * }
	 */

	public static void mkactions(WebDriver driver, String actiontype, String keyname, String xpath, String Description)
			throws InterruptedException {
		if (actiontype == "press" & keyname == "ENTER") {
			DriverManager.sleep(3000);
			driver.findElement(By.xpath(xpath)).sendKeys(Keys.ENTER);
		} else if (actiontype == "mouseaction") {
			DriverManager.sleep(3000);
		}

	}

	/*
	 * public String gettext(WebDriver driver, String xpath) throws
	 * InterruptedException { DriverManager.sleep(3000); return
	 * driver.findElement(By.xpath(xpath)).getText(); }
	 */

}
