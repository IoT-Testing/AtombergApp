package app.Assistants;

import app.ScreenCheck.ScreenCheck;
import app.util.ActionsUtil;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.*;
import static app.util.AppUtil.*;
import static app.resources.Locators.Android.MoreTabLocators.*;
import static app.resources.Messages.*;

/**
 * Alexa - Handles linking/unlinking Atomberg account with Amazon Alexa.
 */
public class Alexa {
    private final AndroidDriver driver;
    private final ScreenCheck screenCheck;

    // Test credentials (should be externalized in production)
    private static final String TEST_EMAIL = "iot.alpha@protonmail.com";
    private static final String TEST_PASSWORD = "Atomberg@123";

    public Alexa(AndroidDriver driver) {
        this.driver = driver;
        this.screenCheck = new ScreenCheck(driver);
    }

    /**
     * Connects Atomberg account to Amazon Alexa.
     */
    public void Connect() {
        screenCheck.moreTab();
        connectToAlexa();
    }

    /**
     * Disconnects Atomberg from Amazon Alexa.
     */
    public void Disconnect() {
        if (!isOnDeviceSelectionScreen()) {
            navigateBackToHome();
        }
        WebElement connected = driver.findElement(ALEXA_CONNECTED_BUTTON);
        if (connected != null && connected.isDisplayed()) {
            connected.click();
            System.out.println("Unlinking Alexa...");

            if (clickElementIfExists(YES_BUTTON, "Yes (Confirm Unlink)")) {
                waitForAndLogMessage(UNLINK_SUCCESS, "Alexa Unlink Successful");
            }
        } else {
            System.out.println("Alexa is not currently connected.");
        }
    }

    // === Internal Helpers ===

    /**
     * Attempts to connect to Alexa.
     */
    private void connectToAlexa() {
        if (!clickElementIfExists(ALEXA_CONNECT_BUTTON, "Amazon Alexa Connect")) {
            System.out.println("Alexa is already connected or button not found.");
            return;
        }

        System.out.println("Alexa Connect");

        handlePairAlexaPrompt();
        handleAccountLinkingGuide();
        performWebLoginIfRequired();
        confirmLinkSuccess();
    }

    /**
     * Clicks 'Link' if 'Pair Alexa' prompt is shown.
     */
    private void handlePairAlexaPrompt() {
        if (isElementPresent(driver, PAIR_ALEXA_TEXT)) {
            clickElementIfExists(LINK_BUTTON, "Link (Pair Alexa)");
        }
    }

    /**
     * Closes account linking guide modal if present.
     */
    private void handleAccountLinkingGuide() {
        if (isElementPresent(driver, ACCOUNT_LINKING_GUIDE)) {
            clickElementIfExists(OK_BUTTON, "OK (Account Linking Guide)");
            ActionsUtil.sleep(5000);

            // Tap LINK in native view or web context?
            try {
                driver.findElement(By.xpath("//android.widget.TextView[@text='LINK']")).click();
                ActionsUtil.sleep(5000);
            } catch (NoSuchElementException e) {
                System.err.println("'LINK' button not found in native view.");
            }
        }
    }

    /**
     * Performs login in WebView if signin screen is detected.
     */
    private void performWebLoginIfRequired() {
        if (!isElementPresent(driver, WEBVIEW_SIGNIN)) {
            return;
        }

        System.out.println("Performing Alexa account login...");

        if (!fillInputField(USERNAME_FIELD, TEST_EMAIL, "Email")) return;
        if (!fillInputField(PASSWORD_FIELD, TEST_PASSWORD, "Password")) return;

        if (clickElementIfExists(SUBMIT_BUTTON, "Submit (Login)")) {
            ActionsUtil.sleep(3000);
        }
    }

    /**
     * Fills input field safely.
     */
    private boolean fillInputField(By locator, String value, String label) {
        WebElement field = waitForElement(driver, locator, 10);
        if (field == null) {
            System.err.println(label + " field not found.");
            return false;
        }
        field.click();
        field.sendKeys(value);
        System.out.println(label + " entered: " + maskSensitiveData(value));
        return true;
    }

    /**
     * Confirms successful linking.
     */
    private void confirmLinkSuccess() {
        if (isElementPresent(driver, ALEXA_LINK_SUCCESS)) {
            System.out.println("Alexa Linked Successfully");
            // Tap somewhere safe to dismiss toast
            ActionsUtil.Tap.withPercentage(driver, 0.20, 0.20);
        } else {
            System.err.println("Link success message not displayed.");
        }
    }

    /**
     * Navigates back to home-like screen.
     */
    private void navigateBackToHome() {
        int backCount = 0;
        final int MAX_BACK_PRESS = 10;

        while (!isOnDeviceSelectionScreen() && backCount < MAX_BACK_PRESS) {
            System.out.println("Navigating back... (" + (backCount + 1) + "/" + MAX_BACK_PRESS + ")");
            driver.navigate().back();
            ActionsUtil.sleep(1000);
            backCount++;
        }

        if (!isOnDeviceSelectionScreen()) {
            System.err.println("Failed to return to expected screen after " + MAX_BACK_PRESS + " back presses.");
        }
    }

    /**
     * Checks if currently on main device selection screen.
     */
    private boolean isOnDeviceSelectionScreen() {
        return isElementPresent(driver, SELECT_AND_LINK_DEVICE_HEADER);
    }

    /**
     * Waits for message and logs result.
     */
    private void waitForAndLogMessage(By locator, String logMessage) {
        if (isElementPresent(driver, locator)) {
            System.out.println(logMessage);
        } else {
            System.err.println("Expected message not found: " + logMessage);
        }
    }

    // === Utility Methods ===

    /**
     * Safely clicks element if present and displayed.
     */
    private boolean clickElementIfExists(By locator, String label) {
        try {
            WebElement el = driver.findElement(locator);
            if (el.isDisplayed()) {
                el.click();
                System.out.println("Clicked: " + label);
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
     * Masks sensitive data in logs.
     */
    private String maskSensitiveData(String text) {
        if (text.length() <= 4) return "****";
        return "*" + text.substring(text.length() - 4);
    }
}