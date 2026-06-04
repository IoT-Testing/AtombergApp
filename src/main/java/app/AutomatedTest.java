package app;

import io.appium.java_client.android.AndroidDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class AutomatedTest {

    public AndroidDriver atomberg;
    public AndroidDriver driver;

    public void run() throws MalformedURLException {
        AppInitializer app = new AppInitializer();
        System.out.println("Appium Server Started on URL: " + app.service.getUrl());
        app.initializeDriver();
        app.stopServer();
    }
}