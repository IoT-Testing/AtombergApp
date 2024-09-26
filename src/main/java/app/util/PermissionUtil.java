package app.util;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class PermissionUtil {
    public static void allow(AppiumDriver driver){
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
            ActionsUtil.Tap.withCoordinates(driver, 540, 1850);
            driver.findElement(By.id("com.android.permissioncontroller:id/permission_allow_button")).click();
        }
        System.out.println("Permissions");
        driver.findElement(By.id("com.android.permissioncontroller:id/permission_allow_foreground_only_button")).click();
        driver.findElement(By.id("com.android.permissioncontroller:id/permission_allow_button")).click();
        System.out.println("All Permissions Granted");
    }
}
