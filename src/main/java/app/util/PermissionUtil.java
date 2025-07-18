package app.util;

import app.Resources.HomeElements;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class PermissionUtil {

    public static void allow(AndroidDriver driver){
        HomeElements he = new HomeElements();
        WebElement permission = null;  // Check Direct allow button, when device is already present in the family
        WebElement AddButton = null; // Check direct Add button"+" , When empty Family

        try {
            permission = driver.findElement(By.id("com.android.permissioncontroller:id/permission_icon"));
        } catch (Exception ignored) {
        }
        try {
            AddButton = driver.findElement(By.xpath(he.addButtonId));
        } catch (Exception ignored) {}
        if(permission != null){//Direct allow button available
            WebElement allowButton = driver.findElement(By.id("com.android.permissioncontroller:id/permission_allow_button"));
            allowButton.click();
            System.out.println("Permissions");
            driver.findElement(By.id("com.android.permissioncontroller:id/permission_allow_foreground_only_button")).click();
            ActionsUtil.sleep(1000);
            driver.findElement(By.id("com.android.permissioncontroller:id/permission_allow_button")).click();
            System.out.println("All Permissions Granted");
            alexaPopUp(driver);
        }
        else if(AddButton != null){//Direct add button available
            System.out.println("No Device Present");
            ActionsUtil.Tap.withCoordinates(driver, 540, 1900);// click on the Add button, No element id/xpath available

            try {//checking the permissions, in case of logout and login again
                permission = driver.findElement(By.id("com.android.permissioncontroller:id/permission_icon"));
            }catch (Exception ignored){}
            if(permission!=null) {
                WebElement allowButton = driver.findElement(By.id("com.android.permissioncontroller:id/permission_allow_button"));
                allowButton.click();
                System.out.println("Permissions");
                driver.findElement(By.id("com.android.permissioncontroller:id/permission_allow_foreground_only_button")).click();
                ActionsUtil.sleep(1000);
                driver.findElement(By.id("com.android.permissioncontroller:id/permission_allow_button")).click();
                System.out.println("All Permissions Granted");
                alexaPopUp(driver);
            }
            else
            {
                driver.navigate().back();
            }
        }
    }

    public static void allowForBrowserStack(AndroidDriver driver){
        HomeElements he = new HomeElements();
        WebElement permission = null;  // Check Direct allow button, when device is already present in the family
        WebElement AddButton = null; // Check direct Add button"+" , When empty Family
        try {
            permission = driver.findElement(By.id("com.android.permissioncontroller:id/permission_icon"));
        } catch (Exception ignored) {
        }
        try {
            AddButton = driver.findElement(By.xpath(he.addYourFirstSmartDeviceId));
        } catch (Exception ignored) {
        }
        if (permission != null)//Direct allow button available
        {
            System.out.println("Permissions");
            WebElement allowButton = driver.findElement(By.id("com.android.permissioncontroller:id/permission_allow_button"));
            allowButton.click();
            ActionsUtil.sleep(1000);
            System.out.println("All Permissions Granted");
            alexaPopUp(driver);
        }
        else if(AddButton != null)//Direct add button available
        {
            System.out.println("No Device Present");
            ActionsUtil.Tap.withCoordinates(driver, 540, 1900);// click on the Add button, No element id/xpath available
            try {//checking the permissions, in case of logout and login again
                permission = driver.findElement(By.id("com.android.permissioncontroller:id/permission_icon"));
            }catch (Exception ignored){}
            if(permission!=null) {
                System.out.println("Permissions");
                driver.findElement(By.id("com.android.permissioncontroller:id/permission_allow_foreground_only_button")).click();
                ActionsUtil.sleep(1000);
                System.out.println("All Permissions Granted");
                alexaPopUp(driver);
            }
            else
            {
                driver.navigate().back();
            }
        }
    }

    private static void alexaPopUp(AndroidDriver driver){
        HomeElements he = new HomeElements();
        WebElement alexaPopup = null;
        try {
            alexaPopup = driver.findElement(By.xpath(he.alexaPopupId));
        }catch (Exception ignored){}
        if(alexaPopup!=null){
            driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]")).click();
        }
    }
}
