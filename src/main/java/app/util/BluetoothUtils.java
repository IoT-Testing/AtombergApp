package app.util;

import java.util.HashMap;
import java.util.Map;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.UnsupportedCommandException;

public class BluetoothUtils {

    /**
     * Enables or disables Bluetooth using ADB shell command via Appium
     * @param driver The AppiumDriver instance
     */

    public static boolean isBluetoothEnabled(AndroidDriver driver) {
        Map<String, Object> params = new HashMap<>();
        params.put("command", "dumpsys");
        params.put("args", "bluetooth_manager");

        try {
            Object resultObj = driver.executeScript("mobile: shell", params);
            String result = resultObj != null ? resultObj.toString() : "";

            // Look for mState=ON or mState=OFF
            for (String line : result.split("\\r?\\n")) {
                if (line.trim().startsWith("mState=")) {
                    return line.contains("mState=ON");
                }
            }

            // Fallback: check if any Bluetooth service is active
            return result.contains("Bluetooth Enabled") ||
                    result.contains("state: ON");

        } catch (Exception e) {
            System.err.println("Failed to read Bluetooth status: " + e.getMessage());
            return false; // Assume off or error
        }
    }

    public static void setBluetoothState(AndroidDriver driver, boolean enabled) {
        String action = enabled ? "enable" : "disable";
        Map<String, Object> params = new HashMap<>();
        params.put("command", "cmd");
        params.put("args", "bluetooth_manager " + action);

        try {
            // Modern Appium command (Appium 2.x + UIA2)
            driver.executeScript("mobile: shell", params);
        } catch (UnsupportedCommandException e) {
            // Fallback for older versions (less common)
            System.out.println("Fallback: Using legacy execute command");
            driver.execute("mobile: shell", params);
        } catch (Exception ex) {
            System.err.println("Failed to set Bluetooth " + action + ": " + ex.getMessage());
            throw ex;
        }
    }

    // Convenience methods
    public static void turnOffBluetooth(AndroidDriver driver) {
        setBluetoothState(driver, false);
    }

    public static void turnOnBluetooth(AndroidDriver driver) {
        setBluetoothState(driver, true);
    }
}
