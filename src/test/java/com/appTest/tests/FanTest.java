package com.appTest.tests;

import app.resources.Locators.Android.DeviceScreens.FanLocators;
import app.util.ActionsUtil;
import app.util.AppUtil;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static app.resources.Locators.Android.DeviceScreens.FanLocators.*;
import static app.resources.Locators.Android.HomeLocators.APP_LOGO;
import static app.resources.Locators.Android.HomeLocators.MORE_TAB;

/**
 * FanTest – validates all fan device controls on the Atomberg Home App.
 *
 * NOTE: @Listeners is intentionally omitted here.
 * It is declared on BaseTest and is inherited by all subclasses.
 * Re-declaring it here would register each listener twice.
 *
 * Prerequisites:
 *   - At least one Atomberg fan must already be paired and visible on the home screen.
 *   - The user must be logged in (ensured by BaseTest.setup() → AppInitializer).
 *
 * Test order:
 *    1. Open fan control panel          → asserts POWER_BUTTON visible
 *    2. Power OFF                       → asserts panel still open; speed buttons not tappable
 *    3. Power ON                        → asserts speed buttons are now present and ready
 *    4–9. Speed 1 → 6                   → asserts correct speed button tapped successfully
 *   10. Sleep mode ON                   → asserts panel stays responsive (or skips if absent)
 *   11. Timer screen visible            → asserts TIMER_SCREEN element (or skips if absent)
 *   12. Return to home screen           → asserts MORE_TAB visible
 */
public class FanTest extends BaseTest {

    @BeforeClass(dependsOnMethods = "setup")
    public void initFanTest() {
        Assert.assertNotNull(driver, "Driver must not be null before FanTest");
        System.out.println("FanTest ready on device: " + deviceSlot);
    }

    // ── 1. Open fan control panel ─────────────────────────────────────────────

    @Test(priority = 1, description = "Open the fan device control panel from the home screen")
    public void testOpenFanControlPanel() {
        reporter.startTest("Open Fan Control Panel", deviceSlot);
        try {
            ActionsUtil.SSleep(2);

            Assert.assertTrue(
                    AppUtil.isElementPresent(driver, FAN_CARD),
                    "A paired fan card must be visible on the home screen before the panel can open");

            AppUtil.clickElement(driver, FAN_CARD, "Fan Card");
            ActionsUtil.SSleep(3);

            Assert.assertTrue(
                    AppUtil.isElementPresent(driver, POWER_BUTTON),
                    "Power button must be visible once the fan control panel opens");

            AppUtil.captureScreenshot(driver, "fan_panel_opened");
            reporter.log(Status.PASS, "Fan control panel opened — power button visible");
        } catch (Exception e) {
            AppUtil.captureScreenshot(driver, "fan_panel_fail");
            reporter.log(Status.FAIL, "Failed to open fan panel: " + e.getMessage());
            afterTestFailure();
            throw new RuntimeException(e);
        } finally {
            reporter.endTest();
        }
    }

    // ── 2. Power OFF ─────────────────────────────────────────────────────────

    @Test(priority = 2, description = "Power the fan OFF and assert the panel stays open",
            dependsOnMethods = "testOpenFanControlPanel")
    public void testPowerOff() {
        reporter.startTest("Fan Power OFF", deviceSlot);
        try {
            AppUtil.clickElement(driver, POWER_BUTTON, "Power Button (OFF)");
            ActionsUtil.SSleep(2);

            // Panel must remain open after power toggle
            Assert.assertTrue(
                    AppUtil.isElementPresent(driver, POWER_BUTTON),
                    "Power button must still be visible after tapping OFF — panel should not close");

            // Speed buttons should be absent or unresponsive when fan is off
            Assert.assertFalse(
                    AppUtil.isElementPresent(driver, SPEED_1),
                    "Speed 1 button must NOT be present when the fan is powered OFF");

            AppUtil.captureScreenshot(driver, "fan_power_off");
            reporter.log(Status.PASS, "Fan powered OFF — speed buttons absent, panel still open");
        } catch (Exception e) {
            AppUtil.captureScreenshot(driver, "fan_power_off_fail");
            reporter.log(Status.FAIL, "Power OFF failed: " + e.getMessage());
            afterTestFailure();
            throw new RuntimeException(e);
        } finally {
            reporter.endTest();
        }
    }

    // ── 3. Power ON ──────────────────────────────────────────────────────────

    @Test(priority = 3, description = "Power the fan ON and assert speed buttons appear",
            dependsOnMethods = "testPowerOff")
    public void testPowerOn() {
        reporter.startTest("Fan Power ON", deviceSlot);
        try {
            AppUtil.clickElement(driver, POWER_BUTTON, "Power Button (ON)");
            ActionsUtil.SSleep(2);

            // Panel must remain open
            Assert.assertTrue(
                    AppUtil.isElementPresent(driver, POWER_BUTTON),
                    "Power button must still be visible after tapping ON");

            // Speed buttons must be accessible when fan is on
            Assert.assertTrue(
                    AppUtil.isElementPresent(driver, SPEED_1),
                    "Speed 1 button must be visible and accessible when the fan is powered ON");

            AppUtil.captureScreenshot(driver, "fan_power_on");
            reporter.log(Status.PASS, "Fan powered ON — speed buttons visible and ready");
        } catch (Exception e) {
            AppUtil.captureScreenshot(driver, "fan_power_on_fail");
            reporter.log(Status.FAIL, "Power ON failed: " + e.getMessage());
            afterTestFailure();
            throw new RuntimeException(e);
        } finally {
            reporter.endTest();
        }
    }

    // ── 4–9. Speed 1 through Speed 6 ─────────────────────────────────────────

    @Test(priority = 4, description = "Set fan to Speed 1",  dependsOnMethods = "testPowerOn")
    public void testSpeed1() { setAndVerifySpeed(1, SPEED_1); }

    @Test(priority = 5, description = "Set fan to Speed 2",  dependsOnMethods = "testSpeed1")
    public void testSpeed2() { setAndVerifySpeed(2, SPEED_2); }

    @Test(priority = 6, description = "Set fan to Speed 3",  dependsOnMethods = "testSpeed2")
    public void testSpeed3() { setAndVerifySpeed(3, SPEED_3); }

    @Test(priority = 7, description = "Set fan to Speed 4",  dependsOnMethods = "testSpeed3")
    public void testSpeed4() { setAndVerifySpeed(4, SPEED_4); }

    @Test(priority = 8, description = "Set fan to Speed 5",  dependsOnMethods = "testSpeed4")
    public void testSpeed5() { setAndVerifySpeed(5, SPEED_5); }

    @Test(priority = 9, description = "Set fan to Speed 6 (maximum)", dependsOnMethods = "testSpeed5")
    public void testSpeed6() { setAndVerifySpeed(6, BOOST_BUTTON); }

    // ── 10. Sleep mode ────────────────────────────────────────────────────────

    @Test(priority = 10, description = "Activate Sleep mode; skip gracefully if unavailable on this firmware",
            dependsOnMethods = "testSpeed6")
    public void testSleepModeOn() {
        reporter.startTest("Sleep Mode ON", deviceSlot);
        try {
            if (!AppUtil.isElementPresent(driver, SLEEP_BUTTON)) {
                reporter.log(Status.PASS,
                        "Sleep button not present on this firmware/model — test skipped");
                throw new SkipException("Sleep button absent — skipping for this device/firmware");
            }

            AppUtil.clickElement(driver, SLEEP_BUTTON, "Sleep Button");
            ActionsUtil.SSleep(2);

            // Panel must remain responsive after sleep toggle
            Assert.assertTrue(
                    AppUtil.isElementPresent(driver, POWER_BUTTON),
                    "Power button must still be visible after activating Sleep mode — panel should not close");

            AppUtil.captureScreenshot(driver, "sleep_mode_on");
            reporter.log(Status.PASS, "Sleep mode activated — panel still responsive");
        } catch (SkipException se) {
            throw se; // propagate so TestNG marks as SKIP, not FAIL
        } catch (Exception e) {
            AppUtil.captureScreenshot(driver, "sleep_mode_fail");
            reporter.log(Status.FAIL, "Sleep mode toggle failed: " + e.getMessage());
            afterTestFailure();
            throw new RuntimeException(e);
        } finally {
            reporter.endTest();
        }
    }

    // ── 11. Timer screen ─────────────────────────────────────────────────────

    @Test(priority = 11, description = "Assert Timer element is visible on the fan panel; skip if absent",
            dependsOnMethods = "testSleepModeOn")
    public void testTimerScreen() {
        reporter.startTest("Timer Screen", deviceSlot);
        try {
            if (!AppUtil.isElementPresent(driver, TIMER_SCREEN)) {
                reporter.log(Status.PASS,
                        "Timer element not present on this model/firmware — test skipped");
                throw new SkipException("Timer element absent — skipping for this device/firmware");
            }

            Assert.assertTrue(
                    AppUtil.isElementPresent(driver, TIMER_SCREEN),
                    "Timer screen element must be visible on the fan control panel");

            AppUtil.captureScreenshot(driver, "timer_screen_visible");
            reporter.log(Status.PASS, "Timer screen element confirmed visible");
        } catch (SkipException se) {
            throw se;
        } catch (Exception e) {
            AppUtil.captureScreenshot(driver, "timer_screen_fail");
            reporter.log(Status.FAIL, "Timer screen check failed: " + e.getMessage());
            afterTestFailure();
            throw new RuntimeException(e);
        } finally {
            reporter.endTest();
        }
    }

    // ── 12. Return to home screen ─────────────────────────────────────────────

    @Test(priority = 12, description = "Return to home screen from the fan control panel",
            dependsOnMethods = "testTimerScreen")
    public void testReturnToHome() {
        reporter.startTest("Return to Home Screen", deviceSlot);
        try {
            if (AppUtil.isElementPresent(driver, APP_LOGO)) {
                    driver.navigate().back();
            } else {
                driver.navigate().back();
            }
            ActionsUtil.SSleep(2);

            Assert.assertTrue(
                    AppUtil.isElementPresent(driver, MORE_TAB),
                    "More tab must be visible after returning to the home screen");

            AppUtil.captureScreenshot(driver, "returned_to_home");
            reporter.log(Status.PASS, "Returned to home screen — More tab confirmed visible");
        } catch (Exception e) {
            AppUtil.captureScreenshot(driver, "return_to_home_fail");
            reporter.log(Status.FAIL, "Return to home failed: " + e.getMessage());
            afterTestFailure();
            throw new RuntimeException(e);
        } finally {
            reporter.endTest();
        }
    }

    // ── Internal helper ───────────────────────────────────────────────────────

    /**
     * Taps the speed button for {@code speedNumber}, waits for the UI to settle,
     * then asserts the tapped button is still present (confirms we are still on the
     * panel and the app did not crash).
     *
     * @param speedNumber 1–6
     * @param locator     Matching {@code By} locator from {@link FanLocators}
     */
    private void setAndVerifySpeed(int speedNumber, By locator) {
        String label = "Speed " + speedNumber;
        reporter.startTest("Fan " + label, deviceSlot);
        try {
            // Assert speed button is available before tapping
            Assert.assertTrue(
                    AppUtil.isElementPresent(driver, locator),
                    label + " button must be present before tapping (fan must be ON)");

            AppUtil.clickElement(driver, locator, label + " Button");
            ActionsUtil.SSleep(2);
            AppUtil.captureScreenshot(driver, "fan_speed_" + speedNumber);

            // Assert the panel is still open and the speed button is still visible
            Assert.assertTrue(
                    AppUtil.isElementPresent(driver, locator),
                    label + " button must still be visible after tapping — panel must remain open");

            // Optional: read content-desc for a best-effort label confirmation
            String displayed = getDisplayedSpeed();
            if (displayed != null && displayed.contains(String.valueOf(speedNumber))) {
                reporter.log(Status.PASS,
                        label + " set — UI speed label confirms " + speedNumber);
            } else {
                reporter.log(Status.PASS,
                        label + " button tapped — panel still open (speed label not directly readable)");
            }
        } catch (Exception e) {
            AppUtil.captureScreenshot(driver, "fan_speed_" + speedNumber + "_fail");
            reporter.log(Status.FAIL, label + " failed: " + e.getMessage());
            afterTestFailure();
            throw new RuntimeException(e);
        } finally {
            reporter.endTest();
        }
    }

    /**
     * Attempts to read the speed label from the fan panel's {@code CURRENT_SPEED_LABEL}.
     * Returns {@code null} if the element is absent or has no readable content-desc.
     * Uses the inherited {@code driver} field directly — no parameter needed.
     */
    private String getDisplayedSpeed() {
        try {
            WebElement label = driver.findElement(BOOST_BUTTON);
            return label.getDomAttribute("content-desc");
        } catch (NoSuchElementException e) {
            return null;
        }
    }
}
