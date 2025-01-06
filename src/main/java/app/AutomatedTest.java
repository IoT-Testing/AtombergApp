package app;

import app.Lock.LockManagement;
import app.WaterPurifier.ROManagement;
import app.util.ActionsUtil;
import app.util.ScreenRecording;
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
        ScreenRecording.start();
        AppInitializer appInitializer = new AppInitializer();
        //TODO: do not use "openApp()" if "initializeDriver()" is used.
        appInitializer.openApp();
        atomberg = appInitializer.getDriver();
        ActionsUtil.SSleep(2);
        //TODO: Use tapOpAppLogo() if you are using initializeDriver()
        appInitializer.checkMainScreen();
        ScreenRecording.stop();
    }
}