package Tests;

import app.AppInitializer;
import app.Login.Email;
import app.MoreTab.Manage;
import app.util.ActionsUtil;
import app.util.PermissionUtil;
import com.aventstack.extentreports.Status;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

public class ManageFamilyTest extends BaseTest{
    public AndroidDriver driver = getDriver();


    @Test(priority = 1)
    void testManageFamily() {
        try {
            reporter.startTest("Open App", deviceSlot);
            System.out.println("OpenApp test start");
            driver = getDriver();
            driver.activateApp("com.atomberg.app");
            ActionsUtil.SSleep(5);
            AppInitializer appInitializer = new AppInitializer();
            appInitializer.setDriver(driver);
            appInitializer.checkMainScreen();
            if(appInitializer.checkMainScreen()){
                Email login = new Email(driver);
                if(appInitializer.checkMainScreen()){
                    try{
                        login.email("hiwitaw422@wuzak.com", "Atomberg@1234");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                ActionsUtil.sleep(5000);
                PermissionUtil.allow(driver);
                Manage manage = new Manage(driver);
                manage.family();
                driver.terminateApp("com.atomberg.app");
            }
            else{
                WebElement home = null;
                try{
                    home = driver.findElement(By.xpath("//android.view.View[@content-desc=\"My Home\"]"));
                }catch (Exception ignored){}
                if (home != null){
                    Manage manage = new Manage(driver);
                    manage.logout();
                    System.out.println("Already Logged in, Logging In");
                    Email login = new Email(driver);
                    login.email("hiwitaw422@wuzak.com", "Atomberg@1234");
                }
            }
        } catch (Exception e) {
            reporter.log(Status.FAIL, "App Open failed: " + e.getMessage());
        } finally {
            System.out.println("OpenApp test end");
            reporter.endTest();
        }
    }
}
