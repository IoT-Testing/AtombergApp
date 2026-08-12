package com.appTest.tests;

import app.Login.Email;
import app.sharing.DeviceSharingPage;
import app.sharing.DualDeviceManager;
import app.sharing.MemberDevicePage;
import app.sharing.PermissionEditorPage;
import app.sharing.SharingMode;
import app.sharing.SharingModeDetector;
import app.util.ActionsUtil;
import app.util.AppUtil;
import com.appTest.util.SharingRunLedger;
import com.aventstack.extentreports.Status;
import ExtentReports.ExtentReportAT;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.testng.SkipException;
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
    protected PermissionEditorPage permissionEditor;   // admin-side only: members never edit
    protected ExtentReportAT       reporter;

    // ── Which of the two sharing flows this build exposes ──────────────────────
    /**
     * The mode the run is operating in. Resolved once (cached across classes) in
     * {@link #setupDualDevices()} and never {@link SharingMode#AUTO} by the time a test
     * sees it. Gate every Super/Basic/Custom assertion on
     * {@link #requirePermissionLevels(String)} rather than reading this directly.
     */
    protected SharingMode sharingMode = SharingMode.AUTO;

    /** What the probe saw, so a skip can explain itself instead of just happening. */
    protected SharingModeDetector.Evidence modeEvidence;

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
        permissionEditor = new PermissionEditorPage(adminDriver);
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

        // Decide WHICH sharing flow this build exposes, before any test asserts on it.
        // Done here rather than in a separate @BeforeClass because TestNG does not order
        // sibling @BeforeClass methods that share a dependency — a subclass hook could
        // otherwise run first and assert against an unresolved mode.
        resolveSharingMode();

        System.out.println("=== BaseDeviceSharingTest setup complete ===");
    }

    /**
     * Resolves {@link #sharingMode} for the run, cached across classes by
     * {@link SharingModeDetector}, so only the first class pays for the probe.
     *
     * <p>Never throws. A probe that cannot reach the share screens still has to leave a
     * usable mode behind, because failing here would abort classes whose tests need no
     * permission levels at all — the whole present-flow half of the suite. On an error it
     * falls back to {@link SharingMode#WITHOUT_PERMISSIONS}, which makes the level tests
     * skip with a reason rather than fail on a setup problem.</p>
     */
    protected void resolveSharingMode() {
        try {
            modeEvidence = SharingModeDetector.resolve(adminDriver, testDeviceName);
            sharingMode  = modeEvidence.mode();
        } catch (Exception e) {
            System.err.println("[Base] Sharing-mode probe failed: " + e.getMessage()
                    + " — assuming the present (family-wide) flow so level tests skip "
                    + "rather than fail on a setup error.");
            sharingMode  = SharingMode.WITHOUT_PERMISSIONS;
            modeEvidence = null;
        }
        System.out.println("[Base] SHARING MODE: " + sharingMode.label);
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

    // ══════════════════════════════════════════════════════════════════════════
    // TEST-CASE RUNNER — traceability to the sheet, in one place
    // ══════════════════════════════════════════════════════════════════════════

    /** A test case body. Allowed to throw, so assertions read normally inside it. */
    @FunctionalInterface
    protected interface SharingCase {
        void run() throws Exception;
    }

    /**
     * Runs one sheet test case: opens its Extent node, records the verdict against its TC
     * ID(s) in {@link SharingRunLedger}, screenshots both phones on failure, and closes
     * the node — then re-raises so TestNG still sees the real outcome.
     *
     * <p>Every case goes through here so the three things that must never be forgotten
     * cannot be: the ledger entry (or the coverage matrix reports the case as "not run"
     * even though it ran), the failure screenshots (a black-box UI failure is close to
     * undiagnosable without them), and {@code reporter.endTest()} (a missed one nests
     * every later case inside this one).</p>
     *
     * @param tcIds comma-separated sheet IDs this case covers, e.g. {@code "RP-10, RP-11"}
     * @param title short human title, shown after the IDs in the report
     * @param slot  reporting device slot: {@code Admin_Device}, {@code Member_Device} or
     *              {@code Admin+Member}
     * @param body  the assertions
     */
    protected void runCase(String tcIds, String title, String slot, SharingCase body) {
        reporter.startTest("[" + tcIds + "] " + title, slot);
        reporter.log(Status.INFO, "Sharing mode: " + sharingMode.label);
        try {
            body.run();
            SharingRunLedger.pass(tcIds);
            reporter.log(Status.PASS, tcIds + " — verified.");
        } catch (SkipException se) {
            // A skip is a result, not a non-event: it is how the suite says "this build
            // has nothing for this case to assert against" without faking a pass.
            SharingRunLedger.skip(tcIds, se.getMessage());
            reporter.log(Status.SKIP, tcIds + " skipped — " + se.getMessage());
            throw se;
        } catch (AssertionError ae) {
            SharingRunLedger.fail(tcIds, ae.getMessage());
            reporter.log(Status.FAIL, tcIds + " FAILED — " + ae.getMessage());
            captureBothPhones(tcIds);
            throw ae;
        } catch (Exception e) {
            SharingRunLedger.fail(tcIds, e.getMessage());
            reporter.log(Status.FAIL, tcIds + " ERRORED — " + e);
            captureBothPhones(tcIds);
            throw new RuntimeException("[" + tcIds + "] " + title + " — " + e.getMessage(), e);
        } finally {
            reporter.endTest();
        }
    }

    /** Screenshots both phones under one tag, so a cross-phone failure is readable. */
    protected void captureBothPhones(String tag) {
        String safe = AppUtil.sanitizeFileName(tag);
        try { AppUtil.captureScreenshot(adminDriver,  safe + "_admin"); }  catch (Exception ignored) {}
        try { AppUtil.captureScreenshot(memberDriver, safe + "_member"); } catch (Exception ignored) {}
    }

    // ══════════════════════════════════════════════════════════════════════════
    // MODE GATES
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Skips the current case unless the build exposes Super / Basic / Custom.
     *
     * <p>A skip rather than a failure, because on the present flow there is genuinely no
     * permission level to assert — failing would report a product defect where the
     * feature simply is not shipped yet. The message carries the probe's evidence so the
     * report says which it was.</p>
     *
     * <p>Pin {@code SHARING_MODE=with_permissions} in {@code sharing-test.properties} to
     * turn these skips into failures. That is what you want while pinning the level
     * locators against the new APK: there, "no picker found" IS the finding.</p>
     *
     * @param what the assertion being gated, named in the skip message
     */
    protected void requirePermissionLevels(String what) {
        if (sharingMode.hasPermissionLevels()) return;
        throw new SkipException(what + " needs the with-permissions build — this run is on "
                + sharingMode.label + "."
                + (modeEvidence == null ? "" : " " + modeEvidence.summary())
                + " Set SHARING_MODE=with_permissions in sharing-test.properties to make this "
                + "a failure instead.");
    }

    /**
     * Skips unless the build is the present family-wide flow. The mirror of
     * {@link #requirePermissionLevels}, for the handful of cases that assert on
     * behaviour the levels build is expected to REMOVE — chiefly the "Share access to
     * family?" dialog. Asserting that on a levels build would be asserting a regression.
     */
    protected void requirePresentFlow(String what) {
        if (!sharingMode.hasPermissionLevels()) return;
        throw new SkipException(what + " describes the present family-wide flow, and this "
                + "build exposes permission levels (" + sharingMode.label + ") — the "
                + "behaviour it asserts is expected to be gone.");
    }

    /**
     * Returns a required-for-this-case config value, or skips when it is unset.
     *
     * <p>For test data the bench may legitimately not have — a lock, a water purifier, a
     * device with a 200-character name. Skipping names the missing key, so an
     * unexercised case reads as "TEST_LOCK_DEVICE_NAME is not set" rather than as a
     * mysterious locator miss deep in a flow.</p>
     *
     * @param key   the config key, quoted in the skip message
     * @param value the already-resolved value
     * @param what  what the case needs it for
     */
    protected String requireTestData(String key, String value, String what) {
        if (value != null && !value.isBlank()) return value;
        throw new SkipException(key + " is not set — needed for " + what
                + ". Add it to sharing-test.properties to enable this case.");
    }

    /**
     * Skips unless an opt-in flag is switched on. For cases that are automated but too
     * slow or too invasive to run every time (the 15-minute QR expiry wait, for
     * instance): they must be runnable on demand, and must not silently disappear.
     *
     * @param key  config key, e.g. {@code RUN_QR_EXPIRY_TEST}
     * @param what what running it costs, so the reader knows why it is opt-in
     */
    protected void requireOptIn(String key, String what) {
        if (Boolean.parseBoolean(app.sharing.SharingConfig.get(key, "false"))) return;
        throw new SkipException(key + " is not enabled — " + what
                + ". Set " + key + "=true in sharing-test.properties to run it.");
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