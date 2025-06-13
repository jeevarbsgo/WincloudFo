package Fo;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class Reservation {
	WebDriver driver;
	WebDriverWait wait;
	//JavascriptExecutor js;

	@BeforeClass
	public void setup() {
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\Jeeva\\eclipse-workspace\\Wincloud_FO\\Chromedriver\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	}

	@Test
	public void Test_Sucessfull_Login() {
		driver.get("https://test1dns.wincloudpms.net/WinLogin/Login/");

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ProptyText")));
		driver.findElement(By.id("ProptyText")).sendKeys("dubaidemo" + Keys.ENTER);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("UserName")));
		driver.findElement(By.id("UserName")).sendKeys("wincloud" + Keys.ENTER);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Password")))
		.sendKeys("rbsgo" + Keys.ENTER);

		WebElement logo = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//img[@src=\"../images/wincloud-gray.png\"]")));

		Assert.assertTrue(logo.isDisplayed(), "Login was not successful - Wincloud logo not displayed.");
	}

	@Test(dependsOnMethods = "Test_Sucessfull_Login", priority = 1)
	public void Test_Registration_page() {
		// Navigate to the reservation page
		driver.get("https://test1dns.wincloudpms.net/TravelAgentBlock/FOReservation?VN=3.04.025");

		// Wait for the element to be visible
		WebElement add = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@class='webix_el_box'])[2]")));

		// Click the element
		add.click();

		// Assert that the element is displayed (or you can check another element after clicking)
		Assert.assertTrue(add.isDisplayed(), "Failed in opening new registration");
	}

	@Test(dependsOnMethods = "Test_Sucessfull_Login", priority = 2)
	public void Test_Future_Arrival_Date() {
		WebElement arrivalDate = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@aria-label='Arrival']")));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", arrivalDate);

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='webix_cal_month_name']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='webix_cal_month_name']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='2023']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Jan']"))).click();

		WebElement day = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//span[text()='10'])[1]")));
		day.click();

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@aria-label='RoomType']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@webix_l_id='DXR']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='fa fa-search ExpBkGridIconBtn']"))).click();

		WebElement doubleClick = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//div[@aria-rowindex='5'])[1]")));
		new Actions(driver).doubleClick(doubleClick).perform();

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='OK']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='OK']"))).click();

		// Click the save button (only once)
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='fa fa-save']"))).click();

		// Wait for the alert to appear
		By alertXpath = By.xpath("//div[text()='Arrival Date cannot be less than Account Date']");
		WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(alertXpath));

		// Assert that the alert message is correct
		Assert.assertTrue(alert.isDisplayed(), "Alert message is not displayed.");
		Assert.assertEquals(alert.getText().trim(), "Arrival Date cannot be less than Account Date", "Incorrect alert text.");

		// Wait for it to disappear
		WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
		boolean disappeared = shortWait.until(ExpectedConditions.invisibilityOfElementLocated(alertXpath));
		Assert.assertTrue(disappeared, "Alert did not disappear after 2 seconds.");
	}
	@Test(dependsOnMethods = "Test_Sucessfull_Login", priority=3)
	public void Test_Registration_with_TodayDate() {
		driver.get("https://test1dns.wincloudpms.net/TravelAgentBlock/FOReservation?VN=3.04.025");

		WebElement add = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@class='webix_el_box'])[2]")));
		add.click();

		WebElement nightsInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//label[text()='Nights']/following-sibling::input")
				));
		nightsInput.clear();
		nightsInput.sendKeys("0");
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@aria-label='RoomType']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@webix_l_id='DXR']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='fa fa-search ExpBkGridIconBtn']"))).click();

		WebElement doubleClick = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//div[@aria-rowindex='5'])[1]")));
		new Actions(driver).doubleClick(doubleClick).perform();

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='OK']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='OK']"))).click();		    

		// After clicking the Save button
		By saveButton = By.xpath("//span[@class='fa fa-save']");
		wait.until(ExpectedConditions.elementToBeClickable(saveButton)).click();

		// Switch to browser alert
		WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
		Alert alert = shortWait.until(ExpectedConditions.alertIsPresent());

		// Get the text
		String alertText = alert.getText();
		System.out.println("Browser alert message: " + alertText);

		// Verify the alert text
		Assert.assertEquals(alertText.trim(), "Arrival Date should be less than or equal to Departure Date", "Unexpected browser alert message.");

		// Accept the alert
		alert.accept();

	}

	@Test(dependsOnMethods = "Test_Sucessfull_Login", priority = 4)
	public void Verify_Positive_nights() throws InterruptedException, AWTException {
	    driver.get("https://test1dns.wincloudpms.net/TravelAgentBlock/FOReservation?VN=3.04.025");

	    WebElement add = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@class='webix_el_box'])[2]")));
	    add.click();

	    WebElement arrivalDate = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@aria-label='Arrival']")));
	    JavascriptExecutor js = (JavascriptExecutor) driver;
	    js.executeScript("arguments[0].click();", arrivalDate);

	    wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='webix_cal_month_name']"))).click();
	    wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='webix_cal_month_name']"))).click();
	    wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='2023']"))).click();
	    wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Sep']"))).click();

	    WebElement day = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//span[text()='29'])[2]")));
	    day.click();
	    
	    WebElement DepartureDate = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@aria-label='Departure']")));
	    JavascriptExecutor js1 = (JavascriptExecutor) driver;
	    js.executeScript("arguments[0].click();", DepartureDate);
	    
	    wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//div[@class=\"webix_cal_month\"])[2]"))).click();
	    wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//div[@class=\"webix_cal_month\"])[2]"))).click();
	    wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='2023']"))).click();
	    wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Sep']"))).click();
	    
	    WebElement day1 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//span[text()='30'])[4]")));
	    day1.click();
	    
	    Thread.sleep(2000);

	  WebElement nightsInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("//label[text()='Nights']/following-sibling::input")));
	  String Nights = nightsInput.getAttribute("value");
	   System.out.println(Nights);
	   Assert.assertEquals(Nights, "1");
	}
	@Test(dependsOnMethods = "Test_Sucessfull_Login", priority = 5)
	public void Test_Mandatory_Fields_arrivaldate() throws InterruptedException, AWTException {
		driver.get("https://test1dns.wincloudpms.net/TravelAgentBlock/FOReservation?VN=3.04.025");

		WebElement add = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@class='webix_el_box'])[2]")));
		add.click();

		WebElement arrivalDate = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@aria-label='Arrival']")));

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].innerText = '';", arrivalDate);

		WebElement DepartureDate = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@aria-label='Departure']")));
		JavascriptExecutor js1 = (JavascriptExecutor) driver;
		js1.executeScript("arguments[0].innerText = '';", DepartureDate);

		driver.findElement(By.xpath("(//input[@placeholder=\"HH:MM\"])[1]")).clear();
		driver.findElement(By.xpath("(//input[@placeholder=\"HH:MM\"])[2]")).clear();
		WebElement nightsInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[text()='Nights']/following-sibling::input")));
		nightsInput.clear();

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@aria-label='RoomType']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@webix_l_id='DXR']"))).click();


		// Click the save button (only once)
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='fa fa-save']"))).click();
		By alertXpath = By.xpath("//div[text()=\"Arrival Time cannot be empty.\"]");
		WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(alertXpath));
		Assert.assertTrue(alert.isDisplayed(), "Alert message is not displayed.");
	}

	@Test(dependsOnMethods = "Test_Sucessfull_Login", priority = 6)
	public void Test_Mandatory_Fields_arraivaltime() throws InterruptedException, AWTException {

		driver.get("https://test1dns.wincloudpms.net/TravelAgentBlock/FOReservation?VN=3.04.025");

		WebElement add = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@class='webix_el_box'])[2]")));
		add.click();

		WebElement arrivalDate = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@aria-label='Arrival']")));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", arrivalDate);

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='webix_cal_month_name']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='webix_cal_month_name']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='2023']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Sep']"))).click();

		WebElement day = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//span[text()='29'])[2]")));
		day.click();
//		WebElement DepartureDate = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@aria-label='Departure']")));
//		JavascriptExecutor js1 = (JavascriptExecutor) driver;
//		js1.executeScript("arguments[0].innerText = '';", DepartureDate);

		driver.findElement(By.xpath("(//input[@placeholder=\"HH:MM\"])[1]")).clear();
		

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='fa fa-save']"))).click();
		By alertXpath = By.xpath("//div[text()=\"Arrival Time cannot be empty.\"]");
		WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(alertXpath));
		Assert.assertTrue(alert.isDisplayed(), "Alert message is not displayed.");
	}

	@Test(dependsOnMethods = "Test_Sucessfull_Login", priority = 7)
	public void Test_Mandatory_Fields_departuredate() throws InterruptedException, AWTException {


		driver.get("https://test1dns.wincloudpms.net/TravelAgentBlock/FOReservation?VN=3.04.025");

		WebElement add = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@class='webix_el_box'])[2]")));
		add.click();

		WebElement arrivalDate = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@aria-label='Arrival']")));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", arrivalDate);

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='webix_cal_month_name']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='webix_cal_month_name']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='2023']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Sep']"))).click();

		WebElement day = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//span[text()='29'])[2]")));
		day.click();
		WebElement DepartureDate = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@aria-label='Departure']")));
		JavascriptExecutor js1 = (JavascriptExecutor) driver;
		js1.executeScript("arguments[0].innerText = '';", DepartureDate);


		driver.findElement(By.xpath("(//input[@placeholder=\"HH:MM\"])[2]")).clear();
		

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@aria-label='RoomType']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@webix_l_id='DXR']"))).click();


		// Click the save button (only once)
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='fa fa-save']"))).click();
		By alertXpath = By.xpath("//div[text()=\"Departure Time cannot be empty.\"]");
		WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(alertXpath));
		Assert.assertTrue(alert.isDisplayed(), "Alert message is not displayed.");
	}
	@Test(dependsOnMethods = "Test_Sucessfull_Login", priority = 8)
	public void Test_Mandatory_Fields_departuretime() throws InterruptedException, AWTException {

		driver.get("https://test1dns.wincloudpms.net/TravelAgentBlock/FOReservation?VN=3.04.025");

		WebElement add = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@class='webix_el_box'])[2]")));
		add.click();

		WebElement arrivalDate = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@aria-label='Arrival']")));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", arrivalDate);

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='webix_cal_month_name']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='webix_cal_month_name']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='2023']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Sep']"))).click();

		WebElement day = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//span[text()='29'])[2]")));
		day.click();

		WebElement DepartureDate = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@aria-label='Departure']")));
		JavascriptExecutor js1 = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", DepartureDate);

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//div[@class=\"webix_cal_month\"])[2]"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//div[@class=\"webix_cal_month\"])[2]"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='2023']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Sep']"))).click();

		WebElement day1 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//span[text()='30'])[4]")));
		day1.click();



		driver.findElement(By.xpath("(//input[@placeholder=\"HH:MM\"])[2]")).clear();

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='fa fa-save']"))).click(); Thread.sleep(2000);
		By alertXpath = By.xpath("//div[text()='Departure Time cannot be empty. ']");
		WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(alertXpath));
		Assert.assertTrue(alert.isDisplayed(), "Alert message is not displayed.");
	}

	@Test(dependsOnMethods = "Test_Sucessfull_Login", priority = 9)
	public void Test_Mandatory_Guest() throws InterruptedException, AWTException {
		driver.get("https://test1dns.wincloudpms.net/TravelAgentBlock/FOReservation?VN=3.04.025");
		WebElement add = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@class='webix_el_box'])[2]")));
		add.click();
		WebElement arrivalDate = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@aria-label='Arrival']")));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", arrivalDate);

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='webix_cal_month_name']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='webix_cal_month_name']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='2023']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Sep']"))).click();

		WebElement day = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//span[text()='29'])[2]")));
		day.click();
		
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='fa fa-save']"))).click();
		By alertXpath = By.xpath("//div[text()='Last Name cannot be empty.']");
		WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(alertXpath));
		Assert.assertTrue(alert.isDisplayed(), "Alert message is not displayed.");

	}

	@Test(dependsOnMethods = "Test_Sucessfull_Login", priority = 19)
	public void Test_Complete_Reservation() {
		driver.get("https://test1dns.wincloudpms.net/TravelAgentBlock/FOReservation?VN=3.04.025");

		WebElement add = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@class='webix_el_box'])[2]")));
		add.click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@aria-label='RoomType']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@webix_l_id='DXR']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='fa fa-search ExpBkGridIconBtn']"))).click();

		WebElement doubleClick = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//div[@aria-rowindex='5'])[1]")));
		new Actions(driver).doubleClick(doubleClick).perform();

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='OK']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='OK']"))).click();

		// Click the save button (only once)
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='fa fa-save']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//button[@class=\"webix_button webix_img_btn\"])[1]"))).click();

		WebElement reserveNoInput = driver.findElement(
				By.xpath("//label[text()='Reserve No']/following-sibling::input")
				);

		// Get the value from the input
		String reserveNoValue = reserveNoInput.getAttribute("value");
		System.out.println("Reserve No: " + reserveNoValue);
		Assert.assertNotNull(reserveNoValue, "Reserve No field is null.");
		Assert.assertFalse(reserveNoValue.trim().isEmpty(), "Reserve No is empty - Reservation not created.");

	}

	@Test(dependsOnMethods = "Test_Sucessfull_Login", priority = 10)
	public void Test_Mandatory_Ratecode() throws InterruptedException, AWTException {
		driver.get("https://test1dns.wincloudpms.net/TravelAgentBlock/FOReservation?VN=3.04.025");
		WebElement add = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@class='webix_el_box'])[2]")));
		add.click();
		WebElement arrivalDate = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@aria-label='Arrival']")));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", arrivalDate);

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='webix_cal_month_name']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='webix_cal_month_name']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='2023']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Sep']"))).click();

		WebElement day = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//span[text()='29'])[2]")));
		day.click();
		
		
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='fa fa-search ExpBkGridIconBtn']"))).click();

		WebElement doubleClick = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//div[@aria-rowindex='5'])[1]")));
		new Actions(driver).doubleClick(doubleClick).perform();

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='OK']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='OK']"))).click();

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='fa fa-save']"))).click();
		By alertXpath = By.xpath("//div[text()='Ratecode cannot be empty.']");
		WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(alertXpath));
		Assert.assertTrue(alert.isDisplayed(), "Alert message is not displayed.");

	}
	@Test(dependsOnMethods = "Test_Sucessfull_Login", priority = 11)
	public void Test_Mandatory_Roomtype() throws InterruptedException, AWTException {
		driver.get("https://test1dns.wincloudpms.net/TravelAgentBlock/FOReservation?VN=3.04.025");
		WebElement add = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@class='webix_el_box'])[2]")));
		add.click();
		WebElement arrivalDate = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@aria-label='Arrival']")));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", arrivalDate);

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='webix_cal_month_name']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='webix_cal_month_name']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='2023']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Sep']"))).click();

		WebElement day = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//span[text()='29'])[2]")));
		day.click();
		
		
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='fa fa-search ExpBkGridIconBtn']"))).click();

		WebElement doubleClick = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//div[@aria-rowindex='5'])[1]")));
		new Actions(driver).doubleClick(doubleClick).perform();

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='OK']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='OK']"))).click();

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//span[@class=\"webix_input_icon wxi-search\"])[3]"))).click();
		By alertXpath = By.xpath("//div[text()='Room Type cannot be empty']");
		WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(alertXpath));
		Assert.assertTrue(alert.isDisplayed(), "Alert message is not displayed.");
	
}
	@Test(dependsOnMethods = "Test_Sucessfull_Login", priority = 12)
	public void Test_Mandatory_Segment() throws InterruptedException, AWTException {
		driver.get("https://test1dns.wincloudpms.net/TravelAgentBlock/FOReservation?VN=3.04.025");
		WebElement add = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@class='webix_el_box'])[2]")));
		add.click();
		WebElement arrivalDate = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@aria-label='Arrival']")));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", arrivalDate);

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='webix_cal_month_name']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='webix_cal_month_name']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='2023']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Sep']"))).click();

		WebElement day = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//span[text()='29'])[2]")));
		day.click();

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@aria-label='RoomType']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@webix_l_id='DXR']"))).click();

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='fa fa-search ExpBkGridIconBtn']"))).click();

		WebElement doubleClick = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//div[@aria-rowindex='5'])[1]")));
		new Actions(driver).doubleClick(doubleClick).perform();

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='OK']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='OK']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@aria-label=\"Segment\"]"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//div[@role=\"option\"])[1]"))).click();


		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='fa fa-save']"))).click();
		By alertXpath = By.xpath("//div[text()='Segment cannot be empty.']");
		WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(alertXpath));
		Assert.assertTrue(alert.isDisplayed(), "Alert message is not displayed.");

	}
	@Test(dependsOnMethods = "Test_Sucessfull_Login", priority = 13)
	public void Test_Mandatory_GuestType() throws InterruptedException, AWTException {
		driver.get("https://test1dns.wincloudpms.net/TravelAgentBlock/FOReservation?VN=3.04.025");
		WebElement add = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@class='webix_el_box'])[2]")));
		add.click();
		WebElement arrivalDate = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@aria-label='Arrival']")));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", arrivalDate);

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='webix_cal_month_name']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='webix_cal_month_name']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='2023']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Sep']"))).click();

		WebElement day = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//span[text()='29'])[2]")));
		day.click();

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@aria-label='RoomType']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@webix_l_id='DXR']"))).click();

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='fa fa-search ExpBkGridIconBtn']"))).click();

		WebElement doubleClick = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//div[@aria-rowindex='5'])[1]")));
		new Actions(driver).doubleClick(doubleClick).perform();

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='OK']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='OK']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@aria-label=\"Guest Type\"]"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//div[@role=\"option\"])[1]"))).click();


		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='fa fa-save']"))).click();
		By alertXpath = By.xpath("//div[text()='Guest Type cannot be empty.']");
		WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(alertXpath));
		Assert.assertTrue(alert.isDisplayed(), "Alert message is not displayed.");
}
	@Test(dependsOnMethods = "Test_Sucessfull_Login", priority = 14)
	public void Test_Mandatory_NoofRooms() throws InterruptedException, AWTException {
		driver.get("https://test1dns.wincloudpms.net/TravelAgentBlock/FOReservation?VN=3.04.025");
		WebElement add = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@class='webix_el_box'])[2]")));
		add.click();
		WebElement arrivalDate = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@aria-label='Arrival']")));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", arrivalDate);

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='webix_cal_month_name']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='webix_cal_month_name']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='2023']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Sep']"))).click();

		WebElement day = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//span[text()='29'])[2]")));
		day.click();

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@aria-label='RoomType']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@webix_l_id='DXR']"))).click();

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='fa fa-search ExpBkGridIconBtn']"))).click();

		WebElement doubleClick = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//div[@aria-rowindex='5'])[1]")));
		new Actions(driver).doubleClick(doubleClick).perform();

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='OK']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='OK']"))).click();
		
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[normalize-space(text())='Rooms']/following-sibling::input[1]"))).clear();


		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='fa fa-save']"))).click();
		By alertXpath = By.xpath("//div[text()='Room cannot be empty.']");
		WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(alertXpath));
		Assert.assertTrue(alert.isDisplayed(), "Alert message is not displayed.");
}
	
	@Test(dependsOnMethods = "Test_Sucessfull_Login", priority = 15)
	public void Test_CheckinList_Confirmed() throws InterruptedException, AWTException {
		driver.get("https://test1dns.wincloudpms.net/TravelAgentBlock/FOReservation?VN=3.04.025");

		WebElement add = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@class='webix_el_box'])[2]")));
		add.click();

		WebElement arrivalDate = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@aria-label='Arrival']")));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", arrivalDate);

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='webix_cal_month_name']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='webix_cal_month_name']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='2023']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Sep']"))).click();

		WebElement day = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//span[text()='29'])[2]")));
		day.click();

		WebElement nightsInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//label[text()='Nights']/following-sibling::input")));
		nightsInput.clear();
		nightsInput.sendKeys("2");

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@aria-label='RoomType']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@webix_l_id='DXR']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='fa fa-search ExpBkGridIconBtn']"))).click();

		WebElement doubleClick = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//div[@aria-rowindex='5'])[1]")));
		new Actions(driver).doubleClick(doubleClick).perform();

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='OK']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='OK']"))).click();

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='fa fa-save']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//button[@class='webix_button webix_img_btn'])[1]"))).click();

		WebElement reserveNoInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//label[text()='Reserve No']/following-sibling::input")));
		String reserveNoValue = reserveNoInput.getAttribute("value");
		System.out.println("Reserve No: " + reserveNoValue);

		// Navigate to reservation list
		driver.navigate().to("https://test1dns.wincloudpms.net/TravelAgentBlock/FOReservation?MODE=C&VN=3.04.025");
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class=' fa fa-folder-open']"))).click();

		WebElement filterInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//div[@row='1'])[1]")));
		filterInput.click();

		Robot robot = new Robot();
		robot.setAutoDelay(200);
		for (char ch : reserveNoValue.toCharArray()) {
			typeCharWithRobot1(robot, ch);
		}

		WebElement dynamicElement = wait.until(ExpectedConditions.elementToBeClickable(
				By.xpath("//div[text()='" + reserveNoValue + "']")));
		Actions actions = new Actions(driver);
		actions.doubleClick(dynamicElement).perform();
		WebElement checkin = driver.findElement(By.xpath("//span[text()=\"CheckIn\"]"));

		Assert.assertTrue(checkin.isDisplayed(), "CheckIn element is not visible.");


	}

	private void typeCharWithRobot1(Robot robot, char ch) {
		int keyCode = KeyEvent.getExtendedKeyCodeForChar(ch);
		if (KeyEvent.CHAR_UNDEFINED == keyCode) {
			throw new RuntimeException("Key code not found for character '" + ch + "'");
		}

		boolean upperCase = Character.isUpperCase(ch);
		if (upperCase) {
			robot.keyPress(KeyEvent.VK_SHIFT);
		}

		robot.keyPress(keyCode);
		robot.keyRelease(keyCode);

		if (upperCase) {
			robot.keyRelease(KeyEvent.VK_SHIFT);
		}
	}

	@Test(dependsOnMethods = "Test_Sucessfull_Login", priority = 16)
	public void Test_CheckinList_Tentative() throws InterruptedException, AWTException {
		driver.get("https://test1dns.wincloudpms.net/TravelAgentBlock/FOReservation?VN=3.04.025");

		WebElement add = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@class='webix_el_box'])[2]")));
		add.click();

		WebElement arrivalDate = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@aria-label='Arrival']")));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", arrivalDate);

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='webix_cal_month_name']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='webix_cal_month_name']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='2023']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Sep']"))).click();

		WebElement day = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//span[text()='29'])[2]")));
		day.click();

		WebElement nightsInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//label[text()='Nights']/following-sibling::input")));
		nightsInput.clear();
		nightsInput.sendKeys("2");

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@aria-label='RoomType']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@webix_l_id='DXR']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='fa fa-search ExpBkGridIconBtn']"))).click();

		WebElement doubleClick = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//div[@aria-rowindex='5'])[1]")));
		new Actions(driver).doubleClick(doubleClick).perform();

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='OK']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='OK']"))).click();

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@aria-label=\"Status\"]"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()=\"Tentative\"]"))).click();
		WebElement tipElement = driver.findElement(By.id("zs-fl-tip"));

		Actions actions = new Actions(driver);
		actions.moveToElement(tipElement).perform(); // Hover over the element

		driver.findElement(By.id("zs-tip-close")).click();
		// Scroll to the bottom of the page

		WebElement scrollElement = driver.findElement(By.xpath("(//div[@class='webix_vscroll_body'])[3]"));

		JavascriptExecutor js1 = (JavascriptExecutor) driver;
		js1.executeScript("arguments[0].scrollIntoView(true);", scrollElement);

		// Wait until the "Due Date" element is clickable
		WebElement dueDate = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@aria-label='Due Date']")));

		// Click on the Due Date element using JavaScriptExecutor
		js1.executeScript("arguments[0].click();", dueDate);


		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//span[@class=\"webix_cal_month_name\"])[2]"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//span[@class=\"webix_cal_month_name\"])[2]"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='2023']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Sep']"))).click();

		WebElement day1 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//span[text()=\"29\"])[4]")));
		day1.click();

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='fa fa-save']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//button[@class='webix_button webix_img_btn'])[1]"))).click();

		WebElement reserveNoInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//label[text()='Reserve No']/following-sibling::input")));
		String reserveNoValue = reserveNoInput.getAttribute("value");
		System.out.println("Reserve No: " + reserveNoValue);

		// Navigate to reservation list
		driver.navigate().to("https://test1dns.wincloudpms.net/TravelAgentBlock/FOReservation?MODE=C&VN=3.04.025");
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class=' fa fa-folder-open']"))).click();

		WebElement filterInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//div[@row='1'])[1]")));
		filterInput.click();

		Robot robot = new Robot();
		robot.setAutoDelay(200);
		for (char ch : reserveNoValue.toCharArray()) {
			typeCharWithRobot(robot, ch);
		}



		try {
		    // Wait for the dynamic element to be clickable
		    WebElement dynamicElement = wait.until(ExpectedConditions.elementToBeClickable(
		            By.xpath("//div[text()='" + reserveNoValue + "']")));

		    // If the element is clickable, the test should fail
		    Assert.fail("The dynamic element is clickable, but it should not be.");
		} catch (TimeoutException e) {
		    // If the element is not found (not clickable), the test passes
		    System.out.println("The dynamic element is not clickable. Test passed.");
		}

	}

	private void typeCharWithRobotA(Robot robot, char ch) {
		int keyCode = KeyEvent.getExtendedKeyCodeForChar(ch);
		if (KeyEvent.CHAR_UNDEFINED == keyCode) {
			throw new RuntimeException("Key code not found for character '" + ch + "'");
		}

		boolean upperCase = Character.isUpperCase(ch);
		if (upperCase) {
			robot.keyPress(KeyEvent.VK_SHIFT);
		}

		robot.keyPress(keyCode);
		robot.keyRelease(keyCode);

		if (upperCase) {
			robot.keyRelease(KeyEvent.VK_SHIFT);
		}
	}


	@Test(dependsOnMethods = "Test_Sucessfull_Login", priority = 17)
	public void Test_CheckinList_WaitingList() throws InterruptedException, AWTException {
		driver.get("https://test1dns.wincloudpms.net/TravelAgentBlock/FOReservation?VN=3.04.025");

		WebElement add = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@class='webix_el_box'])[2]")));
		add.click();

		WebElement arrivalDate = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@aria-label='Arrival']")));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", arrivalDate);

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='webix_cal_month_name']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='webix_cal_month_name']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='2023']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Sep']"))).click();

		WebElement day = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//span[text()='29'])[2]")));
		day.click();

		WebElement nightsInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//label[text()='Nights']/following-sibling::input")));
		nightsInput.clear();
		nightsInput.sendKeys("2");

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@aria-label='RoomType']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@webix_l_id='DXR']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='fa fa-search ExpBkGridIconBtn']"))).click();

		WebElement doubleClick = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//div[@aria-rowindex='5'])[1]")));
		new Actions(driver).doubleClick(doubleClick).perform();

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='OK']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='OK']"))).click();
		
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@aria-label=\"Status\"]"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()=\"Wait List\"]"))).click();


		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='fa fa-save']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//button[@class='webix_button webix_img_btn'])[1]"))).click();

		WebElement reserveNoInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//label[text()='Reserve No']/following-sibling::input")));
		String reserveNoValue = reserveNoInput.getAttribute("value");
		System.out.println("Reserve No: " + reserveNoValue);

		// Navigate to reservation list
		driver.navigate().to("https://test1dns.wincloudpms.net/TravelAgentBlock/FOReservation?MODE=C&VN=3.04.025");
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class=' fa fa-folder-open']"))).click();

		WebElement filterInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//div[@row='1'])[1]")));
		filterInput.click();

		Robot robot = new Robot();
		robot.setAutoDelay(200);
		for (char ch : reserveNoValue.toCharArray()) {
			typeCharWithRobot1(robot, ch);
		}

	
		
		try {
		    // Wait for the dynamic element to be clickable
		    WebElement dynamicElement = wait.until(ExpectedConditions.elementToBeClickable(
		            By.xpath("//div[text()='" + reserveNoValue + "']")));

		    // If the element is clickable, the test should fail
		    Assert.fail("The dynamic element is clickable, but it should not be.");
		} catch (TimeoutException e) {
		    // If the element is not found (not clickable), the test passes
		    System.out.println("The dynamic element is not clickable. Test passed.");
		}

	}

	private void typeCharWithRobot111(Robot robot, char ch) {
		int keyCode = KeyEvent.getExtendedKeyCodeForChar(ch);
		if (KeyEvent.CHAR_UNDEFINED == keyCode) {
			throw new RuntimeException("Key code not found for character '" + ch + "'");
		}

		boolean upperCase = Character.isUpperCase(ch);
		if (upperCase) {
			robot.keyPress(KeyEvent.VK_SHIFT);
		}

		robot.keyPress(keyCode);
		robot.keyRelease(keyCode);

		if (upperCase) {
			robot.keyRelease(KeyEvent.VK_SHIFT);
		}
	}


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Test(dependsOnMethods = "Test_Sucessfull_Login", priority = 20)
	public void Test_Complete_Checkin() throws InterruptedException, AWTException {
		driver.get("https://test1dns.wincloudpms.net/TravelAgentBlock/FOReservation?VN=3.04.025");
		WebElement add = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@class='webix_el_box'])[2]")));
		add.click();
		WebElement arrivalDate = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@aria-label='Arrival']")));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", arrivalDate);

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='webix_cal_month_name']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='webix_cal_month_name']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='2023']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Sep']"))).click();

		WebElement day = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//span[text()='29'])[2]")));
		day.click();
		WebElement nightsInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//label[text()='Nights']/following-sibling::input")
				));
		nightsInput.clear();
		nightsInput.sendKeys("2");

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@aria-label='RoomType']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@webix_l_id='DXR']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='fa fa-search ExpBkGridIconBtn']"))).click();

		WebElement doubleClick = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//div[@aria-rowindex='5'])[1]")));
		new Actions(driver).doubleClick(doubleClick).perform();

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='OK']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='OK']"))).click();

		// Click the save button (only once)
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='fa fa-save']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//button[@class=\"webix_button webix_img_btn\"])[1]"))).click();
		WebElement reserveNoInput1 = driver.findElement(
				By.xpath("//label[text()='Reserve No']/following-sibling::input")
				);

		// Get the value from the input
		// Get the Reserve No value
		String reserveNoValue1 = reserveNoInput1.getAttribute("value");
		System.out.println("Reserve No: " + reserveNoValue1);

		driver.navigate().to("https://test1dns.wincloudpms.net/TravelAgentBlock/FOReservation?MODE=C&VN=3.04.025");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class=\" fa fa-folder-open\"]")));
		driver.findElement(By.xpath("//span[@class=\" fa fa-folder-open\"]")).click();

		WebElement filterInput = wait.until(ExpectedConditions.elementToBeClickable(
				By.xpath("(//div[@row=\"1\"])[1]")));

		filterInput.click(); Thread.sleep(2000);

		

		// Use Robot to type each character
		Robot robot = new Robot();
		robot.setAutoDelay(200);

		for (char ch : reserveNoValue1.toCharArray()) {
			typeCharWithRobot1(robot, ch);
		}	
		
		Actions actions = new Actions(driver);
		WebElement dynamicElement = wait.until(ExpectedConditions.elementToBeClickable(
				By.xpath("//div[text()='" + reserveNoValue1 + "']")));
		actions.doubleClick(dynamicElement).perform();
		

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//span[@class=\"webix_input_icon wxi-search\"])[1]")));
		driver.findElement(By.xpath("(//span[@class=\"webix_input_icon wxi-search\"])[1]")).click();


		List<WebElement> vacantRooms = driver.findElements(By.cssSelector(".RmBgColorV1"));
		if (!vacantRooms.isEmpty()) {
			WebElement firstVacantRoom = vacantRooms.get(0);
			js.executeScript("arguments[0].scrollIntoView(true);", firstVacantRoom);
			firstVacantRoom.click();
			System.out.println(firstVacantRoom + "First vacant room selected.");

		} else {
			System.out.println("No vacant rooms found.");
		}
		driver.findElement(By.xpath("//button[text()=\"Select\"]")).click();
		driver.findElement(By.xpath("//span[text()=\"CheckIn\"]")).click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@style=\"text-align:center !important;font-weight:bold;width:100%;color:#fb2510\"]")));
		WebElement regnum = driver.findElement(By.xpath("//div[@style=\"text-align:center !important;font-weight:bold;width:100%;color:#fb2510\"]"));
		String regText = regnum.getText();    
		System.out.println(regText);  
	
		Assert.assertTrue(regnum.isDisplayed(), "The alert element is not displayed!");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//input[@type='checkbox'])[6]")));
		driver.findElement(By.xpath("(//input[@type=\"checkbox\"])[6]")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@class='webix_button webix_img_btn']")));
		driver.findElement(By.xpath("//button[@class=\"webix_button webix_img_btn\"]")).click();
	}

	private void typeCharWithRobot(Robot robot, char ch) {
		// TODO Auto-generated method stub
		int keyCode = KeyEvent.getExtendedKeyCodeForChar(ch);
		if (KeyEvent.CHAR_UNDEFINED == keyCode) {
			throw new RuntimeException("Key code not found for character '" + ch + "'");
		}

		boolean upperCase = Character.isUpperCase(ch);
		if (upperCase) {
			robot.keyPress(KeyEvent.VK_SHIFT);
		}

		robot.keyPress(keyCode);
		robot.keyRelease(keyCode);

		if (upperCase) {
			robot.keyRelease(KeyEvent.VK_SHIFT);
		}
	}
	 @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
	 @Test(dependsOnMethods = "Test_Sucessfull_Login", priority = 18)
	 public void Test_Advance_Amount() throws InterruptedException, AWTException {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			JavascriptExecutor js = (JavascriptExecutor) driver;

			driver.get("https://test1dns.wincloudpms.net/TravelAgentBlock/FOReservation?VN=3.04.025");

			WebElement add = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@class='webix_el_box'])[2]")));
			add.click();

			WebElement arrivalDate = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@aria-label='Arrival']")));
			js.executeScript("arguments[0].click();", arrivalDate);

			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='webix_cal_month_name']"))).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='webix_cal_month_name']"))).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='2023']"))).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Sep']"))).click();

			WebElement day = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//span[text()='29'])[2]")));
			day.click();

			WebElement nightsInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[text()='Nights']/following-sibling::input")));
			nightsInput.clear();
			nightsInput.sendKeys("2");

			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@aria-label='RoomType']"))).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@webix_l_id='DXR']"))).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='fa fa-search ExpBkGridIconBtn']"))).click();

			WebElement doubleClick = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//div[@aria-rowindex='5'])[1]")));
			new Actions(driver).doubleClick(doubleClick).perform();

			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='OK']"))).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='OK']"))).click();

			// Click the save button once
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='fa fa-save']"))).click();

			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//button[@class='webix_button webix_img_btn'])[1]"))).click();

			WebElement reserveNoInput1 = driver.findElement(By.xpath("//label[text()='Reserve No']/following-sibling::input"));
			String reserveNoValue1 = reserveNoInput1.getAttribute("value");
			System.out.println("Reserve No: " + reserveNoValue1);

			driver.navigate().to("https://test1dns.wincloudpms.net/TravelAgentBlock/FOReservation?MODE=C&VN=3.04.025");

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class=' fa fa-folder-open']"))).click();

			WebElement filterInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//div[@row='1'])[1]")));
			filterInput.click();
			Thread.sleep(2000);

			Robot robot = new Robot();
			robot.setAutoDelay(200);

			for (char ch : reserveNoValue1.toCharArray()) {
				typeCharWithRobot(robot, ch);
			}
			// Wait until the element is present and clickable
			By dynamicLocator = By.xpath("//div[text()='" + reserveNoValue1 + "']");

			// Retry-safe click action
			WebElement dynamicElement = wait.until(ExpectedConditions.elementToBeClickable(dynamicLocator));

			// Scroll into view (optional)
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", dynamicElement);

			// Re-fetch right before interacting
			dynamicElement = driver.findElement(dynamicLocator);

			// Perform double-click (or JavaScript click if needed)
			Actions actions = new Actions(driver);
			actions.moveToElement(dynamicElement).doubleClick().perform();

			// Use Robot to type each character of reserveNoValue1
			

			//WebElement dynamicElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='" + reserveNoValue1 + "']")));

			//	    Thread.sleep(1000);
			//	    Actions actions = new Actions(driver);
			//	    actions.doubleClick(dynamicElement).perform();




			System.out.println("executed");

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Ledger']"))).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Payment']"))).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//button[text()='Advance'])[2]"))).click();

			Thread.sleep(2000);
			Robot robot1 = new Robot();
			robot1.setAutoDelay(200);

			typeChar(robot1, 'r');
			typeChar(robot1, 'b');
			typeChar(robot1, 's');
			typeChar(robot1, 'g');
			typeChar(robot1, 'o');

			robot1.keyPress(KeyEvent.VK_ENTER);
			robot1.keyRelease(KeyEvent.VK_ENTER);

			// Switch to iframe and enter amount
			WebElement iframe = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//iframe[contains(@src, 'TravelAgentBlock/FoResAdvance')]")));
			driver.switchTo().frame(iframe);
			
			String expected = "300.000";

			WebElement amountInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[text()='Amount']/following-sibling::input[@type='text']")));
			amountInput.click();
			amountInput.sendKeys(expected);

			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='fa fa-plus']"))).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Ok']"))).click();


			WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement saveButton = wait1.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[.//span[contains(@class, 'fa-save')]]")));
			saveButton.click();

			wait.until(ExpectedConditions.elementToBeClickable( By.xpath("(//button[text()=\"Ok\"])[2]"))).click();
			driver.switchTo().defaultContent();

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//span[@class=\"webix_icon webix_icon wxi-close\"])[1]"))).click();

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//span[@class=\"webix_input_icon wxi-search\"])[1]")));

			driver.findElement(By.xpath("(//span[@class=\"webix_input_icon wxi-search\"])[1]")).click();

			List<WebElement> vacantRooms = driver.findElements(By.cssSelector(".RmBgColorV1"));
			if (!vacantRooms.isEmpty()) {
				WebElement firstVacantRoom = vacantRooms.get(0);
				js.executeScript("arguments[0].scrollIntoView(true);", firstVacantRoom);
				firstVacantRoom.click();
				System.out.println("First vacant room selected.");
			} else {
				System.out.println("No vacant rooms found.");
			}

			driver.findElement(By.xpath("//button[text()='Select']")).click();
			driver.findElement(By.xpath("//span[text()='CheckIn']")).click();

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@style='text-align:center !important;font-weight:bold;width:100%;color:#fb2510']")));
			WebElement regnum = driver.findElement(By.xpath("//div[@style='text-align:center !important;font-weight:bold;width:100%;color:#fb2510']"));
			String regText = regnum.getText();
			System.out.println(regText);

			String lastFiveChars = regText.substring(regText.length() - 5);
			System.out.println("Last 5 characters: " + lastFiveChars);

			//Assert.assertTrue(regnum.isDisplayed(), "The alert element is not displayed!");

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//input[@type='checkbox'])[6]"))).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@class='webix_button webix_img_btn']"))).click();

			driver.navigate().to("https://test1dns.wincloudpms.net/TravelAgentBlock/FoInHouseGuest?VN=3.04.025");
			WebElement filterInput1 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//div[@row='1'])[1]")));
			filterInput1.click();
		
			
			try {
				Robot robot11 = new Robot();
				Thread.sleep(500);

				for (char ch : lastFiveChars.toCharArray()) {
					typeCharacter(robot11, ch);
					Thread.sleep(100);
				}

			} catch (AWTException | InterruptedException e) {
				e.printStackTrace();
			}
			WebElement dynamicElement1 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='" + lastFiveChars + "']")));

			Thread.sleep(1000);
			Actions actions1 = new Actions(driver);
			actions1.doubleClick(dynamicElement1).perform();

			
			Thread.sleep(1000);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()=\"Bill Details\"]"))).click();

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@class=\"webix_last_topcell webix_topcell webix_cell FBDTotRow\"])[2]")));
			WebElement bill_amount = driver.findElement(By.xpath("(//div[@class=\"webix_last_topcell webix_topcell webix_cell FBDTotRow\"])[2]"));

			String Actual = bill_amount.getText();
			System.out.println("Original Bill amount: " + Actual);

			// Remove first character if possible and assign back to Actual
			if (Actual.length() > 1) {
			    Actual = Actual.substring(1);
			    System.out.println("Bill without first character: " + Actual);
			} else {
			    System.out.println("Bill value is too short to remove first character: " + Actual);
			}

			// Now assert using Actual without first char
			Assert.assertEquals(Actual, expected, "Amount mismatch between expected and actual bill value!");

		}

		// Helper method to type characters with Robot
		public static void typeCharacter(Robot robot, char character) {
			try {
				boolean upperCase = Character.isUpperCase(character);
				int keyCode = KeyEvent.getExtendedKeyCodeForChar(character);
				if (keyCode == KeyEvent.VK_UNDEFINED) {
					System.out.println("Cannot type character: " + character);
					return;
				}
				if (upperCase) {
					robot.keyPress(KeyEvent.VK_SHIFT);
				}

				robot.keyPress(keyCode);
				robot.keyRelease(keyCode);

				if (upperCase) {
					robot.keyRelease(KeyEvent.VK_SHIFT);
				}
			} catch (IllegalArgumentException e) {
				System.out.println("Unsupported character: " + character);
			}
		}

		// Helper method to press a character key using Robot
		private void typeChar(Robot robot, char character) {
			boolean isUpperCase = Character.isUpperCase(character);
			int keyCode = KeyEvent.getExtendedKeyCodeForChar(character);

			if (keyCode == KeyEvent.CHAR_UNDEFINED) {
				throw new RuntimeException("Key code not found for character '" + character + "'");
			}

			if (isUpperCase) {
				robot.keyPress(KeyEvent.VK_SHIFT);
			}

			robot.keyPress(keyCode);
			robot.keyRelease(keyCode);

			if (isUpperCase) {
				robot.keyRelease(KeyEvent.VK_SHIFT);
			}
		}

		private void typeCharWithRobot1111(Robot robot, char ch) {
			int keyCode = KeyEvent.getExtendedKeyCodeForChar(ch);
			if (keyCode == KeyEvent.CHAR_UNDEFINED) {
				throw new RuntimeException("Key code not found for character '" + ch + "'");
			}

			boolean upperCase = Character.isUpperCase(ch);
			if (upperCase) {
				robot.keyPress(KeyEvent.VK_SHIFT);
			}

			robot.keyPress(keyCode);
			robot.keyRelease(keyCode);

			if (upperCase) {
				robot.keyRelease(KeyEvent.VK_SHIFT);
			}
		}
	
}