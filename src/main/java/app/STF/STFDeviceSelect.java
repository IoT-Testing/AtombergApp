package app.STF;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class STFDeviceSelect {
    public STFDeviceSelect(WebDriver driver) {
        try {
            // Get all <li> device elements
            List<WebElement> elementList = driver.findElements(By.tagName("li"));
            // Filter out non-device elements (ensure 'id' attribute is not null)
            List<WebElement> devicesList = elementList.stream()
                    .filter(webElement -> webElement.getDomAttribute("id") != null)
                    .collect(Collectors.toList());
            // Further filter to include only devices that do NOT have the "Stop Using" button
            List<WebElement> availableDevices = devicesList.stream()
                    .filter(device -> {
                        List<WebElement> buttons = device.findElements(By.cssSelector("button.btn"));
                        for (WebElement button : buttons) {
                            String text = button.getText().trim();
                            if (text.equalsIgnoreCase("Stop Using")) {
                                return false; // Exclude in-use devices
                            }
                            if (text.equalsIgnoreCase("Disconnected")) {
                                return false; // Exclude in-use devices
                            }
                            if (text.equalsIgnoreCase("Offline")) {
                                return false; // Exclude in-use devices
                            }
                        }
                        return true; // Include available ones
                    })
                    .collect(Collectors.toList());
            if (availableDevices.isEmpty()) {
                System.out.println("No available devices found.");
            } else {
                // Select a random available device
                int i = new Random().nextInt(availableDevices.size());
                WebElement selectedDevice = availableDevices.get(i);
                String deviceId = selectedDevice.getAttribute("id");
                System.out.println("Device number " + i + " selected. Device ID: " + deviceId);
                selectedDevice.click();
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
