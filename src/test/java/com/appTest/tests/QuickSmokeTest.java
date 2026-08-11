package com.appTest.tests;

import app.AppInitializer;
import app.Login.Email;
import app.MoreTab.Manage;
import app.util.ActionsUtil;
import app.util.AppUtil;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.*;
import java.time.Duration;
import static app.resources.AppInfo.*;
import static app.resources.Credentials.*;

/**
 * QuickSmokeTest – rapid device-farm sanity check:
 * launch → login (or re-login if already logged in) → verify home screen.
 *
 * <p>Used to prove that the test script has full device control after
 * acquiring a session from a device farm (e.g. STF). The test does NOT
 * call {@code driver.quit()} — the driver lifecycle is owned by
 * {@link BaseTest#teardown()}.</p>
 *
 * <p>Inherits driver lifecycle and Extent reporting from {@link BaseTest}.</p>
 *
 * <p><strong>@Listeners must NOT be redeclared here</strong> — it is registered
 * once on {@code BaseTest}. Re-declaring it causes each listener to fire twice.</p>
 */
public class QuickSmokeTest extends BaseTest {

    private AppInitializer appInitializer;

    @BeforeClass(dependsOnMethods = "setup")
    public void setUp() {
        Assert.assertNotNull(driver, "Driver must not be null before QuickSmokeTest");
        appInitializer = new AppInitializer(driver);
        appInitializer.setDriver(driver);
        System.out.println("QuickSmokeTest ready on: " + deviceSlot);
    }

    @Test(description = "Verify device farm control: launch → login → confirm home screen")
    public void verifyDeviceFarmControl() throws Exception {
        reporter.startTest("Quick Smoke Test", deviceSlot);
        try {
            System.out.println("Session : " + driver.getSessionId());
            System.out.println("Device  : " + driver.getCapabilities().getCapability("deviceName"));
            System.out.println("UDID    : " + driver.getCapabilities().getCapability("udid"));

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            driver.activateApp(ATOMBERG_HOME);
            ActionsUtil.SSleep(3);

            // checkMainScreen() returns true when the LOGIN screen was found (and auto-login ran).
            // Returns false when already on the home screen.
            boolean wasOnLoginScreen = appInitializer.checkMainScreen();

            if (!wasOnLoginScreen) {
                // Already logged in — log out then re-login with default credentials
                System.out.println("Already logged in. Logging out for clean smoke run…");
                new Manage(driver).logout();
                ActionsUtil.SSleep(3);
                driver.activateApp(ATOMBERG_HOME);
                ActionsUtil.SSleep(3);
                new Email(driver).email(DEFAULT_EMAIL, DEFAULT_PASSWORD);
                ActionsUtil.SSleep(5);
            }

            // After login the home screen must be visible; checkMainScreen() should return false
            boolean stillOnLoginScreen = appInitializer.checkMainScreen();
            Assert.assertFalse(stillOnLoginScreen,
                    "Expected to be on home screen after login, but login screen is still visible");

            Assert.assertTrue(
                    AppUtil.isElementPresent(driver, app.resources.Locators.Android.HomeLocators.MORE_TAB),
                    "More tab must be present on the home screen after successful login");

            AppUtil.captureScreenshot(driver, "quick_smoke_pass");
            reporter.log(Status.PASS,
                    "Script has full device control — session: " + driver.getSessionId());
        } catch (Exception e) {
            AppUtil.captureScreenshot(driver, "quick_smoke_fail");
            reporter.log(Status.FAIL, "Quick smoke test failed on " + deviceSlot + ": " + e.getMessage());
            afterTestFailure();
            throw e;
        } finally {
            reporter.endTest();
        }
    }
}
