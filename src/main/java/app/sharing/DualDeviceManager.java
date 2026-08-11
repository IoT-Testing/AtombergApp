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
 * DualDeviceManager â€“ creates and manages two AndroidDriver instances for two
 * physical Android phones connected to the same laptop via USB.
 * â”€â”€ Physical setup â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
 * 1. Connect both phones via USB.
 * 2. Enable "USB Debugging" on both (Developer Options).
 * 3. Run:  adb devices
 *    You will see two entries, e.g.:
 *      UDID_PHONE_1   device
 *      UDID_PHONE_2   device
 * 4. Set environment variables (or pass directly to DualDeviceManager):
 *      ADMIN_DEVICE_UDID   = first device UDID  (Admin account will log in here)
 *      MEMBER_DEVICE_UDID  = second device UDID (Member account will log in here)
 * â”€â”€ Appium server â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
 * A SINGLE Appium server handles both drivers. Each driver gets its own session
 * because each driver creation call specifies a different `udid` capability.
 * No separate servers needed.
 * â”€â”€ Role mapping â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
 *   adminDriver  â†’ Admin (home admin) account â€“ shares devices, edits permissions
 *   memberDriver â†’ Non-admin (recipient) account â€“ receives shared devices
 * Usage:
 *   DualDeviceManager mgr = DualDeviceManager.create();
 *   AndroidDriver admin  = mgr.adminDriver();
 *   AndroidDriver member = mgr.memberDriver();
 *   // ... run tests ...
 *   mgr.quitAll();
 */
public class DualDeviceManager {

    // Environment variable names â€” set these before running tests
    // These are the CONFIG KEY NAMES to look up (in sharing-test.properties or
    // as env vars) â€” NOT the values. The actual UDIDs live in sharing-test.properties.
    public static final String ENV_ADMIN_UDID  = "ADMIN_DEVICE_UDID";
    public static final String ENV_MEMBER_UDID = "MEMBER_DEVICE_UDID";
    public static final String ENV_APPIUM_URL  = "APPIUM_URL";

    private final AndroidDriver adminDriver;
    private final AndroidDriver memberDriver;

    // â”€â”€ Factory â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    /**
     * Creates a DualDeviceManager by reading UDIDs from environment variables.
     *
     * @throws IllegalStateException if ADMIN_DEVICE_UDID or MEMBER_DEVICE_UDID are not set
     * @throws RuntimeException      if driver creation fails
     */
    public static DualDeviceManager create() {
        // Resolves via SharingConfig: sharing-test.properties â†’ env var â†’ -D property.
        String adminUdid  = SharingConfig.require(ENV_ADMIN_UDID);
        String memberUdid = SharingConfig.require(ENV_MEMBER_UDID);
        String appiumUrl  = SharingConfig.get(ENV_APPIUM_URL, "http://127.0.0.1:4723");
        return new DualDeviceManager(adminUdid, memberUdid, appiumUrl);
    }

    /**
     * Creates a DualDeviceManager with explicit UDID and server values.
     * Useful when called from a TestNG @BeforeSuite / @BeforeClass.
     */
    public static DualDeviceManager create(String adminUdid, String memberUdid, String appiumUrl) {
        return new DualDeviceManager(adminUdid, memberUdid, appiumUrl);
    }

    // â”€â”€ Constructor â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    // Distinct UiAutomator2 ports per session. Two concurrent UiAutomator2
    // sessions on ONE Appium server MUST use different systemPorts â€” otherwise
    // both default to 8200, the second session's instrumentation collides with
    // the first, and commands route to the WRONG device (adminâ†”member swap).
    private static final int ADMIN_SYSTEM_PORT  = 8200;
    private static final int MEMBER_SYSTEM_PORT = 8201;
    private static final int ADMIN_MJPEG_PORT   = 7810;
    private static final int MEMBER_MJPEG_PORT  = 7811;

    private DualDeviceManager(String adminUdid, String memberUdid, String appiumUrl) {
        logpoint("[DualDeviceManager] Admin  UDID: " + adminUdid
                + "  (systemPort " + ADMIN_SYSTEM_PORT + ")");
        logpoint("[DualDeviceManager] Member UDID: " + memberUdid
                + "  (systemPort " + MEMBER_SYSTEM_PORT + ")");
        if (adminUdid.equals(memberUdid)) {
            throw new IllegalStateException(
                    "ADMIN_DEVICE_UDID and MEMBER_DEVICE_UDID are identical (" + adminUdid
                            + "). Set two different device UDIDs â€” run `adb devices`.");
        }

        URL url = parseUrl(appiumUrl);

        // Admin phone â€“ session 1 (own systemPort / mjpegServerPort)
        adminDriver = buildDriver(url, adminUdid, "Admin_Device",
                ADMIN_SYSTEM_PORT, ADMIN_MJPEG_PORT);

        // Member phone â€“ session 2 on the SAME Appium server (distinct ports)
        memberDriver = buildDriver(url, memberUdid, "Member_Device",
                MEMBER_SYSTEM_PORT, MEMBER_MJPEG_PORT);

    }

    // â”€â”€ Accessors â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

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

    // â”€â”€ Lifecycle â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    /**
     * Quits both drivers. Should be called in @AfterClass / @AfterSuite.
     * Logs errors but does not rethrow so teardown always completes.
     */
    public void quitAll() {
        quit(adminDriver,  "Admin");
        quit(memberDriver, "Member");
    }

    // â”€â”€ Helpers â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

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

    // â”€â”€ Private builders â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    private static AndroidDriver buildDriver(URL url, String udid, String deviceLabel,
                                             int systemPort, int mjpegServerPort) {
        UiAutomator2Options opts = new UiAutomator2Options();
        opts.setUdid(udid);
        opts.setAppPackage(ATOMBERG_HOME);
        opts.setAppActivity(ATOMBERG_ACTIVITY);
        opts.setPlatformName("Android");
        opts.setAutomationName("UiAutomator2");
        // Isolate this session's UiAutomator2 instrumentation from the other
        // device's â€” REQUIRED for two concurrent sessions on one Appium server.
        opts.setSystemPort(systemPort);
        opts.setMjpegServerPort(mjpegServerPort);
        // Pin the capability to this exact device so a slow/failed attach never
        // falls back to whichever device the server sees first.
        opts.setDeviceName(udid);
        // Do not reset â€” preserve existing login sessions on both devices
        opts.setNoReset(true);
        // In a two-phone suite one session idles while the other works (the admin
        // long-presses, shares and reads the invite code while the member waits).
        // The default newCommandTimeout is 60 s, after which Appium kills the idle
        // session AND closes its app â€” which looks like "the member phone shut its
        // app down on its own". 30 min is comfortably longer than any single step.
        opts.setNewCommandTimeout(Duration.ofMinutes(30));

        try {
            AndroidDriver d = new AndroidDriver(url, opts);
            d.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            logpoint("[DualDeviceManager] " + deviceLabel + " session created on UDID="
                    + udid + " (systemPort " + systemPort + ")");
            return d;
        } catch (Exception e) {
            throw new RuntimeException(
                    "[DualDeviceManager] Failed to create driver for " + deviceLabel
                            + " (UDID=" + udid + "): " + e.getMessage(), e);
        }
    }

    private static void quit(AndroidDriver d, String label) {
        if (d == null) return;
        try   { d.quit(); logpoint("[DualDeviceManager] " + label + " driver quit."); }
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
