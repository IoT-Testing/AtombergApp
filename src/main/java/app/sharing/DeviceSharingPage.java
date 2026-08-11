package app.sharing;

import app.util.ActionsUtil;
import app.util.AppUtil;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static app.resources.Locators.Android.AppLocators.MoreTab.MANAGE_FAMILY;
import static app.resources.Locators.Android.HomeLocators.MORE_TAB;
import static app.resources.Locators.Android.Sharing.SharingLocators.*;

/**
 * DeviceSharingPage – Page Object for the Device Sharing &amp; Permission
 * Management feature (spec §4 – User Flows 1, 2, 3).
 * This page object is always operated by the ADMIN driver, because:
 *   – Only Admins/Device Owners can share devices (spec §2.4)
 *   – Only Admins can edit member permissions in Manage Family (spec §4 Flow 2)
 * Naming convention:
 *   flow1_*  → Device Onboarding: Assign Permissions (post-onboarding share)
 *   flow2_*  → Manage Family: Edit Device Permissions Per User
 *   flow3_*  → Share Home or Specific Devices via QR
 */
public class DeviceSharingPage {

    private final AndroidDriver driver;
    private final WebDriverWait wait;
    private static final Duration TIMEOUT = Duration.ofSeconds(15);

    public DeviceSharingPage(AndroidDriver adminDriver) {
        this.driver = adminDriver;
        this.wait   = new WebDriverWait(adminDriver, TIMEOUT);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // NAVIGATION
    // ══════════════════════════════════════════════════════════════════════════

    /** Navigate to More tab → Manage Family */
    public DeviceSharingPage navigateToManageFamily() {
        tap(MORE_TAB, "More Tab");
        ActionsUtil.SSleep(1);
        scrollAndTap(MANAGE_FAMILY, "Manage Family");
        ActionsUtil.SSleep(2);
        AppUtil.captureScreenshot(driver, "manage_family_screen");
        return this;
    }

    /** On Manage Family, tap the family/home row to expand members. */
    public DeviceSharingPage openFamilyHome(String homeNameContains) {
        By homeRow = By.xpath("//android.view.View[contains(@content-desc, \""
                + homeNameContains + "\")]");
        tap(homeRow, "Home row: " + homeNameContains);
        ActionsUtil.SSleep(2);
        return this;
    }

    /** Tap a specific member row to open their permission editor. */
    public DeviceSharingPage openMemberPermissions(String memberName) {
        tap(memberRow(memberName), "Member row: " + memberName);
        ActionsUtil.SSleep(2);
        AppUtil.captureScreenshot(driver, "member_device_list_" + memberName.replace(" ", "_"));
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // FLOW 2 — Manage Family: Edit Device Permissions Per User  (spec §4)
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Flow 2 step 3a: Add a device to a member at the given permission level.
     *
     * @param deviceName      Device to add (must exist in home but not yet shared with user)
     * @param permissionLevel "Super", "Basic", or "Custom"
     */
    public DeviceSharingPage flow2_addDeviceToMember(String deviceName, String permissionLevel) {
        // On the member device list screen, tap "+" or the device row to add
        By addDeviceButton = By.xpath("//android.widget.Button[@content-desc=\"Add Device\"]");
        tap(addDeviceButton, "Add Device button");
        ActionsUtil.SSleep(1);

        // Select device from the home device list
        tap(By.xpath("//android.view.View[contains(@content-desc, \"" + deviceName + "\")]"),
                "Device: " + deviceName);
        ActionsUtil.SSleep(1);

        // Select permission level
        selectPermissionLevel(permissionLevel);
        ActionsUtil.SSleep(1);

        // Confirm
        tap(PERMISSION_CONFIRM_BUTTON, "Confirm button");
        ActionsUtil.SSleep(2);
        AppUtil.captureScreenshot(driver, "flow2_add_device_" + deviceName);
        return this;
    }

    /**
     * Flow 2 step 3b: Edit an existing device's permission level for a member.
     * Calls /edit_user_device (PUT) — upsert semantics (spec §5.1).
     *
     * @param deviceName      Device already shared with the member
     * @param permissionLevel New level: "Super", "Basic", or "Custom"
     */
    public DeviceSharingPage flow2_editDevicePermission(String deviceName, String permissionLevel) {
        tap(memberDeviceRow(deviceName), "Device row: " + deviceName);
        ActionsUtil.SSleep(1);

        selectPermissionLevel(permissionLevel);
        ActionsUtil.SSleep(1);

        tap(PERMISSION_SAVE_BUTTON, "Save button");
        ActionsUtil.SSleep(2);
        AppUtil.captureScreenshot(driver, "flow2_edit_permission_" + deviceName);
        return this;
    }

    /**
     * Flow 2 step 3c: Remove a device from a member.
     * Calls /remove_user_device (spec §5.1).
     *
     * @param deviceName Device to remove access from
     */
    public DeviceSharingPage flow2_removeDeviceFromMember(String deviceName) {
        // Long-press or swipe left on device row to expose Remove option, OR tap row → Remove
        tap(memberDeviceRow(deviceName), "Device row: " + deviceName);
        ActionsUtil.SSleep(1);

        tap(DEVICE_REMOVE_ACCESS_OPTION, "Remove Access option");
        ActionsUtil.SSleep(1);

        tap(REMOVE_ACCESS_CONFIRM_BUTTON, "Remove confirm button");
        ActionsUtil.SSleep(2);
        AppUtil.captureScreenshot(driver, "flow2_remove_device_" + deviceName);
        return this;
    }

    /**
     * Set Custom capabilities for a fan device.
     *
     * @param control        enable/disable fan control
     * @param editDevice     enable/disable edit device
     * @param viewAnalytics  enable/disable view analytics
     * @param editWifi       enable/disable edit wifi
     * @param automations    enable/disable automations
     */
    public DeviceSharingPage setFanCustomPermissions(
            boolean control, boolean editDevice,
            boolean viewAnalytics, boolean editWifi, boolean automations) {

        // First select "Custom"
        selectPermissionLevel("Custom");
        ActionsUtil.SSleep(1);

        setToggle(TOGGLE_FAN_CONTROL,        control,       "Fan Control");
        setToggle(TOGGLE_FAN_EDIT_DEVICE,    editDevice,    "Fan Edit Device");
        setToggle(TOGGLE_FAN_VIEW_ANALYTICS, viewAnalytics, "Fan View Analytics");
        setToggle(TOGGLE_FAN_EDIT_WIFI,      editWifi,      "Fan Edit Wifi");
        setToggle(TOGGLE_FAN_AUTOMATIONS,    automations,   "Fan Automations");

        AppUtil.captureScreenshot(driver, "custom_fan_permissions");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // FLOW 3 — Share via QR  (spec §4, Flow 3)
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Flow 3: Tap the (+) FAB on Manage Family to open the share bottom sheet.
     */
    public DeviceSharingPage flow3_openShareBottomSheet() {
        tap(FAB_ADD_MEMBER, "FAB Add Member");
        ActionsUtil.SSleep(1);
        AppUtil.captureScreenshot(driver, "share_bottom_sheet");
        return this;
    }

    /**
     * Flow 3 step 2a: Share the entire home.
     * All devices shared at Basic permission level by default (spec §4 Flow 3 step 2a).
     */
    public DeviceSharingPage flow3_shareEntireHome() {
        tap(SHARE_ENTIRE_HOME_OPTION, "Share Entire Home");
        ActionsUtil.SSleep(3);
        AppUtil.captureScreenshot(driver, "qr_entire_home_generated");
        return this;
    }

    /**
     * Flow 3 step 2b: Share specific devices at chosen permission level.
     *
     * @param deviceNames     List of device names to share
     * @param permissionLevel "Super", "Basic", or "Custom"
     */
    public DeviceSharingPage flow3_shareSpecificDevices(
            List<String> deviceNames, String permissionLevel) {

        tap(SHARE_SPECIFIC_DEVICES_OPTION, "Share Specific Devices");
        ActionsUtil.SSleep(1);

        // Select each device
        for (String deviceName : deviceNames) {
            By deviceCheckbox = By.xpath(
                    "//android.widget.CheckBox[contains(@content-desc, \"" + deviceName + "\")]");
            tap(deviceCheckbox, "Device checkbox: " + deviceName);
            ActionsUtil.SSleep(500);
        }

        tap(SHARE_NEXT_BUTTON, "Next button");
        ActionsUtil.SSleep(1);

        // Set permission level for selected devices
        selectPermissionLevel(permissionLevel);
        ActionsUtil.SSleep(1);

        tap(SHARE_CONFIRM_BUTTON, "Confirm Share button");
        ActionsUtil.SSleep(3);
        AppUtil.captureScreenshot(driver, "qr_specific_devices_generated");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // DEVICE DETAIL — share from device screen  (Flow 1 post-onboarding path)
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Opens the named device's detail screen from the home screen.
     */
    public DeviceSharingPage openDevice(String deviceName) {
        tap(deviceCardOnHome(deviceName), "Device card: " + deviceName);
        ActionsUtil.SSleep(2);
        AppUtil.captureScreenshot(driver, "device_detail_" + deviceName.replace(" ", "_"));
        return this;
    }

    /**
     * Taps the share icon / kebab → Share Device on the device detail screen.
     */
    public DeviceSharingPage tapShareDevice() {
        // Try direct share icon first; fall back to kebab → Share Device
        if (isPresent(DEVICE_SHARE_ICON)) {
            tap(DEVICE_SHARE_ICON, "Share Icon");
        } else {
            tap(DEVICE_KEBAB_MENU, "Kebab menu");
            ActionsUtil.SSleep(500);
            tap(DEVICE_SHARE_OPTION, "Share Device option");
        }
        ActionsUtil.SSleep(2);
        return this;
    }

    /**
     * Flow 1 step 3–5a: Select users and apply permission level from the user list.
     *
     * @param userNames       Non-admin users to share with (admin rows must be greyed-out)
     * @param permissionLevel "Super", "Basic", or "Custom"
     */
    public DeviceSharingPage shareWithUsers(List<String> userNames, String permissionLevel) {
        for (String user : userNames) {
            tap(shareUserCheckbox(user), "User checkbox: " + user);
            ActionsUtil.SSleep(400);
        }

        tap(SHARE_NEXT_BUTTON, "Next button");
        ActionsUtil.SSleep(1);

        selectPermissionLevel(permissionLevel);
        ActionsUtil.SSleep(1);

        tap(PERMISSION_CONFIRM_BUTTON, "Confirm button");
        ActionsUtil.SSleep(2);
        AppUtil.captureScreenshot(driver, "share_with_users_confirmed");
        return this;
    }

    /**
     * Flow 1 step 5b: Skip the share step.
     */
    public DeviceSharingPage skipSharing() {
        tap(SHARE_SKIP_BUTTON, "Skip sharing button");
        ActionsUtil.SSleep(1);
        AppUtil.captureScreenshot(driver, "share_skipped");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // STATE QUERIES (used by tests for assertions)
    // ══════════════════════════════════════════════════════════════════════════

    /** Returns true if the QR code image is visible on screen. */
    public boolean isQrCodeVisible() {
        return isPresent(QR_CODE_IMAGE);
    }

    /** Returns true if the success snackbar / confirmation banner is visible. */
    public boolean isSuccessMessageVisible() {
        return isPresent(SUCCESS_SNACKBAR);
    }

    /** Returns true if the permission denied indicator is shown. */
    public boolean isPermissionDeniedShown() {
        return isPresent(PERMISSION_DENIED_INDICATOR) || isPresent(PERMISSION_DENIED_SNACKBAR);
    }

    /**
     * Verifies that the admin member row in the user list is disabled/greyed-out.
     * Spec §6 edge case 1: admin rows must be non-selectable.
     *
     * @param adminName  Display name of the admin member
     * @return true if the row is found but not enabled (disabled state)
     */
    public boolean isAdminRowDisabled(String adminName) {
        By locator = adminRowDisabled(adminName);
        List<WebElement> elements = driver.findElements(locator);
        if (!elements.isEmpty()) return true;

        // Fallback: find the row by name and check enabled attribute
        try {
            WebElement row = driver.findElement(memberRow(adminName));
            String enabled = row.getDomAttribute("enabled");
            return "false".equalsIgnoreCase(enabled);
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Returns the currently displayed permission level for a device row in the
     * member's device list.  Returns null if the element is not found.
     */
    public String getCurrentPermissionLevelFor(String deviceName) {
        // The permission level badge is expected adjacent to the device name row
        By badge = By.xpath("//android.view.View[contains(@content-desc, \""
                + deviceName + "\")]/following-sibling::android.view.View[1]");
        try {
            WebElement el = driver.findElement(badge);
            return el.getDomAttribute("content-desc");
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    /**
     * Returns true if the named device appears in the member's visible device list.
     * Used by member phone after receiving a share.
     */
    public boolean isDeviceVisibleOnHome(String deviceName) {
        return isPresent(deviceCardOnHome(deviceName));
    }

    // ══════════════════════════════════════════════════════════════════════════
    // Private helpers
    // ══════════════════════════════════════════════════════════════════════════

    private void selectPermissionLevel(String level) {
        switch (level.toLowerCase()) {
            case "super"  -> tap(PERMISSION_LEVEL_SUPER,  "Super level");
            case "basic"  -> tap(PERMISSION_LEVEL_BASIC,  "Basic level");
            case "custom" -> tap(PERMISSION_LEVEL_CUSTOM, "Custom level");
            default -> throw new IllegalArgumentException(
                    "Unknown permission level: " + level + ". Use Super, Basic, or Custom.");
        }
    }

    private void setToggle(By locator, boolean enabledState, String label) {
        try {
            WebElement toggle = driver.findElement(locator);
            boolean currentlyChecked = Boolean.parseBoolean(toggle.getDomAttribute("checked"));
            if (currentlyChecked != enabledState) {
                toggle.click();
                System.out.println("[DeviceSharingPage] Toggle " + label
                        + " → " + (enabledState ? "ON" : "OFF"));
            } else {
                System.out.println("[DeviceSharingPage] Toggle " + label
                        + " already " + (enabledState ? "ON" : "OFF") + " — skipping");
            }
        } catch (NoSuchElementException e) {
            System.err.println("[DeviceSharingPage] Toggle not found: " + label);
        }
    }

    private void tap(By locator, String label) {
        try {
            WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
            el.click();
            System.out.println("[DeviceSharingPage] Tapped: " + label);
        } catch (Exception e) {
            System.err.println("[DeviceSharingPage] Could not tap: " + label + " — " + e.getMessage());
            AppUtil.captureScreenshot(driver, "tap_fail_" + label.replace(" ", "_").toLowerCase());
            throw new RuntimeException("Tap failed on: " + label, e);
        }
    }

    private void scrollAndTap(By locator, String label) {
        if (isPresent(locator)) { tap(locator, label); return; }
        for (int i = 0; i < 5; i++) {
            ActionsUtil.Scroll.Up(driver);
            ActionsUtil.SSleep(500);
            if (isPresent(locator)) { tap(locator, label); return; }
        }
        tap(locator, label);  // Let it fail with a clear message
    }

    private boolean isPresent(By locator) {
        try { return driver.findElement(locator).isDisplayed(); }
        catch (Exception e) { return false; }
    }

    // Overload: millisecond sleep flag
    private void ActionsUtil_SSleep(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}