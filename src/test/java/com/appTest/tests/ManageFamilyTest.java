package com.appTest.tests;

import app.AppInitializer;
import app.Login.Email;
import app.MoreTab.Manage;
import app.util.ActionsUtil;
import app.util.PermissionUtil;
import com.aventstack.extentreports.Status;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * ManageFamilyTest - Tests family management functionality.
 */
public class ManageFamilyTest extends BaseTest {
    public AndroidDriver driver;

    @BeforeClass
    public void setUp() {
        driver = getDriver();
        System.out.println("ManageFamilyTest setup completed");
    }

    @Test(priority = 1, description = "Manage family members")
    void testManageFamily() {
        try {
            reporter.startTest("Manage Family", deviceSlot);
            System.out.println("Manage Family test start");
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
            reporter.log(Status.PASS, "Family managed successfully");
            }
            else{
                WebElement home = null;
                try{
                    home = driver.findElement(By.xpath("//android.view.View[@content-desc=\"My Home\"]"));
                }catch (Exception ignored){}
                if (home != null){
                    Manage manage = new Manage(driver);
                    manage.logout();
                    System.out.println("Already Logged in, Logging back in");
                    Email login = new Email(driver);
                    login.email("hiwitaw422@wuzak.com", "Atomberg@1234");
                    reporter.log(Status.PASS, "Family management completed");
                }
            }
        } catch (Exception e) {
            reporter.log(Status.FAIL, "Family management failed: " + e.getMessage());
            throw e;
        } finally {
            System.out.println("Manage Family test end");
            reporter.endTest();
        }
    }
}
