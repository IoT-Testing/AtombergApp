package app.resources;

/**
 * Credentials - Loads test credentials from environment variables.
 *
 * Setup:
 *   1. Copy test.env.example → test.env in project root
 *   2. Fill in real values in test.env
 *   3. Load before running: export $(cat test.env | xargs)
 *      OR set variables in your CI/CD pipeline secrets.
 *
 * NEVER hardcode credentials here. NEVER commit test.env.
 */
public class Credentials {

    /** Default test account — used by AppInitializer and Email.java fallback login. */
    public static final String DEFAULT_EMAIL    = getRequired("DEFAULT_EMAIL");
    public static final String DEFAULT_PASSWORD = getRequired("DEFAULT_PASSWORD");

    /** Login test account — used by LoginTest. */
    public static final String LOGIN_TEST_EMAIL            = getRequired("LOGIN_TEST_EMAIL");
    public static final String LOGIN_TEST_PASSWORD         = getRequired("LOGIN_TEST_PASSWORD");
    public static final String LOGIN_TEST_INVALID_PASSWORD = getRequired("LOGIN_TEST_INVALID_PASSWORD");

    /** Alexa integration test account. */
    public static final String ALEXA_TEST_EMAIL    = getRequired("ALEXA_TEST_EMAIL");
    public static final String ALEXA_TEST_PASSWORD = getRequired("ALEXA_TEST_PASSWORD");

    /** Google Home integration test account. */
    public static final String GOOGLE_TEST_EMAIL    = getRequired("GOOGLE_TEST_EMAIL");
    public static final String GOOGLE_TEST_PASSWORD = getRequired("GOOGLE_TEST_PASSWORD");

    /** Wi-Fi provisioning credentials. */
    public static final String WIFI_SSID     = getRequired("WIFI_SSID");
    public static final String WIFI_PASSWORD = getRequired("WIFI_PASSWORD");

    /**
     * Reads an environment variable.
     * Throws a clear RuntimeException at startup if the variable is missing,
     * rather than failing silently mid-test with a NullPointerException.
     */
    private static String getRequired(String key) {
        String value = System.getenv(key);
        if (value == null || value.isBlank()) {
            throw new RuntimeException(
                    "Missing required environment variable: " + key +
                            "\nSee test.env.example for setup instructions."
            );
        }
        return value;
    }
}