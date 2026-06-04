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
import static app.resources.AppInfo.ATOMBERG_ACTIVITY;
import static app.resources.AppInfo.ATOMBERG_HOME;

/**
 * BaseTest – driver lifecycle, Appium server management, and Extent reporting.
 * ── Listener registration ────────────────────────────────────────────────────
 * Listeners are declared ONLY here on BaseTest. Subclasses must NOT redeclare
 * @Listeners — doing so causes TestNG to register each listener twice, which
 * produces duplicate log entries and inflated pass/fail counts in the report.
 * ── TestNG parameter bindings (from testng.xml) ──────────────────────────────
 *   deviceSlot – human-readable label, e.g. "Pixel_7_Slot1"
 *   deviceIp   – ADB-over-TCP IP for STF devices (127.0.0.1 for local)
 *   devicePort – ADB-over-TCP port (default 5555)
 */
@Listeners({com.appTest.listeners.TestListeners.class, com.appTest.listeners.DashboardReporter.class})
public class BaseTest {

    protected AndroidDriver  driver;
    protected ExtentReportAT reporter;
    protected String         deviceSlot;

    private final ServerInitializer serverInit = new ServerInitializer();

    // ── Suite hooks ───────────────────────────────────────────────────────────

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        serverInit.startServer();
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        serverInit.stopServer();
        if (reporter != null) reporter.close();
    }

    // ── Class hooks ───────────────────────────────────────────────────────────

    @BeforeClass(alwaysRun = true)
    @Parameters({"deviceSlot", "deviceIp", "devicePort"})
    public void setup(
            @Optional("Device_Default") String deviceSlot,
            @Optional("127.0.0.1")     String deviceIp,
            @Optional("5555")          String devicePort) {

        this.deviceSlot = deviceSlot;
        this.reporter   = new ExtentReportAT(deviceSlot);

        System.out.printf("Setting up: slot=%s  ip=%s  port=%s%n", deviceSlot, deviceIp, devicePort);

        UiAutomator2Options options = buildOptions(deviceIp, devicePort);
        URL serverUrl               = resolveServerUrl();

        driver = new AndroidDriver(serverUrl, options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        Assert.assertNotNull(driver, "AndroidDriver must be created before tests run");
        System.out.println("Driver initialised for: " + deviceSlot);
    }

    @AfterClass(alwaysRun = true)
    public void teardown() {
        if (driver != null) {
            try { driver.quit(); } catch (Exception e) {
                System.err.println("Driver quit error: " + e.getMessage());
            }
        }
        if (reporter != null) reporter.endTest();
        System.out.println("Driver quit for: " + deviceSlot);
    }

    // ── Shared helpers ────────────────────────────────────────────────────────

    /**
     * Called from a test's catch block to attempt navigation back to the home screen.
     * Uses the inherited {@code driver} field directly — no parameter needed.
     * Prevents cascading failures when one test leaves the app in an unexpected state.
     */
    protected void afterTestFailure() {
        try {
            AppUtil.confirmOnHomeScreen(driver);
        } catch (Exception e) {
            System.err.println("Recovery failed: " + e.getMessage());
        }
    }

    // ── Private builders ──────────────────────────────────────────────────────

    private UiAutomator2Options buildOptions(String deviceIp, String devicePort) {
        UiAutomator2Options options = new UiAutomator2Options();
        options.setAppPackage(ATOMBERG_HOME);
        options.setAppActivity(ATOMBERG_ACTIVITY);
        options.setPlatformName("Android");

        boolean isRemote = !"127.0.0.1".equals(deviceIp);
        if (isRemote) {
            String udid = deviceIp + ":" + devicePort;
            options.setUdid(udid);
            System.out.println("STF UDID: " + udid);
        }
        return options;
    }

    private URL resolveServerUrl() {
//        if (serverInit.isRunning()) {
//            return serverInit.service.getUrl();
//        }
//        try {
//            return new URL(APPIUM_URL);
//        } catch (MalformedURLException e) {
//            throw new RuntimeException("Invalid Appium URL: " + APPIUM_URL, e);
//        }
        return resolveServerUrl();
    }
}
