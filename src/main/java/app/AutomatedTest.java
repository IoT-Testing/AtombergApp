package app;

import io.appium.java_client.AppiumDriver;


public class AutomatedTest {

    public AppiumDriver driver;
    public void run() {
        AppInitializer appInitializer = new AppInitializer();
        appInitializer.openApp();
        driver = appInitializer.getDriver();
        appInitializer.checkMainScreen();
        Login login = new Login(driver);
        login.email();
    }
}
