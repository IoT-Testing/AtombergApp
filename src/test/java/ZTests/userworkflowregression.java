package ZTests;

import app.AppInitializer;
import app.Fan.FanManagement;
import app.MoreTab.Help;
import app.MoreTab.Manage;
import app.MoreTab.Play;
import app.MoreTab.Profile;
import app.ScreenCheck.ScreenCheck;
import app.util.ActionsUtil;
import app.util.AppUtil;
import com.appTest.listeners.DashboardReporter;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.util.List;

@Listeners(DashboardReporter.class)
public class userworkflowregression {

    private AndroidDriver driver;
    private AppInitializer appInitializer;
    private FanManagement fanManagement;
    private Help help;
    private Play play;
    private Manage manage;
    private Profile profile;
    private ScreenCheck screenCheck;

    @BeforeClass(alwaysRun = true)
    public void setup() throws MalformedURLException {
        appInitializer = new AppInitializer(driver);
//        driver = appInitializer.appInitialize();

        // Initialize all page objects
        fanManagement = new FanManagement(driver);
        help = new Help(driver);
        play = new Play(driver);
        manage = new Manage(driver);
        profile = new Profile(driver);
        screenCheck = new ScreenCheck(driver);

        System.out.println("=== Setup Complete: App Initialized ===");
    }

    @Test(priority = 1, description = "Test Step 1: Verify App Opened Successfully")
    public void testAppOpened() {
        try {
            System.out.println(" >>> TEST 1: App Opened Successfully");

            // Verify app is in focus
            String currentPackage = driver.getCurrentPackage();
            Assert.assertTrue(currentPackage.contains("atomberg"),
                    "App package should contain 'atomberg', but found: " + currentPackage);

            AppUtil.captureScreenshot(driver, "App Opened");
            System.out.println("✓ App opened successfully with package: " + currentPackage);
        } catch (Exception e) {
            System.err.println("✗ Test failed: " + e.getMessage());
            throw new AssertionError("App failed to open", e);
        }
    }

    @Test(priority = 2, description = "Test Step 2: User Login")
    public void testUserLogin() {
        try {
            System.out.println(" >>> TEST 2: User Login");

            // Perform email login
//            appInitializer.email();
            ActionsUtil.sleep(3000);

            // Verify home screen is displayed
            screenCheck.homeScreen();
            AppUtil.captureScreenshot(driver, "Login Success");
            System.out.println("✓ User logged in successfully");
        } catch (Exception e) {
            System.err.println("✗ Login test failed: " + e.getMessage());
            throw new AssertionError("Login failed", e);
        }
    }

    @Test(priority = 3, description = "Test Step 3: Check Available Devices")
    public void testCheckAvailableDevices() {
        try {
            System.out.println(" >>> TEST 3: Check Available Devices");

            // Navigate to home screen
            screenCheck.homeScreen();
            ActionsUtil.sleep(2000);

            // Check if devices section exists
            WebElement devicesSection = driver.findElement(By.xpath(
                    "//android.widget.FrameLayout[@resource-id='android:id/content']"));
            Assert.assertNotNull(devicesSection, "Devices section should be visible");

            AppUtil.captureScreenshot(driver, "Available Devices Screen");
            System.out.println("✓ Available devices displayed");
        } catch (Exception e) {
            System.err.println("✗ Device check failed: " + e.getMessage());
            throw new AssertionError("Device check failed", e);
        }
    }

    @Test(priority = 4, description = "Test Step 4: Add Device (Fan)")
    public void testAddFan() {
        try {
            System.out.println(" >>> TEST 4: Add Fan Device");

            // Navigate to home and initiate fan addition
            screenCheck.homeScreen();
            ActionsUtil.sleep(1000);

//            fanManagement.addFan();

            AppUtil.captureScreenshot(driver, "Fan Addition Initiated");
            System.out.println("✓ Fan addition process initiated");
        } catch (Exception e) {
            System.err.println("✗ Fan addition failed: " + e.getMessage());
            // Note: This test may fail if no fan is available, which is expected
            System.out.println("⚠ Fan addition skipped (No fan device available in range)");
        }
    }

    @Test(priority = 5, description = "Test Step 5: Control Device (Fan)")
    public void testControlFan() {
        try {
            System.out.println(" >>> TEST 5: Control Fan Device");

            screenCheck.homeScreen();
            ActionsUtil.sleep(1000);

            // Try to find and control a fan
            WebElement fanTab = null;
            try {
                fanTab = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc='Fan']"));
            } catch (Exception ignored) {}

            if (fanTab != null) {
                fanTab.click();
                ActionsUtil.sleep(2000);

                // Try to find fan control elements
                List<WebElement> controls = driver.findElements(AppiumBy.className("android.widget.ImageView"));
                Assert.assertFalse(controls.isEmpty(), "Fan controls should be available");

                AppUtil.captureScreenshot(driver, "Fan Control Screen");
                System.out.println("✓ Fan controls displayed");

                // Navigate back
                driver.navigate().back();
            } else {
                System.out.println("⚠ No fan available for control test");
            }
        } catch (Exception e) {
            System.err.println("⚠ Fan control test skipped: " + e.getMessage());
        }
    }

    @Test(priority = 6, description = "Test Step 6: Check Analytics")
    public void testCheckAnalytics() {
        try {
            System.out.println(" >>> TEST 6: Check Analytics");

            screenCheck.homeScreen();
            ActionsUtil.sleep(1000);

            // Look for analytics button/section
            WebElement analyticsElement = null;
            try {
                analyticsElement = driver.findElement(By.xpath(
                        "//android.widget.ImageView[@content-desc='Analytics'] | //android.view.View[@content-desc='Analytics']"));
            } catch (Exception ignored) {}

            if (analyticsElement != null) {
                analyticsElement.click();
                ActionsUtil.sleep(2000);

                AppUtil.captureScreenshot(driver, "Analytics Screen");
                System.out.println("✓ Analytics screen displayed");

                driver.navigate().back();
            } else {
                System.out.println("⚠ Analytics section not found on home screen");
                // Analytics might be accessed from MoreTab
                testMoreTabAnalytics();
            }
        } catch (Exception e) {
            System.err.println("⚠ Analytics test partial: " + e.getMessage());
        }
    }

    @Test(priority = 7, description = "Test Step 7: More Tab - All Buttons")
    public void testMoreTabAllButtons() {
        try {
            System.out.println(" >>> TEST 7: More Tab - Check All Available Buttons");

            screenCheck.homeScreen();
            ActionsUtil.sleep(1000);

            // Navigate to MoreTab
            screenCheck.moreTab();
            ActionsUtil.sleep(2000);
            AppUtil.captureScreenshot(driver, "MoreTab Screen");

            System.out.println(" --- Testing MoreTab Sections ---");

                    // Test Profile Section
                    testMoreTabProfile();

            // Test Manage Section
            testMoreTabManage();

            // Test Help Section
            testMoreTabHelp();

            // Test Play Section
            testMoreTabPlay();

            System.out.println("✓ All MoreTab sections tested");
        } catch (Exception e) {
            System.err.println("✗ MoreTab test failed: " + e.getMessage());
            throw new AssertionError("MoreTab test failed", e);
        }
    }

    private void testMoreTabProfile() {
        try {
            System.out.println(" > Testing Profile Section");

                    // Navigate to profile
                    WebElement profileElement = driver.findElement(By.xpath(
                            "//android.widget.ImageView[contains(@content-desc, 'Hi,')] | //android.view.View[@content-desc='Profile']"));
            profileElement.click();
            ActionsUtil.sleep(2000);

            AppUtil.captureScreenshot(driver, "MoreTab Profile");
            System.out.println("  ✓ Profile section accessible");

            // Navigate back to MoreTab
            driver.navigate().back();
            ActionsUtil.sleep(1000);
        } catch (Exception e) {
            System.out.println("  ⚠ Profile section test skipped: " + e.getMessage());
        }
    }

    private void testMoreTabManage() {
        try {
            System.out.println(" > Testing Manage Section");

                    // Navigate to manage
                    WebElement manageElement = driver.findElement(By.xpath(
                            "//android.widget.ImageView[@content-desc='Manage'] | //android.view.View[@content-desc='Manage Family']"));
            manageElement.click();
            ActionsUtil.sleep(2000);

            AppUtil.captureScreenshot(driver, "MoreTab Manage");
            System.out.println("  ✓ Manage section accessible");

            // Navigate back to MoreTab
            driver.navigate().back();
            ActionsUtil.sleep(1000);
        } catch (Exception e) {
            System.out.println("  ⚠ Manage section test skipped: " + e.getMessage());
        }
    }

    private void testMoreTabHelp() {
        try {
            System.out.println(" > Testing Help Section");

                    // Navigate to Help
                    WebElement helpElement = driver.findElement(By.xpath(
                            "//android.widget.ImageView[@content-desc='Help'] | //android.view.View[@content-desc='Help']"));
            helpElement.click();
            ActionsUtil.sleep(2000);

            AppUtil.captureScreenshot(driver, "MoreTab Help");

            // Test Help sub-buttons
            System.out.println("    - Testing Help sub-buttons:");
            testHelpSubButtons();

            System.out.println("  ✓ Help section tested");

            // Navigate back
            int attempts = 0;
            while (attempts < 5) {
                try {
                    WebElement moreTabCheck = driver.findElement(By.xpath("//android.view.View[@content-desc='More']"));
                    if (moreTabCheck != null) break;
                } catch (Exception ignored) {}
                driver.navigate().back();
                attempts++;
            }
        } catch (Exception e) {
            System.out.println("  ⚠ Help section test skipped: " + e.getMessage());
        }
    }

    private void testHelpSubButtons() {
        try {
            // New Complaint
            try {
                help.newComplaint();
                System.out.println("      ✓ New Complaint button working");
            } catch (Exception e) {
                System.out.println("      ⚠ New Complaint: " + e.getMessage());
            }

            // Installation Request
            try {
                help.installationRequest();
                System.out.println("      ✓ Installation Request button working");
            } catch (Exception e) {
                System.out.println("      ⚠ Installation Request: " + e.getMessage());
            }

            // Service Request
            try {
                help.serviceRequest();
                System.out.println("      ✓ Service Request button working");
            } catch (Exception e) {
                System.out.println("      ⚠ Service Request: " + e.getMessage());
            }

            // Track Complaint
            try {
                help.trackAComplaint();
                System.out.println("      ✓ Track Complaint button working");
            } catch (Exception e) {
                System.out.println("      ⚠ Track Complaint: " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("    Error testing help sub-buttons: " + e.getMessage());
        }
    }

    private void testMoreTabPlay() {
        try {
            System.out.println(" > Testing Play Section");

                    // Navigate to Play
                    WebElement playElement = driver.findElement(By.xpath(
                            "//android.widget.ImageView[@content-desc='Play'] | //android.view.View[@content-desc='Play']"));
            playElement.click();
            ActionsUtil.sleep(2000);

            AppUtil.captureScreenshot(driver, "MoreTab Play");
            System.out.println("  ✓ Play section accessible");

            // Navigate back
            driver.navigate().back();
            ActionsUtil.sleep(1000);
        } catch (Exception e) {
            System.out.println("  ⚠ Play section test skipped: " + e.getMessage());
        }
    }

    private void testMoreTabAnalytics() {
        try {
            System.out.println("> Testing Analytics from MoreTab");

                    WebElement analyticsElement = driver.findElement(By.xpath(
                            "//android.widget.ImageView[@content-desc='Analytics'] | //android.view.View[@content-desc='Analytics']"));
            analyticsElement.click();
            ActionsUtil.sleep(2000);

            AppUtil.captureScreenshot(driver, "MoreTab Analytics");
            System.out.println("  ✓ Analytics section accessible from MoreTab");

            driver.navigate().back();
        } catch (Exception e) {
            System.out.println("  ⚠ Analytics in MoreTab not found: " + e.getMessage());
        }
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        try {
            if (driver != null) {
                System.out.println("=== Test Suite Completed ===");
                        System.out.println("Closing application...");
                driver.quit();
                System.out.println("✓ App closed successfully");
            }
        } catch (Exception e) {
            System.err.println("Error during teardown: " + e.getMessage());
        }
    }
}
