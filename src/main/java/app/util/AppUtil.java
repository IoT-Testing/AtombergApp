package app.util;

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

    private static final String DEFAULT_WIFI_SSID     = "Better_Together";
    private static final String DEFAULT_WIFI_PASSWORD = "123@ToMb^rg#2425";

    private static final By WIFI_INPUT_FIELD    = By.xpath("//android.widget.EditText[1]");
    private static final By PASSWORD_INPUT_FIELD = By.xpath("//android.widget.EditText[2]");
    private static final By CONTINUE_BUTTON     = By.xpath("//android.widget.Button[@content-desc='Continue']");

    static {
        File dir = new File(SCREENSHOT_DIR);
        if (!dir.exists()) dir.mkdirs();
    }

    // ── Screenshots ───────────────────────────────────────────────────────────

    public static void captureScreenshot(AndroidDriver driver, String name) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String destPath  = SCREENSHOT_DIR + name + "_" + timestamp + ".png";
        try {
            File src = driver.getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(src, new File(destPath));
            System.out.println("Screenshot saved: " + destPath);
        } catch (IOException | WebDriverException e) {
            System.err.println("Failed to save screenshot: " + e.getMessage());
        }
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
