package com.appTest.tests;

import app.AppInitializer;
import app.util.ActionsUtil;
import app.util.ScreenRecording;
import com.aventstack.extentreports.Status;
import io.appium.java_client.android.AndroidDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.appTest.listeners.TestListeners;

/**
 * OpenAppTest - Tests app launch and basic initialization.
 */
@Listeners({TestListeners.class})
public class OpenAppTest extends BaseTest {

    public AndroidDriver driver;
    private ScreenRecording recording;

    @BeforeClass
    public void setUp() {
        driver = getDriver();
        Assert.assertNotNull(driver, "Driver should not be null after setup");
        System.out.println("OpenAppTest setup completed for device: " + deviceSlot);
    }

    @Test(priority = 1, description = "Open app and verify launch")
    void testOpenApp() {
        try {
            reporter.startTest("Open App", deviceSlot);
            System.out.println("Open App test start");
            
            ActionsUtil.SSleep(2);
            driver.activateApp("com.atomberg.app");
            ActionsUtil.SSleep(5);

            AppInitializer appInitializer = new AppInitializer();
            appInitializer.setDriver(driver);
            boolean onMainScreen = appInitializer.checkMainScreen();
            
            Assert.assertTrue(onMainScreen, "Should reach main screen after app launch");
            reporter.log(Status.PASS, "App launched successfully");
        } catch (Exception e) {
            reporter.log(Status.FAIL, "App Open failed: " + e.getMessage());
            throw e;
        } finally {
            System.out.println("Open App test end");
            reporter.endTest();
        }
    }
}
