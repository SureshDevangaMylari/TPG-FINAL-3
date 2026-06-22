package maf.apps.util;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

public class RootPath {

	public static String BASE_DIR = Paths.get(".", new String[0]).toAbsolutePath().normalize().toString();

	public static String inputFilePath;

	public static boolean osFlag = (System.getProperty("os.name").contains("linux")
			|| System.getProperty("os.name").contains("Linux"));

	public static FileReader reader;

}
