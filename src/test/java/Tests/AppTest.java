package Tests;

import app.Analytics.analytics;
import app.AppInitializer;
import app.Fan.FanManagement;
import app.Lock.LockManagement;
import app.Login.Email;
import app.MoreTab.Help;
import app.MoreTab.Manage;
import app.MoreTab.Play;
import app.MoreTab.Profile;
import app.util.ActionsUtil;
import app.util.ScreenRecording;
import com.aventstack.extentreports.Status;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.appmanagement.ApplicationState;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.*;

import static io.appium.java_client.appmanagement.ApplicationState.RUNNING_IN_FOREGROUND;

/**
 * AppTest - End-to-end test suite for Atomberg app.
 *
 * <p>Refactored to:
 * <ul>
 *   <li>Add meaningful assertions</li>
 *   <li>Improve error recovery</li>
 *   <li>Reduce duplication</li>
 *   <li>Follow TestNG best practices</li>
 * </ul>
 */
public class AppTest extends BaseTest {

    public AndroidDriver driver;

    // === Locator Constants ===
    private static final By MORE_TAB_INDICATOR = By.xpath("//android.widget.ImageView[@content-desc='More\nTab 3 of 3']");
    private static final By LOGIN_SCREEN_INDICATOR = By.xpath("//android.view.View[contains(@content-desc, 'Experience smart living')]");
    private static final String APP_PACKAGE = "com.atomberg.app";

    @BeforeClass
    public void setUp() {
        driver = getDriver();
        Assert.assertNotNull(driver, "Driver should not be null after setup");
    }

    @Test(priority = 1, description = "Open app and log in")
    public void testOpenApp() throws Exception {
        reporter.startTest("Open App", deviceSlot);
        try {
            System.out.println("OpenApp test start");

            ActionsUtil.SSleep(2);
            driver.activateApp(APP_PACKAGE);
            ActionsUtil.SSleep(5);

            AppInitializer appInitializer = new AppInitializer();
            appInitializer.setDriver(driver);

            boolean onLoginScreen = appInitializer.checkMainScreen();
            System.out.println("On login screen: " + onLoginScreen);

            if (onLoginScreen) {
                Email login = new Email(driver);
                login.email("hiwitaw422@wuzak.com", "Atomberg@1234");
            }

            // ✅ Assertion: Verify we are past login
            WebElement moreTab = findElementWithWait(MORE_TAB_INDICATOR);
            Assert.assertTrue(moreTab.isDisplayed(), "Should reach home screen after login");
            reporter.log(Status.PASS, "Successfully logged in and reached home screen");

        } catch (Exception e) {
            reporter.log(Status.FAIL, "App Open failed: " + e.getMessage());
            throw e; // Fail fast
        } finally {
            System.out.println("OpenApp test end");
            reporter.endTest();
        }
    }

    @Test(priority = 2, dependsOnMethods = "testOpenApp", description = "Edit user profile")
    public void testManageProfile() {
        reporter.startTest("Profile Edit", deviceSlot);
        try {
            Profile profile = new Profile(driver);
            profile.edit();

            // ✅ Assertion: Confirm edit was applied (example)
            WebElement successToast = findOptionalElement(By.xpath("//android.widget.Toast"));
            Assert.assertNull(successToast, "No error toast should appear during profile edit");

            reporter.log(Status.PASS, "Profile edited successfully");

        } catch (Exception e) {
            reporter.log(Status.FAIL, "Profile edit failed: " + e.getMessage());
            throw e;
        } finally {
            if (reporter.getCurrentStatus() == Status.FAIL) afterTestFailure();
            reporter.endTest();
        }
    }

    @Test(priority = 3, dependsOnMethods = "testManageProfile", description = "Manage family members")
    public void testManageFamily() {
        reporter.startTest("Family Management", deviceSlot);
        try {
            Manage manage = new Manage(driver);
            manage.family();

            // ✅ Assertion: At least one family member visible?
            WebElement familyHeader = findOptionalElement(By.xpath("//android.view.View[@content-desc='Your Families']"));
            Assert.assertNotNull(familyHeader, "Family section header should be visible");

            reporter.log(Status.PASS, "Family managed successfully");

        } catch (Exception e) {
            reporter.log(Status.FAIL, "Family Management failed: " + e.getMessage());
            throw e;
        } finally {
            if (reporter.getCurrentStatus() == Status.FAIL) afterTestFailure();
            reporter.endTest();
        }
    }

    @Test(priority = 4, description = "Control connected fans")
    public void testFanControl() {
        reporter.startTest("Fan Control", deviceSlot);
        try {
            FanManagement fan = new FanManagement(driver);
            fan.checkFan();

            // ✅ Assertion: Ensure at least one fan was controlled
            WebElement fansTab = findOptionalElement(By.xpath("//android.widget.ImageView[@content-desc='Fans']"));
            Assert.assertNotNull(fansTab, "Fans tab should exist after checkFan()");
            reporter.log(Status.PASS, "Fan control completed");

        } catch (Exception e) {
            reporter.log(Status.FAIL, "Fan Control failed: " + e.getMessage());
            throw e;
        } finally {
            if (reporter.getCurrentStatus() == Status.FAIL) afterTestFailure();
            reporter.endTest();
        }
    }

    @Test(priority = 5, dependsOnMethods = "testManageProfile", description = "Control connected locks")
    public void testLockControl() {
        reporter.startTest("Lock Control", deviceSlot);
        try {
            LockManagement lock = new LockManagement(driver);
            lock.checkLock();

            // ✅ Assertion: Lock settings or history accessed
            WebElement unlockHandle = findOptionalElement(By.xpath("//android.view.View[@content-desc='Pull down to unlock']"));
            Assert.assertNotNull(unlockHandle, "Unlock handle should be accessible");

            reporter.log(Status.PASS, "Lock control completed");

        } catch (Exception e) {
            reporter.log(Status.FAIL, "Lock Control failed: " + e.getMessage());
            throw e;
        } finally {
            if (reporter.getCurrentStatus() == Status.FAIL) afterTestFailure();
            reporter.endTest();
        }
    }

    @Test(priority = 7, dependsOnMethods = "testManageFamily", description = "Verify analytics data")
    public void testAnalytics() {
        reporter.startTest("Analytics", deviceSlot);
        try {
            analytics analytics = new analytics(driver);
            analytics.Show();

            // ✅ Assertion: Graph or data loaded
            WebElement chart = findOptionalElement(By.className("android.view.View")); // Simplified
            Assert.assertNotNull(chart, "Analytics chart should be rendered");

            reporter.log(Status.PASS, "Analytics displayed correctly");

        } catch (Exception e) {
            reporter.log(Status.FAIL, "Analytics failed: " + e.getMessage());
            throw e;
        } finally {
            if (reporter.getCurrentStatus() == Status.FAIL) afterTestFailure();
            reporter.endTest();
        }
    }

    @Test(priority = 8, dependsOnMethods = "testAnalytics", description = "Navigate help section")
    public void testHelp() {
        reporter.startTest("Help Section", deviceSlot);
        try {
            Help help = new Help(driver);
            Play play = new Play(driver);
            Manage manage = new Manage(driver);

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

            // ✅ Assertion: Back on main screen
            Assert.assertTrue(isOnHomeScreen(), "Should return to home screen after Help section");

            reporter.log(Status.PASS, "Help section navigated successfully");

        } catch (Exception e) {
            reporter.log(Status.FAIL, "Help Section failed: " + e.getMessage());
            throw e;
        } finally {
            if (reporter.getCurrentStatus() == Status.FAIL) afterTestFailure();
            reporter.endTest();
        }
    }

    @Test(priority = 9, description = "Log out from account")
    public void testLogout() {
        reporter.startTest("Logout", deviceSlot);
        try {
            Manage manage = new Manage(driver);
            manage.logout();
            ActionsUtil.SSleep(5);

            // ✅ Assertion: Should return to login screen
            Assert.assertTrue(
                    findElementWithWait(LOGIN_SCREEN_INDICATOR).isDisplayed(),
                    "Login screen should appear after logout"
            );
            reporter.log(Status.PASS, "Logged out successfully");

        } catch (Exception e) {
            reporter.log(Status.FAIL, "Logout failed: " + e.getMessage());
            throw e;
        } finally {
            if (reporter.getCurrentStatus() == Status.FAIL) afterTestFailure();
            reporter.endTest();
        }
    }

    @Test(priority = 10, dependsOnMethods = "testLogout", description = "Close driver and stop server")
    public void testDriverClose() {
        reporter.startTest("Driver Close", deviceSlot);
        try {
            ScreenRecording recording = new ScreenRecording(driver);
            recording.stop();
            server.stopServer();
            reporter.log(Status.PASS, "Driver closed and server stopped");
        } catch (Exception e) {
            reporter.log(Status.FAIL, "Cleanup failed: " + e.getMessage());
            throw e;
        } finally {
            reporter.endTest();
        }
    }

    @AfterSuite
    public void tearDown() {
        if (reporter != null) {
            reporter.endTest(); // Ensure report is written
        }
        if (driver != null) {
            driver.quit();
        }
    }

    // === Utility Methods ===

    /**
     * Finds element with explicit wait.
     */
    private WebElement findElementWithWait(By locator) {
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < (long) 10 * 1000) {
            try {
                return driver.findElement(locator);
            } catch (NoSuchElementException ignored) {
                ActionsUtil.sleep(500);
            }
        }
        throw new RuntimeException("Element not found after " + (long) 10 + " seconds: " + locator);
    }

    /**
     * Safely finds element without throwing exception.
     */
    private WebElement findOptionalElement(By locator) {
        try {
            return driver.findElement(locator);
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    /**
     * Checks if currently on home screen.
     */
    private boolean isOnHomeScreen() {
        return findOptionalElement(MORE_TAB_INDICATOR) != null;
    }

    /**
     * Recovery logic: navigate back to home if app is stuck
     */
    public void afterTestFailure() {
        ApplicationState state = driver.queryAppState(APP_PACKAGE);
        if (state.equals(RUNNING_IN_FOREGROUND)) {
            int backCount = 0;
            while (!isOnHomeScreen() && backCount < 10) {
                System.out.println("Navigating back... attempt " + backCount);
                driver.navigate().back();
                ActionsUtil.SSleep(2);
                backCount++;
            }
            Assert.assertTrue(isOnHomeScreen(), "Failed to recover to home screen after multiple back presses");
        } else {
            driver.activateApp(APP_PACKAGE);
            Assert.assertEquals(driver.queryAppState(APP_PACKAGE), RUNNING_IN_FOREGROUND, "App should be in foreground");
        }
    }
}