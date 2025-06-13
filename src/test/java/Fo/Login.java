package Fo;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class Login {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeClass
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Jeeva\\eclipse-workspace\\Wincloud_FO\\Chromedriver\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @BeforeMethod
    public void loadLoginPage() {
        driver.get("https://test1dns.wincloudpms.net/WinLogin/Login/");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ProptyText")));
    }

    @Test
    public void testloginpage() {
        WebElement name = driver.findElement(By.id("ProptyText"));
        name.sendKeys("dubaidemo" + Keys.ENTER);

        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ProptyDisplay")));
        Assert.assertTrue(element.isDisplayed(), "The element with id 'ProptyDisplay' is not displayed.");
    }

    @Test
    public void testnegativelogin() {
        WebElement name1 = driver.findElement(By.id("ProptyText"));
        name1.sendKeys("dubaidem" + Keys.ENTER);

        WebElement msg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("AlertMsg1")));
        Assert.assertTrue(msg.isDisplayed(), "Alert message is not displayed.");
        Assert.assertEquals(msg.getText(), "Property Does not Exist", "Alert message text does not match.");
    }

    @Test
    public void testnegativeuserid() {
        driver.findElement(By.id("ProptyText")).sendKeys("dubaidemo" + Keys.ENTER);
        driver.findElement(By.id("UserName")).sendKeys("123" + Keys.ENTER);

        WebElement msg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("AlertMsg1")));
        Assert.assertTrue(msg.isDisplayed(), "Alert message is not displayed.");
    }

    @Test
    public void testpositiveuserid() throws InterruptedException {
        driver.findElement(By.id("ProptyText")).sendKeys("dubaidemo" + Keys.ENTER);
        driver.findElement(By.id("UserName")).sendKeys("wincloud" + Keys.ENTER);
        
      boolean isAlertDisplayed = driver.findElements(By.id("AlertMsg1")).stream()
                .anyMatch(WebElement::isDisplayed);

        Assert.assertFalse(isAlertDisplayed, "Alert message is displayed but should NOT be.");
    }

    
    @Test
    public void testnegativepassword() throws InterruptedException {
        driver.findElement(By.id("ProptyText")).sendKeys("dubaidemo" + Keys.ENTER);
        driver.findElement(By.id("UserName")).sendKeys("admin" + Keys.ENTER);
        driver.findElement(By.id("Password")).sendKeys("admi" + Keys.ENTER); Thread.sleep(2000);

        WebElement msg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("AlertMsg1")));
        Assert.assertTrue(msg.isDisplayed(), "Alert message is not displayed.");
    }

    @Test
    public void testpositivepassword() {
        driver.findElement(By.id("ProptyText")).sendKeys("dubaidemo" + Keys.ENTER);
        driver.findElement(By.id("UserName")).sendKeys("wincloud" + Keys.ENTER);
        driver.findElement(By.id("Password")).sendKeys("rbsgo");

        boolean isAlertDisplayed = driver.findElements(By.id("AlertMsg1")).stream()
                .anyMatch(WebElement::isDisplayed);

        Assert.assertFalse(isAlertDisplayed, "Alert message is displayed but should NOT be.");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
    private void takeScreenshot(String methodName) throws IOException {
	    TakesScreenshot ts = (TakesScreenshot) driver;
	    File srcFile = ts.getScreenshotAs(OutputType.FILE);

	    String filePath = "C:\\Users\\Jeeva\\Pictures\\ScreenShot\\" + methodName + ".png";

	    FileHandler.copy(srcFile, new File(filePath));
	    System.out.println("Screenshot saved at: " + filePath);
}
}