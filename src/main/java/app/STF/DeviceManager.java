package app.STF;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DeviceManager {
    private static final List<String> availableDevices = new ArrayList<>();

    public static synchronized void init(WebDriver driver) {
        if (!availableDevices.isEmpty()) return; // already initialized

        List<WebElement> deviceElements = driver.findElements(By.tagName("li"));
        List<String> detectedDevices = deviceElements.stream()
                .filter(elem -> elem.getDomAttribute("id") != null)
                .filter(elem -> {
                    List<WebElement> buttons = elem.findElements(By.cssSelector("button.btn"));
                    return buttons.stream().noneMatch(btn -> {
                        String text = btn.getText().trim().toLowerCase();
                        return text.contains("stop using") || text.contains("offline") || text.contains("disconnected");
                    });
                })
                .map(elem -> elem.getDomAttribute("id"))
                .collect(Collectors.toList());

        availableDevices.addAll(detectedDevices);
    }

    public static synchronized List<String> getAvailableDevices() {
        return new ArrayList<>(availableDevices);
    }

    public static synchronized boolean isDeviceAvailable(int requiredCount) {
        return availableDevices.size() >= requiredCount;
    }
}
