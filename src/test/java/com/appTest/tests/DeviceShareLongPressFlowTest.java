package com.appTest.tests;

import app.util.AppUtil;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * DeviceShareLongPressFlowTest — end-to-end, TWO-PHONE test for the "Flow 2"
 * device-sharing path:
 *
 * <pre>
 *   ADMIN : Home → long-press a device tile → "Share device"
 *           → "Share access to family?" dialog → Yes
 *           → QR + invite code screen → read the code into a variable
 *   MEMBER: More → Manage family → (+) Add → enter the code → Join
 *           → Home → Switch Family → the admin's family
 *   VERIFY: both phones list the SAME devices
 *   CLEANUP: member leaves the family AND admin removes the member
 * </pre>
 *
 * <p><b>Sharing is family-wide, not per-device.</b> The confirmation dialog the app
 * raises after "Share device" states it outright: access is granted for every device
 * in the family, not the one long-pressed. So this is a single round-trip, not a
 * per-device matrix — the long-pressed tile is only the entry point, and the
 * assertion is dashboard parity across the two phones.</p>
 *
 * <p><b>Permission levels are ON HOLD.</b> Super / Basic / Custom are not in
 * production yet, and the long-press path never presented a picker on the current
 * build. The whole access matrix and its per-capability verification are therefore
 * out of this test. They live on in {@code SharedAccess} / {@code PermissionModel}
 * and the Manage-Family suites, ready to be re-attached here once the APK exposing
 * the levels is installed on both phones.</p>
 */
public class DeviceShareLongPressFlowTest extends BaseDeviceSharingTest {

    /** Family/home name to open on the admin phone — ADMIN_FAMILY_HINT (e.g. "Main Family"). */
    private static final String FAMILY_HOME_HINT =
            app.sharing.SharingConfig.get("ADMIN_FAMILY_HINT", "Main Family");

    /**
     * The devices that must appear on the member's home after joining. Built from the
     * configured TEST_*_DEVICE_NAME values rather than a screen scrape: those names
     * are proven findable by {@code deviceCardOnHome}, whereas the scrape is still
     * unpinned against a real Home dump.
     */
    private final List<String> expectedDevices = new ArrayList<>();

    /** Best-effort scrape of the admin's home tiles — reported, never asserted on. */
    private List<String> adminHomeDevices = new ArrayList<>();

    // ── Pre-flight: all target devices must exist on the admin home ─────────────

    /**
     * Verifies that every configured device (fan required; lock and water purifier
     * when their names are set) is present on the ADMIN home dashboard before the
     * share runs, and records the admin's full tile list as the baseline the member's
     * dashboard is later compared against. Runs after {@code setupDualDevices}
     * (base {@code @BeforeClass}); a miss aborts the class.
     */
    @BeforeClass(dependsOnMethods = "setupDualDevices")
    public void verifyAdminHomeHasAllDevices() {
        reporter.startTest("Flow2 pre-flight – admin home has all devices", "Admin_Device");
        try {
            adminHomeDevices = adminSharingPage.listDevicesOnHome();

            // Ground truth for the tile structure — the scrape and the family-switcher
            // locator are both still guesses at this screen. Dumped every run, not just
            // on failure, so there is always a current capture to pin them against.
            adminSharingPage.dumpTree("admin_devices_home");

            List<String> missing = new ArrayList<>();
            requireOnHome(testDeviceName, missing);                 // fan (required)
            if (isSet(testLockDeviceName))     requireOnHome(testLockDeviceName, missing);
            if (isSet(testPurifierDeviceName)) requireOnHome(testPurifierDeviceName, missing);

            Assert.assertTrue(missing.isEmpty(),
                    "Admin home is missing device tile(s): " + missing
                            + " — add them to the admin's family, or fix the TEST_*_DEVICE_NAME values "
                            + "to match the exact on-screen names.");

            if (adminHomeDevices.isEmpty()) {
                // Not fatal: the parity assertion runs off `expectedDevices`, which is
                // built from names just confirmed present. The scrape only adds breadth.
                reporter.log(Status.WARNING, "Tile scrape returned nothing — parity will cover "
                        + "the " + expectedDevices.size() + " configured device(s) only. "
                        + "Pin AppUtil.listDeviceTilesOnHome against the dumped admin_devices_home.xml.");
            }

            reporter.log(Status.PASS, "All target devices present on admin home. Expecting on member: "
                    + AppUtil.prettyTiles(expectedDevices)
                    + "; scraped tiles: " + AppUtil.prettyTiles(adminHomeDevices));
        } catch (AssertionError | Exception e) {
            AppUtil.captureScreenshot(adminDriver, "flow2lp_preflight_devices_missing");
            reporter.log(Status.FAIL, "Pre-flight device presence check failed: " + e.getMessage());
            throw e;
        } finally {
            reporter.endTest();
        }
    }

    /**
     * Confirms the device is on the admin's home and, when it is, adds it to the set
     * the member's dashboard must reproduce. Only confirmed-present devices become
     * expectations, so a config typo fails here with a clear message instead of
     * surfacing later as a bogus parity mismatch.
     */
    private void requireOnHome(String deviceName, List<String> missing) {
        if (adminSharingPage.isDeviceOnHome(deviceName)) {
            expectedDevices.add(deviceName);
        } else {
            missing.add(deviceName.replace("\n", " / "));
        }
    }

    private static boolean isSet(String s) { return s != null && !s.isBlank(); }

    // ── The round-trip ──────────────────────────────────────────────────────────

    /**
     * Drives the whole flow end to end. The invite code is the hand-off between the
     * two phones — read off the admin's QR screen and held in {@code inviteCode} for
     * the member to type, because Appium cannot drive the camera to scan the QR.
     */
    @Test(description = "Long-press share → Yes → member joins via code → switch family "
            + "→ both phones list the same devices")
    public void shareViaLongPress() {
        reporter.startTest("Flow2 LongPress – family share round-trip", "Admin+Member");
        try {
            // ── ADMIN: long-press the tile → "Share device" → confirmation → Yes ──
            // shareDeviceFromHomeLongPress() answers the "Share access to family?"
            // dialog itself, so the QR screen is up when it returns.
            adminSharingPage.shareDeviceFromHomeLongPress(testDeviceName);

            if (adminSharingPage.isQrCodeVisible()) {
                reporter.log(Status.INFO, "QR code is displayed on the admin phone.");
            } else {
                // Not fatal: the code is the artifact the member actually needs, and
                // the QR image locator is still a candidate rather than a pinned one.
                reporter.log(Status.WARNING, "QR image not detected — continuing on the invite code.");
            }

            // ── The code: read once, stored, carried to the other phone ───────────
            String inviteCode = adminSharingPage.readInviteCode();
            Assert.assertTrue(isSet(inviteCode),
                    "Admin must be able to read/copy the invite code from the share screen "
                            + "— see test-output/page-source/invite_code_not_read.xml for what was on screen.");
            reporter.log(Status.INFO, "Invite code captured: " + inviteCode);
            System.out.println("[Flow2LP] Invite code = " + inviteCode);

            // ── MEMBER: Manage Family → (+) Add → Join an existing smart home ─────
            //            → enter code → Join → "Added to home successfully!"
            memberDevicePage.joinViaManageFamilyAddCode(inviteCode);
            Assert.assertTrue(memberDevicePage.wasJoinConfirmed(),
                    "Member never saw \"Added to home successfully!\" after submitting code '"
                            + inviteCode + "' — the join did not land. See "
                            + "test-output/page-source/member_join_unconfirmed.xml.");
            reporter.log(Status.INFO, "Join confirmed on the member phone.");
            waitForPermissionPropagation();

            // ── MEMBER: Home → Switch Family → the admin's family ─────────────────
            // The join finishes on the More tab, so landing on Home is its own step
            // and its own assertion — the family switcher header exists nowhere else,
            // and a silent failure here previously surfaced as an unfindable switcher.
            Assert.assertTrue(memberDevicePage.returnToHomeScreen(),
                    "Member phone did not return to the Home/Devices dashboard after joining "
                            + "— the family switcher is only reachable from there. See "
                            + "test-output/page-source/member_home_not_reached.xml.");
            reporter.log(Status.INFO, "Member is on the Home dashboard.");

            Assert.assertTrue(memberDevicePage.switchToFamily(FAMILY_HOME_HINT),
                    "Member could not switch to '" + FAMILY_HOME_HINT + "' after joining "
                            + "— the join may not have landed, or the family switcher locator needs "
                            + "pinning (see the dumped page source).");

            // ── VERIFY: both phones show the same devices ─────────────────────────
            memberDevicePage.dumpTree("member_devices_home");
            assertSameDevices();

            reporter.log(Status.PASS, "Both phones list the same devices: "
                    + AppUtil.prettyTiles(expectedDevices));
        } catch (AssertionError | Exception e) {
            AppUtil.captureScreenshot(adminDriver,  "flow2lp_admin_fail");
            AppUtil.captureScreenshot(memberDriver, "flow2lp_member_fail");
            reporter.log(Status.FAIL, e.getMessage());
            throw e;
        } finally {
            cleanup();
            reporter.endTest();
        }
    }

    // ── Verification ─────────────────────────────────────────────────────────────

    /**
     * Asserts dashboard parity: every device confirmed on the admin's home must be
     * reachable on the member's. Each device is looked up with the same
     * {@code deviceCardOnHome} locator the pre-flight used, rather than by diffing
     * two scrapes — the scrape is unpinned, and a locator that already proved itself
     * on the admin phone is the stronger check.
     *
     * <p>The member's scrape is still logged alongside, so any device the member sees
     * that the admin didn't is visible in the output even though it isn't asserted.</p>
     */
    private void assertSameDevices() {
        List<String> memberScrape = memberDevicePage.listDevicesOnHome();

        List<String> missingOnMember = new ArrayList<>();
        for (String device : expectedDevices) {
            if (!memberDevicePage.canSeeDeviceAfterScroll(device)) {
                missingOnMember.add(device.replace("\n", " / "));
            }
        }

        Set<String> extraOnMember = new LinkedHashSet<>(memberScrape);
        extraOnMember.removeAll(new LinkedHashSet<>(adminHomeDevices));

        System.out.println("[Flow2LP] Expected on member: " + AppUtil.prettyTiles(expectedDevices));
        System.out.println("[Flow2LP] Member scrape     : " + AppUtil.prettyTiles(memberScrape));
        if (!extraOnMember.isEmpty()) {
            System.out.println("[Flow2LP] On member but not scraped from admin: "
                    + AppUtil.prettyTiles(extraOnMember)
                    + " — expected while the admin-side scrape is unpinned; not asserted.");
        }

        Assert.assertTrue(missingOnMember.isEmpty(),
                "Devices shared by the admin are missing from the member's home after "
                        + "switching family."
                        + "\n  expected on member : " + AppUtil.prettyTiles(expectedDevices)
                        + "\n  missing            : " + missingOnMember
                        + "\n  member scrape      : " + AppUtil.prettyTiles(memberScrape));
    }

    // ── Revocation: two equivalent paths, one picked per run ─────────────────────

    /** How the member's access is revoked at the end of a run. */
    private enum RevokeMode {
        /** MEMBER: More → Manage Family → open the family → Leave home. */
        MEMBER_LEAVES,
        /** ADMIN: Manage Family → open the family → member row → Remove member. */
        ADMIN_REMOVES
    }

    /**
     * Which revocation path runs — {@code member_leave}, {@code admin_remove}, or
     * {@code random} (default). Set it per run on the command line:
     *
     * <pre>
     *   mvn test "-DsuiteFile=testng-flow2.xml" "-DCLEANUP_MODE=admin_remove"
     *   mvn test "-DsuiteFile=testng-flow2.xml" "-DCLEANUP_MODE=member_leave"
     * </pre>
     *
     * <p>Randomising means a green run doesn't prove both paths work, so the chosen
     * mode is always logged and can be pinned when a specific path needs testing.
     * <b>Pin it to one value before starting the Super/Basic/Custom work</b>: those
     * tests must re-share repeatedly, and a revocation path that varies per run makes
     * a permission mismatch impossible to attribute.</p>
     */
    private static final String CLEANUP_MODE = resolveCleanupMode();

    /**
     * {@code -DCLEANUP_MODE=...} wins over the properties file for this key.
     *
     * <p>{@code SharingConfig.get} deliberately resolves the file FIRST and only then
     * falls back to env/system properties, which is right for machine-specific values
     * like UDIDs — you don't want a stray {@code -D} silently retargeting a phone. It
     * is wrong for a per-run switch: with {@code CLEANUP_MODE} set in the file, a
     * command-line override would be ignored without saying so. Reading the system
     * property first inverts that for this one key only.</p>
     */
    private static String resolveCleanupMode() {
        String cli = System.getProperty("CLEANUP_MODE");
        if (cli != null && !cli.isBlank()) {
            System.out.println("[Flow2LP] CLEANUP_MODE from -D: " + cli.trim());
            return cli.trim().toLowerCase();
        }
        return app.sharing.SharingConfig.get("CLEANUP_MODE", "random").trim().toLowerCase();
    }

    /**
     * Seeded so the choice is reproducible from the logged seed — an unseeded
     * {@code Random} would make a flaky revocation impossible to replay.
     */
    private static final long REVOKE_SEED = Long.getLong("flow2.revokeSeed", System.nanoTime());
    private static final java.util.Random REVOKE_RANDOM = new java.util.Random(REVOKE_SEED);

    private RevokeMode pickRevokeMode() {
        // Singular and plural both accepted: the enum constants read MEMBER_LEAVES /
        // ADMIN_REMOVES, so the plural is the natural thing to type into the properties
        // file even though the documented token is singular.
        switch (CLEANUP_MODE) {
            case "member_leave", "member_leaves" -> {
                return RevokeMode.MEMBER_LEAVES;
            }
            case "admin_remove", "admin_removes" -> {
                return RevokeMode.ADMIN_REMOVES;
            }
            case "random" -> { /* fall through to the coin flip below */ }
            default -> {
                // A typo used to land here and silently randomise, which reads exactly
                // like the pinned path working — say so instead of guessing quietly.
                System.err.println("[Flow2LP] Unrecognised CLEANUP_MODE='" + CLEANUP_MODE
                        + "' — expected member_leave | admin_remove | random. "
                        + "Falling back to random; fix sharing-test.properties if you meant "
                        + "to pin one path.");
                reporter.log(Status.WARNING, "Unrecognised CLEANUP_MODE='" + CLEANUP_MODE
                        + "' — revocation path was randomised, not pinned.");
            }
        }

        RevokeMode picked = REVOKE_RANDOM.nextBoolean()
                ? RevokeMode.MEMBER_LEAVES : RevokeMode.ADMIN_REMOVES;
        System.out.println("[Flow2LP] CLEANUP_MODE=" + CLEANUP_MODE + " → " + picked
                + " (replay with -Dflow2.revokeSeed=" + REVOKE_SEED + ")");
        return picked;
    }

    /**
     * Revokes the member's access by ONE of the two paths — they are alternatives that
     * reach the same end state, so running both would leave the second acting on a
     * family the first already dismantled.
     *
     * <p>Never throws: this runs from a {@code finally}, and a cleanup problem must
     * not replace the real test verdict. Failures are logged and screenshotted.</p>
     */
    private void cleanup() {
        RevokeMode mode = pickRevokeMode();
        reporter.log(Status.INFO, "Revoking access via " + mode + ".");

        try {
            switch (mode) {
                case MEMBER_LEAVES ->
                        memberDevicePage.leaveFamily(FAMILY_HOME_HINT);
                case ADMIN_REMOVES ->
                        adminSharingPage.removeMemberFromFamily(FAMILY_HOME_HINT, testMemberDisplayName);
            }
        } catch (Exception e) {
            System.err.println("[Flow2LP] Cleanup via " + mode + " failed: " + e.getMessage());
            AppUtil.captureScreenshot(
                    mode == RevokeMode.MEMBER_LEAVES ? memberDriver : adminDriver,
                    "flow2lp_cleanup_" + mode.name().toLowerCase() + "_fail");
        }

        refreshBystanderPhone(mode);
    }

    /**
     * Pull-to-refresh the phone that did NOT perform the revocation. Whichever phone
     * drove the change already re-rendered as it navigated; the other one is still
     * showing state from before it happened, and the app does not push the change to
     * it. Leaving that phone stale is what makes the NEXT run start from a dashboard
     * that disagrees with the server.
     *
     * <p>Separate try/catch from the revocation itself: a refresh that fails must not
     * be mistaken for a revocation that failed.</p>
     */
    private void refreshBystanderPhone(RevokeMode mode) {
        try {
            switch (mode) {
                // Member left → the ADMIN's family list still lists them.
                case MEMBER_LEAVES -> adminSharingPage.refreshHomeDashboard();
                // Admin removed them → the MEMBER's home still shows the shared devices.
                case ADMIN_REMOVES -> memberDevicePage.refreshHomeDashboard();
            }
        } catch (Exception e) {
            System.err.println("[Flow2LP] Post-revoke refresh after " + mode
                    + " failed (revocation itself already ran): " + e.getMessage());
        }
    }
}
