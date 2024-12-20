package ZTests;

import app.Analytics.Analytics;
import app.Fan.FanManagement;
import app.Lock.LockManagement;
import app.MoreTab.Help;
import app.MoreTab.Manage;
import app.MoreTab.Play;
import app.MoreTab.Profile;
import app.AppInitializer;
import app.STF.Connect;
import app.util.ActionsUtil;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;
import io.appium.java_client.AppiumDriver;
import org.junit.jupiter.api.*;
import static ZTests.ExtentReportAT.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppTest {
    public static AppiumDriver atomberg;
    public static final ExtentReports extent = getReportObjects();

    @Order(1)
    @Test// This test is for Opening the Atomberg Home App
    void testOpenApp() {
        try {
            startTest("Open App");
            System.out.println("OpenApp test start");
            Connect connect = new Connect();
            connect.ipAddress();
            String command = connect.copiedText;
            Runtime.getRuntime().exec(command);
            ActionsUtil.sleep(1000);

            AppInitializer appInitializer = new AppInitializer();
            //TODO: do not use "openApp()" if "initializeDriver()" is used.
            appInitializer.openApp();
            atomberg = appInitializer.getDriver();
            ActionsUtil.SSleep(2);
            //TODO: Use tapOpAppLogo() if you are using initializeDriver()
            appInitializer.checkMainScreen();
        } catch (Exception e) {
            getTest().log(Status.FAIL, "App Open failed: " + e.getMessage());
        } finally {
            System.out.println("OpenApp test end");
            endTest();
        }
    }

    @Order(2)
    @Test
    void testManageProfile(){
        try {
            startTest("Profile Edit");
            System.out.println("Profile Edit test start");
            Profile profile = new Profile(atomberg);
            profile.edit();
        } catch (Exception e) {
            getTest().log(Status.FAIL, "Logout failed: " + e.getMessage());
        } finally {
            System.out.println("Profile Edit test end");
            endTest();
        }
    }

    @Order(3)
    @Test
    void testManageFamily(){
        try {
            startTest("Family");
            System.out.println("Family test start");
            Manage manage = new Manage(atomberg);
            manage.family();
        } catch (Exception e) {
            getTest().log(Status.FAIL, "Family Management failed: " + e.getMessage());
        } finally {
            System.out.println("Family test end");

            endTest();
        }
    }

    @Order(4)
    @Test
    void testFanControl() {
        try {
            startTest("Fan Control");
            System.out.println("Fan Control test start");
            FanManagement fan = new FanManagement(atomberg);
            fan.checkFan();
        } catch (Exception e) {
            getTest().log(Status.FAIL, "Fan Control failed: " + e.getMessage());
        } finally {
            System.out.println("Fan Control test end");
            endTest();
        }
    }

    @Order(5)
    @Test
    void testLockControl(){
        try {
            startTest("Lock Control");
            System.out.println("Lock Control test start");
            LockManagement lock = new LockManagement(atomberg);
            lock.checkLock();
        } catch (Exception e) {
            getTest().log(Status.FAIL, "Lock Control failed: " + e.getMessage());
        } finally {
            System.out.println("Lock Control test end");
            endTest();
        }
    }

    @Order(6)
    @Test
    void testAnalytics() {
        try {
            startTest("Analytics");
            System.out.println("Analytics test start");
            Analytics analytics = new Analytics(atomberg);
            analytics.Show();
        } catch (Exception e) {
            getTest().log(Status.FAIL, "Analytics failed: " + e.getMessage());
        } finally {
            System.out.println("Analytics test end");
            endTest();
        }
    }

    @Order(7)
    @Test
    void testHelp() {
        try {
            startTest("More Tab");
            System.out.println("Help Section test start");
            Manage manage = new Manage(atomberg);
            Help help = new Help(atomberg);
            Play play = new Play(atomberg);
            manage.help();
            help.raiseAComplaint();
            help.trackAComplaint();
            play.videos();
            help.manual();
//            help.troubleshoot();
            ActionsUtil.Scroll.Up(atomberg);
            help.email();
            help.call();
            atomberg.navigate().back();
        } catch (Exception e) {
            getTest().log(Status.FAIL, "More Tab failed: " + e.getMessage());
        } finally {
            {
                System.out.println("Help Section test end");

                endTest();
            }
        }
    }

    @Order(8)
    @Test
    void testLogout() {
        try {
            startTest("Logout");
            System.out.println("Logout test start");
            Manage manage = new Manage(atomberg);
            manage.logout();
            ActionsUtil.SSleep(5);
        } catch (Exception e) {
            getTest().log(Status.FAIL, "Logout failed: " + e.getMessage());
        } finally {
            System.out.println("Logout test end");
            endTest();
        }
    }

    @Order(9)
    @Test
    void testDriverClose() {
        atomberg.quit();
    }

    @AfterAll
    static void tearDown() {

        if (extent != null) {
            extent.flush();
        }
    }
}
