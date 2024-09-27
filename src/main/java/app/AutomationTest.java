package app;

import app.Fan.FanManagement;
import app.Fan.FanModels;
import app.util.ActionsUtil;
import app.util.AppUtil;
import io.appium.java_client.AppiumDriver;

public class AutomationTest {

    public AppiumDriver driver;

    public void run() {
        AppInitializer appInitializer = new AppInitializer();
        appInitializer.openApp();
        driver = appInitializer.getDriver();
        appInitializer.checkMainScreen();
        appInitializer.login();
        FanManagement fanManagement = new FanManagement(driver);
        fanManagement.checkFanOnline(driver);
        ActionsUtil.sleep(5000);
    }
}
