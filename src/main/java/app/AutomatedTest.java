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
import java.net.URL;

import static io.appium.java_client.android.NetworkSpeed.LTE;

public class AutomatedTest {

    public AndroidDriver atomberg;
    public AndroidDriver driver;

    public void run(){
        AppInitializer app = new AppInitializer();
        app.startServer();
        System.out.println("Appium Server Started on URL: " + app.service.getUrl());
        app.initializeDriverWithURL();
        app.stopServer();
    }
}