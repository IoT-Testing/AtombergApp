package Tests;

import app.AppInitializer;
import app.Login.Apple;
import app.Login.Email;
import app.Login.Google;
import app.MoreTab.Manage;
import app.util.ActionsUtil;
import app.util.PermissionUtil;
import com.aventstack.extentreports.Status;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest{
    public AndroidDriver driver = getDriver();

    @Test(priority = 1)
    void testCase1() {
        try {
            reporter.startTest("Correct Credentials", deviceSlot);
            System.out.println("Manage Family Test start");
            driver = getDriver();
            ActionsUtil.SSleep(2);
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
                manage.logout();
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
        } catch (Throwable e) {
            reporter.log(Status.FAIL, "App Open failed: " + e.getMessage());
        } finally {
            System.out.println("Test end");
            reporter.endTest();
        }
    }

    @Test(priority = 2)
    void testCase2() {
        try {
            reporter.startTest("Incorrect Email", deviceSlot);
            System.out.println("Test start");
            driver = getDriver();
            ActionsUtil.SSleep(2);

            driver.activateApp("com.atomberg.app");
            AppInitializer appInitializer = new AppInitializer();
            appInitializer.setDriver(driver);
            appInitializer.checkMainScreen();
            Email login = new Email(driver);
            if(appInitializer.checkMainScreen()){
                try{
                    login.email("hiwitaw422wuzak.com", "Atomberg@1234");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            driver.terminateApp("com.atomberg.app");
            ActionsUtil.SSleep(10);
            driver.activateApp("com.atomberg.app");
//            Assert.assertTrue();
        } catch (Exception e) {
            reporter.log(Status.FAIL, "App Open failed: " + e.getMessage());
        } finally {
            System.out.println("Test end");
            reporter.endTest();
        }
    }

    @Test(priority = 3)
    void testCase3() {
        try {
            reporter.startTest("Incorrect Password", deviceSlot);
            System.out.println("Test start");
            driver = getDriver();
            ActionsUtil.SSleep(2);

            driver.activateApp("com.atomberg.app");
            AppInitializer appInitializer = new AppInitializer();
            appInitializer.setDriver(driver);
            appInitializer.checkMainScreen();
            Email login = new Email(driver);
            if(appInitializer.checkMainScreen()){
                try{
                    login.email("hiwitaw422@wuzak.com", "Atomberg@12345");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (Exception e) {
            reporter.log(Status.FAIL, "App Open failed: " + e.getMessage());
        } finally {
            System.out.println("Test end");
            reporter.endTest();
        }
    }

    @Test(priority = 4)
    void testCase4() {
        try {
            reporter.startTest("App Kill after login", deviceSlot);
            System.out.println("Test start");
            driver = getDriver();
            ActionsUtil.SSleep(2);

            driver.activateApp("com.atomberg.app");
            AppInitializer appInitializer = new AppInitializer();
            appInitializer.setDriver(driver);
            appInitializer.checkMainScreen();
            Email login = new Email(driver);
            if(appInitializer.checkMainScreen()){
                try{
                    login.email("hiwitaw422@wuzak.com", "Atomberg@1234");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            driver.terminateApp("com.atomberg.app");
            ActionsUtil.SSleep(5);
            driver.activateApp("com.atomberg.app");

        } catch (Exception e) {
            reporter.log(Status.FAIL, "App Open failed: " + e.getMessage());
        } finally {
            System.out.println("Test end");
            reporter.endTest();
        }
    }

    @Test(priority = 5)
    void testcase5(){
        try {
            reporter.startTest("Apple Login", deviceSlot);
            System.out.println("Apple Login test start");
            driver = getDriver();
            ActionsUtil.SSleep(2);

            driver.activateApp("com.atomberg.app");
            AppInitializer appInitializer = new AppInitializer();
            appInitializer.setDriver(driver);
            appInitializer.checkMainScreen();
            Apple.Login(driver);
            Manage manage = new Manage(driver);
            manage.logout();
            driver.terminateApp("com.atomberg.app");
//            Assert.assertTrue();
        } catch (Exception e) {
            reporter.log(Status.FAIL, "App Open failed: " + e.getMessage());
        } finally {
            System.out.println("Apple Login test end");
            reporter.endTest();
        }
    }

    @Test(priority = 6)
    void testcase6(){
        try {
            reporter.startTest("Google Login", deviceSlot);
            System.out.println("Google Login test start");
            driver = getDriver();
            ActionsUtil.SSleep(2);

            driver.activateApp("com.atomberg.app");
            AppInitializer appInitializer = new AppInitializer();
            appInitializer.setDriver(driver);
            appInitializer.checkMainScreen();
            Google.Login(driver);
            Manage manage = new Manage(driver);
            manage.logout();
            driver.terminateApp("com.atomberg.app");
//            Assert.assertTrue();
        } catch (Exception e) {
            reporter.log(Status.FAIL, "failed: " + e.getMessage());
        } finally {
            System.out.println("Google Login test end");
            reporter.endTest();
        }
    }
}
