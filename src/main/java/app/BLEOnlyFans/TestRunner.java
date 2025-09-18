package app.BLEOnlyFans;


import app.AppInitializer;
import app.Login.Email;
import app.ServerInitializer;
import app.util.ActionsUtil;
import app.util.PermissionUtil;
import io.appium.java_client.android.AndroidDriver;
import java.time.Duration;

public class TestRunner {

    private AndroidDriver driver;
    private final ServerInitializer server = new ServerInitializer();

    public static void main(String[] args) {
        TestRunner main = new TestRunner();
        try {
            main.runTestFlow();
        } catch (Exception e) {
            System.err.println("Test failed with exception: " + e.getMessage());
        } finally {
            main.quitDriverSafely();
        }
    }

    public void runTestFlow() throws Exception {
        startAppiumServer();
        initializeDriver();
//
        System.out.println("Attempt Number   | Action ID          | Iteration  | Status");
        System.out.println("-----------------|--------------------|------------|--------");

        OpenAndControl control = new OpenAndControl(driver);

        for (int i = 1; i <= 20; i++) { // 1-based attempt number as shown in your table
            control.runOneAttempt(i);
        }
    }

    /**
     * Starts Appium server.
     */
    private void startAppiumServer() {
        server.startServer();
        System.out.println("Appium server started.");
    }

    // === Setup Methods ===

    private void initializeDriver() throws Exception {
        AppInitializer initializer = new AppInitializer();
        initializer.initializeDriver(); // Connects to device
        this.driver = initializer.getDriver();

        // Set implicit wait
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        System.out.println("Driver initialized successfully.");
    }


    private void launchApp() {
        ActionsUtil.SSleep(2);
        driver.activateApp("com.atomberg.app");
        ActionsUtil.SSleep(3);
        System.out.println("App launched.");
    }

    private boolean isOnLoginScreen() {
        AppInitializer appCheck = new AppInitializer();
        appCheck.setDriver(driver);
        boolean onLogin = appCheck.checkMainScreen();
        System.out.println("On login screen: " + onLogin);
        return onLogin;
    }

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

    private void handlePermissions() {
        PermissionUtil.allow(driver);
        System.out.println("Permissions handled.");
    }


    // === Utility Methods ===

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

    private String getEnvOrFallback(String key, String fallback) {
        String value = System.getenv(key);
        return value != null ? value : fallback;
    }
}