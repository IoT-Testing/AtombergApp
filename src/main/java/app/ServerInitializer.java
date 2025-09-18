package app;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.openqa.selenium.remote.service.DriverService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * ServerInitializer - Manages the lifecycle of the Appium server.
 *
 * <p>Refactored to:
 * <ul>
 *   <li>Remove hardcoded paths</li>
 *   <li>Add cross-platform support</li>
 *   <li>Improve error handling</li>
 *   <li>Support flexible configuration</li>
 * </ul>
 */
public class ServerInitializer {

    public AppiumDriverLocalService service;
    private static final int DEFAULT_PORT = 4723;

    /**
     * Starts the Appium server using a free port.
     * Tries to auto-detect Appium installation path.
     */
    public void startServer() {
        if (isServerRunning()) {
            System.out.println("Appium server is already running at: " + service.getUrl());
            return;
        }

        try {
            AppiumServiceBuilder builder = configureServiceBuilder();
            service = builder.build();

            service.start();

            if (!service.isRunning()) {
                throw new RuntimeException("Appium service started but not detected as running.");
            }

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
            } catch (Exception e) {
                System.err.println("Error while stopping Appium server: " + e.getMessage());
            }
        } else {
            System.out.println("Appium Server was not running.");
        }
    }

    /**
     * Checks if the server is currently active.
     *
     * @return true if running
     */
    public boolean isServerRunning() {
        return service != null && service.isRunning();
    }

    // === Internal Helpers ===

    /**
     * Configures the Appium service builder with platform-aware settings.
     */
    private AppiumServiceBuilder configureServiceBuilder() {
        AppiumServiceBuilder builder = new AppiumServiceBuilder()
                .usingAnyFreePort()
                .withArgument(() -> "--base-path", "/wd/hub")
                .withLogFile(getLogFile()); // Optional log output

        // Try to auto-detect Appium main.js path
        File appiumJs = detectAppiumJsPath();
        if (appiumJs != null && appiumJs.exists()) {
            builder.withAppiumJS(appiumJs);
            System.out.println("Using Appium JS: " + appiumJs.getAbsolutePath());
        } else {
            System.out.println("⚠️ Appium JS not found at expected location. Using system-installed Appium (PATH).");
            // Let AppiumDriverLocalService find it via PATH
        }

        return builder;
    }

    /**
     * Attempts to detect the Appium main.js path based on OS.
     *
     * @return Detected File or null
     */
    private File detectAppiumJsPath() {
        String os = System.getProperty("os.name").toLowerCase();
        Path appiumJsPath;

        if (os.contains("win")) {
            // Windows: Common NPM path
            String userProfile = System.getenv("USERPROFILE");
            if (userProfile == null) return null;
            appiumJsPath = Paths.get(userProfile, "AppData", "Roaming", "npm", "node_modules", "appium", "build", "lib", "main.js");
        } else {
            // macOS/Linux: Usually in global node_modules
            appiumJsPath = Paths.get("/usr", "local", "lib", "node_modules", "appium", "build", "lib", "main.js");

            // Fallback: Check if 'appium' is in PATH
            if (!Files.exists(appiumJsPath)) {
                try {
                    Process which = Runtime.getRuntime().exec("which appium");
                    java.util.Scanner scanner = new java.util.Scanner(which.getInputStream()).useDelimiter("\\A");
                    if (scanner.hasNext()) {
                        String appiumBin = scanner.next().trim();
                        // Resolve from bin to main.js (common symlink structure)
                        appiumJsPath = Paths.get(appiumBin).getParent().getParent()
                                .resolve("lib").resolve("node_modules").resolve("appium").resolve("build").resolve("lib").resolve("main.js");
                    }
                    scanner.close();
                } catch (IOException ignored) {}
            }
        }

        return Files.exists(appiumJsPath) ? appiumJsPath.toFile() : null;
    }

    /**
     * Returns log file for Appium server output (optional).
     *
     * @return Log file
     */
    private File getLogFile() {
        String reportDir = System.getProperty("user.dir") + "/logs/appium/";
        File dir = new File(reportDir);
        if (!dir.exists()) dir.mkdirs();

        String timestamp = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return new File(dir, "appium_log_" + timestamp + ".log");
    }
}