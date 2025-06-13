package Fo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;

import org.testng.Assert;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


	/*public static void main(String[] args) throws IOException {

	    File file = new File("C:\\Users\\Jeeva\\eclipse-workspace\\Wincloud_FO\\excel\\Credentials.xlsx");
	    FileInputStream fis = new FileInputStream(file);
	    Workbook workbook = new XSSFWorkbook(fis);
	    Sheet sheet = workbook.getSheetAt(0);
	    Row row = sheet.getRow(0);
	    Cell cell = row.getCell(0);

	    System.out.println(cell.toString());

	    workbook.close();
	    fis.close();
	}*/

	public class Excel {
	    WebDriver driver; // Moved here

	    public static void main(String[] args) throws IOException {
	        Excel obj = new Excel();
	        obj.runTest(); // Avoid static main logic for test code
	    }

	    public void runTest() throws IOException {
	        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Jeeva\\eclipse-workspace\\Wincloud_FO\\Chromedriver\\chromedriver.exe");
	        driver = new ChromeDriver(); // set the instance variable

	        driver.manage().window().maximize();

	        File file = new File("C:\\Users\\Jeeva\\eclipse-workspace\\Wincloud_FO\\excel\\Credentials.xlsx");
	        FileInputStream fis = new FileInputStream(file);
	        Workbook workbook = new XSSFWorkbook(fis);
	        Sheet sheet = workbook.getSheetAt(0);

	        driver.get(sheet.getRow(1).getCell(1).toString().trim());

	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	        WebElement name = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ProptyText")));
	        name.sendKeys(sheet.getRow(2).getCell(1).toString().trim() + Keys.ENTER);

	        WebElement username = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("UserName")));
	        username.sendKeys(sheet.getRow(3).getCell(1).toString().trim() + Keys.ENTER);

	        WebElement password = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Password")));
	        password.sendKeys(sheet.getRow(4).getCell(1).toString().trim() + Keys.ENTER);

	        // Example usage:
	        takeScreenshot("LoginTest");

	        workbook.close();
	        fis.close();
	    }

	    private void takeScreenshot(String methodName) throws IOException {
	        TakesScreenshot ts = (TakesScreenshot) driver;
	        File srcFile = ts.getScreenshotAs(OutputType.FILE);
	        String filePath = "C:\\Users\\Jeeva\\Pictures\\ScreenShot\\" + methodName + ".png";
	        FileHandler.copy(srcFile, new File(filePath));
	        System.out.println("Screenshot saved at: " + filePath);
	    }
	
}