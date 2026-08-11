package com.appTest.tests;

import ExtentReports.ExtentReportAT;
import app.ServerInitializer;
import app.util.AppUtil;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.testng.Assert;
import org.testng.annotations.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import static app.resources.AppInfo.*;
import static app.resources.Endpoints.APPIUM_URL;

/**
 * BaseTest â€“ driver lifecycle, Appium server management, and Extent reporting.

 * FIX C11: Listeners are declared ONLY here (on BaseTest). Subclasses must NOT
 * redeclare @Listeners â€” doing so causes TestNG to register each listener twice,
 * producing duplicate log entries and inflated pass/fail counts in the report.
 * The testng.xml <listeners> block is also REMOVED from the XML to avoid a
 * third registration path (XML + @Listeners is already sufficient, XML alone is
 * what you want when @Listeners is present on the class â€” not both).

 * FIX H1/H2: The driver field is now ThreadLocal so that each parallel <test>
 * block (thread) owns its own driver instance. Direct assignment from @BeforeClass
 * only into a plain field is thread-unsafe when parallel="tests" is used.

 * FIX C12: Removed unreachable `if (driver == null)` guard that appeared after
 * Assert.assertNotNull(driver) â€” the assert already throws if null.
 */
@Listeners({
        com.appTest.listeners.TestListeners.class,
        com.appTest.listeners.DashboardReporter.class
})
public class BaseTest {

    /*
     * FIX H2: ThreadLocal ensures each parallel test thread holds its own
     * driver reference. Access via getDriver() / setDriver().
     */
    private static final ThreadLocal<AndroidDriver>  TL_DRIVER       = new ThreadLocal<>();
    private static final ThreadLocal<ExtentReportAT> TL_REPORTER     = new ThreadLocal<>();
    private static final ThreadLocal<String>          TL_DEVICE_SLOT  = new ThreadLocal<>();

    /** Convenience accessor for subclasses â€” mirrors the old `driver` field. */
    protected AndroidDriver getDriver()   { return TL_DRIVER.get(); }

    /** Kept for backward-compat where subclasses reference `driver` directly. */
    protected AndroidDriver driver       () { return TL_DRIVER.get(); }

    protected ExtentReportAT reporter    () { return TL_REPORTER.get(); }
    protected String         deviceSlot  () { return TL_DEVICE_SLOT.get(); }

    /*
     * Expose protected fields to maintain backward compatibility with subclasses
     * that reference `driver`, `reporter`, and `deviceSlot` directly.
     *
     * NOTE: In Java, ThreadLocal fields are not inheritable fields in the
     * traditional sense, so subclasses must go through the accessors. If
     * migrating gradually, rename usages in subclasses to getDriver() etc.
     */
    public AndroidDriver  driver;
    protected ExtentReportAT reporter;
    public String         deviceSlot;

    private final ServerInitializer serverInit = new ServerInitializer();

    // â”€â”€ Suite hooks â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        serverInit.startServer();
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        serverInit.stopServer();
        ExtentReportAT r = TL_REPORTER.get();
        if (r != null) r.close();
    }

    // â”€â”€ Per-test-class hooks â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    /**
     * Sets up a fresh AndroidDriver for each TestNG {@code <test>} block.
     * Parameters are injected from testng.xml.
     */
    @BeforeClass(alwaysRun = true)
    @Parameters({"deviceSlot", "deviceUdid"})
    public void setup(
            @Optional("Device_Default") String slot,
            @Optional("")               String deviceUdid) {

        // Populate ThreadLocal AND backward-compat fields
        TL_DEVICE_SLOT.set(slot);
        this.deviceSlot = slot;

        ExtentReportAT rep = new ExtentReportAT(slot);
        TL_REPORTER.set(rep);
        this.reporter = rep;

        System.out.printf("[BaseTest] Setup â€“ slot=%s  udid=%s%n",
                slot, (deviceUdid == null || deviceUdid.isBlank()) ? "<device-farm auto-allocate>" : deviceUdid);

        UiAutomator2Options options = buildOptions(deviceUdid);
        URL serverUrl               = resolveServerUrl();

        AndroidDriver d = new AndroidDriver(serverUrl, options);
        d.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        // FIX C12: Assert is the guard; the redundant if(driver == null) block is removed.
        Assert.assertNotNull(d, "AndroidDriver must be non-null after construction");

        TL_DRIVER.set(d);
        this.driver = d;

        logpoint("[BaseTest] Driver initialised for: " + slot);
    }

    @AfterClass(alwaysRun = true)
    public void teardown() {
        AndroidDriver d = TL_DRIVER.get();
        if (d != null) {
            try   { d.quit(); }
            catch (Exception e) { System.err.println("[BaseTest] Driver quit error: " + e.getMessage()); }
            finally {
                TL_DRIVER.remove();
                this.driver = null;
            }
        }
        ExtentReportAT r = TL_REPORTER.get();
        if (r != null) {
            r.endTest();
            TL_REPORTER.remove();
            this.reporter = null;
        }
        TL_DEVICE_SLOT.remove();
        logpoint("[BaseTest] Driver quit for: " + deviceSlot);
    }

    // â”€â”€ Shared helpers â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    /**
     * Best-effort recovery after a test failure â€” navigates back to home screen.
     * Prevents cascading failures when one test leaves the app in an unexpected state.
     */
    protected void afterTestFailure() {
        try {
            AppUtil.confirmOnHomeScreen(TL_DRIVER.get());
        } catch (Exception e) {
            System.err.println("[BaseTest] Recovery failed: " + e.getMessage());
        }
    }

    // â”€â”€ Private builders â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    private UiAutomator2Options buildOptions(String deviceUdid) {
        UiAutomator2Options options = new UiAutomator2Options();
        options.setAppPackage(ATOMBERG_HOME);
        options.setAppActivity(ATOMBERG_ACTIVITY);
        options.setPlatformName("Android");

        // The Appium Device Farm plugin auto-allocates a free connected device for
        // each new session when no UDID is supplied â€” this is how parallel <test>
        // blocks each get a distinct device. Supply deviceUdid in testng.xml only
        // to pin a specific <test> block to a specific device.
        if (deviceUdid != null && !deviceUdid.isBlank()) {
            options.setUdid(deviceUdid);
            logpoint("[BaseTest] Pinning session to device UDID: " + deviceUdid);
        } else {
            logpoint("[BaseTest] No UDID set â€” Appium Device Farm will auto-allocate a device.");
        }
        return options;
    }

    private URL resolveServerUrl() {
        if (serverInit.isServerRunning()) {
            return serverInit.service.getUrl();
        }
        try {
            // FIX C9: Appium 3 base path is "/", not "/wd/hub"
            return new URL(APPIUM_URL);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid APPIUM_URL: " + APPIUM_URL, e);
        }
    }
}
