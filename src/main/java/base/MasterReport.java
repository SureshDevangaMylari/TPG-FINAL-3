//package base;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import maf.apps.util.RootPath;
//
//public class MasterReport {
//
//    private static final String MASTER_REPORT = RootPath.BASE_DIR +"\\resources\\MasterReportFolder\\Master_Report.xlsx";
//
//    public static String getMasterReportPath() {
//        return MASTER_REPORT;
//    }
//	public static void appendReportRow(ReportRow report) throws Exception {
//
//        Workbook workbook;
//        Sheet sheet;
//
//        File file = new File(MASTER_REPORT);
//
//        if (file.exists()) {
//
//            FileInputStream fis = new FileInputStream(file);
//            workbook = new XSSFWorkbook(fis);
//            sheet = workbook.getSheetAt(0);
//            fis.close();
//
//        } else {
//
//            workbook = new XSSFWorkbook();
//            sheet = workbook.createSheet("Master Report");
//
//            Row header = sheet.createRow(0);
//
//            header.createCell(0).setCellValue("Date");
//            header.createCell(1).setCellValue("PN#");
//            header.createCell(2).setCellValue("DOB");
//            header.createCell(3).setCellValue("Patient Name");
//            header.createCell(4).setCellValue("Location");
//            header.createCell(5).setCellValue("No of attempts");
//            header.createCell(6).setCellValue("Noridian Letter");
//            header.createCell(7).setCellValue("In Lieu Form");
//            header.createCell(8).setCellValue("Claim Status");
//            header.createCell(9).setCellValue("Software Notes");
//        }
//
//        int rowNum = sheet.getLastRowNum() + 1;
//
//        Row row = sheet.createRow(rowNum);
//
//        row.createCell(0).setCellValue(report.getDate());
//        row.createCell(1).setCellValue(report.getPnNumber());
//        row.createCell(2).setCellValue(report.getDob());
//        row.createCell(3).setCellValue(report.getPatientName());
//        row.createCell(4).setCellValue(report.getLocation());
//        row.createCell(5).setCellValue(report.getNoOfAttempts());
//        row.createCell(6).setCellValue(report.getNoridianLetter());
//        row.createCell(7).setCellValue(report.getInLieuForm());
//        row.createCell(8).setCellValue(report.getClaimStatus());
//        row.createCell(9).setCellValue(report.getSoftwareNotes());
//
//        for (int i = 0; i < 10; i++) {
//            sheet.autoSizeColumn(i);
//        }
//
//        FileOutputStream fos = new FileOutputStream(file);
//
//        workbook.write(fos);
//
//        fos.close();
//        workbook.close();
//
//        System.out.println("Master report updated.");
//    }
//}
