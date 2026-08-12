package app.sharing;

import app.resources.Locators.Android.Sharing.SharingLocators;
import app.util.ActionsUtil;
import app.util.AppUtil;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import app.sharing.MemberDevicePage.*;
import java.time.Duration;
import java.util.List;

import static app.resources.Locators.Android.AppLocators.MoreTab.MANAGE_FAMILY;
import static app.resources.Locators.Android.HomeLocators.MORE_TAB;
import static app.resources.Locators.Android.HomeLocators.DEVICES;
import static app.resources.Locators.Android.HomeLocators.APP_LOGO;
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
        // Callers arrive from anywhere — notably the QR/share screen at the end of a
        // share, which has no bottom bar, so reaching straight for the More tab there
        // throws. Get back to a root screen first.
        returnToHomeScreen();
        tap(MORE_TAB, "More Tab");
        ActionsUtil.SSleep(1);
        dumpTree("more_tab_screen");   // capture the More-tab tree to locate the real "Manage family" node
        scrollAndTap(MANAGE_FAMILY, "Manage Family");
        ActionsUtil.SSleep(2);
        AppUtil.captureScreenshot(driver, "manage_family_screen");
        return this;
    }

    /**
     * On the Manage Family list, tap the family tile to open it.
     *
     * <p>The tile is an {@code ImageView} whose content-desc carries the member
     * count as a prefix ("1\nMain Family", "2\nMain Family" once a member joins),
     * so {@link SharingLocators#familyTile} matches on the NAME only.</p>
     */
    public DeviceSharingPage openFamilyHome(String homeNameContains) {
        tap(familyTile(homeNameContains), "Family tile: " + homeNameContains);
        ActionsUtil.SSleep(2);
        AppUtil.captureScreenshot(driver, "family_opened_" + homeNameContains.replace(" ", "_"));
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
     * @param editWifi       enable/disable edit Wi-Fi
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

    /**
     * Flow 2 helper: open a shared fan's permission editor for the current
     * member, switch it to Custom, apply the given capability toggles, and Save.
     * Used by the UI-control permission matrix to drive one capability
     * combination per iteration.
     */
    public DeviceSharingPage flow2_setFanCustomPermission(
            String deviceName, boolean control, boolean editDevice,
            boolean viewAnalytics, boolean editWifi, boolean automations) {

        tap(memberDeviceRow(deviceName), "Device row: " + deviceName);
        ActionsUtil.SSleep(1);

        setFanCustomPermissions(control, editDevice, viewAnalytics, editWifi, automations);

        tap(PERMISSION_SAVE_BUTTON, "Save button");
        ActionsUtil.SSleep(2);
        AppUtil.captureScreenshot(driver, "flow2_custom_fan_" + deviceName.replace(" ", "_"));
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // HOME NAVIGATION + device presence (Flow 2 pre-flight)
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Best-effort: pop any nested screens and land on the Devices/home dashboard
     * where device tiles are shown. Used before a long-press share and by the
     * pre-flight that verifies all target devices are present.
     */
    public DeviceSharingPage goToDevicesHome() {
        // Never blind-loop navigate().back() here — on a root screen that exits the
        // app. ensureAppHome() checks foreground state after each press instead.
        AppUtil.ensureAppHome(driver);
        tapFirstPresent("Devices/Home tab", DEVICES, APP_LOGO);
        ActionsUtil.SSleep(2);
        AppUtil.captureScreenshot(driver, "admin_devices_home");
        return this;
    }

    /**
     * Returns true if the named device tile is present on the admin's home. Scrolls
     * a few times so tiles below the fold are still found.
     */
    public boolean isDeviceOnHome(String deviceName) {
        By tile = deviceCardOnHome(deviceName);
        if (isPresent(tile)) return true;

        // NOTE: ActionsUtil.Scroll.Up() is what reveals content FURTHER DOWN a list
        // (the finger swipes upward). Scroll.Down() moves back toward the top, so
        // calling it here just bounced at the top of the list forever. Every other
        // scroll-to-find in this codebase uses Scroll.Up for the same reason.
        String previous = null;
        for (int i = 0; i < 4; i++) {
            ActionsUtil.Scroll.Up(driver);
            ActionsUtil.SSleep(1);
            if (isPresent(tile)) return true;

            // Stop early once the list stops moving (bottom reached) instead of
            // burning the remaining scrolls on an unchanging screen.
            String current = pageSourceQuietly();
            if (current != null && current.equals(previous)) {
                break;
            }
            previous = current;
        }
        return false;
    }

    /**
     * Scrapes the admin's Home dashboard for every device tile, scrolling to the
     * bottom so tiles below the fold are included. This is the "expected" side of
     * the post-join parity check — after the member joins the family, their home
     * must list the same devices.
     *
     * @return tile content-descs ("&lt;device&gt;\n&lt;room&gt;"), de-duplicated
     */
    public java.util.List<String> listDevicesOnHome() {
        goToDevicesHome();

        java.util.LinkedHashSet<String> all =
                new java.util.LinkedHashSet<>(AppUtil.listDeviceTilesOnHome(driver));
        String previous = null;
        for (int i = 0; i < 6; i++) {
            String current = pageSourceQuietly();
            if (current != null && current.equals(previous)) break;  // list stopped moving
            previous = current;

            ActionsUtil.Scroll.Up(driver);
            ActionsUtil.SSleep(1);
            all.addAll(AppUtil.listDeviceTilesOnHome(driver));
        }

        java.util.List<String> tiles = new java.util.ArrayList<>(all);
        return tiles;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // FLOW 2 (long-press) — Home tile → "Share device" → apply access → QR/code
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Flow 2 entry point: long-press a device tile on the Home screen and tap
     * "Share device". The tile is located with {@link SharingLocators#deviceCardOnHome}
     * and long-pressed via {@link ActionsUtil#longPress(AndroidDriver, int, int)} at
     * the tile's center (the app exposes the Share action from a long-press context
     * menu / bottom sheet rather than a tap).
     *
     * @param deviceName display name of the device tile to share
     */
    public DeviceSharingPage shareDeviceFromHomeLongPress(String deviceName) {
        // A prior iteration's cleanup can leave the admin deep in Manage Family;
        // return to the Devices dashboard so the tile is reachable.
        goToDevicesHome();

        By tile = deviceCardOnHome(deviceName);
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(tile));
        int cx = el.getLocation().getX() + (el.getSize().getWidth() / 2);
        int cy = el.getLocation().getY() + (el.getSize().getHeight() / 2);
        ActionsUtil.longPress(driver, cx, cy);
        ActionsUtil.SSleep(1);
        AppUtil.captureScreenshot(driver, "long-press_menu_" + deviceName.replace(" ", "_"));

        tap(LONGPRESS_SHARE_DEVICE_OPTION, "Share device (long-press)");
        ActionsUtil.SSleep(2);
        AppUtil.captureScreenshot(driver, "share_device_screen_" + deviceName);

        // "Share device" opens a confirmation dialog, not the share screen itself.
        confirmShareToFamily();
        return this;
    }

    /**
     * Answers the "Share access to family?" dialog that the app raises after
     * "Share device" is tapped. The dialog warns that access is granted for the
     * whole family rather than the single device, and offers No / Yes; "Yes" is
     * what advances to the QR + invite-code screen.
     *
     * <p>Tolerant by design: if the dialog is absent (build without it, or already
     * dismissed) this is a no-op, so the caller's flow is unchanged.</p>
     *
     * @return true if the dialog was present and confirmed
     */
    public boolean confirmShareToFamily() {
        if (!isPresent(SHARE_FAMILY_CONFIRM_TITLE) && !isPresent(SHARE_FAMILY_CONFIRM_YES)) {
            return false;
        }
        AppUtil.captureScreenshot(driver, "share_access_to_family_dialog");
        tap(SHARE_FAMILY_CONFIRM_YES, "Share access to family? → Yes");
        ActionsUtil.SSleep(3);
        AppUtil.captureScreenshot(driver, "share_confirmed_qr_screen");

        // The QR/invite-code screen's real content-desc values have not been
        // captured on-device yet; dump it so the code/Copy locators can be
        // pinned to actual nodes instead of the current candidate guesses.
        dumpTree("qr_share_screen");
        return true;
    }

    /**
     * Applies the access captured in a {@link SharedAccess} to the current share
     * screen: selects Super / Basic / Custom and, for Custom, flips each capability
     * toggle to match the spec's expected mask. Then confirms so the QR + invite
     * code are generated. The {@code spec} itself is the "store the set access to
     * verify later" record the member phone reads back.
     *
     * @param spec the access the admin intends to grant (source of truth)
     */
    public DeviceSharingPage applyShareAccess(SharedAccess spec) {
        // A confirmation dialog may still be up if the caller reached this screen
        // by a path that didn't already answer it.
        confirmShareToFamily();

        // The picker is only driven when the app actually offers one. On the
        // long-press path it does not: "Share device" leads straight to the
        // family-level confirmation and then the QR screen, with no Super/Basic/
        // Custom step. Hard-failing there cost the whole matrix at row 1, so a
        // missing picker is now recorded rather than thrown.
        permissionLevelApplied = isPresent(PERMISSION_LEVEL_SUPER)
                || isPresent(PERMISSION_LEVEL_BASIC)
                || isPresent(PERMISSION_LEVEL_CUSTOM);

        if (permissionLevelApplied) {
            switch (spec.level()) {
                case SUPER  -> selectPermissionLevel("Super");
                case BASIC  -> selectPermissionLevel("Basic");
                case CUSTOM -> {
                    selectPermissionLevel("Custom");
                    ActionsUtil.SSleep(1);
                    applyCustomToggles(spec);
                }
            }
            ActionsUtil.SSleep(1);
        } else {
            System.out.println("[DeviceSharingPage] No permission-level picker on this screen — "
                    + "'" + spec.level() + "' was NOT applied. Sharing is family-wide on this path; "
                    + "any per-level assertion for " + spec + " is unverified.");
            dumpTree("no_permission_picker_" + spec.deviceType());
        }

        // The share screen ends with a Share/Confirm action, then the QR appears.
        if (!tapFirstPresent("Confirm share",
                SHARE_CONFIRM_BUTTON, PERMISSION_CONFIRM_BUTTON, QR_SHARE_BUTTON)) {
            System.out.println("[DeviceSharingPage] No explicit confirm button — assuming QR auto-generates.");
        }
        ActionsUtil.SSleep(3);
        AppUtil.captureScreenshot(driver, "share_access_applied_" + spec.deviceType()
                + "_" + spec.label());
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // PROBE — capture the share journey on a new build (no assertions)
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Walks the long-press share flow one step at a time, dumping the tree and a
     * screenshot at each, and reports whether a Super/Basic/Custom picker appears.
     * Asserts nothing — its only job is to hand back the real node names so the
     * permission-level locators can be pinned instead of guessed.
     *
     * <p>Written for the permission-levels APK. Two things decide the shape of that
     * work and neither can be answered from the current build:</p>
     * <ol>
     *   <li><b>Is there a picker, and where?</b> On the build tested through
     *       2026-08-10 "Share device" went straight to the family confirmation and
     *       then the QR screen, with no level step at all.</li>
     *   <li><b>Is sharing still family-wide?</b> The "Share access to family?" dialog
     *       says access is granted for every device in the family. If that dialog is
     *       gone, sharing is now per-device and the access matrix is a genuine
     *       per-device grid; if it remains, a per-device matrix is still meaningless
     *       and the levels apply to the whole family.</li>
     * </ol>
     *
     * <p>Leaves the phone on whatever screen the flow ends at, then returns Home.</p>
     *
     * @param deviceName display name of the tile to long-press
     */
    public void probeShareFlow(String deviceName) {
        System.out.println("\n========== SHARE FLOW PROBE: " + deviceName.replace("\n", " / ")
                + " ==========");

        goToDevicesHome();
        capture("probe_01_home");

        By tile = deviceCardOnHome(deviceName);
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(tile));
        int cx = el.getLocation().getX() + (el.getSize().getWidth() / 2);
        int cy = el.getLocation().getY() + (el.getSize().getHeight() / 2);
        ActionsUtil.longPress(driver, cx, cy);
        ActionsUtil.SSleep(2);
        capture("probe_02_longpress_menu");

        if (!tapFirstPresent("Share device (long-press)", LONGPRESS_SHARE_DEVICE_OPTION)) {
            System.err.println("[Probe] 'Share device' not found on the long-press menu — "
                    + "see probe_02_longpress_menu.xml.");
            returnToHomeScreen();
            return;
        }
        ActionsUtil.SSleep(2);
        capture("probe_03_after_share_device");
        reportPicker("immediately after 'Share device'");

        boolean familyDialog = isPresent(SHARE_FAMILY_CONFIRM_TITLE)
                || isPresent(SHARE_FAMILY_CONFIRM_YES);
        if (familyDialog) {
            System.out.println("[Probe] FAMILY-WIDE dialog still present — access is granted "
                    + "for the whole family, so a PER-DEVICE access matrix stays meaningless.");
            tapFirstPresent("Share access to family? → Yes", SHARE_FAMILY_CONFIRM_YES);
            ActionsUtil.SSleep(3);
        } else {
            System.out.println("[Probe] No family-wide dialog — this build may share PER DEVICE. "
                    + "If so, the per-device matrix in SharedAccess becomes meaningful again.");
        }
        capture("probe_04_after_family_dialog");

        boolean picker = reportPicker("after the family dialog");
        if (picker && tapFirstPresent("Custom level", PERMISSION_LEVEL_CUSTOM)) {
            ActionsUtil.SSleep(2);
            capture("probe_05_custom_toggles");
            System.out.println("[Probe] Custom selected — probe_05_custom_toggles.xml holds the "
                    + "per-capability toggle nodes for TOGGLE_* pinning.");
        }

        System.out.println("[Probe] QR visible: " + isQrCodeVisible()
                + " | invite code: " + readInviteCode());
        System.out.println("========== END PROBE ==========\n");
        returnToHomeScreen();
    }

    /** Logs which of the three level options are on screen right now. */
    private boolean reportPicker(String where) {
        boolean superLvl  = isPresent(PERMISSION_LEVEL_SUPER);
        boolean basicLvl  = isPresent(PERMISSION_LEVEL_BASIC);
        boolean customLvl = isPresent(PERMISSION_LEVEL_CUSTOM);
        boolean any = superLvl || basicLvl || customLvl;

        System.out.println("[Probe] Level picker " + where + ": "
                + (any ? "PRESENT" : "absent")
                + "  (Super=" + superLvl + ", Basic=" + basicLvl + ", Custom=" + customLvl + ")");
        if (!any) {
            System.out.println("[Probe]   → if the build DOES show levels here, the "
                    + "PERMISSION_LEVEL_* locators are wrong; read the dumped XML for this step.");
        }
        return any;
    }

    /** Screenshot + tree dump under one tag, so the pair is easy to line up. */
    private void capture(String tag) {
        AppUtil.captureScreenshot(driver, tag);
        dumpTree(tag);
    }

    /** Whether the last {@link #applyShareAccess} actually found a level picker to drive. */
    private boolean permissionLevelApplied;

    /**
     * True when the most recent {@link #applyShareAccess} found and drove a
     * Super/Basic/Custom picker. False means the app never offered one, so the
     * spec's level was not applied and must not be asserted as if it had been.
     */
    public boolean wasPermissionLevelApplied() {
        return permissionLevelApplied;
    }

    /** Flips the per-capability Custom toggles for the device family in {@code spec}. */
    private void applyCustomToggles(SharedAccess spec) {
        int mask = spec.expectedMask();
        switch (spec.deviceType()) {
            case FAN -> {
                setToggle(TOGGLE_FAN_CONTROL,        bit(mask, FAN_CONTROL_BIT),        "Fan Control");
                setToggle(TOGGLE_FAN_EDIT_DEVICE,    bit(mask, FAN_EDIT_DEVICE_BIT),    "Fan Edit Device");
                setToggle(TOGGLE_FAN_VIEW_ANALYTICS, bit(mask, FAN_VIEW_ANALYTICS_BIT), "Fan View Analytics");
                setToggle(TOGGLE_FAN_EDIT_WIFI,      bit(mask, FAN_EDIT_WIFI_BIT),      "Fan Edit Wifi");
                setToggle(TOGGLE_FAN_AUTOMATIONS,    bit(mask, FAN_AUTOMATIONS_BIT),    "Fan Automations");
            }
            case LOCK -> {
                setToggle(TOGGLE_LOCK_VIEW_KEYS_PINS,     bit(mask, 0), "Lock View Keys & Pins");
                setToggle(TOGGLE_LOCK_VIEW_SETTINGS,      bit(mask, 1), "Lock View Settings");
                setToggle(TOGGLE_LOCK_VIEW_HISTORY,       bit(mask, 2), "Lock View History");
                setToggle(TOGGLE_LOCK_UNLOCK,             bit(mask, 3), "Lock Unlock");
                setToggle(TOGGLE_LOCK_ADD_UNLOCK_METHODS, bit(mask, 4), "Lock Add Unlock Methods");
                setToggle(TOGGLE_LOCK_EDIT_DEVICE,        bit(mask, 8), "Lock Edit Device");
            }
            case WATER_PURIFIER -> {
                setToggle(TOGGLE_WP_CHECK_HEALTH,   bit(mask, 0), "WP Check Health");
                setToggle(TOGGLE_WP_RUN_DIAGNOSTICS,bit(mask, 1), "WP Run Diagnostics");
                setToggle(TOGGLE_WP_EDIT_DEVICE,    bit(mask, 2), "WP Edit Device");
                setToggle(TOGGLE_WP_EDIT_WIFI,      bit(mask, 3), "WP Edit Wifi");
                setToggle(TOGGLE_WP_MODE_CHANGE,    bit(mask, 4), "WP Mode Change");
                setToggle(TOGGLE_WP_VALUE_REFRESH,  bit(mask, 5), "WP Value Refresh");
                setToggle(TOGGLE_WP_SETTINGS,       bit(mask, 6), "WP Settings");
                setToggle(TOGGLE_WP_VIEW_ANALYTICS, bit(mask, 7), "WP View Analytics");
            }
        }
        AppUtil.captureScreenshot(driver, "custom_toggles_" + spec.deviceType());
    }

    // Fan capability bit positions (mirror PermissionModel.FanCapability).
    private static final int FAN_CONTROL_BIT        = 0;
    private static final int FAN_EDIT_DEVICE_BIT    = 1;
    private static final int FAN_VIEW_ANALYTICS_BIT = 2;
    private static final int FAN_EDIT_WIFI_BIT      = 3;
    private static final int FAN_AUTOMATIONS_BIT    = 4;

    private static boolean bit(int mask, int position) {
        return (mask & (1 << position)) != 0;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // FLOW 2 (cleanup) — admin removes a member from the family
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Flow 2 cleanup: removes a member from the family so the next iteration starts
     * clean. Reuses the Manage Family navigation, opens the member's detail screen,
     * and — in the same menu where "Make admin" appears — taps "Remove member"
     * instead, then confirms.
     *
     * @param homeHint   substring of the family/home name row
     * @param memberName display name of the member to remove
     */
    public DeviceSharingPage removeMemberFromFamily(String homeHint, String memberName) {
        // The admin reaches this straight off the QR screen, with a dashboard rendered
        // BEFORE the member joined. Manage Family is populated from that stale state,
        // so without a refresh the member row can be absent and openMemberPermissions
        // fails on a member who is demonstrably in the family.
        refreshHomeDashboard();
        navigateToManageFamily();
        openFamilyHome(homeHint);
//        openMemberPermissions(memberName);

        // Open the member options menu if one is present (the menu that also
        // offers "Make admin"); otherwise the Remove option may be inline.
        tapFirstPresent("Member options menu", MEMBER_OPTIONS_MENU);
        ActionsUtil.SSleep(1);

        tap(MEMBER_REMOVE_OPTION, "Remove member");
        ActionsUtil.SSleep(1);
        tapFirstPresent("Confirm remove member", MEMBER_REMOVE_CONFIRM);
        ActionsUtil.SSleep(2);
        AppUtil.captureScreenshot(driver, "member_removed_" + memberName.replace(" ", "_"));
        return this;
    }

    /**
     * True when the Devices dashboard is on screen, keyed on the
     * "Devices / Rooms / Automations" segment row that exists nowhere else.
     */
    public boolean isOnHomeScreen() {
        return isPresent(HOME_SCREEN_MARKER);
    }

    /**
     * Pull-to-refresh the admin's Devices dashboard so it reflects server-side changes
     * made since the screen was drawn — chiefly a member who joined while the admin
     * sat on the QR screen.
     *
     * <p>{@code Scroll.Down} is the refresh gesture despite the name: it drags the
     * finger DOWNWARD (y 0.5 → 0.8), which is the pull-to-refresh direction.
     * {@code Scroll.Up} drags upward to reveal content further down a list, so it is
     * the wrong one here — the same naming trips up
     * {@link #isDeviceOnHome(String)}, which wants {@code Up} for exactly that reason.</p>
     *
     * <p>Best-effort: if Home can't be reached this logs and returns rather than
     * throwing, since callers run it as a freshness measure and not as the step
     * under test.</p>
     */
    public DeviceSharingPage refreshHomeDashboard() {
        if (!returnToHomeScreen()) {
            System.err.println("[DeviceSharingPage] Not on Home — skipping dashboard refresh.");
            return this;
        }
        ActionsUtil.Scroll.Down(driver);           // swipe down
        ActionsUtil.Scroll.Down(driver);           // == pull-to-refresh
        ActionsUtil.SSleep(3);                     // let the refetch settle
        AppUtil.captureScreenshot(driver, "admin_home_refreshed");
        System.out.println("[DeviceSharingPage] Admin dashboard refreshed.");
        return this;
    }

    /**
     * Lands the admin phone on the Devices dashboard and reports whether it got there.
     *
     * <p>Needed because the share flow ends on the QR screen, which has no bottom tab
     * bar at all — so any later step that reaches for the More tab has to back out
     * first. {@code AppUtil.ensureAppHome} is not sufficient on its own: it returns as
     * soon as {@code MORE_TAB} appears, and that bar shows on every root tab, so it can
     * stop on Analytics or More and call it done.</p>
     *
     * @return true if the dashboard is on screen when this returns
     */
    public boolean returnToHomeScreen() {
        for (int attempt = 1; attempt <= 3; attempt++) {
            AppUtil.ensureAppHome(driver);
            ActionsUtil.SSleep(1);
            if (isOnHomeScreen()) return true;

            // The dashboard tab is labelled with the profile name ("Admin\nTab 2 of 3"),
            // so HOME_TAB matches the "Tab 2 of 3" suffix rather than a label.
            if (isPresent(HOME_TAB)) {
                tap(HOME_TAB, "Home/dashboard tab");
            } else {
                System.out.println("[DeviceSharingPage] No bottom bar (attempt " + attempt
                        + ") — likely the QR/share screen; pressing back.");
                try { driver.navigate().back(); } catch (Exception ignored) {}
            }
            ActionsUtil.SSleep(2);
            if (isOnHomeScreen()) return true;
        }

        System.err.println("[DeviceSharingPage] Could not reach the Home/Devices dashboard.");
        AppUtil.captureScreenshot(driver, "admin_home_not_reached");
        dumpTree("admin_home_not_reached");
        return false;
    }


    // ══════════════════════════════════════════════════════════════════════════
    // FLOW 3 — Share via QR  (spec §4, Flow 3)
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Flow 3: open the share bottom sheet, using the family name configured as
     * {@code ADMIN_FAMILY_HINT} (default "Main Family").
     */
    public DeviceSharingPage flow3_openShareBottomSheet() {
        return flow3_openShareBottomSheet(
                SharingConfig.get("ADMIN_FAMILY_HINT", "Main Family"));
    }

    /**
     * Flow 3: Manage Family → <b>open the family</b> → tap the (+) to open the
     * share bottom sheet.
     *
     * <p>The (+) lives <em>inside</em> an opened family, not on the Manage Family
     * list, so the family row must be tapped first — skipping that step is why the
     * FAB lookup failed with NoSuchElementException. The open is skipped when the
     * FAB is already on screen (i.e. the family is already open).</p>
     *
     * @param familyName family/home name to open (matched with contains())
     */
    public DeviceSharingPage flow3_openShareBottomSheet(String familyName) {
        // Gate on the FAMILY TILE, never on FAB_ADD_MEMBER. The FAB's locator is a
        // generic absolute path (.../View/View/View[3]) that also matches unrelated
        // nodes on the Manage Family LIST screen, so "is the FAB present?" answered
        // TRUE while still on the list — the family was never opened and the
        // subsequent clickable-wait timed out. The family tile only exists on the
        // list, so its presence is an unambiguous "not opened yet".
        if (isPresent(familyTile(familyName))) {
            openFamilyHome(familyName);
            ActionsUtil.SSleep(1);
        }
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
            ActionsUtil.SSleep(1);
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
            ActionsUtil.SSleep(1);
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
            ActionsUtil.SSleep(1);
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
    // INVITE CODE — camera-less cross-phone join  (spec §4, Flow 3, step 3)
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Reads the invite/share code generated on the admin's QR screen so it can be
     * carried to the member phone WITHOUT camera scanning (which Appium cannot
     * drive). Two strategies are tried, in order:
     *   1. Tap "Copy" then read the admin device clipboard (most apps copy the
     *      code/link to the clipboard).
     *   2. Fall back to reading the visible code text element's content-desc and
     *      extracting the token after "Code:".
     *
     * @return the invite code string, or null if it could not be read
     */
    public String readInviteCode() {
        // Strategy 1 — Copy button → clipboard
        if (isPresent(SHARE_CODE_COPY_BUTTON)) {
            tap(SHARE_CODE_COPY_BUTTON, "Copy code button");
            ActionsUtil.SSleep(1);
            try {
                String clip = driver.getClipboardText();
                if (clip != null && !clip.isBlank()) {
                    return clip.trim();
                }
            } catch (Exception e) {
                System.err.println("[DeviceSharingPage] Clipboard read failed: " + e.getMessage());
            }
        }

        // Strategy 2 — visible code text
        try {
            WebElement codeEl = driver.findElement(SHARE_CODE_TEXT);
            String desc = codeEl.getDomAttribute("content-desc");
            if (desc != null && !desc.isBlank()) {
                String code = desc.contains("Code:")
                        ? desc.substring(desc.indexOf("Code:") + 5).trim()
                        : desc.trim();
                System.out.println("[DeviceSharingPage] Invite code (from code text): " + code);
                return code;
            }
        } catch (NoSuchElementException e) {
            System.err.println("[DeviceSharingPage] Invite code text not found.");
        }

        // Strategy 3 — scan the page source for a code-shaped token. The QR
        // screen's real content-desc values aren't pinned yet, so strategies 1-2
        // depend on candidate locators that may not match this build. This scan
        // does not, which keeps the run alive long enough to produce the dump
        // that lets the locators be fixed properly.
        String source = pageSourceQuietly();
        if (source != null) {
            java.util.regex.Matcher m = INVITE_CODE_PATTERN.matcher(source);
            if (m.find()) {
                String code = m.group(1);
                System.out.println("[DeviceSharingPage] Invite code (from page source scan): " + code);
                return code;
            }
        }

        AppUtil.captureScreenshot(driver, "invite_code_not_read");
        dumpTree("invite_code_not_read");
        return null;
    }

    /**
     * Matches a standalone 6–10 char uppercase-alphanumeric invite code inside a
     * {@code content-desc}. Anchored on the attribute so ordinary UI copy and the
     * XML's own identifiers can't be mistaken for a code.
     */
    private static final java.util.regex.Pattern INVITE_CODE_PATTERN =
            java.util.regex.Pattern.compile("content-desc=\"([A-Z0-9]{6,10})\"");

    // ══════════════════════════════════════════════════════════════════════════
    // Invite code with retry — "Error generating the code" failsafe
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Outcome of {@link #obtainInviteCode}: the code if one was produced, plus the
     * full history of what went wrong on the way.
     *
     * <p>Carried back to the test rather than logged and forgotten, because a code
     * that only appeared on attempt 3 still means the app failed twice — and a run
     * that reports nothing but PASS hides a real product defect.</p>
     */
    public static final class ShareCodeResult {
        /** The invite code, or null when every attempt failed. */
        public final String code;
        /** How many share attempts were made (1 == worked first time). */
        public final int attempts;
        /** One entry per observed "Error generating the code", in order. */
        public final java.util.List<String> errors;
        /** Best-effort root-cause reading, or null when no error was seen. */
        public final String diagnosis;

        ShareCodeResult(String code, int attempts,
                        java.util.List<String> errors, String diagnosis) {
            this.code = code;
            this.attempts = attempts;
            this.errors = java.util.List.copyOf(errors);
            this.diagnosis = diagnosis;
        }

        public boolean succeeded()     { return code != null && !code.isBlank(); }
        public boolean hadCodeError()  { return !errors.isEmpty(); }

        /** One-line summary for the test report. */
        public String summary() {
            if (!hadCodeError()) return "Invite code generated first try: " + code;
            return (succeeded()
                    ? "Invite code generated on attempt " + attempts + " (" + code + ")"
                    : "Invite code NEVER generated after " + attempts + " attempts")
                    + " — " + errors.size() + " × code-generation error."
                    + "\n  errors   : " + String.join(" | ", errors)
                    + "\n  diagnosis: " + diagnosis;
        }
    }

    /**
     * Generates the invite code, retrying the share when the app answers with
     * "Error generating the code".
     *
     * <p>The error is intermittent and server-side, so a single failure is not a
     * reason to fail the whole round-trip — but it IS a reason to record that it
     * happened. Each failure is captured with a screenshot, a page-source dump, and a
     * root-cause reading taken while the error is still on screen.</p>
     *
     * <p>Retry strategy: tap an in-place Retry control if the build offers one,
     * otherwise back out and re-run the long-press share from Home. The second is the
     * path that actually exercises on this build, and it also clears any half-made
     * share state the failed attempt left behind.</p>
     *
     * @param deviceName  tile to long-press when a full re-share is needed
     * @param maxAttempts total attempts including the first (values &lt; 1 mean 1)
     */
    public ShareCodeResult obtainInviteCode(String deviceName, int maxAttempts) {
        int limit = Math.max(1, maxAttempts);
        java.util.List<String> errors = new java.util.ArrayList<>();
        String diagnosis = null;

        for (int attempt = 1; attempt <= limit; attempt++) {
            waitForCodeGeneration();

            if (isPresent(SHARE_CODE_ERROR)) {
                String errorText = readErrorText();
                // Diagnose BEFORE any navigation — connectivity and the error copy are
                // only trustworthy while the failing screen is still up.
                diagnosis = diagnoseCodeFailure(errorText);
                errors.add("attempt " + attempt + ": " + errorText);

                System.err.println("[DeviceSharingPage] Code generation FAILED (attempt "
                        + attempt + "/" + limit + "): " + errorText);
                System.err.println("[DeviceSharingPage] Diagnosis: " + diagnosis);
                AppUtil.captureScreenshot(driver, "share_code_error_attempt" + attempt);
                dumpTree("share_code_error_attempt" + attempt);

                if (attempt < limit) {
                    regenerateCode(deviceName, attempt);
                    continue;
                }
                return new ShareCodeResult(null, attempt, errors, diagnosis);
            }

            String code = readInviteCode();
            if (code != null && !code.isBlank()) {
                return new ShareCodeResult(code, attempt, errors, diagnosis);
            }

            // No error banner, but nothing readable either — treat as a soft failure
            // so it still retries instead of handing null to the member phone.
            errors.add("attempt " + attempt + ": no error shown, but no code could be read");
            diagnosis = diagnoseCodeFailure(null);
            if (attempt < limit) {
                regenerateCode(deviceName, attempt);
            }
        }
        return new ShareCodeResult(null, limit, errors, diagnosis);
    }

    /** Convenience overload — three attempts, enough to ride out a transient blip. */
    public ShareCodeResult obtainInviteCode(String deviceName) {
        return obtainInviteCode(deviceName, 3);
    }

    /**
     * Blocks until the "Please wait..." spinner clears, so the screen has settled into
     * either a code or an error before either is read.
     */
    private void waitForCodeGeneration() {
        if (!isPresent(SHARE_CODE_LOADING)) return;
        System.out.println("[DeviceSharingPage] Generating code — waiting for spinner...");
        try {
            new WebDriverWait(driver, Duration.ofSeconds(30))
                    .until(ExpectedConditions.invisibilityOfElementLocated(SHARE_CODE_LOADING));
        } catch (Exception e) {
            System.err.println("[DeviceSharingPage] Spinner still up after 30s — "
                    + "code generation appears hung.");
        }
        ActionsUtil.SSleep(1);
    }

    /** Full text of the error node, for the report. Falls back to the generic copy. */
    private String readErrorText() {
        try {
            String desc = driver.findElement(SHARE_CODE_ERROR).getDomAttribute("content-desc");
            if (desc != null && !desc.isBlank()) return desc.replace("\n", " / ").trim();
        } catch (Exception ignored) {}
        return "Error generating the code";
    }

    /**
     * Best-effort root cause, gathered while the error is still on screen.
     *
     * <p>Appium can only see the device, not the server, so this reports the
     * conditions that plausibly explain the failure rather than asserting one. The
     * checks are ordered most-actionable first, and the phrasing keeps the
     * distinction between "the phone was offline" (our environment) and "connectivity
     * was fine" (a genuine backend fault worth raising with the app team).</p>
     */
    private String diagnoseCodeFailure(String errorText) {
        java.util.List<String> findings = new java.util.ArrayList<>();

        // 1. Connectivity — the single most common cause, and cheap to read.
        try {
            io.appium.java_client.android.connection.ConnectionState net = driver.getConnection();
            boolean wifi = net.isWiFiEnabled();
            boolean data = net.isDataEnabled();
            boolean airplane = net.isAirplaneModeEnabled();
            if (airplane)            findings.add("AIRPLANE MODE is on — device has no network");
            else if (!wifi && !data) findings.add("no network: Wi-Fi and mobile data BOTH off");
            else findings.add("connectivity looks fine (wifi=" + wifi
                        + ", data=" + data + ") — points at a SERVER-side failure");
        } catch (Exception e) {
            findings.add("could not read connection state: " + e.getMessage());
        }

        // 2. Did the spinner never clear? A hang reads differently from a fast reject.
        if (isPresent(SHARE_CODE_LOADING)) {
            findings.add("spinner never cleared — request timed out rather than being refused");
        }

        // 3. Is the app even still foregrounded?
        if (!AppUtil.isAppInForeground(driver)) {
            findings.add("app left the foreground during generation — likely a crash/restart");
        }

        // 4. Anything the error copy itself carries beyond the generic sentence.
        if (errorText != null && errorText.length() > "Error generating the code".length() + 3) {
            findings.add("error detail on screen: \"" + errorText + "\"");
        }

        return String.join("; ", findings);
    }

    /**
     * Clears the failed share and starts a fresh one. Prefers an in-place Retry
     * control; otherwise re-runs the long-press share, which also discards whatever
     * partial state the failed attempt left on screen.
     */
    private void regenerateCode(String deviceName, int failedAttempt) {
        System.out.println("[DeviceSharingPage] Regenerating code after attempt " + failedAttempt + "...");

        if (isPresent(SHARE_CODE_RETRY)) {
            tap(SHARE_CODE_RETRY, "Retry code generation");
            ActionsUtil.SSleep(2);
            return;
        }

        // Back off before re-sharing: hammering a server that just failed tends to
        // fail again, and the delay costs nothing next to a lost round-trip.
        ActionsUtil.SSleep(3);
        shareDeviceFromHomeLongPress(deviceName);
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
    // STATE QUERIES for the sheet-mapped suites (RP / F2 / F3)
    //
    // These return facts and never assert. Keeping the reading here and the verdict in
    // the test is what lets one screen state serve a positive assertion in one case and
    // a negative one in another — "the FAB is present" is required for F3-01 and
    // forbidden for F3-14, and a helper that threw on either could not serve both.
    // ══════════════════════════════════════════════════════════════════════════

    /** Public presence check, for tests that need to read a locator directly. */
    public boolean isElementPresent(By locator) {
        return isPresent(locator);
    }

    /**
     * True when the (+) share FAB is reachable inside the named family.
     *
     * <p>Backs both F3-01 (an admin must have it) and F3-14 (a non-admin must not), so it
     * reports rather than navigates-or-dies: a non-admin may not even reach the family
     * screen, and that is a pass for F3-14, not an error.</p>
     */
    public boolean isShareFabPresent(String familyName) {
        try {
            if (isPresent(familyTile(familyName))) {
                openFamilyHome(familyName);
                ActionsUtil.SSleep(1);
            }
            // FAB_ADD_MEMBER's first branch is a positional path that also matches nodes on
            // the Manage Family LIST screen, so presence there is not evidence. Only trust
            // it once the family tile is gone, i.e. the family really is open.
            boolean stillOnList = isPresent(familyTile(familyName));
            boolean fab = isPresent(FAB_ADD_MEMBER);
            System.out.println("[DeviceSharingPage] Share FAB present=" + fab
                    + " (stillOnFamilyList=" + stillOnList + ")");
            return fab && !stillOnList;
        } catch (Exception e) {
            System.out.println("[DeviceSharingPage] Could not evaluate the share FAB: "
                    + e.getMessage());
            return false;
        }
    }

    /** Which options the share bottom sheet offers — "Share Entire Home", "Share Specific Devices". */
    public java.util.List<String> shareSheetOptions() {
        java.util.List<String> found = new java.util.ArrayList<>();
        if (isPresent(SHARE_ENTIRE_HOME_OPTION))     found.add("Share Entire Home");
        if (isPresent(SHARE_SPECIFIC_DEVICES_OPTION)) found.add("Share Specific Devices");
        if (found.isEmpty()) dumpTree("share_sheet_no_options");
        return found;
    }

    /** Opens the "Share Specific Devices" branch. False when the option is absent. */
    public boolean openSpecificDevicePicker() {
        if (!isPresent(SHARE_SPECIFIC_DEVICES_OPTION)) {
            dumpTree("share_specific_devices_option_missing");
            return false;
        }
        tap(SHARE_SPECIFIC_DEVICES_OPTION, "Share Specific Devices");
        ActionsUtil.SSleep(2);
        AppUtil.captureScreenshot(driver, "specific_device_picker");
        dumpTree("specific_device_picker");
        return true;
    }

    /**
     * Whether the "Share Devices" CTA is enabled, or null when it is not on screen.
     *
     * <p>Three-valued on purpose. F3-12 requires the CTA to be blocked with nothing
     * selected, and the app can satisfy that either by disabling the button or by not
     * rendering it — so "absent" has to be distinguishable from "enabled", which a
     * boolean would flatten into false and score as a pass for the wrong reason.</p>
     */
    public Boolean isShareDevicesButtonEnabled() {
        WebElement el = AppUtil.findOptionalElement(driver, SHARE_DEVICES_BUTTON);
        if (el == null) return null;
        String enabled = el.getDomAttribute("enabled");
        // Android omits enabled="false" on some Flutter nodes; absence means enabled.
        return !"false".equalsIgnoreCase(enabled);
    }

    /** Taps a device's selection control on the specific-device picker. */
    public boolean selectDeviceForShare(String deviceName) {
        By control = deviceSelectControl(deviceName);
        if (!isPresent(control)) {
            dumpTree("device_select_missing_" + AppUtil.sanitizeFileName(deviceName));
            return false;
        }
        tap(control, "Select device: " + deviceName.replace("\n", " / "));
        ActionsUtil.SSleep(1);
        return true;
    }

    /**
     * Every device row on the currently-open member device list.
     *
     * <p>Reads the whole list rather than probing per device name, because F2-03 asserts a
     * COUNT and F2-08 asserts emptiness — neither is answerable by asking "is device X
     * here?". Rows are matched on being clickable with a multi-line content-desc, the
     * "&lt;device&gt;\n&lt;room-or-level&gt;" shape the tiles use elsewhere in the app.</p>
     */
    public java.util.List<String> listMemberDeviceRows() {
        java.util.LinkedHashSet<String> rows = new java.util.LinkedHashSet<>();
        try {
            List<WebElement> candidates = driver.findElements(By.xpath(
                    "//android.widget.ImageView[@content-desc] | //android.view.View[@content-desc"
                            + " and @clickable=\"true\"]"));
            for (WebElement el : candidates) {
                String desc = el.getDomAttribute("content-desc");
                if (desc == null || desc.isBlank()) continue;
                // Drop chrome: tab bar entries and the section headers share the screen.
                if (desc.contains("Tab ") || desc.equals("Devices") || desc.equals("Rooms")
                        || desc.equals("Automations") || desc.startsWith("Add ")) continue;
                rows.add(desc.trim());
            }
        } catch (Exception e) {
            System.err.println("[DeviceSharingPage] Could not list member device rows: "
                    + e.getMessage());
        }
        java.util.List<String> out = new java.util.ArrayList<>(rows);
        System.out.println("[DeviceSharingPage] Member device rows: " + AppUtil.prettyTiles(out));
        return out;
    }

    /** True when the member's device list shows an informative empty state (F2-08). */
    public boolean isMemberDeviceListEmpty() {
        boolean emptyState = isPresent(MEMBER_DEVICE_LIST_EMPTY_STATE);
        if (!emptyState) dumpTree("member_device_list_not_empty_state");
        return emptyState;
    }

    /** Opens a device row inside a member's device list, revealing its permission editor. */
    public boolean openMemberDeviceRow(String deviceName) {
        By row = memberDeviceRow(deviceName);
        if (!isPresent(row)) {
            dumpTree("member_device_row_missing_" + AppUtil.sanitizeFileName(deviceName));
            return false;
        }
        tap(row, "Member device row: " + deviceName.replace("\n", " / "));
        ActionsUtil.SSleep(2);
        return true;
    }

    /**
     * Starts a per-device revoke and reports whether a confirmation dialog appeared
     * BEFORE anything was deleted — the F2-14 requirement — then confirms.
     *
     * @return an outcome distinguishing "no Remove control", "removed without asking"
     *         and "prompted, then removed"
     */
    public RevokeOutcome revokeDeviceFromMember(String deviceName) {
        if (!isPresent(MEMBER_REMOVE_DEVICE_OPTION)
                && !isPresent(DEVICE_REMOVE_ACCESS_OPTION)) {
            dumpTree("revoke_option_missing_" + AppUtil.sanitizeFileName(deviceName));
            return new RevokeOutcome(false, false, false,
                    "no per-device Remove/Revoke control on this screen");
        }
        tapFirstPresent("Remove device access",
                MEMBER_REMOVE_DEVICE_OPTION, DEVICE_REMOVE_ACCESS_OPTION);
        ActionsUtil.SSleep(1);

        // Read the prompt BEFORE confirming — after the tap the dialog is gone and
        // "it asked me" is indistinguishable from "it just deleted it".
        boolean prompted = isPresent(REMOVE_ACCESS_CONFIRM_DIALOG)
                || isPresent(REMOVE_ACCESS_CONFIRM_BUTTON)
                || isPresent(DIALOG_CONFIRM_YES);
        AppUtil.captureScreenshot(driver, "revoke_confirm_dialog");

        boolean confirmed = tapFirstPresent("Confirm revoke",
                REMOVE_ACCESS_CONFIRM_BUTTON, DIALOG_CONFIRM_YES, DIALOG_CONFIRM_OK);
        ActionsUtil.SSleep(2);
        AppUtil.captureScreenshot(driver, "revoke_done_"
                + AppUtil.sanitizeFileName(deviceName));
        return new RevokeOutcome(true, prompted, confirmed,
                prompted ? "confirmation dialog shown before the delete call"
                         : "NO confirmation dialog — the revoke proceeded unprompted");
    }

    /**
     * @param controlFound a Remove/Revoke control existed
     * @param prompted     a confirmation dialog appeared before deletion (F2-14)
     * @param confirmed    the confirmation was accepted
     * @param detail       human-readable reading for the report
     */
    public record RevokeOutcome(boolean controlFound, boolean prompted,
                                boolean confirmed, String detail) {}

    /**
     * Restarts the app on this phone: terminate, relaunch, land on Home.
     *
     * <p>The force-kill half of the stale-cache case (EC-06). The spec's NFR is that
     * permission-sensitive screens re-fetch on resume, and only a cold start proves the
     * client is not serving a cached mask — a background/foreground cycle can be answered
     * from memory.</p>
     */
    public DeviceSharingPage restartApp() {
        try {
            driver.terminateApp(app.resources.AppInfo.ATOMBERG_HOME);
            ActionsUtil.SSleep(2);
            driver.activateApp(app.resources.AppInfo.ATOMBERG_HOME);
            ActionsUtil.SSleep(6);
            returnToHomeScreen();
            AppUtil.captureScreenshot(driver, "admin_app_restarted");
        } catch (Exception e) {
            System.err.println("[DeviceSharingPage] App restart failed: " + e.getMessage());
        }
        return this;
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
            // Remember what we just tapped so an untagged screenshot taken on the
            // screen this opens is still named after the action that produced it.
            AppUtil.noteAction(label);
            System.out.println("[DeviceSharingPage] Tapped: " + label);
        } catch (Exception e) {
            System.err.println("[DeviceSharingPage] Could not tap: " + label + " — " + e.getMessage());
            AppUtil.captureScreenshot(driver, "tap_fail_" + label.replace(" ", "_").toLowerCase());
            dumpTree("tap_fail_" + label.replace(" ", "_").toLowerCase());
            throw new RuntimeException("Tap failed on: " + label, e);
        }
    }

    /**
     * Dumps the current screen's accessibility tree (page source) to
     * {@code test-output/pagesource/<tag>.xml} so the real content-desc values
     * can be read off and the locators corrected. Called automatically whenever
     * a tap fails; can also be called manually at any navigation point.
     */
    public void dumpTree(String tag) {
        try {
            String xml = driver.getPageSource();
            java.nio.file.Path dir = java.nio.file.Path.of(
                    System.getProperty("user.dir", "."), "test-output", "page-source");
            java.nio.file.Files.createDirectories(dir);
            java.nio.file.Path file = dir.resolve(tag.replaceAll("[^a-zA-Z0-9_-]", "_") + ".xml");
            java.nio.file.Files.writeString(file, xml);
            System.out.println("[DeviceSharingPage] Page source dumped: " + file.toAbsolutePath());
        } catch (Exception ex) {
            System.err.println("[DeviceSharingPage] Could not dump page source: " + ex.getMessage());
        }
    }

    private void scrollAndTap(By locator, String label) {
        if (isPresent(locator)) { tap(locator, label); return; }
        for (int i = 0; i < 5; i++) {
            ActionsUtil.Scroll.Up(driver);
            ActionsUtil.SSleep(1);
            if (isPresent(locator)) { tap(locator, label); return; }
        }
        tap(locator, label);  // Let it fail with a clear message
    }

    private boolean isPresent(By locator) {
        try { return driver.findElement(locator).isDisplayed(); }
        catch (Exception e) { return false; }
    }

    /** Page source, or null if it could not be read — used for change detection. */
    private String pageSourceQuietly() {
        try { return driver.getPageSource(); }
        catch (Exception e) { return null; }
    }

    /**
     * Taps the first of the given locators that is present, and returns true.
     * Unlike {@link #tap}, a total miss is NOT fatal — it returns false so callers
     * can treat an optional/absent control (e.g. a confirm button that some app
     * versions skip) gracefully. Dumps the tree once when nothing matched.
     */
    private boolean tapFirstPresent(String label, By... locators) {
        for (By locator : locators) {
            if (isPresent(locator)) {
                tap(locator, label);
                return true;
            }
        }
        dumpTree("none_present_" + label.replace(" ", "_").toLowerCase());
        return false;
    }

    // Overload: millisecond sleep flag
    private void ActionsUtil_SSleep(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}