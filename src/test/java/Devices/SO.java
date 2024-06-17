package Devices;

import Actions.NumberPad;
import AtombergTest.Method;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import Actions.Scroll;

import io.appium.java_client.AppiumDriver;

public class SO {//Search Online Fan
	public static void Fan(AppiumDriver driver) {
		/**
		 * @author Rohit Bhagat
		 */
		try {
			sleep(2500);
			WebElement AddButton = null;
			try {
				AddButton = driver.findElement(By.xpath(
						"//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[3]/android.widget.ImageView"));
			} catch (Exception exp) {
				exp.getCause();
			}
			if (AddButton != null) {
			
			CDO(driver);
			}
			else
			{
				Add2.Fan(driver);
				CDO(driver);
			}
		} catch (Exception exp) {
			System.out.println(exp.getCause());
			System.out.println(exp.getMessage());
			exp.printStackTrace();
		}
	}

	private static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("......");
	}

	private static void CDO(AppiumDriver driver)
	{
		WebElement Fans = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Fans\"]"));
		Fans.click();
		sleep(1500);
		WebElement FO = null;
		try {
			FO = driver.findElement(By.xpath("(//android.widget.Button/android.widget.ImageView)"));
		}catch(Exception e) {}	

		if (FO != null)
		{
			System.out.println("Fan Online");
			List<WebElement> Device = driver.findElements(By.xpath("(//android.widget.Button/android.widget.ImageView[1])"));
			System.out.println(Device.size());
			for(WebElement element : Device)
			{
				System.out.println(element);
				element.click();
				Method.FanControl(driver);
				driver.navigate().back();			
 			}
			if(Device.size()>=4)
			{
				Scroll.Up(driver);	
			}
		}
		else
		{
			System.out.println("No Fan Offline");
		}
	}

	public static void Lock(AppiumDriver driver)
	{
		driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Locks\"]")).click();
		WebElement LO = null;
		try {
			LO = driver.findElement(By.xpath("(//android.widget.Button/android.widget.Button)"));
		} catch (Exception e) {
		}

		if (LO != null) {
			System.out.println("Lock Available");
			List<WebElement> Device = driver
					.findElements(By.xpath("//android.widget.Button/android.widget.ImageView[1]"));
			System.out.println(Device.size());
			for (WebElement element : Device) {
				System.out.println(element);
				element.click();
				System.out.println("Element clicked");
				LockControl(driver);
			}
		} else {
			System.out.println("No Lock Available");

		}
	}
	public static void LockControl(AppiumDriver driver) {
		sleep(7500);
		driver.findElement(By.xpath("//android.view.View[@content-desc=\"Pull down to unlock\"]")).click();
		System.out.println("Unlocking");
		sleep(1000);
		WebElement Unlocked = null;
		try {
			Unlocked = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Unlocked\"]"));
		} catch (Exception ignored) {
		}
		if (Unlocked != null) {
			System.out.println("Successfully unlocked");

		} else {
			System.out.println("Error");
			driver.navigate().back();
			driver.navigate().back();
		}
		history(driver);
		sleep(5000);
		driver.navigate().back();
		AccessKeys(driver);
//		lockSettings(driver);
//		driver.navigate().back();
//		driver.navigate().back();

	}
	public static void history(AppiumDriver driver) {
		WebElement history = null;
		try {
			history = driver.findElement(By.xpath("//android.view.View[@content-desc=\"History\"]"));
		} catch (Exception e) {
			e.fillInStackTrace();
		}
		if (history != null) {
			history.click();
		}
	}
		public static void lockSettings(AppiumDriver driver){
			WebElement settings = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Settings\"]"));
			settings.click();
			Passcode(driver);


		}
		public static void AccessKeys(AppiumDriver driver){

		WebElement AccessKeys = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Access\nkeys\"]"));
		AccessKeys.click();
		Passcode(driver);

		}
		public static void Passcode(AppiumDriver driver)
		{
			sleep(1000);
			NumberPad.one(driver);
			sleep(250);
			NumberPad.one(driver);
			sleep(250);
			NumberPad.one(driver);
			sleep(250);
			NumberPad.two(driver);
			sleep(250);
			NumberPad.two(driver);
			sleep(250);
			NumberPad.two(driver);
			sleep(250);
			NumberPad.done(driver);
		}
}