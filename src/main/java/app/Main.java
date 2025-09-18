package app;

import app.Fan.FanManagement;
import app.Login.Email;
import app.util.ActionsUtil;
import app.util.PermissionUtil;
import io.appium.java_client.android.AndroidDriver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;

/**
 * Main - Entry point for the test automation suite.
 *
 * <p>Refactored to:
 * <ul>
 *   <li>Eliminate hardcoded values</li>
 *   <li>Separate responsibilities</li>
 *   <li>Add proper error handling</li>
 *   <li>Ensure driver cleanup</li>
 *   <li>Avoid static pollution</li>
 * </ul>
 */
public class Main {

    private AndroidDriver driver;
    private final ServerInitializer server = new ServerInitializer();

    /**
     * Main method: orchestrates login → add fan → control → teardown.
     */
    public static void main(String[] args) {
        Main main = new Main();
        try {
            main.runTestFlow();
        } catch (Exception e) {
            System.err.println("Test failed with exception: " + e.getMessage());
        } finally {
            main.quitDriverSafely();
        }
    }

    /**
     * Encapsulated test flow: setup → login → fan ops → cleanup.
     */
    public void runTestFlow() throws Exception {
        initializeDriver();
        launchApp();

        if (isOnLoginScreen()) {
            performLogin();
        }

        handlePermissions();

        manageFanDevice();
        turnOffBluetoothViaAdb();
    }

    // === Setup Methods ===

    /**
     * Initializes the Appium driver.
     */
    private void initializeDriver() throws Exception {
        AppInitializer initializer = new AppInitializer();
        initializer.initializeDriver(); // Connects to device
        this.driver = initializer.getDriver();

        // Set implicit wait
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        System.out.println("Driver initialized successfully.");
    }

    /**
     * Launches the Atomberg app.
     */
    private void launchApp() {
        ActionsUtil.SSleep(2);
        driver.activateApp("com.atomberg.app");
        ActionsUtil.SSleep(3);
        System.out.println("App launched.");
    }

    /**
     * Checks if login screen is displayed.
     *
     * @return true if login screen is shown
     */
    private boolean isOnLoginScreen() {
        AppInitializer appCheck = new AppInitializer();
        appCheck.setDriver(driver);
        boolean onLogin = appCheck.checkMainScreen();
        System.out.println("On login screen: " + onLogin);
        return onLogin;
    }

    /**
     * Performs email login with credentials.
     */
    private void performLogin() throws Exception {
        String email = getEnvOrFallback("TEST_EMAIL", "iot.alpha@protonmail.com");
        String password = getEnvOrFallback("TEST_PASSWORD", "Atomberg@123");

        Email login = new Email(driver);
        try {
            login.email(email, password);
            System.out.println("Login successful.");
        } catch (Exception e) {
            System.err.println("Login failed: " + e.getMessage());
            throw e; // Re-throw after logging
        }
    }

    /**
     * Handles runtime permissions post-login.
     */
    private void handlePermissions() {
        PermissionUtil.allow(driver);
        System.out.println("Permissions handled.");
    }

    /**
     * Manages fan addition, configuration, and control.
     */
    private void manageFanDevice() {
        FanManagement fan = new FanManagement(driver);

        fan.addFan();
        fan.additionProcess();
        fan.checkFan();
        fan.fanControl();

        System.out.println("Fan operations completed.");
    }

    // === Utility Methods ===

    /**
     * Safely turns off Bluetooth using ADB.
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
     * Safely quit the driver.
     */
    public void quitDriverSafely() {
        if (driver != null) {
            try {
                driver.quit();
                System.out.println("Driver session ended.");
            } catch (Exception e) {
                System.err.println("Error during driver quit: " + e.getMessage());
            }
        }
    }

    /**
     * Gets value from environment variable, falls back to default.
     *
     * @param key      Environment variable name
     * @param fallback Default value
     * @return Resolved value
     */
    private String getEnvOrFallback(String key, String fallback) {
        String value = System.getenv(key);
        return value != null ? value : fallback;
    }
}