package Devices;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import AtombergTest.Method;
import io.appium.java_client.AppiumDriver;

public class Select {
	public static void Fan(AppiumDriver driver) {
		WebElement ModelSelect = null;
		try {
			ModelSelect = driver
					.findElement(By.xpath("//android.view.View[@content-desc=\"Pick the fan model you're having\"]"));
			System.out.println("Six LED fan");
		} catch (Exception e) {
		}
		if (ModelSelect != null) {
			SixLED(driver);
			Fans.SixLEDcolorSelect(driver);
		} else {
			WebElement element = null;
			try {
				element = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Select your fan color\"]"));
				System.out.println("Select your fan color");
			} catch (Exception exp) {
			}
			if (element != null) {
				WebElement Aris = null;
				WebElement Jaguar = null;
				try {
					Aris = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Dark Teakwood\"]"));
				} catch (Exception e) {
				}
				try {
					Jaguar = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Regent Gray\"]"));
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

	public static void SixLED(AppiumDriver driver) {
		int randomNumber = (int) (Math.random() * 3); // generate a random number between 0 and 5

		try {
			switch (randomNumber) {
			case 0:
				WebElement Renesa = driver
						.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Renesa\"]")); // Add 1st
																										// Device
				Renesa.click();
				System.out.println("Renesa Selected");
				Method.captureScreenshot(driver);
				break;
			case 1:
				WebElement StudioPlus = driver
						.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Studio+\"]"));
				StudioPlus.click();
				System.out.println("Studio+ Selected");
				Method.captureScreenshot(driver);
				break;
			case 2:
				WebElement RenesaPlus = driver
						.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Renesa+\"]"));
				RenesaPlus.click();
				System.out.println("Renesa+ Selected");
				Method.captureScreenshot(driver);
				break;

			}
		} catch (Exception exp) {
			System.out.println(exp.getCause());
			System.out.println(exp.getMessage());
			exp.printStackTrace();
		}
		driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Continue\"]")).click();

	}

}