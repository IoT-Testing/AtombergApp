package app;

import app.Analytics.Analytics;
import app.STF.Connect;
import io.appium.java_client.AppiumDriver;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class AutomatedTest {

    public AppiumDriver driver;

    public void run() throws IOException, UnsupportedFlavorException {
        Connect connect = new Connect();
        connect.ipAddress();
        String command = connect.copiedText;
        Process process = Runtime.getRuntime().exec(command);
        AppInitializer appInitializer = new AppInitializer();
        //TODO: do not use "openApp()" if "initializeDriver()" is used.
        appInitializer.initializeDriver();
        driver = appInitializer.getDriver();
        //TODO: Use tapOpAppLogo() if you are using initializeDriver()
        appInitializer.tapOnAppLogo();
        appInitializer.checkMainScreen();
        Analytics analytics = new Analytics(driver);
        analytics.Show();
   }
}