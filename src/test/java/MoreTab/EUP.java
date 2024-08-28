package MoreTab;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import AtombergTest.Method;
import io.appium.java_client.ios.IOSDriver;

import java.util.List;

public class EUP {
	public static void UnitPrice(IOSDriver driver) {
		WebElement UnitPrice = driver.findElement(By.xpath("//XCUIElementTypeOther[@name=\"Electricity unit price\"]"));
		UnitPrice.click();
		Method.captureScreenshot(driver);
		System.out.println("Tap on Unit Price");
		WebElement ChangeAmount = driver.findElement(By.xpath("//XCUIElementTypeTextField[@index=\"2\"]"));
		ChangeAmount.click();
		Method.captureScreenshot(driver);
	}
	public static void Currency(IOSDriver driver) {
		WebElement ChangeCurrency = driver.findElement(By.xpath("//XCUIElementTypeStaticText[@index=\"4\"]"));
		ChangeCurrency.click();
		Method.captureScreenshot(driver);
		List<WebElement> currencies = driver.findElements(By.className("XCUIElementTypeStaticText"));
		currencies.get(0).click();
		driver.findElement(By.xpath("//XCUIElementTypeButton[@name=\"Update\"]")).click();
	}
}
