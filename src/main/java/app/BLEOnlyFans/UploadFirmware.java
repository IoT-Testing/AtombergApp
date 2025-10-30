package app.BLEOnlyFans;

import app.util.Navigation;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.util.List;
import java.util.Objects;
import static app.resources.Locators.BLEFan.*;

public class UploadFirmware {

    private final AndroidDriver driver;

    // === Locators ===
    private static final By FIRMWARE_SUCCESS_TOAST = By.xpath("//android.view.View[@content-desc=\"Firmware upgrade successful\"]");
    private static final By DONE_BUTTON = By.xpath("//android.widget.Button[@content-desc=\"Done\"]");
    public String  expectedVersion;
    // Adjust if needed
    // === Configuration ===
    private static final long FIRMWARE_SUCCESS_TIMEOUT_MS = 20_000;
    public UploadFirmware(AndroidDriver driver) {
        this.driver = driver;
    }

    public void runProgressiveFWUpload() {
        System.out.println("⏱ Starting high-accuracy pause-resume using coordinate taps...");

        try {
            // Step 1: Click Start (if not already started)
            try {
                driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Start\"]")).click();
                System.out.println("▶️ Start button clicked.");
            } catch (Exception e) {
                System.out.println("⚠️ 'Start' button not found or already running.");
            }
            sleep(20000);

        } catch (Exception e) {
            throw new RuntimeException("❌ Error during pause-resume cycle: " + e.getMessage(), e);
        }

        verifyFirmwareUpgradeAndClickDone(FirmwareVersionCheckerTwo.expectedVersion);
    }

    /**
     * Waits for success toast, clicks Done, opens fan control, verifies firmware version.
     *
     * @param expectedVersion Expected firmware version (e.g., "1.0.3")
     */
    public void verifyFirmwareUpgradeAndClickDone(String expectedVersion) {
        System.out.println("🔍 Waiting for firmware upgrade success message...");
        sleep(20000);
        long start = System.currentTimeMillis();

        // Wait for success message
        while ((System.currentTimeMillis() - start) < FIRMWARE_SUCCESS_TIMEOUT_MS) {
            if (isElementPresent(FIRMWARE_SUCCESS_TOAST)) {
                System.out.println("✅ Firmware upgrade successful message displayed.");
                System.out.println(expectedVersion);
                break;
            }
            sleep(500);
        }

        if (!isElementPresent(FIRMWARE_SUCCESS_TOAST)) {
            throw new RuntimeException("❌ Timeout: 'Firmware upgrade successful' not shown.");
        }

        // Click Done
        if (clickIfExists(DONE_BUTTON)) {
            System.out.println("✅ Clicked 'Done' button.");
        } else {
            throw new RuntimeException("❌ 'Done' button not found.");
        }

        // Confirm on Home Screen
        confirmOnHomeScreen();

        // Navigate back to device control
        Navigation.openFanControl(driver); // Reuse utility

        // Open Menu
        openMenuAndWait();

        // Verify Version
        String actualVersion = getCurrentFirmwareVersionFromMenu();
        if (actualVersion == null) {
            throw new RuntimeException("❌ Could not read firmware version from device.");
        }

        if (actualVersion.equals(expectedVersion)) {
            System.out.println("✅ Firmware version verified: " + actualVersion);
        } else {
            throw new RuntimeException(
                    "❌ Version mismatch! Expected: " + expectedVersion + ", Got: " + actualVersion);
        }

        // Close menu
        driver.navigate().back();
        System.out.println("📁 Menu closed. Ready for next update.");
        clickIfExists(MENU_BUTTON);

    }

    // === Helper Methods ===

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
     * Safely clicks element if present and displayed.
     */
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

    /**
     * Confirms that app has returned to Home Screen
     */
    private void confirmOnHomeScreen() {
        By moreTab = By.xpath("//android.widget.ImageView[@content-desc=\"More\nTab 3 of 3\"]");
        long start = System.currentTimeMillis();

        while ((System.currentTimeMillis() - start) < 10_000) {
            if (isElementPresent(moreTab)) {
                System.out.println("🏠 Back on Home Screen.");
                return;
            }
            sleep(500);
        }
        System.err.println("⚠️ Could not confirm return to Home Screen.");
    }

    /**
     * Opens the menu button and waits.
     */
    private void openMenuAndWait() {
        if (!clickIfExists(MENU_BUTTON)) {
            throw new RuntimeException("❌ Menu button not found after returning to device control");
        }
        System.out.println("✅ Menu opened");
        sleep(3000); // Allow load
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
     * Wrapper for Thread.sleep()
     */
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Sleep interrupted: " + e.getMessage());
        }
    }
}