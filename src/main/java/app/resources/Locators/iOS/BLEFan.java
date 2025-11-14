package app.resources.Locators.iOS;

import org.openqa.selenium.By;

import java.util.regex.Pattern;

public class BLEFan {
    public static final By SELECT_FILE = By.xpath("//android.widget.Button[@content-desc=\"Select File\"]");
    public static final By FIRMWARE_UPDATE = By.xpath("//android.view.View[@content-desc=\"Firmware Update\"]");
    public static final By START_BUTTON = By.xpath("//android.widget.Button[@content-desc=\"Start\"]");
    public static final By STOP_BUTTON = By.xpath("//android.widget.Button[@content-desc=\"Stop\"]");
    public static final By PAUSE = By.xpath("//android.widget.Button[@content-desc=\"Pause\"]");
    public static final By RESUME = By.xpath("//android.widget.Button[@content-desc=\"Resume\"]");
    public static final By DEVICE_DISCONNECTED = By.xpath("//android.view.View[@content-desc=\"Device disconnected\"]");
    public static final By FIRMWARE_SUCCESS_TOAST = By.xpath("//android.view.View[@content-desc=\"Firmware upgrade successful\"]");
    public static final By FAN_MODEL = By.xpath("//android.view.View[@content-desc=\"Model: R3\"]");
    public static final By BONDING_FAILED = By.xpath("//android.view.View[@content-desc=\"Failed to create bond. BmBondStateEnum.none\"]");
    public static final By FILE_TRANSFER_ERROR_TOAST = By.xpath("//android.view.View[@content-desc=\"File transfer successful but failed to validate the upgrade. Upgrade will be validated on next connection\"]");
    public static final By DEVICE_IS_UNLINKED = By.xpath("//android.view.View[@content-desc=\"This device is unlinked from this family. Please remove it.\"]");
    public static final By DEVICE_NOT_CONNECTED = By.xpath("//android.view.View[@content-desc=\"Device not connected\"]");
    public static final By MENU_BUTTON = By.xpath(
            "//android.widget.FrameLayout[@resource-id='android:id/content']" +
                    "/android.widget.FrameLayout/android.widget.FrameLayout" +
                    "/android.view.View/android.view.View/android.view.View" +
                    "/android.view.View/android.view.View/android.view.View/android.view.View[4]");
    public static final String FIRMWARE_VERSION_PREFIX = "Firmware Version";
    public static final By SELECT_FILE_OPTION = By.xpath("//android.widget.Button[@content-desc=\"Select File\"]");
    public static final By TITLE_ID = By.id("android:id/title"); // Most reliable for file items
    public static final By DONE_BUTTON = By.xpath("//android.widget.Button[@content-desc=\"Done\"]");
    public static final int BLUETOOTH_ON_OFF_CYCLES = 10;
    public static final int POWER_ON_OFF_CYCLES = 10;
    public static final long HOLD_DURATION_MS = 1500; // Matches your proven timing
    public static final long FIRMWARE_SUCCESS_TIMEOUT_MS = 20_000;
    public static final Pattern VERSION_PATTERN = Pattern.compile("Production_(\\d+\\.\\d+\\.\\d+)\\.bin", Pattern.CASE_INSENSITIVE);
    public static final By FAILED_CONNECTION = By.xpath("//android.view.View[@content-desc=\"Connection Failed\"]");
    public static final By DEVICE_IS_OFFLINE = By.xpath("//android.view.View[@content-desc=\"Device is offline. If it is nearby, please turn on bluetooth to connect.\"]");
}
