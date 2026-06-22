package maf.apps.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import base.DriverManager;


public class Excel_create {
	public static void create(String filepath,String[] headers) throws IOException {
		LocalDate date=LocalDate.now();
		//String filePath = DriverManager.BASE_DIR + "\\resources\\output\\Allcare output\\" + date + "-output.xlsx";
		System.out.println(" in the excel create");

		System.out.println(date);
		Workbook workbook = new XSSFWorkbook();

		// Create a sheet
		Sheet sheet = workbook.createSheet("sheet1");

		// Create a row
		Row headerRow = sheet.createRow(0);

		// Define headers
		//String[] headers = { "facility", "officeKey", "BathNum", "Payment", "Status" };
		for (int i = 0; i < headers.length; i++) {
			Cell headerCell = headerRow.createCell(i);
			headerCell.setCellValue(headers[i]);
		}

		// Write the output to a file
		File file = new File(filepath);

		if (file.exists()) {
			System.out.println("File exists");
		} else {
			System.out.println("File does not exist");

			try (FileOutputStream fileOut = new FileOutputStream(filepath)) {
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
