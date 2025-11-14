package app.util;

import app.resources.Locators.Android.HomeLocators;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.appmanagement.ApplicationState;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static app.resources.Locators.Android.FanLocators.ADD_BUTTON_XPATH;
import static app.util.ActionsUtil.sleep;

/**
 * Utility class providing common helper functions for test automation.
 *
 * <p>Refactored to:
 * <ul>
 *   <li>Improve separation of concerns</li>
 *   <li>Externalize hardcoded values</li>
 *   <li>Replace fragile coordinate taps where possible</li>
 *   <li>Add proper error handling</li>
 *   <li>Improve readability and maintainability</li>
 * </ul>
 */
public class AppUtil {

    // === Directory Constants ===
    private static final String SCREENSHOT_DIR = System.getProperty("user.dir") + File.separator + "screenshots" + File.separator;
    private static final File DIR_FILE = new File(SCREENSHOT_DIR);
    private static WebDriverWait wait;

    // === Wi-Fi Constants ===
//    private static final String DEFAULT_WIFI_SSID = "Room-C4";
    private static final String DEFAULT_WIFI_SSID = "Better_Together";
    private static final String DEFAULT_WIFI_PASSWORD = "123@ToMb^rg#2425";
//    private static final String DEFAULT_WIFI_PASSWORD = "75982256";

    // === Locator Constants ===
    private static final By ROOM_NAMES = By.xpath("//android.widget.ImageView[@content-desc]");
    private static final By WIFI_INPUT_FIELD = By.xpath("//android.widget.EditText[1]");
    private static final By PASSWORD_INPUT_FIELD = By.xpath("//android.widget.EditText[2]");
    private static final By CONTINUE_BUTTON = By.xpath("//android.widget.Button[@content-desc='Continue']");
    private static final String FALLBACK_PASSWORD = "123@ToMb^rg#2425";
//    private static final String FALLBACK_PASSWORD = "75982256";

    // Ensure screenshot directory exists
    static {
        if (!DIR_FILE.exists() && !DIR_FILE.mkdirs()) {
            System.err.println("Failed to create screenshots directory: " + SCREENSHOT_DIR);
        }
    }


    /**
     * Captures a screenshot and saves it with a timestamp.
     *
     * @param driver AndroidDriver instance
     */
    public static void captureScreenshot(AndroidDriver driver, String name) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filename = name +" "+ timestamp + ".png";
        String destinationPath = SCREENSHOT_DIR + filename;

        try {
            File srcFile = driver.getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(srcFile, new File(destinationPath));
            System.out.println("Screenshot saved: " + destinationPath);
        } catch (IOException e) {
            System.err.println("Failed to save screenshot at " + destinationPath + ": " + e.getMessage());
        } catch (WebDriverException e) {
            System.err.println("Driver failed to capture screenshot: " + e.getMessage());
        }
    }

    /**
     * Performs room selection flow during device setup.
     * Selects predefined rooms and clicks Continue.
     * @param driver AndroidDriver instance
     */
    public static void additionProcess(AndroidDriver driver) {
//        String[] roomNames = {"Master Bedroom", "Guest Room", "Kitchen", "Common Bedroom",
//                "Lobby", "Balcony", "Living Room"};
//
//        for (String room : roomNames) {
//            selectRoom(driver, room);
//            captureScreenshot(driver,room);
//        }
        clickIfExists(driver, CONTINUE_BUTTON);
        captureScreenshot(driver,"Continue button");

        SearchWiFi(driver, DEFAULT_WIFI_SSID);
    }

    /**
     * Selects a room by its content description.
     *
     * @param driver AndroidDriver
     * @param roomName Name of the room to select
     */
    private static void selectRoom(AndroidDriver driver, String roomName) {
        By roomLocator = By.xpath("//android.widget.ImageView[@content-desc='" + roomName + "']");
        clickElement(driver, roomLocator, "Room: " + roomName);
    }

    /**
     * Handles Wi-Fi connection flow: enters SSID and password, then continues.
     *
     * @param driver      AndroidDriver
     * @param targetSsid  Target Wi-Fi network name
     */
    public static void SearchWiFi(AndroidDriver driver, String targetSsid) {
        if (targetSsid == null || targetSsid.isEmpty()) {
            throw new IllegalArgumentException("Wi-Fi SSID cannot be null or empty");
        }

        ActionsUtil.Tap.withPercentage(driver, 0.20, 0.20); // Focus field

        for (int attempt = 1; attempt <= 3; attempt++) { // Limit retries
            try {
                WebElement ssidField = findOptionalElement(driver, By.xpath("//android.widget.EditText[@text='" + targetSsid + "']"));
                if (ssidField != null) {
                    System.out.println("Wi-Fi network '" + targetSsid + "' already entered.");
//                    enterPasswordAndContinue(driver, DEFAULT_WIFI_PASSWORD);
                    clickIfExists(driver, CONTINUE_BUTTON);
                    return;
                } else {
                    enterSsidAndPassword(driver, targetSsid, FALLBACK_PASSWORD);
                    sleep(2000); // Allow UI update
                }
            } catch (Exception e) {
                System.err.println("Error during Wi-Fi setup (attempt " + attempt + "): " + e.getMessage());
                sleep(3000);
            }
        }

        System.err.println("Failed to configure Wi-Fi after 3 attempts.");
    }

    /**
     * Enters SSID and fallback password.
     *
     * @param driver AndroidDriver
     * @param ssid   Network name
     * @param pwd    Password to use
     */
    private static void enterSsidAndPassword(AndroidDriver driver, String ssid, String pwd) {
        clearAndSendKeys(driver, WIFI_INPUT_FIELD, ssid, "SSID Input");
        clearAndSendKeys(driver, PASSWORD_INPUT_FIELD, pwd, "Password Input");
        clickIfExists(driver, CONTINUE_BUTTON);
    }

    /**
     * Enters password and clicks Continue.
     *
     * @param driver AndroidDriver
     * @param pwd    Password to enter
     */
    private static void enterPasswordAndContinue(AndroidDriver driver, String pwd) {
        clearAndSendKeys(driver, PASSWORD_INPUT_FIELD, pwd, "Password Input");
        clickIfExists(driver, CONTINUE_BUTTON);
    }

    /**
     * Clears field and sends keys with logging.
     *
     * @param driver   Driver instance
     * @param locator  Field locator
     * @param text     Text to send
     * @param label    Action label for logs
     */
    private static void clearAndSendKeys(AndroidDriver driver, By locator, String text, String label) {
        WebElement field = waitForElement(driver, locator, 10);
        field.click();
        field.clear();
        field.sendKeys(text);
        System.out.println(label + ": '" + text + "'");
    }
    public static boolean isElementPresent(AndroidDriver driver, By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Navigates to Add screen using fallback tap if needed.
     */
    public static void navigateToAddScreen(AndroidDriver driver) {
        try {
            WebElement addButton = wait.until(ExpectedConditions.presenceOfElementLocated(ADD_BUTTON_XPATH));
            addButton.click();
        } catch (TimeoutException | NullPointerException e) {
            System.out.println("Add button not found via XPath, using coordinate fallback.");
            ActionsUtil.Tap.withCoordinates(driver, 540, 1850); // Fallback tap
        }
        sleep(3);
    }

    /**
    Perform Phone Bluetooth On-Off
     */

    public void turnOffBluetoothViaAdb() {
        Process process = null;
        BufferedReader reader = null;
        try {
            process = Runtime.getRuntime().exec("adb shell am broadcast -a android.bluetooth.adapter.action.REQUEST_DISABLE");
            process.waitFor();

            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[ADB] " + line);
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Failed to disable Bluetooth: " + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) { }
            }
            if (process != null) {
                process.destroyForcibly();
            }
        }
    }

    /**
     * Clicks continue button with retry.
     */

    /**
     * Safely finds element without throwing exception.
     */
    public static WebElement findOptionalElement(AndroidDriver driver, By locator) {
        try {
            return driver.findElement(locator);
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    /**
     * Waits up to N seconds for element to be present.
     */
    public static WebElement waitForElement(AndroidDriver driver, By locator, long timeoutSec) {
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < timeoutSec * 1000) {
            try {
                return driver.findElement(locator);
            } catch (NoSuchElementException ignored) {
                sleep(500);
            }
        }
        throw new RuntimeException("Element not found after " + timeoutSec + "s: " + locator);
    }

    /**
     * Clicks an element with logging.
     */
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
     * Generates a random digit (0–9).
     *
     * @return Random integer between 0 and 9
     */
    public static int Array() {
        int[] digits = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        return digits[new Random().nextInt(digits.length)];
    }

    /**
     * Simulates physical number pad input using screen coordinates.
     * <p>
     * Note: Fragile across screen sizes. Prefer actual UI elements when available.
     */
    public static class NumberPad {
        // Screen-relative coordinates (assumed for 1080x2340 resolution)
        private static final int ROW1_Y = 1725;
        private static final int ROW2_Y = 1945;
        private static final int ROW3_Y = 2100;
        private static final int ROW4_Y = 2250;

        private static final int COL1_X = 240;
        private static final int COL2_X = 530;
        private static final int COL3_X = 810;

        public void one(AndroidDriver driver) { tap(driver, COL1_X, ROW1_Y); }
        public void two(AndroidDriver driver) { tap(driver, COL2_X, ROW1_Y); }
        public void three(AndroidDriver driver) { tap(driver, COL3_X, ROW1_Y); }
        public void four(AndroidDriver driver) { tap(driver, COL1_X, ROW2_Y); }
        public void five(AndroidDriver driver) { tap(driver, COL2_X, ROW2_Y); }
        public void six(AndroidDriver driver) { tap(driver, COL3_X, ROW2_Y); }
        public void seven(AndroidDriver driver) { tap(driver, COL1_X, ROW3_Y); }
        public void eight(AndroidDriver driver) { tap(driver, COL2_X, ROW3_Y); }
        public void nine(AndroidDriver driver) { tap(driver, COL3_X, ROW3_Y); }
        public void zero(AndroidDriver driver) { tap(driver, COL2_X, ROW4_Y); }
        public void clear(AndroidDriver driver) { tap(driver, COL1_X, ROW4_Y); }
        public void done(AndroidDriver driver) { tap(driver, COL3_X, ROW4_Y); }

        private void tap(AndroidDriver driver, int x, int y) {
            ActionsUtil.Tap.withCoordinates(driver, x, y);
        }
    }

    /**
     * Retrieves device market name via ADB command.
     *
     * @throws IOException          If process fails
     * @throws InterruptedException If thread is interrupted
     */
    public static void device() throws IOException, InterruptedException {
        Process process = null;
        BufferedReader reader = null;
        try {
            process = Runtime.getRuntime().exec("adb shell getprop ro.product.marketname");
            if (!process.waitFor(10L, TimeUnit.SECONDS)) {
                process.destroy();
                throw new IOException("ADB command timed out.");
            }

            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String marketName = reader.readLine();

            String result = marketName != null ? marketName.trim() : "Not available";
            System.out.println("Device Market Name: " + result);

        } catch (IOException | InterruptedException e) {
            System.err.println("ADB execution failed: " + e.getMessage());
            throw e;
        } finally {
            if (reader != null) reader.close();
            if (process != null) process.destroyForcibly();
        }
    }
    // === Helper Methods ===


    /**
     * Safely clicks element if present and displayed.
     */
    public static boolean clickIfExists(AndroidDriver driver, By locator) {
        try {
            WebElement el = driver.findElement(locator);
            if (el.isDisplayed() && Boolean.parseBoolean(el.getDomAttribute("clickable"))) {
                el.click();
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * Confirms that app has returned to Home Screen
     */
    public static void confirmOnHomeScreen(AndroidDriver driver) {
//        By moreTab = By.xpath("//android.view.View[@content-desc=\"Home\"]");
        By moreTab = HomeLocators.MORE_TAB;

        //TODO : For Guest mode Use the above locator, While for Account Use app.resources.Locators.Android.HomeLocators.MORE_TAB.

        long start = System.currentTimeMillis();

        while ((System.currentTimeMillis() - start) < 10_000) {
            if (isElementPresent(driver, moreTab)) {
                System.out.println("🏠 On Home Screen.");
                return;
            }
            sleep(500);
        }
        System.err.println("⚠️ Could not confirm return to Home Screen.");
        if(!isElementPresent(driver,moreTab)){
            ApplicationState state = driver.queryAppState("com.atomberg.app");
            if(!(state==ApplicationState.RUNNING_IN_FOREGROUND)){
                driver.activateApp("com.atomberg.app");
            }
        }
    }
}