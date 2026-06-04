package com.appTest.tests;

import app.AppInitializer;
import app.util.ActionsUtil;
import app.util.AppUtil;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static app.Supports.OnlyScreenShots.driver;
import static app.resources.AppInfo.ATOMBERG_HOME;
import static app.resources.Credentials.APP_PACKAGE;
import static app.resources.Locators.Android.HomeLocators.MORE_TAB;

/**
 * OpenAppTest – verifies the app launches and reaches a usable home screen state.
 *
 * NOTE: @Listeners is intentionally omitted here.
 * It is declared on BaseTest and is inherited by all subclasses.
 * Re-declaring it here would register each listener twice.
 */
public class OpenAppTest extends BaseTest {

    private AppInitializer appInitializer;

    @BeforeClass(dependsOnMethods = "setup")
    public void setUp() {
        Assert.assertNotNull(driver, "Driver must not be null before OpenAppTest");
        appInitializer = new AppInitializer();
        appInitializer.setDriver(driver);
        System.out.println("OpenAppTest ready on device: " + deviceSlot);
    }

    @Test(priority = 1, description = "Open Atomberg app and verify the home screen is reached")
    public void testOpenApp() throws Exception {
        reporter.startTest("Open App", deviceSlot);
        try {
            ActionsUtil.SSleep(3);
            driver.activateApp(ATOMBERG_HOME);
            ActionsUtil.SSleep(5);

            // Login automatically if the login screen is shown
            appInitializer.checkMainScreen();
            ActionsUtil.SSleep(5);

            Assert.assertTrue(
                    AppUtil.isElementPresent(driver, MORE_TAB),
                    "More tab (home screen indicator) must be visible after app launch and login");

            AppUtil.captureScreenshot(driver, "open_app_success");
            reporter.log(Status.PASS, "Atomberg App launched and home screen confirmed on: " + deviceSlot);
        } catch (Exception e) {
            AppUtil.captureScreenshot(driver, "open_app_fail");
            reporter.log(Status.FAIL, "App open failed on " + deviceSlot + ": " + e.getMessage());
            afterTestFailure();
            throw e;
        } finally {
            reporter.endTest();
        }
    }
}
