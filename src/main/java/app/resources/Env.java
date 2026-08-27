package app.resources;

/**
 * Lazy environment-variable accessor.
 *
 * <p>Unlike {@link Credentials} (which validates every variable eagerly in static
 * initializers the moment the class is touched), {@code Env} reads and validates a
 * single variable <b>at the point of use</b>. This avoids forcing unrelated code
 * paths — e.g. the standalone {@code Main} runner — to define the full credential
 * set just to run one flow.</p>
 *
 * <p>Use this for secrets that must not be committed to source. See
 * {@code test.env.example} for the full list of variables.</p>
 */
public final class Env {

    private Env() {
    }

    /** Returns the variable's value, or throws with a clear message if unset/blank. */
    public static String required(String key) {
        String value = System.getenv(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Missing required environment variable: " + key +
                            "\nSee test.env.example for setup instructions.");
        }
        return value;
    }

    /** Returns the variable's value, or {@code defaultValue} if unset/blank. */
    public static String optional(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value != null && !value.isBlank()) ? value : defaultValue;
    }
}
