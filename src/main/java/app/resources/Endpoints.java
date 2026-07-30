package app.resources;

public class Endpoints {
        public static final String BASE_URL = "https://api.atomberg.com";

        public static final String LOGIN_ENDPOINT = "/auth/login";
        public static final String USER_PROFILE_ENDPOINT = "/user/profile";
        public static final String DEVICE_LIST_ENDPOINT = "/devices";
        public static final String DEVICE_STATUS_ENDPOINT = "/devices/{deviceId}/status";
        public static final String DEVICE_CONTROL_ENDPOINT = "/devices/{deviceId}/control";

        /**
         * Atomberg public developer API — the real backend used for device-state
         * cross-verification (fan / water purifier). This is the API the standalone
         * Python helpers (api_tester.py) already hit; the Java oracle uses the same one.
         * NOTE: the BASE_URL above (api.atomberg.com) is aspirational and currently unused.
         */
        public static final String DEVELOPER_API_BASE = "https://api.developer.atomberg-iot.com";
        public static final String GET_ACCESS_TOKEN    = "/v1/get_access_token";
        public static final String GET_DEVICE_STATE    = "/v1/get_device_state";
        public static final String GET_LIST_OF_DEVICES = "/v1/get_list_of_devices";
        public static final String SEND_COMMAND        = "/v1/send_command";

    /** Appium server URL - can be overridden via environment variable */
    public static final String APPIUM_URL = getOptional("APPIUM_URL", "http://127.0.0.1:4723");

    private static String getOptional(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value != null && !value.isBlank()) ? value : defaultValue;
    }
}
