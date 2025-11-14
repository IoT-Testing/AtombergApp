package app.BLEOnlyFans;

import app.util.ActionsUtil;
import app.util.BluetoothUtils;
import app.util.Navigation;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.util.List;
import java.util.Objects;
import static app.resources.Locators.Android.BLEFan.*;
import static app.util.AppUtil.confirmOnHomeScreen;

public class BluetoothToggleInterruption{
    private final AndroidDriver driver;
    private int iteration;
    private String fileName;
    public BluetoothToggleInterruption(AndroidDriver driver, int iteration, String fileName) {
        this.driver = driver;
        this.iteration = iteration;
        this.fileName = fileName;
    }

    public void runBluetoothOnOff() {
        System.out.println("⏱ Starting bluetooth on-off cycles");

        try {
            // Step 1: Click Start (if not already started)
            try {
                driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Start\"]")).click();
                System.out.println("▶️ Start button clicked.");
            } catch (Exception e) {
                System.out.println("⚠️ 'Start' button not found or already running.");
            }


            // Step 2: Perform 20 cycles of pause-resume via tap
            for (int i = 0; i < BLUETOOTH_ON_OFF_CYCLES; i++) {
                sleep(HOLD_DURATION_MS);
                BluetoothUtils.turnOffBluetooth(driver);
                sleep(250);
                if (isElementPresent(DEVICE_DISCONNECTED))
                {
                    ActionsUtil.Tap.withCoordinates(driver, 100,100);
                    BluetoothUtils.turnOnBluetooth(driver);
                    sleep(2000);
                    confirmOnHomeScreen(driver);
                    Navigation.openFanControl(driver);
                    if (!clickElementWithRetry(MENU_BUTTON, 3)) {
                        throw new RuntimeException("❌ Failed to click Menu button");
                    }
                    WebElement firmwareElement = findElementByContentDescStartsWith(FIRMWARE_VERSION_PREFIX);
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

                    driver.findElement(START_BUTTON).click();
                    sleep(1000);
                }
            }

            System.out.println("✅ All pause-resume cycles completed with high accuracy.");

        } catch (Exception e) {
            throw new RuntimeException("❌ Error during Bluetooth On-Off cycle: " + e.getMessage(), e);
        }
    }
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

    public void verifyFirmwareUpgradeAndClickDone(String expectedVersion) {
        System.out.println("🔍 Waiting for firmware upgrade success message...");

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
        confirmOnHomeScreen(driver);

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

    private boolean isElementPresent(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
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
    private String getAttribute(WebElement el, String attr) {
        try {
            return el.getDomAttribute(attr);
        } catch (Exception e) {
            return null;
        }
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
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

    private void openMenuAndWait() {
        if (!clickIfExists(MENU_BUTTON)) {
            throw new RuntimeException("❌ Menu button not found after returning to device control");
        }
        System.out.println("✅ Menu opened");
        sleep(3000); // Allow load
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


}
