package Tests;

import app.AppInitializer;
import app.Login.Email;
import app.MoreTab.Manage;
import app.ScreenCheck.ScreenCheck;
import app.util.ActionsUtil;
import app.util.ScreenRecording;
import com.aventstack.extentreports.Status;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.appmanagement.ApplicationState;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;

import static io.appium.java_client.appmanagement.ApplicationState.RUNNING_IN_FOREGROUND;


public class DeviceProvTest extends BaseTest{

    public AndroidDriver driver;

    @Test(priority = 1)
    void testOpenApp() {
        try {
            reporter.startTest("Open App", deviceSlot);
            System.out.println("OpenApp test start");
            driver = getDriver();
            ActionsUtil.SSleep(2);
            driver.activateApp("com.atomberg.app");
            ActionsUtil.SSleep(5);
            AppInitializer appInitializer = new AppInitializer();
            appInitializer.setDriver(driver);
            appInitializer.checkMainScreen();
            System.out.println(appInitializer.checkMainScreen());
            if(appInitializer.checkMainScreen()){
                Email login = new Email(driver);
                try{
                    login.email("iot.alpha@protonmail.com", "Atomberg@123");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (Exception e) {
            reporter.log(Status.FAIL, "App Open failed: " + e.getMessage());
        } finally {
            System.out.println("OpenApp test end");
            reporter.endTest();
        }
    }

    @Test(priority = 2)
    void testDeviceProvisioning(){
        try {
            reporter.startTest("Open App", deviceSlot);
            System.out.println("OpenApp test start");
            driver = getDriver();
            ScreenCheck screen = new ScreenCheck(driver);
            screen.homeScreen();

        } catch (Exception e) {
            reporter.log(Status.FAIL, "App Open failed: " + e.getMessage());
        } finally {
            System.out.println("OpenApp test end");
            reporter.endTest();
        }
    }

    @Test(priority = 4)
    void testFanControl() {
        try {
            reporter.startTest("Fan Control", deviceSlot);
            System.out.println("Fan Control test start");
            FanManagement fan = new FanManagement(driver);
            fan.checkFan();
        } catch (Exception e) {
            reporter.log(Status.FAIL, "Fan Control failed: " + e.getMessage());
        } finally {
            System.out.println("Fan Control test end");
            if (reporter.getCurrentStatus() == Status.FAIL) afterTestFailure();
            reporter.endTest();
        }
    }

    @Test(priority = 9)
    void testLogout() {
        try {
            reporter.startTest("Logout", deviceSlot);
            System.out.println("Logout test start");
            Manage manage = new Manage(driver);
            manage.logout();
            ActionsUtil.SSleep(5);
        } catch (Exception e) {
            reporter.log(Status.FAIL, "Logout failed: " + e.getMessage());
        } finally {
            System.out.println("Logout test end");
            if (reporter.getCurrentStatus() == Status.FAIL) afterTestFailure();
            reporter.endTest();
        }
    }

    @Test(priority = 10, dependsOnMethods = "testLogout")
    void testDriverClose() {
        ScreenRecording recording = new ScreenRecording(driver);
        recording.stop();
        server.stopServer();
    }

    @AfterSuite
    public void tearDown() {
        if (reporter != null) {
            reporter.endTest();
        }
    }

    void afterTestFailure() {
        ApplicationState state = driver.queryAppState("com.atomberg.app");
        if (state.equals(RUNNING_IN_FOREGROUND)) {
            WebElement homeScreen = null;
            while (homeScreen == null) {
                try {
                    homeScreen = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"More\nTab 3 of 3\"]"));
                } catch (Exception ignored) {
                }
                if (homeScreen == null) {
                    System.out.println("Back");
                    driver.navigate().back();
                }
            }
        } else {
            driver.activateApp("com.atomberg.app");
        }
    }
}

