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
	
	@Test
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
		// This fails without an explicit wait because the finish element doesn't appear right away. It
		//	doesn't fail on finding it because it is there. The text, though, returns empty 
		//	until it has been made visible. The wait until the element is visible will handle this 
		//	situation. 
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
		
		// Try clicking the start button again to see what happens. It was made not visible so should fail. 
		startButton.click();

	}
	
	@Test
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
		// This fails without an explicit wait because the finish element doesn't appear right away. It
		//	doesn't fail on finding it because it is there. The text, though, returns empty 
		//	until it has been made visible. The wait until the element is visible will handle this 
		//	situation. 
		WebElement helloWorldMessage = driver.findElement(By.id("finish"));
		
		// This timeout is too short so the wait will fail and throw an exception
		//	We may want to do some additional steps in case of a timeout. Allow addiitonal
		// steps by wrapping in a try block. Change to a TimeoutException instead of the default
		// of Exception so that it will only go into the catch block if it's a timeout. 
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
		try {
			wait.until(ExpectedConditions.visibilityOf(helloWorldMessage));
		} catch (TimeoutException e) {
			System.out.println("Exception Caught: " + e.getMessage());
			sleep(3000);
			// Sleep will wait awhile before continuing with execution. Maybe long enough for the button to become visible.
		}
		
		String expectedMessage = "Hello World!";
		String actualMessage = helloWorldMessage.getText();
		// This fails because the getText() returns the text from subelements, too.
//		Assert.assertEquals(actualMessage, expectedMessage, "Successful login message Failed");
		Assert.assertTrue(actualMessage.contains(expectedMessage),
				"Actual message does not contain expected message.\nActual Message: " + actualMessage
						+ "\nExpected Message: " + expectedMessage);
		

	}
	
	private void sleep(long m) {
		try {
			Thread.sleep(m);;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
