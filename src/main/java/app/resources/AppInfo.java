package app.resources;

/**
 * AppInfo – application-level constants (package, activity, recorder).
 *
 * <p>Provides a single source of truth for the Atomberg app's package and
 * activity identifiers. {@link Credentials} uses these values so both
 * {@code APP_PACKAGE} and {@code ATOMBERG_HOME} refer to the same string.</p>
 */
public class AppInfo {

    /** Main package of the Atomberg Home app. */
    public static final String ATOMBERG_HOME       = "com.atomberg.app";

    /** Fully-qualified Flutter main activity. */
    public static final String ATOMBERG_ACTIVITY   = "com.atomberg.app.MainActivity";

    /** Package of the HBRecorder example app (used for on-device screen recording). */
    public static final String RECORDER_APP_PACKAGE = "com.hbisoft.hbrecorderexample";

    private AppInfo() { /* constants-only class */ }
}