package maf.apps.HumanaAuth;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Screen;

import maf.apps.hoagCC.Flow;
import maf.apps.util.ResultEnum;
import maf.apps.util.RootPath;
import maf.apps.util.SikuliUtility;

public class Test2 {
	public static final Logger logger = LogManager.getLogger(Flow.class);

	public static void main(String[] args) throws ParseException, FindFailed, InterruptedException {
		// TODO Auto-generated method stub

	    String username = System.getProperty("user.name");
        System.out.println("User: " + username);
	}
}