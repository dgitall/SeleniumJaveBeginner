package com.herokuapp.theinternet;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

public class PositiveTests {

	@Test
	public void loginTest() {
		System.out.println("Starting loginTest");
		// Create Driver
		// Start with the Chrome driver
		System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
		WebDriver driver = new ChromeDriver();

		// Just for this example, slow down the code to see it.
		sleep(3000);

		// Maximize browser window
		driver.manage().window().maximize();

		// open test page
		String url = "http://the-internet.herokuapp.com/login";
		driver.get(url);
		System.out.println("Page is opened.");
		
		sleep(2000);

//		enter user name
		WebElement username = driver.findElement(By.id("username"));
		
//		enter password
		WebElement password = driver.findElement(By.xpath("//input[@id='password']"));
		
//		click login button
		WebElement login = driver.findElement(By.xpath("//form[@id='login']/button[@class='radius']"));
		
//		verifications:
//			new url
//			logout button visible
//			successful login message
		
		// Close browser
		driver.quit();

	}

	private void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
