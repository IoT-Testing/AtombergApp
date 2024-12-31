package Permissions;
//Add First Device

import org.awaitility.Awaitility;
import org.openqa.selenium.By; //Selenium Dependencies
import org.openqa.selenium.WebElement;
import Actions.Tap;
import io.appium.java_client.android.AndroidDriver;

import java.util.concurrent.TimeUnit;

public class Permission {

	public static void Allow(AndroidDriver driver){
		WebElement AllowButton = null;  // Check Direct allow button, when device is already present in the family
		WebElement AddButton = null; // Check direct Add button"+" , When empty Family
		try {
			AllowButton = driver.findElement(By.id("com.android.permissioncontroller:id/permission_allow_button"));
		} catch (Exception exp) {
		}
		try {
			AddButton = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Add your first smart device\"]"));
		} catch (Exception exp) {
		}
		if (AllowButton != null)//Direct allow button available
		{
			AllowButton.click();
			System.out.println("Permissions");
			driver.findElement(By.id("com.android.permissioncontroller:id/permission_allow_foreground_only_button")).click();
			sleep(1000);
			driver.findElement(By.id("com.android.permissioncontroller:id/permission_allow_button")).click();
			System.out.println("All Permissions Granted");
		}
		else if(AddButton != null)//Direct add button available
		{
			System.out.println("No Device Present");
			Tap.withCoordinates(driver, 540, 1900);// click on the Add button, No element id/xpath available

			try {//checking the permissions, in case of logout and login again
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