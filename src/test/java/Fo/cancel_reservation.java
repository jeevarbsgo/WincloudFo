package Fo;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

public class cancel_reservation {
    public static void main(String[] args) throws InterruptedException {

        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Jeeva\\eclipse-workspace\\Wincloud_FO\\Chromedriver\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        // Maximize the browser window
        driver.manage().window().maximize();

        // Navigate to the login page
        driver.get("https://test1dns.wincloudpms.net/WinLogin/Login/");

        // Create a WebDriverWait object (10 seconds timeout)
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Actions action = new Actions(driver);

        // Interact with the login form
        WebElement name = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ProptyText")));
        name.sendKeys("dubaidemo" + Keys.ENTER);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("UserName")));
        driver.findElement(By.id("UserName")).sendKeys("wincloud" + Keys.ENTER);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Password")));
        driver.findElement(By.id("Password")).sendKeys("rbsgo" + Keys.ENTER);

        // Wait for navigation and login to complete
        Thread.sleep(2000);  // You can replace this with a better wait if you know the element to wait for after login

        // Navigate to the reservation page
        driver.navigate().to("https://test1dns.wincloudpms.net/TravelAgentBlock/FOReservation?VN=3.04.025");

        // Number of times you want to repeat the cancellation process
        int loopCount = 50;

        for (int i = 0; i < loopCount; i++) {
            // Click on folder icon
        	driver.navigate().refresh();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class=' fa fa-folder-open']"))).click();

            // Wait for the element at aria-rowindex 4 to be visible and double-click it
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@aria-rowindex=\"4\"])[1]")));
            action.doubleClick(element).perform();
            Thread.sleep(1000);
            // Click the close (fa-times) icon
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class=' fa fa-times']"))).click();

            // Enter "cancel" into the textarea
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//textarea[@class='webix_inp_textarea']"))).sendKeys("cancel");

            // Enter "admin" into the input field
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@style='width:347px;text-align:left;']"))).sendKeys("admin");

            // Click the check icon (submit)
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='webix_icon_btn wxi-check']"))).click();

            // Click the first button with class "webix_button webix_img_btn"
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//button[@class='webix_button webix_img_btn'])[1]"))).click();

            // You can add a small wait here if necessary, to allow UI to update before next loop iteration
        }

        // Optionally, quit the driver after loop finishes
        driver.quit();
    }
}
