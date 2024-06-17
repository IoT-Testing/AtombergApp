package MoreTab;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import Actions.Scroll;
import Actions.Tap;
import AtombergTest.Method;
import io.appium.java_client.AppiumDriver;

public class Edit {
	public static AppiumDriver driver;
	public static void Profile(AppiumDriver driver)
	{
	WebElement ChangeAvatar = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Change avatar\"]"));
	 ChangeAvatar.click();
	 Method.captureScreenshot(driver);
	 System.out.println("Tap On Change Avatar"); 
	Scroll.Down(driver); 
	 for(int i=1; i<25; i++) {
	 WebElement Avatar1 = driver.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[2]/android.view.View/android.view.View/android.widget.ImageView["
	 +i+"]"));
	 Avatar1.click();
	 Method.captureScreenshot(driver); 
	 }
	 driver.navigate().back(); 
	 
	 
	  Tap.withPercentage(driver, 0.25, 0.275);
	Method.captureScreenshot(driver);
	

	WebElement EditCountryCode = driver
			.findElement(By.xpath("//android.view.View[@content-desc=\"+91\"]/android.widget.EditText"));
	EditCountryCode.click();

	
	Method.captureScreenshot(driver);
	driver.navigate().back();
	sleep(250);

	WebElement EditNumber = driver.findElement(By.xpath("//android.view.View[@content-desc=\"+91\"]"));
	EditNumber.click();
	Method.captureScreenshot(driver);
	

	driver.navigate().back();
	sleep(250);
	driver.navigate().back();
}
	private static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
