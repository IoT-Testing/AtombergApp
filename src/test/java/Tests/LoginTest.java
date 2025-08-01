package Tests;

import app.AppInitializer;
import app.Login.Email;
import app.MoreTab.Manage;
import app.util.ActionsUtil;
import app.util.PermissionUtil;
import com.aventstack.extentreports.Status;
import io.appium.java_client.android.AndroidDriver;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest{
    public AndroidDriver driver = getDriver();

    @Test(priority = 1)
    void testCase1() {
        try {
            reporter.startTest("Correct Credentials", deviceSlot);
            System.out.println("OpenApp test start");
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
                    login.email("hiwitaw422@wuzak.com", "Atomberg@1234");
                }
                ActionsUtil.sleep(5000);
                PermissionUtil.allow(driver);
                Manage manage = new Manage(driver);
                manage.logout();
                driver.terminateApp("com.atomberg.app");
            }
        } catch (Exception e) {
            reporter.log(Status.FAIL, "App Open failed: " + e.getMessage());
        } finally {
            System.out.println("OpenApp test end");
            reporter.endTest();
        }
    }
    @Test(priority = 2)
    void testCase2() {
        try {
            reporter.startTest("Incorrect Email", deviceSlot);
            System.out.println("OpenApp test start");
            driver = getDriver();
            ActionsUtil.SSleep(2);

            driver.activateApp("com.atomberg.app");
            AppInitializer appInitializer = new AppInitializer();
            appInitializer.setDriver(driver);
            appInitializer.checkMainScreen();
            Email login = new Email(driver);
            if(appInitializer.checkMainScreen()){
                login.email("hiwitaw422wuzak.com", "password");
            }
            driver.terminateApp("com.atomberg.app");
        } catch (Exception e) {
            reporter.log(Status.FAIL, "App Open failed: " + e.getMessage());
        } finally {
            System.out.println("OpenApp test end");
            reporter.endTest();
        }
    }

    @Test(priority = 3)
    void testCase3() {
        try {
            reporter.startTest("Incorrect Password", deviceSlot);
            System.out.println("OpenApp test start");
            driver = getDriver();
            ActionsUtil.SSleep(2);

            driver.activateApp("com.atomberg.app");
            AppInitializer appInitializer = new AppInitializer();
            appInitializer.setDriver(driver);
            appInitializer.checkMainScreen();
            Email login = new Email(driver);
            if(appInitializer.checkMainScreen()){
                login.email("hiwitaw422@wuzak.com", "Atomberg@12345");
            }
            driver.terminateApp("com.atomberg.app");

        } catch (Exception e) {
            reporter.log(Status.FAIL, "App Open failed: " + e.getMessage());
        } finally {
            System.out.println("OpenApp test end");
            reporter.endTest();
        }
    }

    @Test(priority = 4)
    void testCase4() {
        try {
            reporter.startTest("App Kill after login", deviceSlot);
            System.out.println("OpenApp test start");
            driver = getDriver();
            ActionsUtil.SSleep(2);

            driver.activateApp("com.atomberg.app");
            AppInitializer appInitializer = new AppInitializer();
            appInitializer.setDriver(driver);
            appInitializer.checkMainScreen();
            Email login = new Email(driver);
            if(appInitializer.checkMainScreen()){
                login.email("hiwitaw422@wuzak.com", "Atomberg@1234");
            }
            driver.terminateApp("com.atomberg.app");
            ActionsUtil.SSleep(5);
            driver.activateApp("com.atomberg.app");

        } catch (Exception e) {
            reporter.log(Status.FAIL, "App Open failed: " + e.getMessage());
        } finally {
            System.out.println("OpenApp test end");
            reporter.endTest();
        }
    }
}
