package com.herokuapp.theinternet;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class NegativeTests {

	@Test
	public void incorrectUserNameTest() {
		System.out.println("Starting loginTest");
		// Create Driver
		// Start with the Chrome driver
		System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
		WebDriver driver = new ChromeDriver();

		// Maximize browser window
		driver.manage().window().maximize();

		// open test page
		String url = "http://the-internet.herokuapp.com/login";
		driver.get(url);
		System.out.println("Page is opened.");

//		enter user name
		WebElement username = driver.findElement(By.id("username"));
		username.sendKeys("badusername");

//		enter password
		WebElement password = driver.findElement(By.name("password"));
		password.sendKeys("SuperSecretPassword!");

//		click login button
		WebElement logInButton = driver.findElement(By.tagName("button"));
		logInButton.click();

//		verifications:
//			new url
		String expectedUrl = "http://the-internet.herokuapp.com/login";
		String actualUrl = driver.getCurrentUrl();

		Assert.assertEquals(actualUrl, expectedUrl, "Actual page url is not the same as expected");

//			Failed username message
		WebElement failedUserNameMessage = driver.findElement(By.xpath("//div[@id='flash']"));
		String expectedMessage = "Your username is invalid!";
		String actualMessage = failedUserNameMessage.getText();
		// This fails because the getText() returns the text from subelements, too.
//		Assert.assertEquals(actualMessage, expectedMessage, "Successful login message Failed");
		Assert.assertTrue(actualMessage.contains(expectedMessage),
				"Actual message does not contain expected message.\nActual Message: " + actualMessage
						+ "\nExpected Message: " + expectedMessage);

		// Close browser
		driver.quit();
	}
	
	@Test
	public void incorrectPasswordTest() {
		System.out.println("Starting loginTest");
		// Create Driver
		// Start with the Chrome driver
		System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
		WebDriver driver = new ChromeDriver();

		// Maximize browser window
		driver.manage().window().maximize();

		// open test page
		String url = "http://the-internet.herokuapp.com/login";
		driver.get(url);
		System.out.println("Page is opened.");

//		enter user name
		WebElement username = driver.findElement(By.id("username"));
		username.sendKeys("tomsmith");

//		enter password
		WebElement password = driver.findElement(By.name("password"));
		password.sendKeys("badpassword");

//		click login button
		WebElement logInButton = driver.findElement(By.tagName("button"));
		logInButton.click();

//		verifications:
//			new url
		String expectedUrl = "http://the-internet.herokuapp.com/login";
		String actualUrl = driver.getCurrentUrl();

		Assert.assertEquals(actualUrl, expectedUrl, "Actual page url is not the same as expected");

//			Failed username message
		WebElement failedUserNameMessage = driver.findElement(By.xpath("//div[@id='flash']"));
		String expectedMessage = "Your password is invalid!";
		String actualMessage = failedUserNameMessage.getText();
		// This fails because the getText() returns the text from subelements, too.
//		Assert.assertEquals(actualMessage, expectedMessage, "Successful login message Failed");
		Assert.assertTrue(actualMessage.contains(expectedMessage),
				"Actual message does not contain expected message.\nActual Message: " + actualMessage
						+ "\nExpected Message: " + expectedMessage);

		// Close browser
		driver.quit();
	}
}
