package Tests;

import ExtentReports.ExtentReportAT;
import app.AppInitializer;
import app.STF.Connect2;
import app.ServerInitializer;
import app.util.ActionsUtil;
import app.util.ScreenRecording;
import com.applitools.eyes.appium.Eyes;
import com.aventstack.extentreports.Status;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.appmanagement.ApplicationState;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.testng.annotations.*;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.time.Duration;

/**
 * BaseTest - Base class for all test classes.
 *
 * <p>Refactored to:
 * <ul>
 *   <li>Ensure safe resource cleanup</li>
 *   <li>Improve error visibility</li>
 *   <li>Remove silent exceptions</li>
 *   <li>Follow test lifecycle best practices</li>
 * </ul>
 */
public class BaseTest {

    protected String deviceSlot;
    protected ExtentReportAT reporter;
    protected String adbCommand;
    protected ServerInitializer server;
    protected Eyes eyes; // Optional: Applitools integration
    protected AndroidDriver driver;

    private ScreenRecording screenRecording; // Track for cleanup
    private boolean setupSuccessful = false;

    // === Constants ===
    private static final String APP_PACKAGE = "com.atomberg.app";
    private static final By HOME_INDICATOR = By.xpath("//android.widget.ImageView[@content-desc='More\\nTab 3 of 3']");

    @BeforeClass
    @Parameters({"deviceSlot"})
    public void setup(@Optional("default") String deviceSlot) throws Exception {
        this.deviceSlot = deviceSlot;
        this.server = new ServerInitializer();

        try {
            initializeReporter();
            reporter.startTest("Device Setup", deviceSlot);

            fetchAdbCommand();
            executeAdbCommand();
            startAppiumServer();

            initializeDriver();
            startScreenRecording();

            setupSuccessful = true;
            reporter.log(Status.PASS, "Setup completed successfully");
        } catch (Exception e) {
            reporter.log(Status.FAIL, "Setup failed: " + e.getMessage());
            throw e; // Fail fast
        } finally {
            reporter.endTest();
        }
    }

    /**
     * Creates reporter instance.
     */
    private void initializeReporter() {
        this.reporter = new ExtentReportAT(deviceSlot);
    }

    /**
     * Fetches ADB connect command from system (via Connect2).
     */
    private void fetchAdbCommand() throws IOException, InterruptedException, UnsupportedFlavorException {
        Connect2 connect = new Connect2();
        connect.ipAddress();
        this.adbCommand = connect.getCopiedText();

        if (adbCommand == null || adbCommand.trim().isEmpty()) {
            throw new IllegalArgumentException("ADB command is null or empty. Ensure IP address was copied.");
        }

        System.out.println("ADB Command: " + adbCommand);
        ActionsUtil.SSleep(5); // Allow time after copy
    }

    /**
     * Executes ADB command to connect device.
     */
    private void executeAdbCommand() throws IOException {
        Process process = Runtime.getRuntime().exec(adbCommand);
        int exitCode = waitForProcess(process);

        if (exitCode != 0) {
            throw new RuntimeException("ADB command failed with exit code: " + exitCode);
        }
        System.out.println("Device connected via ADB.");
    }

    /**
     * Starts Appium server.
     */
    private void startAppiumServer() {
        server.startServer();
        System.out.println("Appium server started.");
    }

    /**
     * Initializes Android driver.
     */
    private void initializeDriver() throws Exception {
        AppInitializer appInitializer = new AppInitializer();
        appInitializer.initializeDriverWithURL(server.service.getUrl(), adbCommand);
        this.driver = appInitializer.getDriver();

        if (driver == null) {
            throw new IllegalStateException("Driver initialization returned null");
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        System.out.println("Driver initialized successfully.");
    }

    /**
     * Starts screen recording.
     */
    private void startScreenRecording() throws IOException, InterruptedException {
        this.screenRecording = new ScreenRecording(driver);
        this.screenRecording.start();
        System.out.println("Screen recording started.");
    }

    /**
     * Safely waits for process completion.
     *
     * @param process Process to wait for
     * @return Exit code
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
     * Gets the current driver instance.
     *
     * @return AndroidDriver
     */
    public AndroidDriver getDriver() {
        return driver;
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        System.out.println("Starting teardown...");

        // Close visual testing session
        if (eyes != null) {
            try {
                eyes.abortIfNotClosed();
                System.out.println("Applitools Eyes session aborted if open.");
            } catch (Exception e) {
                System.err.println("Error closing Eyes session: " + e.getMessage());
            }
        }

        // Stop screen recording
        if (screenRecording != null) {
            try {
                screenRecording.stop();
                System.out.println("Screen recording stopped.");
            } catch (Exception e) {
                System.err.println("Error stopping screen recording: " + e.getMessage());
            }
        }

        // Stop Appium server
        if (server != null && server.service.isRunning()) {
            try {
                server.stopServer();
                System.out.println("Appium server stopped.");
            } catch (Exception e) {
                System.err.println("Error stopping Appium server: " + e.getMessage());
            }
        }

        // Quit driver
        if (driver != null) {
            try {
                driver.quit();
                System.out.println("Driver session ended.");
            } catch (Exception e) {
                System.err.println("Error quitting driver: " + e.getMessage());
            }
        }

        // Flush report only if setup succeeded
        if (setupSuccessful && reporter != null) {
            try {
                // Note: In full suite, flush should be in @AfterSuite
                // This is here just in case
                ExtentReportAT.close(); // Static method ensures one-time flush
            } catch (Exception e) {
                System.err.println("Error flushing report: " + e.getMessage());
            }
        }
    }

    /**
     * Recovery logic: navigate back to home screen after test failure.
     *
     * @param driver The AndroidDriver instance
     */
    public void afterTestFailure(AndroidDriver driver) {
        if (driver == null) return;

        ApplicationState state = driver.queryAppState(APP_PACKAGE);
        int backPressCount = 0;
        final int MAX_BACK_PRESS = 10;

        try {
            if (state.equals(ApplicationState.RUNNING_IN_FOREGROUND)) {
                while (isOnHomeScreen(driver) && backPressCount < MAX_BACK_PRESS) {
                    System.out.println("Navigating back... (" + (backPressCount + 1) + "/" + MAX_BACK_PRESS + ")");
                    driver.navigate().back();
                    ActionsUtil.SSleep(2);
                    backPressCount++;
                }

                if (isOnHomeScreen(driver)) {
                    System.err.println("Failed to recover to home screen after " + MAX_BACK_PRESS + " back presses.");
                } else {
                    System.out.println("Successfully recovered to home screen.");
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
     *
     * @param driver Driver instance
     * @return true if home indicator is visible
     */
    private boolean isOnHomeScreen(AndroidDriver driver) {
        try {
            WebElement element = driver.findElement(HOME_INDICATOR);
            return !element.isDisplayed();
        } catch (NoSuchElementException e) {
            return true;
        }
    }
}