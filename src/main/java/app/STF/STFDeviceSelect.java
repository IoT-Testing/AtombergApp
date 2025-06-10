package app.STF;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class STFDeviceSelect {
    private static final Set<String> allocatedDevices = ConcurrentHashMap.newKeySet();
    private static final Object lock = new Object();
    public String selectedDeviceId = null;

    public STFDeviceSelect(WebDriver driver) {
        try {
            List<WebElement> elementList = driver.findElements(By.tagName("li"));
            // Get all device elements with an ID
            List<WebElement> devicesList = elementList.stream()
                    .filter(webElement -> webElement.getDomAttribute("id") != null)
                    .collect(Collectors.toList());
            // Map device ID -> WebElement for easier lookup
            Map<String, WebElement> deviceMap = new HashMap<>();
            for (WebElement device : devicesList) {
                String id = device.getAttribute("id");
                if (isAvailable(device)) {
                    deviceMap.put(id, device);
                }
            }
            // Synchronize selection to avoid duplicate allocation
            synchronized (lock) {
                Optional<Map.Entry<String, WebElement>> available = deviceMap.entrySet().stream()
                        .filter(entry -> !allocatedDevices.contains(entry.getKey()))
                        .findFirst();

                if (available.isPresent()) {
                    selectedDeviceId = available.get().getKey();
                    WebElement selectedDevice = available.get().getValue();
                    allocatedDevices.add(selectedDeviceId);
                    System.out.println("Device selected: " + selectedDeviceId);
                    selectedDevice.click();
                } else {
                    System.out.println("No available devices left to allocate.");
                }
            }

        } catch (Exception e) {
            System.out.println("Exception in STFDeviceSelect: " + e.getMessage());
        }
    }

    private boolean isAvailable(WebElement device) {
        try {
            List<WebElement> buttons = device.findElements(By.cssSelector("button.btn"));
            for (WebElement button : buttons) {
                String text = button.getText().trim();
                if (text.equalsIgnoreCase("Stop Using") ||
                        text.equalsIgnoreCase("Disconnected") ||
                        text.equalsIgnoreCase("Offline")) {
                    return false;
                }
            }
        } catch (Exception ignored) {}
        return true;
    }

    public static void releaseDevice(String deviceId) {
        if (deviceId != null) {
            allocatedDevices.remove(deviceId);
            System.out.println("Device released: " + deviceId);
        }
    }
}
