package browserstack;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class PermissionUtil {
    public static void allow(AndroidDriver driver){
//        WebElement AllowButton = null;  // Check Direct allow button, when device is already present in the family
//        WebElement AddButton = null; // Check direct Add button"+" , When empty Family
//        try {
//            AllowButton = driver.findElement(By.id("com.android.permissioncontroller:id/permission_allow_button"));
//        } catch (Exception ignored) {
//        }
//        try {
//            AddButton = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Add your first smart device\"]"));
//        } catch (Exception ignored) {
//        }
//        if (AllowButton != null)//Direct allow button available
//        {
//            AllowButton.click();
            System.out.println("Permissions");
            driver.findElement(By.id("com.android.permissioncontroller:id/permission_allow_foreground_only_button")).click();
//            driver.findElement(By.id("com.android.permissioncontroller:id/permission_allow_button")).click();
            System.out.println("All Permissions Granted");
//        }
//        else if(AddButton != null)//Direct add button available
//        {
//            System.out.println("No Device Present");
////            ActionsUtil.Tap.withCoordinates(driver, 540, 1900);// click on the Add button, No element id/xpath available
//
//            try {//checking the permissions, in case of logout and login again
//                AllowButton = driver.findElement(By.id("com.android.permissioncontroller:id/permission_allow_button"));
//            }catch (Exception ignored){}
//            if(AllowButton!=null) {
//                driver.findElement(By.id("com.android.permissioncontroller:id/permission_allow_button")).click();
//                System.out.println("Permissions");
//                driver.findElement(By.id("com.android.permissioncontroller:id/permission_allow_foreground_only_button")).click();
////                ActionsUtil.sleep(1000);
//                driver.findElement(By.id("com.android.permissioncontroller:id/permission_allow_button")).click();
//                System.out.println("All Permissions Granted");
//            }
//            else
//            {
//                driver.navigate().back();
//            }
//        }
    }
}
