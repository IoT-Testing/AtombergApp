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
}
