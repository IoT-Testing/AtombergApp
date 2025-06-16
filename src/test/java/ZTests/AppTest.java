package ZTests;

import app.Analytics.Analytics;
import app.AppInitializer;
import app.MoreTab.Help;
import app.MoreTab.Manage;
import app.MoreTab.Play;
import app.MoreTab.Profile;
import app.STF.Connect2;
import app.STF.DeviceManager;
import app.ServerInitializer;
import app.util.ActionsUtil;
import app.util.ScreenRecording;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.appmanagement.ApplicationState;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.SkipException;
import org.testng.annotations.*;


import static ZTests.ExtentReportAT.*;

public class AppTest {
    public String command;
    public static AndroidDriver driver;
    public ServerInitializer server = new ServerInitializer();
    public static final ExtentReports extent = getReportObjects();

    @BeforeClass
    @Parameters({"deviceSlot"})

    public void setup(String deviceName) {
        WebDriver driver = new ChromeDriver(); // Or your STF WebDriver
        DeviceManager.init(driver);

        if (!DeviceManager.isDeviceAvailable(2)) {
            throw new SkipException("Insufficient devices for parallel test execution.");
        }
        // Proceed with test initialization
    }



    @Test(priority = 1)// This test is for Opening the Atomberg Home App
    void testOpenApp() {
        try {
            startTest("Open App");
            System.out.println("OpenApp test start");
            Connect2 connect = new Connect2();
            connect.ipAddress();
            command = connect.copiedText;
            Runtime.getRuntime().exec(command);
            server.startServer();
            //TODO: do not use "openApp()" if "initializeDriver()" is used.
            AppInitializer appInitializer = new AppInitializer();
            appInitializer.initializeDriverWithURL(server.service.getUrl(), command);
            driver = appInitializer.getDriver();
            ScreenRecording recording = new ScreenRecording(driver);
            recording.start();
            ActionsUtil.SSleep(2);
            //TODO: Use tapOpAppLogo() if you are using initializeDriver()
            driver.activateApp("com.atomberg.app");
            appInitializer.checkMainScreen();
        } catch (Exception e) {
            getTest().log(Status.FAIL, "App Open failed: " + e.getMessage());
        } finally {
            System.out.println("OpenApp test end");
            endTest();
        }
    }

    @Test(priority = 2, dependsOnMethods = "testOpenApp")
    void testManageProfile(){
        try {
            startTest("Profile Edit");
            System.out.println("Profile Edit test start");
            Profile profile = new Profile(driver);
            profile.edit();
        } catch (Exception e) {
            getTest().log(Status.FAIL, "Logout failed: " + e.getMessage());
        } finally {
            System.out.println("Profile Edit test end");
            if(getTest().getStatus() == Status.FAIL) afterTestFailure();
            endTest();
        }
    }

    @Test(priority = 3, dependsOnMethods = "testManageProfile")
    void testManageFamily(){
        try {
            startTest("Family");
            System.out.println("Family test start");
            Manage manage = new Manage(driver);
            manage.family();
        } catch (Exception e) {
            getTest().log(Status.FAIL, "Family Management failed: " + e.getMessage());
        } finally {
            System.out.println("Family test end");
            if(getTest().getStatus() == Status.FAIL) afterTestFailure();
            endTest();
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
//            if(getTest().getStatus() == Status.FAIL) afterTestFailure();
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
//            if(getTest().getStatus() == Status.FAIL) afterTestFailure();
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
//            if(getTest().getStatus() == Status.FAIL) afterTestFailure();
//            endTest();
//        }
//    }
//
    @Test(priority = 7,dependsOnMethods = "testManageFamily")
    void testAnalytics() {
        try {
            startTest("Analytics");
            System.out.println("Analytics test start");
            Analytics analytics = new Analytics(driver);
            analytics.Show();
        } catch (Exception e) {
            getTest().log(Status.FAIL, "Analytics failed: " + e.getMessage());
        } finally {
            System.out.println("Analytics test end");
            if(getTest().getStatus() == Status.FAIL) afterTestFailure();
            endTest();
        }
    }

    @Test(priority = 8,dependsOnMethods = "testAnalytics")
    void testHelp() {
        try {
            startTest("Help Section");
            System.out.println("Help Section test start");
            Manage manage = new Manage(driver);
            Help help = new Help(driver);
            Play play = new Play(driver);
            manage.help();
            help.newComplaint();
            help.installationRequest();
            help.serviceRequest();
            help.trackAComplaint();
            play.videos();
            help.manual();
//            help.troubleshoot();
            ActionsUtil.Scroll.Up(driver);
            help.email();
            help.call();
            driver.navigate().back();
        } catch (Exception e) {
            getTest().log(Status.FAIL, "Help Section failed: " + e.getMessage());
        } finally {
            {
                System.out.println("Help Section test end");
                if(getTest().getStatus() == Status.FAIL) afterTestFailure();
                endTest();
            }
        }
    }

    @Test(priority = 9,dependsOnMethods = "testHelp")
    void testLogout() {
        try {
            startTest("Logout");
            System.out.println("Logout test start");
            Manage manage = new Manage(driver);
            manage.logout();
            ActionsUtil.SSleep(5);
        } catch (Exception e) {
            getTest().log(Status.FAIL, "Logout failed: " + e.getMessage());
        } finally {
            System.out.println("Logout test end");
            endTest();
        }
    }

    @Test(priority = 10, dependsOnMethods = "testLogout")
    void testDriverClose() {
        ScreenRecording recording = new ScreenRecording(driver);
        recording.stop();
        server.stopServer();
    }

    @AfterSuite
    static void tearDown() {
        if (extent != null) {
            extent.flush();
        }
    }

    void afterTestFailure() {
        ApplicationState state = driver.queryAppState("com.atomberg.app");
        if (state.equals(ApplicationState.RUNNING_IN_FOREGROUND)){
            WebElement homeScreen = null;
            while (homeScreen == null) {
                try {
                    homeScreen = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"More\nTab 3 of 3\"]"));
                } catch (Exception ignored) {
                }
                if (homeScreen == null) {
                    System.out.println("Back");
                    driver.navigate().back(); // 180, 1550 860, 1960
                }
            }
        }
        else{
            driver.activateApp("com.atomberg.app");
        }
    }
}
