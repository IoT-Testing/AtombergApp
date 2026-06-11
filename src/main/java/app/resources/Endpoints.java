package app.resources;

public class Endpoints {
        public static final String BASE_URL = "https://api.atomberg.com";

        public static final String LOGIN_ENDPOINT = "/auth/login";
        public static final String USER_PROFILE_ENDPOINT = "/user/profile";
        public static final String DEVICE_LIST_ENDPOINT = "/devices";
        public static final String DEVICE_STATUS_ENDPOINT = "/devices/{deviceId}/status";
        public static final String DEVICE_CONTROL_ENDPOINT = "/devices/{deviceId}/control";

    /** Appium server URL - can be overridden via environment variable */
    public static final String APPIUM_URL = getOptional("APPIUM_URL", "http://127.0.0.1:4723/wd/hub");

    private static String getOptional(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value != null && !value.isBlank()) ? value : defaultValue;
    }
}
