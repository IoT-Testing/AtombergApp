package app.util;

import app.AppInitializer;
import io.appium.java_client.AppiumDriver;

public class AutomationTest {

    public AppiumDriver driver;

    public void run() {
        AppInitializer appInitializer = new AppInitializer();
        appInitializer.openApp();
        driver = appInitializer.getDriver();
        appInitializer.checkMainScreen();
        appInitializer.login();
    }
}
