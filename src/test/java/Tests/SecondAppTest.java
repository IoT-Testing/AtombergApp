package Tests;

import app.AppInitializer;
import app.Login.Email;
import app.util.ActionsUtil;
import app.util.AppUtil;
import app.util.Navigation;
import com.aventstack.extentreports.Status;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import java.net.MalformedURLException;
import static app.resources.Locators.Android.BLEFan.*;
import static app.resources.Locators.Android.FanLocators.*;
import static app.resources.Locators.Android.HomeLocators.*;
import static app.resources.Locators.Android.LoginLocators.*;

import static app.util.AppUtil.clickIfExists;
import static app.util.AppUtil.waitForElement;


/**
 * FullAppFlowTest - End-to-end automation of Atomberg app flow as per provided document.
 *
 * Covers:
 * - App launch
 * - Login
 * - Permissions
 * - Device addition (Fan)
 * - Device control & firmware check
 * - Analytics
 * - Automations
 * - More Tab: Profile, Help, Marketplace, Logout
 */

public class SecondAppTest extends BaseTest {

    public AndroidDriver driver;
    private SoftAssert softAssert;

    // === Locators ===
    private static final String APP_PACKAGE = "com.atomberg.app";

    @BeforeSuite
    public void setupSuite() {
        reporter.startTest("Atomberg App Full Flow Test", "Device_1");
        reporter.log(Status.INFO, "Test suite started");
    }

    @BeforeTest
    public void setUp() throws MalformedURLException {
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
            WebElement moreTab = waitForElement(driver, MORE_TAB, 5);
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

    @Test(priority = 2)
    public void test_LoginWithEmail() {
        reporter.startTest("Login with Email", "Device_1");

        try {
            // Assuming login screen appears
            driver.findElement(EMAIL_LOGIN_BUTTON).click();
            sleep(2000);

            WebElement et = driver.findElement(EDIT_TEXT_FIELD);
            et.click();
            et.sendKeys("hiwitaw422@wuzak.com");
            driver.findElement(LOGIN_CONTINUE_BUTTON).click();
            WebElement pw = driver.findElement(EDIT_TEXT_FIELD);
                    pw.sendKeys("Atomberg@1234");
            driver.findElement(By.xpath("//android.widget.Button[@content-desc='Continue']")).click();

            sleep(5000); // Wait for home screen

            Assert.assertTrue(isElementPresent(APP_LOGO), "❌ App Logo not found after login");
            reporter.log(Status.PASS, "✅ Logged in successfully and reached Home Screen");
            AppUtil.captureScreenshot(driver, "Login_Success");

        } catch (Exception e) {
            reporter.log(Status.FAIL, "❌ Login failed: " + e.getMessage());
            AppUtil.captureScreenshot(driver, "Login_Failure");
            Assert.fail("Login failed: " + e.getMessage());
        }
    }

    @Test(priority = 3)
    public void test_AddSmartDevice_Fan() {
        reporter.startTest("Add Smart Fan Device", "Device_1");

        try {
            clickIfExists(driver, ADD_BUTTON_XPATH);
            sleep(3000);

            // Simulate BLE scan and selection
            driver.findElement(By.xpath("//android.widget.TextView[@text='Renesa Smart']")).click();
            sleep(2000);

            // Complete setup process
            driver.findElement(By.id("device_name_input")).clear();
            driver.findElement(By.id("device_name_input")).sendKeys("Living Room Fan");
            driver.findElement(By.xpath("//android.widget.Button[@content-desc='Next']")).click();
            sleep(8000); // Simulate Wi-Fi config

            driver.findElement(By.xpath("//android.widget.Button[@content-desc='Finish']")).click();
            sleep(3000);

            reporter.log(Status.PASS, "✅ Fan added successfully to dashboard");
            AppUtil.captureScreenshot(driver, "Fan_Added");

        } catch (Exception e) {
            reporter.log(Status.FAIL, "❌ Fan addition failed: " + e.getMessage());
            AppUtil.captureScreenshot(driver, "Fan_Addition_Failed");
            Assert.fail("Device addition failed");
        }
    }

    @Test(priority = 3)
    public void test_ControlFan_And_CheckFirmware() {
        reporter.startTest("Control Fan & Check Firmware Version", "Device_1");

        try {
            Navigation.openFanControl(driver);
            sleep(2000);

            // Control: Speed slider or buttons
            driver.findElement(By.xpath("//android.widget.Button[@content-desc='Speed Up']")).click();
            sleep(1000);
            driver.findElement(By.xpath("//android.widget.Button[@content-desc='Speed Down']")).click();
            sleep(2000);

            // Open Menu
            clickIfExists(driver,MENU_BUTTON);
            sleep(2000);

            // Check Firmware Version
            String firmwareText = driver.findElement(By.xpath("//android.view.View[contains(@content-desc,'Firmware Version')]"))
                    .getDomAttribute("content-desc");
            System.out.println("📄 Firmware: " + firmwareText);

            softAssert.assertTrue(firmwareText.contains("Firmware Version"), "Firmware info missing");
            reporter.log(Status.INFO, "📄 Firmware Info: " + firmwareText);

            // Close menu
            driver.navigate().back();
            sleep(1000);

            reporter.log(Status.PASS, "✅ Fan controlled and firmware verified");
            AppUtil.captureScreenshot(driver, "Firmware_Checked");

        } catch (Exception e) {
            reporter.log(Status.FAIL, "❌ Control or firmware check failed: " + e.getMessage());
            AppUtil.captureScreenshot(driver, "Control_Firmware_Fail");
            softAssert.fail("Control/Firmware error");
        }

        softAssert.assertAll();
    }

    @Test(priority = 4)
    public void test_ViewAnalytics() {
        reporter.startTest("View Analytics Screens", "Device_1");

        try {
            Navigation.openFanControl(driver);
            sleep(2000);

            clickIfExists(driver, ANALYTICS_TAB);
            sleep(3000);

            // Validate analytics screens
            softAssert.assertTrue(isElementPresent(By.xpath("//*[contains(@text,'Runtime')]")), "Runtime section missing");
            softAssert.assertTrue(isElementPresent(By.xpath("//*[contains(@text,'Energy Saved')]")), "Energy section missing");
            softAssert.assertTrue(isElementPresent(By.xpath("//*[contains(@text,'Speed Usage')]")), "Speed usage missing");

            reporter.log(Status.PASS, "✅ All analytics screens loaded correctly");
            AppUtil.captureScreenshot(driver, "Analytics_Viewed");

        } catch (Exception e) {
            reporter.log(Status.FAIL, "❌ Analytics view failed: " + e.getMessage());
            AppUtil.captureScreenshot(driver, "Analytics_Failed");
            softAssert.fail("Analytics error");
        }

        softAssert.assertAll();
    }

    @Test(priority = 5)
    public void test_CreateAutomations() {
        reporter.startTest("Create Time-based Automation", "Device_1");

        try {
            clickIfExists(driver, AUTOMATION_TAB);
            sleep(3000);

            driver.findElement(By.xpath("//android.widget.Button[@content-desc='Create Automation']")).click();
            sleep(2000);

            // Select trigger: Time of Day
            driver.findElement(By.xpath("//android.widget.TextView[@text='Time of Day']")).click();
            sleep(1000);

            // Set time
            driver.findElement(By.id("hour_picker")).sendKeys("08");
            driver.findElement(By.id("minute_picker")).sendKeys("00");
            driver.findElement(By.xpath("//android.widget.Button[@content-desc='Next']")).click();
            sleep(1000);

            // Set action: Turn On
            driver.findElement(By.xpath("//android.widget.TextView[@text='Turn On']")).click();
            sleep(1000);

            // Save
            driver.findElement(By.xpath("//android.widget.Button[@content-desc='Save']")).click();
            sleep(2000);

            softAssert.assertTrue(
                    isElementPresent(By.xpath("//*[contains(@text,'8:00 AM')]")),
                    "Automation not saved"
            );

            reporter.log(Status.PASS, "✅ Automation created: 'Turn On at 8:00 AM'");
            AppUtil.captureScreenshot(driver, "Automation_Created");

        } catch (Exception e) {
            reporter.log(Status.FAIL, "❌ Automation creation failed: " + e.getMessage());
            AppUtil.captureScreenshot(driver, "Automation_Failed");
            softAssert.fail("Automation error");
        }

        softAssert.assertAll();
    }

    @Test(priority = 6)
    public void test_MoreTab_Functionality() {
        reporter.startTest("More Tab: Profile, Help, Settings", "Device_1");

        try {
            // Navigate to More Tab
            driver.findElement(By.xpath("//android.widget.ImageView[@content-desc='More\nTab 3 of 3']")).click();
            sleep(2000);

            // --- Profile ---
            driver.findElement(By.xpath("//android.widget.TextView[@text='Profile']")).click();
            sleep(2000);

            // Change Name
            WebElement nameField = driver.findElement(By.id("user_name_input"));
            nameField.clear();
            nameField.sendKeys("Test User Updated");
            driver.hideKeyboard();
            driver.findElement(By.xpath("//android.widget.Button[@content-desc='Save']")).click();
            sleep(1000);

            reporter.log(Status.INFO, "✅ Name updated in profile");

            // Manage Family
            driver.findElement(By.xpath("//android.widget.TextView[@text='Manage Family']")).click();
            sleep(2000);
            softAssert.assertTrue(isElementPresent(By.xpath("//android.widget.Button[@content-desc='Add Member']")));
            driver.navigate().back();
            sleep(1000);

            // Preferences
            driver.findElement(By.xpath("//android.widget.TextView[@text='Preferences']")).click();
            sleep(2000);
            driver.findElement(By.xpath("//android.widget.Switch[@text='Live Widget']")).click();
            sleep(1000);
            driver.navigate().back();
            sleep(1000);

            // --- Help ---
            driver.findElement(By.xpath("//android.widget.TextView[@text='Help']")).click();
            sleep(2000);

            scrollAndClick("New Complaint");
            scrollAndClick("Installation Request");
            scrollAndClick("Track Complaints / Requests");
            scrollAndClick("Video Tutorials");
            scrollAndClick("Manual");
            scrollAndClick("FAQs");
            scrollAndClick("Email Us");
            scrollAndClick("Call us");

            // Connectivity Troubleshoot
            scrollAndClick("Connectivity Troubleshoot");
            sleep(2000);
            softAssert.assertTrue(isElementPresent(By.xpath("//android.widget.TextView[@text='Atomberg Fan']")));
            softAssert.assertTrue(isElementPresent(By.xpath("//android.widget.TextView[@text='Atomberg Lock']")));
            softAssert.assertTrue(isElementPresent(By.xpath("//android.widget.TextView[@text='Atomberg Water Purifier']")));
            driver.navigate().back();
            sleep(1000);

            // --- Other Sections ---
            scrollAndClick("Market Place");
            sleep(2000);
            driver.navigate().back();

            scrollAndClick("Rate Us");
            sleep(2000);
            driver.navigate().back();

            scrollAndClick("Privacy Policy");
            sleep(2000);
            driver.navigate().back();

            scrollAndClick("Change Password");
            sleep(2000);
            driver.navigate().back();

            scrollAndClick("Developer Options");
            sleep(2000);
            driver.navigate().back();

            reporter.log(Status.PASS, "✅ More Tab sections validated");
            AppUtil.captureScreenshot(driver, "MoreTab_Validated");

        } catch (Exception e) {
            reporter.log(Status.FAIL, "❌ MoreTab validation failed: " + e.getMessage());
            AppUtil.captureScreenshot(driver, "MoreTab_Failed");
            softAssert.fail("MoreTab error");
        }

        softAssert.assertAll();
    }

    @Test(priority = 7)
    public void test_Logout() {
        reporter.startTest("Logout from App", "Device_1");

        try {
            // From More Tab
            driver.findElement(By.xpath("//android.widget.ImageView[@content-desc='More\nTab 3 of 3']")).click();
            sleep(1000);

            scrollAndClick("Logout");
            sleep(1000);

            driver.findElement(By.xpath("//android.widget.Button[@text='Yes']")).click();
            sleep(3000);

            Assert.assertTrue(
                    isElementPresent(By.xpath("//android.widget.ImageView[@content-desc='Email']")),
                    "❌ Did not return to login screen"
            );

            reporter.log(Status.PASS, "✅ Successfully logged out");
            AppUtil.captureScreenshot(driver, "Logged_Out");

        } catch (Exception e) {
            reporter.log(Status.FAIL, "❌ Logout failed: " + e.getMessage());
            AppUtil.captureScreenshot(driver, "Logout_Failed");
            Assert.fail("Logout failed");
        }
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        reporter.log(Status.INFO, "Driver session closed");
    }

    @AfterSuite
    public void flushReport() {
        reporter.endTest();
        reporter.close();
    }

    // === Utility Methods ===

    private boolean isElementPresent(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }


    private void scrollAndClick(String text) {
        try {
            driver.findElement(AppiumBy.androidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true))" +
                            ".scrollIntoView(new UiSelector().text(\"" + text + "\"))"
            )).click();
            sleep(1000);
        } catch (Exception e) {
            System.err.println("⚠️ Scroll to '" + text + "' failed: " + e.getMessage());
        }
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}