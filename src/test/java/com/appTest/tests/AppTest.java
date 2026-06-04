package com.appTest.tests;

import app.AppInitializer;
import app.Login.Email;
import app.MoreTab.Manage;
import app.util.ActionsUtil;
import app.util.AppUtil;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static app.resources.Credentials.*;
import static app.resources.Locators.Android.AppLocators.Login.LOGIN_SCREEN_INDICATOR;
import static app.resources.Locators.Android.AppLocators.MoreTab.PROFILE_GREETING;
import static app.resources.Locators.Android.HomeLocators.*;

/**
 * AppTest – end-to-end smoke test covering app open, tab navigation, and logout.
 *
 * NOTE: @Listeners is intentionally omitted here.
 * It is declared on BaseTest and is inherited by all subclasses.
 * Re-declaring it here would register each listener twice.
 *
 * Test order:
 *   1. Open app / login if on login screen        → asserts MORE_TAB visible
 *   2. Navigate to Analytics tab                  → asserts ANALYTICS_TAB + MORE_TAB visible
 *   3. Navigate to More tab                       → asserts profile greeting visible
 *   4. Logout                                     → asserts LOGIN_SCREEN_INDICATOR visible
 */
public class AppTest extends BaseTest {

    private AppInitializer appInit;
    private Email          emailLogin;
    private Manage         manage;

    @BeforeClass(dependsOnMethods = "setup")
    public void initHelpers() {
        appInit    = new AppInitializer();
        appInit.setDriver(driver);
        emailLogin = new Email(driver);
        manage     = new Manage(driver);
    }

    // ── 1. Open + Login ───────────────────────────────────────────────────────

    @Test(priority = 1, description = "Open app and login if on login screen; verify home screen")
    public void testOpenAndLogin() throws Exception {
        reporter.startTest("Open App + Login", deviceSlot);
        try {
            ActionsUtil.SSleep(3);
            appInit.checkMainScreen();
            ActionsUtil.SSleep(5);

            Assert.assertTrue(
                    AppUtil.isElementPresent(driver, MORE_TAB),
                    "More tab must be visible — confirms user reached the home screen");

            AppUtil.captureScreenshot(driver, "home_screen_confirmed");
            reporter.log(Status.PASS, "App opened and logged in. Home screen confirmed.");
        } catch (Exception e) {
            AppUtil.captureScreenshot(driver, "open_login_fail");
            reporter.log(Status.FAIL, "Open/Login failed: " + e.getMessage());
            afterTestFailure();
            throw e;
        } finally {
            reporter.endTest();
        }
    }

    // ── 2. Analytics tab ─────────────────────────────────────────────────────

    @Test(priority = 2, description = "Tap Analytics tab and verify the analytics view is shown",
            dependsOnMethods = "testOpenAndLogin")
    public void testAnalyticsTab() {
        reporter.startTest("Analytics Tab Navigation", deviceSlot);
        try {
            AppUtil.clickElement(driver, ANALYTICS_TAB, "Analytics Tab");
            ActionsUtil.SSleep(2);

            // Both tabs must remain visible — confirms we are in the main nav on the Analytics screen
            Assert.assertTrue(
                    AppUtil.isElementPresent(driver, ANALYTICS_TAB),
                    "Analytics tab button must still be visible after tapping it");
            Assert.assertTrue(
                    AppUtil.isElementPresent(driver, MORE_TAB),
                    "More tab button must be visible — confirms we are still in the bottom navigation");

            AppUtil.captureScreenshot(driver, "analytics_tab");
            reporter.log(Status.PASS, "Analytics tab opened; bottom nav present");
        } catch (Exception e) {
            AppUtil.captureScreenshot(driver, "analytics_tab_fail");
            reporter.log(Status.FAIL, "Analytics tab failed: " + e.getMessage());
            afterTestFailure();
            throw new RuntimeException(e);
        } finally {
            reporter.endTest();
        }
    }

    // ── 3. More tab ───────────────────────────────────────────────────────────

    @Test(priority = 3, description = "Tap More tab and verify the More screen content is shown",
            dependsOnMethods = "testOpenAndLogin")
    public void testMoreTab() {
        reporter.startTest("More Tab Navigation", deviceSlot);
        try {
            AppUtil.clickElement(driver, MORE_TAB, "More Tab");
            ActionsUtil.SSleep(2);

            // MORE_TAB button must still be visible (we are still in the main nav)
            Assert.assertTrue(
                    AppUtil.isElementPresent(driver, MORE_TAB),
                    "More tab button must still be visible after tapping it");

            // The profile greeting ("Hi, <name>") is always the first element on the More screen
            Assert.assertTrue(
                    AppUtil.isElementPresent(driver, PROFILE_GREETING),
                    "Profile greeting (\"Hi, ...\") must be visible on the More screen");

            AppUtil.captureScreenshot(driver, "more_tab");
            reporter.log(Status.PASS, "More tab opened; profile greeting visible");
        } catch (Exception e) {
            AppUtil.captureScreenshot(driver, "more_tab_fail");
            reporter.log(Status.FAIL, "More tab failed: " + e.getMessage());
            afterTestFailure();
            throw new RuntimeException(e);
        } finally {
            reporter.endTest();
        }
    }

    // ── 4. Logout ─────────────────────────────────────────────────────────────

    @Test(priority = 4, description = "Full logout flow; verify login screen appears",
            dependsOnMethods = "testMoreTab")
    public void testLogout() {
        reporter.startTest("Logout Flow", deviceSlot);
        try {
            manage.logout();
            ActionsUtil.SSleep(3);

            Assert.assertTrue(
                    AppUtil.isElementPresent(driver, LOGIN_SCREEN_INDICATOR),
                    "Login screen indicator must be visible after logout");

            AppUtil.captureScreenshot(driver, "logout_confirmed");
            reporter.log(Status.PASS, "Logout successful — login screen visible");
        } catch (Exception e) {
            AppUtil.captureScreenshot(driver, "logout_fail");
            reporter.log(Status.FAIL, "Logout failed: " + e.getMessage());
            afterTestFailure();
            throw new RuntimeException(e);
        } finally {
            reporter.endTest();
        }
    }
}
