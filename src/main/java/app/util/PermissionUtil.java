package app.util;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import static app.resources.Locators.Android.FanLocators.*;
import static app.resources.Locators.Android.HomeLocators.*;

/**
 * Utility class to handle Android runtime permissions and common startup popups.
 *
 * Refactored to:
 * - Eliminate duplication between allow() and allowForBrowserStack()
 * - Centralize locators
 * - Improve error visibility
 * - Follow single responsibility principle
 */
public class PermissionUtil {

    // === Locator Constants ===
    private static final By PERMISSION_ICON = By.id("com.android.permissioncontroller:id/permission_icon");
    private static final By ALLOW_BUTTON = By.id("com.android.permissioncontroller:id/permission_allow_button");
    private static final By ALLOW_FOREGROUND_ONLY_BUTTON = By.id("com.android.permissioncontroller:id/permission_allow_foreground_only_button");

    // Coordinate tap for Add button (fallback when no ID/XPath available)
    private static final int ADD_BUTTON_X = 540;
    private static final int ADD_BUTTON_Y = 1900;

    /**
     * Main method to handle permissions flow for standard environment.
     *
     * @param driver AndroidDriver instance
     */
    public static void allow(AndroidDriver driver) {
        new PermissionHandler(driver).handleStandardFlow();
    }

    /**
     * Handles permission flow optimized for BrowserStack environment.
     * @param driver AndroidDriver instance
     */
    public static void allowForBrowserStack(AndroidDriver driver) {
        new PermissionHandler(driver).handleBrowserStackFlow();
    }

    // === Internal Handler Class (Encapsulates Logic) ===
    private static class PermissionHandler {
        private final AndroidDriver driver;

        public PermissionHandler(AndroidDriver driver) {
            this.driver = driver;
        }

        /**
         * Full permission flow: check if permission dialog appears,
         * grant location + foreground access, handle enable button and Alexa popup.
         */
        public void handleStandardFlow() {
            if (isPermissionDialogPresent()) {
                System.out.println("Permissions dialog detected.");
                clickAllowButton();           // First allow (full permission)
                clickLocationPermission();    // Foreground only
                clickAllowButton();           // Final allow
                System.out.println("All Permissions Granted");
                alexaPopUp();                 // Optional cancel
                clickEnableIfPresent();       // Handle Enable button
            } else if (isAddDeviceButtonPresent(ADD_BUTTON_XPATH)) {
                handleEmptyFamilyFlow();
            }
        }

        /**
         * Simplified flow for BrowserStack: skips some steps assumed already granted.
         */
        public void handleBrowserStackFlow() {
            if (isPermissionDialogPresent()) {
                System.out.println("Permissions dialog detected (BrowserStack).");
                clickAllowForegroundOnly();   // Only one prompt expected
                System.out.println("Permissions Granted");
                alexaPopUp();                 // Cancel Alexa
            } else if (isAddDeviceButtonPresent(ADD_FIRST_DEVICE_ICON)) {
                handleEmptyFamilyFlow();
            }
        }

        // --- Helper Methods ---

        /**
         * Checks if system permission dialog is visible.
         *
         * @return true if permission icon is found
         */
        private boolean isPermissionDialogPresent() {
            return findOptionalElement(PERMISSION_ICON) != null;
        }

        /**
         * Clicks the main 'Allow' button.
         */
        private void clickAllowButton() {
            clickElement(ALLOW_BUTTON, "Allow Button");
        }

        /**
         * Clicks 'Allow only while using the app' (foreground).
         */
        private void clickLocationPermission() {
            clickElement(ALLOW_FOREGROUND_ONLY_BUTTON, "Location Permission");
        }

        /**
         * Clicks allow button used specifically in BrowserStack context.
         */
        private void clickAllowForegroundOnly() {
            clickElement(ALLOW_FOREGROUND_ONLY_BUTTON, "Allow Foreground Only");
        }

        /**
         * Navigates through empty family state: taps Add button and re-checks permissions.
         */
        private void handleEmptyFamilyFlow() {
            System.out.println("No device present – navigating to Add Device.");
            ActionsUtil.Tap.withCoordinates(driver, ADD_BUTTON_X, ADD_BUTTON_Y);

            // Re-check for permissions after tapping Add
            if (isPermissionDialogPresent()) {
                handleStandardFlow(); // Re-enter full flow
            } else {
                driver.navigate().back();
                System.out.println("Returned to previous screen.");
            }
        }

        /**
         * Looks for Alexa setup popup and cancels it.
         */
        private void alexaPopUp() {
            WebElement alexaPopup = findOptionalElement(ALEXA_POPUP);
            if (alexaPopup != null) {
                try {driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]")).click();

                    System.out.println("Alexa popup canceled.");
                } catch (Exception e) {
                    System.out.println("Failed to close Alexa popup: " + e.getMessage());
                }
            }
        }

        /**
         * Clicks 'Enable' button if shown after permissions.
         */
        private void clickEnableIfPresent() {
            try {
                WebElement enableBtn = driver.findElement(By.xpath("//android.widget.Button[@content-desc='Enable']"));
                if (enableBtn.isDisplayed()) {
                    enableBtn.click();
                    System.out.println("Enable button clicked.");
                }
            } catch (NoSuchElementException e) {
                // Ignore: not always present
            }
        }

        /**
         * Checks if a specific Add Device button is visible.
         *
         * @param xpathLocator The dynamic XPath to test
         * @return true if element exists
         */
        private boolean isAddDeviceButtonPresent(By xpathLocator) {
            return findOptionalElement(xpathLocator) != null;
        }

        /**
         * Safely finds an element without throwing exception.
         *
         * @param locator Element locator
         * @return WebElement or null
         */
        private WebElement findOptionalElement(By locator) {
            try {
                return driver.findElement(locator);
            } catch (NoSuchElementException e) {
                return null;
            }
        }

        /**
         * Clicks an element with logging.
         *
         * @param locator By strategy
         * @param label   Action label for logs
         */
        private void clickElement(By locator, String label) {
            try {
                driver.findElement(locator).click();
                System.out.println(label + " clicked.");
            } catch (Exception e) {
                System.err.println("Failed to click " + label + ": " + e.getMessage());
            }
        }
    }
}