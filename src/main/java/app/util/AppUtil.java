package app.util;

import app.resources.Env;
import app.resources.Locators.Android.HomeLocators;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.appmanagement.ApplicationState;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import static app.resources.AppInfo.ATOMBERG_HOME;
import static app.resources.Locators.Android.DeviceAdditionScreen.Phoenix.ADD_BUTTON_XPATH;
import static app.util.ActionsUtil.sleep;

/**
 * AppUtil – common helper methods used across all test classes.
 */
public class AppUtil {

    private static final String SCREENSHOT_DIR =
            System.getProperty("user.dir") + File.separator + "screenshots" + File.separator;

    // Wi-Fi provisioning credentials — read from the environment, never committed.
    // Set WIFI_SSID / WIFI_PASSWORD (see test.env.example) before running provisioning flows.
    private static final String DEFAULT_WIFI_SSID     = Env.optional("WIFI_SSID", "");
    private static final String DEFAULT_WIFI_PASSWORD = Env.optional("WIFI_PASSWORD", "");

    private static final By WIFI_INPUT_FIELD    = By.xpath("//android.widget.EditText[1]");
    private static final By PASSWORD_INPUT_FIELD = By.xpath("//android.widget.EditText[2]");
    private static final By CONTINUE_BUTTON     = By.xpath("//android.widget.Button[@content-desc='Continue']");

    static {
        File dir = new File(SCREENSHOT_DIR);
        if (!dir.exists()) dir.mkdirs();
    }

    // ── Screenshots ───────────────────────────────────────────────────────────

    /**
     * Label of the most recent element a page object tapped. Screenshots taken
     * with no name of their own fall back to this, so an untagged capture is
     * still identifiable by the action that produced it.
     * Written from page-object taps on both driver threads → volatile.
     */
    private static volatile String lastActionLabel = "screen";

    /**
     * Records the element a page object just interacted with. Call this from tap
     * helpers; {@link #captureScreenshot(AndroidDriver)} then names untagged
     * screenshots after it.
     */
    public static void noteAction(String label) {
        String safe = sanitizeFileName(label);
        if (!safe.isEmpty()) lastActionLabel = safe;
    }

    /** The last recorded action label — used as the fallback screenshot name. */
    public static String lastActionLabel() {
        return lastActionLabel;
    }

    /**
     * Screenshot named after the last tapped element (see {@link #noteAction}).
     * Use when there is no meaningful tag for the current screen.
     */
    public static void captureScreenshot(AndroidDriver driver) {
        captureScreenshot(driver, lastActionLabel);
    }

    public static void captureScreenshot(AndroidDriver driver, String name) {
        // Device names carry the room on a second line ("Aris Fan\nLiving Room"),
        // so raw names reach here containing newlines — and a newline in a path is
        // what produced "The filename, directory name, or volume label syntax is
        // incorrect" on Windows. Sanitize centrally so every call site is covered
        // rather than relying on each one to strip its own separators.
        String safeName = sanitizeFileName(name);
        if (safeName.isEmpty()) safeName = lastActionLabel;

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String destPath  = SCREENSHOT_DIR + safeName + "_" + timestamp + ".png";
        try {
            File src = driver.getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(src, new File(destPath));
            System.out.println("Screenshot saved: " + destPath);
        } catch (IOException | WebDriverException e) {
            System.err.println("Failed to save screenshot: " + e.getMessage());
        }
    }

    /**
     * Makes an arbitrary UI label safe to embed in a Windows filename: collapses
     * newlines/tabs and the reserved set {@code \ / : * ? " < > |} to underscores
     * and caps the length so the full path stays inside MAX_PATH.
     *
     * @return the sanitized name, or "" when nothing usable remains
     */
    public static String sanitizeFileName(String raw) {
        if (raw == null) return "";
        String s = raw.replaceAll("[\\r\\n\\t]+", "_")
                      .replaceAll("[\\\\/:*?\"<>|]", "_")
                      .replaceAll("\\s+", "_")
                      .replaceAll("_{2,}", "_")
                      .replaceAll("^_+|_+$", "");
        return s.length() > 80 ? s.substring(0, 80) : s;
    }

    // ── Home device tiles ─────────────────────────────────────────────────────

    /**
     * Scrapes the display names of every device tile on the current Home/Devices
     * screen. Used to compare the admin's and member's dashboards after a share
     * (they must list the same devices).
     *
     * <p><b>Best-effort / diagnostic only.</b> Tiles are matched on the
     * {@code "<device>\n<room>"} content-desc shape, across the same three classes
     * {@code SharingLocators.deviceCardOnHome} ORs — a run on 2026-08-07 scanning
     * only {@code ImageView} returned zero while {@code isDeviceOnHome} found every
     * device, i.e. the tiles are not all ImageViews. Until a real Home-screen dump
     * pins the structure, never assert on this list being complete: use it to
     * report what is on screen, and drive assertions off known device names with
     * {@code deviceCardOnHome}. Chrome that shares the newline shape (the tab bar's
     * "Analytics\nTab 1 of 3", count-prefixed family tiles) is filtered out.</p>
     *
     * @return the tile content-descs, newline intact, in screen order and de-duped
     */
    public static java.util.List<String> listDeviceTilesOnHome(AndroidDriver driver) {
        java.util.LinkedHashSet<String> tiles = new java.util.LinkedHashSet<>();
        String[] tileClasses = {
                "android.view.View", "android.widget.ImageView", "android.widget.Button"
        };
        for (String cls : tileClasses) {
            try {
                for (WebElement el : driver.findElements(By.className(cls))) {
                    String desc;
                    try {
                        desc = el.getDomAttribute("content-desc");
                    } catch (StaleElementReferenceException e) {
                        continue;
                    }
                    if (desc == null || !desc.contains("\n")) continue;
                    if (desc.contains("Tab ") || desc.matches("(?s)^\\d+\\n.*")) continue;
                    tiles.add(desc.trim());
                }
            } catch (WebDriverException e) {
                System.err.println("[AppUtil] Could not scrape " + cls + " tiles: " + e.getMessage());
            }
        }
        return new java.util.ArrayList<>(tiles);
    }

    /** Renders a scraped tile list for logs/assert messages ("Aris Fan / Living Room"). */
    public static String prettyTiles(java.util.Collection<String> tiles) {
        return tiles.stream()
                .map(t -> t.replace("\n", " / "))
                .collect(java.util.stream.Collectors.joining(", ", "[", "]"));
    }

    // ── Element helpers ───────────────────────────────────────────────────────

    /**
     * Returns true if the element is present AND displayed.
     * <p>
     * FIX H3: catches StaleElementReferenceException in addition to NoSuchElementException.
     * The element can become stale between findElement() and isDisplayed() during any
     * screen transition — previously this threw an uncaught exception, causing false failures.
     */
    public static boolean isElementPresent(AndroidDriver driver, By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    public static WebElement findOptionalElement(AndroidDriver driver, By locator) {
        try {
            return driver.findElement(locator);
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return null;
        }
    }

    /**
     * Polls until the element is present and visible, then returns it.
     * <p>
     * FIX H2: replaced custom busy-poll (which rethrew StaleElementReferenceException
     * immediately) with WebDriverWait + ExpectedConditions, which automatically retries
     * on both NoSuchElementException and StaleElementReferenceException.
     */
    public static WebElement waitForElement(AndroidDriver driver, By locator, long timeoutSec) {
        try {
            return new WebDriverWait(driver, Duration.ofSeconds(timeoutSec))
                    .until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (Exception e) {
            throw new RuntimeException(
                    "Element not found after " + timeoutSec + "s: " + locator, e);
        }
    }

    public static void clickElement(AndroidDriver driver, By locator, String label) {
        try {
            driver.findElement(locator).click();
            System.out.println(label + " clicked.");
        } catch (Exception e) {
            System.err.println("Failed to click " + label + ": " + e.getMessage());
            throw new RuntimeException("Interaction failed: " + label, e);
        }
    }

    /**
     * Clicks the element if it is present, displayed, and enabled.
     * <p>
     * FIX H1: the previous check used getDomAttribute("clickable"), which always
     * returns null on Flutter-rendered elements (Atomberg app is Flutter-based).
     * Boolean.parseBoolean(null) == false, so every click was silently skipped —
     * including logout, tab navigation, and Wi-Fi setup buttons.
     * Replaced with isEnabled() && isDisplayed(), which are standard Selenium
     * element-state checks that work correctly via the UIAutomator2 accessibility tree.
     *
     * @return true if the element was found and clicked; false otherwise
     */
    public static boolean clickIfExists(AndroidDriver driver, By locator) {
        try {
            WebElement el = driver.findElement(locator);
            if (el.isDisplayed() && el.isEnabled()) {
                el.click();
                return true;
            }
        } catch (NoSuchElementException | StaleElementReferenceException ignored) {}
        return false;
    }

    // ── Navigation helpers ────────────────────────────────────────────────────

    /**
     * Navigates to the device-addition screen by tapping the Add FAB.
     * <p>
     * FIX H5: old fallback used Tap.withCoordinates(driver, 540, 1850) — a pixel
     * coordinate hardcoded for one specific device resolution. Replaced with
     * Tap.withPercentage(0.50, 0.95), which taps the bottom-centre of any screen.
     */
    public static void navigateToAddScreen(AndroidDriver driver) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.presenceOfElementLocated(ADD_BUTTON_XPATH))
                    .click();
        } catch (Exception e) {
            System.out.println("Add button not found via locator — using percentage-based fallback.");
            ActionsUtil.Tap.withPercentage(driver, 0.50, 0.95);
        }
        sleep(3000);
    }

    /**
     * Polls until the More-tab (home screen indicator) is visible, then returns.
     * <p>
     * FIX H4: replaced two hardcoded "com.atomberg.app" string literals with the
     * APP_PACKAGE constant from Credentials, so this method stays correct if the
     * package name ever changes.
     */
    public static void confirmOnHomeScreen(AndroidDriver driver) {
        By moreTab = HomeLocators.MORE_TAB;
        long start = System.currentTimeMillis();
        while ((System.currentTimeMillis() - start) < 15_000) {
            if (isElementPresent(driver, moreTab)) {
                System.out.println("On Home Screen.");
                return;
            }
            sleep(500);
        }
        System.err.println("Could not confirm return to Home Screen.");
        ApplicationState state = driver.queryAppState(ATOMBERG_HOME);
        if (state != ApplicationState.RUNNING_IN_FOREGROUND) {
            driver.activateApp(ATOMBERG_HOME);
        }
    }

    /**
     * True when the Atomberg app is currently in the foreground on this device.
     */
    public static boolean isAppInForeground(AndroidDriver driver) {
        try {
            return driver.queryAppState(ATOMBERG_HOME) == ApplicationState.RUNNING_IN_FOREGROUND;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Brings the app to a root/home screen <b>without ever pressing BACK off the
     * app</b>.
     *
     * <p>A blind {@code navigate().back()} loop is unsafe: on a root screen the
     * back press exits the app entirely (the app "closes by itself" while the other
     * phone keeps running). This method presses back at most {@code maxBacks} times
     * and, after every press, checks whether the app is still in the foreground —
     * if a press knocked it out, the app is immediately re-activated and the loop
     * stops. {@link HomeLocators#MORE_TAB} (the bottom nav) is the home marker,
     * because it is present on every root screen including an empty device list.</p>
     */
    public static void ensureAppHome(AndroidDriver driver, int maxBacks) {
        if (!isAppInForeground(driver)) {
            System.out.println("[AppUtil] App not in foreground — re-activating.");
            try { driver.activateApp(ATOMBERG_HOME); } catch (Exception ignored) {}
            sleep(2000);
        }

        for (int i = 0; i < maxBacks; i++) {
            if (isElementPresent(driver, HomeLocators.MORE_TAB)) return;   // already at a root screen
            try { driver.navigate().back(); } catch (Exception ignored) {}
            sleep(1000);

            if (!isAppInForeground(driver)) {
                // That back press left the app — undo it and stop pressing.
                System.out.println("[AppUtil] BACK exited the app — re-activating and stopping.");
                try { driver.activateApp(ATOMBERG_HOME); } catch (Exception ignored) {}
                sleep(2000);
                return;
            }
        }
    }

    /** {@link #ensureAppHome(AndroidDriver, int)} with a sensible default depth. */
    public static void ensureAppHome(AndroidDriver driver) {
        ensureAppHome(driver, 3);
    }

    // ── Wi-Fi provisioning ────────────────────────────────────────────────────

    public static void additionProcess(AndroidDriver driver) {
        clickIfExists(driver, CONTINUE_BUTTON);
        captureScreenshot(driver, "Continue button");
        SearchWiFi(driver, DEFAULT_WIFI_SSID);
    }

    public static void SearchWiFi(AndroidDriver driver, String targetSsid) {
        if (targetSsid == null || targetSsid.isBlank())
            throw new IllegalArgumentException("Wi-Fi SSID cannot be blank");

        ActionsUtil.Tap.withPercentage(driver, 0.20, 0.20);
        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                WebElement ssidField = findOptionalElement(driver,
                        By.xpath("//android.widget.EditText[@text='" + targetSsid + "']"));
                if (ssidField != null) {
                    clickIfExists(driver, CONTINUE_BUTTON);
                    return;
                }
                clearAndSendKeys(driver, WIFI_INPUT_FIELD, targetSsid, "SSID");
                clearAndSendKeys(driver, PASSWORD_INPUT_FIELD, DEFAULT_WIFI_PASSWORD, "Password");
                clickIfExists(driver, CONTINUE_BUTTON);
                sleep(2000);
            } catch (Exception e) {
                System.err.println("Wi-Fi setup attempt " + attempt + " failed: " + e.getMessage());
                sleep(3000);
            }
        }
    }

    private static void clearAndSendKeys(AndroidDriver driver, By locator, String text, String label) {
        WebElement field = waitForElement(driver, locator, 10);
        field.click();
        field.clear();
        field.sendKeys(text);
        System.out.println(label + ": '" + text + "'");
    }
}
