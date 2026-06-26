package com.appTest.tests;

import app.Login.Email;
import app.sharing.DeviceSharingPage;
import app.sharing.DualDeviceManager;
import app.sharing.MemberDevicePage;
import app.util.ActionsUtil;
import app.util.AppUtil;
import ExtentReports.ExtentReportAT;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.testng.annotations.*;

/**
 * BaseDeviceSharingTest – Base class for all Device Sharing & Permission Management
 * tests that require two physical phones.
 *
 * ── Two-phone architecture ────────────────────────────────────────────────────
 *   adminDriver  → Phone 1 — logged in as Admin (home admin account)
 *   memberDriver → Phone 2 — logged in as Member (non-admin family member)
 *
 * ── Environment variables needed ─────────────────────────────────────────────
 *   ADMIN_DEVICE_UDID       UDID of admin phone  (from `adb devices`)
 *   MEMBER_DEVICE_UDID      UDID of member phone (from `adb devices`)
 *   APPIUM_URL              Appium server URL (default: http://127.0.0.1:4723)
 *   ADMIN_EMAIL             Admin account email
 *   ADMIN_PASSWORD          Admin account password
 *   MEMBER_EMAIL            Member account email (non-admin, already in the home)
 *   MEMBER_PASSWORD         Member account password
 *   TEST_FAN_DEVICE_NAME    Display name of the fan to test with (e.g. "My Aris Fan")
 *   TEST_MEMBER_DISPLAY_NAME Display name of the member in the admin's family list
 *
 * ── Running from command line ─────────────────────────────────────────────────
 *   # Discover connected device UDIDs:
 *   adb devices
 *
 *   # Export credentials:
 *   export ADMIN_DEVICE_UDID=R3CTN03VYZD
 *   export MEMBER_DEVICE_UDID=emulator-5554
 *   export ADMIN_EMAIL=admin@test.com
 *   export ADMIN_PASSWORD=AdminPass@1
 *   export MEMBER_EMAIL=member@test.com
 *   export MEMBER_PASSWORD=MemberPass@1
 *   export TEST_FAN_DEVICE_NAME="My Aris Fan"
 *   export TEST_MEMBER_DISPLAY_NAME="Rohit Bhagat"
 *
 *   # Run sharing tests:
 *   mvn test -DsuiteFile=testng-sharing.xml
 *
 * ── No duplicate @Listeners ───────────────────────────────────────────────────
 * Listeners are declared here only. Subclasses must NOT redeclare @Listeners.
 */
@Listeners({
        com.appTest.listeners.TestListeners.class,
        com.appTest.listeners.DashboardReporter.class
})
public abstract class BaseDeviceSharingTest {

    // ── Driver & page objects ─────────────────────────────────────────────────
    protected DualDeviceManager    deviceManager;
    protected AndroidDriver        adminDriver;
    protected AndroidDriver        memberDriver;
    protected DeviceSharingPage    adminSharingPage;
    protected MemberDevicePage     memberDevicePage;
    protected ExtentReportAT       reporter;

    // ── Test data (from env vars) ─────────────────────────────────────────────
    protected String adminEmail;
    protected String adminPassword;
    protected String memberEmail;
    protected String memberPassword;
    protected String testDeviceName;
    protected String testMemberDisplayName;

    // ── Lifecycle ─────────────────────────────────────────────────────────────

    @BeforeClass(alwaysRun = true)
    public void setupDualDevices() {
        // Load env-driven test data
        adminEmail             = requireEnv("ADMIN_EMAIL");
        adminPassword          = requireEnv("ADMIN_PASSWORD");
        memberEmail            = requireEnv("MEMBER_EMAIL");
        memberPassword         = requireEnv("MEMBER_PASSWORD");
        testDeviceName         = requireEnv("TEST_FAN_DEVICE_NAME");
        testMemberDisplayName  = requireEnv("TEST_MEMBER_DISPLAY_NAME");

        // Detect connected devices and print them for diagnostics
        System.out.println("=== Connected ADB devices ===");
        DualDeviceManager.detectConnectedDevices()
                .forEach(udid -> System.out.println("  " + udid));

        // Create both drivers
        deviceManager   = DualDeviceManager.create();
        adminDriver     = deviceManager.adminDriver();
        memberDriver    = deviceManager.memberDriver();
        adminSharingPage = new DeviceSharingPage(adminDriver);
        memberDevicePage = new MemberDevicePage(memberDriver);
        reporter         = new ExtentReportAT("DualDevice_Sharing");

        // Ensure both phones are logged in
        ensureAdminLoggedIn();
        ensureMemberLoggedIn();

        System.out.println("=== BaseDeviceSharingTest setup complete ===");
    }

    @AfterClass(alwaysRun = true)
    public void teardownDualDevices() {
        if (deviceManager != null) deviceManager.quitAll();
        if (reporter      != null) reporter.close();
        System.out.println("=== BaseDeviceSharingTest teardown complete ===");
    }

    // ── Login helpers ─────────────────────────────────────────────────────────

    /**
     * Ensures the admin phone is logged in with the admin account.
     * If a login screen is detected, performs email login.
     */
    protected void ensureAdminLoggedIn() {
        System.out.println("[Base] Checking admin phone login state...");
        if (isOnLoginScreen(adminDriver)) {
            System.out.println("[Base] Admin phone: login required.");
            try {
                new Email(adminDriver).email(adminEmail, adminPassword);
                ActionsUtil.SSleep(5);
            } catch (Exception e) {
                throw new RuntimeException("Admin login failed: " + e.getMessage(), e);
            }
        } else {
            System.out.println("[Base] Admin phone: already logged in.");
        }
        AppUtil.captureScreenshot(adminDriver, "admin_logged_in");
    }

    /**
     * Ensures the member phone is logged in with the member account.
     */
    protected void ensureMemberLoggedIn() {
        System.out.println("[Base] Checking member phone login state...");
        if (isOnLoginScreen(memberDriver)) {
            System.out.println("[Base] Member phone: login required.");
            try {
                new Email(memberDriver).email(memberEmail, memberPassword);
                ActionsUtil.SSleep(5);
            } catch (Exception e) {
                throw new RuntimeException("Member login failed: " + e.getMessage(), e);
            }
        } else {
            System.out.println("[Base] Member phone: already logged in.");
        }
        AppUtil.captureScreenshot(memberDriver, "member_logged_in");
    }

    /**
     * Returns true if the given driver is currently showing the login screen.
     */
    protected boolean isOnLoginScreen(AndroidDriver driver) {
        By loginIndicator = org.openqa.selenium.By.xpath(
                "//android.view.View[@content-desc=\"Experience smart living \n with Atomberg\"]");
        try { return driver.findElement(loginIndicator).isDisplayed(); }
        catch (Exception e) { return false; }
    }

    // ── Shared test utilities ─────────────────────────────────────────────────

    /**
     * Reads a required environment variable. Throws with a clear message if absent.
     */
    protected static String requireEnv(String key) {
        String v = System.getenv(key);
        if (v == null || v.isBlank())
            throw new IllegalStateException(
                    "Missing required environment variable for device sharing tests: " + key
                            + "\nSee BaseDeviceSharingTest Javadoc for setup instructions.");
        return v;
    }

    /**
     * Pauses briefly between admin action and member verification.
     * The app is event-driven; typically 2–4 s is enough for the permission
     * update to propagate and the member device to reflect the change.
     */
    protected void waitForPermissionPropagation() {
        ActionsUtil.SSleep(4);
    }
}