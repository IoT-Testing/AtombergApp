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
 * Manage - Handles 'More' tab operations: logout, family, settings.
 */
public class Manage {
    private final AndroidDriver atomberg;
    private final ScreenCheck screenCheck;

    // === Locator Constants ===

    public Manage(AndroidDriver driver) {
        this.atomberg = driver;
        this.screenCheck = new ScreenCheck(driver);
    }

    // === Public Methods ===

    public void theme() {
        if (clickElementIfExists(THEME_BUTTON, "Theme")) {
            System.out.println("Theme");
        }
    }

    public void electricityUnitPrice() {
        if (!scrollToAndClick(ELECTRICITY_UNIT_PRICE, "Electricity Unit Price")) return;
        AppUtil.captureScreenshot(atomberg, "Electricity Unit Price");

        if (clickElementIfExists(UNIT_PRICE_INPUT, "Unit Price Input")) {
            AppUtil.captureScreenshot(atomberg, "Unit Price Input");
        }
    }

    public void changeCurrency() {
        if (clickElementIfExists(CHANGE_CURRENCY, "Change Currency")) {
            AppUtil.captureScreenshot(atomberg, "Change Currency");
            atomberg.navigate().back();
        }
    }

    public void help() {
        screenCheck.moreTab();
        if (scrollToAndClick(HELP_BUTTON, "Help")) {
            System.out.println("Navigated to Help section.");
        }
    }

    public void changePassword() {
        ensureOnMoreTab();
        if (scrollToAndClick(CHANGE_PASSWORD, "Change Password")) {
            AppUtil.captureScreenshot(atomberg,"Change Password");
            atomberg.navigate().back();
        }
    }

    public void deleteAccount() {
        ensureOnMoreTab();
        if (scrollToAndClick(DELETE_ACCOUNT, "Delete Account")) {
            AppUtil.captureScreenshot(atomberg, "Delete Account");
            atomberg.navigate().back();
        }
    }

    public void developerOptions() {
        ensureOnMoreTab();
        if (scrollToAndClick(DEVELOPER_OPTIONS, "Developer Options")) {
            AppUtil.captureScreenshot(atomberg, "Developer Options");
            ActionsUtil.sleep(1000);
            atomberg.navigate().back();
        }
    }

    public void logout() {
        ensureOnMoreTab();
        if (!scrollToAndClick(LOGOUT_BUTTON, "Logout")) return;
        AppUtil.captureScreenshot(atomberg, "Logout");
        if (clickElementIfExists(YES_BUTTON, "Yes (Confirm Logout)")) {
            System.out.println("Logged out successfully.");
        }
    }

    public void family() {
        screenCheck.moreTab();
        if (!scrollToAndClick(MANAGE_FAMILY, "Manage Family")) return;
        AppUtil.captureScreenshot(atomberg, "Manage Family");
        System.out.println("Tap on Manage Family");

        List<WebElement> families = getVisibleFamilyNames();
        int initialCount = families.size();

        for (int i = 0; i < families.size(); i++) {
            WebElement family = families.get(i);
            String name = getElementText(family);

            if ("Add".equals(name)) {
                addHome();
                break; // Exit after adding
            }

            if (name != null && name.endsWith("Script")) {
                handleExistingScriptFamily();
                i--; // Adjust index due to deletion
            } else if (name != null) {
                // Optional: leave or ignore other homes
                System.out.println("Skipping family: " + name);
            }
        }

        atomberg.navigate().back(); // Back to More tab
    }

    // === Internal Helpers ===

    /**
     * Ensures we're on the More tab before proceeding.
     */
    private void ensureOnMoreTab() {
        screenCheck.moreTab();
//        scrollToTop(); // Reset scroll position
    }

    /**
     * Scrolls up until element is found and clicks it.
     *
     * @param locator Locator
     * @param label   Label for logs
     * @return true if clicked
     */
    private boolean scrollToAndClick(By locator, String label) {
        if (clickElementIfExists(locator, label)) return true;

        int attempts = 0;
        while (attempts < 10) {
            ActionsUtil.Scroll.Up(atomberg);
            ActionsUtil.sleep(500);
            if (clickElementIfExists(locator, label)) return true;
            attempts++;
        }

        System.err.println("Failed to find and click: " + label);
        return false;
    }

    /**
     * Safely clicks element if present and displayed.
     *
     * @param locator Locator
     * @param label   Label for logs
     * @return true if clicked
     */
    private boolean clickElementIfExists(By locator, String label) {
        try {
            WebElement el = atomberg.findElement(locator);
            if (el.isDisplayed()) {
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
     * Gets text from element's content-desc.
     */
    private String getElementText(WebElement el) {
        try {
            return el.getDomAttribute("content-desc");
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Gets list of visible family names.
     */
    private List<WebElement> getVisibleFamilyNames() {
        return atomberg.findElements(By.className("android.widget.ImageView")).stream()
                .filter(el -> getElementText(el) != null)
                .collect(Collectors.toList());
    }

    /**
     * Handles an existing "Script" family: edit → leave/delete.
     */
    private void handleExistingScriptFamily() {
        if (!clickElementIfExists(FAMILY_EDIT_ICON, "Family Edit")) return;
        System.out.println("Family Edit");
        AppUtil.captureScreenshot(atomberg, "Family Edit Screen");

        if (clickElementIfExists(LEAVE_HOME, "Leave Home")) {
            AppUtil.captureScreenshot(atomberg, "Leave Home");
            clickElementIfExists(CANCEL_BUTTON, "Cancel");
            AppUtil.captureScreenshot(atomberg, "Cancel button");
        }

        WebElement deleteBtn = findOptionalElement(DELETE_HOME);
        if (deleteBtn != null) {
            deleteBtn.click();
            clickElementIfExists(YES_BUTTON, "Yes (Confirm Delete)");
            ActionsUtil.sleep(3000);
        }
    }

    /**
     * Adds a new home named "Script".
     */
    private void addHome() {
        if (clickElementIfExists(CREATE_HOME_BUTTON, "Create New Smart Home")) {
            AppUtil.captureScreenshot(atomberg, "Create New Smart Home");
        }

        if (clickElementIfExists(HOME_NAME_INPUT, "Home Name Input")) {
            WebElement input = atomberg.findElement(HOME_NAME_INPUT);
            input.clear();
            input.sendKeys("Script");
            AppUtil.captureScreenshot(atomberg, "Home Name Input");
        }

        if (clickElementIfExists(CREATE_BUTTON, "Create")) {
            AppUtil.captureScreenshot(atomberg, "Create Family button");
            ActionsUtil.sleep(5000);
        }
    }

    /**
     * Navigates back to a known element safely.
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

    /**
     * Scrolls toward top of screen.
     */
    private void scrollToTop() {
        for (int i = 0; i < 3; i++) {
            ActionsUtil.Scroll.Down(atomberg);
            ActionsUtil.sleep(300);
        }
    }

    // === Utility Methods ===

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

    /**
     * Finds element without throwing exception.
     */
    private WebElement findOptionalElement(By locator) {
        try {
            return atomberg.findElement(locator);
        } catch (NoSuchElementException e) {
            return null;
        }
    }
}