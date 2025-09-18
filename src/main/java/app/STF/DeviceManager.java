package app.STF;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * DeviceManager2 - Thread-safe device pool for STF-managed Android devices.
 *
 * <p>Implements a FIFO queue to allocate unique devices across parallel test threads.
 * Designed for use with TestNG + Appium in multi-device environments.
 */
public class DeviceManager {
    private static final Queue<String> AVAILABLE_DEVICES = new ConcurrentLinkedQueue<>();
    private static volatile boolean initialized = false;

    // === Configurable Filters ===
    private static final List<String> BUSY_INDICATORS = List.of(
            "stop using", "in use", "disconnect", "disconnected", "offline"
    );

    /**
     * Initializes the device pool by scanning STF web interface.
     * Idempotent: only runs once per JVM lifecycle.
     *
     * @param driver WebDriver pointing to STF device list page
     */
    public static synchronized void init(WebDriver driver) {
        if (initialized) {
            System.out.println("DeviceManager2 already initialized. Skipping.");
            return;
        }

        Objects.requireNonNull(driver, "WebDriver cannot be null");

        try {
            List<WebElement> deviceItems = driver.findElements(By.tagName("li"));
            System.out.println("Found " + deviceItems.size() + " device elements.");

            deviceItems.stream()
                    .filter(DeviceManager::isValidDeviceElement)
                    .filter(elem -> !isDeviceBusy(elem))
                    .map(elem -> elem.getDomAttribute("id"))
                    .filter(Objects::nonNull)
                    .forEach(deviceId -> {
                        AVAILABLE_DEVICES.add(deviceId);
                        System.out.println("Added available device: " + deviceId);
                    });

            initialized = true;
            System.out.println("DeviceManager2 initialized with " + AVAILABLE_DEVICES.size() + " devices.");

            if (AVAILABLE_DEVICES.isEmpty()) {
                System.err.println("⚠️ No available devices found. Check STF status.");
            }

        } catch (Exception e) {
            System.err.println("Failed to initialize DeviceManager2: " + e.getMessage());
            throw new RuntimeException("Device discovery failed", e);
        }
    }

    /**
     * Checks if element represents a valid device container.
     */
    private static boolean isValidDeviceElement(WebElement elem) {
        String id = elem.getDomAttribute("id");
        return id != null && !id.trim().isEmpty();
    }

    /**
     * Determines if a device is busy based on button text.
     */
    private static boolean isDeviceBusy(WebElement deviceElement) {
        try {
            List<WebElement> buttons = deviceElement.findElements(By.cssSelector("button.btn"));
            return buttons.stream().anyMatch(btn -> {
                String text = btn.getText().trim().toLowerCase();
                return BUSY_INDICATORS.stream().anyMatch(text::contains);
            });
        } catch (Exception e) {
            System.err.println("Error checking device status: " + e.getMessage());
            return true; // Assume busy on error
        }
    }

    /**
     * Allocates a device from the pool (FIFO).
     *
     * @return Unique device ID
     * @throws RuntimeException if no devices are available
     */
    public static String getDevice() {
        if (!initialized) {
            throw new IllegalStateException("DeviceManager2 not initialized. Call init() first.");
        }

        String device = AVAILABLE_DEVICES.poll();
        if (device == null) {
            throw new RuntimeException("No available devices in the pool!");
        }

        System.out.println("Allocated device: " + device + " [Remaining: " + AVAILABLE_DEVICES.size() + "]");
        return device;
    }

    /**
     * Checks if any devices are available.
     */
    public static boolean hasAvailableDevices() {
        return !AVAILABLE_DEVICES.isEmpty();
    }

    /**
     * Gets current size of device pool.
     */
    public static int size() {
        return AVAILABLE_DEVICES.size();
    }

    /**
     * Resets the device pool (useful for dynamic environments).
     */
    public static synchronized void reset() {
        AVAILABLE_DEVICES.clear();
        initialized = false;
        System.out.println("DeviceManager2 reset.");
    }

    /**
     * Re-initializes the device pool with fresh scan.
     */
    public static void refresh(WebDriver driver) {
        reset();
        init(driver);
    }
}