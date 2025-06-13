package Fo;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class ExcelTestListener implements ITestListener {

    private List<TestResultData> testResults = new ArrayList<>();

    @Override
    public void onTestSuccess(ITestResult result) {
        long duration = result.getEndMillis() - result.getStartMillis();
        testResults.add(new TestResultData(result.getName(), "PASS", "", duration));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        long duration = result.getEndMillis() - result.getStartMillis();
        String error = result.getThrowable() != null ? result.getThrowable().getMessage() : "Exception occurred";
        testResults.add(new TestResultData(result.getName(), "FAIL", error, duration));
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        long duration = result.getEndMillis() - result.getStartMillis();
        testResults.add(new TestResultData(result.getName(), "SKIPPED", "", duration));
    }

    @Override
    public void onFinish(ITestContext context) {
        writeResultsToExcel();
    }

    private void writeResultsToExcel() {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Test Results");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Test Name");
        header.createCell(1).setCellValue("Status");
        header.createCell(2).setCellValue("Error Message");
        header.createCell(3).setCellValue("Duration (ms)");

        int rowNum = 1;
        for (TestResultData data : testResults) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(data.testName);
            row.createCell(1).setCellValue(data.status);
            row.createCell(2).setCellValue(data.errorMessage);
            row.createCell(3).setCellValue(data.duration);
        }

        try (FileOutputStream out = new FileOutputStream("TestReport.xlsx")) {
            workbook.write(out);
            workbook.close();
            System.out.println("Test report generated: TestReport.xlsx");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class TestResultData {
        String testName;
        String status;
        String errorMessage;
        long duration;

        TestResultData(String testName, String status, String errorMessage, long duration) {
            this.testName = testName;
            this.status = status;
            this.errorMessage = errorMessage;
            this.duration = duration;
        }
    }
}
