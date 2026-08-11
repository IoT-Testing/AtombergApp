package app;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * ServerInitializer – manages the Appium server lifecycle.
 *
 * <p>Supports three execution modes:
 * <ol>
 *   <li><b>External server already running</b> (Appium GUI / Device Farm started separately) –
 *       detected automatically; programmatic start is skipped.</li>
 *   <li><b>Programmatic start with Device Farm plugin</b> – starts Appium + ATD plugin in-process.</li>
 *   <li><b>CI/CD with APPIUM_URL set</b> – remote hub; no local server needed.</li>
 * </ol>
 *
 * FIX C7: All hardcoded absolute paths ("C:/Users/Rohitbhagat/...") removed.
 *         Appium is located via APPIUM_JS_PATH env var → PATH resolution → OS-specific heuristics.
 * FIX H3: withAppiumJS() is now called when main.js is found so Appium starts reliably.
 * FIX H4: ATD readiness check uses correct endpoint /device-farm/api/device (not /devices).
 */
public class ServerInitializer {

    public AppiumDriverLocalService service;

    private static final String APPIUM_URL      = System.getenv().getOrDefault("APPIUM_URL", "http://127.0.0.1:4723");
    private static final String ATD_DEVICE_API  = APPIUM_URL + "/device-farm/api/device"; // FIX H4
    private static final String STATUS_ENDPOINT = APPIUM_URL + "/status";
    private static final int    DEFAULT_PORT    = 4723;

    // ── Public API ─────────────────────────────────────────────────────────────

    /**
     * Starts the Appium server (or skips if already running externally).
     */
    public void startServer() {
        if (isExternalServerRunning()) {
            System.out.println("[ServerInitializer] External Appium server detected at " + APPIUM_URL + " — skipping local start.");
            return;
        }
        if (isServerRunning()) {
            System.out.println("[ServerInitializer] Programmatic server already running at: " + service.getUrl());
            return;
        }

        try {
            AppiumServiceBuilder builder = buildServiceBuilder();
            service = builder.build();
            service.start();

            if (!service.isRunning()) {
                throw new RuntimeException("Appium service started but isRunning() returned false — check Appium installation.");
            }

            waitForDeviceFarmReady();
            System.out.println("[ServerInitializer] Appium + Device Farm started at: " + service.getUrl());

        } catch (Exception e) {
            System.err.println("[ServerInitializer] FATAL: Failed to start Appium server: " + e.getMessage());
            throw new RuntimeException("Appium server failed to start", e);
        }
    }

    /**
     * Stops the locally started service. Never touches an external (GUI-managed) server.
     */
    public void stopServer() {
        if (isExternalServerRunning() && !isServerRunning()) {
            System.out.println("[ServerInitializer] External Appium server — skipping stop.");
            return;
        }
        if (isServerRunning()) {
            service.stop();
            service = null;
            System.out.println("[ServerInitializer] Appium server stopped.");
        }
    }

    public boolean isServerRunning() {
        return service != null && service.isRunning();
    }

    // ── Private helpers ────────────────────────────────────────────────────────

    /**
     * Pings the /status endpoint to detect an already-running server.
     */
    private boolean isExternalServerRunning() {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(STATUS_ENDPOINT).openConnection();
            conn.setConnectTimeout(3_000);
            conn.setReadTimeout(3_000);
            conn.setRequestMethod("GET");
            conn.connect();
            return conn.getResponseCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * FIX H4: Polls the correct ATD endpoint /device-farm/api/device (singular).
     * Previous code polled /device-farm/api/devices which does not exist → 404 always.
     */
    private void waitForDeviceFarmReady() {
        System.out.println("[ServerInitializer] Waiting for Appium Device Farm to initialise device pool...");
        final int maxWaitSeconds = 30;
        int waited = 0;

        while (waited < maxWaitSeconds) {
            try {
                Thread.sleep(2_000);
                waited += 2;

                HttpURLConnection conn = (HttpURLConnection) new URL(ATD_DEVICE_API).openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(2_000);
                conn.setReadTimeout(2_000);

                if (conn.getResponseCode() == 200) {
                    System.out.println("[ServerInitializer] Device Farm ready after " + waited + "s.");
                    return;
                }
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                return;
            } catch (Exception e) {
                System.out.printf("[ServerInitializer] Waiting for Device Farm... (%ds)%n", waited);
            }
        }
        System.err.println("[ServerInitializer] WARNING: Device Farm readiness check timed out after "
                + maxWaitSeconds + "s. Proceeding anyway.");
    }

    /**
     * Builds the AppiumServiceBuilder.
     *
     * FIX C7 / H3: detectAppiumJsPath() uses environment variables and OS-aware
     * heuristics — no hardcoded personal paths.
     */
    private AppiumServiceBuilder buildServiceBuilder() {
        AppiumServiceBuilder builder = new AppiumServiceBuilder()
                .withIPAddress("127.0.0.1")
                .usingPort(DEFAULT_PORT)
                .withArgument(() -> "--base-path", "/")            // Appium 3 default
                .withArgument(() -> "--use-plugins", "device-farm")
                .withArgument(() -> "--plugin-device-farm-platform", "android")
                .withArgument(() -> "--keep-alive-timeout", "800")
                .withArgument(() -> "--allow-insecure",
                        "*:session_discovery,uiautomator2:adb_shell")
                .withArgument(() -> "--log-level", "info")
                .withLogFile(getLogFile());

        // FIX H3: Attempt to locate main.js; if found, pass to withAppiumJS() so
        // AppiumServiceBuilder does not have to rely on PATH resolution (fragile on CI).
        File mainJs = detectAppiumJsPath();
        if (mainJs != null) {
            builder.withAppiumJS(mainJs);
            System.out.println("[ServerInitializer] Using Appium main.js: " + mainJs.getAbsolutePath());
        } else {
            System.out.println("[ServerInitializer] Appium main.js not found via heuristics; relying on PATH.");
        }

        return builder;
    }

    /**
     * Locates Appium's main.js using environment variables and OS-specific conventions.
     *
     * FIX C7: Removed hardcoded "C:/Users/Rohitbhagat/..." path entirely.
     * Priority:
     *   1. APPIUM_JS_PATH env var (highest priority — set this in CI secrets)
     *   2. Windows global npm: %APPDATA%/npm/node_modules/appium/build/lib/main.js
     *   3. macOS/Linux: $(npm root -g)/appium/build/lib/main.js resolved via common prefixes
     *   4. Returns null → relying on PATH (still works if appium is on PATH)
     */
    private File detectAppiumJsPath() {
        // Priority 1: explicit override
        String envOverride = System.getenv("APPIUM_JS_PATH");
        if (envOverride != null && !envOverride.isBlank() && Files.exists(Paths.get(envOverride))) {
            return new File(envOverride);
        }

        String os = System.getProperty("os.name", "").toLowerCase();
        String relativeMainJs = String.join(File.separator, "appium", "build", "lib", "main.js");

        if (os.contains("win")) {
            // Priority 2a: %APPDATA%\npm\node_modules\...
            String appData = System.getenv("APPDATA");
            if (appData != null) {
                Path candidate = Paths.get(appData, "npm", "node_modules", relativeMainJs);
                if (Files.exists(candidate)) return candidate.toFile();
            }
            // Priority 2b: user.home\AppData\Roaming\npm\node_modules\...
            Path candidate = Paths.get(System.getProperty("user.home"),
                    "AppData", "Roaming", "npm", "node_modules", relativeMainJs);
            if (Files.exists(candidate)) return candidate.toFile();

        } else {
            // Priority 3: Unix — check common global npm roots
            for (String prefix : List.of(
                    "/usr/local/lib",
                    "/usr/lib",
                    System.getProperty("user.home") + "/.nvm/versions/node"
            )) {
                Path candidate = Paths.get(prefix, "node_modules", relativeMainJs);
                if (Files.exists(candidate)) return candidate.toFile();
            }
        }

        return null; // main.js not found — caller will rely on PATH
    }

    /**
     * Returns a time-stamped log file under logs/appium/.
     */
    private File getLogFile() {
        File dir = new File(System.getProperty("user.dir") + File.separator + "logs" + File.separator + "appium");
        dir.mkdirs();
        String ts = java.time.LocalDateTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return new File(dir, "appium_" + ts + ".log");
    }
}