package app.BLEOnlyFans;

import app.ScreenCheck.ScreenCheck;
import app.util.AppUtil;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static app.resources.Locators.FanLocators.*;
import static app.util.ActionsUtil.SSleep;
import static app.util.AppUtil.findOptionalElement;

public class FanManagementBLEOnly {
    private static AndroidDriver atomberg;
    private final WebDriverWait wait;

    public FanManagementBLEOnly(WebDriverWait wait) {
        this.wait = wait;
    }

    public FanManagementBLEOnly(AndroidDriver driver) {
        atomberg = driver;
        // Initialize WebDriverWait with a 10-second timeout
        this.wait = new WebDriverWait(atomberg, Duration.ofSeconds(10));
    }

    public void addBLEFan(){
        AppUtil.navigateToAddScreen(atomberg);

        System.out.println("Searching for available devices...");
        for (int attempt = 0; attempt < 10; attempt++){
            SSleep(15);
            System.out.println("15 seconds wait complete");// Wait for scan results

            List<WebElement> availableBLEDevice = atomberg.findElements(By.className("android.view.View")).stream()
                    .filter(element -> {
                        try {
                            String desc = element.getDomAttribute("content-desc");
                            return desc != null && desc.startsWith("Atomberg_R3_");
                        } catch (Exception e) {
                            return false; // Skip stale or inaccessible elements
                        }
                    })
                    .collect(Collectors.toList());
            System.out.println("Available BLE Device Count " + availableBLEDevice.size());
        }
    }

    public boolean checkFan() {
        ScreenCheck screen = new ScreenCheck(atomberg);
        screen.homeScreen();

        WebElement emptyFamily = findOptionalElement(atomberg, ADD_FIRST_DEVICE_ICON);
        if(emptyFamily != null){
            System.out.println("No Fan Added");
        }
        return emptyFamily != null;
    }

    public void connectToDevice(String devicePrefix) {
        // XPath to get all views that have content-desc (we'll filter manually)
        String candidateDeviceXpath = "//android.view.View[@content-desc]";
        String connectButtonXpath = "//android.view.View[@content-desc=\"Connect\"]";

        // Find all elements with content-desc (potential devices)
        List<WebElement> allElements = atomberg.findElements(By.xpath(candidateDeviceXpath));
        List<WebElement> matchedDevices = new ArrayList<>();

        // Filter elements whose content-desc starts with the given prefix
        for (WebElement elem : allElements) {
            String contentDesc = elem.getAttribute("content-desc");
            if (contentDesc != null && contentDesc.startsWith(devicePrefix)) {
                matchedDevices.add(elem);
            }
        }

        if (matchedDevices.isEmpty()) {
            System.out.println("No device found with content-desc starting with: " + devicePrefix);
            return;
        }

        // Find all Connect buttons
        List<WebElement> connectButtons = atomberg.findElements(By.xpath(connectButtonXpath));
        if (connectButtons.isEmpty()) {
            System.out.println("No Connect buttons found.");
            return;
        }

        // Store processed Y-centers to avoid duplicates
        Set<Integer> processedDeviceCenters = new HashSet<>();

        for (WebElement deviceElement : matchedDevices) {
            int deviceCenterY = getElementCenterY(deviceElement);

            // Skip duplicates (same row)
            if (processedDeviceCenters.contains(deviceCenterY)) {
                continue;
            }
            processedDeviceCenters.add(deviceCenterY);

            // Find the closest Connect button by center Y
            WebElement bestMatchButton = null;
            int minDiff = Integer.MAX_VALUE;

            for (WebElement button : connectButtons) {
                int buttonCenterY = getElementCenterY(button);
                int verticalDiff = Math.abs(buttonCenterY - deviceCenterY);

                if (verticalDiff < minDiff) {
                    minDiff = verticalDiff;
                    bestMatchButton = button;
                }
            }

            // Click if within reasonable distance (tolerance ~100px)
            if (bestMatchButton != null && minDiff <= 50) {
                String actualName = deviceElement.getAttribute("content-desc");
                System.out.println("Connecting to device: '" + actualName +
                        "' (matched by prefix: '" + devicePrefix + "')");
                bestMatchButton.click();
                return; // Success, exit after first valid match
            }
        }

        System.out.println("Failed to find a Connect button near device with prefix: " + devicePrefix);
    }

    /**
     * Calculates the vertical center (Y) of a WebElement.
     *
     * @param element The WebElement
     * @return Center Y coordinate
     */
    private int getElementCenterY(WebElement element) {
        int topY = element.getLocation().getY();
        int height = element.getSize().getHeight();
        return topY + (height / 2);
    }

}
