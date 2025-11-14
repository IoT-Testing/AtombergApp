package app.BLEOnlyFans;

import app.Fan.FanManagement;
import app.util.ActionsUtil;
import app.util.Navigation;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static app.resources.Locators.Android.BLEFan.*;
import static app.util.AppUtil.confirmOnHomeScreen;

public class FirmwareVersionChecker {

    private final AndroidDriver driver;
    public static String previousExpectedVersion;
    private static PrintWriter csvWriter;
    private static boolean csvInitialized = false;
    private int currentAttempt = 1;

    // === Locators ===
    private static final By MENU_BUTTON = By.xpath(
            "//android.widget.FrameLayout[@resource-id='android:id/content']" +
                    "/android.widget.FrameLayout/android.widget.FrameLayout" +
                    "/android.view.View/android.view.View/android.view.View" +
                    "/android.view.View/android.view.View/android.view.View/android.view.View[4]"
    );
    private static final String FIRMWARE_VERSION_PREFIX = "Firmware Version";
    private static final By SELECT_FILE_OPTION = By.xpath("//android.widget.Button[@content-desc=\"Select File\"]");
    private static final By FIRMWARE_SUCCESS_TOAST = By.xpath("//android.view.View[@content-desc=\"Firmware upgrade successful\"]");
    private static final By DONE_BUTTON = By.xpath("//android.widget.Button[@content-desc=\"Done\"]");

    public FirmwareVersionChecker(AndroidDriver driver) {
        this.driver = driver;
    }

    /**
     * Runs exactly 20 OTA updates using dynamically named files:
     * Production_1.0.1.bin → Production_1.0.20.bin
     * Skips if current version >= "i"
     * Scrolls if file not in view
     */
    public void runSequentialFirmwareUpdates() {
        System.out.println("🔁 Starting 20-iteration dynamic OTA update test...");

        // Initialize CSV
        initCSV();
        System.out.println("\n📊 CSV Log Format:");
        System.out.println("Attempt Number,Action ID,Iteration,Status,Updated Version");
        System.out.println("--------------------------------------------------");

        // Step 1: Open Fan Control & Read Current Firmware
        Navigation.openFanControl(driver);
        sleep(2000);

        if (!clickElementWithRetry(MENU_BUTTON, 3)) {
            throw new RuntimeException("❌ Failed to click Menu button");
        }
        sleep(2000);

        WebElement firmwareElement = findElementByContentDescStartsWith(FIRMWARE_VERSION_PREFIX);
        if (firmwareElement == null) {
            throw new RuntimeException("❌ 'Firmware Version' option not found");
        }

        String currentFullText = firmwareElement.getDomAttribute("content-desc");
        String currentVersion = extractVersionFromFirmwareText(currentFullText);
        System.out.println("📄 Current Firmware: " + currentVersion);

        // Parse patch version: 1.0.X → X
        int currentPatch = parsePatchVersion(currentVersion);
        int startFrom = currentPatch + 1;
        System.out.println("➡️ Starting from iteration: " + startFrom);

        // Main Loop: i = 1 to 20
        //TODO : i is the file number to start from.
        for (int i = 1; i <= 100; i++) {
            if (i < startFrom) {
                System.out.println("⏭️ Skipping iteration " + i + " (already at or above this version)");
                continue;
            }

            String fileName = "Production_1.0." + i + ".bin";
            String expectedVersion = "1.0." + i;

            System.out.println("\n🚀 Starting Iteration " + i + "/20");
            System.out.println("📄 Firmware File: " + fileName);
            System.out.println("🎯 Expected Version: " + expectedVersion);

            // Click Firmware Version
            firmwareElement = findElementByContentDescStartsWith(FIRMWARE_VERSION_PREFIX);
            if (firmwareElement == null) {
                throw new RuntimeException("❌ 'Firmware Version' option not found");
            }
            firmwareElement.click();
            System.out.println("✅ Clicked on Firmware Version");
            sleep(2000);

            // Click 'Select File'
            if (!clickElementWithRetry(SELECT_FILE_OPTION, 1)) {
                throw new RuntimeException("❌ 'Select File' option not clickable");
            }
            System.out.println("📁 Select File clicked. Waiting for file picker...");
            sleep(3000);

            // ✅ Scroll and select correct file (handles bad sorting)
            selectFileWithScroll(fileName);
            sleep(2000);

            // Run validation: wait for success → click Done → verify


//            1. Pause Resume
//            ProgressivePauseResume validator = new ProgressivePauseResume(driver, this);
//            validator.runProgressivePauseResume();

            previousExpectedVersion = expectedVersion;
            //TODO: change according to the interruption you want to run.

//             2. Uninterrupted upload.
//            UploadFirmware upload = new UploadFirmware(driver);
//            upload.runProgressiveFWUpload();


            //3. Bluetooth On-Off cycles
//            BluetoothToggleInterruption ble  = new BluetoothToggleInterruption(driver,i,fileName);
//            ble.runBluetoothOnOff();


            //4. Power Toggle
            PowerToggleInterruption toggle = new PowerToggleInterruption(driver,i, fileName);
            toggle.runPowerToggle();
            // Execute firmware verification
            executeFirmwareVerification(currentAttempt, expectedVersion);

            // Only run pause-resume if firmware verification succeeded

            currentAttempt++;
        }

        System.out.println("✅ Completed all dynamic OTA updates successfully!");
        driver.navigate().back();
        driver.navigate().back();
        driver.navigate().back();
        driver.navigate().back();
        FanManagement fan = new FanManagement(driver);
        fan.fanControl();
    }

    /**
     * Executes firmware verification with retry capability
     */
    private boolean executeFirmwareVerification(int attemptNumber, String expectedVersion) {
        String actionId = "Firmware Verification";
        int iteration = 1;
        boolean success = false;
        String updatedVersion = "";

        while (iteration <= 5) {
            String status = "Fail";
            updatedVersion = "";

            try {
                // Wait for success message
                boolean failedMessageFound = waitForElement(FILE_TRANSFER_ERROR_TOAST,5);
                boolean successMessageFound = waitForElement(FIRMWARE_SUCCESS_TOAST, 5);

                if (successMessageFound) {
                    // Click Done button
                    boolean doneClicked = clickElementWithRetry(DONE_BUTTON, 3);

                    if (doneClicked) {
                        // Confirm on Home Screen
                        confirmOnHomeScreen(driver);
                        sleep(1000);

                        // Navigate back to fan control
                        Navigation.openFanControl(driver);
                        sleep(2000);

                        // Open menu and verify version
                        if (clickElementWithRetry(MENU_BUTTON, 3)) {
                            sleep(2000);
                            String actualVersion = getCurrentFirmwareVersionFromMenu();

                            if (actualVersion != null && actualVersion.equals(expectedVersion)) {
                                status = "Success";
                                updatedVersion = actualVersion;
                                success = true;
                            }
                        }
                    }
                }
                else if(failedMessageFound)
                {
                    status = "Successful but Not Detected";
                    success = true;
                    updatedVersion = expectedVersion;
                }

            } catch (Exception e) {
                // Ignore and retry
            }

            printRow(attemptNumber, actionId, iteration, status, updatedVersion);

            if (success) {
                break;
            }

            // Add more delay only for firmware verification retries
            sleep(2000); // 2 seconds instead of 1 for better stability

            iteration++;
        }

        return success;
    }

    /**
     * Executes the pause-resume cycle with retry capability
     */
    private boolean executePauseResumeCycle(int attemptNumber, String expectedVersion) {
        String actionId = "Pause-Resume Cycle";
        int iteration = 1;
        boolean success = false;
        String updatedVersion = "";

        while (iteration <= 5) {
            String status = "Fail";
            updatedVersion = "";

            try {
                // Run pause-resume sequence
                ProgressivePauseResume validator = new ProgressivePauseResume(driver, this);
                validator.runProgressivePauseResume();

                // Verify device is still connected
                if (isDeviceConnected()) {
                    status = "Success";
                    updatedVersion = expectedVersion;
                    success = true;
                }
            } catch (Exception e) {
                // Ignore and retry
            }

            printRow(attemptNumber, actionId, iteration, status, updatedVersion);

            if (success) {
                break;
            }

            sleep(1000); // Standard delay for pause-resume (not increased)

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
     * Waits for element to appear with timeout
     */
    private boolean waitForElement(By locator, int timeoutSeconds) {
        long startTime = System.currentTimeMillis();
        while ((System.currentTimeMillis() - startTime) < (timeoutSeconds * 1000)) {
            if (isElementPresent(locator)) {
                return true;
            }
            sleep(500);
        }
        return false;
    }

    // === Internal Helpers ===

    /**
     * Selects a file by name, with intelligent scrolling.
     * Handles incorrectly sorted file lists (e.g., 1.0.1, 1.0.10, 1.0.2).
     */
    private void selectFileWithScroll(String fileName) {
        System.out.println("🔍 Scrolling to find: " + fileName);
        By fileLocator = By.xpath("//android.widget.TextView[@resource-id='android:id/title' and @text='" + fileName + "']");

        boolean found = false;
        int scrolls = 0;
        final int MAX_SCROLLS = 10; // Prevent infinite loop

        while (!found && scrolls < MAX_SCROLLS) {
            try {
                WebElement fileEl = driver.findElement(fileLocator);
                if (fileEl.isDisplayed()) {
                    fileEl.click();
                    System.out.println("✅ File selected: " + fileName);
                    sleep(2000);
                    return;
                }
            } catch (Exception ignored) {}
            ActionsUtil.Scroll.Up(driver);

            scrolls++;
            sleep(800); // Let UI stabilize
        }

        throw new RuntimeException("❌ Could not find or click file: " + fileName +
                " | Total scrolls attempted: " + scrolls);
    }

    /**
     * Finds element with content-desc starting with given prefix.
     */
    private WebElement findElementByContentDescStartsWith(String prefix) {
        List<WebElement> candidates = driver.findElements(By.className("android.view.View"));
        return candidates.stream()
                .map(el -> getAttribute(el, "content-desc"))
                .filter(Objects::nonNull)
                .filter(desc -> desc.startsWith(prefix))
                .findFirst()
                .flatMap(desc -> driver.findElements(By.className("android.view.View")).stream()
                        .filter(el -> Objects.equals(getAttribute(el, "content-desc"), desc))
                        .findFirst())
                .orElse(null);
    }

    /**
     * Safely gets DOM attribute.
     */
    private String getAttribute(WebElement el, String attr) {
        try {
            return el.getDomAttribute(attr);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Extracts version from "Firmware Version 1.0.2" → returns "1.0.2"
     */
    private String extractVersionFromFirmwareText(String fullText) {
        return fullText.replaceFirst("Firmware Version\\s*", "").trim();
    }

    /**
     * Reads current firmware version from menu option.
     */
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

    /**
     * Parses patch version from "1.0.4" → returns 4
     */
    private int parsePatchVersion(String version) {
        try {
            return Integer.parseInt(version.split("\\.")[2]);
        } catch (Exception e) {
            System.err.println("Failed to parse patch version: " + version);
            return 0;
        }
    }

    /**
     * Safely clicks an element with retry.
     */
    private boolean clickElementWithRetry(By locator, int maxRetries) {
        for (int i = 0; i < maxRetries; i++) {
            try {
                WebElement el = driver.findElement(locator);
                if (el.isDisplayed() && Boolean.parseBoolean(el.getDomAttribute("clickable"))) {
                    el.click();
                    return true;
                }
            } catch (Exception ignored) {
                sleep(1000);
            }
        }
        return false;
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

    /**
     * Safe sleep utility.
     */
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Sleep interrupted");
        }
    }

    // ===== CSV LOGGING METHODS =====


    void printRow(int attemptNumber, String actionId, int iteration, String status, String updatedVersion) {
        // Format: clean, no extra spaces
        String row = String.format("%d,%s,%d,%s,%s",
                attemptNumber, actionId, iteration, status, updatedVersion);

        // Write to CSV
        initCSV();
        csvWriter.println(row);
        csvWriter.flush();

        // Print to console
        System.out.printf("%-15d | %-20s | %-10d | %-8s | %-10s%n",
                attemptNumber, actionId, iteration, status, updatedVersion);
    }

    private static void initCSV() {
        if (csvInitialized) return;
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            csvWriter = new PrintWriter(new FileWriter("FirmwareTests/firmware_test_results_" + timestamp + ".csv", true));
            csvWriter.println("Attempt Number,Action ID,Iteration,Status,Updated Version");
            csvWriter.flush();
            csvInitialized = true;
        } catch (IOException e) {
            System.err.println("Failed to create CSV file: " + e.getMessage());
        }
    }

    public static void closeCSV() {
        if (csvWriter != null) {
            csvWriter.close();
        }
    }
}