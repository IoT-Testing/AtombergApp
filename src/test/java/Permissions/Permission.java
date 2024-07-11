package Permissions;
//Add First Device

import org.openqa.selenium.By; //Selenium Dependencies
import org.openqa.selenium.WebElement;

import Actions.Tap;
import AtombergTest.Method;
import io.appium.java_client.AppiumDriver;

public class Permission {
	public static void Allow(AppiumDriver driver) // Main
	{

		WebElement AllowButton = null;
		try {
			AllowButton = driver.findElement(By.id("com.android.permissioncontroller:id/permission_message"));
		} catch (Exception exp) {
		}
		if (AllowButton != null) {

			allowPermissionButton(driver, "com.android.permissioncontroller:id/permission_allow_button");
			Method.captureScreenshot(driver);
			allowPermissionButton(driver,"com.android.permissioncontroller:id/permission_allow_foreground_only_button");
			Method.captureScreenshot(driver);
			allowPermissionButton(driver,"com.android.permissioncontroller:id/permission_allow_button");
			System.out.println("On Home Screen");
			Method.captureScreenshot(driver);
		} else {
			System.out.println("No Device Present");
			Tap.withCoordinates(driver, 540, 1940);
			// Click on allow permissions
			allowPermissionButton(driver, "com.android.permissioncontroller:id/permission_allow_button");
			Method.captureScreenshot(driver);
			allowPermissionButton(driver,"com.android.permissioncontroller:id/permission_allow_foreground_only_button");
			Method.captureScreenshot(driver);
			allowPermissionButton(driver,"com.android.permissioncontroller:id/permission_allow_button");
			System.out.println("On Home Screen");
			Method.captureScreenshot(driver);
		}
	}

	private static void allowPermissionButton(AppiumDriver driver, String resourceId) {
		WebElement permissionButton = driver
				.findElement(By.xpath("//android.widget.Button[@resource-id=\"" + resourceId + "\"]\r\n" + ""));
		permissionButton.click();
	}


}