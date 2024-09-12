package Permissions;
//Add First Device

import org.awaitility.Awaitility;
import org.openqa.selenium.By; //Selenium Dependencies
import org.openqa.selenium.WebElement;
import Actions.Tap;
import io.appium.java_client.AppiumDriver;

import java.util.concurrent.TimeUnit;

public class Permission {
	public static WebElement AllowButton;
	public static void Allow(AppiumDriver driver){
		AllowButton = null;
		WebElement AddButton = null;
		try {
			AllowButton = driver.findElement(By.id("com.android.permissioncontroller:id/permission_allow_button"));
		} catch (Exception exp) {
		}
		try {
			AddButton = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Add your first smart device\"]"));
		} catch (Exception exp) {
		}
		if (AllowButton != null)
		{
			AllowButton.click();
			System.out.println("Permissions");
			driver.findElement(By.id("com.android.permissioncontroller:id/permission_allow_foreground_only_button")).click();
			sleep(1000);
			driver.findElement(By.id("com.android.permissioncontroller:id/permission_allow_button")).click();
			System.out.println("All Permissions Granted");
		}
		else if(AddButton != null)
		{
			System.out.println("No Device Present");
			Tap.withCoordinates(driver, 540, 1900);

			try {
				AllowButton = driver.findElement(By.id("com.android.permissioncontroller:id/permission_allow_button"));
			}catch (Exception e){}
			if(AllowButton!=null) {
				driver.findElement(By.id("com.android.permissioncontroller:id/permission_allow_button")).click();
				System.out.println("Permissions");
				driver.findElement(By.id("com.android.permissioncontroller:id/permission_allow_foreground_only_button")).click();
				sleep(1000);
				driver.findElement(By.id("com.android.permissioncontroller:id/permission_allow_button")).click();
				System.out.println("All Permissions Granted");
			}
			else
			{
				driver.navigate().back();
			}
		}

	}
	private static void sleep(long millis) {
		Awaitility.await().atMost(millis, TimeUnit.MILLISECONDS);
	}

}