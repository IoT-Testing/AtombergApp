package com.appTest.tests;

import ExtentReports.ExtentReportAT;
import app.AppInitializer;
import app.ServerInitializer;
import app.util.ActionsUtil;
import app.util.ScreenRecording;
import com.applitools.eyes.appium.Eyes;
import com.aventstack.extentreports.Status;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.appmanagement.ApplicationState;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.testng.annotations.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;

import static app.resources.Locators.Android.HomeLocators.*;

/**
 * BaseTest - Base class for all test classes.
 * STF connection via ADB over TCP (adb connect <ip>:<port>)
 * Supports multiple devices running in parallel via TestNG parameters.
 */
public class BaseTest {

    // ── STF Configuration ─────────────────────────────────────
    // Update these values to match your STF setup
    private static final String STF_TOKEN   = "YOUR_STF_TOKEN_HERE";   // STF Bearer token
    private static final String STF_URL     = "http://YOUR_STF_HOST";  // STF server URL
    private static final int    STF_PORT    = 7100;                     // Default STF ADB port

    // ── App Configuration ──────────────────────────────────────
    private static final String APP_PACKAGE = "com.atomberg.app";

    // ── Instance Variables ─────────────────────────────────────
    public static String        deviceSlot;
    public static ExtentReportAT reporter;
    public static ServerInitializer server;
    public Eyes                 eyes;
    public AndroidDriver        driver;
    private ScreenRecording     screenRecording;
    private boolean             setupSuccessful = false;
    private String              deviceIp;
    private String              devicePort;

    // ══════════════════════════════════════════════════════════
    // SETUP
    // ══════════════════════════════════════════════════════════

    @BeforeClass
    @Parameters({"deviceSlot", "deviceIp", "devicePort"})
    public void setup(
            @Optional("default")  String deviceSlot,
            @Optional("0.0.0.0")  String deviceIp,
            @Optional("5555")     String devicePort
    ) throws Exception {

        BaseTest.deviceSlot = deviceSlot;
        this.deviceIp       = deviceIp;
        this.devicePort     = devicePort;
        server              = new ServerInitializer();

        try {
            initializeReporter();
            reporter.startTest("Device Setup", deviceSlot);

            connectDeviceViaAdb();
            startAppiumServer();
            initializeDriver();
            startScreenRecording();

            setupSuccessful = true;
            reporter.log(Status.PASS, "Setup completed successfully for device: " + deviceIp + ":" + devicePort);

        } catch (Exception e) {
            reporter.log(Status.FAIL, "Setup failed: " + e.getMessage());
            throw e;
        } finally {
            reporter.endTest();
        }
    }

    // ══════════════════════════════════════════════════════════
    // STF — ADB OVER TCP CONNECTION
    // ══════════════════════════════════════════════════════════

    /**
     * Connects to the STF device via ADB over TCP.
     * Runs: adb connect <deviceIp>:<devicePort>
     */
    private void connectDeviceViaAdb() throws IOException {
        String adbConnectCommand = "adb connect " + deviceIp + ":" + devicePort;
        System.out.println("Connecting to STF device: " + adbConnectCommand);

        Process process = Runtime.getRuntime().exec(adbConnectCommand);
        String output   = readProcessOutput(process);
        int exitCode    = waitForProcess(process);

        System.out.println("ADB connect output: " + output);

        // Validate connection succeeded
        if (!output.contains("connected to") && !output.contains("already connected")) {
            throw new RuntimeException(
                    "ADB connect failed for " + deviceIp + ":" + devicePort +
                            " | Output: " + output
            );
        }

        System.out.println("✅ Device connected via ADB TCP: " + deviceIp + ":" + devicePort);
        ActionsUtil.SSleep(2); // Allow ADB to stabilize
    }

    /**
     * Disconnects the STF device after test run.
     * Runs: adb disconnect <deviceIp>:<devicePort>
     */
    private void disconnectDeviceViaAdb() {
        try {
            String adbDisconnectCommand = "adb disconnect " + deviceIp + ":" + devicePort;
            System.out.println("Disconnecting STF device: " + adbDisconnectCommand);
            Process process = Runtime.getRuntime().exec(adbDisconnectCommand);
            waitForProcess(process);
            System.out.println("✅ Device disconnected: " + deviceIp + ":" + devicePort);
        } catch (IOException e) {
            System.err.println("Error disconnecting device: " + e.getMessage());
        }
    }

    // ══════════════════════════════════════════════════════════
    // APPIUM SERVER & DRIVER
    // ══════════════════════════════════════════════════════════

    /**
     * Starts Appium server.
     */
    private void startAppiumServer() {
        server.startServer();
        System.out.println("✅ Appium server started.");
    }

    /**
     * Initializes Android driver using device IP:Port.
     */
    private void initializeDriver() throws Exception {
        String adbCommand = "adb connect " + deviceIp + ":" + devicePort;

        AppInitializer appInitializer = new AppInitializer();
        appInitializer.initializeDriverWithURL();
        this.driver = appInitializer.getDriver();

        if (driver == null) {
            throw new IllegalStateException("Driver initialization returned null for device: " + deviceIp);
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        System.out.println("✅ Driver initialized for device: " + deviceIp + ":" + devicePort);
    }

    /**
     * Starts screen recording.
     */
    private void startScreenRecording() {
        this.screenRecording = new ScreenRecording(driver);
        this.screenRecording.start();
        System.out.println("✅ Screen recording started.");
    }

    // ══════════════════════════════════════════════════════════
    // TEARDOWN
    // ══════════════════════════════════════════════════════════

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        System.out.println("Starting teardown for device: " + deviceIp + ":" + devicePort);

        // Close Applitools Eyes session
        if (eyes != null) {
            try {
                eyes.abortIfNotClosed();
                System.out.println("✅ Applitools Eyes session closed.");
            } catch (Exception e) {
                System.err.println("Error closing Eyes session: " + e.getMessage());
            }
        }

        // Stop screen recording
        if (screenRecording != null) {
            try {
                screenRecording.stop();
                System.out.println("✅ Screen recording stopped.");
            } catch (Exception e) {
                System.err.println("Error stopping screen recording: " + e.getMessage());
            }
        }

        // Stop Appium server
        if (server != null && server.service.isRunning()) {
            try {
                server.stopServer();
                System.out.println("✅ Appium server stopped.");
            } catch (Exception e) {
                System.err.println("Error stopping Appium server: " + e.getMessage());
            }
        }

        // Quit driver
        if (driver != null) {
            try {
                driver.quit();
                System.out.println("✅ Driver session ended.");
            } catch (Exception e) {
                System.err.println("Error quitting driver: " + e.getMessage());
            }
        }

        // Disconnect ADB device
        disconnectDeviceViaAdb();

        // Flush report
        if (setupSuccessful && reporter != null) {
            try {
                reporter.close();
            } catch (Exception e) {
                System.err.println("Error flushing report: " + e.getMessage());
            }
        }
    }

    // ══════════════════════════════════════════════════════════
    // HELPERS
    // ══════════════════════════════════════════════════════════

    /**
     * Initializes the reporter.
     */
    private void initializeReporter() {
        reporter = new ExtentReportAT(deviceSlot);
    }

    /**
     * Reads stdout from a process.
     */
    private String readProcessOutput(Process process) throws IOException {
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }
        return output.toString().trim();
    }

    /**
     * Waits for a process to complete (max 10 seconds).
     */
    private int waitForProcess(Process process) {
        try {
            if (process.waitFor(10, java.util.concurrent.TimeUnit.SECONDS)) {
                return process.exitValue();
            } else {
                process.destroyForcibly();
                return -1;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            process.destroyForcibly();
            return -1;
        }
    }

    /**
     * Returns the current driver instance.
     */
    public AndroidDriver getDriver() {
        return driver;
    }

    /**
     * Recovery logic: navigate back to home screen after test failure.
     */
    public void afterTestFailure(AndroidDriver driver) {
        if (driver == null) return;

        ApplicationState state = driver.queryAppState(APP_PACKAGE);
        int backPressCount     = 0;
        final int MAX_BACK     = 10;

        try {
            if (state.equals(ApplicationState.RUNNING_IN_FOREGROUND)) {
                while (isOnHomeScreen(driver) && backPressCount < MAX_BACK) {
                    System.out.println("Navigating back... (" + (backPressCount + 1) + "/" + MAX_BACK + ")");
                    driver.navigate().back();
                    ActionsUtil.SSleep(2);
                    backPressCount++;
                }

                if (isOnHomeScreen(driver)) {
                    System.err.println("Failed to recover to home screen after " + MAX_BACK + " back presses.");
                } else {
                    System.out.println("✅ Recovered to home screen.");
                }
            } else {
                System.out.println("App not in foreground. Activating...");
                driver.activateApp(APP_PACKAGE);
                ActionsUtil.SSleep(3);
            }
        } catch (Exception e) {
            System.err.println("Error during failure recovery: " + e.getMessage());
        }
    }

    /**
     * Checks if currently on home screen.
     */
    boolean isOnHomeScreen(AndroidDriver driver) {
        try {
            WebElement element = driver.findElement(DEVICES);
            return !element.isDisplayed();
        } catch (NoSuchElementException e) {
            return true;
        }
    }
}