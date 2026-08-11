package com.appTest.tests;

import app.util.AppUtil;
import com.aventstack.extentreports.Status;
import org.testng.annotations.Test;

/**
 * ShareFlowProbeTest — a capture run, not a test. It walks the long-press share
 * journey on the ADMIN phone and dumps a screenshot + page source at every step,
 * then reports whether a Super / Basic / Custom picker appears and whether the
 * family-wide confirmation dialog is still there.
 *
 * <p>Run this FIRST after installing the permission-levels APK:</p>
 * <pre>
 *   mvn test "-DsuiteFile=testng-probe.xml"
 * </pre>
 *
 * <p>It asserts nothing and always passes. That is deliberate — every
 * {@code PERMISSION_LEVEL_*} and {@code TOGGLE_*} locator in
 * {@code SharingLocators} is an unverified guess written before any build exposed
 * those screens, so a run that asserted on them would fail for reasons that say
 * nothing about the app. The dumps under {@code test-output/page-source/probe_*.xml}
 * are the deliverable; the matrix gets rebuilt from those.</p>
 *
 * <p>Only the member phone is left untouched, so this is safe to re-run. It does
 * generate an invite code that nobody redeems, which expires on its own.</p>
 */
public class ShareFlowProbeTest extends BaseDeviceSharingTest {

    @Test(description = "Capture every screen of the long-press share flow (no assertions)")
    public void probeShareFlow() {
        reporter.startTest("Probe – long-press share journey", "Admin_Device");
        try {
            adminSharingPage.probeShareFlow(testDeviceName);
            reporter.log(Status.PASS, "Probe complete — see test-output/page-source/probe_*.xml");
        } catch (Exception e) {
            // Still not a failure: a probe that dies halfway has usually already
            // dumped the screen that explains why.
            AppUtil.captureScreenshot(adminDriver, "probe_aborted");
            reporter.log(Status.WARNING, "Probe stopped early: " + e.getMessage()
                    + " — the dumps captured up to that point are still usable.");
            System.err.println("[Probe] Stopped early: " + e.getMessage());
        } finally {
            reporter.endTest();
        }
    }
}
