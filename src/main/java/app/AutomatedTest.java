package app;

import app.Analytics.Analytics;
import io.appium.java_client.AppiumDriver;

public class AutomatedTest {

    public AppiumDriver driver;

    public void run() {
        AppInitializer appInitializer = new AppInitializer();
        //TODO: do not use openApp if initializeDriver() is used.
//        appInitializer.openApp();
        // TODO: Use initializeDriver() in case you don't want to open the app directly
        appInitializer.initializeDriver();
//        appInitializer.initializeApkFile();
        driver = appInitializer.getDriver();
        //TODO: Use tapOpAppLogo() if you are using initializeDriver()
        appInitializer.tapOnAppLogo();
//        appInitializer.checkMainScreen();// checkOnMainScreen is a check for login screen, if login screen then login else skip
//
//        //what to test is to be entered below.
//        Analytics analytics = new Analytics(driver);
//        analytics.Show();

        //Optional.
        driver.navigate().back();
   }
}