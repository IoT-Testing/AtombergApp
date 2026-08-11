package app.api;

import app.resources.Endpoints;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Thin native client for the Atomberg developer API.
 *
 * <p>Replaces the old approach of shelling out to {@code api_tester.py} via
 * {@link ProcessBuilder} with a hardcoded {@code d:/AtombergAppBoF/...} path.
 * Uses the JDK's built-in {@link HttpClient} (no extra dependency) and
 * {@code org.json} for parsing (already on the classpath).</p>
 *
 * <p>Credentials come from environment variables so nothing is committed:</p>
 * <ul>
 *   <li>{@code ATOMBERG_API_KEY}        — developer x-api-key</li>
 *   <li>{@code ATOMBERG_REFRESH_TOKEN}  — developer refresh token</li>
 * </ul>
 *
 * <p>The access token is fetched once and cached statically for the whole JVM
 * (one Atomberg account per run), and transparently refreshed on a 401. This is
 * the key win over the old per-call Python auth, which re-authenticated on every
 * single state read.</p>
 */
public class AtombergApiClient {

    private static final Duration HTTP_TIMEOUT = Duration.ofSeconds(10);

    /** Shared across instances — one account per run, so cache the bearer token once. */
    private static volatile String cachedAccessToken;
    private static final Object TOKEN_LOCK = new Object();

    private final String apiKey;
    private final String refreshToken;
    private final HttpClient http;

    /** Reads credentials from the environment; fails fast with a clear message if absent. */
    public AtombergApiClient() {
        this(requireEnv("ATOMBERG_API_KEY"), requireEnv("ATOMBERG_REFRESH_TOKEN"));
    }

    public AtombergApiClient(String apiKey, String refreshToken) {
        this.apiKey = apiKey;
        this.refreshToken = refreshToken;
        this.http = HttpClient.newBuilder()
                .connectTimeout(HTTP_TIMEOUT)
                .build();
    }

    private static String requireEnv(String key) {
        String value = System.getenv(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Missing required environment variable: " + key +
                            "\nThe device-state API oracle needs Atomberg developer credentials. " +
                            "See test.env.example for setup instructions.");
        }
        return value;
    }

    /**
     * Returns the first {@code device_state} object for the given device id, e.g.
     * {@code {"power": true, "last_recorded_speed": 3, "timer_hours": 0, "led": true, "sleep_mode": false}}.
     *
     * @param deviceId the device MAC id (e.g. {@code "3030f9c92258"})
     * @throws IOException          on transport failure or non-2xx after auth retry
     * @throws InterruptedException if the calling thread is interrupted
     */
    public JSONObject getDeviceState(String deviceId) throws IOException, InterruptedException {
        return getDeviceState(deviceId, true);
    }

    private JSONObject getDeviceState(String deviceId, boolean retryOnAuth)
            throws IOException, InterruptedException {
        String token = accessToken();
        String url = Endpoints.DEVELOPER_API_BASE + Endpoints.GET_DEVICE_STATE
                + "?device_id=" + URLEncoder.encode(deviceId, StandardCharsets.UTF_8);

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("x-api-key", apiKey)
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .timeout(HTTP_TIMEOUT)
                .GET()
                .build();

        HttpResponse<String> resp = http.send(req, HttpResponse.BodyHandlers.ofString());

        // Token expired mid-run: drop the cache and re-auth once.
        if (resp.statusCode() == 401 && retryOnAuth) {
            invalidateToken();
            return getDeviceState(deviceId, false);
        }
        if (resp.statusCode() / 100 != 2) {
            throw new IOException("get_device_state failed for " + deviceId
                    + ": HTTP " + resp.statusCode() + " — " + resp.body());
        }

        JSONObject message = new JSONObject(resp.body()).getJSONObject("message");
        JSONArray deviceState = message.getJSONArray("device_state");
        if (deviceState.isEmpty()) {
            throw new IOException("Empty device_state array for device " + deviceId
                    + " — is the device online and owned by this account?");
        }
        return deviceState.getJSONObject(0);
    }

    /** Returns a cached bearer token, fetching a fresh one if none is cached. */
    private String accessToken() throws IOException, InterruptedException {
        String token = cachedAccessToken;
        if (token != null) {
            return token;
        }
        synchronized (TOKEN_LOCK) {
            if (cachedAccessToken != null) {
                return cachedAccessToken;
            }
            cachedAccessToken = fetchAccessToken();
            return cachedAccessToken;
        }
    }

    private String fetchAccessToken() throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(Endpoints.DEVELOPER_API_BASE + Endpoints.GET_ACCESS_TOKEN))
                .header("x-api-key", apiKey)
                .header("Authorization", "Bearer " + refreshToken)
                .timeout(HTTP_TIMEOUT)
                .GET()
                .build();

        HttpResponse<String> resp = http.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() == 401 || resp.statusCode() == 403) {
            throw new IOException("Atomberg refresh token rejected (HTTP " + resp.statusCode()
                    + "). Check ATOMBERG_REFRESH_TOKEN / ATOMBERG_API_KEY.");
        }
        if (resp.statusCode() / 100 != 2) {
            throw new IOException("get_access_token failed: HTTP " + resp.statusCode()
                    + " — " + resp.body());
        }

        String token = new JSONObject(resp.body())
                .getJSONObject("message")
                .optString("access_token", null);
        if (token == null || token.isBlank()) {
            throw new IOException("No access_token in get_access_token response: " + resp.body());
        }
        return token;
    }

    private static void invalidateToken() {
        synchronized (TOKEN_LOCK) {
            cachedAccessToken = null;
        }
    }
}
