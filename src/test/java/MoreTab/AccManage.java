package MoreTab;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import AtombergTest.Method;
import io.appium.java_client.AppiumDriver;

public class AccManage {
public static AppiumDriver driver;
public static void ChangePassword(AppiumDriver driver) {
	WebElement ChangePassword = driver
			.findElement(By.xpath("//android.view.View[@content-desc=\"Change password\"]"));
	ChangePassword.click();
	Method.captureScreenshot(driver);
	System.out.println("Tap on Change Password");
	
	driver.navigate().back();
}
public static void DeleteAccount(AppiumDriver driver) {
	WebElement DeleteAccount = driver
			.findElement(By.xpath("//android.view.View[@content-desc=\"Delete account\"]"));
	DeleteAccount.click();
	Method.captureScreenshot(driver);
	System.out.println("Tap on Delete Account");
	
	driver.navigate().back();
}
public static void DeveloperOptions(AppiumDriver driver) {
	WebElement DevOps = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Developer options\"]"));
	DevOps.click();
	Method.captureScreenshot(driver);
	System.out.println("Tap on Developer Options");
	
	driver.navigate().back();
	
}
public static void Logout(AppiumDriver driver) {
	WebElement Logout = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Logout\"]"));
	Logout.click();
	Method.captureScreenshot(driver);
	System.out.println("Tap on Logout");
	
	driver.navigate().back();
}
}
