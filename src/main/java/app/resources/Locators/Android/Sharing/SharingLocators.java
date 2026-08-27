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
     * The (+) "Add member" button shown <b>inside an opened family</b>
     * (Manage Family → open family → +). The app renders it as a bare Flutter
     * {@code View} with no content-desc, so this uses the verified absolute path
     * (captured on the admin phone, Main Family) unioned with a content-desc
     * fallback in case a future build labels it. NOTE: the family must be opened
     * first — this button is not present on the Manage Family list itself.
     */
    public static final By FAB_ADD_MEMBER =
            By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]"
                    + "/android.widget.FrameLayout/android.widget.FrameLayout"
                    + "/android.view.View/android.view.View/android.view.View"
                    + "/android.view.View/android.view.View[3]"
                    + " | //android.widget.Button[@content-desc=\"Add member\"]");

    /**
     * The "Add" FAB on the Manage Family screen used by the MEMBER phone to join a
     * family via invite code (Flow 2, member side). Confirmed on device as an
     * ImageView with content-desc "Add", and it sits on the Manage Family list
     * itself — unlike {@link #FAB_ADD_MEMBER}, no family needs to be opened first.
     */
    public static final By MEMBER_ADD_FAB =
            By.xpath("//android.widget.ImageView[@content-desc=\"Add\"]");

    /** The family/home name row the admin taps to expand member list. */
    public static final By FAMILY_HOME_ROW =
            By.xpath("//android.view.View[contains(@content-desc, \"Home\")]");

    /**
     * The family tile on the Manage Family LIST screen (the one you tap to open a
     * family). Rendered as an {@code ImageView} whose content-desc is
     * "&lt;memberCount&gt;\n&lt;familyName&gt;" — e.g. {@code "1\nMain Family"},
     * which becomes {@code "2\nMain Family"} once a member joins. We therefore
     * match on the NAME only with contains(), so the locator survives the count
     * changing as members are added and removed.
     *
     * <p>Presence of this tile is also the reliable signal that we are still on the
     * Manage Family list and have NOT yet opened the family.</p>
     */
    public static By familyTile(String familyName) {
        return By.xpath("//android.widget.ImageView[contains(@content-desc, \"" + familyName + "\")]"
                + " | //android.view.View[contains(@content-desc, \"" + familyName + "\")]");
    }

    // ── Bottom sheet: Share Home or Specific Devices ──────────────────────────

    /** Bottom sheet option: "Share Entire Home" — shares all devices at Basic. */
    public static final By SHARE_ENTIRE_HOME_OPTION =
            By.xpath("//android.view.View[@content-desc=\"Share Entire Home\"]");

    /** Bottom sheet option: "Share Specific Devices" — admin picks devices + levels. */
    public static final By SHARE_SPECIFIC_DEVICES_OPTION =
            By.xpath("//android.view.View[@content-desc=\"Share Specific Devices\"]");

    // ── QR code screen ────────────────────────────────────────────────────────

    /**
     * Signals that the share QR screen has been generated.
     *
     * <p>Pinned from test-output/page-source/qr_share_screen.xml: the QR bitmap
     * itself exposes NO accessible node, so "QR Code" ImageView never matched and
     * the check reported a false negative on a screen that was up and correct. The
     * caption above the bitmap is the reliable marker; the "valid only for 15
     * minutes" line is OR-ed as a second anchor.</p>
     */
    public static final By QR_CODE_IMAGE =
            By.xpath("//android.view.View[@content-desc=\"Scan the below QR to join this family\"]"
                    + " | //android.view.View[contains(@content-desc, \"valid only for 15 minutes\")]"
                    + " | //android.widget.ImageView[@content-desc=\"QR Code\"]");

    /** "Share QR" button or icon on the QR screen. */
    public static final By QR_SHARE_BUTTON =
            By.xpath("//android.widget.Button[@content-desc=\"Share\"]");

    /** "Scan QR" button — visible on the member's device join flow. */
    public static final By QR_SCAN_BUTTON =
            By.xpath("//android.widget.Button[@content-desc=\"Scan QR\"]");

    // ── Invite code (camera-less join path) ───────────────────────────────────
    // Camera QR scanning cannot be driven by Appium, so the join is done with the
    // text invite code shown alongside the QR. The admin copies it; the member
    // types it (or it is pasted from the member-device clipboard).

    /**
     * The human-readable invite / share code text shown under the QR image on
     * the admin's QR screen. Its content-desc holds the code to copy across.
     * Multiple candidate labels are OR-ed so the locator survives copy changes.
     */
    /**
     * Pinned from {@code test-output/page-source/qr_share_screen.xml} (2026-08-07):
     * the code is a bare {@code android.view.View} whose content-desc IS the code
     * ("NZ75JW") — no "Code:" prefix — sitting as the next sibling of the label
     * "Enter the below code to join this family", and it is the screen's only
     * long-clickable short node (long-press to copy).
     *
     * <p>Anchoring on the label rather than on the code's shape keeps the sentence
     * nodes around it from matching; the long-clickable branch is the fallback for
     * when the copy changes.</p>
     */
    public static final By SHARE_CODE_TEXT =
            By.xpath("//android.view.View[@content-desc="
                    + "\"Enter the below code to join this family\"]"
                    + "/following-sibling::android.view.View[1]"
                    + " | //android.view.View[@long-clickable=\"true\""
                    + " and string-length(@content-desc) >= 6"
                    + " and string-length(@content-desc) <= 10]");

    /**
     * "Copy" / "Copy code" button on the admin QR screen.
     *
     * <p>The 2026-08-07 build has NO copy button — the QR screen offers only
     * "Share", and the code itself is long-clickable. Kept so the clipboard
     * strategy in {@code readInviteCode()} still fires on builds that add one;
     * it simply never matches today.</p>
     */
    public static final By SHARE_CODE_COPY_BUTTON =
            By.xpath("//android.widget.Button[@content-desc=\"Copy\" "
                    + "or @content-desc=\"Copy code\" "
                    + "or @content-desc=\"Copy link\"]");

    /**
     * "Join an existing smart home" — the option the member picks on the sheet that
     * the Manage-Family (+) FAB opens. Without it the flow stops on the sheet and
     * the code field is never reached.
     *
     * <p>Captured from test-output/page-source/member_add_family_sheet.xml — the
     * sheet's two options carry title AND subtitle in one content-desc:
     * <pre>
     *   "Create a new smart home\nA smart home is a collection of smart devices.\n"
     *   "Join an existing smart home\nControl devices in an existing smart home\n"
     * </pre>
     * so an equality match on the title alone never fires. {@code starts-with} keeps
     * the two options apart while surviving subtitle copy changes; nothing matches a
     * bare "Join", which would also hit the submit button on the next screen.</p>
     */
    public static final By JOIN_EXISTING_HOME_OPTION =
            By.xpath("//android.view.View[starts-with(@content-desc, \"Join an existing smart home\")]"
                    + " | //android.view.View[starts-with(@content-desc, \"Join an existing home\")]"
                    + " | //android.widget.Button[starts-with(@content-desc, \"Join an existing\")]");

    /** "Enter code" / "Have a code?" entry point on the member join screen. */
    public static final By JOIN_ENTER_CODE_OPTION =
            By.xpath("//android.view.View[@content-desc=\"Enter code\" "
                    + "or @content-desc=\"Enter code manually\" "
                    + "or @content-desc=\"Have a code?\"]");

    /** Text field where the member types/pastes the invite code. */
    public static final By JOIN_CODE_INPUT =
            By.xpath("//android.widget.EditText");

    /** "Join" / "Submit" button that applies the entered invite code. */
    public static final By JOIN_CODE_SUBMIT_BUTTON =
            By.xpath("//android.widget.Button[@content-desc=\"Join\" "
                    + "or @content-desc=\"Submit\" "
                    + "or @content-desc=\"Continue\"]");

    /**
     * The confirmation the member sees once the invite code is accepted. This is the
     * only positive signal that the join itself succeeded — everything after it
     * (family switch, device parity) can fail for unrelated reasons, so catching this
     * separates "the join didn't work" from "the join worked but the next step broke".
     *
     * <p>Transient: it is polled for immediately after the Join tap rather than after
     * a sleep, because a dismissed toast is indistinguishable from one that never
     * appeared.</p>
     */
    public static final By JOIN_SUCCESS_MESSAGE =
            By.xpath("//android.view.View[@content-desc=\"Added to home successfully!\" "
                    + "or starts-with(@content-desc, \"Added to home successfully\")]");

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

    // ── Options menu inside an opened family (both Flow 2 cleanup paths) ───────
    // The same overflow control gates BOTH revocation routes: on the member's
    // detail screen it exposes "Make admin" / "Remove member" (admin_remove), and
    // on the opened family it exposes "Leave home" (member_leave). Neither option
    // is on screen until this is tapped.

    /**
     * Absolute path of that overflow control, as captured on device.
     *
     * <p><b>Brittle by necessity.</b> The node carries no content-desc and no
     * resource-id below {@code android:id/content}, so there is nothing to match on
     * but position — any change to the widget nesting silently breaks it. Both
     * callers tap it optionally and dump the tree on a miss rather than failing
     * blind. Replace this the moment the control gains a label.</p>
     */
    private static final String OPTIONS_MENU_XPATH =
            "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]"
                    + "/android.widget.FrameLayout/android.widget.FrameLayout"
                    + "/android.view.View/android.view.View/android.view.View/android.view.View"
                    + "/android.view.View[1]/android.view.View/android.view.View[2]";

    /**
     * Overflow/options control on a member's detail screen (admin_remove path).
     * See {@link #FAMILY_OPTIONS_MENU} for the same control on an opened family.
     */
    public static final By MEMBER_OPTIONS_MENU = By.xpath("//android.widget.ImageView[@content-desc=\"Member\"]/android.view.View");

    /** "Make admin" option — sibling of "Remove member" in the same menu. */
    public static final By MEMBER_MAKE_ADMIN_OPTION =
            By.xpath("//android.view.View[@content-desc=\"Make admin\" "
                    + "or @content-desc=\"Make Admin\"]");

    /** "Remove member" option — chosen instead of "Make admin" during cleanup. */
    public static final By MEMBER_REMOVE_OPTION =
            By.xpath("//android.view.View[@content-desc=\"Remove member\" "
                    + "or @content-desc=\"Remove Member\" "
                    + "or @content-desc=\"Remove\"]");

    /** Confirm button on the "Remove member" confirmation dialog. */
    public static final By MEMBER_REMOVE_CONFIRM =
            By.xpath("//android.widget.Button[@content-desc=\"Remove\" "
                    + "or @content-desc=\"Yes\" "
                    + "or @content-desc=\"Confirm\"]");

    /**
     * Parameterised: the permission-level badge text ("Super"/"Basic"/"Custom")
     * shown next to a device row in a member's device list — used to verify the
     * access the admin applied (Flow 2 presence + badge check).
     */
    public static By permissionBadgeFor(String deviceName) {
        return By.xpath("//android.view.View[contains(@content-desc, \"" + deviceName
                + "\")]/following-sibling::android.view.View[1]");
    }

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
    // Custom capability toggles — water purifier (spec §3.4)
    // Labels mirror PermissionModel.PurifierCapability.
    // ══════════════════════════════════════════════════════════════════════════

    public static final By TOGGLE_WP_CHECK_HEALTH =
            By.xpath("//android.widget.Switch[@content-desc=\"Check Health\"]");

    public static final By TOGGLE_WP_RUN_DIAGNOSTICS =
            By.xpath("//android.widget.Switch[@content-desc=\"Run Diagnostics\"]");

    public static final By TOGGLE_WP_EDIT_DEVICE =
            By.xpath("//android.widget.Switch[@content-desc=\"Edit Device\"]");

    public static final By TOGGLE_WP_EDIT_WIFI =
            By.xpath("//android.widget.Switch[@content-desc=\"Edit Wifi Details\"]");

    public static final By TOGGLE_WP_MODE_CHANGE =
            By.xpath("//android.widget.Switch[@content-desc=\"Mode Change\"]");

    public static final By TOGGLE_WP_VALUE_REFRESH =
            By.xpath("//android.widget.Switch[@content-desc=\"Value Refresh\"]");

    public static final By TOGGLE_WP_SETTINGS =
            By.xpath("//android.widget.Switch[@content-desc=\"Settings\"]");

    public static final By TOGGLE_WP_VIEW_ANALYTICS =
            By.xpath("//android.widget.Switch[@content-desc=\"View Analytics\"]");

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
    // Flow 2 — long-press a device tile on Home → "Share device"
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * The "Share device" action revealed after long-pressing a device tile on the
     * Home screen (Flow 2 entry point). Candidate labels OR-ed so the locator
     * survives copy changes. The device tile itself is located with
     * {@link #deviceCardOnHome(String)} and long-pressed via
     * {@code ActionsUtil.longPress}.
     */
    public static final By LONGPRESS_SHARE_DEVICE_OPTION =
            By.xpath("//android.view.View[@content-desc=\"Share device\" "
                    + "or @content-desc=\"Share Device\" "
                    + "or @content-desc=\"Share\"]");

    // ── "Share access to family?" confirmation dialog ─────────────────────────
    // Captured from test-output/page-source/tap_fail_super_level.xml: tapping
    // "Share device" does NOT open a permission picker — it opens this dialog:
    //   title  : "Share access to family?"
    //   body   : "Access is not shared for individual devices, but for families.
    //             If you choose to provide access, it will be given for all
    //             devices in this family"
    //   buttons: "No" / "Yes"
    // The QR + invite code screen only appears after "Yes".

    /** Title node of the share-confirmation dialog — used to detect the dialog. */
    public static final By SHARE_FAMILY_CONFIRM_TITLE =
            By.xpath("//android.view.View[@content-desc=\"Share access to family?\"]");

    /** "Yes" — proceeds to the QR / invite-code screen. */
    public static final By SHARE_FAMILY_CONFIRM_YES =
            By.xpath("//android.widget.Button[@content-desc=\"Yes\"]");

    /** "No" — dismisses the dialog without sharing. */
    public static final By SHARE_FAMILY_CONFIRM_NO =
            By.xpath("//android.widget.Button[@content-desc=\"No\"]");

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
        // Device tiles on the home screen render as ImageView (same pattern as the
        // family tiles), with content-desc "<device>\n<room>" — e.g.
        // "Aris Fan\nLiving Room". Matching only android.view.View missed them
        // entirely. View/Button are kept as fallbacks for other screens that reuse
        // this locator (e.g. per-member device lists).
        return By.xpath("//android.widget.ImageView[contains(@content-desc, \"" + deviceName + "\")]"
                + " | //android.view.View[contains(@content-desc, \"" + deviceName + "\")]"
                + " | //android.widget.Button[contains(@content-desc, \"" + deviceName + "\")]");
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

    // ══════════════════════════════════════════════════════════════════════════
    // Family switcher (Home header) — used after a member joins a new family
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * The control that OPENS the family switcher, when the currently-active family
     * name isn't known ahead of time (the member's case — they're on their own
     * family until they switch).
     *
     * <p>Confirmed against {@code test-output/page-source/admin_devices_home.xml}
     * (2026-08-07): the header is a clickable {@code android.view.View} carrying the
     * family name ("Main Family") in the top bar, and it is the first clickable View
     * in document order. The section tabs below it ("Devices" / "Rooms" /
     * "Automations") are also clickable Views, so they are excluded by name rather
     * than relying on document order alone.</p>
     */
    public static final By FAMILY_SWITCHER_TRIGGER =
            By.xpath("(//android.view.View[@clickable=\"true\""
                    + " and @content-desc != \"Devices\""
                    + " and @content-desc != \"Rooms\""
                    + " and @content-desc != \"Automations\"])[1]"
                    + " | //android.widget.Button[@content-desc=\"Switch family\"]");

    /**
     * The current-family header when the name IS known — e.g. checking whether the
     * member already sits on the target family. Prefer this over
     * {@link #FAMILY_SWITCHER_TRIGGER} whenever the caller has the name.
     */
    public static By familySwitcherHeader(String familyName) {
        return By.xpath("//android.view.View[@content-desc=\"" + familyName + "\"]"
                + " | //android.widget.Button[@content-desc=\"" + familyName + "\"]");
    }

    /**
     * A family row inside the opened switcher sheet. Distinct from
     * {@link #familyTile(String)}, which is the Manage-Family LIST tile and carries
     * a member-count prefix; switcher rows show the bare name.
     */
    public static By familySwitcherOption(String familyName) {
        return By.xpath("//android.view.View[@content-desc=\"" + familyName + "\"]"
                + " | //android.widget.ImageView[contains(@content-desc, \"" + familyName + "\")]");
    }

    // ══════════════════════════════════════════════════════════════════════════
    // Home / dashboard identification
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Present only on the Devices dashboard — the "Devices / Rooms / Automations"
     * segment row. Used to answer "are we actually on Home?".
     *
     * <p>{@code AppUtil.ensureAppHome} cannot answer that: it returns as soon as
     * {@code HomeLocators.MORE_TAB} is present, and the bottom tab bar shows on
     * EVERY root tab. A run that entered the join flow from the More tab was
     * therefore treated as "home" while still sitting on More.</p>
     */
    public static final By HOME_SCREEN_MARKER =
            By.xpath("//android.view.View[@content-desc=\"Devices\"]"
                    + " | //android.view.View[@content-desc=\"Rooms\"]"
                    + " | //android.view.View[@content-desc=\"Automations\"]"
                    + " | //android.widget.ImageView[@content-desc=\"Add your first smart device\"]");

    /**
     * The bottom-bar tab that opens the Devices dashboard. It is labelled with the
     * signed-in profile's NAME, not "Home" — "Member\nTab 2 of 3" on the member
     * phone, "Admin\nTab 2 of 3" on the admin's — so it is matched on the stable
     * "Tab 2 of 3" suffix instead of the name.
     */
    public static final By HOME_TAB =
            By.xpath("//android.widget.ImageView[contains(@content-desc, \"Tab 2 of 3\")]"
                    + " | //android.widget.Button[contains(@content-desc, \"Tab 2 of 3\")]");

    /**
     * The options/kebab control inside an OPENED family, which reveals "Leave home"
     * (member) and the other family actions. Opening the family is not enough — the
     * menu has to be tapped first, or "Leave home" is never in the tree.
     *
     * <p><b>Fragile by necessity.</b> The node carries no content-desc, so the only
     * thing available is the absolute path the user captured on 2026-08-07. Any
     * layout change in that subtree silently re-points it at a different View, and
     * nothing here will tell you it moved. The content-desc candidates are tried
     * FIRST so that a build which does label the control takes the stable route and
     * this path is never used; {@code leaveFamily} dumps the opened-family tree every
     * run so it can be replaced the moment a labelled node appears.</p>
     */
    public static final By FAMILY_OPTIONS_MENU =
            By.xpath("//android.widget.ImageView[@content-desc=\"More options\"]"
                    + " | //android.view.View[@content-desc=\"Options\"]"
                    + " | " + OPTIONS_MENU_XPATH);

    // ══════════════════════════════════════════════════════════════════════════
    // Invite-code generation: loading / error / retry
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * The blocking spinner shown while the backend mints the invite code. Captured in
     * test-output/page-source/qr_share_screen.xml as
     * {@code content-desc="Please wait..."}.
     *
     * <p>This is why code-reading must not fire straight after the Yes tap: the screen
     * is still generating, and whatever is read then is neither the code nor the
     * error. Waiting for THIS to clear is what makes the success/error distinction
     * meaningful.</p>
     */
    public static final By SHARE_CODE_LOADING =
            By.xpath("//android.view.View[@content-desc=\"Please wait...\"]"
                    + " | //android.view.View[starts-with(@content-desc, \"Please wait\")]");

    /**
     * The failure state the share screen can land in instead of producing a code —
     * reported on device as "Error generating the code".
     *
     * <p>Matched with {@code contains} on the distinctive "generating the code" stem
     * rather than the full sentence, so trailing punctuation or a wrapped detail line
     * still matches. Deliberately NOT a bare "Error": the share screens carry other
     * error copy, and a false positive here would trigger a pointless re-share.</p>
     */
    public static final By SHARE_CODE_ERROR =
            By.xpath("//android.view.View[contains(@content-desc, \"generating the code\")]"
                    + " | //android.view.View[contains(@content-desc, \"Error generating\")]"
                    + " | //android.widget.TextView[contains(@text, \"generating the code\")]");

    /**
     * In-place retry control on the error state, when the build offers one. Absent on
     * this build as far as we've seen, which is why the retry path falls back to
     * re-running the whole long-press share rather than depending on this.
     */
    public static final By SHARE_CODE_RETRY =
            By.xpath("//android.widget.Button[@content-desc=\"Retry\""
                    + " or @content-desc=\"Try again\""
                    + " or @content-desc=\"Try Again\"]"
                    + " | //android.view.View[@content-desc=\"Retry\""
                    + " or @content-desc=\"Try again\"]");

    // ══════════════════════════════════════════════════════════════════════════
    // PERMISSION LEVELS — widened locators for the with-permissions build
    //
    // The PERMISSION_LEVEL_* constants above match android.view.View only, which is
    // what the pre-levels build rendered everywhere. That is too narrow to DETECT a
    // picker with: a level rendered as a RadioButton or a Button reads as absent, and
    // "absent" is the answer that routes the whole suite into the without-permissions
    // branch. These *_ANY variants widen the node type and tolerate a subtitle, so a
    // present picker is found whatever it is built from.
    //
    // UNPINNED. Written against the spec's labels, not against a page-source capture
    // of the levels APK — no such capture exists yet. Every helper that uses them
    // dumps the tree on a miss (test-output/page-source/) so they can be replaced
    // with real nodes; PermissionEditorPage.discover() reports which variant matched.
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Any node type carrying the given permission-level label.
     *
     * <p>Matches an exact content-desc and the {@code "Super\nAll device access"}
     * title+subtitle shape Flutter uses for list options — the same shape that already
     * defeated an equality match on the member join sheet (see
     * {@link #JOIN_EXISTING_HOME_OPTION}). {@code starts-with} on the label plus a
     * newline keeps "Super" from also matching a longer word.</p>
     *
     * @param level "Super", "Basic" or "Custom"
     */
    public static By permissionLevelOption(String level) {
        String match = "@content-desc=\"" + level + "\""
                + " or starts-with(@content-desc, \"" + level + "\n\")";
        return By.xpath("//android.view.View["      + match + "]"
                + " | //android.widget.Button["     + match + "]"
                + " | //android.widget.RadioButton[" + match + "]"
                + " | //android.widget.CheckBox["   + match + "]"
                + " | //android.widget.ImageView["  + match + "]");
    }

    /** Widened "Super" level option — see {@link #permissionLevelOption}. */
    public static final By PERMISSION_LEVEL_SUPER_ANY  = permissionLevelOption("Super");

    /** Widened "Basic" level option — the default a QR share is expected to apply. */
    public static final By PERMISSION_LEVEL_BASIC_ANY  = permissionLevelOption("Basic");

    /** Widened "Custom" level option — reveals the per-capability toggles. */
    public static final By PERMISSION_LEVEL_CUSTOM_ANY = permissionLevelOption("Custom");

    /**
     * The currently-SELECTED level, for reading back what the app applied without
     * re-tapping anything. Flutter exposes selection as {@code checked} on radio-like
     * nodes and {@code selected} on segmented controls, so both are accepted.
     *
     * <p>Used by the re-share/upsert check (RP-18), which has to prove the level
     * CHANGED — a locator that only proves an option exists cannot show that.</p>
     */
    public static final By SELECTED_PERMISSION_LEVEL =
            By.xpath("//*[(@checked=\"true\" or @selected=\"true\")"
                    + " and (@content-desc=\"Super\" or @content-desc=\"Basic\""
                    + " or @content-desc=\"Custom\""
                    + " or starts-with(@content-desc, \"Super\n\")"
                    + " or starts-with(@content-desc, \"Basic\n\")"
                    + " or starts-with(@content-desc, \"Custom\n\"))]");

    /**
     * A single capability toggle by its spec label ("Control", "View Analytics",
     * "Unlock", …), across every node type a Flutter toggle can render as.
     *
     * <p>The {@code TOGGLE_*} constants above are {@code android.widget.Switch} only.
     * That is one plausible rendering of five; a checkbox-style capability list would
     * make every one of them miss, and {@code setToggle} logs a miss and carries on —
     * so a whole Custom mask would be silently left at its defaults and the test would
     * assert against permissions it never actually set.</p>
     *
     * @param label capability label exactly as it appears in {@code PermissionModel}
     */
    public static By capabilityToggle(String label) {
        String match = "@content-desc=\"" + label + "\""
                + " or starts-with(@content-desc, \"" + label + "\n\")";
        return By.xpath("//android.widget.Switch["   + match + "]"
                + " | //android.widget.CheckBox["   + match + "]"
                + " | //android.widget.ToggleButton[" + match + "]"
                + " | //android.view.View["         + match + " ][@clickable=\"true\"]");
    }

    /**
     * Every toggle-like node on the current screen, regardless of label.
     *
     * <p>This is the only way to assert a NEGATIVE about the capability list — that the
     * editor offers exactly the device's defined capabilities and no more (RP-14). A
     * per-label lookup can confirm the expected ones are present but can never reveal
     * an extra one, which is the case that matters: an unknown bit exposed in the UI is
     * a permission the backend never validated.</p>
     */
    public static final By ALL_CAPABILITY_TOGGLES =
            By.xpath("//android.widget.Switch | //android.widget.CheckBox"
                    + " | //android.widget.ToggleButton");

    // ══════════════════════════════════════════════════════════════════════════
    // PER-MEMBER DEVICE LIST  (Manage Family → member → their devices)
    // UNPINNED — candidate labels from the spec; dumped on miss.
    // ══════════════════════════════════════════════════════════════════════════

    /** "Add device" CTA on a member's device list — the Flow 2 add path (F2-04). */
    public static final By MEMBER_ADD_DEVICE_BUTTON =
            By.xpath("//android.widget.Button[@content-desc=\"Add device\""
                    + " or @content-desc=\"Add Device\""
                    + " or @content-desc=\"Add devices\"]"
                    + " | //android.view.View[@content-desc=\"Add device\""
                    + " or @content-desc=\"Add Device\"]");

    /**
     * Empty state on a member's device list — no devices shared with them yet (F2-08).
     *
     * <p>Matched on the "Add device" CTA the spec says accompanies the empty state, OR
     * on the copy itself. Both are candidates: the point of the test is to prove
     * SOMETHING informative renders rather than a blank screen, so a locator that can
     * only match one exact sentence would fail on a copy change that is not a defect.</p>
     */
    public static final By MEMBER_DEVICE_LIST_EMPTY_STATE =
            By.xpath("//android.view.View[contains(@content-desc, \"No devices\")"
                    + " or contains(@content-desc, \"no devices\")"
                    + " or contains(@content-desc, \"nothing shared\")]"
                    + " | //android.widget.Button[@content-desc=\"Add device\""
                    + " or @content-desc=\"Add Device\"]");

    /** "Remove"/"Revoke" access action on a device row in a member's device list (F2-07). */
    public static final By MEMBER_REMOVE_DEVICE_OPTION =
            By.xpath("//android.view.View[@content-desc=\"Remove device\""
                    + " or @content-desc=\"Remove access\""
                    + " or @content-desc=\"Revoke access\""
                    + " or @content-desc=\"Remove\"]"
                    + " | //android.widget.Button[@content-desc=\"Remove device\""
                    + " or @content-desc=\"Remove access\"]");

    /**
     * The confirmation dialog that must precede a revoke (F2-14).
     *
     * <p>Its TITLE, not its confirm button: the test asserts a prompt appeared before
     * the DELETE call, and {@link #REMOVE_ACCESS_CONFIRM_BUTTON} cannot distinguish "a
     * dialog is up" from "a Remove button is on the underlying screen".</p>
     */
    public static final By REMOVE_ACCESS_CONFIRM_DIALOG =
            By.xpath("//android.view.View[contains(@content-desc, \"Remove access\")"
                    + " or contains(@content-desc, \"remove access\")"
                    + " or contains(@content-desc, \"Are you sure\")]");

    // ══════════════════════════════════════════════════════════════════════════
    // NON-ADMIN AFFORDANCES  (RP-03, RP-04 — actions that must NOT be offered)
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * "Delete device" wherever it is offered — device detail, its kebab, or a
     * long-press menu.
     *
     * <p>Deliberately broad. This locator is used almost exclusively for NEGATIVE
     * assertions ("a non-admin is not offered delete"), and for those, breadth is
     * safety: a narrow locator that misses a delete control the app really does show
     * reports a pass on a genuine privilege escalation. Over-matching can only cost a
     * false failure, which a human then reads.</p>
     */
    public static final By DEVICE_DELETE_OPTION =
            By.xpath("//*[@content-desc=\"Delete device\""
                    + " or @content-desc=\"Delete Device\""
                    + " or @content-desc=\"Remove device\""
                    + " or @content-desc=\"Delete\"]");

    /**
     * Any share affordance on a device screen — icon, kebab entry or long-press action.
     * Broad for the same reason as {@link #DEVICE_DELETE_OPTION}: it backs the negative
     * assertion that a non-admin, non-owner is offered no way to share (RP-04).
     */
    public static final By DEVICE_SHARE_AFFORDANCE_ANY =
            By.xpath("//*[@content-desc=\"Share\""
                    + " or @content-desc=\"Share device\""
                    + " or @content-desc=\"Share Device\""
                    + " or @content-desc=\"Share access\"]");

    // ══════════════════════════════════════════════════════════════════════════
    // SHARE-LIST EMPTY / BLOCKED STATES  (EC-01, EC-02, RP-17, F3-12, F3-13)
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * The message shown when there is nobody to share with — a single-user home
     * (EC-01) or a home whose every member is an admin (EC-02).
     *
     * <p>Matched on distinctive stems ("are admins", "no sharing required", "only user")
     * rather than a full sentence, because the two cases word it differently and the
     * assertion is about an informative empty state existing at all.</p>
     */
    public static final By SHARE_LIST_EMPTY_MESSAGE =
            By.xpath("//android.view.View[contains(@content-desc, \"are admins\")"
                    + " or contains(@content-desc, \"no sharing required\")"
                    + " or contains(@content-desc, \"No users\")"
                    + " or contains(@content-desc, \"only user\")]");

    /**
     * "No devices to share" / "Add a device to family" — an entire-home share from an
     * empty home (F3-13). The remark on that row records both wordings, so both match.
     */
    public static final By NO_DEVICES_TO_SHARE_MESSAGE =
            By.xpath("//android.view.View[contains(@content-desc, \"No Devices to Share\")"
                    + " or contains(@content-desc, \"No devices to share\")"
                    + " or contains(@content-desc, \"Add a device to Family\")"
                    + " or contains(@content-desc, \"Add a device\")]");

    /**
     * The "Share Devices" CTA on the specific-device picker. F3-12 asserts it is
     * DISABLED with nothing selected, so callers read its {@code enabled} attribute —
     * presence alone is not the assertion.
     */
    public static final By SHARE_DEVICES_BUTTON =
            By.xpath("//android.view.View[@content-desc=\"Share Devices\""
                    + " or @content-desc=\"Share devices\"]"
                    + " | //android.widget.Button[@content-desc=\"Share Devices\""
                    + " or @content-desc=\"Share devices\"]");

    /**
     * Parameterised: a device's selection control on the specific-device share picker.
     * Widened past CheckBox — the picker may render rows as tappable Views with a
     * trailing tick rather than as checkboxes.
     */
    public static By deviceSelectControl(String deviceName) {
        return By.xpath("//android.widget.CheckBox[contains(@content-desc, \"" + deviceName + "\")]"
                + " | //android.view.View[contains(@content-desc, \"" + deviceName + "\")"
                + " and @clickable=\"true\"]"
                + " | //android.widget.ImageView[contains(@content-desc, \"" + deviceName + "\")]");
    }

    /**
     * A validation error blocking a zero-capability Custom share (RP-17).
     * Kept separate from {@link #PERMISSION_DENIED_SNACKBAR}, which is about a MEMBER
     * being refused an action — this is an ADMIN being refused an invalid save.
     */
    public static final By ZERO_PERMISSION_ERROR =
            By.xpath("//android.view.View[contains(@content-desc, \"at least one\")"
                    + " or contains(@content-desc, \"Select at least\")"
                    + " or contains(@content-desc, \"no permission\")"
                    + " or contains(@content-desc, \"No permission selected\")]");

    // ══════════════════════════════════════════════════════════════════════════
    // PINNED 2026-08-25 against com.atomberg.app 7.6.6 (versionCode 261)
    //
    // Everything in this block was read off the running build with `uiautomator
    // dump` on the bench pair (admin e5b51506054a / member 3d2bf8dd6709), not
    // written from the spec. The capture notes are in
    // docs/SHARING_PERMISSIONS_BUILD_FINDINGS_2026-08-25.md. Treat these as facts
    // about THIS build; the locators above still marked UNPINNED are not.
    //
    // Three build facts drive the design of this block:
    //
    //  1. Selection state is invisible. The Super/Basic/Custom tabs and the
    //     device-picker radios report checkable=false checked=false
    //     selected=false ALWAYS, whichever one is highlighted. So no "currently
    //     selected level" locator can ever work; read the level off the user card
    //     (see userCard) or infer it from switch state instead.
    //  2. clickable is this build's enabled/disabled signal on Flutter Views
    //     ("Share Devices", "Update Devices"), while real widgets (Switch,
    //     Button) use enabled. Both have to be checked, per node type.
    //  3. Two devices can be open in the share editor at once, and they share
    //     capability labels ("Edit Device" exists on both fan and lock). A bare
    //     //Switch[@content-desc="Edit Device"] is therefore AMBIGUOUS on that
    //     screen - scope it with capabilityToggleNear(...).
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * "Manage Users" on the device long-press sheet - the entry point to the
     * per-device permission editor, new on the permissions build.
     *
     * <p><b>Do not tap this sheet by position.</b> The option order differs per
     * device type: on the fan it is second (Edit device, Manage Users, Share
     * device, Delete device), on the lock it is last (Edit device, Share device,
     * Delete device, Manage Users).</p>
     */
    public static final By LONGPRESS_MANAGE_USERS_OPTION =
            By.xpath("//android.view.View[@content-desc=\"Manage Users\""
                    + " or @content-desc=\"Manage users\"]");

    /** Screen title of the per-device permission editor. */
    public static final By MANAGE_USERS_TITLE =
            By.xpath("//android.view.View[@content-desc=\"Manage Users\"]");

    /** Screen title of the share flow's device picker. */
    public static final By MANAGE_DEVICES_TITLE =
            By.xpath("//android.view.View[@content-desc=\"Manage Devices\"]");

    /**
     * The "Update Devices" CTA at the bottom of Manage Users.
     *
     * <p>Rendered as a View, and clickable=true ONLY while there is an unsaved
     * change pending - which makes it the signal for "the editor registered my
     * edit", not merely a button to press.</p>
     */
    public static final By UPDATE_DEVICES_BUTTON =
            By.xpath("//android.view.View[@content-desc=\"Update Devices\"]");

    /**
     * "Update Permission Role" confirmation, raised by "Update Devices".
     * Body: "Are you sure you want to update user role?", buttons Cancel / Yes.
     */
    public static final By UPDATE_ROLE_DIALOG_TITLE =
            By.xpath("//android.view.View[@content-desc=\"Update Permission Role\"]");

    /** "Yes" on the Update-Permission-Role confirmation. */
    public static final By UPDATE_ROLE_CONFIRM_YES =
            By.xpath("//android.widget.Button[@content-desc=\"Yes\"]");

    /** "Cancel" on the Update-Permission-Role confirmation. */
    public static final By UPDATE_ROLE_CANCEL =
            By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]");

    /**
     * "Share this device with family?" - an EXTRA confirmation the lock raises
     * before the device picker opens. The fan does NOT raise it, so a share flow
     * must tolerate its absence rather than wait for it.
     *
     * <p>Body: "You can share this individual device with your family members.
     * This will give family members access to this device." Note "individual
     * device": the opposite of the old family-wide wording.</p>
     */
    public static final By SHARE_INDIVIDUAL_DEVICE_DIALOG =
            By.xpath("//android.view.View[@content-desc=\"Share this device with family?\"]"
                    + " | //android.view.View[starts-with(@content-desc,"
                    + " \"Share this device\")]");

    /**
     * The tick in the Manage Devices app bar that selects EVERY device at once.
     * Unlabelled (empty content-desc), so it is matched as a clickable Button
     * carrying no description - on that screen it is the only one.
     */
    public static final By SELECT_ALL_DEVICES_BUTTON =
            By.xpath("//android.widget.Button[@clickable=\"true\""
                    + " and @content-desc=\"\"]");

    /**
     * A user's card in Manage Users, whose content-desc is "name\nlevel" - e.g.
     * "Member\nCustom". The admin's own card reads "Admin\nAdmin" (role, not
     * level).
     *
     * <p>This is the ONLY reliable read-back of the persisted level, and it shows
     * the SAVED value - a pending, unsaved tab tap does not change it.</p>
     */
    public static By userCard(String userName) {
        return By.xpath("//android.widget.ImageView[starts-with(@content-desc, \""
                + userName + "\n\") and not(contains(@content-desc, \"Tab \"))]");
    }

    /**
     * Every user card on Manage Users, for counting and enumerating the user list.
     *
     * <p>The "Tab " exclusion is load-bearing, not defensive: the bottom navigation
     * renders as ImageView nodes whose content-desc is "Admin\nTab 2 of 3" /
     * "Member\nTab 2 of 3" - the same name-newline-something shape as a user card,
     * and carrying the very names being looked up. Without the exclusion,
     * userCard("Admin") matches the nav bar and levelOf("Admin") returns
     * "Tab 2 of 3".</p>
     */
    public static final By ALL_USER_CARDS =
            By.xpath("//android.widget.ImageView[contains(@content-desc, \"\n\")"
                    + " and not(contains(@content-desc, \"Tab \"))]");

    /**
     * A capability toggle scoped to the card of a named user or device, for the
     * screens where two capability lists are on screen at once (build fact 3 at
     * the top of this block).
     *
     * @param anchorDesc start of the owning card's content-desc - a user name on
     *                   Manage Users, a device name on Manage Devices
     * @param label      the capability label
     */
    public static By capabilityToggleNear(String anchorDesc, String label) {
        return scopedTo(anchorDesc, "android.widget.Switch", label);
    }

    /**
     * A permission-level tab scoped to one card, same reason as
     * {@link #capabilityToggleNear}.
     */
    public static By permissionLevelNear(String anchorDesc, String level) {
        return scopedTo(anchorDesc, "android.view.View", level);
    }

    /**
     * The first node of {@code nodeClass} carrying {@code label} that belongs to the
     * card whose content-desc starts with {@code anchorDesc}.
     *
     * <p><b>Why both axes.</b> The two editors nest their controls differently, and a
     * single axis is wrong on one of them:</p>
     * <ul>
     *   <li>on <b>Manage Users</b> the level tabs and capability switches are CHILDREN
     *       of the user card, so {@code following::} - which excludes descendants -
     *       skips straight past them and lands on the NEXT card's controls. That is the
     *       worst possible failure here: asking for the admin row returns the member
     *       row, so a locked-row assertion reads an editable row and passes;</li>
     *   <li>on <b>Manage Devices</b> the inline editor is a SIBLING that follows the
     *       device row, where {@code descendant::} finds nothing.</li>
     * </ul>
     *
     * <p>Unioning the axes and taking {@code [1]} resolves both: XPath returns the
     * union in document order, and a descendant always precedes a following node, so
     * the nested case wins whenever it exists. Verified against captured dumps of both
     * screens.</p>
     */
    private static By scopedTo(String anchorDesc, String nodeClass, String label) {
        String anchor = "//*[starts-with(@content-desc, \"" + anchorDesc + "\")]";
        String pred   = "[@content-desc=\"" + label + "\"]";
        return By.xpath("(" + anchor + "/descendant::" + nodeClass + pred
                + " | " + anchor + "/following::" + nodeClass + pred + ")[1]");
    }

    /**
     * "Create or Join" on the family switcher sheet - the member's route to the
     * join screen on this build.
     *
     * <p>The + FAB does NOT lead here any more: it goes straight to the BLE
     * "Looking for devices" scan. The join path is
     * family switcher then Create or Join then Join an existing smart home.</p>
     */
    public static final By CREATE_OR_JOIN_OPTION =
            By.xpath("//android.view.View[@content-desc=\"Create or Join\"]");

    /** A family row in the family-switcher sheet, by exact name (untruncated there). */
    public static By familySwitcherRow(String familyName) {
        return By.xpath("//android.view.View[@content-desc=\"" + familyName + "\"]");
    }

    /**
     * A device row in the Manage Devices picker.
     *
     * <p>Rows render as clickable ImageView nodes whose content-desc is
     * "device\nroom" - the same shape as a Home tile. Tapping the row is what
     * toggles selection; the trailing radio is not a separate node.</p>
     */
    public static By deviceRowInPicker(String deviceName) {
        return By.xpath("//android.widget.ImageView[starts-with(@content-desc, \""
                + deviceName + "\") and @clickable=\"true\"]");
    }

    /** The "15 minutes" validity notice on the invite-code sheet. */
    public static final By SHARE_CODE_VALIDITY_NOTICE =
            By.xpath("//android.view.View[contains(@content-desc, \"valid only for\")]");
}
