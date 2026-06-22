package base;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

/* pop-up handler */

public class PopupsHandler {

	public ThreadTwo threadtwofun(WebDriver driver) {
		return new ThreadTwo(driver);

	}

	public ThreadOkWarning threadokwarningfun(WebDriver driver) {
		return new ThreadOkWarning(driver);

	}

	public ThreadContinue threadcontinuefun(WebDriver driver) {
		return new ThreadContinue(driver);

	}

	public ThreadAlert threadtalertfun(WebDriver driver) {
		return new ThreadAlert(driver);

	}

	public ThreadAlertClose threadtalertclosefun(WebDriver driver) {
		return new ThreadAlertClose(driver);

	}

}

class ThreadTwo implements Runnable {

	public final Logger logger = LogManager.getLogger(ThreadTwo.class);

	WebDriver driver;
	QuitBot quitBot = QuitBot.getObj();
	Actions actions;

	public ThreadTwo(WebDriver driver) {
		this.driver = driver;
		actions = new Actions(driver);

	}

	@Override
	public void run() {
		while (true) {

			if (quitBot.isQuit()) {
				logger.info("closing the ThreadTwo");
				break;
			}

			try {
				driver.findElement(By.xpath("//*[contains(text(), 'Abandon Changes')]")).click();

				try {

					WebElement yes = driver.findElement(By.xpath("//*[contains(text(), 'Yes')]"));
					actions.doubleClick(yes).build();
					yes.click();
					System.out.println("clicked yes btn");
				} catch (Exception e) {
					try {
						WebElement yes = driver.findElement(By.xpath("//u[contains(text(), 'Y')]"));
						actions.doubleClick(yes).build();
						System.out.println("clicked yes btn");
						yes.click();
					} catch (Exception e2) {

					}
				}
			} catch (Exception e) {
			}

			try {
				driver.findElement(By.xpath("//*[@id='onetrust-accept-btn-handler']")).click();
				logger.info(" clicked accept btn");
			} catch (Exception e) {
				logger.info(" not found accept btn");
			}

		}
	}
}

class ThreadOkWarning implements Runnable {
	public final Logger logger = LogManager.getLogger(ThreadTwo.class);

	QuitBot quitBot = QuitBot.getObj();

	public ThreadOkWarning(WebDriver driver) {
		this.driver = driver;

	}

	WebDriver driver;

	@Override
	public void run() {
		while (true) {
			// logger.info(Thread.currentThread().getName() + ": Running...");

			if (quitBot.isQuit()) {
				logger.info("closing the ThreadTwo");
				break;
			}
			try {

				driver.findElement(By.xpath("//*[@id=\"id._MSG_WARNING!!11.btn_0\"]")).click();
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
	}
}

//for continue button and ok button
class ThreadContinue implements Runnable {
	public final Logger logger = LogManager.getLogger(ThreadContinue.class);
	WebDriver driver;
	QuitBot quitBot = QuitBot.getObj();
	Actions actions;

	public ThreadContinue(WebDriver driver) {
		super();
		this.driver = driver;
	}

	@Override
	public void run() {
		while (true) {

			if (quitBot.isQuit()) {
				logger.info("closing the ThreadContinue");
				break;
			}
			try {
				driver.findElement(By.xpath("//*[contains(text(), 'Continue')]")).click();
				Thread.sleep(500);
			} catch (Exception e2) {
				try {

				} catch (Exception e) {

				}
			}
			try {
				WebElement ok = driver.findElement(By.id("id.ACTIVEOVERLAP!!11.btnProceed"));
				// actions.doubleClick(yes).build();
				ok.click();
			} catch (Exception e2) {

			}

		}
	}

}

//for bread-crumb at right 
class ThreadAlert implements Runnable {
	public final Logger logger = LogManager.getLogger(ThreadContinue.class);
	WebDriver driver;
	QuitBot quitBot = QuitBot.getObj();

	public ThreadAlert(WebDriver driver) {
		super();
		this.driver = driver;
	}

	@Override
	public void run() {
		while (true) {

			if (quitBot.isQuit()) {
				logger.info("closing the ThreadAlert");
				break;
			}
			try {
				driver.findElement(By.xpath("//*[@id='topmost-message-container']//u")).click();
				Thread.sleep(2000);
			} catch (Exception e) {
				// TODO: handle exception
			}
			try {
				driver.findElement(By.xpath("//*[@class=\"mdi mdi-information\"]")).click();
				Thread.sleep(500);
			} catch (Exception e2) {
				try {

				} catch (Exception e) {

				}
			}
		}
	}

}

// For closing alerts and cards
class ThreadAlertClose implements Runnable {
	public final Logger logger = LogManager.getLogger(ThreadContinue.class);
	WebDriver driver;
	QuitBot quitBot = QuitBot.getObj();

	public ThreadAlertClose(WebDriver driver) {
		super();
		this.driver = driver;
	}

	@Override
	public void run() {
		while (true) {

			if (quitBot.isQuit()) {
				logger.info("closing the ThreadAlert");
				break;
			}
			try {
				driver.findElement(By.xpath("//*[@class=\"hoverbox-buttons\"]/div[2]")).click();
				Thread.sleep(500);
			} catch (Exception e2) {
				try {

				} catch (Exception e) {

				}
			}
		}
	}

}