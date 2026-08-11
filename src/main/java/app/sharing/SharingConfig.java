package app.sharing;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * SharingConfig â€“ single source of configuration for the Device Sharing tests.
 *
 * <p>Resolution order for any key (first non-blank wins):
 * <ol>
 *   <li>{@code sharing-test.properties} in the project root (the working dir)</li>
 *   <li>the same file on the test classpath (e.g. src/test/resources)</li>
 *   <li>an environment variable of the same name</li>
 *   <li>a {@code -D} system property of the same name</li>
 * </ol>
 *
 * <p>This exists because {@code $env:} variables set in one PowerShell window do
 * NOT reach a Maven/Surefire fork launched from another â€” a recurring source of
 * "Missing required environment variable". A checked-in (git-ignored) properties
 * file removes that fragility: edit it once, run from anywhere.</p>
 *
 * <p>Placeholder values like {@code <fan display name>} (starting with '&lt;')
 * are treated as UNSET, so {@link #require(String)} still fails clearly until a
 * real value is filled in.</p>
 */
public final class SharingConfig {

    private static final String FILE_NAME = "sharing-test.properties";
    private static final Properties PROPS = new Properties();
    private static boolean loaded = false;

    private SharingConfig() {}

    private static synchronized void ensureLoaded() {
        if (loaded) return;
        loaded = true;

        // 1. Project root (Surefire's working dir is the module basedir)
        Path p = Path.of(System.getProperty("user.dir", "."), FILE_NAME);
        if (Files.isReadable(p)) {
            try (InputStream in = Files.newInputStream(p)) {
                PROPS.load(in);
                logpoint("[SharingConfig] Loaded config: " + p.toAbsolutePath());
                return;
            } catch (IOException e) {
                System.err.println("[SharingConfig] Could not read " + p + ": " + e.getMessage());
            }
        }

        // 2. Classpath fallback (src/test/resources/sharing-test.properties)
        try (InputStream in = SharingConfig.class.getClassLoader().getResourceAsStream(FILE_NAME)) {
            if (in != null) {
                PROPS.load(in);
                logpoint("[SharingConfig] Loaded config from classpath: " + FILE_NAME);
            } else {
                logpoint("[SharingConfig] No " + FILE_NAME
                        + " found â€” falling back to environment variables / -D properties.");
            }
        } catch (IOException e) {
            System.err.println("[SharingConfig] Classpath config read error: " + e.getMessage());
        }
    }

    /** @return resolved value, or null if unset/blank/placeholder everywhere. */
    public static String get(String key) {
        ensureLoaded();
        String v = PROPS.getProperty(key);
        if (isSet(v)) return v.trim();
        v = System.getenv(key);
        if (isSet(v)) return v.trim();
        v = System.getProperty(key);
        return isSet(v) ? v.trim() : null;
    }

    /** @return resolved value, or {@code fallback} if unset. */
    public static String get(String key, String fallback) {
        String v = get(key);
        return v != null ? v : fallback;
    }

    /** @throws IllegalStateException with actionable guidance if the key is unset. */
    public static String require(String key) {
        String v = get(key);
        if (v == null)
            throw new IllegalStateException(
                    "Missing required config: " + key
                            + "\nSet it in " + FILE_NAME + " (project root) or as an environment variable."
                            + "\nSee sharing-test.properties for the template.");
        return v;
    }

    private static boolean isSet(String v) {
        return v != null && !v.isBlank() && !v.trim().startsWith("<");
    }
}

