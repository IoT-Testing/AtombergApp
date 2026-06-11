package app.util;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.*;
import java.time.Duration;

import static app.resources.AppInfo.*;

/** * ScreenRecording - Controls HBRecorderExample app to start/stop screen recording. * * Usage: *  - Caller must create/initialize an AndroidDriver that is connected to the device *    and pointing to the recorder app (or able to activate it). * *    Example: *      // initialize driver for recorder app (see Main.initializeDriver("recorder")) *      AndroidDriver driver = ...; *      ScreenRecording recorder = new ScreenRecording(driver); *      recorder.start(); *      // run tests... *      recorder.stop(); */
public class ScreenRecording {
    private final AndroidDriver driver;

    // === Locators ===
    private static final By START_BUTTON = By.xpath("//android.widget.Button[@resource-id=\"com.hbisoft.hbrecorderexample:id/button_start\"]");
    private static final By FINAL_START_BUTTON = By.xpath("//android.widget.Button[@resource-id=\"android:id/button1\"]");
    private static final By STOP_BUTTON = By.xpath("//android.widget.Button[@text=\"STOP\"]");

    // Dialog Options
    private static final By SINGLE_APP_OPTION = By.xpath("//android.widget.TextView[@text=\"A single app\"]");
    private static final By ENTIRE_SCREEN_OPTION = By.xpath("//android.widget.TextView[@text=\"Entire screen\"]");

    // Default wait time (seconds)
    private static final long DEFAULT_WAIT_SECONDS = 8;

    /**     * Construct with an existing, non-null driver.     * The driver must be valid and able to activate the recorder app.     */
    public ScreenRecording(AndroidDriver driver) {
        if (driver == null) {
            throw new IllegalArgumentException("AndroidDriver must not be null. Initialize driver before creating ScreenRecording.");
        }
        this.driver = driver;
    }

    /**     * Starts screen recording via HBRecorder app.     */
    public void start() {
        activateRecorderApp();
        ActionsUtil.SSleep(2); // small pause for UI to stabilize

        // Step 1: Click initial START/START NOW
        if (!clickElementIfExists(START_BUTTON, DEFAULT_WAIT_SECONDS)) {
            throw new RuntimeException("Failed to find any start button in recorder app.");
        }

        // Step 2: Handle optional dialog: "A single app" → "Entire screen" → final Start
        handleRecordingOptionsDialog();

        System.out.println("Recording started.");
    }

    /**     * Stops ongoing screen recording.     */
    public void stop() {
        try {
            activateRecorderApp();
            if (clickElementIfExists(STOP_BUTTON, DEFAULT_WAIT_SECONDS)) {
                System.out.println("Screen recording stopped.");
            } else {
                System.err.println("Could not find STOP button in recorder app.");
            }
        } catch (Exception e) {
            System.err.println("Error while stopping recording: " + e.getMessage());
        }
    }

    // === Internal Helpers ===

    private void activateRecorderApp() {
        try {
            driver.activateApp(RECORDER_APP_PACKAGE);
        } catch (Exception e) {
            throw new RuntimeException("Failed to activate screen recorder app: " + RECORDER_APP_PACKAGE, e);
        }
    }

    /**     * Handles the dialog that appears after first START click.     * Expected flow:     * 1. Tap "A single app"     * 2. Tap "Entire screen"     * 3. Tap final "Start" button     */
    private void handleRecordingOptionsDialog() {
        // 1. Click "A single app" (if present)
        if (!clickElementIfExists(SINGLE_APP_OPTION, 4)) {
            // Not fatal in some recorder versions; continue if not present
            System.out.println("'A single app' option not present; continuing.");
        } else {
            ActionsUtil.sleep(800);
        }

        // 2. Click "Entire screen" (if present)
        if (!clickElementIfExists(ENTIRE_SCREEN_OPTION, 4)) {
            System.out.println("'Entire screen' option not present; continuing.");
        } else {
            ActionsUtil.sleep(1200);
        }

        // 3. Click final "Start" (OK button)
        if (!clickElementIfExists(FINAL_START_BUTTON, DEFAULT_WAIT_SECONDS)) {
            throw new RuntimeException("Final 'Start' button not found after configuration.");
        }

        ActionsUtil.SSleep(2); // let recording begin
    }

    /**     * Polling wait: waits up to timeoutSec for an element to be present.     */
    private boolean waitUntilPresent(By locator, long timeoutSec) {
        long end = System.currentTimeMillis() + Duration.ofSeconds(timeoutSec).toMillis();
        while (System.currentTimeMillis() < end) {
            try {
                if (!driver.findElements(locator).isEmpty()) {
                    WebElement el = driver.findElement(locator);
                    if (el != null && el.isDisplayed()) return true;
                }
            } catch (WebDriverException ignored) {
                // ignore and retry
            }
            ActionsUtil.sleep(300);
        }
        return false;
    }

    /**     * Clicks element if present and clickable within timeout.     */
    private boolean clickElementIfExists(By locator, long timeoutSec) {
        try {
            if (!waitUntilPresent(locator, timeoutSec)) return false;
            WebElement el = driver.findElement(locator);
            // Try click; if fails, attempt tap via JS or fallback
            try {
                el.click();
                return true;
            } catch (Exception clickEx) {
                // fallback: try to tap by coordinates (center of element)
                try {
                    Rectangle r = el.getRect();
                    int cx = r.getX() + r.getWidth() / 2;
                    int cy = r.getY() + r.getHeight() / 2;
                    ActionsUtil.Tap.withCoordinates(driver, cx, cy);
                    return true;
                } catch (Exception tapEx) {
                    System.err.println("Failed to click or tap element: " + locator + " -> " + tapEx.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            return false;
        } catch (Exception e) {
            System.err.println("Unexpected error clicking: " + locator + " -> " + e.getMessage());
        }
        return false;
    }
}