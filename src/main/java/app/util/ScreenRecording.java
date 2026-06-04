package app.util;

import app.AppInitializer;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.*;
import java.io.IOException;
import java.net.URL;
import static app.resources.AppInfo.*;

/**
 * ScreenRecording - Records device screen using HBRecorderExample app.
 */
public class ScreenRecording {
    private AndroidDriver driver;

    // === App Info ===

    // === Locators ===
    private static final By START_BUTTON = By.xpath("//android.widget.Button[@resource-id=\"com.hbisoft.hbrecorderexample:id/button_start\"]");
    private static final By FINAL_START_BUTTON = By.xpath("//android.widget.Button[@resource-id=\"android:id/button1\"]");
    private static final By STOP_BUTTON = By.xpath("//android.widget.Button[@text='STOP']");

    // Dialog Options
    private static final By SINGLE_APP_OPTION = By.xpath("//android.widget.TextView[@text=\"A single app\"]");
    private static final By ENTIRE_SCREEN_OPTION = By.xpath("//android.widget.TextView[@text=\"Entire screen\"]");

    public ScreenRecording() throws IOException, InterruptedException {
        AppInitializer initializer = new AppInitializer();
        initializer.initializeDriver();

    }
    /**
     * Starts screen recording via HBRecorder app.
     */
    public void start() {
        activateRecorderApp();
        ActionsUtil.SSleep(3); // Allow UI load

        // Step 1: Click initial START/START NOW
        if (!clickElementIfExists(START_BUTTON)) {
            throw new RuntimeException("Failed to find any start button in recorder app.");
        }

        // Step 2: Handle optional dialog: "A single app" → "Entire screen"
        handleRecordingOptionsDialog();

        System.out.println("Recording Screen");
    }

    /**
     * Stops ongoing screen recording.
     */
    public void stop() {
        try {
            activateRecorderApp();
            if (clickElementIfExists(STOP_BUTTON)) {
                System.out.println("Screen recording stopped.");
            } else {
//                System.err.println("Failed to click STOP button.");
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

    /**
     * Handles the dialog that appears after first START click.
     * Expected flow:
     * 1. Tap "A single app"
     * 2. Tap "Entire screen"
     * 3. Tap final "Start" button
     */
    private void handleRecordingOptionsDialog() {
        // 1. Click "A single app"
        if (!clickElementIfExists(SINGLE_APP_OPTION)) {
            throw new RuntimeException("Failed to select 'A single app'");
        }
        ActionsUtil.sleep(1000);

        // 2. Click "Entire screen"
        if (!clickElementIfExists(ENTIRE_SCREEN_OPTION)) {
            throw new RuntimeException("Failed to select 'Entire screen'");
        }
        ActionsUtil.sleep(2000);

        // 3. Click final "Start" (lowercase 'S') — NOT "START"
        if (!clickElementIfExists(FINAL_START_BUTTON)) {
            throw new RuntimeException("Final 'Start' button not found after configuration.");
        }

        ActionsUtil.SSleep(2); // Let recording begin
    }

    /**
     * Waits up to N seconds for an element to be present.
     */
    private boolean waitUntilPresent(By locator, long timeoutSec) {
        return isElementPresent(locator);
    }

    /**
     * Safely checks if element is present and displayed.
     */
    private boolean isElementPresent(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Clicks element if present and clickable.
     */
    private boolean clickElementIfExists(By locator) {
        try {
            WebElement el = driver.findElement(locator);
            if (el.isDisplayed()) {
                el.click();
                return true;
            }
        } catch (NoSuchElementException e) {
            return false;
        } catch (Exception e) {
            System.err.println("Unexpected error clicking: " + locator + " -> " + e.getMessage());
        }
        return false;
    }
}