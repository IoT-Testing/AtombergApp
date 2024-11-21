package app;

import app.ScreenCheck.CreateWidget;
import app.ScreenCheck.ScreenCheck;
import io.appium.java_client.AppiumDriver;

public class AutomatedTest {

    public AppiumDriver driver;

    public void run() {
        AppInitializer appInitializer = new AppInitializer();
        appInitializer.initializeDriver();
        driver = appInitializer.getDriver();
        appInitializer.checkMainScreen();
        CreateWidget widget = new CreateWidget(driver);
        widget.createWidget2();
        driver.navigate().back();
   }
}