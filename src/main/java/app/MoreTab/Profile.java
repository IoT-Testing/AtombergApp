package app.MoreTab;

import app.ScreenCheck.ScreenCheck;
import app.util.ActionsUtil;
import app.util.AppUtil;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.*;
import java.util.List;
import java.util.stream.Collectors;

import static app.resources.Locators.MoreTabLocators.*;

/**
 * Profile - Handles user profile editing: avatar, name, phone number.
 */
public class Profile {
    private final AndroidDriver atomberg;
    private final ScreenCheck screenCheck;

    // === Locator Constants ===

    public Profile(AndroidDriver driver) {
        this.atomberg = driver;
        this.screenCheck = new ScreenCheck(driver);
    }

    /**
     * Edits user profile: avatar, name, and triggers phone edit.
     */
    public void edit() {
        openProfile();
        changeAvatar();
        editNameAndTriggerPhoneEdit();
        saveChanges();
        navigateBackToHome();
    }

    // === Internal Helpers ===

    /**
     * Navigates to More tab and opens profile via "Hi," greeting.
     */
    private void openProfile() {
        screenCheck.moreTab();

        List<WebElement> greetings = atomberg.findElements(PROFILE_GREETING).stream()
                .filter(el -> isDisplayed(el) && getAttribute(el, "content-desc").startsWith("Hi,"))
                .collect(Collectors.toList());

        if (greetings.isEmpty()) {
            System.err.println("No profile greeting found. Cannot open profile.");
            return;
        }

        WebElement profileLink = greetings.get(0);
        profileLink.click();
        System.out.println("Edit Profile");
        AppUtil.captureScreenshot(atomberg,"Edit Profile");
        ActionsUtil.sleep(3000);
    }

    /**
     * Opens avatar selection and cycles through options.
     */
    private void changeAvatar() {
        if (!clickElementIfExists(CHANGE_AVATAR_BUTTON, "Change Avatar")) return;
        AppUtil.captureScreenshot(atomberg, "Change Avatar");
        System.out.println("Tap On Change Avatar");

        List<WebElement> avatars = atomberg.findElements(AVATAR_OPTIONS).stream()
                .filter(el -> isClickable(el) && !getAttribute(el, "content-desc").isEmpty())
                .collect(Collectors.toList());

        for (WebElement avatar : avatars) {
            try {
                avatar.click();
                AppUtil.captureScreenshot(atomberg, "Avatar");
                ActionsUtil.sleep(1000);
            } catch (Exception e) {
                System.err.println("Failed to select avatar: " + e.getMessage());
            }
        }

        atomberg.navigate().back();
    }

    /**
     * Edits username and clicks phone section to trigger edit flow.
     */
    private void editNameAndTriggerPhoneEdit() {
        // Edit Name
        List<WebElement> textFields = atomberg.findElements(NAME_INPUT_FIELD);
        if (textFields.size() >= 1) {
            WebElement nameField = textFields.get(0);
            nameField.click();
            nameField.clear();
            nameField.sendKeys("IoT");
            System.out.println("Name updated to 'IoT'");
            AppUtil.captureScreenshot(atomberg,"User Name Changed");
        } else {
            System.err.println("Name input field not found.");
        }

        // Trigger phone edit
        if (clickElementIfExists(PHONE_EDIT_SECTION, "Edit Phone Number")) {
            AppUtil.captureScreenshot(atomberg, "Phone Number");
        }
    }

    /**
     * Saves changes by clicking Update button.
     */
    private void saveChanges() {
        if (clickElementIfExists(UPDATE_BUTTON, "Update")) {
            System.out.println("Profile updated successfully.");
            ActionsUtil.SSleep(2);
        }
    }

    /**
     * Navigates back until home-like screen is reached.
     */
    private void navigateBackToHome() {
        int backCount = 0;
        final int MAX_BACK_PRESS = 10;

        while (!isElementPresent(SELECT_AND_LINK_DEVICE_HEADER) && backCount < MAX_BACK_PRESS) {
            System.out.println("Navigating back... (" + (backCount + 1) + "/" + MAX_BACK_PRESS + ")");
            atomberg.navigate().back();
            ActionsUtil.sleep(1000);
            backCount++;
        }

        if (!isElementPresent(SELECT_AND_LINK_DEVICE_HEADER)) {
            System.err.println("Failed to return to expected screen after " + MAX_BACK_PRESS + " back presses.");
        } else {
            System.out.println("Successfully returned to device selection screen.");
        }
    }

    // === Utility Methods ===

    /**
     * Safely checks if element is displayed.
     */
    private boolean isDisplayed(WebElement el) {
        try {
            return el.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets attribute safely.
     */
    private String getAttribute(WebElement el, String attr) {
        try {
            return el.getDomAttribute(attr);
        } catch (Exception e) {
            return "";
        }
    }


    private boolean isClickable(WebElement el) {
        try {
            return "true".equals(el.getDomAttribute("clickable"));
        } catch (Exception e) {
            return false;
        }
    }

    private boolean clickElementIfExists(By locator, String label) {
        try {
            WebElement el = atomberg.findElement(locator);
            if (isDisplayed(el)) {
                el.click();
                System.out.println("Tap on " + label);
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
     * Safely checks if element is present.
     */
    private boolean isElementPresent(By locator) {
        try {
            return atomberg.findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}