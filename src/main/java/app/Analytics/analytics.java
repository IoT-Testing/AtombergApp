package app.Analytics;

import app.util.ActionsUtil;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static app.resources.Locators.Android.AppLocators.Analytics.*;
import static app.resources.Locators.Android.HomeLocators.*;


/**
 * analytics - Handles analytics screen interactions: device switching, data inspection.
 *
 * <p>This version avoids all assertions and focuses on robust execution,
 * graceful failure, and reusability as a utility class.
 */
public class analytics {
    private final AndroidDriver atomberg;

    // Swipe constants
    private static final double SWIPE_START_X_RATIO = 0.75;
    private static final double SWIPE_Y_RATIO = 0.50;

    public analytics(AndroidDriver driver) {
        this.atomberg = driver;
    }

    /**
     * Navigates to Analytics and inspects available devices.
     */
    public void Show() {
        navigateToAnalytics();
        rateUs(); // Dismiss any popup

        if (isNoDeviceMessagePresent()) {
            System.out.println("Fan Not Available in Analytics");
            return;
        }

        System.out.println("Fan Available in Analytics");
        inspectAllDevices();
    }

    // === Internal Helpers ===

    /**
     * Switches to Analytics tab.
     */
    private void navigateToAnalytics() {
        try {
            WebElement analyticsTab = atomberg.findElement(ANALYTICS_TAB);
            analyticsTab.click();
            System.out.println("Switched to Analytics");
        } catch (NoSuchElementException e) {
            System.err.println("Analytics tab not found.");
        }
    }

    /**
     * Checks if "no devices" message is shown.
     */
    private boolean isNoDeviceMessagePresent() {
        try {
            return atomberg.findElement(NO_DEVICES_MESSAGE).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Inspects analytics for all available devices.
     */
    private void inspectAllDevices() {
        List<String> deviceNames = getAvailableDeviceNames();
        if (deviceNames.isEmpty()) {
            System.out.println("No selectable devices found.");
            return;
        }

        for (int i = 0; i < deviceNames.size(); i++) {
            String deviceName = deviceNames.get(i);
            selectDevice(deviceName);
            inspectDeviceData(deviceName);

            // Return to analytics unless last device
            if (i < deviceNames.size() - 1) {
                navigateBackToAnalytics();
            }
        }
    }

    /**
     * Gets list of available device names from UI.
     */
    private List<String> getAvailableDeviceNames() {
        fanChange(); // Open device list

        List<WebElement> elements = atomberg.findElements(AppiumBy.className("android.view.View"));
        return elements.stream()
                .map(el -> el.getDomAttribute("content-desc"))
                .filter(Objects::nonNull)
                .filter(desc -> desc.endsWith("Fan") || desc.endsWith("Purifier"))
                .collect(Collectors.toList());
    }

    /**
     * Opens the device selection dropdown.
     */
    private void fanChange() {
        List<WebElement> fans = atomberg.findElements(AppiumBy.className("android.view.View")).stream()
                .filter(el -> {
                    String desc = el.getDomAttribute("content-desc");
                    return desc != null && (desc.endsWith("Fan") || desc.contains("Select"));
                })
                .collect(Collectors.toList());

        if (!fans.isEmpty()) {
            fans.get(0).click();
            ActionsUtil.sleep(2000);
        }
    }

    /**
     * Selects a specific device by name.
     */
    private void selectDevice(String deviceName) {
        try {
            WebElement device = atomberg.findElement(By.xpath("//android.view.View[@content-desc='" + deviceName + "']"));
            device.click();
            ActionsUtil.sleep(2000);
        } catch (NoSuchElementException e) {
            System.err.println("Failed to select device: " + deviceName);
        }
    }

    /**
     * Inspects data screens based on device type.
     */
    private void inspectDeviceData(String deviceName) {
        int screenCount = deviceName.endsWith("Purifier") ? 2 : 4;

        for (int i = 0; i < screenCount; i++) {
            inspectInteractiveIcons();
            confetti(); // Check for confetti animation

            if (i < screenCount - 1) {
                ActionsUtil.Swipe.Left(atomberg, SWIPE_START_X_RATIO, SWIPE_Y_RATIO);
                ActionsUtil.sleep(1500);
            }
        }
    }

    /**
     * Clicks on all interactive icons in current analytics view.
     */
    private void inspectInteractiveIcons() {
        List<WebElement> icons = atomberg.findElements(CLICKABLE_ICONS).stream()
                .filter(el -> el.getDomAttribute("content-desc") == null)
                .collect(Collectors.toList());

        for (WebElement icon : icons) {
            try {
                icon.click();
                ActionsUtil.sleep(2000);
                atomberg.navigate().back();
                ActionsUtil.sleep(1500);
            } catch (Exception e) {
                System.err.println("Failed to interact with icon: " + e.getMessage());
            }
        }
    }

    /**
     * Checks for and interacts with confetti animation.
     */
    private void confetti() {
        System.out.println("Checking confetti");
        List<WebElement> images = atomberg.findElements(CONFETTI_IMAGE);

        List<WebElement> confettiCandidates = images.stream()
                .filter(el -> el.getDomAttribute("content-desc") == null)
                .filter(this::hasConfettiBounds) // Use method reference
                .collect(Collectors.toList());

        System.out.println("Confetti size: " + confettiCandidates.size());

        for (WebElement e : confettiCandidates) {
            try {
                System.out.println("Confetti bounds: " + e.getDomAttribute("bounds"));
                e.click();
                ActionsUtil.sleep(2000);
                atomberg.navigate().back();
            } catch (Exception ex) {
                System.err.println("Error interacting with confetti: " + ex.getMessage());
            }
        }
    }

    private boolean hasConfettiBounds(WebElement el) {
        try {
            String bounds = el.getDomAttribute("bounds");
            return bounds != null && bounds.endsWith("482]");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Navigates back to Analytics screen after visiting another tab.
     */
    private void navigateBackToAnalytics() {
        try {
            atomberg.findElement(MORE_TAB).click();
            rateUs();
            ActionsUtil.sleep(1500);
            atomberg.findElement(ANALYTICS_TAB).click();
            rateUs();
        } catch (Exception e) {
            System.err.println("Failed to return to Analytics: " + e.getMessage());
        }
    }

    /**
     * Dismisses 'Rate Us' or other modals.
     */
    public void rateUs() {
        try {
            List<WebElement> buttons = atomberg.findElements(CANCEL_BUTTON);
            for (WebElement btn : buttons) {
                if ("Cancel".equals(btn.getDomAttribute("content-desc"))) {
                    btn.click();
                    System.out.println("Canceled Rate us");
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Error dismissing popup: " + e.getMessage());
        }

        // Re-cache tabs after potential navigation
        try {
            atomberg.findElement(ANALYTICS_TAB); // Just verify existence
            atomberg.findElement(MORE_TAB);
        } catch (Exception ignored) {}
    }
}