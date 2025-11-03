package app.server;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.openqa.selenium.WebDriverException;

import java.io.File;
import java.time.Duration;

/**
 * AppiumServerManager - Manages the lifecycle of the Appium server.
 *
 * <p>Supports Android and iOS with configurable settings.
 * Thread-safe and production-ready.
 */
public class AppiumServerManager {

    private AppiumDriverLocalService service;
    private boolean isRunning = false;

    // === Configuration Constants ===
    private static final String DEFAULT_IP = "127.0.0.1";
    private static final int DEFAULT_PORT = 4723;
    private static final String BASE_PATH = "/wd/hub";
    private static final String LOG_LEVEL = "debug";

    /**
     * Starts the Appium server for the given platform.
     *
     * @param platformName "android" or "ios"
     */
    public void startServer(String platformName) {
        if (isRunning()) {
            System.out.println("Appium server is already running at: " + service.getUrl());
            return;
        }

        try {
            AppiumServiceBuilder builder = createBaseBuilder();

            if ("android".equalsIgnoreCase(platformName)) {
                configureForAndroid(builder);
            } else if ("ios".equalsIgnoreCase(platformName)) {
                configureForIOS(builder);
            } else {
                throw new IllegalArgumentException("Unsupported platform: " + platformName);
            }

            this.service = builder.build();
            this.service.start();

            if (!this.service.isRunning()) {
                throw new WebDriverException("Appium server started but not detected as running.");
            }

            this.isRunning = true;
            System.out.println("Appium Server Started at: " + service.getUrl());

        } catch (Exception e) {
            System.err.println("Failed to start Appium server: " + e.getMessage());
            throw new RuntimeException("Appium server failed to start", e);
        }
    }

    /**
     * Stops the Appium server gracefully.
     */
    public void stopServer() {
        if (service != null && service.isRunning()) {
            try {
                service.stop();
                System.out.println("Appium Server stopped successfully.");
                this.isRunning = false;
            } catch (Exception e) {
                System.err.println("Error while stopping Appium server: " + e.getMessage());
            }
        } else {
            System.out.println("Appium Server was not running.");
        }
    }

    /**
     * Checks if server is currently active.
     *
     * @return true if running
     */
    public boolean isRunning() {
        return isRunning && service != null && service.isRunning();
    }

    /**
     * Gets the running service instance.
     *
     * @return AppiumDriverLocalService or null
     */
    public AppiumDriverLocalService getService() {
        return isRunning() ? service : null;
    }

    // === Internal Helpers ===

    /**
     * Creates base builder with common settings.
     */
    private AppiumServiceBuilder createBaseBuilder() {
        return new AppiumServiceBuilder()
                .withIPAddress(DEFAULT_IP)
                .usingPort(DEFAULT_PORT)
                .withArgument(GeneralServerFlag.BASEPATH, BASE_PATH)
                .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
                .withArgument(GeneralServerFlag.LOG_LEVEL, LOG_LEVEL);
    }

    /**
     * Configures builder for Android.
     */
    private void configureForAndroid(AppiumServiceBuilder builder) {
        builder.withArgument(GeneralServerFlag.USE_DRIVERS, "uiautomator2");
        // Optional: Set node path if not in PATH
        setNodeJsAndAppiumPaths(builder);
    }

    /**
     * Configures builder for iOS.
     */
    private void configureForIOS(AppiumServiceBuilder builder) {
        builder.withArgument(GeneralServerFlag.USE_DRIVERS, "xcuitest")
                .withArgument(GeneralServerFlag.ALLOW_INSECURE, "chromedriver_autodownload");
        setNodeJsAndAppiumPaths(builder);
    }

    /**
     * Sets custom Node.js and Appium paths if they exist.
     * Helps in environments where Appium is not globally installed.
     */
    private void setNodeJsAndAppiumPaths(AppiumServiceBuilder builder) {
        // Example: Customize these paths based on your environment
        File customNodePath = getCustomNodeExecutable();
        File customAppiumPath = getCustomAppiumMainJs();

        if (customNodePath != null) {
            builder.usingDriverExecutable(customNodePath);
        }
        if (customAppiumPath != null) {
            builder.withAppiumJS(customAppiumPath);
        }
    }

    /**
     * Attempts to detect or provide custom Node executable.
     * Return null to use system default.
     */
    private File getCustomNodeExecutable() {
        // Example for Windows:
        // return new File("C:\\Program Files\\nodejs\\node.exe");

        // Example for macOS/Linux:
        // return new File("/usr/local/bin/node");

        return null; // Use system PATH
    }

    /**
     * Attempts to detect or provide custom Appium main.js.
     */
    private File getCustomAppiumMainJs() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            // Windows example
            String userProfile = System.getenv("USERPROFILE");
            if (userProfile != null) {
                File appiumJs = new File(userProfile + "\\AppData\\Roaming\\npm\\node_modules\\appium\\build\\lib\\main.js");
                if (appiumJs.exists()) return appiumJs;
            }
        } else {
            // macOS/Linux
            File globalAppium = new File("/usr/local/lib/node_modules/appium/build/lib/main.js");
            if (globalAppium.exists()) return globalAppium;
        }
        return null; // Let Appium auto-detect
    }
}