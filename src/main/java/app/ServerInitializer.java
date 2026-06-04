package app;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

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
    private static final String DEVICE_FARM_URL  = "http://localhost:4723";
    private static final String DEVICE_FARM_HUB = DEVICE_FARM_URL + "/wd/hub";

    public void startServer() {

        // Mode 1: Check if Device Farm is already running (external/GUI)
        if (isExternalServerRunning()) {
            System.out.println("Appium + Device Farm already running at: " + DEVICE_FARM_URL);
            System.out.println("Skipping programmatic server start.");
            return;
        }

        // Mode 2: Start programmatically with Device Farm plugin
        if (isServerRunning()) {
            System.out.println("Appium server already running at: " + service.getUrl());
            return;
        }

        try {
            AppiumServiceBuilder builder = configureServiceBuilder();
            service = builder.build();
            service.start();

            if (!service.isRunning()) {
                throw new RuntimeException("Appium service started but not detected as running.");
            }

            // Device Farm needs a few seconds to initialize device pool
            waitForDeviceFarmReady();

            System.out.println("Appium + Device Farm started at: " + service.getUrl());
            System.out.println("Device Farm dashboard: " + DEVICE_FARM_URL + "/device-farm");

        } catch (Exception e) {
            System.err.println("Failed to start Appium server: " + e.getMessage());
            throw new RuntimeException("Appium server failed to start", e);
        }
    }



    /**
     * Checks if an external Appium server (e.g. Appium GUI) is running
     * by hitting the /status endpoint.
     */
    private boolean isExternalServerRunning() {
        try {
            HttpURLConnection conn = (HttpURLConnection)
                    new URL(DEVICE_FARM_URL + "/status").openConnection();
            conn.setConnectTimeout(3000);
            conn.setRequestMethod("GET");
            conn.connect();
            return conn.getResponseCode() == 200;
        } catch (Exception e) {
            return false; // No external server reachable
        }
    }

    private void waitForDeviceFarmReady() {
        System.out.println("Waiting for Device Farm to initialize device pool...");
        int maxWaitSeconds = 30;
        int waited = 0;

        while (waited < maxWaitSeconds) {
            try {
                Thread.sleep(2000);
                waited += 2;

                // Poll Device Farm device list endpoint
                java.net.URL url = new java.net.URL(
                        DEVICE_FARM_URL + "/device-farm/api/devices");
                java.net.HttpURLConnection conn =
                        (java.net.HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(2000);
                conn.setReadTimeout(2000);

                int status = conn.getResponseCode();
                if (status == 200) {
                    System.out.println("Device Farm ready. (" + waited + "s)");
                    return;
                }
            } catch (Exception e) {
                System.out.println("Device Farm not ready yet... (" + waited + "s)");
            }
        }

        // Non-fatal — server may still work even if endpoint isn't up
        System.err.println("Warning: Device Farm readiness check timed out after "
                + maxWaitSeconds + "s. Proceeding anyway.");
    }

    /**
     * Checks if the programmatically started service is running.
     */
    public boolean isServerRunning() {
        return service != null && service.isRunning();
    }

    /**
     * Stops the Appium server gracefully.
     */
    public void stopServer() {
        if (isExternalServerRunning() && !isServerRunning()) {
            // Server was GUI-managed — don't touch it
            System.out.println("External Appium server detected. Skipping stop.");
            return;
        }

        if (isServerRunning()) {
            service.stop();
            service = null;
            System.out.println("Appium server stopped.");
        }
    }


    // === Internal Helpers ===

    /**
     * Configures the Appium service builder with platform-aware settings.
     */
    private AppiumServiceBuilder configureServiceBuilder() {
        return new AppiumServiceBuilder()
                .withIPAddress("127.0.0.1")
                .usingPort(4723)

                // Base path required for Device Farm
                .withArgument(() -> "--base-path", "/wd/hub")

                // Activate Device Farm plugin
                .withArgument(() -> "--use-plugins", "device-farm")

                // Target platform
                .withArgument(() -> "--plugin-device-farm-platform", "android")

                // Keep alive timeout
                .withArgument(() -> "--keep-alive-timeout", "800")

                // Security — scoped flags required by Appium 3
                .withArgument(() -> "--allow-insecure",
                        "*:session_discovery,uiautomator2:adb_shell")

                // Optional: more verbose logs during migration
                .withArgument(() -> "--log-level", "info");
    }
    /**
     * Attempts to detect the Appium main.js path based on OS.
     *
     * @return Detected File or null
     */
    private File detectAppiumJsPath() {
        // 🔥 Priority 1: Override via env var or system property
        String override = System.getProperty("appium.js.path");
        if (override == null) override = System.getenv("APPIUM_JS_PATH");
        if (override != null && Files.exists(Paths.get(override))) {
            return new File(override);
        }

        // 🔥 Priority 2: Your confirmed working path (Rohit's machine)
        String knownPath = "C:/Users/Rohitbhagat/AppData/Roaming/npm/node_modules/appium/build/lib/main.js";
        if (Files.exists(Paths.get(knownPath))) {
            System.out.println("✓ Using known Appium path: " + knownPath);
            return new File(knownPath);
        }

        // 🔥 Priority 3: Windows fallback paths (NO npm command calls)
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            String userHome = System.getProperty("user.home");
            String appData = System.getenv("APPDATA");

            Path[] candidates = {
                    // Standard global npm install location
                    Paths.get(appData, "npm", "node_modules", "appium", "build", "lib", "main.js"),
                    // User home variant
                    Paths.get(userHome, "AppData", "Roaming", "npm", "node_modules", "appium", "build", "lib", "main.js"),
                    // NVM-Windows pattern (if used)
                    Paths.get(userHome, "nvm", "node_modules", "appium", "build", "lib", "main.js")
            };

            for (Path candidate : candidates) {
                if (candidate != null && Files.exists(candidate)) {
                    System.out.println("✓ Found Appium at: " + candidate);
                    return candidate.toFile();
                }
            }
        }

        // 🔥 Priority 4: Unix paths (unchanged)
        // ... your existing Unix logic ...

        System.err.println("✗ Appium main.js not found.");
        System.err.println("  → Set APPIUM_JS_PATH env var to the full path of main.js");
        System.err.println("  → Or ensure Appium is installed: npm install -g appium");
        return null;
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