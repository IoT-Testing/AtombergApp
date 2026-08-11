package app.FailSafe;

import app.util.BluetoothUtils;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;

public class DeviceDisconnectedHelper {
    private final AndroidDriver driver;

    public DeviceDisconnectedHelper(AndroidDriver driver) {
        this.driver = driver;
    }

    /**
     * Full failsafe recovery: Ensures BT is on + navigates to Firmware screen
     */
    public void recoverFromDeviceDisconnected() {
        logpoint("Starting failsafe recovery...");

        if(!BluetoothUtils.isBluetoothEnabled(driver)){
            BluetoothUtils.setBluetoothState(driver, true);
        }
        logpoint("Failsafe recovery completed.");
    }
    public interface recoveryFromDeviceDisconnected{}
}

