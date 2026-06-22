package maf.apps.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import maf.apps.HumanaAuth.FlowInitial;

public class FileUtil {
	static Date d = new Date();
	static SimpleDateFormat f = new SimpleDateFormat("MM-dd-yyyy");
	static String date = f.format(d);
	public static final Logger logger = LogManager.getLogger(FileUtil.class);

	public static List<Map<String, String>> getexceltomap(String path) throws IOException {
		List<Map<String, String>> data = new ArrayList<>();

		try (FileInputStream fileInputStream = new FileInputStream(new File(path))) {
			Workbook workbook = WorkbookFactory.create(fileInputStream);
			Sheet sheet = workbook.getSheetAt(0); // Get the first sheet

			Iterator<Row> rowIterator = sheet.iterator();
			Row headerRow = rowIterator.next(); // Assuming first row is the header row

			List<String> headers = new ArrayList<>();
			for (Cell cell : headerRow) {
				headers.add(cell.getStringCellValue());
			}

			for (Row row : sheet) {
				Map<String, String> rowData = new HashMap<>();

				for (int i = 0; i < headers.size(); i++) {

					Cell cell = row.getCell(i);
					String cellValue = "";
					if (cell != null) {
						switch (cell.getCellType()) {
						case STRING:
							cellValue = cell.getStringCellValue();
							break;
						case NUMERIC:
							cellValue = String.valueOf((int) cell.getNumericCellValue());
							break;
						case BOOLEAN:
							cellValue = String.valueOf(cell.getBooleanCellValue());
							break;
						default:
							cellValue = "";
						}
					}
					rowData.put(headers.get(i), cellValue);
				}
				data.add(rowData);
			}

			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return data;
	}

	// creating json file
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

//			logger.info(e);

		}

	}

	public static void create() throws IOException {
		String filePath = RootPath.BASE_DIR + "\\resources\\output\\" + date + "-output.xlsx";
		System.out.println(" in the excel create");

		System.out.println(date);
		Workbook workbook = new XSSFWorkbook();

		// Create a sheet
		Sheet sheet = workbook.createSheet("sheet1");

		// Create a row
		Row headerRow = sheet.createRow(0);

		// Define headers
		String[] headers = { "facility", "officeKey", "BathNum", "Payment", "Status" };
		for (int i = 0; i < headers.length; i++) {
			Cell headerCell = headerRow.createCell(i);
			headerCell.setCellValue(headers[i]);
		}

		// Write the output to a file
		File file = new File(filePath);

		if (file.exists()) {
			System.out.println("File exists");
		} else {
			System.out.println("File does not exist");

			try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
				workbook.write(fileOut);
				System.out.println("Excel File has been created successfully.");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			workbook.close();
		}
	}

	public static void addData(String[] data, String name) {
		String filePath = RootPath.BASE_DIR + "\\resources\\output\\" + name + ".xlsx";

		try (FileInputStream fis = new FileInputStream(filePath);

				Workbook workbook = new XSSFWorkbook(fis)) {

			// Get the first sheet of the workbook
			Sheet sheet = workbook.getSheetAt(0);

			// Get the index of the last row with data
			int lastRowNum = sheet.getLastRowNum();

			// Create a new row after the last row
			Row newRow = sheet.createRow(lastRowNum + 1);

			// Add data to the new row
			for (int i = 0; i < data.length; i++) {
				Cell cell = newRow.createCell(i);
				cell.setCellValue(data[i]);
			}

			// Write the changes to the Excel file
			try (FileOutputStream fos = new FileOutputStream(filePath)) {
				workbook.write(fos);
				System.out.println("Data appended successfully to the last row.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void addRow(List<Object> data, String name) {
		String filePath = RootPath.BASE_DIR + "\\resources\\output\\" + name + ".xlsx";
		System.out.println(filePath);

		try (FileInputStream fis = new FileInputStream(filePath);

				Workbook workbook = new XSSFWorkbook(fis)) {

			// Get the first sheet of the workbook
			Sheet sheet = workbook.getSheetAt(0);

			// Get the index of the last row with data
			int lastRowNum = sheet.getLastRowNum();

			// Create a new row after the last row
			Row newRow = sheet.createRow(lastRowNum + 1);

			// Add data to the new row
			for (int i = 0; i < data.size(); i++) {
				Cell cell = newRow.createCell(i);
				System.out.println("** " + data.get(i));
				if (data.get(i) == null) {
					cell.setCellValue("");
				} else {
					cell.setCellValue(data.get(i).toString());
				}
			}

			// Write the changes to the Excel file
			try (FileOutputStream fos = new FileOutputStream(filePath)) {
				workbook.write(fos);
				System.out.println("Data appended successfully to the last row.");
			}
		} catch (IOException e) {
			logger.info(e.getMessage());
		}
	}

	public static void create(String name) throws IOException {
		String filePath = RootPath.BASE_DIR + "\\resources\\output\\hpsj\\" + date + "-" + name + ".xlsx";
		System.out.println(" in the excel create");

		System.out.println(date);
		Workbook workbook = new XSSFWorkbook();

		// Create a sheet
		Sheet sheet = workbook.createSheet("sheet1");

		// Create a row
		Row headerRow = sheet.createRow(0);

		// Define headers
		String[] headers = { "facility", "officeKey", "BathNum", "Payment", "Status" };
		for (int i = 0; i < headers.length; i++) {
			Cell headerCell = headerRow.createCell(i);
			headerCell.setCellValue(headers[i]);
		}

		// Write the output to a file
		File file = new File(filePath);

		if (file.exists()) {
			System.out.println("File exists");
		} else {
			System.out.println("File does not exist");

			try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
				workbook.write(fileOut);
				System.out.println("Excel File has been created successfully.");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			workbook.close();
		}
	}

}
