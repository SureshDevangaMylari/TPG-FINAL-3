package maf.apps.util;

import org.sikuli.hotkey.Keys;

public interface SikuliOps {

	public abstract void click(String path, int delay);

	public abstract void clickNoDelay(String path);

	public abstract void select(String path);

	public abstract void entertext(String path);

	public abstract void capture(int x, int y, int w, int h);

	public abstract void backspace();

	public abstract void preessAnyKey(Keys key, int delay);

	public abstract void pressEnter();

	public abstract void pressTab();

	public abstract void pressPrintscreen();

	public abstract void pressCAPS();

	public abstract void pressShift();

	public abstract void pressControl();

	public abstract void pressShiftControl();
	
	public abstract void pressKeyWithCount(String path, int count);
	
	public abstract void printpage();
	
	public abstract void waituntilLoad();
	
	public abstract boolean checkElement(String path);

	public abstract void waituntilLoad(String path,int count);

}
