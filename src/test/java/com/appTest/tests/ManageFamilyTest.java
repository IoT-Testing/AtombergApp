package com.appTest.tests;

import app.AppInitializer;
import app.MoreTab.Manage;
import app.util.ActionsUtil;
import app.util.AppUtil;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static app.resources.AppInfo.*;
import static app.resources.Locators.Android.HomeLocators.MORE_TAB;

/**
 * ManageFamilyTest – verifies that the Manage Family workflow is reachable
 * and executes without crashing.
 *
 * <p>Inherits driver lifecycle and Extent reporting from {@link BaseTest}.</p>
 *
 * <p><strong>@Listeners must NOT be redeclared here</strong> — it is registered
 * once on {@code BaseTest}. Re-declaring it causes each listener to fire twice.</p>
 */
public class ManageFamilyTest extends BaseTest {

    private AppInitializer appInitializer;

    @BeforeClass(dependsOnMethods = "setup")
    public void setUp() {
        Assert.assertNotNull(driver, "Driver must not be null before ManageFamilyTest");
        appInitializer = new AppInitializer(driver);
        appInitializer.setDriver(driver);
        System.out.println("ManageFamilyTest ready on: " + deviceSlot);
    }

    @Test(priority = 1, description = "Navigate to Manage Family and verify the screen loads")
    public void testManageFamily() throws Exception {
        reporter.startTest("Manage Family", deviceSlot);
        try {
            System.out.println("Manage Family test start");

            driver.activateApp(ATOMBERG_HOME);
            ActionsUtil.SSleep(5);

            // Auto-login if the login screen is detected
            appInitializer.checkMainScreen();

            // Verify home screen reached before proceeding
            Assert.assertTrue(
                    AppUtil.isElementPresent(driver, MORE_TAB),
                    "Home screen (More tab) must be visible before managing family");

            new Manage(driver).family();

            AppUtil.captureScreenshot(driver, "manage_family_success");
            reporter.log(Status.PASS, "Family management screen reached successfully on: " + deviceSlot);
        } catch (Exception e) {
            AppUtil.captureScreenshot(driver, "manage_family_fail");
            reporter.log(Status.FAIL, "Family management failed on " + deviceSlot + ": " + e.getMessage());
            afterTestFailure();
            throw e;
        } finally {
            System.out.println("Manage Family test end");
            reporter.endTest();
        }
    }
}
