package app.BLEOnlyFans;

import app.Fan.FanManagement;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import static app.resources.Locators.BLEFan.*;

public class FirmwareVersionCheckerTwo {

    private final AndroidDriver driver;
    private String previousExpectedVersion; // To verify in next iteration
    public static String expectedVersion;

    public FirmwareVersionCheckerTwo(AndroidDriver driver) {
        this.driver = driver;
    }

    public void runSequentialFirmwareUpdates() {
        System.out.println("🔄 Starting sequential firmware update process...");

        while (true) {

            if (!clickElementWithRetry(MENU_BUTTON, 3)) {
                throw new RuntimeException("❌ Failed to click Menu button");
            }
            sleep(2000);

            WebElement firmwareElement = findElementByContentDescStartsWith(FIRMWARE_VERSION_PREFIX);
            if (firmwareElement == null) {
                throw new RuntimeException("❌ 'Firmware Version' option not found");
            }

            String currentVersion = extractVersionFromFirmwareText(Objects.requireNonNull(firmwareElement.getDomAttribute("content-desc")));
            System.out.println("📄 Current Firmware: " + currentVersion);

            if (previousExpectedVersion != null && !currentVersion.equals(previousExpectedVersion)) {
                throw new RuntimeException(
                        "❌ Version mismatch! Expected after previous upload: " + previousExpectedVersion +
                                ", But found: " + currentVersion);
            } else if (previousExpectedVersion != null) {
                System.out.println("✅ Confirmed: Firmware is at expected version " + currentVersion);
            }

            firmwareElement.click();
            System.out.println("✅ Clicked on Firmware Version");
            sleep(2000);

            if (!clickElementWithRetry(SELECT_FILE_OPTION, 3)) {
                throw new RuntimeException("❌ 'Select File' option not clickable");
            }
            System.out.println("📁 Select File clicked. Waiting for file picker...");
            sleep(3000);

            List<String> availableFiles = getSortedBinFiles();
            Optional<String> nextFileOpt = availableFiles.stream()
                    .filter(file -> compareVersions(file, currentVersion) > 0)
                    .findFirst();

            if (!nextFileOpt.isPresent()) {
                System.out.println("✅ All firmware updates completed. No more files to upload.");
                break; // Exit loop
            }

            String fileName = nextFileOpt.get();
            expectedVersion = extractVersionFromFilename(fileName);

            System.out.println("\n🚀 Starting Update");
            System.out.println("📄 Firmware File: " + fileName);
            System.out.println("🎯 Expected Version: " + expectedVersion);

            selectAndUploadFile(fileName);
//            UploadFirmware firmware = new UploadFirmware(driver);
//            firmware.runProgressiveFWUpload();
            ProgressivePauseResume validator = new ProgressivePauseResume(driver);
            previousExpectedVersion = validator.expectedVersion;
            validator.runProgressivePauseResume();
        }

        System.out.println("✅ All pending firmware updates completed successfully!");
        driver.navigate().back();
        driver.navigate().back();
        driver.navigate().back();
        driver.navigate().back();
        FanManagement fan = new FanManagement(driver);
        fan.fanControl();
    }

    // === Internal Helpers ===

    /**
     * Finds element with content-desc starting with given prefix.
     */
    private WebElement findElementByContentDescStartsWith(String prefix) {
        return driver.findElements(By.className("android.view.View")).stream()
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
     * Gets all .bin files starting with "Production_"
     */
    private List<String> getSortedBinFiles() {
        return driver.findElements(FILE_LIST_ITEM).stream()
                .map(this::getTextSafe)
                .filter(text -> text != null && text.startsWith("Production_") && text.endsWith(".bin"))
                .sorted(this::compareVersionsByName)
                .collect(Collectors.toList());
    }

    /**
     * Safely get text from TextView.
     */
    private String getTextSafe(WebElement el) {
        try {
            return el.getText().trim();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Compares two filenames by semantic version.
     */
    private int compareVersionsByName(String f1, String f2) {
        return compareVersions(f1, f2);
    }

    /**
     * Compare file version vs current firmware string
     */
    private int compareVersions(String filename, String currentVersion) {
        String fileVersion = extractVersionFromFilename(filename);
        return compareSemanticVersions(fileVersion, currentVersion);
    }

    /**
     * Extracts version like "1.0.1" from "Production_1.0.1.bin"
     */
    private String extractVersionFromFilename(String filename) {
        Matcher m = VERSION_PATTERN.matcher(filename);
        return m.find() ? m.group(1) : "0.0.0";
    }

    /**
     * Compares semantic versions: 1.0.1 vs 1.0.2
     */
    private int compareSemanticVersions(String v1, String v2) {
        if (v1 == null || v2 == null) return 0;

        String[] a = v1.split("\\.");
        String[] b = v2.split("\\.");

        for (int i = 0; i < Math.max(a.length, b.length); i++) {
            int x = i < a.length ? parseIntSafely(a[i], 0) : 0;
            int y = i < b.length ? parseIntSafely(b[i], 0) : 0;
            if (x != y) return x > y ? 1 : -1;
        }
        return 0;
    }

    /**
     * Safely parses integer with fallback.
     */
    private int parseIntSafely(String s, int fallback) {
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return fallback;
        }
    }

    /**
     * Clicks the file directly to select it.
     */
    private void selectAndUploadFile(String fileName) {
        System.out.println("📂 Selecting file: " + fileName);

        By fileLocator = By.xpath("//android.widget.TextView[@resource-id='android:id/title'][@text='" + fileName + "']");
        if (!clickElementWithRetry(fileLocator, 3)) {
            throw new RuntimeException("❌ Could not click file: " + fileName);
        }

        System.out.println("✅ File selected: " + fileName);
        sleep(2000);
    }

    /**
     * Safely clicks an element with retry.
     */
    private boolean clickElementWithRetry(By locator, int maxRetries) {
        for (int i = 0; i < maxRetries; i++) {
            try {
                WebElement el = driver.findElement(locator);
                if (el.isDisplayed()) {
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


    /**
     * Fail-safe for File Transfer Failed
     */
    private void fileTransferFailSafe(){

    }
}
