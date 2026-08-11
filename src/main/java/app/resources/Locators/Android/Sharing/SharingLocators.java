package app.resources.Locators.Android.Sharing;

import org.openqa.selenium.By;

/**
 * SharingLocators – UI element locators for the Device Sharing &
 * Permission Management feature screens.
 * ── Locator strategy ──────────────────────────────────────────────────────────
 * The Atomberg app is Flutter-based. All interactive elements expose an
 * accessibility content-desc. Locators follow the same pattern as the existing
 * codebase (FanLocators, Login, MoreTab) — xpath on content-desc.
 * ── Screen map ────────────────────────────────────────────────────────────────
 *   More Tab
 *     └─ Manage Family
 *          └─ [family row]
 *               └─ [member row]  ← MEMBER_PERMISSION_ROW
 *                    └─ Device list for user  ← MEMBER_DEVICE_LIST_*
 *                         └─ Permission editor ← PERMISSION_LEVEL_*
 *          └─ (+) FAB  ← FAB_ADD_MEMBER
 *               └─ Share Entire Home / Share Specific Devices  ← SHARE_*
 *                    └─ QR code screen  ← QR_*
 *   Device detail screen
 *     └─ Share icon / kebab  ← DEVICE_SHARE_ICON
 *          └─ User list  ← SHARE_USER_*
 *               └─ Permission picker  ← PERMISSION_LEVEL_*
 */
public final class SharingLocators {

    private SharingLocators() {}

    // ══════════════════════════════════════════════════════════════════════════
    // Manage Family screen (More → Manage Family)
    // ══════════════════════════════════════════════════════════════════════════

    /** The Manage Family nav option on the More tab. */
    public static final By MANAGE_FAMILY_OPTION =
            By.xpath("//android.view.View[@content-desc=\"Manage family\"]");

    /**
     * FAB (+) button on the Manage Family screen —
     * tapping this opens the "Share Home or Device" bottom sheet.
     */
    public static final By FAB_ADD_MEMBER =
            By.xpath("//android.widget.Button[@content-desc=\"Add member\"]");

    /** The family/home name row the admin taps to expand member list. */
    public static final By FAMILY_HOME_ROW =
            By.xpath("//android.view.View[contains(@content-desc, \"Home\")]");

    // ── Bottom sheet: Share Home or Specific Devices ──────────────────────────

    /** Bottom sheet option: "Share Entire Home" — shares all devices at Basic. */
    public static final By SHARE_ENTIRE_HOME_OPTION =
            By.xpath("//android.view.View[@content-desc=\"Share Entire Home\"]");

    /** Bottom sheet option: "Share Specific Devices" — admin picks devices + levels. */
    public static final By SHARE_SPECIFIC_DEVICES_OPTION =
            By.xpath("//android.view.View[@content-desc=\"Share Specific Devices\"]");

    // ── QR code screen ────────────────────────────────────────────────────────

    /** QR code image element — present when share QR has been generated. */
    public static final By QR_CODE_IMAGE =
            By.xpath("//android.widget.ImageView[@content-desc=\"QR Code\"]");

    /** "Share QR" button or icon on the QR screen. */
    public static final By QR_SHARE_BUTTON =
            By.xpath("//android.widget.Button[@content-desc=\"Share\"]");

    /** "Scan QR" button — visible on the member's device join flow. */
    public static final By QR_SCAN_BUTTON =
            By.xpath("//android.widget.Button[@content-desc=\"Scan QR\"]");

    // ══════════════════════════════════════════════════════════════════════════
    // Member row and per-member device list
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Parameterised locator: given a member display name, returns the row element.
     * Usage:  driver.findElement(SharingLocators.memberRow("Rohit"))
     */
    public static By memberRow(String memberName) {
        return By.xpath("//android.view.View[contains(@content-desc, \"" + memberName + "\")]");
    }

    /** "Devices" tab or section on the per-member edit screen. */
    public static final By MEMBER_DEVICE_TAB =
            By.xpath("//android.view.View[@content-desc=\"Devices\"]");

    /**
     * Parameterised: device row for a specific device name inside the
     * member's device list screen (GET /family_user_devicelist).
     */
    public static By memberDeviceRow(String deviceName) {
        return By.xpath("//android.view.View[contains(@content-desc, \"" + deviceName + "\")]");
    }

    /**
     * "Admin" label shown on an admin member's row —
     * admin rows must appear greyed-out and non-selectable (spec §6, edge case 1).
     */
    public static final By ADMIN_MEMBER_LABEL =
            By.xpath("//android.view.View[@content-desc=\"Admin\"]");

    /**
     * Greyed-out/disabled state on an admin row.
     * Admin rows must not be tappable (spec §6.1). Check enabled=false attribute.
     */
    public static By adminRowDisabled(String adminName) {
        return By.xpath("//android.view.View[contains(@content-desc, \"" + adminName
                + "\") and @enabled=\"false\"]");
    }

    // ══════════════════════════════════════════════════════════════════════════
    // Permission level picker (Super / Basic / Custom)
    // ══════════════════════════════════════════════════════════════════════════

    /** "Super" permission level option. */
    public static final By PERMISSION_LEVEL_SUPER =
            By.xpath("//android.view.View[@content-desc=\"Super\"]");

    /** "Basic" permission level option (default, applied on QR share). */
    public static final By PERMISSION_LEVEL_BASIC =
            By.xpath("//android.view.View[@content-desc=\"Basic\"]");

    /** "Custom" permission level option — reveals individual capability toggles. */
    public static final By PERMISSION_LEVEL_CUSTOM =
            By.xpath("//android.view.View[@content-desc=\"Custom\"]");

    /** Confirm / Apply button on the permission level picker. */
    public static final By PERMISSION_CONFIRM_BUTTON =
            By.xpath("//android.widget.Button[@content-desc=\"Confirm\"]");

    /** Save button on the permission editor. */
    public static final By PERMISSION_SAVE_BUTTON =
            By.xpath("//android.widget.Button[@content-desc=\"Save\"]");

    // ══════════════════════════════════════════════════════════════════════════
    // Custom capability toggles — fan (spec §3.1)
    // ══════════════════════════════════════════════════════════════════════════

    public static final By TOGGLE_FAN_CONTROL =
            By.xpath("//android.widget.Switch[@content-desc=\"Control\"]");

    public static final By TOGGLE_FAN_EDIT_DEVICE =
            By.xpath("//android.widget.Switch[@content-desc=\"Edit Device\"]");

    public static final By TOGGLE_FAN_VIEW_ANALYTICS =
            By.xpath("//android.widget.Switch[@content-desc=\"View Analytics\"]");

    public static final By TOGGLE_FAN_EDIT_WIFI =
            By.xpath("//android.widget.Switch[@content-desc=\"Edit Wifi Details\"]");

    public static final By TOGGLE_FAN_AUTOMATIONS =
            By.xpath("//android.widget.Switch[@content-desc=\"Automations\"]");

    // ══════════════════════════════════════════════════════════════════════════
    // Custom capability toggles — door lock (spec §3.2)
    // ══════════════════════════════════════════════════════════════════════════

    public static final By TOGGLE_LOCK_VIEW_KEYS_PINS =
            By.xpath("//android.widget.Switch[@content-desc=\"View Keys & Pins\"]");

    public static final By TOGGLE_LOCK_VIEW_SETTINGS =
            By.xpath("//android.widget.Switch[@content-desc=\"View Settings\"]");

    public static final By TOGGLE_LOCK_VIEW_HISTORY =
            By.xpath("//android.widget.Switch[@content-desc=\"View History\"]");

    public static final By TOGGLE_LOCK_UNLOCK =
            By.xpath("//android.widget.Switch[@content-desc=\"Unlock\"]");

    public static final By TOGGLE_LOCK_ADD_UNLOCK_METHODS =
            By.xpath("//android.widget.Switch[@content-desc=\"Add Unlock Methods\"]");

    public static final By TOGGLE_LOCK_EDIT_DEVICE =
            By.xpath("//android.widget.Switch[@content-desc=\"Edit Device\"]");

    // ══════════════════════════════════════════════════════════════════════════
    // Device detail screen — share entry point
    // ══════════════════════════════════════════════════════════════════════════

    /** Share icon on the device detail screen — opens user list for sharing. */
    public static final By DEVICE_SHARE_ICON =
            By.xpath("//android.widget.ImageView[@content-desc=\"Share\"]");

    /** Kebab/overflow menu on the device detail screen. */
    public static final By DEVICE_KEBAB_MENU =
            By.xpath("//android.widget.ImageView[@content-desc=\"More options\"]");

    /** "Share Device" option inside the kebab menu. */
    public static final By DEVICE_SHARE_OPTION =
            By.xpath("//android.view.View[@content-desc=\"Share Device\"]");

    /** "Remove Access" option inside the kebab menu. */
    public static final By DEVICE_REMOVE_ACCESS_OPTION =
            By.xpath("//android.view.View[@content-desc=\"Remove Access\"]");

    // ══════════════════════════════════════════════════════════════════════════
    // User selection list (shown during device share flow)
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Parameterised: user checkbox in the share-with-user list.
     * Admin rows in this list should be greyed out (spec §4, Flow 1, step 2).
     */
    public static By shareUserCheckbox(String userName) {
        return By.xpath("//android.widget.CheckBox[contains(@content-desc, \"" + userName + "\")]");
    }

    /** "Next" or "Share" button after selecting users during device share. */
    public static final By SHARE_NEXT_BUTTON =
            By.xpath("//android.widget.Button[@content-desc=\"Next\"]");

    public static final By SHARE_CONFIRM_BUTTON =
            By.xpath("//android.widget.Button[@content-desc=\"Share\"]");

    /** "Skip" button — allows skipping user selection (spec §4, Flow 1, step 5b). */
    public static final By SHARE_SKIP_BUTTON =
            By.xpath("//android.widget.Button[@content-desc=\"Skip\"]");

    // ══════════════════════════════════════════════════════════════════════════
    // Permission-denied state (spec §6, edge case 6)
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Permission-denied indicator — shown when a member tries to access a
     * feature not covered by their permission mask.
     */
    public static final By PERMISSION_DENIED_INDICATOR =
            By.xpath("//android.view.View[@content-desc=\"Permission Denied\"]");

    /**
     * "You don't have permission" or similar snackbar/toast.
     * The app shows this when a control call is blocked client-side.
     */
    public static final By PERMISSION_DENIED_SNACKBAR =
            By.xpath("//android.view.View[contains(@content-desc, \"permission\")]");

    // ══════════════════════════════════════════════════════════════════════════
    // Confirmation dialogs
    // ══════════════════════════════════════════════════════════════════════════

    public static final By DIALOG_CONFIRM_YES =
            By.xpath("//android.widget.Button[@content-desc=\"Yes\"]");

    public static final By DIALOG_CONFIRM_OK =
            By.xpath("//android.widget.Button[@content-desc=\"OK\"]");

    public static final By DIALOG_CANCEL =
            By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]");

    /** Snackbar that appears after a successful permission save. */
    public static final By SUCCESS_SNACKBAR =
            By.xpath("//android.view.View[contains(@content-desc, \"saved\") "
                    + "or contains(@content-desc, \"updated\") "
                    + "or contains(@content-desc, \"Success\")]");

    /** "Remove" button on the remove-access confirmation dialog. */
    public static final By REMOVE_ACCESS_CONFIRM_BUTTON =
            By.xpath("//android.widget.Button[@content-desc=\"Remove\"]");

    // ══════════════════════════════════════════════════════════════════════════
    // Device list on the home screen (to navigate into a device)
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Parameterised: taps the named device card on the home screen.
     * Used by member phone to verify device presence/absence.
     */
    public static By deviceCardOnHome(String deviceName) {
        return By.xpath("//android.view.View[contains(@content-desc, \"" + deviceName + "\")]");
    }

    /**
     * Fan speed control button — member uses this to verify Control permission.
     * Greyed-out or absent when bit 0 is not set.
     */
    public static final By FAN_SPEED_CONTROL =
            By.xpath("//android.widget.Button[contains(@content-desc, \"Speed\")]");

    /**
     * Fan "Edit" / settings button on device detail — requires Edit Device permission (bit 1).
     */
    public static final By FAN_EDIT_BUTTON =
            By.xpath("//android.widget.ImageView[@content-desc=\"Edit\"]");

    /**
     * Analytics tab inside the device detail — requires View Analytics permission (bit 2).
     */
    public static final By FAN_ANALYTICS_TAB =
            By.xpath("//android.view.View[@content-desc=\"Analytics\"]");
}
