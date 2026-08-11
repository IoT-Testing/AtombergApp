package com.appTest.tests;

import app.util.AppUtil;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * FanUiControlPermissionTest – the UI-control permission MATRIX for a shared fan
 * (spec §3.1). This is the "all possible test cases for UI control" suite: for
 * every permission preset and every meaningful Custom capability combination,
 * the ADMIN sets the member's fan permission and the MEMBER phone verifies which
 * on-device controls are actually exposed vs permission-denied.
 *
 * <p>Verifiable capability bits (member-side checks in {@code MemberDevicePage}):
 * <ul>
 *   <li>bit 0 — Control       → speed/power UI ({@code canControlFan})</li>
 *   <li>bit 1 — Edit Device   → rename/settings button ({@code canEditFanDevice})</li>
 *   <li>bit 2 — View Analytics→ analytics tab ({@code canViewFanAnalytics})</li>
 * </ul>
 * Bits 3 (Edit Wifi) and 4 (Automations) are toggled during setup for
 * completeness but have no dedicated member-side control assertion yet — add a
 * checker in {@code MemberDevicePage} when those screens are confirmed.</p>
 *
 * <p>Each row runs the full admin→member round-trip, so it is a true black-box
 * assertion of the client-side bitmask evaluation (spec §6, edge case 6).</p>
 */
public class FanUiControlPermissionTest extends BaseDeviceSharingTest {

    /** Family/home name to open on the admin phone — ADMIN_FAMILY_HINT (e.g. "Main Family"). */
    private static final String FAMILY_HOME_HINT =
            app.sharing.SharingConfig.get("ADMIN_FAMILY_HINT", "Main Family");

    /**
     * Rows: label, permission preset ("Basic"/"Super"/"Custom"), the five fan
     * capability toggles (only used when preset == Custom), and the three
     * expected member-visible controls.
     */
    @DataProvider(name = "fanPermissionMatrix")
    public Object[][] fanPermissionMatrix() {
        return new Object[][] {
                // label,                preset,   ctrl,  edit,  analy, wifi,  auto | expControl, expEdit, expAnaly
                {"Basic preset",         "Basic",  true,  false, false, false, false,  true,  false, false},
                {"Super preset",         "Super",  true,  true,  true,  true,  true,   true,  true,  true},
                {"Custom Control only",  "Custom", true,  false, false, false, false,  true,  false, false},
                {"Custom Control+Edit",  "Custom", true,  true,  false, false, false,  true,  true,  false},
                {"Custom Control+Analy", "Custom", true,  false, true,  false, false,  true,  false, true},
                {"Custom Ctrl+Edit+Anly","Custom", true,  true,  true,  false, false,  true,  true,  true},
        };
    }

    @Test(dataProvider = "fanPermissionMatrix",
            description = "Member fan UI controls match the granted permission bits")
    public void fanUiControlMatrix(
            String label, String preset,
            boolean control, boolean edit, boolean analytics, boolean wifi, boolean automations,
            boolean expControl, boolean expEdit, boolean expAnalytics) {

        reporter.startTest("Fan UI matrix – " + label, "Admin+Member");
        try {
            // ── ADMIN: set the member's fan permission for this row ──────────────
            adminSharingPage
                    .navigateToManageFamily()
                    .openFamilyHome(FAMILY_HOME_HINT)
                    .openMemberPermissions(testMemberDisplayName);

            if ("Custom".equalsIgnoreCase(preset)) {
                adminSharingPage.flow2_setFanCustomPermission(
                        testDeviceName, control, edit, analytics, wifi, automations);
            } else {
                adminSharingPage.flow2_editDevicePermission(testDeviceName, preset);
            }

            // ── MEMBER: verify the resulting UI controls ─────────────────────────
            waitForPermissionPropagation();
            memberDevicePage.refreshHomeScreen();

            Assert.assertTrue(memberDevicePage.canSeeDevice(testDeviceName),
                    "[" + label + "] Fan must be visible to member (Control is granted in every row)");

            boolean actualControl   = memberDevicePage.canControlFan(testDeviceName);
            boolean actualEdit      = memberDevicePage.canEditFanDevice(testDeviceName);
            boolean actualAnalytics = memberDevicePage.canViewFanAnalytics(testDeviceName);

            Assert.assertEquals(actualControl, expControl,
                    "[" + label + "] Control (bit 0) UI visibility mismatch");
            Assert.assertEquals(actualEdit, expEdit,
                    "[" + label + "] Edit Device (bit 1) UI visibility mismatch");
            Assert.assertEquals(actualAnalytics, expAnalytics,
                    "[" + label + "] View Analytics (bit 2) UI visibility mismatch");

            reporter.log(Status.PASS, "[" + label + "] control=" + actualControl
                    + " edit=" + actualEdit + " analytics=" + actualAnalytics);
        } catch (AssertionError | Exception e) {
            AppUtil.captureScreenshot(adminDriver,  "matrix_fail_admin_"  + label.replace(" ", "_"));
            AppUtil.captureScreenshot(memberDriver, "matrix_fail_member_" + label.replace(" ", "_"));
            reporter.log(Status.FAIL, "[" + label + "] " + e.getMessage());
            throw e;
        } finally {
            reporter.endTest();
        }
    }
}
