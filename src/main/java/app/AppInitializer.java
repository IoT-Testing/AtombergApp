package app;

import app.util.ActionsUtil;
import app.util.AppUtil;
import app.util.PermissionUtil;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.appmanagement.ApplicationState;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * AppInitializer - Central class to set up the Android driver and launch/login to the Atomberg app.
 *
 * <p>Refactored to:
 * <ul>
 *   <li>Eliminate duplicated driver setup logic</li>
 *   <li>Separate concerns (driver init vs login vs ADB)</li>
 *   <li>Improve error visibility</li>
 *   <li>Externalize constants</li>
 *   <li>Follow clean coding practices</li>
 * </ul>
 */
public class AppInitializer {
    private AndroidDriver atomberg;
    private String osVersion;

    // === Constants ===
    private static final String APP_PACKAGE = "com.atomberg.app";
    private static final String APP_ACTIVITY = "com.atomberg.app.MainActivity";
    private static final String APPIUM_SERVER_URL = "http://127.0.0.1:4723/wd/hub";
    private static final Duration IMPLICIT_WAIT = Duration.ofSeconds(5);
    private static final int ADB_TIMEOUT_SECONDS = 10;

    // === Locators (could move to Page Object later) ===
    private static final By LOGIN_SCREEN_INDICATOR = By.xpath(
            "//android.view.View[@content-desc=\"Experience smart living \\n with Atomberg\"]"
    );
    private static final By EMAIL_LOGIN_BUTTON = By.xpath(
            "(//android.widget.ImageView)[4]" // Simplified from deep hierarchy
    );
    private static final By CONTINUE_BUTTON = By.xpath("//android.widget.Button[@content-desc='Continue']");
    private static final By EDIT_TEXT_FIELD = By.xpath("//android.widget.EditText");

    // === Getters & Setters ===
    public AndroidDriver getDriver() {
        return atomberg;
    }

    public void setDriver(AndroidDriver driver) {
        this.atomberg = driver;
    }

    public String getOsVersion() {
        return osVersion;
    }

    // === Driver Initialization Methods ===

    /**
     * Launches the Atomberg app with default capabilities.
     */
    public void openApp() {
        UiAutomator2Options options = baseOptions()
                .setAppPackage(APP_PACKAGE)
                .setAppActivity(APP_ACTIVITY);
        createDriver(options);
    }

    /**
     * Initializes driver connected to a device only (no app launched).
     */
    public void initializeDriver() {
        UiAutomator2Options options = baseOptions();
        options.setCapability("platformName", "Android");
        options.setCapability("platformVersion", "15");
        createDriver(options);
    }

    /**
     * Initializes driver with dynamic platform version fetched via ADB.
     *
     * @param url  Appium server URL
     * @param text Output containing device ID (e.g., 'adb devices' output line)
     * @throws IOException          If ADB process fails
     * @throws InterruptedException If thread is interrupted
     */
    public void initializeDriverWithURL(URL url, String text) throws IOException, InterruptedException {
        String[] parts = text.trim().split("\\s+");
        if (parts.length == 0) throw new IllegalArgumentException("Invalid device info string");

        String deviceId = parts[parts.length - 1];
        System.out.println("Target Device ID: " + deviceId);

        // Execute ADB command to get OS version
        Process process = Runtime.getRuntime().exec("adb -s " + deviceId + " shell getprop ro.system.build.version.release");
        if (!process.waitFor(10L, TimeUnit.SECONDS)) {
            process.destroy();
            throw new IOException("ADB command timed out.");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }
            osVersion = output.toString().trim();
        }

        System.out.println("Detected OS Version: " + osVersion);

        UiAutomator2Options options = baseOptions();
                options.setCapability("platformName", "Android");
                options.setCapability("platformVersion", osVersion);
                options.setCapability("appium:udid", deviceId);

        createDriver(url, options);
    }

    /**
     * Initializes driver without launching any specific app.
     *
     * @param url Appium server URL
     */
    public void initializeDriverWithURL(URL url) {
        UiAutomator2Options options = baseOptions();
        options.setCapability("platformName", "Android");
        createDriver(url, options);
    }

    /**
     * Opens the Atomberg app at a given Appium server URL.
     *
     * @param url Appium server endpoint
     */
    public void openAppWithURL(URL url) {
        UiAutomator2Options options = baseOptions()
                .setAppPackage(APP_PACKAGE)
                .setAppActivity(APP_ACTIVITY);
        createDriver(url, options);
    }

    // === Internal Helpers ===

    /**
     * Returns base UiAutomator2Options shared across all initializations.
     */
    private UiAutomator2Options baseOptions() {
        try {
            return new UiAutomator2Options().merge(new UiAutomator2Options()
                    .setAdbExecTimeout(Duration.ofMinutes(5)) // Prevent early timeout
                    .setEnsureWebviewsHavePages(true));
        } catch (Exception e) {
            System.err.println("Failed to configure base options: " + e.getMessage());
            return new UiAutomator2Options();
        }
    }

    /**
     * Creates driver with options and default Appium server.
     *
     * @param options Driver options
     */
    private void createDriver(UiAutomator2Options options) {
        createDriver(parseUrl(APPIUM_SERVER_URL), options);
    }

    /**
     * Creates driver with custom URL and options.
     *
     * @param url     Appium server URL
     * @param options Driver options
     */
    private void createDriver(URL url, UiAutomator2Options options) {
        if (url == null) throw new IllegalArgumentException("Appium server URL cannot be null");
        if (options == null) throw new IllegalArgumentException("Driver options cannot be null");

        try {
            atomberg = new AndroidDriver(url, options);
            atomberg.manage().timeouts().implicitlyWait(IMPLICIT_WAIT);
            System.out.println("Driver created successfully.");
        } catch (Exception e) {
            System.err.println("Failed to create AndroidDriver: " + e.getMessage());
            throw new RuntimeException("Driver initialization failed", e);
        }
    }

    /**
     * Parses a fixed or variable URL string safely.
     *
     * @param urlString URL as string
     * @return Parsed URL object
     */
    private URL parseUrl(String urlString) {
        try {
            return new URL(urlString);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid URL: " + urlString, e);
        }
    }

    // === App State & Navigation ===

    /**
     * Checks if login screen is displayed.
     *
     * @return true if login screen is visible, false otherwise
     */
    public boolean checkMainScreen() {
        ApplicationState appState = atomberg.queryAppState(APP_PACKAGE);

        if (appState == ApplicationState.RUNNING_IN_FOREGROUND ||
                appState == ApplicationState.RUNNING_IN_BACKGROUND) {

            ActionsUtil.SSleep(5); // Wait for UI to stabilize

            WebElement mainScreenIndicator = findOptionalElement(LOGIN_SCREEN_INDICATOR);
            if (mainScreenIndicator != null) {
                System.out.println("Login Screen Displayed");
                return true;
            } else {
                System.out.println("Already logged in");
                return false;
            }
        } else {
            // Try activating app
            atomberg.activateApp(APP_PACKAGE);
            ActionsUtil.SSleep(5);
            WebElement indicator = findOptionalElement(LOGIN_SCREEN_INDICATOR);
            System.out.println(indicator != null ? "Login Screen Displayed" : "Already logged in");
            return indicator != null;
        }
    }

    // === Login Methods ===

    /**
     * Performs full login flow with email and password.
     *
     * @param email User email
     * @param pass  User password
     */
    public void login(String email, String pass) {
        if (email == null || pass == null) {
            throw new IllegalArgumentException("Email and password must not be null");
        }

        clickEmailLoginButton();
        enterEmail(email);
        clickContinue();

        ActionsUtil.sleep(1000); // Allow transition

        enterPassword(pass);
        clickContinue();

        ActionsUtil.sleep(5000); // Allow login processing

        PermissionUtil.allow(atomberg); // Handle permissions

        dismissAlexaPopupIfPresent(); // Optional popup
    }

    /**
     * Hardcoded login for quick testing.
     */
    public void email() {
        login("hiwitaw422@wuzak.com", "Atomberg@1234");
    }

    // === Login Helpers ===

    private void clickEmailLoginButton() {
        clickElement(EMAIL_LOGIN_BUTTON, "Email Login Button");
        AppUtil.captureScreenshot(atomberg);
    }

    private void enterEmail(String email) {
        WebElement field = waitForElement(EDIT_TEXT_FIELD, 10);
        field.click();
        field.sendKeys(email);
        System.out.println("Email Entered: " + email);
        AppUtil.captureScreenshot(atomberg);
    }

    private void enterPassword(String password) {
        WebElement field = waitForElement(EDIT_TEXT_FIELD, 10);
        field.click();
        field.sendKeys(password);
        System.out.println("Password entered...");
        AppUtil.captureScreenshot(atomberg);
    }

    private void clickContinue() {
        clickElement(CONTINUE_BUTTON, "Continue Button");
        AppUtil.captureScreenshot(atomberg);
        System.out.println("Continue...");
    }

    private void dismissAlexaPopupIfPresent() {
        List<WebElement> views = atomberg.findElements(By.className("android.view.View"));
        List<WebElement> labeledElements = views.stream()
                .filter(el -> el.getDomAttribute("content-desc") != null)
                .collect(Collectors.toList());

        for (WebElement el : labeledElements) {
            if (Objects.equals(el.getDomAttribute("content-desc"),
                    "Use Alexa to control your smart fan(s) with voice")) {
                try {
                    atomberg.findElement(By.xpath("//android.widget.Button[@content-desc='Cancel']")).click();
                    System.out.println("Alexa popup dismissed.");
                    break;
                } catch (Exception e) {
                    System.err.println("Failed to dismiss Alexa popup: " + e.getMessage());
                }
            }
        }
    }

    // === Utility Methods ===

    /**
     * Safely finds element without throwing exception.
     *
     * @param locator Element locator
     * @return Found element or null
     */
    private WebElement findOptionalElement(By locator) {
        try {
            return atomberg.findElement(locator);
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    /**
     * Clicks element with logging.
     *
     * @param locator By strategy
     * @param label   Action label
     */
    private void clickElement(By locator, String label) {
        try {
            atomberg.findElement(locator).click();
            System.out.println(label + " clicked.");
        } catch (Exception e) {
            System.err.println("Failed to click " + label + ": " + e.getMessage());
            throw new RuntimeException("Interaction failed: " + label, e);
        }
    }

    /**
     * Waits up to N seconds for element to be present.
     *
     * @param locator  Element locator
     * @param timeoutSec Timeout in seconds
     * @return WebElement if found
     */
    private WebElement waitForElement(By locator, long timeoutSec) {
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < timeoutSec * 1000) {
            try {
                return atomberg.findElement(locator);
            } catch (NoSuchElementException ignored) {
                ActionsUtil.sleep(500);
            }
        }
        throw new RuntimeException("Element not found after " + timeoutSec + " seconds: " + locator);
    }
}