package app.sharing;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;

import static app.resources.AppInfo.ATOMBERG_HOME;
import static app.resources.AppInfo.ATOMBERG_ACTIVITY;

/**
 * DualDeviceManager – creates and manages two AndroidDriver instances for two
 * physical Android phones connected to the same laptop via USB.
 * ── Physical setup ────────────────────────────────────────────────────────────
 * 1. Connect both phones via USB.
 * 2. Enable "USB Debugging" on both (Developer Options).
 * 3. Run:  adb devices
 *    You will see two entries, e.g.:
 *      UDID_PHONE_1   device
 *      UDID_PHONE_2   device
 * 4. Set environment variables (or pass directly to DualDeviceManager):
 *      ADMIN_DEVICE_UDID   = first device UDID  (Admin account will log in here)
 *      MEMBER_DEVICE_UDID  = second device UDID (Member account will log in here)
 * ── Appium server ────────────────────────────────────────────────────────────
 * A SINGLE Appium server handles both drivers. Each driver gets its own session
 * because each driver creation call specifies a different `udid` capability.
 * No separate servers needed.
 * ── Role mapping ─────────────────────────────────────────────────────────────
 *   adminDriver  → Admin (home admin) account – shares devices, edits permissions
 *   memberDriver → Non-admin (recipient) account – receives shared devices
 * Usage:
 *   DualDeviceManager mgr = DualDeviceManager.create();
 *   AndroidDriver admin  = mgr.adminDriver();
 *   AndroidDriver member = mgr.memberDriver();
 *   // ... run tests ...
 *   mgr.quitAll();
 */
public class DualDeviceManager {

    // Environment variable names — set these before running tests
    public static final String ENV_ADMIN_UDID  = "ADMIN_DEVICE_UDID";
    public static final String ENV_MEMBER_UDID = "MEMBER_DEVICE_UDID";
    public static final String ENV_APPIUM_URL  = "APPIUM_URL";

    private final AndroidDriver adminDriver;
    private final AndroidDriver memberDriver;

    // ── Factory ───────────────────────────────────────────────────────────────

    /**
     * Creates a DualDeviceManager by reading UDIDs from environment variables.
     *
     * @throws IllegalStateException if ADMIN_DEVICE_UDID or MEMBER_DEVICE_UDID are not set
     * @throws RuntimeException      if driver creation fails
     */
    public static DualDeviceManager create() {
        String adminUdid  = requireEnv(ENV_ADMIN_UDID);
        String memberUdid = requireEnv(ENV_MEMBER_UDID);
        String appiumUrl  = optionalEnv(ENV_APPIUM_URL, "http://127.0.0.1:4723");
        return new DualDeviceManager(adminUdid, memberUdid, appiumUrl);
    }

    /**
     * Creates a DualDeviceManager with explicit UDID and server values.
     * Useful when called from a TestNG @BeforeSuite / @BeforeClass.
     */
    public static DualDeviceManager create(String adminUdid, String memberUdid, String appiumUrl) {
        return new DualDeviceManager(adminUdid, memberUdid, appiumUrl);
    }

    // ── Constructor ───────────────────────────────────────────────────────────

    private DualDeviceManager(String adminUdid, String memberUdid, String appiumUrl) {
        System.out.println("[DualDeviceManager] Admin  UDID: " + adminUdid);
        System.out.println("[DualDeviceManager] Member UDID: " + memberUdid);
        System.out.println("[DualDeviceManager] Appium URL : " + appiumUrl);

        URL url = parseUrl(appiumUrl);

        // Admin phone – creates session 1 on the Appium server
        adminDriver  = buildDriver(url, adminUdid,  "Admin_Device");

        // Member phone – creates session 2 on the SAME Appium server
        memberDriver = buildDriver(url, memberUdid, "Member_Device");

        System.out.println("[DualDeviceManager] Both drivers created successfully.");
    }

    // ── Accessors ─────────────────────────────────────────────────────────────

    /**
     * Returns the driver for the Admin phone.
     * The admin account should be logged in on this device.
     */
    public AndroidDriver adminDriver()  { return adminDriver; }

    /**
     * Returns the driver for the Member phone.
     * The non-admin member account should be logged in on this device.
     */
    public AndroidDriver memberDriver() { return memberDriver; }

    // ── Lifecycle ─────────────────────────────────────────────────────────────

    /**
     * Quits both drivers. Should be called in @AfterClass / @AfterSuite.
     * Logs errors but does not rethrow so teardown always completes.
     */
    public void quitAll() {
        quit(adminDriver,  "Admin");
        quit(memberDriver, "Member");
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /**
     * Detects connected devices via `adb devices`.
     * Prints them to stdout to help the user identify UDIDs.
     * Call this before creating a DualDeviceManager if UDIDs are unknown.
     */
    public static List<String> detectConnectedDevices() {
        try {
            Process p = Runtime.getRuntime().exec("adb devices");
            String output = new String(p.getInputStream().readAllBytes());
            List<String> udids = java.util.Arrays.stream(output.split("\n"))
                    .filter(line -> line.contains("\tdevice"))
                    .map(line -> line.split("\t")[0].trim())
                    .filter(udid -> !udid.isEmpty())
                    .toList();
            System.out.println("[DualDeviceManager] Connected devices: " + udids);
            return udids;
        } catch (Exception e) {
            System.err.println("[DualDeviceManager] Could not run 'adb devices': " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Convenience: take a screenshot on both devices with the given name prefix.
     */
    public void screenshotBoth(String name) {
        app.util.AppUtil.captureScreenshot(adminDriver,  name + "_admin");
        app.util.AppUtil.captureScreenshot(memberDriver, name + "_member");
    }

    // ── Private builders ──────────────────────────────────────────────────────

    private static AndroidDriver buildDriver(URL url, String udid, String deviceLabel) {
        UiAutomator2Options opts = new UiAutomator2Options();
        opts.setUdid(udid);
        opts.setAppPackage(ATOMBERG_HOME);
        opts.setAppActivity(ATOMBERG_ACTIVITY);
        opts.setPlatformName("Android");
        opts.setAutomationName("UiAutomator2");
        // Do not reset — preserve existing login sessions on both devices
        opts.setNoReset(true);

        try {
            AndroidDriver d = new AndroidDriver(url, opts);
            d.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            System.out.println("[DualDeviceManager] " + deviceLabel + " session: " + d.getSessionId());
            return d;
        } catch (Exception e) {
            throw new RuntimeException(
                    "[DualDeviceManager] Failed to create driver for " + deviceLabel
                            + " (UDID=" + udid + "): " + e.getMessage(), e);
        }
    }

    private static void quit(AndroidDriver d, String label) {
        if (d == null) return;
        try   { d.quit(); System.out.println("[DualDeviceManager] " + label + " driver quit."); }
        catch (Exception e) { System.err.println("[DualDeviceManager] Error quitting " + label + ": " + e.getMessage()); }
    }

    private static URL parseUrl(String raw) {
        try   { return new URL(raw); }
        catch (MalformedURLException e) { throw new RuntimeException("Invalid APPIUM_URL: " + raw, e); }
    }

    private static String requireEnv(String key) {
        String v = System.getenv(key);
        if (v == null || v.isBlank())
            throw new IllegalStateException(
                    "Missing required environment variable: " + key
                            + "\nRun:  adb devices  to find UDIDs, then set the variable.");
        return v;
    }

    private static String optionalEnv(String key, String fallback) {
        String v = System.getenv(key);
        return (v != null && !v.isBlank()) ? v : fallback;
    }
}