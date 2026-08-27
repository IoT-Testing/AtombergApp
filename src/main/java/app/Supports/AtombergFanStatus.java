package app.Supports;

import app.api.DeviceStateVerifier;

/**
 * @deprecated Legacy shim. Prefer {@link app.api.DeviceStateVerifier} directly —
 * it takes an explicit device id and supports polling ({@code awaitState}) instead
 * of a single blind read.
 *
 * <p>Retained so existing {@code FanManagement} callers keep compiling. The old
 * implementation shelled out to {@code api_tester.py} via {@link ProcessBuilder}
 * with a hardcoded {@code d:/AtombergAppBoF/...} path and re-authenticated on every
 * call; this version delegates to the native {@link DeviceStateVerifier}.</p>
 *
 * <p>The device id is read once from the {@code ATOMBERG_TEST_DEVICE_ID}
 * environment variable, since the legacy {@code fanStatus(String)} signature has
 * no device parameter.</p>
 */
@Deprecated
public class AtombergFanStatus {

    private DeviceStateVerifier verifier;

    /**
     * Returns the current value of a fan state field, or {@code null} on error.
     *
     * @param command one of {@code power, speed, timer, led, sleep}
     */
    public String fanStatus(String command) {
        String deviceId = System.getenv("ATOMBERG_TEST_DEVICE_ID");
        if (deviceId == null || deviceId.isBlank()) {
            System.err.println("[AtombergFanStatus] ATOMBERG_TEST_DEVICE_ID is not set; "
                    + "cannot verify fan state via the API.");
            return null;
        }
        return fanStatus(deviceId, command);
    }

    /** Preferred overload: verify a specific device by id. */
    public String fanStatus(String deviceId, String command) {
        try {
            return verifier().readField(deviceId, command);
        } catch (Exception e) {
            System.err.println("[AtombergFanStatus] failed to read '" + command
                    + "' for " + deviceId + ": " + e.getMessage());
            return null;
        }
    }

    /** Lazily built so constructing this class never fails when creds are absent. */
    private DeviceStateVerifier verifier() {
        if (verifier == null) {
            verifier = new DeviceStateVerifier();
        }
        return verifier;
    }
}
