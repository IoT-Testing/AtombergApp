package Tests;

import app.Analytics.Analytics;
import app.AppInitializer;
import app.MoreTab.Help;
import app.MoreTab.Manage;
import app.MoreTab.Play;
import app.MoreTab.Profile;
import app.STF.Connect2;
import app.ServerInitializer;
import app.util.ActionsUtil;
import app.util.ScreenRecording;
import com.aventstack.extentreports.Status;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.appmanagement.ApplicationState;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.*;
import ExtentReports.ExtentReportAT;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class AppTest {

    public AndroidDriver driver;
    public ServerInitializer server = new ServerInitializer();
    public String command;
    private String deviceSlot;
    private ExtentReportAT reporter;

    @BeforeClass
    @Parameters({"deviceSlot"})
    void setup(String deviceSlot) throws IOException, UnsupportedFlavorException {
        this.deviceSlot = deviceSlot;
        reporter = new ExtentReportAT(deviceSlot);
        reporter.startTest("Device Setup", deviceSlot);
        Connect2 connect = new Connect2();
        connect.ipAddress();
        command = connect.copiedText;
        Runtime.getRuntime().exec(command);
        server.startServer();
    }


    @Test(priority = 1)
    void testOpenApp() {
        try {
            reporter.startTest("Open App", deviceSlot);
            System.out.println("OpenApp test start");

            AppInitializer appInitializer = new AppInitializer();
            appInitializer.initializeDriverWithURL(server.service.getUrl(), command);
            driver = appInitializer.getDriver();

            ScreenRecording recording = new ScreenRecording(driver);
            recording.start();

            ActionsUtil.SSleep(2);
            driver.activateApp("com.atomberg.app");
            appInitializer.checkMainScreen();

        } catch (Exception e) {
            reporter.log(Status.FAIL, "App Open failed: " + e.getMessage());
        } finally {
            System.out.println("OpenApp test end");
            reporter.endTest();
        }
    }

    @Test(priority = 2, dependsOnMethods = "testOpenApp")
    void testManageProfile() {
        try {
            reporter.startTest("Profile Edit", deviceSlot);
            System.out.println("Profile Edit test start");
            Profile profile = new Profile(driver);
            profile.edit();
        } catch (Exception e) {
            reporter.log(Status.FAIL, "Logout failed: " + e.getMessage());
        } finally {
            System.out.println("Profile Edit test end");
            if (reporter.getCurrentStatus() == Status.FAIL) afterTestFailure();
            reporter.endTest();
        }
    }

    @Test(priority = 3, dependsOnMethods = "testManageProfile")
    void testManageFamily() {
        try {
            reporter.startTest("Family", deviceSlot);
            System.out.println("Family test start");
            Manage manage = new Manage(driver);
            manage.family();
        } catch (Exception e) {
            reporter.log(Status.FAIL, "Family Management failed: " + e.getMessage());
        } finally {
            System.out.println("Family test end");
            if (reporter.getCurrentStatus() == Status.FAIL) afterTestFailure();
            reporter.endTest();
        }
    }
    //  @Test(priority = 4,dependsOnMethods = "testManageProfile")
//    void testFanControl() {
//        try {
//            startTest("Fan Control");
//            System.out.println("Fan Control test start");
//            FanManagement fan = new FanManagement(driver);
//            fan.checkFan();
//        } catch (Exception e) {
//            getTest().log(Status.FAIL, "Fan Control failed: " + e.getMessage());
//        } finally {
//            System.out.println("Fan Control test end");
//            if (reporter.getCurrentStatus() == Status.FAIL) afterTestFailure();
//            endTest();
//        }
//    }
//
//    @Test(priority = 5,dependsOnMethods = "testManageProfile")
//    void testLockControl(){
//        try {
//            startTest("Lock Control");
//            System.out.println("Lock Control test start");
//            LockManagement lock = new LockManagement(driver);
//            lock.checkLock();
//        } catch (Exception e) {
//            getTest().log(Status.FAIL, "Lock Control failed: " + e.getMessage());
//        } finally {
//            System.out.println("Lock Control test end");
//            if (reporter.getCurrentStatus() == Status.FAIL) afterTestFailure();
//            endTest();
//        }
//    }
//
//    @Test(priority = 6,dependsOnMethods = "testManageProfile")
//    void testROControl(){
//        try {
//            startTest("RO Control");
//            System.out.println("RO Control test start");
//            ROManagement ro = new ROManagement(driver);
//            ro.checkRO();
//        } catch (Exception e) {
//            getTest().log(Status.FAIL, "RO Control failed: " + e.getMessage());
//        } finally {
//            System.out.println("RO Control test end");
//            if (reporter.getCurrentStatus() == Status.FAIL) afterTestFailure();
//            endTest();
//        }
//    }
//
    @Test(priority = 7, dependsOnMethods = "testManageFamily")
    void testAnalytics() {
        try {
            reporter.startTest("Analytics", deviceSlot);
            System.out.println("Analytics test start");
            Analytics analytics = new Analytics(driver);
            analytics.Show();
        } catch (Exception e) {
            reporter.log(Status.FAIL, "Analytics failed: " + e.getMessage());
        } finally {
            System.out.println("Analytics test end");
            if (reporter.getCurrentStatus() == Status.FAIL) afterTestFailure();
            reporter.endTest();
        }
    }

    @Test(priority = 8, dependsOnMethods = "testAnalytics")
    void testHelp() {
        try {
            reporter.startTest("Help Section", deviceSlot);
            System.out.println("Help Section test start");
            Manage manage = new Manage(driver);
            Help help = new Help(driver);
            Play play = new Play(driver);
//            help.troubleshoot();
            manage.help();
            help.newComplaint();
            help.installationRequest();
            help.serviceRequest();
            help.trackAComplaint();
            play.videos();
            help.manual();
            ActionsUtil.Scroll.Up(driver);
            help.email();
            help.call();
            driver.navigate().back();

        } catch (Exception e) {
            reporter.log(Status.FAIL, "Help Section failed: " + e.getMessage());
        } finally {
            System.out.println("Help Section test end");
            if (reporter.getCurrentStatus() == Status.FAIL) afterTestFailure();
            reporter.endTest();
        }
    }

    @Test(priority = 9, dependsOnMethods = "testHelp")
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
        if (state.equals(ApplicationState.RUNNING_IN_FOREGROUND)) {
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

