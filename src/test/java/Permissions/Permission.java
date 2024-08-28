package Permissions;
//Add First Device

import org.awaitility.Awaitility;
import org.openqa.selenium.By; //Selenium Dependencies
import org.openqa.selenium.WebElement;

import Actions.Tap;
import AtombergTest.Method;
import io.appium.java_client.ios.IOSDriver;

import java.util.concurrent.TimeUnit;

public class Permission {
	public static void Allow(IOSDriver driver){
		WebElement AllowButton = null;
		try {
			AllowButton = driver.findElement(By.id("com.android.permissioncontroller:id/permission_allow_button"));
		} catch (Exception exp) {
		}
		if (AllowButton != null)
		{
			AllowButton.click();
		}
		else
		{
			System.out.println("No Device Present");
			Tap.withCoordinates(driver, 540, 1900);
			driver.findElement(By.id("com.android.permissioncontroller:id/permission_allow_button")).click();
		}
		System.out.println("Permissions");
		driver.findElement(By.id("com.android.permissioncontroller:id/permission_allow_foreground_only_button")).click();
		sleep(1000);
		driver.findElement(By.id("com.android.permissioncontroller:id/permission_allow_button")).click();
		System.out.println("All Permissions Granted");
	}
	private static void sleep(long millis) {
		Awaitility.await().atMost(millis, TimeUnit.MILLISECONDS);
	}

}