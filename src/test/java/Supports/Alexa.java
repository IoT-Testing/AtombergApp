package Supports;

import Actions.Tap;
import org.awaitility.core.IgnoredException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import io.appium.java_client.ios.IOSDriver;
import Actions.Swipe;

import java.util.List;
import java.util.stream.Collectors;

public class Alexa {
	public static void Connect(IOSDriver driver) {

		WebElement SLD = null;
		try {
			SLD = driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name=\"Select and link device\"]"));
		} catch (Exception e) {
		}
		if (SLD != null) {
			alexa(driver);
		} else {
			driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"More\n" +
					"Tab 3 of 3\"]")).click();
			alexa(driver);
		}

	}

	private static void alexa(IOSDriver driver) {
		WebElement alexaConnect = null;
		WebElement alexaconnected = null;
		try {
			alexaConnect = driver
					.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Amazon Alexa\nConnect\"]"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			alexaconnected = driver
					.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Amazon Alexa\nConnected\"]"));
		} catch (Exception e) {
//			e.printStackTrace();
		}
		if (alexaConnect != null) {
			alexaConnect.click();
			//check alexa linking guide
			ALG(driver);
			linkCheck(driver);
		} else if(alexaconnected!=null){
			System.out.println("Alexa is already connected");
		}

	}

	private static void ALG(IOSDriver driver) {

		driver.findElement(By.xpath("//XCUIElementTypeButton[@name=\"Link\"]")).click();

		List<WebElement> Elements = driver.findElements(By.className("XCUIElementTypeStaticText"));
		List<WebElement> elements = Elements.stream().filter(element -> element.getAttribute("name")!=null).collect(Collectors.toList());
		List<WebElement> algL = elements.stream().filter(element -> element.getAttribute("name").equals("Account linking guide")).collect(Collectors.toList());
		System.out.println(algL.size());
		if (!algL.isEmpty())
		{
			driver.findElement(By.xpath("//XCUIElementTypeButton[@name=\"OK\"]")).click();
			sleep(5000);
			driver.findElement(By.xpath("//XCUIElementTypeButton[@name=\"Yes\"]")).click();
			sleep(5000);
			driver.findElement(By.xpath("//XCUIElementTypeButton[@name=\"LINK\"]")).click();
			sleep(5000);

			alexaCheck(driver);
		}
	}

	private static void alexaCheck(IOSDriver driver){
		WebElement CHECK = null;
		try {
			CHECK = driver.findElement(By.xpath("//android.widget.TextView[@text=\"Sign in with your email and password\"]"));
		}catch(Exception e){}
		if(CHECK != null)
		{
			WebElement Email = driver.findElement(By.xpath("//XCUIElementTypeTextField[@name=\"Email Email\"]"));
			Email.click();
			Email.sendKeys("Weker42331@huleos.com");
			WebElement Password = driver.findElement(By.xpath("//XCUIElementTypeSecureTextField[@name=\"Password Password\"]"));
			Password.click();
			Password.sendKeys("Atomberg@123");
			sleep(1000);
			driver.findElement(By.xpath("//XCUIElementTypeButton[@name=\"submit\"]")).click();
		}

	}

	private static void linkCheck(IOSDriver driver) {
		List<WebElement> Success = driver.findElements(By.className("XCUIElementTypeStaticText"));
		List<WebElement> successM = Success.stream().filter(element -> element.getAttribute("name") != null).collect(Collectors.toList());
		List<WebElement> AppStore = successM.stream().filter(webElement -> webElement.getAttribute("value").equals("Atomberg Home")).collect(Collectors.toList());
		for (WebElement e : AppStore) {
			System.out.println("Alexa Linked Successfully");
			Tap.withCoordinates(driver, 35, 35);
			Tap.withCoordinates(driver, 35, 35);
		}
	}

	private static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}