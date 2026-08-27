package app.api;

import java.time.Duration;

import org.json.JSONObject;

/**
 * Backend state oracle for internet-connected Atomberg devices (fan, water purifier).
 *
 * <p>Cross-verifies the app UI against the real device state reported by the
 * Atomberg developer API. The typical test shape is:</p>
 * <pre>
 *   fan.setSpeed(3);                                   // UI action
 *   assert verifier.awaitState(deviceId, "speed", "3"); // backend confirms
 * </pre>
 *
 * <p><b>Why polling:</b> cloud state lags the UI by a second or two after a
 * command round-trips through the device. {@link #awaitState} polls until the
 * expected value appears or a timeout elapses, which is more reliable than the
 * old {@code ActionsUtil.sleep(1000)} + single read pattern.</p>
 *
 * <p><b>Scope:</b> only devices whose state is readable over the internet.
 * The smart lock is Bluetooth-only and has no cloud state — verify it via UI
 * assertions instead; this class will throw for it.</p>
 *
 * <p>Field names accepted by {@link #readField} / {@link #awaitState} map to the
 * device_state JSON as follows:</p>
 * <ul>
 *   <li>{@code "power"} → {@code power} (boolean)</li>
 *   <li>{@code "speed"} → {@code last_recorded_speed} (int)</li>
 *   <li>{@code "timer"} → {@code timer_hours} (int)</li>
 *   <li>{@code "led"}   → {@code led} (boolean)</li>
 *   <li>{@code "sleep"} → {@code sleep_mode} (boolean)</li>
 * </ul>
 */
public class DeviceStateVerifier {

    public static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(15);
    private static final Duration POLL_INTERVAL = Duration.ofMillis(1500);

    private final AtombergApiClient client;

    public DeviceStateVerifier() {
        this(new AtombergApiClient());
    }

    public DeviceStateVerifier(AtombergApiClient client) {
        this.client = client;
    }

    /**
     * Polls the backend until {@code field} equals {@code expected} (string compare)
     * or {@code timeout} elapses. Transient read errors are swallowed and retried
     * until the deadline.
     *
     * @return true if the expected value was observed before the timeout
     */
    public boolean awaitState(String deviceId, String field, String expected, Duration timeout) {
        long deadline = System.currentTimeMillis() + timeout.toMillis();
        String last = null;
        while (System.currentTimeMillis() < deadline) {
            try {
                last = readField(deviceId, field);
                if (expected.equals(last)) {
                    return true;
                }
            } catch (Exception e) {
                last = "<error: " + e.getMessage() + ">";
            }
            try {
                Thread.sleep(POLL_INTERVAL.toMillis());
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        System.out.println("[DeviceStateVerifier] timeout waiting for " + field
                + "=" + expected + " on " + deviceId + " (last seen: " + last + ")");
        return false;
    }

    /** {@link #awaitState(String, String, String, Duration)} with {@link #DEFAULT_TIMEOUT}. */
    public boolean awaitState(String deviceId, String field, String expected) {
        return awaitState(deviceId, field, expected, DEFAULT_TIMEOUT);
    }

    /**
     * Reads a single friendly field as a string, mapping it to the underlying
     * device_state JSON key. One backend call, no polling.
     */
    public String readField(String deviceId, String field) throws Exception {
        JSONObject state = client.getDeviceState(deviceId);
        return switch (field) {
            case "power" -> String.valueOf(state.getBoolean("power"));
            case "speed" -> String.valueOf(state.getInt("last_recorded_speed"));
            case "timer" -> String.valueOf(state.getInt("timer_hours"));
            case "led"   -> String.valueOf(state.getBoolean("led"));
            case "sleep" -> String.valueOf(state.getBoolean("sleep_mode"));
            default -> throw new IllegalArgumentException(
                    "Unknown device_state field: '" + field
                            + "'. Expected one of: power, speed, timer, led, sleep.");
        };
    }

    // === Typed one-shot getters ===

    public boolean getPower(String deviceId) throws Exception {
        return client.getDeviceState(deviceId).getBoolean("power");
    }

    public int getSpeed(String deviceId) throws Exception {
        return client.getDeviceState(deviceId).getInt("last_recorded_speed");
    }

    public int getTimerHours(String deviceId) throws Exception {
        return client.getDeviceState(deviceId).getInt("timer_hours");
    }

    public boolean getLed(String deviceId) throws Exception {
        return client.getDeviceState(deviceId).getBoolean("led");
    }

    public boolean getSleep(String deviceId) throws Exception {
        return client.getDeviceState(deviceId).getBoolean("sleep_mode");
    }
}
