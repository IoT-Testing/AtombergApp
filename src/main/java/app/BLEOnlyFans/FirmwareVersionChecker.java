package app.BLEOnlyFans;

import app.Fan.FanManagement;
import app.util.ActionsUtil;
import app.util.Navigation;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.*;
import java.util.*;

public class FirmwareVersionChecker {

    private final AndroidDriver driver;
    public static String previousExpectedVersion;

    // === Locators ===
    private static final By MENU_BUTTON = By.xpath(
            "//android.widget.FrameLayout[@resource-id='android:id/content']" +
                    "/android.widget.FrameLayout/android.widget.FrameLayout" +
                    "/android.view.View/android.view.View/android.view.View" +
                    "/android.view.View/android.view.View/android.view.View/android.view.View[4]"
    );
    private static final String FIRMWARE_VERSION_PREFIX = "Firmware Version";
    private static final By SELECT_FILE_OPTION = By.xpath("//android.widget.Button[@content-desc=\"Select File\"]");

    public FirmwareVersionChecker(AndroidDriver driver) {
        this.driver = driver;
    }

    /**
     * Runs exactly 20 OTA updates using dynamically named files:
     * Production_1.0.1.bin → Production_1.0.20.bin
     *
     * Skips if current version >= "i"
     * Scrolls if file not in view
     */
    public void runSequentialFirmwareUpdates() {
        System.out.println("🔁 Starting 20-iteration dynamic OTA update test...");

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
        for (int i = 1; i <= 20; i++) {
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
            if (!clickElementWithRetry(SELECT_FILE_OPTION, 3)) {
                throw new RuntimeException("❌ 'Select File' option not clickable");
            }
            System.out.println("📁 Select File clicked. Waiting for file picker...");
            sleep(3000);

            // ✅ Scroll and select correct file (handles bad sorting)
            selectFileWithScroll(fileName);

            // Run validation: wait for success → click Done → verify
            BluetoothToggleInterruption validator = new BluetoothToggleInterruption(driver);
            previousExpectedVersion = expectedVersion;
            validator.runProgressivePauseResume();
        }

        System.out.println("✅ Completed all 20 dynamic OTA updates successfully!");
        driver.navigate().back();
        driver.navigate().back();
        driver.navigate().back();
        driver.navigate().back();
        FanManagement fan = new FanManagement(driver);
        fan.fanControl();
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
        final int MAX_SCROLLS = 30; // Prevent infinite loop

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
}
