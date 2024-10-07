package app;

import app.util.ActionsUtil;
import app.Automations.Automation;
import io.appium.java_client.AppiumDriver;

public class AutomatedTest {

    public AppiumDriver driver;

    public void run() {
        AppInitializer appInitializer = new AppInitializer();
        appInitializer.openApp();
        driver = appInitializer.getDriver();
        appInitializer.checkMainScreen();
        appInitializer.login();
        SwitchFamily family = new SwitchFamily(driver);
        family.switchFamily();
    }
}
