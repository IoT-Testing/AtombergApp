package app.BLEOnlyFans;

import app.util.ActionsUtil;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;
import static app.resources.Locators.BLEFan.*;


public class ProgressivePauseResume {

    private final AndroidDriver driver;
    private static PrintWriter csvWriter;
    private final FirmwareVersionChecker csvLogger;
    private static boolean csvInitialized = false;
    private int currentAttempt = 1; // Track current attempt number

    // === Configuration ===
    private static final int PAUSE_RESUME_CYCLES = 16;
    private static final long HOLD_DURATION_MS = 800;

    public ProgressivePauseResume(AndroidDriver driver,FirmwareVersionChecker csvLogger) {
        this.driver = driver;
        this.csvLogger = csvLogger;
    }

    /**
     * Executes the full firmware verification and pause-resume cycle
     */
    public void runProgressivePauseResume() {
        System.out.println("⏱ Starting high-accuracy pause-resume using coordinate taps...");

        // Initialize CSV on first run
//        initCSV();
        // Run firmware verification and pause-resume sequence
        boolean success = executePauseResumeSequence(currentAttempt);

        if (success) {
            System.out.println("✅ Firmware verification and pause-resume completed successfully!");
        } else {
            System.out.println("❌ Failed to complete firmware verification and pause-resume sequence");
        }

        currentAttempt++; // Increment for next run
    }


    /**
     * Executes firmware verification with retry capability
     */

    public void openMenuAndWait() {
        if (!clickIfExists(MENU_BUTTON)) {
            throw new RuntimeException("❌ Menu button not found after returning to device control");
        }
        System.out.println("✅ Menu opened");
        sleep(3000); // Allow load
    }

    /**
     * Executes the pause-resume sequence with retry capability
     */
    private boolean executePauseResumeSequence(int attemptNumber) {
        String actionId = "Pause-Resume Cycle";
        int iteration = 1;
        boolean success = false;

        while (iteration <= 5) {
            String status = "Fail";

            try {
                // Step 1: Click Start (if not already started)
                try {
                    driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Start\"]")).click();
                    System.out.println("▶️ Start button clicked.");
                } catch (Exception e) {
                    System.out.println("⚠️ 'Start' button not found or already running.");
                }
                sleep(HOLD_DURATION_MS);

                // Step 2: Perform pause-resume cycles
                boolean allCyclesSuccessful = true;
                for (int i = 0; i < PAUSE_RESUME_CYCLES; i++) {
                    // ⏸️ Pause: Tap at center-bottom
//                    ActionsUtil.Tap.withCoordinates(driver, 370, 1220);// for narzo only
                    ActionsUtil.Tap.withCoordinates(driver, 500, 2020);// for Poco only
                    sleep(HOLD_DURATION_MS);

                    // ▶️ Resume: Tap again
//                    ActionsUtil.Tap.withCoordinates(driver, 370, 1220);// for narzo only
                    ActionsUtil.Tap.withCoordinates(driver, 500, 2020);// for Poco only
                    sleep(HOLD_DURATION_MS);

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
            String versionForLogging = "";

            csvLogger.printRow(attemptNumber, actionId, iteration, status, versionForLogging);

            if (success) {
                break;
            }

            if (iteration == 5) {
                break;
            }

            iteration++;
            sleep(1000);
        }

        return success;
    }

    /**
     * Checks if device is connected
     */
    private boolean isDeviceConnected() {
        try {
            return !Objects.requireNonNull(driver.getPageSource()).contains("Device not connected");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Waits for element to appear with timeout
     */
    private boolean waitForElement(By locator, int timeoutSeconds) {
        long startTime = System.currentTimeMillis();
        while ((System.currentTimeMillis() - startTime) < (timeoutSeconds * 1000L)) {
            if (isElementPresent(locator)) {
                return true;
            }
            sleep(500);
        }
        return false;
    }

    // ===== CSV LOGGING METHODS =====

    private void printRow(int attemptNumber, String actionId, int iteration, String status) {
        // Format: clean, no extra spaces
        String row = String.format("%d,%s,%d,%s", attemptNumber, actionId, iteration, status);

        // Write to CSV
        csvWriter.println(row);
        csvWriter.flush();

        // Print to console
        System.out.printf("%-15d | %-20s | %-10d | %-8s%n",
                attemptNumber, actionId, iteration, status);
    }


    public static void closeCSV() {
        if (csvWriter != null) {
            csvWriter.close();
        }
    }

    // ===== HELPER METHODS =====

    private boolean isElementPresent(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    private boolean clickIfExists(By locator) {
        try {
            WebElement el = driver.findElement(locator);
            if (el.isDisplayed() && Boolean.parseBoolean(el.getDomAttribute("clickable"))) {
                el.click();
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    private String getCurrentFirmwareVersionFromMenu() {
        try {
            List<WebElement> views = driver.findElements(By.className("android.view.View"));
            return views.stream()
                    .map(el -> {
                        try {
                            return el.getDomAttribute("content-desc");
                        } catch (Exception e) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .filter(desc -> desc.startsWith("Firmware Version"))
                    .map(desc -> desc.replaceFirst("Firmware Version\\s*", "").trim())
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            System.err.println("Error reading firmware version: " + e.getMessage());
            return null;
        }
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Sleep interrupted: " + e.getMessage());
        }
    }
}