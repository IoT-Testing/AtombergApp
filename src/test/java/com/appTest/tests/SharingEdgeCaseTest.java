package com.appTest.tests;

import app.sharing.PermissionEditorPage;
import app.sharing.PermissionModel.PermissionLevel;
import app.sharing.SharedAccess;
import app.sharing.SharingConfig;
import app.util.AppUtil;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * SharingEdgeCaseTest – the <b>Edge Cases</b> sheet (EC-01 … EC-14): the boundary,
 * negative and failure scenarios of device sharing.
 *
 * <h2>What is here and what is not</h2>
 * This sheet is the one where the two-phone bench runs out of room most often, and for
 * reasons worth naming rather than glossing:
 * <ul>
 *   <li><b>Homes the bench does not have</b> — a single-user home (EC-01), an all-admin
 *       home (EC-02), a 50-member home (EC-05), an empty home. Reconfiguring the bench's
 *       family to create one would break every other suite.</li>
 *   <li><b>Fixtures the bench cannot make</b> — a device reporting an unknown model
 *       string (EC-03), a firmware/backend fixture.</li>
 *   <li><b>Fault injection</b> — failing one specific request mid-flight (EC-07). Appium
 *       can cut the radio, which fails the call before it is sent; that is a different
 *       scenario with a different expected result.</li>
 *   <li><b>A second admin</b> — concurrent edits (EC-08), delete-while-sharing (EC-09).</li>
 * </ul>
 *
 * <p>Four cases <em>are</em> automated, and they are the four that matter most for
 * security: a revoked permission must not survive a cold restart (EC-06), removing a
 * member must take every shared device with it (EC-10), the widest mask must not overflow
 * its integer (EC-11, asserted offline), and the capability labels must not be raw
 * placeholder keys (EC-14).</p>
 */
public class SharingEdgeCaseTest extends BaseDeviceSharingTest {

    private static final String FAMILY_HOME_HINT =
            SharingConfig.get("ADMIN_FAMILY_HINT", "Main Family");

    // ══════════════════════════════════════════════════════════════════════════════
    // EC-06 — a downgraded permission does not survive a force-kill
    // ══════════════════════════════════════════════════════════════════════════════

    @Test(priority = 1, description = "EC-06: after the admin downgrades Super → Basic, a "
            + "force-killed and reopened app must not still allow Super-only actions")
    public void stalePermissionCacheIsNotHonoured() {
        runCase("EC-06", "Permission check with stale cache", "Admin+Member", () -> {
            requirePermissionLevels("Downgrading Super to Basic to test the permission cache");

            // Establish Super and CONFIRM it. Without this the downgrade proves nothing —
            // "the member cannot do Super things" is unremarkable if they never could.
            applyFanAccessOrSkip(SharedAccess.superAccess(SharedAccess.DeviceType.FAN));
            waitForPermissionPropagation();
            memberDevicePage.refreshHomeDashboard();
            Assert.assertTrue(memberDevicePage.hasFullFanAccess(testDeviceName),
                    "Precondition for EC-06: the member must actually hold Super before it is "
                            + "taken away.");

            // Downgrade from the admin phone.
            applyFanAccessOrSkip(SharedAccess.basicAccess(SharedAccess.DeviceType.FAN));
            waitForPermissionPropagation();

            // Force-kill and cold-start the member's app. A cold start is what makes this
            // meaningful: a resumed process can serve the old mask from memory, so only a
            // fresh one proves the client re-fetches on open (the spec's NFR).
            reporter.log(Status.INFO, "Force-killing and reopening the app on the member phone.");
            memberDevicePage.restartApp();
            waitForPermissionPropagation();

            Assert.assertFalse(memberDevicePage.canEditFanDevice(testDeviceName),
                    "After a downgrade to Basic, Edit Device (bit 1) must be denied on a freshly "
                            + "started app (EC-06). It is still available, which means the "
                            + "client is honouring a cached permission mask — a revoked "
                            + "capability that survives a restart is a live privilege the admin "
                            + "believes they removed.");
            Assert.assertFalse(memberDevicePage.canViewFanAnalytics(testDeviceName),
                    "After a downgrade to Basic, View Analytics (bit 2) must be denied on a "
                            + "freshly started app (EC-06).");
            Assert.assertTrue(memberDevicePage.canControlFan(testDeviceName),
                    "Basic still grants Control (bit 0) — the downgrade must not revoke more "
                            + "than it should (EC-06).");
        });
    }

    // ══════════════════════════════════════════════════════════════════════════════
    // EC-10 — removing a member takes every device they were given
    // ══════════════════════════════════════════════════════════════════════════════

    @Test(priority = 2, description = "EC-10: removing a member from the home revokes every "
            + "device that was shared with them")
    public void removingMemberRevokesAllDevices() {
        runCase("EC-10", "User deleted from home while device shared to them",
                "Admin+Member", () -> {
            // Precondition: the member must currently hold devices, or there is nothing to
            // revoke and the assertion would pass vacuously.
            memberDevicePage.refreshHomeDashboard();
            List<String> heldBefore = new ArrayList<>();
            for (String device : configuredDevices()) {
                if (memberDevicePage.canSeeDeviceAfterScroll(device)) {
                    heldBefore.add(device);
                }
            }
            if (heldBefore.isEmpty()) {
                throw new SkipException("The member currently holds none of the configured "
                        + "devices, so removing them would revoke nothing and the assertion "
                        + "would pass without testing anything. Share first — run "
                        + "SharingQrShareTest or DeviceShareLongPressFlowTest — then re-run.");
            }
            reporter.log(Status.INFO, "Member holds before removal: "
                    + AppUtil.prettyTiles(heldBefore));

            adminSharingPage.removeMemberFromFamily(FAMILY_HOME_HINT, testMemberDisplayName);
            waitForPermissionPropagation();

            // Pull-to-refresh on the member phone: the app does not push a revocation, so a
            // stale dashboard would show the devices still there and fail for the wrong reason.
            memberDevicePage.refreshHomeDashboard();

            List<String> stillVisible = new ArrayList<>();
            for (String device : heldBefore) {
                if (memberDevicePage.canSeeDeviceAfterScroll(device)) {
                    stillVisible.add(device.replace("\n", " / "));
                }
            }
            Assert.assertTrue(stillVisible.isEmpty(),
                    "Removing a member from the home must revoke every device shared with them "
                            + "(EC-10). Still visible after removal: " + stillVisible
                            + ". A device that outlives the membership is access nobody can see "
                            + "to revoke — it no longer appears in any member list.");
        });
    }

    // ══════════════════════════════════════════════════════════════════════════════
    // EC-14 — capability labels are real strings, not placeholder keys
    // ══════════════════════════════════════════════════════════════════════════════

    @Test(priority = 3, description = "EC-14: permission labels are localised strings, with no "
            + "untranslated placeholder keys")
    public void permissionLabelsAreNotPlaceholders() {
        runCase("EC-14", "Language / locale – permission labels", "Admin_Device", () -> {
            requirePermissionLevels("Enumerating the capability labels in the permission editor");

            openFanPermissionEditorOrSkip();
            Assert.assertTrue(permissionEditor.selectLevel(PermissionLevel.CUSTOM),
                    "Custom must be selectable to read the capability labels (EC-14).");
            permissionEditor.discover("ec14_capability_labels");

            List<String> labels = permissionEditor.capabilityLabelsOnScreen();
            reporter.log(Status.INFO, "Capability labels on screen: " + labels);
            Assert.assertFalse(labels.isEmpty(),
                    "No capability labels could be read (EC-14) — see test-output/page-source/"
                            + "permission_editor_ec14_capability_labels.xml.");

            // A full translation check needs the app's own string bundles as the oracle, which
            // a black-box test does not have. What it CAN prove is that nothing fell through
            // to a raw key — the failure mode that actually ships, and that reads as gibberish
            // in every locale including English.
            List<String> suspicious = new ArrayList<>();
            for (String label : labels) {
                if (looksLikeAPlaceholderKey(label)) suspicious.add(label);
            }
            Assert.assertTrue(suspicious.isEmpty(),
                    "These capability labels look like untranslated resource keys rather than "
                            + "display strings: " + suspicious + " (EC-14). All labels read: "
                            + labels + ". Verifying the translations themselves per locale needs "
                            + "the app's string bundles and stays manual.");

            List<String> expected = PermissionEditorPage.expectedCapabilityLabels(
                    SharedAccess.DeviceType.FAN);
            reporter.log(Status.INFO, "Expected (spec §3.1, English): " + expected
                    + ". Run this on a non-English locale to check the labels change — this "
                    + "assertion only proves they are display strings, not that they are "
                    + "correctly translated.");
        });
    }

    // ══════════════════════════════════════════════════════════════════════════════
    // EC-04 — a very long device name does not break the layout
    // ══════════════════════════════════════════════════════════════════════════════

    @Test(priority = 4, description = "EC-04: a device with a 200+ character name renders "
            + "truncated, without overflow or a crash")
    public void veryLongDeviceNameIsHandled() {
        runCase("EC-04", "Very long device name", "Member_Device", () -> {
            String longNameDevice = requireTestData("TEST_LONG_NAME_DEVICE",
                    SharingConfig.get("TEST_LONG_NAME_DEVICE", null),
                    "a device whose display name exceeds 200 characters");

            // Deliberately does NOT rename a device itself. A failure part-way through a
            // rename would leave the bench's device carrying a 200-character name, and every
            // other test looks that device up by name — the cleanup risk outweighs the
            // convenience of self-provisioning this fixture.
            reporter.log(Status.INFO, "Using the pre-named device from TEST_LONG_NAME_DEVICE. "
                    + "This test never renames devices: a half-completed rename would leave the "
                    + "bench unusable for every case that looks a device up by name.");

            memberDevicePage.refreshHomeDashboard();
            boolean visible = memberDevicePage.canSeeDeviceAfterScroll(longNameDevice);
            AppUtil.captureScreenshot(memberDriver, "ec04_long_device_name");
            memberDevicePage.dumpTree("ec04_long_device_name");

            Assert.assertTrue(visible,
                    "The long-named device must still be findable on the member's home (EC-04) — "
                            + "a name that breaks the tile is a name that hides the device. "
                            + "Looked for: '" + truncate(longNameDevice) + "'.");

            // The app surviving is the assertable part. "Truncated with an ellipsis and no
            // overflow" is a visual property Appium cannot read, so the screenshot is the
            // artifact for that half and the report says so rather than implying otherwise.
            Assert.assertTrue(AppUtil.isAppInForeground(memberDriver),
                    "The app must not crash on a 200+ character device name (EC-04).");
            reporter.log(Status.INFO, "Ellipsis truncation and the absence of layout overflow are "
                    + "visual properties Appium cannot read — check "
                    + "test-output/screenshots/ec04_long_device_name*.png by eye.");
        });
    }

    // ══════════════════════════════════════════════════════════════════════════════
    // Cases the bench cannot reach
    // ══════════════════════════════════════════════════════════════════════════════

    @Test(priority = 5, description = "EC-01/02/03/05/07/08/09/12/13: cases needing a "
            + "differently-shaped home, a device fixture, fault injection, or a second admin")
    public void manualOnlyEdgeCases() {
        runCase("EC-01, EC-02, EC-03, EC-05, EC-07, EC-08, EC-09, EC-12, EC-13",
                "Edge cases out of reach on the two-phone bench", "Admin_Device", () -> {
            throw new SkipException(
                    "Not automatable on this rig — run manually:"
                    + "\n  EC-01 single-user home: needs a home with one member who is also its "
                    + "admin. Expect an empty user list and Skip as the only option."
                    + "\n  EC-02 all members are admins: expect every row greyed with \"All "
                    + "users are admins – no sharing required\" (locator "
                    + "SHARE_LIST_EMPTY_MESSAGE is ready)."
                    + "\n  EC-03 unknown device model: needs a device reporting model="
                    + "'unknown_xyz' — a firmware/backend fixture. Expect a graceful empty "
                    + "state and no share option."
                    + "\n  EC-05 50+ member home: needs 50 accounts; the assertion is about "
                    + "list performance and scroll smoothness, which is a human judgement."
                    + "\n  EC-07 network failure mid-save: needs a proxy that can time out one "
                    + "PUT /edit_user_device in flight. Cutting the radio fails the call before "
                    + "it is sent, which tests something else. Expect the previous mask retained "
                    + "and a retry offered."
                    + "\n  EC-08 concurrent admin edits: needs two admins editing the same "
                    + "member simultaneously. Expect last-write-wins by server timestamp with "
                    + "no corruption."
                    + "\n  EC-09 device deleted while the sharing UI is open: needs a second "
                    + "admin and destroys a provisioned device. Expect a 404 surfaced and a "
                    + "clean exit from the share flow."
                    + "\n  EC-12 QR read by a generic scanner: needs a third device with a "
                    + "camera app. Expect encoded text that is inert outside the app, with no "
                    + "sensitive data in the clear."
                    + "\n  EC-13 re-onboarding the same device: needs a remove-then-commission "
                    + "cycle. Expect a new ownership record and the old permission records "
                    + "cleaned up.");
        });
    }

    // ══════════════════════════════════════════════════════════════════════════════
    // Helpers
    // ══════════════════════════════════════════════════════════════════════════════

    private List<String> configuredDevices() {
        List<String> devices = new ArrayList<>();
        devices.add(testDeviceName);
        if (isSet(testLockDeviceName))     devices.add(testLockDeviceName);
        if (isSet(testPurifierDeviceName)) devices.add(testPurifierDeviceName);
        return devices;
    }

    private static boolean isSet(String s) { return s != null && !s.isBlank(); }

    private static String truncate(String s) {
        if (s == null) return "";
        String flat = s.replace("\n", " / ");
        return flat.length() <= 60 ? flat : flat.substring(0, 57) + "...";
    }

    /**
     * True when a label looks like a resource key that was never resolved.
     *
     * <p>Keys carry the tells display strings do not: dotted or snake_case identifiers, a
     * wrapping {@code {{ }}}, or a leading {@code @}. Requiring one of those rather than
     * guessing from length keeps real labels safe — "Modify OTP Refill Settings" is long and
     * legitimate, while {@code permission.fan.control} is not.</p>
     */
    private static boolean looksLikeAPlaceholderKey(String label) {
        if (label == null || label.isBlank()) return false;
        String l = label.trim();
        if (l.startsWith("@") || l.startsWith("{{") || l.contains("}}")) return true;
        // A dotted or underscored identifier with no spaces: permission.fan.control,
        // fan_edit_wifi. A genuine label has spaces or is a single plain word.
        boolean noSpaces = !l.contains(" ");
        return noSpaces && (l.contains(".") || l.contains("_"));
    }

    /** Navigates the admin to the fan's permission editor for the test member, or skips. */
    private void openFanPermissionEditorOrSkip() {
        adminSharingPage.navigateToManageFamily()
                .openFamilyHome(FAMILY_HOME_HINT)
                .openMemberPermissions(testMemberDisplayName);

        if (!adminSharingPage.openMemberDeviceRow(testDeviceName)) {
            throw new SkipException("'" + testDeviceName.replace("\n", " / ") + "' is not in "
                    + testMemberDisplayName + "'s device list — share it with them first, or "
                    + "pin the member-device-row locator.");
        }
        if (!permissionEditor.isAvailable()) {
            permissionEditor.dumpTree("permission_editor_unavailable");
            throw new SkipException("No permission-level picker on the device editor. Levels "
                    + "present: " + permissionEditor.availableLevels()
                    + ". See test-output/page-source/permission_editor_unavailable.xml.");
        }
    }

    /** Applies an access spec to the fan, skipping with the reason when it did not stick. */
    private void applyFanAccessOrSkip(SharedAccess spec) {
        openFanPermissionEditorOrSkip();
        var result = permissionEditor.apply(spec);
        reporter.log(Status.INFO, "Applied access: " + result.summary());
        String blocked = result.blockingReason();
        if (blocked != null) {
            throw new SkipException("Could not apply " + spec + " — " + blocked
                    + ". The member-side assertion is skipped rather than run against a "
                    + "permission set that was never applied.");
        }
    }
}
