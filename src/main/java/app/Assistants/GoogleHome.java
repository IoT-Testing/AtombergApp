package app.Assistants;

import app.resources.Credentials;
import app.ScreenCheck.ScreenCheck;
import app.util.ActionsUtil;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.*;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import static app.util.AppUtil.*;

/**
 * GoogleHome - Handles linking/unlinking Atomberg account with Google Assistant.
 *
 * <p>This version avoids assertions and focuses on robust execution,
 * graceful failure, and reusability as a utility class.
 */
public class GoogleHome {
    private final AndroidDriver driver;
    private final ScreenCheck screenCheck;

    // === Locator Constants ===
    private static final By GOOGLE_CONNECT_BUTTON = By.xpath("//android.widget.ImageView[@content-desc='Google\nConnect']");
    private static final By GOOGLE_CONNECTED_BADGE = By.xpath("//android.widget.ImageView[@content-desc='Google\nConnected']");
    private static final By ACCOUNT_LINKING_GUIDE = By.xpath("//android.view.View[@content-desc='Account linking guide']");
    private static final By OK_BUTTON = By.xpath("//android.widget.Button[@content-desc='OK']");
    private static final By CONTINUE_BUTTON = By.xpath("//android.widget.Button[@text='Continue']");
    private static final By USERNAME_FIELD = By.id("signInFormUsername");
    private static final By PASSWORD_FIELD = By.id("signInFormPassword");
    private static final By SUBMIT_BUTTON = By.xpath("//android.widget.Button[@text='submit']");
    private static final By SIGN_IN_AS_BUTTONS = AppiumBy.className("android.widget.Button"); // Filter by text later
    private static final By UNLINK_ACCOUNT = By.xpath("//android.widget.TextView[@text='Unlink account']");
    private static final By UNLINK_CONFIRM_BUTTON = By.xpath("//android.widget.Button[@text='UNLINK']");
    private static final By NAVIGATE_UP = By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']");
    private static final By CREATE_HOME_BUTTON = By.xpath("//android.widget.Button[@content-desc='Create home']");
    private static final By SELECT_AND_LINK_DEVICE_HEADER = By.xpath("//android.view.View[@content-desc='Select and link device']");

    // Credentials loaded from environment â€” see test.env.example
    private static final String TEST_EMAIL    = Credentials.GOOGLE_TEST_EMAIL;
    private static final String TEST_PASSWORD = Credentials.GOOGLE_TEST_PASSWORD;

    public GoogleHome(AndroidDriver driver) {
        this.driver = driver;
        this.screenCheck = new ScreenCheck(driver);
    }

    /**
     * Connects Atomberg account to Google Home.
     */
    public void Connect() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        screenCheck.moreTab();
        connectToGoogleHome();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    /**
     * Disconnects Atomberg from Google Home.
     */
    public void Disconnect() {
        screenCheck.moreTab();

        if (!clickElementIfExists(GOOGLE_CONNECTED_BADGE, "Google Connected")) {
            logpoint("Google is not currently connected.");
            return;
        }

        ActionsUtil.sleep(2500);
        handleGoogleHomeControl();

        // Verify unlinked state
        verifyGoogleUnlinked();
    }

    // === Internal Helpers ===

    /**
     * Attempts to connect to Google Home.
     */
    private void connectToGoogleHome() {
        if (!clickElementIfExists(GOOGLE_CONNECT_BUTTON, "Google Connect")) {
            logpoint("Google Home is already connected.");
            return;
        }

        logpoint("Connecting Google Home...");
        handleAccountLinkingGuide();
        checkContinueOrTapFallback();
        performLoginIfRequired();
        navigateUpAndHandleCreateHome();
    }

    /**
     * Closes account linking guide modal if present.
     */
    private void handleAccountLinkingGuide() {
        if (isElementPresent(ACCOUNT_LINKING_GUIDE)) {
            clickElementIfExists(OK_BUTTON, "OK (Account Linking Guide)");
            ActionsUtil.sleep(1000);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        }
    }

    /**
     * Clicks 'Continue' or taps fallback coordinate.
     */
    private void checkContinueOrTapFallback() {
        if (clickElementIfExists(CONTINUE_BUTTON, "Continue")) {
            return;
        }
        logpoint("Fallback: Tapping coordinates for Continue");
        ActionsUtil.Tap.withCoordinates(driver, 890, 1290);
    }

    /**
     * Performs login if username field is visible.
     */
    private void performLoginIfRequired() {
        if (isElementPresent(USERNAME_FIELD)) {
            fillInputField(USERNAME_FIELD, TEST_EMAIL, "Email");
            fillInputField(PASSWORD_FIELD, TEST_PASSWORD, "Password");

            if (clickElementIfExists(SUBMIT_BUTTON, "Submit (Login)")) {
                ActionsUtil.sleep(8000);
            }
        } else {
            selectSavedAccount();
        }
    }

    /**
     * Selects a saved account from "Sign In as ..." buttons.
     */
    private void selectSavedAccount() {
        List<WebElement> buttons = driver.findElements(SIGN_IN_AS_BUTTONS).stream()
                .filter(el -> {
                    String text = el.getDomAttribute("text");
                    return text != null && text.startsWith("Sign In as");
                })
                .collect(Collectors.toList());

        if (!buttons.isEmpty()) {
            buttons.get(0).click();
            logpoint("Selected saved Google account");
            ActionsUtil.sleep(8000);
        }
    }

    /**
     * Navigates up and handles optional "Create home" prompt.
     */
    private void navigateUpAndHandleCreateHome() {
        if (clickElementIfExists(NAVIGATE_UP, "Navigate Up")) {
            if (isElementPresent(CREATE_HOME_BUTTON)) {
                backToHomeScreen();
            }
        }
    }

    /**
     * Unlinks account via Google Home control screen.
     */
    private void handleGoogleHomeControl() {
        ActionsUtil.sleep(2500);

        List<WebElement> services = findLabeledTextElements().stream()
                .filter(el -> "Atomberg Home".equals(el.getDomAttribute("text")))
                .collect(Collectors.toList());

        if (!services.isEmpty()) {
            services.get(0).click();
            clickElementIfExists(UNLINK_ACCOUNT, "Unlink Account");
            clickElementIfExists(UNLINK_CONFIRM_BUTTON, "UNLINK (Confirm)");
            ActionsUtil.sleep(10000);
            driver.navigate().back();
            driver.navigate().back();
        } else {
            System.err.println("Atomberg Home service not found in Google Home.");
        }
    }

    /**
     * Verifies Google was successfully unlinked.
     */
    private void verifyGoogleUnlinked() {
        screenCheck.homeScreen();
        ActionsUtil.sleep(2500);
        ActionsUtil.refresh(driver);
        screenCheck.moreTab();

        if (clickElementIfExists(GOOGLE_CONNECT_BUTTON, "Google Connect (after unlink)")) {
            logpoint("Google Home Unlinked Successfully");
        } else {
            // Retry once
            screenCheck.homeScreen();
            ActionsUtil.sleep(2500);
            ActionsUtil.refresh(driver);
            screenCheck.moreTab();

            if (isElementPresent(GOOGLE_CONNECT_BUTTON)) {
                logpoint("Google Home Unlinked Successfully (retry)");
            } else {
                System.err.println("Google unlink verification failed.");
            }
        }
    }

    /**
     * Navigates back to device selection screen safely.
     */
    private void backToHomeScreen() {
        int backCount = 0;
        final int MAX_BACK_PRESS = 10;

        while (!isOnDeviceSelectionScreen() && backCount < MAX_BACK_PRESS) {
            logpoint("Navigating back... (" + (backCount + 1) + "/" + MAX_BACK_PRESS + ")");
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
        return isElementPresent(SELECT_AND_LINK_DEVICE_HEADER);
    }

    // === Utility Methods ===

    /**
     * Safely checks if element is present.
     */
    private boolean isElementPresent(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Safely clicks element if present and displayed.
     */
    private boolean clickElementIfExists(By locator, String label) {
        try {
            WebElement el = driver.findElement(locator);
            if (el.isDisplayed()) {
                el.click();
                logpoint("Clicked: " + label);
                return true;
            } else {
                logpoint(label + " found but not displayed.");
                return false;
            }
        } catch (NoSuchElementException e) {
            logpoint(label + " not found.");
            return false;
        } catch (Exception e) {
            System.err.println("Error clicking " + label + ": " + e.getMessage());
            return false;
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
        logpoint(label + " entered: " + maskSensitiveData(value));
        return true;
    }

    /**
     * Gets all TextView elements with non-null text.
     */
    private List<WebElement> findLabeledTextElements() {
        return driver.findElements(AppiumBy.className("android.widget.TextView")).stream()
                .filter(el -> getAttribute(el, "text") != null)
                .collect(Collectors.toList());
    }

    /**
     * Safely gets DOM attribute.
     */
    private String getAttribute(WebElement el, String attr) {
        try {
            return el.getDomAttribute(attr);
        } catch (Exception e) {
            return null;
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
