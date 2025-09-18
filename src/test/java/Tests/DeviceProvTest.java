package Tests;

import app.AppInitializer;
import app.Fan.FanManagement;
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
import org.testng.annotations.*;

import static io.appium.java_client.appmanagement.ApplicationState.RUNNING_IN_FOREGROUND;

/**
 * DeviceProvTest - End-to-end test: login → fan control → logout.
 *
 * <p>This version improves structure, separation of concerns,
 * error handling, and reporting integration.
 */
public class DeviceProvTest extends BaseTest {

    private AndroidDriver driver;

    @BeforeClass
    public void setup() {
        this.driver = getDriver();
        reporter.startTest("Device Provisioning Suite", deviceSlot);
        System.out.println("Starting test suite for slot: " + deviceSlot);
    }

    @Test(priority = 1)
    public void openAppAndLogin() throws Exception {
        try {
            reporter.startTest("Open App & Login", deviceSlot);
            System.out.println("Opening app...");

            driver.activateApp("com.atomberg.app");
            ActionsUtil.SSleep(5);

            AppInitializer appInitializer = new AppInitializer();
            appInitializer.setDriver(driver);

            if (!appInitializer.checkMainScreen()) {
                performLogin();
            } else {
                System.out.println("Already logged in.");
            }

            reporter.log(Status.PASS, "App launched and user logged in successfully");
        } catch (Exception e) {
            reporter.log(Status.FAIL, "Failed to open app or login: " + e.getMessage());
            afterTestFailure(driver); // Recovery
            throw e; // Fail fast
        } finally {
            reporter.endTest();
        }
    }

    @Test(priority = 2)
    public void verifyHomeScreenAndControlFan() {
        try {
            reporter.startTest("Verify Home Screen & Control Fan", deviceSlot);
            ScreenCheck screen = new ScreenCheck(driver);
            screen.homeScreen();

            FanManagement fan = new FanManagement(driver);
            fan.checkFan();

            reporter.log(Status.PASS, "Fan control verified successfully");
        } catch (Exception e) {
            reporter.log(Status.FAIL, "Fan control failed: " + e.getMessage());
            afterTestFailure(driver);
            throw e;
        } finally {
            reporter.endTest();
        }
    }

    @Test(priority = 3)
    public void logoutFromApp() {
        try {
            reporter.startTest("Logout", deviceSlot);
            System.out.println("Logging out...");

            Manage manage = new Manage(driver);
            manage.logout();
            ActionsUtil.SSleep(5);

            // Verify logout success
            String currentActivity = driver.currentActivity();
            boolean isOnLoginScreen = currentActivity.contains("Login") || currentActivity.contains("Splash");

            if (isOnLoginScreen) {
                reporter.log(Status.PASS, "Successfully logged out");
            } else {
                reporter.log(Status.WARNING, "Logout completed but still on main screen");
            }
        } catch (Exception e) {
            reporter.log(Status.FAIL, "Logout failed: " + e.getMessage());
            afterTestFailure(driver);
            throw e;
        } finally {
            reporter.endTest();
        }
    }

    @AfterClass
    public void tearDownSuite() {
        System.out.println("Tearing down test suite for device slot: " + deviceSlot);

        // Stop screen recording
        if (getDriver() != null) {
            ScreenRecording recording = new ScreenRecording(getDriver());
            recording.stop();
        }

        // Server already stopped in BaseTest.tearDown()
        reporter.endTest(); // Final flush
    }

    // === Internal Helpers ===

    private void performLogin() throws Exception {
        String email = System.getenv("TEST_EMAIL");
        String password = System.getenv("TEST_PASSWORD");

        if (email == null || password == null) {
            System.err.println("Environment variables TEST_EMAIL/TEST_PASSWORD not set. Using fallback...");
            email = "iot.alpha@protonmail.com";
            password = "Atomberg@123";
        }

        Email login = new Email(driver);
        login.email(email, password);
        ActionsUtil.SSleep(5);
    }
}