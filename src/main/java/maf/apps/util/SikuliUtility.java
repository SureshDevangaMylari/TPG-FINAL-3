package maf.apps.util;

import java.awt.event.KeyEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sikuli.api.robot.Keyboard;
import org.sikuli.api.robot.desktop.DesktopKeyboard;
import org.sikuli.hotkey.Keys;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Screen;

import maf.apps.hoagCC.RTservices;

public class SikuliUtility implements SikuliOps {
	public static final Logger logger = LogManager.getLogger(SikuliUtility.class);

	static Keyboard keyboard = new DesktopKeyboard();

	static Screen screen = new Screen();

	@Override
	public void click(String path, int delay) {
		while (true) {
			try {
				screen.click(path);
				break;
			} catch (FindFailed e) {
				logger.info("not found " + path);
//			e.printStackTrace();
			}

		}
	}

	@Override
	public void backspace() {
		int keyCounter = 0;
		while (true) {
			keyCounter++;
			keyboard.keyDown(Keys.BACKSPACE);
			keyboard.keyUp(Keys.BACKSPACE);
			if (keyCounter == 50)
				break;
		}

	}

	@Override
	public void select(String path) {

	}

	@Override
	public void entertext(String text) {
		screen.type(text);
	}

	@Override
	public void capture(int x, int y, int w, int h) {

	}

	@Override
	public void preessAnyKey(Keys key, int delay) {

	}

	@Override
	public void pressEnter() {
		keyboard.keyDown(KeyEvent.VK_ENTER);
		keyboard.keyUp(KeyEvent.VK_ENTER);

	}

	@Override
	public void pressTab() {
		keyboard.keyDown(KeyEvent.VK_TAB);
		keyboard.keyUp(KeyEvent.VK_TAB);
	}

	@Override
	public void pressPrintscreen() {

	}

	@Override
	public void pressCAPS() {

	}

	@Override
	public void pressShift() {

	}

	@Override
	public void pressControl() {

	}

	@Override
	public void pressShiftControl() {

	}

	@Override
	public void clickNoDelay(String path) {

	}

	@Override
	public void pressKeyWithCount(String path, int count) {

	}

	@Override
	public void printpage() {
		keyboard.keyDown(KeyEvent.VK_CONTROL);
		keyboard.keyDown(KeyEvent.VK_P);
		keyboard.keyUp(KeyEvent.VK_P);
		keyboard.keyUp(KeyEvent.VK_CONTROL);

	}

	@Override
	public void waituntilLoad(String path, int count) {
		try {
			screen.wait(path, count);
		} catch (FindFailed e) {

			e.printStackTrace();
		}

	}

	@Override
	public boolean checkElement(String path) {
		if (screen.exists(path) != null) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	public void waituntilLoad() {
		// TODO Auto-generated method stub

	}

}
