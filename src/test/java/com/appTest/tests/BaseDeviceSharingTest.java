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
 *   Required:
 *   ADMIN_DEVICE_UDID       UDID of admin phone  (from `adb devices`)
 *   MEMBER_DEVICE_UDID      UDID of member phone (from `adb devices`)
 *   TEST_FAN_DEVICE_NAME    Display name of the fan to test with (e.g. "My Aris Fan")
 *   TEST_MEMBER_DISPLAY_NAME Display name of the member in the admin's family list
 *
 *   Optional:
 *   APPIUM_URL              Appium server URL (default: http://127.0.0.1:4723)
 *   ADMIN_EMAIL / ADMIN_PASSWORD    Fallback login ONLY — unused while the admin
 *                                   phone stays logged in (noReset=true)
 *   MEMBER_EMAIL / MEMBER_PASSWORD  Fallback login ONLY — unused while the member
 *                                   phone stays logged in
 *   ADMIN_DISPLAY_NAME      Enables the §6.1 admin-row-greyed-out edge-case test
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
    protected String testDeviceName;          // fan (required — back-compat)
    protected String testLockDeviceName;      // optional — Flow 2 lock iteration
    protected String testPurifierDeviceName;  // optional — Flow 2 water-purifier iteration
    protected String testMemberDisplayName;

    // ── Role-guard hints (detect swapped phones) ──────────────────────────────
    protected String adminProfileHint;
    protected String adminFamilyHint;
    protected String memberProfileHint;
    protected String memberPresentFamily;

    // ── Lifecycle ─────────────────────────────────────────────────────────────

    @BeforeClass(alwaysRun = true)
    public void setupDualDevices() {
        // Required test targets — the tests must know which fan / which member to act on.
        testDeviceName         = requireEnv("TEST_FAN_DEVICE_NAME");
        testMemberDisplayName  = requireEnv("TEST_MEMBER_DISPLAY_NAME");

        // Optional device names for the Flow 2 per-device loop. When a name is
        // unset the corresponding device type is skipped in DeviceShareLongPressFlowTest.
        testLockDeviceName     = optionalEnv("TEST_LOCK_DEVICE_NAME",      null);
        testPurifierDeviceName = optionalEnv("TEST_PURIFIER_DEVICE_NAME",  null);

        // Role-guard hints (defaults match the standard admin/member accounts).
        adminProfileHint    = optionalEnv("ADMIN_PROFILE_HINT",     "Admin");
        adminFamilyHint     = optionalEnv("ADMIN_FAMILY_HINT",      "Main Family");
        memberProfileHint   = optionalEnv("MEMBER_PROFILE_HINT",    "Member");
        memberPresentFamily = optionalEnv("MEMBER_PRESENT_FAMILY",  "Secondary Family");

        // Account credentials are OPTIONAL — both phones are expected to stay
        // logged in (noReset=true). These are only used as a fallback IF a login
        // screen is unexpectedly detected. Leave them unset for a normal run.
        adminEmail             = optionalEnv("ADMIN_EMAIL",     null);
        adminPassword          = optionalEnv("ADMIN_PASSWORD",  null);
        memberEmail            = optionalEnv("MEMBER_EMAIL",    null);
        memberPassword         = optionalEnv("MEMBER_PASSWORD", null);

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
        // Register every device slot the sharing tests report under, so
        // reporter.startTest(..., slot) can find its parent node.
        reporter.registerDevice("Admin_Device");
        reporter.registerDevice("Member_Device");
        reporter.registerDevice("Admin+Member");

        // Ensure both phones are logged in
        ensureAdminLoggedIn();
        ensureMemberLoggedIn();

        // Fail fast if the phones/accounts are swapped (admin UDID → member phone).
        verifyDeviceRoles();

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
            if (adminEmail == null || adminPassword == null)
                throw new IllegalStateException(
                        "Admin phone is on the LOGIN screen but ADMIN_EMAIL/ADMIN_PASSWORD "
                                + "were not provided. Keep the admin account logged in, or set "
                                + "those env vars to enable fallback login.");
            System.out.println("[Base] Admin phone: login required (using fallback credentials).");
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
            if (memberEmail == null || memberPassword == null)
                throw new IllegalStateException(
                        "Member phone is on the LOGIN screen but MEMBER_EMAIL/MEMBER_PASSWORD "
                                + "were not provided. Keep the member account logged in, or set "
                                + "those env vars to enable fallback login.");
            System.out.println("[Base] Member phone: login required (using fallback credentials).");
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
        // Delegates to SharingConfig: sharing-test.properties → env var → -D property.
        return app.sharing.SharingConfig.require(key);
    }

    /**
     * Reads an optional config value, returning {@code fallback} when it is
     * unset. Resolves via SharingConfig (properties file, then env var). Used for
     * the account credentials, which are only needed as a login fallback when the
     * phones are not already logged in.
     */
    protected static String optionalEnv(String key, String fallback) {
        return app.sharing.SharingConfig.get(key, fallback);
    }

    /**
     * Pauses briefly between admin action and member verification.
     * The app is event-driven; typically 2–4 s is enough for the permission
     * update to propagate and the member device to reflect the change.
     */
    protected void waitForPermissionPropagation() {
        ActionsUtil.SSleep(4);
    }

    // ── Role guard ──────────────────────────────────────────────────────────────

    /**
     * Fail-fast check that the two phones are not swapped. Each driver is brought
     * to its home dashboard (best-effort) and its screen is scanned for the
     * configured admin/member hints (family name + profile label). The suite
     * aborts ONLY on a positive swap signal — the ADMIN driver showing member
     * markers AND the MEMBER driver showing admin markers — so a screen that
     * simply lacks the hints never produces a false failure.
     */
    protected void verifyDeviceRoles() {
        System.out.println("[Base] Verifying admin/member phone roles...");
        bestEffortHome(adminDriver);
        bestEffortHome(memberDriver);

        String adminSrc  = pageSourceOrEmpty(adminDriver);
        String memberSrc = pageSourceOrEmpty(memberDriver);

        boolean adminHasAdminMarker  = contains(adminSrc,  adminProfileHint)  || contains(adminSrc,  adminFamilyHint);
        boolean adminHasMemberMarker = contains(adminSrc,  memberProfileHint) || contains(adminSrc,  memberPresentFamily);
        boolean memberHasMemberMarker= contains(memberSrc, memberProfileHint) || contains(memberSrc, memberPresentFamily);
        boolean memberHasAdminMarker = contains(memberSrc, adminProfileHint)  || contains(memberSrc, adminFamilyHint);

        boolean swapped = adminHasMemberMarker && !adminHasAdminMarker
                && memberHasAdminMarker && !memberHasMemberMarker;

        if (swapped) {
            String adminUdid  = app.sharing.SharingConfig.get(DualDeviceManager.ENV_ADMIN_UDID);
            String memberUdid = app.sharing.SharingConfig.get(DualDeviceManager.ENV_MEMBER_UDID);
            AppUtil.captureScreenshot(adminDriver,  "role_guard_admin_looks_like_member");
            AppUtil.captureScreenshot(memberDriver, "role_guard_member_looks_like_admin");
            throw new IllegalStateException(
                    "Admin/Member phones appear SWAPPED: the ADMIN driver (UDID=" + adminUdid
                    + ") is showing MEMBER markers and the MEMBER driver (UDID=" + memberUdid
                    + ") is showing ADMIN markers. Swap ADMIN_DEVICE_UDID and MEMBER_DEVICE_UDID "
                    + "in sharing-test.properties (admin = the phone logged into '" + adminFamilyHint
                    + "').");
        }

        if (adminHasAdminMarker && memberHasMemberMarker) {
            System.out.println("[Base] Role check OK — admin/member phones correctly mapped.");
        } else {
            System.out.println("[Base] Role check inconclusive (hints not visible on the current "
                    + "screens) — proceeding. Set ADMIN_/MEMBER_ hints to enable a stronger check.");
        }
    }

    /**
     * Best-effort: land on a root/home screen. Delegates to
     * {@link AppUtil#ensureAppHome(AndroidDriver)}, which never presses BACK off
     * the app (a blind back-loop closes the app on a root screen — it looks like
     * "the member phone's app shut itself down").
     */
    private void bestEffortHome(AndroidDriver driver) {
        AppUtil.ensureAppHome(driver);
        clickIfPresent(driver, app.resources.Locators.Android.HomeLocators.DEVICES);
        ActionsUtil.SSleep(1);
    }

    private static boolean contains(String haystack, String needle) {
        return needle != null && !needle.isBlank() && haystack.contains(needle);
    }

    private static String pageSourceOrEmpty(AndroidDriver driver) {
        try { return driver.getPageSource(); }
        catch (Exception e) { return ""; }
    }

    private static boolean isPresent(AndroidDriver driver, By locator) {
        try { return driver.findElement(locator).isDisplayed(); }
        catch (Exception e) { return false; }
    }

    private static void clickIfPresent(AndroidDriver driver, By locator) {
        try { if (driver.findElement(locator).isDisplayed()) driver.findElement(locator).click(); }
        catch (Exception ignored) {}
    }
}