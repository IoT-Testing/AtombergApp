package Devices;

import Actions.Tap;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import AtombergTest.Method;
import io.appium.java_client.ios.IOSDriver;

import java.util.List;
import java.util.stream.Collectors;

public class Select {
	public static void Fan(IOSDriver driver) {
		Error(driver);
		WebElement ModelSelect = null;
		try {
			ModelSelect = driver
					.findElement(By.xpath("//XCUIElementTypeStaticText[@name=\"Pick the fan model you're having\"]"));
			System.out.println("Six LED fan");
		} catch (Exception e) {
		}
		if (ModelSelect != null) {
			SixLED(driver);
			Fans.SixLEDColorSelect(driver);
		} else {
			WebElement Others = null;
			try {
				Others = driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name=\"Select your device color\"]"));
				System.out.println("Select your fan color");
			} catch (Exception exp) {
			}
			if (Others != null) {
				WebElement Aris = null;
				WebElement Jaguar = null;
				try {
					Aris = driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Dark Teakwood\"]"));
				} catch (Exception e) {
				}
				try {
					Jaguar = driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Regent Gray\"]"));
				} catch (Exception e) {
				}
				if (Aris != null) {
					Fans.Aris(driver);
				} else if (Jaguar != null) {
					Fans.Jaguar(driver);
				} else {
					Fans.Erica(driver);
				}
				Method.captureScreenshot(driver);
			}
			else 
			{
				SixLED(driver);
			}
		}
		Method.AdditionProcess(driver);

	}

	private static void SixLED(IOSDriver driver) {
		int randomNumber = (int) (Math.random() * 3); // generate a random number between 0 and 5

		try {
			switch (randomNumber) {
			case 0:
				WebElement Renesa = driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Renesa\"]")); // Add 1st
				// Device
				Renesa.click();
				System.out.println("Renesa Selected");
				Method.captureScreenshot(driver);
				break;
			case 1:
				WebElement StudioPlus = driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Studio+\"]"));
				StudioPlus.click();
				System.out.println("Studio+ Selected");
				Method.captureScreenshot(driver);
				break;
			case 2:
				WebElement RenesaPlus = driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Renesa+\"]"));
				RenesaPlus.click();
				System.out.println("Renesa+ Selected");
				Method.captureScreenshot(driver);
				break;

			}
		} catch (Exception exp) {
			System.out.println(exp.getMessage());
			exp.printStackTrace();
		}
		driver.findElement(By.xpath("//XCUIElementTypeButton[@name=\"Continue\"]")).click();

	}

	private static void Error(IOSDriver driver){
		List<WebElement> errorMsg = driver.findElements(By.className("XCUIElementTypeStaticText"));
		List<WebElement> BLEError = errorMsg.stream().filter(element -> element.getAttribute("name")!=null).collect(Collectors.toList());
		for (WebElement e: BLEError)
		{
			if(e.getAttribute("name").startsWith("Could not reach"))
			{
				System.out.println("Error connecting the device");
				Tap.withCoordinates(driver,50,50);
				Tap.withCoordinates(driver,35,125);
			}
		}
	}

}