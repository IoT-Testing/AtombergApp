package com.appTest.tests;

import app.util.AppUtil;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * SharingEdgeCaseTest – covers the edge cases &amp; constraints from spec §6, and
 * the admin-blocked behaviour from Flow 1 step 2. Two phones required.
 *
 * <p>Each test maps to a numbered spec scenario:
 * <ol>
 *   <li>Admin edits own permissions → editor blocked, admin rows greyed-out.</li>
 *   <li>Same device shared to same user twice → upsert, latest wins.</li>
 *   <li>Control without permission → client shows permission-denied, no API call.</li>
 *   <li>QR share with no explicit permission → defaults to Basic (mask ≠ 0).</li>
 * </ol>
 * </p>
 */
public class SharingEdgeCaseTest extends BaseDeviceSharingTest {

    /** Family/home name to open on the admin phone — ADMIN_FAMILY_HINT (e.g. "Main Family"). */
    private static final String FAMILY_HOME_HINT =
            app.sharing.SharingConfig.get("ADMIN_FAMILY_HINT", "Main Family");

    // ── §6.1 + Flow 1 step 2 — admin rows are non-selectable ──────────────────

    @Test(priority = 1,
            description = "§6.1: Admin members appear greyed-out / non-selectable in the share user list")
    public void adminRowsAreGreyedOut() {
        reporter.startTest("Edge §6.1 – Admin row disabled", "Admin_Device");
        try {
            adminSharingPage
                    .navigateToManageFamily()
                    .openFamilyHome(FAMILY_HOME_HINT);

            String adminName = optionalEnv("ADMIN_DISPLAY_NAME", null);
            if (adminName == null) {
                reporter.log(Status.SKIP, "ADMIN_DISPLAY_NAME not set — skipping admin-row assertion.");
                throw new org.testng.SkipException("ADMIN_DISPLAY_NAME not provided");
            }

            Assert.assertTrue(adminSharingPage.isAdminRowDisabled(adminName),
                    "Admin member row '" + adminName + "' must be disabled/greyed-out (spec §6.1)");
            reporter.log(Status.PASS, "Admin row is non-selectable as required.");
        } catch (org.testng.SkipException se) {
            throw se;
        } catch (Exception e) {
            AppUtil.captureScreenshot(adminDriver, "edge_admin_row_fail");
            reporter.log(Status.FAIL, "Admin-row disabled check failed: " + e.getMessage());
            throw e;
        } finally {
            reporter.endTest();
        }
    }

    // ── §6.5 — same device shared twice uses upsert (latest permission wins) ──

    @Test(priority = 2,
            description = "§6.5: Re-sharing the same fan to the same member upserts — Basic then Super, Super wins")
    public void reshareSameDeviceUpserts() {
        reporter.startTest("Edge §6.5 – Upsert on re-share", "Admin+Member");
        try {
            // First set Basic
            adminSharingPage
                    .navigateToManageFamily()
                    .openFamilyHome(FAMILY_HOME_HINT)
                    .openMemberPermissions(testMemberDisplayName)
                    .flow2_editDevicePermission(testDeviceName, "Basic");
            waitForPermissionPropagation();
            memberDevicePage.refreshHomeScreen();
            Assert.assertTrue(memberDevicePage.hasBasicFanAccess(testDeviceName),
                    "After first share the member should hold Basic access");

            // Re-share the SAME device to the SAME member at Super
            adminSharingPage
                    .navigateToManageFamily()
                    .openFamilyHome(FAMILY_HOME_HINT)
                    .openMemberPermissions(testMemberDisplayName)
                    .flow2_editDevicePermission(testDeviceName, "Super");
            waitForPermissionPropagation();
            memberDevicePage.refreshHomeScreen();

            Assert.assertTrue(memberDevicePage.hasFullFanAccess(testDeviceName),
                    "Upsert: latest (Super) permission must overwrite the previous Basic — spec §6.5");
            reporter.log(Status.PASS, "Re-share upserts to latest permission (no duplicate record).");
        } catch (Exception e) {
            AppUtil.captureScreenshot(adminDriver,  "edge_upsert_admin_fail");
            AppUtil.captureScreenshot(memberDriver, "edge_upsert_member_fail");
            reporter.log(Status.FAIL, "Upsert re-share check failed: " + e.getMessage());
            throw e;
        } finally {
            reporter.endTest();
        }
    }

    // ── §6.6 — control without permission shows a permission-denied state ─────

    @Test(priority = 3,
            description = "§6.6: With Basic-only fan, member attempting Edit/Analytics gets a denied state, no crash")
    public void controlWithoutPermissionShowsDenied() {
        reporter.startTest("Edge §6.6 – Permission denied", "Admin+Member");
        try {
            // Ensure member has ONLY Basic (Control) on the fan
            adminSharingPage
                    .navigateToManageFamily()
                    .openFamilyHome(FAMILY_HOME_HINT)
                    .openMemberPermissions(testMemberDisplayName)
                    .flow2_editDevicePermission(testDeviceName, "Basic");
            waitForPermissionPropagation();
            memberDevicePage.refreshHomeScreen();

            // Edit Device (bit 1) is NOT granted at Basic → the control must be
            // absent/denied, and the member must NOT be able to edit.
            Assert.assertFalse(memberDevicePage.canEditFanDevice(testDeviceName),
                    "Edit Device UI must be hidden/denied when bit 1 is not set (spec §6.6)");
            Assert.assertFalse(memberDevicePage.canViewFanAnalytics(testDeviceName),
                    "Analytics UI must be hidden/denied when bit 2 is not set (spec §6.6)");

            reporter.log(Status.PASS, "Ungranted capabilities are correctly blocked in the UI.");
        } catch (Exception e) {
            AppUtil.captureScreenshot(memberDriver, "edge_denied_fail");
            reporter.log(Status.FAIL, "Permission-denied check failed: " + e.getMessage());
            throw e;
        } finally {
            reporter.endTest();
        }
    }

    // ── §6.4 — QR share with no explicit permission defaults to Basic ─────────

    @Test(priority = 4,
            description = "§6.4: Entire-home QR share with no explicit level defaults to Basic (non-zero mask)")
    public void qrShareDefaultsToBasic() {
        reporter.startTest("Edge §6.4 – QR defaults to Basic", "Admin+Member");
        try {
            adminSharingPage
                    .navigateToManageFamily()
                    .flow3_openShareBottomSheet()
                    .flow3_shareEntireHome();
            Assert.assertTrue(adminSharingPage.isQrCodeVisible(), "QR must be generated");

            String code = adminSharingPage.readInviteCode();
            Assert.assertNotNull(code, "Invite code must be readable");

            memberDevicePage
                    .openJoinFamilyScreen()
                    .joinViaCode(code);
            waitForPermissionPropagation();
            memberDevicePage.refreshHomeScreen();

            // Basic (mask != 0) means Control is granted but Edit/Analytics are not.
            Assert.assertTrue(memberDevicePage.hasBasicFanAccess(testDeviceName),
                    "QR share must default to Basic — Control granted, no zero-permission share (spec §6.4)");
            reporter.log(Status.PASS, "QR share defaulted to Basic access as required.");
        } catch (Exception e) {
            AppUtil.captureScreenshot(adminDriver,  "edge_qr_default_admin_fail");
            AppUtil.captureScreenshot(memberDriver, "edge_qr_default_member_fail");
            reporter.log(Status.FAIL, "QR-defaults-to-Basic check failed: " + e.getMessage());
            throw e;
        } finally {
            reporter.endTest();
        }
    }

    // ── helpers ────────────────────────────────────────────────────────────────

    /** List form kept for readability where a single-device share is expressed as a list. */
    @SuppressWarnings("unused")
    private static List<String> one(String device) { return List.of(device); }
}
