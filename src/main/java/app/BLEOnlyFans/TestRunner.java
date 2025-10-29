package app.BLEOnlyFans;

import app.ServerInitializer;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import java.net.URL;
import java.time.Duration;
import java.util.Scanner;

import static app.util.AppUtil.confirmOnHomeScreen;

public class TestRunner {
    public AndroidDriver driver;
    private final ServerInitializer server = new ServerInitializer();
    private static final int DEFAULT_ATTEMPTS = 20;

    public static void main(String[] args) {
        TestRunner main = new TestRunner();
        try {
            main.runTestFlow();
        } catch (Exception e) {
            System.err.println("Test failed with exception: " + e.getMessage());
            e.printStackTrace();
        } finally {
            main.quitDriverSafely();
            FirmwareVersionChecker.closeCSV(); // Properly close CSV file
        }
    }

    /**
     * Starts Appium server.
     */
    private void startAppiumServer() {
        server.startServer();
        System.out.println("🚀 Appium server started.");
    }

    public void runTestFlow() throws Exception {
        startAppiumServer();
        URL url = server.service.getUrl();
        initializeDriverWithURL(url);

        driver.activateApp("com.atomberg.app");
        sleep(4000);

        // Get number of attempts from user

        // Initialize CSV with header (this will be done in FirmwareVersionChecker)
        System.out.println("\n📊 CSV Log Format:");
        System.out.println("Attempt Number,Action ID,Iteration,Status,Updated Version");
        System.out.println("--------------------------------------------------");

        // Run test flow for specified number of attempts
        for (int i = 1; i <= 20; i++) {
            System.out.println("\n=== ATTEMPT #" + i + " ===");

            try {
                // Ensure we're on home screen
                confirmOnHomeScreen(driver);
                sleep(1000);

                // Execute the full firmware verification and pause-resume sequence
                FirmwareVersionChecker checker = new FirmwareVersionChecker(driver);
                checker.runSequentialFirmwareUpdates();
                System.out.println("✅ Attempt #" + i + " completed successfully");

            } catch (Exception e) {
                System.err.println("❌ Error during attempt #" + i + ": " + e.getMessage());
                // Continue with next attempt even if current fails
            }

            // Return to home screen for next attempt
            driver.navigate().back();
            sleep(1000);
            confirmOnHomeScreen(driver);
            sleep(1000);
        }

        System.out.println("\n✅ All 20 attempts completed!");
        System.out.println("📊 CSV report saved to: firmware_test_results_*.csv");
    }

    /**
     * Gets number of attempts from user input (with default)
     */
    private int getNumberOfAttempts() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter number of test attempts (default 20): ");

        try {
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return DEFAULT_ATTEMPTS;
            }
            return Integer.parseInt(input);
        } catch (Exception e) {
            System.out.println("⚠️ Invalid input. Using default of " + DEFAULT_ATTEMPTS + " attempts.");
            return DEFAULT_ATTEMPTS;
        }
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

    private UiAutomator2Options baseOptions() {
        try {
            return new UiAutomator2Options().merge(new UiAutomator2Options()
                    .setAdbExecTimeout(Duration.ofMinutes(5)) // Prevent early timeout
                    .setEnsureWebviewsHavePages(true)
                    .setAutoGrantPermissions(true) // Automatically grant permissions
                    .setNoReset(false)); // Clear app data between runs
        } catch (Exception e) {
            System.err.println("Failed to configure base options: " + e.getMessage());
            return new UiAutomator2Options();
        }
    }

    private void createDriver(URL url, UiAutomator2Options options) {
        if (url == null) throw new IllegalArgumentException("Appium server URL cannot be null");
        if (options == null) throw new IllegalArgumentException("Driver options cannot be null");

        try {
            driver = new AndroidDriver(url, options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            System.out.println("✅ Driver created successfully.");
        } catch (Exception e) {
            System.err.println("❌ Failed to create AndroidDriver: " + e.getMessage());
            throw new RuntimeException("Driver initialization failed", e);
        }
    }

    // === Utility Methods ===

    public void quitDriverSafely() {
        if (driver != null) {
            try {
                System.out.println("\n🧹 Cleaning up resources...");
                driver.quit();
                System.out.println("✅ Driver session ended.");
            } catch (Exception e) {
                System.err.println("⚠️ Error during driver quit: " + e.getMessage());
            }
        }
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}