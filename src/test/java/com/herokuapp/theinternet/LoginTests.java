package com.herokuapp.theinternet;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
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

public class LoginTests {

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

	@Test(priority = 1, groups = { "positiveTests", "smokeTests" })
	public void positiveLogInTest() {
		System.out.println("Starting loginTest");

		// open test page
		String url = "http://the-internet.herokuapp.com/login";
		driver.get(url);
		System.out.println("Page is opened.");

//		enter user name
		WebElement username = driver.findElement(By.id("username"));
		username.sendKeys("tomsmith");

//		enter password
		WebElement password = driver.findElement(By.name("password"));
		password.sendKeys("SuperSecretPassword!");
		
		// Create a variable for waits on buttons
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

//		click login button
		WebElement logInButton = driver.findElement(By.tagName("button"));
		wait.until(ExpectedConditions.elementToBeClickable(logInButton));
		logInButton.click();

//		verifications:
//			new url
		String expectedUrl = "http://the-internet.herokuapp.com/secure";
		String actualUrl = driver.getCurrentUrl();

		Assert.assertEquals(actualUrl, expectedUrl, "Actual page url is not the same as expected");

//			logout button visible
		WebElement logOutButton = driver.findElement(By.xpath("//a[@class='button secondary radius']"));
		Assert.assertTrue(logOutButton.isDisplayed(), "Log Out button is not visible.");

//			successful login message
		WebElement successMessage = driver.findElement(By.cssSelector("#flash"));
		String expectedMessage = "You logged into a secure area!";
		String actualMessage = successMessage.getText();
		// This fails because the getText() returns the text from subelements, too.
//		Assert.assertEquals(actualMessage, expectedMessage, "Successful login message Failed");
		Assert.assertTrue(actualMessage.contains(expectedMessage),
				"Actual message does not contain expected message.\nActual Message: " + actualMessage
						+ "\nExpected Message: " + expectedMessage);

	}

	@Parameters({ "username", "password", "expectedMessage" })
	@Test(priority = 2, groups = { "negativeTests", "smokeTests" })
	public void negativeLogInTest(String username, String password, String expectedErrorMessage) {
		System.out.println("Starting negativeLogInTest with " + username + " and " + password);

		// open test page
		String url = "http://the-internet.herokuapp.com/login";
		driver.get(url);
		System.out.println("Page is opened.");

//		enter user name
		WebElement usernameField = driver.findElement(By.id("username"));
		usernameField.sendKeys(username);

//		enter password
		WebElement passwordField = driver.findElement(By.name("password"));
		passwordField.sendKeys(password);

//		click login button
		WebElement logInButton = driver.findElement(By.tagName("button"));
		logInButton.click();

//		verifications:
//			new url
		String expectedUrl = "http://the-internet.herokuapp.com/login";
		String actualUrl = driver.getCurrentUrl();

		Assert.assertEquals(actualUrl, expectedUrl, "Actual page url is not the same as expected");

//			Failed message
		WebElement failedUserNameMessage = driver.findElement(By.xpath("//div[@id='flash']"));
		String actualMessage = failedUserNameMessage.getText();
		// This fails because the getText() returns the text from subelements, too.
//		Assert.assertEquals(actualMessage, expectedMessage, "Successful login message Failed");
		Assert.assertTrue(actualMessage.contains(expectedErrorMessage),
				"Actual message does not contain expected message.\nActual Message: " + actualMessage
						+ "\nExpected Message: " + expectedErrorMessage);

	}

	@AfterMethod(alwaysRun = true)
	private void tearDown() {
		// Close browser
		driver.quit();
	}

}
