package app;

import app.Lock.LockManagement;
import app.WaterPurifier.ROManagement;
import app.util.ActionsUtil;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.appmanagement.ApplicationState;
import org.openqa.selenium.By;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static io.appium.java_client.android.NetworkSpeed.LTE;

public class AutomatedTest {

    public AndroidDriver atomberg;
    public AndroidDriver driver;

    public void run(){
        try {
            Process process = Runtime.getRuntime().exec("adb shell getprop ro.product.marketname");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String marketName = reader.readLine();
            process.waitFor();
            System.out.println((marketName != null && !marketName.isEmpty() ? marketName : "Not available"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}