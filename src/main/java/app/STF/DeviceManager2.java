package app.STF;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class DeviceManager2 {
    private static final Queue<String> availableDevices = new ConcurrentLinkedQueue<>();

    public static synchronized void init(WebDriver driver) {
        if (!availableDevices.isEmpty()) return;

        List<WebElement> elementList = driver.findElements(By.tagName("li"));
        List<WebElement> devicesList = elementList.stream()
                .filter(webElement -> webElement.getDomAttribute("id") != null)
                .collect(Collectors.toList());

        for (WebElement deviceElement : devicesList) {
            String deviceId = deviceElement.getDomAttribute("id");
            availableDevices.add(deviceId);
        }
    }

    public static String getDevice() {
        String device = availableDevices.poll(); // thread-safe removal
        if (device == null) {
            throw new RuntimeException("No available devices!");
        }
        return device;
    }
}
