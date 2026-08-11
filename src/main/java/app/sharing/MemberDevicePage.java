package app.sharing;

import app.util.ActionsUtil;
import app.util.AppUtil;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static app.resources.Locators.Android.Sharing.SharingLocators.*;
import static app.resources.Locators.Android.AppLocators.MoreTab.MANAGE_FAMILY;
import static app.resources.Locators.Android.AppLocators.MoreTab.LEAVE_HOME;
import static app.resources.Locators.Android.AppLocators.MoreTab.DELETE_HOME;
import static app.resources.Locators.Android.AppLocators.MoreTab.YES_BUTTON;
import static app.resources.Locators.Android.AppLocators.MoreTab.FAMILY_EDIT_ICON;
import static app.resources.Locators.Android.HomeLocators.MORE_TAB;

/**
 * MemberDevicePage – Page Object operated on the MEMBER phone.
 * The member (non-admin) account verifies:
 *   – Devices received via sharing appear on their home screen
 *   – Controls are accessible or blocked based on their permission mask (spec §3)
 *   – Permission-denied state is shown when the relevant bit is not set (spec §6.6)
 *   – Joining via QR code (spec §4, Flow 3, step 3)
 */
public class MemberDevicePage {

    private final AndroidDriver driver;
    private final WebDriverWait wait;
    private static final Duration TIMEOUT = Duration.ofSeconds(15);

    public MemberDevicePage(AndroidDriver memberDriver) {
        this.driver = memberDriver;
        this.wait   = new WebDriverWait(memberDriver, TIMEOUT);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // HOME SCREEN ASSERTIONS
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Refreshes the home screen by navigating away and back.
     * Spec NFR: "All permission-sensitive screens must re-fetch on app resume."
     */
    public MemberDevicePage refreshHomeScreen() {
        returnToHomeScreen();
        return this;
    }

    /**
     * Lands the member phone on the Devices dashboard and reports whether it got
     * there. The join flow finishes on the More tab, so "refresh" has to actively
     * navigate — the previous version tapped a positional
     * {@code ImageView[@index="0"]} and silently swallowed the miss, which left the
     * run on More and made the family switcher unreachable.
     *
     * <p>{@code ensureAppHome} alone is not enough: it stops at any ROOT screen,
     * and every root tab shows the bottom bar it keys on. So the dashboard tab is
     * tapped explicitly and the result checked against {@link #isOnHomeScreen()}.</p>
     *
     * @return true if the dashboard is on screen when this returns
     */
    public boolean returnToHomeScreen() {
        for (int attempt = 1; attempt <= 3; attempt++) {
            AppUtil.ensureAppHome(driver);
            ActionsUtil.SSleep(1);

            if (isOnHomeScreen()) {
                AppUtil.captureScreenshot(driver, "member_home_refreshed");
                return true;
            }

            // The dashboard tab carries the profile NAME ("Member\nTab 2 of 3"), so
            // HOME_TAB matches on the "Tab 2 of 3" suffix rather than a label.
            if (!tapOptional(HOME_TAB, "Home/dashboard tab")) {
                System.out.println("[MemberDevicePage] Dashboard tab not visible (attempt "
                        + attempt + ") — backing out and retrying.");
                try { driver.navigate().back(); } catch (Exception ignored) {}
            }
            ActionsUtil.SSleep(2);

            if (isOnHomeScreen()) {
                AppUtil.captureScreenshot(driver, "member_home_refreshed");
                return true;
            }
        }

        System.err.println("[MemberDevicePage] Could not reach the Home/Devices dashboard.");
        AppUtil.captureScreenshot(driver, "member_home_not_reached");
        dumpTree("member_home_not_reached");
        return false;
    }

    /**
     * Pull-to-refresh the member's Devices dashboard so it reflects server-side
     * changes made since the screen was drawn — chiefly access the admin revoked
     * from the other phone, which the app has no reason to push here on its own.
     *
     * <p>{@code Scroll.Down} is the refresh gesture despite the name: it drags the
     * finger DOWNWARD (y 0.5 → 0.8), the pull-to-refresh direction. {@code Scroll.Up}
     * drags upward to reveal content further down a list, which is why the
     * scroll-to-find helpers use it.</p>
     *
     * <p>Best-effort: a phone that can't reach Home logs and returns rather than
     * throwing, since this runs as a freshness measure, not as the step under test.</p>
     */
    public MemberDevicePage refreshHomeDashboard() {
        if (!returnToHomeScreen()) {
            System.err.println("[MemberDevicePage] Not on Home — skipping dashboard refresh.");
            return this;
        }
        ActionsUtil.Scroll.Down(driver);           // swipe down == pull-to-refresh
        ActionsUtil.SSleep(3);                     // let the refetch settle
        AppUtil.captureScreenshot(driver, "member_home_refreshed_after_revoke");
        System.out.println("[MemberDevicePage] Member dashboard refreshed.");
        return this;
    }

    /**
     * True when the Devices dashboard is on screen, keyed on the
     * "Devices / Rooms / Automations" segment row that exists nowhere else.
     */
    public boolean isOnHomeScreen() {
        boolean home = isPresent(HOME_SCREEN_MARKER);
        System.out.println("[MemberDevicePage] On home screen? " + home);
        return home;
    }

    /**
     * Returns true if the named device card is visible on the member's home screen.
     * A shared device must appear here after the admin completes sharing.
     */
    public boolean canSeeDevice(String deviceName) {
        boolean visible = isPresent(deviceCardOnHome(deviceName));
        return visible;
    }

    /**
     * Returns true if the named device is NOT visible on the member's home screen.
     * Used after an admin removes the member's access (Flow 2, step 3c).
     */
    public boolean cannotSeeDevice(String deviceName) {
        return !canSeeDevice(deviceName);
    }

    /**
     * Like {@link #canSeeDevice(String)} but scrolls the dashboard before giving up,
     * so a device below the fold still counts as visible. Used for the post-join
     * parity check, where a false negative would fail the whole flow.
     *
     * <p>Kept separate from {@code canSeeDevice} on purpose: the negative-assertion
     * callers ({@link #cannotSeeDevice}) must not pay several scrolls each time they
     * confirm an absence.</p>
     */
    public boolean canSeeDeviceAfterScroll(String deviceName) {
        if (canSeeDevice(deviceName)) return true;

        String previous = null;
        for (int i = 0; i < 4; i++) {
            ActionsUtil.Scroll.Up(driver);
            ActionsUtil.SSleep(1);
            if (canSeeDevice(deviceName)) return true;

            String current = pageSourceQuietly();
            if (current != null && current.equals(previous)) break;  // bottom reached
            previous = current;
        }
        return false;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // FAN PERMISSION ASSERTIONS (spec §3.1)
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Opens the shared fan device and returns whether the speed/control UI
     * is interactable. Tests Fan Control (bit 0).
     */
    public boolean canControlFan(String deviceName) {
        openDevice(deviceName);
        ActionsUtil.SSleep(2);
        boolean controlVisible = isPresent(FAN_SPEED_CONTROL);
        boolean denied = isPermissionDeniedShown();
        AppUtil.captureScreenshot(driver, "member_fan_control_check");
        driver.navigate().back();
        return controlVisible && !denied;
    }

    /**
     * Opens the fan and checks if the Edit/rename button is accessible.
     * Tests Fan Edit Device (bit 1).
     */
    public boolean canEditFanDevice(String deviceName) {
        openDevice(deviceName);
        ActionsUtil.SSleep(2);
        boolean editVisible = isPresent(FAN_EDIT_BUTTON);
        AppUtil.captureScreenshot(driver, "member_fan_edit_check");
        driver.navigate().back();
        return editVisible;
    }

    /**
     * Opens the fan and checks if the Analytics tab is visible.
     * Tests Fan View Analytics (bit 2).
     */
    public boolean canViewFanAnalytics(String deviceName) {
        openDevice(deviceName);
        ActionsUtil.SSleep(2);
        boolean analyticsVisible = isPresent(FAN_ANALYTICS_TAB);
        AppUtil.captureScreenshot(driver, "member_fan_analytics_check");
        driver.navigate().back();
        return analyticsVisible;
    }

    /**
     * Verifies that a fan with Super permission shows ALL capability controls.
     */
    public boolean hasFullFanAccess(String deviceName) {
        openDevice(deviceName);
        ActionsUtil.SSleep(2);

        boolean control   = isPresent(FAN_SPEED_CONTROL);
        boolean edit      = isPresent(FAN_EDIT_BUTTON);
        boolean analytics = isPresent(FAN_ANALYTICS_TAB);
        boolean denied    = isPermissionDeniedShown();

        AppUtil.captureScreenshot(driver, "member_fan_full_access_check");
        driver.navigate().back();

        System.out.println("[MemberDevicePage] Fan full access — "
                + "control=" + control + " edit=" + edit
                + " analytics=" + analytics + " denied=" + denied);

        return control && edit && analytics && !denied;
    }

    /**
     * Verifies that a fan with Basic permission (bit 0 only) shows
     * Control UI but NOT Edit or Analytics (spec §3.1 Basic Default column).
     */
    public boolean hasBasicFanAccess(String deviceName) {
        openDevice(deviceName);
        ActionsUtil.SSleep(2);

        boolean control   = isPresent(FAN_SPEED_CONTROL);
        boolean edit      = isPresent(FAN_EDIT_BUTTON);
        boolean analytics = isPresent(FAN_ANALYTICS_TAB);

        AppUtil.captureScreenshot(driver, "member_fan_basic_access_check");
        driver.navigate().back();

        System.out.println("[MemberDevicePage] Fan basic access — "
                + "control=" + control + " edit=" + edit + " analytics=" + analytics);

        return control && !edit && !analytics;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // GENERAL PERMISSION DENIED CHECK
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Checks if a permission-denied indicator is currently visible on screen.
     * Spec §6.6: "Client reads bitmask, detects bit is not set, shows permission-denied state."
     */
    public boolean isPermissionDeniedShown() {
        return isPresent(PERMISSION_DENIED_INDICATOR) || isPresent(PERMISSION_DENIED_SNACKBAR);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // QR JOIN FLOW (spec §4, Flow 3, step 3 — member side)
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Taps "Scan QR" and confirms joining the shared home/device.
     * After this, the member's home screen should show the shared devices.
     * NOTE: This method navigates to the scan QR screen. The actual camera
     * scanning of the QR is outside test automation scope — in real test
     * execution the QR payload URL is usually deep-linked directly.
     */
    public MemberDevicePage tapScanQR() {
        tap(QR_SCAN_BUTTON, "Scan QR button");
        ActionsUtil.SSleep(2);
        AppUtil.captureScreenshot(driver, "member_scan_qr_screen");
        return this;
    }

    /**
     * Navigates the member phone to the "Join family / Scan QR" screen so an
     * invite code can be entered. Path: More tab → Manage family → Join family.
     * Locators are best-effort with fallbacks, matching the app's Flutter
     * content-desc convention.
     */
    public MemberDevicePage openJoinFamilyScreen() {
        By moreTab = By.xpath("//android.view.View[@content-desc=\"More\"]");
        By joinFamily = By.xpath("//android.view.View[@content-desc=\"Join family\" "
                + "or @content-desc=\"Join a family\" "
                + "or @content-desc=\"Join Family\"]");
        try {
            if (isPresent(moreTab)) { driver.findElement(moreTab).click(); ActionsUtil.SSleep(1); }
            if (isPresent(joinFamily)) { driver.findElement(joinFamily).click(); ActionsUtil.SSleep(2); }
            else if (isPresent(QR_SCAN_BUTTON)) { /* already on a screen exposing Scan QR */ }
        } catch (Exception e) {
            System.err.println("[MemberDevicePage] openJoinFamilyScreen best-effort nav: " + e.getMessage());
        }
        AppUtil.captureScreenshot(driver, "member_join_family_screen");
        return this;
    }

    /**
     * Joins a shared home/device using the invite CODE copied from the admin
     * phone — the camera-less path required because Appium cannot drive the QR
     * scanner. Navigates the member's join flow to the "Enter code" entry, sets
     * the code on the member-device clipboard, pastes/types it, and submits.
     * After this the member's home screen should list the shared devices at the
     * pre-configured permissions (spec §4, Flow 3, step 3 → /join_family_v2).
     *
     * @param inviteCode the code read from the admin phone via
     *                   {@link DeviceSharingPage#readInviteCode()}
     */
    public MemberDevicePage joinViaCode(String inviteCode) {
        if (inviteCode == null || inviteCode.isBlank())
            throw new IllegalArgumentException("Invite code is null/blank — admin readInviteCode() failed.");

        // Open the "Enter code" entry (member is already on the Scan QR / join screen)
        if (isPresent(JOIN_ENTER_CODE_OPTION)) {
            tap(JOIN_ENTER_CODE_OPTION, "Enter code option");
            ActionsUtil.SSleep(1);
        }

        // Pre-load the member clipboard so a long-press paste also works, then type.
        try { driver.setClipboardText(inviteCode); } catch (Exception ignored) {}

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(JOIN_CODE_INPUT));
            java.util.List<WebElement> fields = driver.findElements(JOIN_CODE_INPUT);

            if (fields.size() > 1 && fields.size() >= inviteCode.length()) {
                // Segmented / OTP-style entry: one box per character. Typing the whole
                // code into the first box only lands its first character on inputs that
                // don't auto-advance, so each field is filled individually.
                for (int i = 0; i < inviteCode.length(); i++) {
                    WebElement box = fields.get(i);
                    box.click();
                    box.sendKeys(String.valueOf(inviteCode.charAt(i)));
                }
                System.out.println("[MemberDevicePage] Entered invite code across "
                        + inviteCode.length() + " boxes: " + inviteCode);
            } else {
                WebElement input = fields.get(0);
                input.click();
                input.clear();
                input.sendKeys(inviteCode);
                System.out.println("[MemberDevicePage] Entered invite code: " + inviteCode);
            }
        } catch (Exception e) {
            AppUtil.captureScreenshot(driver, "member_enter_code_fail");
            dumpTree("member_enter_code_fail");
            throw new RuntimeException("[MemberDevicePage] Could not enter invite code", e);
        }

        tap(JOIN_CODE_SUBMIT_BUTTON, "Join/Submit code button");

        // Poll for the confirmation straight away — no sleep first. "Added to home
        // successfully!" is a transient banner, and once it clears there is no way to
        // tell a missed toast from a join that never happened.
        joinConfirmed = false;
        try {
            new WebDriverWait(driver, Duration.ofSeconds(12))
                    .until(ExpectedConditions.presenceOfElementLocated(JOIN_SUCCESS_MESSAGE));
            joinConfirmed = true;
            System.out.println("[MemberDevicePage] Join confirmed: \"Added to home successfully!\"");
            AppUtil.captureScreenshot(driver, "member_join_success_popup");
        } catch (Exception e) {
            System.err.println("[MemberDevicePage] Join confirmation banner not seen within 12s.");
            AppUtil.captureScreenshot(driver, "member_join_unconfirmed");
            dumpTree("member_join_unconfirmed");
        }

        ActionsUtil.SSleep(3);
        AppUtil.captureScreenshot(driver, "member_joined_via_code");
        return this;
    }

    /** Whether the last {@link #joinViaCode} saw "Added to home successfully!". */
    private boolean joinConfirmed;

    /**
     * True when the most recent join showed the success banner. Exposed rather than
     * asserted in-page so the test owns the verdict and can report it, matching
     * {@code DeviceSharingPage.wasPermissionLevelApplied()}.
     */
    public boolean wasJoinConfirmed() {
        return joinConfirmed;
    }

    /**
     * Simulates joining via a QR code deep-link URL.
     * In CI this replaces camera scanning.
     *
     * @param joinUrl The /join_family_v2 deep-link URL embedded in the QR payload
     */
    public MemberDevicePage joinViaDeepLink(String joinUrl) {
        // Open the join URL via adb intent (Appium executeScript / adb)
        try {
            driver.executeScript("mobile: shell", java.util.Map.of(
                    "command", "am",
                    "args", java.util.List.of(
                            "start", "-a", "android.intent.action.VIEW",
                            "-d", joinUrl, "com.atomberg.app"
                    )
            ));
            ActionsUtil.SSleep(3);
            AppUtil.captureScreenshot(driver, "member_joined_via_deeplink");
        } catch (Exception e) {
            System.err.println("[MemberDevicePage] Deep-link join failed: " + e.getMessage());
        }
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // FLOW 2 (member) — join via Manage Family → (+) Add → Enter code
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Flow 2 member-side join: More tab → Manage Family → (+) Add →
     * "Join an existing smart home" → Enter code.
     *
     * <p>The (+) FAB opens a sheet offering both "create a new home" and "join an
     * existing" — the join option must be picked before any code field exists. This
     * is a required step, not an optional one: skipping it left the run sitting on
     * the sheet until the code-field wait timed out.</p>
     *
     * <p>Once the code entry is reached this delegates to {@link #joinViaCode(String)}
     * for the clipboard/type/submit sequence.</p>
     *
     * @param inviteCode the code read from the admin's share/QR screen
     */
    public MemberDevicePage joinViaManageFamilyAddCode(String inviteCode) {
        tapOptional(MORE_TAB, "More tab");
        ActionsUtil.SSleep(1);
        scrollAndTap(MANAGE_FAMILY, "Manage Family");
        ActionsUtil.SSleep(1);
        tapOptional(MEMBER_ADD_FAB, "Add (FAB)");
        ActionsUtil.SSleep(2);
        AppUtil.captureScreenshot(driver, "member_add_family_sheet");

        if (!tapOptional(JOIN_EXISTING_HOME_OPTION, "Join an existing smart home")) {
            // Dump before failing: the sheet's labels are the one part of this path
            // never captured on-device, so a miss should hand over the real tree.
            dumpTree("member_add_family_sheet");
            throw new RuntimeException("[MemberDevicePage] 'Join an existing smart home' not found "
                    + "on the Add-family sheet — see test-output/page-source/member_add_family_sheet.xml");
        }
        ActionsUtil.SSleep(2);
        AppUtil.captureScreenshot(driver, "member_join_existing_home");

        return joinViaCode(inviteCode);
    }

    /**
     * Presence + permission-level badge verification (Flow 2). Returns the badge
     * text ("Super"/"Basic"/"Custom") shown next to the device row, or null if the
     * device/badge is not found.
     */
    public String getDevicePermissionBadge(String deviceName) {
        try {
            WebElement badge = driver.findElement(permissionBadgeFor(deviceName));
            return badge.getDomAttribute("content-desc");
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * True when the device is visible AND its badge contains the expected level
     * label (case-insensitive). Used as the Lock/WP verification until per-capability
     * checkers land.
     */
    public boolean badgeMatches(String deviceName, String expectedLevelLabel) {
        if (!canSeeDevice(deviceName)) return false;
        String badge = getDevicePermissionBadge(deviceName);
        boolean ok = badge != null && expectedLevelLabel != null
                && badge.toLowerCase().contains(expectedLevelLabel.toLowerCase());
        System.out.println("[MemberDevicePage] Badge for " + deviceName + " = '" + badge
                + "' (expected contains '" + expectedLevelLabel + "') → " + ok);
        return ok;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // FLOW 2 (cleanup) — member leaves the joined family
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Flow 2 cleanup on the member phone: More → Manage Family → open the joined
     * family → Leave. Reuses the existing {@code MoreTab} family-management locators.
     * A member cannot delete an owner's home, so "Leave home" is preferred; "Delete
     * home" is tried only as a fallback. Confirmation is via the "Yes" dialog button.
     *
     * @param homeHint substring of the joined family/home name
     */
    public MemberDevicePage leaveFamily(String homeHint) {
        // Cleanup runs from a `finally`, so the phone can be stranded anywhere the
        // test failed — mid-join sheet, a dialog, a device detail. Without this the
        // More tab isn't on screen, every locator misses, and the scroll-and-retry
        // helpers grind through their full retry budget before giving up.
        AppUtil.ensureAppHome(driver);
        ActionsUtil.SSleep(1);

        tapOptional(MORE_TAB, "More tab");
        ActionsUtil.SSleep(1);
        scrollAndTap(MANAGE_FAMILY, "Manage Family");
        ActionsUtil.SSleep(1);

        // Open the family, then Leave — "Leave home" lives inside an opened family.
        // familyTile() first: the Manage-Family list tile is an ImageView whose
        // content-desc is "<memberCount>\n<familyName>" ("1\nMain Family"), so the
        // View-only locator below can never match it. Kept as a fallback for builds
        // that render the row differently.
        By homeRow = By.xpath("//android.view.View[contains(@content-desc, \"" + homeHint + "\")]");
        if (!tapOptional(familyTile(homeHint), "Family tile: " + homeHint)
                && !tapOptional(homeRow, "Family row: " + homeHint)) {
            tapOptional(FAMILY_EDIT_ICON, "Family edit icon");
        }
        ActionsUtil.SSleep(2);

        // Opening the family does not surface "Leave home" — it sits behind the
        // family's options menu, so that has to be tapped first. Skipping it is why
        // every leave step logged "Not present (skipping)" and the member stayed in
        // the family. Only tapped when "Leave home" isn't already visible, so a build
        // that shows the action inline doesn't get a stray tap.
        if (!isPresent(LEAVE_HOME)) {
            // Dump before the menu tap: FAMILY_OPTIONS_MENU falls back to an absolute
            // path with no content-desc to anchor on, and this capture is what lets it
            // be replaced with a stable locator.
            dumpTree("member_family_opened");
            tapOptional(FAMILY_OPTIONS_MENU, "Family options menu");
            ActionsUtil.SSleep(1);
        }

        if (!tapOptional(LEAVE_HOME, "Leave home")) {
            if (!tapOptional(DELETE_HOME, "Delete home (fallback)")) {
                // Neither action reachable — the menu tap missed, or the absolute path
                // has drifted. Hand over the tree rather than reporting a silent no-op.
                System.err.println("[MemberDevicePage] Neither 'Leave home' nor 'Delete home' "
                        + "found after opening the family options menu — the member is still "
                        + "in '" + homeHint + "'.");
                dumpTree("member_leave_home_missing");
                AppUtil.captureScreenshot(driver, "member_leave_home_missing");
            }
        }
        ActionsUtil.SSleep(1);
        tapOptional(YES_BUTTON, "Confirm leave/delete");
        ActionsUtil.SSleep(2);
        AppUtil.captureScreenshot(driver, "member_left_family");
        return this;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // Lock / Water-Purifier per-capability checks — STUBS (presence+badge for now)
    //
    // TODO: implement real control-visibility assertions once the on-device
    // content-desc values for these screens are captured (via a page-source dump).
    // Until then Flow 2 verifies Lock/WP by presence + permission-level badge only,
    // and these stubs are intentionally NOT asserted by the tests.
    // ══════════════════════════════════════════════════════════════════════════

    public boolean canUnlockLock(String deviceName)          { return capabilityStub("Lock Unlock"); }
    public boolean canViewLockHistory(String deviceName)     { return capabilityStub("Lock View History"); }
    public boolean canViewLockSettings(String deviceName)    { return capabilityStub("Lock View Settings"); }
    public boolean canRunPurifierDiagnostics(String name)    { return capabilityStub("WP Run Diagnostics"); }
    public boolean canChangePurifierMode(String name)        { return capabilityStub("WP Mode Change"); }
    public boolean canViewPurifierAnalytics(String name)     { return capabilityStub("WP View Analytics"); }

    private boolean capabilityStub(String capability) {
        System.out.println("[MemberDevicePage] TODO capability check not yet implemented: "
                + capability + " — Flow 2 relies on presence + badge for now.");
        return false;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // FAMILY SWITCHER — after joining, move to the admin's family and compare
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Switches the member phone to the named family via the Home-screen family
     * header. Joining does not change the active family, so the shared devices are
     * only on screen once this has run.
     *
     * <p>No-op when the header already shows {@code familyName}. Because the
     * switcher sheet's node labels vary, the family row is matched by name with a
     * scroll-and-retry, and the tree is dumped when it can't be found so the
     * locator can be pinned from a real capture.</p>
     *
     * @param familyName the family/home to switch to (the admin's family)
     * @return true if the member ended up on {@code familyName}
     */
    public boolean switchToFamily(String familyName) {
        // The switcher header lives only on the dashboard, so being on Home is a
        // precondition, not a nicety — reaching this from the More tab is what made
        // the trigger unfindable.
        if (!returnToHomeScreen()) {
            System.err.println("[MemberDevicePage] Not on Home — cannot open the family switcher.");
            return false;
        }

        if (isPresent(familySwitcherHeader(familyName))) {
            // Header already shows the target — but it is also the control that
            // OPENS the switcher, so presence alone isn't proof we're on it.
            // Opening and re-selecting is idempotent and costs one tap.
            System.out.println("[MemberDevicePage] Family header already shows '" + familyName + "'.");
        }

        if (!tapOptional(FAMILY_SWITCHER_TRIGGER, "Family switcher header")) {
            System.err.println("[MemberDevicePage] Could not open the family switcher.");
            dumpTree("member_family_switcher_not_found");
            return false;
        }
        ActionsUtil.SSleep(2);
        AppUtil.captureScreenshot(driver, "member_family_switcher_open");

        By option = familySwitcherOption(familyName);
        for (int i = 0; i < 4; i++) {
            if (tapOptional(option, "Family option: " + familyName)) {
                ActionsUtil.SSleep(3);
                AppUtil.captureScreenshot(driver, "member_switched_to_" + familyName.replace(" ", "_"));
                return true;
            }
            ActionsUtil.Scroll.Up(driver);
            ActionsUtil.SSleep(1);
        }

        System.err.println("[MemberDevicePage] Family '" + familyName + "' not in the switcher list.");
        dumpTree("member_family_option_missing");
        AppUtil.captureScreenshot(driver, "member_family_option_missing");
        return false;
    }

    /**
     * Scrapes the member's Home dashboard for every device tile, scrolling so tiles
     * below the fold are included. Compared against the admin's list to assert both
     * phones show the same devices after the join.
     *
     * @return tile content-descs ("&lt;device&gt;\n&lt;room&gt;"), de-duplicated
     */
    public java.util.List<String> listDevicesOnHome() {
        returnToHomeScreen();

        java.util.LinkedHashSet<String> all =
                new java.util.LinkedHashSet<>(AppUtil.listDeviceTilesOnHome(driver));
        String previous = null;
        for (int i = 0; i < 6; i++) {
            String current = pageSourceQuietly();
            if (current != null && current.equals(previous)) break;
            previous = current;

            ActionsUtil.Scroll.Up(driver);
            ActionsUtil.SSleep(1);
            all.addAll(AppUtil.listDeviceTilesOnHome(driver));
        }

        java.util.List<String> tiles = new java.util.ArrayList<>(all);
        System.out.println("[MemberDevicePage] Member home devices: " + AppUtil.prettyTiles(tiles));
        return tiles;
    }

    /**
     * Dumps the member screen's accessibility tree to
     * {@code test-output/page-source/<tag>.xml}, mirroring
     * {@link DeviceSharingPage#dumpTree(String)} on the admin side.
     */
    public void dumpTree(String tag) {
        try {
            String xml = driver.getPageSource();
            java.nio.file.Path dir = java.nio.file.Path.of(
                    System.getProperty("user.dir", "."), "test-output", "page-source");
            java.nio.file.Files.createDirectories(dir);
            java.nio.file.Path file = dir.resolve(
                    AppUtil.sanitizeFileName(tag) + ".xml");
            java.nio.file.Files.writeString(file, xml);
            System.out.println("[MemberDevicePage] Page source dumped: " + file.toAbsolutePath());
        } catch (Exception ex) {
            System.err.println("[MemberDevicePage] Could not dump page source: " + ex.getMessage());
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // PRIVATE HELPERS
    // ══════════════════════════════════════════════════════════════════════════

    /** Page source, or null if it could not be read — used for scroll change detection. */
    private String pageSourceQuietly() {
        try { return driver.getPageSource(); }
        catch (Exception e) { return null; }
    }

    private void openDevice(String deviceName) {
        try {
            WebElement card = wait.until(
                    ExpectedConditions.elementToBeClickable(deviceCardOnHome(deviceName)));
            card.click();
            System.out.println("[MemberDevicePage] Opened device: " + deviceName);
        } catch (Exception e) {
            AppUtil.captureScreenshot(driver, "open_device_fail");
            throw new RuntimeException("[MemberDevicePage] Could not open device: " + deviceName, e);
        }
    }

    private void tap(By locator, String label) {
        try {
            WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
            el.click();
            // Name any untagged screenshot on the next screen after this action.
            AppUtil.noteAction(label);
            System.out.println("[MemberDevicePage] Tapped: " + label);
        } catch (Exception e) {
            AppUtil.captureScreenshot(driver, "tap_fail_member_" + AppUtil.sanitizeFileName(label));
            throw new RuntimeException("[MemberDevicePage] Tap failed: " + label, e);
        }
    }

    private boolean isPresent(By locator) {
        try { return driver.findElement(locator).isDisplayed(); }
        catch (Exception e) { return false; }
    }

    /**
     * Taps a locator if it is present, returning whether it was tapped. Non-fatal
     * when absent — used for optional nav steps (menus/dialogs that some app
     * versions skip) in the Flow 2 join and cleanup paths.
     */
    private boolean tapOptional(By locator, String label) {
        if (!isPresent(locator)) {
            System.out.println("[MemberDevicePage] Not present (skipping): " + label);
            return false;
        }
        try {
            driver.findElement(locator).click();
            AppUtil.noteAction(label);
                System.out.println("[MemberDevicePage] Tapped: " + label);
            return true;
        } catch (Exception e) {
            System.err.println("[MemberDevicePage] Optional tap failed: " + label + " — " + e.getMessage());
            return false;
        }
    }

    /** Scrolls up to a few times looking for {@code locator}, then taps it. */
    private void scrollAndTap(By locator, String label) {
        if (tapOptional(locator, label)) return;
        for (int i = 0; i < 5; i++) {
            ActionsUtil.Scroll.Up(driver);
            ActionsUtil.SSleep(1);
            if (tapOptional(locator, label)) return;
        }
        // Final attempt via the strict tap so failures surface clearly.
        tap(locator, label);
    }
}