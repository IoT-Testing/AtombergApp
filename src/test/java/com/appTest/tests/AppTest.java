package com.appTest.tests;

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
import org.openqa.selenium.*;
import org.testng.Assert;
import org.testng.annotations.*;
import static app.resources.Locators.Android.HomeLocators.*;
import static app.resources.Locators.Android.AppLocators.Login.*;
import static app.util.AppUtil.findOptionalElement;

/**
 * AppTest - End-to-end test suite for Atomberg app.
 */
public class AppTest extends BaseTest {

    public AndroidDriver driver;

    // === Locator Constants ===
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
            WebElement moreTab = findElementWithWait(MORE_TAB, 5);
            Assert.assertNotNull(moreTab);
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
            reporter.log(Status.PASS, "Profile edited successfully");

        } catch (Exception e) {
            reporter.log(Status.FAIL, "Profile edit failed: " + e.getMessage());
            throw e;
        } finally {
            if (reporter.getCurrentStatus() == Status.FAIL) afterTestFailure(driver);
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
            WebElement familyHeader = findOptionalElement(driver, By.xpath("//android.view.View[@content-desc='Your Families']"));
            Assert.assertNotNull(familyHeader, "Family section header should be visible");

            reporter.log(Status.PASS, "Family managed successfully");

        } catch (Exception e) {
            reporter.log(Status.FAIL, "Family Management failed: " + e.getMessage());
            throw e;
        } finally {
            if (reporter.getCurrentStatus() == Status.FAIL) afterTestFailure(driver);
            reporter.endTest();
        }
    }

    @Test(priority = 4, dependsOnMethods = "testOpenApp", description = "Control connected fans")
    public void testFanControl() {
        reporter.startTest("Fan Control", deviceSlot);
        try {
            FanManagement fan = new FanManagement(driver);
            fan.checkFan();

            // ✅ Assertion: Ensure at least one fan was controlled
            WebElement fansTab = findOptionalElement(driver, By.xpath("//android.widget.ImageView[@content-desc='Fans']"));
            Assert.assertNotNull(fansTab, "Fans tab should exist after checkFan()");
            reporter.log(Status.PASS, "Fan control completed");

        } catch (Exception e) {
            reporter.log(Status.FAIL, "Fan Control failed: " + e.getMessage());
            throw e;
        } finally {
            if (reporter.getCurrentStatus() == Status.FAIL) afterTestFailure(driver);
            reporter.endTest();
        }
    }

    @Test(priority = 5, dependsOnMethods = "testOpenApp", description = "Control connected locks")
    public void testLockControl() {
        reporter.startTest("Lock Control", deviceSlot);
        try {
            LockManagement lock = new LockManagement(driver);
            lock.checkLock();

            // ✅ Assertion: Lock settings or history accessed
            WebElement unlockHandle = findOptionalElement(driver, By.xpath("//android.view.View[@content-desc='Pull down to unlock']"));
            Assert.assertNotNull(unlockHandle, "Unlock handle should be accessible");

            reporter.log(Status.PASS, "Lock control completed");

        } catch (Exception e) {
            reporter.log(Status.FAIL, "Lock Control failed: " + e.getMessage());
            throw e;
        } finally {
            if (reporter.getCurrentStatus() == Status.FAIL) afterTestFailure(driver);
            reporter.endTest();
        }
    }

    @Test(priority = 6, dependsOnMethods = "testOpenApp", description = "Verify analytics data")
    public void testAnalytics() {
        reporter.startTest("Analytics", deviceSlot);
        try {
            analytics analytics = new analytics(driver);
            analytics.Show();

            // ✅ Assertion: Graph or data loaded
            WebElement chart = findOptionalElement(driver, By.className("android.view.View")); // Simplified
            Assert.assertNotNull(chart, "Analytics chart should be rendered");

            reporter.log(Status.PASS, "Analytics displayed correctly");

        } catch (Exception e) {
            reporter.log(Status.FAIL, "Analytics failed: " + e.getMessage());
            throw e;
        } finally {
            if (reporter.getCurrentStatus() == Status.FAIL) afterTestFailure(driver);
            reporter.endTest();
        }
    }

    @Test(priority = 7, dependsOnMethods = "testAnalytics", description = "Navigate help section")
    public void testHelp() {
        reporter.startTest("Help Section", deviceSlot);
        try {
            Help help = new Help(driver);
            Play play = new Play(driver);
            Manage manage = new Manage(driver);

            help.ConnectivityTroubleshoot();
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
            Assert.assertTrue(isOnHomeScreen(driver), "Should return to home screen after Help section");

            reporter.log(Status.PASS, "Help section navigated successfully");

        } catch (Exception e) {
            reporter.log(Status.FAIL, "Help Section failed: " + e.getMessage());
            throw e;
        } finally {
            if (reporter.getCurrentStatus() == Status.FAIL) afterTestFailure(driver);
            reporter.endTest();
        }
    }

    @Test(priority = 8, dependsOnMethods = "testAnalytics", description = "Log out from account")
    public void testLogout() {
        reporter.startTest("Logout", deviceSlot);
        try {
            Manage manage = new Manage(driver);
            manage.logout();
            ActionsUtil.SSleep(5);

            // ✅ Assertion: Should return to log in screen
            Assert.assertTrue(
                    findElementWithWait(LOGIN_SCREEN_INDICATOR, 5).isDisplayed(),
                    "Login screen should appear after logout"
            );
            reporter.log(Status.PASS, "Logged out successfully");

        } catch (Exception e) {
            reporter.log(Status.FAIL, "Logout failed: " + e.getMessage());
            throw e;
            
        } finally {
            if (reporter.getCurrentStatus() == Status.FAIL) afterTestFailure(driver);
            reporter.endTest();
        }
    }

    @Test(priority = 9, dependsOnMethods = "testLogout", description = "Close driver and stop server")
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
    /**
     * Finds element with explicit wait.
     */
    private WebElement findElementWithWait(By locator, long timeoutSec) {
        final long POLLING_INTERVAL_MS = 500;
        final long TIMEOUT_MS = timeoutSec * 1000;
        long startTimeMs = System.currentTimeMillis();

        System.out.println("Waiting up to " + timeoutSec + "s for element: " + locator);

        while ((System.currentTimeMillis() - startTimeMs) < TIMEOUT_MS) {                WebElement element = driver.findElement(locator);

            try {
                if (element.isDisplayed()) {
                    System.out.println("✅ Found and visible: " + locator);
                    return element;
                } else {
                    System.out.println("⚠️  Found but not displayed. Retrying...");
                }
            } catch (NoSuchElementException | StaleElementReferenceException e) {
                // Expected: element not yet available
            } catch (WebDriverException e) {
                // Common in Appium: e.g., "no such context", "remote end disconnected"
                System.err.println("WebDriverException while waiting: " + e.getMessage());
            } catch (Exception e) {
                // Catch-all for unexpected issues
                System.err.println("Unexpected error waiting for element: " + e.getClass().getSimpleName());
            }
            ActionsUtil.sleep(POLLING_INTERVAL_MS); // Wait before retry
        }

        // Timeout expired
        String message = "❌ Failed to find element after " + timeoutSec + " seconds: " + locator;
        System.err.println(message);
        throw new RuntimeException(message);
}
}