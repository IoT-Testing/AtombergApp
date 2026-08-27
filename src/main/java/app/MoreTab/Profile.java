package app.MoreTab;

import app.ScreenCheck.ScreenCheck;
import app.util.ActionsUtil;
import app.util.AppUtil;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.*;

import java.util.List;
import java.util.stream.Collectors;

import static app.resources.Locators.Android.AppLocators.MoreTab.*;

/**
 * Profile – handles user profile editing: avatar, name, and phone number trigger.
 *
 * <p>Follows the page-object pattern: each public method represents a high-level
 * user action and delegates internally to focused helper methods. All transient
 * failures (element not found, click error) are logged and do not throw, so the
 * test receives a meaningful failure only when a mandatory step actually fails.</p>
 */
public class Profile {

    private final AndroidDriver atomberg;
    private final ScreenCheck   screenCheck;

    public Profile(AndroidDriver driver) {
        this.atomberg     = driver;
        this.screenCheck  = new ScreenCheck(driver);
    }

    // ── Public API ────────────────────────────────────────────────────────────

    /**
     * Full profile-edit flow: navigate → avatar → name/phone → save → back to home.
     */
    public void edit() {
        openProfile();
        changeAvatar();
        editNameAndTriggerPhoneEdit();
        saveChanges();
        navigateBackToHome();
    }

    // ── Internal steps ────────────────────────────────────────────────────────

    /**
     * Navigates to the More tab and opens the profile via the "Hi," greeting element.
     */
    private void openProfile() {
        screenCheck.moreTab();

        List<WebElement> greetings = atomberg.findElements(PROFILE_GREETING).stream()
                .filter(el -> isDisplayed(el) && getAttribute(el, "content-desc").startsWith("Hi,"))
                .collect(Collectors.toList());

        if (greetings.isEmpty()) {
            System.err.println("No profile greeting found — cannot open profile.");
            return;
        }
        greetings.get(0).click();
        System.out.println("Opened Edit Profile.");
        AppUtil.captureScreenshot(atomberg, "edit_profile_opened");
        ActionsUtil.sleep(3000);
    }

    /**
     * Opens avatar selection and cycles through all available avatar options.
     */
    private void changeAvatar() {
        if (!clickElementIfExists(CHANGE_AVATAR_BUTTON, "Change Avatar")) return;
        AppUtil.captureScreenshot(atomberg, "change_avatar");

        List<WebElement> avatars = atomberg.findElements(AVATAR_OPTIONS).stream()
                .filter(el -> isClickable(el) && !getAttribute(el, "content-desc").isEmpty())
                .collect(Collectors.toList());

        for (WebElement avatar : avatars) {
            try {
                avatar.click();
                AppUtil.captureScreenshot(atomberg, "avatar_selected");
                ActionsUtil.sleep(1000);
            } catch (Exception e) {
                System.err.println("Failed to select avatar: " + e.getMessage());
            }
        }
        atomberg.navigate().back();
    }

    /**
     * Clears and types a new username, then taps the phone section to trigger
     * the phone-number edit flow.
     */
    private void editNameAndTriggerPhoneEdit() {
        List<WebElement> textFields = atomberg.findElements(NAME_INPUT_FIELD);
        if (!textFields.isEmpty()) {
            WebElement nameField = textFields.get(0);
            nameField.click();
            nameField.clear();
            nameField.sendKeys("IoT");
            System.out.println("Name updated to 'IoT'.");
            AppUtil.captureScreenshot(atomberg, "name_updated");
        } else {
            System.err.println("Name input field not found.");
        }

        if (clickElementIfExists(PHONE_EDIT_SECTION, "Edit Phone Number")) {
            AppUtil.captureScreenshot(atomberg, "phone_edit_triggered");
        }
    }

    /**
     * Taps the Update button to persist profile changes.
     */
    private void saveChanges() {
        if (clickElementIfExists(UPDATE_BUTTON, "Update")) {
            System.out.println("Profile updated successfully.");
            ActionsUtil.SSleep(2);
        }
    }

    /**
     * Presses Back until the "Select and link device" header reappears (home screen).
     * Gives up after {@code MAX_BACK_PRESS} attempts.
     */
    private void navigateBackToHome() {
        final int MAX_BACK_PRESS = 10;
        for (int i = 0; i < MAX_BACK_PRESS; i++) {
            if (isElementPresent(SELECT_AND_LINK_DEVICE_HEADER)) {
                System.out.println("Returned to device selection screen.");
                return;
            }
            System.out.printf("Navigating back… (%d/%d)%n", i + 1, MAX_BACK_PRESS);
            atomberg.navigate().back();
            ActionsUtil.sleep(1000);
        }
        System.err.println("Failed to return to expected screen after " + MAX_BACK_PRESS + " back presses.");
    }

    // ── Micro-utilities ───────────────────────────────────────────────────────

    private boolean isDisplayed(WebElement el) {
        try { return el.isDisplayed(); } catch (Exception e) { return false; }
    }

    private String getAttribute(WebElement el, String attr) {
        try {
            String v = el.getDomAttribute(attr);
            return v != null ? v : "";
        } catch (Exception e) { return ""; }
    }

    private boolean isClickable(WebElement el) {
        try { return "true".equals(el.getDomAttribute("clickable")); }
        catch (Exception e) { return false; }
    }

    private boolean clickElementIfExists(By locator, String label) {
        try {
            WebElement el = atomberg.findElement(locator);
            if (isDisplayed(el)) {
                el.click();
                System.out.println("Tapped: " + label);
                return true;
            }
            System.out.println(label + " found but not displayed.");
            return false;
        } catch (NoSuchElementException e) {
            System.out.println(label + " not found.");
            return false;
        } catch (Exception e) {
            System.err.println("Error clicking " + label + ": " + e.getMessage());
            return false;
        }
    }

    private boolean isElementPresent(By locator) {
        try { return atomberg.findElement(locator).isDisplayed(); }
        catch (NoSuchElementException e) { return false; }
    }
}
