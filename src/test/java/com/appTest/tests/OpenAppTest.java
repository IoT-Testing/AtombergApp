package com.appTest.tests;

import app.AppInitializer;
import app.util.ActionsUtil;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.appTest.listeners.TestListeners;

/**
 * OpenAppTest - Tests app launch and basic initialization.
 * Driver is inherited from BaseTest (do NOT redeclare driver here).
 */
@Listeners({TestListeners.class})
public class OpenAppTest extends BaseTest {

    // ── NOTE: Do NOT redeclare `driver` here ──────────────────
    // It is already declared in BaseTest and initialized via
    // BaseTest.setup() → initializeDriver().
    // Redeclaring it here shadows the parent and causes null issues.

    @BeforeClass(dependsOnMethods = "setup")
    public void setUp() {
        // Driver is already initialized in BaseTest.setup()
        // Just validate it's not null before tests run
        Assert.assertNotNull(driver, "Driver should not be null after BaseTest.setup()");
        System.out.println("✅ OpenAppTest setup completed for device: " + deviceSlot);
    }

    @Test(priority = 1, description = "Open Atomberg app and verify launch")
    public void testOpenApp() throws Exception {
        try {
            reporter.startTest("Open App", deviceSlot);
            System.out.println("▶ Open App test started on device: " + deviceSlot);

            // Allow app to stabilize
            ActionsUtil.SSleep(2);

            // Activate the Atomberg app
            driver.activateApp("com.atomberg.app");
            ActionsUtil.SSleep(5);

            // Verify main screen is displayed
            AppInitializer appInitializer = new AppInitializer();
            appInitializer.setDriver(driver);
            boolean onMainScreen = appInitializer.checkMainScreen();

            Assert.assertTrue(onMainScreen, "Should reach main screen after app launch");
            reporter.log(Status.PASS, "✅ Atomberg App launched successfully on device: " + deviceSlot);

        } catch (Exception e) {
            reporter.log(Status.FAIL, "❌ App Open failed on device " + deviceSlot + ": " + e.getMessage());
            afterTestFailure(driver); // Recovery: navigate back to home
            throw e;
        } finally {
            System.out.println("⏹ Open App test ended on device: " + deviceSlot);
            reporter.endTest();
        }
    }
}