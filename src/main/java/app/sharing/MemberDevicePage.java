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
        driver.navigate().back();
        ActionsUtil.SSleep(2);
        // Tap the home tab / logo to return
        By homeLogo = By.xpath("//android.widget.ImageView[@index=\"0\"]");
        try {
            driver.findElement(homeLogo).click();
        } catch (Exception ignored) {}
        ActionsUtil.SSleep(3);
        AppUtil.captureScreenshot(driver, "member_home_refreshed");
        return this;
    }

    /**
     * Returns true if the named device card is visible on the member's home screen.
     * A shared device must appear here after the admin completes sharing.
     */
    public boolean canSeeDevice(String deviceName) {
        boolean visible = isPresent(deviceCardOnHome(deviceName));
        System.out.println("[MemberDevicePage] Device '" + deviceName + "' visible: " + visible);
        return visible;
    }

    /**
     * Returns true if the named device is NOT visible on the member's home screen.
     * Used after an admin removes the member's access (Flow 2, step 3c).
     */
    public boolean cannotSeeDevice(String deviceName) {
        return !canSeeDevice(deviceName);
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
    // PRIVATE HELPERS
    // ══════════════════════════════════════════════════════════════════════════

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
            System.out.println("[MemberDevicePage] Tapped: " + label);
        } catch (Exception e) {
            AppUtil.captureScreenshot(driver, "tap_fail_member");
            throw new RuntimeException("[MemberDevicePage] Tap failed: " + label, e);
        }
    }

    private boolean isPresent(By locator) {
        try { return driver.findElement(locator).isDisplayed(); }
        catch (Exception e) { return false; }
    }
}