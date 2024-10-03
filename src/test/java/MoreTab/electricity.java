package MoreTab;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import AtombergTest.Method;
import io.appium.java_client.AppiumDriver;

public class electricity {
	public static void UnitPrice(AppiumDriver driver) {
		WebElement UnitPrice = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Electricity unit price\"]"));
		UnitPrice.click();
		Method.captureScreenshot(driver);
		System.out.println("Tap on Unit Price");

		WebElement ChangeAmount = driver.findElement(By.xpath("//android.widget.EditText[@text=\"7.0\"]"));
		ChangeAmount.click();
		Method.captureScreenshot(driver);
	}
	public static void Currency(AppiumDriver driver) {
		WebElement ChangeCurrency = driver.findElement(By.xpath("//android.view.View[@content-desc=\"INR\"]"));
		ChangeCurrency.click();
		Method.captureScreenshot(driver);
		driver.navigate().back();
	}
}
