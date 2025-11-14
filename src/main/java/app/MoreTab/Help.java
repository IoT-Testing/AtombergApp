package app.MoreTab;

import app.util.ActionsUtil;
import app.util.AppUtil;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.appmanagement.ApplicationState;
import org.openqa.selenium.*;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import static app.resources.Locators.Android.MoreTabLocators.*;
import static org.awaitility.Awaitility.await;

public class Help {
    private final AndroidDriver atomberg;


    // Fan model names (used in troubleshoot)
    private static final String[] FAN_MODELS = {
            "Renesa", "Renesa Smart", "Renesa+", "Renesa Smart+",
            "Studio+", "Studio Smart+", "Erica", "Erica Smart", "Renesa Halo",
            "Renesa Elite", "Renesa Elite Smart", "Studio Nexus","Studio Nexus Smart",
            "Aris Starlight", "Aris", "Aris Contour", "Renesa Alpha", "Efficio",
            "Ikano", "Ozeo", "Ameza", "Other"
    };

    public Help(AndroidDriver driver) {
        this.atomberg = driver;
    }

    // === Public Methods ===

    public void newComplaint() {
        if (!clickElementIfExists(NEW_COMPLAINT_BUTTON, "New Complaint")) return;
        videoTryCatch();
        ActionsUtil.sleep(2000);
    }

    public void installationRequest() {
        if (!clickElementIfExists(INSTALLATION_REQUEST_BUTTON, "Installation Request")) return;
        videoTryCatch();
        ActionsUtil.sleep(2000);
    }

    public void serviceRequest() {
        if (!clickElementIfExists(SERVICE_REQUEST_BUTTON, "Service Request")) return;
        videoTryCatch();
        ActionsUtil.sleep(2000);
    }

    public void trackAComplaint() {
        if (!clickElementIfExists(TRACK_COMPLAINTS_BUTTON, "Track Complaints")) return;

        waitForPresence(COMPLAINT_STATUS_HEADER, 10);

        if (isElementPresent(NO_COMPLAINTS_INDICATOR)) {
            System.out.println("No Complaints Raised.");
        } else {
            System.out.println("Complaints are present.");
        }
        videoTryCatch();
    }

    public void manual() {
        if (!clickElementIfExists(MANUAL_BUTTON, "Manual")) return;
        System.out.println("Manual Open");
        ActionsUtil.sleep(2500);
        videoTryCatch();
    }

    public void ConnectivityTroubleshoot() {
        if (!clickElementIfExists(CONNECTIVITY_TROUBLESHOOT, "Connectivity Troubleshoot")) return;

        // Fans
        selectAndTestDevice("Atomberg Fan", FAN_MODELS);

        // Locks
        selectAndTestDevice("Atomberg Lock", new String[]{"Lock"});

        // Water Purifier
        selectAndTestDevice("Atomberg Water Purifier", new String[]{"Purifier"});

        videoTryCatch();
    }

    public void email() {
        if (!clickElementIfExists(EMAIL_US_BUTTON, "Email Us")) return;
        navigateBackTo(HELP_HEADER, "Help screen");
    }

    public void call() {
        if (!clickElementIfExists(CALL_US_BUTTON, "Call Us")) return;
        navigateBackTo(HELP_HEADER, "Help screen");
    }

    // === Internal Helpers ===

    /**
     * Safely clicks element if present and visible.
     */
    private boolean clickElementIfExists(By locator, String label) {
        try {
            WebElement el = atomberg.findElement(locator);
            if (el.isDisplayed()) {
                el.click();
                System.out.println("Tap on " + label);
                AppUtil.captureScreenshot(atomberg, label);
                return true;
            } else {
                System.out.println(label + " found but not displayed.");
                return false;
            }
        } catch (NoSuchElementException e) {
            System.out.println(label + " not found.");
            return false;
        } catch (Exception e) {
            System.err.println("Error clicking " + label + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Selects a category and iterates through its models.
     */
    private void selectAndTestDevice(String category, String[] models) {
        if (!clickElementIfExists(By.xpath("//android.widget.ImageView[@content-desc='" + category + "']"), category)) {
            System.err.println("Failed to enter category: " + category);
            return;
        }

        for (String model : models) {
            try {
                By modelLocator = By.xpath("//android.widget.ImageView[@content-desc='" + model + "']");
                WebElement device = atomberg.findElement(modelLocator);
                device.click();
                AppUtil.captureScreenshot(atomberg, "Model Selected");
                System.out.println(category + ": " + model);

                if (model.contains("Smart") || "Other".equals(model)) {
                    EnterSerialNumber();
                } else {
                    OK();
                }

                // Return only if not already at top level
                if (!category.equals("Atomberg Lock")) {
                    ReturnToHome();
                }
            } catch (NoSuchElementException e) {
                System.out.println("Model not available: " + model + " (optional)");

            } catch (Exception e) {
                System.err.println("Unexpected error during model selection: " + e.getMessage());
            }
        }

        if (!category.equals("Atomberg Lock")) {
            atomberg.navigate().back();
        }
    }

    /**
     * Handles serial number input and camera permission.
     */
    private void EnterSerialNumber() {
        try {
            WebElement manualEnter = waitForElement(By.xpath("//android.widget.EditText"), 5);
            if (manualEnter == null) return;

            manualEnter.click();
            AppUtil.captureScreenshot(atomberg, "Enter Barcode Manually");
            System.out.println("Enter Barcode Manually...");

            List<WebElement> images = manualEnter.findElements(By.tagName("ImageView"));
            if (!images.isEmpty()) {
                images.get(0).click();
                AppUtil.captureScreenshot(atomberg, "Scan Barcode");
                System.out.println("Scan Barcode ...");
            }

            // Handle camera permission prompt
            if (isElementPresent(By.id("com.android.permissioncontroller:id/permission_message"))) {
                try {
                    atomberg.findElement(By.id("com.android.permissioncontroller:id/permission_allow_foreground_only_button")).click();
                    AppUtil.captureScreenshot(atomberg, "Permission");
                    ActionsUtil.sleep(2000);
                } catch (Exception ignored) {}
            }

            atomberg.navigate().back();
            cantFindSerialNumber();

            // Wait for identify screen
            waitForElement(By.xpath("//android.view.View[@content-desc='Identify your device']"), 10);

        } catch (Exception e) {
            System.err.println("Error during serial number entry: " + e.getMessage());
        }
    }

    /**
     * Clicks 'Return to home' button.
     */
    private void ReturnToHome() {
        clickElementIfExists(RETURN_TO_HOME_BUTTON, "Return To Home");
    }

    /**
     * Clicks 'OK' button.
     */
    private void OK() {
        clickElementIfExists(OK_BUTTON, "OK");
    }

    /**
     * Handles "Can't find serial number?" flow.
     */
    private void cantFindSerialNumber() {
        if (!clickElementIfExists(CANT_FIND_SERIAL_NUMBER, "Can't find serial number?")) return;
        List<WebElement> buttons = getVisibleButtons();
        for (WebElement btn : buttons) {
            String desc = btn.getDomAttribute("content-desc");
            if ("Yes".equals(desc)) {
                btn.click();
                clickElementIfExists(DOWNLOAD_BUTTON, "Download");
                break;
            } else if ("No".equals(desc)) {
                btn.click();
                clickElementIfExists(APP_EMAIL_OPTION, "App Email");
                System.out.println("app Email ...");
                atomberg.navigate().back();

                clickElementIfExists(GENERIC_EMAIL_OPTION, "Generic Email");
                System.out.println("generic Email ...");
                atomberg.navigate().back();

                clickElementIfExists(CALL_OPTION, "Call");
                System.out.println("Call ...");

                waitForElement(CONTACT_SUPPORT_HEADER, 10);
                break;
            }
        }

        // Reopen dialog
        navigateBackTo(CANT_FIND_SERIAL_NUMBER, "Can't find serial number?");
        clickElementIfExists(CANT_FIND_SERIAL_NUMBER, "Reopen serial number help");
    }

    /**
     * Recovers to Video Tutorials link by navigating back.
     */
    private void videoTryCatch() {
        int attempts = 0;
        while (!isElementPresent(VIDEO_TUTORIALS_LINK) && attempts < 10) {
            System.out.println("Navigating back... attempt " + (++attempts));
            atomberg.navigate().back();
            ActionsUtil.sleep(1000);

            ApplicationState state = atomberg.queryAppState("com.atomberg.app");
            if (state != ApplicationState.RUNNING_IN_FOREGROUND) {
                atomberg.activateApp("com.atomberg.app");
                ActionsUtil.SSleep(5);
            }
        }

        if (!isElementPresent(VIDEO_TUTORIALS_LINK)) {
            System.err.println("Failed to reach 'Video tutorials' after 10 back presses.");
        }
    }

    /**
     * Navigates back until target element is found.
     */
    private void navigateBackTo(By target, String description) {
        int backCount = 0;
        while (!isElementPresent(target) && backCount < 10) {
            atomberg.navigate().back();
            backCount++;
        }
        if (!isElementPresent(target)) {
            System.err.println("Could not return to: " + description);
        }
    }

    // === Utility Methods ===

    /**
     * Safely checks if element is present and displayed.
     */
    private boolean isElementPresent(By locator) {
        try {
            return atomberg.findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Waits up to N seconds for element to be present.
     *
     * @param locator    Element locator
     * @param timeoutSec Timeout in seconds
     * @return WebElement if found, null otherwise
     */
    private WebElement waitForElement(By locator, long timeoutSec) {
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < timeoutSec * 1000) {
            try {
                return atomberg.findElement(locator);
            } catch (NoSuchElementException ignored) {
                ActionsUtil.sleep(500);
            }
        }
        return null;
    }

    /**
     * Waits for element presence using Awaitility.
     */
    private void waitForPresence(By locator, long timeoutSec) {
        try {
            await().atMost(Duration.ofSeconds(timeoutSec))
                    .pollInterval(Duration.ofMillis(500))
                    .until(() -> isElementPresent(locator));
        } catch (Exception e) {
            System.err.println("Timed out waiting for: " + locatorToString(locator));
        }
    }

    /**
     * Gets all visible buttons with non-null content-desc.
     */
    private List<WebElement> getVisibleButtons() {
        return atomberg.findElements(By.className("android.widget.Button")).stream()
                .filter(el -> el.getDomAttribute("content-desc") != null)
                .collect(Collectors.toList());
    }

    /**
     * Converts By to readable string for logs.
     */
    private String locatorToString(By by) {
        return by.toString().split("-> ")[1];
    }
}