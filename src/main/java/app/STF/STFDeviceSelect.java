package app.STF;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.*;
import java.util.stream.Collectors;

/**
 * STFDeviceSelect - Selects a random available device from STF web UI.
 *
 * <p>Filters out devices that are:
 * <ul>
 *   <li>In use ("Stop Using")</li>
 *   <li>Disconnected</li>
 *   <li>Offline</li>
 * </ul>
 *
 * <p>This version improves error handling, maintainability, and reusability.
 */
public class STFDeviceSelect {

    // === Configurable Busy Indicators ===
    private static final List<String> BUSY_BUTTON_TEXTS = Arrays.asList(
            "stop using", "disconnected", "offline", "in use"
    );

    /**
     * Constructor: Finds and clicks on a random available device.
     *
     * @param driver WebDriver instance at STF device list page
     * @throws IllegalStateException if no available devices are found
     */
    public STFDeviceSelect(WebDriver driver) {
        Objects.requireNonNull(driver, "WebDriver cannot be null");

        try {
            List<WebElement> allDevices = driver.findElements(By.tagName("li"));
            System.out.println("Found " + allDevices.size() + " total device elements.");

            List<WebElement> availableDevices = filterAvailableDevices(allDevices);

            if (availableDevices.isEmpty()) {
                throw new IllegalStateException("No available devices found on STF. All devices are busy, offline, or disconnected.");
            }

            WebElement selectedDevice = getRandomElement(availableDevices);
            String deviceId = getDeviceId(selectedDevice);

            selectedDevice.click();
            System.out.println("Selected and clicked device: ID=" + deviceId + ", Index=" + allDevices.indexOf(selectedDevice));
        } catch (IllegalStateException e) {
            System.err.println("Device selection failed: " + e.getMessage());
            throw e; // Fail fast
        } catch (Exception e) {
            System.err.println("Unexpected error during device selection: " + e.getMessage());
            throw new RuntimeException("Failed to select device from STF", e);
        }
    }

    // === Internal Helpers ===

    /**
     * Filters device elements to include only those that are available.
     */
    private List<WebElement> filterAvailableDevices(List<WebElement> deviceElements) {
        return deviceElements.stream()
                .filter(this::isValidDeviceElement)
                .filter(device -> !isDeviceBusy(device))
                .collect(Collectors.toList());
    }

    /**
     * Checks if element is a valid device container (has non-empty ID).
     */
    private boolean isValidDeviceElement(WebElement elem) {
        String id = elem.getDomAttribute("id");
        return id != null && !id.trim().isEmpty();
    }

    /**
     * Determines if a device is busy based on button text.
     */
    private boolean isDeviceBusy(WebElement deviceElement) {
        try {
            List<WebElement> buttons = deviceElement.findElements(By.cssSelector("button.btn"));
            return buttons.stream().anyMatch(btn -> {
                String text = btn.getText().trim().toLowerCase();
                return BUSY_BUTTON_TEXTS.stream().anyMatch(text::contains);
            });
        } catch (Exception e) {
            System.err.println("Error checking device status: " + e.getMessage());
            return true; // Assume busy on error
        }
    }

    /**
     * Gets device ID safely.
     */
    private String getDeviceId(WebElement device) {
        String id = device.getDomAttribute("id");
        return id != null ? id : "unknown";
    }

    /**
     * Returns a random element from the list.
     */
    private <T> T getRandomElement(List<T> list) {
        int index = new Random().nextInt(list.size());
        return list.get(index);
    }
}