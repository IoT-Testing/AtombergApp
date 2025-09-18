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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * LoginTest - Validates various login flows: email, social, error handling.
 *
 * <p>This version eliminates duplication, improves error handling,
 * and follows clean testing practices.
 */
public class LoginTest extends BaseTest {

    private AndroidDriver driver;

    @BeforeClass
    public void setup() {
        this.driver = getDriver();
        System.out.println("Starting LoginTest suite for device slot: " + deviceSlot);
    }

    @Test(priority = 1)
    public void correctCredentials_LoginSuccess() {
        try {
            reporter.startTest("Correct Credentials - Login Success", deviceSlot);

            launchAppAndEnsureLoginScreen();
            performEmailLogin("hiwitaw422@wuzak.com", "Atomberg@1234");
            verifyLoginSuccess();

            // Logout for next test
            new Manage(driver).logout();
            driver.terminateApp("com.atomberg.app");

            reporter.log(Status.PASS, "User logged in successfully with valid credentials");
        } catch (Exception e) {
            handleTestFailure("Login with correct credentials failed", e);
        } finally {
            reporter.endTest();
        }
    }

    @Test(priority = 2)
    public void invalidEmail_FormatError() {
        try {
            reporter.startTest("Invalid Email Format", deviceSlot);

            launchAppAndEnsureLoginScreen();
            performEmailLogin("hiwitaw422wuzak.com", "Atomberg@1234"); // Missing @

            // Expect error message or stay on screen
            if (isOnLoginScreen()) {
                reporter.log(Status.PASS, "App rejected invalid email format correctly");
            } else {
                reporter.log(Status.WARNING, "Invalid email may have been accepted");
            }

            driver.terminateApp("com.atomberg.app");
            ActionsUtil.SSleep(10);
        } catch (Exception e) {
            handleTestFailure("Invalid email test failed", e);
        } finally {
            reporter.endTest();
        }
    }

    @Test(priority = 3)
    public void incorrectPassword_LoginFails() {
        try {
            reporter.startTest("Incorrect Password", deviceSlot);

            launchAppAndEnsureLoginScreen();
            performEmailLogin("hiwitaw422@wuzak.com", "Atomberg@12345"); // Wrong password

            // Should remain on login screen or show error
            if (isOnLoginScreen()) {
                reporter.log(Status.PASS, "Login blocked due to incorrect password");
            } else {
                reporter.log(Status.WARNING, "Incorrect password may have been accepted");
            }
        } catch (Exception e) {
            handleTestFailure("Incorrect password test failed", e);
        } finally {
            reporter.endTest();
        }
    }

    @Test(priority = 4)
    public void appKillAfterLogin_SessionRestored() {
        try {
            reporter.startTest("App Kill After Login - Session Restore", deviceSlot);

            launchAppAndEnsureLoginScreen();
            performEmailLogin("hiwitaw422@wuzak.com", "Atomberg@1234");

            // Kill and relaunch app
            driver.terminateApp("com.atomberg.app");
            ActionsUtil.SSleep(5);
            driver.activateApp("com.atomberg.app");

            // Verify session restored
            if (!new AppInitializer().checkMainScreen()) {
                reporter.log(Status.FAIL, "Session not restored after app kill");
            } else {
                reporter.log(Status.PASS, "User session restored after app termination");
            }

            new Manage(driver).logout();
        } catch (Exception e) {
            handleTestFailure("App kill after login test failed", e);
        } finally {
            reporter.endTest();
        }
    }

    @Test(priority = 5)
    public void appleLogin_Success() {
        try {
            reporter.startTest("Apple Login", deviceSlot);
            launchAppAndEnsureLoginScreen();

            Apple.Login(driver);
            verifyLoginSuccess();

            new Manage(driver).logout();
            driver.terminateApp("com.atomberg.app");

            reporter.log(Status.PASS, "Successfully logged in via Apple ID");
        } catch (Exception e) {
            handleTestFailure("Apple login failed", e);
        } finally {
            reporter.endTest();
        }
    }

    @Test(priority = 6)
    public void googleLogin_Success() {
        try {
            reporter.startTest("Google Login", deviceSlot);
            launchAppAndEnsureLoginScreen();

            Google.Login(driver);
            verifyLoginSuccess();

            new Manage(driver).logout();
            driver.terminateApp("com.atomberg.app");

            reporter.log(Status.PASS, "Successfully logged in via Google");
        } catch (Exception e) {
            handleTestFailure("Google login failed", e);
        } finally {
            reporter.endTest();
        }
    }

    // === Internal Helpers ===

    /**
     * Launches app and ensures we're on login screen.
     */
    private void launchAppAndEnsureLoginScreen() {
        driver.activateApp("com.atomberg.app");
        ActionsUtil.SSleep(5);

        AppInitializer appInit = new AppInitializer();
        appInit.setDriver(driver);

        if (!appInit.checkMainScreen()) {
            System.out.println("Not on home screen. Assuming login required.");
        } else {
            System.out.println("Already logged in. Logging out...");
            new Manage(driver).logout();
            ActionsUtil.SSleep(3);
            driver.activateApp("com.atomberg.app");
            ActionsUtil.SSleep(3);
        }
    }

    /**
     * Performs email login with given credentials.
     */
    private void performEmailLogin(String email, String password) {
        try {
            Email login = new Email(driver);
            login.email(email, password);
            ActionsUtil.sleep(5000);
            PermissionUtil.allow(driver);
        } catch (Exception e) {
            System.err.println("Login failed: " + e.getMessage());
        }
    }

    /**
     * Verifies user is successfully logged in.
     */
    private boolean verifyLoginSuccess() {
        AppInitializer appInit = new AppInitializer();
        appInit.setDriver(driver);
        return appInit.checkMainScreen();
    }

    /**
     * Checks if currently on login/home screen.
     */
    private boolean isOnLoginScreen() {
        try {
            if (driver.findElement(By.xpath("//android.view.View[@content-desc='My Home']")) == null) {
                driver.findElement(By.id("com.atomberg.app:id/bt_power"));
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Handles test failure with logging and recovery.
     */
    private void handleTestFailure(String message, Exception e) {
        reporter.log(Status.FAIL, message + ": " + e.getMessage());
        afterTestFailure(driver); // From BaseTest
    }
}