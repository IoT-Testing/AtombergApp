package app.util;

import app.AppInitializer;
import io.appium.java_client.AppiumDriver;
//import org.openqa.selenium.remote.DesiredCapabilities;
import java.net.MalformedURLException;
//import java.net.URL;
import java.util.UUID;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothGattCharacteristic;
//import android.bluetooth.BluetoothGattCallback;
//import android.bluetooth.BluetoothManager;
//import android.content.Context;
import android.util.Log;

public class AtombergBLETest {
    public AppiumDriver driver;
    public BluetoothAdapter bluetoothAdapter;
    public BluetoothGatt bluetoothGatt;

    // Replace with the UUID of the service you need to interact with
    private static final UUID SERVICE_UUID = UUID.fromString("9256cc8a-85b3-46a6-a4a6-9b6a2e1248be");

//    public void setUp() throws MalformedURLException {
//        DesiredCapabilities caps = new DesiredCapabilities();
//        caps.setCapability("platformName", "Android");
//        caps.setCapability("platformVersion", "13");
//        caps.setCapability("appPackage", "com.atomberg.home");
//        caps.setCapability("appActivity", "com.atomberg.home.MainActivity");
//        driver = new AppiumDriver(new URL("http://127.0.0.1:4723/wd/hub"), caps);
//
//        // Initialize Bluetooth Adapter using InstrumentationRegistry to get context
////        Context context = ApplicationProvider.getApplicationContext();
////        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
////        bluetoothAdapter = bluetoothManager.getAdapter();
//    }

    public void enableBluetooth() {
        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }
    }

    public void scanAndConnectToDevice(String deviceName) {
        bluetoothAdapter.startLeScan((device, rssi, scanRecord) -> {
            if (device.getName().equals(deviceName)) {
                // Stop scanning once the device is found
                bluetoothAdapter.stopLeScan(null);

                // Connect to the device using InstrumentationRegistry context
//                Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
//                bluetoothGatt = device.connectGatt(context, false, gattCallback);
            }
        });
    }

//    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
//        @Override
//        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
//            if (newState == BluetoothGatt.STATE_CONNECTED) {
//                Log.d("BLETest", "Connected to GATT server.");
//                bluetoothGatt.discoverServices();
//            } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
//                Log.d("BLETest", "Disconnected from GATT server.");
//            }
//        }
//
//        @Override
//        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
//            if (status == BluetoothGatt.GATT_SUCCESS) {
//                BluetoothGattService service = gatt.getService(SERVICE_UUID);
//                if (service != null) {
//                    Log.d("BLETest", "Service UUID found: " + service.getUuid().toString());
//
//                    // Here, you can interact with the characteristics of the service
//                    // For example, read a characteristic
//                    readCharacteristic(service);
//                } else {
//                    Log.d("BLETest", "Service UUID not found!");
//                }
//            }
//        }
//    };

    public void readCharacteristic(BluetoothGattService service) {
        // Replace with your specific characteristic UUID
        UUID characteristicUUID = UUID.fromString("9256cc8a-85b3-46a6-a4a6-9b6a2e1248be");
        BluetoothGattCharacteristic characteristic = service.getCharacteristic(characteristicUUID);

        if (characteristic != null) {
            bluetoothGatt.readCharacteristic(characteristic);
            Log.d("BLETest", "Reading characteristic: " + characteristicUUID);
        } else {
            Log.d("BLETest", "Characteristic not found.");
        }
    }

    public void tearDown() {
        if (bluetoothGatt != null) {
            bluetoothGatt.disconnect();
            bluetoothGatt.close();
        }
        if (driver != null) {
            driver.quit();
        }
    }

    public static void main(String[] args) throws MalformedURLException {
        AtombergBLETest test = new AtombergBLETest();
        AppInitializer app = new AppInitializer();
        app.openApp();
        app.email();
        test.enableBluetooth();
        test.scanAndConnectToDevice("Atomberg Device");  // Replace with the actual BLE device name
        test.tearDown();
    }
}

