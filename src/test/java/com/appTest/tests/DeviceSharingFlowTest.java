package com.appTest.tests;

import app.util.AppUtil;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * DeviceSharingFlowTest – end-to-end, TWO-PHONE tests for the Device Sharing &
 * Permission Management feature (spec §4 User Flows).
 *
 * <p>Both phones are assumed <strong>pre-logged-in</strong>
 * ({@code noReset=true}); {@link BaseDeviceSharingTest} only re-logs in if a
 * login screen is unexpectedly detected.</p>
 *
 * <pre>
 *   adminDriver  → Phone 1 (home Admin / Device Owner)  → shares
 *   memberDriver → Phone 2 (non-admin family member)    → receives &amp; verifies
 * </pre>
 *
 * <p><b>QR camera limitation.</b> Appium cannot drive the camera to scan a QR
 * code, so the cross-phone hand-off uses the <em>invite code</em> printed
 * beside the QR: the admin copies it ({@code readInviteCode()}), the test
 * carries the string in-process, and the member enters it
 * ({@code joinViaCode()}) — the automation equivalent of "copy the code and use
 * it on the other phone".</p>
 *
 * <p>Ordering: methods use {@code priority} so a share happens before the
 * matching member verification. Failures are isolated per method; each opens
 * and closes its own Extent test.</p>
 */
public class DeviceSharingFlowTest extends BaseDeviceSharingTest {

    /** Family/home name to open on the admin phone — ADMIN_FAMILY_HINT (e.g. "Main Family"). */
    private static final String FAMILY_HOME_HINT =
            app.sharing.SharingConfig.get("ADMIN_FAMILY_HINT", "Main Family");

    // ══════════════════════════════════════════════════════════════════════════
    // FLOW 3 — Share entire home via QR/code → member joins → Basic access
    // ══════════════════════════════════════════════════════════════════════════

    @Test(priority = 1,
            description = "Flow 3 (2a): Admin shares the ENTIRE HOME; a QR + invite code are generated")
    public void adminSharesEntireHome_generatesCode() {
        reporter.startTest("Flow3 – Share entire home (admin)", "Admin_Device");
        try {
            adminSharingPage
                    .navigateToManageFamily()
                    .flow3_openShareBottomSheet()
                    .flow3_shareEntireHome();

            Assert.assertTrue(adminSharingPage.isQrCodeVisible(),
                    "QR code must be visible after 'Share Entire Home'");

            String code = adminSharingPage.readInviteCode();
            Assert.assertNotNull(code, "Admin must be able to read/copy the invite code");
            shareContext.entireHomeCode = code;

            reporter.log(Status.PASS, "Entire-home share code generated: " + code);
        } catch (Exception e) {
            AppUtil.captureScreenshot(adminDriver, "flow3_share_home_fail");
            reporter.log(Status.FAIL, "Share entire home failed: " + e.getMessage());
            throw e;
        } finally {
            reporter.endTest();
        }
    }

    @Test(priority = 2, dependsOnMethods = "adminSharesEntireHome_generatesCode",
            description = "Flow 3 (3): Member joins via invite code; shared fan appears at BASIC access")
    public void memberJoinsEntireHome_hasBasicAccess() {
        reporter.startTest("Flow3 – Member joins entire home", "Member_Device");
        try {
            Assert.assertNotNull(shareContext.entireHomeCode,
                    "Precondition: admin must have generated an invite code");

            memberDevicePage
                    .openJoinFamilyScreen()
                    .joinViaCode(shareContext.entireHomeCode);

            waitForPermissionPropagation();
            memberDevicePage.refreshHomeScreen();

            Assert.assertTrue(memberDevicePage.canSeeDevice(testDeviceName),
                    "Shared fan '" + testDeviceName + "' must appear on member's home");
            Assert.assertTrue(memberDevicePage.hasBasicFanAccess(testDeviceName),
                    "Entire-home QR share must apply BASIC permissions (Control only) — spec §4 Flow 3 (2a)");

            reporter.log(Status.PASS, "Member joined and holds Basic fan access.");
        } catch (Exception e) {
            AppUtil.captureScreenshot(memberDriver, "flow3_member_join_fail");
            reporter.log(Status.FAIL, "Member join / basic-access check failed: " + e.getMessage());
            throw e;
        } finally {
            reporter.endTest();
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // FLOW 3 — Share a SPECIFIC device at SUPER → member gets full access
    // ══════════════════════════════════════════════════════════════════════════

    @Test(priority = 3,
            description = "Flow 3 (2b): Admin shares one device at SUPER; invite code generated")
    public void adminSharesSpecificDeviceSuper_generatesCode() {
        reporter.startTest("Flow3 – Share specific device @Super (admin)", "Admin_Device");
        try {
            adminSharingPage
                    .navigateToManageFamily()
                    .flow3_openShareBottomSheet()
                    .flow3_shareSpecificDevices(List.of(testDeviceName), "Super");

            Assert.assertTrue(adminSharingPage.isQrCodeVisible(),
                    "QR must be visible after specific-device share");

            String code = adminSharingPage.readInviteCode();
            Assert.assertNotNull(code, "Admin must read the invite code for the specific share");
            shareContext.specificSuperCode = code;

            reporter.log(Status.PASS, "Specific-device Super share code: " + code);
        } catch (Exception e) {
            AppUtil.captureScreenshot(adminDriver, "flow3_share_specific_fail");
            reporter.log(Status.FAIL, "Share specific device @Super failed: " + e.getMessage());
            throw e;
        } finally {
            reporter.endTest();
        }
    }

    @Test(priority = 4, dependsOnMethods = "adminSharesSpecificDeviceSuper_generatesCode",
            description = "Member joins the Super share and sees ALL fan controls (spec §3.1 Super)")
    public void memberJoinsSpecificSuper_hasFullAccess() {
        reporter.startTest("Flow3 – Member full access @Super", "Member_Device");
        try {
            memberDevicePage
                    .openJoinFamilyScreen()
                    .joinViaCode(shareContext.specificSuperCode);

            waitForPermissionPropagation();
            memberDevicePage.refreshHomeScreen();

            Assert.assertTrue(memberDevicePage.canSeeDevice(testDeviceName),
                    "Super-shared fan must appear on member's home");
            Assert.assertTrue(memberDevicePage.hasFullFanAccess(testDeviceName),
                    "Super share must expose every fan control (Control+Edit+Analytics)");

            reporter.log(Status.PASS, "Member holds full (Super) fan access.");
        } catch (Exception e) {
            AppUtil.captureScreenshot(memberDriver, "flow3_member_super_fail");
            reporter.log(Status.FAIL, "Member Super-access check failed: " + e.getMessage());
            throw e;
        } finally {
            reporter.endTest();
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // FLOW 2 — Manage Family: add → edit → remove a device permission per user
    // ══════════════════════════════════════════════════════════════════════════

    @Test(priority = 5,
            description = "Flow 2 (3a/3b): Admin edits the member's fan permission Basic → Super")
    public void adminEditsMemberPermission_toSuper() {
        reporter.startTest("Flow2 – Edit member permission → Super", "Admin_Device");
        try {
            adminSharingPage
                    .navigateToManageFamily()
                    .openFamilyHome(FAMILY_HOME_HINT)
                    .openMemberPermissions(testMemberDisplayName)
                    .flow2_editDevicePermission(testDeviceName, "Super");

            reporter.log(Status.PASS, "Admin upgraded member's fan permission to Super (PUT /edit_user_device).");
        } catch (Exception e) {
            AppUtil.captureScreenshot(adminDriver, "flow2_edit_fail");
            reporter.log(Status.FAIL, "Edit member permission failed: " + e.getMessage());
            throw e;
        } finally {
            reporter.endTest();
        }
    }

    @Test(priority = 6, dependsOnMethods = "adminEditsMemberPermission_toSuper",
            description = "Member's fan now reflects the upgraded Super permission")
    public void memberReflectsUpgradedPermission() {
        reporter.startTest("Flow2 – Member sees upgraded permission", "Member_Device");
        try {
            waitForPermissionPropagation();
            memberDevicePage.refreshHomeScreen();
            Assert.assertTrue(memberDevicePage.hasFullFanAccess(testDeviceName),
                    "After admin upgrade to Super, member must see Edit + Analytics controls");
            reporter.log(Status.PASS, "Member reflects upgraded Super access.");
        } catch (Exception e) {
            AppUtil.captureScreenshot(memberDriver, "flow2_member_upgrade_fail");
            reporter.log(Status.FAIL, "Member upgrade reflection failed: " + e.getMessage());
            throw e;
        } finally {
            reporter.endTest();
        }
    }

    @Test(priority = 7, dependsOnMethods = "memberReflectsUpgradedPermission",
            description = "Flow 2 (3c): Admin removes the member's device access; member can no longer see it")
    public void adminRemovesMemberDevice_memberLosesAccess() {
        reporter.startTest("Flow2 – Remove member device", "Admin+Member");
        try {
            adminSharingPage
                    .navigateToManageFamily()
                    .openFamilyHome(FAMILY_HOME_HINT)
                    .openMemberPermissions(testMemberDisplayName)
                    .flow2_removeDeviceFromMember(testDeviceName);

            waitForPermissionPropagation();
            memberDevicePage.refreshHomeScreen();

            Assert.assertTrue(memberDevicePage.cannotSeeDevice(testDeviceName),
                    "After removal (DELETE /remove_user_device) the fan must disappear from member's home");
            reporter.log(Status.PASS, "Member access removed and reflected on member phone.");
        } catch (Exception e) {
            AppUtil.captureScreenshot(adminDriver, "flow2_remove_fail_admin");
            AppUtil.captureScreenshot(memberDriver, "flow2_remove_fail_member");
            reporter.log(Status.FAIL, "Remove-device flow failed: " + e.getMessage());
            throw e;
        } finally {
            reporter.endTest();
        }
    }

    // ── Cross-test shared state ────────────────────────────────────────────────

    /** Holds invite codes generated by admin steps for the paired member steps. */
    private final ShareContext shareContext = new ShareContext();

    private static final class ShareContext {
        String entireHomeCode;
        String specificSuperCode;
    }
}
