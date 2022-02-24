package com.herokuapp.theinternet;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class ExceptionTests {

	private WebDriver driver;

	// alwaysRun will run it even if a different group is selected like in the
	// positiveLogInTest
	// Add a parameter to specify the browser driver to use so we can run the same
	// tests on multiple browsers
	@Parameters({ "browser" })
	@BeforeMethod(alwaysRun = true)
	private void setUp(String browser) {
		// Create Driver
		switch (browser) {
		case "chrome": {
			// Chrome driver
			System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
			driver = new ChromeDriver();
			break;
		}
		case "firefox": {
			// Firefox driver
			System.setProperty("webdriver.gecko.driver", "src/main/resources/geckodriver.exe");
			driver = new FirefoxDriver();
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + browser);
		}

		// Maximize browser window
		driver.manage().window().maximize();

	}

	@AfterMethod(alwaysRun = true)
	private void tearDown() {
		// Close browser
		driver.quit();
	}

	@Test(priority = 1)
	public void noVisibleTest() {
		System.out.println("Starting noVisibleTest");

		// open test page
		String url = "http://the-internet.herokuapp.com/dynamic_loading/1";
		driver.get(url);
		System.out.println("Page is opened.");

//		click login button
		WebElement startButton = driver.findElement(By.xpath("//div[@id='start']/button[.='Start']"));
		startButton.click();

//		verifications:

//			Hello World! message
		// This fails without an explicit wait because the finish element doesn't appear
		// right away. It
		// doesn't fail on finding it because it is there. The text, though, returns
		// empty
		// until it has been made visible. The wait until the element is visible will
		// handle this
		// situation.
		WebElement helloWorldMessage = driver.findElement(By.id("finish"));

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOf(helloWorldMessage));

		String expectedMessage = "Hello World!";
		String actualMessage = helloWorldMessage.getText();
		// This fails because the getText() returns the text from subelements, too.
//		Assert.assertEquals(actualMessage, expectedMessage, "Successful login message Failed");
		Assert.assertTrue(actualMessage.contains(expectedMessage),
				"Actual message does not contain expected message.\nActual Message: " + actualMessage
						+ "\nExpected Message: " + expectedMessage);

	}

	@Test(priority = 2)
	public void timeoutTest() {
		System.out.println("Starting noVisibleTest");

		// open test page
		String url = "http://the-internet.herokuapp.com/dynamic_loading/1";
		driver.get(url);
		System.out.println("Page is opened.");

//		click login button
		WebElement startButton = driver.findElement(By.xpath("//div[@id='start']/button[.='Start']"));
		startButton.click();

//		verifications:

//			Hello World! message
		// This fails without an explicit wait because the finish element doesn't appear
		// right away. It
		// doesn't fail on finding it because it is there. The text, though, returns
		// empty
		// until it has been made visible. The wait until the element is visible will
		// handle this
		// situation.
		WebElement helloWorldMessage = driver.findElement(By.id("finish"));

		// This timeout is too short so the wait will fail and throw an exception
		// We may want to do some additional steps in case of a timeout. Allow
		// addiitonal
		// steps by wrapping in a try block. Change to a TimeoutException instead of the
		// default
		// of Exception so that it will only go into the catch block if it's a timeout.
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
		try {
			wait.until(ExpectedConditions.visibilityOf(helloWorldMessage));
		} catch (TimeoutException e) {
			System.out.println("Exception Caught: " + e.getMessage());
			sleep(3000);
			// Sleep will wait awhile before continuing with execution. Maybe long enough
			// for the button to become visible.
		}

		String expectedMessage = "Hello World!";
		String actualMessage = helloWorldMessage.getText();
		// This fails because the getText() returns the text from subelements, too.
//		Assert.assertEquals(actualMessage, expectedMessage, "Successful login message Failed");
		Assert.assertTrue(actualMessage.contains(expectedMessage),
				"Actual message does not contain expected message.\nActual Message: " + actualMessage
						+ "\nExpected Message: " + expectedMessage);

	}

	@Test(priority = 3)
	public void noSuchElementTest() {
		System.out.println("Starting noVisibleTest");

		// open test page
		String url = "http://the-internet.herokuapp.com/dynamic_loading/2";
		driver.get(url);
		System.out.println("Page is opened.");

//		click login button
		WebElement startButton = driver.findElement(By.xpath("//div[@id='start']/button[.='Start']"));
		startButton.click();

//		verifications:

//			Hello World! message
		// This fails because the element is not hidden as in the website above.
		// Instead, it
		// is added later instead of having visibility changed. Handle by moving the
		// wait to before we get the element
		// and change the condition to wait for the presence of the element. Note: this
		// returns the element
		// once it is found

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement helloWorldMessage = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("finish")));

		String expectedMessage = "Hello World! broken";
//		String actualMessage = helloWorldMessage.getText();
		// This fails because the getText() returns the text from subelements, too.
//		Assert.assertEquals(actualMessage, expectedMessage, "Successful login message Failed");
//		Assert.assertTrue(actualMessage.contains(expectedMessage),
//				"Actual message does not contain expected message.\nActual Message: " + actualMessage
//						+ "\nExpected Message: " + expectedMessage);

		// Note this version wraps it all into one assert call. THis version also waits for the text in the
		// element, not the element itself. So, if the text is different, it will sit there until the timeout
		Assert.assertTrue(
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("finish"), expectedMessage)),
				"Actual message does not contain expected message");

	}

	private void sleep(long m) {
		try {
			Thread.sleep(m);
			;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
