package app.BLEOnlyFans;

import app.resources.Locators.BLEFan;
import app.util.ActionsUtil;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static app.resources.Locators.BLEFan.*;

public class ProgressivePauseResume {

    private final AndroidDriver driver;
    private final FirmwareVersionChecker csvLogger;

    // === Configuration ===
    private static final int PAUSE_RESUME_CYCLES = 20;
    private static final long HOLD_DURATION_MS = 800;

    public ProgressivePauseResume(AndroidDriver driver, FirmwareVersionChecker csvLogger) {
        this.driver = driver;
        this.csvLogger = csvLogger;
    }

    /**
     * Executes the pause-resume sequence with retry capability
     */
    public boolean runPauseResumeCycle(int attemptNumber) {
        String actionId = "Pause-Resume Cycle";
        int iteration = 1;
        boolean success = false;

        while (iteration <= 5) {
            String status = "Fail";
            String versionForLogging = ""; // Always empty for pause-resume

            try {
                // Step 1: Click Start (if not already started)
                try {
                    driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Start\"]")).click();
                    System.out.println("▶️ Start button clicked.");
                } catch (Exception e) {
                    System.out.println("⚠️ 'Start' button not found or already running.");
                }

                // Step 2: Perform pause-resume cycles
                boolean allCyclesSuccessful = true;
                for (int i = 0; i < PAUSE_RESUME_CYCLES; i++) {
                    // ⏸️ Pause: Tap at center-bottom
                    ActionsUtil.Tap.withCoordinates(driver, 525, 2020);

                    ActionsUtil.sleep(HOLD_DURATION_MS);
                    // ▶️ Resume: Tap again
                    ActionsUtil.Tap.withCoordinates(driver, 525, 2020);

                    System.out.println("🔁 Cycle " + (i + 1) + "/" + PAUSE_RESUME_CYCLES + " completed");

                    // Check if we're still connected
                    if (!isDeviceConnected()) {
                        allCyclesSuccessful = false;
                        break;
                    }
                }
                if(isElementPresent(RESUME)) driver.findElement(RESUME).click();
                if (allCyclesSuccessful) {
                    status = "Success";
                    success = true;
                }
            } catch (Exception e) {
                // Ignore and retry
            }

            // Log to the shared CSV (versionForLogging is always empty)
            csvLogger.printRow(attemptNumber, actionId, iteration, status, versionForLogging);

            if (success) {
                break;
            }

            if (iteration == 5) {
                break;
            }

            iteration++;
        }

        return success;
    }

    /**
     * Checks if device is connected
     */
    private boolean isDeviceConnected() {
        try {
            return !driver.getPageSource().contains("Device not connected");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if an element is present and visible
     */
    private boolean isElementPresent(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}